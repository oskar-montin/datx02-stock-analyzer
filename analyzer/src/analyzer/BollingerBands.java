package analyzer;

import java.util.PriorityQueue;


import data.Curve;
import data.Result;
import data.Signal;
import data.SimpleData;

public class BollingerBands implements AnalysisMethod{
	private SimpleData[] data;
	private PriorityQueue<SimpleData> closePriceRef;
	private int offset;
	private PriorityQueue<SimpleData> upper, middle, lower;
	int value;
	
	public BollingerBands(PriorityQueue<? extends SimpleData> data, int offset) {
		this.offset = offset;
		this.closePriceRef = (PriorityQueue<SimpleData>) data;
		this.data = new SimpleData[data.size()];
		this.data = data.toArray(this.data);
		this.upper = new PriorityQueue<SimpleData>();
		this.middle = new PriorityQueue<SimpleData>();
		this.lower = new PriorityQueue<SimpleData>();
		createBounds();
	}
	
	private void createBounds() {
		SimpleData sma = null;
		double deviationMultiplier = 2;
		double standardDeviation = 0.0;
		if(offset == 10) {
			deviationMultiplier = 1.9;
		}
		for(int i = offset;i<this.data.length;i++) {
			sma = SimpleMovingAverage.getSMA(data, i, offset);
			this.middle.add(sma);
			standardDeviation = standardDeviation(this.data, i, offset).getValue();
			this.upper.add(new SimpleData(data[i].getStock(),data[i].getDate(),sma.getValue()+deviationMultiplier*standardDeviation));
			this.lower.add(new SimpleData(data[i].getStock(),data[i].getDate(),sma.getValue()-deviationMultiplier*standardDeviation));
		}
		if(sma==null) {
			value = 50;
		} else {
			double spann = 4*standardDeviation;
			double lastValue = data[data.length-1].getValue()-sma.getValue()+deviationMultiplier*standardDeviation;
			value = (int) (lastValue*100/spann);
		}
	}
	
	private SimpleData standardDeviation(SimpleData [] sd, int index, int offset) {
		if(index<offset*2) {
			return new SimpleData(sd[index].getStock(),sd[index].getDate(),0.0);
		}
		double total = 0;
		for(int i = index-offset+1;i<=index;i++) {
			double sma = SimpleMovingAverage.getSMA(data, i, offset).getValue();
			total += Math.pow(sd[i].getValue()-sma,2);
		}
		return new SimpleData(sd[index].getStock(),sd[index].getDate(),Math.sqrt(total/offset));
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
		curves[2] = new Curve(getLaggAdjusted(this.middle, this.closePriceRef),"The simple moving avarage curve");
		curves[3] = new Curve(getLaggAdjusted(this.upper, this.closePriceRef),"The upper bound curve");
		return curves;
	}
	
	private PriorityQueue<SimpleData> getLaggAdjusted(PriorityQueue<SimpleData> queue, PriorityQueue<SimpleData> goalPattern) {
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
		return "Bollinger bands";
	}

	@Override
	public Result getResult() {
		Double value = this.value();
		return new Result("Bollinger Bands", value, this.resultString(), this.getGraph(), getSignal());
	}

	@Override
	public Signal getSignal() {
		Double value = this.value();
		Signal signal;
		if(value>=100) {
			signal = Signal.BUY;
		} else if(value<=0) {
			signal = Signal.SELL;
		} else {
			signal = Signal.NONE;
		}
		return signal;
	}
}
