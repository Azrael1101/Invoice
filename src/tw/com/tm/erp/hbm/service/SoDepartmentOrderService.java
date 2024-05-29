package tw.com.tm.erp.hbm.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Fidelity;
import javax.print.attribute.standard.PrinterResolution;

//import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.util.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.BuCustomerCouponRecord;
import tw.com.tm.erp.hbm.bean.BuCustomerId;
import tw.com.tm.erp.hbm.bean.BuCustomerWithAddressView;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuPosBtnConfig;
import tw.com.tm.erp.hbm.bean.BuPosUiConfigHead;
import tw.com.tm.erp.hbm.bean.BuPosUiConfigLine;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderSimpleView;
import tw.com.tm.erp.hbm.bean.TransferStatusInfo;
//import tw.com.tm.erp.hbm.bean.BuThirdPartyPayment;
import tw.com.tm.erp.hbm.bean.ImDistributionItem;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
//import tw.com.tm.erp.hbm.bean.ImItemColor;
import tw.com.tm.erp.hbm.bean.ImItemEanPriceView;
import tw.com.tm.erp.hbm.bean.ImItemOnHandView;
import tw.com.tm.erp.hbm.bean.ImItemPriceOnHandView;
import tw.com.tm.erp.hbm.bean.ImItemPriceView;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.SaleheadTable;
import tw.com.tm.erp.hbm.bean.Sequence;
import tw.com.tm.erp.hbm.bean.SoDepartmentOrderHead;
import tw.com.tm.erp.hbm.bean.SoDepartmentOrderItem;
import tw.com.tm.erp.hbm.bean.SoReceiptHead;
import tw.com.tm.erp.hbm.bean.SoReceiptLine;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderPayment;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCurrencyDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuPosBtnConfigDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
//import tw.com.tm.erp.hbm.dao.BuThirdPartyPaymentDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
//import tw.com.tm.erp.hbm.dao.ImItemColorDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerCouponRecordDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseEmployeeDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.SoDepartmentOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoPostingTallyDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderDetailViewDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderItemDAO;
import tw.com.tm.erp.standardie.print;
import tw.com.tm.erp.test.PrintInvoice;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;

import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
//import tw.com.tm.erp.utils.FileTools;
import tw.com.tm.erp.utils.NumberUtils;
//import tw.com.tm.erp.utils.ThirdPartyPaymentUtils;
import tw.com.tm.erp.utils.UserUtils;

/**
 * @author T15394
 *
 */
public class SoDepartmentOrderService {

/***************************************** LOG登記 *****************************************/	
    private static final Log log = LogFactory.getLog(SoDepartmentOrderService.class);
    public static final String IMG_PATH = "./images/departmentPos/";
    public static final String IMG_TYPE = ".png";
/***************************************** 明細傳遞格式 *****************************************/
    
    /*
    vat.item.make(vnB_Detail, "indexNo", 					{type:"IDX"  , view: "fixed" , desc:"序號"});
    vat.item.make(vnB_Detail, "lineId", 					{type:"TEXT" , view: "fixed" , mode:"READONLY" , desc:"流水號"});
	vat.item.make(vnB_Detail, "itemCode", 					{type:"TEXT" , size:15, view: "fixed", maxLen:20, desc:"品號",mode:"READONLY",eChange:"changeItemData(1)"});
	vat.item.make(vnB_Detail, "itemCName", 					{type:"TEXT" , size:18, view: "fixed", maxLen:20, desc:"品名", mode:"READONLY"});
	vat.item.make(vnB_Detail, "originalUnitPrice", 			{type:"NUMM", size: 6, view: "", maxLen:12, desc:"單價", dec:6, mode:"READONLY"});
	vat.item.make(vnB_Detail, "discountRate", 				{type:"NUMM", size: 2, view: "", maxLen: 6, desc:"折扣率", dec:2,mode:"READONLY", eChange:"changeItemData(2)"});
	vat.item.make(vnB_Detail, "quantity", 					{type:"NUMM", size: 4, view: "", maxLen: 8, desc:"數量", dec:2,mode:"READONLY", onchange:"changeItemData(3)"});
	vat.item.make(vnB_Detail, "originalSalesAmount", 		{type:"NUMM", size: 6, view: "shift", maxLen:20, desc:"金額", dec:6, mode:"READONLY"});
	vat.item.make(vnB_Detail, "actualSalesAmount", 			{type:"NUMM", size: 6, view: "", maxLen:20, desc:"折扣後金額", dec:6, mode:hideExport?"READONLY":""});
	vat.item.make(vnB_Detail, "discountAmount", 			{type:"NUMM", size: 1, view: "", maxLen: 8, desc:"折讓金額", dec:6,mode:"READONLY"});
	vat.item.make(vnB_Detail, "isLockRecord", 				{type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "isDeleteRecord", 			{type:"DEL", desc:"刪除",mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "depositCode", 				{type:"TEXT", desc:"備註"});*/
	
	public static final String[] GRID_FIELD_NAMES = {
		"indexNo",				"lineId",				"itemCode",				"itemCName",				"originalUnitPrice",
		"discountRate",			"quantity",				"originalSalesAmount",	"actualSalesAmount",		"discount",
		"isLockRecord",			"isDeleteRecord",		"reserve1"};
	public static final String[] GRID_FIELD_DEFAULT_VALUES = {
		"",				"",				"",				"",				"0",
		"100",		"0",			"0",			"0",			"0",
		"",				"",				""
	};
	public static final int[] GRID_FIELD_TYPES = {
		AjaxUtils.FIELD_TYPE_LONG,		AjaxUtils.FIELD_TYPE_LONG,		AjaxUtils.FIELD_TYPE_STRING,		AjaxUtils.FIELD_TYPE_STRING,		AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE,	AjaxUtils.FIELD_TYPE_DOUBLE,	AjaxUtils.FIELD_TYPE_DOUBLE,		AjaxUtils.FIELD_TYPE_DOUBLE,		AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_STRING,	AjaxUtils.FIELD_TYPE_STRING,	AjaxUtils.FIELD_TYPE_STRING
	};
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"id.brandCode", "id.itemCode", "itemCName", "id.warehouseCode",
		"warehouseName", "id.lotNo", "itemBrand", "itemBrandName",
		"unitPrice", "stockOnHandQty", "outUncommitQty", "inUncommitQty", 
		"moveUncommitQty", "otherUncommitQty", "currentOnHandQty", "category01", 
		"category01Name", "category02", "category02Name","category03",
		"category03Name", "category17", "supplierName", "supplierItemCode",
		"itemEName", "boxCapacity", "category07", "category09",
		"category13" 
	};
	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
		"", "", "", "", 
		"", "", "", "", 
		"", "", "", "",
		"", "", "", "", 
		"", "", "", "", 
		"", "", "", "", 
		"", "", "", "", 
		"" 
	};
	public static final String[] GRID_SEARCH_SALES_FIELD_NAMES = { 
		"customerPoNo", "salesOrderDate", "superintendentName", "customerCode",
		"itemCode", "itemCName", "originalUnitPrice", "discountRate",
		"actualUnitPrice", "id.orderNo", "id.orderTypeCode", "id.brandCode", "id.indexNo"
	};
	public static final String[] GRID_SEARCH_SALES_FIELD_DEFAULT_VALUES = { 
		"", "", "", "", 
		"", "", "", "", 
		"", "", "", "", 
		""
	};
	/*public static final String[] GRID_THIRD_PARTY_FIELD_NAMES = {
		"paymentCode",				"paymentName"};
	public static final String[] GRID_THIRD_PARTY_FIELD_DEFAULT_VALUES = {
		"",				""};*/
/***************************************** Spring IOC *****************************************/
	private BuCurrencyDAO buCurrencyDAO;
	 public void setBuCurrencyDAO(BuCurrencyDAO buCurrencyDAO) {
	    	this.buCurrencyDAO = buCurrencyDAO;
	    }
    private SoDepartmentOrderHeadDAO soDepartmentOrderHeadDAO;
    public void setSoDepartmentOrderHeadDAO(SoDepartmentOrderHeadDAO soDepartmentOrderHeadDAO) {
    	this.soDepartmentOrderHeadDAO = soDepartmentOrderHeadDAO;
    }
   
    private BuCommonPhraseService buCommonPhraseService;
    public void setBuCommonPhraseService(
    		BuCommonPhraseService buCommonPhraseService) {
    	this.buCommonPhraseService = buCommonPhraseService;
    }
    
    private BuAddressBookDAO buAddressBookDAO;
    public void setBuAddressBookDAO(
    		BuAddressBookDAO buAddressBookDAO) {
    	this.buAddressBookDAO = buAddressBookDAO;
    }
    
    private BuBasicDataService buBasicDataService;
    public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
    	this.buBasicDataService = buBasicDataService;
    }
    
    private BuBrandDAO buBrandDAO;
    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
    	this.buBrandDAO = buBrandDAO;
    }
    
    private ImItemService imItemService;
    public void setImItemService(ImItemService imItemService) {
    	this.imItemService = imItemService;
    }
    
    private ImItemPriceOnHandViewDAO imItemPriceOnHandViewDAO;
    public void setImItemPriceOnHandViewDAO(ImItemPriceOnHandViewDAO imItemPriceOnHandViewDAO) {
    	this.imItemPriceOnHandViewDAO = imItemPriceOnHandViewDAO;
    }
    
    private NativeQueryDAO nativeQueryDAO;
    public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
    	this.nativeQueryDAO = nativeQueryDAO;
    }
    
    private ImItemPriceViewDAO imItemPriceViewDAO;
    public void setImItemPriceViewDAO(ImItemPriceViewDAO imItemPriceViewDAO) {
    	this.imItemPriceViewDAO = imItemPriceViewDAO;
    }
    private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
    public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
    	this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
    }
    private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
    public void setBuEmployeeWithAddressViewDAO(BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
    	this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
    }
    private SoSalesOrderItemDAO soSalesOrderItemDAO;
    public void setSoSalesOrderItemDAO(SoSalesOrderItemDAO soSalesOrderItemDAO) {
    	this.soSalesOrderItemDAO = soSalesOrderItemDAO;
    }
    private ImItemDAO imItemDAO;
    public void setImItemDAO(ImItemDAO imItemDAO) {
    	this.imItemDAO = imItemDAO;
    }
    
    private BuPosBtnConfigDAO buPosBtnConfigDAO;    
    public void setBuPosBtnConfigDAO(BuPosBtnConfigDAO buPosBtnConfigDAO){
    	this.buPosBtnConfigDAO = buPosBtnConfigDAO;
    }
    private BuCustomerWithAddressViewDAO buCustomerWithAddressViewDAO;    
    public void setBuCustomerWithAddressViewDAO(BuCustomerWithAddressViewDAO buCustomerWithAddressViewDAO){
    	this.buCustomerWithAddressViewDAO = buCustomerWithAddressViewDAO;
    }
    
    private SoSalesOrderService soSalesOrderService;
    public void setSoSalesOrderService(SoSalesOrderService soSalesOrderService){
    	this.soSalesOrderService = soSalesOrderService;
    }

    private SoSalesOrderMainService soSalesOrderMainService;
    public void setSoSalesOrderMainService(SoSalesOrderMainService soSalesOrderMainService){
    	this.soSalesOrderMainService = soSalesOrderMainService;
    }
    private BuShopDAO buShopDAO;
    public void setBuShopDAO(BuShopDAO buShopDAO){
    	this.buShopDAO = buShopDAO;
    }
    private SoPostingTallyDAO soPostingTallyDAO;
    public void setSoPostingTallyDAO(SoPostingTallyDAO soPostingTallyDAO){
    	this.soPostingTallyDAO = soPostingTallyDAO;
    }
    private ImWarehouseDAO imWarehouseDAO;
    public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
		this.imWarehouseDAO = imWarehouseDAO;
	}
    private ImItemCategoryDAO imItemCategoryDAO;
    public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}
    private ImItemCategoryService imItemCategoryService;
    public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}
    private ImItemOnHandViewDAO imItemOnHandViewDAO;
	public void setImItemOnHandViewDAO(ImItemOnHandViewDAO imItemOnHandViewDAO) {
		this.imItemOnHandViewDAO = imItemOnHandViewDAO;
	}
    private BuShopMachineDAO buShopMachineDAO;
	public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
		this.buShopMachineDAO = buShopMachineDAO;
	}
	private ImWarehouseEmployeeDAO imWarehouseEmployeeDAO;
	public void setImWarehouseEmployeeDAO(ImWarehouseEmployeeDAO imWarehouseEmployeeDAO) {
		this.imWarehouseEmployeeDAO = imWarehouseEmployeeDAO;
	}
	private SoSalesOrderDetailViewDAO soSalesOrderDetailViewDAO;
	public void setSoSalesOrderDetailViewDAO(SoSalesOrderDetailViewDAO soSalesOrderDetailViewDAO) {
		this.soSalesOrderDetailViewDAO = soSalesOrderDetailViewDAO;
	}
	private ImMovementHeadDAO imMovementHeadDAO;
	public void setImMovementHeadDAO(ImMovementHeadDAO imMovementHeadDAO) {
		this.imMovementHeadDAO = imMovementHeadDAO;
	}
	private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
	public void setImItemCurrentPriceViewDAO(
			ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
		this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
	}
	private BuCustomerCouponRecordDAO buCustomerCouponRecordDAO;
	public void setBuCustomerCouponRecordDAO(
			BuCustomerCouponRecordDAO buCustomerCouponRecordDAO) {
		this.buCustomerCouponRecordDAO = buCustomerCouponRecordDAO;
	}
/*private ImItemColorDAO imItemColorDAO;
	public void setImItemColorDAO(ImItemColorDAO imItemColorDAO) {
		this.imItemColorDAO = imItemColorDAO;
	}
	private BuThirdPartyPaymentDAO buThirdPartyPaymentDAO;
	public void setBuThirdPartyPaymentDAO(
			BuThirdPartyPaymentDAO buThirdPartyPaymentDAO) {
		this.buThirdPartyPaymentDAO = buThirdPartyPaymentDAO;
	}*/
