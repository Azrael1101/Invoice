package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.print.attribute.standard.Finishings;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImLetterOfCreditAlter;
import tw.com.tm.erp.hbm.bean.ImLetterOfCreditHead;
import tw.com.tm.erp.hbm.bean.ImLetterOfCreditLine;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCurrencyDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierDAO;
import tw.com.tm.erp.hbm.dao.ImLetterOfCreditAlterDAO;
import tw.com.tm.erp.hbm.dao.ImLetterOfCreditHeadDAO;
import tw.com.tm.erp.hbm.dao.ImLetterOfCreditLineDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.UserUtils;

/**
 * 信用狀 Service
 * 
 * @author T15394
 *
 */
public class ImLetterOfCreditService {

    private static final Log log = LogFactory.getLog(ImLetterOfCreditService.class);

    public static final String PROGRAM_ID= "IM_LETTEOFCREDIT";
    public static final String LC_ID= "LC";

    private BuBrandDAO 					buBrandDAO;
    private BuCurrencyDAO 				buCurrencyDAO;
    private BuSupplierDAO 				buSupplierDAO;
    private ImLetterOfCreditHeadDAO 	imLetterOfCreditHeadDAO;
    private ImLetterOfCreditAlterDAO 	imLetterOfCreditAlterDAO;
    private ImLetterOfCreditLineDAO 	imLetterOfCreditLineDAO;
    private PoPurchaseOrderHeadDAO 		poPurchaseOrderHeadDAO ;
    //private ImReceiveHeadDAO imReceiveHeadDAO;
    private BuSupplierWithAddressViewService buSupplierWithAddressViewService;
    private BuBasicDataService 			buBasicDataService; 
    private SiProgramLogAction 			siProgramLogAction;
	
