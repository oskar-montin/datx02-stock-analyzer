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
	
	final private double [] BA= {17.92,24.78,1.24,2.22,924.94,154.99,1.81,1.69,31.58,10.34};
	final private double [] LMT= {17.92,24.78,1.24,2.22,924.94,154.99,1.81,1.69,31.58,10.34};
	final private double [] GD= {17.92,24.78,1.24,2.22,924.94,154.99,1.81,1.69,31.58,10.34};
	final private double [] UTX= {17.92,24.78,1.24,2.22,924.94,154.99,1.81,1.69,31.58,10.34};
	final private double [] GM={14.82,26.52,0.81,1.61,6.04,40.77,1.36,1.83,12.35,14.08};
	final private double [] F= {14.82,26.52,0.81,1.61,6.04,40.77,1.36,1.83,12.35,14.08};
	final private double [] C= {17.96,22.40,4.42,5.55,113.83,114.11,0.0,16.7,12.73,10.68};
	final private double [] BAC= {17.96,22.40,4.42,5.55,113.83,114.11,0.0,16.7,12.73,10.68};
	final private double [] JPM= {17.96,22.40,4.42,5.55,113.83,114.11,0.0,16.7,12.73,10.68};
	final private double [] GS= {35.95,22.4,3.75,5.55,170.65,114.11,0.68,16.7,4.68,10.68};
	final private double [] AAPL= {12.96,25.07,0.84,2.96,-119.48,-47.91,1.46,2.74,11.02,18.22};
	final private double [] HPQ= {12.96,25.07,0.84,2.96,-119.48,-47.91,1.46,2.74,11.02,18.22};
	final private double [] IBM= {12.96,25.07,0.84,2.96,-119.48,-47.91,1.46,2.74,11.02,18.22};
	final private double [] CSCO= {24.57,25.07,1.2,2.96,111.72,-47.91,1.99,2.74,8.52,18.22};
	final private double [] KO= {39.68,32.02,1.35,5.09,2207.97,188.97,1.88,1.01,10.74,84.39};
	final private double [] MCD= {50.32,26.52,2.94,1.61,4.85,40.77,1.18,1.83,14.39,14.08};
	final private double [] YUM= {50.32,26.52,2.94,1.61,4.85,40.77,1.18,1.83,14.39,14.08};
	final private double [] PEP= {25.22,32.02,2.69,5.09,13.13,188.97,2.0,1.01,19.48,84.39};



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
		
		//System.out.println("PEQUEUE: " + PEQueue);

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
		return new Result("Fundamental Analysis", value, this.resultString(), this.getGraph(), getSignal());
	}

	@Override
	public Signal getSignal() {
		// TODO Auto-generated method stub
		return Signal.NONE;
	}
}
