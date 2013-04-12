package analyzer;

import java.util.LinkedList;
import java.util.PriorityQueue;

import data.Curve;
import data.DailyData;
import data.Result;
import data.SimpleData;
import data.Stock;

/**
 * Class calculates Chaikin Money Flow values
 * 
 * @author Tom Schön
 */

public class CMF implements AnalysisMethod{

	private PriorityQueue<DailyData> dailyQueue;
	private LinkedList<SimpleData> dailyData;
	private double cmfValue;
	private Stock stock;
	
	/**
	 *  
	 * @param stock
	 * @param offset
	 */
	
	public CMF(Stock stock, PriorityQueue<DailyData> dailyData, int offset) {
		
		dailyQueue = dailyData;
		this.stock = stock;
		this.CMFCalc(dailyQueue, offset);
		
	}
	

	/**
	 * @return symbol of stock
	 */
	
	public Stock getStock(){
		return stock;
	}
	

	/**
	 * Calculates the Chaikin Money Flow value over the given period(offset).
	 * 
	 * @param periodSet
	 * @return cmfValue
	 */
	
	private double getCMF(DailyData[] periodSet) {
		double multiplier = 0, lowValue = 0, highValue = 0, closeValue = 0, MFV = 0, sumMFV = 0;
		long volume = 0, sumVol = 0;
		for (DailyData currentValues:periodSet){
			
			lowValue = currentValues.getLow();
			highValue = currentValues.getHigh();
			closeValue = currentValues.getClosePrice();
			volume = currentValues.getVolume();
			sumVol += volume;
			multiplier = ((closeValue-lowValue)-(highValue-closeValue))/(highValue-lowValue);
			MFV = (volume*multiplier);
			sumMFV += MFV;
		}
		cmfValue = (sumMFV/sumVol);
			return cmfValue;
		}

	/**
	 *  
	 * @param priority queue with dailyData.
	 * @param periodtime for CMF calculation.
	 * @return linked list with CMF values.
	 */
	
	private void CMFCalc(PriorityQueue<DailyData> dailyQueue, int offset){
		dailyData = new LinkedList<SimpleData>();
		DailyData [] dataArray = new DailyData[dailyQueue.size()];
		
		dataArray = dailyQueue.toArray(dataArray);
		
		
		for(int i = offset; i<dataArray.length; i++){
			
			DailyData [] periodSet = new DailyData [offset+1];
			for(int j = 0; j<=offset; j++){
				periodSet[j] = dataArray[i-offset+j];
			}
			
			double CMF = getCMF(periodSet);
			dailyData.add(new SimpleData(stock, dataArray[i].getDate(), CMF));
		}
	}		


	@Override
	public double value() { //Testing in progress, needs to be updated. Decide what format
		return 0;
	}


	@Override
	public Curve[] getGraph() {
		Curve [] CMFCurve = new Curve[1];
		PriorityQueue<SimpleData> cmfQueue= new PriorityQueue<SimpleData>();
		
		for (int i=0; i<dailyData.size(); i++)
			cmfQueue.add(dailyData.get(i));
		
		CMFCurve[0] = new Curve(cmfQueue, "Chaikin Money Flow values");
		return CMFCurve;
	}


	@Override
	public String resultString() {
		return"CMF measures buying and selling pressure over a set period (offset) of time.";
	}


	@Override
	public Result getResult() {
		return null;
	}

}
