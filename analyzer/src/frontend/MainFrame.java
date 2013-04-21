package frontend;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {

	private JTabbedPane mainPanel = new JTabbedPane();
	private AccountPanel accountPanel = new AccountPanel();
	private StockPanel stockPanel = new StockPanel();
	private MenuPanel menuPanel = new MenuPanel();

	public MainFrame(){
		setJMenuBar(menuPanel);
		
		mainPanel.addTab("Stocks", stockPanel);
		mainPanel.addTab("My account", accountPanel);
		
		add(mainPanel);
		
		setTitle("StockOracle");
		setSize(new Dimension(1000, 800));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

}
