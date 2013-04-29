package data;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AnalyticsData {
	private final Stock stock;
	private final String analysisMethod;
	private ArrayList<BuySellPair> pairs;
	private double successRate;
	private int timesSucceeded = 0;
	private int timesFailed = 0;
	private double totalProfit = 0;
	private final int amountStillOwned;
	
	public AnalyticsData(PriorityQueue<SimpleData> bought, PriorityQueue<SimpleData> sold, String analysisMethod, Stock stock) {
		this.stock = stock;
		this.analysisMethod = analysisMethod;
		pairs = new ArrayList<BuySellPair>();
		PriorityQueue<SimpleData> buyQueue = new PriorityQueue<SimpleData>(bought);
		for(SimpleData sell:sold) {
			SimpleData buy = buyQueue.poll();
			BuySellPair pair = new BuySellPair(buy,sell);
			pairs.add(pair);
			if(pair.succeeded()) {
				timesSucceeded++;
			} else {
				timesFailed++;
			}
			totalProfit+=pair.profit();
		}
		this.amountStillOwned = buyQueue.size();
		if((this.timesFailed+this.timesSucceeded)!=0) {
			this.successRate = (double)this.timesSucceeded/(this.timesFailed+this.timesSucceeded);
		} else {
			this.successRate = 1.0;
		}
		
	}

	public Stock getStock() {
		return stock;
	}

	public String getAnalysisMethod() {
		return analysisMethod;
	}

	public double getSuccessRate() {
		return successRate;
	}
	
	public ArrayList<BuySellPair> getPairs() {
		return pairs;
	}

	public int getTimesSucceeded() {
		return timesSucceeded;
	}

	public int getTimesFailed() {
		return timesFailed;
	}

	public double getTotalProfit() {
		return totalProfit;
	}
	
	public int amountOfPairs() {
		return pairs.size();
	}

	public int getAmountStillOwned() {
		return amountStillOwned;
	}
}
