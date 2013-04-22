package analyzer;

import java.util.PriorityQueue;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import data.SimpleData;
import data.Stock;

public class StochRSI extends RelativeStrengthIndex{
	private PriorityQueue<SimpleData> stochRSI;
	private int offset;
	private double todaysValue;
	public StochRSI(Stock stock, PriorityQueue<? extends SimpleData> dailyData, int offset) {
		super(stock, dailyData, offset);
		stochRSI = new PriorityQueue<SimpleData>();
		this.offset = offset;
		doStochastics();
	}
	
	private void doStochastics() {
		SimpleData[] historicalRSI = new SimpleData[this.RSI.size()];
		double value = 0;
		historicalRSI = this.RSI.toArray(historicalRSI);
		for(int i = this.offset; i<historicalRSI.length;i++) {
			double min = Double.MAX_VALUE,max = Double.MIN_VALUE;
			for(int j = i-this.offset;j<=i;j++) {
				min = Math.min(min, historicalRSI[j].getValue());
				max = Math.max(max, historicalRSI[j].getValue());
			}
			value = (historicalRSI[i].getValue()-min)/(max-min);
			this.stochRSI.add(new SimpleData(this.stock, 
					historicalRSI[i].getDate(), 
					value));
		}	
		this.todaysValue = value;
	}

	/**
	 * Return stochRSI-valuelist
	 * 
	 * @return Linkedlist of stochRSI-values oldest first.
	 */
	
	public Curve[] getGraph() {

		PriorityQueue<SimpleData> topLine = new PriorityQueue<SimpleData>();
		PriorityQueue<SimpleData> bottLine = new PriorityQueue<SimpleData>();
		Curve [] RSICurve = new Curve[3];
			
		RSICurve[0] = new Curve(stochRSI, "Relative strength index values") ;
		
		for(SimpleData d:this.stochRSI){
			topLine.add(new SimpleData(d.getStock(), d.getDate(),0.7));
			bottLine.add(new SimpleData(d.getStock(), d.getDate(),0.3));
		}
		RSICurve[1]=new Curve(topLine, "Limit for overbought");
		RSICurve[2]=new Curve(bottLine, "Limit for oversold");
		
		return RSICurve;
	}
	
	/**
	 * 
	 * @return a string that specified what the method indicates in words.
	 */
	public String resultString(){
		return"stochRSI indicates if the stock is overbought or oversold. The mothod looks att price movement.";
	}
	
	public Signal getSignal() {
		Signal signal;
		if(todaysValue==1.0) {
			signal = Signal.BUY;
		} else if(todaysValue==0.0) {
			signal = Signal.SELL;
		} else {
			signal = Signal.NONE;
		}
		return signal;
	}


	@Override
	public Result getResult() {
		
		return new Result("StochRSI", this.todaysValue, this.resultString(), this.getGraph(), getSignal());
	}
}
