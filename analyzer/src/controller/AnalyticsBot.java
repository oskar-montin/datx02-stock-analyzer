package controller;

import java.util.ArrayList;
import java.util.PriorityQueue;

import data.DailyData;
import data.QuarterlyData;
import data.SimpleData;

public class AnalyticsBot {
	private static int numberOfMethods = 10; //Change to actual amount of analysis methods
	private int numberOfStocks;
	private PriorityQueue<SimpleData>[][] boughtStocks;
	private PriorityQueue<SimpleData>[][] soldStocks;
	private PriorityQueue<SimpleData>[][] ownedStocks;
	
	public AnalyticsBot(PriorityQueue<QuarterlyData>[] quarterlyData) {
		numberOfStocks = quarterlyData[0].size();
		boughtStocks = ((PriorityQueue<SimpleData>[][])new PriorityQueue[numberOfMethods][numberOfStocks]);
		soldStocks = ((PriorityQueue<SimpleData>[][])new PriorityQueue[numberOfMethods][numberOfStocks]);
		ownedStocks = ((PriorityQueue<SimpleData>[][])new PriorityQueue[numberOfMethods][numberOfStocks]);
	}
	
	public void feed(ArrayList<DailyData>[] data) {
		//Make things happend
	}
}
