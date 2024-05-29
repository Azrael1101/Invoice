package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException; 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import java.lang.ClassCastException;
import java.math.BigDecimal;
import javax.servlet.http.HttpServletRequest;
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
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuPaymentTerm;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.FiBudgetHead;
import tw.com.tm.erp.hbm.bean.FiBudgetLine;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemPriceView;
import tw.com.tm.erp.hbm.bean.ImLetterOfCreditHead;
import tw.com.tm.erp.hbm.bean.ImLetterOfCreditLine;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImOnHandId;
import tw.com.tm.erp.hbm.bean.ImReceiveExpense;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveInvoice;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.ImStorageItem;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
//for 儲位用
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.ImTransation;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.bean.PoVerificationSheet;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationItemDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
import tw.com.tm.erp.hbm.dao.CustomsProcessResponseDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceHeadDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceLineDAO;
import tw.com.tm.erp.hbm.dao.FiBudgetLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImLetterOfCreditHeadDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveItemDAO;
import tw.com.tm.erp.hbm.dao.ImTransationDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderLineDAO;
import tw.com.tm.erp.hbm.dao.PoVerificationSheetDAO;
import tw.com.tm.erp.hbm.dao.SiProgramLogDAO;
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
//for 儲位用
import tw.com.tm.erp.action.ImStorageAction;
import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.hbm.service.ImMovementService;
import java.net.URL;
//Steve 寄信
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.net.URLConnection;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.TreeMap;
import org.apache.pdfbox.pdmodel.PDPage;
import javax.imageio.ImageIO;

/**進貨單/進貨退回單 Service
 * @author MyEclipse Persistence Tools
 */
public class ImReceiveHeadMainService { 
	private static final Log log = LogFactory.getLog(ImReceiveHeadMainService.class);
	public static final String PROGRAM_ID = "IM_RECEIVE_HEAD";
	private static CustomsProcessResponseDAO customsProcessResponseDAO = new CustomsProcessResponseDAO();
	private ImReceiveItemMainService     imReceiveItemMainService;
	private ImReceiveExpenseMainService  imReceiveExpenseMainService;
	private ImItemService                imItemService;
	private ImItemCategoryService        imItemCategoryService;
	private BuOrderTypeService           buOrderTypeService;
	private BuBasicDataService           buBasicDataService;
	private BuCommonPhraseService        buCommonPhraseService;
	private BuBrandService               buBrandService;
	private BuLocationService            buLocationService;
	private ImLetterOfCreditService      imLetterOfCreditService;
	private FiInvoiceLineMainService     fiInvoiceLineMainService;
	private FiBudgetHeadService          fiBudgetHeadService;
	private FiBudgetLineService          fiBudgetLineService;
	private PoPurchaseOrderHeadMainService   poPurchaseOrderHeadMainService;
	private PoPurchaseOrderLineMainService   poPurchaseOrderLineMainService;
	private BuSupplierWithAddressViewService buSupplierWithAddressViewService;
	
	private ImReceiveHeadDAO             imReceiveHeadDAO;
	private ImReceiveItemDAO             imReceiveItemDAO;
	private CmDeclarationHeadDAO         cmDeclarationHeadDAO;
	private CmDeclarationItemDAO         cmDeclarationItemDAO;
	private CmDeclarationOnHandDAO       cmDeclarationOnHandDAO;
	private FiInvoiceHeadDAO             fiInvoiceHeadDAO;
	private FiInvoiceLineDAO             fiInvoiceLineDAO;
	private FiBudgetLineDAO              fiBudgetLineDAO;
	private ImOnHandDAO                  imOnHandDAO;
	private ImMovementHeadDAO            imMovementHeadDAO;
	private PoPurchaseOrderHeadDAO       poPurchaseOrderHeadDAO;
	private PoPurchaseOrderLineDAO       poPurchaseOrderLineDAO;
	private PoVerificationSheetDAO       poVerificationSheetDAO;
	private ImItemDAO                    imItemDAO;
	private ImTransationDAO              imTransationDAO;
	private ImWarehouseDAO               imWarehouseDAO;
	private ImItemCurrentPriceViewDAO    imItemCurrentPriceViewDAO;
	private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
	private ImLetterOfCreditHeadDAO      imLetterOfCreditHeadDAO;
	private BuBrandDAO                   buBrandDAO;
	private BuCommonPhraseLineDAO        buCommonPhraseLineDAO;
	private SiProgramLogDAO              siProgramLogDAO;
	private SiProgramLogAction           siProgramLogAction;
	private NativeQueryDAO               nativeQueryDAO;
	private ImItemPriceDAO				 imItemPriceDAO;
	//for 儲位用
	private ImStorageAction				 imStorageAction;
	private ImStorageService			 imStorageService;
	private BuEmployeeDAO buEmployeeDAO;  //進貨單寄信用 

	public static final String[] GRID_FIELD_NAMES = { 
	    "indexNo", "itemCode", "declarationItemCode", "itemCName", "quantity", 
	    "isConsignSale", "lastForeignUnitCost", "standardPurchaseCost", "foreignUnitPriceOri", "foreignUnitPrice",
	    "localUnitPrice", "foreignAmount", "localAmount", "unitPrice", "expenseApportionmentAmount", 
	    "invoiceNo", "poOrderNo", "poNo", "receiptQuantity", "acceptQuantity",             
	    "defectQuantity", "sampleQuantity", "shortQuantity", "diffQty", "barcodeCount",
	    "shippingMark", "weight", "originalDeclarationDate", "originalDeclarationNo", "originalDeclarationSeq",     
	    "lotNo", "lineId", "status", "isLockRecord", "isDeleteRecord",
	    "message"};

