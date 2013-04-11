package frontend;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Core;
import controller.DatabaseHandler;


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
			validate();
			repaint();
		}
	}
}
