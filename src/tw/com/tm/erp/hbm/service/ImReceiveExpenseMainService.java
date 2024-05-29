package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.ImReceiveExpense;
import tw.com.tm.erp.hbm.bean.ImReceiveExpenseMod;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveExpenseDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;


/**
 * 進貨單費用 Service 
 * 
 * @author MyEclipse Persistence Tools
 */
public class ImReceiveExpenseMainService {
	private static final Log log = LogFactory.getLog(ImReceiveExpenseMainService.class);
	private ImReceiveExpenseDAO imReceiveExpenseDAO;
	//private BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO ;
	private BuBasicDataService buBasicDataService; 
	
	private ImReceiveHeadDAO imReceiveHeadDAO; 
	/**
	 * save and update
	 * 
	 * @param modifyObj
	 * @return
	 */
	public String create(ImReceiveExpense modifyObj) {
		if (null != modifyObj) {
			if (modifyObj.getLineId() == null) {
				return save(modifyObj);
			} else {
				return update(modifyObj);
			}
		}
		return "";
	}

	/**
	 * save
	 * 
	 * @param saveObj
	 * @return
	 */
	public String save(ImReceiveExpense saveObj) {
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imReceiveExpenseDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 */
	public String update(ImReceiveExpense updateObj) {
		updateObj.setLastUpdateDate(new Date());
		imReceiveExpenseDAO.update(updateObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * find by pk
	 * 
	 * @param headId
	 * @return
	 */
	public ImReceiveExpense findById(Long headId) {
		ImReceiveExpense re = (ImReceiveExpense) imReceiveExpenseDAO
				.findByPrimaryKey(ImReceiveExpense.class, headId);
		if (null == re)
			re = new ImReceiveExpense();
		return re;
	}
	
	public void doValidate(ImReceiveHead head,ImReceiveExpense item) throws Exception{
		log.info("doValidate" );
		// item.getSupplierCode() 客戶代號
		BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(head.getBrandCode(),item.getSupplierCode());
		if(null == buSWAV ){
			throw new Exception("客戶代號有問題 : " + item.getIndexNo() );
		}	
	}

	public void setImReceiveExpenseDAO(
			ImReceiveExpenseDAO imReceiveExpenseDAO) {
		this.imReceiveExpenseDAO = imReceiveExpenseDAO;
	}

	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}

	public void setImReceiveHeadDAO(ImReceiveHeadDAO imReceiveHeadDAO) {
		this.imReceiveHeadDAO = imReceiveHeadDAO;
	}
	/**
	 * LINE 頁面顯示
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public List<ImReceiveExpense> findPageLine(Long headId, int startPage, int pageSize) {
		log.info("findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		return imReceiveExpenseDAO.findPageLine(headId, startPage, pageSize);
	}

	/**
	 * 取得GRID最後一筆
	 * 
	 * @param headId
	 * @return
	 */
	public Long findPageLineMaxIndex(Long headId) {
		log.info("findPageLineMaxIndex");
		return imReceiveExpenseDAO.findPageLineMaxIndex(headId);
	}
	
	
	/**
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public ImReceiveExpense findLine(String headId, String lineId) {
		log.info("findLine headId=" + headId + ",lineId=" + lineId);
		if (StringUtils.hasText(headId) && StringUtils.hasText(lineId)) {
			Long hId = 0L;
			Long lId = 0L;
			try {
				hId = Long.valueOf(headId);
				lId = Long.valueOf(lineId);
			} catch (Exception ex) {

			}
			return imReceiveExpenseDAO.findLine(hId, lId);
		}
		return null;
	}

	/**
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public ImReceiveExpense findLine(Long headId, Long lineId) {
		log.info("findLine headId=" + headId + ",lineId=" + lineId);
		return imReceiveExpenseDAO.findLine(headId, lineId);
	}
	//----------------------------------------------------------------------------
	//----------------------------------------------------------------------------
	//----------------------------------------------------------------------------
	private BuOrderTypeService           buOrderTypeService;
	private BuBrandService               buBrandService;
	private BuCommonPhraseLineDAO        buCommonPhraseLineDAO;
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}
	 public Map executeInitial(Map parameterMap) throws Exception{
	    	log.info("executeInitial");
	    	Object otherBean         = parameterMap.get("vatBeanOther");
	    	HashMap resultMap = new HashMap();
	    	try{
	    		String orderTypeCode     = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
	    		String loginBrandCode    = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    		BuBrand     buBrand     = buBrandService.findById( loginBrandCode );
	    		BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(loginBrandCode, orderTypeCode ) );
	    		
	    		ImReceiveExpenseMod imReceiveExpenseMod = this.executeFindActual(parameterMap);
	    		List<BuOrderType>        allOrderType     = buOrderTypeService.findOrderbyType(imReceiveExpenseMod.getBrandCode(), "IMR");
	    		Map multiList = new HashMap(0);
	    		List allExpenseCode = buCommonPhraseLineDAO.findEnableLineById("Expense");
	    		multiList.put("allExpenseCode", AjaxUtils.produceSelectorData(allExpenseCode, "lineCode", "name", true, true));
	    		multiList.put("allOrderType", AjaxUtils.produceSelectorData(allOrderType, "lineCode", "name", true, true));
	    		log.info("allExpenseCode"+allExpenseCode);
	    		//resultMap.put("branchCode",        buBrand.getBuBranch().getBranchCode() );	// 2->T2
	    		resultMap.put("typeCode",          buOrderType.getTypeCode() );	// IMR->進貨 or RR->進貨退回
	    		resultMap.put("form", imReceiveExpenseMod);
	    		resultMap.put("resultMap",multiList);
		    	return resultMap;
	    	}catch (Exception ex) {
	    		log.error("進貨單費用初始化失敗，原因：" + ex.toString());
	    		throw new Exception("進貨單費用初始化失敗，原因：" + ex.toString());
			}

	 }
	 
	 public ImReceiveExpenseMod createNewImReceiveExpense(Map parameterMap,BuBrand buBrand) throws Exception {
	    	log.info("createNewImReceiveExpense");
	    	Object otherBean = parameterMap.get("vatBeanOther");		
	    	String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    	String brandCode     = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    	try{
	    		log.info("brandCode2"+brandCode);
	    		ImReceiveExpenseMod form = new ImReceiveExpenseMod();
	    		form.setBrandCode(brandCode);	
	    		form.setCreatedBy			 (loginEmployeeCode);
	    		form.setCreationDate		 (DateUtils.parseDate(DateUtils.format(new Date())));
	    		form.setLastUpdatedBy		 (loginEmployeeCode);
	    		form.setLastUpdateDate		 (DateUtils.parseDate(DateUtils.format(new Date())));	 
	    		//saveTmp(form);
	    		return form;
	    	}catch (Exception ex) {
	    		log.error("產生新進貨費用單失敗，原因：" + ex.toString());
	    		throw new Exception("產生新進貨費用單失敗！");
	    	}
	 }
	 
	 public String saveTmp(ImReceiveExpenseMod saveObj) throws FormException, Exception {
			//log.info("ImReceiveHead.saveTmp");
			saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
			saveObj.setLastUpdateDate(new Date());
			saveObj.setCreationDate(new Date());
			imReceiveExpenseDAO.save(saveObj);
			return MessageStatus.SUCCESS;
		}
	 	
	public ImReceiveExpenseMod executeFindActual(Map parameterMap)throws FormException, Exception {

	Object otherBean = parameterMap.get("vatBeanOther");
	ImReceiveExpenseMod imReceiveExpenseMod = null;

	try {			
		String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
		Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;
		String brandCode    = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
		log.info("brandCode1"+brandCode);
		BuBrand     buBrand     = buBrandService.findById( brandCode );
		imReceiveExpenseMod = (ImReceiveExpenseMod) (null == formId ? this.createNewImReceiveExpense(parameterMap,buBrand) : this.findModById(formId));
		
		parameterMap.put("entityBean", imReceiveExpenseMod);
		log.info("executefind:"+imReceiveExpenseMod.getHeadId());
		
		return imReceiveExpenseMod;
	} catch (Exception e) {

		log.error("取得實際主檔失敗,原因:" + e.toString());
		throw new Exception("取得實際主檔失敗,原因:" + e.toString());
		}
	}
	/**
	 * find by pk
	 * 
	 * @param headId
	 * @return
	 */
	public ImReceiveExpenseMod findModById(Long headId) {
		ImReceiveExpenseMod re = (ImReceiveExpenseMod) imReceiveExpenseDAO
				.findByPrimaryKey(ImReceiveExpense.class, headId);
		if (null == re)
			re = new ImReceiveExpenseMod();
		return re;
	}
	/**
	 * 前端資料塞入bean
	 * 
	 * @param parameterMap
	 * @return
	 */
	public void updateExpenseModBean(Map parameterMap)
			throws FormException, Exception {
		// TODO Auto-generated method stub

		ImReceiveExpenseMod imReceiveExpenseMod = null;
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			// Object formLinkBean = parameterMap.get("vatBeanFormLink");
			// Object otherBean = parameterMap.get("vatBeanOther");
			
				
			imReceiveExpenseMod = (ImReceiveExpenseMod) parameterMap.get("entityBean");

			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imReceiveExpenseMod);
			
			parameterMap.put("entityBean", imReceiveExpenseMod);
		}catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}

	}
	
	/**
	 * 存檔
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXExpenseMod(Map parameterMap) throws Exception {

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		String resultMsg = null;
		Date date = new Date();

		try {

		//	 Object formBindBean = parameterMap.get("vatBeanFormBind");
		//	 Object formLinkBean = parameterMap.get("vatBeanFormLink");
			 Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			ImReceiveExpenseMod imReceiveExpenseMod = (ImReceiveExpenseMod) parameterMap.get("entityBean");
			log.info("arrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
			String formAction = (String) PropertyUtils.getProperty(otherBean,"formAction");
			log.info("arrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
			//-----------------------------------------
			if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
				imReceiveExpenseMod.setCreatedBy(loginEmployeeCode);
				imReceiveExpenseMod.setCreationDate(date);
				imReceiveExpenseMod.setLastUpdateDate(date);
				imReceiveExpenseDAO.save(imReceiveExpenseMod);
				//--------------------------
				log.info("存檔之後~~");
				ImReceiveHead OriginalHead =  imReceiveHeadDAO.findByIdentification(brandCode,imReceiveExpenseMod.getOrderTypeCode(),imReceiveExpenseMod.getOrderNo());
				log.info("OriginalHead=="+OriginalHead.getHeadId()+OriginalHead.getImReceiveExpenses());
				Long OriHeadId = OriginalHead.getHeadId();
				
				List<ImReceiveExpense> imReceiveExpense = OriginalHead.getImReceiveExpenses();
				log.info("ListReceive"+imReceiveExpense.size());
				
				
				if(null != OriginalHead){
					ImReceiveExpense imReceiveExpense2 = new ImReceiveExpense();
					imReceiveExpense2.setImReceiveHead(OriginalHead);
					imReceiveExpense2.setBillDate(imReceiveExpenseMod.getBillDate());
					imReceiveExpense2.setSupplierCode(imReceiveExpenseMod.getSupplierCode());
					imReceiveExpense2.setSupplierName(imReceiveExpenseMod.getSupplierName());
					imReceiveExpense2.setExpenseCode(imReceiveExpenseMod.getExpenseCode());
					imReceiveExpense2.setForeignAmount(imReceiveExpenseMod.getForeignAmount());
					imReceiveExpense2.setLocalAmount(imReceiveExpenseMod.getLocalAmount());
					imReceiveExpense2.setTaxAmount(imReceiveExpenseMod.getTaxAmount());
					imReceiveExpense.add(imReceiveExpense2);
//					log.info("OriHeadId"+OriHeadId);
//					
				}else{
					throw new Exception("查無原單號"); 
				}
				
			}else{
				throw new Exception("查無原單號");
			}
			
					
			resultMsg = "費用單："
					+ imReceiveExpenseMod.getOrderNo() + "存檔成功！ 是否繼續新增？";

			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", imReceiveExpenseMod);
			resultMap.put("vatMessage", msgBox);

			return resultMap;

		} catch (Exception ex) {
			log.error("維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("維護單存檔失敗，原因：" + ex.toString());
		}
	}
	
	private BuSupplierWithAddressViewService buSupplierWithAddressViewService;
	
	public void setBuSupplierWithAddressViewService(BuSupplierWithAddressViewService buSupplierWithAddressViewService) {
		this.buSupplierWithAddressViewService = buSupplierWithAddressViewService;
	}
	 public List<Properties> getAJAXFormDataBySupplier(Properties httpRequest)throws Exception {
			//log.info("getFormDataBySupplier");
			Properties pro = new Properties();
			List re = new ArrayList();
			Long   addressBookId    = NumberUtils.getLong(httpRequest.getProperty("addressBookId"));
			String supplierCode     = httpRequest.getProperty("supplierCode");
			String brandCode        = httpRequest.getProperty("brandCode");
			log.info("add+bsup"+addressBookId+"--"+"--"+supplierCode);
			if( !StringUtils.hasText(supplierCode)) {
			    BuSupplierWithAddressView buSWAVaddressBookId = 
				buSupplierWithAddressViewService.findSupplierByAddressBookIdAndBrandCode(addressBookId, brandCode );
			    supplierCode = buSWAVaddressBookId.getSupplierCode();
			}
			//log.info("getFormDataBySupplierCode supplierCode=" + supplierCode + ",organizationCode=" + organizationCode
						//+ ",brandCode=" + brandCode + ",orderDate=" + orderDate);
			BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(brandCode, supplierCode);
		
			
			if (null != buSWAV ) {
			    pro.setProperty("SupplierCode",    AjaxUtils.getPropertiesValue(buSWAV.getSupplierCode(),  ""));
			    pro.setProperty("SupplierName",    AjaxUtils.getPropertiesValue(buSWAV.getChineseName(),     ""));
				
			} else {
				pro.setProperty("SupplierName",    "");
				
			}
			re.add(pro);
			return re;
		    }
	
	 public void validateHead(Map parameterMap) throws Exception {

			Object formBindBean = parameterMap.get("vatBeanFormBind");
			// Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");

			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");

			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : 0;
			
			log.info("formId = " + formId);
			log.info("validate");
			// 驗證名稱
			String orderNo = (String) PropertyUtils.getProperty(formBindBean, "orderNo");
			String billDate = (String) PropertyUtils.getProperty(formBindBean, "billDate");
			if (!StringUtils.hasText(orderNo)) {
				throw new ValidationErrorException("請輸入單號！");
			}
			if (!StringUtils.hasText(billDate)) {
				throw new ValidationErrorException("請輸入Date！");
			}		
		}
}