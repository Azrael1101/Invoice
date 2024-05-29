package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuPaymentTerm;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.FiBudgetHead;
import tw.com.tm.erp.hbm.bean.FiBudgetLine;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeApprovalDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierDAO;
import tw.com.tm.erp.hbm.dao.FiBudgetHeadDAO;
import tw.com.tm.erp.hbm.dao.FiBudgetLineDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveItemDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderLineDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.ValidateUtil;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.action.SiProgramLogAction;

/** 採購單 */
public class PoPurchaseOrderHeadMainService {
	private static final Log log = LogFactory.getLog(PoPurchaseOrderHeadMainService.class);
	public static final String PROGRAM_ID= "PO_POPURCHASE_ORDER_HEAD";
	private PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO;
	private BuBrandService buBrandService;
	private BuOrderTypeService buOrderTypeService;
	private BuBasicDataService buBasicDataService;
	private FiBudgetHeadService fiBudgetHeadService;
	private BuCommonPhraseService buCommonPhraseService;
	private BuSupplierWithAddressViewService buSupplierWithAddressViewService;
	private PoPurchaseOrderLineMainService poPurchaseOrderLineMainService;
	private PoPurchaseOrderLineDAO poPurchaseOrderLineDAO;
	private ImItemService imItemService;
	private ImItemCategoryService imItemCategoryService;
	private BuEmployeeWithAddressViewService buEmployeeWithAddressViewService;
	private FiBudgetHeadDAO fiBudgetHeadDAO;
	private FiBudgetLineDAO fiBudgetLineDAO;
	private NativeQueryDAO nativeQueryDAO;
	private ImOnHandDAO imOnHandDAO;
	private ImReceiveHeadService imReceiveHeadService;
	private BuSupplierDAO buSupplierDAO;
	private ImItemOnHandViewDAO imItemOnHandViewDAO;
	private ImItemDAO imItemDAO;
	private ImItemPriceDAO imItemPriceDAO;
	private ImWarehouseDAO imWarehouseDAO;
	private FiInvoiceLineDAO fiInvoiceLineDAO;
	private ImReceiveItemDAO imReceiveItemDAO;
	private BuOrderTypeApprovalDAO buOrderTypeApprovalDAO;
	private SiProgramLogAction siProgramLogAction;
	private ImItemCategoryDAO imItemCategoryDAO;
	private BuEmployeeDAO buEmployeeDAO;
	
	public static final String[] GRID_FIELD_NAMES = { 
		"indexNo", "category02", "category02Name", "itemCode", "itemCName",
		"stockOnHandQty", "unitPrice", "lastLocalUnitCost", "quantity", "foreignUnitCost",
		"localUnitCost", "foreignPurchaseAmount", "localPurchaseAmount", "unitPriceAmount", "scheduleReceiptDate", 
		"actualPurchaseQuantity" ,"receiptedQuantity" ,"outstandQuantity" ,"outstandAmount", "returnedQuantity" ,
		"returnedAmount", "purchaseUnit", "itemBrand", "supplierItemCode", "margin",
		"nextPriceAdjustDate", "nextAdjustPrice", "minPurchaseQuantity", "maxPurchaseQuantity", "remark",
		"lineId", "isDeleteRecord"
	};

