package analyzer;


import java.util.LinkedList;
import java.util.PriorityQueue;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import data.SimpleData;
import data.Stock;
/**
 * Class calculates Chaikin Money Flow values
 * 
 * @author Tom Schön
 */

public class CMF implements AnalysisMethod{

	private PriorityQueue<DailyData> dailyQueue;
	private PriorityQueue<SimpleData> dailyData;
	private double cmfValue;
	private double current=0;
	private Stock stock;
	
	
	/**
	 *  
	 * @param stock
	 * @param offset
	 */
	
	public CMF(Stock stock, PriorityQueue<DailyData> dailyData, int offset) {
		
		dailyQueue = new PriorityQueue<DailyData>(dailyData);
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
			
			multiplier = ((closeValue-lowValue)-(highValue-closeValue))/(highValue-lowValue);
			MFV = (volume*multiplier);
			if(!Double.isInfinite(multiplier) && !Double.isNaN(MFV)) { // In case of infinite values (for erroneous daily data) 
				sumMFV += MFV;
				sumVol += volume;
			}
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
		dailyData = new PriorityQueue<SimpleData>();
		LinkedList<DailyData> dataList = new LinkedList<DailyData>(dailyQueue);
		double CMF = 0;
		for(int i = offset; i<dataList.size(); i++){
			
			DailyData [] periodSet = new DailyData [offset+1];
			for(int j = 0; j<=offset; j++){
				periodSet[j] = dataList.get(i-offset+j);
			}
			
			CMF = getCMF(periodSet);			
			dailyData.add(new SimpleData(stock, dataList.get(i).getDate(), CMF));
		}
		current=CMF;
	}	
	
	@Override
	public double value() {
		return current;
	}

	@Override
	public Curve[] getGraph() {
		Curve [] CMFCurve = {new Curve(dailyData, "Chaikin money values")};
		return CMFCurve;
	}

	@Override
	public String resultString() {
		return"CMF measures buying and selling pressure over a set period of time.";
	}

	@Override
	public Result getResult() {
		Double value = this.value();
		return new Result("Chaikin Money Flow", value, this.resultString(), this.getGraph(), getSignal());
	}


	@Override
	public Signal getSignal() {
		Double value = this.value();

		Signal signal;
		if(value < 0) {
			signal = Signal.SELL;
		} else if(value > 0) {
			signal = Signal.BUY;
		} else {
			signal = Signal.NONE;
		}
		return signal;
	}
}
