package analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import data.DailyData;

/**
 * A class representing the method Stochastic Oscillator. 
 * @author Axner
 *
 */

public class StochasticOscillator {
	
	private int shortPeriod, midPeriod, longPeriod; 
	private List<DailyData> dailyData;
	private List<Double> kList = new ArrayList<Double>();
	private List<Double> dList = new ArrayList<Double>();
	
	/*
	 * Constructor automatically setting numerical settings to standard 5 9 and 14 day periods.
	 */
	public StochasticOscillator(PriorityQueue<DailyData> dailyData){
		
		setParameters(dailyData,5,9,14);
	}
	
	/*
	 * User specified settings for periods.
	 */
	public StochasticOscillator(PriorityQueue<DailyData> dailyData, int shortPeriod,
								int midPeriod, int longPeriod){
		
		setParameters(dailyData, shortPeriod, midPeriod, longPeriod);
	}
	
	/*
	 * Returns the list of %K, %D values, which are ordered from newest to oldest. The list returned
	 * will be of 13 shorter in length than amount of days that Stochastic Oscillator was run over.
	 * This is due to the requirement of having 14 days to analyze. 
	 */
	public List<Double> getK(){
		
		return kList;
	}
	public List<Double> getD(){
		
		return dList;
	}
	
	private void stochasticOscillator(){
		
		while(dailyData.size() >= longPeriod){
			int s = 0;
			periods();
			dailyData.remove(s);
			s++;
		}
		if(!(kList.size() % 3 == 0) || !(dList.size() % 3 == 0)){
			throw new IllegalArgumentException("Ignore Exception type! However SO did not successfully finish.");
		}
		trimLists();
	}
	
	/*
	 * A method that extracts the required elements(low, high, closing) from dailydata
	 */
	private void periods(){
		
		int s = longPeriod-1, j = 0;
		double lowestLow = 0, highestHigh = 0, closingPrice = 0;
		DailyData currentDailyData;
		
		while (j < longPeriod){
			int k = 0;
			s -= longPeriod-1;
			currentDailyData = dailyData.get(s);
			closingPrice = currentDailyData.getClosePrice();
			while(k < longPeriod){
				currentDailyData = dailyData.get(s);
				if(currentDailyData.getLow() < lowestLow){
					lowestLow = currentDailyData.getLow();
				}
				else if(currentDailyData.getHigh() > highestHigh){
					highestHigh = currentDailyData.getHigh();
				}
				if(k == shortPeriod-1){
					computeK(lowestLow, highestHigh, closingPrice);
				}
				else if(k == midPeriod-1){
					computeK(lowestLow, highestHigh, closingPrice);
				}
				else if(k == longPeriod-1){
					computeK(lowestLow, highestHigh, closingPrice);
				}
				k++;
				s++;
			}
			j++;
		}
	}
	
	/*
	 * Method for computing the value %K. The list kList will contain Kvalues where three consecutive
	 * entries represent K for the three different periods, together forming K for one day.
	 */
	private void computeK (double lowestLow, double highestHigh, double closingPrice){
		double k = 100*((closingPrice - lowestLow) / (highestHigh - lowestLow));
		kList.add(k);
	}
	
	/*
	 * Method for computing the value %D. The list dList will contain Dvalues where three consecutive
	 * entries represent D for the three different periods, together forming D for one day.
	 */
	private void computeD (){
		/*
		 * TODO once EMA is ready, use it.
		 */
	}
	
	/*
	 * Trims the lists to contain the averaged values. 
	 */
	private void trimLists(){
		List<Double> k = new ArrayList<Double>(kList);
		List<Double> d = new ArrayList<Double>(dList);
		kList.clear();
		dList.clear();
		for (int i = 0; i < k.size(); i = i+3){
			kList.add((k.get(i) + k.get(i+1) + k.get(i+2))/3);
			dList.add((d.get(i) + d.get(i+1) + d.get(i+2))/3);
		}
	}
	
	/*
	 * Method for setting variables according to constructor arguments.
	 */
	private void setParameters(PriorityQueue<DailyData> dailyData, int shortPeriod,
								int midPeriod, int longPeriod){
		
		this.shortPeriod = shortPeriod;
		this.midPeriod = midPeriod;
		this.longPeriod = longPeriod;
		this.dailyData = new ArrayList<DailyData>(dailyData);
		Collections.reverse(this.dailyData);
		stochasticOscillator();
	}
}
