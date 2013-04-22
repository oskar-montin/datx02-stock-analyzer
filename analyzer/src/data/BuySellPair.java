package data;

public class BuySellPair {
	private final boolean success;
	private final double profit;
	
	public BuySellPair(SimpleData buy, SimpleData sell) {
		profit = sell.getValue()-buy.getValue();
		success = this.profit>0?true:false;
	}
	
	public boolean succeeded() {
		return success;
	}
	
	public double profit() {
		return this.profit;
	}
}
