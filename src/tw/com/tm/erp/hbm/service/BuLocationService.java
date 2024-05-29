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
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiFunction;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuLocationDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class BuLocationService {

    private static final Log log = LogFactory.getLog(BuLocationService.class);
    private BuLocationDAO buLocationDAO;

    public static final Object[][] MASTER_DEFINITION = {
	    { "NAME", "TYPE", "VALUE", "STYLE", "VALUE" },
	    { "locationId", AjaxUtils.FIELD_TYPE_LONG, "0", "mode:HIDDEN", "" },
	    { "locationName", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
	    { "city", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
	    { "zipCode", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
	    { "address", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
	    { "enable", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
	    { "createdBy", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
	    { "creationDate", AjaxUtils.FIELD_TYPE_DATE, "", "", "" },
	    { "lastUpdatedBy", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
	    { "lastUpdateDate", AjaxUtils.FIELD_TYPE_DATE, "", "", "" } };

    public void setBuLocationDAO(BuLocationDAO buLocationDAO) {
    	this.buLocationDAO = buLocationDAO;
    }

    /**
	 * 地點查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		 "locationName", "city", "area",
		 "zipCode", "lastUpdatedByName", "lastUpdateDate", 
		 "locationId"
	};
	
	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
		 "", "", "",
		 "", "", "", 
		 ""
	};
    
    /*
     * public List<Properties> executeInitial(Map parameterMap) throws
     * Exception{ Map resultMap = new HashMap(0);
     * 
     * try{ Object otherBean = parameterMap.get("vatBeanOther"); String
     * formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
     * Long formId = StringUtils.hasText(formIdString)?
     * Long.valueOf(formIdString):null;
     * 
     * log.info("formId:"+formId); Map multiList = new HashMap(0);
     * 
     * 
     * BuLocation form = null == formId ?
     * executeNewBuLocation(otherBean):findBuLocation(otherBean, resultMap);
     * 
     * 
     * log.info( "getLocationId: " + form.getLocationId() ); log.info(
     * "getLocationName: " + form.getLocationName() ); log.info( "getAddress: " +
     * form.getAddress() ); log.info( "getArea: " + form.getArea() ); log.info(
     * "getCity: " + form.getCity() ); log.info( "getZipCode: " +
     * form.getZipCode() ); log.info( "getEnable: " + form.getEnable() );
     * log.info( "getCreatedBy: " + form.getCreatedBy() ); log.info(
     * "getCreationDate: " + form.getCreationDate() ); log.info(
     * "getLastUpdatedBy: " + form.getLastUpdatedBy() ); log.info(
     * "getLastUpdateDate: " + form.getLastUpdateDate() );
     * 
     * 
     * resultMap.put("locationId",form.getLocationId()); }catch(Exception ex){
     * log.error("表單初始化失敗，原因：" + ex.toString()); Map messageMap = new HashMap();
     * messageMap.put("type" , "ALERT"); messageMap.put("message",
     * "表單初始化失敗，原因："+ex.toString()); messageMap.put("event1" , null);
     * messageMap.put("event2" , null); resultMap.put("vatMessage",messageMap); }
     * 
     * return AjaxUtils.parseReturnDataToJSON(resultMap); }
     */

	/**
     * 檢核地點資料
     * 
     * @param location
     * @throws ValidationErrorException
     */
    private void doBuLocationValidate(BuLocation location)
	    throws ValidationErrorException {

		if (!StringUtils.hasText(location.getLocationName())) {
		    throw new ValidationErrorException("請輸入地點名稱！");
		} else {
		    location.setLocationName(location.getLocationName().trim()
			    .toUpperCase());
		}
	
		if (StringUtils.hasText(location.getZipCode())) {
		    location.setZipCode(location.getZipCode().trim());
		    if (!ValidateUtil.isNumber(location.getZipCode())) {
			throw new ValidationErrorException("郵遞區號必須為0~9數字！");
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
			BuLocation buLocation = this.executeFindActualBuLocation(parameterMap);
			
			Map multiList = new HashMap(0);
			resultMap.put("form", buLocation);
			
			resultMap.put("multiList",multiList);
			return resultMap;
		}catch(Exception ex){
			log.error("地點初始化失敗，原因：" + ex.toString());
			throw new Exception("地點單初始化失敗，原因：" + ex.toString());
			
		}
	}
    
	/**
	 * 產生一筆 BuLocation
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
    public BuLocation executeNewBuLocation() throws Exception {

		BuLocation form = new BuLocation();
		form.setAddress("");
		form.setArea("");
		form.setCity("");
		form.setEnable("Y");
		form.setLocationName("");
		form.setZipCode("");
	
		return form;

    }

	/**
	 * 依formId取得實際地點主檔 in 送出
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
    public BuLocation executeFindActualBuLocation(Map parameterMap)
	    throws FormException, Exception {
    	
//		Object formBindBean = parameterMap.get("vatBeanFormBind");
//		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");
		
		BuLocation buLocation = null;
    	try {
		
		    String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
		    Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
	
		    buLocation = null == formId ? this.executeNewBuLocation(): this.findById(formId) ;
		
		    parameterMap.put( "entityBean", buLocation);
		    return buLocation;
    	} catch (Exception e) {
    		log.error("取得實際地點主檔失敗,原因:"+e.toString());
			throw new Exception("取得實際地點主檔失敗,原因:"+e.toString());
		}
    }

    /**
     * 依據primary key為查詢條件，取得地點資料
     * 
     * @param locationId
     * @return BuLocation
     * @throws Exception
     */
    public BuLocation findById(Long locationId) throws Exception {

		try {
		    BuLocation location = (BuLocation) buLocationDAO.findByPrimaryKey(
			    BuLocation.class, locationId);
		    return location;
		} catch (Exception ex) {
		    log.error("依據主鍵：" + locationId + "查詢地點資料時發生錯誤，原因：" + ex.toString());
		    throw new Exception("依據主鍵：" + locationId + "查詢地點資料時發生錯誤，原因："
			    + ex.getMessage());
		}
    }

    public List<BuLocation> find(BuLocation buLocation)
	    throws IllegalAccessException, InvocationTargetException,
	    IllegalArgumentException, SecurityException, NoSuchMethodException,
	    ClassNotFoundException {
		log.info("BuLocationService.find");
		BuLocation searchObj = new BuLocation();
		BeanUtils.copyProperties(buLocation, searchObj);
		BeanUtil.changeSpace2Null(searchObj);
		List temp = new ArrayList();
		if (null != searchObj.getLocationId()) {
		    temp.add(buLocationDAO.findByPrimaryKey(BuLocation.class,
			    buLocation.getLocationId()));
		} else {
		    temp = buLocationDAO.findByExample(searchObj);
		}
		return temp;
    }

    /**
     * 依據地點資料查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List<BuLocation> find(HashMap conditionMap) throws Exception {

		try {
		    String locationName = (String) conditionMap.get("locationName");
		    String city = (String) conditionMap.get("city");
		    String area = (String) conditionMap.get("area");
		    String zipCode = (String) conditionMap.get("zipCode");
	
		    conditionMap.put("locationName", locationName.trim().toUpperCase());
		    conditionMap.put("city", city.trim());
		    conditionMap.put("area", area.trim());
		    conditionMap.put("zipCode", zipCode.trim());
	
		    return buLocationDAO.find(conditionMap);
		} catch (Exception ex) {
		    log.error("查詢地點資料時發生錯誤，原因：" + ex.toString());
		    throw new Exception("查詢地點資料時發生錯誤，原因：" + ex.getMessage());
		}
    }

    /**
     * 查詢出全部的地點資料
     * 
     * @return List
     * @throws Exception
     */
    public List<BuLocation> findAll() throws Exception {

		try {
		    return buLocationDAO.findAll();
		} catch (Exception ex) {
		    log.error("查詢地點資料時發生錯誤，原因：" + ex.toString());
		    throw new Exception("查詢地點資料時發生錯誤，原因：" + ex.getMessage());
		}
    }

    public String findLocationNameById(Long locationId) {
    	BuLocation buLocation = (BuLocation) buLocationDAO.findByPrimaryKey(
		BuLocation.class, locationId);
		if (buLocation != null)
		    return buLocation.getLocationName();
		else
		    return "unknow";
    }
    
    public BuLocation findBuLocation(Object otherBean, Map resultMap) throws NoSuchDataException, IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException, FormException {
	
		String formIdString = (String) PropertyUtils.getProperty(otherBean,
			"formId");
		Long formId = StringUtils.hasText(formIdString) ? Long
			.valueOf(formIdString) : null;
	
		BuLocation form = (BuLocation) buLocationDAO.findById("BuLocation",
			formId);
	
		if (null != form) {
	
		    resultMap.put("form", form);
	
		    return form;
		} else {
		    throw new NoSuchDataException("查無此單號(" + formId
			    + ")，於按下「確認」鍵後，將關閉本視窗！");
	
		}
	
	}
    
	/**
	 * ajax 取得查詢的結果
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
	  	    
	  	    String locationName = httpRequest.getProperty("locationName");
	  	    String city = httpRequest.getProperty("city");
	  	    String area = httpRequest.getProperty("area");
	  	    String zipCode = httpRequest.getProperty("zipCode");
	  	    
	  	    log.info("locationName:"+locationName);
	  	    log.info("city:"+city);
	  	    log.info("area:"+area);
	  	    log.info("zipCode:"+zipCode);
	  	    
	  	    HashMap map = new HashMap();
	  	    HashMap findObjs = new HashMap();
	  	    findObjs.put("and model.locationName like :locationName","%"+locationName+"%");
	  	    findObjs.put("and model.city like :city","%"+city+"%");
	  	    findObjs.put("and model.area like :city","%"+area+"%");
	  	    findObjs.put("and model.zipCode like :zipCode","%"+zipCode+"%");
	  	    
	  	    //==============================================================	    
	  	    
	  	    Map buLocationMap = buLocationDAO.search( "BuLocation as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	  	    List<BuLocation> buLocations = (List<BuLocation>) buLocationMap.get(BaseDAO.TABLE_LIST); 

	  	    log.info("BuLocation.size"+ buLocations.size());	
	  	    if (buLocations != null && buLocations.size() > 0) {
	  	    	
	  	    	// 設定額外欄位
	  	    	this.setLineOtherColumn(buLocations);
	  	    	
	  	    	Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
	  	    	Long maxIndex = (Long)buLocationDAO.search("BuLocation as model", "count(model.locationId) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
	  	    	
	  	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,buLocations, gridDatas, firstIndex, maxIndex));
	  	    }else {
	  	    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
	  	    }

	  	    return result;
	  	}catch(Exception ex){
	  	    log.error("載入頁面顯示的地點功能查詢發生錯誤，原因：" + ex.toString());
	  	    throw new Exception("載入頁面顯示的地點功能查詢失敗！");
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
     * 新增至BuLocation
     * 
     * @param saveObj
     * @param loginUser
     */
    private void insertBuLocation(BuLocation saveObj, String loginUser) {

		saveObj.setCreatedBy(loginUser);
		saveObj.setCreationDate(new Date());
		saveObj.setLastUpdatedBy(loginUser);
		saveObj.setLastUpdateDate(new Date());
		buLocationDAO.save(saveObj);
    }
	
	public void modifyLocation(BuLocation updateObj) {
		updateObj.setLastUpdateDate(new Date());
		// resetLineReserve(updateObj.getImMovementItems());
		buLocationDAO.update(updateObj);

    }
	
	/**
     * 修改BuLocation
     * 
     * @param saveObj
     * @param loginUser
     */
    private void modifyBuLocation(BuLocation updateObj, String loginUser) {

		updateObj.setLastUpdatedBy(loginUser);
		updateObj.setLastUpdateDate(new Date());
		buLocationDAO.update(updateObj);
    }
	
    public String saveAjaxData(BuLocation modifyObj, String formAction)
	    throws Exception {
	try {
	    if (null != modifyObj) {

		log.info("getLocationId = " + modifyObj.getLocationId());
		log.info("formAction = " + formAction);
		log
			.info("OrderStatus.FORM_SUBMIT = "
				+ OrderStatus.FORM_SUBMIT);
		log.info("formAction compare result = "
			+ OrderStatus.FORM_SUBMIT.equals(formAction));

		if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
		    // log.info("start check movement...");
		    // checkBuLocation(modifyObj);
		    // resetLineReserve(modifyObj.getImMovementItems());
		    // modifyObj.setLastUpdateDate( new Date() );
		    // log.info("start update movement");

		    update(modifyObj);
		    log.info("update location success");
		} else {
		    update(modifyObj);
		}

		return modifyObj.getLocationId() + "存檔成功";
	    } else {
		throw new FormException("");
	    }
	} catch (FormException fe) {
	    log.error("地點維護單存檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("地點維護單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("地點維護單存檔時發生錯誤，原因：" + ex.getMessage());
	}

    }

    public String saveOrUpdateBuLocation(BuLocation location, String loginUser,
	    boolean isNew) throws FormException, Exception {
		try {
		    doBuLocationValidate(location);
		    location.setEnable(("N".equals(location.getEnable()) ? "N" : "Y"));
		    if (isNew) {
			BuLocation locationPO = buLocationDAO
				.findByLocationName(location.getLocationName());
			if (locationPO == null) {
			    insertBuLocation(location, loginUser);
			} else {
			    throw new ValidationErrorException("地點名稱："
				    + location.getLocationName() + "已經存在，請勿重複建立！");
			}
		    } else {
			modifyBuLocation(location, loginUser);
		    }
	
		    return "地點名稱：" + location.getLocationName() + "存檔成功！";
		} catch (FormException fe) {
		    log.error("地點資料存檔時發生錯誤，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    log.error("地點資料存檔時發生錯誤，原因：" + ex.toString());
		    throw new Exception("地點資料存檔時發生錯誤，原因：" + ex.getMessage());
		}
    }

    /**
     * 將調撥單主檔查詢結果存檔
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
     * 設定額外欄位
     * @param buLocations
     */
    private void setLineOtherColumn(List<BuLocation> buLocations){
    	for (BuLocation buLocation : buLocations) {
    		buLocation.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(buLocation.getLastUpdatedBy()));
		}
    }
    
    public void update(BuLocation updateObj) {
    	modifyLocation(updateObj);
    }
    
    /**
	 * 前端資料塞入bean
	 * @param parameterMap
	 * @return
	 */
	public void updateBuLocationBean(Map parameterMap)throws FormException, Exception {
		BuLocation buLocation = null;
		try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
//    	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
//    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    
            buLocation = (BuLocation)parameterMap.get("entityBean");
            
    	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buLocation);
    	    
    	    log.info("buLocation.getEnable"+ buLocation.getEnable());
    	    parameterMap.put("entityBean", buLocation);
        } catch (FormException fe) {
		    log.error("前端資料塞入bean失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
		    throw new Exception("地點資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}
    
	/**
	 * 地點存檔
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXBuLocation(Map parameterMap) throws Exception {

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		String resultMsg = null;
		Date date = new Date();
		try {
//		    Object formBindBean = parameterMap.get("vatBeanFormBind");
//		    Object formLinkBean = parameterMap.get("vatBeanFormLink");
		    Object otherBean = parameterMap.get("vatBeanOther");
	
		    String loginEmployeeCode = (String) PropertyUtils.getProperty(
					otherBean, "loginEmployeeCode");
		    
		    BuLocation buLocation = (BuLocation)parameterMap.get("entityBean");
		    
		    String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
		    
		    if(OrderStatus.FORM_SUBMIT.equals(formAction)){
		    	log.info("buLocation.getEnable() = " + buLocation.getEnable());
		    	buLocation.setEnable(("N".equals(buLocation.getEnable()) ? "Y" : "N"));
		    	buLocation.setLocationName(buLocation.getLocationName().trim().toUpperCase());
		    	buLocation.setZipCode(buLocation.getZipCode().trim());
		    	
		    	buLocation.setLastUpdatedBy(loginEmployeeCode);
		    	buLocation.setLastUpdateDate(date);
				
//		    	BuLocation buLocationUpdate = buLocationDAO.findByLocationName(buLocation.getLocationName());
		    	
				if( buLocation.getLocationId() == null ){
					buLocation.setCreatedBy(loginEmployeeCode);
			    	buLocation.setCreationDate(date);
					buLocationDAO.save(buLocation);
				}else{
					buLocationDAO.update(buLocation);
				}
		    }
		    
		    resultMsg = "Location Name：" + buLocation.getLocationName() + "存檔成功！ 是否繼續新增？";
		    
		    resultMap.put("resultMsg", resultMsg);
		    resultMap.put("entityBean", buLocation);
		    resultMap.put("vatMessage", msgBox);
		    return resultMap;
		    
		} catch (Exception ex) {
		    log.error("地點維護單存檔時發生錯誤，原因：" + ex.toString());
		    throw new Exception("地點維護單單存檔失敗，原因：" + ex.toString());
		}
	}
    
	/**
	 * 驗證主檔
	 * @param parameterMap
	 * @throws Exception
	 */
	public void validateHead(Map parameterMap) throws Exception {
		
		Object formBindBean = parameterMap.get("vatBeanFormBind");
//		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");
		
		String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
		Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
		
		String locationName = (String) PropertyUtils.getProperty(formBindBean, "locationName");
		String zipCode = (String) PropertyUtils.getProperty(formBindBean, "zipCode");
		
		log.info( "formId = " + formId );
		
		// 驗證地點名稱
		if(!StringUtils.hasText(locationName)){
			throw new ValidationErrorException("請輸入地點名稱！");
		}else{
			if( formId == null){
				BuLocation locationPO = buLocationDAO.findByLocationName(locationName.trim().toUpperCase());
				if (locationPO != null) {
				    throw new ValidationErrorException("地點名稱：" + locationName + "已經存在，請勿重複建立！");
				}
			}
		}
		
		// 驗證郵遞區號
		if(StringUtils.hasText(zipCode)){
			if (!ValidateUtil.isNumber(zipCode)) {
				throw new ValidationErrorException("郵遞區號必須為0~9數字！");
			}
		}
	}
	
}
