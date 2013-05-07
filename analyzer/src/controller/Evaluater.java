package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

import data.AnalyticsData;
import data.DailyData;
import data.QuarterlyData;
import data.Stock;

public class Evaluater {
	private final Calendar startDate;
	private final Calendar endDate;
	private AnalyticsData[][] data;
	private PriorityQueue<Stock> stocks;
	private AnalyticsBot bot;
	private int currentDateIndex;
	private ArrayList<Calendar> dates;
	
	public Evaluater(ArrayList<Calendar> dates, Calendar startDate, Calendar endDate) {
		this.dates = new ArrayList<Calendar>(dates);
		this.startDate = startDate;
		this.endDate = endDate;
		stocks = DatabaseHandler.getAllStocks();
		bot = new AnalyticsBot(stocks);
		this.start();
	}
	
	private LinkedList<DailyData> trimmedData(PriorityQueue<DailyData> data, Calendar date) {
		LinkedList<DailyData> trimmedData = new LinkedList<DailyData>();
		for(DailyData dd:data) {
			Calendar currDate = dd.getDate();
			
			//System.out.println("Input Date: "+ date.get(Calendar.DATE)+"/"+date.get(Calendar.MONTH));

			if(currDate.compareTo(date)<=0) {
				trimmedData.add(dd);
			} else {
				break;
			}
		}
		
		return trimmedData;
	}
	
	private ArrayList<DailyData>[] getAllData() {
		ArrayList<DailyData>[] allData = new ArrayList[stocks.size()];
		int i = 0;
		for(Stock stock : stocks) {
			allData[i] = new ArrayList<DailyData>(DatabaseHandler.getDailyData(stock, dates.get(0), dates.get(dates.size()-1)));
			i++;
		}
		return allData;
	}
	
	private void start() {
		ArrayList<DailyData>[] inputData = getAllData();
		currentDateIndex = Collections.binarySearch(dates, startDate);
		while(dates.get(currentDateIndex).compareTo(endDate)<0) {
			LinkedList<DailyData>[] dailyData = ((LinkedList<DailyData>[])new LinkedList[stocks.size()]);
			QuarterlyData[] quarterlyData = new QuarterlyData[stocks.size()];
			int i = 0;
			System.out.println("Input Date: "+ dates.get(currentDateIndex).get(Calendar.DATE)+"/"+dates.get(currentDateIndex).get(Calendar.MONTH)+" - "+dates.get(currentDateIndex).get(Calendar.YEAR));
			for(Stock stock : stocks) {
				/*
				if(inputData[i] == null) {
					inputData[i] = DatabaseHandler.getDailyData(stock);
				}*/
				if(quarterlyData[i] == null) {
					quarterlyData[i] = DatabaseHandler.getQuarterlyData(stock);
				}
				//dailyData[i] = this.trimmedData(inputData[i], dates.get(currentDateIndex));
				//dailyData[i] = new LinkedList<DailyData>(DatabaseHandler.getDailyData(stock, dates.get(currentDateIndex-50), dates.get(currentDateIndex)));
				try {
				dailyData[i] = new LinkedList<DailyData>(inputData[i].subList(currentDateIndex-50, currentDateIndex));
				} catch(IndexOutOfBoundsException e) {
					dailyData[i] = new LinkedList<DailyData>();
				}
				i++;
			}
			bot.feed(dailyData, quarterlyData);
			currentDateIndex++;
		}
		data = bot.evaluate();
	}
	
	public AnalyticsData[][] getAnalyticsData() {
		return data;
	}
	
	
	public void printMethodStat() {
		
		String totalString = new String();
		String[] perStockStrings = new String[data[0].length];
		Stock [] stockArray = new Stock[perStockStrings.length];
		stockArray = stocks.toArray(stockArray);
		//Initiate per stock strings
		for(int n = 0;n<stockArray.length;n++) {
			perStockStrings[n] = "";
		}
		//For each analysismethod:
		for(int i = 0; i<data.length;i++) {
			double successRate = 0;
			double worstSuccessRate = 1.0;
			double stillOwned = 0;
			double timesSucceeded = 0;
			double timesFailed = 0;
			double profit = 0;
			int totalBuySell = 0;
			String outputString = "";
			for(int j = 0; j< data[0].length; j++) {
				worstSuccessRate = Math.min(worstSuccessRate, data[i][j].getSuccessRate());
				stillOwned += data[i][j].getAmountStillOwned();
				timesFailed += data[i][j].getTimesFailed();
				timesSucceeded += data[i][j].getTimesSucceeded();
				profit += data[i][j].getTotalProfit();
				totalBuySell += data[i][j].amountOfPairs();
				perStockStrings[j] += data[i][j].getAnalysisMethod()+":\n"+"Success rate: " + data[i][j].getSuccessRate()   + "\n" +
						   "Profit: " + data[i][j].getTotalProfit() + " - Stock avarage: "+data[i][j].getTotalProfit()/data[i].length+"\n" +
						   "Still owned: " + data[i][j].getAmountStillOwned() + " - Avarage: "+data[i][j].getAmountStillOwned()/data[0].length +"\n" +
						   "Times Succeeded: " + data[i][j].getTimesSucceeded() + " - Avarage: "+data[i][j].getTimesSucceeded()/data[0].length + "\n" +
						   "Times failed: " + data[i][j].getTimesFailed() + " - Avarage: "+data[i][j].getTimesFailed()/data[0].length + "\n" +
						   "Total buy/sell pairs: " + data[i][j].amountOfPairs() + " of " + data[i].length + " stocks. - Avg: " + ((double)data[i][j].amountOfPairs()/data[0].length)+"\n";

			}
			successRate = timesSucceeded/(timesSucceeded+timesFailed);
			outputString = data[i][0].getAnalysisMethod()+":"+"\n"+"Success rate: " + successRate +  " Worst:"+ worstSuccessRate+"\n" +
					   "Profit: " + profit + " - Stock avarage: "+profit/data[0].length+"\n" +
					   "Still owned: " + stillOwned + " - Avarage: "+stillOwned/data[0].length +"\n" +
					   "Times Succeeded: " + timesSucceeded + " - Avarage: "+timesSucceeded/data[0].length + "\n" +
					   "Times failed: " + timesFailed + " - Avarage: "+timesFailed/data[0].length + "\n" +
					   "Total buy/sell pairs: " + totalBuySell + " of " + data[i].length + " stocks. - Avg: " + ((double)totalBuySell/data[0].length)+"\n";

			System.out.println(outputString);
			totalString +=outputString+"\n";
		}
		writeToFile(totalString, "MethodStatistics.txt");
		
		for(int n = 0; n<perStockStrings.length;n++) {
			writeToFile(perStockStrings[n], stockArray[n].getSymbol()+".txt");
		}
	}
	
	private void writeToFile(String content, String fileName) {
		FileOutputStream fop = null;
		File file;
 
		try {
 
			file = new File(fileName);
			fop = new FileOutputStream(file);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
			System.out.println("Wrote to file: "+fileName);
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		
		ArrayList<Calendar> dates = new ArrayList<Calendar>(DatabaseHandler.getDates());
		Evaluater evaluater = new Evaluater(dates,dates.get(dates.size()-80), dates.get(dates.size()-1));
		AnalyticsData[][] ad = evaluater.getAnalyticsData();
		evaluater.printMethodStat();
		
	}
}
