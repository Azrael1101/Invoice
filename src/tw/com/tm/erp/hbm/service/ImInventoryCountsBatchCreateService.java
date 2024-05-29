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
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsHead;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.ImInventoryCountsHeadDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.DateUtils;

public class ImInventoryCountsBatchCreateService {
	
	private static final Log log = LogFactory.getLog(ImInventoryCountsBatchCreateService.class);
	private ImInventoryCountsHeadDAO imInventoryCountsHeadDAO;
	
	private BuCommonPhraseService buCommonPhraseService;
	
	private BuOrderTypeService buOrderTypeService;
	
	private BuBrandDAO buBrandDAO;

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}
	
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}
	
    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
    	this.buBrandDAO = buBrandDAO;
    }	
	
	public static final Object[][] MASTER_DEFINITION =  
  {{"NAME","TYPE","VALUE","STYLE","VALUE"},
     {"menuId"      		,AjaxUtils.FIELD_TYPE_LONG  ,"0", "mode:HIDDEN", ""},
    	{"lineNo"   			,AjaxUtils.FIELD_TYPE_LONG,"", "", ""},
    	{"systemType"     ,AjaxUtils.FIELD_TYPE_STRING,"", "", ""},
    	{"type"   				,AjaxUtils.FIELD_TYPE_STRING,"", "", ""},
    	{"name"        		,AjaxUtils.FIELD_TYPE_STRING,"", "", ""},
    	{"functionCode"		,AjaxUtils.FIELD_TYPE_STRING,"", "", ""},
    	{"url"						,AjaxUtils.FIELD_TYPE_STRING,"", "", ""},
    	{"parentMenuId"		,AjaxUtils.FIELD_TYPE_LONG	,"0", "", ""},
    	
    	{"reserve1"				,AjaxUtils.FIELD_TYPE_STRING,"", "", ""},
    	{"reserve2"				,AjaxUtils.FIELD_TYPE_STRING,"", "", ""},
    	{"reserve3"				,AjaxUtils.FIELD_TYPE_STRING,"", "", ""},
    	{"reserve4"				,AjaxUtils.FIELD_TYPE_STRING,"", "", ""},
    	{"reserve5"				,AjaxUtils.FIELD_TYPE_STRING,"", "", ""},
    	
    	{"createdBy"      ,AjaxUtils.FIELD_TYPE_STRING,"", "", ""},
    	{"creationDate"   ,AjaxUtils.FIELD_TYPE_DATE  ,"", "", ""},
    	{"lastUpdatedBy"  ,AjaxUtils.FIELD_TYPE_STRING,"", "", ""},
    	{"lastUpdateDate" ,AjaxUtils.FIELD_TYPE_DATE  ,"", "", ""}};
	
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"systemType", "functionType", "type",
		"functionCode", "menuId" };
	
	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
  	"", "", "", 
  	"", "0"};
	
  public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception{

  	Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try{
			log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			log.info("getSearchSelection.picker_parameter:" + timeScope +"/"+ searchKeys.toString());
			
			List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
			log.info("getSearchSelection.result:" + result.size());
			if(result.size() > 0 )
				pickerResult.put("si_Menu_List", result);
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
	
	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{

  	try{
  	    List<Properties> result = new ArrayList();
  	    List<Properties> gridDatas = new ArrayList();
  	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
  	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
  	  
  	    //======================帶入Head的值=========================
  	    
  	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
  	    String functionCode = httpRequest.getProperty("functionCode");
  	    String systemType = httpRequest.getProperty("systemType");
  	    String functionType = httpRequest.getProperty("functionType");
  	    String type = httpRequest.getProperty("type");
  	    String name = httpRequest.getProperty("name");
  	    
  	    HashMap map = new HashMap();
  	    HashMap findObjs = new HashMap();
  	    findObjs.put(" and functionCode = :functionCode", functionCode);
  	    findObjs.put(" and name = :name",name);
  	    findObjs.put(" and systemType = :systemType",systemType);
  	    findObjs.put(" and functionType = :functionType",functionType);
  	    findObjs.put(" and type = :type",type);

  	    //==============================================================	    
  	    
  	    Map imInventoryCountsHeadMap = imInventoryCountsHeadDAO.search( "ImInventoryCountsHead", findObjs, iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
  	    List<ImInventoryCountsHead> imInventoryCountsHeads = (List<ImInventoryCountsHead>) imInventoryCountsHeadMap.get(BaseDAO.TABLE_LIST); 

  	    log.info("siMenus.size"+ imInventoryCountsHeads.size());	
  	    if (imInventoryCountsHeads != null && imInventoryCountsHeads.size() > 0) {
  	    	Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
  	    	Long maxIndex = (Long)imInventoryCountsHeadDAO.search("ImInventoryCountsHead", "count(systemType) as rowCount" ,findObjs, iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
  	    	
  	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,imInventoryCountsHeads, gridDatas, firstIndex, maxIndex));
  	    }else {
  	    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
  	    }

  	    return result;
  	}catch(Exception ex){
  	    log.error("載入頁面顯示的選單查詢發生錯誤，原因：" + ex.toString());
  	    throw new Exception("載入頁面顯示的選單功能查詢失敗！");
  	}	
  }
	
	
	public void setImInventoryCountsHeadDAO(ImInventoryCountsHeadDAO imInventoryCountsHeadDAO) {
		this.imInventoryCountsHeadDAO = imInventoryCountsHeadDAO;
	}
	
	/*
	public boolean isRepeat( String propertyName,  String value ){
		return imInventoryCountsHeadDAO.isRepeat( propertyName, value );
	}
	*/
	
	public List<Properties> executeInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
	
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			
			String loginEmployeeCodeString = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String loginBrandCodeString = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
			
			
			String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
			Long formId = StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
			log.info("196: formId="+formId);
			
			List<ImInventoryCountsHead> imInventoryCountsHead = formId != null ? imInventoryCountsHeadDAO.findByProperty( "ImInventoryCountsHead", "headId", formId) : null; 
			
			Map multiList = new HashMap(0);			
			
			log.info("loginBrandCodeString="+loginBrandCodeString);			
			List<BuOrderType> allOrderTypeCodes = buOrderTypeService.findOrderbyType(loginBrandCodeString,"ICF");	
			//List<BuCommonPhraseLine> allOrderTypeCodes = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "OrderTypeCode" );
			log.info( "allOrderTypeCodes size:" + (allOrderTypeCodes==null? "null" : allOrderTypeCodes.size()) );
			
			String dates[] ;
			if( imInventoryCountsHead != null && imInventoryCountsHead.size() > 0 ){
				ImInventoryCountsHead iich = imInventoryCountsHead.get(0);
				
				resultMap.put( "headId", iich.getHeadId() );
				resultMap.put("orderNo", iich.getOrderNo());
				resultMap.put("orderTypeCode", iich.getOrderTypeCode());				
				resultMap.put( "brandCode", iich.getBrandCode() );
				if(iich.getCountsDate() != null){
					dates = iich.getCountsDate().toString().split("-");
					resultMap.put("countsDate",dates[0]+"/"+dates[1]+"/"+dates[2].substring(0,2));
					resultMap.put("creationDate", DateUtils.format(iich.getCreationDate()));
				}
				log.info("228: resultMap: countsDate = "+resultMap.get("countsDate").toString());
				resultMap.put("countsId",iich.getCountsId());
				resultMap.put("countsLotNo",iich.getCountsLotNo());				
				resultMap.put("superintendentCode", iich.getSuperintendentCode());
				resultMap.put("superintendentName", iich.getSuperintendentName());
				resultMap.put( "actualSuperintendentCode", iich.getActualSuperintendentCode() );
				resultMap.put( "actualSuperintendentName", iich.getActualSuperintendentName() );				
				resultMap.put("warehouseCode", iich.getWarehouseCode());
				resultMap.put("warehouseName", iich.getWarehouseName());
				resultMap.put("description", iich.getDescription());
				String employeeName = UserUtils.getUsernameByEmployeeCode(iich.getCreatedBy());
				resultMap.put("createdBy",iich.getCreatedBy());
				resultMap.put("createdByName",employeeName);
				resultMap.put("creationDate",iich.getCreationDate());
			} else {
				resultMap.put("orderNo", "");
				resultMap.put("orderTypeCode", orderTypeCode);
				resultMap.put("brandCode", loginBrandCodeString);
				
				log.info("256: DateUtils.format(new Date())="+DateUtils.format(new Date()));
				resultMap.put("countsDate", DateUtils.parseDate(DateUtils.format(new Date())));
				
				resultMap.put("countsId","");
				resultMap.put("countsLotNo","");
				resultMap.put("superintendentCode", "");
				resultMap.put("superintendentName", "");
				resultMap.put( "actualSuperintendentCode", "" );
				resultMap.put( "actualSuperintendentName", "" );
				resultMap.put("warehouseCode", "");
				resultMap.put("warehouseName", "");
				resultMap.put("description", "");
				String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCodeString);
				resultMap.put("createdBy",loginEmployeeCodeString);
				resultMap.put("createdByName",employeeName);			
			}		
			
			log.info("orderTypeCode="+orderTypeCode);
			log.info("allOrderTypeCodes.toString()="+allOrderTypeCodes.toString());
			multiList.put( "allOrderTypeCodes", AjaxUtils.produceSelectorData(allOrderTypeCodes ,"orderTypeCode" ,"name",  true,  true, orderTypeCode));
			resultMap.put("multiList",multiList);
			log.info("248: multiList: allOrderTypeCodes ==> "+multiList.get("allOrderTypeCodes").toString());
			log.info("248: resultMap: multiList ==> "+resultMap.get("multiList").toString());			
			
			resultMap.put("brandName", buBrandDAO.findById(loginBrandCodeString).getBrandName());
			
			//log.info( "結束" );				
		}catch(Exception ex){
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type"   , "ALERT");
			messageMap.put("message", "表單初始化失敗，原因："+ex.toString());
			messageMap.put("event1" , null);
			messageMap.put("event2" , null);
			resultMap.put("vatMessage",messageMap);
		}
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	
	public List<Properties> executeSearchInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
	
		try{
			//Object otherBean = parameterMap.get("vatBeanOther");
			Map multiList = new HashMap(0);
			List<BuCommonPhraseLine> OrderTypeCode = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "OrderTypeCode" );
			//log.info( "OrderTypeCode size:" + OrderTypeCode.size() );	
			multiList.put( "OrderTypeCode", AjaxUtils.produceSelectorData(OrderTypeCode ,"lineCode" ,"name"         ,  true,  true));
			resultMap.put("multiList",multiList);
			//log.info( "結束" );				
		}catch(Exception ex){
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type"   , "ALERT");
			messageMap.put("message", "表單初始化失敗，原因："+ex.toString());
			messageMap.put("event1" , null);
			messageMap.put("event2" , null);
			resultMap.put("vatMessage",messageMap);
		}
		return AjaxUtils.parseReturnDataToJSON(resultMap);
		
	}
	
	public HashMap updateAJAXMenu(Map parameterMap) throws FormException, Exception {

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
    try{
    	Object formBindBean = parameterMap.get("vatBeanFormBind");
    	Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	Object otherBean = parameterMap.get("vatBeanOther");
  
    	String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    	String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
    	
    	//取得欲更新or新建bean
    	ImInventoryCountsHead imInventoryCountsPO = getActualMovement(formLinkBean);
    	log.info("======Start copyJSONBeantoPojoBean=========");
    	AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imInventoryCountsPO);
    	log.info("======Finish copyJSONBeantoPojoBean=========");
    	
    	String resultMsg = this.saveAjaxData(imInventoryCountsPO, formAction, employeeCode );
    	log.info("resultMsg : " + resultMsg );
    	
    	resultMap.put("headId", imInventoryCountsPO.getHeadId());
    	resultMap.put("resultMsg", resultMsg);
     	resultMap.put("entityBean", imInventoryCountsPO);
        
    }catch (FormException fe) {
       log.error("盤點單批次建立失敗，原因：" + fe.toString());
       throw new FormException("盤點單批次建立失敗，原因：" + fe.toString());
       
    }catch (Exception ex) {		  
      log.error("盤點單批次建立時發生錯誤，原因：" + ex.toString());
      throw new Exception("盤點單批次建立失敗，原因：" + ex.toString());
    }	
 
    resultMap.put("vatMessage" ,msgBox);
    log.info(" updateAJAXMinu 結束");
    return resultMap;
  }

	
	private ImInventoryCountsHead getActualMovement(Object bean) throws FormException, Exception{
  	Long menuId;
  	ImInventoryCountsHead imInventoryCountsHead = null;
  	if(StringUtils.hasText((String)PropertyUtils.getProperty(bean, "menuId"))){
  		menuId= Long.valueOf((String)PropertyUtils.getProperty(bean, "menuId"));
  		imInventoryCountsHead = (ImInventoryCountsHead)imInventoryCountsHeadDAO.findById("SiMenu", menuId);
  	}
		if(imInventoryCountsHead == null){
			imInventoryCountsHead = new ImInventoryCountsHead();
		}
    return imInventoryCountsHead;
  }
	

	public String saveAjaxData(ImInventoryCountsHead modifyObj,String formAction, String employeeCode ) throws Exception {
		try {
			if (null != modifyObj) {
				if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
					modifyObj.setCreatedBy( employeeCode );
					modifyObj.setCreationDate(new Date());
					imInventoryCountsHeadDAO.saveOrUpdate(modifyObj);
				}
				
				return modifyObj.getHeadId() + "成功";
			} else {
				throw new FormException("");
			}
		} catch (FormException fe) {
			log.error("盤點單批次建立時發生錯誤，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("盤點單批次建立時發生錯誤，原因：" + ex.toString());
			throw new Exception("盤點單批次建立時發生錯誤，原因：" + ex.getMessage());
		}

	}

	
	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
    String errorMsg = null;
    AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
	return AjaxUtils.getResponseMsg(errorMsg);
}
	
}