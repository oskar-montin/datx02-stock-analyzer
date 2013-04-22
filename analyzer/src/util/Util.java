package util;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;

import controller.DatabaseHandler;



import data.DailyData;
import data.LargeDouble;
import data.MarketItem;
import data.QuarterlyData;
import data.SimpleData;
import data.Stock;

public class Util {

	public static boolean isUpDay(DailyData data) {
		return data.getOpenPrice()<data.getClosePrice();
	}
	
	/**
	 * A static method that returns the mean values of the business sector of the input stock. Only the daily values
	 * are processed.
	 * 
	 * @param  A stock from which the business will be fetched.
	 * @return Returns a DailyData-point with all the mean values. The dataPoint will have a stock that has the name, symbol
	 * 		   business equal to the business sector of the input. The exchange is set to MeanExchange.
	 */
	public static DailyData dailyDataMean(Stock stock){

		PriorityQueue<DailyData> businessDailyData = new PriorityQueue<DailyData>();

		businessDailyData = DatabaseHandler.getBusinessDailyData(stock);

		LinkedList<DailyData> ddLinkedList = new LinkedList<DailyData>(businessDailyData);

		DailyData dailyData;

		LargeDouble marketCap = new LargeDouble("0");
		double dividentYield = 0;
		double PE = 0;
		double PS = 0;
		double PEG = 0;

		double openPrice = 0;
		double closePrice = 0;
		double high = 0;
		double low = 0;
		long volume = 0;

		Stock returnStock = new Stock(stock.getBusiness(), stock.getBusiness(), 
				stock.getBusiness(), "MeanExchange");


		int marketCapCounter = 0;
		LargeDouble totalMarketCap = new LargeDouble("");
		for(int i = ddLinkedList.size()-1; i >= 0; i--){
			if(!ddLinkedList.get(i).getMarketCap().equals(0)){
				marketCapCounter++;
				totalMarketCap = totalMarketCap.add(ddLinkedList.get(i).getMarketCap());
			}
		}
		marketCap = totalMarketCap.div(new LargeDouble(marketCapCounter+""), 5);


		int dividentYieldCounter = 0;
		double totalDividentYield = 0;
		for(int i = ddLinkedList.size()-1; i >= 0; i--){
			if(ddLinkedList.get(i).getDividentYield() != -Math.PI){
				dividentYieldCounter++;
				totalDividentYield = totalDividentYield + ddLinkedList.get(i).getDividentYield();
			}
		}
		dividentYield = totalDividentYield/dividentYieldCounter;


		int PECounter = 0;
		double totalPE = 0;
		for(int i = ddLinkedList.size()-1; i >= 0; i--){
			if(ddLinkedList.get(i).getPE() != -Math.PI){
				PECounter++;
				totalPE = totalPE + ddLinkedList.get(i).getPE();
			}
		}
		PE = totalPE/PECounter;


		int PSCounter = 0;
		double totalPS = 0;
		for(int i = ddLinkedList.size()-1; i >= 0; i--){
			if(ddLinkedList.get(i).getPS() != -Math.PI){
				PSCounter++;
				totalPS = totalPS + ddLinkedList.get(i).getPS();
			}
		}
		PS = totalPS/PSCounter;


		int PEGCounter = 0;
		double totalPEG = 0;
		for(int i = ddLinkedList.size()-1; i >= 0; i--){
			if(ddLinkedList.get(i).getPEG() != -Math.PI){
				PEGCounter++;
				totalPEG = totalPEG + ddLinkedList.get(i).getPEG();
			}
		}
		PEG = totalPEG/PEGCounter;


		int openPriceCounter = 0;
		double totalOpenPrice = 0;
		for(int i = ddLinkedList.size()-1; i >= 0; i--){
			if(ddLinkedList.get(i).getOpenPrice() != -Math.PI){
				openPriceCounter++;
				totalOpenPrice = totalOpenPrice + ddLinkedList.get(i).getOpenPrice();
			}
		}
		openPrice = totalOpenPrice/openPriceCounter;


		int closePriceCounter = 0;
		double totalClosePrice = 0;
		for(int i = ddLinkedList.size()-1; i >= 0; i--){
			if(ddLinkedList.get(i).getClosePrice() != -Math.PI){
				closePriceCounter++;
				totalClosePrice = totalClosePrice + ddLinkedList.get(i).getClosePrice();
			}
		}
		closePrice = totalClosePrice/closePriceCounter;


		int highCounter = 0;
		double totalHigh = 0;
		for(int i = ddLinkedList.size()-1; i >= 0; i--){
			if(ddLinkedList.get(i).getHigh() != -Math.PI){
				highCounter++;
				totalHigh = totalHigh + ddLinkedList.get(i).getHigh();
			}
		}
		high = totalHigh/highCounter;


		int lowCounter = 0;
		double totalLow = 0;
		for(int i = ddLinkedList.size()-1; i >= 0; i--){
			if(ddLinkedList.get(i).getLow() != -Math.PI){
				lowCounter++;
				totalLow = totalLow + ddLinkedList.get(i).getLow();
			}
		}
		low = totalLow/lowCounter;


		int volumeCounter = 0;
		long totalVolume = 0;
		for(int i = ddLinkedList.size()-1; i >= 0; i--){
			if(ddLinkedList.get(i).getVolume() != -1){
				volumeCounter++;
				totalVolume = totalVolume + ddLinkedList.get(i).getVolume();
			}
		}
		volume = totalVolume/volumeCounter;

		/**
		 * 
		 * @param stock
		 * @param date
		 * @param marketCap
		 * @param dividentYield
		 * @param PE
		 * @param PS
		 * @param PEG
		 * @param openPrice
		 * @param closePrice
		 * @param high
		 * @param low
		 * @param volume
		 */
		dailyData = new DailyData(returnStock, Calendar.getInstance(), marketCap, dividentYield, PE, PS, PEG, openPrice
				, closePrice, high, low, volume);

		return dailyData;

	}

