package data;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Datatype of the closing price (daily) keys and values.
 * 
 * @author oskarnylen, 
 */

public class DailyData implements MarketItem, Comparable<DailyData> {



	private Stock stock;
	private Calendar date;
	

	/* KEYS */
	private LargeDouble marketCap;
	private double dividentYield;
	private double PE;
	private double PS;
	private double PEG;
	
	/* VALUES */
	private double openPrice;
	private double closePrice;
	private double high;
	private double low;
	private long volume;
	
	
/**
 * 
 * @param stock
 * @param date
 * @param marketCap
 * @param dividentYield
 * @param PE
 * @param PS
 * @param PEG
 * @param openPrice
 * @param closePrice
 * @param high
 * @param low
 * @param volume
 */
	public DailyData(Stock stock, Calendar date, LargeDouble marketCap,
			double dividentYield, double PE, 
			double PS, double PEG, double openPrice,
			double closePrice, double high,
			double low, long volume) {
		this.stock = stock;
		this.date = date;
		this.openPrice = openPrice;
		this.closePrice = closePrice;
		this.high = high;
		this.low = low;
		this.volume = volume;
		this.date = date;
		this.marketCap = marketCap;
		this.dividentYield = dividentYield;
		this.PE = PE;
		this.PS = PS;
		this.PEG = PEG;
	}
	
	/**
	 * 
	 * @param stock
	 * @param date
	 * @param openPrice
	 * @param closePrice
	 * @param high
	 * @param low
	 * @param volume
	 */
	public DailyData(Stock stock, Calendar date,  double openPrice,
			double closePrice, double high,
			double low, long volume) {
		this.stock = stock;
		this.date = date;
		this.openPrice = openPrice;
		this.closePrice = closePrice;
		this.high = high;
		this.low = low;
		this.volume = volume;
	}
		
	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toDBEntry() {
		// TODO Auto-generated method stub
		return null;
	}


	public Stock getStock() {
		return stock;
	}

	public Calendar getDate() {
		return date;
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

	public double getOpenPrice() {
		return openPrice;
	}

	public double getClosePrice() {
		return closePrice;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	public long getVolume() {
		return volume;
	}
	
	@Override
	public String toString() {
		return "DailyData [stock=" + stock + ", date=[" + date.get(Calendar.YEAR) 
				+ ", " + (date.get(Calendar.MONTH)+1) + ", " + date.get(Calendar.DAY_OF_MONTH) + "], marketCap="
				+ marketCap + ", dividentYield=" + dividentYield + ", PE=" + PE
				+ ", PS=" + PS + ", PEG=" + PEG + ", openPrice=" + openPrice
				+ ", closePrice=" + closePrice + ", high=" + high + ", low="
				+ low + ", volume=" + volume + "]";
	}

	@Override
	public int compareTo(DailyData o) {
		return this.getDate().compareTo(o.getDate());
	}
	
	public int compareDateTo(DailyData o) {
		return this.getDate().compareTo(o.getDate());
	}
	
	public static Comparator<DailyData> getDateComperator() {
		Comparator<DailyData> comp = new Comparator<DailyData>() {

			@Override
			public int compare(DailyData o1, DailyData o2) {
				return o1.compareDateTo(o2);
			}
			
		};
		return comp;
		
	}
}
