package analyzer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

import data.DailyData;
import data.SimpleData;
import data.Stock;

/**
 * A class that represents a simple moving average.
 * 
 * @author oskarnylen
 */
public class SimpleMovingAverage {

	private LinkedList<DailyData> dailyDataList;
	
	private LinkedList<SimpleData> movingAverageList;
	
	/**
	 * 
	 * @param stock
	 * @param offset - The number of days the SMA should be set to.
	 */
	public SimpleMovingAverage(Stock stock, int offset){
		if(offset < 1 || offset > DatabaseHandler.getDailyData(stock).size()){
			throw new IllegalArgumentException();
		}
		
		dailyDataList = new LinkedList<DailyData>(DatabaseHandler.getDailyData(stock));

		Collections.reverse(dailyDataList);
		
		movingAverageList = new LinkedList<SimpleData>();
		
		/*
		 * For every entry in the dailyDataList - (the offset-1)
		 * The offset-1 means that we we won't check more than we're able to. For example
		 * if I only have 5 entries, and I want to do the SMA 3, I will only do 5-(3-1), that is 5-2 = 3 SMA entries
		 * since there are not enough original entries for more.
		 */
		for(int i = 0; i < dailyDataList.size()-(offset-1); i++){
			double total = 0;
			movingAverageList.add(new SimpleData(dailyDataList.get(i)));
			for(int j = 0; j < offset; j++){
				total = total + dailyDataList.get(i+j).getClosePrice();
			}
			movingAverageList.get(i).setClosePrice(total/offset);
		}
	}
	
	public LinkedList<SimpleData> getMovingAverage(){
		return movingAverageList;
	}
}
