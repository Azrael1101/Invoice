package tw.com.tm.erp.importdb;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.WfApprovalResult;
import tw.com.tm.erp.hbm.dao.BuLocationDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.hbm.service.SiSystemLogService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.OldSysMapNewSys;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.DateUtils;

/**
 * POS 資料匯入 TODO : 要判斷是否已經關帳,是否重匯
 *
 * @author T02049
 *
 */
public class POSMOVImportData implements ImportDataAbs {

    private static final Log log = LogFactory.getLog(POSMOVImportData.class);

    private static final String split[] = { "," };

    private static ApplicationContext context = SpringUtils.getApplicationContext();

    public ImportInfo initial(HashMap uiProperties) {
	log.info("POSMOVImportData.initial");
	ImportInfo imInfo = new ImportInfo();

	// set entity class name
	imInfo
		.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImMovementHead.class
			.getName());
	imInfo.setSplit(split);

	// set key info
	imInfo.setKeyIndex(0);
	imInfo.setKeyValue("M0");
	imInfo.setSaveKeyField(true);

	imInfo.addFieldName("M0");
	imInfo.addFieldName("originalOrderNo");
	imInfo.addFieldName("originalOrderTypeCode");
	imInfo.addFieldName("deliveryWarehouseCode");
	imInfo.addFieldName("arrivalWarehouseCode");
	imInfo.addFieldName("deliveryDate");
	imInfo.addFieldName("remark1");
	imInfo.addFieldName("reserve3");
	imInfo.addFieldName("reserve4");

	// set field type
	imInfo.setFieldType("M0", "java.lang.String");
	imInfo.setFieldType("originalOrderNo", "java.lang.String");
	imInfo.setFieldType("originalOrderTypeCode", "java.lang.String");
	imInfo.setFieldType("deliveryWarehouseCode", "java.lang.String");
	imInfo.setFieldType("arrivalWarehouseCode", "java.lang.String");
	imInfo.setFieldType("deliveryDate", "java.util.Date");
	imInfo.setFieldType("remark1", "java.lang.String");
	imInfo.setFieldType("reserve3", "java.lang.String");
	imInfo.setFieldType("reserve4", "java.lang.String");

	imInfo.setFieldTypeFormat("java.util.Date", "yyyyMMdd");

	imInfo.addDetailImportInfos(getImMovementItem());

