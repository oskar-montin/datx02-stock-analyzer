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

import combinations.BBRSI;
import combinations.BBStochRSI1;
import combinations.BBStochRSI2;
import combinations.CMFEMA;
import combinations.MACDCMF1;
import combinations.MACDCMF3;
import combinations.MACDCMF4;
import combinations.MACDDerivativeSO;
import combinations.MACDHistogramRSI;
import combinations.MACDHistogramRSI2;
import combinations.MACDHistogramRSI3;
import combinations.MACDROC;
import combinations.MACDROC2;
import combinations.MACDROC3;
import combinations.MACDRSI;
import combinations.MACDRSI2;
import combinations.MACDRSI3;
import combinations.MACDRSI4;
import combinations.MACDValueSO2;
import combinations.RSISOCMFROC;
import combinations.SORSIROC;
import combinations.VBROC;

import util.ExcelWriter;
import util.IO;
import util.Util;

import analyzer.AnalysisMethod;
import analyzer.BollingerBands;
import analyzer.Fibonacci;
import analyzer.MACD;
import analyzer.RateOfChange;
import analyzer.RelativeStrengthIndex;
import analyzer.StochRSI;
import analyzer.VolatilityBands;
import data.AnalyticsData;
import data.DailyData;
import data.ProfitRatio;
import data.SimpleData;
import data.Stock;
import data.Transaction;
import data.User;

public class TestBot3 {

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
	private ArrayList<Entry<String,Double>> safetyQueue;
	private ArrayList<ProfitRatio> profitRatios;
	private ArrayList<Entry<String,Double>> expectedList;
	private double expectedSum;
	private double[] bestBuyValues;
	
	public TestBot3(int startDate, int testLength) {
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
		safetyQueue = new ArrayList<Entry<String,Double>>(safetyMap.size()+1);
		profitRatios = (ArrayList<ProfitRatio>) IO.loadFromFile("ProfitRatios.dat");
		expectedList = new ArrayList<Entry<String,Double>>(safetyMap.size()+1);
		expectedSum = 0;
		
		for(Entry<String,Double> e:safetyMap.entrySet()) {
			safetyQueue.add(e);
			for(ProfitRatio pr:profitRatios) {
				if(e.getKey().equals(pr.getMethod())){
					double expected = e.getValue()*getProfitPerSuccess(pr.getMethod())-
							e.getValue()*getLossPerFailure(pr.getMethod());
					if(!(new Double(expected).equals(Double.NaN))) {
						expectedSum+=expected;
					}
					
				}
				 
			}
		}
		TreeMap<String,Double> tempExpectedMap = new TreeMap<String,Double>();
		for(Entry<String,Double> entry:safetyMap.entrySet()) {
			for(ProfitRatio pr:profitRatios) {
				if(entry.getKey().equals(pr.getMethod())){
					double expectedValue = entry.getValue()*getProfitPerSuccess(pr.getMethod())-
							entry.getValue()*getLossPerFailure(pr.getMethod()); 
					expectedValue /= expectedSum;
					tempExpectedMap.put(pr.getMethod(), new Double(expectedValue));
					expectedList.add(tempExpectedMap.pollFirstEntry());
				}
				 
			}
		}
		
		Collections.sort(expectedList, comp);
		
		
		
		
		//
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
		bestBuyValues = new double[stocks.length];
		i=0;
		for(Stock s:stocks) {
			ArrayList<DailyData> a = new ArrayList<DailyData>(DatabaseHandler.getDailyData(s, dates.get(0), dates.get(dates.size()-1)));
			PriorityQueue<DailyData> dd = DatabaseHandler.getDailyData(s, dates.get(startDate), dates.get(endDate));
			data[i] = new ArrayList<DailyData>(dd);
			if(data[i].size()==endDate-startDate) {
				SimpleData firstDay = new SimpleData(data[i].get(currentDate-1));
				int amountfStocks = (int)Math.round(100000/firstDay.getValue());
				index.deposit(firstDay.getValue()*amountfStocks);
				user.deposit(firstDay.getValue()*amountfStocks);
				index.performTransaction(new Transaction(firstDay, amountfStocks));
				
			} else {
				data[i] = new ArrayList<DailyData>();
			}
			bestBuyValues[i]=0;
			i++;
		}
		
		indexList.add(new SimpleData(stocks[0], dates.get(startDate+currentDate-1), index.getTotalAssets()));
		userList.add(new SimpleData(stocks[0], dates.get(startDate+currentDate-1), index.getTotalAssets()));
	}
	
