/**
* This Application will connect to Vertica
* and Query Vertica
* the Query will aggregate IQI Points into
* 2D Building Polygons
*
* @author: msuzuki
* @version: 1.0.0-SNAPSHOT
* @since: 2019-09-13
*/

package iqiConsumer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsumerIqiMiner implements Runnable {
	// Database Connection Parameters
	private final String[] config = ConsumerIqiMiner.getConfiguration();
	private final String DATABASE = config[0];
	private final String USERNAME = config[1];
	private final String PASSWORD = config[2];
	private final String URL = "jdbc:postgresql:" + DATABASE;
	
	private final String text;
	
	ConsumerIqiMiner(String text) {
		this.text = text;
	}
	
	// Get Database Configuration
	// Database, user, password
	public static String[] getConfiguration() {
		String file = "/home/msuzuki/eclipse-java-workspace/DBConfig.csv";
		String[] data = null;
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(file));
			String row;
			try {
				while ((row = csvReader.readLine()) != null) {
					data = row.split(",");
					System.out.println(data[0] + ", " + data[1] + ", " + data[2]);
				}
			} catch (IOException e) {
				e.toString();
			}
			
		} catch (FileNotFoundException e) {
			e.toString();
		}
		return data;
	}
	
	// IQI Query Mining
	public void run() {
		
		// Split Message into Query Components
		String[] messageComponents;
		String delimiter = "_";
		messageComponents = text.split(delimiter);
		
		// Get Components that goes into Query
		String state = messageComponents[0];
		String prevMonYr = messageComponents[1];
		String startDate = messageComponents[2];
		String endDate = messageComponents[3];
		String startLat = messageComponents[4];
		String startLong = messageComponents[5];
		String endLat = messageComponents[6];
		String endLong = messageComponents[7];
		
		// Make Query Statement with the Message Components
		String queryStmt = "INSERT INTO javatest (cars) VALUES ('stbreak: " + state + ", " + 
							startDate + ", " + endDate + 
							", " + startLat + ", " + startLong + 
							", " + endLat + ", " + endLong + 
							", " + prevMonYr +
							"');"; 
		
		// Path Definition in case Query Fails, so a file is created
		// for later being inserted back to the Queue by the Producer
		String dir = "/home/msuzuki/eclipse-java-workspace/IqiGeneratedFiles/";
		File file = new File(dir + text);
		
		// Load Driver
	    try {
	        Class.forName("org.postgresql.Driver");
	    } 
	    catch (ClassNotFoundException e) {
	    	//System.err.println("Could not find Postgresql Driver. File: " + text);
	        //e.printStackTrace();
	    	e.toString();
	        // Create file, so Producer can later put the job back into the Queue
			try {
				file.createNewFile();
			} catch (IOException e1) {
				System.err.println("IQI MINED FAILED, and FILE could not have been created: " + text);
				//e1.printStackTrace();
				e1.toString();
			}
	    }
	    
	    // Connect and Execute Query Insert into DB
	    try {
	    	java.sql.Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	        conn.setAutoCommit(false);
	        Statement stmt = conn.createStatement();
	        stmt.executeUpdate(queryStmt);
	        stmt.close();
	        conn.commit();
	        conn.close();
	        System.out.println("IQI MINED SUCCESSFULLY: " + text);
	    } catch (SQLException e) {
	    	System.err.println("IQI Query NOT Completed: " + text);
	        //e.printStackTrace();
	    	e.toString();
	        
	        // Create file, so Producer can later put the job back into the Queue
			try {
				file.createNewFile();
			} catch (IOException e1) {
				System.err.println("IQI MINED FAILED, and FILE could not have been created: " + text);
				//e1.printStackTrace();
				e1.toString();
			}
	    }
	}
}
