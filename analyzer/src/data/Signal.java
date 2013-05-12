package data;

public enum Signal {
	BUY("Buy",1), SELL("Sell",-1), NONE("Keep",0);
	private String string;
	private int value;
	Signal(String string, int value) {
		this.string = string;
		this.value = value;
	}
	public String getString() {
		return this.string;
	}
	public int getValue() {
		return this.value;
	}
}
