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



	/**
	 * Parses data from the balance sheet from the latest quarterly report.
	 * 
	 * @param symbol - the symbol of the company
	 * @param key - the key of the value eg: "Total Assets" or "Total Stockholder Equity"
	 * @return
	 */
	public String balanceParser(String symbol, String key) {
		String value = "";
		try {
			URL oracle = new URL("http://finance.yahoo.com/q/bs?s=" + symbol);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null){

				if(inputLine.contains(key)){


					/*
					 * If the current line contains numbers, we know that the number we want is in this line.
					 */
					if(inputLine.matches(".*\\d.*")){



						String[] firstSplitIteration = inputLine.split("&");

						String[] secondSplitIteration = firstSplitIteration[0].split("\">");

						String untrimmedValue = secondSplitIteration[1].replace(",","");

						value = untrimmedValue.trim();

						break;
						/*
						 * Else, the number is in some of the next 8 upcoming lines
						 */
					}else {
						in.mark(8);
						in.reset();
						while((inputLine = in.readLine()) != null){
							if(inputLine.contains("-")){
								value = "";
								return value;
							}
							if(inputLine.matches(".*\\d.*")){

								String[] firstSplit = inputLine.split("&");

								String untrimmedValue = firstSplit[0].replace(",","");

								value = untrimmedValue.trim();

								break;
							}
						}
					}
				}
			}
			in.close();

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			System.err.println("Caught NumberFormatException in balanceParser: " + e.getMessage());
			return "";
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println("Caught MalformedURLException in balanceParser: " + e.getMessage());
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("IOException in balanceParser: " + e.getMessage());
			return "";
		}
		return value+"T";
	}

	/**
	 * Parses data from the general information about a company on Yahoo.
	 * 
	 * @param symbol - the symbol of the company
	 * @param key - the key of the value eg: "Return on Equity"
	 * @return
	 */
	public String generalParser(String symbol, String key) {
		String value = "";
		try {
			URL oracle = new URL("http://finance.yahoo.com/q/ks?s=" + symbol);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null){
				if(inputLine.contains(">"+key)){

					String line = inputLine;

					String[] temp = line.split(key);

					String[] temp2 = temp[1].split("yfnc_tabledata1\">");

					String[] temp3 = temp2[1].split("<");

					String nonparsedValue = temp3[0];

					String parsedValue = trimmer(nonparsedValue);

					value = parsedValue;
				}
			}
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println("MalformedURLException in generalParser: " + e.getMessage());
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("IOException in generalParser: " + e.getMessage());
			return "";
		}
		return value;
	}


	/**
	 * Parses data from the general information about a company on Yahoo.
	 * 
	 * @param symbol - the symbol of the company
	 * @param key - the key of the value eg: "Return on Equity"
	 * @return
	 */
	public String resultParser(String symbol, String key) {
		String value = "";
		try {
			URL oracle = new URL("http://finance.yahoo.com/q/is?s=" + symbol);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null){

				if(inputLine.contains(key)){

					/*
					 * Else, the number is in some of the next 8 upcoming lines
					 */
				
					in.mark(8);
					in.reset();
					while((inputLine = in.readLine()) != null){
						if(inputLine.contains("-")){
							value = "";
							return value;
						}
						if(inputLine.matches(".*\\d.*")){

							String[] firstSplit = inputLine.split("&");

							String untrimmedValue = firstSplit[0].replace(",","");

							value = untrimmedValue.trim();

							break;
						}
					}
				}
			}

			in.close();

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			System.err.println("Caught NumberFormatException in balanceParser: " + e.getMessage());
			return "";
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println("Caught MalformedURLException in balanceParser: " + e.getMessage());
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("IOException in balanceParser: " + e.getMessage());
			return "";
		}
		return value+"T";
	}

	public String trimmer(String s){

		String returnValue = s.trim();

		if(s.contains("N/A")){
			return "";
		}
		if(s.contains(",")){
			String temp = s.replace(",", "");
			return temp;
		}
		if(s.contains("%")){
			Double temp2;
			String[] temp;
			temp = s.split("%");
			temp2 = Double.parseDouble(temp[0]);
			temp2 = temp2/100;
			returnValue = temp2+"";
			return returnValue;
		}
		return returnValue;

	}
}
