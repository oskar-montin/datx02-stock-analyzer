package data;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Datatype of the closing price (daily) keys and values.
 * 
 * @author oskarnylen, 
 */

public class SimpleData implements MarketItem, Comparable<SimpleData> {


	protected Stock stock;
	protected Calendar date;
	
	protected double value;

	/**
	 * 
	 * @param stock
	 * @param date
	 * @param closePrice
	 */
	public SimpleData(Stock stock, Calendar date, double value) {
		this.stock = stock;
		this.date = date;
		this.value = value;
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

	public double getValue() {
		return value;
	}

	
	@Override
	public String toString() {
		return "SimpleData [stock=" + stock + ", [date=" + date.get(Calendar.YEAR) 
				+ "-" + (date.get(Calendar.MONTH)+1) + "-" + date.get(Calendar.DAY_OF_MONTH) + "], [value=" + value + "]";
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
	public int compareTo(SimpleData o) {
		return this.getDate().compareTo(o.getDate());
	}
	
	public static Comparator<SimpleData> getValueComparator(final boolean abs) {
		Comparator<SimpleData> comp = new Comparator<SimpleData>() {

			@Override
			public int compare(SimpleData o1, SimpleData o2) {
				if(abs) {
					Double d1 = Math.abs(o1.getValue());
					Double d2 = Math.abs(o2.getValue());
					return d1.compareTo(d2);
				} else {
					return new Double(o1.getValue()).compareTo(o2.getValue());
				}
			}
			
		};
		return comp;
	}
	
	public static Comparator<SimpleData> getDateComperator() {
		Comparator<SimpleData> comp = new Comparator<SimpleData>() {

			@Override
			public int compare(SimpleData o1, SimpleData o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
			
		};
		return comp;
		
	}
	
}
