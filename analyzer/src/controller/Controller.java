package controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import analyzer.BollingerBands;
import analyzer.CMF;
import analyzer.ExponentialMovingAverage;
import analyzer.Fibonacci;
import analyzer.FundamentalAnalysis;
import analyzer.MACD;
import analyzer.RateOfChange;
import analyzer.RelativeStrengthIndex;
import analyzer.SimpleMovingAverage;
import analyzer.StochRSI;
import analyzer.StochasticOscillator;
import analyzer.TrendLine;
import analyzer.VolatilityBands;

import data.Curve;
import data.DailyData;
import data.FundamentalData;
import data.QuarterlyData;
import data.Result;
import data.Signal;
import data.SimpleData;
import data.Stock;


public class Controller {

	private static Controller instance = null;
	private Stock stock = null;
	private PriorityQueue<DailyData> dailyData;
	private FundamentalData fundamentalData;
	private QuarterlyData quarterlyData;
	public static Controller getInstance(){
		if(instance == null){
			instance = new Controller();
		}
		return instance;
	}

	public void setStock(String symbol) {
		this.stock = DatabaseHandler.getStock(symbol);
		ArrayList<Calendar> dates = new ArrayList<Calendar>(DatabaseHandler.getDates());
		//TODO: Om någon vill se alla aktier, "byt ut dates.get(dates.size()-31)" till "dates.get(0)"
		dailyData = DatabaseHandler.getDailyData(stock,dates.get(dates.size()-31),dates.get(dates.size()-1));
		quarterlyData = DatabaseHandler.getQuarterlyData(stock);
	}
	
	public List<Result> getAnalyticsData() {
		List<Result> results = new LinkedList<Result>();
		results.add(this.getClosePriceResult());
		results.addAll(getStandardMethodResults());
		return results;
	}
	
	private List<Result> getStandardMethodResults() {
		Settings settings = Settings.getSettings();
		List<Result> results = new LinkedList<Result>();
		
		SimpleMovingAverage sma = new SimpleMovingAverage(dailyData, settings.getSMAOffset());
		results.add(sma.getResult());
		
		ExponentialMovingAverage ema = new ExponentialMovingAverage(dailyData,settings.getEMAOffset());
		results.add(ema.getResult());
		
		BollingerBands bb = new BollingerBands(dailyData, settings.getBBOffset());
		results.add(bb.getResult());
		
		VolatilityBands vb = new VolatilityBands(dailyData, settings.getBBOffset());
		results.add(vb.getResult());
		
		CMF cmf = new CMF(dailyData, settings.getCMFOffset());
		results.add(cmf.getResult());
		
		MACD macd = new MACD(dailyData,settings.getMACDShortOffset(),settings.getMACDLongOffset(),settings.getMACDSignalOffset());
		results.add(macd.getResult());
		
		RateOfChange roc = new RateOfChange(dailyData, settings.getROCOffset());
		results.add(roc.getResult());
		
		RelativeStrengthIndex rsi = new RelativeStrengthIndex(dailyData, settings.getRSIOffset());
		results.add(rsi.getResult());
		
		StochRSI stochrsi = new StochRSI(dailyData, settings.getRSIOffset());
		results.add(stochrsi.getResult());

		Fibonacci fib = new Fibonacci(dailyData, settings.getFibOffset());
		results.add(fib.getResult());

		TrendLine tlines = new TrendLine(dailyData, settings.getTrendOffset());
		results.add(tlines.getResult());
		
		StochasticOscillator so = new StochasticOscillator(dailyData, 
														   settings.getSOShortOffset(), 
														   settings.getSOMidOffset(), 
														   settings.getSOLongOffset(),
														   settings.getSOSpeedOffset());
		results.add(so.getResult());
	
		FundamentalAnalysis fa = new FundamentalAnalysis(fundamentalData);
		results.add(fa.getResult());
		
		return results;
	}

	private Result getClosePriceResult() {
		PriorityQueue<SimpleData> tempQueue = new PriorityQueue<SimpleData>();
		LinkedList<DailyData> temp = new LinkedList<DailyData>(dailyData);
		Curve[] curves = new Curve[1];
		for(DailyData dd:dailyData) {
			tempQueue.add(new SimpleData(dd));
		}
		curves[0] = new Curve(tempQueue,"Close price curve");
		return new Result("Close price",temp.getLast().getClosePrice(),"",curves, Signal.NONE);
	}
}
