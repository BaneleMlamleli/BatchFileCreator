package scrapesanctionslist;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.testng.annotations.Test;
import com.github.javafaker.Faker;

import util.DBConnection;
import util.SAIDNumberGenerator;


public class JavaFakerData {

    @Test
    public void individualTestData(){

        Faker faker = new Faker();

        for (int i = 0; i <= 4056; i++) {
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
	        String registeredName = null;
	        String registrationNumber = null;
	        String partyType = "N";
	        String dateOfRegistration = null;
	        String countryOfRegistration = null;
	        String industryType = null;
	        String vatRegistrationNumber = null;
	        // --NP address information
	        String npResidentialAddress = faker.address().fullAddress();
	        String npPostalAddress = faker.address().fullAddress(); 
	        String npPoboxAddress = "P.O Box" + faker.address().fullAddress();
	        // -- LE address information
	        String lePostalAddress = null;
	        String lePoboxAddress = null;
	        String leRegisteredAddress =faker.address().fullAddress();
	        String leGcoheadofficeAddress = null;
	        String leOperationalAddress  = null;
            
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

			DBConnection.insertPartyIntoDB(partyType, partyAlert, partyIsUsed, firstname, surname, middleName, previousSurname, dateOfBirth, countryOBirth, nationality, countryOfResidence, partyGenderFromID, profession, monthlyIncome, dateOfLastIncome, SAIDNumber, nationality2, nationality3, passport, passportCountry, taxRegistrationNumber, primaryTaxResidence, foreignTin, foreignTinIssuingCountry, reasonForTransaction, productType, riskClass, businessRelationship, sourceOfFunds, accountNumber, transactionAmount, transactionDate, inceptionDate, authorisedBy, terminationDate, registeredName, registrationNumber, dateOfRegistration, countryOfRegistration, industryType, additionalTaxResidence, vatRegistrationNumber, npResidentialAddress, npPostalAddress, npPoboxAddress, lePostalAddress, lePoboxAddress, leRegisteredAddress, leGcoheadofficeAddress, leOperationalAddress);
        }
    }

    @Test
    public void entityTestData(){

        Faker faker = new Faker();

        for (int i = 0; i <= 4056; i++) {
            boolean partyAlert = false;
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
			String dateOfLastIncome = LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        String nationality2 = "ZA";
	        String nationality3 = "ZA";
	        String passport = null;
	        String passportCountry = null;
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
	        String npResidentialAddress = null;
	        String npPostalAddress = null;
	        String npPoboxAddress = null;
	        // -- LE address information
	        String lePostalAddress = faker.address().fullAddress();
	        String lePoboxAddress = "P.O Box " + faker.address().fullAddress();
	        String leRegisteredAddress =faker.address().fullAddress();
	        String leGcoheadofficeAddress = faker.address().fullAddress();
	        String leOperationalAddress  = faker.address().fullAddress();

            String SAIDNumber = null;
            String dateOfBirth = null;

            String partyGenderFromID = null;

			DBConnection.insertPartyIntoDB(partyType, partyAlert, partyIsUsed, firstname, surname, middleName, previousSurname, dateOfBirth, countryOBirth, nationality, countryOfResidence, partyGenderFromID, profession, monthlyIncome, dateOfLastIncome, SAIDNumber, nationality2, nationality3, passport, passportCountry, taxRegistrationNumber, primaryTaxResidence, foreignTin, foreignTinIssuingCountry, reasonForTransaction, productType, riskClass, businessRelationship, sourceOfFunds, accountNumber, transactionAmount, transactionDate, inceptionDate, authorisedBy, terminationDate, registeredName, registrationNumber, dateOfRegistration, countryOfRegistration, industryType, additionalTaxResidence, vatRegistrationNumber, npResidentialAddress, npPostalAddress, npPoboxAddress, lePostalAddress, lePoboxAddress, leRegisteredAddress, leGcoheadofficeAddress, leOperationalAddress);
        }
	}

}
