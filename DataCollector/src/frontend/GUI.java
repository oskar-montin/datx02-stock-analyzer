package frontend;

import java.awt.event.ActionEvent;
/**A simple, not so functional(yet) GUI. A lot of dead functions
 * that requires implementation. 
 * 
 * May break this up into more classes as required, this is
 * to be decided.
 * 
 * 
 * @author Axner
 * 
 * 
 */
import java.awt.event.ActionListener;
import javax.swing.*;

public class GUI extends JFrame implements ActionListener {
	
	private JFrame addStockFrame;
	private JTextField nameField, symbolField, businessField;
	private JPanel mainPanel, addStockPanel;
	private JLabel symbolLabel, nameLabel, businessLabel;
	private JMenuBar menuBar;
	private JMenu file, settings, help, about;
	private JMenuItem newStockItem, setFrequencyItem,
						exitItem, helpItem, aboutItem;
	private JButton collectDataButton, addStockButton, addStockAddMoreButton;
	
	public GUI() {
		
		super("Data Collector");
		setupMainFrame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == newStockItem) {
			if( addStockFrame == null) {
				newStockFrame();
			}
			else {
				addStockFrame.setVisible(true);
			}
		}
		else if(e.getSource() == setFrequencyItem) {
			// Set Frequency
		}
		else if(e.getSource() == exitItem) {
			System.exit(0);
			// Exit System
		}
		else if(e.getSource() == helpItem) {
			JOptionPane.showMessageDialog(null, "No help available, the end is upon us.");
		}
		else if(e.getSource() == aboutItem) {
			JOptionPane.showMessageDialog(null, "Version 1.0");
		}
		else if(e.getSource() == collectDataButton) {
			// Collect Data
		}
		else if(e.getSource() == addStockButton) {
			// add to database
			
			nameField.setText("");
			symbolField.setText("");
			businessField.setText("");
			addStockFrame.setVisible(false);
		}
		else if(e.getSource() == addStockAddMoreButton) {
			// add to database
			nameField.setText("");
			symbolField.setText("");
			businessField.setText("");
		}
	}
	public void setupMainFrame() {
		
		mainPanel = new JPanel();
		collectDataButton = new JButton("Collect Data");
		menuBar = new JMenuBar();
		file = new JMenu("File");
		settings = new JMenu("Settings");
		help = new JMenu("Help");
		about = new JMenu("About");
		
		newStockItem = new JMenuItem("Add Stock to Database");
		setFrequencyItem = new JMenuItem("Set RT frequency");
		exitItem = new JMenuItem("Exit");
		helpItem = new JMenuItem("FAQ");
		aboutItem = new JMenuItem("Version");
		
		newStockItem.addActionListener(this);
		setFrequencyItem.addActionListener(this);
		exitItem.addActionListener(this);
		helpItem.addActionListener(this);
		aboutItem.addActionListener(this);
		collectDataButton.addActionListener(this);
		
		file.setMnemonic('F');
		settings.setMnemonic('S');
		help.setMnemonic('H');
		about.setMnemonic('A');
		newStockItem.setMnemonic('N');
		setFrequencyItem.setMnemonic('R');
		exitItem.setMnemonic('X');
		
		this.add(mainPanel);
		mainPanel.add(collectDataButton);
		file.add(newStockItem);
		settings.add(setFrequencyItem);	
		file.add(exitItem);
		help.add(helpItem);
		about.add(aboutItem);
		menuBar.add(file);
		menuBar.add(settings);
		menuBar.add(help);
		menuBar.add(about);
		
		this.setJMenuBar(menuBar);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600,400);
		this.setResizable(false);
		this.setVisible(true);
	}
	public void newStockFrame() {
		
		addStockFrame = new JFrame("Add Stock");
		addStockPanel = new JPanel();
		addStockPanel.setLayout(null);
		addStockButton = new JButton("Add stock");
		addStockAddMoreButton = new JButton("Add + New");
		nameField = new JTextField();
		symbolField = new JTextField();
		businessField = new JTextField();
		nameLabel = new JLabel("Name:");
		nameLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		symbolLabel = new JLabel("Symbol:");
		symbolLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		businessLabel = new JLabel("Business:");
		businessLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		
		addStockButton.addActionListener(this);
		addStockAddMoreButton.addActionListener(this);
		
		addStockFrame.add(addStockPanel);
		addStockPanel.add(addStockButton);
		addStockPanel.add(addStockAddMoreButton);
		addStockPanel.add(nameField);
		addStockPanel.add(symbolField);
		addStockPanel.add(businessField);
		addStockPanel.add(nameLabel);
		addStockPanel.add(symbolLabel);
		addStockPanel.add(businessLabel);
		
		nameLabel.setBounds(10,40,70,20);
		symbolLabel.setBounds(10,80,70,20);
		businessLabel.setBounds(10,120,70,20);
		nameField.setBounds(70,40,90,25);
		symbolField.setBounds(70,80,90,25);
		businessField.setBounds(70,120,90,25);
		addStockButton.setBounds(170,74,120,30);
		addStockAddMoreButton.setBounds(170,114,120,30);
		
		addStockFrame.setLocationRelativeTo(null);
		addStockFrame.setSize(330, 200);
		addStockFrame.setResizable(false);
		addStockFrame.setVisible(true);
	}	
}
