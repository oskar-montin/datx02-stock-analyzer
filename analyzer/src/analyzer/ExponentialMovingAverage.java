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
		if(offset < 1 || offset > DatabaseHandler.getDailyData(stock).size()){
			throw new IllegalArgumentException();
		}
		
		dailyDataList = new LinkedList<DailyData>(DatabaseHandler.getDailyData(stock));
		Collections.reverse(dailyDataList);

		movingAverageList = new SimpleMovingAverage(stock, offset).getMovingAverage();

		
		/*
		 * Make sure that the dailyDataList has the same number of entries (with the same dates) as movingAverageList
		 */
		for(int i = offset-1; i > 0; i--){
			dailyDataList.removeLast().getClosePrice();
		}

		
		
		Collections.reverse(movingAverageList);

		
		/*
		 * Make sure that the first EMA is the same as the SMA for the same period
		 */
		double yesterdayEMA = movingAverageList.getFirst().getClosePrice();


		LinkedList<Double> priceList = new LinkedList<Double>();

		Collections.reverse(dailyDataList);

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
		movingAverageList.getLast().setClosePrice(priceList.getLast());
	}

	private double CalculateEMA(double todaysPrice, double numberOfDays, double EMAYesterday){
		double k = 2 / (numberOfDays + 1);
		return (todaysPrice*k) + (EMAYesterday*(1-k));
	}

	public LinkedList<SimpleData> getMovingAverage(){
		return movingAverageList;
	}
}