package collector;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import data.DailyData;
import data.LargeDouble;
import data.QuarterlyData;
import data.RTData;
import data.Stock;


/**
 * DatabaseHandler is a CLASS that adds and collects data from database.
 * IMPORTANT- always add date (and time) separately before adding data connected whit that date (and time).
 */

public class DatabaseHandler {

	private static String user="runa", password="123456",  url="jdbc:mysql://localhost:3306/test?";
	private static String  userpass="user=root&password=123456";
	private static DatabaseHandler dataBase;

	public static void main(String[] args){
		// --- connecting to driver probably needed when not importing jarfile ---
		//try {
			// The newInstance() call is a work around for some
			// broken Java implementations
			//Class.forName("com.mysql.jdbc.Driver").newInstance();
			//} catch (Exception ex) {
			// handle the error
			//}
		
	//	---- FOR TESTING ----
		Calendar cal =Calendar.getInstance();
		long longy= Long.MAX_VALUE;
		System.out.println(longy);
		cal.set(Calendar.YEAR, 1999 );
		addDate(cal);
		addTime(cal);
		addDailyData(new DailyData(getStock("symm"), cal, new LargeDouble("120.2B"), 11, 12, 13, 14, 15, 16, 17, 18, longy ));


		PriorityQueue<DailyData> data = getDailyData(getStock("symm"));
	
	
		while(!data.isEmpty()){
			DailyData i = data.poll();
			System.out.println(i.getStock().getSymbol());

			System.out.println("market : "+i.getMarketCap().toString() );
			System.out.println("volume: "+i.getVolume()  );
			System.out.println("time: "+i.getDate().get(Calendar.HOUR_OF_DAY)  );

			System.out.println("min: "+i.getDate().get(Calendar.MINUTE)  );
			
		}	
		
		// The newInstance() call is a work around for some
		// broken Java implementations
		//Class.forName("com.mysql.jdbc.Driver").newInstance();
		//} catch (Exception ex) {
		// handle the error
		//}

		//	---- FOR TESTING ----
		//		Calendar cal =Calendar.getInstance();
		//		cal.set(Calendar.HOUR_OF_DAY, 10 );
		//		addDate(cal);
		//		addTime(cal);
		//		addRTData(new RTData(getStock("symm"), cal, 212, 213));
		//
		//
		//		PriorityQueue<RTData> data = getRTData(getStock("symm"));
		//	
		//	
		//		while(!data.isEmpty()){
		//			RTData i = data.poll();
		//			System.out.println(i.getStock().getSymbol());
		//
		//			System.out.println("Month : "+i.getDate().get(Calendar.MONTH)  );
		//			System.out.println("date: "+i.getDate().get(Calendar.DAY_OF_MONTH)  );
		//			System.out.println("time: "+i.getDate().get(Calendar.HOUR_OF_DAY)  );
		//
		//			System.out.println("min: "+i.getDate().get(Calendar.MINUTE)  );
		//			
		//		}	

	}


	private DatabaseHandler(){

	}

	static public DatabaseHandler getInstance(){
		dataBase = new DatabaseHandler();
		return dataBase;
	}

