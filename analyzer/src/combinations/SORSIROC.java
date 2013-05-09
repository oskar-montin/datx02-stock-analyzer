package combinations;

/**
 * 
 * @author Axner
 */

import java.util.Collection;
import java.util.PriorityQueue;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import analyzer.AnalysisMethod;
import analyzer.RateOfChange;
import analyzer.RelativeStrengthIndex;
import analyzer.StochasticOscillator;

public class SORSIROC implements AnalysisMethod {
	private final RelativeStrengthIndex rsi;
	private final StochasticOscillator so;
	private final RateOfChange roc;
	
	
	public SORSIROC (Collection<DailyData> dailyData) {
		
		PriorityQueue<DailyData> data = new PriorityQueue<DailyData>(dailyData); 
		rsi = new RelativeStrengthIndex(data, 21);
		so = new StochasticOscillator(data, 5, 9, 14, 1);
		roc = new RateOfChange(data, 20);
	}
	
	
	@Override
	public String resultString() {
		return "RSI = " + rsi.value() + "\n" + 
				"SO = " + so.value() + "\n" +
				"ROC = " + roc.getSignal() + "\n";
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
		return new Result("RSISOCMFROC", value(), resultString(), getGraph(), getSignal());
	}
	@Override
	public Signal getSignal() {
	
		return calculateSignals();
	} 
	
	private Signal calculateSignals() {
		
		Signal rocSignal = roc.getSignal();
		double soValue = so.value();
		double rsiValue = rsi.value();
		
			if(rocSignal == Signal.SELL){
				if(soValue >= 50 && rsiValue >= 50){
					return Signal.SELL;
				}
				else{
					return Signal.NONE;
				}
			}
			else if (rocSignal == Signal.BUY){
				if(soValue <= 50 && rsiValue <= 50){
					return Signal.BUY;
				}
				else{
					return Signal.NONE;
				}
			}
			return Signal.NONE;
	}
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}
}
