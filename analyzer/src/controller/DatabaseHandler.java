package controller;

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
 * 
 */

public class DatabaseHandler {

	private static String  url="jdbc:mysql://129.16.20.232:3306/test?";
	private static String  userpass="user=root&password=123456";
	private static DatabaseHandler dataBase;


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
		return new Stock( name, symbol,  business, stockExchange);

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

		String NAV = ""; //---
		Calendar releaseDate = Calendar.getInstance();

		double solidity = 0;
		double dividentPerShare = 0;

		double ROE = 0;							
		double EPS = 0;								
		double NAVPS = 0;							
		double pricePerNAVPS = 0;						
		double acidTestRatio = 0;						
		double balanceLiquidity = 0;						
		String workingCapital = "";		//------					

		try {

			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM QuarterlyData WHERE stock='"+stock.getSymbol()+"'");


			if(rs.next()){
				releaseDate.setTime(rs.getDate("releasedate"));
				releaseDate.set(Calendar.MONTH, (releaseDate.get(Calendar.MONTH)+1));

				solidity =rs.getDouble("solidity");
				NAV=rs.getString("NAV");
				dividentPerShare=rs.getDouble("dividentPerShare");
				ROE = rs.getDouble("ROE");
				EPS = rs.getDouble("EPS");
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

		return new QuarterlyData(stock, releaseDate, solidity, new LargeDouble(NAV), dividentPerShare, ROE,
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
			Calendar date = null;
			if(rs.next()) {
				marketCap=rs.getString("marketCap");
				dividentYield=rs.getDouble("dividentYield");
				PE=rs.getDouble("PE");
				PS=rs.getDouble("PS");
				PEG=rs.getDouble("PEG");
				openPrice=rs.getDouble("openPrice");
				
				high=rs.getDouble("high");
				low=rs.getDouble("low");
				volume=rs.getLong("volume");
				date = Calendar.getInstance();
				date.setTime(rs.getDate("date"));
				date.set(Calendar.MONTH, (date.get(Calendar.MONTH)+1));
			}
			while (rs.next()) {
				
				closePrice=rs.getDouble("closePrice");
				
				dataList.add(new DailyData(stock, date, new LargeDouble(marketCap), dividentYield, PE, PS, PEG, openPrice, closePrice, high, low, volume));
				
				marketCap=rs.getString("marketCap");
				dividentYield=rs.getDouble("dividentYield");
				PE=rs.getDouble("PE");
				PS=rs.getDouble("PS");
				PEG=rs.getDouble("PEG");
				openPrice=rs.getDouble("openPrice");
				
				high=rs.getDouble("high");
				low=rs.getDouble("low");
				volume=rs.getLong("volume");
				date = Calendar.getInstance();
				date.setTime(rs.getDate("date"));
				date.set(Calendar.MONTH, (date.get(Calendar.MONTH)+1));
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

	/**
	 * Method collects all stocks in database.
	 *  
	 * @return All stocks, stored in a PriorityQueue.					
	 * 
	 * @author Runa Gulliksson
	 */
	public static PriorityQueue<Stock> getAllStocks(){

		String name="", symbol="", business="", stockExchange="";
		PriorityQueue<Stock> dataList = new PriorityQueue<Stock>();

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {

			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			rs = st.executeQuery("SELECT * From stock");

			while (rs.next()) {
				name=rs.getString("name");
				symbol=rs.getString("symbol");
				business=rs.getString("business");
				stockExchange=rs.getString("stockExchange");

				dataList.add( new Stock(name, symbol, business, stockExchange ));
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
	 * Method collects all QuarterlyData connected with a business from the database.
	 * 
	 * @param Stock 
	 * @return All QuarterltDat, connected with the given Stocks business, stored in a PriorityQueue prioritized after date.					
	 * 
	 * @author Runa Gulliksson
	 */
	public static PriorityQueue<QuarterlyData> getBusinessQuarterlyData(Stock stock){

		Statement stS;
		String NAV = ""; //---
		String symbol="";
		Stock businessStock = null;
		Calendar releaseDate = Calendar.getInstance();
		double solidity = 0;
		double dividentPerShare = 0;

		double ROE = 0;							
		double EPS = 0;								
		double NAVPS = 0;							
		double pricePerNAVPS = 0;						
		double acidTestRatio = 0;						
		double balanceLiquidity = 0;						
		String workingCapital = "";		//------		

		PriorityQueue<QuarterlyData> dataList = new PriorityQueue<QuarterlyData>();

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {

			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			stS = con.createStatement();
			rs = st.executeQuery("SELECT * FROM QuarterlyData WHERE stock = ANY (SELECT symbol FROM stock WHERE business ='"+stock.getBusiness()+"')");

			while (rs.next()) {

				symbol = rs.getString("stock");
				releaseDate.setTime(rs.getDate("releasedate"));
				releaseDate.set(Calendar.MONTH, (releaseDate.get(Calendar.MONTH)+1));

				solidity =rs.getDouble("solidity");
				NAV=rs.getString("NAV");
				dividentPerShare=rs.getDouble("dividentPerShare");
				ROE = rs.getDouble("ROE");
				EPS = rs.getDouble("EPS");
				NAVPS = rs.getDouble("NAVPS");
				pricePerNAVPS = rs.getDouble("pricePerNAVPS");
				acidTestRatio = rs.getDouble("acidTestRatio");
				balanceLiquidity = rs.getDouble("balanceLiquidity");
				workingCapital = rs.getString("workingCapital");

				ResultSet rsS = stS.executeQuery("SELECT * FROM stock WHERE symbol = '"+symbol+"'");

				if(rsS.next()){
					businessStock = new Stock(rsS.getString("name"), rsS.getString("symbol"), rsS.getString("business"), rsS.getString("stockExchange") );
				}

				dataList.add( new QuarterlyData(businessStock, releaseDate, solidity, new LargeDouble(NAV), dividentPerShare, ROE,
						EPS, NAVPS, pricePerNAVPS, acidTestRatio, balanceLiquidity, new LargeDouble(workingCapital)));
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
	 * Method collects all DailyData connected with a business from the database.
	 * 
	 * @param Stock 
	 * @return All DailyData, connected with the given Stocks business, stored in a PriorityQueue prioritized after date.					
	 * 
	 * @author Oskar Nylén, Runa Gulliksson
	 */
	public static PriorityQueue<DailyData> getBusinessDailyData(Stock stock){

		Statement stS;
		String symbol="";
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
		
		Stock businessStock = null;
		
		PriorityQueue<DailyData> dataList = new PriorityQueue<DailyData>();

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {

			con = DriverManager.getConnection(url + userpass);
			st = con.createStatement();
			stS = con.createStatement();
			rs = st.executeQuery("SELECT * FROM Daily_data WHERE stock = ANY (SELECT symbol FROM stock WHERE business ='"+stock.getBusiness()+"')");

			while (rs.next()) {

				symbol = rs.getString("stock");
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

				ResultSet rsS = stS.executeQuery("SELECT * FROM stock WHERE symbol = '"+symbol+"'");

				if(rsS.next()){
					businessStock = new Stock(rsS.getString("name"), rsS.getString("symbol"), rsS.getString("business"), rsS.getString("stockExchange") );
				}

				dataList.add(new DailyData(businessStock, date, new LargeDouble(marketCap), dividentYield, PE, PS, PEG, openPrice, closePrice, high, low, volume));
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
