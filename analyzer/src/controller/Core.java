package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import data.Stock;

public class Core{

	private final PropertyChangeSupport observers = new PropertyChangeSupport(this);
	private static Core instance = null;
	
	
	public static Core getInstance(){
		if(instance == null){
			instance = new Core();
		}
		return instance;
	}
	
	public void addObserver(PropertyChangeListener observer) {
		observers.addPropertyChangeListener(observer);
	}
	
	public void setStock(String symbol){
		Controller.getInstance().setStock(symbol);
		observers.firePropertyChange("setStock", null, null);
	}
}
