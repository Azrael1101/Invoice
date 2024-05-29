package tw.com.tm.erp.close;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.balance.DailyBalanceManager;
import tw.com.tm.erp.closevalidation.DailyCloseValidationManager;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.exportdb.CmTransactionExportData;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBatchConfig;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.CustomsContorl;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImTransation;
import tw.com.tm.erp.hbm.bean.ImMonthlyBalanceHead;
import tw.com.tm.erp.hbm.bean.ImMonthlyBalanceLine;
import tw.com.tm.erp.hbm.bean.ImDailyBalanceHead;
import tw.com.tm.erp.hbm.bean.ImDailyBalanceHeadId;
import tw.com.tm.erp.hbm.bean.ImDailyBalanceLine;
import tw.com.tm.erp.hbm.bean.ImDailyBalanceLineId;
import tw.com.tm.erp.hbm.bean.UploadControl;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.hbm.dao.CmMovementHeadDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.hbm.dao.ImTransationDAO;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.ImDailyBalanceDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.BuBatchConfigDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.UploadControlDAO;
import tw.com.tm.erp.hbm.dao.CustomsContorlDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;
import tw.com.tm.erp.utils.DateUtils;

public class DailyCloseMainManager {

	private static final Log log = LogFactory.getLog(DailyCloseMainManager.class);

	private Properties config = new Properties();

	private static final String CONFIG_FILE = "/daily_main_close.properties";

	public static final String PROGRAM_ID= "DAILY_CLOSE_MAIN_MANAGER";
	
	public static final String PROGRAM_ID_CM= "DAILY_CLOSE_MAIN_MANAGER_CM";

	private BuBrandService buBrandService;

	private DailyCloseValidationManager dailyCloseValidationManager;

	private SiProgramLogAction siProgramLogAction;
	
	private CmTransactionExportData cmTransactionExportData;

	private ImTransationDAO imTransationDAO;
	
	private BaseDAO baseDAO;
	
	private ImDailyBalanceDAO imDailyBalanceDAO;
	
	private ImDailyBalanceLineId imDailyBalanceLineId;
	
	private ImDailyBalanceHeadId imDailyBalanceHeadId;
	
	private CmMovementHeadDAO cmMovementHeadDAO;
	
	private ImMovementHeadDAO imMovementHeadDAO;
	
	private ImDeliveryHeadDAO imDeliveryHeadDAO;
	private ImAdjustmentHeadDAO imAdjustmentHeadDAO;
	private SoDeliveryHeadDAO soDeliveryHeadDAO;
	private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
	private BuBatchConfigDAO buBatchConfigDAO;
	private UploadControlDAO uploadControlDAO;
	private CustomsContorlDAO customsContorlDAO;
	/**
	 * 讀取日關帳作業設定檔
	 * 
	 * @throws Exception
	 */
	public void loadConfig() throws Exception {
		try {
			config.load(DailyCloseMainManager.class.getResourceAsStream(CONFIG_FILE));
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
	public List performActionMain(String brandCode, Date closeDate, String schedule,String opUser, String mode,String enforce){
		if(null==closeDate){
		Date ExecloseDate = new Date();
		closeDate = DateUtils.addDays(ExecloseDate, -1);
		}
		String returnMessage = null;
		String message = null;
		String performResult = MessageStatus.SUCCESS;
		String[] brandCodeArray = null;
		Date currentDate = new Date();
		String uuid = null;
		List errorMsgs = new ArrayList(0);
		String msg = null;
		String processName = brandCode + MessageStatus.DAILY_CLOSE;
		
		try {
			uuid = UUID.randomUUID().toString();
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "日關帳作業程序開始執行...", currentDate, uuid, opUser);
			// 執行日關帳檢核
			dailyCloseValidationManager = (DailyCloseValidationManager) SpringUtils.getApplicationContext().getBean("dailyCloseValidationManager");
			log.info("日關檢核開始--execAllProcess");
			log.info("傳入的參數:brandCode:"+brandCode+"/closeDate:"+closeDate+"/currentDate:"+ currentDate+"/errorMsgs:"+ errorMsgs+"/processName:"+ processName+"/uuid:"+ uuid+"/opUser:"+ opUser);
			boolean execAllProcess = dailyCloseValidationManager.performAction(brandCode, closeDate, currentDate, errorMsgs,processName, uuid, opUser);

			//執行未來關帳檢核
			if(!enforce.equals(""))
			{
				BuBatchConfig closeSchedule = (BuBatchConfig)buBatchConfigDAO.findById("BuBatchConfig", schedule);

				boolean isFutureClose = false;
				if(currentDate.before(closeDate))
				{

					isFutureClose=false;//不執行
				}
				else
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					log.info(sdf.format(currentDate).equals(sdf.format(closeDate)));
					log.info(currentDate.getHours()+"    "+Integer.parseInt(closeSchedule.getStartHours().substring(0, 2))+"   ");
					if(sdf.format(currentDate).equals(sdf.format(closeDate))&& currentDate.getHours()<=Integer.parseInt(closeSchedule.getEndHours().substring(0, 2)))
					{
;
						isFutureClose=false;//不執行
					}
					else
					{

						isFutureClose=true;
					}
				}
				if(enforce.equals("Y"))
				{
					log.info("強制送出"+"ENFORCE:"+enforce);
					isFutureClose=true;
				}
				if(isFutureClose==false)
				{
					log.info("未來班次不送出");
					execAllProcess = false;
					errorMsgs.add("您即將要執行日關的班次為未來班次，若仍要執行請勾選強制送出後送出");
				}
				log.info("日關檢核完"+execAllProcess+"...Size"+errorMsgs.size());
			}
		/*	if (errorMsgs.size() == 0) {
				//執行日結作業
				log.info("執行日結mode"+mode);
				if (mode.equalsIgnoreCase("Y")){
					log.info("mode===Y"+mode);
					log.info("execAllProcess"+execAllProcess);
					brandCodeArray = getDailyCloseBrandCode(brandCode);
					if(execAllProcess){
						log.info("執行日結execAllProcess"+execAllProcess);
						loadConfig();
						Enumeration enumeration = config.elements();
						log.info("執行日結enumeration"+enumeration);
						while (enumeration.hasMoreElements()){
							log.info("執行日結while");
							Class clsTask = Class.forName(enumeration.nextElement().toString());
							Object objTask = clsTask.newInstance();
							Method mthdPerformBalance = clsTask.getMethod("performBalance",
									new Class[] { String[].class, Date.class, List.class, String.class, String.class, String.class, Date.class, Date.class });
							log.info("執行日結mthdPerformBalance"+mthdPerformBalance);
							//日結作業(若有未執行至日關日期要補齊)
							//讀取每個品牌日結日期
//							for(int i = 0; i < brandCodeArray.length; i++){
//								BuBrand buBrand = buBrandService.findById(brandCodeArray[i]);
//								Date balanceDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD,buBrand.getDailyBalanceDate());
//								long  day = (closeDate.getTime()-balanceDate.getTime())/(24*60*60*1000); //天數差異(日結小於等於日關>0)
//								
//								Calendar calendar = Calendar.getInstance();    
//								calendar.setTime(balanceDate); //以最後一次日結為主   
//
//								while(day>0){
//									
//									calendar.add(Calendar.DAY_OF_MONTH,1);
//									balanceDate = calendar.getTime();
//									
//									returnMessage = (String)mthdPerformBalance.invoke(objTask, brandCodeArray, currentDate, errorMsgs,
//											processName, uuid, opUser, null, (balanceDate.compareTo(closeDate)<0?balanceDate:closeDate));
//									
//									day--;
//								}	
//							}
							
						}
					}					
				}*/
				if (errorMsgs.size() == 0) {
					brandCodeArray = getDailyCloseBrandCode(brandCode);
					log.info("bbbbbbbbbbb:"+mode+performResult);
					List<BuBatchConfig> buf = buBatchConfigDAO.findAll();
					String maxSchedule=Integer.toString(buf.size());

					log.info("執行日關作業--updateDailyCloseDate");
					updateDailyCloseDate(brandCode, closeDate,schedule,maxSchedule, opUser);    //日關帳記錄日期
					log.info("日關作業完畢--updateDailyCloseDate");
					//MACO 產生開關
					log.info("產生開關並將AEG.IBT.IRP單據押入班次");
					if(brandCode.equals("T2"))
					{
						updateCreateNewUploadControl(brandCode);

						updateOrderSchedule(brandCode,schedule,closeDate);

					}

					//if (mode.equalsIgnoreCase("Y")){
						log.info("CCCCCCCCCCCCCC:"+mode+performResult);
						//updateDailyBalanceDate(brandCode, closeDate, opUser);
						//}  //日結帳記錄日期
					
				}else{
					performResult = MessageStatus.FAIL;
				}
		/*	}else{
				performResult = MessageStatus.FAIL;
			}*/
			return errorMsgs;
		} catch (Exception ex) {
			msg = "執行日關帳作業失敗，原因：";
			log.error(msg + ex.toString());
			errorMsgs.add(ex.getMessage() + "<br>");    
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid, opUser);
			performResult = MessageStatus.FAIL;
			return errorMsgs;
		} finally {
			log.info("final000000000000000");
			// 將工作紀錄到資料庫
			if (MessageStatus.SUCCESS.equals(performResult)) {
				log.info("DAILY_CLOSE_SUCCESSDAILY_CLOSE_SUCCESSDAILY_CLOSE_SUCCESS");
				message = MessageStatus.DAILY_CLOSE_SUCCESS;
			} else if (MessageStatus.FAIL.equals(performResult)) {
				message = MessageStatus.DAILY_CLOSE_FAIL;
			}
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, message, currentDate, uuid, opUser);
			// 將執行日關帳的品牌其日結、日關帳標記設為N
			if(brandCodeArray != null){
				changeDailyCloseToIdle(brandCode);
			}
		}	
	}
