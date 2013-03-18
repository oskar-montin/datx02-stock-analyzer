package data;

import java.util.Calendar;
import java.util.Comparator;


public class RateOfChange implements Comparable<RateOfChange>{
	private final Stock stock;
	private final Calendar date;
	private final Double rate;
	
	public RateOfChange(Stock stock, Calendar date, Double rate) {
		this.stock = stock;
		this.date = date;
		this.rate = rate;
	}
	
	public Stock getStock() {
		return stock;
	}
	
	public Calendar getDate() {
		return date;
	}
	
	public Double getRate() {
		return rate;
	}

	@Override
	public int compareTo(RateOfChange o) {
		return this.getRate().compareTo(o.getRate());
	}
	
	public int compareDateTo(RateOfChange o) {
		return this.getDate().compareTo(o.getDate());
	}
	
	public static Comparator<RateOfChange> getDateComperator() {
		Comparator<RateOfChange> comp = new Comparator<RateOfChange>() {

			@Override
			public int compare(RateOfChange o1, RateOfChange o2) {
				return o1.compareDateTo(o2);
			}
			
		};
		return comp;
		
	}
}
