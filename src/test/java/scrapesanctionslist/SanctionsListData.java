
package scrapesanctionslist;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;

import util.DBConnection;
import util.Utilities;
import util.SAIDNumberGenerator;

/**
 * SanctionsListData
 *
 * <p>Test utility that scrapes party data from the OFAC sanctions search site
 * (https://sanctionssearch.ofac.treas.gov/) using Selenium WebDriver and
 * persists extracted or synthesized party records into a database via
 * util.DBConnection. The class is implemented as TestNG tests and is intended
 * to be executed in a test context (the methods are annotated with @Test).
 *
 * <p>Behavior overview:
 * - Initializes a headless Firefox WebDriver and navigates to the OFAC search
 *   page.
 * - Iterates over the "Type" (Entity / Individual) select and each country in
 *   the "Country" select, performs searches and iterates over result links.
 * - For each result it navigates to the detail view, validates presence of the
 *   expected data blocks and delegates parsing to createEntityData or
 *   createIndividualData depending on selected party type.
 * - Builds a complete party record using values scraped from the page and/or
 *   synthetic values generated via Faker and SAIDNumberGenerator.
 * - Persists party records by calling DBConnection.insertPartyIntoDB(...).
 * - Uses explicit waits, stale element handling and JS fallback clicks to
 *   improve robustness against dynamic DOM updates.
 *
 * <p>Notes and prerequisites:
 * - Requires a configured WebDriver (e.g. geckodriver on PATH) and a reachable
 *   database accessible through util.DBConnection.
 * - Depends on util.Utilities, util.DBConnection, util.SAIDNumberGenerator and
 *   external libraries: Selenium, TestNG, Faker and Log4j.
 * - The scraping run can be long-running and is not optimized for concurrency:
 *   this class is not thread-safe.
 *
 * Public methods:
 *
 * {@link #launchAndGetPartyData()}
 *   - Entry point for the scraping flow (annotated @Test).
 *   - Initializes FirefoxDriver in headless mode, navigates to the sanctions
 *     search page, iterates party types and countries, executes searches and
 *     iterates through result links to collect data.
 *   - Handles common Selenium exceptions (StaleElementReferenceException,
 *     ElementNotInteractableException) by re-fetching elements, waiting for
 *     presence/visibility and using JavaScript click fallbacks.
 *   - Delegates actual record creation to createEntityData and
 *     createIndividualData.
 *   - Side effects: inserts records into DB via DBConnection and logs progress.
 *   - Important: does not close the browser; terminateBrowser() should be run
 *     after completion (or TestNG tearDown) to clean up the WebDriver instance.
 *
 * {@link #createEntityData(String, java.util.List)}
 *   - Extracts entity (legal entity) details from the currently displayed
 *     party detail page.
 *   - Parameters:
 *       selectedCountry - the country selected in the search UI for this run
 *       currentLinks     - the list of result link elements currently used by
 *                         the caller (used for certain special-case logic)
 *   - Attempts to read identification table, address table and other fields.
 *     Where values are missing or for specific known corner cases, synthetic
 *     fallback values are generated using Faker.
 *   - Resolves country codes using DBConnection.returnCountryCode(...) and
 *     populates numerous relationship / LE fields (registration, addresses,
 *     tax identifiers, transaction sample data, etc.).
 *   - Persists a complete LE party record via DBConnection.insertPartyIntoDB(...).
 *   - May throw runtime exceptions from Selenium if expected DOM elements are
 *     absent; callers should treat these as non-fatal for the overall run.
 *
 * {@link #createIndividualData(String, java.util.List)}
 *   - Extracts individual (natural person) details from the currently displayed
 *     party detail page.
 *   - Parameters:
 *       selectedCountry - the country selected in the search UI for this run
 *       currentLinks     - the list of result link elements currently used by
 *                         the caller (used for certain special-case logic)
 *   - Reads first/last name, DOB, nationality/citizenship and identification
 *     and address tables when present.
 *   - Generates synthetic fields when necessary (monthly income, tax IDs,
 *     transaction data) using Faker and generates a SA ID (via
 *     SAIDNumberGenerator) for NP records when appropriate.
 *   - Resolves country codes via DBConnection.returnCountryCode(...) and
 *     assembles a full NP record which is then persisted with
 *     DBConnection.insertPartyIntoDB(...).
 *   - May throw runtime exceptions from Selenium if expected DOM elements are
 *     missing; callers should be prepared to handle or log these situations.
 *
 * {@link #terminateBrowser()}
 *   - Safely closes and quits the WebDriver instance if it has been
 *     initialized.
 *   - Intended to be used as a teardown step after launchAndGetPartyData to
 *     ensure the browser process is terminated and resources are released.
 *
 * Logging:
 * - Uses Log4j (logger) to record errors and progress. Some debug/progress is
 *   also printed to stdout for long-running runs.
 * 
 * STEPS:
 * 1. Open the browser on the home page
 * 2. Type: Select your party type (Entity or Individual)
 * 3. Country: Select the country (193 countries in total)
 * 4. Click on 'Search' button
 * 5. Loop through the list of parties displayed per country
 * 6. Read data for each party
 * 7. Record data in the Database for each party type
 * 8. tables: Parties
 * 
 * @author banele mlamleli
 * @since 1.0
 */

