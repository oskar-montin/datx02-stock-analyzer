package analyzer;

import java.util.PriorityQueue;
import data.Curve;
import data.Result;
import data.Signal;
import data.SimpleData;

/**
 * TrendLine class
 * 
 * This class will draw trend lines using linear regression channels
 * @author Johanna
 *
 */
public class TrendLine implements AnalysisMethod{

	private int value;
	private SimpleData[] data;
	private SimpleData[] l, lmax, lmin;
	private Line[] lines, upperlines, lowerlines; //array for every trendline created and its upper and lower boundaries
	private int S;
	private int skipped;
	
	/**
	 * Private class Line which represents a linear equation y = a + bx
	 *
	 */
	private class Line {
		private double a,b;
		public Line(double a, double b){
			this.a = a;
			this.b = b;
		}
		public double getY(double x){
			return a + b*x;
		}
		
		public double getB(){
			return b;
			
		}
		public String toString() {
			return "y = " + a + " + " + b + "*x";
		}
		
	}
	
	//constructor that creates trend lines
	public TrendLine(PriorityQueue<? extends SimpleData> data, int offset) {
		SimpleData temp[] = new SimpleData[data.size()];
		this.S = offset;
		temp = data.toArray(temp);
		int size = data.size() - data.size() % S;
		skipped = data.size() % S;
		this.data = new SimpleData[size];
		System.arraycopy(temp, temp.length % S, this.data, 0, size);
		l = new SimpleData[size];
		lmax = new SimpleData[size];
		lmin = new SimpleData[size];
		lines = new Line[size/S];
		upperlines = new Line[size/S];
		lowerlines = new Line[size/S];
		createTrendLine();
		createTrendChannels();
	}
	
	/**
	 * Calculate linear regression y = a + bx,
	 * where y is the value of the stock,
	 * a is the y-intercept and
	 * b is the slope.
	 * @param start Start of interval
	 * @param stop End of interval
	 */
	private Line calcLinearRegression(int start, int stop) {
		double a, b, y;
		double sumy = 0;
		double sumx = 0;
		double n = stop-start+1;
		
		for(int x = start; x <= stop; x++){
			y = data[x].getValue();
			sumy += y;
			sumx += x;
		}
		
		double xbar = sumx / n;
		double ybar = sumy / n;
		double xxbar = 0, xybar = 0;
		for(int x = start; x < stop; x++) {
			y = data[x].getValue();
			xxbar += (x-xbar)*(x-xbar);
			xybar += (x-xbar)*(y-ybar);
		}
		b = xybar / xxbar;
		a = ybar - b*xbar;
		return new Line(a,b);
	}
	
	/**
	 * Create trend line
	 */
	private void createTrendLine() {
		
		for (int i = 0; i < data.length; i+=S) {
			int end = i+S-1;
			lines[i/S] = calcLinearRegression(i, end);
		}
		
		for(int i = 0; i < data.length; i++) {
			l[i] = new SimpleData(data[i].getStock(), data[i].getDate(), lines[i/S].getY(i));
		}
	}
	
	/**
	 * Create upper and lower bounds
	 */
	private void createTrendChannels(){
		
		int[] maxpos = new int[data.length / S];
		int[] minpos = new int[data.length / S];
		double[] maxy = new double[data.length / S];
		double[] miny = new double[data.length / S];
		
		for (int i = 0; i < maxpos.length; i++) {
			maxy[i] = 0;
			miny[i] = 100000;
		}
		for(int i = 0; i < data.length; i++) {
			int lineNr = i / S;
			
			if(data[i].getValue() > maxy[lineNr]) {
				maxpos[lineNr] = i;
				maxy[lineNr] = data[i].getValue();
			}
			if(data[i].getValue() < miny[lineNr]) {
				minpos[lineNr] = i;	
				miny[lineNr] = data[i].getValue();
			}
		}
		
		for(int i = 0; i < maxpos.length; i++) {
			double ymax = maxy[i];
			double ymin = miny[i];
			double b = lines[i].getB();
			upperlines[i] = new Line(ymax-b*maxpos[i], b);
			lowerlines[i] = new Line(ymin-b*minpos[i], b);
		}
		
		for(int i = 0; i < data.length; i++) {
			lmax[i] = new SimpleData(data[i].getStock(), data[i].getDate(), upperlines[i/S].getY(i));
			lmin[i] = new SimpleData(data[i].getStock(), data[i].getDate(), lowerlines[i/S].getY(i));
		}
		
	}
	
