package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.InsufficientQuantityException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCustomerWithAddressView;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImOnHandId;
import tw.com.tm.erp.hbm.bean.ImPromotionView;
import tw.com.tm.erp.hbm.bean.ImTransation;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderPayment;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCountryDAO;
import tw.com.tm.erp.hbm.dao.BuCurrencyDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeApprovalDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.hbm.dao.BuPaymentTermDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionViewDAO;
import tw.com.tm.erp.hbm.dao.ImTransationDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.SoPostingTallyDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderItemDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderPaymentDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;
import tw.com.tm.erp.utils.sp.AppGetSaleItemInfoService;

/**
 * @author T15394
 *
 */
public class SoSalesOrderService {

    private static final Log log = LogFactory.getLog(SoSalesOrderService.class);
    
    public static final String PROGRAM_ID= "SO_SALES_ORDER";
    
    private static final String POSTYPECODE = "SOP";
    
    private static final String SOCTYPECODE = "SOC";
    
    private final static String SYSTEM_CONFIG_FILE = "/system_config.properties";
    
    private final static String MAIL_SKIN = "CommonTemplate.ftl";
    
    private final static String T1_POS_ADMIN = "T1POS_Administrator";
    
    private BuCountryDAO buCountryDAO;
    
    private BuCurrencyDAO buCurrencyDAO;
    
    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    
    private BuCustomerWithAddressViewDAO buCustomerWithAddressViewDAO;
    
    private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO ;

    private BuOrderTypeDAO buOrderTypeDAO;
    
    private BuOrderTypeApprovalDAO buOrderTypeApprovalDAO;

    private BuOrderTypeService buOrderTypeService;
    
    private BuPaymentTermDAO buPaymentTermDAO;
    
    private BuShopDAO buShopDAO;
    
    private BuShopMachineDAO buShopMachineDAO;
    
    private ImDeliveryHeadDAO imDeliveryHeadDAO;

    private ImDeliveryLineDAO imDeliveryLineDAO;
    
    private ImWarehouseDAO imWarehouseDAO;
    
    private ImItemDAO imItemDAO;
    
    private ImItemPriceOnHandViewDAO imItemPriceOnHandViewDAO;

    private ImOnHandDAO imOnHandDAO;
    
    private ImPromotionViewDAO imPromotionViewDAO;
    
    private ImTransationDAO imTransationDAO;

    private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
    
    private SoSalesOrderItemDAO soSalesOrderItemDAO;
    
    private SoSalesOrderPaymentDAO soSalesOrderPaymentDAO;
    
    private SoPostingTallyDAO soPostingTallyDAO;
    
    private BuShopMachineService buShopMachineService;
    
    private AppGetSaleItemInfoService appGetSaleItemInfoService;
    
    private SoSalesOrderPaymentService soSalesOrderPaymentService;
    
    private ImDeliveryService imDeliveryService;
    
    private BuCommonPhraseService buCommonPhraseService;
    
    private SiProgramLogAction siProgramLogAction;
    
    public static final String[] GRID_FIELD_NAMES = { "indexNo", "itemCode", "itemCName", "warehouseCode", "warehouseName", 
	"originalUnitPrice", "actualUnitPrice", "currentOnHandQty", "quantity", "originalSalesAmount","actualSalesAmount", 
	"deductionAmount", "discountRate", "taxType", "taxRate", "isTax", "promotionCode", "discountType", "discount",
	"vipPromotionCode", "vipDiscountType", "vipDiscount", "watchSerialNo", "depositCode", "isUseDeposit", "isServiceItem",
	"taxAmount", "lineId", "isLockRecord", "isDeleteRecord", "message"};
    