/***************************************** Service Function *****************************************/
/**主頁面初始化**/
    public Map executePosMainInitial(Map parameterMap) throws Exception {
    	Map resultMap = new HashMap(0);
    	Map multiList = new HashMap(0);
 
    	try {
    		Object otherBean = parameterMap.get("vatBeanOther");
    		Long formId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "formId"));
    		
    		SoDepartmentOrderHead head = this.findSoDepartmentOrderHead(formId, otherBean, resultMap);//主檔初始化
    		
    		resultMap.put("form", head);
    		resultMap.put("multiList", multiList);
    		
    		return resultMap;
    	} catch (Exception ex) {
    		log.error("POS初始化失敗，原因：" + ex.toString());
    		ex.printStackTrace();
    		throw new Exception("銷貨單初始化失敗，原因：" + ex.getMessage());
    	}
    }
    
    public Map executePromotionInitial(Map parameterMap) throws Exception {
    	Map resultMap = new HashMap(0);
    	try {
    		Object otherBean = parameterMap.get("vatBeanOther");
    		String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
    		
    		return resultMap;
    	} catch (Exception ex) {
    		log.error("promotion初始化失敗，原因：" + ex.toString());
    		ex.printStackTrace();
    		throw new Exception("promotion初始化失敗，原因：" + ex.getMessage());
    	}
    }
    
    // 根據str,font的樣式以及輸出檔案目錄
    public static void createImage(String str, Font font, File outFile,Integer width, Integer height) throws Exception {
	    // 建立圖片
	    BufferedImage image = new BufferedImage(width, height,
	    BufferedImage.TYPE_INT_BGR);
	    Graphics g = image.getGraphics();
	    g.setClip(0, 0, width, height);
	    g.setColor(Color.black);
	    g.fillRect(0, 0, width, height);// 先用黑色填充整張圖片,也就是背景
	    g.setColor(Color.red);// 在換成黑色
	    g.setFont(font);// 設定畫筆字型
	    /** 用於獲得垂直居中y */
	    Rectangle clip = g.getClipBounds();
	    FontMetrics fm = g.getFontMetrics(font);
	    int ascent = fm.getAscent();
	    int descent = fm.getDescent();
	    int y = (clip.height - (ascent + descent)) / 2 + ascent;
	    for (int i = 0; i < 6; i++) {// 256 340 0 680
	    g.drawString(str, i * 680, y);// 畫出字串
	    }
	    g.dispose();
	    ImageIO.write(image, "png", outFile);// 輸出png圖片
    }
    
    /**登入頁面初始化**/
    public Map executeLoginInitial(Map parameterMap) throws Exception {
    	Map resultMap = new HashMap(0);
    	Map multiList = new HashMap(0);
    	try {
    		Object otherBean = parameterMap.get("vatBeanOther");
    		String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
    		
    		List<BuPosUiConfigLine> allBuPosUiConfig = buCommonPhraseService.getBuPosUiConfigByHead();

    		//Map formMap = new HashMap(0);
    		//for(BuPosUiConfigLine conf:allBuPosUiConfig) {
    			//formMap.put(conf.get, tran.getTransferStatus());
    		//}
    		  		
    		resultMap.put("form", allBuPosUiConfig);
    		return resultMap;
    	} catch (Exception ex) {
    		log.error("POS初始化失敗，原因：" + ex.toString());
    		throw new Exception("銷貨單初始化失敗，原因：" + ex.getMessage());
    	}
    }
/**取得銷售單主檔**/
    public SoDepartmentOrderHead findSoDepartmentOrderHead(long formId,Object otherBean, Map resultMap) throws FormException, Exception {
		SoDepartmentOrderHead form = formId > 0 ? findById(formId): executeNew(otherBean);
		if (form != null) {
			//BuBrand buBrand = buBrandDAO.findById(form.getBrandCode());
			resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			//resultMap.put("brandName", buBrand.getBrandName());
			//resultMap.put("superintendentName", UserUtils.getUsernameByEmployeeCode(form.getSuperintendentCode()));
			//resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
			return form;
		} else {
			throw new FormException("查無配貨單單主鍵：" + formId + "的資料！");
		}
	}
    
    public SaleheadTable findSaleheadTable(long formId,Object otherBean, Map resultMap) throws FormException, Exception {
    	SaleheadTable form = formId > 0 ? findSaleheadById(formId): executeNewSaleheadTable(otherBean);
		if (form != null) {
//			BuBrand buBrand = buBrandDAO.findById(form.getBrandCode());
//			resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
//			resultMap.put("brandName", buBrand.getBrandName());
//			resultMap.put("superintendentName", UserUtils.getUsernameByEmployeeCode(form.getSuperintendentCode()));
//			resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
			return form;
		} else {
			throw new FormException("查無配貨單單主鍵：" + formId + "的資料！");
		}
	}
    
    //getAJAXEmployeeName
    public List<Properties> getAJAXEmployeeName(Properties httpRequest) throws ValidationErrorException {
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		String itemIndexNo = null;
		try {
			/**前端資料**/
			String brandCode = httpRequest.getProperty("brandCode");
			String codeType = httpRequest.getProperty("codeType");
			String employeeCode = httpRequest.getProperty("employeeCode");
			String superintendentCode = httpRequest.getProperty("superintendentCode");
			
			//String employeeCName = "查無此工號";
			/**主檔查詢**/
			BuEmployee employee = (BuEmployee) soDepartmentOrderHeadDAO.findByPrimaryKey(BuEmployee.class, employeeCode);
			if(null != employee) {
				BuAddressBook addressBook = (BuAddressBook) soDepartmentOrderHeadDAO.findByPrimaryKey(BuAddressBook.class, employee.getAddressBookId());
				if(null != addressBook) {
					if(codeType.equals("employee") && (null != employeeCode && !employeeCode.equals(""))) {
						properties.setProperty("employeeCName", AjaxUtils.getPropertiesValue(addressBook.getChineseName(), ""));
						properties.setProperty("employeeEName", AjaxUtils.getPropertiesValue(addressBook.getEnglishName(), ""));
					}else {
						System.out.println("codeType == null");
						properties.setProperty("employeeCName", "");
					}
				}else {
					System.out.println("addressBook == null");
					properties.setProperty("employeeCName", "");
				}
			}else {
				System.out.println("employee == null");
				properties.setProperty("employeeCName", "");
			}
			
			
			/**主檔查詢**/
			BuEmployee superintendent = (BuEmployee) soDepartmentOrderHeadDAO.findByPrimaryKey(BuEmployee.class, superintendentCode);
			if(null != superintendent) {
				BuAddressBook superintendentAddressBook = (BuAddressBook) soDepartmentOrderHeadDAO.findByPrimaryKey(BuAddressBook.class, superintendent.getAddressBookId());
				/**回傳資料**/
				
				if(null != superintendentAddressBook) {
					if(codeType.equals("superintendent")&& (null != superintendentCode && !superintendentCode.equals(""))) {
						properties.setProperty("superintendentCName", AjaxUtils.getPropertiesValue(superintendentAddressBook.getChineseName(), ""));
						properties.setProperty("superintendentEName", AjaxUtils.getPropertiesValue(superintendentAddressBook.getEnglishName(), ""));
					}else {
						properties.setProperty("superintendentCName", "");
					}
				}else {
					properties.setProperty("superintendentCName", "");
				}
			}else {
				properties.setProperty("superintendentCName", "");
			}
			
			
			result.add(properties);
			return result;
		} catch (Exception ex) {
			log.error("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料發生錯誤，原因：" + ex.getMessage());
			ex.printStackTrace();
			throw new ValidationErrorException("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料失敗！");
		}
    } 
    
