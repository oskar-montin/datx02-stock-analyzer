package data;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * Class that handles big numbers. The number is divided into one value part, and one suffix part. The suffixes are K (thousands)
 * M (million) and B (billion).
 * 
 * @author oskar-montin, oskarnylen
 */
public class LargeDouble implements Comparable<LargeDouble>{
	private BigDecimal value;
	private String fullString;

	public LargeDouble(String s) {
		String temp;
		String temp2;
		String temp3;

		if( s == null || s.equals("") ) {
			this.fullString = null;
			value = new BigDecimal(0);
			return;
		} else {
			this.fullString = s;
		}
		
		if(s.contains("K")){
			temp = s.replace("K", "e3");
		}else if(s.contains("M")){
			temp = s.replace("M", "e6");
		}else if(s.contains("B")){
			temp = s.replace("B", "e9");
		}else if(s.contains("T")){
			temp = s.replace("T", "e12");
		}else {
			temp = s;
		}
		
		temp2 = temp.replace("(", "");
		temp3 = temp2.replace(")", "");
		
		value = new BigDecimal(temp3);
	}

	public LargeDouble(BigDecimal d) {
		value = d;
	}

	/**
	 * Adds a LargeDouble
	 * 
	 * @param d
	 * @return
	 */
	public LargeDouble add(LargeDouble d) {
		return new LargeDouble(this.value.add(d.getValue()));
	}


	/**
	 * Subtracts a large double
	 * 
	 * @param d
	 * @return
	 */
	public LargeDouble sub(LargeDouble d) {
		return new LargeDouble(this.value.subtract(d.getValue()));
	}

	/**
	 * Multiplies two large doubles
	 * 
	 * @param d
	 * @return
	 */
	public LargeDouble mul(LargeDouble d) {
		return new LargeDouble(this.value.multiply(d.getValue()));
	}

	/**
	 * Divides two LargeDoubles
	 * 
	 * @param d
	 * @param precision the amount of decimals allowed in te division
	 * @return
	 */
	public LargeDouble div(LargeDouble d, int precision) {
		BigDecimal bd = null;
		try{
			bd = this.value.divide(d.getValue());
			return new LargeDouble(bd);
		} catch(Exception e) {
			Double dv1 = this.value.unscaledValue().doubleValue();
			Double dv2 = d.getValue().unscaledValue().doubleValue();
			Double div = dv1/dv2;
			int newScale = this.value.scale()-d.getValue().scale();
			while(newScale<precision && div <100) {
				div*=10;
				newScale++;
			}
			int i= div.intValue();
			bd = new BigDecimal(new BigInteger(""+i),newScale);
		}
		//double a = this.toDouble()/d.toDouble();
		return new LargeDouble(bd);
	}

	public double toDouble() {
		return this.value.doubleValue();
	}

	public String toString() {
		if(this.value.intValue()==0) {
			return new String("0");
		}
		if(fullString!=null) {
			return fullString;
		} else {
			BigDecimal temp = this.value;
			BigInteger unscaled = temp.unscaledValue();
			double scaledVal;
			int scale = -this.value.scale();
			String unscaledString = unscaled.toString();
			int digits=unscaledString.length();
			scale +=digits-1;
			if(scale%3!=0) {
				scale -= scale%3;
			}
			scaledVal = Double.parseDouble(unscaledString);
			int exp = (int)Math.pow(10, scale-(-this.value.scale()));
			scaledVal /= exp;


			Suffix[] suffixes = Suffix.values();
			for(int i = suffixes.length-1; i>=0;i--) {
				if(scale>=suffixes[i].getExponent()) {
					return new String(scaledVal+suffixes[i].getSuffix());
				}
			}
			return unscaled.toString();
		}
	}

	public BigDecimal getValue() {
		return value;
	}



	enum Suffix {
		NONE(0,""),K(3,"K"),M(6,"M"),B(9,"B"),T(12,"T");
		int exponent;
		String suffix;
		Suffix(int exponent, String suffix) {
			this.exponent = exponent;
			this.suffix = suffix;
		}
		public int getExponent() {
			return this.exponent;
		}
		public String getSuffix() {
			return this.suffix;
		}
	}



	@Override
	public int compareTo(LargeDouble arg0) {
		return this.value.compareTo(arg0.getValue());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
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
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}


}