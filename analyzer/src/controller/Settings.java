package controller;

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

import data.Stock;

public class Settings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5292410139602565332L;
	
	private static Settings instance;
	private int BBOffset = 5;
	private int CMFOffset = 5;
	private int EMAOffset = 5;
	private int SMAOffset = 5;
	private int RSIOffset = 5;
	private int ROCOffset = 5;
	private int SOShortOffset = 5;
	private int SOMidOffset = 9;
	private int SOLongOffset = 14;
	private int SOSpeedOffset = 3;
	/* Correct values
	private int MACDShortOffset = 12;
	private int MACDLongOffset = 26;
	private int MACDSignalOffset = 9;
	*/
	private int MACDShortOffset = 4;
	private int MACDLongOffset = 5;
	private int MACDSignalOffset = 3;
	
	private Settings() {
		try {
			Settings s = loadFromFile();
			BBOffset = s.getBBOffset();
			CMFOffset = s.getCMFOffset();
			EMAOffset = s.getEMAOffset();
			SMAOffset = s.getSMAOffset();
			RSIOffset = s.getRSIOffset();
			ROCOffset = s.getROCOffset();
			SOShortOffset = s.getSOShortOffset();
			SOMidOffset = s.getSOMidOffset();
			SOLongOffset = s.getSOLongOffset();
			SOSpeedOffset = s.getSOSpeedOffset();
			MACDShortOffset = s.getMACDShortOffset();
			MACDLongOffset = s.getMACDLongOffset();
			MACDSignalOffset = s.getMACDSignalOffset();
		} catch(Exception e) {
			
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

	public int getBBOffset() {
		return BBOffset;
	}

	public void setBBOffset(int bBOffset) {
		BBOffset = bBOffset;
	}
	public int getCMFOffset() {
		return CMFOffset;
	}

	public void setCMFOffset(int cMFOffset) {
		CMFOffset = cMFOffset;
	}

	public int getEMAOffset() {
		return EMAOffset;
	}

	public void setEMAOffset(int eMAOffset) {
		EMAOffset = eMAOffset;
	}

	public int getSMAOffset() {
		return SMAOffset;
	}

	public void setSMAOffset(int sMAOffset) {
		SMAOffset = sMAOffset;
	}

	public int getRSIOffset() {
		return RSIOffset;
	}

	public void setRSIOffset(int rSIOffset) {
		RSIOffset = rSIOffset;
	}

	public int getROCOffset() {
		return ROCOffset;
	}

	public void setROCOffset(int rOCOffset) {
		ROCOffset = rOCOffset;
	}

	public int getSOShortOffset() {
		return SOShortOffset;
	}

	public void setSOShortOffset(int sOShortOffset) {
		SOShortOffset = sOShortOffset;
	}

	public int getSOMidOffset() {
		return SOMidOffset;
	}

	public void setSOMidOffset(int sOMidOffset) {
		SOMidOffset = sOMidOffset;
	}

	public int getSOLongOffset() {
		return SOLongOffset;
	}

	public void setSOLongOffset(int sOLongOffset) {
		SOLongOffset = sOLongOffset;
	}

	public int getSOSpeedOffset() {
		return SOSpeedOffset;
	}

	public void setSOSpeedOffset(int sOSpeedOffset) {
		SOSpeedOffset = sOSpeedOffset;
	}

	public int getMACDShortOffset() {
		return MACDShortOffset;
	}

	public void setMACDShortOffset(int mACDShortOffset) {
		MACDShortOffset = mACDShortOffset;
	}

	public int getMACDLongOffset() {
		return MACDLongOffset;
	}

	public void setMACDLongOffset(int mACDLongOffset) {
		MACDLongOffset = mACDLongOffset;
	}

	public int getMACDSignalOffset() {
		return MACDSignalOffset;
	}

	public void setMACDSignalOffset(int mACDSignalOffset) {
		MACDSignalOffset = mACDSignalOffset;
	}

}
