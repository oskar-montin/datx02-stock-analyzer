package combinations;

import java.util.Collection;
import java.util.PriorityQueue;

import analyzer.AnalysisMethod;
import analyzer.Fibonacci;
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

public class MACDFib1 implements AnalysisMethod {

	private PriorityQueue<DailyData> dataQueue;
	
	private MACD macd;
	private Fibonacci fib;


	public MACDFib1(Collection<DailyData> queue, 
				int firstMACD, int secondMACD, int signalMACD,
				int fibOffset) {
		dataQueue = new PriorityQueue<DailyData>(queue);

		macd = new MACD(dataQueue, firstMACD, secondMACD, signalMACD);

		fib = new Fibonacci(dataQueue, fibOffset);

	}

	@Override
	public String resultString() {
		return "MACD = " + macd.getSignal() + "\n" +
			   "RSI = " + fib.getSignal() + "\n" + 
			   "MACD+RSI = "+ this.getSignal();
	}

	@Override
	public double value() {
		return 0;
	}

	public Signal getSignal() {
		if(macd.getMACDFiltered().peek().getValue() < 0 && fib.getSignal() == Signal.SELL)
			return Signal.SELL;
		else if(macd.getMACDFiltered().peek().getValue() > 0 && fib.getSignal() == Signal.BUY)
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