package frontend;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Collections;
import java.util.PriorityQueue;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import controller.BBController;
import controller.EMAController;
import controller.MACDController;
import controller.SMAController;
import controller.StockController;

import data.Stock;


public class ToolbarPanel extends JPanel {
	private JComboBox<String> stockBox;
	private JSeparator separator;
	private JSeparator separator2;
	private JSeparator separator3;
	
	private JCheckBox SMA;
	private JTextField SMAsize;
	
	private JCheckBox EMA;
	private JTextField EMAsize;
	
	private JCheckBox MACD;
	private JTextField MACDFirstSize;
	private JTextField MACDSecondSize;
	private JTextField MACDSignalSize;
	
	private JCheckBox BB;
	
	public ToolbarPanel(PriorityQueue<Stock> stockQueue){
		setPreferredSize(new Dimension(1050, 80));
		//setBackground(new Color(250, 250, 40));
		separator = new JSeparator(JSeparator.VERTICAL);
		separator.setPreferredSize(new Dimension(5, 25));
		
		separator2 = new JSeparator(JSeparator.VERTICAL);
		separator2.setPreferredSize(new Dimension(5, 25));
		
		separator3 = new JSeparator(JSeparator.VERTICAL);
		separator3.setPreferredSize(new Dimension(5, 25));
		
		stockBox = new JComboBox<String>();
		stockBox.setPreferredSize(new Dimension(140,25));
		stockBox.setToolTipText("V�lj aktie");
		stockBox.addActionListener(new StockController());
		while(!stockQueue.isEmpty()){
			stockBox.addItem(stockQueue.poll().getSymbol());
		}
		
		
		SMA = new JCheckBox("SMA");
		SMA.setToolTipText("Enable Simple Moving Average");
		SMA.addActionListener(new SMAController());
		SMAsize = new JTextField("2");
		SMAsize.setPreferredSize(new Dimension(25, 20));
		SMAsize.setToolTipText("SMA offset");
		SMAsize.addActionListener(new SMAController());
		
		
		EMA = new JCheckBox("EMA");
		EMA.setToolTipText("Enable Exponential Moving Average");
		EMA.addActionListener(new EMAController());
		EMAsize = new JTextField("2");
		EMAsize.setPreferredSize(new Dimension(25, 20));
		SMAsize.setToolTipText("EMA offset");
		EMAsize.addActionListener(new EMAController());
		
		
		MACD = new JCheckBox("MACD");
		MACD.setToolTipText("Enable Moving Average Convergence-Divergence");
		MACD.addActionListener(new MACDController());
		
		MACDFirstSize = new JTextField("12");
		MACDFirstSize.setPreferredSize(new Dimension(25, 20));
		MACDFirstSize.setToolTipText("MACD first EMA offset");
		MACDFirstSize.addActionListener(new MACDController());
		
		MACDSecondSize = new JTextField("26");
		MACDSecondSize.setPreferredSize(new Dimension(25, 20));
		MACDSecondSize.setToolTipText("MACD second EMA offset");
		MACDSecondSize.addActionListener(new MACDController());
		
		MACDSignalSize = new JTextField("9");
		MACDSignalSize.setPreferredSize(new Dimension(25, 20));
		MACDSignalSize.setToolTipText("MACD signal-line EMA offset");
		MACDSignalSize.addActionListener(new MACDController());
		
		
		BB = new JCheckBox("Bollinger Bands");
		BB.addActionListener(new BBController());
		
		add(stockBox);
		
		add(SMA);
		add(SMAsize);
		
		add(separator);
		
		add(EMA);
		add(EMAsize);
		
		add(separator2);
		
		add(MACD);
		add(MACDFirstSize);
		add(MACDSecondSize);
		add(MACDSignalSize);
		
		add(separator3);
		
		add(BB);
	}
	
	public String getSelectedStock(){
		return (String) stockBox.getSelectedItem();
	}
	
	public boolean isSMASelected(){
		return SMA.isSelected();
	}
	
	public int getSMASize(){
		return Integer.parseInt(SMAsize.getText());
	}
	
	public boolean isEMASelected(){
		return EMA.isSelected();
	}
	
	public boolean isMACDSelected(){
		return MACD.isSelected();
	}
	
	public boolean isBBSelected(){
		return BB.isSelected();
	}
	
	public void resetAllMethods(){
		SMA.setSelected(false);
		EMA.setSelected(false);
		MACD.setSelected(false);
		BB.setSelected(false);
	}
}
