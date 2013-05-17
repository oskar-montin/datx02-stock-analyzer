package data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Class that implements a user and its account.
 * @author Johanna
 *
 */
public class User {
	private String name;
	private double balance, depositedAmount; //depositedAmount exist so that we can compare current value with deposited value
	private TreeMap <SimpleData, Integer> ownedStocks;
	private LinkedList<Transaction> historicalTransactions;

	/**
	 * Constructor for user class
	 * @param name Name of the user
	 */
	public User(String name){
		this.name = name;
		balance = 0;
		depositedAmount = 0;
		ownedStocks = new TreeMap<SimpleData, Integer>();
		historicalTransactions = new LinkedList<Transaction>();
	}
	
	/**
	 * @return Returns current balance
	 */
	public double getBalance(){
		return balance;	
	}
	
	/**
	 * @return Returns user name
	 */
	
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 * @return Returns a list of previous transactions.
	 */
	
	public LinkedList<Transaction>getTransactionHistory(){
		return historicalTransactions;
	}
	
	/**
	 * 
	 * @return Returns a specification of the current stocks in the portfolio.
	 */
	
	public TreeMap<SimpleData, Integer> getOwnedStocks(){
		return ownedStocks;
	}

	
	/**
	 * 
	 * @param stock
	 * @return Returns true if a stock is in the portfolio, false otherwise.
	 */
	
	public boolean ownStock(Stock stock){
		 return ownedStocks.containsKey(stock);
	}
	
	/**
	 * Tries to deduce amount from balance. Returns true if successful. 
	 * @param amount
	 * @return
	 */
	
	public boolean withdraw(double amount){
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
	
	public double getTotalAssets(){
		double assets = balance;
		for(Entry<SimpleData,Integer> entry:ownedStocks.entrySet()) {
			assets += entry.getValue()*entry.getKey().getValue();
		}
		return assets;
	}
	

	/**
	 * Updates balance and deposited amount after a deposit
	 * @param amount
	 */
	
	public void deposit(double amount){
			balance += amount;
			depositedAmount += amount;
	}
	
	public double getDepositedAmount() {
		return depositedAmount;
	}
	
	
	/**
	 * Performs transaction, by updating balance, current portfolio and history
	 * @param transaction the transaction that is being performed.
	 * @return true if transaction is successful
	 */
	
	public boolean performTransaction(Transaction transaction){
		Stock stock = transaction.getData().getStock();
		int currentNrOfStocks = amountOfStocks(stock);
		double totalPrice = transaction.getValue();
		
		if(transaction.getAmount() > 0){ //if purchase
			if(balance < totalPrice)  {
				return false;
			}
			else {
				currentNrOfStocks += transaction.getAmount();
				ownedStocks.put(transaction.getData(), transaction.getAmount());
				historicalTransactions.add(transaction);
				balance -= Math.abs(totalPrice);
				return true;
			}	
		}
		
		else if(transaction.getAmount() < 0){
			if(removeStocks(stock, Math.abs(transaction.getAmount()))) {
				balance += Math.abs(totalPrice);
			} else {
				return false;
			}
			historicalTransactions.add(transaction);	
			return true;
		}
		else {
			return false;		
		}
	}

	public boolean removeStocks(Stock stock, int amount) {
		if(amountOfStocks(stock)<amount) {
			return false;
		}
		List<Entry<SimpleData,Integer>> entrySet = new LinkedList<Entry<SimpleData,Integer>>(ownedStocks.entrySet());
		for(Entry<SimpleData,Integer> entry: entrySet) {
			if(entry.getKey().getStock().compareTo(stock)==0) {
				if(entry.getValue()<=amount) {
					amount-=entry.getValue();
				} else {
					Integer newAmount = new Integer(entry.getValue()-amount);
					ownedStocks.put(entry.getKey(), newAmount);
					amount = 0;
				}
				ownedStocks.remove(entry.getKey());
				if(amount <= 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int amountOfStocks(Stock stock) {
		int amount = 0;
		for(Entry<SimpleData,Integer> entry: ownedStocks.entrySet()) {
			//System.out.println(entry.getKey().getStock().getSymbol());
			if(entry.getKey().getStock().compareTo(stock)==0) {
				amount+=entry.getValue();
			}
		}
		return amount;
	}
	
	public double getProfit() {
		return this.balance-this.depositedAmount;
	}
}