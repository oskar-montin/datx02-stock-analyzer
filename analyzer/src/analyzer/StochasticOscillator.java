package analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import data.DailyData;
import data.SimpleData;

/**
 * A class representing the method Stochastic Oscillator. This is a "full" version of the indicator
 * which means that it is possible to set smoothing-periods through the parameter speed(default 3, which is 
 * the standard for running the indicator in "slow" mode).
 * @author Axner
 *
 */

public class StochasticOscillator {
	
	private int shortPeriod, midPeriod, longPeriod, speed;
	private List<DailyData> dailyData;
	private List<SimpleData> kList = new ArrayList<SimpleData>();
	private List<SimpleData> dList = new ArrayList<SimpleData>();
	
	/*
	 * Constructor automatically setting numerical settings to standard 5 9 and 14 day periods and speed 3.
	 */
	public StochasticOscillator(PriorityQueue<DailyData> dailyData){
		
		setParameters(dailyData,5,9,14,3);
	}
	
	/*
	 * User specified settings for periods. Speed is recommended to be no larger than 3.
	 */
	public StochasticOscillator(PriorityQueue<DailyData> dailyData, int shortPeriod,
								int midPeriod, int longPeriod, int speed){
		
		setParameters(dailyData, shortPeriod, midPeriod, longPeriod, speed);
	}
	
	/*
	 * Returns the list of %K and %D values respectively, ordered by date from oldest to newest. 
	 * The list returned will be of 13+2(speed-1) shorter in length
	 *  than amount of days that Stochastic Oscillator was run over.
	 * This is due to the requirement of having 14 days to analyze and speed-days to smooth over.
	 * The returned array will contain values from 0.00 to 1.00 for each analyzed day.
	 */
	public List<SimpleData> getK(){
		
		return kList;
	}
	public List<SimpleData> getD(){
		
		return dList;
	}
	
	private void stochasticOscillator(){
		
		while(dailyData.size() >= longPeriod){
			int s = 0;
			periods();
			dailyData.remove(s);
			s++;
		}
		if(!(kList.size() % speed == 0) || !(dList.size() % speed == 0)){
			throw new IllegalArgumentException("**Ignore Exception type! However SO did not successfully finish.");
		}
		smooth();
	}
	
	/*
	 * A method that extracts the required elements(low, high, closing) from dailydata
	 */
	private void periods(){
		
		int s = longPeriod-1, j = 0;
		double lowestLow = 0, highestHigh = 0;
		DailyData currentDailyData;
		
		while (j < longPeriod){
			int k = 0;
			s -= longPeriod-1;
			currentDailyData = dailyData.get(s);
			SimpleData sd = new SimpleData(currentDailyData.getStock(), currentDailyData.getDate(),
							currentDailyData.getClosePrice());
			while(k < longPeriod){
				currentDailyData = dailyData.get(s);
				if(currentDailyData.getLow() < lowestLow){
					lowestLow = currentDailyData.getLow();
				}
				else if(currentDailyData.getHigh() > highestHigh){
					highestHigh = currentDailyData.getHigh();
				}
				if(k == shortPeriod-1){
					computeK(lowestLow, highestHigh, sd);
				}
				else if(k == midPeriod-1){
					computeK(lowestLow, highestHigh, sd);
				}
				else if(k == longPeriod-1){
					computeK(lowestLow, highestHigh, sd);
				}
				k++;
				s++;
			}
			j++;
		}
	}
	
	/*
	 * The list kList will contain Kvalues where three consecutive
	 * entries represent %K for the three different periods, together forming %K for one day.
	 */
	private void computeK (double lowestLow, double highestHigh, SimpleData simpleData){
		
		double value = 100*((simpleData.getValue() - lowestLow) / (highestHigh - lowestLow));
		SimpleData sd = new SimpleData(simpleData.getStock(), simpleData.getDate(), value);
		kList.add(sd);
	}
	
	/*
	 * Trims the list to contain the averaged values for periods short,medium,long. 
	 */
	private List<SimpleData> trimList(List<SimpleData> k){
		
		List <SimpleData> list = new ArrayList<SimpleData>((k.size()/3)+1);
		for (int i = 0; i < k.size(); i = i+3){
			SimpleData sd = new SimpleData(k.get(i).getStock(), k.get(i).getDate(),
					(k.get(i).getValue() + k.get(i+1).getValue() + k.get(i+2).getValue())/3);
			list.add(sd);
		}
		return list;
	}
	/*
	 * Smoothes the lists by using SMA.
	 *
	 */
	private void smooth(){
		
		List<SimpleData> k = new ArrayList<SimpleData>(trimList(kList));
		kList.clear();
		
		if (speed > 0){
			SimpleMovingAverage sma = new SimpleMovingAverage(k, speed);
			kList.addAll(sma.getMovingAverage());
			sma = new SimpleMovingAverage(kList, speed);
			dList.addAll(sma.getMovingAverage());
			
			for(int i = 0; i < speed-1; i++){
				kList.remove(0);
			}
		}
	}
	
	/*
	 * Method for setting variables according to constructor arguments.
	 */
	private void setParameters(PriorityQueue<DailyData> dailyData, int shortPeriod,
								int midPeriod, int longPeriod,int speed){
		
		this.shortPeriod = shortPeriod;
		this.midPeriod = midPeriod;
		this.longPeriod = longPeriod;
		this.speed = speed;
		this.dailyData = new ArrayList<DailyData>(dailyData);
		stochasticOscillator();
	}
}
