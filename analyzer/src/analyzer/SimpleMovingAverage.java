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

	private LinkedList<SimpleData> dailyDataList;
	private LinkedList<SimpleData> movingAverageList;
	private int offset;
	
	/**
	 * 
	 * @param stock
	 * @param offset - The number of days the SMA should be set to.
	 */
	public SimpleMovingAverage(PriorityQueue<? extends SimpleData> dailyData, int offset){
		if(offset < 1 || offset > dailyData.size()){
			throw new IllegalArgumentException();
		}
		
		this.offset = offset;
		dailyDataList = new LinkedList<SimpleData>(dailyData);

		Collections.reverse(dailyDataList);
		
		
		
		/*
		 * For every entry in the dailyDataList - (the offset-1)
		 * The offset-1 means that we we won't check more than we're able to. For example
		 * if I only have 5 entries, and I want to do the SMA 3, I will only do 5-(3-1), that is 5-2 = 3 SMA entries
		 * since there are not enough original entries for more.
		 */
		
		calculateSMA();

	}
	
	public LinkedList<SimpleData> getMovingAverage(){
		Collections.reverse(movingAverageList);
		return movingAverageList;
	}
	
	public void calculateSMA(){
		movingAverageList = new LinkedList<SimpleData>();
		
		for(int i = 0; i < dailyDataList.size()-(offset-1); i++){
			double total = 0;
			for(int j = 0; j < offset; j++){
				total = total + dailyDataList.get(i+j).getValue();
			}
			movingAverageList.add(new SimpleData(dailyDataList.get(i).getStock(), 
								  dailyDataList.get(i).getDate(), total/offset));
		}
	}
}
