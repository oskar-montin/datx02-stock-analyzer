package frontend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.*;

import program.Controller;

/**A simple GUI. 
 * 
 * May break this up into more classes as required, this is
 * to be decided.
 * 
 * 
 * @author Axner
 * 
 * 
 */
public class GUI extends JFrame implements ActionListener {
	private Controller controller;
	private DefaultListModel<String> addListModel, removeListModel, symbolListModel;
	private ArrayList<ArrayList<String>> itemsToBeAdded = new ArrayList<ArrayList <String>>();
	private JFrame editStocksFrame, addStockFrame;
	private JTextField nameField, symbolField, businessField, stockExchangeField;
	private JList<String> symbolList, addList, removeList;
	private JPanel mainPanel, addStockPanel, editStocksPanel;
	private JLabel symbolLabel, nameLabel, businessLabel,stockExchangeLabel, symbolListLabel,
					addListLabel, removeListLabel;
	private JScrollPane symbolScrollPane, addScrollPane, removeScrollPane;
	private JMenuBar menuBar;
	private JMenu file, settings, help, about;
	private JMenuItem editStocksItem, setWaitTimeItem,
						exitItem, helpItem, aboutItem;
	private JButton collectRealTimeDataButton, collectDailyDataButton,
						collectQuarterlyDataButton, addStockButton,
						finishButton,cancelButton, removeButton,
						doneButton, addAnotherButton;
	      
