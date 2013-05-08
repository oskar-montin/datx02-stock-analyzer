package analyzer;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.PriorityQueue;

import data.Curve;
import data.DailyData;
import data.FundamentalData;
import data.QuarterlyData;
import data.Result;
import data.Signal;
import data.Stock;
import frontend.MainFrame;

import controller.DatabaseHandler;

public class FundamentalAnalysis implements AnalysisMethod {

	private Stock stock;
	private static HashMap<String,double[]> branschValues = null;
	final private int PEind=0, PEsec=1, PSind=2, PSsec=3, EPSind=4, EPSsec=5, CRind=6, CRsec=7, ROEind=8, ROEsec=9;
	private double roeValue, atrValue, epsValue, PEValue, PEGValue;
	
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
//	private FundamentalData businessFundamentalData;

	public FundamentalAnalysis(FundamentalData stockFundamentalData){
		setValues();
		stock = stockFundamentalData.getStock();
		roeValue=ROE(stockFundamentalData);
		atrValue=ATR(stockFundamentalData);
		epsValue=EPS(stockFundamentalData);
		PEGValue = PEG(stockFundamentalData);
		PEValue = PE(stockFundamentalData);
			//System.out.println("PEQUEUE: " + PEQueue);

//		businessFundamentalData = new FundamentalData(Util.quarterlyDataMean(stock), Util.dailyDataMean(stock));
	}
	
	public static void main(String[] args) {
	String [] symbol={"BA", "LMT", "GD", "UTX", "GM", "F", "C", "BAC", "JPM", "GS", "AAPL","HPQ", "IBM","CSCO","MCD","YUM",
	"PEP","UNH","AET","HUM","WLP","GOOG","FB","MSFT","ORCL","XOM","VLO","CVX","CAH","PFE","JNJ","MRK","WMT","CVS","COST","TGT","T","VZ","S","TEF"};
	
	for(int i=0;i<38;i++){
	QuarterlyData qd =DatabaseHandler.getQuarterlyData(DatabaseHandler.getStock(symbol[i]));

	Calendar from = Calendar.getInstance();
	Calendar to = Calendar.getInstance();
	from.set(Calendar.MONTH, 2);
	from.set(Calendar.DATE, 10);
	to.set(Calendar.MONTH, 2);
	to.set(Calendar.DATE, 12);
	
	PriorityQueue<DailyData> dd = DatabaseHandler.getDailyData(DatabaseHandler.getStock(symbol[i]), from, to );
	FundamentalAnalysis fa = new FundamentalAnalysis(new FundamentalData(qd, dd.peek()));

	System.out.println("stock: "+fa.stock.getSymbol());
	System.out.println("roe: "+fa.getRoeValue());
	System.out.println("atr: "+fa.getAtrValue());
	System.out.println("eps: "+fa.getEpsValue());
	System.out.println("peg: "+fa.getPEGValue());
	System.out.println("pe: "+fa.getPEValue());
	System.out.println("nr: "+fa.getNrValues());
	System.out.println("		total: "+fa.value());
	}
	}
	


	public double getRoeValue() {
		return roeValue;
	}

	public double getAtrValue() {
		return atrValue;
	}

	public double getEpsValue() {
		return epsValue;
	}
	
	public double getPEValue(){
		return PEValue;
	}
	
	public double getPEGValue(){
		return PEGValue;
	}

	public int getNrValues(){
		int nrValues=0;
		
		if(PEGValue!=0){
			nrValues=nrValues+1;
		}
		if(PEValue!=0){
			nrValues=nrValues+1;
		}
		if(atrValue!=0){
			nrValues=nrValues+1;
		}
		if(roeValue!=0){
			nrValues=nrValues+1;
		}
		if(epsValue!=0){
			nrValues=nrValues+1;
		}
		
		return nrValues;
	}



	@Override
	public String resultString() {
		return "Fundamental analysis, valued on a scale from 1 to 5";
	}

	@Override
	public double value() {
		double value=0, nrValues=0;
		
		if(PEGValue!=0){
			value=value+PEGValue;
			nrValues=nrValues+1;
		}
		if(PEValue!=0){
			value=value+PEValue;
			nrValues=nrValues+1;
		}
		if(atrValue!=0){
			value=value+atrValue;
			nrValues=nrValues+1;
		}
		if(roeValue!=0){
			value=value+roeValue;
			nrValues=nrValues+1;
		}
		if(epsValue!=0){
			value=value+epsValue;
			nrValues=nrValues+1;
		}
		
		value = value/nrValues;
		
		return value;
	}

	@Override
	public Curve[] getGraph() {
		
		return null;
	}

	@Override
	public Result getResult() {
		Double value = this.value();
		return new Result("Fundamental Analysis", value, this.resultString(), this.getGraph(), getSignal());
	}

