package data;


/**
 * Class that handles big numbers. The number is divided into one value part, and one suffix part. The suffixes are K (thousands)
 * M (million) and B (billion).
 * 
 * @author oskarnylen
 */
public class LargeDouble implements Comparable<LargeDouble> {
	
	private Double value;
	private String suffix = "";
	private long suffixLong;

	private String fullValue;

	/**
	 * Constructor with a string as a parameter. Divides the value and the suffix into variables.
	 * 
	 * @param s - a LargeDouble as a string
	 */
	public LargeDouble(String s){
		fullValue = s;

		if(s.contains("K")){
			String temp = s.replace("K", "");

			suffix = "K";
			suffixLong = 1000;
			value = Double.parseDouble(temp);
		}else if(s.contains("M")){
			String temp = s.replace("M", "");

			suffix = "M";
			suffixLong = 1000000;
			
			value = Double.parseDouble(temp);
		}else if(s.contains("B")){
			String temp = s.replace("B", "");

			suffix = "B";
			suffixLong = 1000000000;
			
			value = Double.parseDouble(temp);
		}else {
			
			suffixLong = 1;
			value = Double.parseDouble(s);
		}

	}

	/**
	 * A constructor with a double as parameter. Sets the value of the LargeDouble to the parameter, and the suffix to
	 * null
	 * @param d
	 */
	public LargeDouble(Double d){
		
		if(d < 1000){
			suffix = "";
			suffixLong = 1;
			value = d;
			fullValue = Double.toString(d);
		} else if(d >= 1000 && d < 1000000){
			suffix = "K";
			suffixLong = 1000;
			d = d/1000;
			value = d;
			fullValue = Double.toString(d)+"K";
		} else if(d >= 1000000 && d < 1000000000){
			suffix = "M";
			suffixLong = 1000000;
			d = d/1000000;
			value = d;
			fullValue = Double.toString(d)+"M";
		} else {
			suffix = "B";
			suffixLong = 1000000000;
			d = d/1000000000;
			value = d;
			fullValue = Double.toString(d)+"B";
		}
		
		
	}

	public String getSuffix() {
		return suffix;
	}

	public Double getValue() {
		return value;
	}

	/**
	 * Adds a LargeDouble
	 * 
	 * @param d
	 * @return
	 */
	public LargeDouble add(LargeDouble d) {
		// TODO Auto-generated catch block
		return d;
	}

	/**
	 * Subtracts a large double
	 * 
	 * @param d
	 * @return
	 */
	public LargeDouble sub(LargeDouble d) {
		/*
		 * Normalize both numbers to millions
		 */
		double firstValue = makeComparable(this);
		double secondValue = makeComparable(d);
		
		double sum = firstValue-secondValue;
		
		
		
		/*
		 * Normalize them back
		 */
		sum = sum*1000000;
		
		LargeDouble returnValue = new LargeDouble(sum);
		return returnValue;
	}

	/**
	 * Multiplies two large doubles
	 * 
	 * @param d
	 * @return
	 */
	public int mul(LargeDouble d) {
		// TODO Auto-generated catch block
		return 0;
	}

	/**
	 * Divides two LargeDoubles
	 * 
	 * @param d
	 * @return
	 */
	public int div(LargeDouble d) {
		// TODO Auto-generated catch block
		return 0;
	}

	/**
	 * Method to make the LargeDouble comparable. Mainly a help method for compareTo.
	 * 
	 * @param d - a LargeDouble
	 * @return - a double normalized as millions
	 */
	public double makeComparable(LargeDouble d){
		double comparableValue;

		if(d.getSuffix() == "B"){
			comparableValue = d.getValue()*1000;

		} else if(d.getSuffix() == "M"){
			comparableValue = d.getValue();

		} else if(d.getSuffix() == "K"){
			comparableValue = d.getValue()/1000;
		} else {
			comparableValue = d.getValue()/1000000;
		}
		return comparableValue;
	}

	@Override
	public int compareTo(LargeDouble d) {

		double thisComparableValue = makeComparable(this);
		double thatComparableValue = makeComparable(d);

		if(thisComparableValue > thatComparableValue){
			return -1;
		}else if(thisComparableValue < thatComparableValue){
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public String toString() {
		return fullValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LargeDouble other = (LargeDouble) obj;
		if (fullValue == null) {
			if (other.fullValue != null)
				return false;
		} else if (!fullValue.equals(other.fullValue))
			return false;
		if (suffix == null) {
			if (other.suffix != null)
				return false;
		} else if (!suffix.equals(other.suffix))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fullValue == null) ? 0 : fullValue.hashCode());
		result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
}

