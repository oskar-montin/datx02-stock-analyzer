package combinations;

import java.util.Collection;
import java.util.PriorityQueue;

import analyzer.AnalysisMethod;
import analyzer.MACD;
import analyzer.RelativeStrengthIndex;

import data.Curve;
import data.Result;
import data.Signal;
import data.SimpleData;

/**
 * Simple MACD histogram and RSI combination, which only signals if both methods signal.
 * 
 * @author oskarnylen
 */

public class MACDHistogramRSI implements AnalysisMethod {

	private PriorityQueue<? extends SimpleData> dataQueue;
	
	private MACD macd;
	private RelativeStrengthIndex rsi;


	public MACDHistogramRSI(Collection<? extends SimpleData> queue, 
				int firstMACD, int secondMACD, int signalMACD, int offsetRSI) {
		dataQueue = new PriorityQueue<SimpleData>(dataQueue);

		macd = new MACD(dataQueue, firstMACD, secondMACD, signalMACD);
		macd.setHistogram(true);
		
		rsi = new RelativeStrengthIndex(dataQueue, offsetRSI);

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
		if(macd.getSignal() == rsi.getSignal())
			return macd.getSignal();
		else
			return Signal.NONE;
	}
	
	@Override
	public Curve[] getGraph() {
		return new Curve[0];
	}

	@Override
	public Result getResult() {
		return new Result("MACDRSI", value(), resultString(), getGraph(), getSignal());
	}
}