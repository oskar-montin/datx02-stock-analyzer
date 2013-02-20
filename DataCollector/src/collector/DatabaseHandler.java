package collector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
		
		String query = "INSERT INTO Stock (symbol, name, stockexchange, business) VALUES('sym', 'name', 'stockexchange', 'business')";
		
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

		return true;
	}
	
	public boolean addDailyData(DailyData data){

		return true;
	}
	
	public boolean addQuarterlyData(QuarterlyData data){

		return true;
	}
	
	
}