	/**
	 * Return slope of trend line at given x
	 * @param x Position in data array
	 * @return Slope of trend line
	 */
	public double getTrend(int x){
		int lineNr = x/S;
		return lines[lineNr].getB();
	}
	
	/**
	 * Return slope of trend line in latest trend
	 * @return Slope of trend line
	 */
	public double getLatestTrend() {
		int lineNr = (data.length-1)/S;
		return lines[lineNr].getB();
	}
	
	/**
	 * Searches for true local min given approx position.
	 * @param x Position close to local min
	 * @return The position of local min
	 */
	private int findTrueMinPos(int x) {
		
		if (x > 0 && data[x].getValue() > data[x-1].getValue()) {
			while (data[x].getValue() > data[x-1].getValue()) {
				x--;
			}
		}
		else if (x < data.length-2 && data[x].getValue() > data[x+1].getValue()) {
			while (data[x].getValue() > data[x+1].getValue()) {
				x++;
			}
		}
		x += skipped;
		//System.out.println("True min: " +x);
		return x;
	}
	
	public int getLatestMinPos() {
		int x = data.length - 1;
		// Increasing
		if (getTrend(x) > 0) {
			
			while (getTrend(x) > 0 && x >= 0) {
				x--;
			}
		}
		// Declining
		else { 
			while (getTrend(x) < 0 && x >= 0) {
				x--;
			}
			while (getTrend(x) > 0 && x >= 0) {
				x--;
			}
		}
		return findTrueMinPos(x+1);
	}
	

	/**
	 * Searches for true local max given approx position.
	 * @param x Position close to local max
	 * @return The position of local max
	 */
	private int findTrueMaxPos(int x) {
		
		if (x > 0 && data[x].getValue() < data[x-1].getValue()) {
			while (data[x].getValue() < data[x-1].getValue()) {
				x--;
			}
		}
		else if (x < data.length-2 && data[x].getValue() < data[x+1].getValue()) {
			while (data[x].getValue() < data[x+1].getValue()) {
				x++;
			}
		}
		x += skipped;
		return x;
	}
	
	public int getLatestMaxPos() {
		int x = data.length - 1;
		// Decreasing
		if (getTrend(x) < 0) {
			
			while (getTrend(x) < 0 && x >= 0) {
				x--;
			}
		}
		// Increasing
		else { 
			while (getTrend(x) > 0 && x >= 0) {
				x--;
			}
			while (getTrend(x) < 0 && x >= 0) {
				x--;
			}
		}
		return findTrueMaxPos(x+1);
	}
	
	
	@Override
	public String resultString() {
		return "Linear regression channels";
	}

	@Override
	public double value() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Curve[] getGraph() {
		Curve[] curves = new Curve[4];
		curves[0] = new Curve(data, "Price");
		curves[1] = new Curve(l,"Trend");
		curves[2] = new Curve(lmax,"Upper");
		curves[3] = new Curve(lmin,"Lower");
		return curves;
	}

	@Override
	public Result getResult() {
		Double value = value();
		Signal signal = getSignal();
		return new Result("Trend lines", value, this.resultString(), this.getGraph(), signal);
	}

	@Override
	public Signal getSignal() {
		double LIMIT = 0.01;
		int x = data.length-1;
		double lastUpper,  lastLower;
		double current = data[x].getValue();
		Line ul = upperlines[upperlines.length-1];
		Line ll = lowerlines[lowerlines.length-1];
		lastUpper = ul.getY(x);
		lastLower = ll.getY(x);
		if( Math.abs((lastUpper - current)/ lastUpper) < LIMIT){
				return Signal.SELL;
		}
		if( ((current - lastLower)/ lastLower) < LIMIT)
			return Signal.BUY;
		else
			return Signal.NONE;
	}

}
