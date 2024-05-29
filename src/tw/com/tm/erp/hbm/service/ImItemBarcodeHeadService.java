package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.Imformation;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemBarcodeHead;
import tw.com.tm.erp.hbm.bean.ImItemBarcodeLine;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.ImItemBarcodeHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemBarcodeLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class ImItemBarcodeHeadService  { 
    private static final Log log = LogFactory.getLog(ImItemBarcodeHeadService.class);
    
    public static final String PROGRAM_ID= "IM_ITEM_BARCODE";
    
    private BaseDAO baseDAO;
    private BuBrandDAO buBrandDAO;
    private ImItemBarcodeHeadDAO imItemBarcodeHeadDAO;
    private ImItemBarcodeLineDAO imItemBarcodeLineDAO;
    private ImItemDAO imItemDAO;
    private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
    
    private BuOrderTypeService buOrderTypeService;
    
    private SiProgramLogAction siProgramLogAction;
    

    public void setImItemCurrentPriceViewDAO(ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
        this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
    }

    public void setImItemBarcodeHeadDAO(ImItemBarcodeHeadDAO imItemBarcodeHeadDAO) {
        this.imItemBarcodeHeadDAO = imItemBarcodeHeadDAO;
    }

    public void setImItemDAO(ImItemDAO imItemDAO) {
        this.imItemDAO = imItemDAO;
    }

    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
        this.siProgramLogAction = siProgramLogAction;
    }

    public void setImItemBarcodeLineDAO(ImItemBarcodeLineDAO imItemBarcodeLineDAO) {
        this.imItemBarcodeLineDAO = imItemBarcodeLineDAO;
    }

    public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
        this.buOrderTypeService = buOrderTypeService;
    }

    public void setBaseDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
        this.buBrandDAO = buBrandDAO;
    }
    
    /**
     * 補條碼明細欄位
     */
    public static final String[] GRID_FIELD_NAMES = { 
	"indexNo", "itemCode", "itemCName", 
	"unitPrice", "paper", "lineId",
	"isLockRecord", "isDeleteRecord", "message"
    };
    
    /**
     * 補條碼明細預設值
     */
    public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
	"", "", "",
	"", "", "",
	AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };      
    
    /**
     * 補條碼明細對應型態
     */
    public static final int[] GRID_FIELD_TYPES = { 
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, 
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_LONG,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE
    };
    
    /**
     * 補條碼查詢picker用的欄位
     */
    public static final String[] GRID_SEARCH_FIELD_NAMES = { 
	"orderNo", "dueDate", 			"statusName", 
	"lastUpdateDate", "orderTypeCode", 	"headId"
    };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
	"", "", "",
	"", "", ""
    };
    
    /**
     * 檢核補條碼主檔,明細檔
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List checkedBarcode(Map parameterMap )throws FormException, Exception{
	List errorMsgs = new ArrayList(0);
	String message = null;
	String identification = null;
	ImItemBarcodeHead head = null;
	try{

	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    head = getActualHead(formLinkBean);

	    String status = head.getStatus();

	    if ( OrderStatus.SAVE.equals(status) ) {

		identification = MessageStatus.getIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());

		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

		validateBarcode( head, PROGRAM_ID, identification, errorMsgs); 
	    }

	}catch (Exception ex) {
	    message = Imformation.REPLENISH_BARCODE.getDescription()+"檢核失敗，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
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
	    log.error(Imformation.REPLENISH_BARCODE.getServiceCompleteAssignment()+ ex.toString());
	    throw new ProcessFailedException(Imformation.REPLENISH_BARCODE.getServiceCompleteAssignment()+ ex.getMessage());
	}
    }
    
    /**
     * 將補條碼明細表 mark 為刪除的刪掉
     * @param head
     */
    private void deleteLine(ImItemBarcodeHead head){

	List<ImItemBarcodeLine> imItemBarcodeLines = head.getImItemBarcodeLines();
	if(imItemBarcodeLines != null && imItemBarcodeLines.size() > 0){
	    for(int i = imItemBarcodeLines.size() - 1; i >= 0; i--){
		ImItemBarcodeLine imItemBarcodeLine = imItemBarcodeLines.get(i);         
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imItemBarcodeLine.getIsDeleteRecord())){
		    imItemBarcodeLines.remove(imItemBarcodeLine);
		}
	    }
	}
    }
    
    /**
     * 匯入補條碼明細
     * @param headId
     * @param lineLists
     * @throws Exception
     */
    public void executeImportLists(Long headId, List lineLists) throws Exception{
	try{
	    log.info(" headId = " + headId);
	    ImItemBarcodeHead head = executeFind(headId);
	    head.setImItemBarcodeLines(new ArrayList(0));
	    imItemBarcodeLineDAO.update(head);

	    if(lineLists != null && lineLists.size() > 0){
		ImItemBarcodeHead headTmp = new ImItemBarcodeHead();
		headTmp.setHeadId(headId);
		
		for(int i = 0; i < lineLists.size(); i++){
		    ImItemBarcodeLine  line = (ImItemBarcodeLine)lineLists.get(i);
		    line.setImItemBarcodeHead(headTmp);
		    line.setIndexNo(i+1L);
		    imItemBarcodeLineDAO.save(line);
		}      	    
	    }     	
	}catch (Exception ex) {
	    log.error(Imformation.REPLENISH_BARCODE.getServiceImport() + ex.toString());
	    throw new Exception(Imformation.REPLENISH_BARCODE.getServiceImport() + ex.getMessage());
	}        
    }
    
    /**
     * 建立一個初始化的bean
     * @param otherBean
     * @return
     * @throws Exception
     */
    public ImItemBarcodeHead executeNew(Object otherBean) throws Exception{
	ImItemBarcodeHead head = new ImItemBarcodeHead();
	try {
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");

	    head.setOrderTypeCode(orderTypeCode);
	    head.setBrandCode(loginBrandCode);
	    head.setStatus( OrderStatus.SAVE );
	    head.setDueDate( null );

	    head.setCreatedBy(loginEmployeeCode);
	    head.setCreationDate(new Date());
	    head.setLastUpdatedBy(loginEmployeeCode);
	    head.setLastUpdateDate(new Date());

	    String tmpOrderNo = AjaxUtils.getTmpOrderNo();
	    head.setOrderNo(tmpOrderNo); 
	    baseDAO.save(head);	   

	} catch (Exception e) {
	    log.error("建立新"+Imformation.REPLENISH_BARCODE.getDescription()+"主檔失敗,原因:"+e.toString());
	    throw new Exception("建立新"+Imformation.REPLENISH_BARCODE.getDescription()+"主檔失敗,原因:"+e.toString());
	}
	return head;
    }
    
    /**
     * 依formId查詢出bean
     * @param formId
     * @return
     * @throws Exception
     */
    public ImItemBarcodeHead executeFind(Long formId) throws Exception {
	try {
	    ImItemBarcodeHead head = (ImItemBarcodeHead) baseDAO.findByPrimaryKey(ImItemBarcodeHead.class,formId);
	    return head;
	} catch (Exception ex) {
	    log.error("依據主鍵：" + formId + "查詢"+Imformation.REPLENISH_BARCODE.getDescription()+"主檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據主鍵：" + formId + "查詢"+Imformation.REPLENISH_BARCODE.getDescription()+"主檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 初始化 bean 額外顯示欄位
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
	log.info("==========<executeInitial>================");
	Map resultMap = new HashMap();
	try{
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");

	    ImItemBarcodeHead imItemBarcodeHead = getActualHead(otherBean, resultMap);
	    
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);

	    Map multiList = new HashMap(0);
	    resultMap.put("brandName",buBrandDAO.findById(imItemBarcodeHead.getBrandCode()).getBrandName());
	    resultMap.put("statusName", OrderStatus.getChineseWord(imItemBarcodeHead.getStatus()));
	    resultMap.put("createByName", employeeName);

	    String orderTypeCode = imItemBarcodeHead.getOrderTypeCode();
	    log.info("orderTypeCode = "+orderTypeCode);

	    List<BuOrderType> allOrderTypeCodes = buOrderTypeService.findOrderbyType(imItemBarcodeHead.getBrandCode() ,"IT");
	    multiList.put("allOrderTypeCodes"	, AjaxUtils.produceSelectorData(allOrderTypeCodes, "orderTypeCode", "name", true, true, orderTypeCode != null ? orderTypeCode : "" ));
	    
	    resultMap.put("multiList",multiList);
	    log.info("==========<executeInitial/>================");
	    return resultMap;
	}catch(Exception ex){
	    log.error(Imformation.REPLENISH_BARCODE.getServiceInitial() + ex.toString());
	    throw new Exception(Imformation.REPLENISH_BARCODE.getServiceInitial() + ex.toString());

	}
    }
    
    /**
     * 查詢補條碼初始化
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);

	try{
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");

	    List<BuOrderType> allOrderTypeCodes = buOrderTypeService.findOrderbyType(loginBrandCode ,"IT");

	    Map multiList = new HashMap(0);
	    multiList.put("allOrderTypeCodes"	, AjaxUtils.produceSelectorData(allOrderTypeCodes, "orderTypeCode", "name", true, true, orderTypeCode != null ? orderTypeCode : "" ));

	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error(Imformation.REPLENISH_BARCODE.getServiceSearchInitial() + ex.toString());
	    throw new Exception(Imformation.REPLENISH_BARCODE.getServiceSearchInitial() + ex.getMessage());

	}
    }
    
    /**
     * 依formId取得實際主檔 in initial  
     * @param otherBean
     * @param resultMap
     * @return
     * @throws Exception
     */
    protected ImItemBarcodeHead getActualHead(Object otherBean, Map resultMap) throws Exception{
	ImItemBarcodeHead head = null;
	try {
	    String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
	    Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
	    
	    head = (null == formId) ? executeNew(otherBean) : executeFind(formId);
	    resultMap.put("form", head);
	} catch (Exception e) {
	    log.error("取得實際主檔失敗,原因:"+e.toString());
	    throw new Exception("取得實際主檔失敗,原因:"+e.toString());
	}
	return head;
    }
    
    /**
     * 透過headId取得補條碼
     * @param bean
     * @return
     * @throws FormException
     * @throws Exception
     */
    public ImItemBarcodeHead getActualHead(Object formLinkBean ) throws FormException, Exception{

	ImItemBarcodeHead head = null;
	String id = (String)PropertyUtils.getProperty(formLinkBean, "headId");

	if(StringUtils.hasText(id)){
	    Long headId = NumberUtils.getLong(id);
	    head = (ImItemBarcodeHead)imItemBarcodeHeadDAO.findByPrimaryKey(ImItemBarcodeHead.class, headId);
	    if(null == head){
		throw new NoSuchObjectException("查無"+Imformation.REPLENISH_BARCODE.getDescription()+"主鍵：" + headId + "的資料！");
	    }
	}else{
	    throw new ValidationErrorException("傳入的"+Imformation.REPLENISH_BARCODE.getDescription()+"主鍵為空值！");
	}
	return head;

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
	    ImItemBarcodeHead head = (ImItemBarcodeHead)imItemBarcodeHeadDAO.findByPrimaryKey(ImItemBarcodeHead.class, headId);
	    if(head != null){
		id = MessageStatus.getIdentification(head.getBrandCode(), 
			head.getOrderTypeCode(), head.getOrderNo());
	    }else{
		throw new NoSuchDataException(Imformation.REPLENISH_BARCODE.getDescription()+"主檔查無主鍵：" + headId + "的資料！");
	    }

	    return id;
	}catch(Exception ex){
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
	}	   
    }
    
    /**
     * 取得檢視回來的資料
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
	    if(result.size() > 0){
		pickerResult.put("resultBarcode", result);
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
     * ajax 第一次載入明細時,取得分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String brandCode = httpRequest.getProperty("brandCode");
	    
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    HashMap findObjs = new HashMap();
	    findObjs.put("and imItemBarcodeHead.headId = :headId", headId);

	    Map searchMap = baseDAO.search("ImItemBarcodeLine", findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE); 
	    List<ImItemBarcodeLine> lines = (List<ImItemBarcodeLine>)searchMap.get(BaseDAO.TABLE_LIST); 

	    HashMap map = new HashMap();
	    map.put("headId", headId);
	    if (lines != null && lines.size() > 0) {

		// 設定明細其他欄位
		setLineOtherColumns(lines, brandCode);
		
		// 取得第一筆的INDEX
		Long firstIndex = lines.get(0).getIndexNo(); 
		// 取得最後一筆 INDEX 
		Long maxIndex = (Long)baseDAO.search("ImItemBarcodeLine", "count(imItemBarcodeHead.headId) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX  
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,lines, gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error(Imformation.REPLENISH_BARCODE.getServiceAJAXPageData() + ex.toString());
	    throw new Exception(Imformation.REPLENISH_BARCODE.getServiceAJAXPageData() + ex.toString());
	}   
    }
    
    /**
     * ajax 取得補條碼search分頁
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
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 品牌代號

	    String orderNo = httpRequest.getProperty("orderNo" );
	    Date dueDateStart = DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("dueDateStart") );
	    Date dueDateEnd = DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("dueDateEnd") );

	    String status = httpRequest.getProperty("status");

	    log.info( "brandCode = " + brandCode );
	    log.info( "orderTypeCode = " + orderTypeCode );
	    log.info( "orderNo = " + orderNo );
	    log.info( "dueDateStart = " + dueDateStart );
	    log.info( "dueDateEnd = " + dueDateEnd );
	    log.info( "status = " + status );
	    
	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and brandCode = :brandCode",brandCode);
	    findObjs.put(" and orderTypeCode = :orderTypeCode",orderTypeCode);
	    findObjs.put(" and orderNo NOT LIKE :TMP","TMP%");
	    findObjs.put(" and orderNo = :orderNo",orderNo);
	    findObjs.put(" and dueDate >= :dueDateStart",dueDateStart);
	    findObjs.put(" and dueDate <= :dueDateEnd",dueDateEnd);
	    findObjs.put(" and status = :status",status);
	    //==============================================================	    

	    log.info( "findObjs = " + findObjs );
	    Map headMap = imItemBarcodeHeadDAO.search( "ImItemBarcodeHead", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	    log.info( "headMap = " + headMap );
	    List<ImItemBarcodeHead> heads = (List<ImItemBarcodeHead>) headMap.get(BaseDAO.TABLE_LIST); 

	    log.info( "heads = " + heads );
	    if (heads != null && heads.size() > 0) {
		log.info( "1 = 1" );
		setOtherColumns(heads);
		log.info( "1 = 2" );
		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		log.info( "1 = 3" );
		Long maxIndex = (Long)imItemBarcodeHeadDAO.search("ImItemBarcodeHead", "count(headId) as rowCount" ,findObjs, "order by lastUpdateDate desc", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

		log.info( "firstIndex = " + firstIndex );
		log.info( "maxIndex = " + maxIndex );
		
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,heads, gridDatas, firstIndex, maxIndex));
	    }else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	}catch(Exception ex){
	    log.error(Imformation.REPLENISH_BARCODE.getServiceAJAXSearchPageData() + ex.toString());
	    throw new Exception(Imformation.REPLENISH_BARCODE.getServiceAJAXSearchPageData() + ex.getMessage());
	}	
    }
    
    /**
     * 若是暫存單號,則取得新單號
     * @param head
     */
    private void setOrderNo(ImItemBarcodeHead head) throws ObtainSerialNoFailedException{
	String orderNo = head.getOrderNo();
	if (AjaxUtils.isTmpOrderNo(orderNo)) {
	    try {
		String serialNo = buOrderTypeService.getOrderSerialNo(head.getBrandCode(), head.getOrderTypeCode());
		if ("unknow".equals(serialNo)) 
		    throw new ObtainSerialNoFailedException("取得" + head.getBrandCode() + "-" + head.getOrderTypeCode() + "單號失敗！");
		else
		    head.setOrderNo(serialNo);
	    } catch (Exception ex) {
		throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode() + "單號失敗！");
	    }
	}
    }
    
    /**
     * 取得下一個狀態
     * @param head
     * @param formAction
     * @param taxType
     * @param approvalResult
     */
    public void setNextStatus(ImItemBarcodeHead head, String formAction){
	if(OrderStatus.FORM_SAVE.equals(formAction)){
	    head.setStatus(OrderStatus.SAVE);
	}else if(OrderStatus.FORM_VOID.equals(formAction)){
	    head.setStatus(OrderStatus.VOID);
	}
	if( (OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction))){
	    if( OrderStatus.SAVE.equals(head.getStatus()) ){
		head.setStatus(OrderStatus.FINISH);
	    }
	}
    }
    
    /**
     * 設定中文狀態名稱
     * @param imAdjustmentHeads
     */
    private void setOtherColumns(List<ImItemBarcodeHead> imItemBarcodeHeads){
	for (ImItemBarcodeHead imItemBarcodeHead : imItemBarcodeHeads) {
	    imItemBarcodeHead.setStatusName(OrderStatus.getChineseWord(imItemBarcodeHead.getStatus()));
	}
    }
    
    /**
     * 設定其他名稱
     * @param imAdjustmentHeads
     */
    private void setLineOtherColumns(List<ImItemBarcodeLine> imItemBarcodeLines, String brandCode)throws Exception{
	for (ImItemBarcodeLine imItemBarcodeLine : imItemBarcodeLines) {
	    String itemCode = imItemBarcodeLine.getItemCode();
	    ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findOneCurrentPriceByProperty(brandCode, "1", itemCode);
	    if(null == imItemCurrentPriceView){
		throw new Exception("查品牌:"+brandCode+ " 品號:"+ itemCode);
	    }
	    imItemBarcodeLine.setItemCName(imItemCurrentPriceView.getItemCName());
	    imItemBarcodeLine.setUnitPrice(imItemCurrentPriceView.getUnitPrice());
	}
    }
    
    /**
     * 啟動流程
     * @param form
     * @return
     * @throws ProcessFailedException
     */
    public static Object[] startProcess(ImItemBarcodeHead head) throws ProcessFailedException{       
	String packageId = null;
	String processId = null;
	String version = null;
	String sourceReferenceType = null;
	try{           
	    packageId = "Im_ItemBarcode";         
	    processId = "process";           
	    version = "20100611";
	    sourceReferenceType = "Barcode";
	    
	    HashMap context = new HashMap();	    
	    context.put("formId", head.getHeadId());
	    return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
	}catch (Exception ex){
	    log.error(Imformation.REPLENISH_BARCODE.getServiceStartProcess() + ex.toString());
	    throw new ProcessFailedException(Imformation.REPLENISH_BARCODE.getServiceStartProcess() + ex.getMessage());
	}	      
    }
    
    /**
     * 將補條碼主檔查詢結果存檔
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
     * ajax  更新補條碼明細
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception{ 
	String errorMsg = null;
	try {
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    String status = httpRequest.getProperty("status");

	    if (headId == null) {
		throw new ValidationErrorException("傳入的主鍵為空值！");
	    }

	    // 將STRING資料轉成List Properties record data
	    List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
	    // Get INDEX NO
	    Map findObjs = new HashMap();
	    findObjs.put("and imItemBarcodeHead.headId = :headId", headId);
	    int indexNo = ((Long) baseDAO.search("ImItemBarcodeLine ", "count(imItemBarcodeHead.headId) as rowCount", findObjs, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT)).intValue(); // 取得最後一筆 INDEX
	    log.info( "MaxIndexNo = " + indexNo );
	    
	    // 考慮狀態
	    if(OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)){
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
			// 先載入HEAD_ID OR LINE DATA

			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
			String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);

			if (StringUtils.hasText(itemCode)) {
			    ImItemBarcodeLine line = (ImItemBarcodeLine)imItemBarcodeLineDAO.findFirstByProperty("ImItemBarcodeLine", "and imItemBarcodeHead.headId = ? and lineId = ?", new Object[]{ headId, lineId } );
			    log.info( "line = " + line + "\nlineId = " + lineId);
			    Date date = new Date();
			    
			    if ( line != null ) {
				log.info( "更新 = " + headId + " | "+ lineId  );
				AjaxUtils.setPojoProperties(line,upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
				line.setLastUpdatedBy(loginEmployeeCode);
				line.setLastUpdateDate(date);
				imItemBarcodeLineDAO.update(line);

			    } else {
				indexNo++;
				log.info( "新增 = " + headId + " | "+  indexNo);
				line = new ImItemBarcodeLine(); 

				AjaxUtils.setPojoProperties(line,upRecord, GRID_FIELD_NAMES,GRID_FIELD_TYPES);
				line.setImItemBarcodeHead((new ImItemBarcodeHead(headId)));
				line.setCreatedBy(loginEmployeeCode);
				line.setCreationDate(date);
				line.setLastUpdatedBy(loginEmployeeCode);
				line.setLastUpdateDate(date);
				line.setIndexNo(Long.valueOf(indexNo));
				imItemBarcodeLineDAO.save(line);

			    }
			} // 含品號則處理
		    }
		}
	    } // 駁回或暫存可修改
	    return AjaxUtils.getResponseMsg(errorMsg);

	} catch (Exception ex) {
	    log.error(Imformation.REPLENISH_BARCODE.getServiceUpdateAJAXPageLinesData() + ex.toString());
	    throw new Exception(Imformation.REPLENISH_BARCODE.getServiceUpdateAJAXPageLinesData() + ex.getMessage());
	}   
    }

    /**
     * 更新補條碼主檔明細檔
     * @param head
     * @param employeeCode
     */
    private void updateBarcode(ImItemBarcodeHead head, String employeeCode )throws Exception{
	try{
	    Date date = new Date();
	    if (head.getHeadId() != null) {
		head.setLastUpdateDate(date);
		head.setLastUpdatedBy(employeeCode);
		
		List<ImItemBarcodeLine> lines = head.getImItemBarcodeLines();
		for (ImItemBarcodeLine imItemBarcodeLine : lines) {
		    imItemBarcodeLine.setLastUpdateDate(date);
		    imItemBarcodeLine.setLastUpdatedBy(employeeCode);
		}
		imItemBarcodeHeadDAO.update(head);
	    }
	}catch (Exception ex) {
	    log.error(Imformation.REPLENISH_BARCODE.getDescription()+"主檔明細檔存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception(Imformation.REPLENISH_BARCODE.getDescription()+"主檔明細檔存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 前端資料塞入bean
     * @param parameterMap
     * @return
     */
    public Map updateBean(Map parameterMap)throws FormException, Exception {
	Map resultMap = new HashMap();
	try{
//	    Object otherBean = parameterMap.get("vatBeanOther");
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    //取得欲更新的bean
	    ImItemBarcodeHead head = getActualHead(formLinkBean); 
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, head);
	    resultMap.put("entityBean", head);
	    return resultMap;      
	} catch (FormException fe) {
	    log.error("前端資料塞入bean失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
	    throw new Exception(Imformation.REPLENISH_BARCODE.getDescription()+"資料塞入bean發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 檢核,存取調整單主檔,明細檔,重新設定狀態
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map updateAJAXBarcode(Map parameterMap)throws Exception{
	Map resultMap = new HashMap();
	List errorMsgs = null;
	String resultMsg = null;
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

	    ImItemBarcodeHead head = getActualHead(formLinkBean);

	    // 檢核
	    if( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ){
		deleteLine(head); 
		errorMsgs  = checkedBarcode(parameterMap);
	    }

	    if( errorMsgs == null || errorMsgs.size() == 0 ){
		// 設定單號
		setOrderNo(head);
		// 成功則設定下個狀態 
		setNextStatus(head, formAction);

		// 更新補條碼主檔明細檔  
		if( ( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) )   ){
		    // 更新調整單主檔明細檔
		    updateBarcode(head, employeeCode );
		}
		resultMsg = head.getOrderTypeCode() + "-" + head.getOrderNo() + "存檔成功！ 是否繼續新增？";

	    } else if( errorMsgs.size() > 0 ){
		if( OrderStatus.FORM_SUBMIT.equals(formAction) ){ 
		    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		}
	    }
	    resultMap.put("entityBean", head);
	    resultMap.put("resultMsg", resultMsg);
	} catch( ValidationErrorException ve ){
	    log.error(Imformation.REPLENISH_BARCODE.getDescription()+"檢核時發生錯誤，原因：" + ve.toString());
	    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	} catch (Exception e) {
	    log.error(Imformation.REPLENISH_BARCODE.getDescription()+"存檔時發生錯誤，原因：" + e.toString());
	    throw new Exception(Imformation.REPLENISH_BARCODE.getDescription()+"存檔時發生錯誤，原因：" + e.getMessage());
	}

	return resultMap;
    }
    
    /**
     *  取單號後更新更新主檔
     * 
     * @param parameterMap
     * @return Map
     * @throws FormException
     * @throws Exception
     */
    public Map updateImAdjustmentWithActualOrderNO(Map parameterMap) throws FormException, Exception {

	Map resultMap = new HashMap();
	try{

	    resultMap = updateBean(parameterMap); 
	    ImItemBarcodeHead head = (ImItemBarcodeHead) resultMap.get("entityBean");

	    //刪除於SI_PROGRAM_LOG的原識別碼資料
	    String identification = MessageStatus.getIdentification(head.getBrandCode(), 
		    head.getOrderTypeCode(), head.getOrderNo());
	    siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

	    setOrderNo(head);
	    String resultMsg = head.getOrderNo() + "存檔成功！是否繼續新增？";
	    resultMap.put("resultMsg", resultMsg);

	    return resultMap;      
	} catch (FormException fe) {
	    log.error(Imformation.REPLENISH_BARCODE.getDescription()+"存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error(Imformation.REPLENISH_BARCODE.getDescription()+"存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception(Imformation.REPLENISH_BARCODE.getDescription()+"存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 檢核補條碼主檔,明細檔
     * @param Head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validateBarcode(ImItemBarcodeHead head, String programId, String identification, List errorMsgs) throws ValidationErrorException, NoSuchObjectException {
	validteHead(head, programId, identification, errorMsgs);
	validteLine(head, programId, identification, errorMsgs);
    }
    
    /**
     * 檢核補條碼主檔
     * @param head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validteHead(ImItemBarcodeHead head, String programId, String identification, List errorMsgs) throws ValidationErrorException, NoSuchObjectException {
	String message = null;
	String tabName = "主檔資料";
	try {
	    Date dueDate = head.getDueDate();
	    String lastUpdatedBy = head.getLastUpdatedBy();
	    if(null == dueDate){
		message = tabName + "的到期日！";
		wrieteMessage(identification, message, lastUpdatedBy, errorMsgs);
	    }
	    
	} catch (Exception e) {
	    message = "檢核"+Imformation.REPLENISH_BARCODE.getDescription()+"主檔單" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }
    
    /**
     * 檢核調整單明細檔
     * @param head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validteLine(ImItemBarcodeHead head, String programId, String identification, List errorMsgs) throws ValidationErrorException, NoSuchObjectException {
	String message = null;
	String tabName = "明細資料頁籤"; 
	String lastUpdatedBy = head.getLastUpdatedBy();
	try{
	    String brandCode = head.getBrandCode();
	    List lines = head.getImItemBarcodeLines();

	    int size = lines.size();
	    if( size > 0 ){
		for (int i = 0; i < size; i++) {
		    ImItemBarcodeLine line = (ImItemBarcodeLine) lines.get(i);
		    String itemCode = line.getItemCode().trim().toUpperCase();
		    message = null;
		    
		    if (!StringUtils.hasText(itemCode)) {
			message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的商品！";
			wrieteMessage(identification, message, lastUpdatedBy, errorMsgs);
		    } else {
			ImItem imItem = imItemDAO.findItem(brandCode, itemCode);
			if(null == imItem){
			    message = "查無" + tabName + "中第" + (i + 1) + "項明細商品:"+itemCode+"！";
			    wrieteMessage(identification, message, lastUpdatedBy, errorMsgs);
			}
		    }

		}
	    }else{
		message = tabName + "中請至少輸入一筆"+Imformation.REPLENISH_BARCODE.getDescription()+"明細！";
		wrieteMessage(identification, message, lastUpdatedBy, errorMsgs);
	    }


	} catch (Exception e) {
	    message = "檢核單明細檔" + tabName + "時發生錯誤，原因：" + e.toString();
	    wrieteMessage(identification, message, lastUpdatedBy, errorMsgs);
	}
    }
    
    /**
     * 寫資訊到programlog
     * @param identification
     * @param message
     * @param user
     * @param errorMsgs
     */
    public void wrieteMessage(String identification, String message, String lastUpdatedBy, List errorMsgs){
	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, lastUpdatedBy);
	errorMsgs.add(message);
	log.error("ERROR:"+message);
    }
}
