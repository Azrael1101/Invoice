package tw.com.tm.erp.hbm.service;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.util.StringUtils;
import bsh.StringUtil;
import tw.com.tm.erp.action.ImPromotionMainAction;
import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCategoryId;
import tw.com.tm.erp.hbm.bean.ImItemCompose;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.bean.ImItemPriceView;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImPriceAdjustment;
import tw.com.tm.erp.hbm.bean.ImPriceList;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.ImPromotionView;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImPriceAdjustmentDAO;
import tw.com.tm.erp.hbm.dao.ImPriceListDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionViewDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;
import tw.com.tm.erp.utils.WfApprovalResultUtils;
import javax.swing.*; 

public class ImPriceAdjustmentMainService {
    private static final Log log = LogFactory.getLog(ImPriceAdjustmentMainService.class);

    public static final String PROGRAM_ID= "IM_PRICE_ADJUSTMENT";
    public static final String PAP= "PAP";
    public static final String PAJ= "PAJ";

    private ImPriceAdjustmentDAO imPriceAdjustmentDAO;
    private ImPriceListDAO imPriceListDAO;
    private ImItemPriceDAO imItemPriceDAO;
    private ImItemDAO imItemDAO;
    private ImOnHandDAO imOnHandDAO;
    private ImItemCategoryDAO imItemCategoryDAO;
    private ImItemPriceViewDAO imItemPriceViewDAO;
    private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
    private BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO;
    private BuSupplierDAO buSupplierDAO;
    private NativeQueryDAO nativeQueryDAO;
    private BuBrandDAO buBrandDAO;
    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
    private BuEmployeeDAO buEmployeeDAO;

    private CeapProcessService ceapProcessService;
    private BuOrderTypeService buOrderTypeService;
    private BuCommonPhraseService buCommonPhraseService;
    private BuBasicDataService buBasicDataService;
    private BuBrandService buBrandService;
    private BuEmployeeWithAddressViewService buEmployeeWithAddressViewService;
    private PoPurchaseOrderHeadService poPurchaseOrderHeadService;
    private ImPriceListService imPriceListService;
    private ImItemCategoryService imItemCategoryService;
    private ImItemService imItemService;
    private SiProgramLogAction siProgramLogAction;
    private ImPromotionDAO imPromotionDAO;
    
    /**
     * 定價單明細
     */
    public static final String[] GRID_FIELD_NAMES = {
	"indexNo", "category01", "itemCode",
	"reserve5", "itemCName", "foreignCost",
	"localCost", "unitPrice", "costRate",
	"priceId",
	"lineId", "isLockRecord", "isDeleteRecord", "message"
    };

    public static final int[] GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_LONG,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };

    public static final String[] GRID_FIELD_DEFAULT_VALUES = {
	"0", "", "",
	"", "", "0",
	"0", "0", "0",
	"",
	"",AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };

    /**
     * 定價單明細 for T2
     */
    public static final String[] T2_PAP_GRID_FIELD_NAMES = {
	"indexNo", "category02", "itemCode",
	"reserve5", "itemCName", "currencyCode",
	"exchangeRate", "foreignCost", "localCost",
	"unitPrice", "grossRate", "priceId",
	"lineId", "isLockRecord", "isDeleteRecord", "message"
    };

    public static final int[] T2_PAP_GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_LONG,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };

    public static final String[] T2_PAP_GRID_FIELD_DEFAULT_VALUES = {
	"0", "", "",
	"", "", "",
	"0.0", "0", "0",
	"0", "0", "",
	"",AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };

    /**
     * 變價單明細
     */
    public static final String[] PAJ_GRID_FIELD_NAMES = {
	"indexNo","itemCode", "reserve5",
	"itemCName", "originalQuotationPrice","newQuotationPrice",
	"originalPrice", "unitPrice", "grossProfitRate",
	"priceId", "isTax", "typeCode",
	"taxCode",
	"lineId", "isLockRecord", "isDeleteRecord", "message"
    };

    public static final int[] PAJ_GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };

    public static final String[] PAJ_GRID_FIELD_DEFAULT_VALUES = {
	"0", "", "",
	"", "0", "0",
	"0", "0", "0",
	"", "","",
	"",
	"",AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };

    /**
     * 變價單明細 for T2
     */
    public static final String[] T2_PAJ_GRID_FIELD_NAMES = {
	"indexNo","itemCode", "reserve5",
	"category02", "category02Name", "itemCName",
	"originalCurrencyCode", "currencyCode","exchangeRate",
	"originalForeignCost", "foreignCost","originalPrice",
	"unitPrice", "originalGrossRate", "grossRate",
	"totalStock", "priceId", "isTax",
	"typeCode", "taxCode", "originalExchangeRate",
	"lineId", "isLockRecord", "isDeleteRecord", "message"
    };

    public static final int[] T2_PAJ_GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };

    public static final String[] T2_PAJ_GRID_FIELD_DEFAULT_VALUES = {
	"0", "", "",
	"", "", "",
	"", "", "0.0",
	"0", "0", "0",
	"0", "0", "0",
	"0", "0", "0",
	"0", "", "",
	"", "", "0.0",
	"",AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };

    /**
     * 定變價 PICKER
     */
    public static final String[] GRID_SEARCH_FIELD_NAMES = {
	"orderTypeCode", "orderNo", "description",
	"priceType",   "enableDate", "status",
	"supplierCode", "supplierName", "headId"
    };

    public static final int[] GRID_SEARCH_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_LONG
    };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "", "",
	"", "", "0"};

    /**
     * 定價單 LINE PICKER
     */
    public static final String[] GRID_LINE_PAP_SEARCH_FIELD_NAMES = {
	"itemCode", 	"itemCName", "itemEName",
	"itemUnit",   	"priceType", "supplierQuotationPrice",
	"unitPrice",   	"priceEnableName", "priceId"
    };

    public static final String[] GRID_LINE_PAP_SEARCH_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "", "0.0",
	"0.0", "", "0"

    };

    /**
     * 變價單 LINE PICKER
     */
    public static final String[] GRID_LINE_PAJ_SEARCH_FIELD_NAMES = {
	"itemCode", 	"itemCName", "itemEName",
	"itemUnit",   	"priceType", "supplierQuotationPrice",
	"unitPrice",   	"beginDate", "priceId"
    };

    public static final String[] GRID_LINE_PAJ_SEARCH_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "", "0.0",
	"0.0", "", "0"

    };

    /**
     * 定價單 LINE PICKER for t2
     */
    public static final String[] T2_GRID_LINE_PAP_SEARCH_FIELD_NAMES = {
	"itemCode", 	"itemCName", "itemEName",
	"itemUnit",   	"priceType", "purchaseAmount",
	"unitPrice",   	"priceEnableName", "priceId"
    };

    public static final String[] T2_GRID_LINE_PAP_SEARCH_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "", "0.0",
	"0.0", "", "0"

    };

    /**
     * 變價單 LINE PICKER for t2
     */
    public static final String[] T2_GRID_LINE_PAJ_SEARCH_FIELD_NAMES = {
	"itemCode", 	"itemCName", "itemEName",
	"itemUnit",   	"priceType", "purchaseAmount",
	"unitPrice",   	"beginDate", "priceId"
    };

    public static final String[] T2_GRID_LINE_PAJ_SEARCH_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "", "0.0",
	"0.0", "", "0"

    };

    public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
        this.buEmployeeDAO = buEmployeeDAO;
    }

    public void setCeapProcessService(CeapProcessService ceapProcessService) {
        this.ceapProcessService = ceapProcessService;
    }

    public ImItemService getImItemService() {
        return imItemService;
    }

    public void setImItemService(ImItemService imItemService) {
        this.imItemService = imItemService;
    }

    public void setImPriceAdjustmentDAO(
	    ImPriceAdjustmentDAO imPriceAdjustmentDAO) {
	this.imPriceAdjustmentDAO = imPriceAdjustmentDAO;
    }

    public void setImItemPriceDAO(ImItemPriceDAO imItemPriceDAO) {
	this.imItemPriceDAO = imItemPriceDAO;
    }

    public void setImItemPriceViewDAO(ImItemPriceViewDAO imItemPriceViewDAO) {
	this.imItemPriceViewDAO = imItemPriceViewDAO;
    }

    public void setImItemCurrentPriceViewDAO(
	    ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
	this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
    }

    public void setImItemDAO(ImItemDAO imItemDAO) {
	this.imItemDAO = imItemDAO;
    }

    public void setBuSupplierWithAddressViewDAO(BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO) {
	this.buSupplierWithAddressViewDAO = buSupplierWithAddressViewDAO;
    }

    public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
	this.buOrderTypeService = buOrderTypeService;
    }

    public void setImPriceListService(ImPriceListService imPriceListService) {
	this.imPriceListService = imPriceListService;
    }

    public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
	this.nativeQueryDAO = nativeQueryDAO;
    }

    public void setImPriceListDAO(ImPriceListDAO imPriceListDAO) {
	this.imPriceListDAO = imPriceListDAO;
    }

    public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
	this.buCommonPhraseService = buCommonPhraseService;
    }

    public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
	this.buBasicDataService = buBasicDataService;
    }

    public void setBuBrandService(BuBrandService buBrandService) {
	this.buBrandService = buBrandService;
    }

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
	this.buBrandDAO = buBrandDAO;
    }
    
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }

    public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
	this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    }

    public void setPoPurchaseOrderHeadService(
	    PoPurchaseOrderHeadService poPurchaseOrderHeadService) {
	this.poPurchaseOrderHeadService = poPurchaseOrderHeadService;
    }

    public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
	this.imItemCategoryService = imItemCategoryService;
    }

    public void setBuEmployeeWithAddressViewService(
	    BuEmployeeWithAddressViewService buEmployeeWithAddressViewService) {
	this.buEmployeeWithAddressViewService = buEmployeeWithAddressViewService;
    }

    public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
	this.imItemCategoryDAO = imItemCategoryDAO;
    }

    public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
	this.imOnHandDAO = imOnHandDAO;
    }
    
    public void setBuSupplierDAO(BuSupplierDAO buSupplierDAO) {
	this.buSupplierDAO = buSupplierDAO;
    }
    
    public void setBuEmployeeWithAddressViewDAO(
	    BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
	this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
    }

	public void setImPromotionDAO(ImPromotionDAO imPromotionDAO) {
		this.imPromotionDAO = imPromotionDAO;
	}
	
	public void setImPromotionViewDAO(ImPromotionViewDAO imPromotionViewDAO) {
		this.imPromotionViewDAO = imPromotionViewDAO;
	}
	private ImPromotionViewDAO imPromotionViewDAO;
	
	public void setImPromotionViewService(ImPromotionViewService imPromotionViewService) {
		this.imPromotionViewService = imPromotionViewService;
	}
	private ImPromotionViewService imPromotionViewService;
    /**
     * 計算成本率
     * @param localCost
     * @param unitPrice
     * @return
     */
    private String calCostRate(String localCost,String unitPrice){
	String calResult="0";
	double tmpResult = 0L;
	if(null==localCost || null == unitPrice || "0".equals(unitPrice) || "0.0".equals(unitPrice) ){
	    return calResult;
	}else{
	    tmpResult = (Double.parseDouble(localCost)/Double.parseDouble(unitPrice))*100;
	    calResult = NumberUtils.roundToStr(tmpResult, 2);
	}

	return calResult;
    }

    /**
     * 計算毛利率 = (售價 - (本地成本))/售價
     * @param localCost 本地成本 = 原幣成本*匯率
     * @param unitPrice
     * @return
     */
    private String calGrossRate(String localCost,String unitPrice){
	String calResult="0";
	double tmpResult = 0L;
	if(null==localCost || null == unitPrice || "0".equals(unitPrice) || "0.0".equals(unitPrice) ){
	    return calResult;
	}else{
	    tmpResult = (Double.parseDouble(unitPrice)-(Double.parseDouble(localCost)))/Double.parseDouble(unitPrice);
	    calResult = NumberUtils.roundToStr(tmpResult*100, 2);
	}

	return calResult;
    }

    public static Object[] completeAssignment(long assignmentId, boolean approveResult,String approvalComment ) throws Exception{
	try
	{

	    HashMap context = new HashMap();
	    context.put("approveResult", approveResult);
	    context.put("approvalComment", approvalComment);
	    return ProcessHandling.completeAssignment(assignmentId, context);
	}
	catch (Exception e){
	    log.error("完成商品定價變價任務時發生錯誤："+e.getMessage());
	    throw new Exception(e);
	}

    }

    /**
     * 檢核主檔,明細檔
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List checkedImPriceAdjustment(Map parameterMap)throws FormException, Exception{
	List errorMsgs = new ArrayList(0);
	String message = null;
	String identification = null;
	ImPriceAdjustment head = null;
	try{

	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    head = getActualImPriceAdjustment(formLinkBean);

	    String status = head.getStatus();

	    if ( (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)) ) {

		identification = MessageStatus.getIdentification(head.getBrandCode(),
			head.getOrderTypeCode(), head.getOrderNo());

		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

		validateImPriceAdjustment( head, PROGRAM_ID, identification, errorMsgs, formLinkBean );
	    }

	}catch (Exception ex) {
	    message = "定變價單檢核失敗，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
	return errorMsgs;
    }

    /**
     *
     * @param head
     */
    private void deleteLine(ImPriceAdjustment head){
	log.info("=====<deleteLine>====");
	List<ImPriceList> imPriceLists = head.getImPriceLists();
	if(imPriceLists != null && imPriceLists.size() > 0){
	    for(int i = imPriceLists.size() - 1; i >= 0; i--){
		ImPriceList imPriceList = imPriceLists.get(i);
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imPriceList.getIsDeleteRecord())){
		    log.info("刪除 = " + imPriceList.getItemCode() +" 索引 " + imPriceList.getIndexNo());
		    imPriceLists.remove(imPriceList);
		}
	    }
	}

	for (int i = 0; i < imPriceLists.size(); i++) {
	    ImPriceList line = imPriceLists.get(i);
	    line.setIndexNo(i+1L);
	}
	log.info("=====<deleteLine/>====");
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
	    String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
	    Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
	    Map multiList = new HashMap(0);

	    ImPriceAdjustment form = null == formId ? executeNewImPriceAdjustment(otherBean, resultMap):findExistImPriceAdjustment(otherBean, resultMap);

	    if(form != null && form.getSupplierCode() != null ){
		String supplierCode = form.getSupplierCode();
		String brandCode = form.getBrandCode();
		String supplierName ="";
		BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(brandCode, supplierCode);
		log.info("buSWAV = " + buSWAV );
		if(buSWAV != null){
		    supplierName = AjaxUtils.getPropertiesValue(buSWAV.getChineseName(), "");
		}
		log.info("supplierName = " + supplierName );
		form.setSupplierName(supplierName);
	    }

	    form.setReserve3(UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
	    List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(form.getBrandCode() ,"PA");
	    List<BuCurrency> allCurrency = buBasicDataService.findCurrencyList("", "", "");

//	    BuCurrency tmpC = new BuCurrency();
//	    tmpC.setCurrencyCode("TW");
//	    tmpC.setCurrencyCName("新台幣");
//	    allCurrency.add(0,tmpC);

	    // 設定T2的下拉
	    setT2Select(form, multiList);

	    List<BuCommonPhraseLine> allPriceType =  buCommonPhraseService.getCommonPhraseLinesById("PriceType",false);
	    multiList.put("allCurrencys", AjaxUtils.produceSelectorData(allCurrency ,"currencyCode" ,"currencyCName",  true,  true));
	    multiList.put("allPriceTypes" , AjaxUtils.produceSelectorData(allPriceType  ,"lineCode" ,"name",  true,  true));
	    multiList.put("allOrderTypes" , AjaxUtils.produceSelectorData(allOrderTypes ,"orderTypeCode" ,"name",  true,  true));
	    resultMap.put("multiList",multiList);
	    resultMap.put("form", form);
	    String brandName = (buBrandService.findById(form.getBrandCode())).getBrandName();
	    resultMap.put("brandName", brandName);
	    resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
	    resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));

	    resultMap.put("multiList",multiList);
	    log.info("========<executeInitial/>========");
	    return resultMap;
	}catch(Exception ex){
	    log.error("定變價單初始化失敗，原因：" + ex.toString());
	    throw new Exception("定變價單初始化失敗，原因：" + ex.toString());
	}
    }

    /**
     * 新建一筆
     * @param otherBean
     * @param resultMap
     * @return
     * @throws Exception
     */
    public ImPriceAdjustment executeNewImPriceAdjustment(Object otherBean,Map resultMap)throws Exception{
	String loginBrandCode     = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	String loginEmployeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	String orderTypeCode      = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
	ImPriceAdjustment form = new ImPriceAdjustment();
	form.setBrandCode(loginBrandCode);
	form.setOrderTypeCode(orderTypeCode);
	form.setPriceType("1");
	form.setSupplierCode("");

	if(loginBrandCode.indexOf("T2") > -1){
	    if(PAP.equalsIgnoreCase(orderTypeCode)){
		form.setEnableDate(DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH, DateUtils.getCurrentDateStr(DateUtils.C_DATE_PATTON_SLASH)));
	    }
	}else{
	    form.setEnableDate(null);
	}
	form.setExchangeRate(1D);
	form.setRatio(1D);
	form.setImPriceLists(null);
	form.setDescription("");
	form.setStatus(OrderStatus.SAVE);
	form.setCreatedBy(loginEmployeeCode);
	saveTmp(form);
	return form;

    }

    /**
     * 定變價查詢
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public List<Properties> executeSearchInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);
	Map multiList = new HashMap(0);
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");

	    resultMap.put("orderTypeCode", orderTypeCode);

	    List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(loginBrandCode ,"PA");
	    List<ImItemCategory> allItemCategorys = imItemCategoryService.findByCategoryType(loginBrandCode, "ITEM_CATEGORY");

	    multiList.put("allOrderTypes"        , AjaxUtils.produceSelectorData(allOrderTypes         ,"orderTypeCode" ,"name"         ,  true,  true, orderTypeCode != null ? orderTypeCode : "" ));
	    multiList.put("allItemCategorys",    AjaxUtils.produceSelectorData( allItemCategorys,    "categoryCode",    "categoryName",  true,  true));
	    resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
	    resultMap.put("multiList",multiList);

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

    /**
     * 定變價明細查詢 初始化
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public List<Properties> executePriceSearchInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);

	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    String priceStatus = (String)PropertyUtils.getProperty(otherBean, "priceStatus");
	    String priceType = (String)PropertyUtils.getProperty(otherBean, "priceType");

	    Map multiList = new HashMap(0);

	    List<BuCommonPhraseLine> allPriceType =  buCommonPhraseService.getCommonPhraseLinesById("PriceType",false);
	    resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
	    resultMap.put("priceStatus",priceStatus);
	    resultMap.put("priceType",priceType);

	    if(loginBrandCode.indexOf("T2") > -1){
		String itemCategory = (String)PropertyUtils.getProperty(otherBean, "itemCategory");
		String supplierCode = (String)PropertyUtils.getProperty(otherBean, "supplierCode");

		List<ImItemCategory> allItemCategory	= imItemCategoryService.findByCategoryType(loginBrandCode, "ITEM_CATEGORY");
		List<ImItemCategory> allCategory01 = imItemCategoryDAO.findByCategoryType(loginBrandCode, "CATEGORY01");
//		List<ImItemCategory> allCategory02 = imItemCategoryDAO.findByCategoryType(loginBrandCode, "CATEGORY02");

		List<BuSupplierWithAddressView> allBuSupplier = buSupplierWithAddressViewDAO.findByBrandCodeAndSupplierCodes(loginBrandCode, supplierCode);

		multiList.put("allItemCategory",    AjaxUtils.produceSelectorData( allItemCategory,    "categoryCode",    "categoryName",  true,  true, itemCategory != null ? itemCategory : ""));
		multiList.put("allCategory01", AjaxUtils.produceSelectorData(allCategory01, "categoryCode", "categoryName", true, true));
//		multiList.put("allCategory02", AjaxUtils.produceSelectorData(allCategory02, "categoryCode", "categoryName", true, true));
		multiList.put("allBuSupplier", AjaxUtils.produceSelectorData(allBuSupplier, "supplierCode", "chineseName", true, true));


	    }

	    multiList.put("allPriceTypes" , AjaxUtils.produceSelectorData(allPriceType  ,"lineCode" ,"name",  true,  true));
	    resultMap.put("multiList",multiList);

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

    /**
     * 取得訊息提示用
     * @param headId
     * @return
     * @throws Exception
     */
    public String getIdentification(Long headId) throws Exception{

	String id = null;
	try{
	    ImPriceAdjustment head = (ImPriceAdjustment)imPriceAdjustmentDAO.findByPrimaryKey(ImPriceAdjustment.class, headId);
	    if(head != null){
		id = MessageStatus.getIdentification(head.getBrandCode(),
			head.getOrderTypeCode(), head.getOrderNo());
	    }else{
		throw new NoSuchDataException("定變價主檔查無主鍵：" + headId + "的資料！");
	    }

	    return id;
	}catch(Exception ex){
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 取得實際商品定變價
     * @param bean
     * @return
     * @throws FormException
     * @throws Exception
     */
    public ImPriceAdjustment getActualImPriceAdjustment(Object bean)throws FormException,Exception{
	ImPriceAdjustment imPriceObject = null;
	String headId = (String)PropertyUtils.getProperty(bean, "headId");
	if(StringUtils.hasText(headId)){
	    imPriceObject = imPriceAdjustmentDAO.findById(NumberUtils.getLong(headId));
	    if(imPriceObject == null){
		throw new NoSuchObjectException("查無變價單主鍵"+headId+"資料");
	    }
	}else{
	    throw new ValidationErrorException("傳入變價單主鍵"+headId+"為空值");
	}

	return imPriceObject;
    }

    public List<Properties> getAJAXSupplierName(Properties httpRequest) throws FormException {
	List reList = new ArrayList();
	String brandCode = httpRequest.getProperty("brandCode");
	String supplierCode = httpRequest.getProperty("supplierCode");
	BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewDAO.findById(supplierCode, brandCode);
	Properties p = new Properties();
	p.setProperty("SupplierName","查無此供應商名稱");
	if(buSupplierWithAddressView != null){
	    p.setProperty("SupplierName", buSupplierWithAddressView.getChineseName());
	}
	reList.add(p);
	return reList;
    }

    /**
     * AJAX Line 輸入商品後 eChange 出其他欄位 百貨
     *
     * @param Properties
     * @throws Exception
     */
    public List<Properties> getAJAXLineData(Properties httpRequest) throws Exception {

	log.info("=======<getAJAXLineData>============");
	List re = new ArrayList();
	String brandCode = httpRequest.getProperty("brandCode");
	String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	String itemCode = httpRequest.getProperty("itemCode");
	String pickerPriceId = httpRequest.getProperty("priceId");

	//String lineId = httpRequest.getProperty("lineId");
	//String headId = httpRequest.getProperty("headId");
	String exchangeRate = httpRequest.getProperty("exchangeRate");
	//String ratio = httpRequest.getProperty("ratio");
	//String priceType = httpRequest.getProperty("priceType");
//	String newQuotationPrice = httpRequest.getProperty("newQuotationPrice");
	log.info("brandCode=" + brandCode);
	log.info("orderTypeCode=" + orderTypeCode);
	log.info("itemCode=" + itemCode);
	log.info("pickerPriceId=" + pickerPriceId);
	log.info("exchangeRate=" + exchangeRate);
//	log.info("newQuotationPrice=" + newQuotationPrice);


	if (StringUtils.hasText(brandCode) && StringUtils.hasText(orderTypeCode) && ( StringUtils.hasText(pickerPriceId) || StringUtils.hasText(itemCode)  ) ) {
	    String itemCName = null;
	    String unitPrice = null;
	    Properties pro = new Properties();
	    try {
		Object result = getItemInfo(httpRequest);

		if(PAP.equals(orderTypeCode)){
		    itemCode = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[0] : null, "");
		    itemCName = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[1] : null,MessageStatus.DATA_NOT_FOUND);
		    String priceId = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[2] : null,"" );
		    String category01 = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[3] : null, "" );
		    unitPrice = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[6] : null , "0");
		    String foreignCost = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[4] : null, "0");
		    String localCost = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[5] : null,"0");
		    String costRate = calCostRate(localCost,unitPrice);
		    pro.setProperty("Category01",category01);
		    pro.setProperty("ForeignCost", foreignCost);
		    pro.setProperty("LocalCost", localCost);
		    pro.setProperty("CostRate", costRate);
		    pro.setProperty("priceId", priceId);
		}else if(PAJ.equals(orderTypeCode)) {
		    itemCName = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[0] : null,MessageStatus.DATA_NOT_FOUND);
		    unitPrice = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[2] : null, "0");
		    String suppierQuotationPrice = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[1] : null,"0");
		    String priceId = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[3] : null, "");
		    String isTax = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[4] : null, "P");
		    String typeCode = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[5] : null, "1");
		    String taxCode = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[6] : null, "3");
		    itemCode = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[7] : null, "");

		    pro.setProperty("OrginalPrice",unitPrice);
		    pro.setProperty("OrginalQuotationPrice",suppierQuotationPrice);
		    pro.setProperty("newQuotationPrice",suppierQuotationPrice);
		    String grossProfitRate = calGrossProfitRate(unitPrice,suppierQuotationPrice,exchangeRate,unitPrice,suppierQuotationPrice);
		    pro.setProperty("GrossProfitRate", grossProfitRate);
		    pro.setProperty("priceId", priceId);
		    pro.setProperty("isTax", isTax);
		    pro.setProperty("typeCode", typeCode);
		    pro.setProperty("taxCode", taxCode);
		}
		log.info("itemCName=" + itemCName);
		pro.setProperty("itemCode", itemCode);
		pro.setProperty("ItemCName", itemCName);
		pro.setProperty("UnitPrice", unitPrice);


		re.add(pro);
	    } catch (Exception e) {
		e.printStackTrace();
		log.error("查詢商品售價，原因：" + e.toString());
		throw new Exception("查詢商品售價，原因：" + e.toString());
	    }

	}
	log.info("=======<getAJAXLineData/>============");
	return re;
    }

    /**
     * AJAX Line 輸入商品後 eChange 出其他欄位 免稅
     *
     * @param Properties
     * @throws Exception
     */
    public List<Properties> getAJAXT2LineData(Properties httpRequest) throws Exception {

	log.info("=======<getAJAXT2LineData>============");
	List re = new ArrayList();
	String brandCode = httpRequest.getProperty("brandCode");
	String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	String itemCode = httpRequest.getProperty("itemCode");
	String pickerPriceId = httpRequest.getProperty("priceId");
	String priceType = httpRequest.getProperty("priceType");

	log.info("brandCode=" + brandCode);
	log.info("orderTypeCode=" + orderTypeCode);
	log.info("itemCode=" + itemCode);
	log.info("pickerPriceId=" + pickerPriceId);
	log.info("priceType=" + priceType);

	if (StringUtils.hasText(brandCode) && StringUtils.hasText(orderTypeCode) && ( StringUtils.hasText(pickerPriceId) || StringUtils.hasText(itemCode)  ) ) {
	    Properties pro = new Properties();
	    String itemCName = null;
	    String unitPrice = null;
	    try {
//		Object result = getT2ItemInfo(httpRequest);

		if(PAP.equals(orderTypeCode)){
		    ImItemPriceView imItemPriceView = imItemPriceViewDAO.findOneItemPriceView(brandCode, priceType, itemCode);

		    if(imItemPriceView != null){
			String category02 = AjaxUtils.getPropertiesValue( imItemPriceView.getCategory02(), "" );
			itemCode = AjaxUtils.getPropertiesValue( imItemPriceView.getItemCode(), "");
			itemCName = AjaxUtils.getPropertiesValue( imItemPriceView.getItemCName(), "");
			unitPrice = AjaxUtils.getPropertiesValue( imItemPriceView.getUnitPrice(), "0");
			String foreignCost = AjaxUtils.getPropertiesValue( imItemPriceView.getPurchaseAmount(), "0");
			String currencyCode = AjaxUtils.getPropertiesValue( imItemPriceView.getPurchaseCurrencyCode(), "");

			// 依幣別品牌的組織取得匯率
			String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
			String exchangeRate = buBasicDataService.getExchangeRateByCurrencyCode(organizationCode,currencyCode);
			log.info("currencyCode=" + currencyCode);
			log.info("exchangeRate=" + exchangeRate);
			String localCost = String.valueOf(Double.valueOf(foreignCost) * Double.valueOf(exchangeRate));
			// 計算毛利率
			String grossRate = calGrossRate(localCost,unitPrice);

			String priceId = AjaxUtils.getPropertiesValue( imItemPriceView.getPriceId(), "" );

			pro.setProperty("currencyCode",currencyCode);
			pro.setProperty("exchangeRate",exchangeRate);
			pro.setProperty("foreignCost", foreignCost);
			pro.setProperty("localCost", localCost);
			pro.setProperty("grossRate", grossRate);
			pro.setProperty("priceId", priceId);

		    }else{
			itemCName = MessageStatus.DATA_NOT_FOUND;
			unitPrice = "0";

			pro.setProperty("currencyCode","");
			pro.setProperty("exchangeRate","1");
			pro.setProperty("category02","");
			pro.setProperty("foreignCost", "0");
			pro.setProperty("localCost", "0");
			pro.setProperty("grossRate", "0");
			pro.setProperty("priceId", "");
		    }

//		    itemCName = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[1] : null,MessageStatus.DATA_NOT_FOUND);
//		    String priceId = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[2] : null,"" );
//		    String category02 = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[3] : null, "" );
//		    String unitPrice = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[6] : null , "0");
//		    String foreignCost = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[4] : null, "0");
//		    String currencyCode = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[7] : null,"0");

		}else if(PAJ.equals(orderTypeCode)) {
		    ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findOneCurrentPriceByProperty(brandCode, priceType, itemCode);

		    if(imItemCurrentPriceView != null){

			// 中類
			String category02 = imItemCurrentPriceView.getCategory02();
			ImItemCategory itemCategoryPO = imItemCategoryService.findById(brandCode, ImItemCategoryDAO.CATEGORY02, imItemCurrentPriceView.getCategory02());
			String category02Name = null;
			if( null == itemCategoryPO ){
			    category02Name = MessageStatus.DATA_NOT_FOUND;
			}else{
			    category02Name = itemCategoryPO.getCategoryName(); // 中類名稱
			}


			String currencyCode = AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getPurchaseCurrencyCode(), "");
			// 依幣別撈匯率
			String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
			String exchangeRate = buBasicDataService.getExchangeRateByCurrencyCode(organizationCode,currencyCode);

			itemCode = AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getItemCode(), "");
			itemCName = AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getItemCName(), "");
			String originalCurrencyCode = AjaxUtils.getPropertiesValue( currencyCode, "");
			String originalForeignCost = AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getPurchaseAmount(), "0");
			String originalPrice = AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getUnitPrice(), "0");
			unitPrice = originalPrice;

			log.info( "itemCode = " + itemCode);
			log.info( "category02 = " + category02 );
			log.info( "category02Name = " + category02Name );
			log.info( "originalCurrencyCode = " + originalCurrencyCode);
			log.info( "originalForeignCost = " + originalForeignCost);
			log.info( "originalPrice =" + originalPrice );
			log.info( "currencyCode=" + currencyCode);
			log.info( "originalExchangeRate =" + exchangeRate );
			log.info( "exchangeRate = " + exchangeRate);

			pro.setProperty("category02", category02);
			pro.setProperty("category02Name", category02Name);
			pro.setProperty("originalCurrencyCode",currencyCode);
			pro.setProperty("currencyCode",currencyCode);
			pro.setProperty("originalExchangeRate",exchangeRate);
			pro.setProperty("exchangeRate",exchangeRate);

			pro.setProperty("originalForeignCost",originalForeignCost);
			pro.setProperty("foreignCost",originalForeignCost);

			pro.setProperty("orginalPrice",originalPrice);

			// 計算毛利率(舊)和 毛利率(新)
			String localCost = String.valueOf(Double.valueOf(originalForeignCost) * Double.valueOf(exchangeRate));

			String grossRate = calGrossRate(localCost,unitPrice);

			log.info("localCost=" + localCost);
			log.info("grossRate=" + grossRate);

			pro.setProperty("originalGrossRate", grossRate);
			pro.setProperty("grossRate", grossRate);

			//撈出總庫存數量
			Double totalStock = imOnHandDAO.getCurrentStockOnHandQuantity(organizationCode, brandCode,itemCode);
			pro.setProperty("totalStock", AjaxUtils.getPropertiesValue( totalStock, "0") );

			pro.setProperty("priceId", AjaxUtils.getPropertiesValue( String.valueOf(imItemCurrentPriceView.getPriceId()), "") );
			pro.setProperty("isTax", AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getIsTax(), "") );
			pro.setProperty("typeCode", AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getTypeCode(), "") );
			pro.setProperty("taxCode", AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getTypeCode(), "") );
		    }else{
			itemCName = MessageStatus.DATA_NOT_FOUND;
			unitPrice = "0";
			pro.setProperty("originalCurrencyCode","");
			pro.setProperty("currencyCode","");
			pro.setProperty("originalExchangeRate","1");
			pro.setProperty("exchangeRate","1");

			pro.setProperty("originalForeignCost","0");
			pro.setProperty("foreignCost","0");
			pro.setProperty("orginalPrice","0");
			pro.setProperty("originalGrossRate", "0");
			pro.setProperty("grossRate", "0");
			pro.setProperty("totalStock", "0");
			pro.setProperty("priceId", "");
			pro.setProperty("isTax", "");
			pro.setProperty("typeCode", "");
			pro.setProperty("taxCode", "");
		    }