/**變更商品資訊**/
    public List<Properties> getAJAXItemInfoData(Properties httpRequest) throws ValidationErrorException {
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		String itemIndexNo = null;
		try {
			/**前端資料**/
			String brandCode = httpRequest.getProperty("brandCode");
			String itemCode = httpRequest.getProperty("itemCode");
			String shopCode = httpRequest.getProperty("shopCode");
			//Long nItemLine = NumberUtils.getLong((String)httpRequest.getProperty("nItemLine"));
			Double totaleActualSalesAmount = NumberUtils.getDouble((String)httpRequest.getProperty("totaleActualSalesAmount"));
			String unitPrice = "0";
			String actualSalesAmount = "0";
			String quantity = "1";
			Long discountRate = 100L;
			String itemCName = "查無此商品";
			/**主檔查詢**/
			ImItem imItem = imItemService.findItem(brandCode, itemCode);
			//ImItemPriceView imItem = imItemPriceViewDAO.findOneItemPriceView(brandCode, "1", itemCode,"Y","Y",DateUtils.format(new Date(), DateUtils.C_DATA_PATTON_YYYYMMDD));
			if(imItem!=null){
				unitPrice = String.valueOf(65000.0000D);//(imItem.getUnitPrice()).toString();
				itemCName = imItem.getItemCName();
				log.info("刷入商品:"+imItem.getItemCode());
				log.info("原價:"+unitPrice);
				/**其他查詢用Map**/
				HashMap conditionMap = new HashMap();
				conditionMap.put("brandCode", brandCode);
				conditionMap.put("itemCode", itemCode);
				conditionMap.put("saleDate", DateUtils.format(new Date(),DateUtils.C_DATA_PATTON_YYYYMMDD));
				conditionMap.put("shopCode", shopCode);
					actualSalesAmount = unitPrice;
			}
			/**最後總金額**/
			totaleActualSalesAmount = totaleActualSalesAmount+Math.round(Double.parseDouble(actualSalesAmount));
			/**回傳資料**/
			properties.setProperty("itemCode", AjaxUtils.getPropertiesValue(itemCode, ""));
			properties.setProperty("itemCName", AjaxUtils.getPropertiesValue(itemCName, ""));
			properties.setProperty("quantity", AjaxUtils.getPropertiesValue(quantity, "0"));
			properties.setProperty("originalUnitPrice", AjaxUtils.getPropertiesValue(unitPrice, "0"));
			properties.setProperty("actualSalesAmount", AjaxUtils.getPropertiesValue(actualSalesAmount, "0"));
			properties.setProperty("discountRate", AjaxUtils.getPropertiesValue(discountRate, "0"));
			properties.setProperty("totaleActualSalesAmount", AjaxUtils.getPropertiesValue(Math.round(totaleActualSalesAmount), "0"));
			result.add(properties);
			return result;
		} catch (Exception ex) {
			log.error("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料發生錯誤，原因：" + ex.getMessage());
			ex.printStackTrace();
			throw new ValidationErrorException("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料失敗！");
		}
    }    
    
	
	public List findItemPrice(HashMap conditionMap, String isAllShop) {
		
		String brandCode = (String)conditionMap.get("brandCode");
		String item_code = (String)conditionMap.get("itemCode");
		String saleDate  = (String)conditionMap.get("saleDate");
		String shopCode  = (String)conditionMap.get("shopCode");
		StringBuffer sqlBody = new StringBuffer();
		
		if("Y".equals(isAllShop)){
			sqlBody.append(" SELECT h.PROMOTION_CODE,h.PROMOTION_NAME,i.DISCOUNT_TYPE,i.ITEM_CODE,item.UNIT_PRICE,i.DISCOUNT_AMOUNT,i.DISCOUNT,i.DISCOUNT_PERCENTAGE, CASE WHEN i.DISCOUNT_TYPE = '1' THEN ROUND(i.DISCOUNT_AMOUNT,2) ELSE ROUND((item.UNIT_PRICE*i.DISCOUNT_PERCENTAGE*0.01),2) END PROMOTION_PRICE ")
			.append(" FROM IM_PROMOTION h,IM_PROMOTION_ITEM i ,IM_ITEM_PRICE_VIEW item ")
			.append(" WHERE  1=1 AND h.head_id = i.head_id AND item.brand_code = h.brand_code AND item.item_code = i.item_code AND h.status ='FINISH' AND item.price_enable = 'Y'")
			.append(" AND h.brand_code = '"+brandCode+"' ")
			.append(" AND i.item_code  = '"+item_code+"' ")
			.append(" AND TO_DATE("+saleDate+",'yyyymmdd') BETWEEN H.BEGIN_DATE AND H.END_DATE ")
			.append(" ORDER BY H.BEGIN_DATE DESC,ITEM.BEGIN_DATE DESC");
		}else{
			sqlBody.append("select p.brand_code, p.promotion_code, s.shop_code, s.begin_date, price.unit_price, s.end_date, i.item_code, i.discount_type, ") ;
			sqlBody.append(" CASE WHEN i.DISCOUNT_TYPE = '1' THEN ROUND(i.DISCOUNT_AMOUNT,2) ELSE ROUND((price.UNIT_PRICE*i.DISCOUNT_PERCENTAGE*0.01),2) END PROMOTION_PRICE, ") ;
			sqlBody.append(" i.discount, to_char(p.last_update_date,'YYYY-MM-DD hh24:mi:ss') as last_update_date");
			sqlBody.append(" from im_promotion p, im_promotion_shop s, im_promotion_item i,");
			sqlBody.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
			sqlBody.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
			sqlBody.append(" and type_code = '1' and begin_date <= TO_DATE('"+saleDate+"','YYYYMMDD')");
			sqlBody.append(" group by item_code, type_code) actual_price");
			sqlBody.append(" where item.item_code = price.item_code");
			sqlBody.append(" and price.item_code = actual_price.item_code");
			sqlBody.append(" and price.type_code = actual_price.type_code");
			sqlBody.append(" and price.begin_date = actual_price.begin_date");
			sqlBody.append(" and item.brand_code = '"+brandCode+"'");
			sqlBody.append(" and item.enable = 'Y') price");			    
			sqlBody.append(" where p.head_Id = s.head_id and p.head_Id = i.head_id and i.item_code = price.item_code");
		    sqlBody.append(" and p.brand_code = '"+brandCode+"' and p.price_type = '1' and p.is_all_shop != 'Y' and p.status = 'FINISH'");
		    sqlBody.append(" and TO_DATE('"+saleDate+"','YYYYMMDD') BETWEEN s.begin_date AND s.end_date ");
		    sqlBody.append(" and s.shop_code = '"+shopCode+"'");
		    sqlBody.append(" and i.item_code = '"+item_code+"'");
		    sqlBody.append(" order by i.item_code");
		    
		}
		log.info(sqlBody.toString());
		List lists = nativeQueryDAO.executeNativeSql(sqlBody.toString());
		return lists;
	}	  
    
    public List<Properties> checkItem(Properties httpRequest)throws ValidationErrorException{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	
    	try{
    		String brandCode = httpRequest.getProperty("brandCode");
			String itemCode = httpRequest.getProperty("itemCode");
			System.out.println("checkItem,itemCode::"+itemCode);
			//System.out.println(imItemPO.getAccountCode());
			
			ImItem imItemPO = imItemDAO.findItem("T2", itemCode); //.findImItem("T2",itemCode,"Y");
			String rtnCode = "";
			if(null!=imItemPO) {
				System.out.println(imItemPO.getItemCName());
				System.out.println(imItemPO.getStandardPurchaseCost());
				rtnCode = imItemPO.getItemCode();
			}
			properties.setProperty("imItemPO", rtnCode);
			result.add(properties);
			
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return result;
    } 
    
    
    
    
    
    
/**POS單查詢**/
    public SoDepartmentOrderHead findById(Long headId) throws Exception {
		try {
			SoDepartmentOrderHead salesOrder = (SoDepartmentOrderHead) soDepartmentOrderHeadDAO.findByPrimaryKey(SoDepartmentOrderHead.class, headId);
			return salesOrder;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢百貨銷售單主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢百貨銷售單主檔時發生錯誤，原因："+ ex.getMessage());
		}
	}
    
    
    public SoSalesOrderHead findT2ById(Long headId) throws Exception {
		try {
			SoSalesOrderHead salesOrder = (SoSalesOrderHead) soDepartmentOrderHeadDAO.findByPrimaryKey(SoSalesOrderHead.class, headId);
			return salesOrder;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢百貨銷售單主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢百貨銷售單主檔時發生錯誤，原因："+ ex.getMessage());
		}
	}
    
    public SaleheadTable findSaleheadById(Long headId) throws Exception {
		try {
			SaleheadTable salesOrder = (SaleheadTable) soDepartmentOrderHeadDAO.findByPrimaryKey(SaleheadTable.class, headId);
			return salesOrder;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢百貨銷售單主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢百貨銷售單主檔時發生錯誤，原因："+ ex.getMessage());
		}
	}
 /**建立初始化POS單**/
    public SoDepartmentOrderHead executeNew(Object otherBean) throws Exception {
    	SoDepartmentOrderHead form = new SoDepartmentOrderHead();
    	try {
//載入前端資訊
    		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
    		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    		//String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
    		String superintendentCode = (String) PropertyUtils.getProperty(otherBean, "superintendentCode");
    		//String localCurrencyCode = buCommonPhraseService.getBuCommonPhraseLineName("SystemConfig", "LocalCurrency");
//帶入預設值
    		//List<BuShop> shopForEmployee = buBasicDataService.getShopForEmployee(loginBrandCode, loginEmployeeCode, "Y");
    		//if (shopForEmployee == null || shopForEmployee.size() == 0)
    		//	throw new Exception("查無使用者可使用之店櫃");
    		String defaultShop = "99";
   			//BuShop firstShop = (BuShop) shopForEmployee.get(0);
   			//defaultShop = firstShop.getShopCode();
    		
    		Sequence seq =  soDepartmentOrderHeadDAO.findSeqByName("soDepartmentOrderHead");
    		Long nextVal = seq.getCurrent_value() + seq.getIncrement();
    		seq.setCurrent_value(nextVal);
    		soDepartmentOrderHeadDAO.save(seq);
    		
    		SoReceiptHead receiptHead = soDepartmentOrderHeadDAO.findReceiptHeadByYearMonth("T1GS", "110", "05-06", "25");
    		SoReceiptLine receiptLine = soDepartmentOrderHeadDAO.findReceiptLineByh(receiptHead.getHeadId());
    		String invoicePrefix = receiptLine.getReceiptPrefix();
    		Long nextInv = Long.valueOf(receiptLine.getReceiptSerialNumberNextVal()) ;
    		Long nextNextInv = (nextInv+1);
    		System.out.println("nextNextInv:"+nextNextInv);
    		
    		receiptLine.setReceiptSerialNumberNextVal(String.valueOf(nextNextInv+1));
    		soDepartmentOrderHeadDAO.save(receiptLine);
    		
    		form.setHeadId(nextVal);
    		String invNo = invoicePrefix + String.valueOf(nextInv++);
    		System.out.println("invNo:"+invNo);
    		form.setGuiCode(invNo);
    		form.setShopCode(defaultShop);
    		form.setBrandCode(loginBrandCode);
    		form.setSalesOrderDate(DateUtils.getCurrentDate());
    		form.setSuperintendentCode(superintendentCode);
    		form.setStatus(OrderStatus.SAVE);
    		form.setCreatedBy(loginEmployeeCode);
    		form.setLastUpdatedBy(loginEmployeeCode);
    		//存檔回傳   		
    		this.save(form);
    	} catch (Exception e) {
    		e.printStackTrace();
    		log.error("建立銷售單失敗,原因:" + e.toString());
    		throw new Exception("建立銷售單失敗,原因:" + e.getMessage());
    	}
    	return form;
    }
    
    //二維碼顏色
  	private static final int BLACK = 0xFF000000;
  	//二維碼顏色
  	private static final int WHITE = 0xFFFFFFFF;
  	
  	/**
  	* ZXing 方式生成二維碼
  	*
  	* @param text       內容
  	* @param width      二維碼寬
  	* @param height     二維碼高
  	* @param outPutPath 二維碼生成儲存路徑
  	* @param imageType  二維碼生成格式
  	*/
  	public static void zxingCodeCreate(String text, int width, int height, String outPutPath, String imageType) {
  		Map<EncodeHintType, String> his = new HashMap<EncodeHintType, String>();
  		//設定編碼字符集
  		his.put(EncodeHintType.CHARACTER_SET, "utf-8");
  		try {
  			//生成二維碼
  			BitMatrix encode = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, his);
  			//二維碼寬高
  			int codeWidth = encode.getWidth();
  			int codeHeight = encode.getHeight();
  			//將二維碼放入緩衝流
  			BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
  			for (int i = 0; i < codeWidth; i++  ) {
  				for (int j = 0; j < codeHeight; j++  ) {
  					//迴圈將二維碼內容定入圖片
  					image.setRGB(i, j, encode.get(i, j) ? BLACK : WHITE);
  				}
  			}
  			File outPutImage = new File(outPutPath);
  			//如果圖片不存在建立圖片
  			if (!outPutImage.exists())
  			outPutImage.getParentFile().mkdir();
  			//將二維碼寫入圖片
  			ImageIO.write(image, imageType, outPutImage);
  			
  			System.out.println("二維碼生成完成");
  			
  		} catch (WriterException e) {
  			e.printStackTrace();
  			System.out.println("二維碼生成失敗");
  		} catch (IOException e) {
  			e.printStackTrace();
  			System.out.println("生成二維碼圖片失敗");
  		}
  	}
    
    public SaleheadTable executeNewSaleheadTable(Object otherBean) throws Exception {
    	SaleheadTable form = new SaleheadTable();
    	try {
    		form.setShopCode("F9900");
    		form.setSalesOrderDate(new Date());
    		//form.setStatus(1);
    		form.setHeadSno(101L);
    		form.setPosMachineCode("99");
    		form.setTransactionSno(999L);
    		form.setTotalActualSalesAmt(10.00D);
//存檔回傳   		
    		soDepartmentOrderHeadDAO.save(form);
    	} catch (Exception e) {
    		log.error("建立銷售單失敗,原因:" + e.toString());
    		throw new Exception("建立銷售單失敗,原因:" + e.getMessage());
    	}
    	return form;
    }
/**POS單存檔**/
    public String save(SoDepartmentOrderHead saveObj) throws FormException,
    Exception {
    	soDepartmentOrderHeadDAO.save(saveObj);
    	return MessageStatus.SUCCESS;
    }
/**POS單更新**/
    public SoSalesOrderHead updateSalesOrder(Map parameterMap) throws FormException, Exception {
	    log.info("updateSalesOrder");
		try {
		    Object formBindBean = parameterMap.get("vatBeanFormBind");
		    //Object formLinkBean = parameterMap.get("vatBeanFormLink");
		    Object otherBean = parameterMap.get("vatBeanOther");
		    Long headId = NumberUtils.getLong((String) PropertyUtils.getProperty(formBindBean, "headId"));
		    String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		    // 取得欲更新的bean
		    SoSalesOrderHead soSalesOrderHead = this.findT2ById(headId);
		    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, soSalesOrderHead);
		    modifySoSalesOrder(soSalesOrderHead, loginEmployeeCode);
		    return soSalesOrderHead;
		} catch (FormException fe) {
		    log.error("銷貨單存檔失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
		    throw new Exception("銷貨單存檔時發生錯誤，原因：" + ex.getMessage());
		}
    }
    
    /**Promotion更新**/
    public SoSalesOrderHead updatePromotion(Map parameterMap) throws FormException, Exception {
	    log.info("updatePromotion");
		try {
		    Object formBindBean = parameterMap.get("vatBeanFormBind");
		    
		    Object otherBean = parameterMap.get("vatBeanOther");
		    Long headId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "headId"));
		    String promotionCode = (String) PropertyUtils.getProperty(otherBean, "promotionCode");
		    // 取得欲更新的bean
		    SoSalesOrderHead SoSalesOrderHead = this.findT2ById(headId);
		    SoSalesOrderHead.setPromotionCode(promotionCode);
		    
		    soDepartmentOrderHeadDAO.save(SoSalesOrderHead);
		    
		    return SoSalesOrderHead;
		} catch (FormException fe) {
		    log.error("銷貨單存檔失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
		    throw new Exception("銷貨單存檔時發生錯誤，原因：" + ex.getMessage());
		}
    }
    
    
/**轉入SoSalesOrderHead**/
    public SoSalesOrderHead copyToRealSalesHead(SoDepartmentOrderHead soDepartmentOrderHead) throws Exception {
	    log.info("updateRealSalesHead");
		try {
			
			Date date = new Date();
			//BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(soDepartmentOrderHead.getBrandCode(), soDepartmentOrderHead.getSuperintendentCode());
			
			
			
			SoSalesOrderHead soSalesOrderHead = new SoSalesOrderHead();
			//soSalesOrderHead.setOrderDiscountType("TEST123");
			

			soSalesOrderHead.setBrandCode(soDepartmentOrderHead.getBrandCode());
			soSalesOrderHead.setOrderTypeCode("SOP");
//			soSalesOrderHead.setVipTypeCode(vipTypeCode);
			soSalesOrderHead.setSalesOrderDate(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH,DateUtils.format(date,DateUtils.C_DATE_PATTON_SLASH)));
			soSalesOrderHead.setCustomerCode(soDepartmentOrderHead.getCustomerCode());
			soSalesOrderHead.setCustomerPoNo(soDepartmentOrderHead.getHeadId().toString());
			soSalesOrderHead.setPaymentTermCode("Z9");
			soSalesOrderHead.setSchedule("99");
			soSalesOrderHead.setCountryCode("TW");
			soSalesOrderHead.setCurrencyCode("NTD");
			soSalesOrderHead.setShopCode(soDepartmentOrderHead.getShopCode());
			soSalesOrderHead.setDefaultWarehouseCode(soDepartmentOrderHead.getShopCode());
			soSalesOrderHead.setSuperintendentCode(soDepartmentOrderHead.getSuperintendentCode());
			soSalesOrderHead.setSuperintendentName(soSalesOrderHead.getSuperintendentCode());
			soSalesOrderHead.setInvoiceTypeCode("2");
			soSalesOrderHead.setTaxType("3");
			soSalesOrderHead.setTaxRate(5D);
			soSalesOrderHead.setScheduleCollectionDate(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH,DateUtils.format(date,DateUtils.C_DATE_PATTON_SLASH)));
			soSalesOrderHead.setScheduleShipDate(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH,DateUtils.format(date,DateUtils.C_DATE_PATTON_SLASH)));
			soSalesOrderHead.setDiscountRate(100D);
			//soSalesOrderHead.setTransactionSeqNo(transactionSeqNo);
			soSalesOrderHead.setSufficientQuantityDelivery("Y");
			soSalesOrderHead.setStatus("SIGNING");
			soSalesOrderHead.setCreatedBy(soDepartmentOrderHead.getSuperintendentCode());
			soSalesOrderHead.setCreationDate(date);
			soSalesOrderHead.setLastUpdatedBy(soDepartmentOrderHead.getSuperintendentCode());
			soSalesOrderHead.setLastUpdateDate(date);
			soSalesOrderHead.setSalesType(soDepartmentOrderHead.getSalesType());

			//soSalesOrderHeadDAO.save(soSalesOrderHead);
			//明細更新//////////////////////////////////////////////////////////
			soSalesOrderHead = this.copyRealSalesLine(soSalesOrderHead,soDepartmentOrderHead);

			return soSalesOrderHead;
			
			
			
			//soSalesOrderHeadDAO.merge(soSalesOrderHead);
			
		} catch (Exception ex) {
		    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
		    ex.printStackTrace();
		    throw new Exception("銷貨單存檔時發生錯誤，原因：" + ex.getMessage());
		}
    }
