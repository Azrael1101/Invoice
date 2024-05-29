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
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.PrSupplier;
import tw.com.tm.erp.hbm.bean.PrSupplierMod;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.PrSupplierDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class PrSupplierService {

	private static final Log log = LogFactory.getLog(PrSupplierService.class);
	private PrSupplierDAO prSupplierDAO;
	private BuOrderTypeService buOrderTypeService;
	//private PrSupplier prSupplier;

	public void setPrSupplierDAO(PrSupplierDAO prSupplierDAO) {
		this.prSupplierDAO = prSupplierDAO;
	}
	
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}
	
	
	
	/**
	 * 資訊廠商查詢picker用的欄位
	 * 丟至前端的欄位
	 */
	public static final String[] GRID_SEARCH_SUPPLIER_FIELD_NAMES = {
		 "orderNo", "supplierNo", "supplier", "unifiedUnmbering"};//, "tel", "fax", "invoiceTypeCode", "isAssessSup", "name", "excuteInCharge"};
	//public static final String[] GRID_SEARCH_SUPPLIER_FIELD_NAMES = {
		//"supplierNo", "supplier", "tel", "fax", "invoiceTypeCode", "unifiedUnmbering", "orderNo", "isAssessSup", "name", "excuteInCharge"};
	//public static final String[] GRID_SEARCH_SUPPLIER_FIELD_NAMES = {
		//"orderNo", "supplierNo", "supplier", "unifiedUnmbering"};

	public static final String[] GRID_SEARCH_SUPPLIER_FIELD_DEFAULT_VALUES = {
		"", "", "", "", "", ""};//, "", "", "", ""};
	
	
	/**
	 * ajaxpicker按檢視返回選取的資料
	 * 
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public List<Properties> getSearchSelection(Map parameterMap)throws FormException, Exception {
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean,AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);

			List<Properties> result = AjaxUtils.getSelectedResults(timeScope,searchKeys);
			if (result.size() > 0) {
				log.info("20170807--------");
				pickerResult.put("result", result);
			}
			else{
				 Map messageMap = new HashMap();
				 messageMap.put("type"   , "ALERT");
				 messageMap.put("message", "請選擇檢視項目！");
				 messageMap.put("event1" , null);
				 messageMap.put("event2" , null);
				 resultMap.put("vatMessage",messageMap);
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
	 * 初始化 bean 額外顯示欄位
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exceptio
	 * * get取得parameterMap元素
	 * 實作set的類別出來
	 */
	public Map executePrSupplierInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		Object otherBean = parameterMap.get("vatBeanOther");
		String category = (String)PropertyUtils.getProperty(otherBean,"category");
		try {
			PrSupplierMod prSupplierMod = this.executeFindActualMod(parameterMap);
			log.info("category:" + category + "--------------");
			if(category.equals("master")){
				PrSupplier prSupplier = this.executeFindActual(parameterMap);
				prSupplierMod = this.copyMasterToPrSupplierMod(prSupplier,prSupplierMod);
			}

			resultMap.put("form", prSupplierMod);
			return resultMap;
		} catch (Exception ex) {
			log.error("資訊廠商初始化失敗，原因：" + ex.toString());
			throw new Exception("資訊廠商初始化失敗，原因：" + ex.toString());
		}
	}

	
	public PrSupplierMod executeNewPrSupplier(Map parameterMap) throws Exception{
		PrSupplierMod form = new PrSupplierMod();
		
		try{
			Object otherBean     = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			
			String tmpOrderNo = AjaxUtils.getTmpOrderNo(); // 產生暫存單號
	        form.setOrderNo(tmpOrderNo); // 寫入暫存單號  
	        form.setOrderTypeCode("PSM");
			form.setStatus(OrderStatus.SAVE );
			form.setCreatedBy(loginEmployeeCode);
			form.setCreationDate(new Date());
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setLastUpdateDate(new Date());
			
			saveModHead(form);
		
		}catch(Exception e){
			e.printStackTrace();
			log.error("建立新資訊廠商主檔失敗,原因:"+e.toString());
			throw new Exception("建立新資訊廠商主檔失敗,原因:"+e.toString());
		}

		return form;
	}
	
	public void saveModHead(PrSupplierMod prSupplierMod) throws Exception{
    	
    	try{
    		prSupplierDAO.save(prSupplierMod);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	    log.error("取得暫時單號儲存資訊廠商發生錯誤，原因：" + ex.toString());
    	    throw new Exception("取得暫時單號儲存資訊廠商發生錯誤，原因：" + ex.getMessage());
    	}	
    }
	
	/**
	 * 驗證主檔
	 * 
	 * @param parameterMap
	 * @throws ValidationErrorException
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */

	public void validateHead(Map parameterMap) throws Exception {

		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
	
			Object otherBean = parameterMap.get("vatBeanOther");
	
			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
			String categoryString = (String) PropertyUtils.getProperty(otherBean,"category");
	
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : 0;
			
			log.info("formId = " + formId);
			log.info("validate");
			// 驗證名稱
			String supplierNo = (String) PropertyUtils.getProperty(formBindBean,"supplierNo");
			String unifiedUnmbering = (String) PropertyUtils.getProperty(formBindBean,"unifiedUnmbering");
			String supplier = (String) PropertyUtils.getProperty(formBindBean,"supplier");
			String executeInCharge = (String) PropertyUtils.getProperty(formBindBean,"executeInCharge");
			
			if(!"master".equals(categoryString)){
				PrSupplier prSupplier = prSupplierDAO.findById(supplierNo);
				if(prSupplier != null){
					throw new ValidationErrorException("此廠商已建立，請勿重複建立！");
				}
			}
			
			if(!StringUtils.hasText(supplierNo)) {
				throw new ValidationErrorException("請輸入廠商代碼！");
			}else if (!StringUtils.hasText(unifiedUnmbering)) {
				throw new ValidationErrorException("請輸入統一編號！");
			}else if (!StringUtils.hasText(supplier)) {
				throw new ValidationErrorException("請輸入廠商名稱！");
			}else if (!StringUtils.hasText(executeInCharge)) {
				throw new ValidationErrorException("請輸入處理人！");
			}
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("追加單資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("公司資料存檔發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	/**
	 * 依formId取得資訊廠商主檔 in 送出
	 * 
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public PrSupplierMod executeFindActualMod(Map parameterMap)throws FormException, Exception {

		Object otherBean = parameterMap.get("vatBeanOther");
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		PrSupplierMod prSupplierMod = null;
		
	
		try {
			
			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");

			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;
			prSupplierMod = null == formId ? this.executeNewPrSupplier(parameterMap) : this.findById(formId);
			
	
			parameterMap.put("entityBean", prSupplierMod);

			
			return prSupplierMod;
		} catch (Exception e) {

			log.error("取得實際主檔失敗,原因:" + e.toString());
			e.printStackTrace();
			throw new Exception("取得實際主檔失敗,原因:" + e.toString());
		}
	}
	
	/**產生實體主檔**/
	public PrSupplier executeFindActual(Map parameterMap)throws FormException, Exception
	{

		Object formBindBean = parameterMap.get("vatBeanFormBind");
	//	Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");
		PrSupplier prSupplier = null;
	
		try {			

			String supplierNo    = (String)PropertyUtils.getProperty(otherBean, "supplierNo");
			log.info(supplierNo + "fsdfsfskrhweuklrfhsdkfhsdklfhsdklf");
			prSupplier = (PrSupplier) prSupplierDAO.findById(supplierNo);
			if(null == prSupplier){
				log.info("e04");
			}
			else
			{
				
			}
			return prSupplier;
		} catch (Exception e) {

			log.error("取得資訊廠商主檔失敗,原因:" + e.toString());
			throw new Exception("取得資訊廠商主檔失敗,原因:" + e.toString());
		} 
	}
	

	
	/**
     * 依據primary key為查詢條件，取得地點資料
     * 
     * @param locationId
     * @return BuLocation
     * @throws Exception
     */
    public PrSupplierMod findById(Long formId) throws Exception {

		try {
			log.info("測試1");
			PrSupplierMod prSupplierMod = (PrSupplierMod) prSupplierDAO.findByPrimaryKey(PrSupplierMod.class, formId);
		    return prSupplierMod;
		} catch (Exception ex) {
		    log.error("依據主鍵：" + formId + "查詢地點資料時發生錯誤，原因：" + ex.toString());
		    throw new Exception("依據主鍵：" + formId + "查詢地點資料時發生錯誤，原因："
			    + ex.getMessage());
		}
    }
	
    /**
     * 依據primary key為查詢條件
     * 
     * @param locationId
     * @return BuLocation
     * @throws Exception
     */
    public PrSupplier findBySupplierNo(String formId) throws Exception {

		try {
			PrSupplier prSupplier = (PrSupplier) prSupplierDAO.findByPrimaryKey(PrSupplier.class, formId);
		    return prSupplier;
		} catch (Exception ex) {
		    log.error("依據主鍵：" + formId + "查詢主檔時發生錯誤，原因：" + ex.toString());
		    throw new Exception("依據主鍵：" + formId + "查詢主檔時發生錯誤，原因："
			    + ex.getMessage());
		}
    }
    
	/**
	 * 前端資料塞入bean
	 * 
	 * @param parameterMap
	 * @return
	 */
	public void updatePrSupplierModBean(Map parameterMap)throws FormException, Exception {
		
		PrSupplierMod prSupplierMod = null;
		
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean    = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			prSupplierMod = (PrSupplierMod) parameterMap.get("entityBean");
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, prSupplierMod);
			log.info(prSupplierMod.getExecuteInCharge()+"20170808-----------");
			String resultMsg = modifyAjaxPrSupplierMod(prSupplierMod, employeeCode, brandCode);
			parameterMap.put("entityBean", prSupplierMod);
			parameterMap.put("resultMsg", resultMsg);
			
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			ex.printStackTrace();
			throw new Exception("資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}

	}
	
	/**暫存單號取實際單號並更新至銷貨單主檔及明細檔
     * @param soSalesOrderHead
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String modifyAjaxPrSupplierMod(PrSupplierMod prSupplierMod, String loginUser, String loginBrandCode)throws ObtainSerialNoFailedException, FormException, Exception {
    	try{
    	
			if (AjaxUtils.isTmpOrderNo(prSupplierMod.getOrderNo())) {
			    String serialNo = buOrderTypeService.getOrderSerialNo(loginBrandCode, prSupplierMod.getOrderTypeCode());
			    if (!serialNo.equals("unknow")) {
			    	prSupplierMod.setOrderNo(serialNo);
			    } else {
			    	throw new ObtainSerialNoFailedException("取得" + prSupplierMod.getOrderTypeCode() + "單號失敗！");
			    }
			}	
			modifyImItemDiscountMod( prSupplierMod, loginUser);
			return prSupplierMod.getOrderTypeCode() + "-" + prSupplierMod.getOrderNo() + "存檔成功！";
    	}catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			ex.printStackTrace();
			throw new Exception("資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
    }
    
    /**暫存單號取實際單號並更新至ImItemDiscountMod主檔
     * @param PoPurchaseOrderHead
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String modifyImItemDiscountMod(PrSupplierMod prSupplierMod, String loginUser)
	    throws ObtainSerialNoFailedException, FormException, Exception {
    	log.info("modifyPoPurchase loginUser="+loginUser);
    	prSupplierMod.setLastUpdatedBy(loginUser);
    	prSupplierMod.setLastUpdateDate(new Date());
    	prSupplierDAO.update(prSupplierMod);
        return prSupplierMod.getOrderTypeCode() + "-" + prSupplierMod.getOrderNo() + "存檔成功！";
    }
    
    /**
	 * 存檔
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXImItemDiscountMod(Map parameterMap) throws Exception {

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		String resultMsg = null;
		Date date = new Date();

		try {
			 
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");			 			 
			PrSupplierMod prSupplierMod = (PrSupplierMod)parameterMap.get("entityBean");
			String formAction = (String) PropertyUtils.getProperty(otherBean,"formAction");			
			
			//-----------------------------------------

			String formStatus = (String) PropertyUtils.getProperty(otherBean,"formStatus");

			String beforeStatus = prSupplierMod.getStatus();
			
			String nextStatus =  getStatus(formAction, beforeStatus, loginEmployeeCode);
			log.info("XXXXXXXXXXXXXXXXXXXXXX單據狀態XXXXXXXXXXXXXXXXXXXXXX");
			log.info("formStatus="+formStatus+"beforeStatus="+beforeStatus+"nextStatus="+nextStatus);
			
			if(OrderStatus.FORM_SUBMIT.equals(formAction)){
				
				PrSupplier prSupplier = prSupplierDAO.findById(prSupplierMod.getSupplierNo());
				if(prSupplier == null){
					log.info("save");
					prSupplier = new PrSupplier();
					prSupplier.setSupplierNo(prSupplierMod.getSupplierNo());
					prSupplier.setSupplier(prSupplierMod.getSupplier());
					prSupplier.setTel(prSupplierMod.getTel());
					prSupplier.setFax(prSupplierMod.getFax());
					prSupplier.setUnifiedUnmbering(prSupplierMod.getUnifiedUnmbering());
					prSupplier.setInvoiceTypeCode(prSupplierMod.getInvoiceTypeCode());
					prSupplier.setExcuteInCharge(prSupplierMod.getExecuteInCharge());
					prSupplier.setOrderNo(prSupplierMod.getOrderNo());
					prSupplier.setName(prSupplierMod.getName());
					prSupplier.setIsAssessSup(prSupplierMod.getIsAssessSup());
					prSupplierDAO.save(prSupplier);
					resultMsg = "Supplier：" + prSupplier.getSupplier() + "存檔成功！ 是否繼續新增？";
					resultMap.put("isUpdate", "0");
				}else{
					log.info("update");
					prSupplier.setSupplierNo(prSupplierMod.getSupplierNo());
					prSupplier.setSupplier(prSupplierMod.getSupplier());
					prSupplier.setTel(prSupplierMod.getTel());
					prSupplier.setFax(prSupplierMod.getFax());
					prSupplier.setUnifiedUnmbering(prSupplierMod.getUnifiedUnmbering());
					prSupplier.setInvoiceTypeCode(prSupplierMod.getInvoiceTypeCode());
					prSupplier.setExcuteInCharge(prSupplierMod.getExecuteInCharge());
					prSupplier.setOrderNo(prSupplierMod.getOrderNo());
					prSupplier.setName(prSupplierMod.getName());
					prSupplier.setIsAssessSup(prSupplierMod.getIsAssessSup());
					prSupplierDAO.merge(prSupplier);
					resultMsg = "Supplier：" + prSupplier.getSupplier() +  "更新成功";
					resultMap.put("isUpdate", "1");
				}
			}
				
			prSupplierMod.setStatus(nextStatus);
			prSupplierMod.setLastUpdatedBy(loginEmployeeCode);
			prSupplierMod.setLastUpdateDate(new Date());
			
			//save or update
			if(prSupplierMod.getHeadId() == null){
				prSupplierMod.setCreatedBy(loginEmployeeCode);
				prSupplierMod.setCreationDate(date);
				prSupplierDAO.save(prSupplierMod);
			}else {
				log.info("測試2");
				prSupplierDAO.update(prSupplierMod);
			}
					
			resultMsg = "商品折扣:存檔成功！ 是否繼續調整？";

			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", prSupplierMod);
			resultMap.put("vatMessage", msgBox);

			return resultMap;

		} catch (Exception ex) {
			log.error("維護單存檔時發生錯誤，原因：" + ex.toString());
			ex.printStackTrace();
			throw new Exception("維護單存檔失敗，原因：" + ex.toString());
		}
	}
	
	private String getStatus(String formAction,String beforeStatus,String loginEmployeeCode) {
		
		log.info("取得下個狀態");
		
		String status = null;
		//BuEmployee bump = buEmployeeDAO.findById(loginEmployeeCode);
				

		log.info("登入工號:"+loginEmployeeCode);
		//log.info("是否為主管:"+bump.getIsDepartmentManager());
		log.info("送出前狀態:"+beforeStatus);
		//若動作為送出
		if(OrderStatus.FORM_SUBMIT.equals(formAction)){
			//若送出前狀態為暫存或駁回
			if(beforeStatus.equals(OrderStatus.SAVE)||beforeStatus.equals(OrderStatus.REJECT)){
				status = OrderStatus.SIGNING;
			}
			//若送出前狀態為簽核中且不為部門主管
			/*else if(beforeStatus.equals(OrderStatus.SIGNING)&& bump.getIsDepartmentManager()==null){
				status = OrderStatus.SIGNING;
			}*/
			//其他則為簽核完成
			else 
			{
				status = OrderStatus.FINISH;
			}
		}
		//動作為暫存
		else if(OrderStatus.FORM_SAVE.equals(formAction)){
			status = OrderStatus.SAVE;
		}
		//動作為作廢
		else if(OrderStatus.FORM_VOID.equals(formAction)){
			status = OrderStatus.VOID;
		}
		log.info("送出後狀態:"+status);
		return status;
	}
	
	public Map searchExecuteInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean,"loginEmployeeCode");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");

			//resultMap.put("multiList", multiList);
			return resultMap;
		} catch (Exception ex) {
			log.error("資訊廠商查詢初始化失敗，原因：" + ex.toString());
			throw new Exception("資訊廠商查詢初始化失敗，原因：" + ex.toString());
		}
	}
	
	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();

			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			// ======================帶入Head的值=========================

	  	    String orderNo = httpRequest.getProperty("orderNo");
	  	    String supplierNo = httpRequest.getProperty("supplierNo");
	  	    String supplier = httpRequest.getProperty("supplier");
	  	    String unifiedUnmbering = httpRequest.getProperty("unifiedUnmbering");
	  	

	  	    //log.info("orderNo:"+orderNo + " supplierNo: " + supplierNo + " supplier: " + supplier + " unifiedUnmbering: " + unifiedUnmbering);
  			HashMap findObjs = new HashMap();
  			HashMap map = new HashMap();

  			findObjs.put(" and model.orderNo like :orderNo","%"+orderNo+"%");
  			findObjs.put(" and model.supplierNo like :supplierNo","%"+supplierNo+"%");
		    findObjs.put(" and model.supplier like :supplier","%"+supplier+"%");
		    findObjs.put(" and model.unifiedUnmbering like :unifiedUnmbering","%"+unifiedUnmbering+"%");

			// ======================取得頁面所需資料===========================
	  	    Map prSupplierMap = prSupplierDAO.search( "PrSupplier as model", findObjs, "order by supplierNo desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	  	    
	  	    List<PrSupplier> prSuppliers = (List<PrSupplier>) prSupplierMap.get(BaseDAO.TABLE_LIST); 

	  	    
	  	    log.info("PrSuppliers.size"+ prSuppliers.size());	
		    if (prSuppliers != null && prSuppliers.size() > 0) {
		    	
		    	// 設定額外欄位
		    	//this.setModLineOtherColumn(prSuppliers);
		    	
		    	Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		    	Long maxIndex = (Long)prSupplierDAO.search("PrSupplier as model", "count(*) as rowCount" ,findObjs, "order by supplierNo desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		    	
		    	
		    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_SUPPLIER_FIELD_NAMES, GRID_SEARCH_SUPPLIER_FIELD_DEFAULT_VALUES,prSuppliers, gridDatas, firstIndex, maxIndex));
		     }else {
		    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_SUPPLIER_FIELD_NAMES, GRID_SEARCH_SUPPLIER_FIELD_DEFAULT_VALUES,map,gridDatas));
		     }
		
		    return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的資訊廠商明細發生錯誤，原因：" + ex.toString());
			ex.printStackTrace();
			throw new Exception("載入頁面顯示的資訊廠商明細失敗！");
		}
	}
	
	/**
	 * 資訊廠商查詢結果存檔
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 * @William說明
	 */
	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_SUPPLIER_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	
	public PrSupplierMod copyMasterToPrSupplierMod(PrSupplier prSupplier,PrSupplierMod prSupplierMod)throws FormException, Exception
	{
	
		try {
			prSupplierMod.setSupplierNo(prSupplier.getSupplierNo());
			prSupplierMod.setSupplier(prSupplier.getSupplier());
			prSupplierMod.setTel(prSupplier.getTel());
			prSupplierMod.setFax(prSupplier.getFax());
			prSupplierMod.setInvoiceTypeCode(prSupplier.getInvoiceTypeCode());
			prSupplierMod.setUnifiedUnmbering(prSupplier.getUnifiedUnmbering());
			prSupplierMod.setName(prSupplier.getName());
			prSupplierMod.setExecuteInCharge(prSupplier.getExcuteInCharge());
			prSupplierMod.setOrderNo(prSupplier.getOrderNo());
			prSupplierMod.setIsAssessSup(prSupplier.getIsAssessSup());
			
			return prSupplierMod;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("複製主檔至異動檔失敗,原因:" + e.toString());
			throw new Exception("複製主檔至異動檔失敗,原因:" + e.toString());
		} 
	}
}
