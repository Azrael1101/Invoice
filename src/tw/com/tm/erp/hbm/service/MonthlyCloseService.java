package tw.com.tm.erp.hbm.service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;

public class MonthlyCloseService {
    
    private static final Log log = LogFactory.getLog(MonthlyCloseService.class);
    
    private Properties config = new Properties();
    
    private final static String CONFIG_FILE = "/monthly_close.properties";
    
    private BuBrandService buBrandService;
    
    public void setBuBrandService(BuBrandService buBrandService) {
	this.buBrandService = buBrandService;
    }
    
    /**
     * 讀取月關帳作業設定檔
     * 
     * @throws Exception
     */
    public void loadConfig() throws Exception{
        try {
	    config.load(MonthlyCloseService.class.getResourceAsStream(CONFIG_FILE));
	} catch (IOException ex) {
	    throw new Exception("無法讀取月關帳作業設定檔！");
        }
    }
   
    /**
     * 執行月關帳
     * 
     * @param brandCode
     * @param year
     * @param month
     * @param opUser
     * @return String
     * @throws FormException
     * @throws Exception
     */
    public String performAction(String brandCode, String year, String month, String opUser) throws FormException, Exception{
	
	//執行月關帳時先將bu_brand中欲關帳的brand，其DAILY_BALANCING、MONTHLY_CLOSING設為Y
	String returnMessage = "";
        try{
            doValidate(year, month);       
            month =  CommonUtils.insertCharacterWithFixLength(month, 2, CommonUtils.ZERO);
            //執行採購單、進貨單、進貨退回、調撥單、調整單的日結
            loadConfig();
	    Enumeration enumeration = config.elements();
	    while(enumeration.hasMoreElements()){
		Class clsTask = Class.forName(enumeration.nextElement().toString());	
		Object objTask = clsTask.newInstance();
		Method mthdPerformBalance = clsTask.getMethod("performBalance", new Class[]{String.class, Date.class, String.class});
		Date currentDate = DateUtils.getShortDate(new Date());
		mthdPerformBalance.invoke(objTask, brandCode, currentDate, opUser);
	    }
	    //更新月關帳月份
            BuBrand brand= buBrandService.findById(brandCode);
            brand.setMonthlyCloseMonth(year + month);
            brand.setLastUpdatedBy(opUser);
            brand.setLastUpdateDate(new Date());
            buBrandService.updateBuBrand(brand);
            
            return returnMessage = "月關帳作業存檔成功！";
        }catch (FormException fe) {
	    log.error("執行月關帳作業失敗，原因：" + fe.toString());
	    returnMessage = "執行月關帳作業失敗，原因：" + fe.getMessage();
	    throw new FormException(returnMessage);
	}catch(Exception ex){
            log.error("執行月關帳作業時發生錯誤，原因：" + ex.toString());
            returnMessage = "執行月關帳作業時發生錯誤，原因：" + ex.getMessage();
            throw new Exception(returnMessage);
        }finally{
           //TODO:將工作紀錄到資料庫(returnMessage)、將bu_brand中欲關帳的brand，其DAILY_BALANCING、MONTHLY_CLOSING設為N
        }	
    }
    
    private void doValidate(String year, String month) throws FormException, Exception{
        /*
          1.關帳月份不可大於currentMonth
          2.檢核關帳月份前採購單、進貨單、進貨退回、調撥單、調整單的Status是否還有SIGNING；
        */
    }

}
