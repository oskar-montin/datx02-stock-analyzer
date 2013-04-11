package analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import data.SimpleData;

/**
 * Class that contains two public methods that are static. These are used to get Moving Averages.
 * 
 * @author oskarnylen
 */
public class MovingAverage {
	
	/**
	 * A static method to generate and return a Simple Moving Average
	 * 
	 * @param dataQueue - A collection of any type that is a class or subclass to SimpleData
	 * @param offset - an int that represents the offset the Moving Average should represent
	 * @return A PriorityQueue containing SimpleData that represents a Simple Moving Average
	 */
	public static List<SimpleData> getSMA(Collection<? extends SimpleData> dataQueue, int offset){
		ArrayList<SimpleData> dataList = new ArrayList<SimpleData>(dataQueue);
		ArrayList<SimpleData> movingAverageList;
		Collections.reverse(dataList);
		
		PriorityQueue<SimpleData> movingAverageQueue = calculateSMA(dataList, offset);
		movingAverageList = new ArrayList<SimpleData>();
		
		
		for(int i = movingAverageQueue.size()-1; i >= 0; i--){
			movingAverageList.add(movingAverageQueue.poll());
		}

		
		
		return movingAverageList;
	}
	
	private static PriorityQueue<SimpleData> calculateSMA(ArrayList<SimpleData> dataList, int offset){
		ArrayList<SimpleData> movingAverageList = new ArrayList<SimpleData>();
		PriorityQueue<SimpleData> movingAverageQueue = new PriorityQueue<SimpleData>();
		
		for(int i = 0; i < dataList.size()-(offset-1); i++){
			double total = 0;
			for(int j = 0; j < offset; j++){
				total = total + dataList.get(i+j).getValue();
			}
			movingAverageQueue.add(new SimpleData(dataList.get(i).getStock(), 
								  dataList.get(i).getDate(), total/offset));
		}
		
		return movingAverageQueue;
	}
	
	/**
	 * A static method to generate and return an Exponential Moving Average
	 * 
	 * @param dataQueue - A collection of any type that is a class or subclass to SimpleData
	 * @param offset - an int that represents the offset the Moving Average should represent
	 * @return A PriorityQueue containing SimpleData that represents a Exponential Moving Average
	 */
	public static PriorityQueue<SimpleData> getEMA(Collection<? extends SimpleData> dataQueue, int offset){
		LinkedList<SimpleData> dataList = new LinkedList<SimpleData>(dataQueue);
		PriorityQueue<SimpleData> movingAverageQueue = new PriorityQueue<SimpleData>();

		/*
		 * Make sure that the dailyDataList has the same number of entries (with the same dates) as movingAverageList
		 */
		for(int i = offset-1; i > 0; i--){
			dataList.removeFirst();
		}
		
		/*
		 * Make sure that the first EMA is the same as the SMA for the same period
		 */
		double yesterdayEMA = getSMA(dataQueue, offset).get(0).getValue();
		
		
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
			movingAverageQueue.add(new SimpleData(dataList.get(i).getStock(), dataList.get(i).getDate(),
					priceList.get(i)));
		}
		
		return movingAverageQueue;
	}
	
	
	private static double calculateEMA(double todaysPrice, double numberOfDays, double EMAYesterday){
		double k = 2 / (numberOfDays + 1);
		return ((todaysPrice*k) + (EMAYesterday*(1-k)));
	}
}
