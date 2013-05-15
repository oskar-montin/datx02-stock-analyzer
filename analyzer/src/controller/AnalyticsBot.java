package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;

import combinations.BBRSI;
import combinations.BBStochRSI1;
import combinations.BBStochRSI2;
import combinations.CMFEMA;
import combinations.MACDCMF1;
import combinations.MACDCMF3;
import combinations.MACDCMF4;
import combinations.MACDDerivativeSO;
import combinations.MACDFib1;
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
import combinations.MACDRSI5;
import combinations.MACDValueSO2;
import combinations.RSISOCMFROC;
import combinations.SORSIROC;
import combinations.VBROC;

import analyzer.AnalysisMethod;
import analyzer.BollingerBands;
import analyzer.CMF;
import analyzer.ExponentialMovingAverage;
import analyzer.Fibonacci;
import analyzer.FundamentalAnalysis;
import analyzer.MACD;
import analyzer.RateOfChange;
import analyzer.RelativeStrengthIndex;
import analyzer.SimpleMovingAverage;
import analyzer.StochRSI;
import analyzer.StochasticOscillator;
import analyzer.TrendLine;
import analyzer.VolatilityBands;

import data.AnalyticsData;
import data.DailyData;
import data.FundamentalData;
import data.QuarterlyData;
import data.Signal;
import data.SimpleData;
import data.Stock;

public class AnalyticsBot {
	private int numberOfStocks;
	private PriorityQueue<SimpleData>[][] boughtStocks;
	private PriorityQueue<SimpleData>[][] soldStocks;
	private PriorityQueue<SimpleData>[][] ownedStocks;
	private AnalyticsData[][] result;
	private Stock[] stocks;
	private ArrayList<AnalysisMethod> analysisMethods;
	
	public AnalyticsBot(Collection<Stock> inputStocks) {
		numberOfStocks = inputStocks.size();
		stocks = new Stock[numberOfStocks];
		int i = 0;
		for(Stock s: inputStocks) {
			stocks[i] = s;
			i++;
		}
	}
	
	private void initArrays(int nrOfMethods) {
		boughtStocks = ((PriorityQueue<SimpleData>[][])new PriorityQueue[nrOfMethods][numberOfStocks]);
		soldStocks = ((PriorityQueue<SimpleData>[][])new PriorityQueue[nrOfMethods][numberOfStocks]);
		ownedStocks = ((PriorityQueue<SimpleData>[][])new PriorityQueue[nrOfMethods][numberOfStocks]);
		for(int n = 0; n<nrOfMethods;n++) {
			for(int m = 0; m<numberOfStocks;m++) {
				boughtStocks[n][m] = new PriorityQueue<SimpleData>();
				soldStocks[n][m] = new PriorityQueue<SimpleData>();
				ownedStocks[n][m] = new PriorityQueue<SimpleData>();
			}
		}
		result = new AnalyticsData[nrOfMethods][numberOfStocks];
		System.out.println("AnalyticsBot initiated");
	}
	
	public void feed(LinkedList<DailyData>[] data) {
		
		for(int i = 0; i< data.length; i++) {
			if(data[i] == null || data[i].size()<45) {
				continue;
			} else {
				this.analysisMethods = getMethods(data[i]);
			}
			if(boughtStocks == null) {
				initArrays(analysisMethods.size());
			}
			for(int j = 0; j<analysisMethods.size();j++) {
				if(analysisMethods.get(j).getSignal() == Signal.BUY) {
					boughtStocks[j][i].add(data[i].getLast());
					ownedStocks[j][i].add(data[i].getLast());
				} 
				if(analysisMethods.get(j).getSignal() == Signal.SELL) {
					if(!ownedStocks[j][i].isEmpty()) {
						for(SimpleData sd : ownedStocks[j][i]) {
							soldStocks[j][i].add(data[i].getLast());
						}
						ownedStocks[j][i].clear();
					}
				}
			}
			//System.out.println("Stock "+stocks[i].getSymbol()+" feeded");
		}
	}
	
	private ArrayList<AnalysisMethod> getMethods(LinkedList<DailyData> dataList) {
		PriorityQueue<DailyData> data = new PriorityQueue<DailyData>(dataList);
		ArrayList<AnalysisMethod> analysisMethods = new ArrayList<AnalysisMethod>();
		
		analysisMethods.add(new BollingerBands(data, 18));
		analysisMethods.add(new Fibonacci(data, 23));
		analysisMethods.add(new MACD(data, 12, 26, 9));
		
		analysisMethods.add(new RateOfChange(data, 7));
		
		analysisMethods.add(new RelativeStrengthIndex(data, 10));
		analysisMethods.add(new VBROC(data, 18,6));
		analysisMethods.add(new VolatilityBands(data, 18));
		analysisMethods.add(new StochRSI(data, 6));
		analysisMethods.add(new CMFEMA(data, 1,28));
		analysisMethods.add(new BBRSI(data, 19,10));
		analysisMethods.add(new BBStochRSI1(data, 18,8));
		analysisMethods.add(new BBStochRSI2(data, 18,10));
		analysisMethods.add(new MACDCMF1(data, 12, 26, 9,10));
		analysisMethods.add(new MACDCMF3(data, 12, 26, 9,10));
		analysisMethods.add(new MACDCMF4(data, 12, 26, 9,10));
		analysisMethods.add(new MACDDerivativeSO(data, 12, 26, 9,5,9,14,3));
		analysisMethods.add(new MACDROC(data, 12, 26, 9,7));
		analysisMethods.add(new MACDROC2(data, 12, 26, 9,7));
		analysisMethods.add(new MACDROC3(data, 12, 26, 9,7));
		analysisMethods.add(new MACDHistogramRSI(data, 8, 17, 3, 7));
		analysisMethods.add(new MACDHistogramRSI2(data, 8, 17, 3, 7));
		analysisMethods.add(new MACDHistogramRSI3(data, 12, 26, 9,7));
		analysisMethods.add(new MACDRSI(data, 8, 17, 3, 10));
		analysisMethods.add(new MACDRSI2(data, 8, 17, 3, 10));
		analysisMethods.add(new MACDRSI3(data,8, 17, 3, 10));
		analysisMethods.add(new MACDRSI4(data, 8, 17, 3, 10));
		analysisMethods.add(new MACDValueSO2(data, 12, 26, 9,5,9,14,3));
		analysisMethods.add(new RSISOCMFROC(data));
		analysisMethods.add(new SORSIROC(data));
		
		return analysisMethods;
	}
	public AnalyticsData[][] evaluate() {
		for(int methodNr = 0; methodNr<result.length;methodNr++) {
			
			for(int stockNr = 0; stockNr<numberOfStocks; stockNr++) {
				result[methodNr][stockNr] = new AnalyticsData(boughtStocks[methodNr][stockNr], 
															soldStocks[methodNr][stockNr], 
															this.analysisMethods.get(methodNr).getName(), 
															stocks[stockNr]);
			}
			
		}
		return result;
		
	}
}
