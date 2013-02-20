package collector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import data.DailyData;
import data.QuarterlyData;
import data.RTData;
import data.Stock;

public class DatabaseHandler {
	
	private static Connection con = null;
	private static Statement st = null;
	private static ResultSet rs = null;
	private static String user="runa", password="123456", server="MySQL", databaseName, url="jdbc:mysql://localhost/test?";
	private static String port="3306", host ="127.0.0.1", userpass="user=runa&password=123456";
	static private DatabaseHandler dataBase;
	
	public static void main(String[] args){
		
		addStock(new Stock("name","AKTSYM", "stockExchange"));
	}
	
	
	private DatabaseHandler(){
		
	}
	
	static public DatabaseHandler getInstance(){
		dataBase = new DatabaseHandler();
		return dataBase;
	}
	
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

	
	public boolean addRTEntry(RTData entry){
		String date = null;
		String time = null;
		
		String queryTime = "INSERT INTO Stock (symbol, date, time, price, orderBook) VALUES('"+time+"')";
		String queryData = "INSERT INTO Stock (symbol, date, time, price, orderBook) VALUES('"+entry.getSymbol()+"', '"+date+"', '"+time+"', '"+entry.getPrice()+"', '"+entry.getOrderBook()+"')";
		
		try {
			con = DriverManager.getConnection(url + userpass);
		     st = con.createStatement();
	         st.executeUpdate(queryData);
	         st.executeUpdate(queryTime);
			
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
	
	public boolean addDailyData(DailyData data){

		String date = null;
		String queryValues = "INSERT INTO Stock (stock, date, closePrice, high, low, volume) VALUES('"+data.getSymbol()+"', '"+date+"', '"+data.getClosePrice()+"', '"+data.getHigh()+"', '"+data.getLow()+"', '"+data.getVolume()+"')";
		String queryKeys = "INSERT INTO Stock (stock, date, marketCap, PE, PS, PEG, dividentYield) VALUES('"+data.getSymbol()+"', '"+date+"', '"+data.getMarketCap()+"', '"+data.getPE()+"', '"+data.getPS()+"', '"+data.getPEG()+"', '"+data.getDividentYield()+"')";

		
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
	
	public boolean addQuarterlyData(QuarterlyData data){

		String date = null;
		String query = "INSERT INTO Stock (stock, releaseDate, yield, solidity, NAV, dividentPerShare) VALUES('"+data.getSymbol()+"', '"+date+"', '"+data.getYield()+"', '"+data.getSolidity()+"', '"+data.getNAV()+"', '"+data.getDividentPerShare()+"')";
		
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
	
	public boolean addDate(Date date){
			
		int year = 0,month = 0,dag = 0 ;
			
		String query = "INSERT INTO Date (date) VALUES('DATE: Manual Date', '"+year+"-"+month+"-"+dag+"')";
		
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
	
	
	
}
