package analyzer;

import java.util.ArrayList;
import java.util.PriorityQueue;

import util.Util;

import data.Result;
import data.Signal;
import data.SimpleData;

public class BBStochRSI1 extends BBRSI {

	public BBStochRSI1(PriorityQueue<? extends SimpleData> data, int offset) {
		super(data, offset);
		calculateSignals();
	}
	
	private void calculateSignals() {
		ArrayList<SimpleData>[] bands = this.vb.getBands();
		
		if(vb.getSignal() == Signal.BUY && stochRSI.getSignal() == Signal.BUY) {
			entry(bands[0]);
		} else if(vb.getSignal() == Signal.SELL && stochRSI.getSignal() == Signal.SELL) {
			exit(bands[2]);
		}
	}
	
	private void exit(ArrayList<SimpleData> upperBand) {
		if(dailyData[dailyData.length-2].getValue()>upperBand.get(dailyData.length-2).getValue()) {
			signal = Signal.BUY;
			
		}
		
	}
	
	public Signal getSignal() {
		return signal;
	}
	
	@Override
	public Result getResult() {
		return new Result("Bollinger and StochRSI 1", value(), resultString(), getGraph(), getSignal());
	}
	
	private void entry(ArrayList<SimpleData> lowerBand) {
		//If the day before was an up day and inside the bands
		if(dailyData[dailyData.length-2].getValue()>lowerBand.get(dailyData.length-2).getValue()) {
			if(Util.isUpDay(dailyData[dailyData.length-1])) {
				//If gone inside the bollingerbands again
				signal = Signal.BUY;
			}
		}
	}

}
