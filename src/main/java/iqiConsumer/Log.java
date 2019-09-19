/**
 * This Application redirect System.Out and System.Err
 * to an output file
 * 
 * @author msuzuki
 * @version 1.0.0-SNAPSHOT
 * @since 09/13/2019
 */

package iqiConsumer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Log {
	
	private String appName;
	
	Log(String appName) {
		this.appName = appName;
	}
	
	public void redirect() {
		//DateFormatter df = new DateFormatter();
		//String prevMonYr = df.getPrevMonYear();
		
		// Redirect System.err
		try {
			System.setErr(new PrintStream(new FileOutputStream("ErrorLog", true), true));
			System.err.println("Hi from " + appName);
		} catch (FileNotFoundException e2) {
			//e2.printStackTrace();
			e2.toString();
		}
		
		// Redirect System.out
		try {
			System.setOut(new PrintStream(new FileOutputStream("SuccessLog", true), true));
			System.out.println("Hi from " + appName);
		} catch (FileNotFoundException e2) {
			//e2.printStackTrace();
			e2.toString();
		}
	}
}
