package data;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Class that implements a user and its account.
 * @author Johanna
 *
 */
public class User {
	private String name;
	private double balance, depositedAmount; //depositedAmount exist so that we can compare current value with deposited value
	private TreeMap <Stock, Integer> ownedStocks;
	private LinkedList<Transaction> historicalTransactions;

	/**
	 * Constructor for user class
	 * @param name Name of the user
	 */
	public User(String name){
		this.name = name;
		balance = 0;
		depositedAmount = 0;
		ownedStocks = new TreeMap<Stock, Integer>();
		historicalTransactions = new LinkedList<Transaction>();
	}
	
	/**
	 * @return Returns current balance
	 */
	private double getBalance(){
		return balance;	
	}
	
	/**
	 * @return Returns user name
	 */
	private String getName(){
		return name;
	}
	
	/**
	 * 
	 * @return Returns a list of previous transactions.
	 */
	private LinkedList<Transaction>getTransactionHistory(){
		return historicalTransactions;
	}
	
	/**
	 * 
	 * @return Returns a specification of the current stocks in the portfolio.
	 */
	private TreeMap getOwnedStocks(){
		return ownedStocks;
	}

	
	/**
	 * 
	 * @param stock
	 * @return Returns true if a stock is in the portfolio, false otherwise.
	 */
	private boolean ownStock(Stock stock){
		 return ownedStocks.containsKey(stock);
	}
	
	/**
	 * Tries to deduce amount from balance. Returns true if successful. 
	 * @param amount
	 * @return
	 */
	private boolean withdraw(double amount){
		if(amount <= balance){
			balance -= amount;
			depositedAmount -= amount;
			return true;
		}
		else return false;	
	}
	
	/**
	 * Calculates the value of the portfolio plus the balance.
	 * @return
	 */
	private double getTotalAssets(){
		double assets = balance;
		for (Integer value: ownedStocks.values())
				assets += value;
		return assets;
	}
	

	/**
	 * Updates balance and deposited amount after a deposit
	 * @param amount
	 */
	private void deposit(double amount){
			balance += amount;
			depositedAmount += amount;
	}
	
	
	/**
	 * Performs transaction, by updating balance, current portfolio and history
	 * @param transaction the transaction that is being performed.
	 * @return true if transaction is successful
	 */
	private boolean performTransaction(Transaction transaction){
		int currentNrOfStocks = ownedStocks.get(transaction.getData());
		double price = transaction.getData().getValue()*transaction.getAmount();
		
		if(transaction.getAmount() > 0){ //if purchase
			if(balance < price) 
				return false;
			
			else {
				currentNrOfStocks += transaction.getAmount();
				ownedStocks.put(transaction.getData().getStock(), new Integer(currentNrOfStocks));
				historicalTransactions.add(transaction);
				return true;
			}	
		}
		else if(transaction.getAmount() < 0){
			currentNrOfStocks -= transaction.getAmount();
			if(currentNrOfStocks == 0){
				ownedStocks.remove(transaction.getData().getStock());
			}
			else if(currentNrOfStocks > 0){
				ownedStocks.put(transaction.getData().getStock(), new Integer(currentNrOfStocks));
				balance += Math.abs(price);
			}
			else return false;
			historicalTransactions.add(transaction);	
			return true;
		}
		else return false;		
	}
}