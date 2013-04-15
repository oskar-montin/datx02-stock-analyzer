package frontend;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import controller.Controller;
import controller.Core;
import controller.DatabaseHandler;
import data.Result;


import analyzer.ExponentialMovingAverage;
import analyzer.MACD;
import analyzer.SimpleMovingAverage;


public class MainFrame extends JFrame implements PropertyChangeListener {

	private JTabbedPane mainPanel = new JTabbedPane();;
	private ToolbarPanel toolbar;
	private StatusbarPanel statusbar = new StatusbarPanel();
	private GraphPanel graphPanel;
	private MenuPanel menuPanel = new MenuPanel();

	private Core core = Core.getInstance();

	public MainFrame(){
		setJMenuBar(menuPanel);
		CardLayout cardLayout = new CardLayout();


		
//		mainPanel = new JPanel();
//		mainPanel.setLayout(new GridLayout(3,3));
//		mainPanel.setLayout(cardLayout);
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
			mainPanel.removeAll();
			List<Result> results = Controller.getInstance().getAnalyticsData();
			for(Result result:results) {
				if(result!=null) {
					JPanel tab = new JPanel();
					mainPanel.addTab(result.getName(), tab);
					GraphPanel p = new GraphPanel(result);
					tab.add(p.getGraphPanel());
				}
			}
			validate();
			repaint();
		}
	}
}
