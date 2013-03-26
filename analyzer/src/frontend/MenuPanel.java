package frontend;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;


/*
 * Class that handles the menuPanel.
 */

@SuppressWarnings("serial")
public class MenuPanel extends JMenuBar implements ActionListener{
	private JMenu file = new JMenu("File");
	private JMenu edit = new JMenu("Edit");
	private JMenu help = new JMenu("Help");

	private JMenuItem exit = new JMenuItem("Exit");
	private JMenuItem settings = new JMenuItem("Settings");
	private JMenuItem about = new JMenuItem("About");


	public MenuPanel(){

		add(file);
		file.add(exit);


		add(edit);


		add(help);
		help.add(about);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}