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
	
	private ExponentialMovingAverage ema;
	private CMF cmf;
	
	public CMFEMA(PriorityQueue<DailyData> data, int offsetCMF, int offsetEMA) {
		
		cmf = new CMF(data, offsetCMF);
		ema = new ExponentialMovingAverage(data, offsetEMA);
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
		if(emaValue < 0 && cmf.getSignal() == Signal.SELL) {
			signal = Signal.SELL;
		} else if(emaValue > 0 && cmf.getSignal() == Signal.BUY) {
			signal = Signal.BUY;
		} else {
			signal = Signal.NONE;
		}
		return signal;
	}
}