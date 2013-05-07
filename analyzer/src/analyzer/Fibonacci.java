package analyzer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

import data.Curve;
import data.Result;
import data.Signal;
import data.SimpleData;

/**
 * A class that calculates and plots Fibonacci time zones and retracements,
 * leaving no buy or sell recommendation.
 * 
 * @author Johanna
 *
 */

public class Fibonacci implements AnalysisMethod{
	
	//The different Fibonacci levels.
	final double MIN = 0.0;
	final double L1 = 0.236;
	final double L2 = 0.382;
	final double L3 = 0.500;
	final double L4 = 0.618;
	final double L5 = 0.854;
	final double MAX = 1.0;
			
	private int value;
	private SimpleData[] data;
	private int offset;
	private double max;
	private double min;
	private TrendLine tl;
	
	//constructor that creates retracement curves
	public Fibonacci(PriorityQueue<? extends SimpleData> data, int offset) {
		this.offset = offset;
		this.data = new SimpleData[data.size()];
		this.data = data.toArray(this.data);
		tl = new TrendLine(data, offset);
		//max = this.data[tl.getLatestMaxPos()].getValue();
		//min = this.data[tl.getLatestMinPos()].getValue();
		max = findMax().getValue();
		min = findMin().getValue();
		createRetracements();
	}
	
	//method that returns the maximum stock price for a stock chart
	private SimpleData findMax(){
		double max = 0;
		int maxpos = -1;
		for(int i = 0; i < data.length; i++){
			double currentValue = data[i].getValue();
			if(currentValue > max){
				max = currentValue;
				maxpos = i;
			}
			
		}
		if(maxpos == -1) 
			return null;
		else
			return data[maxpos];
	}
	
	
	//method that returns the minimum stock price for a stock chart
	private SimpleData findMin(){
		double min = 1e12;
		int minpos = -1;
		for(int i = 0; i < data.length; i++){
			double currentValue = data[i].getValue();
			if(currentValue < min){
				min = currentValue;
				minpos = i;
			}
			
		}
		if(minpos == -1)
			return null;
		else 
			return data[minpos];
	}
	
	
	// Creates Fibonacci retracements curves
	private void createRetracements() {
		System.out.println("Minvärdet ar: " + min);
		System.out.println("Maxvärdet är: " + max);
		System.out.println("Trending? " + isTrending());
	}

	//check if curve is trending
	private boolean isTrending(){
		if(findMin().getDate().after(findMax().getDate()))
				return false;
		else
				return true;		
	}
	
	private SimpleData[] createRetracementLine(double value) {
		SimpleData[] ret = new SimpleData[data.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = new SimpleData(data[i].getStock(), data[i].getDate(), value);
		}
		return ret;
	}

	private double findLevel(double l, double max, double min){
		return min + (max - min)*l;
		
	}
		
	@Override
	public double value() {
		return this.value;
	}

	@Override
	public Curve[] getGraph() {
		Curve[] curves = new Curve[6];
		curves[0] = new Curve(data, "Price");
		curves[1] = new Curve(createRetracementLine(max),"100");
		curves[2] = new Curve(createRetracementLine(min),"0");
		curves[3] = new Curve(createRetracementLine(findLevel(L4, max, min)),"61.8");
		curves[4] = new Curve(createRetracementLine(findLevel(L3, max, min)),"50");
		curves[5] = new Curve(createRetracementLine(findLevel (L2, max, min)),"38.2");
		return curves;
	}

	@Override
	public String resultString() {
		return "Fibonacci retracements";
	}

	@Override
	public Result getResult() {
		Double value = this.value();
		return new Result("Fibonacci retracements", value, this.resultString(), this.getGraph(), getSignal());
	}

	/**
	 * Generate buy/sell/keep signal
	 * 
	 * If upwards trending and between 38.2 % and 61.8 % retracement -> sell
	 * If upwards trending and close 100 %  -> sell
	 * If downwards trending and between 38.2 % and 61.8 % retracement -> buy
	 * If downwards trending and close 0 %  -> buy
	 */
	@Override
	public Signal getSignal() {
		
		Signal s = Signal.NONE;
		double LIMIT = 0.01;
		
		double trend = tl.getLatestTrend();
		/* Current price */
		double current = data[data.length - 1].getValue();
		/* Value of 62.1 % retracement */
		double r62 = findLevel(L4, max, min);
		/* Value of 38.2 % retracement */
		double r38 = findLevel(L2, max, min);
		
		if (trend > 0) {
			if (current > r38 && current < r62)
				s = Signal.SELL;
			else if ( Math.abs((max - current)/max) < LIMIT )
				s = Signal.SELL;
		}
		else {
			if (current > r38 && current < r62)
				s = Signal.BUY;
			else if ( Math.abs((min - current)/min) < LIMIT )
				s = Signal.BUY;
		}
		
		return s;
	}
}
	
	
