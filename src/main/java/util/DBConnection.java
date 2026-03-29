package util;

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

import pojo.Party;

/**
 * Utility class that encapsulates database access for the application.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Establish a JDBC connection to the application's SQLite database.</li>
 *   <li>Insert new party records into the parties table.</li>
 *   <li>Query small helper values (e.g. country code) and summary values (record count).</li>
 *   <li>Fetch party records (master and linked variants) and map them to {@code pojo.Party}
 *       instances while marking those parties as used.</li>
 *   <li>Update the {@code party_is_used} flag for a given party id.</li>
 * </ul>
 *
 * <p>Notes on behavior and design:
 * <ul>
 *   <li>All methods are static convenience methods and use a local JDBC {@code Connection}
 *       obtained from {@link #connection()}.</li>
 *   <li>The connection points to an embedded SQLite database (jdbc:sqlite:src/main/java/database/parties.db).
 *       The SQLite JDBC driver class is loaded in {@link #connection()}.</li>
 *   <li>Errors and exceptions are caught and logged; methods generally swallow exceptions and
 *       return a safe default (null, zero, or an empty list) rather than propagating SQLExceptions.</li>
 *   <li>Resource management is handled manually (explicit close in finally blocks). Several methods
 *       close read resources (ResultSet/PreparedStatement/Connection) before performing a follow-up
 *       write (to avoid SQLITE_BUSY).</li>
 *   <li>Query methods that return a List&lt;Party&gt; currently construct a single Party from the
 *       first result row and return it in a list. They expect at least one matching row; if none
 *       are present the returned list will be empty (and errors are logged).</li>
 * </ul>
 *
 * <p>Threading / concurrency:
 * <ul>
 *   <li>Methods are not synchronized. Multiple concurrent callers may attempt to open connections
 *       to the same SQLite file; SQLite has limitations for concurrent writes—care should be taken
 *       if this utility is used concurrently from multiple threads/processes.</li>
 * </ul>
 *
 * <p>Limitations and recommendations:
 * <ul>
 *   <li>The {@code insertPartyIntoDB} method accepts a very long list of parameters and uses
 *       positional placeholders (VALUES (?, ?, ...)). It would be clearer and safer to use a named
 *       column list in the INSERT and/or a builder/DTO for parameters.</li>
 *   <li>Prefer try-with-resources for Connection/PreparedStatement/ResultSet to simplify and
 *       harden resource handling.</li>
 *   <li>Consider returning Optional&lt;Party&gt; or a more descriptive result object for the
 *       fetch methods, and consider supporting multiple rows instead of only the first.</li>
 *   <li>Consider using a connection pool or higher-level data access abstraction if concurrency,
 *       transaction management, or performance becomes important.</li>
 * </ul>
 *
 * @author banele mlamleli
 * @see pojo.Party
 */

public class DBConnection {

    public static Logger logger = LogManager.getLogger(new Object() {
    }.getClass().getName());
    
