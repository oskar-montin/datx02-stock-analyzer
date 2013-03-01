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
	private double yield;
	private double solidity;
	private LargeDouble NAV;
	private double dividentPerShare;



	/**
	 * 
	 * @param stock
	 * @param releaseDate
	 * @param yield
	 * @param solidity
	 * @param NAV
	 * @param dividentPerShare
	 */
	public QuarterlyData(Stock stock, Calendar releaseDate, double yield,
			double solidity, LargeDouble NAV, double dividentPerShare) {
		this.stock = stock;
		this.dateCollected = releaseDate;
		this.yield = yield;
		this.solidity = solidity;
		this.NAV = NAV;
		this.dividentPerShare = dividentPerShare;
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

	public double getYield() {
		return yield;
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
	
	@Override
	public String toString() {
		return "QuarterlyData [stock=" + stock + ", dateCollected="
				+ dateCollected + ", yield=" + yield + ", solidity=" + solidity
				+ ", NAV=" + NAV + ", dividentPerShare=" + dividentPerShare
				+ "]";
	}
	
}
