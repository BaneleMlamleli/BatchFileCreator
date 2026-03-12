package scrapesanctionslist;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.testng.annotations.Test;
import com.github.javafaker.Faker;

import util.DBConnection;
import util.SAIDNumberGenerator;

/**
 * JavaFakerData
 *
 * <p>Test utility class that generates synthetic test records for two kinds of parties and inserts
 * them into the database via util.DBConnection.insertPartyIntoDB(). The class is intended to be run
 * as TestNG tests (each public method is annotated with {@code @Test}) and is designed to populate
 * a test environment with large volumes of realistic-looking data produced by the Faker library.</p>
 *
 * <p>General behaviour:</p>
 * <ul>
 *   <li>Each test method loops from 0 to 10,000 (inclusive), producing 10,001 records per run.</li>
 *   <li>Values are generated using com.github.javafaker.Faker together with java.time for date
 *       manipulation and util.SAIDNumberGenerator for South African ID generation (individuals).</li>
 *   <li>Dates are formatted as {@code dd/MM/yyyy}.</li>
 *   <li>The methods call DBConnection.insertPartyIntoDB(...) for every generated record, causing
 *       lasting side effects in the target database. Run only against non-production/test DBs.</li>
 * </ul>
 *
 * <p>Thread-safety and performance:</p>
 * <ul>
 *   <li>The methods are not designed for concurrent invocation; they perform many blocking DB
 *       operations and may take a long time to complete.</li>
 *   <li>Because of the large number of inserts, consider batching, throttling or disabling these
 *       tests when not required.</li>
 * </ul>
 *
 * Methods:
 * <ul>
 *   <li>{@link #individualTestData()}:
 *       <ul>
 *         <li>Generates personal (natural person) test data including first/middle/last names,
 *             country/nationality fields (defaulting to "ZA"), job title, income, passport and tax
 *             values, various addresses, and relationship/transaction metadata.</li>
 *         <li>Generates a random date of birth for individuals between 18 and 100 years old and
 *             uses {@code SAIDNumberGenerator.generateSAID(...)} to produce a South African ID
 *             number based on the DOB and a randomly chosen gender.</li>
 *         <li>Derives {@code partyGenderFromID} from the generated SA ID and populates other
 *             related fields before inserting into the DB.</li>
 *         <li>Side effects: inserts each generated party into the database via
 *             {@code DBConnection.insertPartyIntoDB(...)}.</li>
 *       </ul>
 *   </li>
 *
 *   <li>{@link #entityTestData()}:
 *       <ul>
 *         <li>Generates legal-entity (company) test data: company name, registration number,
 *             registration date, industry type, VAT and tax identifiers, and multiple addresses.</li>
 *         <li>Entity records do not include a SA ID or personal date of birth; many personal fields
 *             are left empty and {@code partyType} is set to "L".</li>
 *         <li>Side effects: inserts each generated entity into the database via
 *             {@code DBConnection.insertPartyIntoDB(...)}.</li>
 *       </ul>
 *   </li>
 * </ul>
 *
 * Warnings and recommendations:
 * <ul>
 *   <li>Because this class writes many records, run it only against test or dedicated development
 *       databases to avoid polluting production data.</li>
 *   <li>DBConnection.insertPartyIntoDB(...) may throw unchecked runtime exceptions for DB errors;
 *       callers/test runners should be prepared to handle failures.</li>
 *   <li>Values such as country, nationality and many defaults are hard-coded (commonly "ZA");
 *       change these constants if you require different locales.</li>
 * </ul>
 *
 * @author
 *     Test data generator (uses Faker and SAIDNumberGenerator)
 * @see util.DBConnection
 * @see util.SAIDNumberGenerator
 * 
 * @author banele mlamleli
 * @since 1.0
 */


public class JavaFakerData {

