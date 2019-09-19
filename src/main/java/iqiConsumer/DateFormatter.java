/**
 * This Application Gets the Dates from Previous Month
 * Start and End Dates
 * Previous Month and Year
 * 
 * @author msuzuki
 * @version 1.0.0-SNAPSHOT
 * @since 09/13/2019
 */

package iqiConsumer;

import java.util.Calendar;
import java.util.Date;

public class DateFormatter {
	
	// Set Date
	public static Calendar setDate() {
		Calendar c = Calendar.getInstance();

		Integer curMonth = c.get(Calendar.MONTH)+ 1; 
		Integer decMonth;
		
		if(curMonth == 2) {
			decMonth = -27;
		} else {
			decMonth = -30;
		}
		
		// Set-up new time. In this case 30days ago.
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_YEAR, decMonth);
		return c;
	}
	
	// Get Previous Month Start and End Dates
	public static String[] getPrevMonStEndDate() {
		
		Calendar c = setDate();
		String[] startEndDate = new String[2];
		
		Integer prevMonth = c.get(Calendar.MONTH);
		Integer prevYear;
		
		if(prevMonth == 0) {
			prevYear = c.get(Calendar.YEAR) - 1;
		} else {
			prevYear = c.get(Calendar.YEAR);
		}
		
		startEndDate[0] = prevYear + "-" + (c.get(Calendar.MONTH) + 1) + "-" + 1;
		startEndDate[1] = prevYear + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.getActualMaximum(c.DAY_OF_MONTH);
			
		return startEndDate;
	}
	
	// Get Previous Month and Current Year
	public static String getPrevMonYear() {
		
		Calendar c = setDate();
		
		Integer prevMonth = c.get(Calendar.MONTH);
		Integer prevYear;
		
		if(prevMonth == 0) {
			prevYear = c.get(Calendar.YEAR) - 1;
		} else {
			prevYear = c.get(Calendar.YEAR);
		}
		
		String prevMonYr = c.get(Calendar.MONTH) + 1 + "-" + prevYear;
			
		return prevMonYr;
	}
}
