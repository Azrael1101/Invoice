package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import tw.com.tm.erp.hbm.bean.BuGoal;
import tw.com.tm.erp.hbm.bean.BuGoalDeployHead;
import tw.com.tm.erp.hbm.bean.BuGoalDeployLine;
import tw.com.tm.erp.hbm.bean.BuGoalEmployee;
import tw.com.tm.erp.hbm.bean.BuGoalEmployeeLine;
import tw.com.tm.erp.hbm.bean.BuGoalHead;
import tw.com.tm.erp.hbm.bean.BuGoalPercentLine;
import tw.com.tm.erp.hbm.bean.BuGoalTarget;
import tw.com.tm.erp.hbm.bean.BuGoalWork;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuGoalDeployHeadDAO;
import tw.com.tm.erp.hbm.dao.BuGoalDeployLineDAO;
import tw.com.tm.erp.hbm.dao.BuGoalEmployeeLineDAO;
import tw.com.tm.erp.hbm.dao.BuGoalHeadDAO;
import tw.com.tm.erp.hbm.dao.BuGoalPercentLineDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.UserUtils;

public class BuGoalService {
    private static final Log log = LogFactory.getLog(BuGoalService.class);


    public static final String BASIC_PROGRAM_ID= "BU_GOAL_BASIC";
    public static final String DEPLOY_PROGRAM_ID= "BU_GOAL_DEPLOY";
    public static final String BU_GOAL_DEPLOY= "BGD";
    public static final String BU_GOAL_BASIC= "BGB";

    private BuGoalHeadDAO buGoalHeadDAO;
    private BuGoalDeployHeadDAO buGoalDeployHeadDAO;
    private BuGoalDeployLineDAO buGoalDeployLineDAO;
    private BuGoalPercentLineDAO buGoalPercentLineDAO;
    private BuGoalEmployeeLineDAO buGoalEmployeeLineDAO;
    private BuBrandDAO buBrandDAO; 
    private BaseDAO baseDAO;
    private BuShopDAO buShopDAO;
    private BuEmployeeDAO buEmployeeDAO;
    private ImItemCategoryDAO imItemCategoryDAO;
    private ImDeliveryHeadDAO imDeliveryHeadDAO;

    private SiProgramLogAction siProgramLogAction;

    public void setBuGoalHeadDAO(BuGoalHeadDAO buGoalHeadDAO) {
	this.buGoalHeadDAO = buGoalHeadDAO;
    }

    public void setBuGoalDeployHeadDAO(BuGoalDeployHeadDAO buGoalDeployHeadDAO) {
	this.buGoalDeployHeadDAO = buGoalDeployHeadDAO;
    }

    public void setBuGoalDeployLineDAO(BuGoalDeployLineDAO buGoalDeployLineDAO) {
	this.buGoalDeployLineDAO = buGoalDeployLineDAO;
    }

    public void setBuGoalPercentLineDAO(BuGoalPercentLineDAO buGoalPercentLineDAO) {
	this.buGoalPercentLineDAO = buGoalPercentLineDAO;
    }

    public void setBuGoalEmployeeLineDAO(BuGoalEmployeeLineDAO buGoalEmployeeLineDAO) {
	this.buGoalEmployeeLineDAO = buGoalEmployeeLineDAO;
    }

    public void setBaseDAO(BaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
	this.buBrandDAO = buBrandDAO;
    }

    public void setBuShopDAO(BuShopDAO buShopDAO) {
	this.buShopDAO = buShopDAO;
    }

