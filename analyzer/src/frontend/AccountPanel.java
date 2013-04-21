package frontend;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class AccountPanel extends JPanel{

	private JTabbedPane mainPanel = new JTabbedPane();
	private StockPortfolioPanel portfolio = new StockPortfolioPanel();
	private HomeAccountPanel home = new HomeAccountPanel();
	private TransactionHistoryPanel transactionHistory = new TransactionHistoryPanel();
	private TransferPanel transfer = new TransferPanel();
	private ResultPanel results = new ResultPanel();
	
	public AccountPanel(){
		setLayout(new BorderLayout());
		mainPanel.addTab("Home", home);
		mainPanel.addTab("Portfolio", portfolio);
		mainPanel.addTab("Results", results);
		mainPanel.addTab("Transfer", transfer);
		mainPanel.addTab("Transaction History", transactionHistory);
		
		add(mainPanel, BorderLayout.CENTER);
	}
}
