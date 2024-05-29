package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrandId;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiGroup;
import tw.com.tm.erp.hbm.bean.SiGroupId;
import tw.com.tm.erp.hbm.bean.SiGroupMenu;
import tw.com.tm.erp.hbm.bean.SiUsersGroup;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeBrandDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.SiGroupDAO;
import tw.com.tm.erp.hbm.dao.SiUsersGroupDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class BuEmployeeService {

    private static final Log log = LogFactory.getLog(BuEmployeeService.class);
    private SiGroupDAO siGroupDAO;
    private BuCommonPhraseService buCommonPhraseService;
    private SiUsersGroupDAO siUsersGroupDAO;
    private BuEmployeeBrandDAO buEmployeeBrandDAO;
    private BuEmployeeDAO buEmployeeDAO;
    private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
    // cache all common phrase data

  	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
  		"employeeCode", "chineseName","englishName"};
      
  	public static final int[] GRID_SEARCH_FIELD_TYPES = { 
  		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};
      
  	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
  		"", "",""};
  	
    public void setSiGroupDAO(SiGroupDAO siGroupDAO) {
    	this.siGroupDAO = siGroupDAO;
    }
	
  	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
  		this.buCommonPhraseService = buCommonPhraseService;
  	}
  	
  	public void setSiUsersGroupDAO(SiUsersGroupDAO siUsersGroupDAO) {
  		this.siUsersGroupDAO = siUsersGroupDAO;
  	}
  	
		public void setBuEmployeeBrandDAO(BuEmployeeBrandDAO buEmployeeBrandDAO) {
			this.buEmployeeBrandDAO = buEmployeeBrandDAO;
		}
		
		public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
			this.buEmployeeDAO = buEmployeeDAO;
		}

		public void setBuEmployeeWithAddressViewDAO(
				BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
			this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
		}

    public HashMap updateAJAXMovement(Map parameterMap) throws FormException, Exception {
  
    	MessageBox msgBox = new MessageBox();
    	HashMap resultMap = new HashMap(0);
      try{
  			//log.info("getting vatBeanFormBind");
      	Object formBindBean = parameterMap.get("vatBeanFormBind");
  			//log.info("getting vatBeanFormLink");
      	Object formLinkBean = parameterMap.get("vatBeanFormLink");
  			//log.info("getting vatBeanOther");
      	Object otherBean = parameterMap.get("vatBeanOther");
  
      	//取得欲更新的bean
      	BuEmployee PO = getActualMovement(formLinkBean);
      	if(PO == null){
          throw new Exception("查無此使用者");
      	}
      	System.out.println("======Start copyJSONBeantoPojoBean=========");
      	AjaxUtils.copyJSONBeantoPojoBean(formBindBean, PO);
      	System.out.println("======Finish copyJSONBeantoPojoBean=========");
      	String resultMsg = "";

      	//resultMsg = saveAjaxData(PO);
      	resultMap.put("resultMsg", resultMsg);
      	resultMap.put("entityBean", PO);
          
      }catch (FormException fe) {
         //log.error("使用者群組存檔失敗，原因：" + fe.toString());
         throw new FormException("員工存檔失敗，原因：" + fe.toString());
         
      }catch (Exception ex) {		  
          //log.error("使用者群組存檔時發生錯誤，原因：" + ex.toString());
          throw new Exception("員工存檔時發生錯誤，原因：" + ex.toString());
      }
    	resultMap.put("vatMessage" ,msgBox);
    	return resultMap;
    }
  
    private BuEmployee getActualMovement(Object bean) throws FormException, Exception{
    	String employeeCode = (String)PropertyUtils.getProperty(bean, "employeeCode");
    	log.info("employeeCode =" + employeeCode);
    	BuEmployee buEmployee  = buEmployeeDAO.findById(employeeCode);
      return buEmployee;
    }
  
    public String saveAjaxData(BuEmployee modifyObj) throws Exception {
      try{
  			log.info("Save 該 Save的資料");
  			buEmployeeDAO.update(modifyObj);
          	return "OK";
      } catch (Exception ex) {
      		log.error("使用者群組存檔時發生錯誤，原因：" + ex.toString());
      		throw new Exception("" + ex.getMessage());
      }
    }
    
    public String checkDuplicateEmployee(SiGroup modifyObj) throws Exception {
    	List<SiUsersGroup> SiUsersGroups = modifyObj.getsiUsersGroups();
    	HashMap hm = new HashMap();
			log.info("Check 該 Check的資料 , SiUsersGroups.size() = " + SiUsersGroups.size());
    	if(SiUsersGroups != null && SiUsersGroups.size()>0){
      	for (int i=SiUsersGroups.size()-1 ; i>=0 ; i--) {
  				SiUsersGroup siUsersGroup = SiUsersGroups.get(i);
    				log.info("siUsersGroup.getEmployeeCode() = " + siUsersGroup.getEmployeeCode());
      				if(AjaxUtils.IS_DELETE_RECORD_FALSE.equals(siUsersGroup.getIsDeleteRecord())){
      					if(hm.containsKey(siUsersGroup.getEmployeeCode())){
      						return "第"+hm.get(siUsersGroup.getEmployeeCode())+"筆資料與第"+siUsersGroup.getIndexNo()+"筆資料重複，請確認";
      					}else{
      						hm.put(siUsersGroup.getEmployeeCode(), siUsersGroup.getIndexNo());
      					}
      				}
      		}
  			}
    	return "OK";
    	}
    
    public String checkExistEmployee(SiGroup modifyObj) throws Exception {
    	List<SiUsersGroup> SiUsersGroups = modifyObj.getsiUsersGroups();
    	BuEmployeeBrandId buEmployeeBrandId;
    	BuEmployeeBrand buEmployeeBrand;
			log.info("Check 該 Check的資料 , SiUsersGroups.size() = " + SiUsersGroups.size());
    	if(SiUsersGroups != null && SiUsersGroups.size()>0){
      	for (int i=SiUsersGroups.size()-1 ; i>=0 ; i--) {
  				SiUsersGroup siUsersGroup = SiUsersGroups.get(i);
    				log.info("siUsersGroup.getEmployeeCode() = " + siUsersGroup.getEmployeeCode());
	    			buEmployeeBrandId = new BuEmployeeBrandId(siUsersGroup.getEmployeeCode() , siUsersGroup.getBrandCode());
	    			buEmployeeBrand = buEmployeeBrandDAO.findById(buEmployeeBrandId);
      				if(buEmployeeBrand == null && AjaxUtils.IS_DELETE_RECORD_FALSE.equals(siUsersGroup.getIsDeleteRecord())){
      						return "第"+siUsersGroup.getIndexNo()+"筆資料使用者工號不存在，請確認";
      				}
      		}
  			}
    	return "OK";
    	}
    
    public void removeIsDeleteRecordLine(SiGroup modifyObj) throws Exception {
    	List<SiUsersGroup> SiUsersGroups = modifyObj.getsiUsersGroups();
			log.info("Delete 該 Delete的資料 , SiUsersGroups.size() = " + SiUsersGroups.size());
    	if(SiUsersGroups != null && SiUsersGroups.size()>0){
      	for (int i=SiUsersGroups.size()-1 ; i>=0 ; i--) {
  				SiUsersGroup siUsersGroup = SiUsersGroups.get(i);
  				if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(siUsersGroup.getIsDeleteRecord())){
  					siUsersGroupDAO.delete(siUsersGroup);
  					SiUsersGroups.remove(siUsersGroup);
  				}
  			}
    	}
    }
    
    public void sortIndxeLine(SiGroup modifyObj) throws Exception {
    	List<SiUsersGroup> siUsersGroups = modifyObj.getsiUsersGroups();
			log.info("Sort 該 Sort的資料 , SiUsersGroups.size() = " + siUsersGroups.size());
    	if(siUsersGroups != null && siUsersGroups.size()>0){
      	for (int i=0 ; i<siUsersGroups.size() ;i++) {
  				SiUsersGroup siUsersGroup = siUsersGroups.get(i);
  				siUsersGroup.setIndexNo(i+1l);
  				siUsersGroup.setIsLockRecord(AjaxUtils.IS_LOCK_RECORD_FALSE);
  				siUsersGroupDAO.update(siUsersGroup);
  			}
    	}
    }

  	public List<Properties> executeInitial(Map parameterMap) throws Exception{
  		Map resultMap = new HashMap(0);
  		String dates[];
  		try{
  			Object otherBean = parameterMap.get("vatBeanOther");
  			String employeeCode = (String)PropertyUtils.getProperty(otherBean, "formId");
  			BuEmployee form = buEmployeeDAO.findById(employeeCode);
  			//log.info("employeeCode =" + employeeCode);
  			resultMap.put("employeeCode", employeeCode);
  			//log.info("remark2 =" + form.getRemark2());
  			resultMap.put("remark2", form.getRemark2());
  			//log.info("employeePosition =" + form.getEmployeePosition());
  			resultMap.put("employeePosition", form.getEmployeePosition());
  			
  			if(form.getArriveDate() != null){
    			dates = form.getArriveDate().toString().split("-");
    			resultMap.put("arriveDate", dates[0]+"/"+dates[1]+"/"+dates[2].substring(0,2));
  			}
  			if(form.getLeaveDate() != null){
  				dates = form.getLeaveDate().toString().split("-");
  				//log.info("leaveDate =" + form.getLeaveDate());
  				resultMap.put("leaveDate", dates[0]+"/"+dates[1]+"/"+dates[2].substring(0,2));
  			}
  			//log.info("remark1 =" + form.getRemark1());
  			resultMap.put("remark1", form.getRemark1());
  			//log.info("loginName =" + form.getLoginName());
  			resultMap.put("loginName", form.getLoginName());
  			//log.info("isDepartmentManager =" + form.getIsDepartmentManager());
  			resultMap.put("isDepartmentManager", form.getIsDepartmentManager());
  			//log.info("reportLoginName =" + form.getReportLoginName());
  			resultMap.put("reportLoginName", form.getReportLoginName());
  			//log.info("reportPassword =" + form.getReportPassword());
  			resultMap.put("reportPassword", form.getReportPassword());
  			//log.info("remark3 =" + form.getRemark3());
  			resultMap.put("remark3", form.getRemark3());
  			
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

  	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{

     	try{
     	    List<Properties> result = new ArrayList();
     	    List<Properties> gridDatas = new ArrayList();
     	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
     	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
     	    
     	    //======================帶入Head的值=========================
     	    String employeeDepartment 					= httpRequest.getProperty("employeeDepartment");
     	    String type 						= httpRequest.getProperty("type");
     	    String identityCode			 		= httpRequest.getProperty("identityCode");
     	    String chineseName 					= httpRequest.getProperty("chineseName");     	    
     	    String birthdayYear 				= httpRequest.getProperty("birthdayYear");
     	    String birthdayMonth 				= httpRequest.getProperty("birthdayMonth");     	    
     	    String birthdayDay 					= httpRequest.getProperty("birthdayDay");
     	    String gender 						= httpRequest.getProperty("gender");
     	    String englishName 					= httpRequest.getProperty("englishName");
     	    String employeeCode 				= httpRequest.getProperty("employeeCode");
     	    String employeePosition 			= httpRequest.getProperty("employeePosition");
     	    String isDepartmentManager 			= httpRequest.getProperty("isDepartmentManager");
     	    String brandCode 					= httpRequest.getProperty("brandCode");
     	    String arriveDateB 					= httpRequest.getProperty("arriveDateB");
     	    //log.info("arriveDateB = " + arriveDateB);
     	    String arriveDateE 					= httpRequest.getProperty("arriveDateE");
     	    //log.info("arriveDateE = " + arriveDateE);
     	    Date arriveDateBegin				= DateUtils.parseDate("yyyy/MM/dd", arriveDateB);
     	    Date arriveDateEnd					= DateUtils.parseDate("yyyy/MM/dd", arriveDateE);
     	    //log.info("arriveDateBegin = " + arriveDateBegin);
     	   	//log.info("arriveDateEnd = " + arriveDateEnd);
     	    //==============================================================	    
     	    
     	    HashMap findObjs = new HashMap();
     	    findObjs.put(" and employeeDepartment 		= :employeeDepartment",employeeDepartment);
     	    findObjs.put(" and type 		= :type",type);
     	    //log.info("identityCode =" + identityCode);
     	    findObjs.put(" and identityCode 		= :identityCode",identityCode);
     	    //log.info("chineseName =" + chineseName);
     	    if(StringUtils.hasText(chineseName))
     	    	findObjs.put(" and chineseName 			like :chineseName","%"+chineseName+"%");
     	   	//log.info("birthdayYear =" + birthdayYear);
     	  	findObjs.put(" and birthdayYear 		= :birthdayYear",birthdayYear);
     	  	//log.info("birthdayMonth =" + birthdayMonth);
     	  	findObjs.put(" and birthdayMonth 		= :birthdayMonth",birthdayMonth);
     	  	//log.info("birthdayDay =" + birthdayDay);
     	  	findObjs.put(" and birthdayDay 			= :birthdayDay",birthdayDay);
     	  	//log.info("gender =" + gender);
     	  	findObjs.put(" and gender 					= :gender",gender);
     	  	//log.info("englishName =" + englishName);
     	  	if(StringUtils.hasText(englishName))
     	  		findObjs.put(" and englishName 			like :englishName","%"+englishName+"%");
     	  	//log.info("employeeCode =" + employeeCode);
     	  	findObjs.put(" and employeeCode 		= :employeeCode",employeeCode);
     	  	//log.info("employeePosition =" + employeePosition);
     	  	if(StringUtils.hasText(employeePosition))
     	  		findObjs.put(" and employeePosition like :employeePosition","%"+employeePosition+"%");
     	  	//log.info("isDepartmentManager =" + isDepartmentManager);
     	  	findObjs.put(" and isDepartmentManager	= :isDepartmentManager",isDepartmentManager);
     	  	//log.info("brandCode =" + brandCode);
     	  	findObjs.put(" and brandCode				= :brandCode",brandCode);
     	  	//log.info("arriveDate =" + arriveDateBegin);
     	  	findObjs.put(" and arriveDate 			>= :arriveDateBegin",arriveDateBegin);
     	  	//log.info("arriveDate =" + arriveDateEnd);
     	  	findObjs.put(" and arriveDate 			<= :arriveDateEnd",arriveDateEnd);
     	  	
     	    Map buEmployeeWithAddressViewMap = buEmployeeWithAddressViewDAO.search("BuEmployeeWithAddressView", findObjs, iSPage, iPSize , BaseDAO.QUERY_SELECT_RANGE);
     	    List<BuEmployeeWithAddressView> buEmployeeWithAddressViews = (List<BuEmployeeWithAddressView>)buEmployeeWithAddressViewMap.get(BaseDAO.TABLE_LIST);
     	    HashMap map = new HashMap();
     	    if (buEmployeeWithAddressViews != null && buEmployeeWithAddressViews.size() > 0) {
     	    	// 取得第一筆的INDEX
     	    	Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
     	    	// 取得最後一筆 INDEX
     	    	Long maxIndex = (Long)buEmployeeWithAddressViewDAO.search("BuEmployeeWithAddressView as model", "count(model.employeeCode) as rowCount", findObjs, iSPage, iPSize , BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT);
     	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,buEmployeeWithAddressViews, gridDatas, firstIndex, maxIndex));
     	    } else {
     	    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
     	    }
     	    return result;
     	}catch(Exception ex){
     	    log.error("載入頁面顯示的調撥明細發生錯誤，原因：" + ex.toString());
     	    throw new Exception("載入頁面顯示的調撥明細失敗！");
     	}	
    }

    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
      String errorMsg = null;
      AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
  	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
  	public List<Properties> executeSearchInitial(Map parameterMap) throws Exception{
  		Map resultMap = new HashMap(0);
  		try{
  			Map multiList = new HashMap(0);
  			List<BuCommonPhraseLine> brandCodes = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "BrandCode" );
  			multiList.put( "brandCodes", AjaxUtils.produceSelectorData(brandCodes,"lineCode","name",true,true));
  			List<BuCommonPhraseLine> alldepartment = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "EmployeeDepartment" );
			multiList.put("alldepartment", AjaxUtils.produceSelectorData(alldepartment,"lineCode", "name", false, true));
        resultMap.put("multiList",multiList);
    			
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
