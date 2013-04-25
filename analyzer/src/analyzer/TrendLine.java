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

	int value;
	private SimpleData[] data;
	private int offset;
	private SimpleData[] l, lmax, lmin;
	private Line[] lines, upperlines, lowerlines; //array for every trendline created and its upper and lower boundaries
	final int S = 5; //A trendline every 5th day
	
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
		this.offset = offset;
		this.data = new SimpleData[data.size()];
		this.data = data.toArray(this.data);
		l = new SimpleData[data.size()];
		lmax = new SimpleData[data.size()];
		lmin = new SimpleData[data.size()];
		lines = new Line[data.size()/S+1];
		upperlines = new Line[data.size()/S+1];
		lowerlines = new Line[data.size()/S+1];
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
		for(int x = start; x < stop; x++){
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
			if (end >= data.length) 
				end = data.length-1;
			lines[i/S] = calcLinearRegression(i, end);
		}
		
		for(int i = 0; i<data.length; i++) {
			l[i] = new SimpleData(data[i].getStock(), data[i].getDate(), lines[i/S].getY(i));
		}
	}
	
	/**
	 * Create upper and lower bounds
	 */
	private void createTrendChannels(){
		//Jag la till +1 så att det inte blir indes out of bounds eller nullpointerexception...
		// Den verkar dock inte bete sig som väntat på sista värdet så kan du kolla på det johanna?
		int[] maxpos = new int[data.length / S +1];
		int[] minpos = new int[data.length / S +1];
		double[] maxy = new double[data.length / S +1];
		double[] miny = new double[data.length / S +1];
		
		for (int i = 0; i < data.length/S; i++) {
			maxy[i] = 0;
			miny[i] = 100000;
		}
		for(int i = 0; i< data.length; i++){
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
		
		for(int i = 0; i< maxpos.length; i++){
						
			double ymax = maxy[i];
			double ymin = miny[i];
			double b = lines[i].getB();
			upperlines[i] = new Line(ymax-b*maxpos[i], b);
			lowerlines[i] = new Line(ymin-b*minpos[i], b);
			
			System.out.println("Middle: " + lines[i]);
			System.out.println("Mmax: " + upperlines[i]);
			System.out.println("Min: " + lowerlines[i]);
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
		
		Double value = this.value();
		Signal signal = Signal.NONE;
		return new Result("Trend lines", value, this.resultString(), this.getGraph(), signal);
	}

	@Override
	public Signal getSignal() {
		return Signal.NONE;
	}

}