	public void run() {
		ExcelWriter excelWriter = new ExcelWriter();
		double [] stockValues = new double[stocks.length];
		String userCsv = ""+user.getTotalAssets();
		String indexCsv = ""+index.getTotalAssets();
		String[] input = new String[2];
		input[0] = "Index";
		input[1] = "Bot";
		excelWriter.addRow(input, 0);
		int rn = 1;
		while(currentDate<endDate-startDate) {
			System.out.println("Index: "+currentDate);
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
			Double [] assets = new Double[2];
			assets[0] = getTotalAssets(index);
			assets[1] = getTotalAssets(user);
			//input[0] = new String(""+index.getTotalAssets()).replace('.', ',');
			//input[1] = new String(""+user.getTotalAssets()).replace('.', ',');
			excelWriter.addRow(assets, rn);
			rn++;
			currentDate++;
		}
		excelWriter.save("BotIndexFile.xls");
		IO.writeToFile(userCsv, "user.csv");
		IO.writeToFile(indexCsv, "index.csv");
	}
	
	private double getTotalAssets(User user) {
		double totalAssets = user.getBalance();
		for(int i = 0;i<stocks.length;i++) {
			if(data[i]!=null && data[i].size()>currentDate) {
				double close = data[i].get(currentDate).getClosePrice();
				int amount = user.amountOfStocks(stocks[i]);
				totalAssets +=close*amount;
			}
		}
		return totalAssets;
	}
	
