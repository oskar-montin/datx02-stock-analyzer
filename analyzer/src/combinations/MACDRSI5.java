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
 * Simple MACD and RSI combination, which only signals if both methods signal. This combination uses a filtered version of
 * MACD.
 * 
 * @author oskarnylen
 */

public class MACDRSI5 implements AnalysisMethod {

	private PriorityQueue<? extends SimpleData> dataQueue;
	
	private MACD macd;
	private RelativeStrengthIndex rsi;


	public MACDRSI5(Collection<? extends SimpleData> queue, 
				int firstMACD, int secondMACD, int signalMACD, int offsetRSI) {
		dataQueue = new PriorityQueue<SimpleData>(queue);

		macd = new MACD(dataQueue, firstMACD, secondMACD, signalMACD);
		macd.setFilter(true);
		
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
		if(macd.getHistogram().peek().getValue() < 0 && rsi.getSignal() == Signal.SELL)
			return Signal.SELL;
		else if(macd.getHistogram().peek().getValue() > 0 && rsi.getSignal() == Signal.BUY)
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