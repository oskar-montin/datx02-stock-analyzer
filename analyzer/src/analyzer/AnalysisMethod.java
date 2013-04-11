package analyzer;

import data.Curve;
import data.Result;

public interface AnalysisMethod {
	/**
	 * 
	 * @return a string that specified what the method indicates in words.
	 */
	public String resultString();
	/**
	 * 
	 * @return the value of the method on the specific stock. If it indicates wether the stock is oerbought or not its an 
	 * integer between 0 and 100.
	 */
	public double value();
	/**
	 * 
	 * @return a set of curves that represents the graph of the method, some methods may use only one curve
	 */
	public Curve[] getGraph();
	
	/**
	 * 
	 * @return the result object for this analysis method, to see what the value in the result means see the method value()
	 */
	public Result getResult();
}
