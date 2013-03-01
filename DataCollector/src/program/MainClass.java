package program;

import collector.YahooInterface;
import data.DailyData;
import data.QuarterlyData;
import data.RTData;
import frontend.GUI;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// new GUI();
		
	//	RTData rtd = YahooInterface.getRTData("BAC");
		
	//	DailyData dd = YahooInterface.getDailyData("BAC");
		
		QuarterlyData orcl = YahooInterface.getQuarterlyData("ORCL");
		
		System.out.println(orcl);
	//	QuarterlyData qd = YahooInterface.getQuarterlyData("AAPL");
		
		
	}

}
