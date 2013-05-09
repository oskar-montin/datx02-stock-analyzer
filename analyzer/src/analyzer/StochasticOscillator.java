package analyzer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import data.SimpleData;

/**
 * A class representing the method Stochastic Oscillator. This is a "full" version of the indicator
 * which means that it is possible to set smoothing-periods through the parameter speed(default 3, which is 
 * the standard for running the indicator in "slow" mode).
 * @author Axner
 *
 */

public class StochasticOscillator implements AnalysisMethod {
	
	private int shortPeriod, midPeriod, longPeriod, speed;
	private List<DailyData> dailyData;
	private List<SimpleData> kList = new ArrayList<SimpleData>();
	private List<SimpleData> dList = new ArrayList<SimpleData>();
	private final String resultString = "Indicates overbought if value>70 or oversold if value<30. Triggers buy or sell signal if %K,%D cross.";
	private Curve[] curves;
	private Result results;
	
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
		if(speed <= 0) {
			throw new IllegalArgumentException("SO will not work with speed < 1");
		}
		setParameters(dailyData, shortPeriod, midPeriod, longPeriod, speed);
	}
	
	/*
	 * Returns the list of %K and %D values respectively, ordered by date from oldest to newest. 
	 * The list returned will be of 13+2(speed-1) shorter in length
	 *  than amount of days that Stochastic Oscillator was run over.
	 * This is due to the requirement of having 14 days to analyze and speed-days to smooth over.
	 * The returned array will contain values from 0.00 to 1.00 for each analyzed day.
	 */
	@Override
	public String resultString() {
		
		return resultString;
	}

	@Override
	public double value() {
		// last value of kList.
		return kList.get(kList.size()-1).getValue();
	}

	@Override
	public Curve[] getGraph() {
		
		return curves;
	}
	
	@Override
	public Result getResult() {
		
		return results;
	}
	
	private void stochasticOscillator(){
		
		periods();
		if(!(kList.size() % 3 == 0) || !(dList.size() % 3 == 0)){
			throw new IllegalArgumentException("**Ignore Exception type!" +
									" However SO did not successfully finish.");
		}
		smooth();
		curves();
		results = new Result("Stochastic Oscillator", value(), resultString, getGraph(), getSignal());
	}
	
	/*
	 * A method that extracts the required elements(low, high, closing) from dailydata
	 */
	private void periods(){
		
		int j = 0;
		double lowestLow = 100000000, highestHigh = 0;
		DailyData currentDailyData;
		double tempHigh, tempLow;
		while (j < (dailyData.size()-longPeriod)){
			int k = 0;
			int s = j;
			SimpleData sd = new SimpleData(dailyData.get(j).getStock(), 
					dailyData.get(j).getDate(),
					dailyData.get(j).getClosePrice());
			while(k < longPeriod){
				
				currentDailyData = dailyData.get(s);
	
				if(currentDailyData.getClosePrice() > currentDailyData.getHigh()){
					tempHigh = currentDailyData.getClosePrice();
				}
				else{
					tempHigh = currentDailyData.getHigh();
				}
				if(currentDailyData.getClosePrice() < currentDailyData.getLow()){
					tempLow = currentDailyData.getClosePrice();
				}
				else{
					tempLow = currentDailyData.getLow();
				}
				
				if(0 < tempLow && tempLow < lowestLow){
					lowestLow = tempLow;
				}
				if(tempHigh > highestHigh){
					highestHigh = tempHigh;
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
				tempLow = 0;
				tempHigh = 0;
				k++;
				s++;
			}
			highestHigh = 0;
			lowestLow = 10000000;
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
		Collections.reverse(k);
		kList.clear();
		SimpleMovingAverage sma = new SimpleMovingAverage(k, speed);
		kList.addAll(sma.getMovingAverage());
		sma = new SimpleMovingAverage(kList, 3);
		dList.addAll(sma.getMovingAverage());
			
		for(int i = 0; i < 2; i++){
			kList.remove(0);
		}
	}
	
	@Override
	public Signal getSignal(){
		
		double firstD = dList.get(dList.size()-2).getValue();
		double secondD = dList.get(dList.size()-1).getValue();
		double firstK = kList.get(kList.size()-2).getValue();
		double secondK = kList.get(kList.size()-1).getValue();
		
		if(firstD > firstK && secondD < secondK){
			return Signal.BUY;
		}
		else if(firstD < firstK && secondD > secondK){
			return Signal.SELL;
		}
		return Signal.NONE;
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
		Collections.reverse(this.dailyData);
		stochasticOscillator();
	}
	
	private void curves() {
		curves = new Curve[2];
		PriorityQueue<SimpleData> kQ = new PriorityQueue<SimpleData>();
		PriorityQueue<SimpleData> dQ = new PriorityQueue<SimpleData>();
		for (int i = 0; i<kList.size(); i++){
			kQ.add(kList.get(i));
			dQ.add(dList.get(i));
		}
		Curve KCurve = new Curve(kQ, "%K");
		Curve DCurve = new Curve(dQ, "%D");
		curves[0] = KCurve;
		curves[1] = DCurve;
	}
	
	@Override
	public String getName() {
		return this.getClass().getName()+"-Offsets:"+this.shortPeriod+", "+this.midPeriod+", "+this.longPeriod+", "+this.speed;
	}
}
