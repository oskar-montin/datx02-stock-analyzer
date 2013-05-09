package combinations;

import java.util.Collection;
import java.util.PriorityQueue;

import analyzer.AnalysisMethod;
import analyzer.CMF;
import analyzer.MACD;
import analyzer.RelativeStrengthIndex;
import analyzer.StochasticOscillator;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import data.SimpleData;

/**
 * Simple MACD and RSI combination, which only signals if both methods signal.
 * 
 * @author oskarnylen
 */

public class MACDCMF1 implements AnalysisMethod {

	private PriorityQueue<DailyData> dataQueue;
	
	private MACD macd;
	private CMF cmf;


	public MACDCMF1(Collection<DailyData> queue, 
				int firstMACD, int secondMACD, int signalMACD, int CMFoffset) {
		dataQueue = new PriorityQueue<DailyData>(queue);

		macd = new MACD(dataQueue, firstMACD, secondMACD, signalMACD);
		
		cmf = new CMF(dataQueue, CMFoffset);

	}

	@Override
	public String resultString() {
		return "MACD = " + macd.getSignal() + "\n" +
			   "RSI = " + cmf.getSignal() + "\n" + 
			   "MACD+RSI = "+ this.getSignal();
	}

	@Override
	public double value() {
		return 0;
	}

	public Signal getSignal() {
		if(macd.getHistogram().peek().getValue() < 0 && cmf.getSignal() == Signal.SELL)
			return Signal.SELL;
		else if(macd.getHistogram().peek().getValue() > 0 && cmf.getSignal() == Signal.BUY)
			return Signal.BUY;
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
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}
}