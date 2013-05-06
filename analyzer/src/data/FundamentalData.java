package data;

import java.util.Calendar;

/**
 * Datatype that describes the data that is updated every quarter (quarterly earnings report)
 * 
 * @author oskarnylen
 */

public class FundamentalData implements MarketItem, Comparable<FundamentalData> {


	private Stock stock;
	private Calendar dateCollected;
	private double solidity;								// Total shareholders equity / Total assets
	private double ROE;										// Net Income / stockholders equity
	private double EPS;										// Earnings per share (Net earnings/outstanding shares)
	private double acidTestRatio;							// (current assets - inventory) / current liabilities
	private LargeDouble workingCapital;						// current assets - current liabilities
	private double PE;
	private double PS;
	private double PEG;
	private double dividendYeild;

	
	public FundamentalData(QuarterlyData qd, DailyData dd) {
		
		stock = qd.getStock();
		dateCollected = qd.getDateCollected();
		solidity = qd.getSolidity();
		
		ROE = qd.getROE();
		EPS = qd.getEPS();
		acidTestRatio = qd.getAcidTestRatio();
		workingCapital = qd.getWorkingCapital();
		
		dividendYeild = dd.getDividentYield();
		PE = dd.getPE();
		PS = dd.getPS();
		PEG = dd.getPEG();
	}
	
	public FundamentalData(Stock stock, Calendar dateCollected,
			 double solidity, double rOE, double ePS,  double acidTestRatio, 
			 LargeDouble workingCapital, double pE, double pS, double pEG, double dividendYeild) {
		super();
		this.stock = stock;
		this.dateCollected = dateCollected;
		this.solidity = solidity;
		ROE = rOE;
		EPS = ePS;
		this.acidTestRatio = acidTestRatio;
		this.workingCapital = workingCapital;
		this.dividendYeild =dividendYeild;
		PE = pE;
		PS = pS;
		PEG = pEG;
	}
	
	public Stock getStock() {
		return stock;
	}



	public Calendar getDateCollected() {
		return dateCollected;
	}


	public double getSolidity() {
		return solidity;
	}



	public double getROE() {
		return ROE;
	}



	public double getEPS() {
		return EPS;
	}


	public double getAcidTestRatio() {
		return acidTestRatio;
	}




	public LargeDouble getWorkingCapital() {
		return workingCapital;
	}


	public double getPE() {
		return PE;
	}



	public double getPS() {
		return PS;
	}



	public double getPEG() {
		return PEG;
	}
	
	public double getDividendYield() {
		return dividendYeild;
	}

	@Override
	public int compareTo(FundamentalData o) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public String toDBEntry() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "FundamentalData [stock=" + stock + ", dateCollected="
				+ dateCollected 
				+ ", solidity=" + solidity 
				 + ", ROE=" + ROE
				+ ", EPS=" + EPS 
				+  ", acidTestRatio=" + acidTestRatio
				+ ", workingCapital=" + workingCapital 
				  + ", PE=" + PE
				+ ", PS=" + PS + ", PEG=" + PEG + "]";
	}
	
}
