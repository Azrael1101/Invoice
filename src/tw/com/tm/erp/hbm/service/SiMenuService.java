package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List; 
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.dao.SiGroupMenuDAO;
import tw.com.tm.erp.hbm.dao.SiMenuDAO;
import tw.com.tm.erp.hbm.dao.TmpAjaxSearchDataDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class SiMenuService {
	private static final Log log = LogFactory.getLog(SiMenuService.class);
	private SiMenuDAO siMenuDAO;
	private SiGroupMenuDAO siGroupMenuDAO;
	private BuCommonPhraseHeadDAO buCommonPhraseHeadDAO;
	private BuCommonPhraseService buCommonPhraseService;
	private DataSource dataSource;
	private TmpAjaxSearchDataDAO tmpAjaxSearchDataDAO;

	public void setBuCommonPhraseHeadDAO(BuCommonPhraseHeadDAO buCommonPhraseHeadDAO) {
		this.buCommonPhraseHeadDAO = buCommonPhraseHeadDAO;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}
	
	public void setSiGroupMenuDAO(SiGroupMenuDAO siGroupMenuDAO) {
		this.siGroupMenuDAO = siGroupMenuDAO;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setTmpAjaxSearchDataDAO(TmpAjaxSearchDataDAO tmpAjaxSearchDataDAO) {
		this.tmpAjaxSearchDataDAO = tmpAjaxSearchDataDAO;
	}
	
	public void setSiMenuDAO(SiMenuDAO siMenuDAO) {
		this.siMenuDAO = siMenuDAO;
	}
	
	public static final Object[][] MASTER_DEFINITION = {
		{"NAME","TYPE","VALUE","STYLE","VALUE"},
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
    	{"lastUpdateDate" ,AjaxUtils.FIELD_TYPE_DATE  ,"", "", ""}
    };
	
	public static final String[] GRID_SEARCH_FIELD_NAMES = {"functionCode", "name", "url", "parentMenuId", "menuId" };
	
	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {"", "", "", "0", "0"};
	
	public static final String[] GRID_NEW_PRIVILEGE_FIELD_NAMES = {"menuId", "indexNo", "title", "enable"};
	
	public static final String[] GRID_NEW_PRIVILEGE_FIELD_DEFAULT_VALUES = {"0", "0", "", "N"};
	
	public List getBrandUserMenuManager(String brandCode, String loginName,
			String systemType, String functionType) {// 取得User的Menu List
		return siMenuDAO.getBrandUserMenuManager(brandCode, loginName,
				systemType, functionType);
	}

	public List getBrandUserReportManager(String brandCode, String loginName,
			String systemType, String functionType, String functionCode) {// 取得該組條件下(brandCode,loginName,"R","R",functionCode)的SIMenu
																			// List,主要用來檢視該User是否有該報表的權限
		return siMenuDAO.getBrandUserReportManager(brandCode, loginName,
				systemType, functionType, functionCode);
	}

	public List getFunctionURL(String systemType, String functionType,
			String functionCode) {// 取得該組條件下的SIMenu List
		return siMenuDAO.getFunctionURL(systemType, functionType, functionCode);
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
		}
		catch(Exception ex){
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
	
	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {
	  	try{
		  	List<Properties> result = new ArrayList();
		  	List<Properties> gridDatas = new ArrayList();
		  	int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		  	int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
	  	  
		  	//======================帶入Head的值=========================
	  	    
		  	//String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
		  	String functionCode = httpRequest.getProperty("functionCode");
		  	//String systemType = httpRequest.getProperty("systemType");
		  	//String functionType = httpRequest.getProperty("functionType");
		  	//String type = httpRequest.getProperty("type");
		  	String name = httpRequest.getProperty("name");
	  	    
		  	HashMap map = new HashMap();
		  	HashMap findObjs = new HashMap();
		  	findObjs.put(" and functionCode like :functionCode", "%" + functionCode + "%");
		  	findObjs.put(" and name like :name", "%" + name + "%");
		  	//findObjs.put(" and systemType = :systemType",systemType);
		  	//findObjs.put(" and functionType = :functionType",functionType);
		  	//findObjs.put(" and type = :type",type);
	
		  	//==============================================================	    
	  	    
		  	Map siMenuMap = siMenuDAO.search( "SiMenu", findObjs, iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
		  	List<SiMenu> siMenus = (List<SiMenu>) siMenuMap.get(BaseDAO.TABLE_LIST); 
	
		  	log.info("siMenus.size"+ siMenus.size());	
		  	if(siMenus != null && siMenus.size() > 0){
		  		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		  		Long maxIndex = (Long)siMenuDAO.search("SiMenu", "count(systemType) as rowCount" ,findObjs, iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		  		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,siMenus, gridDatas, firstIndex, maxIndex));
		  	}
		  	else
		  		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
	
		  	return result;
	  	}
	  	catch(Exception ex){
	  		log.error("載入頁面顯示的選單查詢發生錯誤，原因：" + ex.toString());
	  		throw new Exception("載入頁面顯示的選單功能查詢失敗！");
	  	}	
  	}
	
	public boolean isRepeat( String propertyName,  String value ){
		return siMenuDAO.isRepeat( propertyName, value );
	}
	
	public List<Properties> executeInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			
			String loginEmployeeCodeString = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			
			String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
			Long formId = StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
			List<SiMenu> siMenu = formId != null ? siMenuDAO.findByProperty( "SiMenu", "menuId", formId) : null; 
			
			Map multiList = new HashMap(0);
			
			List<BuCommonPhraseLine> allSystemTypes = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "SiMenuSystemType" );
			//log.info( "allSystemTypes size:" + allSystemTypes.size() );			
			List<BuCommonPhraseLine> allFunctionTypes = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "SiMenuFunctionType" );
			//log.info( "allFunctionTypes size:" + allFunctionTypes.size() );	
			List<BuCommonPhraseLine> allTypes = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "SiMenuType" );
			//log.info( "allFunctionTypes size:" + allTypes.size() );	
			String dates[] ;
			if( siMenu != null && siMenu.size() > 0 ){
				SiMenu sm = siMenu.get(0);
				
				resultMap.put( "menuId", sm.getMenuId() );
				resultMap.put( "functionCode", sm.getFunctionCode() );
				resultMap.put( "parentMenuId", sm.getParentMenuId() );
				resultMap.put( "url", sm.getUrl() );
				resultMap.put( "lineNo", sm.getLineNo() );
				resultMap.put("systemType",sm.getSystemType());
				resultMap.put("functionType",sm.getFunctionType());
				resultMap.put("type", sm.getType());
				String employeeName = UserUtils.getUsernameByEmployeeCode(sm.getCreatedBy());
				resultMap.put("createdBy",sm.getCreatedBy());
				resultMap.put("createdByName",employeeName);
				resultMap.put("creationDate",sm.getCreationDate());
				if(sm.getCreationDate() != null){
					dates = sm.getCreationDate().toString().split("-");
					resultMap.put("creationDate",dates[0]+"/"+dates[1]+"/"+dates[2].substring(0,2));
				}
			} 
			else{
				resultMap.put( "functionCode", "" );
				resultMap.put( "parentMenuId", "0" );
				resultMap.put( "url", "" );
				resultMap.put( "lineNo", "" );
				String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCodeString);
				resultMap.put("createdBy",loginEmployeeCodeString);
				resultMap.put("createdByName",employeeName);
				resultMap.put("creationDate", new Date() );
				multiList.put( "systemType", AjaxUtils.produceSelectorData(allSystemTypes ,"lineCode" ,"name",  true,  false));
				multiList.put( "functionType", AjaxUtils.produceSelectorData(allFunctionTypes ,"lineCode" ,"name",  true,  false));
				multiList.put( "type", AjaxUtils.produceSelectorData(allTypes ,"lineCode" ,"name",  true,  false));
			}
			resultMap.put("multiList",multiList);
			//log.info( "結束" );				
		}
		catch(Exception ex){
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
			List<BuCommonPhraseLine> allSystemTypes = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "SiMenuSystemType" );
			//log.info( "allSystemTypes size:" + allSystemTypes.size() );			
			List<BuCommonPhraseLine> allFunctionTypes = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "SiMenuFunctionType" );
			//log.info( "allFunctionTypes size:" + allFunctionTypes.size() );	
			List<BuCommonPhraseLine> allTypes = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "SiMenuType" );
			//log.info( "allFunctionTypes size:" + allTypes.size() );	
			multiList.put( "allSystemTypes", AjaxUtils.produceSelectorData(allSystemTypes ,"lineCode" ,"name"         ,  true,  true));
			multiList.put( "allFunctionTypes", AjaxUtils.produceSelectorData(allFunctionTypes ,"lineCode" ,"name"         ,  true,  true));
			multiList.put( "allTypes", AjaxUtils.produceSelectorData(allTypes ,"lineCode" ,"name"         ,  true,  true));
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
	    	//Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    	//Object otherBean = parameterMap.get("vatBeanOther");
	    	//String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
	    	
	    	//取得欲更新or新建bean
	    	SiMenu siMenu = getActualMovement(formBindBean);
	    	log.info("======Start copyJSONBeantoPojoBean=========");
	    	AjaxUtils.copyJSONBeantoPojoBean(formBindBean, siMenu);
	    	log.info("======Finish copyJSONBeantoPojoBean=========");
	    	
	    	siMenu.setEnable("Y");
	    	if(!StringUtils.hasText((String)PropertyUtils.getProperty(formBindBean, "reserve1"))) siMenu.setReserve1("");
	    	Date now = new Date();
	    	siMenu.setCreationDate(now);
	    	siMenu.setUpdateDate(now);
	    	siMenu.setUpdatedBy(siMenu.getCreatedBy());
	    	
	    	String resultMsg = siMenuDAO.saveOrUpdate(siMenu) ? "OK" : "FAIL";
	    	//String resultMsg = this.saveAjaxData(siMenuPO, formAction, employeeCode );
	    	log.info("resultMsg : " + resultMsg );
	    	resultMap.put("menuId", siMenu.getMenuId());
	    	resultMap.put("resultMsg", resultMsg);
	     	resultMap.put("entityBean", siMenu);
	    }
	    catch (FormException fe) {
	       log.error("系統選單存檔失敗，原因：" + fe.toString());
	       throw new FormException("系統選單存檔失敗，原因：" + fe.toString());
	    }
	    catch (Exception ex) {		  
	      log.error("系統選單存檔時發生錯誤，原因：" + ex.toString());
	      throw new Exception("系統選單單存檔失敗，原因：" + ex.toString());
	    }
	    resultMap.put("vatMessage" ,msgBox);
	    log.info(" updateAJAXMinu 結束");
	    return resultMap;
	}
	
	private SiMenu getActualMovement(Object bean) throws FormException, Exception{
	  	Long menuId;
	  	SiMenu siMenu = null;
	  	if(StringUtils.hasText((String)PropertyUtils.getProperty(bean, "menuId"))){
	  		menuId = Long.valueOf((String)PropertyUtils.getProperty(bean, "menuId"));
	  		siMenu = (SiMenu)siMenuDAO.findById("SiMenu", menuId);
	  	}
	    return siMenu == null ? new SiMenu() : siMenu;
	}
	
	public String saveAjaxData(SiMenu modifyObj,String formAction, String employeeCode ) throws Exception {
		try {
			if (null != modifyObj) {
				if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
					modifyObj.setUpdatedBy( employeeCode );
					modifyObj.setUpdateDate(new Date());
					siMenuDAO.saveOrUpdate(modifyObj);
				}
				
				return modifyObj.getMenuId() + "成功";
			} else {
				throw new FormException("");
			}
		} catch (FormException fe) {
			log.error("系統選單存檔時發生錯誤，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("系統選單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("選單存檔時發生錯誤，原因：" + ex.getMessage());
		}

	}
	
	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	
	/*
	//權限維護作業(wade)
	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception {
		try{
		    List<Properties> result = new ArrayList();
		    List<Properties> gridDatas = new ArrayList();
		    int startPage = AjaxUtils.getStartPage(httpRequest) + 1;// 取得起始頁面
		    int pageSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小	    
		    //======================帶入Head的值=========================
		    String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
		    String employeeCode = httpRequest.getProperty("employeeCode");// 員工代號
		    String applicant = httpRequest.getProperty("applicant");// 申請人
		    //==============================================================	
		    
		    List<TmpGetMenuInfo> menus = getUserMenu(brandCode, employeeCode, applicant);
		    
		    if(menus != null && menus.size() > 0){
		    	// 取得第一筆的INDEX
		    	Long firstIndex = (startPage * pageSize) - (pageSize - 1L);
		    	// 取得最後一筆 INDEX
		    	//Long maxIndex = Long.parseLong(((startPage) * pageSize) + "");
		    	//Long maxIndex = siMenuDAO.findPageLineCount(employeeCode, brandCode);
		    	Long maxIndex = Long.parseLong(menus.size() + "");
		    	
		    	for(int i = 0; i<menus.size(); i++){
		    		TmpGetMenuInfo m = (TmpGetMenuInfo)menus.get(i);
		    		m.setIndexNo(Integer.parseInt((firstIndex+i) + ""));
		    		
				}
		    	result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_NEW_PRIVILEGE_FIELD_NAMES, GRID_NEW_PRIVILEGE_FIELD_DEFAULT_VALUES, menus, gridDatas, firstIndex, maxIndex));
		    }
		    
		
		    return result;
		}
		catch(Exception ex){
		    log.error("載入頁面顯示的銷貨明細發生錯誤，原因：" + ex.toString());
		    throw new Exception("載入頁面顯示的銷貨明細失敗！");
		}	
	}
	*/
	
	/*
	public List<TmpGetMenuInfo> getUserMenu(String brandCode, String creator, String employee) throws Exception {
		Connection conn = null;
		CallableStatement calStmt = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<TmpGetMenuInfo> menus = new ArrayList<TmpGetMenuInfo>();
		try{
			conn = dataSource.getConnection();
			calStmt = conn.prepareCall("{call ERP.APP_GEN_SI_MENU_PACKAGE.starting(?, ?, ?)}"); // 呼叫store procedure
			calStmt.setString(1, "T17888");
			calStmt.setString(2, creator);
			calStmt.setString(3, brandCode);
			calStmt.execute();
			
			stmt = conn.prepareStatement("SELECT * FROM ERP.TMP_GET_MENU_INFO");//TMP_GET_MENU_INFO
			rs = stmt.executeQuery();
			while(rs.next()){
				TmpGetMenuInfo menu = new TmpGetMenuInfo();
				menu.setBrandCode(rs.getString("BRAND_CODE"));
				menu.setEmployeeCode("EMPLOYEE_CODE");
				menu.setTitle(rs.getString("TITLE"));
				menu.setTitleId(rs.getString("TITLE_ID"));
				menu.setMenuId(rs.getString("MENU_ID"));
				menu.setUrl(rs.getString("URL"));
				menu.setUrl(rs.getString("ENABLE"));
				menus.add(menu);
			}
			log.info("已經處理完成");
		} 
		catch(Exception ex){
			log.error("呼叫posOnlineExport發生錯誤，原因：" + ex.getMessage());
			throw ex;
		} 
		finally{
			if(calStmt != null){
				try{
					calStmt.close();
				} 
				catch(SQLException e){
					log.error("關閉CallableStatement時發生錯誤！");
				}
			}
			if(conn != null){
				try{
					conn.close();
				} 
				catch(SQLException e){
					log.error("關閉Connection時發生錯誤！");
				}
			}
		}
		return menus;
	}
	*/
	
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
	
	public List<Properties> getAJAXFormDataByMenuId(Properties httpRequest) throws Exception {
		log.info("getAJAXFormDataByMenuId");
		Properties pro = new Properties();
		List re = new ArrayList();
		Long menuId = NumberUtils.getLong(httpRequest.getProperty("menuId"));
		SiMenu siMenu = (SiMenu)siMenuDAO.findById("SiMenu", menuId);
		if(null != siMenu){
		    pro.setProperty("menuId", AjaxUtils.getPropertiesValue(siMenu.getMenuId(),    ""));
			pro.setProperty("lineNo", AjaxUtils.getPropertiesValue(siMenu.getLineNo(),     ""));
			pro.setProperty("systemType", AjaxUtils.getPropertiesValue(siMenu.getSystemType(),     ""));
			pro.setProperty("functionType", AjaxUtils.getPropertiesValue(siMenu.getFunctionType(),    ""));
			pro.setProperty("type", AjaxUtils.getPropertiesValue(siMenu.getType(), ""));
			pro.setProperty("name", AjaxUtils.getPropertiesValue(siMenu.getName(),   ""));
			pro.setProperty("functionCode", AjaxUtils.getPropertiesValue(siMenu.getFunctionCode(),   ""));
			pro.setProperty("url", AjaxUtils.getPropertiesValue(siMenu.getUrl(),   ""));
			pro.setProperty("parentMenuId", AjaxUtils.getPropertiesValue(siMenu.getParentMenuId(),   ""));
			pro.setProperty("parentMenuName", AjaxUtils.getPropertiesValue(siMenu.getParentMenuName(),   ""));
			pro.setProperty("reserve1", AjaxUtils.getPropertiesValue(siMenu.getReserve1(),   ""));
			pro.setProperty("reserve2", AjaxUtils.getPropertiesValue(siMenu.getReserve2(),   ""));
			pro.setProperty("reserve3", AjaxUtils.getPropertiesValue(siMenu.getReserve3(),   ""));
			pro.setProperty("reserve4", AjaxUtils.getPropertiesValue(siMenu.getReserve4(),   ""));
			pro.setProperty("reserve5", AjaxUtils.getPropertiesValue(siMenu.getReserve5(),   ""));
			pro.setProperty("createdBy", AjaxUtils.getPropertiesValue(siMenu.getCreatedBy(),   ""));
			pro.setProperty("creationDate", AjaxUtils.getPropertiesValue(siMenu.getCreationDate(),   ""));
			pro.setProperty("updatedBy", AjaxUtils.getPropertiesValue(siMenu.getUpdatedBy(),   ""));
			pro.setProperty("updateDate", AjaxUtils.getPropertiesValue(siMenu.getUpdateDate(),   ""));
			pro.setProperty("owner", AjaxUtils.getPropertiesValue(siMenu.getOwner(),   ""));
		} 
		else{
			pro.setProperty("menuId", "");
			pro.setProperty("lineNo", "");
			pro.setProperty("systemType", "");
			pro.setProperty("functionType", "");
			pro.setProperty("type", "");
			pro.setProperty("name", "");
			pro.setProperty("functionCode", "");
			pro.setProperty("url", "");
			pro.setProperty("parentMenuId", "");
			pro.setProperty("parentMenuName", "");
			pro.setProperty("reserve1", "");
			pro.setProperty("reserve2", "");
			pro.setProperty("reserve3", "");
			pro.setProperty("reserve4", "");
			pro.setProperty("reserve5", "");
			pro.setProperty("createdBy", "");
			pro.setProperty("creationDate", "");
			pro.setProperty("updatedBy", "");
			pro.setProperty("updateDate", "");
			pro.setProperty("owner", "");
		}
		re.add(pro);
		return re;
	}

}