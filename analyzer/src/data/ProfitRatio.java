package data;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfitRatio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4699535717534062508L;
	private double profitPerTransaction;
	private double profit;
	private double successProfit;
	private double failLoss;
	private final String method;
	
	public ProfitRatio(String method, ArrayList<BuySellPair> pairs) {
		this.method = method;
		int successAmount = 0;
		int failAmount = 0;
		double successSum = 0;
		double failSum = 0;
		this.profit = 0;
		
		for(BuySellPair pair:pairs) {
			double p=pair.profit();
			if(p>0) {
				successAmount++;
				successSum += pair.profit();
			} else {
				failAmount++;
				failSum += pair.profit();
			}
			this.profit+=p;
		}
		if(successAmount==0) {
			this.successProfit = 0;
		} else {
			this.successProfit = successSum/successAmount;
		}
		if(failAmount==0) {
			this.failLoss = 0;
		} else {
			this.failLoss = failSum/failAmount;
		}
		if(successAmount!=0 || failAmount!=0){
			this.profitPerTransaction = this.profit/(successAmount+failAmount);
		} else {
			this.profitPerTransaction = 0;
		}
		
		
		
	}

	public String getMethod() {
		return method;
	}

	public double getProfitPerTransaction() {
		return profitPerTransaction;
	}

	public double getSuccessProfit() {
		return successProfit;
	}

	public double getFailLoss() {
		return failLoss;
	}
}