	public GUI() {
		
		super("Data Collector");
		this.controller = Controller.getInstance();
		setupMainFrame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == editStocksItem) {
			if(editStocksFrame == null) {
				setupStocksFrame();
			}
			else {
				editStocksFrame.setVisible(true);
			}
		}
		else if(e.getSource() == setWaitTimeItem) {
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
		else if(e.getSource() == collectDailyDataButton) {
			controller.collectDailyData();
		}
		else if(e.getSource() == collectQuarterlyDataButton) {
			controller.collectQuarterlyData();
		}
		else if(e.getSource() == collectRealTimeDataButton) {
			if(collectRealTimeDataButton.getText() == "Collect real-time data"){
				controller.startRealTimeCollecting();
				collectRealTimeDataButton.setText("Stop real-time data");
			}
			else {
				controller.stopRealTimeCollecting();
				collectRealTimeDataButton.setText("Collect real-time data");
			}
		}
		
		else if(e.getSource() == addStockButton) {
			
			if(addStockFrame == null) {
				setupAddStockFrame();
			}
			else {
				addStockFrame.setVisible(true);
			}	
		}
		else if(e.getSource() == finishButton) {
			for(ArrayList<String> s: itemsToBeAdded){
				controller.addStock(s.get(0), s.get(1), s.get(2), s.get(3));
			}
			for(int i = 0; i<removeListModel.getSize(); i++){
				controller.removeSymbol(removeListModel.get(i));
			}
			clearListModels();
			controller.saveSettings();
			convertList(controller.getSymbols());
			editStocksFrame.setVisible(false);
		}
		else if(e.getSource() == cancelButton) {
			clearListModels();
			editStocksFrame.setVisible(false);
		}
		else if(e.getSource() == removeButton) {
			if(!addList.isSelectionEmpty()){
				int[] selectedItems = (addList.getSelectedIndices());
				for(int i = selectedItems.length-1; i>-1; i--){
					addListModel.remove(selectedItems[i]);
				}
			}
			else if(!removeList.isSelectionEmpty()){
				int[] selectedItems = (removeList.getSelectedIndices());
				for(int i = selectedItems.length-1; i>-1; i--){
					removeListModel.remove(selectedItems[i]);
				}
			}
			else if(!symbolList.isSelectionEmpty()){
				int[] selectedItems = (symbolList.getSelectedIndices());
				for(int i = 0 ; i<selectedItems.length; i++){
					if(!removeListModel.contains(symbolListModel.getElementAt(selectedItems[i]))){
						removeListModel.add(0,symbolListModel.getElementAt(selectedItems[i]));
					}
				}
			}
		}
		else if(e.getSource() == doneButton) {
			itemsToBeAdded.add(stockDetails());
			addListModel.add(0,symbolField.getText());	
			clearTextFields();
			addStockFrame.setVisible(false);
		}
		else if(e.getSource() == addAnotherButton) {
			if(!addListModel.contains(symbolField.getText())){
				itemsToBeAdded.add(stockDetails());
				addListModel.add(0,symbolField.getText());
				clearTextFields();
			}
		}
	}
	public void setupMainFrame() {
		
		mainPanel = new JPanel();
		collectDailyDataButton = new JButton("Collect daily data");
		collectRealTimeDataButton = new JButton("Collect real-time data");
		collectQuarterlyDataButton = new JButton("Collect quarterly data");
		menuBar = new JMenuBar();
		file = new JMenu("File");
		settings = new JMenu("Settings");
		help = new JMenu("Help");
		about = new JMenu("About");
		
		editStocksItem = new JMenuItem("Edit stocks in database");
		setWaitTimeItem = new JMenuItem("Set RT wait-time");
		exitItem = new JMenuItem("Exit");
		helpItem = new JMenuItem("FAQ");
		aboutItem = new JMenuItem("Version");
		
		editStocksItem.addActionListener(this);
		setWaitTimeItem.addActionListener(this);
		exitItem.addActionListener(this);
		helpItem.addActionListener(this);
		aboutItem.addActionListener(this);
		collectDailyDataButton.addActionListener(this);
		collectRealTimeDataButton.addActionListener(this);
		collectQuarterlyDataButton.addActionListener(this);
		
		file.setMnemonic('F');
		settings.setMnemonic('S');
		help.setMnemonic('H');
		about.setMnemonic('A');
		editStocksItem.setMnemonic('N');
		setWaitTimeItem.setMnemonic('R');
		exitItem.setMnemonic('X');
		
		this.add(mainPanel);
		mainPanel.add(collectRealTimeDataButton);
		mainPanel.add(collectDailyDataButton);
		mainPanel.add(collectQuarterlyDataButton);
		file.add(editStocksItem);
		settings.add(setWaitTimeItem);	
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
	public void setupStocksFrame() {
		
		symbolListModel = new DefaultListModel<String>();
		convertList(controller.getSymbols());
		symbolList = new JList<String>(symbolListModel);
		addListModel = new DefaultListModel<String>();
		addList = new JList<String>(addListModel);
		removeListModel = new DefaultListModel<String>();
		removeList = new JList<String>(removeListModel);
		editStocksFrame = new JFrame("Edit Stocks");
		editStocksPanel = new JPanel();
		editStocksPanel.setLayout(null);
		addStockButton = new JButton("Add stock");
		finishButton = new JButton("Finish");
		cancelButton = new JButton("Cancel");
		removeButton = new JButton("Remove stock");
		symbolListLabel = new JLabel("Existing stocks:");
		addListLabel = new JLabel("To be added:");
		removeListLabel = new JLabel("To be removed:");
		symbolScrollPane = new JScrollPane(symbolList);
		addScrollPane = new JScrollPane(addList);
		removeScrollPane = new JScrollPane(removeList);
		
		addStockButton.addActionListener(this);
		finishButton.addActionListener(this);
		removeButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		editStocksFrame.add(editStocksPanel);
		editStocksPanel.add(addStockButton);
		editStocksPanel.add(finishButton);
		editStocksPanel.add(cancelButton);
		editStocksPanel.add(removeButton);
		//editStocksPanel.add(symbolList);
		//editStocksPanel.add(removeList);
		//editStocksPanel.add(addList);
		editStocksPanel.add(removeListLabel);
		editStocksPanel.add(addListLabel);
		editStocksPanel.add(symbolListLabel);
		editStocksPanel.add(symbolScrollPane);
		editStocksPanel.add(addScrollPane);
		editStocksPanel.add(removeScrollPane);
		
		//symbolList.setBounds(12,30,120,200);
		//addList.setBounds(267,30,120,89);
		//removeList.setBounds(267,141,120,89);
		addStockButton.setBounds(142,160,115,30);
		removeButton.setBounds(142,200,115,30);
		finishButton.setBounds(175,284,100,30);
		cancelButton.setBounds(285,284,100,30);
		removeListLabel.setBounds(267,121,100,20);
		addListLabel.setBounds(267,10,100,20);
		symbolListLabel.setBounds(12,10,100,20);
		symbolScrollPane.setBounds(12,30,120,200);
		addScrollPane.setBounds(267,30,120,89);
		removeScrollPane.setBounds(267,141,120,89);
		
		editStocksFrame.setLocationRelativeTo(null);
		editStocksFrame.setSize(405, 355);
		editStocksFrame.setResizable(false);
		editStocksFrame.setVisible(true);
	}	
	
	public void setupAddStockFrame(){
		addStockFrame = new JFrame("Add Stock");
		addStockPanel = new JPanel();
		addStockPanel.setLayout(null);
		
		nameField = new JTextField();
		symbolField = new JTextField();
		businessField = new JTextField();
		stockExchangeField = new JTextField();
		nameLabel = new JLabel("Name:");
		symbolLabel = new JLabel("Symbol:");
		businessLabel = new JLabel("Business:");
		stockExchangeLabel = new JLabel("Stock Exchange:");
		doneButton = new JButton("Done");
		addAnotherButton = new JButton("Add Another");
		
		addAnotherButton.addActionListener(this);
		doneButton.addActionListener(this);
		
		addStockFrame.add(addStockPanel);
		addStockPanel.add(nameField);
		addStockPanel.add(symbolField);
		addStockPanel.add(businessField);
		addStockPanel.add(stockExchangeField);
		addStockPanel.add(nameLabel);
		addStockPanel.add(symbolLabel);
		addStockPanel.add(businessLabel);
		addStockPanel.add(stockExchangeLabel);
		addStockPanel.add(doneButton);
		addStockPanel.add(addAnotherButton);
		
		nameLabel.setBounds(10,20,110,20);
		symbolLabel.setBounds(10,60,110,20);
		businessLabel.setBounds(10,100,110,20);
		stockExchangeLabel.setBounds(10,140,110,20);
		nameField.setBounds(160,20,110,25);
		symbolField.setBounds(160,60,110,25);
		businessField.setBounds(160,100,110,25);
		stockExchangeField.setBounds(160,140,110,25);
		addAnotherButton.setBounds(15,194,120,30);
		doneButton.setBounds(155,194,120,30);
		
		addStockFrame.setLocationRelativeTo(null);
		addStockFrame.setSize(300, 280);
		addStockFrame.setResizable(false);
		addStockFrame.setVisible(true);
	}
	
	private void clearTextFields(){
		nameField.setText("");
		symbolField.setText("");
		businessField.setText("");
		stockExchangeField.setText("");
	}
	
	private void convertList (LinkedList<String> list){
		symbolListModel.clear();
		for(String s : list){
			symbolListModel.add(0,s);
		}
	}
	private ArrayList<String> stockDetails(){
		ArrayList<String> i = new ArrayList<String>();
		i.add(symbolField.getText());
		i.add(nameField.getText());
		i.add(businessField.getText());
		i.add(stockExchangeField.getText());
		return i;
	}
	private void clearListModels(){
		addListModel.clear();
		removeListModel.clear();
		itemsToBeAdded.clear();
	}
}
