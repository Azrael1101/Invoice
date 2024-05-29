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
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.ImStorageAction;
import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImPicking;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
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
import tw.com.tm.erp.hbm.dao.ImPickingDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.TmpImportPosItemDAO;
import tw.com.tm.erp.hbm.dao.TmpImportPosPaymentDAO;
import tw.com.tm.erp.hbm.service.ImItemPriceService;
import tw.com.tm.erp.hbm.service.ImStorageService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.hbm.service.SoPostingTallyService;
import tw.com.tm.erp.hbm.service.SoSalesOrderMainService;
import tw.com.tm.erp.hbm.service.TmpImportPosItemService;
import tw.com.tm.erp.hbm.service.TmpImportPosPaymentService;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.OldSysMapNewSys;
import tw.com.tm.erp.utils.ValidateUtil;
import tw.com.tm.erp.utils.XFireUtils;
import tw.com.tm.erp.utils.sp.AppExtendItemInfoService;

/**
 * T2 POS 資料匯入
 * 
 * 
 */
public class T2PosImportDataMazu {

	private static final Log log = LogFactory.getLog(T2PosImportDataMazu.class);


	private TmpImportPosItemService tmpImportPosItemService;

	private TmpImportPosPaymentService tmpImportPosPaymentService;

	private SoPostingTallyService soPostingTallyService;

	private SoSalesOrderMainService soSalesOrderMainService;

	private TmpImportPosItemDAO tmpImportPosItemDAO;

	private TmpImportPosPaymentDAO tmpImportPosPaymentDAO;

	private ImItemDAO imItemDAO;

	private BuCountryDAO buCountryDAO;

	private SoSalesOrderHeadDAO soSalesOrderHeadDAO;

	private AppExtendItemInfoService appExtendItemInfoService;

	private SiProgramLogAction siProgramLogAction;

	private BuShopMachineDAO buShopMachineDAO;

	private ImPickingDAO imPickingDAO;
	
	private ImItemPriceService imItemPriceService;
	
	private ImStorageAction imStorageAction;

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

	public void setImPickingDAO(ImPickingDAO imPickingDAO) {
		this.imPickingDAO = imPickingDAO;
	}
	
    public void setImStorageAction(ImStorageAction imStorageAction) {
		this.imStorageAction = imStorageAction;
	}
    
