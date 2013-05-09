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
import analyzer.CMF;
import analyzer.RateOfChange;
import analyzer.RelativeStrengthIndex;
import analyzer.StochasticOscillator;

public class RSISOCMFROC implements AnalysisMethod {
	private final RelativeStrengthIndex rsi;
	private final CMF cmf;
	private final StochasticOscillator so;
	private final RateOfChange roc;
	
	
	public RSISOCMFROC (Collection<DailyData> dailyData) {
		
		PriorityQueue<DailyData> data = new PriorityQueue<DailyData>(dailyData); 
		rsi = new RelativeStrengthIndex(data, 21);
		cmf = new CMF(data, 21);
		so = new StochasticOscillator(data, 5, 9, 14, 1);
		roc = new RateOfChange(data, 20);
	}
	
	
	@Override
	public String resultString() {
		return "RSI = " + rsi.value() + "\n" +
				"CMF = " + cmf.getSignal() + "\n" + 
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
		
		Signal cmfSignal = cmf.getSignal();
		Signal rocSignal = roc.getSignal();
		double soValue = so.value();
		double rsiValue = rsi.value();
		
		if(cmfSignal == rocSignal){
			if(rocSignal == Signal.SELL){
				if(soValue <= 30 && rsiValue <= 30){
					return Signal.NONE;
				}
				else{
					return Signal.SELL;
				}
			}
			else if(rocSignal == Signal.NONE) {
				if(soValue <= 30 && rsiValue <= 30){
					return Signal.BUY;
				}
				else if(soValue >= 70 && rsiValue >= 70){
					return Signal.SELL;
				}
				else{
					return Signal.NONE;
				}
			}
			else{
				if(soValue <= 70 && rsiValue <= 70){
					return Signal.BUY;
				}
				else{
					return Signal.NONE;
				}
			}
		}
		else if (soValue <= 30 && rsiValue <= 30){
			if (cmfSignal == Signal.BUY || rocSignal == Signal.BUY){
				return Signal.BUY;
			}
			else if (soValue <= 15 || rsiValue <= 15){
				if(!(cmfSignal == Signal.SELL || rocSignal == Signal.SELL)){
					return Signal.BUY;
				}
			}
			else{
				return Signal.NONE;
			}
		}
		else if (soValue >= 70 && rsiValue >= 70){
			if (cmfSignal == Signal.SELL || rocSignal == Signal.SELL){
				return Signal.SELL;
			}
			else if (soValue >= 85 || rsiValue >= 85){
				if(!(cmfSignal == Signal.BUY || rocSignal == Signal.BUY)){
					return Signal.SELL;
				}
			}
			else {
				return Signal.NONE;
			}
		}
		return Signal.NONE;
	}
}
