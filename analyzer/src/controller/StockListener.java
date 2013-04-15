package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;


public class StockListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		@SuppressWarnings("unchecked")
		JComboBox<String> box = (JComboBox<String>) e.getSource();
		Core.getInstance().setStock(box.getSelectedItem().toString());
	}	
}