	public static final int[] GRID_FIELD_TYPES = {
	    AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, 
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, 
	    AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, 
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, 
	    AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DATE,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,  
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
	    AjaxUtils.FIELD_TYPE_STRING};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = {
    		"0", "", "", "", "0", 
    		"", "0", "0", "0", "0",
    		"0", "0", "0", "0", "0",  
    		"", "",  "",  "0", "0", 
    		"0", "0", "0", "0", "0",
    		"",  "0",  "",  "", "", 
    		"",  "0",  "", AjaxUtils.IS_LOCK_RECORD_FALSE,	AjaxUtils.IS_DELETE_RECORD_FALSE, 
    		"" };
	
	
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
			"orderTypeCode", "orderNo", "itemCategory", "supplierCode", "supplierName",
			"declarationNo", "warehouseInDate", "receiptDate", "status", "warehouseStatus", 
			"expenseStatus", "sourceOrderNo", "lastUpdateDate", "headId", };

	public static final int[] GRID_SEARCH_FIELD_TYPES = {
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,   AjaxUtils.FIELD_TYPE_DATE,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
			"", "", "", "", "", 
			"", "", "", "", "",
			"", "", "", "0" }; 
	
	public static final String[] GRID_FIELD_NAMES_EXPENSE = { 
			"indexNo",                    "supplierCode",              "reserve5",                  "supplierName",
			"expenseCode",                "foreignAmount",             "localAmount",               "taxAmount",
			"billDate",	              "reserve1",  		   "lineId",                    "status",
			"isLockRecord",               "isDeleteRecord",  	   "message" };

	public static final int[] GRID_FIELD_TYPES_EXPENSE = {
			AjaxUtils.FIELD_TYPE_LONG,    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
			AjaxUtils.FIELD_TYPE_STRING,  AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
			AjaxUtils.FIELD_TYPE_DATE,    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, 
			AjaxUtils.FIELD_TYPE_STRING,  AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING };

	public static final String[] GRID_FIELD_DEFAULT_VALUES_EXPENSE = {
			"0", "",  "",  "", 
			"", "0", "0", "0",
			"", "",  "0",  "", 
			AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "" };

	/** save and update
     * @param modifyObj
     * @return
     * @throws Exception
     */
    public String create(ImReceiveHead modifyObj) throws Exception {
	//log.info("create");
	if (null != modifyObj) {
	    //setDefaultBarCodeCount(modifyObj);	// 2009.11.12 arthur
	    if (modifyObj.getHeadId() == null) {
		return save(modifyObj);
	    } else {
		return update(modifyObj);
	    }
	}
	return "";
    }

	/** save
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String save(ImReceiveHead saveObj) throws Exception {
		//log.info("save");
		//doAllValidate(saveObj);	//2009.11.12 arthur
		//createFiInvoiceRelation(saveObj);
		countHeadTotalAmount(saveObj);
		saveObj.setLotNo( SystemConfig.LOT_NO );
		saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imReceiveHeadDAO.save(saveObj);
		//doVerification(saveObj);
		return MessageStatus.SUCCESS;
	}

	/**update
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String update(ImReceiveHead updateObj) throws Exception {
		//log.info("update");
		//doAllValidate(updateObj);	//2009.11.12 arthur
		//createFiInvoiceRelation(updateObj);
		countHeadTotalAmount(updateObj);
		updateObj.setLastUpdateDate(new Date());
		imReceiveHeadDAO.update(updateObj);
		//doVerification(updateObj);
		// remove item null head id
		imReceiveHeadDAO.deleteNullField(ImReceiveInvoice.TABLE_NAME, ImReceiveInvoice.HEAD_ID);
		imReceiveHeadDAO.deleteNullField(ImReceiveExpense.TABLE_NAME, ImReceiveExpense.HEAD_ID);
		return MessageStatus.SUCCESS;
	}

	
    /**save tmp	2009.10.23 update by arthur
     * @param saveObj
     * @return
     * @throws Exception
     */
	public String saveTmp(ImReceiveHead saveObj) throws FormException, Exception {
		//log.info("ImReceiveHead.k");
		String tmpOrderNo = AjaxUtils.getTmpOrderNo();
		saveObj.setLotNo( SystemConfig.LOT_NO );
		saveObj.setOrderNo(tmpOrderNo);
		saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imReceiveHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}
    
    
    // 取得 批號 2009.11.03 arthur
    public String getReceiveHeadLotNo( ImReceiveHead saveObj ) throws FormException, Exception {
	//log.info("getReceiveHeadLotNo");
	String lotNo      = null;
	BuBrand buBrand   = buBrandService.findById( saveObj.getBrandCode() );
	String branchCode = buBrand.getBranchCode();	// 是否為 T2
	// 設定批號,為了要跟FI相同,所以使用相同的ORDER TYPE,建立批號	// T1 && (IRF || IRL)
	if ( !StringUtils.hasText(saveObj.getLotNo()) && !"2".equals(branchCode) &&
	    (ImReceiveHead.IM_RECEIVE_LOCAL.equalsIgnoreCase(saveObj.getOrderTypeCode()) || 
	     ImReceiveHead.IM_RECEIVE_FOREIGN.equalsIgnoreCase(saveObj.getOrderTypeCode()) ) ){
	    lotNo = buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), FiInvoiceHead.FI_INVOICE_ORDER_TYPE );	
	} 
	if ( !StringUtils.hasText(saveObj.getLotNo()) && !StringUtils.hasText(lotNo)) {	// T2 && 進貨退回
	    lotNo = SystemConfig.LOT_NO;
	}
	return lotNo;
    }


    /** 計算所有的合計 Master , Detail , 費用分攤
     * @param countObj
     */
    public void countHeadTotalAmount(ImReceiveHead countObj) {
    	//log.info("countHeadTotalAmount ImReceiveHead=" + countObj);
		if (null == countObj)
		    return;
		Double exchangeRate  = countObj.getExchangeRate();
	
		// 統計 ImReceiveItem 
		Double totalForeignPurchaseAmount 	= new Double(0);
		Double totalLocalPurchaseAmount   	= new Double(0);
		Double totalQuantity				= new Double(0);
		List<ImReceiveItem> imReceiveItems = countObj.getImReceiveItems();
		for (ImReceiveItem imReceiveItem : imReceiveItems) {
			//重新計算單價售價等
		    countItemTotalAmount(countObj, imReceiveItem);	// 取得每一筆 item 價格＆計算
		    totalForeignPurchaseAmount += imReceiveItem.getForeignAmount();
		    totalLocalPurchaseAmount += imReceiveItem.getLocalAmount();
		    totalQuantity += imReceiveItem.getQuantity();
		}
		
		countObj.setTotalForeignPurchaseAmount( NumberUtils.round(totalForeignPurchaseAmount, 2) );
		countObj.setTotalLocalPurchaseAmount(   NumberUtils.round(totalForeignPurchaseAmount * exchangeRate, 2));
		
		if (StringUtils.hasText(countObj.getLcNo()) && !StringUtils.hasText(countObj.getLcNo1()) && countObj.getTotalForeignPurchaseAmount() > 0) {
		    countObj.setLcUseAmount(countObj.getTotalForeignPurchaseAmount());
		}
		
		// 統計 ImReceiveExpense 費用
		Double expenseLocalAmount = new Double(0);
		// 統計taxAmount 費用
		Double taxAmount = new Double(0);
		
		for (ImReceiveExpense expense : countObj.getImReceiveExpenses()) {
			if(!"1".equals(expense.getIsDeleteRecord())){
				expenseLocalAmount += NumberUtils.getDouble(expense.getLocalAmount());
				taxAmount +=  NumberUtils.getDouble(expense.getTaxAmount());
			}
		}
		
		Double exportCommissionLocalAmount = countObj.getTotalLocalPurchaseAmount() * (NumberUtils.getDouble(countObj.getExportCommissionRate())/100);
		Double totalLocalAccountsPayable   = countObj.getTotalLocalPurchaseAmount() + exportCommissionLocalAmount + NumberUtils.getDouble(countObj.getExpenseLocalAmount());
		taxAmount += totalLocalAccountsPayable*NumberUtils.getDouble(countObj.getTaxRate())/100;
		countObj.setTaxAmount(taxAmount);
		
		//費用攤提 全部的費用 * (Item Amount / All Item Amount)
	    Double surplusExpenseAmount = expenseLocalAmount;
	    for (ImReceiveItem imReceiveItem : imReceiveItems) {
			Double expenseApportionmentAmount = new Double(0);
			//如果有總金額就按照總金額，
			if(totalLocalPurchaseAmount > 0D) 					//分攤費用=(台幣合計/台幣合計總計*費用總計)
				expenseApportionmentAmount = NumberUtils.round(expenseLocalAmount * (imReceiveItem.getLocalAmount() / totalLocalPurchaseAmount), 2);
			else if(totalQuantity > 0D)
				expenseApportionmentAmount = NumberUtils.round(expenseLocalAmount * (imReceiveItem.getQuantity() / totalQuantity), 2);
			imReceiveItem.setExpenseApportionmentAmount(expenseApportionmentAmount);
			surplusExpenseAmount = surplusExpenseAmount - imReceiveItem.getExpenseApportionmentAmount();	// 累扣已攤提費用
	    }
	    if (surplusExpenseAmount != 0 && imReceiveItems.size() > 0) {	//剩餘零頭分攤到第一筆
			ImReceiveItem firstItem = imReceiveItems.get(0);
			firstItem.setExpenseApportionmentAmount(firstItem.getExpenseApportionmentAmount() + surplusExpenseAmount);
	    }
		countObj.setExpenseLocalAmount(expenseLocalAmount);
		
    }

	
    /** 計算 imReceiveItem 的合計
     * @param countObj
     */
    public void countItemTotalAmount(ImReceiveHead headObj, ImReceiveItem itemObj) {
	//log.info("countItemTotalAmount");
	if ((null == headObj) || (null == itemObj))
	    return;
	    
	if ((null == itemObj.getForeignUnitPrice()) && (null == headObj.getExchangeRate()))
	    return;
	
	if (null == itemObj.getUnitPrice() || itemObj.getUnitPrice() == 0) {
	    try {
	    	Double unitPrice = getOriginalUnitPriceDouble(headObj.getBrandCode(), headObj.getOrderTypeCode(), itemObj.getItemCode());
			itemObj.setUnitPrice(unitPrice);
	    } catch (FormException e) {
	    	e.printStackTrace();
	    }
	}
	
	// T2單價 小數位數六位
	//if("T2".equals(headObj.getBrandCode())){
		itemObj.setForeignUnitPrice(NumberUtils.round(itemObj.getForeignUnitPrice(), 6));
		itemObj.setLocalUnitPrice(NumberUtils.round(itemObj.getForeignUnitPrice() * headObj.getExchangeRate(), 6));
	//}else{
		//itemObj.setForeignUnitPrice(NumberUtils.round(itemObj.getForeignUnitPrice(), 3));
		//itemObj.setLocalUnitPrice(NumberUtils.round(itemObj.getForeignUnitPrice() * headObj.getExchangeRate(), 3));
	//}
	
	// 20080908 shan change foreign quantity from quantity field
	//小計 小數位數兩位
	//數量是驗收數量 + 調整數量
	Double quantity = 0D;
	//if("T2".equals(headObj.getBrandCode())){
		quantity = itemObj.getReceiptQuantity();// + itemObj.getDiffQty();
	//}else{
		//quantity = itemObj.getQuantity();// + itemObj.getDiffQty();
	//}
	itemObj.setForeignAmount( NumberUtils.round(quantity * itemObj.getForeignUnitPrice(), 2));
	itemObj.setLocalAmount( NumberUtils.round(itemObj.getForeignAmount() * headObj.getExchangeRate(), 2));
	
    }

    /** 計算 imReceiveItem 的合計
     * @param countObj
     */
    public void countItemTotalAmountForAjax(ImReceiveHead headObj, ImReceiveItem itemObj) {
	//log.info("countItemTotalAmountForAjax");
	if ((null == headObj) || (null == itemObj))
	{
	    return;
	}   
	if ((null == itemObj.getForeignUnitPrice()) && (null == headObj.getExchangeRate()))
	{
	    return;
	}  
	if (null == itemObj.getUnitPrice() || itemObj.getUnitPrice() == 0) {
	    try {
	    	Double unitPrice = getOriginalUnitPriceDouble(headObj.getBrandCode(), headObj.getOrderTypeCode(), itemObj.getItemCode());
			itemObj.setUnitPrice(unitPrice);//2016.04.19 Maco 進貨單鎖零售價
	    } catch (FormException e) {
	    	e.printStackTrace();
	    }
	}
	if(headObj.getOrderTypeCode().equals("EIP") || headObj.getOrderTypeCode().equals("EIF"))
	{
		if (OrderStatus.SAVE.equals(headObj.getStatus()) || OrderStatus.REJECT.equals(headObj.getStatus())) {
		    try {
		    	Double unitPrice = getOriginalUnitPriceDouble(headObj.getBrandCode(), headObj.getOrderTypeCode(), itemObj.getItemCode());
				itemObj.setUnitPrice(unitPrice);//2016.04.19 Maco 進貨單鎖零售價
		    } catch (FormException e) {
		    	e.printStackTrace();
		    }
		}
	}
	
	// 單價 小數位數六位
	itemObj.setForeignUnitPrice(NumberUtils.round(itemObj.getForeignUnitPrice(), 6));
	itemObj.setForeignUnitPriceOri(NumberUtils.round(itemObj.getForeignUnitPriceOri(), 6));
	itemObj.setLocalUnitPrice(NumberUtils.round(itemObj.getForeignUnitPrice() * headObj.getExchangeRate(), 6));
	// 20080908 shan change foreign quantity from quantity field
	//小計 小數位數兩位
	//數量是驗收數量 + 調整數量
	BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(headObj.getBrandCode(), headObj.getOrderTypeCode()));
    String typeCode = buOrderType.getTypeCode();
    if("RR".equals(typeCode)){
    	itemObj.setReceiptQuantity(itemObj.getReceiptQuantity()*(-1));
    	itemObj.setAcceptQuantity(itemObj.getAcceptQuantity()*(-1));
        itemObj.setQuantity(itemObj.getQuantity()*(-1));
    }
	Double quantity = 0D;
    //if("T2".equals(headObj.getBrandCode())){
		quantity = itemObj.getReceiptQuantity();// + itemObj.getDiffQty();
	//}else{
		//quantity = itemObj.getQuantity();// + itemObj.getDiffQty();
	//}
		
	Double b = 0.0050005;
		
    itemObj.setForeignAmount(Math.floor((itemObj.getForeignUnitPrice() * quantity + b)*100.0)/100.0);	
    //itemObj.setForeignAmount( NumberUtils.round(quantity * itemObj.getForeignUnitPrice(), 2));
	itemObj.setLocalAmount( NumberUtils.round(itemObj.getForeignAmount() * headObj.getExchangeRate(), 2));
	
    }
    
    /** 處理AJAX 供應商及相關資料 / 利用供應商代號設定 countryCode,currencyCode,exchangeRate
     * @param httpRequest
     * @return
     */
    public List<Properties> getAJAXFormDataBySupplier(Properties httpRequest)throws Exception {
	//log.info("getFormDataBySupplier");
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
	//log.info("getFormDataBySupplierCode supplierCode=" + supplierCode + ",organizationCode=" + organizationCode
				//+ ",brandCode=" + brandCode + ",orderDate=" + orderDate);

	BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(brandCode, supplierCode);
	if (null != buSWAV) {
	    pro.setProperty("SupplierCode",    AjaxUtils.getPropertiesValue(buSWAV.getSupplierCode(),    ""));
		pro.setProperty("SupplierName",    AjaxUtils.getPropertiesValue(buSWAV.getChineseName(),     ""));
		pro.setProperty("CountryCode",     AjaxUtils.getPropertiesValue(buSWAV.getCountryCode(),     ""));
		pro.setProperty("CurrencyCode",    AjaxUtils.getPropertiesValue(buSWAV.getCurrencyCode(),    ""));
		pro.setProperty("PaymentTermCode", AjaxUtils.getPropertiesValue(buSWAV.getPaymentTermCode(), ""));
		pro.setProperty("PriceTermCode",   AjaxUtils.getPropertiesValue(buSWAV.getPriceTermCode(),   ""));
		pro.setProperty("ContactPerson",   AjaxUtils.getPropertiesValue(buSWAV.getContractPerson(),  ""));
		pro.setProperty("TaxType",         AjaxUtils.getPropertiesValue(buSWAV.getTaxType(),         ""));
		pro.setProperty("TaxRate",         AjaxUtils.getPropertiesValue(getTaxRate(buSWAV.getTaxType()), "0"));
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


    
    /** 依照 CmDeclarationHead.headId 取回 declarationNo
     * @param httpRequest
     * @return
     */
    public List<Properties> getAJAXFormDataByDeclarationHeadId(Properties httpRequest)throws Exception {
	//log.info("getAJAXFormDataByDeclarationHeadId");
	List       re  = new ArrayList();
	Properties pro = new Properties();
	Long   headId    = NumberUtils.getLong(httpRequest.getProperty("headId"));

	List cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "headId", headId );
	if ((null != cmDeclarationHeads) && cmDeclarationHeads.size() > 0) {
		CmDeclarationHead cmDeclarationHead = (CmDeclarationHead) cmDeclarationHeads.get(0);
	    pro.setProperty("DeclarationNo", cmDeclarationHead.getDeclNo());
	}else{ 
	    pro.setProperty("DeclarationNo", "");
	}
	re.add(pro);
	return re;
    }
    
    
    /**利用幣別指定匯率
     * @param headObj
     */
    private String getExchangeRateByCurrencyCode(String organizationCode, String currencyCode) {
	//log.info("getExchangeRateByCurrencyCode organizationCode=" + organizationCode + ",currencyCode=" + currencyCode);
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
	//log.info("getExchangeRateByCurrencyCode");
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

    
    /** 指定稅率
     * @param headObj
     */
    public void setTaxRate(ImReceiveHead headObj) {
	//log.info("setTaxRate");
	if ((null != headObj) && (null != headObj.getOrderTypeCode()) && (null != headObj.getTaxType())) {
	    if (headObj.getOrderTypeCode().equalsIgnoreCase(PoPurchaseOrderHead.PURCHASE_ORDER_LOCAL) &&
		    headObj.getTaxType().equalsIgnoreCase(PoPurchaseOrderHead.PURCHASE_ORDER_TAX)) {
		headObj.setTaxRate(new Double(5d));
	    } else {
		headObj.setTaxRate(new Double(0));
	    }
	}
    }


    /** AJAX利用幣別指定匯率 call by JS
     * @param headObj
     */
    public List<Properties> getAJAXExchangeRateByCurrencyCode(Properties httpRequest) {
	//log.info("getAJAXExchangeRateByCurrencyCode");
	List re = new ArrayList();
	String currencyCode = httpRequest.getProperty("currencyCode");
	String organizationCode = httpRequest.getProperty("organizationCode");
	String orderDate = httpRequest.getProperty("orderDate");

	Properties pro = new Properties();
	String exchangeRate = "1";
	if (StringUtils.hasText(currencyCode) && StringUtils.hasText(organizationCode)) {
	    // 20090204 shan
	    Date od = DateUtils.parseDate("yyyy/MM/dd", orderDate);
	    exchangeRate = getExchangeRateByCurrencyCode(organizationCode, currencyCode, od);
	}
	pro.setProperty("ExchangeRate", AjaxUtils.getPropertiesValue(exchangeRate, "1"));
	re.add(pro);
	return re;
    }

	
    /** AJAX TaxRate Change call by JS
     * @param headObj(brandCode,orderTypeCode,itemCode)
     * @throws FormException
     */
    public List<Properties> getAJAXTaxRate(Properties httpRequest) throws FormException {
	//log.info("getAJAXTaxRate");
	List re = new ArrayList();
	String taxType = httpRequest.getProperty("taxType");
	if (StringUtils.hasText(taxType)) {
	    Properties pro = new Properties();
	    pro.setProperty("TaxRate", getTaxRate(taxType));
	    re.add(pro);
	}
	return re;
    }


    /** 取得稅率
     * @param headObj
     */
    private String getTaxRate(String taxType) {
	//log.info("getTaxRate");
	Double tmp = new Double(0);
	if (StringUtils.hasText(taxType)) {
	    if (PoPurchaseOrderHead.PURCHASE_ORDER_TAX.equalsIgnoreCase(taxType)) {	// 3
		tmp = new Double(5);
	    }
	}
	return tmp.toString();
    }
    
    
    /**AJAX Line Change
     * @param headObj(brandCode,orderTypeCode,itemCode)
     * @throws FormException
     */
    public List<Properties> getAJAXLineData(Properties httpRequest) throws FormException, Exception {
		//log.info("getAJAXLineData");
		List re = new ArrayList();
		try{
			String brandCode        = httpRequest.getProperty("brandCode");
			String orderTypeCode    = httpRequest.getProperty("orderTypeCode");
			String itemCode         = httpRequest.getProperty("itemCode");
			String typeCode         = httpRequest.getProperty("typeCode");	// 進貨 or 進貨退回
		
			if (StringUtils.hasText(brandCode) && StringUtils.hasText(orderTypeCode) && StringUtils.hasText(itemCode)) {
			    Properties pro = new Properties();
			    String orderType = "";
			    String orderNo   = "";
			    String lotNo     = SystemConfig.LOT_NO;
			    String declarationItemCode = "";
			    String lastForeignUnitCost = "0";
			    String standardPurchaseCost = "0";
			    Double foreignUnitPriceOri = 0D;
			    Double foreignUnitPrice = 0D;
			    Double localUnitPrice   = 0D;
			    ImItem imItem = imItemService.findItem(brandCode, itemCode);
			    String itemCName = imItem==null ? MessageStatus.DATA_NOT_FOUND : imItem.getItemCName(); 
			    String isConsignSale = imItem==null ? "" : imItem.getIsConsignSale(); 
			    Double foreignListPrice = imItem==null ? 0D : imItem.getForeignListPrice();//零售價(wade)
			    
			    if (null!=imItem){
			    	foreignUnitPriceOri = imItem.getPurchaseAmount();
			    }
			    
			    if("RR".equals(typeCode)) {	// 進貨退回
			    	standardPurchaseCost = AjaxUtils.getPropertiesValue(poPurchaseOrderLineMainService.getAverageUnitCost(itemCode, brandCode),"0");
			    	if("EOF".equals(orderTypeCode) || "EOP".equals(orderTypeCode))
			    		lastForeignUnitCost = AjaxUtils.getPropertiesValue(poPurchaseOrderLineMainService.getLastUnitCost(itemCode),"0");
			    	else{
			    		lastForeignUnitCost = standardPurchaseCost;
			    	}
					Long headId = 0L;
					if("EOF".equals(orderTypeCode)){
					    orderType = "EIF";
					}else if("EOP".equals(orderTypeCode)){
					    orderType = "EIP";
					}else if("RRF".equals(orderTypeCode)){
					    orderType = "IRF";
					}else if("RRL".equals(orderTypeCode)){
					    orderType = "IRL";
					}
					headId = imReceiveHeadDAO.findOrderNoByItemCode(brandCode, orderType, itemCode );
					if(null != headId){
						ImReceiveHead imReceiveHead = imReceiveHeadDAO.findById(headId);
						if( null == imReceiveHead ){
						    orderNo = "無完成 or 結案進貨單";
						}else{
						    if("EOP".equals(orderTypeCode)){
						    	orderNo = imReceiveHead.getOrderNo();
						    	lotNo   = imReceiveHead.getLotNo();
						    }
						    for (ImReceiveItem imReceiveItem : imReceiveHead.getImReceiveItems() ){
								if(itemCode.equals(imReceiveItem.getItemCode())){
									//log.info("itemCode = " + itemCode);
								    foreignUnitPrice    = null==imReceiveItem.getForeignUnitPrice() ? foreignUnitPriceOri : imReceiveItem.getForeignUnitPrice();
								    localUnitPrice      = null==imReceiveItem.getLocalUnitPrice()? 0D : imReceiveItem.getLocalUnitPrice();
								    declarationItemCode = null==imReceiveItem.getDeclarationItemCode()? itemCode : imReceiveItem.getDeclarationItemCode();
								}
						    }
						}
					}
			    }
			    
			    pro.setProperty("ItemCName", itemCName);
			    pro.setProperty("IsConsignSale", isConsignSale);
			    pro.setProperty("OrderNo",   orderNo);
			    pro.setProperty("LotNo",     lotNo);
			    pro.setProperty("DeclarationItemCode", declarationItemCode);
			    pro.setProperty("LastForeignUnitCost", AjaxUtils.getPropertiesValue(lastForeignUnitCost, "0"));
			    pro.setProperty("StandardPurchaseCost",AjaxUtils.getPropertiesValue(standardPurchaseCost, "0"));
			    pro.setProperty("ForeignUnitPriceOri", AjaxUtils.getPropertiesValue(foreignUnitPriceOri, "0"));
			    pro.setProperty("ForeignUnitPrice",    AjaxUtils.getPropertiesValue(foreignUnitPrice, "0"));
			    pro.setProperty("LocalUnitPrice",      AjaxUtils.getPropertiesValue(localUnitPrice, "0"));
			    pro.setProperty("unitPrice",      	   AjaxUtils.getPropertiesValue(foreignListPrice, "0"));//零售價(wade)
			    re.add(pro);
			}
	    }catch(Exception e){
	    		log.error(e.toString());
	    }
	return re;
    }

    
    /** 移除空白的 imReceiveItem
     * @param headObj
     * @return boolean 是否還有 Detail
     */
    public boolean removeDetailItemCode(ImReceiveHead headObj) {
    	log.info("removeDetailItemCode");
    	List<ImReceiveItem> items = headObj.getImReceiveItems();
    	for (int i = items.size()-1; i >= 0 ; i--) {
    		ImReceiveItem item = items.get(i);
    		if ("1".equals(item.getIsDeleteRecord()) ) {
    			items.remove(i);
    		}
		}
    	
    	long indexNo = 1L;
    	for (Iterator iterator = items.iterator(); iterator.hasNext();) {
    		ImReceiveItem imReceiveItem = (ImReceiveItem) iterator.next();
			imReceiveItem.setIndexNo(indexNo++);
		}
    	return items.isEmpty();
    }


    /** 移除空白的Detail
     * @param headObj
     * @return boolean 是否還有 Detail
     */
    private boolean removeReceiveExpenses(ImReceiveHead headObj) {
    	log.info("removeReceiveExpenses");
    	List<ImReceiveExpense> items = headObj.getImReceiveExpenses();
    	for (int i = items.size()-1; i >= 0 ; i--) {
    		ImReceiveExpense item = items.get(i);
    		if ("1".equals(item.getIsDeleteRecord()) ) {
    			items.remove(i);
    		}
		}
    	long indexNo = 1L;
    	for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			ImReceiveExpense imReceiveExpense = (ImReceiveExpense) iterator.next();
			imReceiveExpense.setIndexNo(indexNo++);
		}
    	return items.isEmpty();
    }


    /**更新 ImOnHand 數量
     * @param organizationCode
     * @param itemCode
     * @param lotNo
     * @param warehouseCode
     * @param inUncommitQty
     * @param lastUpdatedBy
     */
    private void modifyOnHandQty(String organizationCode, String itemCode, String lotNo, String warehouseCode, 
    		Double inUncommitQty, String lastUpdatedBy, String brandCode) throws Exception {
	log.info("modifyOnHandQty organizationCode=" + organizationCode + ",itemCode=" + itemCode + ",lotNo=" + lotNo +
		 ",warehouseCode=" + warehouseCode + ",inUncommitQty=" + inUncommitQty + ",lastUpdatedBy=" + lastUpdatedBy);
	ImOnHandId id = new ImOnHandId();
	id.setOrganizationCode(organizationCode);
	id.setItemCode(itemCode);
	id.setLotNo(lotNo); // 設定批號
	id.setWarehouseCode(warehouseCode);
	ImOnHand imOnHand = imOnHandDAO.findByIdentification(organizationCode, brandCode, itemCode, warehouseCode, lotNo);

	if(inUncommitQty<0 && null==imOnHand){
	    throw new Exception("imOnHand:"+ brandCode +"-"+ warehouseCode +"-"+ itemCode +"-"+ lotNo + " 無庫存數量可異動" );
	}
	
	    if (null != imOnHand) { // update
			inUncommitQty = imOnHand.getInUncommitQty() + inUncommitQty;
			imOnHand.setInUncommitQty(inUncommitQty);
			imOnHand.setLastUpdateDate(new Date());
    	    imOnHand.setLastUpdatedBy(lastUpdatedBy);
    	    imOnHandDAO.update(imOnHand);
	    } else { // insert
			imOnHand = new ImOnHand();
			imOnHand.setId(id);
	    	imOnHand.setBrandCode(brandCode);
	    	imOnHand.setStockOnHandQty(0D);
	    	imOnHand.setOutUncommitQty(0D);
	    	imOnHand.setMoveUncommitQty(0D);
	    	imOnHand.setOtherUncommitQty(0D);
	    	imOnHand.setInUncommitQty(inUncommitQty);
	    	imOnHand.setCreationDate(new Date());
	    	imOnHand.setCreatedBy(lastUpdatedBy);
	    	imOnHand.setLastUpdateDate(new Date());
	    	imOnHand.setLastUpdatedBy(lastUpdatedBy);
	    	imOnHand.setBrandCode(brandCode);
	    	imOnHandDAO.save(imOnHand);
	    }

    }

    
    /**依進貨數量 imReceiveItem.quantity update poPurchaseOrderLine.receivedQuantity 
     * @param imReceiveHead
     * @throws FormException
     * @throws Exception
     */
    public void updateImReceiveItem2PoLine( ImReceiveHead imReceiveHead, boolean isReject, String loginUser, String userType ) throws Exception {
    	log.info("updateImReceiveItem2PoLine");
		List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
		Integer cnt = 0;
		for (ImReceiveItem imReceiveItem : imReceiveItems) {
			Double qty = 0D;
			//如果是船務送出或是倉管駁回
			if(isReject){
				if(imReceiveHead.getOrderTypeCode().equals("EIP") && imReceiveHead.getStatus().equals(OrderStatus.FINISH) || imReceiveHead.getOrderTypeCode().equals("EIP") && imReceiveHead.getStatus().equals(OrderStatus.CLOSE)){
					qty = imReceiveItem.getShortQuantity() > 0 ? imReceiveItem.getShortQuantity()+imReceiveItem.getQuantity()*(-1): imReceiveItem.getReceiptQuantity()*(-1) ;
				}else{
					qty = NumberUtils.getDouble(imReceiveItem.getQuantity()) * (-1);
				}				
			}else if("SHIPPING".equals(userType)){
				qty = NumberUtils.getDouble(imReceiveItem.getQuantity());
			}else if("WAREHOUSE".equals(userType) && !"EIF".equals(imReceiveHead.getOrderTypeCode())){  //如果是倉管驗貨
				qty = NumberUtils.getDouble(imReceiveItem.getShortQuantity()) > 0 ? imReceiveItem.getShortQuantity()*(-1): 0 ;
			}				
			if(isReject){
				// 刪除核銷單
				List<PoVerificationSheet> poVerificationSheets = 
					poVerificationSheetDAO.findByProperty("PoVerificationSheet", "imReceiveLineId", imReceiveItem.getLineId() );
				if(null!=poVerificationSheets && poVerificationSheets.size()>0){
					for(PoVerificationSheet poVerificationSheet : poVerificationSheets ){
						poVerificationSheetDAO.delete(poVerificationSheet);
					}
				}
			}
			log.info("itemCode = " + imReceiveItem.getItemCode() + " & qty = " + qty );
		    // 取得可以核銷的 PO單
			if(qty != 0){
			    List<PoPurchaseOrderHead> poHeads = null;
			    //依照單別尋找採購單
			    if("IRF".equals(imReceiveHead.getOrderTypeCode())){	// IRF, EIF	
					HashMap findObj = new HashMap();
					findObj.put("invoiceNo", imReceiveItem.getInvoiceNo());
					findObj.put("itemCode",  imReceiveItem.getItemCode());
					findObj.put("brandCode", imReceiveHead.getBrandCode());
					findObj.put("status", OrderStatus.FINISH);
					poHeads = poPurchaseOrderHeadDAO.getVerificationPOList(findObj);
			    }else if("EIF".equals(imReceiveHead.getOrderTypeCode())){
			    	//依照 Invoice 設定沖銷PO	2010.02.01
			    }else if("EIP".equals(imReceiveHead.getOrderTypeCode())){					// IRP, EIP
			    	poHeads = poPurchaseOrderHeadDAO.findAllNoCloseHead( OrderStatus.FINISH, imReceiveHead.getBrandCode(), 
			    				imReceiveItem.getPoOrderNo(), "EPP");
			    }else{// IRL
			    	poHeads = poPurchaseOrderHeadDAO.findAllNoCloseHead( OrderStatus.FINISH, imReceiveHead.getBrandCode(), 
			    				imReceiveItem.getPoOrderNo(), PoPurchaseOrderHead.PURCHASE_ORDER_LOCAL);
			    }
			    
			    //保稅進貨單依照 Invoice 設定沖銷PO	2010.02.01
			    if("EIF".equals(imReceiveHead.getOrderTypeCode())){	
			    	List fiInvoiceLines = fiInvoiceLineMainService.findBydDclarationNo(imReceiveHead.getDeclarationNo());
					Object[] obj = (Object[]) fiInvoiceLines.get(cnt);
					HashMap findObj = new HashMap();
					findObj.put("headId",    (Long)obj[1]);	//fiInvoiceHead.headId
					findObj.put("customSeq", (Long)obj[2]);	//fiInvoiceLine.customSeq
					List<FiInvoiceLine> fiLines = fiInvoiceLineMainService.find(findObj);	// 取出相同指定順序的PO
					for(FiInvoiceLine fiLine : fiLines ){
					    PoPurchaseOrderHead poHead = poPurchaseOrderHeadMainService.findById( fiLine.getPoPurchaseOrderHeadId() );
					    PoPurchaseOrderLine poLine = poPurchaseOrderLineMainService.findById( fiLine.getPoPurchaseOrderLineId() );
					    //找不到就不回去核銷了
					    if(null != poHead && null != poLine){
					    	if(fiLines.size()>1){
					    		//數量按照發票的數量
					    		log.info("回寫數量回採購單");
					    		if(!isReject)
					    			qty = fiLine.getQuantity();
					    		else
					    			qty = fiLine.getQuantity()*(-1);
						    	updateImReceiveQty2PoLine( imReceiveHead, imReceiveItem, poHead, poLine, qty, loginUser, isReject);
						    }else{
						    	updateImReceiveQty2PoLine( imReceiveHead, imReceiveItem, poHead, poLine, qty, loginUser, isReject);
						    }
						}else{
							throw new Exception ("商品: " + imReceiveItem.getItemCode() + " 查無可對應核銷之採購單");
						}
					}
				//國內外 完稅進貨單 核銷
			    }else{
			    	if(null == poHeads || poHeads.size() == 0)
				    	throw new Exception ("商品: " + imReceiveItem.getItemCode() + " 查無可對應核銷之採購單頭");
			    	for (int index = 0; index < poHeads.size(); index++) {
					    PoPurchaseOrderHead poHead = poHeads.get(index);// 核銷數量到 PoPurchaseOrderLine
					    List<PoPurchaseOrderLine> poLines = 
						poPurchaseOrderLineMainService.findByItemCode(poHead.getHeadId(), imReceiveItem.getItemCode(), imReceiveItem.getReserve3());//採購欄位回寫增poLineId-Jerome
					    if(null == poLines || poLines.size() == 0)
					    	throw new Exception ("商品: + " + imReceiveItem.getItemCode() + " 查無可對應核銷之採購單身");
					    for (PoPurchaseOrderLine poLine : poLines) {
							Double remainder = 0D;				//未核銷數量
							if (index == (poHeads.size() - 1)) { 		//處理最後一筆, 全部核銷
							    remainder = qty;
							    qty       = new Double(0);
							} else {
								 //如果是扣採購單
						    	if(qty > 0){
						    		remainder = poLine.getActualPurchaseQuantity() - poLine.getReceiptedQuantity();//實際應到量-已進貨數量
						    		if(remainder < 0){
						    			remainder = 0D;
						    		}else if (qty - remainder > 0) {			// 進貨量 >= 未到貨量
									    qty -= remainder;
									}else{		// 進貨量  < 未到貨量
									    remainder = qty;
									    qty       = new Double(0);
								    }
								//回加採購單
						    	}else if (qty < 0){
						    		remainder =  poLine.getReceiptedQuantity()*(-1);//已進貨數量
						    		if(qty - remainder < 0){
						    			qty -= remainder;
						    		}else{
						    			remainder = qty;
									    qty       = new Double(0);
						    		}
						    	}
							}
							if(0 != remainder)
								updateImReceiveQty2PoLine( imReceiveHead, imReceiveItem, poHead, poLine, remainder, loginUser, isReject);
						}
					}
			    }
			}
		    cnt++;
		}
    }
    
    
    /**依進貨數量 imReceiveItem.quantity update poPurchaseOrderLine.receivedQuantity 
     * @param imReceiveHead
     * @throws FormException
     * @throws Exception
     */
    public void updateImReceiveQty2PoLine( ImReceiveHead imHead, ImReceiveItem imItem,
	    				   PoPurchaseOrderHead poHead,    PoPurchaseOrderLine poLine, Double quantity, 
	    				   String loginUser, boolean isReject ) throws Exception {
	    poLine.setReceiptedQuantity( NumberUtils.getDouble(poLine.getReceiptedQuantity()) + quantity );	// 異動 poPurchaseOrderLine.receivedQuantity;
	    poLine.setLastUpdateDate(new Date());
	    poPurchaseOrderLineDAO.update(poLine);
	    if(!isReject && quantity != 0){
		    // 註記PO核銷單
		    if("IRF".equals(imHead.getOrderTypeCode()) || "EIF".equals(imHead.getOrderTypeCode())){	// IRF, EIF
		    	insertVerificationSheet(imItem.getLineId(), imHead.getOrderNo(),   imHead.getOrderTypeCode(), 
			    		    	imHead.getLotNo(),  imItem.getInvoiceNo(), imItem.getItemCode(),
			    		    	poLine.getLineId(), poHead.getOrderNo(),   poHead.getOrderTypeCode(), 
			    		    	quantity,           poHead.getBrandCode(),poLine.getQuantity(),poLine.getLocalUnitCost(),imHead.getCreatedBy(),
			    		    	poHead.getBudgetYear());
		    }else{
		    	insertVerificationSheet(imItem.getLineId(), imHead.getOrderNo(), imHead.getOrderTypeCode(), 
						imHead.getLotNo(),  imHead.getGuiNo(),   imItem.getItemCode(),
						poLine.getLineId(), poHead.getOrderNo(), poHead.getOrderTypeCode(),
						quantity,           poHead.getBrandCode(),poLine.getQuantity(),poLine.getLocalUnitCost(),imHead.getCreatedBy(),
						poHead.getBudgetYear());
		    }
	    }
    }
    
    
    /** 依照 PO 資料累計 FiBudgetLine.receiveActualAmount
     * @param poHead
     * @param imReceiveItem
     * @param remainder
     * @param branchCode
     * @throws Exception
     */
    public void updateImReceive2FiBueget(ImReceiveHead head, boolean isReject, String userType) throws Exception {
		log.info("updateImReceive2FiBueget");
    	try{
    		Long itemBrandCode = 0L;
    		String poOrderTypeCode = "";
    		String poOrderNo = "";
    		PoPurchaseOrderHead poPurchaseOrderHead = null;
    		List <ImReceiveItem> imReceiveItems = null;
    		if(head.getOrderTypeCode().equals("EIF")){
    		//String category01 = "";    		
    		FiInvoiceHead fiInvoiceHead = fiInvoiceHeadDAO.findPoInvoice(head.getBrandCode(),head.getDefPoOrderNo());
    		List<FiInvoiceLine> fiInvoiceLines = fiInvoiceHead.getFiInvoiceLines();	
    			for (FiInvoiceLine fiInvoiceLine : fiInvoiceLines) {
    				poOrderTypeCode = fiInvoiceLine.getSourceOrderTypeCode();
    				poOrderNo = fiInvoiceLine.getSourceOrderNo();    				
				}
    		log.info(poOrderTypeCode+"___"+poOrderNo);	
    			poPurchaseOrderHead = poPurchaseOrderHeadDAO.findPoInvoice(head.getBrandCode(),poOrderTypeCode,poOrderNo);
    		log.info("findInvoiceFinish"+poPurchaseOrderHead.getHeadId());
    		itemBrandCode = poPurchaseOrderHead.getBudgetLineId();
    		log.info("findItemBrandCode"+itemBrandCode+"___EIF");
    		}else if(head.getOrderTypeCode().equals("EIP")){
    			imReceiveItems = head.getImReceiveItems();
    			for (ImReceiveItem imReceiveItem : imReceiveItems) {
    				//poOrderTypeCode = imReceiveItem.getPoOrderType();
    				poOrderNo = imReceiveItem.getPoOrderNo();
				}
    			log.info("EIP"+poOrderTypeCode+"___"+poOrderNo);
    			poPurchaseOrderHead = poPurchaseOrderHeadDAO.findPoInvoice(head.getBrandCode(),head.getDefPoOrderType(),poOrderNo);
    			log.info("findInvoiceFinish"+poPurchaseOrderHead.getHeadId());
    			itemBrandCode = poPurchaseOrderHead.getBudgetLineId();
        		log.info("findItemBrandCode"+itemBrandCode+"___EIP");
    		}else if(head.getOrderTypeCode().equals("IRF")||head.getOrderTypeCode().equals("IRL")){
    			log.info("....百貨無itemBrandCode");
    			itemBrandCode = null;
    		}    		    			
    		String budgetType = "";
    		String quantityType = "RECEIPT_QUANTITY";
    	    budgetType = "LOCAL_UNIT_PRICE";
    		String tmpSqlItem = " select  c.CATEGORY_CODE,  sum(nvl(l."+budgetType+",0) * nvl(l."+quantityType+",0)) as total" +
    							" from IM_RECEIVE_ITEM l , IM_ITEM i, IM_ITEM_CATEGORY c " +
    							" where  l.head_id = " + head.getHeadId() + " and i.brand_code = '"+head.getBrandCode()+ "'" +
    							" and c.brand_code = '"+head.getBrandCode()+"' and c.CATEGORY_TYPE = 'ITEM_CATEGORY' " + 
    							" and l.item_code = i.item_code and i.ITEM_CATEGORY = c.CATEGORY_CODE " + 
    							" group by c.CATEGORY_CODE" ;
    		List itemList = nativeQueryDAO.executeNativeSql(tmpSqlItem);
    		//如果一個進貨單超過一個業種，就會跑迴圈
    		for (Iterator iterator = itemList.iterator(); iterator.hasNext();) {
				Object[] obj = (Object[])iterator.next();
				Double receiveAmount = NumberUtils.round(((BigDecimal)obj[1]).doubleValue(),2);
				//只要倉管送出都是扣
				HashMap findObjs = new HashMap();
				findObjs.put("brandCode",     head.getBrandCode());
				findObjs.put("categoryType",  (String)obj[0]);
				log.info("categoryType----"+(String)obj[0]);
				findObjs.put("budgetYear",    head.getBudgetYear());
				List<FiBudgetHead> budgets = fiBudgetHeadService.find(findObjs);//-------進貨找預算要改寫
				if(null == budgets || budgets.size() == 0)
					throw new Exception("預算單查無年度: " + head.getBudgetYear() + " 之預算建立");
				else{
					FiBudgetHead fiBudgetHead = budgets.get(0);
					log.info("進貨找預算投"+fiBudgetHead.getHeadId());
					List<FiBudgetLine> fiBudgetLines = fiBudgetHead.getFiBudgetLines();
					if(null == fiBudgetLines || fiBudgetLines.size() == 0)
						throw new Exception("預算單查無年度: " + head.getBudgetYear() + " 之預算細項建立");
					else{
						FiBudgetLine fiBudgetLine = null;
						if(null != itemBrandCode){
							/*FiBudgetLine*/ fiBudgetLine = fiBudgetLineDAO.findByLine(itemBrandCode);
						}else{
							/*FiBudgetLine*/ fiBudgetLine = fiBudgetLines.get(0);
						}
						//FiBudgetLine fiBudgetLine = fiBudgetLineDAO.findByLine(itemBrandCode);						
						if(isReject)
							fiBudgetLine.setReceiveActualAmount(NumberUtils.getDouble(fiBudgetLine.getReceiveActualAmount()) - receiveAmount);
						else
							fiBudgetLine.setReceiveActualAmount(NumberUtils.getDouble(fiBudgetLine.getReceiveActualAmount()) + receiveAmount);
						fiBudgetLineDAO.update(fiBudgetLine);
						
					}
				}
    			
			}
    	}catch (Exception ex) {
    	    log.error("進貨單扣除預算作業失敗，原因：" + ex.toString());
    	    throw new Exception("進貨單扣除預算作業失敗，原因：" + ex.getMessage());
	}
    }
    
	
    /** 寄送MAIL給PO單負責人
     * @param modifyObj
     */
    private void doAfterVerification(ImReceiveHead modifyObj) {
	log.info("doAfterVerification");
	sendMailToSuperintendent(modifyObj);
    }

    
    /** 寄送MAIL給採購人員 */
    public void sendMailToSuperintendent(ImReceiveHead imReceiveHead) {
	log.info("sendMailToSuperintendent HeadId=" + imReceiveHead.getHeadId());
	String brandCode = imReceiveHead.getBrandCode();
	if (ImReceiveHead.IM_RECEIVE_LOCAL.equalsIgnoreCase(imReceiveHead.getOrderTypeCode())) {
	    List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
	    for (ImReceiveItem imReceiveItem : imReceiveItems) {
		String orderNo = imReceiveItem.getPoOrderNo();
		try {
		    sendMailToSuperintendent(brandCode, PoPurchaseOrderHead.PURCHASE_ORDER_LOCAL, orderNo);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    }
	} else if (ImReceiveHead.IM_RECEIVE_FOREIGN.equalsIgnoreCase(imReceiveHead.getOrderTypeCode())) {
	    List<ImReceiveInvoice> imReceiveInvoices = imReceiveHead.getImReceiveInvoices();
	    if (null != imReceiveInvoices) {
		for (ImReceiveInvoice imReceiveInvoice : imReceiveInvoices) {
		    FiInvoiceHead fiInvoiceHead = imReceiveInvoice.getFiInvoiceHead();
		    List<FiInvoiceLine> fiInvoiceLines = fiInvoiceHead.getFiInvoiceLines();
		    for (FiInvoiceLine fiInvoiceLine : fiInvoiceLines) {
			Long orderHeadId = fiInvoiceLine.getPoPurchaseOrderHeadId();
			PoPurchaseOrderHead poPurchaseOrderHead = 
			    (PoPurchaseOrderHead) poPurchaseOrderHeadDAO.findByPrimaryKey(PoPurchaseOrderHead.class, orderHeadId);
			if (null != poPurchaseOrderHead) {
			    try {
				sendMailToSuperintendent(brandCode, PoPurchaseOrderHead.PURCHASE_ORDER_LOCAL, poPurchaseOrderHead.getOrderNo());
			    } catch (Exception ex) {
				ex.printStackTrace();
			    }
			}
		    }
		}
	    }
	}
    }

    
    /** send mail to po superintendent
     * @param brandCode
     * @param orderType
     * @param orderNo
     * @throws FormException
     */
    private void sendMailToSuperintendent(String brandCode, String orderType, String orderNo) throws FormException {
	log.info("sendMailToSuperintendent orderNo=" + orderNo + ",orderType=" + orderType + ",brandCode=" + brandCode);
	HashMap findMap = new HashMap();
	// findMap.put("status", OrderStatus.FINISH);
	findMap.put("startOrderNo", orderNo);
	findMap.put("endOrderNo", orderNo);
	findMap.put("brandCode", brandCode);
	findMap.put("orderTypeCode", orderType);
	List<PoPurchaseOrderHead> poPurchaseOrderHeads = poPurchaseOrderHeadDAO.find(findMap);
	if (poPurchaseOrderHeads.size() > 0) {
	    PoPurchaseOrderHead poPurchaseOrderHead = poPurchaseOrderHeads.get(0);
	    Long headId = poPurchaseOrderHead.getHeadId();
	    String superintendentCode = poPurchaseOrderHead.getSuperintendentCode();
	    HashMap conditionMap = new HashMap();
	    conditionMap.put("brandCode", brandCode);
	    conditionMap.put("employeeCode", superintendentCode);
	    BuEmployeeWithAddressView buEmployeeWithAddressView =
		buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode,superintendentCode);
	    log.info("sendMailToSuperintendent brandCode=" + brandCode + ", superintendentCode=" + superintendentCode);
	    if (null != buEmployeeWithAddressView) {
		String eMail = buEmployeeWithAddressView.getEMail();
		log.info("sendMailToSuperintendent eMail=" + eMail);
		if (null != eMail) {
		    StringBuffer subject = new StringBuffer("採購單" + orderNo + "商品已經進貨");
		    StringBuffer content = new StringBuffer();
		    StringBuffer link = new StringBuffer();
		    link.append("<a href=");
		    if (SystemConfig.isProductionSystemMode()) {
			link.append(SystemConfig.getSystemConfigPro("URLRoot"));
		    } else {
			link.append(SystemConfig.getSystemConfigPro("LocalURLRoot"));
		    }
		    link.append("Po_PurchaseOrder:create:1.page");
		    link.append("? formId=");
		    link.append(headId);
		    link.append(">");
		    link.append(orderNo);
		    link.append("</a>");
		    content.append(link.toString());
		    try {
			MailUtils.sendMail(subject.toString(), content.toString(), eMail);
		    } catch (Exception ex) {
			ex.printStackTrace();
		    }
		}
	    }
	} else {
	    log.error("查無寄送MAIL之採購單單號 BrandCode=" + brandCode + ",orderTypeCode" + orderType + ",OrderNo=" + orderNo);
	    // throw new FormException("查無寄送MAIL之採購單單號 " + orderNo);
	}
    }

    
    /** insert verification sheet 產生核銷單
     * @param imReceiveLineId
     * @param imReceiveOrderNo
     * @param imReceiveOrderType
     * @param inoviceLotNo
     * @param invoiceNo
     * @param itemCode
     * @param poOrderLineId
     * @param poOrderNo
     * @param poOrderType
     * @param quantity
     */
    private void insertVerificationSheet(Long   imReceiveLineId, String imReceiveOrderNo, String imReceiveOrderType,
	    				 String inoviceLotNo,    String invoiceNo,        String itemCode,
	    				 Long   poOrderLineId,   String poOrderNo,        String poOrderType,
	    				 Double quantity,         String brandCode,Double poQuantity,Double poUnitCost,String createBy,String budgetYear) throws Exception{
	log.info("insertVerificationSheet imReceiveLineId=" + imReceiveLineId + ",imReceiveOrderNo=" + imReceiveOrderNo + 
		 ",imReceiveOrderType=" + imReceiveOrderType + ",inoviceLotNo=" + inoviceLotNo + ",invoiceNo=" + invoiceNo + 
		 ",itemCode=" + itemCode + ",poOrderLineId=" + poOrderLineId + ",poOrderNo=" + poOrderNo + 
		 ",poOrderType=" + poOrderType + ",quantity=" + quantity);
		//用進貨的lineId以及po的lineId去找那筆資料
	try{
		PoVerificationSheet verificationSheet = new PoVerificationSheet();
		if(quantity > 0){
			verificationSheet = new PoVerificationSheet();
			verificationSheet.setImReceiveLineId(imReceiveLineId);
			verificationSheet.setImReceiveOrderNo(imReceiveOrderNo);
			verificationSheet.setImReceiveOrderType(imReceiveOrderType);
			verificationSheet.setInoviceLotNo(inoviceLotNo);
			verificationSheet.setInvoiceNo(invoiceNo);
			verificationSheet.setItemCode(itemCode);
			verificationSheet.setPoOrderLineId(poOrderLineId);
			verificationSheet.setPoOrderNo(poOrderNo);
			verificationSheet.setPoOrderType(poOrderType);
			verificationSheet.setQuantity(quantity);
			verificationSheet.setStatus("Y");
			verificationSheet.setBrandCode(brandCode);
			verificationSheet.setPoQuantity(poQuantity);
			verificationSheet.setPoUnitCost(poUnitCost);
			verificationSheet.setCreatedBy(createBy);
			verificationSheet.setCreationDate(new Date());
			verificationSheet.setBudgetYear(budgetYear);
			poVerificationSheetDAO.save(verificationSheet);
		}else if(quantity < 0){
			List<PoVerificationSheet> verificationSheets = poVerificationSheetDAO.findByProperty("PoVerificationSheet", 
					new String[]{"imReceiveLineId", "poOrderLineId"}, new Object[]{imReceiveLineId, poOrderLineId});
			if(null == verificationSheets || verificationSheets.size() == 0){
				throw new Exception("查無此單據");
			}else{
				verificationSheet = verificationSheets.get(0);
				verificationSheet.setQuantity(verificationSheet.getQuantity() + quantity);
				poVerificationSheetDAO.update(verificationSheet);
			}
		}
	}catch(Exception e){
		throw new Exception("採購歷史歷程存檔發生錯誤， " + "imReceiveLineId=" + imReceiveLineId + ",imReceiveOrderNo=" + imReceiveOrderNo + 
				 ",imReceiveOrderType=" + imReceiveOrderType + ",inoviceLotNo=" + inoviceLotNo + ",invoiceNo=" + invoiceNo + 
				 ",itemCode=" + itemCode + ",poOrderLineId=" + poOrderLineId + ",poOrderNo=" + poOrderNo + 
				 ",poOrderType=" + poOrderType + ",quantity=" + quantity + " " + e.getMessage());
	}
    }

    
    /** close order
     * @param imReceiveHead
     */
    public void closeOrder(ImReceiveHead imReceiveHead) {
	log.info("closeOrder");
	imReceiveHead.setStatus(OrderStatus.CLOSE);
    }


    /** 建立條碼 */
    public void createBarCode(String orderTypeCode, String brandCode, String imReceiveOrderNo) {
	HashMap findObjs = new HashMap();
	findObjs.put("orderTypeCode", orderTypeCode);
	findObjs.put("brandCode", brandCode);
	findObjs.put("startOrderNo", imReceiveOrderNo);
	findObjs.put("endOrderNo", imReceiveOrderNo);
	List<ImReceiveHead> imReceiveHeads = imReceiveHeadDAO.find(findObjs);
	for (ImReceiveHead imReceiveHead : imReceiveHeads) {
	    List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
	    for (ImReceiveItem imReceiveItem : imReceiveItems) {
		String itemCode = imReceiveItem.getItemCode();
		ImItem item = imItemDAO.findById(itemCode);
		List<ImItemPrice> imItemPrices = item.getImItemPrices();
		if (imItemPrices.size() > 0) {
		    ImItemPrice imItemPrice = imItemPrices.get(0);
		    if (null != imItemPrice) {
			ImItemCurrentPriceView itempricevies = 
			    	imItemCurrentPriceViewDAO.findCurrentPriceByValue(item.getBrandCode(), item.getItemCode(), imItemPrice.getTypeCode());
			itempricevies.getItemCode();
			itempricevies.getBrandCode();
			itempricevies.getItemCName();
		    }
		}
	    }
	}
    }
    

    /** 條碼匯出資料
     * @param headId
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws FormException
     */
    public List getExportBarCode(Long headId, String brandCode, String orderTypeCode) 
    		throws IllegalAccessException, InvocationTargetException, FormException {
	List<ImReceiveItem> re = new ArrayList();
	if (null != headId) {
	    List<ImReceiveItem> imReceiveItems = imReceiveHeadDAO.getItemsByOrder(headId);
	    // List<ImReceiveItem> imReceiveItems = head.getImReceiveItems();
	    for (ImReceiveItem imReceiveItem : imReceiveItems) {
		ImReceiveItem dest = new ImReceiveItem();
		BeanUtils.copyProperties(dest, imReceiveItem);
		String itemCode = imReceiveItem.getItemCode();
		Double localUnitPrice = getOriginalUnitPriceDouble(brandCode, orderTypeCode, itemCode);
		dest.setUnitPrice(localUnitPrice);
		re.add(dest);
	    }
	}
	return re;
    }
    

    /** 取得商品的原始售價
     * @param brandCode
     * @param orderTypeCode
     * @param itemCode
     * @return
     * @throws FormException
     */
    public Double getOriginalUnitPriceDouble(String brandCode, String orderTypeCode, String itemCode) throws FormException {
	log.info("getOriginalUnitPriceDouble BrandCode=" + brandCode + ",OrderTypeCode=" + orderTypeCode);
	Double re = 0D;
	BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(brandCode, orderTypeCode));
	if (null != buOrderType && StringUtils.hasText(orderTypeCode) && StringUtils.hasText(itemCode)) {
	    String priceType = buOrderType.getPriceType();
	    log.info("priceType=" + buOrderType.getPriceType());
	    if (null != priceType) {
		log.info("BrandCode=" + brandCode + ",ItemCode=" + itemCode + ",priceType=" + priceType);
		ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findCurrentPrice(brandCode, itemCode, priceType);
		if (null != imItemCurrentPriceView) {
		    re = imItemCurrentPriceView.getUnitPrice().doubleValue();
		}
	    }
	} else {
	    throw new FormException("取得商品的原始售價資料有誤請確認  品牌 :" + brandCode + ",單別 :" + orderTypeCode + ",品號:" + itemCode);
	}
	return re;
    }

    
    /** search
     * @param findObjs
     * @return
     */
    public List<ImReceiveHead> find(HashMap findObjs) {
	log.info("find");
	return imReceiveHeadDAO.find(findObjs);
    }

    public List<ImReceiveHead> findReceiveByProperty(String brandCode, String status) {
	log.info("findReceiveByProperty");
	return imReceiveHeadDAO.findReceiveByProperty(brandCode, status);
    }

    public List<ImReceiveHead> findReceiveByProperty(String brandCode, String status, String orderType, String orderTypeCode, Date startDate, Date endDate) {
    	log.info("findReceiveByProperty");
    	return imReceiveHeadDAO.findReceiveByProperty(brandCode, status,orderType, orderTypeCode, startDate, endDate);
    }
    
    public void deleteTmpOrder() {
	log.info("deleteTmpOrder");
	imReceiveHeadDAO.removeTmpOrder();
    }

    
    /** 日結處理程序(Anber)
     * @param ImReceiveHead
     * @param opUser
     * @throws FormException
     * @throws NoSuchDataException
     */
    public void executeDailyBalance(ImReceiveHead imReceiveHead, String opUser) throws FormException {
    
    //日結前先把金額重新計算	
    countHeadTotalAmount(imReceiveHead);
	
    String brandCode = imReceiveHead.getBrandCode();
	String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);

	String orderTypeCode = imReceiveHead.getOrderTypeCode();
	String orderNo = imReceiveHead.getOrderNo();
	String identification = MessageStatus.getIdentificationMsg(brandCode, orderTypeCode, orderNo);

	// 更新出貨單Status為CLOSE
	imReceiveHead.setStatus(OrderStatus.CLOSE);
	List<ImReceiveItem> ImReceiveItems = imReceiveHead.getImReceiveItems();
	if (ImReceiveItems != null && ImReceiveItems.size() > 0) {
	    for (ImReceiveItem imReceiveItem : ImReceiveItems) {
	   		//imOnhand日結
	   		imOnHandDAO.updateInOnHand(organizationCode, imReceiveItem.getItemCode(), imReceiveHead.getDefaultWarehouseCode(),
					imReceiveHead.getLotNo(), imReceiveItem.getReceiptQuantity(), imReceiveHead.getBrandCode(), opUser);
	
	   		//cmOnhand日結
			String customsWarehouseCode = null;
		    ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, imReceiveHead.getDefaultWarehouseCode(), "Y");
		    if(null==imWarehouse){
		    	throw new FormException("倉庫代碼：" + imReceiveHead.getDefaultWarehouseCode() + "查無其對應報關倉別！");
		    }else{
		    	customsWarehouseCode = imWarehouse.getCustomsWarehouseCode();	// FW, FD...
		    }
		    
		    ImItem item = imItemDAO.findItem(brandCode, imReceiveItem.getItemCode());
		    if(null == item)
		    	throw new ValidationErrorException("查無商品品號:"+imReceiveItem.getItemCode() +"資料！");
		    
		    BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(brandCode, orderTypeCode));
		    String typeCode = buOrderType.getTypeCode();
			String declarationNo  = null;
			Long   declarationSeq = null;
			if("RR".equals(typeCode)){	// 進貨退回報單資料由 imReceiveItem 取得
				if("F".equals(item.getIsTax())){
			    declarationNo  = imReceiveItem.getOriginalDeclarationNo();
			    declarationSeq = imReceiveItem.getOriginalDeclarationSeq();
				}else{
					declarationNo  = null;
					declarationSeq = null;
				}
			}else{
			    declarationNo  = imReceiveHead.getDeclarationNo();
			    declarationSeq = imReceiveItem.getDeclarationItemNo();
			}
			if("T2".equals(brandCode)){
				if(!StringUtils.hasText(item.getIsTax()))
			    	throw new ValidationErrorException("查無商品品號:"+imReceiveItem.getItemCode() +"之稅別資料！");
				
				if(null != declarationNo&& null != declarationSeq && "F".equals(item.getIsTax()))
				cmDeclarationOnHandDAO.updateInOnHand(declarationNo, declarationSeq, imReceiveItem.getItemCode(), 
						customsWarehouseCode, brandCode, imReceiveItem.getReceiptQuantity(), opUser);
			}
			// 產生交易明細檔 出庫
	    	String declType = new String("");
	   		if(StringUtils.hasText(declarationNo)){
	   			declType =cmDeclarationHeadDAO.getDeclarationTypeByNo(declarationNo);
	   		 	if(!StringUtils.hasText(declType))
	   		 		throw new ValidationErrorException("查無來源報單:"+declarationNo +"類別資料！");
	   		}
			Double costAmount = new Double(0);
			costAmount = NumberUtils.getDouble(imReceiveItem.getLocalAmount()) + NumberUtils.getDouble(imReceiveItem.getExpenseApportionmentAmount());
			ImTransation transation = new ImTransation();
			transation.setBrandCode(brandCode);
			if(!"T2".equals(brandCode))
				transation.setTransationDate(imReceiveHead.getOrderDate());
			else if("IMR".equals(typeCode))
				transation.setTransationDate(imReceiveHead.getReceiptDate());
			else
				transation.setTransationDate(imReceiveHead.getWarehouseInDate());

			transation.setOrderTypeCode(orderTypeCode);
			transation.setOrderNo(orderNo);
			transation.setLineId(imReceiveItem.getIndexNo());
			transation.setItemCode(imReceiveItem.getItemCode());
			transation.setWarehouseCode(imReceiveHead.getDefaultWarehouseCode());
			transation.setLotNo(imReceiveHead.getLotNo());
			if(!"T2".equals(brandCode))
				transation.setQuantity(imReceiveItem.getQuantity());
			else
				transation.setQuantity(imReceiveItem.getReceiptQuantity());
			transation.setCostAmount(costAmount);
			transation.setOrderAmount(costAmount);
			transation.setCreatedBy(opUser);
			transation.setCreationDate(new Date());
			transation.setLastUpdatedBy(opUser);
			transation.setLastUpdatedDate(new Date());
			transation.setDeclarationNo(imReceiveHead.getDeclarationNo());
			transation.setDeclarationDate(imReceiveHead.getDeclarationDate());
			transation.setOriginalDeclarationNo(imReceiveItem.getOriginalDeclarationNo());
			transation.setOriginalDeclarationDate(imReceiveItem.getOriginalDeclarationDate());
			transation.setOriginalDeclarationSeq(imReceiveItem.getOriginalDeclarationSeq());
			transation.setOriginalDeclarationItem(imReceiveItem.getOriginalDeclarationItem());
			List itemPrices = imItemPriceDAO.getItemPriceByBeginDate(brandCode, transation.getItemCode(), "1", transation.getTransationDate(), "Y");
			if(null != itemPrices && itemPrices.size() > 0){
				Object[] objArray = (Object[])itemPrices.get(0);
				BigDecimal unitPrice = (BigDecimal)objArray[1];
				if(unitPrice != null){
					transation.setOriginalPriceAmount(unitPrice.doubleValue()*transation.getQuantity());
				}
			}
			transation.setOriginalDeclarationType(declType);
			imTransationDAO.save(transation);
	    }
	    imReceiveHeadDAO.update(imReceiveHead);
	} else {
	    throw new ValidationErrorException("查無" + identification + "的調撥單明細資料！");
	}
    }

	
    /** AJAX Load ImReceiveItem Page Data
     * @param headObj
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public List<Properties> getAJAXPageData(Properties httpRequest) 
    		throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,Exception {
	//log.info("getAJAXPageData");
	// 要顯示的HEAD_ID
	Long   headId    = NumberUtils.getLong(httpRequest.getProperty("headId"));
	String brandCode = (String) httpRequest.getProperty("brandCode");
	String orderTypeCode = (String) httpRequest.getProperty("orderTypeCode");
	BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(brandCode, orderTypeCode));
    String typeCode = buOrderType.getTypeCode(); 
	int iSPage = AjaxUtils.getStartPage(httpRequest);
	int iPSize = AjaxUtils.getPageSize(httpRequest);

	List<Properties> re = new ArrayList();
	List<ImReceiveItem> imReceiveItems = null;
	List<Properties> gridDatas = new ArrayList();
	imReceiveItems = imReceiveItemMainService.findPageLine(headId, iSPage, iPSize);
	for(ImReceiveItem imReceiveItem : imReceiveItems){
	    if("RR".equals(typeCode)){
	    	imReceiveItem.setQuantity(imReceiveItem.getQuantity()*(-1));
	    	imReceiveItem.setReceiptQuantity(imReceiveItem.getReceiptQuantity()*(-1));
	    	imReceiveItem.setAcceptQuantity(imReceiveItem.getAcceptQuantity()*(-1));
	    	imReceiveItem.setForeignAmount(imReceiveItem.getForeignAmount()*(-1));
	    	imReceiveItem.setLocalAmount(imReceiveItem.getLocalAmount()*(-1));
	    }
	    if(StringUtils.hasText(imReceiveItem.getItemCode())){
			ImItem imItem = imItemDAO.findItem(brandCode, imReceiveItem.getItemCode());
			if(null != imItem){
				imReceiveItem.setItemCName( imItem.getItemCName() );
				imReceiveItem.setIsConsignSale(imItem.getIsConsignSale());
			}else{
				imReceiveItem.setItemCName(MessageStatus.DATA_NOT_FOUND);
			}
	    }
	}
	
	if (null != imReceiveItems && imReceiveItems.size() > 0) {
	    //log.info("getAJAXPageData AjaxUtils.imReceiveItems=" + imReceiveItems.size());
	    // 取得第一筆的INDEX
	    Long firstIndex = imReceiveItems.get(0).getIndexNo();
	    // 取得最後一筆 INDEX
	    Long maxIndex = imReceiveItemMainService.findPageLineMaxIndex(Long.valueOf(headId));
	    re.add(AjaxUtils.getAJAXPageData( httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, 
		    			      imReceiveItems, gridDatas, firstIndex, maxIndex));
	} else {
	    re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));
	}
	return re;
    }

    
    /** 更新 ImReceiveItem PAGE 所有的LINE
     * @param httpRequest
     * @return String message
     */
    public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws NumberFormatException, FormException, Exception {
    	//log.info("updateAJAXPageData");
    	String gridData        = httpRequest.getProperty(AjaxUtils.GRID_DATA);
    	int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
    	int gridRowCount       = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
    	Long headId            = NumberUtils.getLong(httpRequest.getProperty("headId"));
    	Double exchangeRate    = NumberUtils.getDouble(httpRequest.getProperty("exchangeRate"));
    	String errorMsg        = null;

    	ImReceiveHead head = imReceiveHeadDAO.findById(headId);
    	head.setExchangeRate(exchangeRate);
    	// 將STRING資料轉成List Properties record data
    	List<Properties> upRecords = AjaxUtils.getGridFieldValue( gridData, gridLineFirstIndex, gridRowCount,
    			ImReceiveHeadMainService.GRID_FIELD_NAMES);
    	
    	int indexNo = imReceiveItemMainService.findPageLineMaxIndex(headId).intValue();	// Get Max INDEX NO
    	if (null != upRecords && null != head) {
    		try{
    			for (Properties upRecord : upRecords) {
    				// 先載入HEAD_ID OR LINE DATA
    				Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
    				ImReceiveItem imReceiveItem = imReceiveItemMainService.findLine(head.getHeadId(), lineId);
    				String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);
    				//log.info("head Id=" + head.getHeadId() + " ,line Id=" + lineId + ",itemCode=" + itemCode+" indexNo="+indexNo);
    				// 如果沒有ITEM CODE 就不會新增或修改
    				if (StringUtils.hasText(itemCode)) {
    					if (null != imReceiveItem) {
    						AjaxUtils.setPojoProperties(imReceiveItem, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
    						
    						//零售價計算 Macoooooo
    						countItemTotalAmountForAjax(head, imReceiveItem);	// LINE 計算
    						log.info(imReceiveItem.getItemCode()+"   零售價:"+imReceiveItem.getUnitPrice());
    						imReceiveItemDAO.update(imReceiveItem);	// UPDATE 
    					} else if( OrderStatus.SAVE.equals(head.getStatus()) || OrderStatus.REJECT.equals(head.getStatus()) ){
    						indexNo++;
    						imReceiveItem = new ImReceiveItem();
    						AjaxUtils.setPojoProperties(imReceiveItem, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
    						imReceiveItem.setIndexNo(Long.valueOf(indexNo));
    						imReceiveItem.setBarcodeCount(imReceiveItem.getQuantity());	// 預設條碼數量為報關單數量
    						if( null==imReceiveItem.getDeclarationItemNo())		// 手工輸入者帶 indexNo 給 DeclarationItemNo
    							imReceiveItem.setDeclarationItemNo(imReceiveItem.getIndexNo());
    						imReceiveItem.setImReceiveHead(head);
    						
    						//零售價計算 Macoooooo
    						countItemTotalAmountForAjax(head, imReceiveItem);	// LINE 計算
    						log.info(imReceiveItem.getItemCode()+"   零售價:"+imReceiveItem.getUnitPrice());
    						imReceiveItemDAO.save(imReceiveItem);	// INSERT
    					}
    				}
    			}
    		}catch (Exception ex) {
    			log.error("updateAJAXPageLinesDataError ：" + ex.toString());
    			throw new Exception("updateAJAXPageLinesDataError ：" + ex.toString());
    		}
    	} else {
    		errorMsg = "沒有進貨單單頭資料";
    	}
    	return AjaxUtils.getResponseMsg(errorMsg);
    }

	
    /** AJAX 合計計算	call by JS display total PAGE
     * @param headObj
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public List<Properties>updateAJAXHeadTotalAmount(Properties httpRequest)
    		throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, Exception {
	log.info("updateAJAXHeadTotalAmount ");
	List re = new ArrayList();
	Properties pro = new Properties();
	Long   headId        = NumberUtils.getLong(httpRequest.getProperty("headId"));
	Double taxRate       = NumberUtils.getDouble(httpRequest.getProperty("taxRate"));
	Double exchangeRate  = NumberUtils.getDouble(httpRequest.getProperty("exchangeRate"));
	Double exportExpense = NumberUtils.getDouble(httpRequest.getProperty("exportExpense"));
	Double exportCommissionRate = NumberUtils.getDouble(httpRequest.getProperty("exportCommissionRate"));
	if(0D == exchangeRate)
		exchangeRate = 1D;
	
	ImReceiveHead imReceiveHead = findById(headId);
	if (null==imReceiveHead)
	    return re;
	
	BuOrderType buOrderType = buOrderTypeService.findById(
			new BuOrderTypeId(imReceiveHead.getBrandCode(), imReceiveHead.getOrderTypeCode()));
    String typeCode = buOrderType.getTypeCode(); 
    
	//String quantityType = "Quantity";
	//String roundType = "round";
	//int demicalType = 2;
	//if("T2".equals(imReceiveHead.getBrandCode())){
		//quantityType = "receipt_Quantity";
		//roundType = "round";
		//demicalType = 2;
	//}
	
	String tmpSqlItem = " select sum(round(nvl(receipt_Quantity ,0)*nvl(FOREIGN_UNIT_PRICE,0),2)) as TotalForeignPurchaseAmount, " +
			       	" nvl( sum( nvl(receipt_Quantity ,0)*nvl(LOCAL_UNIT_PRICE,0)   ),0 ) as TotalLocalPurchaseAmount,  " +
			       	" nvl(sum(nvl(Quantity ,0)),0) as Quantity, nvl(sum(nvl(receipt_Quantity ,0)),0) as ReceiveQuantity," +
			       	" nvl(sum(nvl(accept_Quantity ,0)),0) as AcceptQuantity, nvl(sum(nvl(defect_Quantity ,0)),0) as DefectQuantity," +
			       	" nvl(sum(nvl(sample_Quantity ,0)),0) as SampleQuantity, nvl(sum(nvl(short_Quantity ,0)),0) as ShortQuantity" +
			       	" from im_receive_item where HEAD_ID=" + headId + " and nvl(IS_DELETE_RECORD,'0')<>'1' ";
	List itemList = nativeQueryDAO.executeNativeSql(tmpSqlItem);
	Object[] obj = (Object[]) itemList.get(0);
	Double TotalForeignPurchaseAmount = NumberUtils.round(((BigDecimal) obj[0]).doubleValue(),2);
	Double TotalLocalPurchaseAmount = NumberUtils.round(((BigDecimal) obj[0]).doubleValue() * exchangeRate,2);
	Double ReceiveQuantity = ((BigDecimal) obj[2]).doubleValue();
	Double ReceiptQuantity = ((BigDecimal) obj[3]).doubleValue();
	Double AcceptQuantity  = ((BigDecimal) obj[4]).doubleValue();
	Double DefectQuantity  = ((BigDecimal) obj[5]).doubleValue();
	Double SampleQuantity  = ((BigDecimal) obj[6]).doubleValue();
	Double ShortQuantity   = ((BigDecimal) obj[7]).doubleValue();
	
	pro.setProperty("ReceiveQuantity", OperationUtils.roundToStr(ReceiveQuantity, 0));
	pro.setProperty("ReceiptQuantity", OperationUtils.roundToStr(ReceiptQuantity, 0));
	pro.setProperty("AcceptQuantity",  OperationUtils.roundToStr(AcceptQuantity,  0));
	pro.setProperty("DefectQuantity",  OperationUtils.roundToStr(DefectQuantity,  0));
	pro.setProperty("SampleQuantity",  OperationUtils.roundToStr(SampleQuantity,  0));
	pro.setProperty("ShortQuantity",   OperationUtils.roundToStr(ShortQuantity,   0));

	
	// 2010.02.15 user ellen requirment
	//明細其他費用加總
	//如果狀態為CLOSE則金額不重算

	String tmpSqlExpense = " select nvl(sum(nvl(FOREIGN_AMOUNT,0)),0), nvl(sum(nvl(LOCAL_AMOUNT,0)),0) , nvl(sum(nvl(TAX_AMOUNT,0)),0) " +
		" from im_receive_expense where HEAD_ID=" + headId + " and nvl(IS_DELETE_RECORD,'0')<>'1' ";
	List listExpense = nativeQueryDAO.executeNativeSql(tmpSqlExpense);
	obj = (Object[]) listExpense.get(0);	
	
	Double expenseLocalAmount = ((BigDecimal) obj[1]).doubleValue() + exportExpense * exchangeRate;
	Double taxAmount = ((BigDecimal) obj[2]).doubleValue();
	Double taxForeignAmount = 0D;
	
	//手續費
	Double exportCommissionForeignAmount = TotalForeignPurchaseAmount*(exportCommissionRate/100);
	Double exportCommissionLocalAmount = TotalLocalPurchaseAmount*(exportCommissionRate/100);
	
	//應收金額
	Double totalForeignAccountsPayable = TotalForeignPurchaseAmount + exportCommissionForeignAmount + imReceiveHead.getExpenseForeignAmount();
	Double totalLocalAccountsPayable   = TotalLocalPurchaseAmount + exportCommissionLocalAmount + imReceiveHead.getExpenseLocalAmount();
	
	//2014.2.25 Caspar 修正稅金不含費用
	taxForeignAmount = (totalForeignAccountsPayable - imReceiveHead.getExpenseForeignAmount())*taxRate/100;
	totalForeignAccountsPayable += taxForeignAmount;
	//2014.2.25 Caspar 修正稅金不含費用
	taxAmount += (totalLocalAccountsPayable - imReceiveHead.getExpenseLocalAmount())*taxRate/100; 
	totalLocalAccountsPayable += taxAmount;
	
	if(!OrderStatus.CLOSE.equals(imReceiveHead.getStatus())){
		imReceiveHead.setTotalForeignPurchaseAmount( TotalForeignPurchaseAmount );
		imReceiveHead.setTotalLocalPurchaseAmount( TotalLocalPurchaseAmount );
		imReceiveHead.setExpenseForeignAmount( exportExpense );
		imReceiveHead.setExpenseLocalAmount(expenseLocalAmount);
		imReceiveHead.setTaxAmount(taxAmount);
		imReceiveHead.setTotalLocalAccountsPayable(totalLocalAccountsPayable);
		imReceiveHeadDAO.update(imReceiveHead);
	}
	
	if("RR".equals(typeCode)){
		TotalForeignPurchaseAmount = TotalForeignPurchaseAmount*(-1);
		TotalLocalPurchaseAmount = TotalLocalPurchaseAmount * (-1);
		ReceiveQuantity = ReceiveQuantity*(-1);
		ReceiptQuantity = ReceiptQuantity*(-1);
		AcceptQuantity  = AcceptQuantity*(-1);
		DefectQuantity  = DefectQuantity*(-1);
		SampleQuantity  = SampleQuantity*(-1);
		ShortQuantity  	= ShortQuantity*(-1);
		taxAmount = taxAmount*(-1);
		taxForeignAmount = taxForeignAmount*(-1);
		totalLocalAccountsPayable = totalLocalAccountsPayable*(-1);
		totalForeignAccountsPayable = totalForeignAccountsPayable*(-1);
	}

	pro.setProperty("TotalLocalPurchaseAmount",    OperationUtils.roundToStr(TotalLocalPurchaseAmount,   2));
	pro.setProperty("TotalForeignPurchaseAmount",  OperationUtils.roundToStr(TotalForeignPurchaseAmount, 2));
	pro.setProperty("ExpenseLocalAmount",          OperationUtils.roundToStr(expenseLocalAmount, 2));
	pro.setProperty("ExpenseForeignAmount",        OperationUtils.roundToStr(exportExpense, 2));
	pro.setProperty("TaxAmount",                   OperationUtils.roundToStr(taxAmount, 2));
	pro.setProperty("TaxForeignAmount",            OperationUtils.roundToStr(taxForeignAmount, 2));
	pro.setProperty("ExportCommissionLocalAmount", OperationUtils.roundToStr(exportCommissionLocalAmount, 2));
	pro.setProperty("ExportCommissionForeignAmount",OperationUtils.roundToStr(exportCommissionForeignAmount, 2));
	pro.setProperty("TotalLocalAccountsPayable",   OperationUtils.roundToStr(totalLocalAccountsPayable,  2));
	pro.setProperty("TotalForeignAccountsPayable", OperationUtils.roundToStr(totalForeignAccountsPayable, 2));
	pro.setProperty("CanBeRefresh", "Y");

	
	re.add(pro);
	return re;
    }


    /**
     * 將進貨單的資料寫到LC單
     * @param modifyObj
     * @throws Exception
     */
    private ImLetterOfCreditHead modifyReceiveToLC(ImReceiveHead imReceiveHead, int opType, boolean isReject ) 
    	throws Exception {
	//log.info("modifyReceiveToLC type ="+ opType);
	String brandCode      = imReceiveHead.getBrandCode();
	String receiveOrderNo = imReceiveHead.getOrderNo();
	String loginUser      = imReceiveHead.getLastUpdatedBy();
	String orderTypeCode  = imReceiveHead.getOrderTypeCode();
	String oldLcNo     = (opType==1?imReceiveHead.getReserve1():   imReceiveHead.getReserve2());
	String lcNo        = (opType==1?imReceiveHead.getLcNo():       imReceiveHead.getLcNo1());
	Double lcUseAmount = (opType==1?imReceiveHead.getLcUseAmount():imReceiveHead.getLcUseAmount1());
	
    	// 用來更新狀態用的
	HashMap statusConditionMap = new HashMap();
	statusConditionMap.put("beforeChangeStatus", OrderStatus.SAVE);
	try{
	    // 若進貨單中記錄原有使用 LC單號, 則清除其相關資料
	    if (StringUtils.hasText(oldLcNo) && !oldLcNo.equals(lcNo)) {	// remove old lcno
			HashMap removeConditionMap = new HashMap();
			removeConditionMap.put("lcNo_Start", oldLcNo);
			removeConditionMap.put("lcNo_End",  oldLcNo);
			removeConditionMap.put("brandCode",  brandCode);
			List<ImLetterOfCreditHead> removeImLetterOfCreditHeads = imLetterOfCreditHeadDAO.findLetterOfCreditList(removeConditionMap);
			//log.info("removeImLetterOfCreditHeads.size=" + removeImLetterOfCreditHeads.size());
			if (null != removeImLetterOfCreditHeads && removeImLetterOfCreditHeads.size() > 0) {
			    ImLetterOfCreditHead imLetterOfCreditHead = removeImLetterOfCreditHeads.get(0);
			    if (OrderStatus.CLOSE.equalsIgnoreCase(imLetterOfCreditHead.getStatus())) {
			    	throw new FormException("LC已經結案 LC NO. " + oldLcNo);
			    } else {
					// 找出此 LC 明細中此進貨單記錄並刪除
					List<ImLetterOfCreditLine> imLetterOfCreditLines = imLetterOfCreditHead.getImLetterOfCreditLines();
					if(null != imLetterOfCreditLines && imLetterOfCreditLines.size() > 0){
						for (int index = 0; index < imLetterOfCreditLines.size(); index++) {
						    ImLetterOfCreditLine imLetterOfCreditLine = imLetterOfCreditLines.get(index);
							if (receiveOrderNo.equals(imLetterOfCreditLine.getReceiveNo())) {
								imLetterOfCreditLines.remove(index);
								break;
							}
						}
					}
					imLetterOfCreditService.countAllTotalAmount(imLetterOfCreditHead, statusConditionMap, loginUser);
					imLetterOfCreditHeadDAO.update(imLetterOfCreditHead);
			    }
			}
	    }
	    
	    ImLetterOfCreditHead imLetterOfCreditHead = null;
	    // 若進貨單狀態不是駁回 && 有註明需要使用 LC 單號
	    if (StringUtils.hasText(lcNo)  && !isReject) {	// add new lcno && 非退回
			HashMap addConditionMap = new HashMap();
			addConditionMap.put("lcNo_Start", lcNo);
			addConditionMap.put("lcNo_End",   lcNo);
			addConditionMap.put("brandCode",  brandCode);
			List<ImLetterOfCreditHead> addImLetterOfCreditHeads = imLetterOfCreditHeadDAO.findLetterOfCreditList(addConditionMap);
			//log.info("modifyReceiveToLC = " + lcNo +" addImLetterOfCreditHeads Count=" + addImLetterOfCreditHeads.size());
			if (null != addImLetterOfCreditHeads && addImLetterOfCreditHeads.size() > 0) {
			    imLetterOfCreditHead = addImLetterOfCreditHeads.get(0);
			    if (OrderStatus.CLOSE.equalsIgnoreCase(imLetterOfCreditHead.getStatus())) {
			    	throw new FormException("LC已經結案 LC NO. " + oldLcNo);
			    } else if(!lcNo.equals(oldLcNo)){
					List<ImLetterOfCreditLine> imLetterOfCreditLines = imLetterOfCreditHead.getImLetterOfCreditLines();
					if (null == imLetterOfCreditLines) {
					    imLetterOfCreditLines = new ArrayList(0);
					}
					//log.info("modifyReceiveToLC =" + lcNo + " OK");
					ImLetterOfCreditLine imLetterOfCreditLine = new ImLetterOfCreditLine();
					imLetterOfCreditLine.setReceiveNo(receiveOrderNo);
					imLetterOfCreditLine.setReceiveAmount(lcUseAmount);
					imLetterOfCreditLine.setBrandCode(brandCode);
					imLetterOfCreditLine.setOrderTypeCode(orderTypeCode);
					imLetterOfCreditLine.setCreatedBy(loginUser);
					imLetterOfCreditLine.setCreationDate(new Date());
					imLetterOfCreditLine.setLastUpdatedBy(loginUser);
					imLetterOfCreditLine.setLastUpdateDate(new Date());
					imLetterOfCreditLines.add(imLetterOfCreditLine);
					imLetterOfCreditHead.setImLetterOfCreditLines(imLetterOfCreditLines);
					imLetterOfCreditService.countAllTotalAmount(imLetterOfCreditHead, statusConditionMap, loginUser);
					imLetterOfCreditHeadDAO.update(imLetterOfCreditHead);
			    }
			}
	    }
	    return imLetterOfCreditHead;
	} catch (Exception ex) {
	    log.error("modifyReceiveToLC 發生錯誤，原因：" + ex.toString());
	    throw new Exception("modifyReceiveToLC 發生錯誤，原因：" + ex.getMessage());
	}
    }

	
    /**執行銷貨單初始化
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
    	log.info("executeInitial");
    	HashMap resultMap = new HashMap();
    	try{
    		Object otherBean         = parameterMap.get("vatBeanOther");
    		String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    		String loginBrandCode    = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
    		
    		BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(loginBrandCode, loginEmployeeCode);
    		if(employeeWithAddressView == null){
    			throw new Exception("查無員工代號: " + loginEmployeeCode +" 對應之員工");
    		} 
    		
    		String orderTypeCode     = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
    		String userType          = (String)PropertyUtils.getProperty(otherBean, "userType");
    		String formIdString      = (String)PropertyUtils.getProperty(otherBean, "formId");
    		Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;

    		ImReceiveHead imReceive      = null;
    		if(formId != null){
    			imReceive = findById(formId);
    			if(null!=imReceive){
    				loginBrandCode = imReceive.getBrandCode();
    				orderTypeCode  = imReceive.getOrderTypeCode();
    			}
    			//為了舊版的BUG 假如TAXRATE存到0.05改成5
    			if(0.05 == NumberUtils.getDouble(imReceive.getTaxRate())){
    				imReceive.setTaxRate(5D);
    			}
    		}
    		log.info("loginbrandcode"+loginBrandCode);
    		BuBrand     buBrand     = buBrandService.findById( loginBrandCode );
    		BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(loginBrandCode, orderTypeCode ) );
    		if(formId == null){
    			imReceive      = createNewImReceiveHead(parameterMap, resultMap, buBrand, buOrderType);
    			if("T2".equals(loginBrandCode))
    				imReceive.setOrderDate(DateUtils.parseDate(DateUtils.format(new Date())));
    		}

    		if("WAREHOUSE".equals(userType) && OrderStatus.SAVE.equals(imReceive.getWarehouseStatus()) ){
    			imReceive.setReceiptDate(DateUtils.parseDate(DateUtils.format(new Date())));
    			imReceive.setReceiptedBy(loginEmployeeCode);
    		}
    		log.info("init===brand"+buBrand.getBranchCode());
    		resultMap.put("userType",	       userType);
    		resultMap.put("branchCode",        buBrand.getBranchCode() );	// 2->T2
    		resultMap.put("typeCode",          buOrderType.getTypeCode() );	// IMR->進貨 or RR->進貨退回
    		resultMap.put("status",            imReceive.getStatus() );
    		resultMap.put("warehouseStatus",   imReceive.getWarehouseStatus() );
    		resultMap.put("expenseStatus",     imReceive.getExpenseStatus() );	    
    		resultMap.put("statusName",        OrderStatus.getChineseWord(imReceive.getStatus()));
    		resultMap.put("brandName",         buBrandDAO.findById(imReceive.getBrandCode()).getBrandName());
    		resultMap.put("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));
    		resultMap.put("createdByName",     UserUtils.getUsernameByEmployeeCode(imReceive.getCreatedBy()));
    		resultMap.put("employeeCode",      loginEmployeeCode);
    		resultMap.put("budgetType",        buBrand.getBudgetType());	// 預算扣除方式 P:UnitPrice/C:Cost/T:整筆扣除
    		resultMap.put("budgetCheckType",   buBrand.getBudgetCheckType());	// 預算扣除類別 Y:year/M:month
    		resultMap.put("monthlyCloseMonth", buBrand.getMonthlyCloseMonth() );	// 關帳月份
    		resultMap.put("form", imReceive);
    		
    		//信用狀LcNo跟著異動(wade)
    		if(imReceive.getLcNo() != null && imReceive.getLcNo().trim().length() > 0)
    			resultMap.put("lcNo", imReceive.getLcNo());
    		
    		if(imReceive.getLcNo1() != null && imReceive.getLcNo1().trim().length() > 0)
    			resultMap.put("lcNo1", imReceive.getLcNo());
    			
    		// 預防轉檔資料中沒有轉入 supplierName
    		if( StringUtils.hasText(imReceive.getSupplierCode()) && !StringUtils.hasText(imReceive.getSupplierName()) ){
    			BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(imReceive.getBrandCode(), imReceive.getSupplierCode());
    			if(null!=buSWAV)
    				resultMap.put("supplierName", buSWAV.getChineseName() );
    		}    
    		
    		HashMap conditionMap = new HashMap();
    		conditionMap.put("brandCode",    loginBrandCode);
    		conditionMap.put("supplierCode", imReceive.getSupplierCode());
    		conditionMap.put("status_Start",   OrderStatus.FINISH );
    		if( OrderStatus.SAVE.equals(imReceive.getStatus()) || OrderStatus.REJECT.equals(imReceive.getStatus()) )
    			conditionMap.put("status_End",   OrderStatus.FINISH );
    		else
    			conditionMap.put("status_End",   OrderStatus.CLOSE );

    		List<ImLetterOfCreditHead> allLCList = imLetterOfCreditService.findLetterOfCreditList(conditionMap);

    		String organizationCode = UserUtils.getOrganizationCodeByBrandCode( imReceive.getBrandCode() );
    		List<BuPaymentTerm>      allPaymentTerm     = buBasicDataService.findPaymentTermByOrganizationAndEnable(organizationCode, null);
    		List<BuCountry>          allCountryCode     = buBasicDataService.findCountryByEnable(null);
    		List<BuCurrency>         allCurrencyCode    = buBasicDataService.findCurrencyByEnable(null);
    		List<BuCommonPhraseLine> allTradeTeam       = buCommonPhraseService.findEnableLineById("TradeTeam");
    		List<BuCommonPhraseLine> allExpenseCode     = buCommonPhraseService.findEnableLineById("Expense");
    		List<BuCommonPhraseLine> allTaxType         = buCommonPhraseService.getBuCommonPhraseLines("TaxType");
    		List<BuCommonPhraseLine> allBudgetYearList  = buCommonPhraseService.getBuCommonPhraseLines("BudgetYearList");
    		List<BuCommonPhraseLine> allBudgetMonthList = buCommonPhraseService.getBuCommonPhraseLines("Month");
    		List<BuCommonPhraseLine> allDeclarationType = buCommonPhraseService.getBuCommonPhraseLines("DeclarationType");
    		List<ImWarehouse>        allWarehouse		= new ArrayList<ImWarehouse>(0);
    		if("T2".equals(imReceive.getBrandCode())){
    			String defaultWarehouseCodes[] = getDefaultWarehouseCodeByBrandCode(buBrand, buOrderType);
    			String[] allconditions = new String[defaultWarehouseCodes.length + 1];
    			allconditions[0] = loginBrandCode; 
    			StringBuffer sb = new StringBuffer("");
    			for (int i = 0; i < defaultWarehouseCodes.length; i++) {
    				allconditions[i+1]  = defaultWarehouseCodes[i];
    				sb.append(" warehouseCode = ? or ");
    			}
    			sb.delete(sb.length()-3,sb.length());
    			allWarehouse       = imWarehouseDAO.findByProperty("ImWarehouse", "and brandCode = ? and ( "+sb.toString()+" )", allconditions);
    		}else{
    			allWarehouse       = imWarehouseDAO.findByProperty("ImWarehouse","brandCode",imReceive.getBrandCode());
    		}
    		List<BuOrderType>        allOrderType       = buOrderTypeService.findOrderbyType(imReceive.getBrandCode(), buOrderType.getTypeCode());
    		List<BuOrderType>        allPoOrderType     = buOrderTypeService.findOrderbyType(imReceive.getBrandCode(), "PO");
    		List<ImItemCategory>     allItemCategory    = imItemCategoryService.findByCategoryType(imReceive.getBrandCode(), "ITEM_CATEGORY");

    		resultMap.put("allLCList",          AjaxUtils.produceSelectorData( allLCList,          "lcNo",            "lcNo",          false, true));
    		resultMap.put("allPaymentTerm",     AjaxUtils.produceSelectorData( allPaymentTerm,     "paymentTermCode", "name",          false, true));
    		resultMap.put("allCountryCode",     AjaxUtils.produceSelectorData( allCountryCode,     "countryCode",     "countryCName",  false, true));
    		resultMap.put("allCurrencyCode",    AjaxUtils.produceSelectorData( allCurrencyCode,    "currencyCode",    "currencyCName", false, true));
    		resultMap.put("allTradeTeam",       AjaxUtils.produceSelectorData( allTradeTeam  ,     "lineCode",        "name",          false, true));
    		resultMap.put("allExpenseCode",     AjaxUtils.produceSelectorData( allExpenseCode,     "lineCode",        "name",          false, true));
    		resultMap.put("allTaxType",         AjaxUtils.produceSelectorData( allTaxType,         "lineCode",        "name",          false, true));
    		resultMap.put("allBudgetYearList",  AjaxUtils.produceSelectorData( allBudgetYearList,  "lineCode",        "name",          false, true, DateUtils.getCurrentDateStr().substring(0, 4)));
    		resultMap.put("allBudgetMonthList", AjaxUtils.produceSelectorData( allBudgetMonthList, "lineCode",        "name",          false, true));
    		resultMap.put("allDeclarationType", AjaxUtils.produceSelectorData( allDeclarationType, "lineCode",        "name",          false, true));
    		resultMap.put("allWarehouse",       AjaxUtils.produceSelectorData( allWarehouse,       "warehouseCode",   "warehouseName", true,  false));
    		resultMap.put("allOrderType",       AjaxUtils.produceSelectorData( allOrderType,       "orderTypeCode",   "name",          true,  true));
    		resultMap.put("allPoOrderType",     AjaxUtils.produceSelectorData( allPoOrderType,     "orderTypeCode",   "name",          true,  true));
    		resultMap.put("allItemCategory",    AjaxUtils.produceSelectorData( allItemCategory,    "categoryCode",    "categoryName",  false, true));

    		//for 儲位用
    		if(imStorageAction.isStorageExecute(imReceive)){
    			//建立儲位單
    			Map storageMap = new HashMap();
    			storageMap.put("storageTransactionDate", "warehouseInDate");
    			storageMap.put("storageTransactionType", ImStorageService.IN);
    			storageMap.put("status", "warehouseStatus");
    			
    			//轉出或轉入
    			//if("IMR".equals(buOrderType.getTypeCode())){
    				//storageMap.put("deliveryWarehouseCode", "");
    				storageMap.put("arrivalWarehouseCode", "defaultWarehouseCode");
    			//}else{
    				//storageMap.put("deliveryWarehouseCode", "defaultWarehouseCode");
    				//storageMap.put("arrivalWarehouseCode", "");
    			//}
    			
    			ImStorageHead imStorageHead = imStorageAction.executeImStorageHead(storageMap, imReceive);

    			resultMap.put("storageHeadId", imStorageHead.getStorageHeadId());
    			resultMap.put("beanHead", "ImReceiveHead");
    			resultMap.put("beanItem", "imReceiveItems");
    			resultMap.put("quantity", "receiptQuantity");
    			resultMap.put("storageTransactionType", ImStorageService.IN);
    			resultMap.put("storageStatus", "#F.warehouseStatus");
    			//轉出或轉入
    			//if("IMR".equals(buOrderType.getTypeCode())){
    				//resultMap.put("deliveryWarehouse", "");
    				resultMap.put("arrivalWarehouse", "#F.defaultWarehouseCode");
    			//}else{
    				//resultMap.put("deliveryWarehouse", "#F.defaultWarehouseCode");
    				//resultMap.put("arrivalWarehouse", "");
    			//}
    		}
    		
    		return resultMap;       	
    	}catch (Exception ex) {
    		log.error("進貨單初始化失敗，原因：" + ex.toString());
    		throw new Exception("進貨單初始化失敗，原因：" + ex.toString());
    	}
    }
	
    
    /** 產生一筆新的 ImReceiveHead 
     * @param argumentMap
     * @param resultMap
     * @return
     * @throws Exception
     */
    public ImReceiveHead createNewImReceiveHead(Map parameterMap, Map resultMap, BuBrand buBrand, BuOrderType buOrderType ) throws Exception {
    	//log.info("createNewImReceiveHead");
    	Object otherBean = parameterMap.get("vatBeanOther");
    	String loginBrandCode    = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");		
    	String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    	String orderTypeCode     = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");

    	try{
    		ImReceiveHead form = new ImReceiveHead();
    		if(!"2".equals(buBrand.getBranchCode())){
    			form.setItemCategory(loginBrandCode);
    		}
    		form.setBrandCode(           loginBrandCode);
    		form.setOrderTypeCode(       orderTypeCode);
    		form.setStatus(              OrderStatus.SAVE);
    		form.setExpenseStatus(       OrderStatus.SAVE);
    		form.setWarehouseStatus(     OrderStatus.SAVE);		// 指定WAREHOUSE STATUS 預設值是 SAVE 
    		
    		String defaultWarehouseCode = getDefaultWarehouseCodeByBrandCode(buBrand, buOrderType)[0];
    		form.setDefaultWarehouseCode(defaultWarehouseCode);
    		form.setCreatedBy(           loginEmployeeCode);
    		form.setLastUpdatedBy(       loginEmployeeCode);
    		form.setLastUpdateDate(      DateUtils.parseDate(DateUtils.format(new Date())));	 
    		form.setExchangeRate(1D);	// 匯率預設
    		form.setBudgetYear(  String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
    		if( "M".equals(buBrand.getBudgetCheckType()) ){	// 預算扣除類別 Y:year/M:month
    			form.setBudgetMonth( String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1));
    		}

    		if( "F".equals(buOrderType.getTaxCode()) ){	// IRF, EIF
    			form.setTaxType("1"); 	// 免稅
    			form.setTaxRate( 0D );
    		}else{
    			form.setTaxType("3"); 	// 應稅
    			form.setTaxRate( 5D );
    			if ("2".equals(buBrand.getBranchCode())){	//EIP
    				form.setDefPoOrderType("EPP");
    			}else{
    				form.setDefPoOrderType("POL");		//IRL
    			}
    		}

    		form.setExportCommissionRate(0D);
    		form.setExportExpense(0D);
    		saveTmp(form);
    		return form;
    	}catch (Exception ex) {
    		log.error("產生新進貨單失敗，原因：" + ex.toString());
    		throw new Exception("產生新進貨單發生錯誤！");
    	} 	
    }
    
	public String[] getDefaultWarehouseCodeByBrandCode(BuBrand buBrand, BuOrderType buOrderType){
		
		String[] defaultWarehouseCodes = new String[]{""};
		
		if("2".equals(buBrand.getBranchCode()) && "IMR".equals(buOrderType.getTypeCode()) && "P".equals(buOrderType.getTaxCode())  ){    // T2 && IMR && P
			log.info("defaulBbranch"+buBrand.getBranchCode());
			defaultWarehouseCodes =  buBrand.getDefaultWarehouseCode11().split(",");
		}else if("2".equals(buBrand.getBranchCode()) && "RR".equals(buOrderType.getTypeCode()) && "P".equals(buOrderType.getTaxCode())  ){// T2 && RR && P
			defaultWarehouseCodes =  buBrand.getDefaultWarehouseCode71().split(",");
		}else if(( "2".equals(buBrand.getBranchCode()) && "IMR".equals(buOrderType.getTypeCode()) && "F".equals(buOrderType.getTaxCode()) ) ||
				(!"2".equals(buBrand.getBranchCode()) && "IMR".equals(buOrderType.getTypeCode()) ) ){// (T2 && IMR && F) or (!T2 && IMR)
			defaultWarehouseCodes =  buBrand.getDefaultWarehouseCode1().split(",");
		}else if(( "2".equals(buBrand.getBranchCode()) && "RR".equals(buOrderType.getTypeCode()) && "F".equals(buOrderType.getTaxCode()) ) ||
				(!"2".equals(buBrand.getBranchCode()) && "RR".equals(buOrderType.getTypeCode())  ) ){// (T2 && RR && F) or (!T2 && IMR)
			defaultWarehouseCodes = buBrand.getDefaultWarehouseCode7().split(",");
		}
		return defaultWarehouseCodes;
		
	}
    
    /** form 啟始時查出該筆DATA
     * @param argumentMap
     * @param resultMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public ImReceiveHead findImReceiveHead(Map parameterMap, Map resultMap) 
    	throws FormException, Exception {
	//log.info("findImImReceiveHead");
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
	    Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
	    ImReceiveHead form = findById(formId);
	    if(form != null){
	        return form;
	    }else{
		throw new NoSuchObjectException("查無進貨單主鍵：" + formId + "的資料！");
	    }	    
	}catch (FormException fe) {
	    log.error("查詢進貨單失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	}catch (Exception ex) {
	    log.error("查詢進貨單發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢進貨單發生錯誤！");
	}
    }
    
    
    /** 檢核 HeadId 是否有值 */
    public Long getImReceiveHeadId(Object bean) throws FormException, Exception{
	Long headId = null;
	String id = (String)PropertyUtils.getProperty(bean, "headId");
	//log.info("headId=" + id);
	if(StringUtils.hasText(id)){
	    headId = NumberUtils.getLong(id);
	}else{
	    throw new ValidationErrorException("傳入的進貨單主鍵為空值！");
	}
	return headId;
    }

    
    /** 檢核 HeadId 是否有值 */
    private ImReceiveHead getActualImReceive(Long headId) throws FormException, Exception{
        //log.info( "getActualImReceive " + headId );
        ImReceiveHead imReceive = null;
        imReceive = findById(headId);
	if(imReceive  == null){
	     throw new NoSuchObjectException("查無進貨單主鍵：" + headId + "的資料！");
	}
        return imReceive;
    }
    
    
    /**find by pk
     * @param headId
     * @return
     */
    public ImReceiveHead findById(Long headId) {
	//log.info(" findById " + headId );
	String message = null;
	ImReceiveHead imReceive = null;
	try{ 
	    imReceive = (ImReceiveHead) imReceiveHeadDAO.findByPrimaryKey( ImReceiveHead.class, headId );
	}catch (Exception ex) {
	    message = "查無進貨單資料，原因：" + ex.toString();
	    log.error(message);
	}
	return imReceive;
    }

    
    /**find by pk
     * @param headId
     * @return
     */
    public ImReceiveHead findByIdForWarehouseExport(Long headId) {
    	//log.info(" findByIdForWarehouseExport " + headId );
    	String message = null;
    	ImReceiveHead imReceive = null;
    	try{ 
    		imReceive = (ImReceiveHead) imReceiveHeadDAO.findByPrimaryKey( ImReceiveHead.class, headId );
    		List<ImReceiveItem> items = imReceive.getImReceiveItems();
    		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
				ImReceiveItem imReceiveItem = (ImReceiveItem) iterator.next();
				imReceiveItem.setReserve2(imReceive.getOrderTypeCode() + imReceive.getOrderNo());
				ImItemCurrentPriceView imitemCurrentPriceView = imItemCurrentPriceViewDAO.
					findCurrentPrice(imReceive.getBrandCode(), imReceiveItem.getItemCode(), "1");
				if(null != imitemCurrentPriceView){
					imReceiveItem.setItemCName(imitemCurrentPriceView.getItemCName());
					imReceiveItem.setReserve4(imitemCurrentPriceView.getSupplierItemCode());
					imReceiveItem.setReserve5(String.valueOf(NumberUtils.getDouble(imitemCurrentPriceView.getUnitPrice())));
				}else{
				}
			}
    	}catch (Exception ex) {
    		message = "查無進貨單資料，原因：" + ex.toString();
    		log.error(message);
    	}
    	return imReceive;
    }
    
    /** 取單號後更新主檔
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updateImReceiveWithActualOrderNO(Map parameterMap, String OpType ) throws FormException, Exception {
	//log.info("updateImReceiveWithActualOrderNO");
        HashMap resultMap = new HashMap();
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Object otherBean    = parameterMap.get("vatBeanOther");
            Long headId = getImReceiveHeadId(formLinkBean); 
            String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
            ImReceiveHead imReceive = getActualImReceive(headId);			//取得欲更新的bean
            String identification = 
        	MessageStatus.getIdentification(imReceive.getBrandCode(), imReceive.getOrderTypeCode(), imReceive.getOrderNo());
            siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);	// clear 原有 ERROR RECORD
            
            AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imReceive);
            if (OpType.equals("FG")) {	// FG 暫存單號
            	modifyImReceive( imReceive, loginEmployeeCode);
            }else{			// BG 取正式單號
            	String resultMsg = modifyAjaxImReceive(imReceive, loginEmployeeCode);
            	resultMap.put("entityBean", imReceive);
            	resultMap.put("resultMsg",  resultMsg);
            }
            return resultMap;
            
        } catch (FormException fe) {
            log.error("進貨單存檔失敗，原因1：" + fe.toString());
            throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("進貨單存檔時發生錯誤，原因1：" + ex.toString());
	    throw new Exception("進貨單存檔時發生錯誤，原因1：" + ex.getMessage());
	}
    }

    
    /**暫存單號取實際單號
     * @param ImReceiveHead
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String modifyAjaxImReceive(ImReceiveHead imReceive, String loginUser)
    throws ObtainSerialNoFailedException, FormException, Exception {
    	if (AjaxUtils.isTmpOrderNo(imReceive.getOrderNo())) {
    		String serialNo = buOrderTypeService.getOrderSerialNo(imReceive.getBrandCode(), imReceive.getOrderTypeCode());
			
    		if (serialNo.equals("unknow"))
    			throw new ObtainSerialNoFailedException("取得" + imReceive.getOrderTypeCode() + "單號失敗！");
    		
    		//for 儲位用
    		if(imStorageAction.isStorageExecute(imReceive)){
    			//取得儲位單正式的單號 2011.11.11 by Caspar
    			ImStorageHead imStorageHead = imStorageAction.updateOrderNo(imReceive);

    			//更新儲位單SOURCE ORDER_NO
    			imStorageHead.setSourceOrderNo(serialNo);
    			imStorageService.updateHead(imStorageHead, imReceive.getLastUpdatedBy());
    		}
			
    		imReceive.setOrderNo(serialNo);
    	}	
    	modifyImReceive( imReceive, loginUser);
    	return imReceive.getOrderTypeCode() + "-" + imReceive.getOrderNo() + "存檔成功！";
    }

    
    /**更新至ImReceiveHead主檔
     * @param ImReceiveHead
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String modifyImReceive(ImReceiveHead imReceive, String loginUser)
	    throws ObtainSerialNoFailedException, FormException, Exception {
		//log.info("modifyImReceive");
		imReceive.setLastUpdatedBy(loginUser);
		imReceive.setLastUpdateDate(new Date());
		imReceiveHeadDAO.update(imReceive);
	    return imReceive.getOrderTypeCode() + "-" + imReceive.getOrderNo() + "存檔成功！";
    }
    
    
    /** 更新 ImReceive
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updateAJAXImReceive(Map parameterMap) throws FormException, Exception {
        HashMap resultMap = new HashMap();
        log.info("updateAJAXImReceive");
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Object otherBean    = parameterMap.get("vatBeanOther");
            String formStatus         = (String)PropertyUtils.getProperty(otherBean, "formStatus");
            String loginEmployeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
            String userType           = (String)PropertyUtils.getProperty(otherBean, "userType");
            String branchCode         = (String)PropertyUtils.getProperty(otherBean, "branchCode");
            String typeCode           = (String)PropertyUtils.getProperty(otherBean, "typeCode");
            String warehouseStatus 	  = (String)PropertyUtils.getProperty(otherBean, "warehouseStatus");//倉庫起一次調撥單(wade)
            
            Long headId = getImReceiveHeadId(formLinkBean);
            if(!StringUtils.hasText(formStatus)){
            	throw new Exception("單據狀態參數為空值，無法執行存檔！");	
            }
	    	
            //取得欲更新的bean
            ImReceiveHead imReceiveHead = getActualImReceive(headId);
            AjaxUtils.copyJSONBeantoPojoBean(formBindBean,imReceiveHead);
            //移除delete的line
            removeDetailItemCode(imReceiveHead);
            removeReceiveExpenses(imReceiveHead);
            
            ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(imReceiveHead.getBrandCode(), imReceiveHead.getDefaultWarehouseCode(), "Y");
            
            BuBrand buBrand       = buBrandService.findById( imReceiveHead.getBrandCode() );
            String orderTypeCode  = imReceiveHead.getOrderTypeCode();
            BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(imReceiveHead.getBrandCode(), orderTypeCode ) );
            String orderTypeCondition = buOrderType.getOrderCondition();
            String resultMsg = modifyAjaxImReceive(imReceiveHead, loginEmployeeCode);
        	// 判斷是否為駁回(當isReject==true, 相關庫存與帳務將 *(-1)處理)
        	boolean isReject = false;
        	if(( formStatus.equals(OrderStatus.REJECT) && "IMR".equals(typeCode) ) ||	// 駁回  && 進貨
        	   (!formStatus.equals(OrderStatus.REJECT) &&  "RR".equals(typeCode) ) ){	// 非駁回  && 進貨退出
        	    isReject = true;
        	}

        	 String identification = MessageStatus.getIdentification(imReceiveHead.getBrandCode(), imReceiveHead.getOrderTypeCode(), imReceiveHead.getOrderNo() );
        	 
            //重新計算金額
	    	countHeadTotalAmount(imReceiveHead);
	    	
            // IMR && (SHIPPING)  時處裡 LC 
        	if("IMR".equals(typeCode) && ("SHIPPING".equals(userType) || "SHIPPING2".equals(userType)) && OrderStatus.SIGNING.equals(formStatus)){
        	    List<ImReceiveExpense> imReceiveExpenses = imReceiveHead.getImReceiveExpenses();
        	    if(null == imReceiveExpenses)
        	    	imReceiveExpenses = new ArrayList<ImReceiveExpense>(0);

        	    //刪除 LC開狀手續費
        	    if(null!=imReceiveExpenses && imReceiveExpenses.size()>0){
        		    for(int i = imReceiveExpenses.size()-1 ; i >=0 ; i --){
        		    	ImReceiveExpense imReceiveExpense = imReceiveExpenses.get(i);
        		    	if("02".equals(imReceiveExpense.getExpenseCode()))
        		    		imReceiveExpenses.remove(i);
        		    }
        	    }
    	    	
        	    // 刪除原本寫入LC的進貨資料, 建立目前的進貨資料於 LC 明細
        	    if(null != imReceiveHead.getLcNo()){
        	    	ImLetterOfCreditHead imLetterOfCreditHead = modifyReceiveToLC( imReceiveHead, 1, isReject );
        	    	imReceiveHead.setReserve1( imReceiveHead.getLcNo() );
        	    	//LC 明細只有一筆時, 表示本進貨單為該LC第一筆使用, 將該LC開狀手續費寫入本進貨單費用
            		if(null!=imLetterOfCreditHead){
            			List<ImLetterOfCreditLine> lines = imLetterOfCreditHead.getImLetterOfCreditLines();
    					if(null != lines && lines.size() >= 1){
    						ImLetterOfCreditLine line = lines.get(0);
    						if(imReceiveHead.getOrderNo().equals(line.getReceiveNo()))
    							insertImReceiveExpense(imReceiveHead, imReceiveExpenses, imLetterOfCreditHead);
    					}
            		}
        	    }
        	    
        	    if(null != imReceiveHead.getLcNo1()){
        	    	ImLetterOfCreditHead imLetterOfCreditHead1 = modifyReceiveToLC( imReceiveHead, 2, isReject );
        	    	imReceiveHead.setReserve2( imReceiveHead.getLcNo1() );
            		//LC1 明細只有一筆時, 表示本進貨單為該LC第一筆使用, 將該LC開狀手續費寫入本進貨單費用
            		if(null!=imLetterOfCreditHead1 && imLetterOfCreditHead1.getImLetterOfCreditLines().size()==1){
            			List<ImLetterOfCreditLine> lines = imLetterOfCreditHead1.getImLetterOfCreditLines();
            			if(null != lines && lines.size() >= 1){
    						ImLetterOfCreditLine line = lines.get(0);
    						if(imReceiveHead.getOrderNo().equals(line.getReceiveNo()))
    							insertImReceiveExpense(imReceiveHead, imReceiveExpenses, imLetterOfCreditHead1);	
    					}
            		}
        	    }
        	    
        	    Long indexNo = 1L;
        	    for (Iterator iterator = imReceiveExpenses.iterator(); iterator.hasNext();) {
					ImReceiveExpense expense = (ImReceiveExpense) iterator.next();
					expense.setIndexNo(indexNo++);
				}
        	    
        	    imReceiveHead.setImReceiveExpenses(imReceiveExpenses);
                //重新計算金額
    	    	countHeadTotalAmount(imReceiveHead);
        	}

            if(
            	( "SHIPPING".equals(userType) && ((OrderStatus.SIGNING.equals(formStatus) && !OrderStatus.SIGNING.equals(imReceiveHead.getStatus())) || OrderStatus.REJECT.equals(formStatus)))
            	||
            	( "WAREHOUSE".equals(userType) && (OrderStatus.SIGNING.equals(formStatus) || OrderStatus.REJECT.equals(formStatus)))
            ){
	        	// 船務送出紀錄負責倉管人員 (在流程時直接取此資料往下一關送)
	        	if("SHIPPING".equals(userType) && "2".equals(branchCode)) {
	        	    if(null==imWarehouse){
	        	    	throw new FormException("倉庫代碼：" + imReceiveHead.getDefaultWarehouseCode() + "查無其對應報關倉別！");
	        	    }
	        	    String warehouseEmployeeType = imReceiveHead.getBrandCode() + imWarehouse.getCustomsWarehouseCode() + imReceiveHead.getItemCategory();
	        	    BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("WarehouseManagerByCategory", warehouseEmployeeType);
	        	    if(null==buCommonPhraseLine){
	        	    	throw new FormException("BuCommonPhraseS 查無相關 " + warehouseEmployeeType + " 對應倉管負責人！");
	        	    }else{
		        		String warehouseEmployee = buCommonPhraseLine.getAttribute1();
		        		imReceiveHead.setWarehouseEmployee(warehouseEmployee);
	        	    }
	        	}
	        	
	        	// 進貨退回 && SHIPPING Invoice 以及扣除預算
	        	if("RR".equals(typeCode) && "SHIPPING".equals(userType)){
	        		log.info("進入RR退貨");
	        		updateImReceiveItem2FiInvoice(imReceiveHead, isReject, loginEmployeeCode, userType, buBrand);
	        	}
		
	            // 寄送MAIL給PO單負責人 -> SHIPPING && !REJECT && IMR
	        	//if("SHIPPING".equals(userType) && !isReject && "IMR".equals(typeCode))
	        	//    sendMailToSuperintendent( imReceiveHead );
	        	
	        	//進貨數量寫入poLine.receivedQuantity & fiBudgetLine.receiveActualAmount (SHIPPING && WAREHOUSE)
	        	if("IMR".equals(typeCode) && "PO".equals(orderTypeCondition)) {	// 進貨累計 PO 已進貨數量 && 依採購年月累計預算已進貨金額
	        	    updateImReceiveItem2PoLine( imReceiveHead, isReject, loginEmployeeCode, userType );
	        	}

	        	//倉管送出後 對採購數量與進貨金額做處理
	        	if("WAREHOUSE".equals(userType) && OrderStatus.SIGNING.equals(formStatus)){
	        		//updateImReceive2FiBueget(imReceiveHead, isReject, userType);
	        	}
	        	log.info("進貨數量寫入 imOnHand");
        	    // 進貨數量寫入 imOnHand
            	updateImReceive2ImOnHand( imReceiveHead, isReject, loginEmployeeCode, userType, typeCode );
            	
            	//for 儲位用
        		if(imStorageAction.isStorageExecute(imReceiveHead)){
        			//異動儲位庫存
        			String storageStatus;
        			if(isReject)
        				storageStatus = OrderStatus.SAVE;
        			else
        				storageStatus = OrderStatus.FINISH;
        			
        			if(("SHIPPING".equals(userType) && !isReject) || ("WAREHOUSE".equals(userType) && isReject))
        				imStorageService.updateStorageOnHandBySource(imReceiveHead, storageStatus, PROGRAM_ID, identification, isReject);
        		}
            	
            	// 回寫ImItem
            	if("2".equals(branchCode) && "IMR".equals(typeCode)){
            		updateImReceive2ImItem(imReceiveHead);
            	}
            	
            	// EIF EOF , 進貨數量寫入 cmOnHand		// D7 update declarationDate
            	if("EIF".equals(orderTypeCode)){
            	    updateImReceive2CmOnHand( imReceiveHead, isReject, userType, typeCode);
        	    }else if("EOF".equals(orderTypeCode)){
        	    	updateImReceive2CmOnHand( imReceiveHead, isReject, userType, typeCode );
        	    }

	        	// 倉管 && 非 REJECT && 非進貨退回	 // 產生調撥單
	        	if("WAREHOUSE".equals(userType) && !isReject && "IMR".equals(typeCode) && "2".equals(branchCode) && !OrderStatus.FINISH.equals(warehouseStatus)){//倉庫起一次調撥單(wade)
	        	    createImMovement(imReceiveHead, branchCode, "ACCEPT", loginEmployeeCode);	//產生良品數量調撥單
	        	    createImMovement(imReceiveHead, branchCode, "DEFACT", loginEmployeeCode);	//產生不良品數量調撥單
	        	    createImMovement(imReceiveHead, branchCode, "SAMPLE", loginEmployeeCode);	//產生抽樣數量調撥單
	        	    if("EIF".equals(orderTypeCode)){
	        	    	createImMovement(imReceiveHead, branchCode, "SHORT",  loginEmployeeCode);	//產生短溢數量調撥單
	        	    }
	        	}
            }
            
            // 修改報關單狀態            
            if(null != imReceiveHead.getDeclarationNo() && "EIF".equals(orderTypeCode) 
            		&& ("WAREHOUSE".equals(userType) && OrderStatus.SIGNING.equals(formStatus)) 
            				//|| ("WAREHOUSE".equals(userType) && OrderStatus.REJECT.equals(formStatus)) 
            			){
        		List<CmDeclarationHead> cmDeclarationHeads = 
    				cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", imReceiveHead.getDeclarationNo());
    			if(null!=cmDeclarationHeads && cmDeclarationHeads.size()>0){
    				CmDeclarationHead cmDeclarationHead = cmDeclarationHeads.get(0);
    				if("WAREHOUSE".equals(userType) && !isReject)
    					cmDeclarationHead.setStatus(OrderStatus.FINISH);
    				cmDeclarationHead.setLastUpdateDate(new Date());
    				cmDeclarationHeadDAO.update(cmDeclarationHead);
    			}
    	    }else if(null != imReceiveHead.getDeclarationNo() && "EOF".equals(orderTypeCode) 
    	    		&& ("SHIPPING2".equals(userType) && OrderStatus.SIGNING.equals(formStatus))){
        		List<CmDeclarationHead> cmDeclarationHeads = 
    				cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", imReceiveHead.getDeclarationNo());
    			if(null!=cmDeclarationHeads && cmDeclarationHeads.size()>0){
    				CmDeclarationHead cmDeclarationHead = cmDeclarationHeads.get(0);
					cmDeclarationHead.setStatus(OrderStatus.FINISH);
    				cmDeclarationHead.setLastUpdateDate(new Date());
    				cmDeclarationHeadDAO.update(cmDeclarationHead);
    			}
    	    }

            // T2倉管 or T4NG or T6CO 送出
            if( ("WAREHOUSE".equals(userType)) || "T4NG".equals(imReceiveHead.getBrandCode()) || "T6CO".equals(imReceiveHead.getBrandCode())){
            	//倉管送出
            	if(formStatus.equals(OrderStatus.SIGNING)){
            		imReceiveHead.setWarehouseStatus(OrderStatus.FINISH);
            		
            		//for 儲位用
            		if(imStorageAction.isStorageExecute(imReceiveHead)){
                		//由於進貨單會先新增預設儲位，等到倉管進行驗收完會調整，所以這邊會移除舊的儲位在新增溪的儲位
                		//儲位單庫存還原
            			List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
            			for (Iterator iterator = imReceiveItems.iterator(); iterator.hasNext();) {
							ImReceiveItem imReceiveItem = (ImReceiveItem) iterator.next();
							imStorageService.executeStorageOnhand(ImStorageService.IN, imReceiveHead.getBrandCode(), 
									imReceiveItem.getItemCode(), imReceiveHead.getDefaultWarehouseCode(), 
									ImStorageService.STORAGE_CODE_DEFAULT, ImStorageService.STORAGE_IN_NO_DEFAULT, 
									ImStorageService.STORAGE_LOT_NO_DEFAULT, imReceiveItem.getQuantity() * -1, imReceiveHead.getLastUpdatedBy(), true);
						}
            			
                		ImStorageHead imStorageHead = imStorageService.getImStorageHead(imReceiveHead);
                		imStorageHead.setStatus(OrderStatus.SAVE);
                		
                		//儲位單庫存新增
                		List<ImStorageItem> imStorageItems= imStorageHead.getImStorageItems();
                		for (Iterator iterator = imStorageItems.iterator(); iterator.hasNext();) {
							ImStorageItem imStorageItem = (ImStorageItem) iterator.next();
							
							log.info("item = " + imStorageItem.getItemCode());
							log.info("item.getStorageInNo = " + imStorageItem.getStorageInNo());
							log.info("item.getStorageLotNo = " + imStorageItem.getStorageLotNo());
							log.info("item.getDeliveryWarehouseCode() = " + imStorageItem.getDeliveryWarehouseCode());
							log.info("item.getDeliveryStorageCode = " + imStorageItem.getDeliveryStorageCode());
							log.info("item.getArrivalWarehouseCode() = " + imStorageItem.getArrivalWarehouseCode());
							log.info("item.getArrivalStorageCode = " + imStorageItem.getArrivalStorageCode());
							
							//建立當下如果是IN的話而且是進貨進貨日即為進貨日期
	            			if( ImStorageService.IN.equals(imStorageHead.getStorageTransactionType()) && imStorageItem.getStorageQuantity() > 0 ){
	            				log.info("warehouseInDate = " + imReceiveHead.getWarehouseInDate());
	            				imStorageItem.setStorageInNo(DateUtils.format(imReceiveHead.getWarehouseInDate(), DateUtils.C_DATA_PATTON_YYYYMMDD));
	            			}
						}
                		
                		imStorageService.updateHead(imStorageHead, imReceiveHead.getLastUpdatedBy());
            			
                		imStorageService.updateStorageOnHandBySource(imReceiveHead, OrderStatus.FINISH, PROGRAM_ID, identification, isReject);
            		}
            	}
            	
            	// 倉管駁回 
            	else if(formStatus.equals(OrderStatus.REJECT)){
            		imReceiveHead.setWarehouseStatus(OrderStatus.SAVE);
            	}
            }
            
            // 倉管2 輸入 EXPENSE 後送出 
            if("SHIPPING2".equals(userType) && formStatus.equals(OrderStatus.SIGNING) )
            	imReceiveHead.setExpenseStatus(OrderStatus.FINISH);
            
            // 目前流程中只有船務主管,會計,會計主管可駁回至船務2, 所以將費用狀態回復成 SAVE // 2010.03.04
            if(!"WAREHOUSE".equals(userType) && formStatus.equals(OrderStatus.REJECT) )
            	imReceiveHead.setExpenseStatus(OrderStatus.SAVE);
    		
            imReceiveHead.setStatus(formStatus);
            
            // 最後把變更 status... 寫入 imReceiveHead
            if(OrderStatus.CLOSE.equals(imReceiveHead.getStatus())){
            	List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
            	List<ImTransation> transations = imTransationDAO.findTransationByIdentification(imReceiveHead.getBrandCode(),
            			orderTypeCode, imReceiveHead.getOrderNo());
            	for (int i = 0; i < imReceiveItems.size(); i++) {
            		ImReceiveItem item = imReceiveItems.get(i);
            		ImTransation tran = transations.get(i);
					Double costAmount = new Double(0);
					costAmount = NumberUtils.getDouble(item.getLocalAmount()) + NumberUtils.getDouble(item.getExpenseApportionmentAmount());
					tran.setCostAmount(costAmount);
					imTransationDAO.update(tran);
				}
            }
  
            resultMsg = modifyAjaxImReceive(imReceiveHead, loginEmployeeCode);
            
            resultMap.put("entityBean", imReceiveHead);
            resultMap.put("resultMsg",  resultMsg);
            return resultMap;
	} catch (FormException fe) {
		log.error(fe.getMessage());
	    //siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, fe.getMessage(), errorLogger);
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
		log.error(ex.getMessage());
	    //createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, ex.getMessage(), errorLogger);
	    throw new Exception(ex.getMessage());
	}
    }   
    
    
    /** 依 LC DATA 建立 imReceiveExpense 
     * @param imReceiveHead
     * @param imLetterOfCreditHead
     */
    public void insertImReceiveExpense( ImReceiveHead imReceiveHead, List<ImReceiveExpense> imReceiveExpenses, ImLetterOfCreditHead imLetterOfCreditHead) throws Exception {
    	try{
    		if( NumberUtils.getDouble(imLetterOfCreditHead.getOpeningFees()) > 0){
    			ImReceiveExpense imReceiveExpense = new ImReceiveExpense();
    			imReceiveExpense.setImReceiveHead(imReceiveHead);
    			imReceiveExpense.setExpenseCode("02");
    			imReceiveExpense.setSupplierCode(imLetterOfCreditHead.getOpeningBankCode());
    			imReceiveExpense.setSupplierName(imLetterOfCreditHead.getOpeningBank());
    			imReceiveExpense.setLocalAmount(imLetterOfCreditHead.getOpeningFees());
    			imReceiveExpenses.add(imReceiveExpense);
    		}
    	} catch (Exception ex) {
    		log.error("insertImReceiveExpense : 新增進貨費用存檔時發生錯誤，原因：" + ex.toString());
    		throw new Exception("insertImReceiveExpense : 新增進貨費用 存檔時發生錯誤，原因：" + ex.getMessage());
    	}
    }
    
    
    /** WareHouse 送出依 imReceive 建立 imMovement 
     * @param imReceiveHead
     * @throws Exception
     */ 
    public void createImMovement( ImReceiveHead imReceiveHead, String branchCode, String opType, String employeeCode )
	throws Exception {
	//log.info("createImMovement : " + opType );
	try{
	    String orderTypeCode = imReceiveHead.getOrderTypeCode();
	    BuBrand buBrand = buBrandService.findById( imReceiveHead.getBrandCode() );
	    BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(imReceiveHead.getBrandCode(), orderTypeCode ) );
	    String defaultWarehouseCodes[] = getDefaultWarehouseCodeByBrandCode(buBrand, buOrderType);
	    String defaultWarehouseCode = imReceiveHead.getDefaultWarehouseCode();
	    int warehouseIndex = 0;
	    
	    for (int i = 0; i < defaultWarehouseCodes.length; i++) {
			if(defaultWarehouseCode.equals(defaultWarehouseCodes[i])){
				warehouseIndex = i;
				break;
			}
		}
	    log.info("OP_TYPE = "+opType+" warehouseIndex = " + warehouseIndex);
	    
	    ImMovementHead imMovementHead = new ImMovementHead();
	    if("EIF".equals(orderTypeCode)) {
			imMovementHead.setDeliveryWarehouseCode(defaultWarehouseCode);		// F待驗倉
			if("ACCEPT".equals(opType)){
				
				if(warehouseIndex >= buBrand.getDefaultWarehouseCode2().split(",").length)
					throw new Exception("單別: "+orderTypeCode+"，進貨庫別: " +defaultWarehouseCode + "查無對應之正貨倉");
			    imMovementHead.setArrivalWarehouseCode( buBrand.getDefaultWarehouseCode2().split(",")[warehouseIndex]);	// F正貨倉
			}else if("DEFACT".equals(opType)){
				if(warehouseIndex >= buBrand.getDefaultWarehouseCode5().split(",").length)
					throw new Exception("單別: "+orderTypeCode+"，進貨庫別: " +defaultWarehouseCode + "查無對應之不良倉");
			    imMovementHead.setArrivalWarehouseCode( buBrand.getDefaultWarehouseCode5().split(",")[warehouseIndex]);	// F不良倉
			}else if("SAMPLE".equals(opType)){
				if(warehouseIndex >= buBrand.getDefaultWarehouseCode6().split(",").length)
					throw new Exception("單別: "+orderTypeCode+"，進貨庫別: " +defaultWarehouseCode + "查無對應之樣品倉");
			    imMovementHead.setArrivalWarehouseCode( buBrand.getDefaultWarehouseCode6().split(",")[warehouseIndex]);	// F樣品倉
			}else if("SHORT".equals(opType)){
				if(warehouseIndex >= buBrand.getDefaultWarehouseCode4().split(",").length)
					throw new Exception("單別: "+orderTypeCode+"，進貨庫別: " +defaultWarehouseCode + "查無對應之短溢倉");
			    imMovementHead.setArrivalWarehouseCode( buBrand.getDefaultWarehouseCode4().split(",")[warehouseIndex]);	// F短溢倉
			}
	    }else if("EIP".equals(orderTypeCode)){
			imMovementHead.setDeliveryWarehouseCode(defaultWarehouseCode);		// P待驗倉
			if("ACCEPT".equals(opType)){
				log.info("ACCEPT:"+defaultWarehouseCode);
				if(warehouseIndex >= buBrand.getDefaultWarehouseCode21().split(",").length)
					throw new Exception("進貨庫別: " +defaultWarehouseCode + "查無對應之正貨倉");
			    imMovementHead.setArrivalWarehouseCode( buBrand.getDefaultWarehouseCode21().split(",")[warehouseIndex]);	// P正貨倉
			}else if("DEFACT".equals(opType)){
				log.info("DEFACT:"+defaultWarehouseCode);
				if(warehouseIndex >= buBrand.getDefaultWarehouseCode51().split(",").length)
					throw new Exception("單別: "+orderTypeCode+"，進貨庫別: " +defaultWarehouseCode + "查無對應之不良倉");
			    imMovementHead.setArrivalWarehouseCode( buBrand.getDefaultWarehouseCode51().split(",")[warehouseIndex]);	// P不良倉
			}else if("SAMPLE".equals(opType)){
				log.info("SAMPLE:"+defaultWarehouseCode);
				if(warehouseIndex >= buBrand.getDefaultWarehouseCode61().split(",").length)
					throw new Exception("單別: "+orderTypeCode+"，進貨庫別: " +defaultWarehouseCode + "查無對應之樣品倉");
	    		imMovementHead.setArrivalWarehouseCode( buBrand.getDefaultWarehouseCode61().split(",")[warehouseIndex]);	// P樣品倉
			}else if("SHORT".equals(opType)){
				log.info("SHORT:"+defaultWarehouseCode);
				if(warehouseIndex >= buBrand.getDefaultWarehouseCode41().split(",").length)
					throw new Exception("單別: "+orderTypeCode+"，進貨庫別: " +defaultWarehouseCode + "查無對應之短溢倉");
			    imMovementHead.setArrivalWarehouseCode( buBrand.getDefaultWarehouseCode41().split(",")[warehouseIndex]);	// F短溢倉
			}
	    }else{
			imMovementHead.setDeliveryWarehouseCode(buBrand.getDefaultWarehouseCode1());		// 待驗倉
			if("ACCEPT".equals(opType)){
			    imMovementHead.setArrivalWarehouseCode( buBrand.getDefaultWarehouseCode2());	// 正貨倉
			}else if("DEFACT".equals(opType)){
			    imMovementHead.setArrivalWarehouseCode( buBrand.getDefaultWarehouseCode5());	// 不良倉
			}else if("SAMPLE".equals(opType)){
			    imMovementHead.setArrivalWarehouseCode( buBrand.getDefaultWarehouseCode6());	// 樣品倉
			}else if("SHORT".equals(opType)){
			    imMovementHead.setArrivalWarehouseCode( buBrand.getDefaultWarehouseCode4());	// 短溢倉
			}
	    }
	    
	    List<ImMovementItem> imMovementItems = imMovementHead.getImMovementItems();
	    //log.info("imMovementItems.size1=" +imMovementItems.size() );
	    List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
	    Long indexNo     = 1L;
	    Double itemCount = 0D;
	    for(ImReceiveItem imReceiveItem : imReceiveItems ){
			Double quantity = 0D;
			if("ACCEPT".equals(opType) && imReceiveItem.getAcceptQuantity()>0){
			    quantity = imReceiveItem.getAcceptQuantity();
			}else if("DEFACT".equals(opType) && imReceiveItem.getDefectQuantity()>0){
			    quantity = imReceiveItem.getDefectQuantity();
			}else if("SAMPLE".equals(opType) && imReceiveItem.getSampleQuantity()>0){
			    quantity = imReceiveItem.getSampleQuantity();
			}else if("SHORT".equals(opType) && imReceiveItem.getShortQuantity()>0){
			    quantity = imReceiveItem.getShortQuantity();
			}
			itemCount += quantity;
			if( quantity != 0D){
			    ImMovementItem imMovementItem = new ImMovementItem();
			    imMovementItem.setIndexNo(indexNo);
			    imMovementItem.setLotNo(                   imReceiveHead.getLotNo());
			    imMovementItem.setItemCode(                imReceiveItem.getItemCode());
			    imMovementItem.setOriginalDeliveryQuantity(quantity);
			    imMovementItem.setDeliveryWarehouseCode(   imMovementHead.getDeliveryWarehouseCode());
			    imMovementItem.setDeliveryQuantity(        quantity);
			    imMovementItem.setArrivalWarehouseCode(    imMovementHead.getArrivalWarehouseCode());
			    imMovementItem.setArrivalQuantity(         quantity);
			    imMovementItem.setCreatedBy(     employeeCode);
			    imMovementItem.setLastUpdatedBy( employeeCode);
			    imMovementItem.setCreationDate(  DateUtils.parseDate(DateUtils.format(new Date())));
			    imMovementItem.setLastUpdateDate(DateUtils.parseDate(DateUtils.format(new Date())));
			    imMovementItems.add(imMovementItem);
			    indexNo++;
			}
	    }
	    
	    //log.info("imMovementItems.size2=" +imMovementItems.size() );
	    if(imMovementItems.size()>0 ){
			if("EIF".equals(orderTypeCode)){
			    imMovementHead.setOrderTypeCode("WCF");	//保稅倉 / WWF
			}else if("EIP".equals(orderTypeCode)){
			    imMovementHead.setOrderTypeCode("WCP");	//完稅倉 / WWP
			}else{
			    imMovementHead.setOrderTypeCode("IMV");	//T1
			}
	
			ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(
				imReceiveHead.getBrandCode(), imMovementHead.getDeliveryWarehouseCode(), "Y");
			if(null!=imWarehouse){
			    imMovementHead.setDeliveryWarehouseName(imWarehouse.getWarehouseName());
			    imMovementHead.setDeliveryContactPerson(imWarehouse.getWarehouseManager());
			    BuLocation buLocation = buLocationService.findById(imWarehouse.getLocationId());
			    if(null!=buLocation){
				imMovementHead.setDeliveryAddress(buLocation.getAddress());
				imMovementHead.setDeliveryArea(buLocation.getArea());
				imMovementHead.setDeliveryCity(buLocation.getCity());
				imMovementHead.setDeliveryZipCode(buLocation.getZipCode());
			    }
			}
	
			imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(
				imReceiveHead.getBrandCode(), imMovementHead.getArrivalWarehouseCode(), "Y");
			if(null!=imWarehouse){
			    imMovementHead.setArrivalWarehouseName(imWarehouse.getWarehouseName());
			    imMovementHead.setArrivalContactPerson(imWarehouse.getWarehouseManager());
			    BuLocation buLocation = buLocationService.findById(imWarehouse.getLocationId());
			    if(null!=buLocation){
				imMovementHead.setArrivalAddress(buLocation.getAddress());
				imMovementHead.setArrivalArea(buLocation.getArea());
				imMovementHead.setArrivalCity(buLocation.getCity());
				imMovementHead.setArrivalZipCode(buLocation.getZipCode());
			    }
			}
	
			imMovementHead.setOriginalOrderTypeCode(imReceiveHead.getOrderTypeCode());
			imMovementHead.setOriginalOrderNo(      imReceiveHead.getOrderNo());
			imMovementHead.setBrandCode(            imReceiveHead.getBrandCode());
	
			String orderNo = 
			    buOrderTypeService.getOrderSerialNo(imMovementHead.getBrandCode(), imMovementHead.getOrderTypeCode());
			buOrderType = 
			    buOrderTypeService.findById( new BuOrderTypeId(imMovementHead.getBrandCode(), imMovementHead.getOrderTypeCode() ) );
			if( null==buOrderType )
			    throw new FormException("無此 orderTypeCode : " + imMovementHead.getBrandCode()+"-"+imMovementHead.getOrderTypeCode() );
			imMovementHead.setItemCategory(         imReceiveHead.getItemCategory());
			imMovementHead.setOrderNo(              orderNo);
			imMovementHead.setTaxType(              buOrderType.getTaxCode());
			imMovementHead.setItemCount(            itemCount);
			imMovementHead.setDeliveryDate(    DateUtils.parseDate(DateUtils.format(new Date())));
			imMovementHead.setCreatedBy(     employeeCode);
			imMovementHead.setLastUpdatedBy( employeeCode);
			imMovementHead.setCreationDate(  DateUtils.parseDate(DateUtils.format(new Date())));
			imMovementHead.setLastUpdateDate(DateUtils.parseDate(DateUtils.format(new Date())));
			
			if("DEFACT".equals(opType)){	// 2010.01.25 for Anber Requirement
			    imReceiveHead.setDefectMovOrderTypeCode( imMovementHead.getOrderTypeCode() );
			    imReceiveHead.setDefectMovOrderNo(       imMovementHead.getOrderNo() );
			}else if("SAMPLE".equals(opType)){
			    imReceiveHead.setSampleMovOrderTypeCode( imMovementHead.getOrderTypeCode() );
			    imReceiveHead.setSampleMovOrderNo(       imMovementHead.getOrderNo() );
			}else if("SHORT".equals(opType)){
			    imReceiveHead.setShortMovOrderTypeCode( imMovementHead.getOrderTypeCode() );
			    imReceiveHead.setShortMovOrderNo(       imMovementHead.getOrderNo() );
			}
			
			//log.info("imMovementHead.setOrderNo="+imMovementHead.getOrderNo());
			imMovementHeadDAO.save(imMovementHead);
	    }
	    
	} catch (Exception ex) {
	    log.error("createImMovement : 新增進貨調撥單時發生錯誤，原因：" + ex.toString());
	    throw new Exception("createImMovement : 新增進貨調撥單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    
    /**產生 FiInvoice
     * @param imReceiveHead
     * @param branchCode
     * @param opType
     * @throws Exception
     */
    public void insertFiInvoice( ImReceiveHead imReceiveHead, String branchCode )
	throws Exception {
	//log.info("insertFiInvoice");
	String orderTypeCode = null;
	if("RRF".equals(imReceiveHead.getOrderTypeCode())){
	    orderTypeCode = "IIF";
	}else if("RRL".equals(imReceiveHead.getOrderTypeCode())){
	    orderTypeCode = "IIL";
	}else if("EOF".equals(imReceiveHead.getOrderTypeCode())){
	    orderTypeCode = "IEF";
	}else if("EOP".equals(imReceiveHead.getOrderTypeCode())){
	    orderTypeCode = "IEP";
	}
	try{
	    Double totalForeignInvoiceAmount = 0D;
	    Double totalLocalInvoiceAmount   = 0D;
	    FiInvoiceHead fiInvoiceHead = new FiInvoiceHead();
	    fiInvoiceHead.setOrderTypeCode(orderTypeCode);
	    fiInvoiceHead.setBrandCode(imReceiveHead.getBrandCode());
	    fiInvoiceHead.setInvoiceNo(imReceiveHead.getOrderTypeCode()+imReceiveHead.getOrderNo());
	    fiInvoiceHead.setSupplierCode(imReceiveHead.getSupplierCode());
	    fiInvoiceHead.setSupplierName(imReceiveHead.getSupplierName());
	    fiInvoiceHead.setExchangeRate(imReceiveHead.getExchangeRate());
	    fiInvoiceHead.setCurrencyCode(imReceiveHead.getCurrencyCode());
	    fiInvoiceHead.setInvoiceDate( imReceiveHead.getOrderDate());
	    fiInvoiceHead.setCustomsSeq(1L); 
	    fiInvoiceHead.setFinanceConfirm("N");	// 需輸入其他資料
	    fiInvoiceHead.setCreatedBy(     imReceiveHead.getLastUpdatedBy());
	    fiInvoiceHead.setLastUpdatedBy( imReceiveHead.getLastUpdatedBy());
	    fiInvoiceHead.setCreationDate(  imReceiveHead.getLastUpdateDate());
	    fiInvoiceHead.setLastUpdateDate(imReceiveHead.getLastUpdateDate());
	    Long indexNo = 1L;
	    List <FiInvoiceLine> fiInvoiceLines = fiInvoiceHead.getFiInvoiceLines();
	    for(ImReceiveItem imReceiveItem : imReceiveHead.getImReceiveItems() ){
		totalForeignInvoiceAmount += imReceiveItem.getQuantity() * imReceiveItem.getForeignUnitPrice();
		totalLocalInvoiceAmount   += imReceiveItem.getQuantity() * imReceiveItem.getLocalUnitPrice();
		FiInvoiceLine fiInvoiceLine = new FiInvoiceLine();
		fiInvoiceLine.setIndexNo(  indexNo);
		fiInvoiceLine.setCustomSeq(indexNo);
		fiInvoiceLine.setItemCode(        imReceiveItem.getItemCode());
		fiInvoiceLine.setItemCName(       imReceiveItem.getItemCName());
		fiInvoiceLine.setQuantity(        imReceiveItem.getQuantity()*(-1));
		fiInvoiceLine.setLocalUnitPrice(  imReceiveItem.getLocalUnitPrice());
		fiInvoiceLine.setLocalAmount(     imReceiveItem.getLocalAmount()*(-1));
		fiInvoiceLine.setForeignUnitPrice(imReceiveItem.getForeignUnitPrice());
		fiInvoiceLine.setForeignAmount(   imReceiveItem.getForeignAmount()*(-1));
		fiInvoiceLine.setShippingMark(    imReceiveItem.getShippingMark());
		fiInvoiceLine.setWeight(          imReceiveItem.getWeight());
		fiInvoiceLine.setBrandCode(       imReceiveHead.getBrandCode());
		fiInvoiceLine.setCreatedBy(       imReceiveHead.getLastUpdatedBy());
		fiInvoiceLine.setLastUpdatedBy(   imReceiveHead.getLastUpdatedBy());
		fiInvoiceLine.setCreationDate(    DateUtils.parseDate(DateUtils.format(new Date())));
		fiInvoiceLine.setLastUpdateDate(  DateUtils.parseDate(DateUtils.format(new Date())));
		fiInvoiceLine.setOriginalDeclarationNo(imReceiveItem.getOriginalDeclarationNo());
		fiInvoiceLine.setOriginalDeclarationSeq(imReceiveItem.getOriginalDeclarationSeq());
		fiInvoiceLines.add(fiInvoiceLine);
		indexNo++;
	    }
	    fiInvoiceHead.setTotalForeignInvoiceAmount(totalForeignInvoiceAmount);
	    fiInvoiceHead.setTotalLocalInvoiceAmount(  totalLocalInvoiceAmount);
	    fiInvoiceHeadDAO.save(fiInvoiceHead);
	} catch (Exception ex) {
	    log.error("insertFiInvoice : 新增進貨退出 INVOICE 時發生錯誤，原因：" + ex.toString());
	    throw new Exception("insertFiInvoice : 新增進貨退出 INVOICE 存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    
    /** WAREHOUSE call updateAJAXImReceive 產生 imMoveMent 時 imMovementHead.ststus = null 預防重複 RUN FLOW
     * @param imReceiveHead
     * @throws Exception
     */
    public void updateAJAXStartOtherFlow(ImReceiveHead imReceiveHead, String useType) throws Exception {
	if("WAREHOUSE".equals(useType)){	// 啟動調撥單 FLOW
	    List<ImMovementHead> imMovementHeads = 
		imMovementHeadDAO.findByProperty("ImMovementHead", "originalOrderNo", imReceiveHead.getOrderNo());
	    if( null!=imMovementHeads && imMovementHeads.size()>0 ){
		for( ImMovementHead imMovementHead : imMovementHeads ){
		    if(null==imMovementHead.getStatus()){
			imMovementHead.setStatus(OrderStatus.SAVE);
			imMovementHeadDAO.update(imMovementHead);
			ImMovementService.startProcess(imMovementHead);
		    }
		}
	    }
	}else if("SHIPPING".equals(useType)){	// 啟動 INVOICE FLOW
	    String invoiceNo = imReceiveHead.getOrderTypeCode()+imReceiveHead.getOrderNo();
	    //log.info(invoiceNo);
	    List<FiInvoiceHead> fiInvoiceHeads = 
		fiInvoiceHeadDAO.findByProperty("FiInvoiceHead", "invoiceNo", invoiceNo);
	    if( null!=fiInvoiceHeads && fiInvoiceHeads.size()>0 ){
		for( FiInvoiceHead fiInvoiceHead : fiInvoiceHeads ){
		    if(null==fiInvoiceHead.getStatus()){
			fiInvoiceHead.setStatus(OrderStatus.SAVE);
			fiInvoiceHeadDAO.update(fiInvoiceHead);
			FiInvoiceHeadMainService.startProcess(fiInvoiceHead);
		    }
		}
	    }
	}
    }
    
    
    /**檢核 ImReceive 資料
     * @param imReceiveHead
     * @param conditionMap
     * @return List
     * @throws ValidationErrorException
     */
    public List updateCheckedImReceiveData(Map parameterMap) throws Exception {
	//log.info("updateCheckedImReceiveData");
	List errorMsgs = new ArrayList(0);
	try{
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Object otherBean    = parameterMap.get("vatBeanOther");
            String typeCode     = (String)PropertyUtils.getProperty(otherBean, "typeCode");
            String userType     = (String)PropertyUtils.getProperty(otherBean, "userType");
            Long headId = getImReceiveHeadId(formLinkBean);
            
            ImReceiveHead imReceivePO = getActualImReceive(headId);
            BuBrand buBrand    = buBrandService.findById( imReceivePO.getBrandCode() );
    	    String  branchCode = buBrand.getBranchCode();	// 是否 T2
            String identification =
        	MessageStatus.getIdentification(imReceivePO.getBrandCode(), imReceivePO.getOrderTypeCode(), imReceivePO.getOrderNo() );
            
            // clear 原有 ERROR RECORD	2010.01.25 改由 ImReceiveAction Call 
            // siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
            
            //只有在船務送出的時候 才檢查相關的主檔資料
            if("SHIPPING".equals(userType)){
	            // check imReceiveHead Data
	            checkImReceiveHead( imReceivePO, branchCode, identification, errorMsgs, typeCode);
	            // check imReceiveItem data
	            checkImReceiveItems(imReceivePO, branchCode, identification, errorMsgs, typeCode);
            }else if ("WAREHOUSE".equals(userType)){
            	checkImReceiveHeadForWarehouse( imReceivePO, branchCode, identification, errorMsgs, typeCode);
            }else if ("SHIPPING2".equals(userType)){
            	checkImReceiveHead( imReceivePO, branchCode, identification, errorMsgs, typeCode);
            	// check imReceiveExpense data
                checkImReceiveExpenses(imReceivePO, branchCode, identification, errorMsgs );	
            }
            
	    return errorMsgs;
	}catch (Exception ex) {
	    log.error("進貨單檢核後存檔失敗，原因：" + ex.toString());
	    throw ex;
	}
    }

    
    /** 檢核進貨單主檔資料
     * @param imReceiveHead
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    public void checkImReceiveHead(ImReceiveHead imReceiveHead, String branchCode, String identification, List errorMsgs, String typeCode) 
        throws Exception{
    	log.info("checkImReceiveHead BranchCode=" + branchCode +"*");
    	String tabName = "進貨單主檔資料";
    	String orderTypeCode = imReceiveHead.getOrderTypeCode();
    	String errorLogger   = imReceiveHead.getLastUpdatedBy();
    	BuBrand buBrand      = buBrandService.findById( imReceiveHead.getBrandCode() );
    	BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(imReceiveHead.getBrandCode(), imReceiveHead.getOrderTypeCode()));
    	List<ImReceiveHead> imReceive = 
			imReceiveHeadDAO.findByProperty("ImReceiveHead", " and headId = ?  and warehouseStatus = 'FINISH' ", new Object[]{imReceiveHead.getHeadId()});
		
    	try{
    		if (null == imReceiveHead.getOrderDate()){
    			if ("IRF".equals(orderTypeCode) || "IRL".equals(orderTypeCode)){ 	//|| "EIF".equals(orderTypeCode)	// 2010.02.25 USER ELLEN REQUIRE
    				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入歸帳日期！", errorLogger);
    			}else{
    				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入日期！", errorLogger);
    			}
    		}
    		if (!StringUtils.hasText(imReceiveHead.getSupplierCode()))
    			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入廠商代號！", errorLogger);

    		BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(imReceiveHead.getBrandCode(), imReceiveHead.getSupplierCode());
    		if (null==buSWAV){
    			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, tabName+"查無此廠商代號！", errorLogger);
    		}
    		
    		//如果單別設定中  判斷為PO的才檢查有無發票
    		if (!StringUtils.hasText(imReceiveHead.getDefPoOrderNo()) && "IRF".equals(orderTypeCode) 
    				&& "PO".equals(buOrderType.getOrderCondition()))  // IRF
    			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入 Invoice No！", errorLogger);

    		if (null == imReceiveHead.getItemCategory() && "2".equals( branchCode ) )
    			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入業種！", errorLogger);
    		
    		if ( "EIF".equals(orderTypeCode) || ("EOF".equals(orderTypeCode) && imReceive.size()>0)){// EIF
    			if (!StringUtils.hasText(imReceiveHead.getDeclarationNo()) )
    				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入報單單號！", errorLogger);
    			String declarationNo = imReceiveHead.getDeclarationNo();
    			List<ImReceiveHead> imReceiveHeads = 
    				imReceiveHeadDAO.findByProperty("ImReceiveHead", " and declarationNo = ? and orderNo not like 'TMP%' and status not in ('SAVE','VOID') ", new Object[]{declarationNo});
    			if(null!=imReceiveHeads && imReceiveHeads.size()>0){
    				if(imReceiveHeads.size()>1 || imReceiveHead.getHeadId()!=imReceiveHeads.get(0).getHeadId() )
    					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "報單單號："+declarationNo+"重複輸入！(已存在於其他進貨單)", errorLogger);
    			}
    		}
    		
    		//檢查稅別稅率(wade)
    		if(!imReceiveHead.isCorrectTaxRate())
    			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請檢察稅率是否正確(稅別: " + imReceiveHead.getTaxTypeName() + ", 稅率: " + imReceiveHead.getTaxRate() + ")", errorLogger);
    		
    		//如果單別設定中  判斷為PO的才檢查預算
    		if(!StringUtils.hasText(imReceiveHead.getBudgetYear()) && "RR".equals(typeCode) && "PO".equals(buOrderType.getOrderCondition()))
    			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入預算年度！", errorLogger);

    		if(!StringUtils.hasText(imReceiveHead.getBudgetMonth()) && "M".equals(buBrand.getBudgetCheckType()) // 預算扣除類別 Y:year/M:month
    	             &&	"RR".equals(typeCode) && "PO".equals(buOrderType.getOrderCondition()))					// 退貨
    			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入預算月份！", errorLogger);

    		if("RR".equals(typeCode)){
    			String categoryType = null;
    			List<ImItemCategory> allItemCategory = imItemCategoryService.findByCategoryType(imReceiveHead.getBrandCode(), "ITEM_CATEGORY");
    			for(ImItemCategory imItemCategory : allItemCategory ){
    				if( imReceiveHead.getItemCategory().equals( imItemCategory.getId().getCategoryCode() ) ){
    					//categoryType = imItemCategory.getParentCategoryCode();
    					categoryType = imItemCategory.getId().getCategoryCode();
    					break;
    				}
    			}
    			HashMap findObjs = new HashMap();
    			findObjs.put("brandCode",     imReceiveHead.getBrandCode());
    			findObjs.put("categoryType",  categoryType);
    			findObjs.put("orderTypeCode", FiBudgetHead.BUDGET_ORDER_TYPE_CODE_PO);
    			findObjs.put("budgetYear",    imReceiveHead.getBudgetYear());
    			if("M".equals(buBrand.getBudgetCheckType())){	// 預算扣除類別 Y:year/M:month
    				findObjs.put("budgetMonth",  imReceiveHead.getBudgetMonth());
    			}
    			List<FiBudgetHead> budgets = fiBudgetHeadService.find(findObjs);
    			if (null==budgets || budgets.size()==0){
    				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請先建立預算資料！", errorLogger);
    			}
    		}
    		log.info("進貨驗收檢察");
    		//* 原程式有檢查關帳日期
    		String dateType = "進貨日期"; 
    		if(!"2".equals(buBrand.getBranchCode()) && null!=imReceiveHead.getOrderDate() ){
    			ValidateUtil.isAfterClose(imReceiveHead.getBrandCode(), imReceiveHead.getOrderTypeCode(), dateType, imReceiveHead.getOrderDate(),"PO");
    		}
    		/*if("2".equals(buBrand.getBranchCode()) && null!=imReceiveHead.getReceiptDate() && imReceiveHead.getWarehouseStatus() != OrderStatus.FINISH){
    			log.info("warehouseStatus=?"+imReceiveHead.getWarehouseStatus());
    			ValidateUtil.isAfterClose(imReceiveHead.getBrandCode(), imReceiveHead.getOrderTypeCode(), dateType, imReceiveHead.getReceiptDate());
    			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "檢核是否小於關帳日或月時發生錯誤，原因：進貨日期必須大於關帳日期"+imReceiveHead.getReceiptDate(), errorLogger);
    		}*/
    	}catch(Exception ex){
    		log.error( "checkHead : " + ex.getMessage() );
    		//siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, ex.getMessage(), imReceiveHead.getLastUpdatedBy());
    		throw ex;
    	}
    }
    
    /** 檢核進貨單主檔資料(倉管)
     * @param imReceiveHead
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    public void checkImReceiveHeadForWarehouse(ImReceiveHead imReceiveHead, String branchCode, String identification, List errorMsgs, String typeCode) 
    throws Exception{
    	//log.info("checkImReceiveHeadForWarehouse BranchCode=" + branchCode +"*");
    	String errorLogger   = imReceiveHead.getLastUpdatedBy();
    	try{
    		if ("2".equals(branchCode)){
    			if (null == imReceiveHead.getWarehouseInDate())
    				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入進倉日期！", errorLogger);
    		}
    		BuBrand buBrand    = buBrandService.findById( imReceiveHead.getBrandCode() );
    		// check imReceiveHead Data
 		if("2".equals(buBrand.getBranchCode()) && null!=imReceiveHead.getReceiptDate() && imReceiveHead.getWarehouseStatus() != OrderStatus.FINISH){
 			log.info("warehouseStatus=?"+imReceiveHead.getWarehouseStatus());
 			String dateType = "驗收日期";  
 			ValidateUtil.isAfterClose(imReceiveHead.getBrandCode(), imReceiveHead.getOrderTypeCode(), dateType, imReceiveHead.getReceiptDate(),"PO");
 			//siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "檢核是否小於關帳日或月時發生錯誤，原因：驗收日期必須大於關帳日期"+buBrand.getDailyCloseDate(), errorLogger);
 			//throw new Exception("檢核是否小於關帳日或月時發生錯誤，原因：進貨日期必須大於關帳日期"+imReceiveHead.getReceiptDate());
 			}
    	}catch(Exception ex){
    		log.error( "checkImReceiveHeadForWarehouse : " + ex.getMessage() );
    		//siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, ex.getMessage(), imReceiveHead.getLastUpdatedBy());
    		throw ex;
    	}
    }
    
    /** check imReceiveItem
     * @param ImReceiveHead
     * @param programId
     * @param identification
     * @param errorMsgs
     */
    public void checkImReceiveItems( ImReceiveHead imReceiveHead, String branchCode, String identification, List errorMsgs, String typeCode) throws Exception{
    	log.info("checkImReceiveItems");
    	String message       = null;
    	String tabName       = "明細資料頁籤";
    	String itemCategory  = imReceiveHead.getItemCategory() ;
    	String brandCode     = imReceiveHead.getBrandCode();
    	String orderTypeCode = imReceiveHead.getOrderTypeCode();
    	String errorLogger   = imReceiveHead.getLastUpdatedBy();
    	BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(imReceiveHead.getBrandCode(), orderTypeCode ) );
    	String orderCondition = buOrderType.getOrderCondition();
    	
    	// EIF -> T2, 保稅, 進貨  -> 檢核報單明細與進貨明細是否符合
    	if("EIF".equals(orderTypeCode)){	    
    		log.info("checkImReceiveItems VS CmDeclarationItem");
    		String declarationNo = imReceiveHead.getDeclarationNo();
    		try{
    			CmDeclarationHead cmDeclarationHead = null;
    			List<CmDeclarationHead> cmDeclarationHeads = 
    				cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", declarationNo);
    			//log.info("DeclarationNo=" + declarationNo + "Count=" + cmDeclarationHeads.size());
    			if(null!=cmDeclarationHeads && cmDeclarationHeads.size()>0){
    				cmDeclarationHead = cmDeclarationHeads.get(0);
    				String declType   = cmDeclarationHead.getDeclType();
    				if( !"D7".equals(declType) && !"D8".equals(declType) && !"D1".equals(declType) && !"B2".equals(declType)){
    					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, declarationNo+" 報單資料非 D7,D8,D1,B2報單！", errorLogger);
    				}else if( !OrderStatus.WAIT_IN.equals(cmDeclarationHead.getStatus())){
    					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, declarationNo+" 報單狀態非 "+OrderStatus.getChineseWord(OrderStatus.WAIT_IN), errorLogger);
    				}else{
    					//如果報關單沒問題，再檢查其他項次
    	    			List<CmDeclarationItem> cmItems = cmDeclarationHead.getCmDeclarationItems();
    	    			List<ImReceiveItem>     imItems = imReceiveHead.getImReceiveItems();
    	    			//log.info("cmDeclarationItems Count="+cmItems.size()+"-imReceiveItems Count="+imItems.size());
    	    			if(null == cmItems || null == imItems || cmItems.size()!=imItems.size()){
    	    				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "報單明細項目與進貨明細項目不符合, 敬請檢查！ ", errorLogger);
    	    			}else{
    	    				for( int i=0; i < cmItems.size(); i++ ){
    	    					if(null == imItems.get(i).getOriginalDeclarationDate()){
    	    						siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "報單明細項目原進口日期未填, 敬請檢查！ ", errorLogger);
    	    					}
    	    				}
    	    			}
    				}
    			} else {
    				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, declarationNo+" : 查無此報單資料！", errorLogger);
    			}
    		}catch(Exception ex){
    			log.error( "checkReceiveItem with declarationItem: " + ex.getMessage() );
    			//siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, ex.getMessage(), errorLogger);
    			throw ex;
    		}
    	}
    	
    	try{
    		int intactRecordCount = 0;
    		int i = 0;
    		for(ImReceiveItem imReceiveItem : imReceiveHead.getImReceiveItems()){
    			i++;
    			if(!"1".equals(imReceiveItem.getIsDeleteRecord())){
    				String itemCode      = imReceiveItem.getItemCode();
    				String poLineId      = imReceiveItem.getReserve3();//採購欄位回寫增poLineId-Jerome
    				
    				if(!StringUtils.hasText(itemCode)){
    					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "第"+i+"項明細未輸入品號！", errorLogger);
    					continue;
    				}
    				ImItem imItem = imItemService.findItem(brandCode, itemCode);
    				if(null == imItem){
    					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "查無第"+i+"項明細(品號"+itemCode+")之商品主檔！", errorLogger);
    					continue;
    				}
					intactRecordCount++;
					
    				String itemCodeCategory = null==imItem? null:imItem.getItemCategory();
    				String isTax            = null==imItem? null:imItem.getIsTax() ;

    				if(!StringUtils.hasText(itemCode))
    					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入"+tabName+"中第"+i+"項明細的品號！", errorLogger);

    				if( null!=itemCodeCategory && !itemCodeCategory.equals(itemCategory) && "2".equals( branchCode ) )
    					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, tabName+"中第"+i+"項明細的業種與訂購單主檔不符合！", errorLogger);

    				if( null!=isTax && !isTax.equals(buOrderType.getTaxCode()) && "2".equals( branchCode )  ) 	// 判斷稅別
    					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, tabName+"中第"+i+"項明細的稅別不符合！", errorLogger);

    				if(!ValidateUtil.isEnglishAlphabetOrNumber(itemCode))
    					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, tabName+"中第"+i+"項明細的品號必須為A~Z、a~z、0~9！", errorLogger);

    				Double quantity      = imReceiveItem.getQuantity();
    				
    				if(quantity == null || (quantity == 0D && !"EIF".equals(orderTypeCode)) )	//數量不可以是零,保稅進貨除外 2012.01.04
    					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入"+tabName+"中第"+i+"項明細的數量！", errorLogger);

    				//如果是保稅退貨 才檢查報關號碼
    				if( "RR".equals(typeCode) && !StringUtils.hasText(imReceiveItem.getOriginalDeclarationNo()) && "EOF".equals(orderTypeCode) )
    					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, tabName+"中第"+i+"項明細查無相對應原進貨資料！", errorLogger);

    				if("RR".equals(typeCode)){
    					if(quantity > 0)
    						siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, tabName+"中第"+i+"項明細數量不可填寫負數！", errorLogger);
    					Double onHandQty = imOnHandDAO.getCurrentStockOnHandQty("TM", imReceiveItem.getItemCode(), 
    							imReceiveHead.getDefaultWarehouseCode(), imReceiveItem.getLotNo(), imReceiveHead.getBrandCode());
    					if(null == onHandQty || quantity + onHandQty < 0)
    						siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, tabName+"中第"+i+"項明細商品可用退貨數量不足！", errorLogger);
    				}

    				/*===================================================*/
    				Long poPurchaseOrderHeadId = null;
    				
    				//如果單別有做採購才做判斷
    				if("PO".equals(orderCondition)){
    					//國外 保稅 驗證明細的發票是否存在，且發票是否有對應採購單
    					if("IRF".equals(orderTypeCode) || "EIF".equals(orderTypeCode)){ // IRF,EIF -> 進貨
    						if(!StringUtils.hasText(imReceiveItem.getInvoiceNo())){
    							siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入"+tabName+"中第"+i+"項明細發票編號！", errorLogger);
    						}else{
    							FiInvoiceHead fiInvoiceHead = null;			// check invoice 是否存在
    							List<FiInvoiceHead> fiInvoiceHeads =
    								fiInvoiceHeadDAO.findByProperty("FiInvoiceHead", " and invoiceNo = ? and status = 'FINISH' ", new Object[]{imReceiveItem.getInvoiceNo()});
    							if(null==fiInvoiceHeads || fiInvoiceHeads.size()<1){
    								siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
    										tabName+"中第"+i+"項明細發票編號:"+imReceiveItem.getInvoiceNo()+" 不存在！", errorLogger);
    							}else{
    								fiInvoiceHead = fiInvoiceHeads.get(0);
    								if("2".equals(branchCode)){	// T2 check purchase order 是否存在
    									HashMap findObj = new HashMap();
    									findObj.put("headId",   fiInvoiceHead.getHeadId());
    									findObj.put("itemCode", itemCode);
    									List<FiInvoiceLine> fiLines = fiInvoiceLineMainService.find(findObj);
    									if( null!=fiLines){
    										FiInvoiceLine fiInvoiceLine = fiLines.get(0);
    										poPurchaseOrderHeadId = fiInvoiceLine.getPoPurchaseOrderHeadId();
    									}else{
    										siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
    												tabName+"中第"+i+"項明細發票編號:"+imReceiveItem.getInvoiceNo()+
    												", 無此品號:"+itemCode+"!"+fiInvoiceHead.getHeadId(),
    												errorLogger);
    									}
    								}
    							}
    						}
    					//國內 完稅 驗證明細的採購單是否存在
    					}else if("IRL".equals(orderTypeCode) || "EIP".equals(orderTypeCode)){ // IRL,EIP) -> 進貨
    						//log.info("IRL/EIP1 imReceiveItem.getPoOrderNo()=" + imReceiveItem.getPoOrderNo()); 
    						List<PoPurchaseOrderHead> poPurchaseOrderHeads;
    						if("IRL".equals(orderTypeCode)){
    							poPurchaseOrderHeads = 
    								poPurchaseOrderHeadDAO.findByProperty("PoPurchaseOrderHead", new String []{"brandCode", "orderNo" , "orderTypeCode" , "status"}, new String []{brandCode, imReceiveItem.getPoOrderNo() , "POL", OrderStatus.FINISH});
    						}else{
    							poPurchaseOrderHeads = 
    								poPurchaseOrderHeadDAO.findByProperty("PoPurchaseOrderHead", new String []{"brandCode", "orderNo" , "orderTypeCode" , "status"}, new String []{brandCode, imReceiveItem.getPoOrderNo() , "EPP", OrderStatus.FINISH});
    						}
    						if(null!=poPurchaseOrderHeads && poPurchaseOrderHeads.size()>0){
    							for (Iterator iterator = poPurchaseOrderHeads.iterator(); iterator.hasNext();) {
									PoPurchaseOrderHead poPurchaseOrderHead = (PoPurchaseOrderHead) iterator.next();
									//判斷如果採購的庫別跟進貨的庫別不同
									if(!(poPurchaseOrderHead.getDefaultWarehouseCode().equals(imReceiveHead.getDefaultWarehouseCode())))
										siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
												tabName+"中第"+i+"項明細採購庫別("+poPurchaseOrderHead.getDefaultWarehouseCode()+ 
		    										")與進貨庫別("+poPurchaseOrderHead.getDefaultWarehouseCode()+")不同", errorLogger);
								}
    							poPurchaseOrderHeadId = poPurchaseOrderHeads.get(0).getHeadId();
    						}else{
    							siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
    									tabName+"中第"+i+"項明細採購單號:"+imReceiveItem.getPoOrderNo()+" 不存在或為不可使用之狀態！", errorLogger);
    						}
    					}
    					
    					
    					//國外 驗證明細的採購單數量 是否充足
    					if("IRF".equals(orderTypeCode)){ // IRF,EIP -> 進貨	 //if("IMF".equals(orderTypeCode)){ // || "EIP".equals(orderTypeCode)
    						HashMap findPOObjs = new HashMap();
    						findPOObjs.put("brandCode", brandCode);
    						findPOObjs.put("invoiceNo", imReceiveItem.getInvoiceNo());
    						findPOObjs.put("itemCode",  itemCode);
    						List<PoPurchaseOrderHead> poHeads = poPurchaseOrderHeadDAO.getInvoiceItemCode(findPOObjs);
    						if ((null == poHeads) || (poHeads.size() <= 0)) {
    							siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
    									tabName+"中第"+i+"項明細品號:"+itemCode+" 查無相關採購單！", errorLogger);
    						} else {

    						}
    					//判斷 國內 完稅 驗證明細的採購單數量是否充足
    					}else if("IRL".equals(orderTypeCode) || "EIP".equals(orderTypeCode)){ // IRL,EIP -> 進貨
    						if(null == poPurchaseOrderHeadId){
    							siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
    									tabName+"中第"+i+"項明細品號:"+itemCode+" 查無相關採購單！", errorLogger);
    						}else{
    							List<PoPurchaseOrderLine> poLines = 
    								poPurchaseOrderLineMainService.findByItemCode(poPurchaseOrderHeadId, itemCode ,poLineId);//採購欄位回寫增poLineId-Jerome
    							if(null!=poLines && poLines.size()>0){

    							}else{
    								siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
    										tabName+"中第"+i+"項明細品號:"+itemCode+" 查無可使用採購單！", errorLogger);
    							}
    						}
    					}
    				}
    				
					//保稅進貨退出 是否有展報單
					/*===================================================*/
					if("EOF".equals(orderTypeCode) && !StringUtils.hasText(imReceiveItem.getOriginalDeclarationNo()) )
						siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
								tabName+"中第"+i+"項明細品號:"+itemCode+" 未執行取得報單資料, 請檢查！", errorLogger);
					/*===================================================*/
    			}
    		}
    		if(0 == intactRecordCount)
    			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
    					tabName+"中請至少輸入一筆有效之明細資料！", errorLogger);
    	}catch(Exception ex){
    		message = "檢核" + tabName + "時發生錯誤，原因：" + ex.toString();
    		//siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, imReceiveHead.getLastUpdatedBy());
    		log.error(message);
    		throw ex;
    	}
    }
    
    
    /** check imReceiveExpense 
     * @param imReceiveHead
     * @param branchCode
     * @param programId
     * @param identification
     * @param errorMsgs
     */
    public void checkImReceiveExpenses( ImReceiveHead imReceiveHead, String branchCode, String identification, List errorMsgs ) throws Exception {
    	//log.info("checkImReceiveExpenses");
    	String message = null;
    	String tabName = "費用資料頁籤";
    	String errorLogger = imReceiveHead.getLastUpdatedBy();

    	try{
    		//兩張LcNo不可重複
    		if(StringUtils.hasText(imReceiveHead.getLcNo()) && StringUtils.hasText(imReceiveHead.getLcNo1()) 
    				&& imReceiveHead.getLcNo().equals(imReceiveHead.getLcNo1())){
    			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "進貨單兩張信用狀不可相同！", errorLogger);
    		}	
    		int intactRecordCount = 0;
    		int i = 0;
    		for(ImReceiveExpense imReceiveExpense : imReceiveHead.getImReceiveExpenses()){
    			i++;
    			if(!"1".equals(imReceiveExpense.getIsDeleteRecord())){

    				String supplierCode  = imReceiveExpense.getSupplierCode();
    				String expenseCode   = imReceiveExpense.getExpenseCode();
    				Double foreignAmount = imReceiveExpense.getForeignAmount();
    				Double localAmount   = imReceiveExpense.getLocalAmount();

    				if(!StringUtils.hasText(supplierCode))
    					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入"+tabName+"中第"+i+"項明細的廠商代號！", errorLogger);

    				if(!StringUtils.hasText(expenseCode))
    					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入"+tabName+"中第"+i+"項明細的費用類型！", errorLogger);

    				//if(foreignAmount == null || foreignAmount == 0D)
    				//siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入"+tabName+"中第"+i+"項明細的原幣金額！", errorLogger);

    				//if((localAmount == null || localAmount == 0D ))
    				//siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入"+tabName+"中第"+i+"項明細的台幣金額！", errorLogger);
    				intactRecordCount++;
    			}
    		}
    	}catch(Exception ex){
    		message = "檢核" + tabName + "時發生錯誤，原因：" + ex.toString();
    		//siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, imReceiveHead.getLastUpdatedBy());
    		log.error(message);
    		throw ex;
    	}
    }
    
    /** 啟始流程
     * @param form
     * @return
     * @throws ProcessFailedException
     */
    public static Object[] startProcess(ImReceiveHead form, String branchCode) throws ProcessFailedException{       
        try{           
            String packageId = "Im_Receive";
            String processId = "approval";  
            String sourceReferenceType = "Im_Receive(1)";
            String version   = "20091111";
            if ( branchCode.equals("2") ) {	// T2 Run different flow
		   version = "20160307T2";
	       } else {
		   version = "20091111";
	       }
            HashMap context = new HashMap();
            context.put("brandCode", form.getBrandCode());
            context.put("formId",    form.getHeadId());
            context.put("orderType", form.getOrderTypeCode());
            context.put("orderNo",   form.getOrderNo() );
            return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
        }catch (Exception ex){
            log.error("進貨單流程啟動失敗，原因：" + ex.toString());
            throw new ProcessFailedException("進貨單流程啟動失敗！");
	}
    }
    
    /** 啟始流程
     * @param form
     * @return
     * @throws ProcessFailedException
     */
    public static Object[] startProcess(ImReceiveHead form) throws ProcessFailedException{       
    	try{           
    		String packageId = "Im_Receive";
    		String processId = "approval";  
    		String sourceReferenceType = "Im_Receive(1)";
    		String version   = "20091111";
    		if ( "T2".equals(form.getBrandCode()) ) {	// T2 Run different flow
    			//version = "20100204T2";
    			version = "20160307T2";
    		} else {
    			version = "20091111";
    		}
    		HashMap context = new HashMap();
    		context.put("brandCode", form.getBrandCode());
    		context.put("formId",    form.getHeadId());
    		context.put("orderType", form.getOrderTypeCode());
    		context.put("orderNo",   form.getOrderNo() );
    		return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
    	}catch (Exception ex){
    		log.error("進貨單流程啟動失敗，原因：" + ex.toString());
    		throw new ProcessFailedException("進貨單流程啟動失敗！");
    	}
    }
    
    public static Object[] completeAssignment(long assignmentId, boolean approveResult, ImReceiveHead imReceive) 
	throws ProcessFailedException{
	try{           
	    HashMap context = new HashMap();
	    context.put("approveResult", approveResult);
	    context.put("form",imReceive);
	    return ProcessHandling.completeAssignment(assignmentId, context);
	}catch (Exception ex){
	    log.error("完成進貨單工作任務失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("完成進貨單工作任務失敗！");
	}
    }

    
    /** AJAX Load ImReceiveExpense Page Data
     * @param headObj
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public List<Properties> getAJAXExpensePageData(Properties httpRequest) 
    		throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,Exception {
	//log.info("getAJAXExpensePageData");
	// 要顯示的HEAD_ID
	Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	int iSPage  = AjaxUtils.getStartPage(httpRequest);
	int iPSize  = AjaxUtils.getPageSize(httpRequest);

	List<Properties> re = new ArrayList();
	List<ImReceiveExpense> imReceiveExpenses = null;
	List<Properties> gridDatas = new ArrayList();
	//log.info("getAJAXExpensePageData headId : " + headId + " iSPage : " + iSPage + " iPSize : " + iPSize );
	imReceiveExpenses = imReceiveExpenseMainService.findPageLine(headId, iSPage, iPSize);
	
	if (null != imReceiveExpenses && imReceiveExpenses.size() > 0) {
	    //log.info("getAJAXExpensePageData AjaxUtils.imReceiveExpenses=" + imReceiveExpenses.size());
	    // 取得第一筆的INDEX
	    Long firstIndex = imReceiveExpenses.get(0).getIndexNo();
	    // 取得最後一筆 INDEX
	    Long maxIndex = imReceiveExpenseMainService.findPageLineMaxIndex(Long.valueOf(headId));
	    re.add(AjaxUtils.getAJAXPageData( httpRequest, GRID_FIELD_NAMES_EXPENSE, GRID_FIELD_DEFAULT_VALUES_EXPENSE, 
		    			      imReceiveExpenses, gridDatas, firstIndex, maxIndex));
	} else {
	  re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES_EXPENSE, GRID_FIELD_DEFAULT_VALUES_EXPENSE, gridDatas));
	}
	return re;
  
    }

    
    /** 更新 ImReceiveExpense PAGE 所有的LINE
     * @param httpRequest
     * @return String message
     */
    public List<Properties> updateAJAXExpensePageLinesData(Properties httpRequest) throws NumberFormatException, FormException, Exception {
	//log.info("updateAJAXExpensePageLinesData");
	String gridData        = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	int gridRowCount       = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
	Long headId            = NumberUtils.getLong(httpRequest.getProperty("headId"));
	Double exchangeRate    = NumberUtils.getDouble(httpRequest.getProperty("exchangeRate"));
	String errorMsg        = null;
	//log.info("updateAJAXExpensePageLinesData gridLineFirstIndex=" + gridLineFirstIndex + ",gridRowCount=" + gridRowCount
				//+ ",headId=" + headId + ",exchangeRate=" + exchangeRate + ",gridData=" + gridData);
		
	ImReceiveHead head = imReceiveHeadDAO.findById(headId);
	head.setExchangeRate(exchangeRate);
	// 將STRING資料轉成List Properties record data
	List<Properties> upRecords = AjaxUtils.getGridFieldValue( gridData, gridLineFirstIndex, gridRowCount,
								  ImReceiveHeadMainService.GRID_FIELD_NAMES_EXPENSE);

	int indexNo = imReceiveExpenseMainService.findPageLineMaxIndex(headId).intValue();	// Get Max INDEX NO
	if (null != upRecords && null != head) {
	    for (Properties upRecord : upRecords) {
		// 先載入HEAD_ID OR LINE DATA
		Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
		//log.info("head Id=" + head.getHeadId() + " ,line Id=" + lineId);
		ImReceiveExpense imReceiveExpense = imReceiveExpenseMainService.findLine(head.getHeadId(), lineId);
		String supplierCode = upRecord.getProperty(GRID_FIELD_NAMES_EXPENSE[1]);
		//log.info("head Id=" + head.getHeadId() + " ,line Id=" + lineId + ",supplierCode=" + supplierCode);
		// 如果沒有 supplierCode 就不會新增或修改
		if (StringUtils.hasText(supplierCode)) {
		    if (null != imReceiveExpense) {
			AjaxUtils.setPojoProperties(imReceiveExpense, upRecord, GRID_FIELD_NAMES_EXPENSE, GRID_FIELD_TYPES_EXPENSE);
			imReceiveHeadDAO.update(imReceiveExpense);	// UPDATE 
		    } else {
			indexNo++;
			imReceiveExpense = new ImReceiveExpense();
			AjaxUtils.setPojoProperties(imReceiveExpense, upRecord, GRID_FIELD_NAMES_EXPENSE, GRID_FIELD_TYPES_EXPENSE);
			imReceiveExpense.setIndexNo(Long.valueOf(indexNo));
			imReceiveExpense.setImReceiveHead(head);
			imReceiveHeadDAO.save(imReceiveExpense);	// INSERT
		    }
		}
	    }
	} else {
	    errorMsg = "沒有進貨單單頭資料";
	}
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    
    
    /** 依照 imReceiveHead.headId, declarationNo 展開報單資料於 imReceiveItem
     * @param httpRequest
     * @return String message
     *
     */
    public List<Properties> updateAJAXImportItem(Map parameterMap)
    		throws NumberFormatException, FormException, Exception {
	//log.info("updateAJAXImportItem");
	Object formBindBean = parameterMap.get("vatBeanFormBind");
	Object formLinkBean = parameterMap.get("vatBeanFormLink");
	Object otherBean    = parameterMap.get("vatBeanOther");
	String branchCode   = (String)PropertyUtils.getProperty(otherBean, "branchCode");
	String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	Long headId = getImReceiveHeadId(formLinkBean);
	
	ImReceiveHead imReceiveHead = getActualImReceive(headId);	// 取得 dbData
	AjaxUtils.copyJSONBeantoPojoBean(formBindBean,imReceiveHead);	// assign formData to dbData
	
	String orderTypeCode = imReceiveHead.getOrderTypeCode();
	String rtnString = "NONE";
	if ("IRF".equals(orderTypeCode) || "EIF".equals(orderTypeCode)){
	    rtnString = setAJAXImportItemFmDeclaration(imReceiveHead, branchCode, employeeCode );
	}else{
	    rtnString = setAJAXImportItemFmPurchase(imReceiveHead, branchCode, employeeCode );
	}

	HashMap resultMap = new HashMap();
	resultMap.put("rtnString", rtnString );
	resultMap.put("form", imReceiveHead); 
	return AjaxUtils.parseReturnDataToJSON(resultMap);

    }
    
    
    /**由 cmDeclaration 轉入 imReceive (T2要參考 imInvoice)
     * 1. 沒有 invoice 時也要展, 2. invCnt>declCnt or invCnt<declCnt  時展 declCnt
     * @param imReceiveHead
     * @param branchCode
     * @param employeeCode
     * @return
     * @throws NumberFormatException
     * @throws FormException
     * @throws Exception
     */
    public String setAJAXImportItemFmDeclaration (ImReceiveHead imReceiveHead, String branchCode, String employeeCode ) 
		throws NumberFormatException, FormException, Exception, ClassCastException {
    	//log.info("setAJAXImportItemFmDeclaration");
    	String declarationNo = imReceiveHead.getDeclarationNo();
	    try{
		// 取報單主檔
			CmDeclarationHead cmDeclarationHead = null;
			List<CmDeclarationHead> cmDeclarationHeads = 
					cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", declarationNo);
			if(null!=cmDeclarationHeads && cmDeclarationHeads.size()>0){
			    cmDeclarationHead = cmDeclarationHeads.get(0);
			    String declType   = cmDeclarationHead.getDeclType();
			    if( !"D7".equals(declType) && !"D8".equals(declType) && !"D1".equals(declType) && !"B2".equals(declType))
			    	return declarationNo + " 報單資料非 D7,D8,D1,B2報單";
			    if( !OrderStatus.WAIT_IN.equals(cmDeclarationHead.getStatus() ))
			    	return declarationNo + " 報單狀態非 " + OrderStatus.getChineseWord(OrderStatus.WAIT_IN);
			} else {
			    return "查無此報單資料";
			}
			    
			imReceiveHead.setDeclarationType( cmDeclarationHead.getDeclType());
			imReceiveHead.setDeclarationDate( cmDeclarationHead.getDeclDate());
			imReceiveHead.setImportDate(      cmDeclarationHead.getImportDate());
			imReceiveHead.setBondNo(          cmDeclarationHead.getBondNo());
			imReceiveHead.setReferenceBillNo( cmDeclarationHead.getRefBillNo());
			imReceiveHead.setReleaseTime(     cmDeclarationHead.getRlsTime());
			imReceiveHead.setReleasePackage(  cmDeclarationHead.getRlsPkg());
			imReceiveHead.setReleaseCondition(cmDeclarationHead.getExtraCond());
			imReceiveHead.setStoragePlace(    cmDeclarationHead.getStgPlace());
			imReceiveHead.setPackageUnit(     cmDeclarationHead.getPkgUnit());
			imReceiveHead.setWeight(          cmDeclarationHead.getGWgt());
			imReceiveHead.setVesselSign(      cmDeclarationHead.getVesselSign());
			imReceiveHead.setVoyageNo(        cmDeclarationHead.getVoyageNo());
			imReceiveHead.setShipCode(        cmDeclarationHead.getShipCode());
			imReceiveHead.setExporter(        cmDeclarationHead.getExporter());
			imReceiveHead.setClearanceType(   cmDeclarationHead.getClearType());
			imReceiveHead.setDeclarationBoxNo(cmDeclarationHead.getBoxNo());
			imReceiveHead.setInbondNo(        cmDeclarationHead.getInbondNo());
			imReceiveHead.setOutbondNo(       cmDeclarationHead.getOutbondNo());
			if(null!=cmDeclarationHead.getCountryCode())
			    imReceiveHead.setCountryCode(     cmDeclarationHead.getCountryCode());	// 2010.01.14 會議中船務 Ellen 提出
			if(null!=cmDeclarationHead.getCurrencyCode())
			    imReceiveHead.setCurrencyCode(    cmDeclarationHead.getCurrencyCode());	// 2010.01.14 會議中船務 Ellen 提出
			if(null!=cmDeclarationHead.getExchangeRate())
			    imReceiveHead.setExchangeRate(    cmDeclarationHead.getExchangeRate());	// 2010.01.14 會議中船務 Ellen 提出
			
			int	indexNo	= 0;
			Double exchangeRate = imReceiveHead.getExchangeRate();
			// 依照 invoceHead.customsSeq, invoiceHead.invoceNo, invoiceLine.customSeq 排序取出採購單報單資料
			List fiInvoiceLines = fiInvoiceLineMainService.findBydDclarationNo( declarationNo );
			//log.info("fiInvoiceLines Count=" + fiInvoiceLines.size() + "-" + fiInvoiceLines);
			if(null==fiInvoiceLines || fiInvoiceLines.size()==0 ){
			    return declarationNo + " 報單無對應 Invoice 資料, 請查明！";
			}
			// 取報單明細資料
			List<CmDeclarationItem> cmDeclarationItems = cmDeclarationHead.getCmDeclarationItems() ;
			List<ImReceiveItem> imReceiveItems = new ArrayList();
			for( CmDeclarationItem cmDeclarationItem : cmDeclarationItems ){
			    ImReceiveItem imReceiveItem = new ImReceiveItem();
			    imReceiveItem.setIndexNo( Long.valueOf(indexNo+1));
			    imReceiveItem.setImReceiveHead( imReceiveHead );
			    imReceiveItem.setDeclarationBrand(        cmDeclarationItem.getBrand() );
			    imReceiveItem.setDeclarationItemName(     cmDeclarationItem.getDescrip());
			    imReceiveItem.setDeclarationItemNo(       cmDeclarationItem.getItemNo());
			    imReceiveItem.setDeclarationModel(        cmDeclarationItem.getModel());
			    imReceiveItem.setDeclarationNetWeight(    cmDeclarationItem.getNWght());
			    imReceiveItem.setDeclarationQty(          cmDeclarationItem.getQty());
			    imReceiveItem.setDeclarationSpecification(cmDeclarationItem.getSpec());
			    imReceiveItem.setDeclarationUnit(         cmDeclarationItem.getUnit());
			    //imReceiveItem.setUnitPrice(               cmDeclarationItem.getUnitPrice());//改以Im_Item中的unitPrice(wade)
			    imReceiveItem.setOriginalDeclarationNo(   cmDeclarationItem.getODeclNo());
			    imReceiveItem.setOriginalDeclarationSeq(  cmDeclarationItem.getOItemNo());
			    //如果是D8報單，自動把單頭的進口日期代到單身的原報單日期
			    if("D8".equals(cmDeclarationHead.getDeclType()) || "D1".equals(cmDeclarationHead.getDeclType()))
			    	imReceiveItem.setOriginalDeclarationDate( cmDeclarationHead.getImportDate());
			    else
			    	imReceiveItem.setOriginalDeclarationDate( cmDeclarationItem.getODeclDate());
			    imReceiveItem.setQuantity( cmDeclarationItem.getQty() );
			    imReceiveItem.setReserve1( cmDeclarationItem.getItemId().toString()    );	// 借用 reserve1 存放報單  itemId
		
			    if ("2".equals(branchCode) && fiInvoiceLines.size()>= indexNo+1 ) {		// T2(EIF) /indexNo 初始值為 0
					Object[] obj = (Object[]) fiInvoiceLines.get(indexNo);
					FiInvoiceLine fiInvoiceLine = (FiInvoiceLine)fiInvoiceLineDAO.findById( "FiInvoiceLine", (Long)obj[0] );
					FiInvoiceHead fiInvoiceHead = fiInvoiceLine.getFiInvoiceHead();
					imReceiveItem.setInvoiceNo(fiInvoiceHead.getInvoiceNo());
					//2011.4.6進貨單匯入報單自動帶出廠商代號
					imReceiveHead.setSupplierCode(fiInvoiceHead.getSupplierCode());
					imReceiveItem.setItemCode( fiInvoiceLine.getItemCode() );
					imReceiveItem.setDeclarationItemCode( fiInvoiceLine.getItemCode());
					if( StringUtils.hasText(fiInvoiceLine.getItemCName()) ) {
					    imReceiveItem.setItemCName(fiInvoiceLine.getItemCName());
					}else{
					    ImItem imItem = imItemService.findItem(imReceiveHead.getBrandCode(), imReceiveItem.getItemCode());
					    if( null!=imItem )
					    	imReceiveItem.setItemCName(imItem.getItemCName());
					}
					
					imReceiveItem.setForeignUnitPrice(fiInvoiceLine.getForeignUnitPrice());
				    imReceiveItem.setForeignAmount(   imReceiveItem.getForeignUnitPrice() * imReceiveItem.getQuantity());
				    imReceiveItem.setLocalUnitPrice(  exchangeRate * fiInvoiceLine.getForeignUnitPrice());
				    imReceiveItem.setLocalAmount(     imReceiveItem.getLocalUnitPrice() * imReceiveItem.getQuantity());
				    
				    //找出採購單號
				    PoPurchaseOrderHead head = poPurchaseOrderHeadMainService.findById(fiInvoiceLine.getPoPurchaseOrderHeadId());
				    if(null != head)
				    	imReceiveItem.setPoOrderNo(head.getOrderNo());
					//}
			    }else{								// T1(IMF)
					imReceiveItem.setInvoiceNo(imReceiveHead.getDefPoOrderNo());	// 借用 defPoOrderNo 為預設 invoiceNo
					imReceiveItem.setItemCode( cmDeclarationItem.getPrdtNo());
					imReceiveItem.setItemCName(cmDeclarationItem.getDescrip());
					if ( null!=cmDeclarationItem.getUnitPrice()){
					    imReceiveItem.setForeignUnitPrice(imReceiveItem.getUnitPrice());
					    imReceiveItem.setForeignAmount(imReceiveItem.getForeignUnitPrice() * imReceiveItem.getQuantity());
					    imReceiveItem.setLocalUnitPrice(exchangeRate * imReceiveItem.getForeignUnitPrice());
					    imReceiveItem.setLocalAmount(imReceiveItem.getLocalUnitPrice() * imReceiveItem.getQuantity());
					}
			    }
			    imReceiveItem.setAcceptQuantity( imReceiveItem.getQuantity());
			    imReceiveItem.setReceiptQuantity(imReceiveItem.getQuantity());
			    imReceiveItem.setBarcodeCount(   imReceiveItem.getQuantity());
			    imReceiveItem.setDefectQuantity(0D);
			    imReceiveItem.setDiffQty(0D);
			    imReceiveItem.setShortQuantity( 0D);
			    imReceiveItem.setSampleQuantity(0D);
			    imReceiveItem.setCreatedBy(imReceiveHead.getLastUpdatedBy());
			    imReceiveItem.setLastUpdatedBy(imReceiveHead.getLastUpdatedBy());
			    imReceiveItem.setCreationDate(DateUtils.parseDate(DateUtils.format(new Date())));
			    imReceiveItem.setLastUpdateDate(DateUtils.parseDate(DateUtils.format(new Date())));		    
			    imReceiveItems.add(imReceiveItem);
			    indexNo++;
			    if( indexNo==1 ) {	// 依照第一筆 itemCode 設定業種
					ImItem imItem = imItemService.findItem(imReceiveHead.getBrandCode(), imReceiveItem.getItemCode());
					if( null!=imItem ){
					    //log.info("indexNo=" + indexNo + "itemCategory=" + imItem.getItemCategory());
					    imReceiveHead.setItemCategory(imItem.getItemCategory());
					}else{
					    //log.info("indexNo=" + indexNo + "itemCategory=null");
					}
			    }
			}
			
			//設定零售價(wade)
			Map<String, Double> prices = new HashMap<String, Double>();
			StringBuffer items = new StringBuffer();
			for(int i=0; i<imReceiveItems.size(); i++){
				items.append("'").append(imReceiveItems.get(i).getItemCode()).append("'").append((i<imReceiveItems.size()-1) ? " , " : ")");
			}
//			List<ImItemCurrentPriceView> imItemPrices = imItemPriceDAO.getImItemPriceViews(imReceiveHead.getBrandCode(), imReceiveItems);
			List<ImItemCurrentPriceView> imItemPrices = imItemCurrentPriceViewDAO.findCurrentPriceList(imReceiveHead.getBrandCode(), items.toString(), "1");
			if(imItemPrices != null && imItemPrices.size() > 0){
				//int i = 0;
				for(ImItemCurrentPriceView imItemPrice : imItemPrices){
					//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>: " + i);
					//if(imItemPrice == null) System.out.println("imItemPrice is null: " + i);
					//if(imItemPrice.getItemCode() == null) System.out.println("ItemCode is null: " + i);
					//if(imItemPrice.getUnitPrice() == null) System.out.println("UnitPrice is null: " + i);
					if(imItemPrice != null)
						prices.put(imItemPrice.getItemCode(), imItemPrice.getUnitPrice());
					//i++;
				}
			}
			if(prices != null){
				for(ImReceiveItem item : imReceiveItems){
					if(item.getItemCode() != null && prices.get(item.getItemCode()) != null){
						item.setUnitPrice(prices.get(item.getItemCode()));
					}
				}	
			}
			
			if( indexNo>0 ){
			    imReceiveHead.setImReceiveItems(imReceiveItems);
			    modifyImReceive(imReceiveHead, employeeCode);
			    return "NONE";
			}else{
			    return "無明細資料轉入";
			}
	    }
	    catch ( ClassCastException cx){
			log.error( cx.toString());
			throw new ProcessFailedException("完成進貨單工作任務失敗！");
	    }
	    catch (Exception ex){
	    	log.error( ex.toString());
	    	throw new ProcessFailedException("完成進貨單工作任務失敗！");
	    }
    }
    
    
    /** 由PoPurchase 轉入完稅/國內採購單到 imReceive
     * @param imReceiveHead
     * @param branchCode
     * @param employeeCode
     * @return
     * @throws NumberFormatException
     * @throws FormException
     * @throws Exception
     */
    public String setAJAXImportItemFmPurchase (ImReceiveHead imReceiveHead, String branchCode, String employeeCode ) 
	throws NumberFormatException, FormException, Exception {
	//log.info("setAJAXImportItemFmPurchase");

	// 取採購單主檔
	PoPurchaseOrderHead poPurchaseOrderHead = null;
	List<PoPurchaseOrderHead> poPurchaseOrderHeads = 
	    poPurchaseOrderHeadDAO.findByProperty("PoPurchaseOrderHead", "orderNo", imReceiveHead.getDefPoOrderNo());	
	if(null!=poPurchaseOrderHeads && poPurchaseOrderHeads.size()>0){
	    poPurchaseOrderHead = poPurchaseOrderHeads.get(0);
	    if (!OrderStatus.FINISH.equals(poPurchaseOrderHead.getStatus()))
		return "採購單號" + imReceiveHead.getDefPoOrderNo() + "尚未簽核完成";
	    
	    BuOrderType buOrderType;
	    // 取採購單稅別
	    buOrderType = buOrderTypeService.findById( new BuOrderTypeId(poPurchaseOrderHead.getBrandCode(), poPurchaseOrderHead.getOrderTypeCode()) );
	    String poTaxCode = buOrderType.getTaxCode();
	    // 取進貨單稅別
	    buOrderType = buOrderTypeService.findById( new BuOrderTypeId(imReceiveHead.getBrandCode(), imReceiveHead.getOrderTypeCode()) );
	    if( !poTaxCode.equals(buOrderType.getTaxCode()) )
		return imReceiveHead.getDefPoOrderNo() + " 採購單稅別=" + poTaxCode + " 與進貨單稅別不符合";
	    if( !imReceiveHead.getBrandCode().equals(poPurchaseOrderHead.getBrandCode()))
		return imReceiveHead.getDefPoOrderNo() + " 採購單品牌=" +
			poPurchaseOrderHead.getBrandCode()+ " 與進貨單品牌不符合";
	} else {
	    return "查無此報單資料";
	}
	
	Integer	indexNo	= 0;
	// 取採購單明細資料
	List<PoPurchaseOrderLine> poPurchaseOrderLines = poPurchaseOrderHead.getPoPurchaseOrderLines() ;
	//log.info("poPurchaseOrderLines counts=" + poPurchaseOrderLines.size());
	List imReceiveItems = new ArrayList();
	for( PoPurchaseOrderLine poLine : poPurchaseOrderLines ){
	    ImReceiveItem imReceiveItem = new ImReceiveItem();
	    imReceiveItem.setIndexNo( Long.valueOf(indexNo+1));
	    imReceiveItem.setImReceiveHead( imReceiveHead );
	    imReceiveItem.setDeclarationBrand(        poPurchaseOrderHead.getBrandCode() );
	    imReceiveItem.setPoOrderNo(               imReceiveHead.getDefPoOrderNo());
	    imReceiveItem.setPoOrderType(             imReceiveHead.getDefPoOrderType());
	    imReceiveItem.setDeclarationItemCode(     poLine.getItemCode());
	    imReceiveItem.setDeclarationItemName(     poLine.getItemCName());
	    imReceiveItem.setItemCode(                poLine.getItemCode() );
	    imReceiveItem.setItemCName(               poLine.getItemCName());
	    imReceiveItem.setQuantity(                poLine.getQuantity()-poLine.getReceiptedQuantity() );	// 採購未到貨數量
	    imReceiveItem.setDeclarationQty(          imReceiveItem.getQuantity());
	    imReceiveItem.setUnitPrice(               poLine.getUnitPrice());
	    imReceiveItem.setLocalUnitPrice(          poLine.getLastLocalUnitCost());
	    imReceiveItem.setLocalAmount(             imReceiveItem.getQuantity()*poLine.getLastLocalUnitCost());
	    imReceiveItem.setForeignUnitPrice(        poLine.getForeignUnitCost());
	    imReceiveItem.setForeignAmount(           imReceiveItem.getQuantity()*poLine.getForeignUnitCost());
	    imReceiveItem.setAcceptQuantity( imReceiveItem.getQuantity());
	    imReceiveItem.setReceiptQuantity(imReceiveItem.getQuantity());
	    imReceiveItem.setBarcodeCount(   imReceiveItem.getQuantity());
	    imReceiveItem.setDefectQuantity(0D);
	    imReceiveItem.setDiffQty(       0D);
	    imReceiveItem.setShortQuantity( 0D);
	    imReceiveItem.setSampleQuantity(0D);
	    imReceiveItem.setCreatedBy(     imReceiveHead.getLastUpdatedBy());
	    imReceiveItem.setLastUpdatedBy( imReceiveHead.getLastUpdatedBy());
	    imReceiveItem.setCreationDate(  DateUtils.parseDate(DateUtils.format(new Date())));
	    imReceiveItem.setLastUpdateDate(DateUtils.parseDate(DateUtils.format(new Date())));	
	    imReceiveItems.add(imReceiveItem);
	    //imReceiveHeadDAO.save(imReceiveItem);		// INSERT
	    indexNo++;
	    
	    if( indexNo==1 ) {	// 依照第一筆 itemCode 設定業種
		ImItem imItem = imItemService.findItem(imReceiveHead.getBrandCode(), imReceiveItem.getItemCode());
		if( null!=imItem ){
		    imReceiveHead.setItemCategory(imItem.getItemCategory());
		}
	    }
	}
	if( indexNo>0 ){
	    imReceiveHead.setImReceiveItems(imReceiveItems);
	    modifyImReceive(imReceiveHead, employeeCode);
	    return "NONE";
	}else{
	    return "無明細資料轉入";
	}
    }
    
    /**
     * 依據itemCode以及brandCode為查詢條件，取得業種
     * 
     * @param currencyCode
     * @return ExchangeRate
     * @throws Exception
     */  
    public List<Properties> getItemCategory(Properties httpRequest) throws Exception{
        List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	try{
    		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
    		String brandCode = httpRequest.getProperty("brandCode");
    		String itemCode = httpRequest.getProperty("itemCode");
    		ImReceiveHead head = findById(headId);
    		
    		if(head != null){
    			properties.setProperty("Remark1", head.getRemark1() == null ? "" : head.getRemark1());//完稅進貨單帶入備註資料(wade)
    			properties.setProperty("Remark2", head.getRemark2() == null ? "" : head.getRemark2());//完稅進貨單帶入備註資料(wade)
    		}
    			
    		ImItem item = null;
    		if(StringUtils.hasText(itemCode)){
    			item = imItemService.findItem(brandCode, itemCode);
    		}
    		else{
	    		if(head != null && head.getImReceiveItems() != null && head.getImReceiveItems().size() > 0){
	    			ImReceiveItem imReceiveItem = head.getImReceiveItems().get(0);
	    			item = imItemService.findItem(brandCode, imReceiveItem.getItemCode());
	    		}
    		}
    		if(item != null){
				properties.setProperty("ItemCategory", item.getItemCategory());	 
				//log.info("item.getItemCategory() = " + item.getItemCategory());
    		}
    	    result.add(properties);
    	    return result;
    	}
    	catch(Exception ex){
    	    log.error("查詢商品業種時發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢商品業種時發生錯誤，原因：" + ex.getMessage());
    	}
    }
    
    
    /**執行進貨單查詢初始化
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception{
        //log.info("executeSearchInitial");
        HashMap resultMap = new HashMap();
        try{
            Object otherBean = parameterMap.get("vatBeanOther");
		    String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");
		    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
		    BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode) );
		    
		    Map multiList = new HashMap(0);
		    List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(brandCode, buOrderType.getTypeCode());
		    List<ImItemCategory>     allItemCategory    = imItemCategoryService.findByCategoryType(brandCode, "ITEM_CATEGORY");
		    resultMap.put("allItemCategory",    AjaxUtils.produceSelectorData( allItemCategory, "categoryCode", "categoryName", true, true));
		    multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
		    resultMap.put("typeCode", buOrderType.getTypeCode());
		    resultMap.put("multiList",multiList);
			
		    return resultMap;       	
        }catch (Exception ex) {
        	log.error("進貨單查詢初始化失敗，原因：" + ex.toString());
        	throw new Exception("進貨單查詢初始化失敗，原因：" + ex.toString());
        }           
    }
    
    
    /**顯示查詢頁面的line
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{
	//log.info("getAJAXSearchPageData");
    	try{
    	    List<Properties> result    = new ArrayList();
    	    List<Properties> gridDatas = new ArrayList();
    	    int iSPage = AjaxUtils.getStartPage(httpRequest);	// 取得起始頁面
    	    int iPSize = AjaxUtils.getPageSize(httpRequest);	// 取得每頁大小  	  
    	    //======================帶入Head的值，製作Map=========================
    	    HashMap searchMap = getSearchMap(httpRequest);
    	    //==============================================================    	   
    	    List<ImReceiveHead> imReceiveHeads = 
    	    	(List<ImReceiveHead>)imReceiveHeadDAO.findPageLine(searchMap, 
    	    			iSPage, iPSize, ImReceiveHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");
    	    if (imReceiveHeads != null && imReceiveHeads.size() > 0) {    	    
    	    	Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;    // 取得第一筆的INDEX 	
    	    	Long maxIndex = (Long)imReceiveHeadDAO.findPageLine(searchMap, -1, iPSize, 
    	    			ImReceiveHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount");	// 取得最後一筆 INDEX
    	    	
    	    	for(ImReceiveHead imReceiveHead : imReceiveHeads){    
    	    	    imReceiveHead.setStatus(OrderStatus.getChineseWord(imReceiveHead.getStatus()));
    	    	    imReceiveHead.setWarehouseStatus(OrderStatus.getChineseWord(imReceiveHead.getWarehouseStatus()));
    	    	    imReceiveHead.setExpenseStatus(OrderStatus.getChineseWord(imReceiveHead.getExpenseStatus()));
    	    	}
    	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, imReceiveHeads, gridDatas, firstIndex, maxIndex));
    	    } else {
    	        result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, gridDatas));
    	    }
    	
    	    return result;
    	}catch(Exception ex){
    	    log.error("載入頁面顯示的進貨單查詢發生錯誤，原因：" + ex.toString());
    	    throw new Exception("載入頁面顯示的進貨單查詢失敗！");
    	}	
    }
    
    public HashMap getSearchMap(Properties httpRequest){
	    HashMap searchMap = new HashMap();
    	String startDate     = httpRequest.getProperty("startDate");
	    String endDate       = httpRequest.getProperty("endDate");
	    Date actualStartDate = null;
	    Date actualEndDate   = null;
	    if(StringUtils.hasText(startDate))
	    	actualStartDate = DateUtils.parseDate("yyyy/MM/dd", startDate);
	    if(StringUtils.hasText(endDate))
	    	actualEndDate = DateUtils.parseDate("yyyy/MM/dd", endDate);
	     	   
	    String startIDate     = httpRequest.getProperty("startIDate");
	    String endIDate       = httpRequest.getProperty("endIDate");
	    Date actualStartIDate = null;
	    Date actualEndIDate   = null;
	    if(StringUtils.hasText(startIDate))
	    	actualStartIDate = DateUtils.parseDate("yyyy/MM/dd", startIDate);
	    if(StringUtils.hasText(endIDate))
	    	actualEndIDate = DateUtils.parseDate("yyyy/MM/dd", endIDate);
	    
	    String startRDate     = httpRequest.getProperty("startRDate");
	    String endRDate       = httpRequest.getProperty("endRDate");
	    Date actualStartRDate = null;
	    Date actualEndRDate   = null;
	    if(StringUtils.hasText(startRDate))
	    	actualStartRDate = DateUtils.parseDate("yyyy/MM/dd", startRDate);
	    if(StringUtils.hasText(endRDate))
	    	actualEndRDate = DateUtils.parseDate("yyyy/MM/dd", endRDate);
	    
	    searchMap.put("brandCode",     		httpRequest.getProperty("brandCode"));
	    searchMap.put("orderTypeCode", 		httpRequest.getProperty("orderTypeCode"));
	    searchMap.put("startDate",     		actualStartDate);
	    searchMap.put("endDate",       		actualEndDate);
	    searchMap.put("status",        		httpRequest.getProperty("status"));
	    searchMap.put("startOrderNo",  		httpRequest.getProperty("startOrderNo"));
	    searchMap.put("endOrderNo",    		httpRequest.getProperty("endOrderNo")); 	   
	    searchMap.put("startSourceOrderNo",  httpRequest.getProperty("startSourceOrderNo"));
	    searchMap.put("endSourceOrderNo",	httpRequest.getProperty("endSourceOrderNo"));
	    searchMap.put("declarationNo", 		httpRequest.getProperty("declarationNo"));  	    
	    searchMap.put("itemCategory",    	httpRequest.getProperty("itemCategory"));
	    searchMap.put("warehouseStatus", 	httpRequest.getProperty("warehouseStatus"));
	    searchMap.put("expenseStatus",   	httpRequest.getProperty("expenseStatus"));
	    searchMap.put("supplierCode",    	"%"+httpRequest.getProperty("supplierCode")+"%");
	    searchMap.put("startDateDiff",   	httpRequest.getProperty("startDateDiff"));
	    searchMap.put("endDateDiff",			httpRequest.getProperty("endDateDiff"));
	    searchMap.put("itemCode",    		httpRequest.getProperty("itemCode"));
	    searchMap.put("startIDate",			actualStartIDate);
	    searchMap.put("endIDate",			actualEndIDate);
	    searchMap.put("startRDate",			actualStartRDate);
	    searchMap.put("endRDate",			actualEndRDate);
	    return searchMap;
    }
    
    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
    
    public void updateAllSearchData(Map parameterMap) throws FormException, Exception{
	//log.info("updateAllSearchData!");
	try{
    	    Object pickerBean    = parameterMap.get("vatBeanPicker");
	    Object formBindBean  = parameterMap.get("vatBeanFormBind");
	    Object otherBean     = parameterMap.get("vatBeanOther");
            String timeScope     = (String)   PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
	    //======================帶入Head的值=========================	    
    	    String brandCode     = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
    	    String orderTypeCode = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");
    	    String startOrderNo  = (String)PropertyUtils.getProperty(formBindBean, "startOrderNo");
    	    String endOrderNo    = (String)PropertyUtils.getProperty(formBindBean, "endOrderNo");
    	    String status        = (String)PropertyUtils.getProperty(formBindBean, "status");
    	    String declarationNo = (String)PropertyUtils.getProperty(formBindBean, "declarationNo");
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
    	    findObjs.put("brandCode",     brandCode);
	    findObjs.put("orderTypeCode", orderTypeCode);
	    findObjs.put("startOrderNo",  startOrderNo);
	    findObjs.put("endOrderNo",    endOrderNo); 	   
	    findObjs.put("status",        status);
	    findObjs.put("declarationNo", declarationNo);  	    
	    findObjs.put("startDate",     actualStartDate);
	    findObjs.put("endDate",       actualEndDate);     
    	    List<ImReceiveHead> imReceiveHeads = 
    	    	(List<ImReceiveHead>)imReceiveHeadDAO.findPageLine(findObjs, 
    	    			-1, -1, imReceiveHeadDAO.QUARY_TYPE_SELECT_ALL).get("form");
    	    //log.info("ResultSize=["+ imReceiveHeads.size() + "]" + findObjs);
	}catch(Exception ex){
	    log.error("更新選取進貨資料失敗，原因：" + ex.toString());
	    throw new Exception("更新選取進貨資料失敗，原因：" + ex.getMessage());		
	}
    }
    
    public Map getSearchSelection(Map parameterMap) throws Exception{
	//log.info("getSearchSelection");
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    Object pickerBean       = parameterMap.get("vatBeanPicker");
	    String timeScope        = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys    = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    //log.info(" selected size = " + result.size() );
	    if(result.size() > 0 )
	        pickerResult.put("result", result);
	    
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel",  new String[]{"vatBeanPicker"});
	    
	    return resultMap;
	}catch(Exception ex){
	    log.error("執行進貨單檢視失敗，原因：" + ex.toString());
	    throw new Exception("執行進貨單檢視失敗，原因：" + ex.getMessage());		
	}
    }
    
    
    public String getIdentification(Long headId) throws Exception{
	String id = null;
	try{
	    ImReceiveHead imReceiveHead = findById(headId);
	    if(imReceiveHead != null){
		id = MessageStatus.getIdentification(imReceiveHead.getBrandCode(), imReceiveHead.getOrderTypeCode(), imReceiveHead.getOrderNo());
	    }else{
		throw new NoSuchDataException("進貨單主檔查無主鍵：" + headId + "的資料！");
	    }
	    return id;
	    
	}catch(Exception ex){
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
	}	   
    }

    /**進貨後寫回商品主檔
     * @param imReceiveHead
     * @param beforeChangeStatus
     * @param formStatus
     * @param loginUser
     * @throws Exception
     */
    public void updateImReceive2ImItem ( ImReceiveHead imReceiveHead ) throws Exception {
    	//log.info("updateImReceive2ImItem");
    	try{
    	    String organizationCode = UserUtils.getOrganizationCodeByBrandCode(imReceiveHead.getBrandCode());
    	    if (organizationCode == null)
    		throw new FormException("依據品牌代碼：" + imReceiveHead.getBrandCode() + "查無其組織代號！");

    	    List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
    	    if (null != imReceiveItems ) {
    		//log.info("updateImReceive2ImItem.imReceiveItems.size =" + imReceiveItems.size());
	    		for (ImReceiveItem imReceiveItem : imReceiveItems) {	
	    				ImItem item = imItemService.findItem(imReceiveHead.getBrandCode(), imReceiveItem.getItemCode());
	    				//item.setLastUnitCost(imReceiveItem.getLocalUnitPrice());
	    				item.setLastCurrencyCode(imReceiveHead.getCurrencyCode());
	    				//採購日期 用驗收日期
	    				if(null != imReceiveHead.getReceiptDate()){
		    				if(null == item.getFirstPurchaseDate())
		    					item.setFirstPurchaseDate(imReceiveHead.getReceiptDate());
		    				item.setLastPurchaseDate(imReceiveHead.getReceiptDate());
	    				}
	    				/* 全部改由單價
	    				item.setLastPurForeignAmount(imReceiveItem.getForeignAmount());
	    				item.setLastPurLocalAmount(imReceiveItem.getLocalAmount());
	    				//最高跟最低都用台幣算
	    				if(null == item.getMaxPurchaseAmount() || imReceiveItem.getLocalAmount() > item.getMaxPurchaseAmount())
	    					item.setMaxPurchaseAmount(imReceiveItem.getLocalAmount());
	    				if(null == item.getMinPurchaseAmount() || imReceiveItem.getLocalAmount() < item.getMinPurchaseAmount())
	    					item.setMinPurchaseAmount(imReceiveItem.getLocalAmount());
	    				*/
	    				item.setLastPurForeignAmount(imReceiveItem.getForeignUnitPrice());
	    				item.setLastPurLocalAmount(imReceiveItem.getLocalUnitPrice());
	    				if(null == item.getMaxPurchaseAmount() || imReceiveItem.getLocalUnitPrice() > item.getMaxPurchaseAmount())
	    					item.setMaxPurchaseAmount(imReceiveItem.getLocalUnitPrice());
	    				if(null == item.getMinPurchaseAmount() || imReceiveItem.getLocalUnitPrice() < item.getMinPurchaseAmount())
	    					item.setMinPurchaseAmount(imReceiveItem.getLocalUnitPrice());
	    		}
    	    }
    	}catch (Exception ex) {
    	    log.error("imItem 進貨資訊回寫作業失敗，原因：" + ex.toString());
    	    throw new Exception("imItem  進貨資訊作業失敗，原因：" + ex.getMessage());
    	}
    }
    
    /**船務送出（SIGNING）後異動庫存, SAVE->SIGNING + quantity; SIGNING->SAVE - quantity
     * @param imReceiveHead
     * @param beforeChangeStatus
     * @param formStatus
     * @param loginUser
     * @throws Exception
     */
    public void updateImReceive2ImOnHand( ImReceiveHead imReceiveHead, boolean isReject, String loginUser, String userType, String typeCode ) throws Exception {
	log.info("updateImReceive2ImOnHand");
	try{
	    String organizationCode = UserUtils.getOrganizationCodeByBrandCode(imReceiveHead.getBrandCode());
	    if (organizationCode == null)
	    	throw new FormException("依據品牌代碼：" + imReceiveHead.getBrandCode() + "查無其組織代號！");

	    List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
	    if (null != imReceiveItems ) {
			log.info("updateImReceive2ImOnHand.imReceiveItems.size =" + imReceiveItems.size());
			Double inUncommitQty = 0D ;
			HashMap map = new HashMap();
			StringBuffer key = new StringBuffer();
			for (ImReceiveItem imReceiveItem : imReceiveItems) {
				if("IMR".equals(typeCode)){
					if("EIP".equals(imReceiveHead.getOrderTypeCode()) && isReject){	
						//inUncommitQty = imReceiveItem.getReceiptQuantity()*(-1);
						if(imReceiveHead.getStatus().equals(OrderStatus.FINISH) || imReceiveHead.getStatus().equals(OrderStatus.CLOSE)){
							inUncommitQty = imReceiveItem.getShortQuantity() > 0 ? imReceiveItem.getShortQuantity()+imReceiveItem.getQuantity()*(-1): imReceiveItem.getReceiptQuantity()*(-1) ;
						}else{
							inUncommitQty = imReceiveItem.getShortQuantity() > 0 ? imReceiveItem.getShortQuantity()*(-1)+imReceiveItem.getReceiptQuantity()*(-1): imReceiveItem.getReceiptQuantity()*(-1) ;
						}						
						log.info("EIP is Reject QTY="+inUncommitQty);
					}
					else if(isReject)
						inUncommitQty = imReceiveItem.getQuantity()*(-1);
					else if("SHIPPING".equals(userType)){
						inUncommitQty = imReceiveItem.getQuantity();
						log.info("SHIPPING Submit"+inUncommitQty);
						}
					else if("EIP".equals(imReceiveHead.getOrderTypeCode())){ 
						inUncommitQty = imReceiveItem.getShortQuantity() > 0 ? imReceiveItem.getShortQuantity()*(-1): 0 ;
						log.info("EIP GetShortQty*-1"+inUncommitQty);
						}
				}else{
					if(!isReject){
						inUncommitQty = imReceiveItem.getQuantity() * (-1);
					}else if("SHIPPING".equals(userType))
						inUncommitQty = imReceiveItem.getQuantity();
				}
				
			    if( inUncommitQty!=0 ){
			    	log.info("算完 inUncommitQty !=0");
					String lotNo = imReceiveHead.getLotNo();
					if("RR".equals(typeCode) )
					    lotNo = imReceiveItem.getLotNo();
					key.delete(0, key.length());
					key.append(imReceiveItem.getItemCode() + "#");
					key.append(lotNo);
				    if (map.get(key.toString()) == null) {
				    	map.put(key.toString(), inUncommitQty);
				    } else {
				    	log.info("算完 inUncommitQty = 0");
				    	map.put(key.toString(), inUncommitQty + ((Double) map.get(key.toString())));
				    }
			    }
			}
			
			Set entrySet = map.entrySet();
			Iterator it = entrySet.iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String[] keyArray = StringUtils.delimitedListToStringArray((String)entry.getKey(), "#");
				
				//如果狀態為結案則還原結案庫存 by Caspar 2013/6/4
				if(OrderStatus.CLOSE.equals(imReceiveHead.getStatus())){
					imOnHandDAO.updateStockOnHand(organizationCode, imReceiveHead.getBrandCode(), keyArray[0], imReceiveHead.getDefaultWarehouseCode(),
						keyArray[1], (Double)entry.getValue(), imReceiveHead.getLastUpdatedBy());					
				}else{log.info("真正updateOnHand");
					imOnHandDAO.updateInUncommitQuantity(organizationCode, keyArray[0], imReceiveHead.getDefaultWarehouseCode(),
						keyArray[1], imReceiveHead.getBrandCode(), (Double)entry.getValue(), imReceiveHead.getLastUpdatedBy());
				}
			}
	    }
	}catch (Exception ex) {
	    log.error("imOnHand 庫存數量異動作業失敗，原因：" + ex.toString());
	    throw new Exception("imOnHand 庫存數量異動作業失敗，原因：" + ex.getMessage());
	}
    }  
    
    
    /**倉管送出（WAREHOUSE）後異動庫存, SAVE->SIGNING + quantity; SIGNING->SAVE - quantity
     * @param imReceiveHead
     * @param beforeChangeStatus
     * @param formStatus
     * @param loginUser
     * @throws Exception
     */
    public void updateImReceive2CmOnHand( ImReceiveHead imReceiveHead, boolean isReject, String userType, String typeCode ) throws Exception {
	//log.info("updateImReceive2CmOnHand");
	try{
	    String organizationCode = UserUtils.getOrganizationCodeByBrandCode(imReceiveHead.getBrandCode());
	    if (organizationCode == null)
	    	throw new FormException("依據品牌代碼：" + imReceiveHead.getBrandCode() + "查無其組織代號！");

	    String customsWarehouseCode = null;
	    ImWarehouse imWarehouse = 
		imWarehouseDAO.findByBrandCodeAndWarehouseCode(imReceiveHead.getBrandCode(), imReceiveHead.getDefaultWarehouseCode(), "Y");
	    if(null==imWarehouse){
	    	throw new FormException("倉庫代碼：" + imReceiveHead.getDefaultWarehouseCode() + "查無其對應報關倉別！");
	    }else{
	    	customsWarehouseCode = imWarehouse.getCustomsWarehouseCode();	// FW, FD...
	    }
	    
		HashMap map = new HashMap();
		StringBuffer key = new StringBuffer();
		
	    List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
	    if (null != imReceiveItems ) {
		//log.info("updateImReceive2CmOnHand.imReceiveItems.size =" + imReceiveItems.size());
		Double inUncommitQty = 0D ;
		for (ImReceiveItem imReceiveItem : imReceiveItems) {	
			//log.info("updateImReceive2CmOnHand:"+imReceiveItem.getItemCode());
		    inUncommitQty = NumberUtils.getDouble(imReceiveItem.getQuantity());
		    
		    if("IMR".equals(typeCode)){
				if(isReject){
					inUncommitQty = inUncommitQty*(-1);
				}else if("SHIPPING".equals(userType)){
					//inUncommitQty = inUncommitQty;
				}else{
					inUncommitQty = 0D;
				}
			}else{
				if(!isReject){
					inUncommitQty = inUncommitQty*(-1);
				}else if("SHIPPING".equals(userType)){
					//inUncommitQty = inUncommitQty;
				}else{
					inUncommitQty = 0D;
				}
			}
		    
		    if( inUncommitQty != 0 ){
			    // 2009.12.13 D7 報單需 update 明細原報單日期，
			    if("D7".equals(imReceiveHead.getDeclarationType()) && "IMR".equals(typeCode)){
			    	if(!StringUtils.hasText(imReceiveItem.getReserve1()))
			    		throw new Exception("進貨明細項次" + imReceiveItem.getIndexNo() + "，品號" + imReceiveItem.getItemCode() + "查無對應之原報關單明細");
			    	CmDeclarationItem cmDeclarationItem = (CmDeclarationItem)
				    cmDeclarationItemDAO.findByPrimaryKey(CmDeclarationItem.class, NumberUtils.getLong(imReceiveItem.getReserve1()) );
			    	if(null == cmDeclarationItem)
			    		throw new Exception("查無Id:" + imReceiveItem.getReserve1() + "之報關單明細");
					if( null!=imReceiveItem.getOriginalDeclarationDate()){
					    cmDeclarationItem.setODeclDate(imReceiveItem.getOriginalDeclarationDate());
					    cmDeclarationItemDAO.update(cmDeclarationItem);
					}
			    }
		    
				String declarationNo  = null;
				Long   declarationSeq = null;
				
				if("RR".equals(typeCode)){	// 進貨退回報單資料由 imReceiveItem 取得
				    declarationNo  = imReceiveItem.getOriginalDeclarationNo();
				    declarationSeq = imReceiveItem.getOriginalDeclarationSeq();
				}else{
				    declarationNo  = imReceiveHead.getDeclarationNo();
				    declarationSeq = imReceiveItem.getDeclarationItemNo();
				}
				
				if(null == declarationNo || null == declarationSeq)
					throw new Exception("進貨明細項次" + imReceiveItem.getIndexNo() + "，品號" + imReceiveItem.getItemCode() + "查無對應之報關單號，報關項次");
				//將所有相同的資料組起來
				key.delete(0, key.length());
				key.append(imReceiveItem.getItemCode() + "#");
				key.append(declarationNo + "#");
				key.append(declarationSeq + "#");
			    if (map.get(key.toString()) == null) {
			    	map.put(key.toString(), inUncommitQty);
			    } else {
			    	map.put(key.toString(), inUncommitQty + ((Double) map.get(key.toString())));
			    }
		    }
		}

		//log.info("updateImReceive2CmOnHand.updateInUncommitQty");
		//這邊扣庫存
		Set entrySet = map.entrySet();
		Iterator it = entrySet.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String[] keyArray = StringUtils.delimitedListToStringArray((String)entry.getKey(), "#");
			if(OrderStatus.CLOSE.equals(imReceiveHead.getStatus())){
				cmDeclarationOnHandDAO.updateStockOnHand(keyArray[1], NumberUtils.getLong(keyArray[2]), keyArray[0], 
						customsWarehouseCode, imReceiveHead.getBrandCode(), (Double)entry.getValue(), imReceiveHead.getLastUpdatedBy());
//				cmDeclarationOnHandDAO.updateInUncommitQty(keyArray[1], NumberUtils.getLong(keyArray[2]), keyArray[0], 
//						customsWarehouseCode, imReceiveHead.getBrandCode(), (Double)entry.getValue(), imReceiveHead.getLastUpdatedBy());
			}else{
				cmDeclarationOnHandDAO.updateInUncommitQty(keyArray[1], NumberUtils.getLong(keyArray[2]), keyArray[0], 
					customsWarehouseCode, imReceiveHead.getBrandCode(), (Double)entry.getValue(), imReceiveHead.getLastUpdatedBy());
			}
		}
	    }
	}catch (Exception ex) {
	    log.error("cmDeclarationOnHand 庫存數量異動作業失敗，原因：" + ex.toString());
	    throw new Exception("cmDeclarationOnHand 庫存數量異動作業失敗，原因：" + ex.getMessage());
	}
    }
    
    /**進貨退回 && SHIPPING Invoice 以及扣除預算
     * @param imReceiveHead
     * @param isReject
     * @param loginUser
     * @throws userType
     */
    public void updateImReceiveItem2FiInvoice(ImReceiveHead imReceiveHead, boolean isReject, String employeeCode, 
    		String userType, BuBrand buBrand) throws Exception{
    	if("EOF".equals(imReceiveHead.getOrderTypeCode())){		//T2->EOF
    		if( isReject ){	// REJECT 刪除原建立 Invoice 
    		    String invoiceNo = imReceiveHead.getOrderTypeCode() + imReceiveHead.getOrderNo();
    		    List<FiInvoiceHead> fiInvoiceHeads = fiInvoiceHeadDAO.findByProperty("FiInvoiceHead", "invoiceNo", invoiceNo);
    		    for(FiInvoiceHead fiInvoiceHead : fiInvoiceHeads){
    		    	fiInvoiceHeadDAO.delete(fiInvoiceHead);
    		    }
    		}else{		// 產生 Invoice && 起始流程
    		    insertFiInvoice(imReceiveHead, buBrand.getBranchCode());
    		}
	    }
	    // 不扣除 PO 已進貨數量, 以 imReceiveHead.budgetYear, budgetMonth 扣除預算已進貨金額
    	log.info("updateImReceiveItem2FiInvoice");
    	//不用進貨預算201503測試移除
	   /* for( ImReceiveItem imReceiveItem : imReceiveHead.getImReceiveItems()){
    		Double receivedAmoount = 0D;
    		if("C".equals(buBrand.getBudgetType())){	// 預算扣除方式 P:UnitPrice/C:Cost/T:整筆扣除
    		    receivedAmoount = NumberUtils.getDouble(imReceiveItem.getQuantity()) * NumberUtils.getDouble(imReceiveItem.getLocalUnitPrice());
    		}else{
    		    receivedAmoount = NumberUtils.getDouble(imReceiveItem.getQuantity()) * NumberUtils.getDouble(imReceiveItem.getUnitPrice());
    		}
    		log.info("updateFiInvoice"+receivedAmoount);
    		fiBudgetLineService.updateFiBudgetLineByItemCode(imReceiveHead.getBrandCode(), 
					imReceiveHead.getBudgetYear(), imReceiveHead.getBudgetMonth(),
					imReceiveItem.getItemCode(), "RECEIVE", receivedAmoount, employeeCode, isReject);
	    }*/
    }
    
    /**
     * 匯入進貨單明細
     * @param headId
     * @param lineLists
     * @throws Exception
     */
    public void executeImportImLists(Long headId, List lineLists) throws Exception{
	try{
    	if(lineLists != null && lineLists.size() > 0){
    		ImReceiveHead imReceiveHeadTmp = findById(headId);
    		String orderTypeCode = imReceiveHeadTmp.getOrderTypeCode();
    		String status = imReceiveHeadTmp.getStatus();
    		String orderType = "";
    		//刪掉line重新載入
    		if(OrderStatus.SAVE.equals(status)||OrderStatus.REJECT.equals(status)||OrderStatus.UNCONFIRMED.equals(status)){
    			List<ImReceiveItem> items  = new ArrayList<ImReceiveItem>(0);
	    		for(int i = 0; i < lineLists.size(); i++){
	    		    ImReceiveItem imReceiveItem = (ImReceiveItem)lineLists.get(i);
	    		    
	    		    Double lastForeignUnitCost = 0D;
	    		    Double standardPurchaseCost = 0D;
	    		    	    Double foreignUnitPriceOri = 0D;
				    
				    ImItem imItem = imItemService.findItem(imReceiveHeadTmp.getBrandCode(), imReceiveItem.getItemCode());
	    		    if (null!=imItem){
				    	foreignUnitPriceOri = imItem.getPurchaseAmount();
				     }
			    
	    		    standardPurchaseCost = poPurchaseOrderLineMainService.getAverageUnitCost(imReceiveItem.getItemCode(), imReceiveHeadTmp.getBrandCode());
	    		    		
	    		    if("EOF".equals(orderTypeCode) || "EOP".equals(orderTypeCode))
			    		lastForeignUnitCost = poPurchaseOrderLineMainService.getLastUnitCost(imReceiveItem.getItemCode());
			    	else{
			    		lastForeignUnitCost = standardPurchaseCost;
			    	}
	    		    
			    	imReceiveItem.setLastForeignUnitCost(lastForeignUnitCost);
			    	imReceiveItem.setStandardPurchaseCost(standardPurchaseCost);
			    	imReceiveItem.setForeignUnitPriceOri(foreignUnitPriceOri);
	    		    imReceiveItem.setAcceptQuantity( imReceiveItem.getQuantity());
	    		    imReceiveItem.setReceiptQuantity(imReceiveItem.getQuantity());
	    		    imReceiveItem.setBarcodeCount(   imReceiveItem.getQuantity());
	    		    imReceiveItem.setDefectQuantity( 0D);
	    		    imReceiveItem.setDiffQty(        0D);
	    		    imReceiveItem.setShortQuantity(  0D);
	    		    imReceiveItem.setSampleQuantity( 0D);
	    		    imReceiveItem.setLotNo(SystemConfig.LOT_NO);
			    	countItemTotalAmountForAjax(imReceiveHeadTmp, imReceiveItem);
			    	if("EOF".equals(orderTypeCode)){
					    orderType = "EIF";
					}else if("EOP".equals(orderTypeCode)){
					    orderType = "EIP";
					}else if("RRF".equals(orderTypeCode)){
					    orderType = "IRF";
					}else if("RRL".equals(orderTypeCode)){
					    orderType = "IRL";
					}
					headId = imReceiveHeadDAO.findOrderNoByItemCode(imReceiveHeadTmp.getBrandCode(), orderType, imReceiveItem.getItemCode() );
					if(null != headId && "EOP".equals(orderTypeCode)){
						ImReceiveHead imReceiveHead = imReceiveHeadDAO.findById(headId);
						if( null == imReceiveHead ){
						    //orderNo = "無完成 or 結案進貨單";
						}else{
						    if("EOP".equals(orderTypeCode)){
						    	imReceiveItem.setOriginalDeclarationNo(imReceiveHead.getOrderNo());
						    	imReceiveItem.setLotNo(imReceiveHead.getLotNo());
						    }
						}
					}
	    		    imReceiveItem.setImReceiveHead(imReceiveHeadTmp);
	    		    items.add(imReceiveItem);
	    		}
	    		imReceiveHeadTmp.setImReceiveItems(items);
	    	//倉管複核數量
    		}else{
    			if(imReceiveHeadTmp.getImReceiveItems().size() != lineLists.size())
    				throw new Exception("進貨單明細數量與匯入的數量不符合");
    			for(int i = 0; i < lineLists.size(); i++){
    				ImReceiveItem lineList = (ImReceiveItem)lineLists.get(i);
    				ImReceiveItem imReceiveItem = (ImReceiveItem)imReceiveHeadTmp.getImReceiveItems().get(i);
    				if(NumberUtils.getDouble(lineList.getAcceptQuantity()) > 0)
    					imReceiveItem.setAcceptQuantity(NumberUtils.getDouble(lineList.getAcceptQuantity()));
    				if(NumberUtils.getDouble(lineList.getDefectQuantity()) > 0)
    					imReceiveItem.setDefectQuantity(NumberUtils.getDouble(lineList.getDefectQuantity()));
    				if(NumberUtils.getDouble(lineList.getSampleQuantity()) > 0)
    					imReceiveItem.setSampleQuantity(NumberUtils.getDouble(lineList.getSampleQuantity()));
    				if(NumberUtils.getDouble(lineList.getShortQuantity()) > 0)
    					imReceiveItem.setShortQuantity(NumberUtils.getDouble(lineList.getShortQuantity()));
    			}
    		}
	    		imReceiveHeadDAO.update(imReceiveHeadTmp);
    	}
    }catch (Exception ex) {
        log.error("進貨單明細匯入時發生錯誤，原因：" + ex.toString());
        throw new Exception("進貨單明細匯入時發生錯誤，原因：" + ex.getMessage());
	}        
    }
    
    
    /** 由 JS 呼叫 Report
     * @param httpRequest
     * @throws Exception
     */
    public List<Properties>  getReportURL(Properties httpRequest) throws Exception {
	//log.info("getReportURL");
	List       re   = new ArrayList();
	Properties pro  = new Properties();
	String errorMsg = null;
	String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	String brandCode     = httpRequest.getProperty("brandCode");
	String employeeCode  = httpRequest.getProperty("employeeCode");
	String orderNo       = httpRequest.getProperty("orderNo");
	String reportName    = httpRequest.getProperty("reportName");
	
	if("NONE".equals(reportName)){
	    //log.info("getReportURL 1 "+orderTypeCode+"-"+brandCode+"-"+employeeCode+"-"+orderNo);
	    errorMsg = SystemConfig.getReportURLString(orderTypeCode, brandCode, employeeCode, orderNo);
	} else {
	    //log.info("getReportURL 2 "+orderTypeCode+"-"+brandCode+"-"+employeeCode+"-"+orderNo+"-"+reportName);
	    errorMsg = SystemConfig.getReportURLString(orderTypeCode, brandCode, employeeCode, orderNo, reportName);
	}
	//log.info(errorMsg);
	pro.setProperty("returnURL", errorMsg); 
	re.add(pro);
	return re;
    }
    
    
    /**檢核 ImReceive 資料
     * @param imReceiveHead
     * @param conditionMap
     * @return List
     * @throws ValidationErrorException
     */
    public Integer deleteProgramLog(Map parameterMap, Integer opType) throws Exception {
	//log.info("deleteProgramLog");
	Integer errorCnt = 0;
	try{
        Object formLinkBean = parameterMap.get("vatBeanFormLink");
        Long headId = getImReceiveHeadId(formLinkBean);
        ImReceiveHead imReceivePO = getActualImReceive(headId);
        String identification =
    	MessageStatus.getIdentification(imReceivePO.getBrandCode(), imReceivePO.getOrderTypeCode(), imReceivePO.getOrderNo() );
        if( opType==0 ){
    	// clear 原有 ERROR RECORD
        	siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
        }else{
        	List errorLogs = siProgramLogDAO.findByIdentification(PROGRAM_ID, null, identification);
        	if(null!=errorLogs)
        		errorCnt = errorLogs.size();
        }
        return errorCnt;
	}catch (Exception ex) {
	    log.error("進貨單刪除 Program Log 失敗，原因：" + ex.toString());
	    throw new Exception("進貨單刪除 Program Log 失敗，原因：" + ex.getMessage());
	}
    }
 
    
    public List executeBatchImportT2(List<ImReceiveHead> imReceiveHeads, List assembly) throws Exception{
        try{
        	//log.info("executeBatchImportT2");
            if(imReceiveHeads != null){
        	    for(int i = 0; i < imReceiveHeads.size(); i++){
        	    	ImReceiveHead head = (ImReceiveHead)imReceiveHeads.get(i);
        	        modifyBatch(head);
        	        assembly.add(head.getBrandCode() + "-" + head.getOrderTypeCode() + "-" + head.getOrderNo());
        	    }
            }
            return assembly;
        }catch(Exception ex){
    	log.error("執行進貨資料批次匯入失敗，原因：" + ex.toString());
    	throw ex;
        }
    }
    
    public void modifyBatch(ImReceiveHead obj) throws Exception{
    	ImReceiveHead head = findByIdentification(obj.getBrandCode(), obj.getOrderTypeCode(), obj.getOrderNo());
    	Map aggregateQuantitys = null;
    	if(head == null){
    		log.error("查無此進貨單");
    	}else{
			aggregateQuantitys = getAggregateQuantitys(obj);
   			aggregateLineBatch(head, aggregateQuantitys);
   			imReceiveHeadDAO.update(head);
    	}
    }
    
    public Map getAggregateQuantitys(ImReceiveHead obj){
    	Map aggregates = new HashMap();
    	List<ImReceiveItem> objItems = obj.getImReceiveItems();
    	for (Iterator iterator = objItems.iterator(); iterator.hasNext();) {
    		ImReceiveItem item = (ImReceiveItem) iterator.next();
			aggregates.put(item.getItemCode(),item.getQuantity());
		}
    	return aggregates;
    }
    
    /**
	 * 依照匯入的資料重組LINE
	 * @param ImReceiveHead
	 * @param Map
	 * @return 
	 * @throws Exception
	 */
    public void aggregateLineBatch(ImReceiveHead head, Map aggregates){
    	List<ImReceiveItem> items = head.getImReceiveItems();
    	for (Iterator iterator = items.iterator(); iterator.hasNext();) {
    		ImReceiveItem item = (ImReceiveItem) iterator.next();
			Double aggregateQuantity = NumberUtils.getDouble((Double)aggregates.get(item.getItemCode()));
			if("D".equals(item.getReceiveItemType())){
				if(item.getQuantity() - aggregateQuantity >=0){
					item.setDefectQuantity(aggregateQuantity);
					item.setAcceptQuantity(item.getQuantity() - aggregateQuantity);
					aggregates.remove(item.getItemCode());
				}else{
					item.setDefectQuantity(item.getQuantity());
					item.setAcceptQuantity(0D);
					aggregates.put(item.getItemCode(), aggregateQuantity - item.getQuantity());
				}
			}else{
				if(item.getQuantity() - aggregateQuantity >=0){
					item.setAcceptQuantity(aggregateQuantity);
					item.setDefectQuantity(item.getQuantity() - aggregateQuantity);
					aggregates.remove(item.getItemCode());
				}else{
					item.setAcceptQuantity(item.getQuantity());
					item.setDefectQuantity(0D);
					aggregates.put(item.getItemCode(), aggregateQuantity - item.getQuantity());
				}
			}
		}
    	head.setImReceiveItems(items);
    }
    
    /**
	 * 依據品牌代號、單別、單號查詢
	 * 
	 * @param brandCode
	 * @param orderTypeCode
	 * @param orderNo
	 * @return ImMovementHead
	 * @throws Exception
	 */
	 public ImReceiveHead findByIdentification(String brandCode, String orderTypeCode, String orderNo) throws Exception{
         try{
	         return imReceiveHeadDAO.findByIdentification(brandCode, orderTypeCode, orderNo);
	     }catch (Exception ex) {
	    	 log.error("依據品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "查詢進貨單時發生錯誤，原因：" + ex.toString());
	    	 throw new Exception("依據品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "查詢進貨單時發生錯誤，原因：" + ex.getMessage());
	     }
	 }
	 
	 
    /**處理AJAX參數
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
	 public List<Properties> getAJAXLcDataBySupplier(Properties httpRequest) throws Exception{
		 //log.info("getAJAXLcDataBySupplier");
		 List<Properties> result = new ArrayList();
		 Properties properties   = new Properties();
		 try{
			 HashMap conditionMap = new HashMap();
			 conditionMap.put("brandCode",    httpRequest.getProperty("brandCode"));
			 conditionMap.put("supplierCode", httpRequest.getProperty("supplierCode"));
			 conditionMap.put("status_Start",   OrderStatus.FINISH );
			 conditionMap.put("status_End",   OrderStatus.FINISH );

			 List<ImLetterOfCreditHead> allLCList = imLetterOfCreditService.findLetterOfCreditList(conditionMap);
			 properties.setProperty("allLCList", AjaxUtils.parseSelectorData(AjaxUtils.produceSelectorData( allLCList, "lcNo", "lcNo", false, true)));
			 result.add(properties);
			 return result;
		 }catch (Exception ex) {
			 log.error("查詢 LC 資料時發生錯誤，原因：" + ex.getMessage());
			 throw new Exception("查詢 LC 資料時發生錯誤失敗！");
		 }
	 }
    
	 public List<Properties> getAJAXDefPoOrderNo(Properties httpRequest) throws FormException {
		 //log.info("getAJAXTaxRate");
		 List re = new ArrayList();
		 String brandCode = httpRequest.getProperty("brandCode");
		 String defPoOrderNo = httpRequest.getProperty("defPoOrderNo");
		 String defPoOrderTypeCode = httpRequest.getProperty("defPoOrderTypeCode");
		 
		 List<PoPurchaseOrderHead> poPurchaseOrderHeads = 
				poPurchaseOrderHeadDAO.findByProperty("PoPurchaseOrderHead", 
						new String []{"brandCode", "orderNo" , "orderTypeCode" , "status"}, 
						new String []{brandCode, defPoOrderNo , defPoOrderTypeCode, OrderStatus.FINISH}); 
		 
		 Properties pro = new Properties();
		 if(null!=poPurchaseOrderHeads && poPurchaseOrderHeads.size()>0){
			 PoPurchaseOrderHead poPurchaseOrderHead = poPurchaseOrderHeads.get(0);
			 pro.setProperty("ReturnMessage", "OK");
			 pro.setProperty("DefaultWarehouseCode", poPurchaseOrderHead.getDefaultWarehouseCode());
			 pro.setProperty("SupplierCode", poPurchaseOrderHead.getSupplierCode());
		 }
		 re.add(pro);
		 return re;
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
			String reportFileName = (String)PropertyUtils.getProperty(otherBean, "reportFileName");
			String warehouseInDate = (String)PropertyUtils.getProperty(otherBean, "warehouseInDate");
			String itemCategory = (String)PropertyUtils.getProperty(otherBean, "itemCategory");
			Map returnMap = new HashMap(0);
			Map parameters = new HashMap(0);
			if("IM0601_T2.rpt".equals(reportFileName)){
				parameters.put("prompt0", orderTypeCode);
				parameters.put("prompt1", orderNo);
			}else if("po0633.rpt".equals(reportFileName)){
				parameters.put("prompt0", brandCode);
				parameters.put("prompt1", orderTypeCode);
				parameters.put("prompt2", "");
				parameters.put("prompt3", "");
				parameters.put("prompt4", orderNo);
				parameters.put("prompt5", orderNo);
				parameters.put("prompt6", warehouseInDate);
				parameters.put("prompt7", warehouseInDate);
			}else if("po0632.rpt".equals(reportFileName)){
				parameters.put("prompt0", brandCode);
				parameters.put("prompt1", orderTypeCode);
				parameters.put("prompt2", "");
				parameters.put("prompt3", "");
				parameters.put("prompt4", orderNo);
				parameters.put("prompt5", orderNo);
			}else if("po0644.rpt".equals(reportFileName)){
				parameters.put("prompt0", brandCode);
				parameters.put("prompt1", orderTypeCode);
				parameters.put("prompt2", "");
				parameters.put("prompt3", "");
				parameters.put("prompt4", orderNo);
				parameters.put("prompt5", orderNo);
				parameters.put("prompt6", itemCategory);
			}else if("T2".equals(brandCode)){
				//CC後面要代的參數使用parameters傳遞			
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
				//parameters.put("prompt3", orderNo);
				//parameters.put("prompt4", displayAmt);
				//parameters.put("prompt5", );
			}
			String reportUrl = new String("");
			try{
				if(StringUtils.hasText(reportFileName))
					reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, reportFileName, parameters);
				else
					reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, parameters);
			}catch(Exception ex){
				reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, parameters);
			}
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
	
	public Object executeReverter(Long headId, String employeeCode) throws Exception {
		log.info("開始執行反確認headId = " + headId);

		//找出進貨單
		log.info("找進貨單");
		ImReceiveHead head = findById(headId);
		if(null == head)
			throw new Exception("查無進貨單主鍵: " + headId +" 對應之出貨單");
		String beforeChangeStatus = head.getStatus();
		String nextStatus = OrderStatus.REJECT;
		
		//找出員工資訊
		log.info("找員工資訊");
		BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(head.getBrandCode(), employeeCode);
		if(employeeWithAddressView == null){
			throw new Exception("查無員工代號: " + employeeCode +" 對應之員工");
		}
		
		//找調撥單
		log.info("尋找調撥單");
		List <ImMovementHead> moveReciece = imMovementHeadDAO.findMovementList(head.getBrandCode(),head.getOrderTypeCode(),head.getOrderNo());
		log.info("moveReciece"+moveReciece.size());

		for (ImMovementHead imMovementHead : moveReciece) {
			if(OrderStatus.SAVE.equals(imMovementHead.getStatus())){
				throw new Exception ("來源單號"+imMovementHead.getOriginalOrderNo()+"已進貨產生調撥單暫存中，請作廢才可反確認");
			}else if(OrderStatus.VOID.equals(imMovementHead.getStatus())){
			}else{
				throw new Exception ("已驗收進貨產生調撥單，請確認作廢才可執行反確認");
			}
		}

		//找出單別資訊
		log.info("找單別");
		BuBrand buBrand = buBrandService.findById( head.getBrandCode() );
		String orderTypeCode = head.getOrderTypeCode();
		BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(head.getBrandCode(), orderTypeCode) );
		String typeCode = buOrderType.getTypeCode();
		String orderTypeCondition = buOrderType.getOrderCondition();
		
		String dateType = "進貨單日期";
		Date orderDate = null;
		
		if(!"T2".equals(head.getBrandCode()))
			orderDate = head.getOrderDate();
		else if("IMR".equals(typeCode))
			orderDate = head.getReceiptDate();
		else
			orderDate = head.getWarehouseInDate();
		
		//反確認
		log.info("開始執行反確認");
		boolean isReject = false;
    	if(( nextStatus.equals(OrderStatus.REJECT) && "IMR".equals(typeCode) ) ||	// 駁回  && 進貨
    	   (!nextStatus.equals(OrderStatus.REJECT) &&  "RR".equals(typeCode) ) ){	// 非駁回  && 進貨退出
    	    isReject = true;
    	}
    	
		//只有簽核完成跟結案才能處理
		if(OrderStatus.FINISH.equals(beforeChangeStatus) || OrderStatus.CLOSE.equals(beforeChangeStatus)){
		//if(OrderStatus.FINISH.equals(beforeChangeStatus)){
			log.info("簽核完成開始處理");
			//檢核是否關帳後
			ValidateUtil.isAfterClose(head.getBrandCode(), head.getOrderTypeCode(), dateType, orderDate,"PO");
			
			//刪除transation
			imTransationDAO.deleteTransationByIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
			
			//庫存還原
			log.info("庫存還原");
			updateImReceive2ImOnHand( head, isReject, employeeCode, "WAREHOUSE", typeCode );
			
			if("EIF".equals(orderTypeCode) || "EOF".equals(orderTypeCode)){
				log.info("報單庫存還原");
    	    	updateImReceive2CmOnHand( head, isReject, "WAREHOUSE", typeCode );
    	    }
			
			//如果是進貨
			if("IMR".equals(typeCode) && "PO".equals(orderTypeCondition) ){
				updateImReceiveItem2PoLine( head, isReject, employeeCode, "WAREHOUSE" );
				//updateImReceive2FiBueget(head, isReject, "WAREHOUSE");
			//退貨
			}else{
				updateImReceiveItem2FiInvoice(head, isReject, employeeCode, "WAREHOUSE", buBrand);
			}
			//還報單狀態			
			if("EIF".equals(orderTypeCode)){
				log.info("報單狀態還原");
    	    	CmDeclarationHead cmDecl = cmDeclarationHeadDAO.findOneCmDeclaration(head.getDeclarationNo());
    	    	cmDecl.setStatus(OrderStatus.WAIT_IN);
    	    }	
				
			//狀態修改
			head.setStatus(OrderStatus.SAVE);
			head.setWarehouseStatus(OrderStatus.SAVE);
			head.setExpenseStatus(OrderStatus.SAVE);

			//日期修改
			head.setWarehouseInDate(null);
			head.setReceiptDate(null);
			
			//設置流程起始人
			head.setProcessId(null);
			head.setCreatedBy(employeeCode);
			head.setLastUpdateDate(new Date());
			imReceiveHeadDAO.update(head);
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
		ImReceiveHead head = (ImReceiveHead)bean;
		Object processObj[] =  ImReceiveHeadMainService.startProcess(head);  
		head.setProcessId((Long)processObj[0]);
		imReceiveHeadDAO.update(head);
	}

	/** updateAjaxDate 
	 * @param
	 * @throws FormException
	 */
	public List<Properties> updateAjaxDate(Properties httpRequest) throws FormException {
		//log.info("updateAjaxDate");
		List re = new ArrayList();
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		String warehouseInDate = httpRequest.getProperty("warehouseInDate");
		ImReceiveHead head = findById(headId);
		if(null != head){
			head.setWarehouseInDate(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, warehouseInDate));
			imReceiveHeadDAO.update(head);
		}
		return re;
	}

	
	/** updateExpense 
	 * @param
	 * @throws FormException
	 */
	public List<Properties> updateExpense(Properties httpRequest) throws FormException {
		//log.info("updateAjaxDate");
		List re = new ArrayList();
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		String orderDate = httpRequest.getProperty("orderDate");
		ImReceiveHead head = findById(headId);
		if(null != head){
			countHeadTotalAmount(head);
			head.setOrderDate(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, orderDate));
			imReceiveHeadDAO.update(head);
		}
		return re;
	}
	
	/**
     * 匯出 by sql
     * @return
     * @throws Exception
     */
    public SelectDataInfo getAJAXExportDataBySql(HttpServletRequest httpRequest) throws Exception{
	Object[] object = null;
	List rowData = new ArrayList();
	StringBuffer sql = new StringBuffer();
	try {
		
		Long headId = NumberUtils.getLong(httpRequest.getParameter("headId"));
		String type = httpRequest.getParameter("type");
		ImReceiveHead head = findById(headId);
		
		if("F".equals(type)){
			sql.append(" select R.INDEX_NO,R.ITEM_CODE, R.QUANTITY, R.FOREIGN_UNIT_PRICE, R.LAST_FOREIGN_UNIT_COST, ")
			.append(" R.INVOICE_NO, R.PO_NO, R.ORIGINAL_DECLARATION_DATE, R.ORIGINAL_DECLARATION_NO, R.ORIGINAL_DECLARATION_SEQ,")
			.append(" R.PO_ORDER_NO, C.CATEGORY05, C.CATEGORY06, C.CATEGORY02, I.CATEGORY_NAME, C.FOREIGN_CATEGORY, C.CATEGORY13, C.SUPPLIER_ITEM_CODE, C.CATEGORY10, C.ITEM_C_NAME, C.UNIT_PRICE, R.ORDER_NO") 
			.append(" from (select * from IM_RECEIVE_HEAD H, IM_RECEIVE_ITEM R ")
			.append("   where H.head_id = R.head_id and H.head_id = "+ headId +") R, ")
			.append(" IM_ITEM_CURRENT_PRICE_VIEW C, ")
			.append(" (select * from IM_ITEM_CATEGORY where CATEGORY_TYPE = 'CATEGORY02' and ENABLE = 'Y') I ")
			.append(" where R.BRAND_CODE = C.BRAND_CODE (+)")
			.append(" and R.ITEM_CODE = C.ITEM_CODE (+)")
			.append(" and C.BRAND_CODE = I.BRAND_CODE (+)")
			.append(" and C.CATEGORY02 = I.CATEGORY_CODE (+)")
			.append(" order by R.INDEX_NO");

			object = new Object[] { 
					"indexNo","itemCode", "quantity",	"foreignUnitPrice", "lastForeignUnitCost", 
					"invoiceNo", "poNo", "originalDeclarationDate", "originalDeclarationNo", 
					"originalDeclarationSeq", "poOrderNo", "category05", "category06",
					"category02", "category02Name", "foreignCategory", "category13", 
					"supplierItemCode", "category10", "itemCName","unitPrice", "orderNo"};
			
		}else if ("P".equals(type)){
			sql.append(" select R.INDEX_NO,R.ITEM_CODE, R.QUANTITY, R.FOREIGN_UNIT_PRICE, R.LAST_FOREIGN_UNIT_COST, ")
			.append(" R.PO_ORDER_NO, R.PO_NO, C.CATEGORY05, C.CATEGORY06, C.CATEGORY02, I.CATEGORY_NAME, C.FOREIGN_CATEGORY, C.CATEGORY13, C.SUPPLIER_ITEM_CODE, C.CATEGORY10, C.ITEM_C_NAME, C.UNIT_PRICE, R.ORDER_NO") 
			.append(" from (select * from IM_RECEIVE_HEAD H, IM_RECEIVE_ITEM R ")
			.append("   where H.head_id = R.head_id and H.head_id = "+ headId +") R, ")
			.append(" IM_ITEM_CURRENT_PRICE_VIEW C, ")
			.append(" (select * from IM_ITEM_CATEGORY where CATEGORY_TYPE = 'CATEGORY02' and ENABLE = 'Y') I ")
			.append(" where R.BRAND_CODE = C.BRAND_CODE (+)")
			.append(" and R.ITEM_CODE = C.ITEM_CODE (+)")
			.append(" and C.BRAND_CODE = I.BRAND_CODE (+)")
			.append(" and C.CATEGORY02 = I.CATEGORY_CODE (+)")
			.append(" order by R.INDEX_NO");

			object = new Object[] { 
					"indexNo","itemCode", "quantity",	"foreignUnitPrice", "lastForeignUnitCost", 
					"poOrderNo", "poNo", "category05", "category06", 
					"category02", "category02Name", "foreignCategory", "category13", 
					"supplierItemCode", "category10", "itemCName", "unitPrice", "orderNo"};
			
		}else if("receiveBarcodeName".equals(type)){
			sql.append(" SELECT R.ITEM_CODE, C.ITEM_C_NAME, TO_CHAR (C.UNIT_PRICE, 'FM999,999,999') as UNIT_PRICE, R.BARCODE_COUNT ")
			.append(" FROM IM_RECEIVE_HEAD H, IM_RECEIVE_ITEM R, IM_ITEM_CURRENT_PRICE_VIEW C ")
			.append(" where H.head_id = ").append(headId)
			.append(" AND H.head_id = R.head_id AND R.ITEM_CODE = C.ITEM_CODE ")
			.append(" AND H.brand_code = C.brand_code ORDER BY R.ITEM_CODE");

			object = new Object[] { "itemCode", "itemCName", "unitPrice", "barcodeCount" };
			
		}else if("receiveBarcode".equals(type)){
			sql.append(" SELECT R.ITEM_CODE, TO_CHAR (C.UNIT_PRICE, 'FM999,999,999') as UNIT_PRICE, R.BARCODE_COUNT ")
			.append(" FROM IM_RECEIVE_HEAD H, IM_RECEIVE_ITEM R, IM_ITEM_CURRENT_PRICE_VIEW C ")
			.append(" where H.head_id = ").append(headId)
			.append(" AND H.head_id = R.head_id AND R.ITEM_CODE = C.ITEM_CODE ")
			.append(" AND H.brand_code = C.brand_code ORDER BY R.ITEM_CODE");

			object = new Object[] { "itemCode", "unitPrice", "barcodeCount" };
			
		}else if("receiveBarcodeNew".equals(type)){
			sql.append(" SELECT R.ITEM_CODE, R.ITEM_C_NAME, B.COUNTRY_C_NAME as CATEGORY14, R.CATEGORY08, R.ORDER_DATE, R.UNIT_PRICE, R.QUANTITY ")
			.append(" FROM (SELECT   R.ITEM_CODE, C.ITEM_C_NAME, C.CATEGORY08, C.CATEGORY14, ");
			if("T1CO".equals(head.getBrandCode()))
				sql.append(" TO_CHAR (ADD_MONTHS(H.ORDER_DATE, -1) , 'YYYY') || '.' || TO_CHAR (ADD_MONTHS(H.ORDER_DATE, -1), 'MM') as ORDER_DATE, ");
			else
				sql.append(" TO_CHAR (H.ORDER_DATE, 'YYYY') || '.' || TO_CHAR (H.ORDER_DATE, 'MM') as ORDER_DATE, ");
			sql.append(" TO_CHAR (C.UNIT_PRICE, 'FM999,999,999') as UNIT_PRICE, R.QUANTITY ")
			.append(" FROM   IM_RECEIVE_HEAD H, IM_RECEIVE_ITEM R, IM_ITEM_CURRENT_PRICE_VIEW C ")
			.append(" where H.head_id = ").append(headId)
			.append(" AND H.head_id = R.head_id AND R.ITEM_CODE = C.ITEM_CODE ")
			.append(" AND H.brand_code = C.brand_code ORDER BY R.ITEM_CODE) R ")
			.append(" LEFT JOIN BU_COUNTRY B ON R.CATEGORY14 = B.COUNTRY_CODE");

			object = new Object[] { 
					"itemCode", "itemCName", "category14", "category08", "orderDate", 
					"unitPrice", "quantity"};
			
		}else if("receiveBarcodeT2Jewerly".equals(type)){
			sql.append(" SELECT R.ITEM_CODE, I.ITEM_C_NAME, R.RECEIPT_QUANTITY, I.CATEGORY14, I.MATERIAL, ")
			.append(" TO_CHAR (ADD_MONTHS(H.RECEIPT_DATE, -2) , 'YYYYMMDD') as RECEIPT_DATE ")
			.append(" FROM   IM_RECEIVE_HEAD H, IM_RECEIVE_ITEM R, IM_ITEM I ")
			.append(" WHERE H.HEAD_ID = ").append(headId)
			.append(" AND H.HEAD_ID = R.HEAD_ID AND H.BRAND_CODE = I.BRAND_CODE AND R.ITEM_CODE = I.ITEM_CODE ");

			object = new Object[] { 
					"itemCode", "itemCName", "receiptQuantity", "category14", "material", "receiptDate"};
			
		}else if("search".equals(type)){
			
	    	String startDate     = httpRequest.getParameter("startDate");
		    String endDate       = httpRequest.getParameter("endDate");
		    //Date actualStartDate = null;
		    // Date actualEndDate   = null;
		    //if(StringUtils.hasText(startDate))
		    	//actualStartDate = DateUtils.parseDate("yyyy/MM/dd", startDate);
		    //if(StringUtils.hasText(endDate))
		    	//actualEndDate = DateUtils.parseDate("yyyy/MM/dd", endDate);
		     	   
		    String startIDate     = httpRequest.getParameter("startIDate");
		    String endIDate       = httpRequest.getParameter("endIDate");
		    //Date actualStartIDate = null;
		    //Date actualEndIDate   = null;
		    //if(StringUtils.hasText(startIDate))
		    	//actualStartIDate = DateUtils.parseDate("yyyy/MM/dd", startIDate);
		    //if(StringUtils.hasText(endIDate))
		    	//actualEndIDate = DateUtils.parseDate("yyyy/MM/dd", endIDate);
		    
		    String startRDate     = httpRequest.getParameter("startRDate");
		    String endRDate       = httpRequest.getParameter("endRDate");
		    //Date actualStartRDate = null;
		    //Date actualEndRDate   = null;
		    //if(StringUtils.hasText(startRDate))
		    	//actualStartRDate = DateUtils.parseDate("yyyy/MM/dd", startRDate);
		    //if(StringUtils.hasText(endRDate))
		    	//actualEndRDate = DateUtils.parseDate("yyyy/MM/dd", endRDate);
		    
		    String brandCode = httpRequest.getParameter("brandCode");
		    String orderTypeCode = httpRequest.getParameter("orderTypeCode");
		    String status = httpRequest.getParameter("status");
		    String startOrderNo = httpRequest.getParameter("startOrderNo");
		    String endOrderNo = httpRequest.getParameter("endOrderNo");
		    String startSourceOrderNo = httpRequest.getParameter("startSourceOrderNo");
		    String endSourceOrderNo = httpRequest.getParameter("endSourceOrderNo");
		    String declarationNo = httpRequest.getParameter("declarationNo");
		    String warehouseStatus = httpRequest.getParameter("warehouseStatus");
		    String expenseStatus = httpRequest.getParameter("expenseStatus");
		    String itemCategory = httpRequest.getParameter("itemCategory");
		    String supplierCode = httpRequest.getParameter("supplierCode");
		    String startDateDiff = httpRequest.getParameter("startDateDiff");
		    String endDateDiff = httpRequest.getParameter("endDateDiff");
		    String itemCode = httpRequest.getParameter("itemCode");
		    
			sql.append(" SELECT R.ORDER_TYPE_CODE, R.ORDER_NO , nvl(sum(nvl(receipt_Quantity ,0)),0) as ReceiveQuantity, R.ITEM_CATEGORY, I.CATEGORY_NAME, ")
			   .append(" R.SUPPLIER_CODE, B.CHINESE_NAME, R.DECLARATION_NO, R.WAREHOUSE_IN_DATE, ")
			   .append(" R.RECEIPT_DATE, R.STATUS, R.WAREHOUSE_STATUS, R.EXPENSE_STATUS, R.SOURCE_ORDER_NO ")
			   .append(" FROM IM_RECEIVE_HEAD R, IM_RECEIVE_ITEM L, ")
			   .append(" BU_SUPPLIER_WITH_ADDRESS_VIEW B, IM_ITEM_CATEGORY I ")
			   .append(" WHERE SUBSTR(ORDER_NO,1,3) <> 'TMP' ")
			   .append(" AND R.HEAD_ID = L.HEAD_ID(+) ")
			   .append(" AND R.BRAND_CODE = B.BRAND_CODE (+) ")
			   .append(" AND R.SUPPLIER_CODE = B.SUPPLIER_CODE (+) ")
			   .append(" AND R.BRAND_CODE = I.BRAND_CODE (+) ")
			   .append(" AND R.ITEM_CATEGORY = I.CATEGORY_CODE (+) ")
			   .append(" AND 'ITEM_CATEGORY' = I.CATEGORY_TYPE (+) ");
			
			if (StringUtils.hasText(brandCode))
			    sql.append(" and R.BRAND_CODE = '" + brandCode + "'");

			if (StringUtils.hasText(orderTypeCode))
			    sql.append(" and ORDER_TYPE_CODE = '" + orderTypeCode+ "'");
				
			if (StringUtils.hasText(startOrderNo))
			    sql.append(" and ORDER_NO >= '" + startOrderNo+ "'");

			if (StringUtils.hasText(endOrderNo))
			    sql.append(" and ORDER_NO <= '" + endOrderNo+ "'");

			if (StringUtils.hasText(startSourceOrderNo))
			    sql.append(" and SOURCE_ORDER_NO >= '" + startSourceOrderNo+ "'");

			if (StringUtils.hasText(endSourceOrderNo))
			    sql.append(" and SOURCE_ORDER_NO <= '" + endSourceOrderNo+ "'");

			if (StringUtils.hasText(status))
			    sql.append(" and STATUS = '" + status+ "'");

			if (StringUtils.hasText(declarationNo))
			    sql.append(" and DECLARATION_NO = '" + declarationNo+ "'");

			if (StringUtils.hasText(startDate))
			    sql.append(" and ORDER_DATE >= to_date('" + startDate+ "','yyyy/MM/dd')");

			if (StringUtils.hasText(endDate))
			    sql.append(" and ORDER_DATE <= to_date('" + endDate+ "','yyyy/MM/dd')");
			
			if (StringUtils.hasText(itemCategory))
			    sql.append(" and R.ITEM_CATEGORY = '" + itemCategory+ "'");
			
			if (StringUtils.hasText(supplierCode))
			    sql.append(" and R.SUPPLIER_CODE like '%" + supplierCode+ "%'");
			
			if (StringUtils.hasText(warehouseStatus))
			    sql.append(" and WAREHOUSE_STATUS = '" + warehouseStatus+ "'");
			
			if (StringUtils.hasText(expenseStatus))
			    sql.append(" and EXPENSE_STATUS = '" + expenseStatus+ "'");
			
			if (StringUtils.hasText(itemCode))
				sql.append(" AND L.ITEM_CODE = '" + itemCode+ "'");
		    
			if (StringUtils.hasText(startIDate))
			    sql.append(" and WAREHOUSE_IN_DATE >= to_date('" + startIDate+ "','yyyy/MM/dd')");

			if (StringUtils.hasText(endIDate))
			    sql.append(" and WAREHOUSE_IN_DATE <= to_date('" + endIDate+ "','yyyy/MM/dd')");
			
			if (StringUtils.hasText(startRDate))
			    sql.append(" and RECEIPT_DATE >= to_date('" + startRDate+ "','yyyy/MM/dd')");

			if (StringUtils.hasText(endRDate))
			    sql.append(" and RECEIPT_DATE <= to_date('" + endRDate+ "','yyyy/MM/dd')");
			
			if (StringUtils.hasText(startDateDiff))
				sql.append(" and RECEIPT_DATE - WAREHOUSE_IN_DATE  >= '" + startDateDiff+ "'");
		    
			if (StringUtils.hasText(endDateDiff))
				sql.append(" and RECEIPT_DATE - WAREHOUSE_IN_DATE  <= '" + endDateDiff+ "'");
			
			sql.append(" group by R.ORDER_TYPE_CODE, R.ORDER_NO, R.ITEM_CATEGORY, I.CATEGORY_NAME, ")
			   .append(" R.SUPPLIER_CODE, B.CHINESE_NAME, R.DECLARATION_NO, R.WAREHOUSE_IN_DATE, ")
			   .append(" R.RECEIPT_DATE, R.STATUS, R.WAREHOUSE_STATUS, R.EXPENSE_STATUS, R.SOURCE_ORDER_NO ");
			
			sql.append(" order by ORDER_NO desc ");
			
			log.info("sql = " + sql.toString());
			
			object = new Object[] { 
					"orderTypeCode", "orderNo", "receiveQuantity", "itemCategory", "categoryName", "supplierCode", 
					"supplierName", "declarationNo", "warehouseInDate", "receiptDate", "status", 
					"warehouseStatus", "expenseStatus", "sourceOrderNo"};
			
		}else{
			throw new Exception("查無正確匯出格式");
		}

		List lists = nativeQueryDAO.executeNativeSql(sql.toString());
		for (int i = 0; i < (lists.size() > 65535 ? 65535 : lists.size()); i++) {
			Object[] getObj = (Object[])lists.get(i);
			Object[] dataObject = new Object[object.length];
			for (int j = 0; j < (object.length); j++) {
				dataObject[j] = getObj[j];
			}
			rowData.add(dataObject);
		}
		
		return new SelectDataInfo(object, rowData);
	} catch (Exception e) {
		//e.printStackTrace();
		//log.error("匯出excel發生錯誤，原因：" + e.toString());
		throw new Exception("匯出excel發生錯誤，原因：" + e.getMessage());
	}
    }
    
    /**
	 * 進貨單簽核
	 * 
	 * @param headId
	 * @return
	 */
    public boolean isSignByManager(long headId){
    	long totalLocalPurchaseAmountCount = imReceiveHeadDAO.getTotalLocalPurchaseAmountCount(headId, 5000000);
    	log.info("是否大於5000000:"+totalLocalPurchaseAmountCount);
    	if(totalLocalPurchaseAmountCount > 0) return true;
    	else{
    		long unitPriceCount = imReceiveItemDAO.getUnitPriceCount(headId, 50000);
    		log.info("是否eeeee:"+unitPriceCount);
    		if(unitPriceCount > 0) return true;
    		
    	}
    	return false;
    }
    
	public void setImReceiveHeadDAO(ImReceiveHeadDAO imReceiveHeadDAO) {
		this.imReceiveHeadDAO = imReceiveHeadDAO;
	}
	
	public void setImReceiveItemDAO(ImReceiveItemDAO imReceiveItemDAO) {
		this.imReceiveItemDAO = imReceiveItemDAO;
	}
	
	public void setImMovementHeadDAO(ImMovementHeadDAO imMovementHeadDAO) {
		this.imMovementHeadDAO = imMovementHeadDAO;
	}
	
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setCmDeclarationHeadDAO(CmDeclarationHeadDAO cmDeclarationHeadDAO) {
		this.cmDeclarationHeadDAO = cmDeclarationHeadDAO;
	}
	
	public void setCmDeclarationItemDAO(CmDeclarationItemDAO cmDeclarationItemDAO) {
		this.cmDeclarationItemDAO = cmDeclarationItemDAO;
	}

	public void setFiInvoiceHeadDAO(FiInvoiceHeadDAO fiInvoiceHeadDAO) {
		this.fiInvoiceHeadDAO = fiInvoiceHeadDAO;
	}
	
	public void setFiInvoiceLineDAO(FiInvoiceLineDAO fiInvoiceLineDAO) {
		this.fiInvoiceLineDAO = fiInvoiceLineDAO;
	}
	
	public void setFiBudgetLineDAO(FiBudgetLineDAO fiBudgetLineDAO) {
		this.fiBudgetLineDAO = fiBudgetLineDAO;
	}

	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}

	public void setImReceiveItemMainService(ImReceiveItemMainService imReceiveItemMainService) {
		this.imReceiveItemMainService = imReceiveItemMainService;
	}
	
	public void setImReceiveExpenseMainService(ImReceiveExpenseMainService imReceiveExpenseMainService) {
		this.imReceiveExpenseMainService = imReceiveExpenseMainService;
	}

	public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
		this.imOnHandDAO = imOnHandDAO;
	}

	public void setPoPurchaseOrderHeadDAO(PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO) {
		this.poPurchaseOrderHeadDAO = poPurchaseOrderHeadDAO;
	}
	
	public void setPoPurchaseOrderLineDAO(PoPurchaseOrderLineDAO poPurchaseOrderLineDAO) {
		this.poPurchaseOrderLineDAO = poPurchaseOrderLineDAO;
	}
	
	public void setSiProgramLogDAO(SiProgramLogDAO siProgramLogDAO) {
	    this.siProgramLogDAO = siProgramLogDAO;
	}

	public void setPoVerificationSheetDAO(PoVerificationSheetDAO poVerificationSheetDAO) {
		this.poVerificationSheetDAO = poVerificationSheetDAO;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setImItemCurrentPriceViewDAO(ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
		this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
	}

	public void setImItemService(ImItemService imItemService) {
		this.imItemService = imItemService;
	}
	
	public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}

	public void setImTransationDAO(ImTransationDAO imTransationDAO) {
		this.imTransationDAO = imTransationDAO;
	}

	public void setBuEmployeeWithAddressViewDAO(BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
		this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
	}

	public void setImLetterOfCreditService(ImLetterOfCreditService imLetterOfCreditService) {
		this.imLetterOfCreditService = imLetterOfCreditService;
	}

	public void setFiInvoiceLineMainService(FiInvoiceLineMainService fiInvoiceLineMainService) {
		this.fiInvoiceLineMainService = fiInvoiceLineMainService;
	}
	
	public void setFiBudgetHeadService(FiBudgetHeadService fiBudgetHeadService) {
		this.fiBudgetHeadService = fiBudgetHeadService;
	}
	
	public void setFiBudgetLineService(FiBudgetLineService fiBudgetLineService) {
		this.fiBudgetLineService = fiBudgetLineService;
	}
	
	public void setBuSupplierWithAddressViewService(BuSupplierWithAddressViewService buSupplierWithAddressViewService) {
		this.buSupplierWithAddressViewService = buSupplierWithAddressViewService;
	}

	public void setPoPurchaseOrderHeadMainService(PoPurchaseOrderHeadMainService poPurchaseOrderHeadMainService) {
		this.poPurchaseOrderHeadMainService = poPurchaseOrderHeadMainService;
	}
	
	public void setPoPurchaseOrderLineMainService(PoPurchaseOrderLineMainService poPurchaseOrderLineMainService) {
		this.poPurchaseOrderLineMainService = poPurchaseOrderLineMainService;
	}
	
	public void setImLetterOfCreditHeadDAO(ImLetterOfCreditHeadDAO imLetterOfCreditHeadDAO) {
		this.imLetterOfCreditHeadDAO = imLetterOfCreditHeadDAO;
	}
	
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	    this.siProgramLogAction = siProgramLogAction;
	}
	
	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}

	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
	    this.imWarehouseDAO = imWarehouseDAO;
	}
	
	public void setBuBrandService(BuBrandService buBrandService) {
	    this.buBrandService = buBrandService;
	}
	
	public void setBuLocationService(BuLocationService buLocationService) {
	    this.buLocationService = buLocationService;
	}
	
	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
	    this.buBrandDAO = buBrandDAO;
	}
	
	public void setBuCommonPhraseLineDAO( BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
	
	public void setCmDeclarationOnHandDAO( CmDeclarationOnHandDAO cmDeclarationOnHandDAO) {
	    this.cmDeclarationOnHandDAO = cmDeclarationOnHandDAO;
	}    

	public void setImItemPriceDAO(ImItemPriceDAO imItemPriceDAO) {
		this.imItemPriceDAO = imItemPriceDAO;
	}

	//for 儲位用
    public void setImStorageAction(ImStorageAction imStorageAction) {
		this.imStorageAction = imStorageAction;
	}
    
	public void setImStorageService(ImStorageService imStorageService) {
		this.imStorageService = imStorageService;
	}
	
	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
		this.buEmployeeDAO = buEmployeeDAO;
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
	
	
	public void getMailList(ImReceiveHead head, String subject, String templateFileName, Map display, List mailAddress,
			List attachFiles, Map cidMap) throws Exception {

		// #1 取得寄件者與其他相關配置檔
		// #2 取得CC報表的URL
		// #3 用URL轉成PDF再轉jpg
		// #4 用URL轉成xls
		// #5 寄信
		try {
			String reportFileName = null;
			String brandCode = head.getBrandCode();
			String orderTypeCode = head.getOrderTypeCode();
			

			String subjectError = null;
			String description = null;
			StringBuffer emailError = new StringBuffer();

			String lineCode = "Receive";
			String subHeadCode = "Rve";
			
			log.info("babugetMailList:"+orderTypeCode+"   "+lineCode);
            log.info("取得的參數:"+orderTypeCode+"   "+lineCode); 
			// #1  取得配置檔得到 寄信的報表與
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("MailList"
					+ subHeadCode,lineCode); // +orderTypeCode itemCategory
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
				
				
              
				// 2.營業 從 BU_COMMON_PHRASE_LINE 取得
				// 修改為寄給多個單位，以分號分隔email地址 by Weichun 2011.09.15
				String[] attribute2 = buCommonPhraseLine.getAttribute2().split(";");
				for (int i = 0; i < attribute2.length; i++) {
					if (StringUtils.hasText(attribute2[i])) {
						//System.out.println("通知email ==> " + attribute2[i]);
						mailAddress.add(attribute2[i]);
						
					}
					System.out.println("mailAddress:"+mailAddress);
				}
				
				// 3.商品異動_副本 從 BU_COMMON_PHRASE_LINE 取得
				String attribute3 = buCommonPhraseLine.getAttribute3();
				if (StringUtils.hasText(attribute3)) {
					mailAddress.add(attribute3);
					
				}

				// 前半部主旨
				description = buCommonPhraseLine.getDescription();
			}

			description = buCommonPhraseLine.getDescription();
			subject = description.concat(head.getOrderTypeCode()).concat(head.getOrderNo());
			if (StringUtils.hasText(subjectError)) {
				subject = subjectError + subject;
			}
			
			String orderNo = head.getOrderNo();
			String fileName = "OvrLi".concat("-").concat(orderTypeCode).concat(orderNo);
			// 設定範本
			templateFileName = "CommonTemplate.ftl";
log.info("prompt0"+ brandCode+"   prompt1"+ orderTypeCode+"    prompt2"+  orderNo);
			// #2 取得cc連結
			Map returnMap = new HashMap(0);
			Map parameters = new HashMap(0);
			
			
			parameters.put("prompt0", head.getBrandCode());
			parameters.put("prompt1", orderTypeCode);
			parameters.put("prompt2", "");
			parameters.put("prompt3", "");
			parameters.put("prompt4", orderNo);
			parameters.put("prompt5", orderNo);
			parameters.put("prompt6", "");
			parameters.put("prompt7", "");
			
			//parameters.put("prompt5", head.getWarehouseInDate());
			
			String reportUrl = SystemConfig.getPureReportURL(brandCode, orderTypeCode, head.getLastUpdatedBy(),
					reportFileName, parameters);
			log.info("reportUrl:"+reportUrl);
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
	/*
	public void getMailList(ImReceiveHead head){
		log.info("進入寄信功能:"+head.getOrderTypeCode()+head.getOrderNo());
	}
	*/
	
	public List<Properties> updateCustomsStatus(Map parameterMap) throws Exception{    	
    	Map returnMap = new HashMap(0);
    	MessageBox msgBox = new MessageBox();
    	Map resultMap = new HashMap();
    	try{
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    
    	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
    	    
    	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);
    	    String id = (String)PropertyUtils.getProperty(otherBean, "headId");    	    
    	    Long headId = NumberUtils.getLong(id);
    	    
    	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
    	    String orderNo = (String)PropertyUtils.getProperty(otherBean, "orderNo");
    	    
    	    ImReceiveHead imReceiveHead = imReceiveHeadDAO.findById(headId);
    	    
    	    
    	    String tranStatus = (String)PropertyUtils.getProperty(otherBean, "tranStatus");
    	    if(tranStatus.equals("cancel")){
    	    	imReceiveHead.setTranAllowUpload("D");
    	    }else{
    	    	imReceiveHead.setTranAllowUpload("I");
    	    }
    	    imReceiveHead.setCustomsStatus("A");
    	    
    	    if(orderTypeCode.equals("EIF")||orderTypeCode.equals("EOF")){
    	    	imReceiveHead.setTranRecordStatus("NF14");  
    	    }else{
    	    	throw new Exception("上傳失敗，原因：");
    	    }
    	    imReceiveHeadDAO.save(imReceiveHead);
    	    
    	    msgBox.setMessage("已將單據送簽海關");
    	    
    	}catch(Exception ex){
    	    log.error("上傳海關失敗，原因：" + ex.toString());
    	    msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
    	    throw new Exception("上傳失敗，原因：" + ex.toString());

    	}
    	returnMap.put("vatMessage", msgBox);
    	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
	
	public List<Properties> getCustomsProcessResponse(Properties httpRequest){//by jason
		String customsStatus = httpRequest.getProperty("customsStatus");
		System.out.println("customsStatus:"+customsStatus);
		List list = new ArrayList();
		Properties properties = new Properties();
		try{
			String desc = customsProcessResponseDAO.getProcessResponseByCode(customsStatus);
			log.info("11ddd---"+desc);
			//for(CustomsProcessResponse cpr:cprs){
				properties.setProperty("response", desc);
			//}
		}catch(Exception e){
			e.printStackTrace();
			properties.setProperty("response", "fail!");
		}
		list.add(properties);
		log.info("1ddd--"+list);
		return list;
	}
	
	/*public void sendMail(ImReceiveHead head, String subject, String templateFileName, Map display,
			List mailAddress, List attachFiles, Map cidMap) throws Exception {

		try{
			System.out.println("SendMail start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
			//String reportFileName = "SO0752.rpt";
			//String brandCode = head.getBrandCode();
			String orderTypeCode = head.getOrderTypeCode();
			//String itemCategory = AjaxUtils.getPropertiesValue(head.getItemCategory(), "");
			//String supplierCode = AjaxUtils.getPropertiesValue(head.getSupplierCode(), "");
			String orderNo = head.getOrderNo();
			
			
			// 寄給商控and資訊部
			mailAddress.add("T00700@tasameng.com.tw"); // 資訊部
			mailAddress.add("cbr_0922@tasameng.com.tw"); // 資訊部
			//mailAddress.add("MM-DG@tasameng.com.tw"); // 商控
			
			//String fileName = itemCategory.concat("-").concat(supplierCode).concat(orderNo);
			//String fileName = itemCategory.concat("-").concat(orderNo);
			// 設定範本
			templateFileName = "CommonTemplate.ftl";
			
			// 設定主旨
			subject = "進貨單:" + orderTypeCode + "-" + orderNo + "已驗收";


			// 設定內容
			StringBuffer html = new StringBuffer();
			//System.out.println("emailError.toString() = " + emailError.toString());
			//if (StringUtils.hasText(emailError.toString())) {
				//html.append(emailError.toString());
			//}
			
			for(int i=0; i<png_files.size(); i++){
				html.append("<img src='cid:" + png_files.get(i).getName() + "'/><br>");
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>html = " + html.toString());
			// 設定樣板的內容值
			
			
			html.append("進貨單(總數大於500萬或單品項大於5萬)：").append(orderTypeCode).append(orderNo);
			
			html.append("<br/>").append("已於").append(DateUtils.formatTime(head.getLastUpdateDate())).append("驗收完成");
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
	}*/
	
	
	public void sendMail(ImReceiveHead head, String subject, String templateFileName, Map display,
			List mailAddress, List attachFiles, Map cidMap) throws Exception {

		log.info("getMailList");
		// #1 取得寄件者與其他相關配置檔
		// #2 取得CC報表的URL
		// #3 用URL轉成PDF再轉jpg
		// #4 用URL轉成xls
		// #5 寄信
		try {
			String reportFileName = null;
			String brandCode = head.getBrandCode();
			String orderTypeCode = head.getOrderTypeCode();
			String itemCategory = "Receive";

			String subjectError = null;
			String description = null;
			StringBuffer emailError = new StringBuffer();

			
            // #1  取得配置檔得到 寄信的報表與
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("MailList"
					+ "Rve",itemCategory); // +orderTypeCode itemCategory
			if (null == buCommonPhraseLine) {
				
			} else {
				reportFileName = buCommonPhraseLine.getAttribute1();
				// 1.該單起單人、採助、採購人、採購主管 從 BU_EMPLOYEE 取得
				// String domain = "@tasameng.com.tw";
				String createdBy = head.getCreatedBy();
				
				//促銷單不需此參數
				
				
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
			subject = description.concat(head.getOrderTypeCode().concat(head.getOrderNo()));
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
	 * 暫存 商控 送出 商控
	 *
	 * @param subject
	 * @param templateFileName
	 * @param display
	 * @param mailAddress
	 * @param attachFiles
	 * @param cidMap
	 */
	public void getAccessMailList(ImReceiveHead head, String subject, String templateFileName, Map display,
			List mailAddress, List attachFiles, Map cidMap,String action) throws Exception {
		log.info("getAccessMailList:"+action);
		try {

			String orderTypeCode = "EIF/EIP";
			//String itemCategory = AjaxUtils.getPropertiesValue(head.getItemCategory(), "");
			StringBuffer html = new StringBuffer();
			String subjectError = null;
			String description = null;
			StringBuffer emailError = new StringBuffer();

			// 取得發信種類名稱-權限
			String itemCategory = "Receive";
			/*
			ImItemCategory imItemCategory = imItemCategoryService.findById(brandCode, ImItemCategoryDAO.ITEM_CATEGORY,
					itemCategory);
			if (null == imItemCategory) {
				categoryName = "";
			} else {
				categoryName = imItemCategory.getCategoryName().concat("-");
			}
			 */
			// #1 取得配置檔得到 寄信的報表與

			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("MailListRve","Receive"); // +orderTypeCode

			// itemCategory
			if (null == buCommonPhraseLine) {
				// 寄給故障維謢人員
				mailAddress.add("Developer@tasameng.com.tw");
				subjectError = "MailList" + orderTypeCode + itemCategory + "無配置檔異常 ";
				description = "";
			} else {
				if(action.equals("SIGNING")){
					String accAdmin = buCommonPhraseLine.getAttribute4();
					if (StringUtils.hasText(accAdmin)) { 
						html.append("權限申請單：LOA" + head.getOrderNo() + "申請調整權限");
						BuEmployee buEmployee = (BuEmployee) buEmployeeDAO.findByPrimaryKey(BuEmployee.class, accAdmin);
						if (null != buEmployee) {
							String EMailCompany = buEmployee.getEMailCompany();
							if (null != EMailCompany) {
								if (!mailAddress.contains(EMailCompany)) // 判斷是否有重複的email
									mailAddress.add(EMailCompany);
								mailAddress.add("cbr_0922@tasameng.com.tw");
							} else {
								emailError.append("查審核人工號：").append(accAdmin).append(" 無信箱設定，請聯絡資訊部人員與通知此人權限調整通知<br>");
							}
						} else {
							emailError.append("查審核人工號：").append(accAdmin).append(" ，請聯絡資訊部人員與通知此人權限調整通知<br>");
						}
					}
				}else if(action.equals("SUBMIT")){
					html.append("權限申請單：IRQ" + head.getOrderNo() + "我們會盡快幫您處理");
					String createdBy = head.getCreatedBy();
					if (StringUtils.hasText(createdBy)) { // 起單人
						BuEmployee buEmployee = (BuEmployee) buEmployeeDAO.findByPrimaryKey(BuEmployee.class, createdBy);
						if (null != buEmployee) {
							String EMailCompany = buEmployee.getEMailCompany();
							if (null != EMailCompany) {
								if (!mailAddress.contains(EMailCompany)) // 判斷是否有重複的email
									mailAddress.add(EMailCompany);
								mailAddress.add("cbr_0922@tasameng.com.tw");
							} else {
								emailError.append("查起單人工號：").append(createdBy).append(" 無信箱設定，請聯絡資訊部人員與通知此人權限調整通知<br>");
							}
						} else {

							emailError.append("查起單人無工號：").append(createdBy).append(" ，請聯絡資訊部人員與通知此人權限調整通知<br>");


							emailError.append("查起單人工號：").append(createdBy).append(" 無信箱設定，請聯絡資訊部人員與通知此人權限調整通知<br>");

						}
					}
				}else{
				}
				// 前半部主旨
				description = buCommonPhraseLine.getDescription();
			}
			String orderNo = head.getOrderNo();
			subject = "KWE權限申請單:".concat(orderNo);
			if (StringUtils.hasText(subjectError)) {
				subject = subjectError + subject;
			}

			// 設定範本
			templateFileName = "CommonTemplate.ftl";
			System.out.println("emailError.toString() = " + emailError.toString());
			if (StringUtils.hasText(emailError.toString())) {
				mailAddress.add("cbr_0922@tasameng.com.tw");
				subjectError = "MailList" + orderTypeCode + itemCategory + "無配置檔異常 ";
				MailUtils.sendMail(subjectError, templateFileName, display, mailAddress, attachFiles, cidMap);
			}
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
    
}