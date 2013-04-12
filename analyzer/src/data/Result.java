package data;

import java.util.Arrays;
import java.util.LinkedList;

public class Result {

	private double value;
	private String returnString;
	private String name;
	private LinkedList<Curve> curves;
	private Signal signal;
	
	public Result(String name, double value, String returnString, Curve[] curves, Signal signal){
		this.curves = new LinkedList<Curve>();
		this.name = name;
		this.value = value;
		this.returnString = returnString;
		this.curves.addAll(Arrays.asList(curves));
//		System.arraycopy(curves, 0, this.curves, 0, curves.length);
		this.signal = signal;
	}
	
	public void addCurve(Curve curve){
		curves.add(curve);
	}
	
	public double getValue() {
		return value;
	}

	public String getReturnString() {
		return returnString;
	}

	public String getName() {
		return name;
	}

	public LinkedList<Curve> getCurves() {
		return curves;
	}

	public Signal getSignal() {
		return signal;
	}
}
