package data;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AnalyticsData {
	private final Stock stock;
	private final String analysisMethod;
	private ArrayList<BuySellPair> pairs;
	private final double successRate;
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
		if(sold.size()!=0) {
			this.successRate = this.timesSucceeded/sold.size();
		} else {
			this.successRate = 1;
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

	public int getAmountStillOwned() {
		return amountStillOwned;
	}
}
