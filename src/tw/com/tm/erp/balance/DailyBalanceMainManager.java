package tw.com.tm.erp.balance;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;
import tw.com.tm.erp.utils.StringTools;

public class DailyBalanceMainManager {

    private static final Log log = LogFactory.getLog(DailyBalanceMainManager.class);

    private Properties config = new Properties();

    private static final String CONFIG_FILE = "/daily_balance.properties";
    
    private static final String ORGANIZATION_CODE = "TM";

    /**
     * 讀取日結作業設定檔
     * 
     * @throws Exception
     */
    public void loadConfig() throws Exception {
	try {
	    config.load(DailyBalanceMainManager.class
		    .getResourceAsStream(CONFIG_FILE));
	} catch (IOException ex) {
	    throw new Exception("讀取日結作業設定檔失敗！");
	}
    }

    /**
     * 執行日結作業
     * 
     * @param brandCode
     * @param opUser
     */
    public void performAction(String brandCode, String opUser, Date startDate, Date endDate) {

	String returnMessage = null;
	String message = null;
	String performResult = MessageStatus.SUCCESS;
	BuBrandService brandService = null;
	String[] brandCodeArray = null;
	Date currentDate = new Date();
	String uuid = null;
	List errorMsgs = new ArrayList(0);
	String msg = null;
	try {
	    uuid = UUID.randomUUID().toString();
	    BalanceAndCloseUtils.createSystemLog(MessageStatus.DAILY_BALANCE, MessageStatus.LOG_INFO, "日結作業程序開始執行...", currentDate, uuid, opUser);
	    brandService = (BuBrandService) SpringUtils.getApplicationContext().getBean("buBrandService");    
	    brandCodeArray = getDailyBalanceBrandCode(brandCode, ORGANIZATION_CODE, brandService, errorMsgs, MessageStatus.DAILY_BALANCE, currentDate, uuid, opUser);
	    System.out.println("品牌:"+brandCode+brandCodeArray);
	    //System.out.println( "brandArray:"+brandCodeArray.length);
	    if(brandCodeArray != null && brandCodeArray.length > 0){
	    	 System.out.println("PART1");
	        loadConfig();
	        Enumeration enumeration = config.elements();	      
	        while (enumeration.hasMoreElements()){
	        	System.out.println("PART2");
	        	//System.out.println("enumeration:"+enumeration.nextElement().toString());
	        	Class clsTask = Class.forName(enumeration.nextElement().toString());
	        	Object objTask = clsTask.newInstance();
	        	Method mthdPerformBalance = clsTask.getMethod("performBalance",
	        			new Class[] { String[].class, Date.class, List.class, String.class, String.class, String.class,Date.class, Date.class });
	        	returnMessage = (String)mthdPerformBalance.invoke(objTask, brandCodeArray, currentDate, errorMsgs,
		        MessageStatus.DAILY_BALANCE, uuid, opUser, startDate, endDate);
	        }
	        if (errorMsgs.size() > 0) {
	            performResult = MessageStatus.ERROR;
		}
	    }else{
		performResult = MessageStatus.NONE;
	    }
	} catch (Exception ex) {
	    msg = "執行日結作業時發生錯誤，原因：";
	    log.error(msg + ex.toString());
	    errorMsgs.add(msg + ex.getMessage() + "<br>");    
	    BalanceAndCloseUtils.createSystemLog(MessageStatus.DAILY_BALANCE, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid, opUser);
	    performResult = MessageStatus.FAIL;
	} finally {
	    //將工作紀錄到資料庫
	    if(MessageStatus.SUCCESS.equals(performResult)){
		message = MessageStatus.DAILY_BALANCE_SUCCESS;
	    }else if(MessageStatus.FAIL.equals(performResult)){
		message = MessageStatus.DAILY_BALANCE_FAIL;
	    }else if(MessageStatus.NONE.equals(performResult)){
		message = MessageStatus.DAILY_BALANCE_NONE;
	    }else{
		message = MessageStatus.DAILY_BALANCE_PARTIAL_FAIL;
	    }
	    
	    BalanceAndCloseUtils.createSystemLog(MessageStatus.DAILY_BALANCE, MessageStatus.LOG_INFO, message, currentDate, uuid, opUser);
	    //將執行日結的品牌其日結標記設為N
	    changeDailyBalancingToIdle(brandCodeArray, brandService);    
	}
    }

