package controller;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import analyzer.BollingerBands;
import analyzer.CMF;
import analyzer.ExponentialMovingAverage;
import analyzer.MACD;
import analyzer.RateOfChange;
import analyzer.RelativeStrengthIndex;
import analyzer.SimpleMovingAverage;
import analyzer.StochasticOscillator;

import data.Curve;
import data.DailyData;
import data.Result;
import data.Signal;
import data.SimpleData;
import data.Stock;


public class Controller {

	private static Controller instance = null;
	private Stock stock = null;
	private PriorityQueue<DailyData> data;
	public static Controller getInstance(){
		if(instance == null){
			instance = new Controller();
		}
		return instance;
	}

	public void setStock(String symbol) {
		this.stock = DatabaseHandler.getStock(symbol);
		data = DatabaseHandler.getDailyData(stock);
	}
	
	public Collection<Result> getAnalyticsData() {
		List<Result> results = new LinkedList<Result>();
		results.add(this.getClosePriceResult());
		results.addAll(getStandardMethodResults());
		return results;
	}
	
	private Collection<Result> getStandardMethodResults() {
		Settings settings = Settings.getSettings();
		List<Result> results = new LinkedList<Result>();
		SimpleMovingAverage sma = new SimpleMovingAverage(data, settings.getSMAOffset());
		results.add(sma.getResult());
		ExponentialMovingAverage ema = new ExponentialMovingAverage(data,settings.getEMAOffset());
		results.add(ema.getResult());
		BollingerBands bb = new BollingerBands(data, settings.getBBOffset());
		results.add(bb.getResult());
		CMF cmf = new CMF(this.stock,data,settings.getCMFOffset());
		results.add(cmf.getResult());
		MACD macd = new MACD(data,settings.getMACDShortOffset(),settings.getMACDLongOffset(),settings.getMACDSignalOffset());
		results.add(macd.getResult());
		RateOfChange roc = new RateOfChange(data, settings.getROCOffset());
		results.add(roc.getResult());
		RelativeStrengthIndex rsi = new RelativeStrengthIndex(this.stock, data, settings.getRSIOffset());
		results.add(rsi.getResult());
		StochasticOscillator so = new StochasticOscillator(data, 
														   settings.getSOShortOffset(), 
														   settings.getSOMidOffset(), 
														   settings.getSOLongOffset(),
														   settings.getSOSpeedOffset());
		results.add(so.getResult());
		return results;
	}

	private Result getClosePriceResult() {
		PriorityQueue<SimpleData> tempQueue = new PriorityQueue<SimpleData>();
		LinkedList<DailyData> temp = new LinkedList<DailyData>(data);
		Curve[] curves = new Curve[1];
		for(DailyData dd:data) {
			tempQueue.add(new SimpleData(dd));
		}
		curves[0] = new Curve(tempQueue,"Close price curve");
		return new Result("Close price",temp.getLast().getClosePrice(),"",curves, Signal.NONE);
	}
}