/**轉入SoSalesOrderLine**/
    public SoSalesOrderHead copyRealSalesLine(SoSalesOrderHead soSalesOrderHead,SoDepartmentOrderHead soDepartmentOrderHead) throws Exception {
	    log.info("updateRealSalesLine");
		try {
			Date date = new Date();
			Double totalOriginalSalesAmount = 0D;
			Double totalActualSalesAmount = 0D;
			Double taxAmount = 0D;
			List<SoDepartmentOrderItem> soDepartmentOrderItems = soDepartmentOrderHead.getSoDepartmentOrderItem();
			List<SoSalesOrderItem> soSalesOrderItems = soSalesOrderHead.getSoSalesOrderItems();
			for(SoDepartmentOrderItem soDepartmentOrderItem:soDepartmentOrderItems){
				ImItem imItem = imItemDAO.findById(soDepartmentOrderItem.getItemCode());
				String isServiceItem = imItem.getIsServiceItem();
				String isComposeItem = imItem.getIsComposeItem();
				SoSalesOrderItem soSalesOrderItem = new SoSalesOrderItem();
				soSalesOrderItem.setLineId(soDepartmentOrderItem.getLineId());
				soSalesOrderItem.setSoSalesOrderHead(soSalesOrderHead);
				soSalesOrderItem.setWarehouseCode(soSalesOrderHead.getDefaultWarehouseCode());
				soSalesOrderItem.setItemCode(soDepartmentOrderItem.getItemCode());
				soSalesOrderItem.setOriginalUnitPrice(soDepartmentOrderItem.getOriginalUnitPrice());
				soSalesOrderItem.setQuantity(soDepartmentOrderItem.getQuantity());
				soSalesOrderItem.setOriginalSalesAmount(soDepartmentOrderItem.getOriginalUnitPrice()*soDepartmentOrderItem.getQuantity());
				soSalesOrderItem.setDiscountRate(soDepartmentOrderItem.getDiscountRate());
				soSalesOrderItem.setActualUnitPrice(Math.abs(soDepartmentOrderItem.getActualSalesAmount()/soDepartmentOrderItem.getQuantity()));
				soSalesOrderItem.setActualSalesAmount(soDepartmentOrderItem.getActualSalesAmount());
				soSalesOrderItem.setScheduleShipDate(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH,DateUtils.format(date,DateUtils.C_DATE_PATTON_SLASH)));
				soSalesOrderItem.setShippedDate(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH,DateUtils.format(date,DateUtils.C_DATE_PATTON_SLASH)));
				soSalesOrderItem.setReserve5("POS");
				soSalesOrderItem.setIsTax("P");
				soSalesOrderItem.setTaxType("3");
				soSalesOrderItem.setTaxRate(5D);
				//soSalesOrderItem.setTaxAmount(soDepartmentOrderItem.getActualSalesAmount() * 0.05);
				soSalesOrderItem.setTaxAmount(soSalesOrderService.calculateTaxAmount(soSalesOrderItem.getTaxType(), soSalesOrderItem.getTaxRate(), soSalesOrderItem.getActualSalesAmount()));
				soSalesOrderItem.setCreatedBy(soDepartmentOrderHead.getSuperintendentCode());
				soSalesOrderItem.setCreationDate(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH,DateUtils.format(date,DateUtils.C_DATE_PATTON_SLASH)));
				soSalesOrderItem.setLastUpdateDate(date);
				soSalesOrderItem.setLastUpdatedBy(soDepartmentOrderHead.getSuperintendentCode());
				soSalesOrderItem.setDepositCode(soDepartmentOrderItem.getReserve1());
				soSalesOrderItem.setIsServiceItem(isServiceItem);
				soSalesOrderItem.setIsComposeItem(isComposeItem);

				totalOriginalSalesAmount = totalOriginalSalesAmount + soSalesOrderItem.getOriginalSalesAmount();
				totalActualSalesAmount   = totalActualSalesAmount+soSalesOrderItem.getActualSalesAmount(); 
				
				taxAmount 				 = taxAmount+soSalesOrderItem.getTaxAmount();	
				//log.info("123");
				//soSalesOrderItemDAO.save(soSalesOrderItem);
				soSalesOrderItems.add(soSalesOrderItem);
			}
//回寫銷售單頭總金額
			soSalesOrderHead.setTotalOriginalSalesAmount(totalOriginalSalesAmount);
			soSalesOrderHead.setTotalActualSalesAmount(totalActualSalesAmount);
			
			Double actualTaxAmount = soSalesOrderService.calculateTaxAmount(soSalesOrderHead.getTaxType(),soSalesOrderHead.getTaxRate(),soSalesOrderHead.getTotalActualSalesAmount());
			soSalesOrderHead.setTaxAmount(actualTaxAmount);
			
			soSalesOrderHead.setSoSalesOrderItems(soSalesOrderItems);
			soSalesOrderHead.setExportExchangeRate(1D); //增加幣別的匯率
			
			Double balanceAmt = actualTaxAmount - taxAmount;			
			List<SoSalesOrderItem> salesOrderItemList = soSalesOrderHead.getSoSalesOrderItems();
		    if (salesOrderItemList != null && salesOrderItemList.size() > 0) {
			SoSalesOrderItem salesOrderItem = (SoSalesOrderItem) salesOrderItemList.get(salesOrderItemList.size() - 1);
			Double lastItemTaxAmt = salesOrderItem.getTaxAmount();
			if (lastItemTaxAmt == null) {
			    lastItemTaxAmt = 0D;
			}
			salesOrderItem.setTaxAmount(lastItemTaxAmt + balanceAmt);
		    }
		    
		    SoSalesOrderPayment soSalesOrderPayment = new SoSalesOrderPayment();
		    
		    Sequence seq =  soDepartmentOrderHeadDAO.findSeqByName("soDepartmentOrderPayment");
    		Long nextVal = seq.getCurrent_value() + seq.getIncrement();
    		seq.setCurrent_value(nextVal);
    		//soDepartmentOrderHeadDAO.save(seq);
		    
		    soSalesOrderPayment.setIndexNo(1L);
		    soSalesOrderPayment.setPosPaymentId(nextVal);
		    soSalesOrderPayment.setPosPaymentType("NT");
		    soSalesOrderPayment.setLocalCurrencyCode("NTD");
		    soSalesOrderPayment.setLocalAmount(totalActualSalesAmount);
		    //soDepartmentOrderHeadDAO.save(soSalesOrderPayment);
		    
		    List<SoSalesOrderPayment> Payments = new ArrayList();
		    Payments.add(soSalesOrderPayment);
		    soSalesOrderHead.setSoSalesOrderPayments(Payments);
		    
			return soSalesOrderHead;
		} catch (Exception ex) {
		    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
		    throw new Exception("銷貨單存檔時發生錯誤，原因：" + ex.getMessage());
		}
    }
    
/**百貨POS單更新**/
    public SoDepartmentOrderHead modifySoSalesOrder(SoDepartmentOrderHead updateObj, String loginUser) {
    	updateObj.setLastUpdatedBy(loginUser);
    	updateObj.setSalesTime(new Date());
    	soDepartmentOrderHeadDAO.update(updateObj);
    	log.info("POS單存檔完畢!");
    	return updateObj;
    }
    
    public void saveRealSales(SoSalesOrderHead soSalesOrderHead) {
    	List<SoSalesOrderPayment> payments = soSalesOrderHead.getSoSalesOrderPayments();
    	for(SoSalesOrderPayment payment:payments) {
    		soSalesOrderHeadDAO.save(payment);
    	}
    	soSalesOrderHeadDAO.save(soSalesOrderHead);
    }
    
 /**明細載入**/
    public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception {
    	try {
    		List<Properties> result = new ArrayList();
    		List<Properties> gridDatas = new ArrayList();
    		

    		// ======================帶入Head的值=========================
    		String headIdString = httpRequest.getProperty("headId");
    		String brandCode = httpRequest.getProperty("brandCode");
    		log.info("POS單明細載入，HEAD_ID = "+headIdString);
    		Long headId = NumberUtils.getLong(headIdString);// 要顯示的HEAD_ID
    		int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
    		int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
    		
    		HashMap map = new HashMap();//查詢用參數
    		
    		map.put("headId", headIdString);

    		// ==============================================================
    		List<SoSalesOrderItem> soSalesOrderItems = soDepartmentOrderHeadDAO.findT2PageLine(headId, iSPage, iPSize);
    		if (soSalesOrderItems != null && soSalesOrderItems.size() > 0) {
    			log.info("商品明細:"+soSalesOrderItems.size());
    			for(SoSalesOrderItem soSalesOrderItem:soSalesOrderItems){
    				ImItem imItem = imItemDAO.findItem(brandCode, soSalesOrderItem.getItemCode());
    				//ImItemPriceView imItem = imItemPriceViewDAO.findOneItemPriceView(brandCode, "1", soDepartmentOrderItem.getItemCode(),"Y","Y");
    				if(imItem!=null){
    					log.info(imItem.getItemBrand()+"/"+imItem.getItemCName());
    					soSalesOrderItem.setItemCName(imItem.getItemCName());

    				}
    			}
    			// 取得第一筆的INDEX
    			Long firstIndex = soSalesOrderItems.get(0).getIndexNo();
    			// 取得最後一筆 INDEX
    			Long maxIndex = soDepartmentOrderHeadDAO.findT2PageLineMaxIndex(headId);
    			result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, soSalesOrderItems, gridDatas, firstIndex, maxIndex));
    		} 
    		else {
    			result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map, gridDatas));
    		}
    		return result;
    	} catch (Exception ex) {
    		log.error("載入頁面顯示的銷貨明細發生錯誤，原因：" + ex.toString());
    		ex.printStackTrace();
    		throw new Exception("載入頁面顯示的銷貨明細失敗！");
    	}
    }
/**明細存檔**/
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception {
		try {
			String errorMsg = "";
		    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		    log.info("POS單明細存檔，HEAD_ID = "+headId);
		    SoSalesOrderHead soSalesOrderHead = this.findT2ById(headId);
			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,GRID_FIELD_NAMES);
			// Get INDEX NO
			int indexNo = soDepartmentOrderHeadDAO.findT2PageLineMaxIndex(headId).intValue();
			if (upRecords != null) {

			    for (Properties upRecord : upRecords) {

					// 先載入HEAD_ID OR LINE DATA
					Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
					String itemCode = upRecord.getProperty("itemCode");
					if (StringUtils.hasText(itemCode)) {

						SoSalesOrderItem soSalesOrderItem = soDepartmentOrderHeadDAO.findT2LineById(lineId);
					    if (soSalesOrderItem != null) {
			
					    	AjaxUtils.setPojoProperties(soSalesOrderItem,upRecord, GRID_FIELD_NAMES,GRID_FIELD_TYPES);
					    	soDepartmentOrderHeadDAO.update(soSalesOrderItem);
					    	log.info("資料更新 HEAD_ID"+soSalesOrderItem.getSoSalesOrderHead().getHeadId()+" LINE_ID:"+soSalesOrderItem.getLineId()+" 品號:"+soSalesOrderItem.getItemCode());
					    } else {

					    	indexNo++;
					    	SoSalesOrderItem newSoSalesOrderItem = new SoSalesOrderItem();
					    	
					    	AjaxUtils.setPojoProperties(newSoSalesOrderItem,upRecord, GRID_FIELD_NAMES,GRID_FIELD_TYPES);
					    	newSoSalesOrderItem.setIndexNo(Long.valueOf(indexNo));
					    	newSoSalesOrderItem.setSoSalesOrderHead(soSalesOrderHead);
					    	
					    	Sequence seq =  soDepartmentOrderHeadDAO.findSeqByName("soDepartmentOrderItem");
				    		Long nextVal = seq.getCurrent_value() + seq.getIncrement();
				    		seq.setCurrent_value(nextVal);
				    		soDepartmentOrderHeadDAO.save(seq);
				    		
				    		newSoSalesOrderItem.setLineId(nextVal);
					    	soDepartmentOrderHeadDAO.save(newSoSalesOrderItem);
					    	log.info("資料存檔 HEAD_ID"+newSoSalesOrderItem.getSoSalesOrderHead().getHeadId());
					    	log.info("資料存檔 LINE_ID:"+newSoSalesOrderItem.getLineId());
					    	log.info("資料存檔 品號:"+newSoSalesOrderItem.getItemCode());

					    }
					}
			    }
			}
	
		    return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			ex.printStackTrace();
		    log.error("更新銷貨明細時發生錯誤，原因：" + ex.toString());
		    throw new Exception("更新銷貨明細失敗！");
		}
	}
	
	/**明細存檔**/
	public List<Properties> updatePayment(Properties httpRequest) throws Exception {
		try {
			String errorMsg = "";
		    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		    log.info("POS單明細存檔，HEAD_ID = "+headId);
		    SoDepartmentOrderHead soDepartmentOrderHead = this.findById(headId);
			
		    return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			ex.printStackTrace();
		    log.error("更新銷貨明細時發生錯誤，原因：" + ex.toString());
		    throw new Exception("更新銷貨明細失敗！");
		}
	}
	
	
	
/**明細刪除**/
    public List<Properties> executeDetailDelete(Properties httpRequest) throws ValidationErrorException {
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		String itemIndexNo = null;
		try {
			/**前端資料**/
			Long indexNo = NumberUtils.getLong((String)httpRequest.getProperty("indexNo"));
			Long headId = NumberUtils.getLong((String)httpRequest.getProperty("headId"));
			List<SoDepartmentOrderItem> isDelItems = new ArrayList();

			/**主檔查詢**/
			SoDepartmentOrderHead head = this.findById(headId);
			if(null != head)
			{
				List<SoDepartmentOrderItem> soDepartmentOrderItems = head.getSoDepartmentOrderItem();
				if(soDepartmentOrderItems.size()>0)
				{
					log.info("刪除單據"+head.getHeadId()+"第"+indexNo+"項商品明細");
					for(SoDepartmentOrderItem soDepartmentOrderItem:soDepartmentOrderItems){
						log.info(indexNo + "/" + soDepartmentOrderItem.getIndexNo());
						if(indexNo.equals(soDepartmentOrderItem.getIndexNo())){
							log.info(soDepartmentOrderItem.getLineId()+"/"+soDepartmentOrderItem.getItemCode()+"/"+soDepartmentOrderItem.getIndexNo());
							//delete item
							isDelItems.add(soDepartmentOrderItem);
							//resetIndex
							log.info("刪除LINE_ID:"+soDepartmentOrderItem.getLineId());

						}
					}
					soDepartmentOrderItems.removeAll(isDelItems);
					Long newIndexNo = 1L;
					for(SoDepartmentOrderItem soDepartmentOrderItem:soDepartmentOrderItems){
						System.out.println("排序前index:"+soDepartmentOrderItem.getIndexNo()+" 排序後index:"+newIndexNo);
				    	soDepartmentOrderItem.setIndexNo(newIndexNo++);
					}
					log.info("最末筆指標位址:"+(newIndexNo-1));
					head.setSoDepartmentOrderItem(soDepartmentOrderItems);
					soDepartmentOrderHeadDAO.update(head);
					properties.setProperty("indexNo", AjaxUtils.getPropertiesValue(newIndexNo-1, ""));
				}
			}

			/**回傳資料**/
//			properties.setProperty("totaleActualSalesAmount", AjaxUtils.getPropertiesValue(totaleActualSalesAmount, "0"));
			result.add(properties);
			return result;
		} catch (Exception ex) {
			log.error("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料發生錯誤，原因：" + ex.getMessage());
			throw new ValidationErrorException("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料失敗！");
		}
    }

    /**全部折扣(讓)**/
    public List<Properties> executeDiscount(Properties httpRequest) throws ValidationErrorException {
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		String itemIndexNo = null;
		try {
			/**前端資料**/
			Long vAmount = NumberUtils.getLong((String)httpRequest.getProperty("vAmount"));
			Long headId = NumberUtils.getLong((String)httpRequest.getProperty("headId"));
			String category= (String)httpRequest.getProperty("category");//1:折扣 2:折讓
			String executeWay= (String)httpRequest.getProperty("executeWay");
			Long ptr = NumberUtils.getLong((String)httpRequest.getProperty("ptr"));
			Double totalAmount = 0D;
			log.info(headId+"進行折扣作業 category:"+category+"/ptr:"+ptr+"  (ptr=0:全部 & category=1:折扣 & category=2:折讓)");
			/**主檔查詢**/
			SoSalesOrderHead head = this.findT2ById(headId);
			if(null != head)
			{
				List<SoSalesOrderItem> soSalesOrderItems = head.getSoSalesOrderItems();
				if(soSalesOrderItems.size()>0)
				{
					for(SoSalesOrderItem soSalesOrderItem:soSalesOrderItems){
						if(ptr == 0 || soSalesOrderItem.getIndexNo().equals(ptr)){
							if(validateDiscountItem(soSalesOrderItem, executeWay)){//檢核商品
								totalAmount = totalAmount + soSalesOrderItem.getActualSalesAmount();//統計銷售總額供計算全部折讓使用
							}
						}
					}
					log.info("總計:"+totalAmount);
					Long remainderAmount = vAmount;
					for(SoSalesOrderItem soSalesOrderItem:soSalesOrderItems){
						log.info("ptr:"+ptr+"/ INDEX:"+soSalesOrderItem.getIndexNo());
						if(ptr == 0 || soSalesOrderItem.getIndexNo().equals(ptr)){
							if(validateDiscountItem(soSalesOrderItem, executeWay)){//檢核商品
								boolean isLast=soSalesOrderItem.getIndexNo()==soSalesOrderItems.size();
								remainderAmount = calculateDiscountAmount(soSalesOrderItem,vAmount,totalAmount,remainderAmount,category,isLast);//逐筆計算折抵金額並回寫折扣率
							}
						}
					}
					head.setSoSalesOrderItems(soSalesOrderItems);//.setSoDepartmentOrderItem(soSalesOrderItems);//回存單身
				}
			}
			/**回傳資料**/
//			properties.setProperty("totaleActualSalesAmount", AjaxUtils.getPropertiesValue(totaleActualSalesAmount, "0"));
			result.add(properties);
			return result;
		} catch (Exception ex) {
			log.error("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料發生錯誤，原因：" + ex.getMessage());
			ex.printStackTrace();
			throw new ValidationErrorException("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料失敗！");
		}
    }
    
    /**全部折扣(讓)**/
