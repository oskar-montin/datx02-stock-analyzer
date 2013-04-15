package analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import data.SimpleData;

/**
 * Class that represent a MACD-object containing a MACD-line, Signal-line, Histogram and a MACD-line filtered by the 
 * histogram.
 * 
 * @author Oskar
 */
public class MACD implements AnalysisMethod {

	private PriorityQueue<SimpleData> MACDQueue;
	private PriorityQueue<SimpleData> signalQueue;
	private PriorityQueue<SimpleData> histogramQueue;
	private PriorityQueue<SimpleData> MACDFilteredQueue;

	private boolean filterOn;

	/**
	 * 
	 * @param queue - the timeserie
	 * @param first - the offset of the fast EMA
	 * @param second - the offset of the slow EMA
	 * @param signal - the offset of the signal-line
	 */
	public MACD(PriorityQueue<DailyData> queue, int first, int second, int signal){
		List<? extends SimpleData> firstEMA = new LinkedList<SimpleData>(MovingAverage.getEMA(queue, first));
		List<? extends SimpleData> secondEMA = new LinkedList<SimpleData>(MovingAverage.getEMA(queue, second));

		MACDQueue = createCompiledQueue(firstEMA, secondEMA, true);

		signalQueue = MovingAverage.getEMA(MACDQueue, signal);

		List<? extends SimpleData> MACDList = new LinkedList<SimpleData>(MACDQueue);
		List<? extends SimpleData> signalList = new LinkedList<SimpleData>(signalQueue);

		histogramQueue = createCompiledQueue(MACDList, signalList, true);

		List<? extends SimpleData> histogramList = new LinkedList<SimpleData>(histogramQueue);

		MACDFilteredQueue = createCompiledQueue(MACDList, histogramList, false);
	}

	private PriorityQueue<SimpleData> createCompiledQueue(List<? extends SimpleData> first, List<? extends SimpleData> second, boolean subtract){
		Collections.reverse(first);
		Collections.reverse(second);

		PriorityQueue<SimpleData> compiledQueue = new PriorityQueue<SimpleData>();

		if(subtract == true){
			for(int i = second.size()-1; i >= 0; i--){
				double value = first.get(i).getValue()-second.get(i).getValue();	//SUBTRACTION
				compiledQueue.add(new SimpleData(second.get(i).getStock(), 				//Add the right stock
						second.get(i).getDate(), 								//Add the right date
						value));												//Add the right value
			}
		}else{
			for(int i = second.size()-1; i >= 0; i--){
				double value = first.get(i).getValue()+second.get(i).getValue();	//ADDITION
				compiledQueue.add(new SimpleData(second.get(i).getStock(), 				//Add the right stock
						second.get(i).getDate(), 								//Add the right date
						value));												//Add the right value
			}
		}
		return compiledQueue;
	}

	private PriorityQueue<SimpleData> createPlusList(List<? extends SimpleData> first, List<? extends SimpleData> second){
		Collections.reverse(first);
		Collections.reverse(second);

		PriorityQueue<SimpleData> compiledMACD = new PriorityQueue<SimpleData>();

		ArrayList<Double> valueArray = new ArrayList<Double>();

		for(int i = second.size()-1; i >= 0; i--){
			double value = first.get(i).getValue()+second.get(i).getValue();
			compiledMACD.add(new SimpleData(second.get(i).getStock(), 				//Add the right stock
					second.get(i).getDate(), 								//Add the right date
					value));												//Add the right value
		}

		return compiledMACD;
	}

	/**
	 * Sets if the returnValue should return a filtered MACD
	 * 
	 * @param flag - true if the filter should be ON, and false if the filter should be OFF.
	 */
	public void setFilter(boolean flag){
		if(flag == true){
			filterOn = true;
		}else{
			filterOn = false;
		}
	}

	public PriorityQueue<SimpleData> getMACD(){
		return MACDQueue;
	}

	public PriorityQueue<SimpleData> getSignal(){
		return signalQueue;
	}

	public PriorityQueue<SimpleData> getHistogram(){
		return histogramQueue;
	}

	@Override
	public String resultString() {
		// TODO Auto-generated method stub
		return "MACD";
	}

	/**
	 * @return Returns 0 if the MACD can't say anything. 100 equals a buy-signal and -100 equals a sell-signal.
	 */
	@Override
	public double value() {
		double returnValue = 0;
		if(filterOn == false){
			PriorityQueue<SimpleData> tempMACD = new PriorityQueue<SimpleData>(MACDQueue);

			double today = tempMACD.poll().getValue();
			double yesterday = tempMACD.poll().getValue();

			if(yesterday < 0 && today > 0){
				returnValue = 100;
			}
			if(yesterday > 0 && today < 0){
				returnValue = -100;
			}

		} else {
			PriorityQueue<SimpleData> tempFilteredMACD = new PriorityQueue<SimpleData>(MACDFilteredQueue);
			double todayFiltered = tempFilteredMACD.poll().getValue();
			double yesterdayFiltered = tempFilteredMACD.poll().getValue();

			if(yesterdayFiltered < 0 && todayFiltered > 0){
				returnValue = 100;
			}
			if(yesterdayFiltered > 0 && todayFiltered < 0){
				returnValue = -100;
			}
		}
		return returnValue;
	}

	/**
	 * @return Returns all the curves a MACD-object contains. That is a MACD-line, Signal-line, MACD-histogram and a filtered MACD-line.
	 */
	@Override
	public Curve[] getGraph() {
		Curve[] curves = new Curve[4];
		curves[0] = new Curve(MACDQueue, "MACD-line");
		curves[1] = new Curve(signalQueue, "Signal-line");
		curves[2] = new Curve(histogramQueue, "MACD-histogram");
		curves[3] = new Curve(MACDFilteredQueue, "MACD-filtered");
		return curves;
	}

	@Override
	public Result getResult() {
		Double value = this.value();
		Signal signal;
		if(value < 0) {
			signal = Signal.SELL;
		} else if(value > 0) {
			signal = Signal.BUY;
		} else {
			signal = Signal.NONE;
		}
		return new Result("MACD", value, this.resultString(), this.getGraph(), signal);
	}
}