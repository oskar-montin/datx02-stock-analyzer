package analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.PriorityQueue;

import data.DailyData;
import data.RateOfChange;

public class ROCHandler {
	
	private DailyData[] dailyData;
	private RateOfChange[] rocList;
	private Double[] maxScope;
	private Double[] midScope = new Double[2];
	private Double[] normalScope = new Double[2];
	private int period;
	
	public ROCHandler(PriorityQueue<DailyData> dailyData, int period) {
		if(dailyData == null) {
			throw new NullPointerException("dailyData == null");
		}
		if(period>=dailyData.size()) {
			throw new IllegalArgumentException("Period > size of data");
		} 
		
		this.dailyData = new DailyData[dailyData.size()];
		this.dailyData = dailyData.toArray(this.dailyData);
		rocList = new RateOfChange[dailyData.size()-period];
		this.period = period;
		createRocArray();
		
	}
	
	private void calculateMaxScope() {
		this.maxScope = new Double[]{new Double(0),new Double(0)};
		if(this.rocList.length<=0)
			return;
		Double max = Double.NEGATIVE_INFINITY;
		Double min = Double.POSITIVE_INFINITY;
		Double temp = 0.0;
		for(RateOfChange roc:rocList) {
			//Om v vill avrunda (ceil)
			max = Math.ceil(Math.max(max, roc.getRate()));
			min = (double) Math.round(Math.min(min, roc.getRate()));
			//Om vi vill ha jämnvikt i omfånget ex: [-5,5], [-2,2], [-8,8]
			temp = Math.max(Math.abs(max), Math.abs(min));
			this.maxScope[0] = -temp;
			this.maxScope[1] = temp;
			//Annars om vi vill kunna ha ex [-1,4]: 
			//this.maxScope[0] = min; this.maxScope[1] = max;
		}
	}
	
	public Double[] getMaxScope() {
		if(this.maxScope == null) {
			calculateMaxScope();
		}
		return this.maxScope;
	}
	
	private void createRocArray() {
		for(int i = this.period, r = 0; i<this.dailyData.length;i++,r++) {
			this.rocList[r] = getROC(this.dailyData,i,this.period);
		}
	}
	
	public static RateOfChange getROC(PriorityQueue<DailyData> dailyData, Calendar t, int period) {
		return getROC(dailyData,new DailyData(null, t, null, 0, 0, 0, 0, 0, 0, 0, 0, 0),period);
	}
	
	public static RateOfChange getROC(PriorityQueue<DailyData> dailyData, DailyData t, int period) {
		DailyData[] temp = new DailyData[dailyData.size()];
		temp = dailyData.toArray(temp);
		int tIndex = Arrays.binarySearch(temp,t,DailyData.getDateComperator());
		return getROC(temp,tIndex,period);
	}	
	
	private static RateOfChange getROC(DailyData[] dailyData, int t, int n) {
		if(t<n || t<0) {
			return null;
		}
		Double rate = 100*(dailyData[t].getClosePrice()-dailyData[t-n].getClosePrice())/dailyData[t-n].getClosePrice();
		return new RateOfChange(dailyData[t].getStock(),dailyData[t].getDate(),rate);
	}
}
