package analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import data.DailyData;
import data.SimpleData;

public class MACD {
	
	
	
	private PriorityQueue<SimpleData> MACDQueue;
	private PriorityQueue<SimpleData> signalList;
	
	
	public MACD(PriorityQueue<DailyData> queue, int first, int second, int signal){
		List<? extends SimpleData> firstEMA = new LinkedList<SimpleData>(MovingAverage.getEMA(queue, first));
		List<? extends SimpleData> secondEMA = new LinkedList<SimpleData>(MovingAverage.getEMA(queue, second));
		
		System.out.println(firstEMA);
		System.out.println(secondEMA);
		
		MACDQueue = createMACDList(firstEMA, secondEMA);
		
		signalList = MovingAverage.getEMA(MACDQueue, signal);
		
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
		PriorityQueue<SimpleData> temp = new PriorityQueue<SimpleData>(MACDQueue);
		return temp;
	}
	
	public PriorityQueue<SimpleData> getSignal(){
		return signalList;
	}
}