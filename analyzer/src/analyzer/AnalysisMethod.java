package analyzer;

import data.Curve;

public interface AnalysisMethod {
	/**
	 * 
	 * @return a string that specified what the method indicates in words.
	 */
	public String toString();
	/**
	 * 
	 * @return the value of the method on the specific stock. If it indicates wether the stock is oerbought or not its an 
	 * integer between 0 and 100.
	 */
	public int value();
	/**
	 * 
	 * @return a set of curves that represents the graph of the method, some methods may use only one curve
	 */
	public Curve[] getGraph();
}