	public void parsingSoFile(List transactionResults, List paymentResults, List cmResults,
			String fileKey, Date posDate, String posMachineCode, HashMap uiProperties)
	throws ValidationErrorException, Exception {
		log.info("=========================parsingSoFile==================================");
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
		String uuid = (String)uiProperties.get("uuidCode");
		String successFilePath = (String) uiProperties.get(ImportDBService.SUCCESS_FILE_PATH);
		String failFilePath = (String) uiProperties.get(ImportDBService.FAIL_FILE_PATH);
		File successFile = new File(successFilePath);
		File failFile = new File(failFilePath);
		String transactionDate = null;
		try{
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
			Object[] obj = parsingTransactionFiles(transactionResults, cmResults, fileKey, posDate, posMachineCode, loginUser);
			
			//解析交易明細檔
			LinkedHashSet transactionSeqs = (LinkedHashSet)obj[0];
			List<TmpImportPosItem> tmpImportPosItems = (List<TmpImportPosItem>)obj[1];
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
			List entityBeans = produceSoBeans(allTransactionSeqs, transactionDate, posDate, posMachineCode, brandCode, fileKey, parameterMap, tmpImportPosItems);
			log.info("entityBeans.size = " + entityBeans.size());
			//==========POS重傳時刪除相關銷售、出貨資料，並將轉檔資料存檔==========
			soSalesOrderMainService.saveT2PosNoValidate(entityBeans, parameterMap);     
			//==========取報單訊息、批號、檢核無誤後扣庫存並將狀態改為SIGNING===========
			updatePosData(processName, executeDate, uuid, parameterMap) ;
			//========================搬移至轉檔成功資料夾=========================
			moveFilesToDestination(successFile, transactionResults, paymentResults, cmResults);
		}catch(Exception ex){
			errorMsg = "執行POS機碼(" + posMachineCode + ")、交易日期(" + transactionDate + ")轉檔發生錯誤；";
			log.error(errorMsg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg + ex.getMessage(), executeDate, uuid, loginUser);
			//========================搬移至轉檔失敗資料夾=========================
			moveFilesToDestination(failFile, transactionResults, paymentResults, cmResults);
		}finally{
			log.info("完成Finally");
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
	private Object[] parsingTransactionFiles(List transactionResults, List cmResults, String fileKey,
			Date posDate, String posMachineCode, String loginUser)
	throws Exception {
		log.info("=========================parsingTransactionFiles==================================");
		log.info("posDate = " + posDate);
		LinkedHashSet transactionSeqs = new LinkedHashSet();
		LinkedHashSet customerPoNos = new LinkedHashSet(); 
		LinkedHashSet salesOrderDates = new LinkedHashSet();
		String errorMsg = null;
		File posFile = null;
		String posFileName = null;
		String shopCode = null;
		BufferedReader transBufReader = null;
		String transactionData = null;
		byte[] transactionDataByteArray = null;
		byte[] dest = null;

		File cmFile = null;
		BufferedReader cmBufReader = null;
		String cmData = null;
		byte[] cmDataByteArray = null;

		String fileId = null; //檔名的識別碼
		Double originalUnitPrice = null; //原單價
		Double discountRate = null; //折扣率	
		//Double actualSalesAmount = null; //實價
		Double discountAmount = null; //減價金額
		String salesPerson = null; //銷售員
		String vipNo = null; //VIP代碼
		String transactionTime = null; //交易時段
		String countryCode = null; //國別代碼	
		Date departureDate = null; //離境日期
		String transactionSeqNo = null; //交易序號
		String vipTypeCode = null; //vip類別	
		Double standardPrice = null; //訂價
		String ladingNo = null;  //提貨單號
		String usedIdentification = null; //使用身份
		String usedCardId = null; //使用卡號
		String usedCardType = null; //使用卡別
		Double usedDiscountRate = null; //使用折扣率
		String itemDiscountType = null; //商品類型
		String transType = null; //傳輸作業別
		String islandsCode = null; //離島別
		String storeCode = null; //免稅店
		String transactionId = null; //交易碼
		String buyerId = null; //購物者ID
		String customerPoNo = null; //銷售單單號
		Date salesOrderDate = null; //銷售單日期
		Long indexNo = null; //項次
		Double quantity = null; //數量
		String salesUnit = null; //銷售單位
		Double actualSalesAmount = null; //實際總售價
		Double importTax = null; //進口稅
		Double goodsTax = null; //貨物稅
		Double cigarWineTax = null; //煙酒稅
		Double healthTax = null; //健康稅
		Double businessTax = null; //營業稅
		String declNo = null;  //原報單號碼
		Long declSeq = null;  //原報單項次
		String cigarWineRemark = null; //煙酒註記
		Double cigarWineQty = null; //煙酒數量
		Double taxationPcent = null; //超額徵稅百分比
		Double taxUnitQty = null; //原進倉報單銷售數量
		String ODeclNo = null; //來源報單號碼
		Long ODeclSeq = null; //來源報單項次
		String itemCode = null; //貨號
		String lotNo = null; //批號
		Map cmPosMap = new HashMap(0);
		List<TmpImportPosItem> tmpImportPosItems = new ArrayList<TmpImportPosItem>(0);
		try{
			log.info("cmResults.size() = " + cmResults.size());
			//這邊做解析Cm記錄
			int cmSeq = 0;
			for (int i = 0; i < cmResults.size(); i++) {
				cmFile = (File)cmResults.get(i);
				cmBufReader = new BufferedReader(new FileReader(cmFile));

				while ((cmData = cmBufReader.readLine()) != null) {
					++cmSeq;
					log.info("cm seq = " + cmSeq);
					log.info("cm cmData.getBytes() = " + cmData.getBytes());
					cmDataByteArray = cmData.getBytes();
					log.info("cm cmDataByteArray = " + new String(cmDataByteArray));
					
					//作業類別
					dest = new byte[1];
					System.arraycopy(cmDataByteArray, 0, dest, 0, 1);		    
					transType = new String(dest).trim();
					if(!StringUtils.hasText(transType)){
						throw new ValidationErrorException("第" + cmSeq + "項作業類別為空值！");
					}
					
					//項次
					dest = new byte[3];
					System.arraycopy(cmDataByteArray, 1, dest, 0, 3);
					try{
						indexNo = Long.parseLong(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + cmSeq + "項項次(" + indexNo+ ")格式錯誤！");
					}
					
					//離島別代號
					dest = new byte[1];
					System.arraycopy(cmDataByteArray, 4, dest, 0, 1);		    
					islandsCode = new String(dest).trim();
					if(!StringUtils.hasText(islandsCode)){
						throw new ValidationErrorException("第" + cmSeq + "項離島別代號為空值！");
					}

					//免稅店代號
					dest = new byte[1];
					System.arraycopy(cmDataByteArray, 5, dest, 0, 1);		    
					storeCode = new String(dest).trim();
					if(!StringUtils.hasText(storeCode)){
						throw new ValidationErrorException("第" + cmSeq + "項免稅店代號為空值！");
					}
					
					//交易碼
					dest = new byte[14];
					System.arraycopy(cmDataByteArray, 6, dest, 0, 14);		    
					transactionId = new String(dest).trim();
					if(!StringUtils.hasText(transactionId)){
						throw new ValidationErrorException("第" + cmSeq + "項交易碼為空值！");
					}

					//購物者ID
					dest = new byte[14];
					System.arraycopy(cmDataByteArray, 20, dest, 0, 14);		    
					buyerId = new String(dest).trim();
					if(!StringUtils.hasText(buyerId)){
						throw new ValidationErrorException("第" + cmSeq + "項購物者ID為空值！");
					}
					
					//銷售單單號
					dest = new byte[14];
					System.arraycopy(cmDataByteArray, 34, dest, 0, 14);		    
					customerPoNo = new String(dest).trim();
					if(!StringUtils.hasText(customerPoNo)){
						throw new ValidationErrorException("第" + cmSeq + "項銷售單單號為空值！");
					}
					customerPoNos.add(customerPoNo);
					
					//銷售單日期
					dest = new byte[10];
					System.arraycopy(cmDataByteArray, 51, dest, 0, 10);
					if(new String(dest).trim().length() != 10){
						throw new ValidationErrorException("第" + cmSeq + "項A銷售單日期(" + new String(dest).trim()+ ")格式錯誤！");
					}
					salesOrderDate = DateUtils.parseDate("yyyy/MM/dd",(new String(dest).trim()));
					if(posDate.compareTo(salesOrderDate) != 0){
						throw new ValidationErrorException("第" + cmSeq + "項A銷售單日期(" + salesOrderDate+ ")與檔名的銷售單日期(" + fileId + ")不相符！");
					}
					salesOrderDates.add(salesOrderDate);
					
					//銷售單位
					dest = new byte[3];
					System.arraycopy(cmDataByteArray, 72, dest, 0, 3);		    
					salesUnit = new String(dest).trim();
					
					
					//進口稅
					dest = new byte[8];
					System.arraycopy(cmDataByteArray, 83, dest, 0, 8);		    
					try{
						importTax = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + cmSeq + "項項次(" + importTax+ ")格式錯誤！");
					}
					//if(null == importTax || 0 == importTax)
						//throw new ValidationErrorException("第" + cmSeq + "項進口稅為空值！");

					//貨物稅
					dest = new byte[8];
					System.arraycopy(cmDataByteArray, 91, dest, 0, 8);		    
					try{
						goodsTax = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + cmSeq + "項項次(" + goodsTax+ ")格式錯誤！");
					}
					//if(null == goodsTax || 0 == goodsTax)
						//throw new ValidationErrorException("第" + cmSeq + "項貨物稅為空值！");

					//煙酒稅
					dest = new byte[8];
					System.arraycopy(cmDataByteArray, 99, dest, 0, 8);		    
					try{
						cigarWineTax = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + cmSeq + "項項次(" + cigarWineTax+ ")格式錯誤！");
					}
					//if(null == cigarWineTax || 0 == cigarWineTax)
						//throw new ValidationErrorException("第" + cmSeq + "項煙酒稅為空值！");

					//健康稅
					dest = new byte[8];
					System.arraycopy(cmDataByteArray, 108, dest, 0, 8);		    
					try{
						healthTax = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + cmSeq + "項項次(" + healthTax+ ")格式錯誤！");
					}
					//if(null == healthTax || 0 == healthTax)
						//throw new ValidationErrorException("第" + cmSeq + "項健康稅為空值！");

					//營業稅
					dest = new byte[8];
					System.arraycopy(cmDataByteArray, 115, dest, 0, 8);		    
					try{
						businessTax = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + cmSeq + "項項次(" + businessTax+ ")格式錯誤！");
					}
					//if(null == businessTax || 0 == businessTax)
						//throw new ValidationErrorException("第" + cmSeq + "項營業稅為空值！");

					//原報關單號
					dest = new byte[14];
					System.arraycopy(cmDataByteArray, 123, dest, 0, 14);		    
					declNo = new String(dest).trim();
					//if(!StringUtils.hasText(declNo)){
						//throw new ValidationErrorException("第" + cmSeq + "項原報關單號為空值！");

					//原報單項次
					dest = new byte[4];
					System.arraycopy(cmDataByteArray, 137, dest, 0, 4);		    
					try{
						declSeq = Long.parseLong(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + cmSeq + "項項次(" + declSeq+ ")格式錯誤！");
					}
					//if(null == declSeq || 0 == declSeq)
						//throw new ValidationErrorException("第" + cmSeq + "項原報單項次為空值！");

					//菸酒註記
					dest = new byte[1];
					System.arraycopy(cmDataByteArray, 141, dest, 0, 1);		    
					cigarWineRemark = new String(dest).trim();
					//if(!StringUtils.hasText(cigarWineRemark)){
						//throw new ValidationErrorException("第" + cmSeq + "項菸酒註記為空值！");
					//}

					//煙酒數量
					dest = new byte[10];
					System.arraycopy(cmDataByteArray, 143, dest, 0, 10);		    
					try{
						cigarWineQty = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + cmSeq + "項項次(" + cigarWineQty+ ")格式錯誤！");
					}
					//if(null == cigarWineQty || 0 == cigarWineQty)
						//throw new ValidationErrorException("第" + cmSeq + "項煙酒數量為空值！");
					
