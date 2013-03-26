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
	private LinkedList<SimpleData> signalList;
	
	
	public MACD(PriorityQueue<DailyData> queue, int first, int second, int signal){
		
		MACDQueue = createMACDList(new ExponentialMovingAverage(queue, first).getMovingAverage(),
								  new ExponentialMovingAverage(queue, second).getMovingAverage());
		
		signalList = new ExponentialMovingAverage(MACDQueue, signal).getMovingAverage();
		
		System.out.println(MACDQueue);
	}
	
	private PriorityQueue<SimpleData> createMACDList(List<? extends SimpleData> first, List<? extends SimpleData> second){
		Collections.reverse(first);
		Collections.reverse(second);
		
		PriorityQueue<SimpleData> compiledMACD = new PriorityQueue<SimpleData>();
		
		ArrayList<Double> valueArray = new ArrayList<Double>();
		
		System.out.println("first: " + first);
		
		System.out.println("second: " + second);
		
		for(int i = second.size()-1; i >= 0; i--){
			compiledMACD.add(new SimpleData(second.get(i).getStock(), 				//Add the right stock
							second.get(i).getDate(), 								//Add the right date
							first.get(i).getValue()-second.get(i).getValue()));		//Add the right value
		}
		
		
		return compiledMACD;
	}
	
	public LinkedList<SimpleData> getMACD(){
		LinkedList<SimpleData> temp = new LinkedList<SimpleData>(MACDQueue);
		return temp;
	}
	
	public LinkedList<SimpleData> getSignal(){
		return signalList;
	}
}
