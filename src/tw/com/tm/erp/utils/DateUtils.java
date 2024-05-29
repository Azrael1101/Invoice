package tw.com.tm.erp.utils;

/****************************************************************************
 Module: 日期轉換處理 
 Version: 1.0
 *****************************************************************************/

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 日期轉換工具
 */
public class DateUtils {
	public static final String C_DATE_DIVISION = "-";

	public static final String C_TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public static final String C_TIME_PATTON_SLASH = "yyyy/MM/dd HH:mm:ss";
	public static final String C_DATE_PATTON_DEFAULT = "yyyy-MM-dd";
	public static final String C_DATE_PATTON_SLASH = "yyyy/MM/dd";
	public static final String C_DATA_PATTON_YYYYMMDD = "yyyyMMdd";
	public static final String C_DATA_PATTON_YYMMDD = "yyMMdd";
	public static final String C_TIME_PATTON_HHMMSS = "HH:mm:ss";
	public static final String C_TIME_PATTON_STAMP = "yyyyMMddHHmmssSS";
	public static final int C_ONE_SECOND = 1000;
	public static final int C_ONE_MINUTE = 60 * C_ONE_SECOND;
	public static final int C_ONE_HOUR = 60 * C_ONE_MINUTE;
	public static final long C_ONE_DAY = 24 * C_ONE_HOUR;

	/**
	 * Return the current date
	 * 
	 * @return － DATE<br>
	 */
	public static Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();

