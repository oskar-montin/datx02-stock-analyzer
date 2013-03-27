package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.Core;

public class SMAController implements ActionListener, IController  {
	@Override
	public void actionPerformed(ActionEvent e) {
		if(Core.getInstance().hasGraph()){
			execute();
		}else {
			System.out.println("No graph active");
		}
	}
	
	public void execute() {
		Core.getInstance().setSMA();
	}
}
