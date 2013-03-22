package analyzer;

import java.util.LinkedList;
import java.util.PriorityQueue;

import data.DailyData;
import data.SimpleData;
import data.Stock;

/**
 * A class that calculates RSI-values.
 * 
 * @author Runa Gulliksson
 */

public class RelativeStrengthIndex {

	private PriorityQueue<DailyData> dailyDataQueue;
	private LinkedList<SimpleData> RSI; // oldest first
	private double avgGain, avgLoss;
	private Stock stock;
	

	/**
	 * Constructor that collects data for a specific stock and calculates RSI
	 * values for the given priodlength. 
	 * 
	 * @param stock
	 * @param offset - The number of days used for the RSI.
	 */
	public RelativeStrengthIndex(Stock stock, PriorityQueue<DailyData> dailyData, int offset) {
		
		dailyDataQueue = dailyData;
		this.stock = stock;
		this.CalculateRSI(dailyDataQueue, offset);
		
	}
	
	/**
	 * Return RSI-valuelist
	 * 
	 * @return Linkedlist of RSI-values oldest first.
	 */
	
	public LinkedList<SimpleData> getRSI(){
		return RSI;
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
		RSI = new LinkedList<SimpleData>();
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
			}

		}
		
		else System.out.println("error in RSI: to large period.");
		
		
	}
	
	/**
	 * Add day to RSI. /// migth not be needed
	 * 
	 * @return symbol for stock.
	 */
	private void addDay(double avgGain, double avgLoss, DailyData old, DailyData newDay ){
	
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		-----For testing-----
		RelativeStrengthIndex rsi = new RelativeStrengthIndex(DatabaseHandler.getStock("AAPL"), DatabaseHandler.getDailyData(DatabaseHandler.getStock("AAPL")) , 4);
		LinkedList<SimpleData> rsList = rsi.getRSI();
		while (!rsList.isEmpty()){
			System.out.println(rsList.removeLast().getValue());
		}
	}


}
