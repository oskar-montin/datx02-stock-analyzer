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
import data.Stock;

/**
 * YahooInterface is a CLASS that communicates with YahooFinance in order to collect and compile the data into the right
 * data types.
 */

public class YahooInterface {

	/**
	 * Method that collects data from Yahoo into a DailyData-object. Also using CSVParser to be able to use the data
	 * correctly.
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

				Stock stock = new Stock(name, symbol);

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
	
}