    @Test
    public void individualTestData(){

        Faker faker = new Faker();

        for (int i = 0; i <= 10000; i++) {
            boolean partyAlert = false;
			boolean partyIsUsed = false;
            String firstname = faker.name().firstName();
            String surname = faker.name().lastName();
            String middleName = faker.name().firstName();
            String previousSurname = faker.name().lastName();
	        String countryOBirth = "ZA";
	        String nationality = "ZA";
	        String countryOfResidence = "ZA";
			String profession = faker.job().title();
			long monthlyIncome = faker.number().numberBetween(100, 10000);
			String dateOfLastIncome = LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        String nationality2 = "ZA";
	        String nationality3 = "ZA";
	        String passport = faker.bothify("??######");
	        String passportCountry = "ZA";
	        String taxRegistrationNumber = faker.number().digits(10);
	        String primaryTaxResidence = "ZA";
	        String additionalTaxResidence = "ZA";
	        String foreignTin = faker.number().digits(10);
	        String foreignTinIssuingCountry = "ZA";
	        // -- relationship
	        String reasonForTransaction = "ADDITIONAL PREMIUM"; // Default value will be used when generating the batch file
	        String productType = "ALT"; // not using this column as values are in the dropdown
	        String riskClass = "H"; // not using this column as values are in the dropdown
	        String businessRelationship = "BO"; // Default value will be used when generating the batch file
	        String sourceOfFunds = "ALLOWANCE";
	        String accountNumber = faker.number().digits(10);
	        int transactionAmount = Integer.parseInt(faker.number().digits(3));
	        String transactionDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); 
	        String inceptionDate = LocalDate.now().minusYears((int) (Math.random() * 20) + 1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        String authorisedBy = faker.name().firstName() +" "+ faker.name().lastName();
	        String terminationDate = LocalDate.now().minusYears((int) (Math.random() * 10) + 1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        // --LE details
	        String registeredName = "";
	        String registrationNumber = "";
	        String partyType = "N";
	        String dateOfRegistration = "";
	        String countryOfRegistration = "";
	        String industryType = "";
	        String vatRegistrationNumber = "";
	        // --NP address information
	        String npResidentialAddress = faker.address().fullAddress();
	        String npPostalAddress = faker.address().fullAddress(); 
	        String npPoboxAddress = "P.O Box" + faker.address().fullAddress();
	        // -- LE address information
	        String lePostalAddress = "";
	        String lePoboxAddress = "";
	        String leRegisteredAddress =faker.address().fullAddress();
	        String leGcoheadofficeAddress = "";
	        String leOperationalAddress  = "";
            
            boolean gender[] = {true, false};
            long startEpoch = LocalDate.now().minusYears(100).toEpochDay();
            long endEpoch = LocalDate.now().minusYears(18).toEpochDay();
            long randomEpochDay = java.util.concurrent.ThreadLocalRandom.current().nextLong(startEpoch, endEpoch + 1);
            LocalDate randomDob = LocalDate.ofEpochDay(randomEpochDay);
            int yearOfBirth = randomDob.getYear();
            int monthOfBirth = randomDob.getMonthValue();
            int dayOfBirth = randomDob.getDayOfMonth();

            String SAIDNumber = SAIDNumberGenerator.generateSAID(LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth), gender[(int) (Math.random() * 1) + 0], true);
            
            LocalDate dob = LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth);
            String dateOfBirth = dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            String partyGenderFromID = Integer.parseInt(SAIDNumber.substring(6, 10)) < 5000 ? "F": "M";

			DBConnection.insertPartyIntoDB(partyType, partyAlert, partyIsUsed, firstname, surname, middleName, previousSurname, dateOfBirth, countryOBirth, nationality, countryOfResidence, partyGenderFromID, profession, monthlyIncome, dateOfLastIncome, SAIDNumber, nationality2, nationality3, passport, passportCountry, taxRegistrationNumber, primaryTaxResidence, foreignTin, foreignTinIssuingCountry, reasonForTransaction, productType, riskClass, businessRelationship, sourceOfFunds, accountNumber, transactionAmount, transactionDate, inceptionDate, authorisedBy, terminationDate, registeredName, registrationNumber, dateOfRegistration, countryOfRegistration, industryType, additionalTaxResidence, vatRegistrationNumber, npResidentialAddress, npPostalAddress, npPoboxAddress, lePostalAddress, lePoboxAddress, leRegisteredAddress, leGcoheadofficeAddress, leOperationalAddress, "");
        }
    }

    @Test
    public void entityTestData(){

        Faker faker = new Faker();

        for (int i = 0; i <= 10000; i++) {
            boolean partyAlert = false;
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
			String dateOfLastIncome = LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        String nationality2 = "ZA";
	        String nationality3 = "ZA";
	        String passport = "";
	        String passportCountry = "";
	        String taxRegistrationNumber = faker.number().digits(10);
	        String primaryTaxResidence = "ZA";
	        String additionalTaxResidence = "ZA";
	        String foreignTin = faker.number().digits(10);
	        String foreignTinIssuingCountry = "ZA";
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
	        String registeredName = faker.company().name();
	        String registrationNumber = faker.number().digits(10);
	        String partyType = "L";
	        String dateOfRegistration = LocalDate.now().minusYears((int) (Math.random() * 10) + 1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        String countryOfRegistration = "ZA";
	        String industryType = "ACCOUNTING SERVICES";
	        String vatRegistrationNumber = faker.number().digits(10);
	        // --NP address information
	        String npResidentialAddress = "";
	        String npPostalAddress = "";
	        String npPoboxAddress = "";
	        // -- LE address information
	        String lePostalAddress = faker.address().fullAddress();
	        String lePoboxAddress = "P.O Box " + faker.address().fullAddress();
	        String leRegisteredAddress =faker.address().fullAddress();
	        String leGcoheadofficeAddress = faker.address().fullAddress();
	        String leOperationalAddress  = faker.address().fullAddress();

            String SAIDNumber = "";
            String dateOfBirth = "";

            String partyGenderFromID = "";

			DBConnection.insertPartyIntoDB(partyType, partyAlert, partyIsUsed, firstname, surname, middleName, previousSurname, dateOfBirth, countryOBirth, nationality, countryOfResidence, partyGenderFromID, profession, monthlyIncome, dateOfLastIncome, SAIDNumber, nationality2, nationality3, passport, passportCountry, taxRegistrationNumber, primaryTaxResidence, foreignTin, foreignTinIssuingCountry, reasonForTransaction, productType, riskClass, businessRelationship, sourceOfFunds, accountNumber, transactionAmount, transactionDate, inceptionDate, authorisedBy, terminationDate, registeredName, registrationNumber, dateOfRegistration, countryOfRegistration, industryType, additionalTaxResidence, vatRegistrationNumber, npResidentialAddress, npPostalAddress, npPoboxAddress, lePostalAddress, lePoboxAddress, leRegisteredAddress, leGcoheadofficeAddress, leOperationalAddress, "");
        }
	}

}
