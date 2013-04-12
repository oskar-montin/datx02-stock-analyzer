package data;

public enum Signal {
	BUY("Buy"), SELL("Sell"), NONE("Keep");
	private String string;
	Signal(String string) {
		this.string = string;
	}
	public String getString() {
		return this.string;
	}
}
