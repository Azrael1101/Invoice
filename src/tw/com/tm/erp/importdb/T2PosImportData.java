package tw.com.tm.erp.importdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.SoPostingTally;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderPayment;
import tw.com.tm.erp.hbm.bean.TmpImportPosItem;
import tw.com.tm.erp.hbm.bean.TmpImportPosItemId;
import tw.com.tm.erp.hbm.bean.TmpImportPosPayment;
import tw.com.tm.erp.hbm.bean.TmpImportPosPaymentId;
import tw.com.tm.erp.hbm.dao.BuCountryDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderItemDAO;
import tw.com.tm.erp.hbm.dao.TmpImportPosItemDAO;
import tw.com.tm.erp.hbm.dao.TmpImportPosPaymentDAO;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.ImItemPriceService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.hbm.service.SoPostingTallyService;
import tw.com.tm.erp.hbm.service.SoSalesOrderMainService;
import tw.com.tm.erp.hbm.service.TmpImportPosItemService;
import tw.com.tm.erp.hbm.service.TmpImportPosPaymentService;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.OldSysMapNewSys;
import tw.com.tm.erp.utils.ValidateUtil;
import tw.com.tm.erp.utils.sp.AppExtendItemInfoService;

/**
 * T2 POS 資料匯入
 * 
 * 
 */
public class T2PosImportData {

	private static final Log log = LogFactory.getLog(T2PosImportData.class);


	private TmpImportPosItemService tmpImportPosItemService;

	private TmpImportPosPaymentService tmpImportPosPaymentService;

	private SoPostingTallyService soPostingTallyService;

	private SoSalesOrderMainService soSalesOrderMainService;

	private TmpImportPosItemDAO tmpImportPosItemDAO;

	private TmpImportPosPaymentDAO tmpImportPosPaymentDAO;

	private ImItemDAO imItemDAO;

	private BuCountryDAO buCountryDAO;

	private SoSalesOrderHeadDAO soSalesOrderHeadDAO;

	private SoSalesOrderItemDAO soSalesOrderItemDAO;

	private AppExtendItemInfoService appExtendItemInfoService;

	private SiProgramLogAction siProgramLogAction;

	private BuShopMachineDAO buShopMachineDAO;

	private BuCommonPhraseService buCommonPhraseService;

	private ImItemPriceService imItemPriceService;

	public void setImItemPriceService(ImItemPriceService imItemPriceService) {
		this.imItemPriceService = imItemPriceService;
	}

	public void setTmpImportPosItemService(TmpImportPosItemService tmpImportPosItemService) {
		this.tmpImportPosItemService = tmpImportPosItemService;
	}

	public void setTmpImportPosPaymentService(TmpImportPosPaymentService tmpImportPosPaymentService) {
		this.tmpImportPosPaymentService = tmpImportPosPaymentService;
	}

	public void setSoPostingTallyService(SoPostingTallyService soPostingTallyService) {
		this.soPostingTallyService = soPostingTallyService;
	}

	public void setSoSalesOrderMainService(SoSalesOrderMainService soSalesOrderMainService) {
		this.soSalesOrderMainService = soSalesOrderMainService;
	}

	public void setTmpImportPosItemDAO(TmpImportPosItemDAO tmpImportPosItemDAO) {
		this.tmpImportPosItemDAO = tmpImportPosItemDAO;
	}

	public void setTmpImportPosPaymentDAO(TmpImportPosPaymentDAO tmpImportPosPaymentDAO) {
		this.tmpImportPosPaymentDAO = tmpImportPosPaymentDAO;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	public void setBuCountryDAO(BuCountryDAO buCountryDAO) {
		this.buCountryDAO = buCountryDAO;
	}

	public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
		this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
	}

