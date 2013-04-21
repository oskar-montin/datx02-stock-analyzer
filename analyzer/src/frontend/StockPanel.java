package frontend;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import controller.Controller;
import controller.Core;
import controller.DatabaseHandler;
import data.Result;

public class StockPanel extends JPanel implements PropertyChangeListener {

	private JTabbedPane mainPanel = new JTabbedPane();
	private ToolbarPanel toolbar;
	private StatusbarPanel statusbar = new StatusbarPanel();

	private Core core = Core.getInstance();

	public StockPanel(){

		toolbar = new ToolbarPanel(DatabaseHandler.getAllStocks());
		
		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		add(statusbar, BorderLayout.SOUTH);
		
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
