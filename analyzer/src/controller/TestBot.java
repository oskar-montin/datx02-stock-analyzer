package controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

import util.IO;

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
		double [] stockValues = new double[stocks.length];
		while(currentDate<data[0].size()) {
			for(int i = 0; i<data.length;i++) {
				stockValues[i] = TA((ArrayList<DailyData>) data[i].subList(currentDate-42, currentDate));
			}
			ArrayList<Transaction> portfolio = portfolioConstructor(stockValues);
			for(Transaction t:portfolio) {
				user.performTransaction(t);
			}
			
			currentDate++;
		}
	}
	
	private ArrayList<Transaction> portfolioConstructor(double[] stockValues) {
		TreeMap<Double, SimpleData> buyCandidates = new TreeMap<Double,SimpleData>();
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		double balance = user.getBalance();
		
		for(int i = 0;i<stockValues.length;i++) {
			SimpleData today = data[i].get(data[i].size()-1);
			if(stockValues[i]>0.5) {
				buyCandidates.put(stockValues[i],today);
			} else if(stockValues[i]<0) {
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
	
	private TreeMap<SimpleData,Integer> distribute(TreeMap<Double, SimpleData> buyCandidates, double balance, int maxCount) {
		Entry<Double,SimpleData> entry;
		TreeMap<Double, SimpleData> choosenEntries = new TreeMap<Double, SimpleData>();
		TreeMap<SimpleData,Integer> distribution = new TreeMap<SimpleData,Integer>();
		int i = 0;
		double sum = 0;
		double usedBalance = 0;
		entry = buyCandidates.pollLastEntry();
		maxCount = Math.min(buyCandidates.size(), maxCount);
		//Choose entries:
		while(entry !=null && i<maxCount) {
			choosenEntries.put(entry.getKey(), entry.getValue());
			sum+=entry.getKey();
			i++;
			entry = buyCandidates.pollLastEntry();
		}
		
		//Calculate shares and add transactions
		for(Entry<Double, SimpleData> e: choosenEntries.entrySet()) {
			double share = balance*e.getKey()/sum;
			int amount = (int) (share/e.getValue().getValue());
			distribution.put(e.getValue(), amount);
			usedBalance += amount*e.getValue().getValue();
		}
		
		return distribution;
		
	}

	private double TA(ArrayList<DailyData> data) {
		TreeMap<Double, String> tempMap = new TreeMap<Double, String>(safetyMap);
		if(data != null && data.size()>42) {
			ArrayList<AnalysisMethod> methods = getMethods(data);
			for(Entry<Double,String> entry = tempMap.pollLastEntry(); entry != null; entry = tempMap.pollLastEntry()) {
				for(AnalysisMethod am:methods) {
					if(am.getName().equals(entry.getValue())) {
						double res = am.getSignal().getValue()*entry.getKey();
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
		methods.add(new RateOfChange(dataQueue, 10));
		return methods;
	}

	
	
}