	private boolean stop(SimpleData todayStock) {
		for(Entry<SimpleData,Integer> owned:user.getOwnedStocks().entrySet()) {
			if(owned.getKey().getStock().compareTo(todayStock.getStock())==0) {
				double border = owned.getKey().getValue()*0.95;
				if(todayStock.getValue()<border) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	private ArrayList<Transaction> portfolioConstructor(double[] stockValues) {
		TreeMap<SimpleData,Double> buyCandidates = new TreeMap<SimpleData,Double>();
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		
		SimpleData today = null;
		for(int i = 0;i<stockValues.length;i++) {
			DailyData[] dailyData = new DailyData[data[i].size()];
			dailyData = data[i].toArray(dailyData);
			
			if(stockValues[i]>0) {
				System.out.println(dailyData[currentDate].equals(today));
				System.out.println(data[i].get(currentDate).getStock().getSymbol());
				//today = new DailyData(dailyData[currentDate]);
				//today = data[i].get(currentDate);
				//buyCandidates.put(today,stockValues[i]);
				bestBuyValues[i] = Math.max(bestBuyValues[i], stockValues[i]);
				buyCandidates.put(new SimpleData(data[i].get(currentDate)),stockValues[i]);
			} else if(stockValues[i]<0 ) {
				if(bestBuyValues[i]<=Math.abs(stockValues[i])) {
					today = data[i].get(currentDate);
					//today = data[i].get(currentDate);
					int amount = user.amountOfStocks(today.getStock());
					if(amount!=0) {
						user.performTransaction(new Transaction(today,-amount));
					}
				}
			}
		}
		int maxCount = 15;
		//Bestäm balance och maxCount:
		//TODO
		double balance = user.getBalance();
		//balance = balance*0.80;
		TreeMap<SimpleData,Integer> distribution = distribute(buyCandidates,balance,maxCount);
		
		for(Entry<SimpleData,Integer> entry : distribution.entrySet()){
			transactions.add(new Transaction(entry.getKey(),entry.getValue().intValue()));
		}
		
		return transactions;
	}
	
	private TreeMap<SimpleData,Integer> distribute(TreeMap<SimpleData,Double> buyCandidates, double balance, int maxCount) {

		TreeMap<SimpleData,Double> choosenEntries = new TreeMap<SimpleData,Double>();
		TreeMap<SimpleData,Integer> distribution = new TreeMap<SimpleData,Integer>();
		if(buyCandidates.size()==0) {
			return distribution;
		}
		int i = 0;
		double sum = 0;
		double usedBalance = 0;
		maxCount = Math.min(buyCandidates.size(), maxCount);
		
		Comparator<Entry<SimpleData,Double>> comp = Util.entryValueComparator();
		ArrayList<Entry<SimpleData,Double>> choosenQueue = new ArrayList<Entry<SimpleData,Double>>(buyCandidates.size());
		
		//Fill queue
		for(Entry<SimpleData,Double> e:buyCandidates.entrySet()) {
			choosenQueue.add(e);
		}
		Collections.sort(choosenQueue,comp);
		//Choose entries:entry = buyCandidates.pollLastEntry();
		for(Entry<SimpleData,Double> entry:choosenQueue) {
			if(i>=maxCount) {
				break;
			}
			choosenEntries.put(entry.getKey(), entry.getValue());
			sum+=entry.getValue();
			i++;
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
			for(Entry<String,Double> e:expectedList){
				for(AnalysisMethod am:methods) {
					if(am.getName().equals(e.getKey())) {
						if((new Double(e.getValue()).equals(Double.NaN))) {
							continue;
						}
						if(e.getValue()<0 || e.getValue()>1) {
							continue;
						}
						double res = am.getSignal().getValue()*e.getValue();
						if(res!=0) {
							System.out.println(data.get(0).getStock().getName()+" "+e.getKey()+" "+am.getSignal());
							return res;
						}
					}
				}
			}
			/*
			ArrayList<AnalysisMethod> methods = getMethods(data);
			for(Entry<String,Double> entry:safetyQueue) {
				for(AnalysisMethod am:methods) {
					if(am.getName().equals(entry.getKey())) {
						double expectedValue = entry.getValue()*getProfitPerSuccess(am.getName())-
												entry.getValue()*getLossPerFailure(am.getName()); 
						expectedValue /= expectedSum;
						if((new Double(expectedValue).equals(Double.NaN))) {
							continue;
						}
						if(expectedValue<0 || expectedValue>1) {
							continue;
						}
						double res = am.getSignal().getValue()*expectedValue;
						if(res!=0) {
							return res;
						}
					}
				}
			}
		}
		*/
		}
		return 0;
	}

	private double getLossPerFailure(String name) {
		for(ProfitRatio pr:profitRatios) {
			if(pr.getMethod().equals(name)) {
				return pr.getFailLoss();
			}
		}
		return 0;
	}

	private double getProfitPerSuccess(String name) {
		// TODO Auto-generated method stub
		for(ProfitRatio pr:profitRatios) {
			if(pr.getMethod().equals(name)) {
				return pr.getSuccessProfit();
			}
		}
		return 0;
	}
	
	public void printEValues() {
		for(Entry<String,Double> entry:safetyQueue) {
					double expectedValue = entry.getValue()*getProfitPerSuccess(entry.getKey())-
											entry.getValue()*getLossPerFailure(entry.getKey()); 
					System.out.println(entry.getKey()+": "+expectedValue);
		}
	}

	private ArrayList<AnalysisMethod> getMethods(ArrayList<DailyData> data2) {
		PriorityQueue<DailyData> dataQueue = new PriorityQueue<DailyData>(data2);
		ArrayList<AnalysisMethod> analysisMethods = new ArrayList<AnalysisMethod>();
		analysisMethods.add(new BollingerBands(dataQueue, 18));
		//analysisMethods.add(new Fibonacci(dataQueue, 23));
		//analysisMethods.add(new MACD(dataQueue, 12, 26, 9));
		
		//analysisMethods.add(new RateOfChange(dataQueue, 7));
		
		analysisMethods.add(new RelativeStrengthIndex(dataQueue, 10));
		
		analysisMethods.add(new VBROC(dataQueue, 18,6));
		analysisMethods.add(new VolatilityBands(dataQueue, 18));
		
		//analysisMethods.add(new CMFEMA(dataQueue, 1,28));
		analysisMethods.add(new BBRSI(dataQueue, 19,10));
		//analysisMethods.add(new BBStochRSI1(dataQueue, 18,8));
		analysisMethods.add(new BBStochRSI2(dataQueue, 18,10));
		analysisMethods.add(new RSISOCMFROC(dataQueue));
		analysisMethods.add(new MACDHistogramRSI2(dataQueue, 8, 17, 3, 7));
		
		//analysisMethods.add(new MACDCMF1(dataQueue, 12, 26, 9,10));
		//analysisMethods.add(new MACDCMF3(dataQueue, 12, 26, 9,10));
		//analysisMethods.add(new MACDCMF4(dataQueue, 12, 26, 9,10));
		//analysisMethods.add(new MACDDerivativeSO(dataQueue, 12, 26, 9,5,9,14,3)); dålig
		//analysisMethods.add(new MACDROC(dataQueue, 12, 26, 9,7));
		//analysisMethods.add(new MACDROC2(dataQueue, 12, 26, 9,7));
		//analysisMethods.add(new MACDROC3(dataQueue, 12, 26, 9,7));
		//analysisMethods.add(new MACDHistogramRSI(dataQueue, 8, 17, 3, 7));
		
		//analysisMethods.add(new MACDHistogramRSI3(dataQueue, 12, 26, 9,7));
		//analysisMethods.add(new MACDRSI(dataQueue, 8, 17, 3, 10)); köper och säljer aldrig
		//analysisMethods.add(new MACDRSI2(dataQueue, 8, 17, 3, 10));
		//analysisMethods.add(new MACDRSI3(dataQueue,8, 17, 3, 10));
		//analysisMethods.add(new MACDRSI4(dataQueue, 8, 17, 3, 10));
		//analysisMethods.add(new MACDValueSO2(dataQueue, 12, 26, 9,5,9,14,3));
		//analysisMethods.add(new StochRSI(dataQueue, 6));
		//analysisMethods.add(new SORSIROC(dataQueue));
		return analysisMethods;
	}

	public static void main(String[] args) {
		ArrayList<Calendar> dates = new ArrayList<Calendar>(DatabaseHandler.getDates());
		TestBot3 bot = new TestBot3(45, 338);
		//TestBot3 bot = new TestBot3(383, 200);
		bot.run();
		bot.printEValues();
		//System.out.println(bot.user.getTotalAssets());
		//System.out.println(bot.index.getTotalAssets());
	}
	
	
}