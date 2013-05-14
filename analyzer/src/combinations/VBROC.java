package combinations;

import java.util.PriorityQueue;

import analyzer.AnalysisMethod;
import analyzer.RateOfChange;
import analyzer.VolatilityBands;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import data.SimpleData;

public class VBROC implements AnalysisMethod {

	protected DailyData[] dailyData;
	protected final VolatilityBands vb;
	protected final RateOfChange roc;
	protected Signal signal = Signal.NONE;
	private int vbOffset; 
	private int rocOffset;
	
	public VBROC(PriorityQueue<? extends SimpleData> data, int vbOffset, int rocOffset) {
		dailyData = new DailyData[data.size()];
		dailyData = data.toArray(dailyData);
		vb = new VolatilityBands(data, vbOffset);
		roc = new RateOfChange(data, rocOffset);
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
		if(vb.getSignal() == roc.getSignal())
			return vb.getSignal();
		else
			return Signal.NONE;
	}

	@Override
	public Result getResult() {
		return new Result("VolatilityROC", value(), resultString(), getGraph(), getSignal());
	}
	
	@Override
	public String getName() {
		return this.getClass().getName()+" VBOffset: "+this.vbOffset+" ROCOffset: "+this.rocOffset;
	}

}