/*    public List<Properties> executeTotalDeductionAmount(Properties httpRequest) throws ValidationErrorException {
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		String itemIndexNo = null;
		try {
			//前端資料
			Long vDeductionAmount = NumberUtils.getLong((String)httpRequest.getProperty("vDeductionAmount"));
			Long headId = NumberUtils.getLong((String)httpRequest.getProperty("headId"));
			String brandCode = (String)httpRequest.getProperty("brandCode");
			String category= (String)httpRequest.getProperty("category");

			Double totalAmount = 0D;

			//主檔查詢
			SoDepartmentOrderHead head = this.findById(headId);
			if(null != head)
			{
				List<SoDepartmentOrderItem> soDepartmentOrderItems = head.getSoDepartmentOrderItem();
				if(soDepartmentOrderItems.size()>0)
				{
					for(SoDepartmentOrderItem soDepartmentOrderItem:soDepartmentOrderItems){

						if(validateDiscountItem(soDepartmentOrderItem)){//檢核商品
							totalAmount = totalAmount + soDepartmentOrderItem.getActualSalesAmount();
						}
					}
					
					log.info("總計:"+totalAmount);
					
					//逐筆計算折抵金額並回寫折扣率
					for(SoDepartmentOrderItem soDepartmentOrderItem:soDepartmentOrderItems){

						if(validateDiscountItem(soDepartmentOrderItem)){//檢核商品
							if(category.equals("totalDiscount")){
								//逐筆計算折扣金額並回寫折扣率
								calculateDiscountAmount(soDepartmentOrderItem,vDeductionAmount,totalAmount,"1");
							}
							else if(category.equals("totalDeductionAmount")){
								//逐筆計算折抵金額並回寫折扣率
								calculateDiscountAmount(soDepartmentOrderItem,vDeductionAmount,totalAmount,"2");
							}
						}
					}
					head.setSoDepartmentOrderItem(soDepartmentOrderItems);
				}
			}
			//回傳資料
//			properties.setProperty("totaleActualSalesAmount", AjaxUtils.getPropertiesValue(totaleActualSalesAmount, "0"));
			result.add(properties);
			return result;
		} catch (Exception ex) {
			log.error("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料發生錯誤，原因：" + ex.getMessage());
			ex.printStackTrace();
			throw new ValidationErrorException("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料失敗！");
		}
    }*/
    
/**計算折扣(讓)金額並回寫折扣率**/
    private Long calculateDiscountAmount(SoSalesOrderItem soSalesOrderItem,Long discountAmount,Double totalAmount,Long remainderAmount,String discountType,boolean isLast) throws Exception{
    	//紀錄原售價
    	Double beforeDiscountAmount = soSalesOrderItem.getActualSalesAmount();
    	int newDiscountRate = 100;
    	if("1".equals(discountType)){//折扣
    		if(discountAmount<100 || discountAmount>0){
    			//計算新售價
		    	Double actualSalesAmount = new Double((int)((soSalesOrderItem.getActualSalesAmount()*discountAmount/100)+0.5)).doubleValue();
		    	soSalesOrderItem.setActualSalesAmount(actualSalesAmount);
    		}
    	}
    	else if("2".equals(discountType)){//折讓
    		
    		if(discountAmount <= totalAmount){
		    	//計算新售價 = 單一商品售價*(1-(折讓金額/目前所有商品售價加總)) 
	    		//Double actualSalesAmount =(soDepartmentOrderItem.getActualSalesAmount() * (1-1/totalAmount*discountAmount));
    			
	    		int actualSalesAmount ;
	    		if(isLast){
	    			log.info("計算新售價 = 單一商品售價-剩餘折讓金額");
	    			log.info("原售價:"+soSalesOrderItem.getActualSalesAmount()+"  剩餘折讓金額:"+remainderAmount);
	    			actualSalesAmount = (int)(soSalesOrderItem.getActualSalesAmount()-remainderAmount); 
	    		}else{
	    			log.info("計算新售價 = 單一商品售價*(1-(折讓金額/目前所有商品售價加總))");
	    			log.info("原售價:"+soSalesOrderItem.getActualSalesAmount()+"  折讓占商品總額比"+(1-(discountAmount/totalAmount)));
	    			actualSalesAmount = (int)(soSalesOrderItem.getActualSalesAmount() * (1-1/totalAmount*discountAmount));
	    		}
	    		soSalesOrderItem.setActualSalesAmount(new Double(actualSalesAmount).doubleValue());
	//	    	Double actualSalesAmount = new Double((int)((soDepartmentOrderItem.getActualSalesAmount()*discountAmount/100)+0.5)).doubleValue();
    		}else{
    			throw new Exception("折讓金額錯誤，折讓金額不可超過商品售價");
    		}
    	}else{
    		throw new Exception("折扣類型輸入錯誤");
    	}
    	//回寫折扣率
    	double newActureUnitSalesAmount = soSalesOrderItem.getActualSalesAmount()/soSalesOrderItem.getQuantity();//單一售價
		newDiscountRate = (int)(newActureUnitSalesAmount/soSalesOrderItem.getOriginalUnitPrice()*100+0.5);
		soSalesOrderItem.setDiscountRate(new Double(newDiscountRate).doubleValue());
		log.info("   折扣前金額:" + beforeDiscountAmount + " /   折扣後金額:" + soSalesOrderItem.getActualSalesAmount() + " =  折扣率:" + soSalesOrderItem.getDiscountRate());
		double discountNum = sub( beforeDiscountAmount,soSalesOrderItem.getActualSalesAmount() );
		soSalesOrderItem.setDiscountType(discountType);
		soSalesOrderItem.setDiscount(discountNum);
		log.info("  折扣/讓 金額："+discountNum +",type:"+discountType);
		remainderAmount = remainderAmount - (int)(beforeDiscountAmount-soSalesOrderItem.getActualSalesAmount());
		log.info("剩餘折讓金額:"+remainderAmount);
    	return remainderAmount;
    }
    
    
    
    public static double sub(double v1,double v2){ 
    	BigDecimal b1 = new BigDecimal(Double.toString(v1)); 
    	BigDecimal b2 = new BigDecimal(Double.toString(v2)); 
    	return b1.subtract(b2).doubleValue(); 
    } 
    
/**檢核商品是否為可折扣商品**/
    private boolean validateDiscountItem(SoSalesOrderItem soSalesOrderItem, String executeWay) throws Exception{
    	boolean isAllDiscountItem =true;
    	boolean isLimited = false;
		ImItem imItem = imItemDAO.findById(soSalesOrderItem.getItemCode());
		if("AUTO".equals(executeWay)){
			isLimited = true;
		}else if("MANUAL".equals(executeWay)){
			isLimited = false;
		}
		log.info("soSalesOrderItem.getItemCode():"+soSalesOrderItem.getItemCode());
		if(null != imItem){
			if("Y".equals(imItem.getIsComposeItem())){
				isAllDiscountItem = false;
			}
			if("Y".equals(imItem.getIsServiceItem())){
				isAllDiscountItem = false;
			}
			if(soSalesOrderItem.getActualSalesAmount() <= 0 ){
				isAllDiscountItem = false;
			}
			if(soSalesOrderItem.getOriginalUnitPrice() <= 0){
				isAllDiscountItem = false;
			}
			if(soSalesOrderItem.getDiscountRate() < 80 && isLimited){
				isAllDiscountItem = false;
			}
		}
		else{
			throw new Exception("商品"+imItem+"品號輸入錯誤");
		}
    	return isAllDiscountItem;
    }
