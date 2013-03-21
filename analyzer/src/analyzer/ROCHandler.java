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
	
	/**
	 * 
	 * @param boundPercent a double larger than 0 and lesser or equal to 100 representing in % how large share 
	 * of all the roc-values that must be within the returned scope, example: a boundPercent of 100 
	 * will give the max scope while a boundPercent of 50 will return a scope that holds for half of the roc's.
	 * @param equilibrium if true the return scope will take the form 
	 * {-Max(Abs(minValue),Abs(maxValue)),Max(Abs(minValue),Abs(maxValue))}, if false: 
	 * {minValue,maxValue)
	 * 
	 * @return the scope that holds for boundPercent % of all rates of changes for this roc handler
	 */
	public Double[] getScope(double boundPercent, boolean equilibrium) {
		if(boundPercent<=0 || boundPercent>100) {
			throw new IllegalArgumentException("rate must be 0<boundPercent<=100");
		}
		Double[] scope;
		Double maxRate;
		int i = ((int) (this.rocList.length*(boundPercent/100))-1);
		i = i<0 ? 0:i; //We don't like index out of bounds :)
		Arrays.sort(this.rocList,RateOfChange.getRateComparator(true));
		
		if(equilibrium) {
			maxRate = this.rocList[i].getRate();
			scope = new Double[]{-Math.abs(maxRate),Math.abs(maxRate)};
		} else { // n < (2*n log n)
			Double max = Double.NEGATIVE_INFINITY;
			Double min = Double.POSITIVE_INFINITY;
			for(int k = 0, nr = 0; k<this.rocList.length && nr<=i; k++ ) {
				if(this.rocList[k].getRate() > max) {
					if(nr!=(i-1) || min>=0) {
						max = this.rocList[k].getRate();
						if(this.rocList[k].getRate()>=0) {
							nr++;
						}
					}
				}
				if(this.rocList[k].getRate() < min) {
					if(nr!=(i-1) || max>=0) {
						min = this.rocList[k].getRate();
						if(this.rocList[k].getRate()<0) {
							nr++;
						}
					}
				}
			}
			
			 
			scope = new Double[]{-Math.abs(min),Math.abs(max)};
		}
		return scope;
	}
	
	/**
	 * @param equilibrium if true the return scope will take the form 
	 * {-Max(Abs(minValue),Abs(maxValue)),Max(Abs(minValue),Abs(maxValue))}, if false: 
	 * {minValue,maxValue)
	 * 
	 * @return the max scope of the this rate of change object
	 */
	public Double[] getMaxScope(boolean equilibrium) {
		if(equilibrium) {
			if(this.maxScope==null) {
				this.maxScope=getScope(100,equilibrium);
			}
			return this.maxScope;
		} else {
			return getScope(100, equilibrium);
		}
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
