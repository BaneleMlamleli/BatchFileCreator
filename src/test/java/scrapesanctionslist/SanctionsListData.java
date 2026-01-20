package scrapesanctionslist;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import util.DBConnection;
import util.ReadAndWriteToFile;
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
    String partyType = "Entity"; // Individual
    public static Logger logger = LogManager.getLogger(new Object() {
    }.getClass().getName());
    Faker faker = new Faker();

    @Test
    public void launchAndGetPartyData(){
        driver = new FirefoxDriver();
        // driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://sanctionssearch.ofac.treas.gov/");

        ReadAndWriteToFile.deleteFileAtInitialExecution();
        
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
            for (int a = 2; a <= 3; a++) {
                String partyCountry = "";
                try {
                    // 2 - Select first country
                    selectCountry.selectByIndex(a);
                    partyCountry = selectCountry.getFirstSelectedOption().getText();
                } catch (StaleElementReferenceException e) {
                    selectCountry = new Select(driver.findElement(By.id("ctl00_MainContent_ddlCountry")));
                    // 2 - Select first country
                    selectCountry.selectByIndex(a);
                    partyCountry = selectCountry.getFirstSelectedOption().getText();
                }
                // 3 - Click on the 'Search' button
                driver.findElement(By.id("ctl00_MainContent_btnSearch")).click();
                // Sum of all countries
                // int allCountries = selectCountry.getOptions().size();
                // Sum of all links for all listed parties for the selected country 
                By linksLocator = By.xpath("//table[@id='gvSearchResults']//tr//td//a");
                List<WebElement> currentLinks = driver.findElements(linksLocator);
                int sumOfLinks = driver.findElements(linksLocator).size()-1;

                // This condition caters for a scenario where there is no links and this message is displayed 'Your search has not returned any results.' 
                if (sumOfLinks == 0) {
                    continue;
                }

                // 4 - Loop through the links to display each party details
                int increment = 0;
                while (increment <= sumOfLinks) {
                    try {
                        // 5 - Click on each link to get the party information
                        currentLinks.get(increment).click();

                        // Check amount of div to make sure all divs that have required data are available
                        List<WebElement> divs = driver.findElements(By.xpath("//div[@id='mainContentBox']//div[@class='groupedContent']//div[@class='content']//div"));
                        // System.out.println("Size of divs: " + divs.size());

                        // If the 'Identifications' information box is not displayed then skip that party
                        if (!divs.get(2).getText().equals("Identifications:")) {
                            continue;
                        }
                        
                        // 6 - Get the party name
                        switch (partyType) {
                            case "Individual":
                                String name = driver.findElement(By.id("ctl00_MainContent_lblFirstName")).getText();
                                String srname = driver.findElement(By.id("ctl00_MainContent_lblLastName")).getText();
                                String entIdType = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[1]")).getText();
                                System.out.println(increment+". Individual Name and Surname: " + name + srname + " - Entity ID: " + entIdType);
                                createIndividualData(partyCountry, currentLinks);
                                // 7 - Click on the 'Back' button
                                driver.findElement(By.xpath("//input[@id='ctl00_MainContent_btnBack']")).click();
                                break;
                            case "Entity":
                                String entityName = driver.findElement(By.id("ctl00_MainContent_lblNameOther")).getText();
                                String entityId = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//*//tr//td[2]")).getText();
                                System.out.println(increment+". Entity Name: " + entityName + " - Entity ID: " + entityId);
                                createEntityData(partyCountry, currentLinks);
                                // 7 - Click on the 'Back' button
                                driver.findElement(By.xpath("//input[@id='ctl00_MainContent_btnBack']")).click();    
                                break;
                            default: logger.error("The select option does not exist, only 'Individual' or 'Entity' can be selected. Issue in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");break;
                        }
                        // String entityName = driver.findElement(By.id("ctl00_MainContent_lblNameOther")).getText();
                        // String entityId = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//*//tr//td[2]")).getText();
                        // System.out.println(increment+". Entity Name: " + entityName + " - Entity ID: " + entityId);
                        // // 7 - Click on the 'Back' button
                        // driver.findElement(By.xpath("//input[@id='ctl00_MainContent_btnBack']")).click();
                    } catch (StaleElementReferenceException e) {
                        currentLinks = driver.findElements(linksLocator);
                        currentLinks.get(increment).click();
                        // 6 - Get the party name
                        switch (partyType) {
                            case "Individual":
                                String name = driver.findElement(By.id("ctl00_MainContent_lblFirstName")).getText();
                                String srname = driver.findElement(By.id("ctl00_MainContent_lblLastName")).getText();
                                String entIdType = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[1]")).getText();
                                System.out.println(increment+". Individual Name and Surname: " + name + srname + " - Entity ID: " + entIdType);
                                createIndividualData(partyCountry, currentLinks);
                                // 7 - Click on the 'Back' button
                                driver.findElement(By.xpath("//input[@id='ctl00_MainContent_btnBack']")).click();
                                break;
                            case "Entity":
                                String entityName = driver.findElement(By.id("ctl00_MainContent_lblNameOther")).getText();
                                String entityId = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//*//tr//td[2]")).getText();
                                System.out.println(increment+". Entity Name: " + entityName + " - Entity ID: " + entityId);
                                createEntityData(partyCountry, currentLinks);
                                // 7 - Click on the 'Back' button
                                driver.findElement(By.xpath("//input[@id='ctl00_MainContent_btnBack']")).click();    
                                break;
                            default: logger.error("The select option does not exist, only 'Individual' or 'Entity' can be selected. Issue in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");break;
                        }
                        // String entityName = driver.findElement(By.id("ctl00_MainContent_lblNameOther")).getText();
                        // System.out.println(increment+". Entity Name: " + entityName + " - size: " + currentLinks.size());
                        // driver.findElement(By.xpath("//input[@id='ctl00_MainContent_btnBack']")).click();
                    }
                    increment += 1;
                }
                System.out.println("===============================================================================");
            }
        }
        terminateBrowser();
        DBConnection.writeDataIntoDb();
        System.out.println("END.....FINISHED DATA SCRAPTING....\n=============================");

    }

    public void createEntityData(String selectedCountry, List<WebElement> currentLinks){
        System.out.println("IN THE createEntityData METHOD");
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(By.id("ctl00_MainContent_lblNameOther")));

        String entityName = driver.findElement(By.id("ctl00_MainContent_lblNameOther")).getText();
        System.out.println("Entity Name: " + entityName);

        String idType = "", id = "", entityCountry = "", address = "", city = "", stateOrProvince = "", postalCode = "", addressCountry = null;

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
            // idType = "NIT No.";
            // id = faker.bothify("??######");
            // entityCountry = "Colombia"; // defaulting to Colombia since it has a lot of alerting parties
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
            // address = faker.address().fullAddress();;
            // city = faker.address().city();
            // stateOrProvince = faker.address().state();
            // postalCode = faker.address().zipCode();
            // addressCountry = "Colombia";
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            logger.error("Table or element for identification information is not in the DOM at all! in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
        }

        String dbEntityCountryCode = DBConnection.returnCountryCode(entityCountry);
        String dbSelectedCountryCode = DBConnection.returnCountryCode(selectedCountry);
        
        boolean partyAlert = true;
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
        String nationality2 = (entityCountry.length() != 0)? dbEntityCountryCode : dbSelectedCountryCode;
        String nationality3 = (entityCountry.length() != 0)? dbEntityCountryCode : dbSelectedCountryCode;
        String passport = null;
        String passportCountry = null;
        String taxRegistrationNumber = faker.number().digits(10);
        String primaryTaxResidence = (entityCountry.length() != 0)? dbEntityCountryCode : dbSelectedCountryCode;
        String additionalTaxResidence = (entityCountry.length() != 0)? dbEntityCountryCode : dbSelectedCountryCode;
        String foreignTin = faker.number().digits(10);
        String foreignTinIssuingCountry = (entityCountry.length() != 0)? dbEntityCountryCode : dbSelectedCountryCode;
        // -- relationship
        String reasonForTransaction = "AD HOC PAYMENT CLAIM"; // Default value will be used when generating the batch file
        String productType = "ADS"; // not using this column as values are in the dropdown
        String riskClass = "L"; // not using this column as values are in the dropdown
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
        String registrationNumber = id;
        String strPartyType = "L";
        String dateOfRegistration = LocalDate.now().minusYears((int) (Math.random() * 10) + 1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String countryOfRegistration = dbEntityCountryCode;
        String industryType = "ACCOUNTING SERVICES";
        String vatRegistrationNumber = faker.number().digits(10);
        // --NP address information
        String npResidentialAddress = null;
        String npPostalAddress = null;
        String npPoboxAddress = null;
        // -- LE address information
        String lePostalAddress = address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode;
        String lePoboxAddress = "P.O Box " + address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode;
        String leRegisteredAddress = address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode;
        String leGcoheadofficeAddress = address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode;
        String leOperationalAddress  = address +" "+ city +" "+ stateOrProvince +" "+ addressCountry +" "+ postalCode;
        
        boolean gender[] = {true, false};
        long startEpoch = LocalDate.now().minusYears(100).toEpochDay();
        long endEpoch = LocalDate.now().minusYears(18).toEpochDay();
        long randomEpochDay = java.util.concurrent.ThreadLocalRandom.current().nextLong(startEpoch, endEpoch + 1);
        LocalDate randomDob = LocalDate.ofEpochDay(randomEpochDay);
        int yearOfBirth = randomDob.getYear();
        int monthOfBirth = randomDob.getMonthValue();
        int dayOfBirth = randomDob.getDayOfMonth();

        String SAIDNumber = null;
        
        LocalDate dob = LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth);
        String dateOfBirth = null;

        String partyGenderFromID = null;

        try {
            Path csvPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "scraped_entities.csv");
            Files.createDirectories(csvPath.getParent());
            boolean csvExists = Files.exists(csvPath);

            String[] headers = {
                "strPartyType","partyAlert","firstname","surname","middleName","previousSurname","dateOfBirth","countryOBirth","nationality","countryOfResidence","partyGenderFromID","profession","monthlyIncome","dateOfLastIncome","SAIDNumber","nationality2","nationality3","passport","passportCountry","taxRegistrationNumber","primaryTaxResidence","foreignTin","foreignTinIssuingCountry","reasonForTransaction","productType","riskClass","businessRelationship","sourceOfFunds","accountNumber","transactionAmount","transactionDate","inceptionDate","authorisedBy","terminationDate","registeredName","registrationNumber","dateOfRegistration","countryOfRegistration","industryType","additionalTaxResidence","vatRegistrationNumber","npResidentialAddress","npPostalAddress","npPoboxAddress","lePostalAddress","lePoboxAddress","leRegisteredAddress","leGcoheadofficeAddress","leOperationalAddress"
            };

            String[] values = {
                String.valueOf(strPartyType),
                String.valueOf(partyAlert),
                firstname == null ? "" : firstname,
                surname == null ? "" : surname,
                middleName == null ? "" : middleName,
                previousSurname == null ? "" : previousSurname,
                dateOfBirth == null ? "" : dateOfBirth,
                countryOBirth == null ? "" : countryOBirth,
                nationality == null ? "" : nationality,
                countryOfResidence == null ? "" : countryOfResidence,
                partyGenderFromID == null ? "" : partyGenderFromID,
                profession == null ? "" : profession,
                String.valueOf(monthlyIncome),
                dateOfLastIncome == null ? "" : dateOfLastIncome,
                SAIDNumber == null ? "" : SAIDNumber,
                nationality2 == null ? "" : nationality2,
                nationality3 == null ? "" : nationality3,
                passport == null ? "" : passport,
                passportCountry == null ? "" : passportCountry,
                taxRegistrationNumber == null ? "" : taxRegistrationNumber,
                primaryTaxResidence == null ? "" : primaryTaxResidence,
                foreignTin == null ? "" : foreignTin,
                foreignTinIssuingCountry == null ? "" : foreignTinIssuingCountry,
                reasonForTransaction == null ? "" : reasonForTransaction,
                productType == null ? "" : productType,
                riskClass == null ? "" : riskClass,
                businessRelationship == null ? "" : businessRelationship,
                sourceOfFunds == null ? "" : sourceOfFunds,
                accountNumber == null ? "" : accountNumber,
                String.valueOf(transactionAmount),
                transactionDate == null ? "" : transactionDate,
                inceptionDate == null ? "" : inceptionDate,
                authorisedBy == null ? "" : authorisedBy,
                terminationDate == null ? "" : terminationDate,
                registeredName == null ? "" : registeredName,
                registrationNumber == null ? "" : registrationNumber,
                dateOfRegistration == null ? "" : dateOfRegistration,
                countryOfRegistration == null ? "" : countryOfRegistration,
                industryType == null ? "" : industryType,
                additionalTaxResidence == null ? "" : additionalTaxResidence,
                vatRegistrationNumber == null ? "" : vatRegistrationNumber,
                npResidentialAddress == null ? "" : npResidentialAddress,
                npPostalAddress == null ? "" : npPostalAddress,
                npPoboxAddress == null ? "" : npPoboxAddress,
                lePostalAddress == null ? "" : lePostalAddress,
                lePoboxAddress == null ? "" : lePoboxAddress,
                leRegisteredAddress == null ? "" : leRegisteredAddress,
                leGcoheadofficeAddress == null ? "" : leGcoheadofficeAddress,
                leOperationalAddress == null ? "" : leOperationalAddress
            };

            StandardOpenOption[] options = new StandardOpenOption[] {
                StandardOpenOption.CREATE, StandardOpenOption.APPEND
            };

            if (!csvExists) {
                StringBuilder headerLine = new StringBuilder();
                for (int h = 0; h < headers.length; h++) {
                    headerLine.append('"').append(headers[h].replace("\"", "\"\"")).append('"');
                    if (h < headers.length - 1) headerLine.append(',');
                }
                headerLine.append(System.lineSeparator());
                Files.write(csvPath, headerLine.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8), options);
            }

            StringBuilder line = new StringBuilder();
            for (int v = 0; v < values.length; v++) {
                String cell = values[v] == null ? "" : values[v];
                cell = cell.replace("\"", "\"\"");
                line.append('"').append(cell).append('"');
                if (v < values.length - 1) line.append(',');
            }
            line.append(System.lineSeparator());
            Files.write(csvPath, line.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8), options);
        } catch (Exception e) {
            logger.error("Failed to write scraped entity CSV '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            e.getMessage();
        }
    }

    public void createIndividualData(String selectedCountry, List<WebElement> currentLinks){
        System.out.println("IN THE createIndividualData METHOD");
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
            entIdType = "NIT No.";
            entId = faker.bothify("??######");
            entCountry = "Colombia";
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
            entAddress = faker.address().fullAddress();
            entCity = faker.address().city();
            entStateOrProvince = faker.address().city();
            entPostalCode = faker.address().zipCode();
            entAddressCountry = faker.address().country();
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            logger.error("Table or element for identification information is not in the DOM at all! in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            e.getMessage();
        }

        String np_dbSelectedCountryCode = DBConnection.returnCountryCode(selectedCountry);


        boolean np_partyAlert = true;
        String np_firstname = name;
        String np_surname = srname;
        String np_middleName = null;
        String np_previousSurname = null;
        String np_countryOBirth = (citizenshp.length() != 0)? DBConnection.returnCountryCode(citizenshp) : np_dbSelectedCountryCode;
        String np_nationality = (nationalty.length() != 0)? DBConnection.returnCountryCode(nationalty) : np_dbSelectedCountryCode;
        String np_countryOfResidence = (entCountry.length() != 0)? DBConnection.returnCountryCode(nationalty) : np_dbSelectedCountryCode;
        String np_profession = faker.job().title();
        long np_monthlyIncome = faker.number().numberBetween(100, 10000);
        String np_dateOfLastIncome = LocalDate.now().minusDays((int)(Math.random()*10)).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String np_nationality2 = (entCountry.length() != 0)? DBConnection.returnCountryCode(entCountry) : np_dbSelectedCountryCode;
        String np_nationality3 = (entCountry.length() != 0)? DBConnection.returnCountryCode(entCountry) : np_dbSelectedCountryCode;
        String np_passport = entId;
        String np_passportCountry = (entCountry.length() != 0)? DBConnection.returnCountryCode(entCountry) : np_dbSelectedCountryCode;
        String np_taxRegistrationNumber = faker.number().digits(10);
        String np_primaryTaxResidence = (entCountry.length() != 0)? DBConnection.returnCountryCode(entCountry) : np_dbSelectedCountryCode;
        String np_additionalTaxResidence = (entCountry.length() != 0)? DBConnection.returnCountryCode(entCountry) : np_dbSelectedCountryCode;
        String np_foreignTin = faker.number().digits(10);
        String np_foreignTinIssuingCountry = (entCountry.length() != 0)? DBConnection.returnCountryCode(entCountry) : np_dbSelectedCountryCode;
        // -- relationship
        String np_reasonForTransaction = "ADDITIONAL PREMIUM"; // Default value will be used when generating the batch file
        String np_productType = "ADS"; // not using this column as values are in the dropdown
        String np_riskClass = "L"; // not using this column as values are in the dropdown
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
        String np_leRegisteredAddress =faker.address().fullAddress();
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

        String np_SAIDNumber = SAIDNumberGenerator.generateSAID(LocalDate.of(np_yearOfBirth, np_monthOfBirth, np_dayOfBirth), np_gender[(int) (Math.random() * 1) + 0], true);
        
        LocalDate np_dob = LocalDate.of(np_yearOfBirth, np_monthOfBirth, np_dayOfBirth);
        String np_dateOfBirth = np_dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String np_partyGenderFromID = Integer.parseInt(np_SAIDNumber.substring(6, 10)) < 5000 ? "F": "M";

        try {
            Path csvPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "scraped_individuals.csv");
            Files.createDirectories(csvPath.getParent());
            boolean csvExists = Files.exists(csvPath);

            String[] headers = {
                "np_partyType","np_partyAlert","np_firstname","np_surname","np_middleName","np_previousSurname","np_dateOfBirth","np_countryOBirth","np_nationality","np_countryOfResidence","np_partyGenderFromID","np_profession","np_monthlyIncome","np_dateOfLastIncome","np_SAIDNumber","np_nationality2","np_nationality3","np_passport","np_passportCountry","np_taxRegistrationNumber","np_primaryTaxResidence","np_foreignTin","np_foreignTinIssuingCountry","np_reasonForTransaction","np_productType","np_riskClass","np_businessRelationship","np_sourceOfFunds","np_accountNumber","np_transactionAmount","np_transactionDate","np_inceptionDate","np_authorisedBy","np_terminationDate","np_registeredName","np_registrationNumber","np_dateOfRegistration","np_countryOfRegistration","np_industryType","np_additionalTaxResidence","np_vatRegistrationNumber","np_npResidentialAddress","np_npPostalAddress","np_npPoboxAddress","np_lePostalAddress","np_lePoboxAddress","np_leRegisteredAddress","np_leGcoheadofficeAddress","np_leOperationalAddress"
            };

            String[] values = {
                String.valueOf(np_partyType),
                String.valueOf(np_partyAlert),
                np_firstname == null ? "" : np_firstname,
                np_surname == null ? "" : np_surname,
                np_middleName == null ? "" : np_middleName,
                np_previousSurname == null ? "" : np_previousSurname,
                np_dateOfBirth == null ? "" : np_dateOfBirth,
                np_countryOBirth == null ? "" : np_countryOBirth,
                np_nationality == null ? "" : np_nationality,
                np_countryOfResidence == null ? "" : np_countryOfResidence,
                np_partyGenderFromID == null ? "" : np_partyGenderFromID,
                np_profession == null ? "" : np_profession,
                String.valueOf(np_monthlyIncome),
                np_dateOfLastIncome == null ? "" : np_dateOfLastIncome,
                np_SAIDNumber == null ? "" : np_SAIDNumber,
                np_nationality2 == null ? "" : np_nationality2,
                np_nationality3 == null ? "" : np_nationality3,
                np_passport == null ? "" : np_passport,
                np_passportCountry == null ? "" : np_passportCountry,
                np_taxRegistrationNumber == null ? "" : np_taxRegistrationNumber,
                np_primaryTaxResidence == null ? "" : np_primaryTaxResidence,
                np_foreignTin == null ? "" : np_foreignTin,
                np_foreignTinIssuingCountry == null ? "" : np_foreignTinIssuingCountry,
                np_reasonForTransaction == null ? "" : np_reasonForTransaction,
                np_productType == null ? "" : np_productType,
                np_riskClass == null ? "" : np_riskClass,
                np_businessRelationship == null ? "" : np_businessRelationship,
                np_sourceOfFunds == null ? "" : np_sourceOfFunds,
                np_accountNumber == null ? "" : np_accountNumber,
                String.valueOf(np_transactionAmount),
                np_transactionDate == null ? "" : np_transactionDate,
                np_inceptionDate == null ? "" : np_inceptionDate,
                np_authorisedBy == null ? "" : np_authorisedBy,
                np_terminationDate == null ? "" : np_terminationDate,
                np_registeredName == null ? "" : np_registeredName,
                np_registrationNumber == null ? "" : np_registrationNumber,
                np_dateOfRegistration == null ? "" : np_dateOfRegistration,
                np_countryOfRegistration == null ? "" : np_countryOfRegistration,
                np_industryType == null ? "" : np_industryType,
                np_additionalTaxResidence == null ? "" : np_additionalTaxResidence,
                np_vatRegistrationNumber == null ? "" : np_vatRegistrationNumber,
                np_npResidentialAddress == null ? "" : np_npResidentialAddress,
                np_npPostalAddress == null ? "" : np_npPostalAddress,
                np_npPoboxAddress == null ? "" : np_npPoboxAddress,
                np_lePostalAddress == null ? "" : np_lePostalAddress,
                np_lePoboxAddress == null ? "" : np_lePoboxAddress,
                np_leRegisteredAddress == null ? "" : np_leRegisteredAddress,
                np_leGcoheadofficeAddress == null ? "" : np_leGcoheadofficeAddress,
                np_leOperationalAddress == null ? "" : np_leOperationalAddress
            };

            StandardOpenOption[] options = new StandardOpenOption[] {
                StandardOpenOption.CREATE, StandardOpenOption.APPEND
            };

            if (!csvExists) {
                StringBuilder headerLine = new StringBuilder();
                for (int h = 0; h < headers.length; h++) {
                    headerLine.append('"').append(headers[h].replace("\"", "\"\"")).append('"');
                    if (h < headers.length - 1) headerLine.append(',');
                }
                headerLine.append(System.lineSeparator());
                Files.write(csvPath, headerLine.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8), options);
            }

            StringBuilder line = new StringBuilder();
            for (int v = 0; v < values.length; v++) {
                String cell = values[v] == null ? "" : values[v];
                cell = cell.replace("\"", "\"\"");
                line.append('"').append(cell).append('"');
                if (v < values.length - 1) line.append(',');
            }
            line.append(System.lineSeparator());
            Files.write(csvPath, line.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8), options);

        } catch (Exception e) {
            logger.error("Failed to write scraped individual CSV '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            e.getMessage();
        }
    }

    @Test
    public void terminateBrowser(){
        driver.close();
        driver.quit();
    }
}
