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
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pojo.Party;

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
    
    public static List<Party> getPartyInformation(boolean alert, String partyType){
        Connection con = DBConnection.connection();
        PreparedStatement prepStatemnt = null;
        ResultSet resultSet = null;
        List<Party> party = new ArrayList<>();
        try {
            String sql = "SELECT * FROM parties WHERE party_alert = ? AND party_type = ?";
            prepStatemnt = con.prepareStatement(sql);
            prepStatemnt.setBoolean(1, alert);
            prepStatemnt.setString(2, partyType);
            resultSet = prepStatemnt.executeQuery();
            // while (resultSet.next()) {                
                party.add(new Party(
                    resultSet.getString("party_type"),
                    resultSet.getBoolean("party_alert"),
                    resultSet.getBoolean("party_is_used"),
                    resultSet.getString("firstname"),
                    resultSet.getString("surname"),
                    resultSet.getString("middle_name"),
                    resultSet.getString("previous_surname"),
                    resultSet.getString("date_of_birth"),
                    resultSet.getString("country_of_birth"),
                    resultSet.getString("nationality"),
                    resultSet.getString("country_of_residence"),
                    resultSet.getString("gender"),
                    resultSet.getString("profession"),
                    resultSet.getInt("monthly_income"),
                    resultSet.getString("date_of_last_income"),
                    resultSet.getString("id_number"),
                    resultSet.getString("nationality2"),
                    resultSet.getString("nationality3"),
                    resultSet.getString("passport"),
                    resultSet.getString("passport_country"),
                    resultSet.getString("tax_registration_number"),
                    resultSet.getString("primary_tax_residence"),
                    resultSet.getString("foreign_tin"),
                    resultSet.getString("foreign_tin_issuing_country"),
                    resultSet.getString("reason_for_transaction"),
                    resultSet.getString("product_type"),
                    resultSet.getString("risk_class"),
                    resultSet.getString("business_relationship"),
                    resultSet.getString("source_of_funds"),
                    resultSet.getString("account_number"),
                    resultSet.getInt("transaction_amount"),
                    resultSet.getString("transaction_date"),
                    resultSet.getString("inception_date"),
                    resultSet.getString("authorised_by"),
                    resultSet.getString("termination_date"),
                    resultSet.getString("registered_name"),
                    resultSet.getString("registration_number"),
                    resultSet.getString("date_of_registration"),
                    resultSet.getString("country_of_registration"),
                    resultSet.getString("industry_type"),
                    resultSet.getString("additional_tax_residence"),
                    resultSet.getString("vat_registration_number"),
                    resultSet.getString("np_residential_address"),
                    resultSet.getString("np_postal_address"),
                    resultSet.getString("np_pobox_address"),
                    resultSet.getString("le_postal_address"),
                    resultSet.getString("le_pobox_address"),
                    resultSet.getString("le_registered_address"),
                    resultSet.getString("le_gcoheadoffice_address"),
                    resultSet.getString("le_operational_address")
                ));
            // }
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
        return party;
    }
}