					//超額徵稅百分比
					dest = new byte[7];
					System.arraycopy(cmDataByteArray, 152, dest, 0, 7);		    
					taxationPcent = NumberUtils.getDouble(new String(dest).trim());
					try{
						taxationPcent = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + cmSeq + "項項次(" + taxationPcent+ ")格式錯誤！");
					}
					//if(null == taxationPcent || 0 == taxationPcent)
						//throw new ValidationErrorException("第" + cmSeq + "項超額徵稅百分比為空值！");

					//原進倉報單銷售數量
					dest = new byte[10];
					System.arraycopy(cmDataByteArray, 159, dest, 0, 10);		    
					try{
						taxUnitQty = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + cmSeq + "項項次(" + taxUnitQty+ ")格式錯誤！");
					}
					//if(null == taxUnitQty || 0 == taxUnitQty)
						//throw new ValidationErrorException("第" + cmSeq + "項原進倉報單銷售數量為空值！");

					//報關單號
					dest = new byte[14];
					System.arraycopy(cmDataByteArray, 229, dest, 0, 14);		    
					ODeclNo = new String(dest).trim();
					//if(!StringUtils.hasText(ODeclNo)){
						//throw new ValidationErrorException("第" + cmSeq + "項報關單號為空值！");

					//報單項次
					dest = new byte[4];
					System.arraycopy(cmDataByteArray, 243, dest, 0, 4);		    
					try{
						ODeclSeq = Long.parseLong(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + cmSeq + "項項次(" + ODeclSeq+ ")格式錯誤！");
					}
					//if(null == ODeclSeq || 0 == ODeclSeq)
						//throw new ValidationErrorException("第" + cmSeq + "項報單項次為空值！");

					//品號
					dest = new byte[15];
					System.arraycopy(cmDataByteArray, 247, dest, 0, 15);	    
					itemCode = new String(dest).trim();
					if(!StringUtils.hasText(itemCode)){
						throw new ValidationErrorException("第" + cmSeq + "項品號為空值！");
					}

					//批號
					dest = new byte[14];
					System.arraycopy(cmDataByteArray, 262, dest, 0, 14);	    
					lotNo = new String(dest).trim();
					if(!StringUtils.hasText(lotNo)){
						throw new ValidationErrorException("第" + cmSeq + "項批號為空值！");
					}
					
					TmpImportPosItem tmpCmImportPosItem = new TmpImportPosItem();
					tmpCmImportPosItem.setTransType(transType);
					tmpCmImportPosItem.setIslandsCode(islandsCode);
					tmpCmImportPosItem.setStoreCode(storeCode);
					tmpCmImportPosItem.setTransactionId(transactionId);
					tmpCmImportPosItem.setBuyerId(buyerId);
					tmpCmImportPosItem.setCustomerPoNo(customerPoNo);
					tmpCmImportPosItem.setSalesUnit(salesUnit);
					tmpCmImportPosItem.setImportTax(importTax);
					tmpCmImportPosItem.setGoodsTax(goodsTax);
					tmpCmImportPosItem.setCigarWineTax(cigarWineTax);
					tmpCmImportPosItem.setHealthTax(healthTax);
					tmpCmImportPosItem.setBusinessTax(businessTax);
					tmpCmImportPosItem.setDeclNo(declNo);
					tmpCmImportPosItem.setDeclSeq(declSeq);
					tmpCmImportPosItem.setCigarWineRemark(cigarWineRemark);
					tmpCmImportPosItem.setCigarWineQty(cigarWineQty);
					tmpCmImportPosItem.setTaxationPcent(taxationPcent);
					tmpCmImportPosItem.setTaxUnitQty(taxUnitQty);
					tmpCmImportPosItem.setOdeclNo(ODeclNo);
					tmpCmImportPosItem.setOdeclSeq(ODeclSeq);
					tmpCmImportPosItem.setLotNo(lotNo);
					cmPosMap.put(cmSeq, tmpCmImportPosItem);
				}
			}

			int seq = 0;
			log.info("transactionResults.size() = " + transactionResults.size());
			for(int i = 0; i < transactionResults.size(); i++){
				posFile = (File)transactionResults.get(i);
				posFileName = posFile.getName().toUpperCase();
				log.info("posFileName = " + posFileName);
				// TW2AD
				fileId = posFileName.substring(1, posFileName.indexOf(".")); // DAAAAAAXXC.REBBBB 即為字串A的部份
				int shiftMove = 0;
				if(posFileName.substring(1,3).equalsIgnoreCase("20")){
				    shiftMove = 2;
				}
				log.info("fileId = " + fileId);
				if(posFileName.indexOf(".") != -1)
					shopCode = posFileName.substring(posFileName.indexOf(".") + 1, posFileName.length());
				transBufReader = new BufferedReader(new FileReader(posFile));

				while ((transactionData = transBufReader.readLine()) != null) {
					++seq;
					transactionDataByteArray = transactionData.getBytes();
					
					log.info("seq = " + seq);
					log.info("Data.getBytes() = " + transactionData.getBytes());
					log.info("transactionDataByteArray = " + new String(transactionDataByteArray));
					
					//銷貨日期
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 13+shiftMove, dest, 0, 10);
					if(new String(dest).trim().length() != 10)
						throw new ValidationErrorException("第" + seq + "項D銷售單日期(" + new String(dest).trim()+ ")格式錯誤！");
					salesOrderDate = DateUtils.parseDate("yyyy/MM/dd",(new String(dest).trim()));
					if(posDate.compareTo(salesOrderDate) != 0)
						throw new ValidationErrorException("第" + seq + "項D銷售單日期(" + salesOrderDate+ ")與檔名的銷售單日期(" + fileId + ")不相符！");

					//項次
					dest = new byte[3];
					System.arraycopy(transactionDataByteArray, 23+shiftMove, dest, 0, 3);
					try{
						indexNo = Long.parseLong(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項項次(" + indexNo+ ")格式錯誤！");
					}

					//品號
					dest = new byte[13];
					System.arraycopy(transactionDataByteArray, 26+shiftMove, dest, 0, 13);	    
					itemCode = new String(dest).trim();
					if(!StringUtils.hasText(itemCode)){
						throw new ValidationErrorException("第" + seq + "項品號(" + itemCode+ ")為空值！");
					}

					//原單價
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 39+shiftMove, dest, 0, 10);    
					try{
						originalUnitPrice = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項原單價(" + originalUnitPrice+ ")格式錯誤！");
					}

					//數量
					dest = new byte[8];
					System.arraycopy(transactionDataByteArray, 49+shiftMove, dest, 0, 8);    
					try{
						quantity = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項數量(" + quantity+ ")格式錯誤！");
					}

					//折扣率
					dest = new byte[3];
					System.arraycopy(transactionDataByteArray, 57+shiftMove, dest, 0, 3); 
					try{
						discountRate = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項折扣率(" + discountRate+ ")格式錯誤！");
					}

					//實售價
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 60+shiftMove, dest, 0, 10);    
					try{
						actualSalesAmount = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項實售價(" + actualSalesAmount+ ")格式錯誤！");
					}

					//折抵價
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 70+shiftMove, dest, 0, 10);
					try{
						discountAmount = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項折抵價(" + discountAmount+ ")格式錯誤！");
					}

					//營業員
					dest = new byte[8];
					System.arraycopy(transactionDataByteArray, 80+shiftMove, dest, 0, 8);
					salesPerson = new String(dest).trim();

					//會員卡號
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
					
					//時段
					dest = new byte[2];
					System.arraycopy(transactionDataByteArray, 108+shiftMove, dest, 0, 2);	    
					transactionTime = new String(dest).trim();

					//國籍
					dest = new byte[20];
					System.arraycopy(transactionDataByteArray, 150+shiftMove, dest, 0, 20);	    
					countryCode = new String(dest).trim();
					if(StringUtils.hasText(countryCode) && countryCode.length() > 2){
						countryCode = countryCode.substring(0, 2);
					}

					//離境日
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 170+shiftMove, dest, 0, 10);
					if(new String(dest).trim().length() != 10){
						throw new ValidationErrorException("第" + seq + "項離境日日期(" + new String(dest).trim()+ ")格式錯誤！");
					}
					departureDate = DateUtils.parseDate("yyyy/MM/dd",(new String(dest).trim()));

					//交易序號
					dest = new byte[8];
					System.arraycopy(transactionDataByteArray, 180+shiftMove, dest, 0, 8);	    
					transactionSeqNo = new String(dest).trim();
					if(!StringUtils.hasText(transactionSeqNo)){
						throw new ValidationErrorException("第" + seq + "項交易序號為空值！");
					}
					transactionSeqs.add(transactionSeqNo);

					//VIP 卡別
					dest = new byte[2];
					System.arraycopy(transactionDataByteArray, 188+shiftMove, dest, 0, 2);	    
					vipTypeCode = new String(dest).trim();

					//訂價
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 190+shiftMove, dest, 0, 10);	    
					try{
						standardPrice = Double.parseDouble(new String(dest).trim());
					}catch(NumberFormatException nfe){
						throw new ValidationErrorException("第" + seq + "項訂價(" + standardPrice+ ")格式錯誤！");
					}

					//提貨單
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
					if(StringUtils.hasText(new String(dest).trim())){
						try{
							usedDiscountRate = Double.parseDouble(new String(dest).trim());
						}catch(NumberFormatException nfe){
							throw new ValidationErrorException("第" + seq + "項使用折扣率(" + usedDiscountRate+ ")格式錯誤！");
						}
					}
					//商品類型
					dest = new byte[10];
					System.arraycopy(transactionDataByteArray, 281+shiftMove, dest, 0, 10);	    
					itemDiscountType = new String(dest).trim();

					TmpImportPosItemId tmpImportPosItemId = new TmpImportPosItemId();
					TmpImportPosItem tmpImportPosItem = new TmpImportPosItem();
					tmpImportPosItemId.setFileId(fileId);
					tmpImportPosItemId.setSalesOrderDate(salesOrderDate);
					tmpImportPosItemId.setPosMachineCode(posMachineCode);
					tmpImportPosItemId.setTransactionSeqNo(transactionSeqNo);
					tmpImportPosItemId.setSeq(indexNo);
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
					//tmpImportPosItem.setCustomerPoNo(customerPoNo);
					tmpImportPosItem.setPeriod(transactionTime);
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
					
					//A的檔名Mapping到D
					TmpImportPosItem cm = (TmpImportPosItem)cmPosMap.get(seq);
					if(null == cm)
						throw new Exception ("交易項次" + indexNo + "號查無對應之報單資訊");
					tmpImportPosItem.setTransType(cm.getTransType());
					tmpImportPosItem.setIslandsCode(cm.getIslandsCode());
					tmpImportPosItem.setStoreCode(cm.getStoreCode());
					tmpImportPosItem.setTransactionId(cm.getTransactionId());
					tmpImportPosItem.setBuyerId(cm.getBuyerId());
					tmpImportPosItem.setCustomerPoNo(cm.getCustomerPoNo());
					tmpImportPosItem.setSalesUnit(cm.getSalesUnit());
					tmpImportPosItem.setImportTax(cm.getImportTax());
					tmpImportPosItem.setGoodsTax(cm.getGoodsTax());
					tmpImportPosItem.setCigarWineTax(cm.getCigarWineTax());
					tmpImportPosItem.setHealthTax(cm.getHealthTax());
					tmpImportPosItem.setBusinessTax(cm.getBusinessTax());
					tmpImportPosItem.setDeclNo(cm.getDeclNo());
					tmpImportPosItem.setDeclSeq(cm.getDeclSeq());
					tmpImportPosItem.setCigarWineRemark(cm.getCigarWineRemark());
					tmpImportPosItem.setCigarWineQty(cm.getCigarWineQty());
					tmpImportPosItem.setTaxationPcent(cm.getTaxationPcent());
					tmpImportPosItem.setTaxUnitQty(cm.getTaxUnitQty());
					tmpImportPosItem.setOdeclNo(cm.getOdeclNo());
					tmpImportPosItem.setOdeclSeq(cm.getOdeclSeq());
					tmpImportPosItem.setLotNo(cm.getLotNo());
					tmpImportPosItems.add(tmpImportPosItem);
				}
			}
			return new Object[]{transactionSeqs, tmpImportPosItems, customerPoNos, salesOrderDates};
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
		log.info("=========================parsingPaymentFiles==================================");
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
				fileId = posFileName.substring(1, posFileName.indexOf(".")); // DAAAAAAXXC.REBBBB 即為字串A的部份
				if(posFileName.indexOf(".") != -1){
					shopCode = posFileName.substring(posFileName.indexOf(".") + 1, posFileName.length());
				}	    	
				bufReader = new BufferedReader(new FileReader(posFile));
				int seq = 0;
				while ((paymentData = bufReader.readLine()) != null) {
					++seq;
					paymentByteArray = paymentData.getBytes();
					
					log.info("seq = " + seq);
					log.info("paymentData.getBytes() = " + paymentData.getBytes());
					log.info("paymentByteArray = " + new String(paymentByteArray));
					
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
			String brandCode, String fileKey, HashMap parameterMap, List<TmpImportPosItem> tmpImportPosItems) throws ValidationErrorException, Exception{
		log.info("=========================produceSoBeans==================================");
		List entityBeans = new ArrayList();
		Iterator it = transactionSeqs.iterator();
		Date currentDateTime = new Date();
		int indexNo = 0;
		while(it.hasNext()){
			String transactionSeq = (String)it.next();
			if(tmpImportPosItems == null || tmpImportPosItems.size() == 0){
				throw new ValidationErrorException("依據POS機碼(" + posMachineCode + ")、交易日期(" + transactionDate + ")、交易序號(" + transactionSeq + ")查無交易明細資料！");
			}
			
			List<TmpImportPosItem> tmpItems = new ArrayList<TmpImportPosItem>(0);
			TmpImportPosItem tmpImportPosItem = tmpImportPosItems.get(indexNo);
			while(transactionSeq.equals(tmpImportPosItem.getId().getTransactionSeqNo())){
				tmpItems.add(tmpImportPosItem);
				indexNo++;
				if(indexNo == tmpImportPosItems.size())
					break;
				tmpImportPosItem = tmpImportPosItems.get(indexNo);
			}
			
			tmpImportPosItem = tmpItems.get(0);
			
			List tmpImportPosPayments = tmpImportPosPaymentDAO.findbyIdentification(posDate, posMachineCode, transactionSeq);
			if(tmpImportPosPayments == null || tmpImportPosPayments.size() == 0){
				throw new ValidationErrorException("依據POS機碼(" + posMachineCode + ")、交易日期(" + transactionDate + ")、交易序號(" + transactionSeq + ")查無付款明細資料！");
			}
			
			log.info("=========================new SoSalesOrderHead()==================================");
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
			//salesOrderHead.setStatus(OrderStatus.UNCONFIRMED);
			salesOrderHead.setReserve3(fileKey);
			salesOrderHead.setReserve5("POS");
			salesOrderHead.setCreationDate(currentDateTime);
			salesOrderHead.setLastUpdateDate(currentDateTime);
			salesOrderHead.setCustomerPoNo(tmpImportPosItem.getCustomerPoNo());
			salesOrderHead.setIslandsCode(tmpImportPosItem.getIslandsCode());
			salesOrderHead.setStoreCode(tmpImportPosItem.getStoreCode());
			salesOrderHead.setTransactionId(tmpImportPosItem.getTransactionId());
			salesOrderHead.setBuyerId(tmpImportPosItem.getBuyerId());
			//產生銷貨交易明細檔
			produceSoItem(salesOrderHead, tmpItems, parameterMap);
			//產生銷貨付款明細檔
			produceSoPayment(salesOrderHead, tmpImportPosPayments);
			//String errorMsg =  XFireUtils.setTransactionData(salesOrderHead);
			entityBeans.add(salesOrderHead);
		}
		return entityBeans;
	}

	private void produceSoItem(SoSalesOrderHead salesOrderHead, List tmpImportPosItems, HashMap parameterMap) throws ValidationErrorException, Exception{
		log.info("=========================produceSoItem==================================");
		List<SoSalesOrderItem> soSalesOrderItems = new ArrayList(0);
		String brandCode = salesOrderHead.getBrandCode();
		String posMachineCode = salesOrderHead.getPosMachineCode();
		String actualShopCode = null;
		String defaultWarehouseCode = null;
		String superintendentCode = null;
		String customerCode = null;
		String customerPoNo = null;
		String headFileName = null;
		String countryCode = "TW";
		String period = null;
		String passportNo = null;
		String flightNo = null;
		Date departureDate = null;
		String ladingNo = null;
		Double totalOriginalSalesAmt = 0D;
		Double totalActualSalesAmt = 0D;
		String orderDiscountType = "A"; //無折扣
		String transType = null;
		log.info("tmpImportPosItems.size() = " + tmpImportPosItems.size());
		if(tmpImportPosItems != null && tmpImportPosItems.size() > 0){
			for(int i = 0; i < tmpImportPosItems.size(); i++){
				TmpImportPosItem importPosItem = (TmpImportPosItem)tmpImportPosItems.get(i);
				log.info("=========================get tmpImportPosItems==================================");
				if(actualShopCode == null){
					log.info("brandCode = " + brandCode);
					log.info("posMachineCode = " + posMachineCode);
					BuShop newBuShop = buShopMachineDAO.getShopCodeByMachineCode(brandCode, posMachineCode, null, null);
					if(newBuShop != null){
						log.info("newBuShop != null");
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
				log.info("=========================start==================================");
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
				//傳輸類別
				if(transType == null){
					transType = importPosItem.getTransType();
				}
				log.info("=========================start produceSoItem==================================");
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
				/*ImItem imItemPO = imItemDAO.findItem(brandCode, itemCode);
		if(imItemPO == null){
		    throw new ValidationErrorException("查無品號(" + itemCode + ")的相關資料！");
		}
		isTax = imItemPO.getIsTax();
		if(!StringUtils.hasText(isTax)){
		    throw new ValidationErrorException("品號(" + itemCode + ")的稅別為空值！");
		}*/		
				String fileName = importPosItem.getFileName();
				
				totalOriginalSalesAmt += originalSalesAmt;
				totalActualSalesAmt += actualSalesAmt;
				log.info("=========================new SoSalesOrderItem==================================");
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
				salesOrderItem.setSalesUnit(importPosItem.getSalesUnit());
				salesOrderItem.setImportTax(importPosItem.getImportTax());
				salesOrderItem.setGoodsTax(importPosItem.getGoodsTax());
				salesOrderItem.setCigarWineTax(importPosItem.getCigarWineTax());
				salesOrderItem.setHealthTax(importPosItem.getHealthTax());
				salesOrderItem.setBusinessTax(importPosItem.getBusinessTax());
				salesOrderItem.setCigarWineRemark(importPosItem.getCigarWineRemark());
				salesOrderItem.setCigarWineQty(importPosItem.getCigarWineQty());
				salesOrderItem.setTaxationPcent(importPosItem.getTaxationPcent());
				salesOrderItem.setTaxUnitQty(importPosItem.getTaxUnitQty());
				salesOrderItem.setImportDeclNo(importPosItem.getDeclNo());
				salesOrderItem.setImportDeclSeq(importPosItem.getDeclSeq());
				salesOrderItem.setOImportDeclNo(importPosItem.getOdeclNo());
				salesOrderItem.setOImportDeclSeq(importPosItem.getOdeclSeq());
				salesOrderItem.setLotNo(SystemConfig.LOT_NO);
				//salesOrderItem.setAllowWholeSale("Y");
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
			if("I".equals(transType))
				salesOrderHead.setStatus(OrderStatus.UNCONFIRMED);
			else
				salesOrderHead.setStatus(OrderStatus.VOID);
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
				SoSalesOrderPayment salesOrderPayment = new SoSalesOrderPayment();
				salesOrderPayment.setPosPaymentType(posPaymentType);
				salesOrderPayment.setLocalCurrencyCode("NTD");
				salesOrderPayment.setLocalAmount(localAmount);
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
					try{
						conditionMap.put("searchKey", salesHeadId);
						//appExtendItemInfoService.executeExtendItemProcessor(conditionMap);正式上線使用
						//===========目前取小花報單訊息=========
						//appExtendItemInfoService.executeSoItemInfoFromXFire(conditionMap);
						//重新計算單身、檢核單身資訊是否完整、扣庫存轉出貨單明細
						List assemblyMsg = soSalesOrderMainService.updateT2PosData(processName, executeDate, uuid, salesHeadId, parameterMap, mailContents);
						//如果是VOID的就不扣庫存
						if(OrderStatus.VOID.equals(salesOrderHead.getStatus()))
							continue;
						
						//for 儲位用
						if(imStorageAction.isStorageExecute(salesOrderHead)){
						    //取單號後，扣庫存前，執行更新儲位單頭與單身，比對單據明細與儲位明細
						    executeStorage(salesOrderHead);
						}
						
						if(assemblyMsg != null && assemblyMsg.size() == 0){
							soSalesOrderMainService.updateT2PosOnHand(processName, executeDate, uuid, salesHeadId, parameterMap, mailContents, assemblyMsg);
						}
						
						
					}catch(Exception ex){
						errorMsg = "更新" + identification + "銷售資料時發生錯誤，原因：";
						log.error(errorMsg + ex.toString());
						SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg + ex.getMessage(), executeDate, uuid, opUser);		        		       
						siProgramLogAction.createProgramLog(SoSalesOrderMainService.PROGRAM_ID, MessageStatus.LOG_ERROR, identification, errorMsg + ex.getMessage(), opUser);
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
	}

	private void moveFilesToDestination(File destination, List transactionResults, List paymentResults, List cmResults){

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
			
			for(int j = 0; j < cmResults.size(); j++){
				try{
					File cmFile = (File)cmResults.get(j);
					FileUtils.copyFileToDirectory(cmFile, destination);
					cmFile.delete();
				}catch(Exception ex){
					log.error("搬移並刪除交易明細檔案失敗，原因：" + ex.toString());  
				}
			}
		}catch(Exception ex){
			log.error("搬移並刪除檔案失敗，原因：" + ex.toString());  
		}	
	}

	public void parsingLadFile(List results, HashMap uiProperties) throws ValidationErrorException, Exception {

		String errorMsg = null;
		String loginUser = "SYS";
		String brandCode = "T2";
		User userObj = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);    
		if (userObj != null) {
			loginUser = userObj.getEmployeeCode();
			brandCode = userObj.getBrandCode();
		}
		String processName = (String)uiProperties.get(ImportDBService.PRCCESS_NAME);
		log.info("processName = " + processName);
		Date executeDate = (Date)uiProperties.get("actualExecuteDate");
		String uuid = (String)uiProperties.get("uuidCode");
		String successFilePath = (String) uiProperties.get(ImportDBService.SUCCESS_FILE_PATH);
		log.info("successFilePath = " + successFilePath);
		String failFilePath = (String) uiProperties.get(ImportDBService.FAIL_FILE_PATH);
		log.info("failFilePath = " + failFilePath);
		File successFile = new File(successFilePath);
		File failFile = new File(failFilePath);
		try{
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行提貨單轉檔程序...", executeDate, uuid, loginUser);
			parsingLadFiles(results, loginUser);
			moveFilesToDestination(successFile, results);
		}catch(Exception ex){
			errorMsg = "執行提貨單轉檔發生錯誤；";
			log.error(errorMsg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg + ex.getMessage(), executeDate, uuid, loginUser);
			//========================搬移至轉檔失敗資料夾=========================
			moveFilesToDestination(failFile, results);
		}finally{
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "提貨單轉檔程序結束...", executeDate, uuid, loginUser);
		}
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
	private void parsingLadFiles(List results, String loginUser) throws Exception {
		log.info("parsingLadFiles");
		String errorMsg = null;
		File file = null;
		BufferedReader bufReader = null;
		String data = null;
		byte[] dataByteArray = null;
		byte[] dest = null;
		
		String transType = null; //傳輸類別
		String islandsCode = null; //離島別
		String storecode = null; //免稅店別
		String transactionId = null; //交易代碼
		String buyerId = null; //購物者
		String salesOrderNo = null; //銷售單單號
		Date departureDate = null; //離境日
		String flightNo = null; //航機班次或船機票號
		
		try{
			for(int i = 0; i < results.size(); i++){
				file = (File)results.get(i);
				bufReader = new BufferedReader(new FileReader(file));

				int seq = 0;
				while ((data = bufReader.readLine()) != null) {
					++seq;
					dataByteArray = data.getBytes();

					//傳輸類別
					dest = new byte[1];
					System.arraycopy(dataByteArray, 0, dest, 0, 1);	    
					transType = new String(dest).trim();
					if(!StringUtils.hasText(transType)){
						throw new ValidationErrorException("第" + seq + "項傳輸類別為空值！");
					}
					
					//離島別代號
					dest = new byte[1];
					System.arraycopy(dataByteArray, 1, dest, 0, 1);	    
					islandsCode = new String(dest).trim();
					if(!StringUtils.hasText(islandsCode)){
						throw new ValidationErrorException("第" + seq + "項離島別代號為空值！");
					}

					//免稅店代號
					dest = new byte[1];
					System.arraycopy(dataByteArray, 2, dest, 0, 1);	    
					storecode = new String(dest).trim();
					if(!StringUtils.hasText(storecode)){
						throw new ValidationErrorException("第" + seq + "項免稅店代號為空值！");
					}
					
					//交易碼
					dest = new byte[14];
					System.arraycopy(dataByteArray, 3, dest, 0, 14);	    
					transactionId = new String(dest).trim();
					if(!StringUtils.hasText(transactionId)){
						throw new ValidationErrorException("第" + seq + "項交易碼為空值！");
					}

					//購物者
					dest = new byte[14];
					System.arraycopy(dataByteArray, 17, dest, 0, 14);	    
					buyerId = new String(dest).trim();
					if(!StringUtils.hasText(buyerId)){
						throw new ValidationErrorException("第" + seq + "項購物者為空值！");
					}
					
					//銷售單單號
					dest = new byte[14];
					System.arraycopy(dataByteArray, 31, dest, 0, 14);	    
					salesOrderNo = new String(dest).trim();
					if(!StringUtils.hasText(salesOrderNo)){
						throw new ValidationErrorException("第" + seq + "項銷售單單號為空值！");
					}
					
					//離境日
					dest = new byte[10];
					System.arraycopy(dataByteArray, 45, dest, 0, 10);
					if(new String(dest).trim().length() != 10){
						throw new ValidationErrorException("第" + seq + "項離境日日期(" + new String(dest).trim()+ ")格式錯誤！");
					}
					departureDate = DateUtils.parseDate("yyyy/MM/dd",(new String(dest).trim()));
					
					//航機班次或船機票號
					dest = new byte[13];
					System.arraycopy(dataByteArray, 55, dest, 0, 13);	    
					flightNo = new String(dest).trim();
					if(!StringUtils.hasText(flightNo)){
						throw new ValidationErrorException("第" + seq + "項航機班次或船機票號為空值！");
					}
					
					log.info("create PICK");
					//如果有重複的就作更新
					if("I".equals(transType)){
						List<ImPicking> picks = imPickingDAO.findByProperty("ImPicking", "transactionId", transactionId);
						if(null != picks &&picks.size() > 0){
							ImPicking pick = picks.get(0);
							pick.setIslandsCode(islandsCode);
							pick.setStorecode(storecode);
							pick.setTransactionId(transactionId);
							pick.setBuyerId(buyerId);
							pick.setSalesOrderNo(salesOrderNo);
							pick.setDepartureDate(departureDate);
							pick.setFlightNo(flightNo);
							pick.setLastUpdatedBy(loginUser);
							pick.setLastUpdateDate(new Date());
							imPickingDAO.update(pick);
						}else{
							ImPicking pick = new ImPicking();
							pick.setIslandsCode(islandsCode);
							pick.setStorecode(storecode);
							pick.setTransactionId(transactionId);
							pick.setBuyerId(buyerId);
							pick.setSalesOrderNo(salesOrderNo);
							pick.setDepartureDate(departureDate);
							pick.setFlightNo(flightNo);
							pick.setCreatedBy(loginUser);
							pick.setCreationDate(new Date());
							pick.setLastUpdatedBy(loginUser);
							pick.setLastUpdateDate(new Date());
							imPickingDAO.save(pick);
						}
						log.info("create PICK over");
					}
				}
			} 
		}catch(Exception ex){
			errorMsg = "解析提貨單失敗，原因：";
			log.error(errorMsg + ex.toString());
			throw new Exception(errorMsg + ex.getMessage());
		}
	}
	
	private void moveFilesToDestination(File destination, List results){
		try{
			for(int i = 0; i < results.size(); i++){
				try{
					File file = (File)results.get(i);
					FileUtils.copyFileToDirectory(file, destination);
					file.delete();
				}catch(Exception ex){
					log.error("搬移並刪除明細檔案失敗，原因：" + ex.toString());  
				}
			}
		}catch(Exception ex){
			log.error("搬移並刪除檔案失敗，原因：" + ex.toString());  
		}	
	}
	
	public void parsingVoidFile(List cmResults, String fileKey, Date posDate, String posMachineCode, HashMap uiProperties)
	throws ValidationErrorException, Exception {
		log.info("=========================parsingVoidFile==================================");
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
		String uuid = (String)uiProperties.get("uuidCode");
		String successFilePath = (String) uiProperties.get(ImportDBService.SUCCESS_FILE_PATH);
		String failFilePath = (String) uiProperties.get(ImportDBService.FAIL_FILE_PATH);
		File successFile = new File(successFilePath);
		File failFile = new File(failFilePath);
		String transactionDate = null;
		try{
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
			Object[] obj = parsingTransactionFiles(null, cmResults, fileKey, posDate, posMachineCode, loginUser);
			LinkedHashSet customerPoNos = (LinkedHashSet)obj[2]; 
			LinkedHashSet salesOrderDates = (LinkedHashSet)obj[3];
			
			//==========POS作廢相關銷售、出貨資料==========
			log.info("=========================saveT2Pos2Void==================================");
			soSalesOrderMainService.saveT2Pos2Void(customerPoNos, salesOrderDates, loginUser);     
			//========================搬移至轉檔成功資料夾=========================
			moveFilesToDestination(successFile, cmResults);
		}catch(Exception ex){
			errorMsg = "執行POS機碼(" + posMachineCode + ")、交易日期(" + transactionDate + ")轉檔發生錯誤；";
			log.error(errorMsg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg + ex.getMessage(), executeDate, uuid, loginUser);
			//========================搬移至轉檔失敗資料夾=========================
			moveFilesToDestination(failFile, cmResults);
		}finally{
			log.info("完成Finally");
			try{
			}catch(Exception ex){
				errorMsg = ex.toString();
				log.error(errorMsg);
				SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser);
			}
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "POS機碼(" + posMachineCode + ")、交易日期(" + transactionDate + ")轉檔程序結束...", executeDate, uuid, loginUser);
		}
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