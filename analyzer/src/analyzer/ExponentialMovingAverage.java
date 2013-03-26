package analyzer;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

import data.DailyData;
import data.SimpleData;
import data.Stock;

/**
 * A class that represents an exponential moving average.
 * 
 * 
 * 
 * @author oskarnylen
 */
public class ExponentialMovingAverage {

	private LinkedList<SimpleData> dailyDataList;

	private LinkedList<SimpleData> simpleMovingAverageList;
	
	private LinkedList<SimpleData> movingAverageList;

	/**
	 * 
	 * @param stock
	 * @param offset
	 */
	public ExponentialMovingAverage(PriorityQueue<? extends SimpleData> MACDQueue, int offset){
		if(offset < 1 || offset > MACDQueue.size()){
			throw new IllegalArgumentException();
		}
		
		dailyDataList = new LinkedList<SimpleData>(MACDQueue);
		Collections.reverse(dailyDataList);

		simpleMovingAverageList = new SimpleMovingAverage(MACDQueue, offset).getMovingAverage();
		
		movingAverageList = new LinkedList<SimpleData>();

		
		/*
		 * Make sure that the dailyDataList has the same number of entries (with the same dates) as movingAverageList
		 */
		for(int i = offset-1; i > 0; i--){
			dailyDataList.removeLast().getValue();
		}
		
		Collections.reverse(simpleMovingAverageList);

		
		/*
		 * Make sure that the first EMA is the same as the SMA for the same period
		 */
		double yesterdayEMA = simpleMovingAverageList.getFirst().getValue();


		LinkedList<Double> priceList = new LinkedList<Double>();

		Collections.reverse(dailyDataList);

		for(int i = 0; i < dailyDataList.size(); i++){
			if(i == 0){
				priceList.add(yesterdayEMA);
			} else {
				
				SimpleData sd = dailyDataList.get(i);
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
			movingAverageList.add(new SimpleData(dailyDataList.get(i).getStock(), dailyDataList.get(i).getDate(),
					priceList.get(i)));
		}
	}

	private double calculateEMA(double todaysPrice, double numberOfDays, double EMAYesterday){
		double k = 2 / (numberOfDays + 1);
		return ((todaysPrice*k) + (EMAYesterday*(1-k)));
	}

	public LinkedList<SimpleData> getMovingAverage(){
		return movingAverageList;
	}
}