//		    log.info( "itemCName " + ((Object[])result)[0] );
//		    itemCName = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[0] : null,MessageStatus.DATA_NOT_FOUND);
//		    unitPrice = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[2] : null, "0");
//		    String purcharseAmount = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[1] : null,"0");
//		    String priceId = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[3] : null, "");
//		    String isTax = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[4] : null, "1");
//		    String typeCode = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[5] : null, "1");
//		    String taxCode = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[6] : null, "3");
//		    itemCode = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[7] : null, "");
//		    String currencyCode = AjaxUtils.getPropertiesValue( result != null ? ((Object[])result)[8] : null, "");

//		    pro.setProperty("originalCurrencyCode",currencyCode);
//		    pro.setProperty("currencyCode",currencyCode);

//		    pro.setProperty("originalForeignCost",purcharseAmount);
//		    pro.setProperty("foreignCost",purcharseAmount);

//		    pro.setProperty("orginalPrice",unitPrice);

//		    // 計算毛利率(舊)和 毛利率(新)
//		    String exchangeRate = poPurchaseOrderHeadService.getExchangeRateByCurrencyCode("TM",currencyCode);
//		    String localCost = String.valueOf(Double.valueOf(purcharseAmount) * Double.valueOf(exchangeRate));
//		    String grossRate = calGrossRate(localCost,unitPrice);

//		    log.info("currencyCode=" + currencyCode);
//		    log.info("exchangeRate=" + exchangeRate);
//		    log.info("localCost=" + localCost);
//		    log.info("grossRate=" + grossRate);

//		    pro.setProperty("originalGrossRate", grossRate);
//		    pro.setProperty("grossRate", grossRate);

