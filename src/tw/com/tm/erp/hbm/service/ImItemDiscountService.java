package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.ImItemDiscountDAO;
import tw.com.tm.erp.hbm.dao.ImItemDiscountHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemDiscountLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemDiscountModDAO;
import tw.com.tm.erp.hbm.dao.PosExportDAO;
import tw.com.tm.erp.hbm.dao.SiGroupDAO;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.FiBudgetLine;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.ImItemDiscount;
import tw.com.tm.erp.hbm.bean.ImItemDiscountHead;
import tw.com.tm.erp.hbm.bean.ImItemDiscountId;
import tw.com.tm.erp.hbm.bean.ImItemDiscountLine;
import tw.com.tm.erp.hbm.bean.ImItemDiscountMod;
import tw.com.tm.erp.hbm.bean.ImItemDiscountModHead;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.ImPromotionShop;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PosEmployee;
import tw.com.tm.erp.hbm.bean.PosItemDiscount;
import tw.com.tm.erp.hbm.bean.SiGroup;
import tw.com.tm.erp.hbm.bean.SiGroupId;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class ImItemDiscountService {
	private static final Log log = LogFactory.getLog(ImItemDiscountService.class);
	private BaseDAO baseDAO;
	private BuEmployeeDAO buEmployeeDAO;
	private ImItemDiscountHeadDAO imItemDiscountHeadDAO;
	private ImItemDiscountLineDAO imItemDiscountLineDAO;
	private ImItemDiscountModDAO imItemDiscountModDAO;
	private ImItemCategoryService imItemCategoryService;
    private BuCommonPhraseService buCommonPhraseService;
    private BuBrandService buBrandService;
	public static final String PROGRAM_ID= "IM_ITEM_DISCOUNT";
	
	private final String orderTypeCode = "IOP"; // 單別
	private final String orderNo = "";
	private SiProgramLogAction siProgramLogAction;
	private BuOrderTypeService buOrderTypeService;
	private ImItemDiscountDAO imItemDiscountDAO;
	private PosExportDAO posExportDAO;
	
	/**
	 * 商品折扣的查詢(Picker)欄位(Header)
	 */
	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO){
		this.buEmployeeDAO = buEmployeeDAO;
	}
	public static final String[] GRID_SEARCH_FIELD_NAMES = { "id.brandCode", "id.vipTypeCode", "id.itemDiscountType",
			"beginDate", "endDate", "discount", "createdBy", "creationDate" };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "", "",
			"", "", "", "", "" };
	/**
	 * Mod商品折扣的查詢(Picker)欄位(Header)
	 */
	public static final String[] GRID_SEARCH_MOD_FIELD_NAMES = { "orderNo", "brandCode", "vipTypeCode", "itemDiscountType",
			"beginDate", "endDate", "discount", "status" , "createdBy", "creationDate","enable","headId" };

	public static final String[] GRID_SEARCH_MOD_FIELD_DEFAULT_VALUES = { "", "", "", "",
			"", "", "", "", "" , "" ,"" ,""};

	/**
	 * 商品折扣明細欄位
	 */
	public static final String[] GRID_FIELD_NAMES = 
		{ "indexNo", "vipTypeCode", "itemDiscountType" 
		,"beginDate" ,"endDate", "lineId"
		, "isLockRecord", "isDeleteRecord", "message" };

	public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
		"", "", ""
		, "", "", ""
		, AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "" };

	public static final int[] GRID_FIELD_TYPES = { 
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
		, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_LONG
		, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING };
	
	
	public void setImItemDiscountHeadDAO(ImItemDiscountHeadDAO imItemDiscountHeadDAO) {
		this.imItemDiscountHeadDAO = imItemDiscountHeadDAO;
	}

	public void setimItemDiscountLineDAO(ImItemDiscountLineDAO imItemDiscountLineDAO) {
		this.imItemDiscountLineDAO = imItemDiscountLineDAO;
	}
	
	public void setimItemDiscountModDAO(ImItemDiscountModDAO imItemDiscountModDAO) {
		this.imItemDiscountModDAO = imItemDiscountModDAO;
	}
		
	public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}
	
	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}	

	public BuBrandService getBuBrandService() {
	    return buBrandService;
	}

	public void setBuBrandService(BuBrandService buBrandService) {
	    this.buBrandService = buBrandService;
	}

	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
	
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}
    public void setImItemDiscountDAO(ImItemDiscountDAO imItemDiscountDAO) {
    	this.imItemDiscountDAO = imItemDiscountDAO;
    }
    
    public void setPosExportDAO(PosExportDAO posExportDAO) {
		this.posExportDAO = posExportDAO;
	}
    
    
	/**
	 * save and update
	 * 
	 * @param modifyObj
	 * @return
	 * @throws Exception
	 */
	public String create(ImItemDiscountHead modifyObj) throws FormException,
			Exception {
		log.info("ImItemDiscountService.create ");
		if (null != modifyObj) {
//			countHeadTotalAmount(modifyObj);
			if (modifyObj.getHeadId() == null) {
				return save(modifyObj);
			} else {
				return update(modifyObj);
			}
		} else {
			throw new FormException("查無表單主檔資料");
		}
	}

	/**
	 * save tmp
	 * 
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String saveTmp(ImItemDiscountHead saveObj) throws FormException, Exception {
		log.info("ImItemDiscountService.saveTmp");
		String tmpOrderNo = AjaxUtils.getTmpOrderNo();
		saveObj.setOrderNo(tmpOrderNo);
		//saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imItemDiscountHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}


	/**
	 * save
	 * 
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String save(ImItemDiscountHead saveObj) throws FormException, Exception {
		log.info("ImItemDiscountService.save ");
		doAllValidate(saveObj);
		saveObj.setCreatedBy(saveObj.getLastUpdateBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imItemDiscountHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}
	
    public Long getImItemDiscountModHeadId(Object bean) throws FormException, Exception{
    	Long headId = null;
    	String id = (String)PropertyUtils.getProperty(bean, "headId");
            System.out.println("headId=" + id);
    	if(StringUtils.hasText(id)){
                headId = NumberUtils.getLong(id);
            }else{
                throw new ValidationErrorException("傳入的採購單主鍵為空值！");
            }
    	return headId;
        }
	
	public String save(ImItemDiscountHead saveObj, String formAction, String loginUser) throws Exception {
		//String result = "Error";
		try {
	
			if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
				System.out.println("1.check discount data");
				this.checkDetailItemCode(saveObj);
			}
			System.out.println("2.save discount data");
			if (saveObj.getOrderNo() == null
					|| "".equals(saveObj.getOrderNo())) {
				// 新增
				// 取得最新一筆流水號
				if (!(loginUser.equals(""))) {
					UserUtils.setUserAndDate(saveObj, loginUser);
//					UserUtils.setUserAndDate(saveObj.getImPromotionItems(), loginUser);
//					UserUtils.setUserAndDate(saveObj.getImPromotionShops(), loginUser);
//					UserUtils.setUserAndDate(saveObj.getImPromotionCustomers(), loginUser);
//					UserUtils.setUserAndDate(saveObj.getImPromotionFiles(), loginUser);
				}
				System.out.println("3.get order No");
				String serialNo = ServiceFactory.getInstance()
				.getBuOrderTypeService().getOrderSerialNo(saveObj.getBrandCode(),saveObj.getOrderTypeCode());
				if (!serialNo.equals("unknow")) {
					saveObj.setOrderNo(serialNo);
					System.out.println("3.reset reserve");
					this.resetLineReserve(saveObj);
					imItemDiscountHeadDAO.save(saveObj);
				} else {
					throw new ObtainSerialNoFailedException("取得"
							+ saveObj.getOrderTypeCode() + "單號失敗！");
				}
				return saveObj.getOrderTypeCode() + "-" + serialNo	+ "存檔成功！";
			} else {
				imItemDiscountHeadDAO.update(saveObj);				
				return saveObj.getOrderTypeCode() + "-" + saveObj.getOrderNo()	+ "存檔成功！";
			}

		} catch (Exception ex) {
			log.error("商品折扣申請單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("商品折扣申請單存檔時發生錯誤，原因：" + ex.getMessage());
		}
	
	}
	
//	save($form,OrderStatus.FORM_SUBMIT, "")

	
	public void resetLineReserve(ImItemDiscountHead resetObj) {
		List<ImItemDiscountLine>objItems = resetObj.getImItemDiscountLines();
		for (ImItemDiscountLine item : objItems) {
			item.setReserve1("Okay ");
			item.setReserve2("");
			item.setReserve3("");
			item.setReserve4("");
			item.setReserve5("");
		}
	}

	
	
	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String update(ImItemDiscountHead updateObj) throws FormException,
			Exception {
		log.info("ImItemDiscountService.update ");
		doAllValidate(updateObj);
		updateObj.setLastUpdateDate(new Date());
		imItemDiscountHeadDAO.update(updateObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * search
	 * 
	 * @param findObjs
	 * @return
	 */
	public List<ImItemDiscountHead> find(HashMap findObjs) {
		log.info("ImItemDiscountService.find ");
		return imItemDiscountHeadDAO.find(findObjs);
	}

	/**
	 * 檢核
	 * 
	 * @param headObj
	 * @return
	 * @throws Exception
	 */
	private boolean doAllValidate(ImItemDiscountHead headObj) throws FormException, Exception {
		log.info("ImAdjustmentHeadService.doAllValidate ");
		boolean isError = false;
		StringBuffer errorMessage = new StringBuffer();
		// if (OrderStatus.SIGNING.equalsIgnoreCase(headObj.getStatus())) {
		String identification = MessageStatus.getIdentification(headObj.getBrandCode(), 
    			headObj.getOrderTypeCode(), headObj.getOrderNo());
		if (checkDetailItemCode(headObj)) {
			errorMessage.append(MessageStatus.ERROR_NO_DETAIL+"<br>");
		}
		

		if(errorMessage.length()>0){
		    isError = true;
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, errorMessage.toString(), headObj.getCreatedBy());
		}
		return isError;
	}

	
	/**
	 * 移除空白的Detail
	 * 
	 * @param headObj
	 * @return boolean 是否還有 Detail
	 */
	private boolean checkDetailItemCode(ImItemDiscountHead headObj) {
		log.info("ImItemDiscountService.checkDetailItemCode ");
		List<ImItemDiscountLine> lines = headObj.getImItemDiscountLines() ;
		Iterator<ImItemDiscountLine> it = lines.iterator();
		while (it.hasNext()) {
			ImItemDiscountLine line = it.next();
			//if (!StringUtils.hasText(line.getItemCode())) {    問題
				it.remove();
				lines.remove(line);
			//}
		}
		// log.info("is empty " + items.isEmpty());
		return lines.isEmpty();
		// return false;
	}

	
	
    /**
     * 依據primary key為查詢條件，取得地點資料
     * 
     * @param locationId
     * @return BuLocation
     * @throws Exception
     */
    /*public ImItemDiscount findById(Long formId) throws Exception {

		try {
		    ImItemDiscount itemDiscount = (ImItemDiscount) imItemDiscountDAO.findByPrimaryKey(
			    ImItemDiscount.class, formId);
		    return itemDiscount;
		} catch (Exception ex) {
		    log.error("依據主鍵：" + formId + "查詢地點資料時發生錯誤，原因：" + ex.toString());
		    throw new Exception("依據主鍵：" + formId + "查詢地點資料時發生錯誤，原因："
			    + ex.getMessage());
		}
    }*/

	public List<Properties> saveSearchResult(Properties httpRequest)
			throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	
	public List<Properties> saveSearchModResult(Properties httpRequest)
		throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_MOD_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	
	

	public List<Properties> getSearchSelection(Map parameterMap)
			throws FormException, Exception {
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			log.info("getSearchSelection.parameterMap:"+ parameterMap.keySet().toString());
			
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean,AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			log.info("pickerBean:" + pickerBean);
			log.info("getSearchSelection.picker_parameter:" + timeScope + "/"+ searchKeys.toString());

			List<Properties> result = AjaxUtils.getSelectedResults(timeScope,searchKeys);
			
			log.info("getSearchSelection.result:" + result.size());
			
			if (result.size() > 0)
			{
				pickerResult.put("result", result);
				log.info("if ---- result:" + result);
			}
			log.info("jason pickerResult:"+pickerResult);
			
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

	
	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();

			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			// ======================帶入Head的值=========================
	  	    String loginBrandCode = httpRequest.getProperty("loginBrandCode");
	  	    String vipTypeCode = httpRequest.getProperty("vipTypeCode");
	  	    String itemDiscountType = httpRequest.getProperty("itemDiscountType");
	  	    
log.info("loginBrandCode:"+loginBrandCode);
log.info("vipTypeCode:"+vipTypeCode);
log.info("itemDiscountType:"+itemDiscountType);

  			HashMap map = new HashMap();
  			HashMap findObjs = new HashMap();
		    findObjs.put(" and model.id.brandCode = :brandCode",loginBrandCode);
		    findObjs.put(" and model.id.vipTypeCode like :vipTypeCode","%"+vipTypeCode+"%");
		    findObjs.put(" and model.id.itemDiscountType like :itemDiscountType","%"+itemDiscountType+"%");
			// ======================取得頁面所需資料===========================
	  	    Map imItemDiscountMap = imItemDiscountDAO.search( "ImItemDiscount as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	  	    List<ImItemDiscount> imItemDiscounts = (List<ImItemDiscount>) imItemDiscountMap.get(BaseDAO.TABLE_LIST); 
	  	    for( ImItemDiscount imItemDiscount : imItemDiscounts ){
	  	    	imItemDiscount.setCreatedBy( UserUtils.getUsernameByEmployeeCode(imItemDiscount.getCreatedBy()));
	  	    }
log.info("ImItemDiscount.size"+ imItemDiscounts.size());	
		    if (imItemDiscounts != null && imItemDiscounts.size() > 0) {
		    	
		    	// 設定額外欄位
		    	this.setLineOtherColumn(imItemDiscounts);
		    	
		    	Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		    	Long maxIndex = (Long)imItemDiscountDAO.search("ImItemDiscount as model", "count(*) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		    	
		    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,imItemDiscounts, gridDatas, firstIndex, maxIndex));
		     }else {
		    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
		     }
		
		    return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的商品折扣明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的商品折扣明細失敗！");
		}
	}
    private void setLineOtherColumn(List<ImItemDiscount> imItemDiscounts){
    	for (ImItemDiscount imItemDiscount : imItemDiscounts) {
    		imItemDiscount.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(imItemDiscount.getLastUpdatedBy()));
		}
    }
    private void setModLineOtherColumn(List<ImItemDiscountMod> imItemDiscountMods){
    	for (ImItemDiscountMod imItemDiscountMod : imItemDiscountMods) {
    		imItemDiscountMod.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(imItemDiscountMod.getLastUpdatedBy()));
		}
    }


	/*
	 * 
	 * headId 是由 hibernate 的 sequence 產生，參考 *.hbm.xml 。存檔之後才會產生 headId 在此取出。
	 */
	public List<Properties> executeInitial(Map parameterMap) throws Exception{ 
		Map resultMap = new HashMap(0);
		try{
//			Object otherBean = parameterMap.get("vatBeanOther");
//  		String brandCode 		= (String)PropertyUtils.getProperty(otherBean, "brandCode");
//  		String vipTypeCode 		= (String)PropertyUtils.getProperty(otherBean, "vipTypeCode");
//  		String itemDiscountType = (String)PropertyUtils.getProperty(otherBean, "itemDiscountType");
//  		ImItemDiscountId id = new ImItemDiscountId(brandCode,vipTypeCode,itemDiscountType);
//			ImItemDiscount imItemDiscount = imItemDiscountDAO.findById(id);
//  		Map multiList = new HashMap(0);
//			resultMap.put("form", imItemDiscountHead);
//			resultMap.put("loginBrandCode", loginBrandCode);
//			resultMap.put("statusName", OrderStatus.getChineseWord(imItemDiscountHead.getStatus())); // 跨 table 選取基本檔
//			resultMap.put("lastUpdatedByName", employeeName);
//			resultMap.putAll( this.getOtherColumn(imItemDiscountHead, loginBrandCode) );
//			String orderType = imItemDiscountHead.getOrderTypeCode();
//			List<BuCommonPhraseLine> allVipTypeCodes = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"VipDiscount", "Y"}, "indexNo" );
//			List<BuCommonPhraseLine> allItemDiscountTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"VIPType", "Y"}, "indexNo" );
//			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(imItemDiscountHead.getBrandCode() ,"IID");
			//String currencyCode = imItemDiscountHead.getCurrencyCode();
			//multiList.put("allCurrencyCodes"	, AjaxUtils.produceSelectorData(allCurrencyCodes, "currencyCode", "currencyCName", true, true, currencyCode != null ? currencyCode : "" ));
//			multiList.put("allVipTypeCodes"	, AjaxUtils.produceSelectorData(allVipTypeCodes, "lineCode", "name", true, true ));
//			multiList.put("allItemDiscountTypes"	, AjaxUtils.produceSelectorData(allItemDiscountTypes, "lineCode", "name", true, true ));
//			multiList.put("allOrderTypes"	, AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, true, orderType != null ? orderType : "" ));
//			resultMap.put("multiList",multiList);
//			return resultMap;
			Object otherBean = parameterMap.get("vatBeanOther");
log.info("jason otherBean:"+otherBean);			
			String brandCode = (String) PropertyUtils.getProperty(otherBean,"brandCode");
			String vipTypeCode = (String) PropertyUtils.getProperty(otherBean,"vipTypeCode");
			String itemDiscountType = (String) PropertyUtils.getProperty(otherBean,"itemDiscountType");

			ImItemDiscount imItemDiscount = (ImItemDiscount)imItemDiscountDAO.findById(brandCode, vipTypeCode, itemDiscountType);
log.info("jason imItemDiscount:"+imItemDiscount);
// 			List<BuCommonPhraseLine> brandCodes = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "BrandCode" );
			String dates[];

//			ImItemDiscount imItemDiscount = this.executeFindActualImItemDiscount(parameterMap);
			
			Map multiList = new HashMap(0);
			if(imItemDiscount!=null)
			{
				resultMap.put("brandCode",brandCode);
				resultMap.put("vipTypeCode",vipTypeCode);
				resultMap.put("itemDiscountType",itemDiscountType);
				resultMap.put("discount",imItemDiscount.getDiscount());
				resultMap.put("beginDate","");
				if(imItemDiscount.getBeginDate() != null)
				{
	       			dates = imItemDiscount.getBeginDate().toString().split("-");
	    			resultMap.put("beginDate",dates[0]+"/"+dates[1]+"/"+dates[2].substring(0,2));
	    		}
				resultMap.put("endDate","");
				if(imItemDiscount.getEndDate() != null)
				{
	       			dates = imItemDiscount.getEndDate().toString().split("-");
	    			resultMap.put("endDate",dates[0]+"/"+dates[1]+"/"+dates[2].substring(0,2));
	    		}
				resultMap.put("createdByName",UserUtils.getUsernameByEmployeeCode(imItemDiscount.getCreatedBy()));
				resultMap.put("creationDate","");
				if(imItemDiscount.getCreationDate() != null)
				{
	       			dates = imItemDiscount.getCreationDate().toString().split("-");
	    			resultMap.put("creationDate",dates[0]+"/"+dates[1]+"/"+dates[2].substring(0,2));
	    		}
			}		
			resultMap.put("multiList",multiList);
//			return resultMap;

		}catch(Exception ex){
			log.error("商品折扣初始化失敗，原因：" + ex.toString());
			throw new Exception("商品折扣初始化失敗，原因：" + ex.toString());
		}
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	
	/**
	 * 依formId取得實際地點主檔 in 送出
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
    public ImItemDiscount executeFindActualImItemDiscount(Map parameterMap)
	    throws FormException, Exception {
    	
//		Object formBindBean = parameterMap.get("vatBeanFormBind");
//		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");
		
		ImItemDiscount imItemDiscount = null;
    	try {

		    String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");

		    Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;

	
		    imItemDiscount = (ImItemDiscount) (null == formId ? this.executeNewImItemDiscount(): this.findById(formId)) ;
		
		    parameterMap.put( "entityBean", imItemDiscount);
		    return imItemDiscount;
    	} catch (Exception e) {
    		log.error("取得實際地點主檔失敗,原因:"+e.toString());
			throw new Exception("取得實際地點主檔失敗,原因:"+e.toString());
		}
    }
    
    public ImItemDiscount executeNewImItemDiscount() throws Exception {

    	ImItemDiscount form = new ImItemDiscount();
//		form.setBrandCode("");
//		form.setVipTypeCode("");
//		form.setItemDiscountType("");
		form.setDiscount(null);
		form.setBeginDate(null);
		form.setEndDate(null);
		form.setEnable("");
		form.setReserve1("");
		form.setReserve2("");
		form.setReserve3("");
		form.setReserve4("");
		form.setReserve5("");
		form.setCreatedBy("");
		form.setCreationDate(null);
		form.setLastUpdatedBy("");
		form.setLastUpdateDate(null);

		return form;

    }

	

	/**
	 * 依據primary key為查詢條件，取得商品折扣主檔
	 * 
	 * @param headId
	 * @return ImItemDiscountHead
	 * @throws Exception
	 */
	public ImItemDiscountHead findImItemDiscountHeadById(Long headId) throws Exception {

		try {
			ImItemDiscountHead itemDiscountHead = (ImItemDiscountHead) imItemDiscountHeadDAO.findByPrimaryKey(ImItemDiscountHead.class,
					headId);
			
			return itemDiscountHead;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢商品折扣主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢商品折扣主檔時發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	
	/**
	 * 取得新的商品折扣表頭
	 * @param otherBean
	 * @return
	 * @throws Exception
	 */
	public ImItemDiscountHead executeNew(Object otherBean)throws Exception{
		ImItemDiscountHead form = new ImItemDiscountHead();
		try {
			String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			
			form.setOrderTypeCode(orderTypeCode);
			form.setOrderNo(orderNo);
			form.setBrandCode(loginBrandCode);
			form.setStatus( OrderStatus.SAVE );
			form.setCreatedBy(loginEmployeeCode);
			form.setLastUpdateBy(loginEmployeeCode);
			form.setLastUpdateDate(new Date());
			
			this.saveHead(form);
			
		} catch (Exception e) {
			log.error("建立新商品折扣主檔失敗,原因:"+e.toString());
			throw new Exception("建立新商品折扣主檔失敗,原因:"+e.toString());
		}
		return form;
	}


	/**
	 * 商品折扣存檔,取得暫存碼
	 * @param imItemDiscountHead
	 * @throws Exception
	 */ 
    public void saveHead(ImItemDiscountHead imItemDiscountHead) throws Exception{
	
    	try{
    	    String tmpOrderNo = AjaxUtils.getTmpOrderNo(); // 產生暫存單號
//    	    imItemDiscountHead.setReserve5(tmpOrderNo); 
    	    imItemDiscountHead.setOrderNo(tmpOrderNo); // 寫入暫存單號
    	    imItemDiscountHead.setLastUpdateDate(new Date());
    	    imItemDiscountHead.setCreationDate(new Date());
    	    imItemDiscountHeadDAO.save(imItemDiscountHead);	  
    	}catch(Exception ex){
    	    log.error("取得暫時單號儲存商品折扣發生錯誤，原因：" + ex.toString());
    	    throw new Exception("取得暫時單號儲存商品折扣發生錯誤，原因：" + ex.getMessage());
    	}	
    }

    
//	public ImItemDiscountHead findImItemDiscountHead(long formId, Map returnMap)
//			throws FormException, Exception {
//		Map multiList = new HashMap(0);
//		try {
//			ImItemDiscountHead form = findById(formId);
//			if (form != null) {
////				BuBrand buBrand = buBrandDAO.findById(form.getBrandCode());
////				returnMap.put("brandName", buBrand.getBrandName());
////				ImItemCategory itemType = imItemCategoryService.findById(form.getBrandCode(), "ITEM_CATEGORY", form.getItemType());
////				if(itemType != null)
////					returnMap.put("itemTypeName", itemType.getCategoryName());
//				returnMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
//				returnMap.put("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode(form.getLastUpdateBy()));
//				returnMap.put("form", form);
//				List <ImItemCategory> category01s = imItemCategoryService.findByCategoryType(form.getBrandCode(), "CATEGORY01");
//			    multiList.put("category01s" , AjaxUtils.produceSelectorData(category01s  ,"categoryCode" ,"categoryName",  false,  true));
//			    List <ImItemCategory> category02s = imItemCategoryService.findByCategoryType(form.getBrandCode(), "CATEGORY02");
//			    multiList.put("category02s" , AjaxUtils.produceSelectorData(category02s  ,"categoryCode" ,"categoryName",  false,  true));
//			    returnMap.put("multiList",multiList);
//				return form;
//			} else {
//				throw new NoSuchObjectException("查無商品折扣主鍵：" + formId + "的資料！");
//			}
//		} catch (FormException fe) {
//			log.error("查詢商品折扣失敗，原因：" + fe.toString());
//			throw new FormException(fe.getMessage());
//		} catch (Exception ex) {
//			log.error("查詢商品折扣發生錯誤，原因：" + ex.toString());
//			throw new Exception("查詢商品折扣發生錯誤！");
//		}
//	}


	/**
	 * 判斷是否為新資料，並將商品折扣資料新增或更新至商品折扣主檔、明細檔
	 * 
	 * @param imItemDiscountHead
	 * @param loginUser
	 * @return String
	 * @throws FormException
	 * @throws Exception
	 */
	private String insertOrUpdateImItemDiscount(ImItemDiscountHead imItemDiscountHead, String loginUser) throws FormException, Exception {

		UserUtils.setOpUserAndDate(imItemDiscountHead, loginUser);
		UserUtils.setUserAndDate(imItemDiscountHead.getImItemDiscountLines(), loginUser);
//		UserUtils.setUserAndDate(imItemDiscountHead.getImItemDiscountAlters(), loginUser);

		if (imItemDiscountHead.getHeadId() == null) {
			insertImItemDiscount(imItemDiscountHead);
		} else {
			modifyImItemDiscount(imItemDiscountHead);
		}

		return "商品折扣：" + imItemDiscountHead.getHeadId() + "存檔成功！";
	}

	/**
	 * 新增至商品折扣主檔、明細檔
	 * 
	 * @param saveObj
	 */
	private void insertImItemDiscount(Object saveObj) {
		imItemDiscountHeadDAO.save(saveObj);
	}

	/**
	 * 更新至商品折扣主檔、明細檔
	 * 
	 * @param updateObj
	 */
	private void modifyImItemDiscount(Object updateObj) {
		imItemDiscountHeadDAO.update(updateObj);
	}

	/**
	 * 更新商品折扣主檔、明細檔的Status
	 * 
	 * @param imItemDiscountHeadId
	 * @param status
	 * @return String
	 * @throws Exception
	 */
	public String updateImItemDiscountStatus(Long imItemDiscountHeadId, String status) throws Exception {

		try {
			ImItemDiscountHead imItemDiscountHead = (ImItemDiscountHead) imItemDiscountHeadDAO.findByPrimaryKey(ImItemDiscountHead.class,
					imItemDiscountHeadId);
			if (imItemDiscountHead != null) {
				imItemDiscountHead.setStatus(status);
				imItemDiscountHead.setLastUpdateDate(new Date());
				List<ImItemDiscountLine> lcLines = imItemDiscountHead.getImItemDiscountLines();
				if (lcLines != null && lcLines.size() > 0) {
					for (ImItemDiscountLine lcLine : lcLines) {
//						lcLine.setStatus(status);
						lcLine.setLastUpdateDate(new Date());
					}
				}
				modifyImItemDiscount(imItemDiscountHead);
				return "Success";
			} else {
				throw new NoSuchDataException("商品折扣主檔查無主鍵：" + imItemDiscountHeadId + "的資料！");
			}
		} catch (Exception ex) {
			log.error("更新商品折扣狀態時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新商品折扣狀態時發生錯誤，原因：" + ex.getMessage());

		}
	}
	

    /**
	 * 商品折扣依formAction取得下個狀態，狀態有2個來源，使用者按下按鈕，或表單狀態(從資料庫抓取)。所以，
	 * formAction = OrderStatus.FORM_SAVE; 
	 * head.getStatus = OrderStatus.SAVE
	 * 兩者的意義不同。 
	 */
	public void setNextStatus(ImItemDiscountHead head, String formAction, String approvalResult){
//		String status = this.setHeadStatus(head,formAction);
//		this.setAllLinesStatus(head, status);
		if(OrderStatus.FORM_SAVE.equals(formAction)){
			head.setStatus(OrderStatus.SAVE);
		}else if( (OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction)) ){
			if(OrderStatus.SAVE.equals( head.getStatus() )  || OrderStatus.REJECT.equals( head.getStatus() ) ){
				head.setStatus(OrderStatus.SIGNING); 
			}else if( OrderStatus.SIGNING.equals(head.getStatus()) ){
				if( "false".equals(approvalResult) ){
					head.setStatus(OrderStatus.REJECT);
				}
			} 
		}else if(OrderStatus.FORM_VOID.equals(formAction)){
			head.setStatus(OrderStatus.VOID);
		}
	}
	

	
	/**
	 * 前端資料塞入bean
	 * @param parameterMap ( formLinkBean:設為 back: false 的值皆在此;   beanOther:自訂變數，eg.頁碼…   formBindBean:真正有對應到資料庫欄位的變數  )
	 * @return
	 */
	public Map updateImItemDiscount(Map parameterMap)throws FormException, Exception {
		Map resultMap = new HashMap();
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
            log.info("updateImItemDiscount formBindBean:" + formBindBean );
    	    Object formLinkBean = parameterMap.get("vatBeanFormLink"); // 空值?
            log.info("updateImItemDiscount formLinkBean:" + formLinkBean );
     	    
    	    //取得欲更新的bean
	    	ImItemDiscountHead imItemDiscountHeadPO = getActualImItemDiscount(formLinkBean); 
    	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imItemDiscountHeadPO);
    	    
    	    resultMap.put("entityBean", imItemDiscountHeadPO);
    	    
    	    return resultMap;      
        } catch (FormException fe) {
		    log.error("前端資料塞入bean失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
		    throw new Exception("前端資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	/**
	 * 透過headId取得商品折扣
	 * @param otherBean
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public ImItemDiscountHead getActualImItemDiscount(Object otherBean ) throws FormException, Exception{
        log.info("getActualImItemDiscount otherBean:" + otherBean );
	  	
	  	ImItemDiscountHead imItemDiscountHead = null;
        log.info("getActualImItemDiscount headId:" + PropertyUtils.getProperty(otherBean, "headId") );
        String id = (String)PropertyUtils.getProperty(otherBean, "headId");
        log.info("getActualImItemDiscount id:" + id );
	    if(StringUtils.hasText(id)){
            Long headId = NumberUtils.getLong(id);
            log.info("getActualImItemDiscount headId:" + PropertyUtils.getProperty(otherBean, "headId") );
            imItemDiscountHead = this.findImItemDiscountHeadById(headId);
    	if(imItemDiscountHead  == null){
    	    throw new NoSuchObjectException("查無商品折扣主鍵：" + headId + "的資料！");
    	}
        }else{
        	throw new ValidationErrorException("傳入的商品折扣主鍵為空值！");
        }
	    return imItemDiscountHead;
	}


	
	/**
	 * 將商品折扣資料新增或更新至商品折扣主檔、明細檔
	 * 
	 * @param imItemDiscountHead
	 * @param conditionMap
	 * @param loginUser
	 * @return String
	 * @throws Exception
	 */
	public String saveOrUpdateImItemDiscount(ImItemDiscountHead imItemDiscountHead, HashMap conditionMap, String loginUser) throws Exception {
		log.info("ImItemDiscountService.saveOrUpdateImItemDiscount");
		try {
//			String beforeChangeStatus = (String) conditionMap.get("beforeChangeStatus");
//			if (OrderStatus.SAVE.equals(beforeChangeStatus)) {
//				validateLC(imItemDiscountHead);
//				countAllTotalAmount(imItemDiscountHead, conditionMap, null);
//			}

			return insertOrUpdateImItemDiscount(imItemDiscountHead, loginUser);
		} catch (FormException fe) {
			log.error("商品折扣存檔時發生錯誤，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("商品折扣存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("商品折扣存檔時發生錯誤，原因：" + ex.getMessage());
		}
	}
	

	/**
	 * ajax 更新商品折扣明細的LINE
	 * 
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> updateAJAXPageLinesData(
			Properties httpRequest) throws Exception {
		try {
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");

			if (headId == null) {
				throw new ValidationErrorException("傳入的商品折扣主鍵為空值！");
			}
			String errorMsg = null;
			
			// 將 JSON String 資料轉成 List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue( gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
			// Get INDEX NO
			int indexNo = imItemDiscountLineDAO.findPageLineMaxIndex(headId).intValue();
			
			// 考慮狀態
			int i = 0;
			if (upRecords != null) {
				for (Properties upRecord : upRecords) {
					// 先載入HEAD_ID OR LINE DATA
					
					Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));

					String itemCode = upRecord.getProperty( GRID_FIELD_NAMES[1]);

					if (StringUtils.hasText(itemCode)) { // 當 itemCode 不為 null

						List<ImItemDiscountLine> imItemDiscountLines = imItemDiscountLineDAO.findByProperty("ImItemDiscountLine", new String[]{ "imItemDiscountHead.headId", "lineId" }, new Object[]{ headId, lineId } );
						 
						Date date = new Date();
						if (imItemDiscountLines != null && imItemDiscountLines.size() > 0 ) { // 若有記錄就變更
							ImItemDiscountLine imItemDiscountLine = imItemDiscountLines.get(0);
							AjaxUtils.setPojoProperties(imItemDiscountLine,upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES); //寫回前端
							imItemDiscountLine.setLastUpdatedBy(loginEmployeeCode);
							imItemDiscountLine.setLastUpdateDate(date);
							imItemDiscountLineDAO.update(imItemDiscountLine);

						} else { // 若無記錄就新增
							indexNo++;
							ImItemDiscountLine imItemDiscountLine = new ImItemDiscountLine(); 

							AjaxUtils.setPojoProperties(imItemDiscountLine,upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
							imItemDiscountLine.setImItemDiscountHead(new ImItemDiscountHead(headId));
							imItemDiscountLine.setCreateBy(loginEmployeeCode);
							imItemDiscountLine.setCreationDate(date);
							imItemDiscountLine.setLastUpdatedBy(loginEmployeeCode);
							imItemDiscountLine.setLastUpdateDate(date);
							imItemDiscountLine.setIndexNo(Long.valueOf(indexNo));
							imItemDiscountLineDAO.save(imItemDiscountLine);
						}
					}
					i++;
				}
			}

			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新商品折扣明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新商品折扣明細失敗！");
		}
	}
	
	/**
	 * 存取商品折扣主檔,明細檔,重新設定狀態
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXImItemDiscount(Map parameterMap, String formAction)throws Exception{
		Map resultMap = new HashMap();
		List errorMsgs = null;
		String resultMsg = null;
		ImItemDiscountHead imItemDiscountHead = null;
		try {
			
			Object otherBean = parameterMap.get("vatBeanOther");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			
	    	String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
//	    	String lcNo = (String)PropertyUtils.getProperty(formLinkBean, "lcNo"); 
	    	
			String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
	    	imItemDiscountHead = this.getActualImItemDiscount(formLinkBean);
	    	// 從formLinkBean來,避免UK問題
	    	
//	    	if ( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction)  || OrderStatus.FORM_SAVE.equals(formAction) ||
//	    			OrderStatus.CLOSE.equals(formAction)){
	    		// 檢核 LcNo for暫存
//	    		if( OrderStatus.FORM_SAVE.equals(formAction) ){
//	    			errorMsgs  = this.checkedLcNo(imItemDiscountHead, lcNo);
//	    			log.info( "檢核checkedLcNo =" + errorMsgs.size() );
//	    		}
	    		// 檢核 送出,背景送出
//	    		if( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) || OrderStatus.CLOSE.equals(formAction)){
//	    			this.deleteAltLine(imItemDiscountHead);
//	    			errorMsgs  = this.checkedImItemDiscount(parameterMap, lcNo);
//	    			log.info( "檢核checkedImItemDiscount = " + errorMsgs.size() );
//	    		}
//	    	}
	    	
	    	if( errorMsgs != null ){
	    		log.info( "errorMsgs.size() = " + errorMsgs.size() );
	    	}
	    	
	    	if( errorMsgs == null || errorMsgs.size() == 0 ){
	    		log.info( "執行 updateAJAXImItemDiscount 沒有錯誤" );
	    		// 成功則設定下個狀態
	    		this.setNextStatus(imItemDiscountHead, formAction, approvalResult );
	    		
	    		// 存檔
	    		this.insertOrUpdateImItemDiscount(imItemDiscountHead, employeeCode);
	    		
	    		resultMsg = "Head ID：" + imItemDiscountHead.getHeadId() + "存檔成功！是否繼續新增？"; 
	    	} else if( errorMsgs.size() > 0 ){
	    		if( OrderStatus.FORM_SUBMIT.equals(formAction) || OrderStatus.FORM_SAVE.equals(formAction) || OrderStatus.CLOSE.equals(formAction) ){
	    			throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	    		}
	    	}
	    	resultMap.put("entityBean", imItemDiscountHead);
			resultMap.put("resultMsg", resultMsg);
		} catch( ValidationErrorException ve ){
			log.error("商品折扣檢核時發生錯誤，原因：" + ve.toString());
			throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		} catch (Exception e) {
			log.error("商品折扣存檔時發生錯誤，原因：" + e.toString());
			throw new Exception("商品折扣存檔時發生錯誤，原因：" + e.getMessage());
		}
		return resultMap;
	}

	/**
     *  取單號後更新更新主檔
     * 
     * @param parameterMap
     * @return Map
     * @throws FormException
     * @throws Exception
     */
    public Map updateImItemDiscountWithActualOrderNO(Map parameterMap) throws FormException, Exception {
        
        Map resultMap = new HashMap();
        try{
            
        	resultMap = this.updateImItemDiscount(parameterMap); 
        	ImItemDiscountHead discountHead = (ImItemDiscountHead) resultMap.get("entityBean");
        	
    	    //刪除於SI_PROGRAM_LOG的原識別碼資料
    	    String identification = MessageStatus.getIdentification(discountHead.getBrandCode(), 
    	    		discountHead.getOrderTypeCode(), discountHead.getOrderNo());
    	    siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
    	    
    	    this.setOrderNo(discountHead);
    	    String resultMsg = discountHead.getOrderNo() + "存檔成功！是否繼續新增？";
            resultMap.put("resultMsg", resultMsg);
    	
    	    return resultMap;      
        } catch (FormException fe) {
		    log.error("商品折扣存檔失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    log.error("商品折扣存檔時發生錯誤，原因：" + ex.toString());
		    throw new Exception("商品折扣存檔時發生錯誤，原因：" + ex.getMessage());
		}
    }

	public SiProgramLogAction getSiProgramLogAction() {
		return siProgramLogAction;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	/**
	 * 若是暫存單號,則取得新單號
	 * @param head
	 */
	private void setOrderNo(ImItemDiscountHead head) throws ObtainSerialNoFailedException{
		String orderNo = head.getOrderNo();
		if (AjaxUtils.isTmpOrderNo(orderNo)) {
			try {
				String serialNo = buOrderTypeService.getOrderSerialNo(head.getBrandCode(), head.getOrderTypeCode());
				if ("unknow".equals(serialNo)) 
					throw new ObtainSerialNoFailedException("取得" + head.getBrandCode() + "-" + head.getOrderTypeCode() + "單號失敗！");
				else
					head.setOrderNo(serialNo);
			} catch (Exception ex) {
					throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode() + "單號失敗！");
			}
		}
	}
	
	/**
	 * 啟動流程
	 * @param form
	 * @return result[0]流程代號, result[1]活動代號, result[2]活動名稱
	 * @throws ProcessFailedException
	 */
	public static Object[] startProcess(ImItemDiscountHead form) throws ProcessFailedException{       
		String packageId = null;
		String processId = null;
		String version = null;
		String sourceReferenceType = null;
        try{           
        	
			packageId = "Im_ItemDiscount";   // 看 cEAP 的目錄名稱
	     	processId = "approval"; // 不是想像中的 process id ，這裡指的是 cEAP 中的路徑   
	     	version = "20100106";
	     	sourceReferenceType = "(20100106)";
	     	
		    HashMap context = new HashMap();	    
		    context.put("formId", form.getHeadId());
	        context.put("brandCode", form.getBrandCode());
	        context.put("orderType", form.getOrderTypeCode());
	        context.put("orderNo", form.getOrderNo());
		    return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		}catch (Exception ex){
		    log.error("商品折扣流程啟動失敗，原因：" + ex.toString());
		    throw new ProcessFailedException("商品折扣流程啟動失敗！");
		}	      
    }

	/**
	 * 完成任務工作
	 * @param assignmentId
	 * @param approveResult
	 * @return
	 * @throws ProcessFailedException
	 */
	public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws ProcessFailedException{
		   
        try{           
		    HashMap context = new HashMap();
		    context.put("approveResult", approveResult);
		       
		    return ProcessHandling.completeAssignment(assignmentId, context);
		}catch (Exception ex){
		    log.error("完成商品折扣任務失敗，原因：" + ex.toString());
		    throw new ProcessFailedException("完成商品折扣任務失敗！");
		}
    }
	
	/**
	 * 查詢商品折扣初始化
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeSearchInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
	
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");
			String orderTypeCode = (String) PropertyUtils.getProperty(otherBean,"orderTypeCode");
			List<BuOrderType> allOrderTypeCode = buOrderTypeService.findOrderbyType(loginBrandCode, "IOP");
			// String condition = "and id.buCommonPhraseHead.headCode = ? and enable =
			// ? and attribute1 like ?";
			// List<BuCommonPhraseLine> allTaxTypes =
			// baseDAO.findByProperty("BuCommonPhraseLine", new String[]{
			// "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"TaxCatalog",
			// "Y"}, "indexNo" );

			Map multiList = new HashMap(0);

			multiList.put("allOrderTypeCode", AjaxUtils.produceSelectorData(
					allOrderTypeCode, "orderTypeCode", "name", true, true,
					orderTypeCode != null ? orderTypeCode : ""));

			resultMap.put("multiList", multiList);
			return resultMap;
		}catch(Exception ex){
			log.error("查詢商品折扣初始化失敗，原因：" + ex.toString());
			throw new Exception("查詢商品折扣初始化失敗，原因：" + ex.toString());
			
		}
	}

//    public String getIdentification(Long headId) throws Exception{
//		   
//		   String id = null;
//		   try{
//		       ImItemDiscountHead head = findById(headId);
//		       if(head != null){
//		    	   id = MessageStatus.getIdentification(head.getBrandCode(), 
//		    	   head.getOrderTypeCode(), head.getOrderNo());
//		       }else{
//		           throw new NoSuchDataException("商品折扣檔查無主鍵：" + headId + " 的資料！");
//		       }
//		       return id;
//		   }catch(Exception ex){
//		       log.error("查詢商品折扣時發生錯誤，原因：" + ex.toString());
//		       throw new Exception("查詢商品折扣時發生錯誤，原因：" + ex.getMessage());	       
//		   }	   
//	}
	public Map searchExecuteInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean,"loginEmployeeCode");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");

			Map multiList = new HashMap(0);
			
			List<BuCommonPhraseLine> allVipTypeCodes = baseDAO.findByProperty(
					"BuCommonPhraseLine", new String[] {
							"id.buCommonPhraseHead.headCode", "attribute1" }, new Object[] {
							"VIPType", loginBrandCode }, "indexNo");
			List<BuCommonPhraseLine> allItemDiscountTypes = baseDAO.findByProperty(
					"BuCommonPhraseLine", new String[] {
							"id.buCommonPhraseHead.headCode", "enable" }, new Object[] {
							"VipDiscount", "Y" }, "indexNo");

			multiList.put("allVipTypeCodes", AjaxUtils.produceSelectorData(allVipTypeCodes, "lineCode", "name", true, true));
			multiList.put("allItemDiscountTypes", AjaxUtils.produceSelectorData(allItemDiscountTypes, "lineCode", "name", true, true));

			resultMap.put("multiList", multiList);
			return resultMap;
		} catch (Exception ex) {
			log.error("商品折扣初始化失敗，原因：" + ex.toString());
			throw new Exception("商品折扣初始化失敗，原因：" + ex.toString());
		}
	}


	/**
     * 透過傳遞過來的參數來做Employee下傳
     * @param parameterMap
     * @throws Exception
     */
    public Long executeItemDiscountExport(HashMap parameterMap) throws Exception{
    	log.info("executeItemDiscountExport");
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
			List results = imItemDiscountDAO.findItemDiscountByCondition(parameterMap);
			if(results != null && results.size() >= 0){
		        for (Object result : results) {
		        	ImItemDiscount imItemDiscount = (ImItemDiscount)result;
			        PosItemDiscount posItemDiscount = new PosItemDiscount();
			        BeanUtils.copyProperties(imItemDiscount, posItemDiscount);
				    BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("VIPType", imItemDiscount.getId().getVipTypeCode());
				    if(null != buCommonPhraseLine){
				        posItemDiscount.setIdentification(buCommonPhraseLine.getAttribute4());
				    }
			        posItemDiscount.setDataId(uuId);
			        posItemDiscount.setAction("U");
			        posExportDAO.save(posItemDiscount);
		        }
			}
		}else{
			//非排程則是把DataId找出，再把ItemDiscount全下
			//尋找PosCustomer中此dataID有哪些需求資料
			parameterMap.put("brandCode", parameterMap.get("BRAND_CODE"));
			List results = imItemDiscountDAO.findItemDiscountByCondition(parameterMap);
			if(results != null && results.size() >= 0){
				log.info("results.size = " + results.size());
		        for (Object result : results) {
		        	ImItemDiscount imItemDiscount = (ImItemDiscount)result;
			        PosItemDiscount posItemDiscount = new PosItemDiscount();
			        BeanUtils.copyProperties(imItemDiscount, posItemDiscount);
			        BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("VIPType", imItemDiscount.getId().getVipTypeCode());
				    if(null != buCommonPhraseLine){
				        posItemDiscount.setIdentification(buCommonPhraseLine.getAttribute4());
				    }
			        posItemDiscount.setDataId(uuId);
			        posItemDiscount.setAction("U");
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
     * 依據primary key為查詢條件，取得地點資料
     * 
     * @param locationId
     * @return BuLocation
     * @throws Exception
     */
    public ImItemDiscountMod findById(Long formId) throws Exception {

		try {
		    ImItemDiscountMod imitemDiscountMod = (ImItemDiscountMod) imItemDiscountDAO.findByPrimaryKey(
			    ImItemDiscountMod.class, formId);
		    return imitemDiscountMod;
		} catch (Exception ex) {
		    log.error("依據主鍵：" + formId + "查詢地點資料時發生錯誤，原因：" + ex.toString());
		    throw new Exception("依據主鍵：" + formId + "查詢地點資料時發生錯誤，原因："
			    + ex.getMessage());
		}
    }
	
  /**
	 * 處理AJAX 商品折扣及相關資料 call by JS
	 * 
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFindDataByItemDiscount(Properties httpRequest) {
		List<Properties> result = new ArrayList();
		
		
		String brandCode  = httpRequest.getProperty("loginBrandCode");
		String vipTypeCode = httpRequest.getProperty("vipTypeCode");
		String itemDiscountCode = httpRequest.getProperty("itemDiscountCode");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Properties pro = new Properties();
		ImItemDiscount imItemDiscount;
		
	  
  	  if (StringUtils.hasText(vipTypeCode) && StringUtils.hasText(itemDiscountCode)) {
  		  	  		  	  		  
  		imItemDiscount = imItemDiscountModDAO.findByvipType(brandCode , vipTypeCode ,itemDiscountCode);
  				  		
  		pro.setProperty("beginDate", AjaxUtils.getPropertiesValue(sdf.format(imItemDiscount.getBeginDate()),""));
  		pro.setProperty("endDate", AjaxUtils.getPropertiesValue(sdf.format(imItemDiscount.getEndDate()),""));
  		pro.setProperty("enable", AjaxUtils.getPropertiesValue(imItemDiscount.getEnable(),""));
  		pro.setProperty("discount", AjaxUtils.getPropertiesValue(imItemDiscount.getDiscount().toString(),""));
  		pro.setProperty("createDate", AjaxUtils.getPropertiesValue(sdf.format(imItemDiscount.getCreationDate()),""));
		result.add(pro);
  		
		}
  	    return result;
	}
  
  /**
	 * 取得新的商品折扣表頭
	 * @param otherBean
	 * @return
	 * @throws Exception
	 */
	public ImItemDiscountMod executeNew(Map parameterMap)throws Exception{
		
		ImItemDiscountMod form = new ImItemDiscountMod();
		try {
			
			Object otherBean     = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			
			String tmpOrderNo = AjaxUtils.getTmpOrderNo(); // 產生暫存單號
    	    
            form.setOrderNo(tmpOrderNo); // 寫入暫存單號    	   
			form.setBrandCode(loginBrandCode);
			form.setStatus(OrderStatus.SAVE );
			form.setCreatedBy(loginEmployeeCode);
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setLastUpdateDate(new Date());
			
			saveModHead(form);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("建立新商品折扣主檔失敗,原因:"+e.toString());
			throw new Exception("建立新商品折扣主檔失敗,原因:"+e.toString());
		}
		return form;
	}
  
   public void saveModHead(ImItemDiscountMod imItemDiscountMod) throws Exception{
    	
    	try{
    	    imItemDiscountMod.setLastUpdateDate(new Date());
    	   
    	    imItemDiscountMod.setCreationDate(new Date());
    	    
    	    imItemDiscountModDAO.save(imItemDiscountMod);
    	    
    	}catch(Exception ex){
    		ex.printStackTrace();
    	    log.error("取得暫時單號儲存商品折扣發生錯誤，原因：" + ex.toString());
    	    throw new Exception("取得暫時單號儲存商品折扣發生錯誤，原因：" + ex.getMessage());
    	}	
    }

  public Map executeImItemDiscountInitial(Map parameterMap) throws Exception {
		
		HashMap resultMap = new HashMap(0);
		
		Map multiList = new HashMap(0);		
		Object otherBean = parameterMap.get("vatBeanOther");
		
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean,"loginEmployeeCode");
		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");
	    String formIdString  = (String)PropertyUtils.getProperty(otherBean, "formId");               
	   
	    
	    Long formId =  StringUtils.hasText(formIdString)? NumberUtils.getLong(formIdString) : null;
	    
		try {
			ImItemDiscountMod imitemDiscountMod = null;
			BuBrand buBrand = buBrandService.findById(loginBrandCode);
			Date day = new Date();
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	    	
			if(formId != null){
				
				imitemDiscountMod = findById(formId);
				if(null!=imitemDiscountMod){
					loginBrandCode  = imitemDiscountMod.getBrandCode();				   
				}	
			}else{
		    		    	
		    	imitemDiscountMod = executeNew(parameterMap);
            	}
			
		    List<BuCommonPhraseLine> allVipTypeCodes = baseDAO.findByProperty(
					"BuCommonPhraseLine", new String[] {
							"id.buCommonPhraseHead.headCode", "attribute1" }, new Object[] {
							"VIPType", loginBrandCode }, "indexNo");
			List<BuCommonPhraseLine> allItemDiscountTypes = baseDAO.findByProperty(
					"BuCommonPhraseLine", new String[] {
							"id.buCommonPhraseHead.headCode", "enable" }, new Object[] {
							"VipDiscount", "Y" }, "indexNo");
			multiList.put("allVipTypeCodes", AjaxUtils.produceSelectorData(allVipTypeCodes, "lineCode", "name", true, true));
			multiList.put("allItemDiscountTypes", AjaxUtils.produceSelectorData(allItemDiscountTypes, "lineCode", "name", true, true));
			
		    resultMap.put("multiList", multiList);			
			resultMap.put("form", imitemDiscountMod);
			resultMap.put("employeeCode",      loginEmployeeCode);
	        resultMap.put("status",            imitemDiscountMod.getStatus() );
	        resultMap.put("orderTypeCode",     "IDM" );
	        resultMap.put("branchCode",        buBrand.getBranchCode());	// 2->T2
	        resultMap.put("brandName",         buBrand.getBrandName());
	        resultMap.put("budgetType",        buBrand.getBudgetType());	// 預算扣除方式 P:UnitPrice/C:Cost/T:整筆扣除
	        resultMap.put("statusName",        OrderStatus.getChineseWord(imitemDiscountMod.getStatus()));
	        resultMap.put("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode(imitemDiscountMod.getLastUpdatedBy()));    
	        resultMap.put("createDate", 	sdf.format(day));
			
	        return resultMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("商品折扣初始化失敗，原因：" + ex.toString());
			throw new Exception("商品折扣初始化失敗，原因：" + ex.toString());
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

		Object formBindBean = parameterMap.get("vatBeanFormBind");

		Object otherBean = parameterMap.get("vatBeanOther");

		String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");

		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : 0;
		
		log.info("formId = " + formId);
		log.info("validate");
		// 驗證名稱
		//String discount = parameterMap.get("discount");
		
		String brandCode = (String) PropertyUtils.getProperty(formBindBean,"brandCode");
		String vipTypeCode = (String) PropertyUtils.getProperty(formBindBean,"vipTypeCode");
		String itemDiscountCode = (String) PropertyUtils.getProperty(formBindBean,"itemDiscountType");
		
		String discount = (String) PropertyUtils.getProperty(formBindBean,"discount");
		String beginDate = (String) PropertyUtils.getProperty(formBindBean,"beginDate");
		String endDate = (String) PropertyUtils.getProperty(formBindBean,"endDate");
		
		ImItemDiscount imItemDiscount = imItemDiscountModDAO.findByvipType(brandCode , vipTypeCode ,itemDiscountCode);
			
		
		if(!StringUtils.hasText(discount)) {
			throw new ValidationErrorException("請輸入折扣比率！");
		}else if (!StringUtils.hasText(beginDate)) {
			throw new ValidationErrorException("請輸入起始日期！");
		}else if (!StringUtils.hasText(endDate)) {
			throw new ValidationErrorException("請輸入結束日期！");
		} 
		
		if(imItemDiscount == null){
			throw new ValidationErrorException("無此卡別及類型之組合，請重新選擇！");
		}
		
		

	}
	
	/**
	 * 依formId取得實際地點主檔 in 送出
	 * 
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public ImItemDiscountMod executeFindActual(Map parameterMap)
			throws FormException, Exception {

	Object otherBean = parameterMap.get("vatBeanOther");
	
		ImItemDiscountMod imItemDiscountMod = null;
		
	
		try {			
			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
			//String brandCode    = (String)PropertyUtils.getProperty(otherBean, "brandCode");
			
			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;
			//BuBrand buBrand = buBrandService.findById( brandCode );
			
			imItemDiscountMod = null == formId ? this.executeNew(parameterMap) : this.findById(formId);
	
			parameterMap.put("entityBean", imItemDiscountMod);
			log.info("executefind:"+imItemDiscountMod.getHeadId());
			
			return imItemDiscountMod;
		} catch (Exception e) {

			log.error("取得實際主檔失敗,原因:" + e.toString());
			throw new Exception("取得實際主檔失敗,原因:" + e.toString());
		}
	}
	
	/**
	 * 前端資料塞入bean
	 * 
	 * @param parameterMap
	 * @return
	 */
	public void updateImitemDiscountModBean(Map parameterMap)
			throws FormException, Exception {
		// TODO Auto-generated method stub
		
		ImItemDiscountMod imItemDiscountMod = null;
		
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			
			Object otherBean    = parameterMap.get("vatBeanOther");
			String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");  
			imItemDiscountMod = (ImItemDiscountMod) parameterMap.get("entityBean");
			
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imItemDiscountMod);
			
			String resultMsg = modifyAjaxImItemDiscountMod(imItemDiscountMod, employeeCode);
			
			parameterMap.put("entityBean", imItemDiscountMod);
			parameterMap.put("resultMsg", resultMsg);
			
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
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
    private String modifyAjaxImItemDiscountMod(ImItemDiscountMod imItemDiscountMod, String loginUser)
	    throws ObtainSerialNoFailedException, FormException, Exception {
	//log.info("modifyAjaxPoPurchase loginUser="+loginUser);
	if (AjaxUtils.isTmpOrderNo(imItemDiscountMod.getOrderNo())) {
	    String serialNo = buOrderTypeService.getOrderSerialNo(imItemDiscountMod.getBrandCode(), imItemDiscountMod.getOrderTypeCode());
	    if (!serialNo.equals("unknow")) {
	    	imItemDiscountMod.setOrderNo(serialNo);
	    } else {
	    	throw new ObtainSerialNoFailedException("取得" + imItemDiscountMod.getOrderTypeCode() + "單號失敗！");
	    }
	}	
	modifyImItemDiscountMod( imItemDiscountMod, loginUser);
	return imItemDiscountMod.getOrderTypeCode() + "-" + imItemDiscountMod.getOrderNo() + "存檔成功！";
    }
    
    /**暫存單號取實際單號並更新至ImItemDiscountMod主檔
     * @param PoPurchaseOrderHead
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String modifyImItemDiscountMod(ImItemDiscountMod imItemDiscountMod, String loginUser)
	    throws ObtainSerialNoFailedException, FormException, Exception {
    	log.info("modifyPoPurchase loginUser="+loginUser);
    	imItemDiscountMod.setLastUpdatedBy(loginUser);
    	imItemDiscountMod.setLastUpdateDate(new Date());
    	imItemDiscountModDAO.update(imItemDiscountMod);
        return imItemDiscountMod.getOrderTypeCode() + "-" + imItemDiscountMod.getOrderNo() + "存檔成功！";
    }
    
	
	public List<Properties> getAJAXSearchModPageData(Properties httpRequest) throws Exception{
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();

			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			// ======================帶入Head的值=========================
			
	  	    String loginBrandCode = httpRequest.getProperty("loginBrandCode");
	  	    String vipTypeCode = httpRequest.getProperty("vipTypeCode");
	  	    String itemDiscountType = httpRequest.getProperty("itemDiscountType");
	  	    
  			HashMap findObjs = new HashMap();
		    findObjs.put(" and model.brandCode = :brandCode",loginBrandCode);
		    findObjs.put(" and model.vipTypeCode like :vipTypeCode","%"+vipTypeCode+"%");
		    findObjs.put(" and model.itemDiscountType like :itemDiscountType","%"+itemDiscountType+"%");
		    findObjs.put(" and model.orderNo not like :orderNo","%"+"TMP"+"%");
			// ======================取得頁面所需資料===========================
	  	    Map imItemDiscountMap = imItemDiscountModDAO.search( "ImItemDiscountMod as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	  	    
	  	    List<ImItemDiscountMod> imItemDiscountMods = 
	  	    	(List<ImItemDiscountMod>) imItemDiscountMap.get(BaseDAO.TABLE_LIST); 
	  	    
	  	    for( ImItemDiscountMod imItemDiscountMod : imItemDiscountMods ){
	  	    	imItemDiscountMod.setCreatedBy( UserUtils.getUsernameByEmployeeCode(imItemDiscountMod.getCreatedBy()));
	  	    }
	  	    log.info("ImItemDiscount.size"+ imItemDiscountMods.size());	
		    if (imItemDiscountMods != null && imItemDiscountMods.size() > 0) {
		    	
		    	// 設定額外欄位
		    	this.setModLineOtherColumn(imItemDiscountMods);
		    	
		    	Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		    	Long maxIndex = (Long)imItemDiscountModDAO.search("ImItemDiscountMod as model", "count(*) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		    	
		    	
		    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_MOD_FIELD_NAMES, GRID_SEARCH_MOD_FIELD_DEFAULT_VALUES,imItemDiscountMods, gridDatas, firstIndex, maxIndex));
		     }else {
		    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_MOD_FIELD_NAMES, GRID_SEARCH_MOD_FIELD_DEFAULT_VALUES,gridDatas));
		     }
		
		    return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的商品折扣明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的商品折扣明細失敗！");
		}
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
			ImItemDiscountMod imItemDiscountMod = (ImItemDiscountMod) parameterMap.get("entityBean");
			String formAction = (String) PropertyUtils.getProperty(otherBean,"formAction");			
			
			//-----------------------------------------

			String formStatus = (String) PropertyUtils.getProperty(otherBean,"formStatus");

			String beforeStatus = imItemDiscountMod.getStatus();
			
			String nextStatus =  getStatus(formAction,imItemDiscountMod.getStatus(),loginEmployeeCode);
			log.info("XXXXXXXXXXXXXXXXXXXXXX單據狀態XXXXXXXXXXXXXXXXXXXXXX");
			log.info("formStatus="+formStatus+"beforeStatus="+beforeStatus+"nextStatus="+nextStatus);
			if(formStatus.equals(OrderStatus.REJECT))
				nextStatus = OrderStatus.REJECT;
			log.info(nextStatus);
			//if(OrderStatus.FINISH.equals(nextStatus)){
				//回寫
				log.info("回寫----updateImItemDiscount");
				
				String brandCode = imItemDiscountMod.getBrandCode();
				String vipTypeCode = imItemDiscountMod.getVipTypeCode();
				String itemDiscountCode = imItemDiscountMod.getItemDiscountType();
				
				ImItemDiscountId imItemDiscountId = new ImItemDiscountId(brandCode, vipTypeCode, itemDiscountCode);
				
				//取得BuSupplier的pk 	
				
				ImItemDiscount imItemDiscount = imItemDiscountDAO.findById(imItemDiscountId);
				log.info("vipTypeCode="+ vipTypeCode +"itemDiscountType=" + itemDiscountCode + "brandCode="+brandCode);
				
				imItemDiscount.setId(imItemDiscountId);
				imItemDiscount.setLastUpdatedBy(imItemDiscountMod.getLastUpdatedBy());
				imItemDiscount.setDiscount(imItemDiscountMod.getDiscount());
				imItemDiscount.setBeginDate(imItemDiscountMod.getBeginDate());
				imItemDiscount.setEndDate(imItemDiscountMod.getEndDate());
				imItemDiscount.setEnable(imItemDiscountMod.getEnable());
				imItemDiscount.setLastUpdateDate(new Date());
				imItemDiscountDAO.update(imItemDiscount);

				log.info("====update====");
				
			//}
				
			imItemDiscountMod.setStatus(nextStatus);
			imItemDiscountMod.setLastUpdatedBy(loginEmployeeCode);
			imItemDiscountMod.setLastUpdateDate(new Date());
			
			//save or update
			if(imItemDiscountMod.getHeadId() == null){			
				imItemDiscountMod.setCreatedBy(loginEmployeeCode);
				imItemDiscountMod.setCreationDate(date);
				imItemDiscountModDAO.save(imItemDiscountMod);
			}else {
				imItemDiscountModDAO.update(imItemDiscountMod);
			}
					
			resultMsg = "商品折扣:存檔成功！ 是否繼續調整？";

			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", imItemDiscountMod);
			resultMap.put("vatMessage", msgBox);

			return resultMap;

		} catch (Exception ex) {
			log.error("維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("維護單存檔失敗，原因：" + ex.toString());
		}
	}
	
	private String getStatus(String formAction,String beforeStatus,String loginEmployeeCode) {
		
		log.info("取得下個狀態");
		
		String status = null;
		BuEmployee bump = buEmployeeDAO.findById(loginEmployeeCode);
				

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
		
}