	/**
	 * A static method that returns the mean values of the business sector of the input stock. Only the quarterly
	 * values are processed.
	 * 
	 * @param  A stock from which the business will be fetched.
	 * @return Returns a QuarterlyData-point with all the mean values. The dataPoint will have a stock that has the name, symbol
	 * 		   business equal to the business sector of the input. The exchange is set to MeanExchange.
	 */
	public static QuarterlyData quarterlyDataMean(Stock stock){

		PriorityQueue<QuarterlyData> businessQuarterlyData = new PriorityQueue<QuarterlyData>();

		businessQuarterlyData = DatabaseHandler.getBusinessQuarterlyData(stock);

		LinkedList<QuarterlyData> qdLinkedList = new LinkedList<QuarterlyData>(businessQuarterlyData);

		QuarterlyData quarterlyData;

		LargeDouble NAV;	
		double solidity;								// Total shareholders equity / Total assets				
		double dividentPerShare;

		double ROE;										// Net Income / stockholders equity
		double EPS;										// Earnings per share (Net earnings/outstanding shares)
		double NAVPS;									// Net Asset Value per Share (NAV/total outstanding shares)
		double pricePerNAVPS;							// Last close / NAVPS			
		double acidTestRatio;							// (current assets - inventory) / current liabilities
		double balanceLiquidity;						// current assets / current liabilities
		LargeDouble workingCapital;						// current assets - current liabilities

		Stock returnStock = new Stock(stock.getBusiness(), stock.getBusiness(), 
				stock.getBusiness(), "MeanExchange");


		int NAVCounter = 0;
		LargeDouble totalNAV = new LargeDouble("");
		for(int i = qdLinkedList.size()-1; i >= 0; i--){
			if(!qdLinkedList.get(i).getNAV().equals(0) || !qdLinkedList.get(i).getNAV().equals(-1)){
				NAVCounter++;
				totalNAV = totalNAV.add(qdLinkedList.get(i).getNAV());
			}
		}
		NAV = totalNAV.div(new LargeDouble(NAVCounter+""), 5);


		int solidityCounter = 0;
		double totalSolidity = 0;
		for(int i = qdLinkedList.size()-1; i >= 0; i--){
			if(qdLinkedList.get(i).getSolidity() != 0 || qdLinkedList.get(i).getSolidity() != -1){
				solidityCounter++;
				totalSolidity = totalSolidity + qdLinkedList.get(i).getSolidity();
			}
		}
		solidity = totalSolidity/solidityCounter;


		int dividentPerShareCounter = 0;
		double totalDividentPerShare = 0;
		for(int i = qdLinkedList.size()-1; i >= 0; i--){
			if(qdLinkedList.get(i).getDividentPerShare() != -Math.PI){
				dividentPerShareCounter++;
				totalDividentPerShare = totalDividentPerShare + qdLinkedList.get(i).getDividentPerShare();
			}
		}
		dividentPerShare = totalDividentPerShare/dividentPerShareCounter;


		int ROECounter = 0;
		double totalROE = 0;
		for(int i = qdLinkedList.size()-1; i >= 0; i--){
			if(qdLinkedList.get(i).getROE() != -Math.PI){
				ROECounter++;
				totalROE = totalROE + qdLinkedList.get(i).getROE();
			}
		}
		ROE = totalROE/ROECounter;


		int EPSCounter = 0;
		double totalEPS = 0;
		for(int i = qdLinkedList.size()-1; i >= 0; i--){
			if(qdLinkedList.get(i).getEPS() != -Math.PI){
				EPSCounter++;
				totalEPS = totalEPS + qdLinkedList.get(i).getEPS();
			}
		}
		EPS = totalEPS/EPSCounter;


		int NAVPSCounter = 0;
		double totalNAVPS = 0;
		for(int i = qdLinkedList.size()-1; i >= 0; i--){
			if(qdLinkedList.get(i).getNAVPS() != -Math.PI){
				NAVPSCounter++;
				totalNAVPS = totalNAVPS + qdLinkedList.get(i).getNAVPS();
			}
		}
		NAVPS = totalNAVPS/NAVPSCounter;


		int pricePerNAVPSCounter = 0;
		double totalPricePerNAVPS = 0;
		for(int i = qdLinkedList.size()-1; i >= 0; i--){
			if(qdLinkedList.get(i).getPricePerNAVPS() != -Math.PI){
				pricePerNAVPSCounter++;
				totalPricePerNAVPS = totalPricePerNAVPS + qdLinkedList.get(i).getPricePerNAVPS();
			}
		}
		pricePerNAVPS = totalPricePerNAVPS/pricePerNAVPSCounter;


		int acidTestRatioCounter = 0;
		double totalAcidTestRatio = 0;
		for(int i = qdLinkedList.size()-1; i >= 0; i--){
			if(qdLinkedList.get(i).getAcidTestRatio() != -Math.PI){
				acidTestRatioCounter++;
				totalAcidTestRatio = totalAcidTestRatio + qdLinkedList.get(i).getAcidTestRatio();
			}
		}
		acidTestRatio = totalAcidTestRatio/acidTestRatioCounter;


		int balanceLiquidityCounter = 0;
		double totalBalanceLiquidity = 0;
		for(int i = qdLinkedList.size()-1; i >= 0; i--){
			if(qdLinkedList.get(i).getBalanceLiquidity() != -1){
				balanceLiquidityCounter++;
				totalBalanceLiquidity = totalBalanceLiquidity + qdLinkedList.get(i).getBalanceLiquidity();
			}
		}
		balanceLiquidity = totalBalanceLiquidity/balanceLiquidityCounter;


		int workingCapitalCounter = 0;
		LargeDouble totalWorkingCapital = new LargeDouble("");
		for(int i = qdLinkedList.size()-1; i >= 0; i--){
			if(!qdLinkedList.get(i).getWorkingCapital().equals(0) || !qdLinkedList.get(i).getWorkingCapital().equals(-1)){
				workingCapitalCounter++;
				totalWorkingCapital = totalWorkingCapital.add(qdLinkedList.get(i).getWorkingCapital());
			}
		}
		workingCapital = totalWorkingCapital.div(new LargeDouble(workingCapitalCounter+""), 5);


		/**
		 * 
		 * @param stock
		 * @param dateCollected
		 * @param solidity
		 * @param nAV
		 * @param dividentPerShare
		 * @param rOE
		 * @param ePS
		 * @param nAVPS
		 * @param pricePerNAVPS
		 * @param acidTestRatio
		 * @param balanceLiquidity
		 * @param workingCapital
		 */
		quarterlyData = new QuarterlyData(returnStock, Calendar.getInstance(), solidity, NAV, 
				dividentPerShare, ROE, EPS, NAVPS, pricePerNAVPS, acidTestRatio, balanceLiquidity, workingCapital);

		return quarterlyData;
	}


