package data;

import java.util.Date;

/**
 * Datatype of the realtime data.
 * 
 * @author oskarnylen
 */

public class RTData implements MarketItem {

	private Stock stock;
	private Date date;
	private double price;
	private double orderBook; // <--- TYPE?!!=!?!?!
	
	public RTData(Stock stock, Date date, double price, double orderBook) {
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
}
