package collector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	 * @return returns a RTData
	 * @throws IOException
	 * 
	 * @author oskarnylen
	 */
	public static RTData getRTData(String symbol) throws IOException{

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
		URL ulr = new URL(YahooKeys.baseURL + symbol + "&f=" +
				YahooKeys.lastTradePrice +
				YahooKeys.orderBookRT +								//RÄTT?

				YahooKeys.name +
				YahooKeys.stockExchange +

				"&e=.csv");
		URLConnection urlConnection = ulr.openConnection();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {

				CSVParser csvp = new CSVParser();

				String[] yahooStockInfo = inputLine.split(",");

				price = csvp.parseToDouble(yahooStockInfo[0]);
				orderBook = csvp.parseToDouble(yahooStockInfo[1]);
				
				name = yahooStockInfo[2];
				stockExchange = yahooStockInfo[3];

				Stock stock = new Stock(name, symbol, stockExchange);

				RTDataPoint = new RTData(stock, date.getTime(), price, orderBook);
				break;  
			}
		}
		finally {
			if(reader != null) reader.close();
		}
		System.out.println(RTDataPoint.toString());
		return RTDataPoint;
	}
	
	
	/**
	 * Method that collects data from Yahoo into a DailyData-object. Also using CSVParser to be able to use the data
	 * correctly. Supposed to be used once a day after the markets are closed.
	 * 
	 * @param symbol - the symbol of the company
	 * @return returns a DailyData
	 * @throws IOException
	 * 
	 * @author oskarnylen
	 */
	public static DailyData getDailyData(String symbol) throws IOException{

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
		try {
			reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {

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

				dailyDataPoint = new DailyData(stock, date.getTime(), marketCap,
						dividentYield, PE, 
						PS, PEG, openPrice,
						closePrice, high,
						low, volume);
				break;  
			}
		}
		finally {
			if(reader != null) reader.close();
		}
		return dailyDataPoint;
	}
	
	/**
	 * Method that collects data from Yahoo into a QuarterlyData-object. Also using CSVParser to be able to use the data
	 * correctly. Supposed to be used four times a year.
	 * 
	 * @param symbol - the symbol of the company
	 * @return returns a QuarterlyData
	 * @throws IOException
	 * 
	 * @author oskarnylen
	 */
	public static QuarterlyData getQuarterlyData(String symbol) throws IOException{

		QuarterlyData quarterlyDataPoint = null;

		double yield = 0;
		double solidity = 0;
		double NAV = 0;
		double dividentPerShare = 0;

		Calendar date = Calendar.getInstance();
		String name;
		String stockExchange;
		
		/*
		 * Fetch CSV data from YahooFinance. The URL consists of a base + symbol of the stock + 
		 * necessary tag + keys + necessary tag.
		 */
		URL ulr = new URL(YahooKeys.baseURL + symbol + "&f=" +
				YahooKeys.dividentYield +							//"Div & Yield" in %
				YahooKeys.orderBookRT +								//Solidity
				YahooKeys.bookValue +								//Book Value Per Share
				YahooKeys.dividentPerShare +						//Divident / total shares
				
				YahooKeys.name +
				YahooKeys.stockExchange +

				"&e=.csv");
		URLConnection urlConnection = ulr.openConnection();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {

				CSVParser csvp = new CSVParser();

				String[] yahooStockInfo = inputLine.split(",");
				
				name = yahooStockInfo[2];
				stockExchange = yahooStockInfo[3];

				Stock stock = new Stock(name, symbol, stockExchange);

				quarterlyDataPoint = new QuarterlyData(stock, date.getTime(), yield, 
						solidity, NAV, dividentPerShare);
				break;  
			}
		}
		finally {
			if(reader != null) reader.close();
		}
		return quarterlyDataPoint;
	}
}
