package tw.com.tm.erp.hbm.service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.CmDeclarationLog;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;
import tw.com.tm.erp.hbm.bean.ImDistributionHead;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationItemDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationLogDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.UserUtils;


/**
 * 報關單車身明細檔(T2)
 * 修改類別 M:修改 N:新增 O:原始 S:不變			
 * @author MyEclipse Persistence Tools
 */
public class CmDeclarationLogService {
	private static final Log log = LogFactory.getLog(CmDeclarationLogService.class);
	private CmDeclarationHeadDAO cmDeclarationHeadDAO;
	private CmDeclarationLogDAO cmDeclarationLogDAO;
	private SiProgramLogAction siProgramLogAction;
	private BuCommonPhraseService buCommonPhraseService;
	private CmDeclarationHeadService cmDeclarationHeadService;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	private ImItemCategoryService imItemCategoryService;
	private ImAdjustmentHeadDAO imAdjustmentHeadDAO;
	private Long iden;
	
	public static final String MORE = "0";	// 溢到
	public static final String LESS = "1";	// 短到
	public static final String ADD = "2";	// 新增
	
	public static final String PROGRAM_ID= "CM_DECLARATION_LOG";
	
    public static final String[] GRID_FIELD_NAMES = { 
    "indexNo", "itemNo", "modType", 
	"prdtNo", "descrip", "brand",
	"model", "spec",
	"NWght", "qty", "unit",
    "ODeclNo", "OItemNo", "custValueAmt", "remark",
    "lineId", "isLockRecord", "isDeleteRecord", "message"};
    
    public static final int[] GRID_FIELD_TYPES = { 
    AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING,
    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
    AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
    AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};
    
