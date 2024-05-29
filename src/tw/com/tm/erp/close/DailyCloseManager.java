package tw.com.tm.erp.close;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.closevalidation.DailyCloseValidationManager;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuBatchConfig;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.dao.BuBatchConfigDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryHeadDAO;
public class DailyCloseManager {

	private BuCommonPhraseService buCommonPhraseService;
    private BuBatchConfigDAO buBatchConfigDAO = new BuBatchConfigDAO();
    public void setBuBatchConfigDAO(BuBatchConfigDAO buBatchConfigDAO) {
        this.buBatchConfigDAO = buBatchConfigDAO;
    } 
    public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
        this.buCommonPhraseService = buCommonPhraseService;
    } 
    private static final Log log = LogFactory.getLog(DailyCloseManager.class);

    private Properties config = new Properties();

    private static final String CONFIG_FILE = "/daily_close.properties";

    /**
     * 讀取日關帳作業設定檔
     * 
     * @throws Exception
     */
    public void loadConfig() throws Exception {
	try {
	    config.load(DailyCloseManager.class
		    .getResourceAsStream(CONFIG_FILE));
	} catch (IOException ex) {
	    throw new Exception("讀取日關帳作業設定檔失敗！");
	}
    }

    
    /**
     * 執行日關帳作業
     * 
     * @param brandCode
     * @param closeDate
     * @param opUser
     * @return List
     * @throws FormException
     * @throws Exception
     */
    public List performAction(String brandCode, Date closeDate, String opUser){
    	log.info("performAction");
    	String returnMessage = null;
		String message = null;
		String performResult = MessageStatus.SUCCESS;
		BuBrandService brandService = null;
		String[] brandCodeArray = null;
		Date currentDate = new Date();
		String uuid = null;
		List errorMsgs = new ArrayList(0);
		String msg = null;
		String processName = brandCode + MessageStatus.DAILY_CLOSE;
		try {
			log.info("日關");
			brandService = (BuBrandService) SpringUtils.getApplicationContext().getBean("buBrandService");
			uuid = UUID.randomUUID().toString();
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "日關帳作業程序開始執行...", currentDate, uuid, opUser);
			// 執行日關帳檢核
			DailyCloseValidationManager dailyCloseValidationManager = (DailyCloseValidationManager)
				SpringUtils.getApplicationContext().getBean("dailyCloseValidationManager");
			boolean execAllProcess = dailyCloseValidationManager.performAction(brandCode, closeDate, currentDate, errorMsgs,
					processName, uuid, opUser);
			if (errorMsgs.size() == 0) {	
				brandCodeArray = getDailyCloseBrandCode(brandCode, brandService);
				if(execAllProcess){
					loadConfig();
					Enumeration enumeration = config.elements();
					while (enumeration.hasMoreElements()){
						Class clsTask = Class.forName(enumeration.nextElement().toString());
						Object objTask = clsTask.newInstance();
						Method mthdPerformBalance = clsTask.getMethod("performBalance",
								new Class[] { String[].class, Date.class, List.class, String.class, String.class, String.class, Date.class, Date.class });
						returnMessage = (String)mthdPerformBalance.invoke(objTask, brandCodeArray, currentDate, errorMsgs, 
								processName, uuid, opUser, null, null);
					}
				}
				if (errorMsgs.size() == 0) {
					updateDailyCloseDate(brandCode, closeDate, brandService);
				}else{
					performResult = MessageStatus.FAIL;
				}
			}else{
				performResult = MessageStatus.FAIL;
			}
			return errorMsgs;
		} catch (Exception ex) {
			msg = "執行日關帳作業失敗，原因：";
			log.error(msg + ex.toString());
			errorMsgs.add(ex.getMessage() + "<br>");    
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid, opUser);
			performResult = MessageStatus.FAIL;
			return errorMsgs;
		} finally {
			// 將工作紀錄到資料庫
			if (MessageStatus.SUCCESS.equals(performResult)) {
				message = MessageStatus.DAILY_CLOSE_SUCCESS;
			} else if (MessageStatus.FAIL.equals(performResult)) {
				message = MessageStatus.DAILY_CLOSE_FAIL;
			}
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, message, currentDate, uuid, opUser);
			// 將執行日關帳的品牌其日結、日關帳標記設為N
			if(brandCodeArray != null){
				changeDailyCloseToIdle(brandCode, brandService);
			}
		}
    }

    /**
     * 取得欲執行日關帳的品牌並將其日結、日關帳標記設為Y
     * 
     * @param brandCode
     * @param brandService
     * @return String[]
     * @throws ValidationErrorException
     * @throws Exception
     */
    private String[] getDailyCloseBrandCode(String brandCode,
	    BuBrandService brandService) throws ValidationErrorException,
	    Exception {

	String[] brandCodeArray = null;
	if (brandCode != null) {
	    BuBrand brandPO = brandService.findById(brandCode);
	    String dailyBalancing = brandPO.getDailyBalancing();
	    String dailyClosing = brandPO.getDailyClosing();
	    if ((dailyBalancing == null || "N".equals(dailyBalancing))
		    && (dailyClosing == null || "N".equals(dailyClosing))) {
		try {
		    brandPO.setDailyBalancing("Y");
		    brandPO.setDailyClosing("Y");
		    brandService.updateBuBrand(brandPO);
		    brandCodeArray = new String[] { brandCode };
		    return brandCodeArray;
		} catch (Exception ex) {
		    log.error("品牌：" + brandPO.getBrandCode()
			    + "的日結與日關帳標記更新為執行狀態時發生錯誤，原因：" + ex.toString());
		    throw new ValidationErrorException("品牌："
			    + brandPO.getBrandCode() + "的日結與日關帳標記更新為執行狀態時發生錯誤！");
		}
	    } else {
		throw new ValidationErrorException("品牌："
			+ brandPO.getBrandCode() + "的日結或日關帳標記為執行狀態，無法執行關帳作業！");
	    }
	} else {
	    throw new ValidationErrorException("參數barndCode為空值，請聯絡系統管理人員處理！");
	}
    }

    private void changeDailyCloseToIdle(String brandCode,
	    BuBrandService brandService) {

        BuBrand brandPO = null;
	try {
	    brandPO = brandService.findById(brandCode);
	    brandPO.setDailyBalancing("N");
            brandPO.setDailyClosing("N");
            brandService.updateBuBrand(brandPO);
	} catch (Exception ex) {
	    log.error("將品牌：" + brandPO.getBrandCode()
	        + "的日結與日關帳標記更新為閒置狀態失敗！");
        }	
    }

    private void updateDailyCloseDate(String brandCode, Date closeDate,
	    BuBrandService brandService) throws Exception {
	
	BuBrand brandPO = null;
        try {
	    brandPO = brandService.findById(brandCode);
            brandPO.setDailyCloseDate(DateUtils.format(closeDate,
			    DateUtils.C_DATA_PATTON_YYYYMMDD));
	    brandService.updateBuBrand(brandPO);
	} catch (Exception ex) {
	    throw new Exception("更新品牌：" + brandPO.getBrandCode()
	        + "的日關帳日期失敗！");
	}
    }
    private BuBrandService buBrandService;
    public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}
	/**
	 * 初始化 bean 額外顯示欄位
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> executeInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		Map multiList = new HashMap(0);
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			BuBrand buBrand = buBrandService.findById(brandCode);
			Integer schedule = null; 
			String userType = (String)PropertyUtils.getProperty(otherBean, "userType");
			if(userType.equals("BALANCE")){
				Date date = new Date();
				resultMap.put("dailyBalanceDate", DateUtils.format(date));
			}
			
			BuBatchConfig buBatch = (BuBatchConfig)buBatchConfigDAO.findById("BuBatchConfig",buBrand.getSchedule());

			//String maxSchedule=Integer.toString(buf.size());
			Date closeDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, buBrand.getDailyCloseDate());
			if(brandCode.equals("T2"))
			{
				if(buBatch.getBatchId().equals("99")){
					schedule = 1;
					closeDate = new Date(closeDate.getTime() + (1000 * 60 * 60 * 24));
				}else{
					schedule = Integer.parseInt(buBatch.getBatchId())+1;
					BuBatchConfig buBatch1 = (BuBatchConfig)buBatchConfigDAO.findById("BuBatchConfig", Integer.toString(schedule));
					if(buBatch1!=null)
					{
						schedule = Integer.parseInt(buBatch1.getBatchId());
					}
					else
					{
						schedule = 99;
					}
				}
			}
			else
			{
				schedule = 100;
			}
			
			//下拉式選單(FOR保稽) MACO
			List<BuCommonPhraseLine> allCmAuditCustomsWarehouse = buCommonPhraseService.getBuCommonPhraseLines("CmAuditCustomsWarehouse");
			List<BuCommonPhraseLine> allCmAuditOrderTypeCode = buCommonPhraseService.getBuCommonPhraseLines("CmAuditOrderTypeCode");
        	multiList.put("allCmAuditCustomsWarehouse" ,     AjaxUtils.produceSelectorData( allCmAuditCustomsWarehouse, "lineCode", "name", true, true));
        	multiList.put("allCmAuditOrderTypeCode" ,     AjaxUtils.produceSelectorData( allCmAuditOrderTypeCode, "lineCode", "name", true, true));

			
        	resultMap.put("multiList",multiList);
			resultMap.put("brandCode", brandCode);
			resultMap.put("brandName", buBrand.getBrandName());
			resultMap.put("schedule", schedule.toString());
			resultMap.put("dailyCloseDate", closeDate);
			resultMap.put("dailyBalanceDate", DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, buBrand.getDailyBalanceDate()));
			return AjaxUtils.parseReturnDataToJSON(resultMap);
		}catch(Exception ex){
			log.error("日關帳初始化失敗，原因：" + ex.toString());
			throw new Exception("日關帳初始化失敗，原因：" + ex.getMessage());
		}
	}

}