    /* Spring IoC */
    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
	this.buBrandDAO = buBrandDAO;
    }
    public void setBuCurrencyDAO(BuCurrencyDAO buCurrencyDAO) {
	this.buCurrencyDAO = buCurrencyDAO;
    }
    public void setBuSupplierDAO(BuSupplierDAO buSupplierDAO) {
	this.buSupplierDAO = buSupplierDAO;
    }
    public void setImLetterOfCreditHeadDAO(ImLetterOfCreditHeadDAO imLetterOfCreditHeadDAO) {
	this.imLetterOfCreditHeadDAO = imLetterOfCreditHeadDAO;
    }
    public void setImLetterOfCreditAlterDAO(
	    ImLetterOfCreditAlterDAO imLetterOfCreditAlterDAO) {
	this.imLetterOfCreditAlterDAO = imLetterOfCreditAlterDAO;
    }
    public void setImLetterOfCreditLineDAO(
	    ImLetterOfCreditLineDAO imLetterOfCreditLineDAO) {
	this.imLetterOfCreditLineDAO = imLetterOfCreditLineDAO;
    }
    public void setBuSupplierWithAddressViewService(
	    BuSupplierWithAddressViewService buSupplierWithAddressViewService) {
	this.buSupplierWithAddressViewService = buSupplierWithAddressViewService;
    }
    public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
	this.buBasicDataService = buBasicDataService;
    }
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }
	
    /**
     * 修狀明細檔欄位
     */
    public static final String[] ALT_GRID_FIELD_NAMES = { 
	"indexNo", "alterLcDate", "alterAmount", 
	"lineId", "isLockRecord", "isDeleteRecord", "message"
    };

    public static final String[] ALT_GRID_FIELD_DEFAULT_VALUES = { 
	"", "", "0.0",
	"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };

    public static final int[] ALT_GRID_FIELD_TYPES = { 
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_DOUBLE, 
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };

    /**
     * 信用狀明細檔欄位
     */
    public static final String[] GRID_FIELD_NAMES = { 
	"indexNo", "receiveNo", "receiveAmount", 
	"creditAmount", "arriveDate", "dueDate", 
	"returnAmount", "returnFees", "remark1",
	"lineId"
    };

    public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
	"", "", "0.0",  
	"0.0","", "", 
	"0.0", "0.0", "",
	""
    };

    public static final int[] GRID_FIELD_TYPES = { 
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, 
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_DATE, 
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_LONG
    };

    /**
     * 信用狀查詢picker用的欄位
     */
    public static final String[] GRID_SEARCH_FIELD_NAMES = { 
	"lcNo", "supplierName", "lcDate", "openingBank",
	"poNo", "statusName", "lastUpdateDate", 
	"headId"
    };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
	"", "", "", 
	"", "", "", 
	"", ""
    };
	
    /**
     * 信用狀初始化 bean 額外顯示欄位
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);

	try{
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);

	    ImLetterOfCreditHead imLetterOfCreditHead = this.getActualHead(otherBean, resultMap);

	    Map multiList = new HashMap(0);
	    resultMap.put("form", imLetterOfCreditHead);
	    resultMap.put("brandName",buBrandDAO.findById(imLetterOfCreditHead.getBrandCode()).getBrandName());
	    resultMap.put("statusName", OrderStatus.getChineseWord(imLetterOfCreditHead.getStatus()));
	    resultMap.put("createByName", employeeName);
	    resultMap.putAll( this.getOtherColumn(imLetterOfCreditHead, loginBrandCode) );

	    List<BuCurrency> allCurrencyCodes = buCurrencyDAO.findByProperty("BuCurrency", "enable", "Y");

	    String currencyCode = imLetterOfCreditHead.getCurrencyCode();
	    multiList.put("allCurrencyCodes"	, AjaxUtils.produceSelectorData(allCurrencyCodes, "currencyCode", "currencyCName", true, true, currencyCode != null ? currencyCode : "" ));

	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("信用狀初始化失敗，原因：" + ex.toString());
	    throw new Exception("信用狀初始化失敗，原因：" + ex.toString());

	}
    }

    /**
     * 產生新的信用狀
     * @param otherBean
     * @return
     * @throws Exception
     */
    public ImLetterOfCreditHead executeNew(Object otherBean)throws Exception{
	ImLetterOfCreditHead form = new ImLetterOfCreditHead();
	try {
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

	    form.setLcNo("");
	    form.setOrderTypeCode(LC_ID);
	    form.setLcDate(null);
//	    form.setOpeningBank("");
	    form.setOpeningBankCode("");
	    form.setBrandCode(loginBrandCode);
	    form.setStatus( OrderStatus.SAVE );
	    form.setSupplierCode("");
	    form.setPoNo("");
	    form.setCurrencyCode("");
	    form.setCreditNoted(0D);
	    form.setCreatedBy(loginEmployeeCode);
	    form.setLastUpdatedBy(loginEmployeeCode);
	    form.setLatestShipmentDate(null);
	    form.setExpiryDate(null);
	    form.setOpeningAmount(0D);
	    form.setOpeningFees(0D);
	    form.setLastUpdateDate(new Date());

	    this.saveHead(form);

	} catch (Exception e) {
	    log.error("建立新信用狀主檔失敗,原因:"+e.toString());
	    throw new Exception("建立新信用狀主檔失敗,原因:"+e.toString());
	}
	return form;
    }
	
    /**
     * 將信用狀資料新增或更新至信用狀主檔、明細檔、修狀明細檔
     * 
     * @param lcHead
     * @param conditionMap
     * @param loginUser
     * @return String
     * @throws Exception
     */
    public String saveOrUpdateImLetterOfCredit(ImLetterOfCreditHead lcHead, HashMap conditionMap, String loginUser) throws Exception {
	log.info("ImLetterOfCreditService.saveOrUpdateImLetterOfCredit");
	try {
	    String beforeChangeStatus = (String) conditionMap.get("beforeChangeStatus");
	    if (OrderStatus.SAVE.equals(beforeChangeStatus)) {
		validateLC(lcHead);
		countAllTotalAmount(lcHead, conditionMap, null);
	    }

	    return insertOrUpdateImLetterOfCredit(lcHead, loginUser);
	} catch (FormException fe) {
	    log.error("信用狀存檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("信用狀存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("信用狀存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 信用狀存檔,取得暫存碼
     * @param imLetterOfCreditHead
     * @throws Exception
     */ 
    public void saveHead(ImLetterOfCreditHead imLetterOfCreditHead) throws Exception{

	try{
	    String tmpOrderNo = AjaxUtils.getTmpOrderNo();
	    imLetterOfCreditHead.setReserve5(tmpOrderNo); 
	    imLetterOfCreditHead.setOrderNo(tmpOrderNo);
	    imLetterOfCreditHead.setLastUpdateDate(new Date());
	    imLetterOfCreditHead.setCreationDate(new Date());
	    imLetterOfCreditHeadDAO.save(imLetterOfCreditHead);	  
	}catch(Exception ex){
	    log.error("取得暫時單號儲存信用狀發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得暫時單號儲存信用狀發生錯誤，原因：" + ex.getMessage());
	}	
    }
	
    /**
     * 將信 用狀主檔查詢結果存檔
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
    /**
     * 信用狀依formAction取得下個狀態
     */
    public void setNextStatus(ImLetterOfCreditHead head, String formAction, String beforeStatus){
	String status = this.setHeadStatus(head,formAction, beforeStatus);
	this.setAllLinesStatus(head, status);
    }

    /**
     * 信用狀反轉狀態
     * @param head
     * @param formAction
     * @param beforeStatus
     */
    public void setReverseNextStatus(ImLetterOfCreditHead head){
	String status = setReverseHeadStatus(head);
	setAllLinesStatus(head, status);
    }
    
    /**
     * 依 formAction 取得信用狀主檔下個狀態
     * @param head
     * @param formAction
     * @return
     */
    private String setHeadStatus(ImLetterOfCreditHead head, String formAction, String beforeStatus){
	String status = OrderStatus.UNKNOW;

	if(OrderStatus.FORM_SAVE.equals(formAction)){
	    head.setStatus(OrderStatus.SAVE);
	    status = OrderStatus.SAVE;
	}else if( (OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction))){ 
	    if( OrderStatus.SAVE.equals(head.getStatus()) ){
		head.setStatus(OrderStatus.FINISH);
		status = OrderStatus.FINISH;
	    }else if( OrderStatus.FINISH.equals(head.getStatus()) ){
		head.setStatus(OrderStatus.FINISH);
		status = OrderStatus.FINISH;
	    }

	}else if( OrderStatus.CLOSE.equals(formAction) ) { 
	    if(OrderStatus.FINISH.equals(head.getStatus()) ) {
		head.setStatus(OrderStatus.CLOSE);
		status = OrderStatus.CLOSE;
	    }

	}else if(OrderStatus.FORM_VOID.equals(formAction)){
	    head.setStatus(OrderStatus.VOID);
	    status = OrderStatus.VOID;
	}

	log.info( "formAction = " + formAction);
	log.info( "head.getStatus() = " + head.getStatus());

	return status;
    }

    /**
     * 反轉主黨檔
     * @param head
     * @return
     */
    private String setReverseHeadStatus(ImLetterOfCreditHead head){
	String status = head.getStatus();
	if( OrderStatus.CLOSE.equals(status) ) { 
	    head.setStatus(OrderStatus.SAVE);
	    status = OrderStatus.SAVE;
	}
	return status;
    }
    
    /**
     * 設定修狀明細檔和信用狀明細檔狀態
     * @param head
     * @param status
     */
    private void setAllLinesStatus(ImLetterOfCreditHead head, String status){
	List<ImLetterOfCreditLine> lcLines = head.getImLetterOfCreditLines();
	List<ImLetterOfCreditAlter> lcAlters = head.getImLetterOfCreditAlters();
	if (lcLines != null && lcLines.size() > 0) {
	    for (ImLetterOfCreditLine lcLine : lcLines) {
		lcLine.setStatus(status);
	    }
	}
	if (lcAlters != null && lcAlters.size() > 0) {
	    for (ImLetterOfCreditAlter lcAlter : lcAlters) {
		if(AjaxUtils.IS_DELETE_RECORD_FALSE.equals(lcAlter.getIsDeleteRecord())){
		    lcAlter.setStatus(status);
		}
	    }
	}
    }
	
    /**
     * 設定中文狀態名稱
     * @param cmMovementHeads
     */
    private void setImLetterOfCreditStatusName(List<ImLetterOfCreditHead> imLetterOfCreditHeads) throws Exception{
	for (ImLetterOfCreditHead imLetterOfCreditHead : imLetterOfCreditHeads) {
	    imLetterOfCreditHead.setStatusName(OrderStatus.getChineseWord(imLetterOfCreditHead.getStatus()));
	    BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewService.findByBrandCodeAndSupplierCode(imLetterOfCreditHead.getBrandCode(), imLetterOfCreditHead.getSupplierCode());
	    if(buSupplierWithAddressView == null){
		imLetterOfCreditHead.setSupplierName("");
	    }else{
		imLetterOfCreditHead.setSupplierName(buSupplierWithAddressView.getChineseName());
	    }
	}
    }

    /**
     * 啟動流程
     * @param form
     * @return
     * @throws ProcessFailedException
     */
    public static Object[] startProcess(ImLetterOfCreditHead form) throws ProcessFailedException{       

	try{           
	    String packageId = "Im_LetterOfCredit";         
	    String processId = "process";           
	    String version = "20091015";
	    String sourceReferenceType = "Im_Letter (1)";
	    HashMap context = new HashMap();	    
	    context.put("formId", form.getHeadId());
	    return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
	}catch (Exception ex){
	    log.error("信用狀流程啟動失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("信用狀流程啟動失敗！");
	}	      
    }

    /**
     * 判斷是否為新資料，並將信用狀資料新增或更新至信用狀主檔、明細檔、修狀明細檔
     * 
     * @param lcHead
     * @param loginUser
     * @return String
     * @throws FormException
     * @throws Exception
     */
    private String insertOrUpdateImLetterOfCredit(ImLetterOfCreditHead lcHead, String loginUser) throws FormException, Exception {

	UserUtils.setOpUserAndDate(lcHead, loginUser);
	UserUtils.setUserAndDate(lcHead.getImLetterOfCreditLines(), loginUser);
	UserUtils.setUserAndDate(lcHead.getImLetterOfCreditAlters(), loginUser);

	if (lcHead.getHeadId() == null) {
	    insertImLetterOfCredit(lcHead);
	} else {
	    modifyImLetterOfCredit(lcHead);
	}
	return "L/C NO：" + lcHead.getLcNo() + "存檔成功！";
    }

    /**
     * 判斷是否為新資料，並將信用狀資料新增或更新至信用狀主檔、明細檔、修狀明細檔
     * 
     * @param lcHead
     * @param loginUser
     * @return String
     * @throws FormException
     * @throws Exception
     */
    private String updateImLetterOfCredit(ImLetterOfCreditHead lcHead, String loginUser) throws FormException, Exception {

	UserUtils.setOpUserAndDate(lcHead, loginUser);
	UserUtils.setUserAndDate(lcHead.getImLetterOfCreditLines(), loginUser);
	UserUtils.setUserAndDate(lcHead.getImLetterOfCreditAlters(), loginUser);

	if (lcHead.getHeadId() == null) {
	    imLetterOfCreditHeadDAO.save(lcHead);
	} else {
	    imLetterOfCreditHeadDAO.update(lcHead);
	}
	return "L/C NO：" + lcHead.getLcNo() + "存檔成功！";
    }
    
    /**
     * 新增至信用狀主檔、明細檔、修狀明細檔
     * 
     * @param saveObj
     */
    private void insertImLetterOfCredit(Object saveObj) {
	imLetterOfCreditHeadDAO.save(saveObj);
    }

    /**
     * 更新至信用狀主檔、明細檔、修狀明細檔
     * 
     * @param updateObj
     */
    private void modifyImLetterOfCredit(Object updateObj) {
	imLetterOfCreditHeadDAO.update(updateObj);
    }

    /**
     * 依據primary key為查詢條件，取得信用狀主檔
     * 
     * @param headId
     * @return ImLetterOfCreditHead
     * @throws Exception
     */
    public ImLetterOfCreditHead findImLetterOfCreditHeadById(Long headId) throws Exception {

	try {
	    ImLetterOfCreditHead lcHead = (ImLetterOfCreditHead) imLetterOfCreditHeadDAO.findByPrimaryKey(ImLetterOfCreditHead.class,
		    headId);

	    return lcHead;
	} catch (Exception ex) {
	    log.error("依據主鍵：" + headId + "查詢信用狀主檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據主鍵：" + headId + "查詢信用狀主檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 合計所有修狀金額、信用狀總額、進貨總額、信用狀剩餘金額
     * 
     * @param lcHead
     * @param conditionMap
     * @param event
     * @throws FormException
     * @throws Exception
     */
    public void countAllTotalAmount(ImLetterOfCreditHead lcHead, HashMap conditionMap, String event) throws FormException, Exception {

	try {
	    String beforeChangeStatus = (String) conditionMap.get("beforeChangeStatus");
	    if (("countAmount").equals(event) && (OrderStatus.SAVE.equals(beforeChangeStatus))) {
		validateLC(lcHead);
	    }

	    Double openingAmount = lcHead.getOpeningAmount();// 開狀金額
	    Double creditNoted = lcHead.getCreditNoted();// creditNoted
	    List<ImLetterOfCreditLine> lcLines = lcHead.getImLetterOfCreditLines();
	    List<ImLetterOfCreditAlter> lcAlters = lcHead.getImLetterOfCreditAlters();

	    Double totalAlterAmount = 0D;
	    Double totalLcAmount = 0D;
	    Double totalCreditAmount = 0D;
	    Double totalReceiveAmount = 0D;
	    Double restAmount = 0D;
	    // 加總進貨總額
	    for (ImLetterOfCreditLine lcLine : lcLines) {
		if( null != lcLine && null != lcLine.getReceiveAmount() )
		    totalReceiveAmount += lcLine.getReceiveAmount();
		if( null != lcLine && null != lcLine.getCreditAmount() ) // 加總折讓金額
		    totalCreditAmount  += lcLine.getCreditAmount();
	    }
	    // 加總修狀金額
	    for (ImLetterOfCreditAlter lcAlter : lcAlters) {
		if( null != lcAlter && null != lcAlter.getAlterAmount() )
		    totalAlterAmount += lcAlter.getAlterAmount();
	    }
	    // 信用狀總額 = 開狀金額 - credit noted + 修狀總額
	    totalLcAmount = openingAmount - creditNoted + totalAlterAmount;
	    // 信用狀剩餘金額 = 信用狀總額 - 進貨總額 + credit noted
	    restAmount = totalLcAmount - totalReceiveAmount + creditNoted;

	    lcHead.setTotalAlterAmount(totalAlterAmount);
	    lcHead.setTotalLcAmount(totalLcAmount);
	    lcHead.setCreditNoted(totalCreditAmount); // 加總折讓金額
	    lcHead.setTotalReceiveAmount(totalReceiveAmount);
	    lcHead.setRestAmount(restAmount);

	    log.info("lcHead.getTotalAlterAmount() = " + lcHead.getTotalAlterAmount() );
	    log.info("lcHead.getTotalLcAmount() = " + lcHead.getTotalLcAmount() );
	    log.info("lcHead.getTotalReceiveAmount() = " + lcHead.getTotalReceiveAmount() );
	    log.info("lcHead.getCreditNoted() = " + lcHead.getCreditNoted() );
	    log.info("lcHead.getRestAmount() = " + lcHead.getRestAmount() );

	} catch (FormException fe) {
	    log.error("信用狀金額統計時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("信用狀金額統計時發生錯誤，原因：" + ex.toString());
	    throw new Exception("信用狀金額統計時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * ajax 合計所有修狀金額、信用狀總額、進貨總額、信用狀剩餘金額 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> countAllTotalAmount(Properties httpRequest) throws Exception{

	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	Long headId = 0L;
	try {
	    headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    Double openingAmount = NumberUtils.getDouble( httpRequest.getProperty("openingAmount") );	// 開狀金額

	    ImLetterOfCreditHead lcHead = this.findImLetterOfCreditHeadById(headId);

//	    Double openingAmount = NumberUtils.getDouble( lcHead.getOpeningAmount() );	
	    Double creditNoted = NumberUtils.getDouble( lcHead.getCreditNoted() );		// creditNoted
	    List<ImLetterOfCreditLine> lcLines = lcHead.getImLetterOfCreditLines();
	    List<ImLetterOfCreditAlter> lcAlters = lcHead.getImLetterOfCreditAlters();
	    Double totalAlterAmount = 0D;
	    Double totalLcAmount = 0D;
	    Double totalCreditAmount = 0D;
	    Double totalReceiveAmount = 0D;
	    Double restAmount = 0D;
	    Double totalReturnAmount = 0D;
	    Double totalReturnFees = 0D;

	    for (ImLetterOfCreditLine lcLine : lcLines) {
		if( null != lcLine && null != lcLine.getReceiveAmount() )
		    // 加總進貨總額
		    totalReceiveAmount = OperationUtils.add(totalReceiveAmount, lcLine.getReceiveAmount()).doubleValue(); // NumberUtils.getDouble( lcLine.getReceiveAmount() );
		// 加總折讓金額
		totalCreditAmount = OperationUtils.add(totalCreditAmount, lcLine.getCreditAmount()).doubleValue(); // NumberUtils.getDouble( lcLine.getCreditAmount() );
		// 加總還款金額
		totalReturnAmount = OperationUtils.add(totalReturnAmount, lcLine.getReturnAmount()).doubleValue(); // NumberUtils.getDouble( lcLine.getReturnAmount() );
		// 加總還款手續費
		totalReturnFees = OperationUtils.add(totalReturnFees, lcLine.getReturnFees()).doubleValue();  // NumberUtils.getDouble( lcLine.getReturnFees() );
	    }
	    // 加總修狀金額
	    for (ImLetterOfCreditAlter lcAlter : lcAlters) {
		if( null != lcAlter && null != lcAlter.getAlterAmount() )
		    totalAlterAmount =  OperationUtils.add(totalAlterAmount, lcAlter.getAlterAmount()).doubleValue(); // NumberUtils.getDouble( lcAlter.getAlterAmount() );
	    }
	    // 信用狀總額 = 開狀金額 - credit noted + 修狀總額
	    totalLcAmount = OperationUtils.add(OperationUtils.subtraction(openingAmount, creditNoted).doubleValue(), totalAlterAmount).doubleValue();  // openingAmount - creditNoted + totalAlterAmount;
	    // 信用狀剩餘金額 = 信用狀總額 - 進貨總額 + credit noted
	    restAmount = OperationUtils.add(OperationUtils.subtraction(totalLcAmount, totalReceiveAmount).doubleValue(), creditNoted).doubleValue(); // totalLcAmount - totalReceiveAmount + creditNoted;

	    properties.setProperty("totalLcAmount", OperationUtils.round(totalLcAmount, 2).toString() );
	    properties.setProperty("totalReceiveAmount", OperationUtils.round(totalReceiveAmount, 2).toString() );
	    properties.setProperty("totalCreditAmount", OperationUtils.round(totalCreditAmount, 2).toString());
	    properties.setProperty("restAmount", OperationUtils.round(restAmount, 2).toString());
	    properties.setProperty("totalReturnAmount", OperationUtils.round(totalReturnAmount, 2).toString()); 
	    properties.setProperty("totalReturnFees", OperationUtils.round(totalReturnFees, 2).toString());

	    result.add(properties);	

	    return result;	        
	} catch (Exception ex) {
	    log.error("依據主鍵：" + headId + "計算時發生錯誤，原因：" + ex.toString());
	    throw new Exception("計算金額統計資料失敗！");
	}        

    }

    /**
     * 檢核信用狀主檔,明細檔,修狀明細檔, 算總金額
     * @param parameterMap
     * @param lcNo
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List checkedImLetterOfCredit(Map parameterMap, String lcNo)throws FormException, Exception{
	List errorMsgs = new ArrayList(0);
	String message = null;
	String identification = null;
	ImLetterOfCreditHead imLetterOfCreditHead = null;
	try{

	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    imLetterOfCreditHead = this.getActualLetterOfCredit(formLinkBean);

	    String status = imLetterOfCreditHead.getStatus();
	    if (OrderStatus.SAVE.equals(status) || OrderStatus.FINISH.equals(status) ) {

		identification = MessageStatus.getIdentification(imLetterOfCreditHead.getBrandCode(), 
			LC_ID, imLetterOfCreditHead.getReserve5());

		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

		validateLC( imLetterOfCreditHead, lcNo, PROGRAM_ID, identification, errorMsgs ); 

		HashMap map = new HashMap();
		map.put("beforeChangeStatus", status);
		countAllTotalAmount(imLetterOfCreditHead,map,null);
	    }

	}catch (Exception ex) {
	    message = "信用狀檢核失敗，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, imLetterOfCreditHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);	
	}
	return errorMsgs;
    }

    /**
     * 只檢核信用狀主檔
     * @param lcHead
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List checkedLcNo (ImLetterOfCreditHead lcHead, String lcNo)throws FormException, Exception{
	List errorMsgs = new ArrayList(0);
	String message = null;
	String identification = null;
	try{

	    String status = lcHead.getStatus();

	    identification = MessageStatus.getIdentification(lcHead.getBrandCode(), 
		    LC_ID, lcHead.getReserve5());

	    if (OrderStatus.SAVE.equals(status)) {
		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

		errorMsgs = validateLCHead( lcHead, lcNo, PROGRAM_ID, identification, errorMsgs );

		HashMap map = new HashMap();
		map.put("beforeChangeStatus", status);
		countAllTotalAmount(lcHead,map,null);
	    }
	}catch (Exception ex) {
	    message = "信用狀檢核失敗，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);	
	}
	return errorMsgs;
    }

    /**
     * 只檢核信用狀主檔 for 背景送出
     * @param lcHead
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List checkedLcNoForBackground (ImLetterOfCreditHead lcHead, String lcNo)throws FormException, Exception{
	List errorMsgs = new ArrayList(0);
	String message = null;
	String identification = null;
	try{

	    String status = lcHead.getStatus();

	    identification = MessageStatus.getIdentification(lcHead.getBrandCode(), 
		    LC_ID, lcHead.getReserve5());

	    if (OrderStatus.SAVE.equals(status)) {
		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

		errorMsgs = validateLCHead( lcHead, lcNo, PROGRAM_ID, identification, errorMsgs );
	    }
	}catch (Exception ex) {
	    message = "信用狀檢核失敗，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);	
	}
	return errorMsgs;
    }

    /**
     * 完成任務工作
     * @param assignmentId
     * @param approveResult
     * @return
     * @throws ProcessFailedException
     */
    public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws ProcessFailedException{

	try{           
	    HashMap context = new HashMap();
	    context.put("approveResult", approveResult);

	    return ProcessHandling.completeAssignment(assignmentId, context);
	}catch (Exception ex){
	    log.error("完成信用狀工作任務失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("完成信用狀工作任務失敗！");
	}
    }

    /**
     * 檢核進入點
     * 
     * @param lcHead
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validateLC(ImLetterOfCreditHead lcHead) throws ValidationErrorException, NoSuchObjectException {

	checkLCHead(lcHead);
	checkLCLines(lcHead);
	checkLCAlters(lcHead);
    }

    /**
     * 檢核進入點 for 訊息提示寫入log
     * @param lcHead
     * @param lcNo
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validateLC(ImLetterOfCreditHead lcHead,String lcNo, String programId, String identification, List errorMsgs) throws ValidationErrorException, NoSuchObjectException {
	checkLCHead(lcHead, lcNo, programId, identification, errorMsgs);
	checkLCLines(lcHead, programId, identification, errorMsgs);
	checkLCAlters(lcHead, programId, identification, errorMsgs);
    }

    /**
     * 只檢核lcNo
     * @param lcHead
     * @param lcNo
     * @param programId
     * @param identification
     * @param errorMsgs
     * @return
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private List validateLCHead(ImLetterOfCreditHead lcHead, String lcNo, String programId, String identification, List errorMsgs) throws ValidationErrorException, NoSuchObjectException {
	String message = null;
	String tabName = "主檔資料";
	try {

	    if (!StringUtils.hasText(lcNo)) {
		message = "請輸入" + tabName + "的L/C NO！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);	
	    } else {
		lcNo = lcNo.trim().toUpperCase();
		if (lcHead.getHeadId() != null) {
		    if(lcHead.getLcNo() == null ){
			List lcHeadList = imLetterOfCreditHeadDAO.findByProperty("ImLetterOfCreditHead", "lcNo", lcNo);
			if (lcHeadList != null && lcHeadList.size() > 0) {
			    message = "L/C NO：" + lcNo + "已存在，請勿重複建立！";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);	
			}else{	// 未重複
			    log.info( "設定lcNo = " + lcNo );
			    lcHead.setLcNo(lcNo);
			}
		    }else{ 
			if( lcNo.equals(lcHead.getLcNo()) ){	// 填入值跟之前一樣,則不處理 ,仍是唯一值
			}else{	
			    List lcHeadList = imLetterOfCreditHeadDAO.findByProperty("ImLetterOfCreditHead", "lcNo", lcNo);
			    if (lcHeadList != null && lcHeadList.size() > 0) {
				message = "L/C NO：" + lcNo + "已存在，請勿重複建立！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);	
			    }else{
				log.info( "設定lcNo = " + lcNo );
				lcHead.setLcNo(lcNo);	
			    }
			}
		    }
		}
	    }

	} catch (Exception e) {
	    message = "檢核信用狀主檔單" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
	return errorMsgs;
    }


    /**
     * 檢核信用狀主檔(ImLetterOfCreditHead)
     * 
     * @param lcHead
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void checkLCHead(ImLetterOfCreditHead lcHead) throws ValidationErrorException, NoSuchObjectException {

	String tabName = "主檔資料";
	String brandCode = lcHead.getBrandCode();
	String lcNo = lcHead.getLcNo();
	Date lcDate = lcHead.getLcDate();
	String openingBank = lcHead.getOpeningBank();
	String supplierCode = lcHead.getSupplierCode();
	String poNo = lcHead.getPoNo();
	String currencyCode = lcHead.getCurrencyCode();
	Date latestShipmentDate = lcHead.getLatestShipmentDate();
	Date expiryDate = lcHead.getExpiryDate();
	Double openingAmount = lcHead.getOpeningAmount();
	Double openingFees = lcHead.getOpeningFees();
	Double creditNoted = lcHead.getCreditNoted();
	String defaultPurchaseOrderTypeCode = "POF" ; //國外採購單
	String defaultReceiveOrderTypeCode = "IRF" ; //國外進貨單

	if (!StringUtils.hasText(lcNo)) {
	    throw new ValidationErrorException("請輸入" + tabName + "的L/C NO！");
	} else {
	    lcNo = lcNo.trim().toUpperCase();
	    lcHead.setLcNo(lcNo);
	    if (lcHead.getHeadId() == null) {
		List lcHeadList = imLetterOfCreditHeadDAO.findByProperty("ImLetterOfCreditHead", "lcNo", lcNo);
		if (lcHeadList != null && lcHeadList.size() > 0) {
		    throw new ValidationErrorException("L/C NO：" + lcNo + "已存在，請勿重複建立！");
		}
	    }
	}

	if (lcDate == null) {
	    throw new ValidationErrorException("請輸入" + tabName + "的開狀日！");
	} else if (!StringUtils.hasText(openingBank)) {
	    throw new ValidationErrorException("請輸入" + tabName + "的銀行！");
	} else {
	    openingBank = openingBank.trim().toUpperCase();
	    lcHead.setOpeningBank(openingBank);
	}

	if (!StringUtils.hasText(supplierCode)) {
	    throw new ValidationErrorException("請輸入" + tabName + "的供應商！");
	} else {
	    supplierCode = supplierCode.trim().toUpperCase();
	    lcHead.setSupplierCode(supplierCode);
	    if (buSupplierDAO.findByPrimaryKey(BuSupplier.class, new BuSupplierId(supplierCode, brandCode)) == null)
		throw new NoSuchObjectException("查無" + tabName + "的供應商！");
	}

	// TODO:檢核PO單號是否存在 , 不過要先確認是否只有國外LC,如果有國內LC 就要在HEAD加上 ORDER_TYPE分辨 國內國外
	/*
		if (!StringUtils.hasText(poNo)) {
			poNo = poNo.trim().toUpperCase();
			String poNos[] = poNo.split(",");
			//lcHead.setPoNo(poNo);

			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode );
			findObjs.put("orderTypeCode", defaultPurchaseOrderTypeCode);
			for(int index = 0; index < poNos.length; index++){				
				findObjs.put("startOrderNo", poNos[index] );
				findObjs.put("endOrderNo", poNos[index]   );
				List<PoPurchaseOrderHead> heads = poPurchaseOrderHeadDAO.find(findObjs);
				if( null == heads || heads.size() == 0)
					throw new ValidationErrorException("查無單別:" + defaultPurchaseOrderTypeCode + "PO單號:" + tabName + " ！");
			}			
		}
	 */

	if (!StringUtils.hasText(currencyCode)) {
	    throw new ValidationErrorException("請輸入" + tabName + "的幣別！");
	} else {
	    currencyCode = currencyCode.trim().toUpperCase();
	    lcHead.setCurrencyCode(currencyCode);
	    if (buCurrencyDAO.findById(currencyCode) == null)
		throw new NoSuchObjectException("查無" + tabName + "的幣別！");
	}

	if (openingAmount == null) {
	    throw new ValidationErrorException("請輸入" + tabName + "的開狀金額！");
	} else if (creditNoted == null) {
	    throw new ValidationErrorException("請輸入" + tabName + "的Credit noted！");
	}
    }

    /**
     * 檢核信用狀明細檔(ImLetterOfCreditLine)
     * 
     * @param lcHead
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void checkLCLines(ImLetterOfCreditHead lcHead) throws ValidationErrorException, NoSuchObjectException {

	String tabName = "明細資料頁籤";
	List lcLines = lcHead.getImLetterOfCreditLines();
	for (int i = 0; i < lcLines.size(); i++) {
	    ImLetterOfCreditLine lcLine = (ImLetterOfCreditLine) lcLines.get(i);
	    String receiveNo = lcLine.getReceiveNo();
	    Double receiveAmount = lcLine.getReceiveAmount();
	    if (!StringUtils.hasText(receiveNo)) {
		throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的進貨單號！");
	    } else {
		receiveNo = receiveNo.trim().toUpperCase();
		lcLine.setReceiveNo(receiveNo);
		// TODO:檢核進貨單號是否存在
	    }

	    if (receiveAmount == null) {
		throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的進貨金額！");
	    } else if (receiveAmount == 0D) {
		throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的進貨金額不可為零！");
	    }
	    // 將第一列的進貨單號放置到Head的分攤費用進貨單號
	    if (i == 0) {
		lcHead.setApportionedFeesReceiveNo(receiveNo);
	    }
	    lcLine.setStatus(lcHead.getStatus());
	}
    }

    /**
     * 檢核信用狀修狀明細檔(ImLetterOfCreditAlter)
     * 
     * @param lcHead
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void checkLCAlters(ImLetterOfCreditHead lcHead) throws ValidationErrorException, NoSuchObjectException {

	String tabName = "修狀資料頁籤";
	Date lcDate = lcHead.getLcDate();
	List lcAlters = lcHead.getImLetterOfCreditAlters();
	HashMap map = new HashMap();
	for (int i = 0; i < lcAlters.size(); i++) {
	    ImLetterOfCreditAlter lcAlter = (ImLetterOfCreditAlter) lcAlters.get(i);
	    Date alterLcDate = lcAlter.getAlterLcDate();
	    Double alterAmount = lcAlter.getAlterAmount();

	    if (alterLcDate == null) {
		throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的修狀日！");
	    } else if (alterLcDate.before(lcDate)) {
		throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的修狀日不可小於開狀日！");
	    } else if (map.get(DateUtils.format(alterLcDate)) != null) {
		throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的修狀日重複！");
	    } else {
		map.put(DateUtils.format(alterLcDate), DateUtils.format(alterLcDate));
	    }

	    if (alterAmount == null) {
		throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的修狀金額！");
	    } else if (alterAmount == 0D) {
		throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的修狀金額不可為零！");
	    }
	    lcAlter.setStatus(lcHead.getStatus());
	}
    }

    /**
     * 檢核信用用狀主檔 for 訊息提示
     * @param lcHead
     * @param lcNo
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void checkLCHead(ImLetterOfCreditHead lcHead, String lcNo, String programId,
	    String identification, List errorMsgs)
    throws ValidationErrorException, NoSuchObjectException {

	String message = null;
	String tabName = "主檔資料";
	try {
	    String brandCode = lcHead.getBrandCode();
//	    String lcNo = lcHead.getLcNo();
	    Date lcDate = lcHead.getLcDate();
	    String openingBankCode = lcHead.getOpeningBankCode();
	    String supplierCode = lcHead.getSupplierCode();
//	    String poNo = lcHead.getPoNo();
	    String currencyCode = lcHead.getCurrencyCode();
//	    Date latestShipmentDate = lcHead.getLatestShipmentDate();
//	    Date expiryDate = lcHead.getExpiryDate();
	    Double openingAmount = lcHead.getOpeningAmount();
//	    Double openingFees = lcHead.getOpeningFees();
	    Double creditNoted = lcHead.getCreditNoted();
//	    String defaultPurchaseOrderTypeCode = "POF" ; //國外採購單
//	    String defaultReceiveOrderTypeCode = "IRF" ; //國外進貨單


	    if (!StringUtils.hasText(lcNo)) {
		message = "請輸入" + tabName + "的L/C NO！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);	
	    } else {
		lcNo = lcNo.trim().toUpperCase();
		if (lcHead.getHeadId() != null) {
		    if(lcHead.getLcNo() == null ){ 
			List lcHeadList = imLetterOfCreditHeadDAO.findByProperty("ImLetterOfCreditHead", "lcNo", lcNo);
			if (lcHeadList != null && lcHeadList.size() > 0) {
			    message = "L/C NO：" + lcNo + "已存在，請勿重複建立！";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);	
			}else{
			    log.info( "設定lcNo = " + lcNo );
			    lcHead.setLcNo(lcNo);	
			}
		    }else{ 
			if( lcNo.equals(lcHead.getLcNo()) ){	// 填入值跟之前一樣,則不處理 ,仍是唯一值
			}else{	
			    List lcHeadList = imLetterOfCreditHeadDAO.findByProperty("ImLetterOfCreditHead", "lcNo", lcNo);
			    if (lcHeadList != null && lcHeadList.size() > 0) {
				message = "L/C NO：" + lcNo + "已存在，請勿重複建立！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);	
			    }else{
				log.info( "設定lcNo = " + lcNo );
				lcHead.setLcNo(lcNo);	
			    }
			}
		    }
		}
	    }

	    if (lcDate == null) {
		message = "請輸入" + tabName + "的開狀日！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);	
	    } 
	    if (!StringUtils.hasText(openingBankCode)) {
		message = "請輸入" + tabName + "的銀行！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);	
	    } else {
		openingBankCode = openingBankCode.trim().toUpperCase();
		lcHead.setOpeningBankCode(openingBankCode);
		if (buSupplierDAO.findByPrimaryKey(BuSupplier.class, new BuSupplierId(openingBankCode, brandCode)) == null){
		    message = "查無" + tabName + "的銀行供應商！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}
	    }

	    if (!StringUtils.hasText(supplierCode)) {
		message = "請輸入" + tabName + "的供應商！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);	
	    } else {
		supplierCode = supplierCode.trim().toUpperCase();
		lcHead.setSupplierCode(supplierCode);
		if (buSupplierDAO.findByPrimaryKey(BuSupplier.class, new BuSupplierId(supplierCode, brandCode)) == null){
		    message = "查無" + tabName + "的供應商！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}
	    }

	    if (!StringUtils.hasText(currencyCode)) {
		message = "請輸入" + tabName + "的幣別！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    } else {
		currencyCode = currencyCode.trim().toUpperCase();
		lcHead.setCurrencyCode(currencyCode);
		if (buCurrencyDAO.findById(currencyCode) == null){
		    message = "查無" + tabName + "的幣別！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}
	    }

	    if (openingAmount == null) {
		message = "請輸入" + tabName + "的開狀金額！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    } else if (creditNoted == null) {
//		throw new ValidationErrorException("請輸入" + tabName + "的Credit noted！"); !!!改到line
	    }

	} catch (Exception e) {
	    message = "檢核信用狀主檔單" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }

    /**
     * 檢核修狀明細檔 for 訊息提示
     * @param lcHead
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void checkLCAlters(ImLetterOfCreditHead lcHead, String programId,
	    String identification, List errorMsgs)
    throws ValidationErrorException, NoSuchObjectException {
	String message = null;
	String tabName = "修狀資料頁籤";
	Date lcDate = null;
	try {
	    log.info( "lcHead.getLcDate() = " + lcHead.getLcDate() );
	    lcDate = lcHead.getLcDate();
	    List lcAlters = lcHead.getImLetterOfCreditAlters();
	    HashMap map = new HashMap();

	    for (int i = 0; i < lcAlters.size(); i++) {
		ImLetterOfCreditAlter lcAlter = (ImLetterOfCreditAlter) lcAlters.get(i);
		Date alterLcDate = lcAlter.getAlterLcDate();
		Double alterAmount = lcAlter.getAlterAmount();

		log.info( "alterAmount = " + alterAmount );

		if (alterLcDate == null) {
		    message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的修狀日！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		} else if (lcDate != null && alterLcDate.before(lcDate)  ) {
		    message = tabName + "中第" + (i + 1) + "項明細的修狀日不可小於開狀日！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		} else if (map.get(DateUtils.format(alterLcDate)) != null) {
		    message = tabName + "中第" + (i + 1) + "項明細的修狀日重複！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		} else {
		    map.put(DateUtils.format(alterLcDate), DateUtils.format(alterLcDate));
		}

		if (alterAmount == null) {
		    message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的修狀金額！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		} else if (alterAmount == 0D) {
		    message = tabName + "中第" + (i + 1) + "項明細的修狀金額不可為零！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}
		lcAlter.setStatus(lcHead.getStatus());
	    }

	} catch (Exception e) {
	    message = "檢核修狀明細檔" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }

    /**
     * 檢核信用狀明細檔 for 訊息提示
     * @param lcHead
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void checkLCLines(ImLetterOfCreditHead lcHead, String programId,
	    String identification, List errorMsgs) throws ValidationErrorException, NoSuchObjectException {

	String message = null;
	String tabName = "明細資料頁籤";

	try{
	    List lcLines = lcHead.getImLetterOfCreditLines();

	    for (int i = 0; i < lcLines.size(); i++) {
		ImLetterOfCreditLine lcLine = (ImLetterOfCreditLine) lcLines.get(i);
		String receiveNo = lcLine.getReceiveNo();
		Double receiveAmount = lcLine.getReceiveAmount();

		if (!StringUtils.hasText(receiveNo)) {
		    message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的進貨單號！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		} else {
		    receiveNo = receiveNo.trim().toUpperCase();
		    lcLine.setReceiveNo(receiveNo);
		    // TODO:檢核進貨單號是否存在
		}

		if (receiveAmount == null) {
		    message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的進貨金額！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		} else if (receiveAmount == 0D) {
		    message = tabName + "中第" + (i + 1) + "項明細的進貨金額不可為零！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}
		// 將第一列的進貨單號放置到Head的分攤費用進貨單號
		if (i == 0) {
		    lcHead.setApportionedFeesReceiveNo(receiveNo);
		}
		lcLine.setStatus(lcHead.getStatus());
	    }

	} catch (Exception e) {
	    message = "檢核信用狀明細檔" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lcHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }

    /**
     * 更新信用狀主檔、明細檔、修狀明細檔的Status
     * 
     * @param lcHeadId
     * @param status
     * @return String
     * @throws Exception
     */
    public String updateImLetterOfCreditStatus(Long lcHeadId, String status) throws Exception {

	try {
	    ImLetterOfCreditHead lcHead = (ImLetterOfCreditHead) imLetterOfCreditHeadDAO.findByPrimaryKey(ImLetterOfCreditHead.class,
		    lcHeadId);
	    if (lcHead != null) {
		lcHead.setStatus(status);
		lcHead.setLastUpdateDate(new Date());
		List<ImLetterOfCreditLine> lcLines = lcHead.getImLetterOfCreditLines();
		List<ImLetterOfCreditAlter> lcAlters = lcHead.getImLetterOfCreditAlters();
		if (lcLines != null && lcLines.size() > 0) {
		    for (ImLetterOfCreditLine lcLine : lcLines) {
			lcLine.setStatus(status);
			lcLine.setLastUpdateDate(new Date());
		    }
		}
		if (lcAlters != null && lcAlters.size() > 0) {
		    for (ImLetterOfCreditAlter lcAlter : lcAlters) {
			lcAlter.setStatus(status);
			lcAlter.setLastUpdateDate(new Date());
		    }
		}
		modifyImLetterOfCredit(lcHead);
		return "Success";
	    } else {
		throw new NoSuchDataException("信用狀主檔查無主鍵：" + lcHeadId + "的資料！");
	    }
	} catch (Exception ex) {
	    log.error("更新信用狀狀態時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新信用狀狀態時發生錯誤，原因：" + ex.getMessage());

	}
    }

    /**
     * 前端資料塞入bean
     * @param parameterMap
     * @return
     */
    public Map updateImLetterOfCredit(Map parameterMap)throws FormException, Exception {
	Map resultMap = new HashMap();
	try{
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
//	    Object otherBean = parameterMap.get("vatBeanOther");

//	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
//	    String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");

	    //取得欲更新的bean
	    ImLetterOfCreditHead imLetterOfCreditHeadPO = this.getActualLetterOfCredit(formLinkBean); 
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imLetterOfCreditHeadPO);

	    resultMap.put("entityBean", imLetterOfCreditHeadPO);

	    return resultMap;      
	} catch (FormException fe) {
	    log.error("前端資料塞入bean失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
	    throw new Exception("信用狀存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 存取信用狀主檔,信狀明細檔,修狀明細檔,重新設定狀態
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map updateAJAXImLetterOfCredit(Map parameterMap, String formAction)throws Exception{
	Map resultMap = new HashMap();
	List errorMsgs = null;
	String resultMsg = null;
	ImLetterOfCreditHead lcHead = null;
	try {

	    Object otherBean = parameterMap.get("vatBeanOther");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String beforeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeStatus");
	    String lcNo = (String)PropertyUtils.getProperty(formLinkBean, "lcNo"); 

	    lcHead = this.getActualLetterOfCredit(formLinkBean);
	    // 從formLinkBean來,避免UK問題

	    if ( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction)  || OrderStatus.FORM_SAVE.equals(formAction) ||
		    OrderStatus.CLOSE.equals(formAction)){
		// 檢核 LcNo for暫存
		if( OrderStatus.FORM_SAVE.equals(formAction) ){
		    errorMsgs  = this.checkedLcNo(lcHead, lcNo);
		    log.info( "檢核checkedLcNo =" + errorMsgs.size() );
		}
		// 檢核 送出,背景送出
		if( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) || OrderStatus.CLOSE.equals(formAction)){
		    this.deleteAltLine(lcHead);
		    errorMsgs  = this.checkedImLetterOfCredit(parameterMap, lcNo);
		    log.info( "檢核checkedImLetterOfCredit = " + errorMsgs.size() );
		}
	    }

	    if( errorMsgs != null ){
		log.info( "errorMsgs.size() = " + errorMsgs.size() );
	    }

	    if( errorMsgs == null || errorMsgs.size() == 0 ){
		log.info( "執行updateAJAXImLetterOfCredit沒有錯誤" );
		// 成功則設定下個狀態
		this.setNextStatus(lcHead, formAction, beforeStatus);

		// 存檔
		this.insertOrUpdateImLetterOfCredit(lcHead, employeeCode);

		resultMsg = "L/C NO：" + lcHead.getLcNo() + "存檔成功！是否繼續新增？"; 
	    } else if( errorMsgs.size() > 0 ){
		if( OrderStatus.FORM_SUBMIT.equals(formAction) || OrderStatus.FORM_SAVE.equals(formAction) || OrderStatus.CLOSE.equals(formAction) ){
		    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		}
	    }
	    resultMap.put("entityBean", lcHead);
	    resultMap.put("resultMsg", resultMsg);
	} catch( ValidationErrorException ve ){
	    log.error("信用狀檢核時發生錯誤，原因：" + ve.toString());
	    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	} catch (Exception e) {
	    log.error("信用狀存檔時發生錯誤，原因：" + e.toString());
	    throw new Exception("信用狀存檔時發生錯誤，原因：" + e.getMessage());
	}
	return resultMap;
    }

    /**
     * 存取信用狀主檔,信狀明細檔,修狀明細檔,重新設定狀態
     * @param parameterMap
     * @return
     * @throws Exception
     */        
    public Object executeReverter(Long headId, String employeeCode)throws Exception{
	ImLetterOfCreditHead head = null;
	try {

	    head = findImLetterOfCreditHeadById(headId);
	    
	    // 確認是否可以反轉
	    if( !OrderStatus.CLOSE.equalsIgnoreCase(head.getStatus()) ){
		throw new Exception("品牌: " + head.getBrandCode() + " LC/NO: " + head.getLcNo() + " 狀態不為CLOSE，無法反轉");
	    }
	    log.info("status = " + head.getStatus());
	    // 設定下一個狀態
	    setReverseNextStatus(head);
	    log.info("setAfterStatus = " + head.getStatus());
	    // 存檔
	    updateImLetterOfCredit(head, employeeCode);

	    log.info("updateAfterstatus = " + head.getStatus());
	    
	    return head;
	    
	} catch (Exception e) {
	    log.error("信用狀存檔時發生錯誤，原因：" + e.toString());
	    throw new Exception("信用狀存檔時發生錯誤，原因：" + e.getMessage());
	}
    }

    /**
     * 執行反確認流程起始
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public void executeReverterProcess(Object bean) throws Exception {
	ImLetterOfCreditService.startProcess((ImLetterOfCreditHead)bean);
    }
    
    /**
     * ajax 更新修狀明細的LINE
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> updateOrSaveAJAXAltPageLinesData(
	    Properties httpRequest) throws Exception {
	try {
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    String status = httpRequest.getProperty("status");

	    if (headId == null) {
		throw new ValidationErrorException("傳入的信用狀主鍵為空值！");
	    }
	    String errorMsg = null;


	    // 將STRING資料轉成List Properties record data
	    List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, ALT_GRID_FIELD_NAMES);
	    // Get INDEX NO
	    int indexNo = imLetterOfCreditAlterDAO.findPageLineMaxIndex(headId).intValue();

	    if(OrderStatus.SAVE.equals(status) || OrderStatus.FINISH.equals(status) ){
		// 考慮狀態
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
			// 先載入HEAD_ID OR LINE DATA

			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));

			String itemCode = upRecord.getProperty(ALT_GRID_FIELD_NAMES[1]);

			if (StringUtils.hasText(itemCode)) {

			    List<ImLetterOfCreditAlter> imLetterOfCreditAlters = imLetterOfCreditAlterDAO.findByProperty("ImLetterOfCreditAlter", new String[]{ "imLetterOfCreditHead.headId", "lineId" }, new Object[]{ headId, lineId } );

			    Date date = new Date();
			    if (imLetterOfCreditAlters != null && imLetterOfCreditAlters.size() > 0 ) {
				log.info( "before isDeleteRecord(0) = " + imLetterOfCreditAlters.get(0).getIsDeleteRecord() );
				ImLetterOfCreditAlter imLetterOfCreditAlter = imLetterOfCreditAlters.get(0);
				AjaxUtils.setPojoProperties(imLetterOfCreditAlter,upRecord, ALT_GRID_FIELD_NAMES,ALT_GRID_FIELD_TYPES);
				log.info( "after isDeleteRecord(0) = " + imLetterOfCreditAlters.get(0).getIsDeleteRecord() );
				imLetterOfCreditAlter.setLastUpdatedBy(loginEmployeeCode);
				imLetterOfCreditAlter.setLastUpdateDate(date);
				imLetterOfCreditAlterDAO.update(imLetterOfCreditAlter);

			    } else {
				indexNo++;
				ImLetterOfCreditAlter imLetterOfCreditAlter = new ImLetterOfCreditAlter(); 

				AjaxUtils.setPojoProperties(imLetterOfCreditAlter,upRecord, ALT_GRID_FIELD_NAMES,ALT_GRID_FIELD_TYPES);
				imLetterOfCreditAlter.setImLetterOfCreditHead(new ImLetterOfCreditHead(headId));
				imLetterOfCreditAlter.setCreatedBy(loginEmployeeCode);
				imLetterOfCreditAlter.setCreationDate(date);
				imLetterOfCreditAlter.setLastUpdatedBy(loginEmployeeCode);
				imLetterOfCreditAlter.setLastUpdateDate(date);
				imLetterOfCreditAlter.setIndexNo(Long.valueOf(indexNo));
				imLetterOfCreditAlterDAO.save(imLetterOfCreditAlter);

			    }
			}
		    }
		}
	    }
	    return AjaxUtils.getResponseMsg(errorMsg);
	} catch (Exception ex) {
	    log.error("更新修狀明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新修狀明細失敗！");
	}
    }

    /**
     * ajax 更新信用狀明細的LINE
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> updateAJAXPageLinesData(
	    Properties httpRequest) throws Exception {
	try {
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    String status = httpRequest.getProperty("status");

	    if (headId == null) {
		throw new ValidationErrorException("傳入的信用狀主鍵為空值！");
	    }
	    String errorMsg = null;

	    // 將STRING資料轉成List Properties record data
	    List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);

	    if( OrderStatus.FINISH.equals(status) ){
		// 考慮狀態
		int i = 0;
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
			// 先載入HEAD_ID OR LINE DATA

			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));

			String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);

			if (StringUtils.hasText(itemCode)) {

			    List<ImLetterOfCreditLine> imLetterOfCreditLines = imLetterOfCreditLineDAO.findByProperty("ImLetterOfCreditLine", new String[]{ "imLetterOfCreditHead.headId", "lineId" }, new Object[]{ headId, lineId } );

			    Date date = new Date();
			    if (imLetterOfCreditLines != null && imLetterOfCreditLines.size() > 0 ) {
				ImLetterOfCreditLine imLetterOfCreditLine = imLetterOfCreditLines.get(0);
				AjaxUtils.setPojoProperties(imLetterOfCreditLine,upRecord, GRID_FIELD_NAMES,GRID_FIELD_TYPES);
				imLetterOfCreditLine.setLastUpdatedBy(loginEmployeeCode);
				imLetterOfCreditLine.setLastUpdateDate(date);
				imLetterOfCreditLineDAO.update(imLetterOfCreditLine);

			    } else {
//				throw new Exception("第" + i + "筆不存在,信用狀明細只能更新不能新增");	
			    }
			}
			i++;
		    }
		}

	    }

	    return AjaxUtils.getResponseMsg(errorMsg);
	} catch (Exception ex) {
	    log.error("更新信用狀明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新信用狀明細失敗！");
	}
    }

    /**
     *  前端資料塞入bean for 背景送出
     * 
     * @param parameterMap
     * @return Map
     * @throws FormException
     * @throws Exception
     */
    public Map updateImLetterOfCreditForBackground(Map parameterMap) throws FormException, Exception {

	Map resultMap = new HashMap();
	List errorMsgs = null;
	String resultMsg = null;
	try{
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    resultMap = this.updateImLetterOfCredit(parameterMap); 
	    ImLetterOfCreditHead imLetterOfCreditHead = (ImLetterOfCreditHead) resultMap.get("entityBean");

	    String lcNo = (String)PropertyUtils.getProperty(formLinkBean, "lcNo"); 

	    errorMsgs = this.checkedLcNoForBackground(imLetterOfCreditHead, lcNo);

	    if( errorMsgs == null || errorMsgs.size() == 0 ) {
		resultMsg = "L/C NO：" + imLetterOfCreditHead.getLcNo() + "存檔成功！是否繼續新增？";   
	    } else if( errorMsgs.size() > 0 ){
		throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	    }

	} catch (ValidationErrorException ve){
	    log.error("信用狀檢核lcNo時發生錯誤，原因：" + ve.toString());
	    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	} catch (Exception ex) {
	    log.error("信用狀存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("信用狀存檔時發生錯誤，原因：" + ex.getMessage());
	}finally{
	    resultMap.put("resultMsg", resultMsg);
	}
	return resultMap;      
    }

    /**
     * 將修狀明細表 mark 為刪除的刪掉
     * @param head
     */
    private void deleteAltLine(ImLetterOfCreditHead head){

	List<ImLetterOfCreditAlter> imLetterOfCreditAlters = head.getImLetterOfCreditAlters();
	if(imLetterOfCreditAlters != null && imLetterOfCreditAlters.size() > 0){
	    for(int i = imLetterOfCreditAlters.size() - 1; i >= 0; i--){
		ImLetterOfCreditAlter imLetterOfCreditAlter = imLetterOfCreditAlters.get(i);         
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imLetterOfCreditAlter.getIsDeleteRecord())){
		    imLetterOfCreditAlters.remove(imLetterOfCreditAlter);
		    imLetterOfCreditAlterDAO.delete(imLetterOfCreditAlter);
		}
	    }
	}
    }

    /**
     * 依據信用狀查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List<ImLetterOfCreditHead> findLetterOfCreditList(HashMap conditionMap) throws Exception {

	try {
	    String lcNo_Start = (String) conditionMap.get("lcNo_Start");
	    String lcNo_End = (String) conditionMap.get("lcNo_End");
	    String supplierCode = (String) conditionMap.get("supplierCode");
	    String poNo_Start = (String) conditionMap.get("poNo_Start");
	    String poNo_End = (String) conditionMap.get("poNo_End");
	    String openingBank = (String) conditionMap.get("openingBank");
	    String brandCode = (String) conditionMap.get("brandCode");

	    if (null != lcNo_Start)
		conditionMap.put("lcNo_Start", lcNo_Start.trim().toUpperCase());
	    if (null != lcNo_End)
		conditionMap.put("lcNo_End", lcNo_End.trim().toUpperCase());
	    if (null != supplierCode)
		conditionMap.put("supplierCode", supplierCode.trim().toUpperCase());
	    if (null != poNo_Start)
		conditionMap.put("poNo_Start", poNo_Start.trim().toUpperCase());
	    if (null != poNo_End)
		conditionMap.put("poNo_End", poNo_End.trim().toUpperCase());
	    if (null != openingBank)
		conditionMap.put("openingBank", openingBank.trim().toUpperCase());
	    if (null != brandCode)
		conditionMap.put("brandCode", brandCode.trim().toUpperCase());

	    return imLetterOfCreditHeadDAO.findLetterOfCreditList(conditionMap);
	} catch (Exception ex) {
	    log.error("查詢信用狀時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢信用狀時發生錯誤，原因：" + ex.getMessage()); 
	}
    }

    public void setPoPurchaseOrderHeadDAO(PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO) {
	this.poPurchaseOrderHeadDAO = poPurchaseOrderHeadDAO;
    }

    /*	public void setImReceiveHeadDAO(ImReceiveHeadDAO imReceiveHeadDAO) {
		this.imReceiveHeadDAO = imReceiveHeadDAO;
	}*/

    /**
     * ajax取得供應商名稱
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public  List<Properties> getAJAXSupplierName(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String brandCode = null;
	String supplierCode = null;    
	try {
	    brandCode = httpRequest.getProperty("brandCode");
	    supplierCode = httpRequest.getProperty("supplierCode");
	    supplierCode = supplierCode.trim().toUpperCase();
	    Long addressBookId = NumberUtils.getLong( httpRequest.getProperty("addressBookId") );

	    if( supplierCode != ""){
		properties.setProperty("supplierCode", supplierCode);

		BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewService.findByBrandCodeAndSupplierCode(brandCode, supplierCode);
		if(buSupplierWithAddressView != null){
		    properties.setProperty("supplierName", buSupplierWithAddressView.getChineseName());
		} else {
		    properties.setProperty("supplierName", "查無此供應商");
		}
	    }else if( addressBookId != 0L ){
		BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewService.findSupplierByAddressBookIdAndBrandCode(addressBookId, brandCode);
		if(buSupplierWithAddressView != null){
		    properties.setProperty("supplierCode", buSupplierWithAddressView.getSupplierCode());
		    properties.setProperty("supplierName", buSupplierWithAddressView.getChineseName());
		} else {
		    properties.setProperty("supplierName", "查無此供應商");
		}
	    }

	    result.add(properties);	

	    return result;	        
	} catch (Exception ex) {
	    log.error("依據品牌代號：" + brandCode + "、供應商號：" + supplierCode + "查詢時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢供應商號資料失敗！");
	}        
    }

    /**
     * ajax 取得幣別名稱
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public  List<Properties> getAJAXCurrencyName(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String currencyCode = null;
	try {
	    currencyCode = httpRequest.getProperty("currencyCode").trim().toUpperCase();

	    properties.setProperty("currencyCode", currencyCode);

	    BuCurrency currency = buBasicDataService.findCurrencyById(currencyCode);
	    if(currency != null){
		properties.setProperty("currencyName", currency.getCurrencyCName());
	    }else{
		properties.setProperty("currencyName", "查無此幣別名稱");
	    }

	    result.add(properties);	

	    return result;	        
	} catch (Exception ex) {
	    log.error("依據幣別代號：" + currencyCode + "查詢時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢幣別代號資料失敗！");
	}        
    }

    /**
     *  信用狀明細,動態改變進貨金額
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public  List<Properties> getAJAXReceiveAmount(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	Double receiveAmount = 0.0D;
	Double creditAmount = 0.0D;
	try {
	    receiveAmount = NumberUtils.getDouble( httpRequest.getProperty("receiveAmount") );
	    creditAmount = NumberUtils.getDouble( httpRequest.getProperty("creditAmount") );

	    properties.setProperty("returnAmount", NumberUtils.roundToStr((receiveAmount - creditAmount), 4 ));

	    result.add(properties);	

	    return result;	        
	} catch (Exception ex) {
	    log.error("進貨金額：" + receiveAmount + ",折讓金額:" + creditAmount +" 計算時發生錯誤，原因：" + ex.toString());
	    throw new Exception("金額計算失敗！");
	}        
    }

    /**
     * ajax 載入修狀明細時,取得分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public  List<Properties> getAJAXAltPageData(Properties httpRequest) throws Exception{
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));

	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小


	    List<ImLetterOfCreditAlter> imLetterOfCreditAlters = imLetterOfCreditAlterDAO.findPageLine(headId, iSPage, iPSize); 

	    HashMap map = new HashMap();
	    map.put("headId", headId);

	    if (imLetterOfCreditAlters != null && imLetterOfCreditAlters.size() > 0) {
//		log.info( "修改金額  = " + imLetterOfCreditAlters.get(0).getAlterAmount() );

		// 取得第一筆的INDEX
		Long firstIndex = imLetterOfCreditAlters.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		Long maxIndex = imLetterOfCreditAlterDAO.findPageLineMaxIndex(headId);  
		result.add(AjaxUtils.getAJAXPageData(httpRequest,ALT_GRID_FIELD_NAMES, ALT_GRID_FIELD_DEFAULT_VALUES,imLetterOfCreditAlters, gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,ALT_GRID_FIELD_NAMES, ALT_GRID_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的修狀明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的修狀明細失敗！");
	}   
    }

    /**
     * ajax 載入信用狀明細時,取得分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));

	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小


	    List<ImLetterOfCreditLine> imLetterOfCreditLines = imLetterOfCreditLineDAO.findPageLine(headId, iSPage, iPSize);  

	    HashMap map = new HashMap();
	    map.put("headId", headId);

	    if (imLetterOfCreditLines != null && imLetterOfCreditLines.size() > 0) {

		// 取得第一筆的INDEX
		Long firstIndex = imLetterOfCreditLines.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		Long maxIndex = imLetterOfCreditLineDAO.findPageLineMaxIndex(headId);  
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,imLetterOfCreditLines, gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的信用狀明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的信用狀明細失敗！");
	}   
    }

    /**
     * ajax 取得信用狀search分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{

	try{
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    //======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
	    String lcNoStart = httpRequest.getProperty("lcNoStart" );
	    String lcNoEnd = httpRequest.getProperty("lcNoEnd");
	    Date lcDateStart = DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("lcDateStart") );
	    Date lcDateEnd = DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("lcDateEnd") );

	    String status = httpRequest.getProperty("status");
	    String supplierCode = httpRequest.getProperty("supplierCode");
	    String poNoStart = httpRequest.getProperty("poNoStart");
	    String poNoEnd = httpRequest.getProperty("poNoEnd");
	    String openingBank = httpRequest.getProperty("openingBank");

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();

	    findObjs.put(" and model.lcNo IS NOT NULL and model.brandCode = :brandCode",brandCode);
	    findObjs.put(" and model.lcNo >= :lcNoStart",lcNoStart);
	    findObjs.put(" and model.lcNo <= :lcNoEnd",lcNoEnd);
	    findObjs.put(" and model.lcDate >= :lcDateStart",lcDateStart);
	    findObjs.put(" and model.lcDate <= :lcDateEnd",lcDateEnd);
	    findObjs.put(" and model.status = :status",status);
	    findObjs.put(" and model.supplierCode = :supplierCode",supplierCode);
	    findObjs.put(" and model.poNo >= :poNoStart",poNoStart);
	    findObjs.put(" and model.poNo <= :poNoEnd",poNoEnd);
	    findObjs.put(" and model.openingBank like :openingBank","%"+openingBank+"%");

	    //==============================================================	    

	    Map imLetterOfCreditHeadMap = imLetterOfCreditHeadDAO.search( "ImLetterOfCreditHead as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	    List<ImLetterOfCreditHead> imLetterOfCreditHeads = (List<ImLetterOfCreditHead>) imLetterOfCreditHeadMap.get(BaseDAO.TABLE_LIST); 

	    if (imLetterOfCreditHeads != null && imLetterOfCreditHeads.size() > 0) {

		this.setImLetterOfCreditStatusName(imLetterOfCreditHeads);

		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		Long maxIndex = (Long)imLetterOfCreditHeadDAO.search("ImLetterOfCreditHead as model", "count(model.headId) as rowCount" ,findObjs, "order by lastUpdateDate desc", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,imLetterOfCreditHeads, gridDatas, firstIndex, maxIndex));
	    }else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的信用狀查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的信用狀查詢失敗！");
	}	
    }

    /**
     * picker按檢視返回選取的資料
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception{
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);

	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    if(result.size() > 0 ){
		pickerResult.put("result", result);
	    }
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel",  new String[]{"vatBeanPicker"});
	}catch(Exception ex){
	    log.error("檢視失敗，原因：" + ex.toString());
	    Map messageMap = new HashMap();
	    messageMap.put("type"   , "ALERT");
	    messageMap.put("message", "檢視失敗，原因："+ex.toString());
	    messageMap.put("event1" , null);
	    messageMap.put("event2" , null);
	    resultMap.put("vatMessage",messageMap);

	}
	return AjaxUtils.parseReturnDataToJSON(resultMap);
    }

    /**
     * 透過headId取得信用狀
     * @param bean
     * @return
     * @throws FormException
     * @throws Exception
     */
    public ImLetterOfCreditHead getActualLetterOfCredit(Object otherBean ) throws FormException, Exception{

	ImLetterOfCreditHead imLetterOfCreditHead = null;
	String id = (String)PropertyUtils.getProperty(otherBean, "headId");

	if(StringUtils.hasText(id)){
	    Long headId = NumberUtils.getLong(id);
	    imLetterOfCreditHead = this.findImLetterOfCreditHeadById(headId);
	    if(imLetterOfCreditHead  == null){
		throw new NoSuchObjectException("查無信用狀主鍵：" + headId + "的資料！");
	    }
	}else{
	    throw new ValidationErrorException("傳入的信用狀主鍵為空值！");
	}
	return imLetterOfCreditHead;

    }

    /**
     * 透過formId取得實際信用狀 in initial  
     * @param otherBean
     * @param resultMap
     * @return
     * @throws Exception
     */
    private ImLetterOfCreditHead getActualHead(Object otherBean, Map resultMap) throws Exception{
	ImLetterOfCreditHead imLetterOfCreditHead = null;
	try {
	    String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
	    Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;

	    imLetterOfCreditHead = null == formId ? this.executeNew(otherBean) : this.findImLetterOfCreditHeadById(formId);

	} catch (Exception e) {
	    log.error("取得實際信用狀主檔失敗,原因:"+e.toString());
	    throw new Exception("取得實際信用狀主檔失敗,原因:"+e.toString());
	}
	return imLetterOfCreditHead;
    }

    /**
     * 取得訊息提示用
     * @param headId
     * @return
     * @throws Exception
     */
    public String getIdentification(Long headId) throws Exception{

	String id = null;
	try{
	    ImLetterOfCreditHead imLetterOfCreditHead = (ImLetterOfCreditHead)imLetterOfCreditHeadDAO.findByPrimaryKey(ImLetterOfCreditHead.class, headId);
	    if(imLetterOfCreditHead != null){
		id = MessageStatus.getIdentification(imLetterOfCreditHead.getBrandCode(), 
			LC_ID, imLetterOfCreditHead.getReserve5());
	    }else{
		throw new NoSuchDataException("信用狀主檔查無主鍵：" + headId + "的資料！");
	    }

	    return id;
	}catch(Exception ex){
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
	}	   
    }

    /**
     * 取得信用狀其他欄位(供應商名稱,幣別名稱,還款金額合計,還款手續費合計)
     * @param head
     * @param loginBrandCode
     * @return
     */
    private Map getOtherColumn(ImLetterOfCreditHead head, String loginBrandCode)throws Exception{
	Map resultMap = new HashMap();
	Double totalReturnAmount = 0.0D;
	Double totalReturnFees = 0.0D;
	try{

	    // 銀行供應商
	    String openingBankCode = head.getOpeningBankCode();
	    if(StringUtils.hasText( openingBankCode ) ){
		BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewService.findByBrandCodeAndSupplierCode(loginBrandCode, openingBankCode);
		if(buSupplierWithAddressView != null){
		    resultMap.put("openingBank", buSupplierWithAddressView.getChineseName());
		}else {
		    resultMap.put("openingBank", "查無此銀行供應商");
		}
	    } else {
//		resultMap.put("openingBank", ""); 怕造成百貨的name被清掉
	    }

	    // 廠商供應商
	    String supplierCode = head.getSupplierCode();
	    if(StringUtils.hasText( supplierCode) ){
		BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewService.findByBrandCodeAndSupplierCode(loginBrandCode, supplierCode);
		if(buSupplierWithAddressView != null){
		    resultMap.put("supplierName", buSupplierWithAddressView.getChineseName());
		}else {
		    resultMap.put("supplierName", "查無此廠商供應商");
		}
	    } else {
		resultMap.put("supplierName", "");
	    }

	    // 幣別
	    String currencyCode = head.getCurrencyCode();
	    if(StringUtils.hasText( currencyCode)){
		BuCurrency currency = buBasicDataService.findCurrencyById(currencyCode);
		if(currency != null){
		    resultMap.put("currencyName", currency.getCurrencyCName());
		}else{
		    resultMap.put("currencyName", "查無此幣別資料");
		}
	    } else {
		resultMap.put("currencyName", "");
	    }

	    List<ImLetterOfCreditLine> imLetterOfCreditLines = head.getImLetterOfCreditLines();

	    for (ImLetterOfCreditLine imLetterOfCreditLine : imLetterOfCreditLines) {
		// 加總還款金額
		totalReturnAmount  += NumberUtils.getDouble( imLetterOfCreditLine.getReturnAmount() );
		// 加總還款手續費
		totalReturnFees += NumberUtils.getDouble( imLetterOfCreditLine.getReturnFees() );
	    }
	    resultMap.put("totalReturnAmount", totalReturnAmount);
	    resultMap.put("totalReturnFees", totalReturnFees);

	}catch(Exception ex){
	    log.error("取得信用狀其他欄位時發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得信用狀其他欄位時發生錯誤，原因：" + ex.toString());

	}
	return resultMap;
    }

}