		return currDate;
	}

	/**
	 * Return the current date string
	 * 
	 * @return － 產生的日期字串
	 */
	public static String getCurrentDateStr() {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();

		return format(currDate);
	}

	/**
	 * Return the current date in the specified format
	 * 
	 * @param strFormat
	 * @return
	 */
	public static String getCurrentDateStr(String strFormat) {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();

		return format(currDate, strFormat);
	}

	/**
	 * Parse a string and return a date value
	 * 
	 * @param dateValue
	 * @return
	 * @throws Exception
	 */
	public static Date parseDate(String dateValue) {
		return parseDate(C_DATE_PATTON_DEFAULT, dateValue);
	}

	/**
	 * Parse a strign and return a datetime value
	 * 
	 * @param dateValue
	 * @return
	 */
	public static Date parseDateTime(String dateValue) {
		return parseDate(C_TIME_PATTON_DEFAULT, dateValue);
	}

	/**
	 * Parse a string and return the date value in the specified format
	 * 
	 * @param strFormat
	 * @param dateValue
	 * @return
	 * @throws ParseException
	 * @throws Exception
	 */
	public static Date parseDate(String strFormat, String dateValue) {
		if (dateValue == null)
			return null;

		if (strFormat == null)
			strFormat = C_TIME_PATTON_DEFAULT;

		SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
		Date newDate = null;

		try {
			newDate = dateFormat.parse(dateValue);
		} catch (ParseException pe) {
			newDate = null;
		}

		return newDate;
	}

	/**
	 * Parse a string and return the date value in the specified format
	 * 
	 * @param strFormat
	 * @param dateValue
	 * @return
	 * @throws ParseException
	 * @throws Exception
	 */
	public static Date parseTDate(String strFormat, String dateValue) {
		if (dateValue == null)
			return null;
		if (strFormat == null)
			strFormat = C_DATA_PATTON_YYMMDD;
		SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
		Date newDate = null;
		try {
			newDate = dateFormat.parse(dateValue);
			Calendar c = Calendar.getInstance();
			c.setTime(newDate);
			c.add(Calendar.YEAR, 11);
			return c.getTime();
		} catch (ParseException pe) {
			newDate = null;
		}
		return newDate;
	}

	/**
	 * 將Timestamp類型的日期轉換為系統參數定義的格式的字串。
	 * 
	 * @param aTs_Datetime
	 *            需要轉換的日期。
	 * @return 轉換後符合給定格式的日期字串
	 */

	public static String format(Date aTs_Datetime) {
		return format(aTs_Datetime, C_DATE_PATTON_DEFAULT);
	}

	/**
	 * 將Timestamp類型的日期轉換為系統參數定義的格式的字串。
	 * 
	 * @param aTs_Datetime
	 *            需要轉換的日期。
	 * @return 轉換後符合給定格式的日期字串
	 */

	public static String formatTime(Date aTs_Datetime) {
		return format(aTs_Datetime, C_TIME_PATTON_DEFAULT);
	}

	/**
	 * 將Date類型的日期轉換為系統參數定義的格式的字串。
	 * 
	 * @param aTs_Datetime
	 * @param as_Pattern
	 * @return
	 */
	public static String format(Date aTs_Datetime, String as_Pattern) {
		if (null == aTs_Datetime || null == as_Pattern )
			return null;
		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Pattern);
		return dateFromat.format(aTs_Datetime);
	}

	/**
	 * @param aTs_Datetime
	 * @param as_Format
	 * @return
	 */
	public static String formatTime(Date aTs_Datetime, String as_Format) {
		if (aTs_Datetime == null || as_Format == null)
			return null;

		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Format);

		return dateFromat.format(aTs_Datetime);
	}

	public static String getFormatTime(Date dateTime) {
		return formatTime(dateTime, C_TIME_PATTON_HHMMSS);
	}

	public static String formatToTWDate(Date datetime, String pattern) {
		String formatDate = format(datetime, pattern);
		if (formatDate == null) {
			return null;
		} else {
			String year = formatDate.substring(0, 4);
			String twYear = String.valueOf(Integer.parseInt(year) - 1911);
			return twYear + formatDate.substring(4);
		}
	}

	public static String formatChangeToTWDate(Date datetime) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(datetime);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			month++;
			int day = c.get(Calendar.DAY_OF_MONTH);
			StringBuffer sb = new StringBuffer();
			sb.append(year - 1911);
			if (month < 10)
				sb.append("0");
			sb.append(month);
			if (day < 10)
				sb.append("0");
			sb.append(day);
			return sb.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * @param aTs_Datetime
	 * @param as_Pattern
	 * @return
	 */
	public static String format(Timestamp aTs_Datetime, String as_Pattern) {
		if (aTs_Datetime == null || as_Pattern == null)
			return null;

		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Pattern);

		return dateFromat.format(aTs_Datetime);
	}

	/**
	 * 取得指定日期N天後的日期
	 * 
	 * @param date
	 * @param days
	 * @return
	 */

	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);

		return cal.getTime();
	}

	/**
	 * 取得指定日期N月後的日期
	 * 
	 * @param date
	 * @param days
	 * @return
	 */

	public static Date addMonths(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);

		return cal.getTime();
	}
	
	/**
	 * 取得日期 沒有時間
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateWithoutTime(Date date) {
		if (null != date) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime();
		}
		return null;
	}

	/**
	 * 計算兩個日期之間相差的天數
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */

	public static int daysBetweenWithoutTime(Date date1, Date date2) {
		date1 = getDateWithoutTime(date1);
		date2 = getDateWithoutTime(date2);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 計算兩個日期之間相差的天數
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */

	public static int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 計算當前日期相對於"1977-12-01"的天數
	 * 
	 * @param date
	 * @return
	 */

	public static long getRelativeDays(Date date) {
		Date relativeDate = DateUtils.parseDate("yyyy-MM-dd", "1977-12-01");

		return DateUtils.daysBetween(relativeDate, date);
	}

	public static Date getDateBeforTwelveMonth() {
		String date = "";
		Calendar cla = Calendar.getInstance();
		cla.setTime(getCurrentDate());
		int year = cla.get(Calendar.YEAR) - 1;
		int month = cla.get(Calendar.MONTH) + 1;
		if (month > 9) {
			date = String.valueOf(year) + C_DATE_DIVISION + String.valueOf(month) + C_DATE_DIVISION + "01";
		} else {
			date = String.valueOf(year) + C_DATE_DIVISION + "0" + String.valueOf(month) + C_DATE_DIVISION + "01";
		}

		Date dateBefore = parseDate(date);
		return dateBefore;
	}

	/**
	 * 傳入時間字串,加一天後返回Date
	 * 
	 * @param date
	 *            時間 格式 YYYY-MM-DD
	 * @return
	 */

	public static Date addDate(String date) {
		if (date == null)
			return null;

		Date tempDate = parseDate(C_DATE_PATTON_DEFAULT, date);
		String year = format(tempDate, "yyyy");
		String month = format(tempDate, "MM");
		String day = format(tempDate, "dd");

		GregorianCalendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));

		calendar.add(GregorianCalendar.DATE, 1);
		return calendar.getTime();
	}

	public static Date getShortDate(Date date) {
		String dateString = format(date);
		return parseDateTime(dateString + " 00:00:00");
	}

	/**
	 * 取得指定月份的第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth( Date date, int nextMonths ) {
	    Calendar   cal   =   Calendar.getInstance(); 
	    cal.setTime(date);
	    int   minDate    =   cal.getActualMinimum(Calendar.DATE);   
	    cal.add(Calendar.MONTH,nextMonths);
	    cal.set(Calendar.DATE,minDate);
	    return cal.getTime();   
	}
	
	public static Date getLastDateOfMonth(Date date) throws Exception {
		String dateString = format(date);
		String[] dateArray = StringTools.StringToken(dateString, "-");
		String year = dateArray[0];
		String month = dateArray[1];
		int lastDay = getLastDayOfMonth(Integer.parseInt(year), Integer.parseInt(month));
		StringBuffer lastDate = new StringBuffer(year);
		lastDate.append("-");
		lastDate.append(month);
		lastDate.append("-");
		lastDate.append(lastDay);
		lastDate.append(" 00:00:00");
		return parseDateTime(lastDate.toString());
	}

	public static int getLastDayOfMonth(int year, int month) {
		Calendar calendar = new GregorianCalendar(year, month, 1);
		calendar.add(Calendar.DATE, -1);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 回傳年月的最後一天
	 */
	public static String getLastDateOfLastMonth(Date date) throws Exception {
		String dateString = format(date);
		String[] dateArray = StringTools.StringToken(dateString, "-");
		String year = dateArray[0];
		String month = dateArray[1];
		Calendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
		calendar.add(Calendar.DATE, -1);
		return format(calendar.getTime(), DateUtils.C_DATA_PATTON_YYYYMMDD);
	}
	
	public static String shortDateConverter(String dateValue) throws Exception {
	    
	        String[] dateArray = StringTools.StringToken(dateValue, "/");
	        String year = dateArray[0];
                String month = dateArray[1];
                String day = dateArray[2];
                int monthTmp = Integer.parseInt(month);
                int dayTmp = Integer.parseInt(day);
                
                StringBuffer formatDateValue = new StringBuffer();
                formatDateValue.append(year);
                formatDateValue.append("/");
                formatDateValue.append(monthTmp);
                formatDateValue.append("/");
                formatDateValue.append(dayTmp);
	        return formatDateValue.toString();
	}
	
	public static String formatTWDateToDate(String twDate){
		
		String twYear = twDate.substring(0, 2);
		String year = String.valueOf(Integer.parseInt(twYear) + 1911);
		StringBuffer formatDateValue = new StringBuffer(year);
		formatDateValue.append(twDate.substring(2, 6));
		return formatDateValue.toString();		
	}
	
	public static String formatTWToDate(String twDate){
	    int dateLen = twDate.length();
	    if(dateLen == 6){ // 民國yymmdd
		String twYear = twDate.substring(0, dateLen-4);
		System.out.println("twYear = " + twYear);
		String year = String.valueOf(Integer.parseInt(twYear) + 1911);
		System.out.println("year = " + year);
		StringBuffer formatDateValue = new StringBuffer(year);
		formatDateValue.append(twDate.substring(dateLen-4, dateLen));
		System.out.println("formatDateValue = " + formatDateValue);
		return formatDateValue.toString();
	    }else{
		return twDate; // 西元yyyymmdd 
	    }
	}

	/**
	 * 取得民國年
	 * @param strFormat
	 * @return
	 */
	public static String getROCDateStr() {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();

		Integer year = Integer.valueOf(format(currDate, "yyyy")) - 1911;
		return year.toString();
	}
	
	/**
	 * 以指定日期為底指定N天前或後的日期
	 * @param date
	 * @return
	 */
	public static String getAppointDateCompareDate(Date date,int appointDate) {
	    Calendar   cal = Calendar.getInstance();   
	    cal.setTime(date);
	    cal.add(Calendar.DATE,appointDate);   

	    int yearInt = cal.get(Calendar.YEAR);
	    int monthInt = cal.get(Calendar.MONTH) + 1;
	    int dateInt = cal.get(Calendar.DATE);
	    DecimalFormat dfNumber = new DecimalFormat("00");
	    
	    return String.valueOf(yearInt) + C_DATE_DIVISION + dfNumber.format(monthInt) + C_DATE_DIVISION + dfNumber.format(dateInt);
	}
	
	/**
	 * 以當前日期為底指定N天前或後的日期
	 * @param date
	 * @return
	 */
	public static String getAppointDateCompareDate(int appointDate) {
	    return getAppointDateCompareDate(new Date(), appointDate);
	}
	
	/**
	 * 將兩日期字串區間轉list
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getDaysBetweenList(String startDate, String endDate) {
	    List list = new ArrayList();
	    Date sDate = parseDate(startDate);
	    Date eDate = parseDate(endDate);
	    
	    int differenceDate = DateUtils.daysBetweenWithoutTime(sDate, eDate) + 1;
	    for (int i = 0; i < differenceDate; i++) {
		list.add(getAppointDateCompareDate(sDate,i));
	    }
	    return list;
	}
	
	/**
	 * 回傳年月的最後一個日期
	 */
	public static Date getLastDateOfMonth(int year, int month) {
		Calendar calendar = new GregorianCalendar(year, month, 1);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}
	
	
	public static void main(String[] args) {
		// Date date1 = DateUtil.addDays(DateUtil.getCurrentDate(),1);
		// Date date2 = DateUtil.addDays(DateUtil.getCurrentDate(),101);
		// 
		// System.out.println(DateUtil.getRelativeDays(date1));
		// System.out.println(DateUtil.getRelativeDays(date2));

		// Timestamp date = new Timestamp(801);
		//       
		// System.out.println(date);
		// String strDate = DateUtil.format(date, C_DATA_PATTON_YYYYMMDD);

		// System.out.println(strDate);

		String date = "2006-07-31";
		System.out.println(date);
		Date date2 = addDate(date);
		System.out.println(date2);

	}
}
