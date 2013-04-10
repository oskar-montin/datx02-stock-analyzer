package analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import data.Curve;
import data.DailyData;
import data.SimpleData;

public class MACD implements AnalysisMethod {
	
	
	
	private PriorityQueue<SimpleData> MACDQueue;
	private PriorityQueue<SimpleData> signalQueue;
	private PriorityQueue<SimpleData> histogramQueue;
	
	
	public MACD(PriorityQueue<DailyData> queue, int first, int second, int signal){
		List<? extends SimpleData> firstEMA = new LinkedList<SimpleData>(MovingAverage.getEMA(queue, first));
		List<? extends SimpleData> secondEMA = new LinkedList<SimpleData>(MovingAverage.getEMA(queue, second));
		
//		System.out.println(firstEMA);
//		System.out.println(secondEMA);
		
		MACDQueue = createMACDList(firstEMA, secondEMA);
		
		signalQueue = MovingAverage.getEMA(MACDQueue, signal);
		
		List<? extends SimpleData> MACDList = new LinkedList<SimpleData>(MACDQueue);
		List<? extends SimpleData> signalList = new LinkedList<SimpleData>(signalQueue);
		
		histogramQueue = createMACDList(MACDList, signalList);
		
		System.out.println(histogramQueue);
		
//		System.out.println(MACDQueue);
	}
	
	private PriorityQueue<SimpleData> createMACDList(List<? extends SimpleData> first, List<? extends SimpleData> second){
		Collections.reverse(first);
		Collections.reverse(second);
		
		PriorityQueue<SimpleData> compiledMACD = new PriorityQueue<SimpleData>();
		
		ArrayList<Double> valueArray = new ArrayList<Double>();
		
//		System.out.println("first: " + first);
//		
//		System.out.println("second: " + second);
		
		for(int i = second.size()-1; i >= 0; i--){
			double value = first.get(i).getValue()-second.get(i).getValue();
			compiledMACD.add(new SimpleData(second.get(i).getStock(), 				//Add the right stock
							second.get(i).getDate(), 								//Add the right date
							value));												//Add the right value
		}
		
		
		return compiledMACD;
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
		return null;
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Curve[] getGraph() {
		Curve[] curves = new Curve[3];
		curves[0] = new Curve(MACDQueue, "MACD-line");
		curves[1] = new Curve(signalQueue, "Signal-line");
		curves[2] = new Curve(histogramQueue, "MACD-histogram");
		return curves;
	}
}