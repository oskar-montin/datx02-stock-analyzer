package collector;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.PriorityQueue;

import data.DailyData;
import data.Stock;

public class QuoteStockerParser {

	private int monthToNumber(String month){
		if(month.equals("01")){
			return 0;
		}
		if(month.equals("02")){
			return 1;
		}
		if(month.equals("03")){
			return 2;
		}
		if(month.equals("04")){
			return 3;
		}
		if(month.equals("05")){
			return 4;
		}
		if(month.equals("06")){
			return 5;
		}
		if(month.equals("07")){
			return 6;
		}
		if(month.equals("08")){
			return 7;
		}
		if(month.equals("09")){
			return 8;
		}
		if(month.equals("10")){
			return 9;
		}
		if(month.equals("11")){
			return 10;
		}
		if(month.equals("12")){
			return 11;
		}
		return -1;
	}

	public void reader(){
		ArrayList<String> pq = new ArrayList<String>();
		pq.add("UNH");
		pq.add("UTX");
		pq.add("VLO");
		pq.add("VZ");
		pq.add("WMT");
		pq.add("WLP");
		pq.add("XOM");
		pq.add("YUM");
		
		for(int i = 0; i < pq.size(); i++){
			try{
				// Open the file that is the first 
				// command line parameter
				FileInputStream fstream = new FileInputStream("C:/StockData/" + pq.get(i) + ".CSV");
				// Get the object of DataInputStream
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				//Read File Line By Line
				String symbol = "";
				double close;
				double open;
				double high;
				double low;
				long volume;

				Calendar calendar;
				while ((strLine = br.readLine()) != null)   {

					String[] splittedString;

					splittedString = strLine.split(",");

					symbol = splittedString[0];


					int year;
					int month;
					int date;
					String dateString;

					dateString = splittedString[2];

					open = Double.parseDouble(splittedString[3]);
					high = Double.parseDouble(splittedString[4]);
					low = Double.parseDouble(splittedString[5]);
					close = Double.parseDouble(splittedString[6]);
					volume = Long.parseLong(splittedString[7]);

					DailyData data;


					calendar = Calendar.getInstance();


					year = Integer.parseInt(dateString.substring(0, 4));


					month = monthToNumber(dateString.substring(4, 6));


					date = Integer.parseInt(dateString.substring(6, 8));


					calendar.set(year, month, date);

					System.out.println("ÅR: " + calendar.get(Calendar.YEAR)+
							" MÅNAD: " + calendar.get(Calendar.MONTH)+
							" DAG: " + calendar.get(Calendar.DATE));

					data = new DailyData(DatabaseHandler.getStock(symbol), calendar, open, close, high, low, volume);
					System.out.println(data);
					DatabaseHandler.addDailyData(data);
				}


				//System.out.println(splittedString[0]);

				//Close the input stream
				in.close();
			}catch (Exception e){//Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
		}
	}
}