	return imInfo;
    }

    /**
     * im item price detail config
     *
     * @return
     */
    private ImportInfo getImMovementItem() {
	log.info("POSMOVImportData.getImMovementItem");
	ImportInfo imInfo = new ImportInfo();

	imInfo.setKeyIndex(0);
	imInfo.setKeyValue("M1");
	imInfo.setSaveKeyField(true);
	imInfo
		.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImMovementItem.class
			.getName());

	imInfo.addFieldName("M1");
	imInfo.addFieldName("itemCode");
	imInfo.addFieldName("deliveryQuantity");

	imInfo.setFieldType("M1", "java.lang.String");
	imInfo.setFieldType("itemCode", "java.lang.String");
	imInfo.setFieldType("deliveryQuantity", "java.lang.Double");

	return imInfo;
    }

    public String updateDB(List entityBeans, ImportInfo info) throws Exception {

	log.info("POSMOVImportData.updateDB");
	StringBuffer reMsg = new StringBuffer();
	HashMap uiProperties = info.getUiProperties();
	//=================記錄system log使用===================
	String processName = (String)uiProperties.get(ImportDBService.PRCCESS_NAME);
	Date executeDate = (Date)uiProperties.get("actualExecuteDate");
	String uuid = (String)uiProperties.get("uuidCode");
	//=================上傳相關檔案路徑=======================
	String headFileName = (String)uiProperties.get(ImportDBService.UPLOAD_HEAD_FILE_NAME);
	String lineFileName = (String)uiProperties.get(ImportDBService.UPLOAD_LINE_FILE_NAME);
	String headFilePath = (String)uiProperties.get(ImportDBService.UPLOAD_HEAD_FILE_PATH);
	String lineFilePath = (String)uiProperties.get(ImportDBService.UPLOAD_LINE_FILE_PATH);
	String combineFilePath = (String)uiProperties.get(ImportDBService.COMBINE_FILE_PATH);
	String baseFilePath = (String)uiProperties.get(ImportDBService.BASE_FILE_PATH);
	String successFilePath = (String)uiProperties.get(ImportDBService.SUCCESS_FILE_PATH);
	File headFile = new File(headFilePath);
	File lineFile = new File(lineFilePath);
	File combineFile = new File(combineFilePath);
	File baseFile = new File(baseFilePath);
	File successFile = new File(successFilePath);
	//=====================================================
	SiSystemLogService siSystemLogService = (SiSystemLogService) SpringUtils.getApplicationContext().getBean("siSystemLogService");
	String employeeCode = null;
	String errorMsg = null;
	try {
	    User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
	    employeeCode = user.getEmployeeCode();
	    String defaultLotNo = null;
	    String systemAdmin = null;
	    String brandCode = null;
	    String deliveryWarehouseCode = null;
	    String origiOrderTypeCode = null;
	    String origiOrderNo = null;
	    List assembly = new ArrayList(0);

	    ImMovementService imMovementService = (ImMovementService) context.getBean("imMovementService");
	    ImWarehouseDAO imWarehouseDAO = (ImWarehouseDAO) context.getBean("imWarehouseDAO");
	    BuLocationDAO buLocationDAO = (BuLocationDAO) context.getBean("buLocationDAO");
	    BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService)context.getBean("buCommonPhraseService");
	    WfApprovalResultService wfApprovalResultService = (WfApprovalResultService)context.getBean("wfApprovalResultService");
	    defaultLotNo = buCommonPhraseService.getLineName("SystemConfig", "DefaultLotNo");
	    if(!StringUtils.hasText(defaultLotNo)){
	        throw new ValidationErrorException("系統預設批號為空值！");
	    }else if("unknow".equals(defaultLotNo)){
		throw new ValidationErrorException("查詢系統預設批號失敗！");
	    }
	    systemAdmin = buCommonPhraseService.getLineName("SystemConfig", "SystemAdmin");
	    if(!StringUtils.hasText(systemAdmin)){
	        throw new ValidationErrorException("系統管理人員為空值！");
	    }else if("unknow".equals(systemAdmin)){
		throw new ValidationErrorException("查詢系統管理人員失敗！");
	    }

	    for (int index = 0; index < entityBeans.size(); index++) {
		try {
		    ImMovementHead entityBean = (ImMovementHead) entityBeans
			    .get(index);
		    Object[] objArray = doValidate(entityBean);
		    deliveryWarehouseCode = (String)objArray[0];
		    origiOrderTypeCode = (String)objArray[1];
		    origiOrderNo = (String)objArray[2];
                    //取得對應的的轉出倉庫代碼及其品牌代碼
		    String actualDeliveryWarehouseCode = OldSysMapNewSys.getNewWarehouse(deliveryWarehouseCode);
		    if(actualDeliveryWarehouseCode == null){
			throw new ValidationErrorException(deliveryWarehouseCode + "查無對應的轉出倉庫代碼！");
		    }else{
			ImWarehouse deliveryWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, actualDeliveryWarehouseCode);
		        if (deliveryWarehouse == null) {
		            throw new ValidationErrorException("轉出倉庫代碼：" + actualDeliveryWarehouseCode + "不存在！");
			}else{
			    //將轉出倉庫的資訊放置於bean
			    entityBean.setDeliveryWarehouseCode(actualDeliveryWarehouseCode);
			    String deliveryWarehouseManager = deliveryWarehouse.getWarehouseManager();
			    if (!StringUtils.hasText(deliveryWarehouseManager)) {
		                throw new ValidationErrorException("轉出倉庫代碼：" + actualDeliveryWarehouseCode + "的倉管人員為空值！");
			    }else{
				if ("unknow".equals( UserUtils.getLoginNameByEmployeeCode(deliveryWarehouseManager)))
				    throw new ValidationErrorException("出庫倉管人員(" + deliveryWarehouseManager + ")錯誤！");
				else
				    entityBean.setDeliveryContactPerson(deliveryWarehouseManager);
			    }
			    brandCode = deliveryWarehouse.getBrandCode();
			    entityBean.setBrandCode(brandCode);

			    Long deliveryLocationId = deliveryWarehouse.getLocationId();
			    if(deliveryLocationId != null){
				BuLocation deliveryLocation = (BuLocation) buLocationDAO
					.findByPrimaryKey(BuLocation.class, deliveryLocationId);
	                        if(deliveryLocation != null){
				    entityBean.setDeliveryAddress(deliveryLocation.getAddress());
				    entityBean.setDeliveryArea(deliveryLocation.getArea());
				    entityBean.setDeliveryCity(deliveryLocation.getCity());
				    entityBean.setDeliveryZipCode( deliveryLocation.getZipCode());
	                        }
			    }
			}
		    }
		    //檢核POS單別必須為TR、TT、TB才能匯入
		    String[] orderTypeCodeArray = new String[]{"TB", "TR", "TT"};
		    Arrays.sort(orderTypeCodeArray);
		    if(Arrays.binarySearch(orderTypeCodeArray, origiOrderTypeCode) < 0){
			 throw new ValidationErrorException("匯入的單別錯誤！");
		    }

                    //TB、TR及TT單以POS單別及POS單號查詢
		    ImMovementHead movementHeadPO = null;
		    movementHeadPO = imMovementService.findPOSMovementByIdentification(brandCode, origiOrderTypeCode, origiOrderNo);
		    if("TR".equals(origiOrderTypeCode) && movementHeadPO == null){
		        throw new NoSuchObjectException("查無對應的TR調撥單！");
		    }else if("TB".equals(origiOrderTypeCode) && movementHeadPO != null){
			throw new ValidationErrorException("資料已存在，請勿重複匯入！");
		    }

		    //設值、調撥存檔、庫存更新
		    if(movementHeadPO != null){
			if(OrderStatus.WAIT_IN.equals(movementHeadPO.getStatus())){
			    if("R".equalsIgnoreCase(entityBean.getReserve3())){
	                        setData(movementHeadPO, entityBean , employeeCode, systemAdmin);
	                        Object[] resultArray = imMovementService.createOrUpdatePOSMovement(movementHeadPO);
				//Long headId = (Long)resultArray[0];
				List errorMsgs = (List)resultArray[1];
	                        if(errorMsgs != null && errorMsgs.size() > 0){
	                            throw new ValidationErrorException("更新調撥單資訊及庫存失敗！");
	                        }
			    }else{
				throw new ValidationErrorException("單別單號已存在，匯入的檔案必須為簽回檔！");
			    }
			}else{
			    //assembly.add("POS調撥單別：" + origiOrderTypeCode + "、單號：" + origiOrderNo + "的單據狀態不為" + OrderStatus.getChineseWord(OrderStatus.WAIT_IN) + "，無法執行匯入！<br>");
			    throw new ValidationErrorException("單據狀態不為" + OrderStatus.getChineseWord(OrderStatus.WAIT_IN) + "，無法執行匯入！");
			}
		    }else{
			if("M".equalsIgnoreCase(entityBean.getReserve3())){
			    setData(entityBean, defaultLotNo , employeeCode, systemAdmin);
			    Object[] resultArray = imMovementService.createOrUpdatePOSMovement(entityBean);
			    Long headId = (Long)resultArray[0];
			    List errorMsgs = (List)resultArray[1];
			    if(errorMsgs != null && errorMsgs.size() > 0){
	                        throw new ValidationErrorException("新增調撥單並更新庫存失敗！");
			    }else{
				if("TB".equals(origiOrderTypeCode)){
				    try{
			                Object[] processInfo = ImMovementService.startProcess(entityBean);
					logApprovalResult(processInfo, entityBean, wfApprovalResultService);
				    }catch(Exception ex){
					log.error("POS調撥單別：" + origiOrderTypeCode + "、單號：" + origiOrderNo + "匯入及扣庫存成功，但啟動流程並記錄歷程時發生錯誤，原因：" + ex.toString());
					assembly.add("POS調撥單別：" + origiOrderTypeCode + "、單號：" + origiOrderNo + "匯入及扣庫存成功，但啟動流程並記錄歷程時發生錯誤，原因：" + ex.getMessage() + "<br>");
					siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, "POS調撥單別：" + origiOrderTypeCode + "、單號：" + origiOrderNo + "匯入及扣庫存成功，但啟動流程並記錄歷程時發生錯誤！"
						, executeDate, uuid, employeeCode);
			            }
				}else{
				    ImMovementHead currentMovementHead = null;
				    try{
				        currentMovementHead = imMovementService.findById(headId);
				        if(currentMovementHead != null){
				            currentMovementHead.setStatus(OrderStatus.WAIT_IN);
				            imMovementService.update(currentMovementHead);
				        }else{
					    throw new Exception("依據主鍵(" + headId + ")查詢調撥單失敗！");
				        }
				    }catch(Exception ex){
				        log.error("POS調撥單別：" + origiOrderTypeCode + "、單號：" + origiOrderNo + "匯入及扣庫存成功，但更新狀態時發生錯誤，原因：" + ex.toString());
				        assembly.add("POS調撥單別：" + origiOrderTypeCode + "、單號：" + origiOrderNo + "匯入及扣庫存成功，但更新狀態時發生錯誤，原因：" + ex.getMessage() + "<br>");
				        siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, "POS調撥單別：" + origiOrderTypeCode + "、單號：" + origiOrderNo + "匯入及扣庫存成功，但更新狀態時發生錯誤！"
				            , executeDate, uuid, employeeCode);
				    }
				}
			    }
			}else{
			    throw new ValidationErrorException("單別單號不存在，匯入的檔案必須為移轉檔！");
			}
		    }
		} catch (Exception ex) {
		    errorMsg = "POS調撥單別：" + origiOrderTypeCode + "、單號：" + origiOrderNo + "的調撥資料匯入失敗！原因：";
		    log.error(errorMsg + ex.toString());
		    assembly.add(errorMsg + ex.getMessage() + "<br>");
		    siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg + ex.toString(), executeDate, uuid, employeeCode);
		}
	    }

	    try{
	        if (assembly.size() == 0) {
		    reMsg.append("POS調撥檔匯入成功！<br>");
		    FileUtils.copyFileToDirectory(headFile, successFile);
		    FileUtils.copyFileToDirectory(lineFile, successFile);
		    headFile.delete();
		    lineFile.delete();
	        } else {
		    reMsg.append("部分POS調撥資料匯入失敗！訊息如下：<br>");
		    for (int k = 0; k < assembly.size(); k++) {
		        reMsg.append(assembly.get(k));
		    }
	        }
	    }catch(Exception ex1){
		log.error("刪除POS調撥上傳原始檔案發生錯誤，原因：" + ex1.toString());
	    }
	} catch (Exception ex) {
	    errorMsg = "POS調撥匯入" + headFileName + "、" + lineFileName + "失敗，原因：" +  ex.toString();
	    log.error(errorMsg);
	    reMsg.append("POS調撥檔匯入失敗！原因：" + ex.getMessage() + "<br>");
	    siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, employeeCode);
	} finally{
	    try{
                combineFile.delete();
		baseFile.delete();
	    }catch(Exception ex2){
		log.error("刪除POS調撥上傳組合檔案發生錯誤，原因：" + ex2.toString());
	    }
	}

	return reMsg.toString();
    }

    /**
     * 專櫃收貨(TR或TT)
     *
     * @param movementHeadPO
     * @param movementHead
     * @param employeeCode
     * @param systemAdmin
     * @throws Exception
     */
    private void setData(ImMovementHead movementHeadPO, ImMovementHead movementHead , String employeeCode, String systemAdmin) throws Exception {

	//====================檢核原資料轉入庫、轉出庫是否與匯入檔相符合=============================
	ImWarehouseDAO imWarehouseDAO = (ImWarehouseDAO) context.getBean("imWarehouseDAO");
	String arrivalWarehouseCode = movementHead.getArrivalWarehouseCode();
	String actualArrivalWarehouseCode = OldSysMapNewSys.getNewWarehouse(arrivalWarehouseCode);//實際的收貨單位
	if(actualArrivalWarehouseCode == null){
            throw new ValidationErrorException(arrivalWarehouseCode + "查無對應的收貨單位代碼！");
	}else{
            ImWarehouse arrivalWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, actualArrivalWarehouseCode);
	    if (arrivalWarehouse == null) {
	        throw new ValidationErrorException("收貨單位代碼：" + actualArrivalWarehouseCode + "不存在！");
	    }else{
		//將收貨單位的資訊放置於bean
		movementHead.setArrivalWarehouseCode(actualArrivalWarehouseCode);
	    }
	}
	if(!movementHeadPO.getDeliveryWarehouseCode().equals(movementHead.getDeliveryWarehouseCode())){
	    throw new ValidationErrorException("匯入檔的轉出庫與原資料的轉出庫不相符合！");
	}else if(!movementHeadPO.getArrivalWarehouseCode().equals(movementHead.getArrivalWarehouseCode())){
	    throw new ValidationErrorException("匯入檔的轉入庫與原資料的轉入庫不相符合！");
	}
	//=====================================================================================
	//set line data
	List imMovementItemsPO  = movementHeadPO.getImMovementItems();
	if(imMovementItemsPO != null ){
	    for(int i = 0; i < imMovementItemsPO.size(); i++){
                ImMovementItem imMovementItemPO = (ImMovementItem)imMovementItemsPO.get(i);
		String itemCode = imMovementItemPO.getItemCode();
		String warehouseCode = imMovementItemPO.getArrivalWarehouseCode();
		Double deliveryQuantity = imMovementItemPO.getDeliveryQuantity();
		if(deliveryQuantity != null){
		    imMovementItemPO.setArrivalQuantity(deliveryQuantity);//收貨單位照單全收
		}else{
		    throw new ValidationErrorException("品號：" + itemCode + ",庫別：" + warehouseCode + "的轉出數量為空值！");
		}
		imMovementItemPO.setLotControl("N");
	    }
	}
	//set head data
	if (StringUtils.hasText(employeeCode)) {
	    movementHeadPO.setLastUpdatedBy(employeeCode);
	}else{
	    movementHeadPO.setLastUpdatedBy(systemAdmin) ;
	}
	movementHeadPO.setLastUpdateDate(new Date()) ;
	movementHeadPO.setStatus(OrderStatus.FINISH);
	movementHeadPO.setArrivalDate(movementHead.getDeliveryDate());
	movementHeadPO.setRemark1(movementHead.getRemark1());
	movementHeadPO.setReserve5(movementHead.getReserve4());
    }

    /**
     * 專櫃轉出或退回總倉(TT或TB)
     *
     * @param movementHead
     * @param defaultLotNo
     * @param employeeCode
     * @param systemAdmin
     * @throws Exception
     */
    private void setData(ImMovementHead movementHead, String defaultLotNo , String employeeCode, String systemAdmin) throws Exception {

	ImWarehouseDAO imWarehouseDAO = (ImWarehouseDAO) context.getBean("imWarehouseDAO");
	BuLocationDAO buLocationDAO = (BuLocationDAO) context.getBean("buLocationDAO");

	//取得對應的的收貨單位代碼
	String brandCode = movementHead.getBrandCode();
	String deliveryWarehouseCode = movementHead.getDeliveryWarehouseCode();
	String arrivalWarehouseCode = movementHead.getArrivalWarehouseCode();
	String actualArrivalWarehouseCode = OldSysMapNewSys.getNewWarehouse(arrivalWarehouseCode);//實際的收貨單位
	if(actualArrivalWarehouseCode == null){
            throw new ValidationErrorException(arrivalWarehouseCode + "查無對應的收貨單位代碼！");
	}else{
            ImWarehouse arrivalWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, actualArrivalWarehouseCode);
	    if (arrivalWarehouse == null) {
	        throw new ValidationErrorException("收貨單位代碼：" + actualArrivalWarehouseCode + "不存在！");
	    }else{
		//將收貨單位的資訊放置於bean
		movementHead.setArrivalWarehouseCode(actualArrivalWarehouseCode);
		String arrivalWarehouseManager = arrivalWarehouse.getWarehouseManager();
		if (!StringUtils.hasText(arrivalWarehouseManager)) {
	            throw new ValidationErrorException("收貨單位代碼：" + actualArrivalWarehouseCode + "的倉管人員為空值！");
		}else{
	            if ("unknow".equals( UserUtils.getLoginNameByEmployeeCode(arrivalWarehouseManager)))
		        throw new ValidationErrorException("入庫倉管人員(" + arrivalWarehouseManager + ")錯誤！");
		    else
			movementHead.setArrivalContactPerson(arrivalWarehouseManager);
		}
		Long arrivalLocationId = arrivalWarehouse.getLocationId();
		if(arrivalLocationId != null){
		    BuLocation arrivalLocation = (BuLocation) buLocationDAO.findByPrimaryKey(BuLocation.class, arrivalLocationId);
                    if(arrivalLocation != null){
                        movementHead.setArrivalAddress(arrivalLocation.getAddress());
                        movementHead.setArrivalArea(arrivalLocation.getArea());
                        movementHead.setArrivalCity(arrivalLocation.getCity());
                        movementHead.setArrivalZipCode( arrivalLocation.getZipCode());
                    }
		}
		if (StringUtils.hasText(employeeCode)) {
		    movementHead.setCreatedBy(employeeCode);
		    movementHead.setLastUpdatedBy(employeeCode);
		}else{
		    movementHead.setCreatedBy(systemAdmin);
		    movementHead.setLastUpdatedBy(systemAdmin);
		}
		movementHead.setCreationDate(new Date());
		movementHead.setLastUpdateDate(new Date());
		movementHead.setStatus(OrderStatus.SIGNING);
		movementHead.setOrderTypeCode("IMV");
		movementHead.setReserve3(null);
	    }
	}

	List imMovementItems  = movementHead.getImMovementItems();
	//產生line的必要資訊
	for(int i = 0; i < imMovementItems.size(); i++){
	    ImMovementItem imMovementItem = (ImMovementItem)imMovementItems.get(i);
	    ImItem newItem = OldSysMapNewSys.getNewItemCode(brandCode, imMovementItem.getItemCode());
	    if(newItem == null){
	        throw new ValidationErrorException("舊ITEM CODE '" + imMovementItem.getItemCode() + "' 查無對應新ITEM CODE");
	    }else{
	        imMovementItem.setItemCode(newItem.getItemCode());
	    }
	    imMovementItem.setDeliveryWarehouseCode(deliveryWarehouseCode);
	    imMovementItem.setLotNo(defaultLotNo);
	    imMovementItem.setLotControl("N");
	    imMovementItem.setArrivalWarehouseCode(actualArrivalWarehouseCode);
	}
    }

    private Object[] doValidate(ImMovementHead entityBean)
	    throws ValidationErrorException {

	String deliveryWarehouseCode = entityBean.getDeliveryWarehouseCode();
	String arrivalWarehouseCode = entityBean.getArrivalWarehouseCode();
	String origiOrderTypeCode = entityBean.getOriginalOrderTypeCode();
	String origiOrderNo = entityBean.getOriginalOrderNo();

	// 檢核POS上傳的轉出倉庫
	if (!StringUtils.hasText(deliveryWarehouseCode)) {
	    throw new ValidationErrorException("轉出庫為空值無法執行匯入！");
	} else {
	    deliveryWarehouseCode = deliveryWarehouseCode.trim();
	    entityBean.setDeliveryWarehouseCode(deliveryWarehouseCode);
	}
	// 檢核POS上傳的轉入倉庫
	if (!StringUtils.hasText(arrivalWarehouseCode)) {
	    throw new ValidationErrorException("轉入庫為空值無法執行匯入！");
	} else {
	    arrivalWarehouseCode = arrivalWarehouseCode.trim();
	    entityBean.setArrivalWarehouseCode(arrivalWarehouseCode);
	}
	// 檢核POS上傳的單別
	if (!StringUtils.hasText(origiOrderTypeCode)) {
	    throw new ValidationErrorException("單別為空值無法執行匯入！");
	} else {
	    origiOrderTypeCode = origiOrderTypeCode.trim();
	    entityBean.setOriginalOrderTypeCode(origiOrderTypeCode);
	}
	// 檢核POS上傳的單號
	if (!StringUtils.hasText(origiOrderNo)) {
	    throw new ValidationErrorException("單號為空值無法執行匯入！");
	} else {
	    origiOrderNo = origiOrderNo.trim();
	    entityBean.setOriginalOrderNo(origiOrderNo);
	}

	return new Object[]{deliveryWarehouseCode, origiOrderTypeCode, origiOrderNo};
    }

    private void logApprovalResult(Object[] processInfo, ImMovementHead movementHead, WfApprovalResultService wfApprovalResultService){

	WfApprovalResult approvalResult = new WfApprovalResult() ;
	approvalResult.setProcessId((Long)processInfo[0]);
	approvalResult.setBrandCode(movementHead.getBrandCode());
	approvalResult.setOrderTypeCode(movementHead.getOrderTypeCode());
	approvalResult.setOrderNo(movementHead.getOrderNo());
	approvalResult.setFormName(movementHead.getOrderTypeCode() +" - "+ movementHead.getOrderNo());
	approvalResult.setActivityId((Long)processInfo[1]);
	approvalResult.setActivityName((String)processInfo[2]);
	approvalResult.setApprover(movementHead.getLastUpdatedBy());
	approvalResult.setDateTime(new Date());

	wfApprovalResultService.save(approvalResult);
    }
}