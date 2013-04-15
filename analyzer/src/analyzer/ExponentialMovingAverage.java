package analyzer;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import data.SimpleData;
import data.Stock;

/**
 * A class that represents an exponential moving average.
 * 
 * 
 * 
 * @author oskarnylen
 */
public class ExponentialMovingAverage implements AnalysisMethod{

	PriorityQueue<SimpleData> movingAverageQueue = new PriorityQueue<SimpleData>();
	PriorityQueue<SimpleData> priceQueue;

	/**
	 * 
	 * @param stock
	 * @param offset
	 */
	public ExponentialMovingAverage(PriorityQueue<? extends SimpleData> dataQueue, int offset){
		priceQueue = new PriorityQueue<SimpleData>(dataQueue);
		LinkedList<SimpleData> dataList = new LinkedList<SimpleData>(dataQueue);
		

		/*
		 * Make sure that the dailyDataList has the same number of entries (with the same dates) as movingAverageList
		 */
		for(int i = offset-1; i > 0; i--){
			dataList.removeFirst();
		}
		
		/*
		 * Make sure that the first EMA is the same as the SMA for the same period
		 */
		double yesterdayEMA = SimpleMovingAverage.getSMA(dataQueue, offset, offset).getValue();

		
		LinkedList<Double> priceList = new LinkedList<Double>();
		
		for(int i = 0; i < dataList.size(); i++){
			if(i == 0){
				priceList.add(yesterdayEMA);
			} else {
				
				SimpleData sd = dataList.get(i);
				//call the EMA calculation
				double EMA = calculateEMA(sd.getValue(), offset, yesterdayEMA);
				//put the calculated EMA in a list
				priceList.add(EMA);
				//make sure yesterdayEMA gets filled with the EMA we used this time around
				yesterdayEMA = EMA;
			}
		}
		
		/*
		 * Insert the calculated values into the movingAverageList
		 */
		for(int i = 0; i < priceList.size(); i++){
			movingAverageQueue.add(new SimpleData(dataList.get(i).getStock(), dataList.get(i).getDate(),
					priceList.get(i)));
		}
	}

	private double calculateEMA(double todaysPrice, double numberOfDays, double EMAYesterday){
		double k = 2 / (numberOfDays + 1);
		return ((todaysPrice*k) + (EMAYesterday*(1-k)));
	}

	public PriorityQueue<SimpleData> getMovingAverage(){
		return movingAverageQueue;
	}

	@Override
	public String resultString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double value() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Curve[] getGraph() {
		Curve[] curves = new Curve[2];
		curves[0] = new Curve(this.priceQueue, "Index");
		curves[1] = new Curve(this.getMovingAverage(), "EMA");
		return curves;
	}

	@Override
	public Result getResult() {
		Double value = this.value();
		Signal signal = Signal.NONE;
		
		return new Result("EMA", value, this.resultString(), this.getGraph(), signal);
	}
}