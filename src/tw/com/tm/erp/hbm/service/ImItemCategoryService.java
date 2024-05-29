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
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.BuExchangeRateId;
import tw.com.tm.erp.hbm.bean.BuItemCategoryPrivilege;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCategoryId;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployee;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class ImItemCategoryService {

    private static final Log log = LogFactory
	    .getLog(ImItemCategoryService.class);

    private ImItemCategoryDAO imItemCategoryDAO;
    private BuOrderTypeDAO buOrderTypeDAO;
    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    private BaseDAO baseDAO;
    private BuBrandDAO buBrandDAO;
    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
    }
    public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
    	this.imItemCategoryDAO = imItemCategoryDAO;
    }
    public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
    private BuBrandService buBrandService;
	  
    public void setBuBrandService(BuBrandService buBrandService) {
		    this.buBrandService = buBrandService;
	}
    
    public void setBuOrderTypeDAO(BuOrderTypeDAO buOrderTypeDAO) {
	    this.buOrderTypeDAO = buOrderTypeDAO;
    }
    public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
    /**
	 * 商品類別查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		 "categoryTypeName", "id.categoryCode", "categoryName",
		 "parentCategoryCode", "enableName", "lastUpdateDate"
	};
	
	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
		 "", "", "",
		 "", "", ""
	};
	 /**
	 * 商品類別查詢picker用的欄位
	 */
	 public static final String[] GRID_FIELD_NAMES = { "indexNo",
	    	"enable", "employeeCode", "employeeName" };

	    public static final String[] GRID_FIELD_DEFAULT_VALUES = { "", "N", "", "" };
	    
	    public static final int[] GRID_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_LONG,
		    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		    AjaxUtils.FIELD_TYPE_STRING};
    /**
	 * 查詢商品類別 初始化
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeSearchInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
	
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			
//			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			
			String attribute = null;
			if(loginBrandCode.indexOf("T2") > -1){
				attribute = "attribute2";
			}else{
				attribute = "attribute1";
			}
			
			List<BuCommonPhraseLine> allCategroyTypes = baseDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and " + attribute + " =?  and  enable = ?", new Object[]{"ItemCategory", "itemCategory", "Y"});
			
			Map multiList = new HashMap(0);
			multiList.put("allCategroyTypes"	, AjaxUtils.produceSelectorData(allCategroyTypes, "attribute3", "name", true, true));
			
			resultMap.put("multiList",multiList);
			resultMap.put("enable","Y");
			return resultMap;
		}catch(Exception ex){
			log.error("查詢進貨短溢調整單初始化失敗，原因：" + ex.toString());
			throw new Exception("查詢進貨短溢調整單初始化失敗，原因：" + ex.toString());
			
		}
	}
    
    public ImItemCategory findById(String brandCode, String categoryType,
	    String categoryCode) throws Exception {
	try {
	    ImItemCategoryId pk = new ImItemCategoryId(brandCode, categoryType,
		    categoryCode);
	    ImItemCategory imItemCategory = (ImItemCategory) imItemCategoryDAO
		    .findByPrimaryKey(ImItemCategory.class, pk);
	    return imItemCategory;
	} catch (Exception ex) {
	    log.error("依據品牌代號：" + brandCode + "、類別：" + categoryType + "、類別代號："
		    + categoryCode + "查詢商品類別資料檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌代號：" + brandCode + "、類別：" + categoryType
		    + "、類別代號：" + categoryCode + "查詢商品類別資料檔時發生錯誤，原因："
		    + ex.getMessage());
	}
    }

    public List<ImItemCategory> findMainCategoryByBrandCode(String brandCode,
	    String categoryType) {
	return imItemCategoryDAO.findMainCategoryByBrandCode(brandCode,
		categoryType);
    }

    public List<ImItemCategory> findByCategoryType(String brandCode,
	    String categoryType) {
	return imItemCategoryDAO.findByCategoryType(brandCode, categoryType);
    }
    
    public List<ImItemCategory> findByCategoryType(String brandCode,
    	    String categoryType, String order) {
    	return imItemCategoryDAO.findByCategoryType(brandCode, categoryType, order);
    }

    public List<ImItemCategory> findByParentCategroyCode(String brandCode,
	    String parentCategroyCode) {
	return imItemCategoryDAO.findByParentCategroyCode(brandCode,
		parentCategroyCode);
    }
    
    /**
     * 選取業種取得其業種子類
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> findAJAXItemSubcategory(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
	    try{
	    	String brandCode = httpRequest.getProperty("brandCode");
	    	String categoryType = httpRequest.getProperty("categoryType");
	    	
	    	List allItemSubcategory = imItemCategoryDAO.findByParentCategroyCode( brandCode, categoryType );
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
	 * ajax 取得商品類別 search分頁
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
	  	    
	  	    String categoryType = httpRequest.getProperty("categoryType");
	  	    String categoryCode = httpRequest.getProperty("categoryCode");
	  	    String categoryName = httpRequest.getProperty("categoryName");
	  	    String enable = httpRequest.getProperty("enable");
	  	    
	  	    HashMap map = new HashMap();
	  	    HashMap findObjs = new HashMap();
	  	    findObjs.put(" and model.id.brandCode = :brandCode",brandCode);
	  	    findObjs.put(" and model.id.categoryType = :categoryType",categoryType);
	  	    findObjs.put(" and model.id.categoryCode = :categoryCode",categoryCode);
	  	    findObjs.put(" and model.categoryName like :categoryName","%" + categoryName + "%");
	  	    findObjs.put(" and model.enable = :enable",enable);

	  	    //==============================================================	    
	  	    
	  	    Map imItemCategoryMap = imItemCategoryDAO.search( "ImItemCategory as model", findObjs, iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	  	    List<ImItemCategory> imItemCategorys = (List<ImItemCategory>) imItemCategoryMap.get(BaseDAO.TABLE_LIST); 

	  	    if (imItemCategorys != null && imItemCategorys.size() > 0) {
	  	    	
	  	    	this.setOtherName(brandCode, imItemCategorys);
	  	    	
	  	    	Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
	  	    	Long maxIndex = (Long)imItemCategoryDAO.search("ImItemCategory as model", "count(model.id.brandCode) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
	  	    	
	  	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,imItemCategorys, gridDatas, firstIndex, maxIndex));
	  	    }else {
	  	    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
	  	    }

	  	    return result;
	  	}catch(Exception ex){
	  	    log.error("載入頁面顯示的商品類別查詢發生錯誤，原因：" + ex.toString());
	  	    throw new Exception("載入頁面顯示的商品類別查詢失敗！");
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
			}//categoryResult
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
     * 判斷是否為新資料，並將商品類別資料新增或更新至商品類別資料檔
     * 
     * @param itemCategory
     * @param loginUser
     * @param isNew
     * @return String
     * @throws FormException
     * @throws Exception
     */
    public String saveOrUpdateImItemCategory(ImItemCategory itemCategory, String loginUser,
	    boolean isNew) throws FormException, Exception {
	try {
	    checkImItemCategory(itemCategory, loginUser);
	    ImItemCategoryId itemCategoryId = itemCategory.getId();
            String brandCode = itemCategoryId.getBrandCode();
            String categoryType = itemCategoryId.getCategoryType();
            String categoryCode = itemCategoryId.getCategoryCode();
//            categoryType = categoryType.toUpperCase();
            categoryCode = categoryCode.trim().toUpperCase();
            itemCategoryId.setCategoryType(categoryType);
            itemCategoryId.setCategoryCode(categoryCode);
        	   	
	    ImItemCategory itemCategoryPO = findById(brandCode, categoryType, categoryCode);	    
	    if (itemCategoryPO == null) {
		insertImItemCategory(itemCategory);
	    }else if (isNew) {
		throw new ValidationErrorException("品牌代號：" + brandCode + "、類別：" + categoryType + "、類別代號：" + categoryCode + "已經存在，請勿重覆建立！");
	    } else {
		BeanUtils.copyProperties(itemCategory, itemCategoryPO);
		modifyImItemCategory(itemCategoryPO);
	    }
	    
	    return "商品類別資料存檔成功！";
	} catch (FormException fe) {
	    log.error("商品類別資料存檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("商品類別資料存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("商品類別資料存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 將商品類別查詢結果存檔
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
        log.info("ssssssssssssssssss:"+httpRequest);
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
    /**
     * 設定其他欄位
     * @param imItemCategorys
     */
    private void setOtherName(String brandCode, List<ImItemCategory> imItemCategorys){
    	for (ImItemCategory imItemCategory : imItemCategorys) {
    		String enable = imItemCategory.getEnable();
    		String categoryType = imItemCategory.getId().getCategoryType();
    		if("Y".equals(enable)){
    			imItemCategory.setEnableName("啟用");
    		}else if("N".equals(enable)){
    			imItemCategory.setEnableName("停用");
    		}
    		
    		if(brandCode.indexOf("T2") <= -1){
    			BuCommonPhraseLine buCommonPhraseLine = (BuCommonPhraseLine)buCommonPhraseLineDAO.findFirstByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and attribute3 = ? and enable = ? and attribute1 = ?", new Object[]{"ItemCategory", categoryType, "Y", "itemCategory" });
    			if(buCommonPhraseLine != null){
    				imItemCategory.setCategoryTypeName(buCommonPhraseLine.getName());
    			}
    		}else{
    			BuCommonPhraseLine buCommonPhraseLine = (BuCommonPhraseLine)buCommonPhraseLineDAO.findFirstByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and attribute3 = ? and enable = ? and attribute2 = ?", new Object[]{"ItemCategory", categoryType, "Y", "itemCategory" });
    			if(buCommonPhraseLine != null){
    				imItemCategory.setCategoryTypeName(buCommonPhraseLine.getName());
    			}
    		}
    		
		}
    }
    
    /**
     * 新增至商品類別資料檔
     * 
     * @param saveObj
     */
    private void insertImItemCategory(Object saveObj) {
	imItemCategoryDAO.save(saveObj);
    }

    /**
     * 更新至商品類別資料檔
     * 
     * @param updateObj
     */
    private void modifyImItemCategory(Object updateObj) {
	imItemCategoryDAO.update(updateObj);
    }
    
   
    /**
     * 檢核商品類別資料
     * 
     * @param itemCategory
     * @param loginUser
     * @throws ValidationErrorException
     */
    private void checkImItemCategory(ImItemCategory itemCategory, String loginUser)
	    throws ValidationErrorException {

	ImItemCategoryId itemCategoryId = itemCategory.getId();
	if (!StringUtils.hasText(itemCategoryId.getCategoryType())) {
	    throw new ValidationErrorException("請選擇類別！");
	} else if (!StringUtils.hasText(itemCategoryId.getCategoryCode())) {
	    throw new ValidationErrorException("請輸入類別代號！");
	} else if (!StringUtils.hasText(itemCategory.getCategoryName())) {
	    throw new ValidationErrorException("請輸入類別名稱！");
	} else if (!StringUtils.hasText(itemCategory.getEnable())) {
	    throw new ValidationErrorException("請點選啟用狀態！");
	}
	
	UserUtils.setOpUserAndDate(itemCategory, loginUser);
    }
    
   
    /**
     * 依據商品類別資料查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return
     * @throws Exception
     */
    public List<ImItemCategory> findItemCategoryList(HashMap conditionMap) throws Exception{
	try{
	    String categoryType = (String) conditionMap.get("categoryType");
	    String categoryCode = (String) conditionMap.get("categoryCode");
	    String categoryName = (String) conditionMap.get("categoryName");
//	    conditionMap.put("categoryType", categoryType.trim().toUpperCase());
	    conditionMap.put("categoryCode", categoryCode.trim().toUpperCase());
	    conditionMap.put("categoryName", categoryName.trim().toUpperCase());
	    
	    return imItemCategoryDAO.findItemCategoryList(conditionMap);
	}catch (Exception ex) {
	    log.error("依據查詢螢幕查詢商品類別資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據查詢螢幕查詢商品類別資料時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 取得促銷活動代碼
     * @param lastNo
     * @return
     */
    public String generatePromotionCodeByDateSerial(String lastNo, String categoryCode) {
	String serialNo = "";
	int seq;
	String prefix = DateUtils.getCurrentDateStr("yyyyMM") + categoryCode;
	if (lastNo == null || "".equals(lastNo) || !prefix.equals(lastNo.substring(0, prefix.length()))) {
		serialNo = "001";
	} else {
		seq = Integer.parseInt(lastNo.substring(prefix.length())) + 1; // 
		serialNo = CommonUtils.insertCharacterWithFixLength(String.valueOf(seq), 3, "0");
	}

	return prefix + serialNo;
    }
    
    /**
     * 單據促銷自動跳號 = 年(4)月(2)業種子類(1)序號(3)
     * @param brandCode
     * @param categoryType
     * @param categoryCode
     * @return
     * @throws Exception
     */
    public synchronized String getOrderSerialNo(String brandCode,
	    String categoryType, String categoryCode) throws Exception {
	String currentNo = "unknow";
	String lastNo = "";
	ImItemCategory imItemCategory = findById(brandCode, categoryType, categoryCode);
	if (imItemCategory != null) {
	    lastNo = imItemCategory.getLastPromotionCode();
	    currentNo = generatePromotionCodeByDateSerial(lastNo, categoryCode);
	    imItemCategory.setLastPromotionCode(currentNo);
	    imItemCategoryDAO.update(imItemCategory);
	}
	return currentNo;
    }
    
    /**
     * 取得指定連動的類別下拉
     * @param httpRequest
     * @return
     * @throws Exception
     */
	public List<Properties> getAJAXCategory(Properties httpRequest)throws Exception{
		List list = new ArrayList();
		Properties properties = new Properties();
		try{
			String brandCode = httpRequest.getProperty("brandCode");
			String category = httpRequest.getProperty("category");
			String categoryType = httpRequest.getProperty("categoryType");
			
			log.info("brandCode = " + brandCode);
			log.info("category = " + category);
			log.info("categoryType = " + categoryType);
			
			List allCategory = imItemCategoryDAO.findByParentCategoryCode( brandCode, categoryType, category, "Y" );
			allCategory = AjaxUtils.produceSelectorData(allCategory, "categoryCode", "categoryName", true, true);
	    	properties.setProperty("allCategory", AjaxUtils.parseSelectorData(allCategory));
	    	
			list.add(properties);
		}catch(Exception e){
			log.error("取得指定連動的類別下拉，原因：" + e.toString());
			throw new Exception("取得指定連動的類別下拉，原因：" + e.getMessage());
		}
		return list;
	}
	
	/**
	 * 取得指定的類別名稱
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXImItemCategoryName(Properties httpRequest)throws Exception{
	    List list = new ArrayList();
	    Properties properties = new Properties();
	    try{
		String brandCode = httpRequest.getProperty("brandCode");
		String categoryType = httpRequest.getProperty("categoryType");
		String categoryCode = httpRequest.getProperty("categoryCode");

		log.info("brandCode = " + brandCode);
		log.info("categoryType = " + categoryType);
		log.info("categoryCode = " + categoryCode);
		String categoryName = null;

		if(StringUtils.hasText(categoryCode)){
		    ImItemCategory category = imItemCategoryDAO.findByCategoryCode(brandCode, categoryType, categoryCode, "Y" );;
		    if(category != null){
			categoryName = category.getCategoryName();
		    }else{
			categoryName = "查無此類別";
		    }
		}else{
		    categoryName = "";
		}

		properties.setProperty("categoryName", categoryName);
		list.add(properties);
	    }catch(Exception e){
		log.error("取得指定的類別名稱發生問題，原因：" + e.toString());
		throw new Exception("取得指定的類別名稱發生問題，原因：" + e.getMessage());
	    }
	    return list;
	}	
	
	
	/**
     * 取得指定的類別名稱
     * @param httpRequest
     * @return
     * @throws Exception
     */
	public List<Properties> getAJAXCategoryName(Properties httpRequest)throws Exception{
		List list = new ArrayList();
		Properties properties = new Properties();
		try{
			String brandCode = httpRequest.getProperty("brandCode");
			String categoryType = httpRequest.getProperty("categoryType");
			String categoryCode = httpRequest.getProperty("categoryCode");
			
			log.info("brandCode = " + brandCode);
			log.info("categoryType = " + categoryType);
			log.info("categoryCode = " + categoryCode);
			String categoryName = null;
			
			if(StringUtils.hasText(categoryCode)){
				ImItemCategory category = imItemCategoryDAO.findByCategoryCode(brandCode, categoryType, categoryCode, "Y" );;
				if(category != null){
					categoryName = category.getCategoryName();
				}else{
					categoryName = "查無此類別";
				}
			}else{
				categoryName = "";
			}
			
			properties.setProperty("categoryName", categoryName);
			list.add(properties);
		}catch(Exception e){
			log.error("取得指定的類別名稱發生問題，原因：" + e.toString());
			throw new Exception("取得指定的類別名稱發生問題，原因：" + e.getMessage());
		}
		return list;
	}
	
	  public List<Properties> getAJAXItemCategoryRelatedList(Map parameterMap) throws Exception{
			log.info("getItemCategoryRelatedList....");
			Map returnMap = new HashMap(0);  
			
			try{
				Object otherBean  = parameterMap.get("vatBeanOther"); 
				String brandCode  = (String)PropertyUtils.getProperty(otherBean, "brandCode");
				log.info("brandCode:"+ brandCode);
				String category01Value = (String)PropertyUtils.getProperty(otherBean, "category01");
				String category02Value = (String)PropertyUtils.getProperty(otherBean, "category02");
				
				Map categoryMap = this.getItemCategoryRelatedList(brandCode, category01Value, category02Value);
				
				returnMap.put("allCategory01", AjaxUtils.produceSelectorData((List<ImItemCategory>)categoryMap.get("allCategory01") ,"categoryCode" ,"categoryName",  true,  true));
				returnMap.put("allCategory02", AjaxUtils.produceSelectorData((List<ImItemCategory>)categoryMap.get("allCategory02"),"categoryCode" ,"categoryName",  true,  true));
				returnMap.put("allCategory03", AjaxUtils.produceSelectorData((List<ImItemCategory>)categoryMap.get("allCategory03") ,"categoryCode" ,"categoryName",  true,  true));
				returnMap.put("allCategory07", AjaxUtils.produceSelectorData((List<ImItemCategory>)categoryMap.get("allCategory07") ,"categoryCode" ,"categoryName",  true,  true));
				returnMap.put("allCategory09", AjaxUtils.produceSelectorData((List<ImItemCategory>)categoryMap.get("allCategory09") ,"categoryCode" ,"categoryName",  true,  true));
				returnMap.put("allCategory13", AjaxUtils.produceSelectorData((List<ImItemCategory>)categoryMap.get("allCategory13"),"categoryCode" ,"categoryName",  true,  true));	
				
				return AjaxUtils.parseReturnDataToJSON(returnMap);
			}catch(IllegalAccessException iae){
				System.out.println(iae.getMessage());
				throw new IllegalAccessException(iae.getMessage());
			}catch(InvocationTargetException ite){
				System.out.println(ite.getMessage());
				throw new InvocationTargetException(ite, ite.getMessage());
			}catch(NoSuchMethodException nse){
				System.out.println(nse.getMessage());
				throw new NoSuchMethodException(nse.getMessage());
			}
		}
	  
	  
	  public Map getItemCategoryRelatedList( String brandCode, String category01Value, String category02Value) throws Exception{
			log.info("getItemCategoryRelatedList....");
			Map resultMap = new HashMap(0);
			try{
				List<ImItemCategory> allCategory07 = new ArrayList();
				List<ImItemCategory> allCategory09 = new ArrayList();
				List<ImItemCategory> allCategory13 = new ArrayList();
				List<ImItemCategory> allCategory02 = new ArrayList();
				List<ImItemCategory> allCategory03 = new ArrayList();
				List<ImItemCategory> allCategory01 = imItemCategoryDAO.findByParentCategroyCode(brandCode, imItemCategoryDAO.CATEGORY01, null);
				if(StringUtils.hasText(category01Value))
					allCategory02 = imItemCategoryDAO.findByParentCategroyCode(brandCode, imItemCategoryDAO.CATEGORY02,category01Value);
				if(StringUtils.hasText(category02Value))
					allCategory03 = imItemCategoryDAO.findByParentCategroyCode(brandCode, imItemCategoryDAO.CATEGORY03,category02Value);
				 allCategory07 = imItemCategoryDAO.findByParentCategroyCode(brandCode, imItemCategoryDAO.CATEGORY07, null);
				 allCategory09 = imItemCategoryDAO.findByParentCategroyCode(brandCode, imItemCategoryDAO.CATEGORY09,null);
				 allCategory13 = imItemCategoryDAO.findByParentCategroyCode(brandCode, imItemCategoryDAO.CATEGORY13,null);
				
				resultMap.put("allCategory01", allCategory01);
				resultMap.put("allCategory02", allCategory02);
				resultMap.put("allCategory03", allCategory03);
				resultMap.put("allCategory07", allCategory07);
				resultMap.put("allCategory09", allCategory09);
				resultMap.put("allCategory13", allCategory13);
		
				
				return resultMap;
			
			}catch(Exception ex){
				System.out.println(ex.getMessage());
				throw new NoSuchMethodException(ex.getMessage());
			}
		}
	  public Map getItemCategoryRelatedList1( String brandCode, String category01Value, String category02Value) throws Exception{
			log.info("getItemCategoryRelatedList....");
			Map resultMap = new HashMap(0);
			try{
				List<ImItemCategory> allCategory07 = imItemCategoryDAO.findByParentCategroyCode(brandCode, imItemCategoryDAO.CATEGORY07, null);
				List<ImItemCategory> allCategory09 = imItemCategoryDAO.findByParentCategroyCode(brandCode, imItemCategoryDAO.CATEGORY09,null);
				List<ImItemCategory> allCategory13 = imItemCategoryDAO.findByCategoryTypeOrder(brandCode, imItemCategoryDAO.CATEGORY13,"categoryName");
				List<ImItemCategory> allCategory02 = imItemCategoryDAO.findByParentCategroyCode(brandCode, imItemCategoryDAO.CATEGORY02,null);
				List<ImItemCategory> allCategory03 = imItemCategoryDAO.findByParentCategroyCode(brandCode, imItemCategoryDAO.CATEGORY03,null);
				List<ImItemCategory> allCategory01 = imItemCategoryDAO.findByParentCategroyCode(brandCode, imItemCategoryDAO.CATEGORY01, null);

				resultMap.put("allCategory01", allCategory01);
				resultMap.put("allCategory02", allCategory02);
				resultMap.put("allCategory03", allCategory03);
				resultMap.put("allCategory07", allCategory07);
				resultMap.put("allCategory09", allCategory09);
				resultMap.put("allCategory13", allCategory13);
		
				
				return resultMap;
			
			}catch(Exception ex){
				System.out.println(ex.getMessage());
				throw new NoSuchMethodException(ex.getMessage());
			}
		}
	  //LoadBeforeAjax BuItemCategory
	  public List<Properties> getAJAXSearchCPageData(Properties httpRequest) throws Exception{

		  	try{
		  	    List<Properties> result = new ArrayList();
		  	    List<Properties> gridDatas = new ArrayList();
		  	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		  	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
		  	  
		  	    //======================帶入Head的值=========================
		  	    
		  	  //  String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
		  	    
		  	    String categoryType = httpRequest.getProperty("categoryType");
		  	    String itemCategory = httpRequest.getProperty("itemCategory");
		  	    String employeeDepartment = httpRequest.getProperty("employeeDepartment");
		  	    String employeePosition = httpRequest.getProperty("employeePosition");
		  	    
		  	    HashMap map = new HashMap();
		  	    HashMap findObjs = new HashMap();
		  	    findObjs.put(" and model.id.categoryType = :categoryType",categoryType);
		  	    findObjs.put(" and model.id.itemCategory = :itemCategory",itemCategory);
		  	    findObjs.put(" and model.id.employeeDepartment = :employeeDepartment",employeeDepartment);
		  	    findObjs.put(" and model.employeePosition like :employeePosition","%" + employeePosition + "%");
		  	    

		  	    //==============================================================	    
		  	    
		  	    Map buItemCategoryMap = imItemCategoryDAO.search( "BuItemCategoryPrivilege as model", findObjs, iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
		  	    List<BuItemCategoryPrivilege> buItemCategorys = (List<BuItemCategoryPrivilege>) buItemCategoryMap.get(BaseDAO.TABLE_LIST); 

		  	    if (buItemCategorys != null && buItemCategorys.size() > 0) {
		  	    	
		  	    //	this.setOtherName(brandCode, imItemCategorys);
		  	    	
		  	    	Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		  	    	Long maxIndex = (Long)imItemCategoryDAO.search("BuItemCategoryPrivilege as model", "count(model.id.employeeCode) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		  	    	
		  	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,buItemCategorys, gridDatas, firstIndex, maxIndex));
		  	    }else {
		  	    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,gridDatas));
		  	    }

		  	    return result;
		  	}catch(Exception ex){
		  	    log.error("載入頁面顯示的商品類別查詢發生錯誤，原因：" + ex.toString());
		  	    throw new Exception("載入頁面顯示的商品類別查詢失敗！");
		  	}	
		}
	  public List<Properties> getAJAXSearchCatePageData(Properties httpRequest) throws Exception{

		  	try{
		  	    List<Properties> result = new ArrayList();
		  	    List<Properties> gridDatas = new ArrayList();
		  	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		  	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
		  	  
		  	    //======================帶入Head的值=========================
		  	    
		  	    String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
		  	    
		  	    String categoryType = httpRequest.getProperty("categoryType");
		  	    String categoryCode = httpRequest.getProperty("categoryCode");
		  	    String categoryName = httpRequest.getProperty("categoryName");
		  	    String enable = httpRequest.getProperty("enable");
		  	    
		  	    HashMap map = new HashMap();
		  	    HashMap findObjs = new HashMap();
		  	    findObjs.put(" and model.id.brandCode = :brandCode",brandCode);
		  	    findObjs.put(" and model.id.categoryType = :categoryType",categoryType);
		  	    findObjs.put(" and model.id.categoryCode = :categoryCode",categoryCode);
		  	    findObjs.put(" and model.id.categoryName like :categoryName","%" + categoryName + "%");
		  	    findObjs.put(" and model.enable = :enable",enable);

		  	    //==============================================================	    
		  	    
		  	    Map imItemCategoryMap = imItemCategoryDAO.search( "ImItemCategory as model", findObjs, iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
		  	    List<ImItemCategory> imItemCategorys = (List<ImItemCategory>) imItemCategoryMap.get(BaseDAO.TABLE_LIST); 

		  	    if (imItemCategorys != null && imItemCategorys.size() > 0) {
		  	    	
		  	    	this.setOtherName(brandCode, imItemCategorys);
		  	    	
		  	    	Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		  	    	Long maxIndex = (Long)imItemCategoryDAO.search("ImItemCategory as model", "count(model.id.brandCode) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		  	    	
		  	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,imItemCategorys, gridDatas, firstIndex, maxIndex));
		  	    }else {
		  	    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,gridDatas));
		  	    }
		  	  String hql2 = "SELECT BRAND_CODE,BRAND_NAME,SUM(ENABLE) AS ENABLE FROM (SELECT BRAND_CODE, BRAND_NAME,0 AS ENABLE FROM ERP.BU_BRAND WHERE ENABLE = 'Y' UNION SELECT B.BRAND_CODE, B.BRAND_NAME,1 AS BRAND_NAME FROM ERP.BU_BRAND B INNER JOIN ERP.BU_EMPLOYEE_BRAND E ON B.BRAND_CODE = E.BRAND_CODE WHERE EMPLOYEE_CODE = 'T49674' AND B.ENABLE = 'Y' ) A GROUP BY BRAND_CODE,BRAND_NAME ORDER BY BRAND_CODE";
		  	    return result;
		  	}catch(Exception ex){
		  	    log.error("載入頁面顯示的商品類別查詢發生錯誤，原因：" + ex.toString());
		  	    throw new Exception("載入頁面顯示的商品類別查詢失敗！");
		  	}	
		}
	  public static final String[] GRID_NEW_PRIVILEGE_FIELD_NAMES = { 
			"indexNo", "enable", "categoryName",
		};
		
		public static final String[] GRID_NEW_PRIVILEGE_FIELD_DEFAULT_VALUES = {
		  	"0", "N", "", ""
		};
	
		//----201404
		public Map executeInitial(Map parameterMap) throws Exception {
			Object otherBean = parameterMap.get("vatBeanOther");
			Map resultMap = new HashMap(0);
			String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			try {
				String attribute = null;
				if(loginBrandCode.indexOf("T2") > -1){
					attribute = "attribute2";
				}else{
					attribute = "attribute1";
				}
				ImItemCategory imItemCategory = this.executeFindActualImItemCategory(parameterMap);
				List<BuCommonPhraseLine> allCategroyTypes = baseDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and " + attribute + " =?  and  enable = ?", new Object[]{"ItemCategory", "itemCategory", "Y"});
				Map multiList = new HashMap(0);
				List allBrand = buBrandDAO.findBrandByEnable("Y");
				multiList.put("allBrand", AjaxUtils.produceSelectorData(allBrand, "brandCode", "brandName", false, false));
				multiList.put("allCategroyTypes"	, AjaxUtils.produceSelectorData(allCategroyTypes, "attribute3", "name", true, true,"ITEM_CATEGORY"));
				resultMap.put("multiList",multiList);
				resultMap.put("enable","Y");
			/*	//發票種類
				List allInvoiceType = buCommonPhraseLineDAO.findEnableLineById("InvoiceType");
				multiList.put("allInvoiceType", AjaxUtils.produceSelectorData(allInvoiceType, "lineCode", "name", true, true));*/
				
				resultMap.put("form", imItemCategory);
				resultMap.put("multiList", multiList);
	

				
				return resultMap;

			} catch (Exception ex) {
				log.error("初始化失敗，原因：" + ex.toString());
				throw new Exception("初始化失敗，原因：" + ex.toString());

			}
		}
		
		
		 public ImItemCategory executeFindActualImItemCategory(Map parameterMap)
		 throws FormException, Exception {
			 
			 Object otherBean = parameterMap.get("vatBeanOther");

			 ImItemCategory imItemCategory = null;
			 ImItemCategoryId ids = null;
			 try {
			//	 String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
				 String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
				 String categoryType =(String)PropertyUtils.getProperty(otherBean, "categoryType");
				 String categoryCode =(String)PropertyUtils.getProperty(otherBean, "categoryCode");
				 System.out.println("executeFindActuaImItembrandCode:"+brandCode);
				 System.out.println("executeFindActuaImItemcategoryType:"+categoryType);
				 System.out.println("executeFindActuaImItemcategoryCode:"+categoryCode);
		
				 
				 
				 if( StringUtils.hasText(brandCode) && StringUtils.hasText(categoryType)
						 && StringUtils.hasText(categoryCode) )
					 ids= new ImItemCategoryId(brandCode,categoryType,categoryCode);
				 System.out.println("ImItemCategoryId===========:"+ids);
				 imItemCategory = null == ids ? this.executeNew(): this.findById(brandCode,categoryType,categoryCode);

				 parameterMap.put( "entityBean", imItemCategory);
				 return imItemCategory;
			 } catch (Exception e) {
				 log.error("取得實際匯率主檔失敗,原因:"+e.toString());
				 throw new Exception("取得實際匯率主檔失敗,原因:"+e.toString());
			 }
		 }
		 private ImItemCategory executeNew() throws Exception {
				log.info("newImItemCategory");
				ImItemCategory form = new ImItemCategory();
		
				form.setId(null);
		//		form.setBrandCode(null);
		//		form.setCategoryCode(null);
		//		form.setCategoryType(null);
				form.setCategoryTypeName("");
				form.setCategoryName("");
				form.setEnable("Y");
				form.setCreationDate(new Date());
				return form;
			}
		 public ImItemCategory findImItemCategoryById(ImItemCategoryId imItemCategoryId) throws Exception {
			 System.out.println("findimItemCategoryId");
			 try {
				 ImItemCategory imItemCategory = (ImItemCategory) imItemCategoryDAO.findByPrimaryKey(ImItemCategory.class, imItemCategoryId);
				 return imItemCategory;
			 } catch (Exception ex) {
				 log.error("依據主鍵查詢匯率資料時發生錯誤，原因：" + ex.toString());
				 throw new Exception("依據主鍵查詢匯率資料時發生錯誤，原因：" + ex.getMessage());
			 }
		 }
		 /**
			 * 前端資料塞入bean
			 * 
			 * @param parameterMap
			 * @return
			 */
			public void updateImItemCategoryBean(Map parameterMap)
					throws FormException, Exception {
				// TODO Auto-generated method stub

			//	ImItemCategory imItemCategory = null;
				try {
					Object formBindBean = parameterMap.get("vatBeanFormBind");
					// Object formLinkBean = parameterMap.get("vatBeanFormLink");
					// Object otherBean = parameterMap.get("vatBeanOther");
					String brandCode = (String) PropertyUtils.getProperty(formBindBean, "brandCode");
					 String categoryCode = (String) PropertyUtils.getProperty(formBindBean, "categoryCode");
					 String categoryType = (String) PropertyUtils.getProperty(formBindBean, "categoryType");
					 ImItemCategoryId exItemCategoryId = new ImItemCategoryId(brandCode,categoryCode,categoryType);

					 ImItemCategory imItemCategory = this.findImItemCategoryById(exItemCategoryId);
					 if(imItemCategory == null) imItemCategory = new ImItemCategory();
			//		imItemCategory = (ImItemCategory) parameterMap.get("entityBean");
					log.info("前端塞入"+imItemCategory);
					AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imItemCategory);
					
					parameterMap.put("entityBean", imItemCategory);
				} /*catch (FormException fe) {
					log.error("前端資料塞入bean失敗，原因：" + fe.toString());
					throw new FormException(fe.getMessage());
				} */catch (Exception ex) {
					log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
					throw new Exception("資料塞入bean發生錯誤，原因：" + ex.getMessage());
				}

			}
			/**
			 * 存檔
			 * 
			 * @param parameterMap
			 * @return
			 */
		    public Map updateAJAXImItemCategory(Map parameterMap) throws Exception {
		    	System.out.println("======異動店別單頭資料=========");
				MessageBox msgBox = new MessageBox();
				Map resultMap = new HashMap(0);
				String resultMsg = null;
			try {
			    Object otherBean = parameterMap.get("vatBeanOther");
			    
			    String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			    
			    ImItemCategory imItemCategory = (ImItemCategory)parameterMap.get("entityBean");
		//	  String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
			    String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			    log.info("enableImItemCategory"+imItemCategory.getEnable());
			    ImItemCategoryId id = new ImItemCategoryId(/*imItemCategory.getBrandCode(),imItemCategory.getCategoryCode(),imItemCategory.getCategoryType()*/);
			    ImItemCategory imItemCategoryIdUpdate = this.findImItemCategoryById(id);
			    log.info("imItemCategoryIdUpdate==="+imItemCategoryIdUpdate);
			    if(OrderStatus.FORM_SUBMIT.equals(formAction))
			    {
			    	imItemCategory.setId(id);
			    	imItemCategory.setCreatedBy(loginEmployeeCode);
			    	imItemCategory.setCreationDate(new Date());
			    	imItemCategory.setLastUpdatedBy(loginEmployeeCode);
			    	imItemCategory.setLastUpdateDate(new Date());
			    	imItemCategory.setEnable("Y".equals(imItemCategory.getEnable())?"N":"Y");
			    	//"Y".equals(imItemCategory.getEnable())?"N":"Y"
			    	System.out.println("======準備異動店別單頭資料=========:");
				    	if(null == imItemCategoryIdUpdate  )
				    	{
				    		imItemCategory.setCreatedBy(loginEmployeeCode);
				    		imItemCategory.setCreationDate(new Date());
				    		imItemCategoryDAO.save(imItemCategory);
				    	}
				    	else
				    	{
				    		imItemCategoryDAO.update(imItemCategory);
				    	}	    		
			    	
			    }
			    
		    	resultMsg = "imItemCategory" +  "存檔成功！ 是否繼續新增？";
		    	
			    resultMap.put("resultMsg", resultMsg);
			    resultMap.put("entityBean", imItemCategory);
			    resultMap.put("vatMessage", msgBox);
			    
			    return resultMap;

			   
			} catch (Exception ex) {
			    log.error("主檔維護作業存檔時發生錯誤，原因：" + ex.toString());
			    throw new Exception(ex.getMessage());
			}
		    }

		  
}