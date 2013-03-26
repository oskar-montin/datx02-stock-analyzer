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
		}
		
		if(evt.getPropertyName().equals("setSMA")){
			if(toolbar.isSMASelected() == true){
				System.out.println("SMA true");
				String symbol = toolbar.getSelectedStock();
				int size = toolbar.getSMASize();
				SimpleMovingAverage sma = new SimpleMovingAverage(DatabaseHandler.getDailyData(DatabaseHandler.getStock(symbol)), size);
				System.out.println("SMA before chart: " + sma.getMovingAverage());
				graphPanel.addMethod(sma.getMovingAverage(), "SMA");
			}else{
				System.out.println("SMA false");
				graphPanel.deleteMethod("SMA");
			}
		}
		
		if(evt.getPropertyName().equals("setEMA")){
			if(toolbar.isEMASelected() == true){
				System.out.println("EMA true");
				String symbol = toolbar.getSelectedStock();
				ExponentialMovingAverage ema = new ExponentialMovingAverage(DatabaseHandler.getDailyData(DatabaseHandler.getStock(symbol)),2);
				System.out.println(ema.getMovingAverage());
				graphPanel.addMethod(ema.getMovingAverage(), "EMA");
			}else{
				System.out.println("EMA false");
			}
		}
		
		if(evt.getPropertyName().equals("setMACD")){
			if(toolbar.isMACDSelected() == true){
				System.out.println("MACD true");
			}else{
				System.out.println("MACD false");
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