    /**
     * 取得欲執行日結的品牌並將其日結標記設為Y
     * 
     * @param brandCode
     * @param organizationCode
     * @param brandService
     * @return String[]
     * @throws Exception
     */
    private String[] getDailyBalanceBrandCode(String brandCode,
	    String organizationCode, BuBrandService brandService, List errorMsgs,
	    String processName, Date currentDate, String processLotNo, String opUser)
	    throws Exception {

	String[] brandCodeArray = null;
	String msg = null;
	if (brandCode != null) {
	    if ("ALL".equals(brandCode)) {
	    	String dailyBalanceBranch = SystemConfig
			.getSystemConfigPro("Daily_Balance_Branch");
		if (dailyBalanceBranch == null) {
		    throw new ValidationErrorException("無法取得日結事業體資訊！");
		} else {
		    String[] dailyBalanceBranchArray = StringTools.StringToken(
			    dailyBalanceBranch, "{$}");
		    if (dailyBalanceBranchArray == null) {
			throw new ValidationErrorException("無法拆解日結事業體資訊！");
		    } else if (dailyBalanceBranchArray.length <= 0) {
			throw new ValidationErrorException("日結事業體資訊為空值！");
		    } else {
			List brandsInfo = brandService.findByBranchCodes(
				dailyBalanceBranchArray, organizationCode);
			if (brandsInfo != null && brandsInfo.size() > 0) {
			    List actualExecuteBrands = new ArrayList();
			    for (int i = 0; i < brandsInfo.size(); i++) {
				BuBrand brandPO = null;
				try {		   
				    brandPO = (BuBrand) brandsInfo.get(i);
				    String dailyBalancing = brandPO.getDailyBalancing();
				    if(dailyBalancing == null || "N".equals(dailyBalancing)){				    
				       brandPO.setDailyBalancing("Y");
				       brandService.updateBuBrand(brandPO);
				       actualExecuteBrands.add(brandPO.getBrandCode());
				    }else{
				       msg = "品牌：" + brandPO.getBrandCode() + "的日結標記為執行狀態，無法執行結算作業！";
				       errorMsgs.add(msg + "<br>");
				       BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg, currentDate, processLotNo, opUser);
				    }
				} catch (Exception ex) {
				    msg = "品牌：" + brandPO.getBrandCode() + "的日結標記更新為執行狀態時發生錯誤，原因：";
				    log.error(msg + ex.toString());
				    errorMsgs.add(msg + ex.getMessage() + "<br>");
				    BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, processLotNo, opUser);
				}
			    }
			    
			    return brandCodeArray = (String[])actualExecuteBrands.toArray(new String[actualExecuteBrands.size()]);
			} else {
			    throw new NoSuchDataException("依據日結事業體代碼查無品牌資訊！");
			}
		    }
		}
	    } else {
		BuBrand brandPO = brandService.findById(brandCode);
		String dailyBalancing = brandPO.getDailyBalancing();
		try{
		    if(dailyBalancing == null || "N".equals(dailyBalancing)){	
		        brandPO.setDailyBalancing("Y");
		        brandService.updateBuBrand(brandPO);
		        brandCodeArray = new String[]{brandCode};
		    }else{
			msg = "品牌：" + brandPO.getBrandCode() + "的日結標記為執行狀態，無法執行結算作業！";
			errorMsgs.add(msg + "<br>");
		        BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg, currentDate, processLotNo, opUser);
		    }
		}catch(Exception ex){
		    msg = "品牌：" + brandPO.getBrandCode() + "的日結標記更新為執行狀態時發生錯誤，原因：";
	            log.error(msg + ex.toString());
	            errorMsgs.add(msg + ex.getMessage() + "<br>");
	            BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, processLotNo, opUser);		
		}
		
		return brandCodeArray;
	    }
	} else {
	    throw new ValidationErrorException("參數barndCode設定錯誤！");
	}
    }
    
    private void changeDailyBalancingToIdle(String[] brandCodeArray, BuBrandService brandService) {
	   
        if (brandCodeArray != null && brandCodeArray.length > 0) {
	    for (int j = 0; j < brandCodeArray.length; j++) {
	        BuBrand brandPO = null;
		try {
	            brandPO = brandService.findById(brandCodeArray[j]);
		    brandPO.setDailyBalancing("N");
		    brandService.updateBuBrand(brandPO);
		} catch (Exception ex) {
		    log.error("將品牌：" + brandPO.getBrandCode()
				+ "的日結標記更新為閒置狀態失敗！");
		}
	    }
	}
    }
}
