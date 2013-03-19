package analyzer;

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

	private LinkedList<DailyData> dailyDataList;

	private LinkedList<SimpleData> movingAverageList;

	/**
	 * 
	 * @param stock
	 * @param offset
	 */
	public ExponentialMovingAverage(Stock stock, int offset){

		dailyDataList = new LinkedList<DailyData>(DatabaseHandler.getDailyData(stock));
		Collections.reverse(dailyDataList);

		movingAverageList = new SimpleMovingAverage(stock, offset).getMovingAverage();
		Collections.reverse(movingAverageList);

		/*
		 * Make sure that the dailyDataList has the same number of entries (with the same dates) as movingAverageList
		 */
		for(int i = movingAverageList.size(); i < dailyDataList.size(); i++){
			dailyDataList.removeLast();
		}

		Collections.reverse(movingAverageList);
		Collections.reverse(dailyDataList);

		/*
		 * Make sure that the first EMA is the same as the SMA for the same period
		 */
		double yesterdayEMA = movingAverageList.getFirst().getClosePrice();


		LinkedList<Double> priceList = new LinkedList<Double>();


		for(int i = 0; !dailyDataList.isEmpty(); i++){
			if(i == 0){
				priceList.add(yesterdayEMA);
				dailyDataList.poll();
			} else {
				DailyData sd = dailyDataList.poll();
				//call the EMA calculation
				double EMA = CalculateEMA(sd.getClosePrice(), offset, yesterdayEMA);
				//put the calculated EMA in a list
				priceList.add(EMA);
				//make sure yesterdayEMA gets filled with the EMA we used this time around
				yesterdayEMA = EMA;
			}
		}

		/*
		 * Insert the calculated values into the movingAverageList
		 */
		for(int i = 0; i < priceList.size()-1; i++){
			movingAverageList.get(i).setClosePrice(priceList.get(i));
		}
	}


	public double CalculateEMA(double todaysPrice, double numberOfDays, double EMAYesterday){
		double k = 2 / (numberOfDays + 1);
		return (todaysPrice * k) + (EMAYesterday * (1-k));
	}

	public LinkedList<SimpleData> getMovingAverage(){
		return movingAverageList;
	}
}