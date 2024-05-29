package tw.com.tm.erp.hbm.service; 

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.FiBudgetHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.dao.CmDeclarationItemDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveItemDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveInvoiceDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderLineDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceHeadDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceLineDAO;
import tw.com.tm.erp.hbm.dao.SiProgramLogDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;
import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.hbm.service.ImReceiveHeadMainService;

/**Invoice Head Service
 * @author MyEclipse Persistence Tools
 */
public class FiInvoiceHeadMainService {
	private static final Log log = LogFactory.getLog(FiInvoiceHeadMainService.class);
	public static final String PROGRAM_ID= "FI_INVOICE_HEAD";
	private FiInvoiceHeadDAO               fiInvoiceHeadDAO;
	private FiInvoiceLineDAO               fiInvoiceLineDAO;
	private BuBasicDataService             buBasicDataService;
	private FiInvoiceLineMainService       fiInvoiceLineMainService;
	private BuOrderTypeService             buOrderTypeService;
	private BuCommonPhraseService          buCommonPhraseService;
	private BuBrandService                 buBrandService;
	private PoPurchaseOrderHeadMainService poPurchaseOrderHeadMainService;
	private PoPurchaseOrderLineMainService poPurchaseOrderLineMainService;
	private ImReceiveItemMainService       imReceiveItemMainService;
	private ImItemService                  imItemService;
	private ImReceiveHeadDAO               imReceiveHeadDAO;
	private ImReceiveItemDAO               imReceiveItemDAO;
	private ImReceiveInvoiceDAO            imReceiveInvoiceDAO;
	private PoPurchaseOrderHeadDAO         poPurchaseOrderHeadDAO;
	private PoPurchaseOrderLineDAO         poPurchaseOrderLineDAO;
	private SiProgramLogAction             siProgramLogAction;
	private SiProgramLogDAO                siProgramLogDAO;
	private NativeQueryDAO                 nativeQueryDAO;
	private CmDeclarationItemDAO 		   cmDeclarationItemDAO;
	private ImReceiveHeadMainService       imReceiveHeadMainService;
	public static final String[] GRID_FIELD_NAMES = { 
		"indexNo",                   "customSeq",                 "sourceOrderTypeCode",       "poPurchaseOrderHeadId",
		"sourceOrderNo",   	     "poPurchaseOrderLineId",     "shippingMark",              "originalDeclarationNo",
		"originalDeclarationSeq",    "itemCode",                  "supplierItemCode",	       "itemCName",
		"purchaseUnit","category15","specWeight",              "quantity",                  "foreignUnitPrice",          "foreignAmount",
		"localUnitPrice",            "localAmount",               "weight",                    "remark",
		"lineId",                    "isLockRecord",              "isDeleteRecord",   	       "message" };

	public static final int[] GRID_FIELD_TYPES = { 
		AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING };

