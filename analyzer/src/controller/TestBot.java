package controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeMap;

import util.IO;

import analyzer.AnalysisMethod;
import data.AnalyticsData;
import data.DailyData;
import data.SimpleData;
import data.Stock;
import data.Transaction;
import data.User;

public class TestBot {

	private User index;
	private User user;
	private AnalyticsData[] result;
	private Stock[] stocks;
	private List<SimpleData> indexList;
	private List<SimpleData> userList;
	private ArrayList<Calendar> dates;
	private final int startDate;
	private final int endDate;
	private int currentDate;
	private ArrayList<DailyData>[] data;
	private TreeMap<Double,String> safetyMap;
	
	public TestBot(int startDate, int testLength) {
		index = new User("Index");
		user = new User("Model");
		currentDate = 50;
		this.startDate = startDate;
		endDate = startDate+testLength;
		indexList = new LinkedList<SimpleData>();
		userList = new LinkedList<SimpleData>();
		init();
	}
	
	private void init() {
		safetyMap = (TreeMap<Double,String>) IO.loadFromFile("MethodSafeties.dat");
		PriorityQueue<Stock> temp = DatabaseHandler.getAllStocks();
		stocks = new Stock[temp.size()];
		stocks = temp.toArray(stocks);
		data = new ArrayList[stocks.length];
		dates = new ArrayList<Calendar>(DatabaseHandler.getDates());
		int i = 0;
		for(Stock s:stocks) {
			data[i] = new ArrayList<DailyData>(DatabaseHandler.getDailyData(s, dates.get(startDate), dates.get(endDate)));
			index.performTransaction(new Transaction(new SimpleData(data[i].get(currentDate-1)), 100));
		}
		user.deposit(index.getTotalAssets());
		indexList.add(new SimpleData(stocks[0], dates.get(startDate+currentDate-1), index.getTotalAssets()));
		userList.add(new SimpleData(stocks[0], dates.get(startDate+currentDate-1), index.getTotalAssets()));
	}
	
	public void run() {
		//for(int i = 0; )
	}
	
	private double TA(ArrayList<DailyData> data) {
		return currentDate;
	}

	
	
}