    public static Connection connection(){
        Path targetDir = Paths.get(System.getProperty("user.home"), "BatchFileCreator", "data"); // Target directory (cross-platform safe)
        Connection connect = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite: //" + targetDir.toString()+"/parties.db"); // targeting the external directory that contains the copied database
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "'");
        }
        return connect;
    }

    public static void insertPartyIntoDB(String partyType, boolean partyAlert, boolean partyIsUsed, String firstname, String surname, String middleName, String previousSurname, String dateOfBirth, String countryOBirth, String nationality, String countryOfResidence, String partyGenderFromID, String profession, long monthlyIncome, String dateOfLastIncome, String SAIDNumber, String nationality2, String nationality3, String passport, String passportCountry, String taxRegistrationNumber, String primaryTaxResidence, String foreignTin, String foreignTinIssuingCountry, String reasonForTransaction, String productType, String riskClass, String businessRelationship, String sourceOfFunds, String accountNumber, int transactionAmount, String transactionDate, String inceptionDate, String authorisedBy, String terminationDate, String registeredName, String registrationNumber, String dateOfRegistration, String countryOfRegistration, String industryType, String additionalTaxResidence, String vatRegistrationNumber, String npResidentialAddress, String npPostalAddress, String npPoboxAddress, String lePostalAddress, String lePoboxAddress, String leRegisteredAddress, String leGcoheadofficeAddress, String leOperationalAddress, String parentAccount){
        Connection con = DBConnection.connection();
        PreparedStatement preparedStatement = null;
        try {
            String sql = "INSERT INTO parties VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            preparedStatement.setString(52,parentAccount);
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
    
    public static void updatePartyToUsedStatus(int partyId){
        Connection con = DBConnection.connection();
        PreparedStatement preparedStatement = null;
        try {
            String sql = "UPDATE parties SET party_is_used = true WHERE id = ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, partyId);
            int updatedRow = preparedStatement.executeUpdate();

            if (updatedRow >= 1) {
                logger.info("'party_is_used' status has been updated to true for Party id " + partyId);
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
    }
    
    /* This method will only be used for Legal and Natural person Master parties and the party will either be alert or no alert */
    public static List<Party> getAlertOrNoAlertMasterPartyOnly(boolean alert, String partyType, String productCode, String riskClass, String businessUnit){
        Connection con = DBConnection.connection();
        PreparedStatement prepStatemnt = null;
        ResultSet resultSet = null;
        List<Party> party = new ArrayList<>();
        try {
            String sql = "SELECT * FROM parties WHERE party_alert = ? AND party_type = ? AND party_is_used = false";
            prepStatemnt = con.prepareStatement(sql);
            prepStatemnt.setBoolean(1, alert);
            prepStatemnt.setString(2, partyType);
            resultSet = prepStatemnt.executeQuery();
            
            party.add(new Party(
                alert,
                resultSet.getBoolean("party_is_used"),
                resultSet.getString("firstname"),
                resultSet.getString("surname"),
                resultSet.getString("middle_name"),
                resultSet.getString("previous_surname"),
                "",
                "",
                resultSet.getString("id_number"),
                "",
                resultSet.getString("gender"),
                resultSet.getString("passport"),
                resultSet.getString("passport_country"),
                resultSet.getString("date_of_birth"),
                resultSet.getString("np_residential_address"),
                resultSet.getString("country_of_residence"),
                resultSet.getString("nationality"),
                resultSet.getString("country_of_birth"),
                resultSet.getInt("monthly_income"),
                resultSet.getString("date_of_last_income"),
                "",
                "",
                "",
                resultSet.getString("np_postal_address"),
                "",
                resultSet.getString("registered_name"),
                resultSet.getString("le_operational_address"),
                "",
                "",
                "",
                "COMMISSION",
                resultSet.getString("source_of_funds"),
                resultSet.getString("profession"),
                "",
                resultSet.getString("registered_name"),
                resultSet.getString("registration_number"),
                "TRS",
                resultSet.getString("le_registered_address"),
                "",
                "",
                "",
                "",
                "",
                resultSet.getString("nationality3"),
                resultSet.getString("vat_registration_number"),
                "",
                "",
                "",
                "ANNUITANT",
                resultSet.getString("reason_for_transaction"),
                resultSet.getString("transaction_date"),
                resultSet.getInt("transaction_amount"),
                resultSet.getString("authorised_by"),
                "",
                resultSet.getString("tax_registration_number"),
                resultSet.getString("foreign_tin"),
                "",
                resultSet.getString("nationality2"),
                resultSet.getString("le_operational_address"),
                resultSet.getString("le_operational_address"),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "OTHER",
                resultSet.getString("business_relationship"),
                "",
                resultSet.getString("primary_tax_residence"),
                resultSet.getString("additional_tax_residence"),
                "",
                "",
                resultSet.getString("le_gcoheadoffice_address"),
                "",
                "",
                resultSet.getString("industry_type"),
                "",
                "SAV",
                resultSet.getString("account_number"),
                productCode,
                businessUnit,
                riskClass,
                partyType,
                resultSet.getString("parent_account_number"),
                "BO",
                "A",
                resultSet.getString("inception_date"),
                resultSet.getString("termination_date")
            ));

            // Update the party status to reflect as used
            int partyIdToUpdate = resultSet.getInt("Id");
            // Close read resources before attempting the write to avoid SQLITE_BUSY
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException ex) {
                logger.error("'" + ex.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            }
            try {
                if (prepStatemnt != null) {
                    prepStatemnt.close();
                }
            } catch (SQLException ex) {
                logger.error("'" + ex.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                logger.error("'" + ex.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            }
            // Prevent finally from closing again
            resultSet = null;
            prepStatemnt = null;
            con = null;
            updatePartyToUsedStatus(partyIdToUpdate);

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

    /* This method will only be used for Legal and Natural person Linked parties and the party will either be alert or no alert. N.B., account_number will not be pulled from the DB is linked parties are linked via master party account_number */
    public static List<Party> getAlertOrNoAlertLinkedPartyOnly(boolean alert, String partyType, String productCode, String riskClass, String businessUnit, String mpAccNumber){
        Connection con = DBConnection.connection();
        PreparedStatement prepStatemnt = null;
        ResultSet resultSet = null;
        List<Party> party = new ArrayList<>();
        try {
            String sql = "SELECT * FROM parties WHERE party_alert = ? AND party_type = ? AND party_is_used = false";
            prepStatemnt = con.prepareStatement(sql);
            prepStatemnt.setBoolean(1, alert);
            prepStatemnt.setString(2, partyType);
            resultSet = prepStatemnt.executeQuery();

            party.add(new Party(
                alert,
                resultSet.getBoolean("party_is_used"),
                resultSet.getString("firstname"),
                resultSet.getString("surname"),
                resultSet.getString("middle_name"),
                resultSet.getString("previous_surname"),
                "",
                "",
                resultSet.getString("id_number"),
                "",
                resultSet.getString("gender"),
                resultSet.getString("passport"),
                resultSet.getString("passport_country"),
                resultSet.getString("date_of_birth"),
                resultSet.getString("np_residential_address"),
                resultSet.getString("country_of_residence"),
                resultSet.getString("nationality"),
                resultSet.getString("country_of_birth"),
                resultSet.getInt("monthly_income"),
                resultSet.getString("date_of_last_income"),
                "",
                "",
                "",
                resultSet.getString("np_postal_address"),
                "",
                resultSet.getString("registered_name"),
                resultSet.getString("le_operational_address"),
                "",
                "",
                "",
                "COMMISSION",
                resultSet.getString("source_of_funds"),
                resultSet.getString("profession"),
                "",
                resultSet.getString("registered_name"),
                resultSet.getString("registration_number"),
                "TRS",
                resultSet.getString("le_registered_address"),
                "",
                "",
                "",
                "",
                "",
                resultSet.getString("nationality3"),
                resultSet.getString("vat_registration_number"),
                "",
                "",
                "",
                "ANNUITANT",
                resultSet.getString("reason_for_transaction"),
                resultSet.getString("transaction_date"),
                resultSet.getInt("transaction_amount"),
                resultSet.getString("authorised_by"),
                "",
                resultSet.getString("tax_registration_number"),
                resultSet.getString("foreign_tin"),
                "",
                resultSet.getString("nationality2"),
                resultSet.getString("le_operational_address"),
                resultSet.getString("le_operational_address"),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "OTHER",
                resultSet.getString("business_relationship"),
                "",
                resultSet.getString("primary_tax_residence"),
                resultSet.getString("additional_tax_residence"),
                "",
                "",
                resultSet.getString("le_gcoheadoffice_address"),
                "",
                "",
                resultSet.getString("industry_type"),
                "",
                "SAV",
                "",
                productCode,
                businessUnit,
                riskClass,
                partyType,
                mpAccNumber,
                "BO",
                "A",
                resultSet.getString("inception_date"),
                resultSet.getString("termination_date")
            ));

            // Update the party status to reflect as used
            int partyIdToUpdate = resultSet.getInt("Id");
            // Close read resources before attempting the write to avoid SQLITE_BUSY
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException ex) {
                logger.error("'" + ex.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            }
            try {
                if (prepStatemnt != null) {
                    prepStatemnt.close();
                }
            } catch (SQLException ex) {
                logger.error("'" + ex.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                logger.error("'" + ex.getMessage() + "' in method '" + new Object() {}.getClass().getEnclosingMethod().getName() + "'");
            }
            // Prevent finally from closing again
            resultSet = null;
            prepStatemnt = null;
            con = null;
            updatePartyToUsedStatus(partyIdToUpdate);

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