	public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
	    	"0", "0", "",  "0",  
	    	"",  "0", "",   "",
	    	"0", "",  "",   "",
	    	"",  "",  "",   "0", "0", "0",
	    	"0", "0", "0",  "",
	    	"0", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "" };
	
	
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"orderTypeCode", "invoiceNo", "invoiceDate",    "supplierCode", 
		"supplierName",  "status",    "lastUpdateDate", "headId", };

	public static final int[] GRID_SEARCH_FIELD_TYPES = {
	        AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_STRING, 
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_LONG };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "", "", "", "", "", "", "0" }; 
	
	
	
	public static final String[] GRID_SEARCH_PoH_FIELD_NAMES = { 
		"orderTypeCode", "orderNo", "supplierCode", "supplierName", "status", "lastUpdateDate", "headId", };

	public static final int[] GRID_SEARCH_PoH_FIELD_TYPES = {
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,   AjaxUtils.FIELD_TYPE_LONG };

	public static final String[] GRID_SEARCH_PoH_FIELD_DEFAULT_VALUES = { "", "", "", "", "", "", "0" }; 
	
	
	
	public static final String[] GRID_SEARCH_PoL_FIELD_NAMES = { 
		"orderNo",     "orderTypeCode", "supplierCode",   "supplierName",
		"reserve1",    "reserve4",      "reserve2",       "asignedBudget",
		"totalBudget", "reserve3",      "status",        "lastUpdateDate", "headId"          };

	public static final int[] GRID_SEARCH_PoL_FIELD_TYPES = {
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, 
		AjaxUtils.FIELD_TYPE_LONG };

	public static final String[] GRID_SEARCH_PoL_FIELD_DEFAULT_VALUES = { "", "", "", "", "", "", "", "", "", "", "", "", "0" };
	
	/**
	 * save and update
	 * 
	 * @param modifyObj
	 * @return
	 * @throws Exception
	 */
	public String create(FiInvoiceHead modifyObj) throws Exception {
		if (null != modifyObj) {
			setAllDetailByPurchaseOrderNo(modifyObj);
			if (modifyObj.getHeadId() == null) {
				modifyObj.setOrderTypeCode(FiInvoiceHead.FI_INVOICE_ORDER_TYPE);
				return save(modifyObj);
			} else {
				return update(modifyObj);
			}
		}
		return MessageStatus.ERROR + " Can't find FiInvoiceHead";
	}

	/**
	 * save
	 * 
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String save(FiInvoiceHead saveObj) throws Exception {
		log.info("FiInvoiceHeadMainService save start " + saveObj);
		doAllValidate(saveObj);
		//saveObj.setLotNo(buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), saveObj.getOrderTypeCode()));
		saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		fiInvoiceHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;

	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String update(FiInvoiceHead updateObj) throws Exception {
		if (!isUsedInvoice(updateObj.getBrandCode(), updateObj.getInvoiceNo())) {
			doAllValidate(updateObj);
			updateObj.setLastUpdateDate(new Date());
			fiInvoiceHeadDAO.update(updateObj);
		} else {
			throw new FormException("發票已被使用請勿修改");
		}
		return MessageStatus.SUCCESS;

	}

	/**
	 * 利用供應商代號設定 countryCode,currencyCode,exchangeRate
	 * 
	 * @param headObj
	 */
	public void setFormDataBySupplierCode(String organizationCode, FiInvoiceHead headObj) {
		BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(headObj.getBrandCode(), headObj.getSupplierCode());
		if (null != buSWAV) {
			headObj.setCurrencyCode(buSWAV.getCurrencyCode());
			setExchangeRateByCurrencyCode(organizationCode, headObj);
		}
	}

	/**
	 * 利用幣別指定匯率
	 * 
	 * @param headObj
	 */
	public void setExchangeRateByCurrencyCode(String organizationCode, FiInvoiceHead headObj) {
		headObj.setExchangeRate(new Double(1));
		if ((null != organizationCode) && (null != headObj.getCurrencyCode())) {
			BuExchangeRate bxr = buBasicDataService.getLastExchangeRate(organizationCode, headObj.getCurrencyCode(), buCommonPhraseService
					.getBuCommonPhraseLineName("SystemConfig", "LocalCurrency"));
			if (null != bxr) {
				headObj.setExchangeRate(bxr.getExchangeRate());
			}
		}
	}

	/**
	 * 設定Detail的內容
	 * 
	 * @param headObj
	 */
	public void setAllDetail(FiInvoiceHead headObj) {
		log.info("FiInvoiceHeadMainService.setAllDetail");
		for (FiInvoiceLine detail : headObj.getFiInvoiceLines()) {
			log.info("FiInvoiceLine head Id " + detail.getPoPurchaseOrderHeadId());
			fiInvoiceLineMainService.setLinePurchaseOrderData(detail);
		}
	}

	/**
	 * 設定Detail的內容
	 * 
	 * @param headObj
	 * @throws FormException
	 */
	public void setAllDetailByPurchaseOrderNo(FiInvoiceHead headObj) throws FormException {
		for (FiInvoiceLine detail : headObj.getFiInvoiceLines()) {
			fiInvoiceLineMainService.setLinePurchaseOrderDataByPurchaseOrderNo(headObj, detail);
		}
	}

	/**
	 * 移除空白的Detail
	 * 
	 * @param headObj
	 * @return boolean 是否還有 Detail
	 */
	private String checkDetailItemCode(FiInvoiceHead headObj) throws Exception {
		log.info("FiInvoiceHeadMainService.removeNullDetail");
		List<FiInvoiceLine> items = headObj.getFiInvoiceLines();
		Iterator<FiInvoiceLine> it = items.iterator();
		while (it.hasNext()) {
			FiInvoiceLine line = it.next();
			if (!StringUtils.hasText(line.getPurchaseOrderNo())) {
				return MessageStatus.ERROR_NO_DETAIL;
			} else if (null == poPurchaseOrderHeadMainService.findById(line.getPoPurchaseOrderHeadId())) {
				return "請再確認採購單號:" + line.getPurchaseOrderNo();
			}
		}
		return null;
	}

	/**
	 * do master validate
	 * 
	 * @param headObj
	 * @throws FormException,Exception
	 */
	public void doValidate(FiInvoiceHead headObj) throws FormException, Exception {
		log.info("FiInvoiceHeadMainService doValidate " + headObj);
		// 判斷是否重覆
		if (null == headObj.getHeadId() || headObj.getHeadId() == 0) {
			List heads = fiInvoiceHeadDAO.findByProperty("FiInvoiceHead", "invoiceNo", headObj.getInvoiceNo());
			if ((null != heads) && (heads.size() > 0))
				throw new FormException("Invoice No 重覆");
		}
	}

	/**
	 * 檢核
	 * 
	 * @param headObj
	 * @return
	 * @throws Exception
	 */
	private void doAllValidate(FiInvoiceHead headObj) throws FormException, Exception {
		log.info("FiInvoiceHeadMainService doAllValidate " + headObj);
		/*
		 * String errorMsg = checkDetailItemCode(headObj); if ( null !=
		 * errorMsg) { throw new FormException(errorMsg); }
		 */

		// 等到確認才開啟
		doValidate(headObj);

		// 明細確認
		List<FiInvoiceLine> items = headObj.getFiInvoiceLines();
		for (FiInvoiceLine item : items) {
			fiInvoiceLineMainService.doValidate(item);
		}

	}

	/**
	 * 判斷發票是否已經被ImReceive使用掉
	 * 
	 * @param invoiceNo
	 * @return true : 已被使用 , false : 未被使用
	 */
	public boolean isUsedInvoice(String brandCode, String invoiceNo) {
		log.info("FiInvoiceHeadMainService isUsedInvoice " + invoiceNo);
		return imReceiveInvoiceDAO.isUsedInvoice(brandCode, invoiceNo, null);
	}

	/**
	 * find by pk
	 * 
	 * @param headId
	 * @return
	 */
	public FiInvoiceHead findById(Long headId) {
		return (FiInvoiceHead) fiInvoiceHeadDAO.findByPrimaryKey(FiInvoiceHead.class, headId);
	}

	/**
	 * search
	 * 
	 * @param findObjs
	 * @return
	 */
	
	public void setImReceiveHeadMainService(ImReceiveHeadMainService imReceiveHeadMainService) {
		this.imReceiveHeadMainService = imReceiveHeadMainService;
	}
	public List<FiInvoiceHead> find(HashMap findObjs) {
		return fiInvoiceHeadDAO.find(findObjs);
	}

	public void setFiInvoiceHeadDAO(FiInvoiceHeadDAO fiInvoiceHeadDAO) {
		this.fiInvoiceHeadDAO = fiInvoiceHeadDAO;
	}
	
	public void setFiInvoiceLineDAO(FiInvoiceLineDAO fiInvoiceLineDAO) {
		this.fiInvoiceLineDAO = fiInvoiceLineDAO;
	}

	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}

	/*
	 * public void setBuSupplierWithAddressViewDAO(BuSupplierWithAddressViewDAO
	 * buSupplierWithAddressViewDAO) { this.buSupplierWithAddressViewDAO =
	 * buSupplierWithAddressViewDAO; }
	 */
	public void setFiInvoiceLineMainService(FiInvoiceLineMainService fiInvoiceLineMainService) {
		this.fiInvoiceLineMainService = fiInvoiceLineMainService;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setPoPurchaseOrderHeadMainService(PoPurchaseOrderHeadMainService poPurchaseOrderHeadMainService) {
		this.poPurchaseOrderHeadMainService = poPurchaseOrderHeadMainService;
	}
	
	public void setPoPurchaseOrderLineMainService(PoPurchaseOrderLineMainService poPurchaseOrderLineMainService) {
	    this.poPurchaseOrderLineMainService = poPurchaseOrderLineMainService;
	}

	public void setImReceiveItemMainService(ImReceiveItemMainService imReceiveItemMainService) {
	    this.imReceiveItemMainService = imReceiveItemMainService;
	}
	
	public void setImReceiveInvoiceDAO(ImReceiveInvoiceDAO imReceiveInvoiceDAO) {
		this.imReceiveInvoiceDAO = imReceiveInvoiceDAO;
	}

	public void setImReceiveHeadDAO(ImReceiveHeadDAO imReceiveHeadDAO) {
		this.imReceiveHeadDAO = imReceiveHeadDAO;
	}
	
	public void setImReceiveItemDAO(ImReceiveItemDAO imReceiveItemDAO) {
		this.imReceiveItemDAO = imReceiveItemDAO;
	}
	
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	    this.siProgramLogAction = siProgramLogAction;
	}
	
	public ImItemService getImItemService() {
	    return imItemService;
	}

	public void setImItemService(ImItemService imItemService) {
	    this.imItemService = imItemService;
	}
	
	public void setPoPurchaseOrderLineDAO(PoPurchaseOrderLineDAO poPurchaseOrderLineDAO) {
		this.poPurchaseOrderLineDAO = poPurchaseOrderLineDAO;
	}
	
	public void setPoPurchaseOrderHeadDAO(PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO) {
	    this.poPurchaseOrderHeadDAO = poPurchaseOrderHeadDAO;
	}
	
	public SiProgramLogDAO getSiProgramLogDAO() {
	        return siProgramLogDAO;
	}
	
	public void setSiProgramLogDAO(SiProgramLogDAO siProgramLogDAO) {
	    this.siProgramLogDAO = siProgramLogDAO;
	}
	
	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}
	
	public BuBrandService getBuBrandService() {
	    return buBrandService;
	}
	
	public void setBuBrandService(BuBrandService buBrandService) {
	    this.buBrandService = buBrandService;
	}
	
	
    public Long getFiInvoiceHeadId(Object bean) throws FormException, Exception{
	Long headId = null;
	String id = (String)PropertyUtils.getProperty(bean, "headId");
	log.info("getFiInvoiceHeadId:headId=" + id );
	if(StringUtils.hasText(id)){
	    headId = NumberUtils.getLong(id);
	}else{
	    throw new ValidationErrorException("傳入的 Invoice 主鍵為空值！");
	}
	return headId;
    }
    
    
    /** 取單號後更新Invoice主檔
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updateFiInvoiceData(Map parameterMap) throws FormException, Exception {
        HashMap resultMap = new HashMap();
        log.info("updateFiInvoiceData");
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Object otherBean    = parameterMap.get("vatBeanOther");
            Long headId = getFiInvoiceHeadId(formLinkBean); 
            String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
            String invoiceNo = (String)PropertyUtils.getProperty(formLinkBean, "invoiceNo");
            if(!StringUtils.hasText(invoiceNo))
            	throw new Exception("請輸入InvoiceNo");
            //取得欲更新的bean
            FiInvoiceHead fiInvoice = getActualFiInvoice(headId);
            AjaxUtils.copyJSONBeantoPojoBean(formBindBean, fiInvoice);
            modifyFiInvoice( fiInvoice, employeeCode);
            return resultMap;
            
        } catch (FormException fe) {
            log.error("updateFiInvoiceData : Invoice 存檔失敗，原因：" + fe.toString());
            throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("updateFiInvoiceData : Invoice 存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("updateFiInvoiceData : Invoice 存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    
    private FiInvoiceHead getActualFiInvoice(Long headId) throws FormException, Exception{
	log.info("getActualFiInvoice");
	FiInvoiceHead fiInvoice = null;
	fiInvoice = findById(headId);
	if( fiInvoice  == null){
	     throw new NoSuchObjectException("getActualFiInvoice : 查無 Invoice 主鍵：" + headId + "的資料！");
	}
        return fiInvoice;
    }
    
   
    /**更新至FiInvoice主檔
     * @param FiInvoiceHead
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String modifyFiInvoice(FiInvoiceHead fiInvoice, String loginUser)
    	    throws ObtainSerialNoFailedException, FormException, Exception {
	log.info("FiInvoiceHeadMainService.modifyFiInvoice");
	fiInvoice.setLastUpdatedBy(loginUser);
	fiInvoice.setLastUpdateDate(new Date());
	fiInvoiceHeadDAO.update(fiInvoice);
        return fiInvoice.getOrderTypeCode() + "-" + fiInvoice.getInvoiceNo() + "存檔成功！";
    }
    
    
    /**檢核 Invoice 資料
     * @param purchaseHead
     * @param conditionMap
     * @return List
     * @throws ValidationErrorException
     */
    public List updateCheckedFiInvoiceData(Map parameterMap) throws Exception {
	log.info("updateCheckedPurchaseOrderData");
	List errorMsgs = new ArrayList(0);
	try{
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Long headId = getFiInvoiceHeadId(formLinkBean);
            String invoiceNo = (String)PropertyUtils.getProperty(formLinkBean, "invoiceNo");
            FiInvoiceHead fiInvoicePO = getActualFiInvoice(headId);
            
            BuBrand buBrand    = buBrandService.findById( fiInvoicePO.getBrandCode() );
    	    String  branchCode = buBrand.getBuBranch().getBranchCode();	// 是否 T2
            
            //String employeeCode   = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
            String identification = 	// 以 TMP 單號取代 invoiceNo 避免未 KEY invoiceNo 造成所有 error msg 混亂
        	MessageStatus.getIdentification(fiInvoicePO.getBrandCode(), fiInvoicePO.getOrderTypeCode(), fiInvoicePO.getReserve1() );

            // clear 原有 ERROR RECORD 2010.01.29 由 action 取代
            // siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
            // check head Data
            checkFiInvoiceHead( fiInvoicePO, branchCode, PROGRAM_ID, identification, errorMsgs, invoiceNo);
            // check line data
            checkFiInvoiceLines(fiInvoicePO, branchCode, PROGRAM_ID, identification, errorMsgs);
            log.info("updateCheckedPurchaseOrderData END");
	    return errorMsgs;
	}catch (Exception ex) {
	    log.error("Invoice 檢核後存檔失敗，原因：" + ex.toString());
	    throw new Exception("Invoice 檢核後存檔失敗，原因：" + ex.getMessage());
	}
    }

  
    /** 更新採購主檔及明細檔
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updateAJAXFiInvoice(Map parameterMap) throws FormException, Exception {
        HashMap resultMap = new HashMap();
        log.info("updateAJAXFiInvoice");
        try{
	    	Object formBindBean = parameterMap.get("vatBeanFormBind");
	    	Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    	Object otherBean    = parameterMap.get("vatBeanOther");
	    	//String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	    	String formStatus         = (String)PropertyUtils.getProperty(otherBean, "formStatus");
	    	String employeeCode       = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
	    	Long headId = getFiInvoiceHeadId(formLinkBean);
	    	if(!StringUtils.hasText(formStatus)){
	    	    throw new ValidationErrorException("單據狀態參數為空值，無法執行存檔！");	
	    	}
	    	
	    	//取得欲更新的bean
	    	FiInvoiceHead fiInvoice = getActualFiInvoice(headId);
	    	AjaxUtils.copyJSONBeantoPojoBean(formBindBean,fiInvoice);
	    	String invoiceNo = (String)PropertyUtils.getProperty(formLinkBean, "invoiceNo");
	    	fiInvoice.setInvoiceNo(invoiceNo);
	    	fiInvoice.setStatus(formStatus);
	    	removeDetailItemCode( fiInvoice );
	    	String resultMsg = modifyFiInvoice(fiInvoice, employeeCode);
	    	resultMap.put("entityBean", fiInvoice);
	    	resultMap.put("resultMsg",  resultMsg);
	    	return resultMap;
	    	
	        } catch (FormException fe) {
	        log.error("updateAJAXFiInvoice : Invoice 存檔失敗，原因：" + fe.toString());
	        throw new FormException(fe.getMessage());
	    } catch (Exception ex) {
	        log.error("updateAJAXFiInvoice : Invoice 存檔時發生錯誤，原因：" + ex.toString());
	        throw new Exception("updateAJAXFiInvoice : Invoice 存檔時發生錯誤，原因：" + ex.getMessage());
	    }
    }
    
    
    /**AJAX Load Page Data
     * @param headObj
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
    */
    public List<Properties> getAJAXPageData(Properties httpRequest)
    	throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception {
	log.info("getAJAXPageData" + httpRequest );

	// 要顯示的HEAD_ID
	Long   headId    = NumberUtils.getLong(httpRequest.getProperty("headId"));
	String brandCode = httpRequest.getProperty("brandCode");
	String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	List<Properties> re = new ArrayList();
	List<Properties> gridDatas = new ArrayList();
	int iSPage = AjaxUtils.getStartPage(httpRequest);
	int iPSize = AjaxUtils.getPageSize(httpRequest);
	
	List<FiInvoiceLine> fiInvoiceLines = fiInvoiceLineMainService.findPageLine(headId, iSPage, iPSize);
	
	if (null != fiInvoiceLines && fiInvoiceLines.size() > 0) { 
		
		try {
			for( FiInvoiceLine line : fiInvoiceLines ){
				if(!StringUtils.hasText(line.getSourceOrderNo())){
					if(null != line.getPoPurchaseOrderHeadId()){
						PoPurchaseOrderHead poHead = poPurchaseOrderHeadMainService.findById(line.getPoPurchaseOrderHeadId());
						if(null!=poHead){
							line.setSourceOrderNo(poHead.getOrderNo());
							line.setSourceOrderTypeCode(poHead.getOrderTypeCode());
						}
					}
				}
				
				if(StringUtils.hasText(line.getItemCode())){
					//如果是保稅進貨退回發票，則找回原本報關單的品評(DESCRIP)
					if(!"IEF".equals(orderTypeCode)){
						ImItem imItem = imItemService.findItem(brandCode, line.getItemCode() );
						String itemCName = imItem == null?"無此商品":imItem.getItemCName(); 
						line.setItemCName( itemCName );
					}else{
						CmDeclarationItem cmDeclarationItem = cmDeclarationItemDAO.
						findOneCmDeclarationItem(line.getOriginalDeclarationNo(), line.getOriginalDeclarationSeq());
						if(cmDeclarationItem != null)
							line.setItemCName( cmDeclarationItem.getDescrip() );
					}
				}
			}			
			if(brandCode.equals("T2")){
				this.setLineOtherColumn(fiInvoiceLines,brandCode);
			}			
			// 取得第一筆的INDEX
			Long firstIndex = fiInvoiceLines.get(0).getIndexNo();
			// 取得最後一筆 INDEX
			Long maxIndex = fiInvoiceLineMainService.findPageLineMaxIndex(Long.valueOf(headId));			
			re.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, fiInvoiceLines, gridDatas,
					firstIndex, maxIndex));
		} catch (Exception e) {
			e.printStackTrace();
		}
	} else {
		re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));
	}
	return re;
    }
    
    
    /**更新PAGE 所有的LINE
     * @param httpRequest
     * @return String message
     * @throws FormException
     * @throws NumberFormatException
     */
    public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws NumberFormatException, FormException, Exception {
	//log.info("updateAJAXPageLinesData");
	String gridData        = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	int gridRowCount       = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
	Long   headId          = NumberUtils.getLong(httpRequest.getProperty("headId"));
	String brandCode       = httpRequest.getProperty("brandCode");
	String status          = httpRequest.getProperty("status");
	String errorMsg = null;
	log.info("updateAJAXPageLinesData gridLineFirstIndex=" + gridLineFirstIndex + ",gridRowCount="
				+ gridRowCount + ",headId=" + headId + ",status=" + status + ",gridData=" + gridData);

	//if (OrderStatus.SAVE.equalsIgnoreCase(status)) {
	    try {	// 將STRING資料轉成List Properties record data
			FiInvoiceHead head = new FiInvoiceHead();
			head = findById(headId);	    //head.setHeadId(headId);
			BuBrand buBrand = (BuBrand) buBrandService.findById( head.getBrandCode() );
			String branchCode = buBrand.getBuBranch().getBranchCode();	// 是否 T2
			    
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
								FiInvoiceHeadMainService.GRID_FIELD_NAMES);
			int indexNo   = fiInvoiceLineMainService.findPageLineMaxIndex(headId).intValue();	// 取得LINE MAX INDEX
			int customSeq = fiInvoiceLineMainService.findPageLineMaxCustomSeq(headId).intValue();	// 取得LINE MAX customSeq
			//customSeq =  (customSeq/10+1)*10 ;
		    
			int rowCnt    = 0;
			if (null != upRecords && null != head) {
			    for (Properties upRecord : upRecords ) {
				// 先載入HEAD_ID OR LINE DATA
				Long lineId = NumberUtils.getLong( upRecord.getProperty("lineId") );
				String sourceOrderNo         = upRecord.getProperty(GRID_FIELD_NAMES[4]);
				String itemCode              = upRecord.getProperty(GRID_FIELD_NAMES[6]);
				//Long   poPurchaseOrderLineId = NumberUtils.getLong(upRecord.getProperty(GRID_FIELD_NAMES[5]));
				log.info("updateAJAXPageLinesData sourceOrderNo" + sourceOrderNo + " itemCode "+ itemCode + " lineSeq " + ++rowCnt);
				if ((!"2".equals(branchCode) &&  StringUtils.hasText(sourceOrderNo)) ||	// 一般百貨沒有 ItemCode
				    ( "2".equals(branchCode) && (StringUtils.hasText(sourceOrderNo)  || StringUtils.hasText(itemCode)) )) {	// T2 sourceOrderNo || itemCode )
				    FiInvoiceLine fiInvoiceLine = fiInvoiceLineMainService.findLine(head.getHeadId(), lineId);
				    if (null != fiInvoiceLine) {
						// UPDATE Properties
						AjaxUtils.setPojoProperties(fiInvoiceLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
						fiInvoiceLineDAO.update(fiInvoiceLine);
				    } else {
				    	// INSERT Properties
						indexNo++;
						customSeq+=10;
						fiInvoiceLine = new FiInvoiceLine();
						AjaxUtils.setPojoProperties(fiInvoiceLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
						fiInvoiceLine.setIndexNo(Long.valueOf(indexNo));
						fiInvoiceLine.setCustomSeq(Long.valueOf(customSeq));
						fiInvoiceLine.setBrandCode(brandCode);
						fiInvoiceLine.setFiInvoiceHead(head);
						fiInvoiceLineDAO.save(fiInvoiceLine);
				    }
				}
			    }// end of for
			} else {
			    errorMsg = "沒有 INVOICE 單頭資料";
			}
	    } catch (Exception e) {
	    	log.info( e.getMessage() );
	    	e.printStackTrace();
	    }
	//}
	
	return AjaxUtils.getResponseMsg(errorMsg);
	//return null;
    }
    
    
    /**AJAX Line Change
     * @param headObj(brandCode,orderTypeCode,itemCode)
     * @throws FormException
     */
    public List<Properties> getAJAXLineData(Properties httpRequest) throws FormException {
	log.info("getAJAXLineData");
	List re        = new ArrayList();
	Properties pro = new Properties();
	
	String brandCode           = httpRequest.getProperty("brandCode");
	String itemCode            = httpRequest.getProperty("itemCode");
	Long   poHeadId            = NumberUtils.getLong( httpRequest.getProperty("poHeadId") );
	Long   poLineId            = NumberUtils.getLong( httpRequest.getProperty("poLineId") );
	String sourceOrderTypeCode = httpRequest.getProperty("sourceOrderTypeCode");
	String sourceOrderNo       = httpRequest.getProperty("sourceOrderNo");
	
	String itemCName = "查無資料";
	String supplierItemCode = null;
	String purchaseUnit     = null;
	String sPOHeadId = poHeadId==0?"0":httpRequest.getProperty("poHeadId");
	String sPOLineId = poLineId==0?"0":httpRequest.getProperty("poLineId");
	String sQuantity = "0";
	String purchaseAmount = "0";
	String category15 = "";
	String specWeight = "";
	
	log.info("getAJAXLineData brandCode=" + brandCode + " itemCode=" + itemCode +
			" poHeadId=" + poHeadId + " sourceOrderTypeCode=" + sourceOrderTypeCode + " sourceOrderNo=" + sourceOrderNo);
	
	if ( StringUtils.hasText(sourceOrderTypeCode) && StringUtils.hasText(sourceOrderNo)) { // 取 poPurchaseOrderLine.lineID
	    try {
		HashMap findObjs = new HashMap();
		findObjs.put("brandCode",     brandCode );
		findObjs.put("orderTypeCode", sourceOrderTypeCode );
		findObjs.put("orderNo",       sourceOrderNo );
		List<PoPurchaseOrderHead> poHeads = poPurchaseOrderHeadDAO.find(findObjs);
		if ( null != poHeads && poHeads.size() > 0 ) {
		    PoPurchaseOrderHead poHead = new PoPurchaseOrderHead();
		    poHead    = poHeads.get(0);
		    poHeadId  = poHead.getHeadId();
		    sPOHeadId = poHead.getHeadId().toString();
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	
	if (StringUtils.hasText(itemCode) && StringUtils.hasText(brandCode)) {	// 取商品名稱
	    try {
		log.info("imItemService.getItemCName brandCode=" + brandCode + ",itemCode=" + itemCode);
		ImItem imItem    = imItemService.findItem(brandCode, itemCode);
		itemCName        = imItem==null?"無此商品主檔":imItem.getItemCName(); 
		supplierItemCode = imItem==null?null:imItem.getSupplierItemCode();
		purchaseUnit     = imItem==null?null:imItem.getPurchaseUnit();
		purchaseAmount   = imItem==null?"0":imItem.getPurchaseAmount().toString();
		category15   	 = imItem==null?"":imItem.getCategory15();
		specWeight   	 = imItem==null?"":imItem.getSpecWeight();
		} catch (Exception e) {
		e.printStackTrace();
	    }
	}
	
	if ( poHeadId!=null && StringUtils.hasText(itemCode)) {	// 取 poPurchaseOrderLine.lineID
	    try {
		log.info("imItemService.getPOLine headId=" + poHeadId + ",itemCode=" + itemCode);
		List<PoPurchaseOrderLine> poLines = null ;
		if(poLineId != null){
		    poLines = poPurchaseOrderLineDAO.findByItemCode( poHeadId, itemCode, poLineId.toString() );//採購欄位回寫增poLineId-Jerome
		}else{
		    poLines = poPurchaseOrderLineDAO.findByItemCode( poHeadId, itemCode );
		}
		if ( null != poLines && poLines.size() > 0 ) {	// Line 存在此品號 record
		    PoPurchaseOrderLine poLine = new PoPurchaseOrderLine();
		    poLine    = poLines.get(0);
		    sPOLineId = poLine.getLineId().toString();
		    Double qty = poLine.getQuantity()-poLine.getReceiptedQuantity();
		    sQuantity = qty.toString();
		    purchaseAmount = poLine.getForeignUnitCost().toString();
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	pro.setProperty( "PoHeadId",         sPOHeadId );
	pro.setProperty( "PoLineId",         sPOLineId );
	pro.setProperty( "ItemCName",        itemCName );
	pro.setProperty( "SupplierItemCode", supplierItemCode );
	pro.setProperty( "PurchaseUnit",     purchaseUnit );
	pro.setProperty( "PurchaseAmount",   purchaseAmount );
	pro.setProperty( "Quantity",         sQuantity );
	if(category15 != null){
		pro.setProperty( "category15",       category15 );
	}
	if(specWeight != null){
		pro.setProperty( "specWeight",       specWeight );
	}	
	re.add(pro);
	return re;
    }

    
    public String getIdentification(Long headId) throws Exception{
	log.info("getIdentification");
	String id = null;
	try{
	    FiInvoiceHead fiInvoice = findById(headId);
	    if(fiInvoice != null){
		//id = MessageStatus.getIdentification(fiInvoice.getBrandCode(), fiInvoice.getOrderTypeCode(), fiInvoice.getInvoiceNo());
		id = MessageStatus.getIdentification(fiInvoice.getBrandCode(), fiInvoice.getOrderTypeCode(), fiInvoice.getReserve1() );
	    }else{
		throw new NoSuchDataException("Invoice 主檔查無主鍵：" + headId + "的資料！");
	    }
	    return id;
	    
	}catch(Exception ex){
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
	}	   
    }
    
    
    /**執行 Invoice 初始化 - 取採購單別
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
	log.info("executeInitial");
        HashMap resultMap    = new HashMap();
        Object otherBean     = parameterMap.get("vatBeanOther");
        String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");
        String employeeCode  = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
        String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
        String formIdString  = (String)PropertyUtils.getProperty(otherBean, "formId");
        Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
        try{
            FiInvoiceHead fiInvoice = null;
            if(formId != null){
            	fiInvoice = findById(formId);
            	if(null!=fiInvoice){
            		brandCode     = fiInvoice.getBrandCode();
            		orderTypeCode = fiInvoice.getOrderTypeCode();
            	}
            }
            
            BuBrand     buBrand     = buBrandService.findById( brandCode );
            BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode) );
            if(formId == null){
            	fiInvoice = createNewFiInvoiceHead(parameterMap, resultMap, buBrand, buOrderType.getTaxCode() );
            }else{
            	fiInvoice = findFiInvoiceHead(parameterMap, resultMap);
            }
            
            resultMap.put("branchCode",        buBrand.getBuBranch().getBranchCode() );	// 2->T2
            resultMap.put("brandName",         buBrand.getBrandName());
            resultMap.put("typeCode",          buOrderType.getTypeCode() );	// IE->進貨退回, IA->調整, IS->銷退, IP->採購 
            resultMap.put("statusName",        OrderStatus.getChineseWord(fiInvoice.getStatus()));
            resultMap.put("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode(employeeCode));
            resultMap.put("createdByName",     UserUtils.getUsernameByEmployeeCode(fiInvoice.getCreatedBy()));
            resultMap.put("employeeCode",      employeeCode);
            resultMap.put("form", fiInvoice);
            // 預防轉檔資料中沒有轉入 supplierName
		    if( StringUtils.hasText(fiInvoice.getSupplierCode()) && !StringUtils.hasText(fiInvoice.getSupplierName()) ){
		    	BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(fiInvoice.getBrandCode(), fiInvoice.getSupplierCode());
		    	if(null!=buSWAV)
		    		resultMap.put("supplierName", buSWAV.getChineseName() );
		    	else
		    		resultMap.put("supplierName", "");
		    }else{
		    	resultMap.put("supplierName", "");
		    }
	
		    Map multiList = new HashMap(0);
		    List<BuCurrency>  allCurrencyCode = buBasicDataService.findCurrencyByEnable(null);
		    List<BuCommonPhraseLine> allDeclarationType = buCommonPhraseService.getBuCommonPhraseLines("DeclarationType");
		    List<BuOrderType> allOrderTypes   = buOrderTypeService.findOrderbyType(brandCode, buOrderType.getTypeCode());
		    List<BuOrderType> allPoOrderTypes = buOrderTypeService.findOrderbyType(brandCode, "PO");
		    String oriTaxCode = null==buOrderType.getTaxCode()? "P" : buOrderType.getTaxCode();
		    for( int i=0; i < allPoOrderTypes.size(); i++ ){
				String taxCode = null==allPoOrderTypes.get(i).getTaxCode()? "P" : allPoOrderTypes.get(i).getTaxCode();
				if( !oriTaxCode.equals( taxCode ) || "POL".equals(allPoOrderTypes.get(i).getId().getOrderTypeCode()))
				    allPoOrderTypes.remove(i);
		    }
		    multiList.put("allCurrencyCode",    AjaxUtils.produceSelectorData( allCurrencyCode,    "currencyCode",  "currencyCName", false, true));
		    multiList.put("allDeclarationType", AjaxUtils.produceSelectorData( allDeclarationType, "lineCode",      "name",          false, true));
		    multiList.put("allOrderTypes" ,     AjaxUtils.produceSelectorData( allOrderTypes,      "orderTypeCode", "name",          true,  true));
		    multiList.put("allPoOrderTypes" ,   AjaxUtils.produceSelectorData( allPoOrderTypes,    "orderTypeCode", "name",          true,  false));
		    resultMap.put("multiList",multiList);
		    return resultMap;
        }catch (Exception ex) {
		    log.error("Invoice 初始化失敗，原因：" + ex.toString());
		    throw new Exception("Invoice 初始化別失敗，原因：" + ex.toString());
        }           
    }

    
    /** 產生一筆新的 ImReceiveHead 
     * @param argumentMap
     * @param resultMap
     * @return
     * @throws Exception
     */
    public FiInvoiceHead createNewFiInvoiceHead(Map parameterMap, Map resultMap, BuBrand buBrand, String taxCode ) throws Exception {
	log.info("createNewFiInvoiceHead");
	Object otherBean     = parameterMap.get("vatBeanOther");
	String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");		
	String employeeCode  = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
	String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");

	try{
	    FiInvoiceHead form = new FiInvoiceHead();
	    form.setBrandCode(      brandCode);
	    form.setOrderTypeCode(  orderTypeCode);
	    form.setStatus(         OrderStatus.SAVE);
	    form.setCreatedBy(      employeeCode);
	    form.setLastUpdatedBy(  employeeCode);
	    form.setCreationDate(   DateUtils.parseDate(DateUtils.format(new Date())));
	    form.setLastUpdateDate( DateUtils.parseDate(DateUtils.format(new Date())));
	    form.setInvoiceDate(    DateUtils.parseDate(DateUtils.format(new Date())));
	    form.setExchangeRate(   1D);	// 匯率預設
	    form.setTotalForeignInvoiceAmount(0D);
	    form.setTotalLocalInvoiceAmount(  0D);
	    form.setCustomsSeq(     1L);
	    if("IPF".equals(orderTypeCode)){
	    	form.setFinanceConfirm("N");	// N 不直接RUN完流程
	    }else{
	    	form.setFinanceConfirm("Y");	// 預設為Ｙ直接RUN完流程
	    }
	    saveTmp(form);
	    return form;
	}catch (Exception ex) {
	    log.error("產生新 Invoice 失敗，原因：" + ex.toString());
	    throw new Exception("產生 Invoice 發生錯誤！");
	} 	
    }
    
	
    /** form 啟始時查出該筆DATA
     * @param argumentMap
     * @param resultMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public FiInvoiceHead findFiInvoiceHead(Map parameterMap, Map resultMap) 
    	throws FormException, Exception {
	log.info("findFiInvoiceHead");
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
	    Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
	    FiInvoiceHead form = findById(formId);
	    if(form != null){
	        return form;
	    }else{
		throw new NoSuchObjectException("查無 Invoice 主鍵：" + formId + "的資料！");
	    }	    
	}catch (FormException fe) {
	    log.error("查詢 Invoice 失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	}catch (Exception ex) {
	    log.error("查詢 Invoice 發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢 Invoice 發生錯誤！");
	}
    }
    
    
    /**save tmp
     * @param saveObj
     * @return
     * @throws Exception
     */
    public String saveTmp(FiInvoiceHead saveObj) throws FormException, Exception {
	log.info("saveTmp");
	try {
	    String tmpOrderNo = AjaxUtils.getTmpOrderNo();
	    saveObj.setReserve1(tmpOrderNo);
	    saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
	    saveObj.setLastUpdateDate(new Date());
	    saveObj.setCreationDate(new Date());
	    fiInvoiceHeadDAO.save(saveObj);
	    return MessageStatus.SUCCESS;
	}catch(Exception ex){
	    log.info( "saveTmp Error: " + ex.getMessage() );
	    return "FiInvoice Head SaveTmp Error";
	}
    }

    
    /** 檢核 Invoice Head 相關資料
     * @param FiInvoiceHead
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    public void checkFiInvoiceHead(FiInvoiceHead fiInvoice, String branchCode, String programId, String identification, List errorMsgs,String invoiceNo) 
        throws ValidationErrorException {
	log.info("checkFiInvoiceHead");
	//boolean noLine        = removeDetailItemCode( fiInvoice );
	//log.info("noLine = " + noLine);
	String financeConfirm = null==fiInvoice.getFinanceConfirm()?"Y":fiInvoice.getFinanceConfirm();
	String errorLogger    = fiInvoice.getLastUpdatedBy();
	String tabName        = "Invoice 主檔資料";
	
	try{
	    HashMap findObjs = new HashMap();	// 判斷不輸入相同 INVOICE NO
	    findObjs.put("brandCode", fiInvoice.getBrandCode());
	    findObjs.put("invoiceNo", invoiceNo);
	    List<FiInvoiceHead> fiInvoices = find(findObjs);
	    for( FiInvoiceHead newInvoice : fiInvoices){
			if ( newInvoice.getHeadId()!=fiInvoice.getHeadId() && !OrderStatus.VOID.equals(newInvoice.getStatus())  ) {
			    throw new ValidationErrorException(tabName + " 輸入 Invoice No. 已存在！");
			}
	    }
	    BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(fiInvoice.getBrandCode(), fiInvoice.getSupplierCode());
	    if (null==buSWAV){
	    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, tabName+"查無此廠商代號！", errorLogger);
	    }
	    /*if (noLine){
	    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入"+tabName+"之明細資料！", errorLogger);
	    }*/
	    if (null == invoiceNo){
	    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, tabName+"請輸入 Invoice No.！", errorLogger);
	    }
	    if (null == fiInvoice.getSupplierCode()){
	    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, tabName+"請輸入 Supplier No.！", errorLogger);
	    }
	    if (null == fiInvoice.getInvoiceDate()){
	    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, tabName+"請輸入 Invoice Date.！", errorLogger);
	    }
	    if ("N".equals(financeConfirm)){
	    	log.info("financeConfirm為N");
	    	throw new ValidationErrorException(tabName + "請經財務確認後才能送出.！");
	    }
	    if ("2".equals(branchCode) && "Y".equals(financeConfirm) && null==fiInvoice.getCustomsDeclarationType()){
	    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, tabName+"請輸入 Declaration Type.！", errorLogger);
	    }
	    if ("2".equals(branchCode) && "Y".equals(financeConfirm) && null==fiInvoice.getCustomsDeclarationNo()){
	    	throw new ValidationErrorException(tabName+"請輸入 Declaration No.！");
	    }
	    if ("2".equals(branchCode) && "Y".equals(financeConfirm) && null==fiInvoice.getCustomsSeq()){
	    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, tabName+"請輸入Customer Sequence.！", errorLogger);
	    }
	    
	}catch(Exception ex){
	    log.info( "checkHead : " + ex.getMessage() );
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, ex.getMessage(), fiInvoice.getLastUpdatedBy());
	    errorMsgs.add(ex.getMessage());
	}
    }
    
    
    /**移除空白的Detail
     * @param headObj
     * @return boolean 是否還有 Detail
     */
    private boolean removeDetailItemCode(FiInvoiceHead headObj) {
	log.info("checkDetailItemCode");
	List<FiInvoiceLine> items = headObj.getFiInvoiceLines();
	Iterator<FiInvoiceLine> it = items.iterator();
	while (it.hasNext()) {
	    FiInvoiceLine line = it.next();
	    // 如果是刪除資料，或是單別為FII IPF且採購單頭號碼為空的刪除
	    //
	    /*if (AjaxUtils.IS_DELETE_RECORD_TRUE.equals(line.getIsDeleteRecord()) ||
	    		(line.getPoPurchaseOrderHeadId()==0  && 
	    			("FII".equals(headObj.getOrderTypeCode()) || "IPF".equals(headObj.getOrderTypeCode())))){*/
	    if (AjaxUtils.IS_DELETE_RECORD_TRUE.equals(line.getIsDeleteRecord())){
	    	it.remove();
	    	items.remove(line);
	    }
	}
	return items.isEmpty();
	}
    
    
    /**檢核 FiInvoice 明細資料(AJAX)
     * @param FiInvoiceHead
     * @param programId
     * @param identification
     * @param errorMsgs
     */
    public void checkFiInvoiceLines( FiInvoiceHead head, String branchCode, String programId, String identification, List errorMsgs) throws Exception {
    	log.info("checkFiInvoiceLines");
    	String message       = null;
    	String tabName       = "明細資料頁籤";
    	Long   headId        = head.getHeadId();
    	String brandCode     = head.getBrandCode();
    	String orderTypeCode = head.getOrderTypeCode();

    	BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode) );
    	String      oriTaxCode  = null==buOrderType.getTaxCode()?"P":buOrderType.getTaxCode();	
    	// 取 InvoiceHead 稅別
    	List fiInvoiceLines = head.getFiInvoiceLines();
    	try{
    		//如果是保稅退貨發票則不檢核
    		if( fiInvoiceLines != null && fiInvoiceLines.size() > 0  && ("IPF".equals(head.getOrderTypeCode()) || "FII".equals(head.getOrderTypeCode()))){
    			int intactRecordCount = 0;
    			for(int i = 0 ; i < fiInvoiceLines.size(); i++){
    				try{
    					FiInvoiceLine fiInvoiceLine = (FiInvoiceLine)fiInvoiceLines.get(i);
    					if(!"1".equals(fiInvoiceLine.getIsDeleteRecord())){
    						String sourceOrderTypeCode   = fiInvoiceLine.getSourceOrderTypeCode();
    						String sourceOrderNo         = fiInvoiceLine.getSourceOrderNo();
    						String itemCode              = fiInvoiceLine.getItemCode();
    						Long   poPurchaseOrderHeadId = fiInvoiceLine.getPoPurchaseOrderHeadId();
    						Long   poPurchaseOrderLineId = fiInvoiceLine.getPoPurchaseOrderLineId();
    						Boolean updateStatus = false;

    						if(!StringUtils.hasText(sourceOrderNo))
    							throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的採購單號碼！");

    						if(!StringUtils.hasText(sourceOrderTypeCode))
    							throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的採購單類別！");

    						if(!StringUtils.hasText(itemCode) && "IPF".equals(orderTypeCode) )		// for T2 / "2".equals(branchCode)
    							throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的品號！");

    						if(null == fiInvoiceLine.getCustomSeq() && "IPF".equals(orderTypeCode) )	// for T2 / "2".equals(branchCode)
    							throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的指定順序！");

    						if(0 == fiInvoiceLine.getQuantity() && "IPF".equals(orderTypeCode) )	// for T2 / "2".equals(branchCode)
    							throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的 Invoice 數量！");

    						HashMap poHeadObjs = new HashMap();		// 檢核 是否為合法 purchaseOrderHeadData
    						poHeadObjs.put( "brandCode",      brandCode );
    						poHeadObjs.put( "orderNo",       sourceOrderNo );
    						poHeadObjs.put( "orderTypeCode", sourceOrderTypeCode );
    						poHeadObjs.put( "status", OrderStatus.FINISH );
    						List<PoPurchaseOrderHead> poHeads = poPurchaseOrderHeadDAO.find(poHeadObjs);
    						if ( null != poHeads && poHeads.size() > 0 ) {
    							PoPurchaseOrderHead poHead = poHeads.get(0);
    							if(null != poHead && poPurchaseOrderHeadId != poHead.getHeadId()){
    								poPurchaseOrderHeadId   = poHead.getHeadId();	// 取出 poHead.headId
    								fiInvoiceLine.setPoPurchaseOrderHeadId(poPurchaseOrderHeadId);
    								updateStatus = true;				    
    							}
    						} else {
    							throw new ValidationErrorException( tabName + "中第" + (i + 1) + "項明細的採購單號碼有誤！");
    						}

    						if ( !"2".equals(branchCode) && StringUtils.hasText(itemCode) ){	// 非 T2 沒有 itemCode
    							fiInvoiceLine.setItemCode("");
    							updateStatus = true;
    						}else if( "2".equals(branchCode) && StringUtils.hasText(itemCode)) {// 檢核 PoPurchaseOrderLine data
    							PoPurchaseOrderLine poLine = null;	
    							List<PoPurchaseOrderLine> poLines= null;
    							if(poPurchaseOrderLineId != null){
    							     poLines = poPurchaseOrderLineDAO.findByItemCode( poPurchaseOrderHeadId, itemCode, poPurchaseOrderLineId.toString() );//採購欄位回寫增poLineId-Jerome
    							}else{
    							     poLines = poPurchaseOrderLineDAO.findByItemCode( poPurchaseOrderHeadId, itemCode );
    							}
    							if ( poLines!=null && poLines.size()>0 ){ 
    								poLine = poLines.get(0);
    								if ( poPurchaseOrderLineId != poLine.getLineId()){
    									poPurchaseOrderLineId = poLine.getLineId();
    									fiInvoiceLine.setPoPurchaseOrderLineId( poPurchaseOrderLineId );
    									updateStatus = true;
    								}
    							} else {
    								throw new ValidationErrorException( tabName + "中第" + (i + 1) + "項明細的採購單號中查無品號 "+itemCode+" 之採購項目！");
    							}
    						} 

    						// 取 InvoiceHead 稅別
    						buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, sourceOrderTypeCode) );
    						String taxCode = null==buOrderType.getTaxCode()? "P" : buOrderType.getTaxCode();
    						if( !oriTaxCode.equals(taxCode) ){
    							throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的採購單稅別:" + 
    									sourceOrderTypeCode +"-"+ "與發票主檔" + orderTypeCode + "不符合");
    						}

    						if (updateStatus) {	// 有異動資料需要 UPDATE 者 
    							poPurchaseOrderLineDAO.update(fiInvoiceLine);
    						}
    						intactRecordCount++;
    					}
    				}catch(Exception ex){
    					log.info( "checkLine : " + ex.getMessage() );
    					message = ex.getMessage();
    					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
    					errorMsgs.add(message);
    					log.error(message);	            
    				}
    			}
    			if(intactRecordCount == 0){
    				message = tabName + "中至少需輸入一筆完整的資料！";
    				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
    				errorMsgs.add(message);
    				log.error(message );
    			}	        
    		}else if(fiInvoiceLines != null && fiInvoiceLines.size() == 0){
    			message = tabName + "中至少需輸入一筆完整的資料！";
    			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
    			errorMsgs.add(message);
    			log.error(message );
    		}
    	}catch(Exception ex){
    		message = "檢核" + tabName + "時發生錯誤，原因：" + ex.toString();
    		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
    		errorMsgs.add(message);
    		log.error(message);
    	}
    }
	
	
    /**search PoHead and PoLine Data For FiInvoice Line
     * @param findObjs
     * @return List
     */
    public List findPoWithLineForInvoice(HashMap findObjs) {
	log.info("findWithLineForInvoice ");
	return poPurchaseOrderHeadDAO.findPoWithLineForInvoice(findObjs);
    }
    
    
    /**search PoHead and PoLine Data For FiInvoice Line (T2)	for Invoice 2.5
     * Call by ceap savePoDataAction
     * @param findObjs
     * @return List 
     */ 
    public void savePoData2InvoiceLine( Long headId, List dataObjs) {
	log.info("savePoData2InvoiceLine " + dataObjs.size() + " " + headId );
	if( dataObjs != null && dataObjs.size()>0 ){
	    FiInvoiceHead fiInvoiceHead = findById( headId );
	    int indexNo   = fiInvoiceLineMainService.findPageLineMaxIndex(headId).intValue();		// 取得LINE MAX INDEX
	    int customSeq = fiInvoiceLineMainService.findPageLineMaxCustomSeq(headId).intValue();	// 取得LINE MAX customSeq
	    //customSeq = (customSeq/10+1)*10 ;
	    log.info("findPageLineMaxICustomSeq =" + customSeq);

	    for( int i=0; i<dataObjs.size(); i++ ){
		indexNo++;
		customSeq+=10;
		log.info(indexNo );
		Object[] obj = (Object[]) dataObjs.get(i);
		FiInvoiceLine fiInvoiceLine = new FiInvoiceLine();
		fiInvoiceLine.setFiInvoiceHead( fiInvoiceHead  );
		fiInvoiceLine.setPoPurchaseOrderHeadId( (Long)   obj[0] );
		fiInvoiceLine.setBrandCode(             (String) obj[1] );
		fiInvoiceLine.setSourceOrderTypeCode(   (String) obj[2] );
		fiInvoiceLine.setSourceOrderNo(         (String) obj[3] );
		fiInvoiceLine.setPoPurchaseOrderLineId( (Long)   obj[6] );
		fiInvoiceLine.setItemCode(              (String) obj[7] );
		fiInvoiceLine.setQuantity(              (Double) obj[9] );	// actualPurchaseQuantity - receiptedQuantity
		fiInvoiceLine.setIndexNo(               Long.valueOf(indexNo));
		fiInvoiceLine.setCustomSeq(             Long.valueOf(customSeq));
		fiInvoiceLineDAO.save( fiInvoiceLine );
	    }
	}
    }
    
    
    /**search PoHead and PoLine Data For FiInvoice Line (T2)	for invoice 3.0
     * Call by ceap savePoDataAction
     * @param findObjs
     * @return List 
     */ 
    public void savePoLine2InvoiceLine( Long headId, List<PoPurchaseOrderLine> poLines) throws Exception{
    	log.info("savePoLine2InvoiceLine " + poLines.size() + " " + headId );
    	if( poLines != null && poLines.size()>0 ){
    		FiInvoiceHead fiInvoiceHead = findById( headId );
    		int indexNo   = fiInvoiceLineMainService.findPageLineMaxIndex(headId).intValue();		// 取得LINE MAX INDEX
    		int customSeq = fiInvoiceLineMainService.findPageLineMaxCustomSeq(headId).intValue();	// 取得LINE MAX customSeq
    		log.info("findPageLineMaxICustomSeq =" + customSeq);
    		for( PoPurchaseOrderLine poLine : poLines ){
    			indexNo++;
    			customSeq+=10;
    			PoPurchaseOrderHead poHead  = poPurchaseOrderHeadMainService.findById( poLine.getPoPurchaseOrderHead().getHeadId() );
    			ImItem              imItem  = imItemService.findItem(fiInvoiceHead.getBrandCode(), poLine.getItemCode());
    			FiInvoiceLine fiInvoiceLine = new FiInvoiceLine();
    			fiInvoiceLine.setFiInvoiceHead( fiInvoiceHead  );
    			fiInvoiceLine.setPoPurchaseOrderHeadId( poHead.getHeadId() );
    			fiInvoiceLine.setBrandCode( poHead.getBrandCode() );
    			fiInvoiceLine.setSourceOrderTypeCode( poHead.getOrderTypeCode() );
    			fiInvoiceLine.setSourceOrderNo( poHead.getOrderNo() );
    			fiInvoiceLine.setPoPurchaseOrderLineId( poLine.getLineId() );
    			fiInvoiceLine.setItemCode( poLine.getItemCode() );
    			fiInvoiceLine.setSupplierItemCode( imItem.getSupplierItemCode());
    			fiInvoiceLine.setPurchaseUnit( imItem.getPurchaseUnit());
    			fiInvoiceLine.setForeignUnitPrice( NumberUtils.getDouble(poLine.getForeignUnitCost()));
    			fiInvoiceLine.setQuantity( NumberUtils.getDouble(poLine.getActualPurchaseQuantity()) - NumberUtils.getDouble(poLine.getReceiptedQuantity()));	// actualPurchaseQuantity - receiptedQuantity
    			fiInvoiceLine.setIndexNo( Long.valueOf(indexNo));
    			fiInvoiceLine.setCustomSeq( Long.valueOf(customSeq));
    			fiInvoiceLine.setForeignAmount( NumberUtils.getDouble(fiInvoiceLine.getQuantity()) * NumberUtils.getDouble(fiInvoiceLine.getForeignUnitPrice()));
    			fiInvoiceLine.setIsDeleteRecord(AjaxUtils.IS_DELETE_RECORD_FALSE);
    			fiInvoiceLineDAO.save( fiInvoiceLine );
    		}
    	}
    }
    
    
    /**search PoHead Data For FiInvoice Line (Not T2)	for invoice 2.5 & 3.0
     * Call by ceap savePoHeadAction
     * @param findObjs
     * @return List 
     */ 
    public void savePoHead2InvoiceLine( Long headId, List dataObjs) {
	log.info("savePoHead2InvoiceLine " + dataObjs.size() + " " + headId );
	if( dataObjs != null && dataObjs.size()>0 ){
	    FiInvoiceHead fiInvoiceHead = findById( headId );
	    int indexNo   = fiInvoiceLineMainService.findPageLineMaxIndex(headId).intValue();		// 取得LINE MAX INDEX
	    log.info("findPageLineMaxIndexNo =" + indexNo);

	    for( int i=0; i<dataObjs.size(); i++ ){
		indexNo++;
		log.info(indexNo );
		PoPurchaseOrderHead poHead = (PoPurchaseOrderHead) dataObjs.get(i);
		// Object[] obj = (Object[]) dataObjs.get(i);
		FiInvoiceLine fiInvoiceLine = new FiInvoiceLine();
		fiInvoiceLine.setFiInvoiceHead( fiInvoiceHead  );
		fiInvoiceLine.setPoPurchaseOrderHeadId( poHead.getHeadId() );
		fiInvoiceLine.setBrandCode(             poHead.getBrandCode() );
		fiInvoiceLine.setSourceOrderTypeCode(   poHead.getOrderTypeCode() );
		fiInvoiceLine.setSourceOrderNo(         poHead.getOrderNo() );
		fiInvoiceLine.setQuantity(              0D );
		fiInvoiceLine.setIndexNo(               Long.valueOf(indexNo));
		fiInvoiceLineDAO.save( fiInvoiceLine );
	    }
	}
    }
    
    
    public static Object[] startProcess(FiInvoiceHead form) throws ProcessFailedException{       
        try{           
	       String packageId = "Fi_Invoice";
	       String processId = "approval";  
	       String version   = "20091019";
	       String sourceReferenceType = "Fi_Invoice(1)";
	       HashMap context = new HashMap();
	       context.put("brandCode", form.getBrandCode());
	       context.put("formId",    form.getHeadId());
	       context.put("orderType", form.getOrderTypeCode());
	       context.put("invoiceNo", form.getInvoiceNo() );
	       return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
	   }catch (Exception ex){
	       log.error("Invoice 流程啟動失敗，原因：" + ex.toString());
	       throw new ProcessFailedException("Invoice 流程啟動失敗！");
	   }
    }
    
    
    public static Object[] completeAssignment(long assignmentId, boolean approveResult, FiInvoiceHead fiInvoice) 
    	throws ProcessFailedException{
	try{           
	    HashMap context = new HashMap();
	    context.put("approveResult", approveResult);
	    context.put("form",fiInvoice);
	    return ProcessHandling.completeAssignment(assignmentId, context);
	}catch (Exception ex){
	    log.error("完成 Invoice 工作任務失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("完成 Invoice 工作任務失敗！");
	}
    }
    
    
    /**更新至FiInvoice主檔 for cEAP Flow
     * @param FiInvoiceHead
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    public String updateFiInvoice(FiInvoiceHead fiInvoice)
    	    throws ObtainSerialNoFailedException, FormException, Exception {
	log.info("updateFiInvoice");
	fiInvoice.setLastUpdateDate(new Date());
	fiInvoiceHeadDAO.update(fiInvoice);
        return fiInvoice.getOrderTypeCode() + "-" + fiInvoice.getInvoiceNo() + "存檔成功！";
    }
    
    // invoice head search start
    /**執行 Invoice 查詢初始化
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception{
        log.info("executeSearchInitial");
        HashMap resultMap = new HashMap();
        try{
            Object otherBean = parameterMap.get("vatBeanOther");
	    String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
	    BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode ) );
	    log.info("brandCode=" + brandCode + " orderTypeCode=" + orderTypeCode + "-" + buOrderType);
	    Map multiList = new HashMap(0);
	    List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(brandCode, buOrderType.getTypeCode());
	    multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));	    
	    resultMap.put("multiList",multiList);
		
	    return resultMap;       	
        }catch (Exception ex) {
	    log.error("Invoice 查詢初始化失敗，原因：" + ex.toString());
	    throw new Exception("Invoice 查詢初始化失敗，原因：" + ex.toString());
	}           
    }
    
    
    /**顯示查詢頁面的line
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{
	log.info("getAJAXSearchPageData");
    	try{
    	    List<Properties> result    = new ArrayList();
    	    List<Properties> gridDatas = new ArrayList();
    	    int iSPage = AjaxUtils.getStartPage(httpRequest);	// 取得起始頁面
    	    int iPSize = AjaxUtils.getPageSize(httpRequest);	// 取得每頁大小  	  
    	    //======================帶入Head的值=========================	    
    	    String startDate     = httpRequest.getProperty("startDate");
	    String endDate       = httpRequest.getProperty("endDate");
    	    Date actualStartDate = null;
    	    Date actualEndDate   = null;
    	    if(StringUtils.hasText(startDate))
    		actualStartDate = DateUtils.parseDate("yyyy/MM/dd", startDate);
    	    if(StringUtils.hasText(endDate))
    		actualEndDate = DateUtils.parseDate("yyyy/MM/dd", endDate);
  	     	   
    	    HashMap findObjs = new HashMap();
    	    findObjs.put("brandCode",      httpRequest.getProperty("brandCode"));
    	    findObjs.put("orderTypeCode",  httpRequest.getProperty("orderTypeCode"));
    	    findObjs.put("startInvoiceNo", httpRequest.getProperty("startInvoiceNo"));
    	    findObjs.put("endInvoiceNo",   httpRequest.getProperty("endInvoiceNo")); 
    	    findObjs.put("supplierCode",   httpRequest.getProperty("supplierCode"));	 
    	    findObjs.put("status",         httpRequest.getProperty("status"));	    
    	    findObjs.put("startDate",      actualStartDate);
    	    findObjs.put("endDate",        actualEndDate);
    	    //==============================================================    	   
    	    List<FiInvoiceHead> fiInvoiceHeads = 
    	    	(List<FiInvoiceHead>) fiInvoiceHeadDAO.findPageLine(findObjs, 
    	    			iSPage, iPSize, FiInvoiceHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");
    	    log.info("ResultSize=["+ fiInvoiceHeads.size() + "]" + findObjs);
    	    if (fiInvoiceHeads != null && fiInvoiceHeads.size() > 0) {    	    
    	    	Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;    		// 取得第一筆的INDEX 	
    	    	Long maxIndex = (Long)fiInvoiceHeadDAO.findPageLine(findObjs, -1, iPSize, 
    	    	FiInvoiceHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount");	// 取得最後一筆 INDEX
    	    	
    	    	for(FiInvoiceHead fiInvoiceHead : fiInvoiceHeads){    
    	    	fiInvoiceHead.setStatus(OrderStatus.getChineseWord(fiInvoiceHead.getStatus()));
    	    	}
    	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, fiInvoiceHeads, gridDatas, firstIndex, maxIndex));
    	    } else {
    	        result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, gridDatas));
    	    }
    	
    	    return result;
    	}catch(Exception ex){
    	    log.error("載入頁面顯示的 Invoice 查詢發生錯誤，原因：" + ex.toString());
    	    throw new Exception("載入頁面顯示的 Invoice 查詢失敗！");
    	}	
    }
    
    
    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
    
    public void updateAllSearchData(Map parameterMap) throws FormException, Exception{
	log.info("updateAllSearchData!");
	try{
    	    Object pickerBean    = parameterMap.get("vatBeanPicker");
	    Object formBindBean  = parameterMap.get("vatBeanFormBind");
	    Object otherBean     = parameterMap.get("vatBeanOther");
            String timeScope     = (String)   PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
	    //======================帶入Head的值=========================	    
    	    String brandCode      = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
    	    String orderTypeCode  = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");
    	    String startInvoiceNo = (String)PropertyUtils.getProperty(formBindBean, "startInvoiceNo");
    	    String endInvoiceNo   = (String)PropertyUtils.getProperty(formBindBean, "endInvoiceNo");
    	    String status         = (String)PropertyUtils.getProperty(formBindBean, "status");
    	    String startDate      = (String)PropertyUtils.getProperty(formBindBean, "startDate");
    	    String endDate        = (String)PropertyUtils.getProperty(formBindBean, "endDate");
    	    Date actualStartDate = null;
    	    Date actualEndDate = null;
    	    if(StringUtils.hasText(endDate)){
    		actualStartDate = DateUtils.parseDate("yyyy/MM/dd", startDate);
    	    }
    	    if(StringUtils.hasText(endDate)){
    		actualEndDate = DateUtils.parseDate("yyyy/MM/dd", endDate);
	    }  	    
    	    
    	    HashMap findObjs = new HashMap();
    	    findObjs.put("brandCode",      brandCode);
	    findObjs.put("orderTypeCode",  orderTypeCode);
	    findObjs.put("startInvoiceNo", startInvoiceNo);
	    findObjs.put("endInvoiceNo",   endInvoiceNo); 	   
	    findObjs.put("status",         status);    
	    findObjs.put("startDate",      actualStartDate);
	    findObjs.put("endDate",        actualEndDate);     
    	    List<FiInvoiceHead> fiInvoiceHeads = 
    	    	(List<FiInvoiceHead>) fiInvoiceHeadDAO.findPageLine(findObjs, 
    	    			-1, -1, FiInvoiceHeadDAO.QUARY_TYPE_SELECT_ALL).get("form");
    	    log.info("ResultSize=["+ fiInvoiceHeads.size() + "]" + findObjs);
	}catch(Exception ex){
	    log.error("更新選取 Invoice 資料失敗，原因：" + ex.toString());
	    throw new Exception("更新選取 Invoice 資料失敗，原因：" + ex.getMessage());		
	}
    }
    
    public Map getSearchSelection(Map parameterMap) throws Exception{
	log.info("getSearchSelection");
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    Object pickerBean       = parameterMap.get("vatBeanPicker");
	    String timeScope        = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys    = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    log.info(" selected size = " + result.size() );
	    if(result.size() > 0 )
	        pickerResult.put("result", result);
	    
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel",  new String[]{"vatBeanPicker"});
	    
	    return resultMap;
	}catch(Exception ex){
	    log.error("執行 Invoice 檢視失敗，原因：" + ex.toString());
	    throw new Exception("執行 Invoice 檢視失敗，原因：" + ex.getMessage());		
	}
    }
    // invoice head search end
    
    
    // poPurchaseOrderHead search start
    /**執行 poPurchaseOrderHead 查詢初始化
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeSearchPoHInitial(Map parameterMap) throws Exception{
        log.info("executeSearchPoHInitial");
        HashMap resultMap = new HashMap();
        try{
            Object otherBean = parameterMap.get("vatBeanOther");
	    String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
	    BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode ) );
	    log.info("brandCode=" + brandCode + " orderTypeCode=" + orderTypeCode + "-" + buOrderType);
	    Map multiList = new HashMap(0);
	    List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(brandCode, "PO");
	    String oriTaxCode = null==buOrderType.getTaxCode()? "P" : buOrderType.getTaxCode();
	    for( int i=0; i < allOrderTypes.size(); i++ ){
		String taxCode = null==allOrderTypes.get(i).getTaxCode()? "P" : allOrderTypes.get(i).getTaxCode();
		if( !oriTaxCode.equals( taxCode ) || "POL".equals(allOrderTypes.get(i).getId().getOrderTypeCode()))
		    allOrderTypes.remove(i);
	    }
	    multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));	    
	    resultMap.put("multiList",multiList);
		
	    return resultMap;       	
        }catch (Exception ex) {
	    log.error("PurchaseOrderHead 查詢初始化失敗，原因：" + ex.toString());
	    throw new Exception("PurchaseOrderHead 查詢初始化失敗，原因：" + ex.toString());
	}           
    }
    
    
    /**顯示查詢頁面的line
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPoHPageData(Properties httpRequest) throws Exception{
	log.info("getAJAXSearchPoHPageData");
    	try{
    	    List<Properties> result    = new ArrayList();
    	    List<Properties> gridDatas = new ArrayList();
    	    int iSPage = AjaxUtils.getStartPage(httpRequest);	// 取得起始頁面
    	    int iPSize = AjaxUtils.getPageSize(httpRequest);	// 取得每頁大小  	  
    	    //======================帶入Head的值=========================	    
    	    String startDate     = httpRequest.getProperty("startDate");
	    String endDate       = httpRequest.getProperty("endDate");
    	    Date actualStartDate = null;
    	    Date actualEndDate   = null;
    	    if(StringUtils.hasText(startDate))
    		actualStartDate = DateUtils.parseDate("yyyy/MM/dd", startDate);
    	    if(StringUtils.hasText(endDate))
    		actualEndDate = DateUtils.parseDate("yyyy/MM/dd", endDate);
  	     	   
    	    HashMap findObjs = new HashMap();
    	    findObjs.put("brandCode",      httpRequest.getProperty("brandCode"));
    	    findObjs.put("supplierCode",   httpRequest.getProperty("supplierCode"));
    	    findObjs.put("orderTypeCode",  httpRequest.getProperty("orderTypeCode"));
    	    findObjs.put("startOrderNo",   httpRequest.getProperty("startOrderNo"));
    	    findObjs.put("endOrderNo",     httpRequest.getProperty("endOrderNo")); 	   
    	    findObjs.put("isHideClose",    httpRequest.getProperty("isHideClose"));	    
    	    findObjs.put("startDate",      actualStartDate);
    	    findObjs.put("endDate",        actualEndDate);
    	    //==============================================================    	   
    	    List<PoPurchaseOrderHead> poPurchaseOrderHeads = 
    	    	(List<PoPurchaseOrderHead>) poPurchaseOrderHeadDAO.findPoHeadPageLine(findObjs, 
    	    			iSPage, iPSize, PoPurchaseOrderHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");
    	    log.info("ResultSize=["+ poPurchaseOrderHeads.size() + "]" + findObjs);
    	    if (poPurchaseOrderHeads != null && poPurchaseOrderHeads.size() > 0) {    	    
    	    	Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;    		// 取得第一筆的INDEX 	
    	    	Long maxIndex = (Long) poPurchaseOrderHeadDAO.findPoHeadPageLine(findObjs, -1, iPSize, 
    	    		PoPurchaseOrderHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount");	// 取得最後一筆 INDEX
    	    	
    	    	for(PoPurchaseOrderHead poPurchaseOrderHead : poPurchaseOrderHeads){    
    	    	poPurchaseOrderHead.setStatus(OrderStatus.getChineseWord(poPurchaseOrderHead.getStatus()));
    	    	}
    	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_PoH_FIELD_NAMES, GRID_SEARCH_PoH_FIELD_DEFAULT_VALUES, poPurchaseOrderHeads, gridDatas, firstIndex, maxIndex));
    	    } else {
    	        result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_PoH_FIELD_NAMES, GRID_SEARCH_PoH_FIELD_DEFAULT_VALUES, gridDatas));
    	    }
    	
    	    return result;
    	}catch(Exception ex){
    	    log.error("載入頁面顯示的 PurchaseOrder 查詢發生錯誤，原因：" + ex.toString());
    	    throw new Exception("載入頁面顯示的 PurchaseOrder 查詢失敗！");
    	}	
    }
    
    
    public List<Properties> saveSearchPoHResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_PoH_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
    
    public void updateAllSearcPoHhData(Map parameterMap) throws FormException, Exception{
	log.info("updateAllSearcPoHhData!");
	try{
    	    Object pickerBean    = parameterMap.get("vatBeanPicker");
	    Object formBindBean  = parameterMap.get("vatBeanFormBind");
	    Object otherBean     = parameterMap.get("vatBeanOther");
            String timeScope     = (String)   PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
	    //======================帶入Head的值=========================	    
    	    String brandCode     = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
    	    String orderTypeCode  = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");
    	    String startOrderNo  = (String)PropertyUtils.getProperty(formBindBean, "startOrderNo");
    	    String endOrderNo    = (String)PropertyUtils.getProperty(formBindBean, "endOrderNo");
    	    String status        = (String)PropertyUtils.getProperty(formBindBean, "status");
    	    String startDate     = (String)PropertyUtils.getProperty(formBindBean, "startDate");
    	    String endDate       = (String)PropertyUtils.getProperty(formBindBean, "endDate");
    	    Date actualStartDate = null;
    	    Date actualEndDate = null;
    	    if(StringUtils.hasText(endDate)){
    		actualStartDate = DateUtils.parseDate("yyyy/MM/dd", startDate);
    	    }
    	    if(StringUtils.hasText(endDate)){
    		actualEndDate = DateUtils.parseDate("yyyy/MM/dd", endDate);
	    }  	    
    	    
    	    HashMap findObjs = new HashMap();
    	    findObjs.put("brandCode",      brandCode);
	    findObjs.put("orderTypeCode",  orderTypeCode);
	    findObjs.put("startOrderNo",   startOrderNo);
	    findObjs.put("endOrderNo",     endOrderNo); 	   
	    findObjs.put("status",         status);    
	    findObjs.put("startDate",      actualStartDate);
	    findObjs.put("endDate",        actualEndDate);     
    	    List<PoPurchaseOrderHead> poPurchaseOrderHeads =
    	    	(List<PoPurchaseOrderHead>) poPurchaseOrderHeadDAO.findPoHeadPageLine(findObjs,
    	    			-1, -1, PoPurchaseOrderHeadDAO.QUARY_TYPE_SELECT_ALL).get("form");
    	    log.info("ResultSize=["+ poPurchaseOrderHeads.size() + "]" + findObjs);
	}catch(Exception ex){
	    log.error("更新選取 PoPurchaseOrder 資料失敗，原因：" + ex.toString());
	    throw new Exception("更新選取 PoPurchaseOrder 資料失敗，原因：" + ex.getMessage());
	}
    }
    
    
    public Map saveSearchPoHSelection(Map parameterMap) throws Exception{
	log.info("getSearchPoHSelection");
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    Object pickerBean       = parameterMap.get("vatBeanPicker");
	    String timeScope        = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys    = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    log.info(" selected size = " + result.size() );
	    if(result.size() > 0 ){
	        pickerResult.put("result", result);
	        
	        // 產生 FiInvoiceLine
	        Object otherBean = parameterMap.get("vatBeanOther");
	        String id   = (String)PropertyUtils.getProperty(otherBean, "headId");
	        Long headId = NumberUtils.getLong(id);
	        List <PoPurchaseOrderHead> poHeads = new ArrayList();
	        for( int i=0; i < result.size(); i++){
	            //log.info(NumberUtils.getLong( result.get(i).getProperty("headId")));
	            Long poHeadId = NumberUtils.getLong( result.get(i).getProperty("headId") ); 
	            PoPurchaseOrderHead poPurchaseOrderHead = poPurchaseOrderHeadMainService.findById( poHeadId );
	            poHeads.add(poPurchaseOrderHead);
	        }
	        savePoHead2InvoiceLine( headId, poHeads );
	        
	    }
	    
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel",  new String[]{"vatBeanPicker"});
	    
	    return resultMap;
	}catch(Exception ex){
	    log.error("執行 PoPurchaseOrder 檢視失敗，原因：" + ex.toString());
	    throw new Exception("執行 PoPurchaseOrder 檢視失敗，原因：" + ex.getMessage());		
	}
    }
    // poPurchaseOrder search end
    
    
    
    // poPurchaseOrderLine search start
    /**執行 poPurchaseOrderHead 查詢初始化
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeSearchPoLInitial(Map parameterMap) throws Exception{
        log.info("executeSearchPoLInitial");
        HashMap resultMap = new HashMap();
        try{
            Object otherBean = parameterMap.get("vatBeanOther");
	    String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
	    
	    BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode ) );
	    log.info("brandCode=" + brandCode + " orderTypeCode=" + orderTypeCode + "-" + buOrderType);
	    Map multiList = new HashMap(0);
	    List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(brandCode, "PO");
	    String oriTaxCode = null==buOrderType.getTaxCode()? "P" : buOrderType.getTaxCode();
	    for( int i=0; i < allOrderTypes.size(); i++ ){
		String taxCode = null==allOrderTypes.get(i).getTaxCode()? "P" : allOrderTypes.get(i).getTaxCode();
		if( !oriTaxCode.equals( taxCode ) || "POF".equals(allOrderTypes.get(i).getId().getOrderTypeCode()))
		    allOrderTypes.remove(i);
	    }
	    multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
	    
	    String currencyCode = (String)PropertyUtils.getProperty(otherBean, "currencyCode");
	    List<BuCurrency> allCurrencyCode    = buBasicDataService.findCurrencyByEnable(null);
	    resultMap.put("allCurrencyCode",    AjaxUtils.produceSelectorData( allCurrencyCode,    "currencyCode",    "currencyCName", false, true , currencyCode));
	    
	    resultMap.put("multiList",multiList);
		
	    return resultMap;       	
        }catch (Exception ex) {
	    log.error("PurchaseOrderLine 查詢初始化失敗，原因：" + ex.toString());
	    throw new Exception("PurchaseOrderLine 查詢初始化失敗，原因：" + ex.toString());
	}           
    }
    
    
    /**顯示查詢頁面的line
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPoLPageData(Properties httpRequest) throws Exception{
	log.info("getAJAXSearchPoLPageData");
    	try{
    	    List<Properties> result    = new ArrayList();
    	    List<Properties> gridDatas = new ArrayList();
    	    int iSPage = AjaxUtils.getStartPage(httpRequest);	// 取得起始頁面
    	    int iPSize = AjaxUtils.getPageSize(httpRequest);	// 取得每頁大小  	  
    	    //======================帶入Head的值=========================	    
    	    String startDate     = httpRequest.getProperty("startDate");
    	    String endDate       = httpRequest.getProperty("endDate");
    	    Date actualStartDate = null;
    	    Date actualEndDate   = null;
    	    if(StringUtils.hasText(startDate))
    		actualStartDate = DateUtils.parseDate("yyyy/MM/dd", startDate);
    	    if(StringUtils.hasText(endDate))
    		actualEndDate = DateUtils.parseDate("yyyy/MM/dd", endDate);

    	    HashMap findObjs = new HashMap();
    	    findObjs.put("brandCode",      			httpRequest.getProperty("brandCode"));
    	    findObjs.put("currencyCode",   			httpRequest.getProperty("currencyCode"));
    	    findObjs.put("supplierCode",   			httpRequest.getProperty("supplierCode"));
    	    findObjs.put("defaultWarehouseCode",	httpRequest.getProperty("defaultWarehouseCode"));
    	    findObjs.put("orderTypeCode",  			httpRequest.getProperty("orderTypeCode"));
    	    findObjs.put("startOrderNo",   			httpRequest.getProperty("startOrderNo"));
    	    findObjs.put("endOrderNo",     			httpRequest.getProperty("endOrderNo")); 	   
    	    findObjs.put("isHideClose",    			httpRequest.getProperty("isHideClose"));
    	    findObjs.put("sortType",       			httpRequest.getProperty("sortType"));
    	    findObjs.put("startDate",      			actualStartDate);
    	    findObjs.put("endDate",        			actualEndDate);
    	    //==============================================================  
    	    List poHeads = (List) poPurchaseOrderHeadDAO.findPoLinePageLine(findObjs, 
    	    			iSPage, iPSize, PoPurchaseOrderHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");
    	    log.info("getAJAXSearchPoLPageData ResultSize=["+ poHeads.size() + "]" + findObjs);
    	    
    	    if (poHeads != null && poHeads.size() > 0) {
	    		List <PoPurchaseOrderHead> poPurchaseOrderHeads = new ArrayList();
	    		for( int i=0; i<poHeads.size(); i++ ){
	    		    Object[] obj = (Object[]) poHeads.get(i);
	    		    PoPurchaseOrderHead poHead = new PoPurchaseOrderHead();
	    		    poHead.setHeadId(        (Long)   obj[6]);		// obj[0]:poHead.headId, obj[6]:poLine.lineId
	    		    poHead.setBrandCode(     (String) obj[1] );
	    		    poHead.setOrderTypeCode( (String) obj[2] );
	    		    poHead.setOrderNo(       (String) obj[3] );
	    		    poHead.setSupplierCode(  (String) obj[4] );
	    		    poHead.setSupplierName(  (String) obj[5] );
	    		    poHead.setReserve1(      (String) obj[7] );	// itemCode
	    		    poHead.setReserve2(      (String) obj[8] );	// itemCName
	    		    poHead.setAsignedBudget( (Double) obj[9] );	// actualPurchaseQuantity - receiptedQuantity
	    		    poHead.setStatus(        (String) obj[10]);	// status
	    		    poHead.setLastUpdateDate((Date)   obj[11]);
	    		    poHead.setReserve3(      (String) obj[12]);	// brandCode
	    		    poHead.setReserve4(      (String) obj[13]);	// supplierItemCode
	    		    poHead.setTotalBudget(   (Double) obj[14]);	// (actualPurchaseQuantity-receiptedQuantity)*foreignUnitCost
	    		    poPurchaseOrderHeads.add(poHead);
	    		}		    
    	    	Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;    				// 取得第一筆的INDEX 	
    	    	Long maxIndex = (Long) poPurchaseOrderHeadDAO.findPoLinePageLine(findObjs, -1, iPSize, 
    	    	PoPurchaseOrderHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount");	// 取得最後一筆 INDEX
    	    	
    	    	for(PoPurchaseOrderHead poPurchaseOrderHead : poPurchaseOrderHeads){    
    	    	    poPurchaseOrderHead.setStatus(OrderStatus.getChineseWord(poPurchaseOrderHead.getStatus()));
    	    	}
    	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_PoL_FIELD_NAMES, GRID_SEARCH_PoL_FIELD_DEFAULT_VALUES, poPurchaseOrderHeads, gridDatas, firstIndex, maxIndex));
    	    } else {
    	        result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_PoL_FIELD_NAMES, GRID_SEARCH_PoL_FIELD_DEFAULT_VALUES, gridDatas));
    	    }
    	
    	    return result;
    	}catch(Exception ex){
    	    log.error("載入頁面顯示的 PurchaseOrderLine 查詢發生錯誤，原因：" + ex.toString());
    	    throw new Exception("載入頁面顯示的 PurchaseOrderLine 查詢失敗！");
    	}	
    }
    
    
    public List<Properties> saveSearchPoLResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_PoL_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
    
    public List<Properties> updateAllSearcPoLhData(Map parameterMap) throws FormException, Exception{
	log.info("updateAllSearcPoHhData!");
	Map resultMap = new HashMap(0);
	try{
   	    	Object pickerBean = parameterMap.get("vatBeanPicker");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");
			String timeScope = (String)PropertyUtils.getProperty(otherBean, AjaxUtils.TIME_SCOPE);
			String isAllClick = (String)PropertyUtils.getProperty(otherBean, "isAllClick");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
			String supplierCode = (String)PropertyUtils.getProperty(otherBean, "supplierCode");
			log.info("timeScope:"+timeScope);
    	    String orderTypeCode  = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");
    	    String startOrderNo  = (String)PropertyUtils.getProperty(formBindBean, "startOrderNo");
    	    String endOrderNo    = (String)PropertyUtils.getProperty(formBindBean, "endOrderNo");
    	    String startDate     = (String)PropertyUtils.getProperty(formBindBean, "startDate");
    	    String endDate       = (String)PropertyUtils.getProperty(formBindBean, "endDate");
    	    String isHideClose    = (String)PropertyUtils.getProperty(formBindBean, "isHideClose");
    	    String sortType    = (String)PropertyUtils.getProperty(formBindBean, "sortType");
    	    Date actualStartDate = null;
    	    Date actualEndDate = null;
    	    if(StringUtils.hasText(endDate)){
    	    	actualStartDate = DateUtils.parseDate("yyyy/MM/dd", startDate);
    	    }
    	    if(StringUtils.hasText(endDate)){
    	    	actualEndDate = DateUtils.parseDate("yyyy/MM/dd", endDate);
    	    }  	    
    	    
    	    HashMap findObjs = new HashMap();
    	    findObjs.put("brandCode",      brandCode);
		    findObjs.put("orderTypeCode",  orderTypeCode);
		    findObjs.put("startOrderNo",   startOrderNo);
		    findObjs.put("endOrderNo",     endOrderNo); 	   
		    findObjs.put("startDate",      actualStartDate);
		    findObjs.put("endDate",        actualEndDate);
    	    findObjs.put("supplierCode",   supplierCode);
    	    findObjs.put("isHideClose",    isHideClose);
    	    findObjs.put("sortType",       sortType);
    	    findObjs.put("startDate",      actualStartDate);
    	    findObjs.put("endDate",        actualEndDate);
    	    List<PoPurchaseOrderHead> poPurchaseOrderHeads =
    	    	(List<PoPurchaseOrderHead>) poPurchaseOrderHeadDAO.findPoLinePageLine(findObjs,
    	    			-1, -1, PoPurchaseOrderHeadDAO.QUARY_TYPE_SELECT_ALL).get("form");
    	    log.info("ResultSize=["+ poPurchaseOrderHeads.size() + "]" + findObjs);
    	    if(poPurchaseOrderHeads.size()>0)
    	    	AjaxUtils.updateAllResult(timeScope,isAllClick,poPurchaseOrderHeads);
    	    log.info("updateAllSearcPoLhData Finish");
	}catch(Exception ex){
	    log.error("更新選取 PurchaseOrderLine 資料失敗，原因：" + ex.toString());
	    throw new Exception("更新選取 PurchaseOrderLine 資料失敗，原因：" + ex.getMessage());
	}
    	return AjaxUtils.parseReturnDataToJSON(resultMap);
    }
    
    
    public Map saveSearchPoLSelection(Map parameterMap) throws Exception{
	log.info("getSearchPoLSelection");
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    Object pickerBean       = parameterMap.get("vatBeanPicker");
	    String timeScope        = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys    = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
	    Double exchangeRate = 1D; 
	    try{
	    	exchangeRate        = NumberUtils.getDouble((String)PropertyUtils.getProperty(pickerBean, "exchangeRate"));
	    	if(exchangeRate == 0D)
	    		exchangeRate = 1D;
	    }catch(Exception e){
	    	
	    }
	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    log.info(" selected size = " + result.size() );
	    if(result.size() > 0 ){
	        pickerResult.put("result", result);
	        // 產生 FiInvoiceLine
	        Object otherBean = parameterMap.get("vatBeanOther");
	        String id       = (String)PropertyUtils.getProperty(otherBean, "headId");
	        String formName = (String)PropertyUtils.getProperty(otherBean, "formName");
	        Long headId = NumberUtils.getLong(id);
	        List <PoPurchaseOrderLine> poLines = new ArrayList();
	        for( int i=0; i < result.size(); i++){
	            log.info(NumberUtils.getLong( result.get(i).getProperty("headId")));
	            Long poLineId = NumberUtils.getLong( result.get(i).getProperty("headId") ); 
	            PoPurchaseOrderLine poPurchaseOrderLine = poPurchaseOrderLineMainService.findById( poLineId );
	            poLines.add(poPurchaseOrderLine);
	        }
	        if( "FiInvoice".equals(formName)){
	            savePoLine2InvoiceLine( headId, poLines );
	        }else if( "ImReceive".equals(formName)){
	            savePoLine2ReceiveItem( headId, poLines, exchangeRate );
	            //imReceiveHeadMainService.savePoLine2ReceiveItem( headId, poLines );
	        }
	    }
	    
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel",  new String[]{"vatBeanPicker"});
	    
	    return resultMap;
	}catch(Exception ex){
	    log.error("執行 PurchaseOrderLine 檢視失敗，原因：" + ex.toString());
	    throw new Exception("執行 PurchaseOrderLine 檢視失敗，原因：" + ex.getMessage());		
	}
    }
    // poPurchaseOrder search end
    
    
    /**AJAX 取得預算及合計資料 , 如果有移除的動作這個階段不會作 , 要等到 SAVE OR SUBMIT
     * @param headObj
     * @throws FormException
     * @throws NumberFormatException
     */
    public List<Properties> getAJAXHeadTotalAmount(Properties httpRequest) 
		throws NumberFormatException, FormException, Exception{
	log.info("getAJAXHeadTotalAmount ");
	Properties pro      = new Properties();
	List       re       = new ArrayList();
	String     headId   = httpRequest.getProperty("headId");
	//Double exchangeRate = NumberUtils.getDouble(httpRequest.getProperty("exchangeRate"));
	log.info("getAJAXHeadTotalAmount headId=" + headId );

	if (StringUtils.hasText(headId)) {
	    String tmpSqlLine = 
		" select nvl(sum( nvl(QUANTITY,0) * nvl(FOREIGN_UNIT_PRICE,0) ),0), " +
		"        nvl(sum( nvl(QUANTITY,0) ),0)  " +
		" from fi_invoice_line where HEAD_ID=" + headId + " and nvl(IS_DELETE_RECORD,'0')<>'1' ";
	    List lineList = nativeQueryDAO.executeNativeSql(tmpSqlLine);
	    Object[] obj = (Object[]) lineList.get(0);
	    Double foreignAmount = ((BigDecimal) obj[0]).doubleValue();
	    Double itemCount     = ((BigDecimal) obj[1]).doubleValue();
	    pro.setProperty("totalForeignInvoiceAmount", NumberUtils.roundToStr(foreignAmount, 4));
	    pro.setProperty("totalLocalInvoiceAmount",   NumberUtils.roundToStr(itemCount,     0));
	}else{
	    pro.setProperty("totalForeignInvoiceAmount", "0");
	    pro.setProperty("totalLocalInvoiceAmount",   "0");
	}
	re.add(pro);
	return re;
    }
    
    
    /**
     * 匯入 Invoice 明細
     * @param headId
     * @param lineLists
     * @throws Exception
     */
    public void updateImportLines(Long headId, List lineLists) throws Exception{
	log.info("updateImportLines ");
	//log.info("headId = " + headId);
	try{
	    //deleteLineLists(headId);
	    if(lineLists != null && lineLists.size() > 0){
    		FiInvoiceHead fiInvoiceHead = findById(headId);
    		for(int i = 0; i < lineLists.size(); i++){
    		    FiInvoiceLine  fiLine = (FiInvoiceLine)lineLists.get(i);
    		    fiLine.setFiInvoiceHead(fiInvoiceHead);
    		    ImItem imItem = imItemService.findItem(fiInvoiceHead.getBrandCode(), fiLine.getItemCode() );
    		    fiLine.setItemCName(       imItem==null?"無此商品主檔":imItem.getItemCName());
    		    fiLine.setSupplierItemCode(imItem==null?null:imItem.getSupplierItemCode());
    		    fiLine.setPurchaseUnit(    imItem==null?null:imItem.getPurchaseUnit());
    		    //fiLine.setForeignUnitPrice(imItem==null?0D:imItem.getPurchaseAmount());
    		    fiLine.setForeignUnitPrice(NumberUtils.getDouble(fiLine.getForeignUnitPrice()));
    		    fiLine.setForeignAmount(NumberUtils.getDouble(fiLine.getQuantity()) *
    			    		       		NumberUtils.getDouble(fiLine.getForeignUnitPrice()));
    		    Long poHeadId = 0L;
    		    HashMap findObjs = new HashMap();
    	    	    findObjs.put("brandCode",      fiInvoiceHead.getBrandCode());
    		    findObjs.put("orderTypeCode",  fiLine.getSourceOrderTypeCode());
    		    findObjs.put("orderNo",   	   fiLine.getSourceOrderNo());
    		    List<PoPurchaseOrderHead> poHeads = poPurchaseOrderHeadDAO.find(findObjs);
    		    
    		    if(poHeads.size()>0){
    		    	poHeadId = poHeads.get(0).getHeadId();
    		    	fiLine.setPoPurchaseOrderHeadId(poHeadId);
    		    	List<PoPurchaseOrderLine> poLines=null;
    		    	if(fiLine.getPoPurchaseOrderLineId() != null){
    		    	    log.info("poHeadId = " + poHeadId);
    		    	    log.info("fiLine.getItemCode() = " + fiLine.getItemCode());
    		    	    log.info("fiLine.getPoPurchaseOrderLineId() = " + fiLine.getPoPurchaseOrderLineId());
	    		    poLines = poPurchaseOrderLineDAO.findByItemCode(poHeadId, fiLine.getItemCode(), fiLine.getPoPurchaseOrderLineId().toString() );//採購欄位回寫增poLineId-Jerome
    		    	   }else{
    		    	    log.info("poHeadId = " + poHeadId);
		    	    log.info("fiLine.getItemCode() = " + fiLine.getItemCode());
		    	    poLines = poPurchaseOrderLineDAO.findByItemCode(poHeadId, fiLine.getItemCode());
    		    	   }
    		    	if(null != poLines && poLines.size()>0){
    		    	    fiLine.setPoPurchaseOrderLineId(poLines.get(0).getLineId());
    		    	}
    		    	fiLine.setIndexNo(i+1L);
    		    }
    		}
    		fiInvoiceHead.setFiInvoiceLines(lineLists);
    		fiInvoiceHeadDAO.update(fiInvoiceHead);
    	}     	
	}catch (Exception ex) {
	    	ex.printStackTrace();
		log.error("Invoice明細匯入時發生錯誤，原因：" + ex.toString());
		throw new Exception("Invoice明細匯入時發生錯誤，原因：" + ex.getMessage());
	}        
    }

    
    public void deleteLineLists(Long headId) throws FormException, Exception{
	log.info("deleteLineLists ");
	try{
	    FiInvoiceHead fiInvoiceHead = (FiInvoiceHead)fiInvoiceHeadDAO.findByPrimaryKey(FiInvoiceHead.class, headId);
	    if( fiInvoiceHead == null){
	        throw new NoSuchObjectException("查無採購單主鍵：" + headId + "的資料");
	    }
	    fiInvoiceHead.setFiInvoiceLines(new ArrayList(0));
	    fiInvoiceHeadDAO.update(fiInvoiceHead);
	} catch (FormException fe) {
	    log.error("刪除Invoice明細失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("刪除Invoice明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("刪除Invoice明細時發生錯誤，原因：" + ex.getMessage());
	} 	
 }
    
    
    /**search PoHead and PoLine Data For ImReceiveItem (T2) for Receive 3.0
     * Call by ceap savePoDataAction
     * @param findObjs
     * @return List 
     */ 

    public void savePoLine2ReceiveItem( Long headId, List<PoPurchaseOrderLine> poLines, Double exchangeRate) throws Exception {
	log.info("savePoLine2ReceiveItem " + poLines.size() + " " + headId );
		if( poLines != null && poLines.size()>0 ){
		    ImReceiveHead imReceiveHead = imReceiveHeadDAO.findById( headId );
		    //int indexNo = 0;
		    int indexNo   = imReceiveItemMainService.findPageLineMaxIndex(headId).intValue();		// 取得LINE MAX INDEX
	
		    for(int i=0;i<poLines.size();i++){
		    	PoPurchaseOrderLine poLine = poLines.get(i);
				indexNo++;
				log.info(indexNo );
				PoPurchaseOrderHead poHead = poPurchaseOrderHeadMainService.findById( poLine.getPoPurchaseOrderHead().getHeadId() );
				//完稅進貨單帶入備註資料(wade)
				if(i==0){
					imReceiveHead.setRemark1(poHead.getReserve1() == null ? "" : poHead.getReserve1());
			    	imReceiveHead.setRemark2(poHead.getReserve2() == null ? "" : poHead.getReserve2());
				}
		    	
				ImReceiveItem imReceiveItem = new ImReceiveItem();
				imReceiveItem.setImReceiveHead(      imReceiveHead);
				imReceiveItem.setPoOrderNo(          poHead.getOrderNo());
				imReceiveItem.setPoOrderType(        poHead.getOrderTypeCode());
				imReceiveItem.setDeclarationBrand(   poHead.getBrandCode() );
				imReceiveItem.setDeclarationItemCode(poLine.getItemCode());
				imReceiveItem.setDeclarationItemName(poLine.getItemCName());
				imReceiveItem.setItemCode(           poLine.getItemCode() );
				imReceiveItem.setItemCName(          poLine.getItemCName());
				imReceiveItem.setQuantity(           poLine.getQuantity()-poLine.getReceiptedQuantity() );	// 採購未到貨數量
				imReceiveItem.setDeclarationQty(     imReceiveItem.getQuantity());
				imReceiveItem.setReserve3(			 poLines.get(i).getLineId().toString());	//Jerome 20161202存PoLineId
				
				
				
				//imReceiveItem.setUnitPrice(          poLine.getUnitPrice());
				//log.info(imReceiveHead.getBrandCode()+"/"+imReceiveHead.getOrderTypeCode()+"/"+poLine.getItemCode());
				//Maco 2016.04.19 進貨單零售價抓採購時採用商品即時價格
				imReceiveItem.setUnitPrice(imReceiveHeadMainService.getOriginalUnitPriceDouble(imReceiveHead.getBrandCode(), imReceiveHead.getOrderTypeCode(), poLine.getItemCode()));
				imReceiveItem.setForeignUnitPrice(   poLine.getForeignUnitCost());
				imReceiveItem.setForeignAmount(      imReceiveItem.getQuantity() * poLine.getForeignUnitCost());
				//如果採購單沒有上次進貨金額 則是撈取原幣乘匯率
				if(0 != NumberUtils.getDouble(poLine.getLastLocalUnitCost()))
					imReceiveItem.setLocalUnitPrice(     poLine.getLastLocalUnitCost());
				else
					imReceiveItem.setLocalUnitPrice(     poLine.getForeignUnitCost() * exchangeRate);
				imReceiveItem.setLocalAmount(        imReceiveItem.getQuantity()*imReceiveItem.getLocalUnitPrice());
				imReceiveItem.setAcceptQuantity(     imReceiveItem.getQuantity());
				imReceiveItem.setReceiptQuantity(    imReceiveItem.getQuantity());
				imReceiveItem.setBarcodeCount(       imReceiveItem.getQuantity());
				imReceiveItem.setDefectQuantity(     0D);
				imReceiveItem.setDiffQty(            0D);
				imReceiveItem.setShortQuantity(      0D);
				imReceiveItem.setSampleQuantity(     0D);
				imReceiveItem.setCreatedBy(          imReceiveHead.getLastUpdatedBy());
				imReceiveItem.setLastUpdatedBy(      imReceiveHead.getLastUpdatedBy());
				imReceiveItem.setCreationDate(       DateUtils.parseDate(DateUtils.format(new Date())));
				imReceiveItem.setLastUpdateDate(     DateUtils.parseDate(DateUtils.format(new Date())));	
				imReceiveItem.setIndexNo(            Long.valueOf(indexNo));
				
				imReceiveItemDAO.save( imReceiveItem );
		    }
		}
    }

    
    public Integer deleteProgramLog(Map parameterMap, Integer opType) throws Exception {
	log.info("deleteProgramLog opType = " + opType);
	Integer errorCnt = 0;
	try{
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Long headId = getFiInvoiceHeadId(formLinkBean);
            FiInvoiceHead fiInvoice = getActualFiInvoice(headId);
            //log.info("deleteProgramLog:"+fiInvoice.getBrandCode()+"-"+fiInvoice.getOrderTypeCode()+"-"+fiInvoice.getReserve1());
            String identification =
        	MessageStatus.getIdentification(fiInvoice.getBrandCode(), fiInvoice.getOrderTypeCode(), 
        					null==fiInvoice.getInvoiceNo()?"":fiInvoice.getReserve1());
            //log.info("deleteProgramLog:"+identification);
            if( opType==0 ){
        	// clear 原有 ERROR RECORD
        	siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
            }else{
        	List errorLogs = siProgramLogDAO.findByIdentification(PROGRAM_ID, MessageStatus.LOG_ERROR, identification);
        	if(null!=errorLogs)
        	    errorCnt = errorLogs.size();
            }
            return errorCnt;
	}catch (Exception ex) {
	    String errorString = "Invoice 刪除 Program Log 失敗，原因：" + ex.toString();
	    if(opType==1)
		errorString = "Invoice 查詢 Program Log 失敗，原因：" + ex.toString();
	    log.error(errorString);
	    throw new Exception(errorString);
	}
    }

	public void setCmDeclarationItemDAO(CmDeclarationItemDAO cmDeclarationItemDAO) {
		this.cmDeclarationItemDAO = cmDeclarationItemDAO;
	}
	
	/**find by pk
	 * @param headId
	 * @return
	 */
	public FiInvoiceHead findByIdForExport(Long headId) throws Exception{
	    String message = null;
	    FiInvoiceHead fiInvoiceHead = null;
	    try{ 
	    	fiInvoiceHead =(FiInvoiceHead) fiInvoiceHeadDAO.findByPrimaryKey( FiInvoiceHead.class, headId );
	    	List<FiInvoiceLine> lines = fiInvoiceHead.getFiInvoiceLines();
	    	for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
				FiInvoiceLine fiInvoiceLine = (FiInvoiceLine) iterator.next();
				ImItem item = imItemService.findItem(fiInvoiceHead.getBrandCode(), fiInvoiceLine.getItemCode());
				fiInvoiceLine.setMessage(item.getSupplierItemCode());
				fiInvoiceLine.setCategory15(item.getCategory15());
				fiInvoiceLine.setSpecWeight(item.getSpecWeight());
			}	    	
	    }catch (Exception ex) {
	    	message = "查無Invoice資料，原因：" + ex.getMessage();
	    	log.error(message);
	    	throw new Exception(message);
	    }
	    return fiInvoiceHead;
	}
	private void setLineOtherColumn (List<FiInvoiceLine> fiInvoiceLines,String brandCode)throws Exception{
		for (FiInvoiceLine fiInvoiceLine : fiInvoiceLines) {
			ImItem imItem = imItemService.findItem(brandCode, fiInvoiceLine.getItemCode() );
			fiInvoiceLine.setCategory15(imItem.getCategory15());
			fiInvoiceLine.setSpecWeight(imItem.getSpecWeight());
		}
	}
}