package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import core.Core;

import data.Stock;

public class StockController implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}
	
	public void execute() {
		Core.getInstance().setStock();
	}


	
}
