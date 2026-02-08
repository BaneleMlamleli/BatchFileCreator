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
 *
 * @author banele
 *
 * STEPS:
 * 1. Open the browser on the home page
 * 2. Type: Select your party type (Entity or Individual)
 * 3. Country: Select the country (193 countries in total)
 * 4. Click on 'Search' button
 * 5. Loop through the list of parties displayed per country
 * 6. Read data for each party
 * 7. Record data in the Database for each party type
 *  - 2 tables: Entity and Individual
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

        // Utilities.deleteFileAtInitialExecution();
        
        Select selectType = new Select(driver.findElement(By.id("ctl00_MainContent_ddlType")));
        Select selectCountry = new Select(driver.findElement(By.id("ctl00_MainContent_ddlCountry")));
        int allCountries = selectCountry.getOptions().size();

        // This will loop 2 times, It will start at index 2 for 'Entity' and index 3 for 'Individual'
        for (int p = 3; p <= 3; p++) {
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
            for (int a = 37; a <= allCountries; a++) {
                String partyCountry = "";
                try {
                    // 2 - Select first country
                    selectCountry.selectByIndex(a);
                    partyCountry = selectCountry.getFirstSelectedOption().getText();
                } catch (StaleElementReferenceException e) {
                    // NOTE: I've put this code before going to sleep. I will check it tomorrow
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
        String firstname = null;
        String surname = null;
        String middleName = null;
        String previousSurname = null;
        String countryOBirth = null;
        String nationality = null;
        String countryOfResidence = null;
        String profession = null;
        long monthlyIncome = faker.number().numberBetween(100, 10000);  //.randomNumber();
        String dateOfLastIncome = LocalDate.now().minusDays((int)(Math.random()*10)).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String nationality2 = (dbEntityCountryCode != null)? dbEntityCountryCode : dbSelectedCountryCode;
        String nationality3 = (dbEntityCountryCode != null)? dbEntityCountryCode : dbSelectedCountryCode;
        String passport = null;
        String passportCountry = null;
        String taxRegistrationNumber = faker.number().digits(10);
        String primaryTaxResidence = (dbEntityCountryCode != null)? dbEntityCountryCode : dbSelectedCountryCode;
        String additionalTaxResidence = (dbEntityCountryCode != null)? dbEntityCountryCode : dbSelectedCountryCode;
        String foreignTin = faker.number().digits(10);
        String foreignTinIssuingCountry = (dbEntityCountryCode != null)? dbEntityCountryCode : dbSelectedCountryCode;
        // -- relationship
        String reasonForTransaction = "AD HOC PAYMENT CLAIM"; // Default value will be used when generating the batch file
        String productType = "ALT"; // not using this column as values are in the dropdown
        String riskClass = "H"; // not using this column as values are in the dropdown
        String businessRelationship = "BO"; // Default value will be used when generating the batch file
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
        String npResidentialAddress = null;
        String npPostalAddress = null;
        String npPoboxAddress = null;
        // -- LE address information
        String lePostalAddress = (address.length() != 0)? address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode : faker.address().fullAddress();
        String lePoboxAddress = (address.length() != 0)? "P.O Box " + address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode : faker.address().fullAddress();
        String leRegisteredAddress = (address.length() != 0)? address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode : faker.address().fullAddress();
        String leGcoheadofficeAddress = (address.length() != 0)? address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode : faker.address().fullAddress();
        String leOperationalAddress  = (address.length() != 0)? address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode : faker.address().fullAddress();
        String SAIDNumber = null;
        String dateOfBirth = null;
        String partyGenderFromID = null;

        DBConnection.insertPartyIntoDB(
            strPartyType, partyAlert, partyIsUsed, firstname, surname, middleName, previousSurname, dateOfBirth, countryOBirth, nationality, countryOfResidence, partyGenderFromID, profession, monthlyIncome, dateOfLastIncome, SAIDNumber, nationality2, nationality3, passport, passportCountry, taxRegistrationNumber, primaryTaxResidence, foreignTin, foreignTinIssuingCountry, reasonForTransaction, productType, riskClass, businessRelationship, sourceOfFunds, accountNumber, transactionAmount, transactionDate, inceptionDate, authorisedBy, terminationDate, registeredName, registrationNumber, dateOfRegistration, countryOfRegistration, industryType, additionalTaxResidence, vatRegistrationNumber, npResidentialAddress, npPostalAddress, npPoboxAddress, lePostalAddress, lePoboxAddress, leRegisteredAddress, leGcoheadofficeAddress, leOperationalAddress
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

        String entIdType = "", entId = "", entCountry = "", entAddress = "", entCity = "", entStateOrProvince = "", entPostalCode = "", entAddressCountry = null;

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
        String np_middleName = null;
        String np_previousSurname = null;
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
        String np_businessRelationship = "BO"; // Default value will be used when generating the batch file
        String np_sourceOfFunds = "ALLOWANCE";
        String np_accountNumber = faker.number().digits(10);
        int np_transactionAmount = Integer.parseInt(faker.number().digits(3));
        String np_transactionDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); 
        String np_inceptionDate = LocalDate.now().minusYears((int) (Math.random() * 20) + 1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String np_authorisedBy = faker.name().firstName() +" "+ faker.name().lastName();
        String np_terminationDate = LocalDate.now().minusYears((int) (Math.random() * 10) + 1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        // --LE details
        String np_registeredName = null;
        String np_registrationNumber = null;
        String np_partyType = "N";
        String np_dateOfRegistration = null;
        String np_countryOfRegistration = null;
        String np_industryType = null;
        String np_vatRegistrationNumber = null;
        // --NP address information
        String np_npResidentialAddress = entAddress +" "+ entCity +" "+ entStateOrProvince +" "+ entAddressCountry +" "+ entPostalCode;
        String np_npPostalAddress = entAddress +" "+ entCity +" "+ entStateOrProvince +" "+ entAddressCountry +" "+ entPostalCode; 
        String np_npPoboxAddress = "P.O Box" + entAddress +" "+ entCity +" "+ entStateOrProvince +" "+ entAddressCountry +" "+ entPostalCode;
        // -- LE address information
        String np_lePostalAddress = null;
        String np_lePoboxAddress = null;
        String np_leRegisteredAddress = null;
        String np_leGcoheadofficeAddress = null;
        String np_leOperationalAddress  = null;
        
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

        DBConnection.insertPartyIntoDB(np_partyType, np_partyAlert, np_partyIsUsed, np_firstname, np_surname, np_middleName, np_previousSurname, np_dateOfBirth, np_countryOfBirth, np_nationality, np_countryOfResidence, np_partyGenderFromID, np_profession, np_monthlyIncome, np_dateOfLastIncome, np_SAIDNumber, np_nationality2, np_nationality3, np_passport, np_passportCountry, np_taxRegistrationNumber, np_primaryTaxResidence, np_foreignTin, np_foreignTinIssuingCountry, np_reasonForTransaction, np_productType, np_riskClass, np_businessRelationship, np_sourceOfFunds, np_accountNumber, np_transactionAmount, np_transactionDate, np_inceptionDate, np_authorisedBy, np_terminationDate, np_registeredName, np_registrationNumber, np_dateOfRegistration, np_countryOfRegistration, np_industryType, np_additionalTaxResidence, np_vatRegistrationNumber, np_npResidentialAddress, np_npPostalAddress, np_npPoboxAddress, np_lePostalAddress, np_lePoboxAddress, np_leRegisteredAddress, np_leGcoheadofficeAddress, np_leOperationalAddress);
    }

    @Test
    public void terminateBrowser(){
        if (driver != null) {            
            driver.close();
            driver.quit();
        }
    }
}
