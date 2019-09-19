/**
 * This Application creates a Table into a Database
 * It checks if the Table Exists First
 * If Table Exists the Table is not created
 * 
 * It use the Previous MonthYear App to 
 * create the table name
 * 
 * @author msuzuki
 * @version 1.0.0-SNAPSHOT
 * @since 09/17/2019
 */

package iqiConsumer;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBase {
	
	final static String DATABASE = "database";
	final static String USERNAME = "user";
	final static String PASSWORD = "password";
	final static String URL = "jdbc:postgresql:" + DATABASE;
	
	static String prevMonYr = DateFormatter.getPrevMonYear();
	static String queryStmt = new String();
	static String te = new String();
	
	// Check if the Table Exists in the Database
	public static String checkTableExist() {
		try {
			java.sql.Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			Statement stmt = conn.createStatement();
			queryStmt = "SELECT EXISTS (SELECT 1 FROM information_schema.tables " + 
						"WHERE table_name='iqi_" + prevMonYr + "');";
			ResultSet rs = stmt.executeQuery(queryStmt);
			while(rs.next()) {
				te = rs.getString(1);
				break;
			}
	        stmt.close();
	        conn.close();  
		} catch (SQLException e) {
//			e.printStackTrace();
			e.toString();
		}
		return te;
	}
	
	// Create Table in the Database
	public static void createTable() throws SQLException {
		java.sql.Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		Statement stmt = conn.createStatement();
		String createStmt = "CREATE TABLE iqi_" + prevMonYr + " (id INT, name VARCHAR(300));";
		stmt.executeUpdate(createStmt);
		stmt.close();
		conn.close();
		System.out.println("Table created Susccessfully, iqi_" + prevMonYr);
	}
}
