/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author banele
 */
public class DBConnection {
    
    public static Connection connection(){
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:src/main/java/database/parties.db");
            System.out.println("Connection established successfully...");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }
    
}
