/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author banele
 */
public class DBConnection {

    public static Logger logger = LogManager.getLogger(new Object() {
    }.getClass().getName());
    
    public static Connection connection(){
        Connection connect = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:src/main/java/database/parties.db");
            logger.info("Connection established successfully...");
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "'");
        }
        return connect;
    }

    public static void insertPartyIntoDB(String partyType, boolean partyAlert, String firstname, String surname, String middleName, String previousSurname, String dateOfBirth, String countryOBirth, String nationality, String countryOfResidence, String partyGenderFromID, String profession, long monthlyIncome, String dateOfLastIncome, String SAIDNumber, String nationality2, String nationality3, String passport, String passportCountry, String taxRegistrationNumber, String primaryTaxResidence, String foreignTin, String foreignTinIssuingCountry, String reasonForTransaction, String productType, String riskClass, String businessRelationship, String sourceOfFunds, String accountNumber, int transactionAmount, String transactionDate, String inceptionDate, String authorisedBy, String terminationDate, String registeredName, String registrationNumber, String dateOfRegistration, String countryOfRegistration, String industryType, String additionalTaxResidence, String vatRegistrationNumber, String npResidentialAddress, String npPostalAddress, String npPoboxAddress, String lePostalAddress, String lePoboxAddress, String leRegisteredAddress, String leGcoheadofficeAddress, String leOperationalAddress){
        Connection con = DBConnection.connection();
        PreparedStatement preparedStatement = null;
        try {
            String sql = "INSERT INTO parties VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(2,partyType);
            preparedStatement.setBoolean(3,partyAlert);
            preparedStatement.setString(4,firstname);
            preparedStatement.setString(5,surname);
            preparedStatement.setString(6,middleName);
            preparedStatement.setString(7,previousSurname);
            preparedStatement.setString(8,dateOfBirth);
            preparedStatement.setString(9,countryOBirth);
            preparedStatement.setString(10,nationality);
            preparedStatement.setString(11,countryOfResidence);
            preparedStatement.setString(12,partyGenderFromID);
            preparedStatement.setString(13,profession);
            preparedStatement.setLong(14,monthlyIncome);
            preparedStatement.setString(15,dateOfLastIncome);
            preparedStatement.setString(16,SAIDNumber);
            preparedStatement.setString(17,nationality2);
            preparedStatement.setString(18,nationality3);
            preparedStatement.setString(19,passport);
            preparedStatement.setString(20,passportCountry);
            preparedStatement.setString(21,taxRegistrationNumber);
            preparedStatement.setString(22,primaryTaxResidence);
            preparedStatement.setString(23,foreignTin);
            preparedStatement.setString(24,foreignTinIssuingCountry);
            preparedStatement.setString(25,reasonForTransaction);
            preparedStatement.setString(26,productType);
            preparedStatement.setString(27,riskClass);
            preparedStatement.setString(28,businessRelationship);
            preparedStatement.setString(29,sourceOfFunds);
            preparedStatement.setString(30,accountNumber);
            preparedStatement.setInt(31,transactionAmount);
            preparedStatement.setString(32,transactionDate);
            preparedStatement.setString(33,inceptionDate);
            preparedStatement.setString(34,authorisedBy);
            preparedStatement.setString(35,terminationDate);
            preparedStatement.setString(36,registeredName);
            preparedStatement.setString(37,registrationNumber);
            preparedStatement.setString(38,dateOfRegistration);
            preparedStatement.setString(39,countryOfRegistration);
            preparedStatement.setString(40,industryType);
            preparedStatement.setString(41,additionalTaxResidence);
            preparedStatement.setString(42,vatRegistrationNumber);
            preparedStatement.setString(43,npResidentialAddress);
            preparedStatement.setString(44,npPostalAddress);
            preparedStatement.setString(45,npPoboxAddress);
            preparedStatement.setString(46,lePostalAddress);
            preparedStatement.setString(47,lePoboxAddress);
            preparedStatement.setString(48,leRegisteredAddress);
            preparedStatement.setString(49,leGcoheadofficeAddress);
            preparedStatement.setString(50,leOperationalAddress);
            preparedStatement.execute();
            logger.info("Party has been inserted successfully");
            System.out.println("Party has been inserted successfully");
        } catch (Exception e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "'");
        } 

    }

    public static String returnCountryCode(String country){
        Connection con = DBConnection.connection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String code = null;
        try {
            String sql = "SELECT code FROM countries WHERE name = ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, country);
            resultSet = preparedStatement.executeQuery();
            code = resultSet.getString(1);
        } catch (Exception e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "'");
        }
        return code;
    }

    public static void writeDataIntoDb(){
        /**
 * TODO: NOTE:
 * 1. Create a method that writes all of the scraped data into a text file. DONE
 * 2. Read from file and write the data into the 'parties' database table
 * */ 
        System.out.println("Write data into DB");
        List<String> csvFiles = new ArrayList<>();
        try {
            Path resourcesDir = Paths.get("src/main/resources");
            if (!Files.exists(resourcesDir) || !Files.isDirectory(resourcesDir)) {
                logger.error("Resources directory not found: " + resourcesDir.toString());
            } else {
                try (DirectoryStream<Path> ds = Files.newDirectoryStream(resourcesDir, "*.csv")) {
                    for (Path p : ds) {
                        csvFiles.add(p.getFileName().toString());
                    }
                }
                if (csvFiles.isEmpty()) {
                    logger.info("No CSV files found in: " + resourcesDir.toString());
                } else {
                    logger.info("CSV files found: " + csvFiles.toString());
                }
            }
        } catch (Exception e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() { }.getClass().getEnclosingMethod().getName() + "'");
        }
        
        try {
            for (int i = 0; i < csvFiles.size(); i++) {
                System.out.println("FILE NAME: " + csvFiles.get(i).toString());
                java.nio.file.Path csvPath = java.nio.file.Paths.get("src/main/java/database/"+csvFiles.get(i).toString());
                if (!java.nio.file.Files.exists(csvPath)) {
                    logger.error("CSV file not found: " + csvPath.toString());
                    return;
                }
                try (java.io.BufferedReader br = java.nio.file.Files.newBufferedReader(csvPath)) {
                    String header = br.readLine();
                    if (header == null) {
                        logger.error("CSV is empty: " + csvPath.toString());
                        return;
                    }
                    String line;
                    while ((line = br.readLine()) != null) {
                        // split preserving empty fields and strip surrounding quotes
                        String[] values = line.split(",", -1);
                        for (int ii = 0; ii < values.length; ii++) {
                            values[ii] = values[ii].trim();
                            if (values[ii].startsWith("\"") && values[ii].endsWith("\"") && values[ii].length() >= 2) {
                                values[ii] = values[ii].substring(1, values[ii].length() - 1);
                            }
                        }

                        String partyType = values.length > 0 ? values[0] : "";
                        boolean partyAlert = values.length > 1 ? Boolean.parseBoolean(values[1]) : false;
                        String firstname = values.length > 2 ? values[2] : "";
                        String surname = values.length > 3 ? values[3] : "";
                        String middleName = values.length > 4 ? values[4] : "";
                        String previousSurname = values.length > 5 ? values[5] : "";
                        String dateOfBirth = values.length > 6 ? values[6] : "";
                        String countryOBirth = values.length > 7 ? values[7] : "";
                        String nationality = values.length > 8 ? values[8] : "";
                        String countryOfResidence = values.length > 9 ? values[9] : "";
                        String partyGenderFromID = values.length > 10 ? values[10] : "";
                        String profession = values.length > 11 ? values[11] : "";
                        long monthlyIncome = 0L;
                        try { monthlyIncome = values.length > 12 && !values[12].isEmpty() ? Long.parseLong(values[12]) : 0L; } catch (NumberFormatException ignored) {}
                        String dateOfLastIncome = values.length > 13 ? values[13] : "";
                        String SAIDNumber = values.length > 14 ? values[14] : "";
                        String nationality2 = values.length > 15 ? values[15] : "";
                        String nationality3 = values.length > 16 ? values[16] : "";
                        String passport = values.length > 17 ? values[17] : "";
                        String passportCountry = values.length > 18 ? values[18] : "";
                        String taxRegistrationNumber = values.length > 19 ? values[19] : "";
                        String primaryTaxResidence = values.length > 20 ? values[20] : "";
                        String foreignTin = values.length > 21 ? values[21] : "";
                        String foreignTinIssuingCountry = values.length > 22 ? values[22] : "";
                        String reasonForTransaction = values.length > 23 ? values[23] : "";
                        String productType = values.length > 24 ? values[24] : "";
                        String riskClass = values.length > 25 ? values[25] : "";
                        String businessRelationship = values.length > 26 ? values[26] : "";
                        String sourceOfFunds = values.length > 27 ? values[27] : "";
                        String accountNumber = values.length > 28 ? values[28] : "";
                        int transactionAmount = 0;
                        try { transactionAmount = values.length > 29 && !values[29].isEmpty() ? Integer.parseInt(values[29]) : 0; } catch (NumberFormatException ignored) {}
                        String transactionDate = values.length > 30 ? values[30] : "";
                        String inceptionDate = values.length > 31 ? values[31] : "";
                        String authorisedBy = values.length > 32 ? values[32] : "";
                        String terminationDate = values.length > 33 ? values[33] : "";
                        String registeredName = values.length > 34 ? values[34] : "";
                        String registrationNumber = values.length > 35 ? values[35] : "";
                        String dateOfRegistration = values.length > 36 ? values[36] : "";
                        String countryOfRegistration = values.length > 37 ? values[37] : "";
                        String industryType = values.length > 38 ? values[38] : "";
                        String additionalTaxResidence = values.length > 39 ? values[39] : "";
                        String vatRegistrationNumber = values.length > 40 ? values[40] : "";
                        String npResidentialAddress = values.length > 41 ? values[41] : "";
                        String npPostalAddress = values.length > 42 ? values[42] : "";
                        String npPoboxAddress = values.length > 43 ? values[43] : "";
                        String lePostalAddress = values.length > 44 ? values[44] : "";
                        String lePoboxAddress = values.length > 45 ? values[45] : "";
                        String leRegisteredAddress = values.length > 46 ? values[46] : "";
                        String leGcoheadofficeAddress = values.length > 47 ? values[47] : "";
                        String leOperationalAddress = values.length > 48 ? values[48] : "";

                        insertPartyIntoDB(
                            partyType, partyAlert, firstname, surname, middleName, previousSurname, dateOfBirth, countryOBirth,
                            nationality, countryOfResidence, partyGenderFromID, profession, monthlyIncome, dateOfLastIncome,
                            SAIDNumber, nationality2, nationality3, passport, passportCountry, taxRegistrationNumber,
                            primaryTaxResidence, foreignTin, foreignTinIssuingCountry, reasonForTransaction, productType,
                            riskClass, businessRelationship, sourceOfFunds, accountNumber, transactionAmount, transactionDate,
                            inceptionDate, authorisedBy, terminationDate, registeredName, registrationNumber, dateOfRegistration,
                            countryOfRegistration, industryType, additionalTaxResidence, vatRegistrationNumber,
                            npResidentialAddress, npPostalAddress, npPoboxAddress, lePostalAddress, lePoboxAddress,
                            leRegisteredAddress, leGcoheadofficeAddress, leOperationalAddress
                        );
                    }
                }
            }
        } catch (Exception e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() { }.getClass().getEnclosingMethod().getName() + "'");
        }
        
    }
    
}
