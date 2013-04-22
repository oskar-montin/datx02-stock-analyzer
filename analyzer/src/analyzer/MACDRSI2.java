package analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;

import data.Curve;
import data.Result;
import data.Signal;
import data.SimpleData;

public class MACDRSI2 implements AnalysisMethod {

	private PriorityQueue<? extends SimpleData> dataQueue;
	
	private MACD macd;
	private MACD yesterdayMACD;
	
	private RelativeStrengthIndex rsi;
	private RelativeStrengthIndex yesterdayRSI;
	
	private int firstMACD;
	private int secondMACD;
	private int signalMACD;
	private int offsetRSI;

	public MACDRSI2(Collection<? extends SimpleData> queue, 
				int firstMACD, int secondMACD, int signalMACD, int offsetRSI) {
		dataQueue = new PriorityQueue<SimpleData>(dataQueue);
		this.firstMACD = firstMACD;
		this.secondMACD = secondMACD;
		this.signalMACD = signalMACD;
		this.offsetRSI = offsetRSI;
		
		LinkedList<? extends SimpleData> dataList = new LinkedList<SimpleData>(queue);
		dataList.removeLast();
		
		macd = new MACD(dataQueue, firstMACD, secondMACD, signalMACD);
		yesterdayMACD = new MACD(dataList, firstMACD, secondMACD, signalMACD);
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