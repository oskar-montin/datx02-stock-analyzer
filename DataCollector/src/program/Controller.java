package program;

import java.io.IOException;
import java.util.Calendar;

import collector.DatabaseHandler;
import collector.YahooInterface;
import data.DailyData;
import data.Settings;
import data.Stock;

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
	
	public static Controller getInstance() {
		if(instance == null) {
			instance = new Controller();
		}
		return instance;
	}
	
	public ProgressInfo getControllerProgress() {
		return this.controllerProgress;
	}
	
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
		this.controllerProgress.update(0, "Starting to collect daily data");
		DatabaseHandler dbh = DatabaseHandler.getInstance();
		int size = this.settings.getSymbols().size();
		int i = 0;
		for(String symbol : this.settings.getSymbols()) {
			//DailyData data = YahooInterface.getDailyData(symbol);
			//dbh.addDailyData(data);
			this.controllerProgress.update((i/size)*100, "Data added: "+symbol);
		}
	}
	
	public void collectQuarterlyData() {
		
	}
	
	public void collectRealTimeData() {
		
	}
	
	public void startRealTimeCollecting() {
		this.realTimeThread.start();
	}
	
	private class RealTimeThread extends Thread{
		private final ProgressInfo progress;
		
		public RealTimeThread() {
			super();
			this.progress = new ProgressInfo();
		}
		
		@Override
		public void run() {
			long startTime, endTime,timeDiff;
			while(true) {
				this.progress.setValue(0);
				this.progress.setMessage("Updating realtime stock info");
				startTime = System.currentTimeMillis();
				
				collectRealTimeData();
				
				this.progress.setValue(100);
				this.progress.setMessage("All stocks updated for time: "+
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
