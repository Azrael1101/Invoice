package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List; //import tw.com.tm.erp.constants.MessageStatus;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
//import tw.com.tm.erp.hbm.SpringUtils;
//import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiFunction;
import tw.com.tm.erp.hbm.bean.SiFunctionObject;
import tw.com.tm.erp.hbm.bean.SiFunctionObjectId;
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.SiFunctionDAO;
import tw.com.tm.erp.hbm.dao.SiFunctionObjectDAO;
import tw.com.tm.erp.hbm.dao.SiMenuDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.UserUtils;

public class SiFunctionService {
	
	private static final Log log = LogFactory.getLog(SiFunctionService.class);
	
	private SiFunctionDAO siFunctionDAO;
	private SiFunctionObjectDAO siFunctionObjectDAO;

	private BuCommonPhraseHeadDAO buCommonPhraseHeadDAO;

	private BuCommonPhraseService buCommonPhraseService;
	
	private SiFunctionObjectService siFunctionObjectService;
	
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"functionCode", "functionName", "functionCode" };
	
	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
  	"", "", "" };
	
	public void setSiFunctionDAO(SiFunctionDAO siFunctionDAO) {
		this.siFunctionDAO = siFunctionDAO;
	}
	
	public void setSiFunctionObjectDAO(SiFunctionObjectDAO siFunctionObjectDAO) {
		this.siFunctionObjectDAO = siFunctionObjectDAO;
	}
	
	public void setBuCommonPhraseHeadDAO(BuCommonPhraseHeadDAO buCommonPhraseHeadDAO) {
		this.buCommonPhraseHeadDAO = buCommonPhraseHeadDAO;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}
	
	public void setSiFunctionObjectService(
			SiFunctionObjectService siFunctionObjectService) {
		this.siFunctionObjectService = siFunctionObjectService;
	}
	
	public List<Properties> executeInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
	
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginEmployeeCodeString = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
			String formId = formIdString;
			List<SiFunction> siFunction = formId != null ? siFunctionDAO.findByProperty( "SiFunction", "functionCode", formId) : null;
			Map multiList = new HashMap(0);
			String dates[] ;
			List<BuCommonPhraseLine> allObjectTypes = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "SiFunctionObjectObjectType" );
			//log.info( "allObjectTypes size:" + allObjectTypes.size() );			
			multiList.put( "allObjectTypes", AjaxUtils.produceSelectorData(allObjectTypes ,"name" ,"name"         ,  false,  false) );
			
			if( siFunction != null && siFunction.size() > 0 ){
				SiFunction sf = siFunction.get(0);
				resultMap.put("functionCode",sf.getFunctionCode());
				resultMap.put("functionName",sf.getFunctionName());
	      String employeeName = UserUtils.getUsernameByEmployeeCode(sf.getCreatedBy());
	      resultMap.put("createdBy",sf.getCreatedBy());
	      resultMap.put("createdByName",employeeName);
	      resultMap.put("creationDate","");
	      if(sf.getCreationDate() != null){
	      	dates = sf.getCreationDate().toString().split("-");
	      	resultMap.put("creationDate",dates[0]+"/"+dates[1]+"/"+dates[2].substring(0,2));
				}
			}else{
				resultMap.put("functionCode","");
				resultMap.put("functionName","");
  			String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCodeString);
        resultMap.put("createdBy",loginEmployeeCodeString);
        resultMap.put("createdByName",employeeName);
        resultMap.put("creationDate", new Date() );
			}

			resultMap.put("multiList",multiList);
			log.info( "結束" );				
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

	public List<Properties> getAJAXExistFunctionName(Properties httpRequest)throws FormException{
		
		List re = new ArrayList();
		String functionCode = httpRequest.getProperty("functionCode");
		log.info("functionCode=" + functionCode );
		
		try{
			Properties pro = new Properties();
			
			List<SiFunction> list = siFunctionDAO.findByProperty( "SiFunction", "functionCode", functionCode);
			
			pro.setProperty("functionName", list.size() > 0 ? list.get(0).getFunctionName() : "" );
			
			re.add(pro);
		}catch(Exception e){
			e.printStackTrace();
		}
		return re;
	}
	
	
