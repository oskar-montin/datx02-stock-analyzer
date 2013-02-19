package data;

import java.util.Date;

/**
 * Datatype of the closing price (daily) keys and values.
 * 
 * @author oskarnylen, 
 */

public class DailyData implements MarketItem {

	private Stock stock;
	private Date  date;
	
	/* KEYS */
	private double marketCap;
	private double dividentYield;
	private double PE;
	private double PS;
	private double PEG;
	
	/* VALUES */
	private double openPrice;
	private double closePrice;
	private double high;
	private double low;
	private double volume;
	
	public DailyData(Stock stock, Date date, double marketCap,
			double dividentYield, double PE, 
			double PS, double PEG, 
			double closePrice, double high,
			double low, double volume) {
		this.stock = stock;
		this.date = date;
		this.openPrice = openPrice;
		this.closePrice = closePrice;
		this.high = high;
		this.low = low;
		this.volume = volume;
		this.stock = stock;
		this.date = date;
		this.marketCap = marketCap;
		this.dividentYield = dividentYield;
		this.PE = PE;
		this.PS = PS;
		this.PEG = PEG;
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
