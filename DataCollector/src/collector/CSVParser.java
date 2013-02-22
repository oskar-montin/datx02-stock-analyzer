package collector;

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
		
		if(returnString.equals("-") || returnString.equals("N/A")){
			return Double.NEGATIVE_INFINITY;
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
	public double parseToDoubleMarketCap(String str){
		Double returnValue;
		returnString = str;
		returnString.charAt(returnString.length()-1);
		
		if(returnString.charAt(returnString.length()-1) == 'B'){
			returnString = returnString.substring(0, returnString.length()-1);
			returnValue = Double.parseDouble(returnString);
			returnValue = returnValue*1000;
			
		} else if(returnString.charAt(returnString.length()-1) == 'M'){
			returnString = returnString.substring(0, returnString.length()-1);
			returnValue = Double.parseDouble(returnString);
			
		} else {
			returnValue = Double.parseDouble(returnString);
			returnValue = returnValue/1000000;
		}
		return returnValue;
	}
	
}
