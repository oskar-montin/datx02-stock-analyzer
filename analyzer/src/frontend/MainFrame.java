package frontend;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.Core;

import analyzer.DatabaseHandler;
import analyzer.ExponentialMovingAverage;
import analyzer.MACD;
import analyzer.SimpleMovingAverage;


public class MainFrame extends JFrame implements PropertyChangeListener {

	private JPanel mainPanel;
	private ToolbarPanel toolbar;
	private StatusbarPanel statusbar = new StatusbarPanel();
	private GraphPanel graphPanel;
	private MenuPanel menuPanel = new MenuPanel();

	private Core core = Core.getInstance();

	public MainFrame(){
		setJMenuBar(menuPanel);
		
		mainPanel = new JPanel();
		toolbar = new ToolbarPanel(DatabaseHandler.getAllStocks());

		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		add(statusbar, BorderLayout.SOUTH);

		setTitle("StockOracle");
		setSize(new Dimension(800, 600));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		core.addObserver(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if(evt.getPropertyName().equals("setStock")){
			if(graphPanel != null){
				mainPanel.remove(graphPanel.getGraphPanel());
			}
			String symbol = toolbar.getSelectedStock();
			graphPanel = new GraphPanel(DatabaseHandler.getDailyData(DatabaseHandler.getStock(symbol)));
			statusbar.setStock(DatabaseHandler.getDailyData(DatabaseHandler.getStock(symbol)).poll());
			mainPanel.add(graphPanel.getGraphPanel());
			toolbar.resetAllMethods();
			validate();
			repaint();
		}
		
		if(evt.getPropertyName().equals("setSMA")){
			if(toolbar.isSMASelected() == true){
				String symbol = toolbar.getSelectedStock();
				int size = toolbar.getSMASize();
				SimpleMovingAverage sma = new SimpleMovingAverage(DatabaseHandler.getDailyData(DatabaseHandler.getStock(symbol)), size);
				graphPanel.addMethod(sma.getMovingAverage(), "SMA");
			}else{
				System.out.println("SMA false");
				graphPanel.deleteMethod("SMA");
			}
		}
		
		if(evt.getPropertyName().equals("setEMA")){
			if(toolbar.isEMASelected() == true){
				String symbol = toolbar.getSelectedStock();
				int size = toolbar.getSMASize();
				ExponentialMovingAverage ema = new ExponentialMovingAverage(DatabaseHandler.getDailyData(DatabaseHandler.getStock(symbol)),size);
				graphPanel.addMethod(ema.getMovingAverage(), "EMA");
			}else{
				System.out.println("EMA false");
				graphPanel.deleteMethod("EMA");
			}
		}
		
		if(evt.getPropertyName().equals("setMACD")){
			if(toolbar.isMACDSelected() == true){
				System.out.println("MACD true");
				String symbol = toolbar.getSelectedStock();
				MACD macd = new MACD(DatabaseHandler.getDailyData(DatabaseHandler.getStock(symbol))
						,toolbar.getFirstMACDSize()
						,toolbar.getSecondMACDSize()
						,toolbar.getSignalSize());
				graphPanel = new GraphPanel(macd.getMACD(), 
							DatabaseHandler.getStock(symbol).getName() + " MACD");
				graphPanel.addMethod(macd.getSignal(), "MACD");
				
				mainPanel.add(graphPanel.getGraphPanel());
				validate();
				repaint();
			}else{
				mainPanel.remove(graphPanel.getGraphPanel());
				System.out.println("MACD false");
				validate();
				repaint();
			}
		}
		
		if(evt.getPropertyName().equals("setBB")){
			if(toolbar.isBBSelected() == true){
				System.out.println("BB true");
			}else{
				System.out.println("BB false");
			}
		}

	}
}
