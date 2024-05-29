package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.DateValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopEmployee;
import tw.com.tm.erp.hbm.bean.BuShopEmployeeId;
import tw.com.tm.erp.hbm.bean.ImItemDiscount;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.PosItemDiscount;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.BuShopEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.PosExportDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.UserUtils;

public class BuShopService {

    private static final Log log = LogFactory.getLog(BuShopService.class);

    private BuShopDAO buShopDAO;
    
    private BuShopEmployeeDAO buShopEmployeeDAO;
    
    private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
    
    private BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO;
    
    private ImWarehouseDAO imWarehouseDAO;
    
    private PosExportDAO posExportDAO;
    
	public void setBuShopDAO(BuShopDAO buShopDAO) {
	this.buShopDAO = buShopDAO;
    }
    
    public void setBuShopEmployeeDAO(BuShopEmployeeDAO buShopEmployeeDAO) {
	this.buShopEmployeeDAO = buShopEmployeeDAO;
    }
    
    public void setBuEmployeeWithAddressViewDAO(
	    BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
	this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
    }
    
    public void setBuSupplierWithAddressViewDAO(
	    BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO) {
	this.buSupplierWithAddressViewDAO = buSupplierWithAddressViewDAO;
    }
    
    public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
	this.imWarehouseDAO = imWarehouseDAO;
    }
    
    public void setPosExportDAO(PosExportDAO posExportDAO) {
		this.posExportDAO = posExportDAO;
	}
    
    public String saveOrUpdateBuShop(BuShop shop, String loginUser, boolean isNew)
	    throws FormException, Exception {
	try {
	    doBuShopValidate(shop);
	    shop.setEnable(("N".equals(shop.getEnable()) ? "N" : "Y"));
	    if (isNew) {
		BuShop shopPO = findById(shop.getShopCode());
		if (shopPO == null) {
		    insertBuShop(shop, loginUser);
		} else {
		    throw new ValidationErrorException("專櫃代號：" + shop.getShopCode()+ "已經存在，請勿重複建立！");
		}
	    } else {
		modifyBuShop(shop, loginUser);
	    }

	    return "專櫃代號：" + shop.getShopCode() + "存檔成功！";
	} catch (FormException fe) {
	    log.error("專櫃資料存檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("專櫃資料存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("專櫃資料存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 新增至BuShop
     * 
     * @param saveObj
     * @param loginUser
     */
    private void insertBuShop(BuShop saveObj, String loginUser) {

	saveObj.setCreatedBy(loginUser);
	saveObj.setCreationDate(new Date());
	saveObj.setLastUpdatedBy(loginUser);
	saveObj.setLastUpdateDate(new Date());
	buShopDAO.save(saveObj);
    }

    /**
     * 修改BuShop
     * 
     * @param saveObj
     * @param loginUser
     */
    private void modifyBuShop(BuShop updateObj, String loginUser) {

	updateObj.setLastUpdatedBy(loginUser);
	updateObj.setLastUpdateDate(new Date());
	buShopDAO.update(updateObj);
    }
    
    /**
     * 檢核專櫃資料
     * 
     * @param shop
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void doBuShopValidate(BuShop shop) throws ValidationErrorException, NoSuchObjectException, Exception {

	if (!StringUtils.hasText(shop.getShopCode())) {
	    throw new ValidationErrorException("請輸入專櫃代號！");
	} else {
	    shop.setShopCode(shop.getShopCode().trim().toUpperCase());
	}

	if (!StringUtils.hasText(shop.getShopCName())) {
	    throw new ValidationErrorException("請輸入專櫃中文名稱！");
	} else {
	    shop.setShopCName(shop.getShopCName().trim());
	}
	
	if (!StringUtils.hasText(shop.getSalesWarehouseCode())) {
	    throw new ValidationErrorException("請輸入銷售倉庫代號！");
	} else {
	    shop.setSalesWarehouseCode(shop.getSalesWarehouseCode().trim().toUpperCase());
	    if(imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, shop.getSalesWarehouseCode()) == null){
		throw new NoSuchObjectException("銷售倉庫代號："+ shop.getSalesWarehouseCode() + "不存在，請輸入正確的倉庫代號！");
	    }
	}
	
	if (StringUtils.hasText(shop.getSupplierCode())) {
	    shop.setSupplierCode(shop.getSupplierCode().trim().toUpperCase());
	    if(buSupplierWithAddressViewDAO.findByBrandCodeAndSupplierCode(shop.getBrandCode(), shop.getSupplierCode()) == null){
		throw new NoSuchObjectException("廠商代號："+ shop.getSupplierCode() + "不存在，請輸入正確的廠商代號！");
	    }	    
	}	

	if (shop.getScheduleDate()== null) {
	    throw new ValidationErrorException("請輸入預計啟用日！");
	} else {
	    shop.setScheduleDate(shop.getScheduleDate());
	}
	
	String nonBussinessDate = shop.getReserve5();
	if (StringUtils.hasText(nonBussinessDate)){
	    String[] nonBussinessDateArray = StringTools.StringToken(nonBussinessDate, "#");
	    if(nonBussinessDateArray != null && nonBussinessDateArray.length > 0){
		StringBuffer result = new StringBuffer();
	        for(int i = 0; i < nonBussinessDateArray.length; i++){
	            nonBussinessDateArray[i] = nonBussinessDateArray[i].trim();
		    DateValidator dateValidator = DateValidator.getInstance();
		    if(!dateValidator.isValid(nonBussinessDateArray[i], DateUtils.C_DATA_PATTON_YYYYMMDD, true)){
		        throw new ValidationErrorException("非營業日：" + nonBussinessDateArray[i] + "格式錯誤！");
		    }
		    result.append(nonBussinessDateArray[i]);
		    if(i != nonBussinessDateArray.length -1){
		        result.append("#");
		    }
	        }
	        shop.setReserve5(result.toString());
	    }	   
	 }
    }
    
    /**
     * 依據primary key為查詢條件，取得專櫃代號資料
     * 
     * @param shopCode
     * @return BuShop
     * @throws Exception
     */
    public BuShop findById(String shopCode) throws Exception {
	
	try {
	    shopCode = shopCode.trim().toUpperCase();
	    return buShopDAO.findById(shopCode);
	} catch (Exception ex) {
	    log.error("依據專櫃代號：" + shopCode + "查詢專櫃資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據專櫃代號：" + shopCode + "查詢專櫃資料時發生錯誤，原因："
		    + ex.getMessage());
	}
    }

    /**
     * 依據專櫃資料查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List<BuShop> find(HashMap conditionMap) throws Exception{
	
        try {	    
            String shopCode = (String) conditionMap.get("shopCode");
    	    String shopCName = (String) conditionMap.get("shopCName");
    	    String place = (String) conditionMap.get("place");	
	    conditionMap.put("shopCode", shopCode.trim().toUpperCase());
	    conditionMap.put("shopCName", shopCName.trim());
	    conditionMap.put("place", place.trim());	    
	    
	    return buShopDAO.find(conditionMap);
	} catch (Exception ex) {
	    log.error("查詢專櫃資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢專櫃資料時發生錯誤，原因：" + ex.getMessage());
	}
    }

    public List<BuShop> find(String ids) {
	return buShopDAO.find(ids);
    }
    
    /**
     * 依據專櫃代號查詢所屬專櫃人員
     * 
     * @param shopCode
     * @return List
     * @throws Exception
     */
    public List<BuShopEmployee> findEmployeeByShopCode(String shopCode) throws Exception{
	
	try {    	    
	    return buShopEmployeeDAO.findEmployeeByShopCode(shopCode);
	} catch (Exception ex) {
	    log.error("查詢專櫃人員資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢專櫃人員資料時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    public void refreshShopEmployee(List refreshObjs) {
	
	for (int i = 0; i < refreshObjs.size(); i++) {
	    BuShopEmployee shopEmployee = (BuShopEmployee)refreshObjs.get(i);
	    BuShopEmployeeId id = shopEmployee.getId();
	    shopEmployee.setEmployeeName(UserUtils.getUsernameByEmployeeCode(id.getEmployeeCode()));
        } 
    }
    
    /**
     * 檢核專櫃人員資料
     * 
     * @param shopEmployees
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void checkBuShopEmployee(List shopEmployees)
	    throws ValidationErrorException, NoSuchObjectException {

	HashMap map = new HashMap();
	for (int i = 0; i < shopEmployees.size(); i++) {
	    BuShopEmployee shopEmployee = (BuShopEmployee) shopEmployees.get(i);
	    BuShopEmployeeId id = shopEmployee.getId();

	    if (!StringUtils.hasText(id.getEmployeeCode())) {
		throw new ValidationErrorException("請輸入第" + (i + 1) + "項明細的工號！");
	    } else {
		id.setEmployeeCode(id.getEmployeeCode().trim().toUpperCase());
		BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO
			.findById(id.getEmployeeCode());
		if (employeeWithAddressView == null) {
		    throw new NoSuchObjectException("查無第" + (i + 1) + "項明細的工號！");
		}
	    }

	    if (map.get(id.getEmployeeCode()) != null) {
		throw new ValidationErrorException("第" + (i + 1) + "項明細的工號重複！");
	    } else {
		map.put(id.getEmployeeCode(), id.getEmployeeCode());
	    }
	}
    }
    
    /**
     * 將專櫃人員資料新增或更新至專櫃人員資料檔
     * 
     * @param shopEmployees
     * @param actualShopCode
     * @param loginUser
     * @return String
     * @throws FormException
     * @throws Exception
     */
    public String saveOrUpdateBuShopEmployee(List<BuShopEmployee> shopEmployees, String actualShopCode, 
	    String loginUser) throws FormException, Exception {

	try {
	    checkBuShopEmployee(shopEmployees);
	    List<BuShopEmployee> actualSaveShopEmployees = produceNewBuShopEmployees(shopEmployees, actualShopCode, loginUser);
	       
	    List<BuShopEmployee> currentShopEmployees = buShopEmployeeDAO.findEmployeeByShopCode(actualShopCode);;	    
	    if(currentShopEmployees != null && currentShopEmployees.size() > 0){
	        for(BuShopEmployee currentShopEmployee : currentShopEmployees){
	            buShopEmployeeDAO.delete(currentShopEmployee);
	        }
	    }
	    for(BuShopEmployee actualSaveShopEmployee : actualSaveShopEmployees){
		buShopEmployeeDAO.save(actualSaveShopEmployee);
	    }
	    
	    return "存檔成功！";
	} catch (FormException fe) {
	    log.error("專櫃人員資料存檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("專櫃人員資料存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("專櫃人員資料存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 產生實際存檔的專櫃人員資料
     * 
     * @param shopEmployees
     * @param actualShopCode
     * @param loginUser
     * @return List
     */
    private List<BuShopEmployee> produceNewBuShopEmployees(
	    List shopEmployees, String actualShopCode,
	    String loginUser) {

	List<BuShopEmployee> actualSaveShopEmployees = new ArrayList(0);
	for (int i = 0; i < shopEmployees.size(); i++) {
	    BuShopEmployee shopEmployee = (BuShopEmployee) shopEmployees.get(i);
	    String origShopCode = shopEmployee.getId().getShopCode();
		    
	    if (origShopCode == null) {
		shopEmployee.getId().setShopCode(actualShopCode);
		shopEmployee.setEnable(("N".equals(shopEmployee.getEnable()) ? "N" : "Y"));
		shopEmployee.setCreatedBy(loginUser);
		shopEmployee.setCreationDate(new Date());
		shopEmployee.setLastUpdatedBy(loginUser);
		shopEmployee.setLastUpdateDate(new Date());
		shopEmployee.setIndexNo(1L);

		actualSaveShopEmployees.add(shopEmployee);
	    } else {
		BuShopEmployee actualSaveShopEmployee = new BuShopEmployee();
		BeanUtils.copyProperties(shopEmployee,actualSaveShopEmployee);
		actualSaveShopEmployee.getId().setShopCode(actualShopCode);
		actualSaveShopEmployee.setEnable(("N".equals(actualSaveShopEmployee.getEnable()) ? "N" : "Y"));
		actualSaveShopEmployee.setLastUpdatedBy(loginUser);
		actualSaveShopEmployee.setLastUpdateDate(new Date());
		actualSaveShopEmployee.setIndexNo(1L);

		actualSaveShopEmployees.add(actualSaveShopEmployee);
	    }
	}

	return actualSaveShopEmployees;
    }
    /**
     * 依據庫別查詢店櫃等級
     * @param warehouseCode
     * @return 
     * @throws FormException
     * @throws NoSuchDataException
     */
	public String findShopLevelbyWarehouseCode(String warehouseCode) throws FormException, NoSuchDataException{
		String result= new String("");
		if(StringUtils.hasText(warehouseCode)){
			List <BuShop> buShops = buShopDAO.findByProperty("BuShop", "salesWarehouseCode", warehouseCode);
			if(buShops.size() == 1){
				BuShop buShop = buShops.get(0);
				result = buShop.getShopLevel();
			}else if(buShops.size() > 1){
				throw new NoSuchDataException("收貨倉("+warehouseCode+")為專櫃類型，查詢專櫃資料時，發現超過一個以上之店櫃使用此庫別，請確認");
			}else{
				throw new NoSuchDataException("收貨倉("+warehouseCode+")為專櫃類型，查無相對應之專櫃資料，請確認");
			}	
		}else{
			throw new NoSuchDataException("轉入庫別代碼為空白，請確認");
		}
		return result;
	}
	
	 /**
     * 取得部門下拉
     * @param httpRequest
     * @return
     * @throws Exception
     */
	public List<Properties> getAJAXShop(Properties httpRequest)throws Exception{
		List list = new ArrayList();
		Properties properties = new Properties();
		try{
			String brandCode = httpRequest.getProperty("brandCode");
			String department = httpRequest.getProperty("department");
			
			log.info("brandCode = " + brandCode);
			log.info("department = " + department);
			
			List allShop = buShopDAO.findShopByProperty( brandCode, department, "Y" );
			allShop = AjaxUtils.produceSelectorData(allShop, "shopCode", "shopCName", true, true);
	    	properties.setProperty("allShop", AjaxUtils.parseSelectorData(allShop));
	    	
			list.add(properties);
		}catch(Exception e){
			log.error("依部門取得專櫃下拉，原因：" + e.toString());
			throw new Exception("依部門取得專櫃下拉，原因：" + e.getMessage());
		}
		return list;
	}
	
	/**
	 * 依預計啟用日自動狀專櫃狀態設為啟用，For每日排程使用.
	 * @author T96640
	 */
	public void executeAutoChangeEnableValue(){
	    List<BuShop> buShopList = buShopDAO.findByProperty("BuShop", "enable", "N");
	    if(buShopList != null && buShopList.size()>0){
		for(int i=0;i<buShopList.size();i++){
		    BuShop buShop = buShopList.get(i);
		    if(buShop.getScheduleDate() != null){
			String execDate = DateUtils.format(DateUtils.addDays(new Date(), 1), DateUtils.C_DATA_PATTON_YYYYMMDD);
			String scheduleDate = DateUtils.format(buShop.getScheduleDate(), DateUtils.C_DATA_PATTON_YYYYMMDD);
			//若預計啟用日等於隔天則修改狀態=Y
			if(scheduleDate.equals(execDate)){
			    buShop.setEnable("Y");
			    this.modifyBuShop(buShop, "SYSTEM");
			}
		    }

		}
	    }
	}
	
	/**
     * 透過傳遞過來的參數來做BuShop下傳
     * @param parameterMap
     * @throws Exception
     */
    public Long executePosExport(HashMap parameterMap) throws Exception{
    	log.info("executePosExport");
    	Long responseId = -1L;
    	Long numbers = 0L;
    	
    	//一、解析程式需要排程下傳或是即時下傳
    	Long batchId = (Long)parameterMap.get("BATCH_ID");
		String uuId = posExportDAO.getDataId();
		
		//二、下傳程式至POS_ITEM_DISCOUNT (產生DataId , ResponseId)
		if(null == batchId || batchId <= 0){
			//輸入搜尋條件(排程)
			parameterMap.put("brandCode", parameterMap.get("BRAND_CODE"));
			parameterMap.put("dataDate", DateUtils.format( (Date)parameterMap.get("DATA_DATE_STRAT"), DateUtils.C_DATA_PATTON_YYYYMMDD));
			parameterMap.put("dataDateEnd", DateUtils.format( (Date)parameterMap.get("DATA_DATE_END"), DateUtils.C_DATA_PATTON_YYYYMMDD));
			List results = buShopDAO.findShopByCondition(parameterMap);
			if(results != null && results.size() >= 0){
		        for (Object result : results) {
		        	ImItemDiscount imItemDiscount = (ImItemDiscount)result;
			        PosItemDiscount posItemDiscount = new PosItemDiscount();
			        BeanUtils.copyProperties(imItemDiscount, posItemDiscount);
			        posItemDiscount.setDataId(uuId);
			        posItemDiscount.setAction("I");
			        posExportDAO.save(posItemDiscount);
		        }
			}
		}else{
			//尋找PosCustomer中此dataID有哪些需求資料
			parameterMap.put("brandCode", parameterMap.get("BRAND_CODE"));
			List results = buShopDAO.findShopByCondition(parameterMap);
			if(results != null && results.size() >= 0){
				log.info("results.size = " + results.size());
		        for (Object result : results) {
		        	ImItemDiscount imItemDiscount = (ImItemDiscount)result;
			        PosItemDiscount posItemDiscount = new PosItemDiscount();
			        BeanUtils.copyProperties(imItemDiscount, posItemDiscount);
			        posItemDiscount.setDataId(uuId);
			        posItemDiscount.setAction("I");
			        posExportDAO.save(posItemDiscount);
		        }
			}
		}

		//更新新的DATA_ID做回傳
		parameterMap.put("DATA_ID", uuId);
		parameterMap.put("NUMBERS", numbers);
		responseId = posExportDAO.executeCommand(parameterMap);
		return responseId;
    }
    
    /**
     * 依據品牌代號、啟用狀態查詢出專櫃代號
     * 
     * @param brandCode
     * @param isEnable
     * @return List
     */
    public List findShopByBrandAndEnable(String brandCode, String isEnable) {
    	return buShopDAO.findShopByBrandAndEnable(brandCode, isEnable);
    }
    
}