//MACO 產生開關
	public void updateCreateNewUploadControl(String brandCode){
		try{
			log.info("產生開關");
			List<CustomsContorl> list = new ArrayList();
			BuBrand buBrand = buBrandService.findById(brandCode);
			Integer schedule = null; 
			Date dailyCloseDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, buBrand.getDailyCloseDate());
			String dailyCloseBatch = buBrand.getSchedule();
			BuBatchConfig buBatch = (BuBatchConfig)buBatchConfigDAO.findById("BuBatchConfig",buBrand.getSchedule());
			

			//String maxSchedule=Integer.toString(buf.size());
			Date closeDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, buBrand.getDailyCloseDate());
			if(buBatch.getBatchId().equals("99")){
				schedule = 1;
				closeDate = new Date(closeDate.getTime() + (1000 * 60 * 60 * 24));
			}else{
				schedule = Integer.parseInt(buBatch.getBatchId())+1;
				closeDate = new Date(closeDate.getTime());
			}
			BuBatchConfig buBatch1 = (BuBatchConfig)buBatchConfigDAO.findById("BuBatchConfig", Integer.toString(schedule));
			
			if(buBatch1!=null)
			{

				String[] fileName = {"schedual","schedualDate","orderTypeCode"};
				list = customsContorlDAO.findByProperty("CustomsContorl", "type", "NF");
				log.info("list.size()======="+list.size());

				
				
				if (list.size() > 0)
				{
					
					for (CustomsContorl cmh : list)
					{
						String orderTypeCode = cmh.getOrderTypeCode();
						log.info("SCHEDULE"+Integer.toString(schedule));
						Object[] fileValue = {Integer.toString(schedule),closeDate,orderTypeCode};
					    List<UploadControl> uploadControls = uploadControlDAO.findByProperty("UploadControl", fileName, fileValue);
					    if(uploadControls.size()==0)
					    {
					    	log.info("無資料..SAVE");
					    	UploadControl newUploadControl = new UploadControl();
					    	newUploadControl.setSchedualDate(closeDate);
					    	newUploadControl.setSchedual(Integer.toString(schedule));
					    	newUploadControl.setOrderTypeCode(orderTypeCode);
					    	newUploadControl.setStatusByAC("OFF");
					    	newUploadControl.setStatusByBC("OFF");
					    	newUploadControl.setStatusByED("OFF");
					    	uploadControlDAO.save(newUploadControl);
					    }
					    else
					    {
					    	log.info("已有資料..UPDATE");
					    	UploadControl uploadControl = uploadControls.get(0);
					    	uploadControl.setSchedualDate(closeDate);
					    	uploadControl.setSchedual(Integer.toString(schedule));
					    	uploadControl.setOrderTypeCode(orderTypeCode);
					    	uploadControl.setStatusByAC("OFF");
					    	uploadControl.setStatusByBC("OFF");
					    	uploadControl.setStatusByED("OFF");
					    	uploadControlDAO.update(uploadControl);
					    }
					}
				}
				else
				{
						
				}
			}
			else
			{
				schedule = 99;
			}
			
		    
		}
		catch(Exception ex)
		{
			log.info("建立開關時發生錯誤");
		}
	}
	public void updateOrderSchedule(String brandCode ,String schedule,Date closeDate){
		
		BuBrand brandPO = null;
		List bbc = null;
		try{
			brandPO = buBrandService.findById(brandCode);
			bbc = buBatchConfigDAO.findAll();
			BuBatchConfig scheduleA = (BuBatchConfig)bbc.get(Integer.parseInt(schedule)-1);
			log.info("班別起迄時間:"+scheduleA.getStartHours()+" "+scheduleA.getEndHours());
			//if(Long.parseLong(schedule)%Long.parseLong(((Integer)bbc.size()).toString())!=0){
				
				log.info("查詢時間內單據");
				
				//List<CmMovementHead> cmObjs = cmMovementHeadDAO.findScheduleOrders(schedule,closeDate,scheduleA.getStartHours(),scheduleA.getEndHours());	
				//List<ImMovementHead> imObjs = imMovementHeadDAO.findScheduleOrders(schedule,closeDate,scheduleA.getStartHours(),scheduleA.getEndHours());
				List<SoDeliveryHead> sodObjs = soDeliveryHeadDAO.findScheduleOrders(schedule,closeDate,scheduleA.getStartHours(),scheduleA.getEndHours());
				List<ImDeliveryHead> idObjs = imDeliveryHeadDAO.findScheduleOrders(schedule,closeDate,scheduleA.getStartHours(),scheduleA.getEndHours());
				List<ImAdjustmentHead> adjObjs = imAdjustmentHeadDAO.findScheduleOrders(schedule,closeDate,scheduleA.getStartHours(),scheduleA.getEndHours());
				                                 
				/*log.info("日關回傳Cm數量:"+cmObjs.size());
				for(CmMovementHead cms : cmObjs){
					  cms.setSchedule(schedule);
					  cmMovementHeadDAO.update(cms);
				}*/
				
				log.info("日關所有單據押入班次");
				
				for(SoDeliveryHead sods : sodObjs){
					  log.info("提貨單:"+sods.getHeadId()+" "+sods.getOrderNo());
					  sods.setSchedule(schedule);
					  soDeliveryHeadDAO.update(sods);
				}
				
				for(ImDeliveryHead ids : idObjs){
					log.info("退貨單:"+ids.getHeadId()+" "+ids.getOrderNo());
					  ids.setSchedule(schedule);
					  imDeliveryHeadDAO.update(ids);
				}
				
				
				for(ImAdjustmentHead adj : adjObjs){
					log.info("領用單:"+adj.getHeadId()+" "+adj.getOrderNo());
					  adj.setSchedule(schedule);
					  imAdjustmentHeadDAO.update(adj);
				}
				
				brandPO.setSchedule(schedule);
				buBrandService.updateBuBrand(brandPO);
				
			//}
		}catch(Exception ex){
				
		}
	    
	
	}
	
	public List performAction(String brandCode, Date closeDate, String schedule, String opUser,String enforce){
		log.info("進入日關作業:"+closeDate+brandCode+opUser+schedule);
		return performActionMain(brandCode,closeDate,schedule,opUser,"Y",enforce);
	}
	
	/**
	 * 執行日結帳作業(DailyBalanceHead、DailyBalanceLine)
	 * 
	 * @param brandCode
	 * @param closeDate
	 * @param opUser
	 * @return List
	 * @throws FormException
	 * @throws Exception
	 */	
	public void executeStatisticDailyBalance(String brandCode,Date closeDate,String opUser){
		
		String processMemo = ""; //記錄發生錯誤前的動作
		
		BalanceAndCloseUtils.createSystemLog(MessageStatus.DAILY_BALANCE, MessageStatus.LOG_INFO, "===========開始計算============", closeDate, "", opUser,brandCode,"executeStatisticDailyBalance",DateUtils.getFormatTime(new Date()));
		
		try
		{
			BuBrand buBrand = buBrandService.findById(brandCode);
			Date getDailyClose = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD,buBrand.getDailyCloseDate());
			Date getDailyBalance = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD,buBrand.getDailyBalanceDate());
			String [] transationType = {"PO","SO","MV","AJ"};
			int modifyResult = 0;
			
			BalanceAndCloseUtils.createSystemLog(MessageStatus.DAILY_BALANCE, "", "日結日期"+DateUtils.getShortDate(getDailyBalance).toString(), closeDate, "", opUser,brandCode,"executeStatisticDailyBalance",DateUtils.getFormatTime(new Date()));
			BalanceAndCloseUtils.createSystemLog(MessageStatus.DAILY_BALANCE, "", "日關日期"+closeDate.toString(), closeDate, "", opUser,brandCode,"executeStatisticDailyBalance",DateUtils.getFormatTime(new Date()));
			
			if (!DateUtils.getShortDate(getDailyClose).before(DateUtils.getShortDate(closeDate))) //系統日關在統計日關前(A>B)的相反-->系統日關不在統計日關前(A<=B)
			{
				//已經日關
				//檢核是否日結
				if (!DateUtils.getShortDate(getDailyBalance).before(DateUtils.getShortDate(closeDate))) //系統日結在統計日結前(A>B)的相反-->系統日結不在統計日結前(A<=B)
				{
					//已經日結；刪除(DailyBalanceHead、DailyBalanceLine)
					
					//刪除DailyBalanceHead
					System.out.println("刪除DailyBalanceHead.......");
					processMemo = "刪除DailyBalanceHead.......";
					modifyResult = imDailyBalanceDAO.deleteDailyBalance("ImDailyBalanceHead", brandCode, DateUtils.format(DateUtils.getShortDate(closeDate), DateUtils.C_DATA_PATTON_YYYYMMDD));
					if (modifyResult < 0){ throw new Exception("整批刪除 DailyBalanceHead 失敗！回傳訊息(modifyResult)："+modifyResult);}
					
					System.out.println("已經刪除單頭明細！");
					
					//刪除DailyBalanceLine
					System.out.println("刪除DailyBalanceLine.......");
					processMemo = "刪除DailyBalanceLine.......";
					modifyResult = imDailyBalanceDAO.deleteDailyBalance("ImDailyBalanceLine", brandCode, DateUtils.format(DateUtils.getShortDate(closeDate), DateUtils.C_DATA_PATTON_YYYYMMDD));
					if (modifyResult < 0){ throw new Exception("刪除 DailyBalanceLine 失敗！回傳訊息(modifyResult)："+modifyResult);}
					
					System.out.println("已經刪除單身明細！");
					
					processMemo = "計算上月日期";
					Calendar calendar = Calendar.getInstance();    
					calendar.setTime(closeDate);    
					calendar.set( Calendar.MONTH, calendar.get( Calendar.MONTH ) - 1 ); 
					
					processMemo = "取得上月年份";
					String year = DateUtils.format(calendar.getTime(),DateUtils.C_DATA_PATTON_YYYYMMDD).substring(0,4);  //上月年份  DateUtils.format(calendar.getTime(),DateUtils.C_DATA_PATTON_YYYYMMDD).substring(6,8)
					processMemo = "取得上月月份";
					String month = DateUtils.format(calendar.getTime(),DateUtils.C_DATA_PATTON_YYYYMMDD).substring(4,6); //上月月份
					processMemo = "取得昨日日期";
					String lastDate = DateUtils.getAppointDateCompareDate(closeDate,-1); //昨日
					
					System.out.println("year:"+year);
					System.out.println("month:"+month);
					
					//開始日結明細塞期初庫存(若為每月1號以上月月結期末庫存為主，反之則為前一日期末庫存為主)
					processMemo = "判別是否為每月1號(開始日結明細塞期初庫存)";
					if (DateUtils.format(closeDate,DateUtils.C_DATA_PATTON_YYYYMMDD).substring(6,8).equalsIgnoreCase("01")){
						//月結檔為主
						//單身
						processMemo = "寫入單身期初值(以上月月結)";
						modifyResult = imDailyBalanceDAO.insertBeginningOnHandQty("IM_MONTHLY_BALANCE_LINE", "IM_DAILY_BALANCE_LINE", 
								"BRAND_CODE,ITEM_CODE,'" + DateUtils.format(DateUtils.getShortDate(closeDate), DateUtils.C_DATA_PATTON_YYYYMMDD) + "',WAREHOUSE_CODE,LOT_NO,ENDING_ON_HAND_QUANTITY",
								"BRAND_CODE,ITEM_CODE,DAILY_DATE,WAREHOUSE_CODE,LOT_NO,BEGINNING_ON_HAND_QUANTITY",
								new String[] {"BRAND_CODE","YEAR","MONTH"} , 
								new String[] {brandCode,year,month},"");
						if (modifyResult < 0){ throw new Exception("整批寫入 DailyBalanceLine 期初庫存(月結)失敗！回傳訊息(modifyResult)："+modifyResult);}
						
						//單頭
						processMemo = "寫入單頭期初值(以上月月結)";
						modifyResult = imDailyBalanceDAO.insertBeginningOnHandQty("IM_MONTHLY_BALANCE_HEAD", "IM_DAILY_BALANCE_HEAD", 
								"BRAND_CODE,ITEM_CODE,'" + DateUtils.format(DateUtils.getShortDate(closeDate), DateUtils.C_DATA_PATTON_YYYYMMDD) + "',ENDING_ON_HAND_QUANTITY", 
								"BRAND_CODE,ITEM_CODE,DAILY_DATE,BEGINNING_ON_HAND_QUANTITY",
								new String[] {"BRAND_CODE","YEAR","MONTH"} , 
								new String[] {brandCode,year,month},"");
						if (modifyResult < 0){ throw new Exception("整批寫入 ImDailyBalanceHead 期初庫存(月結)失敗！回傳訊息(modifyResult)："+modifyResult);}
					}
					else{
						processMemo = "非每月1號(開始日結明細塞期初庫存)";
						//日結檔為主
						//單身
						processMemo = "寫入單身期初值(以昨日日結檔)";
						modifyResult = imDailyBalanceDAO.insertBeginningOnHandQty("IM_DAILY_BALANCE_LINE", "IM_DAILY_BALANCE_LINE", 
								"BRAND_CODE,'" + DateUtils.format(DateUtils.getShortDate(closeDate), DateUtils.C_DATA_PATTON_YYYYMMDD) + "',ITEM_CODE,WAREHOUSE_CODE,LOT_NO,ENDING_ON_HAND_QUANTITY", 
								"BRAND_CODE,DAILY_DATE,ITEM_CODE,WAREHOUSE_CODE,LOT_NO,BEGINNING_ON_HAND_QUANTITY",
								new String[] {"BRAND_CODE","DAILY_DATE"} , 
								new String[] {brandCode,lastDate},"");
						if (modifyResult < 0){ throw new Exception("整批寫入 DailyBalanceLine 期初庫存(日結)失敗！回傳訊息(modifyResult)："+modifyResult);}
						
						//單頭
						processMemo = "寫入單頭期初值(以昨日日結檔)";
						modifyResult = imDailyBalanceDAO.insertBeginningOnHandQty("ImDailyBalanceHead", "ImDailyBalanceHead", 
								"BRAND_CODE,'" + DateUtils.format(DateUtils.getShortDate(closeDate), DateUtils.C_DATA_PATTON_YYYYMMDD) + "',id.itemCode,endingOnHandQuantity", 
								"BRAND_CODE,DAILY_DATE,ITEM_CODE,BEGINNING_ON_HAND_QUANTITY",
								new String[] {"BRAND_CODE","DAILY_DATE"} , 
								new String[] {brandCode,lastDate},"");
						if (modifyResult < 0){ throw new Exception("整批寫入 DailyBalanceLine 期初庫存(日結)失敗！回傳訊息(modifyResult)："+modifyResult);}
						
					}
					
					System.out.println("統計單身........."); 
					processMemo = "統計單身.........";
					//開始統計-單身
					for(int y=0;y<transationType.length;y++){
						
						processMemo = "統計單身.........取得類型:"+transationType[y];
						List imTransations = imTransationDAO.findTransationByDate(brandCode,DateUtils.format(closeDate,DateUtils.C_DATA_PATTON_YYYYMMDD),transationType[y]);
						processMemo = "統計單身........."+transationType[y]+"單身筆數:"+imTransations.size();
						
						System.out.println("統計單身........."+transationType[y]+"單身筆數:"+imTransations.size());
						
						for(Iterator iterator = imTransations.iterator();iterator.hasNext();){  
							
							Long transationQty = 0L;
							
							Object[] objects = (Object[]) iterator.next();  
							transationQty = (Long)objects[5];
							
							ImDailyBalanceLineId id = new ImDailyBalanceLineId();
							id.setBrandCode(String.valueOf(objects[0]));
							id.setDailyDate(DateUtils.format(DateUtils.getShortDate(closeDate), DateUtils.C_DATA_PATTON_YYYYMMDD));
							id.setItemCode(String.valueOf(objects[2]));
							id.setWarehouseCode(String.valueOf(objects[3]));
							id.setLotNo(String.valueOf(objects[4]));
							
							ImDailyBalanceLine line = (ImDailyBalanceLine)baseDAO.findById("ImDailyBalanceLine",id);
							
							if (line==null){
								//沒有資料
								line = new ImDailyBalanceLine(); 
								line.setId(id);
								line.setCreatedBy(opUser);
								line.setCreationDate(new Date());
								line.setBeginningOnHandQuantity(0d);
							}
							
							if (transationType[y].equalsIgnoreCase("PO")){
								line.setDailyPurchaseQuantity((line.getDailyPurchaseQuantity()==null?0:line.getDailyPurchaseQuantity())+transationQty);}
							if (transationType[y].equalsIgnoreCase("SO")){
								line.setDailySalesQuantity((line.getDailySalesQuantity()==null?0:line.getDailySalesQuantity())-transationQty);}
							if (transationType[y].equalsIgnoreCase("MV")){
								line.setDailyMovementQuantity((line.getDailyMovementQuantity()==null?0:line.getDailyMovementQuantity())+transationQty);}
							if (transationType[y].equalsIgnoreCase("AJ")){
								line.setDailyAdjustmentQuantity((line.getDailyAdjustmentQuantity()==null?0:line.getDailyAdjustmentQuantity())+transationQty);}
							
							line.setEndingOnHandQuantity((line.getBeginningOnHandQuantity()==null?0:line.getBeginningOnHandQuantity())+(line.getDailyPurchaseQuantity()==null?0:line.getDailyPurchaseQuantity())-(line.getDailySalesQuantity()==null?0:line.getDailySalesQuantity())+
									(line.getDailyMovementQuantity()==null?0:line.getDailyMovementQuantity())+(line.getDailyAdjustmentQuantity()==null?0:line.getDailyAdjustmentQuantity()));
							line.setUpdatedBy(opUser);
							line.setUpdateDate(new Date());
							
							processMemo = "統計單身.........itemCode:"+line.getId().getItemCode();
							
							baseDAO.update(line);
						}
					}
					
					System.out.println("統計單頭........."); 
					processMemo = "統計單頭.........";
					//開始統計-單頭
					List<List> imDailyBalanceLines =  imDailyBalanceDAO.getSummaryDailyBalance("ImDailyBalanceLine",
							"sum(dailyPurchaseQuantity) as dailyPurchaseQuantity,sum(dailySalesQuantity) as dailySalesQuantity,sum(dailyMovementQuantity) as dailyMovementQuantity,sum(dailyAdjustmentQuantity) as dailyAdjustmentQuantity,sum(dailyOtherQuantity) as dailyOtherQuantity",
							new String[] {"id.brandCode","id.dailyDate"},
							new String[] {brandCode,DateUtils.format(DateUtils.getShortDate(closeDate), DateUtils.C_DATA_PATTON_YYYYMMDD)},
							"id.brandCode,id.dailyDate,id.itemCode");
					
					for(List imDailyBalanceLine : imDailyBalanceLines){
						
						ImDailyBalanceHeadId hId = new ImDailyBalanceHeadId();
						hId.setBrandCode(imDailyBalanceLine.get(0).toString());
						hId.setDailyDate(imDailyBalanceLine.get(1).toString());
						hId.setItemCode(imDailyBalanceLine.get(2).toString());
						
						ImDailyBalanceHead head = (ImDailyBalanceHead)baseDAO.findById("ImDailyBalanceHead",hId);
						
						if (head==null){
							head = new ImDailyBalanceHead();
							head.setCreatedBy(opUser);
							head.setCreationDate(new Date());
							head.setBeginningOnHandQuantity(0d);
						}
						
						head.setDailyPurchaseQuantity(head.getDailyPurchaseQuantity()+((Double)imDailyBalanceLine.get(3)));
						head.setDailySalesQuantity(head.getDailySalesQuantity()+((Double)imDailyBalanceLine.get(4)));
						head.setDailyMovementQuantity(head.getDailyMovementQuantity()+((Double)imDailyBalanceLine.get(5)));
						head.setDailyAdjustmentQuantity(head.getDailyAdjustmentQuantity()+((Double)imDailyBalanceLine.get(6)));
						head.setEndingOnHandQuantity(head.getBeginningOnHandQuantity()+head.getDailyPurchaseQuantity()-head.getDailySalesQuantity()+head.getDailyMovementQuantity()+head.getDailyAdjustmentQuantity());
						head.setUpdatedBy(opUser);
						head.setUpdateDate(new Date());
						
						processMemo = "統計單頭.........itemCode:"+head.getId().getItemCode();
						
						baseDAO.update(head);
					}		
											
				}
				else
				{
					System.out.println("無法統計！系統日結日期在統計日期前！");
					BalanceAndCloseUtils.createSystemLog(MessageStatus.DAILY_BALANCE, MessageStatus.LOG_INFO, "無法統計！系統日結日期在統計日期前！", closeDate, "", opUser,brandCode,"executeStatisticDailyBalance",DateUtils.getFormatTime(new Date()));
				}
			}
			else
			{
				System.out.println("無法統計！系統日關日期在統計日期前！");
				BalanceAndCloseUtils.createSystemLog(MessageStatus.DAILY_BALANCE, MessageStatus.LOG_INFO, "無法統計！系統日關日期在統計日期前！", closeDate, "", opUser,brandCode,"executeStatisticDailyBalance",DateUtils.getFormatTime(new Date()));
			}			
		}
		catch(Exception ex){
			System.out.println("日結時發生錯誤，原因："+ex.getMessage());
			BalanceAndCloseUtils.createSystemLog(MessageStatus.DAILY_BALANCE, MessageStatus.LOG_ERROR, "日結時發生錯誤。原因："+ex.getMessage()+"；當前動作："+processMemo, closeDate, "", opUser,brandCode,"executeStatisticDailyBalance",DateUtils.getFormatTime(new Date()));
		}
		finally
		{
			BalanceAndCloseUtils.createSystemLog(MessageStatus.DAILY_BALANCE, MessageStatus.LOG_INFO, "===========結束計算============", closeDate, "", opUser,brandCode,"executeStatisticDailyBalance",DateUtils.getFormatTime(new Date()));
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
	public List performCmAction(String brandCode, Date cmDate, String opUser){
		List errorMsgs = new ArrayList(0);
		String msg = null;
		try {
			// 執行海關日關帳檢核
			updateCmDate(brandCode, cmDate, opUser);
			
			Map parameterMap = new HashMap();
			parameterMap.put("OP_USER", opUser);
			
			parameterMap.put("CUSTOMER_WAREHOUSE_CODE", "FW");
			cmTransactionExportData.executeCmTransaction(parameterMap);
			
			parameterMap.put("CUSTOMER_WAREHOUSE_CODE", "FD");
			cmTransactionExportData.executeCmTransaction(parameterMap);
			
			return errorMsgs;
		} catch (Exception ex) {
			msg = "執行海關日關帳作業失敗，原因：";
			log.error(msg + ex.toString());
			errorMsgs.add(ex.getMessage() + "<br>");    
			return errorMsgs;
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
	private String[] getDailyCloseBrandCode(String brandCode) throws ValidationErrorException,
			Exception {

		String[] brandCodeArray = null;
		if (brandCode != null) {
			buBrandService = (BuBrandService) SpringUtils.getApplicationContext().getBean("buBrandService");
			log.info("BrandService"+buBrandService);
			//buBrandService = new BuBrandService();
			BuBrand brandPO = buBrandService.findById(brandCode);
			log.info("BrandService++++++++++++++");
			String dailyBalancing = brandPO.getDailyBalancing();
			String dailyClosing = brandPO.getDailyClosing();
			log.info("dailyBalancing"+dailyBalancing);
			log.info("dailyClosing"+dailyClosing);
			if ((dailyBalancing == null || "N".equals(dailyBalancing))
					&& (dailyClosing == null || "N".equals(dailyClosing))) {
				try {
					log.info("getDailyBalance");
					brandPO.setDailyBalancing("Y");
					brandPO.setDailyClosing("Y");
					buBrandService.updateBuBrand(brandPO);
					brandCodeArray = new String[] { brandCode };
					log.info("getBrandPo"+brandCodeArray);
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

	private void changeDailyCloseToIdle(String brandCode) {
		BuBrand brandPO = null;
		try {
			brandPO = buBrandService.findById(brandCode);
			brandPO.setDailyBalancing("N");
			brandPO.setDailyClosing("N");
			buBrandService.updateBuBrand(brandPO);
		} catch (Exception ex) {
			log.error("將品牌：" + brandPO.getBrandCode() + "的日結與日關帳標記更新為閒置狀態失敗！");
		}	
	}

	private void updateDailyCloseDate(String brandCode, Date closeDate,String schedule,String maxSchedule, String opUser) throws Exception {
		BuBrand brandPO = null;
		log.info("updateDailyCloseDate:"+"/品牌"+brandCode+"/日期"+closeDate+"/班次"+schedule+"/執行人"+opUser);
		try {
			
			List<BuBatchConfig> buf = buBatchConfigDAO.findAll();
			
			maxSchedule=Integer.toString(buf.size());
			brandPO = buBrandService.findById(brandCode);
			brandPO.setDailyCloseDate(DateUtils.format(closeDate, DateUtils.C_DATA_PATTON_YYYYMMDD));
			brandPO.setSchedule(schedule);
			brandPO.setLastUpdatedBy(opUser);
			brandPO.setLastUpdateDate(new Date());
			buBrandService.updateBuBrand(brandPO);
		} catch (Exception ex) {
			throw new Exception("更新品牌：" + brandPO.getBrandCode()+ "的日關帳日期失敗！");
		}
	}
	
	private void updateDailyBalanceDate(String brandCode, Date balanceDate, String opUser) throws Exception {
		BuBrand brandPO = null;
		log.info("DDDDDDDDDDDDDDDDDDD");
		try {
			brandPO = buBrandService.findById(brandCode);
			brandPO.setDailyBalanceDate(DateUtils.format(balanceDate, DateUtils.C_DATA_PATTON_YYYYMMDD));
			brandPO.setLastUpdatedBy(opUser);
			brandPO.setLastUpdateDate(new Date());
			buBrandService.updateBuBrand(brandPO);
		} catch (Exception ex) {
			throw new Exception("更新品牌：" + brandPO.getBrandCode()+ "的日結帳日期失敗！");
		}
	}

	private void updateCmDate(String brandCode, Date cmDate, String opUser) throws Exception {
		BuBrand brandPO = null;
		try {
			brandPO = buBrandService.findById(brandCode);
			brandPO.setCmDate(DateUtils.format(cmDate, DateUtils.C_DATA_PATTON_YYYYMMDD));
			brandPO.setLastUpdatedBy(opUser);
			brandPO.setLastUpdateDate(new Date());
			buBrandService.updateBuBrand(brandPO);
		} catch (Exception ex) {
			throw new Exception("更新品牌：" + brandPO.getBrandCode()+ "的報單日關帳日期失敗！");
		}
	}
	
	/**
	 * 初始化 bean 額外顯示欄位
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> executeInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			BuBrand buBrand = buBrandService.findById(brandCode);
			resultMap.put("brandCode", brandCode);
			resultMap.put("brandName", buBrand.getBrandName());
			resultMap.put("dailyCloseDate", DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, buBrand.getDailyCloseDate()));
			resultMap.put("dailyBalanceDate", DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, buBrand.getDailyBalanceDate()));
			return AjaxUtils.parseReturnDataToJSON(resultMap);
		}catch(Exception ex){
			log.error("日關帳初始化失敗，原因：" + ex.toString());
			throw new Exception("日關帳初始化失敗，原因：" + ex.getMessage());
		}
	}

	/**
	 * 初始化 bean 額外顯示欄位
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> executeCmInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			BuBrand buBrand = buBrandService.findById(brandCode);
			resultMap.put("brandCode", brandCode);
			resultMap.put("brandName", buBrand.getBrandName());
			resultMap.put("cmDate", DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, buBrand.getCmDate()));
			return AjaxUtils.parseReturnDataToJSON(resultMap);
		}catch(Exception ex){
			log.error("海關日關帳初始化失敗，原因：" + ex.toString());
			throw new Exception("海關日關帳初始化失敗，原因：" + ex.getMessage());
		}
	}
	
	public void test(Map parameterMap){
		log.info("test");
	}
	
	/**
	 * 送出
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> performTransaction(Map parameterMap){
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		String msg;
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String opUser = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			
			String enforce = (String)PropertyUtils.getProperty(formBindBean, "enforce");
			String userType = (String)PropertyUtils.getProperty(otherBean, "userType");
			log.info("userType"+userType);
			Date dailyCloseDate = DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH,(String)PropertyUtils.getProperty(formBindBean, "dailyCloseDate"));
			//String transCmAudit = (String)PropertyUtils.getProperty(formBindBean, "transCmAudit");
			String closeDate = DateUtils.format(dailyCloseDate, DateUtils.C_DATE_PATTON_DEFAULT);
			Map map = new HashMap();
			map.put("OP_USER", "SYSTEM");
			map.put("CUSTOMER_WAREHOUSE_CODE", "FD");//FD,FW
			map.put("START_DATE", closeDate);  // 2010-11-12
			map.put("END_DATE", closeDate);// 啟用日期~結束日期 會經過函數DateUtils.getDaysBetweenList取得轉檔日期出來
			map.put("ORDER_TYPE_CODE","IOP");
			siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, brandCode);
			//執行日關
			if(userType.equals("CLOSE") || userType == ""){
				String schedule = (String) PropertyUtils.getProperty(formBindBean, "schedule");
				log.info("班別:"+schedule);
				//Date dailyCloseDate = DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH,(String)PropertyUtils.getProperty(formBindBean, "dailyCloseDate"));
				List errorMsgs = this.performAction(brandCode, dailyCloseDate,schedule, opUser,enforce);
				if(errorMsgs.size() == 0)
					msgBox.setMessage(MessageStatus.DAILY_CLOSE_SUCCESS);
				else{
					for (Iterator iterator = errorMsgs.iterator(); iterator.hasNext();) {
						Object object = (Object) iterator.next();
						siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, brandCode, object.toString().replaceAll("<br>", ""), "MIS");
					}
					throw new FormException();
				}
			//log.info("DailyBalance"+transCmAudit);
			}
			//執行日結
			if(userType.equals("BALANCE")){
				log.info("日結開始");
				//Date dailyCloseDate = DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH,(String)PropertyUtils.getProperty(formBindBean, "dailyCloseDate"));
				Date dailyBalanceDate = DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH,(String)PropertyUtils.getProperty(formBindBean, "dailyBalanceDate"));
				BuBrand buBrand = buBrandService.findById(brandCode);
				Date getDailyClose = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD,buBrand.getDailyCloseDate());

				log.info("dailyBalanceDate"+getDailyClose);
				log.info("getDailyClose"+getDailyClose);
				
				if(dailyBalanceDate.after(getDailyClose)){
					msg="結帳日期大於關帳日，目前關帳日"+DateUtils.format(getDailyClose);
					msgBox.setMessage(msg);
					throw new Exception("結帳日期大於關帳日，目前關帳日"+DateUtils.format(getDailyClose));
				}else if(dailyCloseDate.equals(getDailyClose)){
					log.info("小於關帳日，進行日結");
					DailyBalanceManager dailyBalanceManager = null;
					dailyBalanceManager = (DailyBalanceManager) SpringUtils.getApplicationContext().getBean("dailyBalanceManager");
					msg = dailyBalanceManager.performAction1(brandCode, opUser, dailyBalanceDate, dailyBalanceDate);
					log.info("資料更新");
					updateDailyBalanceDate(brandCode, dailyBalanceDate, "SYSTEM");					
					CmTransactionExportData cmTransactionExportData =null;					
					cmTransactionExportData = (CmTransactionExportData) SpringUtils.getApplicationContext().getBean("cmTransactionExportData");
					cmTransactionExportData.executeCmTransaction(map);
					msgBox.setMessage(msg);
					log.info("日結完成 + 拋保稽");
				}
			}
			if(userType.equals("AUDIT")){
				String orderTypeCode = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");
				String customsWarehouseCode = (String)PropertyUtils.getProperty(formBindBean, "customsWarehouseCode");
				map.put("OP_USER", "SYSTEM");
				map.put("CUSTOMER_WAREHOUSE_CODE", customsWarehouseCode);//FD,FW
				map.put("START_DATE", closeDate);  // 2010-11-12
				map.put("END_DATE", closeDate);// 啟用日期~結束日期 會經過函數DateUtils.getDaysBetweenList取得轉檔日期出來
				map.put("ORDER_TYPE_CODE",orderTypeCode);
				//Date dailyBalanceDate = DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH,
				//		(String)PropertyUtils.getProperty(formBindBean, "dailyBalanceDate"));
				//Date dailyCloseDate = DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH,(String)PropertyUtils.getProperty(formBindBean, "dailyCloseDate"));
				String transCmAudit = (String)PropertyUtils.getProperty(formBindBean, "transCmAudit");
				//String closeDate = DateUtils.format(dailyCloseDate, DateUtils.C_DATE_PATTON_DEFAULT);
				log.info("*******重轉保稽資訊************");
				
				log.info("OP_USER:"+map.get("OP_USER"));
				log.info("CUSTOMER_WAREHOUSE_CODE:"+map.get("CUSTOMER_WAREHOUSE_CODE"));
				log.info("START_DATE:"+map.get("START_DATE"));
				log.info("END_DATE:"+map.get("END_DATE"));
				log.info("ORDER_TYPE_CODE:"+map.get("ORDER_TYPE_CODE"));
				
				log.info("****************************");
				CmTransactionExportData cmTransactionExportData =null;
				log.info("transCmAudit+++---"+transCmAudit+closeDate);
				cmTransactionExportData = (CmTransactionExportData) SpringUtils.getApplicationContext().getBean("cmTransactionExportData");
				if(transCmAudit.equals("Y")){
					cmTransactionExportData.executeCmTransaction(map);
					msgBox.setMessage(MessageStatus.AUDIT_UP_SUCCESS);
				}else{
					throw new Exception ("請選擇是否重轉保稽");
				}
			}
		}catch(FormException e){
			log.error("執行日關帳時失敗");
			msgBox.setMessage("執行日關帳時失敗");
			listErrorMessageBox(msgBox);
		}catch (Exception ex) {
			log.error("執行日關帳時失敗");
			msgBox.setMessage("執行日關帳時失敗，原因：" + ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	
	/**
	 * 送出(改版)
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> performTransactionMain(Map parameterMap){
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String opUser = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Date dailyCloseDate = DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH,
					(String)PropertyUtils.getProperty(formBindBean, "dailyCloseDate"));
			String mode = (String)PropertyUtils.getProperty(formBindBean, "mode");
			String schedule = (String)PropertyUtils.getProperty(formBindBean, "schedule");
			
			siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, brandCode);
			//執行日關
			List errorMsgs = this.performActionMain(brandCode, dailyCloseDate,schedule, opUser,mode,"");
			if(errorMsgs.size() == 0)
				msgBox.setMessage(MessageStatus.DAILY_CLOSE_SUCCESS);
			else{
				for (Iterator iterator = errorMsgs.iterator(); iterator.hasNext();) {
					Object object = (Object) iterator.next();
					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, brandCode, object.toString().replaceAll("<br>", ""), "MIS");
				}
				throw new FormException();
			}
		}catch(FormException e){
			log.error("執行日關帳時失敗");
			msgBox.setMessage("執行日關帳時失敗");
			listErrorMessageBox(msgBox);
		}catch (Exception ex) {
			log.error("執行日關帳時失敗");
			msgBox.setMessage("執行日關帳時失敗，原因：" + ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}	
	
	
	

	/**
	 * 送出
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> performCmTransaction(Map parameterMap){
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String opUser = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Date cmDate = DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH,
					(String)PropertyUtils.getProperty(formBindBean, "cmDate"));
			siProgramLogAction.deleteProgramLog(PROGRAM_ID_CM, null, brandCode);
			//執行日關
			List errorMsgs = this.performCmAction(brandCode, cmDate, opUser);
			if(errorMsgs.size() == 0)
				msgBox.setMessage(MessageStatus.DAILY_CLOSE_SUCCESS_CM);
			else{
				for (Iterator iterator = errorMsgs.iterator(); iterator.hasNext();) {
					Object object = (Object) iterator.next();
					siProgramLogAction.createProgramLog(PROGRAM_ID_CM, MessageStatus.LOG_ERROR, brandCode, object.toString().replaceAll("<br>", ""), "MIS");
				}
				throw new FormException();
			}
		}catch(FormException e){
			log.error(MessageStatus.DAILY_CLOSE_FAIL_CM);
			msgBox.setMessage(MessageStatus.DAILY_CLOSE_FAIL_CM);
			listErrorMessageBox(msgBox);
		}catch (Exception ex) {
			log.error(MessageStatus.DAILY_CLOSE_FAIL_CM);
			msgBox.setMessage(MessageStatus.DAILY_CLOSE_FAIL_CM + ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	/**
	 * 送出錯誤後，呼叫的function
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	private void listErrorMessageBox(MessageBox msgBox){
		//如果為背景送出
		Command cmd_error = new Command();
		cmd_error.setCmd(Command.FUNCTION);
		cmd_error.setParameters(new String[] { "showMessage()", "" });
		msgBox.setOk(cmd_error);
	}

	public String getIdentification(String brandCode) throws Exception{
		try{
			return brandCode;
		}catch(Exception ex){
			log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
		}	   
	}
	
	/**
	 * 日結自動計算
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	

	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}

	public void setDailyCloseValidationManager(	DailyCloseValidationManager dailyCloseValidationManager) {
		this.dailyCloseValidationManager = dailyCloseValidationManager;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setCmTransactionExportData(
			CmTransactionExportData cmTransactionExportData) {
		this.cmTransactionExportData = cmTransactionExportData;
	}

	public void setImTransationDAO(ImTransationDAO imTransationDAO){
		this.imTransationDAO = imTransationDAO;
	}
	
	public void setBaseDAO(BaseDAO baseDAO){
		this.baseDAO = baseDAO;
	}
	
	public void setImDailyBalanceDAO(ImDailyBalanceDAO imDailyBalanceDAO){
		this.imDailyBalanceDAO = imDailyBalanceDAO;
	}
	
	public void setImDailyBalanceLineId(ImDailyBalanceLineId imDailyBalanceLineId){
		this.imDailyBalanceLineId = imDailyBalanceLineId;
	}
	
	public void setImDailyBalanceHeadId(ImDailyBalanceHeadId imDailyBalanceHeadId){
		this.imDailyBalanceHeadId = imDailyBalanceHeadId;
	}
	
	public void setCmMovementHeadDAO(CmMovementHeadDAO cmMovementHeadDAO) {
    	this.cmMovementHeadDAO = cmMovementHeadDAO;
    }
	
	public void setImMovementHeadDAO(ImMovementHeadDAO imMovementHeadDAO) {
    	this.imMovementHeadDAO = imMovementHeadDAO;
    }
	
	public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
    	this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
    }
	
	public void setSoDeliveryHeadDAO(SoDeliveryHeadDAO soDeliveryHeadDAO) {
    	this.soDeliveryHeadDAO = soDeliveryHeadDAO;
    }
	
	public void setImDeliveryHeadDAO(ImDeliveryHeadDAO imDeliveryHeadDAO) {
    	this.imDeliveryHeadDAO = imDeliveryHeadDAO;
    }
	
	public void setImAdjustmentHeadDAO(ImAdjustmentHeadDAO imAdjustmentHeadDAO) {
		this.imAdjustmentHeadDAO = imAdjustmentHeadDAO;
	}
	
	public void setBuBatchConfigDAO(BuBatchConfigDAO buBatchConfigDAO) {
    	this.buBatchConfigDAO = buBatchConfigDAO;
    }
	public void setUploadControlDAO(UploadControlDAO uploadControlDAO) {
    	this.uploadControlDAO = uploadControlDAO;
    }
	public void setCustomsContorlDAO(CustomsContorlDAO customsContorlDAO) {
    	this.customsContorlDAO = customsContorlDAO;
    }
	
}