//		    pro.setProperty("priceId", priceId);
//		    pro.setProperty("isTax", isTax);
//		    pro.setProperty("typeCode", typeCode);
//		    pro.setProperty("taxCode", taxCode);
		}
		log.info("itemCName=" + itemCName);
		pro.setProperty("itemCode", itemCode);
		pro.setProperty("itemCName", itemCName);
		pro.setProperty("unitPrice", unitPrice);


		re.add(pro);
	    } catch (Exception e) {
		e.printStackTrace();
		log.error("查詢商品售價，原因：" + e.toString());
		throw new Exception("查詢商品售價，原因：" + e.toString());
	    }

	}
	log.info("=======<getAJAXT2LineData/>============");
	return re;
    }

    /**
     * 取得品號名稱
     */
    public Object getItemCode(String orderTypeCode, String brandCode, String itemCode, String priceType){
	List result = new ArrayList();
	StringBuffer nativeSQL = new StringBuffer();

	if(PAP.equals(orderTypeCode)){
	    nativeSQL.append("SELECT NVL(ITEM_C_NAME,' ') AS ITEM_C_NAME, CATEGORY13, PRICE_ID FROM IM_ITEM_PRICE_VIEW ")
	    .append( "WHERE BRAND_CODE ='").append(brandCode).append("'")
	    .append(" AND TYPE_CODE ='").append(priceType).append("'")
//	    .append("' AND PRICE_ENABLE='N' ")
	    .append(" AND ITEM_ENABLE='Y'");
	}else if(PAJ.equals(orderTypeCode)){
	    nativeSQL.append("SELECT NVL(ITEM_C_NAME,' ') AS ITEM_C_NAME FROM IM_ITEM_CURRENT_PRICE_VIEW WHERE BRAND_CODE ='").append(brandCode)
	    .append("' AND TYPE_CODE ='").append(priceType).append("'");
	}

	if( StringUtils.hasText(itemCode) ){
	    nativeSQL.append(" AND ITEM_CODE ='").append(itemCode).append("'");
	}

	log.info("nativeSQL = " + nativeSQL.toString());
	result = nativeQueryDAO.executeNativeSql(nativeSQL.toString());

	return result != null && result.size() > 0 ? result.get(0) : null;
    }

    /**
     * 取得品號名稱 for T2
     */
    public Object getT2ItemCode(String orderTypeCode, String brandCode, String itemCode, String priceType){
	List result = new ArrayList();
	StringBuffer nativeSQL = new StringBuffer();

	if(PAP.equals(orderTypeCode)){
	    nativeSQL.append("SELECT NVL(ITEM_C_NAME,' ') AS ITEM_C_NAME, CATEGORY02 FROM IM_ITEM_PRICE_VIEW ")
	    .append( "WHERE BRAND_CODE ='").append(brandCode)
	    .append("' AND TYPE_CODE ='").append(priceType)
	    .append("' AND PRICE_ENABLE='N' AND ITEM_ENABLE='Y'");
	}else if(PAJ.equals(orderTypeCode)){
	    nativeSQL.append("SELECT NVL(ITEM_C_NAME,' ') AS ITEM_C_NAME FROM IM_ITEM_CURRENT_PRICE_VIEW WHERE BRAND_CODE ='").append(brandCode)
	    .append("' AND TYPE_CODE ='").append(priceType).append("'");
	}

	if( StringUtils.hasText(itemCode) ){
	    nativeSQL.append(" AND ITEM_CODE ='").append(itemCode).append("'");
	}

	log.info("nativeSQL = " + nativeSQL.toString());
	result = nativeQueryDAO.executeNativeSql(nativeSQL.toString());

	return result != null && result.size() > 0 ? result.get(0) : null;
    }

    /**
     * 依據itemCode以及brandCode為查詢條件，取得廠商代號與商品幣別
     *
     * @param currencyCode
     * @return ExchangeRate
     * @throws Exception
     */
    public List<Properties> getSupplierCode(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try{
	    String brandCode = httpRequest.getProperty("brandCode");
	    String itemCode = httpRequest.getProperty("itemCode");
	    log.info("brandCode = " + brandCode);
	    log.info("itemCode = " + itemCode);
	    ImItem item = null;
	    if(StringUtils.hasText(itemCode)){
		item = imItemService.findItem(brandCode, itemCode);
		if(null != item){
		    properties.setProperty("supplierCode", item.getCategory17());
		}
	    }
	    result.add(properties);
	    return result;
	}catch (Exception ex) {
	    log.error("查詢商品供應商時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢商品供應商時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * ajax 取得商品售價search分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXLineSearchPageData(Properties httpRequest) throws Exception{

	List views = null;
	Map viewMap = null;
	Long maxIndex = null;
	String[] gridNames = null;
	String[] gridDefaultTypes = null;
	try{
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    //======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");  // 區別 table用

	    String startItemCode = httpRequest.getProperty("startItemCode" );
//	    String endItemCode = httpRequest.getProperty("endItemCode");
	    String itemName = httpRequest.getProperty("itemName");
	    String priceType = httpRequest.getProperty("priceType");
	    String priceStatus = httpRequest.getProperty("priceStatus");
	    String supplierCode = httpRequest.getProperty("supplierCode");

	    Map map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and model.brandCode = :brandCode",brandCode);

	    if(StringUtils.hasText(startItemCode)){
		startItemCode = startItemCode.trim().toUpperCase();
	    }

	    findObjs.put(" and model.itemCode like :startItemCode","%"+startItemCode+"%");

	    findObjs.put(" and model.itemCName like :itemName","%"+itemName+"%");
	    findObjs.put(" and model.typeCode = :priceType",priceType);

	    if(brandCode.indexOf("T2") > -1){
		String itemBrand = httpRequest.getProperty("itemBrand");
		String category01 = httpRequest.getProperty("category01");
		String category02 = httpRequest.getProperty("category02");
		String itemCategory = httpRequest.getProperty("itemCategory");

		findObjs.put(" and model.itemBrand like :itemBrand","%"+itemBrand+"%");
		findObjs.put(" and model.category01 = :category01",category01);
		findObjs.put(" and model.category02 = :category02",category02);
		findObjs.put(" and model.itemCategory = :itemCategory",itemCategory);
		findObjs.put(" and model.category17 = :supplierCode", supplierCode);
	    }

	    if(PAP.equals(orderTypeCode)){
		findObjs.put(" and model.priceEnable = :priceStatus",priceStatus);
		findObjs.put(" and model.itemEnable = :itemEnable","Y");

	    }
	    //==============================================================

	    if(PAP.equals(orderTypeCode)){
		viewMap = imItemPriceViewDAO.search( "ImItemPriceView as model", findObjs, "order by priceId", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
		views = (List<ImItemPriceView>) viewMap.get(BaseDAO.TABLE_LIST);
	    }else if(PAJ.equals(orderTypeCode)){
		viewMap = imItemCurrentPriceViewDAO.search( "ImItemCurrentPriceView as model", findObjs, "order by priceId", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
		views = (List<ImItemCurrentPriceView>) viewMap.get(BaseDAO.TABLE_LIST);
	    }

	    if(brandCode.indexOf("T2") > -1){
		if(PAP.equals(orderTypeCode)){
		    gridNames = T2_GRID_LINE_PAP_SEARCH_FIELD_NAMES;
		    gridDefaultTypes = T2_GRID_LINE_PAP_SEARCH_FIELD_DEFAULT_VALUES;
		}else if(PAJ.equals(orderTypeCode)){
		    gridNames = T2_GRID_LINE_PAJ_SEARCH_FIELD_NAMES;
		    gridDefaultTypes = T2_GRID_LINE_PAJ_SEARCH_FIELD_DEFAULT_VALUES;
		}
	    }else{
		if(PAP.equals(orderTypeCode)){
		    gridNames = GRID_LINE_PAP_SEARCH_FIELD_NAMES;
		    gridDefaultTypes = GRID_LINE_PAP_SEARCH_FIELD_DEFAULT_VALUES;
		}else if(PAJ.equals(orderTypeCode)){
		    gridNames = GRID_LINE_PAJ_SEARCH_FIELD_NAMES;
		    gridDefaultTypes = GRID_LINE_PAJ_SEARCH_FIELD_DEFAULT_VALUES;
		}

	    }

	    if (views != null && views.size() > 0) {

		// 設定其他欄位
		setOtherImItemPriceView(orderTypeCode, views);

		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX

		if(PAP.equals(orderTypeCode)){
		    maxIndex = (Long)imItemPriceViewDAO.search("ImItemPriceView as model", "count(model.priceId) as rowCount" ,findObjs, "order by priceId", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
//		    result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_LINE_PAP_SEARCH_FIELD_NAMES, GRID_LINE_PAP_SEARCH_FIELD_DEFAULT_VALUES,views, gridDatas, firstIndex, maxIndex));
		}else if(PAJ.equals(orderTypeCode)){
		    maxIndex = (Long)imItemCurrentPriceViewDAO.search("ImItemCurrentPriceView as model", "count(model.priceId) as rowCount" ,findObjs, "order by priceId", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
//		    result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_LINE_PAJ_SEARCH_FIELD_NAMES, GRID_LINE_PAJ_SEARCH_FIELD_DEFAULT_VALUES,views, gridDatas, firstIndex, maxIndex));
		}
		result.add(AjaxUtils.getAJAXPageData(httpRequest,gridNames, gridDefaultTypes,views, gridDatas, firstIndex, maxIndex));

	    }else {
		if(PAP.equals(orderTypeCode)){
//		    result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_LINE_PAP_SEARCH_FIELD_NAMES, GRID_LINE_PAP_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
		}else if(PAJ.equals(orderTypeCode)){
//		    result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_LINE_PAJ_SEARCH_FIELD_NAMES, GRID_LINE_PAP_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
		}
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,gridNames, gridDefaultTypes, map,gridDatas));
	    }

	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的定變價查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的定變價查詢失敗！");
	}
    }

    public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{

	try{
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    //======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
	    String startOrderNo = httpRequest.getProperty("startOrderNo");
	    String endOrderNo = httpRequest.getProperty("endOrderNo");

	    String startDate = httpRequest.getProperty("startDate");
	    String endDate = httpRequest.getProperty("endDate");
	    String employeeCode = httpRequest.getProperty("employeeCode");
	    String supplierCode = httpRequest.getProperty("supplierCode");
	    String status = httpRequest.getProperty("status");
	    
	    
	    String itemCode = httpRequest.getProperty("itemCode");
	    log.info("employeeCode = " + employeeCode );


	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put("brandCode",brandCode);
	    findObjs.put("orderTypeCode",orderTypeCode) ;
	    findObjs.put("startOrderNo",startOrderNo) ;
	    findObjs.put("endOrderNo",endOrderNo) ;
	    findObjs.put("startDate",startDate) ;
	    findObjs.put("endDate",endDate) ;
	    findObjs.put("employeeCode",employeeCode) ;
	    findObjs.put("supplierCode",supplierCode) ;
	    findObjs.put("status",status) ;
	    findObjs.put("itemCode",itemCode) ;

	    HashMap tmpResultMap = imPriceAdjustmentDAO.findPageLine(findObjs, iSPage, iPSize);
	    List<ImPriceAdjustment> imPriceAdjustments = null;
	    if(!tmpResultMap.isEmpty()){
		imPriceAdjustments = (List<ImPriceAdjustment>)tmpResultMap.get("form");
	    }

	    if (imPriceAdjustments != null && imPriceAdjustments.size() > 0) {
		Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
		Long maxIndex = (Long)imPriceAdjustmentDAO.findPageLine(findObjs,-1,iPSize).get("recordCount");	// 取得最後一筆 INDEX

		for(ImPriceAdjustment imPriceAdjustment: imPriceAdjustments){
		    imPriceAdjustment.setStatus(OrderStatus.getChineseWord(imPriceAdjustment.getStatus()));
		    imPriceAdjustment.setPriceType( buCommonPhraseLineDAO.findOneBuCommonPhraseLine( "PriceType",imPriceAdjustment.getPriceType(), "Y" ).getName());

		    BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewDAO.findById(imPriceAdjustment.getSupplierCode(), brandCode);
		    if( null != buSupplierWithAddressView ){
			imPriceAdjustment.setSupplierName(buSupplierWithAddressView.getChineseName());
		    }
		}
		log.info("ImPriceAdjustment.AjaxUtils.getAJAXPageData ");
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,imPriceAdjustments, gridDatas, firstIndex, maxIndex));
	    } else {
		log.info("ImPriceAdjustment.AjaxUtils.getAJAXPageDataDefault ");
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));

	    }

	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的定變價查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的定變價查詢失敗！");
	}
    }

    /**
     * AJAX Load Page Data
     *
     * @param headObj
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public List<Properties> getAJAXPageData(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
    NoSuchMethodException ,Exception{
	log.info("=====<getAJAXPageData>=====");
	try{

	    // 要顯示的HEAD_ID
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	    String priceType = httpRequest.getProperty("priceType");
	    List<Properties> re = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);
	    int iPSize = AjaxUtils.getPageSize(httpRequest);

	    log.info(" headId=" + headId );

	    ImPriceAdjustment imPriceAdjustment = imPriceAdjustmentDAO.findById(headId);
	    String brandCode = imPriceAdjustment == null ? "" : imPriceAdjustment.getBrandCode();
	    String status = imPriceAdjustment.getStatus();

	    List<ImPriceList> imPriceLists =  imPriceListService.findPageLine(headId, iSPage, iPSize);
	    if (null != imPriceLists && imPriceLists.size() > 0) {
		log.info("imPriceLists.size() = " + imPriceLists.size());

		for(Iterator iter = imPriceLists.iterator();iter.hasNext();){
		    ImPriceList imPriceObj = (ImPriceList)iter.next();
		    String itemCName = null;
		    // 取得商品名稱
		    Object view = getItemCode(orderTypeCode, brandCode, imPriceObj.getItemCode(), priceType);

		    log.info(" view before " );
		    if(PAP.equals(orderTypeCode)){
//				itemCName = !OrderStatus.FINISH.equals(status) ? AjaxUtils.getPropertiesValue( view != null ? ((Object[])view)[0] : null,MessageStatus.DATA_NOT_FOUND): ((String)view);
				itemCName =  AjaxUtils.getPropertiesValue( view != null ? ((Object[])view)[0] : null,MessageStatus.DATA_NOT_FOUND);
		    	imPriceObj.setCategory01(AjaxUtils.getPropertiesValue( view != null ? ((Object[])view)[1] : null ,"" ));

			// 避免後續才打商品主檔的標準售價
			if(null == imPriceObj.getPriceId() || 0l == imPriceObj.getPriceId() ){
			    imPriceObj.setPriceId(NumberUtils.getLong( AjaxUtils.getPropertiesValue(view != null ? ((Object[])view)[2] : null,"")));
			}

		    }else if(PAJ.equals(orderTypeCode)){
			itemCName = !OrderStatus.FINISH.equals(status) ? AjaxUtils.getPropertiesValue( view != null ? ((String)view) : null,MessageStatus.DATA_NOT_FOUND): ((String)view);
		    }
		    imPriceObj.setItemCName(itemCName);
		    log.info(" view after " );
		}
		log.info(" for end " );
		// 取得第一筆的INDEX
		Long firstIndex = imPriceLists.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		log.info(" firstIndex =  " + firstIndex);
		Long maxIndex = imPriceListService.findPageLineMaxIndex(Long.valueOf(headId));
		log.info(" maxIndex =  " + maxIndex);
		if(PAP.equals(orderTypeCode)){
		    re.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, imPriceLists, gridDatas,
			    firstIndex, maxIndex));
		}else if(PAJ.equals(orderTypeCode)){
		    re.add(AjaxUtils.getAJAXPageData(httpRequest, PAJ_GRID_FIELD_NAMES, PAJ_GRID_FIELD_DEFAULT_VALUES, imPriceLists, gridDatas,
			    firstIndex, maxIndex));
		}
		log.info(" bind end ");

	    } else {
		if(PAP.equals(orderTypeCode)){
		    re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));
		}else if(PAJ.equals(orderTypeCode)){
		    re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, PAJ_GRID_FIELD_NAMES, PAJ_GRID_FIELD_DEFAULT_VALUES, gridDatas));
		}
		log.info(" bind initial end ");
	    }
	    log.info("=====<getAJAXPageData/>=====");
	    return re;

	} catch (Exception ex) {
	    log.error("載入頁面顯示的訂變價單明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的訂變價單明細失敗！");
	}
    }

    /**
     * AJAX Load Page Data for T2
     *
     * @param headObj
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public List<Properties> getAJAXT2PageData(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
    NoSuchMethodException ,Exception{
	log.info("=====<getAJAXT2PageData>=====");
	try{

	    // 要顯示的HEAD_ID
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	    String priceType = httpRequest.getProperty("priceType");
	    List<Properties> re = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);
	    int iPSize = AjaxUtils.getPageSize(httpRequest);

	    log.info(" headId=" + headId );

//	    ImPriceAdjustment imPriceAdjustment = imPriceAdjustmentDAO.findById(headId);
//	    String brandCode = imPriceAdjustment == null ? "" : imPriceAdjustment.getBrandCode();
//	    String status = imPriceAdjustment.getStatus();

	    List<ImPriceList> imPriceLists =  imPriceListService.findPageLine(headId, iSPage, iPSize);
	    if (null != imPriceLists && imPriceLists.size() > 0) {
		ImPriceAdjustment imPriceAdjustment = imPriceLists.get(0).getImPriceAdjustment();
		String brandCode = imPriceAdjustment.getBrandCode();
		String status = imPriceAdjustment.getStatus();

		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);

		log.info("imPriceLists.size() = " + imPriceLists.size());

		for(Iterator iter = imPriceLists.iterator();iter.hasNext();){
		    ImPriceList imPriceObj = (ImPriceList)iter.next();
		    String itemCName = null;

		    // 取得商品名稱
//		    Object view = getT2ItemCode(orderTypeCode, brandCode, imPriceObj.getItemCode(), priceType);

		    log.info(" view before " );

		    if(PAP.equals(orderTypeCode)){
			log.info( "LocalCost = " + imPriceObj.getLocalCost());
			log.info( "UnitPrice = " + imPriceObj.getUnitPrice());
//			String foreignCost = AjaxUtils.getPropertiesValue( imPriceObj.getForeignCost(), "0"); // 原幣成本
			String localCost = AjaxUtils.getPropertiesValue( imPriceObj.getLocalCost(), "0"); // 台幣成本
			String unitPrice = AjaxUtils.getPropertiesValue( imPriceObj.getUnitPrice(), "0"); // 標準售價
//			String currencyCode = imPriceObj.getCurrencyCode();

			ImItemPriceView imItemPriceView = imItemPriceViewDAO.findOneItemPriceView(brandCode, priceType, imPriceObj.getItemCode());

			if( imItemPriceView != null ){
			    itemCName = AjaxUtils.getPropertiesValue( imItemPriceView.getItemCName() ,"" );
			    imPriceObj.setCategory02(AjaxUtils.getPropertiesValue( imItemPriceView.getCategory02() ,"" ));

			    // 避免後續才打商品主檔的標準售價
			    if(null == imPriceObj.getPriceId() || 0l == imPriceObj.getPriceId() ){
				imPriceObj.setPriceId(imItemPriceView.getPriceId());
			    }
			}else{
			    if(!OrderStatus.FINISH.equals(status)){
				itemCName = MessageStatus.DATA_NOT_FOUND;
			    }else{
				ImItemPriceView imItemPriceView2 = imItemPriceViewDAO.findOneItemPriceView(brandCode, priceType, imPriceObj.getItemCode(),"Y","Y");
				if(imItemPriceView2 != null){
				    itemCName = imItemPriceView2.getItemCName();
				    imItemPriceView2.setCategory02(AjaxUtils.getPropertiesValue( imItemPriceView2.getCategory02() ,"" ));
				}
			    }
			}
//			itemCName = !OrderStatus.FINISH.equals(status) ? AjaxUtils.getPropertiesValue( view != null ? ((Object[])view)[0] : null,MessageStatus.DATA_NOT_FOUND): ((String)view);
//			imPriceObj.setCategory02(AjaxUtils.getPropertiesValue( view != null ? ((Object[])view)[1] : null ,"" ));

			// 算出毛利率
			String grossRate = calGrossRate( localCost,  unitPrice  );
			imPriceObj.setGrossRate( NumberUtils.getDouble(grossRate) );

		    }else if(PAJ.equals(orderTypeCode)){
			ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findOneCurrentPriceByProperty(brandCode, priceType, imPriceObj.getItemCode() );

			if( imItemCurrentPriceView != null ){
			    itemCName = AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getItemCName(),"");

			    // 中類
			    imPriceObj.setCategory02(imItemCurrentPriceView.getCategory02());
			    ImItemCategory itemCategoryPO = imItemCategoryService.findById(brandCode, ImItemCategoryDAO.CATEGORY02, imItemCurrentPriceView.getCategory02());
			    if( null == itemCategoryPO ){
				imPriceObj.setCategory02Name( MessageStatus.DATA_NOT_FOUND );
			    }else{
				imPriceObj.setCategory02Name(itemCategoryPO.getCategoryName()); // 中類名稱
			    }

			    // 算毛利率(舊)
			    Double originalExchangeRate = imPriceObj.getOriginalExchangeRate();
			    log.info(" originalExchangeRate before = " + originalExchangeRate );
			    if(null == originalExchangeRate){
				String originalCurrencyCode = imPriceObj.getOriginalCurrencyCode();
				originalExchangeRate = NumberUtils.getDouble(buBasicDataService.getExchangeRateByCurrencyCode(organizationCode,originalCurrencyCode));
				log.info(" 匯率(舊) 曾經為 null, 修正為 = " + originalExchangeRate );
			    }
			    log.info(" originalExchangeRate after = " + originalExchangeRate );

			    Double originalForeignCost = imPriceObj.getOriginalForeignCost(); // 原幣成本(舊)
			    String originalPrice = imPriceObj.getOriginalPrice() != null ? imPriceObj.getOriginalPrice().toString() : "0"; // 零售價(舊)
			    // 算出毛利率(舊) = (售價 - 成本*匯率)/售價
			    Double originalGrossRate = NumberUtils.getDouble (calGrossRate(String.valueOf(originalForeignCost * originalExchangeRate), originalPrice) );
			    log.info(" 算毛利率(舊)" );
			    // 算毛利率(新)
			    //String currencyCode = imPriceObj.getCurrencyCode();
			    Double exchangeRate = NumberUtils.getDouble(imPriceObj.getExchangeRate()); //NumberUtils.getDouble(buBasicDataService.getExchangeRateByCurrencyCode(organizationCode,currencyCode));
			    Double foreignCost = NumberUtils.getDouble(imPriceObj.getForeignCost()); // 原幣成本(新)
			    String unitPrice = imPriceObj.getUnitPrice() != null ? imPriceObj.getUnitPrice().toString() : "0"; // 零售價(新)
			    // 算出毛利率(新) = (售價 - 成本*匯率)/售價
			    Double grossRate = NumberUtils.getDouble (calGrossRate(String.valueOf(foreignCost * exchangeRate), unitPrice) );
			    log.info(" 算毛利率(新)" );
			    imPriceObj.setOriginalGrossRate(originalGrossRate);
			    imPriceObj.setGrossRate(grossRate);
			    // 總庫存
			    Double totalStock = imOnHandDAO.getCurrentStockOnHandQuantity(organizationCode, brandCode,imItemCurrentPriceView.getItemCode());
			    imPriceObj.setTotalStock( NumberUtils.getDouble(totalStock) );

			}else{
			    itemCName = MessageStatus.DATA_NOT_FOUND;
			}

		    }
		    imPriceObj.setItemCName(itemCName);
		    log.info(" view after " );
		}
		log.info(" for end " );
		// 取得第一筆的INDEX
		Long firstIndex = imPriceLists.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		log.info(" firstIndex =  " + firstIndex);
		Long maxIndex = imPriceListService.findPageLineMaxIndex(Long.valueOf(headId));
		log.info(" maxIndex =  " + maxIndex);
		if(PAP.equals(orderTypeCode)){
		    re.add(AjaxUtils.getAJAXPageData(httpRequest, T2_PAP_GRID_FIELD_NAMES, T2_PAP_GRID_FIELD_DEFAULT_VALUES, imPriceLists, gridDatas,
			    firstIndex, maxIndex));
		}else if(PAJ.equals(orderTypeCode)){
		    re.add(AjaxUtils.getAJAXPageData(httpRequest, T2_PAJ_GRID_FIELD_NAMES, T2_PAJ_GRID_FIELD_DEFAULT_VALUES, imPriceLists, gridDatas,
			    firstIndex, maxIndex));
		}
		log.info(" bind end ");

	    } else {
		if(PAP.equals(orderTypeCode)){
		    re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, T2_PAP_GRID_FIELD_NAMES, T2_PAP_GRID_FIELD_DEFAULT_VALUES, gridDatas));
		}else if(PAJ.equals(orderTypeCode)){
		    re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, T2_PAJ_GRID_FIELD_NAMES, T2_PAJ_GRID_FIELD_DEFAULT_VALUES, gridDatas));
		}
		log.info(" bind initial end ");
	    }
	    log.info("=====<getAJAXT2PageData/>=====");
	    return re;

	} catch (Exception ex) {
	    log.error("載入頁面顯示的訂變價單明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的訂變價單明細失敗！");
	}
    }

    /**
     * AJAX Line calc costRate
     * @param Properties
     * @throws Excepiton
     */
    public List<Properties> getAJAXGrossProfitRate(Properties httpRequest)throws FormException{
	log.info("ImPriceAdjustmentMainService.getAJAXGrossProfitRate");
	List re = new ArrayList();
	String originalPrice = httpRequest.getProperty("originalPrice");
	String originalQuotationPrice = httpRequest.getProperty("originalQuotationPrice");
	String exchangeRate = httpRequest.getProperty("exchangeRate");
	String newQuotationPrice = httpRequest.getProperty("newQuotationPrice");
	String unitPrice = httpRequest.getProperty("unitPrice");
	String grossProfitRate = calGrossProfitRate(originalPrice,originalQuotationPrice,exchangeRate,unitPrice,newQuotationPrice);
	try{
	    Properties pro = new Properties();
	    pro.setProperty("GrossProfitRate", grossProfitRate);
	    re.add(pro);
	}catch(Exception e){
	    e.printStackTrace();
	}
	return re;
    }

    /**
     * AJAX 變價單Line 計算毛利率(新) for T2
     */
    public List<Properties> getAJAXGrossRateNew(Properties httpRequest)throws Exception{
	List re = new ArrayList();
	Properties pro = new Properties();
	try{
	    String brandCode = httpRequest.getProperty("brandCode");
	    String currencyCode = httpRequest.getProperty("currencyCode");
	    String exchangeRate = httpRequest.getProperty("exchangeRate");
	    String foreignCost = httpRequest.getProperty("foreignCost");
	    String unitPrice = httpRequest.getProperty("unitPrice");

	    if(StringUtils.hasText(currencyCode)){
		// 依幣別撈匯率
		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
		exchangeRate = buBasicDataService.getExchangeRateByCurrencyCode(organizationCode,currencyCode);
	    }

	    String localCost = String.valueOf(NumberUtils.getDouble(foreignCost) * NumberUtils.getDouble(exchangeRate));
	    localCost = AjaxUtils.getPropertiesValue( localCost,"0");

	    String resultRate = calGrossRate(localCost,unitPrice);
	    pro.setProperty("exchangeRate", exchangeRate);
	    pro.setProperty("grossRate", resultRate);
	    re.add(pro);
	}catch(Exception e){
	    log.error("計算毛利率(新)發生錯誤，原因：" + e.toString());
	    throw new Exception("計算毛利率(新)發生錯誤，原因：" + e.toString());
	}
	return re;
    }

    /**
     * AJAX Line calc costRate
     * @param Properties
     * @throws Excepiton
     */
    public List<Properties> getAJAXCalcCostRate(Properties httpRequest)throws FormException{
	log.info("ImPriceAdjustmentMainService.getAJAXLineData");
	List re = new ArrayList();
	String localCost = httpRequest.getProperty("localCost");
	String unitPrice = httpRequest.getProperty("unitPrice");
	String resultRate = calCostRate(localCost,unitPrice);
	try{
	    Properties pro = new Properties();
	    pro.setProperty("CostRate", resultRate);
	    re.add(pro);
	}catch(Exception e){
	    e.printStackTrace();
	}
	return re;
    }

    /**
     * AJAX Line 計算毛利率 for T2
     * @param Properties
     * @throws Excepiton
     */
    public List<Properties> getAJAXCalcGrossRate(Properties httpRequest)throws Exception{
	List re = new ArrayList();
	Properties pro = new Properties();
	try{
	    String brandCode = httpRequest.getProperty("brandCode");
	    String currencyCode = httpRequest.getProperty("currencyCode");
	    String exchangeRate = httpRequest.getProperty("exchangeRate");
	    String foreignCost = httpRequest.getProperty("foreignCost");
	    String localCost = httpRequest.getProperty("localCost");
	    String unitPrice = httpRequest.getProperty("unitPrice");

	    if(StringUtils.hasText(currencyCode)){
		// 依幣別撈匯率
		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
		exchangeRate = buBasicDataService.getExchangeRateByCurrencyCode(organizationCode,currencyCode);
	    }

	    if(!StringUtils.hasText(localCost)){ // 不存在代表重新算匯率*原幣成本
		localCost = NumberUtils.roundToStr(NumberUtils.getDouble(foreignCost) * NumberUtils.getDouble(exchangeRate), 4);
	    }

	    String resultRate = calGrossRate(localCost,unitPrice);

	    pro.setProperty("exchangeRate", exchangeRate);
	    pro.setProperty("localCost", localCost);
	    pro.setProperty("grossRate", resultRate);
	    re.add(pro);
	}catch(Exception e){
	    log.error("計算毛利率發生錯誤，原因：" + e.toString());
	    throw new Exception("計算毛利率發生錯誤，原因：" + e.toString());
	}
	return re;
    }


    public List<String> getAJAXLineJSONData(Map jMap) throws FormException{
	System.out.println(jMap);
	return new ArrayList();
    }

    /**處理AJAX參數
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getAJAXPurchaseEmployee(Properties httpRequest) throws Exception{
	log.info("getAJAXPurchaseEmployee");

	List<Properties> result = new ArrayList();
	Properties properties   = new Properties();
	String categoryType     = httpRequest.getProperty("categoryType");
	String version30        = httpRequest.getProperty("version30");

	List<BuEmployeeWithAddressView> purchaseAssist;
	List<BuEmployeeWithAddressView> purchaseMember;
	List<BuEmployeeWithAddressView> purchaseMaster;
	String assistCode = "";
	String memberCode = "";
	String masterCode = "";

	HashMap mapAssist = new HashMap();
	mapAssist.put("itemCategory",       categoryType);
	mapAssist.put("employeeDepartment", "506");
	mapAssist.put("employeePosition",   "A");

	HashMap mapMember = new HashMap();
	mapMember.put("itemCategory",       categoryType);
	mapMember.put("employeeDepartment", "506");
	mapMember.put("employeePosition",   "P");

	HashMap mapMaster = new HashMap();
	mapMaster.put("itemCategory",       categoryType);
	mapMaster.put("employeeDepartment", "506");
	mapMaster.put("employeePosition",   "M");

	try{
	    purchaseAssist = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege( mapAssist );
	    purchaseMember = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege( mapMember );
	    purchaseMaster = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege( mapMaster );

	    if(null==version30 || "N".equals(version30)) {	// for 2.5
		if(purchaseAssist != null && purchaseAssist.size() > 0){
		    List<String> tn = new ArrayList();
		    List<String> tv = new ArrayList();
		    for(int i = 0; i < purchaseAssist.size(); i++){
			BuEmployeeWithAddressView buEmployee = (BuEmployeeWithAddressView) purchaseAssist.get(i);
			tv.add(buEmployee.getEmployeeCode());
			tn.add(buEmployee.getEmployeeCode()+"-"+buEmployee.getChineseName() );
		    }
		    assistCode = AjaxUtils.getHtmlInput2DString(tn,tv);
		}
		if(purchaseMember != null && purchaseMember.size() > 0){
		    List<String> tn = new ArrayList();
		    List<String> tv = new ArrayList();
		    for(int i = 0; i < purchaseMember.size(); i++){
			BuEmployeeWithAddressView buEmployee = (BuEmployeeWithAddressView) purchaseMember.get(i);
			tv.add(buEmployee.getEmployeeCode());
			tn.add(buEmployee.getEmployeeCode()+"-"+buEmployee.getChineseName() );
		    }
		    memberCode = AjaxUtils.getHtmlInput2DString(tn,tv);
		}
		if(purchaseMaster != null && purchaseMaster.size() > 0){
		    List<String> tn = new ArrayList();
		    List<String> tv = new ArrayList();
		    for(int i = 0; i < purchaseMaster.size(); i++){
			BuEmployeeWithAddressView buEmployee = (BuEmployeeWithAddressView) purchaseMaster.get(i);
			tv.add(buEmployee.getEmployeeCode());
			tn.add(buEmployee.getEmployeeCode()+"-"+buEmployee.getChineseName() );
		    }
		    masterCode = AjaxUtils.getHtmlInput2DString(tn,tv);
		}
		properties.setProperty("purchaseAssist", assistCode);
		properties.setProperty("purchaseMember", memberCode);
		properties.setProperty("purchaseMaster", masterCode);

	    }else{	// for 3.0
		purchaseAssist = AjaxUtils.produceSelectorData(purchaseAssist, "employeeCode", "chineseName", true, true);
		purchaseMember = AjaxUtils.produceSelectorData(purchaseMember, "employeeCode", "chineseName", true, true);
		purchaseMaster = AjaxUtils.produceSelectorData(purchaseMaster, "employeeCode", "chineseName", true, true);
		properties.setProperty("purchaseAssist", AjaxUtils.parseSelectorData(purchaseAssist));
		properties.setProperty("purchaseMember", AjaxUtils.parseSelectorData(purchaseMember));
		properties.setProperty("purchaseMaster", AjaxUtils.parseSelectorData(purchaseMaster));
	    }
	    result.add(properties);
	    return result;
	}catch (Exception ex) {
	    log.error("查詢採購人員資料時發生錯誤，原因：" + ex.getMessage());
	    throw new Exception("查詢採購人員資料時發生錯誤失敗！");
	}
    }

    /**
     * sql 匯出excel
     * @param httpRequest
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws Exception
     */
	public SelectDataInfo getAJAXExportData(HttpServletRequest httpRequest) throws Exception {
		StringBuffer sql = new StringBuffer();
		Object[] object = null;
		List rowData = new ArrayList();
		try {
			String brandCode = httpRequest.getParameter("brandCode");
			String orderTypeCode = httpRequest.getParameter("orderTypeCode");
			String startOrderNo = httpRequest.getParameter("startOrderNo");
			String endOrderNo = httpRequest.getParameter("endOrderNo");
			String startDate = httpRequest.getParameter("startDate");
			// DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, httpRequest.getParameter("startDate"));
			String endDate = httpRequest.getParameter("endDate");
			// DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, httpRequest.getParameter("endDate"));
			String employeeCode = httpRequest.getParameter("employeeCode");
			String status = httpRequest.getParameter("status");

			log.info("brandCode = " + brandCode);
			log.info("orderTypeCode = " + orderTypeCode);
			log.info("startOrderNo = " + startOrderNo);
			log.info("endOrderNo = " + endOrderNo);
			log.info("startDate = " + startDate);
			log.info("endDate = " + endDate);
			log.info("employeeCode = " + employeeCode);
			log.info("status = " + status);

			sql.append(
					"SELECT ORDER_TYPE_CODE, ORDER_NO, DESCRIPTION, PRICE_TYPE, ENABLE_DATE, STATUS, SUPPLIER_CODE, '廠商名稱' ")
					.append("FROM IM_PRICE_ADJUSTMENT ").append("WHERE BRAND_CODE = '").append(brandCode).append("' ")
					.append("AND ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ")
					.append("AND SUBSTR(ORDER_NO,1,3) <> 'TMP' ");

			if (StringUtils.hasText(status)) {
				sql.append("AND STATUS = '").append(status).append("' ");
			}

			if (StringUtils.hasText(employeeCode)) {
				sql.append("AND CREATED_BY = '").append(employeeCode).append("' ");
			}

			if (StringUtils.hasText(startOrderNo)) {
				sql.append("AND ORDER_NO >= '").append(startOrderNo).append("' ");
			}
			if (StringUtils.hasText(endOrderNo)) {
				sql.append("AND ORDER_NO <= '").append(endOrderNo).append("' ");
			}

			if (null != startDate && !"".equals(startDate)) { // modified by Weichun 2012.03.13
				sql.append("AND ENABLE_DATE >= TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ");
			}
			if (null != endDate && !"".equals(endDate)) {
				sql.append("AND ENABLE_DATE <= TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
			}

			sql.append("ORDER BY LAST_UPDATE_DATE DESC ");

			log.info("sql = " + sql);

			List lists = nativeQueryDAO.executeNativeSql(sql.toString());

			object = new Object[] { "orderTypeCode", "orderNo", "description", "priceType", "enableDate", "status",
					"supplierCode", "supplierName" };

			log.info(" lists.size() = " + lists.size());
			for (int i = 0; i < (lists.size() > 65535 ? 65535 : lists.size()); i++) {
				Object[] getObj = (Object[]) lists.get(i);
				Object[] dataObject = new Object[object.length];
				for (int j = 0; j < object.length; j++) {
					if (j == 3) { // 價格類型
						String priceType = String.valueOf(getObj[3]);
						dataObject[j] = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("PriceType", priceType, "Y")
								.getName();
					} else if (j == 5) { // 狀態
						String statusCode = String.valueOf(getObj[5]);
						dataObject[j] = OrderStatus.getChineseWord(statusCode);
					} else if (j == 7) { // 廠商名稱
						String supplierCode = String.valueOf(getObj[6]);
						BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewDAO.findById(
								supplierCode, brandCode);
						if (null != buSupplierWithAddressView) {
							dataObject[j] = buSupplierWithAddressView.getChineseName();
						}
					} else {
						dataObject[j] = getObj[j];
					}
				}
				rowData.add(dataObject);
			}

			return new SelectDataInfo(object, rowData);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("匯出excel發生錯誤，原因：" + e.toString());
			throw new Exception("匯出excel發生錯誤，原因：" + e.getMessage());
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
	public void getMailList(ImPriceAdjustment head, String subject, String templateFileName, Map display, List mailAddress,
			List attachFiles, Map cidMap) throws Exception {
    log.info("進入發信功能:"+head.getOrderTypeCode()+" "+head.getOrderNo());//+head.getOrderTypeCode()+" "+head.getOrderNo()
		// #1 取得寄件者與其他相關配置檔
		// #2 取得CC報表的URL
		// #3 用URL轉成PDF再轉jpg
		// #4 用URL轉成xls
		// #5 寄信
		try {
			String reportFileName = null;
			String brandCode = head.getBrandCode();
			String orderTypeCode = head.getOrderTypeCode();
			log.info("進入發信功能1:"+orderTypeCode);
			String itemCategory = AjaxUtils.getPropertiesValue(head.getItemCategory(), "");
			log.info("進入發信功能2:"+itemCategory);
			String subjectError = null;
			String description = null;
			StringBuffer emailError = new StringBuffer();
			
			// 取得業種子類名稱
			String categoryName = null;
			ImItemCategory imItemCategory = imItemCategoryService.findById(brandCode, ImItemCategoryDAO.ITEM_CATEGORY,
					itemCategory);
			log.info("進入發信功能3");
			if (null == imItemCategory) {
				categoryName = "";
			} else {
				categoryName = imItemCategory.getCategoryName().concat("-");
			}
			 log.info("進入發信功能4");
			// #1  取得配置檔得到 寄信的報表與
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("MailList"
					+ orderTypeCode, itemCategory); // +orderTypeCode itemCategory
			
			log.info("aaa:"+orderTypeCode+" bbb:"+itemCategory);
			if (null == buCommonPhraseLine) {
				// 寄給故障維謢人員
				mailAddress.add("Developer@tasameng.com.tw");
				reportFileName = "SO0752_1.rpt";
				subjectError = "MailList" + orderTypeCode + itemCategory + "無配置檔異常 ";
				description = "";
			} else {
				reportFileName = buCommonPhraseLine.getAttribute1();
				// 1.該單起單人、採助、採購人、採購主管 從 BU_EMPLOYEE 取得
				// String domain = "@tasameng.com.tw";
				String createdBy = head.getCreatedBy();
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
						} else {
							//emailError.append("查起單人工號：").append(createdBy).append(" 無信箱設定，請聯絡資訊部人員與通知此人調價通知<br>");
						}
					} else {
						//emailError.append("查起單人無工號：").append(createdBy).append(" ，請聯絡資訊部人員與通知此人調價通知<br>");
					}
				}

				if (StringUtils.hasText(purchaseAssist)) { // 採購助理
					BuEmployee buEmployee = (BuEmployee) buEmployeeDAO.findByPrimaryKey(BuEmployee.class, purchaseAssist);
					if (null != buEmployee) {
						String EMailCompany = buEmployee.getEMailCompany();
						if (null != EMailCompany) {
							if (!mailAddress.contains(EMailCompany)) // 判斷是否有重複的email
								mailAddress.add(EMailCompany);
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
				}
				
				// 3.商品異動_副本 從 BU_COMMON_PHRASE_LINE 取得
				String attribute3 = buCommonPhraseLine.getAttribute3();
				if (StringUtils.hasText(attribute3)) {
					mailAddress.add(attribute3);
				}

				// 前半部主旨
				description = buCommonPhraseLine.getDescription();
			}

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

			// 設定範本
			templateFileName = "CommonTemplate.ftl";

			// #2 取得cc連結
			Map returnMap = new HashMap(0);
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
     * 成本改變，僅發信通知商控
     *
     * @param subject
     * @param templateFileName
     * @param display
     * @param mailAddress
     * @param attachFiles
     * @param cidMap
     */
	public void getCostMailList(ImPriceAdjustment head, String subject, String templateFileName, Map display,
			List mailAddress, List attachFiles, Map cidMap) throws Exception {
log.info("babugetCostMailList");
		try {
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

			// #1 取得配置檔得到 寄信的報表與
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("MailList"
					+ orderTypeCode, itemCategory); // +orderTypeCode
			// itemCategory
			if (null == buCommonPhraseLine) {
				// 寄給故障維謢人員
				mailAddress.add("Developer@tasameng.com.tw");
				subjectError = "MailList" + orderTypeCode + itemCategory + "無配置檔異常 ";
				description = "";
			} else {
				mailAddress.add("Developer@tasameng.com.tw"); // 資訊部
				mailAddress.add("MM-DG@tasameng.com.tw"); // 商控

				// 前半部主旨
				description = buCommonPhraseLine.getDescription();
			}

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

			// 設定範本
			templateFileName = "CommonTemplate.ftl";

			StringBuffer html = new StringBuffer();
			System.out.println("emailError.toString() = " + emailError.toString());
			if (StringUtils.hasText(emailError.toString())) {
				html.append(emailError.toString());
			}
			html.append("變價單：PAJ" + orderNo + "調整成本");
			System.out.println("subject = " + subject);
			System.out.println("html = " + html.toString());
			// 設定樣板的內容值
			display.put("display", html.toString());

			// 發信通知
			MailUtils.sendMail(subject, templateFileName, display, mailAddress, attachFiles, cidMap);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("取得寄件相關內容發生錯誤");
		}
	}

    /**
     * AJAX利用幣別指定匯率 修正line 上的台幣成本
     *
     * @param headObj
     */
    public List<Properties> updateAJAXLineExchangeRate(Properties httpRequest) throws Exception {
	List re = new ArrayList();
	Properties pro = new Properties();
	String exchangeRate = null;
	try {
	    String currencyCode = httpRequest.getProperty("currencyCode");
	    String brandCode = httpRequest.getProperty("brandCode");
	    String headId = httpRequest.getProperty("headId");
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	    Double ratio = NumberUtils.round(NumberUtils.getDouble(httpRequest.getProperty("ratio")), 4);

	    String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);

	    log.info( "currencyCode = " + currencyCode );
	    log.info( "brandCode = " + brandCode );
	    log.info( "headId = " + headId );
	    log.info( "ratio = " + ratio );
	    log.info( "organizationCode = " + organizationCode );

	    if (StringUtils.hasText(currencyCode)) {
		exchangeRate = buBasicDataService.getExchangeRateByCurrencyCode(organizationCode, currencyCode);
		pro.setProperty("exchangeRate", exchangeRate);
	    } else {
		exchangeRate = "1";
		pro.setProperty("exchangeRate", "1");
	    }
	    log.info( "1 = 1" );
	    // 更新line
	    if(PAP.equals(orderTypeCode)){
		ImPriceAdjustment head = imPriceAdjustmentDAO.findById(NumberUtils.getLong(headId));
		log.info( "1 = 2" );
		if(head != null){
		    log.info( "1 = 3" );
		    List<ImPriceList> line = head.getImPriceLists();
		    log.info( "1 = 4" );
		    for (ImPriceList imPriceList : line) {
			log.info( "1 = 5" );
			Double foreignCost = imPriceList.getForeignCost();

			log.info("foreignCost = " + foreignCost);

			String unitprice = AjaxUtils.getPropertiesValue(imPriceList.getUnitPrice(), "0.0");

			log.info("unitprice = " + unitprice);

			String localCost = String.valueOf(Double.valueOf(foreignCost) * Double.valueOf(exchangeRate) * ratio);
			String costRate = calCostRate(localCost,unitprice);

			log.info("foreignCost = " + foreignCost);
			log.info("unitprice = " + unitprice);
			log.info("localCost = " + localCost);
			log.info("costRate = " + costRate);

			imPriceList.setLocalCost(NumberUtils.getDouble(localCost));
			imPriceList.setCostRate(NumberUtils.getDouble(costRate));
			imPriceListDAO.update(imPriceList);
		    }
		}else{
		    log.error("查無定變價 headId = " + headId );
		    throw new Exception("查無定變價 headId = " + headId );
		}
	    }
	    pro.setProperty("ratio", ratio.toString() );
	    re.add(pro);
	    return re;
	} catch (Exception e) {
	    log.error("定變價匯率換算明細發生問題" + e.toString());
	    throw new Exception("定變價匯率換算明細發生問題" + e.getMessage());
	}
    }

    /**
     * 明細撈商品
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public Object getItemInfo(Properties httpRequest) throws Exception{
	List result = new ArrayList();
	String brandCode = httpRequest.getProperty("brandCode");
	String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	String itemCode = httpRequest.getProperty("itemCode");
	String priceType = httpRequest.getProperty("priceType");
	String exchangeRate = httpRequest.getProperty("exchangeRate");
	String ratio = httpRequest.getProperty("ratio");

	Long priceId = NumberUtils.getLong(httpRequest.getProperty("priceId"));

	log.info( "priceId = " + priceId );

	StringBuffer nativeSQL = new StringBuffer();
	if(PAP.equals(orderTypeCode)){
	    nativeSQL.append("SELECT ITEM_CODE, NVL(ITEM_C_NAME,' ') AS ITEM_C_NAME, PRICE_ID, CATEGORY13, SUPPIER_QUOTATION_PRICE AS FOREIGN_COST, (NVL(SUPPIER_QUOTATION_PRICE,0)  * ")
	    .append( exchangeRate+" * "+ ratio ).append(") AS LOCAL_COST , UNIT_PRICE FROM IM_ITEM_PRICE_VIEW ")
	    .append( "WHERE BRAND_CODE ='").append(brandCode)
	    .append("' AND TYPE_CODE ='").append(priceType)
	    .append("' AND PRICE_ENABLE='N' AND ITEM_ENABLE='Y");
	}else if(PAJ.equals(orderTypeCode)){
	    nativeSQL.append("SELECT NVL(ITEM_C_NAME,' ') AS ITEM_C_NAME, NVL(SUPPIER_QUOTATION_PRICE,0), UNIT_PRICE , PRICE_ID , IS_TAX, TYPE_CODE, TAX_CODE, ITEM_CODE FROM IM_ITEM_CURRENT_PRICE_VIEW WHERE BRAND_CODE ='").append(brandCode)
	    .append("' AND TYPE_CODE ='").append(priceType);
	}

	if( priceId == 0L && StringUtils.hasText(itemCode) ){
	    nativeSQL.append("' AND ITEM_CODE ='").append(itemCode).append("'");
	}else{
	    nativeSQL.append("' AND PRICE_ID ='").append(priceId).append("'");
	}

	log.info("nativeSQL = " + nativeSQL.toString());
	result = nativeQueryDAO.executeNativeSql(nativeSQL.toString());

	return result != null && result.size() > 0 ? result.get(0) : null;
    }

    /**
     * 明細撈商品 for T2
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public Object getT2ItemInfo(Properties httpRequest) throws Exception{
	List result = new ArrayList();
	String brandCode = httpRequest.getProperty("brandCode");
	String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	String itemCode = httpRequest.getProperty("itemCode");
	String priceType = httpRequest.getProperty("priceType");
	String exchangeRate = AjaxUtils.getPropertiesValue( httpRequest.getProperty("exchangeRate") , "1");

	Long priceId = NumberUtils.getLong(httpRequest.getProperty("priceId"));

	log.info( "priceId = " + priceId );

	StringBuffer nativeSQL = new StringBuffer();
	if(PAP.equals(orderTypeCode)){
	    nativeSQL.append("SELECT ITEM_CODE, NVL(ITEM_C_NAME,' ') AS ITEM_C_NAME, PRICE_ID, CATEGORY02, PURCHASE_AMOUNT AS FOREIGN_COST, (NVL(PURCHASE_AMOUNT,0)  * ")
	    .append( exchangeRate ).append(") AS LOCAL_COST, UNIT_PRICE, CURRENCY_CODE FROM IM_ITEM_PRICE_VIEW ")
	    .append( "WHERE BRAND_CODE ='").append(brandCode)
	    .append("' AND TYPE_CODE ='").append(priceType)
	    .append("' AND PRICE_ENABLE='N' AND ITEM_ENABLE='Y");
	}else if(PAJ.equals(orderTypeCode)){
	    nativeSQL.append("SELECT NVL(ITEM_C_NAME,' ') AS ITEM_C_NAME, NVL(PURCHASE_AMOUNT,0), UNIT_PRICE , PRICE_ID , IS_TAX, TYPE_CODE, TAX_CODE, ITEM_CODE, CURRENCY_CODE FROM IM_ITEM_CURRENT_PRICE_VIEW WHERE BRAND_CODE ='").append(brandCode)
	    .append("' AND TYPE_CODE ='").append(priceType);
	}

	if( priceId == 0L && StringUtils.hasText(itemCode) ){
	    nativeSQL.append("' AND ITEM_CODE ='").append(itemCode).append("'");
	}else{
	    nativeSQL.append("' AND PRICE_ID ='").append(priceId).append("'");
	}

	log.info("nativeSQL = " + nativeSQL.toString());
	result = nativeQueryDAO.executeNativeSql(nativeSQL.toString());

	return result != null && result.size() > 0 ? result.get(0) : null;
    }

    /**
     *
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
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
	    if(result.size() > 0){
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
     * 取得CC開窗URL字串
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
	    parameters.put("prompt0", brandCode);
	    parameters.put("prompt1", orderTypeCode);
	    parameters.put("prompt2", "");
	    parameters.put("prompt3", "");
	    parameters.put("prompt4", orderNo);
	    parameters.put("prompt5", orderNo);
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

    /**
     * 儲存商品定價/變價/組合商品送簽單
     *
     * @param imPriceAdjustment
     * @return String (Result message)
     * @throws Exception
     */
    public String save(ImPriceAdjustment imPriceAdjustment) throws Exception {

	try {
	    System.out.println("Save imPriceAdjustment");
	    if (OrderStatus.SIGNING.equals(imPriceAdjustment.getStatus())) {
		if (this.checkImPriceListItemData(imPriceAdjustment))
		    return saveImPriceAdjustment(imPriceAdjustment);
		else
		    return "送簽單存檔時發生錯誤!";
	    } else {
		return saveImPriceAdjustment(imPriceAdjustment);
	    }

	} catch (FormException fe) {
	    log.error("送簽單存檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("送簽單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("送簽單存檔時發生錯誤，原因：" + ex.getMessage());
	}

    }

    /**
     * 送出後順便判斷是否要送信（價格改變則須發email通知）
     *
     * @param imPriceAdjustment
     * @return String (Result message)
     * @throws Exception
     */
	public boolean saveAndSendMail(ImPriceAdjustment imPriceAdjustment) throws Exception {
		boolean b = false;
		try {
			saveImPriceAdjustment(imPriceAdjustment);
			List<ImPriceList> lists = imPriceAdjustment.getImPriceLists();
			for (Iterator iterator = lists.iterator(); iterator.hasNext();) {
				ImPriceList imPriceList = (ImPriceList) iterator.next();
				if (NumberUtils.getDouble(imPriceList.getUnitPrice()) != NumberUtils.getDouble(imPriceList
						.getOriginalPrice())) {
					b = true;
					break;
				}
			}
			return b;
		} catch (Exception ex) {
			log.error("送簽單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("送簽單存檔時發生錯誤，原因：" + ex.getMessage());
		}

	}

	/**
     * 判斷成本是否改變，如改變需通知商控 by Weichun 2011/09/19
     *
     * @param imPriceAdjustment
     * @return String (Result message)
     * @throws Exception
     */
	public boolean saveAndCheckCost(ImPriceAdjustment imPriceAdjustment) throws Exception {
		boolean b = false;
		try {
			saveImPriceAdjustment(imPriceAdjustment);
			List<ImPriceList> lists = imPriceAdjustment.getImPriceLists();
			for (Iterator iterator = lists.iterator(); iterator.hasNext();) { // 成本調整
				ImPriceList imPriceList = (ImPriceList) iterator.next();
				if (NumberUtils.getDouble(imPriceList.getForeignCost()) != NumberUtils.getDouble(imPriceList
						.getOriginalForeignCost())) {
					b = true;
					break;
				}
			}
			for (Iterator iterator = lists.iterator(); iterator.hasNext();) { // 如果有調價，則不重複發信
				ImPriceList imPriceList = (ImPriceList) iterator.next();
				if (NumberUtils.getDouble(imPriceList.getUnitPrice()) != NumberUtils.getDouble(imPriceList
						.getOriginalPrice())) {
					b = false;
					break;
				}
			}
			return b;
		} catch (Exception ex) {
			log.error("送簽單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("送簽單存檔時發生錯誤，原因：" + ex.getMessage());
		}

	}

    /**
     * save temp_order_no
     * @param saveObj
     * @exception FormException ,Exception
     * @return String
     */
    public void saveTmp(ImPriceAdjustment imAdjustmentHead) throws FormException ,Exception{
	String tmpOrderNo = AjaxUtils.getTmpOrderNo();
	imAdjustmentHead.setOrderNo(tmpOrderNo);
	imAdjustmentHead.setLastUpdateDate(new Date());
	imAdjustmentHead.setCreationDate(new Date());
	imPriceAdjustmentDAO.save(imAdjustmentHead);
    }

    private String saveImPriceAdjustment(ImPriceAdjustment imPriceAdjustment)
    throws ObtainSerialNoFailedException {
	if (imPriceAdjustment.getHeadId() == null
		|| imPriceAdjustment.getHeadId() == 0) {

	    // 新增
	    imPriceAdjustment.setLastUpdateDate(new Date());
	    imPriceAdjustment.setCreationDate(new Date());
	    // 取得最新一筆流水號
	    String serialNo = buOrderTypeService.getOrderSerialNo(
		    imPriceAdjustment.getBrandCode(), imPriceAdjustment
		    .getOrderTypeCode());
	    if (!serialNo.equals("unknow")) {
		imPriceAdjustment.setOrderNo(serialNo);
		imPriceAdjustmentDAO.save(imPriceAdjustment);
	    } else {
		throw new ObtainSerialNoFailedException("取得"
			+ imPriceAdjustment.getOrderTypeCode() + "單號失敗！");
	    }
	    return imPriceAdjustment.getOrderTypeCode() + "-" + serialNo
	    + "存檔成功！";

	} else {
	    imPriceAdjustment.setLastUpdateDate(new Date());
	    imPriceAdjustmentDAO.update(imPriceAdjustment);
	    return imPriceAdjustment.getOrderTypeCode() + "-"
	    + imPriceAdjustment.getOrderNo() + "存檔成功！";
	}
    }

    /**
     * 額外設定 其他欄位
     * @param imItemPriceViews
     */
    private void setOtherImItemPriceView(String orderTypeCode, List views){

	for (Object object : views) {
	    if( PAP.equals(orderTypeCode)){
		ImItemPriceView imItemPriceView = ((ImItemPriceView)object);

		String priceEnable = imItemPriceView.getPriceEnable();
		imItemPriceView.setItemUnit( buCommonPhraseService.getLineName("ItemUnit", imItemPriceView.getSalesUnit()) );
		imItemPriceView.setPriceType( buCommonPhraseService.getLineName("PriceType", imItemPriceView.getTypeCode()) );

		if(priceEnable==null){
		    imItemPriceView.setPriceEnableName("未啟用");
		}else if(priceEnable.equals("Y")){
		    imItemPriceView.setPriceEnableName("啟用");
		}else if(priceEnable.equals("N")){
		    imItemPriceView.setPriceEnableName("未啟用");
		}

	    }else if(PAJ.equals(orderTypeCode)){
		ImItemCurrentPriceView imItemCurrentPriceView = (ImItemCurrentPriceView)object;

		imItemCurrentPriceView.setItemUnit( buCommonPhraseService.getLineName("ItemUnit", imItemCurrentPriceView.getSalesUnit()) );
		imItemCurrentPriceView.setPriceType( buCommonPhraseService.getLineName("PriceType", imItemCurrentPriceView.getTypeCode()) );
	    }

	}
    }

    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
	String errorMsg = null;
	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     *
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveLineSearchResult(Properties httpRequest) throws Exception{
	String errorMsg = null;
	String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	String brandCode = httpRequest.getProperty("brandCode");

	if(brandCode.indexOf("T2") > -1){
	    if(PAP.equals(orderTypeCode)){
		AjaxUtils.updateSearchResult(httpRequest, T2_GRID_LINE_PAP_SEARCH_FIELD_NAMES);
	    }else if(PAJ.equals(orderTypeCode)){
		AjaxUtils.updateSearchResult(httpRequest, T2_GRID_LINE_PAJ_SEARCH_FIELD_NAMES);
	    }
	}else{
	    if(PAP.equals(orderTypeCode)){
		AjaxUtils.updateSearchResult(httpRequest, GRID_LINE_PAP_SEARCH_FIELD_NAMES);
	    }else if(PAJ.equals(orderTypeCode)){
		AjaxUtils.updateSearchResult(httpRequest, GRID_LINE_PAJ_SEARCH_FIELD_NAMES);
	    }
	}

	return AjaxUtils.getResponseMsg(errorMsg);
    }


    /**
     * 設定下一個狀態
     * @param head
     */
    private void setNextStatus(ImPriceAdjustment head, String formAction, String approvalResult){
	if(OrderStatus.FORM_SAVE.equals(formAction)){
	    head.setStatus(OrderStatus.SAVE);
	}else if(OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ){
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
     * 一般調整單取得下個狀態
     */
    public void setReverseNextStatus(ImPriceAdjustment head){

	if(OrderStatus.FINISH.equals(head.getStatus()) ){
		head.setStatus(OrderStatus.SAVE);
	}
    }

    /**
     * 若是暫存單號,則取得新單號
     * @param head
     */
    private void setOrderNo(ImPriceAdjustment head) throws ObtainSerialNoFailedException{
	String orderNo = head.getOrderNo();
	if (AjaxUtils.isTmpOrderNo(orderNo)) {
	    try {
		String serialNo = buOrderTypeService.getOrderSerialNo(head.getBrandCode(), head.getOrderTypeCode());
		if ("unknow".equals(serialNo))
		    throw new ObtainSerialNoFailedException("取得" + head.getBrandCode() + "-" + head.getOrderTypeCode() + "單號失敗！");
		else{
		    head.setCreationDate(new Date());
		    head.setCreatedBy(head.getCreatedBy());
		    head.setOrderNo(serialNo);
		}
	    } catch (Exception ex) {
		throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode() + "單號失敗！");
	    }
	}
    }

    /**
     * 設定 for T2 下拉
     * @param brandCode
     * @param multiList
     */
    private void setT2Select(ImPriceAdjustment form, Map multiList)throws ValidationErrorException, Exception{
	String brandCode = form.getBrandCode();
//	String itemCategory = form.getItemCategory();

	log.info( "brandCode = " + brandCode);
//	log.info( "itemCategory = " + itemCategory);
	/*		if("T2".equals(brandCode)){
			 List<ImItemCategory>     allItemCategory    = imItemCategoryService.findByCategoryType(brandCode, "ITEM_CATEGORY");
			 List<BuEmployeeWithAddressView> allPurchaseAssist = new ArrayList();
			 List<BuEmployeeWithAddressView> allPurchaseMember = new ArrayList();
			 List<BuEmployeeWithAddressView> allPurchaseMaster = new ArrayList();
			 log.info( "1 = 1");
			 if(!StringUtils.hasText(itemCategory) && allItemCategory.size()>0) {
				 itemCategory = allItemCategory.get(0).getId().getCategoryCode();
			 }
			 log.info( "1 = 2");
			 if(StringUtils.hasText(itemCategory)){
				HashMap mapAssist = new HashMap();
				mapAssist.put("itemCategory",       itemCategory);
				mapAssist.put("employeeDepartment", "506");
				mapAssist.put("employeePosition",   "A");
				allPurchaseAssist = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege( mapAssist );

				HashMap mapMember = new HashMap();
				mapMember.put("itemCategory",       itemCategory);
				mapMember.put("employeeDepartment", "506");
				mapMember.put("employeePosition",   "P");
				allPurchaseMember = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege( mapMember );

				HashMap mapMaster = new HashMap();
				mapMaster.put("itemCategory",       itemCategory);
				mapMaster.put("employeeDepartment", "506");
				mapMaster.put("employeePosition",   "M");
				allPurchaseMaster = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege( mapMaster );
			}
			log.info( "1 = 3");
			if( allPurchaseAssist.size()>0 ){	// 預設採購助理=KEY單人員
				if( form.getCreatedBy().equals(allPurchaseAssist.get(0).getEmployeeCode()) ){
					form.setPurchaseAssist(allPurchaseAssist.get(0).getEmployeeCode());
				}
			}
			log.info( "1 = 4");
			multiList.put("allPurchaseAssist",  AjaxUtils.produceSelectorData( allPurchaseAssist,  "employeeCode",    "chineseName", true, true));
			multiList.put("allPurchaseMember",  AjaxUtils.produceSelectorData( allPurchaseMember,  "employeeCode",    "chineseName", true, true));
			multiList.put("allPurchaseMaster",  AjaxUtils.produceSelectorData( allPurchaseMaster,  "employeeCode",    "chineseName", true, true));
			multiList.put("allItemCategory",    AjaxUtils.produceSelectorData( allItemCategory,    "categoryCode",    "categoryName",  true,  false));
			log.info( "1 = 5");
		}*/

	if(form.getBrandCode().indexOf("T2") > -1){
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
	}
    }

    /**
     * 取得採購助理,採購人員,採購主管預設值
     * @param itemCategory
     * @return
     */
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
     * 啟動流程
     * @param form
     * @return
     * @throws Exception
     */
    public static Object[] startProcess(ImPriceAdjustment form) throws Exception{
	String packageId = null;
	String processId = null;
	String version = null;
	String sourceReferenceType = null;
	try{
	    String orderTypeCode = form.getOrderTypeCode();
	    String brandCode = form.getBrandCode();
			if (brandCode.indexOf("T2") > -1) {
				packageId = "Im_ItemPriceAdjustment";
				processId = "PriceAdjustmentT2";
				version = "20110421"; // 20110421 20101103
										// 20100710,20100105,20100602
				sourceReferenceType = "T2_PAP_PAJ";
			} else {
				if (PAP.equals(orderTypeCode)) {
					packageId = "Im_ItemPriceAdjustment";
					processId = "StandardPriceApproval";
					version = "20100308";// "20090824";
					sourceReferenceType = "PAP";
				} else if (PAJ.equals(orderTypeCode)) {
					packageId = "Im_ItemPriceAdjustment";
					processId = "PriceAdjustmentApproval";
					version = "20140701";// "20091229";
					sourceReferenceType = "PAJ";
				} else {
					throw new Exception("查無此單別流程,請確認單別");
				}
			}
			HashMap context = new HashMap();
			context.put("brandCode", form.getBrandCode());
			context.put("formId", form.getHeadId());
			context.put("orderType", form.getOrderTypeCode());
			context.put("orderNo", form.getOrderNo());
			context.put("isThreePointMoving", false);
			return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
	} catch(Exception e){
	    e.printStackTrace();
	    log.error("商品定價變價流程執行時發生錯誤，原因：" + e.toString());
	    throw new ProcessFailedException("商品定價變價流程執行時發生錯誤，原因：" +e.getMessage());
	}

    }

    /**
     * 依據 pk 查詢送簽單
     *
     * @param id
     * @return ImPriceAdjustment
     */
    public ImPriceAdjustment findById(Long id) {
	return imPriceAdjustmentDAO.findById(id);
    }

    /**
     * 依據查詢條件查詢送簽單
     *
     * @param brandCode
     * @param orderType
     * @param startOrderNo
     * @param endOrderNo
     * @param startDate
     * @param endDate
     * @param status
     * @param employeeCode
     * @param categorySearchString
     * @return List<ImPriceAdjustment>
     * @throws Exception
     */
    public List<ImPriceAdjustment> findPriceAdjustmentByValue(HashMap findObjs)
    throws Exception {
	try {

	    return imPriceAdjustmentDAO.findPriceAdjustmentByValue(findObjs);

	} catch (Exception ex) {
	    log.error("查詢送簽單時發生錯誤，原因：" + ex.toString());
	    throw new NoSuchDataException("查詢送簽單時發生錯誤：" + ex.getMessage());
	}
    }

    /**
     * 依據調價比例及進位方式設定變價價格
     *
     * @param priceList
     * @param percentage
     *            調價比例
     * @param calculateType
     *            進位方式 1)四捨五入 2)無條件捨去 3)無條件進位
     * @param scale
     *            第幾位進位
     * @return List<ImPriceList>
     */
    public List<ImPriceList> calculateAdjustmentAmount(
	    List<ImPriceList> priceList, Integer percentage,
	    String calculateType, Integer scale) {
	List<ImPriceList> result = new ArrayList(0);
	if (scale >= 0) {
	    double percent = (double) percentage / 100;
	    for (ImPriceList price : priceList) {
		// 依據調整比例計算調整後金額
		if ("2".equals(calculateType)) { // 無條件捨去

		    price.setUnitPrice(OperationUtils.floorUp(
			    price.getOriginalPrice() * percent, scale)
			    .doubleValue());

		} else if ("3".equals(calculateType)) {// 無條件進位
		    price.setUnitPrice(OperationUtils.ceilUp(
			    price.getOriginalPrice() * percent, scale)
			    .doubleValue());

		} else { // 四捨五入
		    price.setUnitPrice(OperationUtils.roundUp(
			    price.getOriginalPrice() * percent, scale)
			    .doubleValue());
		}
		result.add(price);
	    }
	    return result;
	} else {
	    return priceList;
	}

    }

    /**
     * 取得商品名稱及單位(Only for Update using)
     *
     * @param imPriceLists
     * @throws Exception
     */
    public void reflashImPriceListItemData(List<ImPriceList> imPriceLists)
    throws Exception {

	for (ImPriceList imPriceList : imPriceLists) {
	    if ((imPriceList.getItemCode() != null && !("".equals(imPriceList.getItemCode())))) {
		try {
		    imPriceList.setItemCode(imPriceList.getItemCode().toUpperCase());
		    ImItem imItem = imItemDAO.findById(imPriceList.getItemCode());
		    if (imItem != null) {
			imPriceList.setSalesUnit(imItem.getSalesUnit());
			imPriceList.setItemName(imItem.getItemCName());
			imPriceList.setCategory01(imItem.getCategory13());
		    } else {
			imPriceList.setSalesUnit("");
			imPriceList.setItemName("查無商品資料");
		    }
		} catch (Exception ex) {
		    log.error(imPriceList.getItemCode() + "查詢商品資料時發生錯誤，原因："
			    + ex.toString());
		    throw new Exception(imPriceList.getItemCode()
			    + "查詢商品資料時發生錯誤，原因：" + ex.getMessage());
		}
	    }
	}
    }

    /**
     * 檢查 ImPriceAdjustment 之正確性
     *
     * @param checkObj
     *            (ImPriceAdjustment)
     * @return
     * @throws Exception
     */
    public boolean checkImPriceListItemData(ImPriceAdjustment checkObj)
    throws Exception {
	if (null == checkObj.getEnableDate())
	    throw new FormException("啟用日期為空白，請重新輸入!");
	if (!StringUtils.hasText(checkObj.getBrandCode()))
	    throw new FormException("品牌代號為空白，請重新輸入!");
	if (!StringUtils.hasText(checkObj.getPriceType()))
	    throw new FormException("價格類別為空白，請重新輸入!");
	if("PAP".equals(checkObj.getOrderTypeCode())){
	    if (!StringUtils.hasText(checkObj.getSupplierCode()))
		throw new FormException("供應商代號為空白，請重新輸入!");
	    else{
		BuSupplierWithAddressView buSupplierWithAddressView =
		    buSupplierWithAddressViewDAO.findById(checkObj.getSupplierCode() , checkObj.getBrandCode());
		if( null == buSupplierWithAddressView ){
		    throw new FormException("供應商代號錯誤，請重新輸入!");
		}
		if(!(StringUtils.hasText(checkObj.getCurrencyCode()))){
		    throw new FormException("幣別資料為空白!");
		}
	    }
	}
	List<ImPriceList> imPriceLists = checkObj.getImPriceLists();
	if (imPriceLists.size() == 0)
	    throw new FormException("您尚未輸入明細資料，請重新輸入!");
	int i = 1;
	for (ImPriceList imPriceList : imPriceLists) {
	    if (StringUtils.hasText(imPriceList.getItemCode())) {
		this.checkPriceItemByValue(checkObj.getBrandCode(), checkObj
			.getOrderTypeCode(), checkObj.getOrderNo(), i, checkObj
			.getPriceType(), imPriceList);
		imPriceList.setReserve1("Okay ");

	    } else {
		throw new FormException("明細資料第" + i + "筆品號未輸入，請重新輸入!");
	    }

	    i++;
	}

	return true;
    }

    /**
     * 檢查送簽單之明細品號之正確性 PAJ)變價單 - 品號必須為價格資料已啟用之商品 PDP)定價單 - 品號必須為價格資料未啟用之商品
     * CIA)組合商品送簽單 - 品號必須為價格資料未啟用之組合商品
     *
     * @param brandCode
     * @param orderTypeCode
     * @param orderNo
     * @param lineNo
     * @param priceType
     * @param imPriceList
     * @throws FormException
     * @throws Exception
     */
    private void checkPriceItemByValue(String brandCode, String orderTypeCode,
	    String orderNo, int lineNo, String priceType,
	    ImPriceList imPriceList) throws FormException {

	if ("PAJ".equals(orderTypeCode)) {
	    List<ImItemCurrentPriceView> imItemCurrentPriceView = imItemCurrentPriceViewDAO
	    .findCurrentPriceByValue(brandCode, imPriceList
		    .getItemCode(), imPriceList.getItemCode(),
		    priceType, "");
	    if (imItemCurrentPriceView.size() == 0) {
		throw new NoSuchObjectException("商品變價單" + orderTypeCode + "-"
			+ orderNo + "中，第" + lineNo + "筆資料("
			+ imPriceList.getItemCode() + ")，查無已啟用之商品資料");
	    }
	} else if ("PAP".equals(orderTypeCode)) {
	    List<ImItemPriceView> imItemPriceView = imItemPriceViewDAO
	    .findPriceViewByValue(brandCode, imPriceList.getItemCode(),
		    imPriceList.getItemCode(), "", "", priceType, "N",
		    "", "");
	    if (imItemPriceView.size() == 0) {
		throw new NoSuchObjectException("商品定價送簽單" + orderTypeCode + "-"
			+ orderNo + "中，第" + lineNo + "筆資料("
			+ imPriceList.getItemCode() + ")，查無未啟用之商品資料");
	    }/*else{
				if(!(StringUtils.hasText(imPriceList.getCurrencyCode()))){
					throw new NoSuchObjectException("商品定價送簽單" + orderTypeCode + "-"
							+ orderNo + "中，第" + lineNo + "筆資料("
							+ imPriceList.getItemCode() + ")，幣別資料為空白");
				}
			}*/
	} else if ("CIA".equals(orderTypeCode)) {
	    List<ImItemPriceView> imItemPriceView = imItemPriceViewDAO
	    .findPriceViewByValue(brandCode, imPriceList.getItemCode(),
		    imPriceList.getItemCode(), "", "", priceType, "N",
		    "", "and model.isComposeItem = 'Y'");
	    if (imItemPriceView.size() == 0) {
		throw new NoSuchObjectException("組合商品送簽單" + orderTypeCode + "-"
			+ orderNo + "中，第" + lineNo + "筆資料("
			+ imPriceList.getItemCode() + ")，查無未啟用之組合商品資料");
	    }
	} else {
	    throw new FormException("單別資料錯誤「" + orderTypeCode + "」");
	}

    }


	/**
	 * 啟動ImItemPrice之價格，於Process中使用 查看資料庫中是否有已生效的價格資訊，如果有即使用此筆資料進行更新
	 * 如果沒有，使用ItemCode將整筆Item取出，並在Price中新增一筆價格資料 此用意為如價格在經過第一次調整後，user發現價格有誤時，
	 * user可以再次新建一張變價單進行更新
	 *
	 * @param headId
	 * @throws Exception
	 */
	public String updateAdjustUnitPrice(Long headId) throws Exception {
		try {
			System.out.println("anber");
			String result = "";
			ImPriceAdjustment imPriceAdjustment = imPriceAdjustmentDAO.findById(headId);
			if (imPriceAdjustment != null) {
				java.util.Date enableDate = imPriceAdjustment.getEnableDate();
				List<ImPriceList> imPriceLists = imPriceAdjustment.getImPriceLists();

				for (ImPriceList imPriceList : imPriceLists) {
					// 如果新價格<>原價才進行修改
					if (imPriceList.getOriginalPrice() != imPriceList.getUnitPrice()) {
						// 查看資料庫中是否有已生效的價格資訊，如果有即使用此筆資料進行更新
						// 如果沒有，使用ItemCode將整筆Item取出，並在Price中新增一筆價格資料
						// 此用意為如價格在經過第一次調整後，user發現價格有誤時，
						// user可以再次新建一張變價單進行更新
						// 需增加判斷品牌(brandCode)，避免不同品牌，品號相同，發生資料修改錯誤的狀況 modify by Weichun 2011.06.10
						ImItemPrice imItemPrice = imItemPriceDAO.findEnablePrice(imPriceList.getItemCode(),
								imPriceAdjustment.getPriceType(), enableDate, imPriceAdjustment.getBrandCode());
						System.out.println("a:" + imItemPrice);
						if (imItemPrice == null) {
							ImItem item = imItemDAO.getLockedImItem(imPriceAdjustment.getBrandCode(), imPriceList
									.getItemCode());
							System.out.println("b:" + item);
							if (item != null) {

								List<ImItemPrice> imItemPrices = item.getImItemPrices();
								ImItemPrice imPrice = new ImItemPrice();
								imPrice.setPriceAdjustId(headId);
								imPrice.setItemCode(imPriceList.getItemCode());
								imPrice.setTypeCode(imPriceAdjustment.getPriceType());
								imPrice.setCurrencyCode(imPriceList.getCurrencyCode());
								imPrice.setUnitPrice(imPriceList.getUnitPrice());
								imPrice.setIsTax(imPriceList.getIsTax());
								imPrice.setTaxCode(imPriceList.getTaxCode());
								imPrice.setBeginDate(enableDate);
								imPrice.setEnable("Y");
								imPrice.setCreatedBy(imPriceAdjustment.getLastUpdatedBy());
								imPrice.setCreationDate(new Date());
								imPrice.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
								imPrice.setLastUpdateDate(new Date());
								imPrice.setIndexNo(imItemPrices.size() + 1L);

								// imItemPrices.add(imItemPrice);
								// System.out.println("d:"+imItem.getImItemPrices().size());
								// imItem.setImItemPrices(imItemPrices);
								// System.out.println("e:"+imItem.getImItemPrices().get(1));
								// imItemDAO.update(imItem);
								imItemPriceDAO.save(imPrice);
								result = "imItemPriceDAO.save";

							} else {
								throw new NoSuchDataException("查無商品資訊(ImItem)，ID:" + imPriceList.getItemCode() + "，變價作業啟動失敗");
							}
						} else {
							System.out.println("c:" + headId);
							imItemPrice.setPriceAdjustId(headId);
							imItemPrice.setUnitPrice(imPriceList.getUnitPrice());
							imItemPrice.setBeginDate(enableDate);
							imItemPrice.setEnable("Y");
							imItemPrice.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
							imItemPrice.setLastUpdateDate(new Date());
							imItemPriceDAO.update(imItemPrice);
							result = "imItemPriceDAO.update";
						}
					}
					// 更改原廠 報價欄位
					if (imPriceList.getOriginalQuotationPrice() != imPriceList.getNewQuotationPrice()) {
						ImItem imItem = imItemDAO.getLockedImItem(imPriceAdjustment.getBrandCode(), imPriceList
								.getItemCode());
						System.out.println("d:" + imItem);
						if (imItem != null) {
							imItem.setSupplierQuotationPrice(imPriceList.getNewQuotationPrice());
							imItem.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
							imItem.setLastUpdateDate(new Date());
							imItemDAO.update(imItem);
							result = "imItemDAO.update";
						} else {
							throw new NoSuchDataException("查無商品資訊(ImItem)，ID:" + imPriceList.getItemCode()
									+ "，變價作業啟動失敗(查無商品資料)");
						}
					}

				}

			} else {
				throw new NoSuchDataException("查無變價單(ImPriceAdjustment)，ID:" + headId + "，變價作業啟動失敗");
			}
			return result;
		} catch (Exception ex) {
			log.error("ID:" + headId + ",變價作業啟動時發生錯誤，原因：" + ex.toString());
			throw new Exception("ID:" + headId + ",變價作業啟動時發生錯誤，原因：" + ex.getMessage());
		}
	}

    public void composeItem(String itemCode, Long quantity) throws NoSuchDataException{
	ImItem imItem = imItemDAO.findById(itemCode);
	if (imItem != null) {
	    if("Y".equals(imItem.getIsComposeItem())){
		List<ImItemCompose> composeItems = imItem.getImItemComposes();
		for(ImItemCompose item : composeItems){
		    Long reserveQuantity =  quantity * item.getQuantity();

		}
	    }else{

	    }
	}else{
	    throw new NoSuchDataException("查無商品資訊(ImItem)，ID:"
		    + itemCode + "，商品組合失敗");
	}
    }

    public boolean isMarkdown(Long headId){
	ImPriceAdjustment imPriceAdjustment = imPriceAdjustmentDAO.findById(headId);
	boolean isMarkdown =true; //
	if (imPriceAdjustment != null) {
	    List<ImPriceList> imPriceLists = imPriceAdjustment.getImPriceLists();
	    for(ImPriceList imPriceList:imPriceLists){
		if (imPriceList.getOriginalPrice() < imPriceList.getUnitPrice() ) //漲價
		    isMarkdown = false;
	    }

	}
	return isMarkdown;
    }

    public void changeLocalCost(Long headId) throws Exception{
	imItemDAO.changeLocalCost(String.valueOf(headId));
    }

    /**
     * 判斷是否超出CEO的上限毛利率差異
     * @param headId
     * @return
     */
    public boolean isOverCEOLimitation(Long headId){
	ImPriceAdjustment imPriceAdjustment = imPriceAdjustmentDAO.findById(headId);
	if (imPriceAdjustment != null) {
	    List<ImPriceList> imPriceLists = imPriceAdjustment.getImPriceLists();
	    for(ImPriceList imPriceList:imPriceLists){
		log.info("毛利率差異 = " + imPriceList.getGrossProfitRate());
		log.info("毛利率差異 >= 5D = " + (imPriceList.getGrossProfitRate() >= 5D));
		log.info("毛利率差異 <= -5D = " + (imPriceList.getGrossProfitRate() <= -5D));
		if (imPriceList.getGrossProfitRate() <= -5D || imPriceList.getGrossProfitRate() >= 5D){ //毛利率差異+-5%將送總經理審核
		    return true;
		}
	    }

	}
	return false;
    }

    private String modifyImPriceAdjustmentHead(ImPriceAdjustment imPriceObject,String loginId , String formAction)throws ObtainSerialNoFailedException, FormException,Exception{
	if(AjaxUtils.isTmpOrderNo(imPriceObject.getOrderNo())){
	    String serialNo = buOrderTypeService.getOrderSerialNo(imPriceObject.getBrandCode(), imPriceObject.getOrderTypeCode());
	    if(serialNo.equals("unknow")){
		throw new ObtainSerialNoFailedException("取得"+imPriceObject.getOrderTypeCode()+"單號失敗");
	    }else{
		imPriceObject.setOrderNo(serialNo);
	    }
	}
	if(OrderStatus.FORM_SUBMIT.equals(formAction)){
	    imPriceObject.setLastUpdatedBy(loginId);
	    imPriceObject.setLastUpdateDate(new Date());
	    if(checkImPriceListItemData(imPriceObject)){
		System.out.println("--------is---Here--------"+new Date());
		imPriceAdjustmentDAO.update(imPriceObject);
		System.out.println("--------is----Over------"+new Date());
	    }
	}else{
	    log.error(new Date());
	    imPriceAdjustmentDAO.update(imPriceObject);
	    log.error(new Date());
	}

	return imPriceObject.getOrderTypeCode()+"-"+imPriceObject.getOrderNo()+"存檔成功";
    }

    /**
     * 計算毛利率差異
     * @param oPrice
     * @param oQuotation
     * @param exchangeRate
     * @param unitPrice
     * @param newQuotation
     * @return
     */
    private String calGrossProfitRate(String oPrice,String oQuotation,String exchangeRate,String unitPrice,String newQuotation){
	String calResult="0";
	double tmpResult = 0L;
	double tmpNew = 0L;

	log.info("oPrice = " + oPrice );
	log.info("oQuotation = " + oQuotation );
	log.info("exchangeRate = " + exchangeRate );
	log.info("unitPrice = " + unitPrice );
	log.info("newQuotation = " + newQuotation );

	if(null==newQuotation || "0".equals(newQuotation) || "0.0".equals(newQuotation) || null == unitPrice || "0".equals(unitPrice) || "0.0".equals(unitPrice) || null == oPrice || "0".equals(oPrice) || "0.0".equals(oPrice) ){
	    return calResult;
	}else{
	    tmpResult = (Double.parseDouble(oPrice)-(Double.parseDouble(oQuotation)*Double.parseDouble(exchangeRate)))/Double.parseDouble(oPrice);
	    tmpNew =  (Double.parseDouble(unitPrice)-(Double.parseDouble(newQuotation)*Double.parseDouble(exchangeRate)))/Double.parseDouble(unitPrice);

	    calResult = NumberUtils.roundToStr((tmpNew-tmpResult)*100, 2);
	}

	return calResult;
    }

    public ImPriceAdjustment findExistImPriceAdjustment(Object otherBean,Map resultMap)throws Exception{
	String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
	Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
	ImPriceAdjustment form = imPriceAdjustmentDAO.findById(formId);
	if(form != null){
//	    reflashImPriceListItemData(form.getImPriceLists());
	    //form.setImPriceLists(null);
	    return form;
	}else{
	    throw new NoSuchDataException("查無此單號("+formId+")，於按下「確認」鍵後，將關閉本視窗！");
	}
    }

    /**
     * 匯入商品定價變價明細 免稅
     *
     * @param headId
     * @param priceLists
     * @throws Exception
     */
    public void executeImportT2PriceLists(Long headId, String brandCode, String orderTypeCode, String priceType, List priceLists) throws Exception{

	try{
	    imPriceListService.deletePriceLists(headId);

	    log.info("headId = " + headId);
	    log.info("brandCode = " + brandCode);
	    log.info("orderTypeCode = " + orderTypeCode);
	    log.info("priceType = " + priceType);

	    String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
	    if(priceLists != null && priceLists.size() > 0){
		ImPriceAdjustment imPriceAdjustmentTmp = new ImPriceAdjustment();
		imPriceAdjustmentTmp.setHeadId(headId);
		for(int i = 0; i < priceLists.size(); i++){
		    ImPriceList  imPriceList = (ImPriceList)priceLists.get(i);

		    String itemCode = imPriceList.getItemCode(); // 商品品號
//		    String foreignCost = AjaxUtils.getPropertiesValue( imPriceList.getForeignCost(), "0"); // 原幣成本
		    String localCost = AjaxUtils.getPropertiesValue( imPriceList.getLocalCost(), "0"); // 台幣成本
		    String unitPrice = AjaxUtils.getPropertiesValue( imPriceList.getUnitPrice(), "0"); // 標準售價
		    String currencyCode = imPriceList.getCurrencyCode();
		    String originalCurrencyCode = imPriceList.getOriginalCurrencyCode(); // 變價 原幣幣別(舊)

		    log.info("imPriceList.getUnitPrice() = " + imPriceList.getUnitPrice());
		    log.info("unitPrice = " + unitPrice);

		    if(PAP.equals(orderTypeCode)){ // 訂價
			ImItemPriceView imItemPriceView = imItemPriceViewDAO.findOneItemPriceView(brandCode, priceType, itemCode);

			if( null != imItemPriceView){
			    // 如果訂變價幣別為空，則用view裡的幣別帶出匯率,表示是從明細匯入進來
			    if(!StringUtils.hasText(currencyCode)){
				// 依幣別品牌的組織取得匯率
				String exchangeRate = buBasicDataService.getExchangeRateByCurrencyCode(organizationCode,imItemPriceView.getPurchaseCurrencyCode());
				log.info("currencyCode = " + imItemPriceView.getPurchaseCurrencyCode());
				log.info("exchangeRate = " + exchangeRate);
				localCost = String.valueOf(imItemPriceView.getPurchaseAmount() * Double.valueOf(exchangeRate)); // 匯入後得出的新台幣成本

				String grossRate = calGrossRate( localCost,  unitPrice  );

				imPriceList.setCurrencyCode( imItemPriceView.getPurchaseCurrencyCode());
				imPriceList.setExchangeRate(Double.valueOf(exchangeRate));
				imPriceList.setForeignCost(imItemPriceView.getPurchaseAmount());
				imPriceList.setLocalCost(NumberUtils.getDouble(localCost));

				imPriceList.setGrossRate( NumberUtils.getDouble(grossRate) );
				imPriceList.setPriceId(imItemPriceView.getPriceId());
			    }
			}else{
			    log.info("匯入的品號:"+itemCode+"不存在");
			}
		    }else if(PAJ.equals(orderTypeCode)){ //變價
			log.info("匯入 itemCode = " + imPriceList.getItemCode());
			log.info("匯入 currencyCode = " + imPriceList.getCurrencyCode());
			log.info("匯入 foreignCost = " + imPriceList.getForeignCost());
			log.info("匯入 unitPrice = " + imPriceList.getUnitPrice());
			ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findOneCurrentPriceByProperty(brandCode, priceType, itemCode);

			if(imItemCurrentPriceView != null){
			    Double originalExchangeRate = NumberUtils.getDouble(buBasicDataService.getExchangeRateByCurrencyCode(organizationCode,imItemCurrentPriceView.getPurchaseCurrencyCode()));

			    imPriceList.setOriginalCurrencyCode( imItemCurrentPriceView.getPurchaseCurrencyCode()); // 原幣幣別(舊)
			    imPriceList.setOriginalExchangeRate(originalExchangeRate);				// 匯率(舊)
			    imPriceList.setOriginalForeignCost( imItemCurrentPriceView.getPurchaseAmount()); // 原幣成本(舊)
			    imPriceList.setOriginalPrice( imItemCurrentPriceView.getUnitPrice()); // 標準售價(舊)

			    // 不存在則塞原本值
			    if(!StringUtils.hasText(imPriceList.getCurrencyCode())){ // 原幣幣別(新)
				imPriceList.setCurrencyCode(imItemCurrentPriceView.getPurchaseCurrencyCode());
			    }
			    if(null == imPriceList.getForeignCost()){ // 原幣幣別(新)
				imPriceList.setForeignCost(imItemCurrentPriceView.getPurchaseAmount());
			    }
			    if(null == imPriceList.getUnitPrice()){ // 標準售價(新)
				imPriceList.setUnitPrice(imItemCurrentPriceView.getUnitPrice());
			    }

			    // 依幣別品牌的組織取得匯率(新)
			    String newExchangeRate = buBasicDataService.getExchangeRateByCurrencyCode(organizationCode,imPriceList.getCurrencyCode());
			    imPriceList.setExchangeRate(NumberUtils.getDouble(newExchangeRate)); // 匯率(新)

			    imPriceList.setPriceId(imItemCurrentPriceView.getPriceId());
			}else{
			    log.info("匯入的品號:"+itemCode+"不存在");
			}
		    }
		    imPriceList.setImPriceAdjustment(imPriceAdjustmentTmp);
		    imPriceList.setIndexNo(i + 1L);
		    imPriceListDAO.save(imPriceList);
		}
	    }
	}catch (Exception ex) {
	    log.error("商品定價變價明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("商品定價變價明細匯入時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 匯入商品定價變價明細 百貨
     *
     * @param headId
     * @param priceLists
     * @throws Exception
     */
    public void executeImportT1PriceLists(Long headId, String brandCode, String orderTypeCode, String priceType, String exchangeRate, List priceLists) throws Exception{

	try{
	    imPriceListService.deletePriceLists(headId);
	    if(priceLists != null && priceLists.size() > 0){
		ImPriceAdjustment imPriceAdjustmentTmp = new ImPriceAdjustment();
		imPriceAdjustmentTmp.setHeadId(headId);
		for(int i = 0; i < priceLists.size(); i++){
		    ImPriceList  imPriceList = (ImPriceList)priceLists.get(i);

		    String itemCode = imPriceList.getItemCode();

		    if(PAP.equals(orderTypeCode)){
			ImItemPriceView imItemPriceView = imItemPriceViewDAO.findOneItemPriceView(brandCode, priceType, itemCode);
			if( null != imItemPriceView ){
			    log.info("原幣成本:" + imItemPriceView.getSupplierQuotationPrice());
			    imPriceList.setPriceId(imItemPriceView.getPriceId());
			    imPriceList.setForeignCost(imItemPriceView.getSupplierQuotationPrice()); // 用商品主檔的原廠報價當作原幣成本

			    // 若匯入的台幣成本不存在則撈原有的做換算
			    if(null == imPriceList.getLocalCost()){
				imPriceList.setLocalCost(NumberUtils.getDouble(imItemPriceView.getSupplierQuotationPrice() * Double.valueOf(exchangeRate))); //台幣成本
			    }
			    log.info("台幣成本:" + imPriceList.getForeignCost());

			}else{
			    log.info("匯入的品號:"+itemCode+"不存在");
			}
		    }else if(PAJ.equals(orderTypeCode)){
			ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findOneCurrentPriceByProperty(brandCode, priceType, itemCode);

			if(imItemCurrentPriceView != null){
			    imPriceList.setPriceId(imItemCurrentPriceView.getPriceId());
			    imPriceList.setOriginalQuotationPrice(imItemCurrentPriceView.getSupplierQuotationPrice()); // 原廠報價(舊)
			    imPriceList.setOriginalPrice(imItemCurrentPriceView.getUnitPrice());  // 原價

			    String oldUnitPrice = AjaxUtils.getPropertiesValue(imItemCurrentPriceView.getUnitPrice(), "0");
			    String oldSupplierQuotationPrice = AjaxUtils.getPropertiesValue(imItemCurrentPriceView.getSupplierQuotationPrice(), "0");
			    String newUnitPrice = AjaxUtils.getPropertiesValue(imPriceList.getUnitPrice(),"0");
			    String newQuotationPrice = AjaxUtils.getPropertiesValue(imPriceList.getNewQuotationPrice(),"0");

			    String grossProfitRate = calGrossProfitRate(oldUnitPrice,oldSupplierQuotationPrice,exchangeRate,newUnitPrice,newQuotationPrice);
			    imPriceList.setGrossProfitRate(NumberUtils.getDouble(grossProfitRate));	// 毛利率差異
			}else{
			    log.info("匯入的品號:"+itemCode+"不存在");
			}
		    }

		    imPriceList.setImPriceAdjustment(imPriceAdjustmentTmp);
		    imPriceList.setIndexNo(i + 1L);
		    imPriceListDAO.save(imPriceList);
		}
	    }
	}catch (Exception ex) {
	    log.error("商品定價變價明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("商品定價變價明細匯入時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 定變價反轉
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Object executeReverter(Long headId, String employeeCode)throws Exception{
	ImPriceAdjustment head = null;
	Date now = new Date();
	try {
	    head = findById(headId);


	    String brandCode = head.getBrandCode();
	    String orderTypeCode = head.getOrderTypeCode();
	    String orderNo = head.getOrderNo();
	    Date enbaleDate = head.getEnableDate();
	    String beforeStatus = head.getStatus();

	    // 檢核啟用日期日期大於等於當天,狀態為簽核完成才能反轉
	    if( !(OrderStatus.FINISH.equalsIgnoreCase(beforeStatus)) ){
		throw new Exception("品牌: " + brandCode + " 單別: " + orderTypeCode +  " 單號: " + orderNo + " 狀態不為FINISH，無法反轉");
	    }
	    if(enbaleDate.before(now)){
		throw new Exception("品牌: " + brandCode + " 單別: " + orderTypeCode +  " 單號: " + orderNo + " 啟用日期("+DateUtils.format(enbaleDate, DateUtils.C_DATA_PATTON_YYYYMMDD)+")小於現在日期("+DateUtils.format(now, DateUtils.C_DATA_PATTON_YYYYMMDD)+")，無法反轉");
	    }
	    if( !PAJ.equalsIgnoreCase(orderTypeCode) && !PAP.equalsIgnoreCase(orderTypeCode) ){
		throw new Exception("查無單別 :"+orderTypeCode+"的反轉");
	    }

	    // 檢核成功則設定下個狀態
	    setReverseNextStatus(head);

	    // 更新調整單主檔明細檔
	    updateImPriceAdjustment(head, employeeCode );
	    // 依訂價還是變價反轉
	    if(PAP.equalsIgnoreCase(orderTypeCode)){
		updateStandardPrice(headId, true );
	    }else if(PAJ.equalsIgnoreCase(orderTypeCode)){
		updateAdjustPrice(headId, true );
	    }

	    // 清掉流程
	    head.setProcessId(null);

	    log.info("head.status = " + head.getStatus() );

	    return head;
	} catch (Exception e) {
	    log.error("定變價反轉時發生錯誤，原因：" + e.toString());
	    throw new Exception(e.getMessage());
	}
    }

    /**
     * 執行反確認流程起始
     *
     * @param parameterMap
     * @return List<Properties>
     */
    public void executeReverterProcess(Object bean) throws Exception {
	ImPriceAdjustment head = (ImPriceAdjustment)bean;

	Object processObj[] = ImPriceAdjustmentMainService.startProcess(head);
	head.setProcessId((Long)processObj[0]);
	imPriceAdjustmentDAO.update(head);
    }

    /**
     * 更新bean
     * @param head
     */
    public void update(ImPriceAdjustment head) {
	try {
	    imPriceAdjustmentDAO.update(head);
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("e = " + e.toString());
	}
    }

    /**
     * 更新PAGE 所有的LINE
     *
     * @param httpRequest
     * @return String message
     * @throws FormException
     * @throws NumberFormatException
     */
    public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws NumberFormatException, FormException, Exception {
	log.info("==========<updateAJAXPageLinesData>=============");
	String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

	Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	String status = httpRequest.getProperty("status");
	String brandCode = httpRequest.getProperty("brandCode");
	String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	//Double exchangeRate = NumberUtils.getDouble(httpRequest.getProperty("exchangeRate"));
	String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	String errorMsg = null;

	Date date = new Date();

	log.info("orderTypeCode =" + orderTypeCode);
	log.info("gridLineFirstIndex=" + gridLineFirstIndex + ",gridRowCount="
		+ gridRowCount + ",headId=" + headId + ",status=" + status + ",gridData=" + gridData);
	try{
	    if (OrderStatus.SAVE.equalsIgnoreCase(status) || OrderStatus.REJECT.equalsIgnoreCase(status) ) {

		// 將STRING資料轉成List Properties record data
		List<Properties> upRecords = null;

		if(brandCode.indexOf("T2") > -1){
		    if(PAP.equals(orderTypeCode)){
			upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
				ImPriceAdjustmentMainService.T2_PAP_GRID_FIELD_NAMES);
		    }else if(PAJ.equals(orderTypeCode)){
			upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
				ImPriceAdjustmentMainService.T2_PAJ_GRID_FIELD_NAMES);
		    }
		}else{
		    if(PAP.equals(orderTypeCode)){
			upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
				ImPriceAdjustmentMainService.GRID_FIELD_NAMES);
		    }else if(PAJ.equals(orderTypeCode)){
			upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
				ImPriceAdjustmentMainService.PAJ_GRID_FIELD_NAMES);
		    }
		}


		// Get INDEX NO
		int indexNo = imPriceListService.findPageLineMaxIndex(headId).intValue();
		if (null != upRecords ) {
		    for (Properties upRecord : upRecords) {
			// 先載入HEAD_ID OR LINE DATA
			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));

			String itemCode = "";
			if(brandCode.indexOf("T2") > -1){
			    if(PAP.equals(orderTypeCode)){
				itemCode = upRecord.getProperty(T2_PAP_GRID_FIELD_NAMES[2]);

			    }else if(PAJ.equals(orderTypeCode)){
				itemCode = upRecord.getProperty(T2_PAJ_GRID_FIELD_NAMES[1]);
			    }
			}else{
			    if(PAP.equals(orderTypeCode)){
				itemCode = upRecord.getProperty(GRID_FIELD_NAMES[2]);

			    }else if(PAJ.equals(orderTypeCode)){
				itemCode = upRecord.getProperty(PAJ_GRID_FIELD_NAMES[1]);
			    }
			}
			log.info( "itemCode = " + itemCode );

			// 如果沒有ITEM CODE 就不會新增或修改
			if (StringUtils.hasText(itemCode)) {
			    log.info("headId=" + headId + " ,lineId=" + lineId);
			    ImPriceList imPriceList = imPriceListService.findLine(headId,lineId);
			    log.info("imPriceList = " + imPriceList);
			    if (null != imPriceList) {
				// UPDATE Properties

				if(brandCode.indexOf("T2") > -1){
				    if(PAP.equals(orderTypeCode)){
					AjaxUtils.setPojoProperties(imPriceList, upRecord, T2_PAP_GRID_FIELD_NAMES, T2_PAP_GRID_FIELD_TYPES);
				    }else if(PAJ.equals(orderTypeCode)){
					AjaxUtils.setPojoProperties(imPriceList, upRecord, T2_PAJ_GRID_FIELD_NAMES, T2_PAJ_GRID_FIELD_TYPES);
				    }
				}else{
				    if(PAP.equals(orderTypeCode)){
					AjaxUtils.setPojoProperties(imPriceList, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
				    }else if(PAJ.equals(orderTypeCode)){
					AjaxUtils.setPojoProperties(imPriceList, upRecord, PAJ_GRID_FIELD_NAMES, PAJ_GRID_FIELD_TYPES);
				    }
				}

				imPriceList.setLastUpdatedBy(loginEmployeeCode);
				imPriceList.setLastUpdateDate(date);
				imPriceListDAO.update(imPriceList);
			    } else {
				// INSERT Properties
				indexNo++;
				imPriceList = new ImPriceList();

				if(brandCode.indexOf("T2") > -1){
				    if(PAP.equals(orderTypeCode)){
					AjaxUtils.setPojoProperties(imPriceList, upRecord, T2_PAP_GRID_FIELD_NAMES, T2_PAP_GRID_FIELD_TYPES);
				    }else if(PAJ.equals(orderTypeCode)){
					AjaxUtils.setPojoProperties(imPriceList, upRecord, T2_PAJ_GRID_FIELD_NAMES, T2_PAJ_GRID_FIELD_TYPES);
				    }
				}else{
				    if(PAP.equals(orderTypeCode)){
					AjaxUtils.setPojoProperties(imPriceList, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
				    }else if(PAJ.equals(orderTypeCode)){
					AjaxUtils.setPojoProperties(imPriceList, upRecord, PAJ_GRID_FIELD_NAMES, PAJ_GRID_FIELD_TYPES);
				    }
				}
				imPriceList.setImPriceAdjustment(new ImPriceAdjustment(headId, brandCode));

				imPriceList.setCreatedBy(loginEmployeeCode);
				imPriceList.setCreationDate(date);
				imPriceList.setLastUpdatedBy(loginEmployeeCode);
				imPriceList.setLastUpdateDate(date);
				imPriceList.setIndexNo(Long.valueOf(indexNo));

				imPriceListDAO.save(imPriceList);
			    }
			}
		    }
		} else {
		    errorMsg = "沒有定價單單頭資料";
		}
	    } else {
		// 如果狀態不是暫存 是否要提示訊息
	    }

	}catch (Exception ex) {
	    log.error("商品定變價明細更新發生錯誤，原因：" + ex.toString());
	    throw new Exception("商品定變價明細更新發生錯誤，原因：" + ex.getMessage());
	}
	log.info("==========</updateAJAXPageLinesData>=============");
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * 檢核,存取主檔,明細檔,重新設定狀態
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public Map updateAJAXImPriceAdjustment(Map parameterMap)throws FormException,Exception{
//	List<Properties> result = new ArrayList();

//	StringBuffer retMessage = new StringBuffer();
	Map resultMap = new HashMap();
	List errorMsgs = null;
	String resultMsg = null;
//	Properties pros = new Properties();

//	Long processId = NumberUtils.getLong( (String)PropertyUtils.getProperty(otherBean, "processId"));
	try{
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
	    String beforeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeStatus");

	    ImPriceAdjustment head = getActualImPriceAdjustment(formLinkBean);
	    
	    List<ImPriceList> imPriceLists = head.getImPriceLists();
	    
//	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imPriceObject);
//	    String tmpStuts = imMovementService.getStatus(formAction, imPriceObject.getHeadId(), processId, imPriceObject.getStatus(), "", imPriceObject.getOrderTypeCode(), imPriceObject.getOrderNo());
//	    imPriceObject.setStatus(tmpStuts);

	    // 檢核
	    if( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ){
		    //檢核簽核日是否大於生效日
		    //log.info(DateUtils.daysBetween(head.getEnableDate(), DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, DateUtils.format(DateUtils.getCurrentDate(),DateUtils.C_TIME_PATTON_DEFAULT ))));
		    //log.info("日期差異為" +  DateUtils.format(DateUtils.getCurrentDate(),DateUtils.C_TIME_PATTON_DEFAULT ));
	    	//==========2014/03/10==========
	   /* 	for (Iterator iterator = imPriceLists.iterator(); iterator.hasNext();) {
    			ImPriceList imPriceList = (ImPriceList) iterator.next();	

    			String beginDate = DateUtils.format(head.getEnableDate(),DateUtils.C_DATA_PATTON_YYYYMMDD);
    			String endDate = DateUtils.format(head.getEnableDate(),DateUtils.C_DATA_PATTON_YYYYMMDD);

    			String tmpSqlItem= "select ITEM_CODE, BRAND_CODE, STATUS,TO_CHAR(BEGIN_DATE,'YYYYMMDD') AS BEGIN_DATE, TO_CHAR(END_DATE,'YYYYMMDD') AS END_DATE from IM_PROMOTION_DETAIL_VIEW WHERE ITEM_CODE ='" +imPriceList.getItemCode() +"' and BRAND_CODE = '"+head.getBrandCode()+"' and  STATUS = 'FINISH' AND BEGIN_DATE <= TO_DATE('"+beginDate+"','yyyyMMdd') AND END_DATE >= TO_DATE('"+endDate+"','yyyyMMdd')";           

    			List itemList = nativeQueryDAO.executeNativeSql(tmpSqlItem);
    			for (Iterator iteratorV = itemList.iterator(); iteratorV.hasNext();) {
    				Object[] obj = (Object[])iteratorV.next();
    				
    				if(null != obj )
    					//throw new Exception("注意!!此變價單簽核完成會產生新促銷單");
    					javax.swing.JOptionPane.showMessageDialog(null,"注意!!此變價單簽核完成會產生新促銷單!");
    				
    			}			
	    	}*/
	    	
		    int checkSignsingDateBetweenValiddate = 0;
		    checkSignsingDateBetweenValiddate = 
		    	DateUtils.daysBetweenWithoutTime(head.getEnableDate(), DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, DateUtils.format(DateUtils.getCurrentDate(),DateUtils.C_TIME_PATTON_DEFAULT )));
		    //定價單啟用日要 >= 簽核日
		    //變價單啟用日要 >  簽核日
		    if( ((checkSignsingDateBetweenValiddate >= 0) && (head.getOrderTypeCode().equals(PAJ))) ||    //變價單啟用日要 >  簽核日 
		    	((checkSignsingDateBetweenValiddate >  0) && (head.getOrderTypeCode().equals(PAP))) ){    //定價單用日要   >= 簽核日
		    	//加入檢查是否按駁回
		    	//approvalResult
		    	Boolean result = Boolean.valueOf(approvalResult);
		    	//不是選駁回就按送出則出現錯誤.
		    	if( result ){
		    		if(head.getOrderTypeCode().equals(PAJ)){
		    			throw new Exception("簽核日已超過啟用日期或等於啟用日期, 須駁回修正啟用日期, 並重簽核.");
		    		}else{
		    			throw new Exception("簽核日已超過啟用日期, 須駁回修正啟用日期, 並重簽核.");
		    		}
		    	}
		    }
			deleteLine(head);
			log.info("===<check data>===");
			errorMsgs  = checkedImPriceAdjustment(parameterMap);
			log.info("===<check data/>===");
	    }

	    if( errorMsgs == null || errorMsgs.size() == 0 ){

		// 設定單號,createDate
		setOrderNo(head);

		// 設定下一個狀態
		setNextStatus(head, formAction, approvalResult);

//		modifyImPriceAdjustmentHead(head, employeeCode, formAction);

		// 若為變價則更新主旨,第一次主旨是在一開始起流程顯示的
		updateSubject(head,beforeStatus);

		// 更新主檔明細檔
		if( ( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ) ){

		    // 更新定變單主檔明細檔
		    updateImPriceAdjustment( head, employeeCode );

		}
		resultMsg = head.getOrderTypeCode() + "-" + head.getOrderNo() + "存檔成功！ 是否繼續新增？";

	    } else if( errorMsgs.size() > 0 ){
		if( OrderStatus.FORM_SUBMIT.equals(formAction) ){
		    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		}
	    }

	    resultMap.put("entityBean", head);
	    resultMap.put("resultMsg", resultMsg);
	} catch( ValidationErrorException ve ){
	    log.error("定變價單檢核時發生錯誤，原因：" + ve.toString());
	    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	} catch (Exception e) {
	    log.error("定變價單存檔時發生錯誤，原因：" + e.toString());
	    throw new Exception("調整單存檔時發生錯誤，原因：" + e.getMessage());
	}
	return resultMap;

    }

    /**
     * 更新定變價的 line
     * @param headId
     * @param result
     * @throws Exception
     */
    private void updateLine(Map parameterMap, List<Properties> result)throws Exception{
	try {
	    Properties httpProperties = new Properties();

	    Object vatBeanOther = parameterMap.get("vatBeanOther");
	    String brandCode = (String)PropertyUtils.getProperty(vatBeanOther, "loginBrandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(vatBeanOther, "orderTypeCode");
	    String priceStatus = (String)PropertyUtils.getProperty(vatBeanOther, "priceStatus");
	    String priceType = (String)PropertyUtils.getProperty(vatBeanOther, "priceType");
	    Long headId = NumberUtils.getLong((String)PropertyUtils.getProperty(vatBeanOther, "headId"));
	    String exchangeRate = (String)PropertyUtils.getProperty(vatBeanOther, "exchangeRate");
	    String ratio = (String)PropertyUtils.getProperty(vatBeanOther, "ratio");

	    log.info( "headId =" + headId );
	    log.info( "brandCode =" + brandCode );
	    log.info( "orderTypeCode =" + orderTypeCode );
	    log.info( "priceStatus =" + priceStatus );
	    log.info( "priceType =" + priceType );
	    log.info( "exchangeRate =" + exchangeRate );
	    log.info( "ratio =" + ratio );

	    httpProperties.setProperty("brandCode", brandCode);
	    httpProperties.setProperty("orderTypeCode", orderTypeCode);
	    httpProperties.setProperty("priceStatus", priceStatus);
	    httpProperties.setProperty("priceType", priceType);
	    httpProperties.setProperty("exchangeRate", exchangeRate);
	    httpProperties.setProperty("ratio", ratio);
	    httpProperties.setProperty("itemCode", "");

	    ImPriceAdjustment head = imPriceAdjustmentDAO.findById(headId);
	    List<ImPriceList> imPriceLists = head.getImPriceLists();

	    Long indexNo =  (imPriceLists != null && imPriceLists.size() > 0) ? (imPriceLists.get(imPriceLists.size() - 1 ).getIndexNo()) + 1L : 1L;

	    for (Properties properties : result) {
		Long priceId = NumberUtils.getLong((String)properties.getProperty("priceId"));
		log.info( "priceId =" + priceId );
		httpProperties.setProperty("priceId", priceId.toString());

		Object resultItem = getItemInfo(httpProperties);
		if(PAP.equals(orderTypeCode)){ // 訂價單
		    String itemCode = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[0] : null, "");
		    String itemCName = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[1] : null,MessageStatus.DATA_NOT_FOUND);
		    String category01 = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[3] : null, "" );
		    String unitPrice = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[6] : null , "0");
		    String foreignCost = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[4] : null, "0");
		    String localCost = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[5] : null,"0");
		    String costRate = calCostRate(localCost,unitPrice);

		    log.info( "itemCode = " + itemCode);
		    log.info( "category01 = " + category01);
		    log.info( "foreignCost = " + foreignCost);
		    log.info( "localCost = " + localCost);
		    log.info( "costRate = " + costRate);

		    ImPriceList imPriceList = new ImPriceList();
		    imPriceList.setItemCode(itemCode);
		    imPriceList.setItemCName(itemCName);
		    imPriceList.setCategory01(category01);
		    imPriceList.setUnitPrice(NumberUtils.getDouble(unitPrice));
		    imPriceList.setForeignCost(NumberUtils.getDouble(foreignCost));
		    imPriceList.setLocalCost(NumberUtils.getDouble(localCost));
		    imPriceList.setCostRate(NumberUtils.getDouble(costRate));

		    imPriceList.setPriceId(priceId);
		    imPriceList.setIndexNo(indexNo++);
		    imPriceList.setImPriceAdjustment(head);
		    imPriceAdjustmentDAO.save(imPriceList);
		}else if(PAJ.equals(orderTypeCode)) {
		    // 變價單
		    String itemCName = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[0] : null,MessageStatus.DATA_NOT_FOUND);
		    String unitPrice = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[2] : null, "0");
		    String suppierQuotationPrice = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[1] : null,"0");
		    String isTax = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[4] : null, "1");
		    String typeCode = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[5] : null, "1");
		    String taxCode = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[6] : null, "3");
		    String itemCode = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[7] : null, "");

		    ImPriceList imPriceList = new ImPriceList();
		    imPriceList.setItemCode(itemCode);
		    imPriceList.setItemCName(itemCName);
		    imPriceList.setUnitPrice(NumberUtils.getDouble(unitPrice));
		    imPriceList.setOriginalPrice(NumberUtils.getDouble(unitPrice));
		    imPriceList.setOriginalQuotationPrice(NumberUtils.getDouble(suppierQuotationPrice));
		    imPriceList.setNewQuotationPrice(NumberUtils.getDouble(suppierQuotationPrice));

		    log.info( "itemCode = " + itemCode);

		    imPriceList.setPriceId(priceId);
		    imPriceList.setIndexNo(indexNo++);
		    imPriceList.setImPriceAdjustment(head);

		    String grossProfitRate = calGrossProfitRate(unitPrice,suppierQuotationPrice,exchangeRate,unitPrice,"0");
		    imPriceList.setGrossProfitRate(NumberUtils.getDouble(grossProfitRate));
		    imPriceList.setIsTax(isTax);
		    imPriceList.setTypeCode(typeCode);
		    imPriceList.setTaxCode(taxCode);

		    imPriceAdjustmentDAO.save(imPriceList);
		}
	    }
	} catch (Exception e) {
	    log.error("更新定變價明細發生問題" + e.toString());
	    throw new Exception("更新定變價明細發生問題" + e.getMessage());
	}
    }

    /**
     * 更新定變價的 line for T2
     * @param headId
     * @param result
     * @throws Exception
     */
    private void updateT2Line(Map parameterMap, List<Properties> result)throws Exception{
	try {
//	    Properties httpProperties = new Properties();

	    Object vatBeanOther = parameterMap.get("vatBeanOther");
	    String brandCode = (String)PropertyUtils.getProperty(vatBeanOther, "loginBrandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(vatBeanOther, "orderTypeCode");
	    String priceStatus = (String)PropertyUtils.getProperty(vatBeanOther, "priceStatus");
	    String priceType = (String)PropertyUtils.getProperty(vatBeanOther, "priceType");
	    Long headId = NumberUtils.getLong((String)PropertyUtils.getProperty(vatBeanOther, "headId"));
//	    String exchangeRate = (String)PropertyUtils.getProperty(vatBeanOther, "exchangeRate");

	    log.info( "headId =" + headId );
	    log.info( "brandCode =" + brandCode );
	    log.info( "orderTypeCode =" + orderTypeCode );
	    log.info( "priceStatus =" + priceStatus );
	    log.info( "priceType =" + priceType );

//	    httpProperties.setProperty("brandCode", brandCode);
//	    httpProperties.setProperty("orderTypeCode", orderTypeCode);
//	    httpProperties.setProperty("priceStatus", priceStatus);
//	    httpProperties.setProperty("priceType", priceType);

//	    httpProperties.setProperty("itemCode", "");

	    ImPriceAdjustment head = imPriceAdjustmentDAO.findById(headId);
	    List<ImPriceList> imPriceLists = head.getImPriceLists();

	    Long indexNo =  (imPriceLists != null && imPriceLists.size() > 0) ? (imPriceLists.get(imPriceLists.size() - 1 ).getIndexNo()) + 1L : 1L;

	    for (Properties properties : result) {
		Long priceId = NumberUtils.getLong((String)properties.getProperty("priceId"));
		log.info( "priceId =" + priceId );
//		httpProperties.setProperty("priceId", priceId.toString());
//		httpProperties.setProperty("exchangeRate", exchangeRate);
//		Object resultItem = getT2ItemInfo(httpProperties);

		if(PAP.equals(orderTypeCode)){ // 訂價單
		    ImItemPriceView imItemPriceView = (ImItemPriceView)imItemPriceViewDAO.findById(priceId);

		    if( imItemPriceView != null ){
			String currencyCode = imItemPriceView.getPurchaseCurrencyCode();
			// 依幣別撈匯率
			String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
			String exchangeRate = buBasicDataService.getExchangeRateByCurrencyCode(organizationCode,currencyCode);

			String itemCode = AjaxUtils.getPropertiesValue( imItemPriceView.getItemCode(), "");
			String category02 = AjaxUtils.getPropertiesValue( imItemPriceView.getCategory02(), "" );
			String unitPrice = AjaxUtils.getPropertiesValue( imItemPriceView.getUnitPrice(), "0");
			String foreignCost = AjaxUtils.getPropertiesValue( imItemPriceView.getPurchaseAmount(), "0");
			String localCost = String.valueOf(NumberUtils.getDouble(foreignCost) * NumberUtils.getDouble(exchangeRate));
			localCost = AjaxUtils.getPropertiesValue( localCost,"0");

			log.info( "itemCode = " + itemCode);
			log.info( "category02 = " + category02);
			log.info( "foreignCost = " + foreignCost);
			log.info( "exchangeRate =" + exchangeRate );
			log.info( "localCost = " + localCost);

			ImPriceList imPriceList = new ImPriceList();
			imPriceList.setItemCode(itemCode);
			imPriceList.setCurrencyCode(currencyCode);
			imPriceList.setExchangeRate(NumberUtils.getDouble(exchangeRate));
			imPriceList.setCategory02(category02);
			imPriceList.setUnitPrice(NumberUtils.getDouble(unitPrice));
			imPriceList.setForeignCost(NumberUtils.getDouble(foreignCost));
			imPriceList.setLocalCost(NumberUtils.getDouble(localCost));

			imPriceList.setPriceId(priceId);
			imPriceList.setIndexNo(indexNo++);
			imPriceList.setImPriceAdjustment(head);
			imPriceAdjustmentDAO.save(imPriceList);
		    }

//		    String itemCode = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[0] : null, "");
//		    String itemCName = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[1] : null,MessageStatus.DATA_NOT_FOUND);
//		    String category01 = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[3] : null, "" );
//		    String unitPrice = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[6] : null , "0");
//		    String foreignCost = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[4] : null, "0");
//		    String localCost = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[5] : null,"0");
//		    String grossRate = calGrossRate(localCost,unitPrice);
//		    log.info( "grossRate = " + grossRate);
//		    imPriceList.setGrossRate(NumberUtils.getDouble(grossRate)); // !!!

		}else if(PAJ.equals(orderTypeCode)) {
		    ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findById(priceId);

		    if(imItemCurrentPriceView != null){
			String currencyCode = imItemCurrentPriceView.getPurchaseCurrencyCode();
			// 依幣別撈匯率
			String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
			String exchangeRate = buBasicDataService.getExchangeRateByCurrencyCode(organizationCode,currencyCode);

			String itemCode = AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getItemCode(), "");
			String originalCurrencyCode = AjaxUtils.getPropertiesValue( currencyCode, "");
			String originalForeignCost = AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getPurchaseAmount(), "0");
			String originalPrice = AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getUnitPrice(), "0");

			log.info( "itemCode = " + itemCode);
			log.info( "originalCurrencyCode = " + originalCurrencyCode);
			log.info( "originalForeignCost = " + originalForeignCost);
			log.info( "originalPrice =" + originalPrice );
			log.info( "exchangeRate = " + exchangeRate);

			ImPriceList imPriceList = new ImPriceList();
			imPriceList.setItemCode(itemCode);
			imPriceList.setOriginalCurrencyCode(originalCurrencyCode);
			imPriceList.setCurrencyCode(originalCurrencyCode);
			imPriceList.setExchangeRate(NumberUtils.getDouble(exchangeRate));
			imPriceList.setOriginalForeignCost(NumberUtils.getDouble(originalForeignCost));
			imPriceList.setForeignCost(NumberUtils.getDouble(originalForeignCost));
			imPriceList.setOriginalPrice(NumberUtils.getDouble(originalPrice));
			imPriceList.setUnitPrice(NumberUtils.getDouble(originalPrice));

			imPriceList.setPriceId(priceId);
			imPriceList.setIndexNo(indexNo++);
			imPriceList.setImPriceAdjustment(head);

			imPriceList.setIsTax(imItemCurrentPriceView.getIsTax());
			imPriceList.setTypeCode(imItemCurrentPriceView.getTypeCode());
			imPriceList.setTaxCode(imItemCurrentPriceView.getTaxCode());

			imPriceAdjustmentDAO.save(imPriceList);
		    }

		    // 變價單
//		    String itemCName = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[0] : null,MessageStatus.DATA_NOT_FOUND);
//		    String unitPrice = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[2] : null, "0");
//		    String suppierQuotationPrice = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[1] : null,"0");
//		    String isTax = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[4] : null, "1");
//		    String typeCode = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[5] : null, "1");
//		    String taxCode = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[6] : null, "3");
//		    String itemCode = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[7] : null, "");
//		    String isTax = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[4] : null, "1");
//		    String typeCode = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[5] : null, "1");
//		    String taxCode = AjaxUtils.getPropertiesValue( resultItem != null ? ((Object[])resultItem)[6] : null, "3");

		}
	    }
	} catch (Exception e) {
	    log.error("更新定變價明細發生問題" + e.toString());
	    throw new Exception("更新定變價明細發生問題" + e.getMessage());
	}
    }

    /**
     * 啟動ImItemPrice之價格，於Process中使用
     *
     * @param headId
     * @throws Exception
     */
    public void updateStandardPrice(Long headId) throws Exception {
	updateStandardPrice(headId,false);
    }

    /**
     * 啟動ImItemPrice之價格，於Process中使用
     *
     * @param headId
     * @throws Exception
     */
    public void updateStandardPrice(Long headId,boolean isRevert) throws Exception {
	try {
	    ImPriceAdjustment imPriceAdjustment = imPriceAdjustmentDAO.findById(headId);

	    if (imPriceAdjustment != null) {
		java.util.Date enableDate = imPriceAdjustment.getEnableDate();
		List<ImPriceList> imPriceLists = imPriceAdjustment.getImPriceLists();
		//啟用價格，並依啟用單上之價格更新
		for (ImPriceList imPriceList : imPriceLists) {

		    ImItemPrice imItemPrice = imItemPriceDAO.findById(imPriceList.getPriceId());

		    log.info("enableDate = " + enableDate);
		    log.info("headId = " + headId);
		    log.info("imPriceList.getForeignCost() = " + imPriceList.getForeignCost());
		    log.info("imPriceList.getLocalCost() = " + imPriceList.getLocalCost());

		    if(!isRevert){
			if (null != imItemPrice ) {
			    imItemPrice.setBeginDate(enableDate);
			    imItemPrice.setUnitPrice(imPriceList.getUnitPrice());
			    imItemPrice.setEnable("Y");
			    imItemPrice.setPriceAdjustId(headId);
			    imItemPrice.setLastUpdateDate(new Date());
			    imItemPrice.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
			    imItemPriceDAO.update(imItemPrice);
			} else {
			    throw new NoSuchDataException("查無商品價格(ImItemPrice)，ID:"
				    + imPriceList.getPriceId() + "，標準售價啟動失敗");
			}
			//更改標準進貨成本
			ImItem imItem = imItemDAO.findItem(imPriceAdjustment.getBrandCode(), imPriceList.getItemCode());
			if (imItem != null) {
			    if(imPriceAdjustment.getBrandCode().indexOf("T2") > -1){
			    imItem.setPurchaseAmount(imPriceList.getForeignCost()); // 修正成原幣成本
			    }else{
				imItem.setStandardPurchaseCost(imPriceList.getLocalCost());
			    }

			    imItem.setLastUpdateDate(new Date());
			    imItem.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
			    imItemDAO.update(imItem);
			}
		    }else{
			if (null != imItemPrice ) {
			    imItemPrice.setBeginDate(null);  // 清除啟用日期
			    imItemPrice.setEnable("N");	     // 還原成停用
			    imItemPrice.setPriceAdjustId(null);	// 清除
			    imItemPrice.setLastUpdateDate(new Date());
			    imItemPrice.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
			    imItemPriceDAO.update(imItemPrice);
			} else {
			    throw new NoSuchDataException("查無商品價格(ImItemPrice)，ID:"
				    + imPriceList.getPriceId() + "，標準售價啟動失敗");
			}
		    }
		}
	    } else {
		throw new NoSuchDataException("查無商品定價單(ImPriceAdjustment)，ID:"
			+ headId + "，標準售價啟動失敗");
	    }
	} catch (Exception ex) {
	    log.error("ID:" + headId + ",標準售價啟動時發生錯誤，原因：" + ex.toString());
	    throw new Exception("ID:" + headId + ",標準售價啟動時發生錯誤，原因："
		    + ex.getMessage());
	}
    }

    /**
     * 啟動ImItemPrice之價格，於Process中使用
     *
     * @param headId
     * @throws Exception
     */
    public String updateAdjustPrice(Long headId) throws Exception {
	return updateAdjustPrice(headId, false);
    }

    /**
     * 啟動ImItemPrice之價格，於Process中使用
     *
     * @param headId
     * @throws Exception
     */
	public String updateAdjustPrice(Long headId, boolean isRevert) throws Exception {
		log.info("=====<updateAdjustPrice>=======");
		try {
			String result = null;
			ImPriceAdjustment imPriceAdjustment = imPriceAdjustmentDAO.findById(headId);
			Date date = new Date();
			String enableDate = DateUtils.format(imPriceAdjustment.getEnableDate(), DateUtils.C_DATE_PATTON_SLASH);			
			String toDate = DateUtils.format(date, DateUtils.C_DATE_PATTON_SLASH);
			
			if (imPriceAdjustment != null) {
				List<ImPriceList> imPriceLists = imPriceAdjustment.getImPriceLists();
				// 啟用價格，並依啟用單上之價格更新
				for (ImPriceList imPriceList : imPriceLists) {
					if (!isRevert) {
						// 更新ImItemPrice
						updateImItemPrice(result, headId, imPriceAdjustment, imPriceList);
						// 更新ImItem 20141120 修改為 異動成本時 啟用日不是今日 不會時即回寫 改由排程去撈啟用日去回寫 Mark
						if(enableDate.equals(toDate)){
							updateImItem(result, imPriceAdjustment, imPriceList);
						}else{
							//updateImItem(result, imPriceAdjustment, imPriceList);	
						}
						

					} else { // 反轉
						// 更新ImItemPrice
						updateRevertImItemPrice(result, headId, imPriceAdjustment, imPriceList);
						// 更新ImItem
						updateRevertImItem(result, imPriceAdjustment, imPriceList);
					}
				}
			} else {
				throw new NoSuchDataException("查無變價單(ImPriceAdjustment)，ID:" + headId + "，變價作業啟動失敗");
			}
			log.info("=====<updateAdjustPrice/>=======");
			return result;
		} catch (Exception ex) {
			log.error("ID:" + headId + ",變價作業啟動時發生錯誤，原因：" + ex.toString());
			throw new Exception("ID:" + headId + ",變價作業啟動時發生錯誤，原因：" + ex.getMessage());
		}
	}

    /**
     * 更新ImItemPrice
     *
     * @param headId
     * @throws Exception
     */
	public void updateImItemPrice(String result, Long headId, ImPriceAdjustment imPriceAdjustment, ImPriceList imPriceList)
			throws Exception {
		java.util.Date enableDate = imPriceAdjustment.getEnableDate();
		// 如果新價格<>原價才進行修改
		if (imPriceList.getOriginalPrice() != imPriceList.getUnitPrice()) {
			log.info("imPriceList.getItemCode() = " + imPriceList.getItemCode());
			log.info("imPriceAdjustment.getPriceType() = " + imPriceAdjustment.getPriceType());
			log.info("imPriceAdjustment.getEnableDate() = " + enableDate);
			// 查看資料庫中是否有已生效的價格資訊，如果有即使用此筆資料進行更新
			// 如果沒有，使用ItemCode將整筆Item取出，並在Price中新增一筆價格資料
			// 此用意為如價格在經過第一次調整後，user發現價格有誤時，
			// user可以再次新建一張變價單進行更新
			// 需增加判斷品牌(brandCode)，避免不同品牌，品號相同，發生資料修改錯誤的狀況 modify by Weichun 2011.06.10
			ImItemPrice imItemPrice = imItemPriceDAO.findEnablePrice(imPriceList.getItemCode(), imPriceAdjustment
					.getPriceType(), enableDate, imPriceAdjustment.getBrandCode());
			log.info("imItemPrice = " + imItemPrice);

			int tryTimes = 3; // 嘗試連接次數
			int interval = 5000; // 等待時間
			for (int i = 0; i < tryTimes; i++) { // 如果更新失敗，經過5秒後重試 add by Weichun 2012.03.12
				try {
					if (imItemPrice == null) { // 如果沒有品號相同、啟用日期相同的資料，則新增一筆，否則就更新 by Weichun 2011.06.10
						log.info("imPriceAdjustment.getBrandCode() = " + imPriceAdjustment.getBrandCode());
						log.info("imPriceList.getItemCode() = " + imPriceList.getItemCode());
						ImItem imItem = imItemDAO.getLockedImItem(imPriceAdjustment.getBrandCode(),
								imPriceList.getItemCode());
						List<ImItemPrice> imItemPrices = imItem.getImItemPrices();
						Long indexNo = imItemPrices.get(imItemPrices.size() - 1).getIndexNo() + 1L;
						// 更改標準進貨成本

						log.info("imItem = " + imItem);
						if (imItem != null) {
							ImItemPrice newImItemPrice = new ImItemPrice();
							log.info("test5");
							newImItemPrice.setBrandCode(imPriceAdjustment.getBrandCode());
							newImItemPrice.setBeginDate(enableDate);
							newImItemPrice.setPriceAdjustId(headId);

							log.info("test6");
							newImItemPrice.setItemCode(imPriceList.getItemCode());
							newImItemPrice.setTypeCode(imPriceAdjustment.getPriceType());
							newImItemPrice.setTaxCode(imPriceList.getTaxCode());
							newImItemPrice.setIsTax(imPriceList.getIsTax());
							newImItemPrice.setUnitPrice(imPriceList.getUnitPrice());
							newImItemPrice.setCurrencyCode(imPriceList.getCurrencyCode()); // 幣別

							log.info("test7");
							newImItemPrice.setItemId(imItem.getItemId());
							newImItemPrice.setSalesUnit(imItem.getSalesUnit());

							log.info("test8");
							newImItemPrice.setEnable("Y");
							newImItemPrice.setIndexNo(indexNo);
							newImItemPrice.setCreationDate(new Date());
							newImItemPrice.setCreatedBy(imPriceAdjustment.getLastUpdatedBy());
							newImItemPrice.setLastUpdateDate(new Date());
							newImItemPrice.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
							imItemPriceDAO.save(newImItemPrice);

						} else {
							throw new NoSuchDataException("查無商品資訊(ImItem)，ID:" + imPriceList.getItemCode() + "，變價作業啟動失敗");
						}
						break;
					} else {
						imItemPrice.setPriceAdjustId(headId);
						imItemPrice.setUnitPrice(imPriceList.getUnitPrice());
						imItemPrice.setBeginDate(enableDate);
						imItemPrice.setEnable("Y");
						imItemPrice.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
						imItemPrice.setLastUpdateDate(new Date());
						imItemPriceDAO.update(imItemPrice);
						result = "imItemPriceDAO.update";
					}
				} catch (Exception e) {
					try {
						Thread.sleep(interval);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
					if (i == tryTimes - 1) {
						// 價格更新失敗通知資訊部失敗原因 add by Weichun 2012.03.13
						String errorMsg = "更新商品價格時(updateImItemPrice." + imItemPrice.getItemCode() + ")發生錯誤，原因:\n" + e;
						log.error(errorMsg);
						List mailAddressList = new ArrayList();
						mailAddressList.add("weichun_liao@tasameng.com.tw");
						// mailAddressList.add("IT-DG@tasameng.com.tw");
						String templateFileName = "CommonTemplate.ftl";
						Map content = new HashMap();
						content.put("display", errorMsg);
						MailUtils.sendMail("[重要通知]定變價單據更新價格檔失敗", templateFileName, content, mailAddressList);
					}
				}
			}
		}
	}

    /**
     * 反轉更新ImItemPrice
     *
     * @param headId
     * @throws Exception
     */
	public void updateRevertImItemPrice(String result, Long headId, ImPriceAdjustment imPriceAdjustment,
			ImPriceList imPriceList) throws Exception {
		java.util.Date enableDate = imPriceAdjustment.getEnableDate();
		// 如果新價格<>原價才進行修改
		if (imPriceList.getOriginalPrice() != imPriceList.getUnitPrice()) {
			log.info("imPriceList.getItemCode() = " + imPriceList.getItemCode());
			log.info("imPriceAdjustment.getPriceType() = " + imPriceAdjustment.getPriceType());
			log.info("imPriceAdjustment.getEnableDate() = " + enableDate);
			// 查看資料庫中是否有已生效的價格資訊，如果有即使用此筆資料進行更新
			// 如果沒有，使用ItemCode將整筆Item取出，並在Price中新增一筆價格資料
			// 此用意為如價格在經過第一次調整後，user發現價格有誤時，
			// user可以再次新建一張變價單進行更新
			// 需增加判斷品牌(brandCode)，避免不同品牌，品號相同，發生資料修改錯誤的狀況 modify by Weichun 2011.06.10
			ImItemPrice imItemPrice = imItemPriceDAO.findEnablePrice(imPriceList.getItemCode(), imPriceAdjustment
					.getPriceType(), enableDate, imPriceAdjustment.getBrandCode());
			log.info("imItemPrice = " + imItemPrice);

			if (null != imItemPrice) {
				imItemPrice.setPriceAdjustId(headId);
				imItemPrice.setUnitPrice(imPriceList.getOriginalPrice()); // 還原上次價錢
				imItemPrice.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
				imItemPrice.setLastUpdateDate(new Date());
				imItemPriceDAO.update(imItemPrice);
				result = "imItemPriceDAO.update";
			}
		}
	}

    /**
     * 更新ImItem
     *
     * @param headId
     * @throws Exception
     */
    public void updateImItem(String result, ImPriceAdjustment imPriceAdjustment,ImPriceList imPriceList) throws Exception {
	if(imPriceAdjustment.getBrandCode().indexOf("T2") > -1){
	    ImItem imItem = imItemDAO.getLockedImItem(imPriceAdjustment.getBrandCode(), imPriceList.getItemCode());
	    // 修改原幣幣別
	    if(imPriceList.getOriginalCurrencyCode() != imPriceList.getCurrencyCode()){
		imItem.setPurchaseCurrencyCode(imPriceList.getCurrencyCode());
	    }
	    if(imPriceList.getOriginalPrice() != imPriceList.getUnitPrice()){	    
		imItem.setPurchaseAmount(imPriceList.getForeignCost());
	    }
	    imItem.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
	    imItem.setLastUpdateDate(new Date());
	    imItemDAO.update(imItem);
	    result =  "imItemDAO.update";
	}else{
	    //更改原廠 報價欄位
	    if(imPriceList.getOriginalQuotationPrice() != imPriceList.getNewQuotationPrice()){
		ImItem imItem = imItemDAO.getLockedImItem(imPriceAdjustment.getBrandCode(), imPriceList.getItemCode());
		if (imItem != null) {
		    imItem.setSupplierQuotationPrice(imPriceList.getNewQuotationPrice());
		    imItem.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
		    imItem.setLastUpdateDate(new Date());
		    imItemDAO.update(imItem);
		    result =  "imItemDAO.update";
		} else {
		    throw new NoSuchDataException("查無商品資訊(ImItem)，ID:"
			    + imPriceList.getItemCode() + "，變價作業啟動失敗(查無商品資料)");
		}
	    }
	}
    }

    /**
     * 反轉更新ImItem
     *
     * @param headId
     * @throws Exception
     */
    public void updateRevertImItem(String result, ImPriceAdjustment imPriceAdjustment,ImPriceList imPriceList) throws Exception {
	if(imPriceAdjustment.getBrandCode().indexOf("T2") > -1){
	    ImItem imItem = imItemDAO.getLockedImItem(imPriceAdjustment.getBrandCode(), imPriceList.getItemCode());
	    // 修改原幣幣別
	    if(imPriceList.getOriginalCurrencyCode() != imPriceList.getCurrencyCode()){
		imItem.setPurchaseCurrencyCode(imPriceList.getOriginalCurrencyCode());		// 還原原始幣別
	    }
	    log.info("imPriceList.getOriginalPrice()="+imPriceList.getOriginalPrice()+"<<<>>>"+"imPriceList.getUnitPrice()="+imPriceList.getUnitPrice());
	    // 修改原幣成本
	    if(imPriceList.getOriginalPrice() != imPriceList.getUnitPrice()){
	    	log.info("TMMark3");
		imItem.setPurchaseAmount(imPriceList.getOriginalForeignCost());			// 還原原幣成本
	    }
	    imItem.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
	    imItem.setLastUpdateDate(new Date());
	    imItemDAO.update(imItem);
	    result =  "imItemDAO.update";
	}else{
	    //更改原廠 報價欄位
	    if(imPriceList.getOriginalQuotationPrice() != imPriceList.getNewQuotationPrice()){
		ImItem imItem = imItemDAO.getLockedImItem(imPriceAdjustment.getBrandCode(), imPriceList.getItemCode());
		if (imItem != null) {
		    imItem.setSupplierQuotationPrice(imPriceList.getOriginalQuotationPrice());	// 還原原廠報價
		    imItem.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
		    imItem.setLastUpdateDate(new Date());
		    imItemDAO.update(imItem);
		    result =  "imItemDAO.update";
		} else {
		    throw new NoSuchDataException("查無商品資訊(ImItem)，ID:"
			    + imPriceList.getItemCode() + "，變價作業啟動失敗(查無商品資料)");
		}
	    }
	}
    }

    public List<Properties> updateSearchSelection(Map parameterMap) throws FormException, Exception{
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    Object vatBeanOther = parameterMap.get("vatBeanOther");
	    String brandCode = (String)PropertyUtils.getProperty(vatBeanOther, "loginBrandCode");

	    String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);

	    log.info("getSearchSelection.picker_parameter:" + timeScope +"/"+ searchKeys.toString());

	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    log.info("getSearchSelection.result:" + result.size());
	    if(result.size() > 0){
		pickerResult.put("result", result);
		// 直接加 在 定變價 line
		if(brandCode.indexOf("T2") <= -1){
		    updateLine(parameterMap,result);
		}else if(brandCode.indexOf("T2") > -1){
		    updateT2Line(parameterMap,result);
		}
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
     * 訂變價單 line picker 全選
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public List<Properties> updateAllSearchData(Map parameterMap) throws Exception {
	Map resultMap = new HashMap(0);
	List views = null;
	Map viewMap = null;
	int iSPage  = -1;
	int iPSize  = -1;

	log.info(parameterMap.keySet().toString());
//	Object pickerBean = parameterMap.get("vatBeanPicker");
	Object formBindBean = parameterMap.get("vatBeanFormBind");
	Object otherBean = parameterMap.get("vatBeanOther");

	try{
	    String timeScope = (String)PropertyUtils.getProperty(otherBean, AjaxUtils.TIME_SCOPE);
	    String isAllClick = (String)PropertyUtils.getProperty(otherBean, "isAllClick");
	    log.info("timeScope:"+timeScope);
	    log.info("isAllClick:"+isAllClick);

	    // 所有預查詢的條件
	    String brandCode =(String)PropertyUtils.getProperty(otherBean,"loginBrandCode");// 品牌代號
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean,"orderTypeCode");// 單別
	    String startItemCode = (String)PropertyUtils.getProperty(formBindBean,"startItemCode");
//	    String endItemCode = (String)PropertyUtils.getProperty(formBindBean,"endItemCode");
	    String itemName = (String)PropertyUtils.getProperty(formBindBean,"itemName");
	    String priceType = (String)PropertyUtils.getProperty(formBindBean,"priceType");
	    String priceStatus = (String)PropertyUtils.getProperty(otherBean,"priceStatus");
	    String supplierCode = (String)PropertyUtils.getProperty(otherBean,"supplierCode");

	    Map findObjs = new HashMap();

	    findObjs.put(" and model.brandCode = :brandCode",brandCode);

	    findObjs.put(" and model.itemCode like :startItemCode","%"+startItemCode+"%");
	    findObjs.put(" and model.itemCName like :itemName","%"+itemName+"%");
	    findObjs.put(" and model.typeCode = :priceType",priceType);

	    if( brandCode.indexOf("T2") > -1){
		String itemBrand = (String)PropertyUtils.getProperty(formBindBean,"itemBrand");
		String category01 = (String)PropertyUtils.getProperty(formBindBean,"category01");
		String category02 = (String)PropertyUtils.getProperty(formBindBean,"category02");
		String itemCategory = (String)PropertyUtils.getProperty(formBindBean,"itemCategory");

		findObjs.put(" and model.itemBrand like :itemBrand","%"+itemBrand+"%");
		findObjs.put(" and model.category01 = :category01",category01);
		findObjs.put(" and model.category02 = :category02",category02);
		findObjs.put(" and model.itemCategory = :itemCategory",itemCategory);
		findObjs.put(" and model.category17 = :supplierCode", supplierCode);
	    }

	    if(PAP.equals(orderTypeCode)){
		findObjs.put(" and model.priceEnable = :priceStatus",priceStatus);
		findObjs.put(" and model.itemEnable = :itemEnable","Y");
	    }
	    //==============================================================

	    if(PAP.equals(orderTypeCode)){
		viewMap = imItemPriceViewDAO.search( "ImItemPriceView as model", "model.priceId", findObjs, "order by priceId", iSPage, iPSize, BaseDAO.QUERY_SELECT_ALL );
		views = (List<ImItemPriceView>) viewMap.get(BaseDAO.TABLE_LIST);
	    }else if(PAJ.equals(orderTypeCode)){
		viewMap = imItemCurrentPriceViewDAO.search( "ImItemCurrentPriceView as model", "model.priceId", findObjs, "order by priceId", iSPage, iPSize, BaseDAO.QUERY_SELECT_ALL );
		views = (List<ImItemCurrentPriceView>) viewMap.get(BaseDAO.TABLE_LIST);
	    }

	    if(views.size()>0)
		AjaxUtils.updateAllResult(timeScope,isAllClick,views);


	}catch (Exception e) {
	    log.error("全選更新發生問題，原因：" + e.toString());
	    throw new Exception(e.getMessage());
	}

	return AjaxUtils.parseReturnDataToJSON(resultMap);
    }

    /**
     * 前端資料塞入bean
     * @param parameterMap
     * @return
     */
    public Map updateImPriceAdjustmentBean(Map parameterMap)throws FormException, Exception {
	Map resultMap = new HashMap();
	try{
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    
	    String supplierCode = (String)PropertyUtils.getProperty(formBindBean, "supplierCode");
	    
	    //取得欲更新的bean
	    ImPriceAdjustment head = getActualImPriceAdjustment(formLinkBean);
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, head);
	    head.setSupplierCode(supplierCode);
       
	    resultMap.put("entityBean", head);
	    return resultMap;
	} catch (FormException fe) {
	    log.error("前端資料塞入bean失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
	    throw new Exception("調整單資料塞入bean發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     *  取單號後更新主檔
     *
     * @param parameterMap
     * @return Map
     * @throws FormException
     * @throws Exception
     */
    public Map updateImPriceAdjustmentWithActualOrderNO(Map parameterMap) throws FormException, Exception {

	Map resultMap = new HashMap();
	String resultMsg = null;
	try{

	    resultMap = updateImPriceAdjustmentBean(parameterMap);
	    ImPriceAdjustment head = (ImPriceAdjustment) resultMap.get("entityBean");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
	    
	    //檢核簽核日是否大於生效日
	    //log.info(DateUtils.daysBetween(head.getEnableDate(), DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, DateUtils.format(DateUtils.getCurrentDate(),DateUtils.C_TIME_PATTON_DEFAULT ))));
	    //log.info("日期差異為" +  DateUtils.format(DateUtils.getCurrentDate(),DateUtils.C_TIME_PATTON_DEFAULT ));

	    int checkSignsingDateBetweenValiddate = 0;
	    checkSignsingDateBetweenValiddate = 
	    	DateUtils.daysBetweenWithoutTime(head.getEnableDate(), DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, DateUtils.format(DateUtils.getCurrentDate(),DateUtils.C_TIME_PATTON_DEFAULT )));
	    //定價單啟用日要 >= 簽核日 
	    //變價單啟用日要 >  簽核日
	    if( ((checkSignsingDateBetweenValiddate >= 0) && (head.getOrderTypeCode().equals(PAJ))) ||    //變價單啟用日要 >  簽核日 
	    	((checkSignsingDateBetweenValiddate >  0) && (head.getOrderTypeCode().equals(PAP))) ){    //定價單用日要   >= 簽核日
	    	//加入檢查是否按駁回
	    	//approvalResult
	    	Boolean result = Boolean.valueOf(approvalResult);
	    	//不是選駁回就按送出則出現錯誤.
	    	if( result ){
	    		if(head.getOrderTypeCode().equals(PAJ)){
	    			throw new Exception("簽核日已超過啟用日期或等於啟用日期, 須駁回修正啟用日期, 並重簽核.");
	    		}else{
	    			throw new Exception("簽核日已超過啟用日期, 須駁回修正啟用日期, 並重簽核.");
	    		}
	    	}
	    }

	    //刪除於SI_PROGRAM_LOG的原識別碼資料
	    String identification = MessageStatus.getIdentification(head.getBrandCode(),
		    head.getOrderTypeCode(), head.getOrderNo());
	    siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

	    setOrderNo(head);
	    resultMsg = head.getOrderTypeCode() + "-" + head.getOrderNo() + "存檔成功！ 是否繼續新增？";

	    resultMap.put("resultMsg", resultMsg);

	    return resultMap;
	} catch (FormException fe) {
	    log.error("定變價存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("定變價存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("定變價存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 更新訂價單主檔明細檔
     * @param head
     * @param employeeCode
     */
    private void updateImPriceAdjustment(ImPriceAdjustment head, String employeeCode )throws Exception{
	try{
	    Date date = new Date();
	    if (head.getHeadId() != null) {
	    
		head.setLastUpdateDate(date);
		head.setLastUpdatedBy(employeeCode);
		//log.info("百貨業種判斷:"+head.getBrandCode());
//		if(head.getBrandCode().equals("T1BS")){
//			log.info("百貨業種判斷:"+head.getBrandCode());
//	      	head.setItemCategory("T1BS");
//	    }else if(head.getBrandCode().equals("T1GS")){
//	    	log.info("百貨業種判斷:"+head.getBrandCode());
//	       	head.setItemCategory("T1GS");
//	    }else if(head.getBrandCode().equals("T3PE")){
//	    	log.info("百貨業種判斷:"+head.getBrandCode());
//	       	head.setItemCategory("T3PE");
//	    }else if(head.getBrandCode().equals("T3CO")){
//	    	log.info("百貨業種判斷:"+head.getBrandCode());
//	       	head.setItemCategory("T3CO");
//	    }else if(head.getBrandCode().equals("T3CU")){
//	    	log.info("百貨業種判斷:"+head.getBrandCode());
//	       	head.setItemCategory("T3CU");
//	    }
		
		if(!head.getBrandCode().equals("T2")){
			log.info("百貨業種判斷:"+head.getBrandCode());
			head.setItemCategory(head.getBrandCode());
		}
		
		List<ImPriceList> lines = head.getImPriceLists();
		for (ImPriceList imPriceList : lines) {
		    imPriceList.setLastUpdateDate(date);
		    imPriceList.setLastUpdatedBy(employeeCode);
		}

		imPriceAdjustmentDAO.update(head);
	    }
	}catch (Exception ex) {
	    log.error("定變價主檔明細檔存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("定變價主檔明細檔存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 更新訂價單主檔明細檔
     * @param head
     * @param employeeCode
     */
    private void updateSubject(ImPriceAdjustment head,String beforeStatus)throws Exception{
	String categroyName = "";
	String supplierName = "";
	StringBuffer subject = new StringBuffer();
	try{
	    log.info("head.getProcessId() = " + head.getProcessId());
	    log.info("head.getOrderTypeCode() = " + head.getOrderTypeCode());
	    log.info("head.getStatus() = " + head.getStatus());
	    log.info("beforeStatus = " + beforeStatus);

	    // 若為變價則更新主旨 第一次主旨是在一開始起流程顯示的
	    if(head.getBrandCode().indexOf("T2") > - 1){
		if(null != head.getProcessId()){
		    if( (OrderStatus.SAVE.equalsIgnoreCase(head.getStatus()) && OrderStatus.SAVE.equalsIgnoreCase(beforeStatus)) ||
			    (OrderStatus.SIGNING.equalsIgnoreCase(head.getStatus()) && OrderStatus.SAVE.equalsIgnoreCase(beforeStatus)) ||
			    (OrderStatus.SAVE.equalsIgnoreCase(head.getStatus()) && OrderStatus.REJECT.equalsIgnoreCase(beforeStatus)) ||
			    (OrderStatus.SIGNING.equalsIgnoreCase(head.getStatus()) && OrderStatus.REJECT.equalsIgnoreCase(beforeStatus)) ){
			// 	取得業種子類名稱
			ImItemCategory imItemCategory = imItemCategoryDAO.findByCategoryCode(head.getBrandCode(), ImItemCategoryDAO.ITEM_CATEGORY, head.getItemCategory(),"Y");
			if(null != imItemCategory ){
			    ImItemCategory parentImItemCategory = imItemCategoryDAO.findByCategoryCode(head.getBrandCode(), ImItemCategoryDAO.CATEGORY00, imItemCategory.getParentCategoryCode(),"Y");
			    if(null != parentImItemCategory){
				categroyName = parentImItemCategory.getCategoryName() + " - ";
			    }
			}

			// 取得廠商名稱
			BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(head.getBrandCode(), head.getSupplierCode());
			if(null!=buSWAV){
			    supplierName = " - " + buSWAV.getChineseName();
			}

			subject.append(categroyName)
			.append(MessageStatus.getJobManagerMsg(head.getBrandCode(),head.getOrderTypeCode(),head.getOrderNo()))
			.append(supplierName);
			ceapProcessService.updateProcessSubject( head.getProcessId(), subject.toString());
		    }
		}
	    }
	}catch (Exception ex) {
	    log.error("定變價更新主旨時發生錯誤，原因：" + ex.toString());
	    throw new Exception("定變價更新主旨時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 檢核主檔,明細檔
     * @param Head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validateImPriceAdjustment(ImPriceAdjustment head, String programId, String identification, List errorMsgs, Object formLinkBean) throws ValidationErrorException, NoSuchObjectException {
	validteHead(head, programId, identification, errorMsgs, formLinkBean);
	validteLine(head, programId, identification, errorMsgs);
    }

    /**
     * 檢核主檔
     * @param head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validteHead(ImPriceAdjustment head, String programId, String identification, List errorMsgs, Object formLinkBean) throws ValidationErrorException, NoSuchObjectException {
	String message = null;
	String tabName = "主檔資料";
	try {

	    if (null == head.getEnableDate()){
		message = "請輸入" + tabName + "的啟用日期";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	    if (!StringUtils.hasText(head.getBrandCode())){
		message = "請輸入" + tabName + "的品牌代號";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	    if (!StringUtils.hasText(head.getPriceType())){
		message = "請輸入" + tabName + "的價格類別";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }

	    if (!StringUtils.hasText(head.getSupplierCode())){
//		message = "請輸入" + tabName + "的供應商代號";
//		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//		errorMsgs.add(message);
//		log.error(message);
	    }else{
		BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewDAO.findById(head.getSupplierCode() , head.getBrandCode());
		if( null == buSupplierWithAddressView ){
		    message = tabName + "的供應商代號錯誤，請重新輸入!";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}

		if(head.getBrandCode().indexOf("T2") <= -1){
		    if(!(StringUtils.hasText(head.getCurrencyCode()))){
			message = "請輸入" + tabName + "的幣別資料";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		    }
		}
	    }

	    if(head.getBrandCode().indexOf("T2") > -1){
		if(!(StringUtils.hasText(head.getItemCategory()))){
		    message = "請輸入" + tabName + "的業種";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}
		if(!(StringUtils.hasText(head.getPurchaseAssist()))){
		    message = "請輸入" + tabName + "的採購助理";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}
		if(!(StringUtils.hasText(head.getPurchaseMember()))){
		    message = "請輸入" + tabName + "的採購人員";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}
		if(!(StringUtils.hasText(head.getPurchaseMaster()))){
		    message = "請輸入" + tabName + "的採購主管";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}
	    }

	} catch (Exception e) {
	    message = "檢核定變價主檔單" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }

    /**
     * 檢核明細檔
     * @param head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validteLine(ImPriceAdjustment head, String programId,
	    String identification, List errorMsgs) throws ValidationErrorException, NoSuchObjectException {
	String message = null;
	String tabName = "明細資料頁籤";
	int i = 1;
	Map itemCodeMap = new HashMap();
	try{
	    String brandCode = head.getBrandCode();
	    String orderTypeCode = head.getOrderTypeCode();
	    String orderNo = head.getOrderNo();
	    String priceType = head.getPriceType();
	    String itemCategory = head.getItemCategory(); // 業種子類
	    String supplierCode = head.getSupplierCode();

	    List<ImPriceList> lines = head.getImPriceLists();

	    int size = lines.size();
	    if( size > 0 ){
		for (ImPriceList imPriceList : lines) {
		    String itemCode = imPriceList.getItemCode();
		    Double unitPrice = imPriceList.getUnitPrice();
		    if (StringUtils.hasText(itemCode)) {

			// 檢核價錢
			if( imPriceList.getUnitPrice() < 0d ){
			    message = tabName + "中，第" + i + "筆資料("+ itemCode + ")，售價:"+unitPrice+" 必須大於0";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);
			}else{
			    if(ValidateUtil.isDecimal(unitPrice)){
				message = tabName + "中，第" + i + "筆資料("+ itemCode + ")，售價:"+unitPrice+" 不得含有小數點";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }
			}
			
			// 檢查 商品不得為促銷中
			log.info("check Info");
			/*訂變價檢察是否有促銷先註解掉(wade 10/30)
			List promotions = imPromotionDAO.findPromotionInfo(head.getEnableDate(), brandCode, null, itemCode, priceType);
			if ((null != promotions) && (promotions.size() > 0)) {
			    Object pmo[] = (Object[]) promotions.get(0);
			    String promotionCode = (String) pmo[0];
			    message = tabName + "中，第" + i + "筆資料("+ itemCode+ ")，已於促銷中(促銷代號:"+promotionCode+")，無法定變價";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}
			*/

			if (PAJ.equals(orderTypeCode)) {
			    List<ImItemCurrentPriceView> imItemCurrentPriceView = imItemCurrentPriceViewDAO.findCurrentPriceByValue(brandCode, imPriceList
				    .getItemCode(), itemCode,
				    priceType, "");
			    if (imItemCurrentPriceView.size() == 0) {
				message = tabName + "中，第" + i + "筆資料("+ itemCode+ ")，查無已啟用之商品資料";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }

			    if( brandCode.indexOf("T2") > -1 && imItemCurrentPriceView != null && imItemCurrentPriceView.size() > 0 ){
				ImItemCurrentPriceView view = imItemCurrentPriceView.get(0);
				if(!view.getItemCategory().equals(itemCategory)){
				    message = tabName + "中，第" + i + "筆資料("+ itemCode + ")，不屬於業種子類:" + itemCategory;
				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				    errorMsgs.add(message);
				    log.error(message);
				}

			    }
			} else if (PAP.equals(orderTypeCode)) {
			    List<ImItemPriceView> imItemPriceView = imItemPriceViewDAO
			    .findPriceViewByValue(brandCode, imPriceList.getItemCode(),
				    itemCode, "", "", priceType, "N",
				    "", "");
			    if (imItemPriceView.size() == 0) {
				message = tabName + "中，第" + i + "筆資料("+ imPriceList.getItemCode() + ")，查無未啟用之商品資料";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }
			    if( brandCode.indexOf("T2") > -1 && imItemPriceView != null && imItemPriceView.size() > 0 ){
				ImItemPriceView view = imItemPriceView.get(0);
				if(!view.getItemCategory().equals(itemCategory)){
				    message = tabName + "中，第" + i + "筆資料("+ itemCode + ")，不屬於業種子類:" + itemCategory;
				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				    errorMsgs.add(message);
				    log.error(message);
				}

			    }

			} else if ("CIA".equals(orderTypeCode)) {
			    List<ImItemPriceView> imItemPriceView = imItemPriceViewDAO
			    .findPriceViewByValue(brandCode, imPriceList.getItemCode(),
				    imPriceList.getItemCode(), "", "", priceType, "N",
				    "", "and model.isComposeItem = 'Y'");
			    if (imItemPriceView.size() == 0) {
				message = "組合商品送簽單" + orderTypeCode + "-"+ orderNo + "中，第" + i + "筆資料("+ imPriceList.getItemCode() + ")，查無未啟用之組合商品資料";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }
			} else {
			    message = "單別資料錯誤「" + orderTypeCode + "」";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);
			}

			// 檢核廠商代號一致
			if(brandCode.indexOf("T2") > -1 && StringUtils.hasText(supplierCode)){
			    ImItem imItem = imItemDAO.findItem(brandCode, itemCode);
			    if(imItem != null){
				if(!supplierCode.equals(imItem.getCategory17())){
				    message = tabName + "中第" + i + "項明細不屬於製造商/供應商" + supplierCode;
				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				    errorMsgs.add(message);
				    log.error(message);
				}
			    }
			}


			// 檢核重複
			if( itemCodeMap.containsKey( itemCode ) ){
//			    orderNoMap.put( indexNo+orderNo ,  indexNo );
			    message = tabName + "中第" + i + "項明細的" + itemCode + "重複！";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);
			}else{
			    itemCodeMap.put( itemCode ,  i );
			}

		    } else {
			message = "請輸入" + tabName + "中第" + i + "項明細的商品";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		    }
		    i++;
		}
	    }else{
		message = tabName + "中請至少輸入一筆明細！";
		log.info( "至少輸入一筆明細 message = " + message );
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	} catch (Exception e) {
	    message = "檢核單明細檔" + tabName + "時發生錯誤，原因：" + e.toString();
	    log.info( "exceptiion message = " + message );
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }
    
    
    
    //=======2014/2/27變價完成產生新促銷單=======
    public Long createPromotion(Long headId)throws Exception {
    	log.info("createPromotion");
    	try{
    		ImPriceAdjustment imPriceAdjustment = imPriceAdjustmentDAO.findById(headId);
    		List<ImPriceList> imPriceLists = imPriceAdjustment.getImPriceLists();  
    		ImPromotion imPromotion = new ImPromotion();
    		String orderTypeCode = "PMO";
    		String orderNo = buOrderTypeService.getOrderSerialNo(imPriceAdjustment.getBrandCode(), orderTypeCode); 
    		//BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(imPromotion.getBrandCode(), orderTypeCode ) );
    		log.info("ImPromotionSetHead");			
    		imPromotion.setBeginDate(imPriceAdjustment.getEnableDate());
    		imPromotion.setPriceType(imPriceAdjustment.getPriceType());
    		imPromotion.setBrandCode(imPriceAdjustment.getBrandCode());
    		imPromotion.setItemCategory(imPriceAdjustment.getItemCategory());
    		imPromotion.setStatus("SAVE");
    		imPromotion.setOrderNo(orderNo);
    		imPromotion.setOrderTypeCode(orderTypeCode);
    		imPromotion.setInCharge(imPriceAdjustment.getCreatedBy());
    		imPromotion.setIsAllCustomer("N");
    		imPromotion.setIsAllItem("N");
    		imPromotion.setIsAllShop("N");
    		imPromotion.setCustomerType("1");
    		imPromotion.setCreatedBy(imPriceAdjustment.getCreatedBy());
    		imPromotion.setCreationDate(  DateUtils.parseDate(DateUtils.format(new Date())));
    		imPromotion.setLastUpdateDate(DateUtils.parseDate(DateUtils.format(new Date())));

    		List<ImPromotionItem> imPromotionItems = imPromotion.getImPromotionItems();
    		
    		for (Iterator iterator = imPriceLists.iterator(); iterator.hasNext();) {
    			ImPriceList imPriceList = (ImPriceList) iterator.next();	

    			String beginDate = DateUtils.format(imPriceAdjustment.getEnableDate(),DateUtils.C_DATA_PATTON_YYYYMMDD);
    			String endDate = DateUtils.format(imPriceAdjustment.getEnableDate(),DateUtils.C_DATA_PATTON_YYYYMMDD);

    			String tmpSqlItem= "select ITEM_CODE, BRAND_CODE, STATUS,TO_CHAR(BEGIN_DATE,'YYYYMMDD') AS BEGIN_DATE, TO_CHAR(END_DATE,'YYYYMMDD') AS END_DATE from IM_PROMOTION_DETAIL_VIEW WHERE ITEM_CODE ='" +imPriceList.getItemCode() +"' and BRAND_CODE = '"+imPriceAdjustment.getBrandCode()+"' and  STATUS = 'FINISH' AND BEGIN_DATE <= TO_DATE('"+beginDate+"','yyyyMMdd') AND END_DATE >= TO_DATE('"+endDate+"','yyyyMMdd')";           

    			List itemList = nativeQueryDAO.executeNativeSql(tmpSqlItem);
    			for (Iterator iteratorV = itemList.iterator(); iteratorV.hasNext();) {
    				Object[] obj = (Object[])iteratorV.next();

    				if(null != obj ){
    					imPromotion.setEndDate(DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD,(String)obj[4]));   		
    					Long indexNo     = 1L;
    					ImPromotionItem imPromotionItem = new ImPromotionItem();
    					imPromotionItem.setItemCode((String)obj[0]);
    					imPromotionItem.setIndexNo(indexNo);
    					imPromotionItem.setCreationDate(  DateUtils.parseDate(DateUtils.format(new Date())));
    					imPromotionItem.setLastUpdateDate(DateUtils.parseDate(DateUtils.format(new Date())));
    					imPromotionItems.add(imPromotionItem);
    					indexNo++;
    				}
    				break;
    			}			
    		}
    		if(imPromotionItems.size()>0){
    			imPromotionDAO.save(imPromotion);
    			return imPromotion.getHeadId();
    		}else{  
    		return null;
    		}
    	} catch (Exception ex) {
    		log.error("createPromotion : 產生促銷單時發生錯誤，原因：" + ex.toString());
    		throw new Exception("createPromotion : 新增促銷單存檔時發生錯誤，原因：" + ex.getMessage());
    	}
    }
    
    public void updatePriceAdjustmentPAJ()throws Exception{
    	String result = null;
    	
    	List<ImPriceAdjustment> imPriceAdjustments =imPriceAdjustmentDAO.findPriceAdjustmentPAJ();
    	List<ImPriceList> imPriceLists = null;
    	log.info("imPriceAdjustments.size()==="+imPriceAdjustments.size()); //當日變價單數量
    	if (imPriceAdjustments != null && imPriceAdjustments.size() > 0) {
	    	for(ImPriceAdjustment imPriceAdjustment:imPriceAdjustments){	    		
	    		imPriceLists = imPriceAdjustment.getImPriceLists();	    		
	    		if (imPriceLists != null && imPriceLists.size() > 0) {
	    			log.info("imPriceLists.size()==="+imPriceLists.size());  //變價單內明細數量
	    			for(ImPriceList imPriceList:imPriceLists){
	    				if(imPriceAdjustment.getBrandCode().indexOf("T2") > -1){
		    				if(!imPriceList.getForeignCost().equals(imPriceList.getOriginalForeignCost())){
		    					log.info("imPriceList.getForeignCost()==="+imPriceList.getForeignCost());
			    				this.updateImItem(result, imPriceAdjustment, imPriceList);
		    				}
	    				}else{
	    					this.updateImItem(result, imPriceAdjustment, imPriceList);
	    				}	
	    			}	    			
	    		}
	    	}
    	}
    }
    
}