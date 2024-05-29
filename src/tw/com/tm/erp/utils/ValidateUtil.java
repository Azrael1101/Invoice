package tw.com.tm.erp.utils;

import java.util.regex.Pattern;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.hbm.service.BuOrderTypeService;

public class ValidateUtil {

    private static final Log log = LogFactory.getLog(ValidateUtil.class);

    public static final Long INITIAL_YEAR = 1900L;

    private static ApplicationContext context = SpringUtils
	    .getApplicationContext();

    public static boolean isUpperCaseOrNumber(String input) {
	return Pattern.matches("[A-Z\\d]+", input);
    }

    public static boolean isEnglishAlphabetOrNumber(String input) {
	return Pattern.matches("[a-zA-Z\\d]+", input);
    }

    public static boolean isEnglishAlphabet(String input) {
	return Pattern.matches("[a-zA-Z\\s]+", input);
    }

    public static boolean isEnglishAlphabetNoSpace(String input) {
	return Pattern.matches("[a-zA-Z]+", input);
    }

    public static boolean isNumber(String input) {
	return Pattern.matches("\\d+", input);
    }
    
    /**
     * 正整數
     * @param input
     * @return
     */
    public static boolean isPositiveInteger(String input) {
    	return Pattern.matches("^[0-9]*[1-9][0-9]*$", input);
    }
    /**
     * 負整數
     * @param input
     * @return
     */
    public static boolean isNegativeInteger(String input) {
    	return Pattern.matches("^-[0-9]*[1-9][0-9]*$", input);
    }
    /**
     * 不分正負的整數
     * @param input
     * @return
     */
    public static boolean isInteger(String input){
    	return Pattern.matches("^-?\\d+$", input);
    }
    
    /**
     * 是否含有小數點
     * @param input
     * @return
     */
    public static boolean isDecimal(Double unitPrice){
	
	if(unitPrice - unitPrice.intValue() != 0  ){
	    return true;
	}else{
	    return false;
	}
    }
    
    public static boolean isEmailFormat(String input) {
	return Pattern.matches(".+@.+\\.[a-z]+", input);
    }

    public static boolean isERPAccountPattern(String input) {
	return Pattern.matches("[a-zA-Z\\d_-]+@?[a-zA-Z\\d]*", input);
    }

    public static boolean isBIAccountPattern(String input) {
	return Pattern.matches("[a-zA-Z\\d_-]+", input);
    }

    public static boolean isAfterInitialYear(Long year) {
	if (year >= INITIAL_YEAR)
	    return true;
	else
	    return false;
    }

