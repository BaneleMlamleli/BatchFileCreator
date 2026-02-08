/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "'");
        }
        return connect;
    }

    public static void insertPartyIntoDB(String partyType, boolean partyAlert, boolean partyIsUsed, String firstname, String surname, String middleName, String previousSurname, String dateOfBirth, String countryOBirth, String nationality, String countryOfResidence, String partyGenderFromID, String profession, long monthlyIncome, String dateOfLastIncome, String SAIDNumber, String nationality2, String nationality3, String passport, String passportCountry, String taxRegistrationNumber, String primaryTaxResidence, String foreignTin, String foreignTinIssuingCountry, String reasonForTransaction, String productType, String riskClass, String businessRelationship, String sourceOfFunds, String accountNumber, int transactionAmount, String transactionDate, String inceptionDate, String authorisedBy, String terminationDate, String registeredName, String registrationNumber, String dateOfRegistration, String countryOfRegistration, String industryType, String additionalTaxResidence, String vatRegistrationNumber, String npResidentialAddress, String npPostalAddress, String npPoboxAddress, String lePostalAddress, String lePoboxAddress, String leRegisteredAddress, String leGcoheadofficeAddress, String leOperationalAddress){
        Connection con = DBConnection.connection();
        PreparedStatement preparedStatement = null;
        try {
            String sql = "INSERT INTO parties VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(2,partyType);
            preparedStatement.setBoolean(3,partyAlert);
            preparedStatement.setBoolean(4,partyIsUsed);
            preparedStatement.setString(5,firstname);
            preparedStatement.setString(6,surname);
            preparedStatement.setString(7,middleName);
            preparedStatement.setString(8,previousSurname);
            preparedStatement.setString(9,dateOfBirth);
            preparedStatement.setString(10,countryOBirth);
            preparedStatement.setString(11,nationality);
            preparedStatement.setString(12,countryOfResidence);
            preparedStatement.setString(13,partyGenderFromID);
            preparedStatement.setString(14,profession);
            preparedStatement.setLong(15,monthlyIncome);
            preparedStatement.setString(16,dateOfLastIncome);
            preparedStatement.setString(17,SAIDNumber);
            preparedStatement.setString(18,nationality2);
            preparedStatement.setString(19,nationality3);
            preparedStatement.setString(20,passport);
            preparedStatement.setString(21,passportCountry);
            preparedStatement.setString(22,taxRegistrationNumber);
            preparedStatement.setString(23,primaryTaxResidence);
            preparedStatement.setString(24,foreignTin);
            preparedStatement.setString(25,foreignTinIssuingCountry);
            preparedStatement.setString(26,reasonForTransaction);
            preparedStatement.setString(27,productType);
            preparedStatement.setString(28,riskClass);
            preparedStatement.setString(29,businessRelationship);
            preparedStatement.setString(30,sourceOfFunds);
            preparedStatement.setString(31,accountNumber);
            preparedStatement.setInt(32,transactionAmount);
            preparedStatement.setString(33,transactionDate);
            preparedStatement.setString(34,inceptionDate);
            preparedStatement.setString(35,authorisedBy);
            preparedStatement.setString(36,terminationDate);
            preparedStatement.setString(37,registeredName);
            preparedStatement.setString(38,registrationNumber);
            preparedStatement.setString(39,dateOfRegistration);
            preparedStatement.setString(40,countryOfRegistration);
            preparedStatement.setString(41,industryType);
            preparedStatement.setString(42,additionalTaxResidence);
            preparedStatement.setString(43,vatRegistrationNumber);
            preparedStatement.setString(44,npResidentialAddress);
            preparedStatement.setString(45,npPostalAddress);
            preparedStatement.setString(46,npPoboxAddress);
            preparedStatement.setString(47,lePostalAddress);
            preparedStatement.setString(48,lePoboxAddress);
            preparedStatement.setString(49,leRegisteredAddress);
            preparedStatement.setString(50,leGcoheadofficeAddress);
            preparedStatement.setString(51,leOperationalAddress);
            preparedStatement.execute();
            logger.info("Party has been inserted successfully");
        } catch (Exception e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "'");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error("'" + e.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            }
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
            while (resultSet.next()) {
                code = resultSet.getString("code");
                break;
            }
        } catch (Exception e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "'");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error("'" + e.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            }
        }
        return code;
    }

    public static int returnAmountOfRecordsInTheDb(){
        Connection con = DBConnection.connection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int recordAmount = 0;
        try {
            String sql = "SELECT count(*) FROM parties";
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            recordAmount = resultSet.getInt(1);
        } catch (Exception e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "'");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error("'" + e.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            }
        }
        return recordAmount;
    }
    
}