	public static final int[] GRID_FIELD_TYPES = { 
		AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DATE, 
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DATE,   AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING
	};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
    	"", "", "", "", "",   
    	"0", "0", "0", "0", "0",
    	"0", "0", "0", "0", "",
    	"0", "0", "0", "0", "0",
    	"0", "", "", "", "", 
    	"", "", "", "", "", 
    	"", AjaxUtils.IS_DELETE_RECORD_FALSE
    };
	
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		 "orderNo", "quotationCode", "poOrderNo", "superintendentName", "status", "sourceOrderNo", "lastUpdateDate", "headId"};

	public static final int[] GRID_SEARCH_FIELD_TYPES = {
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {"", "", "", "", "", "", "", "0" }; 

	public static final String PO_WAREHOUSE_CODE = "00"; // BRAND_CODE + 00
	// -> 進貨單 預設
	// WAREHOUSE_CODE
	public static final String PO_RETURN_WAREHOUSE_CODE = "AE"; // BRAND_CODE +
	
	public void setPoPurchaseOrderHeadDAO(PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO) {
		this.poPurchaseOrderHeadDAO = poPurchaseOrderHeadDAO;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}

	public void setFiBudgetHeadService(FiBudgetHeadService fiBudgetHeadService) {
		this.fiBudgetHeadService = fiBudgetHeadService;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}
	
	public void setBuSupplierWithAddressViewService(BuSupplierWithAddressViewService buSupplierWithAddressViewService) {
		this.buSupplierWithAddressViewService = buSupplierWithAddressViewService;
	}

	public void setPoPurchaseOrderLineMainService(PoPurchaseOrderLineMainService poPurchaseOrderLineMainService) {
		this.poPurchaseOrderLineMainService = poPurchaseOrderLineMainService;
	}

	public void setImItemService(ImItemService imItemService) {
		this.imItemService = imItemService;
	}

	public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}
	
	public void setBuEmployeeWithAddressViewService(BuEmployeeWithAddressViewService buEmployeeWithAddressViewService) {
		this.buEmployeeWithAddressViewService = buEmployeeWithAddressViewService;
	}

	public void setPoPurchaseOrderLineDAO(PoPurchaseOrderLineDAO poPurchaseOrderLineDAO) {
		this.poPurchaseOrderLineDAO = poPurchaseOrderLineDAO;
	}

	public void setFiBudgetHeadDAO(FiBudgetHeadDAO fiBudgetHeadDAO) {
		this.fiBudgetHeadDAO = fiBudgetHeadDAO;
	}
	
	public void setFiBudgetLineDAO(FiBudgetLineDAO fiBudgetLineDAO) {
		this.fiBudgetLineDAO = fiBudgetLineDAO;
	}

	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}

	public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
		this.imOnHandDAO = imOnHandDAO;
	}

	public void setImReceiveHeadService(ImReceiveHeadService imReceiveHeadService) {
		this.imReceiveHeadService = imReceiveHeadService;
	}

	public void setBuSupplierDAO(BuSupplierDAO buSupplierDAO) {
		this.buSupplierDAO = buSupplierDAO;
	}

	public ImItemOnHandViewDAO getImItemOnHandViewDAO() {
		return imItemOnHandViewDAO;
	}

	public void setImItemOnHandViewDAO(ImItemOnHandViewDAO imItemOnHandViewDAO) {
		this.imItemOnHandViewDAO = imItemOnHandViewDAO;
	}

	public FiInvoiceLineDAO getFiInvoiceLineDAO() {
		return fiInvoiceLineDAO;
	}

	public void setFiInvoiceLineDAO(FiInvoiceLineDAO fiInvoiceLineDAO) {
		this.fiInvoiceLineDAO = fiInvoiceLineDAO;
	}

	public ImReceiveItemDAO getImReceiveItemDAO() {
		return imReceiveItemDAO;
	}

	public void setImReceiveItemDAO(ImReceiveItemDAO imReceiveItemDAO) {
		this.imReceiveItemDAO = imReceiveItemDAO;
	}

	public BuOrderTypeApprovalDAO getBuOrderTypeApprovalDAO() {
		return buOrderTypeApprovalDAO;
	}

	public void setBuOrderTypeApprovalDAO(BuOrderTypeApprovalDAO buOrderTypeApprovalDAO) {
		this.buOrderTypeApprovalDAO = buOrderTypeApprovalDAO;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	    this.siProgramLogAction = siProgramLogAction;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
	    this.imItemDAO = imItemDAO;
	}
	
	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
	    this.imWarehouseDAO = imWarehouseDAO;
	}

	public ImItemPriceDAO getImItemPriceDAO() {
	    return imItemPriceDAO;
	}

	public void setImItemPriceDAO(ImItemPriceDAO imItemPriceDAO) {
	    this.imItemPriceDAO = imItemPriceDAO;
	}

	public BuBrandService getBuBrandService() {
	    return buBrandService;
	}

	public void setBuBrandService(BuBrandService buBrandService) {
	    this.buBrandService = buBrandService;
	}
	
	public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}
	
	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
		this.buEmployeeDAO = buEmployeeDAO;
	}
	
	
	/**save and update
	 * @param modifyObj
	 * @return
	 * @throws Exception
	 */
	public String create(PoPurchaseOrderHead modifyObj) throws FormException, Exception {
	    log.info("PoPurchaseOrderHeadMainService.create");
	    log.info("HeadId="+modifyObj.getHeadId()+" OrderTypeCode= "+modifyObj.getOrderTypeCode()+" OrderNo="+modifyObj.getOrderNo()+ " Status="+modifyObj.getStatus());
	    if (null != modifyObj) {
		if (modifyObj.getHeadId() == null) {
		    setDefActualPurchaseQuantity(modifyObj);
		    return save(modifyObj);
		} else {
		    return update(modifyObj);
		}
	    } else {
		throw new FormException("查無表單主檔資料");
	    }
	}

	
	/**save
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String save(PoPurchaseOrderHead saveObj) throws FormException, Exception {
	    log.info("PoPurchaseOrderHeadMainService.save");
	    if (!StringUtils.hasText(saveObj.getCloseOrder()))
		saveObj.setCloseOrder(PoPurchaseOrderHead.CLOSE_ORDER_N);
	    if (AjaxUtils.isTmpOrderNo(saveObj.getOrderNo())) {
	    	saveObj.setOrderNo(buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), saveObj.getOrderTypeCode()));
	    }
	    //countHeadTotalAmount(saveObj);
	    saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
	    saveObj.setLastUpdateDate(new Date());
	    saveObj.setCreationDate(new Date());
		
	    poPurchaseOrderHeadDAO.save(saveObj);
	    // log.info("PoPurchaseOrderHeadMainService.save end ");
	    return MessageStatus.SUCCESS;
	}

	
	/**save tmp
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String saveTmp(PoPurchaseOrderHead saveObj) throws FormException, Exception {
	    log.info("PoPurchaseOrderHeadMainService.saveTmp");
	    String tmpOrderNo = AjaxUtils.getTmpOrderNo();
	    log.info("PoPurchaseOrderHeadMainService.saveTmp"+tmpOrderNo);
	    saveObj.setOrderNo(tmpOrderNo);
	    saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
	    saveObj.setLastUpdateDate(new Date());
	    saveObj.setCreationDate(new Date());
	    poPurchaseOrderHeadDAO.save(saveObj);
	    return MessageStatus.SUCCESS;
	}

	
	/**update
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String update(PoPurchaseOrderHead updateObj) throws FormException, Exception {
	    log.info("PoPurchaseOrderHeadMainService.update");
	    if (!StringUtils.hasText(updateObj.getCloseOrder()))
		updateObj.setCloseOrder(PoPurchaseOrderHead.CLOSE_ORDER_N);
	    //countHeadTotalAmount(updateObj);
	    updateObj.setLastUpdateDate(new Date());
	    poPurchaseOrderHeadDAO.update(updateObj);
	    return MessageStatus.SUCCESS;
	}
	
	
	/**find by pk
	 * @param headId
	 * @return
	 */
	public PoPurchaseOrderHead findById(Long headId) throws Exception{
	    log.info("PoPurchaseOrderHeadMainService.findById");
	    String message = null;
	    PoPurchaseOrderHead poPurchaseOrderHead = null;
	    try{ 
	    	poPurchaseOrderHead =(PoPurchaseOrderHead) poPurchaseOrderHeadDAO.
	    		findByPrimaryKey( PoPurchaseOrderHead.class, headId );
	    }catch (Exception ex) {
	    	message = "查無採購單資料，原因：" + ex.getMessage();
	    	log.error(message);
	    	throw new Exception(message);
	    }
	    return poPurchaseOrderHead;
	}

	
	/**計算所有的合計
	 * @param countObj
	 * @throws FormException
	 * @throws NumberFormatException
	 */
	public void countHeadTotalAmount(PoPurchaseOrderHead countObj)
		throws NumberFormatException, FormException, Exception {
	    log.info("PoPurchaseOrderHeadMainService.countHeadTotalAmount");
	    log.info("取得欲更新的bean="+countObj.getTotalRemainderBudget());
	    //List errorMsgs = new ArrayList(0);
	    BuBrand buBrand  = buBrandService.findById( countObj.getBrandCode() );
	    BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(countObj.getBrandCode(), countObj.getOrderTypeCode()));
	    
	    List<PoPurchaseOrderLine> poPurchaseOrderLines = countObj.getPoPurchaseOrderLines();
	    if (null != poPurchaseOrderLines) {
		countObj.setTotalForeignPurchaseAmount(new Double(0));
		countObj.setTotalLocalPurchaseAmount(new Double(0));
		countObj.setTotalUnitPriceAmount(new Double(0));
		countObj.setTotalProductCounts(new Double(0));
		countObj.setTotalUsedPriceAmount(new Double(0));		// 本次使用預算
		//log.info("poPurchaseOrderLines.size =" + poPurchaseOrderLines.size()+" unitPriceAmt:"+countObj.getTotalUnitPriceAmount());
		for (PoPurchaseOrderLine poPurchaseOrderLine : poPurchaseOrderLines) {
		    if(OrderStatus.SAVE.equals(countObj.getStatus()) || OrderStatus.REJECT.equals(countObj.getStatus()) ){
		    	setPoLineInfo(buOrderType, countObj, poPurchaseOrderLine);
		    	countItemTotalAmount(countObj, poPurchaseOrderLine );
		    }
		    countObj.setTotalForeignPurchaseAmount( countObj.getTotalForeignPurchaseAmount() + poPurchaseOrderLine.getForeignPurchaseAmount());
		    countObj.setTotalLocalPurchaseAmount(   countObj.getTotalLocalPurchaseAmount()   + poPurchaseOrderLine.getLocalPurchaseAmount());
		    countObj.setTotalProductCounts(         countObj.getTotalProductCounts()         + poPurchaseOrderLine.getQuantity());
		    countObj.setTotalUnitPriceAmount(       countObj.getTotalUnitPriceAmount()       + poPurchaseOrderLine.getUnitPriceAmount());
		    if ( "C".equals(buBrand.getBudgetType()) ) {	// 預算扣除方式 P:UnitPrice/C:Cost/T:整筆扣除
			countObj.setTotalUsedPriceAmount( countObj.getTotalUsedPriceAmount() + 
				NumberUtils.round( poPurchaseOrderLine.getForeignUnitCost() * 
						   poPurchaseOrderLine.getQuantity() *
						   countObj.getExchangeRate(), 2));
		    } else {
			countObj.setTotalUsedPriceAmount( countObj.getTotalUsedPriceAmount() + 
				NumberUtils.round( poPurchaseOrderLine.getUnitPrice() * poPurchaseOrderLine.getQuantity(), 2));
		    }
		}
		setTaxRate(countObj);
		countObj.setTotalForeignPurchaseAmount(NumberUtils.round(countObj.getTotalForeignPurchaseAmount(), 4));
		countObj.setTaxAmount(NumberUtils.round(countObj.getTaxRate() * countObj.getTotalLocalPurchaseAmount() / 100, 0));
		countObj.setTotalLocalPurchaseAmount(NumberUtils.round(countObj.getTotalLocalPurchaseAmount(), 0));
		countHeadBudget(countObj);
	    }
	}

	
	/** 計算ITEM的合計
	 * @param countObj
	 * @throws FormException
	 * @throws NumberFormatException
	 */
	public void countItemTotalAmount(PoPurchaseOrderHead headObj, PoPurchaseOrderLine itemObj ) {
	    log.info("PoPurchaseOrderHeadMainService.countItemTotalAmount");
	    
	    if (null != headObj && null != itemObj.getForeignUnitCost() && null != headObj.getExchangeRate() && null != itemObj.getQuantity()) {
	    	//查詢商品現價
	    	List imItemCurrentPriceViews = imItemService.getImItemCurrentPriceView(headObj.getBrandCode(), itemObj.getItemCode());
	    	if ((null != imItemCurrentPriceViews) && (imItemCurrentPriceViews.size() > 0)) {
	    		ImItemCurrentPriceView imItemCurrentPriceView = (ImItemCurrentPriceView) imItemCurrentPriceViews.get(0);
	    		itemObj.setUnitPrice(imItemCurrentPriceView.getUnitPrice());
	    	}
    		// 台幣單價
    		itemObj.setLocalUnitCost(NumberUtils.round(itemObj.getForeignUnitCost() * headObj.getExchangeRate(), 2));
    		// 台幣單價合計
    		itemObj.setLocalPurchaseAmount(NumberUtils.round(itemObj.getLocalUnitCost() * itemObj.getQuantity(), 2));
	    	// 台幣售價合計
	    	itemObj.setUnitPriceAmount(NumberUtils.round(itemObj.getUnitPrice() * itemObj.getQuantity(), 2));
	    	// 原幣合計
	    	itemObj.setForeignPurchaseAmount(NumberUtils.round(itemObj.getForeignUnitCost() * itemObj.getQuantity(), 2));
	    	
		    Double margin  = 100D;
		    if(0 != NumberUtils.getDouble(itemObj.getUnitPrice()))
		    	margin -= (itemObj.getForeignUnitCost() * headObj.getExchangeRate()/itemObj.getUnitPrice())*100;
		    itemObj.setMargin(margin);
		    log.info("countItem="+headObj.getTotalBudget());
		    log.info("countItem="+headObj.getTotalRemainderBudget());
	    }

	    //未交量金額
	    itemObj.setOutstandQuantity(itemObj.getActualPurchaseQuantity()-itemObj.getReceiptedQuantity());
	    itemObj.setOutstandAmount(itemObj.getOutstandQuantity() * itemObj.getLocalUnitCost());
	}

	
	public void setPoLineInfo(BuOrderType buOrderType, PoPurchaseOrderHead head,  PoPurchaseOrderLine line){
		
		ImItem imItem = imItemDAO.findItem(head.getBrandCode(), line.getItemCode());
   	    if( null==imItem ){
	   		line.setItemCName("無此商品");
	   		line.setItemBrand("");
	   		line.setSupplierItemCode("") ;
	   		line.setMinPurchaseQuantity(0D) ;
	   		line.setMaxPurchaseQuantity(0D) ;
	   		line.setItemMargin(0D);
   	    }else{
	   		line.setItemCName(imItem.getItemCName());
	   		line.setPurchaseUnit(imItem.getPurchaseUnit());
	   		line.setItemBrand(imItem.getItemBrand());
	   		line.setSupplierItemCode(imItem.getSupplierItemCode()) ;
	   		line.setMinPurchaseQuantity( NumberUtils.getDouble(imItem.getMinPurchaseQuantity())) ;
	   		line.setMaxPurchaseQuantity( NumberUtils.getDouble(imItem.getMaxPurchaseQuantity())) ;
	   		line.setItemMargin( NumberUtils.getDouble(imItem.getMargen()));
	   		line.setCategory02(imItem.getCategory02());
		    if(null == line.getForeignUnitCost()){
		    	if("T2".equals(head.getBrandCode()))
		    		line.setForeignUnitCost(imItem.getPurchaseAmount());
		    	else
		    		line.setForeignUnitCost(imItem.getSupplierQuotationPrice());
		    }
   	    }
   	    
	    if(null == line.getNextPriceAdjustDate()){
			ImItemPrice imItemPrice = imItemPriceDAO.
				getItemPriceByNextBeginDate(line.getItemCode(), buOrderType.getPriceType(), head.getPurchaseOrderDate(), "Y" );
			if( null != imItemPrice ){
				line.setNextPriceAdjustDate(imItemPrice.getBeginDate());
				line.setNextAdjustPrice(imItemPrice.getUnitPrice());
			}
		}
	}
	
	/**計算PO單預算
	 * @param headObj
	 */
	public void countHeadBudget(PoPurchaseOrderHead headObj) throws Exception {
	    log.info("countHeadBudget"+headObj.getTotalRemainderBudget());
	    // branchCode.equals("2") 以T2方式用採購成本計算
	    BuBrand buBrand       = buBrandService.findById( headObj.getBrandCode() );
	    String branchCode     = buBrand.getBranchCode();
	    Map budgetSummary =null;
	    try {
		FiBudgetHead fiBudgetHead = findBudgetHead( headObj );
		if (null == fiBudgetHead ) {
		    //throw new ValidationErrorException(headObj.getErrorMessage());
		}else{														//暫時註解
		    if(headObj.getBrandCode().equals("T2")){
		    	log.info("countT2");
		    	 budgetSummary = fiBudgetLineDAO.getSummaryTotalT2addCategory(fiBudgetHead.getHeadId(),headObj.getBudgetLineId(),headObj.getCategory01());
		    }else{
		    	log.info("countT1 & other");
		    	 budgetSummary = fiBudgetLineDAO.getSummaryTotal(fiBudgetHead.getHeadId());
		    	}
		    headObj.setTotalBudget(         (Double)budgetSummary.get("budgetAmount"));
		    headObj.setTotalSigningBudget(  (Double)budgetSummary.get("signingAmount"));
		    headObj.setTotalAppliedBudget(  (Double)budgetSummary.get("poActualAmount"));
		    headObj.setTotalRemainderBudget( NumberUtils.round(
			    				(Double)budgetSummary.get("budgetAmount") -
			    				(Double)budgetSummary.get("signingAmount") -
			    				(Double)budgetSummary.get("poActualAmount") +
			    				(Double)budgetSummary.get("poReturnAmount"),4) );
		    log.info( "countHeadBudget :" + headObj.getTotalRemainderBudget() + "countTotal :"+headObj.getTotalBudget() );
		    log.info( "Amount :" + (Double)budgetSummary.get("budgetAmount") + "signing :"+(Double)budgetSummary.get("signingAmount") );
		    log.info( "poActual :" + (Double)budgetSummary.get("poActualAmount") + "poReturn :"+(Double)budgetSummary.get("poReturnAmount") );
		}
	    }catch (Exception ex) {
		log.error("採購單計算預算時發生錯誤，原因：" + ex.toString());
		throw new ValidationErrorException("採購單計算預算時發生錯誤，原因：" + ex.getMessage());
	    }   
	}

	
	/** Call By Ceap Flow when CEO signing
	 * 取得使用預算比例 = (已核准採購金額 + 簽核中預算) / 總預算
	 * @param headObj
	 * @return
	 */
	public double getBudget(PoPurchaseOrderHead headObj) throws Exception{
		log.info("PoPurchaseOrderHeadMainService.getBudget");
		Double allItemTotalAmount = headObj.getTotalLocalPurchaseAmount();
		Double totalBugetItemAmount = new Double(0);
		Double totalPoActualAmount  = new Double(0);

		FiBudgetHead fiBudgetHead = findBudgetHead( headObj );
		if (null != fiBudgetHead ) {											//暫時註解
		    Map budgetSummary = fiBudgetLineDAO.getSummaryTotal(fiBudgetHead.getHeadId());
		    totalBugetItemAmount = (Double)budgetSummary.get("budgetAmount");
		    totalPoActualAmount  = (Double)budgetSummary.get("poActualAmount") + (Double)budgetSummary.get("signingAmount");
		    totalBugetItemAmount = 0==totalBugetItemAmount ? 1 : totalBugetItemAmount;
		    totalPoActualAmount  = 0==totalPoActualAmount  ? 1 : totalPoActualAmount;
		    return totalPoActualAmount / totalBugetItemAmount;
		}else{
		    return 0;
		}
		
	}
	
	
	/**
	 * 處理AJAX 供應商及相關資料 call by JS
	 * 
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataBySupplier(Properties httpRequest) throws Exception {
		log.info("getAJAXFormDataBySupplierCode");
		Properties pro = new Properties();
		List re = new ArrayList();
		Long   addressBookId    = NumberUtils.getLong(httpRequest.getProperty("addressBookId"));
		String supplierCode     = httpRequest.getProperty("supplierCode");
		String organizationCode = httpRequest.getProperty("organizationCode");
		String brandCode        = httpRequest.getProperty("brandCode");
		String orderDate        = httpRequest.getProperty("orderDate");

		if( !StringUtils.hasText(supplierCode)) {
		    BuSupplierWithAddressView buSWAVaddressBookId = 
			buSupplierWithAddressViewService.findSupplierByAddressBookIdAndBrandCode(addressBookId, brandCode );
		    supplierCode = buSWAVaddressBookId.getSupplierCode();
		}

		log.info("getAJAXFormDataBySupplierCode brandCode=" + brandCode + ",supplierCode=" + supplierCode);
		BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(brandCode, supplierCode);
		log.info("warehouse: "+buSWAV.getDefault_warehouse_code());
		if (null != buSWAV) {
		    	pro.setProperty("SupplierCode",    AjaxUtils.getPropertiesValue(buSWAV.getSupplierCode(),    ""));
			pro.setProperty("SupplierName",    AjaxUtils.getPropertiesValue(buSWAV.getChineseName(),     ""));
			pro.setProperty("CountryCode",     AjaxUtils.getPropertiesValue(buSWAV.getCountryCode(),     ""));
			pro.setProperty("CurrencyCode",    AjaxUtils.getPropertiesValue(buSWAV.getCurrencyCode(),    ""));
			pro.setProperty("PaymentTermCode", AjaxUtils.getPropertiesValue(buSWAV.getPaymentTermCode(), ""));
			pro.setProperty("PriceTermCode",   AjaxUtils.getPropertiesValue(buSWAV.getPriceTermCode(),   ""));
			//wade, 價格條件
			String priceTermCodeName = buCommonPhraseService.getLineName("TradeTeam", AjaxUtils.getPropertiesValue(buSWAV.getPriceTermCode(), ""));
			pro.setProperty("PriceTermCodeName", priceTermCodeName.equals("unknow") ? "" : priceTermCodeName);
			pro.setProperty("ContactPerson",   AjaxUtils.getPropertiesValue(buSWAV.getContractPerson(),  ""));
			pro.setProperty("TaxType",         AjaxUtils.getPropertiesValue(buSWAV.getTaxType(),         ""));
			pro.setProperty("TaxRate",         AjaxUtils.getPropertiesValue(getTaxRate(buSWAV.getTaxType()), "0"));
			pro.setProperty("CategoryType",    AjaxUtils.getPropertiesValue(buSWAV.getCategoryType(),    ""));
			pro.setProperty("DefaultWarehouseCode",    AjaxUtils.getPropertiesValue(buSWAV.getDefault_warehouse_code(),    ""));
			if (null==orderDate) {
			    pro.setProperty("ExchangeRate",    
					  AjaxUtils.getPropertiesValue(getExchangeRateByCurrencyCode(organizationCode, buSWAV.getCurrencyCode()), "1"));
			} else {
			    Date od = DateUtils.parseDate("yyyy/MM/dd", orderDate);
			    pro.setProperty("ExchangeRate", getExchangeRateByCurrencyCode(organizationCode, buSWAV.getCurrencyCode(), od));
			    
			}
		} else {
			pro.setProperty("CountryCode",     "");
			pro.setProperty("CurrencyCode",    "");
			pro.setProperty("PaymentTermCode", "");
			pro.setProperty("PriceTermCode",   "");
			pro.setProperty("ContactPerson",   "");
			pro.setProperty("TaxType",         "");
			pro.setProperty("SupplierName",    "");
			pro.setProperty("TaxRate",        "0");
			pro.setProperty("ExchangeRate",   "0");
		}
		re.add(pro);
		return re;
	}
	
	
    /**處理AJAX 採購負責人 call by JS
     * @param httpRequest
     * @return
     */
    public List<Properties> getAJAXFormDataBySuperintendent(Properties httpRequest) {
	log.info("getAJAXFormDataBySuperintendent");
	Properties pro = new Properties();
	List re = new ArrayList();
	String superintendentCode = httpRequest.getProperty("superintendentCode");
	pro.setProperty("superintendentName", UserUtils.getUsernameByEmployeeCode(superintendentCode));
	re.add(pro);
	return re;
    }
	
	/**
	 * AJAX Line Change
	 * 
	 * @param headObj(brandCode,orderTypeCode,itemCode)
	 * @throws FormException
	 */
	public List<Properties> getAJAXLineData(Properties httpRequest) throws FormException {
	    log.info("PoPurchaseOrderHeadMainService.getAJAXLineData");
	    List re = new ArrayList();
	    String branchCode    = httpRequest.getProperty("branchCode");
	    String brandCode     = httpRequest.getProperty("brandCode");
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	    String itemCode      = httpRequest.getProperty("itemCode");
	    Date purchaseOrderDate = 
		DateUtils.parseDate("yyyy/MM/dd", httpRequest.getProperty("purchaseOrderDate"));
	    Double exchangeRate    = NumberUtils.getDouble(httpRequest.getProperty("exchangeRate"));
	    if(exchangeRate == 0D)
	    	exchangeRate = 1D;
	    Double quantity    = NumberUtils.getDouble(httpRequest.getProperty("quantity"));
	    if(quantity == 0D)
	    	quantity = 1D;
	    
	    if (StringUtils.hasText(brandCode) && StringUtils.hasText(orderTypeCode) && StringUtils.hasText(itemCode)) {
		try {
		    Properties pro = new Properties();
		    Double unitPrice = poPurchaseOrderLineMainService.getOriginalUnitPrice(brandCode, orderTypeCode,itemCode);
		    Double foreignUnitCost     = poPurchaseOrderLineMainService.getLastUnitCost(itemCode);
		    Double lastLocalUnitCost   = poPurchaseOrderLineMainService.getAverageUnitCost(itemCode, brandCode);
		    Double lastForeignUnitCost = foreignUnitCost;
		    ImItem imItem = imItemService.findItem(brandCode, itemCode);
		    String itemCName = "無此商品", itemBrand = "", supplierItemCode = "", maxPurchaseQuantity = "0", minPurchaseQuantity = "0", category02="", category02Name="";
		    Double margin = 100D;
		    if ( null != imItem ){
				itemCName           = AjaxUtils.getPropertiesValue( imItem.getItemCName(),           itemCName ); 
				itemBrand           = AjaxUtils.getPropertiesValue( imItem.getItemBrand(),           itemBrand );
				supplierItemCode    = AjaxUtils.getPropertiesValue( imItem.getSupplierItemCode(),    supplierItemCode);
				maxPurchaseQuantity = AjaxUtils.getPropertiesValue( imItem.getMaxPurchaseQuantity(), maxPurchaseQuantity);
				minPurchaseQuantity = AjaxUtils.getPropertiesValue( imItem.getMinPurchaseQuantity(), minPurchaseQuantity);
				category02			= AjaxUtils.getPropertiesValue( imItem.getCategory02(), category02);
				ImItemCategory itemCategoryPO = imItemCategoryService.findById(brandCode, ImItemCategoryDAO.CATEGORY02, category02);
		   	    if(null != itemCategoryPO)
		   	    	category02Name = itemCategoryPO.getCategoryName(); // 中類名稱
				if("2".equals(branchCode)){	// T2
				    foreignUnitCost =  imItem.getPurchaseAmount();	// Anber指示：小花轉入外幣單價
				}
		    }
		    
		    Double localUnitCost = NumberUtils.round(exchangeRate * foreignUnitCost,2);
		    Double foreignPurchaseAmount = NumberUtils.round(foreignUnitCost * quantity,2);
		    Double localPurchaseAmount = NumberUtils.round(localUnitCost * quantity,2);
		    
		    // 依商品編號撈取庫存數量
		    /*
		    List onHandQtyByItemCodes = imItemOnHandViewDAO.getOnHandQtyByItemCodes("'" + itemCode + "'");
		    Double stockOnHandQty = new Double(0);
		    if (onHandQtyByItemCodes.size() > 0) {
				Object[] itemCodeAndQty = (Object[]) onHandQtyByItemCodes.get(0);
				//log.info("XXXXXXXXXX:"+ onHandQtyByItemCodes.size() +"-"+ (String) itemCodeAndQty[0] +"-"+ (Double) itemCodeAndQty[1] );
				stockOnHandQty = (Double) itemCodeAndQty[1];
		    }*/
		    
		    Double stockOnHandQty = new Double(0);
		    stockOnHandQty = imOnHandDAO.getCurrentStockOnHandQuantity("TM", brandCode, itemCode);
		    
		    Date nextPriceAdjustDate = null;	// 取下次變價日期
		    Double nextAdjustPrice = null;	// 取下次變價金額
		    BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode) );
		    if ( null != buOrderType) {
				String priceType =  buOrderType.getPriceType();
				ImItemPrice imItemPrice = imItemPriceDAO.getItemPriceByNextBeginDate(itemCode, priceType, purchaseOrderDate, "Y" );
				if( null != imItemPrice ){
				    nextPriceAdjustDate = imItemPrice.getBeginDate();
				    nextAdjustPrice = imItemPrice.getUnitPrice();
				}
		    }
		    //銷售毛利率={1-成本/售價)
		    if(0 != NumberUtils.getDouble(unitPrice))
		    	margin -= ((foreignUnitCost*exchangeRate)/unitPrice)*100;
		    pro.setProperty("UnitPrice",           	OperationUtils.roundToStr(unitPrice,4));
		    pro.setProperty("ForeignUnitCost",     	OperationUtils.roundToStr(foreignUnitCost,4));
		    pro.setProperty("LocalUnitCost",       	OperationUtils.roundToStr(localUnitCost,2));
		    pro.setProperty("ForeignPurchaseAmount",OperationUtils.roundToStr(foreignPurchaseAmount,2));
		    pro.setProperty("LocalPurchaseAmount", 	OperationUtils.roundToStr(localPurchaseAmount,2));
		    pro.setProperty("LastForeignUnitCost", 	OperationUtils.roundToStr(lastForeignUnitCost,4));
		    pro.setProperty("LastLocalUnitCost",   	OperationUtils.roundToStr(lastLocalUnitCost,2));
		    pro.setProperty("ItemCName",           	itemCName);
		    pro.setProperty("StockOnHandQty",      	AjaxUtils.getPropertiesValue(stockOnHandQty,"0"));
		    pro.setProperty("ItemBrand",           	itemBrand);		//商品品牌    2009.09.24 arthur
		    pro.setProperty("SupplierItemCode",    	supplierItemCode);	//廠商貨號    2009.09.24 arthur
		    pro.setProperty("Margin",              	OperationUtils.roundToStr(margin,2));//毛利率         2009.09.24 arthur
		    pro.setProperty("MaxPurchaseQuanity",  	maxPurchaseQuantity);//最高採購量 2009.09.24 arthur
		    pro.setProperty("MinPurchaseQuanity",  	minPurchaseQuantity);//最低採購量 2009.09.24 arthur
		    pro.setProperty("Category02", 			category02);//中類代碼 2010.03.30
		    pro.setProperty("Category02Name",  		category02Name);//中類代碼 2010.03.30
		    pro.setProperty("NextPriceAdjustDate", null == DateUtils.format(nextPriceAdjustDate,DateUtils.C_DATE_PATTON_SLASH) ? 
		    		"" : DateUtils.format(nextPriceAdjustDate,DateUtils.C_DATE_PATTON_SLASH));//下次變價日
		    pro.setProperty("NextAdjustPrice", 		OperationUtils.roundToStr(nextAdjustPrice,0));//下次變價金額
 		    re.add(pro);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    return re;
	}

	
	/**利用幣別指定匯率
	 * @param headObj
	 */
	public void setExchangeRateByCurrencyCode(String organizationCode, PoPurchaseOrderHead headObj) {
	    log.info("PoPurchaseOrderHeadMainService.setExchangeRateByCurrencyCode");
	    headObj.setExchangeRate(new Double(1));
	    if ((null != organizationCode) && (null != headObj.getCurrencyCode())) {
		BuExchangeRate bxr = buBasicDataService.getLastExchangeRate(organizationCode, headObj.getCurrencyCode(),
				     	buCommonPhraseService.getBuCommonPhraseLineName("SystemConfig", "LocalCurrency"));
		if (null != bxr) {
		    headObj.setExchangeRate(bxr.getExchangeRate());
    		}
	    }
	}
	

	/**指定稅率
	 * @param headObj
	 */
	public void setTaxRate(PoPurchaseOrderHead headObj) {
	    log.info("PoPurchaseOrderHeadMainService.setTaxRate headObj.getTaxType=" + headObj.getTaxType());
	    if ((null != headObj) && (null != headObj.getOrderTypeCode()) && (null != headObj.getTaxType())) {
		if (headObj.getTaxType().equalsIgnoreCase(PoPurchaseOrderHead.PURCHASE_ORDER_TAX)) {
		    headObj.setTaxRate(new Double(5d));
		} else {
		    headObj.setTaxRate(new Double(0));
		}
	    }
	}



    /**AJAX 取得預算及合計資料 , 如果有移除的動作這個階段不會作 , 要等到 SAVE OR SUBMIT
     * @param headObj
     * @throws FormException
     * @throws NumberFormatException
     */
	public List<Properties> updateAJAXHeadTotalAmount(Properties httpRequest) 
	throws NumberFormatException, FormException, Exception{
		log.info("PoPurchaseOrderHeadMainService.getAJAXHeadTotalAmount ");

		List re = new ArrayList();
		String headId       = httpRequest.getProperty("headId");
		Double taxRate      = NumberUtils.getDouble(httpRequest.getProperty("taxRate"));
		String purchaseType = httpRequest.getProperty("purchaseType");
		String budgetYear   = httpRequest.getProperty("budgetYear");
		String budgetMonth  = httpRequest.getProperty("budgetMonth");
		String categoryType = httpRequest.getProperty("categoryType");
		String category01 = httpRequest.getProperty("category01");
		Long itemBrandCode = NumberUtils.getLong(httpRequest.getProperty("itemBrandCode"));
		PoPurchaseOrderHead poHead = findById(NumberUtils.getLong(headId));
		if (null==poHead){
			log.info("headId is null");
			return re;
		}

		BuBrand buBrand  = buBrandService.findById( poHead.getBrandCode() );
		String tmpSqlLine = 
			" select nvl(sum( nvl(QUANTITY,0) * nvl(FOREIGN_UNIT_COST,0) ) ,0), " +
			"        nvl(sum( nvl(QUANTITY,0) * nvl(LOCAL_UNIT_COST,0) ) ,0),  " +
			"        nvl(sum( nvl(QUANTITY,0) * nvl(UNIT_PRICE,0) ) ,0),  " +
			"        nvl(sum( nvl(QUANTITY,0) ) ,0),  " +
			"        nvl(sum( nvl(RECEIPTED_QUANTITY,0) * nvl(LOCAL_UNIT_COST,0) ) ,0),  " +
			"        nvl(sum( (nvl(ACTUAL_PURCHASE_QUANTITY,0) - nvl(RECEIPTED_QUANTITY,0)) * nvl(LOCAL_UNIT_COST,0) ) ,0),  " +
			"        nvl(sum( nvl(RETURNED_QUANTITY,0) * nvl(LOCAL_UNIT_COST,0) ) ,0)  " +
			" from po_purchase_order_line where HEAD_ID=" + headId + " and nvl(IS_DELETE_RECORD,'0')<>'1' ";
		List lineList = nativeQueryDAO.executeNativeSql(tmpSqlLine);
		Object[] obj = (Object[]) lineList.get(0);
		log.info("show~~~listBudget"+((BigDecimal) obj[0]).doubleValue());
		poHead.setTotalForeignPurchaseAmount( ((BigDecimal) obj[0]).doubleValue() );
		poHead.setTotalLocalPurchaseAmount(   ((BigDecimal) obj[1]).doubleValue() );
		poHead.setTotalUnitPriceAmount( ((BigDecimal) obj[2]).doubleValue() );
		poHead.setTotalProductCounts( ((BigDecimal) obj[3]).doubleValue() );
		Double totalReceiptedAmount = ((BigDecimal) obj[4]).doubleValue();
		Double totalOutstandAmount = ((BigDecimal) obj[5]).doubleValue();
		Double totalReturnedAmount = ((BigDecimal) obj[6]).doubleValue();
		log.info("show~~~totalReturnedAmount"+totalReturnedAmount);
		
		if(taxRate!=0){
			poHead.setTaxAmount( poHead.getTotalLocalPurchaseAmount()*taxRate/100 );
		}else{
			poHead.setTaxAmount( 0D );
		}
		log.info("itemBrandCode::"+itemBrandCode);
		log.info("category01::"+category01);
		poHead.setCategoryType(categoryType);
		poHead.setBudgetYear(  budgetYear);
		poHead.setBudgetMonth( budgetMonth);
		if(0 == itemBrandCode){
			poHead.setBudgetLineId(null);
			poHead.setCategory01(null);
		//}else if(itemBrandCode != 0 && category01 != null){
		//	poHead.setBudgetLineId(itemBrandCode);
		//	poHead.setCategory01(category01);
				}else{
					poHead.setBudgetLineId(itemBrandCode);
					poHead.setCategory01(category01);
				}
		FiBudgetHead fiBudgetHead = findBudgetHead( poHead );
		if (poHead.getBrandCode().equals("T2") && null!=fiBudgetHead  ){
			log.info("loginBudgetT2有大類:"+category01+"商品品牌:"+itemBrandCode);
			if(StringUtils.hasText(category01)){
				log.info("loginBudgetT2有大類");
				Map budgetSummary = fiBudgetLineDAO.getSummaryTotalT2addCategory(fiBudgetHead.getHeadId(),itemBrandCode,category01);
				poHead.setTotalBudget(        (Double)budgetSummary.get("budgetAmount"));
				poHead.setTotalAppliedBudget( (Double)budgetSummary.get("poActualAmount")); 
				poHead.setTotalSigningBudget( (Double)budgetSummary.get("signingAmount"));
				poHead.setTotalReturnedBudget((Double)budgetSummary.get("poReturnAmount"));		
					log.info("get REMAINDER"+poHead.getTotalRemainderBudget());
				poHead.setTotalRemainderBudget( poHead.getTotalBudget() - poHead.getTotalAppliedBudget() 
							- poHead.getTotalSigningBudget() + poHead.getTotalReturnedBudget());
				
				
			}else if(null != itemBrandCode && itemBrandCode != 0){
				log.info("loginBudgetT2無大類");
				Map budgetSummary = fiBudgetLineDAO.getSummaryTotalT2(fiBudgetHead.getHeadId(),itemBrandCode);
				poHead.setTotalBudget(        (Double)budgetSummary.get("budgetAmount"));
				poHead.setTotalAppliedBudget( (Double)budgetSummary.get("poActualAmount")); 
				poHead.setTotalSigningBudget( (Double)budgetSummary.get("signingAmount"));
				poHead.setTotalReturnedBudget((Double)budgetSummary.get("poReturnAmount"));
				poHead.setTotalRemainderBudget( poHead.getTotalBudget() - poHead.getTotalAppliedBudget() 
						- poHead.getTotalSigningBudget() + poHead.getTotalReturnedBudget() );
			}else{
				log.info("loginBudgetT2無大類無商品品牌");
				Map budgetSummary = fiBudgetLineDAO.getSummaryTotal(fiBudgetHead.getHeadId());
				poHead.setTotalBudget(        (Double)budgetSummary.get("budgetAmount"));
				poHead.setTotalAppliedBudget( (Double)budgetSummary.get("poActualAmount")); 
				poHead.setTotalSigningBudget( (Double)budgetSummary.get("signingAmount"));
				poHead.setTotalReturnedBudget((Double)budgetSummary.get("poReturnAmount"));
				poHead.setTotalRemainderBudget( poHead.getTotalBudget() - poHead.getTotalAppliedBudget() 
						- poHead.getTotalSigningBudget() + poHead.getTotalReturnedBudget() );
			}
		}else if(!poHead.getBrandCode().equals("T2") && null!=fiBudgetHead){
			log.info("loginBudgetT1");
			Map budgetSummary = fiBudgetLineDAO.getSummaryTotal(fiBudgetHead.getHeadId());
			poHead.setTotalBudget(        (Double)budgetSummary.get("budgetAmount"));
			poHead.setTotalAppliedBudget( (Double)budgetSummary.get("poActualAmount")); 
			poHead.setTotalSigningBudget( (Double)budgetSummary.get("signingAmount"));
			poHead.setTotalReturnedBudget((Double)budgetSummary.get("poReturnAmount"));
			poHead.setTotalRemainderBudget( poHead.getTotalBudget() - poHead.getTotalAppliedBudget() 
					- poHead.getTotalSigningBudget() + poHead.getTotalReturnedBudget() );
		}else{
			log.info("failfind...");
		    poHead.setErrorMessage( poHead.getBudgetYear() +"無此預算，請檢查!");
		}

		Properties pro = new Properties();
		pro.setProperty("TotalSigningBudget",         OperationUtils.roundToStr(poHead.getTotalSigningBudget(), 2));
		pro.setProperty("TotalBudget",                OperationUtils.roundToStr(poHead.getTotalBudget(), 2));
		pro.setProperty("TotalAppliedBudget",         OperationUtils.roundToStr(poHead.getTotalAppliedBudget(), 2));
		pro.setProperty("TotalReturnedBudget",        OperationUtils.roundToStr(poHead.getTotalReturnedBudget(), 2));
		pro.setProperty("TotalRemainderBudget",       OperationUtils.roundToStr(poHead.getTotalRemainderBudget(), 2));

		pro.setProperty("TotalForeignPurchaseAmount", OperationUtils.roundToStr(poHead.getTotalForeignPurchaseAmount(), 4));
		pro.setProperty("TotalLocalPurchaseAmount",   OperationUtils.roundToStr(poHead.getTotalLocalPurchaseAmount(), 2));
		pro.setProperty("TotalUnitPriceAmount",       OperationUtils.roundToStr(poHead.getTotalUnitPriceAmount(), 2));
		pro.setProperty("TotalProductCounts",         OperationUtils.roundToStr(poHead.getTotalProductCounts(), 0));
		pro.setProperty("TaxAmount",                  OperationUtils.roundToStr(poHead.getTaxAmount(), 2));
		pro.setProperty("TotalReceiptedAmount",       OperationUtils.roundToStr(totalReceiptedAmount, 2));
		pro.setProperty("TotalOutstandAmount",        OperationUtils.roundToStr(totalOutstandAmount, 2));
		pro.setProperty("TotalReturnedAmount",        OperationUtils.roundToStr(totalReturnedAmount, 2));

		if("buy".equals(purchaseType)){
			if("C".equals(buBrand.getBudgetType())){
				pro.setProperty("TotalUsedPriceAmount",   OperationUtils.roundToStr(poHead.getTotalLocalPurchaseAmount(), 2));
			}else{
				pro.setProperty("TotalUsedPriceAmount",   OperationUtils.roundToStr(poHead.getTotalUnitPriceAmount(), 2));
			}
		}else{
			pro.setProperty("TotalUsedPriceAmount",   OperationUtils.roundToStr(0, 2));
		}
		if( StringUtils.hasText(poHead.getErrorMessage()) ) {
			pro.setProperty("ErrorMessage", poHead.getErrorMessage() );
		}else{
			pro.setProperty("ErrorMessage", "NONE" );
		}
		re.add(pro);
		poPurchaseOrderHeadDAO.update(poHead);
		return re;
	}
	

	/**AJAX Load Page Data
	 * @param headObj
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
    public List<Properties> getAJAXPageData(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
    	NoSuchMethodException,Exception {
    	//log.info("PoPurchaseOrderHeadMainService.getAJAXPageData");

    	// 要顯示的HEAD_ID
    	Long headId   = NumberUtils.getLong(httpRequest.getProperty("headId"));
    	String status = httpRequest.getProperty("status");
    	Double exchangeRate    = NumberUtils.getDouble(httpRequest.getProperty("exchangeRate"));
    	if(exchangeRate == 0D)
    		exchangeRate = 1D;
    	List<Properties> re        = new ArrayList();
    	List<Properties> gridDatas = new ArrayList();
    	int iSPage = AjaxUtils.getStartPage(httpRequest);
    	int iPSize = AjaxUtils.getPageSize(httpRequest);

    	PoPurchaseOrderHead head =  findById(headId);
    	BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(head.getBrandCode(), head.getOrderTypeCode()));
    	head.setExchangeRate(exchangeRate);
    	List<PoPurchaseOrderLine> poPurchaseOrderLines = poPurchaseOrderLineMainService.findPageLine(headId, iSPage, iPSize);
    	String brandCode = null;
    	
    	if (null != poPurchaseOrderLines && poPurchaseOrderLines.size() > 0) {
    		brandCode = ((PoPurchaseOrderLine)poPurchaseOrderLines.get(0)).getPoPurchaseOrderHead().getBrandCode();
    		// 依商品編號撈取目前庫存數量
    		if( OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status) || !"T2".equals(brandCode)){
				for(PoPurchaseOrderLine poLine : poPurchaseOrderLines){
					setPoLineInfo(buOrderType, head, poLine);
					countItemTotalAmount(head, poLine);
					refreshPoLineMessage(poLine);
					ImItemCategory itemCategoryPO = imItemCategoryService.findById(brandCode, ImItemCategoryDAO.CATEGORY02, poLine.getCategory02());
					if(null != itemCategoryPO)
						poLine.setCategory02Name(itemCategoryPO.getCategoryName()); // 中類名稱
				}
    		}else{
    			for(PoPurchaseOrderLine poLine : poPurchaseOrderLines){
    			    //未交量金額
    				poLine.setOutstandQuantity(poLine.getActualPurchaseQuantity()-poLine.getReceiptedQuantity());
    				poLine.setOutstandAmount(poLine.getOutstandQuantity() * poLine.getLocalUnitCost());
    				setPoLineInfo(buOrderType, head, poLine);
    				ImItemCategory itemCategoryPO = imItemCategoryService.findById(brandCode, ImItemCategoryDAO.CATEGORY02, poLine.getCategory02());
    				if(null != itemCategoryPO)
    					poLine.setCategory02Name(itemCategoryPO.getCategoryName()); // 中類名稱
    			}
    		}
    		// 取得第一筆的INDEX
    		Long firstIndex = poPurchaseOrderLines.get(0).getIndexNo();
    		// 取得最後一筆 INDEX
    		Long maxIndex = poPurchaseOrderLineMainService.findPageLineMaxIndex(Long.valueOf(headId));
    		re.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, poPurchaseOrderLines, gridDatas, firstIndex, maxIndex));
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
	    log.info("PoPurchaseOrderHeadMainService.updateAJAXPageLinesData");
	    String gridData        = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount       = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
	    Long   headId          = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String status          = httpRequest.getProperty("status");
	    String brandCode       = httpRequest.getProperty("brandCode");
	    Double exchangeRate    = NumberUtils.getDouble(httpRequest.getProperty("exchangeRate"));
	    if(exchangeRate == 0D)
	    	exchangeRate = 1D;
	    String orderTypeCode   = httpRequest.getProperty("orderTypeCode");
	    String errorMsg = null;
	    log.info("PoPurchaseOrderHeadMainService.updateAJAXPageLinesData gridLineFirstIndex=" + gridLineFirstIndex + ",gridRowCount="
	    			+ gridRowCount + ",headId=" + headId + ",status=" + status + ",gridData=" + gridData);

	    //if (OrderStatus.SAVE.equalsIgnoreCase(status) || OrderStatus.REJECT.equalsIgnoreCase(status)) {
			// CREATE TMP HEAD
			PoPurchaseOrderHead head = new PoPurchaseOrderHead();
			head.setHeadId(headId);
			head.setExchangeRate(exchangeRate);
			head.setBrandCode(brandCode);
			head.setOrderTypeCode(orderTypeCode);
			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
										 PoPurchaseOrderHeadMainService.GRID_FIELD_NAMES);
			// Get INDEX NO
			int indexNo = poPurchaseOrderLineMainService.findPageLineMaxIndex(headId).intValue();	// 取得LINE MAX INDEX
			if (null != upRecords && null != head) {
			    for (Properties upRecord : upRecords) {
			    	// 先載入HEAD_ID OR LINE DATA
					Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
					//log.info("head Id = " + head.getHeadId() + " ,line Id = " + lineId);
					String itemCode = upRecord.getProperty("itemCode");
						
					// 如果沒有ITEM CODE 就不會新增或修改
					if (StringUtils.hasText(itemCode)) {
						PoPurchaseOrderLine poPurchaseOrderLine = poPurchaseOrderLineMainService.findLine(head.getHeadId(), lineId);
						if (null != poPurchaseOrderLine) {
							// UPDATE Properties
							log.info("Line update Properties");
							AjaxUtils.setPojoProperties(poPurchaseOrderLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
							countItemTotalAmount(head, poPurchaseOrderLine);
							poPurchaseOrderLineDAO.update(poPurchaseOrderLine);
						} else {
							// 重複輸入的品號合併其訂購數量
							//List<PoPurchaseOrderLine> poLines = poPurchaseOrderLineDAO.findByItemCode( head.getHeadId(), itemCode );
							List<PoPurchaseOrderLine> poLines = poPurchaseOrderLineDAO.findPoPurchaseOrderLines( head.getHeadId(), itemCode, "0");
							if ( null != poLines && poLines.size() > 0 ) {				// Line 已存在相同品號
								log.info("重複合併Line update Properties");
								PoPurchaseOrderLine oldLine = new PoPurchaseOrderLine();
								oldLine = poLines.get(0);						// 取已存在得PO LINE
								PoPurchaseOrderLine newLine = new PoPurchaseOrderLine();		// 將資料存在新的 BEAN
								AjaxUtils.setPojoProperties(newLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								//原幣單價相同才合併(wade)
								if(oldLine.getForeignUnitCost() != null && newLine.getForeignUnitCost() != null && oldLine.getForeignUnitCost().doubleValue() == newLine.getForeignUnitCost().doubleValue()){
									oldLine.setQuantity( oldLine.getQuantity()+ newLine.getQuantity() );//合併其訂購數量
									oldLine.setActualPurchaseQuantity( oldLine.getActualPurchaseQuantity()+ newLine.getActualPurchaseQuantity() );//合併其訂購數量
									newLine.setItemCode("");						// 更新品號為 ""
									poPurchaseOrderLineDAO.update(oldLine);
								}
								else{
								    indexNo++;
								    newLine.setIndexNo(Long.valueOf(indexNo));
								    newLine.setPoPurchaseOrderHead(head);
									countItemTotalAmount(head, newLine);
									poPurchaseOrderLineDAO.save(newLine);
								}
							} else {
								// INSERT Properties
								indexNo++;
								poPurchaseOrderLine = new PoPurchaseOrderLine();
								AjaxUtils.setPojoProperties(poPurchaseOrderLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								poPurchaseOrderLine.setIndexNo(Long.valueOf(indexNo));
								poPurchaseOrderLine.setPoPurchaseOrderHead(head);
								countItemTotalAmount(head, poPurchaseOrderLine);
								poPurchaseOrderLineDAO.save(poPurchaseOrderLine);
							}
						}
					}
			    }	// end of for
			} else {
			    errorMsg = "沒有採購單單頭資料";
			}
	    //} else {
		// 如果狀態不是暫存 是否要提示訊息
	    //}
	    return AjaxUtils.getResponseMsg(errorMsg);
	}

	/**
	 * delete all line
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 * @throws FormException
	 */
	public List<Properties> deleteAJAXAllLinesData(Properties httpRequest) throws FormException, Exception {
		log.info("PoPurchaseOrderHeadMainService.deleteAJAXAllLinesData");
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		String errorMsg = null;
		if (null != headId) {
			PoPurchaseOrderHead head = findById(headId);
			List<PoPurchaseOrderLine> poPurchaseOrderLines = new ArrayList();
			head.setPoPurchaseOrderLines(poPurchaseOrderLines);
			update(head);
		} else {
			errorMsg = "請輸入單頭";
		}
		return AjaxUtils.getResponseMsg(errorMsg);
	}

	/**
	 * 取得稅率
	 * 
	 * @param headObj
	 */
	private String getTaxRate(String taxType) {
		log.info("PoPurchaseOrderHeadMainService.getTaxRate taxType=" + taxType);
		Double tmp = new Double(0);
		if (StringUtils.hasText(taxType)) {
			if (PoPurchaseOrderHead.PURCHASE_ORDER_TAX.equalsIgnoreCase(taxType)) {
				tmp = new Double(5);
			}
		}
		return tmp.toString();
	}

	/**
	 * AJAX利用幣別指定匯率
	 * 
	 * @param headObj
	 */
	public List<Properties> getAJAXExchangeRateByCurrencyCode(Properties httpRequest) {
		log.info("PoPurchaseOrderHeadMainService.getAJAXExchangeRateByCurrencyCode");
		List re = new ArrayList();
		String currencyCode = httpRequest.getProperty("currencyCode");
		String organizationCode = httpRequest.getProperty("organizationCode");
		Properties pro = new Properties();
		if (StringUtils.hasText(currencyCode) && StringUtils.hasText(organizationCode)) {
			String exchangeRate = getExchangeRateByCurrencyCode(organizationCode, currencyCode);
			pro.setProperty("ExchangeRate", exchangeRate);
			re.add(pro);
		} else {
			pro.setProperty("ExchangeRate", "1");
			re.add(pro);
		}
		return re;

	}

    /**利用幣別指定匯率
     * @param headObj
     */
    private String getExchangeRateByCurrencyCode(String organizationCode, String currencyCode) {
	log.info("getExchangeRateByCurrencyCode organizationCode=" + organizationCode + ",currencyCode=" + currencyCode);
	Double tmp = new Double(1);
	if (StringUtils.hasText(organizationCode) && (StringUtils.hasText(currencyCode))) {
	    BuExchangeRate bxr = buBasicDataService.getLastExchangeRate(organizationCode, currencyCode, 
		    buCommonPhraseService.getBuCommonPhraseLineName("SystemConfig", "LocalCurrency"));
	    if (null != bxr) {
		tmp = bxr.getExchangeRate();
	    }
	}
	return tmp.toString();
    }

    
    /** 利用幣別指定匯率
     * call by getAJAXExchangeRateByCurrencyCode, getAJAXFormDataBySupplier
     * @param headObj
     */
     private String getExchangeRateByCurrencyCode(String organizationCode, String currencyCode, Date currenceDate) {
	log.info("getExchangeRateByCurrencyCode");
	Double tmp = new Double(1);
	if (StringUtils.hasText(organizationCode) && (StringUtils.hasText(currencyCode))) {
	    BuExchangeRate bxr = buBasicDataService.getLastExchangeRate(organizationCode, currencyCode, 
		    buCommonPhraseService.getBuCommonPhraseLineName("SystemConfig", "LocalCurrency"), currenceDate);
	    if (null != bxr) {
		tmp = bxr.getExchangeRate();
	    }
	}
	return tmp.toString();
     }

	
	/**
	 * 移除所有 TMP 單據
	 */
	public void deleteTmpOrder() {
		log.info("PoPurchaseOrderHeadMainService.deleteTmpOrder");
		poPurchaseOrderHeadDAO.removeTmpOrder();
	}


    /** 移除空白 or delete 的Detail 
     * @param headObj
     * @return boolean 是否還有 Detail
     */
    private boolean removeDetailItemCode(PoPurchaseOrderHead headObj) {
	log.info("PoPurchaseOrderHeadMainService.checkDetailItemCode");
	List<PoPurchaseOrderLine> items = headObj.getPoPurchaseOrderLines();
	Iterator<PoPurchaseOrderLine> it = items.iterator();
	while (it.hasNext()) {
	    PoPurchaseOrderLine line = it.next();
	    if (!StringUtils.hasText(line.getItemCode()) || "1".equals(line.getIsDeleteRecord()) ) {
	    	it.remove();
	    	items.remove(line);
	    }
	}
	return items.isEmpty();
    }

	/**
	 * set default actual purchase quantity
	 * 
	 * @param headObj
	 */
	private void setDefActualPurchaseQuantity(PoPurchaseOrderHead headObj) {
		log.info("PoPurchaseOrderHeadMainService.setDefActualPurchaseQuantity");
		List<PoPurchaseOrderLine> lines = headObj.getPoPurchaseOrderLines();
		if (null != lines) {
			for (PoPurchaseOrderLine line : lines) {
				if (line.getActualPurchaseQuantity() == 0) {
					line.setActualPurchaseQuantity(line.getQuantity());
				}
			}
		}
	}

	/**
	 * 修改所有的採購單 預算資料
	 * 
	 * @throws FormException
	 */
	public List<PoPurchaseOrderHead> updateAllPOBudget() throws FormException, Exception {
		// 取得所有的採購單
		List<PoPurchaseOrderHead> poPurchaseOrderHeads = poPurchaseOrderHeadDAO.findAllHead();
		// 修改預算
		for (PoPurchaseOrderHead poPurchaseOrderHead : poPurchaseOrderHeads) {
			countHeadTotalAmount(poPurchaseOrderHead);
		}
		return poPurchaseOrderHeads;
	}

	/**
	 * 重整BUDGET 使用掉預算,簽核中預算,作廢預算
	 * 
	 * @param poPurchaseOrderHead
	 * @param itemCode
	 * @param qty
	 * @param price
	 * @throws FormException
	 */
	public void updateBudget(PoPurchaseOrderHead poPurchaseOrderHead) throws FormException, Exception {
	    // 進貨 totalUnitPriceAmount = + , 退貨單 totalUnitPriceAmount = -
	    log.info("PoPurchaseOrderHeadMainService.minusBudget poPurchaseOrderHead.getHeadId()=" + poPurchaseOrderHead.getHeadId()
		    	+ ",poPurchaseOrderHead.orderNo=" + poPurchaseOrderHead.getOrderNo() + ",year=" + poPurchaseOrderHead.getBudgetYear());

	    Double totalAppliedBudget = 0D;
	    Double totalSigningBudget = 0D;
	    Double totalUnitPriceAmount = 0D;
	    Double totalRemainderBudget = 0D;
	    Double totalBudget = 0D;
	    if (null != poPurchaseOrderHead) {
	    	String status    = poPurchaseOrderHead.getStatus();

	    	FiBudgetHead fiBudgetHead = findBudgetHead( poPurchaseOrderHead );
	    	if ( null==fiBudgetHead ){
	    		throw new ValidationErrorException( poPurchaseOrderHead.getErrorMessage() );
	    	}else{									//暫時註解
	    		Map budgetSummary  = fiBudgetLineDAO.getSummaryTotal(fiBudgetHead.getHeadId());
	    		totalBudget        = (Double)budgetSummary.get("budgetAmount");
	    		totalAppliedBudget = (Double)budgetSummary.get("poActualAmount");
	    		totalSigningBudget = (Double)budgetSummary.get("signingAmount");
	    		//totalBudget = fiBudgetHead.getTotalBudget();
	    	}

	    	if (null != poPurchaseOrderHead.getTotalUnitPriceAmount()) {
	    		totalUnitPriceAmount = poPurchaseOrderHead.getTotalUnitPriceAmount();
	    		if ((!OrderStatus.SIGNING.equals(status)) && (!OrderStatus.FINISH.equals(status)) && (!OrderStatus.VOID.equals(status))) {
	    			totalRemainderBudget = totalBudget - totalAppliedBudget - totalSigningBudget - totalUnitPriceAmount;
	    		} else {
	    			totalRemainderBudget = totalBudget - totalAppliedBudget - totalSigningBudget;
	    		}
	    	}

	    	poPurchaseOrderHead.setTotalAppliedBudget(  totalAppliedBudget);
	    	poPurchaseOrderHead.setTotalBudget(         totalBudget);
	    	poPurchaseOrderHead.setTotalRemainderBudget(totalRemainderBudget);
	    	poPurchaseOrderHead.setTotalSigningBudget(  totalSigningBudget);

	    	modifyBudget( poPurchaseOrderHead );
	    }
	}

	
	private Double getTotalAppliedBudget(String year, String brandCode) {
		Double totalAppliedBudget = 0D;
		String sqlTotalAppliedBudget = "select sum(TOTAL_UNIT_PRICE_AMOUNT) as totalAppliedBudget from po_purchase_order_head where (status = 'FINISH' or status = 'CLOSE') and PURCHASE_TYPE = 'buy' and BUDGET_YEAR ='"
				+ year + "' and brand_code ='" + brandCode + "'";
		List list = nativeQueryDAO.executeNativeSql(sqlTotalAppliedBudget);
		if (null != list && list.size() > 0) {
			Object obj = (Object) list.get(0);
			if (null != obj) {
				totalAppliedBudget = ((BigDecimal) obj).doubleValue();
			}
		}
		return totalAppliedBudget;
	}

	
	private Double getTotalSigningBudget(String year, String brandCode) {
		Double totalSigningBudget = 0D;
		String sqlTotalSigningBudget = "select sum(TOTAL_UNIT_PRICE_AMOUNT)  as totalSigningBudget from po_purchase_order_head where status = 'SIGNING' and PURCHASE_TYPE = 'buy' and BUDGET_YEAR ='"
				+ year + "' and brand_code ='" + brandCode + "'";
		List list = nativeQueryDAO.executeNativeSql(sqlTotalSigningBudget);
		if (null != list && list.size() > 0) {
			Object obj = (Object) list.get(0);
			if (null != obj) {
				totalSigningBudget = ((BigDecimal) obj).doubleValue();
			}
		}
		return totalSigningBudget;
	}

	
	/**
	 * 修改預算資料
	 * 
	 * @param totalAppliedBudget
	 * @param totalSigningBudget
	 */
	private FiBudgetHead modifyBudget( PoPurchaseOrderHead poPurchase ) throws Exception {
	    log.info("modifyBudget");
	    FiBudgetHead fiBudgetHead = findBudgetHead( poPurchase );
	    if ( null!=fiBudgetHead ){
	    	fiBudgetHead.setTotalAppliedBudget( poPurchase.getTotalAppliedBudget());	//totalAppliedBudget
	    	fiBudgetHead.setTotalSigningBudget( poPurchase.getTotalSigningBudget());	//totalSigningBudget);
	    	fiBudgetHeadDAO.update(fiBudgetHead);
	    	return fiBudgetHead;
	    }else{
		return null;
	    }
	}

	
	// 退貨單的轉換
	private void changeReturnQuantity(PoPurchaseOrderHead poPurchaseOrderHead, PoPurchaseOrderLine poPurchaseOrderLine) {
		log.info("PoPurchaseOrderHeadMainService.changeReturnQuantity");
		if (PoPurchaseOrderHead.PURCHASE_RETURN_ORDER_FOREIGN.equals(poPurchaseOrderHead.getOrderTypeCode())
				|| PoPurchaseOrderHead.PURCHASE_RETURN_ORDER_LOCAL.equals(poPurchaseOrderHead.getOrderTypeCode())) {
			if (poPurchaseOrderLine.getQuantity() > 0)
				poPurchaseOrderLine.setQuantity(poPurchaseOrderLine.getQuantity() * -1);
		}
	}

	// 寫入ON_HAND,寫入預算 , UNCONFIRMED 才可以寫 , FINISH 完成
	private void modifyOnHandReturnReceiveOrder(PoPurchaseOrderHead poPurchaseOrderHead) throws Exception {
		log.info("PoPurchaseOrderHeadMainService.modifyOnHandReturnReceiveOrder");
		// 如果已經扣掉過了 就無法在執行
		if (OrderStatus.FINISH.equalsIgnoreCase(poPurchaseOrderHead.getStatus())) {
			throw new FormException(poPurchaseOrderHead.getOrderNo() + "已經做過ON_HAND,請再確認資料後重新執行退貨動作");
		} else if (OrderStatus.UNCONFIRMED.equalsIgnoreCase(poPurchaseOrderHead.getStatus())) {
			movePOToDeliveryAndModifyOnHand(poPurchaseOrderHead);
			// 加上 預算
			updateBudget(poPurchaseOrderHead);
			poPurchaseOrderHead.setStatus(OrderStatus.FINISH);
			

		}
	}

	/**
	 * 建立 DELIVERY 跟 呼叫 DELIVERY 修改 ON_HAND
	 * 
	 * @param poPurchaseOrderHead
	 * @throws Exception
	 */
	private void movePOToDeliveryAndModifyOnHand(PoPurchaseOrderHead poPurchaseOrderHead) throws Exception {
		log.info("PoPurchaseOrderHeadMainService.movePOToDeliveryAndModifyOnHand");

		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(poPurchaseOrderHead.getBrandCode());
		if (organizationCode == null) {
			throw new FormException("依據品牌代碼：" + poPurchaseOrderHead.getBrandCode() + "查無其組織代號！");
		}

		// 建立退貨單
		// 建立單頭
		ImReceiveHead imReceiveHead = new ImReceiveHead();
		imReceiveHead.setExchangeRate(1D);
		imReceiveHead.setBrandCode(poPurchaseOrderHead.getBrandCode());
		imReceiveHead.setBudgetYear(poPurchaseOrderHead.getBudgetYear());
		imReceiveHead.setSupplierCode(poPurchaseOrderHead.getSupplierCode());
		imReceiveHead.setSupplierName(poPurchaseOrderHead.getSupplierName());
		imReceiveHead.setCurrencyCode(poPurchaseOrderHead.getCurrencyCode());
		imReceiveHead.setOrderDate(poPurchaseOrderHead.getPurchaseOrderDate());
		imReceiveHead.setLastUpdatedBy(poPurchaseOrderHead.getLastUpdatedBy());
		imReceiveHead.setCreatedBy(poPurchaseOrderHead.getCreatedBy());

		String orderTypeCode = ImReceiveHead.RECEIVE_RETURN_ORDER_LOCAL;
		String defPoOrderType = PoPurchaseOrderHead.PURCHASE_RETURN_ORDER_LOCAL;
		if (PoPurchaseOrderHead.PURCHASE_RETURN_ORDER_FOREIGN.equalsIgnoreCase(poPurchaseOrderHead.getOrderTypeCode())) {
			orderTypeCode = ImReceiveHead.RECEIVE_RETURN_ORDER_FOREIGN;
			defPoOrderType = PoPurchaseOrderHead.PURCHASE_RETURN_ORDER_FOREIGN;
		}

		imReceiveHead.setOrderNo(poPurchaseOrderHead.getOrderNo());
		imReceiveHead.setDefPoOrderType(defPoOrderType);
		imReceiveHead.setOrderTypeCode(orderTypeCode);
		imReceiveHead.setDefaultWarehouseCode(poPurchaseOrderHead.getDefaultWarehouseCode());
		imReceiveHead.setStatus("FINISH");
		imReceiveHead.setWarehouseInDate(new Date());
		// 判斷是否有辦法建ON_HAND
		imReceiveHead.setWarehouseStatus("FINISH");
		imReceiveHead.setOnHandStatus("N");

		// 建立單身
		List<PoPurchaseOrderLine> poPurchaseOrderLines = poPurchaseOrderHead.getPoPurchaseOrderLines();
		// log.info("PoPurchaseOrderHeadMainService.movePOToDeliveryAndModifyOnHand
		// poPurchaseOrderLines=" + poPurchaseOrderLines.size());
		List<ImReceiveItem> imReceiveItems = new ArrayList();
		for (PoPurchaseOrderLine poPurchaseOrderLine : poPurchaseOrderLines) {
			ImReceiveItem imReceiveItem = new ImReceiveItem();
			imReceiveItem.setImReceiveHead(imReceiveHead);
			imReceiveItem.setLocalUnitPrice(poPurchaseOrderLine.getLastLocalUnitCost());
			imReceiveItem.setLocalAmount(poPurchaseOrderLine.getLastLocalUnitCost()*poPurchaseOrderLine.getQuantity());
			imReceiveItem.setForeignUnitPrice(poPurchaseOrderLine.getForeignUnitCost());
			imReceiveItem.setForeignAmount(poPurchaseOrderLine.getForeignUnitCost()*poPurchaseOrderLine.getQuantity());
			imReceiveItem.setQuantity(poPurchaseOrderLine.getQuantity());
			imReceiveItem.setItemCode(poPurchaseOrderLine.getItemCode());
			imReceiveItem.setItemCName(poPurchaseOrderLine.getItemCName());
			imReceiveItems.add(imReceiveItem);
		}
		imReceiveHead.setImReceiveItems(imReceiveItems);

		// 作ON_HAND的條件
		// OrderStatus.FINISH.equalsIgnoreCase(modifyObj.getWarehouseStatus())
		// && "N".equalsIgnoreCase(modifyObj.getOnHandStatus())
		imReceiveHeadService.save(imReceiveHead);
	}

	public String updateCloseOrder(PoPurchaseOrderHead modifyObj) throws FormException, Exception {
		log.info("PoPurchaseOrderHeadMainService.close");
		if (null != modifyObj) {
			try {
				boolean allFinish = true;
				// PoPurchaseOrderHead poPurchaseOrderHead =
				// (PoPurchaseOrderHead)poPurchaseOrderHeadDAO.findByPrimaryKey(PoPurchaseOrderHead.class,
				// modifyObj.getHeadId());
				List<PoPurchaseOrderLine> poPurchaseOrderLines = modifyObj.getPoPurchaseOrderLines();
				for (PoPurchaseOrderLine poPurchaseOrderLine : poPurchaseOrderLines) {
					if ((poPurchaseOrderLine.getActualPurchaseQuantity() - poPurchaseOrderLine.getReceiptedQuantity()) > 0) {
						allFinish = false;
						break;
					}
				}
				// System.out.println("poPurchaseOrderHead.getOrderNo():" +
				// modifyObj.getOrderNo());
				if (allFinish) {
					modifyObj.setStatus(OrderStatus.CLOSE);
					modifyObj.setCloseOrder(PoPurchaseOrderHead.CLOSE_ORDER_Y);
					poPurchaseOrderHeadDAO.update(modifyObj);
					return MessageStatus.SUCCESS;
				} else {
					throw new FormException("尚有到貨數量未核銷 ");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new FormException("表單有誤，請與資訊人員連繫");
			}
		} else {
			throw new FormException("查無表單主檔資料");
		}
	}

	
	public String updateUncomfirmOrder(PoPurchaseOrderHead modifyObj) throws FormException, Exception {
		log.info("PoPurchaseOrderHeadMainService.updateUncomfirmOrder");
		if (null != modifyObj) {
			try {
				boolean isUncomfirm = false;
				String msg = null;
				if("POF".equals(modifyObj.getOrderTypeCode())){
					isUncomfirm = !fiInvoiceLineDAO.isUsedByPo(modifyObj.getHeadId());
					msg = "採購單號已被發票所使用，不可反確認";
				}else{
					isUncomfirm = !imReceiveItemDAO.isUsedByPo(modifyObj.getOrderNo());
					msg = "採購單號已被進貨單所使用，不可反確認";
				}
				if(isUncomfirm){
					modifyObj.setStatus(OrderStatus.SAVE);
					modifyObj.setCloseOrder(PoPurchaseOrderHead.CLOSE_ORDER_N);
					poPurchaseOrderHeadDAO.update(modifyObj);
				}else{
					throw new FormException(msg);
				}
				return MessageStatus.SUCCESS;
			} catch (FormException e) {
				e.printStackTrace();
				throw new FormException(e.getMessage());
			}catch (Exception e) {
				e.printStackTrace();
				throw new FormException("表單有誤，請與資訊人員連繫");
			}
		} else {
			throw new FormException("查無表單主檔資料");
		}
	}
	
	
	/** 更新採購主檔及明細檔
	 * @param parameterMap
	 * @return Map
	 * @throws Exception
	 */
	public Map updateAJAXPoPurchase(Map parameterMap) throws FormException, Exception {
		HashMap resultMap = new HashMap();
		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean    = parameterMap.get("vatBeanOther");
			String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formStatus         = (String)PropertyUtils.getProperty(otherBean, "formStatus");
			String employeeCode       = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			//String itemBrand = (String)PropertyUtils.getProperty(formBindBean, "budgetLineId");
			//String itemBrand = (String) PropertyUtils.getProperty(formBindBean,"itemBrandCode");
			Long itemBrandCode =  NumberUtils.getLong("budgetLineId");
			log.info("itemBrandCode==="+itemBrandCode);
			Long headId = getPoPurchaseHeadId(formLinkBean);
			log.info("單據"+headId +"狀態為"+ formStatus);//觀察未回寫狀態
			
			//取得欲更新的bean
			PoPurchaseOrderHead poPurchase = getActualPoPurchase(headId);
			log.info("itemBrand**"+poPurchase.getBudgetLineId()+"*-*-*-"+poPurchase.getCategory01());			
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, poPurchase);
			//刪除mark掉的Line
			removeAJAXLine(poPurchase);

			if( OrderStatus.SIGNING.equals(formStatus) &&
					(OrderStatus.REJECT.equals(beforeChangeStatus) || OrderStatus.SAVE.equals(beforeChangeStatus)) ){
				countHeadTotalAmount(poPurchase);	// 每次送出時都 CALL 計算總金額, 寫入金額合計資料 2009.11.27
			}
			
			poPurchase.setStatus(formStatus);
			
			String purchaseMaster =(String)PropertyUtils.getProperty(formBindBean, "purchaseMaster");
			String managerCode = UserUtils.getLoginNameByEmployeeCode(employeeCode);
			BuEmployee bump = buEmployeeDAO.findById(employeeCode);
	
			//poPurchase.setBudgetLineId(itemBrandCode);
			//poPurchase.setBudgetLineId(itemBrand);
			// 修改預算檔 fiBudgetLine / status 由  ( !SIGNING -> SIGNING || FINISH -> CLOSE ) && purchaseType=="buy" 一般採購
			if( (OrderStatus.SAVE.equals(beforeChangeStatus) || OrderStatus.REJECT.equals(beforeChangeStatus)) && OrderStatus.SIGNING.equals(formStatus) 
					|| (OrderStatus.FINISH.equals(beforeChangeStatus) && OrderStatus.CLOSE.equals(formStatus)) 
					|| (OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.REJECT.equals(formStatus))
					/*|| (OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus))*/
					|| (OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.FINISH.equals(formStatus))
			){
				if("buy".equalsIgnoreCase(poPurchase.getPurchaseType())) {
					//這一段可以用NATIVESQL寫加總扣更快					
					updateActualBudget( poPurchase, beforeChangeStatus, formStatus, employeeCode );
				}
				
				// 如果是T4NG，採購單直接結案
				if("T4NG".equals(poPurchase.getBrandCode())){
					poPurchase.setStatus(OrderStatus.FINISH);
					updateActualBudget( poPurchase, OrderStatus.SIGNING, OrderStatus.FINISH, employeeCode );
				}
			}
			
			// 如果是免稅3C採購且採購助理送出，採購單結案
			if( "T2".equals(poPurchase.getBrandCode()) && "D".equals(poPurchase.getCategoryType()) && OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus) ){
				poPurchase.setStatus(OrderStatus.FINISH);
				log.info("單據"+headId +"狀態為"+ formStatus);//觀察未回寫狀態
				updateActualBudget( poPurchase, OrderStatus.SIGNING, OrderStatus.FINISH, employeeCode );
			}
			
			if ( managerCode.equals(purchaseMaster) || bump.getIsDepartmentManager() == "Y"){	// 主管 	//or Key 單是主管 || applicantCode.equals(purchaseMaster) 
				poPurchase.setStatus(OrderStatus.FINISH);
				updateActualBudget( poPurchase, OrderStatus.SIGNING, OrderStatus.FINISH, employeeCode );
			}
			
			//關帳的話把實際採購量弄好
			if(OrderStatus.CLOSE.equals(formStatus) && OrderStatus.FINISH.equals(beforeChangeStatus)){
				List<PoPurchaseOrderLine> lines = poPurchase.getPoPurchaseOrderLines();
				for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
					PoPurchaseOrderLine poPurchaseOrderLine = (PoPurchaseOrderLine) iterator.next();
					poPurchaseOrderLine.setReturnedQuantity(poPurchaseOrderLine.getActualPurchaseQuantity() - poPurchaseOrderLine.getReceiptedQuantity());
					poPurchaseOrderLine.setActualPurchaseQuantity(NumberUtils.getDouble(poPurchaseOrderLine.getReceiptedQuantity()));
				}
			}
			log.info("單據"+headId +"狀態為"+ formStatus);//觀察未回寫狀態
			String resultMsg = modifyAjaxPoPurchase(poPurchase, employeeCode);
			resultMap.put("entityBean", poPurchase);
			resultMap.put("resultMsg", resultMsg);

			return resultMap;
			//=============================================              
		} catch (FormException fe) {
			log.error("採購單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("採購單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("採購單存檔時發生錯誤，原因：" + ex.getMessage());
		}
	}
    
    private PoPurchaseOrderHead getActualPoPurchase(Long headId) throws FormException, Exception{
        log.info("getActualPoPurchase");
        PoPurchaseOrderHead poPurchase = null;
        poPurchase = findById(headId);
	if(poPurchase  == null){
	     throw new NoSuchObjectException("查無採購單主鍵：" + headId + "的資料！");
	}else{
	    
	}
        return poPurchase;
    }
    
    
    /**暫存單號取實際單號並更新至PoPurchase主檔
     * @param PoPurchaseOrderHead
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String modifyPoPurchase(PoPurchaseOrderHead poPurchase, String loginUser)
	    throws ObtainSerialNoFailedException, FormException, Exception {
    	log.info("modifyPoPurchase loginUser="+loginUser);
        poPurchase.setLastUpdatedBy(loginUser);
        poPurchase.setLastUpdateDate(new Date());
        poPurchaseOrderHeadDAO.update(poPurchase);
        return poPurchase.getOrderTypeCode() + "-" + poPurchase.getOrderNo() + "存檔成功！";
    }
    
    
    public static Object[] startProcess(PoPurchaseOrderHead form, String branchCode) throws ProcessFailedException{  
    //public Object[] startProcess(PoPurchaseOrderHead form) throws ProcessFailedException{  
        try{           
	       String packageId = "Po_Purchase";
	       String processId = "approval";
	       String sourceReferenceType = "PoPurchase (1)";
	       String version = "20090803";
	       if ( branchCode.equals("2") ) {	// T2 Run different flow
		   version = "20091015T2";
	       } else {
		   version = "20091015";
	       }
	       
	       HashMap context = new HashMap();
	       context.put("brandCode", form.getBrandCode());
	       context.put("formId",    form.getHeadId());
	       context.put("orderType", form.getOrderTypeCode());
	       context.put("orderNo",   form.getOrderNo());
	       return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
	   }catch (Exception ex){
	       log.error("請購單流程啟動失敗，原因：" + ex.toString());
	       throw new ProcessFailedException("請購單流程啟動失敗！");
	   }
    }
    
    public static Object[] startProcess(PoPurchaseOrderHead form) throws ProcessFailedException{  
    	try{           
    		String packageId = "Po_Purchase";
    		String processId = "approval";
    		String sourceReferenceType = "PoPurchase (1)";
    		String version = "20090803";
    		if ( "T2".equals(form.getBrandCode()) ) {	// T2 Run different flow
    			version = "20091015T2";
    		} else {
    			version = "20091015";
    		}

    		HashMap context = new HashMap();
    		context.put("brandCode", form.getBrandCode());
    		context.put("formId",    form.getHeadId());
    		context.put("orderType", form.getOrderTypeCode());
    		context.put("orderNo",   form.getOrderNo());
    		return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
    	}catch (Exception ex){
    		log.error("請購單流程啟動失敗，原因：" + ex.toString());
    		throw new ProcessFailedException("請購單流程啟動失敗！");
    	}
    }
    
    public static Object[] completeAssignment(long assignmentId, boolean approveResult,PoPurchaseOrderHead poPurchase) throws ProcessFailedException{
	try{           
	    HashMap context = new HashMap();
	    context.put("approveResult", approveResult);
	    context.put("form",poPurchase);
	    return ProcessHandling.completeAssignment(assignmentId, context);
	}catch (Exception ex){
	    log.error("完成採購工作任務失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("完成採購工作任務失敗！");
	}
    }
    
    
    /**
     * 匯入採購單明細
     * @param headId
     * @param lineLists
     * @throws Exception
     */
    public void executeImportPoLists(Long headId,Double exchangeRate, List lineLists) throws Exception{
    	try{
    		log.info("executeImportPoLists headId = " + headId);
    		poPurchaseOrderLineMainService.deleteLineLists(headId);
    		if(lineLists != null && lineLists.size() > 0){
    			PoPurchaseOrderHead poPurchaseOrderHeadTmp = findById(headId);
    			BuOrderType buOrderType = buOrderTypeService.
    				findById( new BuOrderTypeId(poPurchaseOrderHeadTmp.getBrandCode(), poPurchaseOrderHeadTmp.getOrderTypeCode()));
    			poPurchaseOrderHeadTmp.setExchangeRate(exchangeRate);
    			for(int i = 0; i < lineLists.size(); i++){
    				PoPurchaseOrderLine line = (PoPurchaseOrderLine)lineLists.get(i);
    				line.setPoPurchaseOrderHead(poPurchaseOrderHeadTmp);
    				line.setItemCode(line.getItemCode().toUpperCase());
    				line.setActualPurchaseQuantity(line.getQuantity());
    				line.setReceiptedQuantity(0D);
    				//進貨成本
    				Double lastLocalUnitCost = poPurchaseOrderLineMainService.
    					getAverageUnitCost(line.getItemCode(), poPurchaseOrderHeadTmp.getBrandCode());
    				line.setLastLocalUnitCost(lastLocalUnitCost);
    				setPoLineInfo(buOrderType, poPurchaseOrderHeadTmp, line);
    				countItemTotalAmount(poPurchaseOrderHeadTmp, line);
    				//庫存值 modify by iris 2012.06.20
    				Double stockOnHandQtytmp = new Double(0);
    			    stockOnHandQtytmp = imOnHandDAO.getCurrentStockOnHandQuantity("TM", poPurchaseOrderHeadTmp.getBrandCode(), line.getItemCode());
    			    line.setStockOnHandQty(stockOnHandQtytmp);
    				line.setIndexNo(i+1L);
    				poPurchaseOrderLineDAO.save(line);
    			}
    		}     	
    	}catch (Exception ex) {
    		log.error("採購單明細匯入時發生錯誤，原因：" + ex.toString());
    		throw new Exception("採購單明細匯入時發生錯誤，原因：" + ex.getMessage());
    	}        
    }
    
    
    public String getIdentification(Long headId) throws Exception{
	String id = null;
	try{
	    PoPurchaseOrderHead poPurchase = findById(headId);
	    if(poPurchase != null){
		id = MessageStatus.getIdentification(poPurchase.getBrandCode(), poPurchase.getOrderTypeCode(), poPurchase.getOrderNo());
	    }else{
		throw new NoSuchDataException("採購單主檔查無主鍵：" + headId + "的資料！");
	    }
	    return id;
	    
	}catch(Exception ex){
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
	}	   
    }
    
    
    public Long getPoPurchaseHeadId(Object bean) throws FormException, Exception{
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
    
    
    /** 取單號後更新採購單主檔
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updatePoPurchaseWithActualOrderNO(Map parameterMap, String OpType ) throws Exception {
	log.info("updatePoPurchaseWithActualOrderNO");
        HashMap resultMap = new HashMap();
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Object otherBean    = parameterMap.get("vatBeanOther");
            Long headId = getPoPurchaseHeadId(formLinkBean); 
            String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
            //取得欲更新的bean
            PoPurchaseOrderHead poPurchase = getActualPoPurchase(headId);
            String identification = 
        	MessageStatus.getIdentification(poPurchase.getBrandCode(), poPurchase.getOrderTypeCode(), poPurchase.getOrderNo());
            // clear 原有 ERROR RECORD
            siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
            
            AjaxUtils.copyJSONBeantoPojoBean(formBindBean, poPurchase);
            if (OpType.equals("FG")) {
            	modifyPoPurchase( poPurchase, employeeCode);
            }else{			// BG
	        	String resultMsg = modifyAjaxPoPurchase(poPurchase, employeeCode);
	        	resultMap.put("entityBean", poPurchase);
	        	resultMap.put("resultMsg", resultMsg);
            }
            return resultMap;
		} catch (Exception ex) {
		    log.error("採購單存檔時發生錯誤，原因：" + ex.toString());
		    throw new Exception("採購單存檔時發生錯誤，原因：" + ex.getMessage());
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
    private String modifyAjaxPoPurchase(PoPurchaseOrderHead poPurchase, String loginUser)
	    throws ObtainSerialNoFailedException, FormException, Exception {
	log.info("modifyAjaxPoPurchase loginUser="+loginUser);
	if (AjaxUtils.isTmpOrderNo(poPurchase.getOrderNo())) {
	    String serialNo = buOrderTypeService.getOrderSerialNo(poPurchase.getBrandCode(), poPurchase.getOrderTypeCode());
	    if (!serialNo.equals("unknow")) {
	    	poPurchase.setOrderNo(serialNo);
	    } else {
	    	throw new ObtainSerialNoFailedException("取得" + poPurchase.getOrderTypeCode() + "單號失敗！");
	    }
	}	
	modifyPoPurchase( poPurchase, loginUser);
	return poPurchase.getOrderTypeCode() + "-" + poPurchase.getOrderNo() + "存檔成功！";
    }

    
    /** 2009.09.22 arthur
     * 更新檢核後的 PO 單資料
     * @param purchaseHead
     * @param conditionMap
     * @return List
     * @throws ValidationErrorException
     */
    public List updateCheckedPurchaseOrderData(Map parameterMap) throws Exception {
	log.info("updateCheckedPurchaseOrderData");
	log.info("1111dddddpopo");
	List errorMsgs = new ArrayList(0);
	try{
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Long headId = getPoPurchaseHeadId(formLinkBean);
            PoPurchaseOrderHead poPurchasePO = getActualPoPurchase(headId);
            BuBrand buBrand       = buBrandService.findById( poPurchasePO.getBrandCode() );
            String branchCode     = buBrand.getBranchCode();
            String identification = 
        	MessageStatus.getIdentification(poPurchasePO.getBrandCode(), poPurchasePO.getOrderTypeCode(), poPurchasePO.getOrderNo());

            // clear 原有 ERROR RECORD
            siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
            // check PO head Data
            checkPoPurchaseData( poPurchasePO, buBrand, PROGRAM_ID, identification, errorMsgs);
            // check PO line data
            checkPoPurchaseLines(poPurchasePO, branchCode, PROGRAM_ID, identification, errorMsgs);
	    
	    return errorMsgs;
	}catch (Exception ex) {
	    log.error("採購單檢核後存檔失敗，原因：" + ex.toString());
	    throw new Exception("採購單檢核後存檔失敗，原因：" + ex.getMessage());
	}
    }

    /** 檢核採購單相關資料
     * @param poPurchaseOrderHead
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    public void checkPoPurchaseData(PoPurchaseOrderHead poPurchase, BuBrand buBrand, String programId, String identification, List errorMsgs) 
        throws ValidationErrorException {
	log.info("PoPurchaseOrderHeadMainService.checkPoPurchaseData : BranchCode=" + buBrand.getBranchCode());
	//boolean noLine = removeDetailItemCode( poPurchase );
	String tabName = "採購單主檔資料";
	Boolean errorStatus = false;
	
	try{
	    if (null == poPurchase.getSupplierCode()){
			errorStatus = true;
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+"請輸入廠商代號！", poPurchase.getLastUpdatedBy());
			//throw new ValidationErrorException(tabName + "請輸入廠商代號！");
	    }else{
	    	BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(poPurchase.getBrandCode(), poPurchase.getSupplierCode());
	    	if(null == buSWAV){
	    		errorStatus = true;
	    		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+"查無此廠商代號！", poPurchase.getLastUpdatedBy());
	    	}
	    }
	    
	    if (null == poPurchase.getPurchaseOrderDate()){
			errorStatus = true;
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+"請輸入採購日期！", poPurchase.getLastUpdatedBy());
			//throw new ValidationErrorException(tabName + "請輸入採購日期！");
	    }
	    if (null == poPurchase.getBudgetYear()){
			errorStatus = true;
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+"請輸入預算年度！", poPurchase.getLastUpdatedBy());
			//throw new ValidationErrorException(tabName + "請輸入預算年度！");
	    }
	    if (null == poPurchase.getBudgetMonth() && "M".equals(buBrand.getBudgetCheckType()) ){
			errorStatus = true;
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+"請輸入預算月份！", poPurchase.getLastUpdatedBy());
			//throw new ValidationErrorException(tabName + "請輸入預算月份！");
	    }
	    if (!StringUtils.hasText( poPurchase.getSuperintendentCode()) && "2".equals(buBrand.getBranchCode())  ){	// T2 需指定採購單負責人
			errorStatus = true;
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+"請輸入採購單負責人！", poPurchase.getLastUpdatedBy());
			//throw new ValidationErrorException(tabName + "請輸入採購單負責人！");
	    }
	    if(null == poPurchase.getExchangeRate() || poPurchase.getExchangeRate() <= 0){
			errorStatus = true;
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+"匯率不可為零或小於負數！", poPurchase.getLastUpdatedBy());
			//throw new ValidationErrorException(tabName + "匯率不可為零或小於負數！");
		}
	    if (null == poPurchase.getCategoryType()){
			errorStatus = true;
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+"請輸入預算業種！", poPurchase.getLastUpdatedBy());
			//throw new ValidationErrorException(tabName + "請輸入預算業種！");
	    }
	    //只有選擇buy才會檢查 2010.3.16
	    if (0 == NumberUtils.getDouble(poPurchase.getAsignedBudget()) && "T".equals(buBrand.getBudgetType()) && "buy".equalsIgnoreCase(poPurchase.getPurchaseType()) ) {
			//&& "T1CO".equals( poPurchase.getBrandCode()) ){		// 百貨需輸入指定預算 !"2".equals(branchCode
			errorStatus = true;
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+"請輸入指定預算！", poPurchase.getLastUpdatedBy());
			//throw new ValidationErrorException(tabName + "請輸入指定預算！");
	    }
	    if (!StringUtils.hasText(poPurchase.getBudgetYear())) {
			errorStatus = true;
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+"請選擇預算年度月份！", poPurchase.getLastUpdatedBy());
			//throw new ValidationErrorException(tabName + "請選擇預算年度月份！");
	    } else {
			FiBudgetHead fiBudgetHead = findBudgetHead( poPurchase );
			if ( null==fiBudgetHead ){
			    errorStatus = true;
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+poPurchase.getErrorMessage(), poPurchase.getLastUpdatedBy());
			}
	    }
	    if (!StringUtils.hasText( poPurchase.getPurchaseAssist() ) && "2".equals(buBrand.getBranchCode())  ){	// T2 需指定採購單負責人
			errorStatus = true;
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+"請輸入負責採購助理！", poPurchase.getLastUpdatedBy());
			//throw new ValidationErrorException(tabName + "請輸入負責採購助理！");
	    }
	    if( !StringUtils.hasText( poPurchase.getPurchaseMember() ) && "2".equals(buBrand.getBranchCode())  ){
			errorStatus = true;
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+"請輸入負責採購人員！", poPurchase.getLastUpdatedBy());
			//throw new ValidationErrorException(tabName + "請輸入負責採購人員！");
	    }
	    if( !StringUtils.hasText( poPurchase.getPurchaseMaster() ) && "2".equals(buBrand.getBranchCode())  ){
			errorStatus = true;
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, tabName+"請輸入負責採購主管！", poPurchase.getLastUpdatedBy());
			//throw new ValidationErrorException(tabName + "請輸入負責採購主管！");
	    }
	    if(errorStatus){
	    	throw new ValidationErrorException("採購單主檔檢核發生錯誤！");
	    }
	    	
	}catch(Exception ex){
	    log.info( "checkHead : " + ex.getMessage() );
	    errorMsgs.add("checkHead : " + ex.getMessage());
	}
    }
    
	/**2009.09.21
	* 檢核訂購明細資料(AJAX)
	* @param orderHead
	* @param programId
	* @param identification
	* @param errorMsgs
	*/
	public void checkPoPurchaseLines( PoPurchaseOrderHead orderHead, String branchCode, String programId, String identification, List errorMsgs){
		log.info("PoPurchaseOrderHeadMainService.checkPoPurchaseLines");
		
		String message       = null;
		String tabName       = "明細資料頁籤";
		String categoryType  = orderHead.getCategoryType();
		String brandCode     = orderHead.getBrandCode();
		String orderTypeCode = orderHead.getOrderTypeCode();
		Double exchangeRate  = orderHead.getCurrencyCode() == null || orderHead.getCurrencyCode().equals("TW") ? 1.0 : orderHead.getExchangeRate();
	    
		// 取稅別
		BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode) );
		String taxCode = buOrderType.getTaxCode();
		try{
			Double totalUsedPriceAmount = 0.0;//本次採購使用預算
			List poPurchaseOrderLines = orderHead.getPoPurchaseOrderLines();
			
			if(poPurchaseOrderLines.equals("") || poPurchaseOrderLines.size() == 0){//檢核明細未輸入-Jerome				
				message = "請輸入明細資料";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}
			
			
			if(poPurchaseOrderLines != null && poPurchaseOrderLines.size() > 0){
				int intactRecordCount = 0;
				for(int i = 0 ; i < poPurchaseOrderLines.size(); i++){
					try{
						PoPurchaseOrderLine poPurchaseOrderLine = (PoPurchaseOrderLine)poPurchaseOrderLines.get(i);
						if(!"1".equals(poPurchaseOrderLine.getIsDeleteRecord())){
							String itemCode      = poPurchaseOrderLine.getItemCode();
							Double quantity      = poPurchaseOrderLine.getQuantity();
							Double actualPurchaseQuantity	= poPurchaseOrderLine.getActualPurchaseQuantity();
							ImItem imItem = imItemService.findItem(brandCode, itemCode);
							
							if(null==imItem)
								throw new NoSuchObjectException("查無" + tabName + "中第" + (i + 1) + "項明細的品號！" + itemCode);
						    	                    
							String itemCategory     = imItem.getItemCategory() ;
							String isTax            = imItem.getIsTax();
						
							if(!StringUtils.hasText(itemCode))
								throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的品號！");
							if(null!=categoryType && !categoryType.equals(itemCategory) && "2".equals( branchCode ))
								throw new ValidationErrorException( tabName + "中第" + (i + 1) + "項明細的業種子類與訂購單主檔不符合！");
							if(null!=isTax && !isTax.equals(taxCode)  && "2".equals( branchCode ) )
								throw new ValidationErrorException( tabName + "中第" + (i + 1) + "項明細的稅別不符合！");
							if(!ValidateUtil.isEnglishAlphabetOrNumber(itemCode))
								throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號必須為A~Z、a~z、0~9！");
							if(quantity == null || quantity == 0D )	//數量不可以是零 
								throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的數量！");
							if(actualPurchaseQuantity == null || actualPurchaseQuantity == 0D )	//最終採購量不可以是零 
								throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的最終採購量！");
							
							List onHandQtyByItemCodes = imItemOnHandViewDAO.getOnHandQtyByItemCodes("'" + poPurchaseOrderLine.getItemCode()+ "'");
							if(onHandQtyByItemCodes.size() > 0){
								Object[] itemCodeAndQty = (Object[]) onHandQtyByItemCodes.get(0);
								poPurchaseOrderLine.setStockOnHandQty((Double) itemCodeAndQty[1]);
								poPurchaseOrderLineDAO.update(poPurchaseOrderLine);	// update 目前庫存
							}
							totalUsedPriceAmount += (poPurchaseOrderLine.getQuantity() * poPurchaseOrderLine.getForeignUnitCost() * exchangeRate);
							intactRecordCount++;
						}
					}
					catch(Exception ex){
						log.info( "checkLind : " + ex.getMessage() );
						message = ex.getMessage();
						siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
						errorMsgs.add(message);
						log.error(message);	            
					}  	        
				}
				if(intactRecordCount == 0){
					message = tabName + "中至少需輸入一筆完整的資料！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message );
				}
				Double totalRemainderBudget = orderHead.getTotalBudget() - orderHead.getTotalAppliedBudget() - orderHead.getTotalSigningBudget() + orderHead.getTotalReturnedBudget();
				DecimalFormat df = new DecimalFormat("##.00");
				totalUsedPriceAmount = Double.parseDouble(df.format(totalUsedPriceAmount));
				totalRemainderBudget = Double.parseDouble(df.format(totalRemainderBudget));
				/*
				 * 1.buy-一般商品採購 
				 * 2.present-企劃贈品採購 
				 * 3.display-陳列商品採購 
				 * 4.watchpart-手錶零件 
				 * 5.sample-Sample採購 
				 */ 
				if(totalUsedPriceAmount > (totalRemainderBudget)&& !(
						"present".equals(orderHead.getPurchaseType())  || 
						"display".equals(orderHead.getPurchaseType())  ||
						"watchpart".equals(orderHead.getPurchaseType())||
						"sample".equals(orderHead.getPurchaseType()))){
					message = "採購金額大於剩餘可使用預算,無法執行存檔！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message );
				}
			}
			else{
				/*
				message = tabName + "中至少需輸入一筆資料！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
				*/
			} 
		}
		catch(Exception ex){
			message = "檢核" + tabName + "時發生錯誤，原因：" + ex.toString();
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		}
	}
   
    /** 2009.09.28 arthur
     * 依PO更新預算檔資料
     * @param purchaseHead
     * @parm  newStatus
     * @parm  loginUser
     * @return none
     * @throws ValidationErrorException
     * T2:依照年,月,業種,品牌,大分類,中分類     -> 扣除進貨成本*進貨數量
     * T1:依照年度,業種(T1BS...),ALL,ALL,ALL -> 扣除售價*進貨數量
     * T1CO:依照年度,業種(T1CO),ALL,ALL,ALL  -> 扣除 PoHead.asignedBudget
     */
    public void updateActualBudget( PoPurchaseOrderHead poPurchase, String beforeChangeStatus, String formStatus, String loginUser ) throws Exception {
    	//log.info("PoPurchaseOrderHeadMainService.updateActualBudget catetoryType="+poPurchase.getCategoryType());
    	log.info("1.PoPurchaseOrderHeadMainService.updateActualBudget brand="+poPurchase.getBrandCode()+"  orderno="+poPurchase.getOrderTypeCode()+"_"+poPurchase.getOrderNo()+" formStatus="+formStatus+"  beforeChangeStatus="+beforeChangeStatus);
    	log.info("111DDD--2...."+beforeChangeStatus+"-----------"+formStatus);
    	List<PoPurchaseOrderLine> poPurchaseOrderLines = poPurchase.getPoPurchaseOrderLines();
    	log.info("111ddd---3"+poPurchaseOrderLines.size());
    	
    	
    	if (null != poPurchaseOrderLines) {
    		log.info("2.PoPurchaseOrderHeadMainService.poPurchaseOrderLines.size =" + poPurchaseOrderLines.size());
    		BuBrand buBrand   = buBrandService.findById( poPurchase.getBrandCode() );    		
    		FiBudgetHead fiBudgetHead = null;
    		FiBudgetLine fiBudgetLine;
    		//List <FiBudgetLine> fiBudgetLine ;
    		try{
    			Double exchangeRate = NumberUtils.getDouble(poPurchase.getExchangeRate());
    			if(0 == exchangeRate)
    				exchangeRate = 1D;
    			
    			// 取 Budget Head Data
    			fiBudgetHead = findBudgetHead( poPurchase );
    			log.info("取 Budget Head Data"+fiBudgetHead);
    			if ( null==fiBudgetHead ){
    				throw new ValidationErrorException( poPurchase.getErrorMessage() );
    			}

    			for (PoPurchaseOrderLine poPurchaseOrderLine : poPurchaseOrderLines) {
    				
    				// 取 ITEM 預算扣除方式 DATA
    				ImItem imItem = imItemService.findItem(poPurchase.getBrandCode(), poPurchaseOrderLine.getItemCode());
    				if(null==imItem){
    					throw new ValidationErrorException( "無此品號 : "+ poPurchaseOrderLine.getItemCode() + "請檢查！");
    				}
    				
    				List fiBudgetLines;				// 取 Budget Line Data
    				if("M".equals(buBrand.getBudgetCheckType())){	// 預算扣除類別 Y:year/M:month
    					fiBudgetLines = fiBudgetLineDAO.find( fiBudgetHead.getHeadId(), imItem.getBudgetType(), imItem.getItemBrand(), 
    							imItem.getCategory01(),   imItem.getCategory02());
    				}else{						// 其他僅有一筆 fiBudgetLine
    					log.info("buBrand.getBudgetCheckType"+buBrand.getBudgetCheckType());
    					if(poPurchase.getBrandCode().equals("T2")){
    						fiBudgetLines = fiBudgetHead.getFiBudgetLines();
    					}else{
    					fiBudgetLines = fiBudgetLineDAO.find( fiBudgetHead.getHeadId(), "A", "ALL", "ALL", "ALL" );
    					}
    				}
    				log.info("fiBudgetLines___"+fiBudgetLines);
    				if (null != fiBudgetLines && fiBudgetLines.size() > 0) {
    					
    					if(poPurchase.getBrandCode().equals("T2")){
    					//fiBudgetLine = fiBudgetLineDAO.findByPoItem(fiBudgetHead.getHeadId(),imItem.getItemBrand());
    					fiBudgetLine = fiBudgetLineDAO.findByLine(poPurchase.getBudgetLineId());
    					log.info("updateBudgetline"+fiBudgetLine.getFiBudgetHead().getHeadId());
    					log.info("updateBudgetline"+fiBudgetLine.getLineId());
    					}else{
    						fiBudgetLine = (FiBudgetLine) fiBudgetLines.get(0);
    					}
    				}else{
    					if("M".equals(buBrand.getBudgetCheckType())){	// 預算扣除類別 Y:year/M:month
    						throw new FormException("無此預算 " + poPurchase.getBudgetYear() +"/" + poPurchase.getBudgetMonth() + "，請檢查");
    					}else{
    						throw new FormException("無此預算 " + poPurchase.getBudgetYear() + "，請檢查");
    					}
    				}
    				
    				Double useBudgetAmount  = 0D;
    				Double quantity = poPurchaseOrderLine.getActualPurchaseQuantity() - poPurchaseOrderLine.getReceiptedQuantity();
    				if(quantity == 0)
    					continue;
    				
    				// 預算扣除方式 P:UnitPrice/C:Cost/T:整筆扣除
    				// 扣除方式：採購數量 * 原幣單價 * 匯率
    				if("C".equals(buBrand.getBudgetType())){
    					useBudgetAmount =  NumberUtils.getDouble(quantity) * 
    							NumberUtils.round( NumberUtils.getDouble(poPurchaseOrderLine.getForeignUnitCost()) * exchangeRate, 2 ) ;
    				// 扣除方式：採購數量 * 售價
    				}else{						
    					useBudgetAmount = NumberUtils.round( NumberUtils.getDouble(quantity) * 
    							NumberUtils.getDouble(poPurchaseOrderLine.getUnitPrice()), 2 ) ;
    					log.info("line quantity = "+quantity +" line UnitPrice="+poPurchaseOrderLine.getUnitPrice());
    				}    				
    				// 依狀態計算預算檔異動額度 fiBudgetLine 
    				Double signmentAmount = NumberUtils.getDouble(fiBudgetLine.getSigningAmount());
    				Double poActualAmount = NumberUtils.getDouble(fiBudgetLine.getPoActualAmount());
    				log.info("目前status::"+beforeChangeStatus+"下一步status::"+formStatus);
    				// SAVE -> SIGNING or REJECT -> SINGING or FINISH -> SINGING 增加 SingingAmount
    				if(!OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus)) {
    					log.info("SAVE -> SIGNING or REJECT -> SINGING or FINISH(增加)_item="+fiBudgetLine.getItemCode()+"signmentAmount="+signmentAmount);
    					log.info("useBudgetAmount="+useBudgetAmount);
    					log.info("fiBudgetHead.getTotalSigningBudge="+fiBudgetHead.getTotalSigningBudget());
    					fiBudgetLine.setSigningAmount( signmentAmount + useBudgetAmount );
    					fiBudgetHead.setTotalSigningBudget( fiBudgetHead.getTotalSigningBudget() + useBudgetAmount );
    					poPurchaseOrderLine.setFiBuegetLineId( fiBudgetLine.getLineId() );
    					poPurchase.setTotalRemainderBudget(poPurchase.getTotalRemainderBudget());
    					log.info("setSigningAmount="+fiBudgetHead.getTotalSigningBudget() + useBudgetAmount);
    					log.info("TotalRemainderBudget()"+poPurchase.getTotalRemainderBudget());
    					log.info("|簽核中-->簽核中| "+signmentAmount);
    				}
    				
    				// SIGNING -> FINISH 增加 PoActualAmount
    				if( OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.FINISH.equals(formStatus) ) {
    					fiBudgetLine.setPoActualAmount( poActualAmount + useBudgetAmount );
    					fiBudgetHead.setTotalAppliedBudget( fiBudgetHead.getTotalAppliedBudget() + useBudgetAmount );
    					poPurchaseOrderLine.setFiBuegetLineId( fiBudgetLine.getLineId() );
    					log.info("|簽核-->完成| +PoActualAmount="+useBudgetAmount);
    				}
    				
    				// SIGNING -> REJECT or SIGNING -> FINISH 扣除 SingingAmount
    				if(( OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.REJECT.equals(formStatus))||
    						( OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.FINISH.equals(formStatus)) ) {
    					log.info("SIGNING -> REJECT or SIGNING -> FINISH(扣除)_item="+fiBudgetLine.getItemCode()+"signmentAmount="+signmentAmount);
    					log.info("useBudgetAmount="+useBudgetAmount);
    					log.info("fiBudgetHead.getTotalSigningBudge="+fiBudgetHead.getTotalSigningBudget());
    					fiBudgetLine.setSigningAmount( signmentAmount - useBudgetAmount );
    					fiBudgetHead.setTotalSigningBudget( fiBudgetHead.getTotalSigningBudget() - useBudgetAmount );
    					poPurchaseOrderLine.setFiBuegetLineId( fiBudgetLine.getLineId() );
    					log.info("setSigningAmount="+fiBudgetHead.getTotalSigningBudget() + useBudgetAmount);
    					log.info("|簽核-->完成| -SingingAmount&&&&|簽核-->駁回(save)| -SingingAmount="+useBudgetAmount);
    				}
    				
    				// FINISH -> SIGNING 扣除 PoActualAmount
    				if( OrderStatus.FINISH.equals(beforeChangeStatus) && OrderStatus.SAVE.equals(formStatus) ) {
    					fiBudgetLine.setPoActualAmount( poActualAmount - useBudgetAmount );
    					fiBudgetHead.setTotalAppliedBudget( fiBudgetHead.getTotalAppliedBudget() - useBudgetAmount );//原本是加
    					poPurchaseOrderLine.setFiBuegetLineId( fiBudgetLine.getLineId() );
    					log.info("|反確認FINISH-->SAVE| +PoActualAmount="+useBudgetAmount);
    				}
    				
    				// FINISH -> SIGNING or FINISH -> CLOSE 增加 PoReturnAmount
    				if( OrderStatus.FINISH.equals(beforeChangeStatus) && OrderStatus.CLOSE.equals(formStatus) ) {
    					fiBudgetLine.setPoReturnAmount( fiBudgetLine.getPoReturnAmount() + useBudgetAmount );
    					fiBudgetHead.setTotalReturnedBudget( fiBudgetHead.getTotalReturnedBudget() +  useBudgetAmount );
    					poPurchaseOrderLine.setFiBuegetLineId( fiBudgetLine.getLineId() );
    					log.info("|完成-->結案| +PoReturnAmount="+useBudgetAmount);
    				}
    				
    				modifyActualBudget( fiBudgetLine, loginUser );
    				fiBudgetHeadDAO.update( fiBudgetHead );
    				poPurchaseOrderLine.setFiBuegetLineId(fiBudgetLine.getLineId());
    				poPurchaseOrderLineMainService.update( poPurchaseOrderLine );
    			}
    			
    		}catch (Exception ex) {
    			log.error("採購單扣除預算作業失敗，原因：" + ex.toString());
    			throw new Exception("採購單扣除預算作業失敗，原因：" + ex.getMessage());
    		}
    	}
    }
    
    
    /**修改預算資料
     * @param FiBudgetLine fiBudgetLine
     */
    private void modifyActualBudget( FiBudgetLine fiBudgetLine, String loginUser ) {
	log.info("PoPurchaseOrderHeadMainService.modifyActualBudget");
	fiBudgetLine.setLastUpdatedBy(loginUser);
	fiBudgetLine.setLastUpdateDate(new Date());
	fiBudgetLineDAO.update(fiBudgetLine);
	}
    
	/**
	 * search
	 * 
	 * @param findObjs
	 * @return
	 */
	public List<PoPurchaseOrderHead> find(HashMap findObjs) {
		return poPurchaseOrderHeadDAO.find(findObjs);
	}
	
    /**處理AJAX參數(查詢專櫃POS機號、預設庫別及庫別的倉管人員)
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getPurchaseEmployeeForAJAX(Properties httpRequest) throws Exception{
	log.info("getPurchaseEmployeeForAJAX");
	
	List<Properties> result = new ArrayList();
	Properties properties   = new Properties();
	String categoryType     = httpRequest.getProperty("categoryType");
	String budgetYear     = httpRequest.getProperty("budgetYear");
	String budgetMonth     = httpRequest.getProperty("budgetMonth");
	log.info("ajaxBudget"+budgetYear+categoryType);
	String version30        = httpRequest.getProperty("version30");
	Long headId 			= NumberUtils.getLong(httpRequest.getProperty("headId"));
	PoPurchaseOrderHead head = findById(headId);
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

	try{log.info("-0-0-0-0-0-0-0-3");
	    purchaseAssist = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege( mapAssist );
	    purchaseMember = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege( mapMember );
	    purchaseMaster = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege( mapMaster );	    
	    log.info("null==version30  "+version30);
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
		}log.info("-0-0-0-0-0-0-0-1");
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
	    	if(head != null){
	    		log.info("-0-0-0-0-0-0-0-2");
	    		purchaseAssist = AjaxUtils.produceSelectorData(purchaseAssist, "employeeCode", "chineseName", true, true, head.getPurchaseAssist());
	    		purchaseMember = AjaxUtils.produceSelectorData(purchaseMember, "employeeCode", "chineseName", true, true, head.getPurchaseMember());
	    		purchaseMaster = AjaxUtils.produceSelectorData(purchaseMaster, "employeeCode", "chineseName", true, true, head.getPurchaseMaster());
	    		log.info("purchaseAssist::"+purchaseAssist+"purchaseMember::"+purchaseMember+"purchaseMaster::"+purchaseMaster);
	    	}else{
	    		log.info("-0-0-0-0-0-0-0-0");
	    		purchaseAssist = AjaxUtils.produceSelectorData(purchaseAssist, "employeeCode", "chineseName", true, true, "");
	    		purchaseMember = AjaxUtils.produceSelectorData(purchaseMember, "employeeCode", "chineseName", true, true, "");
	    		purchaseMaster = AjaxUtils.produceSelectorData(purchaseMaster, "employeeCode", "chineseName", true, true, "");
	    	}
	    	properties.setProperty("allPurchaseAssist", AjaxUtils.parseSelectorData(purchaseAssist));
			properties.setProperty("allPurchaseMember", AjaxUtils.parseSelectorData(purchaseMember));
			properties.setProperty("allPurchaseMaster", AjaxUtils.parseSelectorData(purchaseMaster));
	    }
	    
	    List<FiBudgetLine> allFibudgetLine;
	    List<FiBudgetLine> allFibudgetLineDefult;
	    FiBudgetHead fibudgetHead = null;
	    if(null == head.getProcessId()){
	    log.info("headGetnullProcess:"+head.getBrandCode()+"Year="+budgetYear+OrderStatus.FINISH+categoryType+"Month"+budgetMonth);
	     	fibudgetHead = fiBudgetHeadDAO.findByHeadByCategory(head.getBrandCode(),budgetYear,OrderStatus.FINISH,categoryType,budgetMonth);
	    //log.info("採購找業種預算"+fibudgetHead.getHeadId());
	    if(null != fibudgetHead && "T2".equals(head.getBrandCode())){	    		    	
	    	allFibudgetLine = fibudgetHead.getFiBudgetLines();
	    	log.info("allFibudgetLine"+allFibudgetLine.size());
	    	allFibudgetLine =  AjaxUtils.produceSelectorData(allFibudgetLine  ,"lineId","itemBrandCode" , false,  false);
	    	log.info("fibudgetHead !=null & T2");
	    	properties.setProperty("allFibudgetLine", AjaxUtils.parseSelectorData(allFibudgetLine));
	    }else if(null != fibudgetHead && !head.getBrandCode().equals("T2")){
	    	log.info("T1 = "+fibudgetHead.getHeadId());
	    	FiBudgetLine budgetLine = fiBudgetLineDAO.findByHeadId(fibudgetHead.getHeadId()); 
	    	log.info("ProceeNull用HEAD找line"+budgetLine.getLineId());
	    	allFibudgetLine = fiBudgetLineDAO.findByItembrand(budgetLine.getLineId());
	    	log.info("T1Budget"+allFibudgetLine);
	    	allFibudgetLine =  AjaxUtils.produceSelectorData(allFibudgetLine  ,"lineId","itemBrandCode" , false,  false);
	    	properties.setProperty("allFibudgetLine", AjaxUtils.parseSelectorData(allFibudgetLine));
	    	}else{	    		
	    	    	log.info("fibudgetHead==null");
	    	    	allFibudgetLine = new ArrayList<FiBudgetLine> ();
	    	  	    allFibudgetLine =  AjaxUtils.produceSelectorData(allFibudgetLine  ,"lineId","itemBrandCode" , false,  true);
	    	  	    log.info(allFibudgetLine);
	    	  	    properties.setProperty("allFibudgetLine", AjaxUtils.parseSelectorData(allFibudgetLine));	    		    	    
	    	}
	    }else{
	    	log.info("else--10/17");
	    	log.info("BRAND="+head.getBrandCode());
	    	log.info("categoryType=" + categoryType +" budgetYear = "+budgetYear+ " budgetMonth = "+budgetMonth +" Status = "+head.getStatus());	  
	    	if(head.getStatus().equals(OrderStatus.SAVE) && null != head.getProcessId() || head.getStatus().equals(OrderStatus.REJECT) && null != head.getProcessId()){
	    		 fibudgetHead = fiBudgetHeadDAO.findByHeadByCategory(head.getBrandCode(),budgetYear,OrderStatus.FINISH,categoryType,budgetMonth);
	    		 log.info("10/17 Status = save");
	    	}else{
	    		 fibudgetHead = fiBudgetHeadDAO.findByHeadByCategory(head.getBrandCode(),head.getBudgetYear(),OrderStatus.FINISH,head.getCategoryType(),budgetMonth);
	    		 log.info("10/17 Status != save");
	    	}
		    if(null != fibudgetHead && "T2".equals(head.getBrandCode()) ){
		    	log.info("採購找業種預算else"+fibudgetHead.getHeadId());
		    	if(head.getStatus().equals(OrderStatus.SAVE) || head.getStatus().equals(OrderStatus.REJECT)){
		    	allFibudgetLine = fibudgetHead.getFiBudgetLines();		    	
		    		log.info("找業種="+allFibudgetLine.size());		    		
		    		if(null != head.getBudgetLineId()){
		    			allFibudgetLineDefult = fiBudgetLineDAO.findByItembrand(head.getBudgetLineId());
		    			log.info("找業種Defult="+allFibudgetLineDefult.get(0).getCategoryTypeCode1());
				    	allFibudgetLine =  AjaxUtils.produceSelectorData(allFibudgetLine  ,"lineId","itemBrandCode" , false, false,allFibudgetLineDefult.get(0).getItemBrandCode());
				    	properties.setProperty("allFibudgetLine", AjaxUtils.parseSelectorData(allFibudgetLine));
				    	log.info("代預設業種:"+allFibudgetLineDefult.get(0).getItemBrandCode());
		    		}else{
		    			//allFibudgetLine = new ArrayList<FiBudgetLine> ();
			  	    	allFibudgetLine =  AjaxUtils.produceSelectorData(allFibudgetLine  ,"lineId","itemBrandCode" , false,  false);
			  	    	log.info("代預設業種 null size"+allFibudgetLine.size());	
			  	    	properties.setProperty("allFibudgetLine", AjaxUtils.parseSelectorData(allFibudgetLine));
			  	    	log.info("代預設業種null budget ==");		    			
		    		}
		    	}else{		    				    
		    	allFibudgetLine = fiBudgetLineDAO.findByItembrand(head.getBudgetLineId());
		    	allFibudgetLine =  AjaxUtils.produceSelectorData(allFibudgetLine  ,"lineId","itemBrandCode" , false, false,allFibudgetLine.get(0).getItemBrandCode());
		    	log.info("null != fibudgetHead && T2"+allFibudgetLine.size());
		    	properties.setProperty("allFibudgetLine", AjaxUtils.parseSelectorData(allFibudgetLine));
		    	}		    	
		    	/*allFibudgetLine =  AjaxUtils.produceSelectorData(allFibudgetLine  ,"lineId","itemBrandCode" , false, false,allFibudgetLine.get(0).getItemBrandCode());
		    	log.info("null != fibudgetHead && T2"+allFibudgetLine.size());
		    	properties.setProperty("allFibudgetLine", AjaxUtils.parseSelectorData(allFibudgetLine));20151101*/
		    }else if(null == fibudgetHead){		    	
		    	allFibudgetLine = new ArrayList<FiBudgetLine> ();
		  	    allFibudgetLine =  AjaxUtils.produceSelectorData(allFibudgetLine  ,"lineId","itemBrandCode" , false,  true);
		  	    log.info("null budget ==");
		  	    properties.setProperty("allFibudgetLine", AjaxUtils.parseSelectorData(allFibudgetLine));	    	
		    }else if(null != fibudgetHead && !head.getBrandCode().equals("T2")){
		    	log.info("T1 = "+fibudgetHead.getHeadId());
		    	FiBudgetLine budgetLine = fiBudgetLineDAO.findByHeadId(fibudgetHead.getHeadId()); 
		    	log.info("用HEAD找line"+budgetLine.getLineId());
		    	allFibudgetLine = fiBudgetLineDAO.findByItembrand(budgetLine.getLineId());
		    	log.info("T1Budget"+allFibudgetLine);
		    	allFibudgetLine =  AjaxUtils.produceSelectorData(allFibudgetLine  ,"lineId","itemBrandCode" , false,  false);
		    	properties.setProperty("allFibudgetLine", AjaxUtils.parseSelectorData(allFibudgetLine));
		    	}else{
		    		throw new Exception("查無預算商品品牌");
		    	}
	    	
	    }
    	log.info("==get CategoryType Finish==");	    
	    result.add(properties);
	    return result;
	}catch (Exception ex) {
		ex.printStackTrace();
	    log.error("查詢採購人員資料時發生錯誤，原因：" + ex.getMessage());
	    throw new Exception("查詢採購人員資料時發生錯誤失敗！"+ex.toString());
	}
    }
    //找大類------------
    public List<Properties> findInitialCategory01(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	try{
    	Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
    	PoPurchaseOrderHead poHead = findById(headId);
    	BuBrand buBrand = buBrandService.findById( poHead.getBrandCode() );
    	String categoryType = httpRequest.getProperty("categoryType");
    	String budgetYear =  httpRequest.getProperty("budgetYear");
    	String budgetMonth =  httpRequest.getProperty("budgetMonth");    	
    	Long itemBrandCode = NumberUtils.getLong(httpRequest.getProperty("itemBrandCode"));
    	log.info("findInitialOther:"+itemBrandCode+" 大類:"+categoryType);    	
    	List<FiBudgetLine> categoryFibudgetLine;
    	List<FiBudgetLine> categoryFibudgetLineNow;
    	    	
    	if(null == poHead.getProcessId()){
	    FiBudgetHead fibudgetHead = fiBudgetHeadDAO.findByHeadByCategory(poHead.getBrandCode(),budgetYear,OrderStatus.FINISH,categoryType,budgetMonth);
	    if(null != fibudgetHead && "T2".equals(poHead.getBrandCode())){
	    	log.info("狀態在SAVE 沒有PRO_ID :::"+fibudgetHead.getHeadId());	    	
	    	categoryFibudgetLine = fibudgetHead.getFiBudgetLines();	    	
	    	categoryFibudgetLine = fiBudgetLineDAO.findByItembrand(itemBrandCode);
	    	//categoryFibudgetLine = fiBudgetLineDAO.findByItembrand(itemBrandCode);
	    	if(null != categoryFibudgetLine && null != categoryFibudgetLine.get(0).getCategoryTypeCode1() ){	    			    		    		
	    		log.info("初始大類  not null :::"+categoryFibudgetLine.size()+" findcategory------------"+categoryFibudgetLine.get(0).getCategoryTypeCode1());
			    	List <ImItemCategory> category01s = imItemCategoryDAO.findByCategroyCode(poHead.getBrandCode(),categoryFibudgetLine.get(0).getCategoryTypeCode1());
			    	//categoryFibudgetLine =  AjaxUtils.produceSelectorData(categoryFibudgetLine  ,"categoryTypeCode1","categoryValue1" , true,  false);
			    	categoryFibudgetLine  =  AjaxUtils.produceSelectorData(category01s  ,"categoryCode" ,"categoryName",  true,  false);			    	
			    	properties.setProperty("categoryFibudgetLine", AjaxUtils.parseSelectorData(categoryFibudgetLine));
			    		
	    	}else{
	    		categoryFibudgetLine = new ArrayList<FiBudgetLine> ();
		    	categoryFibudgetLine =  AjaxUtils.produceSelectorData(categoryFibudgetLine  ,"categoryTypeCode1","categoryTypeCode1" , false,  true);
		  	    log.info("else NULL:"+categoryFibudgetLine);
		  	    properties.setProperty("categoryFibudgetLine", AjaxUtils.parseSelectorData(categoryFibudgetLine));
	    	}	    	
	    }else if(null == fibudgetHead){
	    	log.info("找大類budgetHead==null");
	    	categoryFibudgetLine = new ArrayList<FiBudgetLine> ();
	    	categoryFibudgetLine =  AjaxUtils.produceSelectorData(categoryFibudgetLine  ,"categoryTypeCode1","categoryTypeCode1" , false,  true);
	  	    //log.info(categoryFibudgetLine);
	  	    properties.setProperty("categoryFibudgetLine", AjaxUtils.parseSelectorData(categoryFibudgetLine));	    	
	    }	
	    }else{
	    	log.info("else跑 大類連動問題");
	    	FiBudgetHead fibudgetHead = null;
	    	if(poHead.getStatus().equals(OrderStatus.SAVE) && null != poHead.getProcessId()||poHead.getStatus().equals(OrderStatus.REJECT) && null != poHead.getProcessId()){
	    		 fibudgetHead = fiBudgetHeadDAO.findByHeadByCategory(poHead.getBrandCode(),budgetYear,OrderStatus.FINISH,categoryType,budgetMonth);	    		 
	    	}else{
	    	 	 fibudgetHead = fiBudgetHeadDAO.findByHeadByCategory(poHead.getBrandCode(),budgetYear,OrderStatus.FINISH,poHead.getCategoryType(),poHead.getBudgetMonth());
	    	}
	    	if(null != fibudgetHead && "T2".equals(poHead.getBrandCode())){
		    	log.info("else findInitialCategory T2 & headId ="+fibudgetHead.getHeadId());	    	
		    	//categoryFibudgetLine = fibudgetHead.getFiBudgetLines();	
		    	if(poHead.getStatus().equals(OrderStatus.SAVE) && null != poHead.getProcessId() || poHead.getStatus().equals(OrderStatus.REJECT) && null != poHead.getProcessId()){
		    		log.info("findBy JS.品牌:"+itemBrandCode);
		    	categoryFibudgetLine = fibudgetHead.getFiBudgetLines();
		    	categoryFibudgetLine = fiBudgetLineDAO.findByItembrand(itemBrandCode);
		    	//properties.setProperty("categoryFibudgetLine", AjaxUtils.parseSelectorData(categoryFibudgetLine));		    		    			    
			    	log.info("狀態 在 SAVE :JS:"+categoryFibudgetLine.size());	
			    	log.info("狀態 在 SAVE 且有PRO_ID::JS-Code:"+categoryFibudgetLine.get(0).getCategoryTypeCode1());
			    
		    	}else{// if(!poHead.getStatus().equals(OrderStatus.SAVE)){
			    	log.info("狀態 != save");
			    	categoryFibudgetLine = fiBudgetLineDAO.findByItembrand(poHead.getBudgetLineId());
			    	log.info("狀態 != save   1:"+categoryFibudgetLine.size());
			    	//List <ImItemCategory> category01s = imItemCategoryService.findByCategoryType(poHead.getBrandCode(),categoryFibudgetLine.get(0).getCategoryTypeCode1());
			    	List <ImItemCategory> category01s = imItemCategoryDAO.findByCategoryCode(poHead.getBrandCode(),categoryFibudgetLine.get(0).getCategoryTypeCode1());
			    	log.info("狀態 != save   2:"+category01s.size() +" code:"+category01s.get(0).getId().getCategoryCode()+" GetClassName:"+category01s.getClass().getName());			    	
			    	//categoryFibudgetLine =  AjaxUtils.produceSelectorData(category01s  ,"categoryCode" ,"categoryName", true,  false);
			    	log.info("狀態 != save   3:" + categoryFibudgetLine.size()+" code:"+categoryFibudgetLine.get(0).getCategoryTypeCode1());
			    	//properties.setProperty("categoryFibudgetLine", AjaxUtils.parseSelectorData(categoryFibudgetLine));
		    	}
		    	if(null != categoryFibudgetLine && null != categoryFibudgetLine.get(0).getCategoryTypeCode1()  ){	    			    	
			    	log.info("else findInitialCategory01 size:"+categoryFibudgetLine.size() +" findcategory-code:"+categoryFibudgetLine.get(0).getCategoryTypeCode1());						
			    	List <ImItemCategory> category01s = imItemCategoryDAO.findByCategoryCode(poHead.getBrandCode(),categoryFibudgetLine.get(0).getCategoryTypeCode1());
			    	categoryFibudgetLine =  AjaxUtils.produceSelectorData(category01s  ,"categoryCode" ,"categoryName", true,  false);	    					   		    	
			    	properties.setProperty("categoryFibudgetLine", AjaxUtils.parseSelectorData(categoryFibudgetLine));
		    	}else{
		    		categoryFibudgetLine = new ArrayList<FiBudgetLine> ();
			    	categoryFibudgetLine =  AjaxUtils.produceSelectorData(categoryFibudgetLine  ,"categoryTypeCode1","categoryTypeCode1" , false,  true);
			  	    log.info("else 空:"+categoryFibudgetLine);
			  	    properties.setProperty("categoryFibudgetLine", AjaxUtils.parseSelectorData(categoryFibudgetLine));
		    	}    	
		    }else if(null == fibudgetHead){
		    	log.info("fibudgetHead==null");
		    	categoryFibudgetLine = new ArrayList<FiBudgetLine> ();
		    	categoryFibudgetLine =  AjaxUtils.produceSelectorData(categoryFibudgetLine  ,"categoryTypeCode1","categoryTypeCode1" , false,  true);
		  	    log.info(categoryFibudgetLine);
		  	    properties.setProperty("categoryFibudgetLine", AjaxUtils.parseSelectorData(categoryFibudgetLine));	    	
		    }
	    }    
	    result.add(properties);
	    	return result; 
    	}catch(Exception ex){
		    log.error("初始Master發生錯誤，原因：" + ex.toString());
		    throw new Exception("初始Master發生錯誤，原因：" + ex.getMessage());
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
        Map multiList = new HashMap(0);
        Object otherBean     = parameterMap.get("vatBeanOther");
        Object formBindBean = parameterMap.get("vatBeanFormBind");
        String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");
        String employeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
        String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
        String formIdString  = (String)PropertyUtils.getProperty(otherBean, "formId");               
        Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
        
        
        try{
        	Long itemBrand = null;
        	FiBudgetLine fiBudgetline = null;
            PoPurchaseOrderHead poHead = null;
            if(formId != null){
            	poHead = findById(formId);
				if(null!=poHead){
				    brandCode     = poHead.getBrandCode();
				    orderTypeCode = poHead.getOrderTypeCode();
				    log.info("init head != null");
				    if("T2".equals(brandCode) &&  null != poHead.getBudgetLineId()){					    	
				    	List<FiBudgetLine> allFibudgetLine = null;
				    	FiBudgetHead fibudgetHead = fiBudgetHeadDAO.findByHeadByCategory(poHead.getBrandCode(),poHead.getBudgetYear(),OrderStatus.FINISH,poHead.getCategoryType(),poHead.getBudgetMonth());
				    	if(null != fibudgetHead){
				    	allFibudgetLine = fibudgetHead.getFiBudgetLines();
				    	List<FiBudgetLine> allFibudgetLine2 = fiBudgetLineDAO.findByItembrand(poHead.getBudgetLineId());
		    	    	log.info("executeInit = "+allFibudgetLine.size() + " itembrand "+allFibudgetLine2.get(0).getItemBrandCode());
		    	    	multiList.put("allFibudgetLineInit", AjaxUtils.produceSelectorData(allFibudgetLine, "lineId", "itemBrandCode", false, true ,allFibudgetLine2.get(0).getItemBrandCode()));
		    	    			    	    	
		    	    	List<FiBudgetLine> categoryFibudgetLine2 = fiBudgetLineDAO.findByItembrand(poHead.getBudgetLineId());
		    	    	List<FiBudgetLine> categoryFibudgetLine = fibudgetHead.getFiBudgetLines();		    	  
		    	    	log.info("大類initCode:"+categoryFibudgetLine2.get(0).getCategoryTypeCode1()+" initSize:"+categoryFibudgetLine.size());
		    	    	if(null != categoryFibudgetLine2 && categoryFibudgetLine.size() > 0 && StringUtils.hasText(poHead.getCategory01())){
		    	    	//List <ImItemCategory> category01s = imItemCategoryDAO.findByCategroyCode(poHead.getBrandCode(),categoryFibudgetLine2.get(0).getCategoryTypeCode1());
		    	    	List <ImItemCategory> category01s = imItemCategoryDAO.findByCategoryCode(poHead.getBrandCode(),categoryFibudgetLine2.get(0).getCategoryTypeCode1());
		    	    	List <ImItemCategory> category02s = imItemCategoryService.findByCategoryType(poHead.getBrandCode(), "CATEGORY01");
		    	    	//log.info("ImItemCategory02"+category02s.size()+category02s.get(2).getId().getCategoryCode());
		    	    	log.info("ImItemcategory01s:"+category01s.size()+category01s.get(0).getId().getCategoryCode());
				    	//categoryFibudgetLine =  AjaxUtils.produceSelectorData(categoryFibudgetLine  ,"categoryTypeCode1","categoryTypeCode1" , true,  false);
				    	categoryFibudgetLine  =  AjaxUtils.produceSelectorData(category01s  ,"categoryCode" ,"categoryName",  true,  false);
				    	log.info("executeInit categoryFibudgetLine :"+category01s.size()+" and id:"+category01s.get(0).getId().getCategoryCode());
				    	multiList.put("categoryFibudgetLine", AjaxUtils.produceSelectorData(category01s, "categoryCode" ,"categoryName", true, true ,poHead.getCategory01()));
				    	log.info("大類init2Code:"+categoryFibudgetLine.get(0));				    	
			            //multiList.put("categoryFibudgetLine", AjaxUtils.produceSelectorData(category02s, "categoryCode" ,"categoryName", true, true ,poHead.getCategory01()));
			    	    	}else{
			    	    		
			    	    	}
				    	}else{
				    		log.info("Initail 此業種內無選擇商品品牌");
				    	}
				    }else{
				    	log.info("Initail 無商品品牌");
				    }
				    
				}				
            }
            
            
            List<BuCommonPhraseLine> allBudgetYearList  = buCommonPhraseService.getBuCommonPhraseLines("BudgetYearList");
            multiList.put("allBudgetYearList", AjaxUtils.produceSelectorData(allBudgetYearList, "lineCode", "name", false, true));
            log.info("init Year"+allBudgetYearList);
		    List<BuCommonPhraseLine> allBudgetMonthList = buCommonPhraseService.getBuCommonPhraseLines("Month");
		    multiList.put("allBudgetMonthList", AjaxUtils.produceSelectorData(allBudgetMonthList, "lineCode", "attribute1", false, false));
		    log.info("init month"+allBudgetMonthList);
		    List<ImItemCategory>     allItemCategory    = imItemCategoryService.findByCategoryType(brandCode, "ITEM_CATEGORY");
		    multiList.put("allItemCategory", AjaxUtils.produceSelectorData(allItemCategory, "categoryCode", "categoryName", true, false));
		    log.info("init Category"+allItemCategory);
		    
            BuBrand     buBrand     = buBrandService.findById( brandCode );
            BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode) );
            if(formId == null){
            	poHead = createNewPoPurchaseHead(parameterMap, resultMap, buBrand, buOrderType.getTaxCode() );
            }else{
            	//poHead = findPoPurchaseHead(parameterMap, resultMap);
            }
            resultMap.put("multiList", multiList);
            resultMap.put("form", poHead);
            resultMap.put("employeeCode",      employeeCode);
            resultMap.put("status",            poHead.getStatus() );
            resultMap.put("branchCode",        buBrand.getBranchCode());	// 2->T2
            resultMap.put("brandName",         buBrand.getBrandName());
            resultMap.put("budgetType",        buBrand.getBudgetType());	// 預算扣除方式 P:UnitPrice/C:Cost/T:整筆扣除
            resultMap.put("budgetCheckType",   buBrand.getBudgetCheckType());	// 預算扣除類別 Y:year/M:month
            resultMap.put("typeCode",          buOrderType.getTypeCode());
            resultMap.put("statusName",        OrderStatus.getChineseWord(poHead.getStatus()));
            //resultMap.put("createdByName",     UserUtils.getUsernameByEmployeeCode(poHead.getCreatedBy()));
            // 預防轉檔資料中沒有轉入 supplierName
		    if( StringUtils.hasText(poHead.getSupplierCode()) && !StringUtils.hasText(poHead.getSupplierName()) ){
		    	BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(poHead.getBrandCode(), poHead.getSupplierCode());
		    	if(null != buSWAV)
		    		resultMap.put("supplierName", buSWAV.getChineseName() );
		    }log.info("return resultMap");
	    	return resultMap;       	
        }catch (Exception ex) {
        	log.error("採購單初始化失敗，原因：" + ex.toString());
	    	throw new Exception("採購單初始化別失敗，原因：" + ex.toString());
        }           
    }

    
    /** 產生一筆新的 PoPurchaseOrderHead 
     * @param argumentMap
     * @param resultMap
     * @return
     * @throws Exception
     */
    public PoPurchaseOrderHead createNewPoPurchaseHead(Map parameterMap, Map resultMap, BuBrand buBrand, String taxCode ) throws Exception {
    	log.info("createNewPoPurchaseHead");
    	Object otherBean     = parameterMap.get("vatBeanOther");
    	String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");		
    	String employeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    	String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");

    	try{
    		PoPurchaseOrderHead form = new PoPurchaseOrderHead();
    		form.setBrandCode(            brandCode);
    		form.setOrderTypeCode(        orderTypeCode);
    		form.setStatus(               OrderStatus.SAVE);
    		form.setSuperintendentCode(   employeeCode );	// 2010.01.05 user 取消
    		form.setSuperintendentName(   UserUtils.getUsernameByEmployeeCode(employeeCode));
    		form.setCreatedBy(            employeeCode);
    		form.setLastUpdatedBy(        employeeCode);
    		form.setCreationDate(         DateUtils.parseDate(DateUtils.format(new Date())));
    		form.setLastUpdateDate(       DateUtils.parseDate(DateUtils.format(new Date())));
    		form.setExchangeRate(         1D);	// 匯率預設
    		form.setInvoiceTypeCode(      "3"); //三聯
    		form.setTaxType(              "3"); // 應稅
    		form.setTaxRate(              5d); 
    		form.setAsignedBudget(        0D);
    		form.setIsPartialShipment(    "N");
    		form.setPurchaseType(         "buy");
    		form.setPurchaseOrderDate(    new Date()) ;
    		form.setBudgetYear(           String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
    		
    		if( "M".equals(buBrand.getBudgetCheckType()) ){	// 預算扣除類別 Y:year/M:month
    			form.setBudgetMonth( String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1));
    		}
    		
    		if("EPP".equals(orderTypeCode) ){ // T2 EPP
    			form.setDefaultWarehouseCode( buBrand.getDefaultWarehouseCode11().split(",")[0] );
    		}else{ // else
    			form.setDefaultWarehouseCode( buBrand.getDefaultWarehouseCode1().split(",")[0] );
    		}
    		
    		//如果 國外採購單 START
    		if( "F".equals(taxCode) || "POF".equals(orderTypeCode) ) {
    			form.setInvoiceTypeCode("I"); 	//收據
    			form.setTaxType("1"); 		//免稅
    			form.setTaxRate(0D);
    		}
    		saveTmp(form);
    		log.info("createNewReturnForm");
    		return form;
    	}catch (Exception ex) {
    		log.error("產生新採購單失敗，原因：" + ex.toString());
    		throw new Exception("產生採購單發生錯誤！");
    	}
    }
    
	
    /** form 啟始時查出該筆DATA
     * @param argumentMap
     * @param resultMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public PoPurchaseOrderHead findPoPurchaseHead(Map parameterMap, Map resultMap) 
    	throws FormException, Exception {
    	log.info("findPoPurchaseHead");
		try{
		    Object otherBean = parameterMap.get("vatBeanOther");
		    String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
		    Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
		    PoPurchaseOrderHead form = findById(formId);
		    if(form != null){
		        return form;
		    }else{
		    	throw new NoSuchObjectException("查無採購單主鍵：" + formId + "的資料！");
		    }	    
		}catch (FormException fe) {
		    log.error("查詢採購單失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		}catch (Exception ex) {
		    log.error("查詢採購單發生錯誤，原因：" + ex.toString());
		    throw new Exception("查詢採購單發生錯誤！");
		}
    }
    
    
    /**執行進貨單查詢初始化
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
	    //BuBrand     buBrand     = buBrandService.findById( brandCode );
	    BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode) );
	    
	    Map multiList = new HashMap(0);
	    List<BuOrderType>    allOrderTypes   = buOrderTypeService.findOrderbyType(brandCode, buOrderType.getTypeCode());
	    List<ImItemCategory> allItemCategory = imItemCategoryService.findByCategoryType(brandCode, "ITEM_CATEGORY");
	    multiList.put("allOrderTypes",   AjaxUtils.produceSelectorData(allOrderTypes,    "orderTypeCode", "name",         true, false));
	    multiList.put("allItemCategory", AjaxUtils.produceSelectorData( allItemCategory, "categoryCode",  "categoryName", true, true));
	    resultMap.put("multiList",multiList);
	    return resultMap;       	
        }catch (Exception ex) {
	    log.error("採購單查詢初始化失敗，原因：" + ex.toString());
	    throw new Exception("採購單查詢初始化失敗，原因：" + ex.toString());
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
    	    Date startDate 		= DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("startDate") );
    	    Date endDate 		= DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("endDate") );
    	    
    	    HashMap findObjs = new HashMap();
    	    findObjs.put("brandCode",          httpRequest.getProperty("brandCode"));
    	    findObjs.put("orderTypeCode",      httpRequest.getProperty("orderTypeCode"));
    	    findObjs.put("startOrderNo",       httpRequest.getProperty("startOrderNo"));
    	    findObjs.put("endOrderNo",         httpRequest.getProperty("endOrderNo")); 	   
    	    findObjs.put("status",             httpRequest.getProperty("status"));
    	    findObjs.put("superintendentCode", httpRequest.getProperty("superintendentCode"));
    	    findObjs.put("supplierCode",       httpRequest.getProperty("supplierCode")); 
    	    findObjs.put("categoryType",       httpRequest.getProperty("categoryType")); 
    	    findObjs.put("startDate",          startDate);
    	    findObjs.put("endDate",            endDate);
    	    findObjs.put("startSourceOrderNo",  httpRequest.getProperty("startSourceOrderNo"));
    	    findObjs.put("endSourceOrderNo",    httpRequest.getProperty("endSourceOrderNo")); 
    	    findObjs.put("startItemCode",       httpRequest.getProperty("startItemCode"));
    	    findObjs.put("endItemCode",         httpRequest.getProperty("endItemCode")); 	
    	    findObjs.put("startQuotationCode",       httpRequest.getProperty("startQuotationCode"));
    	    findObjs.put("endQuotationCode",         httpRequest.getProperty("endQuotationCode")); 	
    	    findObjs.put("startPoOrderNo",       httpRequest.getProperty("startPoOrderNo"));
    	    findObjs.put("endPoOrderNo",         httpRequest.getProperty("endPoOrderNo")); 	    	    
    	    //==============================================================    	   
    	    List<PoPurchaseOrderHead> poHeads = 
    	    	(List<PoPurchaseOrderHead>) poPurchaseOrderHeadDAO.findPageLine(findObjs, 
    	    			iSPage, iPSize, ImReceiveHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");
    	    log.info("ResultSize=["+ poHeads.size() + "]" + findObjs);
    	    if (poHeads != null && poHeads.size() > 0) {    	    
    	    	Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;    // 取得第一筆的INDEX 	
    	    	Long maxIndex = (Long)poPurchaseOrderHeadDAO.findPageLine(findObjs, -1, iPSize, 
    	    		PoPurchaseOrderHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount");	// 取得最後一筆 INDEX
    	    	
    	    	for(PoPurchaseOrderHead poHead : poHeads){    
    	    	    poHead.setStatus(OrderStatus.getChineseWord(poHead.getStatus()));
    	    	    if(null==poHead.getSuperintendentCode())
    	    		poHead.setSuperintendentName(UserUtils.getUsernameByEmployeeCode(poHead.getSuperintendentCode()));
    	    	}
    	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, poHeads, gridDatas, firstIndex, maxIndex));
    	    } else {
    	        result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, gridDatas));
    	    }
    	
    	    return result;
    	}catch(Exception ex){
    	    log.error("載入頁面顯示的採購單查詢發生錯誤，原因：" + ex.toString());
    	    throw new Exception("載入頁面顯示的採購單查詢失敗！");
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
    	    String startDate     = (String)PropertyUtils.getProperty(formBindBean, "startDate");
    	    String endDate       = (String)PropertyUtils.getProperty(formBindBean, "endDate");
    	    Date actualStartDate = null;
    	    Date actualEndDate   = null;
    	    if(StringUtils.hasText(endDate)){
    		actualStartDate = DateUtils.parseDate("yyyy/MM/dd", startDate);
    	    }
    	    if(StringUtils.hasText(endDate)){
    		actualEndDate = DateUtils.parseDate("yyyy/MM/dd", endDate);
	    }  	    
    	    
    	    HashMap findObjs = new HashMap();
    	    findObjs.put("brandCode",          (String)PropertyUtils.getProperty(formBindBean, "brandCode"));
	    findObjs.put("orderTypeCode",      (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode"));
	    findObjs.put("startOrderNo",       (String)PropertyUtils.getProperty(formBindBean, "startOrderNo"));
	    findObjs.put("endOrderNo",         (String)PropertyUtils.getProperty(formBindBean, "startOrderNo")); 	   
	    findObjs.put("status",             (String)PropertyUtils.getProperty(formBindBean, "status"));
	    findObjs.put("superintendentCode", (String)PropertyUtils.getProperty(formBindBean, "superintendentCode"));  	    
	    findObjs.put("startDate",          actualStartDate);
	    findObjs.put("endDate",            actualEndDate);     
    	    List<PoPurchaseOrderHead> poHeads = 
    	    	(List<PoPurchaseOrderHead>)poPurchaseOrderHeadDAO.findPageLine(findObjs, 
    	    			-1, -1, PoPurchaseOrderHeadDAO.QUARY_TYPE_SELECT_ALL).get("form");
    	    log.info("ResultSize=["+ poHeads.size() + "]" + findObjs);
	}catch(Exception ex){
	    log.error("更新選取採購資料失敗，原因：" + ex.toString());
	    throw new Exception("更新選取採購資料失敗，原因：" + ex.getMessage());		
	}
    }
    
    public Map getSearchSelection(Map parameterMap) throws Exception{
	log.info("getSearchSelection");
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    Object pickerBean       = parameterMap.get("vatBeanPicker");
	    String timeScope        = (String)PropertyUtils.getProperty(pickerBean,    AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys    = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    log.info(" selected size = " + result.size() );
	    if(result.size() > 0 )
	        pickerResult.put("result", result);
	    
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel",  new String[]{"vatBeanPicker"});
	    
	    return resultMap;
	}catch(Exception ex){
	    log.error("執行採購單檢視失敗，原因：" + ex.toString());
	    throw new Exception("執行採購單檢視失敗，原因：" + ex.getMessage());		
	}
    }
    
    
    /** AJAX 取回員工代號與姓名
     * @param httpRequest
     * @return
     */
    public List<Properties> getAJAXFormDataByEmp(Properties httpRequest)throws Exception {
	log.info("getAJAXFormDataByEmp");
	List       re  = new ArrayList();
	Properties pro = new Properties();
	String employeeCode     = httpRequest.getProperty("employeeCode");

	if( StringUtils.hasText(employeeCode)) {
	    pro.setProperty("employeeName", UserUtils.getUsernameByEmployeeCode(employeeCode));
	}else{
	    pro.setProperty("employeeName", "");
	}
	log.info("getAJAXFormDataByEmp employeeCode=" + employeeCode + 
		 ",employeeName=" + UserUtils.getUsernameByEmployeeCode(employeeCode));
	re.add(pro);
	return re;
    }
    
    
    /**計算PO單預算
     * @param headObj
     */
    public FiBudgetHead findBudgetHead(PoPurchaseOrderHead poHead) throws Exception {
	log.info("PoPurchaseOrderHeadMainService.findBudgetHead");
	try{
	    if (null==poHead.getCategoryType() || !StringUtils.hasText(poHead.getCategoryType())){
			poHead.setErrorMessage( "採購單預算未選擇業種類別！" );
			return null;
	    }
	    String categoryType = null;
	    FiBudgetHead fiBudgetHead = null;
	    log.info("PoPurchaseOrderHeadMainService.findBudgetHead2="+poHead.getHeadId()+poHead.getBudgetLineId()+poHead.getCategory01()+poHead.getCategoryType());
	    BuBrand buBrand = buBrandService.findById( poHead.getBrandCode() );
	    log.info("BrandFind"+buBrand);
	    List<ImItemCategory> allItemCategory = imItemCategoryService.findByCategoryType(poHead.getBrandCode(), "ITEM_CATEGORY");
	    for(ImItemCategory imItemCategory : allItemCategory ){
	    	log.info("P-K-8:"+imItemCategory);
			if( poHead.getCategoryType().equals( imItemCategory.getId().getCategoryCode() ) ){
				log.info("P-K-9:"+imItemCategory.getId().getCategoryCode());
				if(null == poHead.getBudgetLineId() /*&& null != poHead.getProcessId() ||  0 == poHead.getBudgetLineId() && null != poHead.getProcessId()*/){
					log.info("P-K-10:"+poHead.getBudgetLineId());
				categoryType = imItemCategory.getParentCategoryCode(); //扣採購單預算業種子類
					log.info("find_old_categoryType"+categoryType);
				}else{
				categoryType = imItemCategory.getId().getCategoryCode();
				log.info("find_New_categoryType"+categoryType);
				}
				break;
			}
	    }
	    if (!StringUtils.hasText(categoryType)){
			poHead.setErrorMessage( "預算業種類別無對應業種大類！" );
			return null;
	    }
	    log.info("find_findObjs");
	    HashMap findObjs = new HashMap();
	    findObjs.put("orderTypeCode", FiBudgetHead.BUDGET_ORDER_TYPE_CODE_PO);
	    findObjs.put("categoryType",  categoryType );
	    findObjs.put("brandCode",     poHead.getBrandCode());
	    findObjs.put("budgetYear",    poHead.getBudgetYear());
	    findObjs.put("budgetMonth",    poHead.getBudgetMonth());
	    // 預算扣除類別 Y:year/M:month
	    if ("M".equals(buBrand.getBudgetCheckType()) && StringUtils.hasText(poHead.getBudgetMonth())){
	    	findObjs.put("budgetMonth", poHead.getBudgetMonth() );
	    }
	    log.info("fiBudgetHeads預算++"+FiBudgetHead.BUDGET_ORDER_TYPE_CODE_PO);
	    log.info("fiBudgetHeads預算++業種="+categoryType);
	    log.info("fiBudgetHeads預算++品牌="+poHead.getBrandCode());
	    log.info("fiBudgetHeads預算++年="+poHead.getBudgetYear());
	    log.info("fiBudgetHeads預算++月="+poHead.getBudgetMonth());
	    List<FiBudgetHead> fiBudgetHeads = null;
	    if(null != poHead.getBudgetLineId() && poHead.getBudgetLineId() != 0){
	    	fiBudgetHeads = fiBudgetHeadService.find_new(findObjs,poHead);//------find_new------- 	    	
	    }
	    else{
	    	fiBudgetHeads = fiBudgetHeadService.find(findObjs);
	    }
	    log.info("fiBudgetHeads預算++"+fiBudgetHeads);
	    if (null != fiBudgetHeads && fiBudgetHeads.size() > 0) {
	    	fiBudgetHead = (FiBudgetHead) fiBudgetHeads.get(0);
	    	log.info("headGetII::"+ fiBudgetHead.getHeadId());
	    	poHead.setErrorMessage("NONE");
	    }else{	// 預算扣除類別 Y:year/M:month
			if ("M".equals(buBrand.getBudgetCheckType()) && StringUtils.hasText(poHead.getBudgetMonth()) ){
			    poHead.setErrorMessage( poHead.getBudgetYear() +"/" + poHead.getBudgetMonth() +"無此預算，請檢查!");
			}else{
			    poHead.setErrorMessage( poHead.getBudgetYear() +"無此預算，請檢查!");
			}
	    }
	    return fiBudgetHead;
	}catch(Exception ex){
	    log.error("查詢預算失敗，原因：" + ex.toString());
	    throw new Exception("查詢預算失敗，原因：" + ex.getMessage());		
	}   
    }
    
    /**
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> findInitialCommon(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	String categoryType1 = httpRequest.getProperty("categoryType");
    	String budgetYear =  httpRequest.getProperty("budgetYear");
    	String budgetMonth =  httpRequest.getProperty("budgetMonth");
	    try{
	    	Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    	log.info("findInitialCommon"+headId);
	    	PoPurchaseOrderHead poHead = findById(headId);
	    	BuBrand buBrand = buBrandService.findById( poHead.getBrandCode() );
	    	BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(poHead.getBrandCode(), poHead.getOrderTypeCode()) );
	    	String warehouse = "";
    		if("EPP".equals(poHead.getOrderTypeCode()) ){ // T2 EPP
    			warehouse = buBrand.getDefaultWarehouseCode11();
    		}else{ // else
    			warehouse = buBrand.getDefaultWarehouseCode1();
    		}
    		List<FiBudgetLine> allFibudgetLine;
    		List<FiBudgetLine> categoryFibudgetLine;
    		//if(poHead.getStatus().equals(OrderStatus.SAVE) || poHead.getStatus().equals(OrderStatus.REJECT)){
    			/*FiBudgetHead fibudgetHead = fiBudgetHeadDAO.findByHeadByCategory(poHead.getBrandCode(),budgetYear,OrderStatus.FINISH,categoryType1,budgetMonth);
    			log.info("CommonInit"+fibudgetHead.getFiBudgetLines().size());
    			allFibudgetLine = fibudgetHead.getFiBudgetLines();
    			allFibudgetLine =  AjaxUtils.produceSelectorData(allFibudgetLine  ,"lineId","itemBrandCode" , false,  true);
    			properties.setProperty("allFibudgetLine", AjaxUtils.parseSelectorData(allFibudgetLine));  */  		
    			
    		//}else{
    			/*FiBudgetHead fibudgetHead = fiBudgetHeadDAO.findByHeadByCategory(poHead.getBrandCode(),poHead.getBudgetYear(),OrderStatus.FINISH,poHead.getCategoryType(),poHead.getBudgetMonth());
        		//allFibudgetLine = fibudgetHead.getFiBudgetLines();
        		allFibudgetLine = fiBudgetLineDAO.findByItembrand(poHead.getBudgetLineId());
    	    	log.info("findInitialCommonallFibudgetLine"+allFibudgetLine.size());
    	    	allFibudgetLine =  AjaxUtils.produceSelectorData(allFibudgetLine  ,"lineId","itemBrandCode" , false,  true);
    	    	properties.setProperty("allFibudgetLine", AjaxUtils.parseSelectorData(allFibudgetLine)); */
    		//}
    		
    		//List <ImItemCategory> category01s = imItemCategoryService.findByCategoryType(poHead.getBrandCode(), "CATEGORY01");    		    		
	    	List<ImWarehouse>        allWarehouse       = imWarehouseDAO.findByProperty("ImWarehouse", " and warehouseCode in ('"+warehouse.replaceAll(",", "','")+"') ", new Object[]{});
		    List<BuPaymentTerm>      allPaymentTerm     = buBasicDataService.findPaymentTermByOrganizationAndEnable("TM", null);
		    List<BuCountry>          allCountryCode     = buBasicDataService.findCountryByEnable(null);
		    List<BuCurrency>         allCurrencyCode    = buBasicDataService.findCurrencyByEnable(null);
		    List<BuCommonPhraseLine> allInvoiceCode     = buCommonPhraseService.getBuCommonPhraseLines("InvoiceType");
		    List<BuCommonPhraseLine> allTaxType         = buCommonPhraseService.getBuCommonPhraseLines("TaxType");
		    List<BuCommonPhraseLine> allPurchaseType    = buCommonPhraseService.getBuCommonPhraseLines("PurchaseType");
		    List<BuCommonPhraseLine> allBudgetYearList  = buCommonPhraseService.getBuCommonPhraseLines("BudgetYearList");
		    List<BuCommonPhraseLine> allBudgetMonthList = buCommonPhraseService.getBuCommonPhraseLines("Month");
		    List<ImItemCategory>     allItemCategory    = imItemCategoryService.findByCategoryType(poHead.getBrandCode(), "ITEM_CATEGORY");
		    List<BuOrderType>        allOrderTypes      = buOrderTypeService.findOrderbyType(poHead.getBrandCode(), buOrderType.getTypeCode());
		    List<BuEmployeeWithAddressView> allCreatedBy = new ArrayList();
		    List<BuEmployeeWithAddressView> allPurchaseAssist = new ArrayList();
		    List<BuEmployeeWithAddressView> allPurchaseMember = new ArrayList();
		    List<BuEmployeeWithAddressView> allPurchaseMaster = new ArrayList();		    
		    String oriTaxCode = null==buOrderType.getTaxCode()? "P" : buOrderType.getTaxCode();
		    for( int i=0; i < allOrderTypes.size(); i++ ){
			String taxCode = null==allOrderTypes.get(i).getTaxCode()? "P" : allOrderTypes.get(i).getTaxCode();
			if( !oriTaxCode.equals( taxCode ))
			    allOrderTypes.remove(i);
		    }
		    String categoryType = null==poHead.getCategoryType() ? null : poHead.getCategoryType();
		    if(!StringUtils.hasText(categoryType) && allItemCategory.size()>0) {
		    	categoryType = allItemCategory.get(0).getId().getCategoryCode();
		    }
		    
		    if(StringUtils.hasText(categoryType) ){
		    	
		    	//採購單建立人
		    	HashMap mapCreatedBy = new HashMap();
		    	mapCreatedBy.put("itemCategory",       "ALL");
		    	mapCreatedBy.put("employeeDepartment", "506");
		    	mapCreatedBy.put("employeePosition",   "C");
		    	allCreatedBy = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege( mapCreatedBy );
		    	
		    	//採購助理
		    	HashMap mapAssist = new HashMap();
		    	mapAssist.put("itemCategory",       categoryType);
		    	mapAssist.put("employeeDepartment", "506");
		    	mapAssist.put("employeePosition",   "A");
		    	allPurchaseAssist = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege( mapAssist );

		    	//採購人員
		    	HashMap mapMember = new HashMap();
		    	mapMember.put("itemCategory",       categoryType);
		    	mapMember.put("employeeDepartment", "506");
		    	mapMember.put("employeePosition",   "P");
		    	allPurchaseMember = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege( mapMember );

		    	//採購主管
		    	HashMap mapMaster = new HashMap();
		    	mapMaster.put("itemCategory",       categoryType);
		    	mapMaster.put("employeeDepartment", "506");
		    	mapMaster.put("employeePosition",   "M");
		    	allPurchaseMaster = buEmployeeWithAddressViewService.findByBuItemCategoryPrivilege( mapMaster );
		    }
		    
		    //填單人員再加上DEFAULT一個自己
		    BuEmployeeWithAddressView createdBy = buEmployeeWithAddressViewService.
		    										findbyBrandCodeAndEmployeeCode(poHead.getBrandCode(), poHead.getCreatedBy());
		    if(null != createdBy)
		    	allCreatedBy.add(createdBy);
		    if( OrderStatus.SAVE.equals(poHead.getStatus()) || OrderStatus.REJECT.equals(poHead.getStatus()) ){	
		    	if( null==poHead.getPurchaseAssist())
		    		for( BuEmployeeWithAddressView buEmployee : allPurchaseAssist ){	// 預設採購助理=KEY單人員
		    			if( poHead.getCreatedBy().equals(buEmployee.getEmployeeCode()) )
		    				poHead.setPurchaseAssist(buEmployee.getEmployeeCode());
		    		}
		    	if( null==poHead.getPurchaseMember())
		    		for( BuEmployeeWithAddressView buEmployee : allPurchaseMember ){	// 預設採購人員=KEY單人員
		    			if( poHead.getCreatedBy().equals(buEmployee.getEmployeeCode()) )
		    				poHead.setPurchaseMember(buEmployee.getEmployeeCode());
		    		}
		    	if( null==poHead.getPurchaseMember())
		    		for( BuEmployeeWithAddressView buEmployee : allPurchaseMaster ){	// 預設採購主管=KEY單人員
		    			if( poHead.getCreatedBy().equals(buEmployee.getEmployeeCode()) )
		    				poHead.setPurchaseMaster(buEmployee.getEmployeeCode());
		    		}
		    }
		    log.info("what ==="+allBudgetYearList);
		    //allFiBudgetLine = AjaxUtils.produceSelectorData(allFiBudgetLine  ,"lineId" ,"itemBrandCode",  true,  false);
		    //category01s  =  AjaxUtils.produceSelectorData(category01s  ,"categoryCode" ,"categoryName",  true,  true);
		    allCreatedBy =  AjaxUtils.produceSelectorData(allCreatedBy  ,"employeeCode" ,"chineseName",  true,  false);
		    log.info("what ==Cr="+allCreatedBy);
		    allPurchaseAssist =  AjaxUtils.produceSelectorData(allPurchaseAssist  ,"employeeCode" ,"chineseName",  true,  true);
		    log.info("what ==Ass="+allPurchaseAssist);
		    allPurchaseMember =  AjaxUtils.produceSelectorData(allPurchaseMember  ,"employeeCode" ,"chineseName",  true,  true);
		    log.info("what ==Mem="+allPurchaseMember);
		    allPurchaseMaster =  AjaxUtils.produceSelectorData(allPurchaseMaster  ,"employeeCode" ,"chineseName",  true,  true);
		    log.info("what ==Mas="+allPurchaseMaster);
		    allWarehouse =  AjaxUtils.produceSelectorData(allWarehouse  ,"warehouseCode" ,"warehouseName",  true,  true);
		    log.info("what ==Ware="+allWarehouse);
		    allPaymentTerm =  AjaxUtils.produceSelectorData(allPaymentTerm  ,"paymentTermCode" ,"name",  false,  true);
		    log.info("what ==PaymentT="+allPaymentTerm);
		    allCountryCode =  AjaxUtils.produceSelectorData(allCountryCode  ,"countryCode" ,"countryCName",  false,  true);
		    log.info("what ==allCountryCode="+allCountryCode);
		    allCurrencyCode =  AjaxUtils.produceSelectorData(allCurrencyCode  ,"currencyCode" ,"currencyCName",  false,  true);
		    log.info("what ==allCurrencyCode="+allCurrencyCode);
		    allInvoiceCode =  AjaxUtils.produceSelectorData(allInvoiceCode  ,"lineCode" ,"name",  false,  true);
		    log.info("what ==allInvoiceCode="+allInvoiceCode);
		    allTaxType =  AjaxUtils.produceSelectorData(allTaxType  ,"lineCode" ,"name",  false,  true);
		    log.info("what ==Tax="+allTaxType);
		    allPurchaseType =  AjaxUtils.produceSelectorData(allPurchaseType  ,"lineCode" ,"name",  false,  true);
		    log.info("what ==allPurchaseType="+allPurchaseType);
		    allBudgetYearList =  AjaxUtils.produceSelectorData(allBudgetYearList  ,"lineCode" ,"name",  false,  true);
		    log.info("what ==allBudgetYearList="+allBudgetYearList);
		    allBudgetMonthList =  AjaxUtils.produceSelectorData(allBudgetMonthList  ,"lineCode" ,"attribute1",  false,  false);
		    log.info("what ==allBudgetMonthList="+allBudgetMonthList);
		    allItemCategory =  AjaxUtils.produceSelectorData(allItemCategory  ,"categoryCode" ,"categoryName",  true,  false);
		    log.info("what ==allItemCategory="+allItemCategory);
		    allOrderTypes =  AjaxUtils.produceSelectorData(allOrderTypes  ,"orderTypeCode" ,"name",  true,  true);
		    log.info("what ==allOrderTypes="+allOrderTypes);
		    properties.setProperty("allCreatedBy", AjaxUtils.parseSelectorData(allCreatedBy));
		    properties.setProperty("allPurchaseAssist", AjaxUtils.parseSelectorData(allPurchaseAssist));
		    properties.setProperty("allPurchaseMember", AjaxUtils.parseSelectorData(allPurchaseMember));
		    properties.setProperty("allPurchaseMaster", AjaxUtils.parseSelectorData(allPurchaseMaster));
		    properties.setProperty("allWarehouse", AjaxUtils.parseSelectorData(allWarehouse));
		    properties.setProperty("allPaymentTerm", AjaxUtils.parseSelectorData(allPaymentTerm));
		    properties.setProperty("allCountryCode", AjaxUtils.parseSelectorData(allCountryCode));
		    properties.setProperty("allCurrencyCode", AjaxUtils.parseSelectorData(allCurrencyCode));
		    properties.setProperty("allInvoiceCode", AjaxUtils.parseSelectorData(allInvoiceCode));
		    properties.setProperty("allTaxType", AjaxUtils.parseSelectorData(allTaxType));
		    properties.setProperty("allPurchaseType", AjaxUtils.parseSelectorData(allPurchaseType));
		    //properties.setProperty("allBudgetYearList", AjaxUtils.parseSelectorData(allBudgetYearList));
		    //properties.setProperty("allBudgetMonthList", AjaxUtils.parseSelectorData(allBudgetMonthList));
		    //properties.setProperty("allItemCategory", AjaxUtils.parseSelectorData(allItemCategory));
		    properties.setProperty("allOrderTypes", AjaxUtils.parseSelectorData(allOrderTypes));
		    //properties.setProperty("category01s", AjaxUtils.parseSelectorData(category01s));
		    //properties.setProperty("allOrderTypes", AjaxUtils.parseSelectorData(allFiBudgetLine));
		    log.info("allBudgetYearList==="+allBudgetYearList);
    	    result.add(properties);
    		return result;
		}catch(Exception ex){
		    log.error("初始Master發生錯誤，原因：" + ex.toString());
		    throw new Exception("初始Master發生錯誤，原因：" + ex.getMessage());
		}
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
			if("T2".equals(brandCode)){
				parameters.put("prompt0", brandCode);
				parameters.put("prompt1", orderTypeCode);
				parameters.put("prompt2", "");
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
	
    /**
     * 把標記為刪除的資料刪除
     * 
     * @param salesOrderHead
     */
    private void removeAJAXLine(PoPurchaseOrderHead poPurchase) {    	
        List<PoPurchaseOrderLine> lines = poPurchase.getPoPurchaseOrderLines();        
        if(lines != null && lines.size() > 0){
            for(int i = lines.size() - 1; i >= 0; i--){
            	PoPurchaseOrderLine line = lines.get(i);
            	log.info("把標記為刪除的資料刪除++"+line.getIsDeleteRecord());
	        	if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(line.getIsDeleteRecord())){
	        		lines.remove(line);
	        	}
            }
        }
        //poPurchase.setPoPurchaseOrderLines(lines);
        
        for (int i = 0; i < lines.size(); i++) {
        	PoPurchaseOrderLine line = lines.get(i); 
        	line.setIndexNo(i+1L);
		}
    }
    
    public void executeReverse(Long headId){
    	try{
	    	PoPurchaseOrderHead head = findById(headId);
	    	if(head == null){
	    		
	    	}else{
	    		if(OrderStatus.SIGNING.equals(head.getStatus())){
	    			updateActualBudget(head, head.getStatus(), OrderStatus.SAVE, head.getLastUpdatedBy());
	    		}
	    		
	    		
	    		
	    		
	    	}
    	}catch(Exception e){
    		
    	}
    }
    
    public Object executeReverter(Long headId, String employeeCode) throws Exception {
		log.info("headId = " + headId);
		log.info("employeeCode = " + employeeCode);
    	PoPurchaseOrderHead head = findById(headId);
    	String beforeChangeStatus = head.getStatus();
    	log.info("beforeChangeStatus = " + beforeChangeStatus);
    	if(OrderStatus.FINISH.equals(beforeChangeStatus)){
    		List<PoPurchaseOrderLine> lines = head.getPoPurchaseOrderLines();
    		for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
				PoPurchaseOrderLine line = (PoPurchaseOrderLine) iterator.next();
				if(line.getReceiptedQuantity() > 0){
					log.error("此單據已進過貨不可反轉");
		    		throw new Exception("此單據已進過貨不可反轉");
				}
			}
    		
			// 修改預算檔 fiBudgetLine / status 由 SAVE -> SIGNING && purchaseType=="buy" 一般採購
			if("buy".equalsIgnoreCase(head.getPurchaseType())) {
				//這一段可以用NATIVESQL寫加總扣更快
				updateActualBudget( head, beforeChangeStatus, "SAVE", employeeCode );
			}
			head.setProcessId(null);
			head.setCreatedBy(employeeCode);
			head.setStatus("SAVE");
			poPurchaseOrderHeadDAO.update(head);
			return head;
    	}else{
    		log.error("此單據狀態不可反轉");
    		throw new Exception("此單據狀態不可反轉");
    	}
    }
    
    /**
	 * 執行反確認流程起始
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public void executeReverterProcess(Object bean) throws Exception {
		PoPurchaseOrderHead head = (PoPurchaseOrderHead)bean;
		Object processObj[] = PoPurchaseOrderHeadMainService.startProcess(head);
		head.setProcessId((Long)processObj[0]);
		poPurchaseOrderHeadDAO.update(head);
	}
	
	/**find by pk
	 * @param headId
	 * @return
	 */
	public PoPurchaseOrderHead findByIdForExport(Long headId) throws Exception{
	    log.info("PoPurchaseOrderHeadMainService.findByIdForExport");
	    String message = null;
	    PoPurchaseOrderHead poPurchaseOrderHead = null;
	    try{ 
	    	poPurchaseOrderHead =(PoPurchaseOrderHead) poPurchaseOrderHeadDAO.findByPrimaryKey( PoPurchaseOrderHead.class, headId );
	    	List<PoPurchaseOrderLine> lines = poPurchaseOrderHead.getPoPurchaseOrderLines();
	    	for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
				PoPurchaseOrderLine poPurchaseOrderLine = (PoPurchaseOrderLine) iterator.next();
				ImItem item = imItemService.findItem(poPurchaseOrderHead.getBrandCode(), poPurchaseOrderLine.getItemCode());
				if(null != item){
					poPurchaseOrderLine.setReserve4(item.getSupplierItemCode());
					poPurchaseOrderLine.setReserve5(item.getPurchaseUnit());
				}
			}
	    }catch (Exception ex) {
	    	message = "查無採購單資料，原因：" + ex.getMessage();
	    	log.error(message);
	    	throw new Exception(message);
	    }
	    return poPurchaseOrderHead;
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
    		ImItem item = null;
    		if(StringUtils.hasText(itemCode)){
    			item = imItemService.findItem(brandCode, itemCode);
	    		if(item != null){
					properties.setProperty("SupplierCode", item.getCategory17());
					properties.setProperty("PurchaseCurrencyCode", item.getPurchaseCurrencyCode());
					//log.info("item.getCategory17() = " + item.getCategory17());
	    		}
    		}
    	    result.add(properties);
    	    return result;
    	}catch (Exception ex) {
    	    log.error("查詢商品業種時發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢商品業種時發生錯誤，原因：" + ex.getMessage());
    	}
    }
    
    /**
     * 依據poLine狀態設置Message
     */ 
    public void refreshPoLineMessage(PoPurchaseOrderLine poLine){
    	StringBuffer sb = new StringBuffer();
    	try{
    		if(NumberUtils.getDouble(poLine.getItemMargin()) > 0 
    				&& NumberUtils.getDouble(poLine.getMargin()) < NumberUtils.getDouble(poLine.getItemMargin())){
    			sb.append("毛利率小於商品主檔設置之毛利率;");
    		}

    		if((poLine.getMaxPurchaseQuantity() > 0 && poLine.getQuantity() > poLine.getMaxPurchaseQuantity()) 
    				|| (poLine.getMinPurchaseQuantity() > 0 && poLine.getQuantity() < poLine.getMinPurchaseQuantity())){
    			sb.append("實際採購量超過最高最低採購量;");
    		}
    	}catch(Exception ex){
    		sb.append("訊息提示顯示錯誤，請洽資訊部");
    	}
    	poLine.setMessage(sb.toString());
    }
    
	/**
	 * 歸還採購金額 
	 * @param headObj
	 */
	public List<Properties> updateCloseLine(Properties httpRequest) {
		log.info("PoPurchaseOrderHeadMainService.updateCloseLine");
		List re = new ArrayList();
		Properties pro = new Properties();
		
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		Long lineId = NumberUtils.getLong(httpRequest.getProperty("lineId"));
		String purchaseType = httpRequest.getProperty("purchaseType");
		String employeeCode = httpRequest.getProperty("employeeCode");
		
		PoPurchaseOrderLine poLine = poPurchaseOrderLineDAO.findLine(headId, lineId);
		
		if(null != poLine){
			countItemTotalAmount(null, poLine);
			//一般商品採購 才退預算
			if("buy".equals(purchaseType) && poLine.getActualPurchaseQuantity() != poLine.getReceiptedQuantity()){
				FiBudgetLine fiLine = fiBudgetLineDAO.findById(poLine.getFiBuegetLineId());
				if(null != fiLine){
					fiLine.setPoReturnAmount( NumberUtils.getDouble(fiLine.getPoReturnAmount()) + poLine.getOutstandAmount());
					modifyActualBudget( fiLine, employeeCode );
					FiBudgetHead fiHead = fiLine.getFiBudgetHead();
					if(null != fiHead)
						fiHead.setTotalReturnedBudget( NumberUtils.getDouble(fiHead.getTotalReturnedBudget()) + poLine.getOutstandAmount() );
					fiBudgetHeadDAO.update(fiHead);
				}
				poLine.setReturnedQuantity(poLine.getActualPurchaseQuantity() -  poLine.getReceiptedQuantity());
				poLine.setReturnedAmount(poLine.getReturnedQuantity() * poLine.getLocalUnitCost());
				poLine.setActualPurchaseQuantity(poLine.getReceiptedQuantity());
				poPurchaseOrderLineDAO.update(poLine);
			}
			pro.setProperty("ActualPurchaseQuantity", OperationUtils.roundToStr(poLine.getActualPurchaseQuantity(), 0));
			pro.setProperty("OutstandQuantity", "0");
			pro.setProperty("OutstandAmount", "0");
			pro.setProperty("ReturnedQuantity", OperationUtils.roundToStr(poLine.getReturnedQuantity(), 0));
			pro.setProperty("ReturnedAmount", OperationUtils.roundToStr(poLine.getReturnedAmount(), 2));
		}else{
			pro.setProperty("ActualPurchaseQuantity", "0");
		}
		re.add(pro);
		return re;
	}
	
	public String getProcessSubject(PoPurchaseOrderHead poPurchase){
		StringBuffer subject = new StringBuffer("");
		String categoryName = "";
		String shortName = "";
		if("T2".equals(poPurchase.getBrandCode())){
			ImItemCategory imItemCategory = imItemCategoryDAO.findByCategoryCode(poPurchase.getBrandCode(), "ITEM_CATEGORY", poPurchase.getCategoryType(),"Y");
			if(null != imItemCategory ){
				ImItemCategory parentImItemCategory = imItemCategoryDAO.findByCategoryCode(poPurchase.getBrandCode(), "CATEGORY00", imItemCategory.getParentCategoryCode(),"Y");
				if(null != parentImItemCategory)
					categoryName = parentImItemCategory.getCategoryName() + " - ";
				else
					categoryName = "";
			}else
				categoryName = "";

			BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(poPurchase.getBrandCode(), poPurchase.getSupplierCode());
			if(null!=buSWAV)
				shortName = " - " + buSWAV.getShortName();
			subject.append(categoryName);
			subject.append(MessageStatus.getJobManagerMsg(poPurchase.getBrandCode(), poPurchase.getOrderTypeCode(), poPurchase.getOrderNo()));
			subject.append(shortName);
		}else if ( "T1CO".equals(poPurchase.getBrandCode()) || "T1GS".equals(poPurchase.getBrandCode()) ){
			BuOrderTypeId id = new BuOrderTypeId();
			id.setBrandCode(poPurchase.getBrandCode());
			id.setOrderTypeCode(poPurchase.getOrderTypeCode());
			BuOrderType buOrderType = buOrderTypeService.findById(id);
			subject.append(MessageStatus.getJobManagerMsg(poPurchase.getBrandCode(), poPurchase.getOrderTypeCode(), poPurchase.getOrderNo()) + " - " + poPurchase.getPoOrderNo());
		}else if ( "T1BS".equals(poPurchase.getBrandCode()) ){
			subject.append(MessageStatus.getJobManagerMsg(poPurchase.getBrandCode(), poPurchase.getOrderTypeCode(), poPurchase.getOrderNo()) + " - " + poPurchase.getSupplierCode());
		}else{
			subject.append(MessageStatus.getJobManagerMsg(poPurchase.getBrandCode(), poPurchase.getOrderTypeCode(), poPurchase.getOrderNo()));
		}
		return subject.toString();
	}
	//--------by jason
	public Map updateAJAXPoPurchaseClose(Map parameterMap,Long headId) throws FormException, Exception {
		HashMap resultMap = new HashMap();
		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean    = parameterMap.get("vatBeanOther");
			String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formStatus         = (String)PropertyUtils.getProperty(otherBean, "formStatus");
			String employeeCode       = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			Long itemBrandCode =  NumberUtils.getLong("budgetLineId");
			log.info("itemBrandCode==="+itemBrandCode);
			log.info("111ddd---beforeChangeStatus==="+beforeChangeStatus);
			
			//取得欲更新的bean
			PoPurchaseOrderHead poPurchase = getActualPoPurchase(headId);
			log.info("itemBrand**"+poPurchase.getBudgetLineId()+"*-*-*-"+poPurchase.getCategory01());			
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, poPurchase);
			//刪除mark掉的Line
			removeAJAXLine(poPurchase);

			if( OrderStatus.SIGNING.equals(formStatus) &&
					(OrderStatus.REJECT.equals(beforeChangeStatus) || OrderStatus.SAVE.equals(beforeChangeStatus)) ){
				log.info("111ddd---SIGNING");
				countHeadTotalAmount(poPurchase);	// 每次送出時都 CALL 計算總金額, 寫入金額合計資料 2009.11.27
			}
			
			poPurchase.setStatus(formStatus);
			// 修改預算檔 fiBudgetLine / status 由  ( !SIGNING -> SIGNING || FINISH -> CLOSE ) && purchaseType=="buy" 一般採購
			if( (OrderStatus.SAVE.equals(beforeChangeStatus) || OrderStatus.REJECT.equals(beforeChangeStatus)) && OrderStatus.SIGNING.equals(formStatus) 
					|| (OrderStatus.FINISH.equals(beforeChangeStatus) && OrderStatus.CLOSE.equals(formStatus)) 
					|| (OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.REJECT.equals(formStatus))
					/*|| (OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus))*/
					|| (OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.FINISH.equals(formStatus))
			){
				if("buy".equalsIgnoreCase(poPurchase.getPurchaseType())) {
					//這一段可以用NATIVESQL寫加總扣更快	
					log.info("111ddd---buy");
					updateActualBudget( poPurchase, beforeChangeStatus, formStatus, employeeCode );
				}
				
				// 如果是T4NG，採購單直接結案
				if("T4NG".equals(poPurchase.getBrandCode())){
					poPurchase.setStatus(OrderStatus.FINISH);
					updateActualBudget( poPurchase, OrderStatus.SIGNING, OrderStatus.FINISH, employeeCode );
				}
			}
			
			// 如果是免稅3C採購且採購助理送出，採購單結案
			if( "T2".equals(poPurchase.getBrandCode()) && "D".equals(poPurchase.getCategoryType()) && OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus) ){
				poPurchase.setStatus(OrderStatus.FINISH);
				log.info("單據"+headId +"狀態為"+ formStatus);//觀察未回寫狀態
				updateActualBudget( poPurchase, OrderStatus.SIGNING, OrderStatus.FINISH, employeeCode );
			}
			
			
			//關帳的話把實際採購量弄好
			if(OrderStatus.CLOSE.equals(formStatus) && OrderStatus.FINISH.equals(beforeChangeStatus)){
				List<PoPurchaseOrderLine> lines = poPurchase.getPoPurchaseOrderLines();
				for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
					PoPurchaseOrderLine poPurchaseOrderLine = (PoPurchaseOrderLine) iterator.next();
					poPurchaseOrderLine.setReturnedQuantity(poPurchaseOrderLine.getActualPurchaseQuantity() - poPurchaseOrderLine.getReceiptedQuantity());
					poPurchaseOrderLine.setActualPurchaseQuantity(NumberUtils.getDouble(poPurchaseOrderLine.getReceiptedQuantity()));
				}
			}
			
			String resultMsg = modifyAjaxPoPurchase(poPurchase, employeeCode);
			resultMap.put("entityBean", poPurchase);
			resultMap.put("resultMsg", resultMsg);

			return resultMap;
			//=============================================              
		} catch (FormException fe) {
			log.error("採購單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("採購單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("採購單存檔時發生錯誤，原因：" + ex.getMessage());
		}
	}
	public String updateAJAXHeadTotalAmount(Long headId)
									throws NumberFormatException, FormException, Exception{
		log.info("111dddreturnedAmount--"+headId);
		log.info("PoPurchaseOrderHeadMainService.getAJAXHeadTotalAmount ");
		PoPurchaseOrderHead poHead = findById(NumberUtils.getLong(headId));
		BuBrand buBrand  = buBrandService.findById( poHead.getBrandCode() );
		String tmpSqlLine = 
				" select nvl(sum( nvl(QUANTITY,0) * nvl(FOREIGN_UNIT_COST,0) ) ,0), " +
				"        nvl(sum( nvl(QUANTITY,0) * nvl(LOCAL_UNIT_COST,0) ) ,0),  " +
				"        nvl(sum( nvl(QUANTITY,0) * nvl(UNIT_PRICE,0) ) ,0),  " +
				"        nvl(sum( nvl(QUANTITY,0) ) ,0),  " +
				"        nvl(sum( nvl(RECEIPTED_QUANTITY,0) * nvl(LOCAL_UNIT_COST,0) ) ,0),  " +
				"        nvl(sum( (nvl(QUANTITY,0) - nvl(RECEIPTED_QUANTITY,0)) * nvl(LOCAL_UNIT_COST,0) ) ,0),  " +
				"        nvl(sum( nvl(RETURNED_QUANTITY,0) * nvl(LOCAL_UNIT_COST,0) ) ,0)  " +
				" from po_purchase_order_line where HEAD_ID=" + headId + " and nvl(IS_DELETE_RECORD,'0')<>'1' ";
		
		log.info("111ddd ---- poSQL:"+tmpSqlLine);
		
		List lineList = nativeQueryDAO.executeNativeSql(tmpSqlLine);
		

		Object[] obj = (Object[]) lineList.get(0);
		log.info("show~~~listBudget"+((BigDecimal) obj[0]).doubleValue());
		poHead.setTotalForeignPurchaseAmount( ((BigDecimal) obj[0]).doubleValue() );
		poHead.setTotalLocalPurchaseAmount(   ((BigDecimal) obj[1]).doubleValue() );
		poHead.setTotalUnitPriceAmount( ((BigDecimal) obj[2]).doubleValue() );
		poHead.setTotalProductCounts( ((BigDecimal) obj[3]).doubleValue() );
		Double totalReceiptedAmount = ((BigDecimal) obj[4]).doubleValue();
		Double totalOutstandAmount = ((BigDecimal) obj[5]).doubleValue();
		Double totalReturnedAmount = ((BigDecimal) obj[6]).doubleValue();
		String totalRe = totalOutstandAmount.toString();
		log.info("show~~~totalOutstandAmount"+totalOutstandAmount);
		return totalRe;
	}
}
