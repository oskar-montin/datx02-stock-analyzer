package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class EMAListener implements Listener {
	@Override
	public void actionPerformed(ActionEvent e) {
		if(Core.getInstance().hasGraph()){
			execute();
		}else {
			System.out.println("No graph active");
		}
		
	}
	
	public void execute() {
		Core.getInstance().setEMA();
	}
}