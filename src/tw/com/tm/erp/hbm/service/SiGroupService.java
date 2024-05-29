package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
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
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrandId;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiGroup;
import tw.com.tm.erp.hbm.bean.SiGroupId;
import tw.com.tm.erp.hbm.bean.SiUsersGroup;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeBrandDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.SiGroupDAO;
import tw.com.tm.erp.hbm.dao.SiGroupMenuDAO;
import tw.com.tm.erp.hbm.dao.SiUsersGroupDAO;
import tw.com.tm.erp.hbm.dao.TmpAjaxSearchDataDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.UserUtils;

public class SiGroupService {

    private static final Log log = LogFactory.getLog(SiGroupService.class);
    private SiGroupDAO siGroupDAO;
    private BuCommonPhraseService buCommonPhraseService;
    private SiUsersGroupDAO siUsersGroupDAO;
    private BuEmployeeBrandDAO buEmployeeBrandDAO;
    private SiGroupMenuDAO siGroupMenuDAO;
    private NativeQueryDAO nativeQueryDAO;
    private TmpAjaxSearchDataDAO tmpAjaxSearchDataDAO;

  	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
  		"brandCode", "groupCode", "groupName", "enable"};
      
  	public static final int[] GRID_SEARCH_FIELD_TYPES = { 
  		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};
      
  	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
  		"", "", "", ""};
  	
    // cache all common phrase data

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
	
    public void setSiGroupMenuDAO(SiGroupMenuDAO siGroupMenuDAO) {
		this.siGroupMenuDAO = siGroupMenuDAO;
	}
    
	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}
	
	public void setTmpAjaxSearchDataDAO(TmpAjaxSearchDataDAO tmpAjaxSearchDataDAO) {
		this.tmpAjaxSearchDataDAO = tmpAjaxSearchDataDAO;
	}

	public HashMap updateAJAXMovement(Map parameterMap) throws FormException, Exception {
		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			//取得欲更新的bean
			SiGroup po = getActualMovement(formBindBean);
  			log.info("撈出來的的資料 , PO.SiUsersGroups.size() = " + po.getsiUsersGroups().size());
  			//AjaxUtils.copyJSONBeantoPojoBean(formBindBean, po);
  			String brandCode = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
  	    	String groupCode = (String)PropertyUtils.getProperty(formBindBean, "groupCode");
  	    	String groupName = (String)PropertyUtils.getProperty(formBindBean, "groupName");
  	    	po.setId(new SiGroupId(brandCode, groupCode));
  	    	po.setGroupName(groupName);
	      	po.setEnable("Y");
	      	po.setUpdatedBy(po.getCreatedBy());
	      	Date now = new Date();
	      	po.setCreationDate(now);
	      	po.setUpdateDate(now);
	      	String resultMsg = saveAjaxData(po);
	      	
	      	//更新SI_GROUP_MENU && SI_USERS_GROUP
	      	String pkString = (String)PropertyUtils.getProperty(formBindBean, "pkString");
	    	String oldBrandCode = pkString.split(",")[0];
	    	String oldGroupCode = pkString.split(",")[1];
	      	nativeQueryDAO.executeNativeUpdateSql("update ERP.SI_GROUP_MENU set GROUP_CODE='" + groupCode + "', BRAND_CODE='" + brandCode + "', UPDATED_BY='" + po.getCreatedBy() + "', UPDATE_DATE=sysdate where BRAND_CODE='" + oldBrandCode + "' and GROUP_CODE='" + oldGroupCode + "'");
	      	nativeQueryDAO.executeNativeUpdateSql("update ERP.SI_USERS_GROUP set GROUP_CODE='" + groupCode + "', BRAND_CODE='" + brandCode + "', UPDATED_BY='" + po.getCreatedBy() + "', UDPATE_DATE=sysdate where BRAND_CODE='" + oldBrandCode + "' and GROUP_CODE='" + oldGroupCode + "'");
	      	
      		resultMap.put("resultMsg", resultMsg);
      		resultMap.put("entityBean", po);
      }
	  catch(FormException fe) {
         log.error("使用者群組存檔失敗，原因：" + fe.toString());
         throw new FormException("使用者群組存檔失敗，原因：" + fe.toString());
         
      }
	  catch(Exception ex) {	  
          log.error("使用者群組存檔時發生錯誤，原因：" + ex.toString());
          throw new Exception("使用者群組存檔時發生錯誤，原因：" + ex.toString());
      }
	  resultMap.put("vatMessage" ,msgBox);
	  return resultMap;
    }
  
    private SiGroup getActualMovement(Object bean) throws FormException, Exception {
    	String pkString = (String)PropertyUtils.getProperty(bean, "pkString");
    	String brandCode = pkString.split(",")[0];
    	String groupCode = pkString.split(",")[1];
 	    String sql = "SELECT * FROM ERP.SI_GROUP WHERE BRAND_CODE='" + brandCode + "' AND GROUP_CODE='" + groupCode + "' ";
 	    List datas = nativeQueryDAO.executeNativeSql(sql);
 	    if(datas != null && datas.size() > 0){
	   		Object[] obj = (Object[])datas.get(0);
	   		SiGroup siGroup = new SiGroup();
	   		siGroup.setId(new SiGroupId(obj[0] == null ? "" : obj[0].toString(), obj[1] == null ? "" : obj[1].toString()));
	   		siGroup.setGroupName(obj[2] == null ? "" : obj[2].toString());
	   		siGroup.setDescription(obj[3] == null ? "" : obj[3].toString());
	   		siGroup.setEnable(obj[4] == null ? "" : obj[4].toString());
	   		siGroup.setReserve1(obj[5] == null ? "" : obj[5].toString());
	   		siGroup.setReserve2(obj[6] == null ? "" : obj[6].toString());
	   		siGroup.setReserve3(obj[7] == null ? "" : obj[7].toString());
	   		siGroup.setReserve4(obj[8] == null ? "" : obj[8].toString());
	   		siGroup.setReserve5(obj[9] == null ? "" : obj[9].toString());
	   		siGroup.setCreatedBy(obj[10] == null ? "" : obj[10].toString());
	   		siGroup.setCreationDate(obj[11] == null ? null : (Date)obj[11]);
	   		siGroup.setUpdatedBy(obj[12] == null ? "" : obj[12].toString());
	   		siGroup.setUpdateDate(obj[13] == null ? null : (Date)obj[13]);
	   		return siGroup;
		}
 	    else
 	    	return new SiGroup(new SiGroupId(brandCode, groupCode));
    }
  
    public String saveAjaxData(SiGroup modifyObj) throws Exception {
      try{
  			log.info("Save 該 Save的資料");
      	siGroupDAO.saveOrUpdate(modifyObj);
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
  		try{
  			Object otherBean = parameterMap.get("vatBeanOther");
  			String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
  			String groupCode = (String)PropertyUtils.getProperty(otherBean, "groupCode");
  			SiGroupId id = new SiGroupId(brandCode,groupCode);
  			SiGroup siGroup = siGroupDAO.findById(id);
  			Map multiList = new HashMap(0);
  			List<BuCommonPhraseLine> brandCodes = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "BrandCode" );
  			String dates[];
    			if(siGroup != null){
    				resultMap.put("groupCode",groupCode);
    				resultMap.put("groupName",siGroup.getGroupName());
    				resultMap.put("description",siGroup.getDescription());
    				resultMap.put("creationDate","");
    				if(siGroup.getCreationDate() != null){
        			dates = siGroup.getCreationDate().toString().split("-");
      				resultMap.put("creationDate",dates[0]+"/"+dates[1]+"/"+dates[2].substring(0,2));
    				}
    				resultMap.put("createdByName",UserUtils.getUsernameByEmployeeCode(siGroup.getCreatedBy()));
    				resultMap.put("brandCodes", brandCode);
    			}else{
    				resultMap.put("groupCode","");
    				resultMap.put("groupName","");
    				resultMap.put("description","");
    				multiList.put( "brandCodes", AjaxUtils.produceSelectorData(brandCodes,"lineCode","name",true,false));
            String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
            String employeeName = UserUtils.getUsernameByEmployeeCode(employeeCode);
            resultMap.put("createdBy",employeeCode);
            resultMap.put("createdByName",employeeName);
            resultMap.put("creationDate",new Date());
    			}
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
  	
  	public List<Properties> executeSearchInitial(Map parameterMap) throws Exception{
  		Map resultMap = new HashMap(0);
  		try{
  			Map multiList = new HashMap(0);
  			List<BuCommonPhraseLine> brandCodes = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "BrandCode" );
  			multiList.put( "brandCodes", AjaxUtils.produceSelectorData(brandCodes,"lineCode","name",true,true));
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

  	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {
     	try{
     	    List<Properties> result = new ArrayList();
     	    List<Properties> gridDatas = new ArrayList();
     	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
     	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
     	    //======================帶入Head的值=========================
     	    String brandCode 		= httpRequest.getProperty("brandCode");
     	    String groupCode		= httpRequest.getProperty("groupCode");
     	    String groupName 		= httpRequest.getProperty("groupName");     	    

     	    HashMap map = new HashMap();
     	    //==============================================================	    
     	    HashMap findObjs = new HashMap();
     	    findObjs.put(" and id.brandCode = :brandCode", brandCode);
     	    findObjs.put(" and id.groupCode = :groupCode", groupCode);
     	   	findObjs.put(" and groupName = :groupName", groupName);
     	  	
     	    Map siGroupMap = siGroupDAO.search("SiGroup", findObjs, iSPage, iPSize , BaseDAO.QUERY_SELECT_RANGE);
     	   	List<SiGroup> siGroups = (List<SiGroup>)siGroupMap.get(BaseDAO.TABLE_LIST);
     	   	
     	   if(siGroups != null && siGroups.size() > 0){
	     	   // 取得第一筆的INDEX
	     	   Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
	     	   log.info("firstIndex = " + firstIndex);
	     	   // 取得最後一筆 INDEX
	     	   Long maxIndex = (Long)siGroupDAO.search("SiGroup", "count(*) as rowCount", findObjs, iSPage, iPSize , BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT);
	     	   log.info("maxIndex = " + maxIndex);
	     	   result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,siGroups, gridDatas, firstIndex, maxIndex));
     	   } 
     	   else
     		   result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
     	   return result;
     	}
     	catch(Exception ex){
     	    log.error("載入頁面顯示的群組資料發生錯誤，原因：" + ex.toString());
     	    throw new Exception("載入頁面顯示群組資料失敗！");
     	}	
    }
  	
    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception {
	    String errorMsg = null;
	    AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
	  	return AjaxUtils.getResponseMsg(errorMsg);
    }

    public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception {
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
    
    //新增或修改SiGroup(wade)
    public void saveSiGroup(String brandCode, String employeeCode, String creater) throws FormException, Exception {
	    try{
	      	//Object obj = siGroupDAO.findByProperty("SiGroup", new String[]{"id.brandCode", "id.groupCode"}, new Object[]{brandCode, employeeCode});
	      	
	      	Object obj = siGroupDAO.find("select count(*) from SiGroup G where G.id.brandCode=? and G.id.groupCode=?", new Object[]{brandCode, employeeCode});
	      	Long count = (Long)(((List)obj).get(0));
	      	if(count == 0){
	      		SiGroupId siGroupId = new SiGroupId(brandCode, employeeCode);
	      		SiGroup groupg = new SiGroup();
	      		groupg.setId(siGroupId);
	      		groupg.setGroupName(employeeCode);
	      		groupg.setEnable("Y");
	      		groupg.setCreatedBy(creater);
	      		groupg.setCreationDate(new Date());
	      		groupg.setUpdatedBy(creater);
	      		groupg.setUpdateDate(new Date());
	      		siGroupDAO.save(groupg);
	      	}
	      	else{
	      		siGroupDAO.update("update SiGroup set enable=?, updatedBy=?, updateDate=? where id.brandCode=? and id.groupCode=?", 
	      				new Object[]{"Y", creater, new Date(), brandCode, employeeCode});
	      		/*
	      		SiGroup groupg = groups.get(0);
	      		if(groupg.getEnable() == null || !groupg.getEnable().equals("Y")){
	      			groupg.setEnable("Y");
	      			groupg.setUpdatedBy(creater);
		      		groupg.setUpdateDate(new Date());
	      			siGroupDAO.update(groupg);
	      		}
	      		*/
	      	}
	    }
	    catch(Exception ex){		  
	    	log.error("使用者群組存檔時發生錯誤，原因：" + ex.toString());
	    	throw new Exception("使用者群組存檔時發生錯誤，原因：" + ex.toString());
	    }
    }
    
    public int deleteUnableSiGroupMenu(String brandCode, String groupCode){
    	return siGroupMenuDAO.deleteUnableSiGroupMenu(brandCode, groupCode);
    }
    
    public void saveOrUpdateSiUsersGroup(String groupCode, String brandCode, String employeeCode, String creater){
    	Date today = new Date();
    	String hql = "from SiUsersGroup G where G.groupCode=? and G.brandCode=? and G.employeeCode=?";
    	Object obj = siUsersGroupDAO.find(hql, new Object[]{groupCode, brandCode, employeeCode});
    	if(obj == null || ((List<SiUsersGroup>)obj).size() == 0 || ((List<SiUsersGroup>)obj).get(0) == null){
    		SiUsersGroup siUsersGroup = new SiUsersGroup();
    		siUsersGroup.setBrandCode(brandCode);
    		siUsersGroup.setEmployeeCode(employeeCode);
    		siUsersGroup.setGroupCode(groupCode);
    		siUsersGroup.setCreatedBy(creater);
    		siUsersGroup.setCreationDate(today);
    		siUsersGroup.setUpdatedBy(creater);
    		siUsersGroup.setUdpateDate(today);
    		siUsersGroup.setIndexNo(0L);
    		siUsersGroupDAO.save(siUsersGroup);
    	}
    	
    	siUsersGroupDAO.update("update SiUsersGroup set groupCode = 'NEW_PRI_BAK_' || groupCode where groupCode<>? and brandCode=? and employeeCode=?", 
    			new Object[]{groupCode, brandCode, employeeCode});
    	/*
    	List<SiUsersGroup> groups = (List<SiUsersGroup>)siUsersGroupDAO.find("from SiUsersGroup G where G.groupCode<>? and G.brandCode=? and G.employeeCode=?", 
    			new Object[]{groupCode.trim(), brandCode.trim(), employeeCode.trim()});
    	for(SiUsersGroup group : groups){
    		group.setGroupCode("NEW_PRI_BAK_" + group.getGroupCode());
    		group.setUpdatedBy(creater);
    		group.setUdpateDate(today);
    		siUsersGroupDAO.update(group);
    	}
    	*/
    }
    
    public List<Properties> deleteByTimeScope(Map parameterMap) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		log.info("deleteByTimeScope.by Map");
		Map resultMap = new HashMap(0);
		log.info(parameterMap.keySet().toString());
		String timeScope = (String) parameterMap.get(AjaxUtils.TIME_SCOPE);		
		log.info("deleteByTimeScope by Map.timeScope:"+ timeScope);
		if(StringUtils.hasText(timeScope)){
			tmpAjaxSearchDataDAO.deleteByTimeScope(timeScope);
		}
		log.info("deleteByTimeScope.success");
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
    
}
