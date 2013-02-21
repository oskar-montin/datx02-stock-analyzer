package program;

import java.util.Calendar;

import collector.DatabaseHandler;
import collector.YahooInterface;
import data.DailyData;
import data.QuarterlyData;
import data.RTData;
import data.Settings;
import data.Stock;

/**
 * 
 * @author oskar-montin
 *
 */
public class Controller {
	private Settings settings;
	private static Controller instance;
	private final RealTimeThread realTimeThread;
	private final ProgressInfo controllerProgress;
	
	private Controller() {
		this.settings = Settings.getSettings();
		this.realTimeThread = new RealTimeThread();
		this.controllerProgress = new ProgressInfo();
	}
	
	/**
	 * 
	 * @return the only instance of the controller, if no controller exist create a new one.
	 */
	public static Controller getInstance() {
		if(instance == null) {
			instance = new Controller();
		}
		return instance;
	}
	
	/**
	 * 
	 * @return the progress info object representing the progress of the methods in this class
	 */
	public ProgressInfo getControllerProgress() {
		return this.controllerProgress;
	}
	
	/**
	 * 
	 * @return the progress info object representing the progress of the real time collector
	 */
	public ProgressInfo getRealTimeProgress() {
		return this.realTimeThread.getProgress();
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
		Stock stock = new Stock(name, symbol, business, stockExchange);
		if(!DatabaseHandler.addStock(stock)){
			return false;
		}
		return settings.addSymbol(stock.getSymbol());
	}
	
	/**
	 * Removes the given symbol from the settings file. This will mean that the stock represented by the 
	 * symbol will no longer be collected data from. However, no previous data collected from the stock will 
	 * be removed from the database.
	 * @param symbol th symbol to remove
	 * @return true if the symbol was cotained in the settingsfile, false if it didn't exist.
	 */
	public boolean removeSymbol(String symbol) {
		return settings.removeSymbol(symbol);
	}
	
	/**
	 * Saves all settings to the file system
	 * @return true if successful
	 */
	public boolean saveSettings() {
		return settings.save();
	}
	
	/**
	 * Changed the wait time between real time collects.
	 * @param waitTime the new wait time
	 */
	public void changeWaitTime(long waitTime) {
		settings.changeWaitTime(waitTime);
	}
	
	/**
	 * Collects all daily data from all symbols in the settings file then adds the data to the database.
	 */
	public void collectDailyData() {
		this.controllerProgress.update(0, "Starting to collect daily data");
		DatabaseHandler dbh = DatabaseHandler.getInstance();
		int size = this.settings.getSymbols().size();
		int i = 0;
		for(String symbol : this.settings.getSymbols()) {
			DailyData data = YahooInterface.getDailyData(symbol);
			dbh.addDailyData(data);
			this.controllerProgress.update((i/size)*100, "Data added: "+symbol);
		}
		this.controllerProgress.update(100, "Finished. All daily data collected");
	}
	
	/**
	 * Collects all quarterly data from all symbols in the settings file then adds the data to the database.
	 */
	public void collectQuarterlyData() {
		this.controllerProgress.update(0, "Starting to collect quarterly data");
		DatabaseHandler dbh = DatabaseHandler.getInstance();
		int size = this.settings.getSymbols().size();
		int i = 0;
		for(String symbol : this.settings.getSymbols()) {
			QuarterlyData data = YahooInterface.getQuarterlyData(symbol);
			dbh.addQuarterlyData(data);
			this.controllerProgress.update((i/size)*100, "Data added: "+symbol);
		}
		this.controllerProgress.update(100, "Finished. All quarterly data collected");
	}
	
	/**
	 * Collects all real time data from all symbols in the settings file then adds the data to the database.
	 */
	public void collectRealTimeData() {
		this.controllerProgress.update(0, "Starting to collect real time data");
		DatabaseHandler dbh = DatabaseHandler.getInstance();
		int size = this.settings.getSymbols().size();
		int i = 0;
		for(String symbol : this.settings.getSymbols()) {
			RTData data = YahooInterface.getRTData(symbol);
			dbh.addRTEntry(data);
			this.controllerProgress.update((i/size)*100, "Data added: "+symbol);
		}
		this.controllerProgress.update(100, "Finished. All real time data collected");
	}
	
	/**
	 * Starts the real time thread to collect data.
	 */
	public void startRealTimeCollecting() {
		this.realTimeThread.start();
	}
	
	/**
	 * Stops the real time thread.
	 */
	public void stopRealTimeCollecting() {
		this.realTimeThread.stopRT();
	}
	
	private class RealTimeThread extends Thread{
		private final ProgressInfo progress;
		private boolean running;
		
		public RealTimeThread() {
			super();
			this.progress = new ProgressInfo();
		}
		
		public void stopRT() {
			this.running = false;
			this.progress.update(-1, "Stopped");
		}
		
		@Override
		public void run() {
			long startTime, endTime,timeDiff;
			this.running = true;
			while(this.running) {
				this.progress.update(0, "Updating realtime stock info");
				startTime = System.currentTimeMillis();
				
				collectRealTimeData();
				this.progress.update(100, "All stocks updated for time: "+
												Calendar.getInstance().get(Calendar.HOUR)+":"+
												Calendar.getInstance().get(Calendar.MINUTE));
				endTime = System.currentTimeMillis();
				timeDiff = endTime-startTime;
				if(timeDiff<settings.getWaitTime()) {
					try {
						Thread.sleep(settings.getWaitTime()-timeDiff);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}

		public ProgressInfo getProgress() {
			return progress;
		}
	}
}
