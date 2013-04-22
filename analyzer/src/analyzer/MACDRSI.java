package analyzer;

import java.util.Collection;
import java.util.PriorityQueue;

import data.Curve;
import data.Result;
import data.Signal;
import data.SimpleData;

public class MACDRSI implements AnalysisMethod {

	private PriorityQueue<? extends SimpleData> dataQueue;
	
	private MACD macd;
	private RelativeStrengthIndex rsi;
	
	private Signal[] signals;
	
	private int firstMACD;
	private int secondMACD;
	private int signalMACD;
	private int offsetRSI;

	public MACDRSI(Collection<? extends SimpleData> queue, 
				int firstMACD, int secondMACD, int signalMACD, int offsetRSI) {
		dataQueue = new PriorityQueue<SimpleData>(dataQueue);
		this.firstMACD = firstMACD;
		this.secondMACD = secondMACD;
		this.signalMACD = signalMACD;
		this.offsetRSI = offsetRSI;
		
		macd = new MACD(dataQueue, firstMACD, secondMACD, signalMACD);
		rsi = new RelativeStrengthIndex(dataQueue.peek().getStock(), dataQueue, offsetRSI);
		
		signals = new Signal[5];
		
		calculateSignals();
	}

	private void calculateSignals() {
		signals[0] = macd.getResult().getSignal();
		signals[1] = rsi.getResult().getSignal();
		if(signals[0] == signals[1]) {
			signals[3] = signals[0];
		} else {
			signals[3] = Signal.NONE;
		}
	}	

	@Override
	public String resultString() {
		return "MACD = " + signals[0].getString() + "\n" +
			   "RSI = " + signals[1].getString() + "\n" + 
			   "MACD+RSI = "+ signals[2].getString();
	}

	@Override
	public double value() {
		if(signal == Signal.NONE) {
			return 0;
		}
		return signal==Signal.BUY?1:-1;
	}

	@Override
	public Curve[] getGraph() {
		return new Curve[0];
	}

	@Override
	public Result getResult() {
		return new Result("MACDRSI", value(), resultString(), getGraph(), Signal.NONE);
	}
}