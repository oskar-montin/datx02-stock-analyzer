package data;

import java.util.Calendar;

/**
 * Datatype of the realtime data.
 * 
 * @author oskarnylen
 */

public class RTData implements MarketItem {


	private Stock stock;
	private Calendar date;
	private double price;
	private double orderBook; // <--- TYPE?!!=!?!?!
	
	/**
	 * 
	 * @param stock
	 * @param date
	 * @param price
	 * @param orderBook
	 */
	public RTData(Stock stock, Calendar date, double price, double orderBook) {
		super();
		this.stock = stock;
		this.date = date;
		this.price = price;
		this.orderBook = orderBook;
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
		return "RTData [stock=" + stock + ", date=" + date + ", price=" + price
				+ ", orderBook=" + orderBook + "]";
	}
	
	public Stock getStock() {
		return stock;
	}

	public Calendar getDate() {
		return date;
	}

	public double getPrice() {
		return price;
	}

	public double getOrderBook() {
		return orderBook;
	}

}
