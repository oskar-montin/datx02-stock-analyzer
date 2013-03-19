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
	private Double[] maxScope = new Double[2];
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
