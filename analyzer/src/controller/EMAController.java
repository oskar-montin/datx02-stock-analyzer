package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.Core;

public class EMAController implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}
	
	public void execute() {
		Core.getInstance().setEMA();
	}
}