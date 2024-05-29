package tw.com.tm.erp.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.PosEmployee;
import tw.com.tm.erp.hbm.bean.SiResend;
import tw.com.tm.erp.hbm.bean.SiResendId;
import tw.com.tm.erp.hbm.bean.SiSystemLog;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderPayment;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.PosExportDAO;
import tw.com.tm.erp.hbm.dao.SiResendDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderPaymentDAO;
import tw.com.tm.erp.hbm.service.ImStorageService;
import tw.com.tm.erp.hbm.service.PosImportDataService;
import tw.com.tm.erp.hbm.service.SoSalesOrderMainService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.FtpUtils;
import tw.com.tm.erp.utils.MailUtils;
 

public class PosImportDataAction {

    private static final Log log = LogFactory.getLog(PosImportDataAction.class);

    private String programId = "PosDU";

    private PosImportDataService posImportDataService;
    private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
    private SoSalesOrderPaymentDAO soSalesOrderPaymentDAO;
    /**
	 * @param soSalesOrderHeadDAO the soSalesOrderHeadDAO to set
	 */
	public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
		this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
	}

	public void setSoSalesOrderPaymentDAO(SoSalesOrderPaymentDAO soSalesOrderPaymentDAO) {
		this.soSalesOrderPaymentDAO = soSalesOrderPaymentDAO;
	}
	
	public void setPosImportDataService(PosImportDataService posImportDataService) {
	this.posImportDataService = posImportDataService;
    }

    private SoDeliveryHeadDAO soDeliveryHeadDAO;

    public void setSoDeliveryHeadDAO(SoDeliveryHeadDAO soDeliveryHeadDAO) {
	this.soDeliveryHeadDAO = soDeliveryHeadDAO;
    }
    
    private SiResendDAO siResendDAO;
    
    public void setSiResendDAO(SiResendDAO siResendDAO) {
    	this.siResendDAO = siResendDAO;
    }
//brian 20200612=======    
    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    
    public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO){
    	this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    }
    private ImPromotionDAO imPromotionDAO;
    
    public void setImPromotionDAO(ImPromotionDAO imPromotionDAO){
    	this.imPromotionDAO = imPromotionDAO;
    }
    private SoSalesOrderMainService soSalesOrderMainService;
    
    public void setSoSalesOrderMainService(SoSalesOrderMainService soSalesOrderMainService) {
    	this.soSalesOrderMainService = soSalesOrderMainService;
    }
