package combinations;

import java.util.PriorityQueue;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import analyzer.AnalysisMethod;
import analyzer.CMF;
import analyzer.ExponentialMovingAverage;

/**
 * A class that recommends buying and selling depending on a combination
 * of trending with EMA and reversal signals from CMF
 * 
 * @author Tom Schön
 */
public class CMFEMA implements AnalysisMethod {
	
	protected final CMF today;
	protected final CMF yesterday;
	protected final ExponentialMovingAverage ema;
	public CMFEMA(PriorityQueue<DailyData> data, int offset) {
		
		PriorityQueue<DailyData> tempQueue = new PriorityQueue<DailyData>(data);
		PriorityQueue<DailyData> yesterdayQueue = new PriorityQueue<DailyData>();
		while(tempQueue.size()<data.size()+1){
			yesterdayQueue.add(tempQueue.poll());
		}
		today = new CMF(data, offset);
		yesterday = new CMF(yesterdayQueue, offset);
		ema = new ExponentialMovingAverage(data, offset);
	}

	@Override
	public String resultString() {		
		return "";
	}

	@Override
	public double value() {
		return 0;
	}

	@Override
	public Curve[] getGraph() {
		return new Curve[0];
	}

	@Override
	public Result getResult() {
		return new Result("CMFEMA", value(), resultString(), getGraph(), getSignal());
	}

	@Override
	public Signal getSignal() {
		Double emaValue = ema.value();

		Signal signal;
		if(emaValue > 0 && (yesterday.value() > 0 && today.value() < 0)) {
			signal = Signal.SELL;
		} else if(emaValue < 0 && (yesterday.value() < 0 && today.value() > 0 )) {
			signal = Signal.BUY;
		} else {
			signal = Signal.NONE;
		}
		return signal;
	}
}