package data;

public class Result {

	double value;
	String returnString;
	String name;
	Curve curves;
	Signal signal;
	
	public Result(String name, double value, String returnString, Curve curves, Signal signal){
		this.name = name;
		this.value = value;
		this.returnString = returnString;
		this.curves = curves;
		this.signal = signal;
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

	public Curve getCurves() {
		return curves;
	}

	public Signal getSignal() {
		return signal;
	}
}