	public void setAppExtendItemInfoService(AppExtendItemInfoService appExtendItemInfoService) {
		this.appExtendItemInfoService = appExtendItemInfoService;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
		this.buShopMachineDAO = buShopMachineDAO;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setSoSalesOrderItemDAO(SoSalesOrderItemDAO soSalesOrderItemDAO) {
		this.soSalesOrderItemDAO = soSalesOrderItemDAO;
	}

	public void parsingSoFile(List transactionResults, List paymentResults,
			String fileKey, Date posDate, String posMachineCode, HashMap uiProperties)
	throws ValidationErrorException, Exception {
		log.info("===ENTER==t2POSImportData.parsingSoFile===");
		String errorMsg = null;
		String loginUser = "SYS";
		String brandCode = "T2";
		User userObj = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);    
		if (userObj != null) {
			loginUser = userObj.getEmployeeCode();
			brandCode = userObj.getBrandCode();
		}
		String processName = (String)uiProperties.get(ImportDBService.PRCCESS_NAME);
		Date executeDate = (Date)uiProperties.get("actualExecuteDate");
		String autoJobControl = (String)uiProperties.get("autoJobControl");
		String uuid = (String)uiProperties.get("uuidCode");
		String successFilePath = (String) uiProperties.get(ImportDBService.SUCCESS_FILE_PATH);
		String failFilePath = (String) uiProperties.get(ImportDBService.FAIL_FILE_PATH);
		File successFile = new File(successFilePath);
		File failFile = new File(failFilePath);
		String transactionDate = null;
		try{
			log.info("posMachineCode = " + posMachineCode);
			BuShopMachine buShopMachine = (buShopMachineDAO.findByBrandCodeAndMachineCode(brandCode, posMachineCode));

			// 機台不等於自動轉才執行排程
			if(null != buShopMachine ){
				String uploadType = buShopMachine.getUploadType();

				if( !"AUTO".equalsIgnoreCase(uploadType) || !"Y".equals(autoJobControl) ){

					log.info(posMachineCode+ "是晚上自動排程:"+uploadType);

					transactionDate = DateUtils.format(posDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
					SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行POS機碼(" + posMachineCode + ")、交易日期(" + transactionDate + ")轉檔程序...", executeDate, uuid, loginUser);
					// 執行檢核
					HashMap parameterMap = new HashMap();
					parameterMap.put("brandCode", brandCode);
					parameterMap.put("fileKey", fileKey);
					parameterMap.put("posMachineCode", posMachineCode);
					parameterMap.put("actualSalesDate", posDate);
					parameterMap.put("transactionDate", transactionDate);
					parameterMap.put("orderTypeCode", "SOP");
					parameterMap.put("identification", "POS");
					parameterMap.put("opUser", loginUser);
					doValidate(parameterMap);         
					//解析交易明細檔
					LinkedHashSet transactionSeqs = parsingTransactionFiles(transactionResults, fileKey, posDate, posMachineCode, loginUser);
					LinkedHashSet allTransactionSeqs = (LinkedHashSet)transactionSeqs.clone(); //所有的交易序號
					//解析付款明細檔
					parsingPaymentFiles(transactionSeqs, paymentResults, fileKey, posDate, posMachineCode, loginUser);
					//檢核交易序號是否皆有付款資訊
					if(transactionSeqs.size() > 0){
						String msg = "POS識別碼(" + fileKey + ")其交易序號(";
						Iterator it = transactionSeqs.iterator();
						while(it.hasNext()){
							errorMsg = msg + it.next() + ")並無相關的付款明細資料！";
							SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser); 
						}
						throw new ValidationErrorException("POS識別碼(" + fileKey + ")其交易明細中的交易序號並無完整的付款明細資料！");
					}
					//==========將資料轉成bean==========
					List entityBeans = produceSoBeans(allTransactionSeqs, transactionDate, posDate, posMachineCode, brandCode, fileKey, parameterMap);
					//==========POS重傳時刪除相關銷售、出貨資料，並將轉檔資料存檔==========
					soSalesOrderMainService.saveT2PosNoValidate(entityBeans, parameterMap);     
					//==========取報單訊息、批號、檢核無誤後扣庫存並將狀態改為SIGNING===========
					updatePosData(processName, executeDate, uuid, parameterMap) ;

				}else{
					log.info(posMachineCode+ "是白天即時上傳:"+uploadType);
				}
			}else{
				throw new ValidationErrorException("查無品牌("+brandCode+")、POS機台(" + posMachineCode + ")資料！");
			}
			//========================搬移至轉檔成功資料夾=========================
			moveFilesToDestination(successFile, transactionResults, paymentResults);
		}catch(Exception ex){
			errorMsg = "執行POS機碼(" + posMachineCode + ")、交易日期(" + transactionDate + ")轉檔發生錯誤；";
			log.error(errorMsg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg + ex.getMessage(), executeDate, uuid, loginUser);
			//========================搬移至轉檔失敗資料夾=========================
			moveFilesToDestination(failFile, transactionResults, paymentResults);
		}finally{
			try{
				SoPostingTally postingTallyPO = soPostingTallyService.findSoPostingTallyById(posMachineCode, posDate);
				if(postingTallyPO != null){
					//機台轉檔結束後將此機台unlock
					postingTallyPO.setReserve5("N");
					soPostingTallyService.updateSoPostingTally(postingTallyPO);
				}
				tmpImportPosItemService.deletePosIetmByIdentification(posDate, posMachineCode);
				tmpImportPosPaymentService.deletePosPaymentByIdentification(posDate, posMachineCode);		
			}catch(Exception ex){
				errorMsg = ex.toString();
				log.error(errorMsg);
				SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser);
			}
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "POS機碼(" + posMachineCode + ")、交易日期(" + transactionDate + ")轉檔程序結束...", executeDate, uuid, loginUser);
		}
		log.info("===LEAVE==t2POSImportData.parsingSoFile===");
	}


	/**
	 * 解析交易明細檔
	 * 
	 * @param transactionResults
	 * @param fileKey
	 * @param posDate
	 * @param posMachineCode
	 * @param loginUser
	 * @return LinkedHashSet
	 * @throws Exception
	 */
	private LinkedHashSet parsingTransactionFiles(List transactionResults, String fileKey,
			Date posDate, String posMachineCode, String loginUser)
	throws Exception {

		LinkedHashSet transactionSeqs = new LinkedHashSet();
		String errorMsg = null;
		File posFile = null;
		String posFileName = null;
		String shopCode = null;
		BufferedReader bufReader = null;
		String transactionData = null;
		byte[] transactionDataByteArray = null;
		byte[] dest = null;
		String fileId = null; //檔名的識別碼
		String contentFileId = null; //檔案內容的識別碼
		String salesDate = null; //銷貨日期
		Date salesOrderDate = null; //銷貨日期
		String itemSeqTmp = null; //項次
		Long itemSeq = null; //項次
		String itemCode = null; //貨號
		String originalUnitPriceTmp = null; //零售價
		Double originalUnitPrice = null; //零售價
		String qty = null; //數量
		Double quantity = null; //數量
		String discountRateTmp = null; //折扣率
		Double discountRate = null; //折扣率	
		String actualSalesAmountTmp = null; //實際總售價
		Double actualSalesAmount = null; //實際總售價
		String discountAmountTmp = null; //減價金額
		Double discountAmount = null; //減價金額
		String salesPerson = null; //銷售員
		String vipNo = null; //VIP代碼
		String salesOrderNo = null; //銷貨單號
		String transactionTime = null; //交易時段
		String passportNo = null; //護照
		String flightNo = null;   //班機
		String countryCode = null; //國別代碼	
		String departureDateTmp = null; //離境日期
		Date departureDate = null; //離境日期
		String transactionSeqNo = null; //交易序號
		String vipTypeCode = null; //vip類別	
		String standardPriceTmp = null; //訂價
		Double standardPrice = null; //訂價
		String ladingNo = null;  //提貨單號	
		String usedIdentification = null; //使用身份
		String usedCardId = null; //使用卡號
		String usedCardType = null; //使用卡別
		String usedDiscountRateTmp = null; //使用折扣率
		Double usedDiscountRate = null; //使用折扣率
		String itemDiscountType = null; //商品類型
		String combineCode = null; //組合代號

		try{
			for(int i = 0; i < transactionResults.size(); i++){
				posFile = (File)transactionResults.get(i);
				posFileName = posFile.getName().toUpperCase();
				// TW2AD
				fileId = posFileName.substring(1, posFileName.indexOf(".")); // DAAAAAAXXC.REBBBB 即為字串A的部份
				if(posFileName.indexOf(".") != -1){
					shopCode = posFileName.substring(posFileName.indexOf(".") + 1, posFileName.length());
				}	    	
				bufReader = new BufferedReader(new FileReader(posFile));
				int seq = 0;
				while ((transactionData = bufReader.readLine()) != null) {
					++seq;
					transactionDataByteArray = transactionData.getBytes();
					// TW2AD
					int checkBytes_M = 291; //馬祖 M6~M7, N0~N4
				    int checkBytes = 312;   //T2機場改版 
					int shiftMove = 0;
					if(posFileName.substring(1,3).equalsIgnoreCase("20")){
						shiftMove = 2;
					}
					checkBytes = checkBytes + shiftMove; // 因移動位移
					checkBytes_M = checkBytes_M + shiftMove; //因移動位移

					if (transactionDataByteArray.length > 0 && (transactionDataByteArray.length != checkBytes && transactionDataByteArray.length != checkBytes_M)) {//新舊格式同時不符合才不符合
						throw new ValidationErrorException("第" + seq + "項格式錯誤！");
					}
					//日期機號
					dest = new byte[13+shiftMove];
					System.arraycopy(transactionDataByteArray, 0, dest, 0, 13+shiftMove );		    
					contentFileId = new String(dest).trim();
					if(!fileId.equals(contentFileId)){
						throw new ValidationErrorException("第" + seq + "項識別碼(" + contentFileId + ")與檔名識別碼(" + fileId + ")不相符！");
					}
					//銷貨日期
					// TW2AD
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 13+shiftMove, dest, 0, 10);
					salesDate = new String(dest).trim();
					if(salesDate.length() != 10){
						throw new ValidationErrorException("第" + seq + "項銷貨日期(" + salesDate+ ")格式錯誤！");
					}
					salesOrderDate = DateUtils.parseDate("yyyy/MM/dd", salesDate);
					if(posDate.compareTo(salesOrderDate) != 0){
						throw new ValidationErrorException("第" + seq + "項銷貨日期(" + salesDate+ ")與檔名的銷貨日期(" + fileId + ")不相符！");
					}
					//項次
					dest = new byte[3];
					System.arraycopy(transactionDataByteArray, 23+shiftMove, dest, 0, 3);
					itemSeqTmp = new String(dest).trim();
					try{
						itemSeq = Long.parseLong(itemSeqTmp);
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項項次(" + itemSeqTmp+ ")格式錯誤！");
					}
					//品號
					dest = new byte[13];
					System.arraycopy(transactionDataByteArray, 26+shiftMove, dest, 0, 13);	    
					itemCode = new String(dest).trim();
					if(!StringUtils.hasText(itemCode)){
						throw new ValidationErrorException("第" + seq + "項品號(" + itemCode+ ")為空值！");
					}
					//零售價
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 39+shiftMove, dest, 0, 10);
					originalUnitPriceTmp = new String(dest).trim();
					try{
						originalUnitPrice = Double.parseDouble(originalUnitPriceTmp);
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項零售價(" + originalUnitPriceTmp + ")格式錯誤！");
					}
					//數量
					dest = new byte[8];
					System.arraycopy(transactionDataByteArray, 49+shiftMove, dest, 0, 8);    
					qty = new String(dest).trim();
					try{
						quantity = Double.parseDouble(qty);
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項數量(" + qty + ")格式錯誤！");
					}
					//折扣率
					dest = new byte[3];
					System.arraycopy(transactionDataByteArray, 57+shiftMove, dest, 0, 3); 
					discountRateTmp = new String(dest).trim();
					try{
						discountRate = Double.parseDouble(discountRateTmp);
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項折扣率(" + discountRateTmp + ")格式錯誤！");
					}		    
					//實際總售價
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 60+shiftMove, dest, 0, 10); 		    
					actualSalesAmountTmp = new String(dest).trim();
					try{
						actualSalesAmount = Double.parseDouble(actualSalesAmountTmp);
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項總售價(" + actualSalesAmountTmp + ")格式錯誤！");
					}	    

					//減價金額
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 70+shiftMove, dest, 0, 10);		    		    
					discountAmountTmp = new String(dest).trim();
					try{
						discountAmount = Double.parseDouble(discountAmountTmp);
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項減價金額(" + discountAmountTmp + ")格式錯誤！");
					}
					//銷售員
					dest = new byte[8];
					System.arraycopy(transactionDataByteArray, 80+shiftMove, dest, 0, 8);		    
					salesPerson = new String(dest).trim();
					//客戶代碼
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 88+shiftMove, dest, 0, 10);		    
					vipNo = new String(dest).trim();
					if(StringUtils.hasText(vipNo)){
						String vipPrefix = vipNo.substring(0, 1);
						if("：".startsWith(vipPrefix))
							throw new ValidationErrorException("第" + seq + "項VIP代碼(" + vipNo + ")格式錯誤！");
						else if(":".startsWith(vipPrefix))
							vipNo = vipNo.substring(1, vipNo.length());
					}
					//銷貨單號
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 98+shiftMove, dest, 0, 10);		    
					salesOrderNo = new String(dest).trim();
					//時段
					dest = new byte[2];
					System.arraycopy(transactionDataByteArray, 108+shiftMove, dest, 0, 2);		    
					transactionTime = new String(dest).trim();
					//護照
					dest = new byte[20];
					System.arraycopy(transactionDataByteArray, 110+shiftMove, dest, 0, 20);		    
					passportNo = new String(dest).trim();
					//班機
					dest = new byte[20];
					System.arraycopy(transactionDataByteArray, 130+shiftMove, dest, 0, 20);		    
					flightNo = new String(dest).trim();
					//國籍
					dest = new byte[20];
					System.arraycopy(transactionDataByteArray, 150+shiftMove, dest, 0, 20);
					countryCode = new String(dest).trim();
					if(StringUtils.hasText(countryCode) && countryCode.length() > 2){
						countryCode = countryCode.substring(0, 2);
					}		    
					//離境日期
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 170+shiftMove, dest, 0, 10);		    
					departureDateTmp = new String(dest).trim();		    
					if(departureDateTmp.length() != 10){
						throw new ValidationErrorException("第" + seq + "項離境日期(" + departureDateTmp+ ")格式錯誤！");
					}
					departureDate = DateUtils.parseDate("yyyy/MM/dd", departureDateTmp);
					//交易序號
					dest = new byte[8];
					System.arraycopy(transactionDataByteArray, 180+shiftMove, dest, 0, 8);		    
					transactionSeqNo = new String(dest).trim();
					if(transactionSeqNo.length() != 8){
						throw new ValidationErrorException("第" + seq + "項交易序號(" + transactionSeqNo+ ")格式錯誤！");
					}		    
					transactionSeqs.add(transactionSeqNo);
					//VIP類型
					dest = new byte[2];
					System.arraycopy(transactionDataByteArray, 188+shiftMove, dest, 0, 2);
					vipTypeCode = new String(dest).trim();
					//訂價
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 190+shiftMove, dest, 0, 10);	    
					standardPriceTmp = new String(dest).trim();
					try{
						standardPrice = Double.parseDouble(standardPriceTmp);
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項訂價(" + standardPriceTmp + ")格式錯誤！");
					}
					//提貨單號
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 209+shiftMove, dest, 0, 10);
					ladingNo = new String(dest).trim();
					//使用身份
					dest = new byte[2];
					System.arraycopy(transactionDataByteArray, 264+shiftMove, dest, 0, 2);
					usedIdentification = new String(dest).trim();
					//使用卡號
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 266+shiftMove, dest, 0, 10);
					usedCardId = new String(dest).trim();
					if(StringUtils.hasText(usedCardId)){
						String usedCardIdPrefix = usedCardId.substring(0, 1);
						if("：".startsWith(usedCardIdPrefix))
							throw new ValidationErrorException("第" + seq + "項使用卡號(" + usedCardId + ")格式錯誤！");
						else if(":".startsWith(usedCardIdPrefix))
							usedCardId = usedCardId.substring(1, usedCardId.length());
					}
					//使用卡別
					dest = new byte[2];
					System.arraycopy(transactionDataByteArray, 276+shiftMove, dest, 0, 2);
					usedCardType = new String(dest).trim();
					//使用折扣率
					dest = new byte[3];
					System.arraycopy(transactionDataByteArray, 278+shiftMove, dest, 0, 3);
					usedDiscountRateTmp = new String(dest).trim();
					try{
						if(StringUtils.hasText(usedDiscountRateTmp)){
							usedDiscountRate = Double.parseDouble(usedDiscountRateTmp);
						}
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項使用折扣率(" + usedDiscountRateTmp + ")格式錯誤！");
					}
					//商品類型
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 281+shiftMove, dest, 0, 10);
					itemDiscountType = new String(dest).trim();
					
					//品號
					dest = new byte[8];
					System.arraycopy(transactionDataByteArray, 304+shiftMove, dest, 0, 8);	    
					combineCode = new String(dest).trim();					

					System.out.print("fileId=[" + fileId + "]");
					System.out.print("salesDate=[" + salesDate + "]");
					System.out.print("itemSeq=[" + itemSeq + "]");
					System.out.print("itemCode=[" + itemCode + "]");
					System.out.print("originalUnitPrice=[" + originalUnitPrice + "]");
					System.out.print("quantity=[" + quantity + "]");
					System.out.print("discountRate=[" + discountRate + "]");
					System.out.print("actualSalesAmount=[" + actualSalesAmount + "]");
					System.out.print("discountAmount=[" + discountAmount + "]");
					System.out.print("salesPerson=[" + salesPerson + "]");
					System.out.print("vipNo=[" + vipNo + "]");
					System.out.print("salesOrderNo=[" + salesOrderNo + "]");
					System.out.print("transactionTime=[" + transactionTime + "]");		    
					System.out.print("passportNo=[" + passportNo + "]");
					System.out.print("flightNo=[" + flightNo + "]");
					System.out.print("countryCode=[" + countryCode + "]");
					System.out.print("departureDate=[" + departureDateTmp + "]");
					System.out.print("transactionSeqNo=[" + transactionSeqNo + "]");
					System.out.print("vipTypeCode=[" + vipTypeCode + "]");
					System.out.print("standardPrice=[" + standardPrice + "]");
					System.out.print("ladingNo=[" + ladingNo + "]");		    
					System.out.print("usedIdentification=[" + usedIdentification + "]");
					System.out.print("usedCardId=[" + usedCardId + "]");
					System.out.print("usedCardType=[" + usedCardType + "]");
					System.out.print("usedDiscountRate=[" + usedDiscountRate + "]");
					log.info("itemDiscountType=[" + itemDiscountType + "]");
					System.out.print("combineCode=[" + combineCode + "]");

					TmpImportPosItemId tmpImportPosItemId = new TmpImportPosItemId();
					TmpImportPosItem tmpImportPosItem = new TmpImportPosItem();
					tmpImportPosItemId.setFileId(fileId);
					tmpImportPosItemId.setSalesOrderDate(salesOrderDate);
					tmpImportPosItemId.setPosMachineCode(posMachineCode);
					tmpImportPosItemId.setTransactionSeqNo(transactionSeqNo);
					tmpImportPosItemId.setSeq(itemSeq);
					tmpImportPosItem.setId(tmpImportPosItemId);	
					tmpImportPosItem.setShopCode(shopCode);
					tmpImportPosItem.setItemCode(itemCode);
					tmpImportPosItem.setOriginalUnitPrice(originalUnitPrice);
					tmpImportPosItem.setQuantity(quantity);
					tmpImportPosItem.setDiscountRate(discountRate);
					tmpImportPosItem.setActualSalesAmount(actualSalesAmount);
					tmpImportPosItem.setDiscountAmount(discountAmount);
					tmpImportPosItem.setSuperintendentCode(salesPerson);
					tmpImportPosItem.setCustomerCode(vipNo);
					tmpImportPosItem.setCustomerPoNo(salesOrderNo);
					tmpImportPosItem.setPeriod(transactionTime);
					tmpImportPosItem.setPassportNo(passportNo);
					tmpImportPosItem.setFlightNo(flightNo);
					tmpImportPosItem.setCountryCode(countryCode);
					tmpImportPosItem.setDepartureDate(departureDate);
					tmpImportPosItem.setVipTypeCode(vipTypeCode);
					tmpImportPosItem.setStandardPrice(standardPrice);
					tmpImportPosItem.setLadingNo(ladingNo);
					tmpImportPosItem.setUsedIdentification(usedIdentification);
					tmpImportPosItem.setUsedCardId(usedCardId);
					tmpImportPosItem.setUsedCardType(usedCardType);
					tmpImportPosItem.setUsedDiscountRate(usedDiscountRate);
					tmpImportPosItem.setItemDiscountType(itemDiscountType);		    
					tmpImportPosItem.setFileName(posFileName);
					tmpImportPosItem.setReserve1(fileKey);
					tmpImportPosItem.setCreatedBy(loginUser);
					tmpImportPosItem.setCreationDate(new Date());
					tmpImportPosItem.setCombineCode(combineCode);
					tmpImportPosItemService.savePosItem(tmpImportPosItem);
				}
			} 
			return transactionSeqs;
		}catch(Exception ex){
			errorMsg = "解析交易明細檔(" + posFileName + ")失敗，原因：";
			log.error(errorMsg + ex.toString());
			throw new Exception(errorMsg + ex.getMessage());
		}
	}



	/**
	 * 解析付款明細檔
	 * 
	 * @param transactionSeqs
	 * @param paymentResults
	 * @param fileKey
	 * @param posDate
	 * @param posMachineCode
	 * @param loginUser
	 * @return Set
	 * @throws Exception
	 */
	private void parsingPaymentFiles(Set transactionSeqs, List paymentResults, String fileKey,
			Date posDate, String posMachineCode, String loginUser)
	throws Exception {

		String errorMsg = null;
		File posFile = null;
		String posFileName = null;
		String shopCode = null;
		BufferedReader bufReader = null;
		String paymentData = null;
		byte[] paymentByteArray = null;
		byte[] dest = null;
		String fileId = null; //檔名的識別碼
		String transactionSeqNo = null; //交易序號
		String salesDate = null; //銷貨日期
		Date salesOrderDate = null; //銷貨日期
		String storeId = null; //storeId
		String machineCode = null; //機台	
		String paySeqTmp = null; //項次
		Long paySeq = null; //項次
		String payId = null; //付款類型(外幣)
		String payAmtTmp = null; //付款金額(外幣)
		Double payAmt = null; //付款金額
		String payNo = null; //付款號碼(信用卡號)
		String payQtyTmp = null; //付款張數
		Double payQty = null; //付款張數
		String exchangeRateTmp = null; //匯率
		Double exchangeRate = null; //匯率
		String payDueTmp = null; //付款金額(本幣)
		Double payDue = null; //付款金額(本幣)
		String orderFlag = null; //是否預購
		String orderId = null; //預購代碼
		String casherCode = null; //收銀員		
		try{
			for(int i = 0; i < paymentResults.size(); i++){
				posFile = (File)paymentResults.get(i);
				posFileName = posFile.getName().toUpperCase();
				// TW2AD
				fileId = posFileName.substring(1, posFileName.indexOf(".")-1); // DAAAAAAMBB.REBBBB 即為字串A的部份
				if(posFileName.indexOf(".") != -1){
					shopCode = posFileName.substring(posFileName.indexOf(".") + 1, posFileName.length());
				}	    	
				bufReader = new BufferedReader(new FileReader(posFile));
				int seq = 0;
				while ((paymentData = bufReader.readLine()) != null) {
					++seq;
					paymentByteArray = paymentData.getBytes();
					if (paymentByteArray.length > 0 && paymentByteArray.length != 125) {
						throw new ValidationErrorException("第" + seq + "項格式錯誤！");
					}
					//交易序號
					dest = new byte[8];
					System.arraycopy(paymentByteArray, 0, dest, 0, 8);		    
					transactionSeqNo = new String(dest).trim();
					if(transactionSeqs.contains(transactionSeqNo)){
						transactionSeqs.remove(transactionSeqNo);
					}
					//銷貨日期
					dest = new byte[10];
					System.arraycopy(paymentByteArray, 8, dest, 0, 10);
					salesDate = new String(dest).trim();
					if(salesDate.length() != 10){
						throw new ValidationErrorException("第" + seq + "項銷貨日期(" + salesDate+ ")格式錯誤！");
					}
					salesOrderDate = DateUtils.parseDate("yyyy/MM/dd", salesDate);
					if(posDate.compareTo(salesOrderDate) != 0){
						throw new ValidationErrorException("第" + seq + "項銷貨日期(" + salesDate+ ")與檔名的銷貨日期(" + fileId + ")不相符！");
					}
					//storeId
					dest = new byte[8];
					System.arraycopy(paymentByteArray, 18, dest, 0, 8);
					storeId = new String(dest).trim();
					//機台
					dest = new byte[3];
					System.arraycopy(paymentByteArray, 26, dest, 0, 3);
					machineCode = new String(dest).trim();
					if(!posMachineCode.equals(machineCode)){
						throw new ValidationErrorException("第" + seq + "項POS機台(" + machineCode+ ")與檔名的POS機台(" + posMachineCode + ")不相符！");
					}
					//項次
					dest = new byte[3];
					System.arraycopy(paymentByteArray, 29, dest, 0, 3);
					paySeqTmp = new String(dest).trim();
					try{
						paySeq = Long.parseLong(paySeqTmp);
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項項次(" + paySeqTmp+ ")格式錯誤！");
					}
					//付款類型
					dest = new byte[4];
					System.arraycopy(paymentByteArray, 32, dest, 0, 4);
					payId = new String(dest).trim();
					//付款金額(外幣)
					dest = new byte[10];
					System.arraycopy(paymentByteArray, 36, dest, 0, 10); 		    
					payAmtTmp = new String(dest).trim();
					try{
						payAmt = Double.parseDouble(payAmtTmp);
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項付款原幣金額(" + payAmtTmp + ")格式錯誤！");
					}
					//付款號碼(信用卡號)
					dest = new byte[20];
					System.arraycopy(paymentByteArray, 46, dest, 0, 20); 		    
					payNo = new String(dest).trim();
					//付款張數
					dest = new byte[10];
					System.arraycopy(paymentByteArray, 66, dest, 0, 10);
					payQtyTmp = new String(dest).trim();
					try{
						payQty = Double.parseDouble(payQtyTmp);
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項付款張數(" + payQtyTmp + ")格式錯誤！");
					}
					//匯率
					dest = new byte[10];
					System.arraycopy(paymentByteArray, 76, dest, 0, 10);
					exchangeRateTmp = new String(dest).trim();
					try{
						exchangeRate = Double.parseDouble(exchangeRateTmp);
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項匯率(" + exchangeRateTmp + ")格式錯誤！");
					}
					//付款金額(本幣)
					dest = new byte[10];
					System.arraycopy(paymentByteArray, 86, dest, 0, 10);
					payDueTmp = new String(dest).trim();
					try{
						payDue = Double.parseDouble(payDueTmp);
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項付款本幣金額(" + payDueTmp + ")格式錯誤！");
					}
					//是否預購
					dest = new byte[1];
					System.arraycopy(paymentByteArray, 96, dest, 0, 1);
					orderFlag = new String(dest).trim();
					//預購代碼
					dest = new byte[20];
					System.arraycopy(paymentByteArray, 97, dest, 0, 20);
					orderId = new String(dest).trim();
					//收銀員
					dest = new byte[8];
					System.arraycopy(paymentByteArray, 117, dest, 0, 8);
					casherCode = new String(dest).trim();

					TmpImportPosPaymentId tmpImportPosPaymentId = new TmpImportPosPaymentId();
					TmpImportPosPayment tmpImportPosPayment = new TmpImportPosPayment();
					tmpImportPosPaymentId.setSalesOrderDate(salesOrderDate);
					tmpImportPosPaymentId.setPosMachineCode(posMachineCode);
					tmpImportPosPaymentId.setTransactionSeqNo(transactionSeqNo);
					tmpImportPosPaymentId.setPaySeq(paySeq);
					tmpImportPosPayment.setId(tmpImportPosPaymentId);
					tmpImportPosPayment.setShopCode(shopCode);
					tmpImportPosPayment.setStoreId(storeId);
					tmpImportPosPayment.setPayId(payId);
					tmpImportPosPayment.setPayAmt(payAmt);
					tmpImportPosPayment.setPayNo(payNo);
					tmpImportPosPayment.setPayQty(payQty);
					tmpImportPosPayment.setExchangeRate(exchangeRate);
					tmpImportPosPayment.setPayDue(payDue);
					tmpImportPosPayment.setOrderFlag(orderFlag);
					tmpImportPosPayment.setOrderId(orderId);
					tmpImportPosPayment.setCasherCode(casherCode);
					tmpImportPosPayment.setFileName(posFileName);
					tmpImportPosPayment.setCreatedBy(loginUser);
					tmpImportPosPayment.setCreationDate(new Date());	    
					tmpImportPosPaymentService.savePosPayment(tmpImportPosPayment);		    
				}
			}
		}catch(Exception ex){
			errorMsg = "解析付款明細檔(" + posFileName + ")失敗，原因：";
			log.error(errorMsg + ex.toString());
			throw new Exception(errorMsg + ex.getMessage());
		}
	}

	private void doValidate(HashMap parameterMap) throws Exception {

		String dateType = "銷貨日期";
		String brandCode = (String) parameterMap.get("brandCode");
		String posMachineCode = (String) parameterMap.get("posMachineCode");
		Date actualSalesDate = (Date) parameterMap.get("actualSalesDate");
		String orderTypeCode = (String) parameterMap.get("orderTypeCode");
		// 檢核是否關帳
		//ValidateUtil.isAfterClose(brandCode, orderTypeCode, dateType,
		//		actualSalesDate);
		// 檢核是否過帳
		SoPostingTally soPostingTally = soPostingTallyService.findSoPostingTallyById(posMachineCode, actualSalesDate);
		if (soPostingTally != null && "Y".equalsIgnoreCase(soPostingTally.getIsPosting())) {
			throw new ValidationErrorException("POS機碼(" + posMachineCode
					+ ")、交易日期：" + DateUtils.format(actualSalesDate)
					+ "已完成過帳，無法執行匯入！");
		}
	}

	private List produceSoBeans(Set transactionSeqs, String transactionDate, Date posDate, String posMachineCode, 
			String brandCode, String fileKey, HashMap parameterMap) throws ValidationErrorException, Exception{

		List entityBeans = new ArrayList();
		Iterator it = transactionSeqs.iterator();
		Date currentDateTime = new Date();
		while(it.hasNext()){
			String transactionSeq = (String)it.next();
			List tmpImportPosItems = tmpImportPosItemDAO.findbyIdentification(posDate, posMachineCode, transactionSeq);
			if(tmpImportPosItems == null || tmpImportPosItems.size() == 0){
				throw new ValidationErrorException("依據POS機碼(" + posMachineCode + ")、交易日期(" + transactionDate + ")、交易序號(" + transactionSeq + ")查無交易明細資料！");
			}
			List tmpImportPosPayments = tmpImportPosPaymentDAO.findbyIdentification(posDate, posMachineCode, transactionSeq);
			if(tmpImportPosPayments == null || tmpImportPosPayments.size() == 0){
				throw new ValidationErrorException("依據POS機碼(" + posMachineCode + ")、交易日期(" + transactionDate + ")、交易序號(" + transactionSeq + ")查無付款明細資料！");
			}
			SoSalesOrderHead salesOrderHead = new SoSalesOrderHead();
			salesOrderHead.setBrandCode(brandCode);
			salesOrderHead.setOrderTypeCode("SOP");
			salesOrderHead.setSalesOrderDate(posDate);
			salesOrderHead.setPaymentTermCode("Z9");
			salesOrderHead.setCurrencyCode("NTD");
			salesOrderHead.setDiscountRate(100D);
			salesOrderHead.setExportExchangeRate(1D);
			salesOrderHead.setInvoiceTypeCode("2");
			salesOrderHead.setTaxType("1");
			salesOrderHead.setTaxRate(0D);
			salesOrderHead.setTaxAmount(0D);
			salesOrderHead.setScheduleCollectionDate(posDate);
			salesOrderHead.setScheduleShipDate(posDate);
			salesOrderHead.setPosMachineCode(posMachineCode);
			salesOrderHead.setTransactionSeqNo("TD" + posMachineCode + transactionSeq);
			salesOrderHead.setSufficientQuantityDelivery("Y");
			salesOrderHead.setStatus(OrderStatus.UNCONFIRMED);
			salesOrderHead.setReserve3(fileKey);
			salesOrderHead.setReserve5("POS");
			salesOrderHead.setCreationDate(currentDateTime);
			salesOrderHead.setLastUpdateDate(currentDateTime);
			//產生銷貨交易明細檔
			produceSoItem(salesOrderHead, tmpImportPosItems, parameterMap);
			//產生銷貨付款明細檔
			produceSoPayment(salesOrderHead, tmpImportPosPayments);
			entityBeans.add(salesOrderHead);
		}
		return entityBeans;
	}

	private void produceSoItem(SoSalesOrderHead salesOrderHead, List tmpImportPosItems, HashMap parameterMap) throws ValidationErrorException, Exception{

		List<SoSalesOrderItem> soSalesOrderItems = new ArrayList(0);
		String brandCode = salesOrderHead.getBrandCode();
		String posMachineCode = salesOrderHead.getPosMachineCode();
		String actualShopCode = null;
		String defaultWarehouseCode = null;
		String superintendentCode = null;
		String customerCode = null;
		String customerPoNo = null;
		String headFileName = null;
		String countryCode = null;
		String period = null;
		String passportNo = null;
		String flightNo = null;
		Date departureDate = null;
		String ladingNo = null;
		Double totalOriginalSalesAmt = 0D;
		Double totalActualSalesAmt = 0D;
		String orderDiscountType = "A"; //無折扣

		if(tmpImportPosItems != null && tmpImportPosItems.size() > 0){
			for(int i = 0; i < tmpImportPosItems.size(); i++){
				TmpImportPosItem importPosItem = (TmpImportPosItem)tmpImportPosItems.get(i);
				if(actualShopCode == null){
					BuShop newBuShop = buShopMachineDAO.getShopCodeByMachineCode(brandCode, posMachineCode, null, null);
					if(newBuShop != null){
						actualShopCode = newBuShop.getShopCode();
						parameterMap.put("actualShopCode", actualShopCode);
						defaultWarehouseCode = newBuShop.getSalesWarehouseCode();
						if(!StringUtils.hasText(defaultWarehouseCode)){
							throw new ValidationErrorException("專櫃代碼(" + actualShopCode + ")並未設定預設的庫別！");
						}
					}else{
						throw new ValidationErrorException("依據品牌(" + brandCode + ")、POS機碼(" + posMachineCode + ")查無對應的專櫃代碼！");
					}
				}
				//銷貨員
				if(superintendentCode == null){
					superintendentCode = importPosItem.getSuperintendentCode();
				}
				//客戶代碼
				if(customerCode == null){
					customerCode = importPosItem.getCustomerCode();
				}
				//customerPoNo
				if(customerPoNo == null){
					customerPoNo = importPosItem.getCustomerPoNo();
				}
				//headFileName
				if(headFileName == null){
					headFileName = importPosItem.getFileName();
				}
				//國別
				if(countryCode == null){		   
					List countrys = buCountryDAO.findByProperty("oldCountryCode", importPosItem.getCountryCode());
					if(countrys != null && countrys.size() > 0){
						countryCode = ((BuCountry)countrys.get(0)).getCountryCode();
					}
				}
				//時段
				if(period == null){
					period = importPosItem.getPeriod();
				}
				//護照號碼
				if(passportNo == null){
					passportNo = importPosItem.getPassportNo();
				}
				//班機號碼
				if(flightNo == null){
					flightNo = importPosItem.getFlightNo();
				}
				//出境日期
				if(departureDate == null){
					departureDate = importPosItem.getDepartureDate();
				}
				//提貨單號
				if(ladingNo == null){
					ladingNo = importPosItem.getLadingNo();
				}

				Date salesOrderDate = importPosItem.getId().getSalesOrderDate();
				String itemCode = importPosItem.getItemCode();
//				Double originalUnitPrice = importPosItem.getStandardPrice();  
				Double originalUnitPrice = imItemPriceService.getUnitPriceByDate(brandCode, itemCode, salesOrderDate); // 20110318 David 改撈KWE當時最接近日期的定變價

				Double quantity = importPosItem.getQuantity();
				String vipTypeCode = importPosItem.getVipTypeCode();
				Double originalSalesAmt = CommonUtils.round(originalUnitPrice * quantity, 0);  
				Double actualSalesAmt = importPosItem.getActualSalesAmount();
				Double actualUnitPrice = getActualUnitPrice(actualSalesAmt, quantity);
				Double discountRate = getDiscountRate(actualSalesAmt, originalSalesAmt);
				String isTax = null;
				String usedIdentification = importPosItem.getUsedIdentification();
				String usedCardId = importPosItem.getUsedCardId();
				String usedCardType = importPosItem.getUsedCardType();
				Double usedDiscountRate = importPosItem.getUsedDiscountRate();
				String itemDiscountType = importPosItem.getItemDiscountType();		
				if(StringUtils.hasText(usedIdentification)){
					if(!"C".equals(orderDiscountType) && ("02".equals(usedIdentification) || "03".equals(usedIdentification))){
						orderDiscountType = "C";
					}else if("A".equals(orderDiscountType) && "01".equals(usedIdentification)){
						orderDiscountType = "B";
					}
				}		
				//正式上線打開
				ImItem imItemPO = imItemDAO.findItem(brandCode, itemCode);
				if(imItemPO == null){
					throw new ValidationErrorException("查無品號(" + itemCode + ")的相關資料！");
				}
				isTax = imItemPO.getIsTax();
				if(!StringUtils.hasText(isTax)){
					throw new ValidationErrorException("品號(" + itemCode + ")的稅別為空值！");
				}		
				String fileName = importPosItem.getFileName();
				totalOriginalSalesAmt += originalSalesAmt;
				totalActualSalesAmt += actualSalesAmt;
				
				String combineCode = importPosItem.getCombineCode();

				SoSalesOrderItem salesOrderItem = new SoSalesOrderItem();
				salesOrderItem.setItemCode(itemCode);
				salesOrderItem.setWarehouseCode(defaultWarehouseCode);
				salesOrderItem.setOriginalForeignUnitPrice(originalUnitPrice);
				salesOrderItem.setOriginalUnitPrice(originalUnitPrice);
				salesOrderItem.setQuantity(quantity);
				salesOrderItem.setOriginalForeignSalesAmt(originalSalesAmt);
				salesOrderItem.setOriginalSalesAmount(originalSalesAmt);
				salesOrderItem.setActualForeignSalesAmt(actualSalesAmt);
				salesOrderItem.setActualSalesAmount(actualSalesAmt);
				salesOrderItem.setPosActualSalesAmount(actualSalesAmt);//放入POS檔案裏的實際銷售金額
				salesOrderItem.setActualForeignUnitPrice(actualUnitPrice);
				salesOrderItem.setActualUnitPrice(actualUnitPrice);
				salesOrderItem.setDiscountRate(discountRate);		
				salesOrderItem.setVipPromotionCode(vipTypeCode);
				salesOrderItem.setScheduleShipDate(salesOrderDate);
				salesOrderItem.setShippedDate(salesOrderDate);
				salesOrderItem.setIsTax(isTax);
				salesOrderItem.setTaxType("1");
				salesOrderItem.setTaxRate(0D);
				salesOrderItem.setTaxAmount(0D);
				//salesOrderItem.setIsServiceItem(imItemPO.getIsServiceItem());
				//salesOrderItem.setIsComposeItem(imItemPO.getIsComposeItem());
				salesOrderItem.setReserve4(fileName);
				salesOrderItem.setReserve5("POS");
				salesOrderItem.setUsedIdentification(usedIdentification);
				salesOrderItem.setUsedCardId(usedCardId);
				salesOrderItem.setUsedCardType(usedCardType);
				salesOrderItem.setUsedDiscountRate(usedDiscountRate);
				salesOrderItem.setItemDiscountType(itemDiscountType);
				//salesOrderItem.setAllowMinusStock(imItemPO.getAllowMinusStock()); //是否允許負庫存
				salesOrderItem.setPosSeq(i + 1L); //此交易明細序號
				salesOrderItem.setCombineCode(combineCode); //組合代號
				soSalesOrderItems.add(salesOrderItem);
			}
			salesOrderHead.setCustomerCode(customerCode);
			salesOrderHead.setCustomerPoNo(customerPoNo);
			salesOrderHead.setCountryCode(countryCode);
			salesOrderHead.setNationalityCode(countryCode);
			salesOrderHead.setShopCode(actualShopCode);
			salesOrderHead.setDefaultWarehouseCode(defaultWarehouseCode);
			salesOrderHead.setSuperintendentCode(superintendentCode);
			salesOrderHead.setTotalOriginalSalesAmount(totalOriginalSalesAmt);
			salesOrderHead.setOriginalTotalFrnSalesAmt(totalOriginalSalesAmt);
			salesOrderHead.setTotalActualSalesAmount(totalActualSalesAmt);
			salesOrderHead.setActualTotalFrnSalesAmt(totalActualSalesAmt);
			salesOrderHead.setExpenseForeignAmount(0D);
			salesOrderHead.setExpenseLocalAmount(0D);
			salesOrderHead.setExportCommissionRate(0D);	    
			salesOrderHead.setTransactionTime(period);
			salesOrderHead.setPassportNo(passportNo);
			salesOrderHead.setFlightNo(flightNo);
			salesOrderHead.setDepartureDate(departureDate);
			salesOrderHead.setLadingNo(ladingNo);
			salesOrderHead.setCreatedBy(superintendentCode);
			salesOrderHead.setLastUpdatedBy(superintendentCode);
			salesOrderHead.setReserve4(headFileName);
			salesOrderHead.setOrderDiscountType(orderDiscountType);
			salesOrderHead.setSoSalesOrderItems(soSalesOrderItems);
		}	
	}

	private void produceSoPayment(SoSalesOrderHead salesOrderHead, List tmpImportPosPayments) throws ValidationErrorException, Exception{

		List<SoSalesOrderPayment> soSalesOrderPayments = new ArrayList(0);
		String casherCode = null;
		if(tmpImportPosPayments != null && tmpImportPosPayments.size() > 0){
			for(int i = 0; i < tmpImportPosPayments.size(); i++){
				TmpImportPosPayment importPosPayment = (TmpImportPosPayment)tmpImportPosPayments.get(i);		
				String posPaymentType = importPosPayment.getPayId(); //付款類別
				Double foreignAmount = importPosPayment.getPayAmt(); //原幣金額
				Double localAmount = importPosPayment.getPayDue();   //本幣金額
				Double exchangeRate = importPosPayment.getExchangeRate();//匯率
				String payNo = importPosPayment.getPayNo();//付款登記
				Double payQty = importPosPayment.getPayQty();//付款張數
				//收銀員
				if(casherCode == null){
					casherCode = importPosPayment.getCasherCode();
				}
				String fileName = importPosPayment.getFileName();
				String foreignCurrencyCode = null; //原幣幣別
				if(StringUtils.hasText(posPaymentType)){
					BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("PaymentType", posPaymentType);
					if(buCommonPhraseLine != null){
						foreignCurrencyCode = buCommonPhraseLine.getAttribute1();
					}
				}
				SoSalesOrderPayment salesOrderPayment = new SoSalesOrderPayment();
				salesOrderPayment.setPosPaymentType(posPaymentType);
				salesOrderPayment.setLocalCurrencyCode("NTD");
				salesOrderPayment.setLocalAmount(localAmount);
				salesOrderPayment.setForeignCurrencyCode(foreignCurrencyCode);
				salesOrderPayment.setForeignAmount(foreignAmount);
				salesOrderPayment.setExchangeRate(exchangeRate);
				salesOrderPayment.setPayNo(payNo);
				salesOrderPayment.setPayQty(payQty);
				salesOrderPayment.setReserve4(fileName);
				salesOrderPayment.setReserve5("POS");
				soSalesOrderPayments.add(salesOrderPayment);
			}	   
			salesOrderHead.setCasherCode(casherCode);
			salesOrderHead.setSoSalesOrderPayments(soSalesOrderPayments);
		}	
	}

	private Double getActualUnitPrice(Double actualSalesAmt, Double quantity){

		if(quantity == 0D){
			return actualSalesAmt;
		}else if(actualSalesAmt != null && actualSalesAmt != 0D && quantity != null && quantity != 0D){
			return CommonUtils.round(actualSalesAmt / quantity, 1);
		}else{
			return 0D;
		}	
	}

	private Double getDiscountRate(Double actualSalesAmt, Double originalSalesAmt){

		if(originalSalesAmt == 0D){
			return 100D;
		}else if(actualSalesAmt != null && actualSalesAmt != 0D && originalSalesAmt != null && originalSalesAmt != 0D){
			return CommonUtils.round(actualSalesAmt / originalSalesAmt * 100, 2);
		}else{
			return 0D;
		}	
	}

	/**
	 * T2的POS上傳作業，取報單訊息、批號、檢核無誤後扣庫存並將狀態改為SIGNING
	 * 
	 * @param entityBeans
	 * @param parameterMap
	 * @throws Exception
	 */
	public void updatePosData(String processName, Date executeDate, String uuid, HashMap parameterMap) throws Exception{
		log.info("===ENTER T2PosImportData.updatePosData====");
		String errorMsg = null;
		String actualbrandCode = (String)parameterMap.get("brandCode");
		String posMachineCode = (String)parameterMap.get("posMachineCode");
		Date actualSalesDate = (Date)parameterMap.get("actualSalesDate");
		String opUser = (String)parameterMap.get("opUser");

		try{
			List<SoSalesOrderHead> salesOrderHeads = soSalesOrderHeadDAO.findT2SOPByProperty(parameterMap);
			if(salesOrderHeads != null && salesOrderHeads.size() > 0){
				List mailContents = new ArrayList(0);
				HashMap conditionMap = new HashMap();
				conditionMap.put("processObjectName", "soSalesOrderMainService");
				conditionMap.put("searchMethodName", "findSoSalesOrderHeadById");
				conditionMap.put("tableType", "SO_SALES_ORDER");
				conditionMap.put("subEntityBeanName", "soSalesOrderItems");
				conditionMap.put("itemFieldName", "itemCode");
				conditionMap.put("warehouseCodeFieldName", "warehouseCode");
				conditionMap.put("declTypeFieldName", "importDeclType");
				conditionMap.put("declNoFieldName", "importDeclNo");
				conditionMap.put("declSeqFieldName", "importDeclSeq");
				conditionMap.put("declDateFieldName", "importDeclDate");		
				conditionMap.put("lotFieldName", "lotNo");
				conditionMap.put("qtyFieldName", "quantity");
				//=======================================================
				for(SoSalesOrderHead salesOrderHead : salesOrderHeads){
					Long salesHeadId = salesOrderHead.getHeadId();
					String identification = MessageStatus.getIdentificationMsg(
							salesOrderHead.getBrandCode(), salesOrderHead.getOrderTypeCode(), 
							salesOrderHead.getOrderNo());

					String identification2 = MessageStatus.getIdentification(
							salesOrderHead.getBrandCode(), salesOrderHead.getOrderTypeCode(), 
							salesOrderHead.getOrderNo());
					try{
						conditionMap.put("searchKey", salesHeadId);
						List<SoSalesOrderItem> soSalesOrderItem = salesOrderHead.getSoSalesOrderItems();
						//===========記錄pos轉入的金額=========
						//HashMap posAMTMap = recordPOSAmount(soSalesOrderItem);
						//===========目前取KWE報單訊息=========
						appExtendItemInfoService.executeExtendItemProcessor(conditionMap);
						//===========目前取小花報單訊息=========
						//appExtendItemInfoService.executeSoItemInfoFromXFire(conditionMap);

						log.info("enter 設定pos轉入的金額-----");
						//設定pos轉入的金額
						//soSalesOrderMainService.setOrderItemPosAMTMap(orderItemPosAMTMap);
						//log.info("leave 設定pos轉入的金額-----soSalesOrderMainService.getOrderItemPosAMTMap().size() = "+soSalesOrderMainService.getOrderItemPosAMTMap().size());
						//重新計算單身、檢核單身資訊是否完整、扣庫存轉出貨單明細
						List assemblyMsg = soSalesOrderMainService.updateT2PosData(processName, executeDate, uuid, salesHeadId, parameterMap, mailContents);
						if(assemblyMsg != null && assemblyMsg.size() == 0){
							soSalesOrderMainService.updateT2PosOnHand(processName, executeDate, uuid, salesHeadId, parameterMap, mailContents, assemblyMsg);
						}
					}catch(Exception ex){
						errorMsg = "更新" + identification + "銷售資料時發生錯誤，原因：";
						log.error(errorMsg + ex.toString());
						SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg + ex.getMessage(), executeDate, uuid, opUser);		        		       
						siProgramLogAction.createProgramLog(SoSalesOrderMainService.PROGRAM_ID, MessageStatus.LOG_ERROR, identification2, errorMsg + ex.getMessage(), opUser);
						mailContents.add(identification);
					}            
				}
				//寄送mail
				soSalesOrderMainService.mailToPOSAdministratorForT2(parameterMap, mailContents);
			}
		}catch(Exception ex){
			errorMsg = "更新品牌代號(" + actualbrandCode + ")、POS機碼(" + posMachineCode + ")、銷售日期(" + DateUtils.format(actualSalesDate) + ")的POS銷售資料時發生錯誤，原因：" + ex.toString();
			log.error(errorMsg);
			throw new Exception(errorMsg);
		}
		log.info("===LEAVE T2PosImportData.updatePosData====");
	}

	private void moveFilesToDestination(File destination, List transactionResults, List paymentResults){

		try{
			for(int i = 0; i < transactionResults.size(); i++){
				try{
					File transactionFile = (File)transactionResults.get(i);
					FileUtils.copyFileToDirectory(transactionFile, destination);
					transactionFile.delete();
				}catch(Exception ex){
					log.error("搬移並刪除交易明細檔案失敗，原因：" + ex.toString());  
				}
			}

			for(int j = 0; j < paymentResults.size(); j++){
				try{
					File paymentFile = (File)paymentResults.get(j);
					FileUtils.copyFileToDirectory(paymentFile, destination);
					paymentFile.delete();
				}catch(Exception ex){
					log.error("搬移並刪除交易明細檔案失敗，原因：" + ex.toString());  
				}
			}
		}catch(Exception ex){
			log.error("搬移並刪除檔案失敗，原因：" + ex.toString());  
		}	
	}
}