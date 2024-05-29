package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceLine;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.bean.ImItemPriceView;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImPriceAdjustment;
import tw.com.tm.erp.hbm.bean.ImPriceList;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployee;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployeeId;
import tw.com.tm.erp.hbm.bean.ImWarehouseType;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuLocationDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseEmployeeDAO;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class ImWarehouseService {

    private static final Log log = LogFactory.getLog(ImWarehouseService.class);

    private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
    private ImWarehouseDAO imWarehouseDAO;
    private ImWarehouseEmployeeDAO imWarehouseEmployeeDAO;
    private BuLocationDAO buLocationDAO;
    private BaseDAO baseDAO;
    private BuBrandDAO buBrandDAO;

    private ImWarehouseTypeService imWarehouseTypeService;
    private BuLocationService buLocationService;

    private final static String SAVE = "SAVE";
    private final static String UPDATE = "UPDATE";

    public void setBuLocationDAO(BuLocationDAO buLocationDAO) {
	this.buLocationDAO = buLocationDAO;
    }

    public void setBuEmployeeWithAddressViewDAO(
	    BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
	this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
    }

    public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
	this.imWarehouseDAO = imWarehouseDAO;
    }

    public void setBaseDAO(BaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
	this.buBrandDAO = buBrandDAO;
    }

    public void setImWarehouseEmployeeDAO(
	    ImWarehouseEmployeeDAO imWarehouseEmployeeDAO) {
	this.imWarehouseEmployeeDAO = imWarehouseEmployeeDAO;
    }

    public void setImWarehouseTypeService(
	    ImWarehouseTypeService imWarehouseTypeService) {
	this.imWarehouseTypeService = imWarehouseTypeService;
    }

    public void setBuLocationService(BuLocationService buLocationService) {
	this.buLocationService = buLocationService;
    }

    /**
     * 倉庫查詢picker用的欄位
     */
    public static final String[] GRID_SEARCH_FIELD_NAMES = {"warehouseId", "warehouseCode",
	    "warehouseName", "warehouseTypeName", "storage", "storageArea",
	    "storageBin", "categoryName", "locationName", "taxTypeName" };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "", "",
	    "", "", "", "", "", "", "" };

    /**
     * 查詢倉庫初始化
     * 
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception {
	Map resultMap = new HashMap(0);

	try {
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String) PropertyUtils.getProperty(
		    otherBean, "loginBrandCode");
	    String storage = (String) PropertyUtils.getProperty(
			    otherBean, "customsWarehouseCode");
	    List<ImWarehouseType> allImWarehouseTypes = imWarehouseTypeService.findAll();
	    List<BuLocation> allBuLocations = buLocationService.findAll();
	    log.info(" 1allBuLocations.size()  = " + allBuLocations.size());

	    Map multiList = new HashMap(0);
	    multiList.put("allImWarehouseTypes", AjaxUtils.produceSelectorData(allImWarehouseTypes, "warehouseTypeId", "name",false, true));
	    multiList.put("allBuLocations", AjaxUtils.produceSelectorData(allBuLocations, "locationId", "locationName", false, true));

	    resultMap.put("brandCode", loginBrandCode);
	    resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());
	    resultMap.put("storage", storage);
	    resultMap.put("multiList", multiList);
	    return resultMap;
	} catch (Exception ex) {
	    log.error("查詢倉庫初始化失敗，原因：" + ex.toString());
	    throw new Exception("查詢倉庫初始化失敗，原因：" + ex.toString());

	}
    }

    /**
     * ajax 取得倉庫search分頁
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPageData(Properties httpRequest)
	    throws Exception {
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    // ======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號

	    String warehouseCode = httpRequest.getProperty("warehouseCode");// 倉庫代碼
	    String enable = httpRequest.getProperty("enable");
	    
	    System.out.println("Y".equals(enable));
	    
	    //String trueEnable = ("Y".equals(enable)? "N" : "Y");
	    enable = ("Y".equals(enable)? "N" : "Y");
	    
	    log.info("getAJAXSearchPageData----enable = " + enable);
	    log.info("getAJAXSearchPageData----httpRequest = " + httpRequest);
	    String warehouseName = httpRequest.getProperty("warehouseName");
	    String storage = httpRequest.getProperty("storage");
	    String storageArea = httpRequest.getProperty("storageArea");
	    String storageBin = httpRequest.getProperty("storageBin");
	    String warehouseTypeId = httpRequest.getProperty("warehouseTypeId");
	    String categoryCode = httpRequest.getProperty("categoryCode");
	    String locationId = httpRequest.getProperty("locationId");
	    String taxTypeCode = httpRequest.getProperty("taxTypeCode");
	    String warehouseManager = httpRequest.getProperty("warehouseManager");

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    log.info("tttttttttttttttttttttttttt");	    
	    // ==============================================================
	    findObjs.put(" and model.brandCode = :brandCode", brandCode);
	    findObjs.put(" and model.warehouseCode like :warehouseCode", "%" + warehouseCode.toUpperCase() + "%");
	    findObjs.put(" and model.warehouseName like :warehouseName", "%"+ warehouseName.toUpperCase() + "%");
	    findObjs.put(" and model.storage = :storage", storage.toUpperCase());
	    findObjs.put(" and model.storageArea = :storageArea", storageArea.toUpperCase());
	    findObjs.put(" and model.storageBin = :storageBin", storageBin.toUpperCase());
	    findObjs.put(" and model.warehouseManager = :warehouseManager",warehouseManager.toUpperCase());
	    findObjs.put(" and model.warehouseTypeId = :warehouseTypeId",warehouseTypeId);
	    findObjs.put(" and model.categoryCode = :categoryCode",categoryCode);
	    findObjs.put(" and model.locationId = :locationId", locationId);
	    findObjs.put(" and model.taxTypeCode = :taxTypeCode", taxTypeCode);
	    findObjs.put(" and model.enable = :enable", enable);
	    // ==============================================================
	    
	    Map imWarehouseMap = imWarehouseDAO.search("ImWarehouse as model",findObjs, "order by warehouseCode asc", iSPage, iPSize,BaseDAO.QUERY_SELECT_RANGE);
	    log.info("ooooooooooooooooooooooooooo");
	    List<ImWarehouse> imWarehouses = (List<ImWarehouse>) imWarehouseMap.get(BaseDAO.TABLE_LIST);

	    
	    if (imWarehouses != null && imWarehouses.size() > 0) {
	    	log.info("aaaaaaaaaaaaaaaaaaaaaa");
		this.setOtherColumns(imWarehouses);

		Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
		Long maxIndex = (Long) imWarehouseDAO.search(
			"ImWarehouse as model",
			"count(model.warehouseCode) as rowCount", findObjs,
			"order by warehouseCode desc",
			BaseDAO.QUERY_RECORD_COUNT).get(
			BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

		result.add(AjaxUtils.getAJAXPageData(httpRequest,
			GRID_SEARCH_FIELD_NAMES,
			GRID_SEARCH_FIELD_DEFAULT_VALUES, imWarehouses,
			gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
			GRID_SEARCH_FIELD_NAMES,
			GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的倉庫查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的倉庫查詢失敗！");
	}
    }

    /**
     * ajax picker按檢視返回選取的資料
     * 
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List<Properties> getSearchSelection(Map parameterMap)
	    throws FormException, Exception {
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try {
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    String timeScope = (String) PropertyUtils.getProperty(pickerBean,
		    AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(
		    pickerBean, AjaxUtils.SEARCH_KEY);

	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
		    searchKeys);
	    System.out.println("size:::" + result.size());
	    if (result.size() > 0) {
    	pickerResult.put("result", result);
	    }
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel", new String[] { "vatBeanPicker" });
	} catch (Exception ex) {
	    log.error("檢視失敗，原因：" + ex.toString());
	    Map messageMap = new HashMap();
	    messageMap.put("type", "ALERT");
	    messageMap.put("message", "檢視失敗，原因：" + ex.toString());
	    messageMap.put("event1", null);
	    messageMap.put("event2", null);
	    resultMap.put("vatMessage", messageMap);

	}
	return AjaxUtils.parseReturnDataToJSON(resultMap);
    }

    /**
     * 設定額外欄位
     * 
     * @param imWarehouses
     */
    private void setOtherColumns(List<ImWarehouse> imWarehouses) {
	for (ImWarehouse imWarehouse : imWarehouses) {
	    Long warehouseTypeId = imWarehouse.getWarehouseTypeId(); // 倉庫類型
	    String categoryCode = imWarehouse.getCategoryCode(); // 型態
	    Long locationId = imWarehouse.getLocationId(); // 地點
	    String taxTypeCode = imWarehouse.getTaxTypeCode(); // 稅別

	    imWarehouse.setWarehouseTypeName(imWarehouseTypeService
		    .findWarehouseTypeNameById(warehouseTypeId));

	    if ("S".equals(categoryCode)) {
		imWarehouse.setCategoryName("專櫃");
	    } else if ("M".equals(categoryCode)) {
		imWarehouse.setCategoryName("倉庫");
	    } else {
		imWarehouse.setCategoryName("UNKNOW");
	    }

	    imWarehouse.setLocationName(buLocationService
		    .findLocationNameById(locationId));

	    if ("M".equals(taxTypeCode)) {
		imWarehouse.setTaxTypeName("免稅/完稅");
	    } else if ("T".equals(taxTypeCode)) {
		imWarehouse.setTaxTypeName("完稅");
	    } else if ("F".equals(taxTypeCode)) {
		imWarehouse.setTaxTypeName("免稅");
	    }

	}
    }

    /**
     * 將倉庫資料檔查詢結果存檔
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveSearchResult(Properties httpRequest)
	    throws Exception {
	String errorMsg = null;
	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    public String saveOrUpdateImWarehouse(ImWarehouse warehouse,
	    List warehouseEmployees, String loginUser, boolean isNew)
	    throws FormException, Exception {
	log.info("---saveOrUpdateImWarehouse---");
	return "倉儲代號：" + warehouse.getWarehouseCode() + "存檔成功！";
	// try {
	// warehouse.setWarehouseManager(warehouse.getWarehouseManager()
	// .trim().toUpperCase());
	// checkImWarehouse(warehouse);
	// checkImWarehouseEmployee(warehouseEmployees);
	//
	// // warehouse
	// // .setEnable(("N".equals(warehouse.getEnable()) ? "N" : "Y"));
	// if (isNew) {
	// ImWarehouse warehousePO = findById(warehouse.getWarehouseCode());
	// if (warehousePO == null) {
	// insertImWarehouse(warehouse, loginUser);
	// } else {
	// throw new ValidationErrorException("倉儲代號："
	// + warehouse.getWarehouseCode() + "已經存在，請勿重覆建立！");
	// }
	// } else {
	// modifyImWarehouse(warehouse, loginUser);
	// }
	// List<ImWarehouseEmployee> currentWarehouseEmployees =
	// imWarehouseEmployeeDAO
	// .findByWarehouseCode(warehouse.getWarehouseCode());
	// if (currentWarehouseEmployees != null
	// && currentWarehouseEmployees.size() > 0) {
	// for (ImWarehouseEmployee currentWarehouseEmployee :
	// currentWarehouseEmployees) {
	// imWarehouseEmployeeDAO.delete(currentWarehouseEmployee);
	// }
	// }
	// List<ImWarehouseEmployee> actualSaveWarehouseEmployees =
	// produceNewImWarehouseEmployees(
	// warehouseEmployees, warehouse.getWarehouseCode(), loginUser);
	// for (ImWarehouseEmployee actualSaveWarehouseEmployee :
	// actualSaveWarehouseEmployees) {
	// imWarehouseEmployeeDAO.save(actualSaveWarehouseEmployee);
	// }
	//
	// return "倉儲代號：" + warehouse.getWarehouseCode() + "存檔成功！";
	// } catch (FormException fe) {
	// log.error("倉庫資料存檔時發生錯誤，原因：" + fe.toString());
	// throw new FormException(fe.getMessage());
	// } catch (Exception ex) {
	// log.error("倉庫資料存檔時發生錯誤，原因：" + ex.toString());
	// throw new Exception("倉庫資料存檔時發生錯誤，原因：" + ex.getMessage());
	// }
    }

    /**
     * 新增至ImWarehouse
     * 
     * @param saveObj
     * @param loginUser
     */
    // private void insertImWarehouse(ImWarehouse saveObj, String loginUser) {
    //
    // saveObj.setCreatedBy(loginUser);
    // saveObj.setCreationDate(new Date());
    // saveObj.setLastUpdatedBy(loginUser);
    // saveObj.setLastUpdateDate(new Date());
    // imWarehouseDAO.save(saveObj);
    // }
    /**
     * 修改ImWarehouse
     * 
     * @param saveObj
     * @param loginUser
     */
    // private void modifyImWarehouse(ImWarehouse updateObj, String loginUser) {
    //
    // updateObj.setLastUpdatedBy(loginUser);
    // updateObj.setLastUpdateDate(new Date());
    // imWarehouseDAO.update(updateObj);
    // }
    /*
public void checkImWarehouse(Map parameterMap) throws Exception {
		
		Object formBindBean = parameterMap.get("vatBeanFormBind");
//		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");
		
		String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
//		Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
		
		//String warehouseId = (String) PropertyUtils.getProperty(formBindBean, "warehouseId");
		String warehouseCode = (String) PropertyUtils.getProperty(formBindBean, "warehouseCode");
		String warehouseName = (String) PropertyUtils.getProperty(formBindBean, "warehouseName");
		String warehouseManager = (String) PropertyUtils.getProperty(formBindBean, "warehouseManager");
		String storage = (String) PropertyUtils.getProperty(formBindBean, "storage");
		String storageArea = (String) PropertyUtils.getProperty(formBindBean, "storageArea");
		String storageBin = (String) PropertyUtils.getProperty(formBindBean, "storageBin");
		String warehouseCapacity = (String) PropertyUtils.getProperty(formBindBean, "warehouseCapacity");
		String warehouseTypeId = (String) PropertyUtils.getProperty(formBindBean, "warehouseTypeId");
		String categoryCode = (String) PropertyUtils.getProperty(formBindBean, "categoryCode");
		String locationId = (String) PropertyUtils.getProperty(formBindBean, "locationId");
		String taxTypeCode = (String) PropertyUtils.getProperty(formBindBean, "taxTypeCode");
		String companyCode = (String) PropertyUtils.getProperty(formBindBean, "companyCode");
		
		// 驗證地點名稱
		if(!StringUtils.hasText(warehouseCode)){
			throw new ValidationErrorException("請輸入庫別代碼！");
		}else{
			if(!StringUtils.hasText(formIdString)){
				log.info("eeeeeeeeee:"+warehouseCode);
				ImWarehouse imWareHouse = this.findById(warehouseCode);
				if (imWareHouse != null) {
				    throw new ValidationErrorException("庫別代碼：" + warehouseCode + "已經存在，請勿重複建立！");
				}
			}
		}
		// 驗證 中文與英文
		if (!StringUtils.hasText(warehouseName)) {
		    throw new ValidationErrorException("請輸入中文名稱！");
		}
	}
*/
    public ImWarehouse findWareHouseById(String id) throws Exception{
    	System.out.println("findWareHouseById().........");
	  try {
	      return imWarehouseDAO.findByWarehouseId(id);
	  } catch (Exception ex) {
	      log.error("依據庫別代碼：" + id + "查詢庫別資料時發生錯誤，原因：" + ex.toString());
	      throw new Exception("依據庫別代碼：" + id + "查詢庫別資料時發生錯誤，原因："
		    + ex.getMessage());
	  }
    }
    
    /** 檢核 HeadId 是否有值 */
	public Long getImWareHouseHeadId(Object bean) throws FormException, Exception {
		Long headId = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("headId=" + id);
		if (StringUtils.hasText(id)) {
			headId = NumberUtils.getLong(id);
		} else {
			throw new ValidationErrorException("傳入的庫別主鍵為空值！");
		}
		return headId;
	}
    
    /**
	 * 依formId取得實際庫別主檔
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
    public ImWarehouse executeFindActualImWareHouse(Map parameterMap)
	    throws FormException, Exception {
    	
//		Object formBindBean = parameterMap.get("vatBeanFormBind");
//		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");
		
		ImWarehouse imWareHouse = null;
    	try {
		
		    String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
//		    Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
		    log.info("asdf:"+formIdString);
		    imWareHouse = !StringUtils.hasText(formIdString) ? this.executeNewImWareHouse(): this.findWareHouseById(formIdString) ;
		    
		    parameterMap.put( "entityBean", imWareHouse);
		    return imWareHouse;
    	} catch (Exception e) {
    		log.error("取得庫別主檔失敗,原因:"+e.toString());
			throw new Exception("取得庫別主檔失敗,原因:"+e.toString());
		}
    }
    
    /**
	 * 前端資料塞入bean
	 * @param parameterMap
	 * @return
	 */
	public void updateImWareHouseBean(Map parameterMap)throws FormException, Exception {
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
//    	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    ImWarehouse imWareHouse = null;
    	    String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
    	    
    	    if(!StringUtils.hasText(formIdString)){
    	    	imWareHouse = new ImWarehouse();
    	    }else{
    	    	imWareHouse = this.findByWarehouseId(formIdString);
    	    }
    	    
            
    	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imWareHouse);
    	    
    	    log.info("imWareHouse.getWareHouseCode"+ imWareHouse.getWarehouseCode());
    	    log.info("imWareHouse.getEnable"+ imWareHouse.getEnable());
    	    parameterMap.put("entityBean", imWareHouse);
        } catch (FormException fe) {
		    log.error("前端資料塞入bean失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
		    throw new Exception("國別資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	
	
    
    /**
	 * 產生一筆 BuCountry
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
    public ImWarehouse executeNewImWareHouse() throws Exception {
      System.out.println("executeNewImWareHouse().........");
    	ImWarehouse form = new ImWarehouse();
    	
		form.setWarehouseCode("");
		form.setWarehouseName("");
		form.setBusinessTypeCode("");
		form.setContractCode("");
		form.setWarehouseCode("");
		form.setCategoryCode("");
		form.setCategoryCode("");
		form.setCategoryCode("");
		form.setCategoryCode("");
		form.setCategoryCode("");
		form.setCategoryCode("");
		form.setCategoryCode("");
		form.setCategoryCode("");
		form.setCategoryCode("");
		
		return form;

    }
    
    /**
     * 檢核倉庫資料
     * 
     * @param warehouse
     * @throws ValidationErrorException
     */
    private void checkImWarehouseOri(ImWarehouse warehouse)
	    throws ValidationErrorException, NoSuchObjectException {

    System.out.println(warehouse.getWarehouseCode());
    	
	if (!StringUtils.hasText(warehouse.getWarehouseCode())) {
	    throw new ValidationErrorException("請輸入倉儲代號！");
	} else {
	    warehouse.setWarehouseCode(warehouse.getWarehouseCode().trim()
		    .toUpperCase());
	}

	if (!StringUtils.hasText(warehouse.getWarehouseName())) {
	    throw new ValidationErrorException("請輸入倉儲名稱！");
	} else {
	    warehouse.setWarehouseName(warehouse.getWarehouseName().trim());
	}

	if (!StringUtils.hasText(warehouse.getStorage())) {
	    throw new ValidationErrorException("請輸入庫別分類！");
	} else {
	    warehouse.setStorage(warehouse.getStorage().trim());
	}

	if (warehouse.getWarehouseTypeId() == null) {
	    throw new ValidationErrorException("請輸入類別代號！");
	} else {
	    warehouse.setWarehouseTypeId(warehouse.getWarehouseTypeId());
	}

	if (!StringUtils.hasText(warehouse.getTaxTypeCode())) {
	    throw new ValidationErrorException("請輸入稅別！");
	} else {
	    warehouse.setTaxTypeCode(warehouse.getTaxTypeCode().trim());
	}

	if (!StringUtils.hasText(warehouse.getStorageArea())) {
	    throw new ValidationErrorException("請輸入儲區分類！");
	} else {
	    warehouse.setStorageArea(warehouse.getStorageArea().trim());
	}

	if (!StringUtils.hasText(warehouse.getCategoryCode())) {
	    throw new ValidationErrorException("請輸入倉庫類型！");
	} else {
	    warehouse.setCategoryCode(warehouse.getCategoryCode().trim());
	}

	if (warehouse.getWarehouseCapacity() == null) {
	    throw new ValidationErrorException("請輸入倉庫容量！");
	} else {
	    // 補上只可填入數字
	    try {
		Long.parseLong("" + warehouse.getWarehouseCapacity());
	    } catch (NumberFormatException e) {
		throw new ValidationErrorException("倉庫容量只可填入數字！");
	    }
	    warehouse.setWarehouseCapacity(warehouse.getWarehouseCapacity());
	}

	if (!StringUtils.hasText(warehouse.getStorageBin())) {
	    throw new ValidationErrorException("請輸入儲位分類！");
	} else {
	    warehouse.setStorageBin(warehouse.getStorageBin().trim());
	}

	if (warehouse.getLocationId() == null) {
	    throw new ValidationErrorException("請輸入地點！");
	} else {
	    warehouse.setLocationId(warehouse.getLocationId());
	}

	if (!StringUtils.hasText(warehouse.getWarehouseManager())) {
	    throw new ValidationErrorException("請輸入倉管人員！");
	} else {
	    checkImWarehouseEmployee(warehouse.getWarehouseManager(), "倉管人員");
	}
    }

    /**
     * 依工號檢核人員是否存在
     * 
     * @param employeeCode
     * @throws NoSuchObjectException
     */
    private String checkImWarehouseEmployee(String employeeCode, String message)
	    throws NoSuchObjectException {
	String empName = "";
	employeeCode = employeeCode.trim().toUpperCase();
	BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO
		.findById(employeeCode);
	log.info("checkImWarehouseEmployee--- employeeWithAddressView = "
		+ employeeWithAddressView);
	if (employeeWithAddressView != null
		&& !"null".equals(employeeWithAddressView)) {
	    empName = employeeWithAddressView.getChineseName();
	    return empName;
	}else{
	    throw new NoSuchObjectException("查無" + message + "資料。工號：" + employeeCode);
	}
	

    }

    public List<Properties> getAJAXEmployeeName(Properties httpRequest)
	    throws Exception {
	List list = new ArrayList();
	Properties properties = new Properties();
	try {
	    String employeeCode = httpRequest.getProperty("employeeCode");

	    log.info("getAJAXEmployeeName---employeeCode = " + employeeCode);

	    String empName = this.checkImWarehouseEmployee(employeeCode, "");
	    if (!"".equals(empName)) {
		properties.setProperty("employeeName", empName);
	    }

	    list.add(properties);
	} catch (Exception e) {
	    log.error("查詢姓名發生錯誤，原因：" + e.toString());
	    throw new Exception("查詢姓名發生錯誤，原因：" + e.getMessage());
	}
	return list;
    }

    /**
     * 檢核倉庫人員明細資料
     * 
     * @param warehouse
     * @param loginUser
     * @throws ValidationErrorException
     */
    private void checkImWarehouseEmployee(List<Properties> upRecords)
	    throws ValidationErrorException, NoSuchObjectException {
	log.info("checkImWarehouseEmployee ---");
	String tabName = "所屬調度人員";
	int index = 0;
	HashMap map = new HashMap();
	for (Properties upRecord : upRecords) {
	    String employeeCode = upRecord.getProperty("id.employeeCode");
	    employeeCode = employeeCode.trim().toUpperCase();

	    // for (int i = 0; i < warehouseEmployees.size(); i++) {
	    // ImWarehouseEmployee warehouseEmployee = (ImWarehouseEmployee)
	    // warehouseEmployees
	    // .get(i);
	    // ImWarehouseEmployeeId id = warehouseEmployee.getId();

	    if (!StringUtils.hasText(employeeCode)) {
		// if (!StringUtils.hasText(id.getEmployeeCode())) {
		throw new ValidationErrorException("請輸入" + tabName + "中第"
			+ (index + 1) + "項明細的工號！");
	    } else {
		// id.setEmployeeCode(id.getEmployeeCode().trim().toUpperCase());
		// BuEmployeeWithAddressView employeeWithAddressView =
		// buEmployeeWithAddressViewDAO
		// .findById(id.getEmployeeCode());
		this.checkImWarehouseEmployee(employeeCode, "明細裏第"
			+ (index + 1) + "筆");
		// BuEmployeeWithAddressView employeeWithAddressView =
		// buEmployeeWithAddressViewDAO
		// .findById(employeeCode);
		// if (employeeWithAddressView == null) {
		// throw new NoSuchObjectException("查無" + tabName + "中第"
		// + (index + 1) + "項明細的工號！");
		// }
	    }
	    log.info("checkImWarehouseEmployee ---map.size() = " + map.size());
	    log.info("checkImWarehouseEmployee ---map.get(employeeCode) = "
		    + map.get(employeeCode));

	    if (map.get(employeeCode) != null) {
		// if (map.get(id.getEmployeeCode()) != null) {
		throw new ValidationErrorException(tabName + "中第" + (index + 1)
			+ "項明細的工號重複！");
	    } else {
		// map.put(id.getEmployeeCode(), id.getEmployeeCode());
		map.put(employeeCode, employeeCode);
	    }
	    index++;
	}
    }

    public List<Properties> checkImWarehouseEmployee(Properties httpRequest)
	    throws NoSuchObjectException {
	List list = new ArrayList();
	Properties properties = new Properties();

	String employeeCode = httpRequest.getProperty("employeeCode");
	String empName = this.checkImWarehouseEmployee(employeeCode, "");
	properties.setProperty("employeeName", empName);
	list.add(properties);

	return list;
    }

    /**
     * 產生實際存檔的倉儲人員資料
     * 
     * @param warehouseEmployees
     * @param actualWarehouseCode
     * @param loginUser
     * @return List
     */
    /*
    private List<ImWarehouseEmployee> produceNewImWarehouseEmployees(List warehouseEmployees, String actualWarehouseCode,String loginUser) {

	List<ImWarehouseEmployee> actualSaveWarehouseEmployees = new ArrayList(
		0);
	for (int i = 0; i < warehouseEmployees.size(); i++) {
	    ImWarehouseEmployee warehouseEmployee = (ImWarehouseEmployee) warehouseEmployees
		    .get(i);
	    String origWarehouseCode = warehouseEmployee.getId().getWarehouseCode();
	    if (origWarehouseCode == null) {
		warehouseEmployee.getId().setWarehouseCode(actualWarehouseCode);
		warehouseEmployee.setEnable("Y");
		warehouseEmployee.setCreatedBy(loginUser);
		warehouseEmployee.setCreationDate(new Date());
		warehouseEmployee.setLastUpdatedBy(loginUser);
		warehouseEmployee.setLastUpdateDate(new Date());

		actualSaveWarehouseEmployees.add(warehouseEmployee);
	    } else {
		ImWarehouseEmployee actualSaveWarehouseEmployee = new ImWarehouseEmployee();
		BeanUtils.copyProperties(warehouseEmployee,
			actualSaveWarehouseEmployee);
		actualSaveWarehouseEmployee.getId().setWarehouseCode(
			actualWarehouseCode);
		actualSaveWarehouseEmployee.setLastUpdatedBy(loginUser);
		actualSaveWarehouseEmployee.setLastUpdateDate(new Date());

		actualSaveWarehouseEmployees.add(actualSaveWarehouseEmployee);
	    }
	}

	return actualSaveWarehouseEmployees;
    }
*/
    /*
    public void reflashImWarehouseEmployee(List reflashObjs) {

	for (int i = 0; i < reflashObjs.size(); i++) {
	    ImWarehouseEmployee warehouseEmployee = (ImWarehouseEmployee) reflashObjs.get(i);
	    ImWarehouseEmployeeId id = warehouseEmployee.getId();
	    warehouseEmployee.setEmployeeName(UserUtils.getUsernameByEmployeeCode(id.getEmployeeCode()));
	}
    }
*/
    /**
     * 檢核是否為重覆之倉儲代號
     * 
     * @param warehouseCode
     * @return boolean
     */
    public boolean isDuplication(String warehouseId) throws Exception {
	try {
	    if (warehouseId != null) {
		if (findWareHouseById(warehouseId) != null) {
		    return true;
		} else {
		    return false;
		}
	    } else {
		throw new FormException("輸入的倉儲代號為空白，請重新輸入");

	    }
	} catch (Exception ex) {
	    log.error("檢核倉儲代號重覆時發生錯誤，原因：" + ex.toString());
	    throw new Exception("檢核倉儲代號重覆時發生錯誤，原因：" + ex.getMessage());

	}

    }
/*
    public ImWarehouse findById(Long headId) {
		return imWarehouseDAO.findById(headId.toString());
	}
    */
    
    public ImWarehouse findById(String warehouseCode) {
    	
		//return (ImWarehouse)imWarehouseDAO.findByPrimaryKey(ImWarehouse.class,warehouseCode);
		return imWarehouseDAO.findById(warehouseCode);
	}

    public List<Object> find(ImWarehouse imWarehouse)
	    throws IllegalAccessException, InvocationTargetException,
	    IllegalArgumentException, SecurityException, NoSuchMethodException,
	    ClassNotFoundException {
	ImWarehouse searchObj = new ImWarehouse();
	BeanUtils.copyProperties(imWarehouse, searchObj);
	BeanUtil.changeSpace2Null(searchObj);
	List temp = new ArrayList();
	if (null != searchObj.getWarehouseCode()) {
	    temp.add(imWarehouseDAO.findByPrimaryKey(ImWarehouse.class,
		    imWarehouse.getWarehouseCode()));
	} else {
	    temp = imWarehouseDAO.findByExample(searchObj);
	}
	return temp;
    }
    
    

    /**
     * 依照倉庫資料查詢螢幕輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List<ImWarehouse> find(HashMap conditionMap) throws Exception {
	try {

	    String warehouseCode = (String) conditionMap.get("warehouseCode");
	    String warehouseName = (String) conditionMap.get("warehouseName");
	    String storage = (String) conditionMap.get("storage");
	    String storageArea = (String) conditionMap.get("storageArea");
	    String storageBin = (String) conditionMap.get("storageBin");
	    String warehouseManager = (String) conditionMap
		    .get("warehouseManager");
	    conditionMap.put("warehouseCode", warehouseCode.trim()
		    .toUpperCase());
	    conditionMap.put("warehouseName", warehouseName.trim());
	    conditionMap.put("storage", storage.trim());
	    conditionMap.put("storageArea", storageArea.trim());
	    conditionMap.put("storageBin", storageBin.trim());
	    conditionMap.put("warehouseManager", warehouseManager.trim()
		    .toUpperCase());

	    return imWarehouseDAO.find(conditionMap);
	} catch (Exception ex) {
	    log.error("查詢倉庫資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢倉庫資料時發生錯誤，原因：" + ex.getMessage());
	}
    }

    public List<ImWarehouse> findWarehouseBelong2Employee(String employeeCode,
	    HashMap findObjs) throws Exception {
	try {
	    return imWarehouseDAO.findWarehouseBelong2Employee(employeeCode,
		    findObjs);
	} catch (Exception ex) {
	    log.error("倉庫查詢時發生錯誤，原因：" + ex.toString());
	    throw new Exception("倉庫查詢時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 依據品牌代號、工號、啟用狀態等條件查詢
     * 
     * @param brandCode
     * @param employeeCode
     * @param isEnable
     * @return List
     * @throws Exception
     */
    public List<ImWarehouse> getWarehouseByWarehouseEmployee(String brandCode,
	    String employeeCode, String isEnable) throws Exception {

	try {
	    return imWarehouseDAO.getWarehouseByWarehouseEmployee(brandCode,
		    employeeCode, isEnable);
	} catch (Exception ex) {
	    log.error("依據品牌代號：" + brandCode + "、工號：" + employeeCode
		    + "查詢倉庫資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌代號：" + brandCode + "、工號：" + employeeCode
		    + "查詢倉庫資料時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 依據品牌代號、庫號、啟用狀態等條件查詢(AJAX)
     * 
     * @param httpRequest
     * @return List<Properties>
     */
    public List<Properties> findByBrandCodeAndWarehouseCodeForAJAX(
	    Properties httpRequest) throws Exception {

	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String brandCode = null;
	String warehouseCode = null;
	String isEnable = null;
	try {
	    brandCode = httpRequest.getProperty("brandCode");
	    warehouseCode = httpRequest.getProperty("warehouseCode");
	    warehouseCode = warehouseCode.trim().toUpperCase();
	    ImWarehouse warehousePO = (ImWarehouse) imWarehouseDAO
		    .findByBrandCodeAndWarehouseCode(brandCode, warehouseCode,
			    isEnable);
	    if (warehousePO != null) {
		properties.setProperty("WarehouseCode", warehousePO
			.getWarehouseCode());
		properties
			.setProperty("WarehouseName", AjaxUtils
				.getPropertiesValue(warehousePO
					.getWarehouseName(), ""));
		properties.setProperty("WarehouseManager", AjaxUtils
			.getPropertiesValue(warehousePO.getWarehouseManager(),
				""));
	    } else {
		properties.setProperty("WarehouseCode", warehouseCode);
		properties.setProperty("WarehouseName", "查無此庫別");
		properties.setProperty("WarehouseManager", "");
	    }
	    result.add(properties);

	    return result;
	} catch (Exception ex) {
	    log.error("依據庫別代號：" + warehouseCode + "查詢庫別資料時發生錯誤，原因："
		    + ex.toString());
	    throw new Exception("查詢庫別資料失敗！");
	}
    }

    /**
     * 依據品牌代號、庫號、啟用狀態等條件查詢
     * 
     * @param brandCode
     * @param warehouseCode
     * @param isEnable
     * @return ImWarehouse
     */
    public ImWarehouse findByBrandCodeAndWarehouseCode(String brandCode,
	    String warehouseCode, String isEnable) throws Exception {
	try {
	    warehouseCode = warehouseCode.trim().toUpperCase();
	    return imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode,
		    warehouseCode, isEnable);
	} catch (Exception ex) {
	    log.error("依據品牌代號：" + brandCode + "、倉儲代號：" + warehouseCode
		    + "查詢倉庫資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌代號：" + brandCode + "、倉儲代號："
		    + warehouseCode + "查詢倉庫資料時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /*public List<Properties> findByIdForAJAX(Properties httpRequest)
	    throws Exception {
	    log.error("依據品牌代號：" + brandCode + "、倉儲代號：" + warehouseCode + "查詢倉庫資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌代號：" + brandCode + "、倉儲代號：" + warehouseCode + "查詢倉庫資料時發生錯誤，原因：" + ex.getMessage());
	}	
    }  */

    /**
     * 依據品牌代號、庫號、啟用狀態等條件查詢
     * 
     * @param brandCode
     * @param warehouseCode
     * @param isEnable
     * @return ImWarehouse
     */
    public List<ImWarehouse> findByBrandCode(String brandCode, String isEnable) throws Exception{
	try {
	    return imWarehouseDAO.findByBrandCode(brandCode, isEnable);
	} catch (Exception ex) {
	    log.error("依據品牌代號：" + brandCode + "查詢倉庫資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌代號：" + brandCode + "查詢倉庫資料時發生錯誤，原因：" + ex.getMessage());
	}	
    }  
    
   /* public List<Properties> findByIdForAJAX(Properties httpRequest) throws Exception{
	
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String warehouseCode = null;
	try {
	    warehouseCode = httpRequest.getProperty("warehouseCode");
	    warehouseCode = warehouseCode.trim().toUpperCase();
	    ImWarehouse warehousePO = (ImWarehouse) imWarehouseDAO
		    .findByPrimaryKey(ImWarehouse.class, warehouseCode);
	    if (warehousePO != null) {
		properties.setProperty("WarehouseCode", warehousePO
			.getWarehouseCode());
		properties
			.setProperty("WarehouseName", AjaxUtils
				.getPropertiesValue(warehousePO
					.getWarehouseName(), ""));
		properties.setProperty("WarehouseManager", AjaxUtils
			.getPropertiesValue(warehousePO.getWarehouseManager(),
				""));
	    } else {
		properties.setProperty("WarehouseCode", warehouseCode);
		properties.setProperty("WarehouseName", "查無此倉儲代號資料");
		properties.setProperty("WarehouseManager", "");
	    }
	    result.add(properties);

	    return result;
	} catch (Exception ex) {
	    log.error("依據倉儲代號：" + warehouseCode + "查詢倉庫資料時發生錯誤，原因："
		    + ex.toString());
	    throw new Exception("查詢倉儲資料失敗！");
	}
    }*/

    /**
     * 依據品牌代號、庫號、啟用狀態等條件查詢(AJAX)
     * 
     * @param httpRequest
     * @return List<Properties>
     */
    public List<Properties> findByBrandCodeAndWarehouseCode(
	    Properties httpRequest) throws Exception {

	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String brandCode = null;
	String warehouseCode = null;
	String isEnable = null;
	try {
	    brandCode = httpRequest.getProperty("brandCode");
	    warehouseCode = httpRequest.getProperty("warehouseCode");
	    warehouseCode = warehouseCode.trim().toUpperCase();
	    ImWarehouse warehousePO = (ImWarehouse) imWarehouseDAO
		    .findByBrandCodeAndWarehouseCode(brandCode, warehouseCode,
			    isEnable);
	    if (warehousePO != null) {
		properties.setProperty("WarehouseCode", warehousePO
			.getWarehouseCode());
		properties
			.setProperty("WarehouseName", AjaxUtils
				.getPropertiesValue(warehousePO
					.getWarehouseName(), ""));
		properties.setProperty("WarehouseManager", AjaxUtils
			.getPropertiesValue(warehousePO.getWarehouseManager(),
				""));
	    } else {
		properties.setProperty("WarehouseCode", warehouseCode);
		properties.setProperty("WarehouseName", "查無此庫別");
		properties.setProperty("WarehouseManager", "");
	    }
	    result.add(properties);

	    return result;
	} catch (Exception ex) {
	    log.error("依據庫別代號：" + warehouseCode + "查詢庫別資料時發生錯誤，原因："
		    + ex.toString());
	    throw new Exception("查詢庫別資料失敗！");
	}
    }

    /**
     * ajax 取得庫別相關欄位(拆併貨調整單)(條碼輸出)
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXWarehouseCode(Properties httpRequest) throws Exception {
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try {
		    String warehouseCode = httpRequest.getProperty("warehouseCode").trim().toUpperCase();
		    String brandCode = httpRequest.getProperty("brandCode");
		    log.info("warehouseCode = " + warehouseCode);
		    log.info("brandCode = " + brandCode);
	
		    ImWarehouse imWarehouse = (ImWarehouse) imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, warehouseCode, "Y");
		    properties.setProperty("warehouseCode", warehouseCode);
	
		    if (StringUtils.hasText(warehouseCode)) {
				if (imWarehouse != null) {
				    properties.setProperty("warehouseName", imWarehouse.getWarehouseName());
				} else {
				    properties.setProperty("warehouseName", "查無庫號");
				}
		    } else {
		    	properties.setProperty("warehouseName", "");
		    }
	
		    result.add(properties);
		    return result;
		} catch (Exception ex) {
		    log.error("取得庫別相關欄位資料發生錯誤，原因：" + ex.toString());
		    throw new Exception("取得庫別相關欄位資料失敗！");
		}
    }

    /**
     * 初始化
     * 
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception {
	log.info("============<executeInitial>===============2");
	HashMap resultMap = new HashMap();
	Map multiList = new HashMap();
	ImWarehouse imWarehouse = null;
	try {
	    HashMap argumentMap = getRequestParameter(parameterMap, false);

	    String formId = (String) argumentMap.get("formId");
	    String loginBrandCode = (String) argumentMap.get("loginBrandCode");
	    String loginEmployeeCode = (String) argumentMap.get("loginEmployeeCode");

	    log.info("executeInitial formId = " + formId);
	    log.info("executeInitial argumentMap = " + argumentMap);

	    //formId ==> warehouseCode
	    //設定初始值
	    if (formId == null) {
		imWarehouse = createNewImWarehouse(argumentMap, resultMap);       //-->新增
	    } else {
		imWarehouse = findImWarehouse(argumentMap, resultMap, multiList); //-->修改
	    }
	    
	    //================畫面下拉選單內容初始設定================
		List <ImWarehouseType> warehouseTypeId = imWarehouseTypeService.findAll();
		log.info("warehouseTypeId.size:"+warehouseTypeId.size());
		List <BuLocation> buLocations = buLocationService.findAll();
		log.info("buLocations.size:"+buLocations.size());
		multiList.put("warehouseTypeId" , AjaxUtils.produceSelectorData(warehouseTypeId  ,"warehouseTypeId" ,"name",  true,  true));
		multiList.put("buLocations", AjaxUtils.produceSelectorData(buLocations, "locationId", "locationName", true, true));

	    resultMap.put("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));
	    resultMap.put("createByName", UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));

	    resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());
	    //================畫面下拉選單內容初始設定================
	    
	    //================讀取倉管人員姓名================
		if (imWarehouse.getWarehouseManager()!="")
			resultMap.put("warehouseManagerName" ,this.checkImWarehouseEmployee(imWarehouse.getWarehouseManager(),""));
		else
			resultMap.put("warehouseManagerName" ,"");
		//================讀取倉管人員姓名================
		
	    resultMap.put("form", imWarehouse);

	    // 取得下拉
	    getInitData(multiList, resultMap, loginBrandCode);

	    resultMap.put("multiList", multiList);

	    log.info("executeInitial resultMap = " + resultMap);
	    return resultMap;
	} catch (Exception ex) {
	    log.error("倉庫資料檔維護初始化失敗，原因：" + ex.toString());
	    throw new Exception("倉庫資料檔維護初始化失敗，原因：" + ex.toString());
	}
    }

    private HashMap getRequestParameter(Map parameterMap, boolean isSubmitAction)
	    throws Exception {

	Object otherBean = parameterMap.get("vatBeanOther");
	String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,
		"loginBrandCode");
	String loginEmployeeCode = (String) PropertyUtils.getProperty(
		otherBean, "loginEmployeeCode");
	String formIdString = (String) PropertyUtils.getProperty(otherBean,
		"formId");
	String formId = StringUtils.hasText(formIdString) ? formIdString : null;
	HashMap conditionMap = new HashMap();
	conditionMap.put("loginBrandCode", loginBrandCode);
	conditionMap.put("loginEmployeeCode", loginEmployeeCode);

	conditionMap.put("formId", formId);
	// if (isSubmitAction) {
	// String beforeChangeStatus = (String) PropertyUtils.getProperty(
	// otherBean, "beforeChangeStatus");
	// String formStatus = (String) PropertyUtils.getProperty(otherBean,
	// "formStatus");
	// conditionMap.put("beforeChangeStatus", beforeChangeStatus);
	// conditionMap.put("formStatus", formStatus);
	// }

	return conditionMap;
    }

    /**
     * 初始化下拉
     * 
     * @param multiList
     * @param resultMap
     * @param loginBrandCode
     * @throws Exception
     */
    public void getInitData(Map multiList, HashMap resultMap,
	    String loginBrandCode) throws Exception {

	List<BuCommonPhraseLine> allTaxCatalogs = baseDAO.findByProperty(
		"BuCommonPhraseLine", new String[] {
			"id.buCommonPhraseHead.headCode", "enable" },
		new Object[] { "TaxTypeCode", "Y" }, "indexNo");
	List<BuLocation> allBuLocations = buLocationDAO.findAll();
	List<BuCommonPhraseLine> allCategoryCodes = baseDAO.findByProperty(
		"BuCommonPhraseLine", new String[] {
			"id.buCommonPhraseHead.headCode", "enable" },
		new Object[] { "CategoryCode", "Y" }, "indexNo");
	List<ImWarehouseType> allImWarehouseTypes = imWarehouseTypeService
		.findAll();
	List<BuCommonPhraseLine> allCustomsWarehouseCodes = baseDAO
		.findByProperty("BuCommonPhraseLine", new String[] {
			"id.buCommonPhraseHead.headCode", "enable" },
			new Object[] { "CustomsWarehouseCode", "Y" }, "indexNo");
	List<BuCommonPhraseLine> allWarehouseAreas = baseDAO.findByProperty(
		"BuCommonPhraseLine", new String[] {
			"id.buCommonPhraseHead.headCode", "enable" },
		new Object[] { "WarehouseArea", "Y" }, "indexNo");
	List<BuCommonPhraseLine> allWarehouseIOAreas = baseDAO.findByProperty(
		"BuCommonPhraseLine", new String[] {
			"id.buCommonPhraseHead.headCode", "enable" },
		new Object[] { "WarehouseIOArea", "Y" }, "indexNo");
	List<BuCommonPhraseLine> allContractCodes = baseDAO.findByProperty(
		"BuCommonPhraseLine", new String[] {
			"id.buCommonPhraseHead.headCode", "enable" },
		new Object[] { "WarehouseContract", "Y" }, "indexNo");
//	List<BuCompany> allCompanyCodes = baseDAO.findByProperty("BuCompany",
//		new String[] { "companyCode" }, new Object[] { "companyCode" },
//		"companyCode");
	List<BuCommonPhraseLine> allTaxAreaCodes = baseDAO.findByProperty(
		"BuCommonPhraseLine", new String[] {
			"id.buCommonPhraseHead.headCode", "enable" },
		new Object[] { "WarehouseTaxArea", "Y" }, "indexNo");
	
	List<BuCommonPhraseLine> allBusinessTypeCodes = baseDAO.findByProperty(
		"BuCommonPhraseLine", new String[] {
			"id.buCommonPhraseHead.headCode", "enable" },
		new Object[] { "WarehouseBusinessType", "Y" }, "indexNo");

	multiList.put("allTaxCatelogs", AjaxUtils.produceSelectorData(
		allTaxCatalogs, "lineCode", "name", true, true));
	multiList.put("allImWarehouseTypes", AjaxUtils.produceSelectorData(
		allImWarehouseTypes, "warehouseTypeId", "name", true, true));
	multiList.put("allCategoryCodes", AjaxUtils.produceSelectorData(
		allCategoryCodes, "lineCode", "name", true, true));
	multiList.put("allBuLocations", AjaxUtils.produceSelectorData(
		allBuLocations, "locationId", "locationName", true, true));

	multiList.put("allCustomsWarehouseCodes", AjaxUtils
		.produceSelectorData(allCustomsWarehouseCodes, "lineCode",
			"name", true, true));
	multiList.put("allWarehouseAreas", AjaxUtils.produceSelectorData(
		allWarehouseAreas, "lineCode", "name", true, true));
	multiList.put("allWarehouseIOAreas", AjaxUtils.produceSelectorData(
		allWarehouseIOAreas, "lineCode", "name", true, true));
	multiList.put("allContractCodes", AjaxUtils.produceSelectorData(
		allContractCodes, "lineCode", "name", true, true));
//	multiList.put("allCompanyCodes", AjaxUtils.produceSelectorData(
//		allCompanyCodes, "companyCode", "companyName", true, true));
	multiList.put("allTaxAreaCodes", AjaxUtils.produceSelectorData(
		allTaxAreaCodes, "lineCode", "name", true, true));
	multiList.put("allBusinessTypeCodes", AjaxUtils.produceSelectorData(
		allBusinessTypeCodes, "lineCode", "name", true, true));

    }

    // create default ImWarehouse
    public ImWarehouse createNewImWarehouse(Map argumentMap, Map resultMap)
	    throws Exception {

	try {
		System.out.println("====createNew====");
	    String loginEmployeeCode = (String) argumentMap
		    .get("loginEmployeeCode");
	    String loginBrandCode = (String) argumentMap.get("loginBrandCode");

	    Date date = new Date();

	    ImWarehouse imWarehouse = new ImWarehouse();
	    imWarehouse.setBrandCode(loginBrandCode);
	    imWarehouse.setWarehouseCapacity(0L);
        
	    imWarehouse.setCompanyCode("");
	    imWarehouse.setTaxAreaCode("");
	    imWarehouse.setAllowMinusStock("");
	    imWarehouse.setFinanceSalesType("");
	    imWarehouse.setCustomsArea("");
	    imWarehouse.setBusinessTypeCode("");
	    imWarehouse.setWarehouseCode("");
	    imWarehouse.setEnable("Y");
	    imWarehouse.setWarehouseName("");
	    imWarehouse.setWarehouseManager("");
	    imWarehouse.setStorage("");
	    imWarehouse.setStorageArea("");
	    imWarehouse.setStorageBin("");
	    imWarehouse.setCustomsWarehouseCode("");
	    resultMap.put("tmpEnable","Y".equals(imWarehouse.getEnable()) ? "N" : "Y");
	    imWarehouse.setAllowMinusStock("N");
	    imWarehouse.setWarehouseArea("");
	    imWarehouse.setIoArea("");
	    imWarehouse.setContractCode("");
	    imWarehouse.setLocationId(Long.parseLong("0"));
	    
	    imWarehouse.setCreatedBy(loginEmployeeCode);
	    imWarehouse.setCreationDate(date);
	    imWarehouse.setLastUpdatedBy(loginEmployeeCode);
	    imWarehouse.setLastUpdateDate(date);
	    
	    resultMap.put("form", imWarehouse);

	    return imWarehouse;
	} catch (Exception ex) {
	    log.error("產生倉庫資料檔失敗，原因：" + ex.toString());
	    throw new Exception("產生倉庫資料檔發生錯誤！");
	}
    }

    // Find ImWarehouse data
    public ImWarehouse findImWarehouse(Map argumentMap, Map resultMap, Map multiList) throws FormException, Exception {
		try {
		    String formId = (String) argumentMap.get("formId");
		    log.info("findImWarehouse--formId = " + formId);
		    ImWarehouse form = this.findByWarehouseId(formId);
		    if (form != null) {
				resultMap.put("entityBean", form);
				log.info("form.name XXXX"+form.getEnable());
				log.info("findImWarehouse--form = " + form);
				return form;
		    } else {
		    	throw new NoSuchObjectException("查無倉庫資料檔主鍵：" + formId + "的資料！");
		    }
		} catch (FormException fe) {
		    log.error("查詢倉庫資料檔單失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    log.error("查詢倉庫資料檔單發生錯誤，原因：" + ex.toString());
		    throw new Exception("查詢倉庫資料檔單發生錯誤！");
		}
    }

    public ImWarehouse findByWarehouseId(String id) {
    	return imWarehouseDAO.findByWarehouseId(id);
    }

    /**
     * 倉庫檔明細欄位設定.
     */
    public static final String[] GRID_FIELD_NAMES = { "indexNo",
    	"enable", "id.employeeCode", "employeeName" };

    public static final String[] GRID_FIELD_DEFAULT_VALUES = { "", "", "", "" };

    public static final int[] GRID_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_LONG,
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	    AjaxUtils.FIELD_TYPE_STRING};

    /**
     * ajax 庫存資料檔載入時,取得明細分頁資料
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXPageData(Properties httpRequest)
	    throws Exception {
	try {
		System.out.println("========================getAJAXPageData=========================:"+httpRequest.getProperty("warehouseId"));
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
        String warehouseId = httpRequest.getProperty("warehouseId");// 取得warehouseCode
	    
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小


	    // ======================帶入Head的值=========================
	    List<ImWarehouseEmployee> imWarehouseEmployees = null;
	    
	    HashMap findObjs = new HashMap();
	    findObjs.put("and model.warehouseId = :warehouseId",
	    		warehouseId);
	    if(warehouseId.equals("") || warehouseId ==null){
	    	imWarehouseEmployees = null;
	    }else{
	    	Map imWarehouseEmployeeMap = imWarehouseEmployeeDAO.search("ImWarehouseEmployee as model",findObjs, "order by model.id.employeeCode ", iSPage, iPSize,BaseDAO.QUERY_SELECT_RANGE);
		    imWarehouseEmployees = (List<ImWarehouseEmployee>) imWarehouseEmployeeMap.get(BaseDAO.TABLE_LIST);
	    }
	    
	    if (imWarehouseEmployees != null && imWarehouseEmployees.size() > 0) {
	    Long index = 1L;
	    if(iSPage>0){
	    	index = Long.valueOf(iSPage * iPSize) + 1;
	    }
	    for (ImWarehouseEmployee imWarehouseEmployee : imWarehouseEmployees) {
			//System.out.println("進入明細中~~~~~111"+UserUtils.getUsernameByEmployeeCode(imWarehouseEmployee.getEmployeeCode()));
			//System.out.println("進入明細中~~~~~222"+UserUtils.getUsernameByEmployeeCode(imWarehouseEmployee.getId().getEmployeeCode()));
		    imWarehouseEmployee.setEmployeeName(UserUtils.getUsernameByEmployeeCode(imWarehouseEmployee.getId().getEmployeeCode().trim().toUpperCase()));
		    imWarehouseEmployee.setIndexNo(index);
		    index++;
		}    
		Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
		
		// 取得最後一筆 INDEX
		Long maxIndex = (Long) baseDAO.search(
			"ImWarehouseEmployee as model",
			"count(model.id.warehouseCode) as rowCount", findObjs,
			BaseDAO.QUERY_RECORD_COUNT).get(
			BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		
		result.add(AjaxUtils.getAJAXPageData(httpRequest,
			GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,
			imWarehouseEmployees, gridDatas, firstIndex, maxIndex));
	    } else {
	    	 
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
				GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,
				gridDatas));
	    }
	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的明細失敗！");
	}
    }
    
   

    // 異動一筆倉庫資料檔
    public Map updateAJAXImWarehouse(Map parameterMap) throws Exception {
    	System.out.println("======異動庫別單頭資料=========");
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0);
		String resultMsg = null;
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");
	    
	    String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    ImWarehouse imWarehouse = (ImWarehouse)parameterMap.get("entityBean");
	    String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
	    
	    
	    String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
	    
	    if(OrderStatus.FORM_SUBMIT.equals(formAction))
	    {
	    	imWarehouse.setCreatedBy(loginEmployeeCode);
	    	imWarehouse.setCreationDate(new Date());
	    	imWarehouse.setLastUpdatedBy(loginEmployeeCode);
	    	imWarehouse.setLastUpdateDate(new Date());
	    	imWarehouse.setEnable("Y".equals(imWarehouse.getEnable())?"N":"Y");
	    	System.out.println("======準備異動庫別單頭資料=========:"+imWarehouse.getFinanceSalesType());
	    	if(!StringUtils.hasText(imWarehouse.getFinanceSalesType())){
				System.out.println("======進入FinanceSalesType判斷=========");
	    		imWarehouse.setFinanceSalesType("");
			}
	    	
	    	//if(!StringUtils.hasText(warehouseId)){
	    		//imWarehouseUpdate = this.findByWarehouseId(warehouseId);
	    	//}	
		    	
		    	if(!StringUtils.hasText(formIdString) )
		    	{
		    		
		    		imWarehouse.setCreatedBy(loginEmployeeCode);
		    		imWarehouse.setCreationDate(new Date());
		    		this.save(imWarehouse, SAVE);
		    	}
		    	else
		    	{
		    		
		    		imWarehouse.setCreatedBy(loginEmployeeCode);
		    		//imWarehouse.setWarehouseId(Long.parseLong(formIdString));
		    		this.save(imWarehouse, UPDATE);
		    	}	    		
	    	
	    	
	    }
	    
    	resultMsg = "Warehouse Name：" + imWarehouse.getWarehouseName() + "存檔成功！ 是否繼續新增？";
    	
	    resultMap.put("resultMsg", resultMsg);
	    resultMap.put("entityBean", imWarehouse);
	    resultMap.put("vatMessage", msgBox);
	    
	    return resultMap;

	   
	} catch (FormException fe) {
	    log.error("倉庫維護檔主檔維護作業存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("倉庫維護檔主檔維護作業存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception(ex.getMessage());
	}
    }

    public String save(ImWarehouse imWarehouse, String action) throws Exception {
	try {
	    this.checkImWarehouseOri(imWarehouse);

	    imWarehouse.setWarehouseManager(imWarehouse.getWarehouseManager()
		    .trim().toUpperCase());
	  	    
	    //this.deleteLine(imWarehouse.getWarehouseCode());
	   
	    System.out.println(action);
	    if (SAVE.equals(action)) {
	    	
		imWarehouseDAO.save(imWarehouse);
	    } else {
	    
		imWarehouseDAO.update(imWarehouse);
	    }
	    return imWarehouse.getWarehouseCode() + "存檔成功";

	} catch (FormException fe) {
	    log.error("更新倉庫資料檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());

	} catch (Exception ex) {
	    log.error("更新倉庫資料檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception(ex.getMessage());
	}
    }

    /**
     * 更新明細
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateOrSaveAJAXPageLinesData(Properties httpRequest)throws Exception {
    	 
    	try {
	    Date date = new Date();
	 
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
	    String warehouseCode = httpRequest.getProperty("warehouseCode");
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    String warehouseId = httpRequest.getProperty("warehouseId");
	    String enable = httpRequest.getProperty("enable");
	    Long indexNo = 0L;
	    ImWarehouseEmployeeId ids = null;
	    String errorMsg = null;
	    System.out.println("==========warehouseId=============="+warehouseId);
	    System.out.println("==========warehouseCode=============="+warehouseCode);	
	    if (warehouseId != "") 
	    {
	    	// 將STRING資料轉成List Properties record data
	    	List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
	    	
	    	System.out.println("==========upRecords=============="+upRecords.size());	
	    	
//	        List<ImWarehouseEmployee> imWarehouseEmployees = imWarehouseEmployeeDAO.findByProperty("ImWarehouseEmployee", "indexNo",
//				    " and warehouseId = ? ",new String[] { warehouseId }," order by indexNo desc");
	        
		   //if (imWarehouseEmployees.size() > 0) {indexNo = Long.parseLong("" + imWarehouseEmployees.get(0));}
		    //你這邊直接用 indexNo = imWarehouseEmployees.size() 就好了 頂多加個 0L
		    //indexNo = Long.parseLong(""+imWarehouseEmployees.size());
		    indexNo = imWarehouseEmployeeDAO.findPageLineMaxIndex(warehouseId);
		    log.info("warehouseId:"+warehouseId+" "+indexNo);
		    if (upRecords != null) 
		    {
		    	for (Properties upRecord : upRecords) {
		    		
		    		String employeeCode = upRecord.getProperty("id.employeeCode");
		    		log.info("前端來的emp:"+employeeCode+" "+warehouseCode);		    		
		    		if(employeeCode.equals("")||employeeCode==null){
		    			log.info("無資料");
		    		}else{
		    			if(StringUtils.hasText(employeeCode)){	
				    		ImWarehouseEmployee emp = imWarehouseEmployeeDAO.findByEmpCode(employeeCode,warehouseCode);
				    		
				    		//log.info("前端來的emp2:"+emp.getId().getEmployeeCode());
				    		
				    		//List empList = imWarehouseEmployeeDAO.findByEmpCode(employeeCode,warehouseCode);
				    		//log.info("有無值:"+empList.size());
				    		//ImWarehouseEmployee emp1 = null;
				    		//ImWarehouseEmployee emp  = null;
				    		 log.info("emp:"+emp);  
				    		if (emp != null) {
				    		        //AjaxUtils.setPojoProperties(emp, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
				    				emp.getId().setEmployeeCode(employeeCode);
				    		        log.info("更新庫別人員");  
									emp.setLastUpdatedBy(loginEmployeeCode);
									emp.setEnable(upRecord.getProperty("enable"));
									emp.setLastUpdateDate(date);
									imWarehouseEmployeeDAO.merge(emp);
									
								//}
							} else {
								if(employeeCode==null || employeeCode.equals("")){
									log.info("無新增庫別人員");  
								}else{
									log.info("新增庫別人員");  
									indexNo++;
									ids = new ImWarehouseEmployeeId(warehouseCode,employeeCode);
									log.info("ids"+ids.getEmployeeCode()+" "+ids.getWarehouseCode());
									ImWarehouseEmployee emp1 = new ImWarehouseEmployee();
									emp1.setEnable(upRecord.getProperty("enable"));									 
									emp1.setId(ids);									
									emp1.setWarehouseId(Long.parseLong(warehouseId));									 
									emp1.setIndexNo(Long.valueOf(indexNo));									 
									emp1.setCreatedBy(loginEmployeeCode);									 
									emp1.setCreationDate(date);									 
									emp1.setLastUpdatedBy(loginEmployeeCode);									 
									emp1.setLastUpdateDate(date);						    	
									imWarehouseEmployeeDAO.save(emp1);								
								}								
							} 						    		
				    	}
		    		}		    		
		    	}	
		    }
	    }
	    else
	    {
	    	errorMsg = "請輸入倉儲代碼！";
	    }
	    
	    return AjaxUtils.getResponseMsg(errorMsg);

	    //if (warehouseCode == null || warehouseCode=="") {
	    //	System.out.println("我進來了~~~");
		//throw new ValidationErrorException("傳入的倉庫維護明細資料的主鍵為空值！");
	    //}
	} catch (Exception ex) {
	    log.error("更新倉庫維護檔明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新倉庫維護檔明細失敗！" + ex.getMessage());
	}
	
	  //return new ArrayList();
    }

    /**
     * 檢核倉庫資料
     * 
     * @param warehouse
     * @param action
     * @throws Exception
     * @throws ValidationErrorException
     * @throws ValidationErrorException
     */
    public void checkImWarehouseCode(String warehouseCode)
	    throws ValidationErrorException, Exception {
	if (this.isDuplication(warehouseCode)) {
	    throw new ValidationErrorException("倉儲代號：" + warehouseCode
		    + "已經存在，請勿重覆建立！");
	}
    }

    /**
	 * 依據品牌代號查詢
	 * 
	 * @param brandCode
	 * @return List
	 * @throws Exception
	 */
	public List<ImWarehouse> findByBrandCode(String brandCode) throws Exception {

		try {
			return imWarehouseDAO.findByBrandCodeOrder(brandCode, "Y");
		} catch (Exception ex) {
			log.error("依據品牌代號：" + brandCode + "查詢倉庫資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據品牌代號：" + brandCode + "查詢倉庫資料時發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	public void updateImWarehouseBean(Map parameterMap)throws FormException, Exception {
		
		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			
			ImWarehouse imWarehouse = new ImWarehouse();
			
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imWarehouse);
			
			parameterMap.put("entityBean", imWarehouse);
		}catch (FormException fe) {
		    log.error("倉庫資料塞入bean失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    log.error("倉庫資料塞入bean發生錯誤，原因：" + ex.toString());
		    throw new Exception("品牌資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	/**
	 * 依關別取得庫別
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXEditWarehouseCode(Properties httpRequest)throws Exception{
		List list = new ArrayList();
		Properties properties = new Properties();
		try{
			String brandCode = httpRequest.getProperty("brandCode");
			String customsWarehouseCode = httpRequest.getProperty("customsWarehouseCode");
			
			log.info("brandCode = " + brandCode);
			log.info("customsWarehouseCode = " + customsWarehouseCode);
			
			List<ImWarehouse> wc = imWarehouseDAO.findImWarehouse(brandCode, customsWarehouseCode, "");
			wc = AjaxUtils.produceSelectorData(wc, "warehouseCode", "warehouseName", true, true);

			properties.setProperty("warehouseCode", AjaxUtils.parseSelectorData(wc));
			list.add(properties);
		}catch(Exception e){
			log.error("取得指定連動的類別下拉，原因：" + e.toString());
			throw new Exception("取得指定連動的類別下拉，原因：" + e.getMessage());
		}
		return list;
	}
	
	public SelectDataInfo findViewByMap(HttpServletRequest httpRequest) throws Exception {
		log.info("findViewByMap...export excel");
		String beanName = (String)httpRequest.getParameter("exportBeanName");
		HashMap map = new HashMap();
		try {

			map.put("warehouseCode"        , (String)httpRequest.getParameter("warehouseCode"));
			map.put("QueryType"        , "ExcelExport");	
			map.put("isSuperVisor"   	   , beanName);
			// 可用庫存excel表的欄位順序
			Object[] object = null;
			object = new Object[] {"enable","employeeCode"};
			
			List<Object[]> imwareHouseEmps = imWarehouseEmployeeDAO.findViewByMap(map);
			

		// 按excel表的欄位順序將資料放入Object[]，再一筆筆放到List

			log.info("imwareHouseEmps.size:"+imwareHouseEmps.size());
			List rowData = new ArrayList();
			String line = new String("");
			Long headId = new Long(0);
			for (int i = 0; i < imwareHouseEmps.size(); i++) {
				line ="";
				Object[] dataObject = (Object[]) imwareHouseEmps.get(i);
				for (int j = 0; j < object.length; j++) {
					String actualValue = null;
					
						if (object[j] != null) {
							actualValue = dataObject[j] != null ? String.valueOf(dataObject[j]) : null;
						}
					
					log.info("excel output i:"+i+"/j:"+j+"/value:"+actualValue);
					dataObject[j] = actualValue;
					line = line +"/"+actualValue;
			}
		    log.info("阿災");
			rowData.add(dataObject);
			log.info(line);
		}
		return new SelectDataInfo(object, rowData);
		} catch (Exception ex) {
			// ex.printStackTrace();
			log.error("匯出庫別人員資訊時發生錯誤，原因：" + ex.toString());
			throw new Exception("匯出庫別人員資訊時失敗！");
		}
	}
	
	//by jason 20160315  明細匯入
    public void executeImportImwareHouseEmps(String warehouseCode,List employee) throws FormException, Exception{
		log.info("111ddd:"+warehouseCode);
		log.info("111ddd:"+employee.size());
		try{
			ImWarehouse imeWarehous  =  imWarehouseDAO.findById(warehouseCode);
    	    if(imeWarehous == null){
    	        throw new NoSuchObjectException("查無此庫：" + warehouseCode + "的資料");
    	    }
			Long warehouseId = imeWarehous.getWarehouseId();		
			List<ImWarehouseEmployee> imWarehouseEmployees = new ArrayList(0);

				for(int i = 0  ; i<employee.size() ; i++){
					ImWarehouseEmployeeId imWarehouseEmployeeId = new ImWarehouseEmployeeId();
					ImWarehouseEmployee newImWarehouseEmployee = (ImWarehouseEmployee)employee.get(i);	
		    	    imWarehouseEmployeeId.setWarehouseCode(warehouseCode);	 
		    	    imWarehouseEmployeeId.setEnable(newImWarehouseEmployee.getEnable());
		    		BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findById(newImWarehouseEmployee.getEmployeeCode());
		    	    if(employeeWithAddressView == null)
		    	    	throw new Exception(newImWarehouseEmployee.getEmployeeCode()+"不存在此工號");
		    	    else
		    	    	imWarehouseEmployeeId.setEmployeeCode(newImWarehouseEmployee.getEmployeeCode());
		    	    
		    	    newImWarehouseEmployee.setWarehouseId(warehouseId);
		    	    newImWarehouseEmployee.setId(imWarehouseEmployeeId);
		    	    imWarehouseEmployees.add(newImWarehouseEmployee);	

				}
				imeWarehous.setWarehouseEmployees(imWarehouseEmployees);
				imWarehouseDAO.merge(imeWarehous);

				List<ImWarehouseEmployee> imwe = imWarehouseEmployeeDAO.findByWarehouseCode(warehouseCode);
				List<ImWarehouseEmployee> imwee = new ArrayList();
				for(ImWarehouseEmployee i:imwe){
					i.setWarehouseId(warehouseId);
					imwee.add(i);
				}
				imeWarehous.setWarehouseEmployees(imwee);
				imWarehouseDAO.merge(imeWarehous);

	        } catch (FormException fe) {
		    log.error("1111，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
		    log.error("2222，原因：" + ex.toString());
		    throw new Exception("2222，原因：" + ex.getMessage());
		} 	
	  }
    
}
