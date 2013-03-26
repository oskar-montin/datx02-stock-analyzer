package frontend;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;

import data.DailyData;
import data.Stock;


public class StatusbarPanel extends JPanel {
	private DailyData stockInfo;
	private JLabel latestData;
	private JLabel currentMethods;

	public StatusbarPanel(){
		latestData = new JLabel("Latest Data:");
		currentMethods = new JLabel("Current Methods:");

		add(latestData);
		add(currentMethods);
	}
	
	public void setStock(DailyData dd){
		stockInfo = dd;
		String stock = dd.getStock().getName();
		String business = dd.getStock().getBusiness();
		String symbol = dd.getStock().getSymbol();
		String date = dd.getDate().get(Calendar.YEAR)+"-"+(dd.getDate().get(Calendar.MONTH)+1)+"-"+dd.getDate().get(Calendar.DAY_OF_MONTH);

		String PE = dd.getPE()+"";
		String PS = dd.getPS()+"";
		String openPrice = dd.getOpenPrice()+"";
		String closePrice = dd.getClosePrice()+"";
		String high = dd.getHigh()+"";
		String low = dd.getLow()+"";
		String volume = dd.getVolume()+"";

		latestData.setText("Latest Data: Name: "+stock+" Business: "+business+" Symbol: "+symbol+" Date: "+date);
		
		add(latestData);
	}
}
