package analyzer;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.PriorityQueue;

import util.Util;

import data.DailyData;
import data.FundamentalData;
import data.LargeDouble;
import data.QuarterlyData;
import data.Stock;

public class FundamentalAnalysis {

	private Stock stock;

	private QuarterlyData quarterlyDataPoint;
	private PriorityQueue<DailyData> dailyDataList = new PriorityQueue<DailyData>();
	private DailyData dailyDataPoint;

	private FundamentalData stockFundamentalData;
	private FundamentalData businessFundamentalData;

	public FundamentalAnalysis(String symbol){
		this.stock = DatabaseHandler.getStock(symbol);

		dailyDataList = DatabaseHandler.getDailyData(stock);

		quarterlyDataPoint = DatabaseHandler.getQuarterlyData(stock);
		dailyDataPoint = Util.getLatestDailyData(dailyDataList);

		stockFundamentalData = new FundamentalData(quarterlyDataPoint, dailyDataPoint);

		businessFundamentalData = new FundamentalData(Util.quarterlyDataMean(stock), Util.dailyDataMean(stock));
	}

	public int comparePE(){
		if(stockFundamentalData.getPE() > businessFundamentalData.getPE())
			return 1;
		else
			return 0;
	}
}
