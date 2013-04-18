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
	private double dividendYield;
	private double solidity;								// Total shareholders equity / Total assets
	private LargeDouble NAV;								
	private double dividentPerShare;



	private double ROE;										// Net Income / stockholders equity
	private double EPS;										// Earnings per share (Net earnings/outstanding shares)
	private double NAVPS;									// Net Asset Value per Share (NAV/total outstanding shares)
	private double pricePerNAVPS;							// Last close / NAVPS			
	private double acidTestRatio;							// (current assets - inventory) / current liabilities
	private double balanceLiquidity;						// current assets / current liabilities
	private LargeDouble workingCapital;						// current assets - current liabilities

	private LargeDouble marketCap;
	
	private double dividentYield;
	
	private double PE;
	private double PS;
	private double PEG;

	
	public FundamentalData(QuarterlyData qd, DailyData dd) {
		
		stock = qd.getStock();
		dateCollected = qd.getDateCollected();
		solidity = qd.getSolidity();
		NAV = qd.getNAV();
		dividentPerShare = qd.getDividentPerShare();
		
		ROE = qd.getROE();
		EPS = qd.getEPS();
		NAVPS = qd.getNAVPS();
		pricePerNAVPS = qd.getPricePerNAVPS();
		acidTestRatio = qd.getAcidTestRatio();
		balanceLiquidity = qd.getBalanceLiquidity();
		workingCapital = qd.getWorkingCapital();
		
		marketCap = dd.getMarketCap();
		dividentYield = dd.getDividentYield();
		PE = dd.getPE();
		PS = dd.getPS();
		PEG = dd.getPEG();
	}
	
	public FundamentalData(Stock stock, Calendar dateCollected,
			double dividendYield, double solidity, LargeDouble nAV,
			double dividentPerShare, double rOE, double ePS, double nAVPS,
			double pricePerNAVPS, double acidTestRatio,
			double balanceLiquidity, LargeDouble workingCapital,
			LargeDouble marketCap, double dividentYield, double pE, double pS,
			double pEG) {
		super();
		this.stock = stock;
		this.dateCollected = dateCollected;
		this.dividendYield = dividendYield;
		this.solidity = solidity;
		NAV = nAV;
		this.dividentPerShare = dividentPerShare;
		ROE = rOE;
		EPS = ePS;
		NAVPS = nAVPS;
		this.pricePerNAVPS = pricePerNAVPS;
		this.acidTestRatio = acidTestRatio;
		this.balanceLiquidity = balanceLiquidity;
		this.workingCapital = workingCapital;
		this.marketCap = marketCap;
		this.dividentYield = dividentYield;
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



	public double getDividendYield() {
		return dividendYield;
	}



	public double getSolidity() {
		return solidity;
	}



	public LargeDouble getNAV() {
		return NAV;
	}



	public double getDividentPerShare() {
		return dividentPerShare;
	}



	public double getROE() {
		return ROE;
	}



	public double getEPS() {
		return EPS;
	}



	public double getNAVPS() {
		return NAVPS;
	}



	public double getPricePerNAVPS() {
		return pricePerNAVPS;
	}



	public double getAcidTestRatio() {
		return acidTestRatio;
	}



	public double getBalanceLiquidity() {
		return balanceLiquidity;
	}



	public LargeDouble getWorkingCapital() {
		return workingCapital;
	}



	public LargeDouble getMarketCap() {
		return marketCap;
	}



	public double getDividentYield() {
		return dividentYield;
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
				+ dateCollected + ", dividendYield=" + dividendYield
				+ ", solidity=" + solidity + ", NAV=" + NAV
				+ ", dividentPerShare=" + dividentPerShare + ", ROE=" + ROE
				+ ", EPS=" + EPS + ", NAVPS=" + NAVPS + ", pricePerNAVPS="
				+ pricePerNAVPS + ", acidTestRatio=" + acidTestRatio
				+ ", balanceLiquidity=" + balanceLiquidity
				+ ", workingCapital=" + workingCapital + ", marketCap="
				+ marketCap + ", dividentYield=" + dividentYield + ", PE=" + PE
				+ ", PS=" + PS + ", PEG=" + PEG + "]";
	}
	
}
