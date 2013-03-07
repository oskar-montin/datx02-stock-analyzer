package collector;

import data.LargeDouble;

/**
 * CSVParser is a help method to parse data specifically from YahooFinance
 * 
 * @author oskarnylen
 */

public class CSVParser {
	
	public static String returnString;
	
	/**
	 * Simple parse method that only does a check to see if there's data. If not, return NEGATIVE_INIFINITY, if there is
	 * return a double.
	 * 
	 * @param str - the string to be parsed
	 * @return
	 */
	public double parseToDouble(String str){
		returnString = str;
		
		if(returnString.equals("-") || returnString.equals("N/A") || returnString.equals("\"N/A\"")){
			return -Math.PI;
		}
		
		else {
			return Double.parseDouble(returnString);
		}
	}
	
	/**
	 * Simple parse method that only does a check to see if there's data. If not, return -1, if there is
	 * return a long.
	 * 
	 * @param str - the string to be parsed
	 * @return
	 */
	public long parseToLong(String str){
		returnString = str;
		
		if(returnString.equals("-") || returnString.equals("N/A")){
			return -1;
		}
		
		else {
			return Long.parseLong(returnString);
		}
	}
	
	/**
	 * Parse method to remove and rescale large numbers. If the input is in billion (dollars) rescale it to million
	 * If the input is in million don't rescale but remove the char
	 * If the input is under a million, rescale it to million
	 * 
	 * @param str - the string to be parsed
	 * @return a double in the scale of million dollars
	 */
	public LargeDouble parseToLargeDouble(String str){
		if(str.equals("-") || str.equals("N/A") || str.equals("\"N/A\"")){
			return new LargeDouble("");
		}
		
		return new LargeDouble(str);
	}
	
}
