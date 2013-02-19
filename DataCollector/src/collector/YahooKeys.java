package collector;
/*
 * Lista över alla flaggor
 * 
 * a	 Ask	a2	 Average Daily Volume	
 * a5	 Ask Size
 * b	 Bid	
 * b2	 Ask (Real-time)	b3	 Bid (Real-time)
 * b4	 Book Value	
 * b6	 Bid Size	
 * c	 Change & Percent Change
 * c1	 Change	
 * c3	 Commission	
 * c6	 Change (Real-time)
 * c8	 After Hours Change (Real-time)	d	 Dividend/Share	
 * d1	 Last Trade Date
 * d2	 Trade Date	
 * e	 Earnings/Share	e1	 Error Indication (returned for symbol changed / invalid)
 * e7	 EPS Estimate Current Year	
 * e8	 EPS Estimate Next Year	
 * e9	 EPS Estimate Next Quarter
 * f6	 Float Shares	
 * g	 Day’s Low	
 * h	 Day’s High
 * j	 52-week Low	k	 52-week High	
 * g1	 Holdings Gain Percent
 * g3	 Annualized Gain	
 * g4	 Holdings Gain	g5	 Holdings Gain Percent (Real-time)
 * g6	 Holdings Gain (Real-time)	
 * i	 More Info	
 * i5	 Order Book (Real-time)
 * j1	 Market Capitalization	
 * j3	 Market Cap (Real-time)	
 * j4	 EBITDA
 * j5	 Change From 52-week Low	j6	 Percent Change From 52-week Low	
 * k1	 Last Trade (Real-time) With Time
 * k2	 Change Percent (Real-time)	
 * k3	 Last Trade Size	k4	 Change From 52-week High
 * k5	 Percebt Change From 52-week High	
 * l	 Last Trade (With Time)	
 * l1	 Last Trade (Price Only)
 * l2	 High Limit	
 * l3	 Low Limit	
 * m	 Day’s Range
 * m2	 Day’s Range (Real-time)	m3	 50-day Moving Average	
 * m4	 200-day Moving Average
 * m5	 Change From 200-day Moving Average	
 * m6	 Percent Change From 200-day Moving Average	m7	 Change From 50-day Moving Average
 * m8	 Percent Change From 50-day Moving Average	
 * n	 Name	
 * n4	 Notes
 * o	 Open	
 * p	 Previous Close	
 * p1	 Price Paid
 * p2	 Change in Percent	p5	 Price/Sales	
 * p6	 Price/Book
 * q	 Ex-Dividend Date	
 * r	 P/E Ratio	r1	 Dividend Pay Date
 * r2	 P/E Ratio (Real-time)	
 * r5	 PEG Ratio	
 * r6	 Price/EPS Estimate Current Year
 * r7	 Price/EPS Estimate Next Year	
 * s	 Symbol	
 * s1	 Shares Owned
 * s7	 Short Ratio	t1	 Last Trade Time	
 * t6	 Trade Links
 * t7	 Ticker Trend	
 * t8	 1 yr Target Price	v	 Volume
 * v1	 Holdings Value	
 * v7	 Holdings Value (Real-time)	
 * w	 52-week Range
 * w1	 Day’s Value Change	
 * w4	 Day’s Value Change (Real-time)	
 * x	 Stock Exchange
 * y	 Dividend Yield
 */
/**
 * This class stores all tags used in the yahoo finance api as static String variables. 
 * Note: you can't create an instanse of this class.
 * @author Oskar Montin
 *
 */
public final class YahooKeys {
	
	public final static String baseURL = "http://finance.yahoo.com/d/quotes.csv?s=";
	
	//Tags
	public final static String ask = "a";
	public final static String askSize = "a5";
	public final static String bid = "b";
	public final static String askRT = "b2";
	public final static String bidRT = "b3";
	public final static String bookValue = "b6";
	public final static String changeAndPercentChange = "c";
	public final static String change = "c1";
	public final static String commission = "c3";
	public final static String changeRT = "c6";
	public final static String afterHoursChangeRT = "c8";
	public final static String dividentPerShare = "d";
	public final static String lastTradeDate = "d1";
	public final static String tradeDate = "d2";
	public final static String earningsPerShare = "e";
	public final static String errorIndication = "e1";
	public final static String EPSCurrYear = "e7";
	public final static String EPSNextYear = "e8";
	public final static String EPSNextQuarter = "e9";
	public final static String floatShares = "f6";
	public final static String daysLow = "g";
	public final static String daysHigh = "h";
	public final static String yearLow = "j";
	public final static String yearHigh = "k";
	public final static String holdingsGainPercent = "g1";
	public final static String annualizedGain = "g3";
	public final static String holdingsGain = "g4";
	public final static String holdingsGainPercentRT = "g5";
	public final static String holdingsGainRT = "g6";
	public final static String moreInfo = "i";
	public final static String orderBookRT = "i5";
	public final static String marketCap = "j1";
	public final static String marketCapRT = "j3";
	public final static String EBITDA = "j4";
	public final static String changeFromYearlyLow = "j5";
	public final static String percentChangeFromYearlyLow = "j6";
	public final static String lastTradeWithTimeRT = "k1";
	public final static String changePercentRT = "k2";
	public final static String lastTradeSize = "k3";
	public final static String changeFromYearlyHigh = "k4";
	public final static String percentChangeFromYearlyHigh = "k5";
	public final static String lastTradeWithTime = "l";
	public final static String lastTradePrice = "l1";
	public final static String highLimit = "l2";
	public final static String lowLimit = "l3";
	public final static String daysRange = "m";
	public final static String daysRangeRT = "m2";
	public final static String movingAvarage50Days = "m3";
	public final static String movingAvarage200Days = "m4";
	public final static String changeFrom200DaysMovingAvarage = "m5";
	public final static String percentChangeFrom200DaysMovingAvarage = "m6";
	public final static String changeFrom50DaysMovingAvarage = "m7";
	public final static String percentChangeFrom50DaysMovingAvarage = "m8";
	public final static String name = "n";
	public final static String notes = "n4";
	public final static String open = "o";
	public final static String previousClose = "p";
	public final static String pricePaid = "p1";
	public final static String changeInPercent = "p2";
	public final static String PSRatio = "p5";
	public final static String pricePerBook = "p6";
	public final static String exDividentRate = "q";
	public final static String PERatio = "r";
	public final static String dividentPayDate = "r1";
	public final static String PERatioRT = "r2";
	public final static String PEGRatio = "r5";
	public final static String pricePerEPSCurrYear = "r6";
	public final static String pricePerEPSNextYear = "r7";
	public final static String symbol = "s";
	public final static String sharesOwned = "s1";
	public final static String shortRatio = "s7";
	public final static String lastTradeTime = "t1";
	public final static String tradeLinks = "t6";
	public final static String tickerTrend = "t7";
	public final static String oneYearTargetPrice = "t8";
	public final static String volume = "v";
	public final static String holdingsValue = "v1";
	public final static String holdingsValueRT = "v7";
	public final static String yearlyRange = "w";
	public final static String daysValueChange = "w1";
	public final static String daysValueChangeRT = "w4";
	public final static String stockExchange = "x";
	public final static String dividentYield = "y";
}
