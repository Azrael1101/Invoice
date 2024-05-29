package tw.com.tm.erp.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtils {

	public static double getDouble(Double d) {
		try {
			return Double.valueOf(d).doubleValue();
		} catch (Exception ex) {
			return 0;
		}
	}

	public static long getLong(Long l) {
		try {
			return Long.valueOf(l).longValue();
		} catch (Exception ex) {
			return 0;
		}
	}

	public static int getInt(Integer i) {
		try {
			return Integer.valueOf(i).intValue();
		} catch (Exception ex) {
			return 0;
		}
	}

	public static Double getDouble(String d) {
		try {
			return Double.valueOf(d);
		} catch (Exception ex) {
			return 0D;
		}
	}

	public static Long getLong(String l) {
		try {
			return Long.valueOf(l);
		} catch (Exception ex) {
			return 0L;
		}
	}

	public static int getInt(String i) {
		try {
			return Integer.valueOf(i).intValue();
		} catch (Exception ex) {
			return 0;
		}
	}

	public static double getDouble(BigDecimal d) {
		try {
			return d.doubleValue();
		} catch (Exception ex) {
			return 0;
		}
	}
	
	public static Double round(Double v, int scale) {
		if(null == v){
			return 0D;
		}else{
			BigDecimal value = new BigDecimal(v);
			try {
				double roundNum = value.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
				if (scale <= 0)
					return roundNum;
				String temp = "#####0.";
				for (int i = 0; i < scale; i++) {
					temp += "0";
				}
				return Double.valueOf(new DecimalFormat(temp).format(v));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return value.doubleValue();
		}
	}

	public static double round(double v, int scale, boolean b) {
		if(b){
			if(NumberUtils.getDouble(v) < 0){
				v = v*(-1);
				return OperationUtils.ceilUp(v, scale).doubleValue()*(-1);
			}else
				return OperationUtils.ceilUp(v, scale).doubleValue();
		}else{
			return round( v, scale);
		}
	}
	
	public static String roundToStr(double v, int scale) {
		double d = round(v, scale);
		if( d > 9999999 )
			return (new BigDecimal(d)).toPlainString();
		else if( d < -9999999 )
			return (new BigDecimal(d)).toPlainString();
		else
			return String.valueOf(d);
	}

	public static String roundToStr(double v, double exchangeRate ,int scale) {
		try{
			double d = round(v/exchangeRate, scale);
			if( d > 9999999 )
				return (new BigDecimal(d)).toPlainString();
			else if( d < -9999999 )
				return (new BigDecimal(d)).toPlainString();
			else
				return String.valueOf(d);
		}catch (Exception ex) {
			return null;
		}
	}
	
	public static Double localtoOriginal(Double origin , Double exchangeRate){
		try{
			return origin/exchangeRate;
		}catch (Exception ex) {
			return null;
		}
	}

}