    public static final int[] GRID_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, 
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,	
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_LONG, 
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};
    
    public static final String[] GRID_FIELD_DEFAULT_VALUES = { "0", "", "", "", "", "", "", "", "", "", "", "0.00", "100.0", "3", "5", "1", "", "", "", "", 
	"", "", "", "", "", "", "0.0", "", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""};
       
    public static final String[] GRID_FIELD_NAMES_PAYMENT = { "indexNo", "posPaymentType", "localCurrencyCode", "localAmount", "discountRate", 
	"remark1", "posPaymentId", "isLockRecord", "isDeleteRecord", "message"};
    
    public static final int[] GRID_FIELD_TYPES_PAYMENT = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};
    
    public static final String[] GRID_FIELD_DEFAULT_VALUES_PAYMENT = { "0", "", "NTD", "", "", "", "", AjaxUtils.IS_LOCK_RECORD_FALSE, 
	AjaxUtils.IS_DELETE_RECORD_FALSE, ""}; 

    /* Spring IoC */
    
    public void setBuCountryDAO(BuCountryDAO buCountryDAO) {
	this.buCountryDAO = buCountryDAO;
    }
    
    public void setBuCurrencyDAO(BuCurrencyDAO buCurrencyDAO) {
	this.buCurrencyDAO = buCurrencyDAO;
    }
    
    public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
	this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    }
    
    public void setBuCustomerWithAddressViewDAO(
	    BuCustomerWithAddressViewDAO buCustomerWithAddressViewDAO) {
	this.buCustomerWithAddressViewDAO = buCustomerWithAddressViewDAO;
    }
    
    public void setBuEmployeeWithAddressViewDAO(
		BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
	this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
    }
    
    public void setBuOrderTypeDAO(BuOrderTypeDAO buOrderTypeDAO) {
	this.buOrderTypeDAO = buOrderTypeDAO;
    }
    
    public void setBuOrderTypeApprovalDAO(BuOrderTypeApprovalDAO buOrderTypeApprovalDAO) {
	this.buOrderTypeApprovalDAO = buOrderTypeApprovalDAO;
    }

    public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
	this.buOrderTypeService = buOrderTypeService;
    }
    
    public void setBuPaymentTermDAO(BuPaymentTermDAO buPaymentTermDAO) {
	this.buPaymentTermDAO = buPaymentTermDAO;
    }
    
    public void setBuShopDAO(BuShopDAO buShopDAO) {
	this.buShopDAO = buShopDAO;
    }
    
    public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
	this.buShopMachineDAO = buShopMachineDAO;
    }

    public void setImDeliveryHeadDAO(ImDeliveryHeadDAO imDeliveryHeadDAO) {
	this.imDeliveryHeadDAO = imDeliveryHeadDAO;
    }
    
    public void setImDeliveryLineDAO(ImDeliveryLineDAO imDeliveryLineDAO) {
	this.imDeliveryLineDAO = imDeliveryLineDAO;
    }
    
    public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
	this.imWarehouseDAO = imWarehouseDAO;
    }

    public void setImItemDAO(ImItemDAO imItemDAO) {
	this.imItemDAO = imItemDAO;
    }
    
    public void setImItemPriceOnHandViewDAO(
	    ImItemPriceOnHandViewDAO imItemPriceOnHandViewDAO) {
	this.imItemPriceOnHandViewDAO = imItemPriceOnHandViewDAO;
    }
    
    public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
	this.imOnHandDAO = imOnHandDAO;
    }

    public void setImPromotionViewDAO(ImPromotionViewDAO imPromotionViewDAO) {
	this.imPromotionViewDAO = imPromotionViewDAO;
    }
    
    public void setImTransationDAO(ImTransationDAO imTransationDAO) {
	this.imTransationDAO = imTransationDAO;
    }
    
    public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
	this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
    }
    
    public void setSoSalesOrderItemDAO(SoSalesOrderItemDAO soSalesOrderItemDAO) {
	this.soSalesOrderItemDAO = soSalesOrderItemDAO;
    }
    
    public void setSoSalesOrderPaymentDAO(SoSalesOrderPaymentDAO soSalesOrderPaymentDAO) {
	this.soSalesOrderPaymentDAO = soSalesOrderPaymentDAO;
    }
    
    public void setSoPostingTallyDAO(SoPostingTallyDAO soPostingTallyDAO) {
	this.soPostingTallyDAO = soPostingTallyDAO;
    }
    
    public void setAppGetSaleItemInfoService(AppGetSaleItemInfoService appGetSaleItemInfoService) {
	this.appGetSaleItemInfoService = appGetSaleItemInfoService;
    }
    
    public void setSoSalesOrderPaymentService(SoSalesOrderPaymentService soSalesOrderPaymentService) {
	this.soSalesOrderPaymentService = soSalesOrderPaymentService;
    }
    
    public void setImDeliveryService(ImDeliveryService imDeliveryService) {
	this.imDeliveryService = imDeliveryService;
    }
    
    public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
	this.buCommonPhraseService = buCommonPhraseService;
    }
    
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }

    /**
     * 更新至銷貨單主檔或明細檔
     * 
     * @param updateObj
     * @param loginUser
     */
    private void modifySoSalesOrder(SoSalesOrderHead updateObj, String loginUser) {
	
	updateObj.setLastUpdatedBy(loginUser);
	updateObj.setLastUpdateDate(new Date());
	soSalesOrderHeadDAO.update(updateObj);
    }

    /**
     * 依據primary key為查詢條件，取得銷貨單主檔
     * 
     * @param headId
     * @return SoSalesOrderHead
     * @throws Exception
     */
    public SoSalesOrderHead findSoSalesOrderHeadById(Long headId)
	    throws Exception {

	try {
	    SoSalesOrderHead salesOrder = (SoSalesOrderHead) soSalesOrderHeadDAO
		    .findByPrimaryKey(SoSalesOrderHead.class, headId);
	    return salesOrder;
	} catch (Exception ex) {
	    log.error("依據主鍵：" + headId + "查詢銷售單主檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據主鍵：" + headId + "查詢銷售單主檔時發生錯誤，原因："
		    + ex.getMessage());
	}
    }

    /**
     * 依據銷貨單查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List<SoSalesOrderHead> findSalesOrderList(HashMap conditionMap)
	    throws Exception {

	try {
	    String startOrderNo = (String) conditionMap.get("startOrderNo");
	    String endOrderNo = (String) conditionMap.get("endOrderNo");
	    String customerCode = (String) conditionMap.get("customerCode");
	    String superintendentCode = (String) conditionMap
		    .get("superintendentCode");
	    String defaultWarehouseCode = (String) conditionMap
		    .get("defaultWarehouseCode");
	    conditionMap.put("startOrderNo", startOrderNo.trim().toUpperCase());
	    conditionMap.put("endOrderNo", endOrderNo.trim().toUpperCase());
	    conditionMap.put("customerCode", customerCode.trim().toUpperCase());
	    conditionMap.put("superintendentCode", superintendentCode.trim()
		    .toUpperCase());
	    conditionMap.put("defaultWarehouseCode", defaultWarehouseCode.trim()
		    .toUpperCase());

	    return soSalesOrderHeadDAO.findSalesOrderList(conditionMap);
	} catch (Exception ex) {
	    log.error("查詢銷售單時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢銷售單時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 將SalesOrderItem轉換成DeliveryLine(派發批號至DeliveryLine)
     * 
     * @param salesOrderItems
     * @param onHandsMap
     * @param stockControl
     * @param loginUser
     */
    private void salesOrderToDelivery(SoSalesOrderHead salesOrderHead,
	    List<SoSalesOrderItem> salesOrderItems, HashMap onHandsMap,
	    String stockControl, String loginUser) {

	Long salesOrderHeadId = salesOrderHead.getHeadId();
	StringBuffer key = new StringBuffer();
	for (int k = 0; k < salesOrderItems.size(); k++) {
	    Long indexSeq = k + 1L;
	    SoSalesOrderItem salesOrderItem = (SoSalesOrderItem)salesOrderItems.get(k);
	    Double orderQuantity = salesOrderItem.getQuantity();
	    key.delete(0, key.length());
	    key.append(salesOrderItem.getItemCode());
	    key.append(salesOrderItem.getWarehouseCode());
	    TreeMap lotNoMap = (TreeMap) onHandsMap.get(key.toString());
	    Set set = lotNoMap.entrySet();
	    Map.Entry[] lotNoEntry = (Map.Entry[]) set
		    .toArray(new Map.Entry[set.size()]);

	    if ("FIFO".equals(stockControl)) {
		for (Map.Entry entry : lotNoEntry) {
		    String lotNo = (String) entry.getKey();
		    if(!POSTYPECODE.equals(salesOrderHead.getOrderTypeCode())){
			if(!"Y".equals(salesOrderItem.getIsServiceItem())){
		            Double availableQuantity = (Double) entry.getValue();
		            if (availableQuantity > 0D && orderQuantity > 0D) {
			        if (orderQuantity > availableQuantity) {
			            imDeliveryLineDAO.save(copySalesOrderItemToDeliveryLine(
					        salesOrderHeadId, salesOrderItem,
					        lotNo, availableQuantity, loginUser, indexSeq));
			            entry.setValue(0D);
			            orderQuantity -= availableQuantity;
			        } else if (orderQuantity <= availableQuantity) {
			            imDeliveryLineDAO.save(copySalesOrderItemToDeliveryLine(
					        salesOrderHeadId, salesOrderItem,
					        lotNo, orderQuantity, loginUser, indexSeq));
			            entry.setValue(availableQuantity - orderQuantity);
			            break;
			        }
		            }
			}else{
			    imDeliveryLineDAO.save(copySalesOrderItemToDeliveryLine(
				        salesOrderHeadId, salesOrderItem,
				        lotNo, orderQuantity, loginUser, indexSeq));
			    break;		    
			}
		    }else{
			imDeliveryLineDAO.save(copySalesOrderItemToDeliveryLine(
				    salesOrderHeadId, salesOrderItem,
				    lotNo, orderQuantity, loginUser, indexSeq));
		        break;			
		    }
		}
	    } else {
		for (int i = lotNoEntry.length - 1; i >= 0; i--) {		    
		    Map.Entry entry = (Map.Entry)lotNoEntry[i];
		    String lotNo = (String) entry.getKey();
		    if(!POSTYPECODE.equals(salesOrderHead.getOrderTypeCode())){
			if(!"Y".equals(salesOrderItem.getIsServiceItem())){
		            Double availableQuantity = (Double) entry.getValue();
		            if (availableQuantity > 0D && orderQuantity > 0D) {
			        if (orderQuantity > availableQuantity) {
			            imDeliveryLineDAO.save(copySalesOrderItemToDeliveryLine(
					    salesOrderHeadId, salesOrderItem,
					    lotNo, availableQuantity, loginUser, indexSeq));
			            entry.setValue(0D);
			            orderQuantity -= availableQuantity;
			        } else if (orderQuantity <= availableQuantity) {
			            imDeliveryLineDAO.save(copySalesOrderItemToDeliveryLine(
					    salesOrderHeadId, salesOrderItem,
					    lotNo, orderQuantity, loginUser, indexSeq));
			            entry.setValue(availableQuantity- orderQuantity);
			            break;
			        }
		            }
			}else{
			    imDeliveryLineDAO.save(copySalesOrderItemToDeliveryLine(
				        salesOrderHeadId, salesOrderItem,
				        lotNo, orderQuantity, loginUser, indexSeq));
			    break;		    
			}
		    }else{
			imDeliveryLineDAO.save(copySalesOrderItemToDeliveryLine(
				    salesOrderHeadId, salesOrderItem,
				    lotNo, orderQuantity, loginUser, indexSeq));
		        break;			
		    }
		}
	    }
	}
    }

    /**
     * 將SalesOrderItem內容複製到ImDeliveryLine
     * 
     * @param salesOrderItem
     * @param lotNo
     * @param loginUser
     * @return ImDeliveryLine
     */
    private ImDeliveryLine copySalesOrderItemToDeliveryLine(
	    Long salesOrderHeadId, SoSalesOrderItem salesOrderItem,
	    String lotNo, Double quantity, String loginUser, Long indexSeq) {

	ImDeliveryLine deliveryLine = new ImDeliveryLine();
	deliveryLine.setSalesOrderId(salesOrderHeadId);
	deliveryLine.setItemCode(salesOrderItem.getItemCode());
	deliveryLine.setWarehouseCode(salesOrderItem.getWarehouseCode());
	deliveryLine.setLotNo(lotNo);
	deliveryLine.setSalesQuantity(quantity);
	deliveryLine.setShipQuantity(quantity);
	deliveryLine.setOriginalWarehouseCode(salesOrderItem.getWarehouseCode());
	deliveryLine.setOriginalLotNo(lotNo);
	deliveryLine.setOriginalSalesQuantity(quantity);
	deliveryLine.setOriginalUnitPrice(salesOrderItem.getOriginalUnitPrice());
	deliveryLine.setOriginalSalesAmount(salesOrderItem.getOriginalUnitPrice() * quantity);
	deliveryLine.setPromotionCode(salesOrderItem.getPromotionCode());
	deliveryLine.setDiscountType(salesOrderItem.getDiscountType());
	deliveryLine.setDiscount(salesOrderItem.getDiscount());
	deliveryLine.setVipPromotionCode(salesOrderItem.getVipPromotionCode());
	deliveryLine.setVipDiscountType(salesOrderItem.getVipDiscountType());
	deliveryLine.setVipDiscount(salesOrderItem.getVipDiscount());
	deliveryLine.setOriginalOnHandQty(salesOrderItem.getCurrentOnHandQty());//原庫存量	
	deliveryLine.setActualUnitPrice(salesOrderItem.getActualUnitPrice());
//	if(salesOrderItem.getPosActualSalesAmount() != 0){
//	    deliveryLine.setActualSalesAmount(salesOrderItem.getActualSalesAmount());
//	}else{
//	    deliveryLine.setActualSalesAmount(salesOrderItem.getActualUnitPrice() * quantity);
//	}
	//updated by anber
	if(salesOrderItem.getPosActualSalesAmount() == null || salesOrderItem.getPosActualSalesAmount() == 0){
	    deliveryLine.setActualSalesAmount(CommonUtils.round(salesOrderItem.getActualUnitPrice() * quantity, 0));
	}else{
	    deliveryLine.setActualSalesAmount(CommonUtils.round(salesOrderItem.getActualSalesAmount(), 0));
	}
	deliveryLine.setDiscountRate(salesOrderItem.getDiscountRate());
	deliveryLine.setScheduleShipDate(salesOrderItem.getScheduleShipDate());
	deliveryLine.setIsTax(salesOrderItem.getIsTax());
	deliveryLine.setTaxType(salesOrderItem.getTaxType());
	deliveryLine.setTaxRate(salesOrderItem.getTaxRate());	
	deliveryLine.setTaxAmount(calculateTaxAmount(deliveryLine.getTaxType(), deliveryLine.getTaxRate(), deliveryLine.getActualSalesAmount()));	
	deliveryLine.setDepositCode(salesOrderItem.getDepositCode());
	deliveryLine.setIsUseDeposit(salesOrderItem.getIsUseDeposit());
	deliveryLine.setWatchSerialNo(salesOrderItem.getWatchSerialNo());
	deliveryLine.setIsComposeItem(salesOrderItem.getIsComposeItem());//組合性商品
	deliveryLine.setIsServiceItem(salesOrderItem.getIsServiceItem());//服務性商品	
	deliveryLine.setReserve1(salesOrderItem.getReserve1());
	deliveryLine.setReserve2(salesOrderItem.getReserve2());
	deliveryLine.setReserve3(salesOrderItem.getReserve3());
	deliveryLine.setReserve4(salesOrderItem.getReserve4());
	deliveryLine.setReserve5(salesOrderItem.getReserve5());
	deliveryLine.setStatus(OrderStatus.SAVE);
	deliveryLine.setIndexNo(indexSeq); //依照銷貨明細排序
	UserUtils.setOpUserAndDate(deliveryLine, loginUser);

	return deliveryLine;
    }

    /**
     * 將相同itemCode、warehouseCode的數量合計
     * 
     * @param organization
     * @param originalOrderItems
     * @param isServiceItemMap
     * @param isComposeItemMap
     * @return Set
     */
    private Set aggregateOrderItemQuantity(
	    List<SoSalesOrderItem> originalOrderItems, HashMap isServiceItemMap, HashMap isComposeItemMap) {

	StringBuffer key = new StringBuffer();
	HashMap map = new HashMap();
	for (SoSalesOrderItem originalOrderItem : originalOrderItems) {
	    Double quantity = originalOrderItem.getQuantity();
	    String itemCode = originalOrderItem.getItemCode();
	    //數量不可為null
	    if(quantity == null){
	        quantity = 0D;
	        originalOrderItem.setQuantity(quantity);
	    }
	    key.delete(0, key.length());
	    key.append(itemCode + "#");
	    key.append(originalOrderItem.getWarehouseCode());

	    if (map.get(key.toString()) == null) {
		map.put(key.toString(), quantity);
	    } else {
		map.put(key.toString(), quantity
			+ ((Double) map.get(key.toString())));
	    }
	    //將itemCode是否為服務性商品、組合性商品放入map中
	    if(isServiceItemMap.get(itemCode) == null){
	        isServiceItemMap.put(itemCode, originalOrderItem.getIsServiceItem());
	    }
	    if(isComposeItemMap.get(itemCode) == null){
		isComposeItemMap.put(itemCode, originalOrderItem.getIsComposeItem());
	    }
	}
	return map.entrySet();
    }

    /**
     * 從ImOnHand資料中取得必要資訊，放置到HashMap
     * 
     * @param lockedOnHands
     * @param onHandsMap
     */
    private void getRequiredPropertyConvertToMap(List<ImOnHand> lockedOnHands,
	    HashMap onHandsMap) {

	StringBuffer key = new StringBuffer();
	StringBuffer subKey = new StringBuffer();
	TreeMap lotNoMap = new TreeMap();
	for (ImOnHand onHand : lockedOnHands) {
	    Double availableQuantity = onHand.getStockOnHandQty()
		    - onHand.getOutUncommitQty() + onHand.getInUncommitQty() 
		    + onHand.getMoveUncommitQty() + onHand.getOtherUncommitQty();
	    key.delete(0, key.length());
	    subKey.delete(0, subKey.length());
	    key.append(onHand.getId().getItemCode());
	    key.append(onHand.getId().getWarehouseCode());
	    subKey.append(onHand.getId().getLotNo());

	    lotNoMap.put(subKey.toString(), availableQuantity);
	}
	onHandsMap.put(key.toString(), lotNoMap);
    }

    /**
     * 回復至原始可用庫存，並將出貨單明細刪除
     * 
     * @param SalesOrderHeadId
     * @param organizationCode
     * @param loginUser
     * @throws FormException
     * @throws NoSuchDataException
     */
    private void revertToOriginallyAvailableQuantity(Long SalesOrderHeadId,
	    String organizationCode, String loginUser) throws FormException,
	    NoSuchDataException {

	List imDeliveryLines = imDeliveryLineDAO.findByProperty(
		"ImDeliveryLine", "salesOrderId", SalesOrderHeadId);
	if (imDeliveryLines != null && imDeliveryLines.size() > 0) {
	    for (int i = 0; i < imDeliveryLines.size(); i++) {
		ImDeliveryLine deliveryLine = (ImDeliveryLine) imDeliveryLines.get(i);
		//非服務性商品補回庫存
		if(!"Y".equals(deliveryLine.getIsServiceItem())){	  
		    imOnHandDAO.updateOutUncommitQuantity(organizationCode, deliveryLine.getItemCode()
			    , deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), 
			    deliveryLine.getSalesQuantity(), loginUser);
		}

		imDeliveryLineDAO.delete(deliveryLine);
	    }
	}else {
	    throw new NoSuchDataException("依據銷貨單主檔主鍵："
		    + SalesOrderHeadId + "查無相關出貨單明細檔資料！");
	}
    }

    /**
     * 更新銷貨單主檔及明細檔的Status
     * 
     * @param SalesOrderHeadId
     * @param status
     * @return String
     * @throws Exception
     */
    public String updateSoSalesOrderStatus(Long salesOrderHeadId, String status, String loginUser)
	    throws Exception {

	try {
	    SoSalesOrderHead salesOrderHead = (SoSalesOrderHead) soSalesOrderHeadDAO
		    .findByPrimaryKey(SoSalesOrderHead.class, salesOrderHeadId);
	    if (salesOrderHead != null) {
		changeSoSalesOrderStatus(salesOrderHead, status);
		if(!StringUtils.hasText(loginUser)){
		    loginUser = salesOrderHead.getLastUpdatedBy();
		}
		modifySoSalesOrder(salesOrderHead, loginUser);
		return "Success";
	    } else {
		throw new NoSuchDataException("銷貨單主檔查無主鍵：" + salesOrderHeadId
			+ "的資料！");
	    }
	} catch (Exception ex) {
	    log.error("更新銷貨單狀態時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新銷貨單狀態時發生錯誤，原因：" + ex.getMessage());

	}
    }
    
    /**
     * 檢核銷貨主檔資料(SalesOrderHead)
     * 
     * @param orderHead
     * @param conditionMap
     * @param programId
     * @param identification
     * @param errorMsgs
     */
    private void checkSalesOrderHead(SoSalesOrderHead orderHead, HashMap conditionMap, 
	    String programId, String identification, List errorMsgs) {
	
	String message = null;
	String tabName = "主檔資料頁籤";
	String posTabName = "POS資料頁籤";
	String dateType = "銷貨日期";
	try{
	    String brandCode = orderHead.getBrandCode();
	    String orderTypeCode = orderHead.getOrderTypeCode();
	    String customerCode = orderHead.getCustomerCode();
	    String superintendentCode = orderHead.getSuperintendentCode();
	    //String countryCode = orderHead.getCountryCode();
	    //String paymentTermCode = orderHead.getPaymentTermCode();
	    //String currencyCode = orderHead.getCurrencyCode();
	    String defaultWarehouseCode = orderHead.getDefaultWarehouseCode();
	    String promotionCode = orderHead.getPromotionCode();
	    String priceType = (String)conditionMap.get("priceType");	
	    String customerCode_var = (String)conditionMap.get("customerCode_var");
	    String searchCustomerType = (String)conditionMap.get("searchCustomerType");
	    String salesDate = DateUtils.format(orderHead.getSalesOrderDate(), DateUtils.C_DATA_PATTON_YYYYMMDD);	  
	    String customerType = null;
	    String vipType = null;
	    String vipPromotionCode = null;
	
	    ValidateUtil.isAfterClose(brandCode, orderTypeCode, dateType, orderHead.getSalesOrderDate(),orderHead.getSchedule());
	    if(StringUtils.hasText(customerCode_var)){
	        customerCode_var = customerCode_var.trim().toUpperCase();    
	        BuCustomerWithAddressView customerWithAddressView = buCustomerWithAddressViewDAO.findCustomerByType(brandCode, customerCode_var, searchCustomerType, null);
	        if(customerWithAddressView == null){
	            orderHead.setCustomerCode(customerCode_var);
	            message = "查無" + tabName + "的客戶代號！";
                    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
                }/*else if(customerCode == null || !customerWithAddressView.getCustomerCode().equals(customerCode.trim().toUpperCase())){
        	throw new ValidationErrorException("請勿輸入主檔的客戶代號後直接存檔！");
	        }*/
	        else{
		    customerCode = customerWithAddressView.getCustomerCode();
		    orderHead.setCustomerCode(customerCode);
		    customerType = customerWithAddressView.getCustomerTypeCode();
		    vipType = customerWithAddressView.getVipTypeCode();
		    vipPromotionCode = customerWithAddressView.getPromotionCode();
		    //將查詢到的客戶資料，customerType、vipType放置conditionMap
		    conditionMap.put("customerType", customerType);
		    conditionMap.put("vipType", vipType);
		    if(POSTYPECODE.equals(orderTypeCode)){
		        conditionMap.put("vipPromotionCode", null);
		    }else{
		        conditionMap.put("vipPromotionCode", vipPromotionCode);
		    }
	        }
	    }else{
	        orderHead.setCustomerCode(null);
	    }
	
	    if(!StringUtils.hasText(orderHead.getShopCode())){
		message = "請選擇" + tabName + "的專櫃代號！";
                siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }/*else if(!StringUtils.hasText(superintendentCode)){
	    throw new ValidationErrorException("請輸入" + tabName + "的訂單負責人！");
	    }*/
	    if(StringUtils.hasText(superintendentCode)){
	        superintendentCode = superintendentCode.trim().toUpperCase();
	        orderHead.setSuperintendentCode(superintendentCode);
	        BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, superintendentCode);
	        if(employeeWithAddressView == null){
	            message = "查無" + tabName + "的訂單負責人！";
	            siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
	            errorMsgs.add(message);
		    log.error(message);
	        }
	    }else{
	        orderHead.setSuperintendentCode(null);
	    }	
	    /*
	    if(!StringUtils.hasText(orderHead.getContactPerson())){
	        throw new ValidationErrorException("請輸入" + tabName + "的客戶聯絡窗口！");
	    }	
	    if(!StringUtils.hasText(countryCode)){
	        throw new ValidationErrorException("請輸入" + tabName + "的國別！");
	    }else{
	        countryCode = countryCode.trim().toUpperCase();
	        orderHead.setCountryCode(countryCode);
	        if(buCountryDAO.findById(countryCode) == null)
		    throw new NoSuchObjectException("查無" + tabName + "的國別！");
	    }	
	    if(!StringUtils.hasText(paymentTermCode)){
	        throw new ValidationErrorException("請輸入" + tabName + "的付款條件！");
	    }else{
	        paymentTermCode = paymentTermCode.trim().toUpperCase();
	        orderHead.setPaymentTermCode(paymentTermCode);
	        BuPaymentTermId paymentTermId = new BuPaymentTermId();
	        paymentTermId.setOrganizationCode(organizationCode);
	        paymentTermId.setPaymentTermCode(paymentTermCode);	    
	        if(buPaymentTermDAO.findById(paymentTermId) == null)
		    throw new NoSuchObjectException("查無" + tabName + "的付款條件！");
	    }	
	    if(orderHead.getScheduleCollectionDate() == null){
	        throw new ValidationErrorException("請輸入" + tabName + "的付款日期！");
	    }else if(!StringUtils.hasText(currencyCode)){
	        throw new ValidationErrorException("請輸入" + tabName + "的幣別！");
	    }else{
	        currencyCode = currencyCode.trim().toUpperCase();
	        orderHead.setCurrencyCode(currencyCode);
	        if(buCurrencyDAO.findById(currencyCode) == null)
		    throw new NoSuchObjectException("查無" + tabName + "的幣別！");
	    }	
	    //發票地址檢核
	    if(StringUtils.hasText(orderHead.getInvoiceCity()) || StringUtils.hasText(orderHead.getInvoiceArea()) || StringUtils.hasText(orderHead.getInvoiceZipCode()) || StringUtils.hasText(orderHead.getInvoiceAddress())){
	        if(!StringUtils.hasText(orderHead.getInvoiceCity()) || !StringUtils.hasText(orderHead.getInvoiceArea()) || !StringUtils.hasText(orderHead.getInvoiceZipCode()) || !StringUtils.hasText(orderHead.getInvoiceAddress())){
	            throw new ValidationErrorException("請完整輸入" + tabName + "的發票地址！");
	        }else if(!ValidateUtil.isNumber(orderHead.getInvoiceZipCode())){
		    throw new ValidationErrorException(tabName + "的發票地址郵遞區號必須為0~9數字！");
	        }
	    }
	    //送貨地址檢核
	    if(StringUtils.hasText(orderHead.getShipCity()) || StringUtils.hasText(orderHead.getShipArea()) || StringUtils.hasText(orderHead.getShipZipCode()) || StringUtils.hasText(orderHead.getShipAddress())){
	        if(!StringUtils.hasText(orderHead.getShipCity()) || !StringUtils.hasText(orderHead.getShipArea()) || !StringUtils.hasText(orderHead.getShipZipCode()) || !StringUtils.hasText(orderHead.getShipAddress())){
	            throw new ValidationErrorException("請完整輸入" + tabName + "的送貨地址！");
	        }else if(!ValidateUtil.isNumber(orderHead.getShipZipCode())){
		    throw new ValidationErrorException(tabName + "的送貨地址郵遞區號必須為0~9數字！");
	        }
	    }
	    if(!StringUtils.hasText(orderHead.getInvoiceTypeCode())){
	        throw new ValidationErrorException("請輸入" + tabName + "的發票類型！");
	    }*/ 
	    if(orderHead.getScheduleShipDate() == null){
		message = "請輸入" + tabName + "的預計出貨日期！";
	        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
	        errorMsgs.add(message);
		log.error(message);
	    }
	
	    if (!StringUtils.hasText(orderHead.getTaxType())) {
		message = "請選擇" + tabName + "的稅別！";
	        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
	        errorMsgs.add(message);
		log.error(message);
	    }else if(("1".equals(orderHead.getTaxType()) || "2".equals(orderHead.getTaxType())) && (orderHead.getTaxRate() == null || orderHead.getTaxRate() != 0D)){
		message = tabName + "的稅別為免稅或零稅時，其稅率應為0%！";
	        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
	        errorMsgs.add(message);
		log.error(message);
	    }else if("3".equals(orderHead.getTaxType()) && (orderHead.getTaxRate() == null || orderHead.getTaxRate() != 5D)){
		message = tabName + "的稅別為應稅時，其稅率應為5%！";
	        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
	        errorMsgs.add(message);
		log.error(message);
	    }
	    
	    if(!StringUtils.hasText(defaultWarehouseCode)){
		message = "請選擇" + tabName + "的庫別！";
	        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
	        errorMsgs.add(message);
		log.error(message);
	    }else{
	        defaultWarehouseCode = defaultWarehouseCode.trim().toUpperCase();
	        orderHead.setDefaultWarehouseCode(defaultWarehouseCode);
	        conditionMap.put("warehouseCode", defaultWarehouseCode);
	        if(imItemPriceOnHandViewDAO.getWarehouseByCondition(conditionMap) == null){
	            message = "查無" + tabName + "的庫別！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
	        }
	    }	
	    //活動代號檢核
	    if (StringUtils.hasText(promotionCode)) {
	        promotionCode = promotionCode.trim().toUpperCase();
	        orderHead.setPromotionCode(promotionCode);
	        HashMap parameterMap = new HashMap();
	        parameterMap.put("brandCode", brandCode);
	        parameterMap.put("promotionCode", promotionCode);
	        parameterMap.put("priceType", priceType);
	        parameterMap.put("shopCode", orderHead.getShopCode());
	        parameterMap.put("customerType", customerType);
	        parameterMap.put("vipType", vipType);
	        parameterMap.put("salesDate", salesDate);	    
	        Object[] promotionInfo = imPromotionViewDAO.findPromotionCodeByCondition(parameterMap);
	        if(promotionInfo == null || promotionInfo[3] == null || promotionInfo[4] == null){
	            message = "查無" + tabName + "的活動代號！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
	        }
	    }
	
	    if(orderHead.getDiscountRate() != null && orderHead.getDiscountRate() < 0D){
                message = tabName + "的折扣比率不可小於0%！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	
	    //單別非SOP，將POS相關欄位清除
	    if(!POSTYPECODE.equals(orderTypeCode)){
	        orderHead.setPosMachineCode(null);
	        orderHead.setCasherCode(null);
	        orderHead.setDepartureDate(null);
	        orderHead.setFlightNo(null);
	        orderHead.setPassportNo(null);
	        orderHead.setLadingNo(null);
	        orderHead.setTransactionSeqNo(null);
	        orderHead.setSalesInvoicePage(null);
	    }else{
	        /*
	        if(!StringUtils.hasText(orderHead.getCustomerPoNo())){
	            throw new ValidationErrorException("請輸入" + tabName + "的原訂單編號！");
	        }	    
	        if(!StringUtils.hasText(orderHead.getCasherCode())){
	            throw new ValidationErrorException("請輸入" + posTabName + "的收銀員代號！");
	        }*/
	        if(StringUtils.hasText(orderHead.getCasherCode())){
		    orderHead.setCasherCode(orderHead.getCasherCode().trim().toUpperCase());
		    BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, orderHead.getCasherCode());
		    if(employeeWithAddressView == null){
			message = "查無" + posTabName + "的收銀員代號！";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		    }
	        }else{
		    orderHead.setCasherCode(null);
	        }
	        if(StringUtils.hasText(orderHead.getFlightNo()) && !ValidateUtil.isUpperCaseOrNumber(orderHead.getFlightNo())){
	            message = posTabName + "的班機代碼必須為A~Z、0~9！";
	            siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
	        }	        
	        if(StringUtils.hasText(orderHead.getPassportNo()) && !ValidateUtil.isUpperCaseOrNumber(orderHead.getPassportNo())){
	            message = posTabName + "的護照號碼必須為A~Z、0~9！";
	            siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
	        }        
	        if(StringUtils.hasText(orderHead.getLadingNo()) && !ValidateUtil.isUpperCaseOrNumber(orderHead.getLadingNo())){
	            message = posTabName + "的提貨單號必須為A~Z、0~9！";
	            siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
	        }
	        if(StringUtils.hasText(orderHead.getTransactionSeqNo()) && !ValidateUtil.isUpperCaseOrNumber(orderHead.getTransactionSeqNo())){
	            message = posTabName + "的交易序號必須為A~Z、0~9！";
	            siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
	        }
	    }
	}catch(Exception ex){
	    message = "檢核銷貨單" + tabName + "時發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }
    
    
    public Double calculateActualPrice(HashMap parameterMap){
	
	Double originalUnitPrice = (Double)parameterMap.get("originalUnitPrice");
	String promotionCode = (String)parameterMap.get("promotionCode");
	String discountType = (String)parameterMap.get("discountType");
	Double discount = (Double)parameterMap.get("discount");	
	String vipPromotionCode = (String)parameterMap.get("vipPromotionCode");
	String vipDiscountType = (String)parameterMap.get("vipDiscountType");
	Double vipDiscount = (Double)parameterMap.get("vipDiscount");
	Double discountRate = (Double)parameterMap.get("discountRate");
	Double deductionAmount = (Double)parameterMap.get("deductionAmount");

	Double actualUnitPrice = originalUnitPrice;
	if(originalUnitPrice != null){
	    if(StringUtils.hasText(promotionCode) && discount != null){		
                actualUnitPrice = calculateDiscountedPrice(actualUnitPrice, discountType, discount);   		
	    }	    
	    if(StringUtils.hasText(vipPromotionCode) && vipDiscount != null){
		actualUnitPrice = calculateDiscountedPrice(actualUnitPrice, vipDiscountType, vipDiscount);
	    }
	    //折扣率為正向扣除
	    if(discountRate != null){
                actualUnitPrice = actualUnitPrice * discountRate / 100;		
	    }
	    //扣除折讓金額
	    if(deductionAmount != null && deductionAmount != 0D){
                actualUnitPrice -= deductionAmount;
	    }
	    
	    return CommonUtils.round(actualUnitPrice, 0);	    
	}else{
	    return null;
	}
    }
    
    private Double calculateDiscountedPrice(Double unitPrice, String discountType, Double discount){
	
	if("1".equals(discountType))
	    unitPrice = unitPrice - discount;
        else if(discountType == null || "2".equals(discountType))
            unitPrice = unitPrice * (100 - discount) / 100; 
	
	return unitPrice;
    }
    
    public Double calculateTaxAmount(String taxType, Double taxRate, Double actualSalesAmount){
	
	Double taxAmount = 0D;
	if("3".equals(taxType) && taxRate != null && taxRate != 0D && actualSalesAmount != null && actualSalesAmount != 0D){
	    Double salesAmount = actualSalesAmount /(1 + taxRate/100);
	    taxAmount = actualSalesAmount - salesAmount;
	}
	
	return CommonUtils.round(taxAmount, 0);
    }
       
    private Object[] bookAvailableQuantity(SoSalesOrderHead soSalesOrderHead, String organizationCode, String loginUser) throws NoSuchDataException, FormException{
	
	BuOrderTypeId buOrderTypeId = new BuOrderTypeId();
	buOrderTypeId.setOrderTypeCode(soSalesOrderHead.getOrderTypeCode());
	buOrderTypeId.setBrandCode(soSalesOrderHead.getBrandCode());

	BuOrderType buOrderType = buOrderTypeDAO.findById(buOrderTypeId);
	if (buOrderType == null) {
	    throw new NoSuchDataException("依據單據代號："
	        + soSalesOrderHead.getOrderTypeCode() + ",品牌代號："
		+ soSalesOrderHead.getBrandCode() + "查無相關單別資料！");
	}
	String stockControl = buOrderType.getStockControl(); // 庫存控制方法
	HashMap onHandsMap = new HashMap(); // 出貨單派發批號之來源
	HashMap isServiceItemMap = new HashMap(); // 是否為服務性商品集合
	HashMap isComposeItemMap = new HashMap(); // 是否為組合性商品集合

	List mailContents = new ArrayList(0);
	Set entrySet = aggregateOrderItemQuantity(soSalesOrderHead.getSoSalesOrderItems(), isServiceItemMap, isComposeItemMap);
	Iterator it = entrySet.iterator();
	while (it.hasNext()) {
	    Map.Entry entry = (Map.Entry) it.next();
	    String[] keyArray = StringUtils.delimitedListToStringArray((String) entry.getKey(), "#");
	    List<ImOnHand> lockedOnHands = null;
	    try {
	        lockedOnHands = imOnHandDAO.getLockedOnHand(
		organizationCode, keyArray[0], keyArray[1], null);
	    } catch (CannotAcquireLockException cale) {
	        throw new FormException("品號：" + keyArray[0] + ",庫別：" + keyArray[1] + "已鎖定，請稍後再試！");
	    }
	    //非服務性商品且數量大於零時檢核庫存量是否足夠
	    if(!"Y".equals((String)isServiceItemMap.get(keyArray[0])) && (Double) entry.getValue() > 0D){
	        Double availableQuantity = imOnHandDAO.getCurrentStockOnHandQty(organizationCode, keyArray[0], keyArray[1]);

	        if (availableQuantity == null) {
	            //非SOP單需檢核是否有庫存
	            if(!POSTYPECODE.equals(soSalesOrderHead.getOrderTypeCode())){
	                throw new NoSuchObjectException("查無品號：" + keyArray[0]
		            + ",庫別：" + keyArray[1] + "的庫存量！");
	            }
	        } else if ((Double) entry.getValue() > availableQuantity) {
	            if(!POSTYPECODE.equals(soSalesOrderHead.getOrderTypeCode())){
		        throw new InsufficientQuantityException("品號：" + keyArray[0]
		            + ",庫別：" + keyArray[1] + "可用庫存量不足！");
		    }else{
		        //POS庫存不足不丟出例外，改以mail通知
		        mailContents.add(keyArray[0] + "#" + keyArray[1] + "#" + (Double)entry.getValue());
		    }
	        }
	    }
	    // ==========非服務性商品預訂可用庫存==========
	    if (lockedOnHands != null && lockedOnHands.size() > 0) {
	        getRequiredPropertyConvertToMap(lockedOnHands, onHandsMap); // 從ImOnHand資料中取得必要資訊，放置到HashMap
	        if(!"Y".equals((String)isServiceItemMap.get(keyArray[0]))){
	            if(!POSTYPECODE.equals(soSalesOrderHead.getOrderTypeCode())){
		        imOnHandDAO.bookAvailableQuantity(lockedOnHands, (Double) entry.getValue(), stockControl, loginUser);
	            }else{
	                imOnHandDAO.bookQuantity(lockedOnHands, (Double) entry.getValue(), stockControl, loginUser);
	            }
	        }
	    } else {
		if(!POSTYPECODE.equals(soSalesOrderHead.getOrderTypeCode())){
		    throw new NoSuchObjectException("查無品號：" + keyArray[0] + ",庫別：" + keyArray[1] + "的庫存資料！");
		}else{
		    //==========================SOP單查無onHand時新增一筆====================================
		    BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("SystemConfig", "DefaultLotNo");		
	            String defaultLotNo = buCommonPhraseLine != null? buCommonPhraseLine.getName():"000000000000";
	            ImOnHandId id = new ImOnHandId();
	            ImOnHand newOnHand = new ImOnHand();
		    id.setOrganizationCode(organizationCode);
		    id.setItemCode(keyArray[0]);
		    id.setWarehouseCode(keyArray[1]);
		    id.setLotNo(defaultLotNo);
		    newOnHand.setId(id);
		    newOnHand.setBrandCode(soSalesOrderHead.getBrandCode());  //增加庫存的品牌
		    newOnHand.setStockOnHandQty(0D);
		    if(!"Y".equals((String)isServiceItemMap.get(keyArray[0]))){
		        newOnHand.setOutUncommitQty((Double) entry.getValue());
		    }else{
			newOnHand.setOutUncommitQty(0D);
		    }
		    newOnHand.setInUncommitQty(0D);
		    newOnHand.setMoveUncommitQty(0D);
		    newOnHand.setOtherUncommitQty(0D);
		    newOnHand.setCreatedBy(loginUser);
		    newOnHand.setCreationDate(new Date());
		    newOnHand.setLastUpdatedBy(loginUser);
		    newOnHand.setLastUpdateDate(new Date());
		    imOnHandDAO.save(newOnHand);
		    lockedOnHands = new ArrayList();
		    lockedOnHands.add(newOnHand);
		    getRequiredPropertyConvertToMap(lockedOnHands, onHandsMap);
		}
	    }	    
	}
	return new Object[]{stockControl, onHandsMap, mailContents};
    }
    /**
     * POS上傳作業，產生SO Entity Bean、Delivery Line、扣庫存、過帳記錄檔
     * 
     * @param entityBeans
     * @param opUser
     * @throws Exception
     */
    public void saveNoValidate(SoSalesOrderHead soSalesOrderHead, HashMap parameterMap) throws Exception{
	
	//================POS重傳時刪除相關銷售、出貨、過帳記錄資料=================
	String actualbrandCode = (String)parameterMap.get("brandCode");
	String actualShopCode = (String)parameterMap.get("posMachineCode");
	Date actualSalesDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String)parameterMap.get("salesOrderDate") );
	String customerPoNoStart = (String)parameterMap.get("customerPoNoStart");
	String customerPoNoEnd = (String)parameterMap.get("customerPoNoend");
	String opUser = (String)parameterMap.get("opUser");
	

	try{    
	    String organization = UserUtils.getOrganizationCodeByBrandCode(actualbrandCode);
	    if(!StringUtils.hasText(organization)){
	        throw new ValidationErrorException("品牌代號：" + actualbrandCode + "所屬的組織代號為空值！");
	    }	   
	    List<SoSalesOrderHead> salesOrderHeads = soSalesOrderHeadDAO.findCustomerPoNo(parameterMap);
	    if(salesOrderHeads != null){
                for(SoSalesOrderHead salesOrderHead : salesOrderHeads){
	            String identification = MessageStatus.getIdentificationMsg(
		    salesOrderHead.getBrandCode(), salesOrderHead.getOrderTypeCode(), 
		    salesOrderHead.getOrderNo());
	            if(!OrderStatus.SIGNING.equals(salesOrderHead.getStatus())){
	                throw new ValidationErrorException(identification + "的狀態為" + OrderStatus.getChineseWord(salesOrderHead.getStatus()) + "不可執行反確認！");
	            }
		    revertToOriginallyAvailableQuantity(salesOrderHead.getHeadId(), organization, opUser);
		    soSalesOrderHeadDAO.delete(salesOrderHead);
		}
	    }

	}catch(Exception ex){
	    log.error("刪除品牌代號：" + actualbrandCode + "、專櫃代號：" + actualShopCode + "、銷售日期：" + DateUtils.format(actualSalesDate) + "的銷售、出貨資料時發生錯誤，原因：" + ex.toString());
            throw new Exception("刪除品牌代號：" + actualbrandCode + "、專櫃代號：" + actualShopCode + "、銷售日期：" + DateUtils.format(actualSalesDate) + "的銷售、出貨資料失敗！");
	}	    
	//========================================================================
	String organizationCode = null;
	String shopCode = null;
	Date salesDate = null;
	String brandCode = null;

	    if(organizationCode == null){
		organizationCode = UserUtils.getOrganizationCodeByBrandCode(soSalesOrderHead.getBrandCode());
		if(organizationCode == null)
		    throw new NoSuchDataException("查無" + soSalesOrderHead.getBrandCode() + "的組織代號！");
	    }
	    if(shopCode == null){
		shopCode = soSalesOrderHead.getShopCode();
	    }
	    if(salesDate == null){
		salesDate = soSalesOrderHead.getSalesOrderDate();
	    }
	    if(brandCode == null){
		brandCode = soSalesOrderHead.getBrandCode();
	    }
		
            //取單別序號，insert至SO
	    String serialNo = "";
	    if("SOP".equals(soSalesOrderHead.getOrderTypeCode())){
		serialNo = buOrderTypeService.getOrderNo(soSalesOrderHead.getBrandCode(), soSalesOrderHead.getOrderTypeCode()
			, soSalesOrderHead.getShopCode(), soSalesOrderHead.getPosMachineCode());
	    }else{
		serialNo = buOrderTypeService.getOrderSerialNo(soSalesOrderHead.getBrandCode(),soSalesOrderHead.getOrderTypeCode());
	    }
	    
	    if (!serialNo.equals("unknow")) {
	        soSalesOrderHead.setOrderNo(serialNo);	
	    }else {
		throw new ObtainSerialNoFailedException("取得"
			+ soSalesOrderHead.getOrderTypeCode() + "單號失敗！");
	    }
	    soSalesOrderHeadDAO.save(soSalesOrderHead);
	    //扣庫存
	    Object[] objArray = bookAvailableQuantity(soSalesOrderHead, organizationCode, soSalesOrderHead.getCreatedBy());
	    //產生Delivery Line
	    salesOrderToDelivery(soSalesOrderHead, soSalesOrderHead.getSoSalesOrderItems(), (HashMap)objArray[1], (String)objArray[0], soSalesOrderHead.getCreatedBy());
	    if(POSTYPECODE.equals(soSalesOrderHead.getOrderTypeCode())){
	        mailToPOSAdministrator(soSalesOrderHead, (List)objArray[2]);
	    }

	//寫入過帳記錄檔 
	/*SoPostingTallyId postingTallyId = new SoPostingTallyId(shopCode, salesDate);
	if(soPostingTallyDAO.findByPrimaryKey(SoPostingTally.class, postingTallyId) == null){
	    SoPostingTally postingTally = new SoPostingTally();
	    postingTally.setId(postingTallyId);
	    postingTally.setBrandCode(brandCode);
	    postingTally.setIsPosting("N");
	    postingTally.setCreatedBy(opUser);
	    postingTally.setCreateDate(new Date());
	    postingTally.setLastUpdatedBy(opUser);
	    postingTally.setLastUpdateDate(new Date());
	    soPostingTallyDAO.save(postingTally);
	}*/
    }
    /**
     * POS上傳作業，產生SO Entity Bean、Delivery Line、扣庫存、過帳記錄檔
     * 
     * @param entityBeans
     * @param opUser
     * @throws Exception
     */
    public void saveNoValidate(List entityBeans, HashMap parameterMap) throws Exception{
	
	//================POS重傳時刪除相關銷售、出貨、過帳記錄資料=================
	String actualbrandCode = (String)parameterMap.get("brandCode");
	String actualShopCode = (String)parameterMap.get("actualShopCode");
	Date actualSalesDate = (Date)parameterMap.get("actualSalesDate");
	String opUser = (String)parameterMap.get("opUser");
	try{    
	    String organization = UserUtils.getOrganizationCodeByBrandCode(actualbrandCode);
	    if(!StringUtils.hasText(organization)){
	        throw new ValidationErrorException("品牌代號：" + actualbrandCode + "所屬的組織代號為空值！");
	    }	   
	    List<SoSalesOrderHead> salesOrderHeads = soSalesOrderHeadDAO.findSOPByProperty(parameterMap);
	    if(salesOrderHeads != null){
                for(SoSalesOrderHead salesOrderHead : salesOrderHeads){
	            String identification = MessageStatus.getIdentificationMsg(
		    salesOrderHead.getBrandCode(), salesOrderHead.getOrderTypeCode(), 
		    salesOrderHead.getOrderNo());
	            if(!OrderStatus.SIGNING.equals(salesOrderHead.getStatus())){
	                throw new ValidationErrorException(identification + "的狀態為" + OrderStatus.getChineseWord(salesOrderHead.getStatus()) + "不可執行反確認！");
	            }
		    revertToOriginallyAvailableQuantity(salesOrderHead.getHeadId(), organization, opUser);
		    soSalesOrderHeadDAO.delete(salesOrderHead);
		}
	    }
	    /*SoPostingTally postingTally = (SoPostingTally)soPostingTallyDAO.findByPrimaryKey(SoPostingTally.class, new SoPostingTallyId(actualShopCode, actualSalesDate));
	    if(postingTally != null){
	        soPostingTallyDAO.delete(postingTally);
            }*/
	}catch(Exception ex){
	    log.error("刪除品牌代號：" + actualbrandCode + "、專櫃代號：" + actualShopCode + "、銷售日期：" + DateUtils.format(actualSalesDate) + "的銷售、出貨資料時發生錯誤，原因：" + ex.toString());
            throw new Exception("刪除品牌代號：" + actualbrandCode + "、專櫃代號：" + actualShopCode + "、銷售日期：" + DateUtils.format(actualSalesDate) + "的銷售、出貨資料失敗！");
	}	    
	//========================================================================
	String organizationCode = null;
	String shopCode = null;
	Date salesDate = null;
	String brandCode = null;
	for (int index = 0; index < entityBeans.size(); index++) {			
	    SoSalesOrderHead soSalesOrderHead = (SoSalesOrderHead)entityBeans.get(index);
	    if(organizationCode == null){
		organizationCode = UserUtils.getOrganizationCodeByBrandCode(soSalesOrderHead.getBrandCode());
		if(organizationCode == null)
		    throw new NoSuchDataException("查無" + soSalesOrderHead.getBrandCode() + "的組織代號！");
	    }
	    if(shopCode == null){
		shopCode = soSalesOrderHead.getShopCode();
	    }
	    if(salesDate == null){
		salesDate = soSalesOrderHead.getSalesOrderDate();
	    }
	    if(brandCode == null){
		brandCode = soSalesOrderHead.getBrandCode();
	    }
		
            //取單別序號，insert至SO
	    String serialNo = "";
	    if("SOP".equals(soSalesOrderHead.getOrderTypeCode())){
		serialNo = buOrderTypeService.getOrderNo(soSalesOrderHead.getBrandCode(), soSalesOrderHead.getOrderTypeCode()
			, soSalesOrderHead.getShopCode(), soSalesOrderHead.getPosMachineCode());
	    }else{
		serialNo = buOrderTypeService.getOrderSerialNo(soSalesOrderHead.getBrandCode(),soSalesOrderHead.getOrderTypeCode());
	    }
	    
	    if (!serialNo.equals("unknow")) {
	        soSalesOrderHead.setOrderNo(serialNo);	
	    }else {
		throw new ObtainSerialNoFailedException("取得"
			+ soSalesOrderHead.getOrderTypeCode() + "單號失敗！");
	    }
	    soSalesOrderHeadDAO.save(soSalesOrderHead);
	    //扣庫存
	    Object[] objArray = bookAvailableQuantity(soSalesOrderHead, organizationCode, soSalesOrderHead.getCreatedBy());
	    //產生Delivery Line
	    salesOrderToDelivery(soSalesOrderHead, soSalesOrderHead.getSoSalesOrderItems(), (HashMap)objArray[1], (String)objArray[0], soSalesOrderHead.getCreatedBy());
	    if(POSTYPECODE.equals(soSalesOrderHead.getOrderTypeCode())){
	        mailToPOSAdministrator(soSalesOrderHead, (List)objArray[2]);
	    }
	}	
	//寫入過帳記錄檔 
	/*SoPostingTallyId postingTallyId = new SoPostingTallyId(shopCode, salesDate);
	if(soPostingTallyDAO.findByPrimaryKey(SoPostingTally.class, postingTallyId) == null){
	    SoPostingTally postingTally = new SoPostingTally();
	    postingTally.setId(postingTallyId);
	    postingTally.setBrandCode(brandCode);
	    postingTally.setIsPosting("N");
	    postingTally.setCreatedBy(opUser);
	    postingTally.setCreateDate(new Date());
	    postingTally.setLastUpdatedBy(opUser);
	    postingTally.setLastUpdateDate(new Date());
	    soPostingTallyDAO.save(postingTally);
	}*/
    }
    
    /**
     * 依據品牌代號、單別、專櫃代號、銷售日期、狀態進行查詢
     * 
     * @param conditionMap
     * @return SoSalesOrderHead
     * @throws Exception
     */
    public List<SoSalesOrderHead> findSalesOrderByProperty(HashMap conditionMap) throws Exception{
	try{
	    return soSalesOrderHeadDAO.findSalesOrderByProperty(conditionMap);
	}catch (Exception ex) {
	    log.error("依據品牌代號、專櫃代號、銷售日期、狀態，查詢銷售單時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌代號、專櫃代號、銷售日期、狀態，查詢銷售單時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    
    /**
     * 依據品牌代號、單別、專櫃代號、銷售日期、狀態查詢出貨單及銷貨單
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List findSalesOrderAndDeliveryByProperty(HashMap conditionMap) throws Exception{
	try{
	    return soSalesOrderHeadDAO.findSalesOrderAndDeliveryByProperty(conditionMap);
	}catch (Exception ex) {
	    log.error("依據品牌代號、單別、專櫃代號、銷售日期、狀態查詢出貨單及銷貨單時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌代號、單別、專櫃代號、銷售日期、狀態查詢出貨單及銷貨單時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 依據品牌代號、單別、單號查詢銷貨單
     * 
     * @param brandCode
     * @param orderTypeCode
     * @param orderNo
     * @return SoSalesOrderHead
     * @throws Exception
     */
    public SoSalesOrderHead findSalesOrderByIdentification(String brandCode, String orderTypeCode, String orderNo) throws Exception{
	
	try{
	    return soSalesOrderHeadDAO.findSalesOrderByIdentification(brandCode, orderTypeCode, orderNo);
	}catch (Exception ex) {
	    log.error("依據品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "查詢銷貨單時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "查詢銷貨單時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 透過匯入更新銷貨單主檔或明細檔
     * 
     * @param salesOrderHeads
     * @throws Exception
     */
    public void updateSalesOrderForImportTask(List<SoSalesOrderHead> salesOrderHeads) throws Exception{
	
	for(SoSalesOrderHead salesOrderHead : salesOrderHeads){
	    modifySoSalesOrder(salesOrderHead, salesOrderHead.getLastUpdatedBy());
	}	
    }
    
    /**
     * 複製新的SoSalesOrderHead
     * 
     * @param salesOrderHead
     * @param loginUser
     * @return SoSalesOrderHead
     */
    public SoSalesOrderHead copySalesOrder(SoSalesOrderHead salesOrderHead, String loginUser){
	
	SoSalesOrderHead newSalesOrderHead = new SoSalesOrderHead();
	BeanUtils.copyProperties(salesOrderHead, newSalesOrderHead);
	newSalesOrderHead.setHeadId(null);
	newSalesOrderHead.setOrderNo(null);
	newSalesOrderHead.setSalesOrderDate(new Date());
	newSalesOrderHead.setSuperintendentCode(loginUser);
	newSalesOrderHead.setScheduleCollectionDate(null);
	newSalesOrderHead.setScheduleShipDate(null);
	newSalesOrderHead.setCreatedBy(null);
	newSalesOrderHead.setCreationDate(null);
	newSalesOrderHead.setLastUpdatedBy(null);
	newSalesOrderHead.setLastUpdateDate(null);
	newSalesOrderHead.setStatus(OrderStatus.SAVE);
	
	List<SoSalesOrderItem> salesOrderItems = salesOrderHead.getSoSalesOrderItems();
	List<SoSalesOrderPayment> salesOrderPayments = salesOrderHead.getSoSalesOrderPayments();
	List<SoSalesOrderItem> newSalesOrderItems = new ArrayList(0);
	List<SoSalesOrderPayment> newSalesOrderPayments = new ArrayList(0);
	
	for(SoSalesOrderItem salesOrderItem : salesOrderItems){
	    SoSalesOrderItem newSalesOrderItem = new SoSalesOrderItem();
	    BeanUtils.copyProperties(salesOrderItem, newSalesOrderItem);
	    newSalesOrderItem.setLineId(null);
	    newSalesOrderItem.setSoSalesOrderHead(null);
	    newSalesOrderItem.setCreatedBy(null);
	    newSalesOrderItem.setCreationDate(null);
	    newSalesOrderItem.setLastUpdatedBy(null);
	    newSalesOrderItem.setLastUpdateDate(null);
	    //newSalesOrderItem.setStatus(OrderStatus.SAVE);
	    newSalesOrderItems.add(newSalesOrderItem);
	}
	
	for(SoSalesOrderPayment salesOrderPayment : salesOrderPayments){
	    SoSalesOrderPayment newSalesOrderPayment = new SoSalesOrderPayment();
	    BeanUtils.copyProperties(salesOrderPayment, newSalesOrderPayment);
	    newSalesOrderPayment.setPosPaymentId(null);
	    newSalesOrderPayment.setSoSalesOrderHead(null);
	    newSalesOrderPayment.setCreatedBy(null);
	    newSalesOrderPayment.setCreationDate(null);
	    newSalesOrderPayment.setLastUpdatedBy(null);
	    newSalesOrderPayment.setLastUpdateDate(null);
	    //newSalesOrderPayment.setStatus(OrderStatus.SAVE);   
	    newSalesOrderPayments.add(newSalesOrderPayment);
	}
	
	newSalesOrderHead.setSoSalesOrderItems(newSalesOrderItems);
	newSalesOrderHead.setSoSalesOrderPayments(newSalesOrderPayments);
	return newSalesOrderHead;	
    }
    
    /**
     * SOP執行反確認
     * 
     * @param soSalesOrderHead
     * @param organizationCode
     * @param loginUser
     * @return String
     * @throws FormException
     * @throws Exception
     */
    public String executeAntiConfirm(SoSalesOrderHead salesOrderHead, String organizationCode, String loginUser) throws FormException, Exception{
	
	try{
	    if(!POSTYPECODE.equals(salesOrderHead.getOrderTypeCode())){
		throw new ValidationErrorException("單別：" + salesOrderHead.getOrderTypeCode() + "不可執行反確認！");
	    }else if(!OrderStatus.SIGNING.equals(salesOrderHead.getStatus())){
		throw new ValidationErrorException("狀態：" + OrderStatus.getChineseWord(salesOrderHead.getStatus()) + "不可執行反確認！");
	    }
            revertToOriginallyAvailableQuantity(salesOrderHead.getHeadId(), organizationCode, loginUser);
            changeSoSalesOrderStatus(salesOrderHead, OrderStatus.UNCONFIRMED);
            modifySoSalesOrder(salesOrderHead, loginUser);          
            
            return salesOrderHead.getOrderTypeCode() + "-" + salesOrderHead.getOrderNo() + "執行反確認成功！";
	}catch (FormException fe) {
	    log.error("銷售單主檔主鍵：" + salesOrderHead.getHeadId() + "執行反確認時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("銷售單主檔主鍵：" + salesOrderHead.getHeadId() + "執行反確認時發生錯誤，原因：" + ex.toString());
	    throw new Exception(salesOrderHead.getOrderTypeCode() + "-" + salesOrderHead.getOrderNo() + "執行反確認時發生錯誤，原因：" + ex.getMessage());
	}	
    }
    
    private void changeSoSalesOrderStatus(SoSalesOrderHead salesOrderHead, String status){
	
	salesOrderHead.setStatus(status);
	/*List<SoSalesOrderItem> salesOrderItems = salesOrderHead.getSoSalesOrderItems();		
	List<SoSalesOrderPayment> salesOrderPayments = salesOrderHead.getSoSalesOrderPayments();
	if (salesOrderItems != null && salesOrderItems.size() > 0) {
	    for (SoSalesOrderItem orderItem : salesOrderItems) {
		orderItem.setStatus(status);
	    }	    
	}
	if (salesOrderPayments != null && salesOrderPayments.size() > 0) {
	    for (SoSalesOrderPayment orderPayment : salesOrderPayments) {
		orderPayment.setStatus(status);
	    }	    
	}*/
    }
    
    /**
     * 寄發E-mail至POS管理人員
     * 
     * @param salesOrderHead
     * @param mailContents
     */
    private void mailToPOSAdministrator(SoSalesOrderHead salesOrderHead, List mailContents){
	
	try{
            if(mailContents != null && mailContents.size() > 0){
        	String brandCode = salesOrderHead.getBrandCode();
        	String orderTypeCode = salesOrderHead.getOrderTypeCode();
        	String orderNo = salesOrderHead.getOrderNo();      	    
        	StringBuffer display = new StringBuffer();
        	Map root = new HashMap();
                for(int i = 0; i < mailContents.size(); i++){
        	    String content = (String)mailContents.get(i);
        	    String[] actualInfoArray = StringTools.StringToken(content, "#");
        	    display.append("品牌代號：" + brandCode);
        	    display.append("、單別：" + orderTypeCode);
        	    display.append("、單號：" + orderNo);
        	    display.append("，扣除品號：" + actualInfoArray[0]);
        	    display.append("、倉儲代號：" + actualInfoArray[1]);
        	    display.append("、數量：" + actualInfoArray[2]);
        	    display.append("時發生庫存不足情形！<br>");
                }
                root.put("display", display.toString());
    	        String posAdministrator = SystemConfig.getSystemConfigPro(T1_POS_ADMIN);
    	        String[] posAdministratorArray = null;
    	        if (posAdministrator == null) {
	            throw new ValidationErrorException("無法取得POS管理人員電子郵件信箱資訊！");
		}else{
	            posAdministratorArray = StringTools.StringToken(posAdministrator, ";");
		    if(posAdministratorArray == null){
		        throw new ValidationErrorException("無法拆解POS管理人員電子郵件信箱資訊！");
		    }else{
			List mailAddress = new ArrayList(0);
			for(int j = 0; j < posAdministratorArray.length; j++){
			    mailAddress.add(posAdministratorArray[j]);
			}
			MailUtils.sendMail("品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "扣庫存量時發生不足情形！" , MAIL_SKIN, root , mailAddress ) ;
		    }
		}
            }
	}catch(Exception ex){
	    log.error("寄發E-mail至POS管理人員時發生錯誤！原因：" + ex.toString());
	}
    }
    
   
    /**
     * 檢核銷貨付款資料(SoSalesOrderPayment)
     * 
     * @param orderHead
     * @param conditionMap
     * @param programId
     * @param identification
     * @param errorMsgs
     */
    private void checkSalesOrderPayments(SoSalesOrderHead orderHead, HashMap conditionMap, 
	    String programId, String identification, List errorMsgs) {
	
	String message = null;
	String tabName = "付款資料頁籤";
	try{
	    List soSalesOrderPayments = orderHead.getSoSalesOrderPayments();
	    for(int i = 0 ; i < soSalesOrderPayments.size(); i++){
		try{
	            SoSalesOrderPayment salesOrderPayment = (SoSalesOrderPayment)soSalesOrderPayments.get(i);
	            if(!"1".equals(salesOrderPayment.getIsDeleteRecord())){
	                String posPaymentType = salesOrderPayment.getPosPaymentType();
	                Double localAmount = salesOrderPayment.getLocalAmount();
	                Double discountRate = salesOrderPayment.getDiscountRate();	    
	                //金額取整數
	                if(localAmount != null && localAmount != 0D){
		            salesOrderPayment.setLocalAmount(new Double(localAmount.intValue()));
		            localAmount = salesOrderPayment.getLocalAmount();
	                }
	    
	                if(!StringUtils.hasText(posPaymentType)){
		            throw new ValidationErrorException("請選擇" + tabName + "中第" + (i + 1) + "項明細的付款類型！");
	                }/*else if((localAmount == null || localAmount == 0D) && (discountRate == null || discountRate == 0D)){
		        throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的金額或折扣率！");
	                }*/
	                else if((localAmount != null && localAmount != 0D) && (discountRate != null && discountRate != 0D)){
		            throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的金額與折扣率只可擇一輸入！");
	                }
	  
	                if(localAmount == 0D && (discountRate != null && discountRate != 0D)){
		            salesOrderPayment.setLocalAmount(null);
	                }
	                if(discountRate == 0D && (localAmount != null && localAmount != 0D)){
		            salesOrderPayment.setDiscountRate(null);
	                }
	            }
	        }catch(Exception ex){
	            message = ex.getMessage();
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);	            
	        }
	    }
	}catch(Exception ex){
	    message = "檢核" + tabName + "時發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }
    
    /**
     * 處理AJAX參數(查詢專櫃POS機號、預設庫別及庫別的倉管人員)
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getShopMachineForAJAX(Properties httpRequest) throws Exception{
	
        List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String shopCode = null;
	//回傳預設值
	String machineCode = "";
	String defaultWarehouseCode = "";
	String warehouseManager = "";
	try{
	    shopCode = httpRequest.getProperty("shopCode");
	    shopCode = shopCode.trim().toUpperCase();
	    List<BuShopMachine> shopMachines = buShopMachineDAO.findByShopCode(shopCode);
	    if(shopMachines != null && shopMachines.size() > 0){
		List<String> tn = new ArrayList(); 
		List<String> tv = new ArrayList();
		for(int i = 0; i < shopMachines.size(); i++){
		    BuShopMachine shopMachine = (BuShopMachine)shopMachines.get(i);
		    tn.add(shopMachine.getId().getPosMachineCode());
		    tv.add(shopMachine.getId().getPosMachineCode());
		}
		machineCode = AjaxUtils.getHtmlInput2DString(tn,tv);
	    }
	        
	    BuShop shopPO = buShopDAO.findById(shopCode);
	    if(shopPO != null){
		String salesWarehouseCode = shopPO.getSalesWarehouseCode();
		if(StringUtils.hasText(salesWarehouseCode)){		    
		    ImWarehouse warehousePO = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, salesWarehouseCode);
		    if(warehousePO != null){
			defaultWarehouseCode = AjaxUtils.getPropertiesValue(salesWarehouseCode, "");
			warehouseManager = AjaxUtils.getPropertiesValue(warehousePO.getWarehouseManager(), "");
		    }
		}
	    }
	    properties.setProperty("ShopMachine", machineCode);
	    properties.setProperty("DefaultWarehouseCode", defaultWarehouseCode);
	    properties.setProperty("WarehouseManager", warehouseManager);	    
	    result.add(properties);
	    
	    return result;
	}catch (Exception ex) {
	    log.error("查詢專櫃代號 ："+ shopCode + "的POS機號時發生錯誤，原因：" + ex.getMessage());
	    throw new Exception("查詢POS機號失敗！");
	}
    }
    
    public void refreshSoSalesOrder(SoSalesOrderHead orderHead) throws Exception{
	
	try{
	    String brandCode = orderHead.getBrandCode();
	    String orderTypeCode = orderHead.getOrderTypeCode();
	    String shopCode = orderHead.getShopCode();
	    String customerCode = orderHead.getCustomerCode();
	    String promotionCode = orderHead.getPromotionCode();
	    String superintendentCode = orderHead.getSuperintendentCode();
	    String casherCode = orderHead.getCasherCode();
	    String customerName = "查無此客戶資料";
	    String promotionName = "查無資料";
	    String superintendentName = "查無此員工資料";
	    String casherName = "查無此員工資料";
	    String customerType = null;
	    String vipType = null;
	    String priceType = "1";
	    Date salesDate = orderHead.getSalesOrderDate();
	    String salesOrderDate  = DateUtils.format(salesDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
	    
	    BuOrderType orderTypePO = buOrderTypeService.findById(new BuOrderTypeId(brandCode, orderTypeCode));
	    if(orderTypePO != null){
		String priceTypeCode = orderTypePO.getPriceType();
		if(StringUtils.hasText(priceTypeCode))
		    priceType = priceTypeCode;
	    }
	    
	    HashMap conditionMap = new HashMap();
            conditionMap.put("brandCode", brandCode);
	    conditionMap.put("promotionCode", promotionCode);
            conditionMap.put("priceType", priceType);
	    conditionMap.put("shopCode", shopCode);	    
	    conditionMap.put("salesDate", salesOrderDate);
	    
	    if(StringUtils.hasText(customerCode)){
		BuCustomerWithAddressView customerWithAddressView = buCustomerWithAddressViewDAO.findCustomerByType
	            (brandCode, customerCode, "customerCode", null);
		if(customerWithAddressView != null){
		    customerName = customerWithAddressView.getShortName();
		    customerType = customerWithAddressView.getCustomerTypeCode();
		    vipType = customerWithAddressView.getVipTypeCode();
		}
		orderHead.setCustomerName(customerName);
	    }
	    
	    if(StringUtils.hasText(superintendentCode)){
	        BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, superintendentCode);
	        if(employeeWithAddressView != null){
	            superintendentName = employeeWithAddressView.getChineseName();
                }
	        orderHead.setSuperintendentName(superintendentName);
	    }
	
	    if(StringUtils.hasText(casherCode)){
	        BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, casherCode);
	        if(employeeWithAddressView != null){
	            casherName = employeeWithAddressView.getChineseName();
                }
        	orderHead.setCasherName(casherName);             
	    }
	    
	    if(StringUtils.hasText(promotionCode)){
		conditionMap.put("customerType", customerType);
		conditionMap.put("vipType", vipType);
		Object[] promotionInfo = imPromotionViewDAO.findPromotionCodeByCondition(conditionMap);
		if(promotionInfo != null){
		    if(promotionInfo[3] != null && promotionInfo[4] != null)
			promotionName = (String)promotionInfo[2];
		}
		orderHead.setPromotionName(promotionName);
	    }
	}catch(Exception ex){
	    log.error("查詢銷貨單相關資訊時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢銷貨單相關資訊時發生錯誤，原因：" + ex.getMessage());
	}	
    }
    
    public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{

	try{
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小	    
	    //======================帶入Head的值=========================
	    String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
	    String shopCode = httpRequest.getProperty("shopCode");// 品牌代號
	    String formStatus = httpRequest.getProperty("formStatus");// 狀態
	    String defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode");// 預設倉別
	    String taxType = httpRequest.getProperty("taxType");// 稅別
	    String taxRate = httpRequest.getProperty("taxRate");// 稅率
	    String discountRate = httpRequest.getProperty("discountRate");// 折扣比率
	    String vipPromotionCode = httpRequest.getProperty("vipPromotionCode");//vip類別代號
	    String promotionCode = httpRequest.getProperty("promotionCode");//活動代號
	    String warehouseEmployee = httpRequest.getProperty("warehouseEmployee");
	    String warehouseManager = httpRequest.getProperty("warehouseManager");
	    String customerType = httpRequest.getProperty("customerType");
	    String vipType = httpRequest.getProperty("vipType");
	    String priceType = httpRequest.getProperty("priceType");
	    String salesDate = httpRequest.getProperty("salesDate");
	    salesDate = DateUtils.format(DateUtils.parseDate("yyyy/MM/dd", salesDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
	   
	    HashMap map = new HashMap();
	    map.put("brandCode", brandCode);
	    map.put("orderTypeCode", orderTypeCode);
	    map.put("shopCode", shopCode);
	    map.put("warehouseCode", defaultWarehouseCode);
	    map.put("taxType", taxType);
	    map.put("taxRate", taxRate);
	    map.put("discountRate", discountRate);
	    map.put("vipPromotionCode", vipPromotionCode);
	    map.put("promotionCode", promotionCode);
	    map.put("warehouseEmployee", warehouseEmployee);
	    map.put("warehouseManager", warehouseManager);
	    map.put("customerType", customerType);
	    map.put("vipType", vipType);
	    map.put("priceType", priceType);
	    map.put("salesDate", salesDate);	    
	    getDefaultParameter(map);	    
	    //==============================================================	    
	    List<SoSalesOrderItem> soSalesOrderItems = soSalesOrderItemDAO.findPageLine(headId, iSPage, iPSize);
	    if(soSalesOrderItems != null && soSalesOrderItems.size() > 0){
		// 取得第一筆的INDEX
		Long firstIndex = soSalesOrderItems.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		Long maxIndex = soSalesOrderItemDAO.findPageLineMaxIndex(headId);		
		if(OrderStatus.SAVE.equals(formStatus) || OrderStatus.REJECT.equals(formStatus) 
		    || OrderStatus.UNCONFIRMED.equals(formStatus)){
		    //可編輯狀態
		    refreshSoItemDataForModify(map, soSalesOrderItems);
		}else{
		    //不可編輯狀態
	            refreshSoItemDataForReadOnly(map, soSalesOrderItems);
		}
		map.put("itemCName", "查無資料");
		map.put("warehouseName", "查無資料");
		result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, soSalesOrderItems, gridDatas,
			firstIndex, maxIndex));		    
	    }else{
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map, gridDatas));   
	    }
	
	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的銷貨明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的銷貨明細失敗！");
	}	
    }
    
    /**
     * 更新SoItem相關資料(狀態為可編輯時)
     * 
     * @param parameterMap
     * @param salesOrderItems
     */
    private void refreshSoItemDataForModify(HashMap parameterMap, List<SoSalesOrderItem> salesOrderItems) throws Exception{
	
	String brandCode = (String)parameterMap.get("brandCode");
	String shopCode = (String)parameterMap.get("shopCode");
	String warehouseEmployee = (String)parameterMap.get("warehouseEmployee");
	String warehouseManager = (String)parameterMap.get("warehouseManager");
	String priceType = (String)parameterMap.get("priceType");
	String customerType = (String)parameterMap.get("customerType");
	String vipType = (String)parameterMap.get("vipType");
	String salesDate = (String)parameterMap.get("salesDate");
	String defaultVipPromotionCode = (String)parameterMap.get("vipPromotionCode");
	String defaultPromotionCode = (String)parameterMap.get("promotionCode");
	HashMap conditionMap = new HashMap();
	conditionMap.put("brandCode", brandCode);
	conditionMap.put("shopCode", shopCode);	
	conditionMap.put("warehouseEmployee", warehouseEmployee);
	conditionMap.put("warehouseManager", warehouseManager);
	conditionMap.put("priceType", priceType);
	conditionMap.put("customerType", customerType);
	conditionMap.put("vipType", vipType);
	conditionMap.put("salesDate", salesDate);
	
	for(SoSalesOrderItem salesOrderItem : salesOrderItems){
	    String itemCode = salesOrderItem.getItemCode();
	    String warehouseCode = salesOrderItem.getWarehouseCode();
	    Double quantity = salesOrderItem.getQuantity();
	    Double discountRate = salesOrderItem.getDiscountRate();
	    if(discountRate == null){
		discountRate = 100D;
		salesOrderItem.setDiscountRate(discountRate);
	    }
	    Double deductionAmount = salesOrderItem.getDeductionAmount();
	    String promotionCode = salesOrderItem.getPromotionCode();
	    /*if(!StringUtils.hasText(promotionCode)){
		promotionCode = defaultPromotionCode;
		salesOrderItem.setPromotionCode(promotionCode);
	    }*/
	    
	    //依據customer的VipPromotionCode帶入line中
            salesOrderItem.setVipPromotionCode(defaultVipPromotionCode);
	    Double originalUnitPrice = salesOrderItem.getOriginalUnitPrice();
	    String taxType = salesOrderItem.getTaxType();
	    Double taxRate = salesOrderItem.getTaxRate();
	    
	    conditionMap.put("itemCode", itemCode);
	    conditionMap.put("warehouseCode", warehouseCode);
	    conditionMap.put("quantity", quantity);
	    conditionMap.put("discountRate", discountRate);
	    conditionMap.put("deductionAmount", deductionAmount);
	    conditionMap.put("promotionCode", promotionCode);
	    conditionMap.put("vipPromotionCode", defaultVipPromotionCode);
	    conditionMap.put("originalUnitPrice", originalUnitPrice);
	    conditionMap.put("taxType", taxType);
	    conditionMap.put("taxRate", taxRate);
	    //查詢後的銷售明細
	    SoSalesOrderItem soItemInfo = appGetSaleItemInfoService.getSaleItemInfo(conditionMap, setSoItemBean(conditionMap));
	    //copy查詢後的銷售明細
	    setSoItemInfo(salesOrderItem, soItemInfo);
	    refreshItemPriceRelationData(salesOrderItem);	    
	}
    }
    
    /**
     * 活動代號折扣資料更新
     * 
     * @param salesOrderItem
     * @param conditionMap
     */
    private void refreshPromotionRelationData(SoSalesOrderItem salesOrderItem , HashMap conditionMap){
	
	String promotionCode = salesOrderItem.getPromotionCode();
	if(StringUtils.hasText(promotionCode)){
	    promotionCode = promotionCode.trim().toUpperCase();
            salesOrderItem.setPromotionCode(promotionCode);
	    conditionMap.put("promotionCode", promotionCode);
	    ImPromotionView promotion = imPromotionViewDAO.findPromotionCodeByProperty(conditionMap);
	    if(promotion == null){
	        salesOrderItem.setPromotionCode(null);
		salesOrderItem.setDiscountType(null);
		salesOrderItem.setDiscount(null);
            }else{
		salesOrderItem.setDiscountType(promotion.getDiscountType());
		salesOrderItem.setDiscount(promotion.getDiscount());
	    }		
	}else{
	    salesOrderItem.setPromotionCode(null);
	    salesOrderItem.setDiscountType(null);
	    salesOrderItem.setDiscount(null);
	}	
    }
    
    /**
     * VIP類別代號折扣資料更新
     * 
     * @param salesOrderItem
     * @param conditionMap
     */
    private void refreshVIPPromotionRelationData(SoSalesOrderItem salesOrderItem , HashMap conditionMap){
	
	String vipPromotionCode = salesOrderItem.getVipPromotionCode();
	if(StringUtils.hasText(vipPromotionCode)){
            vipPromotionCode = vipPromotionCode.trim().toUpperCase();
            salesOrderItem.setVipPromotionCode(vipPromotionCode);
	    conditionMap.put("promotionCode", vipPromotionCode);
	    ImPromotionView vipPromotion = imPromotionViewDAO.findPromotionCodeByProperty(conditionMap);
	    if(vipPromotion == null){
	        salesOrderItem.setVipPromotionCode(null);
		salesOrderItem.setVipDiscountType(null);
		salesOrderItem.setVipDiscount(null);
	    }else{
		salesOrderItem.setVipDiscountType(vipPromotion.getDiscountType());
		salesOrderItem.setVipDiscount(vipPromotion.getDiscount());
            }		
	}else{
            salesOrderItem.setVipPromotionCode(null);
	    salesOrderItem.setVipDiscountType(null);
	    salesOrderItem.setVipDiscount(null);
	}	
    }
    
    /**
     * 更新折扣後單價、重新計算Item的原銷售金額、實際售價、實際銷售金額、稅金
     * 
     * @param salesOrderItem
     */
    private void refreshItemPriceRelationData(SoSalesOrderItem salesOrderItem){
	
	String isServiceItem = salesOrderItem.getIsServiceItem();
	if(!"Y".equals(isServiceItem)){
	    //計算折扣後單價
	    HashMap parameterMap = new HashMap();
	    parameterMap.put("originalUnitPrice", salesOrderItem.getOriginalUnitPrice());
	    parameterMap.put("promotionCode", salesOrderItem.getPromotionCode());
	    parameterMap.put("discountType", salesOrderItem.getDiscountType());
	    parameterMap.put("discount", salesOrderItem.getDiscount());
	    parameterMap.put("vipPromotionCode", salesOrderItem.getVipPromotionCode());
	    parameterMap.put("vipDiscountType", salesOrderItem.getVipDiscountType());
	    parameterMap.put("vipDiscount", salesOrderItem.getVipDiscount());
	    Double discountRate = salesOrderItem.getDiscountRate();
	    if(discountRate == null){
		discountRate = 100D;
		salesOrderItem.setDiscountRate(discountRate);
	    }
	    parameterMap.put("discountRate", discountRate);
	    parameterMap.put("deductionAmount", salesOrderItem.getDeductionAmount());
	    salesOrderItem.setActualUnitPrice(calculateActualPrice(parameterMap));
	}else{
            salesOrderItem.setActualUnitPrice(salesOrderItem.getOriginalUnitPrice());
	}
	
	refreshItemRelationAmount(salesOrderItem);
    }
    
    /**
     * 重新計算Item的原銷售金額、實際售價、實際銷售金額、稅金
     * 
     * @param orderItem
     */
    private void refreshItemRelationAmount(SoSalesOrderItem orderItem) {
	
	Double originalSalesAmount = 0D;
	Double actualSalesAmount = 0D;
	Double taxAmount = 0D;
	
	Double originalUnitPrice = orderItem.getOriginalUnitPrice(); //原始售價
	Double actualUnitPrice = orderItem.getActualUnitPrice(); // 實際售價
	Double quantity = orderItem.getQuantity(); // 數量
	String taxType = orderItem.getTaxType(); // 稅別
	Double taxRate = orderItem.getTaxRate(); // 稅率

	if(originalUnitPrice != null && actualUnitPrice != null && quantity != null){
	    originalSalesAmount = CommonUtils.round(originalUnitPrice * quantity, 0); // 原銷售金額
	    actualSalesAmount = CommonUtils.round(actualUnitPrice * quantity , 0); // 實際銷售金額
	    taxAmount = calculateTaxAmount(taxType, taxRate, actualSalesAmount);
	}
	orderItem.setOriginalSalesAmount(originalSalesAmount);
	orderItem.setActualSalesAmount(actualSalesAmount);
	orderItem.setTaxAmount(taxAmount);
    }
    
    
    /**
     * 更新SoItem相關資料(狀態為不可編輯時)
     * 
     * @param parameterMap
     * @param salesOrderItems
     */
    private void refreshSoItemDataForReadOnly(HashMap parameterMap, List<SoSalesOrderItem> salesOrderItems){
	
	String brandCode = (String)parameterMap.get("brandCode");
	for(SoSalesOrderItem salesOrderItem : salesOrderItems){
	    String itemCode = salesOrderItem.getItemCode();
	    String warehouseCode = salesOrderItem.getWarehouseCode();
	    //品名
	    ImItem itemPO = imItemDAO.findItem(brandCode, itemCode);
	    if(itemPO != null){
		salesOrderItem.setItemCName(itemPO.getItemCName());
	    }
	    
	    //庫別名稱
	    ImWarehouse warehouse = (ImWarehouse) imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, warehouseCode, null);
	    if(warehouse != null){
		salesOrderItem.setWarehouseName(warehouse.getWarehouseName());
	    }
	}
    }
    
    /**
     * 取得暫時單號存檔
     * 
     * @param salesOrderHead
     * @throws Exception
     */
    public void saveTmp(SoSalesOrderHead salesOrderHead) throws Exception{
	
	try{
	    String tmpOrderNo = AjaxUtils.getTmpOrderNo();
	    salesOrderHead.setOrderNo(tmpOrderNo);
	    salesOrderHead.setLastUpdateDate(new Date());
	    salesOrderHead.setCreationDate(new Date());
	    soSalesOrderHeadDAO.save(salesOrderHead);	    
	}catch(Exception ex){
	    log.error("取得暫時單號儲存銷貨單發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得暫時單號儲存銷貨單發生錯誤，原因：" + ex.getMessage());
	}	
    }
    
    /**
     * item帶入head的預設值
     * 
     * @param parameterMap
     */
    private void getDefaultParameter(HashMap parameterMap){
	
        String taxType = (String)parameterMap.get("taxType");
	String discountRate = (String)parameterMap.get("discountRate");
	String brandCode = (String)parameterMap.get("brandCode");
	String warehouseCode = (String)parameterMap.get("warehouseCode");
	//taxType、taxRate預設值
	if("1".equals(taxType) || "2".equals(taxType)){
	    parameterMap.put("taxRate", "0.0");
	}else if("3".equals(taxType)){
	    parameterMap.put("taxRate", "5.0");
	}else{
            parameterMap.put("taxType", "3");
	    parameterMap.put("taxRate", "5.0");
	}
	//折扣比率預設值
	try{
            Double.parseDouble(discountRate);
	}catch(NumberFormatException nfe){
	    parameterMap.put("discountRate", "100.0");
	}
	//預設庫別名稱
	if(StringUtils.hasText(warehouseCode)){
	    ImWarehouse warehousePO = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, warehouseCode, null);
	    if(warehousePO != null){
	        parameterMap.put("warehouseName", warehousePO.getWarehouseName());
	    }else{
		parameterMap.put("warehouseName", "查無資料");
	    }
	}
    }
    
    /**
     * 處理AJAX參數(line品號、庫別、數量變動時計算)
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws ValidationErrorException
     */
    public List<Properties> getAJAXItemData(Properties httpRequest) throws ValidationErrorException{
	
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String itemIndexNo = null;
	try{
	    itemIndexNo = httpRequest.getProperty("itemIndexNo");
	    String brandCode = httpRequest.getProperty("brandCode");
	    String priceType = httpRequest.getProperty("priceType");// 價格類型
	    String shopCode = httpRequest.getProperty("shopCode");
	    String customerType = httpRequest.getProperty("customerType");
	    String vipType = httpRequest.getProperty("vipType");
	    String itemCode = httpRequest.getProperty("itemCode");
	    String warehouseCode = httpRequest.getProperty("warehouseCode");
	    String quantity = httpRequest.getProperty("quantity");
	    String deductionAmount = httpRequest.getProperty("deductionAmount");
	    String discountRate = httpRequest.getProperty("discountRate");
	    if(!StringUtils.hasText(discountRate)){
		discountRate = "100";
	    }
	    String promotionCode = httpRequest.getProperty("promotionCode");//活動代號
	    String vipPromotionCode = httpRequest.getProperty("vipPromotionCode");//vip類別代號
	    String warehouseManager = httpRequest.getProperty("warehouseManager");
	    String warehouseEmployee = httpRequest.getProperty("warehouseEmployee");
	    String originalUnitPrice = httpRequest.getProperty("originalUnitPrice");
	    String salesDate = httpRequest.getProperty("salesDate");
	    salesDate = DateUtils.format(DateUtils.parseDate("yyyy/MM/dd", salesDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
	    String taxType = httpRequest.getProperty("taxType");
	    String taxRate = httpRequest.getProperty("taxRate");	    
	    String actionId = httpRequest.getProperty("actionId");
	    //查詢相關參數
	    HashMap conditionMap = new HashMap();
	    conditionMap.put("brandCode", brandCode);
	    conditionMap.put("priceType", priceType);
	    conditionMap.put("warehouseEmployee", warehouseEmployee);
	    conditionMap.put("warehouseManager", warehouseManager);
	    conditionMap.put("shopCode", shopCode);
	    conditionMap.put("customerType", customerType);
	    conditionMap.put("vipType", vipType);
	    conditionMap.put("itemCode", itemCode);
	    conditionMap.put("warehouseCode", warehouseCode);
	    conditionMap.put("quantity", quantity);
	    conditionMap.put("discountRate", discountRate);
	    conditionMap.put("deductionAmount", deductionAmount);
	    conditionMap.put("promotionCode", promotionCode);
	    conditionMap.put("vipPromotionCode", vipPromotionCode);
	    conditionMap.put("originalUnitPrice", originalUnitPrice);
	    conditionMap.put("salesDate", salesDate);
	    conditionMap.put("taxType", taxType);
	    conditionMap.put("taxRate", taxRate);
	    SoSalesOrderItem salesOrderItem = setSoItemData(conditionMap); //原銷售明細
	    SoSalesOrderItem actualSalesOrderItem = appGetSaleItemInfoService.getSaleItemInfo(conditionMap, salesOrderItem);//查詢後銷售明細
	    if("Y".equals(actualSalesOrderItem.getIsServiceItem()) && "1".equals(actionId)){
		actualSalesOrderItem.setOriginalUnitPrice(null);
            }
	    //price相關資料更新
	    refreshItemPriceRelationData(actualSalesOrderItem);

	    properties.setProperty("ItemCode", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getItemCode(), ""));
	    properties.setProperty("ItemCName", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getItemCName(), "查無資料"));
	    properties.setProperty("WarehouseCode", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getWarehouseCode(), ""));
	    properties.setProperty("WarehouseName", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getWarehouseName(), "查無資料"));
	    properties.setProperty("OriginalUnitPrice", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getOriginalUnitPrice(), ""));
	    properties.setProperty("ActualUnitPrice", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getActualUnitPrice(), ""));
	    properties.setProperty("CurrentOnHandQty", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getCurrentOnHandQty(), ""));
	    properties.setProperty("Quantity", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getQuantity(), ""));
	    properties.setProperty("OriginalSalesAmount", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getOriginalSalesAmount(), ""));
	    properties.setProperty("ActualSalesAmount", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getActualSalesAmount(), ""));
	    properties.setProperty("DeductionAmount", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getDeductionAmount(), "0.00"));
	    properties.setProperty("DiscountRate", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getDiscountRate(), "100.0"));
	    properties.setProperty("IsTax", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getIsTax(), "1"));
	    properties.setProperty("PromotionCode", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getPromotionCode(), ""));
	    properties.setProperty("PromotionName", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getPromotionName(), "查無資料"));
	    properties.setProperty("DiscountType", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getDiscountType(), ""));
	    properties.setProperty("Disct", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getDiscount(), ""));
	    properties.setProperty("VipPromotionCode", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getVipPromotionCode(), ""));
	    properties.setProperty("VipPromotionName", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getVipPromotionName(), "查無資料"));
	    properties.setProperty("VipDiscountType", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getVipDiscountType(), ""));
	    properties.setProperty("VipDisct", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getVipDiscount(), ""));
	    properties.setProperty("TaxType", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getTaxType(), "3"));
	    properties.setProperty("TaxRate", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getTaxRate(), "5"));
	    properties.setProperty("IsServiceItem", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getIsServiceItem(), ""));
	    properties.setProperty("TaxAmount", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getTaxAmount(), "0.0"));
	    result.add(properties);
	    
	    return result;
	}catch (Exception ex) {
	    log.error("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料發生錯誤，原因：" + ex.getMessage());
	    throw new ValidationErrorException("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料失敗！");
	}
    }
    
    private SoSalesOrderItem setSoItemData(HashMap conditionMap){
		
	String itemCode = (String)conditionMap.get("itemCode");	
	String warehouseCode = (String)conditionMap.get("warehouseCode");			
	String quantity = (String)conditionMap.get("quantity");	
	String discountRate = (String)conditionMap.get("discountRate");
	String deductionAmount = (String)conditionMap.get("deductionAmount");
	String promotionCode = (String)conditionMap.get("promotionCode");
	String vipPromotionCode = (String)conditionMap.get("vipPromotionCode");
	String originalUnitPrice = (String)conditionMap.get("originalUnitPrice");
	String taxType = (String)conditionMap.get("taxType");
	String taxRate = (String)conditionMap.get("taxRate");
	
	SoSalesOrderItem salesOrderItem = new SoSalesOrderItem();	
	if(StringUtils.hasText(itemCode)){
	    itemCode = itemCode.trim().toUpperCase();
	}
	if(StringUtils.hasText(warehouseCode)){
	    warehouseCode = warehouseCode.trim().toUpperCase();
	}
	if(StringUtils.hasText(promotionCode)){
	    promotionCode = promotionCode.trim().toUpperCase();
	}
	salesOrderItem.setItemCode(itemCode);
	salesOrderItem.setWarehouseCode(warehouseCode);
	salesOrderItem.setPromotionCode(promotionCode);
	salesOrderItem.setVipPromotionCode(vipPromotionCode);
	salesOrderItem.setTaxType(taxType);
	//數量
	try{
	    Double salesQuantity = Double.parseDouble(quantity);
	    salesOrderItem.setQuantity(salesQuantity);
	    conditionMap.put("quantity", salesQuantity);
	}catch(NumberFormatException nfe){
	    salesOrderItem.setQuantity(0D);
	    conditionMap.put("quantity", 0D);
	}
	//折扣比率
	try{
	    Double actualDiscountRate = Double.parseDouble(discountRate);
	    salesOrderItem.setDiscountRate(actualDiscountRate);
	    conditionMap.put("discountRate", actualDiscountRate);
	}catch(NumberFormatException nfe){
	    salesOrderItem.setDiscountRate(100D);
	    conditionMap.put("discountRate", 100D);
	}
	//折讓
	try{
	    Double actualDeductionAmount = Double.parseDouble(deductionAmount);
	    salesOrderItem.setDeductionAmount(actualDeductionAmount);
	    conditionMap.put("deductionAmount", actualDeductionAmount);
	}catch(NumberFormatException nfe){
	    salesOrderItem.setDeductionAmount(0D);
	    conditionMap.put("deductionAmount", 0D);
	}
	//原始單價
	try{
	    Double origiUnitPrice = Double.parseDouble(originalUnitPrice);
	    salesOrderItem.setOriginalUnitPrice(origiUnitPrice);
	    conditionMap.put("originalUnitPrice", origiUnitPrice);
	}catch(NumberFormatException nfe){
	    salesOrderItem.setOriginalUnitPrice(0D);
	    conditionMap.put("originalUnitPrice", 0D);
	}
	
	//稅率
	try{
	    Double actualTaxRate = Double.parseDouble(taxRate);
	    salesOrderItem.setTaxRate(actualTaxRate);
	    conditionMap.put("taxRate", actualTaxRate);
	}catch(NumberFormatException nfe){
	    salesOrderItem.setTaxRate(null);
	    conditionMap.put("taxRate", null);
	}
	
	return salesOrderItem;
    }
    
    /**
     * 更新PAGE的LINE
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception{
	
        try{
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    if(headId == null){
		throw new ValidationErrorException("傳入的銷貨單主鍵為空值！");
	    }
	    String status = httpRequest.getProperty("status");
	    String errorMsg = null;
	    
	    if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status) || OrderStatus.UNCONFIRMED.equals(status)) {
		SoSalesOrderHead salesOrderHead = new SoSalesOrderHead();
		salesOrderHead.setHeadId(headId);
		// 將STRING資料轉成List Properties record data
		List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
		// Get INDEX NO
		int indexNo = soSalesOrderItemDAO.findPageLineMaxIndex(headId).intValue();
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
		        // 先載入HEAD_ID OR LINE DATA
			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
			String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);
			if (StringUtils.hasText(itemCode)) {
			    SoSalesOrderItem salesOrderItemPO = soSalesOrderItemDAO.findItemByIdentification(salesOrderHead.getHeadId(), lineId);
			    if(salesOrderItemPO != null){
				AjaxUtils.setPojoProperties(salesOrderItemPO, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
				soSalesOrderItemDAO.update(salesOrderItemPO);
			    }else{
				indexNo++;
				SoSalesOrderItem salesOrderItem = new SoSalesOrderItem();
				AjaxUtils.setPojoProperties(salesOrderItem, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
				salesOrderItem.setIndexNo(Long.valueOf(indexNo));
				salesOrderItem.setSoSalesOrderHead(salesOrderHead);				
				soSalesOrderItemDAO.save(salesOrderItem);
			    }
			}
		    }
		}
	    }
	    
	    return AjaxUtils.getResponseMsg(errorMsg);
        }catch(Exception ex){
            log.error("更新銷貨明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新銷貨明細失敗！"); 
        }	
    }
    
    /**
     * 將銷貨資料更新至銷貨單主檔及明細檔(AJAX)
     * 
     * @param soSalesOrderHead
     * @param conditionMap
     * @param organizationCode
     * @param loginUser
     * @return String
     * @throws FormException
     * @throws Exception
     */
    public String updateAJAXSoSalesOrder(SoSalesOrderHead salesOrderHead, HashMap conditionMap, String organizationCode,
	    String loginUser) throws FormException, Exception {

	try {
	    soSalesOrderPaymentService.deleteSalesOrderPayment(salesOrderHead);
	    SoSalesOrderHead salesOrderHeadPO = getActualSalesOrder(salesOrderHead);
	    sortSalesOrderItem(salesOrderHeadPO);
	    //=====================================================================
	    String brandCode = salesOrderHead.getBrandCode();
	    String orderTypeCode = salesOrderHead.getOrderTypeCode();
	    String customerCode_var = (String)conditionMap.get("customerCode_var");
	    String searchCustomerType = (String)conditionMap.get("searchCustomerType");    
	    //================取得customerType、vipType、vipPromotionCode======================
	    HashMap customerInfoMap = getCustomerInfo(brandCode, orderTypeCode, customerCode_var, searchCustomerType, null);
	    String actualCustomerCode = (String)customerInfoMap.get("actualCustomerCode");
	    salesOrderHeadPO.setCustomerCode(actualCustomerCode);
	    String customerType = (String)customerInfoMap.get("customerType");
	    String vipType = (String)customerInfoMap.get("vipType");
	    String vipPromotionCode = (String)customerInfoMap.get("vipPromotionCode");
	    conditionMap.put("customerType", customerType);
	    conditionMap.put("vipType", vipType);
	    conditionMap.put("vipPromotionCode", vipPromotionCode);
	    //================================================================================
	    getItemPriceRelationData(salesOrderHeadPO, conditionMap, false);
	    countTotalAmount(salesOrderHeadPO, conditionMap, null);
	    String message = modifyAjaxSoSalesOrder(salesOrderHeadPO, loginUser);	    
	    if (OrderStatus.REJECT.equals(salesOrderHeadPO.getStatus())) {
		revertToOriginallyAvailableQuantity(salesOrderHeadPO
			.getHeadId(), organizationCode, loginUser);
	    }
	    return message;
	} catch (FormException fe) {
	    log.error("銷售單存檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("銷售單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷售單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
   
    /**
     * remove delete mark record
     * 
     * @param salesOrderHead
     */
    private void removeAJAXLine(SoSalesOrderHead salesOrderHead) {
		
        List<SoSalesOrderItem> salesOrderItems = salesOrderHead.getSoSalesOrderItems();
        List<SoSalesOrderPayment> salesOrderPayments = salesOrderHead.getSoSalesOrderPayments();       
        if(salesOrderItems != null && salesOrderItems.size() > 0){
            for(int i = salesOrderItems.size() - 1; i >= 0; i--){
        	SoSalesOrderItem salesOrderItem = salesOrderItems.get(i);         
        	if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(salesOrderItem.getIsDeleteRecord())){
        	    salesOrderItems.remove(salesOrderItem);
        	}
            }
        }
        
        if(salesOrderPayments != null && salesOrderPayments.size() > 0){
            for(int i = salesOrderPayments.size() - 1; i >= 0; i--){
        	SoSalesOrderPayment salesOrderPayment = salesOrderPayments.get(i);         
        	if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(salesOrderPayment.getIsDeleteRecord())){
        	    salesOrderItems.remove(salesOrderPayment);
        	}
            }
        }
    }
    
    private SoSalesOrderHead getActualSalesOrder(SoSalesOrderHead salesOrderHead) throws Exception{
	 
        // load line
	SoSalesOrderHead salesOrderHeadPO = findSoSalesOrderHeadById(salesOrderHead.getHeadId());
	if(salesOrderHeadPO == null){
	    throw new ValidationErrorException("查無銷貨單主鍵：" + salesOrderHead.getHeadId() + "的資料！");
	}
	
	salesOrderHead.setSoSalesOrderItems(salesOrderHeadPO.getSoSalesOrderItems());
	BeanUtils.copyProperties(salesOrderHead, salesOrderHeadPO);
	//create new SoSalesOrderPayment
	List<SoSalesOrderPayment> actualSaveSalesOrderPayments = new ArrayList(0);
	List<SoSalesOrderPayment> salesOrderPayments = salesOrderHead.getSoSalesOrderPayments();	
	if(salesOrderPayments != null){
	    for(SoSalesOrderPayment salesOrderPayment : salesOrderPayments){
		SoSalesOrderPayment newSoPayment = new SoSalesOrderPayment();
		BeanUtils.copyProperties(salesOrderPayment, newSoPayment);
		newSoPayment.setSoSalesOrderHead(null);
		actualSaveSalesOrderPayments.add(newSoPayment);
	    }
	}	
	salesOrderHeadPO.setSoSalesOrderPayments(actualSaveSalesOrderPayments);
	// remove line record
	removeAJAXLine(salesOrderHeadPO);
	return salesOrderHeadPO;
    }
    
    /**
     * 暫存單號取實際單號並更新至銷貨單主檔及明細檔
     * 
     * @param soSalesOrderHead
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String modifyAjaxSoSalesOrder(SoSalesOrderHead soSalesOrderHead, String loginUser)
    throws ObtainSerialNoFailedException, FormException, Exception {

	if (AjaxUtils.isTmpOrderNo(soSalesOrderHead.getOrderNo())) {
	    String serialNo = "";
	    if("SOP".equals(soSalesOrderHead.getOrderTypeCode())){
		serialNo = buOrderTypeService.getOrderNo(soSalesOrderHead.getBrandCode(), soSalesOrderHead.getOrderTypeCode()
			, soSalesOrderHead.getShopCode(), soSalesOrderHead.getPosMachineCode());
	    }else{
		serialNo = buOrderTypeService.getOrderSerialNo(
			soSalesOrderHead.getBrandCode(), soSalesOrderHead
			.getOrderTypeCode());
	    }

	    if (!serialNo.equals("unknow")) {
		soSalesOrderHead.setOrderNo(serialNo);
	    } else {
		throw new ObtainSerialNoFailedException("取得"
			+ soSalesOrderHead.getOrderTypeCode() + "單號失敗！");
	    }
	}	
	modifySoSalesOrder(soSalesOrderHead, loginUser);	
	return soSalesOrderHead.getOrderTypeCode() + "-" + soSalesOrderHead.getOrderNo() + "存檔成功！";
    }
    
    private SoSalesOrderItem setSoItemBean(HashMap conditionMap){
	
	String itemCode = (String)conditionMap.get("itemCode");	
	String warehouseCode = (String)conditionMap.get("warehouseCode");			
	Double quantity = (Double)conditionMap.get("quantity");	
	Double discountRate = (Double)conditionMap.get("discountRate");
	Double deductionAmount = (Double)conditionMap.get("deductionAmount");
	String promotionCode = (String)conditionMap.get("promotionCode");
	String vipPromotionCode = (String)conditionMap.get("vipPromotionCode");
	Double originalUnitPrice = (Double)conditionMap.get("originalUnitPrice");
	String taxType = (String)conditionMap.get("taxType");
	Double taxRate = (Double)conditionMap.get("taxRate");
	
	SoSalesOrderItem salesOrderItem = new SoSalesOrderItem();	
	if(StringUtils.hasText(itemCode)){
	    itemCode = itemCode.trim().toUpperCase();
	}
	if(StringUtils.hasText(warehouseCode)){
	    warehouseCode = warehouseCode.trim().toUpperCase();
	}
	if(StringUtils.hasText(promotionCode)){
	    promotionCode = promotionCode.trim().toUpperCase();
	}
	salesOrderItem.setItemCode(itemCode);
	salesOrderItem.setWarehouseCode(warehouseCode);
	salesOrderItem.setQuantity(quantity);
	salesOrderItem.setDiscountRate(discountRate);
	salesOrderItem.setDeductionAmount(deductionAmount);
	salesOrderItem.setPromotionCode(promotionCode);
	salesOrderItem.setVipPromotionCode(vipPromotionCode);
	salesOrderItem.setOriginalUnitPrice(originalUnitPrice);
	salesOrderItem.setTaxType(taxType);
	salesOrderItem.setTaxRate(taxRate);	
	
	return salesOrderItem;
    }
    
    private void setSoItemInfo(SoSalesOrderItem origiSalesOrderItem, SoSalesOrderItem salesOrderItem){
	
	origiSalesOrderItem.setItemCName(salesOrderItem.getItemCName());
	origiSalesOrderItem.setWarehouseName(salesOrderItem.getWarehouseName());
	//origiSalesOrderItem.setQuantity(salesOrderItem.getQuantity());
	origiSalesOrderItem.setCurrentOnHandQty(salesOrderItem.getCurrentOnHandQty());
	origiSalesOrderItem.setOriginalUnitPrice(salesOrderItem.getOriginalUnitPrice());
	origiSalesOrderItem.setVipPromotionName(salesOrderItem.getVipPromotionName());
	origiSalesOrderItem.setVipDiscount(salesOrderItem.getVipDiscount());
	origiSalesOrderItem.setVipDiscountType(salesOrderItem.getVipDiscountType());
	origiSalesOrderItem.setFoundVipPromotion(salesOrderItem.getFoundVipPromotion());	
	origiSalesOrderItem.setPromotionName(salesOrderItem.getPromotionName());
	origiSalesOrderItem.setDiscount(salesOrderItem.getDiscount());
	origiSalesOrderItem.setDiscountType(salesOrderItem.getDiscountType());
	origiSalesOrderItem.setFoundPromotion(salesOrderItem.getFoundPromotion());
	origiSalesOrderItem.setIsServiceItem(salesOrderItem.getIsServiceItem());
	origiSalesOrderItem.setIsComposeItem(salesOrderItem.getIsComposeItem());
	origiSalesOrderItem.setIsTax(salesOrderItem.getIsTax());
	origiSalesOrderItem.setTaxType(salesOrderItem.getTaxType());
	origiSalesOrderItem.setTaxRate(salesOrderItem.getTaxRate());
    }
    
    
    /**
     * 檢核銷貨明細資料(AJAX)
     * 
     * @param orderHead
     * @param conditionMap
     * @param programId
     * @param identification
     * @param errorMsgs
     */
    private void checkSOItems(SoSalesOrderHead orderHead, HashMap conditionMap, 
	    String programId, String identification, List errorMsgs) {
	
	String message = null;
	String tabName = "明細資料頁籤";
	try{
	    String vipPromotionCode = (String)conditionMap.get("vipPromotionCode");
	    List soSalesOrderItems = orderHead.getSoSalesOrderItems();
	    if(soSalesOrderItems != null && soSalesOrderItems.size() > 0){
		int intactRecordCount = 0;
	        for(int i = 0 ; i < soSalesOrderItems.size(); i++){
	            try{
	                SoSalesOrderItem salesOrderItem = (SoSalesOrderItem)soSalesOrderItems.get(i);
	                if(!"1".equals(salesOrderItem.getIsDeleteRecord())){
	                    salesOrderItem.setVipPromotionCode(vipPromotionCode);//依據head的vipPromotionCode將item的替換掉
	                    salesOrderItem.setWarehouseCode(orderHead.getDefaultWarehouseCode());//依據head的預設庫別將item的替換掉
	                    salesOrderItem.setTaxType(orderHead.getTaxType());//依據head的taxType將item的替換掉
	                    salesOrderItem.setTaxRate(orderHead.getTaxRate());//依據head的taxRate將item的替換掉	    
	                    String itemCode = salesOrderItem.getItemCode();
	                    String warehouseCode = salesOrderItem.getWarehouseCode();	    
	                    Double quantity = salesOrderItem.getQuantity();
	                    if(quantity != null && quantity != 0D){
		                quantity = CommonUtils.round(quantity, 2);
		                salesOrderItem.setQuantity(quantity);
	                    }	    	    
	                    String promotionCode = salesOrderItem.getPromotionCode();
	                    if(!StringUtils.hasText(itemCode)){
		                throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的品號！");
	                    }else if(!StringUtils.hasText(warehouseCode)){
		                throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的庫別！");
	                    }else if(!ValidateUtil.isEnglishAlphabetOrNumber(itemCode)){
		                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號必須為A~Z、a~z、0~9！");
	                    }else if(!ValidateUtil.isEnglishAlphabetOrNumber(warehouseCode)){
		                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的庫別必須為A~Z、a~z、0~9！");
	                    }
	    
	                    Double discountRate = salesOrderItem.getDiscountRate();
	                    if(discountRate == null){
		                discountRate = 100D;
		                salesOrderItem.setDiscountRate(discountRate);
	                    }
	                    Double deductionAmount = salesOrderItem.getDeductionAmount();
	                    Double originalUnitPrice = salesOrderItem.getOriginalUnitPrice();	    
	                    conditionMap.put("itemCode", itemCode);
	                    conditionMap.put("warehouseCode", warehouseCode);
	                    conditionMap.put("quantity", quantity);
	                    conditionMap.put("discountRate", discountRate);
	                    conditionMap.put("deductionAmount", deductionAmount);
	                    conditionMap.put("promotionCode", promotionCode);
	                    //conditionMap.put("vipPromotionCode", vipPromotionCode);依據head帶入的vipPromotionCode
	                    conditionMap.put("originalUnitPrice", originalUnitPrice);
	                    conditionMap.put("taxType", salesOrderItem.getTaxType());
	                    conditionMap.put("taxRate", salesOrderItem.getTaxRate());	    
	                    //查詢後的銷售明細
	                    SoSalesOrderItem soItemInfo = appGetSaleItemInfoService.getSaleItemInfo(conditionMap, setSoItemBean(conditionMap));    
	                    //copy查詢後的銷售明細
	        	    setSoItemInfo(salesOrderItem, soItemInfo);
	        	    salesOrderItem.setScheduleShipDate(orderHead.getScheduleShipDate()); // 將head的預計出貨日放置Item中
	        	    //refreshItemPriceRelationData(salesOrderItem);	                    
	                    if(soItemInfo.getItemCName() == null){
		                throw new NoSuchObjectException("查無" + tabName + "中第" + (i + 1) + "項明細的品號！");
	                    }else if(soItemInfo.getWarehouseName() == null){
		                throw new NoSuchObjectException("查無" + tabName + "中第" + (i + 1) + "項明細的庫別！");
	                    }else if("Y".equals(soItemInfo.getIsServiceItem()) && salesOrderItem.getOriginalUnitPrice() == null){
		                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號為服務性商品，請輸入單價！");
	                    }else if(StringUtils.hasText(soItemInfo.getPromotionCode()) && soItemInfo.getDiscountType() == null && soItemInfo.getDiscount() == null){
		                throw new ValidationErrorException("查無" + tabName + "中第" + (i + 1) + "項明細的活動代號！");
	                    }/*else if(StringUtils.hasText(soItemInfo.getVipPromotionCode()) && soItemInfo.getVipDiscountType() == null && soItemInfo.getVipDiscount() == null){
		                throw new ValidationErrorException("查無" + tabName + "中第" + (i + 1) + "項明細的VIP類別代號！");
	                    }*/
	    
	                    //數量不可以是零
	                    if(quantity == null || quantity == 0D){
		                throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的數量！");
	                    }else if(!POSTYPECODE.equals(orderHead.getOrderTypeCode()) && !"Y".equals(salesOrderItem.getIsServiceItem()) 
	                	    && quantity != null && salesOrderItem.getCurrentOnHandQty() < quantity){
		                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的數量不可大於庫存量！");
	                    }else if(!POSTYPECODE.equals(orderHead.getOrderTypeCode()) && !"Y".equals(salesOrderItem.getIsServiceItem()) 
	                	    && quantity != null && quantity < 0D){
		                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的數量不可小於零！");
	                    }else if("Y".equals(salesOrderItem.getIsServiceItem()) && quantity != null && quantity != 1D && quantity != -1D){
		                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號為服務性商品，數量請輸入1或-1！");
	                    }
	                    intactRecordCount++;
	                }
	            }catch(Exception ex){
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
		    log.error(message);
		}	        
	    }else{
	        message = tabName + "中至少需輸入一筆資料！";
	        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
            } 
	}catch(Exception ex){
	    message = "檢核" + tabName + "時發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, orderHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }
    
    /**
     * 合計所有Item的金額，包括原總銷售金額、總實際銷售金額、稅金總額
     * 
     * @param orderHead
     * @param conditionMap
     * @param event
     * @throws FormException
     * @throws Exception
     */
    public Map countTotalAmount(SoSalesOrderHead orderHead, HashMap conditionMap, String event) throws FormException, Exception {

	try{
	    HashMap returnMap = new HashMap();
	    List<SoSalesOrderItem> soSalesOrderItems = orderHead.getSoSalesOrderItems();
	    Double totalOriginalSalesAmount = 0D;
	    Double totalActualSalesAmount = 0D;
	    Double taxAmount = 0D;
	    Double actualTaxAmount = 0D;
	    Double totalItemQuantity = 0D;
	    if(soSalesOrderItems != null && soSalesOrderItems.size() > 0){
	        for (SoSalesOrderItem soSalesOrderItem : soSalesOrderItems) {
		    refreshItemPriceRelationData(soSalesOrderItem);
		    if(!AjaxUtils.IS_DELETE_RECORD_TRUE.equals(soSalesOrderItem.getIsDeleteRecord())){
	                totalOriginalSalesAmount += soSalesOrderItem.getOriginalSalesAmount();
	                totalActualSalesAmount += soSalesOrderItem.getActualSalesAmount();
	                taxAmount += soSalesOrderItem.getTaxAmount();
	                Double itemQuantity = soSalesOrderItem.getQuantity();
	                Double actualUnitPrice = soSalesOrderItem.getActualUnitPrice();
	                if(itemQuantity != null && actualUnitPrice != null){
	        	    totalItemQuantity += itemQuantity;
	                }
		    }
	        }
	    }
	    actualTaxAmount = calculateTaxAmount(orderHead.getTaxType(), orderHead.getTaxRate(), totalActualSalesAmount);
	    orderHead.setTotalOriginalSalesAmount(totalOriginalSalesAmount);
	    orderHead.setTotalActualSalesAmount(totalActualSalesAmount);
	    orderHead.setTaxAmount(actualTaxAmount);	    
	    returnMap.put("taxAmount", taxAmount);
	    returnMap.put("totalItemQuantity", totalItemQuantity);	    
	    
	    return returnMap;
	} catch (Exception ex) {
	    log.error("銷售單金額統計時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷售單金額統計時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 將有刪除註記的item移除，並合計所有Item的金額，包括原總銷售金額、總實際銷售金額、稅金總額
     * 
     * @param orderHead
     * @param conditionMap
     * @param event
     * @throws FormException
     * @throws Exception
     */
    public List<Properties> executeCountTotalAmount(Properties httpRequest) throws ValidationErrorException {

	try{
	    List<Properties> result = new ArrayList();
	    Properties properties = new Properties();
            //===================取得傳遞的的參數===================
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	    SoSalesOrderHead salesOrderHeadPO = findSoSalesOrderHeadById(headId);
	    if(salesOrderHeadPO == null){
                throw new ValidationErrorException("查無銷貨單主鍵：" + headId + "的資料！");
	    }
	    String formStatus = httpRequest.getProperty("formStatus");// 狀態
	    Double totalItemQuantity = 0D;
	    if(OrderStatus.SAVE.equals(formStatus) || OrderStatus.REJECT.equals(formStatus) || 
		    OrderStatus.UNCONFIRMED.equals(formStatus)){
	        String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
	        String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
	        String shopCode = httpRequest.getProperty("shopCode");// 品牌代號	    
	        String warehouseEmployee = httpRequest.getProperty("warehouseEmployee");
	        String warehouseManager = httpRequest.getProperty("warehouseManager");
	        String customerCode = httpRequest.getProperty("customerCode");
	        String searchCustomerType = httpRequest.getProperty("searchCustomerType");
	        String priceType = httpRequest.getProperty("priceType");
	        String salesDate = httpRequest.getProperty("salesDate");
	        salesDate = DateUtils.format(DateUtils.parseDate("yyyy/MM/dd", salesDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
	        //================取得customerType、vipType、vipPromotionCode======================	    
	        HashMap customerInfoMap = getCustomerInfo(brandCode, orderTypeCode, customerCode, searchCustomerType, null);
	        String customerType = (String)customerInfoMap.get("customerType");
	        String vipType = (String)customerInfoMap.get("vipType");
	        String vipPromotionCode = (String)customerInfoMap.get("vipPromotionCode");
	        //================================================================================
	        HashMap conditionMap = new HashMap();
	        conditionMap.put("brandCode", brandCode);	   
	        conditionMap.put("shopCode", shopCode);
	        conditionMap.put("warehouseEmployee", warehouseEmployee);
	        conditionMap.put("warehouseManager", warehouseManager);
	        conditionMap.put("customerType", customerType);
	        conditionMap.put("vipType", vipType);
	        conditionMap.put("vipPromotionCode", vipPromotionCode);
	        conditionMap.put("priceType", priceType);
	        conditionMap.put("salesDate", salesDate);
	        conditionMap.put("beforeChangeStatus", formStatus);	    
	    
		getItemPriceRelationData(salesOrderHeadPO, conditionMap, false);
		Map countResultMap  = countTotalAmount(salesOrderHeadPO, conditionMap, null);
		totalItemQuantity = (Double)countResultMap.get("totalItemQuantity");
		soSalesOrderHeadDAO.update(salesOrderHeadPO);
	    }else{
	        //=========================計算商品總數================
	        List<SoSalesOrderItem> salesOrderItems = salesOrderHeadPO.getSoSalesOrderItems();
	        if(salesOrderItems != null && salesOrderItems.size() > 0){
	            for(SoSalesOrderItem salesOrderItem : salesOrderItems){
	                if(!AjaxUtils.IS_DELETE_RECORD_TRUE.equals(salesOrderItem.getIsDeleteRecord())){
	                    Double itemQuantity = salesOrderItem.getQuantity();
	                    Double actualUnitPrice = salesOrderItem.getActualUnitPrice();
	                    if(itemQuantity != null && actualUnitPrice != null){
	        	        totalItemQuantity += itemQuantity;
	                    }
	                }
		    }
                }
	    }
	    //===================================================
	    Double totalOriginalSalesAmount = salesOrderHeadPO.getTotalOriginalSalesAmount();
	    Double totalActualSalesAmount = salesOrderHeadPO.getTotalActualSalesAmount();
	    Double taxAmount = salesOrderHeadPO.getTaxAmount();
	    Double totalDeductionAmount = 0D;
	    Double totalNoneTaxSalesAmount = 0D;
	    if(totalOriginalSalesAmount != null && totalActualSalesAmount != null){
		totalDeductionAmount = totalOriginalSalesAmount - totalActualSalesAmount;
	    }
	    if(totalActualSalesAmount != null){
		if(taxAmount == null){
		    totalNoneTaxSalesAmount = totalActualSalesAmount;
		}else{
		    totalNoneTaxSalesAmount = totalActualSalesAmount - taxAmount;
		}
	    }
	    	    
	    properties.setProperty("TotalOriginalSalesAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalOriginalSalesAmount, 0), "0.0"));
	    properties.setProperty("TotalDeductionAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalDeductionAmount, 0), "0.0"));
	    properties.setProperty("TaxAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(taxAmount, 0), "0.0"));
	    properties.setProperty("TotalOtherExpense", AjaxUtils.getPropertiesValue(salesOrderHeadPO.getTotalOtherExpense(), "0.0"));
	    properties.setProperty("TotalActualSalesAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalActualSalesAmount, 0), "0.0"));
	    properties.setProperty("TotalNoneTaxSalesAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalNoneTaxSalesAmount, 0), "0.0"));
	    properties.setProperty("TotalItemQuantity", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalItemQuantity, 0), "0.0"));	    
	    result.add(properties);
	    
	    return result;
	}catch (Exception ex) {
	    log.error("銷售單金額統計失敗，原因：" + ex.toString());
	    throw new ValidationErrorException("銷售單金額統計失敗！");
	} 
    }
    
    /**
     * 執行銷貨單merge
     * 
     * @param updateObj
     */
    public void executeMergeSalesOrder(SoSalesOrderHead updateObj) {
	soSalesOrderHeadDAO.merge(updateObj);
    }
    
    /**
     * 執行複製SoSalesOrderHead
     * 
     * @param salesOrderHead
     * @param loginUser
     * @return SoSalesOrderHead
     */
    public SoSalesOrderHead executeCopySalesOrder(SoSalesOrderHead salesOrderHead, String loginUser) 
        throws FormException, Exception{
	
	try{
	    SoSalesOrderHead salesOrderHeadPO = findSoSalesOrderHeadById(salesOrderHead.getHeadId());	
	    SoSalesOrderHead newSalesOrderHead = new SoSalesOrderHead();
	    BeanUtils.copyProperties(salesOrderHeadPO, newSalesOrderHead);
	    newSalesOrderHead.setHeadId(null);
	    newSalesOrderHead.setOrderNo(null);
	    newSalesOrderHead.setSalesOrderDate(DateUtils.parseDate(DateUtils.format(new Date())));
	    newSalesOrderHead.setSuperintendentCode(loginUser);
	    newSalesOrderHead.setScheduleCollectionDate(null);
	    newSalesOrderHead.setScheduleShipDate(DateUtils.parseDate(DateUtils.format(new Date())));
	    newSalesOrderHead.setCustomerPoNo(null);
	    newSalesOrderHead.setTransactionSeqNo(null);
	    newSalesOrderHead.setReserve5(null);
	    newSalesOrderHead.setCreatedBy(loginUser);
	    newSalesOrderHead.setCreationDate(new Date());
	    newSalesOrderHead.setLastUpdatedBy(loginUser);
	    newSalesOrderHead.setLastUpdateDate(new Date());
	    newSalesOrderHead.setStatus(OrderStatus.SAVE);
	
	    List<SoSalesOrderItem> salesOrderItems = salesOrderHeadPO.getSoSalesOrderItems();
	    List<SoSalesOrderPayment> salesOrderPayments = salesOrderHeadPO.getSoSalesOrderPayments();
	    List<SoSalesOrderItem> newSalesOrderItems = new ArrayList(0);
	    List<SoSalesOrderPayment> newSalesOrderPayments = new ArrayList(0);
	
	    for(SoSalesOrderItem salesOrderItem : salesOrderItems){
	        SoSalesOrderItem newSalesOrderItem = new SoSalesOrderItem();
	        BeanUtils.copyProperties(salesOrderItem, newSalesOrderItem);
	        newSalesOrderItem.setLineId(null);
	        newSalesOrderItem.setSoSalesOrderHead(null);
	        newSalesOrderItem.setCreatedBy(null);
	        newSalesOrderItem.setCreationDate(null);
	        newSalesOrderItem.setLastUpdatedBy(null);
	        newSalesOrderItem.setLastUpdateDate(null);
	        newSalesOrderItem.setStatus(null);
	        newSalesOrderItems.add(newSalesOrderItem);
	    }	
	    for(SoSalesOrderPayment salesOrderPayment : salesOrderPayments){
	        SoSalesOrderPayment newSalesOrderPayment = new SoSalesOrderPayment();
	        BeanUtils.copyProperties(salesOrderPayment, newSalesOrderPayment);
	        newSalesOrderPayment.setPosPaymentId(null);
	        newSalesOrderPayment.setSoSalesOrderHead(null);
	        newSalesOrderPayment.setCreatedBy(null);
	        newSalesOrderPayment.setCreationDate(null);
	        newSalesOrderPayment.setLastUpdatedBy(null);
	        newSalesOrderPayment.setLastUpdateDate(null);
	        newSalesOrderPayment.setStatus(null);   
	        newSalesOrderPayments.add(newSalesOrderPayment);
	    }	
	    newSalesOrderHead.setSoSalesOrderItems(newSalesOrderItems);
	    newSalesOrderHead.setSoSalesOrderPayments(newSalesOrderPayments);
	    saveTmp(newSalesOrderHead);
	    
	    return newSalesOrderHead;
	}catch (FormException fe) {
	    log.error("複製銷售單時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("複製銷售單時發生錯誤，原因：" + ex.toString());
	    throw new Exception("複製銷售單時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 重新計算Line的金額
     * 
     * @param orderHead
     * @param conditionMap
     * @throws Exception
     */
    private void getItemPriceRelationData(SoSalesOrderHead orderHead, HashMap conditionMap, boolean isReplace) throws Exception{

	String vipPromotionCode = (String)conditionMap.get("vipPromotionCode");
	String defaultPromotionCode = (String)conditionMap.get("promotionCode");
	String defaultWarehouseCode = (String)conditionMap.get("warehouseCode");
	String defaultTaxType = (String)conditionMap.get("taxType");   
	Double defaultTaxRate = (Double)conditionMap.get("taxRate");   
	Double defaultDiscountRate = (Double)conditionMap.get("discountRate");   
	
	List soSalesOrderItems = orderHead.getSoSalesOrderItems();
	if(soSalesOrderItems != null){
	    for(int i = 0 ; i < soSalesOrderItems.size(); i++){
	        SoSalesOrderItem salesOrderItem = (SoSalesOrderItem)soSalesOrderItems.get(i);
	        String itemCode = salesOrderItem.getItemCode();
	        String warehouseCode = salesOrderItem.getWarehouseCode();	    
	        Double quantity = salesOrderItem.getQuantity();
	        if(quantity != null && quantity != 0D){
		    quantity = CommonUtils.round(quantity, 2);
		    salesOrderItem.setQuantity(quantity);
	        }
	        salesOrderItem.setVipPromotionCode(vipPromotionCode);//依據head的vipPromotionCode將item的替換掉
	        String promotionCode = salesOrderItem.getPromotionCode();
	        //String vipPromotionCode = salesOrderItem.getVipPromotionCode();	    
	        Double discountRate = salesOrderItem.getDiscountRate();
	        if(discountRate == null){
	            discountRate = 100D;
	            salesOrderItem.setDiscountRate(discountRate);
	        }
	        Double deductionAmount = salesOrderItem.getDeductionAmount();
	        Double originalUnitPrice = salesOrderItem.getOriginalUnitPrice();
	        String taxType = salesOrderItem.getTaxType();
	        Double taxRate = salesOrderItem.getTaxRate();
	        conditionMap.put("itemCode", itemCode);
	        conditionMap.put("quantity", quantity);
	        conditionMap.put("deductionAmount", deductionAmount);
	        conditionMap.put("originalUnitPrice", originalUnitPrice);
	        if(isReplace){
	            salesOrderItem.setWarehouseCode(defaultWarehouseCode);
	            salesOrderItem.setDiscountRate(defaultDiscountRate);
	            salesOrderItem.setPromotionCode(defaultPromotionCode);
	            salesOrderItem.setTaxType(defaultTaxType);
	            salesOrderItem.setTaxRate(defaultTaxRate);            	            
	        }else{
	            conditionMap.put("warehouseCode", warehouseCode);        
	            conditionMap.put("discountRate", discountRate);	        
	            conditionMap.put("promotionCode", promotionCode);	        
	            conditionMap.put("taxType", taxType);
	            conditionMap.put("taxRate", taxRate);	            
	        }
	    
	        //查詢後的銷售明細
	        SoSalesOrderItem soItemInfo = appGetSaleItemInfoService.getSaleItemInfo(conditionMap, setSoItemBean(conditionMap));
	        //copy查詢後的銷售明細
	        setSoItemInfo(salesOrderItem, soItemInfo);
	    }
	}
    }
    
    /**
     * 取得客戶資料
     * 
     * @param brandCode
     * @param orderTypeCode
     * @param tmpCustomerCode
     * @param searchCustomerType
     * @param isEnable
     * @return HashMap
     */
    private HashMap getCustomerInfo(String brandCode, String orderTypeCode, String tmpCustomerCode, String searchCustomerType, String isEnable){
	
	HashMap result = new HashMap();
	if(StringUtils.hasText(tmpCustomerCode)){	    
	    tmpCustomerCode = tmpCustomerCode.trim().toUpperCase();    
            BuCustomerWithAddressView customerWithAddressView = buCustomerWithAddressViewDAO.findCustomerByType(brandCode, tmpCustomerCode, searchCustomerType, isEnable);
	    if(customerWithAddressView != null){
		result.put("actualCustomerCode", customerWithAddressView.getCustomerCode());
		result.put("customerType", customerWithAddressView.getCustomerTypeCode());
		result.put("vipType", customerWithAddressView.getVipTypeCode());
		if(!POSTYPECODE.equals(orderTypeCode)){
		    result.put("vipPromotionCode", customerWithAddressView.getPromotionCode());
		}
            }	    
	}
	return result;
    }
    
   
    /**
     * 將Head的VipPromotion、折扣率、稅別、稅額、預設庫別替換line的相關欄位
     * 
     * @param httpRequest
     * @return
     * @throws ValidationErrorException
     */
    public List<Properties> updateItemRelationData(Properties httpRequest) throws ValidationErrorException {

	try{
	    String formStatus = httpRequest.getProperty("formStatus");// 狀態
	    if(OrderStatus.SAVE.equals(formStatus) || OrderStatus.REJECT.equals(formStatus) 
		    || OrderStatus.UNCONFIRMED.equals(formStatus)){
	        Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	        SoSalesOrderHead salesOrderHeadPO = findSoSalesOrderHeadById(headId);
		if(salesOrderHeadPO == null){
	            throw new ValidationErrorException("查無銷貨單主鍵：" + headId + "的資料！");
		}
	        //======================帶入Head的值=========================
	        String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
	        String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
	        String shopCode = httpRequest.getProperty("shopCode");// 品牌代號
	        String defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode");// 預設倉別
	        String taxType = httpRequest.getProperty("taxType");// 稅別
	        String taxRate = httpRequest.getProperty("taxRate");// 稅率
	        String discountRate = httpRequest.getProperty("discountRate");// 折扣比率
	        String vipPromotionCode = httpRequest.getProperty("vipPromotionCode");//vip類別代號
	        String promotionCode = httpRequest.getProperty("promotionCode");//活動代號
	        String warehouseEmployee = httpRequest.getProperty("warehouseEmployee");
	        String warehouseManager = httpRequest.getProperty("warehouseManager");
	        String customerType = httpRequest.getProperty("customerType");
	        String vipType = httpRequest.getProperty("vipType");
	        String priceType = httpRequest.getProperty("priceType");
	        String salesDate = httpRequest.getProperty("salesDate");
	        salesDate = DateUtils.format(DateUtils.parseDate("yyyy/MM/dd", salesDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
	   
	        HashMap map = new HashMap();
	        map.put("brandCode", brandCode);
	        map.put("orderTypeCode", orderTypeCode);
	        map.put("shopCode", shopCode);
	        map.put("warehouseCode", defaultWarehouseCode);
	        map.put("taxType", taxType);
	        map.put("taxRate", taxRate);
	        map.put("discountRate", discountRate);
	        map.put("vipPromotionCode", vipPromotionCode);
	        map.put("promotionCode", promotionCode);
	        map.put("warehouseEmployee", warehouseEmployee);
	        map.put("warehouseManager", warehouseManager);
	        map.put("customerType", customerType);
	        map.put("vipType", vipType);
	        map.put("priceType", priceType);
	        map.put("salesDate", salesDate);	    
	        getDefaultArgument(map);		
		getItemPriceRelationData(salesOrderHeadPO, map, true);
		countTotalAmount(salesOrderHeadPO, map, null);
		soSalesOrderHeadDAO.update(salesOrderHeadPO);
	    }
	    
	    return AjaxUtils.getResponseMsg(null);
	}catch (Exception ex) {
	    log.error("更新銷貨明細相關欄位發生錯誤，原因：" + ex.toString());
	    throw new ValidationErrorException("更新銷貨明細相關欄位失敗！");
	} 
    }
    
    /**
     * item帶入head的預設值
     * 
     * @param parameterMap
     */
    private void getDefaultArgument(HashMap parameterMap){
	
        String taxType = (String)parameterMap.get("taxType");
	String discountRate = (String)parameterMap.get("discountRate");
	String promotionCode = (String)parameterMap.get("promotionCode");
	//taxType、taxRate預設值
	if("1".equals(taxType) || "2".equals(taxType)){
	    parameterMap.put("taxRate", 0D);
	}else if("3".equals(taxType)){
	    parameterMap.put("taxRate", 5D);
	}else{
            parameterMap.put("taxType", "3");
	    parameterMap.put("taxRate", 5D);
	}
	//折扣比率預設值
	try{
	    parameterMap.put("discountRate", Double.parseDouble(discountRate));
	}catch(NumberFormatException nfe){
	    parameterMap.put("discountRate", 100D);
	}
	//促銷活動
	if (StringUtils.hasText(promotionCode)) {
	    promotionCode = promotionCode.trim().toUpperCase();
	    parameterMap.put("promotionCode", promotionCode);
	}	
    }
    
    /**
     * 更新出貨單狀態及出貨資料(不跑流程)
     * 
     * @param deliveryHead
     * @param status
     * @throws Exception
     */
    private void modifyImDeliveryStatusAndShipData(ImDeliveryHead deliveryHead, String status) throws Exception{
	
	deliveryHead.setShipDate(deliveryHead.getScheduleShipDate());
	deliveryHead.setTotalOriginalShipAmount(deliveryHead.getTotalOriginalSalesAmount());
	deliveryHead.setTotalActualShipAmount(deliveryHead.getTotalActualSalesAmount());	
	deliveryHead.setShipTaxAmount(deliveryHead.getTaxAmount());
	deliveryHead.setStatus(status);
	deliveryHead.setLastUpdateDate(new Date());
	imDeliveryHeadDAO.update(deliveryHead);
        List deliveryLines = imDeliveryLineDAO.findByProperty("ImDeliveryLine", "salesOrderId", deliveryHead.getSalesOrderId());     
        if (deliveryLines != null && deliveryLines.size() > 0) {
            for (int i = 0; i < deliveryLines.size(); i++) {
        	ImDeliveryLine deliveryLine = (ImDeliveryLine)deliveryLines.get(i);
                deliveryLine.setShipQuantity(deliveryLine.getSalesQuantity());
		deliveryLine.setOriginalShipAmount(deliveryLine.getOriginalSalesAmount());
		deliveryLine.setActualShipAmount(deliveryLine.getActualSalesAmount());
		deliveryLine.setShipTaxAmount(deliveryLine.getTaxAmount());		
		//deliveryLine.setStatus(status);
	        //deliveryLine.setLastUpdateDate(new Date());
		imDeliveryLineDAO.update(deliveryLine);
	    }
	}else {
	    throw new NoSuchDataException("依據銷貨單主檔主鍵："
		    + deliveryHead.getSalesOrderId() + "查無相關出貨單明細檔資料！");
	}
    }
    
    /**
     * 銷貨單轉出貨單(不跑流程)
     * 
     * @param soHeadId
     * @throws Exception
     */
    public void executeConvertToDelivery(Long soHeadId) throws Exception{
	
	try{
	    SoSalesOrderHead salesOrderHeadPO = findSoSalesOrderHeadById(soHeadId);
	    if(salesOrderHeadPO == null){
	        throw new ValidationErrorException("查無銷貨單主鍵：" + soHeadId + "的資料！");
	    }
	    ImDeliveryHead deliveryHead = imDeliveryService.produceDeliveryHeadAndSetRelation(soHeadId);
	    salesOrderHeadPO.setStatus(OrderStatus.FINISH);
	    soSalesOrderHeadDAO.update(salesOrderHeadPO);
	    modifyImDeliveryStatusAndShipData(deliveryHead, OrderStatus.FINISH);
	}catch(Exception ex){
	    log.error("銷售單轉出貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷售單轉出貨單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    public Long executeSoReverse(String organizationCode, String brandCode, String orderTypeCode, String orderNo, String loginUser) throws FormException, Exception{
	
	try{
	    if(StringUtils.hasText(orderTypeCode)){
	        orderTypeCode = orderTypeCode.trim().toUpperCase();
	    }else{
		throw new ValidationErrorException("請輸入單別！");
	    }
	    if(StringUtils.hasText(orderNo)){
	        orderNo = orderNo.trim();
	    }else{
		throw new ValidationErrorException("請輸入單號！");
	    }
	    SoSalesOrderHead salesOrderHeadPO = soSalesOrderHeadDAO.findSalesOrderByIdentification(brandCode, orderTypeCode, orderNo);
	    if(salesOrderHeadPO == null){
		throw new NoSuchObjectException("查無品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "的銷貨資料！");
	    }
	    String identityCode = MessageStatus.getIdentificationMsg(brandCode, orderTypeCode, orderNo);
	    String status = salesOrderHeadPO.getStatus();
	    //===================================檢核狀態是否可執行反確認==========================================
	    if(status == null){
		throw new ValidationErrorException(identityCode + "的狀態為空值，無法執行反確認！");
	    }else if(OrderStatus.SAVE.equalsIgnoreCase(status) || OrderStatus.REJECT.equalsIgnoreCase(status) 
		    || OrderStatus.UNCONFIRMED.equalsIgnoreCase(status)){
		throw new ValidationErrorException(identityCode + "的狀態為" + OrderStatus.getChineseWord(status) + "，無法執行反確認！");
	    }
	    //===================================檢核銷貨日期是否大於日關帳日期=====================================
	    String dateType = "銷貨日期";
	    ValidateUtil.isAfterClose(brandCode, orderTypeCode, dateType, salesOrderHeadPO.getSalesOrderDate(),salesOrderHeadPO.getSchedule());
	    //================================================================================================	    
	    List imDeliveryHeads = imDeliveryHeadDAO.findByProperty(
			"ImDeliveryHead", "salesOrderId", salesOrderHeadPO.getHeadId());
	    if(imDeliveryHeads != null && imDeliveryHeads.size() > 0){
		if(imDeliveryHeads.size() > 1){
		    throw new ValidationErrorException("銷貨單主鍵：" + salesOrderHeadPO.getHeadId() + "對應到" + imDeliveryHeads.size() + "筆出貨單號！");
		}
		ImDeliveryHead deliveryHeadPO = (ImDeliveryHead)imDeliveryHeads.get(0);
		String delievryIdentityCode = MessageStatus.getIdentificationMsg(deliveryHeadPO.getBrandCode(), deliveryHeadPO.getOrderTypeCode(), deliveryHeadPO.getOrderNo());
		String deliveryStatus = deliveryHeadPO.getStatus();
		if(deliveryStatus == null){
		    throw new ValidationErrorException(identityCode + "對應的出貨單：" + delievryIdentityCode + "其狀態為空值，無法執行反確認！");
		}
		List<ImDeliveryLine> deliveryLines = deliveryHeadPO.getImDeliveryLines();
		//===============================刪除交易明細檔==================================================
		List<ImTransation> imTransations = imTransationDAO.findTransationByIdentification(deliveryHeadPO.getBrandCode(), 
			deliveryHeadPO.getOrderTypeCode(), deliveryHeadPO.getOrderNo());
		if(imTransations != null && imTransations.size() > 0){
		    for(ImTransation imTransation : imTransations){
			imTransationDAO.delete(imTransation);
		    }
		}
		//=================================補回庫存=====================================================
		if(OrderStatus.SIGNING.equalsIgnoreCase(deliveryStatus) || OrderStatus.FINISH.equalsIgnoreCase(deliveryStatus) 
			|| OrderStatus.CLOSE.equalsIgnoreCase(deliveryStatus)){
		    revertOnHandByQuantity(deliveryLines, organizationCode, loginUser, true);	    
		}else{
		    revertOnHandByQuantity(deliveryLines, organizationCode, loginUser, false);
		}
		imDeliveryHeadDAO.delete(deliveryHeadPO);
	    }else{
		if(OrderStatus.SIGNING.equals(status)){
		    revertToOriginallyAvailableQuantity(salesOrderHeadPO.getHeadId(), organizationCode, loginUser);
		}
	    }
	    revertToSoInitially(salesOrderHeadPO, loginUser);
	    soSalesOrderHeadDAO.update(salesOrderHeadPO);
	    
	    return salesOrderHeadPO.getHeadId();
	}catch (FormException fe) {
	    log.error("銷售單反確認時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	}catch (Exception ex) {
	    log.error("銷售單反確認時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷售單反確認時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 將出貨單的實際出貨數量或銷貨數量補回庫存
     * 
     * @param deliveryLines
     * @param organizationCode
     * @param loginUser
     * @throws FormException
     */
    private void revertOnHandByQuantity(List<ImDeliveryLine> deliveryLines, String organizationCode, 
	    String loginUser, boolean isStandOnShipQuantity) throws FormException{
	
	if(deliveryLines != null && deliveryLines.size() > 0){
	    for(ImDeliveryLine deliveryLine : deliveryLines){
	        //非服務性商品補回庫存
		if(!"Y".equals(deliveryLine.getIsServiceItem())){
		    if(isStandOnShipQuantity){
		        imOnHandDAO.updateOutUncommitQuantity(organizationCode, deliveryLine.getItemCode()
			    , deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), 
			    deliveryLine.getShipQuantity(), loginUser);
		    }else{
			 imOnHandDAO.updateOutUncommitQuantity(organizationCode, deliveryLine.getItemCode()
			    , deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), 
		            deliveryLine.getSalesQuantity(), loginUser);
		    }
		}
	    }		
	}	
    }
    
    private void revertToSoInitially(SoSalesOrderHead salesOrderHead, String loginUser){
	
        salesOrderHead.setStatus(OrderStatus.SAVE);
        salesOrderHead.setLastUpdatedBy(loginUser);
        salesOrderHead.setLastUpdateDate(new Date());
        List<SoSalesOrderItem> salesOrderItems = salesOrderHead.getSoSalesOrderItems();
        if(salesOrderItems != null && salesOrderItems.size() > 0){
            for(SoSalesOrderItem salesOrderItem : salesOrderItems){
        	salesOrderItem.setDeliveryId(null);
        	salesOrderItem.setShippedDate(null);
        	salesOrderItem.setStatus(null);
        	salesOrderItem.setLastUpdatedBy(null);
        	salesOrderItem.setLastUpdateDate(null);
            }
        }     
    }
    
    private void sortSalesOrderItem(SoSalesOrderHead salesOrderHead) throws Exception{
	
        List<SoSalesOrderItem> salesOrderItems = soSalesOrderItemDAO.findByHeadId(salesOrderHead.getHeadId());
	salesOrderItems = StringTools.setBeanValue(salesOrderItems, "indexNo", null);
        salesOrderHead.setSoSalesOrderItems(salesOrderItems);
    }
    
    /**
     * 執行銷貨單初始化
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
        
        HashMap resultMap = new HashMap();
        try{
    	    //Object otherBean = parameterMap.get("vatBeanOther");
    	    //String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
            //Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
	    Map multiList = new HashMap(0);
	    List<BuCommonPhraseLine> allPaymentType = buCommonPhraseService.getCommonPhraseLinesById("PaymentType", false);
	    multiList.put("allPaymentType" , AjaxUtils.produceSelectorData(allPaymentType  ,"lineCode" ,"name",  false,  true));		
	    resultMap.put("multiList",multiList);
		
	    return resultMap;       	
        }catch (Exception ex) {
	    log.error("銷貨單初始化失敗，原因：" + ex.toString());
	    throw new Exception("銷貨單初始化失敗，原因：" + ex.toString());
	}           
    }
    
    /**
     * 載入頁面顯示付款資料明細
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getAJAXPageDataForPayment(Properties httpRequest) throws Exception{
        
        try{
            List<Properties> result = new ArrayList();
 	    List<Properties> gridDatas = new ArrayList();
 	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
 	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
 	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
 	    //======================帶入Head的值=========================
 	    String formStatus = httpRequest.getProperty("formStatus");// 狀態
 	    HashMap map = new HashMap();
	    map.put("formStatus", formStatus);
 	    //======================取得頁面所需資料===========================
 	    List<SoSalesOrderPayment> salesOrderPayments = soSalesOrderPaymentDAO.findPageLine(headId, iSPage, iPSize);
 	    if(salesOrderPayments != null && salesOrderPayments.size() > 0){
 	        // 取得第一筆的INDEX
		Long firstIndex = salesOrderPayments.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		Long maxIndex = soSalesOrderPaymentDAO.findPageLineMaxIndex(headId);
		result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES_PAYMENT, GRID_FIELD_DEFAULT_VALUES_PAYMENT, salesOrderPayments, gridDatas,
	            firstIndex, maxIndex));	
 	    }else{
 	        result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES_PAYMENT, GRID_FIELD_DEFAULT_VALUES_PAYMENT, gridDatas));
 	    }
 	        
 	    return result;
        }catch(Exception ex){
	    log.error("載入頁面顯示的付款資料明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的付款資料明細失敗！");
	}
    }
    
    /**
     * 更新PAYMENT PAGE的LINE
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> updateAJAXPaymentPageLinesData(Properties httpRequest) throws Exception{
	
        try{
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String status = httpRequest.getProperty("status");
	    String errorMsg = null;
	    
	    if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status) || OrderStatus.UNCONFIRMED.equals(status)) {
		SoSalesOrderHead salesOrderHead = new SoSalesOrderHead();
		salesOrderHead.setHeadId(headId);
		// 將STRING資料轉成List Properties record data
		List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES_PAYMENT);
		// Get INDEX NO
		int indexNo = soSalesOrderPaymentDAO.findPageLineMaxIndex(headId).intValue();
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
		        // 先載入HEAD_ID OR LINE DATA
			Long posPaymentId = NumberUtils.getLong(upRecord.getProperty("posPaymentId"));
			String posPaymentType = upRecord.getProperty(GRID_FIELD_NAMES_PAYMENT[1]);
			if (StringUtils.hasText(posPaymentType)) {
			    SoSalesOrderPayment salesOrderPaymentPO = soSalesOrderPaymentDAO.findPaymentByIdentification(salesOrderHead.getHeadId(), posPaymentId);
			    if(salesOrderPaymentPO != null){
				AjaxUtils.setPojoProperties(salesOrderPaymentPO, upRecord, GRID_FIELD_NAMES_PAYMENT, GRID_FIELD_TYPES_PAYMENT);
				soSalesOrderPaymentDAO.update(salesOrderPaymentPO);
			    }else{
				indexNo++;
				SoSalesOrderPayment salesOrderPayment = new SoSalesOrderPayment();
				AjaxUtils.setPojoProperties(salesOrderPayment, upRecord, GRID_FIELD_NAMES_PAYMENT, GRID_FIELD_TYPES_PAYMENT);
				salesOrderPayment.setIndexNo(Long.valueOf(indexNo));
				salesOrderPayment.setSoSalesOrderHead(salesOrderHead);				
				soSalesOrderPaymentDAO.save(salesOrderPayment);
			    }
			}
		    }
		}
	    }
	    
	    return AjaxUtils.getResponseMsg(errorMsg);
        }catch(Exception ex){
            log.error("更新銷貨付款資料明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新銷貨付款資料失敗！"); 
        }	
    }
    
    
    /**
     * 匯入銷貨商品明細
     * 
     * @param headId
     * @param vipPromotionCode
     * @param taxType
     * @param taxRate
     * @param salesOrderItems
     * @throws Exception
     */
    public void executeImportItems(Long headId, String vipPromotionCode, String taxType, Double taxRate, List salesOrderItems) throws Exception{
        
        try{
    	    SoSalesOrderHead salesOrderHead = findSoSalesOrderHeadById(headId); 
    	    if(salesOrderHead == null){
	        throw new NoSuchObjectException("查無銷貨單主鍵：" + headId + "的資料");
	    }   
	         
	    if(salesOrderItems != null && salesOrderItems.size() > 0){
	        for(int i = 0; i < salesOrderItems.size(); i++){
	            SoSalesOrderItem salesOrderItem = (SoSalesOrderItem)salesOrderItems.get(i);
	            if(StringUtils.hasText(vipPromotionCode)){
	                salesOrderItem.setVipPromotionCode(vipPromotionCode);
	            }
	            if(StringUtils.hasText(taxType)){
	                salesOrderItem.setTaxType(taxType);
	            }
	            if(taxRate != null){
	                salesOrderItem.setTaxRate(taxRate);
	            }   
	        }
	        salesOrderHead.setSoSalesOrderItems(salesOrderItems);  	                
	    }else{
	        salesOrderHead.setSoSalesOrderItems(new ArrayList(0));   	        
	    }
    	    soSalesOrderHeadDAO.update(salesOrderHead);
        }catch (Exception ex) {
	    log.error("銷貨商品明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷貨商品明細匯入時發生錯誤，原因：" + ex.getMessage());
	}        
    }
    
    /**
     * 將銷貨資料更新至銷貨單主檔及明細檔(AJAX)
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updateAJAXSalesOrder(Map parameterMap) throws FormException, Exception {
	
	HashMap resultMap = new HashMap();
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Long headId = getSalesOrderHeadId(formLinkBean);
            SoSalesOrderHead salesOrderHeadPO = getActualSalesOrder(headId);  	    
    	    //====================取得條件資料======================
            HashMap conditionMap = getConditionData(parameterMap);
	    String formStatus = (String)conditionMap.get("formStatus");
	    String employeeCode = (String)conditionMap.get("employeeCode");
	    String organizationCode = (String)conditionMap.get("organizationCode");           
	    if(OrderStatus.SAVE.equals(formStatus)){
		//刪除於SI_PROGRAM_LOG的原識別碼資料
	    	String identification = MessageStatus.getIdentification(salesOrderHeadPO.getBrandCode(), 
	    	        salesOrderHeadPO.getOrderTypeCode(), salesOrderHeadPO.getOrderNo());
	    	siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);		
		sortSalesOrderItem(salesOrderHeadPO);
    	        String brandCode = (String)conditionMap.get("brandCode");
    	        String orderTypeCode = (String)conditionMap.get("orderTypeCode");
    	        String customerCode_var = (String)conditionMap.get("customerCode_var");
	        String searchCustomerType = (String)conditionMap.get("searchCustomerType");	    	    
                //================取得customerType、vipType、vipPromotionCode======================
	        HashMap customerInfoMap = getCustomerInfo(brandCode, orderTypeCode, customerCode_var, searchCustomerType, null);
	        String actualCustomerCode = (String)customerInfoMap.get("actualCustomerCode");
	        salesOrderHeadPO.setCustomerCode(actualCustomerCode);
	        AjaxUtils.copyJSONBeantoPojoBean(formBindBean, salesOrderHeadPO); 
	    }
	    salesOrderHeadPO.setStatus(formStatus);
	    String resultMsg = modifyAjaxSoSalesOrder(salesOrderHeadPO, employeeCode);	    
	    if (OrderStatus.REJECT.equals(formStatus)) {
		revertToOriginallyAvailableQuantity(salesOrderHeadPO
			.getHeadId(), organizationCode, employeeCode);
	    }
	    resultMap.put("entityBean", salesOrderHeadPO);
    	    resultMap.put("resultMsg", resultMsg);
    	    
            return resultMap;
        }catch (FormException fe) {
	    log.error("銷貨單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷貨單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    private SoSalesOrderHead getActualSalesOrder(Long headId) throws FormException, Exception{
       
	SoSalesOrderHead salesOrderHeadPO  = findSoSalesOrderHeadById(headId);
    	if(salesOrderHeadPO == null){
    	    throw new NoSuchObjectException("查無銷貨單主鍵：" + headId + "的資料！");
    	}
	return salesOrderHeadPO;
    }
    
    private HashMap getConditionData(Map parameterMap) throws FormException, Exception{
        
	Object formBindBean = parameterMap.get("vatBeanFormBind");
	Object formLinkBean = parameterMap.get("vatBeanFormLink");
	Object otherBean = parameterMap.get("vatBeanOther");
	HashMap conditionMap = new HashMap();
	//取出參數	
 	String shopCode = (String)PropertyUtils.getProperty(formBindBean, "shopCode");
 	String salesDate = (String)PropertyUtils.getProperty(formBindBean, "salesOrderDate");
 	salesDate = DateUtils.format(DateUtils.parseDate("yyyy/MM/dd", salesDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
 	
 	String brandCode = (String)PropertyUtils.getProperty(formLinkBean, "brandCode");
	String orderTypeCode = (String)PropertyUtils.getProperty(formLinkBean, "orderTypeCode");
	String customerCode_var = (String)PropertyUtils.getProperty(formLinkBean, "customerCode_var");
	String searchCustomerType = (String)PropertyUtils.getProperty(formLinkBean, "searchCustomerType");
	String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
	String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
	String priceType = (String)PropertyUtils.getProperty(otherBean, "priceType");
 	String warehouseEmployee = (String)PropertyUtils.getProperty(otherBean, "warehouseEmployee");
 	String warehouseManager = (String)PropertyUtils.getProperty(otherBean, "warehouseManager");
 	String organizationCode = (String)PropertyUtils.getProperty(otherBean, "organizationCode");
 	
 	conditionMap.put("shopCode", shopCode);
 	conditionMap.put("salesDate", salesDate);
 	conditionMap.put("brandCode", brandCode);
 	conditionMap.put("orderTypeCode", orderTypeCode);
 	conditionMap.put("customerCode_var", customerCode_var);
 	conditionMap.put("searchCustomerType", searchCustomerType);
 	conditionMap.put("beforeChangeStatus", beforeChangeStatus);
 	conditionMap.put("formStatus", formStatus);
 	conditionMap.put("employeeCode", employeeCode);
 	conditionMap.put("priceType", priceType);
 	conditionMap.put("warehouseEmployee", warehouseEmployee);
 	conditionMap.put("warehouseManager", warehouseManager);
 	conditionMap.put("organizationCode", organizationCode);
 	
 	/*System.out.println("[brandCode]=" + brandCode);
 	System.out.println("[orderTypeCode]=" + orderTypeCode);
 	System.out.println("[customerCode_var]=" + customerCode_var);
 	System.out.println("[searchCustomerType]=" +searchCustomerType);
 	System.out.println("[beforeChangeStatus]=" + beforeChangeStatus);
 	System.out.println("[formStatus]=" + formStatus);
 	System.out.println("[employeeCode]=" + employeeCode);
 	System.out.println("[priceType]=" + priceType);
 	System.out.println("[warehouseEmployee]=" + warehouseEmployee);
 	System.out.println("[warehouseManager]=" + warehouseManager);
 	System.out.println("[organizationCode]=" + organizationCode);*/
 	
 	return conditionMap;
    }
    
    /**
     * 更新銷售單主單及明細檔、預訂可用庫存、產生出貨單明細檔
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updateAJAXSOToDelivery(Map parameterMap) throws FormException, Exception {
	
	HashMap resultMap = new HashMap();
        try{
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Long headId = getSalesOrderHeadId(formLinkBean);          
            SoSalesOrderHead salesOrderHeadPO = getActualSalesOrder(headId);
            //============remove delete mark record=============
            removeAJAXLine(salesOrderHeadPO);
    	    //====================取得條件資料======================
            HashMap conditionMap = getConditionData(parameterMap);
	    String employeeCode = (String)conditionMap.get("employeeCode");
	    String organizationCode = (String)conditionMap.get("organizationCode");		
	    sortSalesOrderItem(salesOrderHeadPO);
	    List<SoSalesOrderItem> salesOrderItems = salesOrderHeadPO.getSoSalesOrderItems();
	    
	    Map countResultMap = countTotalAmount(salesOrderHeadPO, conditionMap, null);
	    Double lineTaxAmount = (Double)countResultMap.get("taxAmount");
	    Double actualTaxAmount = salesOrderHeadPO.getTaxAmount();
	    //將差額補到最後一筆明細的稅額
	    Double balanceAmt = actualTaxAmount - lineTaxAmount;
	    SoSalesOrderItem salesOrderItem = salesOrderItems.get(salesOrderItems.size() - 1);
	    Double lastItemTaxAmt = salesOrderItem.getTaxAmount();
	    if(lastItemTaxAmt == null){
	        lastItemTaxAmt = 0D;
            }
	    salesOrderItem.setTaxAmount(lastItemTaxAmt + balanceAmt);	    
	    salesOrderHeadPO.setStatus(OrderStatus.SIGNING);
	    String resultMsg = modifyAjaxSoSalesOrder(salesOrderHeadPO, employeeCode);
	    //===================扣庫存、轉出貨單========================================
	    Object[] objArray = bookAvailableQuantity(salesOrderHeadPO, organizationCode, employeeCode);
	    salesOrderToDelivery(salesOrderHeadPO, salesOrderHeadPO.getSoSalesOrderItems(), 
		    (HashMap)objArray[1], (String)objArray[0], employeeCode);	    
	    if(POSTYPECODE.equals(salesOrderHeadPO.getOrderTypeCode())){
	        mailToPOSAdministrator(salesOrderHeadPO, (List)objArray[2]);
	    }	    
	    resultMap.put("entityBean", salesOrderHeadPO);
    	    resultMap.put("resultMsg", resultMsg);
    	    
            return resultMap;
        }catch (FormException fe) {
	    log.error("銷貨單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷貨單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * SOP執行反確認
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeAJAXAntiConfirm(Map parameterMap) throws FormException, Exception {
	
        HashMap resultMap = new HashMap();
        try{
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Long headId = getSalesOrderHeadId(formLinkBean);
            SoSalesOrderHead salesOrderHeadPO = getActualSalesOrder(headId);
            //====================取得條件資料======================
            HashMap conditionMap = getConditionData(parameterMap);
	    String employeeCode = (String)conditionMap.get("employeeCode");
	    String organizationCode = (String)conditionMap.get("organizationCode"); 
	    if(!POSTYPECODE.equals(salesOrderHeadPO.getOrderTypeCode())){
		throw new ValidationErrorException("單別：" + salesOrderHeadPO.getOrderTypeCode() + "不可執行反確認！");
	    }else if(!OrderStatus.SIGNING.equals(salesOrderHeadPO.getStatus())){
		throw new ValidationErrorException("狀態：" + OrderStatus.getChineseWord(salesOrderHeadPO.getStatus()) + "不可執行反確認！");
	    }
	    revertToOriginallyAvailableQuantity(salesOrderHeadPO.getHeadId(), organizationCode, employeeCode);
	    salesOrderHeadPO.setStatus(OrderStatus.UNCONFIRMED);
            modifySoSalesOrder(salesOrderHeadPO, employeeCode);
            String resultMsg = salesOrderHeadPO.getOrderTypeCode() + "-" + salesOrderHeadPO.getOrderNo() + "執行反確認成功！";	    
            resultMap.put("entityBean", salesOrderHeadPO);
    	    resultMap.put("resultMsg", resultMsg);
    	    
            return resultMap;
        }catch (FormException fe) {
            log.error("執行反確認失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("執行反確認時發生錯誤，原因：" + ex.toString());
	    throw new Exception("執行反確認時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    public static Object[] startProcess(SoSalesOrderHead form) throws ProcessFailedException{       
        
        try{           
	    String packageId = "So_SalesOrder";         
	    String processId = "SalesOrder";           
	    String version = "20090723";
	    String sourceReferenceType = "SoSalesOrder (1)";
	    HashMap context = new HashMap();	    
	    context.put("formId", form.getHeadId());
	    return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
	}catch (Exception ex){
	    log.error("銷貨流程啟動失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("銷貨流程啟動失敗！");
	}	      
    }
    
    public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws ProcessFailedException{
	   
        try{           
	    HashMap context = new HashMap();
	    context.put("approveResult", approveResult);
	       
	    return ProcessHandling.completeAssignment(assignmentId, context);
	}catch (Exception ex){
	    log.error("完成銷貨工作任務失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("完成銷貨工作任務失敗！");
	}
    }
    
    public Long getSalesOrderHeadId(Object bean) throws FormException, Exception{
	   
	Long headId = null;
	String id = (String)PropertyUtils.getProperty(bean, "headId");
        System.out.println("headId=" + id);
	if(StringUtils.hasText(id)){
            headId = NumberUtils.getLong(id);
        }else{
    	    throw new ValidationErrorException("傳入的銷貨單主鍵為空值！");
        }
	   
	return headId;
    }
    
   
    /**
     * 更新檢核後的銷貨單資料
     * 
     * @param salesOrderHead
     * @param conditionMap
     * @param programId
     * @param identification
     * @return List
     * @throws ValidationErrorException
     */
    public List updateCheckedSalesOrderData(Map parameterMap) throws Exception {
        
	List errorMsgs = new ArrayList(0);
	try{
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Long headId = getSalesOrderHeadId(formLinkBean);          
            SoSalesOrderHead salesOrderHeadPO = getActualSalesOrder(headId);
            String identification = MessageStatus.getIdentification(salesOrderHeadPO.getBrandCode(), 
        	    salesOrderHeadPO.getOrderTypeCode(), salesOrderHeadPO.getOrderNo());
            HashMap conditionMap = getConditionData(parameterMap); 
            String employeeCode = (String)conditionMap.get("employeeCode");
            
            siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
	    checkSalesOrderHead(salesOrderHeadPO, conditionMap, PROGRAM_ID, identification, errorMsgs);
	    checkSOItems(salesOrderHeadPO, conditionMap, PROGRAM_ID, identification, errorMsgs);
	    checkSalesOrderPayments(salesOrderHeadPO, conditionMap, PROGRAM_ID, identification, errorMsgs);
	    
	    modifySoSalesOrder(salesOrderHeadPO, employeeCode);
	    
	    return errorMsgs;
	}catch (Exception ex) {
	    log.error("銷貨單檢核後存檔失敗，原因：" + ex.toString());
	    throw new Exception("銷貨單檢核後存檔失敗，原因：" + ex.getMessage());
	}
    }
    
    
    /**
     *  取單號後更新銷貨單主檔
     * 
     * @param parameterMap
     * @return Map
     * @throws FormException
     * @throws Exception
     */
    public Map updateSalesOrderWithActualOrderNO(Map parameterMap) throws FormException, Exception {
        
        HashMap resultMap = new HashMap();
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
    	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    Long headId = getSalesOrderHeadId(formLinkBean); 
    	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
    	    //取得欲更新的bean
    	    SoSalesOrderHead salesOrderHeadPO = getActualSalesOrder(headId);
    	    //刪除於SI_PROGRAM_LOG的原識別碼資料
    	    String identification = MessageStatus.getIdentification(salesOrderHeadPO.getBrandCode(), 
    	            salesOrderHeadPO.getOrderTypeCode(), salesOrderHeadPO.getOrderNo());
    	    siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, salesOrderHeadPO);	    
    	    String resultMsg = modifyAjaxSoSalesOrder(salesOrderHeadPO, employeeCode);
    	    resultMap.put("entityBean", salesOrderHeadPO);
            resultMap.put("resultMsg", resultMsg);
    	
    	    return resultMap;      
        } catch (FormException fe) {
	    log.error("銷貨單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷貨單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    public String getIdentification(Long headId) throws Exception{
	   
        String id = null;
	try{
	    SoSalesOrderHead salesOrderHead = findSoSalesOrderHeadById(headId);
	    if(salesOrderHead != null){
                id = MessageStatus.getIdentification(salesOrderHead.getBrandCode(), 
		        salesOrderHead.getOrderTypeCode(), salesOrderHead.getOrderNo());
	    }else{
	        throw new NoSuchDataException("銷貨單主檔查無主鍵：" + headId + "的資料！");
	    }
	    
	    return id;
	}catch(Exception ex){
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
	}	   
    }
    
    /**
     *  更新銷貨單主檔
     * 
     * @param parameterMap
     * @throws FormException
     * @throws Exception
     */
    public void updateSalesOrder(Map parameterMap) throws FormException, Exception {
        
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
    	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    Long headId = getSalesOrderHeadId(formLinkBean); 
    	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
    	    //取得欲更新的bean
    	    SoSalesOrderHead salesOrderHeadPO = getActualSalesOrder(headId);
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, salesOrderHeadPO);
	    modifySoSalesOrder(salesOrderHeadPO, employeeCode);    
        } catch (FormException fe) {
	    log.error("銷貨單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷貨單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    public void setBuShopMachineService(BuShopMachineService buShopMachineService) {
        this.buShopMachineService = buShopMachineService;
    }
}
