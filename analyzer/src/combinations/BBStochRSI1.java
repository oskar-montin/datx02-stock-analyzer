package combinations;

import java.util.ArrayList;
import java.util.PriorityQueue;

import util.Util;

import data.Result;
import data.Signal;
import data.SimpleData;

/**
 * This combination of stochRSI and volatility bands looks at two days, today and yesterday. 
 * If yesterday was an upday without volatility bands buy signal and today both rsi and volatility bands says buy, buy.
 * Sell if both vb and rsi says sell.
 * @author Guribur
 *
 */
public class BBStochRSI1 extends BBRSI {

	public BBStochRSI1(PriorityQueue<? extends SimpleData> data, int bbOffset, int rocOffset) {
		super(data, bbOffset, rocOffset);
		calculateSignals();
	}
	
	private void calculateSignals() {
		ArrayList<SimpleData>[] bands = this.vb.getBands();
		
		if(vb.getSignal() == Signal.BUY && stochRSI.getSignal() == Signal.BUY) {
			entry(bands[0]);
		} else if(vb.getSignal() == Signal.SELL && stochRSI.getSignal() == Signal.SELL) {
			signal = Signal.SELL;
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
		if(dailyData[dailyData.length-2].getValue()>lowerBand.get(lowerBand.size()-2).getValue()) {
			if(Util.isUpDay(dailyData[dailyData.length-1])) {
				//If gone inside the bollingerbands again
				signal = Signal.BUY;
			}
		}
	}

}