//======= 
    //for 儲位用
    private ImStorageAction imStorageAction;

    //for 儲位用
    public void setImStorageAction(ImStorageAction imStorageAction) {
	this.imStorageAction = imStorageAction;
    }

    // 整批檔案轉檔 轉入ERP系統
    public Long executeUploadTransfer (HashMap posCommandMap){
	log.info("POS executeTableTransfer requestId = " + posCommandMap.get("REQUEST_ID"));
	Long responseId = (Long)posCommandMap.get("responseId");
	String dataId = (String) posCommandMap.get("DATA_ID");
	String uuId = (String) posCommandMap.get("UU_ID");
	String ip = (String) posCommandMap.get("IP");
	String company = (String) posCommandMap.get("COMPANY");
	SiResend siResend = new SiResend();
	SiResendId siResendId = new SiResendId();
	String machineCode = (String)posCommandMap.get("MACHINE_CODE");
	
	String dataBase = (String)posCommandMap.get("DATA_BASE");
	String defaultWarehouseCode = (String)posCommandMap.get("");
	
	List returnBeans = new ArrayList();
	
	try{
	    dataId = (String) posCommandMap.get("DATA_ID");
	    uuId = (String) posCommandMap.get("UU_ID");
	    ip = (String) posCommandMap.get("IP");
	    company = (String) posCommandMap.get("COMPANY");
	    
	    if(dataBase.equals("MySql")||dataBase.equals("Oracle")){
	    	returnBeans = posImportDataService.crtSoHeadMySqlBean(posCommandMap);
	    }else{
	    	returnBeans = posImportDataService.crtSoHeadBean(posCommandMap);
	    }

	    //如果POS無上傳正確資料 直接給他過 再請商控補單 by Caspar2014.2.19  之前版本
	    //如果POS無上傳正確資料 回傳-1 請POS重傳 Mark2014.11.24
	    responseId = (Long)posCommandMap.get("responseId");
	    if(responseId <0 ){
	    	log.error(machineCode+"機台上傳 DataID = "+dataId+"查詢POS_SCHEMA有錯誤或無資料");
	    	MailUtils.sendMail("POS上傳KWE失敗", "POS上傳KWE失敗<br>"+machineCode+"機台上傳異常請檢查SALEDATA.MDB是否損毀", "ITMA-DG@tasameng.com.tw");
	    }else{
	    //log.info("POS重傳時刪除相關銷售、出貨資料，並將轉檔資料存檔  requestId = " + posCommandMap.get("REQUEST_ID"));
	    	
		posImportDataService.saveT2PosNoValidate(returnBeans, posCommandMap);
		
		//於配報單之前先建立入提單2018.6.13 Yao
		List<SoSalesOrderHead> soSalesOrderHeads = soSalesOrderHeadDAO.findOnlineSoBean(posCommandMap);
		soSalesOrderHeads = soSalesOrderHeadDAO.findOnlineSoBean(posCommandMap);
		if (soSalesOrderHeads != null && soSalesOrderHeads.size() > 0) {
			for (SoSalesOrderHead soSalesOrderHead : soSalesOrderHeads) {
				if(StringUtils.hasText(soSalesOrderHead.getLadingNo())) {
				    Map soDeliveryHeadMap = new HashMap();
				    soDeliveryHeadMap.put("salesOrderId", soSalesOrderHead.getHeadId());
				    soDeliveryHeadMap.put("employeeCode", soSalesOrderHead.getSuperintendentCode());
				    soDeliveryHeadMap.put("logType", "AUTO");
				    soDeliveryHeadDAO.updateDeliveryData(SoDeliveryHeadDAO.SP_TYPE_SALES_ORDER_ID, soDeliveryHeadMap);
				}
			}
		}
		
	      

		
		//log.info("取報單訊息  requestId = " + posCommandMap.get("REQUEST_ID"));
		List<SoSalesOrderHead> SoSalesOrderHeads = posImportDataService.updatePosData(posCommandMap);

		for (Iterator iterator = SoSalesOrderHeads.iterator(); iterator.hasNext();) {
		    SoSalesOrderHead soSalesOrderHead = (SoSalesOrderHead) iterator.next();
		    try{
		    //for 儲位用
//			if(imStorageAction.isStorageExecute(soSalesOrderHead)){
//			    //取單號後，扣庫存前，執行更新儲位單頭與單身，比對單據明細與儲位明細
//			    executeStorage(soSalesOrderHead);
//			}
		    
		    	
		    //一日遊宅配特定庫不扣庫存 Brian	========
		  String dateType = (String)posCommandMap.get("DATA_TYPE");
		    if(!dateType.equals("SVP")){     //單別SVP不扣庫存
		    	log.info("此單別為非SVP照正常流程");
		    	if(OrderStatus.SAVE.equals(soSalesOrderHead.getStatus()) || OrderStatus.UNCONFIRMED.equals(soSalesOrderHead.getStatus())){
				    if(soSalesOrderHead.getSoSalesOrderItems().size()<=20 ){
				    	log.info("扣庫存並將狀態改為SIGNING requestId = " + posCommandMap.get("REQUEST_ID"));
					    posImportDataService.updateSOToDelivery(soSalesOrderHead);
				    }else{
				    	log.info("soSalesOrderHead.getSoSalesOrderItems().size()="+soSalesOrderHead.getSoSalesOrderItems().size());
				    	log.info("明細筆數多餘10 直接變未確認!交由排程送");
				    	siResendId.setOrderTypeCode(soSalesOrderHead.getOrderTypeCode());
				    	siResendId.setOrderNo(soSalesOrderHead.getOrderNo());
				    	siResend.setId(siResendId);
				    	siResend.setHeadId(soSalesOrderHead.getHeadId());
				    	siResend.setIsLock("Y");
				    	siResend.setStatus("NOTOK"); 
				    	siResend.setCreationDate(new Date());
				    	siResend.setLastUpdateDate(new Date());
				    	siResendDAO.save(siResend);
				    }				
				}
		    }else{
		    	//若單別為SVP,作廢則狀態為作廢否則狀態設為SAVE
		    	if(OrderStatus.VOID.equals(soSalesOrderHead.getStatus())){
		    		log.info("此單別為SVP不扣庫存,作廢VOID");
		    		soSalesOrderHead.setStatus(OrderStatus.VOID);
		    		soSalesOrderHeadDAO.update(soSalesOrderHead);
		    	}else{
		    		log.info("此單別為SVP不扣庫存,預設為SAVE");
		    		soSalesOrderHead.setStatus(OrderStatus.SAVE);
					soSalesOrderHeadDAO.update(soSalesOrderHead);
		    	}
		    }
		    //===========
			/**///將資料匯出到FTP (17使用)2020.04.06 Justin
			if(StringUtils.hasText(soSalesOrderHead.getAppCustomerCode())){
				soSalesOrderMainService.appTransferToFTP(soSalesOrderHead);
			}
			 /**/
			
			//如果有入提單號
			if(StringUtils.hasText(soSalesOrderHead.getLadingNo())) {
			    Map soDeliveryHeadMap = new HashMap();
			    soDeliveryHeadMap.put("salesOrderId", soSalesOrderHead.getHeadId());
			    soDeliveryHeadMap.put("employeeCode", soSalesOrderHead.getSuperintendentCode());
			    soDeliveryHeadMap.put("logType", "AUTO");
			    soDeliveryHeadDAO.updateDeliveryData(SoDeliveryHeadDAO.SP_TYPE_SALES_ORDER_ID, soDeliveryHeadMap);
			}
		    }catch(Exception e){
		    	responseId = 4L;
		    	posCommandMap.put("responseId", responseId);
		    	e.printStackTrace();
		    	throw new Exception("DATA_ID = "+ dataId + " POS銷售上傳錯誤，原因 ：" + e.getMessage());
		    }
		}
		log.info("dataId = " + dataId + "，銷售資料上傳完成。");
		responseId = 1L;
	    }
	}catch(FormException e){
    	responseId = -5L;
    }catch(Exception e){
		log.info(e.getMessage() + " dataId = " + dataId + " , uuId = " + uuId + " , ip = " + ip);	    
		responseId = (Long)posCommandMap.get("responseId");;
	}
	return responseId;
    }

    public void executeDownloadTransfer(HashMap posDUMap) throws Exception{
	posImportDataService.executeDownloadTransfer(posDUMap);
    }

    public void updateCleanSO(HashMap posDUMap) throws Exception{
	posImportDataService.updateCleanSO(posDUMap);
    }

    /**
     * 執行儲位單 更新 與 比對
     */
    public void executeStorage(SoSalesOrderHead soSalesOrderHead) throws Exception {
	log.info("executeStorage");
	//更新儲位單頭 2011.11.11 by Caspar
	Map storageMap = new HashMap();
	storageMap.put("storageTransactionDate", "salesOrderDate");
	storageMap.put("storageTransactionType", ImStorageService.OUT);
	storageMap.put("deliveryWarehouseCode", "defaultWarehouseCode");
	ImStorageHead imStorageHead = imStorageAction.executeImStorageHead(storageMap, soSalesOrderHead);

	//更新儲位單身與比對 2011.11.11 by Caspar
	storageMap.put("beanItem", "soSalesOrderItems");
	storageMap.put("quantity", "quantity");
	imStorageAction.executeImStorageItem(storageMap, soSalesOrderHead, imStorageHead);
    }
}