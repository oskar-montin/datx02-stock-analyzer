package collector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * A parser class containing methods to parse data from Yahoo's website.
 * 
 * @author oskarnylen
 */

public class YahooParser {
	
	private static double value;
	
	/**
	 * Parses data from the balance sheet from the latest quarterly report.
	 * 
	 * @param symbol - the symbol of the company
	 * @param key - the key of the value eg: "Total Assets" or "Total Stockholder Equity"
	 * @return
	 * @throws IOException
	 */
	public static double balanceParser(String symbol, String key) {
		
        try {
			URL oracle = new URL("http://finance.yahoo.com/q/bs?s=" + symbol);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
			                            yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null){
				
				if(inputLine.contains(key)){
					in.mark(8);
					in.reset();
					while((inputLine = in.readLine()) != null){
						
						if(inputLine.matches(".*\\d.*")){
							
							String[] tempArray = inputLine.split("&");
						
							String tempString = tempArray[0].replace(",","");
							
							value = Double.parseDouble(tempString);

			    			break;
			    		}
					}
				}
			}
			in.close();
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			System.err.println("Caught NumberFormatException: " + e.getMessage());
			return Double.NEGATIVE_INFINITY;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println("Caught MalformedURLException: " + e.getMessage());
			return Double.NEGATIVE_INFINITY;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("IOException: " + e.getMessage());
			return Double.NEGATIVE_INFINITY;
		}
        
		return value;
	}
	
	/**
	 * NON COMPLETE METHOD.
	 * 
	 * @param symbol
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public static double generalParser(String symbol, String key) {
		
        try {
			URL oracle = new URL("http://finance.yahoo.com/q/ks?s=" + symbol);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
			                            yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null){
				if(inputLine.contains(">"+key)){

					String line = inputLine;
					
					int index = line.indexOf(key);
					
					String[] temp = line.split(key);
					
					System.out.println(temp[1]);
					
					String[] temp2 = temp[1].split("yfnc_tabledata1\">");
					
					String[] temp3 = temp2[1].split("<");
					
					String nonparsedValue = temp3[0];
					
					System.out.println(nonparsedValue);
				}
			}
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println("MalformedURLException: " + e.getMessage());
			return Double.NEGATIVE_INFINITY;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("IOException: " + e.getMessage());
			return Double.NEGATIVE_INFINITY;
		}
		return value;
	}
}
