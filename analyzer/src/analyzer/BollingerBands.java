package analyzer;

import java.util.Calendar;
import java.util.HashMap;
import java.util.PriorityQueue;

import util.Util;

import data.Curve;
import data.DailyData;
import data.SimpleData;

public class BollingerBands implements AnalysisMethod{
	private SimpleData[] data;
	private int offset;
	private PriorityQueue<SimpleData> close,upper, middle, lower;
	int value;
	
	public BollingerBands(PriorityQueue<? extends SimpleData> data, int offset) {
		this.offset = offset;
		this.close = new PriorityQueue<SimpleData>(data);
		this.data = new SimpleData[data.size()];
		this.data = data.toArray(this.data);
		this.upper = new PriorityQueue<SimpleData>();
		this.middle = new PriorityQueue<SimpleData>();
		this.lower = new PriorityQueue<SimpleData>();
		createBounds();
	}
	
	private void createBounds() {
		SimpleData sma = null;
		double standardDeviation = 0.0;
		for(int i = offset;i<this.data.length;i++) {
			sma = SimpleMovingAverage.getSMA(data, i, offset);
			this.middle.add(sma);
			standardDeviation = standardDeviation(this.data, i, offset).getValue();
			this.upper.add(new SimpleData(data[i].getStock(),data[i].getDate(),sma.getValue()+2*standardDeviation));
			this.lower.add(new SimpleData(data[i].getStock(),data[i].getDate(),sma.getValue()-2*standardDeviation));
		}
		if(sma==null) {
			value = 50;
		} else {
			double spann = 4*standardDeviation;
			double lastValue = data[data.length-1].getValue()-sma.getValue()+2*standardDeviation;
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
	public int value() {
		return this.value;
	}

	@Override
	public Curve[] getGraph() {
		Curve[] curves = new Curve[3];
		curves[0] = new Curve(this.lower,"The lower bound curve");
		curves[1] = new Curve(this.middle,"The simple moving avarage curve");
		curves[2] = new Curve(this.upper,"The upper bound curve");
		return curves;
	}

	@Override
	public String resultString() {
		// TODO Auto-generated method stub
		return null;
	}
}
