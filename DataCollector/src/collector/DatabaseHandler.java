package collector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.LinkedList;

import data.DailyData;
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
		try {
			// The newInstance() call is a work around for some
			// broken Java implementations
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (Exception ex) {
			// handle the error
			}
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
	public static boolean addRTEntry(RTData entry){

		Connection con = null;
		Statement st = null;
		String queryData = "INSERT INTO realTimeData (stock, date, time, price, orderBook) VALUES('"+entry.getSymbol()+"', '"+getDate(entry.getDate())+"', '"+getTime(entry.getDate())+"', '"+entry.getPrice()+"', '"+entry.getOrderBook()+"')";
		
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
		String queryValues = "INSERT INTO DailyValues (stock, date, closePrice, openPrice, high, low, volume) VALUES('"+data.getSymbol()+"', '"+ getDate(data.getDate())+"', '"+data.getClosePrice()+"', '"+data.getOpenPrice()+"', '"+data.getHigh()+"', '"+data.getLow()+"', '"+data.getVolume()+"')";
		String queryKeys = "INSERT INTO DailyKeys (stock, date, marketCap, PE, PS, PEG, dividentYield) VALUES('"+data.getSymbol()+"', '"+ getDate(data.getDate())+"', '"+data.getMarketCap()+"', '"+data.getPE()+"', '"+data.getPS()+"', '"+data.getPEG()+"', '"+data.getDividentYield()+"')";

		
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
		String query = "INSERT INTO quarterlyData(stock, releaseDate, yield, solidity, NAV, dividentPerShare) VALUES('"+data.getSymbol()+"', '"+ getDate(data.getDateCollected())+"', '"+data.getYield()+"', '"+data.getSolidity()+"', '"+data.getNAV()+"', '"+data.getDividentPerShare()+"')";
		
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
		month = date.get(Calendar.MONTH) +1;
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
	
	public static Stock getStock(String symbol){
		
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		String name = null, stockExchange = null, business = null;
		try {

			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM Stock WHERE symbol='"+symbol+"'");
			
			name=rs.getString(2);
			stockExchange =rs.getString(3);
			business=rs.getString(4);
			System.out.println( );
			
			
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
		return new Stock(symbol, name, stockExchange, business);
		
	}
	
	public static LinkedList<DailyData> getDailyData(Stock stock){

		Calendar date = null;
		double marketCap = 0;
		double dividentYield=0;
		double PE=0;
		double PS=0;
		double PEG=0;
		double openPrice=0;
		double closePrice=0;
		double high=0;
		double low=0;
		long volume=0;
		LinkedList<DailyData> dataList = new LinkedList<DailyData>();
		
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {

			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			rs = st.executeQuery("SELECT marketcap, dividentYield, PE, PEG FROM Daily_data  WHERE symbol='"+stock.getSymbol()+"'");
			
			while (rs.next()) {
				marketCap=rs.getDouble("marketCap");
				 dividentYield=rs.getDouble("dividentYield");
				PE=rs.getDouble("PE");
				PE=rs.getDouble("PEG");
				System.out.println( );
				dataList.add(new DailyData(stock, date, marketCap, dividentYield, PE, PS, PEG, openPrice, closePrice, high, low, volume));
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
	/*
	* // Setup the connection with the DB
      connect = DriverManager
          .getConnection("jdbc:mysql://localhost/feedback?"
              + "user=sqluser&password=sqluserpw");
	*
	*(Stock stock, Calendar date, double marketCap,
			double dividentYield, double PE, 
			double PS, double PEG, double openPrice,
			double closePrice, double high,
			double low, long volume)
	*
	**/
	
	
}