    public static final String[] GRID_FIELD_DEFAULT_VALUES = {
    "0", "", "",
    "", "", "",
    "", "",
    "", "", "",
    "", "", "", "",
    "", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""};
        
	
	public void setCmDeclarationHeadDAO(CmDeclarationHeadDAO cmDeclarationHeadDAO) {
		this.cmDeclarationHeadDAO = cmDeclarationHeadDAO;
	}
	public void setCmDeclarationLogDAO(CmDeclarationLogDAO cmDeclarationLogDAO) {
		this.cmDeclarationLogDAO = cmDeclarationLogDAO;
	}
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}
	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}
	public void setCmDeclarationHeadService(
			CmDeclarationHeadService cmDeclarationHeadService) {
		this.cmDeclarationHeadService = cmDeclarationHeadService;
	}
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
	
	public void setImAdjustmentHeadDAO(ImAdjustmentHeadDAO imAdjustmentHeadDAO){
		this.imAdjustmentHeadDAO = imAdjustmentHeadDAO;
	}
	
	public List<CmDeclarationLog> findcmDeclarationLogsById(Long formId){
		List<CmDeclarationLog> cmDeclarationLogs = cmDeclarationLogDAO.findByProperty("CmDeclarationLog", "identify", formId , "itemNo");
		return cmDeclarationLogs;
	}
	
	public List<CmDeclarationLog> findcmDeclarationLogsByIdforMail(Long formId){
		List<CmDeclarationLog> cmDeclarationLogs = cmDeclarationLogDAO.findByProperty("CmDeclarationLog", "identify", formId , "lastUpdateDate"+" desc");
		return cmDeclarationLogs;
	}
		
	public CmDeclarationLog findcmDeclarationLogById(Long formId){
		List<CmDeclarationLog> cmDeclarationLogs = cmDeclarationLogDAO.findByProperty("CmDeclarationLog", "identify", formId);
		return cmDeclarationLogs.get(0);
	}
	
	public List<Properties> executeInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		Map multiList = new HashMap(0);
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
			Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
			CmDeclarationLog form = formId != 0L ? findcmDeclarationLogsById(formId).get(0) : null;
			//HashMap findObjs = new HashMap();
			//Long headId = (Long)cmDeclarationLogDAO.search("CmDeclarationLog as model", "max(model.headId) as rowCount" ,findObjs, 1, 10, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最大 headId
			//log.info("目前headId到" + headId);
			Long identify = System.currentTimeMillis();
			List <BuCommonPhraseLine> modifyTypes = buCommonPhraseService.findEnableLineById("ModifyTypes");
			multiList.put("modifyTypes" , AjaxUtils.produceSelectorData(modifyTypes  ,"lineCode" ,"name",  false,  true));
			resultMap.put("multiList",multiList);
			
			if(form != null){
				resultMap.put("form",form);
				resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
				resultMap.put("createdByName",UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
				if(form.getSourceOrderNo()!=null){
					resultMap.put("orderNo", form.getSourceOrderNo().substring(3));	
				}				
			}else{
				resultMap.put("identify", identify);
				resultMap.put("status", OrderStatus.SAVE);
				resultMap.put("statusName", OrderStatus.getChineseWord(OrderStatus.SAVE));
				resultMap.put("createdBy", employeeCode);
				resultMap.put("createdByName",UserUtils.getUsernameByEmployeeCode(employeeCode));
				resultMap.put("creationDate", new Date());
			}
			
		}catch(Exception ex){
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type"   , "ALERT");
			messageMap.put("message", "表單初始化失敗，原因："+ex.getMessage());
			messageMap.put("event1" , null);
			messageMap.put("event2" , null);
			resultMap.put("vatMessage",messageMap);
			
		}
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}

	
	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{
		try{
		    List<Properties> result = new ArrayList();
		    List<Properties> gridDatas = new ArrayList();
		    Long identify = NumberUtils.getLong(httpRequest.getProperty("identify"));// 要顯示的HEAD_ID
		    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
		    //==============================================================
		    List<CmDeclarationLog> cmDeclarationLogs = cmDeclarationLogDAO.findPageLine("CmDeclarationLog", "", "identify", identify, iSPage, iPSize);
		    if(cmDeclarationLogs != null && cmDeclarationLogs.size() > 0){
			// 取得第一筆的INDEX
			Long firstIndex = cmDeclarationLogs.get(0).getIndexNo();
			// 取得最後一筆 INDEX
			Long maxIndex = 0L;
			Object object = cmDeclarationLogDAO.findPageLineMaxIndex("CmDeclarationLog", "", "identify", identify, "indexNo desc");
			if(object != null)
				maxIndex = ((CmDeclarationLog)object).getIndexNo();
			result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, cmDeclarationLogs, gridDatas,
				firstIndex, maxIndex));	
		    }else{
		    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));   
		    }
		    
		    return result;
		}catch(Exception ex){
		    log.error("載入頁面顯示的出貨明細發生錯誤，原因：" + ex.toString());
		    throw new Exception("載入頁面顯示的出貨明細失敗！");
		}	
	}
	
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception{
        try{
		    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		    //log.info("gridData =" + gridData);
		    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		    Long identify = NumberUtils.getLong(httpRequest.getProperty("identify"));
		    if(identify == null){
		    	throw new ValidationErrorException("傳入的出貨單主鍵為空值！");
		    }
		    
		    String status = httpRequest.getProperty("status");
		    String employeeCode = httpRequest.getProperty("employeeCode");
		    String errorMsg = null;
		    if (OrderStatus.SAVE.equals(status)) {
				// 將STRING資料轉成List Properties record data
				List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
				// Get INDEX NO
				int indexNo = 0 ;
				Object object = cmDeclarationLogDAO.findPageLineMaxIndex("CmDeclarationLog", "", "identify", identify, "indexNo desc");
				if(object != null)
					indexNo = ((CmDeclarationLog)object).getIndexNo().intValue();
				log.info("indexNo =" + indexNo);
				if (upRecords != null) {
				    for (Properties upRecord : upRecords) {
				        // 先載入HEAD_ID OR LINE DATA
					Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
					
					//如果報關單項次明細有填新的值，就會更新
					//如果不是輸入報關單所撈出來的明細，而是新增 則會寫一筆新的為N
					boolean b = checkAjaxLineHaveValue(upRecord);
						if (b) {
							CmDeclarationLog cmDeclarationLog = (CmDeclarationLog)cmDeclarationLogDAO.findById("CmDeclarationLog", lineId);
						    if(cmDeclarationLog != null){
						    	AjaxUtils.setPojoProperties(cmDeclarationLog, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
						    	//把所有會被存成0的清為null
						    	cleanAjaxLineNumbers(upRecord,cmDeclarationLog);
						    	this.setUpdate(cmDeclarationLog,employeeCode);
						    	cmDeclarationLogDAO.update(cmDeclarationLog);
						    }else{
								indexNo++;
								cmDeclarationLog = new CmDeclarationLog();
								AjaxUtils.setPojoProperties(cmDeclarationLog, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								cleanAjaxLineNumbers(upRecord,cmDeclarationLog);
								cmDeclarationLog.setIdentify(identify);
								cmDeclarationLog.setIndexNo(Long.valueOf(indexNo));
								cmDeclarationLog.setItemNo(Long.valueOf(indexNo));
								cmDeclarationLog.setStatus(OrderStatus.SAVE);
								cmDeclarationLog.setModType("N");
								this.setCreate(cmDeclarationLog,employeeCode);
								cmDeclarationLogDAO.save(cmDeclarationLog);
						    }
					    }
				    }
				}
		    }	    
	    return AjaxUtils.getResponseMsg(errorMsg);
        }catch(Exception ex){
            log.error("更新出貨明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新出貨明細失敗！"); 
        }
    }
	
    public void setUpdate(CmDeclarationLog cmDeclarationLog , String employeeCode){
    	cmDeclarationLog.setLastUpdatedBy(employeeCode);
    	cmDeclarationLog.setLastUpdateDate(new Date());
    }
    
    public void setCreate(CmDeclarationLog cmDeclarationLog , String employeeCode){
		cmDeclarationLog.setCreatedBy(employeeCode);
		cmDeclarationLog.setCreationDate(new Date());
    	cmDeclarationLog.setLastUpdatedBy(employeeCode);
    	cmDeclarationLog.setLastUpdateDate(new Date());
    }
    
    //抓取報關單的內容明細
    public List<Properties> updateDeclByDeclNoForAJAX(Properties httpRequest) throws Exception{
    	
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	try{
    		Long identify = NumberUtils.getLong(httpRequest.getProperty("identify"));// 要顯示的HEAD_ID
    	    String declNo = httpRequest.getProperty("declNo");// 要顯示的HEAD_ID
    	    String employeeCode = httpRequest.getProperty("employeeCode");// 要顯示的HEAD_ID
    	    String statusType = httpRequest.getProperty("statusType"); //紀錄進貨前或後修改
    	    log.info("declNo = " + declNo);
    	    log.info("employeeCode = " + employeeCode);
    	    
    	    //刪除舊的單
    	    List<CmDeclarationLog> cmDeclarationLogs = cmDeclarationLogDAO.findByProperty("CmDeclarationLog", 
    	    		"and identify = ?", new Object[]{identify});
    	    for (Iterator iterator = cmDeclarationLogs.iterator(); iterator.hasNext();) {
				CmDeclarationLog cmDeclarationLog = (CmDeclarationLog) iterator.next();
				cmDeclarationLogDAO.delete(cmDeclarationLog);
			}
    	    
    	    //查詢報關單
        	List<CmDeclarationHead> cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", declNo);
        	if(cmDeclarationHeads == null || cmDeclarationHeads.size() == 0){
        		properties.setProperty("errorMsg", "查無該報關單號！");
    	    }else{
	        	CmDeclarationHead head = cmDeclarationHeads.get(0); 
	    	    properties.setProperty("DeclType", head.getDeclType());
	    	    properties.setProperty("DeclNo", head.getDeclNo());
	    	    List<CmDeclarationItem> cmDeclarationItems = head.getCmDeclarationItems();
	    	    for (Iterator iterator = cmDeclarationItems.iterator(); iterator.hasNext();) {
					CmDeclarationItem cmDeclarationItem = (CmDeclarationItem) iterator.next();
					CmDeclarationLog cmDeclarationLog = new CmDeclarationLog();
					cmDeclarationLog.setIdentify(identify);
					cmDeclarationLog.setModType("S");
					cmDeclarationLog.setDeclType(head.getDeclType());
					cmDeclarationLog.setDeclNo(head.getDeclNo());
					cmDeclarationLog.setItemNo(cmDeclarationItem.getItemNo());
					cmDeclarationLog.setPrdtNo(cmDeclarationItem.getPrdtNo());
					cmDeclarationLog.setDescrip(cmDeclarationItem.getDescrip());
					cmDeclarationLog.setBrand(cmDeclarationItem.getBrand());
					cmDeclarationLog.setModel(cmDeclarationItem.getModel());
					cmDeclarationLog.setSpec(cmDeclarationItem.getSpec());
					cmDeclarationLog.setNWght(cmDeclarationItem.getNWght());
					cmDeclarationLog.setQty(cmDeclarationItem.getQty());
					cmDeclarationLog.setUnit(cmDeclarationItem.getUnit());
					cmDeclarationLog.setODeclNo(cmDeclarationItem.getODeclNo());
					cmDeclarationLog.setOItemNo(cmDeclarationItem.getOItemNo());
					cmDeclarationLog.setCustValueAmt(cmDeclarationItem.getCustValueAmt());
					cmDeclarationLog.setIndexNo(cmDeclarationItem.getItemNo());
					cmDeclarationLog.setStatus(OrderStatus.SAVE);
					if(statusType!="" && statusType!=null){
						cmDeclarationLog.setStatusType(statusType);
					}					
					this.setCreate(cmDeclarationLog,employeeCode);
					cmDeclarationLogDAO.save(cmDeclarationLog);
				}
    	    }
    	    result.add(properties);
    	    
    	    return result;
    	}catch(Exception ex){
    	    log.error("查詢報關單號發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢報關單號發生錯誤，原因：" + ex.getMessage());
    	}
    }
    
    public Map updateParameter(Map parameterMap) throws Exception {
		log.info("updateParameter ");
    	HashMap resultMap = new HashMap();
    	Object bindBean = parameterMap.get("vatBeanFormBind");
    	Object otherBean = parameterMap.get("vatBeanOther");
    	String declNo = (String)PropertyUtils.getProperty(bindBean, "declNo");
    	String declType = (String)PropertyUtils.getProperty(bindBean, "declType");
    	Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
    	String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
    	List<CmDeclarationLog> cmDeclarationLogs = findcmDeclarationLogsById(formId);
    	log.info("更新資料CMLog: "+NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId")));
    	for (Iterator iterator = cmDeclarationLogs.iterator(); iterator.hasNext();) {
			CmDeclarationLog cmDeclarationLog = (CmDeclarationLog) iterator.next();
			cmDeclarationLog.setDeclNo(declNo);
			cmDeclarationLog.setDeclType(declType);
			this.setUpdate(cmDeclarationLog, employeeCode);
			cmDeclarationLogDAO.update(cmDeclarationLog);
		}
    	return resultMap;
    }
    
    //送出檢核
    public Map updateCheckedParameter(Map parameterMap) throws FormException ,Exception{
    	HashMap resultMap = new HashMap();
    	List errorMsgs = new ArrayList(0);
    	String message = "";
    	boolean b = false;
    	try{
	    	Object bindBean = parameterMap.get("vatBeanFormBind");
	    	Object otherBean = parameterMap.get("vatBeanOther");
	    	String declNo = (String)PropertyUtils.getProperty(bindBean, "declNo");
	    	Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
	    	String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
	    	String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
	    	String identification = MessageStatus.getIdentification("T2", "CM", String.valueOf(formId));
	    	siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
	    	
	    	List<CmDeclarationLog> cmDeclarationLogs = findcmDeclarationLogsById(formId);
	    	//移除重複
	    	removeDeletedAJAXLine(cmDeclarationLogs);
	    	//檢查明細項目數量
	    	if(cmDeclarationLogs.size() ==0){
	    		message = "至少要有一筆報關單修改明細";
        		siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, employeeCode);
                errorMsgs.add(message);
	    	}
        	List<CmDeclarationHead> cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", declNo);
        	//檢查是否有報關單
        	if(cmDeclarationHeads == null || cmDeclarationHeads.size() == 0){
        		message = "查無此報關單資訊";
        		siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, employeeCode);
                errorMsgs.add(message);
        	}else{
	        	CmDeclarationHead cmDeclarationHead = cmDeclarationHeads.get(0);;
		    	//List<CmDeclarationItem> cmDeclarationItems = cmDeclarationHead.getCmDeclarationItems();
		    	HashMap itemNoMap = new HashMap();
		    	//檢查報關單是否有明細之項次
		    	for (Iterator iterator = cmDeclarationLogs.iterator(); iterator.hasNext();) {
					CmDeclarationLog cmDeclarationLog = (CmDeclarationLog) iterator.next();
					if("M".equals(cmDeclarationLog.getModType()) || "N".equals(cmDeclarationLog.getModType()))
							b=true;
					itemNoMap.put(cmDeclarationLog.getItemNo(), cmDeclarationLog.getIndexNo());
					cmDeclarationLog.setStatus(formAction);
					this.setUpdate(cmDeclarationLog, employeeCode);
					cmDeclarationLogDAO.update(cmDeclarationLog);
				}
        	}
        	if(!b){
        		message = "至少要有一筆明細資料經過修改";
        		siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, employeeCode);
                errorMsgs.add(message);
        	}
	    	if(errorMsgs.size() > 0){
	    		throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	    	}
    	}catch (FormException fe) {
    	    throw new FormException(fe.getMessage());
    	}catch (Exception e){
    		log.error(e.toString());
    		throw new Exception("報關單修改發生錯誤");
    	}
    	return resultMap;
    }
    
    public void removeDeletedAJAXLine(List<CmDeclarationLog> cmDeclarationLogs){
    	for(int i = cmDeclarationLogs.size() - 1; i >= 0; i--){
			CmDeclarationLog cmDeclarationLog = (CmDeclarationLog) cmDeclarationLogs.get(i);
			if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(cmDeclarationLog.getIsDeleteRecord())){
				cmDeclarationLogDAO.delete(cmDeclarationLog);
				cmDeclarationLogs.remove(cmDeclarationLog);
        	}
		}
    }
    
    public String getIdentification(Long identify) throws Exception{
        String id = null;
		try{
			id = MessageStatus.getIdentification("T2", "CM", String.valueOf(identify));	    
			return id;
		}catch(Exception ex){
		    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
		    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
		}	   
    }

    public static Object[] startProcess(CmDeclarationLog form) throws ProcessFailedException{       
        try{           
		    String packageId = "Cm_Declaration_Log";         
		    String processId = "process";           
		    String version = "20150416";
		    String sourceReferenceType = "CmLog";
		    HashMap context = new HashMap();	    
		    context.put("formId", form.getIdentify());
		    return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		}catch (Exception ex){
		    log.error("報關單修改流程啟動發生錯誤，原因：" + ex.toString());
		    throw new ProcessFailedException("報關單修改流程啟動失敗！");
		}	      
    }
    
    public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws ProcessFailedException{
        try{           
		    HashMap context = new HashMap();
		    context.put("approveResult", approveResult);
		    return ProcessHandling.completeAssignment(assignmentId, context);
		}catch (Exception ex){
		    log.error("完成銷貨工作任務失敗，原因：" + ex.toString());
		    throw new ProcessFailedException("完成銷貨工作任務失敗！");
		}
    }
    
    public static void cleanAjaxLineNumbers(Properties upRecord , CmDeclarationLog cmDeclarationLog){
		String NWght = upRecord.getProperty(GRID_FIELD_NAMES[8]);
		String qty = upRecord.getProperty(GRID_FIELD_NAMES[9]);
		String OtemNo = upRecord.getProperty(GRID_FIELD_NAMES[12]);
    	if(!StringUtils.hasText(NWght))
    		cmDeclarationLog.setNWght(null);
    	if(!StringUtils.hasText(qty))
    		cmDeclarationLog.setQty(null);
    	if(!StringUtils.hasText(OtemNo))
    		cmDeclarationLog.setOItemNo(null);
    }
    
    public static boolean checkAjaxLineHaveValue(Properties upRecord){
    	boolean b = false;
    	StringBuffer s = new StringBuffer();
    	s.append(upRecord.getProperty(GRID_FIELD_NAMES[3]));
    	s.append(upRecord.getProperty(GRID_FIELD_NAMES[4]));
    	s.append(upRecord.getProperty(GRID_FIELD_NAMES[5]));
    	s.append(upRecord.getProperty(GRID_FIELD_NAMES[6]));
    	s.append(upRecord.getProperty(GRID_FIELD_NAMES[7]));
    	s.append(upRecord.getProperty(GRID_FIELD_NAMES[8]));
    	s.append(upRecord.getProperty(GRID_FIELD_NAMES[9]));
    	s.append(upRecord.getProperty(GRID_FIELD_NAMES[10]));
    	s.append(upRecord.getProperty(GRID_FIELD_NAMES[11]));
    	s.append(upRecord.getProperty(GRID_FIELD_NAMES[12]));
    	s.append(upRecord.getProperty(GRID_FIELD_NAMES[13]));
    	if(StringUtils.hasText(s.toString()))
    		b = true;
    	return b;
    }
    
    //流程結束後，回寫報單
    public void saveFinishDeclLog(Long formId){
    	log.info("finishDeclLog Start!!!!!");
    	
    	Long index = 1L;
    	try{
    	List<CmDeclarationLog> cmDeclarationLogs = findcmDeclarationLogsById(formId);
    	log.info("Log原Iden:"+cmDeclarationLogs.get(0).getIdentify());
		CmDeclarationHead cmDeclarationHead = (CmDeclarationHead)cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", cmDeclarationLogs.get(0).getDeclNo()).get(0);
		this.iden = cmDeclarationHead.getHeadId();
		Object object = cmDeclarationLogDAO.findPageLineMaxIndex("CmDeclarationLog", "", "identify", cmDeclarationHead.getHeadId(), "indexNo desc");
		if(object != null)
			index = ((CmDeclarationLog)object).getIndexNo() + 1;	
		List<CmDeclarationItem> cmDeclarationItems = cmDeclarationHead.getCmDeclarationItems();
		CmDeclarationItem cmDeclarationItem;
		Long itemNo = cmDeclarationItems.size()+0L;
    	for (Iterator iterator = cmDeclarationLogs.iterator(); iterator.hasNext();) {
			CmDeclarationLog cmDeclarationLog = (CmDeclarationLog) iterator.next();
			if(cmDeclarationLog.getItemNo() > itemNo){
				cmDeclarationItem = new CmDeclarationItem();
				cmDeclarationItem.setIndexNo(itemNo+1);
				cmDeclarationItem.setItemNo(itemNo+1);
				cmDeclarationItem.setCustValueAmt(cmDeclarationLog.getCustValueAmt());
				itemNo++;
			}else{
				cmDeclarationItem = cmDeclarationItems.get(cmDeclarationLog.getItemNo().intValue()-1);
			}
			
			if("M".equals(cmDeclarationLog.getModType())){
				CmDeclarationLog oLog = new CmDeclarationLog();
				cmDeclarationLog.setIdentify(cmDeclarationHead.getHeadId());
				BeanUtils.copyProperties(cmDeclarationLog, oLog);
				cmDeclarationLog.setModType("O");
				cmDeclarationLog.setIndexNo(index++);
				cmDeclarationLogDAO.update(cmDeclarationLog);
				oLog.setLineId(0L);
				oLog.setIndexNo(index++);
				copyOriginalDeclItem(cmDeclarationLog,cmDeclarationItem,oLog);
				cmDeclarationLogDAO.save(oLog);
				cmDeclarationItem.setAdj("2");
			}else if("N".equals(cmDeclarationLog.getModType())){
				cmDeclarationLog.setIdentify(cmDeclarationHead.getHeadId());
				cmDeclarationLog.setIndexNo(index++);
				cmDeclarationLog.setModType("A");
				cmDeclarationLogDAO.update(cmDeclarationLog);
				cmDeclarationItem.setDeclNo(cmDeclarationLog.getDeclNo());
				cmDeclarationItem.setPrdtNo(cmDeclarationLog.getPrdtNo());
				cmDeclarationItem.setDescrip(cmDeclarationLog.getDescrip());
				cmDeclarationItem.setBrand(cmDeclarationLog.getBrand());
				cmDeclarationItem.setModel(cmDeclarationLog.getModel());
				cmDeclarationItem.setSpec(cmDeclarationLog.getSpec());
				cmDeclarationItem.setNWght(cmDeclarationLog.getNWght());
				cmDeclarationItem.setQty(cmDeclarationLog.getQty());
				cmDeclarationItem.setUnit(cmDeclarationLog.getUnit());
				cmDeclarationItem.setODeclNo(cmDeclarationLog.getODeclNo());
				cmDeclarationItem.setOItemNo(cmDeclarationLog.getOItemNo());				
				cmDeclarationItem.setCmDeclarationHead(cmDeclarationHead);
				cmDeclarationItem.setAdj("1");
				cmDeclarationItems.add(cmDeclarationItem);
			}else{
				cmDeclarationLog.setIdentify(null);
			}
		}
    	cmDeclarationHead.setCmDeclarationItems(cmDeclarationItems);
    	cmDeclarationHeadService.update(cmDeclarationHead);
    	}catch(Exception e){
    		log.error(e.toString());
    	}
    }
    
    public void copyOriginalDeclItem(CmDeclarationLog cmDeclarationLog, CmDeclarationItem cmDeclarationItem,CmDeclarationLog oLog){
    	String prdtNo = cmDeclarationLog.getPrdtNo();
    	String descrip = cmDeclarationLog.getDescrip();
    	String brand = cmDeclarationLog.getBrand();
    	String model = cmDeclarationLog.getModel();
    	String spec = cmDeclarationLog.getSpec();
    	Double NWght = cmDeclarationLog.getNWght();
    	Double Qty = cmDeclarationLog.getQty();
    	String unit = cmDeclarationLog.getUnit();
    	String ODeclNo = cmDeclarationLog.getODeclNo();
    	Long OItemNo = cmDeclarationLog.getOItemNo();
    	if(StringUtils.hasText(prdtNo)){
			cmDeclarationLog.setPrdtNo(cmDeclarationItem.getPrdtNo());
			cmDeclarationLog.setPrdtNoMod("Y");
			oLog.setPrdtNoMod("Y");
			cmDeclarationItem.setPrdtNo(prdtNo);
		}
		if(StringUtils.hasText(descrip)){
			cmDeclarationLog.setDescrip(cmDeclarationItem.getDescrip());
			cmDeclarationLog.setDescripMod("Y");
			oLog.setDescripMod("Y");
			cmDeclarationItem.setDescrip(descrip);
		}
		if(StringUtils.hasText(brand)){
			cmDeclarationLog.setBrand(cmDeclarationItem.getBrand());
			cmDeclarationLog.setBrandMod("Y");
			cmDeclarationLog.setBrandMod("Y");
			cmDeclarationItem.setBrand(brand);
		}
		if(StringUtils.hasText(model)){
			cmDeclarationLog.setModel(cmDeclarationItem.getModel());
			cmDeclarationLog.setModelMod("Y");
			oLog.setModelMod("Y");
			cmDeclarationItem.setModel(model);
		}
		if(StringUtils.hasText(spec)){
			cmDeclarationLog.setSpec(cmDeclarationItem.getSpec());
			cmDeclarationLog.setSpecMod("Y");
			oLog.setSpecMod("Y");
			cmDeclarationItem.setSpec(spec);
		}
		if(NWght != null){
			cmDeclarationLog.setNWght(cmDeclarationItem.getNWght());
			cmDeclarationLog.setNWghtMod("Y");
			oLog.setNWghtMod("Y");
			cmDeclarationItem.setNWght(NWght);
		}
		if(Qty != null){
			cmDeclarationLog.setQty(cmDeclarationItem.getQty());
			cmDeclarationLog.setQtyMod("Y");
			oLog.setQtyMod("Y");
			cmDeclarationItem.setQty(Qty);
		}
		if(StringUtils.hasText(unit)){
			cmDeclarationLog.setUnit(cmDeclarationItem.getUnit());
			cmDeclarationLog.setUnitMod("Y");
			oLog.setUnitMod("Y");
			cmDeclarationItem.setUnit(unit);
		}
		if(StringUtils.hasText(ODeclNo)){
			cmDeclarationLog.setODeclNo(cmDeclarationItem.getODeclNo());
			cmDeclarationLog.setODeclNoMod("Y");
			oLog.setODeclNoMod("Y");
			cmDeclarationItem.setODeclNo(ODeclNo);
		}
		if(OItemNo != null){
			cmDeclarationLog.setOItemNo(cmDeclarationItem.getOItemNo());
			cmDeclarationLog.setOItemNoMod("Y");
			oLog.setOItemNoMod("Y");
			cmDeclarationItem.setOItemNo(OItemNo);
		}
	}
    /*
    public void getMailList(String test) throws Exception {
    	System.out.println("測試發信:"+test);
    }
    */
    /**
     * 取得寄信清單與相關資訊
     * @param subject
     * @param templateFileName
     * @param display
     * @param mailAddress
     * @param attachFiles
     * @param cidMap
     */
    
	public void getMailList(Long formId , String subject, String templateFileName, Map display, List mailAddress,
			List attachFiles, Map cidMap) throws Exception {
            log.info("進入報單修改寄信功能:");
			// #1 取得寄件者與其他相關配置檔
			// #2 取得CC報表的URL
			// #3 用URL轉成PDF再轉jpg
			// #4 用URL轉成xls
			// #5 寄信
    
		try {
			
			String brandCode = "T2";
			String orderTypeCode = "CMM";
			log.info("取得的formId:"+this.iden+" 原iden:"+formId); 
			
			List<CmDeclarationLog> cmDeclarationLogs = findcmDeclarationLogsByIdforMail(this.iden);
			CmDeclarationLog  logHead = (CmDeclarationLog)cmDeclarationLogs.get(0); 
			System.out.println("取得的單頭號碼:"+logHead.getDeclNo());
	        
	        
	        String reportFileName = null;
			String subjectError = null;
			String description = null;
			StringBuffer emailError = new StringBuffer();
			
			
			 
			// #1  取得配置檔得到 寄信的報表與  
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("MailListCMU"
					,"CMLog"); // +orderTypeCode itemCategory
			
			if (null == buCommonPhraseLine) {
				// 寄給故障維謢人員
				mailAddress.add("Developer@tasameng.com.tw");
				//reportFileName = "SO0753_1.rpt";
				subjectError = "修改報單無配置檔異常 ";
				description = "";
				
			} else {
				reportFileName = buCommonPhraseLine.getAttribute1();
				// 1.該單起單人、採助、採購人、採購主管 從 BU_EMPLOYEE 取得
				// String domain = "@tasameng.com.tw";
				//String createdBy = logHead.getCreatedBy();
				
				// 2.營業 從 BU_COMMON_PHRASE_LINE 取得
				// 修改為寄給多個單位，以分號分隔email地址 by Weichun 2011.09.15
				
				String[] attribute2 = buCommonPhraseLine.getAttribute2().split(";");
				for (int i = 0; i < attribute2.length; i++) {
					if (StringUtils.hasText(attribute2[i])) {
						//System.out.println("通知email ==> " + attribute2[i]);
						mailAddress.add(attribute2[i]);
						
					}
					
				}
				
				// 3.商品異動_副本 從 BU_COMMON_PHRASE_LINE 取得
				String attribute3 = buCommonPhraseLine.getAttribute3();
				if (StringUtils.hasText(attribute3)) {
					
					mailAddress.add(attribute3);
					
				}
				
				// 前半部主旨
				description = buCommonPhraseLine.getDescription();
				
			}
			
			templateFileName = "CommonTemplate.ftl";
			description = buCommonPhraseLine.getDescription();
			Map parameters = new HashMap(0);
			parameters.put("prompt0", logHead.getIdentify().toString());
			subject = "修改報單寄信";
			if (StringUtils.hasText(subjectError)) {
				
				subject = subjectError + subject;
			}
			
			String fileName = "CMEdit";
			
			List<File> png_files = new ArrayList<File>();
            PDDocument doc = null;
            // #2 取得cc連結
			//String reportUrl = "http://10.1.98.161:8080/crystal/t2/CM0201.rpt?prompt0="+logHead.getDeclNo();
			String reportUrl = SystemConfig.getPureReportURL(brandCode, orderTypeCode, logHead.getLastUpdatedBy(),reportFileName, parameters);
			log.info("babureportUrl:"+reportUrl);
			
			try {
				
				URL pdfUrl = new URL(reportUrl + "&init=pdf");
				URLConnection pdfConnection = pdfUrl.openConnection();

				// - 模擬client端的瀏覽器 避免被當作網頁機器人而被阻擋
				pdfConnection.setRequestProperty("User-Agent", "");
				pdfConnection.setRequestProperty("accept-language", "");
				pdfConnection.setRequestProperty("cookie", "");
				pdfConnection.setDoOutput(true);
				log.info("已");
				BufferedInputStream pdfBis = new BufferedInputStream(pdfConnection.getInputStream());
				System.out.println("pdfBis = " + pdfBis);
				File pdfFile = File.createTempFile(fileName, ".pdf");
				FileOutputStream pdfFos = new FileOutputStream(pdfFile);
				byte[] data = new byte[1];
				while (pdfBis.read(data) != -1) {
					pdfFos.write(data);
				}
				pdfFos.flush();
				pdfFos.close();
				log.info("餅");
				// #4 取得excel
				URL xlsUrl = new URL(reportUrl + "&init=xls");
				URLConnection xlsConnection = xlsUrl.openConnection();

				// - 模擬client端的瀏覽器 避免被當作網頁機器人而被阻擋
				xlsConnection.setRequestProperty("User-Agent", "");
				xlsConnection.setRequestProperty("accept-language", "");
				xlsConnection.setRequestProperty("cookie", "");
				xlsConnection.setDoOutput(true);
				log.info("丁");
				BufferedInputStream xlsBis = new BufferedInputStream(xlsConnection.getInputStream());
				System.out.println("xlsBis = " + xlsBis);
				File xlsFile = File.createTempFile(fileName + "-" + DateUtils.getCurrentDateStr("yyyyMMddHHmmss") + "_",
						".xls");
				FileOutputStream xlsFos = new FileOutputStream(xlsFile);
				byte[] xlsData = new byte[1];
				while (xlsBis.read(xlsData) != -1) {
					xlsFos.write(xlsData);
				}
				xlsFos.flush();
				xlsFos.close();

				attachFiles.add(xlsFile.getAbsolutePath());

				// pdf轉圖檔
				doc = PDDocument.load(pdfFile);
				List pages = doc.getDocumentCatalog().getAllPages();
				for (int j = 0; j < pages.size(); j++) {
					PDPage page = (PDPage) pages.get(j);
					BufferedImage bufferedImage = page.convertToImage();
					File imageFile = File.createTempFile(fileName + j, ".jpg"); // "new File("PA"+j+".png") File.createTempFile("PA"+j,".gif")
					ImageIO.write(bufferedImage, "jpg", imageFile);
					png_files.add(imageFile);
					attachFiles.add(imageFile.getAbsolutePath());

					cidMap.put(imageFile.getAbsolutePath(), png_files.get(j).getName());

				}

				StringBuffer html = new StringBuffer();
				System.out.println("emailError.toString() = " + emailError.toString());
				if (StringUtils.hasText(emailError.toString())) {
					html.append(emailError.toString());
				}

				for (int i = 0; i < png_files.size(); i++) {
					html.append("<img src='cid:" + png_files.get(i).getName() + "'/><br>");
				}
				System.out.println("html = " + html.toString());
				// 設定樣板的內容值
				display.put("display", html.toString());

				// #5
                MailUtils.sendMail(subject, templateFileName, display, mailAddress, attachFiles, cidMap);
				
				
				

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("取得寄件相關內容發生錯誤");
		}

	}
	
	//透過AIF抓取報關單的內容明細
    public List<Properties> updateDeclNoForAifOrderAJAX(Properties httpRequest) throws Exception{
    	
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	try{
    		Long identify = NumberUtils.getLong(httpRequest.getProperty("identify"));// 要顯示的HEAD_ID
    	    String orderNo = httpRequest.getProperty("orderNo");// 要顯示的HEAD_ID
    	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 要顯示的HEAD_ID
    	    String brandCode = "T2";
    	    String employeeCode = httpRequest.getProperty("employeeCode");// 要顯示的HEAD_ID
    	    String statusType = httpRequest.getProperty("statusType"); //紀錄進貨前或後修改
    	    log.info("orderNo = " + orderNo);
    	    log.info("employeeCode = " + employeeCode);
    	    
    	    //刪除舊的單
    	    List<CmDeclarationLog> cmDeclarationLogs = cmDeclarationLogDAO.findByProperty("CmDeclarationLog", 
    	    		"and identify = ?", new Object[]{identify});
    	    for (Iterator iterator = cmDeclarationLogs.iterator(); iterator.hasNext();) {
				CmDeclarationLog cmDeclarationLog = (CmDeclarationLog) iterator.next();
				cmDeclarationLogDAO.delete(cmDeclarationLog);
			}
    	    
    	    List<ImAdjustmentHead> imAdjustmentHeads = imAdjustmentHeadDAO.findAdjustForAIFOrderNo(brandCode, orderTypeCode, orderNo);
    	    
    	    if(imAdjustmentHeads == null || imAdjustmentHeads.size() == 0){
        		properties.setProperty("errorMsg", "查無該AIF單！");
    	    }else{
    	    	Long indexNo = 0L;
    	    	ImAdjustmentHead adjHead = imAdjustmentHeads.get(0);
    	    	List <ImAdjustmentLine> imAdjustmentLines = adjHead.getImAdjustmentLines();
    	    	for (Iterator iterator = imAdjustmentLines.iterator(); iterator.hasNext();) {
    	    		
    	    		
    	    		indexNo = indexNo+1;
    	    		log.info("indexNo = " + indexNo);
    	    		ImAdjustmentLine imAdjustmentLine = (ImAdjustmentLine) iterator.next();
    	    		String declNo = imAdjustmentLine.getOriginalDeclarationNo();
    	    		Long declSeq = imAdjustmentLine.getOriginalDeclarationSeq();
    	    		
    	    		String moreOrLessType = imAdjustmentLine.getMoreOrLessType(); //判斷是新增或修改
    	    		log.info("declNo = " + declNo);
    	    		log.info("declSeq = " + declSeq);
    	    		//查詢報關單
    	    		
                	List<CmDeclarationHead> cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", declNo);
                	if(cmDeclarationHeads == null || cmDeclarationHeads.size() == 0){
                		properties.setProperty("errorMsg", "查無該報關單號！");
            	    }else{
        	        	CmDeclarationHead head = cmDeclarationHeads.get(0); 
        	    	    properties.setProperty("DeclType", head.getDeclType());
        	    	    properties.setProperty("DeclNo", head.getDeclNo());
        	    	    
        	    	    if(ADD.equals(moreOrLessType)){
        	    	    	CmDeclarationLog cmDeclarationLog = new CmDeclarationLog();
        	    	    	cmDeclarationLog.setIdentify(identify);
        	    	    	cmDeclarationLog.setModType("N");
        	    	    	cmDeclarationLog.setDeclType(head.getDeclType());
        					cmDeclarationLog.setDeclNo(head.getDeclNo());
        					cmDeclarationLog.setItemNo(imAdjustmentLine.getOriginalDeclarationSeq());
        					cmDeclarationLog.setQty(imAdjustmentLine.getDifQuantity());
        					cmDeclarationLog.setIndexNo(indexNo);
        					cmDeclarationLog.setSourceOrderNo(orderTypeCode+orderNo);
        					cmDeclarationLog.setStatus(OrderStatus.SAVE);
        					if(statusType!="" && statusType!=null){
        						cmDeclarationLog.setStatusType(statusType);
        					}					
        					this.setCreate(cmDeclarationLog,employeeCode);
        					cmDeclarationLogDAO.save(cmDeclarationLog);
        	    	    }else{
        	    	    	List<CmDeclarationItem> cmDeclarationItems = head.getCmDeclarationItems();
            	    	    for (Iterator cmIterator = cmDeclarationItems.iterator(); cmIterator.hasNext();) {
            					CmDeclarationItem cmDeclarationItem = (CmDeclarationItem) cmIterator.next();
            					if(declSeq.equals(cmDeclarationItem.getItemNo())){
            						log.info("項次declSeq進入幾次 = " + declSeq);
            						CmDeclarationLog cmDeclarationLog = new CmDeclarationLog();
                					cmDeclarationLog.setIdentify(identify);
                					cmDeclarationLog.setModType("S");
                					cmDeclarationLog.setDeclType(head.getDeclType());
                					cmDeclarationLog.setDeclNo(head.getDeclNo());
                					cmDeclarationLog.setItemNo(cmDeclarationItem.getItemNo());
                					cmDeclarationLog.setPrdtNo(cmDeclarationItem.getPrdtNo());
                					cmDeclarationLog.setDescrip(cmDeclarationItem.getDescrip());
                					cmDeclarationLog.setBrand(cmDeclarationItem.getBrand());
                					cmDeclarationLog.setModel(cmDeclarationItem.getModel());
                					cmDeclarationLog.setSpec(cmDeclarationItem.getSpec());
                					cmDeclarationLog.setNWght(cmDeclarationItem.getNWght());
                					cmDeclarationLog.setQty(cmDeclarationItem.getQty()+imAdjustmentLine.getDifQuantity());
                					cmDeclarationLog.setUnit(cmDeclarationItem.getUnit());
                					cmDeclarationLog.setODeclNo(cmDeclarationItem.getODeclNo());
                					cmDeclarationLog.setOItemNo(cmDeclarationItem.getOItemNo());
                					cmDeclarationLog.setIndexNo(indexNo);
                					cmDeclarationLog.setSourceOrderNo(orderTypeCode+orderNo);
                					cmDeclarationLog.setStatus(OrderStatus.SAVE);
                					cmDeclarationLog.setCustValueAmt(cmDeclarationItem.getCustValueAmt());
                					if(statusType!="" && statusType!=null){
                						cmDeclarationLog.setStatusType(statusType);
                					}					
                					this.setCreate(cmDeclarationLog,employeeCode);
                					cmDeclarationLogDAO.save(cmDeclarationLog);            					
            					}        					
            				}
        	    	    }        	    	    
            	    }
    	    	}    	    	
    	    }    	    
    	    
    	    result.add(properties);
    	    
    	    return result;
    	}catch(Exception ex){
    	    log.error("查詢報關單號發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢報關單號發生錯誤，原因：" + ex.getMessage());
    	}
    }
    
    /**
     * 執行報關單查詢初始化
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception{
        
        HashMap resultMap = new HashMap();
        try{
            Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    Map multiList = new HashMap(0);
	    //List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(loginBrandCode, "PM");
	    List<ImItemCategory> allItemCategory = imItemCategoryService.findByCategoryType(loginBrandCode, "ITEM_CATEGORY");
	    //multiList.put("allOrderTypes",   AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
	    multiList.put("allItemCategory", AjaxUtils.produceSelectorData( allItemCategory, "categoryCode", "categoryName", true, true));
	    resultMap.put("multiList",multiList);
		
	    return resultMap;       	
        }catch (Exception ex) {
	    log.error("報關單查詢初始化失敗，原因：" + ex.toString());
	    throw new Exception("報關單查詢初始化失敗，原因：" + ex.toString());
	}           
    }
}