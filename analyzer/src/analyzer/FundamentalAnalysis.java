package analyzer;

import java.util.Collection;
import java.util.PriorityQueue;

import data.Curve;
import data.DailyData;
import data.FundamentalData;
import data.QuarterlyData;
import data.Result;
import data.Signal;
import data.SimpleData;
import data.Stock;

public class FundamentalAnalysis implements AnalysisMethod {

	private Stock stock;

	private QuarterlyData quarterlyDataPoint;
	
	private PriorityQueue<DailyData> dailyDataQueue;
	
	private PriorityQueue<SimpleData> PEQueue;
	private PriorityQueue<SimpleData> PEGQueue;
	private PriorityQueue<SimpleData> PSQueue;
	private PriorityQueue<SimpleData> marketCap;
	private PriorityQueue<SimpleData> dividentYield;


	private FundamentalData stockFundamentalData;
	
//	private FundamentalData businessFundamentalData;

	public FundamentalAnalysis(QuarterlyData qd, Collection<DailyData> sd){
		stock = qd.getStock();

		quarterlyDataPoint = qd;
		dailyDataQueue = new PriorityQueue<DailyData>(sd);
		
		PEQueue = createValueQueue(sd, "PE");
		PEGQueue = createValueQueue(sd, "PEG");
		PSQueue = createValueQueue(sd, "PS");
		marketCap = createValueQueue(sd, "marketCap");
		dividentYield = createValueQueue(sd, "dividentYield");
		
		System.out.println("PEQUEUE: " + PEQueue);

//		businessFundamentalData = new FundamentalData(Util.quarterlyDataMean(stock), Util.dailyDataMean(stock));
	}

	private PriorityQueue<SimpleData> createValueQueue(Collection<DailyData> sd, String type){
		PriorityQueue<SimpleData> returnQueue = new PriorityQueue<SimpleData>();
		PriorityQueue<DailyData> sdQueue = new PriorityQueue<DailyData>(sd);
		
		if(type == "PE"){
			while(!sdQueue.isEmpty()){
				DailyData tempData = sdQueue.poll();
				returnQueue.add(new SimpleData(tempData.getStock(),tempData.getDate(),tempData.getPE()));
			}
		}
		
		if(type == "PEG"){
			while(!sdQueue.isEmpty()){
				DailyData tempData = sdQueue.poll();
				returnQueue.add(new SimpleData(tempData.getStock(),tempData.getDate(),tempData.getPEG()));
			}
		}
		
		if(type == "PS"){
			while(!sdQueue.isEmpty()){
				DailyData tempData = sdQueue.poll();
				returnQueue.add(new SimpleData(tempData.getStock(),tempData.getDate(),tempData.getPS()));
			}
		}
		
		if(type == "marketCap"){
			while(!sdQueue.isEmpty()){
				DailyData tempData = sdQueue.poll();
				returnQueue.add(new SimpleData(tempData.getStock(),tempData.getDate(),tempData.getMarketCap().toDouble()));
			}
		}
		
		if(type == "dividentYield"){
			while(!sdQueue.isEmpty()){
				DailyData tempData = sdQueue.poll();
				returnQueue.add(new SimpleData(tempData.getStock(),tempData.getDate(),tempData.getDividentYield()));
			}
		}
		
		return returnQueue;
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
		Curve[] curves = new Curve[1];
		curves[0] = new Curve(dividentYield, "Dividend Yield");
	//	curves[0] = new Curve(PSQueue, "PS-value");
	//	curves[0] = new Curve(marketCap, "Market Cap");
	//	curves[0] = new Curve(PEGQueue, "PEG-value");
	//	curves[0] = new Curve(PEQueue, "PE-value");
		
		return curves;
	}

	@Override
	public Result getResult() {
		Double value = this.value();
		Signal signal;
		return new Result("Fundamental Analysis", value, this.resultString(), this.getGraph(), Signal.NONE);
	}
}
