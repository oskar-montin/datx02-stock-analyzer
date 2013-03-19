package analyzer;

import java.util.LinkedList;
import java.util.PriorityQueue;

import data.DailyData;
import data.Stock;

public class RelativeStrengthIndex {

	private PriorityQueue<DailyData> dailyDataQueue;
	private LinkedList<Double> RSI;
	private double avgGain, avgLoss;
	
	
	public RelativeStrengthIndex(Stock stock, int offset) {
		
		dailyDataQueue = DatabaseHandler.getDailyData(stock);
		RSI = new LinkedList<Double>();
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
				else sumLoss = sumLoss + diff;
				
				priv=now;
			}
			
			avgGain = sumGain/14;
			avgLoss = sumLoss/14;
			RSI.add(100-100/(1+avgGain/avgLoss));
			
			while(!dailyDataQueue.isEmpty()){
				
				now=dailyDataQueue.poll();
				diff=now.getClosePrice()-priv.getClosePrice();
				
				if(diff>0){
					avgGain=(avgGain*13+diff)/14;
					avgLoss=(avgLoss*13)/14;
				}
				else{
					avgGain=(avgGain*13)/14;
					avgLoss=(avgLoss*13+diff)/14;
					
				}

				RSI.add(100-100/(1+avgGain/avgLoss));
			}

		}
		
		else System.out.println("error in RSI: to large period.");
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
