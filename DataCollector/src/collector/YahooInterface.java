package collector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import data.DailyData;
import data.QuarterlyData;
import data.RTData;
import data.Stock;

/**
 * YahooInterface is a CLASS that communicates with YahooFinance in order to collect and compile the data into the right
 * data types. 
 */

public class YahooInterface {

	/**
	 * Method that collects data from Yahoo into a RTData-object. Also using CSVParser to be able to use the data
	 * correctly. Supposed to be used several times during the trading hours of the day.
	 * 
	 * @param symbol - the symbol of the company
	 * @return returns a RTData object
	 * 
	 * @author oskarnylen
	 */
	public static RTData getRTData(String symbol) {

		RTData RTDataPoint = null;

		double price;
		double orderBook;									//VÄRDE?

		Calendar date = Calendar.getInstance();
		String name;
		String stockExchange;
		
		/*
		 * Fetch CSV data from YahooFinance. The URL consists of a base + symbol of the stock + 
		 * necessary tag + keys + necessary tag.
		 */
		try {
			URL ulr = new URL(YahooKeys.baseURL + symbol + "&f=" +
					YahooKeys.lastTradePrice +
					YahooKeys.orderBookRT +								//RÄTT?

					YahooKeys.name +
					YahooKeys.stockExchange +

					"&e=.csv");
			URLConnection urlConnection = ulr.openConnection();
			BufferedReader reader = null;

			reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {

				/*
				 * Creating a CSVParser to parse the data correctly
				 */
				CSVParser csvp = new CSVParser();

				String[] yahooStockInfo = inputLine.split(",");

				price = csvp.parseToDouble(yahooStockInfo[0]);
				orderBook = csvp.parseToDouble(yahooStockInfo[1]);

				name = yahooStockInfo[2];
				stockExchange = yahooStockInfo[3];

				Stock stock = new Stock(name, symbol, stockExchange);

				RTDataPoint = new RTData(stock, date, price, orderBook);
				break;  
			}


			if(reader != null) reader.close();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println("MalformedURLException in getRTData: " + e.getMessage());
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("IOException in getRTData: " + e.getMessage());
			return null;
		}

		return RTDataPoint;
	}
	
	
	/**
	 * Method that collects data from Yahoo into a DailyData-object. Also using CSVParser to be able to use the data
	 * correctly. Supposed to be used once a day after the markets are closed.
	 * 
	 * @param symbol - the symbol of the company
	 * @return returns a DailyData object
	 * 
	 * @author oskarnylen
	 */
	public static DailyData getDailyData(String symbol) {

		DailyData dailyDataPoint = null;

		/* KEYS */
		double marketCap;
		double dividentYield;
		double PE;
		double PS;
		double PEG;

		/* VALUES */
		double openPrice;
		double closePrice;
		double high;
		double low;
		long volume;

		Calendar date = Calendar.getInstance();
		String name;
		String stockExchange;

		/*
		 * Fetch CSV data from YahooFinance. The URL consists of a base + symbol of the stock + 
		 * necessary tag + keys + necessary tag.
		 */
		try {
			URL ulr = new URL(YahooKeys.baseURL + symbol + "&f=" +
					YahooKeys.marketCap + 
					YahooKeys.dividentYield + 
					YahooKeys.PERatio +
					YahooKeys.PSRatio +
					YahooKeys.PEGRatio +

					YahooKeys.open +
					YahooKeys.previousClose +
					YahooKeys.highLimit +
					YahooKeys.lowLimit +
					YahooKeys.volume +

					YahooKeys.name +
					YahooKeys.stockExchange +

					"&e=.csv");
			URLConnection urlConnection = ulr.openConnection();
			BufferedReader reader = null;

			reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {

				/*
				 * Creating a CSVParser to parse the data correctly
				 */
				CSVParser csvp = new CSVParser();

				String[] yahooStockInfo = inputLine.split(",");

				marketCap = csvp.parseToDoubleMarketCap(yahooStockInfo[0]);
				dividentYield = csvp.parseToDouble(yahooStockInfo[1]);
				PE = csvp.parseToDouble(yahooStockInfo[2]);
				PS = csvp.parseToDouble(yahooStockInfo[3]);
				PEG = csvp.parseToDouble(yahooStockInfo[4]);

				openPrice = csvp.parseToDouble(yahooStockInfo[5]);
				closePrice = csvp.parseToDouble(yahooStockInfo[6]);
				high = csvp.parseToDouble(yahooStockInfo[7]);
				low = csvp.parseToDouble(yahooStockInfo[8]);
				volume = csvp.parseToLong(yahooStockInfo[9]);

				name = yahooStockInfo[10];
				stockExchange = yahooStockInfo[11];

				Stock stock = new Stock(name, symbol, stockExchange);

				dailyDataPoint = new DailyData(stock, date, marketCap,
						dividentYield, PE, 
						PS, PEG, openPrice,
						closePrice, high,
						low, volume);
				break;  
			}


			if(reader != null) reader.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println("MalformedURLException in getDailyData: " + e.getMessage());
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("IOException in getDailyData: " + e.getMessage());
			return null;
		}

		return dailyDataPoint;
	}
	
	/**
	 * Method that collects data from Yahoo into a QuarterlyData-object. Also using CSVParser to be able to use the data
	 * correctly. Supposed to be used four times a year.
	 * 
	 * @param symbol - the symbol of the company
	 * @return returns a QuarterlyData object
	 * 
	 * @author oskarnylen
	 */
	public static QuarterlyData getQuarterlyData(String symbol) {

		QuarterlyData quarterlyDataPoint = null;

		double yield;
		double solidity;
		double NAV = Double.NEGATIVE_INFINITY;
		double dividentPerShare;

		Calendar date = Calendar.getInstance();
		String name;
		String stockExchange;
		
		/*
		 * Fetch CSV data from YahooFinance. The URL consists of a base + symbol of the stock + 
		 * necessary tag + keys + necessary tag.
		 */
		try {
			URL ulr = new URL(YahooKeys.baseURL + symbol + "&f=" +
					YahooKeys.dividentYield +							//"Div & Yield" in %
																		
					YahooKeys.dividentPerShare +						//Divident / total shares
					
					YahooKeys.name +
					YahooKeys.stockExchange +

					"&e=.csv");
			URLConnection urlConnection = ulr.openConnection();
			BufferedReader reader = null;

			reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {

				CSVParser csvp = new CSVParser();
				YahooParser yp = new YahooParser();


				String[] yahooStockInfo = inputLine.split(",");

				yield = csvp.parseToDouble(yahooStockInfo[0]);
				dividentPerShare = csvp.parseToDouble(yahooStockInfo[1]);
				
				solidity = yp.balanceParser(symbol, "Total Stockholder Equity")/
							yp.balanceParser(symbol, "Total Assets");


				name = yahooStockInfo[2];
				stockExchange = yahooStockInfo[3];

				Stock stock = new Stock(name, symbol, stockExchange);

				quarterlyDataPoint = new QuarterlyData(stock, date, yield, 
						solidity, NAV, dividentPerShare);
				break;  
			}


			if(reader != null) reader.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println("MalformedURLException in getQuarterlyData: " + e.getMessage());
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("IOException in getQuarterlyData: " + e.getMessage());
			return null;
		}

		return quarterlyDataPoint;
	}
}
