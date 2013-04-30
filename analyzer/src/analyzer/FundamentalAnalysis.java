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
	private final double[] UNH = {25.70,32.88,0.57,10.15,-5.92,43.27,0.51,2.94,14.67,16.97};
	private final double[] AET = {26.58,22.40,2.90,5.55,43.18,114.11,0,16.70,8.22,10.68};
	private final double[] HUM = {25.70,32.88,0.57,10.15,-5.92,43.27,0.51,2.94,14.67,16.97};
	private final double[] WLP = {26.58,22.40,2.90,5.55,43.18,114.11,0,16.70,8.22,10.68};
	private final double[] GOOG = {21.27,25.07,3.83,2.96,-53.72,-47.91,2.77,2.74,25.30,18.22};
	private final double[] FB = {21.27,25.07,3.83,2.96,-53.72,-47.91,2.77,2.74,25.30,18.22};
	private final double[] MSFT = {19.53,25.07,3.49,2.96,44.44,-47.91,3.12,2.74,11.74,18.22};
	private final double[] ORCL = {19.53,25.07,3.49,2.96,44.44,-47.91,3.12,2.74,11.74,18.22};
	private final double[] XOM = {15.13,16.94,1.72,1.95,341.88,525.89,1.81,1.51,16.77,9.22};
	private final double[] VLO = {12.35,11.09,2.50,2.08,68.97,181.57,0.54,1.35,5.62,16.38};
	private final double[] CVX = {12.35,11.09,2.50,2.08,68.97,181.57,0.54,1.35,5.62,16.38};
	private final double[] BP = {6.96,11.09,3.15,2.08,6.87,181.57,0.66,1.35,7.56,16.38};
	private final double[] CAH = {20.84,32.02,3.84,5.09,3.72,188.97,1.27,1.01,11.34,84.39};
	private final double[] PFE = {33.28,32.88,4.69,10.15,32.16,43.27,2.90,2.94,17.85,16.97};
	private final double[] JNJ = {33.28,32.88,4.69,10.15,32.16,43.27,2.90,2.94,17.85,16.97};
	private final double[] MRK = {33.28,32.88,4.69,10.15,32.16,43.27,2.90,2.94,17.85,16.97};
	private final double[] WMT = {15.61,26.52,0.68,1.61,29.22,40.77,1.49,1.83,13.24,14.08};
	private final double[] CVS = {20.84,32.02,3.84,5.09,3.72,188.97,1.27,1.01,11.34,84.39};
	private final double[] COST = {24.21,26.52,1.07,1.61,21.02,40.77,1.17,1.83,17.34,14.08};
	private final double[] TGT = {24.21,26.52,1.07,1.61,21.02,40.77,1.17,1.83,17.34,14.08};
	private final double[] T = {24.74,20.21,7.27,4.64,82.38,57.22,1.16,0.92,17.36,19.40};
	private final double[] VZ = {15.19,20.21,1.72,4.64,29.32,57.22,0.64,0.92,21.65,19.40};
	private final double[] S = {24.74,20.21,7.27,4.64,82.38,57.22,1.16,0.92,17.36,19.40};
	private final double[] TEF = {15.19,20.21,1.72,4.64,29.32,57.22,0.64,0.92,21.65,19.40};



	private FundamentalData stockFundamentalData;
	
//	private FundamentalData businessFundamentalData;

	public FundamentalAnalysis(QuarterlyData qd, DailyData dd){
		stock = qd.getStock();

		//System.out.println("PEQUEUE: " + PEQueue);

//		businessFundamentalData = new FundamentalData(Util.quarterlyDataMean(stock), Util.dailyDataMean(stock));
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
	//	Curve[] curves = new Curve[1];
	//	curves[0] = new Curve(dividentYield, "Dividend Yield");
	//	curves[0] = new Curve(PSQueue, "PS-value");
	//	curves[0] = new Curve(marketCap, "Market Cap");
	//	curves[0] = new Curve(PEGQueue, "PEG-value");
	//	curves[0] = new Curve(PEQueue, "PE-value");
		
		return null;
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
