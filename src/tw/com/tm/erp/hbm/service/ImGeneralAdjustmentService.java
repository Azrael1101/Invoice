package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.saxon.instruct.ValueOf;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationItemDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationLogDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceHeadDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceLineDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemEanPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveItemDAO;
import tw.com.tm.erp.hbm.dao.ImTransationDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseEmployeeDAO;
import tw.com.tm.erp.hbm.dao.IslandExportDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.BuShopEmployee;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHand;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceLine;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImItemEanPriceView;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.ImTransation;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

//for 儲位用
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.action.ImStorageAction;

public class ImGeneralAdjustmentService {

    private static final Log log = LogFactory.getLog(ImGeneralAdjustmentService.class);
    public static final String PROGRAM_ID= "IM_GENERAL_ADJUSTMENT";

    public static final String NO_SIGNGING = "noSigning";
    public static final String HAS_SIGNGING = "hasSigning";

    public static final String CREATOR = "CREATOR"; // 填單人
    public static final String FINANCE = "FINANCE"; // 船務主管
    public static final String ACCOUNT = "ACCOUNT"; // 會計
    public static final String FINANCEMGR = "FINANCEMGR"; // 會計主管

    public static final String STOCK = "stock";
    public static final String STOCK_INVOICE_REJECT_ARCHIVE_DECL = "stockInvoiceRejectArchiveDecl";
//    public static final String STOCK_REJECT_ARCHIVE = "stockRejectArchive";
//    public static final String STOCK_DECL = "stockDecl";

    public static final int PROFIT = 11;		// 盤盈
    public static final int LOSS = 12;			// 盤虧
    public static final int DISCARD = 41;		// 報廢
    public static final int F2P = 51;			// 保稅轉完稅
    public static final int ERASE_ACCOUNT = 61;		// 領用除帳
    public static final int ADJUST_COST = 81;		// 調成本
    public static final int OUT = 21;			// 出倉(離島)
    public static final int IN = 22;			// 入倉(離島)
    public static final int RES = 0;			// 重整
    public static final int NEGATIVE_DECLARATION = 99;	// 負報單
    public static final String RES_TYPE_BEFORE = "1";			// 重整前
    public static final String RES_TYPE_AEFTER = "2";			// 重整後

    public static final String TAX_TYPE_F = "F"; // 保稅

    public static final String T2 = "T2"; // 免稅

    /**
     * spring Ioc
     */
    private BaseDAO baseDAO;

    private ImAdjustmentHeadDAO imAdjustmentHeadDAO;
    private ImAdjustmentLineDAO imAdjustmentLineDAO;
    private CmDeclarationHeadDAO cmDeclarationHeadDAO;
    private CmDeclarationItemDAO cmDeclarationItemDAO;
    private FiInvoiceHeadDAO fiInvoiceHeadDAO;
    private FiInvoiceLineDAO fiInvoiceLineDAO;
    private ImWarehouseEmployeeDAO imWarehouseEmployeeDAO;
    private ImReceiveHeadDAO imReceiveHeadDAO;
    private ImWarehouseDAO imWarehouseDAO;
    private ImItemDAO imItemDAO;
    private ImItemPriceDAO imItemPriceDAO;
    private ImItemEanPriceViewDAO imItemEanPriceViewDAO;
    private ImTransationDAO imTransationDAO;
    private NativeQueryDAO nativeQueryDAO;
    private BuBrandDAO buBrandDAO;
    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    private BuOrderTypeDAO buOrderTypeDAO;
    private ImOnHandDAO imOnHandDAO;
    private CmDeclarationOnHandDAO cmDeclarationOnHandDAO;
    private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;

    private BuOrderTypeService buOrderTypeService;
    private BuBrandService buBrandService;
    private BuCommonPhraseService buCommonPhraseService;
    private ImAdjustmentLineService imAdjustmentLineService;
    private SiProgramLogAction siProgramLogAction;
    
	//for 儲位用
	private ImStorageAction				 imStorageAction;
	private ImStorageService			 imStorageService;

    public void setBuCommonPhraseLineDAO(
    		BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
    	this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    }

    public void setImItemCurrentPriceViewDAO(
    		ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
    	this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
    }

    public void setImAdjustmentLineService(
    		ImAdjustmentLineService imAdjustmentLineService) {
    	this.imAdjustmentLineService = imAdjustmentLineService;
    }

