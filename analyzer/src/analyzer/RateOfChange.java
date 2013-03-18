package analyzer;

import java.util.Arrays;
import java.util.Calendar;
import java.util.PriorityQueue;

import data.DailyData;

public class RateOfChange {
	
	public static Double getROC(PriorityQueue<DailyData> dailyData, Calendar t, int period) {
		return getROC(dailyData,new DailyData(null, t, null, 0, 0, 0, 0, 0, 0, 0, 0, 0),period);
	}
	
	public static Double getROC(PriorityQueue<DailyData> dailyData, DailyData t, int period) {
		DailyData[] temp = new DailyData[dailyData.size()];
		temp = dailyData.toArray(temp);
		int tIndex = Arrays.binarySearch(temp,t,DailyData.getDateComperator());
		if(tIndex<period || tIndex<0) {
			return null;
		}
		return 100*(temp[tIndex].getClosePrice()-temp[tIndex-period].getClosePrice())/temp[tIndex-period].getClosePrice();
	}
	
	
}