	/**
	 * Method adds a new stock with stock-data to the Stock-table 
	 * in the database
	 * 
	 * @param Stock - new stock to be added to database
	 * @return returns a Boolean stating if insert was successful
	 * 
	 * @author Runa Gulliksson
	 */
	public static boolean addStock(Stock stock){

		Connection con = null;
		Statement st = null;

		String query = "INSERT INTO Stock (symbol, name, stockexchange, business) VALUES('"+stock.getSymbol()+"', '"+stock.getName()+"', '"+stock.getStockExchange()+"', '"+stock.getBusiness()+"')";

		try {

			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			st.executeUpdate(query);


		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}} catch (SQLException ex) {
					return false;
				}
		}

		return true;

	}

	/**
	 * Method adds all real-time data that is used to a RealTimeData-table 
	 * in the database
	 * 
	 * @param RTData - data to be added to database
	 * @return returns a Boolean stating if insert was successful
	 * 
	 * @author Runa Gulliksson
	 */
	public static boolean addRTData(RTData entry){

		Connection con = null;
		Statement st = null;
		String queryData = "INSERT INTO realTimeData (stock, date, time, price, orderBook) VALUES('"+entry.getStock().getSymbol()+"', "+getDate(entry.getDate())+", "+getTime(entry.getDate())+", '"+entry.getPrice()+"', '"+entry.getOrderBook()+"')";

		try {
			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			st.executeUpdate(queryData);

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}} catch (SQLException ex) {
					return false;
				}
		}

		return true;

	}

	/**
	 * Method adds all data that is to be added once daily to the database.
	 * The data is split into two tables, DailyValues and DailyKeys.
	 * 
	 * @param DailyData - daily data to be added to database
	 * @return returns a Boolean stating if insert was successful
	 * 
	 * @author Runa Gulliksson
	 */
	public static boolean addDailyData(DailyData data){


		Connection con = null;
		Statement st = null;
		String queryValues = "INSERT INTO DailyValues (stock, date, closePrice, openPrice, high, low, volume) VALUES('"+data.getStock().getSymbol()+"', "+ getDate(data.getDate())+", '"+data.getClosePrice()+"', '"+data.getOpenPrice()+"', '"+data.getHigh()+"', '"+data.getLow()+"', '"+data.getVolume()+"')";
		String queryKeys = "INSERT INTO DailyKeys (stock, date, marketCap, PE, PS, PEG, dividentYield) VALUES('"+data.getStock().getSymbol()+"', "+ getDate(data.getDate())+", '"+data.getMarketCap().toString()+"', '"+data.getPE()+"', '"+data.getPS()+"', '"+data.getPEG()+"', '"+data.getDividentYield()+"')";


		try {
			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			st.executeUpdate(queryValues);
			st.executeUpdate(queryKeys);


		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}} catch (SQLException ex) {
					return false;
				}
		}

		return true;
	}

	/**
	 * Method adds all quarterly data that is used to a QuarterlyData-table 
	 * in database
	 * 
	 * @param QuarterlyData - data to be added to database
	 * @return returns a Boolean stating if insert was successful
	 * 
	 * @author Runa Gulliksson
	 */
	public static boolean addQuarterlyData(QuarterlyData data){

		Connection con = null;
		Statement st = null;
		String query = "INSERT INTO quarterlyData(stock, releaseDate, yield, solidity, NAV, dividentPerShare) VALUES('"+data.getStock().getSymbol()+"', "+ getDate(data.getDateCollected())+", '"+data.getDividendYield()+"', '"+data.getSolidity()+"', '"+data.getNAV()+"', '"+data.getDividentPerShare()+"')";

		try {
			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			st.executeUpdate(query);


		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}} catch (SQLException ex) {
					return false;
				}
		}

		return true;
	}

	/**
	 * Method adds a date to the date-table before using the date as key in other tables
	 * 
	 * @param Calendar - date to be added to date-table in database
	 * @return returns a Boolean stating if insert was successful
	 * 
	 * @author Runa Gulliksson
	 */
	public static boolean addDate(Calendar date){

		Connection con = null;
		Statement st = null;
		String query = "INSERT INTO Date (date) VALUES("+getDate(date)+")";

		try {
			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			st.executeUpdate(query);


		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}} catch (SQLException ex) {
					return false;
				}
		}

		return true;
	}


	/**
	 * Method adds a time to the time-table before using the date as key in other tables
	 * 
	 * @param Calendar - date to be added to the time-table in database
	 * @return returns a Boolean stating if insert was successful
	 * 
	 * @author Runa Gulliksson
	 */
	public static boolean addTime(Calendar date){

		Connection con = null;
		Statement st = null;
		String query = "INSERT INTO Time (time) VALUES("+getTime(date)+")";

		try {

			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			st.executeUpdate(query);


		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}} catch (SQLException ex) {
					return false;
				}
		}

		return true;
	}


	/**
	 * Method generates a value-string for a date
	 * helpmethod for inserting a date database
	 * 
	 * @param Calendar- date to be added to date-table in database
	 * @return returns a String 
	 * 
	 * @author Runa Gulliksson
	 */
	private static String getDate(Calendar date){

		int year,month,day ;
		year = date.get(Calendar.YEAR);
		month = date.get(Calendar.MONTH);
		day = date.get(Calendar.DATE);

		return" '"+year+"-"+month+"-"+day+"'";
	}

	/**
	 * Method generates a value-string for a time
	 * helpmethod for inserting a time database
	 * 
	 * @param Calendar- time to be added to time-table in database
	 * @return returns a String 
	 * 
	 * @author Runa Gulliksson
	 */
	private static String getTime(Calendar date){

		int hour, minit;
		hour = date.get(Calendar.HOUR_OF_DAY);
		minit = date.get(Calendar.MINUTE);

		return"'"+hour+":"+minit+":00'";
	}


	/**
	 * Method collects all attributes connected with a stock from the database.
	 * Returns an instance of stock with the collected data.
	 * 
	 * @param String - symbol for a stock
	 * @return A Stock connected to the symbol
	 * 
	 * @author Runa Gulliksson
	 */
	public static Stock getStock(String symbol){

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		String name = null, stockExchange = null, business = null;
		try {

			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			rs = st.executeQuery("SELECT name, stockExchange, business FROM Stock WHERE symbol='"+symbol+"'");

			if(rs.next()){
				name=rs.getString("name");
				stockExchange =rs.getString("stockExchange");
				business=rs.getString("business");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}} catch (SQLException ex) {
					System.out.println("error- while closing connection");
				}
		}
		return new Stock( name, symbol, stockExchange, business);

	}

	/**
	 * Method collects all quarterlyData connected with a stock from the database.
	 * Returns an instance of QuaterlyData with the collected data.
	 * 
	 * @param Stock 
	 * @return QuaterlyData connected with the given Stock
	 * 
	 * @author Runa Gulliksson
	 */
	public static QuarterlyData getQuarterlyData(Stock stock){

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		String NAV = "";
		Calendar releaseDate = Calendar.getInstance();
		double yield = 0;
		double solidity = 0;
		double dividentPerShare = 0;

		double ROE = 0;							
		double EPS = 0;								
		double NAVPS = 0;							
		double pricePerNAVPS = 0;						
		double acidTestRatio = 0;						
		double balanceLiquidity = 0;						
		String workingCapital = "";							

		try {

			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM QuarterlyData WHERE stock='"+stock.getSymbol()+"'");


			if(rs.next()){
				releaseDate.setTime(rs.getDate("releasedate"));
				releaseDate.set(Calendar.MONTH, (releaseDate.get(Calendar.MONTH)+1));
				yield=rs.getDouble("yield");
				solidity =rs.getDouble("solidity");
				NAV=rs.getString("NAV");
				dividentPerShare=rs.getDouble("dividentPerShare");
				ROE = rs.getDouble("ROE");
				EPS = rs.getDouble("ROE");
				NAVPS = rs.getDouble("NAVPS");
				pricePerNAVPS = rs.getDouble("pricePerNAVPS");
				acidTestRatio = rs.getDouble("acidTestRatio");
				balanceLiquidity = rs.getDouble("balanceLiquidity");
				workingCapital = rs.getString("workingCapital");
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}} catch (SQLException ex) {
					System.out.println("error- while closing connection");
				}
		}

		return new QuarterlyData(stock, releaseDate, yield, solidity, new LargeDouble(NAV), dividentPerShare, ROE,
				EPS, NAVPS, pricePerNAVPS, acidTestRatio, balanceLiquidity, new LargeDouble(workingCapital));

	}

	/**
	 * Method collects all DalyData connected with a stock from the database.
	 * 
	 * @param Stock 
	 * @return All DailyData, connected with the given Stock, stored in a PriorityQueue prioritized after date.
	 * 						
	 * 
	 * @author Runa Gulliksson
	 */
	public static PriorityQueue<DailyData> getDailyData(Stock stock){

		String marketCap = ""; //----
		double dividentYield=0;
		double PE=0;
		double PS=0;
		double PEG=0;
		double openPrice=0;
		double closePrice=0;
		double high=0;
		double low=0;
		long volume=0;
		PriorityQueue<DailyData> dataList = new PriorityQueue<DailyData>();

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {

			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			rs = st.executeQuery("SELECT openPrice, closePrice, high, low, date, volume, marketcap, dividentYield, PE, PS, PEG FROM Daily_data  WHERE stock='"+stock.getSymbol()+"'");

			while (rs.next()) {
				marketCap=rs.getString("marketCap");
				dividentYield=rs.getDouble("dividentYield");
				PE=rs.getDouble("PE");
				PS=rs.getDouble("PS");
				PEG=rs.getDouble("PEG");
				openPrice=rs.getDouble("openPrice");
				closePrice=rs.getDouble("closePrice");
				high=rs.getDouble("high");
				low=rs.getDouble("low");
				volume=rs.getLong("volume");
				Calendar date = Calendar.getInstance();
				date.setTime(rs.getDate("date"));
				date.set(Calendar.MONTH, (date.get(Calendar.MONTH)+1));

				System.out.println(date.get(Calendar.MONTH));

				dataList.add(new DailyData(stock, date, new LargeDouble(marketCap), dividentYield, PE, PS, PEG, openPrice, closePrice, high, low, volume));

			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}} catch (SQLException ex) {
					System.out.println("error- while closing connection");
				}
		}
		return dataList;

	}

	/**
	 * Method collects all realtimeData connected with a stock from the database.
	 * 
	 * @param Stock 
	 * @return All RTData, connected with the given Stock, stored in a PriorityQueue prioritized after date.					
	 * 
	 * @author Runa Gulliksson
	 */
	public static PriorityQueue<RTData> getRTData(Stock stock){

		double price = 0;
		double orderBook=0;
		PriorityQueue<RTData> dataList = new PriorityQueue<RTData>();

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {

			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			rs = st.executeQuery("SELECT date, time, price, orderBook FROM realtimedata  WHERE stock='"+stock.getSymbol()+"'");

			while (rs.next()) {
				Calendar date = Calendar.getInstance() ;
				Calendar time = Calendar.getInstance() ;
				date.setTime(rs.getDate("date"));
				time.setTime(rs.getTime("time"));
				date.set(Calendar.MONTH, (date.get(Calendar.MONTH)+1));
				date.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
				date.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
				price=rs.getDouble("price");
				orderBook=rs.getDouble("orderBook");

				dataList.add( new RTData(stock, date, price, orderBook));
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}} catch (SQLException ex) {
					System.out.println("error- while closing connection");
				}
		}
		return dataList;

	}



}
