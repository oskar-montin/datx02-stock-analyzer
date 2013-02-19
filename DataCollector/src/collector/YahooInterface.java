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

public class YahooInterface {

	public static DailyData getDailyData(String symbol) throws IOException{
		
		DailyData stockInfo = null;
		
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
		 * Fetch CSV data from Yahoo. The format codes (f=) are:
		 * s=symbol, l1 = last, c1 = change, d1 = trade day, t1 = trade time, o = open, h = high, g = low, v = volume
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
	            
	            stockInfo = new DailyData(stock, date.getTime(), marketCap,
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
        System.out.println(stockInfo.toString());
		return stockInfo;
     }
}
