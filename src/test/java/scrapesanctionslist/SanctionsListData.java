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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import util.DBConnection;
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
    FirefoxOptions options;
    public static Logger logger = LogManager.getLogger(new Object() {
    }.getClass().getName());

    Faker faker = new Faker();

    @SuppressWarnings("null")
    @Test
    @Parameters({"partyType"})
    public void scrapeWebsite(@Optional("Entity") String partyType){
        // Configure Firefox options for headless mode
        options = new FirefoxOptions();
                // Recommended modern approach using addArguments
        options.addArguments("--headless");
        driver = new FirefoxDriver(options);
        driver.get("https://sanctionssearch.ofac.treas.gov/");
        driver.manage().window().maximize();

        // Always delete file at initial execution to prevent duplication 
        try {
            Path entitiesFileToDelete = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "scraped_entities.csv");
            if (Files.exists(entitiesFileToDelete)) {
                Files.delete(entitiesFileToDelete);
                System.out.println("Deleted existing scraped_entities.csv");
            }

            Path individualsFileToDelete = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "scraped_individuals.csv");
            if (Files.exists(individualsFileToDelete)) {
                Files.delete(individualsFileToDelete);
                System.out.println("Deleted existing scraped_individuals.csv");
            }
            
        } catch (IOException e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "'");
        }

        
        Select selectType = new Select(driver.findElement(By.id("ctl00_MainContent_ddlType")));
        Select selectCountry = new Select(driver.findElement(By.id("ctl00_MainContent_ddlCountry")));

        selectType.selectByValue(partyType);
        
        System.out.println("Total countries: " + selectCountry.getOptions().size());
        
        int allCountries = selectCountry.getOptions().size();

        for (int a = 1; a < allCountries; a++) { // starting at 1 because index 0 will select the 'All' option which is not what we need
           
            new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("ctl00_MainContent_ddlCountry")));
            
            Select selectCountries = new Select(driver.findElement(By.id("ctl00_MainContent_ddlCountry")));
            selectCountries.selectByIndex(a); // select country
            String selectedCountry = selectCountries.getFirstSelectedOption().getText();

            // Click on the 'Search' button
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.id("ctl00_MainContent_btnSearch")))
                    .click();

            By linksLocator = By.xpath("//table[@id='gvSearchResults']//tr//td//a");
            int size = driver.findElements(linksLocator).size();

            for (int i = 0; i < size; i++) {
                boolean clicked = false;
                int attempts = 0;
                while (!clicked && attempts < 3) {
                    try {
                        List<WebElement> currentLinks = driver.findElements(linksLocator);
                        WebElement link = currentLinks.get(i);

                        new WebDriverWait(driver, Duration.ofSeconds(10))
                            .until(ExpectedConditions.elementToBeClickable(link)).click();

                        clicked = true;
                    } catch (org.openqa.selenium.StaleElementReferenceException ex) {
                        attempts++;
                        // small pause before retrying
                        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
                    }
                }

                if (!clicked) {
                    System.out.println("Skipping index " + i + " after retries due to staleness.");
                    continue;
                }

                switch (partyType) {
                    case "Entity":
                        new WebDriverWait(driver, Duration.ofSeconds(10))
                            .until(ExpectedConditions.visibilityOfElementLocated(By.id("ctl00_MainContent_lblNameOther")));

                        String entityName = driver.findElement(By.id("ctl00_MainContent_lblNameOther")).getText();
                        System.out.println("Entity Name: " + entityName);

                        String idType = "", id = "", entityCountry = "", address = "", city = "", stateOrProvince = "", postalCode = "", addressCountry = null;

                        try {
                            idType = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[1]")).getText();
                            id = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[2]")).getText();
                            entityCountry = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[3]")).getText();
                        } catch (Exception e) {
                            logger.error("'" + e.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
                            logger.error("Table or element for identification information is not in the DOM at all! in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
                            e.getMessage();
                            continue;
                        }

                        try {
                            address = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[1]")).getText();
                            city = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[2]")).getText();
                            stateOrProvince = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[3]")).getText();
                            postalCode = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[4]")).getText();
                            addressCountry = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[5]")).getText();
                        } catch (Exception e) {
                            logger.error("'" + e.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
                            logger.error("Table or element for identification information is not in the DOM at all! in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
                            e.getMessage();
                            continue;
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
                        String dateOfLastIncome = LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
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

                        // DBConnection.insertPartyIntoDB(strPartyType, partyAlert, firstname, surname, middleName, previousSurname, dateOfBirth, countryOBirth, nationality, countryOfResidence, partyGenderFromID, profession, monthlyIncome, dateOfLastIncome, SAIDNumber, nationality2, nationality3, passport, passportCountry, taxRegistrationNumber, primaryTaxResidence, foreignTin, foreignTinIssuingCountry, reasonForTransaction, productType, riskClass, businessRelationship, sourceOfFunds, accountNumber, transactionAmount, transactionDate, inceptionDate, authorisedBy, terminationDate, registeredName, registrationNumber, dateOfRegistration, countryOfRegistration, industryType, additionalTaxResidence, vatRegistrationNumber, npResidentialAddress, npPostalAddress, npPoboxAddress, lePostalAddress, lePoboxAddress, leRegisteredAddress, leGcoheadofficeAddress, leOperationalAddress);

                        try {
                            // Path fileToDelete = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "scraped_entities.csv");
                            // if (Files.exists(fileToDelete)) {
                            //     Files.delete(fileToDelete);
                            //     System.out.println("Deleted existing scraped_entities.csv");
                            // }

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
                            logger.error("Failed to write scraped entity CSV", e);
                        }

                        System.out.println("************************************************************************"); break;
                    case "Individual":
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
                            System.out.println("Table or element for identification information is not in the DOM at all!RMATION IS NOT IN THE DOM AT ALL!");
                            e.getMessage();
                        }

                        try {
                            entAddress = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[1]")).getText();
                            entCity = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[2]")).getText();
                            entStateOrProvince = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[3]")).getText();
                            entPostalCode = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[4]")).getText();
                            entAddressCountry = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[5]")).getText();
                        } catch (Exception e) {
                            System.out.println("TABLE FOR ADDRESS INFORMATION IS NOT IN THE DOM AT ALL!");
                            // e.getMessage();
                            continue;
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
                        String np_dateOfLastIncome = LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
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

                        // DBConnection.insertPartyIntoDB(np_partyType, np_partyAlert, np_firstname, np_surname, np_middleName, np_previousSurname, np_dateOfBirth, np_countryOBirth, np_nationality, np_countryOfResidence, np_partyGenderFromID, np_profession, np_monthlyIncome, np_dateOfLastIncome, np_SAIDNumber, np_nationality2, np_nationality3, np_passport, np_passportCountry, np_taxRegistrationNumber, np_primaryTaxResidence, np_foreignTin, np_foreignTinIssuingCountry, np_reasonForTransaction, np_productType, np_riskClass, np_businessRelationship, np_sourceOfFunds, np_accountNumber, np_transactionAmount, np_transactionDate, np_inceptionDate, np_authorisedBy, np_terminationDate, np_registeredName, np_registrationNumber, np_dateOfRegistration, np_countryOfRegistration, np_industryType, np_additionalTaxResidence, np_vatRegistrationNumber, np_npResidentialAddress, np_npPostalAddress, np_npPoboxAddress, np_lePostalAddress, np_lePoboxAddress, np_leRegisteredAddress, np_leGcoheadofficeAddress, np_leOperationalAddress);
                        try {

                            // Path fileToDelete = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "scraped_individuals.csv");
                            // if (Files.exists(fileToDelete)) {
                            //     Files.delete(fileToDelete);
                            //     System.out.println("Deleted existing scraped_individuals.csv");
                            // }

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
                            logger.error("Failed to write scraped individual CSV", e);
                        }
                        System.out.println("************************************************************************"); break;
                    default:
                            logger.error("Error. Only 'Entity' or 'Individual' are expected options! in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
                            break;
                }

                // go back and wait until results table is present again
                new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.id("ctl00_MainContent_btnBack")))
                    .click();

                new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("gvSearchResults")));
            }
        }

        System.out.println("end of method");
    }

    @AfterMethod
    public void terminateBrowser(){
        driver.close();
        driver.quit();
        DBConnection.writeDataIntoDb();
        System.out.println("END.....FINISH....\n=============================");
    }
}
