package controller;

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
	
	private void start() {
		
		currentDateIndex = Collections.binarySearch(dates, startDate);
		while(dates.get(currentDateIndex).compareTo(endDate)<0) {
			LinkedList<DailyData>[] dailyData = ((LinkedList<DailyData>[])new LinkedList[stocks.size()]);
			QuarterlyData[] quarterlyData = new QuarterlyData[stocks.size()];
			int i = 0;
			for(Stock stock : stocks) {
				System.out.println("Get data for stock: "+stock.getSymbol()+" Date: "+ dates.get(currentDateIndex).get(Calendar.DATE)+"/"+dates.get(currentDateIndex).get(Calendar.MONTH));
				dailyData[i] = this.trimmedData(DatabaseHandler.getDailyData(stock), dates.get(currentDateIndex));
				quarterlyData[i] = DatabaseHandler.getQuarterlyData(stock);
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
	
	public static void main(String[] args) {
		ArrayList<Calendar> dates = new ArrayList<Calendar>(DatabaseHandler.getDates());
		Evaluater evaluater = new Evaluater(dates,dates.get(dates.size()-11), dates.get(dates.size()-1));
		AnalyticsData[][] ad = evaluater.getAnalyticsData();
		
		for(Calendar c:dates) {
			System.out.println("Day: "+c.get(Calendar.DATE)+" Month: "+c.get(Calendar.MONTH));
		}
	}
}
