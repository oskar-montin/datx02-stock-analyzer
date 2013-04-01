package controller;

import java.awt.event.ActionEvent;


public class CMFListener implements Listener {

	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}
	
	public void execute() {
		Core.getInstance().setCMF();
	}

}