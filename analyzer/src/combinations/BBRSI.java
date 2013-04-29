package combinations;

import java.util.ArrayList;
import java.util.PriorityQueue;

import analyzer.AnalysisMethod;
import analyzer.BollingerBands;
import analyzer.RelativeStrengthIndex;
import analyzer.StochRSI;
import analyzer.VolatilityBands;

import util.Util;

import controller.DatabaseHandler;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import data.SimpleData;
import data.Stock;

/**
 * This Combinationmethod is a simple combination of Volatilitybands and standard rsi. 
 * If the signals of volatilitybands and rsi are the same, send that signal.
 * @author Guribur
 *
 */
public class BBRSI implements AnalysisMethod {
	
	protected DailyData[] dailyData;
	protected final BollingerBands bb;
	protected final VolatilityBands vb;
	protected final RelativeStrengthIndex rsi;
	protected final StochRSI stochRSI;
	protected Signal signal = Signal.NONE;
	
	public BBRSI(PriorityQueue<? extends SimpleData> data, int offset) {
		dailyData = new DailyData[data.size()];
		dailyData = data.toArray(dailyData);
		vb = new VolatilityBands(data, offset);
		bb = new BollingerBands(data, offset);
		rsi = new RelativeStrengthIndex(data, 14);
		stochRSI = new StochRSI(data, 14);
		
	}
	
	

	@Override
	public String resultString() {
		return "";
	}

	@Override
	public double value() {
		if(getSignal() == Signal.NONE) {
			return 0;
		}
		return getSignal()==Signal.BUY?1:-1;
	}

	@Override
	public Curve[] getGraph() {
		return new Curve[0];
	}
	
	public Signal getSignal() {
		if(vb.getSignal() == rsi.getSignal())
			return vb.getSignal();
		else
			return Signal.NONE;
	}

	@Override
	public Result getResult() {
		return new Result("BollingerRSI", value(), resultString(), getGraph(), getSignal());
	}
	
	public static void main(String[] args) {
		
	}
	
}