    public static String validateDate(Long year, Long month, Long Day) {
	String message = "Success";
	GregorianCalendar calendar = new GregorianCalendar();
	boolean isLeap = calendar.isLeapYear(year.intValue());
	if (month == 2L && !isLeap && Day > 28L)
	    message = year + "年的二月最後一天為28日";
	else if (month == 2L && isLeap && Day > 29L)
	    message = year + "年的二月最後一天為29日";
	else if ((month == 4L || month == 6L || month == 9L || month == 11L)
		&& Day > 30L)
	    message = month + "月的最後一天為30日";

	return message;
    }

    
    /**
     * 檢核是否小於關帳日或月
     * 
     * @param brandCode
     * @param orderTypeCode
     * @param dateType
     * @param date
     * @throws FormException
     * @throws Exception
     */
    public static void isAfterClose(String brandCode, String orderTypeCode, String dateType, Date date,String schedule) throws Exception {

    	try {
    		BuOrderTypeService orderTypeService = (BuOrderTypeService) context.getBean("buOrderTypeService");
    		BuOrderType orderType = orderTypeService.findById(new BuOrderTypeId(brandCode, orderTypeCode));
    		if (orderType != null) {
    			String closeType = orderType.getCloseType(); // 關帳類型(D:日關,M:月關)
    			if (!StringUtils.hasText(closeType)) {
    				throw new FormException("品牌代號：" + brandCode + "、單別：" + orderTypeCode + "的關帳類型為空值，請聯絡系統管理人員處理！");
    			} else if (!"D".equals(closeType) && !"M".equals(closeType)) {
    				throw new FormException("品牌代號：" + brandCode + "、單別：" + orderTypeCode + "的關帳類型設定錯誤，請聯絡系統管理人員處理！");
    			}
    			
    			BuBrandService brandService = (BuBrandService) context.getBean("buBrandService");
    			BuBrand brand = brandService.findById(brandCode);
    			String dailyCloseDate = brand.getDailyCloseDate(); // 日關日期
    			String nowSchedule = brand.getSchedule();
    			log.info("日關日期dailyCloseDate"+dailyCloseDate+" 現在班次="+nowSchedule);
    			String monthlyCloseMonth = brand.getMonthlyCloseMonth(); // 月關月份
    			String dateString = DateUtils.format(date, DateUtils.C_DATA_PATTON_YYYYMMDD);// YYYYMMDD
    			String yearAndMonth = dateString.substring(0, 6);// YYYYMM
    			String getOrderCondition = null==orderType.getOrderCondition()?"":orderType.getOrderCondition();
    			log.info("傳入日期dateString"+dateString+" 班次="+schedule);
    			if ("D".equals(closeType)) {
    				if(getOrderCondition.equals("PO")){
    					log.info("PO驗日期");
    					if (!StringUtils.hasText(dailyCloseDate)) {
        					throw new FormException("品牌代號：" + brandCode + "的日關帳日期為空值，請聯絡系統管理人員處理！");
        				} else if (dailyCloseDate.length() != 8) {
        					throw new FormException("品牌代號：" + brandCode + "的日關帳日期長度不足8碼，請聯絡系統管理人員處理！");
        				} else if (Integer.parseInt(dateString) <= Integer.parseInt(dailyCloseDate)) {
        					log.info("日關是否大於===?");
        					throw new Exception(dateType + "必須大於關帳日期：" + dailyCloseDate + "班別必須大於: "+nowSchedule +"！");
        				} 
    				}else{
    					if (!StringUtils.hasText(dailyCloseDate)) {
    						throw new FormException("品牌代號：" + brandCode + "的日關帳日期為空值，請聯絡系統管理人員處理！");
    					} else if (dailyCloseDate.length() != 8) {
    						throw new FormException("品牌代號：" + brandCode + "的日關帳日期長度不足8碼，請聯絡系統管理人員處理！");
    					} else if (Integer.parseInt(dateString) <= Integer.parseInt(dailyCloseDate)&&Integer.parseInt(schedule) <= Integer.parseInt(nowSchedule)) {
    						log.info("日關是否大於===?");
    						throw new Exception(dateType + "必須大於關帳日期：" + dailyCloseDate + "班別必須大於: "+nowSchedule +"！");
    					} 
    				}
    			} else {
    				if (!StringUtils.hasText(monthlyCloseMonth)) {
    					throw new FormException("品牌代號：" + brandCode + "的月關帳年月為空值，請聯絡系統管理人員處理！");
    				} else if (monthlyCloseMonth.length() != 6) {
    					throw new FormException("品牌代號：" + brandCode + "的月關帳年月長度不足6碼，請聯絡系統管理人員處理！");
    				} else if (Integer.parseInt(yearAndMonth) <= Integer.parseInt(monthlyCloseMonth)) {
    					throw new FormException(dateType + "必須大於關帳年月：" + monthlyCloseMonth + "！");
    				} 
    			}
    		} else {
    			throw new NoSuchObjectException("查無品牌代號：" + brandCode + "、單別：" + orderTypeCode + "的資料，請聯絡系統管理人員處理！");
    		}
    	} catch (FormException fe) {
    		fe.printStackTrace();
    		log.error("檢核是否小於關帳日或月時發生錯誤，原因：" + fe.toString());
    		throw new FormException(fe.getMessage());
    	} catch (Exception ex) {
    		log.error("檢核是否小於關帳日或月時發生錯誤，原因：" + ex.toString());
    		throw new Exception("檢核是否小於關帳日或月時發生錯誤，原因：" + ex.getMessage());
    	}
    }
}