	/**
	 * A simple method made just to fetch the latest daily data from a PriorityQueue.
	 * 
	 * @param queue - a PriorityQueue
	 * @return a DailyData-point
	 */
	public static DailyData getLatestDailyData(PriorityQueue<DailyData> queue){
		PriorityQueue<DailyData> tempQueue = new PriorityQueue<DailyData>(queue);
		DailyData returnData = null;
		if(!tempQueue.isEmpty()){
			while(tempQueue.size() != 0){
				returnData = tempQueue.poll();
			}
		}
		return returnData;
	}

	public static PriorityQueue<SimpleData> trimQueue(Collection<? extends SimpleData> methodQueue, int offset){
		LinkedList<SimpleData> methodData = new LinkedList<SimpleData>(methodQueue);
		LinkedList<SimpleData> nullData = new LinkedList<SimpleData>();
		
		
		for(int i = 0; i < offset-1; i++){
			methodData.get(0).getDate();
			Calendar newDate = Calendar.getInstance();
			newDate.set(Calendar.YEAR, i);
			nullData.add(new SimpleData(methodData.get(0).getStock(), newDate, methodData.get(0).getValue()));
		}
////		
//		System.out.println("NULLDATA: " + nullData);
//		System.out.println("METHODDATA AFTER: " + methodData);

		nullData.addAll(methodData);

//		System.out.println("EHHEHEE: " + nullData);


		//for(int i = 0; i < originalSize-priceData.size(); i++){
		//priceData.add(methodData.getFirst());
		//}



		PriorityQueue<SimpleData> returnQueue = new PriorityQueue<SimpleData>(nullData);

//		System.out.println(returnQueue);
		
		return returnQueue;
	}
}
