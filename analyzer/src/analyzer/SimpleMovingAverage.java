package analyzer;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
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
		Collections.reverse(movingAverageList);
	}
	
	/**
	 * 
	 * @param data the set of dayly data that will be used
	 * @param index the index of the day from wich the sma should be calculated
	 * @param offset the offest over how long period the avarage should be calculated
	 * @return the simple moving avarage for one specific date at the specified index of the collection
	 */
	public static SimpleData getSMA(Collection<? extends SimpleData> data, int index, int offset) {
		SimpleData[] temp = new SimpleData[data.size()];
		temp = data.toArray(temp);
		return getSMA(temp,index,offset);
	}
	
	/**
	 * 
	 * @param data the set of dayly data that will be used
	 * @param index the index of the day from wich the sma should be calculated
	 * @param offset the offest over how long period the avarage should be calculated
	 * @return the simple moving avarage for one specific date at the specified index of the collection
	 */
	public static SimpleData getSMA(SimpleData[] data, int index, int offset) {
		if(index<offset) {
			throw new IllegalArgumentException("Offset larger than index");
		}
		if(index>=data.length) {
			throw new IndexOutOfBoundsException("Index larger than the size of the collection");
		}
		
		double total = 0;
		for(int i = index-offset;i<index;i++) {
			total += data[i].getValue();
		}
		return new SimpleData(data[index].getStock(), data[index].getDate(), total/offset);
	}
	
	/**
	 * 
	 * @param data the set of dayly data that will be used
	 * @param date the date from wich the avarage should be calculated
	 * @param offset the offest over how long period the avarage should be calculated
	 * @return the simple moving avarage for one specific date at the specified index of the collection
	 */
	public static SimpleData getSMA(PriorityQueue<? extends SimpleData> data, Calendar date, int offset) {
		SimpleData[] temp = new SimpleData[data.size()];
		temp = data.toArray(temp);
		int index = Arrays.binarySearch(temp, new SimpleData(null, date, 0), SimpleData.getDateComperator());
		return getSMA(temp,index,offset);
	}
}
