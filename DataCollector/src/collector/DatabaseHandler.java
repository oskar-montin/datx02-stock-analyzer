package collector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import data.DailyData;
import data.QuarterlyData;
import data.RTData;
import data.Stock;


/**
 * DatabaseHandler is a CLASS that adds and collects data from database.
 * IMPORTANT- always add date (and time) separately before adding data connected whit that date (and time).
 */

public class DatabaseHandler {
	
	private static Connection con = null;
	private static Statement st = null;
	private static ResultSet rs = null;
	private static String user="runa", password="123456", server="MySQL", databaseName, url="jdbc:mysql://localhost:3306/test?";
	private static String port="3306", host ="127.0.0.1", userpass="user=root&password=123456";
	private static DatabaseHandler dataBase;
	private static int HOUR_OF_DAY;
	private static int MINUTE;
	private static int YEAR;
	private static int MONTH;
	private static int DATE;
	
	public static void main(String[] args){

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
	                if (rs != null) {
	                    rs.close();
	                }
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
	                if (rs != null) {
	                    rs.close();
	                }
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
	                if (rs != null) {
	                    rs.close();
	                }
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
	                if (rs != null) {
	                    rs.close();
	                }
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
	                if (rs != null) {
	                    rs.close();
	                }
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
	                if (rs != null) {
	                    rs.close();
	                }
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
	

	/*
	* // Setup the connection with the DB
      connect = DriverManager
          .getConnection("jdbc:mysql://localhost/feedback?"
              + "user=sqluser&password=sqluserpw");

      // Statements allow to issue SQL queries to the database
      statement = connect.createStatement();
      // Result set get the result of the SQL query
      resultSet = statement
          .executeQuery("select * from FEEDBACK.COMMENTS");
      writeResultSet(resultSet);

	*
	**/
	
	
}
