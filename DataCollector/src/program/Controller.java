package program;

import data.Settings;

public class Controller {
	private Settings settings;
	private static Controller instance;
	
	private Controller() {
		
	}
	
	public static Controller getInstance() {
		if(instance == null) {
			instance = new Controller();
		}
		return instance;
	}
	
	/**
	 * Adds a stock that the program should start collecting data from. This wil add it both to the database and to the settings file.
	 * @param symbol the key value of the stock
	 * @param name the name of the stock
	 * @param business the business of the company olding the stock
	 * @param stockExchange the stockexchange where the stock is listed
	 * @return
	 */
	public boolean addStock(String symbol, String name, String business, String stockExchange) {
		//Skicka saker till databasen
		return settings.addSymbol(symbol);
	}
	
	public boolean removeSymbol(String symbol) {
		return settings.removeSymbol(symbol);
	}
	
	public boolean saveSettings() {
		return settings.save();
	}
	
	public void changeWaitTime(long waitTime) {
		settings.changeWaitTime(waitTime);
	}
	
	public void collectDailyData() {
		
	}
	
	public void startRealTimeCollecting() {
		
	}
}