	@Override
	public Signal getSignal() {
		return Signal.NONE;
	}
	private void setValues(){
		if (branschValues == null) {
			branschValues = new HashMap<String, double[]>();
			branschValues.put("BA",BA);
			branschValues.put("LMT",LMT);
			branschValues.put("GD",GD);
			branschValues.put("UTX",UTX);
			branschValues.put("GM",GM);
			branschValues.put("F",F);
			branschValues.put("C",C);
			branschValues.put("BAC",BAC);
			branschValues.put("JPM", JPM);
			branschValues.put("GS",GS);
			branschValues.put("AAPL",AAPL);
			branschValues.put("HPQ",HPQ);
			branschValues.put("IBM",IBM);
			branschValues.put("CSCO",CSCO);
			branschValues.put("MCD",MCD);
			branschValues.put("YUM",YUM);
			branschValues.put("PEP",PEP);
			branschValues.put("UNH",UNH);
			branschValues.put("AET",AET);
			branschValues.put("HUM",HUM);
			branschValues.put("WLP",WLP);
			branschValues.put("GOOG",GOOG);
			branschValues.put("FB",FB);
			branschValues.put("MSFT",MSFT);
			branschValues.put("ORCL",ORCL);
			branschValues.put("XOM",XOM);
			branschValues.put("VLO",VLO);
			branschValues.put("CVX",CVX);
			branschValues.put("BP",BP);
			branschValues.put("CAH",CAH);
			branschValues.put("PFE",PFE);
			branschValues.put("JNJ",JNJ);
			branschValues.put("MRK",MRK);
			branschValues.put("WMT",WMT);
			branschValues.put("CVS",CVS);
			branschValues.put("COST",COST);
			branschValues.put("TGT",TGT);
			branschValues.put("T",T);
			branschValues.put("VZ",VZ);
			branschValues.put("S",S);
			branschValues.put("TEF",TEF);
		}
	}
	private int PE(FundamentalData dd){
		
		double PEIndValue = branschValues.get(stock.getSymbol())[PEind];
		double quotientPE = dd.getPE()/PEIndValue;
		boolean over = quotientPE > 1;
		if(dd.getPE()==0) return 0;
		
		else if (quotientPE >= 0.97 && quotientPE <= 1.03) {
			return 5; // PE is within limit, other values result in 5 or 3 return.
		}
		else if (quotientPE >= 0.94 && quotientPE <= 1.06){
			return 4; // PE is barely within limit, other values result in....
		}
		else if (over){
			if (quotientPE <= 1.09){
				return 3; // PE is slightly out of limit..
			}
			else if (quotientPE <= 1.16){
				return 2; // PE is very out of limit..
			}
			else {
				return 1;
			}
		}
		else if (quotientPE >= 0.91){
			return 3; //PE is slightly out of limit(under)
		}
		else if (quotientPE >= 0.84){
			return 2; //PE is very out of limit(under)
		}
		else {
			return 1;
		}
	}
	
	private int PEG(FundamentalData dd){
		boolean highDivYield = dd.getDividendYield() > 1.05; // divident yield high? -> bad
		double PEG = dd.getPEG();
		//höja värden?, väldigt få som får bra resultat
		if(PEG==0 || dd.getDividendYield()==0) return 0;
		
		else if(PEG < 0.5){
			return (highDivYield) ? 3 : 5; // PEG good, but yield decides 3 or 5.
		}
		else if (PEG <= 1){
			return (highDivYield) ? 3 : 4; // PEG mnjaaaa maybe, yield decides...
		}
		else if (PEG <= 1.5){
			return (highDivYield) ? 1 : 2; // PEG Bad ......
		}
		else {
			return 1;                      // PEG stinks
		}
	}
	
	
	
	private int ROE(FundamentalData qd){
		double roeValue = qd.getROE()*100;
		double roeIndValue=branschValues.get(stock.getSymbol())[ROEind];
		
		if(roeValue==(-Math.PI)) return 0; // inget ROE värde
		
		if(roeValue>(roeIndValue+3)){
			if(qd.getSolidity()>0.25){
				return 5; // bra ROE och bra soliditet, dela upp i två både 4 och 5 (gradera Roe el soliditet)
			}
			else return 2;//bra ROE och dålig soliditet
		}
		
		else if(roeValue>(roeIndValue-3))
			if(qd.getSolidity()>0.25 ){
				return 3; //OK ROE och bra soliditet, kanske 4
			}
			else return 2; //OK ROE och dålig soliditet
		
		else if(qd.getSolidity()>0.25){
			return 2; // dåligt ROE och bra soliditet
		}
		else return 1; //dåligt ROE och dålig soliditet
	}
	
	private int ATR(FundamentalData qd){
		double atrValue = qd.getAcidTestRatio();
		double atrIndValue=branschValues.get(stock.getSymbol())[CRind];
		
		if(!(atrValue==-Math.PI)){
			if(atrValue>(atrIndValue-0.5)){
				if(atrValue<(atrIndValue+2)&&atrValue>atrIndValue) 
					if (qd.getWorkingCapital().toDouble()<0) return 3;
					else return 5;// bra intervall och bättre än medel, kanske 4
				
				if(atrValue>(atrIndValue+2)) return 2; // för högt värde
				
				else return 3; //över undre gräsen men  under medel
			}
			else if(qd.getWorkingCapital().toDouble()<1) return 1; //dålig ATR och dåligt WC
			
			else return 2; //dåligt ATR men bra WC
			
		}
		else return 0; // inget värde finns
	}
	
	private int EPS(FundamentalData qd){
		double epsValue = qd.getEPS();
		double epsIndValue=branschValues.get(stock.getSymbol())[EPSind];
		
		if(epsValue==0) return 0; // inget värde för EPS finns
		
		else{
			
			if(epsValue>(epsIndValue-5))
				if(epsValue<0) return 2; //bättre än medel men under 0
				else if (epsValue>epsIndValue+15) return 5; // en bra bit bättre än medel
				else return 4; // bättre än medel
			
			else if (epsValue<0)return 1; // sämre än medel och under 0
			
			else return 2; // sämre än medel
			
			
		}
		
	}
}
