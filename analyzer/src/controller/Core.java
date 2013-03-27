package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import data.Stock;

public class Core{

	private final PropertyChangeSupport observers = new PropertyChangeSupport(this);
	private static Core instance = null;
	
	private boolean hasGraph = false;
	
	public static Core getInstance(){
		if(instance == null){
			instance = new Core();
		}
		return instance;
	}
	
	public void addObserver(PropertyChangeListener observer) {
		observers.addPropertyChangeListener(observer);
	}
	
	public void setStock(){
		hasGraph = true;
		observers.firePropertyChange("setStock", null, null);
	}

	public void setSMA() {
		observers.firePropertyChange("setSMA", null, null);
	}

	public void setBB() {
		observers.firePropertyChange("setBB", null, null);
	}

	public void setEMA() {
		observers.firePropertyChange("setEMA", null, null);
	}

	public void setMACD() {
		observers.firePropertyChange("setMACD", null, null);
	}
	
	public boolean hasGraph(){
		return hasGraph;
	}
}