public HashMap updateAJAXMenu(Map parameterMap) throws FormException, Exception {
    
	log.info("==========updateAJAXMinu() start==========");

		MessageBox msgBox = new MessageBox();
    HashMap resultMap = new HashMap(0);
    try{
    	Object formBindBean = parameterMap.get("vatBeanFormBind");
    	Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	Object otherBean = parameterMap.get("vatBeanOther");
  
    	//取得欲更新的bean
    	SiFunction siFunctionPO = getActualSiFunction(formBindBean);
    	log.info("======Start copyJSONBeantoPojoBean=========");
    	AjaxUtils.copyJSONBeantoPojoBean(formBindBean, siFunctionPO);
    	log.info("======Finish copyJSONBeantoPojoBean=========");
    	
    	String resultMsg = this.saveAjaxData(siFunctionPO, formBindBean, otherBean );
    	log.info("resultMsg : " + resultMsg );
    	
    	resultMap.put("getFunctionCode", siFunctionPO.getFunctionCode());
    	resultMap.put("resultMsg", resultMsg);
     	resultMap.put("entityBean", siFunctionPO);
     	
    }catch (FormException fe) {
       log.error("系統功能單存檔失敗，原因：" + fe.toString());
       throw new FormException("系統功能單單存檔失敗，原因：" + fe.toString());
       
    }catch (Exception ex) {		  
      log.error("系統功能單存檔時發生錯誤，原因：" + ex.toString());
      throw new Exception("系統功能單存檔失敗，原因：" + ex.toString());
    }	
 
    resultMap.put("vatMessage" ,msgBox);
    log.info("==========updateAJAXMinu() end==========");
    return resultMap;
  }
	
	private SiFunction getActualSiFunction(Object bean ) throws FormException, Exception{
  	String id = (String)PropertyUtils.getProperty(bean, "functionCode");
  	log.info("functionCode = "+id);
  	
  	List<SiFunction> siFunction = siFunctionDAO.findByProperty( "SiFunction", "functionCode", id);
  	
  	return ( siFunction != null && siFunction.size() > 0 )? siFunction.get(0) : new SiFunction();
  }
	
	public String saveAjaxData(SiFunction modifyObj, Object formBindBean, Object otherBean ) throws Exception {
		try {
			if (null != modifyObj) {
				String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
				String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
				String functionCode = (String)PropertyUtils.getProperty(formBindBean, "functionCode");
				String msg = functionCode + "存檔成功";
				log.info("formAction = "+ formAction);
				
				log.info("OrderStatus.FORM_SUBMIT = "+OrderStatus.FORM_SUBMIT);
				log.info("formAction compare result = "+OrderStatus.FORM_SUBMIT.equals(formAction));
				
				if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
					
					modifyObj.setFunctionCode( functionCode );
					
					modifyObj.setCreatedBy( employeeCode );
					modifyObj.setCreationDate(new Date());
					modifyObj.setUpdatedBy( employeeCode );
					modifyObj.setUpdateDate(new Date());
					
					if(siFunctionDAO.findByProperty( "SiFunction", "functionCode", functionCode).size() > 0  ){
						siFunctionDAO.update(modifyObj);
						msg = functionCode + "更新成功";
						
						//更新line(delete)
						if ( !AjaxUtils.CHECK_OK.equals(siFunctionObjectService.updateObjectData(formBindBean) ) ) {
							msg = siFunctionObjectService.updateObjectData(formBindBean);
						}
						
					} else {
						siFunctionDAO.save(modifyObj);
					}	
					log.info("save siFunction success");
					
				}
				
				return msg;
			} else {
				throw new FormException("");
			}
		} catch (FormException fe) {
			log.error("系統功能單存檔時發生錯誤，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("系統功能單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("系統功能單存檔時發生錯誤，原因：" + ex.getMessage());
		}

	}

  public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
      String errorMsg = null;
  	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
  	return AjaxUtils.getResponseMsg(errorMsg);
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
  	    String functionName = httpRequest.getProperty("functionName");
  	   
  	    HashMap map = new HashMap();
  	    HashMap findObjs = new HashMap();
  	    findObjs.put("and model.functionCode like :functionCode",functionCode);
  	    findObjs.put("and model.functionName like :functionName",functionName);

  	    log.info("functionCode:"+functionCode);
  	    log.info("functionName:"+functionName);
  	    //==============================================================	    
  	    
  	    Map siFunctionMap = siFunctionDAO.search( "SiFunction as model", findObjs, iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
  	    List<SiFunction> siFunctions = (List<SiFunction>) siFunctionMap.get(BaseDAO.TABLE_LIST); 

  	    log.info("SiFunction.size"+ siFunctions.size());	
  	    if (siFunctions != null && siFunctions.size() > 0) {
  	    	Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
  	    	Long maxIndex = (Long)siFunctionDAO.search("SiFunction as model", "count(model.functionCode) as rowCount" ,findObjs, iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
  	    	
  	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,siFunctions, gridDatas, firstIndex, maxIndex));
  	    }else {
  	    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
  	    }

  	    return result;
  	}catch(Exception ex){
  	    log.error("載入頁面顯示的系統功能查詢發生錯誤，原因：" + ex.toString());
  	    throw new Exception("載入頁面顯示的系統功能查詢失敗！");
  	}	
  }
	
	public String getTest( String tmp ){
		return tmp + " dwr測試成功";
	}
	
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
				pickerResult.put("result", result);
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

}