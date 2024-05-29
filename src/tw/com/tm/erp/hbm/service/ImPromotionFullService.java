package tw.com.tm.erp.hbm.service;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.ImExcessivePromotionView;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCategoryId;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImItemOnHandView;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImPriceAdjustment;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionCustomer;
import tw.com.tm.erp.hbm.bean.ImPromotionFull;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.ImPromotionShop;
import tw.com.tm.erp.hbm.bean.ImPromotionView;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployee;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployeeId;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionCustomerDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionFullDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionItemDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionShopDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionViewDAO;
import tw.com.tm.erp.hbm.dao.PosExportDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OldSysMapNewSys;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.bean.ImPromotionReCombine;
import tw.com.tm.erp.hbm.dao.ImPromotionReCombineDAO;

public class ImPromotionFullService {
    private static final Log log = LogFactory
	    .getLog(ImPromotionMainService.class);
    public static final String PROGRAM_ID = "IM_PROMOTION";
    private ImPromotionDAO imPromotionDAO;
    private ImPromotionItemDAO imPromotionItemDAO;
    private ImPromotionShopDAO imPromotionShopDAO;
    private ImPromotionCustomerDAO imPromotionCustomerDAO;
    private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
    private ImItemOnHandViewDAO imItemOnHandViewDAO;
    private BuShopDAO buShopDAO;
    private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
    private ImPromotionItemService imPromotionItemService;
    private BuOrderTypeService buOrderTypeService;
    private SiProgramLogAction siProgramLogAction;
    private BuBrandDAO buBrandDAO;
    private BuCommonPhraseService buCommonPhraseService;
    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    private BuEmployeeWithAddressViewService buEmployeeWithAddressViewService;
    private ImItemCategoryService imItemCategoryService;
    private BuEmployeeDAO buEmployeeDAO;
    private ImPromotionFullDAO imPromotionFullDAO;
    /**
     * 促銷明細
     */
    public static final String[] GRID_FIELD_NAMES = { 
    	"indexNo", "vipDiscountCode", "discountRate", "isJoin", "lineId"
    };

    public static final int[] GRID_FIELD_TYPES = { 
    	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG 
    };

