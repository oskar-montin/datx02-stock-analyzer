package analyzer;

import java.util.LinkedList;
import java.util.PriorityQueue;

import controller.DatabaseHandler;

import data.Curve;
import data.DailyData;
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
	 *  
	 * @param priority queue with dailyData.
	 * @param periodtime for CMF calc.
	 * @return linked list with CMF values.
	 */
	
	private void CMFCalc(PriorityQueue<DailyData> dailyQueue, int offset){
		dailyData = new LinkedList<SimpleData>();
		DailyData [] dataArray = new DailyData[dailyQueue.size()];
		
		dataArray = dailyQueue.toArray(dataArray);
		
		
		for(int i = offset; i<dataArray.length; i++){
			
			DailyData [] periodSet = new DailyData [offset+1];
			for(int j = 0; j<=offset; j++){
//				System.out.println("J:"+j+" i-offset+j="+(i-offset+j)+" Size:"+dataArray.length);
				periodSet[j] = dataArray[i-offset+j];
			}
			
			double CMF = getCMF(periodSet);
			dailyData.add(new SimpleData(stock, dataArray[i].getDate(), CMF));
		}
			
	}
	
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


//-----------------		TEST	--------------------------
	
	public static void main(String[] args) {
		CMF cmf = new CMF(DatabaseHandler.getStock("KO"), DatabaseHandler.getDailyData(DatabaseHandler.getStock("KO")), 6);
		LinkedList<SimpleData> cmfList = cmf.getCmfList();
		for (SimpleData cmfPrint:cmfList){
			System.out.println(cmfPrint.getValue());
		}

	}

	private LinkedList<SimpleData> getCmfList() {
		return dailyData;
	}


	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Curve[] getGraph() {
		// TODO Auto-generated method stub
		return null;
	}

}
