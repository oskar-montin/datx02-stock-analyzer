package analyzer;

import java.util.ArrayList;
import java.util.PriorityQueue;

import util.Util;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import data.SimpleData;

public class VolatilityBands implements AnalysisMethod{
	private Signal signal = Signal.NONE;
	private DailyData[] data;
	private PriorityQueue<SimpleData> closePriceRef;
	private int offset;
	private ArrayList<SimpleData> upper, middle, lower;
	private double value;
	
	public VolatilityBands(PriorityQueue<? extends SimpleData> data, int offset) {
		this.offset = offset;
		this.closePriceRef = (PriorityQueue<SimpleData>) data;
		this.data = new DailyData[data.size()];
		this.data = data.toArray(this.data);
		this.upper = new ArrayList<SimpleData>();
		this.middle = new ArrayList<SimpleData>();
		this.lower = new ArrayList<SimpleData>();
		createBounds();
	}
	
	private void createBounds() {
		SimpleData ema = null;
		double deviationMultiplier = 2;
		double standardDeviation = 0.0;
		if(offset <= 10) {
			deviationMultiplier = 1.9;
		}
		for(int i = offset;i<this.data.length;i++) {
			ema = ExponentialMovingAverage.getEMA(data, i, offset);
			this.middle.add(ema);
			standardDeviation = standardDeviation(this.data, i, offset).getValue();
			this.upper.add(new SimpleData(data[i].getStock(),data[i].getDate(),ema.getValue()+deviationMultiplier*standardDeviation));
			this.lower.add(new SimpleData(data[i].getStock(),data[i].getDate(),ema.getValue()-deviationMultiplier*standardDeviation));
		}
		if(ema==null) {
			value = 50;
		} else {
			//double spann = 4*standardDeviation;
			//double lastValue = data[data.length-1].getValue()-ema.getValue()+deviationMultiplier*standardDeviation;
			//value = (int) (lastValue*100/spann);
			value = (data[data.length-1].getClosePrice()-this.lower.get(this.lower.size()-1).getValue())/
					(this.upper.get(this.upper.size()-1).getValue()-this.lower.get(this.lower.size()-1).getValue());
		}
	}
	
	private SimpleData standardDeviation(SimpleData [] sd, int index, int offset) {
		if(index<offset*2) {
			return new SimpleData(sd[index].getStock(),sd[index].getDate(),0.0);
		}
		double total = 0;
		for(int i = index-offset+1;i<=index;i++) {
			double ema = ExponentialMovingAverage.getEMA(data, i, offset).getValue();
			total += Math.pow(sd[i].getValue()-ema,2);
		}
		return new SimpleData(sd[index].getStock(),sd[index].getDate(),Math.sqrt(total/offset));
	}
	
	public ArrayList<SimpleData>[] getBands() {
		ArrayList<SimpleData>[] lists = new ArrayList[3];
		lists[0] = this.lower;
		lists[1] = this.middle;
		lists[2] = this.upper;
		return lists;
	}

	/**
	 * @return the value of this bollingerband. The result will be an integer, if the last close price
	 * is inside the bollinger band it will return a value between 0 and 100, if its outside (happends in maybe 5-10%
	 * of the cases it can be for instance -2 or 108. Cases where this happens is interesting and should therefore
	 * not be inside the percent spann of 0 to 100. However, in a perfect world with a perfect implementation of the bollinger
	 * bands this should not occur. A high value represents an overbought market, low underbought.
	 */
	@Override
	public double value() {
		return this.value;
	}

	@Override
	public Curve[] getGraph() {
		Curve[] curves = new Curve[4];
		curves[0] = new Curve(this.closePriceRef,"Close price");
		
		curves[1] = new Curve(getLaggAdjusted(this.lower, this.closePriceRef),"The lower bound curve");
		curves[2] = new Curve(getLaggAdjusted(this.middle, this.closePriceRef),"The moving avarage curve");
		curves[3] = new Curve(getLaggAdjusted(this.upper, this.closePriceRef),"The upper bound curve");
		return curves;
	}
	
	private PriorityQueue<SimpleData> getLaggAdjusted(ArrayList<SimpleData> queue, PriorityQueue<SimpleData> goalPattern) {
		PriorityQueue<SimpleData> temp = new PriorityQueue<SimpleData>(goalPattern);
		PriorityQueue<SimpleData> returnQueue = new PriorityQueue<SimpleData>();
		while(queue.size()<temp.size()) {
			returnQueue.add(temp.poll());
		}
		for(SimpleData s:queue) {
			returnQueue.add(s);
		}
		return returnQueue;	
	}

	@Override
	public String resultString() {
		// TODO Auto-generated method stub
		return "Volatility bands";
	}
	
	public Signal getSignal() {
		Signal signal;
		
		if(this.value<0.05) {
			signal = Signal.BUY;
		} else if(this.value>0.95) {
			signal = Signal.SELL;
		} else {
			signal = Signal.NONE;
		}
		return signal;
	}

	@Override
	public Result getResult() {
		Double value = this.value();
		
		return new Result("Volatility Bands", value, this.resultString(), this.getGraph(), getSignal());
	}
}
