package combinations;

import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;

import analyzer.AnalysisMethod;
import analyzer.MACD;
import analyzer.RelativeStrengthIndex;

import data.Curve;
import data.Result;
import data.Signal;
import data.SimpleData;


/**
 * Simple MACD and RSI combination, that signals if yesterdays RSI and todays MACD signals additionally. This one uses
 * a filtered version of MACD.
 * 
 * @author oskarnylen
 */

public class MACDHistogramRSI3 implements AnalysisMethod {

	private PriorityQueue<? extends SimpleData> dataQueue;
	
	private MACD macd;
	
	private RelativeStrengthIndex rsi;
	private RelativeStrengthIndex yesterdayRSI;

	public MACDHistogramRSI3(Collection<? extends SimpleData> queue, 
				int firstMACD, int secondMACD, int signalMACD, int offsetRSI) {
		dataQueue = new PriorityQueue<SimpleData>(queue);

		
		LinkedList<? extends SimpleData> dataList = new LinkedList<SimpleData>(queue);
		dataList.removeLast();
		
		macd = new MACD(dataQueue, firstMACD, secondMACD, signalMACD);
		macd.setHistogram(true);
		
		rsi = new RelativeStrengthIndex(dataQueue, offsetRSI);
		yesterdayRSI = new RelativeStrengthIndex(dataList, offsetRSI);

	}

	@Override
	public String resultString() {
		return "MACD = " + macd.getSignal() + "\n" +
			   "RSI = " + rsi.getSignal() + "\n" + 
			   "MACD+RSI = "+ this.getSignal();
	}

	@Override
	public double value() {
		if(getSignal() == Signal.NONE) {
			return 0;
		}
		return getSignal()==Signal.BUY?1:-1;
	}

	public Signal getSignal() {
		if(macd.getSignal() == Signal.BUY && rsi.value() < 0)
			return Signal.BUY;
		else if(macd.getSignal() == Signal.SELL && rsi.value() > 0)
			return Signal.SELL;
		else {
			return Signal.NONE;
		}
	}
	
	@Override
	public Curve[] getGraph() {
		return new Curve[0];
	}

	@Override
	public Result getResult() {
		return new Result("MACDRSI", value(), resultString(), getGraph(), getSignal());
	}
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}
}