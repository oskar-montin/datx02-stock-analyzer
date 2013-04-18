package analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.PriorityQueue;

import util.Util;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import data.SimpleData;
import data.Stock;

/**
 * A class that represents an exponential moving average.
 * 
 * @author oskarnylen
 */
public class ExponentialMovingAverage implements AnalysisMethod {

	private PriorityQueue<SimpleData> movingAverageQueue;
	private PriorityQueue<SimpleData> priceQueue;
	private int offset;

	/**
	 * 
	 * @param stock
	 * @param offset
	 */
	public ExponentialMovingAverage(PriorityQueue<? extends SimpleData> dataQueue, int offset){
		this.offset = offset;
		priceQueue = new PriorityQueue<SimpleData>(dataQueue);
		movingAverageQueue = getEMA(dataQueue, offset);
	}

	/**
	 * 
	 * @param todaysPrice
	 * @param numberOfDays
	 * @param EMAYesterday
	 * @return
	 */
	private static double calculateEMA(double todaysPrice, double numberOfDays, double EMAYesterday){
		double k = 2 / (numberOfDays + 1);
		return ((todaysPrice*k) + (EMAYesterday*(1-k)));
	}

	/**
	 * 
	 * @param dataQueue
	 * @param offset
	 * @return
	 */
	private static PriorityQueue<SimpleData> getEMA(PriorityQueue<? extends SimpleData> dataQueue, int offset){
		PriorityQueue<SimpleData> priceQueue = new PriorityQueue<SimpleData>(dataQueue);
		
		PriorityQueue<SimpleData> resultQueue = new PriorityQueue<SimpleData>();
		
		LinkedList<SimpleData> dataList = new LinkedList<SimpleData>(dataQueue);
		

		/*
		 * Make sure that the dailyDataList has the same number of entries (with the same dates) as movingAverageList
		 */
		for(int i = offset-1; i > 0; i--){
			dataList.removeFirst();
		}
		
		/*
		 * Make sure that the first EMA is the same as the SMA for the same period
		 */
		double yesterdayEMA = SimpleMovingAverage.getSMA(dataQueue, offset, offset).getValue();

		
		LinkedList<Double> priceList = new LinkedList<Double>();
		
		for(int i = 0; i < dataList.size(); i++){
			if(i == 0){
				priceList.add(yesterdayEMA);
			} else {
				
				SimpleData sd = dataList.get(i);
				//call the EMA calculation
				double EMA = calculateEMA(sd.getValue(), offset, yesterdayEMA);
				//put the calculated EMA in a list
				priceList.add(EMA);
				//make sure yesterdayEMA gets filled with the EMA we used this time around
				yesterdayEMA = EMA;
			}
		}
		
		/*
		 * Insert the calculated values into the movingAverageList
		 */
		for(int i = 0; i < priceList.size(); i++){
			resultQueue.add(new SimpleData(dataList.get(i).getStock(), dataList.get(i).getDate(),
					priceList.get(i)));
		}
		return resultQueue;
	}
	
	/**
	 * 
	 * @return
	 */
	public PriorityQueue<SimpleData> getMovingAverage(){
		return movingAverageQueue;
	}

	/**
	 * 
	 * @param data the set of dayly data that will be used
	 * @param index the index of the day from wich the sma should be calculated
	 * @param offset the offest over how long period the avarage should be calculated
	 * @return the simple moving avarage for one specific date at the specified index of the collection
	 */
	public static SimpleData getEMA(SimpleData[] data, int index, int offset) {
		if(index<offset) {
			throw new IllegalArgumentException("Offset larger than index");
		}
		if(index>=data.length) {
			throw new IndexOutOfBoundsException("Index larger than the size of the collection");
		}
		
		PriorityQueue<SimpleData> queue = new PriorityQueue<SimpleData>(Arrays.asList(data));
		ArrayList<SimpleData> emaList = new ArrayList<SimpleData>(getEMA(queue, offset));
		
		return new SimpleData(data[index].getStock(), data[index].getDate(), emaList.get(index-offset).getValue());
	}
	
	/**
	 * 
	 * @param data the set of dayly data that will be used
	 * @param index the index of the day from wich the sma should be calculated
	 * @param offset the offest over how long period the avarage should be calculated
	 * @return the simple moving avarage for one specific date at the specified index of the collection
	 */
	public static SimpleData getEMA(Collection<? extends SimpleData> data, int index, int offset) {
		SimpleData[] temp = new SimpleData[data.size()];
		temp = data.toArray(temp);
		return getEMA(temp,index,offset);
	}
	

	
	/**
	 * 
	 * @param data the set of dayly data that will be used
	 * @param date the date from wich the avarage should be calculated
	 * @param offset the offest over how long period the avarage should be calculated
	 * @return the simple moving avarage for one specific date at the specified index of the collection
	 */
	public static SimpleData getEMA(PriorityQueue<? extends SimpleData> data, Calendar date, int offset) {
		SimpleData[] temp = new SimpleData[data.size()];
		temp = data.toArray(temp);
		int index = Arrays.binarySearch(temp, new SimpleData(null, date, 0), SimpleData.getDateComperator());
		return getEMA(temp,index,offset);
	}
	
	
	@Override
	public String resultString() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the k-value of the last two data points in the moving average.
	 */
	@Override
	public double value() {
		LinkedList<SimpleData> tempList = new LinkedList<SimpleData>(this.getMovingAverage());
		double firstValue = tempList.pollLast().getValue();
		double secondValue = tempList.pollLast().getValue();
		
		double value = secondValue-firstValue;
		return value;
	}

	@Override
	public Curve[] getGraph() {
		PriorityQueue<SimpleData> trimmedQueue = Util.trimQueue(this.getMovingAverage(), this.offset);
		
		Curve[] curves = new Curve[2];
		curves[0] = new Curve(this.priceQueue, "Index");
		curves[1] = new Curve(trimmedQueue, "EMA");
		return curves;
	}
	
	
	@Override
	public Result getResult() {
		Double value = this.value();
		Signal signal = Signal.NONE;
		
		return new Result("EMA", value, this.resultString(), this.getGraph(), signal);
	}
}
