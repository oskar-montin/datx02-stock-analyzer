package analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.PriorityQueue;

import data.Curve;
import data.DailyData;
import data.SimpleData;
import data.Stock;

public class RateOfChange implements AnalysisMethod {
	
	private DailyData[] dailyData;
	private SimpleData[] rocList;
	private Double[] maxScope;
	private int period;
	private Stock stock;
	
	/**
	 * Creates a new ROCHandler that creates a curve with roc values that can be used to determine if thestock is under/overbought.
	 * @param dailyData a queue containing all data collected for the given stock
	 * @param offset the offset that should be used to create the roc list
	 */
	public RateOfChange(PriorityQueue<DailyData> dailyData, int offset) {
		if(dailyData == null) {
			throw new NullPointerException("dailyData == null");
		}
		if(period>=dailyData.size()) {
			throw new IllegalArgumentException("Period > size of data");
		} 
		this.period = offset;
		this.stock = dailyData.peek().getStock();
		this.dailyData = new DailyData[dailyData.size()];
		this.dailyData = dailyData.toArray(this.dailyData);
		rocList = new SimpleData[dailyData.size()-period];
		this.period = offset;
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
		SimpleData[] tempROC = Arrays.copyOf(this.rocList, this.rocList.length);
		int i = ((int) (this.rocList.length*(boundPercent/100))-1);
		i = i<0 ? 0:i; //We don't like index out of bounds :)
		Arrays.sort(tempROC,SimpleData.getValueComparator(true));
		
		if(equilibrium) {
			maxRate = tempROC[i].getValue();
			scope = new Double[]{-Math.abs(maxRate),Math.abs(maxRate)};
		} else { 
			Double max = Double.NEGATIVE_INFINITY;
			Double min = Double.POSITIVE_INFINITY;
			for(int k = 0, nr = 0; k<tempROC.length && nr<=i; k++ ) {
				if(tempROC[k].getValue() > max) {
					if(nr!=(i-1) || min>=0) {
						max = tempROC[k].getValue();
						if(tempROC[k].getValue()>=0) {
							nr++;
						}
					}
				}
				if(tempROC[k].getValue() < min) {
					if(nr!=(i-1) || max>=0) {
						min = tempROC[k].getValue();
						if(tempROC[k].getValue()<0) {
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
	
	public static SimpleData getROC(PriorityQueue<? extends SimpleData> dailyData, Calendar t, int period) {
		return getROC(dailyData,new SimpleData(null, t, 0),period);
	}
	
	public static SimpleData getROC(PriorityQueue<? extends SimpleData> dailyData, SimpleData t, int period) {
		SimpleData[] temp = new SimpleData[dailyData.size()];
		temp = dailyData.toArray(temp);
		int tIndex = Arrays.binarySearch(temp,t,SimpleData.getDateComperator());
		return getROC(temp,tIndex,period);
	}	
	
	private static SimpleData getROC(SimpleData[] dailyData, int t, int n) {
		if(t<n || t<0) {
			return null;
		}
		Double rate = 100*(dailyData[t].getValue()-dailyData[t-n].getValue())/dailyData[t-n].getValue();
		Calendar c = dailyData[t].getDate();
		System.out.println(""+c.get(Calendar.DATE));
		return new SimpleData(dailyData[t].getStock(),dailyData[t].getDate(),rate);
	}

	/**
	 * @return a value between 0 and 100 that indicates if the stock is overbought or not. A high value indicates an 
	 * overbought market. A value near 50% has stability
	 */
	@Override
	public double value() {
		Double[] maxScope = this.getMaxScope(false);
		double pMax = maxScope[1]+Math.abs(maxScope[0]);
		Double lastValue = this.rocList[this.rocList.length-1].getValue()+Math.abs(maxScope[0]);
		return ((lastValue*100)/pMax);
	}

	@Override
	public Curve[] getGraph() {
		Curve[] curves = new Curve[3];
		PriorityQueue<SimpleData> rocQueue = new PriorityQueue<SimpleData>();
		PriorityQueue<SimpleData> maxBoundQueue = new PriorityQueue<SimpleData>();
		PriorityQueue<SimpleData> minBoundQueue = new PriorityQueue<SimpleData>();
		Double[] maxScope = this.getMaxScope(true);
		for(SimpleData d:this.rocList) {
			rocQueue.add(d);
			maxBoundQueue.add(new SimpleData(d.getStock(), d.getDate(), maxScope[1]));
			minBoundQueue.add(new SimpleData(d.getStock(), d.getDate(), maxScope[0]));
		}
		
		curves[0] = new Curve(rocQueue,"Rate of change values in percent");
		curves[1] = new Curve(minBoundQueue,"The minimum bound");
		curves[2] = new Curve(maxBoundQueue,"THe maximum bound");
		
		return curves;
	}

	@Override
	public String resultString() {
		// TODO Auto-generated method stub
		return null;
	}
}
