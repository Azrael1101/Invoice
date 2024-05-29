/**
 * Copyright © 2008 Tasameng Corperation. All rights reserved.
 * -----------------------------------------------------------
 * Create Date Apr 1, 2008
 */
package tw.com.tm.erp.utils;

import java.math.BigDecimal;

/**
 * @author Dumars.Tsai
 */
public class OperationUtils {
	
	// 預設小數點位數
	private static final int DEFAULT_DECIMAL_POINT = 10;
	/**
	 * 加法(v1 + v2)
	 * @param v1
	 * @param v2
	 * @return Number
	 */
	public static Number add(Number v1, Number v2) {
		return bigDec(v1).add(bigDec(v2));
	}
	
	/**
	 * 減法(v1 - v2)
	 * @param v1
	 * @param v2
	 * @return Number
	 */
	public static Number subtraction(Number v1, Number v2) {
		return bigDec(v1).subtract(bigDec(v2));
	}
	
	/**
	 * 乘法(v1 * v2)
	 * @param v1
	 * @param v2
	 * @return Number
	 */
	public static Number multiplication(Number v1, Number v2) {
		return bigDec(v1).multiply(bigDec(v2));
	}
	
	/**
	 * 除法(v1 / v2)，當有除不盡的時候，取到小數點第10位，之後的數字四捨五入
	 * @param v1
	 * @param v2
	 * @return Number
	 */
	public static Number divison(Number v1, Number v2) {
		return divison(v1, v2, DEFAULT_DECIMAL_POINT);
	}
	
	/**
	 * 除法(v1 / v2)，當有除不盡的時候，由scale參數提供精確度到第幾位，之後的數字四捨五入
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return Number
	 */
	public static Number divison(Number v1, Number v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		return bigDec(v1).divide(bigDec(v2), scale, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 小數位四捨五入處理
	 * @param v 需四捨五入的數字
	 * @param scale 小數點保留位數
	 * @return Number
	 */
	public static Number round(Number v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		return bigDec(v).divide(bigDec(1), scale, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 小數位四捨五入處理
	 * @param v 需四捨五入的數字
	 * @param scale 小數點保留位數
	 * @return Number
	 */
	public static String roundToStr(Number v, int scale) {
		try{
			if (scale < 0) {
				throw new IllegalArgumentException("The scale must be a positive integer or zero");
			}
			return bigDec(v).divide(bigDec(1), scale, BigDecimal.ROUND_HALF_UP).toString();
		}catch(Exception e){
			//e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 小數位前四捨五入處理
	 * @param v 需四捨五入的數字
	 * @param scale 需四捨五入位數
	 * @return Number
	 */
	public static Number roundUp(Number v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		//Long x= Long.valueOf(("1"+CommonUtils.insertCharacterWithFixLength("",scale,"0")));
		//return Math.round(v.longValue()/x)*x;
		Integer x= Integer.valueOf(("1"+CommonUtils.insertCharacterWithFixLength("",scale,"0")));
		//return Math.round(divison(v.doubleValue(),x,DEFAULT_DECIMAL_POINT).doubleValue())*x;
		if( x.doubleValue() >= v.doubleValue()){
			return 0;
		}else{
			return Math.round(divison(v.doubleValue(),x,DEFAULT_DECIMAL_POINT).doubleValue())*x;
		}
	}
	
	/**
	 * 小數位前無條件捨去處理
	 * @param v 需無條件捨去的數字
	 * @param scale 需無條件捨去位數
	 * @return Number
	 */
	public static Number floorUp(Number v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		Integer x= Integer.valueOf(("1"+CommonUtils.insertCharacterWithFixLength("",scale,"0")));
		if( x.doubleValue() >= v.doubleValue()){
			return 0;
		}else{
			return Math.floor(divison(v.doubleValue(),x,DEFAULT_DECIMAL_POINT).doubleValue())*x;
			
		}
	}
	
	/**
	 * 小數位前無條件進位處理
	 * @param v 需無條件進位去的數字
	 * @param scale 需無條件進位位數
	 * @return Number
	 */
	public static Number ceilUp(Number v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		Integer x= Integer.valueOf(("1"+CommonUtils.insertCharacterWithFixLength("",scale,"0")));
		return Math.ceil(divison(v.doubleValue(),x,DEFAULT_DECIMAL_POINT).doubleValue())*x;
	}
	
	private static BigDecimal bigDec(Number num) {
		return new BigDecimal(String.valueOf(num));
	}
}
