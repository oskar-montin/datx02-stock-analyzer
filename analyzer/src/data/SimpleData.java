package data;

import java.util.Calendar;

/**
 * Datatype of the closing price (daily) keys and values.
 * 
 * @author oskarnylen, 
 */

public class SimpleData implements MarketItem, Comparable<DailyData> {


	private Stock stock;
	private Calendar date;
	
	private double value;

	/**
	 * 
	 * @param stock
	 * @param date
	 * @param closePrice
	 */
	public SimpleData(Stock stock, Calendar date, double closePrice) {
		this.stock = stock;
		this.date = date;
		this.value = closePrice;
	}
	
	/**
	 * 
	 * @param dd
	 */
	public SimpleData(DailyData dd) {
		this.stock = dd.getStock();
		this.date = dd.getDate();
		this.value = dd.getClosePrice();
	}
	

	public Stock getStock() {
		return stock;
	}

	public Calendar getDate() {
		return date;
	}

	public double getClosePrice() {
		return value;
	}
	
	public void setClosePrice(double price){
		value = price;
	}
	

	@Override
	public int compareTo(DailyData o) {
		return this.getDate().compareTo(o.getDate());
	}

	
	@Override
	public String toString() {
		return "SimpleData [stock=" + stock + ", [date=" + date.get(Calendar.YEAR) 
				+ "-" + (date.get(Calendar.MONTH)+1) + "-" + date.get(Calendar.DAY_OF_MONTH) + "], [closePrice=" + value + "]";
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
