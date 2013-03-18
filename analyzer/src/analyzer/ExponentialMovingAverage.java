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
 * UNDER CONSTRUCTION, DO NOT USE
 * 
 * @author oskarnylen
 */
public class ExponentialMovingAverage {

	private Stock stock;
	private int offset;

	private double multiplier;

	private LinkedList<DailyData> dailyDataList;

	private LinkedList<SimpleData> movingAverageList;

	/**
	 * 
	 * @param stock
	 * @param offset
	 */
	public ExponentialMovingAverage(Stock stock, int offset){
		this.stock = stock;
		this.offset = offset;

		multiplier = (2/(offset+1));

		dailyDataList = new LinkedList<DailyData>(DatabaseHandler.getDailyData(stock));
		
		Collections.reverse(dailyDataList);
		
		for(int i = offset-1; i > 0; i--){
			dailyDataList.removeLast();
		}
		
		System.out.println("removed: " + dailyDataList);
		
		for(int i = 0; i < dailyDataList.size()-(offset-1); i++){
			movingAverageList.add(new SimpleData(dailyDataList.get(i)));
		}

		SimpleData firstSimpleMovingAverage = new SimpleMovingAverage(stock, offset).getMovingAverage().getLast();

		//		/*
		//		 * For every entry in the dailyDataList - (the offset-1)
		//		 * The offset-1 means that we we won't check more than we're able to. For example
		//		 * if I only have 5 entries, and I want to do the SMA 3, I will only do 5-(3-1), that is 5-2 = 3 SMA entries
		//		 * since there are not enough original entries for more.
		//		 */
		//		for(int i = 0; i < dailyDataList.size()-(offset-1); i++){
		//			double total = 0;
		//			movingAverageList.add(new SimpleData(dailyDataList.get(i)));
		//			for(int j = 0; j < offset; j++){
		//				total = total + dailyDataList.get(i+j).getClosePrice();
		//				System.out.println(total);
		//			}
		//			movingAverageList.get(i).setClosePrice(total/offset);
		//		}
		//		System.out.println(movingAverageList);

		calcEMA(firstSimpleMovingAverage);
	}

	public SimpleData calcEMA(SimpleData last){
		SimpleData sd = null;
		movingAverageList.getFirst().setClosePrice((dailyDataList.poll().getClosePrice() - last.getClosePrice())*multiplier+last.getClosePrice());

		if(dailyDataList.isEmpty()){
			return sd;
		}else{
			calcEMA(movingAverageList.getFirst());
		}

		return null;

	}
}