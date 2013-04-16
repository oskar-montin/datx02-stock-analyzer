/**
 * @author Johanna
 */

package data;

public class Transaction {

	SimpleData stock;
	int amount;
	
	/**
	 * 
	 * @param stock current stock which includes closing price
	 * @param amount positive for purchases, negative for sales
	 *
	 */
	public Transaction(SimpleData stock, int amount){
		this.stock = stock;
		this.amount = amount;
	}
	
	public SimpleData getData(){
		return stock;
	}
	
	public int getAmount(){
		return amount;
		
	}
	
}
