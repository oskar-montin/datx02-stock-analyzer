package analyzer;

import java.util.LinkedList;
import java.util.PriorityQueue;

import data.DailyData;
import data.Stock;

/**
 * A class that calculates RSI-values.
 * 
 * @author Runa Gulliksson
 */

public class RelativeStrengthIndex {

	private PriorityQueue<DailyData> dailyDataQueue;
	private LinkedList<Double> RSI; // oldest first
	private double avgGain, avgLoss;
	private String stock;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		-----For testing-----
//		RelativeStrengthIndex rsi = new RelativeStrengthIndex(DatabaseHandler.getStock("AAPL"), 2 );
//		LinkedList<Double> rsList = rsi.getRSI();
//		while (!rsList.isEmpty()){
//			System.out.println(rsList.removeLast());
//		}
	}

	/**
	 * Constructor that collects data for a specific stock and calculates RSI
	 * values for the given priodlength. 
	 * 
	 * @param stock
	 * @param offset - The number of days used for the RSI.
	 */
	public RelativeStrengthIndex(Stock stock, int offset) {
		
		dailyDataQueue = DatabaseHandler.getDailyData(stock);
		RSI = new LinkedList<Double>();
		this.stock = stock.getSymbol();
		double sumGain = 0, sumLoss = 0, diff;
		DailyData now, priv;
		
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
			RSI.add(100-100/(1+avgGain/avgLoss));
			
			while(!dailyDataQueue.isEmpty()){
				
				now=dailyDataQueue.poll();
				diff=now.getClosePrice()-priv.getClosePrice();
				
				if(diff>0){
					avgGain=(avgGain*13+diff)/offset;
					avgLoss=(avgLoss*13)/offset;
				}
				else{
					avgGain=(avgGain*13)/offset;
					avgLoss=(avgLoss*13+diff)/offset;
					
				}

				RSI.add(100-100/(1+avgGain/avgLoss));

			}

		}
		
		else System.out.println("error in RSI: to large period.");
		
	}
	
	/**
	 * Return RSI-valuelist
	 * 
	 * @return Linkedlist of RSI-values oldest first.
	 */
	
	public LinkedList<Double> getRSI(){
		return RSI;
	}

	/**
	 * Return stock connected with this instance.
	 * 
	 * @return symbol for stock.
	 */
	public String getStock(){
		return stock;
	}

}
