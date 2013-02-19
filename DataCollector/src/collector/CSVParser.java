package collector;

public class CSVParser {
	
	public static String returnString;
	
	public double parseToDouble(String str){
		Double returnValue;
		returnString = str;
		
		if(returnString.equals("-")){
			return Double.NEGATIVE_INFINITY;
		}
		else {
			return Double.parseDouble(returnString);
		}
	}
	
	public long parseToLong(String str){
		long returnValue;
		returnString = str;
		
		if(returnString.equals("-")){
			return -1;
		}
		else {
			return Long.parseLong(returnString);
		}
	}
	
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
