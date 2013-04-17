package analyzer;

import java.util.PriorityQueue;
import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import data.SimpleData;
import data.Stock;

/**
 * A class that calculates RSI-values.
 * 
 * @author Runa Gulliksson
 */

public class RelativeStrengthIndex implements AnalysisMethod {

	private PriorityQueue<DailyData> dailyDataQueue;
	private PriorityQueue<SimpleData> RSI; // oldest first
	private double avgGain, avgLoss;
	private Stock stock;
	private int last=0;
	

	/**
	 * Constructor that collects data for a specific stock and calculates RSI
	 * values for the given priodlength. 
	 * 
	 * @param stock
	 * @param offset - The number of days used for the RSI.
	 */
	public RelativeStrengthIndex(Stock stock, PriorityQueue<DailyData> dailyData, int offset) {
		
		dailyDataQueue = new PriorityQueue<DailyData>(dailyData);
		this.stock = stock;
		RSI = new PriorityQueue<SimpleData>();
		this.CalculateRSI(dailyDataQueue, offset);
		
	}


	/**
	 * Return stock connected with this instance.
	 * 
	 * @return symbol for stock.
	 */
	public Stock getStock(){
		return stock;
	}

	/**
	 * Calculates RSI values.
	 * 
	 * @param PriorityQueue with dailyData.
	 * @param Number of periods for RSI calculation
	 * @return LinkedList with all RSI-values, oldest first.
	 */
	private void CalculateRSI(PriorityQueue<DailyData> dailyDataQueue, int offset){

		double sumGain = 0, sumLoss = 0, diff;
		DailyData now = null, priv;
		
		if (dailyDataQueue.size()>=offset+1){
			
			priv=dailyDataQueue.poll();
			
			for(int i=0; i<offset; i++){
				
				now=dailyDataQueue.poll();
				diff=now.getClosePrice()-priv.getClosePrice();
				
				if(diff>0){
					sumGain = sumGain + diff;
					
				}
				else sumLoss = sumLoss - diff;
				
				priv=now;
			}
			
			avgGain = sumGain/offset;
			avgLoss = sumLoss/offset;
			RSI.add(new SimpleData(stock, now.getDate(), (100-100/(1+avgGain/avgLoss))));
			
			while(!dailyDataQueue.isEmpty()){
				
				now=dailyDataQueue.poll();
				diff=now.getClosePrice()-priv.getClosePrice();
				
				if(diff>0){
					avgGain=(avgGain*(offset-1)+diff)/offset;
					avgLoss=(avgLoss*(offset-1))/offset;
				}
				else{
					avgGain=(avgGain*(offset-1))/offset;
					avgLoss=(avgLoss*(offset-1)-diff)/offset;
					
				}
				
				RSI.add(new SimpleData(stock, now.getDate(), (100-100/(1+avgGain/avgLoss))));
				priv=now;
				last=(int)RSI.peek().getValue();
			}

		}
		
		else System.out.println("error in RSI: to large period.");
		
		
	}
	

	@Override
	public double value() {
		return last;
	}

	
	/**
	 * Return RSI-valuelist
	 * 
	 * @return Linkedlist of RSI-values oldest first.
	 */
	
	public Curve[] getGraph() {
		Curve [] RSICurve = {new Curve(RSI, "Relative strength index values in percent")};
		return RSICurve;
	}
	
	/**
	 * 
	 * @return a string that specified what the method indicates in words.
	 */
	public String resultString(){
		return"RSI indicates if the stock is overbought or oversold. The mothod looks att price movement.";
	}


	@Override
	public Result getResult() {
		Double value = this.value();
		Signal signal;
		if(value>80) {
			signal = Signal.BUY;
		} else if(value<20) {
			signal = Signal.SELL;
		} else {
			signal = Signal.NONE;
		}
		return new Result("Relative Strength Index", value, this.resultString(), this.getGraph(), signal);
	}

}
