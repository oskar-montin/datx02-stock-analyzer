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
	final double MAX = 1.0;
			
			
	int value;
	private SimpleData[] data;
	private int offset;
	private PriorityQueue<SimpleData> r0,r1,r2,r3,r4,r5;
	private double max;
	private double min;
	
	//constructor that creates retracement curves
	public Fibonacci(PriorityQueue<? extends SimpleData> data, int offset) {
		this.offset = offset;
		this.data = new SimpleData[data.size()];
		this.data = data.toArray(this.data);
		r0 = new PriorityQueue<SimpleData>();
		r1 = new PriorityQueue<SimpleData>();
		r2 = new PriorityQueue<SimpleData>();
		r3 = new PriorityQueue<SimpleData>();
		r4 = new PriorityQueue<SimpleData>();
		r5 = new PriorityQueue<SimpleData>();
		createRetracements();
		max = findMax().getValue();
		min = findMin().getValue();
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
		System.out.println("Minvärdet ar: " + findMin().getValue());
		System.out.println("Maxvärdet är: " + findMax().getValue());
		System.out.println("Trending? " + isTrending());
		
		r1.add(new SimpleData(findMin()));
		r1.add(new SimpleData(findMax()));
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
		Curve[] curves = new Curve[7];
		curves[0] = new Curve(data, "Price");
		curves[1] = new Curve(createRetracementLine(findMax().getValue()),"100");
		curves[2] = new Curve(createRetracementLine(findMin().getValue()),"0");
		curves[3] = new Curve(createRetracementLine(findLevel(L4, max, min)),"61.8");
		curves[4] = new Curve(createRetracementLine(findLevel(L3, max, min)),"50");
		curves[5] = new Curve(createRetracementLine(findLevel (L2, max, min)),"38.2");
		curves[6] = new Curve(createRetracementLine(findLevel (L1, max, min)),"23.6");
		return curves;
	}

	@Override
	public String resultString() {
		// TODO Auto-generated method stub
		return "Fibonacci retracements";
	}

	@Override
	public Result getResult() {
		Double value = this.value();
		Signal signal = Signal.NONE;
		return new Result("Fibonacci retracements", value, this.resultString(), this.getGraph(), signal);
	}
}
	
	
