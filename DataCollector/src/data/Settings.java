package data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;

import collector.DatabaseHandler;

/**
 * 
 * @author oskar-montin
 *
 */
public class Settings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5292410139602565332L;
	
	private static Settings instance;
	private LinkedList<String> symbols = new LinkedList<String>();
	private long waitTime;
	private Calendar dailyUpdateTime;

	
	private Settings() {
		
		PriorityQueue<Stock> queue = DatabaseHandler.getAllStocks();
		
		/**
		 * Add already existing stocks into the settings file.
		 */
		while(queue.size() != 0){
			this.addSymbol((queue.poll().getSymbol()));
		}
		
		try {
			Settings s = loadFromFile();
			this.dailyUpdateTime = s.getDailyUpdateTime();
			this.symbols = s.getSymbols();
			this.waitTime = s.waitTime;
			
		} catch(Exception e) {
			Calendar date = Calendar.getInstance();
			date.set(0,0,0,12,0);
			this.dailyUpdateTime = date;
			this.waitTime = 60*15*1000;
		}
	}
	
	public static Settings getSettings() {
		if(instance == null) {
			instance = new Settings();
		}
		return instance;
		
	}
	
	/**
	 * Saves the Settings object to the settings file in the file system.
	 * @return true if successful
	 */
	public boolean save() {
		try{
			OutputStream file = new FileOutputStream( "settings.dat" );
			OutputStream buffer = new BufferedOutputStream( file );
			ObjectOutput output = new ObjectOutputStream( buffer );
			try{
				output.writeObject(getSettings());
			}
			finally{
				output.close();
			}
		}  
		catch(IOException ex){
			return false;
		}
		return true;
		
	}
	
	private Settings loadFromFile() {
		Settings s;
		try{
			InputStream file = new FileInputStream( "settings.dat" );
			InputStream buffer = new BufferedInputStream( file );
			ObjectInput input = new ObjectInputStream ( buffer );
			try{
				s = (Settings) input.readObject();
			}
			finally{
				input.close();
			}
			return s;
		}
		catch(ClassNotFoundException ex){
			//fLogger.log(Level.SEVERE, "Cannot perform input. Class not found.", ex);
		}
		catch(IOException ex){
			//fLogger.log(Level.SEVERE, "Cannot perform input.", ex);
		}
		return null;
		
	}
	
	/**
	 * Adds a symbol to be used in the yahoo interface to collect data from.
	 * @param symbol 
	 * @return true if the symbol wasn't already in the list
	 */
	public boolean addSymbol(String symbol) {
		return symbols.add(symbol);
	}
	
	/**
	 * Removes a symbol that was used in the yahoo interface to collect data from
	 * @param symbol
	 * @return true if the list contained the symbol 
	 */
	public boolean removeSymbol(String symbol) {
		return symbols.remove(symbol);
	}
	
	/**
	 * Change the waittime between the collection points in the real time collection
	 * @param waitTime
	 */
	public void changeWaitTime(long waitTime) {
		if(waitTime<=0) {
			throw new IllegalArgumentException("Tried to change the wait time to <=0");
		}
		this.waitTime = waitTime;
	}
	
	/**
	 * 
	 * @return a list iterator over the symbol list
	 */
	public ListIterator<String> getSymbolIterator() {
		return symbols.listIterator();
	}
	
	/**
	 * 
	 * @return a list of all the symbols
	 */
	public LinkedList<String> getSymbols() {
		return this.symbols;
	}

	public long getWaitTime() {
		return waitTime;
	}

	public Calendar getDailyUpdateTime() {
		return dailyUpdateTime;
	}

	public void setDailyUpdateTime(Calendar dailyUpdateTime) {
		this.dailyUpdateTime = dailyUpdateTime;
	}
	
	
}
