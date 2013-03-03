package data;

import java.util.Calendar;

/**
 * Datatype that describes the data that is updated every quarter (quarterly earnings report)
 * 
 * @author oskarnylen
 */

public class QuarterlyData implements MarketItem {





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





	/**
	 * 
	 * @param stock
	 * @param dateCollected
	 * @param yield
	 * @param solidity
	 * @param nAV
	 * @param dividentPerShare
	 * @param rOE
	 * @param ePS
	 * @param nAVPS
	 * @param pricePerNAVPS
	 * @param acidTestRatio
	 * @param balanceLiquidity
	 * @param workingCapital
	 */
	public QuarterlyData(Stock stock, Calendar dateCollected, double yield,
			double solidity, LargeDouble nAV, double dividentPerShare,
			double rOE, double ePS, double nAVPS, double pricePerNAVPS,
			double acidTestRatio, double balanceLiquidity,
			LargeDouble workingCapital) {
		this.stock = stock;
		this.dateCollected = dateCollected;
		this.dividendYield = yield;
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

	@Override
	public String toString() {
		return "QuarterlyData [stock=" + stock + ", dateCollected="
				+ dateCollected + ", dividendYield=" + dividendYield + ", solidity=" + solidity
				+ ", NAV=" + NAV + ", dividentPerShare=" + dividentPerShare
				+ ", ROE=" + ROE + ", EPS=" + EPS + ", NAVPS=" + NAVPS
				+ ", pricePerNAVPS=" + pricePerNAVPS + ", acidTestRatio=" + acidTestRatio
				+ ", balanceLiquidity=" + balanceLiquidity
				+ ", workingCapital=" + workingCapital + "]";
	}

}