    public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
    	this.nativeQueryDAO = nativeQueryDAO;
    }

    public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
    	this.buCommonPhraseService = buCommonPhraseService;
    }

    public void setBaseDAO(BaseDAO baseDAO) {
    	this.baseDAO = baseDAO;
    }

    public void setImAdjustmentHeadDAO(ImAdjustmentHeadDAO imAdjustmentHeadDAO) {
    	this.imAdjustmentHeadDAO = imAdjustmentHeadDAO;
    }
    
    public void setImAdjustmentLineDAO(ImAdjustmentLineDAO imAdjustmentLineDAO) {
    	this.imAdjustmentLineDAO = imAdjustmentLineDAO;
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
    
    public void setImWarehouseEmployeeDAO(ImWarehouseEmployeeDAO imWarehouseEmployeeDAO) {
    	this.imWarehouseEmployeeDAO = imWarehouseEmployeeDAO;
    }
    
    public void setImReceiveHeadDAO(ImReceiveHeadDAO imReceiveHeadDAO) {
    	this.imReceiveHeadDAO = imReceiveHeadDAO;
    }

    public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
    	this.imWarehouseDAO = imWarehouseDAO;
    }
    
    public void setImItemDAO(ImItemDAO imItemDAO) {
    	this.imItemDAO = imItemDAO;
    }
    
    public void setImItemPriceDAO(ImItemPriceDAO imItemPriceDAO) {
    	this.imItemPriceDAO = imItemPriceDAO;
    }

    public void setImItemEanPriceViewDAO(ImItemEanPriceViewDAO imItemEanPriceViewDAO) {
    	this.imItemEanPriceViewDAO = imItemEanPriceViewDAO;
    }
    
    public void setImTransationDAO(ImTransationDAO imTransationDAO) {
    	this.imTransationDAO = imTransationDAO;
    }
    
    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
    	this.buBrandDAO = buBrandDAO;
    }
    
    public void setBuOrderTypeDAO(BuOrderTypeDAO buOrderTypeDAO) {
    	this.buOrderTypeDAO = buOrderTypeDAO;
    }
    
    public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
    	this.imOnHandDAO = imOnHandDAO;
    }
    
    public void setCmDeclarationOnHandDAO(
    		CmDeclarationOnHandDAO cmDeclarationOnHandDAO) {
    	this.cmDeclarationOnHandDAO = cmDeclarationOnHandDAO;
    }
    
    public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
    	this.buOrderTypeService = buOrderTypeService;
    }

    public void setBuBrandService(BuBrandService buBrandService) {
    	this.buBrandService = buBrandService;
    }
    
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
    	this.siProgramLogAction = siProgramLogAction;
    }
    
	//for 儲位用
	public void setImStorageAction(ImStorageAction imStorageAction) {
		this.imStorageAction = imStorageAction;
	}
	
	public void setImStorageService(ImStorageService imStorageService) {
		this.imStorageService = imStorageService;
	}

    /**
     * 調整單明細欄位
     */
    public static final String[] GRID_FIELD_NAMES = {
	"indexNo", 		"itemCode" ,	 	"reserve4",
	"itemCName",		"lotNo", 			"localUnitCost","unitCost",
	"amount", 			"warehouseCode",   	"difQuantity",
	"originalDeclarationNo", "reserve5",	"originalDeclarationSeq", "originalDeclarationDate",
	"boxNo", 			"weight",			"customsItemCode",
	"lineId", "isLockRecord", "isDeleteRecord", "message"
    };
    /**
     * 拆併單明細欄位
     */
    public static final String[] COMPOSE_GRID_FIELD_NAMES = {
    	"indexNo", 		"itemCode" ,	 	"reserve4",
    	"itemCName",		"lotNo", 			"localUnitCost","unitCost",
    	"amount", 			"warehouseCode",   	"difQuantity",
    	"originalDeclarationNo", "reserve5",	"originalDeclarationSeq", "originalDeclarationDate",
    	"boxNo", 			"weight",			"customsItemCode",
    	"lineId", "isLockRecord", "isDeleteRecord", "message"
        };
    
    public static final String[] GRID_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "", "0.0","0.0",
	"0.0", "", "0",
	"", "", "", "",
	"", "0.0", "",
	"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };
    
    public static final String[] COMPOSE_GRID_FIELD_DEFAULT_VALUES = {
    	"", "", "",
    	"", "", "0.0","0.0",
    	"0.0", "", "0",
    	"", "", "", "",
    	"", "0.0", "",
    	"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
        };
    
    
    public static final int[] GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_DATE,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };
    
    public static final int[] COMPOSE_GRID_FIELD_TYPES = {
    	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,AjaxUtils.FIELD_TYPE_DOUBLE,
    	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_DATE,
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
    	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
        };
    /**
     * AIR調整單明細欄位
     */
    public static final String[] AIR_GRID_FIELD_NAMES = {
    "indexNo", 		"itemCode" ,		"lotNo",	 	"warehouseCode",
    "difQuantity",		"amount", 			"accountCode",
    "checkStatus", 			"reason"
    };

    public static final String[] AIR_GRID_FIELD_DEFAULT_VALUES = {
    "", "", "000000000000", "",
    "0.0", "0.0", "",
    "", ""
    };

    public static final int[] AIR_GRID_FIELD_TYPES = {
    AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
    AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };

	/**
     * MEG調整單明細欄位
     */
    public static final String[] MEG_GRID_FIELD_NAMES = {
	"indexNo", 		"itemCode" ,	 	"reserve4",
	"itemCName",		"lotNo", 			"localUnitCost",
	"amount", 			"warehouseCode",   	"difQuantity",
	"originalDeclarationNo", "reserve5",	"originalDeclarationSeq", "originalDeclarationDate",
	"boxNo", 			"weight",			"customsItemCode",
	"specDesc", "unit",	"orgMoveWhNo", "orgMoveWhItemNo",
	"lineId", "isLockRecord", "isDeleteRecord", "message"
    };

    public static final String[] MEG_GRID_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "", "0.0",
	"0.0", "", "0",
	"", "", "", "",
	"", "0.0", "",
	"", "", "", "",
	"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };

    public static final int[] MEG_GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_DATE,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };

    /**
     * 調整單查詢picker用的欄位
     */
    public static final String[] GRID_SEARCH_FIELD_NAMES = {
	"orderNo", "adjustmentDate", "declarationNo",
	"statusName", "lastUpdateDate", "orderTypeCode",
	"headId"
    };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "", "",
	""
    };

    /**
     * 拆/併/貨調整單明細欄位
     */
    
    public static final String[] GRID_FIELD_APART_NAMES = {
	"indexNo", "beAft"	,	"itemCode" , "reserve4",
	"itemCName", 		"warehouseCode",	"reserve5",
	"warehouseName", 	"lotNo",			"difQuantity",
	"localUnitCost",    "amount",	"sourceItemCode",
	"originalDeclarationNo","reserve3", "originalDeclarationSeq", "itemNo", "unit", "cigarWineMark",
	"lineId", "isLockRecord", "isDeleteRecord", "message","specDesc"
    
    };

    public static final String[] GRID_FIELD_APART_DEFAULT_VALUES = {
	"", "", "", "",
	"", "", "",
	"", "", "0.0",
	"", "", "",
	"", "", "", "", "","",
	"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "",""
    };

    public static final int[] GRID_FIELD_APART_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    
    };

    /**
     * 拆/併/貨調整單查詢picker用的欄位
     */
    public static final String[] GRID_SEARCH_APART_FIELD_NAMES = {
	"sourceOrderTypeCode", "orderNo", "adjustmentDate",
	"statusName", "lastUpdateDate", "headId"
    };

    public static final String[] GRID_SEARCH_APART_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "", ""
    };

    /**
     * 1.將相同的商品、庫別、批號集合
     * 2.將相同的報單號碼、報單項次、商品、關別集合
     * @param imAdjustmentHead
     * @param employeeCode
     * @return
     * @throws FormException
     * @throws Exception
     */
    private Set[] aggregateOrderItemsQty(ImAdjustmentHead imAdjustmentHead, String employeeCode, boolean isReverse) throws FormException, Exception{
    	log.info("Reset - 扣庫項目計算");
		StringBuffer key = new StringBuffer();
		StringBuffer cmKey = new StringBuffer();
		HashMap imMap = new HashMap();
		HashMap cmMap = new HashMap();
	
		String brandCode = imAdjustmentHead.getBrandCode();
		String taxType = imAdjustmentHead.getTaxType();
	
		int adjustmentType = NumberUtils.getInt(null == imAdjustmentHead.getAdjustmentType()? "0":imAdjustmentHead.getAdjustmentType());
	
		List<ImAdjustmentLine> imAdjustmentLines = imAdjustmentHead.getImAdjustmentLines();
		//log.info( "imAdjustmentLines.size() = " + imAdjustmentLines.size() );
		for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
		    imAdjustmentLine.setLastUpdateDate( new Date() );
		    imAdjustmentLine.setLastUpdatedBy( employeeCode );
	
		    Double quantity = imAdjustmentLine.getDifQuantity();
		    String warehouseCode = imAdjustmentLine.getWarehouseCode();
		    String resType = "";
		    if("BEF".equals(imAdjustmentHead.getOrderTypeCode())){resType = imAdjustmentLine.getBeAft();} 
	
		    String declarationNo = null;
		    Long declarationSeq = null;
		    if( IN == adjustmentType ){
			
		    	if (imAdjustmentHead.getOrderTypeCode().equals("MDF") || imAdjustmentHead.getOrderTypeCode().equals("MDP")){
		    		
		    		declarationNo = imAdjustmentLine.getOriginalDeclarationNo(); // 單身的報單
		    		declarationSeq = imAdjustmentLine.getOriginalDeclarationSeq(); // 單身的報單項次
				
		    	}else{
		    		declarationNo = imAdjustmentHead.getDeclarationNo(); // 單頭的報單
		    		declarationSeq = imAdjustmentLine.getIndexNo(); // 單身的indexNo
		    	}
		    }else{
				declarationNo = imAdjustmentLine.getOriginalDeclarationNo(); // 單身的報單
				declarationSeq = imAdjustmentLine.getOriginalDeclarationSeq(); // 單身的報單項次
		    }
	
		    String customsItemCode = imAdjustmentLine.getItemCode();
		    String lotNo = imAdjustmentLine.getLotNo();
	
		    //數量不可為null
		    if(quantity == null){
				quantity = 0D;
				imAdjustmentLine.setDifQuantity(quantity);
		    }
	
		    if(isReverse){
		    	quantity = quantity * -1;
		    }
	
		    log.info( "imAdjustmentLine.quantity = " + quantity );
		    log.info( "imAdjustmentLine.getOriginalDeclarationNo = " + declarationNo );
		    log.info( "imAdjustmentLine.getOriginalDeclarationSeq = " + declarationSeq );
		    log.info( "imAdjustmentLine.getItemCode = " + customsItemCode );
		    log.info( "imAdjustmentLine.getWarehouseCode = " + warehouseCode );
	
		    // 保稅商品集合
		    if("F".equals(taxType) ){
				if( (!(quantity == 0D && ADJUST_COST == adjustmentType)) || // 非調成本且數量0的 OR
						(resType == RES_TYPE_BEFORE && RES == adjustmentType) || // 重整前的
						(resType == RES_TYPE_AEFTER && RES == adjustmentType && isReverse) ){ // 重整後的
					log.info( "RESET - F & 計算扣庫存項目 - CM");
				    ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, warehouseCode, "Y");
				    String customsWarehouseCode = null;
				    if( imWarehouse != null ){
						// 入庫倉屬於的關 cmMoveWarehouseTo
						customsWarehouseCode = imWarehouse.getCustomsWarehouseCode();
						if(!StringUtils.hasText(warehouseCode)){
						    throw new ValidationErrorException("庫別(" + warehouseCode + ")的海關關別未設定！");
						}
				    }else{
				    	throw new NoSuchObjectException("依據品牌(" + brandCode + ")、庫別(" + warehouseCode + ")查無庫別相關資料！");
				    }
		
				    cmKey.delete(0, cmKey.length());
				    cmKey.append(declarationNo).append("{$}");
				    cmKey.append(declarationSeq).append("{$}");
				    cmKey.append(customsItemCode).append("{$}");
				    cmKey.append(customsWarehouseCode);
				    if (cmMap.get(cmKey.toString()) == null) {
					cmMap.put(cmKey.toString(), quantity);
				    } else {
					cmMap.put(cmKey.toString(), quantity + ((Double) cmMap.get(cmKey.toString())));
				    }
				}
		    }
	
		    // 
		    if( (NEGATIVE_DECLARATION != adjustmentType && resType != RES_TYPE_AEFTER) || //負報單跟重整後不做
		    		(resType == RES_TYPE_AEFTER && RES == adjustmentType && isReverse) ){ //重整後報單庫存
		    	log.info( "RESET - 計算扣庫存項目-IM");
				key.delete(0, key.length());
				key.append(customsItemCode).append("{$}");
				key.append(warehouseCode).append("{$}");
				key.append(lotNo);
				if (imMap.get(key.toString()) == null) {
				    imMap.put(key.toString(), quantity);
				} else {
				    imMap.put(key.toString(), quantity + ((Double) imMap.get(key.toString())));
				}
		    }
		}
		return new Set[]{imMap.entrySet(), cmMap.entrySet()};
    }

    /**
     * 把相同的報關品項 合併起來確認庫存以及展發票
     *
     * @param entityBeans
     * @param opUser
     * @throws Exception
     */
    private Set aggregateDeclarationItemQuantity ( List<ImAdjustmentLine> imAdjustmentLines ){
	StringBuffer key = new StringBuffer();
	HashMap map = new HashMap();
	for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
	    String itemCode = imAdjustmentLine.getItemCode();
	    Double difQuantity = imAdjustmentLine.getDifQuantity();
	    String originalDeclarationNo = imAdjustmentLine.getOriginalDeclarationNo();
	    Long originalDeclarationSeq = imAdjustmentLine.getOriginalDeclarationSeq();
	    //數量不可為null
	    if(difQuantity == null){
		difQuantity = 0D;
		imAdjustmentLine.setDifQuantity(difQuantity);
	    }
	    if(originalDeclarationSeq == null){
		originalDeclarationSeq = 0L;
	    }
	    key.delete(0, key.length());
	    key.append(itemCode + "#");
	    key.append(originalDeclarationNo + "#");
	    key.append(originalDeclarationSeq);

	    if (map.get(key.toString()) == null) {
		map.put(key.toString(), difQuantity);
	    } else {
		map.put(key.toString(), difQuantity + ((Double) map.get(key.toString())));
	    }
	}
	return map.entrySet();
    }

    /**
     * 檢核調整單主檔,明細檔
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List checkedImGeneralAdjustment(Map parameterMap)throws FormException, Exception{
	List errorMsgs = new ArrayList(0);
	String message = null;
	String identification = null;
	ImAdjustmentHead imAdjustmentHead = null;
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");

	    imAdjustmentHead = this.getActualImAdjustment(formLinkBean);

	    String status = imAdjustmentHead.getStatus();
	    int adjustmentType = NumberUtils.getInt(imAdjustmentHead.getAdjustmentType());
	    String taxType = imAdjustmentHead.getTaxType();

	    if ( (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)) || ((adjustmentType == DISCARD || adjustmentType == F2P || adjustmentType == OUT )&&
		    "F".equals(taxType) && OrderStatus.SIGNING.equals(status) && "true".equals(approvalResult) ) ) {

		identification = MessageStatus.getIdentification(imAdjustmentHead.getBrandCode(),
			imAdjustmentHead.getOrderTypeCode(), imAdjustmentHead.getOrderNo());

		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

		validateImGeneralAdjustment( imAdjustmentHead, PROGRAM_ID, identification, errorMsgs, parameterMap );
	    }

	}catch (Exception ex) {
	    message = "調整單檢核失敗，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, imAdjustmentHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
	return errorMsgs;
    }

    /**
     * 檢核拆/併貨調整單主檔,明細檔
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List checkedImApartAdjustment(Map parameterMap, ImAdjustmentHead imAdjustmentHead )throws FormException, Exception{
	List errorMsgs = new ArrayList(0);
	String message = null;
	String identification = null;
	try{

	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    String status = imAdjustmentHead.getStatus();

	    if ( (OrderStatus.SAVE.equals(status) ) ) {

		identification = MessageStatus.getIdentification(imAdjustmentHead.getBrandCode(),
			imAdjustmentHead.getOrderTypeCode(), imAdjustmentHead.getOrderNo());

		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

		this.validateImApartAdjustment( imAdjustmentHead, PROGRAM_ID, identification, errorMsgs, formLinkBean );
	    }

	}catch (Exception ex) {
	    message = "拆/併貨調整單檢核失敗，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, imAdjustmentHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
	return errorMsgs;
    }

    /**
     * 完成任務工作
     * @param assignmentId
     * @param approveResult
     * @return
     * @throws ProcessFailedException
     */
    public static Object[] completeAssignment(long assignmentId, boolean approveResult, String formAction) throws ProcessFailedException{

	try{
	    HashMap context = new HashMap();
	    context.put("approveResult", approveResult);
	    context.put("formAction", formAction);

	    return ProcessHandling.completeAssignment(assignmentId, context);
	}catch (Exception ex){
	    log.error("完成調整工作任務失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("完成調整工作任務失敗！"+ ex.getMessage());
	}
    }
    public static Object[] completeApartAssignment(long assignmentId, boolean approveResult, ImAdjustmentHead imAdjustmentHead) 
	throws ProcessFailedException{
try{           
    HashMap context = new HashMap();
    context.put("approveResult", approveResult);
    context.put("form",imAdjustmentHead);
    return ProcessHandling.completeAssignment(assignmentId, context);
}catch (Exception ex){
    log.error("完成 Invoice 工作任務失敗，原因：" + ex.toString());
    throw new ProcessFailedException("完成 Invoice 工作任務失敗！");
}
}

    /**
     * 刪除調整單明細
     * @param imReceiveHead
     */
    public void deleteImAdjustmentLine(ImAdjustmentHead imAdjustmentHead ){
	List<ImAdjustmentLine> imAdjustmentLines = imAdjustmentHead.getImAdjustmentLines();
	if(imAdjustmentLines != null && imAdjustmentLines.size() > 0){
	    for(int i = imAdjustmentLines.size() - 1; i >= 0; i--){
		ImAdjustmentLine imAdjustmentLine = imAdjustmentLines.get(i);
		imAdjustmentLines.remove(imAdjustmentLine);
		imAdjustmentLineDAO.delete(imAdjustmentLine);
	    }
	}
    }


    /**
     * 將調整單明細表 mark 為刪除的刪掉
     * @param head
     */
    private void deleteLine(ImAdjustmentHead head){

	List<ImAdjustmentLine> imAdjustmentLines = head.getImAdjustmentLines();
	if(imAdjustmentLines != null && imAdjustmentLines.size() > 0){
	    for(int i = imAdjustmentLines.size() - 1; i >= 0; i--){
		ImAdjustmentLine imAdjustmentLine = imAdjustmentLines.get(i);
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imAdjustmentLine.getIsDeleteRecord())){
			
		    imAdjustmentLines.remove(imAdjustmentLine);
//		    imAdjustmentLineDAO.delete(imAdjustmentLine);
		}
	    }
	}
	for (int i = 0; i < imAdjustmentLines.size(); i++) {
	    ImAdjustmentLine line = imAdjustmentLines.get(i);
	    line.setIndexNo(i+1L);
	}
    }

    /**
     * rollbck 庫存與 delete invoice
     * @param head
     * @param employeeCode
     */
    private void deleteStocksAndInvoice(ImAdjustmentHead head, String employeeCode)throws Exception {
	try {
	    // delete invoice
	    FiInvoiceHead fiInvoiceHead = (FiInvoiceHead)fiInvoiceHeadDAO.findByPrimaryKey(FiInvoiceHead.class, NumberUtils.getLong(head.getReserve1()));
	    fiInvoiceHeadDAO.delete(fiInvoiceHead);
	    head.setReserve1("");

	    // rollbck 庫存
	    List<ImAdjustmentLine> lines = head.getImAdjustmentLines();
	    for (ImAdjustmentLine imAdjustmentLine : lines) {
		this.deleteOneStock(imAdjustmentLine, head, employeeCode);
	    }
	} catch (Exception e) {
	    log.error("刪除invoice和rollback庫存時發生錯誤，原因：" + e.toString());
	    throw new Exception("刪除invoice和rollback庫存時發生錯誤，原因：" + e.getMessage());
	}
    }

    /**
     * rollback cm_on_hand 和 im_on_hand 庫存
     * @param imAdjustmentLine
     * @param imAdjustmentHead
     * @param employeeCode
     * @throws Exception
     */
    private void deleteOneStock(ImAdjustmentLine imAdjustmentLine, ImAdjustmentHead imAdjustmentHead, String employeeCode)
    throws Exception {
	log.info( "======<updateOneStock>=======");
	try{
	    String brandCode = imAdjustmentHead.getBrandCode();								// 共用 PK
//	    int adjustmentType = Integer.valueOf(imAdjustmentHead.getAdjustmentType());
	    String taxType = imAdjustmentHead.getTaxType();
	    Double difQuantity = imAdjustmentLine.getDifQuantity() * - 1;  								// 共用 數量

	    // 更新cm,im庫存

	    // im_on_hand 用
	    String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode); 	// 組織代碼 PK
	    String itemCode = imAdjustmentLine.getItemCode();  								// 商品 PK
	    String warehouseCode = imAdjustmentLine.getWarehouseCode(); 					// 倉庫 PK
	    String lotNo = imAdjustmentLine.getLotNo();										// 批號 PK

	    log.info( "organizationCode = " + organizationCode );
	    log.info( "itemCode = " + itemCode );
	    log.info( "warehouseCode = " + warehouseCode );
	    log.info( "lotNo = " + lotNo );
	    log.info( "difQuantity = " + difQuantity );

	    // cm_declaration_on_hand 用
	    String originalDeclarationNo = imAdjustmentLine.getOriginalDeclarationNo();		// 報單單 號 PK
	    Long originalDeclarationSeq = imAdjustmentLine.getOriginalDeclarationSeq(); 	// 報單項次 PK
	    String customsItemCode = imAdjustmentLine.getCustomsItemCode();					// 報單品項 PK
	    ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, warehouseCode, "Y");
	    // 倉庫屬於的關
	    String customsWarehouseCode = imWarehouse.getCustomsWarehouseCode(); 			// 關別PK

	    log.info( "originalDeclarationNo = " + originalDeclarationNo );
	    log.info( "originalDeclarationSeq = " + originalDeclarationSeq );
	    log.info( "customsItemCode = " + customsItemCode );
	    log.info( "customsWarehouseCode = " + customsWarehouseCode );

	    // cm_on_hand 回滾庫存
	    log.info("======<cm_decaration_on_hand>======");
	    if( "F".equals(taxType) ){
		cmDeclarationOnHandDAO.updateOtherUncommitQuantity( originalDeclarationNo, originalDeclarationSeq,
			customsItemCode, customsWarehouseCode, brandCode, difQuantity, employeeCode, null ); // modified by Weichun 2011.09.27
	    }
	    log.info("======<cm_decaration_on_hand/>======");

	    // im_on_hand 回滾庫存
	    log.info("======<im_on_hand>======");
	    imOnHandDAO.updateOtherUncommitQuantity( organizationCode, itemCode,
		    warehouseCode, lotNo, brandCode, difQuantity, employeeCode);
	    log.info("======<im_on_hand/>======");

	}catch (Exception ex) {
	    log.error("rollback庫存時發生錯誤，原因：" + ex.toString());
	    throw new Exception("rollback庫存時發生錯誤，原因：" + ex.getMessage());
	}
	log.info( "======<updateOneStock/>=======");
    }

    /**
     * 保稅
     * @param conditionMap
     */
    private void doTaxTypeIsF(Map conditionMap){
	String brandCode = (String)conditionMap.get("brandCode");
	String itemCode = (String)conditionMap.get("itemCode");
	String taxType = (String)conditionMap.get("taxType");
	String adjustmentType = (String)conditionMap.get("adjustmentType");
	String customsWarehouseCode = (String)conditionMap.get("customsWarehouseCode");
	String originalDeclarationNo = (String)conditionMap.get("originalDeclarationNo");
	String originalDeclarationSeq = (String)conditionMap.get("originalDeclarationSeq");
	Properties properties = (Properties)conditionMap.get("properties");

	Double currentOnHandQty = 0D;

	if( StringUtils.hasText(originalDeclarationNo) && StringUtils.hasText(originalDeclarationSeq) ){
	    CmDeclarationOnHand cmDeclarationOnHand = cmDeclarationOnHandDAO.getOnHandById(originalDeclarationNo, NumberUtils.getLong(originalDeclarationSeq), customsWarehouseCode, brandCode);
	    log.info( "cmDeclarationOnHand = " + cmDeclarationOnHand);

	    if( cmDeclarationOnHand != null ){
		String customsItemCode = cmDeclarationOnHand.getCustomsItemCode();

		if(StringUtils.hasText(itemCode)){
		    ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findOneCurrentPrice(brandCode, taxType, itemCode);
//		    ImItemEanPriceView imItemEanPriceView = imItemEanPriceViewDAO.getItemInfoByProperty(brandCode, itemCode, taxType);
		    if( imItemCurrentPriceView != null){
			properties.setProperty("itemCode", imItemCurrentPriceView.getItemCode());
			properties.setProperty("itemCName", imItemCurrentPriceView.getItemCName());
			properties.setProperty("localUnitCost", imItemCurrentPriceView.getUnitPrice().toString() );
			properties.setProperty("customsItemCode", itemCode );
		    }else{
			properties.setProperty("itemCode", itemCode);
			properties.setProperty("itemCName", "查報關單無此品號");
			properties.setProperty("localUnitCost", "0.0");
			properties.setProperty("customsItemCode", "" );
		    }
		}else{
		    ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findOneCurrentPrice(brandCode, taxType, itemCode);
//		    ImItemEanPriceView imItemEanPriceView = imItemEanPriceViewDAO.getItemInfoByProperty(brandCode, customsItemCode, taxType);
		    if( imItemCurrentPriceView != null){
			properties.setProperty("itemCode", customsItemCode);
			properties.setProperty("itemCName", imItemCurrentPriceView.getItemCName());
			properties.setProperty("localUnitCost", imItemCurrentPriceView.getUnitPrice().toString() );
			properties.setProperty("customsItemCode", customsItemCode );
		    }else{
			properties.setProperty("itemCode", customsItemCode);
			properties.setProperty("itemCName", "查報關單無此品號");
			properties.setProperty("localUnitCost", "0.0");
			properties.setProperty("customsItemCode", customsItemCode );
		    }

		}
	    }else{
		properties.setProperty("itemCode", itemCode);
		properties.setProperty("itemCName", "查無此報關單或項次");
		properties.setProperty("localUnitCost", "0.0");
		properties.setProperty("customsItemCode", "" );
	    }
	}else{
	    if(StringUtils.hasText(itemCode)){
		ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findOneCurrentPrice(brandCode, taxType, itemCode);
//		ImItemEanPriceView imItemEanPriceView = imItemEanPriceViewDAO.getItemInfoByProperty(brandCode, itemCode, taxType);
		if( imItemCurrentPriceView != null){
		    properties.setProperty("itemCode", itemCode);
		    properties.setProperty("itemCName", imItemCurrentPriceView.getItemCName());
		    properties.setProperty("localUnitCost", imItemCurrentPriceView.getUnitPrice().toString() );
		    properties.setProperty("customsItemCode", itemCode );
		}else{
		    properties.setProperty("itemCode", itemCode);
		    properties.setProperty("itemCName", "查無此品號");
		    properties.setProperty("localUnitCost", "0.0");
		    properties.setProperty("customsItemCode", "" );
		}
	    }else{
		properties.setProperty("itemCode", "");
		properties.setProperty("itemCName", "");
		properties.setProperty("localUnitCost", "0.0");
		properties.setProperty("customsItemCode", "" );
	    }
	}

	properties.setProperty("lotNo", SystemConfig.LOT_NO);
    }

    /**
     * 完稅
     * @param conditionMap
     */
    private void doTaxTypeIsP(Map conditionMap){
	String brandCode = (String)conditionMap.get("brandCode");
	String itemCode = (String)conditionMap.get("itemCode");
	String taxType = (String)conditionMap.get("taxType");
	//Double difQuantity = NumberUtils.getDouble((String)conditionMap.get("difQuantity"));
	//Double unitCost = NumberUtils.getDouble((String)conditionMap.get("unitCost"));	
	//String amount = "0.0";
	Properties properties = (Properties)conditionMap.get("properties");
	log.info("doTaxTypeIsP");
	if(StringUtils.hasText(itemCode)){
	    ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findOneCurrentPrice(brandCode, taxType, itemCode);
//	    ImItemEanPriceView imItemEanPriceView = imItemEanPriceViewDAO.getItemInfoByProperty(brandCode, itemCode, taxType);
	    if( imItemCurrentPriceView != null ){
		properties.setProperty("itemCode", imItemCurrentPriceView.getItemCode());
		properties.setProperty("itemCName", imItemCurrentPriceView.getItemCName());
		properties.setProperty("lotNo", SystemConfig.LOT_NO);
		//amount = NumberUtils.roundToStr(difQuantity * unitCost, 0);
		//properties.setProperty("amount", amount);
		properties.setProperty("unitCost",String.valueOf(imItemCurrentPriceView.getStandardPurchaseCost()));
		properties.setProperty("localUnitCost", String.valueOf(imItemCurrentPriceView.getUnitPrice()));
	    }else{
		properties.setProperty("itemCode", itemCode);
		properties.setProperty("itemCName", "查無此品號");
		properties.setProperty("lotNo", SystemConfig.LOT_NO);
		properties.setProperty("localUnitCost", "0.0");
	    }

	}else{
	    properties.setProperty("itemCode", itemCode);
	    properties.setProperty("itemCName", "查無此品號");
	    properties.setProperty("lotNo", SystemConfig.LOT_NO);
	    properties.setProperty("localUnitCost", "0.0");
	}
    }

    /**
     * 調整單初始化 bean 額外顯示欄位
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
    	Map resultMap = new HashMap(0);

    	try{
    		Object otherBean = parameterMap.get("vatBeanOther");
    		ImAdjustmentHead imAdjustmentHead = this.getActualHead(otherBean, resultMap);
    		String employeeName = UserUtils.getUsernameByEmployeeCode(imAdjustmentHead.getCreatedBy());

    		Map multiList = new HashMap(0);
    		resultMap.put("form", imAdjustmentHead);
    		resultMap.put("brandName",buBrandDAO.findById(imAdjustmentHead.getBrandCode()).getBrandName());
    		resultMap.put("statusName", OrderStatus.getChineseWord(imAdjustmentHead.getStatus()));
    		resultMap.put("createByName", employeeName);

    		String taxType = imAdjustmentHead.getTaxType();
    		String orderType = imAdjustmentHead.getOrderTypeCode();
    		String adjustmentType = imAdjustmentHead.getAdjustmentType();
    		String warehouseCode = imAdjustmentHead.getDefaultWarehouseCode();
    		String diffProcess = imAdjustmentHead.getDiffProcess();
    		String sourceOrderTypeCode = imAdjustmentHead.getSourceOrderTypeCode();
    		String checkStatus = imAdjustmentHead.getCheckStatus();
    		String ItemCheckStatus = null; ;
    		
    		log.info("adjustmentType = "+adjustmentType);
    		log.info("orderType = "+orderType);

    		List<BuCommonPhraseLine> allTaxTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"TaxCatalog", "Y"}, "indexNo" );
    		List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(imAdjustmentHead.getBrandCode() ,"IAJ");
    		String condition = "and id.buCommonPhraseHead.headCode = ? and enable = ? and attribute1 like ?";
    		List<BuCommonPhraseLine> allAdjustmentTypes = baseDAO.findByProperty("BuCommonPhraseLine", "", condition, new Object[]{"AdjustmentType", "Y", "%"+orderType+"%"}, " order by indexNo");
    		List<BuCommonPhraseLine> allDiffProcess = baseDAO.findByProperty("BuCommonPhraseLine", new String[] {"id.buCommonPhraseHead.headCode", "enable" },new Object[] { "ImAdjustmentDifferentProcess", "Y" }, "indexNo");
    		List<BuOrderType> allSourceOrderTypeCode = buOrderTypeDAO.findOrderbyType( imAdjustmentHead.getBrandCode(), "IMR" );
    		List<BuCommonPhraseLine> allCheckStatus = baseDAO.findByProperty("BuCommonPhraseLine", new String[] {"id.buCommonPhraseHead.headCode", "enable" },new Object[] { "ImAdjustmentCheckStatus", "Y" }, "indexNo");
    		List<BuCommonPhraseLine> allItemCheckStatus = baseDAO.findByProperty("BuCommonPhraseLine", new String[] {"id.buCommonPhraseHead.headCode", "enable" },new Object[] { "ImAdjustmentItemCheckStatus", "Y" }, "indexNo");
    		
    		
    		List<ImWarehouse> allWarehouseCodes = null;
    		if(imAdjustmentHead.getBrandCode().indexOf("T2") > -1 ){
    			String condition2 = "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? and attribute1 like ?";
    			if(StringUtils.hasText(adjustmentType)){
    				BuCommonPhraseLine buCommonPhraseLine = (BuCommonPhraseLine)baseDAO.findFirstByProperty("BuCommonPhraseLine", "", condition2, new Object[]{"AdjustmentType", adjustmentType, "Y", "%"+orderType+"%"}, " order by indexNo");
    				log.info("buCommonPhraseLine="+buCommonPhraseLine);
    				String warehouseCodes = null;
    				if("F".equals(taxType)){
    					warehouseCodes = buCommonPhraseLine.getAttribute2();
    				}else if("P".equals(taxType)){
    					warehouseCodes = buCommonPhraseLine.getAttribute3();
    				}
    				if(!StringUtils.hasText(warehouseCodes)){
    					allWarehouseCodes = imWarehouseEmployeeDAO.findByEmployeeCode(imAdjustmentHead.getBrandCode(), imAdjustmentHead.getCreatedBy());
    				}else{
    					allWarehouseCodes = imWarehouseEmployeeDAO.findByEmployeeCode(imAdjustmentHead.getBrandCode(), imAdjustmentHead.getCreatedBy(), warehouseCodes);
    				}
    			}else{
    				allWarehouseCodes = imWarehouseEmployeeDAO.findByEmployeeCode(imAdjustmentHead.getBrandCode(), imAdjustmentHead.getCreatedBy());
    			}
    		}else{
    			allWarehouseCodes = imWarehouseEmployeeDAO.findByEmployeeCode(imAdjustmentHead.getBrandCode(), imAdjustmentHead.getCreatedBy());
    		}

    		multiList.put("allItemCheckStatus"	, AjaxUtils.produceSelectorData(allItemCheckStatus, "lineCode", "name", true, true, ItemCheckStatus != null ? ItemCheckStatus : "" ));
    		multiList.put("allSourceOrderTypeCode"	, AjaxUtils.produceSelectorData(allSourceOrderTypeCode, "orderTypeCode", "name", true, true, sourceOrderTypeCode != null ? sourceOrderTypeCode : "" ));
    		multiList.put("allDiffProcess", AjaxUtils.produceSelectorData(allDiffProcess, "lineCode", "name", true, true, diffProcess!=null ? diffProcess : "" ));
    		multiList.put("allCheckStatus", AjaxUtils.produceSelectorData(allCheckStatus, "lineCode", "name", true, true, checkStatus!=null ? checkStatus : "" ));
    		multiList.put("allWarehouseCodes"	, AjaxUtils.produceSelectorData(allWarehouseCodes, "warehouseCode", "warehouseName", true, true, warehouseCode != null ? warehouseCode : "" ));
    		multiList.put("allTaxTypes"	, AjaxUtils.produceSelectorData(allTaxTypes, "lineCode", "name", true, true, taxType != null ? taxType : "" ));
    		multiList.put("allOrderTypes"	, AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, true, orderType != null ? orderType : "" ));
    		multiList.put("allAdjustmentTypes"	, AjaxUtils.produceSelectorData(allAdjustmentTypes, "lineCode", "name", true, true, adjustmentType != null ? adjustmentType : "" ));
    		
    		resultMap.put("multiList",multiList);

    		return resultMap;
    	}catch(Exception ex){
    		ex.printStackTrace();
    		log.error("調整單初始化失敗，原因：" + ex.toString());
    		throw new Exception("調整單初始化失敗，原因：" + ex.toString());

    	}
    }

    /**
     * 拆/併貨調整單初始化 bean 額外顯示欄位
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeApartInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);

	try{
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
//	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);

	    ImAdjustmentHead imAdjustmentHead = this.getActualHead(otherBean, resultMap);

	    Map multiList = new HashMap(0);
	    resultMap.put("form", imAdjustmentHead);
	    resultMap.put("brandName",buBrandDAO.findById(imAdjustmentHead.getBrandCode()).getBrandName());
	    resultMap.put("statusName", OrderStatus.getChineseWord(imAdjustmentHead.getStatus()));
	    resultMap.put("createByName", employeeName);
//	    resultMap.putAll( this.getOtherColumn(cmMovementHead) );

	    String orderType = imAdjustmentHead.getOrderTypeCode();

	    List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(imAdjustmentHead.getBrandCode() ,"IAJ");

	    multiList.put("allOrderTypes"	, AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, true, orderType != null ? orderType : "" ));

	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("拆/併貨調整單初始化失敗，原因：" + ex.toString());
	    throw new Exception("拆/併貨調整單初始化失敗，原因：" + ex.toString());

	}
    }

    /**
     * 離島調整單初始化 bean 額外顯示欄位
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeOffShoreIsIandInitial(Map parameterMap) throws Exception{
    	Map resultMap = new HashMap(0);

    	try{
    		Object otherBean = parameterMap.get("vatBeanOther");

    		String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    		String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

    		ImAdjustmentHead imAdjustmentHead = this.getActualHead(otherBean, resultMap);
    		//String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);
    		String employeeName = UserUtils.getUsernameByEmployeeCode(imAdjustmentHead.getCreatedBy());

    		imAdjustmentHead.setDistrict("2");
    		imAdjustmentHead.setTransport("N");

    		Map multiList = new HashMap(0);
    		resultMap.put("form", imAdjustmentHead);
    		resultMap.put("brandName",buBrandDAO.findById(imAdjustmentHead.getBrandCode()).getBrandName());
    		resultMap.put("statusName", OrderStatus.getChineseWord(imAdjustmentHead.getStatus()));
    		resultMap.put("createByName", employeeName);
//  		resultMap.putAll( this.getOtherColumn(cmMovementHead) );

    		String taxType = imAdjustmentHead.getTaxType();
    		String orderType = imAdjustmentHead.getOrderTypeCode();
    		String adjustmentType = imAdjustmentHead.getAdjustmentType();
    		String warehouseCode = imAdjustmentHead.getDefaultWarehouseCode();

    		log.info("adjustmentType = "+adjustmentType);
    		log.info("orderType = "+orderType);
    		List<BuCommonPhraseLine> allTaxTypes = buCommonPhraseService.getCommonPhraseLinesById("TaxCatalog", false);

    		List<BuCommonPhraseLine> allDistricts = buCommonPhraseService.getCommonPhraseLinesById("District", false);

    		List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(imAdjustmentHead.getBrandCode() ,"IAJ");
//  		String condition = "F".equals(taxType) ? "and id.buCommonPhraseHead.headCode = ? and enable = ? and attribute1 like ?" : "and id.buCommonPhraseHead.headCode = ? and enable = ? and attribute2 like ?";
    		String condition = "and id.buCommonPhraseHead.headCode = ? and enable = ? and attribute1 like ?";
    		List<BuCommonPhraseLine> allAdjustmentTypes = baseDAO.findByProperty("BuCommonPhraseLine", "", condition, new Object[]{"AdjustmentType", "Y", "%"+orderType+"%"}, " order by indexNo");

    		List<BuCommonPhraseLine> allDeclTypes = buCommonPhraseService.getCommonPhraseLinesById("DeclType", false);

    		List<ImWarehouse> allWarehouseCodes = null;
    		if( imAdjustmentHead.getBrandCode().indexOf("T2") > -1){
    			String condition2 = "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? and attribute1 like ?";
    			if(StringUtils.hasText(adjustmentType)){
    				BuCommonPhraseLine buCommonPhraseLine = (BuCommonPhraseLine)baseDAO.findFirstByProperty("BuCommonPhraseLine", "", condition2, new Object[]{"AdjustmentType", adjustmentType, "Y", "%"+orderType+"%"}, " order by indexNo");
    				log.info("buCommonPhraseLine="+buCommonPhraseLine);
    				String warehouseCodes = null;
    				if("F".equals(taxType)){
    					if("MEG".equals(orderType)){
    						warehouseCodes = buCommonPhraseLine.getAttribute4();
    					}else if("MDF".equals(orderType)){
    						warehouseCodes = buCommonPhraseLine.getParameter3();
    			    	}else{
    						warehouseCodes = buCommonPhraseLine.getAttribute2();
    					}
    				}else if("P".equals(taxType)){
    					if("MDP".equals(orderType)){
    			    		warehouseCodes = buCommonPhraseLine.getParameter3();
    			    	}else{
    			    		warehouseCodes = buCommonPhraseLine.getAttribute3();
    			    	}
    				}
    				if(!StringUtils.hasText(warehouseCodes)){
    					allWarehouseCodes = imWarehouseEmployeeDAO.findByEmployeeCode(imAdjustmentHead.getBrandCode(), imAdjustmentHead.getCreatedBy());
    				}else{
    					allWarehouseCodes = imWarehouseEmployeeDAO.findByEmployeeCode(imAdjustmentHead.getBrandCode(), imAdjustmentHead.getCreatedBy(), warehouseCodes);
    				}
    			}else{
    				allWarehouseCodes = imWarehouseEmployeeDAO.findByEmployeeCode(imAdjustmentHead.getBrandCode(), imAdjustmentHead.getCreatedBy());
    			}
    		}else{
    			allWarehouseCodes = imWarehouseEmployeeDAO.findByEmployeeCode(imAdjustmentHead.getBrandCode(), imAdjustmentHead.getCreatedBy());
    		}

    		multiList.put("allWarehouseCodes"	, AjaxUtils.produceSelectorData(allWarehouseCodes, "warehouseCode", "warehouseName", true, true, warehouseCode != null ? warehouseCode : "" ));

    		multiList.put("allDistricts"	, AjaxUtils.produceSelectorData(allDistricts, "lineCode", "name", true, false));
    		multiList.put("allTaxTypes"	, AjaxUtils.produceSelectorData(allTaxTypes, "lineCode", "name", true, true, taxType != null ? taxType : "" ));
    		multiList.put("allOrderTypes"	, AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, true, orderType != null ? orderType : "" ));
    		multiList.put("allSourceOrderTypeCodes"	, AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, true ));

    		multiList.put("allAdjustmentTypes"	, AjaxUtils.produceSelectorData(allAdjustmentTypes, "lineCode", "name", true, true, adjustmentType != null ? adjustmentType : "" ));

    		multiList.put("allDeclTypes"		, AjaxUtils.produceSelectorData(allDeclTypes, "lineCode", "name", false, true ));

    		resultMap.put("multiList",multiList);
    		
    		//for 儲位用
    		if(imStorageAction.isStorageExecute(imAdjustmentHead)){
    			//建立儲位單
    			Map storageMap = new HashMap();
    			storageMap.put("storageTransactionDate", "adjustmentDate");
    			storageMap.put("storageTransactionType", ImStorageService.ADJ);
    			storageMap.put("arrivalWarehouseCode", "defaultWarehouseCode");

    			ImStorageHead imStorageHead = imStorageAction.executeImStorageHead(storageMap, imAdjustmentHead);

    			resultMap.put("storageHeadId", imStorageHead.getStorageHeadId());
    			resultMap.put("beanHead", "ImAdjustmentHead");
    			resultMap.put("beanItem", "imAdjustmentLines");
    			resultMap.put("quantity", "difQuantity");
    			resultMap.put("storageTransactionType", ImStorageService.ADJ);
    			resultMap.put("storageStatus", "#F.status");
    			resultMap.put("arrivalWarehouseCode", "#F.defaultWarehouseCode");
    		}
    		
    		return resultMap;
    	}catch(Exception ex){
    		log.error("離島調整單初始化失敗，原因：" + ex.toString());
    		throw new Exception("離島調整單初始化失敗，原因：" + ex.toString());
    	}
    }

    /**
     * 查詢調整單初始化
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);

	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    String taxType = (String)PropertyUtils.getProperty(otherBean, "taxType");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
	    String adjustmentType  = (String)PropertyUtils.getProperty(otherBean, "adjustmentType");

	    List<BuOrderType> allOrderTypeCodes = buOrderTypeService.findOrderbyType(loginBrandCode ,"IAJ");
//	    String condition = "F".equals(taxType) ? "and id.buCommonPhraseHead.headCode = ? and enable = ? and attribute1 like ?" : "and id.buCommonPhraseHead.headCode = ? and enable = ? and attribute2 like ?";
	    String condition = "and id.buCommonPhraseHead.headCode = ? and enable = ? and attribute1 like ?";
	    List<BuCommonPhraseLine> allAdjustmentTypes = baseDAO.findByProperty("BuCommonPhraseLine", "", condition, new Object[]{"AdjustmentType", "Y", "%"+orderTypeCode+"%"}, " order by indexNo");
	    List<BuCommonPhraseLine> allTaxTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"TaxCatalog", "Y"}, "indexNo" );

	    Map multiList = new HashMap(0);
	    multiList.put("allOrderTypeCodes"	, AjaxUtils.produceSelectorData(allOrderTypeCodes, "orderTypeCode", "name", true, true, orderTypeCode != null ? orderTypeCode : "" ));
	    multiList.put("allAdjustmentTypes"	, AjaxUtils.produceSelectorData(allAdjustmentTypes, "lineCode", "name", true, true, adjustmentType!= null ? adjustmentType : "" ));
	    multiList.put("allTaxTypes"	, AjaxUtils.produceSelectorData(allTaxTypes, "lineCode", "name", true, true, taxType != null ? taxType : "" ));

	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("查詢調整單初始化失敗，原因：" + ex.toString());
	    throw new Exception("查詢調整單初始化失敗，原因：" + ex.toString());

	}
    }

    /**
     * 查詢拆/併貨調整單初始化
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeApartSearchInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);

	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");

	    List<BuOrderType> allOrderTypeCodes = buOrderTypeService.findOrderbyType(loginBrandCode ,"IAA");

	    Map multiList = new HashMap(0);
	    multiList.put("allOrderTypeCodes"	, AjaxUtils.produceSelectorData(allOrderTypeCodes, "orderTypeCode", "name", true, true, orderTypeCode != null ? orderTypeCode : "" ));

	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("查詢拆/併貨調整單初始化失敗，原因：" + ex.toString());
	    throw new Exception("查詢拆/併貨調整單初始化失敗，原因：" + ex.toString());
	}
    }

    /**
     * 執行反確認流程起始
     *
     * @param parameterMap
     * @return List<Properties>
     */
    public void executeReverterProcess(Object bean) throws Exception {
	ImAdjustmentHead head = (ImAdjustmentHead)bean;
	Map resultMap = new HashMap();
	getProcessConfig(head, resultMap);

	Object processObj[] = ImGeneralAdjustmentService.startProcess(head,resultMap);
	head.setProcessId((Long)processObj[0]);
	imAdjustmentHeadDAO.update(head);
    }

    /**
     * 日結處理程序(david)
     *
     * @param ImAdjustmentHead
     * @param opUser
     * @throws FormException
     * @throws NoSuchDataException
     */
    public void executeDailyBalance(ImAdjustmentHead ImAdjustmentHead, String opUser) throws FormException, NoSuchDataException {
	log.info("ImGeneralAdjustmentService.executeDailyBalance");
	String brandCode = ImAdjustmentHead.getBrandCode();
	String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);

	String orderTypeCode = ImAdjustmentHead.getOrderTypeCode();
	String orderNo = ImAdjustmentHead.getOrderNo();
	String identification = MessageStatus.getIdentificationMsg(brandCode, orderTypeCode, orderNo);
	String taxType = ImAdjustmentHead.getTaxType();
	int adjustmentType = NumberUtils.getInt(ImAdjustmentHead.getAdjustmentType());

	String customsWarehouseCode = null;
	if(TAX_TYPE_F.equals(taxType)){
	ImWarehouse imWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, ImAdjustmentHead.getDefaultWarehouseCode());
	if(null != imWarehouse){
	    customsWarehouseCode = StringUtils.hasText(imWarehouse.getCustomsWarehouseCode())?imWarehouse.getCustomsWarehouseCode():"";
	}
	}

	// 更新調整單Status為CLOSE
	ImAdjustmentHead.setStatus(OrderStatus.CLOSE);
	List<ImAdjustmentLine> ImAdjustmentLines = ImAdjustmentHead.getImAdjustmentLines();
	if (ImAdjustmentLines != null && ImAdjustmentLines.size() > 0) {
	    for (ImAdjustmentLine ImAdjustmentLine : ImAdjustmentLines) {
		imOnHandDAO.updateOtherOnHand(organizationCode, ImAdjustmentLine.getItemCode(), ImAdjustmentLine.getWarehouseCode(),
			ImAdjustmentLine.getLotNo(), ImAdjustmentLine.getDifQuantity(), opUser,brandCode);
		String declarationNo  = ImAdjustmentLine.getOriginalDeclarationNo();
		Double originalPriceAmount = new Double(0d);

		List itemPrices = imItemPriceDAO.getItemPriceByBeginDate(brandCode, ImAdjustmentLine.getItemCode(), "1",
				ImAdjustmentHead.getAdjustmentDate(), "Y");
		if (itemPrices != null && itemPrices.size() > 0) {
			Object[] objArray = (Object[]) itemPrices.get(0);
			BigDecimal unitPrice = (BigDecimal) objArray[1];
			if (unitPrice != null) {
				originalPriceAmount = unitPrice.doubleValue();
			}
		}

		String declType = new String("");
		if(StringUtils.hasText(declarationNo)){
		    declType =cmDeclarationHeadDAO.getDeclarationTypeByNo(declarationNo);
		    if(!StringUtils.hasText(declType))
			throw new ValidationErrorException("查無來源報單:"+declarationNo +"類別資料！");
		}
		Double amount= new Double(0D);
		if(null!=ImAdjustmentLine.getAmount())
			amount = ImAdjustmentLine.getAmount();
		// 產生交易明細檔 出庫
		ImTransation transation = new ImTransation();
		transation.setBrandCode(brandCode);
		transation.setTransationDate(ImAdjustmentHead.getAdjustmentDate());
		transation.setOrderTypeCode(orderTypeCode);
		transation.setOrderNo(orderNo);
		transation.setLineId(ImAdjustmentLine.getIndexNo());
		transation.setItemCode(ImAdjustmentLine.getItemCode());
		transation.setWarehouseCode(ImAdjustmentLine.getWarehouseCode());
		transation.setLotNo(ImAdjustmentLine.getLotNo());
		transation.setQuantity(ImAdjustmentLine.getDifQuantity());
		transation.setCostAmount(amount);
		transation.setOrderAmount(amount);		
		// === 廠商註解 2019/12/28 海關上傳重整單修正
		if(ImAdjustmentHead.getOrderTypeCode().equals("BEF")){
			if (ImAdjustmentHead.getSourceOrderTypeCode().equals("1")) {  // 1-整理
				if (ImAdjustmentLine.getBeAft().equals("1")){
					transation.setAffectCost("N");
				} else {
					transation.setAffectCost("Y");
				}
			} else {                                                     //  2-分割
				transation.setAffectCost("N");
			}
			// Reserve5 紀錄重整單的1-調整前或2-調整後
			if (ImAdjustmentLine.getBeAft().equals("1"))
				transation.setReserve5("1-重整前");
			else
				transation.setReserve5("2-重整後");
		} else {
			// === 廠商註解 2019/12/28 原來程式
			if(ImAdjustmentHead.getAffectCost().equals("")||ImAdjustmentHead.getAffectCost()==null){
				transation.setAffectCost("N");
			}else{
				transation.setAffectCost(ImAdjustmentHead.getAffectCost());
			}
			// === 廠商註解 原來程式
		}
		// === 廠商註解
		transation.setOriginalPriceAmount(originalPriceAmount * transation.getQuantity()); // 原商品訂價金額 * 數量
		transation.setDeclarationNo(ImAdjustmentHead.getDeclarationNo());
		transation.setDeclarationDate(ImAdjustmentHead.getDeclarationDate());
		transation.setOriginalDeclarationNo(ImAdjustmentLine.getOriginalDeclarationNo());
		transation.setOriginalDeclarationDate(ImAdjustmentLine.getOriginalDeclarationDate());
		transation.setOriginalDeclarationSeq(ImAdjustmentLine.getOriginalDeclarationSeq());
		transation.setOriginalDeclarationType(declType);
		transation.setAdjustmentType(ImAdjustmentHead.getAdjustmentType());
		transation.setCreatedBy(opUser);
		transation.setCreationDate(new Date());
		transation.setLastUpdatedBy(opUser);
		transation.setLastUpdatedDate(new Date());
		imTransationDAO.save(transation);

		// 保稅才會維護cmOnHand
		if(TAX_TYPE_F.equals(taxType)){
		    if( IN == adjustmentType && ("MEF".equalsIgnoreCase( orderTypeCode )||"MDF".equalsIgnoreCase( orderTypeCode )) ){
			// 離島調整單,入倉
			cmDeclarationOnHandDAO.updateOtherOnHand( ImAdjustmentHead.getDeclarationNo(), ImAdjustmentLine.getIndexNo(), ImAdjustmentLine.getItemCode(),
				customsWarehouseCode, brandCode, ImAdjustmentLine.getDifQuantity(), opUser);
		    }else{
			// 一般情況
			cmDeclarationOnHandDAO.updateOtherOnHand( ImAdjustmentLine.getOriginalDeclarationNo(), ImAdjustmentLine.getOriginalDeclarationSeq(), ImAdjustmentLine.getItemCode(),
				customsWarehouseCode, brandCode, ImAdjustmentLine.getDifQuantity(), opUser);
		    }
		}
	    }
	    imAdjustmentHeadDAO.update(ImAdjustmentHead);

	} else {
	    if(ImAdjustmentHead.getOrderTypeCode().equals("APF")){
	    	imAdjustmentHeadDAO.update(ImAdjustmentHead);
	    }else{
	    	throw new ValidationErrorException("查無" + identification + "的調整單明細資料！");
	    }		
	}

    }

    /**
     * 產生新的移倉單主檔
     * @param otherBean
     * @return
     * @throws Exception
     */
    private ImAdjustmentHead executeNew(Object otherBean)throws Exception{
	ImAdjustmentHead head = new ImAdjustmentHead();
	try {
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");

	    head.setOrderTypeCode(orderTypeCode);
	    head.setBrandCode(loginBrandCode);
	    head.setStatus( OrderStatus.SAVE );
	    head.setAffectCost("N"); 	// 影響成本
	    head.setBoxQty(0L);
	    head.setAdjustmentDate( DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH, DateUtils.getCurrentDateStr(DateUtils.C_DATE_PATTON_SLASH)) );
	    //head.setResNo(resNo);
	    
	    this.setOtherColumn(head);
	    
	    if(orderTypeCode.equals("APF")){
	    	head.setSourceOrderTypeCode("AAF");
	    }
	    //
	    if(orderTypeCode.equals("BEF")){
	    	head.setDefaultWarehouseCode("F9900");
	    }
	    
	    head.setCreatedBy(loginEmployeeCode);
	    head.setCreationDate(new Date());
	    head.setLastUpdatedBy(loginEmployeeCode);
	    head.setLastUpdateDate(new Date());

	    this.saveTmpHead(head);
	    this.saveResNo(head);

	} catch (Exception e) {
	    log.error("建立新調整單主檔失敗,原因:"+e.toString());
	    throw new Exception("建立新調整單主檔失敗,原因:"+e.toString());
	}
	return head;
    }

    /**
     * 依據headId為查詢條件，取得調整單主檔
     * @param headId
     * @return
     * @throws Exception
     */
    public ImAdjustmentHead executeFind(Long headId) throws Exception {

	try {
	    ImAdjustmentHead head = (ImAdjustmentHead) baseDAO.findByPrimaryKey(ImAdjustmentHead.class,
		    headId);
	    return head;
	} catch (Exception ex) {
	    log.error("依據主鍵：" + headId + "查詢調整單主檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據主鍵：" + headId + "查詢調整單主檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 匯入調整單明細
     * @param headId
     * @param lineLists
     * @throws Exception
     */
    public void executeImportLists(Long headId, String defaultWarehouseCode, List lineLists) throws Exception{
	try{
	    log.info(" headId = " + headId);
	    log.info(" defaultWarehouseCode = " + defaultWarehouseCode);
	    
	    imAdjustmentLineService.deleteLineLists(headId);
	   // ImAdjustmentHead head = (ImAdjustmentHead) baseDAO.findByPrimaryKey(ImAdjustmentHead.class,headId);
	    
	    if(lineLists != null && lineLists.size() > 0){
		ImAdjustmentHead imAdjustmentHeadTmp = new ImAdjustmentHead();
		imAdjustmentHeadTmp.setHeadId(headId);
		imAdjustmentHeadTmp.setDefaultWarehouseCode(defaultWarehouseCode);
		for(int i = 0; i < lineLists.size(); i++){
		    ImAdjustmentLine  imAdjustmentLine = (ImAdjustmentLine)lineLists.get(i);
		    imAdjustmentLine.setWarehouseCode(defaultWarehouseCode);
		    imAdjustmentLine.setImAdjustmentHead(imAdjustmentHeadTmp);

		    if(StringUtils.hasText(imAdjustmentLine.getLotNo())){
			imAdjustmentLine.setLotNo(SystemConfig.LOT_NO);
		    }

		    //如果報單是手動自己配再匯入 則自動找出原報單日期 by Caspar 2012.12.28
		    if(null != imAdjustmentLine.getOriginalDeclarationNo() && null != imAdjustmentLine.getOriginalDeclarationSeq()){
		    	String tmpSqlItem = " select to_char(IMPORT_DATE,'yyyyMMdd') from CM_DECLARATION_VIEW " +
		    			" where DECL_NO = '"+imAdjustmentLine.getOriginalDeclarationNo()+"' " +
		    			" and ITEM_NO = " + imAdjustmentLine.getOriginalDeclarationSeq() ;
		    	List declList = nativeQueryDAO.executeNativeSql(tmpSqlItem);

		    	for (Iterator iterator = declList.iterator(); iterator.hasNext();) {
		    		Object obj = (Object)iterator.next();
		    		Date originalDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, String.valueOf(obj));
		    		imAdjustmentLine.setOriginalDeclarationDate(originalDate);
		    		
		    	/*	if (head.equals("MDF") || head.equals("MDP")){
		    			imAdjustmentLine.setDifQuantity((imAdjustmentLine.getDifQuantity()*-1));
		    		}*/
		    	}
		    }
		    
		    imAdjustmentLine.setIndexNo(i+1L);
		    imAdjustmentLineDAO.save(imAdjustmentLine);
		}
	    }
	}catch (Exception ex) {
	    log.error("調整單明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("調整單明細匯入時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 匯入拆/併貨調整單明細
     * @param headId
     * @param lineLists
     * @throws Exception
     */
    public void executeImportApartLists(Long headId, List lineLists) throws Exception{
	try{
	    ImAdjustmentHead imAdjustmentHead = this.executeFind(headId);
	    imAdjustmentHead.setImAdjustmentLines(new ArrayList(0));
	    imAdjustmentHeadDAO.update(imAdjustmentHead);

	    if(lineLists != null && lineLists.size() > 0){
		ImAdjustmentHead imAdjustmentHeadTmp = new ImAdjustmentHead();
		imAdjustmentHeadTmp.setHeadId(headId);
		for(int i = 0; i < lineLists.size(); i++){
		    ImAdjustmentLine  imAdjustmentLine = (ImAdjustmentLine)lineLists.get(i);
		    imAdjustmentLine.setImAdjustmentHead(imAdjustmentHeadTmp);
		    imAdjustmentLine.setIndexNo(i+1L);
		    imAdjustmentLine.setItemNo(i+1L);
		    imAdjustmentLine.setLotNo("000000000000");
		    imAdjustmentLine.setBeAft("1");
		    imAdjustmentLine.setSpecDesc(imAdjustmentLine.getItemCode());
		    imAdjustmentLineDAO.save(imAdjustmentLine);
		}
	    }
	}catch (Exception ex) {
	    log.error("拆/併貨調整單明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("拆/併貨調整單明細匯入時發生錯誤，原因：" + ex.getMessage());
	}
    }


    /**
     * 撈符合的進貨單
     * @param brandCode
     * @param orderTypeCode
     * @param taxType
     * @param sourceOrderNo
     * @return
     */
    private ImReceiveHead findOneImReceive(String brandCode, String sourceOrderTypeCode, String taxType, String sourceOrderNo){
	return (ImReceiveHead)imReceiveHeadDAO.findFirstByProperty(
		"ImReceiveHead","and brandCode = ? and orderTypeCode = ? and orderNo = ? ",  // and taxType = ? and status = ?	!!! 確認撈進貨單
		new Object[] { brandCode, sourceOrderTypeCode, sourceOrderNo,  });  // taxType, OrderStatus.FINISH

    }

    /**
     * 撈一筆商品零售價 !!! 條件還有ㄇ?
     * @param brandCode
     * @param orderTypeCode
     * @param taxType
     * @param sourceOrderNo
     * @return
     */
    private ImItemPrice findOneImItemPrice(String brandCode, String itemCode){
	return (ImItemPrice)imItemPriceDAO.findFirstByProperty("ImItemPrice", "and brandCode = ? and itemCode = ?", new Object[] { brandCode, itemCode});
    }

    /**
     * 撈一筆報關單
     * @param declNo
     * @return
     */
    private CmDeclarationHead findOneCmDeclaration(String declNo){
	return (CmDeclarationHead)cmDeclarationHeadDAO.findFirstByProperty("CmDeclarationHead", "and declNo = ?", new Object[]{ declNo });
    }

    /**
     * 撈一筆報關單 D7
     * @param declNo
     * @return
     */
    private CmDeclarationHead findOneD7CmDeclaration(String declNo, String decType){
	return findOneCmDeclaration(declNo, decType, OrderStatus.WAIT_IN);
    }

    /**
     * 撈一筆報關單 D?
     * @param declNo
     * @return
     */
    private CmDeclarationHead findOneCmDeclaration(String declNo, String decType, String Status){
	if(StringUtils.hasText(Status)){
	    return (CmDeclarationHead)cmDeclarationHeadDAO.findFirstByProperty("CmDeclarationHead", "and declNo = ? and declType = ? and status = ?", new Object[]{ declNo, decType, Status});
	}else{
	    if(StringUtils.hasText(decType)){
		return (CmDeclarationHead)cmDeclarationHeadDAO.findFirstByProperty("CmDeclarationHead", "and declNo = ? and declType = ?", new Object[]{ declNo, decType});
	    }else{
		return (CmDeclarationHead)cmDeclarationHeadDAO.findFirstByProperty("CmDeclarationHead", "and declNo = ?", new Object[]{ declNo});
	    }
	}

    }

    /**
     * 透過headId取得調整單
     * @param bean
     * @return
     * @throws FormException
     * @throws Exception
     */
    public ImAdjustmentHead getActualImAdjustment(Object otherBean ) throws FormException, Exception{

	ImAdjustmentHead imAdjustmentHead = null;
	String id = (String)PropertyUtils.getProperty(otherBean, "headId");

	if(StringUtils.hasText(id)){
	    Long headId = NumberUtils.getLong(id);
	    imAdjustmentHead = (ImAdjustmentHead)imAdjustmentHeadDAO.findByPrimaryKey(ImAdjustmentHead.class, headId);
	    if(imAdjustmentHead  == null){
		throw new NoSuchObjectException("查無調整單主鍵：" + headId + "的資料！");
	    }
	}else{
	    throw new ValidationErrorException("傳入的調整單主鍵為空值！");
	}
	return imAdjustmentHead;

    }

    /**
     * 依formId取得實際移倉單 in initial
     * @param otherBean
     * @param resultMap
     * @return
     * @throws Exception
     */
    private ImAdjustmentHead getActualHead(Object otherBean, Map resultMap) throws Exception{
	ImAdjustmentHead imAdjustmentHead = null;
	try {
	    String formIdString = null;//使用PropertyUtils.getProperty() 從otherBean取得formId 
	    Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
	    imAdjustmentHead = null == formId ? this.executeNew(otherBean) : this.executeFind(formId);

	} catch (Exception e) {
	    log.error("取得實際調整單主檔失敗,原因:"+e.toString());
	    throw new Exception("取得實際調整單主檔失敗,原因:"+e.toString());
	}
	return imAdjustmentHead;
    }

    /**
     * 匯出 by sql
     * @return
     * @throws Exception
     */
    public SelectDataInfo getAJAXFExportData(HttpServletRequest httpRequest) throws Exception{
	Object[] object = null;
	List rowData = new ArrayList();
	StringBuffer sql = new StringBuffer();
	try {
	    Properties properties = new Properties();
	    Enumeration paramNames = httpRequest.getParameterNames();
	    while (paramNames.hasMoreElements()) {
		String name = (String) paramNames.nextElement();
		String[] values = httpRequest.getParameterValues(name);
		if ((null != values) && (values.length > 0)) {
		    String value = values[0];
		    properties.setProperty(name, value);
		}
	    }
	    Long headId = NumberUtils.getLong(httpRequest.getParameter("headId"));

	    sql.append("SELECT L.ITEM_CODE, I.ITEM_C_NAME, ")

	    .append("(SELECT c1.category_name FROM IM_ITEM_CATEGORY c1 ")
        .append("WHERE I.brand_code = c1.brand_code ")
        .append("AND I.category02 = c1.category_code ")
        .append("AND c1.category_type = 'CATEGORY02') AS category02_name, ")
	    .append("L.LOT_NO, L.AMOUNT, L.DIF_QUANTITY, I.PURCHASE_UNIT, L.ORIGINAL_DECLARATION_NO, ")
	    .append("L.ORIGINAL_DECLARATION_SEQ, L.ORIGINAL_DECLARATION_DATE ,CI.QTY, NVL(CH.IMPORT_DATE,''), ")
	    .append("L.BOX_NO, L.WEIGHT, CI.O_DECL_NO, CI.O_ITEM_NO, ")
	    .append("CI.PRODUCE_COUNTRY, CI.CODE, NVL(CI.PRICDUTY,0), CI.CUST_VALUE_AMT/CI.QTY AS declUnitPrice ")

	    .append("FROM IM_ADJUSTMENT_HEAD H, IM_ADJUSTMENT_LINE L, CM_DECLARATION_HEAD CH, CM_DECLARATION_ITEM CI, IM_ITEM I ")
	    .append("WHERE H.HEAD_ID = L.HEAD_ID ")
	    .append("AND H.BRAND_CODE = I.BRAND_CODE ")
	    .append("AND L.ITEM_CODE = I.ITEM_CODE ")
	    .append("AND L.ORIGINAL_DECLARATION_NO = CH.DECL_NO ")
	    .append("AND CH.HEAD_ID = CI.HEAD_ID ")
	    .append("AND L.ORIGINAL_DECLARATION_SEQ = CI.ITEM_NO ")
	    .append("AND H.HEAD_ID = ").append(headId).append(" ")
	    .append("ORDER BY L.INDEX_NO ");

	    List lists = nativeQueryDAO.executeNativeSql(sql.toString());
	    object = new Object[] {
		    "itemCode", "itemCName", "category02name", 
		    "lotNo", "amount", "difQuantity", "purchaseUnit", "originalDeclarationNo", 
		    "originalDeclarationSeq", "originalDeclarationDate", "originalDeclarationQty", "importDate",
		    "boxNo", "weight", "oDeclNo", "oItemNo", 
		    "produceCountry", "code", "pricdutty", "declUnitPrice"

	    };

	    log.info(" lists.size() = " + lists.size() );
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
	    e.printStackTrace();
	    log.error("匯出excel發生錯誤，原因：" + e.toString());
	    throw new Exception("匯出excel發生錯誤，原因：" + e.getMessage());
	}
    }

    /**
     * 匯出其它 by sql
     * @return
     * @throws Exception
     */
    public SelectDataInfo getAJAXFExportDataOther(HttpServletRequest httpRequest) throws Exception{
	Object[] object = null;
	List rowData = new ArrayList();
	StringBuffer sql = new StringBuffer();
	try {
	    Properties properties = new Properties();
	    Enumeration paramNames = httpRequest.getParameterNames();
	    while (paramNames.hasMoreElements()) {
		String name = (String) paramNames.nextElement();
		String[] values = httpRequest.getParameterValues(name);
		if ((null != values) && (values.length > 0)) {
		    String value = values[0];
		    properties.setProperty(name, value);
		}
	    }
	    Long headId = NumberUtils.getLong(httpRequest.getParameter("headId"));

	    sql.append("SELECT L.ITEM_CODE, L.LOT_NO, L.AMOUNT, L.DIF_QUANTITY, ")
	    .append("I.ITEM_C_NAME, I.ITEM_E_NAME, I.SUPPLIER_ITEM_CODE, I.CATEGORY01, ")

	    .append("(SELECT c1.category_name FROM IM_ITEM_CATEGORY c1 ")
            .append("WHERE I.brand_code = c1.brand_code ")
            .append("AND I.category01 = c1.category_code ")
            .append("AND c1.category_type = 'CATEGORY01') AS category01_name, ")

	    .append("I.CATEGORY02, ")
	    .append("(SELECT c1.category_name FROM IM_ITEM_CATEGORY c1 ")
            .append("WHERE I.brand_code = c1.brand_code ")
            .append("AND I.category02 = c1.category_code ")
            .append("AND c1.category_type = 'CATEGORY02') AS category02_name, ")

            .append("I.CATEGORY03, ")
	    .append("(SELECT c1.category_name FROM IM_ITEM_CATEGORY c1 ")
            .append("WHERE I.brand_code = c1.brand_code ")
            .append("AND I.category03 = c1.category_code ")
            .append("AND c1.category_type = 'CATEGORY03') AS category03_name, ")
            .append("I.BOX_CAPACITY ")

            .append("FROM IM_ADJUSTMENT_HEAD H, IM_ADJUSTMENT_LINE L, IM_ITEM I ")
	    .append("WHERE H.HEAD_ID = L.HEAD_ID ")
	    .append("AND H.BRAND_CODE = I.BRAND_CODE ")
	    .append("AND L.ITEM_CODE = I.ITEM_CODE ")
	    .append("AND H.HEAD_ID = ").append(headId).append(" ")
	    .append("ORDER BY L.INDEX_NO ");

	    List lists = nativeQueryDAO.executeNativeSql(sql.toString());

	    // 為了跑回圈放值用
	    object = new Object[] {
		    "itemCode",		"lotNo",		"amount", 		"difQuantity",
		    "itemCName",	"itemEName",		"supplierItemCode", 	"category01",	 "category01Name",
		    "category02",	"category02Name", 	"category03",		"category03Name",	 "boxCapacity"
	    };

	    log.info(" lists.size() = " + lists.size() );
	    // 限制65535 excel
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
	    e.printStackTrace();
	    log.error("匯出excel發生錯誤，原因：" + e.toString());
	    throw new Exception("匯出excel發生錯誤，原因：" + e.getMessage());
	}
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
	    ImAdjustmentHead imAdjustmentHead = (ImAdjustmentHead)imAdjustmentHeadDAO.findByPrimaryKey(ImAdjustmentHead.class, headId);
	    if(imAdjustmentHead != null){
		id = MessageStatus.getIdentification(imAdjustmentHead.getBrandCode(),
			imAdjustmentHead.getOrderTypeCode(), imAdjustmentHead.getOrderNo());
	    }else{
		throw new NoSuchDataException("調整單主檔查無主鍵：" + headId + "的資料！");
	    }

	    return id;
	}catch(Exception ex){
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * ajax 第一次載入調整單明細時,取得分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String brandCode = httpRequest.getProperty("brandCode");
	    String taxType = httpRequest.getProperty("taxType");
	    String defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode");
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	    log.info("adj orderType:"+orderTypeCode);
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    HashMap findObjs = new HashMap();
	    findObjs.put("and model.imAdjustmentHead.headId = :headId", headId);

	    Map searchMap = baseDAO.search("ImAdjustmentLine as model", findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
	    List<ImAdjustmentLine> imAdjustmentLines = (List<ImAdjustmentLine>)searchMap.get(BaseDAO.TABLE_LIST);

	    HashMap map = new HashMap();
	    map.put("headId", headId);

	    if (imAdjustmentLines != null && imAdjustmentLines.size() > 0) {

	    	//AIR併入調整單
	    	/*if(orderTypeCode.equals("AIR") && ((defaultWarehouseCode == null ||  defaultWarehouseCode.equals("")) || (taxType == null || taxType.equals("")) )){
	    		defaultWarehouseCode = imAdjustmentLines.get(0).getWarehouseCode();
	    		taxType = "P";
	    	}*/
	    // 設定調撥單明細檔其他欄位(品名,售價,庫別)
		this.setLineOtherColumn(imAdjustmentLines, brandCode, taxType, defaultWarehouseCode);

		// 取得第一筆的INDEX
		Long firstIndex = imAdjustmentLines.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		Long maxIndex = (Long)baseDAO.search("ImAdjustmentLine as model", "count(model.imAdjustmentHead.headId) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT);
		if(orderTypeCode.equals("MEG") || orderTypeCode.equals("MEF") || orderTypeCode.equals("MEP")|| orderTypeCode.equals("MDF")|| orderTypeCode.equals("MDP")){
			result.add(AjaxUtils.getAJAXPageData(httpRequest,MEG_GRID_FIELD_NAMES, MEG_GRID_FIELD_DEFAULT_VALUES,imAdjustmentLines, gridDatas, firstIndex, maxIndex));
		}else if(orderTypeCode.equals("AIR")){
			result.add(AjaxUtils.getAJAXPageData(httpRequest,AIR_GRID_FIELD_NAMES, AIR_GRID_FIELD_DEFAULT_VALUES,imAdjustmentLines, gridDatas, firstIndex, maxIndex));
		}else if(orderTypeCode.equals("ICA")){
			result.add(AjaxUtils.getAJAXPageData(httpRequest,COMPOSE_GRID_FIELD_NAMES, COMPOSE_GRID_FIELD_DEFAULT_VALUES,imAdjustmentLines, gridDatas, firstIndex, maxIndex));
		}else {
			result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,imAdjustmentLines, gridDatas, firstIndex, maxIndex));
		}
		
	    } else {
	    	if(orderTypeCode.equals("MEG") || orderTypeCode.equals("MEF") || orderTypeCode.equals("MEP")|| orderTypeCode.equals("MDF")|| orderTypeCode.equals("MDP")){
	    		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,MEG_GRID_FIELD_NAMES, MEG_GRID_FIELD_DEFAULT_VALUES, map,gridDatas));
	    	}else if(orderTypeCode.equals("AIR")){
	    		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,AIR_GRID_FIELD_NAMES, AIR_GRID_FIELD_DEFAULT_VALUES, map,gridDatas));
	    	}else if(orderTypeCode.equals("ICA")){
	    		log.info("跑ICA+");
	    		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,COMPOSE_GRID_FIELD_NAMES, COMPOSE_GRID_FIELD_DEFAULT_VALUES, map,gridDatas));
	    	}else {
	    		log.info("跑else++");
	    		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,gridDatas));
	    	}
	    }

	    return result;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    log.error("載入頁面顯示的調整單明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的調整單明細失敗！");
	}
    }

    /**
     * ajax 第一次載入拆/併貨調整單明細時,取得分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXApartPageData(Properties httpRequest) throws Exception{
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String brandCode = httpRequest.getProperty("brandCode");

	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    HashMap findObjs = new HashMap();
	    findObjs.put("and model.imAdjustmentHead.headId = :headId", headId);

	    Map searchMap = baseDAO.search("ImAdjustmentLine as model", findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
	    List<ImAdjustmentLine> imAdjustmentLines = (List<ImAdjustmentLine>)searchMap.get(BaseDAO.TABLE_LIST);

	    HashMap map = new HashMap();
	    map.put("headId", headId);

	    if (imAdjustmentLines != null && imAdjustmentLines.size() > 0) {

		// 設定調撥單明細檔其他欄位(品名,售價,庫別)
		this.setApartLineOtherColumn(imAdjustmentLines, brandCode);

		// 取得第一筆的INDEX
		Long firstIndex = imAdjustmentLines.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		Long maxIndex = (Long)baseDAO.search("ImAdjustmentLine as model", "count(model.imAdjustmentHead.headId) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT);
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_APART_NAMES, GRID_FIELD_APART_DEFAULT_VALUES,imAdjustmentLines, gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_APART_NAMES, GRID_FIELD_APART_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的拆/併貨調整單明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的拆/併貨調整單明細失敗！");
	}
    }

    /**
     * ajax 計算合計金額(拆併貨調整單)
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXAmount(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String amount = "0.0";
	try {
	    Double difQuantity = NumberUtils.getDouble(httpRequest.getProperty("difQuantity"));
	    Double localUnitCost = NumberUtils.getDouble(httpRequest.getProperty("localUnitCost"));

	    log.info( "difQuantity = " + difQuantity);
	    log.info( "localUnitCost = " + localUnitCost);

	    amount = NumberUtils.roundToStr(difQuantity * localUnitCost, 0);

	    properties.setProperty("localUnitCost", NumberUtils.roundToStr(localUnitCost, 2) );
	    properties.setProperty("amount", amount );

	    result.add(properties);
	    return result;
	}catch (Exception ex) {
	    log.error("計算金額資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("計算金額資料失敗！");
	}
    }

    /**
     * ajax 取得品號相關欄位(拆併貨調整單)
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXItemCode(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String itemCName = null;
	String amount = "0.0";
	try {
	    String brandCode = httpRequest.getProperty("brandCode");
	    String itemCode = httpRequest.getProperty("itemCode").trim().toUpperCase();
	    String lotNo = httpRequest.getProperty("lotNo");
	    Double localUnitCost = NumberUtils.getDouble(httpRequest.getProperty("localUnitCost"));
	    Double difQuantity = NumberUtils.getDouble(httpRequest.getProperty("difQuantity"));

	    if(StringUtils.hasText(itemCode)){
			ImItem imItem = imItemDAO.findItem(brandCode, itemCode);
			if (null != imItem) {
			    itemCName = imItem.getItemCName();
			} else {
			    itemCName = "查無品號";
			}
	
			//localUnitCost = imItemPriceDAO.getOldestUnitPrice(brandCode, itemCode);
			//
			if(null == imItem.getStandardPurchaseCost()){
				if(null == imItem.getLastPurLocalAmount()){
					localUnitCost = imItemPriceDAO.getOldestUnitPrice(brandCode, itemCode);
				}else{
					localUnitCost = imItem.getLastPurLocalAmount();
				}
			}else{
				localUnitCost = imItem.getStandardPurchaseCost();
			}
			
	
			if(!StringUtils.hasText(lotNo)){
			    lotNo = SystemConfig.LOT_NO;
			}
	
			amount = NumberUtils.roundToStr(difQuantity * localUnitCost, 0);
	
			log.info( "itemCode = " + itemCode);
			log.info( "lotNo = " + lotNo);
			log.info( "localUnitCost = " + localUnitCost);
			log.info( "amount = " + amount);
			log.info( "specDesc = " + itemCode);
	
			properties.setProperty("itemCode", itemCode );
			properties.setProperty("itemCName", itemCName );
			properties.setProperty("lotNo", lotNo );
			properties.setProperty("localUnitCost", NumberUtils.roundToStr(localUnitCost, 2) );
			properties.setProperty("amount", amount );
			properties.setProperty("specDesc", itemCode );
	    }else{
			properties.setProperty("itemCode", "" );
			properties.setProperty("itemCName", "" );
			properties.setProperty("lotNo", lotNo );
			properties.setProperty("localUnitCost", NumberUtils.roundToStr(localUnitCost, 2) );
			properties.setProperty("amount", amount );
			properties.setProperty("specDesc", "" );
	    }
	    result.add(properties);
	    return result;
	}catch (Exception ex) {
	    log.error("取得品號相關欄位資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得品號相關欄位資料失敗！");
	}
    }

    /**
     * ajax 取得庫別下拉(T2調整單)
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXWarehouse(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {
	    String brandCode = httpRequest.getProperty("brandCode");
	    String adjustmentType = httpRequest.getProperty("adjustmentType");
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	    String taxType = httpRequest.getProperty("taxType");
	    String employeeCode = httpRequest.getProperty("employeeCode");
	    log.info( "brandCode = " + brandCode);
	    log.info( "adjustmentType = " + adjustmentType);
	    log.info( "orderTypeCode = " + orderTypeCode);
	    log.info( "taxType = " + taxType);
	    log.info( "employeeCode = " + employeeCode);

	    List<ImWarehouse> allWarehouseCodes = null;
	    if( brandCode.indexOf("T2") > -1){
		String condition2 = "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? and attribute1 like ?";
		if(StringUtils.hasText(adjustmentType)){
		    BuCommonPhraseLine buCommonPhraseLine = (BuCommonPhraseLine)baseDAO.findFirstByProperty("BuCommonPhraseLine", "", condition2, new Object[]{"AdjustmentType", adjustmentType, "Y", "%"+orderTypeCode+"%"}, " order by indexNo");
		    log.info("buCommonPhraseLine = " + buCommonPhraseLine);
		    String warehouseCodes = null;
		    if("F".equals(taxType)){
		    	if("MEG".equals(orderTypeCode)){
		    		warehouseCodes = buCommonPhraseLine.getAttribute4();
		    	}else if("MDF".equals(orderTypeCode)){
					warehouseCodes = buCommonPhraseLine.getParameter3();
		    	}else{
		    		warehouseCodes = buCommonPhraseLine.getAttribute2();
		    	}
		    }else if("P".equals(taxType)){
		    	if("MDP".equals(orderTypeCode)){
		    		warehouseCodes = buCommonPhraseLine.getParameter3();
		    	}else{
		    		warehouseCodes = buCommonPhraseLine.getAttribute3();
		    	}
		    }
		    if(!StringUtils.hasText(warehouseCodes)){
			allWarehouseCodes = imWarehouseEmployeeDAO.findByEmployeeCode(brandCode, employeeCode);
		    }else{
			allWarehouseCodes = imWarehouseEmployeeDAO.findByEmployeeCode(brandCode, employeeCode, warehouseCodes);
		    }

		    if(IN == NumberUtils.getInt(adjustmentType) || OUT == NumberUtils.getInt(adjustmentType)){
			BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(brandCode, orderTypeCode));
			String sourceOrderType = AjaxUtils.getPropertiesValue( buOrderType.getReserve1(), "");
			String declType = AjaxUtils.getPropertiesValue( buOrderType.getReserve2(), "");

			properties.setProperty("sourceOrderType", sourceOrderType );
			properties.setProperty("declType", declType );
		    }

		}else{
		    allWarehouseCodes = imWarehouseEmployeeDAO.findByEmployeeCode(brandCode, employeeCode);
		}
		allWarehouseCodes = AjaxUtils.produceSelectorData(allWarehouseCodes, "warehouseCode", "warehouseName", true, true);
		log.info("allWarehouseCodes.size = " + allWarehouseCodes.size());
		log.info("AjaxUtils.parseSelectorData(allWarehouseCodes) = " + AjaxUtils.parseSelectorData(allWarehouseCodes));
		properties.setProperty("allWarehouseCodes", AjaxUtils.parseSelectorData(allWarehouseCodes));
	    }

	    result.add(properties);
	    return result;
	}catch (Exception ex) {
	    log.error("取得庫別相關欄位資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得庫別相關欄位資料失敗！");
	}
    }

    /**
     * ajax 取得商品
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXImItem(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
//	Map cmDeclarationlineMap = new HashMap();
	Map conditionMap = new HashMap();
	String customsWarehouseCode = null;
	try {
	    String brandCode = httpRequest.getProperty("brandCode");
	    String taxType = httpRequest.getProperty("taxType");
	    String adjustmentType = httpRequest.getProperty("adjustmentType");
	    String defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode");
	    String itemCode = httpRequest.getProperty("itemCode").trim().toUpperCase();
	    String originalDeclarationNo = httpRequest.getProperty("originalDeclarationNo").trim().toUpperCase();
	    String originalDeclarationSeq = httpRequest.getProperty("originalDeclarationSeq");
	    Double difQuantity = NumberUtils.getDouble(httpRequest.getProperty("difQuantity"));
	    Double unitCost = NumberUtils.getDouble(httpRequest.getProperty("unitCost"));
	    
	    log.info( "adjustmentType = " + adjustmentType);
	    log.info( "itemCode = " + itemCode);
	    log.info( "originalDeclarationNo = " + originalDeclarationNo);
	    log.info( "originalDeclarationSeq = " + originalDeclarationSeq);

//	    String declarationNo = httpRequest.getProperty("declarationNo");

	    ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, defaultWarehouseCode, "Y");
	    // 倉庫屬於的關
	    log.info( "imWarehouse = " + imWarehouse);

	    if( imWarehouse != null ){
		log.info( "imWarehouse.getCustomsWarehouseCode() = " + imWarehouse.getCustomsWarehouseCode());
		customsWarehouseCode = imWarehouse.getCustomsWarehouseCode(); 			// 關別PK
	    }else{
		log.info( "customsWarehouseCode() = 空" );
		customsWarehouseCode = ""; 			// 關別PK
	    }
	    log.info( "taxType = " + taxType);
	    log.info( "unitCost = " + unitCost);
	    log.info( "difQuantity = " + difQuantity);
	    conditionMap.put("brandCode", brandCode);
	    conditionMap.put("taxType", taxType);
	    conditionMap.put("adjustmentType", adjustmentType);
	    conditionMap.put("itemCode", itemCode);
	    conditionMap.put("customsWarehouseCode", customsWarehouseCode);
	    conditionMap.put("originalDeclarationNo", originalDeclarationNo);
	    conditionMap.put("originalDeclarationSeq", originalDeclarationSeq);
//	    conditionMap.put("difQuantity", difQuantity);
	    //conditionMap.put("unitCost", unitCost);
	    conditionMap.put("properties", properties);

	    // F保稅入口
	    if("F".equals(taxType)){
		doTaxTypeIsF(conditionMap);
	    }else if("P".equals(taxType)){
		doTaxTypeIsP(conditionMap);
	    }
	    
	    if(StringUtils.hasText(originalDeclarationNo)){
	    	String tmpSqlItem = " select to_char(IMPORT_DATE,'yyyy/mm/dd') from CM_DECLARATION_VIEW " +
	    			" where DECL_NO = '"+originalDeclarationNo+"' " ;
	    	List declList = nativeQueryDAO.executeNativeSql(tmpSqlItem);

	    	for (Iterator iterator = declList.iterator(); iterator.hasNext();) {
	    		Object obj = (Object)iterator.next();
	    		properties.setProperty("originalDeclarationDate", String.valueOf(obj));
	    	}
	    }
	    
//	    properties.setProperty("amount", "0.0");
	    properties.setProperty("warehouseCode", defaultWarehouseCode);
//	    properties.setProperty("difQuantity", "0");
	    properties.setProperty("originalDeclarationNo", originalDeclarationNo );
	    properties.setProperty("originalDeclarationSeq", String.valueOf(originalDeclarationSeq));

	    result.add(properties);
	    return result;
	}catch (Exception ex) {
	    log.error("取得商品資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得商品資料失敗！");
	}
    }

    /**
     * ajax 取得調整單search分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{

	try{
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    //======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 品牌代號

	    String orderNo = httpRequest.getProperty("orderNo" );
	    Date adjustmentDateStart = DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("adjustmentDateStart") );
	    Date adjustmentDateEnd = DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("adjustmentDateEnd") );

	    String status = httpRequest.getProperty("status");
	    String sourceOrderNo = httpRequest.getProperty("sourceOrderNo");
	    String adjustmentType = httpRequest.getProperty("adjustmentType");
	    String declarationNo = httpRequest.getProperty("declarationNo");

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and model.brandCode = :brandCode",brandCode);
	    findObjs.put(" and model.orderTypeCode = :orderTypeCode",orderTypeCode);
	    findObjs.put(" and model.orderNo NOT LIKE :TMP","TMP%");
	    findObjs.put(" and model.orderNo = :orderNo",orderNo);
	    findObjs.put(" and model.adjustmentDate >= :adjustmentDateStart",adjustmentDateStart);
	    findObjs.put(" and model.adjustmentDate <= :adjustmentDateEnd",adjustmentDateEnd);
	    findObjs.put(" and model.status = :status",status);
	    findObjs.put(" and model.sourceOrderNo = :sourceOrderNo",sourceOrderNo);
	    findObjs.put(" and model.adjustmentType = :adjustmentType", adjustmentType);
	    findObjs.put(" and model.declarationNo = :declarationNo",declarationNo);

	    //==============================================================

	    Map imAdjustmentHeadMap = imAdjustmentHeadDAO.search( "ImAdjustmentHead as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	    List<ImAdjustmentHead> imAdjustmentHeads = (List<ImAdjustmentHead>) imAdjustmentHeadMap.get(BaseDAO.TABLE_LIST);

	    if (imAdjustmentHeads != null && imAdjustmentHeads.size() > 0) {

		this.setImAdjustmentStatusName(imAdjustmentHeads);

		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
		Long maxIndex = (Long)imAdjustmentHeadDAO.search("ImAdjustmentHead as model", "count(model.headId) as rowCount" ,findObjs, "order by lastUpdateDate desc", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,imAdjustmentHeads, gridDatas, firstIndex, maxIndex));
	    }else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的調整單查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的調整單查詢失敗！");
	}
    }

    /**
     * ajax 取得拆/併貨調整單search分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXApartSearchPageData(Properties httpRequest) throws Exception{

	try{
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    //======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 品牌代號

	    String orderNoStart = httpRequest.getProperty("orderNoStart");
	    String orderNoEnd = httpRequest.getProperty("orderNoEnd");

	    Date adjustmentDateStart = DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("adjustmentDateStart") );
	    Date adjustmentDateEnd = DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("adjustmentDateEnd") );

	    String status = httpRequest.getProperty("status");
	    String sourceOrderTypeCode = httpRequest.getProperty("sourceOrderTypeCode");

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and model.brandCode = :brandCode",brandCode);
	    findObjs.put(" and model.orderTypeCode = :orderTypeCode",orderTypeCode);
	    findObjs.put(" and model.orderNo NOT LIKE :TMP","TMP%");
	    findObjs.put(" and model.orderNo >= :orderNoStart",orderNoStart);
	    findObjs.put(" and model.orderNo <= :orderNoEnd",orderNoEnd);
	    findObjs.put(" and model.adjustmentDate >= :adjustmentDateStart",adjustmentDateStart);
	    findObjs.put(" and model.adjustmentDate <= :adjustmentDateEnd",adjustmentDateEnd);
	    findObjs.put(" and model.status = :status",status);
	    findObjs.put(" and model.sourceOrderTypeCode = :sourceOrderTypeCode", sourceOrderTypeCode);

	    //==============================================================

	    Map imAdjustmentHeadMap = imAdjustmentHeadDAO.search( "ImAdjustmentHead as model", findObjs, "order by orderNo desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	    List<ImAdjustmentHead> imAdjustmentHeads = (List<ImAdjustmentHead>) imAdjustmentHeadMap.get(BaseDAO.TABLE_LIST);

	    if (imAdjustmentHeads != null && imAdjustmentHeads.size() > 0) {

		this.setImAdjustmentStatusName(imAdjustmentHeads);

		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
		Long maxIndex = (Long)imAdjustmentHeadDAO.search("ImAdjustmentHead as model", "count(model.headId) as rowCount" ,findObjs, "order by lastUpdateDate desc", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_APART_FIELD_NAMES, GRID_SEARCH_APART_FIELD_DEFAULT_VALUES,imAdjustmentHeads, gridDatas, firstIndex, maxIndex));
	    }else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_APART_FIELD_NAMES, GRID_SEARCH_APART_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的拆/併貨調整單查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的拆/併貨調整單查詢失敗！");
	}
    }

    /**
     * 合計所有Item的成本總成本、總數量
     *
     * @param httpRequest
     * @return List<Properties>
     * @throws ValidationErrorException
     */
    public List<Properties> getAJAXCountTotalAmount(Properties httpRequest)
    throws ValidationErrorException {

	try {
	    List<Properties> result = new ArrayList();
	    Properties properties = new Properties();
	    // ===================取得傳遞的的參數===================
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	    ImAdjustmentHead head = executeFind(headId);
	    // ============計算成本總金額、定價總金額、折扣後總金額、折扣總金額、平均折扣率==========
	    Double totalQuantity = 0D;
	    Double totalAmount = 0D;

	    List<ImAdjustmentLine> lines = head.getImAdjustmentLines();
	    for (ImAdjustmentLine imAdjustmentLine : lines) {
		Double difQuantity = imAdjustmentLine.getDifQuantity();
		Double amount = imAdjustmentLine.getAmount();

		if(!AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imAdjustmentLine.getIsDeleteRecord())){
		    totalQuantity += difQuantity;
		    totalAmount = OperationUtils.add(totalAmount, amount).doubleValue();
		}
	    }
	    log.info("totalQuantity = " + totalQuantity );
	    log.info("totalAmount = " + totalAmount );
	    properties.setProperty("totalQuantity", AjaxUtils.getPropertiesValue(OperationUtils.roundToStr(totalQuantity,0), "0.0"));
	    properties.setProperty("totalAmount", AjaxUtils.getPropertiesValue(OperationUtils.roundToStr(totalAmount, 2), "0.0"));

	    result.add(properties);

	    return result;
	} catch (Exception ex) {
	    log.error("調整單金額統計失敗，原因：" + ex.toString());
	    throw new ValidationErrorException("調整單金額統計失敗！");
	}
    }


    /**
     * 取得流程的配置
     * @param head
     * @param parameterMap
     * @return
     */
    public void getProcessConfig(ImAdjustmentHead head, Map parameterMap, Map resultMap)throws Exception{

	// 撈配置檔
	String condition = head.getBrandCode()+head.getOrderTypeCode()+ (null == head.getAdjustmentType() ? "" : head.getAdjustmentType()) ;
	log.info("condition = " + condition);
	BuCommonPhraseLine adjustmentTypeConfig = (BuCommonPhraseLine)baseDAO.findFirstByProperty("BuCommonPhraseLine","",
		"and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ",
		new Object[]{"AdjustmentTypeConfig",condition,"Y"}, "order by indexNo" );
	if(null == adjustmentTypeConfig){
	    throw new Exception("查無流程配置檔");
	}

	parameterMap.put("config", adjustmentTypeConfig);
	resultMap.put("processConfig", adjustmentTypeConfig);
    }

    /**
     * 取得流程的配置
     * @param head
     * @param parameterMap
     * @return
     */
    public void getProcessConfig(ImAdjustmentHead head, Map resultMap)throws Exception{
	getProcessConfig( head, new HashMap(), resultMap);
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
	    String headId = (String)PropertyUtils.getProperty(otherBean, "formId");

	    System.out.println(headId);
	    Map returnMap = new HashMap(0);
	    //CC後面要代的參數使用parameters傳遞
	    Map parameters = new HashMap(0);
	    if(brandCode.indexOf("T2") > -1){
		parameters.put("prompt0", brandCode);
		parameters.put("prompt1", orderTypeCode);
		parameters.put("prompt2", "");
		parameters.put("prompt3", "");
		parameters.put("prompt4", orderNo);
		parameters.put("prompt5", orderNo);
		parameters.put("prompt6", headId);
	    }else{
		parameters.put("prompt0", brandCode);
		parameters.put("prompt1", orderTypeCode);
		parameters.put("prompt2", orderNo);
		parameters.put("prompt6", headId);
	    }
	    System.out.println("parameters:"+parameters);
	    String reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, parameters);
	    System.out.println(reportUrl);
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
     * ajax picker按檢視返回選取的資料
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception{
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);

	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    if(result.size() > 0 ){
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
     * 執行invoice流程
     * @param head
     * @param taxType
     * @throws Exception
     */
    public void startInvoiceProcess(ImAdjustmentHead head, Object otherBean, Map parameterMap)throws Exception{
	String beforeStatus	= (String)PropertyUtils.getProperty(otherBean, "beforeStatus");
	String formAction	= (String)PropertyUtils.getProperty(otherBean, "formAction");
	String userType 	= (String)PropertyUtils.getProperty(otherBean, "userType");

	BuCommonPhraseLine adjustmentTypeConfig = (BuCommonPhraseLine)parameterMap.get("config");

	// 呼叫invoice 流程
	String business = adjustmentTypeConfig.getParameter1(); // 取得主要商業邏輯
//	String taxType = head.getTaxType();
//	int adjustmentType = NumberUtils.getInt(head.getAdjustmentType());

//	switch (adjustmentType) {
//	case DISCARD:		//	報廢
//	case F2P:		//	保稅轉完稅
//	case OUT:		//	馬祖出倉
//	    if( "F".equals(taxType)){	// 保稅
	if(STOCK_INVOICE_REJECT_ARCHIVE_DECL.equalsIgnoreCase(business)){
	    if(OrderStatus.FORM_SUBMIT.equalsIgnoreCase(formAction) && CREATOR.equalsIgnoreCase(userType)){
		if( OrderStatus.SIGNING.equals(head.getStatus()) || (OrderStatus.FINISH.equals(head.getStatus()) && OrderStatus.SAVE.equals(beforeStatus)) ){
		    log.info("imAdjustmentHead.getReserve1() = " + head.getReserve1());
		    FiInvoiceHead fiInvoiceHead = (FiInvoiceHead)fiInvoiceHeadDAO.findByPrimaryKey(FiInvoiceHead.class, NumberUtils.getLong(head.getReserve1()));
		    log.info("fiInvoiceHead = " + fiInvoiceHead);
		    FiInvoiceHeadMainService.startProcess(fiInvoiceHead);
		}
	    }
	}
//	    }
//	    break;
//	}
    }

    /**
     * 調整單存檔,取得暫存碼
     * @param imLetterOfCreditHead
     * @throws Exception
     */
    private void saveTmpHead(ImAdjustmentHead head) throws Exception{

	try{
	    String tmpOrderNo = AjaxUtils.getTmpOrderNo();
	    head.setOrderNo(tmpOrderNo);
	    baseDAO.save(head);
	}catch(Exception ex){
	    log.error("取得暫時單號儲存調整單發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得暫時單號儲存調整單發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 將調整單主檔查詢結果存檔
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
	String errorMsg = null;
	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
	return AjaxUtils.getResponseMsg(errorMsg);
    }
    /**
     * 將拆/併貨調整單主檔查詢結果存檔
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveApartSearchResult(Properties httpRequest) throws Exception{
	String errorMsg = null;
	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_APART_FIELD_NAMES);
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * 寫入invoice
     * @param imAdjustmentHead
     * @param employeeCode
     * @throws Exception
     */
    public void saveInovice(ImAdjustmentHead imAdjustmentHead, String employeeCode)throws Exception {
	log.info( "======<saveInovice>=======");
	try{
	    Date date = new Date();
	    String orderTypeCode = "F".equals(imAdjustmentHead.getTaxType()) ? "IAF" : "IAP";
	    FiInvoiceHead fiInvoiceHead = new FiInvoiceHead();
	    fiInvoiceHead.setBrandCode(imAdjustmentHead.getBrandCode());
	    fiInvoiceHead.setStatus(OrderStatus.SIGNING);
	    fiInvoiceHead.setInvoiceNo(orderTypeCode+imAdjustmentHead.getOrderNo());
	    fiInvoiceHead.setInvoiceDate(imAdjustmentHead.getAdjustmentDate());
	    fiInvoiceHead.setTotalBoxNo(imAdjustmentHead.getBoxQty());
	    fiInvoiceHead.setOrderTypeCode(orderTypeCode);
	    fiInvoiceHead.setReceiveOrderNo(imAdjustmentHead.getOrderNo()); // 來源調整單
	    fiInvoiceHead.setReceiveOrderTypeCode(imAdjustmentHead.getOrderTypeCode()); // 來源調整單單別

	    fiInvoiceHead.setCreatedBy(employeeCode);
	    fiInvoiceHead.setCreationDate(date);
	    fiInvoiceHead.setLastUpdatedBy(employeeCode);
	    fiInvoiceHead.setLastUpdateDate(date);
	    fiInvoiceHeadDAO.save(fiInvoiceHead);

	    Long i = 1L;
	    Iterator it = this.aggregateDeclarationItemQuantity(imAdjustmentHead.getImAdjustmentLines()).iterator();
	    while (it.hasNext()) {
		Map.Entry entry = (Map.Entry) it.next();
		String[] keyArray = StringUtils.delimitedListToStringArray((String) entry.getKey(), "#");
		Double quantity = Math.abs((Double) entry.getValue());
		// 撈報關單
		CmDeclarationItem cmDeclarationItem = cmDeclarationItemDAO.findOneCmDeclarationItem( keyArray[1], NumberUtils.getLong(keyArray[2]));
		if( cmDeclarationItem != null ){
		    // ============ 撈商品=====================
		    ImItem imItem = imItemDAO.findItem(imAdjustmentHead.getBrandCode(), keyArray[0]);
		    // ================ 新增發票單身 ================
		    FiInvoiceLine fiInvoiceLine = new FiInvoiceLine();
		    fiInvoiceLine.setFiInvoiceHead(fiInvoiceHead);
		    fiInvoiceLine.setBrandCode(imAdjustmentHead.getBrandCode());
		    fiInvoiceLine.setQuantity(quantity);
		    fiInvoiceLine.setItemCode(keyArray[0]);

//		    if(null != imItem){
//  fiInvoiceLine.setForeignUnitPrice(imItem.getPurchaseAmount()); //抓原報單的外幣售價
//		    fiInvoiceLine.setForeignAmount(OperationUtils.round(OperationUtils.multiplication(quantity, imItem.getPurchaseAmount()), 2).doubleValue() ); //抓原商品的原幣小計
//		    }else{
//		    log.error("查無品牌:"+imAdjustmentHead.getBrandCode()+" 商品:"+keyArray[0]);
//		    }

		    fiInvoiceLine.setOriginalDeclarationNo(keyArray[1]);
		    fiInvoiceLine.setOriginalDeclarationSeq(NumberUtils.getLong(keyArray[2]));
		    fiInvoiceLine.setIndexNo(i);
		    fiInvoiceLine.setUnit(cmDeclarationItem.getUnit());
		    fiInvoiceLine.setWeight(cmDeclarationItem.getNWght());
		    fiInvoiceLine.setIsDeleteRecord(AjaxUtils.IS_LOCK_RECORD_FALSE);
		    fiInvoiceLine.setIsLockRecord(AjaxUtils.IS_DELETE_RECORD_FALSE);
		    fiInvoiceLine.setLastUpdateDate(date);
		    fiInvoiceLine.setLastUpdatedBy(employeeCode);
		    fiInvoiceLine.setCreationDate(date);
		    fiInvoiceLine.setCreatedBy(employeeCode);
		    i++;
		    fiInvoiceLineDAO.save(fiInvoiceLine);
		}else{
		    log.error("查無此報關單:" + keyArray[1] + "項次:" + NumberUtils.getLong(keyArray[2]) );
		    throw new Exception("查無此報關單:" + keyArray[1] + "項次:" + NumberUtils.getLong(keyArray[2]));
		}
	    }

	    imAdjustmentHead.setReserve1(String.valueOf(fiInvoiceHead.getHeadId())); // 為了delete或參考用的invoice headId

	}catch (Exception ex) {
	    log.error("寫入invoice時發生錯誤，原因：" + ex.toString());
	    throw new Exception("寫入invoice時發生錯誤，原因：" + ex.getMessage());
	}
	log.info( "======<saveInovice/>=======");
    }

    /**
     * 設定調整單主檔其他欄位
     * @param head
     * @return
     * @throws Exception
     */
    private void setOtherColumn(ImAdjustmentHead head)throws Exception{
	BuOrderType buOrderType = null;
	try{

	    String orderTypeCode = head.getOrderTypeCode();
	    String brandCode = head.getBrandCode();
	    if(StringUtils.hasText( orderTypeCode ) && StringUtils.hasText( brandCode ) ){

		buOrderType = buOrderTypeDAO.findFirstByProperty("BuOrderType" , "and id.orderTypeCode = ? and id.brandCode = ?", new Object[]{orderTypeCode, brandCode } );
		if(buOrderType != null){
		    head.setTaxType( buOrderType.getTaxCode() );
//		    if( "AIF".equals(orderTypeCode) ){
//		    head.setAdjustmentType("71");
//		    }
		}else {
		    throw new Exception("請確認 BuOrderType table 有無此:" + orderTypeCode + "與" + brandCode);
		}

//		BuBrand buBrand = buBrandDAO.findById(brandCode);
//		if(buBrand != null){
//		head.setDefaultWarehouseCode( buBrand.getDefaultWarehouseCode1() == null ? "": buBrand.getDefaultWarehouseCode1() );
//		}
	    }

	}catch(Exception ex){
	    log.error("設定調整單主檔其他欄位時發生錯誤，原因：" + ex.toString());
	    throw new Exception("設定調整單主檔其他欄位時發生錯誤，原因：" + ex.toString());

	}
    }

    /**
     * 設定調撥單明細檔其他欄位(品名,售價,庫別)
     * @param imAdjustmentLines
     * @throws Exception
     */
    private void setLineOtherColumn(List<ImAdjustmentLine> imAdjustmentLines, String brandCode, String taxType, String defaultWarehouseCode)throws Exception{
	try{
	    for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
		String itemCode = imAdjustmentLine.getItemCode();

		ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findOneCurrentPrice(brandCode, taxType, itemCode);
		if( imItemCurrentPriceView != null ){
		    imAdjustmentLine.setItemCName(imItemCurrentPriceView.getItemCName());
		    imAdjustmentLine.setLocalUnitCost(imItemCurrentPriceView.getUnitPrice());
		}else{
		    imAdjustmentLine.setItemCName("查無此品號");
		    imAdjustmentLine.setLocalUnitCost(0D);
		}
		
		imAdjustmentLine.setWarehouseCode(defaultWarehouseCode);

		if("F".equals(taxType)){
		    imAdjustmentLine.setCustomsItemCode(itemCode);
		}
	    }
	}catch(Exception ex){
	    log.error("設定調撥單明細檔其他欄位時發生錯誤，原因：" + ex.toString());
	    throw new Exception("設定調撥單明細檔其他欄位時發生錯誤，原因：" + ex.toString());
	}
    }

    /**
     * 設定拆/併貨調撥單明細檔其他欄位(品名,庫名)
     * @param imAdjustmentLines
     * @throws Exception
     */
    private void setApartLineOtherColumn(List<ImAdjustmentLine> imAdjustmentLines, String brandCode)throws Exception{
	try{
	    for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
		String itemCode = imAdjustmentLine.getItemCode();
		String warehouseCode = imAdjustmentLine.getWarehouseCode();
		String declNo = imAdjustmentLine.getOriginalDeclarationNo();
		Long itemNo = imAdjustmentLine.getOriginalDeclarationSeq();
		Double difQuantity = imAdjustmentLine.getDifQuantity();
		log.info("1");
		// 品名
/*		ImItemEanPriceView imItemEanPriceView = imItemEanPriceViewDAO.getItemInfoByProperty(brandCode, itemCode);
		if( imItemEanPriceView != null ){
		    imAdjustmentLine.setItemCName(imItemEanPriceView.getItemCName());
		    imAdjustmentLine.setLocalUnitCost(imItemEanPriceView.getUnitPrice());
		    imAdjustmentLine.setAmount(NumberUtils.round(difQuantity * imItemEanPriceView.getUnitPrice(), 0));
		}else{
		    imAdjustmentLine.setItemCName("查無此品號");
		    imAdjustmentLine.setLocalUnitCost(0D);
		    imAdjustmentLine.setAmount(0D);
		}*/
		ImItem lineItem = imItemDAO.findById(itemCode);
		if( lineItem != null ){
		    imAdjustmentLine.setItemCName(lineItem.getItemCName());
		   // imAdjustmentLine.setLocalUnitCost(lineItem.getUnitPrice());
		   // imAdjustmentLine.setAmount(NumberUtils.round(difQuantity * lineItem.getUnitPrice(), 0));
		}else{
			ImItemEanPriceView imItemEanPriceView = imItemEanPriceViewDAO.getItemInfoByProperty(brandCode, itemCode);
			if( imItemEanPriceView != null ){
			    imAdjustmentLine.setItemCName(imItemEanPriceView.getItemCName());
			   // imAdjustmentLine.setLocalUnitCost(imItemEanPriceView.getUnitPrice());
			  //  imAdjustmentLine.setAmount(NumberUtils.round(difQuantity * imItemEanPriceView.getUnitPrice(), 0));
			}else{
			    imAdjustmentLine.setItemCName("查無此品號");
			  //  imAdjustmentLine.setLocalUnitCost(0D);
			  //  imAdjustmentLine.setAmount(0D);
			}
		}
		log.info("2");
		// 庫名
		ImWarehouse imWarehouse = (ImWarehouse) imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, warehouseCode, "Y");
		log.info( "imWarehouse = " + imWarehouse);
		if( imWarehouse != null ){
		    log.info( "imWarehouse.getWarehouseName() = " + imWarehouse.getWarehouseName());
		    imAdjustmentLine.setWarehouseName(imWarehouse.getWarehouseName());
		}else{
		    imAdjustmentLine.setWarehouseName("查無庫號");
		}
		log.info("FIND:"+declNo+"|"+itemNo);
		log.info("3");
		if(!StringUtils.hasText(imAdjustmentLine.getUnit())){
			if((null!=(declNo))&&(null!=(itemNo)))
			{
				CmDeclarationItem item = cmDeclarationItemDAO.findOneCmDeclarationItem(declNo, itemNo);

				if(item != null)
				{
					imAdjustmentLine.setUnit(item.getUnit());         
				}
				else
				{
					//imAdjustmentLine.setUnit(item.getUnit());   
				}

			}
		}
		
	    }
	}catch(Exception ex){
		ex.printStackTrace();
	    log.error("設定拆/併貨調撥單明細檔其他欄位時發生錯誤，原因：" + ex.toString());
	    throw new Exception("設定拆/併貨調撥單明細檔其他欄位時發生錯誤，原因：" + ex.toString());
	}
    }

    /**
     * 一般調整單依formAction取得下個狀態
     */
    public void setNextStatus(Map parameterMap, ImAdjustmentHead head) throws Exception{ // , String formAction, String approvalResult, BuCommonPhraseLine adjustmentTypeConfig
	Object otherBean = parameterMap.get("vatBeanOther");
	BuCommonPhraseLine adjustmentTypeConfig = (BuCommonPhraseLine)parameterMap.get("config");

	String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
	String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");

	String userType = (String)PropertyUtils.getProperty(otherBean, "userType");

	if(OrderStatus.FORM_SAVE.equals(formAction)){
	    if(!StringUtils.hasText(userType)){
		head.setStatus(OrderStatus.SAVE);
	    }
	}else if(OrderStatus.FORM_VOID.equals(formAction)){
	    head.setStatus(OrderStatus.VOID);
	}

//	int AdjustmentType = NumberUtils.getInt( head.getAdjustmentType());

	if( (OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction))){
	    String statusFlow = adjustmentTypeConfig.getAttribute5(); // 取得狀態是否有signing

	    if(NO_SIGNGING.equalsIgnoreCase(statusFlow)){
		if( OrderStatus.SAVE.equals(head.getStatus()) ){
		    head.setStatus(OrderStatus.FINISH);
		}
	    }else if(HAS_SIGNGING.equalsIgnoreCase(statusFlow)){
		// 判斷是否有使用人物
		if(!StringUtils.hasText(userType)){
		    if( ( OrderStatus.SAVE.equals(head.getStatus()) ) || OrderStatus.REJECT.equals(head.getStatus()) ){
			head.setStatus(OrderStatus.SIGNING);
		    }else if( OrderStatus.SIGNING.equals(head.getStatus()) ){
			if( "true".equals(approvalResult) ){
			    head.setStatus(OrderStatus.FINISH);
			}else{
			    head.setStatus(OrderStatus.REJECT);
			}
		    }
		}else{ // 報單轉換
		    if( ( OrderStatus.SAVE.equals(head.getStatus()) ) || OrderStatus.REJECT.equals(head.getStatus()) ){
			head.setStatus(OrderStatus.SIGNING);
		    }else if( OrderStatus.SIGNING.equals(head.getStatus()) ){
			if(FINANCEMGR.equalsIgnoreCase(userType)){ // 若為會計主管簽核 則結束
			    if( "true".equals(approvalResult) ){
				head.setStatus(OrderStatus.FINISH);
			    }else{
				head.setStatus(OrderStatus.REJECT);
			    }
			}else{	// 表示按下存檔按鈕
			    if( "true".equals(approvalResult) ){
				head.setStatus(OrderStatus.SIGNING);
			    }else{
				head.setStatus(OrderStatus.REJECT);
			    }
			}
		    }
		}
	    }else{
		throw new Exception("查無此狀態流程:" + statusFlow);
	    }


//	    switch (AdjustmentType) {
//	    case PROFIT: 		//	盤盈
//	    case LOSS:		//	盤虧
//	    case ADJUST_COST: 	//	調成本
//	    case ERASE_ACCOUNT:	//	領用除帳
//	    case NEGATIVE_DECLARATION: // 	負報單
//		if( OrderStatus.SAVE.equals(head.getStatus()) ){
//		    head.setStatus(OrderStatus.FINISH);
//		}
//		break;
//	    case DISCARD: 		//	報廢
//	    case F2P:		//	保稅轉完稅
//	    case OUT:    		// 	出倉
//		if("F".equals(taxType)){	// 保稅
//		    if( ( OrderStatus.SAVE.equals(head.getStatus()) ) || OrderStatus.REJECT.equals(head.getStatus()) ){
//			head.setStatus(OrderStatus.SIGNING);
//		    }else if( OrderStatus.SIGNING.equals(head.getStatus()) ){
//			if( "true".equals(approvalResult) ){
//			    head.setStatus(OrderStatus.FINISH);
//			}else{
//			    head.setStatus(OrderStatus.REJECT);
//			}
//		    }
//		}else if("P".equals(taxType)){	// 完稅
//		    if( OrderStatus.SAVE.equals(head.getStatus()) ){
//			head.setStatus(OrderStatus.FINISH);
//		    }
//		}
//		break;
//		// 商檢抽樣
//	    default: // 百貨
//		if( OrderStatus.SAVE.equals(head.getStatus()) ){
//		    head.setStatus(OrderStatus.FINISH);
//		}
//	    break;
//	    }
	}
    }

    /**
     * 一般調整單取得下個狀態
     */
    public void setReverseNextStatus(ImAdjustmentHead head){

	int AdjustmentType = NumberUtils.getInt( head.getAdjustmentType());
//	String taxType = head.getTaxType();

	switch (AdjustmentType) {
//	case PROFIT: 	//
//	case LOSS:		//
//	case ADJUST_COST: 	//
//	case ERASE_ACCOUNT:	//
//	    if( OrderStatus.CLOSE.equals(head.getStatus()) ){
//		head.setStatus(OrderStatus.SAVE);
//	    }else if(OrderStatus.FINISH.equals(head.getStatus()) ){
//		head.setStatus(OrderStatus.SAVE);
//	    }
//	    break;
//	case DISCARD: 		//	報廢
//	case F2P:		//	保稅轉完稅
//	case OUT:    		// 	出倉
//	    if("F".equals(taxType)){	// 保稅
//		if( OrderStatus.CLOSE.equals(head.getStatus()) ){
//		    head.setStatus(OrderStatus.SAVE);
//		}else if(OrderStatus.FINISH.equals(head.getStatus()) ){
//		    head.setStatus(OrderStatus.SAVE);
//		}
//	    }else if("P".equals(taxType)){	// 完稅
//		if( OrderStatus.CLOSE.equals(head.getStatus()) ){
//		    head.setStatus(OrderStatus.SAVE);
//		}else if(OrderStatus.FINISH.equals(head.getStatus()) ){
//		    head.setStatus(OrderStatus.SAVE);
//		}
//	    }
//	    break;
	default: // 百貨, 盤盈, 盤虧, 調成本, 領用除帳, 報廢, 保稅轉完稅, 出入倉
	    if( OrderStatus.CLOSE.equals(head.getStatus()) ){
		head.setStatus(OrderStatus.SAVE);
	    }else if(OrderStatus.FINISH.equals(head.getStatus()) ){
		head.setStatus(OrderStatus.SAVE);
	    }
	break;
	}
    }

    /**
     * 拆/併貨調整單依formAction取得下個狀態
     */
    public void setApartNextStatus(ImAdjustmentHead head, String formAction){

	if(OrderStatus.FORM_SAVE.equals(formAction)){
	    head.setStatus(OrderStatus.SAVE);
	}else if( (OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction)) ){
	    if( OrderStatus.SAVE.equals(head.getStatus()) ){
		head.setStatus(OrderStatus.FINISH);
	    }
	}else if(OrderStatus.FORM_VOID.equals(formAction)){
	    head.setStatus(OrderStatus.VOID);
	}
    }


    /**
     * 若是暫存單號,則取得新單號
     * @param head
     */
    private void setOrderNo(ImAdjustmentHead head) throws ObtainSerialNoFailedException{
    	String orderNo = head.getOrderNo();
    	if (AjaxUtils.isTmpOrderNo(orderNo)) {
    		try {
    			String serialNo = buOrderTypeService.getOrderSerialNo(head.getBrandCode(), head.getOrderTypeCode());
    			if ("unknow".equals(serialNo)){
    				throw new ObtainSerialNoFailedException("取得" + head.getBrandCode() + "-" + head.getOrderTypeCode() + "單號失敗！");
    			}else{
    				
    				//for 儲位用
    				if(imStorageAction.isStorageExecute(head)){
    					//取得儲位單正式的單號 2011.11.11 by Caspar
    					ImStorageHead imStorageHead = imStorageAction.updateOrderNo(head);
    				
    					head.setOrderNo(serialNo);
    					//更新儲位單SOURCE ORDER_NO
    					imStorageHead.setSourceOrderNo(head.getOrderNo());
    					imStorageService.updateHead(imStorageHead, head.getLastUpdatedBy());
    				}else{
    					head.setOrderNo(serialNo);
    				}
    				if(head.getOrderTypeCode().equals("BEF") && !StringUtils.hasText(head.getResNo())){
    					if(head.getProcessCustCd().equals("KW"))
    						this.saveIslandResNo(head);	//取得重整申請書號碼
    					else
    						this.saveResNo(head);
    					//insertToTradeVan(head);
    				}
    				//head.setCustomsStatus("A01");	
					head.setCreationDate(new Date());
					head.setCreatedBy(head.getCreatedBy());
    			}
    		} catch (Exception ex) {
    			throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode() + "單號失敗！");
    		}
    	}
    }

    /**
     * 設定中文狀態名稱
     * @param imAdjustmentHeads
     */
    private void setImAdjustmentStatusName(List<ImAdjustmentHead> imAdjustmentHeads){
		for (ImAdjustmentHead imAdjustmentHead : imAdjustmentHeads) {
		    imAdjustmentHead.setStatusName(OrderStatus.getChineseWord(imAdjustmentHead.getStatus()));
		    if(imAdjustmentHead.getOrderTypeCode().equals("BEF")){
		    	if(imAdjustmentHead.getSourceOrderTypeCode().equals("1")){
			    	imAdjustmentHead.setSourceOrderTypeCode("整理");
			    }else{
			    	imAdjustmentHead.setSourceOrderTypeCode("分割");
			    }
		    }		    	    
		}
    }

    /**
     * 更新bean
     * @param head
     */
    public void update(ImAdjustmentHead head) throws Exception{
	try {
	    log.info("head.getDeclarationNo() = " +head.getDeclarationNo());
	    imAdjustmentHeadDAO.update(head);
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("e = " + e.toString());
	}
    }

    /**
     * set head of line by cmDeclarationHead of cmDeclarationLine
     * @param head
     * @param cmDeclarationHead
     */
    private void updateImAdjustmentLineFromCmDeclarationItem(ImAdjustmentHead head, CmDeclarationHead cmDeclarationHead, Properties httpRequest, Properties properties ){
    	try{

    		head.setImAdjustmentLines(null);
    		String brandCode = head.getBrandCode();

    		String sourceOrderTypeCode =  httpRequest.getProperty("sourceOrderTypeCode");
    		String sourceOrderNo = httpRequest.getProperty("sourceOrderNo");
    		String warehouseCode = httpRequest.getProperty("defaultWarehouseCode");

    		log.info("warehouseCode = " + warehouseCode);
    		log.info("brandCode = " + brandCode);
    		log.info("sourceOrderTypeCode = " + sourceOrderTypeCode);
    		log.info("sourceOrderNo = " + sourceOrderNo);

    		Long i1 = 0L;
    		List<ImAdjustmentLine> lines1 = new ArrayList<ImAdjustmentLine>();
    		List<CmDeclarationItem> cmDeclarations = cmDeclarationHead.getCmDeclarationItems();
    		for (CmDeclarationItem cmDeclarationItem1 : cmDeclarations) {
    			ImAdjustmentLine imLine = new ImAdjustmentLine();
    			imLine.setImAdjustmentHead(head);
    			
    			log.info("DeclarationNo:"+cmDeclarationItem1.getDeclNo());
    			log.info("DeclarationNo:"+cmDeclarationItem1.getItemNo());
    			CmDeclarationOnHand cmDeclarationOnHand = 
    				(CmDeclarationOnHand)cmDeclarationOnHandDAO.findFirstByProperty("CmDeclarationOnHand", "and brandCode = ? and customsWarehouseCode = ? and declarationNo = ? and declarationSeq = ?"
    						, new Object[]{brandCode, "FW", cmDeclarationItem1.getDeclNo(), cmDeclarationItem1.getItemNo()});
    			//Double currentQty = cmDeclarationOnHandDAO.getCurrentOnHandQtyByProperty(brandCode, cmDeclarationItem1.getDeclNo(), cmDeclarationItem1.getItemNo(), "", "FW");
    		    log.info("cmDeclarationOnHand:::"+cmDeclarationOnHand);
    			imLine.setItemCode(cmDeclarationOnHand.getItemCode());
    			imLine.setDifQuantity(cmDeclarationItem1.getQty());
    			imLine.setWarehouseCode(warehouseCode); // 庫別
    			imLine.setOriginalDeclarationNo(cmDeclarationItem1.getDeclNo()); // D8報單單號
    			imLine.setOriginalDeclarationSeq(cmDeclarationItem1.getItemNo()); // D8報單項次
    			imLine.setOriginalDeclarationDate(cmDeclarationItem1.getODeclDate());
    			imLine.setCustomsItemCode(cmDeclarationOnHand.getCustomsItemCode()); // 海關品號
    			imLine.setLocalUnitCost(cmDeclarationItem1.getUnitPrice()); // 售價
    			imLine.setWeight(cmDeclarationItem1.getNWght());// 重量
    			imLine.setLotNo("000000000000");
				
    			lines1.add(imLine);
    			imLine.setIndexNo(i1+1);
    			i1++;
    			imAdjustmentLineDAO.save(imLine);
    		}
    		    
    		
    		    
    		ImAdjustmentHead sourceHead = (ImAdjustmentHead)imAdjustmentHeadDAO.findFirstByProperty("ImAdjustmentHead", "and brandCode = ? and orderTypeCode = ? and orderNo = ?", new Object[]{brandCode, sourceOrderTypeCode, sourceOrderNo});
    		if(null == sourceHead){
    		    properties.setProperty("sourceOrderTypeNoMemo", MessageStatus.DATA_NOT_FOUND);
//    		    throw new Exception("查無品牌:"+brandCode+" 來源單別:" + sourceOrderTypeCode +" 來源單號:"+ sourceOrderTypeNo + "資料");
    		}else{
    		    // 關別()
    		    String sourceWarehouseCode = sourceHead.getDefaultWarehouseCode();

    		    ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, sourceWarehouseCode, "Y");
    		    String customsWarehouseCode = null;
    		    if( null != imWarehouse   ){
    			// 入庫倉屬於的關 cmMoveWarehouseTo
    			customsWarehouseCode = imWarehouse.getCustomsWarehouseCode();
    			if(!StringUtils.hasText(sourceWarehouseCode)){
    			    throw new ValidationErrorException("原出倉的單號之庫別(" + sourceWarehouseCode + ")的海關關別未設定！");
    			}
    		    }else{
    			throw new NoSuchObjectException("依據品牌(" + brandCode + ")、庫別(" + sourceWarehouseCode + ")查無庫別相關資料！");
    		    }

    		    Long i = 0L;
    		    List<ImAdjustmentLine> lines = new ArrayList<ImAdjustmentLine>();
    		    List<CmDeclarationItem> cmDeclarationItems = cmDeclarationHead.getCmDeclarationItems();
//    		    List<ImAdjustmentLine> sourceLines = sourceHead.getImAdjustmentLines();
    		    for (CmDeclarationItem cmDeclarationItem : cmDeclarationItems) {
    				ImAdjustmentLine line = new ImAdjustmentLine();
    				line.setImAdjustmentHead(head);
    		
    				String originalDeclarationNoD7 = cmDeclarationItem.getDeclNo(); // D7報關單號
    				Long originalDeclarationSeqD7 = cmDeclarationItem.getItemNo(); // D7原報關項次
    				Double qty = cmDeclarationItem.getQty();
    				Double unitPrice = cmDeclarationItem.getUnitPrice();
    		
    				String originalDeclarationNoD8 = cmDeclarationItem.getODeclNo(); // D7報單來源報單紀錄著D8報關單號
    				Long originalDeclarationSeqD8 = NumberUtils.getLong(cmDeclarationItem.getOItemNo()); // D7報單來源報單紀錄著D8原報關項次
    		
    				log.info("brandCode = " + brandCode);
    				log.info("customsWarehouseCode = " + customsWarehouseCode);
    				log.info("D7 originalDeclarationNoD7 = " + originalDeclarationNoD7);
    				log.info("D7 originalDeclarationSeqD7 = " + originalDeclarationSeqD7);
    				log.info("D8 originalDeclarationNoD8 = " + originalDeclarationNoD8);
    				log.info("D8 originalDeclarationSeqD8 = " + originalDeclarationSeqD8);
    		
    				CmDeclarationOnHand cmDeclarationOnHand = (CmDeclarationOnHand)cmDeclarationOnHandDAO.findFirstByProperty("CmDeclarationOnHand", "and brandCode = ? and customsWarehouseCode = ? and declarationNo = ? and declarationSeq = ?", new Object[]{brandCode, customsWarehouseCode, originalDeclarationNoD8, originalDeclarationSeqD8});
    				if( null == cmDeclarationOnHand ){
    				    throw new Exception("查無品牌:"+brandCode+" 關別" + customsWarehouseCode + " D8報關單號:" + originalDeclarationNoD8 +" D8報單項次:"+ originalDeclarationSeqD8 + "資料");
    				}
    		
    				line.setItemCode(cmDeclarationOnHand.getCustomsItemCode()); // 品號
    				line.setLotNo(SystemConfig.LOT_NO); // 批號固定12碼?
    		
    				line.setWarehouseCode(warehouseCode); // 庫別
    				line.setOriginalDeclarationNo(originalDeclarationNoD8); // D8報單單號
    				line.setOriginalDeclarationSeq(originalDeclarationSeqD8); // D8報單項次
    		
    				line.setCustomsItemCode(cmDeclarationOnHand.getCustomsItemCode()); // 海關品號
    				line.setDifQuantity( qty ); // 報單數量一定是正數的
    				line.setLocalUnitCost(unitPrice); // 售價
    				line.setWeight(cmDeclarationItem.getNWght());// 重量
    		
    				line.setIsDeleteRecord(AjaxUtils.IS_DELETE_RECORD_FALSE);
    				line.setIsLockRecord(AjaxUtils.IS_LOCK_RECORD_FALSE);
    				lines.add(line);
    				line.setIndexNo(i+1);
    				i++;
    				imAdjustmentLineDAO.save(line);
    			    }

    		    head.setImAdjustmentLines(lines);
    		    imAdjustmentHeadDAO.update(head);
    		}
    	    
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    /**
     * 啟動流程
     * @param form
     * @return
     * @throws ProcessFailedException
     */
    public static Object[] startProcess(ImAdjustmentHead form,Map resultMap) throws ProcessFailedException{
	String packageId = null;
	String processId = null;
	String version = null;
	String sourceReferenceType = null;
	HashMap context = new HashMap();
	try{
//	    String taxType = form.getTaxType();
//	    int adjustmentType = NumberUtils.getInt(form.getAdjustmentType());
	    BuCommonPhraseLine processConfig = (BuCommonPhraseLine)resultMap.get("processConfig");

	    packageId = processConfig.getAttribute1();
	    processId = processConfig.getAttribute2();
	    version = processConfig.getAttribute3();
	    sourceReferenceType = processConfig.getAttribute4();

//	    packageId = "Im_GeneralAdjustment";
//	    if("F".equals(taxType) && ( adjustmentType == DISCARD || adjustmentType == F2P || adjustmentType == OUT) ){
//		if( adjustmentType == DISCARD || adjustmentType == F2P ){
//		    processId = "approvalChangeDecl"; // approval
//		    version = "20100809"; //20091104
//		    sourceReferenceType = "ImGeneralChg"; // ImGeneralApr
//
//		}else if( adjustmentType == OUT ){
//		    processId = "approvalIsland";
//		    version = "20100813"; //20100423
//		    sourceReferenceType = "Aprlsland";
//		}
//	    }else{
//		if( adjustmentType == IN ){
//		    processId = "processIsland";
//		    version = "20100423";
//		    sourceReferenceType = "Adjlsland";
//		}else{
//		    processId = "process";
//		    version = "20091102";
//		    sourceReferenceType = "ImGeneralAdj";
//		}
//	    }
	    /*if(form.getOrderTypeCode().equals("ICA")){
	    	log.info("ICA拆併單流程");
	    	 version = "20150817";
			 sourceReferenceType = "ImGeneralAdj";
			
			 context.put("brandCode", form.getBrandCode());
		     context.put("formId",    form.getHeadId());	    	
	    }*/
	    
	    context.put("formId", form.getHeadId());
	    return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
	}catch (Exception ex){
	    ex.printStackTrace();
	    log.error("調整單流程啟動失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("調整單流程啟動失敗！"+ex.getMessage());
	}
    }

    /**
     * 重排line
     * @param head
     * @param taxType
     */
    public void sortImAdjustmentLine(ImAdjustmentHead head, String taxType){
	List<ImAdjustmentLine> imAdjustmentLines = imAdjustmentLineDAO.findByProperty("ImAdjustmentLine", new String[]{ "imAdjustmentHead.headId" }, new Object[]{ head.getHeadId() }, "boxNo, itemCode" );
	Long i = 1L;
	for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
	    imAdjustmentLine.setIndexNo( i );
	    i = i + 1L;
	}
	head.setImAdjustmentLines( imAdjustmentLines );

    }

    /**
     * ajax 取得報關單主檔
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXByCmDeclarationData(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {
	    String brandCode = httpRequest.getProperty("brandCode");
	    String declarationNo = httpRequest.getProperty("declarationNo");

	    log.info( "取得報關單");
	    log.info( "brandCode = " + brandCode );
	    log.info( "declarationNo = " + declarationNo );

	    if( StringUtils.hasText(declarationNo) ){

		CmDeclarationHead cmDeclarationHead = this.findOneCmDeclaration(declarationNo);
		if(cmDeclarationHead != null){

		    properties.setProperty("declarationNo", declarationNo);
		    properties.setProperty("declarationNoMemo", "");
		    properties.setProperty("declarationDate", DateUtils.format( cmDeclarationHead.getDeclDate(), "yyyy/MM/dd"));
		    properties.setProperty("declarationType", cmDeclarationHead.getDeclType());


		} else {
		    properties.setProperty("declarationNo", declarationNo);
		    properties.setProperty("declarationNoMemo", "");
		    properties.setProperty("declarationDate", "");
		    properties.setProperty("declarationType", "");
		}
	    }else{
		properties.setProperty("declarationNo", declarationNo);
		properties.setProperty("declarationNoMemo", "");
		properties.setProperty("declarationDate", "");
		properties.setProperty("declarationType", "");
	    }
	    result.add(properties);
	    return result;

	} catch (Exception ex) {
	    log.error("取得報關單資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得報關單資料失敗！");
	}

    }

    /**
     * ajax 將head庫別,塞回line庫別
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXWarehouseCode(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {
	    Long headId = NumberUtils.getLong( httpRequest.getProperty("headId") );
	    String defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode");

	    log.info( "取得庫別");
	    log.info( "defaultWarehouseCode = " + defaultWarehouseCode );

	    ImAdjustmentHead imAdjustmentHead = this.executeFind(headId);
	    if(imAdjustmentHead != null){
		List<ImAdjustmentLine> imAdjustmentLines = imAdjustmentHead.getImAdjustmentLines();
		for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
		    if( StringUtils.hasText(defaultWarehouseCode) ){
			imAdjustmentLine.setWarehouseCode(defaultWarehouseCode);
		    }else{
			imAdjustmentLine.setWarehouseCode("");
		    }
		    imAdjustmentLineDAO.save(imAdjustmentLine);
		}
	    }

	    result.add(properties);
	    return result;

	} catch (Exception ex) {
	    log.error("取得調整單資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得調整單資料失敗！");
	}

    }

//  public List<Properties> updateAJAXImItemByAdjustmentType (Properties httpRequest) throws Exception{
//  List<Properties> result = new ArrayList();
//  Properties properties = new Properties();
//  try {
//  Long headId = NumberUtils.getLong( httpRequest.getProperty("headId") );
//  String adjustmentType = httpRequest.getProperty("adjustmentType");

//  log.info( "adjustmentType = " + adjustmentType );

//  ImAdjustmentHead imAdjustmentHead = this.executeFind(headId);
//  if(imAdjustmentHead != null){
//  List<ImAdjustmentLine> imAdjustmentLines = imAdjustmentHead.getImAdjustmentLines();
//  for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
//  if( LOSS == Integer.valueOf(adjustmentType)){

//  }else if(){

//  }
//  imAdjustmentLineDAO.save(imAdjustmentLine);
//  }
//  }

//  result.add(properties);
//  return result;

//  } catch (Exception ex) {
//  log.error("更新調整單明細資料發生錯誤，原因：" + ex.toString());
//  throw new Exception("更新調整單明細資料失敗！");
//  }
//  }

    /**
     * ajax  更新調整單明細
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception{
	String errorMsg = null;
	try {
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    String status = httpRequest.getProperty("status");
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	    Long adjustmentType = NumberUtils.getLong(httpRequest.getProperty("adjustmentType"));
	    String userType = AjaxUtils.getPropertiesValue(httpRequest.getProperty("userType"),"");

	    if (headId == null) {
		throw new ValidationErrorException("傳入的調整單主鍵為空值！");
	    }

	    // 將STRING資料轉成List Properties record data
	    List<Properties> upRecords = null;
	    if(orderTypeCode.equals("MEG") || orderTypeCode.equals("MEF") || orderTypeCode.equals("MEP")|| orderTypeCode.equals("MDF") || orderTypeCode.equals("MDP")){
	    	    upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, MEG_GRID_FIELD_NAMES);
		}else if(orderTypeCode.equals("AIR")){
	    	    upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, AIR_GRID_FIELD_NAMES);
		}else {
		    upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
		}
	    	
	    // Get INDEX NO 取得最後一筆 INDEX
	    int indexNo = imAdjustmentLineDAO.findPageLineMaxIndex(headId).intValue();

//	    List<ImAdjustmentLine> result = baseDAO.findByProperty("ImAdjustmentLine", "headId", headId, "indexNo");
//	    int indexNo = result.get( result.size() - 1 ).getIndexNo().intValue();
	    log.info( "MaxIndexNo = " + indexNo );

	    if(OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)
		    || ("AEF".equals(orderTypeCode) && F2P == adjustmentType && OrderStatus.SIGNING.equals(status) && CREATOR.equalsIgnoreCase(userType))
		    || ("MEF".equals(orderTypeCode) && OUT == adjustmentType && OrderStatus.SIGNING.equals(status) && CREATOR.equalsIgnoreCase(userType))
		    || ("ADF".equals(orderTypeCode) && DISCARD == adjustmentType && OrderStatus.SIGNING.equals(status)) && CREATOR.equalsIgnoreCase(userType)){
		// 考慮狀態
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
			// 先載入HEAD_ID OR LINE DATA
		    	String itemCode = "";
			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
			if(orderTypeCode.equals("MEG") || orderTypeCode.equals("MEF") || orderTypeCode.equals("MEP")|| orderTypeCode.equals("MDF")|| orderTypeCode.equals("MDP")){
				itemCode = upRecord.getProperty(MEG_GRID_FIELD_NAMES[1]);
			}else if(orderTypeCode.equals("AIR")){
				itemCode = upRecord.getProperty(AIR_GRID_FIELD_NAMES[1]);
			}else {
				itemCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);
			}
			
			if (StringUtils.hasText(itemCode)) {

			    ImAdjustmentLine imAdjustmentLine = imAdjustmentLineDAO.findFirstByProperty("ImAdjustmentLine", "and imAdjustmentHead.headId = ? and lineId = ?", new Object[]{ headId, lineId } );
			    log.info( "imAdjustmentLine = " + imAdjustmentLine + "\nlineId = " + lineId);
			    Date date = new Date();
			    if ( imAdjustmentLine != null ) {
				log.info( "更新 = " + headId + " | "+ lineId  );
				if(orderTypeCode.equals("MEG") || orderTypeCode.equals("MEF") || orderTypeCode.equals("MEP")|| orderTypeCode.equals("MDF")|| orderTypeCode.equals("MDP")){
					AjaxUtils.setPojoProperties(imAdjustmentLine,upRecord, MEG_GRID_FIELD_NAMES, MEG_GRID_FIELD_TYPES);
				}else if(orderTypeCode.equals("AIR")){
					AjaxUtils.setPojoProperties(imAdjustmentLine,upRecord, AIR_GRID_FIELD_NAMES, AIR_GRID_FIELD_TYPES);
				}else{
					AjaxUtils.setPojoProperties(imAdjustmentLine,upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
				}
				imAdjustmentLine.setLastUpdatedBy(loginEmployeeCode);
				imAdjustmentLine.setLastUpdateDate(date);
				imAdjustmentLineDAO.update(imAdjustmentLine);

			    } else {
				indexNo++;
				log.info( "新增 = " + headId + " | "+  indexNo);
				imAdjustmentLine = new ImAdjustmentLine();
				if(orderTypeCode.equals("MEG") || orderTypeCode.equals("MEF") || orderTypeCode.equals("MEP")|| orderTypeCode.equals("MDF")|| orderTypeCode.equals("MDP")){
					AjaxUtils.setPojoProperties(imAdjustmentLine,upRecord, MEG_GRID_FIELD_NAMES,MEG_GRID_FIELD_TYPES);
				}else if(orderTypeCode.equals("AIR")){
					AjaxUtils.setPojoProperties(imAdjustmentLine,upRecord, AIR_GRID_FIELD_NAMES,AIR_GRID_FIELD_TYPES);
				}else{
					AjaxUtils.setPojoProperties(imAdjustmentLine,upRecord, GRID_FIELD_NAMES,GRID_FIELD_TYPES);
				}
				imAdjustmentLine.setImAdjustmentHead(new ImAdjustmentHead(headId));
				imAdjustmentLine.setCreatedBy(loginEmployeeCode);
				imAdjustmentLine.setCreationDate(date);
				imAdjustmentLine.setLastUpdatedBy(loginEmployeeCode);
				imAdjustmentLine.setLastUpdateDate(date);
				imAdjustmentLine.setIndexNo(Long.valueOf(indexNo));
				imAdjustmentLineDAO.save(imAdjustmentLine);

			    }
			}
		    }
		}
	    }
	    return AjaxUtils.getResponseMsg(errorMsg);

	} catch (Exception ex) {
	    log.error("更新調整單明細資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新調整單明細資料失敗！");
	}
    }

    /**
     * 聚集相同商品
     * @param imAdjustmentHead
     * @param employeeCode
     */
    private void updateAggregateStock(ImAdjustmentHead imAdjustmentHead, String employeeCode, List errorMsgs, boolean isReverse, String beforeStatus) throws FormException, Exception {

		String errorMsg = null;
		String brandCode = imAdjustmentHead.getBrandCode();
		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(imAdjustmentHead.getBrandCode());
		String orderTypeCode = imAdjustmentHead.getOrderTypeCode();
		String identification = MessageStatus.getIdentification(brandCode, orderTypeCode, imAdjustmentHead.getOrderNo());
		// ==============相同的商品、庫別、批號產生產生集合、相同的報單號碼、報單項次、商品、關別集合============
		Set[] aggregateResult = aggregateOrderItemsQty(imAdjustmentHead, employeeCode, isReverse);
		Iterator it = aggregateResult[0].iterator(); // ImOnHand扣庫存用
		Iterator cmIt = aggregateResult[1].iterator(); // CmOnHand扣庫存用
		// ======================================預扣報單庫存量=======================================================
		while (cmIt.hasNext()) {
			try {
				Map.Entry cmEntry = (Map.Entry) cmIt.next();
				Double unCommitQty = (Double) cmEntry.getValue();
				log.info("(String) cmEntry.getKey() :"+(String) cmEntry.getKey());
				log.info("(Double) cmEntry.getValue() :"+unCommitQty);
				String[] cmkeyArray = StringUtils.delimitedListToStringArray((String) cmEntry.getKey(), "{$}");

				if (OrderStatus.CLOSE.equalsIgnoreCase(beforeStatus) && isReverse) {
					cmDeclarationOnHandDAO.updateStockOnHandByOther(cmkeyArray[0], Long.valueOf(cmkeyArray[1]),
							cmkeyArray[2], cmkeyArray[3], brandCode, unCommitQty, employeeCode); // modified by Weichun 2011.09.27
				} else { // FINISH
					cmDeclarationOnHandDAO.updateOtherUncommitQuantity(cmkeyArray[0], Long.valueOf(cmkeyArray[1]),
							cmkeyArray[2], cmkeyArray[3], brandCode, unCommitQty, employeeCode, imAdjustmentHead
									.getUnBlockOnHand()); // modified by Weichun 2011.09.27
				}
			} catch (Exception ex) {
				errorMsg = "預扣" + identification + "的報單庫存量時發生錯誤，原因："+ex.toString();
				log.error(errorMsg + ex.toString());
				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, errorMsg
						+ ex.getMessage(), employeeCode);
				//errorMsgs.add(errorMsg);
				throw new Exception(errorMsg);
			}
		}
		// ======================================預扣實體庫別庫存量=======================================================
		while (it.hasNext()) {
			try {
				Map.Entry entry = (Map.Entry) it.next();
				Double unCommitQty = (Double) entry.getValue();
				String[] keyArray = StringUtils.delimitedListToStringArray((String) entry.getKey(), "{$}");
				if (OrderStatus.CLOSE.equalsIgnoreCase(beforeStatus) && isReverse) {
					imOnHandDAO.updateStockOnHand(organizationCode, brandCode, keyArray[0], keyArray[1], keyArray[2],
							unCommitQty, employeeCode);
				} else { // FINISH
					imOnHandDAO.updateOtherUncommitQuantity(organizationCode, keyArray[0], keyArray[1], keyArray[2],
							brandCode, unCommitQty, employeeCode);
				}
			} catch (Exception ex) {
				errorMsg = "預扣" + identification + "庫別庫存量時發生錯誤，原因："+ ex.toString();
				log.error(errorMsg + ex.toString());
				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, errorMsg
						+ ex.getMessage(), employeeCode);
				errorMsgs.add(errorMsg);
				throw new Exception(errorMsg);
			}
		}
	}

    /**
     * ajax  更新拆/併貨調整單明細
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXApartPageLinesData(Properties httpRequest) throws Exception{
	String errorMsg = null;
	try {
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    String status = httpRequest.getProperty("status");

	    if (headId == null) {
		throw new ValidationErrorException("傳入的調整單主鍵為空值！");
	    }

	    // 將STRING資料轉成List Properties record data
	    List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_APART_NAMES);
	    // Get INDEX NO 取得最後一筆 INDEX
	    int indexNo = imAdjustmentLineDAO.findPageLineMaxIndex(headId).intValue();

//	    List<ImAdjustmentLine> result = baseDAO.findByProperty("ImAdjustmentLine", "headId", headId, "indexNo");
//	    int indexNo = result.get( result.size() - 1 ).getIndexNo().intValue();
	    log.info( "MaxIndexNo = " + indexNo );
	    log.info( "status = " + status );
	    log.info( "upRecords = " + upRecords.size() );

	    // 考慮狀態
	    if( OrderStatus.SAVE.equals(status) ){
	    	
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
			// 先載入HEAD_ID OR LINE DATA

			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));

			String itemCode = upRecord.getProperty(GRID_FIELD_APART_NAMES[2]);

			if (StringUtils.hasText(itemCode)) {

			    ImAdjustmentLine imAdjustmentLine = imAdjustmentLineDAO.findFirstByProperty("ImAdjustmentLine", "and imAdjustmentHead.headId = ? and lineId = ?", new Object[]{ headId, lineId } );
			    log.info( "imAdjustmentLine = " + imAdjustmentLine + "\nlineId = " + lineId);
			    Date date = new Date();
			    if ( imAdjustmentLine != null ) {
				log.info( "更新 = " + headId + " | "+ lineId  );
				AjaxUtils.setPojoProperties(imAdjustmentLine,upRecord, GRID_FIELD_APART_NAMES, GRID_FIELD_APART_TYPES);
				imAdjustmentLine.setLastUpdatedBy(loginEmployeeCode);
				imAdjustmentLine.setLastUpdateDate(date);

				imAdjustmentLineDAO.update(imAdjustmentLine);

			    } else {
				indexNo++;
				log.info( "新增 = " + headId + " | "+  indexNo);
				imAdjustmentLine = new ImAdjustmentLine();

				AjaxUtils.setPojoProperties(imAdjustmentLine,upRecord, GRID_FIELD_APART_NAMES,GRID_FIELD_APART_TYPES);
				imAdjustmentLine.setImAdjustmentHead(new ImAdjustmentHead(headId));
				imAdjustmentLine.setCreatedBy(loginEmployeeCode);
				imAdjustmentLine.setCreationDate(date);
				imAdjustmentLine.setLastUpdatedBy(loginEmployeeCode);
				imAdjustmentLine.setLastUpdateDate(date);
				imAdjustmentLine.setIndexNo(Long.valueOf(indexNo));

				imAdjustmentLineDAO.save(imAdjustmentLine);

			    }
			}
		    }
		}

	    }

	    return AjaxUtils.getResponseMsg(errorMsg);

	} catch (Exception ex) {
	    log.error("更新拆/併貨調整單明細資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新拆/併貨調整單明細資料失敗！");
	}
    }

    /**
     * ajax 取得報關單主檔 D7報單
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXByD7CmDeclarationData(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {
	    String brandCode = httpRequest.getProperty("brandCode");
	    String declarationNo = httpRequest.getProperty("declarationNo");
	    String declType = httpRequest.getProperty("declType");
	    int adjustmentType = NumberUtils.getInt(httpRequest.getProperty("adjustmentType"));

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String warehouseCode = httpRequest.getProperty("defaultWarehouseCode");

	    String sourceOrderTypeCode = httpRequest.getProperty("sourceOrderTypeCode");
	    String sourceOrderNo = httpRequest.getProperty("sourceOrderNo");

	    log.info( "httpRequest = " + httpRequest );

	    if( StringUtils.hasText(declarationNo) ){
		CmDeclarationHead cmDeclarationHead = findOneCmDeclaration(declarationNo, declType, "");
		if(cmDeclarationHead != null){
		    if(OUT == adjustmentType){
			if(!OrderStatus.WAIT_IN.equals(cmDeclarationHead.getStatus())){
			    properties.setProperty("declarationNo", declarationNo);
			    properties.setProperty("declarationNoMemo", "報單狀態不為" + OrderStatus.getChineseWord(OrderStatus.WAIT_IN));
			    properties.setProperty("declarationDate", "");
			}else{
			    ImAdjustmentHead head = executeFind(headId);
			    properties.setProperty("declarationNo", declarationNo);
			    properties.setProperty("declarationNoMemo", "");
			    properties.setProperty("declarationDate", DateUtils.format( cmDeclarationHead.getDeclDate(), "yyyy/MM/dd"));
			}
		    }else if(IN == adjustmentType){
			if(!OrderStatus.FINISH.equals(cmDeclarationHead.getStatus())){
			    properties.setProperty("declarationNo", declarationNo);
			    properties.setProperty("declarationNoMemo", "報單狀態不為" + OrderStatus.getChineseWord(OrderStatus.FINISH));
			    properties.setProperty("declarationDate", "");
			}else{
			    ImAdjustmentHead head = executeFind(headId);
			    updateImAdjustmentLineFromCmDeclarationItem(head, cmDeclarationHead, httpRequest, properties);

			    properties.setProperty("declarationNo", declarationNo);
			    properties.setProperty("declarationNoMemo", "");
			    properties.setProperty("declarationDate", DateUtils.format( cmDeclarationHead.getDeclDate(), "yyyy/MM/dd"));
			}
		    }else{
			properties.setProperty("declarationNo", declarationNo);
			properties.setProperty("declarationNoMemo", "調整類別異常");
			properties.setProperty("declarationDate", DateUtils.format( cmDeclarationHead.getDeclDate(), "yyyy/MM/dd"));
		    }

		} else {
		    properties.setProperty("declarationNo", declarationNo);
		    properties.setProperty("declarationNoMemo", MessageStatus.DATA_NOT_FOUND);
		    properties.setProperty("declarationDate", "");
		}
	    }else{
		properties.setProperty("declarationNo", declarationNo);
		properties.setProperty("declarationNoMemo", MessageStatus.DATA_NOT_FOUND);
		properties.setProperty("declarationDate", "");
	    }
	    result.add(properties);
	    return result;

	} catch (Exception ex) {
	    log.error("取得報關單資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得報關單資料失敗！");
	}

    }

    /**
     * ajax 用來源單別單號取得明細
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXBySourceOrderNo(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {
	    String brandCode = httpRequest.getProperty("brandCode");
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String warehouseCode = httpRequest.getProperty("warehouseCode");

	    String sourceOrderTypeCode = httpRequest.getProperty("sourceOrderTypeCode");
	    String sourceOrderNo = httpRequest.getProperty("sourceOrderNo");

	    log.info( "brandCode = " + brandCode );
	    log.info( "headId = " + headId );
	    log.info( "warehouseCode = " + warehouseCode );
	    log.info( "sourceOrderTypeCode = " + sourceOrderTypeCode );
	    log.info( "sourceOrderNo = " + sourceOrderNo );

	    if( !StringUtils.hasText(sourceOrderTypeCode)){
		properties.setProperty("sourceOrderTypeNoMemo", "未輸入來源單別");
	    }else if(!StringUtils.hasText(sourceOrderNo)){
		properties.setProperty("sourceOrderTypeNoMemo", "未輸入來源單號");
	    }else{
		ImAdjustmentHead head = executeFind(headId);
//		updateImAdjustmentLineFromCmDeclarationItem(head, httpRequest, properties);
	    }

	    result.add(properties);
	    return result;

	} catch (Exception ex) {
	    log.error("取得明細資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得明細資料失敗！");
	}

    }

    /**
     * 檢核,存取調整單主檔,明細檔,重新設定狀態
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map updateImGeneralAdjustment(Map parameterMap)throws Exception{
    log.info("updateImGeneralAdjustment");
	Map resultMap = new HashMap();
	List errorMsgs = null;
	String resultMsg = null;
	try {

	    Object otherBean = parameterMap.get("vatBeanOther");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

	    ImAdjustmentHead head = this.getActualImAdjustment(formLinkBean);

	    // 由於不管選何種調整類別，都要透過調整單配置檔知道跑哪個流程，
	    // 因此前端JS有作應控制如下1,2
	    // 1. T2的調整類別必填
	    // 2. 已填過的調整類別無法再修改
	    getProcessConfig(head,parameterMap,resultMap);

	    // 送出或背景送出
	    if( OrderStatus.FORM_SUBMIT.equalsIgnoreCase(formAction) || "SUBMIT_BG".equalsIgnoreCase(formAction) ){

			// 刪明細
			this.deleteLine(head);
			
			// 檢核
			errorMsgs  = this.checkedImGeneralAdjustment(parameterMap);
	    }

	    // errorMsgs == null 表示暫存
	    // errorMsgs.size() == 0 表示送出或背景送出
	    if( errorMsgs == null || errorMsgs.size() == 0 ){
	
			// 設定單號
			this.setOrderNo(head);
	
			// 成功則設定下個狀態
			this.setNextStatus(parameterMap, head); // , formAction, approvalResult, adjustmentTypeConfig
			// 保稅則重排line
	//		this.sortImAdjustmentLine(head, taxType);
	
			// 更新調整單主檔明細檔,im和cm庫存
			if( ( OrderStatus.FORM_SUBMIT.equalsIgnoreCase(formAction) || "SUBMIT_BG".equalsIgnoreCase(formAction) || "ARCHIVE".equalsIgnoreCase(formAction) )   ){
	
			    // 更新調整單主檔明細檔
			    this.updateImAdjustment(head, employeeCode );
			    // 依各種調整類別更新庫存
			    executeBusiness(parameterMap, head, errorMsgs);
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
		ve.printStackTrace();
	    log.error("調整單檢核時發生錯誤，原因：" + ve.toString());
	    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	} catch (Exception e) {
		e.printStackTrace();
	    log.error("調整單存檔時發生錯誤，原因：" + e.toString());
	    throw new Exception("調整單存檔時發生錯誤，原因：" + e.getMessage());
	}

	return resultMap;
    }

    /**
     * 調整單反轉
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Object executeReverter(Long headId, String employeeCode)throws Exception{
	List errorMsgs = null;
	ImAdjustmentHead head = null;
	Map resultMap = new HashMap();
	Map parameterMap = new HashMap();
	try {
	    head = executeFind(headId);

	    String brandCode = head.getBrandCode();
	    String orderTypeCode = head.getOrderTypeCode();
	    String orderNo = head.getOrderNo();

	    String beforeStatus = head.getStatus();

	    // 檢核是否關帳
	    if( !(OrderStatus.CLOSE.equalsIgnoreCase(beforeStatus) || OrderStatus.FINISH.equalsIgnoreCase(beforeStatus)) ){
		throw new Exception("品牌: " + brandCode + " 單別: " + orderTypeCode +  " 單號: " + orderNo + " 狀態不為CLOSE或FINISH，無法反轉");
	    }
	    ValidateUtil.isAfterClose(brandCode, orderTypeCode, "調整日期", head.getAdjustmentDate(),head.getSchedule());

	    getProcessConfig(head,parameterMap,resultMap);

	    // 檢核成功則設定下個狀態
	    setReverseNextStatus(head);

	    // 更新調整單主檔明細檔
	    updateImAdjustment(head, employeeCode );
	    // 依各種調整類別更新庫存
	    updateImGeneralAdjustmentStock(head, beforeStatus, employeeCode, errorMsgs, parameterMap );
	    // 清掉流程
	    head.setProcessId(null);

	    if(null != errorMsgs && errorMsgs.size() > 0 ){
		String identification = MessageStatus.getIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
		throw new Exception(identification + "更新庫存時發生錯誤");
	    }

	    log.info("head.status = " + head.getStatus() );

	    return head;
	} catch (Exception e) {
	    log.error("調整單存檔時發生錯誤，原因：" + e.toString());
	    throw new Exception(e.getMessage());
	}
    }

    /**
     * 依各種調整類別執行商業邏輯, 更新庫存
     * @param head
     * @param employeeCode
     * @param taxType
     * @param adjustmentType
     * @throws Exception
     */
    private void executeBusiness( Map parameterMap, ImAdjustmentHead head, List errorMsgs)throws Exception{

    	Object otherBean = parameterMap.get("vatBeanOther");
    	Object bindBean = parameterMap.get("vatBeanFormBind");
    	BuCommonPhraseLine adjustmentTypeConfig = (BuCommonPhraseLine)parameterMap.get("config");

    	String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    	String beforeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeStatus");
    	String userType = (String)PropertyUtils.getProperty(otherBean, "userType");
    	String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");

//  	String affectCost = (String)PropertyUtils.getProperty(bindBean, "affectCost");

//  	String taxType = head.getTaxType();
//  	int adjustmentType = NumberUtils.getInt(head.getAdjustmentType());

    	String business = adjustmentTypeConfig.getParameter1(); // 取得主要商業邏輯

    	// STOCK_INVOICE_REJECT_ARCHIVE_DECL，T2AEF51(保稅轉完稅)、T2ADF41(報廢)、T2MEF21(免稅F馬祖出倉)
    	// STOCK，其餘調整單皆為此類型參考adjustmentTypeConfig參數配置
    	// 若含有INVOICE的按鈕駁回要請使用者自行按按鈕駁回，目前不自動幫使用者刪除
    	
    	String identification = MessageStatus.getIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
    	
    	if(STOCK_INVOICE_REJECT_ARCHIVE_DECL.equalsIgnoreCase(business) ){
    		// 如果 主檔的報單有填,寫入invoice,扣庫存,則狀態save -> finish,
    		// 如果 主檔的報單沒填  狀態signing -> finish 只是修改備註欄位為新報單的資訊,以供海關人員追朔
    		// 若含decl,當狀態改成 FINISH將單頭報單WAIT_IN改寫為FINISH
    		if( (OrderStatus.SIGNING.equals(head.getStatus()) && OrderStatus.SAVE.equals(beforeStatus)) ||
    				(OrderStatus.SIGNING.equals(head.getStatus()) && OrderStatus.REJECT.equals(beforeStatus)) ){
    			// 扣庫存,寫入invoice
    			updateStocksAndInvoice(head, employeeCode, errorMsgs, beforeStatus);		// STOCK_INVOICE
    			
    			//for 儲位用
    			if(imStorageAction.isStorageExecute(head)){
    				//異動儲位庫存，目前只能送出增加庫存，若為駁回isReject應為true
    				boolean isReject = false;
    				imStorageService.updateStorageOnHandBySource(head, OrderStatus.FINISH, PROGRAM_ID, identification, isReject);
    			}
    			
    		}else if(OrderStatus.SIGNING.equals(head.getStatus()) && OrderStatus.SIGNING.equals(beforeStatus)){
    			if( CREATOR.equalsIgnoreCase(userType)){
    				if("ARCHIVE".equalsIgnoreCase(formAction) ){
    					update(head);								// ARCHIVE
    				}else if(OrderStatus.FORM_SUBMIT.equalsIgnoreCase(formAction) || "SUBMIT_BG".equalsIgnoreCase(formAction)){
    					// 回寫單頭的報關單號
    					Map map = new HashMap();
    					map.put("beforeStatus", OrderStatus.WAIT_IN);
    					map.put("afterStatus", OrderStatus.FINISH);
    					if(!(head.getOrderTypeCode().equals("MEF") && head.getAdjustmentType().equals("22"))) updateCmDeclaration(head, employeeCode, map);				// DECL
    				}
    			}

    		}else if( OrderStatus.REJECT.equals(head.getStatus())){
    			// rollback 庫存, delete invoice
    			// 更新 D2報單狀態 FINISH -> WAIT_IN

    			if( ACCOUNT.equalsIgnoreCase(userType) || FINANCEMGR.equalsIgnoreCase(userType) || FINANCE.equalsIgnoreCase(userType) ){
    				Map map = new HashMap();
    				map.put("beforeStatus", OrderStatus.FINISH);
    				map.put("afterStatus", OrderStatus.WAIT_IN);
    				updateCmDeclaration(head, employeeCode, map);				// DECL
    			}
    			updateStocks(head, employeeCode, errorMsgs, true, beforeStatus);		// STOCK

    			//for 儲位用
    			if(imStorageAction.isStorageExecute(head)){
    				//異動儲位庫存，目前只能送出增加庫存，若為駁回isReject應為true
    				boolean isReject = true;
    				imStorageService.updateStorageOnHandBySource(head, OrderStatus.SAVE, PROGRAM_ID, identification, isReject);
    			}

    		}else if( OrderStatus.FINISH.equals(head.getStatus()) ){
    			if( OrderStatus.SAVE.equals(beforeStatus) ){
    				Map map = new HashMap();
    				map.put("beforeStatus", OrderStatus.WAIT_IN);
    				map.put("afterStatus", OrderStatus.FINISH);
    				updateCmDeclaration(head, employeeCode, map);				// DECL
    				// 扣庫存,寫入invoice
    				updateStocksAndInvoice(head, employeeCode, errorMsgs, beforeStatus);	// STOCK_INVOICE

    				//for 儲位用
    				if(imStorageAction.isStorageExecute(head)){
    					//異動儲位庫存，目前只能送出增加庫存，若為駁回isReject應為true
    					boolean isReject = false;
    					imStorageService.updateStorageOnHandBySource(head, OrderStatus.FINISH, PROGRAM_ID, identification, isReject);
    				}
    			}else if(OrderStatus.SIGNING.equals(beforeStatus)){
    				// 不扣庫存
    			}
    		}
    	}else if(STOCK.equalsIgnoreCase(business)){
    		log.info("STATUS:::"+head.getStatus());
    		if( OrderStatus.FINISH.equals(head.getStatus()) ){	// 簽核完成後,下個階段按下送出才能扣庫存
    			// 更新cm,im庫存,
    			updateStocks(head, employeeCode, errorMsgs, false, beforeStatus);		// STOCK

    			//for 儲位用
    			if(imStorageAction.isStorageExecute(head)){
    				//異動儲位庫存，目前只能送出增加庫存，若為駁回isReject應為true
    				boolean isReject = false;
    				imStorageService.updateStorageOnHandBySource(head, OrderStatus.FINISH, PROGRAM_ID, identification, isReject);
    			}

    		}
//  		}else if(STOCK_REJECT_ARCHIVE.equalsIgnoreCase(business)){
//  		// 如果 主檔的報單沒填  狀態signing -> finish 只是修改備註欄位為新報單的資訊,以供海關人員追朔
//  		if( OrderStatus.SIGNING.equals(head.getStatus()) && OrderStatus.SAVE.equals(beforeStatus) ||
//  		OrderStatus.SIGNING.equals(head.getStatus()) && OrderStatus.REJECT.equals(beforeStatus) ){

//  		// 扣庫存,寫入invoice,沒寫單頭的報關單號
//  		updateStocks(head, employeeCode, errorMsgs, false, beforeStatus);		// STOCK

//  		}else if(OrderStatus.SIGNING.equals(head.getStatus()) && OrderStatus.SIGNING.equals(beforeStatus)){
//  		// 回寫單頭的報關單號
//  		if( CREATOR.equalsIgnoreCase(userType) && "ARCHIVE".equalsIgnoreCase(formAction) ){
//  		update(head);								// ARCHIVE
//  		}
//  		}else if( OrderStatus.REJECT.equals(head.getStatus())){
//  		// rollback 庫存
//  		updateStocks(head, employeeCode, errorMsgs, true, beforeStatus);		// STOCK
//  		}
//  		}else if(STOCK_DECL.equalsIgnoreCase(business)){
//  		if(OrderStatus.FINISH.equals(head.getStatus()) ){	// 簽核完成後,下個階段按下送出才能扣庫存
//  		// 更新 D7報單狀態 WAIT_IN -> FINISH
//  		Map map = new HashMap();
//  		map.put("beforeStatus", OrderStatus.WAIT_IN);
//  		map.put("afterStatus", OrderStatus.FINISH);
//  		updateCmDeclaration(head, employeeCode, map);					// DECL

//  		// 更新cm,im庫存,
//  		updateStocks(head, employeeCode, errorMsgs, false, beforeStatus);		// STOCK
//  		}
    	}else{
    		throw new Exception("查無此商業邏輯:" + business);
    	}

//	switch (adjustmentType) {
//	case DISCARD:		//	報廢
//	case F2P:			// 	保稅轉完稅
//	    if( "F".equals(taxType)){	// 保稅
//		// 如果 主檔的報單有填,寫入invoice,扣庫存,則狀態save -> finish,
//		// 如果 主檔的報單沒填  狀態signing -> finish 只是修改備註欄位為新報單的資訊,以供海關人員追朔
//		if( OrderStatus.SIGNING.equals(head.getStatus()) ){
//		    // 扣庫存,寫入invoice,沒寫單頭的報關單號
//		    this.updateStocksAndInvoice(head, employeeCode, errorMsgs, beforeStatus);
//
//		}else if( OrderStatus.REJECT.equals(head.getStatus())){
//		    // rollback 庫存, delete invoice
//		    this.deleteStocksAndInvoice(head, employeeCode);
//		}else if( OrderStatus.FINISH.equals(head.getStatus()) ){
//		    if( OrderStatus.SAVE.equals(beforeStatus) ){
//			// 扣庫存,寫入invoice
//			this.updateStocksAndInvoice(head, employeeCode, errorMsgs, beforeStatus);
//		    }else if(OrderStatus.SIGNING.equals(beforeStatus)){
//			// 不扣庫存
//		    }
//		}
//	    }else if("P".equals(taxType)){	// 完稅
//		if( OrderStatus.FINISH.equals(head.getStatus()) ){	// 簽核完成後,下個階段按下送出才能扣庫存
//		    // 更新cm,im庫存,
//		    this.updateStocks(head, employeeCode, errorMsgs, beforeStatus);
//		}
//	    }
//	    break;
//	case OUT:			// 	出倉
//	    if( "F".equals(taxType)){	// 保稅
//		// 如果 主檔的報單沒填  狀態signing -> finish 只是修改備註欄位為新報單的資訊,以供海關人員追朔
//		if( OrderStatus.SIGNING.equals(head.getStatus()) ){
//		    // 扣庫存,寫入invoice,沒寫單頭的報關單號
//		    this.updateStocks(head, employeeCode, errorMsgs, beforeStatus);
//
//		}else if( OrderStatus.REJECT.equals(head.getStatus())){
//		    // rollback 庫存
//		    List<ImAdjustmentLine> lines = head.getImAdjustmentLines();
//		    for (ImAdjustmentLine imAdjustmentLine : lines) {
//			this.deleteOneStock(imAdjustmentLine, head, employeeCode);
//		    }
//		}
//	    }
//	    break;
//	case IN: 	// 入倉
//	    if( "F".equals(taxType) && OrderStatus.FINISH.equals(head.getStatus()) ){	// 簽核完成後,下個階段按下送出才能扣庫存
//		// 更新 D7報單狀態 WAIT_IN -> FINISH
//		Map map = new HashMap();
//		map.put("beforeStatus", OrderStatus.WAIT_IN);
//		map.put("afterStatus", OrderStatus.FINISH);
//		updateD7CmDeclaration(head, employeeCode, errorMsgs, map);
//
//		// 更新cm,im庫存,
//		updateStocks(head, employeeCode, errorMsgs, beforeStatus);
//	    }
//
//	    break;
//	default:	// 一般情況 for 保稅完稅:盤盈虧, 領用除帳(非賣品), 調成本
//	    if( OrderStatus.FINISH.equals(head.getStatus()) ){	// 簽核完成後,下個階段按下送出才能扣庫存
//		// 更新cm,im庫存,
//		this.updateStocks(head, employeeCode, errorMsgs, beforeStatus);
//	    }
//	break;
//	}
    }

    /**
     * 檢核,存取拆/併貨調整單主檔,明細檔,重新設定狀態
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map updateImApartAdjustment(Map parameterMap, String formAction)throws Exception{
	Map resultMap = new HashMap();
	List errorMsgs = null;
	String resultMsg = null;
	try {

	    Object otherBean = parameterMap.get("vatBeanOther");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

	    ImAdjustmentHead head = this.getActualImAdjustment(formLinkBean);
		String beforeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeStatus");
		
	    // 檢核
	    if( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction)){
	    	this.deleteLine(head);
	    	errorMsgs  = this.checkedImApartAdjustment(parameterMap, head );
	    }

	    if( errorMsgs == null || errorMsgs.size() == 0 ){
			// 設定單號
			this.setOrderNo(head);
			// 成功則設定下個狀態
			this.setApartNextStatus(head, formAction );
			
			log.info("Reset - formAction : "+formAction);
			log.info("Reset - beforeStatus : "+beforeStatus);
			// 更新調整單主檔明細檔
			if(OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) || "SAVE".equals(formAction))
				this.removeDetailItemCode(head);
			if(OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction)){
			    this.updateImAdjustment(head, employeeCode );
			}
			
			if(OrderStatus.FORM_SUBMIT.equals(formAction) && "SAVE".equals(beforeStatus) ){
				log.info("Reset - 'BEF'.equals(head.getOrderTypeCode()):"+"BEF".equals(head.getOrderTypeCode()));
				//20191212-luke-重整
			    if("BEF".equals(head.getOrderTypeCode())){
			    	//異動重整前庫存
			    	this.updateStocks(head, employeeCode, errorMsgs, false, beforeStatus);
			    	log.info("Reset - method:updateStocks END");
			    	//新增重整後報單資料與 可用庫存
			    	this.updateResCmDeclaration(head, employeeCode);
			    	log.info("Reset - method:updateStocks END");	    	
			    }
			}
	
			// 顯示前端alert訊息
			resultMsg = "Order No：" + head.getOrderTypeCode() + "-" + head.getOrderNo()  + "存檔成功！ 是否繼續新增？";

	    } else if( errorMsgs.size() > 0 ){
		if( OrderStatus.FORM_SUBMIT.equals(formAction) ){
		    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		}
	    }
	    resultMap.put("entityBean", head);
	    resultMap.put("resultMsg", resultMsg);
	} catch( ValidationErrorException ve ){
	    log.error("拆/併貨調整單檢核時發生錯誤，原因：" + ve.toString());
	    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	} catch (Exception e) {
	    log.error("拆/併貨調整單存檔時發生錯誤，原因：" + e.toString());
	    throw new Exception("拆/併貨調整單存檔時發生錯誤，原因：" + e.getMessage());
	}

	return resultMap;
    }
    public boolean removeDetailItemCode(ImAdjustmentHead headObj) {
    	log.info("進入removeDetailItemCode時間點"+new Date());
    	System.out.println("移除空白的 Item : checkDetailItemCode單別:"+headObj.getOrderTypeCode()+" 單號:"+headObj.getOrderNo());
		List<ImAdjustmentLine> items = headObj.getImAdjustmentLines();
		List<ImAdjustmentLine> isDelItems = new ArrayList();
		System.out.println("刪除前數量:"+items.size());
		for(ImAdjustmentLine item:items){
			if (item != null && "1".equals(item.getIsDeleteRecord()) ) {
				isDelItems.add(item);
			}
		}
		items.removeAll(isDelItems);
		System.out.println("刪除後數量:"+items.size());
		long indexNo = 1L;
		for(ImAdjustmentLine item:items){
			System.out.println("排序前index:"+item.getIndexNo());
	    	System.out.println("排序後index:"+indexNo);
	    	item.setIndexNo(indexNo++);
		}
		headObj.setImAdjustmentLines(items);
		return items.isEmpty();
	}
    /**
     * 更新拆併貨的imOnHand庫存
     * @param head
     * @param employeeCode
     * @param reject
     */
    private void updateImApartAdjustmentOnHand(ImAdjustmentHead head, String employeeCode, boolean reject)throws Exception{
	if (OrderStatus.FINISH.equalsIgnoreCase(head.getStatus())) {
	    if (reject) {
		// 回復
		head.setOnHandStatus("N");
	    } else {
		head.setOnHandStatus("Y");
	    }
	    List<ImAdjustmentLine> lines = head.getImAdjustmentLines();
	    for (ImAdjustmentLine line : lines) {
		updateOneApartStock(line, head, employeeCode, reject);
	    }
	}
    }

    /**
     * 前端資料塞入bean
     * @param parameterMap
     * @return
     */
    public Map updateImAdjustmentBean(Map parameterMap)throws FormException, Exception {
    	Map resultMap = new HashMap();
	try{
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    //取得欲更新的bean
	    ImAdjustmentHead imAdjustmentHead = this.getActualImAdjustment(formLinkBean);
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imAdjustmentHead);
	    resultMap.put("entityBean", imAdjustmentHead);
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
     *  取單號後更新更新主檔
     *
     * @param parameterMap
     * @return Map
     * @throws FormException
     * @throws Exception
     */
    public Map updateImAdjustmentWithActualOrderNO(Map parameterMap) throws FormException, Exception {

	Map resultMap = new HashMap();
	try{

	    resultMap = this.updateImAdjustmentBean(parameterMap);
	    ImAdjustmentHead imAdjustmentHead = (ImAdjustmentHead) resultMap.get("entityBean");

	    //刪除於SI_PROGRAM_LOG的原識別碼資料
	    String identification = MessageStatus.getIdentification(imAdjustmentHead.getBrandCode(),
		    imAdjustmentHead.getOrderTypeCode(), imAdjustmentHead.getOrderNo());
	    siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

	    this.setOrderNo(imAdjustmentHead);
	    String resultMsg = imAdjustmentHead.getOrderNo() + "存檔成功！是否繼續新增？";
	    resultMap.put("resultMsg", resultMsg);

	    return resultMap;
	} catch (FormException fe) {
	    log.error("調整單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("調整單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("調整單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 更新調整單主檔明細檔
     * @param head
     * @param employeeCode
     */
    private void updateImAdjustment(ImAdjustmentHead head, String employeeCode )throws Exception{
	try{
	    Date date = new Date();
	    if (head.getHeadId() != null) {
		head.setLastUpdateDate(date);
		head.setLastUpdatedBy(employeeCode);

		List<ImAdjustmentLine> lines = head.getImAdjustmentLines();
		for (ImAdjustmentLine imAdjustmentLine : lines) {
		    imAdjustmentLine.setLastUpdateDate(date);
		    imAdjustmentLine.setLastUpdatedBy(employeeCode);
		}

		imAdjustmentHeadDAO.update(head);
	    }
	}catch (Exception ex) {
	    log.error("調整單主檔明細檔存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("調整單主檔明細檔存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 依各種調整類別更新庫存 for 反轉
     * @param head
     * @param employeeCode
     * @param taxType
     * @param adjustmentType
     * @throws Exception
     */
    private void updateImGeneralAdjustmentStock( ImAdjustmentHead head, String beforeStatus, String employeeCode, List errorMsgs, Map parameterMap )throws Exception{

	BuCommonPhraseLine adjustmentTypeConfig = (BuCommonPhraseLine)parameterMap.get("config");

	String business = adjustmentTypeConfig.getParameter1(); // 取得主要商業邏輯
	String lineCode = adjustmentTypeConfig.getId().getLineCode();

	int adjustmentType = NumberUtils.getInt( head.getAdjustmentType());

	if( OrderStatus.CLOSE.equals(beforeStatus) || OrderStatus.FINISH.equals(beforeStatus) ){
	    if(StringUtils.hasText(business)){
		if(STOCK_INVOICE_REJECT_ARCHIVE_DECL.equalsIgnoreCase(business) ){
		    if("F".equalsIgnoreCase(head.getTaxType())){
			// 更新 D7報單狀態 FINISH -> WAIT_IN
			Map map = new HashMap();
			map.put("beforeStatus", OrderStatus.FINISH);
			map.put("afterStatus", OrderStatus.WAIT_IN);
			updateCmDeclaration(head, employeeCode, map);
		    }
		    updateAggregateStock(head, employeeCode, errorMsgs, true, beforeStatus);

		    if( OrderStatus.CLOSE.equals(beforeStatus) ){
			imTransationDAO.deleteTransationByIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
		    }

		}else if(STOCK.equalsIgnoreCase(business)){
		    updateAggregateStock(head, employeeCode, errorMsgs, true, beforeStatus);
		    if( OrderStatus.CLOSE.equals(beforeStatus) ){
			imTransationDAO.deleteTransationByIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
		    }
		}else{
		    throw new Exception("查無".concat(lineCode).concat("此商業邏輯").concat(business));
		}
	    }else{
		throw new Exception("查無".concat(lineCode).concat("此商業邏輯定義"));
	    }
//	    switch (adjustmentType) {
//	    case DISCARD:	//	報廢
//	    case IN: 		// 	入倉
//	    case F2P:		// 	保稅轉完稅
//		if("F".equalsIgnoreCase(head.getTaxType())){
//		    if(IN == adjustmentType || F2P == adjustmentType || DISCARD == adjustmentType ){
//			// 更新 D7報單狀態 FINISH -> WAIT_IN
//        		Map map = new HashMap();
//        		map.put("beforeStatus", OrderStatus.FINISH);
//        		map.put("afterStatus", OrderStatus.WAIT_IN);
//        		updateCmDeclaration(head, employeeCode, map);
//		    }
//		}
//		updateAggregateStock(head, employeeCode, errorMsgs, true, beforeStatus);
//
//		if( OrderStatus.CLOSE.equals(beforeStatus) ){
//		    imTransationDAO.deleteTransationByIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
//		}
//		break;
//	    default:	// 一般情況 for 保稅完稅:盤盈虧, 領用除帳(非賣品), 調成本,馬祖出倉，T1商品調整單，拆併貨(反轉後一定要做廢，因為舊介面會有問題)
//		updateAggregateStock(head, employeeCode, errorMsgs, true, beforeStatus);
//	    if( OrderStatus.CLOSE.equals(beforeStatus) ){
//		imTransationDAO.deleteTransationByIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
//	    }
//	    break;
//	    }
	}
    }

    /**
     * 更新D7報單 WAIT_IN -> FINISH
     * @param imAdjustmentHead
     * @param employeeCode
     * @throws Exception
     */
    private void updateCmDeclaration(ImAdjustmentHead imAdjustmentHead, String employeeCode, Map map)throws Exception{
	try {
	    String declarationNo = imAdjustmentHead.getDeclarationNo();
	    String declarationType = imAdjustmentHead.getDeclarationType();

	    String beforeStatus = (String)map.get("beforeStatus");
	    String afterStatus = (String)map.get("afterStatus");

	    CmDeclarationHead cmDeclarationHead = findOneCmDeclaration(declarationNo, declarationType, beforeStatus);
	    if(cmDeclarationHead == null){
		log.error("查無報單: " +declarationNo + " 報單類型: " +declarationType + "狀態待轉入");
		throw new Exception("查無報單: " +declarationNo + " 報單類型: " +declarationType + "狀態待轉入");
	    }else{
		cmDeclarationHead.setStatus(afterStatus);
		cmDeclarationHead.setLastUpdatedBy(employeeCode);
		cmDeclarationHead.setLastUpdateDate(new Date());
		cmDeclarationHeadDAO.update(cmDeclarationHead);
	    }
	} catch (Exception ex) {
	    log.error("更新報單狀態時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新報單狀態發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    private void copyCmDeclarationHead(CmDeclarationHead targetHead,CmDeclarationHead source){
    	if(null != targetHead && null != source){
    		targetHead.setMsgFun((null != source.getMsgFun()?source.getMsgFun():""));
    		targetHead.setBondNo((null != source.getBondNo()?source.getBondNo():""));
    		targetHead.setStrType((null != source.getStrType()?source.getStrType():""));
    		targetHead.setBoxNo((null != source.getBoxNo()?source.getBoxNo():""));
    		targetHead.setDeclType((null != source.getDeclType()?source.getDeclType():""));
    		targetHead.setImportDate((null != source.getImportDate()?source.getImportDate():new Date()));
    		targetHead.setDeclDate((null != source.getDeclDate()?source.getDeclDate():new Date()));
    		targetHead.setStgPlace((null != source.getStgPlace()?source.getStgPlace():""));
    		targetHead.setRlsTime((null != source.getRlsTime()?source.getRlsTime():new Date()));
    		targetHead.setRlsPkg((null == source.getRlsPkg()?source.getRlsPkg():0L));
    		targetHead.setExtraCond((null != source.getExtraCond()?source.getExtraCond():""));
    		targetHead.setPkgUnit((null != source.getPkgUnit()?source.getPkgUnit():""));
    		targetHead.setGWgt((null != source.getGWgt()?source.getGWgt():0D));
    		targetHead.setVesselSign((null != source.getVesselSign()?source.getVesselSign():""));
    		targetHead.setVoyageNo((null != source.getVoyageNo()?source.getVoyageNo():""));
    		targetHead.setShipCode((null != source.getShipCode()?source.getShipCode():""));
    		targetHead.setExporter((null != source.getExporter()?source.getExporter():""));
    		targetHead.setClearType((null != source.getClearType()?source.getClearType():""));
    		targetHead.setRefBillNo((null != source.getRefBillNo()?source.getRefBillNo():""));
    		targetHead.setOutbondNo((null != source.getOutbondNo()?source.getOutbondNo():""));
    		targetHead.setInbondNo((null != source.getInbondNo()?source.getInbondNo():""));
    	}
    }
    
    private void copyCmDeclarationItem(CmDeclarationItem targetItem,CmDeclarationItem source, String employeeCode){
    	if(null != targetItem && null != source){
    		targetItem.setPrdtNo(StringUtils.hasText(source.getPrdtNo())?source.getPrdtNo():"");
    		targetItem.setDescrip(StringUtils.hasText(source.getDescrip())?source.getDescrip():"");
    		targetItem.setBrand(StringUtils.hasText(source.getBrand())?source.getBrand():"");
    		targetItem.setModel(StringUtils.hasText(source.getModel())?source.getModel():"");
    		targetItem.setSpec(StringUtils.hasText(source.getSpec())?source.getSpec():"");
    		targetItem.setNWght((null != source.getNWght()?source.getNWght():0D));
    		targetItem.setQty((null != source.getQty()?source.getQty():0D));
    		targetItem.setUnit(StringUtils.hasText(source.getUnit())?source.getUnit():"");
    		targetItem.setODeclNo(StringUtils.hasText(source.getODeclNo())?source.getODeclNo():"");
    		targetItem.setOItemNo((null != source.getOItemNo()?source.getOItemNo():0L));
    		targetItem.setReserve1(StringUtils.hasText(source.getReserve1())?source.getReserve1():"");
    		targetItem.setReserve2(StringUtils.hasText(source.getReserve2())?source.getReserve2():"");
    		targetItem.setReserve3(StringUtils.hasText(source.getReserve3())?source.getReserve3():"");
    		targetItem.setReserve4(StringUtils.hasText(source.getReserve4())?source.getReserve4():"");
    		targetItem.setReserve5(StringUtils.hasText(source.getReserve5())?source.getReserve5():"");
    		targetItem.setCreatedBy(StringUtils.hasText(employeeCode)?employeeCode:"");
    		targetItem.setCreationDate(DateUtils.parseTDate(DateUtils.C_DATE_PATTON_DEFAULT,DateUtils.format(new Date())));
    		targetItem.setLastUpdatedBy(StringUtils.hasText(employeeCode)?employeeCode:"");
    		targetItem.setLastUpdateDate(DateUtils.parseTDate(DateUtils.C_DATE_PATTON_DEFAULT,DateUtils.format(new Date())));
    		targetItem.setIndexNo((null != source.getIndexNo()?source.getIndexNo():0L));
    		targetItem.setDescripOther(StringUtils.hasText(source.getDescripOther())?source.getDescripOther():"");
    		targetItem.setODeclDate((null != source.getODeclDate())?source.getODeclDate():new Date());
    		//targetItem.setCode(StringUtils.hasText(source.getDescrip())?source.getDescrip():"");//tasNo
    		targetItem.setPacs(StringUtils.hasText(source.getPacs())?source.getPacs():"");
    		targetItem.setProduceCountry(StringUtils.hasText(source.getProduceCountry())?source.getProduceCountry():"");
    		targetItem.setStuAnt((null != source.getStuAnt()?source.getStuAnt():0D));
    		targetItem.setStuUnit(StringUtils.hasText(source.getStuUnit())?source.getStuUnit():"");
    		targetItem.setFobValue((null != source.getFobValue()?source.getFobValue():0D));
    		targetItem.setCustValueAmt((null != source.getCustValueAmt()?source.getCustValueAmt():0D));
    		targetItem.setUnitPrice((null != source.getUnitPrice()?source.getUnitPrice():0D));
    		targetItem.setCurrencyCode(StringUtils.hasText(source.getCurrencyCode())?source.getCurrencyCode():"");
    		targetItem.setUnitCuritem(StringUtils.hasText(source.getUnitCuritem())?source.getUnitCuritem():"");
    		targetItem.setBuyCommNo(StringUtils.hasText(source.getBuyCommNo())?source.getBuyCommNo():"");
    		targetItem.setStatmode(StringUtils.hasText(source.getStatmode())?source.getStatmode():"");
    		targetItem.setDuty1(StringUtils.hasText(source.getDuty1())?source.getDuty1():"");
    		targetItem.setDutyRate1((null != source.getDutyRate1()?source.getDutyRate1():0D));
    		targetItem.setDuty2(StringUtils.hasText(source.getDuty2())?source.getDuty2():"");
    		targetItem.setDutyRate2((null != source.getDutyRate2()?source.getDutyRate2():0D));
    		targetItem.setDuty3(StringUtils.hasText(source.getDuty3())?source.getDuty3():"");
    		targetItem.setDutyRate3((null != source.getDutyRate3()?source.getDutyRate3():0D));
    		targetItem.setDuty4(StringUtils.hasText(source.getDuty4())?source.getDuty4():"");
    		targetItem.setDutyRate4((null != source.getDutyRate4()?source.getDutyRate4():0D));
    		targetItem.setPermitNo(StringUtils.hasText(source.getPermitNo())?source.getPermitNo():"");
    		targetItem.setPermitItem((null != source.getPermitItem()?source.getPermitItem():0D));
    		targetItem.setPricduty((null != source.getPricduty()?source.getPricduty():0D));
    		targetItem.setAdj(StringUtils.hasText(source.getAdj())?source.getAdj():"");
    		targetItem.setAssGoodNo(StringUtils.hasText(source.getAssGoodNo())?source.getAssGoodNo():"");
    		targetItem.setDutiableValuePrice((null != source.getDutiableValuePrice()?source.getDutiableValuePrice():0L));
    	}
    }
    
    /**
     * 新增重整報單 
     * @param imAdjustmentHead
     * @param employeeCode
     * @throws Exception
     */
    private void updateResCmDeclaration(ImAdjustmentHead imAdjustmentHead, String employeeCode)throws Exception{
    	log.info("RESET - 進行新增重整報單");
		try {
		    String resNo = imAdjustmentHead.getResNo();
		    
		    List<ImAdjustmentLine> items = imAdjustmentHead.getImAdjustmentLines();
		    List<CmDeclarationItem> lastItems = new ArrayList<CmDeclarationItem>();
		    CmDeclarationHead lastDecl = new CmDeclarationHead();
		    CmDeclarationItem lastItem = new CmDeclarationItem();
		    lastDecl.setImportDate(new Date());
		    Date lastDt = null;
		    Map<String,String> codeItem = new HashMap<String,String>();
		    codeItem.put("type", "G");
		    codeItem.put("code", "00000000000");
		    
		    for(ImAdjustmentLine line:items){
		    	String declNo = line.getOriginalDeclarationNo();
		    	Long declSeq = line.getOriginalDeclarationSeq();
		    	log.info("RESET - line.getBeAft():"+line.getBeAft()+":"+line.getBeAft().equals(RES_TYPE_AEFTER));
		    	if(line.getBeAft().equals(RES_TYPE_BEFORE) ){
		    		CmDeclarationHead cmDeclarationHead = findOneCmDeclaration(declNo);
		    		CmDeclarationItem cmDeclarationItem = cmDeclarationItemDAO.findOneCmDeclarationItem( declNo, declSeq);
		    		ImItem item = imItemDAO.findItem(imAdjustmentHead.getBrandCode(), line.getItemCode());
		    		lastDt = lastDecl.getImportDate();
			    	//log.info("RESET - cmDeclarationHead.getImportDate().compareTo(lastDt) :"+cmDeclarationHead.getImportDate().compareTo(lastDt));
			    	if(cmDeclarationHead.getImportDate().compareTo(lastDt)<= 0 ){
			    		copyCmDeclarationHead(lastDecl,cmDeclarationHead);
			    		lastItem = cmDeclarationItemDAO.findOneCmDeclarationItem( declNo, declSeq);
			    	}
			    	if(Long.valueOf(cmDeclarationItem.getCode())>Long.valueOf(codeItem.get("code"))){
			    		codeItem.put("type", item.getItemType());
					    codeItem.put("code", cmDeclarationItem.getCode());
			    	}else {
			    		if(item.getItemType().equals("N") && !codeItem.get("type").equals("N")){
			    			codeItem.put("type", item.getItemType());
						    codeItem.put("code", cmDeclarationItem.getCode());
			    		}
			    	}
		    	}else if(line.getBeAft().equals(RES_TYPE_AEFTER)){
		    		CmDeclarationItem newItem = new CmDeclarationItem();
		    		copyCmDeclarationItem(newItem,lastItem,employeeCode);
		    		newItem.setDeclNo(declNo);
		    		newItem.setItemNo(declSeq);
		    		newItem.setCode(codeItem.get("code"));
		    		lastItems.add(newItem);
		    	}
		    }
		    log.info("RESET - clastDecl :"+lastDecl+"  :"+lastItems.size());
		    if(null != lastDecl && lastItems.size() > 0){
		    	lastDecl.setDeclNo(resNo);
		    	lastDecl.setLastUpdatedBy(employeeCode);
		    	lastDecl.setLastUpdateDate(DateUtils.parseTDate(DateUtils.C_DATE_PATTON_DEFAULT,DateUtils.format(new Date())));
		    	lastDecl.setCmDeclarationItems(lastItems);
				cmDeclarationHeadDAO.save(lastDecl);
				log.info("RESET - 新增重整報單 完成");
		    }
		    
		} catch (Exception ex) {
		    log.error("新增重整報單時發生錯誤，原因：" + ex.toString());
		    ex.printStackTrace();
		    throw new Exception("新增重整報單發生錯誤，原因：查無報單/商品資訊 ");
		}
    }

    /**
     * 更新cm_on_hand 或 im_on_hand 庫存
     * @param imAdjustmentHead
     * @param employeeCode
     * @throws Exception
     */
    private void updateStocks(ImAdjustmentHead imAdjustmentHead, String employeeCode , List errorMsgs, boolean isReverse, String beforeStatus)throws Exception {
		log.info( "======<updateStockAndLog>=======");
		try{
		    updateAggregateStock(imAdjustmentHead, employeeCode, errorMsgs, isReverse, beforeStatus);
		    if(null != errorMsgs && errorMsgs.size() > 0 ){
		    	String identification = MessageStatus.getIdentification(imAdjustmentHead.getBrandCode(), imAdjustmentHead.getOrderTypeCode(), imAdjustmentHead.getOrderNo());
		    	throw new Exception(identification + "更新庫存時發生錯誤");
		    }
		}catch (Exception ex) {
		    log.error("更新庫存時發生錯誤，原因：" + ex.toString());
		    throw new Exception("更新庫存時發生錯誤，原因：" + ex.getMessage());
		}
		log.info( "======<updateStockAndLog/>=======");

    }

    /**
     * 更新cm_on_hand 或 im_on_hand 庫存, 寫入invoice
     * @param imAdjustmentHead
     * @param employeeCode
     * @throws Exception
     */
    private void updateStocksAndInvoice(ImAdjustmentHead imAdjustmentHead, String employeeCode, List errorMsgs, String beforeStatus )throws Exception {
	log.info( "======<updateStockAndLog>=======");
	try{
	    // 寫入invoice
	    this.saveInovice(imAdjustmentHead, employeeCode);

	    // 聚集庫存
	    updateAggregateStock(imAdjustmentHead, employeeCode, errorMsgs, false, beforeStatus);

	}catch (Exception ex) {
	    log.error("更新庫存與寫入invoice時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新庫存與寫入invoice時發生錯誤，原因：" + ex.getMessage());
	}
	log.info( "======<updateStockAndLog/>=======");

    }

    /**
     * 更新拆併貨庫存
     * @param imAdjustmentLine
     * @param imAdjustmentHead
     * @param employeeCode
     * @throws Exception
     */
    private void updateOneApartStock(ImAdjustmentLine imAdjustmentLine, ImAdjustmentHead imAdjustmentHead, String employeeCode, boolean reject)
    throws Exception {
	log.info( "======<updateOneApartStock>=======");
	try{
	    String brandCode = imAdjustmentHead.getBrandCode();								// 品牌 PK
	    Double difQuantity = imAdjustmentLine.getDifQuantity();  						// 數量

	    // im_on_hand 用
	    String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode); 	// 組織代碼 PK
	    String itemCode = imAdjustmentLine.getItemCode();  								// 商品 PK
	    String warehouseCode = imAdjustmentLine.getWarehouseCode(); 					// 倉庫 PK
	    String lotNo = imAdjustmentLine.getLotNo();										// 批號 PK

	    log.info( "organizationCode = " + organizationCode );
	    log.info( "itemCode = " + itemCode );
	    log.info( "warehouseCode = " + warehouseCode );
	    log.info( "lotNo = " + lotNo );
	    log.info( "difQuantity = " + difQuantity );

	    if (reject) {
		difQuantity = 0 - difQuantity;
	    }
	    long salesRatio = 1;
	    ImItem imItem = (ImItem)imItemDAO.findFirstByProperty("ImItem","and brandCode = ? and itemCode = ? and enable = ? ", new Object[]{ brandCode, itemCode, "Y"});

	    if( imItem != null){
		if ((null != imItem.getSalesRatio()) && (imItem.getSalesRatio() > 0)){
		    salesRatio = imItem.getSalesRatio();
		}
	    }else{
		throw new Exception("品牌:" + brandCode + " 商品:" + imAdjustmentLine.getItemCode()+"資料異常， 無法查出商品交易單位與進貨單位的比例");
	    }

	    difQuantity = salesRatio * difQuantity;

	    // im_on_hand 加庫存
	    log.info("======<im_on_hand>======");
	    imOnHandDAO.updateOtherUncommitQuantity( organizationCode, itemCode,
		    warehouseCode, lotNo, brandCode, difQuantity, employeeCode);
	    log.info("======<im_on_hand/>======");

	}catch (Exception ex) {
	    log.error("存檔庫存時發生錯誤，原因：" + ex.toString());
	    throw new Exception("存檔庫存時發生錯誤，原因：" + ex.getMessage());
	}
	log.info( "======<updateOneApartStock/>=======");
    }

    /**
     * imOnHand 選取多筆檢視
     * @param parameterMap
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public List<Properties> updatePickerData(Map parameterMap) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,Exception {
	Map returnMap = new HashMap(0);
	Object otherBean             = parameterMap.get("vatBeanOther");
	Object pickerBean            = parameterMap.get("vatBeanPicker");
	String formIdString          = (String)PropertyUtils.getProperty(otherBean, "formId");
	String brandCode          = (String)PropertyUtils.getProperty(otherBean, "brandCode");
	String taxType          = (String)PropertyUtils.getProperty(otherBean, "taxType");

	Integer lineId               = (Integer)PropertyUtils.getProperty(otherBean, "lineId");
	List<Object>  pickResults = (List<Object>)PropertyUtils.getProperty(pickerBean ,"imOnHandResult");
	log.info("lineId:"+ lineId);
	log.info("formIdString:"+ formIdString);
	log.info("pickResult.size:"+ pickResults.size());

	Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
	//Integer lineId =  StringUtils.hasText(lineIdString)? Integer.valueOf(lineIdString)-1:null;
	try{
	    if(null!=formId && null != pickResults && pickResults.size()> 0){
		ImAdjustmentHead head = executeFind(formId);
		if(null!=head ){
		    List<ImAdjustmentLine> lines = head.getImAdjustmentLines();
		    log.info("lines.size:"+lines.size());
		    String itemCode = new String("");
		    String lotNo = new String("");

		    for(Object pickResult: pickResults){
			itemCode =(String)PropertyUtils.getProperty(pickResult, "id_itemCode");
			lotNo =(String)PropertyUtils.getProperty(pickResult, "id_lotNo");
			log.info("itemCode:"+itemCode+"/lotNo="+lotNo);

			ImAdjustmentLine  newLine = new ImAdjustmentLine();

			ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findOneCurrentPrice(brandCode, taxType, itemCode);
//			ImItemEanPriceView imItemEanPriceView = imItemEanPriceViewDAO.getItemInfoByProperty(brandCode, itemCode, taxType);
			if( imItemCurrentPriceView != null){
			    newLine.setItemCName(imItemCurrentPriceView.getItemCName());
			    newLine.setLocalUnitCost(imItemCurrentPriceView.getUnitPrice());
			}else{
			    throw new Exception("查品牌"+brandCode+"、品牌:"+itemCode+"稅別:"+taxType+"無此資料");
			}
			newLine.setCustomsItemCode(itemCode);
			newLine.setItemCode(itemCode);
			newLine.setLotNo(lotNo);
			lines.add(newLine);
		    }
		    head.setImAdjustmentLines(lines);
		    log.info("lines.size:"+lines.size());
		    imAdjustmentHeadDAO.update(head);
		}
	    }

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
     * imOnHand 選取多筆檢視 from 拆併貨
     * @param parameterMap
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public List<Properties> updateApartPickerData(Map parameterMap) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,Exception {
	Map returnMap = new HashMap(0);
	Object otherBean             = parameterMap.get("vatBeanOther");
	Object pickerBean            = parameterMap.get("vatBeanPicker");
	String formIdString          = (String)PropertyUtils.getProperty(otherBean, "formId");

	Integer lineId               = (Integer)PropertyUtils.getProperty(otherBean, "lineId");
	List<Object>  pickResults = (List<Object>)PropertyUtils.getProperty(pickerBean ,"imOnHandResult");
	log.info("lineId:"+ lineId);
	log.info("formIdString:"+ formIdString);
	log.info("pickResult.size:"+ pickResults.size());

	Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
	try{
	    if(null!=formId && null != pickResults && pickResults.size()> 0){
		ImAdjustmentHead head = executeFind(formId);
		if(null!=head ){
		    List<ImAdjustmentLine> lines = head.getImAdjustmentLines();
		    log.info("lines.size:"+lines.size());
		    String itemCode = new String("");
		    String lotNo = new String("");

		    for(Object pickResult: pickResults){
			itemCode =(String)PropertyUtils.getProperty(pickResult, "id_itemCode");
			lotNo =(String)PropertyUtils.getProperty(pickResult, "id_lotNo");
			String warehouseCode = (String)PropertyUtils.getProperty(pickResult, "id_warehouseCode");
			log.info("itemCode:"+itemCode+"/lotNo="+lotNo+"/warehouseCode="+warehouseCode);

			ImAdjustmentLine  newLine = new ImAdjustmentLine();

			newLine.setItemCode(itemCode);
			newLine.setLotNo(lotNo);
			newLine.setWarehouseCode(warehouseCode);
			newLine.setDifQuantity(0D);
			lines.add(newLine);
		    }
		    head.setImAdjustmentLines(lines);
		    log.info("lines.size:"+lines.size());
		    imAdjustmentHeadDAO.update(head);
		}
	    }

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
     * 檢核調整主檔,明細檔
     * @param Head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validateImGeneralAdjustment(ImAdjustmentHead head, String programId, String identification, List errorMsgs, Map parameterMap) throws ValidationErrorException, NoSuchObjectException {
	validteHead(head, programId, identification, errorMsgs, parameterMap);
	if(head.getOrderTypeCode().equals("APF")){
		if(head.getImAdjustmentLines()!=null){
			log.info("沒填寫明細直接不檢核");
		}else{
			validteLine(head, programId, identification, errorMsgs ,parameterMap);
		}
	}else{
		validteLine(head, programId, identification, errorMsgs ,parameterMap);
	}
    }

    /**
     * 檢核調整單主檔
     * @param head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
	private void validteHead(ImAdjustmentHead head, String programId, String identification, List errorMsgs, Map parameterMap)
			throws ValidationErrorException, NoSuchObjectException {
		String message = null;
		String tabName = "主檔資料";
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			BuCommonPhraseLine adjustmentTypeConfig = (BuCommonPhraseLine) parameterMap.get("config");

			String business = adjustmentTypeConfig.getParameter1(); // 取得主要商業邏輯

			String loginUser = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			String userType = (String) PropertyUtils.getProperty(otherBean, "userType");

//	    String declarationNo = head.getDeclarationNo();
			String brandCode = head.getBrandCode();
			String orderTypeCode = head.getOrderTypeCode();

			String adjustmentTypeString = head.getAdjustmentType();
			String defaultWarehouseCode = head.getDefaultWarehouseCode();
			Date adjustmentDate = head.getAdjustmentDate();
			Date declarationDate = head.getDeclarationDate();
			String declarationType = head.getDeclarationType();

//	    if(!StringUtils.hasText(declarationNo)){
//	    message = "請輸入" + tabName + "的報關單號！";
//	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//	    errorMsgs.add(message);
//	    log.error(message);
//	    }else{
//	    ImReceiveHead imReceiveHead = this.findOneImReceive( head.getBrandCode(), head.getSourceOrderTypeCode() , head.getTaxType(), sourceOrderNo);
//	    if( imReceiveHead == null ){
//	    message = "查無" + tabName + "的來源進貨單號！";
//	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//	    errorMsgs.add(message);
//	    log.error(message);
//	    }
//	    }
			if (brandCode.indexOf("T2") > -1 && !StringUtils.hasText(adjustmentTypeString)) {
				message = "請輸入" + tabName + "的調整類別！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head
						.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}
			if (!StringUtils.hasText(defaultWarehouseCode) && !orderTypeCode.equals("AIR")) {
				message = "請輸入" + tabName + "的庫別！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head
						.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			} else if (StringUtils.hasText(defaultWarehouseCode)){
				ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, defaultWarehouseCode,
						"Y");
				if (imWarehouse == null) {
					message = "查無" + tabName + "的庫別！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head
							.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}
			}
			if (adjustmentDate == null && !loginUser.equals(head.getCreatedBy())) {
				message = "請輸入" + tabName + "的調整日期！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head
						.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);

			} else if(!loginUser.equals(head.getCreatedBy())) {
				// ===================================檢核調整日期是否大於日關帳日期=====================================
				String dateType = "調整日期";
				ValidateUtil.isAfterClose(brandCode, orderTypeCode, "調整日期", head.getAdjustmentDate(),head.getSchedule());
				try {
					BuOrderType orderType = buOrderTypeService.findById(new BuOrderTypeId(brandCode, orderTypeCode));
					if (orderType != null) {
						String closeType = orderType.getCloseType(); // 關帳類型(D:日關,M:月關)
						if (!StringUtils.hasText(closeType)) {
							message = "請輸入" + tabName + "的品牌代號：" + brandCode + "、單別：" + orderTypeCode
									+ "的關帳類型為空值，請聯絡系統管理人員處理！";
							siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message,
									head.getLastUpdatedBy());
							errorMsgs.add(message);
							log.error(message);
						} else if (!"D".equals(closeType) && !"M".equals(closeType)) {
							message = "請輸入" + tabName + "的品牌代號：" + brandCode + "、單別：" + orderTypeCode
									+ "的關帳類型設定錯誤，請聯絡系統管理人員處理！";
							siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message,
									head.getLastUpdatedBy());
							errorMsgs.add(message);
							log.error(message);
						}

						BuBrand brand = buBrandService.findById(brandCode);
						String dailyCloseDate = brand.getDailyCloseDate(); // 日關日期
						String monthlyCloseMonth = brand.getMonthlyCloseMonth(); // 月關月份
						String dateString = DateUtils.format(adjustmentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);// YYYYMMDD
						String yearAndMonth = dateString.substring(0, 6);// YYYYMM

						if ("D".equals(closeType)) {
							if (!StringUtils.hasText(dailyCloseDate)) {
								message = "請輸入" + tabName + "的日關帳日期為空值，請聯絡系統管理人員處理！";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification,
										message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							} else if (dailyCloseDate.length() != 8) {
								message = tabName + "的日關帳日期長度不足8碼，請聯絡系統管理人員處理！";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification,
										message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							} else if (Integer.parseInt(dateString) <= Integer.parseInt(dailyCloseDate)) {
								message = tabName + "的日關帳日期長度不足8碼，請聯絡系統管理人員處理！";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification,
										message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							}
						} else {
							if (!StringUtils.hasText(monthlyCloseMonth)) {
								message = tabName + "的月關帳年月為空值，請聯絡系統管理人員處理！";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification,
										message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							} else if (monthlyCloseMonth.length() != 6) {
								message = tabName + "的的月關帳年月長度不足6碼，請聯絡系統管理人員處理！";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification,
										message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							} else if (Integer.parseInt(yearAndMonth) <= Integer.parseInt(monthlyCloseMonth)) {
								message = tabName + "的" + dateType + "必須大於關帳年月：" + monthlyCloseMonth + "！";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification,
										message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							}
						}
					} else {
						message = "查無品牌代號：" + brandCode + "、單別：" + orderTypeCode + "的資料，請聯絡系統管理人員處理！";
						siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message,
								head.getLastUpdatedBy());
						errorMsgs.add(message);
						log.error(message);
					}
				} catch (FormException fe) {
					message = "檢核是否小於關帳日或月時發生錯誤，原因：" + fe.toString();
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head
							.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				} catch (Exception ex) {
					message = "檢核是否小於關帳日或月時發生錯誤，原因：" + ex.toString();
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head
							.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}
				// ================================================================================================
				/*if(adjustmentDate.equals("")){
					
				}
				*/
			}

			int adjustmentType = NumberUtils.getInt(adjustmentTypeString);
			String taxType = head.getTaxType();
			String status = head.getStatus();
			String declarationNo = head.getDeclarationNo();

			log.info("adjustmentType = " + adjustmentType);
			log.info("taxType = " + taxType);
			log.info("status = " + status);
			log.info("declarationNo = " + declarationNo);
			// 調整類別為報廢或保稅轉完稅,且稅別為保稅,狀態SIGNING,則檢核單頭的報單單號
			if ((STOCK_INVOICE_REJECT_ARCHIVE_DECL.equalsIgnoreCase(business)) && "F".equals(taxType)
					&& OrderStatus.SIGNING.equals(status)) { // 保稅轉完稅且保稅，報廢，馬祖出倉
				if ((OrderStatus.FORM_SUBMIT.equalsIgnoreCase(formAction) || OrderStatus.SUBMIT_BG
						.equalsIgnoreCase(formAction))
						&& CREATOR.equalsIgnoreCase(userType)) {
					log.info("額外檢核,狀態簽核,(保稅)報廢,(保稅)保稅轉完稅,(保稅)出倉");
					if (!StringUtils.hasText(declarationNo)) {
						message = "請輸入" + tabName + "的報單單號！";
						siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message,
								head.getLastUpdatedBy());
						errorMsgs.add(message);
						log.error(message);
					} else {
						log.info("declarationNo = " + declarationNo);
						log.info("declarationType = " + declarationType);
						CmDeclarationHead cmDeclarationHead = findOneCmDeclaration(declarationNo, declarationType, "");
						if (cmDeclarationHead == null) {
							message = tabName + "的報單單號:" + declarationNo + "不存在！";
							siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message,
									head.getLastUpdatedBy());
							errorMsgs.add(message);
							log.error(message);
						} else {
							if (!OrderStatus.WAIT_IN.equals(cmDeclarationHead.getStatus())) {
								message = tabName + "的[" + declarationType + "]報單單號:" + declarationNo + "狀態不為"
										+ OrderStatus.getChineseWord(OrderStatus.WAIT_IN) + "！";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification,
										message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							}
						}
					}
				}
			} else if (adjustmentType == F2P && "P".equals(taxType)) { // 保稅轉完稅且完稅
				if (!StringUtils.hasText(declarationNo) && OrderStatus.SAVE.equals(status)) {
					message = "請輸入" + tabName + "的報單單號！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head
							.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}
				if (null == declarationDate) {
					message = "請輸入" + tabName + "的報單日期！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head
							.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}
				if (!StringUtils.hasText(declarationType)) {
					message = "請輸入" + tabName + "的報關類別！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head
							.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}

			} else if (adjustmentType == IN && "F".equals(taxType)&& !orderTypeCode.equals("MDF")) { // 轉入倉且稅別為保稅
				if (!StringUtils.hasText(declarationNo) && OrderStatus.SAVE.equals(status)) {
					message = "請輸入" + tabName + "的報單單號！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head
							.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				} else {
					log.info("declarationNo = " + declarationNo);
					log.info("declarationType = " + declarationType);

					CmDeclarationHead cmDeclarationHead = findOneCmDeclaration(declarationNo, declarationType, "");
					if (cmDeclarationHead == null) {
						message = tabName + "的報單單號:" + declarationNo + "不存在！";
						siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message,
								head.getLastUpdatedBy());
						errorMsgs.add(message);
						log.error(message);
					} else if (!OrderStatus.FINISH.equals(cmDeclarationHead.getStatus())) { // modify by Weichun 2011.12.09
						message = tabName + "的[" + declarationType + "]報單單號:" + declarationNo + "狀態不為"
								+ OrderStatus.getChineseWord(OrderStatus.FINISH) + "！";
						siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message,
								head.getLastUpdatedBy());
						errorMsgs.add(message);
						log.error(message);
					}
				}
			}

		} catch (Exception e) {
			message = "檢核調整單主檔單" + tabName + "時發生錯誤，原因：" + e.toString();
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head
					.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		}
	}

    /**
     * 檢核調整單明細檔
     * @param head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validteLine(ImAdjustmentHead head, String programId, String identification, List errorMsgs,  Map parameterMap) throws ValidationErrorException, NoSuchObjectException {
	String message = null;
	String tabName = "明細資料頁籤";
	Map orderNoMap = new HashMap();
//	Map seqMap = new HashMap();
//	Map conditionMap = new HashMap();
	
	
	
	try{
	    String brandCode = head.getBrandCode();
	    String taxType = head.getTaxType();
	    int adjustmentType = NumberUtils.getInt( head.getAdjustmentType());
	    String status = head.getStatus();
	    String defaultWarehouseCode = head.getDefaultWarehouseCode();
	    Object otherBean = parameterMap.get("vatBeanOther");
		String loginUser = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

	    String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);

	    List lines = head.getImAdjustmentLines();

	    String customsWarehouseCode = "";
	    if("F".equals(taxType)){
		ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, head.getDefaultWarehouseCode(), "Y");
		// 倉庫屬於的關
		log.info( "imWarehouse = " + imWarehouse);
		if( imWarehouse != null ){
		    log.info( "imWarehouse.getCustomsWarehouseCode() = " + imWarehouse.getCustomsWarehouseCode());
		    customsWarehouseCode = imWarehouse.getCustomsWarehouseCode(); 			// 關別PK
		}else{
		    throw new NoSuchObjectException("依據品牌(" + brandCode + ")、倉庫(" + head.getDefaultWarehouseCode() + ")查無倉庫相關資料！");
		}
	    }

	    int size = lines.size();
	    if( size > 0 ){
		for (int i = 0; i < size; i++) {
		    ImAdjustmentLine line = (ImAdjustmentLine) lines.get(i);
		    String itemCode = line.getItemCode().trim().toUpperCase();
		    String indexNo = String.valueOf( line.getIndexNo() );
		    String lotNo = line.getLotNo();

		    message = null;

		    if (!StringUtils.hasText(itemCode)) {
			message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的商品！";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		    } else {
			log.info( "brandCode = " + brandCode );
			log.info( "itemCode = " + itemCode );
			log.info( "taxType = " + taxType );
			// 檢核調整單明細商品是否存在
			String actualItemCode = null;
			ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findOneCurrentPrice(brandCode, taxType, itemCode);
			log.info( "imItemCurrentPriceView = " + imItemCurrentPriceView );
			if( imItemCurrentPriceView == null ){
			    message = tabName + "中第" + (i + 1) + "項明細的商品不存在！";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);
			} else {
			    actualItemCode = imItemCurrentPriceView.getItemCode();
			    String warehouseCode = line.getWarehouseCode();
			    Double difQuantity = line.getDifQuantity();
			    Long originalDeclarationSeq = line.getOriginalDeclarationSeq()==null?0L:line.getOriginalDeclarationSeq();

			    log.info( "difQuantity = " + difQuantity );
			    if( !StringUtils.hasText(lotNo) && !head.getOrderTypeCode().equals("AIR") ){
				message = tabName + "中第" + (i + 1) + "項明細的批號不能為空！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }else{
				// 若商品不用批號管理 檢查批號預設值
				log.info( "brandCode = " + brandCode );
				log.info( "actualItemCode = " + actualItemCode );
				log.info( "taxType = " + taxType );
				ImItem imItem= imItemDAO.findOneImItem(brandCode, actualItemCode,taxType);
				log.info( "imItem = " + imItem );
				if(null != imItem){
				    String lotControl = imItem.getLotControl();
				    log.info( "lotControl = " + lotControl );
				    log.info( "lotNo = " + lotNo );
				    if("N".equalsIgnoreCase(lotControl) && !lotNo.equals(SystemConfig.LOT_NO)){
					message = "查" + tabName + "中第" + (i + 1) + "項明細商品不用批號管理則批號應為預設值"+SystemConfig.LOT_NO+"(12個0)！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				    }
				}
			    }

			    // 倉別
			    ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(head.getBrandCode(), warehouseCode, "Y");
			    if(imWarehouse == null){
				message = "查無" + tabName + "中第" + (i + 1) + "項明細商品的庫別！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }else{
			    	if(head.getOrderTypeCode().equals("AIR"))//AIR無defaultWarehouseCode
				    	defaultWarehouseCode = warehouseCode;
				// 比對每筆明細的倉庫與單頭的倉庫一樣
				if(!defaultWarehouseCode.equals(warehouseCode)){
				    message = tabName + "中第" + (i + 1) + "項明細商品的庫別("+warehouseCode+")與單頭庫別("+defaultWarehouseCode+")不符！";
				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				    errorMsgs.add(message);
				    log.error(message);
				}
			    }
			    
			    //規格單位
			    String specDesc = line.getSpecDesc()==null?"":line.getSpecDesc();
			    String unit = line.getUnit()==null?"":line.getUnit();
			    if(specDesc == "" && head.getOrderTypeCode().equals("MEG")){
					message = tabName + "中第" + (i + 1) + "項明細商品的 規格 不能為空";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}
			    if(unit == "" && head.getOrderTypeCode().equals("MEG")){
					message = tabName + "中第" + (i + 1) + "項明細商品的 單位 不能為空";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}else{
					if((unit.length()>3 || unit.length()<1) && head.getOrderTypeCode().equals("MEG")){
						message = tabName + "中第" + (i + 1) + "項明細商品的 單位 不得超過三字元";
						siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
						errorMsgs.add(message);
						log.error(message);
					}
				}
			    
			    String moveWhNo = line.getOrgMoveWhNo()==null?"":line.getOrgMoveWhNo();
			    Long moveWhNoItem = line.getOrgMoveWhItemNo()==null?0L:line.getOrgMoveWhItemNo();
			    if(moveWhNo == "" && head.getOrderTypeCode().equals("MEG")){
					message = tabName + "中第" + (i + 1) + "項明細商品的 原移倉單號 不能為空";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}
			    if(moveWhNoItem == 0L && head.getOrderTypeCode().equals("MEG")){
					message = tabName + "中第" + (i + 1) + "項明細商品的 原移倉單號 項次不能為空";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}
			    
			    // 數量
			    if(difQuantity == 0D && ADJUST_COST != adjustmentType && !head.getOrderTypeCode().equals("MEF") && (F2P != adjustmentType && !head.getOrderTypeCode().equals("AEF")) && !head.getOrderTypeCode().equals("ADF") ){
			    	message = tabName + "中第" + (i + 1) + "項明細商品的數量不能為0";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }else if( PROFIT == adjustmentType && difQuantity < 0D  ){
				message = tabName + "中第" + (i + 1) + "項盤盈商品的數量必須為正數！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }else if( LOSS == adjustmentType && difQuantity > 0D ){
				message = tabName + "中第" + (i + 1) + "項盤虧商品的數量必須為負數！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }else if( DISCARD == adjustmentType && difQuantity > 0D ){
				message = tabName + "中第" + (i + 1) + "項報廢商品的數量必須為負數！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
//				}else if( ERASE_ACCOUNT == adjustmentType && difQuantity > 0D ){
//				message = tabName + "中第" + (i + 1) + "項領用除帳的數量必須為負數！";
//				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//				errorMsgs.add(message);
//				log.error(message);
			    }else if( F2P == adjustmentType ){
				if(  "P".equals(taxType) && difQuantity < 0D ){
				    message = tabName + "中第" + (i + 1) + "項保稅轉完稅之完稅商品的數量必須為正數！";
				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				    errorMsgs.add(message);
				    log.error(message);
				}else if( "F".equals(taxType) && difQuantity > 0D ){
				    message = tabName + "中第" + (i + 1) + "項保稅轉完稅之保稅商品的數量必須為負數！";
				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				    errorMsgs.add(message);
				    log.error(message);
				}
			    }else if(OUT == adjustmentType && difQuantity > 0D){
				message = tabName + "中第" + (i + 1) + "項出倉商品的數量必須為負數！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }else if(IN == adjustmentType && difQuantity < 0D){
				message = tabName + "中第" + (i + 1) + "項入倉商品的數量必須為正數！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }
			    log.info("organizationCode = " + organizationCode );
			    log.info("actualItemCode = " + actualItemCode );
			    log.info("warehouseCode = " + warehouseCode );

			    if(OrderStatus.SAVE.equalsIgnoreCase(status) || OrderStatus.REJECT.equalsIgnoreCase(status) ){

				Double currentStockOnHandQty = imOnHandDAO.getCurrentStockOnHandQuantity(organizationCode, brandCode, actualItemCode, warehouseCode, SystemConfig.LOT_NO );
				log.info("currentIMStockOnHandQty = " + currentStockOnHandQty );
				if("P".equals(taxType)){
//				    if( orderNoMap.containsKey( actualItemCode ) ){
//				    message = tabName + "中第" + (i + 1) + "項明細的商品" + itemCode + "重複！";
//				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//				    errorMsgs.add(message);
//				    log.error(message);
//				    }else{
//				    orderNoMap.put( actualItemCode ,  indexNo );
//				    }

				}else if("F".equals(taxType)){
				    String originalDeclarationNo = line.getOriginalDeclarationNo()==null?"":line.getOriginalDeclarationNo();

				    //	單身報關項次是否重複
//				    if(seqMap.containsKey( actualItemCode + originalDeclarationNo + originalDeclarationSeq )){
//				    message = tabName + "中第" + (i + 1) + "項明細的報單項次" + originalDeclarationSeq + "重複！";
//				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//				    errorMsgs.add(message);
//				    log.error(message);
//				    }else{
//				    seqMap.put( actualItemCode + originalDeclarationNo + originalDeclarationSeq, indexNo);
//				    }
				    
				    if(!(difQuantity == 0D && ADJUST_COST == adjustmentType)){ // 數量不為0且非調成本
				    
				    //計算報單原始日是否大於目前兩年
				    //判斷一 如果原報單日為空 報錯
				    //判斷二 如果起始單大於兩年 且不為盤盈單 報錯
				    // by Caspar 2013.1.3
				    Date originalDeclarationDate = line.getOriginalDeclarationDate();
				    if(null == line.getOriginalDeclarationDate()){
						message = tabName + "中第" + (i + 1) + "項明細商品的報單起始日為空！";
			    		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    		errorMsgs.add(message);
			    		log.error(message);
				    }else{
					    if(!loginUser.equals(head.getCreatedBy())){
					    	log.info("DaysBetween = " + DateUtils.daysBetweenWithoutTime(originalDeclarationDate, head.getAdjustmentDate()));
					    	Date d = null==head.getDischargeDate()? head.getAdjustmentDate():head.getDischargeDate();
					    	if(PROFIT != adjustmentType && DateUtils.daysBetweenWithoutTime(originalDeclarationDate, d ) > 730){
					    		message = tabName + "中第" + (i + 1) + "項明細商品的報單起始日大於目前兩年！";
					    		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					    		errorMsgs.add(message);
					    		log.error(message);
					    	}
					    }
				    }
				    	
					// 單身報單項次不能為0
					if(originalDeclarationSeq == 0L){
					    message = tabName + "中第" + (i + 1) + "項明細商品的報單項次不能為0！";
					    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					    errorMsgs.add(message);
					    log.error(message);
					}
					
					// 單身報關單號是否存在
					if(!StringUtils.hasText(originalDeclarationNo)){
					    message = tabName + "中第" + (i + 1) + "項明細商品的報單單號不能為空！";
					    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					    errorMsgs.add(message);
					    log.error(message);
					}else{
					    if(IN == adjustmentType){ // 入倉可能報單不存在
					    }else{
						CmDeclarationOnHand cmDeclarationOnHand = cmDeclarationOnHandDAO.getOnHandById(originalDeclarationNo, NumberUtils.getLong(originalDeclarationSeq), customsWarehouseCode, brandCode);
						if( cmDeclarationOnHand == null ){
						    message = tabName + "中第" + (i + 1) + "項明細報單或項次不存在！";
						    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
						    errorMsgs.add(message);
						    log.error(message);
						}else{
//						    Double currentOnHandQty = cmDeclarationOnHandDAO.getCurrentOnHandQtyByProperty(brandCode, originalDeclarationNo, NumberUtils.getLong(originalDeclarationSeq), actualItemCode, customsWarehouseCode);

//						    log.info( "currentCMOnHandQty = " + currentOnHandQty);
						    if( !actualItemCode.equals(cmDeclarationOnHand.getCustomsItemCode()) ){
							message = "查無" + tabName + "中第" + (i + 1) + "項明細報關單的品號";
							siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
							errorMsgs.add(message);
							log.error(message);
						    }
						}
					    }

					    if(LOSS == adjustmentType || PROFIT == adjustmentType){
						CmDeclarationItem cmDeclarationItem = cmDeclarationItemDAO.findOneCmDeclarationItem(originalDeclarationNo, originalDeclarationSeq);
						if(null == cmDeclarationItem){
						    message = tabName + "中第" + (i + 1) + "項明細報單或項次不存在！";
						    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
						    errorMsgs.add(message);
						    log.error(message);
						}else{
						    Double qty = cmDeclarationItem.getQty();
						    Double difQuantityAbs = Math.abs(difQuantity);
						    if(difQuantityAbs > qty){
							message = tabName + "中第" + (i + 1) + "項明細的盤盈虧絕對值數量("+difQuantityAbs+")不能大於原始報單進貨的數量("+qty+")！";
							siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
							errorMsgs.add(message);
							log.error(message);
						    }
						}
					    }

					}
				    }

				} // 稅別
			    } // 狀態
			}
		    }

		}
	    }else{
		message = tabName + "中請至少輸入一筆調整單明細！";
		log.info( "至少輸入一筆調整單明細 message = " + message );
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }

	    if( "P".equals(taxType) || ( "F".equals(taxType) && !(OrderStatus.SIGNING.equalsIgnoreCase(status) && (F2P == adjustmentType || DISCARD == adjustmentType || OUT == adjustmentType ) )) ){ // 保稅,非狀態簽核中且是保稅轉完稅和報廢
		//==============相同的商品、庫別、批號產生產生集合、相同的報單號碼、報單項次、商品、關別集合============
		Set[] aggregateResult = aggregateOrderItemsQty(head, head.getCreatedBy(), false);
		Iterator it = aggregateResult[0].iterator(); //ImOnHand扣庫存用
		Iterator cmIt = aggregateResult[1].iterator(); //CmOnHand扣庫存用
		//======================================檢核報單庫存量=======================================================
		while(cmIt.hasNext()){
		    Map.Entry cmEntry = (Map.Entry) cmIt.next();
		    Double unCommitQty = (Double) cmEntry.getValue();
		    String[] cmkeyArray = StringUtils.delimitedListToStringArray((String)cmEntry.getKey(), "{$}");
		    Double currentOnHandQty = cmDeclarationOnHandDAO.getCurrentOnHandQtyByProperty(brandCode, cmkeyArray[0], Long.valueOf(cmkeyArray[1]), cmkeyArray[2], cmkeyArray[3]);
		    Double currentOnHandQtyWithBlockQty = cmDeclarationOnHandDAO.getCurrentOnHandQtyWithBlockByProperty(brandCode, cmkeyArray[0], Long.valueOf(cmkeyArray[1]), cmkeyArray[2], cmkeyArray[3]);
		    //Double currentOnHandQtyWithBlockQty = cmDeclarationOnHandDAO.getCurrentOnHandQtyByProperty(brandCode, cmkeyArray[2], cmkeyArray[3]);
		    if(head.getUnBlockOnHand().equals("Y")) currentOnHandQty = currentOnHandQtyWithBlockQty;
		    if(  currentOnHandQty != null ){
			if( unCommitQty < 0D ){
				log.info("unCommitQty:"+unCommitQty + "  currentOnHandQty"+currentOnHandQty);
			    if( Math.abs(unCommitQty) > currentOnHandQty ){
				message = tabName + "中明細商品"+ cmkeyArray[2] +"的報關庫存數量不足("+unCommitQty+","+currentOnHandQty+")";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }
			}
		    }else{
			if( unCommitQty < 0D ){
				log.info("unCommitQty:"+unCommitQty + "  currentOnHandQty"+currentOnHandQty);
			    message = tabName + "中明細商品"+ cmkeyArray[2] +"的報關庫存量不足("+unCommitQty+",0)";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);
			}
		    }
		}
		//======================================檢核實體庫別庫存量=======================================================
		while (it.hasNext()) {
		    Map.Entry entry = (Map.Entry) it.next();
		    Double unCommitQty = (Double) entry.getValue();
		    String[] keyArray = StringUtils.delimitedListToStringArray((String) entry.getKey(), "{$}");
		    Double currentStockOnHandQty = imOnHandDAO.getCurrentStockOnHandQuantity(organizationCode, brandCode, keyArray[0], keyArray[1], keyArray[2] );
		    if(  currentStockOnHandQty != null ){
			if( unCommitQty < 0D ){
			    if( Math.abs(unCommitQty) > currentStockOnHandQty ){
				message = tabName + "中明細倉庫:" + keyArray[1] + " 商品:"+ keyArray[0] +"的實體庫存數量不足("+unCommitQty+","+currentStockOnHandQty+")";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }
			}
		    }else{
			if( unCommitQty < 0D ){
			    message = tabName + "中明細倉庫:" + keyArray[1] + " 商品"+ keyArray[0] +"的實體庫存量不足("+unCommitQty+",0)";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);
			}
		    }
		}
		//===========================================================================================================

	    }

	} catch (Exception e) {
		e.printStackTrace();
	    message = "檢核單明細檔" + tabName + "時發生錯誤，原因：" + e.toString();
	    log.info( "exceptiion message = " + message );
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }

    /**
     * 驗證稅別是保稅
     * @param line
     */
    private void validteTaxTypeIsF(Map conditionMap){
	String lastUpdatedBy = (String)conditionMap.get( "lastUpdatedBy" );
	String message = (String)conditionMap.get( "message" );
	String tabName = (String)conditionMap.get( "tabName" );
	String programId = (String)conditionMap.get( "programId" );
	String identification = (String)conditionMap.get( "identification" );
	List errorMsgs = (List)conditionMap.get( "errorMsgs" );

	ImAdjustmentLine line = (ImAdjustmentLine)conditionMap.get( "line" );
	String itemCode = (String)conditionMap.get( "itemCode" );
	String indexNo = (String)conditionMap.get( "indexNo" );
    }

    /**
     * 驗證稅別是完稅
     * @param line
     */
    private void validteTaxTypeIsP(Map conditionMap){
	String brandCode = (String)conditionMap.get( "brandCode" );
	String taxType = (String)conditionMap.get( "taxType" );
	String lastUpdatedBy = (String)conditionMap.get( "lastUpdatedBy" );
	String message = (String)conditionMap.get( "message" );
	String tabName = (String)conditionMap.get( "tabName" );
	String programId = (String)conditionMap.get( "programId" );
	String identification = (String)conditionMap.get( "identification" );
	List errorMsgs = (List)conditionMap.get( "errorMsgs" );

	Map orderNoMap = (HashMap)conditionMap.get( "orderNoMap" );

	ImAdjustmentLine line = (ImAdjustmentLine)conditionMap.get( "line" );
	String itemCode = (String)conditionMap.get( "itemCode" );
	String indexNo = (String)conditionMap.get( "indexNo" );
	int i = (Integer)conditionMap.get( "i" );
    }

    /**
     * 檢核拆/併貨調整主檔,明細檔
     * @param Head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validateImApartAdjustment(ImAdjustmentHead head, String programId, String identification, List errorMsgs, Object formLinkBean) throws ValidationErrorException, NoSuchObjectException {
	this.validteApartHead(head, programId, identification, errorMsgs, formLinkBean);
	this.validteApartLine(head, programId, identification, errorMsgs);
    }

    /**
     * 檢核拆/併貨調整單主檔
     * @param head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validteApartHead(ImAdjustmentHead head, String programId, String identification, List errorMsgs, Object formLinkBean) throws ValidationErrorException, NoSuchObjectException {
	String message = null;
	String tabName = "主檔資料";
	try {
	    Date adjustmentDate = head.getAdjustmentDate();

	    if( adjustmentDate == null ){
		message = "請輸入" + tabName + "的調整日期！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }else{
			// 是否關帳日
			String dateType = "調整單日期";
			try {
			    ValidateUtil.isAfterClose(head.getBrandCode(), head.getOrderTypeCode(), dateType, adjustmentDate ,head.getSchedule());
			} catch (Exception ex) {
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, ex.getMessage(), head.getCreatedBy());
			}
	    }
	    if( null == head.getSourceOrderTypeCode() || "" == head.getSourceOrderTypeCode() ){
			message = "請輸入" + tabName + "的重整類型！";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
	    }else{
	    	log.info("重整類型"+head.getSourceOrderTypeCode());
	    }
	    
	    Long bef_totalNum = head.getBefTotalItems()==null?0L:head.getBefTotalItems();
	    Long aft_totalNum = head.getAftTotalItems() ==null?0L:head.getAftTotalItems();
	    if(bef_totalNum ==0L){
	    	message = "請輸入重整前總項次！";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
	    }
	    if(aft_totalNum ==0L){
	    	message = "請輸入重整後總項次！";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
	    }

	} catch (Exception e) {
	    message = "檢核拆/併貨調整單主檔單" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }

    /**
     * 檢核拆/併貨調整單明細檔
     * @param head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validteApartLine(ImAdjustmentHead head, String programId,
	    String identification, List errorMsgs) throws ValidationErrorException, NoSuchObjectException {
	String message = null;
	String tabName = "明細資料頁籤";
	Map orderNoMap = new HashMap();
	try{
	    String brandCode = head.getBrandCode();

	    String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
	    String sourceOrderTypeCode = head.getSourceOrderTypeCode();

	    List lines = head.getImAdjustmentLines();

	    int size = lines.size();
	    if( size > 0 ){
		for (int i = 0; i < size; i++) {
		    ImAdjustmentLine line = (ImAdjustmentLine) lines.get(i);
		    String itemCode = line.getItemCode().trim().toUpperCase();
		    String indexNo = String.valueOf( line.getIndexNo() );
		    String lotNo = line.getLotNo();
		    Double localUnitCost = line.getLocalUnitCost();
		    String sourceitemCode = line.getSourceItemCode();

		    if (!StringUtils.hasText(itemCode)) {
			message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的商品！";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		    } else {
//Maco 增加重整單送出效能
			ImItem imItem = null;
			String actualItemCode = null;
			boolean hasItem = false;

			imItem = imItemDAO.findById(itemCode);
			if( imItem != null ){
				actualItemCode = imItem.getItemCode();
				hasItem = true;
			}else{
				ImItemEanPriceView imItemEanPriceView = imItemEanPriceViewDAO.getItemInfoByProperty(brandCode, itemCode);
				if( imItemEanPriceView != null ){
				    imItem = imItemDAO.findItem( brandCode, imItemEanPriceView.getItemCode() );
				    actualItemCode = imItem.getItemCode();
				    hasItem = true;
				}else{
					hasItem = false;
				}
			}			

			if(!hasItem){
			    message = tabName + "中第" + (i + 1) + "項明細的商品不存在！";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);
			} else {
			    String warehouseCode = line.getWarehouseCode();
			    Double difQuantity = line.getDifQuantity();

			    log.info( "difQuantity = " + difQuantity );

			    // 差異數量
			    if(difQuantity == 0D ){
				message = tabName + "中第" + (i + 1) + "項明細商品的差異數量不能為0";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }
			    // 重整前後序號
/*			    if(line.getBeAft() == null ){
				message = tabName + "中第" + (i + 1) + "項明細商品的重整前後序號不可為空";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }*/
			    if(line.getItemNo() == null ){
					message = tabName + "中第" + (i + 1) + "項明細商品的重整前後序號不可為空";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				    }
			    // 單項金額
//			    if(localUnitCost == 0D ){
//			    message = tabName + "中第" + (i + 1) + "項明細商品的單項金額不能為0";
//			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//			    errorMsgs.add(message);
//			    log.error(message);
//			    }
			    
			    String be_aft = line.getBeAft()==null?"":line.getBeAft();
			    if(be_aft.equals("")){
			    	message = tabName + "中第" + (i + 1) + "項明細商品的重整前後編號不能為空";
				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				    errorMsgs.add(message);
				    log.error(message);
			    }
			    String cwMark = line.getCigarWineMark()==null?"NULL":line.getCigarWineMark();
			    if(cwMark.equals("NULL")){
			    	message = tabName + "中第" + (i + 1) + "項明細商品的重整前後菸酒註記不能為空";
				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				    errorMsgs.add(message);
				    log.error(message);
			    }
			    String decl = line.getOriginalDeclarationNo() ==null?"":line.getOriginalDeclarationNo();
			    if(decl.equals("")){
			    	message = tabName + "中第" + (i + 1) + "項明細商品的報單號碼不能為空";
				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				    errorMsgs.add(message);
				    log.error(message);
			    }
			    
			    Long declNo = line.getOriginalDeclarationSeq() ==null?0L:line.getOriginalDeclarationSeq();
			    if(declNo == 0L){
			    	message = tabName + "中第" + (i + 1) + "項明細商品的報單項次不能為空";
				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				    errorMsgs.add(message);
				    log.error(message);
			    }
			    
//				    Long import_tax = line.getImportTax() ==null?0L:line.getImportTax();
//				    Long cigarWineTax = line.getCigarWineTax() ==null?0L:line.getCigarWineTax();
//				    Long goodsTax = line.getGoodsTax() ==null?0L:line.getGoodsTax();
//				    Long health = line.getHealth() ==null?0L:line.getHealth();
//				    Double advDutyRate = line.getAdvDutyRate() ==null?0.0:line.getAdvDutyRate();
//				    Double cigarWineTaxRate = line.getCigarWineTaxRate() ==null?0.0:line.getCigarWineTaxRate();
//				    Double goodsTaxRate = line.getGoodsTaxRate() ==null?0.0:line.getGoodsTaxRate();
//				    if(import_tax == 0L){
//				    	message = tabName + "中第" + (i + 1) + "項明細商品的重整前/後進口稅不能為空";
//					    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//					    errorMsgs.add(message);
//					    log.error(message);
//				    }
//				    if(cigarWineTax == 0L){
//				    	message = tabName + "中第" + (i + 1) + "項明細商品的重整前/後菸酒稅不能為空";
//					    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//					    errorMsgs.add(message);
//					    log.error(message);
//				    }
//				    if(goodsTax == 0L){
//				    	message = tabName + "中第" + (i + 1) + "項明細商品的重整前/後貨物稅不能為空";
//					    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//					    errorMsgs.add(message);
//					    log.error(message);
//				    }
//				    if(health == 0L){
//				    	message = tabName + "中第" + (i + 1) + "項明細商品的重整前/後健康福利捐不能為空";
//					    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//					    errorMsgs.add(message);
//					    log.error(message);
//				    }
//				    if(advDutyRate == 0.0){
//				    	message = tabName + "中第" + (i + 1) + "項明細商品的重整前/後進口稅率不能為空";
//					    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//					    errorMsgs.add(message);
//					    log.error(message);
//				    }
//				    if(cigarWineTaxRate ==  0.0){
//				    	message = tabName + "中第" + (i + 1) + "項明細商品的重整前/後菸酒稅稅率不能為空";
//					    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//					    errorMsgs.add(message);
//					    log.error(message);
//				    }
//				    if(goodsTaxRate ==  0.0){
//				    	message = tabName + "中第" + (i + 1) + "項明細商品的重整前/後貨物稅率不能為空";
//					    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//					    errorMsgs.add(message);
//					    log.error(message);
//				    }
			    
			    log.info("organizationCode = " + organizationCode );
			    log.info("actualItemCode = " + actualItemCode );
			    log.info("warehouseCode = " + warehouseCode );
			    log.info("lotNo = " + lotNo );

			    // 倉別
			    ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(head.getBrandCode(), warehouseCode, "Y");
			    if(imWarehouse == null){
				message = "查無" + tabName + "中第" + (i + 1) + "項明細商品的庫別！";
				log.info( "倉別 message = " + message );
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }else{
				if(!StringUtils.hasText(lotNo)){
				    message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的批號！";
				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				    errorMsgs.add(message);
				    log.error(message);
				}else{/*
				    Double currentStockOnHandQty = imOnHandDAO.getCurrentStockOnHandQuantity(organizationCode, brandCode, actualItemCode, warehouseCode, lotNo );
				    log.info("currentStockOnHandQty = " + currentStockOnHandQty );
//				    if( currentStockOnHandQty == null ){
//				    message = "查無" +tabName + "中第" + (i + 1) + "項在庫別" +warehouseCode+ "的商品" + itemCode;
//				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//				    errorMsgs.add(message);
//				    log.error(message);
//				    }
				    if( currentStockOnHandQty != null ){
					if( ( difQuantity < 0D  && ( Math.abs(difQuantity) > currentStockOnHandQty ) )){
					    message = tabName + "中第" + (i + 1) + "項明細的商品數量之可用庫存量不足";
					    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					    errorMsgs.add(message);
					    log.error(message);
					}
				    }else{
					if( difQuantity < 0D ){
					    message = tabName + "中第" + (i + 1) + "項明細的商品數量之可用庫存量不足";
					    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					    errorMsgs.add(message);
					    log.error(message);
					}
				    }*/
				}
			    }

			 // 併貨:負數明細要key來源品號
			    // 拆貨:正數明細要key來源品號
			    if("DISTRIBUTE".equalsIgnoreCase(sourceOrderTypeCode) || "CHANGE_ITEM".equalsIgnoreCase(sourceOrderTypeCode) ){
				String dis = "拆貨";
				String chg = "換貨號";
				String key = null;
				if("DISTRIBUTE".equalsIgnoreCase(sourceOrderTypeCode)){
				    key = dis;
				}else{
				    key = chg;
				}
				if(difQuantity > 0){
				    if(!StringUtils.hasText(sourceitemCode)){
					log.error("查" + tabName + "中第" + (i + 1) + "項明細商品"+key+"差異數量為正數，應輸入關聯品號");
					message = ("查" + tabName + "中第" + (i + 1) + "項明細商品"+key+"差異數量為正數，應輸入關聯品號\n");
					errorMsgs.add(message);

				    } else{
					itemCode = sourceitemCode;
				    }
				}
			    }else if("COMBINE".equalsIgnoreCase(sourceOrderTypeCode)){
				if(difQuantity < 0){
				    if(!StringUtils.hasText(sourceitemCode)){
					log.error("查" + tabName + "中第" + (i + 1) + "項明細商品併貨差異數量為負數，應輸入關聯品號");
					message = ("查" + tabName + "中第" + (i + 1) + "項明細商品併貨差異數量為負數，應輸入關聯品號\n");
					errorMsgs.add(message);
				    } else{
					itemCode = sourceitemCode;
				    }
				}
			    }
				    // 關聯品號
//				    if(!StringUtils.hasText(sourceitemCode)){
//					message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的關聯品號！";
//					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//					errorMsgs.add(message);
//					log.error(message);
//				    }

			    // 是否已做過庫存
			    if ("Y".equalsIgnoreCase(head.getOnHandStatus())) {
				message = head.getOrderNo() + "已經做過ON_HAND,請再確認資料後重新執行核銷動作";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }

			    // 商品重複
//			    if( orderNoMap.containsKey( actualItemCode ) ){
//			    message = tabName + "中第" + (i + 1) + "項明細的商品" + itemCode + "重複！";
//			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//			    errorMsgs.add(message);
//			    log.error(message);
//			    }else{
//			    orderNoMap.put( actualItemCode ,  indexNo );
//			    }

			}
		    }

		}
	    }else{
		message = tabName + "中請至少輸入一筆調整單明細！";
		log.info( "至少輸入一筆調整單明細 message = " + message );
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	} catch (Exception e) {
	    message = "檢核調整單明細檔" + tabName + "時發生錯誤，原因：" + e.toString();
	    log.info( "exceptiion message = " + message );
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }

    /**
     * 匯出 by sql
     * @return
     * @throws Exception
     */
    public SelectDataInfo getAJAXExportDataHeadBySql(HttpServletRequest httpRequest) throws Exception{
    	Object[] object = null;
    	List rowData = new ArrayList();
    	StringBuffer sql = new StringBuffer();
    	try {
    		Long headId = NumberUtils.getLong(httpRequest.getParameter("headId"));

    		sql.append(" SELECT '' AS T1, '' AS MsgFun, '' AS BondNo, '' AS StrType, ")
    		   .append(" '' AS GdsType,'IA' || H.ORDER_NO AS RefBillNo, '' AS CtmCode, '' AS ReferCode ")
    		   .append(" FROM IM_ADJUSTMENT_HEAD H WHERE H.HEAD_ID = ")
    		   .append(headId);

    		object = new Object[] {
    			"T1", "MsgFun", "BondNo", "StrType",
    			"GdsType", "RefBillNo", "CtmCode", "ReferCode"};

    		List lists = nativeQueryDAO.executeNativeSql(sql.toString());
    		for (int i = 0; i < (lists.size() > 65535 ? 65535 : lists.size()); i++) {
			Object[] getObj = (Object[])lists.get(i);
			Object[] dataObject = new Object[object.length];
			getObj[0] = "T1";
			getObj[1] = "9";
			getObj[2] = "CD190";
			getObj[3] = "2";
			getObj[4] = "I";
			getObj[6] = "CD190";
			getObj[7] = "BC";
    			for (int j = 0; j < (object.length); j++) {
    				dataObject[j] = getObj[j];
    			}
    			rowData.add(dataObject);
    		}
    		return new SelectDataInfo(object, rowData);
    	} catch (Exception e) {
    		throw new Exception("SQL查詢發生錯誤，原因：" + e.getMessage());
    	}
    }

    /**
     * 匯出 by sql
     * @return
     * @throws Exception
     */
    public SelectDataInfo getAJAXExportDataLineBySql(HttpServletRequest httpRequest) throws Exception{
    	Object[] object = null;
    	List rowData = new ArrayList();
    	StringBuffer sql = new StringBuffer();
    	try {
    		Long headId = NumberUtils.getLong(httpRequest.getParameter("headId"));

    	         sql.append(" SELECT '' AS T2, L.INDEX_NO AS Item, L.ITEM_CODE AS PrdtNo, ABS(L.DIF_QUANTITY) AS RStrQty,")
    	            .append(" D.UNIT AS StrUnit, 'A' AS StrPost, TO_CHAR (H.ADJUSTMENT_DATE, 'YYYYMMDD') AS StrDate,'I' AS DeclType, ")
    	            .append(" 'IA'|| H.ORDER_NO AS DeclNo, L.INDEX_NO AS ItemNo, L.ORIGINAL_DECLARATION_NO As ODeclNo, L.ORIGINAL_DECLARATION_SEQ As OItemNo, ")
    	            .append(" L.ITEM_CODE AS TradePrdtNo, D.O_DECL_NO AS OODeclNo, D.O_ITEM_NO AS OOItemNo ")
    	            .append(" FROM IM_ADJUSTMENT_HEAD H, IM_ADJUSTMENT_LINE L, CM_DECLARATION_ITEM D ")
    	            .append(" WHERE L.ORIGINAL_DECLARATION_NO = D.DECL_NO AND L.ORIGINAL_DECLARATION_SEQ = D.ITEM_NO ")
    	            .append(" AND H.HEAD_ID = L.HEAD_ID AND H.HEAD_ID = ")
    	            .append(headId);

    		object = new Object[] {
    			"T2", "Item", "PrdtNo", "RStrQty",
    			"StrUnit", "StrPost", "StrDate", "DeclType",
    			"DeclNo", "ItemNo", "ODeclNo", "OItemNo",
    			"TradePrdtNo", "ODeclNo", "OItemNo"};

    		List lists = nativeQueryDAO.executeNativeSql(sql.toString());
    		for (int i = 0; i < (lists.size() > 65535 ? 65535 : lists.size()); i++) {
    			Object[] getObj = (Object[])lists.get(i);
    			Object[] dataObject = new Object[object.length];
    			getObj[0] = "T2";
    			getObj[7] = "I";
    			for (int j = 0; j < (object.length); j++) {
    				dataObject[j] = getObj[j];
    			}
    			rowData.add(dataObject);
    		}
    		return new SelectDataInfo(object, rowData);
    	} catch (Exception e) {
    		throw new Exception("SQL查詢發生錯誤，原因：" + e.getMessage());
    	}
    }
    
    public void saveResNo(ImAdjustmentHead head) throws NoSuchObjectException{
        System.out.println("取重整申請書號碼開始");       
		//MACO 2016.01.30 superviseCode
		String superviseCodeT2=buCommonPhraseLineDAO.findById("SuperviseCode","T2Warehouse").getName();
	try{
		Long seq = Long.valueOf(imAdjustmentHeadDAO.getResNoSq().get(0).toString());
        System.out.println("QQQQQQQQQQQQQ:"+seq);
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(System.currentTimeMillis());
        int year=cl.get(Calendar.YEAR)-1911;
        String ResNo = "P" + superviseCodeT2 + String.valueOf(year).substring(1) + String.format("%06d",seq) ;
        System.out.println("LLLLLLLLLLLLLLLLLLLLLLLL:"+ResNo);
        head.setResNo(ResNo);
		System.out.println("更新移倉申請書號碼開始:更新單號以及最後更新日期及人員開始");
		
		imAdjustmentHeadDAO.merge(head);//更新成正式單號
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	System.out.println("更新移倉申請書號碼開始:更新單號以及最後更新日期及人員結束");
    }
    
    
    public void saveIslandResNo(ImAdjustmentHead head) throws NoSuchObjectException{
        System.out.println("取重整申請書號碼開始");       
		//MACO 2016.01.30 superviseCode
		String superviseCodeT2=buCommonPhraseLineDAO.findById("SuperviseCode","MSWarehouse").getName();
	try{
		Long seq = Long.valueOf(imAdjustmentHeadDAO.getResNoSq().get(0).toString());
        System.out.println("QQQQQQQQQQQQQ:"+seq);
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(System.currentTimeMillis());
        int year=cl.get(Calendar.YEAR)-1911;
        String ResNo = "P" + superviseCodeT2 + String.valueOf(year).substring(1) + String.format("%06d",seq) ;
        System.out.println("LLLLLLLLLLLLLLLLLLLLLLLL:"+ResNo);
        head.setResNo(ResNo);
		System.out.println("更新移倉申請書號碼開始:更新單號以及最後更新日期及人員開始");
		
		imAdjustmentHeadDAO.merge(head);//更新成正式單號
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	System.out.println("更新移倉申請書號碼開始:更新單號以及最後更新日期及人員結束");
    }
/*
    private void insertToTradeVan(ImAdjustmentHead modifyObj)throws FormException, Exception{
    	
		
		
		
		int aft = 0;
		int bef = 0;
		String resNo = "";
		String resDateTime = "";
		String processCustCd = "";
		String resStoreCode = "";
		String resWay = "";
		String aftTotalItems = "";
		String befTotalItems = "";
		String note = "";
		
		
		if(modifyObj.getOrderTypeCode().equals("BEF")){
			System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUUUUU");
			List lst_STORET1 = new ArrayList(23);			
			Map map_items2 = new HashMap();
			Map map_items3 = new HashMap();
			IslandExportDAO islandExportDAO = (IslandExportDAO) SpringUtils.getApplicationContext().getBean("islandExportDAO");			
			resNo =  modifyObj.getResNo();
			resDateTime = modifyObj.getAdjustmentDate().toString();
			processCustCd = modifyObj.getProcessCustCd();
			resStoreCode = modifyObj.getResStoreCode();
			resWay = modifyObj.getSourceOrderTypeCode();
			if(resWay.equals("COMBINE")){
				resWay = "1";
			}else{
				resWay = "2";
			}
			aftTotalItems = modifyObj.getAftTotalItems().toString();
			befTotalItems = modifyObj.getBefTotalItems().toString();
			note = modifyObj.getReason();
			
			  if(modifyObj.getStatus().equals("FINISH")){
				    
				  	lst_STORET1.add(0, "");
					lst_STORET1.add(1, "");
					lst_STORET1.add(2, "");
					lst_STORET1.add(3, "");
					lst_STORET1.add(4, "");
					lst_STORET1.add(5, "");
					lst_STORET1.add(6, "");
					lst_STORET1.add(7, "");					
					lst_STORET1.add(8, resNo);
					lst_STORET1.add(9, resDateTime);
					lst_STORET1.add(10, processCustCd);
					lst_STORET1.add(11, resStoreCode);
					lst_STORET1.add(12, resWay); 
					lst_STORET1.add(13, aftTotalItems);
					lst_STORET1.add(14, befTotalItems);
					lst_STORET1.add(15, note);
					lst_STORET1.add(16, "");
					lst_STORET1.add(17, "");
					lst_STORET1.add(18, "");
					lst_STORET1.add(19, "");
					lst_STORET1.add(20, "");
					lst_STORET1.add(21, "");
					lst_STORET1.add(22, "");
					
					List<ImAdjustmentLine> imt = modifyObj.getImAdjustmentLines();
					System.out.println("item.size:"+imt.size());
					for (ImAdjustmentLine item : imt){
					   if(item.getSourceItemCode() == null){
						   List lst_STORET2 = new ArrayList(16);						   
						   lst_STORET2.add(0, "");
						   lst_STORET2.add(1, item.getItemNo());
						   lst_STORET2.add(2, item.getCustomsItemCode());
						   lst_STORET2.add(3, item.getItemCode());
						   lst_STORET2.add(4, item.getItemCName());
						   lst_STORET2.add(5, item.getSpecDesc());
						   lst_STORET2.add(6, item.getDifQuantity());
						   lst_STORET2.add(7, item.getUnit());					   
						   lst_STORET2.add(8, item.getImportTax());					  
						   lst_STORET2.add(9, item.getGoodsTax());					   
						   lst_STORET2.add(10, item.getCigarWineTax());					  
						   lst_STORET2.add(11, item.getHealth());				   
						   lst_STORET2.add(12, item.getAdvDutyRate());					   
						   lst_STORET2.add(13, item.getCigarWineTaxRate());					   
						   lst_STORET2.add(14, item.getGoodsTaxRate());
						   lst_STORET2.add(15, item.getCigarWineMark());
						  
						   map_items2.put(aft, lst_STORET2);
						   aft++;   
					   }else{
						   List lst_STORET3 = new ArrayList(20);
						   lst_STORET3.add(0, "");
						   lst_STORET3.add(1, item.getItemNo());
						   lst_STORET3.add(2, item.getCustomsItemCode());
						   lst_STORET3.add(3, item.getItemCode());
						   lst_STORET3.add(4, item.getItemCName());
						   lst_STORET3.add(5, item.getSpecDesc());
						   lst_STORET3.add(6, item.getDifQuantity());
						   lst_STORET3.add(7, item.getUnit());					   
						   lst_STORET3.add(8, item.getImportTax());					  
						   lst_STORET3.add(9, item.getGoodsTax());					   
						   lst_STORET3.add(10, item.getCigarWineTax());					  
						   lst_STORET3.add(11, item.getHealth());				   
						   lst_STORET3.add(12, item.getAdvDutyRate());					   
						   lst_STORET3.add(13, item.getCigarWineTaxRate());					   
						   lst_STORET3.add(14, item.getGoodsTaxRate());
						   lst_STORET3.add(15, item.getCigarWineMark());					   
						   lst_STORET3.add(16, item.getOriginalDeclarationNo());
						   lst_STORET3.add(17, item.getOriginalDeclarationSeq());					   
						   lst_STORET3.add(18, item.getOrgMoveWhNo());
						   lst_STORET3.add(19, item.getOrgMoveWhItemNo());
						  
						   map_items3.put(bef, lst_STORET3);
						   bef++;
					   }					   
					}
					System.out.println("Test");
					islandExportDAO.INSERT_DF12T1(lst_STORET1, map_items2, map_items3);					
			  }			
		}		
    }
 */
    public List<Properties> updateCustomsStatus(Map parameterMap) throws Exception{    	
    	Map returnMap = new HashMap(0);
    	MessageBox msgBox = new MessageBox();
    	Map resultMap = new HashMap();
    	try{
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    
    	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
//    	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
    	    
    	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);
    	    String id = (String)PropertyUtils.getProperty(otherBean, "headId");   
    	    String cw = (String)PropertyUtils.getProperty(otherBean, "customsWarehouse");   
    	    Long headId = NumberUtils.getLong(id);
    	    
    	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
    	    ImAdjustmentHead imAdjustmentHead = imAdjustmentHeadDAO.findById(headId);
    	    
    	    
    	    String tranStatus = (String)PropertyUtils.getProperty(otherBean, "tranStatus");
    	    if(tranStatus.equals("cancel")){
    	    	imAdjustmentHead.setTranAllowUpload("D");
    	    }else{
    	    	imAdjustmentHead.setTranAllowUpload("I");
    	    }
    	    imAdjustmentHead.setCustomsStatus("A");
    	    if(orderTypeCode.equals("ADF")||orderTypeCode.equals("AEF")||orderTypeCode.equals("MEF")||orderTypeCode.equals("MDF")){
       	     if(tranStatus.equals("E")){
       	    	 log.info("調整單改狀態NF14");
       	    	imAdjustmentHead.setTranRecordStatus("NF14");
       	    	imAdjustmentHead.setcStatus("E");
       	     }	
       	    }else if(orderTypeCode.equals("BEF")&&cw.equals("FW")){
       	    	imAdjustmentHead.setTranRecordStatus("NF12");  //重整
       	    }else if(orderTypeCode.equals("BEF")&&cw.equals("KW")){
       	    	imAdjustmentHead.setTranRecordStatus("DF12");  //重整
       	    }
       	    else if(orderTypeCode.equals("AEG")){
       	    	imAdjustmentHead.setTranRecordStatus("NF11");  //重整
       	    }else if(orderTypeCode.equals("MEG")){
    	    	imAdjustmentHead.setTranRecordStatus("DF11");  
    	    }else{
    	    	throw new Exception("上傳失敗，原因：");
    	    }
    	    imAdjustmentHeadDAO.save(imAdjustmentHead);
    	    
    	    msgBox.setMessage("已將單據送簽海關");
    	    
    	}catch(Exception ex){
    	    log.error("上傳海關失敗，原因：" + ex.toString());
    	    msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
    	    throw new Exception("上傳失敗，原因：" + ex.toString());

    	}
    	returnMap.put("vatMessage", msgBox);
    	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
	
	/**
     * 處理AJAX參數(在APF單上依據AAF來源單號，檢查是否有無來源單號)
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> findSourceOrderNoForAJAX(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	String sourceOrderNo = "";
    	String sourceOrderTypeCode = "";
    	String brandCode = "";    	
    	try{
    		brandCode = httpRequest.getProperty("brandCode");
    		log.info("brandCode="+brandCode);
    		sourceOrderNo = httpRequest.getProperty("sourceOrderNo");
    		log.info("sourceOrderNo="+sourceOrderNo);
    		sourceOrderTypeCode = httpRequest.getProperty("sourceOrderTypeCode");
    		log.info("sourceOrderTypeCode="+sourceOrderTypeCode);
    		List<ImAdjustmentHead> imAdjustmentHeads = imAdjustmentHeadDAO.findAdjustForAIFOrderNo(brandCode, sourceOrderTypeCode, sourceOrderNo);
    		if(imAdjustmentHeads == null || imAdjustmentHeads.size() == 0){
        		properties.setProperty("errorMsg", "查無該AAF單！");
    	    }
    		result.add(properties);
    	}catch(Exception ex){
    		log.error("依據品牌代號：" + brandCode + "、AAF單號：" + sourceOrderNo + "查詢資料時發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢AAF單資料失敗！");
    	}
    	return result;
    }
    
	 public List<Properties> updateCopyDetail(Properties httpRequest) throws Exception{
		 log.info("copyDetail");
			List list = new ArrayList();
			String errorMsg = null;
			Date todayDate = new Date();
			String headIdString = httpRequest.getProperty("headId");
			Long headId =  StringUtils.hasText(headIdString)? Long.valueOf(headIdString):null;
			ImAdjustmentHead imAdjustmentHead = imAdjustmentHeadDAO.findById(headId);
			Properties properties = new Properties();
			String loginEmployee = httpRequest.getProperty("loginEmployee");
			if(imAdjustmentHead!=null)
			{
				log.info("imAdjustmentHead:"+imAdjustmentHead.getHeadId());
				List<ImAdjustmentLine> imAdjustmentLines = imAdjustmentHead.getImAdjustmentLines();
				 if (imAdjustmentLines != null && imAdjustmentLines.size() > 0)
				 {
					 log.info("listSize:"+imAdjustmentLines.size());
					 Long indexNo = imAdjustmentLines.size()+0L;
					properties.setProperty("befTotalItems", indexNo.toString());
					properties.setProperty("aftTotalItems", indexNo.toString());
					 for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines)
					 {
						 indexNo++;
						 log.info("indexNo:"+indexNo);
						 ImAdjustmentLine newImAdjustmentLine = new ImAdjustmentLine();
						 newImAdjustmentLine.setImAdjustmentHead(imAdjustmentHead);
						 newImAdjustmentLine.setItemCode(imAdjustmentLine.getItemCode());
						 newImAdjustmentLine.setWarehouseCode(imAdjustmentLine.getWarehouseCode());
						 newImAdjustmentLine.setLotNo(imAdjustmentLine.getLotNo());
						 newImAdjustmentLine.setQuantity(imAdjustmentLine.getQuantity());
						 newImAdjustmentLine.setAmount(imAdjustmentLine.getAmount());
						 newImAdjustmentLine.setIndexNo(indexNo);
						 newImAdjustmentLine.setActualQuantity(imAdjustmentLine.getActualQuantity());
						 newImAdjustmentLine.setDifQuantity(imAdjustmentLine.getDifQuantity());
						 newImAdjustmentLine.setItemCName(imAdjustmentLine.getItemCName());
						 newImAdjustmentLine.setWarehouseName(imAdjustmentLine.getWarehouseName());
						 newImAdjustmentLine.setLocalUnitCost(imAdjustmentLine.getLocalUnitCost());
						 newImAdjustmentLine.setOriginalDeclarationNo(imAdjustmentLine.getOriginalDeclarationNo());
						 newImAdjustmentLine.setOriginalDeclarationSeq(imAdjustmentLine.getOriginalDeclarationSeq());
						 newImAdjustmentLine.setOriginalDeclarationDate(imAdjustmentLine.getOriginalDeclarationDate());
						 newImAdjustmentLine.setIsLockRecord(imAdjustmentLine.getIsLockRecord());
						 newImAdjustmentLine.setUnit(imAdjustmentLine.getUnit());
						 newImAdjustmentLine.setCigarWineMark(imAdjustmentLine.getCigarWineMark());
						 newImAdjustmentLine.setItemNo(imAdjustmentLine.getItemNo());
						 newImAdjustmentLine.setBeAft("2");
						 newImAdjustmentLine.setUnitCost(imAdjustmentLine.getUnitCost());
						 newImAdjustmentLine.setSpecDesc(imAdjustmentLine.getItemCode());
						 newImAdjustmentLine.setCreationDate(todayDate);
						 newImAdjustmentLine.setCreatedBy(loginEmployee);
						 newImAdjustmentLine.setLastUpdateDate(todayDate);
						 newImAdjustmentLine.setLastUpdatedBy(loginEmployee);
						 imAdjustmentLineDAO.save(newImAdjustmentLine);
					 }
				 }
			}
			else
			{
				
			}

			list.add(properties);	
			return list;
		}
	 
	    public static Object[] startApartProcess(ImAdjustmentHead form) throws ProcessFailedException{       
	        try{           
		       String packageId = "Im_GeneralAdjustment";
		       String processId = "Im_Apart_Adjustment";  
		       String version   = "20160506";
		       String sourceReferenceType = "Im_Apart(1)";
		       HashMap context = new HashMap();
		       context.put("brandCode", form.getBrandCode());
		       context.put("formId",    form.getHeadId());
		       context.put("orderType", form.getOrderTypeCode());
		       context.put("orderNo", form.getOrderNo() );
		       return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		   }catch (Exception ex){
		       log.error("Invoice 流程啟動失敗，原因：" + ex.toString());
		       throw new ProcessFailedException("Invoice 流程啟動失敗！");
		   }
	    }
	    public String updateApartAdjustment(ImAdjustmentHead imAdjustmentHead)
	    throws ObtainSerialNoFailedException, FormException, Exception {
log.info("updateFiInvoice");
imAdjustmentHead.setLastUpdateDate(new Date());
fiInvoiceHeadDAO.update(imAdjustmentHead);
    return imAdjustmentHead.getOrderTypeCode() + "-" + imAdjustmentHead.getOrderNo() + "存檔成功！";
}
		public ImAdjustmentHead findById(Long headId) {
			return imAdjustmentHeadDAO.findById(headId);
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
			
			
			String type = httpRequest.getParameter("type");
			String orderTypeCode = httpRequest.getParameter("orderTypeCode");
			String status = httpRequest.getParameter("status");
			String adjustmentDateStart = httpRequest.getParameter("adjustmentDateStart");
			String adjustmentDateEnd = httpRequest.getParameter("adjustmentDateEnd");
			String declarationNo = httpRequest.getParameter("declarationNo");
			String loginBrandCode = httpRequest.getParameter("loginBrandCode");
			
			log.info("type:"+type+" orderTypeCode:"+orderTypeCode+" status:"+status+" adjustmentDateStart:"+adjustmentDateStart+" adjustmentDateEnd:"+adjustmentDateEnd+" declarationNo:"+declarationNo);
			
			
			
			if("search".equals(type)){
				sql.append("SELECT A.ORDER_TYPE_CODE , A.ORDER_NO,A.ADJUSTMENT_DATE,A.DECLARATION_NO,A.STATUS ,A.LAST_UPDATE_DATE FROM IM_ADJUSTMENT_HEAD A WHERE SUBSTR(ORDER_NO,1,3) <> 'TMP' AND A.BRAND_CODE='T2'");
				
				if (StringUtils.hasText(orderTypeCode))
					sql.append(" and A.ORDER_TYPE_CODE ='" + orderTypeCode+"'");
				
				if (StringUtils.hasText(loginBrandCode))
					sql.append(" and A.BRAND_CODE ='" + loginBrandCode+"'");
				
				if (StringUtils.hasText(declarationNo))
					sql.append(" and A.DECLARATION_NO ='" + declarationNo+"'");

				if (StringUtils.hasText(adjustmentDateStart))
					sql.append(" and A.ADJUSTMENT_DATE >=TO_DATE('"+adjustmentDateStart+"','yyyy/mm/dd')");

				if (StringUtils.hasText(adjustmentDateStart))
					sql.append(" and A.ADJUSTMENT_DATE <=TO_DATE('"+adjustmentDateEnd+"','yyyy/mm/dd')");

				if (StringUtils.hasText(status))
					sql.append(" and A.STATUS ='" + status+"'"); 
				
				
				
			object = new Object[] { "orderTypeCode", "orderNo", "adjustmentDate", "declarationNo", "status", "lastUpdateDate" };
				
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
}