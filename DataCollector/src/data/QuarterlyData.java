package data;

import java.util.Date;

/**
 * Datatype that describes the data that is updated every quarter (quarterly earnings report)
 * 
 * @author oskarnylen
 */

public class QuarterlyData implements MarketItem {

	private Stock stock;
	private Date releaseDate;
	private double yield;
	private double solidity;
	private double NAV;
	private double dividentPerShare;
	
	public QuarterlyData(Stock stock, Date releaseDate, double yield,
			double solidity, double NAV, double dividentPerShare) {
		super();
		this.stock = stock;
		this.releaseDate = releaseDate;
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
	
}