    public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
	this.buEmployeeDAO = buEmployeeDAO;
    }

    public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
	this.imItemCategoryDAO = imItemCategoryDAO;
    }

    public void setImDeliveryHeadDAO(ImDeliveryHeadDAO imDeliveryHeadDAO) {
	this.imDeliveryHeadDAO = imDeliveryHeadDAO;
    }

    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }

    /**
     * 目標佔比明細欄位
     */
    public static final String[] GRID_PERCENT_FIELD_NAMES = { 
	"indexNo", 		"itemCategory" , 			"itemCategoryPercent",		
	"itemSubcategory",	"itemSubcategoryPercent",	"itemBrand",
	"reserve5",		"itemBrandName", 			"itemBrandPercent", 		
	"workType",		"workTypePercent", 
	"lineId", "isLockRecord", "isDeleteRecord", "message"
    };

    public static final String[] GRID_PERCENT_FIELD_DEFAULT_VALUES = { 
	"", "", 	"0.0", 
	"", "0.0", "",
	"", "", 	"0.0", 
	"", "0.0", 
	"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };     

    public static final int[] GRID_PERCENT_FIELD_TYPES = { 
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,AjaxUtils.FIELD_TYPE_STRING, 
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, 
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };

    /**
     * 人員設定欄位
     */
    public static final String[] GRID_EMPLOYEE_FIELD_NAMES = { 
	"indexNo", 			"employeeItemCategory",		"employeeItemSubcategory",		
	"employeeItemBrand", 	"reserve5",					"employeeItemBrandName", 	
	"employeeCode",   		"employeeName",				"employeeWorkType",	
	"lineId", "isLockRecord", "isDeleteRecord", "message"
    };

    public static final String[] GRID_EMPLOYEE_FIELD_DEFAULT_VALUES = { 
	"", "", "", 
	"", "", "", 
	"", "", "", 
	"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };     

    public static final int[] GRID_EMPLOYEE_FIELD_TYPES = { 
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };

    /**
     * 目標設定明細欄位
     */
    public static final String[] GRID_DEPLOY_FIELD_NAMES = { 
	"indexNo",		"itemCategory", 	"itemSubcategory", 
	"itemBrand",	"employeeCode" ,	"workType",	 			
	"goalAmount",	"signingAmount",
	"lineId", "isLockRecord", "isDeleteRecord", "message"
    };

    public static final String[] GRID_DEPLOY_FIELD_DEFAULT_VALUES = { 
	"", "", "", 
	"", "", "", 
	"0.0", "0.0",
	"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };     

    public static final int[] GRID_DEPLOY_FIELD_TYPES = { 
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };


    /**
     * 業績目標定義主檔 查詢picker用的欄位
     */
    public static final String[] GRID_SEARCH_FIELD_NAMES = { 
	"department", 		"shopCode", 	"statusName",
	"lastUpdateDate", 	"headId"
    };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
	"", "", "",
	"", ""
    }; 

    /**
     * 業績目標設定 查詢picker用的欄位
     */
    public static final String[] GRID_SEARCH_DEPLOY_FIELD_NAMES = { 
	"department", 		"shopCode", 		"year", 
	"month" ,			"originalGoal",	 		"statusName",
	"lastUpdateDate", 	"headId"
    };

    public static final String[] GRID_SEARCH_DEPLOY_FIELD_DEFAULT_VALUES = { 
	"", "", "", 
	"", "", "",
	"", ""
    };     


    /**
     * 檢核業績目標基本檔主檔,明細檔
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List checkedBuGoal(Map parameterMap, String approvalResult )throws FormException, Exception{
	List errorMsgs = new ArrayList(0);
	String message = null;
	String identification = null;
	BuGoalHead head = null;
	try{

	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    head = getActualBuGoal(formLinkBean);

	    String status = head.getStatus();

	    if ( (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)) ) {

		identification = MessageStatus.getIdentification(head.getBrandCode(), 
			BU_GOAL_BASIC, head.getReserve1());

		siProgramLogAction.deleteProgramLog(BASIC_PROGRAM_ID, null, identification);

		validateBuGoal( head, BASIC_PROGRAM_ID, identification, errorMsgs ); 
	    }

	}catch (Exception ex) {
	    message = "業績目標基本檔檢核失敗，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(BASIC_PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);	
	}
	return errorMsgs;
    }

    /**
     * 檢核業績目標設定主檔,明細檔
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List checkedBuGoalDeploy(Map parameterMap, String approvalResult )throws FormException, Exception{
	List errorMsgs = new ArrayList(0);
	String message = null;
	String identification = null;
	BuGoalDeployHead buGoalDeployHead = null;
	try{

	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    buGoalDeployHead = getActualBuGoalDeploy(formLinkBean);

	    String status = buGoalDeployHead.getStatus();

	    if ( (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)) ) {

		identification = MessageStatus.getIdentification(buGoalDeployHead.getBrandCode(), 
			BU_GOAL_DEPLOY, buGoalDeployHead.getReserve1());

		siProgramLogAction.deleteProgramLog(DEPLOY_PROGRAM_ID, null, identification);

		validateBuGoalDeploy( buGoalDeployHead, DEPLOY_PROGRAM_ID, identification, errorMsgs, formLinkBean ); 
	    }

	}catch (Exception ex) {
	    message = "業績目標設定檢核失敗，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(DEPLOY_PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, buGoalDeployHead.getLastUpdatedBy());
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
	    
	    return null;//                                                              ProcessHandling.completeAssignment(assignmentId, context);
	}catch (Exception ex){
	    log.error("完成業績工作任務失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("完成業績工作任務失敗！"+ ex.getMessage());
	}
    }
    
    /**
     * 將業績目標設定的單身 mark 為刪除的刪掉
     * @param head
     */
    private void deleteLine(BuGoalDeployHead head){
	List<BuGoalDeployLine> buGoalDeployLines = head.getBuGoalDeployLines();
	if(buGoalDeployLines != null && buGoalDeployLines.size() > 0){
	    for(int i = buGoalDeployLines.size() - 1; i >= 0; i--){
		BuGoalDeployLine buGoalDeployLine = buGoalDeployLines.get(i);         
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(buGoalDeployLine.getIsDeleteRecord())){
		    buGoalDeployLines.remove(buGoalDeployLine);
		    buGoalDeployLineDAO.delete(buGoalDeployLine);
		}
	    }
	}
    }

    /**
     * 將業績目標定義檔的單身 mark 為刪除的刪掉
     * @param head
     */
    private void deletePercentAndEmployeeLine(BuGoalHead head){
	// 刪 目標佔比
	List<BuGoalPercentLine> buGoalPercentLines = head.getBuGoalPercentLines();
	if(buGoalPercentLines != null && buGoalPercentLines.size() > 0){
	    for(int i = buGoalPercentLines.size() - 1; i >= 0; i--){
		BuGoalPercentLine buGoalPercentLine = buGoalPercentLines.get(i);         
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(buGoalPercentLine.getIsDeleteRecord())){
		    buGoalPercentLines.remove(buGoalPercentLine);
		    buGoalPercentLineDAO.delete(buGoalPercentLine);
		}
	    }
	}
	// 刪 人員設定
	List<BuGoalEmployeeLine> buGoalEmployeeLines = head.getBuGoalEmployeeLines();
	if(buGoalEmployeeLines != null && buGoalEmployeeLines.size() > 0){
	    for(int i = buGoalEmployeeLines.size() - 1; i >= 0; i--){
		BuGoalEmployeeLine buGoalEmployeeLine = buGoalEmployeeLines.get(i);         
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(buGoalEmployeeLine.getIsDeleteRecord())){
		    buGoalEmployeeLines.remove(buGoalEmployeeLine);
		    buGoalEmployeeLineDAO.delete(buGoalEmployeeLine);
		}
	    }
	}
    }

    /**
     * 初始化 bean 額外顯示欄位
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);

	try{
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    

	    BuGoalHead buGoalHead = executeFindActualBuGoalHead(parameterMap);
	    String shopCode = buGoalHead.getShopCode();
	    String department = buGoalHead.getDepartment();

	    String employeeName = UserUtils.getUsernameByEmployeeCode(buGoalHead.getCreatedBy());
	    
	    Map multiList = new HashMap(0);
	    resultMap.put("form", buGoalHead);
	    resultMap.put("statusName", OrderStatus.getChineseWord(buGoalHead.getStatus()));
	    resultMap.put("brandName",buBrandDAO.findById(buGoalHead.getBrandCode()).getBrandName());
	    resultMap.put("createByName", employeeName);

	    List<BuCommonPhraseLine> allDepartments = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"Department", "Y"}, "indexNo" );
	    List<BuShop> allShopCodes = buShopDAO.findShopByProperty( buGoalHead.getBrandCode(), department, "Y");

	    List<ImItemCategory> allItemCategorys = imItemCategoryDAO.findCategoryByBrandCode(buGoalHead.getBrandCode(), ImItemCategoryDAO.CATEGORY00, "Y");
//	    List<ImItemCategory> allItemSubcategorys = imItemCategoryDAO.findCategoryByBrandCode(buGoalHead.getBrandCode(), "ITEM_CATEGORY", "Y");
//	    List<ImItemCategory> allItemBrands = imItemCategoryDAO.findCategoryByBrandCode(buGoalHead.getBrandCode(), "ItemBrand", "Y");
	    List<BuCommonPhraseLine> allWorkTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"WorkType", "Y"}, "indexNo" );

	    multiList.put("allDepartments"	, AjaxUtils.produceSelectorData(allDepartments, "lineCode", "name", true, true, department != null ? department : "" ));
	    multiList.put("allShopCodes"	, AjaxUtils.produceSelectorData(allShopCodes, "shopCode", "shopCName", true, true, shopCode != null ? shopCode : "" ));
	    multiList.put("allItemCategorys"	, AjaxUtils.produceSelectorData(allItemCategorys, "categoryCode", "categoryName", true, true));
//	    multiList.put("allItemSubcategorys"	, AjaxUtils.produceSelectorData(allItemSubcategorys, "categoryCode", "categoryName", true, true));
//	    multiList.put("allItemBrands"	, AjaxUtils.produceSelectorData(allItemBrands, "categoryCode", "categoryName", true, true));

	    multiList.put("allWorkTypes"	, AjaxUtils.produceSelectorData(allWorkTypes, "lineCode", "name", true, true));

	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("目標初始化失敗，原因：" + ex.toString());
	    throw new Exception("目標單初始化失敗，原因：" + ex.toString());

	}
    }

    /**
     * 目標設定 初始化 bean 額外顯示欄位
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeDeployInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);

	try{
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	   
	    BuGoalDeployHead buGoalDeployHead = excuteFindActiualBuGoalDeploy(parameterMap); // executeFindActualBuGoalHead(parameterMap, true);
	    String shopCode = buGoalDeployHead.getShopCode();
	    String department = buGoalDeployHead.getDepartment();
	    Long year = buGoalDeployHead.getYear();
	    Long month = buGoalDeployHead.getMonth();

	    String employeeName = UserUtils.getUsernameByEmployeeCode(buGoalDeployHead.getCreatedBy());

	    Map multiList = new HashMap(0);
	    resultMap.put("form", buGoalDeployHead);
	    resultMap.put("flowStatusName", OrderStatus.getChineseWord(buGoalDeployHead.getFlowStatus()));
	    resultMap.put("brandName",buBrandDAO.findById(buGoalDeployHead.getBrandCode()).getBrandName());
	    resultMap.put("createByName", employeeName);

	    List<BuCommonPhraseLine> allDepartments = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"Department", "Y"}, "indexNo" );
	    List<BuShop> allShopCodes = buShopDAO.findShopByProperty( buGoalDeployHead.getBrandCode(), department, "Y");

	    List<ImItemCategory> allItemCategorys = imItemCategoryDAO.findCategoryByBrandCode(buGoalDeployHead.getBrandCode(), "CATEGORY00", "Y");
	    List<ImItemCategory> allItemSubcategorys = imItemCategoryDAO.findCategoryByBrandCode(buGoalDeployHead.getBrandCode(), "ITEM_CATEGORY", "Y");
	    List<ImItemCategory> allItemBrands = imItemCategoryDAO.findCategoryByBrandCode(buGoalDeployHead.getBrandCode(), "ItemBrand", "Y");

	    List<BuCommonPhraseLine> allWorkTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"WorkType", "Y"}, "indexNo" );
	    List<BuCommonPhraseLine> allYears = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"BudgetYearList", "Y"}, "indexNo" );
	    List<BuCommonPhraseLine> allMonths = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"Month", "Y"}, "indexNo" );

	    multiList.put("allDepartments"	, AjaxUtils.produceSelectorData(allDepartments, "lineCode", "name", true, true, department != null ? department : "" ));
	    multiList.put("allShopCodes"	, AjaxUtils.produceSelectorData(allShopCodes, "shopCode", "shopCName", true, true, shopCode != null ? shopCode : "" ));
	    multiList.put("allItemCategorys"	, AjaxUtils.produceSelectorData(allItemCategorys, "categoryCode", "categoryName", true, true));
	    multiList.put("allItemSubcategorys"	, AjaxUtils.produceSelectorData(allItemSubcategorys, "categoryCode", "categoryName", true, true));
	    multiList.put("allItemBrands"	, AjaxUtils.produceSelectorData(allItemBrands, "categoryCode", "categoryName", true, true));

	    multiList.put("allWorkTypes"	, AjaxUtils.produceSelectorData(allWorkTypes, "lineCode", "name", true, true));
	    multiList.put("allYears"		, AjaxUtils.produceSelectorData(allYears, "lineCode", "name", false, true, year != null ? String.valueOf(year) : "" ));
	    multiList.put("allMonths"		, AjaxUtils.produceSelectorData(allMonths, "lineCode", "name", false, true, month != null ? String.valueOf(month) : ""));

	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("目標設定初始化失敗，原因：" + ex.toString());
	    throw new Exception("目標設定初始化失敗，原因：" + ex.toString());

	}
    }
    /**
     * 查詢目標佔比/人員設定初始化
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

	    Map multiList = new HashMap(0);

	    List<BuCommonPhraseLine> allDepartments = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"Department", "Y"}, "indexNo" );
	    List<BuShop> allShopCodes = buShopDAO.findShopByProperty( loginBrandCode, "", "Y");

	    multiList.put("allDepartments"	, AjaxUtils.produceSelectorData(allDepartments, "lineCode", "name", true, true));
	    multiList.put("allShopCodes"	, AjaxUtils.produceSelectorData(allShopCodes, "shopCode", "shopCName", true, true));

	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("查詢目標佔比/人員設定初始化失敗，原因：" + ex.toString());
	    throw new Exception("查詢目標佔比/人員設定初始化失敗，原因：" + ex.toString());

	}
    }


    /**
     * 查詢目標設定 初始化
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeSearchDeployInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);

	try{
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

	    Map multiList = new HashMap(0);

	    List<BuCommonPhraseLine> allDepartments = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"Department", "Y"}, "indexNo" );
	    List<BuShop> allShopCodes = buShopDAO.findShopByProperty( loginBrandCode, "", "Y");
	    List<BuCommonPhraseLine> allYears = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"BudgetYearList", "Y"}, "indexNo" );
	    List<BuCommonPhraseLine> allMonths = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"Month", "Y"}, "indexNo" );

	    multiList.put("allDepartments"	, AjaxUtils.produceSelectorData(allDepartments, "lineCode", "name", true, true));
	    multiList.put("allShopCodes"	, AjaxUtils.produceSelectorData(allShopCodes, "shopCode", "shopCName", true, true));
	    multiList.put("allYears"		, AjaxUtils.produceSelectorData(allYears, "lineCode", "name", false, true));
	    multiList.put("allMonths"		, AjaxUtils.produceSelectorData(allMonths, "lineCode", "name", false, true));

	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("查詢目標設定初始化失敗，原因：" + ex.toString());
	    throw new Exception("查詢目標設定初始化失敗，原因：" + ex.toString());

	}
    }

    /**
     * 依formId取得實際目標主檔 in 送出
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public BuGoalHead executeFindActualBuGoalHead(Map parameterMap)
    throws FormException, Exception {

//	Object formLinkBean = parameterMap.get("vatBeanFormLink");
	Object otherBean = parameterMap.get("vatBeanOther");

	BuGoalHead buGoalHead = null;
	try {
	    String formIdString = String.valueOf(PropertyUtils.getProperty(otherBean, "formId"));
	    Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;

//	    if(isInitial){
	    buGoalHead = (null == formId) ? executeNewBuGoalHead(otherBean): buGoalHeadDAO.findById(formId) ;
//	    }else{
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
//	    String shopCode = (String) PropertyUtils.getProperty(formBindBean, "shopCode");
//	    String department = (String) PropertyUtils.getProperty(formBindBean, "department");
//	    buGoalHead = buGoalHeadDAO.findOneByShopCodeAndDepartment(loginBrandCode, shopCode, department);
//	    buGoalHead = null == buGoalHead ? executeNewBuGoalHead(otherBean) : buGoalHead;
//	    }

	    log.info( "buGoalHead = " + buGoalHead );

	    parameterMap.put( "entityBean", buGoalHead);
	    return buGoalHead;
	} catch (Exception e) {
	    log.error("取得實際目標主檔失敗,原因:"+e.toString());
	    throw new Exception("取得實際目標主檔失敗,原因:"+e.toString());
	}
    }

    /**
     * 依formId取得實際目標主檔 in 送出
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public BuGoalDeployHead excuteFindActiualBuGoalDeploy(Map parameterMap)
    throws FormException, Exception {

//	Object formLinkBean = parameterMap.get("vatBeanFormLink");
	Object otherBean = parameterMap.get("vatBeanOther");

	BuGoalDeployHead buGoalDeployHead = null;
	try {
	    String formIdString = String.valueOf(PropertyUtils.getProperty(otherBean, "formId"));
	    Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;

	    buGoalDeployHead = (null == formId) ? executeNewBuGoalDeployHead(otherBean): buGoalDeployHeadDAO.findById(formId) ;
	    log.info( "check buGoalDeployHead = " + buGoalDeployHead );

	    parameterMap.put( "entityBean", buGoalDeployHead);
	    return buGoalDeployHead;
	} catch (Exception e) {
	    log.error("取得實際目標主檔失敗,原因:"+e.toString());
	    throw new Exception("取得實際目標主檔失敗,原因:"+e.toString());
	}
    }

    /**
     * 尋找匹配 業績目標定義檔
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> executeMatchBuGoal(Properties httpRequest) throws Exception{
	log.info( "===========<executeMatchBuGoal>===========");
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	Date date = new Date();
	try{
	    // 取得複製時所需的必要資訊
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    String brandCode = httpRequest.getProperty("brandCode");
	    String department = httpRequest.getProperty("department");
	    String shopCode = httpRequest.getProperty("shopCode");

	    log.info( "brandCode = " + brandCode );
	    log.info( "department = " + department );
	    log.info( "shopCode = " + shopCode );

	    BuGoalHead head = buGoalHeadDAO.findOneByShopCodeAndDepartment(brandCode, shopCode, department, "");

	    if( head != null ){
		log.info( " 已存在定義檔 " );
	    }else{
		log.info( " 不存在定義檔 " );

		// 設定單頭 
		head = new BuGoalHead();
		head.setBrandCode(brandCode);
		head.setDepartment(department);
		head.setShopCode(shopCode);
		head.setCreatedBy(loginEmployeeCode);
		head.setCreationDate(date);
		head.setLastUpdatedBy(loginEmployeeCode);
		head.setLastUpdateDate(date);

		head.setStatus(OrderStatus.SAVE); 	// 等待設定成功

		// 跑流程用
		head.setReserve1(AjaxUtils.getTmpOrderNo());
		// 表示在流程內
		head.setReserve2("N");

		buGoalHeadDAO.save(head);
	    }

	    // 取得 上月銷售金額, 去年同月銷售金額
	    properties.setProperty("headId", String.valueOf(head.getHeadId()));
	    properties.setProperty("formStatus", String.valueOf(head.getStatus()));
	    properties.setProperty("statusName", String.valueOf( OrderStatus.getChineseWord( head.getStatus() )));
	    properties.setProperty("reserve2", String.valueOf( head.getReserve2() ));

	    result.add(properties);
	    log.info( "===========<executeMatchBuGoal/>===========");
	    return result;
	}catch(Exception ex){
	    log.error("尋找匹配業績目標定義檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("尋找匹配業績目標定義檔時發生錯誤，原因：" + ex.getMessage());
	}

    }

    /**
     * 尋找匹配 業績目標設定
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> executeMatchBuGoalDeploy(Properties httpRequest) throws Exception{
	log.info( "===========<executeMatchBuGoalDeploy>===========");
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	Date date = new Date();
	try{
	    // 取得複製時所需的必要資訊
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    String brandCode = httpRequest.getProperty("brandCode");
	    String department = httpRequest.getProperty("department");
	    String shopCode = httpRequest.getProperty("shopCode");
	    Long year = NumberUtils.getLong( httpRequest.getProperty("year") );
	    Long month = NumberUtils.getLong(httpRequest.getProperty("month"));
	    Double actualGoal = NumberUtils.getDouble(httpRequest.getProperty("actualGoal"));

	    log.info( "brandCode = " + brandCode );
	    log.info( "department = " + department );
	    log.info( "shopCode = " + shopCode );
	    log.info( "year = " + year );
	    log.info( "month = " + month );
	    log.info( "actualGoal = " + actualGoal );

	    BuGoalDeployHead buGoalDeployHead = buGoalDeployHeadDAO.findOneByShopCodeAndDepartment(brandCode, shopCode, department, year, month);

	    if( buGoalDeployHead != null ){
		log.info( " != null " );
	    }else{
		log.info( " == null " );
		buGoalDeployHead = new BuGoalDeployHead();

		buGoalDeployHead.setBrandCode(brandCode);
		buGoalDeployHead.setStatus(OrderStatus.SAVE);
		buGoalDeployHead.setFlowStatus(OrderStatus.SAVE);
		buGoalDeployHead.setReserve2("N");
		
		buGoalDeployHead.setDepartment(department);
		buGoalDeployHead.setShopCode(shopCode);
		buGoalDeployHead.setYear(year);
		buGoalDeployHead.setMonth(month);

		buGoalDeployHead.setCreatedBy(loginEmployeeCode);
		buGoalDeployHead.setCreationDate(date);
		buGoalDeployHead.setLastUpdatedBy(loginEmployeeCode);
		buGoalDeployHead.setLastUpdateDate(date);

		buGoalDeployHead.setReserve1(AjaxUtils.getTmpOrderNo());
		log.info( " 1=1 " );
		// 上月銷售金額,去年同月銷售金額
		setOtherBuGoalDeployHead(buGoalDeployHead);
		buGoalDeployHead.setOriginalGoal(0D);
		buGoalDeployHead.setActualGoal(buGoalDeployHead.getLastMonthGoal());
		
		log.info( " 1=2 " );
		List<BuGoalDeployLine> buGoalDeployLines = new ArrayList(0);
		log.info( " 1=3 " );
		// 取得匹配到的業績目標定義檔
		BuGoalHead buGoalHead = buGoalHeadDAO.findOneByShopCodeAndDepartment(brandCode, shopCode, department, OrderStatus.FINISH);
		log.info( " 1=4 " );
		
		Long indexNo = 1L;
		log.info( " 1=5 " );
		if(null != buGoalHead){
		    // 將業績目標定義檔的人員設定單身 複製到 業績目標設定單身
		    List<BuGoalEmployeeLine> buGoalEmployeeLines = buGoalHead.getBuGoalEmployeeLines();
		    if(buGoalEmployeeLines != null && buGoalEmployeeLines.size() > 0 ){
			for (BuGoalEmployeeLine buGoalEmployeeLine : buGoalEmployeeLines) {
			    log.info( " 1=5.1 " );
			    BuGoalDeployLine buGoalDeployLine = new BuGoalDeployLine();
			    buGoalDeployLine.setItemCategory(buGoalEmployeeLine.getEmployeeItemCategory());
			    buGoalDeployLine.setItemSubcategory(buGoalEmployeeLine.getEmployeeItemSubcategory());
			    buGoalDeployLine.setItemBrand(buGoalEmployeeLine.getEmployeeItemBrand());
			    log.info( " 1=5.2 " );
			    buGoalDeployLine.setEmployeeCode(buGoalEmployeeLine.getEmployeeCode());
			    buGoalDeployLine.setWorkType(buGoalEmployeeLine.getEmployeeWorkType());
			    buGoalDeployLine.setSigningAmount( OperationUtils.round( buGoalDeployHead.getActualGoal() * buGoalEmployeeLine.getTotalPencent() / 100, 0 ).doubleValue() );
			    buGoalDeployLine.setIsDeleteRecord(AjaxUtils.IS_DELETE_RECORD_FALSE);
			    buGoalDeployLine.setIsLockRecord(AjaxUtils.IS_LOCK_RECORD_FALSE);
			    buGoalDeployLine.setIndexNo(indexNo++);
			    log.info( " 1=5.3 " );
			    buGoalDeployLine.setReserve1( String.valueOf(buGoalEmployeeLine.getTotalPencent()) );	// 以利後續打原目標金額動態改變簽核目標金額

			    buGoalDeployLine.setCreatedBy(loginEmployeeCode);
			    buGoalDeployLine.setCreationDate(date);
			    buGoalDeployLine.setLastUpdatedBy(loginEmployeeCode);
			    buGoalDeployLine.setLastUpdateDate(date);
			    log.info( " 1=5.3 " );
			    buGoalDeployLines.add(buGoalDeployLine);
			}
		    }
		}else{
		    log.info( " 1=6 " );
		    properties.setProperty("reserve1", "" );
		    properties.setProperty("flowStatus", OrderStatus.SAVE );
		    properties.setProperty("flowStatusName", OrderStatus.getChineseWord(OrderStatus.SAVE) );
//		    properties.setProperty("originalGoal", "0");
		    properties.setProperty("actualGoal", "0");
		    properties.setProperty("lastMonthGoal", "0");
		    properties.setProperty("lastYearGoal", "0");
		    properties.setProperty("headId", "" );
		    result.add(properties);
		    // 查無定義檔退回
		    return result;
		}

		buGoalDeployHead.setBuGoalDeployLines(buGoalDeployLines);
		buGoalDeployHeadDAO.save(buGoalDeployHead);

	    }
	    // 取得 上月銷售金額, 去年同月銷售金額
	    properties.setProperty("reserve1", buGoalDeployHead.getReserve1() );
	    properties.setProperty("flowStatus", buGoalDeployHead.getFlowStatus() );
	    properties.setProperty("flowStatusName", OrderStatus.getChineseWord(buGoalDeployHead.getFlowStatus()) );
	    properties.setProperty("originalGoal", AjaxUtils.getPropertiesValue(OperationUtils.roundToStr(buGoalDeployHead.getOriginalGoal().doubleValue(),0),"0"));
	    properties.setProperty("actualGoal", AjaxUtils.getPropertiesValue(OperationUtils.roundToStr(buGoalDeployHead.getActualGoal().doubleValue(),0),"0"));
	    properties.setProperty("lastMonthGoal", AjaxUtils.getPropertiesValue(OperationUtils.roundToStr(buGoalDeployHead.getLastMonthGoal().doubleValue(), 0),"0"));
	    properties.setProperty("lastYearGoal", AjaxUtils.getPropertiesValue(OperationUtils.roundToStr(buGoalDeployHead.getLastYearGoal().doubleValue(), 0),"0"));
	    properties.setProperty("headId", String.valueOf(buGoalDeployHead.getHeadId()));
	    properties.setProperty("reserve2", String.valueOf(buGoalDeployHead.getReserve2()));
	    
	    result.add(properties);
	    log.info( "===========<executeMatchBuGoalDeploy/>===========");
	    return result;
	}catch(Exception ex){
	    ex.printStackTrace();
	    log.error("尋找匹配業績目標定義檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("尋找匹配業績目標定義檔時發生錯誤，原因：" + ex.getMessage());
	}

    }

    /**
     * 解除匹配 業績目標設定
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> executeUnMatchBuGoalDeploy(Properties httpRequest) throws Exception{

	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try{
	    // 取得複製時所需的必要資訊
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    if(headId == 0L){
		throw new ValidationErrorException("無法取得業績目標設定的主鍵值！");
	    }
	    BuGoalDeployHead buGoalDeployHead = buGoalDeployHeadDAO.findById(headId);
	    buGoalDeployHead.setBuGoalDeployLines(new ArrayList(0));
	    buGoalDeployHeadDAO.update(buGoalDeployHead);

	    result.add(properties);
	    return result;
	}catch(Exception ex){
	    log.error("解除匹配業績目標設定時發生錯誤，原因：" + ex.toString());
	    throw new Exception("解除匹配業績目標設定發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 產生一筆 BuGoalHead
     * @param otherBean
     * @return
     * @throws Exception
     */
    public BuGoalHead executeNewBuGoalHead(Object otherBean) throws Exception {
	BuGoalHead head = new BuGoalHead();
	try {
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

	    head.setBrandCode(loginBrandCode);
	    head.setStatus(OrderStatus.SAVE);

	    head.setCreatedBy(loginEmployeeCode);
	    head.setCreationDate(new Date());
	    head.setLastUpdatedBy(loginEmployeeCode);
	    head.setLastUpdateDate(new Date());


	} catch (Exception e) {
	    log.error("建立新目標主檔失敗,原因:"+e.toString());
	    throw new Exception("建立新目標主檔失敗,原因:"+e.toString());
	}
	return head;
    }

    /**
     * 產生一筆 BuGoalDeployHead
     * @param otherBean
     * @return
     * @throws Exception
     */
    public BuGoalDeployHead executeNewBuGoalDeployHead(Object otherBean) throws Exception {
	BuGoalDeployHead head = new BuGoalDeployHead();
	try {
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

	    head.setBrandCode(loginBrandCode);
	    head.setStatus(OrderStatus.SAVE);
	    head.setFlowStatus(OrderStatus.SAVE);

	    head.setLastMonthGoal(0D);
	    head.setLastYearGoal(0D);
	    head.setOriginalGoal(0D);
	    head.setActualGoal(0D);

	    head.setCreatedBy(loginEmployeeCode);
	    head.setCreationDate(new Date());
	    head.setLastUpdatedBy(loginEmployeeCode);
	    head.setLastUpdateDate(new Date());

//	    saveTmpHead(head);

	} catch (Exception e) {
	    log.error("建立新目標設定主檔失敗,原因:"+e.toString());
	    throw new Exception("建立新目標設定主檔失敗,原因:"+e.toString());
	}
	return head;
    } 

    /**
     * 匯入業績目標佔比明細
     * @param headId
     * @param lineLists
     * @throws Exception
     */
    public void executeImportPercentLists(Long headId, List lineLists) throws Exception{
	try{
	    BuGoalHead buGoalHead = buGoalHeadDAO.findById(headId);
	    buGoalHead.setBuGoalPercentLines(new ArrayList(0));
	    buGoalHeadDAO.update(buGoalHead);

	    if(lineLists != null && lineLists.size() > 0){
		BuGoalHead buGoalHeadTmp = new BuGoalHead();
		buGoalHeadTmp.setHeadId(headId);
		for(int i = 0; i < lineLists.size(); i++){
		    BuGoalPercentLine  buGoalPercentLine = (BuGoalPercentLine)lineLists.get(i);
		    buGoalPercentLine.setBuGoalHead(buGoalHeadTmp);
		    buGoalPercentLine.setIndexNo(i+1L);
		    buGoalPercentLineDAO.save(buGoalPercentLine);
		}      	    
	    }     	
	}catch (Exception ex) {
	    log.error("業績目標佔比明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("業績目標佔比明細匯入時發生錯誤，原因：" + ex.getMessage());
	}        
    }

    /**
     * 匯入業績目標人員明細
     * @param headId
     * @param lineLists
     * @throws Exception
     */
    public void executeImportEmployeeLists(Long headId, List lineLists) throws Exception{
	try{
	    BuGoalHead buGoalHead = buGoalHeadDAO.findById(headId);
	    buGoalHead.setBuGoalEmployeeLines(new ArrayList(0));
	    buGoalHeadDAO.update(buGoalHead);

	    if(lineLists != null && lineLists.size() > 0){
		BuGoalHead buGoalHeadTmp = new BuGoalHead();
		buGoalHeadTmp.setHeadId(headId);
		for(int i = 0; i < lineLists.size(); i++){
		    BuGoalEmployeeLine  buGoalEmployeeLine = (BuGoalEmployeeLine)lineLists.get(i);
		    buGoalEmployeeLine.setBuGoalHead(buGoalHeadTmp);
		    buGoalEmployeeLine.setIndexNo(i+1L);
		    buGoalEmployeeLineDAO.save(buGoalEmployeeLine);
		}      	    
	    }     	
	}catch (Exception ex) {
	    log.error("業績目標人員明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("業績目標人員明細匯入時發生錯誤，原因：" + ex.getMessage());
	}        
    }

    /**
     * 目標佔比單身選取業種取得其業種子類
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> findAJAXItemSubcategory(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try{
	    String brandCode = httpRequest.getProperty("brandCode");
	    String itemCategory = httpRequest.getProperty("itemCategory");

	    List allItemSubcategory = imItemCategoryDAO.findByParentCategroyCode( brandCode, itemCategory );
	    allItemSubcategory = AjaxUtils.produceSelectorData(allItemSubcategory, "categoryCode", "categoryName", true, true);
	    properties.setProperty("allItemSubcategory", AjaxUtils.parseSelectorData(allItemSubcategory));
	    result.add(properties);
	    return result;
	}catch(Exception ex){
	    log.error("查詢業種子類發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢業種子類發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 目標佔比明細依業種子類取得其商品品牌
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> findAJAXItemBrand(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try{
	    String brandCode = httpRequest.getProperty("brandCode");
	    String itemBrand = httpRequest.getProperty("itemBrand").trim().toUpperCase();

	    ImItemCategory imItemCategory = imItemCategoryDAO.findByCategoryCode( brandCode, ImItemCategoryDAO.ITEM_BRAND, itemBrand, "Y" );

	    if(imItemCategory != null ){
		properties.setProperty("itemBrandName", imItemCategory.getCategoryName());
		properties.setProperty("itemBrand", itemBrand);
	    }else{
		properties.setProperty("itemBrandName", "查無此商品品牌");
		properties.setProperty("itemBrand", itemBrand);
	    }

	    result.add(properties);
	    return result;
	}catch(Exception ex){
	    log.error("查詢商品品牌發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢商品品牌發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 人員設定單身選取業種取得其業種子類
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> findAJAXEmployeeItemSubcategory(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try{
	    String brandCode = httpRequest.getProperty("brandCode");
	    String itemCategory = httpRequest.getProperty("employeeItemCategory");

	    List allItemSubcategory = imItemCategoryDAO.findByParentCategroyCode( brandCode, itemCategory );
	    allItemSubcategory = AjaxUtils.produceSelectorData(allItemSubcategory, "categoryCode", "categoryName", true, true);
	    properties.setProperty("allItemSubcategory", AjaxUtils.parseSelectorData(allItemSubcategory));
	    result.add(properties);
	    return result;
	}catch(Exception ex){
	    log.error("查詢業種子類發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢業種子類發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 取得CC開窗URL字串
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public List<Properties> getReportConfig(Map parameterMap) throws Exception  {
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
	    String year = (String)PropertyUtils.getProperty(otherBean, "year");
	    DecimalFormat df = new DecimalFormat("00");
	    Long month = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "month"));
	    String shopCode = (String)PropertyUtils.getProperty(otherBean, "shopCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
//	    String orderNo = (String)PropertyUtils.getProperty(otherBean, "orderNo");
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

	    log.info("brandCode = " + brandCode);
	    log.info("year = " + year);
	    log.info("month = " + df.format(month));
	    
	    log.info("shopCode = " + shopCode);
	    log.info("loginEmployeeCode = " + loginEmployeeCode);
	    Map returnMap = new HashMap(0);
	    //CC後面要代的參數使用parameters傳遞			
	    Map parameters = new HashMap(0);
	    log.info("1 = 1" );
	    parameters.put("prompt0", year+df.format(month));
	    parameters.put("prompt1", shopCode);
	    log.info("1 = 2" );
//	    parameters.put("prompt2", "");
//	    parameters.put("prompt3", "");
//	    parameters.put("prompt4", orderNo);
//	    parameters.put("prompt5", orderNo);
	    log.info("1 = 3" );
	    String reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, parameters);
	    returnMap.put("reportUrl", reportUrl);
	    return AjaxUtils.parseReturnDataToJSON(returnMap);
	}catch(IllegalAccessException iae){
	    System.out.println(iae.getMessage());
	    throw new IllegalAccessException(iae.getMessage());
	}catch(InvocationTargetException ite){
	    System.out.println(ite.getMessage());
	    throw new InvocationTargetException(ite, ite.getMessage());
	}catch(NoSuchMethodException nse){
	    System.out.println(nse.getMessage());
	    throw new NoSuchMethodException("NoSuchMethodException:" +nse.getMessage());
	}
    }
    
    /**
     * 透過headId取得業績目標基本檔
     * @param bean
     * @return
     * @throws FormException
     * @throws Exception
     */
    public BuGoalHead getActualBuGoal(Object otherBean ) throws FormException, Exception{

	BuGoalHead head = null;
	String id = (String)PropertyUtils.getProperty(otherBean, "headId");

	if(StringUtils.hasText(id)){
	    Long headId = NumberUtils.getLong(id);
	    head = (BuGoalHead)buGoalHeadDAO.findByPrimaryKey(BuGoalHead.class, headId);
	    if(head  == null){
		throw new NoSuchObjectException("查無業績目標基本檔主鍵：" + headId + "的資料！");
	    }
	}else{
	    throw new ValidationErrorException("傳入的業績目標基本檔主鍵為空值！");
	}
	return head;

    }

    /**
     * 透過headId取得業績目標設定
     * @param bean
     * @return
     * @throws FormException
     * @throws Exception
     */
    public BuGoalDeployHead getActualBuGoalDeploy(Object otherBean ) throws FormException, Exception{

	BuGoalDeployHead buGoalDeployHead = null;
	String id = (String)PropertyUtils.getProperty(otherBean, "headId");

	if(StringUtils.hasText(id)){
	    Long headId = NumberUtils.getLong(id);
	    buGoalDeployHead = (BuGoalDeployHead)buGoalDeployHeadDAO.findByPrimaryKey(BuGoalDeployHead.class, headId);
	    if(buGoalDeployHead  == null){
		throw new NoSuchObjectException("查無業績目標設定主鍵：" + headId + "的資料！");
	    }
	}else{
	    throw new ValidationErrorException("傳入的業績目標設定主鍵為空值！");
	}
	return buGoalDeployHead;

    }

    /**
     * ajax 第一次載入目標佔比明細時,取得分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXPercentPageData(Properties httpRequest) throws Exception{
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();

	    String loginBrandCode = httpRequest.getProperty("loginBrandCode");
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));

	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    HashMap findObjs = new HashMap();
	    findObjs.put("and model.buGoalHead.headId = :headId", headId);

	    Map searchMap = baseDAO.search("BuGoalPercentLine as model", findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE); 
	    List<BuGoalPercentLine> buGoalPercentLines = (List<BuGoalPercentLine>)searchMap.get(BaseDAO.TABLE_LIST); 

	    HashMap map = new HashMap();
	    map.put("headId", headId);

	    if (buGoalPercentLines != null && buGoalPercentLines.size() > 0) {

		setPercentLineOtherColumn(buGoalPercentLines, loginBrandCode);

		// 取得第一筆的INDEX
		Long firstIndex = buGoalPercentLines.get(0).getIndexNo(); 
		// 取得最後一筆 INDEX
		Long maxIndex = (Long)baseDAO.search("BuGoalPercentLine as model", "count(model.buGoalHead.headId) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT);  
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_PERCENT_FIELD_NAMES, GRID_PERCENT_FIELD_DEFAULT_VALUES,buGoalPercentLines, gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_PERCENT_FIELD_NAMES, GRID_PERCENT_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的目標佔比明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的目標佔比明細失敗！");
	}   
    }

    /**
     * ajax 第一次載入人員設定明細時,取得分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXEmployeePageData(Properties httpRequest) throws Exception{
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();

	    String loginBrandCode = httpRequest.getProperty("loginBrandCode");
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));

	    log.info("headId = " + headId);

	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    HashMap findObjs = new HashMap();
	    findObjs.put("and model.buGoalHead.headId = :headId", headId);

	    Map searchMap = baseDAO.search("BuGoalEmployeeLine as model", findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE); 
	    List<BuGoalEmployeeLine> buGoalEmployeeLines = (List<BuGoalEmployeeLine>)searchMap.get(BaseDAO.TABLE_LIST); 

	    HashMap map = new HashMap();
	    map.put("headId", headId);

	    if (buGoalEmployeeLines != null && buGoalEmployeeLines.size() > 0) {

		setLineOtherColumn(buGoalEmployeeLines, loginBrandCode);

		// 取得第一筆的INDEX
		Long firstIndex = buGoalEmployeeLines.get(0).getIndexNo(); 
		// 取得最後一筆 INDEX
		Long maxIndex = (Long)baseDAO.search("BuGoalEmployeeLine as model", "count(model.buGoalHead.headId) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT);  
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_EMPLOYEE_FIELD_NAMES, GRID_EMPLOYEE_FIELD_DEFAULT_VALUES,buGoalEmployeeLines, gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_EMPLOYEE_FIELD_NAMES, GRID_EMPLOYEE_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的人員設定明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的人員設定明細失敗！");
	}   
    }

    /**
     * ajax 第一次載入目標設定明細時,取得分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXDeployPageData(Properties httpRequest) throws Exception{
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));

	    log.info("headId = " + headId);

	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    HashMap findObjs = new HashMap();
	    findObjs.put("and model.buGoalDeployHead.headId = :headId", headId);

	    Map searchMap = baseDAO.search("BuGoalDeployLine as model", findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE); 
	    List<BuGoalDeployLine> buGoalDeployLines = (List<BuGoalDeployLine>)searchMap.get(BaseDAO.TABLE_LIST); 

	    HashMap map = new HashMap();
	    map.put("headId", headId);

	    if (buGoalDeployLines != null && buGoalDeployLines.size() > 0) {

		// 取得第一筆的INDEX
		Long firstIndex = buGoalDeployLines.get(0).getIndexNo(); 
		// 取得最後一筆 INDEX
		Long maxIndex = (Long)baseDAO.search("BuGoalDeployLine as model", "count(model.buGoalDeployHead.headId) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT);  
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_DEPLOY_FIELD_NAMES, GRID_DEPLOY_FIELD_DEFAULT_VALUES,buGoalDeployLines, gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_DEPLOY_FIELD_NAMES, GRID_DEPLOY_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的目標設定明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的目標設定明細失敗！");
	}   
    }

    /**
     * 取得工號
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXEmployee(Properties httpRequest) throws Exception {
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {
	    String department = httpRequest.getProperty("department");
	    String shopCode = httpRequest.getProperty("shopCode");

	    String employeeCode = httpRequest.getProperty("employeeCode").trim().toUpperCase();
	    String employeeName = UserUtils.getUsernameByEmployeeCode(employeeCode);

	    String itemCategory = httpRequest.getProperty("itemCategory");
	    String itemSubcategory = httpRequest.getProperty("itemSubcategory");

	    log.info("shopCode = " + shopCode );
	    log.info("department = " + department );
	    log.info("employeeCode = " + employeeCode );
	    log.info("employeeName = " + employeeName );
	    log.info("itemCategory = " + itemCategory );
	    log.info("itemSubcategory = " + itemSubcategory );

	    if(StringUtils.hasText(employeeCode)){
		BuEmployee buEmployee = (BuEmployee)buEmployeeDAO.findById(employeeCode);

		log.info("buEmployee = " + buEmployee );

		if( buEmployee != null ){
		    properties.setProperty("employeeCode", employeeCode);
		    properties.setProperty("employeeName", employeeName);
		    properties.setProperty("employeeWorkType", StringUtils.hasText(buEmployee.getWorkType()) ? buEmployee.getWorkType() : "");
		}else{
		    properties.setProperty("employeeCode", employeeCode);
		    properties.setProperty("employeeName", "查無此工號");
		    properties.setProperty("employeeWorkType", "");
		}
	    }else{
		properties.setProperty("employeeCode", "");
		properties.setProperty("employeeName", "");
		properties.setProperty("employeeWorkType", "");
	    }

	    result.add(properties);	
	    return result;
	} catch (Exception e) {
	    log.error("取得工號資料發生錯誤，原因：" + e.toString());
	    throw new Exception("取得工號資料失敗");
	}

    }

    /**
     * ajax 取得業績目標定義主檔search分頁
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

	    String department = httpRequest.getProperty("department");
	    String shopCode = httpRequest.getProperty("shopCode");
	    String status = httpRequest.getProperty("status");

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and model.brandCode = :brandCode",brandCode);
	    findObjs.put(" and model.department = :department",department);
	    findObjs.put(" and model.shopCode = :shopCode", shopCode);
	    findObjs.put(" and model.status = :status", status);

	    //==============================================================	    

	    Map searchMap = buGoalHeadDAO.search( "BuGoalHead as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	    List<BuGoalHead> heads = (List<BuGoalHead>) searchMap.get(BaseDAO.TABLE_LIST); 

	    if (heads != null && heads.size() > 0) {

		setBuGoalHeadStatusName(heads);

		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		Long maxIndex = (Long)buGoalHeadDAO.search("BuGoalHead as model", "count(model.headId) as rowCount" ,findObjs, "order by lastUpdateDate desc", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,heads, gridDatas, firstIndex, maxIndex));
	    }else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的業績目標定義主檔查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的業績目標定義主檔查詢失敗！");
	}	
    }

    /**
     * ajax 取得業績目標設定search分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXDeploySearchPageData(Properties httpRequest) throws Exception{

	try{
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    //======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號

	    String status = httpRequest.getProperty("status");
	    String department = httpRequest.getProperty("department");
	    String shopCode = httpRequest.getProperty("shopCode");
	    String year = httpRequest.getProperty("year");
	    String month = httpRequest.getProperty("month");
	    String originalGoalStart = httpRequest.getProperty("originalGoalStart");
	    String originalGoalEnd = httpRequest.getProperty("originalGoalEnd");

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and model.brandCode = :brandCode",brandCode);
	    findObjs.put(" and model.status = :status",status);
	    findObjs.put(" and model.department = :department",department);
	    findObjs.put(" and model.shopCode = :shopCode", shopCode);
	    findObjs.put(" and model.year = :year",year);
	    findObjs.put(" and model.month = :month",month);
	    findObjs.put(" and model.originalGoal >= :originalGoalStart",originalGoalStart);
	    findObjs.put(" and model.originalGoal <= :originalGoalEnd",originalGoalEnd);

	    //==============================================================	    

	    Map searchMap = buGoalDeployHeadDAO.search( "BuGoalDeployHead as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	    List<BuGoalDeployHead> heads = (List<BuGoalDeployHead>) searchMap.get(BaseDAO.TABLE_LIST); 

	    if (heads != null && heads.size() > 0) {

		setBuGoalDeployStatusName(heads);

		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		Long maxIndex = (Long)buGoalDeployHeadDAO.search("BuGoalDeployHead as model", "count(model.headId) as rowCount" ,findObjs, "order by lastUpdateDate desc", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_DEPLOY_FIELD_NAMES, GRID_SEARCH_DEPLOY_FIELD_DEFAULT_VALUES,heads, gridDatas, firstIndex, maxIndex));
	    }else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_DEPLOY_FIELD_NAMES, GRID_SEARCH_DEPLOY_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的業績目標設定查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的業績目標設定查詢失敗！");
	}	
    }

    /**
     * 取得訊息提示用
     * @param headId
     * @return
     * @throws Exception
     */
    public String getIdentificationBasic(Long headId) throws Exception{

	String id = null;
	try{
	    BuGoalHead head = (BuGoalHead)buGoalHeadDAO.findByPrimaryKey(BuGoalHead.class, headId);
	    if(head != null){
		id = MessageStatus.getIdentification(head.getBrandCode(), 
			BU_GOAL_BASIC, head.getReserve1());
	    }else{
		throw new NoSuchDataException("業績目標基本檔主檔查無主鍵：" + headId + "的資料！");
	    }

	    return id;
	}catch(Exception ex){
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
	}	   
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
	    BuGoalDeployHead head = (BuGoalDeployHead)buGoalDeployHeadDAO.findByPrimaryKey(BuGoalDeployHead.class, headId);
	    if(head != null){
		id = MessageStatus.getIdentification(head.getBrandCode(), 
			BU_GOAL_DEPLOY, head.getReserve1());
	    }else{
		throw new NoSuchDataException("業績目標設定主檔查無主鍵：" + headId + "的資料！");
	    }

	    return id;
	}catch(Exception ex){
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
	}	   
    }

    /**
     * ajax picker按檢視返回選取的資料
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
     * 業績目標基本檔依formAction取得下個狀態, 連同設定是在流程內
     */
    public void setNextStatusBasic(BuGoalHead head, String formAction, String approvalResult){

	if(OrderStatus.FORM_SAVE.equals(formAction)){
	    head.setStatus(OrderStatus.SAVE);
	}else if(OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ){
	    if(OrderStatus.SAVE.equals( head.getStatus() )  ){ // || OrderStatus.FINISH.equals( head.getStatus() ) || OrderStatus.REJECT.equals( head.getStatus())
		head.setStatus(OrderStatus.FINISH);
//		head.setStatus(OrderStatus.SIGNING);
	    }else if( OrderStatus.SIGNING.equals(head.getStatus()) ){
		if( "true".equals(approvalResult) ){
//		    head.setStatus(OrderStatus.FINISH);
		}else{
		    head.setStatus(OrderStatus.REJECT);
		}
	    } 
	}

	// 表示在流程內
	head.setReserve2("Y");

    }

    /**
     * 業績目標設定依formAction取得下個狀態
     */
    public void setNextStatus(BuGoalDeployHead head, String formAction, String approvalResult){
	
	if(OrderStatus.FINISH.equalsIgnoreCase(head.getStatus())){
	    if(OrderStatus.FORM_SAVE.equals(formAction)){
		head.setFlowStatus(OrderStatus.SAVE);
		// 表示在流程內
		head.setReserve2("Y");
	    }else if(OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ){
		if(OrderStatus.SAVE.equals( head.getFlowStatus() ) || OrderStatus.FINISH.equals( head.getFlowStatus() ) || OrderStatus.REJECT.equals( head.getFlowStatus() )){
		    head.setFlowStatus(OrderStatus.SIGNING);
		    // 表示在流程內
		    head.setReserve2("Y");
		}else if( OrderStatus.SIGNING.equals(head.getFlowStatus()) ){
		    if( "true".equals(approvalResult) ){
			head.setFlowStatus(OrderStatus.FINISH);
			//流程結束
			head.setReserve2("N");
		    }else{
			head.setFlowStatus(OrderStatus.REJECT);
			// 表示在流程內
			head.setReserve2("Y");
		    }
		} 
	    }
	}else{
	    if(OrderStatus.FORM_SAVE.equals(formAction)){
		head.setStatus(OrderStatus.SAVE);
		head.setFlowStatus(OrderStatus.SAVE);
		// 表示在流程內
		head.setReserve2("Y");
	    }else if(OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ){
		if(OrderStatus.SAVE.equals( head.getStatus() ) || OrderStatus.FINISH.equals( head.getStatus() ) || OrderStatus.REJECT.equals( head.getStatus() )){
		    head.setStatus(OrderStatus.SIGNING);
		    head.setFlowStatus(OrderStatus.SIGNING);
		    // 表示在流程內
		    head.setReserve2("Y");
		}else if( OrderStatus.SIGNING.equals(head.getStatus()) ){
		    if( "true".equals(approvalResult) ){
			head.setStatus(OrderStatus.FINISH);
			head.setFlowStatus(OrderStatus.FINISH);
			//流程結束
			head.setReserve2("N");
		    }else{
			head.setStatus(OrderStatus.REJECT);
			head.setFlowStatus(OrderStatus.REJECT);
			// 表示在流程內
			head.setReserve2("Y");
		    }
		} 
	    }
	}
	
    }

    /**
     * 目標佔比明細檔 其他欄位(商品品牌)
     * @param imAdjustmentLines
     * @throws Exception
     */
    private void setPercentLineOtherColumn(List<BuGoalPercentLine> buGoalPercentLines, String loginBrandCode)throws Exception{
	try{
	    for (BuGoalPercentLine buGoalPercentLine : buGoalPercentLines) {
//		String itemSubcategory = buGoalPercentLine.getItemSubcategory();
		String itemBrand = buGoalPercentLine.getItemBrand();

		if( StringUtils.hasText(itemBrand) ){

		    ImItemCategory imItemCategory = imItemCategoryDAO.findByCategoryCode( loginBrandCode, ImItemCategoryDAO.ITEM_BRAND, itemBrand, "Y" );
		    if(imItemCategory != null ){
			buGoalPercentLine.setItemBrandName(imItemCategory.getCategoryName());
		    }else{
			buGoalPercentLine.setItemBrandName("查無此商品品牌");
		    }

		}else{
		    buGoalPercentLine.setItemBrandName("");
		}
	    }
	}catch(Exception ex){
	    log.error("目標佔比明細檔其他欄位時發生錯誤，原因：" + ex.toString());
	    throw new Exception("目標佔比明細檔其他欄位時發生錯誤，原因：" + ex.toString());
	}
    }

    /**
     * 人員設定明細檔其他欄位(姓名)
     * @param imAdjustmentLines
     * @throws Exception
     */
    private void setLineOtherColumn(List<BuGoalEmployeeLine> buGoalEmployeeLines, String loginBrandCode)throws Exception{
	try{
	    for (BuGoalEmployeeLine buGoalEmployeeLine : buGoalEmployeeLines) {
		String employeeCode = buGoalEmployeeLine.getEmployeeCode();

		if( StringUtils.hasText(employeeCode) ){
		    String employeeName = UserUtils.getUsernameByEmployeeCode(employeeCode);
		    if( "unknow".equals( employeeName )){
			buGoalEmployeeLine.setEmployeeName("查無此工號");
		    }else{
			buGoalEmployeeLine.setEmployeeName( employeeName);
		    }
		}else{
		    buGoalEmployeeLine.setEmployeeName("");
		}

		String itemBrand = buGoalEmployeeLine.getEmployeeItemBrand();

		if( StringUtils.hasText(itemBrand) ){

		    ImItemCategory imItemCategory = imItemCategoryDAO.findByCategoryCode( loginBrandCode, ImItemCategoryDAO.ITEM_BRAND, itemBrand, "Y" );
		    if(imItemCategory != null ){
			buGoalEmployeeLine.setEmployeeItemBrandName(imItemCategory.getCategoryName());
		    }else{
			buGoalEmployeeLine.setEmployeeItemBrandName("查無此商品品牌");
		    }

		}else{
		    buGoalEmployeeLine.setEmployeeItemBrandName("");
		}

	    }
	}catch(Exception ex){
	    log.error("人員設定明細檔其他欄位時發生錯誤，原因：" + ex.toString());
	    throw new Exception("人員設定明細檔其他欄位時發生錯誤，原因：" + ex.toString());
	}
    }

    /**
     * 業績目標設定(狀態)
     * @param heads
     * @throws Exception
     */
    private void setBuGoalDeployStatusName(List<BuGoalDeployHead> heads){
	for (BuGoalDeployHead buGoalDeployHead : heads) {
	    buGoalDeployHead.setStatusName(OrderStatus.getChineseWord(buGoalDeployHead.getStatus()));
	}
    }

    /**
     * 業績目標佔比 設定中文狀態名稱
     * @param buGoalHeads
     */
    private void setBuGoalHeadStatusName(List<BuGoalHead> buGoalHeads){
	for (BuGoalHead buGoalHead : buGoalHeads) {
	    buGoalHead.setStatusName(OrderStatus.getChineseWord(buGoalHead.getStatus()));
	}
    }

    /**
     * 設定業績目標設定額外欄位(上月銷售金額,去年同月銷售金額)
     * @param buGoalDeployHead
     */
    private void setOtherBuGoalDeployHead(BuGoalDeployHead buGoalDeployHead){
	String brandCdoe = buGoalDeployHead.getBrandCode();
	String shopCode = buGoalDeployHead.getShopCode();
	Long year = buGoalDeployHead.getYear();
	Long month = buGoalDeployHead.getMonth();

	buGoalDeployHead.setLastMonthGoal( OperationUtils.round(imDeliveryHeadDAO.getLastMonthGoal(brandCdoe,shopCode, year, month), 0).doubleValue() );
	buGoalDeployHead.setLastYearGoal(  OperationUtils.round(imDeliveryHeadDAO.getLastYearGoal(brandCdoe,shopCode, year, month), 0).doubleValue() ) ; 
    }

    /**
     * 將業績目標社設定主檔查詢結果存檔
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
     * 將業績目標社設定主檔查詢結果存檔
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveDeploySearchResult(Properties httpRequest) throws Exception{
	String errorMsg = null;
	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_DEPLOY_FIELD_NAMES);
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * 流程結束前,改成FINISH狀態
     * @param headId
     */
    public void saveFinishStatus(Long headId){
	try {
	    BuGoalHead head = buGoalHeadDAO.findById(headId);
	    head.setStatus(OrderStatus.FINISH);
	    head.setReserve2("N");	// 表示流程結束
	    buGoalDeployHeadDAO.update(head);
	} catch (Exception e) {
	    log.error("流程內更新狀態發生錯誤，原因：" + e.toString());
	}
    }

    /**
     * 業績目標基本檔,啟動流程
     * @param form
     * @return
     * @throws ProcessFailedException
     */
    public static Object[] startProcessBasic(BuGoalHead form) throws ProcessFailedException{       
	try{           
	    String packageId = "Bu_GoalDeploy";         
	    String processId = "basicApproval";           
	    String version = "20100713";
	    String sourceReferenceType = "BuGoalBasic";
	    HashMap context = new HashMap();	    
	    context.put("formId", form.getHeadId());
	    return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
	}catch (Exception ex){
	    log.error("業績目標基本檔流程啟動失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("業績目標基本檔流程啟動失敗！");
	}	      
    }

    /**
     * 業績目標個積,啟動流程
     * @param form
     * @return
     * @throws ProcessFailedException
     */
    public static Object[] startProcess(BuGoalDeployHead form) throws ProcessFailedException{       
	try{           
	    String packageId = "Bu_GoalDeploy";         
	    String processId = "approval";           
	    String version = "20091203";
	    String sourceReferenceType = "BuGoalDeploy";
	    HashMap context = new HashMap();	    
	    context.put("formId", form.getHeadId());
	    return null;//ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
	}catch (Exception ex){
	    ex.printStackTrace();
	    log.error("業績目標設定流程啟動失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("業績目標設定流程啟動失敗！"+ ex.getMessage());
	}	      
    }

    /**
     * ajax  更新目標佔比明細
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXPercentPageLinesData(Properties httpRequest) throws Exception{ 
	String errorMsg = null;
	try {
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    String status = httpRequest.getProperty("status");

	    if (headId == null) {
		throw new ValidationErrorException("傳入的目標主鍵為空值！");
	    }

	    // 將STRING資料轉成List Properties record data
	    List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_PERCENT_FIELD_NAMES);
	    // Get INDEX NO 取得最後一筆 INDEX
//	    int indexNo = buGoalPercentLineDAO.findPageLineMaxIndex(headId).intValue();

	    List<BuGoalPercentLine> result = baseDAO.findByProperty("BuGoalPercentLine", "buGoalHead.headId", headId, "indexNo");
	    int indexNo = result != null && result.size() > 0 ? result.get( result.size() - 1 ).getIndexNo().intValue() : 0;
	    log.info( "MaxIndexNo = " + indexNo );

	    if(OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)){
		// 考慮狀態
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
			// 先載入HEAD_ID OR LINE DATA

			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));

			String itemCategory = upRecord.getProperty(GRID_PERCENT_FIELD_NAMES[1]);

			if (StringUtils.hasText(itemCategory)) {

			    BuGoalPercentLine buGoalPercentLine = (BuGoalPercentLine)buGoalPercentLineDAO.findFirstByProperty("BuGoalPercentLine", "and buGoalHead.headId = ? and lineId = ?", new Object[]{ headId, lineId } );
			    log.info( "buGoalPercentLine = " + buGoalPercentLine + "\nlineId = " + lineId);
			    Date date = new Date();
			    if ( buGoalPercentLine != null ) {
				log.info( "更新 = " + headId + " | "+ lineId  );
				AjaxUtils.setPojoProperties(buGoalPercentLine,upRecord, GRID_PERCENT_FIELD_NAMES, GRID_PERCENT_FIELD_TYPES);
				buGoalPercentLine.setLastUpdatedBy(loginEmployeeCode);
				buGoalPercentLine.setLastUpdateDate(date);
				buGoalPercentLineDAO.update(buGoalPercentLine);

			    } else {
				indexNo++;
				log.info( "新增 = " + headId + " | "+  indexNo);
				buGoalPercentLine = new BuGoalPercentLine(); 

				AjaxUtils.setPojoProperties(buGoalPercentLine,upRecord, GRID_PERCENT_FIELD_NAMES,GRID_PERCENT_FIELD_TYPES);
				buGoalPercentLine.setBuGoalHead(new BuGoalHead(headId));
				buGoalPercentLine.setCreatedBy(loginEmployeeCode);
				buGoalPercentLine.setCreationDate(date);
				buGoalPercentLine.setLastUpdatedBy(loginEmployeeCode);
				buGoalPercentLine.setLastUpdateDate(date);
				buGoalPercentLine.setIndexNo(Long.valueOf(indexNo));
				buGoalPercentLineDAO.save(buGoalPercentLine);

			    }
			}
		    }
		}
	    }
	    return AjaxUtils.getResponseMsg(errorMsg);

	} catch (Exception ex) {
	    log.error("更新目標佔比明細資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新目標佔比明細資料失敗！");
	}   
    }

    /**
     * ajax  更新人員設定明細
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXEmployeePageLinesData(Properties httpRequest) throws Exception{ 
	String errorMsg = null;
	try {
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    String status = httpRequest.getProperty("status");

	    if (headId == null) {
		throw new ValidationErrorException("傳入的目標主鍵為空值！");
	    }

	    // 將STRING資料轉成List Properties record data
	    List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_EMPLOYEE_FIELD_NAMES);
	    // Get INDEX NO 取得最後一筆 INDEX
//	    int indexNo = buGoalEmployeeLineDAO.findPageLineMaxIndex(headId).intValue();

	    List<BuGoalEmployeeLine> result = baseDAO.findByProperty("BuGoalEmployeeLine", "buGoalHead.headId", headId, "indexNo");
	    int indexNo = result != null && result.size() > 0 ? result.get( result.size() - 1 ).getIndexNo().intValue() : 0;
	    log.info( "MaxIndexNo = " + indexNo );

	    if(OrderStatus.SAVE.equals(status)){
		// 考慮狀態
		if (upRecords != null) {
		    log.info( " 0 " );
		    for (Properties upRecord : upRecords) {
			// 先載入HEAD_ID OR LINE DATA

			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));

			String itemCategory = upRecord.getProperty(GRID_EMPLOYEE_FIELD_NAMES[1]);

			if (StringUtils.hasText(itemCategory)) {

			    BuGoalEmployeeLine buGoalEmployeeLine = (BuGoalEmployeeLine)buGoalEmployeeLineDAO.findFirstByProperty("BuGoalEmployeeLine", "and buGoalHead.headId = ? and lineId = ?", new Object[]{ headId, lineId } );
			    log.info( "buGoalEmployeeLine = " + buGoalEmployeeLine + "\nlineId = " + lineId);
			    Date date = new Date();
			    if ( buGoalEmployeeLine != null ) {
				log.info( "更新 = " + headId + " | "+ lineId  );
				AjaxUtils.setPojoProperties(buGoalEmployeeLine,upRecord, GRID_EMPLOYEE_FIELD_NAMES, GRID_EMPLOYEE_FIELD_TYPES);
				buGoalEmployeeLine.setLastUpdatedBy(loginEmployeeCode);
				buGoalEmployeeLine.setLastUpdateDate(date);
				buGoalEmployeeLineDAO.update(buGoalEmployeeLine);

			    } else {
				indexNo++;
				log.info( "新增 = " + headId + " | "+  indexNo);
				buGoalEmployeeLine = new BuGoalEmployeeLine(); 

				AjaxUtils.setPojoProperties(buGoalEmployeeLine,upRecord, GRID_EMPLOYEE_FIELD_NAMES,GRID_EMPLOYEE_FIELD_TYPES);
				buGoalEmployeeLine.setBuGoalHead(new BuGoalHead(headId));
				buGoalEmployeeLine.setCreatedBy(loginEmployeeCode);
				buGoalEmployeeLine.setCreationDate(date);
				buGoalEmployeeLine.setLastUpdatedBy(loginEmployeeCode);
				buGoalEmployeeLine.setLastUpdateDate(date);
				buGoalEmployeeLine.setIndexNo(Long.valueOf(indexNo));
				buGoalEmployeeLineDAO.save(buGoalEmployeeLine);

			    }
			}
		    }
		}
	    }

	    return AjaxUtils.getResponseMsg(errorMsg);

	} catch (Exception ex) {
	    log.error("更新人員設定明細資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新人員設定明細資料失敗！");
	}   
    }

    /**
     * ajax  更新目標設定明細 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXDeployPageLinesData(Properties httpRequest) throws Exception{ 
	String errorMsg = null;
	try {
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");

	    if (headId == null) {
		throw new ValidationErrorException("傳入的目標主鍵為空值！");
	    }

	    // 將STRING資料轉成List Properties record data
	    List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_DEPLOY_FIELD_NAMES);
	    // Get INDEX NO 取得最後一筆 INDEX
//	    int indexNo = buGoalEmployeeLineDAO.findPageLineMaxIndex(headId).intValue();

	    List<BuGoalDeployLine> result = buGoalDeployLineDAO.findBuGoalDeployLine( headId );
	    int indexNo = result != null && result.size() > 0 ? result.get( result.size() - 1 ).getIndexNo().intValue() : 0;
	    log.info( "MaxIndexNo = " + indexNo );

	    // 考慮狀態
	    if (upRecords != null) {
		for (Properties upRecord : upRecords) {
		    // 先載入HEAD_ID OR LINE DATA

		    Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));

		    String employeeCode = upRecord.getProperty(GRID_DEPLOY_FIELD_NAMES[1]);

		    if (StringUtils.hasText(employeeCode)) {

			BuGoalDeployLine buGoalDeployLine = buGoalDeployLineDAO.findOneBuGoalDeployLine(headId, lineId );
			log.info( "buGoalDeployLine = " + buGoalDeployLine + "\nlineId = " + lineId);
			Date date = new Date();
			if ( buGoalDeployLine != null ) {
			    log.info( "更新 = " + headId + " | "+ lineId  );
			    AjaxUtils.setPojoProperties(buGoalDeployLine,upRecord, GRID_DEPLOY_FIELD_NAMES, GRID_DEPLOY_FIELD_TYPES);
			    buGoalDeployLine.setLastUpdatedBy(loginEmployeeCode);
			    buGoalDeployLine.setLastUpdateDate(date);
			    buGoalDeployLineDAO.update(buGoalDeployLine);

			} else {
//			    indexNo++;
//			    log.info( "新增 = " + headId + " | "+  indexNo);
//			    buGoalDeployLine = new BuGoalDeployLine(); 

//			    AjaxUtils.setPojoProperties(buGoalDeployLine,upRecord, GRID_DEPLOY_FIELD_NAMES,GRID_DEPLOY_FIELD_TYPES);
//			    buGoalDeployLine.setBuGoalHead(new BuGoalHead(headId));
//			    buGoalDeployLine.setCreatedBy(loginEmployeeCode);
//			    buGoalDeployLine.setCreationDate(date);
//			    buGoalDeployLine.setLastUpdatedBy(loginEmployeeCode);
//			    buGoalDeployLine.setLastUpdateDate(date);
//			    buGoalDeployLine.setIndexNo(Long.valueOf(indexNo));
//			    buGoalDeployLineDAO.save(buGoalDeployLine);

			}
		    }
		}
	    }

	    return AjaxUtils.getResponseMsg(errorMsg);

	} catch (Exception ex) {
	    log.error("更新目標設定明細資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新目標設定明細資料失敗！");
	}   
    }

    /**
     * ajax  業績目標設定 更新LINE 簽核目標金額
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXSigningAmount(Properties httpRequest) throws Exception{ 
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {
	    Long headId = NumberUtils.getLong( httpRequest.getProperty("headId") );
	    Double actualGoal = OperationUtils.round(NumberUtils.getDouble(httpRequest.getProperty("actualGoal")), 0).doubleValue();

	    BuGoalDeployHead head = buGoalDeployHeadDAO.findById(headId);

	    List<BuGoalDeployLine> Lines = head.getBuGoalDeployLines();

	    for (BuGoalDeployLine buGoalDeployLine : Lines) {
		buGoalDeployLine.setSigningAmount( OperationUtils.round(NumberUtils.getDouble( buGoalDeployLine.getReserve1()) / 100 * actualGoal , 0).doubleValue() );
	    }
	    buGoalDeployHeadDAO.update(head);

//	    properties.setProperty("originalGoal", OperationUtils.roundToStr(actualGoal,0));
	    properties.setProperty("actualGoal", OperationUtils.roundToStr(actualGoal,0));
	    result.add(properties);	
	    return result;

	} catch (Exception ex) {
	    log.error("取得業績目標資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得業績目標資料失敗！");
	}   
    }

    /**
     * ajax  業績目標設定 更新本月目標,原目標金額 更新LINE 簽核目標金額,
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXPercent(Properties httpRequest) throws Exception{ 
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {
	    Long headId = NumberUtils.getLong( httpRequest.getProperty("headId") );
	    Double lastMonthGoal = OperationUtils.round(NumberUtils.getDouble(httpRequest.getProperty("lastMonthGoal")), 0).doubleValue();
	    Double percent = NumberUtils.getDouble(httpRequest.getProperty("percent"));

	    lastMonthGoal = lastMonthGoal * (1 + percent/100);

	    BuGoalDeployHead head = buGoalDeployHeadDAO.findById(headId);

	    List<BuGoalDeployLine> Lines = head.getBuGoalDeployLines();

	    for (BuGoalDeployLine buGoalDeployLine : Lines) {
		buGoalDeployLine.setSigningAmount( OperationUtils.round(NumberUtils.getDouble( buGoalDeployLine.getReserve1()) / 100 * lastMonthGoal , 0).doubleValue() );
	    }
	    buGoalDeployHeadDAO.update(head);

//	    properties.setProperty("originalGoal", OperationUtils.roundToStr(lastMonthGoal,0));
	    properties.setProperty("actualGoal", OperationUtils.roundToStr(lastMonthGoal,0));
	    result.add(properties);	
	    return result;

	} catch (Exception ex) {
	    log.error("取得業績目標資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得業績目標資料失敗！");
	}   
    }

    /**
     * ajax  業績目標設定 更新HEAD 原始目標金額
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXOriginalGoal(Properties httpRequest) throws Exception{ 
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	Double actualGoal = 0.0d;
	try {
	    Long headId = NumberUtils.getLong( httpRequest.getProperty("headId") );
	    Double signingAmount = OperationUtils.round(NumberUtils.getDouble(httpRequest.getProperty("signingAmount")), 0).doubleValue();

	    BuGoalDeployHead head = buGoalDeployHeadDAO.findById(headId);
	    List<BuGoalDeployLine> buGoalDeployLines = head.getBuGoalDeployLines();
	    for (BuGoalDeployLine buGoalDeployLine : buGoalDeployLines) {
		actualGoal += buGoalDeployLine.getSigningAmount();
	    }

	    properties.setProperty("actualGoal", OperationUtils.roundToStr(actualGoal,0));
	    properties.setProperty("signingAmount", OperationUtils.roundToStr(signingAmount,0));
	    result.add(properties);	
	    return result;

	} catch (Exception ex) {
	    log.error("取得業績目標資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得業績目標資料失敗！");
	}   
    }

    /**
     * ajax  業績目標設定 更新HEAD 原始目標金額
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateHead(Properties httpRequest) throws Exception{ 
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	Date date = new Date();
	try {
	    Long headId = NumberUtils.getLong( httpRequest.getProperty("headId") );
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    String brandCode = httpRequest.getProperty("brandCode");
	    
	    BuGoalHead head = buGoalHeadDAO.findById(headId);
	    head.setStatus(OrderStatus.SAVE);
	    head.setLastUpdatedBy(loginEmployeeCode);
	    head.setLastUpdateDate(date);
	    
	    List<BuGoalEmployeeLine> buGoalEmployeeLines = head.getBuGoalEmployeeLines();
	    for (BuGoalEmployeeLine buGoalEmployeeLine : buGoalEmployeeLines) {
		buGoalEmployeeLine.setLastUpdatedBy(loginEmployeeCode);
		buGoalEmployeeLine.setLastUpdateDate(date);
	    }
	    
	    List<BuGoalPercentLine> buGoalPercentLines = head.getBuGoalPercentLines();
	    for (BuGoalPercentLine buGoalPercentLine : buGoalPercentLines) {
		buGoalPercentLine.setLastUpdatedBy(loginEmployeeCode);
		buGoalPercentLine.setLastUpdateDate(date);
	    }
	    buGoalHeadDAO.update(head);
	    
	    properties.setProperty("formStatus", OrderStatus.SAVE);
	    properties.setProperty("statusName", OrderStatus.getChineseWord(OrderStatus.SAVE));
	    result.add(properties);	
	    return result;

	} catch (Exception ex) {
	    log.error("取得業績目標資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得業績目標資料失敗！");
	}   
    }
    
    /**
     * ajax  業績目標金額設定 更新HEAD
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateDeployHead(Properties httpRequest) throws Exception{ 
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	Date date = new Date();
	try {
	    Long headId = NumberUtils.getLong( httpRequest.getProperty("headId") );
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
//	    String brandCode = httpRequest.getProperty("brandCode");
	    
	    BuGoalDeployHead head = buGoalDeployHeadDAO.findById(headId);
	    head.setFlowStatus(OrderStatus.SAVE);
	    head.setLastUpdatedBy(loginEmployeeCode);
	    head.setLastUpdateDate(date);
	    
	    List<BuGoalDeployLine> buGoalDeployLines = head.getBuGoalDeployLines();
	    for (BuGoalDeployLine buGoalDeployLine : buGoalDeployLines) {
		buGoalDeployLine.setLastUpdatedBy(loginEmployeeCode);
		buGoalDeployLine.setLastUpdateDate(date);
	    }
	    buGoalDeployHeadDAO.update(head);
	    
	    properties.setProperty("flowStatus", OrderStatus.SAVE);
	    properties.setProperty("flowStatusName", OrderStatus.getChineseWord(OrderStatus.SAVE));
	    properties.setProperty("headId", headId.toString());
	    result.add(properties);	
	    return result;

	} catch (Exception ex) {
	    ex.printStackTrace();
	    log.error("取得業績目標金額資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得業績目標金額資料失敗！");
	}   
    }
    
    /**
     * 前端資料塞入bean
     * @param parameterMap
     * @return
     */
    public Map updateBuGoalBean(Map parameterMap)throws FormException, Exception {
	try{
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
//	    Object otherBean = parameterMap.get("vatBeanOther");

	    String headIdString = String.valueOf(PropertyUtils.getProperty(formLinkBean, "headId"));
	    Long headId = StringUtils.hasText(headIdString) ? Long.valueOf(headIdString) : null;

	    BuGoalHead buGoalHead = buGoalHeadDAO.findById(headId); 

	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buGoalHead);

	    parameterMap.put("entityBean", buGoalHead);

	    return parameterMap; 
	} catch (FormException fe) {
	    log.error("前端資料塞入bean失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
	    throw new Exception("目標資料塞入bean發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 前端資料塞入bean
     * @param parameterMap
     * @return
     */
    public Map updateBuGoalDeployBean(Map parameterMap)throws FormException, Exception {
	try{
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
//	    Object otherBean = parameterMap.get("vatBeanOther");
	    BuGoalDeployHead buGoalDeployHead = getActualBuGoalDeploy(formLinkBean); 
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buGoalDeployHead);
	    parameterMap.put("entityBean", buGoalDeployHead);
	    return parameterMap;    
	} catch (FormException fe) {
	    log.error("前端資料塞入bean失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
	    throw new Exception("目標資料塞入bean發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     *  業績目標基本檔,取單號後更新更新主檔
     * 
     * @param parameterMap
     * @return Map
     * @throws FormException
     * @throws Exception
     */
    public Map updateBuGoalWithActualOrderNO(Map parameterMap) throws FormException, Exception {

	Map resultMap = new HashMap();
	try{

	    resultMap = updateBuGoalBean(parameterMap); 
	    BuGoalHead head = (BuGoalHead) resultMap.get("entityBean");

	    //刪除於SI_PROGRAM_LOG的原識別碼資料
	    String identification = MessageStatus.getIdentification(head.getBrandCode(), 
		    BU_GOAL_BASIC, head.getReserve1());
	    siProgramLogAction.deleteProgramLog(BASIC_PROGRAM_ID, null, identification);

	    String resultMsg = "部門:" + head.getDepartment() + " 專櫃代號:" +  head.getShopCode() + "存檔成功！是否繼續新增？";
	    resultMap.put("resultMsg", resultMsg);

	    return resultMap;      
	} catch (FormException fe) {
	    log.error("業績目標設定存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("業績目標設定存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("業績目標設定存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     *  取單號後更新更新主檔
     * 
     * @param parameterMap
     * @return Map
     * @throws FormException
     * @throws Exception
     */
    public Map updateBuGoalDeployWithActualOrderNO(Map parameterMap) throws FormException, Exception {

	Map resultMap = new HashMap();
	try{

	    resultMap = updateBuGoalDeployBean(parameterMap); 
	    BuGoalDeployHead head = (BuGoalDeployHead) resultMap.get("entityBean");

	    //刪除於SI_PROGRAM_LOG的原識別碼資料
	    String identification = MessageStatus.getIdentification(head.getBrandCode(), 
		    BU_GOAL_DEPLOY, head.getReserve1());
	    siProgramLogAction.deleteProgramLog(DEPLOY_PROGRAM_ID, null, identification);

	    String resultMsg = head.getReserve1() + "存檔成功！是否繼續新增？";
	    resultMap.put("resultMsg", resultMsg);

	    return resultMap;      
	} catch (FormException fe) {
	    log.error("業績目標設定存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("業績目標設定存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("業績目標設定存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 目標存檔
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map updateAJAXBuGoal(Map parameterMap) throws Exception {

	MessageBox msgBox = new MessageBox();
	HashMap resultMap = new HashMap(0);
	List errorMsgs = null;
	String resultMsg = null;
	try {
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
	    String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");

	    BuGoalHead head = getActualBuGoal(formLinkBean); 

	    if(OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ){
		// 清除line
		deletePercentAndEmployeeLine(head);
		// 驗證
		errorMsgs  = checkedBuGoal(parameterMap, approvalResult);

	    }

	    if( errorMsgs == null || errorMsgs.size() == 0 ){
		// 成功則設定下個狀態 
		setNextStatusBasic(head, formAction, approvalResult); 

		// 更新業績目標基本檔主檔明細檔
		if( ( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ) ){
		    updateBuGoal(head, loginEmployeeCode);
		}
		resultMsg = "部門:" + head.getDepartment() + " 專櫃代號:" +  head.getShopCode() + "存檔成功！ 是否繼續新增？";
	    }else if( errorMsgs.size() > 0 ){
		if( OrderStatus.FORM_SUBMIT.equals(formAction) ){ 
		    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		}
	    }

	    resultMap.put("resultMsg", resultMsg);
	    resultMap.put("entityBean", head);
	    resultMap.put("vatMessage", msgBox);
	    return resultMap;

	} catch( ValidationErrorException ve ){
	    log.error("業績目標基本檔檢核時發生錯誤，原因：" + ve.toString());
	    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	} catch (Exception ex) {
	    log.error("業績目標基本檔存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception( ex.toString());
	}
    }

    /**
     * 檢核,存取業績目標設定主檔,明細檔,重新設定狀態
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map updateAJAXBuGoalDeploy(Map parameterMap)throws Exception{
	Map resultMap = new HashMap();
	List errorMsgs = null;
	String resultMsg = null;
	try {

	    Object otherBean = parameterMap.get("vatBeanOther");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
//	    String beforeStatus	= (String)PropertyUtils.getProperty(otherBean, "beforeStatus");
	    String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");

	    BuGoalDeployHead head = getActualBuGoalDeploy(formLinkBean); 

	    log.info("start check");
	    // 檢核
	    if( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ){
		deleteLine(head); 
		errorMsgs  = checkedBuGoalDeploy(parameterMap, approvalResult);
	    }
	    log.info("end check");
	    if( errorMsgs == null || errorMsgs.size() == 0 ){
		// 成功則設定下個狀態 
		setNextStatus(head, formAction, approvalResult); 
		log.info("end status");
		// 更新調整單主檔明細檔,im和cm庫存  
		if( ( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ) ){

		    // 更新業績目標設定主檔明細檔
		    updateBuGoalDeploy(head, employeeCode );
		    log.info("end update");
		}
		DecimalFormat df = new DecimalFormat("00");
		String yearMonth = head.getYear() + df.format(head.getMonth());
		resultMsg =  head.getDepartment() +"-"+head.getShopCode() +"-"+ yearMonth + "存檔成功！ 是否繼續新增？"; //"Order No：" + head.getBrandCode() + BU_GOAL_DEPLOY + head.getReserve1()
		log.info("end1");
	    } else if( errorMsgs.size() > 0 ){
		if( OrderStatus.FORM_SUBMIT.equals(formAction) ){ 
		    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		}
	    }
	    log.info("end2");
	    resultMap.put("entityBean", head);
	    resultMap.put("resultMsg", resultMsg);
	} catch( ValidationErrorException ve ){
	    log.error("業績目標設定檢核時發生錯誤，原因：" + ve.toString());
	    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	} catch (Exception e) {
	    log.error("業績目標設定存檔時發生錯誤，原因：" + e.toString());
	    throw new Exception("業績目標設定存檔時發生錯誤，原因：" + e.getMessage());
	}

	return resultMap;
    }

    /**
     * 更新業績目標定義檔主檔明細檔
     * @param head
     * @param employeeCode
     */
    private void updateBuGoal(BuGoalHead head, String employeeCode )throws Exception{
	try{
	    Date date = new Date();
	    if (head.getHeadId() != null) {
		head.setLastUpdateDate(date);
		head.setLastUpdatedBy(employeeCode);

		List<BuGoalPercentLine> buGoalPercentLines = head.getBuGoalPercentLines();
		for (BuGoalPercentLine buGoalPercentLine : buGoalPercentLines) {
		    buGoalPercentLine.setLastUpdateDate(date);
		    buGoalPercentLine.setLastUpdatedBy(employeeCode);
		}
		List<BuGoalEmployeeLine> buGoalEmployeeLines = head.getBuGoalEmployeeLines();
		for (BuGoalEmployeeLine buGoalEmployeeLine : buGoalEmployeeLines) {
		    buGoalEmployeeLine.setLastUpdateDate(date);
		    buGoalEmployeeLine.setLastUpdatedBy(employeeCode);
		}

		buGoalHeadDAO.merge(head);
	    }
	}catch (Exception ex) {
	    log.error("業績目標定義檔主檔明細檔存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("業績目標定義檔主檔明細檔存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 更新業績目標設定主檔明細檔
     * @param head
     * @param employeeCode
     */
    private void updateBuGoalDeploy(BuGoalDeployHead head, String employeeCode )throws Exception{
	try{
	    Date date = new Date();
	    if (head.getHeadId() != null) {
		head.setLastUpdateDate(date);
		head.setLastUpdatedBy(employeeCode);

		if( OrderStatus.FINISH.equals( head.getStatus() ) && OrderStatus.FINISH.equals(head.getFlowStatus()) ){
		    head.setOriginalGoal(head.getActualGoal());
		}
		
		List<BuGoalDeployLine> lines = head.getBuGoalDeployLines();
		for (BuGoalDeployLine buGoalDeployLine : lines) {
		    buGoalDeployLine.setLastUpdateDate(date);
		    buGoalDeployLine.setLastUpdatedBy(employeeCode);

		    //	當簽核過去後, 將簽核目標複製到業績目標裡, 且流程也結束
		    if( OrderStatus.FINISH.equals( head.getStatus() ) && OrderStatus.FINISH.equals(head.getFlowStatus()) ){
			buGoalDeployLine.setGoalAmount(buGoalDeployLine.getSigningAmount());
		    }
		}

		buGoalDeployHeadDAO.update(head);
	    }
	}catch (Exception ex) {
	    log.error("業績目標設定主檔明細檔存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("業績目標設定主檔明細檔存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 檢核業績目標
     * @param parameterMap
     * @throws Exception
     */
    private void validateBuGoal(BuGoalHead head, String programId, String identification, List errorMsgs) {

	validateHead(head, programId, identification, errorMsgs); 
	validatePercentLine(head, programId, identification, errorMsgs);
	validateEmployeeLine(head, programId, identification, errorMsgs);
    }

    /**
     * 驗證主檔
     * @param parameterMap
     * @throws Exception
     */
    private void validateHead(BuGoalHead head, String programId, String identification, List errorMsgs) {
	String message = null;
	String tabName = "主檔資料";
	try {

	    String shopCode = head.getShopCode();
	    String department = head.getDepartment();

	    // 驗證專櫃代號
	    if(!StringUtils.hasText(shopCode)){
		message = "請輸入" + tabName + "的專櫃代號！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);	
	    }
	    // 驗證部門
	    if(!StringUtils.hasText(department)){
		message = "請輸入" + tabName + "的部門！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);	
	    }
	} catch (Exception e) {
	    message = "檢核業績目標基本檔的" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }

    /**
     * 檢核目標佔比
     * @param parameterMap
     * @throws Exception
     */
    private void validatePercentLine(BuGoalHead head, String programId, String identification, List errorMsgs){
	String message = null;
	String tabName = "目標佔比明細資料頁籤";
	try {
	    Map<String, Double> itemCategoryMap = new HashMap<String, Double>();  // 業種
	    Map<String, Double> itemSubCategoryMap = new HashMap<String, Double>();	// 業種子類
	    Map<String, Double> itemBrandMap = new HashMap<String, Double>();	// 商品品牌

	    Map<String, Double> workTypeMap = new HashMap<String, Double>();	// 班別(未含商品品牌)
	    Map<String, Double> workTypeHasItemBrandMap = new HashMap<String, Double>();	// 班別(含商品品牌)

	    List<BuGoalPercentLine> buGoalPercentLines = head.getBuGoalPercentLines();
	    int i = 0;
	    int size = buGoalPercentLines.size();
	    if( size > 0 ){
		for (BuGoalPercentLine buGoalPercentLine : buGoalPercentLines) {
		    String itemCategory = AjaxUtils.getPropertiesValue(buGoalPercentLine.getItemCategory(), "");
		    Double itemCategoryPercent = buGoalPercentLine.getItemCategoryPercent();
		    String itemSubcategory = AjaxUtils.getPropertiesValue(buGoalPercentLine.getItemSubcategory(),"");
		    Double itemSubcategoryPercent = buGoalPercentLine.getItemSubcategoryPercent();

		    String itemBrand = AjaxUtils.getPropertiesValue(buGoalPercentLine.getItemBrand(), "");
		    Double itemBrandPercent = buGoalPercentLine.getItemBrandPercent();

		    if(!StringUtils.hasText(itemBrand)){
			itemBrandPercent = 100d;
		    }

		    String workType = AjaxUtils.getPropertiesValue(buGoalPercentLine.getWorkType(),"");
		    Double workTypePercent = buGoalPercentLine.getWorkTypePercent();
		    ImItemCategory imItemCategory = null;

		    log.info("itemCategory = " + itemCategory );
		    log.info("itemSubcategory = " + itemSubcategory );
		    log.info("itemBrand = " + itemBrand );
		    log.info("workType = " + workType );

		    if( !StringUtils.hasText(itemCategory) ){
			message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的業種";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);	
		    }
//		    if( !StringUtils.hasText(itemSubcategory) ){
//			message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的業種子類";
//			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//			errorMsgs.add(message);
//			log.error(message);	
//		    }

		    // 若有值,檢核商品品牌是否存在,佔比
		    if( StringUtils.hasText(itemBrand) ){
			imItemCategory = imItemCategoryDAO.findByCategoryCode( head.getBrandCode(), ImItemCategoryDAO.ITEM_BRAND, itemBrand, "Y" );
			if(imItemCategory == null){
			    message = tabName + "中第" + (i + 1) + "項明細的 "+ itemBrand +" 商品品牌不存在";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);	
			}
		    }
//		    else{
//			message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的品牌";
//			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//			errorMsgs.add(message);
//			log.error(message);	
//		    }

		    if( !StringUtils.hasText(workType) ){
			message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的班別";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);	
		    }

		    if(itemCategoryPercent > 100 || itemCategoryPercent < 0){
			message = tabName + "中第" + (i + 1) + "項明細的業種佔比不能大於100或小於0";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);	
		    }

		    if(itemSubcategoryPercent > 100 || itemSubcategoryPercent < 0){
			message = tabName + "中第" + (i + 1) + "項明細的業種子類佔比不能大於100或小於0";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);	
		    }

		    if(itemBrandPercent > 100 || itemBrandPercent < 0){
			message = tabName + "中第" + (i + 1) + "項明細的商品品牌佔比不能大於100或小於0";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);	
		    }

		    if(workTypePercent > 100 || workTypePercent < 0){
			message = tabName + "中第" + (i + 1) + "項明細的班別佔比不能大於100或小於0";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);	
		    }

		    // 業種檢核 佔比相同(業種)
		    if( !itemCategoryMap.containsKey(itemCategory)){
			itemCategoryMap.put(itemCategory, itemCategoryPercent);
		    }else{
			log.info("itemCategoryMap.get(itemCategory) = " + itemCategoryMap.get(itemCategory) );
			// 佔比相同
			if(!itemCategoryMap.get(itemCategory).equals(itemCategoryPercent)){
			    message = tabName + "中第" + (i + 1) + "項明細有其相同業種:" + itemCategory + "則佔比必須相同";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);	
			}
		    }
		    // 業種子類檢核佔比相同(業種,業種子類)
		    if(!itemSubCategoryMap.containsKey(itemCategory+itemSubcategory)){
			itemSubCategoryMap.put(itemCategory+itemSubcategory, itemSubcategoryPercent);
		    }else{
			log.info("itemSubCategoryMap.get(itemCategory+itemSubcategory) = " + itemSubCategoryMap.get(itemCategory+itemSubcategory) );
			if(!itemSubCategoryMap.get(itemCategory+itemSubcategory).equals(itemSubcategoryPercent)){
			    message = tabName + "中第" + (i + 1) + "項明細有其相同業種:"+itemCategory+"、和業種子類:" + itemSubcategory + "則佔比必須相同";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);	
			}
		    }

		    // 不管是否商品品牌存在, 檢核佔比相同(業種,業種子類,商品品牌)
		    if( (StringUtils.hasText(itemBrand) && imItemCategory != null) ){ // || ( !StringUtils.hasText(itemBrand) && imItemCategory == null )
			if(!itemBrandMap.containsKey(itemCategory+itemSubcategory+itemBrand)){
			    itemBrandMap.put(itemCategory+itemSubcategory+itemBrand, itemBrandPercent);
			}else{
			    log.info("itemBrandMap.get(itemCategory+itemSubcategory+itemBrand) = " + itemBrandMap.get(itemCategory+itemSubcategory+itemBrand) );

			    if(!itemBrandMap.get(itemCategory+itemSubcategory+itemBrand).equals(itemBrandPercent)){
				message = tabName + "中第" + (i + 1) + "項明細有其相同業種:"+itemCategory+"、業種子類:" + itemSubcategory + "、商品品牌:" + itemBrand + "則佔比必須相同";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);	
			    }
			}
		    }

//		    if(StringUtils.hasText(itemBrand) && imItemCategory != null ){
//		    log.info(" is not null itemBrand = " + itemBrand);

//		    if(!itemBrandMap.containsKey(itemCategory+itemSubcategory+itemBrand)){
//		    itemBrandMap.put(itemCategory+itemSubcategory+itemBrand, itemBrandPercent);
//		    }else{
//		    log.info("itemBrandMap.get(itemCategory+itemSubcategory+itemBrand) = " + itemBrandMap.get(itemCategory+itemSubcategory+itemBrand) );

//		    if(!itemBrandMap.get(itemCategory+itemSubcategory+itemBrand).equals(itemBrandPercent)){
//		    message = tabName + "中第" + (i + 1) + "項明細有其相同業種:"+itemCategory+"、業種子類:" + itemSubcategory + "、商品品牌:" + itemBrand + "則佔比必須相同";
//		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//		    errorMsgs.add(message);
//		    log.error(message);	
//		    }
//		    }
//		    }else{
//		    log.info(" is null itemBrand = " + itemBrand);
//		    if(itemSubCategoryMap.containsKey(itemCategory+itemSubcategory) ){
//		    message = tabName + "中第" + (i + 1) + "項明細有相同業種:"+itemCategory+"、業種子類:" + itemSubcategory + " 則商品品牌必須輸入";
//		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//		    errorMsgs.add(message);
//		    log.error(message);	
//		    }else{
//		    if( !itemSubCategoryMap.containsKey(itemCategory+itemSubcategory) ){
//		    itemBrandMap.put(itemCategory+itemSubcategory, itemBrandPercent);
//		    }else{
//		    message = tabName + "中第" + (i + 1) + "項明細有相同業種:"+itemCategory+"、業種子類:" + itemSubcategory + " 則商品品牌必須輸入";
//		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//		    errorMsgs.add(message);
//		    log.error(message);	
//		    }
//		    }
//		    }

		    // 班別,不管是否存在商品品牌,檢核佔比相同(業種,業種子類,商品品牌,班別) workTypeHasItemBrandMap
		    if( (StringUtils.hasText(itemBrand) && imItemCategory != null) ){ // || ( !StringUtils.hasText(itemBrand) && imItemCategory == null )
			if(!workTypeHasItemBrandMap.containsKey(itemCategory+itemSubcategory+itemBrand+workType)){
			    workTypeHasItemBrandMap.put(itemCategory+itemSubcategory+itemBrand+workType, workTypePercent);
			}else{
			    log.info("workTypeHasItemBrandMap.get(itemCategory+itemSubcategory+itemBrand+workType) = " + workTypeHasItemBrandMap.get(itemCategory+itemSubcategory+itemBrand+workType) );

			    message = tabName + "中相同的業種:"+itemCategory+"、業種子類:"+itemSubcategory+"、商品品牌:"+ itemBrand +"、班別:" + workType + "已存在";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);	
			}
		    }
//		    if( StringUtils.hasText(itemBrand) && imItemCategory != null ){ // 表示商品品牌存在
//		    // 班別,不管是否存在商品品牌,檢核佔比相同(業種,業種子類,商品品牌,班別) workTypeHasItemBrandMap
//		    if(!workTypeHasItemBrandMap.containsKey(itemCategory+itemSubcategory+itemBrand+workType)){
//		    workTypeHasItemBrandMap.put(itemCategory+itemSubcategory+itemBrand+workType, workTypePercent);
//		    }else{
//		    log.info("workTypeHasItemBrandMap.get(itemCategory+itemSubcategory+itemBrand+workType) = " + workTypeHasItemBrandMap.get(itemCategory+itemSubcategory+itemBrand+workType) );

//		    message = tabName + "中相同的業種:"+itemCategory+"、業種子類:"+itemSubcategory+"、商品品牌:"+ itemBrand +"、班別:" + workType + "已存在";
//		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//		    errorMsgs.add(message);
//		    log.error(message);	
//		    }
//		    }else {
//		    if(!workTypeHasItemBrandMap.containsKey(itemCategory+itemSubcategory+workType)){
//		    workTypeHasItemBrandMap.put(itemCategory+itemSubcategory+workType, workTypePercent);
//		    }else{
//		    log.info("workTypeHasItemBrandMap.get(itemCategory+itemSubcategory+workType) = " + workTypeHasItemBrandMap.get(itemCategory+itemSubcategory+workType) );

//		    message = tabName + "中相同的業種:"+itemCategory+"、業種子類:"+itemSubcategory+"、班別:" + workType + "已存在";
//		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//		    errorMsgs.add(message);
//		    log.error(message);	
//		    }
//		    }
		    i++;
		}
		log.info( "======for 迴圈結束==========");



		Double category = 0D;
		Iterator itCategory = itemCategoryMap.keySet().iterator();
		while (itCategory.hasNext()) {	// 業種
		    String keyCategory = (String) itCategory.next();	// keyCategory = 業種
		    Double categoryPercent = itemCategoryMap.get( keyCategory );
		    log.info( " categoryPercent = " + categoryPercent);
		    category += categoryPercent;

		    Double subCategory = 0D;
		    Iterator itSubCategory = itemSubCategoryMap.keySet().iterator();
		    while (itSubCategory.hasNext()) {	// 業種子類
			String keySubCategory = (String) itSubCategory.next();	// keySubCategory = 業種 + 業種子類
			log.info( " keySubCategory = " + keySubCategory);
			if( keySubCategory.indexOf(keyCategory) > -1 ){
			    Double subCategoryPercent = itemSubCategoryMap.get( keySubCategory );
			    log.info( " subCategoryPercent = " + subCategoryPercent);
			    subCategory += subCategoryPercent;
			}
			//--------------------
			Double itemBrand = 0D;
			Iterator itItemBrand = itemBrandMap.keySet().iterator();
			while (itItemBrand.hasNext()) {	// 商品品牌
			    String keyItemBrand = (String) itItemBrand.next();	// keyItemBrand = 業種 + 業種子類 + 商品品牌
			    log.info( " keyItemBrand = " + keyItemBrand);
			    if( keyItemBrand.indexOf(keySubCategory) > -1  ){
				Double itemBrandPercent = itemBrandMap.get( keyItemBrand );
				log.info( " itemBrandPercent = " + itemBrandPercent);
				itemBrand += itemBrandPercent;
			    }

			    //--------------------
			    Double workType = 0D;
			    Iterator itWorkType = workTypeHasItemBrandMap.keySet().iterator();
			    while (itWorkType.hasNext()) { // 班別 (有商品品牌)
				String keyWorkType = (String) itWorkType.next();	// keyWorkType = 業種 + 業種子類 + 商品品牌 + 班別
				log.info( " keyWorkType = " + keyWorkType);
				if( keyWorkType.indexOf(keyItemBrand) > -1  ){
				    Double workTypePercent = workTypeHasItemBrandMap.get( keyWorkType );
				    log.info( " workTypePercent = " + workTypePercent);
				    workType += workTypePercent;
				}
			    }
			    log.info( "workType = " + workType);
			    if( workTypeHasItemBrandMap.size() > 0 && workType != 100D ){
				message = tabName + "中業種、業種子類、商品品牌組合為("+keyItemBrand+")的班別總共佔比"+workType+"不等於100";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);	
			    }
			    //--------------------
			}
			log.info( "itemBrand = " + itemBrand);
			if( itemBrandMap.size() > 0 && itemBrand != 100D ){
			    message = tabName + "中業種、業種子類組合為("+keySubCategory+")的商品品牌總共佔比"+itemBrand+"不等於100";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);	
			}
			//--------------------
		    }
		    log.info( "subCategory = " + subCategory);
		    if( subCategory != 100D ){
			message = tabName + "中業種為("+ keyCategory +")的業種子類總共佔比"+subCategory+"不等於100";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);	
		    }
		}
		log.info( "category = " + category);
		if( category != 100D ){
		    message = tabName + "的業種總共佔比"+category+"不等於100";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}

	    }else{
		message = tabName + "中請至少輸入一筆目標佔比明細！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	} catch (Exception e) {
	    message = "檢核業績目標基本檔的" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }

    /**
     * 檢核人員設定
     * @param parameterMap
     * @throws Exception
     */
    private void validateEmployeeLine(BuGoalHead head, String programId, String identification, List errorMsgs){
	Map<String, Double> keyMap = new HashMap<String, Double>(); // 佔比明細 key:業種+業種子類+商品品牌+班別, value:業種佔比*業種子類佔比*商品品牌佔比*班別佔比/1000000
	Map<String, Long> employeeKeyMap = new HashMap<String, Long>(); // 人員明細 key:業種+業種子類+商品品牌+班別, value:相同key的員工個數

	String message = null;
	String tabName = "人員設定明細資料頁籤";
	try {

	    List<BuGoalPercentLine> buGoalPercentLines = head.getBuGoalPercentLines();
	    for (BuGoalPercentLine buGoalPercentLine : buGoalPercentLines) {
		String itemBrand = AjaxUtils.getPropertiesValue(buGoalPercentLine.getItemBrand(),"");
		double itemBrandPercent = buGoalPercentLine.getItemBrandPercent();
		if(!StringUtils.hasText(itemBrand)){
		    itemBrandPercent = 100;
		}

		String key = buGoalPercentLine.getItemCategory() + buGoalPercentLine.getItemSubcategory() + itemBrand + buGoalPercentLine.getWorkType();

		keyMap.put( key, 
			(buGoalPercentLine.getItemCategoryPercent() * buGoalPercentLine.getItemSubcategoryPercent() * + itemBrandPercent * buGoalPercentLine.getWorkTypePercent() / 1000000 ) );

	    }

	    List<BuGoalEmployeeLine> buGoalEmployeeLines = head.getBuGoalEmployeeLines();
	    int j = 0;
	    int size = buGoalEmployeeLines.size();
	    if( size > 0 ){
		for (BuGoalEmployeeLine buGoalEmployeeLine : buGoalEmployeeLines) {
		    String employeeCode =  buGoalEmployeeLine.getEmployeeCode();
		    String itemCategory = buGoalEmployeeLine.getEmployeeItemCategory();
		    String itemSubcategory = buGoalEmployeeLine.getEmployeeItemSubcategory();
		    String itemBrand = AjaxUtils.getPropertiesValue(buGoalEmployeeLine.getEmployeeItemBrand(),"");

		    String workType = buGoalEmployeeLine.getEmployeeWorkType();

		    String employeeKey = itemCategory + itemSubcategory + itemBrand + workType;

		    if( !employeeKeyMap.containsKey( employeeKey )){
			employeeKeyMap.put( employeeKey, 1L);
		    }else{
			employeeKeyMap.put( employeeKey, employeeKeyMap.get(employeeKey) + 1L);
		    }

		    if( !StringUtils.hasText(itemCategory) ){
			message = "請輸入" + tabName + "中第" + (j + 1) + "項明細的業種";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		    }
//		    if( !StringUtils.hasText(itemSubcategory) ){
//			message = "請輸入" + tabName + "中第" + (j + 1) + "項明細的業種子類";
//			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//			errorMsgs.add(message);
//			log.error(message);
//		    }

		    // 若有值,檢核商品品牌是否存在,佔比
		    if( StringUtils.hasText(itemBrand) ){
			ImItemCategory imItemCategory = imItemCategoryDAO.findByCategoryCode( head.getBrandCode(), ImItemCategoryDAO.ITEM_BRAND, itemBrand, "Y" );
			if(imItemCategory == null){
			    message = tabName + "中第" + (j + 1) + "項明細的 "+ itemBrand +" 商品品牌不存在";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);	
			}
		    }

		    if( !StringUtils.hasText(employeeCode) ){
			message = "請輸入" + tabName + "中第" + (j + 1) + "項明細的工號";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		    }

		    BuEmployee buEmployee = (BuEmployee)buEmployeeDAO.findById(employeeCode);
		    if( buEmployee == null ){
			message = tabName + "中第" + (j + 1) + "項明細的工號不存在";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		    }else{
			// 確認人員明細設定的值是否存在佔比明細的設定
			if(!keyMap.containsKey( employeeKey )){
			    message = tabName + "中第" + (j + 1) + "項明細的業種,業種子類,商品品牌,班別在目標佔比不存在";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);
			}else{
			    log.info( "keyMap.get(employeeKey) = " + keyMap.get(employeeKey));
			    log.info( "employeeKeyMap.get(employeeKey) = " + employeeKeyMap.get(employeeKey));
			    buGoalEmployeeLine.setTotalPencent( keyMap.get(employeeKey) / employeeKeyMap.get(employeeKey) ); // 佔比結果/員工個數
			}
		    }
		    j++;
		}

		// 檢核是否目標設定都存在的人員設定
		Iterator it = keyMap.keySet().iterator();
		while (it.hasNext()) {
		    String key = (String) it.next();

		    if(!employeeKeyMap.containsKey(key)){
			message = tabName + "中業種,業種子類,班別組合為:"+ key +" 在人員設定不存在";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		    }
		}
	    }else{
		message = tabName + "中請至少輸入一筆人員設定明細！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	} catch (Exception e) {
	    message = "檢核業績目標基本檔的" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }

    /**
     * 檢核業績目標設定主檔,明細檔
     * @param Head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validateBuGoalDeploy(BuGoalDeployHead head, String programId, String identification, List errorMsgs, Object formLinkBean) throws ValidationErrorException, NoSuchObjectException {
	validteDeployHead(head, programId, identification, errorMsgs, formLinkBean);
	validteDeployLine(head, programId, identification, errorMsgs);
    }

    /**
     * 檢核業績目標設定主檔
     * @param head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validteDeployHead(BuGoalDeployHead head, String programId, String identification, List errorMsgs, Object formLinkBean) throws ValidationErrorException, NoSuchObjectException {
	String message = null;
	String tabName = "主檔資料";
	try {
	    String department	= head.getDepartment();
	    String shopCode		= head.getShopCode();
	    Long year			= head.getYear();
	    Long month			= head.getMonth();

	    if( !StringUtils.hasText(department) ){
		message = "請輸入" + tabName + "的部門！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);	
	    }
	    if( !StringUtils.hasText(shopCode) ){
		message = "請輸入" + tabName + "的專櫃代號！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);	
	    }
	    if( year == null ){
		message = "請輸入" + tabName + "的年！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);	
	    }
	    if( month == null ){
		message = "請輸入" + tabName + "的月！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);	
	    }

	} catch (Exception e) {
	    message = "檢核業績目標設定主檔單" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }


    /**
     * 檢核業績目標設定明細
     * @param head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @param formLinkBean
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validteDeployLine(BuGoalDeployHead head, String programId, String identification, List errorMsgs) throws ValidationErrorException, NoSuchObjectException {
	String message = null;
	String tabName = "明細檔資料";
	int i = 1;
	try {
	    List<BuGoalDeployLine> buGoalDeployLines = head.getBuGoalDeployLines();

	    for (BuGoalDeployLine buGoalDeployLine : buGoalDeployLines) {
		Double signingAmount = buGoalDeployLine.getSigningAmount();
		if( signingAmount == 0D || signingAmount < 0D ){
		    message = "請輸入" + tabName + "中第" + (i) + "項明細的簽核目標金額必須大於零！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);	
		}
		i++;
	    }

	} catch (Exception e) {
	    message = "檢核業績目標設定明細檔單" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }

    public void save (Object object){
    	baseDAO.save(object);
    }
    
    public void update (Object object){
    	baseDAO.update(object);
    }
    
    public BuGoal findBoGoalByKeyProperty(String brandCode, String goalCode, String year, String month ){
    	return (BuGoal)baseDAO.findFirstByProperty("BuGoal", 
				"and brandCode = ? and goalCode = ? and year = ? and month = ?", 
				new Object[]{ brandCode, goalCode, year, month } );
    }
    
    public BuGoalWork findBuGoalWorkByKeyProperty(String brandCode, String goalCode, String year, String month, String workType){
    	BuGoal buGoal = (BuGoal)baseDAO.findFirstByProperty("BuGoal", 
				"and brandCode = ? and goalCode = ? and year = ? and month = ?", 
				new Object[]{ brandCode, goalCode, year, month } );
    	if(null != buGoal){
    		return (BuGoalWork)baseDAO.findFirstByProperty("BuGoalWork", 
    				"and buGoal = ? and workType = ?", 
    				new Object[]{ buGoal, workType} );
    	}else{
    		return null;
    	}
    }

    public BuGoalTarget findBuGoalTargetByKeyProperty(String brandCode, String goalCode, String year, String month, 
    		String shopCode, String targetType, String targetValue){
    	BuGoal buGoal = (BuGoal)baseDAO.findFirstByProperty("BuGoal", 
				"and brandCode = ? and goalCode = ? and year = ? and month = ?", 
				new Object[]{ brandCode, goalCode, year, month } );
    	if(null != buGoal){
    		return (BuGoalTarget)baseDAO.findFirstByProperty("BuGoalTarget", 
    				"and buGoal = ? and shopCode = ? and targetType = ? and targetValue = ?", 
    				new Object[]{ buGoal, shopCode, targetType, targetValue} );
    	}else{
    		return null;
    	}
    }
    
    public BuGoalEmployee findBuGoalEmployeeByKeyProperty(String brandCode, String goalCode, String year, String month, 
    		String workType, String employeeCode){
    	BuGoal buGoal = (BuGoal)baseDAO.findFirstByProperty("BuGoal", 
				"and brandCode = ? and goalCode = ? and year = ? and month = ?", 
				new Object[]{ brandCode, goalCode, year, month } );
    	if(null != buGoal){
    		return (BuGoalEmployee)baseDAO.findFirstByProperty("BuGoalEmployee", 
    				"and buGoal = ? and workType = ? and employeeCode = ?", 
    				new Object[]{ buGoal, workType, employeeCode} );
    	}else{
    		return null;
    	}
    }
    
}