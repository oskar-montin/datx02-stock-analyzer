package collector;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import data.DailyData;
import data.Stock;

public class ReutersFileParser {

	private int monthToNumber(String month){
		if(month.equals("Jan")){
			return 0;
		}
		if(month.equals("Feb")){
			return 1;
		}
		if(month.equals("Mar")){
			return 2;
		}
		if(month.equals("Apr")){
			return 3;
		}
		if(month.equals("May")){
			return 4;
		}
		if(month.equals("Jun")){
			return 5;
		}
		if(month.equals("Jul")){
			return 6;
		}
		if(month.equals("Aug")){
			return 7;
		}
		if(month.equals("Sep")){
			return 8;
		}
		if(month.equals("Oct")){
			return 9;
		}
		if(month.equals("Nov")){
			return 10;
		}
		if(month.equals("Dec")){
			return 11;
		}
		return -1;
	}

	public void reader(){
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream("C:/Users/Oskar/Documents/Skolan/kandidatarbetet/data/GS.txt");
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



				DailyData data;
				if(strLine.contains("%")){
					splittedString = strLine.split("%");
					symbol = splittedString[1];
//					System.out.println(symbol);
				} else if (strLine.contains("open")){
					String[] inputString;
					inputString = strLine.split("\\{", 100);

					for(int i = 1; i < inputString.length; i++){
//						System.out.println("SYMBOL: " + symbol);
						splittedString = inputString[i].split("open\":\"");
						splittedString = splittedString[1].split("\",\"");
						splittedString[0] = splittedString[0].substring(1);
						open = Double.parseDouble(splittedString[0]);
//						System.out.println("OPEN: " + open);

						splittedString = inputString[i].split("close\":\"");
						splittedString = splittedString[1].split("\",\"");
						splittedString[0] = splittedString[0].substring(1);
						close = Double.parseDouble(splittedString[0]);
//						System.out.println("CLOSE: " + close);

						splittedString = inputString[i].split("high\":\"");
						splittedString = splittedString[1].split("\",\"");
						splittedString[0] = splittedString[0].substring(1);
						high = Double.parseDouble(splittedString[0]);
//						System.out.println("HIGH: " + high);

						splittedString = inputString[i].split("low\":\"");
						splittedString = splittedString[1].split("\",\"");
						splittedString[0] = splittedString[0].substring(1);
						low = Double.parseDouble(splittedString[0]);
//						System.out.println("LOW: " + low);

						splittedString = inputString[i].split("volume\":\"");
						splittedString = splittedString[1].split("\"}");
						splittedString[0] = splittedString[0].replaceAll(",", "");
						volume = Long.parseLong(splittedString[0]);
//						System.out.println("VOLUME: " + volume);

						calendar = Calendar.getInstance();

						int year;
						int month;
						int date;
						String dateString;

						splittedString = inputString[i].split("date\":\"");
						splittedString = splittedString[1].split("\",\"");
						dateString = splittedString[0];
						dateString = dateString.trim();
						month = monthToNumber(dateString.substring(0, 3));
						String[] splittedDate = dateString.substring(3,dateString.length()).split(",");
						date = Integer.parseInt(splittedDate[0].trim());
						year = Integer.parseInt(splittedDate[1].trim());

						calendar.set(year, month, date);

//						System.out.println("ÅR: " + calendar.get(Calendar.YEAR)+
//								" MÅNAD: " + calendar.get(Calendar.MONTH)+
//								" DAG: " + calendar.get(Calendar.DATE));
						
						data = new DailyData(DatabaseHandler.getStock(symbol), calendar, open, close, high, low, volume);
						System.out.println(data);
						DatabaseHandler.addDailyData(data);
					}
				}

				//System.out.println(splittedString[0]);
			}
			//Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}

