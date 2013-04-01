package analyzer;

import java.util.Calendar;
import java.util.HashMap;
import java.util.PriorityQueue;

import data.Curve;
import data.SimpleData;

public class BollingerBands implements AnalysisMethod{
	private SimpleData[] data;
	private int offset;
	private PriorityQueue<SimpleData> close,upper, middle, lower;
	
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
		for(int i = offset;i<this.data.length;i++) {
			SimpleData sma = SimpleMovingAverage.getSMA(data, i, offset);
			this.middle.add(sma);
			double standardDeviation = standardDeviation(this.data, i, offset).getValue();
			this.upper.add(new SimpleData(data[i].getStock(),data[i].getDate(),sma.getValue()+2*standardDeviation));
			this.lower.add(new SimpleData(data[i].getStock(),data[i].getDate(),sma.getValue()-2*standardDeviation));
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

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Curve[] getGraph() {
		Curve[] curves = new Curve[3];
		curves[0] = new Curve(this.lower,"The lower bound curve");
		curves[1] = new Curve(this.middle,"The simple moving avarage curve");
		curves[2] = new Curve(this.upper,"The upper bound curve");
		return curves;
	}
}