/**小計金額計算**/
    public List<Properties> findTotalOriginalSalesAmount(Properties httpRequest)throws ValidationErrorException{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	System.out.println("findTotalOriginalSalesAmount");
    	try{
    		//String brandCode = httpRequest.getProperty("brandCode");
    		Long headId = NumberUtils.getLong((String)httpRequest.getProperty("headId"));
    		Double totaleActualSalesAmount=0D;
    		System.out.println("headId:"+headId);
    		SoSalesOrderHead soSalesOrderHead = this.findT2ById(headId);
    		List<SoSalesOrderItem> soSalesOrderItems = soSalesOrderHead.getSoSalesOrderItems();
    		for(SoSalesOrderItem soSalesOrderItem:soSalesOrderItems)
    		{
    			totaleActualSalesAmount = totaleActualSalesAmount+Math.round(soSalesOrderItem.getActualSalesAmount());
    		}
    		log.info("小計總額:"+Double.toString(totaleActualSalesAmount));
			properties.setProperty("totaleActualSalesAmount", Long.toString(Math.round(totaleActualSalesAmount)));
			
			
			List<BuCurrency> buCurrency = buCurrencyDAO.findCurrencyByEnable("Y");
			for(BuCurrency cur:buCurrency) {
				if(cur.getCurrencyCode().equals("NTD")) {
					properties.setProperty("NTD_COUNT", String.valueOf(totaleActualSalesAmount*cur.getExChangeRate()));
				}else if(cur.getCurrencyCode().equals("USD")) {
					properties.setProperty("USD_COUNT", String.valueOf(totaleActualSalesAmount*cur.getExChangeRate()));
				}else if(cur.getCurrencyCode().equals("JPY")) {
					properties.setProperty("JPY_COUNT", String.valueOf(totaleActualSalesAmount*cur.getExChangeRate()));
				}else if(cur.getCurrencyCode().equals("CNY")) {
					properties.setProperty("CNY_COUNT", String.valueOf(totaleActualSalesAmount*cur.getExChangeRate()));
				}
			}
			System.out.println("小記轉換匯率");
			
			result.add(properties);
			
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return result;
    }
    
    
    public List<Properties> findVIP(Properties httpRequest)throws ValidationErrorException{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	
    	try{
    		String brandCode = httpRequest.getProperty("brandCode");
			String searchKey = httpRequest.getProperty("searchKey");
			log.info("會員查詢:"+brandCode+"/"+searchKey);                                                                                                                                              
			List<BuCustomerWithAddressView> customers;
			log.info("會員編號查詢...");
			String[] propertyName = {"bradCode","customerCode"};
			customers = buCustomerWithAddressViewDAO.findVIPByCode(brandCode, searchKey);

			
			if(customers.size()!=0){
				properties.setProperty("customerCode", customers.get(0).getCustomerCode());
				properties.setProperty("customerName", customers.get(0).getChineseName());
				log.info("查詢成功!回覆結果:"+customers.get(0).getCustomerCode()+" "+customers.get(0).getChineseName());
			}
			else{
				log.info("會員ID查詢...");
				customers = buCustomerWithAddressViewDAO.findVIPByID(brandCode, searchKey);
				if(customers.size()!=0){
					properties.setProperty("customerCode", customers.get(0).getCustomerCode());
					properties.setProperty("customerName", customers.get(0).getChineseName());
					log.info("查詢成功!回覆結果:"+customers.get(0).getCustomerCode()+" "+customers.get(0).getChineseName());
				}
				else{
					log.info("會員編號查詢...");
					customers = buCustomerWithAddressViewDAO.findVIPByPhone(brandCode, searchKey);
					if(customers.size()!=0){
						properties.setProperty("customerCode", customers.get(0).getCustomerCode());
						properties.setProperty("customerName", customers.get(0).getChineseName());
						log.info("查詢成功!回覆結果:"+customers.get(0).getCustomerCode()+" "+customers.get(0).getChineseName());
					}
					else{
						log.info("查詢失敗");
						properties.setProperty("customerCode", "");
						properties.setProperty("customerName", "");
					}
				}
			}

			result.add(properties);
			
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return result;
    }
    public List<Properties> findTodaySales(Properties httpRequest)throws ValidationErrorException{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	
    	try{
    		String brandCode = httpRequest.getProperty("brandCode");
			String shopCode = httpRequest.getProperty("shopCode");
			String ComNum = "";			//公司編號
			String CompanyName = "";	//公司名稱
			String ShopName = "";		//專櫃名稱
			String Title = "";			//銷售單標題
			String CompanyAddress = "";	//公司地址
			String CompanyID = "";		//公司統編
			String CompanyTel = "";		//公司電話
			String SaleDateTime = "";	//銷貨日期時間
			
			
			BuShop buShop = (BuShop)buShopDAO.findByBrandCodeAndShopCode(brandCode, shopCode);//銷售專櫃
			ComNum = "COM99";
//			CompanyName = "如盈股份有限公司";
			CompanyName = buShop.getReserve3();
			Title = "<< 當 日 銷 售 統 計 表 >>";
			ShopName = buShop.getShopCName();
			CompanyAddress = buShop.getShopAddress();
			BuShopMachine buShopMachine = (BuShopMachine)buShopMachineDAO.findByBrandCodeAndMachineCode(brandCode, shopCode);
//			CompanyID = "23891530";
			CompanyID = buShop.getReserve4();
			CompanyTel = buShop.getTel();
			Date date = new Date();
			log.info( DateUtils.formatToTWDate(date, DateUtils.C_DATE_PATTON_DEFAULT)+" "+DateUtils.format(date, DateUtils.C_TIME_PATTON_HHMMSS));
			SaleDateTime = DateUtils.formatToTWDate(date, DateUtils.C_DATE_PATTON_DEFAULT)+" "+DateUtils.format(date, DateUtils.C_TIME_PATTON_HHMMSS);//DateUtils.format(new Date());

			
			
			
			
			
			
			log.info("當日銷售查詢:"+brandCode+"/"+shopCode);
			List soList = soPostingTallyDAO.findPostingDataListBySalesUnit(brandCode,shopCode);
			Object[] obj = (Object[])soList.get(0);

			log.info(DateUtils.format(date, DateUtils.C_DATE_PATTON_SLASH)+"銷售 總筆數:"+obj[1]+" 總金額:"+obj[0]);
			properties.setProperty("PrinterId", buShopMachine.getPrinterId());
			properties.setProperty("ComNum", ComNum);
			properties.setProperty("CompanyName", CompanyName);
			properties.setProperty("ShopName", ShopName);
			properties.setProperty("Title", Title);
			properties.setProperty("CompanyAddress", CompanyAddress);
			properties.setProperty("CompanyID", CompanyID);
			properties.setProperty("CompanyTel", CompanyTel);
			properties.setProperty("SaleDateTime", SaleDateTime);
			
			properties.setProperty("totalCount", obj[1].toString());
			properties.setProperty("totalAmount", obj[0].toString());
			
			result.add(properties);
			
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return result;
    }
    
    /**單據列印**/ 
    public List<Properties> importPrint(Properties httpRequest) throws ValidationErrorException{
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		
		try{
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			String brandCode = httpRequest.getProperty("brandCode");
			
			final String COMMA_SIGN = ",";
			String ComNum = "";			//公司編號
			String CompanyName = "";	//公司名稱
			String ShopName = "";		//專櫃名稱
			String Title = "";			//銷售單標題
			String CompanyAddress = "";	//公司地址
			String CompanyID = "";		//公司統編
			String CompanyTel = "";		//公司電話
			String SaleDateTime = "";	//銷貨日期時間
			String Pg = "";				//VIP
			String TY = "";				//年齡層
			String CashierID = "";		//班別
			String SalerID = "";		//負責人員
			
			String ItemID = "";			//品號
			String UnitPrice = "";		//單價
			String ItemPrices = "";		//售價
			String Categories = "";		//商品分類
			String Quantity = "";		//數量
			String ItemNames = "";		//品名
			String Remark = "";			//備註
			String Discount = "";		//折扣率
			String DiscountPrice = "";	//折扣金額
			
			String Sum = "";			//總額
			String TotalSum = "";		//單據總額
			String ReceivedMoney = "";	//應付金額
			String CreditCard = "";		//信用卡
			String Voucher = "";		//禮券
			String Void = "";			//

			SoSalesOrderHead head = (SoSalesOrderHead) this.findT2ById(headId);//銷售單頭
			System.out.println("head != null "+head.getHeadId());
			if(null!=head){
				ComNum = "99";
				CompanyName = "tasameng 采盟股份有限公司";
				Title = "銷貨單";
				ShopName = "SHOP_NAME";//buShop.getShopCName();
				CompanyAddress = "COMPANY_ADDRESS";//buShop.getShopAddress();
				CompanyID = "buShop.getReserve4()";//buShop.getReserve4();
				CompanyTel = "buShop.getTel()";//buShop.getTel();
				TY = head.getSalesType();
				log.info( DateUtils.formatToTWDate(head.getSalesOrderDate(), DateUtils.C_DATE_PATTON_DEFAULT)+" "+DateUtils.format(head.getSalesOrderDate(), DateUtils.C_TIME_PATTON_HHMMSS));
				SaleDateTime = DateUtils.formatToTWDate(head.getSalesOrderDate(), DateUtils.C_DATE_PATTON_DEFAULT)+" "+DateUtils.format(head.getSalesOrderDate(), DateUtils.C_TIME_PATTON_HHMMSS);//DateUtils.format(new Date());
				if(null!=head.getCustomerCode()){
					Pg=head.getCustomerCode();
				}
				CashierID = Long.toString(head.getHeadId());
				SalerID=head.getSuperintendentCode();
				
				//============================================================================
				String printTxtHead = "";
				printTxtHead = printTxtHead + CompanyName+ ShopName+ CompanyAddress+ CompanyID+ Title+ CompanyTel+ SaleDateTime;
				//printText("\n", 1, 0, 0);
				//printText(printTxtHead, 1, 0, 0);
				//============================================================================
				
				List<SoSalesOrderItem> soSalesOrderItems = head.getSoSalesOrderItems();//.getSoDepartmentOrderItem();//銷售單身
				if(null!=soSalesOrderItems || soSalesOrderItems.size()!=0){
				/*
					for(SoDepartmentOrderItem soDepartmentOrderItem:soDepartmentOrderItems){
						ImItem imItem = imItemService.findItem(brandCode, soDepartmentOrderItem.getItemCode());
							if(soDepartmentOrderItem.getIndexNo()>1){
								
							}
							ItemID += soDepartmentOrderItem.getItemCode();
							ItemNames += imItem.getItemCName();
							ItemPrices += Math.round(soDepartmentOrderItem.getOriginalUnitPrice()*soDepartmentOrderItem.getQuantity());
							Categories += imItem.getCategory02();
							if(soDepartmentOrderItem.getQuantity() > 1){
							Quantity += Long.toString(Math.round(soDepartmentOrderItem.getQuantity()));
							UnitPrice += Math.round(soDepartmentOrderItem.getOriginalUnitPrice());
						}
						if(null!=soDepartmentOrderItem.getReserve1())
							Remark += soDepartmentOrderItem.getReserve1();
						else
							Remark += "";
						log.info((100-soDepartmentOrderItem.getDiscountRate()) + " " +(soDepartmentOrderItem.getOriginalUnitPrice()-soDepartmentOrderItem.getActualSalesAmount()));
						if(soDepartmentOrderItem.getDiscountRate()!=100){
							Discount += Math.round((100-soDepartmentOrderItem.getDiscountRate()));
							DiscountPrice += Math.round(((soDepartmentOrderItem.getOriginalUnitPrice()*soDepartmentOrderItem.getQuantity()-soDepartmentOrderItem.getActualSalesAmount())*-1));
						}
					}
					result.add(properties);*/
					                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
					//print test 
//					print pm = new print();// 實例化列印類
//					pm.pageSize = 2;//列印
//					pm.doPrint(head);
					tw.com.tm.erp.test.PrintInvoice pm = new tw.com.tm.erp.test.PrintInvoice();// 實例化列印類
					pm.pageSize = 2;//列印兩頁
					//pm.head = head; //小D
					pm.starPrint();
					
					return result;
				}
				else{
					throw new Exception(headId+"單據明細為空，查詢失敗");
				}
			}
			else{
				throw new Exception(headId+"單據查詢失敗");
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ValidationErrorException("列印表單失敗");
		}		
	}
    
    public void printInvoice() {
    	
    	try {
    		
    		
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	
    }
    
    /**
     * SOP執行反確認
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public List<Properties> executeAJAXAntiConfirm(Properties httpRequest) throws FormException,Exception {


    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
		try {
			String resultMsg="";
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    	log.info("單據作廢,單號:"+headId);
	    	SoDepartmentOrderHead soDepartmentOrderHead = this.findById(headId);
	    	if(null!=soDepartmentOrderHead){
	    		log.info("對應銷售單HEAD_ID:"+soDepartmentOrderHead.getSalesoOrderId());
			    String shopCode = (String) httpRequest.getProperty("shopCode");
			    Date date = new Date();
			    if(shopCode.equals(soDepartmentOrderHead.getShopCode())){
			    	if(DateUtils.format(date, DateUtils.C_DATE_PATTON_SLASH).equals(DateUtils.format(soDepartmentOrderHead.getSalesOrderDate(), DateUtils.C_DATE_PATTON_SLASH))){
					    SoSalesOrderHead salesOrderHeadPO = soSalesOrderMainService.findById(soDepartmentOrderHead.getSalesoOrderId());
					    // ====================取得條件資料======================
					    //HashMap conditionMap = getConditionData(parameterMap);

					    String employeeCode = (String) httpRequest.getProperty("employeeCode");
					    String organizationCode = (String) httpRequest.getProperty("organizationCode");
					    
					    if (!"SOP".equals(salesOrderHeadPO.getOrderTypeCode())) {
					    	resultMsg = "單別："+ salesOrderHeadPO.getOrderTypeCode() + "不可執行作廢！";
			//			throw new ValidationErrorException("單別："+ salesOrderHeadPO.getOrderTypeCode() + "不可執行作廢！");
					    } else if (!OrderStatus.SIGNING.equals(salesOrderHeadPO.getStatus())) {
					    	resultMsg = "單據狀態非"+ OrderStatus.getChineseWord(OrderStatus.SIGNING) + "時不可執行作廢！請確認單據狀態";
			
			//			throw new ValidationErrorException("狀態："+ OrderStatus.getChineseWord(salesOrderHeadPO.getStatus()) + "不可執行作廢！");
					    }else {
						    soSalesOrderMainService.revertToOriginallyAvailableQuantity(salesOrderHeadPO, organizationCode, employeeCode);
						    salesOrderHeadPO.setStatus(OrderStatus.VOID);
						    modifySoSalesOrder(salesOrderHeadPO, employeeCode);
						    voidCoupon(salesOrderHeadPO);
						    
						    resultMsg = headId + "執行作廢成功！";
					    }
			    	}
			    	else{
					    	resultMsg = "僅可作廢當日之銷售單據";
					}
			    }
			    else{
			    	resultMsg = "僅可作廢本店之銷售單據";
			    }
	    	}
	    	else{
	    		resultMsg = "查無銷售單！請確認原訂單編號";
	    	}
		    properties.setProperty("resultMsg", resultMsg);
		    log.info(resultMsg);
		    result.add(properties);
		} catch (FormException fe) {
		    log.error("執行作廢失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    log.error("執行作廢時發生錯誤，原因：" + ex.toString());
		    throw new Exception("執行作廢時發生錯誤，原因：" + ex.getMessage());
		}
		return result;
    }


	private void modifySoSalesOrder(SoSalesOrderHead updateObj, String loginUser) {
		updateObj.setLastUpdatedBy(loginUser);
		updateObj.setLastUpdateDate(new Date());
		soSalesOrderHeadDAO.update(updateObj);
    }
	
	public Map executeGoodsSearchInitial(Map parameterMap) throws Exception {
		log.info("executeGoodsSearchInitial");
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String superintendentCode = (String) PropertyUtils.getProperty(otherBean, "superintendentCode");
			log.info("superintendentCode:"+superintendentCode);
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", loginBrandCode);
			findObjs.put("warehouseCode", "");
			findObjs.put("warehouseName", "");
			findObjs.put("storage", "");
			findObjs.put("storageArea", "");
			findObjs.put("storageBin", "");
			findObjs.put("warehouseTypeId", 0L);
			findObjs.put("categoryCode", "");
			findObjs.put("locationId", 0L);
			findObjs.put("warehouseManager", "");
			findObjs.put("taxTypeCode", "");
			findObjs.put("enable", "Y");
			findObjs.put("employeeCode", loginEmployeeCode);

			
			Map multiList = new HashMap(0); 
			log.info(loginBrandCode+"-----------------------");
			resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());
			
//			List<ImWarehouse> allWarehouses = imWarehouseDAO.find(findObjs);
			List<ImWarehouse> allWarehouses = imWarehouseDAO.getWarehouseByWarehouseEmployee(loginBrandCode, superintendentCode, "Y");
			multiList.put("allWarehouses", AjaxUtils.produceSelectorData(allWarehouses, "warehouseCode", "warehouseName", true, true));
			
			//List<ImWarehouse> imArrivalWarehouses = imWarehouseDAO.find(findObjs);
			//multiList.put("allArrivalWarehouses", AjaxUtils.produceSelectorData(imArrivalWarehouses, "warehouseCode", "warehouseName", true, true));
			
			//List<ImItemCategory> allItemCategories = imItemCategoryDAO.findByCategoryType(loginBrandCode, "ITEM_CATEGORY");
			//multiList.put("allItemCategories", AjaxUtils.produceSelectorData(allItemCategories, "categoryCode", "categoryName", true, !"Y".equalsIgnoreCase((String) resultMap.get("itemCategoryMode"))));
            
			List<ImItemCategory> allItemBrands = imItemCategoryDAO.findAllBrand(loginBrandCode, "ItemBrand");
			log.info("allItemBrands:"+allItemBrands.size());
			multiList.put("allItemBrands", AjaxUtils.produceSelectorData(allItemBrands, "categoryCode", "categoryName", true, true));

			
			Map itemCategoryMap = imItemCategoryService.getItemCategoryRelatedList1(loginBrandCode, null, null);
			List<ImItemCategory> allCategory01 = (List<ImItemCategory>) itemCategoryMap.get("allCategory01");
			List<ImItemCategory> allCategory02 = (List<ImItemCategory>) itemCategoryMap.get("allCategory02");
			List<ImItemCategory> allCategory03 = (List<ImItemCategory>) itemCategoryMap.get("allCategory03");
			List<ImItemCategory> allCategory13 = (List<ImItemCategory>) itemCategoryMap.get("allCategory13");
			
			multiList.put("allCategory01", AjaxUtils.produceSelectorData(allCategory01, "categoryCode", "categoryName", true, true));
			multiList.put("allCategory02", AjaxUtils.produceSelectorData(allCategory02, "categoryCode", "categoryName", true, true));
			multiList.put("allCategory03", AjaxUtils.produceSelectorData(allCategory03, "categoryCode", "categoryName", true, true));
			multiList.put("allCategory13", AjaxUtils.produceSelectorData(allCategory13, "categoryCode", "categoryName", false, true));

			//List<ImItemCategory> allCategory07 = this.imItemCategoryDAO.findByCategoryType(loginBrandCode, "CATEGORY07");
			//multiList.put("allCategory07", AjaxUtils.produceSelectorData(allCategory07, "categoryCode", "categoryName", false, true));
			
			resultMap.put("multiList", multiList);

		} catch (Exception ex) {
			log.error("表單初始化失敗，原因：" + ex.toString());
			ex.printStackTrace();
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "表單初始化失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}

		return resultMap;

	}
	
	public Map executeSalesSearchInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String customerCode = (String) PropertyUtils.getProperty(otherBean, "customerCode");
			BuCustomerWithAddressView buCustomerWithAddressView = buCustomerWithAddressViewDAO.findEnableCustomer(loginBrandCode, customerCode);
			String customerName = buCustomerWithAddressView.getChineseName();
			BuShop buShop = (BuShop)buShopDAO.findByBrandCodeAndShopCode(loginBrandCode, buCustomerWithAddressView.getCategory07());
			Map multiList = new HashMap(0); 
			resultMap.put("brandCode", buBrandDAO.findById(loginBrandCode).getBrandCode());
			resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());
			resultMap.put("form", buCustomerWithAddressView);
//			resultMap.put("customerCode", customerCode);
			resultMap.put("area", buCustomerWithAddressView.getArea());
			resultMap.put("customerName", customerName);
			resultMap.put("category07Name", buShop != null ? buShop.getShopCName() : "查無資料");
			
		} catch (Exception ex) {
			log.error("表單初始化失敗，原因：" + ex.toString());
			ex.printStackTrace();
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "表單初始化失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);
		}

		return resultMap;

	}
    public List<Properties> getAJAXFormDataByItemCode(Properties httpRequest)throws Exception {
		//log.info("getFormDataBySupplier");
		Properties pro = new Properties();
		List re = new ArrayList();

		String brandCode        = httpRequest.getProperty("brandCode");
		String itemCode     = httpRequest.getProperty("itemCode");
		String employeeCode     = httpRequest.getProperty("employeeCode");
		String warehouseCode = "";

		try{
			List<ImWarehouse> warehouseCodes = imWarehouseEmployeeDAO.findByEmployeeCode(brandCode, employeeCode);
			log.info("warehouseCodes.size()"+warehouseCodes.size());
			itemCode = itemCode.toUpperCase();
			if(warehouseCodes != null){
				warehouseCode = warehouseCodes.get(0).getWarehouseCode();
			}
			log.info("brandCode:"+brandCode);
			log.info("itemCode:"+itemCode);
			log.info("warehouseCode:"+warehouseCode);
			
			/*
			 * 0.im.id.itemCode
			 * 1.im.id.warehouseCode
			 * 2.im.category01
			 * 3.im.category02
			 * 4.im.category03
			 * 5.im.unitPirce
			 * 6.sum( im.currentOnHandQty
			 * 7.im.itemCName*/
			List items = imItemOnHandViewDAO.getOnHandQtyByWareHouseCodes1(brandCode,warehouseCode,itemCode);

			if (items.size() > 0) { 
    			for (int index = 0; index < items.size(); index++) { 
            	    Object[] item = (Object[]) items.get(index);
					pro.setProperty("itemCode",AjaxUtils.getPropertiesValue((String)item[0],  ""));
					pro.setProperty("warehouseCode",AjaxUtils.getPropertiesValue((String)item[1],  ""));
					pro.setProperty("category01",AjaxUtils.getPropertiesValue((String)item[2],  ""));
					pro.setProperty("category02",AjaxUtils.getPropertiesValue((String)item[3],  ""));
					pro.setProperty("category03",AjaxUtils.getPropertiesValue((String)item[4],  ""));
					pro.setProperty("category13",AjaxUtils.getPropertiesValue((String)item[5],  ""));
					pro.setProperty("currentOnHandQty",AjaxUtils.getPropertiesValue((Double)item[6],  ""));
					pro.setProperty("itemCName",AjaxUtils.getPropertiesValue((String)item[7],  ""));
					pro.setProperty("unitPirce",AjaxUtils.getPropertiesValue((Double)item[8],  ""));
					pro.setProperty("imageFileName",AjaxUtils.getPropertiesValue((String)item[9],  ""));
					log.info("AjaxUtils.getPropertiesValue" + AjaxUtils.getPropertiesValue((String)item[9],  ""));
				}
    		}
			else{
				log.info("沒東西找屁");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		re.add(pro);
		return re;
	}

    
	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {
		log.info("getAJAXSearchPageData");
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================
			String itemCode = httpRequest.getProperty("itemCode");// 品號
			String itemCName = httpRequest.getProperty("itemCName");
			String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌 
			String employeeCode = httpRequest.getProperty("employeeCode");// 品牌 
			String warehouseCode = httpRequest.getProperty("warehouseCode");//庫別
			String category01 = httpRequest.getProperty("category01");//大類
			String category02 = httpRequest.getProperty("category02");//中類
			String category03 = httpRequest.getProperty("category03");//小類
			String category01Advance = httpRequest.getProperty("category01Advance");//小類
			String category02Advance = httpRequest.getProperty("category02Advance");//小類
			String category03Advance = httpRequest.getProperty("category03Advance");//小類
			String category13 = httpRequest.getProperty("category13");//系列
			String searchWay = httpRequest.getProperty("searchWay");
			String superintendentCode = httpRequest.getProperty("superintendentCode");
			String isShowOwn = httpRequest.getProperty("isShowOwn");
			//String bySeries = httpRequest.getProperty("bySeries");//系列
			String itemCodeList = "";
			List<ImWarehouse> warehouseCodes = imWarehouseEmployeeDAO.findByEmployeeCode(brandCode, employeeCode);
			log.info("warehouseCodes.size()"+warehouseCodes.size());
			if(warehouseCodes != null){
				warehouseCode = warehouseCodes.get(0).getWarehouseCode();
			}
			List<ImWarehouse> allWarehouses = imWarehouseDAO.getWarehouseByWarehouseEmployee(brandCode, superintendentCode, "Y");
			List<String> warehouseCodeList = new ArrayList();
			HashMap findObjs = new HashMap();
			log.info("itemCode:"+itemCode);
			log.info("brandCode:"+brandCode);
			log.info("employeeCode:"+employeeCode);
			log.info("warehouseCode:"+warehouseCode);
			log.info("category01:"+category01);
			log.info("category02:"+category02);
			log.info("category03:"+category03);
			log.info("category13:"+category13);
			log.info("searchWay:"+searchWay);
			log.info("isShowOwn:"+isShowOwn);
			log.info("allWarehouses.size:"+allWarehouses.size());
			
			for(int i = 0; i < allWarehouses.size(); i++){
				warehouseCodeList.add(allWarehouses.get(i).getWarehouseCode());
			}
			
			if(StringUtils.hasText(itemCode)){
				itemCode = itemCode.toUpperCase();
				if("byColor".equals(searchWay) && "T1BS".equals(brandCode)){
					/*log.info("byColor");
					List<ImItemColor> itemColors = imItemColorDAO.findByBrand(brandCode);
					log.info("itemColors.size():"+itemColors.size());
					for(int i = 0; i < itemColors.size(); i++){
						if(itemCode.indexOf(itemColors.get(i).getId().getColorCode()) >= 0){
							int index = itemCode.indexOf(itemColors.get(i).getId().getColorCode());
							for(int j = 0; j < itemColors.size(); j++){
								String itemCodeOtherColor = itemCode.replace(itemCode.substring(index, index+2), itemColors.get(j).getId().getColorCode());
								if(StringUtils.hasText(itemCodeList)){
									itemCodeList = itemCodeList + ", '" +itemCodeOtherColor+"'";
								}else{
									itemCodeList = "'"+itemCodeOtherColor+"'";
								}
							}
							break;
						}
					}
					log.info("itemCodeList:"+itemCodeList);
					findObjs.put("itemCodeList", itemCodeList);*/
//					findObjs.put("startWarehouseCode", warehouseCode);
//					findObjs.put("endWarehouseCode", warehouseCode);
				}else if("bySeries".equals(searchWay)){
					log.info("bySeries");
					findObjs.put("category13", category13);
					findObjs.put("startWarehouseCode", warehouseCode);
					findObjs.put("endWarehouseCode", warehouseCode);
				}else{
					log.info("byOther");
					findObjs.put("startItemCode", itemCode);
//					findObjs.put("endItemCode", itemCode);
//					findObjs.put("warehouseCodeList", warehouseCodeList);
				}
				if(StringUtils.hasText(category01Advance)){
					findObjs.put("category01", category01Advance);
				}
				if(StringUtils.hasText(category02Advance)){
					findObjs.put("category02", category02Advance);
				}
				if(StringUtils.hasText(category03Advance)){
					findObjs.put("category03", category03Advance);
				}
			}else{
				findObjs.put("itemCName", itemCName);
				findObjs.put("category01", category01);
				findObjs.put("category02", category02);
				findObjs.put("category03", category03);
				findObjs.put("category13", category13);
			}
		    
			HashMap map = new HashMap();
//			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			if("Y".equals(isShowOwn)){
				findObjs.put("startWarehouseCode", warehouseCode);
			}else{
				findObjs.put("warehouseCodeList", warehouseCodeList);
			}
			/*findObjs.put("startItemCode", itemCode);
			findObjs.put("endItemCode", itemCode);
*/
//			findObjs.put("category01", category01);
//			findObjs.put("category02", category02);
//			findObjs.put("category03", category03);
//			findObjs.put("category13", category13);
//			findObjs.put("itemCodeList", itemCodeList);


			List<ImItemOnHandView> imItemOnHandViews = (List<ImItemOnHandView>) imItemOnHandViewDAO.findPageLine(findObjs,iSPage, iPSize, ImItemOnHandViewDAO.QUARY_TYPE_SELECT_RANGE).get("form");

			log.info("ImItemOnHandView.size" + imItemOnHandViews.size());

			if (imItemOnHandViews != null && imItemOnHandViews.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				log.info("firstIndex = " + firstIndex);	
				
				Long maxIndex = (Long) imItemOnHandViewDAO.findPageLine(findObjs, -1, -1,
						ImItemOnHandViewDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆INDEX
				log.info("maxIndex = " + maxIndex);
				
				log.info("ImItemOnHand.AjaxUtils.getAJAXPageData ");
				
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,
						imItemOnHandViews, gridDatas, firstIndex, maxIndex));
			} else {
				log.info("ImItemOnHand.AjaxUtils.getAJAXPageDataDefault ");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
			}
			log.info("finish");
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入頁面顯示的庫存查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的庫存查詢失敗！");
		}
	}

	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	
	public List<Properties> saveSalesSearchResult(Properties httpRequest) throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_SALES_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	
	public List<Properties> getAJAXSalesSearchPageData(Properties httpRequest) throws Exception {
		log.info("getAJAXSalesSearchPageData");
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================
			String itemCode = httpRequest.getProperty("itemCode");
			String itemName = httpRequest.getProperty("itemName");
			String customerCode = httpRequest.getProperty("customerCode");
			Date salesOrderBeginDate = DateUtils.parseDate( "yyyy/MM/dd", httpRequest.getProperty("salesOrderBeginDate"));
			Date salesOrderEndDate = DateUtils.parseDate( "yyyy/MM/dd", httpRequest.getProperty("salesOrderEndDate"));
//			String superintendentCode = httpRequest.getProperty("superintendentCode");
//			String customerPoNo = httpRequest.getProperty("customerPoNo");
			String brandCode = httpRequest.getProperty("brandCode");
			HashMap findObjs = new HashMap();
			log.info("itemCode:"+itemCode);
			log.info("itemName:"+itemName);
			log.info("customerCode:"+customerCode);
			log.info("salesOrderBeginDate:"+salesOrderBeginDate);
			log.info("salesOrderEndDate:"+salesOrderEndDate);;
			log.info("brandCode:"+brandCode);
			log.info("iSPage:"+iSPage);
			log.info("iPSize:"+iPSize);
			
			HashMap map = new HashMap();
			
			findObjs.put("itemCode",itemCode);
			findObjs.put("itemName",itemName);
			findObjs.put("customerCode",customerCode);
			findObjs.put("salesOrderBeginDate",salesOrderBeginDate);
			findObjs.put("salesOrderEndDate",salesOrderEndDate);
			findObjs.put("brandCode",brandCode);

			List<SoSalesOrderSimpleView> soSalesOrderSimpleViews = (List<SoSalesOrderSimpleView>) soSalesOrderDetailViewDAO.findPageLine(findObjs, 
																	iSPage, iPSize, SoSalesOrderDetailViewDAO.QUARY_TYPE_SELECT_RANGE).get("form");
			for(SoSalesOrderSimpleView soSalesOrderSimpleView : soSalesOrderSimpleViews){
				BuEmployeeWithAddressView buEmployeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, soSalesOrderSimpleView.getSuperintendentCode());
				log.info("soSalesOrderSimpleView.getSuperintendentCode() "+ soSalesOrderSimpleView.getSuperintendentCode());
				soSalesOrderSimpleView.setSuperintendentName(buEmployeeWithAddressView.getChineseName());
				BuCustomerWithAddressView buCustomerWithAddressView = buCustomerWithAddressViewDAO.findEnableCustomer(brandCode, soSalesOrderSimpleView.getCustomerCode());
				log.info("soSalesOrderSimpleView.getCustomerCode() "+ soSalesOrderSimpleView.getCustomerCode());
				log.info("buCustomerWithAddressView.getChineseName() "+ buCustomerWithAddressView.getChineseName());
				soSalesOrderSimpleView.setCustomerName(buCustomerWithAddressView.getChineseName());
				ImItem imItem = imItemDAO.findById(soSalesOrderSimpleView.getItemCode());
				log.info("soSalesOrderSimpleView.getItemCode() "+ soSalesOrderSimpleView.getItemCode());
				soSalesOrderSimpleView.setItemCName(imItem.getItemCName());
			}

			log.info("soSalesOrderDetailView.size" + soSalesOrderSimpleViews.size());

			if (soSalesOrderSimpleViews != null && soSalesOrderSimpleViews.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				log.info("firstIndex = " + firstIndex);	
				
				Long maxIndex = (Long) soSalesOrderDetailViewDAO.findPageLine(findObjs, -1, -1,	SoSalesOrderDetailViewDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆INDEX
				log.info("maxIndex = " + maxIndex);
				
				log.info("AjaxUtils.getAJAXPageData ");
				
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_SALES_FIELD_NAMES, GRID_SEARCH_SALES_FIELD_DEFAULT_VALUES,
						soSalesOrderSimpleViews, gridDatas, firstIndex, maxIndex));
			} else {
				log.info("AjaxUtils.getAJAXPageDataDefault ");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_SALES_FIELD_NAMES,
						GRID_SEARCH_SALES_FIELD_DEFAULT_VALUES, map, gridDatas));
			}
			log.info("finish");
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入頁面顯示的庫存查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的庫存查詢失敗！");
		}
	}
	
	public List<Properties> importMovePrint(Properties httpRequest) throws ValidationErrorException{
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		
		try{
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			String brandCode = httpRequest.getProperty("brandCode");
			
			final String COMMA_SIGN = ",";
			
			String ComNum = "";			//公司編號
			String CompanyName = "";	//公司名稱
			String WarehouseDeliveryName = "";	//轉出專櫃名稱
			String WarehouseArrivalName = "";	    //轉入專櫃名稱
			String Title = "";			//銷售單標題
			String OrderNo = "";
			String DeliveryDate = "";
			String ItemCount = "";
			
			
			String ItemID = "";			//品號
			String UnitPrice = "";		//單價
			String ItemPrices = "";		//售價
			String Quantity = "";		//數量
			String ItemNames = "";		//品名



			ImMovementHead head = (ImMovementHead) this.findMoveById(headId);
			if(null!=head){
				ImWarehouse imDeliveryWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, head.getDeliveryWarehouseCode(), "Y");
				ImWarehouse imArrivalWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, head.getArrivalWarehouseCode(), "Y");
				BuShopMachine buShopMachine = (BuShopMachine)buShopMachineDAO.findByBrandCodeAndMachineCode(brandCode, head.getDeliveryWarehouseCode());
				ComNum = "COM99";
				if("T1GS".equals(brandCode)){
					CompanyName = "如盈股份有限公司";
				}else{
					CompanyName = "采盟股份有限公司";
				}
				Title = "調撥單";
				OrderNo = head.getOrderNo();
				WarehouseDeliveryName = imDeliveryWarehouse.getWarehouseName();
				WarehouseArrivalName = imArrivalWarehouse.getWarehouseName();
				DeliveryDate = DateUtils.formatToTWDate(head.getDeliveryDate(), DateUtils.C_DATE_PATTON_DEFAULT);
				ItemCount = String.valueOf(head.getItemCount().intValue());

				log.info( DateUtils.formatToTWDate(head.getDeliveryDate(), DateUtils.C_DATE_PATTON_DEFAULT)+" "+DateUtils.format(head.getDeliveryDate(), DateUtils.C_TIME_PATTON_HHMMSS));

				
				
				
				List<ImMovementItem> imMovementItems = head.getImMovementItems();//銷售單身
				if(null!=imMovementItems || imMovementItems.size()!=0){
				
					for(ImMovementItem imMovementItem:imMovementItems){
						ImItem imItem = imItemService.findItem(brandCode, imMovementItem.getItemCode());
						ImItemCurrentPriceView price = imItemCurrentPriceViewDAO.findOneCurrentPrice(brandCode, "P",imMovementItem.getItemCode());
						if(imMovementItem.getIndexNo()>1){
							ItemID += COMMA_SIGN;
							ItemNames += COMMA_SIGN;
							UnitPrice += COMMA_SIGN;
							Quantity += COMMA_SIGN;
							ItemPrices += COMMA_SIGN;
						}
						ItemID += imMovementItem.getItemCode();
						ItemNames += imItem.getItemCName();
						
						ItemPrices += Math.round(price.getUnitPrice()*imMovementItem.getDeliveryQuantity());
//						if(imMovementItem.getDeliveryQuantity() > 1){
							Quantity += Long.toString(Math.round(imMovementItem.getDeliveryQuantity()));
							UnitPrice += Math.round(price.getUnitPrice());
//						}
					}
					
					log.info("PrinterId"+buShopMachine.getPrinterId());
					log.info("ComNum"+ComNum);
					log.info("CompanyName"+CompanyName);
					log.info("WarehouseArrivalName"+WarehouseArrivalName);
					log.info("WarehouseDeliveryName"+WarehouseDeliveryName);
					log.info("ItemCount"+ItemCount);
					log.info("OrderNo"+OrderNo);
					log.info("DeliveryDate"+DeliveryDate);
					log.info("Title"+Title);
					log.info("ItemID"+ItemID);
					log.info("UnitPrice"+UnitPrice);					
					log.info("ItemPrices"+ItemPrices);
					log.info("Quantity"  +Quantity);
					log.info("ItemNames"+ItemNames);
					
					properties.setProperty("PrinterId", buShopMachine.getPrinterId());
					properties.setProperty("ComNum", ComNum);
					properties.setProperty("CompanyName", CompanyName);
					properties.setProperty("WarehouseArrivalName", WarehouseArrivalName);
					properties.setProperty("WarehouseDeliveryName", WarehouseDeliveryName);
					properties.setProperty("ItemCount", ItemCount);
					properties.setProperty("DeliveryDate", DeliveryDate);
					properties.setProperty("OrderNo", OrderNo);
					properties.setProperty("Title", Title);
					properties.setProperty("ItemID", ItemID);
					properties.setProperty("UnitPrice", UnitPrice);					
					properties.setProperty("ItemPrices", ItemPrices);
					properties.setProperty("Quantity"  , Quantity);
					properties.setProperty("ItemNames", ItemNames);

					result.add(properties);
					return result;
				}
				else{
					throw new Exception(headId+"單據明細為空，查詢失敗");
				}
			}
			else{
				throw new Exception(headId+"單據查詢失敗");
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ValidationErrorException("列印表單失敗");
		}		
	}
	
	public ImMovementHead findMoveById(Long headId) throws Exception {
		try {
			ImMovementHead movesOrder = (ImMovementHead) imMovementHeadDAO.findByPrimaryKey(ImMovementHead.class, headId);
			return movesOrder;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢百貨調撥單主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢百貨調撥單主檔時發生錯誤，原因："+ ex.getMessage());
		}
	}
	
	public List<Properties> findMemberCondition(Properties httpRequest)throws Exception {
		log.info("findMemberCondition");
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		Double total = 0D;
		String attach = "N";
		
		String customerCode = (String)httpRequest.getProperty("customerCode");
		Long headId = NumberUtils.getLong((String)httpRequest.getProperty("headId"));
		SoDepartmentOrderHead head = soDepartmentOrderHeadDAO.findById(headId);
		
		if(!StringUtils.hasText(customerCode)){
			
			List<SoDepartmentOrderItem> items = head.getSoDepartmentOrderItem();
			for(int i = 0; i < items.size(); i++){
				if("T1BS".equals(head.getBrandCode())){
					if(items.get(i).getDiscountRate() >= 70){
						total = total + items.get(i).getActualSalesAmount();
					}
				}else{
					total = total + items.get(i).getActualSalesAmount();
				}
			}
			log.info("總金額："+total);
			if(total >= 8000){
				attach = "Y";
			}else{
				attach = "N";
			}
		}
		properties.setProperty("attach", attach);

		result.add(properties);

		return result;
	}
	
	public List<Properties> executeBirthdayCoupon(Properties httpRequest)throws Exception {
		log.info("excuteBirthdayCoupon");
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		Date date = new Date();
//		Date date = new Date(118, 11, 20);
		
		try{
			Long headId = NumberUtils.getLong((String)httpRequest.getProperty("headId"));
			String brandCode = (String)httpRequest.getProperty("brandCode");
			String customerCode = (String)httpRequest.getProperty("customerCode");
			String superintendentCode = (String)httpRequest.getProperty("superintendentCode");
			String shopCode = (String)httpRequest.getProperty("shopCode");
			String currentYear = DateUtils.format(date, "yyyy");
			String currentMonth = String.valueOf(date.getMonth()+1);
			Double zz68value = 0D;
			Boolean isDiscount = false;
			String discount = "";
			String useYear = "";
			
			BuCustomerWithAddressView buCustomerWithAddressView = buCustomerWithAddressViewDAO.findEnableCustomer(brandCode, customerCode);
			Long birthdayYear = buCustomerWithAddressView.getBirthdayYear();
			Long birthdayMonth = buCustomerWithAddressView.getBirthdayMonth();
			Long birthdayDay = buCustomerWithAddressView.getBirthdayDay();
			int buffer = 1;
			
			if("T1GS".equals(brandCode)){
				buffer = 1;
				zz68value = 0D;
				isDiscount = true;
			}else if("T1BS".equals(brandCode)){
				buffer = 0;
				zz68value = 500D;
				isDiscount = false;
			}
			
			
			if(birthdayMonth == 1){
				useYear = String.valueOf(Long.valueOf(currentYear)+1);
			}else if(birthdayMonth == 12){
				useYear = String.valueOf(Long.valueOf(currentYear)-1);
			}else{
				useYear = currentYear;
			}
			
			Date beginDate = DateUtils.getFirstDateOfMonth(DateUtils.parseDate(useYear+"-"+birthdayMonth+"-"+birthdayDay),buffer*-1);
			Date endDate = DateUtils.getLastDateOfMonth(DateUtils.addMonths(DateUtils.parseDate(useYear+"-"+birthdayMonth+"-"+birthdayDay),buffer));
			log.info("beginDate： "+beginDate);
			log.info("endDate： "+endDate);
			
			log.info(Long.valueOf(currentMonth));
			log.info(buCustomerWithAddressView.getBirthdayMonth());
			log.info(birthdayMonth.compareTo(Long.valueOf(currentMonth)));
			BuCustomerCouponRecord buCustomerCouponRecord = buCustomerCouponRecordDAO.findByCustomerCodeAndYear(customerCode, useYear, "Birthday");
//			if(birthdayMonth.compareTo(Long.valueOf(currentMonth)) > -1){
			if(date.compareTo(beginDate) >= 0 && date.compareTo(DateUtils.addDays(endDate, 1)) == -1){
				if(buCustomerCouponRecord != null){
					BuShop buShop = buShopDAO.findById(buCustomerCouponRecord.getShopCode());
					
					throw new ValidationErrorException(useYear + "年度生日券已在 "+DateUtils.format(buCustomerCouponRecord.getUseDate(),DateUtils.C_DATE_PATTON_SLASH)+" 於 "+buShop.getShopCName()+" 被使用");
				}
			}else{
				throw new ValidationErrorException("會員不在可使用日期範圍內");
			}
			
			
			SoDepartmentOrderHead head = soDepartmentOrderHeadDAO.findById(headId);
			List<SoDepartmentOrderItem> items = head.getSoDepartmentOrderItem();
			
			if(isDiscount){
//				for(SoDepartmentOrderItem item : items){
//					if(item.getDiscountRate() >= 80){
//						item.setDiscountRate(80D);
//						item.setActualSalesAmount(item.getOriginalUnitPrice()*item.getQuantity()*(item.getDiscountRate()/100));
//					}
//				}
				discount = "80";
			}
			
			ImItem imItem = imItemDAO.findById("ZZ68"+brandCode);
//			SoDepartmentOrderItem zz68Item = new SoDepartmentOrderItem();
//			zz68Item.setItemCode(imItem.getItemCode());
//			zz68Item.setItemCName(imItem.getItemCName());
//			zz68Item.setOriginalUnitPrice(zz68value);
//			zz68Item.setQuantity(1D);
//			zz68Item.setActualSalesAmount(zz68Item.getOriginalUnitPrice()*zz68Item.getQuantity());
//			items.add(zz68Item);
//			soDepartmentOrderHeadDAO.update(head);

//			properties.setProperty("zz68value", zz68value);
//			properties.setProperty("isDiscount", isDiscount);
			properties.setProperty("itemCode", imItem.getItemCode());
			properties.setProperty("originalUnitPrice", zz68value.toString());
			properties.setProperty("allDiscount", discount);	
			properties.setProperty("useYear", useYear);
			result.add(properties);
		}catch(Exception e){
			e.printStackTrace();
			properties.setProperty("errorMsg", e.getMessage());
			
		}
		result.add(properties);
		return result;
	}
	
	public void updateCouponRecord(SoDepartmentOrderHead head, String useYear){
		String brandCode = head.getBrandCode();
		String customerCode = head.getCustomerCode();
		String superintendentCode = head.getSuperintendentCode();
		String shopCode = head.getShopCode();
		String zz68 = "ZZ68"+brandCode;
		List<SoDepartmentOrderItem> items = head.getSoDepartmentOrderItem();
		
		for(int i = 0; i < items.size(); i++){
			if(zz68.equals(items.get(i).getItemCode())){
				BuCustomerCouponRecord buCustomerCouponRecordNew = new BuCustomerCouponRecord();
				buCustomerCouponRecordNew.setCustomerCode(customerCode);
				buCustomerCouponRecordNew.setInCharge(superintendentCode);
				buCustomerCouponRecordNew.setType("Birthday");
				buCustomerCouponRecordNew.setUseDate(new Date());
				buCustomerCouponRecordNew.setShopCode(shopCode);
				buCustomerCouponRecordNew.setSalesOrderId(head.getSalesoOrderId());
				buCustomerCouponRecordNew.setUseYear(useYear);
				buCustomerCouponRecordNew.setStatus(OrderStatus.FINISH);
				buCustomerCouponRecordDAO.save(buCustomerCouponRecordNew);
			}
		}
	}
	
	private void voidCoupon(SoSalesOrderHead soSalesOrderHead){
		BuCustomerCouponRecord buCustomerCouponRecord = buCustomerCouponRecordDAO.findBySalesOrderId(soSalesOrderHead.getHeadId());
		if(buCustomerCouponRecord != null){
			buCustomerCouponRecord.setStatus(OrderStatus.VOID);
			buCustomerCouponRecordDAO.update(buCustomerCouponRecord);
		}
	}
	
	
}
