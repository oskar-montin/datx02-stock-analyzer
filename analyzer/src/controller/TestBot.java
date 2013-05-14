package controller;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

import util.IO;
import util.Util;

import analyzer.AnalysisMethod;
import analyzer.RateOfChange;
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
	private PriorityQueue <Entry<String,Double>> safetyQueue;
	
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
		TreeMap<String,Double> safetyMap = (TreeMap<String,Double>) IO.loadFromFile("MethodSafeties.dat");
		Comparator<Entry<String,Double>> comp = Util.entryValueComparator();
		safetyQueue = new PriorityQueue<Entry<String,Double>>(safetyMap.size(),comp);
		for(Entry<String,Double> e:safetyMap.entrySet()) {
			safetyQueue.add(e);
		}
		
		PriorityQueue<Stock> temp = DatabaseHandler.getAllStocks();
		stocks = new Stock[temp.size()];
		int i = 0;
		for(Stock s:temp) {
			System.out.println(s.getSymbol());
			stocks[i] = new Stock(s.getName(), s.getSymbol(), s.getBusiness(), s.getStockExchange());
			i++;
		}
		data = new ArrayList[stocks.length];
		dates = new ArrayList<Calendar>(DatabaseHandler.getDates());
		i=0;
		for(Stock s:stocks) {
			ArrayList<DailyData> a = new ArrayList<DailyData>(DatabaseHandler.getDailyData(s, dates.get(0), dates.get(dates.size()-1)));
			PriorityQueue<DailyData> dd = DatabaseHandler.getDailyData(s, dates.get(startDate), dates.get(endDate));
			data[i] = new ArrayList<DailyData>(dd);
			if(data[i].size()==endDate-startDate) {
				SimpleData firstDay = new SimpleData(data[i].get(currentDate-1));
				index.deposit(firstDay.getValue()*100);
				index.performTransaction(new Transaction(firstDay, 100));
			} else {
				data[i] = new ArrayList<DailyData>();
			}
			i++;
		}
		user.deposit(index.getTotalAssets());
		indexList.add(new SimpleData(stocks[0], dates.get(startDate+currentDate-1), index.getTotalAssets()));
		userList.add(new SimpleData(stocks[0], dates.get(startDate+currentDate-1), index.getTotalAssets()));
	}
	
	public void run() {
		double [] stockValues = new double[stocks.length];
		String userCsv = ""+user.getTotalAssets();
		String indexCsv = ""+index.getTotalAssets();
		while(currentDate<endDate-startDate) {
			for(int i = 0; i<data.length;i++) {
				//System.out.println(data[i].get(0).getSymbol());
				if(data[i].size()==endDate-startDate) {
					stockValues[i] = TA(new ArrayList<DailyData>(data[i].subList(currentDate-42, currentDate)));
				} else {
					stockValues[i] = 0;
				}
			}
			ArrayList<Transaction> portfolio = portfolioConstructor(stockValues);
			for(Transaction t:portfolio) {
				user.performTransaction(t);
			}
			userCsv+=","+user.getTotalAssets();
			indexCsv+=","+index.getTotalAssets();
			currentDate++;
		}
		IO.writeToFile(userCsv, "user.csv");
		IO.writeToFile(indexCsv, "index.csv");
	}
	
	private ArrayList<Transaction> portfolioConstructor(double[] stockValues) {
		TreeMap<SimpleData,Double> buyCandidates = new TreeMap<SimpleData,Double>();
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		double balance = user.getBalance();
		SimpleData today = null;
		for(int i = 0;i<stockValues.length;i++) {
			DailyData[] dailyData = new DailyData[data[i].size()];
			dailyData = data[i].toArray(dailyData);
			if(stockValues[i]>0.2) {
				System.out.println(dailyData[currentDate].equals(today));
				System.out.println(data[i].get(currentDate).getStock().getSymbol());
				//today = new DailyData(dailyData[currentDate]);
				//today = data[i].get(currentDate);
				//buyCandidates.put(today,stockValues[i]);
				buyCandidates.put(new SimpleData(data[i].get(currentDate)),stockValues[i]);
			} else if(stockValues[i]<0) {
				today = data[i].get(currentDate);
				if(user.ownStock(today.getStock())) {
					int amount = user.amountOfStocks(today.getStock());
					user.performTransaction(new Transaction(today,-amount));
				}
			}
		}
		int maxCount = 15;
		//Bestäm balance och maxCount:
		//TODO
		balance = balance*0.75;
		TreeMap<SimpleData,Integer> distribution = distribute(buyCandidates,balance,maxCount);
		
		for(Entry<SimpleData,Integer> entry : distribution.entrySet()){
			transactions.add(new Transaction(entry.getKey(),entry.getValue()));
		}
		
		return transactions;
	}
	
	private TreeMap<SimpleData,Integer> distribute(TreeMap<SimpleData,Double> buyCandidates, double balance, int maxCount) {
		Entry<SimpleData,Double> entry;
		TreeMap<SimpleData,Double> choosenEntries = new TreeMap<SimpleData,Double>();
		TreeMap<SimpleData,Integer> distribution = new TreeMap<SimpleData,Integer>();
		int i = 0;
		double sum = 0;
		double usedBalance = 0;
		maxCount = Math.min(buyCandidates.size(), maxCount);
		entry = buyCandidates.pollLastEntry();
		
		//Choose entries:
		while(entry !=null && i<maxCount) {
			choosenEntries.put(entry.getKey(), entry.getValue());
			sum+=entry.getValue();
			i++;
			entry = buyCandidates.pollLastEntry();
		}
		
		//Calculate shares and add transactions
		for(Entry<SimpleData,Double> e: choosenEntries.entrySet()) {
			double share = balance*e.getValue()/sum;
			int amount = (int) (share/e.getKey().getValue());
			distribution.put(e.getKey(), amount);
			usedBalance += amount*e.getKey().getValue();
		}
		
		return distribution;
		
	}

	private double TA(ArrayList<DailyData> data) {
		
		if(data != null && data.size()>=42) {
			ArrayList<AnalysisMethod> methods = getMethods(data);
			for(Entry<String,Double> entry:safetyQueue) {
				for(AnalysisMethod am:methods) {
					if(am.getName().equals(entry.getKey())) {
						double res = am.getSignal().getValue()*entry.getValue();
						if(res!=0) {
							return res;
						}
					}
				}
			}
		}
		return 0;
	}

	private ArrayList<AnalysisMethod> getMethods(ArrayList<DailyData> data2) {
		PriorityQueue<DailyData> dataQueue = new PriorityQueue<DailyData>(data2);
		ArrayList<AnalysisMethod> methods = new ArrayList<AnalysisMethod>();
		methods.add(new RateOfChange(dataQueue, 7));
		return methods;
	}

	public static void main(String[] args) {
		ArrayList<Calendar> dates = new ArrayList<Calendar>(DatabaseHandler.getDates());
		TestBot bot = new TestBot(45, 338);
		bot.run();
		System.out.println(bot.user.getTotalAssets());
	}
	
	
}