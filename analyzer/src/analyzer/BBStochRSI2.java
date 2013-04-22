package analyzer;

import java.util.PriorityQueue;

import data.Result;
import data.Signal;
import data.SimpleData;

/**
 * This Combinationmethod is a simple combination of Volatilitybands and stochastics rsi. 
 * If the signals of volatilitybands and stochrsi are the same, send that signal.
 * @author Guribur
 *
 */
public class BBStochRSI2 extends BBRSI {

	public BBStochRSI2(PriorityQueue<? extends SimpleData> data, int offset) {
		super(data, offset);
	}
	
	@Override
	public Signal getSignal() {
		if(vb.getSignal() == stochRSI.getSignal())
			return vb.getSignal();
		else
			return Signal.NONE;
	}

	@Override
	public Result getResult() {
		return new Result("BollingerStochRSI2", value(), resultString(), getGraph(), getSignal());
	}

}