public class SanctionsListData {

    WebDriver driver;
    ChromeOptions chromeOptions;
    FirefoxOptions firefoxOptions;
    String partyType = "Entity"; // Individual
    public static Logger logger = LogManager.getLogger(new Object() {
    }.getClass().getName());
    
    Faker faker = new Faker();

    @SuppressWarnings("null")
    @Test
    public void launchAndGetPartyData(){
        firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--headless");
        driver = new FirefoxDriver(firefoxOptions);
        // Configure ChromeOptions for headless mode
        // chromeOptions = new ChromeOptions();
        // chromeOptions.addArguments("--headless=new"); // Use the modern headless mode
        // driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
        driver.get("https://sanctionssearch.ofac.treas.gov/");
        
        Select selectType = new Select(driver.findElement(By.id("ctl00_MainContent_ddlType")));
        Select selectCountry = new Select(driver.findElement(By.id("ctl00_MainContent_ddlCountry")));
        int allCountries = selectCountry.getOptions().size();

        // This will loop 2 times, It will start at index 2 for 'Entity' and index 3 for 'Individual'
        for (int p = 2; p <= 3; p++) {
            String partyType = "";
            try {
                 // 1 - Select party type
                selectType.selectByIndex(p);
                partyType = selectType.getFirstSelectedOption().getText();
                allCountries = selectCountry.getOptions().size();
            } catch (Exception e) {
                selectType = new Select(driver.findElement(By.id("ctl00_MainContent_ddlType")));
                selectCountry = new Select(driver.findElement(By.id("ctl00_MainContent_ddlCountry")));
                allCountries = selectCountry.getOptions().size();
                 // 1 - Select party type
                selectType.selectByIndex(p);
                partyType = selectType.getFirstSelectedOption().getText();
            }

            // starting at 1 because index 0 will select the 'All' option which is not what we need
            for (int a = 1; a <= allCountries; a++) {
                String partyCountry = "";
                try {
                    // 2 - Select first country
                    selectCountry.selectByIndex(a);
                    partyCountry = selectCountry.getFirstSelectedOption().getText();
                } catch (StaleElementReferenceException e) {
                    WebDriverWait initialWait = new WebDriverWait(driver, Duration.ofSeconds(15));
                    initialWait.until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_MainContent_ddlCountry")));

                    selectCountry = new Select(driver.findElement(By.id("ctl00_MainContent_ddlCountry")));
                    // 2 - Select first country
                    selectCountry.selectByIndex(a);
                    partyCountry = selectCountry.getFirstSelectedOption().getText();
                }
                // 3 - Click on the 'Search' button
                driver.findElement(By.id("ctl00_MainContent_btnSearch")).click();
                By linksLocator = By.xpath("//table[@id='gvSearchResults']//tr//td//a");
                List<WebElement> currentLinks = driver.findElements(linksLocator);
                int linksSize = currentLinks.size();

                // This condition caters for a scenario where there are no links and this message is displayed 'Your search has not returned any results.' 
                if (linksSize == 0) {
                    continue;
                }
                int sumOfLinks = linksSize - 1;

                // 4 - Loop through the links to display each party details
                int increment = 0;
                while (true) {
                    // Re-fetch current links on each iteration to avoid stale or emptied lists
                    currentLinks = driver.findElements(linksLocator);
                    int currentSize = currentLinks.size();
                    if (currentSize == 0 || increment >= currentSize) {
                        break;
                    }

                    try {
                        // 5 - Click on each link to get the party information
                        WebElement link = currentLinks.get(increment);
                        try {
                            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(link)).click();
                        } catch (ElementNotInteractableException enie) {
                            try {
                                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", link);
                                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
                            } catch (Exception jsEx) {
                                logger.error("Failed to click link via JS: " + jsEx.getMessage());
                                throw enie;
                            }
                        }

                        // Check amount of div to make sure all divs that have required data are available
                        List<WebElement> divs = driver.findElements(By.xpath("//div[@id='mainContentBox']//div[@class='groupedContent']//div[@class='content']//div"));

                        // If the 'Identifications' information box is not displayed then skip that party
                        if (divs.size() < 3 || !divs.get(2).getText().equals("Identifications:")) {
                            increment++;
                            driver.findElement(By.xpath("//input[@id='ctl00_MainContent_btnBack']")).click();
                            continue;
                        }
                        
                        // 6 - Get the party information
                        switch (partyType) {
                            case "Individual":
                                createIndividualData(partyCountry, currentLinks);
                                // 7 - Click on the 'Back' button
                                driver.findElement(By.xpath("//input[@id='ctl00_MainContent_btnBack']")).click();
                                break;
                            case "Entity":
                                createEntityData(partyCountry, currentLinks);
                                // 7 - Click on the 'Back' button
                                driver.findElement(By.xpath("//input[@id='ctl00_MainContent_btnBack']")).click();    
                                break;
                            default: logger.error("The select option does not exist, only 'Individual' or 'Entity' can be selected. Issue in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");break;
                        }
                    } catch (StaleElementReferenceException e) {
                        // On stale element, re-fetch and ensure the index is still valid before clicking
                        currentLinks = driver.findElements(linksLocator);
                        if (increment >= currentLinks.size()) {
                            break;
                        }
                        WebElement clickable = currentLinks.get(increment);
                        try {
                            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(clickable)).click();
                        } catch (ElementNotInteractableException enie) {
                            try {
                                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", clickable);
                                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickable);
                            } catch (Exception jsEx) {
                                logger.error("Failed to click link via JS after stale element: " + jsEx.getMessage());
                                throw enie;
                            }
                        }

                        // Check amount of div to make sure all divs that have required data are available
                        List<WebElement> divs = driver.findElements(By.xpath("//div[@id='mainContentBox']//div[@class='groupedContent']//div[@class='content']//div"));
                        // If the 'Identifications' information box is not displayed then skip that party
                        if (divs.size() < 3 || !divs.get(2).getText().equals("Identifications:")) {
                            increment += 1;
                            driver.findElement(By.xpath("//input[@id='ctl00_MainContent_btnBack']")).click();
                            continue;
                        }

                        // 6 - Get the party name
                        switch (partyType) {
                            case "Individual":
                                createIndividualData(partyCountry, currentLinks);
                                // 7 - Click on the 'Back' button
                                driver.findElement(By.xpath("//input[@id='ctl00_MainContent_btnBack']")).click();
                                break;
                            case "Entity":
                                createEntityData(partyCountry, currentLinks);
                                // 7 - Click on the 'Back' button
                                driver.findElement(By.xpath("//input[@id='ctl00_MainContent_btnBack']")).click();    
                                break;
                            default: logger.error("The select option does not exist, only 'Individual' or 'Entity' can be selected. Issue in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");break;
                        }
                    }
                    increment += 1;
                }
                logger.info(partyType + ": " + a + " of " + allCountries + " - " + partyCountry + " DONE\n======================================================");
                // System.out.println(a + " of " + allCountries + " - " + partyCountry + " DONE");
                // System.out.println("=====================================================");
            }
        }
        // DBConnection.writeDataIntoDb();
        System.out.println("END.....FINISHED DATA SCRAPTING....\n=============================");

    }

    public void createEntityData(String selectedCountry, List<WebElement> currentLinks){
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(By.id("ctl00_MainContent_lblNameOther")));

        String entityName = driver.findElement(By.id("ctl00_MainContent_lblNameOther")).getText();
        String idType = "", id = "", entityCountry = "", address = "", city = "", stateOrProvince = "", postalCode = "", addressCountry = "";

        try {
            if (selectedCountry.equalsIgnoreCase("Afghanistan") && currentLinks.get(0).getText().equalsIgnoreCase("ABID ALI KHAN TRANSNATIONAL CRIMINAL ORGANIZATION")){
                idType = "NIT No.";
                id = faker.bothify("??######");
                entityCountry = "Colombia"; // defaulting to Colombia since it has a lot of alerting parties
            }else{
                idType = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[1]")).getText();
                id = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[2]")).getText();
                entityCountry = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[3]")).getText();
            }
        } catch (Exception e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            logger.error("Table or element for identification information is not in the DOM at all! in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
        }

        try {
            if (selectedCountry.equalsIgnoreCase("Afghanistan") && currentLinks.get(0).getText().equalsIgnoreCase("ABID ALI KHAN TRANSNATIONAL CRIMINAL ORGANIZATION")){
                address = faker.address().fullAddress();;
                city = faker.address().city();
                stateOrProvince = faker.address().state();
                postalCode = faker.address().zipCode();
                addressCountry = "Colombia";
            }else{
                address = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[1]")).getText();
                city = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[2]")).getText();
                stateOrProvince = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[3]")).getText();
                postalCode = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[4]")).getText();
                addressCountry = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[5]")).getText();
            }
        } catch (Exception e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            logger.error("Table or element for identification information is not in the DOM at all! in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
        }

        id = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[2]")).getText();

        String dbEntityCountryCode = DBConnection.returnCountryCode(entityCountry);
        String dbSelectedCountryCode = DBConnection.returnCountryCode(selectedCountry);
        
        boolean partyAlert = true;
        boolean partyIsUsed = false;
        String firstname = "";
        String surname = "";
        String middleName = "";
        String previousSurname = "";
        String countryOBirth = "";
        String nationality = "";
        String countryOfResidence = "";
        String profession = "";
        long monthlyIncome = faker.number().numberBetween(100, 10000);  //.randomNumber();
        String dateOfLastIncome = LocalDate.now().minusDays((int)(Math.random()*10)).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String nationality2 = (dbEntityCountryCode != null)? dbEntityCountryCode : dbSelectedCountryCode;
        String nationality3 = (dbEntityCountryCode != null)? dbEntityCountryCode : dbSelectedCountryCode;
        String passport = "";
        String passportCountry = "";
        String taxRegistrationNumber = faker.number().digits(10);
        String primaryTaxResidence = (dbEntityCountryCode != null)? dbEntityCountryCode : dbSelectedCountryCode;
        String additionalTaxResidence = (dbEntityCountryCode != null)? dbEntityCountryCode : dbSelectedCountryCode;
        String foreignTin = faker.number().digits(10);
        String foreignTinIssuingCountry = (dbEntityCountryCode != null)? dbEntityCountryCode : dbSelectedCountryCode;
        // -- relationship
        String reasonForTransaction = "AD HOC PAYMENT CLAIM"; // Default value will be used when generating the batch file
        String productType = "ALT"; // not using this column as values are in the dropdown
        String riskClass = "H"; // not using this column as values are in the dropdown
        String businessRelationship = "Policy Owner"; // Default value will be used when generating the batch file
        String sourceOfFunds = "BUSINESS OPERATING INCOME";
        String accountNumber = faker.number().digits(10);
        int transactionAmount = Integer.parseInt(faker.number().digits(3));
        String transactionDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); 
        String inceptionDate = LocalDate.now().minusYears((int) (Math.random() * 20) + 1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String authorisedBy = faker.name().firstName() +" "+ faker.name().lastName();
        String terminationDate = LocalDate.now().minusYears((int) (Math.random() * 10) + 1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        // --LE details
        String registeredName = entityName;
        String registrationNumber = (id.length() != 0 )? id : faker.number().digits(10);
        String strPartyType = "L";
        String dateOfRegistration = LocalDate.now().minusYears((int) (Math.random() * 10) + 1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String countryOfRegistration = (dbEntityCountryCode != null)? dbEntityCountryCode : dbSelectedCountryCode;
        String industryType = "ACCOUNTING SERVICES";
        String vatRegistrationNumber = faker.number().digits(10);
        // --NP address information
        String npResidentialAddress = "";
        String npPostalAddress = "";
        String npPoboxAddress = "";
        // -- LE address information
        String lePostalAddress = (address.length() != 0)? address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode : faker.address().fullAddress();
        String lePoboxAddress = (address.length() != 0)? "P.O Box " + address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode : faker.address().fullAddress();
        String leRegisteredAddress = (address.length() != 0)? address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode : faker.address().fullAddress();
        String leGcoheadofficeAddress = (address.length() != 0)? address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode : faker.address().fullAddress();
        String leOperationalAddress  = (address.length() != 0)? address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode : faker.address().fullAddress();
        String SAIDNumber = "";
        String dateOfBirth = "";
        String partyGenderFromID = "";

        DBConnection.insertPartyIntoDB(
            strPartyType, partyAlert, partyIsUsed, firstname, surname, middleName, previousSurname, dateOfBirth, countryOBirth, nationality, countryOfResidence, partyGenderFromID, profession, monthlyIncome, dateOfLastIncome, SAIDNumber, nationality2, nationality3, passport, passportCountry, taxRegistrationNumber, primaryTaxResidence, foreignTin, foreignTinIssuingCountry, reasonForTransaction, productType, riskClass, businessRelationship, sourceOfFunds, accountNumber, transactionAmount, transactionDate, inceptionDate, authorisedBy, terminationDate, registeredName, registrationNumber, dateOfRegistration, countryOfRegistration, industryType, additionalTaxResidence, vatRegistrationNumber, npResidentialAddress, npPostalAddress, npPoboxAddress, lePostalAddress, lePoboxAddress, leRegisteredAddress, leGcoheadofficeAddress, leOperationalAddress, ""
        );
    }

    public void createIndividualData(String selectedCountry, List<WebElement> currentLinks){
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(By.id("ctl00_MainContent_lblFirstName")));

        String name = driver.findElement(By.id("ctl00_MainContent_lblFirstName")).getText();
        String srname = driver.findElement(By.id("ctl00_MainContent_lblLastName")).getText();
        String dateOfBrth = driver.findElement(By.id("ctl00_MainContent_lblDOB")).getText();
        String nationalty = driver.findElement(By.id("ctl00_MainContent_lblNationality")).getText();
        String citizenshp = driver.findElement(By.id("ctl00_MainContent_lblCitizenship")).getText();

        String entIdType = "", entId = "", entCountry = "", entAddress = "", entCity = "", entStateOrProvince = "", entPostalCode = "", entAddressCountry = "";

        try {
            entIdType = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[1]")).getText();
            entId = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[2]")).getText();
            entCountry = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[3]")).getText();
        } catch (Exception e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            logger.error("Table or element for identification information is not in the DOM at all! in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            e.getMessage();
        }

        try {
            entAddress = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[1]")).getText();
            entCity = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[2]")).getText();
            entStateOrProvince = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[3]")).getText();
            entPostalCode = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[4]")).getText();
            entAddressCountry = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[5]")).getText();
        } catch (Exception e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            logger.error("Table or element for identification information is not in the DOM at all! in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            e.getMessage();
        }

        String np_dbEntityCountryCode = DBConnection.returnCountryCode(entCountry);
        String np_dbSelectedCountryCode = DBConnection.returnCountryCode(selectedCountry);

        boolean np_partyAlert = true;
        boolean np_partyIsUsed = false;
        String np_firstname = name;
        String np_surname = srname;
        String np_middleName = "";
        String np_previousSurname = "";
        String np_countryOfBirth = (DBConnection.returnCountryCode(citizenshp) != null)? DBConnection.returnCountryCode(citizenshp) : np_dbSelectedCountryCode;
        String np_nationality = (DBConnection.returnCountryCode(nationalty) !=  null)? DBConnection.returnCountryCode(nationalty) : np_dbSelectedCountryCode;
        String np_countryOfResidence = (np_dbEntityCountryCode != null)? np_dbEntityCountryCode : np_dbSelectedCountryCode;
        String np_profession = faker.job().title();
        long np_monthlyIncome = faker.number().numberBetween(100, 10000);
        String np_dateOfLastIncome = LocalDate.now().minusDays((int)(Math.random()*10)).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String np_nationality2 = (np_dbEntityCountryCode != null)? np_dbEntityCountryCode : np_dbSelectedCountryCode;
        String np_nationality3 = (np_dbEntityCountryCode != null)? np_dbEntityCountryCode : np_dbSelectedCountryCode;
        String np_passport = entId;
        String np_passportCountry = (np_dbEntityCountryCode != null)? np_dbEntityCountryCode : np_dbSelectedCountryCode;
        String np_taxRegistrationNumber = faker.number().digits(10);
        String np_primaryTaxResidence = (np_dbEntityCountryCode != null)? np_dbEntityCountryCode : np_dbSelectedCountryCode;
        String np_additionalTaxResidence = (np_dbEntityCountryCode != null)? np_dbEntityCountryCode : np_dbSelectedCountryCode;
        String np_foreignTin = faker.number().digits(10);
        String np_foreignTinIssuingCountry = (np_dbEntityCountryCode != null)? np_dbEntityCountryCode : np_dbSelectedCountryCode;
        // -- relationship
        String np_reasonForTransaction = "ADDITIONAL PREMIUM"; // Default value will be used when generating the batch file
        String np_productType = "ALT"; // not using this column as values are in the dropdown
        String np_riskClass = "H"; // not using this column as values are in the dropdown
        String np_businessRelationship = "Policy Owner"; // Default value will be used when generating the batch file
        String np_sourceOfFunds = "ALLOWANCE";
        String np_accountNumber = faker.number().digits(10);
        int np_transactionAmount = Integer.parseInt(faker.number().digits(3));
        String np_transactionDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); 
        String np_inceptionDate = LocalDate.now().minusYears((int) (Math.random() * 20) + 1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String np_authorisedBy = faker.name().firstName() +" "+ faker.name().lastName();
        String np_terminationDate = LocalDate.now().minusYears((int) (Math.random() * 10) + 1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        // --LE details
        String np_registeredName = "";
        String np_registrationNumber = "";
        String np_partyType = "P";
        String np_dateOfRegistration = "";
        String np_countryOfRegistration = "";
        String np_industryType = "ACCOUNTING SERVICES";
        String np_vatRegistrationNumber = "";
        // --NP address information
        String np_npResidentialAddress = entAddress +" "+ entCity +" "+ entStateOrProvince +" "+ entAddressCountry +" "+ entPostalCode;
        String np_npPostalAddress = entAddress +" "+ entCity +" "+ entStateOrProvince +" "+ entAddressCountry +" "+ entPostalCode; 
        String np_npPoboxAddress = "P.O Box" + entAddress +" "+ entCity +" "+ entStateOrProvince +" "+ entAddressCountry +" "+ entPostalCode;
        // -- LE address information
        String np_lePostalAddress = "";
        String np_lePoboxAddress = "";
        String np_leRegisteredAddress = "";
        String np_leGcoheadofficeAddress = "";
        String np_leOperationalAddress  = "";
        
        boolean np_gender[] = {true, false};
        long np_startEpoch = LocalDate.now().minusYears(100).toEpochDay();
        long np_endEpoch = LocalDate.now().minusYears(18).toEpochDay();
        long np_randomEpochDay = java.util.concurrent.ThreadLocalRandom.current().nextLong(np_startEpoch, np_endEpoch + 1);
        LocalDate np_randomDob = LocalDate.ofEpochDay(np_randomEpochDay);
        int np_yearOfBirth = np_randomDob.getYear();
        int np_monthOfBirth = np_randomDob.getMonthValue();
        int np_dayOfBirth = np_randomDob.getDayOfMonth();

        String np_SAIDNumber = SAIDNumberGenerator.generateSAID(LocalDate.of(np_yearOfBirth, np_monthOfBirth, np_dayOfBirth), np_gender[(((int) (Math.random() * 100) + 0) > 50)? 0 : 1], true);
        
        LocalDate np_dob = LocalDate.of(np_yearOfBirth, np_monthOfBirth, np_dayOfBirth);
        String np_dateOfBirth = np_dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String np_partyGenderFromID = Integer.parseInt(np_SAIDNumber.substring(6, 10)) < 5000 ? "F": "M";

        DBConnection.insertPartyIntoDB(np_partyType, np_partyAlert, np_partyIsUsed, np_firstname, np_surname, np_middleName, np_previousSurname, np_dateOfBirth, np_countryOfBirth, np_nationality, np_countryOfResidence, np_partyGenderFromID, np_profession, np_monthlyIncome, np_dateOfLastIncome, np_SAIDNumber, np_nationality2, np_nationality3, np_passport, np_passportCountry, np_taxRegistrationNumber, np_primaryTaxResidence, np_foreignTin, np_foreignTinIssuingCountry, np_reasonForTransaction, np_productType, np_riskClass, np_businessRelationship, np_sourceOfFunds, np_accountNumber, np_transactionAmount, np_transactionDate, np_inceptionDate, np_authorisedBy, np_terminationDate, np_registeredName, np_registrationNumber, np_dateOfRegistration, np_countryOfRegistration, np_industryType, np_additionalTaxResidence, np_vatRegistrationNumber, np_npResidentialAddress, np_npPostalAddress, np_npPoboxAddress, np_lePostalAddress, np_lePoboxAddress, np_leRegisteredAddress, np_leGcoheadofficeAddress, np_leOperationalAddress, "");
    }

    @Test
    public void terminateBrowser(){
        if (driver != null) {            
            driver.close();
            driver.quit();
        }
    }
}
