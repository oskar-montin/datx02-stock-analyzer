package analyzer;

import java.util.PriorityQueue;

import controller.DatabaseHandler;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import data.SimpleData;
import data.Stock;

public class BBRSI implements AnalysisMethod {
	
	private final BollingerBands bb;
	private final RelativeStrengthIndex rsi;
	private final StochRSI stochRSI;
	private Signal[] signals;
	
	public BBRSI(PriorityQueue<? extends SimpleData> data, int offset) {
		bb = new BollingerBands(data, offset);
		rsi = new RelativeStrengthIndex(data.peek().getStock(), data, offset);
		stochRSI = new StochRSI(data.peek().getStock(), data, offset);
		signals = new Signal[5];
		calculateSignals();
	}
	
	private void calculateSignals() {
		signals[0] = bb.getResult().getSignal();
		signals[1] = rsi.getResult().getSignal();
		signals[2] = stochRSI.getResult().getSignal();
		if(signals[0] == signals[1]) {
			signals[3] = signals[0];
		} else {
			signals[3] = Signal.NONE;
		}
		if(signals[0] == signals[2]) {
			signals[4] = signals[0];
		}else {
			signals[4] = Signal.NONE;
		}
		
	}

	@Override
	public String resultString() {
		return "BB = " + signals[0].getString() + "\n" +
			   "RSI = " + signals[1].getString() + "\n" + 
			   "StochRSI = "+ signals[2].getString()+"\n" +
			   "BB+RSI = " + signals[3].getString()+"\n"+
			   "BB+StochRSI = " + signals[4].getString();
	}

	@Override
	public double value() {
		double value = 0.0;
		for(Signal s:signals) {
			if(s!=Signal.NONE) {
				value += s==Signal.BUY?1:0;
			}
		}
		return value;
	}

	@Override
	public Curve[] getGraph() {
		return new Curve[0];
	}

	@Override
	public Result getResult() {
		return new Result("BollingerRSI", value(), resultString(), getGraph(), Signal.NONE);
	}
	
	public static void main(String[] args) {
		Stock stock = DatabaseHandler.getStock("GOOG");
		PriorityQueue<DailyData> data = DatabaseHandler.getDailyData(stock);
		BBRSI b = new BBRSI(data, 10);
		System.out.println(b.getResult().getReturnString());
	}
	
}