    public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
    	"0", "", "", "", "0"};
    
    public static final String[] GRID_FIELD_NAMES_SHOP = { "indexNo",
	    "shopCode", "shopName", "beginDate", "endDate", "reserve5",
	    "lineId", "isLockRecord", "isDeleteRecord", "message" };

    public static final int[] GRID_FIELD_TYPES_SHOP = {
	    AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,
	    AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_STRING,
	    AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING };

    public static final String[] GRID_FIELD_DEFAULT_VALUES_SHOP = { "0", "",
	    "", "", "", "", "", AjaxUtils.IS_LOCK_RECORD_FALSE,
	    AjaxUtils.IS_DELETE_RECORD_FALSE, "" };

    public static final String[] GRID_FIELD_NAMES_CUSTOMER = { "enable",
	    "indexNo", "vipTypeCode", "vipTypeName", "lineId" };

    public static final int[] GRID_FIELD_TYPES_CUSTOMER = {
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	    AjaxUtils.FIELD_TYPE_LONG};

    public static final String[] GRID_FIELD_DEFAULT_VALUES_CUSTOMER = { "N",
	    "0", "", "", "" };
    
    
    public static final String[] GRID_SEARCH_FIELD_NAMES = { "orderTypeCode", 
	"orderNo", "promotionCode", "promotionName", "inChargeName", 
	"beginDate", "endDate", "statusName", "headId"};

    public static final int[] GRID_SEARCH_FIELD_TYPES = { 
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_DATE, 
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG};

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", 
    	"", "", "", "", 
    	"", "", "", "0"};
    
    public static final String[] GRID_FIELD_NAMES_RECOMBINE = { 
		"indexNo", "combineCode", "combineName", "combineQuantity", "combinePrice",
		"itemBrand", "itemBrandName", "reserve5", "category02", "category02Name",
		"reserve5", "unitPrice", "lineId", "reCombineId" };

    public static final int[] GRID_FIELD_TYPES_RECOMBINE = {
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,	AjaxUtils.FIELD_TYPE_LONG,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_LONG };

    public static final String[] GRID_FIELD_DEFAULT_VALUES_RECOMBINE = { "",
		"", "", "", "", "", "", "", "", "", "", "", "", "" };

    public void setImPromotionDAO(ImPromotionDAO imPromotionDAO) {
	this.imPromotionDAO = imPromotionDAO;
    }

    public void setImPromotionItemDAO(ImPromotionItemDAO imPromotionItemDAO) {
	this.imPromotionItemDAO = imPromotionItemDAO;
    }

    public void setImPromotionShopDAO(ImPromotionShopDAO imPromotionShopDAO) {
	this.imPromotionShopDAO = imPromotionShopDAO;
    }

    public void setImPromotionCustomerDAO(
	    ImPromotionCustomerDAO imPromotionCustomerDAO) {
	this.imPromotionCustomerDAO = imPromotionCustomerDAO;
    }

    public void setImItemCurrentPriceViewDAO(
	    ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
	this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
    }
    
    public void setImItemOnHandViewDAO(
    	ImItemOnHandViewDAO imItemOnHandViewDAO){
    this.imItemOnHandViewDAO = imItemOnHandViewDAO;
    }

    public void setBuShopDAO(BuShopDAO buShopDAO) {
	this.buShopDAO = buShopDAO;
    }

    public void setBuEmployeeWithAddressViewDAO(
	    BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
	this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
    }

    public void setImPromotionItemService(
	    ImPromotionItemService imPromotionItemService) {
	this.imPromotionItemService = imPromotionItemService;
    }

    public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
	this.buOrderTypeService = buOrderTypeService;
    }

    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
	this.buBrandDAO = buBrandDAO;
    }

    public void setBuCommonPhraseService(
	    BuCommonPhraseService buCommonPhraseService) {
	this.buCommonPhraseService = buCommonPhraseService;
    }

    public void setBuCommonPhraseLineDAO(
	    BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
	this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    }
    
    public void setBuEmployeeWithAddressViewService(BuEmployeeWithAddressViewService buEmployeeWithAddressViewService) {
	this.buEmployeeWithAddressViewService = buEmployeeWithAddressViewService;
    }
    
    public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
	this.imItemCategoryService = imItemCategoryService;
    }

    public ImPromotion findById(Long id) {
	return imPromotionDAO.findById(id);
    }
    
    public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
        this.buEmployeeDAO = buEmployeeDAO;
    }
    
    /**
	 * @param imPromotionFullDAO the imPromotionFullDAO to set
	 */
	public void setImPromotionFullDAO(ImPromotionFullDAO imPromotionFullDAO) {
		this.imPromotionFullDAO = imPromotionFullDAO;
	}

	private HashMap getRequestParameter(Map parameterMap, boolean isSubmitAction) throws Exception {

	Object otherBean = parameterMap.get("vatBeanOther");
	String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
	String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
	String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
	Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
	HashMap conditionMap = new HashMap();
	conditionMap.put("loginBrandCode", loginBrandCode);
	conditionMap.put("loginEmployeeCode", loginEmployeeCode);
	conditionMap.put("orderTypeCode", orderTypeCode);
	conditionMap.put("formId", formId);
	if (isSubmitAction) {
	    String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	    String formStatus = (String) PropertyUtils.getProperty(otherBean, "formStatus");
	    conditionMap.put("beforeChangeStatus", beforeChangeStatus);
	    conditionMap.put("formStatus", formStatus);
	}

	return conditionMap;
    }

    public Map executeInitial(Map parameterMap) throws Exception {

	HashMap resultMap = new HashMap();
	try {
	    HashMap argumentMap = getRequestParameter(parameterMap, false);
	    Long formId = (Long) argumentMap.get("formId");
	    ImPromotion form = null;
	    // 建立暫存單
	    if (formId == null){
		form = createNewImPromotion(argumentMap, resultMap);
	    }else{
		form = updateCurrentPromotion(argumentMap, resultMap);
	    }
	    resultMap.put("form", form);
	    refreshImPromotion(form);
	    Map multiList = new HashMap(0);
	    List<BuCommonPhraseLine> allCustomerTypes = buCommonPhraseService.getCommonPhraseLinesById("CustomerType", false);
	    List<BuCommonPhraseLine> allPriceTypes = buCommonPhraseService.getCommonPhraseLinesById("PriceType", false);
	    List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(form.getBrandCode(), "PM");
	    if("T2".equals(form.getBrandCode())){
		List<BuEmployeeWithAddressView> allPurchaseAssist = null;
                List<BuEmployeeWithAddressView> allPurchaseMember = null;
                List<BuEmployeeWithAddressView> allPurchaseMaster = null;
                List<ImItemCategory> allItemCategory = null;
		if(OrderStatus.SAVE.equals(form.getStatus()) || OrderStatus.REJECT.equals(form.getStatus())){
	            allItemCategory = imItemCategoryService.findByCategoryType(form.getBrandCode(), "ITEM_CATEGORY");
	            if(allItemCategory != null && allItemCategory.size() > 0){
	                String itemCategory = null; 
	                if(form.getItemCategory() == null){
	                    itemCategory = allItemCategory.get(0).getId().getCategoryCode();
	                }else{
	                    itemCategory = form.getItemCategory();
	                }	                
	                HashMap mapResult = getPurchaseEmployee(itemCategory);
	                allPurchaseAssist = (List<BuEmployeeWithAddressView>)mapResult.get("purchaseAssist");
	                allPurchaseMember = (List<BuEmployeeWithAddressView>)mapResult.get("purchaseMember");
	                allPurchaseMaster = (List<BuEmployeeWithAddressView>)mapResult.get("purchaseMaster");
	                if(form.getItemCategory() == null){
	                    form.setItemCategory(itemCategory);
	                    // ================預設採購助理=KEY單人員==========================	
	    		    for(BuEmployeeWithAddressView buEmployee : allPurchaseAssist){	
	    		        if(buEmployee.getEmployeeCode().equals(form.getCreatedBy())){
	    			    form.setPurchaseAssist(buEmployee.getEmployeeCode());
	    		        }
	    		    }
	    		    // ================預設採購人員=KEY單人員==========================	
	    		    for(BuEmployeeWithAddressView buEmployee : allPurchaseMember){ 
	    		        if(buEmployee.getEmployeeCode().equals(form.getCreatedBy())){
	    			    form.setPurchaseMember(buEmployee.getEmployeeCode());
	    		        }
	    		    }
	    		    //=================預設採購主管=KEY單人員===========================
	    		    for(BuEmployeeWithAddressView buEmployee : allPurchaseMaster){	
	    		        if(buEmployee.getEmployeeCode().equals(form.getCreatedBy())){
	    			    form.setPurchaseMaster(buEmployee.getEmployeeCode());
	    		        }
	    		    }
	                }
	    		multiList.put("allItemCategory", AjaxUtils.produceSelectorData(allItemCategory, "categoryCode", "categoryName",true, true));
			multiList.put("allPurchaseAssist", AjaxUtils.produceSelectorData(allPurchaseAssist, "employeeCode", "chineseName", true, true));
			multiList.put("allPurchaseMember", AjaxUtils.produceSelectorData(allPurchaseMember, "employeeCode", "chineseName", true, true));
			multiList.put("allPurchaseMaster", AjaxUtils.produceSelectorData(allPurchaseMaster, "employeeCode", "chineseName", true, true));
	            }else{
	                throw new ValidationErrorException("查無" + form.getBrandCode() + "的業種子類！");
	            }
		}else{
		    //==================================業種子類=======================================
		    allItemCategory = new ArrayList(0);
		    ImItemCategory itemCategoryPO = imItemCategoryService.findById(form.getBrandCode(), "ITEM_CATEGORY", form.getItemCategory());
		    if(itemCategoryPO != null){
			allItemCategory.add(itemCategoryPO);
			multiList.put("allItemCategory", AjaxUtils.produceSelectorData(allItemCategory, "categoryCode", "categoryName",true, true));
		    }else{
		        ImItemCategoryId itemCategoryId = new ImItemCategoryId();
		        itemCategoryId.setCategoryCode(form.getItemCategory());
		        ImItemCategory itemCategory = new ImItemCategory();
		        itemCategory.setId(itemCategoryId);		        
		        allItemCategory.add(itemCategory);
		        multiList.put("allItemCategory", AjaxUtils.produceSelectorData(allItemCategory, "categoryCode", "categoryCode",false, true));
		    }    
		    //==================================採購助理=======================================
		    allPurchaseAssist = new ArrayList(0);
		    BuEmployeeWithAddressView purchaseAssistView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(form.getBrandCode(), form.getPurchaseAssist());
		    if(purchaseAssistView != null){
			allPurchaseAssist.add(purchaseAssistView);
			multiList.put("allPurchaseAssist", AjaxUtils.produceSelectorData(allPurchaseAssist, "employeeCode", "chineseName", true, true));
		    }else{
			purchaseAssistView = new BuEmployeeWithAddressView();
			purchaseAssistView.setEmployeeCode(form.getPurchaseAssist());
			allPurchaseAssist.add(purchaseAssistView);
			multiList.put("allPurchaseAssist", AjaxUtils.produceSelectorData(allPurchaseAssist, "employeeCode", "employeeCode", false, true));
		    }
		    //==================================採購人員=======================================
		    allPurchaseMember = new ArrayList(0);
		    BuEmployeeWithAddressView purchaseMemberView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(form.getBrandCode(), form.getPurchaseMember());
		    if(purchaseMemberView != null){
			allPurchaseMember.add(purchaseMemberView);
			multiList.put("allPurchaseMember", AjaxUtils.produceSelectorData(allPurchaseMember, "employeeCode", "chineseName", true, true));
		    }else{
			purchaseMemberView = new BuEmployeeWithAddressView();
			purchaseMemberView.setEmployeeCode(form.getPurchaseMember());
			allPurchaseMember.add(purchaseMemberView);
			multiList.put("allPurchaseMember", AjaxUtils.produceSelectorData(allPurchaseMember, "employeeCode", "employeeCode", false, true));
		    }
		    //==================================採購主管=======================================
		    allPurchaseMaster = new ArrayList(0);
		    BuEmployeeWithAddressView purchaseMasterView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(form.getBrandCode(), form.getPurchaseMaster());
		    if(purchaseMasterView != null){
			allPurchaseMaster.add(purchaseMasterView);
			multiList.put("allPurchaseMaster", AjaxUtils.produceSelectorData(allPurchaseMaster, "employeeCode", "chineseName", true, true));
		    }else{
			purchaseMasterView = new BuEmployeeWithAddressView();
			purchaseMasterView.setEmployeeCode(form.getPurchaseMaster());
			allPurchaseMaster.add(purchaseMasterView);
			multiList.put("allPurchaseMaster", AjaxUtils.produceSelectorData(allPurchaseMaster, "employeeCode", "employeeCode", false, true));
		    }
		}
	    }else{
	    	
	    	
            List<ImItemCategory> allItemCategory = null;
	        if(OrderStatus.SAVE.equals(form.getStatus()) || OrderStatus.REJECT.equals(form.getStatus())){
               allItemCategory = imItemCategoryService.findByCategoryType(form.getBrandCode(), "ITEM_CATEGORY");
               if(allItemCategory != null && allItemCategory.size() > 0){
                  String itemCategory = null; 
                  if(form.getItemCategory() == null){
                     itemCategory = allItemCategory.get(0).getId().getCategoryCode();
                  }else{
                     itemCategory = form.getItemCategory();
                  }	                
               
                  if(form.getItemCategory() == null){
                     form.setItemCategory(itemCategory);
                  }
    		multiList.put("allItemCategory", AjaxUtils.produceSelectorData(allItemCategory, "categoryCode", "categoryName",true, true));
		
	            }else{
	                throw new ValidationErrorException("查無" + form.getBrandCode() + "的業種！");
	            }
			}else{
			    //==================================業種子類=======================================
			    allItemCategory = new ArrayList(0);
			    ImItemCategory itemCategoryPO = imItemCategoryService.findById(form.getBrandCode(), "ITEM_CATEGORY", form.getItemCategory());
			    if(itemCategoryPO != null){
				allItemCategory.add(itemCategoryPO);
				multiList.put("allItemCategory", AjaxUtils.produceSelectorData(allItemCategory, "categoryCode", "categoryName",true, true));
			    }else{
			        ImItemCategoryId itemCategoryId = new ImItemCategoryId();
			        itemCategoryId.setCategoryCode(form.getItemCategory());
			        ImItemCategory itemCategory = new ImItemCategory();
			        itemCategory.setId(itemCategoryId);		        
			        allItemCategory.add(itemCategory);
			        multiList.put("allItemCategory", AjaxUtils.produceSelectorData(allItemCategory, "categoryCode", "categoryCode",false, true));
			    }    
			}
        }
	    
	    multiList.put("allCustomerTypes", AjaxUtils.produceSelectorData(allCustomerTypes, "lineCode", "name", false, false));
	    multiList.put("allPriceTypes", AjaxUtils.produceSelectorData(allPriceTypes, "lineCode", "name", false, false));
	    multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
	    resultMap.put("multiList", multiList);

	    return resultMap;
	} catch (Exception ex) {
	    log.error("促銷單初始化失敗，原因：" + ex.toString());
	    throw new Exception("促銷單初始化失敗，原因：" + ex.toString());
	}
    }

    public ImPromotion createNewImPromotion(Map argumentMap, Map resultMap) throws Exception {

	try {
	    String loginBrandCode = (String) argumentMap.get("loginBrandCode");
	    String loginEmployeeCode = (String) argumentMap.get("loginEmployeeCode");
	    String orderTypeCode = (String) argumentMap.get("orderTypeCode");
	    BuOrderTypeId orderTypeId = new BuOrderTypeId(loginBrandCode, orderTypeCode);
	    BuOrderType orderTypePO = buOrderTypeService.findById(orderTypeId);
	    if (orderTypePO == null) {
		throw new NoSuchObjectException("查無品牌(" + loginBrandCode
			+ ")、單別(" + orderTypeCode + ")的單別資料！");
	    }
	    // 將此品牌的VIPTYPE塞入此促銷單
	    List imPromotionFulls= new ArrayList(0);
	    getVipDiscountInfomation(imPromotionFulls, loginBrandCode, true);
	    ImPromotion form = new ImPromotion();
	    form.setBrandCode(loginBrandCode);
	    form.setOrderTypeCode(orderTypeCode);
	    form.setPriceType(orderTypePO.getPriceType());
	    form.setStatus(OrderStatus.SAVE);
	    form.setInCharge(loginEmployeeCode);
	    form.setDiscount(0L);
	    form.setIsAllItem("N");
	    form.setIsAllShop("N");
	    form.setIsAllCustomer("N");
	    form.setCreatedBy(loginEmployeeCode);
	    form.setLastUpdatedBy(loginEmployeeCode);
	    form.setImPromotionFulls(imPromotionFulls);
	    saveTmp(form);
	    
	    return form;
	} catch (Exception ex) {
	    log.error("產生新促銷單失敗，原因：" + ex.toString());
	    throw new Exception("產生新促銷單發生錯誤！");
	}
    }

    public ImPromotion updateCurrentPromotion(Map argumentMap, Map resultMap) throws FormException, Exception {

	try {
	    Long formId = (Long) argumentMap.get("formId");
	    //String loginBrandCode = (String) argumentMap.get("loginBrandCode");
	    ImPromotion form = findById(formId);
	    if (form != null) {
		if(OrderStatus.SAVE.equals(form.getStatus()) || OrderStatus.REJECT.equals(form.getStatus())){
			getVipDiscountInfomation(form.getImPromotionFulls(), form.getBrandCode(), false);
		    imPromotionDAO.update(form);
		}
		return form;
	    } else {
		throw new NoSuchObjectException("查無促銷單主鍵(" + formId + ")的資料！");
	    }
	} catch (FormException fe) {
	    log.error("查詢促銷單失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("查詢促銷單發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢促銷單發生錯誤！");
	}
    }
    
    /**
     * 取得品牌的VIPTYPE並塞入此促銷單
     * 
     * @param imPromotionCustomers
     * @param brandCode
     * @param isNew
     */
    private void getVipDiscountInfomation(List<ImPromotionFull> imPromotionFulls, String brandCode, boolean isNew){
    	List<BuCommonPhraseLine> allVIPDiscount = buCommonPhraseService.findEnableLineById("VipDiscount");
    	long indexNo = 1;
    	if (allVIPDiscount != null && allVIPDiscount.size() > 0) {
    		if(isNew){
		    	for (BuCommonPhraseLine vipDiscount : allVIPDiscount) {
		    		String vipDiscountCode = vipDiscount.getId().getLineCode();
		    		if(!"00".equals(vipDiscountCode)){
			    		ImPromotionFull imPromotionFull = new ImPromotionFull();
			    		imPromotionFull.setVipDiscountCode(vipDiscountCode);
			    		imPromotionFull.setDiscountRate(0L);
			    		imPromotionFull.setIsJoin("Y");
			    		imPromotionFulls.add(imPromotionFull);
		    		}
		    	}
    		}
    	}
    }

    /**
     * 取得暫時單號存檔
     * 
     * @param promotion
     * @throws Exception
     */
    public void saveTmp(ImPromotion promotion) throws Exception {

	try {
	    String tmpOrderNo = AjaxUtils.getTmpOrderNo();
	    promotion.setOrderNo(tmpOrderNo);
	    promotion.setLastUpdateDate(new Date());
	    promotion.setCreationDate(new Date());
	    imPromotionDAO.save(promotion);
	} catch (Exception ex) {
	    log.error("取得暫時單號儲存促銷單發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得暫時單號儲存促銷單發生錯誤，原因：" + ex.getMessage());
	}
    }

    private void refreshImPromotion(ImPromotion form) {

	String brandCode = form.getBrandCode();
	String brandName = "";
	BuBrand buBrandPO = buBrandDAO.findById(brandCode);
	if (buBrandPO == null) {
	    brandName = "查無此品牌資料";
	} else {
	    brandName = buBrandPO.getBrandName();
	}
	form.setBrandName(brandName);

	// 負責人員
	String inChargeName = "";
	if (StringUtils.hasText(form.getInCharge())) {
	    BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.
	            findbyBrandCodeAndEmployeeCode(brandCode, form.getInCharge());
	    if (employeeWithAddressView == null) {
		inChargeName = "查無此員工資料";
	    } else {
		inChargeName = employeeWithAddressView.getChineseName();
	    }
	}
	form.setInChargeName(inChargeName);
	// 填單人員
	String createdByName = "";
	if (StringUtils.hasText(form.getCreatedBy())) {
	    BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO
		    .findbyBrandCodeAndEmployeeCode(brandCode, form.getCreatedBy());
	    if (employeeWithAddressView == null) {
		createdByName = "查無此員工資料";
	    } else {
		createdByName = employeeWithAddressView.getChineseName();
	    }
	}
	form.setCreatedByName(createdByName);
	// 狀態
	form.setStatusName(OrderStatus.getChineseWord(form.getStatus()));
    }

    public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception {

	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
	    // ======================帶入Head的值=========================
	    String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
	    String formStatus = httpRequest.getProperty("formStatus");// 狀態
	    
	    HashMap map = new HashMap();
	    map.put("headId", headId);
	    map.put("startPage", iSPage);
	    map.put("pageSize", iPSize);
	    map.put("brandCode", brandCode);
	    map.put("formStatus", formStatus);
	    // ======================更新品號的價格資訊=========================
//	    if (OrderStatus.SAVE.equals(formStatus) || OrderStatus.REJECT.equals(formStatus)) {
//		imPromotionItemService.savePartialItemData(map);
//	    }
	    // ======================取得頁面所需資料===========================
	    List<ImPromotionFull> imPromotionFulls = imPromotionFullDAO.findPageLine(headId, iSPage, iPSize);
	    if (imPromotionFulls != null && imPromotionFulls.size() > 0) {
		// 取得第一筆的INDEX
		Long firstIndex = imPromotionFulls.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		Long maxIndex = imPromotionFullDAO.findPageLineMaxIndex(headId);
		//refreshItemData(map, imPromotionFulls);
		result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, imPromotionFulls, 
			gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的促銷商品明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的促銷商品明細失敗！");
	}
    }

    /**
     * 更新促銷商品明細資料
     * 
     * @param parameterMap
     * @param promotionItems
     */
    private void refreshItemData(HashMap parameterMap, List<ImPromotionItem> promotionItems) throws Exception{

	for (ImPromotionItem promotionItem : promotionItems) {
	    getPromotionItemRelationData(parameterMap, promotionItem, false);
	}
    }

    private void getPromotionItemRelationData(HashMap parameterMap, ImPromotionItem promotionItem, boolean isReplace) throws Exception{

	String brandCode = (String) parameterMap.get("brandCode");
	String priceType = (String) parameterMap.get("priceType");
	String itemName = "查無此商品資料";
	String itemCode = promotionItem.getItemCode();
	// 品名
	if (StringUtils.hasText(itemCode)) {
	    ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findCurrentPrice(brandCode, itemCode, priceType);
	    if (imItemCurrentPriceView != null) {
		promotionItem.setItemName(imItemCurrentPriceView.getItemCName());
		promotionItem.setCategory02(imItemCurrentPriceView.getCategory02()); // 中類	
		
		ImItemCategory itemCategoryPO = imItemCategoryService.findById(brandCode, ImItemCategoryDAO.CATEGORY02, imItemCurrentPriceView.getCategory02());
		if(null != itemCategoryPO ){
		    promotionItem.setCategory02Name(itemCategoryPO.getCategoryName()); // 中類名稱
		}else{
		    promotionItem.setCategory02Name(MessageStatus.DATA_NOT_FOUND);
		}
		if (isReplace) {
		    promotionItem.setStandardPurchaseCost(imItemCurrentPriceView.getStandardPurchaseCost());
		    promotionItem.setOriginalPrice(imItemCurrentPriceView.getUnitPrice());
		}
	    } else {
		promotionItem.setItemName(itemName);
	    }
	    
	    Double onHandQty = imItemOnHandViewDAO.getOnHandQtyByItemCode(brandCode,itemCode,"");
	    promotionItem.setStockOnHandQty((onHandQty==null?0:onHandQty));
	    
	    if (promotionItem.getOriginalPrice() != null && promotionItem.getOriginalPrice() > 0D){
	    	promotionItem.setMargenBeforeDisc(CommonUtils.round((promotionItem.getOriginalPrice()- promotionItem.getStandardPurchaseCost()) / promotionItem.getOriginalPrice() * 100D,1));
	    }
	    else
	    {
	    	promotionItem.setMargenBeforeDisc(0D);
	    }
	    
	    if (promotionItem.getTotalDiscountAmount() != null && promotionItem.getTotalDiscountAmount() > 0D){
	    	promotionItem.setMargenAfterDisc(CommonUtils.round((promotionItem.getTotalDiscountAmount() - promotionItem.getStandardPurchaseCost()) / promotionItem.getTotalDiscountAmount() * 100D,1));
	    }
	    else
	    {
	    	promotionItem.setMargenAfterDisc(0D);
	    }
	    //ImItemOnHandView imItemOnHandView = imItemOnHandViewDAO.getOnHandQtyByItemCode
	} else {
	    promotionItem.setItemName("");
	    promotionItem.setMargenAfterDisc(0D);
	    promotionItem.setMargenBeforeDisc(0D);
	}
    }

    /**
     * 處理AJAX參數(促銷單item line變動時計算)
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws ValidationErrorException
     */
    public List<Properties> getAJAXItemData(Properties httpRequest) throws ValidationErrorException {
    System.out.println("================程式進入==========");
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String itemIndexNo = null;
	try {
	    itemIndexNo = httpRequest.getProperty("itemIndexNo");
	    String brandCode = httpRequest.getProperty("brandCode");
	    String priceType = httpRequest.getProperty("priceType");// 價格類型
	    String itemCode = httpRequest.getProperty("itemCode");
	    String discountAmountTemp = httpRequest.getProperty("discountAmount");//
	    String discountPercentageTemp = httpRequest.getProperty("discountPercentage");
	    String quantityTemp = httpRequest.getProperty("quantity");
	    Double discountAmount = null;
	    Double discountPercentage = null;
	    Double quantity = null;
	    if (StringUtils.hasText(itemCode)) {
		itemCode = itemCode.trim().toUpperCase();
	    }
	    if (StringUtils.hasText(discountAmountTemp)) {
		discountAmount = NumberUtils.getDouble(discountAmountTemp);
		discountAmount = CommonUtils.round(discountAmount, 0);
	    }
	    if (StringUtils.hasText(discountPercentageTemp)) {
		discountPercentage = NumberUtils.getDouble(discountPercentageTemp);
		discountPercentage = CommonUtils.round(discountPercentage, 1);
	    }
	    if (StringUtils.hasText(quantityTemp)) {
		quantity = NumberUtils.getDouble(quantityTemp);
	    }

	    HashMap map = new HashMap();
	    map.put("brandCode", brandCode);
	    map.put("priceType", priceType);
	    ImPromotionItem promotionItem = new ImPromotionItem();
	    promotionItem.setItemCode(itemCode);
	    promotionItem.setDiscountAmount(discountAmount);
	    promotionItem.setDiscountPercentage(discountPercentage);
	    promotionItem.setQuantity(quantity);
	    // 取得item的相關資料
	    System.out.println("================程式進入1111==========");
	    getPromotionItemRelationData(map, promotionItem, true);
	    if (promotionItem.getOriginalPrice() != null && promotionItem.getOriginalPrice() > 0D) {
		if (promotionItem.getDiscountAmount() != null && promotionItem.getDiscountAmount() != 0D) {
		    promotionItem.setDiscountPercentage(null);
		    promotionItem.setTotalDiscountAmount(promotionItem.getDiscountAmount());
		    promotionItem.setTotalDiscountPercentage(CommonUtils.round(
			    (promotionItem.getDiscountAmount() / promotionItem.getOriginalPrice() * 100D), 1));
		} else if (promotionItem.getDiscountPercentage() != null) {
		    promotionItem.setDiscountAmount(null);
		    promotionItem.setTotalDiscountAmount(CommonUtils.round(
			    (promotionItem.getOriginalPrice() * (promotionItem.getDiscountPercentage() / 100D)), 0));
		    promotionItem.setTotalDiscountPercentage(promotionItem.getDiscountPercentage());
		}
	    } else {
		promotionItem.setDiscountAmount(null);
		promotionItem.setDiscountPercentage(null);
	    }
	    
	    if (promotionItem.getOriginalPrice() != null && promotionItem.getOriginalPrice() > 0D){
	    	promotionItem.setMargenBeforeDisc(CommonUtils.round((promotionItem.getOriginalPrice()- promotionItem.getStandardPurchaseCost()) / promotionItem.getOriginalPrice() * 100D,1));
	    }
	    else
	    {
	    	promotionItem.setMargenBeforeDisc(0D);
	    }
	    
	    if (promotionItem.getTotalDiscountAmount() != null && promotionItem.getTotalDiscountAmount() > 0D){
	    	promotionItem.setMargenAfterDisc(CommonUtils.round((promotionItem.getTotalDiscountAmount() - promotionItem.getStandardPurchaseCost()) / promotionItem.getTotalDiscountAmount() * 100D,1));
	    }
	    else
	    {
	    	promotionItem.setMargenAfterDisc(0D);
	    }
	    
	    properties.setProperty("ItemCode", AjaxUtils.getPropertiesValue(promotionItem.getItemCode(), ""));
	    properties.setProperty("Category02", AjaxUtils.getPropertiesValue(promotionItem.getCategory02(), ""));
	    properties.setProperty("Category02Name", AjaxUtils.getPropertiesValue(promotionItem.getCategory02Name(), ""));
	    properties.setProperty("ItemName", AjaxUtils.getPropertiesValue(promotionItem.getItemName(), ""));
	    properties.setProperty("StandardPurchaseCost", AjaxUtils.getPropertiesValue(promotionItem.getStandardPurchaseCost(), ""));
	    properties.setProperty("OriginalPrice", AjaxUtils.getPropertiesValue(promotionItem.getOriginalPrice(), ""));
	    properties.setProperty("DiscountAmount", AjaxUtils.getPropertiesValue(promotionItem.getDiscountAmount(), ""));
	    properties.setProperty("DiscountPercentage", AjaxUtils.getPropertiesValue(promotionItem.getDiscountPercentage(), ""));
	    properties.setProperty("TotalDiscountAmount", AjaxUtils.getPropertiesValue(promotionItem.getTotalDiscountAmount(), ""));
	    properties.setProperty("TotalDiscountPercentage", AjaxUtils.getPropertiesValue(promotionItem.getTotalDiscountPercentage(), ""));
	    properties.setProperty("Quantity", AjaxUtils.getPropertiesValue(promotionItem.getQuantity(), ""));
	    properties.setProperty("StockOnHandQty", AjaxUtils.getPropertiesValue(promotionItem.getStockOnHandQty(), ""));
	    properties.setProperty("MrgenBeforeDisc", AjaxUtils.getPropertiesValue(promotionItem.getMargenBeforeDisc(), ""));
	    properties.setProperty("MargenAfterDisc", AjaxUtils.getPropertiesValue(promotionItem.getMargenAfterDisc(), ""));

	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("更新商品資料頁籤中第 " + itemIndexNo + "項明細的資料發生錯誤，原因："
		    + ex.getMessage());
	    throw new ValidationErrorException("更新商品資料頁籤中第 " + itemIndexNo
		    + "項明細的資料失敗！");
	}
    }

    public List<Properties> getAJAXPageDataForShop(Properties httpRequest) throws Exception {

	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
	    // ======================帶入Head的值=========================
	    String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
	    String formStatus = httpRequest.getProperty("formStatus");// 狀態
	    HashMap map = new HashMap();
	    map.put("brandCode", brandCode);
	    map.put("formStatus", formStatus);
	    // ======================取得頁面所需資料===========================
	    List<ImPromotionShop> imPromotionShops = imPromotionShopDAO.findPageLine(headId, iSPage, iPSize);
	    if (imPromotionShops != null && imPromotionShops.size() > 0) {
		// 取得第一筆的INDEX
		Long firstIndex = imPromotionShops.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		Long maxIndex = imPromotionShopDAO.findPageLineMaxIndex(headId);
		refreshShopData(map, imPromotionShops);
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES_SHOP, GRID_FIELD_DEFAULT_VALUES_SHOP,
			imPromotionShops, gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES_SHOP, GRID_FIELD_DEFAULT_VALUES_SHOP, gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的促銷專櫃明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的促銷專櫃明細失敗！");
	}
    }

    /**
     * 更新促銷專櫃明細資料
     * 
     * @param parameterMap
     * @param promotionItems
     */
    private void refreshShopData(HashMap parameterMap, List<ImPromotionShop> promotionShops) {

	for (ImPromotionShop promotionShop : promotionShops) {
	    getShopRelationData(parameterMap, promotionShop);
	}
    }

    private void getShopRelationData(HashMap parameterMap, ImPromotionShop promotionShop) {

	String brandCode = (String) parameterMap.get("brandCode");
	String formStatus = (String) parameterMap.get("formStatus");
	String shopName = "查無此專櫃資料";

	if (StringUtils.hasText(promotionShop.getShopCode())) {
	    BuShop buShop = null;
	    if (OrderStatus.SAVE.equals(formStatus) || OrderStatus.REJECT.equals(formStatus)) {
		buShop = buShopDAO.findEnableShopById(brandCode, promotionShop.getShopCode());
	    } else {
		buShop = buShopDAO.findShopByBrandCodeAndShopCode(brandCode, promotionShop.getShopCode());
	    }
	    if (buShop != null) {
		promotionShop.setShopName(buShop.getShopCName());
	    } else {
		promotionShop.setShopName(shopName);
	    }
	} else {
	    promotionShop.setShopName("");
	}
    }

    /**
     * 處理AJAX參數(促銷單shop line變動時計算)
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws ValidationErrorException
     */
    public List<Properties> getAJAXShopData(Properties httpRequest) throws ValidationErrorException {

	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String itemIndexNo = null;
	try {
	    itemIndexNo = httpRequest.getProperty("itemIndexNo");
	    String brandCode = httpRequest.getProperty("brandCode");
	    String shopCode = httpRequest.getProperty("shopCode");
	    String formStatus = httpRequest.getProperty("formStatus");// 狀態
	    if (StringUtils.hasText(shopCode)) {
		shopCode = shopCode.trim().toUpperCase();
	    }

	    HashMap map = new HashMap();
	    map.put("brandCode", brandCode);
	    map.put("formStatus", formStatus);
	    ImPromotionShop promotionShop = new ImPromotionShop();
	    promotionShop.setShopCode(shopCode);
	    // 取得shop的相關資料
	    getShopRelationData(map, promotionShop);
	    properties.setProperty("ShopCode", AjaxUtils.getPropertiesValue(promotionShop.getShopCode(), ""));
	    properties.setProperty("ShopName", AjaxUtils.getPropertiesValue(promotionShop.getShopName(), ""));
	    System.out.println("ShopCode=" + promotionShop.getShopCode());
	    System.out.println("ShopName=" + promotionShop.getShopName());

	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("更新參與專櫃頁籤中第 " + itemIndexNo + "項明細的資料發生錯誤，原因："+ ex.getMessage());
	    throw new ValidationErrorException("更新參與專櫃頁籤中第 " + itemIndexNo + "項明細的資料失敗！");
	}
    }

    public List<Properties> getAJAXPageDataForCustomer(Properties httpRequest) throws Exception {

	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
	    // ======================帶入Head的值=========================
	    String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
	    String formStatus = httpRequest.getProperty("formStatus");// 狀態
	    HashMap map = new HashMap();
	    map.put("brandCode", brandCode);
	    map.put("formStatus", formStatus);
	    // ======================取得頁面所需資料===========================
	    List<ImPromotionCustomer> imPromotionCustomers = imPromotionCustomerDAO.findPageLine(headId, iSPage, iPSize);
	    if (imPromotionCustomers != null && imPromotionCustomers.size() > 0) {
		// 取得第一筆的INDEX
		Long firstIndex = imPromotionCustomers.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		Long maxIndex = imPromotionCustomerDAO.findPageLineMaxIndex(headId);
		refreshCustomerData(map, imPromotionCustomers);
		result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES_CUSTOMER, GRID_FIELD_DEFAULT_VALUES_CUSTOMER,
			imPromotionCustomers, gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES_CUSTOMER, GRID_FIELD_DEFAULT_VALUES_CUSTOMER, gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的促銷客戶明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的促銷客戶明細失敗！");
	}
    }

    /**
     * 更新促銷客戶明細資料
     * 
     * @param parameterMap
     * @param promotionItems
     */
    private void refreshCustomerData(HashMap parameterMap, List<ImPromotionCustomer> promotionCustomers) {

	for (ImPromotionCustomer promotionCustomer : promotionCustomers) {
	    getCustomerRelationData(parameterMap, promotionCustomer);
	}
    }

    private void getCustomerRelationData(HashMap parameterMap, ImPromotionCustomer promotionCustomer) {

	String customerName = "查無此客戶類別資料";
	if (StringUtils.hasText(promotionCustomer.getVipTypeCode())) {
	    BuCommonPhraseLineId id = new BuCommonPhraseLineId(new BuCommonPhraseHead("VIPType"), promotionCustomer.getVipTypeCode());
	    BuCommonPhraseLine commonPhraseLine = buCommonPhraseLineDAO.findById(id);
	    if (commonPhraseLine != null) {
		customerName = commonPhraseLine.getName();
	    }
	} else {
	    customerName = "";
	}
	promotionCustomer.setVipTypeName(customerName);
    }

    /**
     * 更新PAGE的LINE
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception {
    	log.info("updateAJAXPageLinesData");
    	try {
    		String status = httpRequest.getProperty("status");
    		String errorMsg = null;
    		if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)) {
    			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
    			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
    			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
    			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
    			if (headId == null) {
    				throw new ValidationErrorException("傳入的促銷單主鍵為空值！");
    			}


    			ImPromotion promotion = new ImPromotion();
    			promotion.setHeadId(headId);
    			// 將STRING資料轉成List Properties record data
    			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
    			// Get INDEX NO
    			int indexNo = imPromotionFullDAO.findPageLineMaxIndex(headId).intValue();
    			if (upRecords != null) {
    				for (Properties upRecord : upRecords) {
    					// 先載入HEAD_ID OR LINE DATA
    					Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
    					String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);
    					if (StringUtils.hasText(itemCode)) {
    						ImPromotionFull promotionFullPO = imPromotionFullDAO.findItemByIdentification(headId, lineId);
    						if (promotionFullPO != null) {
    							log.info("promotionFullPO.update");
    							AjaxUtils.setPojoProperties(promotionFullPO,upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
    							imPromotionFullDAO.update(promotionFullPO);
    						} else {
    							log.info("promotionFullPO.save");
    							indexNo++;
    							ImPromotionFull promotionFull = new ImPromotionFull();
    							AjaxUtils.setPojoProperties(promotionFull, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
    							promotionFull.setIndexNo(Long.valueOf(indexNo));
    							promotionFull.setImPromotion(promotion);
    							imPromotionFullDAO.save(promotionFull);
    						}
    					}
    				}
    			}
    		}

    		return AjaxUtils.getResponseMsg(errorMsg);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		log.error("更新促銷商品明細時發生錯誤，原因：" + ex.toString());
    		throw new Exception("更新促銷商品明細失敗！");
    	}
    }

    /**
     * 更新PAGE的SHOP LINE
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> updateAJAXShopLinesData(Properties httpRequest) throws Exception {

	try {
	    String status = httpRequest.getProperty("status");
	    String errorMsg = null;
	    if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)) {
		String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		if (headId == null) {
		    throw new ValidationErrorException("傳入的促銷單主鍵為空值！");
		}

		ImPromotion promotion = new ImPromotion();
		promotion.setHeadId(headId);
		// 將STRING資料轉成List Properties record data
		List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES_SHOP);
		// Get INDEX NO
		int indexNo = imPromotionShopDAO.findPageLineMaxIndex(headId).intValue();
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
			// 先載入HEAD_ID OR LINE DATA
			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
			String shopCode = upRecord.getProperty(GRID_FIELD_NAMES_SHOP[1]);
			if (StringUtils.hasText(shopCode)) {
			    ImPromotionShop promotionShopPO = imPromotionShopDAO.findShopByIdentification(headId, lineId);
			    if (promotionShopPO != null) {
				AjaxUtils.setPojoProperties(promotionShopPO,upRecord, GRID_FIELD_NAMES_SHOP, GRID_FIELD_TYPES_SHOP);
				imPromotionShopDAO.update(promotionShopPO);
			    } else {
				indexNo++;
				ImPromotionShop promotionShop = new ImPromotionShop();
				AjaxUtils.setPojoProperties(promotionShop, upRecord, GRID_FIELD_NAMES_SHOP, GRID_FIELD_TYPES_SHOP);
				promotionShop.setIndexNo(Long.valueOf(indexNo));
				promotionShop.setImPromotion(promotion);
				imPromotionShopDAO.save(promotionShop);
			    }
			}
		    }
		}
	    }

	    return AjaxUtils.getResponseMsg(errorMsg);
	} catch (Exception ex) {
	    System.out.println(ex.toString());
	    log.error("更新促銷專櫃明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新促銷專櫃明細失敗！");
	}
    }

    /**
     * 更新PAGE的CUSTOMER LINE
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> updateAJAXCustomerLinesData(Properties httpRequest) throws Exception {

	try {
	    String status = httpRequest.getProperty("status");
	    String errorMsg = null;
	    if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)) {
		String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		if (headId == null) {
		    throw new ValidationErrorException("傳入的促銷單主鍵為空值！");
		}

		ImPromotion promotion = new ImPromotion();
		promotion.setHeadId(headId);
		// 將STRING資料轉成List Properties record data
		List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES_CUSTOMER);
		// Get INDEX NO
		//int indexNo = imPromotionCustomerDAO.findPageLineMaxIndex(headId).intValue();
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
			// 先載入HEAD_ID OR LINE DATA
			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
			String vipTypeCode = upRecord.getProperty(GRID_FIELD_NAMES_CUSTOMER[2]);
			if (StringUtils.hasText(vipTypeCode)) {
			    ImPromotionCustomer promotionCustomerPO = imPromotionCustomerDAO.findCustomerByIdentification(headId, lineId);
			    if (promotionCustomerPO != null) {
				AjaxUtils.setPojoProperties(promotionCustomerPO, upRecord, GRID_FIELD_NAMES_CUSTOMER, GRID_FIELD_TYPES_CUSTOMER);
				imPromotionCustomerDAO.update(promotionCustomerPO);
			    } 
			}
		    }
		}
	    }

	    return AjaxUtils.getResponseMsg(errorMsg);
	} catch (Exception ex) {
	    System.out.println(ex.toString());
	    log.error("更新促銷客戶明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新促銷客戶明細失敗！");
	}
    }
    
    /**
     * 合計所有Item的成本總金額、定價總金額、折扣後總金額、折扣總金額、平均折扣率
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws ValidationErrorException
     */
    public List<Properties> performCountTotalAmount(Properties httpRequest) throws ValidationErrorException {

        try{
	       List<Properties> result = new ArrayList();
	       Properties properties = new Properties();
	       //===================取得傳遞的的參數===================
	       Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	       ImPromotion promotionPO = findById(headId);    
	       if(promotionPO == null){
                throw new ValidationErrorException("查無促銷單主鍵(" + headId + ")的資料！");
	       }
	       //============計算成本總金額、定價總金額、折扣後總金額、折扣總金額、平均折扣率==========
	       Double totalCostAmount = 0D;
	       Double totalPriceAmount = 0D;
	       Double totalDiscountAmount = 0D;
	       Double totalAfterDiscountAmount = 0D;
	       Double averageDiscountRate = 0D;;
	       List<ImPromotionItem> promotionItems = promotionPO.getImPromotionItems();	    
	       if(promotionItems != null && promotionItems.size() > 0){
	           for(ImPromotionItem promotionItem : promotionItems){
	               if(!"1".equals(promotionItem.getIsDeleteRecord())){
	                   Double quantity = (promotionItem.getQuantity() == null || promotionItem.getQuantity() == 0D)?1D:promotionItem.getQuantity();  	            
	                   Double discountAmount = (promotionItem.getTotalDiscountAmount() == null)?0D:promotionItem.getTotalDiscountAmount();
	                   Double originalPrice = (promotionItem.getOriginalPrice() == null)?0D:promotionItem.getOriginalPrice();
                        Double standardPurchaseCost = (promotionItem.getStandardPurchaseCost() == null)?0D:promotionItem.getStandardPurchaseCost();	            
	           	 
	                   totalCostAmount += standardPurchaseCost * quantity;
	                   totalPriceAmount += originalPrice * quantity;
	                   totalDiscountAmount += discountAmount * quantity;
	               }
		   }
            }
	       //=============================================================
	       totalAfterDiscountAmount = totalPriceAmount - totalDiscountAmount;
	       if(totalPriceAmount != 0D){
	           averageDiscountRate = OperationUtils.round((totalDiscountAmount / totalPriceAmount) * 100D, 4).doubleValue();
	       }
	    	    
	       properties.setProperty("TotalCostAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalCostAmount, 0), "0.0"));
	       properties.setProperty("TotalPriceAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalPriceAmount, 0), "0.0"));
	       properties.setProperty("TotalDiscountAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalDiscountAmount, 0), "0.0"));
	       properties.setProperty("TotalAfterDiscountAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalAfterDiscountAmount, 0), "0.0"));
	       properties.setProperty("AverageDiscountRate", AjaxUtils.getPropertiesValue(averageDiscountRate, "0.0"));    
	       result.add(properties);
	    
	       return result;
	   }catch (Exception ex) {
	       log.error("促銷單金額統計失敗，原因：" + ex.toString());
	       throw new ValidationErrorException("促銷單金額統計失敗！");
	   } 
    }
    
    /**
     * 匯入促銷商品明細
     * 
     * @param headId
     * @param promotionItems
     * @throws Exception
     */
    public void executeImportPromotionItems(Long headId, List promotionItems) throws Exception{
        
        try{
    	    ImPromotion promotionPO = findById(headId);
    	    if(promotionPO == null){
	        throw new NoSuchObjectException("查無促銷貨單主鍵(" + headId + ")的資料");
	    }       	
    	    if(promotionItems != null && promotionItems.size() > 0){
    	        promotionPO.setImPromotionItems(promotionItems);
    	    }else{
    	        promotionPO.setImPromotionItems(new ArrayList(0));
    	    }
    	
    	    imPromotionDAO.update(promotionPO);      	
        }catch (Exception ex) {
	    log.error("促銷商品明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("促銷商品明細匯入時發生錯誤，原因：" + ex.getMessage());
	}        
    }
    
    public String getIdentification(Long headId) throws Exception{
	   
        String id = null;
	try{
	    ImPromotion promotion = findById(headId);
	    if(promotion != null){
                id = MessageStatus.getIdentification(promotion.getBrandCode(), 
	        promotion.getOrderTypeCode(), promotion.getOrderNo());
	    }else{
	        throw new NoSuchDataException("促銷單主檔查無主鍵(" + headId + ")的資料！");
	    }
	    
	    return id;
	}catch(Exception ex){
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
	}	   
    }
    
    public Long getPromotionHeadId(Object bean) throws FormException, Exception{

    	Long headId = null;
    	String id = (String)PropertyUtils.getProperty(bean, "headId");
    	if(StringUtils.hasText(id)){
    		headId = NumberUtils.getLong(id);
    	}else{
    		throw new ValidationErrorException("傳入的促銷單主鍵為空值！");
    	}

    	return headId;
    }
    
    public ImPromotion getActualPromotion(Long headId) throws FormException, Exception{
        
        ImPromotion promotionPO = findById(headId);
	if(promotionPO  == null){
	    throw new NoSuchObjectException("查無促銷單主鍵(" + headId + ")的資料！");
	}
        
	return promotionPO;
    }
    
    /**
     * 暫存單號取實際單號並更新至Promotion主檔及明細檔
     * 
     * @param promotion
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String modifyImPromotion(ImPromotion promotion, String loginUser)
    throws ObtainSerialNoFailedException, FormException, Exception {
    	log.info("modifyImPromotion");
    	if (AjaxUtils.isTmpOrderNo(promotion.getOrderNo())) {
    		String serialNo = buOrderTypeService.getOrderSerialNo(
    				promotion.getBrandCode(), promotion.getOrderTypeCode());
    		if (!serialNo.equals("unknow")) {
    			promotion.setOrderNo(serialNo);
    		} else {
    			throw new ObtainSerialNoFailedException("取得" + promotion.getOrderTypeCode() + "單號失敗！");
    		}
    	}
    	promotion.setLastUpdatedBy(loginUser);
    	promotion.setLastUpdateDate(new Date());
    	imPromotionDAO.update(promotion);
    	return promotion.getOrderTypeCode() + "-" + promotion.getOrderNo() + "存檔成功！";
    }
    
    /**
     * remove delete mark record(item)
     * 
     * @param promotion
     */
    private void removeDeleteMarkLineForItem(ImPromotion promotion){
	       
	List promotionItems = promotion.getImPromotionItems();
	if(promotionItems != null && promotionItems.size() > 0){
	    for(int i = promotionItems.size() - 1; i >= 0; i--){
                ImPromotionItem promotionItem = (ImPromotionItem)promotionItems.get(i);
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(promotionItem.getIsDeleteRecord())){
		    promotionItems.remove(promotionItem);
	        }
	    }
	}
    }
    
    /**
     * remove delete mark record(shop)
     * 
     * @param promotion
     */
    private void removeDeleteMarkLineForShop(ImPromotion promotion){
	       
	List promotionShops = promotion.getImPromotionShops();
	if(promotionShops != null && promotionShops.size() > 0){
	    for(int i = promotionShops.size() - 1; i >= 0; i--){
                ImPromotionShop promotionShop = (ImPromotionShop)promotionShops.get(i);
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(promotionShop.getIsDeleteRecord())){
		    promotionShops.remove(promotionShop);
	        }
	    }
	}
    }
    
    /**
     * 取單號後更新促銷單主檔
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updatePromotionWithActualOrderNO(Map parameterMap) throws FormException, Exception {
        
        HashMap resultMap = new HashMap();
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
    	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    Long headId = getPromotionHeadId(formLinkBean); 
    	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    	    //取得欲更新的bean
    	    ImPromotion promotionPO = getActualPromotion(headId);
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, promotionPO);
	    imPromotionItemService.refreshFieldData(promotionPO);
    	    String resultMsg = modifyImPromotion(promotionPO, employeeCode);
    	    resultMap.put("entityBean", promotionPO);
            resultMap.put("resultMsg", resultMsg);
    	
    	    return resultMap;
    	    //=============================================              
        } catch (FormException fe) {
	    log.error("促銷單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("促銷單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("促銷單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 更新促銷單主檔及明細檔
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updateAJAXPromotion(Map parameterMap) throws FormException, Exception {

    	HashMap resultMap = new HashMap();
    	try{
    		Object formBindBean = parameterMap.get("vatBeanFormBind");
//    		Object formLinkBean = parameterMap.get("vatBeanFormLink");
    		Object otherBean = parameterMap.get("vatBeanOther");
    		Long headId = getPromotionHeadId(formBindBean);      	
    		String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
    		String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
    		String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");      	

    		//取得欲更新的bean
    		ImPromotion promotionPO = getActualPromotion(headId);
    		if(OrderStatus.SAVE.equals(formStatus)){
    			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, promotionPO);       	    
    		}else if((OrderStatus.SAVE.equals(beforeChangeStatus) || OrderStatus.REJECT.equals(beforeChangeStatus)) 
    				&& OrderStatus.SIGNING.equals(formStatus)){
    			//========================檢核資料======================================   	    
    			String identification = MessageStatus.getIdentification(promotionPO.getBrandCode(), 
    					promotionPO.getOrderTypeCode(), promotionPO.getOrderNo());
    			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, promotionPO);
//  			imPromotionItemService.refreshFieldData(promotionPO);
    			promotionPO.setLastUpdatedBy(employeeCode);
    			siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
    			this.checkPromotionData(promotionPO, PROGRAM_ID, identification);  //暫時註解Steve
    			//============remove delete mark record=============
    			removeDeleteMarkLineForItem(promotionPO);
    			removeDeleteMarkLineForShop(promotionPO);
    		}//==================2014/2/26==================簽核中也檢核變價日期
    		else if(OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus)){
    			String identification = MessageStatus.getIdentification(promotionPO.getBrandCode(), 
    					promotionPO.getOrderTypeCode(), promotionPO.getOrderNo());
    			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, promotionPO);
    			promotionPO.setLastUpdatedBy(employeeCode);
    			siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
//    			imPromotionItemService.checkPromotionData(promotionPO, PROGRAM_ID, identification);
    		}
    		promotionPO.setStatus(formStatus);        	
    		String resultMsg = modifyImPromotion(promotionPO, employeeCode);
    		resultMap.put("entityBean", promotionPO);
    		resultMap.put("resultMsg", resultMsg);

    		return resultMap;            
    	} catch (FormException fe) {
    		log.error("促銷單存檔失敗，原因：" + fe.toString());
    		throw new FormException(fe.getMessage());
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		log.error("促銷單存檔時發生錯誤，原因：" + ex.toString());
    		throw new Exception("促銷單存檔時發生錯誤，原因：" + ex.getMessage());
    	}
    }
    
    public static Object[] startProcess(ImPromotion form) throws ProcessFailedException{       

    	try{           
    		String packageId = "Im_Promotion";         
    		String processId = "full";           
    		String version = "20190524";
    		String sourceReferenceType = "ImPromotion (T2)";

    		HashMap context = new HashMap();
    		context.put("brandCode", form.getBrandCode());
    		context.put("formId", form.getHeadId());
    		context.put("orderType", form.getOrderTypeCode());
    		context.put("orderNo", form.getOrderNo());
    		return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
    	}catch (Exception ex){
    		log.error("促銷流程啟動失敗，原因：" + ex.toString());
    		throw new ProcessFailedException("促銷流程啟動失敗！");
    	}       
    }

    
    public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws ProcessFailedException{
	   
	   try{           
	       HashMap context = new HashMap();
	       context.put("approveResult", approveResult);
	       
	       return ProcessHandling.completeAssignment(assignmentId, context);
	   }catch (Exception ex){
	       log.error("完成促銷工作任務失敗，原因：" + ex.toString());
	       throw new ProcessFailedException("完成促銷工作任務失敗！");
	   }
    }
    
    /**查詢採購助理、採購人員、採購主管
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getPurchaseEmployeeForAJAX(Properties httpRequest) throws Exception{
	log.info("getPurchaseEmployeeForAJAX");
	
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try{
	    String itemCategory = httpRequest.getProperty("itemCategory");	
	    List<BuEmployeeWithAddressView> purchaseAssist = null;
	    List<BuEmployeeWithAddressView> purchaseMember = null;
	    List<BuEmployeeWithAddressView> purchaseMaster = null;
	    
	    HashMap mapResult = getPurchaseEmployee(itemCategory);
	    purchaseAssist = (List<BuEmployeeWithAddressView>)mapResult.get("purchaseAssist");
	    purchaseMember = (List<BuEmployeeWithAddressView>)mapResult.get("purchaseMember");
	    purchaseMaster = (List<BuEmployeeWithAddressView>)mapResult.get("purchaseMaster");	    
	    
	    purchaseAssist = AjaxUtils.produceSelectorData(purchaseAssist, "employeeCode", "chineseName", true, true);
	    purchaseMember = AjaxUtils.produceSelectorData(purchaseMember, "employeeCode", "chineseName", true, true);
	    purchaseMaster = AjaxUtils.produceSelectorData(purchaseMaster, "employeeCode", "chineseName", true, true);
	    properties.setProperty("purchaseAssist", AjaxUtils.parseSelectorData(purchaseAssist));
	    properties.setProperty("purchaseMember", AjaxUtils.parseSelectorData(purchaseMember));
	    properties.setProperty("purchaseMaster", AjaxUtils.parseSelectorData(purchaseMaster));
	  
	    result.add(properties);
	    return result;
	}catch (Exception ex) {
	    log.error("查詢採購人員資料時發生錯誤，原因：" + ex.getMessage());
	    throw new Exception("查詢採購人員資料時發生錯誤失敗！");
	}
    }
    
    private HashMap getPurchaseEmployee(String itemCategory){
	
	List<BuEmployeeWithAddressView> purchaseAssist = null;
	List<BuEmployeeWithAddressView> purchaseMember = null;
	List<BuEmployeeWithAddressView> purchaseMaster = null;
	HashMap mapParameter = new HashMap();
	if(StringUtils.hasText(itemCategory)){	   
	    mapParameter.put("itemCategory", itemCategory);
	    mapParameter.put("employeeDepartment", "506");
	    //採購助理
	    mapParameter.put("employeePosition", "A");	    
	    purchaseAssist = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege(mapParameter);	    	
	    //採購人員
	    mapParameter.put("employeePosition", "P");
	    purchaseMember = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege(mapParameter);	    
	    //採購主管
	    mapParameter.put("employeePosition", "M");    
	    purchaseMaster = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege(mapParameter);	    
	}
	
	if(purchaseAssist == null || purchaseAssist.size() == 0){
	    purchaseAssist = new ArrayList(0);
	}
	if(purchaseMember == null || purchaseMember.size() == 0){
	    purchaseMember = new ArrayList(0);
	}
	if(purchaseMaster == null || purchaseMaster.size() == 0){
	    purchaseMaster = new ArrayList(0);
	}
	mapParameter.put("purchaseAssist", purchaseAssist);
	mapParameter.put("purchaseMember", purchaseMember);
	mapParameter.put("purchaseMaster", purchaseMaster);	    
        
	return mapParameter;
    }
    
    /**
     * 是否商品項目低於折扣限制
     * @param imPromotion  
     * @param percentage  70% 使用70  50%使用50傳入
     * @return 低於傳入之percentage，則回傳true 
     */
    public boolean isOverLimitation(ImPromotion imPromotion, Double percentage ) {
        List<ImPromotionItem> objItems = imPromotion.getImPromotionItems();
	boolean result = false;
	for (int i = 0; i < objItems.size(); i++) {
	    ImPromotionItem item = (ImPromotionItem)objItems.get(i);
	    if(percentage >= item.getTotalDiscountPercentage()){
	        result = true;
	    }
	}
	return result;
    }
    
    /**
     * 更新促銷單主檔的Status 和促銷活動代碼
     * 
     * @param headId
     * @param status
     * @throws Exception
     */
    public void updatePromotionStatus(Long headId, String status) throws Exception {
    
        try {
            ImPromotion promotion = findById(headId);
	    if(promotion != null){
	        promotion.setStatus(status);
		imPromotionDAO.update(promotion);
	    }else{
	        throw new NoSuchDataException("促銷單主檔查無主鍵(" + headId + ")的資料！");
	    }
        } catch (Exception ex) {
	    log.error("更新促銷單狀態時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新促銷單狀態時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 執行促銷活動查詢初始化
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception{
        
        HashMap resultMap = new HashMap();
        try{
            Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    Map multiList = new HashMap(0);
	    List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(loginBrandCode, "PM");
	    List<ImItemCategory> allItemCategory = imItemCategoryService.findByCategoryType(loginBrandCode, "ITEM_CATEGORY");
	    multiList.put("allOrderTypes",   AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
	    multiList.put("allItemCategory", AjaxUtils.produceSelectorData( allItemCategory, "categoryCode", "categoryName", true, true));
	    resultMap.put("multiList",multiList);
		
	    return resultMap;       	
        }catch (Exception ex) {
	    log.error("促銷活動查詢初始化失敗，原因：" + ex.toString());
	    throw new Exception("促銷活動查詢初始化失敗，原因：" + ex.toString());
	}           
    }
    
    /**
     * 顯示促銷活動查詢頁面的line
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{
    	log.info("getAJAXSearchPageData");
    	try{
    		List<Properties> result = new ArrayList();
    		List<Properties> gridDatas = new ArrayList();
    		int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
    		int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小  	  
    		//======================帶入查詢的值=========================
    		String brandCode = httpRequest.getProperty("brandCode");
    		String orderTypeCode = httpRequest.getProperty("orderTypeCode");
    		String inCharge = httpRequest.getProperty("inCharge");
    		String status = httpRequest.getProperty("status");
    		String startOrderNo = httpRequest.getProperty("startOrderNo");
    		String endOrderNo = httpRequest.getProperty("endOrderNo");	   
    		String startDate = httpRequest.getProperty("startDate");
    		String endDate = httpRequest.getProperty("endDate");

    		Date actualStartDate = null;
    		Date actualEndDate = null;
    		if(StringUtils.hasText(startDate)){
    			actualStartDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, startDate);
    		}
    		if(StringUtils.hasText(endDate)){
    			actualEndDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, endDate);
    		}
    		//=======================執行查詢==============================   	   
    		HashMap findObjs = new HashMap();
    		findObjs.put("brandCode", brandCode);
    		findObjs.put("orderTypeCode", orderTypeCode);
    		findObjs.put("inCharge", inCharge);
    		findObjs.put("status", status);
    		findObjs.put("startOrderNo", startOrderNo);
    		findObjs.put("endOrderNo", endOrderNo);
    		findObjs.put("startDate", actualStartDate);
    		findObjs.put("endDate", actualEndDate);

    		HashMap promotionMap = imPromotionDAO.findPageLine(findObjs, iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
    		List<ImPromotion> promotions = (List<ImPromotion>)promotionMap.get(BaseDAO.TABLE_LIST);
    		for(ImPromotion promotion : promotions){
    			BuEmployeeWithAddressView buEmployeeWithAddressView = buEmployeeWithAddressViewDAO.findById(promotion.getInCharge());
    			if(buEmployeeWithAddressView != null){
    				promotion.setInChargeName(buEmployeeWithAddressView.getChineseName());
    			}else{
    				promotion.setInChargeName(MessageStatus.DATA_NOT_FOUND);
    			}

    			promotion.setStatusName(OrderStatus.getChineseWord(promotion.getStatus()));
    		}
    		System.out.println("ResultSize=["+ promotions.size() + "]");
    		if (promotions != null && promotions.size() > 0) {
    			Long firstIndex =Long.valueOf(iSPage * iPSize) + 1;    // 取得第一筆的INDEX 
    			promotionMap = imPromotionDAO.findPageLine(findObjs, -1, iPSize, BaseDAO.QUERY_RECORD_COUNT);
    			Long maxIndex = (Long)promotionMap.get(BaseDAO.TABLE_RECORD_COUNT);	// 取得最後一筆 INDEX
//  			refreshSearchResultBeans(promotions, findObjs);
    			result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, promotions, gridDatas, firstIndex, maxIndex));
    		} else {
    			result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, gridDatas));
    		}

    		return result;
    	}catch(Exception ex){
    		log.error("載入頁面顯示的促銷活動查詢發生錯誤，原因：" + ex.toString());
    		throw new Exception("載入頁面顯示的促銷活動查詢失敗！");
    	}	
    }
    
    
    
    /**
     * 查詢明細額外撈取欄位
     * @param searchResult
     * @param findObjs
     */
    private void refreshSearchResultBeans(List searchResult,HashMap findObjs){
	
	if(searchResult != null && searchResult.size() > 0){
	    for(int i = 0; i < searchResult.size(); i++){
		ImPromotion promotion = (ImPromotion)searchResult.get(i);
		if(StringUtils.hasText(promotion.getInCharge())){		    
		    BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findById(promotion.getInCharge());
		    if(employeeWithAddressView != null){
			promotion.setInChargeName(employeeWithAddressView.getChineseName());
		    }else{
			promotion.setInChargeName(promotion.getInCharge());
		    }
		}
		if(StringUtils.hasText(promotion.getStatus())){
		    promotion.setStatusName(OrderStatus.getChineseWord(promotion.getStatus()));
		}
		
		// 抓商品明細其他欄位
		String itemCode = (String)findObjs.get("itemCode");
		if(StringUtils.hasText(itemCode)){
		    ImPromotionItem imPromotionItem = (ImPromotionItem)imPromotionDAO.findFirstByProperty("ImPromotionItem", "and imPromotion.headId = ? and itemCode = ? ", new Object[]{promotion.getHeadId(), itemCode});
		    if(null != imPromotionItem){
			promotion.setTotalDiscountAmount(imPromotionItem.getTotalDiscountAmount());
			promotion.setTotalDiscountPercentage(imPromotionItem.getTotalDiscountPercentage());
			promotion.setOriginalPrice(imPromotionItem.getOriginalPrice());
		    }
		}
	    }
	}
    }
    
    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
    public Map getSearchSelection(Map parameterMap) throws Exception{
	
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    if(result.size() > 0 )
	        pickerResult.put("result", result);
	    
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel",  new String[]{"vatBeanPicker"});
	    
	    return resultMap;
	}catch(Exception ex){
	    log.error("執行促銷活動檢視失敗，原因：" + ex.toString());
	    throw new Exception("執行促銷活動檢視失敗，原因：" + ex.getMessage());		
	}
    }
    
    public Long executePosPromotion(HashMap parameterMap) throws Exception{
 	log.info("exectuePosPromotionExport");
 	Long responseId = -1L;

 	PosExportDAO posExportDAO  = (PosExportDAO) SpringUtils.getApplicationContext().getBean("posExportDAO");
 	try{
 	    //一、解析程式需要排程下傳或是即時下傳 
 	    Long batchId = (Long)parameterMap.get("BATCH_ID");
 	    String uuId = posExportDAO.getDataId(); // 產生dataId
 	    parameterMap.put("UUID", uuId); 
 	    parameterMap.put("DATA_ID", uuId); 
 	    
 	    //二、下傳程式至POS_PMOS (產生DataId , ResponseId)  排程下傳
 	    if(null == batchId || batchId <= 0){
		parameterMap.put("brandCode", parameterMap.get("BRAND_CODE"));
		parameterMap.put("dataDate", DateUtils.format( (Date)parameterMap.get("DATA_DATE_STRAT"), DateUtils.C_DATA_PATTON_YYYYMMDD));
		parameterMap.put("dataDateEnd", DateUtils.format( (Date)parameterMap.get("DATA_DATE_END"), DateUtils.C_DATA_PATTON_YYYYMMDD));
 		// 1.複製ERP_PMOS促銷至POS__PMOS 
 		getPromotion(parameterMap);
 	    }
// 	    else{
// 		//非排程則是把DataId找出，再去POS_PROMOTION依據Data_Id把資訊傳進去	即時下傳
// 		updateRequestExcessivePMO(parameterMap);
// 	    }
 	    
 	    //三、依照batchID來判斷是寫入POS_COMMAND 或是 ERP_COMMAND
 	    responseId = posExportDAO.executeCommand(parameterMap);
 	    
 	    return responseId;
 	    
         }catch (Exception ex) {
            ex.printStackTrace();
 	    log.error("過度促銷活動執行轉移失敗，原因：" + ex.toString());
 	    throw new Exception("過度促銷活動執行轉移失敗，原因：" + ex.toString());
 	}           
     }
    
    private void getPromotion(HashMap parameterMap) throws Exception{
	TreeMap actualPromotionItemMap = new TreeMap();
	ImPromotionViewDAO imPromotionViewDAO = (ImPromotionViewDAO) SpringUtils.getApplicationContext().getBean("imPromotionViewDAO");
	String brandCode = (String)parameterMap.get("brandCode");
	String endDate = (String)parameterMap.get("dataDateEnd");
//	String uuId = String.valueOf(parameterMap.get("DATA_ID"));
	//==============================取得isAllShop不為Y的promotion======================
	TreeMap shopPromotionItemMap = new TreeMap();
	List shopPromotionItems = imPromotionViewDAO.getPromotionItem(brandCode, "1", endDate, "N");
	log.info("shopPromotionItems.size() = " + shopPromotionItems.size());
	if(shopPromotionItems != null){
	    for(int i = 0; i < shopPromotionItems.size(); i++){
	        Object[] promotionInfo = (Object[])shopPromotionItems.get(i);
	        filterPromItem(parameterMap,shopPromotionItemMap, promotionInfo);
            }
	}
	//==============================取得isAllShop為Y的promotion========================
	TreeMap promotionItemMap = new TreeMap();
	List promotionItems = imPromotionViewDAO.getPromotionItemInfo(brandCode, "1", endDate, "Y");
	log.info("promotionItems.size() = " + promotionItems.size());
	if(promotionItems != null){
	    for(int i = 0; i < promotionItems.size(); i++){
	        Object[] promotionInfo = (Object[])promotionItems.get(i);
	        filterPromItem(parameterMap,promotionItemMap, promotionInfo);
            }
	}
	//==============================取得比對後的promotion item========================ex
	getActualExportPromItem(brandCode, actualPromotionItemMap, shopPromotionItemMap, promotionItemMap);

	Set set = actualPromotionItemMap.entrySet();
	Map.Entry[] promotionItemEntry = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);

        for (Map.Entry entry : promotionItemEntry) {
            
            ImExcessivePromotionView promotionView = (ImExcessivePromotionView)entry.getValue();
    	
            String tempShopCode = promotionView.getShopCode();
            imPromotionViewDAO.updatePosExPromotion(parameterMap,promotionView);
            if(StringUtils.hasText(tempShopCode)){
        	tempShopCode = OldSysMapNewSys.getOldShopCode(tempShopCode);
            }
            
        }
        //查無資料
        if(promotionItems == null || promotionItems.size() <= 0){
            parameterMap.put("searchResult","noData");
        }
      }

    /**
     * 過濾出要的促銷
     * @param parameterMap
     * @param map
     * @param promotionItemInfo
     */
    private void filterPromItem(Map parameterMap, TreeMap map, Object[] promotionItemInfo){
	
	Date now = (Date)parameterMap.get("DATA_DATE_END");
	
	String brandCode = (String)promotionItemInfo[0];
	String promotionCode = (String)promotionItemInfo[1];
	String shopCode = (String)promotionItemInfo[2];
	if(shopCode == null){
	    shopCode = "";
	}
	Date beginDate = (Date)promotionItemInfo[3];
	Date endDate = (Date)promotionItemInfo[4];
	String itemCode = (String)promotionItemInfo[5];
	String discountType = (String)promotionItemInfo[6];
	Double discount = ((BigDecimal)promotionItemInfo[7]).doubleValue();
	Double unitPrice = ((BigDecimal)promotionItemInfo[8]).doubleValue();
//	Double promotionPrice = ((BigDecimal)promotionItemInfo[9]).doubleValue();
	Double promotionPrice =0D;
	
	if("1".equals(discountType)){
	    promotionPrice = promotionPrice - discount;
	}else if("2".equals(discountType)){
	    promotionPrice = promotionPrice * (100 - discount) / 100;
	}
	if(promotionPrice < 0D){
	    promotionPrice = 0D;
	}
	//updated by anber 2010.12.29 change lastUpdateDate type to String
	//Date lastUpdateDate = (Date)promotionItemInfo[9];
//	java.sql.Date lastUpdateDate = (java.sql.Date)promotionItemInfo[9];
	String updateDateString = (String)promotionItemInfo[9];
	log.info("ORIG promotionItemInfo[10] = " + promotionItemInfo[9]);
	Date lastUpdateDate = DateUtils.parseDate(DateUtils.C_TIME_PATTON_DEFAULT, updateDateString);

	//===============set promotionView bean================
	ImExcessivePromotionView imExcessivePromotionView = new ImExcessivePromotionView();
	imExcessivePromotionView.setBrandCode(brandCode);
	imExcessivePromotionView.setPromotionCode(promotionCode);
	imExcessivePromotionView.setShopCode(shopCode);
	imExcessivePromotionView.setBeginDate(beginDate);
	imExcessivePromotionView.setEndDate(endDate);
	imExcessivePromotionView.setItemCode(itemCode);
	imExcessivePromotionView.setDiscountType(discountType);
	imExcessivePromotionView.setDiscount(discount);
	imExcessivePromotionView.setOriginalPrice(unitPrice);
	
	if(now.compareTo(endDate) >= 0){
	    imExcessivePromotionView.setPromotionUnitPrice(unitPrice); // 當前日期等於或大於結束日期則下原價
	}else{
	    imExcessivePromotionView.setPromotionUnitPrice(promotionPrice); // 反之促銷
	}
	
	imExcessivePromotionView.setLastUpdateDate(lastUpdateDate);
//	log.info("discountType = " + discountType);
//	log.info("discount = " + discount);
//	log.info("unitPrice = " + unitPrice);
	//======================================================
	String key = itemCode + "#" + shopCode;
//	log.info("itemCode # shopCode = " + itemCode + "#" + shopCode);
	ImExcessivePromotionView tmpImExcessivePromotionView = (ImExcessivePromotionView)map.get(key);
	if(tmpImExcessivePromotionView == null){ // 若不存在則放入實際促銷
	    map.put(key, imExcessivePromotionView);
	}else{
	    Date tempBeginDate = tmpImExcessivePromotionView.getBeginDate();
	    Date tempLastUpdateDate = tmpImExcessivePromotionView.getLastUpdateDate();
	    if(beginDate.after(tempBeginDate)){ // 後蓋舊
		map.put(key, imExcessivePromotionView);
	    }else if(beginDate.getTime() == tempBeginDate.getTime()){
		//活動日期相同時，依據更新日期作比對
		if(lastUpdateDate != null && tempLastUpdateDate != null){
		    if(lastUpdateDate.after(tempLastUpdateDate)){
			map.put(key, imExcessivePromotionView);
		    }
		}
	    }
	}
    }
    
    private void getActualExportPromItem(String brandCode, TreeMap actualPromotionItemMap,
		TreeMap shopPromotionItemMap, TreeMap promotionItemMap) throws Exception{

	    TreeMap tempMap = new TreeMap();
	    //=================取得此brand下啟用中的專櫃=========================
	    BuShopDAO buShopDAO = (BuShopDAO) SpringUtils.getApplicationContext().getBean("buShopDAO");
	    List buShopInfos = buShopDAO.findShopByBrandAndEnable(brandCode, "Y");
	    List<String> brandShops = new ArrayList();
	    if(buShopInfos != null){
		for(int i = 0; i < buShopInfos.size(); i++){
		    BuShop buShop = (BuShop)buShopInfos.get(i);
		    brandShops.add(buShop.getShopCode());
		}
	    }
	    
	    System.out.println("==============DDD==="+shopPromotionItemMap.entrySet());
	    Set set = shopPromotionItemMap.entrySet();
	    System.out.println("==============EEE==="+promotionItemMap.entrySet());
	    Map.Entry[] promotionItemEntry = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
        for (Map.Entry entry : promotionItemEntry) {
            String key = (String) entry.getKey();	// 品號#專櫃
            //===================單一專櫃促銷=====================
            ImPromotionView shopPromotionView = (ImPromotionView)entry.getValue();
            Date ShopBeginDate = shopPromotionView.getBeginDate();
		Date ShopLastUpdateDate = shopPromotionView.getLastUpdateDate();
		String[] keyArray = StringTools.StringToken(key, "#");
		String searchKey = keyArray[0] + "#";	// 品號#
		//====================全專櫃促銷======================
		ImPromotionView promotionView = (ImPromotionView)promotionItemMap.get(searchKey);
		//==================================================
		if(promotionView == null){ // 若品號# 沒有在全部專櫃找到則放置 實際促銷map
		    actualPromotionItemMap.put(key, shopPromotionView);
		}else{
		    Date beginDate = promotionView.getBeginDate();
		    Date lastUpdateDate = promotionView.getLastUpdateDate();
		    String isAllShop = "Y";
		    if(ShopBeginDate.after(beginDate)){ // 單一專櫃的促銷起始日大於全部專櫃的起始日 
			actualPromotionItemMap.put(key, shopPromotionView);
			isAllShop = "N";
		    }else if(ShopBeginDate.getTime() == beginDate.getTime()){	// 相等
		        //活動日期相同時，依據更新日期作比對
			if(ShopLastUpdateDate != null && lastUpdateDate != null){
		            if(ShopLastUpdateDate.after(lastUpdateDate)){
			        actualPromotionItemMap.put(key, shopPromotionView);
				isAllShop = "N";
			    }else if(ShopLastUpdateDate.getTime() == lastUpdateDate.getTime()){
				actualPromotionItemMap.put(key, shopPromotionView);
				isAllShop = "N";
			    }
			}
		    }
		    //===============自動新增出其他專櫃促銷資料=======================
		    if("N".equals(isAllShop)){
			tempMap.put(searchKey, searchKey);//此map中的promotionItem不處理
		        for(int i = 0; i < brandShops.size(); i++){
			    String tempShop = brandShops.get(i);
			    if(!keyArray[1].equals(tempShop) ){ // 單一專櫃不等於掃出的專櫃，因為當全部品牌其一專櫃不等於單一專櫃的專櫃時就會覆蓋　如brandShops有T1CO24單一專櫃有T1CO24其他的就會蓋到成全部促銷
				if(!actualPromotionItemMap.containsKey(searchKey + tempShop)){ // 且不存在actualPromotionItemMap的key才不覆蓋, 因為已存在的是單一專櫃用   20110217 david
				    ImPromotionView newPromotionView = new ImPromotionView();
				    BeanUtils.copyProperties(promotionView, newPromotionView);
				    newPromotionView.setShopCode(tempShop);
				    actualPromotionItemMap.put(searchKey + tempShop, newPromotionView);
				}
			    }
			}
		    }
		}
        }
        //=========================處理全專櫃的promotion==========================
        Set allShopSet = promotionItemMap.entrySet();
	    Map.Entry[] allShopPromotionItemEntry = (Map.Entry[]) allShopSet.toArray(new Map.Entry[allShopSet.size()]);
        for (Map.Entry entry : allShopPromotionItemEntry) {
    	String key = (String) entry.getKey();
    	ImExcessivePromotionView promotionView = (ImExcessivePromotionView)entry.getValue();
            if(tempMap.get(key) == null){ // 若單一專櫃查不到則將全部專櫃放置實際促銷MAP
                actualPromotionItemMap.put(key, promotionView);
            }
        }
	}
    
    
    
    /**
	 * 執行反確認單據反轉
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
    public Object executeReverter(Long headId, String employeeCode) throws Exception {
		log.info("executeReverter promotion headId = " + headId);

		ImPromotion head = null;
		head = findById(headId);
		BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO
				.findbyBrandCodeAndEmployeeCode(head.getBrandCode(),
						employeeCode);
		if (employeeWithAddressView == null) {
			throw new Exception("查無員工代號: " + employeeCode + " 對應之員工");
		}
		// 取出促銷單
		//status = finish
		//判斷促銷是否為所有專櫃
		//enableDate < 今日
		//才可以做反轉
		//status 改為 save
		if (head == null) {
			throw new Exception("促銷單查無此單號: " + headId);
		}

		// status必為finish
		if (!(OrderStatus.FINISH.equals(head.getStatus()))) {
			throw new Exception("促銷單號: " + headId + "  ,未簽核完成, 不可反確.");
		}
		// 檢核啟用日期日期大於等於當天,狀態為簽核完成才能反轉
		if (!(OrderStatus.FINISH.equalsIgnoreCase(head.getStatus()))) {
			throw new Exception("品牌: " + head.getBrandCode() + 
					" 單別: " + head.getOrderTypeCode() + 
					" 單號: " + head.getOrderNo()+ " 狀態不為FINISH，無法反轉");
		}

		Date shopMinBeginDate = getShopMinBeginDate(head);
		Date shopMinEndDate = getShopMinEndDate(head);
		
	    int dateBetween = 0;
	    
	    dateBetween = 
	    	DateUtils.daysBetweenWithoutTime(shopMinBeginDate, DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, DateUtils.format(DateUtils.getCurrentDate(),DateUtils.C_TIME_PATTON_DEFAULT )));
		// 促銷條件****生效日*****,未下到POS機前,beginDate < today
	    /*啟用日小於或等於現在日期: 提前於執行反轉的當天結束*/
	    if(dateBetween >= 0){
	    	Date now = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, DateUtils.format(new Date(), DateUtils.C_DATA_PATTON_YYYYMMDD));
	    	int dateBetween1 = 
		    	DateUtils.daysBetweenWithoutTime(now, DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, DateUtils.format(shopMinEndDate, DateUtils.C_TIME_PATTON_DEFAULT )));
	    	
	    	if(dateBetween1 > 0){
	    		
	    		List<ImPromotionShop> imPromotionShops = head.getImPromotionShops();
				
				if(imPromotionShops != null){
					for(ImPromotionShop imPromotionShop : imPromotionShops){
						imPromotionShop.setEndDate(shopMinEndDate);
					}
				}
				
	    		head.setEndDate(now);
		    	head.setLastUpdateDate(new Date());
		    	imPromotionDAO.update(head);
		    	return head;
		    	/*
		    	throw new Exception("品牌: "	+ head.getBrandCode()
						+ " 單別: " + head.getOrderTypeCode()
						+ " 單號: " + head.getOrderNo()
						+ " 促銷活動(" + head.getPromotionName() + ")結束日已提前至(" + DateUtils.format(head.getEndDate(), DateUtils.C_DATA_PATTON_YYYYMMDD) 
						+ ")!");
		    	*/
	    	}else{
				throw new Exception("品牌: "	+ head.getBrandCode()
						+ " 單別: " + head.getOrderTypeCode()
						+ " 單號: " + head.getOrderNo()
						+ " 結束日期-(" + DateUtils.format(head.getEndDate(), DateUtils.C_DATA_PATTON_YYYYMMDD) 
						+ ")， 促銷已經結束無法反轉!");	   
	    	}
	    }else{/*啟用日大於現在日期: 執行反轉*/
	    	//反轉單據狀態
			head.setStatus(OrderStatus.SAVE);
			head.setCreatedBy(employeeCode);
			head.setLastUpdateDate(new Date());
			imPromotionDAO.update(head);
			return head;
	    }
	}
    
    /**
	 * 執行反確認流程起始
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public void executeReverterProcess(Object bean) throws Exception {
		String status = org.apache.commons.beanutils.BeanUtils.getNestedProperty(bean, "status");
		//起流程
		if(OrderStatus.SAVE.equals(status))
			ImPromotionMainService.startProcess((ImPromotion)bean);
	}
    
    /**
	 * 取特定促銷專櫃中, 最早開始促銷的日期
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public Date getShopMinBeginDate(ImPromotion head){
		Date minBeginDate = null;
		if(head.getIsAllShop().equals("Y")){
			minBeginDate = head.getBeginDate();
		}else{
			List<ImPromotionShop> imPromotionShops = null;
			imPromotionShops = head.getImPromotionShops();
			
			if(imPromotionShops != null)
			for(ImPromotionShop checkImPromotionShop : imPromotionShops){
				log.info("imPromotionShops.getBeginDate() = " + checkImPromotionShop.getBeginDate() + " : " + minBeginDate);
				
				if(null == minBeginDate){
					minBeginDate = checkImPromotionShop.getBeginDate();
				}
				
				if(minBeginDate.after(checkImPromotionShop.getBeginDate())){
				    minBeginDate = checkImPromotionShop.getBeginDate();
				}
			}
		}
		return minBeginDate;
	}
	
    /**
	 * 取特定促銷專櫃中, 最早結束促銷的日期
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public Date getShopMinEndDate(ImPromotion head){
		Date minEndDate = null;
		if(head.getIsAllShop().equals("Y")){
			minEndDate = head.getEndDate();
		}else{
			List<ImPromotionShop> imPromotionShops = null;
			imPromotionShops = head.getImPromotionShops();
			
			if(imPromotionShops != null)
			for(ImPromotionShop checkImPromotionShop : imPromotionShops){
				log.info("imPromotionShops.getEndDate() = " + checkImPromotionShop.getEndDate() + " : " + minEndDate);
				
				if(null == minEndDate){
					minEndDate = checkImPromotionShop.getEndDate();
				}
				
				if(minEndDate.after(checkImPromotionShop.getEndDate())){
					minEndDate = checkImPromotionShop.getEndDate();
				}
			}
		}
		return minEndDate;
	}
	
	/**
     * 促銷活動簽核完成，僅發信通知商控
     *
     * @param subject
     * @param templateFileName
     * @param display
     * @param mailAddress
     * @param attachFiles
     * @param cidMap
     * @author wade
     */
     
	public void sendMail(ImPromotion head, String subject, String templateFileName, Map display,
			List mailAddress, List attachFiles, Map cidMap) throws Exception {

		try{
			System.out.println("SendMail start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
			//String reportFileName = "SO0752.rpt";
			//String brandCode = head.getBrandCode();
			String orderTypeCode = head.getOrderTypeCode();
			//String itemCategory = AjaxUtils.getPropertiesValue(head.getItemCategory(), "");
			//String supplierCode = AjaxUtils.getPropertiesValue(head.getSupplierCode(), "");
			String orderNo = head.getOrderNo();
			String employeeName = buEmployeeWithAddressViewService.findById(head.getLastUpdatedBy()).getChineseName();
			
			// 寄給商控and資訊部
			mailAddress.add("wade_chung@tasameng.com.tw"); // 資訊部
			//mailAddress.add("Developer@tasameng.com.tw"); // 資訊部
			mailAddress.add("cbr_0922@tasameng.com.tw"); // 資訊部
			mailAddress.add("MM-DG@tasameng.com.tw"); // 商控
			
			//String fileName = itemCategory.concat("-").concat(supplierCode).concat(orderNo);
			//String fileName = itemCategory.concat("-").concat(orderNo);
			// 設定範本
			templateFileName = "CommonTemplate.ftl";
			
			// 設定主旨
			subject = "促銷單:" + orderTypeCode + "-" + orderNo + ",已簽核完成";
/*			
			Map parameters = new HashMap(0);
			parameters.put("prompt0", brandCode);
			parameters.put("prompt1", orderTypeCode);
			parameters.put("prompt2", "");
			parameters.put("prompt3", "");
			parameters.put("prompt4", orderNo);
			parameters.put("prompt5", orderNo);
			
			String reportUrl = SystemConfig.getPureReportURL(brandCode, orderTypeCode, head.getLastUpdatedBy(),
					reportFileName, parameters);
			// "http://10.1.99.161:8080/crystal/t2/SO0752.rpt?crypto=56557594c4cafe66e6f67ad5cebf7c2445a1727c95855b5469e88aea836ed3d62a509c34795a3452&prompt2=&prompt5=201104270004&prompt0=T2&prompt4=201104270004&prompt1=PAJ&prompt3=";
			//  http://10.1.99.161:8080/crystal/t2/null?      crypto=0d4a33df7ffd13aaf0145db07fc69f225c5a22aeaf2a87d10820b15f8225c64d&prompt2=&prompt5=201306240001&prompt0=T2&prompt4=201306240001&prompt1=PMO&prompt3=
			List<File> png_files = new ArrayList<File>();
			PDDocument doc = null;
			
			// #3 取得pdf
			URL pdfUrl = new URL(reportUrl + "&init=pdf");
			URLConnection pdfConnection = pdfUrl.openConnection();
			
System.out.println("SendMail start 111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111 ");
			
			// - 模擬client端的瀏覽器 避免被當作網頁機器人而被阻擋
			pdfConnection.setRequestProperty("User-Agent", "");
			pdfConnection.setRequestProperty("accept-language", "");
			pdfConnection.setRequestProperty("cookie", "");
			pdfConnection.setDoOutput(true);

			BufferedInputStream pdfBis = new BufferedInputStream(pdfConnection.getInputStream());
			System.out.println("pdfBis = " + pdfBis);
			File pdfFile = File.createTempFile(fileName, ".pdf");
			FileOutputStream pdfFos = new FileOutputStream(pdfFile);
			byte[] data = new byte[1];
			while (pdfBis.read(data) != -1) {
				pdfFos.write(data);
			}
			pdfFos.flush();
			pdfFos.close();

System.out.println("SendMail start 222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222 ");
			
			// #4 取得excel
			URL xlsUrl = new URL(reportUrl + "&init=xls");
			URLConnection xlsConnection = xlsUrl.openConnection();

			// - 模擬client端的瀏覽器 避免被當作網頁機器人而被阻擋
			xlsConnection.setRequestProperty("User-Agent", "");
			xlsConnection.setRequestProperty("accept-language", "");
			xlsConnection.setRequestProperty("cookie", "");
			xlsConnection.setDoOutput(true);

			BufferedInputStream xlsBis = new BufferedInputStream(xlsConnection.getInputStream());
			System.out.println("xlsBis = " + xlsBis);
			File xlsFile = File.createTempFile(fileName + "-" + DateUtils.getCurrentDateStr("yyyyMMddHHmmss") + "_",
					".xls");
			FileOutputStream xlsFos = new FileOutputStream(xlsFile);
			byte[] xlsData = new byte[1];
			while (xlsBis.read(xlsData) != -1) {
				xlsFos.write(xlsData);
			}
			xlsFos.flush();
			xlsFos.close();
			
//System.out.println("SendMail start 3333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333 ");
						

			attachFiles.add(xlsFile.getAbsolutePath());

			// pdf轉圖檔
			doc = PDDocument.load(pdfFile);
			List pages = doc.getDocumentCatalog().getAllPages();
			for (int j = 0; j < pages.size(); j++) {
				PDPage page = (PDPage) pages.get(j);
				BufferedImage bufferedImage = page.convertToImage();
				File imageFile = File.createTempFile(fileName + j, ".jpg"); // "new File("PA"+j+".png") File.createTempFile("PA"+j,".gif")
				ImageIO.write(bufferedImage, "jpg", imageFile);
				png_files.add(imageFile);
				attachFiles.add(imageFile.getAbsolutePath());

				cidMap.put(imageFile.getAbsolutePath(), png_files.get(j).getName());

			}

//System.out.println("SendMail start 444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444 ");
*/
			// 設定內容
			StringBuffer html = new StringBuffer();
			//System.out.println("emailError.toString() = " + emailError.toString());
			//if (StringUtils.hasText(emailError.toString())) {
				//html.append(emailError.toString());
			//}
			/*
			for(int i=0; i<png_files.size(); i++){
				html.append("<img src='cid:" + png_files.get(i).getName() + "'/><br>");
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>html = " + html.toString());
			// 設定樣板的內容值
			*/
			
			html.append("促銷單：").append(orderTypeCode).append(orderNo);
			if(StringUtils.hasText(head.getPromotionName())) 
				html.append("-").append(head.getPromotionName());
			html.append("<br/>").append("已於").append(DateUtils.formatTime(head.getLastUpdateDate()))
			    .append(",由").append(employeeName).append("簽核完成");
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>html = " + html.toString());
			// 設定樣板的內容值
			display.put("display", html.toString());
			// 發信通知
			MailUtils.sendMail(subject, templateFileName, display, mailAddress, attachFiles, cidMap);
			//MailUtils.sendMail(head, templateFileName, "ApplicationServerPath", "/erp/Im_Promotion:create:20091231.page?formId=" + head.getHeadId(), true);
			System.out.println("SendMail end>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		} 
		catch(Exception e){
			e.printStackTrace();
			throw new Exception("取得寄件相關內容發生錯誤");
		}
	}
	
	 /**
     * 取得寄信清單與相關資訊
     * @param subject
     * @param templateFileName
     * @param display
     * @param mailAddress
     * @param attachFiles
     * @param cidMap
     */
	public void getMailList(ImPromotion head, String subject, String templateFileName, Map display, List mailAddress,
			List attachFiles, Map cidMap) throws Exception {
log.info("babugetMailList");
		// #1 取得寄件者與其他相關配置檔
		// #2 取得CC報表的URL
		// #3 用URL轉成PDF再轉jpg
		// #4 用URL轉成xls
		// #5 寄信
		try {
			String reportFileName = null;
			String brandCode = head.getBrandCode();
			String orderTypeCode = head.getOrderTypeCode();
			String itemCategory = AjaxUtils.getPropertiesValue(head.getItemCategory(), "");

			String subjectError = null;
			String description = null;
			StringBuffer emailError = new StringBuffer();

			// 取得業種子類名稱
			String categoryName = null;
			ImItemCategory imItemCategory = imItemCategoryService.findById(brandCode, ImItemCategoryDAO.ITEM_CATEGORY,
					itemCategory);
			if (null == imItemCategory) {
				categoryName = "";
			} else {
				categoryName = imItemCategory.getCategoryName().concat("-");
			}

			// #1  取得配置檔得到 寄信的報表與
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("MailList"
					+ orderTypeCode,itemCategory); // +orderTypeCode itemCategory
			if (null == buCommonPhraseLine) {
				// 寄給故障維謢人員
				mailAddress.add("Developer@tasameng.com.tw");
				reportFileName = "SO0753_1.rpt";
				subjectError = "MailList" + orderTypeCode + "PMO" + "無配置檔異常 ";
				description = "";
			} else {
				reportFileName = buCommonPhraseLine.getAttribute1();
				// 1.該單起單人、採助、採購人、採購主管 從 BU_EMPLOYEE 取得
				// String domain = "@tasameng.com.tw";
				String createdBy = head.getCreatedBy();
				
				//促銷單不需此參數
				
				String purchaseAssist = head.getPurchaseAssist();
				String purchaseMember = head.getPurchaseMember();
				String purchaseMaster = head.getPurchaseMaster();
              
				if (StringUtils.hasText(createdBy)) { // 起單人
					BuEmployee buEmployee = (BuEmployee) buEmployeeDAO.findByPrimaryKey(BuEmployee.class, createdBy);
					if (null != buEmployee) {
						String EMailCompany = buEmployee.getEMailCompany();
						if (null != EMailCompany) {
							if (!mailAddress.contains(EMailCompany)) // 判斷是否有重複的email
								mailAddress.add(EMailCompany);
							    log.info("起單人:"+EMailCompany);
						} else {
							//emailError.append("查起單人工號：").append(createdBy).append(" 無信箱設定，請聯絡資訊部人員與通知此人促銷通知<br>");
						}
					} else {
						//emailError.append("查起單人無工號：").append(createdBy).append(" ，請聯絡資訊部人員與通知此人促銷通知<br>");
					}
				}

				if (StringUtils.hasText(purchaseAssist)) { // 採購助理
					BuEmployee buEmployee = (BuEmployee) buEmployeeDAO.findByPrimaryKey(BuEmployee.class, purchaseAssist);
					if (null != buEmployee) {
						String EMailCompany = buEmployee.getEMailCompany();
						if (null != EMailCompany) {
							if (!mailAddress.contains(EMailCompany)) // 判斷是否有重複的email
								mailAddress.add(EMailCompany);
							  log.info("採購助理:"+EMailCompany);
						} else {
							//emailError.append("查採購助理工號：").append(purchaseAssist).append(" 無信箱設定，請聯絡資訊部人員與通知此人調價通知<br>");
						}
					} else {
						//emailError.append("查採購助理無工號：").append(purchaseAssist).append(" ，請聯絡資訊部人員與通知此人調價通知<br>");
					}
				}
				if (StringUtils.hasText(purchaseMember)) { // 採購
					BuEmployee buEmployee = (BuEmployee) buEmployeeDAO.findByPrimaryKey(BuEmployee.class, purchaseMember);
					if (null != buEmployee) {
						String EMailCompany = buEmployee.getEMailCompany();
						if (null != EMailCompany) {
							if (!mailAddress.contains(EMailCompany)) // 判斷是否有重複的email
								mailAddress.add(EMailCompany);
							  log.info("查採購人:"+EMailCompany);
						} else {
							//emailError.append("查採購人員工號：").append(purchaseMember).append(" 無信箱設定，請聯絡資訊部人員與通知此人調價通知<br>");
						}
					} else {
						//emailError.append("查採購人員無工號：").append(purchaseMember).append(" ，請聯絡資訊部人員與通知此人調價通知<br>");
					}
				}
				if (StringUtils.hasText(purchaseMaster)) { // 採購主管
					BuEmployee buEmployee = (BuEmployee) buEmployeeDAO.findByPrimaryKey(BuEmployee.class, purchaseMaster);
					if (null != buEmployee) {
						String EMailCompany = buEmployee.getEMailCompany();
						if (null != EMailCompany) {
							if (!mailAddress.contains(EMailCompany)) // 判斷是否有重複的email
								mailAddress.add(EMailCompany);
							  log.info("採購主管:"+EMailCompany);
						} else {
							//emailError.append("查採購主管工號：").append(purchaseMaster).append(" 無信箱設定，請聯絡資訊部人員與通知此人調價通知<br>");
						}
					} else {
						//emailError.append("查採購主管無工號：").append(purchaseMaster).append(" ，請聯絡資訊部人員與通知此人調價通知<br>");
					}
				}

				// 2.營業 從 BU_COMMON_PHRASE_LINE 取得
				// 修改為寄給多個單位，以分號分隔email地址 by Weichun 2011.09.15
				String[] attribute2 = buCommonPhraseLine.getAttribute2().split(";");
				for (int i = 0; i < attribute2.length; i++) {
					if (StringUtils.hasText(attribute2[i])) {
						//System.out.println("通知email ==> " + attribute2[i]);
						mailAddress.add(attribute2[i]);
						
					}
					System.out.println("BaubmailAddress:"+mailAddress);
				}
				
				// 3.商品異動_副本 從 BU_COMMON_PHRASE_LINE 取得
				String attribute3 = buCommonPhraseLine.getAttribute3();
				if (StringUtils.hasText(attribute3)) {
					mailAddress.add(attribute3);
					
				}

				// 前半部主旨
				description = buCommonPhraseLine.getDescription();
			}
/*
			String supplierCode = AjaxUtils.getPropertiesValue(head.getSupplierCode(), "");
			String orderNo = head.getOrderNo();

			// 設定主旨
			if (StringUtils.hasText(supplierCode)) {
				supplierCode = supplierCode.concat("-");
			}

			subject = description.concat(categoryName).concat(supplierCode).concat(orderNo);
			if (StringUtils.hasText(subjectError)) {
				subject = subjectError + subject;
			}

			String fileName = itemCategory.concat("-").concat(supplierCode).concat(orderNo);
*/
			description = buCommonPhraseLine.getDescription();
			subject = description.concat(categoryName).concat(head.getOrderTypeCode()).concat(head.getOrderNo());
			if (StringUtils.hasText(subjectError)) {
				subject = subjectError + subject;
			}
			
			String orderNo = head.getOrderNo();
			String fileName = itemCategory.concat("-").concat(orderTypeCode).concat(orderNo);
			// 設定範本
			templateFileName = "CommonTemplate.ftl";
log.info("prompt0"+ brandCode+"   prompt1"+ orderTypeCode+"    prompt2"+  orderNo);
			// #2 取得cc連結
			Map returnMap = new HashMap(0);
			Map parameters = new HashMap(0);
			parameters.put("prompt0", brandCode);
			parameters.put("prompt1", orderTypeCode);
			parameters.put("prompt2",  orderNo);
			//parameters.put("prompt3", "");
			//parameters.put("prompt4", orderNo);
			//parameters.put("prompt5", orderNo);
			String reportUrl = SystemConfig.getPureReportURL(brandCode, orderTypeCode, head.getLastUpdatedBy(),
					reportFileName, parameters);
			log.info("babureportUrl:"+reportUrl);
			// "http://10.1.99.161:8080/crystal/t2/SO0752.rpt?crypto=56557594c4cafe66e6f67ad5cebf7c2445a1727c95855b5469e88aea836ed3d62a509c34795a3452&prompt2=&prompt5=201104270004&prompt0=T2&prompt4=201104270004&prompt1=PAJ&prompt3=";

			List<File> png_files = new ArrayList<File>();

			PDDocument doc = null;
			try {
				// #3 取得pdf
				URL pdfUrl = new URL(reportUrl + "&init=pdf");
				URLConnection pdfConnection = pdfUrl.openConnection();

				// - 模擬client端的瀏覽器 避免被當作網頁機器人而被阻擋
				pdfConnection.setRequestProperty("User-Agent", "");
				pdfConnection.setRequestProperty("accept-language", "");
				pdfConnection.setRequestProperty("cookie", "");
				pdfConnection.setDoOutput(true);

				BufferedInputStream pdfBis = new BufferedInputStream(pdfConnection.getInputStream());
				System.out.println("pdfBis = " + pdfBis);
				File pdfFile = File.createTempFile(fileName, ".pdf");
				FileOutputStream pdfFos = new FileOutputStream(pdfFile);
				byte[] data = new byte[1];
				while (pdfBis.read(data) != -1) {
					pdfFos.write(data);
				}
				pdfFos.flush();
				pdfFos.close();

				// #4 取得excel
				URL xlsUrl = new URL(reportUrl + "&init=xls");
				URLConnection xlsConnection = xlsUrl.openConnection();

				// - 模擬client端的瀏覽器 避免被當作網頁機器人而被阻擋
				xlsConnection.setRequestProperty("User-Agent", "");
				xlsConnection.setRequestProperty("accept-language", "");
				xlsConnection.setRequestProperty("cookie", "");
				xlsConnection.setDoOutput(true);

				BufferedInputStream xlsBis = new BufferedInputStream(xlsConnection.getInputStream());
				System.out.println("xlsBis = " + xlsBis);
				File xlsFile = File.createTempFile(fileName + "-" + DateUtils.getCurrentDateStr("yyyyMMddHHmmss") + "_",
						".xls");
				FileOutputStream xlsFos = new FileOutputStream(xlsFile);
				byte[] xlsData = new byte[1];
				while (xlsBis.read(xlsData) != -1) {
					xlsFos.write(xlsData);
				}
				xlsFos.flush();
				xlsFos.close();

				attachFiles.add(xlsFile.getAbsolutePath());

				// pdf轉圖檔
				doc = PDDocument.load(pdfFile);
				List pages = doc.getDocumentCatalog().getAllPages();
				for (int j = 0; j < pages.size(); j++) {
					PDPage page = (PDPage) pages.get(j);
					BufferedImage bufferedImage = page.convertToImage();
					File imageFile = File.createTempFile(fileName + j, ".jpg"); // "new File("PA"+j+".png") File.createTempFile("PA"+j,".gif")
					ImageIO.write(bufferedImage, "jpg", imageFile);
					png_files.add(imageFile);
					attachFiles.add(imageFile.getAbsolutePath());

					cidMap.put(imageFile.getAbsolutePath(), png_files.get(j).getName());

				}

				StringBuffer html = new StringBuffer();
				System.out.println("emailError.toString() = " + emailError.toString());
				if (StringUtils.hasText(emailError.toString())) {
					html.append(emailError.toString());
				}

				for (int i = 0; i < png_files.size(); i++) {
					html.append("<img src='cid:" + png_files.get(i).getName() + "'/><br>");
				}
				System.out.println("html = " + html.toString());
				// 設定樣板的內容值
				display.put("display", html.toString());

				// #5
                MailUtils.sendMail(subject, templateFileName, display, mailAddress, attachFiles, cidMap);
				//測試
//				List mailAddressList = new ArrayList();
//				mailAddressList.add("weichun_liao@tasameng.com.tw");
//				mailAddressList.add("MM-DG@tasameng.com.tw");
//				MailUtils.sendMail(subject, templateFileName, display, mailAddressList, attachFiles, cidMap);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("取得寄件相關內容發生錯誤");
		}

	}
	
	/**
     * 取得CC開窗URL字串
     *
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public List<Properties> getReportConfig(Map parameterMap) throws Exception  {
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
	    String orderNo = (String)PropertyUtils.getProperty(otherBean, "orderNo");
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

	    Map returnMap = new HashMap(0);
	    //CC後面要代的參數使用parameters傳遞
	    Map parameters = new HashMap(0);
	    if(brandCode.indexOf("T2") > -1){
		parameters.put("prompt0", brandCode);
		parameters.put("prompt1", orderTypeCode);
		parameters.put("prompt2", orderNo);
		parameters.put("prompt3", "");
		parameters.put("prompt4", orderNo);
		parameters.put("prompt5", orderNo);
	    }else{
		parameters.put("prompt0", brandCode);
		parameters.put("prompt1", orderTypeCode);
		parameters.put("prompt2", orderNo);
	    }

	    String reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, parameters);
	    returnMap.put("reportUrl", reportUrl);
	    return AjaxUtils.parseReturnDataToJSON(returnMap);
	}catch(IllegalAccessException iae){
	    System.out.println(iae.getMessage());
	    throw new IllegalAccessException(iae.getMessage());
	}catch(InvocationTargetException ite){
	    System.out.println(ite.getMessage());
	    throw new InvocationTargetException(ite, ite.getMessage());
	}catch(NoSuchMethodException nse){
	    System.out.println(nse.getMessage());
	    throw new NoSuchMethodException("NoSuchMethodException:" +nse.getMessage());
	}
    }
     
    //=====================20140521 
    public void getMailListbefore(Date actualendDate, String subject, String templateFileName, Map display, List mailAddress,
			List attachFiles, Map cidMap) throws Exception {
		log.info("babugetMailList");
		// #1 取得寄件者與其他相關配置檔
		// #2 取得CC報表的URL
		// #3 用URL轉成PDF再轉jpg
		// #4 用URL轉成xls
		// #5 寄信
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
			actualendDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, DateUtils.format(new Date(), DateUtils.C_DATE_PATTON_SLASH));
			log.info("todate="+actualendDate);
			Date endDate = DateUtils.addDays(actualendDate, 10);
			log.info("endDate="+endDate);
			List<ImPromotion> imPromotions = imPromotionDAO.findByProperty("endDate", endDate);
			log.info("size" + imPromotions.size() + endDate);
			String reportFileName = null;
			String brandCode = null;
			String orderTypeCode = "PMO";
			String itemCategory = "T1GS";
			String itemCategory2 = "T1BS";			
		//	List<ImPromotion> imPromotionBra = imPromotionDAO.findByProperty("itemCategory",itemCategory);
			List imPromotionList = imPromotionDAO.findImPromotionByProperty(endDate,itemCategory);
			log.info("imPromotionList"+imPromotionList.size());
			List imPromotionList2 = imPromotionDAO.findImPromotionByProperty(endDate,itemCategory2);			
			String subjectError = null;
			String description = null;
			String categoryName = null;
			String orderNo = null;
			String fileName = null;
			StringBuffer emailError = new StringBuffer();
			List<File> png_files = new ArrayList<File>();
			PDDocument doc = null;
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("MailListEND"
					+ orderTypeCode,itemCategory);
			log.info("buCommonPhraseLine=" + buCommonPhraseLine);
			String[] attribute2 = buCommonPhraseLine.getAttribute2().split(";");
			for (int i = 0; i < attribute2.length; i++) {
				if (StringUtils.hasText(attribute2[i])) {
					//System.out.println("通知email ==> " + attribute2[i]);
					mailAddress.add(attribute2[i]);	
				}
				System.out.println("BaubmailAddress:"+mailAddress);
			}
			String[] itemCategory3 = {"T1BS","T1GS"};
			for (int k = 0; k < itemCategory3.length; k++) {
				
				List imPromotionList5 = imPromotionDAO.findImPromotionByProperty(endDate,itemCategory3[k]);
				log.info("imPromotionList5====="+imPromotionList5.size());
				ImPromotion imPromotion = (ImPromotion)imPromotionList5.get(k);
				log.info("imPromotionList5Category3=====" + imPromotion.getItemCategory()+imPromotion.getEndDate());
			
		//	for (Iterator iterator = imPromotionList.iterator(); iterator.hasNext();) {
		//		ImPromotion imPromotion = (ImPromotion) iterator.next();
				//log.info("imPromotion=====" + imPromotion.getEndDate());
				log.info("imPromotion=====" + imPromotion.getItemCategory()+imPromotion.getEndDate());
			
				Date pendDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, DateUtils.format(imPromotion.getEndDate(), DateUtils.C_DATE_PATTON_SLASH));
				log.info("pendDate=====" + pendDate +"endDate======" + endDate);
				log.info("getItemCategory=" + imPromotion.getItemCategory());
				if(pendDate.equals(endDate)){
					//imPromotion.getItemCategory().equals(itemCategory)&&
					brandCode = imPromotion.getBrandCode();
					orderTypeCode = imPromotion.getOrderTypeCode();
					itemCategory = AjaxUtils.getPropertiesValue(imPromotion.getItemCategory(), "");

					// 取得業種子類名稱
					
					ImItemCategory imItemCategory = imItemCategoryService.findById(brandCode, ImItemCategoryDAO.ITEM_CATEGORY,
							itemCategory);
					if (null == imItemCategory) {
						categoryName = "";
					} else {
						categoryName = imItemCategory.getCategoryName().concat("-");
					}
					log.info("categoryName"+categoryName);
					// #1  取得配置檔得到 寄信的報表與
					
					 // +orderTypeCode itemCategory
					if (null == buCommonPhraseLine) {
						// 寄給故障維謢人員
						mailAddress.add("Developer@tasameng.com.tw");
						reportFileName = "SO0753_1.rpt";
						subjectError = "MailList" + orderTypeCode + "PMO" + "無配置檔異常 ";
						description = "";
					} else {
						reportFileName = buCommonPhraseLine.getAttribute1();
					
						log.info("buCommonPhraseLine=" + buCommonPhraseLine);
						
						// 2.營業 從 BU_COMMON_PHRASE_LINE 取得
						// 修改為寄給多個單位，以分號分隔email地址 by Weichun 2011.09.15						
					/*	String[] attribute2 = buCommonPhraseLine.getAttribute2().split(";");
							for (int i = 0; i < attribute2.length; i++) {
								if (StringUtils.hasText(attribute2[i])) {
									//System.out.println("通知email ==> " + attribute2[i]);
									mailAddress.add(attribute2[i]);	
								}
								System.out.println("BaubmailAddress:"+mailAddress);
							}*/
						// 3.商品異動_副本 從 BU_COMMON_PHRASE_LINE 取得
						String attribute3 = buCommonPhraseLine.getAttribute3();
						if (StringUtils.hasText(attribute3)) {
							mailAddress.add(attribute3);	
						}
						// 前半部主旨
						description = buCommonPhraseLine.getDescription();
					}

					description = buCommonPhraseLine.getDescription();
					subject = description;//.concat(categoryName).concat(imPromotion.getOrderTypeCode()).concat(imPromotion.getOrderNo());
					if (StringUtils.hasText(subjectError)) {
						subject = subjectError + subject;
					}
					
					orderNo = imPromotion.getOrderNo();
					fileName = itemCategory.concat("-").concat(orderTypeCode).concat(orderNo);
					// 設定範本
					templateFileName = "CommonTemplate.ftl";
		log.info("prompt0"+ brandCode+"   prompt1"+ orderTypeCode+"    prompt2"+  orderNo);
					// #2 取得cc連結
					Map returnMap = new HashMap(0);
					Map parameters = new HashMap(0);
					parameters.put("prompt0", brandCode);
					parameters.put("prompt1", orderTypeCode);
					parameters.put("prompt2",  orderNo);
					//parameters.put("prompt3", "");
					//parameters.put("prompt4", orderNo);
					//parameters.put("prompt5", orderNo);
					String reportUrl = SystemConfig.getPureReportURL(brandCode, orderTypeCode, imPromotion.getLastUpdatedBy(),
							reportFileName, parameters);
					log.info("babureportUrl:"+reportUrl);
					// "http://10.1.99.161:8080/crystal/t2/SO0752.rpt?crypto=56557594c4cafe66e6f67ad5cebf7c2445a1727c95855b5469e88aea836ed3d62a509c34795a3452&prompt2=&prompt5=201104270004&prompt0=T2&prompt4=201104270004&prompt1=PAJ&prompt3=";

					
					try {
						// #3 取得pdf
						URL pdfUrl = new URL(reportUrl + "&init=pdf");
						URLConnection pdfConnection = pdfUrl.openConnection();

						// - 模擬client端的瀏覽器 避免被當作網頁機器人而被阻擋
						pdfConnection.setRequestProperty("User-Agent", "");
						pdfConnection.setRequestProperty("accept-language", "");
						pdfConnection.setRequestProperty("cookie", "");
						pdfConnection.setDoOutput(true);

						BufferedInputStream pdfBis = new BufferedInputStream(pdfConnection.getInputStream());
						System.out.println("pdfBis = " + pdfBis);
						File pdfFile = File.createTempFile(fileName, ".pdf");
						FileOutputStream pdfFos = new FileOutputStream(pdfFile);
						byte[] data = new byte[1];
						while (pdfBis.read(data) != -1) {
							pdfFos.write(data);
						}
						pdfFos.flush();
						pdfFos.close();

						// #4 取得excel
						URL xlsUrl = new URL(reportUrl + "&init=xls");
						URLConnection xlsConnection = xlsUrl.openConnection();

						// - 模擬client端的瀏覽器 避免被當作網頁機器人而被阻擋
						xlsConnection.setRequestProperty("User-Agent", "");
						xlsConnection.setRequestProperty("accept-language", "");
						xlsConnection.setRequestProperty("cookie", "");
						xlsConnection.setDoOutput(true);

						BufferedInputStream xlsBis = new BufferedInputStream(xlsConnection.getInputStream());
						System.out.println("xlsBis = " + xlsBis);
						File xlsFile = File.createTempFile(fileName + "-" + DateUtils.getCurrentDateStr("yyyyMMddHHmmss") + "_",
								".xls");
						FileOutputStream xlsFos = new FileOutputStream(xlsFile);
						byte[] xlsData = new byte[1];
						while (xlsBis.read(xlsData) != -1) {
							xlsFos.write(xlsData);
						}
						xlsFos.flush();
						xlsFos.close();

						//attachFiles.add(xlsFile.getAbsolutePath());

						// pdf轉圖檔
						doc = PDDocument.load(pdfFile);
						List pages = doc.getDocumentCatalog().getAllPages();
						for (int j = 0; j < pages.size(); j++) {
							PDPage page = (PDPage) pages.get(j);
							BufferedImage bufferedImage = page.convertToImage();
							File imageFile = File.createTempFile(fileName + j, ".jpg"); // "new File("PA"+j+".png") File.createTempFile("PA"+j,".gif")
							ImageIO.write(bufferedImage, "jpg", imageFile);
							png_files.add(imageFile);
							//attachFiles.add(imageFile.getAbsolutePath());

							//cidMap.put(imageFile.getAbsolutePath(), png_files.get(j).getName());

						}

						StringBuffer html = new StringBuffer();
						System.out.println("emailError.toString() = " + emailError.toString());
						if (StringUtils.hasText(emailError.toString())) {
							html.append(emailError.toString());
						}

						for (int i = 0; i < png_files.size(); i++) {
							html.append("<img src='cid:" + png_files.get(i).getName() + "'/><br>");
						}
						System.out.println("html = " + html.toString());
						// 設定樣板的內容值
						//display.put("display", html.toString());
						display.put("display", "促銷單"+imPromotion.getItemCategory()+"-"+imPromotion.getOrderNo()+"活動結束提前十日通知");
						// #5
						
						//if(pendDate.equals(endDate)&&imPromotion.getItemCategory().equals(itemCategory2)){
							log.info("準備SendMAIL");
		                MailUtils.sendMail(subject, templateFileName, display, mailAddress, attachFiles, cidMap);
		                log.info("subject="+subject +"templateFileName"+templateFileName + "display" + display +"mailAddress"+ mailAddress+"attachFiles"+attachFiles+"cidMap"+cidMap);
						/*}else{
							throw new NoSuchDataException("結束促銷寄信失敗");
						}*/
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
    
    public void getMailListbeforeSteve(Date actualendDate, String subject, String templateFileName, Map display, List mailAddressFrom,
			List attachFiles, Map cidMap) throws Exception {
		log.info("babugetMailList");
		// #1 取得寄件者與其他相關配置檔
		// #2 取得CC報表的URL
		// #3 用URL轉成PDF再轉jpg
		// #4 用URL轉成xls
		// #5 寄信
		try {
			log.info("------PromoEnd-----Start");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
			Date endDate = DateUtils.addDays(actualendDate, 10);
			String endDateAfterFormat = sdf.format(endDate);    //格式化後日期 ex  2014/06/12-->2014/06/12
			log.info("todate="+sdf.format(endDate));
			
			String reportFileName = null;
			String brandCode = null;
			String orderTypeCode = "PMO";
			String itemCategory = "T1GS";
			String itemCategory2 = "T1BS";			
		    String subjectError = null;
			String description = null;
			String categoryName = null;
			String orderNo = null;
			String fileName = null;
			StringBuffer emailError = new StringBuffer();
			List<File> png_files = new ArrayList<File>();
			PDDocument doc = null;
			
			
			String[] itemCategory3 = {"T1BS","T1GS"};
			for (int k = 0; k < itemCategory3.length; k++) {
				List mailAddress = new ArrayList();
				List imPromotionList5 = imPromotionDAO.findImPromotionByPropertySteve(endDateAfterFormat,itemCategory3[k]);
				 log.info(itemCategory3[k]+"的促銷單數量為:"+ imPromotionList5.size());
				 
				 BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("MailListEND"
							+ orderTypeCode,itemCategory3[k]);
				
				  
				 if (null == buCommonPhraseLine) {
						// 寄給故障維謢人員
						mailAddress.add("Developer@tasameng.com.tw");
						subjectError = "MailList" + orderTypeCode + itemCategory + "無配置檔異常 ";
						description = "";
				 } else {
					 String[] attribute2 = buCommonPhraseLine.getAttribute2().split(";");
						for (int i = 0; i < attribute2.length; i++) {
							if (StringUtils.hasText(attribute2[i])) {
								log.info("通知email ==> " + attribute2[i]);
								mailAddress.add(attribute2[i]);	
							}
						}
						System.out.println("BaubmailAddress:"+mailAddress);

						// 前半部主旨
						description = buCommonPhraseLine.getDescription();
				 }
				

					
					subject = itemCategory3[k]+"促銷結束前10日通知";
					if (StringUtils.hasText(subjectError)) {
						subject = subjectError + subject;
					}

					// 設定範本
					templateFileName = "CommonTemplate.ftl";

					StringBuffer html = new StringBuffer();
					System.out.println("emailError.toString() = " + emailError.toString());
					if (StringUtils.hasText(emailError.toString())) {
						html.append(emailError.toString());
					}
					for(int j=0;j<imPromotionList5.size();j++){
						ImPromotion head = (ImPromotion)imPromotionList5.get(j);
					    html.append("促銷單：PMO" + head.getOrderNo()+"<br>");
					}
					System.out.println("subject = " + subject);
					System.out.println("html = " + html.toString());
					// 設定樣板的內容值
					display.put("display", html.toString());
					log.info("準備SendMAIL");
					// 發信通知
					MailUtils.sendMail(subject, templateFileName, display, mailAddress, attachFiles, cidMap);
			}
			   
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

   
    
    /**
     * 匯入促銷商品明細
     * 
     * @param headId
     * @param promotionItems
     * @throws Exception
     */
    public void executeImportPromotionCombines(Long headId, List imPromotionReCombines) throws Exception{
        
        try{
    	    ImPromotion promotionPO = findById(headId);
    	    if(promotionPO == null){
	        throw new NoSuchObjectException("查無促銷貨單主鍵(" + headId + ")的資料");
	    }       	
    	    if(imPromotionReCombines != null && imPromotionReCombines.size() > 0){
    	        promotionPO.setImPromotionReCombines(imPromotionReCombines);
    	    }else{
    	        promotionPO.setImPromotionReCombines(new ArrayList(0));
    	    }
    	
    	    imPromotionDAO.update(promotionPO);      	
        }catch (Exception ex) {
		    log.error("促銷商品明細匯入時發生錯誤，原因：" + ex.toString());
		    throw new Exception("促銷商品明細匯入時發生錯誤，原因：" + ex.getMessage());
        }        
    } 
    
    
    /**
     * 暫存單號取實際單號並更新至Promotion主檔
     * 
     * @param headId
     * @param loginUser
     * @return ImPromotion
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    public ImPromotion saveActualOrderNo(Long headId, String loginUser)
    throws ObtainSerialNoFailedException, FormException, Exception {
    	log.info("saveActualOrderNo");

    	ImPromotion promotionPO = imPromotionDAO.findById(headId);
    	if(promotionPO == null){
    		throw new NoSuchObjectException("查無促銷單主鍵(" + headId + ")的資料！");
    	}else if (AjaxUtils.isTmpOrderNo(promotionPO.getOrderNo())) {
    		String serialNo = buOrderTypeService.getOrderSerialNo(
    				promotionPO.getBrandCode(), promotionPO.getOrderTypeCode());
    		if (!serialNo.equals("unknow")) {
    			promotionPO.setOrderNo(serialNo);
    		} else {
    			throw new ObtainSerialNoFailedException("取得"
    					+ promotionPO.getOrderTypeCode() + "單號失敗！");
    		}
    	}
    	promotionPO.setLastUpdatedBy(loginUser);
    	promotionPO.setLastUpdateDate(new Date());
    	imPromotionDAO.update(promotionPO);

    	return promotionPO;
    }
    
    public List<Properties> updateField(Properties httpRequest){
    	Map returnMap = new HashMap();
    	Properties properties = new Properties();
    	List<Properties> list =  new ArrayList();
    	try{
    		Long headId = Long.valueOf((String)httpRequest.getProperty("headId"));
    		String type = (String)httpRequest.getProperty("type");
    		
    		log.info("headId: "+headId);
    		log.info("type: "+type);
    		
    		ImPromotion imPromotion = findById(headId);
    		//imPromotion.setEnable("N");
    		List<ImPromotionFull> imPromotionFulls = (List<ImPromotionFull>)imPromotion.getImPromotionFulls();
    		for(ImPromotionFull imPromotionFull : imPromotionFulls){
    			imPromotionFull.setIsJoin("Y");
    			imPromotionFull.setDiscountRate(100L);
    		}
    		imPromotionDAO.update(imPromotion);
    		properties.setProperty("enable", "N");
    		properties.setProperty("discount", "0");
    		list.add(properties);
    	}catch(Exception ex) {
    		
    	}
//    	properties.setProperty("sqls", jsArr.toString());
		return list;
    }
    
    /**
     * 檢核促銷活動相關資料
     * 
     * @param imPromotion
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    public void checkPromotionData(ImPromotion imPromotion, String programId, String identification) 
    throws ValidationErrorException {

    	String message = null;
    	List errorMsgs = new ArrayList(0);
    	try{
    		this.checkPromotionHead(imPromotion, programId, identification, errorMsgs);
//  		checkPromotionItems(imPromotion, programId, identification, errorMsgs);
//  		checkPromotionShops(imPromotion, programId, identification, errorMsgs);
    	}catch(Exception ex){
    		message = "檢核促銷單時發生錯誤，原因：" + ex.toString();
    		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
    		errorMsgs.add(message);
    		log.error(message);
    	}
    }

    /**
     * check promotion head
     * 
     * @param imPromotion
     * @param programId
     * @param identification
     * @param errorMsgs
     */
    private void checkPromotionHead(ImPromotion imPromotion, String programId, String identification, List errorMsgs){

    	String message = null;
    	try{
    		String brandCode = imPromotion.getBrandCode();

    		if(!StringUtils.hasText(imPromotion.getPromotionCode()) && !"T2".equalsIgnoreCase(brandCode) ){
    			message = "請輸入活動代號！";
    			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
    			errorMsgs.add(message);
    			log.error(message);
    		}

    		if(imPromotion.getBuyAmount() < 0 || imPromotion.getBuyAmount() == null){
    			message = "請輸入購買金額！";
    			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
    			errorMsgs.add(message);
    			log.error(message);
    		}
    		
    		if(imPromotion.getDiscount() < 0 || imPromotion.getDiscount() == null){
    			message = "請輸入購買金額！";
    			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
    			errorMsgs.add(message);
    			log.error(message);
    		}
    		
    		if(StringUtils.hasText(imPromotion.getEnable())){
    			message = "請輸入是否參與VIP！";
    			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
    			errorMsgs.add(message);
    			log.error(message);
    		}

    		if(StringUtils.hasText(imPromotion.getInCharge())){
    			BuEmployeeWithAddressView employeeWithAddressView = 
    				buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(imPromotion.getBrandCode(), imPromotion.getInCharge());
    			if(employeeWithAddressView == null){
    				message = "查無" + imPromotion.getInCharge() + "的員工資料！";
    				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
    				errorMsgs.add(message);
    				log.error(message);
    			}	       
    		}
    		
    		if(imPromotion.getBeginDate() == null){
    			message = "請輸入開始日期！";
    			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
    			errorMsgs.add(message);
    			log.error(message);
    		}
    		
    		if(imPromotion.getEndDate() == null){
    			message = "請輸入結束日期！";
    			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
    			errorMsgs.add(message);
    			log.error(message);
    		}

    		
    	}catch(Exception ex){
    		message = "檢核促銷單主檔時發生錯誤，原因：" + ex.toString();
    		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
    		errorMsgs.add(message);
    		log.error(message);
    	}	
    }
}    