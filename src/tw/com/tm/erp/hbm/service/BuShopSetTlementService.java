package tw.com.tm.erp.hbm.service;

import java.rmi.NoSuchObjectException;
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
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopSetTlement;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.BuShopSetTlementDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;


public class BuShopSetTlementService {
    private static final Log log = LogFactory.getLog(BuShopSetTlementService.class);

    public static final String PROGRAM_ID= "BU_SHOP_SETTLEMENT";
    private BuShopSetTlementDAO buShopSetTlementDAO;
    private BuShopDAO buShopDAO;

    /**
     * 
     */
    public static final String[] GRID_FIELD_NAMES = { 
	"shopCode", "discountName",
	"discountRate", "setTlementRate",
	"lastUpdatedBy", "lastUpdateDate", "lineId" 
    };

    public static final int[] GRID_FIELD_TYPES = { 
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,
	AjaxUtils.FIELD_TYPE_LONG
    };

    public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
	"", "",  
	"0", "0",
	"", "",""
    };


    /**
     * PICKER
     */
    public static final String[] GRID_SEARCH_FIELD_NAMES = { 
	"shopCode", "discountName",
	"discountRate", "setTlementRate",
	"lastUpdatedByName", "lastUpdateDate", "lineId" 
    };

    public static final int[] GRID_SEARCH_FIELD_TYPES = { 
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, 
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,
	AjaxUtils.FIELD_TYPE_LONG
    };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
	"", "",  
	"0", "0",
	"", "", ""
    };

    public void setBuShopSetTlementDAO(BuShopSetTlementDAO buShopSetTlementDAO) {
        this.buShopSetTlementDAO = buShopSetTlementDAO;
    }

    public void setBuShopDAO(BuShopDAO buShopDAO) {
        this.buShopDAO = buShopDAO;
    }
    
    /**
     * 初始化 bean 額外顯示欄位
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
	log.info("========<executeInitial>========");
	Map resultMap = new HashMap(0);
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginUser = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginUser);
 
	    BuShopSetTlement buShopSetTlement = this.getActualBuShopSetTlement(otherBean, resultMap);
//	    BuShopSetTlement buShopSetTlement = new BuShopSetTlement();
//	    buShopSetTlement.setShopCode("");
//	    buShopSetTlement.setDiscountName("");
//	    buShopSetTlement.setDiscountRate(0.0D);
//	    buShopSetTlement.setSetTlementRate(0.0D);
//	    buShopSetTlement.setCreatedBy(loginUser);
//	    buShopSetTlement.setCreationDate(new Date());
//	    buShopSetTlement.setLastUpdatedBy(loginUser);
//	    buShopSetTlement.setLastUpdateDate(new Date());
	    
	    Map multiList = new HashMap(0);
	    resultMap.put("form", buShopSetTlement);
	    resultMap.put("createByName", employeeName);

	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("定變價單初始化失敗，原因：" + ex.toString());
	    throw new Exception("定變價單初始化失敗，原因：" + ex.toString());
	}
	
    }

    /**
     * 拆帳率查詢初始化
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

	    resultMap.put("brandCode", loginBrandCode);

	    return resultMap;
	}catch(Exception ex){
	    log.error("拆帳率查詢初始化失敗，原因：" + ex.toString());
	    throw new Exception("拆帳率查詢初始化失敗，原因：" + ex.toString());

	}
    }
    
    /**
     * 透過formId取得實際專櫃拆帳 in initial  
     * @param otherBean
     * @param resultMap
     * @return
     * @throws Exception
     */
    private BuShopSetTlement getActualBuShopSetTlement(Object otherBean, Map resultMap) throws Exception{
	BuShopSetTlement buShopSetTlement = null;
	try {
	    String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
	    Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;

	    buShopSetTlement = null == formId ? this.executeNew(otherBean) : this.findBuShopSetTlementById(formId);

	} catch (Exception e) {
	    log.error("取得實際專櫃拆帳主檔失敗,原因:"+e.toString());
	    throw new Exception("取得實際專櫃拆帳主檔失敗,原因:"+e.toString());
	}
	return buShopSetTlement;
    }
    
    /**
     * 產生新的專櫃拆帳
     * @param otherBean
     * @return
     * @throws Exception
     */
    public BuShopSetTlement executeNew(Object otherBean)throws Exception{
	BuShopSetTlement form = new BuShopSetTlement();
	try {
	    String loginUser = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");	    
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    
	    form.setShopCode("");
	    form.setDiscountName("");
	    form.setDiscountRate(0.0D);
	    form.setSetTlementRate(0.0D);
	    form.setBrandCode(loginBrandCode);
	    form.setCreatedBy(loginUser);
	    form.setCreationDate(new Date());
	    form.setLastUpdatedBy(loginUser);
	    form.setLastUpdateDate(new Date());
	} catch (Exception e) {
	    log.error("建立新專櫃拆帳主檔失敗,原因:"+e.toString());
	    throw new Exception("建立新專櫃拆帳主檔失敗,原因:"+e.toString());
	}
	return form;
    }
    
    /**
     * 取得專櫃拆帳的資料.
     * @param bean
     * @return
     * @throws FormException
     * @throws Exception
     */
    public BuShopSetTlement getBuShopSetTlement(Object bean)throws FormException,Exception{
	BuShopSetTlement buShopSetTlement = null;
	String lineId = (String)PropertyUtils.getProperty(bean, "lineId");
	if(StringUtils.hasText(lineId)){
	    buShopSetTlement = buShopSetTlementDAO.findById(NumberUtils.getLong(lineId));
	    if(buShopSetTlement == null){
		throw new NoSuchObjectException("查無專櫃拆帳主鍵"+lineId+"資料");
	    }
	}else{
	    throw new ValidationErrorException("傳入專櫃拆帳主鍵"+lineId+"為空值");    
	}

	return buShopSetTlement;
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

	    String shopCode = httpRequest.getProperty("shopCode");
	    String discountName = httpRequest.getProperty("discountName");
	    String brandCode = httpRequest.getProperty("brandCode");

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and model.shopCode = :shopCode", shopCode);
	    findObjs.put(" and model.discountName = :discountName", discountName);
	    findObjs.put(" and model.brandCode = :brandCode", brandCode);
	    Map buShopSettlementMap = buShopSetTlementDAO.search( "BuShopSetTlement as model", findObjs, " order by model.shopCode ", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	    List<BuShopSetTlement> buShopSettlements = (List<BuShopSetTlement>) buShopSettlementMap.get(BaseDAO.TABLE_LIST); 
	    if (buShopSettlements != null && buShopSettlements.size() > 0) {
		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		Long maxIndex = (Long)buShopSetTlementDAO.search("BuShopSetTlement as model", "count(model.lineId) as rowCount" ,findObjs, " order by model.shopCode ", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		// 設定其他欄位
		setOtherColumn(buShopSettlements);
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, buShopSettlements, gridDatas, firstIndex, maxIndex));
	    }else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }
	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的功能查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的功能查詢失敗！");
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
     * 將查詢結果存檔
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
     * 設定其他欄位
     * @param cmMovementHeads
     */
    private void setOtherColumn(List<BuShopSetTlement> buShopSettlements){
	for (BuShopSetTlement buShopSetTlement : buShopSettlements) {
	    buShopSetTlement.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(buShopSetTlement.getLastUpdatedBy()));
	}
    }
	
    /**
     * 依據primary key為查詢條件，取得專櫃拆帳檔資料
     * 
     * @param lineId
     * @return BuShopSetTlement
     * @throws Exception
     */
    public BuShopSetTlement findBuShopSetTlementById(Long lineId) throws Exception {
	try {
	    BuShopSetTlement buShopSetTlement = (BuShopSetTlement) buShopSetTlementDAO.findByPrimaryKey(BuShopSetTlement.class,
		    lineId);

	    return buShopSetTlement;
	} catch (Exception ex) {
	    log.error("依據主鍵：" + lineId + "查詢專櫃拆帳檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據主鍵：" + lineId + "查詢專櫃拆帳檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 前端資料塞入bean
     * @param parameterMap
     * @return
     */
    public void updateBuShopSetTlementBean(Map parameterMap)throws FormException, Exception {
	log.info("updateBuShopSetTlementBean");
	try{
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    //取得欲更新的bean
	    BuShopSetTlement buShopSetTlement = this.getActualBuShopSetTlement(otherBean,null); 
//	    BuShopSetTlement buShopSetTlement = new BuShopSetTlement();
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buShopSetTlement);

	    parameterMap.put("entityBean", buShopSetTlement);

	} catch (FormException fe) {
	    log.error("前端資料塞入bean失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
	    throw new Exception("專櫃拆帳存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 存取專櫃拆帳主檔
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map updateAJAXBuShopSetTlement(Map parameterMap)throws Exception{
	log.info( "執行updateAJAXBuShopSetTlement" );
	Map resultMap = new HashMap();
//	List errorMsgs = new ArrayList(0);
	String resultMsg = null;
	BuShopSetTlement buShopSetTlement = null;
	try {	    
	    Object otherBean = parameterMap.get("vatBeanOther");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");

//	    String lineIdStr = (String)PropertyUtils.getProperty(formLinkBean, "lineId");
	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
//	    String shopCode = (String)PropertyUtils.getProperty(formBindBean, "shopCode"); 
//	    String discountName = (String)PropertyUtils.getProperty(formBindBean, "discountName"); 
//	    String discountRateStr = (String)PropertyUtils.getProperty(formBindBean, "discountRate"); 
//	    String setTlementRateStr = (String)PropertyUtils.getProperty(formBindBean, "setTlementRate"); 
	    
	    buShopSetTlement = (BuShopSetTlement)parameterMap.get("entityBean");
//	    buShopSetTlement = this.getActualBuShopSetTlement(formLinkBean);

//	    log.info( "discountRate = " + discountRateStr + ", setTlementRate = " +setTlementRateStr+", lineIdStr =" +lineIdStr );
//	    log.info( "shopCode = " + shopCode + ", discountName = " +discountName );
	    
//	    log.info( "Double.parseDouble(discountRateStr) = " +Double.parseDouble(discountRateStr) + ", " +
//	    		"Double.parseDouble(setTlementRateStr) = " +Double.parseDouble(setTlementRateStr));
//	    if(lineIdStr!= null && !"".equals(lineIdStr)){
//		buShopSetTlement.setLineId(Long.parseLong(lineIdStr));
//	    }else{
//		buShopSetTlement.setLineId(null);
//	    }
//	    buShopSetTlement.setShopCode(shopCode);
//	    buShopSetTlement.setDiscountName(discountName);
//	    buShopSetTlement.setDiscountRate(Double.parseDouble(discountRateStr));
//	    buShopSetTlement.setSetTlementRate(Double.parseDouble(setTlementRateStr));

	    // 存檔
	    resultMsg = this.updateBuShopSetTlement(buShopSetTlement, employeeCode);
	    
//	    if( errorMsgs.size() > 0 ){
//		if( OrderStatus.FORM_SUBMIT.equals(formAction)){
//		    resultMsg = (String)errorMsgs.get(0);		    
//		}
//	    }else{
//	    }
	    
	    resultMap.put("entityBean", buShopSetTlement);
	    resultMap.put("resultMsg", resultMsg);
	} catch (Exception e) {
	    log.error("專櫃拆帳存檔時發生錯誤，原因：" + e.toString());
	    throw new Exception("專櫃拆帳存檔時發生錯誤，原因：" + e.getMessage());
	}
	return resultMap;
    }
    
    /**
     * 取得專櫃拆帳
     * @param bean
     * @return
     * @throws FormException
     * @throws Exception
     */
    public BuShopSetTlement getActualBuShopSetTlement(Object otherBean ) throws FormException, Exception{

	BuShopSetTlement buShopSetTlement = null;
	String id = (String)PropertyUtils.getProperty(otherBean, "lineId");

	if(StringUtils.hasText(id)){
	    Long headId = NumberUtils.getLong(id);
	    buShopSetTlement = this.findBuShopSetTlementById(headId);
	    if(buShopSetTlement  == null){
		throw new NoSuchObjectException("查無專櫃拆帳主鍵：" + headId + "的資料！");
	    }
	}else{
	    throw new ValidationErrorException("傳入的專櫃拆帳主鍵為空值！");
	}
	return buShopSetTlement;

    }
    
    /**
     * 判斷是否為新資料，並將專櫃拆帳資料新增或更新至專櫃拆帳主檔
     * 
     * @param buShopsetTlement
     * @param loginUser
     * @return String
     * @throws ValidationErrorException 
     */
    private String updateBuShopSetTlement(BuShopSetTlement buShopsetTlement, String loginUser){
	log.info("enter insertOrUpdateBuShopSetTlement===");
	UserUtils.setOpUserAndDate(buShopsetTlement, loginUser);
	String resultMsg = null;
	log.info("buShopsetTlement.getSetTlementRate() = " + buShopsetTlement.getSetTlementRate());
	if (buShopsetTlement.getLineId() == null) { 
//	    errorMsgs = checkPK(buShopsetTlement.getShopCode(), buShopsetTlement.getDiscountName());
//	    if(errorMsgs.size()>0){
//		return errorMsgs;
//	    }
	    buShopSetTlementDAO.save(buShopsetTlement);
	    resultMsg = "專櫃代號：" + buShopsetTlement.getShopCode() + "新增成功！"; 
	} else {
	    buShopSetTlementDAO.update(buShopsetTlement);
	    resultMsg = "專櫃代號：" + buShopsetTlement.getShopCode() + "更新成功！"; 
	}
//	errorMsgs.add("專櫃代號：" + buShopsetTlement.getShopCode().toUpperCase().trim() + "存檔成功！")
	return resultMsg;
    }
    
    /**
     * 只檢核專櫃拆帳主檔
     * @param 
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List checkBuShopSetTlement (Map parameterMap)throws FormException, Exception{
	log.info("checkBuShopSetTlement");
	List errorMsgs = new ArrayList(0);
	String message = "";
	Object otherBean = null;
	try{
	    Object formBean = parameterMap.get("vatBeanFormBind");
	    otherBean = parameterMap.get("vatBeanOther");
	    
	    String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
	    Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    
	    String shopCode = (String)PropertyUtils.getProperty(formBean, "shopCode"); 
	    String discountName = (String)PropertyUtils.getProperty(formBean, "discountName"); 
	    String discountRate = (String)PropertyUtils.getProperty(formBean, "discountRate"); 
	    String setTlementRate = (String)PropertyUtils.getProperty(formBean, "setTlementRate"); 
	    
	    shopCode = shopCode.toUpperCase().trim();
	    
	    if (!StringUtils.hasText(shopCode)) {
		message = "『專櫃代號』不可為空值!!";
		 errorMsgs.add(message+"\n");
	    }else{
		BuShop buShop = buShopDAO.findByBrandCodeAndShopCode(loginBrandCode,shopCode);
		if(buShop == null){
		    message = "『專櫃代號』不存在，請重新輸入!!";
		    errorMsgs.add(message+"\n");
		}else{
			log.info("getShopCode = "+buShop.getShopCode());
		    if (discountName == null || !StringUtils.hasText(discountName)) {
			message = "『中文名稱』不可為空值!!";
			errorMsgs.add(message+"\n");
		    }
		    if (discountRate.equals("0")) {
			message = "『折扣率』不可為0!!";
			errorMsgs.add(message+"\n");
		    }
		    if (setTlementRate.equals("0")) {
			message = "『拆帳率』不可為0!!";
			errorMsgs.add(message+"\n");
		    }	
		    
		    if(null == formId){
			String fieldNames[] = new String[]{"shopCode", "discountName"};
			Object fieldValue[] = new Object[]{shopCode, discountName};
			List chkList = buShopSetTlementDAO.findByProperty("BuShopSetTlement", fieldNames, fieldValue);
			 
			if(chkList != null && chkList.size()>0){
			    log.info("chkList.size()" + chkList.size());
			    message = "『專櫃代號』:" + shopCode + ",『中文名稱』:" + discountName + "，資料已存在，無法新增!!";
			    errorMsgs.add(message+"\n");
			}	
		    }
		}
	    }

	}catch (Exception ex) {
	    message = "專櫃拆帳檢核失敗，原因：" + ex.toString();
	    errorMsgs.add(message);
	    log.error(message);	
	}
	return errorMsgs;
    }

  //若為新增資料時，才需檢核「專櫃代號」及「中文名稱」是否已存在
    private List checkPK(String shopCode, String discountName){
	List errorMsgs = new ArrayList(0);
	String message = "";
	shopCode = shopCode.toUpperCase().trim();
	
	String fieldNames[] = new String[]{"shopCode", "discountName"};
	Object fieldValue[] = new Object[]{shopCode, discountName};
	List chkList = buShopSetTlementDAO.findByProperty("BuShopSetTlement", fieldNames, fieldValue);

	if(chkList != null && chkList.size()>0){
	    log.info("chkList.size()" + chkList.size());
	    message = "專櫃代號:" + shopCode + ",中文名稱:" + discountName + "，資料已存在，無法新增!!";
	    errorMsgs.add(message);
	}	
	return errorMsgs;
    }

}
