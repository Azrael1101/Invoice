package tw.com.tm.erp.hbm.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.InsufficientQuantityException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuCustomerWithAddressView;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuPaymentTerm;
import tw.com.tm.erp.hbm.bean.BuPaymentTermId;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImTransation;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.dao.BuCountryDAO;
import tw.com.tm.erp.hbm.dao.BuCurrencyDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeApprovalDAO;
import tw.com.tm.erp.hbm.dao.BuPaymentTermDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.ImTransationDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderItemDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class ImDeliveryService {

    private static final Log log = LogFactory.getLog(ImDeliveryService.class);
    
    public static final String PROGRAM_ID_RETURN= "SO_RETURN";
    
    private static final int INVOICE_LOCUS_LENGTH = 2;
    
    private static final String ORGANIZATION_CODE = "TM";
    
    private BuCustomerWithAddressViewDAO buCustomerWithAddressViewDAO;
    
    private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO ;

    private ImDeliveryHeadDAO imDeliveryHeadDAO;

    private ImDeliveryLineDAO imDeliveryLineDAO;
    
    private ImItemDAO imItemDAO;
    
    private ImItemPriceDAO imItemPriceDAO;

    private ImOnHandDAO imOnHandDAO;
    
    private ImPromotionDAO imPromotionDAO;
    
    private ImTransationDAO imTransationDAO;
    
    private ImWarehouseDAO imWarehouseDAO;

    private SoSalesOrderHeadDAO soSalesOrderHeadDAO;

    private SoSalesOrderItemDAO soSalesOrderItemDAO;

    private BuOrderTypeService buOrderTypeService;
    
    private BuEmployeeDAO buEmployeeDAO ;
    
    private BuOrderTypeApprovalDAO buOrderTypeApprovalDAO;
    
    private BuShopDAO buShopDAO;
    
    private BuCountryDAO buCountryDAO;
    
    private BuPaymentTermDAO buPaymentTermDAO;
    
    private BuCurrencyDAO buCurrencyDAO;
    
    private ImDeliveryLineService imDeliveryLineService;
    
    private SiProgramLogAction siProgramLogAction;
    
    public static final String[] GRID_FIELD_NAMES = { 
    "indexNo", "itemCode", "itemCName", "warehouseCode", "warehouseName", 
    "originalUnitPrice", "actualUnitPrice", "salesQuantity", "shipQuantity", "originalShipAmount",
    "actualShipAmount", "importDeclNo", "importDeclDate", "importDeclType", "importDeclSeq",
    "lotNo", "lineId", "isLockRecord", "message"};
    
    public static final int[] GRID_FIELD_TYPES = { 
    AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
    AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, 
    AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};
    
    public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
    "0", "", "", "", "", 
    "",  "", "", "", "", 
    "",  "", "", "", "",
    "",  "", AjaxUtils.IS_LOCK_RECORD_FALSE, ""};
    
    public static final String[] GRID_FIELD_NAMES_RETURN = { "indexNo", "itemCode", "itemCName", "warehouseCode", "warehouseName", "lotNo", 
	"originalUnitPrice", "actualUnitPrice", "returnableQuantity", "shipQuantity", "originalShipAmount","actualShipAmount", "taxType",
        "taxRate", "shipTaxAmount", "isTax", "watchSerialNo", "reserve2", "lineId", "isLockRecord", "isDeleteRecord", "message"};
    
    public static final int[] GRID_FIELD_TYPES_RETURN = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, 
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};
    
    public static final String[] GRID_FIELD_DEFAULT_VALUES_RETURN = { "0", "", "", "", "", "000000000000", "", "", "", "", "", "", "3", "5", "0.0", "1", "", "", "",
	AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""};

    /* spring IoC */   
    public void setBuCustomerWithAddressViewDAO(
	    BuCustomerWithAddressViewDAO buCustomerWithAddressViewDAO) {
	this.buCustomerWithAddressViewDAO = buCustomerWithAddressViewDAO;
    }
    
    public void setBuEmployeeWithAddressViewDAO(
	    BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
	this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
    }
    
    public void setImDeliveryHeadDAO(ImDeliveryHeadDAO imDeliveryHeadDAO) {
	this.imDeliveryHeadDAO = imDeliveryHeadDAO;
    }

    public void setImDeliveryLineDAO(ImDeliveryLineDAO imDeliveryLineDAO) {
	this.imDeliveryLineDAO = imDeliveryLineDAO;
    }
    
    public void setImItemDAO(ImItemDAO imItemDAO) {
	this.imItemDAO = imItemDAO;
    }
    
    public void setImItemPriceDAO(ImItemPriceDAO imItemPriceDAO) {
	this.imItemPriceDAO = imItemPriceDAO;
    }

    public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
	this.imOnHandDAO = imOnHandDAO;
    }
    
    public void setImPromotionDAO(ImPromotionDAO imPromotionDAO) {
	this.imPromotionDAO = imPromotionDAO;
    }
    
    public void setImTransationDAO(ImTransationDAO imTransationDAO) {
	this.imTransationDAO = imTransationDAO;
    }
    
    public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
	this.imWarehouseDAO = imWarehouseDAO;
    }

    public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
	this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
    }

    public void setSoSalesOrderItemDAO(SoSalesOrderItemDAO soSalesOrderItemDAO) {
	this.soSalesOrderItemDAO = soSalesOrderItemDAO;
    }

    public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
	this.buOrderTypeService = buOrderTypeService;
    }
    
    public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
	this.buEmployeeDAO = buEmployeeDAO;
    }
    
    public void setBuOrderTypeApprovalDAO(BuOrderTypeApprovalDAO buOrderTypeApprovalDAO) {
	this.buOrderTypeApprovalDAO = buOrderTypeApprovalDAO;
    }
    
    public void setBuShopDAO(BuShopDAO buShopDAO) {
	this.buShopDAO = buShopDAO;
    }
    
    public void setBuCountryDAO(BuCountryDAO buCountryDAO) {
	this.buCountryDAO = buCountryDAO;
    }
    
    public void setBuPaymentTermDAO(BuPaymentTermDAO buPaymentTermDAO) {
	this.buPaymentTermDAO = buPaymentTermDAO;
    }
    
    public void setBuCurrencyDAO(BuCurrencyDAO buCurrencyDAO) {
	this.buCurrencyDAO = buCurrencyDAO;
    }
    
    public void setImDeliveryLineService(ImDeliveryLineService imDeliveryLineService) {
	this.imDeliveryLineService = imDeliveryLineService;
    }
    
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }

    /**
     * 新增出貨單主檔、與出貨單明細檔建立關連
     * 
     * @param SalesOrderHeadId
     * @return ImDeliveryHead
     * @throws Exception
     */
    public ImDeliveryHead createDeliveryHeadAndSetRelation(Long SalesOrderHeadId)
	    throws Exception {

	return produceDeliveryHeadAndSetRelation(SalesOrderHeadId);
    }
    
    /**
     * 新增出貨單主檔、與出貨單明細檔建立關連
     * 
     * @param SalesOrderHeadId
     * @return ImDeliveryHead
     * @throws Exception
     */
    public ImDeliveryHead produceDeliveryHeadAndSetRelation(Long SalesOrderHeadId)
	    throws Exception {

	try {
	    SoSalesOrderHead salesOrderHead = (SoSalesOrderHead) soSalesOrderHeadDAO
		    .findByPrimaryKey(SoSalesOrderHead.class, SalesOrderHeadId);
	    if (salesOrderHead != null) {

		List imDeliveryLines = imDeliveryLineDAO.findByProperty(
			"ImDeliveryLine", "salesOrderId", SalesOrderHeadId);

		if (imDeliveryLines != null && imDeliveryLines.size() > 0) {

		    String userId = salesOrderHead.getCreatedBy();

		    ImDeliveryHead deliveryHead = copySalesOrderHeadToDeliveryHead(
			    salesOrderHead, userId);

		    imDeliveryHeadDAO.save(deliveryHead); // save出貨單主檔

		    // =====將出貨單主檔的PK儲存至明細檔，建立關連=====
		    for (int i = 0; i < imDeliveryLines.size(); i++) {
			ImDeliveryLine deliveryLine = (ImDeliveryLine) imDeliveryLines
				.get(i);
			deliveryLine.setImDeliveryHead(deliveryHead);
			deliveryLine.setCreatedBy(null);
			deliveryLine.setCreationDate(null);
			deliveryLine.setLastUpdatedBy(null);
			deliveryLine.setLastUpdateDate(null);
			deliveryLine.setStatus(null);
			deliveryLine.setIndexNo(i + 1L);
			modifyImDeliveryLine(deliveryLine);
		    }

		    // =========將出貨單主檔的PK儲存至銷貨單明細檔========
		    List<SoSalesOrderItem> soSalesOrderItems = salesOrderHead
			    .getSoSalesOrderItems();
		    if (soSalesOrderItems != null) {
			for (SoSalesOrderItem salesOrderItem : soSalesOrderItems) {
			    salesOrderItem.setDeliveryId(deliveryHead
				    .getHeadId());
			    soSalesOrderItemDAO.update(salesOrderItem);
			}
		    }

		    return deliveryHead;
		} else {
		    throw new NoSuchDataException("依據銷貨單主檔主鍵："
			    + SalesOrderHeadId + "查無相關出貨單明細檔資料！");
		}
	    } else {
		throw new NoSuchDataException("銷貨單主檔查無主鍵：" + SalesOrderHeadId
			+ "的資料！");
	    }
	} catch (Exception ex) {
	    log.error("產生出貨單主檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("產生出貨單主檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 將SalesOrderHead內容複製到ImDeliveryHead
     * 
     * @param salesOrderHead
     * @param userId
     * @return ImDeliveryHead
     */
    private ImDeliveryHead copySalesOrderHeadToDeliveryHead(
	    SoSalesOrderHead salesOrderHead, String userId)
	    throws NoSuchDataException, ObtainSerialNoFailedException {

	//依據銷售單別取得出貨單別(nextOrderTypeCode)
	BuOrderTypeId id = new BuOrderTypeId(salesOrderHead.getBrandCode(), salesOrderHead.getOrderTypeCode());
	BuOrderType buOrderType = buOrderTypeService.findById(id);
	if(buOrderType == null)
	    throw new NoSuchDataException("查無品牌代號：" + salesOrderHead.getBrandCode() + "、單據代號：" + salesOrderHead.getOrderTypeCode() + "的單別資料！");
	String serialNo = buOrderTypeService.getOrderSerialNo(salesOrderHead
		.getBrandCode(), buOrderType.getNextOrderTypeCode());
	ImDeliveryHead deliveryHead = new ImDeliveryHead();
	if (!serialNo.equals("unknow")) {
	    BeanUtils.copyProperties(salesOrderHead, deliveryHead);
	    deliveryHead.setHeadId(null);
	    deliveryHead.setOrderTypeCode(buOrderType.getNextOrderTypeCode());
	    deliveryHead.setOrderNo(serialNo);
	    deliveryHead.setSalesOrderId(salesOrderHead.getHeadId());
	    deliveryHead.setStatus(OrderStatus.SAVE);
	    deliveryHead.setCreatedBy(userId);
	    deliveryHead.setCreationDate(new Date());
	    deliveryHead.setLastUpdatedBy(userId);
	    deliveryHead.setLastUpdateDate(new Date());
	} else {
	    throw new ObtainSerialNoFailedException("取得出貨單號失敗！");
	}

	return deliveryHead;
    }

    /**
     * 依據primary key為查詢條件，取得出貨單主檔
     * 
     * @param headId
     * @return ImDeliveryHead
     * @throws Exception
     */
    public ImDeliveryHead findImDeliveryHeadById(Long headId) throws Exception {
	try {
	    ImDeliveryHead deliveryHead = (ImDeliveryHead) imDeliveryHeadDAO
		    .findByPrimaryKey(ImDeliveryHead.class, headId);
	    return deliveryHead;
	} catch (Exception ex) {
	    log.error("依據主鍵：" + headId + "查詢出貨單主檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據主鍵：" + headId + "查詢出貨單主檔時發生錯誤，原因："
		    + ex.getMessage());
	}
    }

    /**
     * 更換庫別、批號及更新出貨單明細
     * 
     * @param deliveryLine
     * @param organizationCode
     * @param newWarehouseCode
     * @param newLotNo
     * @param newSalesQuantity
     * @param loginUser
     * @throws FormException
     * @throws Exception
     */
    public void updateWarehouseCodeAndLotNo(ImDeliveryLine deliveryLine,
	    String organizationCode, String newWarehouseCode, String newLotNo,
	    Double newSalesQuantity, String loginUser) throws FormException,
	    Exception {

	try {
	    List<ImOnHand> lockedOnHands = null;
	    try {
		lockedOnHands = imOnHandDAO.getLockedOnHand(organizationCode,
			deliveryLine.getItemCode(), newWarehouseCode, newLotNo);
	    } catch (CannotAcquireLockException cale) {
		throw new FormException("品號：" + deliveryLine.getItemCode()
			+ ",庫別：" + newWarehouseCode + ",批號：" + newLotNo
			+ "已鎖定，請稍後再試！");
	    }
	    Double availableQuantity = imOnHandDAO.getCurrentStockOnHandQty(
		    organizationCode, deliveryLine.getItemCode(),
		    newWarehouseCode, newLotNo);

	    if (availableQuantity == null) {
		throw new NoSuchObjectException("查無品號："
			+ deliveryLine.getItemCode() + ",庫別："
			+ newWarehouseCode + ",批號：" + newLotNo + "的庫存量！");
	    } else if (availableQuantity < newSalesQuantity) {
		throw new InsufficientQuantityException("品號："
			+ deliveryLine.getItemCode() + ",庫別："
			+ newWarehouseCode + ",批號：" + newLotNo + "可用庫存量不足！");
	    }
	    // =====預訂新庫別、新批號的可用庫存=====
	    if (lockedOnHands != null && lockedOnHands.size() > 0) {
		imOnHandDAO.bookAvailableQuantity(lockedOnHands.get(0),
			newSalesQuantity, loginUser);
	    } else {
		throw new NoSuchObjectException("查無品號："
			+ deliveryLine.getItemCode() + ",庫別："
			+ newWarehouseCode + ",批號：" + newLotNo + "的庫存資料！");
	    }

	    // =====將原庫別、原批號的預計出貨數補回至可用庫存=====
	    imOnHandDAO.updateOutUncommitQuantity(organizationCode, deliveryLine.getItemCode()
			, deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), 
			deliveryLine.getSalesQuantity(), loginUser);

	    // =====將出貨單明細的庫存、批號、預計出貨量替換=====
	    deliveryLine.setWarehouseCode(newWarehouseCode);
	    deliveryLine.setLotNo(newLotNo);
	    deliveryLine.setSalesQuantity(newSalesQuantity);
	    deliveryLine.setShipQuantity(0D);
	    UserUtils.setOpUserAndDate(deliveryLine, loginUser);
	    modifyImDeliveryLine(deliveryLine);
	} catch (FormException fe) {
	    log.error("更換庫別批號時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("更換庫別批號時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更換庫別批號時發生錯誤，原因：" + ex.getMessage());
	}

    }

    /**
     * 更新出貨單主檔及明細檔
     * 
     * @param deliveryHead
     * @param loginUser
     * @return String
     * @throws Exception
     */
    public String updateDelivery(ImDeliveryHead deliveryHead,
	    HashMap conditionMap, String loginUser) throws FormException, Exception {

	String beforeChangeStatus = (String)conditionMap.get("beforeChangeStatus");
	String organizationCode = (String)conditionMap.get("organizationCode");
	try {
	    Double lineShipTaxAmount = countAllTotalAmount(deliveryHead, conditionMap);
	    Double actualShipTaxAmount = deliveryHead.getShipTaxAmount();	    
            Double balanceAmt = actualShipTaxAmount - lineShipTaxAmount;           
            
	    if(OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(deliveryHead.getStatus())){
		checkImDeliveryHead(deliveryHead);
		List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
		if(deliveryLines != null){
		    //預計出貨數不等於實際出貨數時，將差額補回庫存
		    for(ImDeliveryLine deliveryLine : deliveryLines){
		        Double salesQuantity = deliveryLine.getSalesQuantity();
		        Double shipQuantity = deliveryLine.getShipQuantity();
		        if(salesQuantity != shipQuantity){
			    if(!"Y".equals(deliveryLine.getIsServiceItem())){
			        imOnHandDAO.updateOutUncommitQuantity(organizationCode, deliveryLine.getItemCode()
				    , deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), 
				    (salesQuantity - shipQuantity), loginUser);
			    }
		        }    
		    }
		    //將差額補到出貨數量和實際出貨金額不為空值且不為零的明細其稅額
		    for(int i = deliveryLines.size() - 1; i >= 0; i--){
			ImDeliveryLine deliveryLineBean = (ImDeliveryLine)deliveryLines.get(i);
			Double actualShipQuantity = deliveryLineBean.getShipQuantity();
			Double actualShipAmount = deliveryLineBean.getActualShipAmount();
			if(actualShipQuantity != null && actualShipQuantity != 0D && actualShipAmount != null && actualShipAmount != 0D){
			    Double lastItemShipTaxAmt = deliveryLineBean.getShipTaxAmount();
			    if(lastItemShipTaxAmt == null){
			        lastItemShipTaxAmt = 0D;
			    }
			    deliveryLineBean.setShipTaxAmount(lastItemShipTaxAmt + balanceAmt);
			    break;
			}
		    }
		}
	    }
	    UserUtils.setOpUserAndDate(deliveryHead, loginUser);
	    UserUtils.setUserAndDate(deliveryHead.getImDeliveryLines(), loginUser);
	  
	    modifyImDelivery(deliveryHead);
	    return deliveryHead.getOrderTypeCode() + "-"
		    + deliveryHead.getOrderNo() + "存檔成功！";
	} catch (FormException fe) {
	    log.error("出貨單存檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("出貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("出貨單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 新增至ImDeliveryHead和ImDeliveryLine
     * 
     * @param updateObj
     */
    private void insertImDelivery(Object saveObj) {
	imDeliveryHeadDAO.save(saveObj);
    }
    
    /**
     * 更新至至ImDeliveryHead和ImDeliveryLine
     * 
     * @param updateObj
     */
    private void modifyImDelivery(Object updateObj) {
	imDeliveryHeadDAO.update(updateObj);
    }

    /**
     * 更新至ImDeliveryLine
     * 
     * @param updateObj
     */
    private void modifyImDeliveryLine(Object updateObj) {
	imDeliveryLineDAO.update(updateObj);
    }

    /**
     * 合計所有Line的金額，包括原總出貨金額、總實際出貨金額、出貨稅金總額
     * 
     * @param deliveryHead
     */
    public Double countAllTotalAmount(ImDeliveryHead deliveryHead,
	    HashMap conditionMap) throws FormException, Exception {

	try {
	    /*String beforeChangeStatus = (String) conditionMap
		    .get("beforeChangeStatus");
	    if(OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(deliveryHead.getStatus())){
		checkImDeliveryLines(deliveryHead);
	    }*/
	    List<ImDeliveryLine> imDeliveryLines = deliveryHead
		    .getImDeliveryLines();
	    Double totalOriginalShipAmount = 0D;
	    Double totalActualShipAmount = 0D;
	    Double shipTaxAmount = 0D;
	    Double actualShipTaxAmount = 0D;
	    for (ImDeliveryLine deliveryLine : imDeliveryLines) {
		countItemRelationAmount(deliveryLine);
		totalOriginalShipAmount += deliveryLine.getOriginalShipAmount();
		totalActualShipAmount += deliveryLine.getActualShipAmount();
		shipTaxAmount += deliveryLine.getShipTaxAmount();

		//deliveryLine.setStatus(deliveryHead.getStatus()); // 將head的status放置Item中
	    }
	    actualShipTaxAmount = calculateTaxAmount(deliveryHead.getTaxType(), deliveryHead.getTaxRate(), totalActualShipAmount);
	    deliveryHead.setTotalOriginalShipAmount(totalOriginalShipAmount);
	    deliveryHead.setTotalActualShipAmount(totalActualShipAmount);
	    deliveryHead.setShipTaxAmount(actualShipTaxAmount);
	    return shipTaxAmount;
	} /*catch (FormException fe) {
	    log.error("出貨單金額統計時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	}*/ catch (Exception ex) {
	    log.error("出貨單金額統計時發生錯誤，原因：" + ex.toString());
	    throw new Exception("出貨單金額統計時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 重新計算Line的原出貨金額、實際出貨金額、出貨稅金
     * 
     * @param deliveryLine
     */
    private void countItemRelationAmount(ImDeliveryLine deliveryLine) {
	
	Double originalShipAmount = 0D; // 原出貨金額
	Double actualShipAmount = 0D; // 實際出貨金額
	Double shipTaxAmount = 0D; // 出貨稅金

	Double originalUnitPrice = deliveryLine.getOriginalUnitPrice();
	Double actualUnitPrice = deliveryLine.getActualUnitPrice(); // 實際售價
	Double shipQuantity = deliveryLine.getShipQuantity(); // 實際出貨數量
	String taxType = deliveryLine.getTaxType(); // 稅別
	Double taxRate = deliveryLine.getTaxRate(); // 稅率

	if(originalUnitPrice != null && actualUnitPrice != null && shipQuantity != null){
	    originalShipAmount = CommonUtils.round(originalUnitPrice * shipQuantity, 0); // 原出貨金額
	    actualShipAmount = CommonUtils.round(actualUnitPrice * shipQuantity, 0); // 實際出貨金額	
	    shipTaxAmount = calculateTaxAmount(taxType, taxRate, actualShipAmount); // 出貨稅金
	}

	deliveryLine.setOriginalShipAmount(originalShipAmount);
	deliveryLine.setActualShipAmount(actualShipAmount);
	deliveryLine.setShipTaxAmount(shipTaxAmount);
    }

    /**
     * 依據出貨單查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List findDeliveryList(HashMap conditionMap) throws Exception {

	try {
	    String customerCode = (String) conditionMap.get("customerCode");
	    String shipNo_Start = (String) conditionMap.get("shipNo_Start");
	    String shipNo_End = (String) conditionMap.get("shipNo_End");
	    String salesNo_Start = (String) conditionMap.get("salesNo_Start");
	    String salesNo_End = (String) conditionMap.get("salesNo_End");
	    String defaultWarehouseCode = (String) conditionMap.get("defaultWarehouseCode");
	    String incharge = (String) conditionMap.get("incharge");
	    
	    conditionMap.put("customerCode", customerCode.trim().toUpperCase());
	    conditionMap.put("shipNo_Start", shipNo_Start.trim());
	    conditionMap.put("shipNo_End", shipNo_End.trim());
	    conditionMap.put("salesNo_Start", salesNo_Start.trim());
	    conditionMap.put("salesNo_End", salesNo_End.trim());
	    conditionMap.put("defaultWarehouseCode", defaultWarehouseCode.trim().toUpperCase());
	    conditionMap.put("incharge", incharge.trim().toUpperCase());  //依URL區分T2,馬祖,高雄

	    return imDeliveryHeadDAO.findDeliveryList(conditionMap);
	} catch (Exception ex) {
	    log.error("查詢出貨單時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢出貨單時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 更新出貨單主檔及明細檔的Status
     * 
     * @param deliveryHeadId
     * @param status
     * @return String
     * @throws Exception
     */
    public String updateImDeliveryStatus(Long deliveryHeadId, String status)
	    throws Exception {

	try {
	    ImDeliveryHead deliveryHead = (ImDeliveryHead) imDeliveryHeadDAO
		    .findByPrimaryKey(ImDeliveryHead.class, deliveryHeadId);
	    if (deliveryHead != null) {
		deliveryHead.setStatus(status);
		deliveryHead.setLastUpdateDate(new Date());
		/*List<ImDeliveryLine> deliveryLines = deliveryHead
			.getImDeliveryLines();
		if (deliveryLines != null && deliveryLines.size() > 0) {
		    for (ImDeliveryLine deliveryLine : deliveryLines) {
			deliveryLine.setStatus(status);
			deliveryLine.setLastUpdateDate(new Date());
		    }
		}*/
		modifyImDelivery(deliveryHead);
		return "Success";
	    } else {
		throw new NoSuchDataException("出貨單主檔查無主鍵：" + deliveryHeadId
			+ "的資料！");
	    }
	} catch (Exception ex) {
	    log.error("更新出貨單狀態時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新出貨單狀態時發生錯誤，原因：" + ex.getMessage());

	}
    }

    /**
     * 檢核出貨明細資料(ImDeliveryLine)
     * 
     * @param deliveryHead
     * @throws ValidationErrorException
     */
    private void checkImDeliveryLines(ImDeliveryHead deliveryHead)
	    throws ValidationErrorException {

	String tabName = "明細資料頁籤";
	List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
	if (deliveryLines != null) {
	    String sufficientQuantityDelivery = deliveryHead
		    .getSufficientQuantityDelivery();
	    for (int i = 0; i < deliveryLines.size(); i++) {
		ImDeliveryLine deliveryLine = (ImDeliveryLine) deliveryLines
			.get(i);
		double salesQuantity = deliveryLine.getSalesQuantity().doubleValue();
		double shipQuantity = deliveryLine.getShipQuantity().doubleValue();
		if(shipQuantity != 0D){
		    shipQuantity = CommonUtils.round(shipQuantity, 2);
		    deliveryLine.setShipQuantity(shipQuantity);
		}		
		
		if ("Y".equals(sufficientQuantityDelivery)
			&& salesQuantity != shipQuantity) {
		    throw new ValidationErrorException(tabName + "中第" + (i + 1)
			    + "項明細的實際出貨數不等於預計出貨數，無法執行出貨流程！");
		} else if (salesQuantity < shipQuantity) {
		    throw new ValidationErrorException(tabName + "中第" + (i + 1)
			    + "項明細的實際出貨數不可大於預計出貨數！");
		} else if (shipQuantity < 0D) {
		    throw new ValidationErrorException(tabName + "中第" + (i + 1)
			    + "項明細的實際出貨數不可小於零！");
		}
	    }
	}
    }
    
    /**
     * 檢核出貨主檔資料(ImDeliveryLine)
     * 
     * @param deliveryHead
     * @throws ValidationErrorException
     * @throws FormException
     * @throws Exception
     */
    private void checkImDeliveryHead(ImDeliveryHead deliveryHead)
	    throws ValidationErrorException, FormException, Exception {
	
	String tabName = "主檔資料";
	String dateType = "出貨日期";
	if(deliveryHead.getShipDate() == null){
	    throw new ValidationErrorException("請輸入" + tabName + "的出貨日期！");
	}
	ValidateUtil.isAfterClose(deliveryHead.getBrandCode(), deliveryHead.getOrderTypeCode(), dateType, deliveryHead.getShipDate(),deliveryHead.getSchedule());	
    }
    
    /**
     * 依據品牌代號、出貨單別、出貨單號查詢出貨單
     * 
     * @param brandCode
     * @param orderType
     * @param orderNo
     * @return ImDeliveryHead
     * @throws Exception
     */
    public ImDeliveryHead findDeliveryByIdentification(String brandCode, String orderType, String orderNo) throws Exception {
	try{
	    return imDeliveryHeadDAO.findDeliveryByIdentification(brandCode, orderType, orderNo);
	}catch (Exception ex) {
	    log.error("依據品牌代號、出貨單別、出貨單號查詢出貨單時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌代號、出貨單別、出貨單號查詢出貨單時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 合計有輸入退貨數量Line的相關金額
     * 
     * @param deliveryHead
     */
    public Map countAllRelationAmountForReturn(ImDeliveryHead deliveryHead, HashMap conditionMap, boolean isExecCovert) throws FormException, Exception {

	try {
	    HashMap returnMap = new HashMap();
	    List<ImDeliveryLine> imDeliveryLines = deliveryHead.getImDeliveryLines();
	    Double totalOriginalShipAmount = 0D;   //原總退貨金額
	    Double totalActualShipAmount = 0D;     //總實際退貨金額
	    Double shipTaxAmount = 0D; //退貨稅金
	    Double actualShipTaxAmount = 0D;
	    String taxType = deliveryHead.getTaxType(); //稅別
	    Double taxRate = deliveryHead.getTaxRate(); //稅率 
	    Double totalItemQuantity = 0D;
	    for (ImDeliveryLine deliveryLine : imDeliveryLines) {
		deliveryLine.setTaxType(taxType);//line的稅別與head相同
		deliveryLine.setTaxRate(taxRate);//line的稅率與head相同
		if(deliveryLine.getShipQuantity() != null && deliveryLine.getShipQuantity() != 0D){
		    countItemRelationAmountForReturn(deliveryLine, isExecCovert);
		    if(!AjaxUtils.IS_DELETE_RECORD_TRUE.equals(deliveryLine.getIsDeleteRecord())){
		        totalOriginalShipAmount += deliveryLine.getOriginalShipAmount();
		        totalActualShipAmount += deliveryLine.getActualShipAmount();
		        shipTaxAmount += deliveryLine.getShipTaxAmount();
		        totalItemQuantity += deliveryLine.getShipQuantity();		           
		    }
		}
	    }
	    actualShipTaxAmount = calculateTaxAmount(deliveryHead.getTaxType(), deliveryHead.getTaxRate(), totalActualShipAmount);
	    deliveryHead.setTotalOriginalShipAmount(totalOriginalShipAmount);	  
	    deliveryHead.setTotalActualShipAmount(totalActualShipAmount);
	    deliveryHead.setShipTaxAmount(actualShipTaxAmount);
	    returnMap.put("shipTaxAmount", shipTaxAmount);
	    returnMap.put("totalItemQuantity", totalItemQuantity);
	    
	    
	    return returnMap;
	} catch (Exception ex) {
	    log.error("銷貨退貨單金額統計時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷貨退貨單金額統計時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 重新計算有輸入退貨數量Line的原退貨金額、實際退貨金額、退貨稅金
     * 
     * @param deliveryLine
     */
    private void countItemRelationAmountForReturn(ImDeliveryLine deliveryLine, boolean isExecCovert) {	
	
	Double originalUnitPrice = deliveryLine.getOriginalUnitPrice();
	Double actualUnitPrice = deliveryLine.getActualUnitPrice(); // 實際售價
	Double returnQuantity = deliveryLine.getShipQuantity(); // 實際退貨數量
	if(originalUnitPrice == null){
	    originalUnitPrice = 0D;
	}else{
	    originalUnitPrice = CommonUtils.round(originalUnitPrice, 0);
	}
	
	if(actualUnitPrice == null){
	    actualUnitPrice = 0D;
	}else{
	    actualUnitPrice = CommonUtils.round(actualUnitPrice, 0);
	}
	
	if(returnQuantity != null){
	    if(isExecCovert){
	        returnQuantity = returnQuantity * -1;
	    }
	    returnQuantity =  CommonUtils.round(returnQuantity, 2);
	}else{
	    returnQuantity = 0D;
	}
	deliveryLine.setOriginalUnitPrice(originalUnitPrice);
	deliveryLine.setActualUnitPrice(actualUnitPrice);
	deliveryLine.setShipQuantity(returnQuantity);

	Double originalReturnAmount = CommonUtils.round(originalUnitPrice * returnQuantity, 0); // 原退貨金額
	Double actualReturnAmount = CommonUtils.round(actualUnitPrice * returnQuantity, 0); // 實際退貨金額
	String taxType = deliveryLine.getTaxType(); // 稅別
	Double taxRate = deliveryLine.getTaxRate(); // 稅率
	Double returnTaxAmount = calculateTaxAmount(taxType, taxRate, actualReturnAmount); // 退貨稅金

	deliveryLine.setOriginalShipAmount(originalReturnAmount);
	deliveryLine.setActualShipAmount(actualReturnAmount);
	deliveryLine.setShipTaxAmount(returnTaxAmount);
    }
    
    
    /**
     * 檢核退貨主檔資料(ImDeliveryHead)
     * 
     * @param deliveryHead
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     * @throws Exception
     */
    private ImDeliveryHead checkImDeliveryHeadForReturn(ImDeliveryHead deliveryHead, HashMap conditionMap, 
	    String programId, String identification, List errorMsgs){
	
	String message = null;
	String tabName = "主檔資料";
	String dateType = "退貨日期";
	ImDeliveryHead orgiDeliveryHead = null;
	try{
	    String brandCode = deliveryHead.getBrandCode();
	    String orderTypeCode = deliveryHead.getOrderTypeCode();
	    String origDeliveryOrderTypeCode = deliveryHead.getOrigDeliveryOrderTypeCode();
	    String origDeliveryOrderNo = deliveryHead.getOrigDeliveryOrderNo();
		
	    ValidateUtil.isAfterClose(brandCode, orderTypeCode, dateType, deliveryHead.getShipDate(),deliveryHead.getSchedule());
	    origDeliveryOrderNo = origDeliveryOrderNo.trim().toUpperCase();
	    deliveryHead.setOrigDeliveryOrderNo(origDeliveryOrderNo);
	    orgiDeliveryHead = findDeliveryByIdentification(brandCode, origDeliveryOrderTypeCode, origDeliveryOrderNo);
	    if(orgiDeliveryHead == null){
		message = "查無" + tabName + "的出貨單號：" + origDeliveryOrderNo + "！";
                siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, deliveryHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }else if(!OrderStatus.FINISH.equals(orgiDeliveryHead.getStatus()) && !OrderStatus.CLOSE.equals(orgiDeliveryHead.getStatus())) {
		message = tabName + "的出貨單號：" + origDeliveryOrderNo + "狀態為" + OrderStatus.getChineseWord(orgiDeliveryHead.getStatus()) + "無法執行退貨！";
                siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, deliveryHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	}catch(Exception ex){
	    message = "檢核銷退單" + tabName + "時發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, deliveryHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
	return orgiDeliveryHead;
    }
    
    /**
     * 判斷是否為新資料，並將退貨資料新增或更新至退貨單主檔及明細檔
     * 
     * @param deliveryHead
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String insertOrUpdateImDeliveryForReturn(ImDeliveryHead deliveryHead, String loginUser)
	    throws ObtainSerialNoFailedException, FormException, Exception {
		
	UserUtils.setOpUserAndDate(deliveryHead, loginUser);
	//UserUtils.setUserAndDate(deliveryHead.getImDeliveryLines(), loginUser);
	
	if (deliveryHead.getHeadId() == null
		&& !StringUtils.hasText(deliveryHead.getOrderNo())) {

	    String serialNo = buOrderTypeService.getOrderSerialNo(
		    deliveryHead.getBrandCode(), deliveryHead.getOrderTypeCode());
	    if (!serialNo.equals("unknow")) {
		deliveryHead.setOrderNo(serialNo);
		insertImDelivery(deliveryHead);
		return deliveryHead.getOrderTypeCode() + "-" + serialNo
			+ "存檔成功！";
	    } else {
		throw new ObtainSerialNoFailedException("取得"
			+ deliveryHead.getOrderTypeCode() + "單號失敗！");
	    }
	} else {
	    modifyImDelivery(deliveryHead);
	    return deliveryHead.getOrderTypeCode() + "-"
		    + deliveryHead.getOrderNo() + "存檔成功！";
	}
    }
    
    public void copyOrigDelivery(ImDeliveryHead origDeliveryHead, ImDeliveryHead deliveryHead, HashMap map){
	
	String orderTypeCode = (String)map.get("orderTypeCode");
	String status = (String)map.get("status");
	String origDeliveryOrderTypeCode = (String)map.get("origDeliveryOrderTypeCode");
	Date returnDate = (Date)map.get("returnDate");
	BeanUtils.copyProperties(origDeliveryHead, deliveryHead);
	
	deliveryHead.setHeadId(null);
	deliveryHead.setSalesOrderId(null);
	deliveryHead.setOrderTypeCode(orderTypeCode);
	deliveryHead.setOrderNo(null);	
	deliveryHead.setOrigDeliveryOrderTypeCode(origDeliveryOrderTypeCode);
	deliveryHead.setOrigDeliveryOrderNo(origDeliveryHead.getOrderNo());
	deliveryHead.setReturnDate(origDeliveryHead.getShipDate());//放置原出貨單出貨日期
	deliveryHead.setShipDate(returnDate);
	deliveryHead.setInvoiceNo(null);
	deliveryHead.setInvoiceDate(null);
	deliveryHead.setInvoicePrintLotNo(null);
	deliveryHead.setStatus(status);
	deliveryHead.setCreatedBy(null);
	deliveryHead.setCreationDate(null);
	deliveryHead.setLastUpdatedBy(null);
	deliveryHead.setLastUpdateDate(null);
	//退貨單不記錄原單銷貨及出貨相關金額、稅別
	deliveryHead.setTotalOriginalSalesAmount(null);
	deliveryHead.setTotalActualSalesAmount(null);
	deliveryHead.setTotalOriginalShipAmount(null);
	deliveryHead.setTotalActualShipAmount(null);
	deliveryHead.setTaxAmount(null);
	deliveryHead.setShipTaxAmount(null);
	
	List<ImDeliveryLine> imDeliveryLines = origDeliveryHead.getImDeliveryLines();
	List<ImDeliveryLine> newLines = new ArrayList();
	for(ImDeliveryLine imDeliveryLine : imDeliveryLines){
	    ImDeliveryLine newLine = new ImDeliveryLine();
	    BeanUtils.copyProperties(imDeliveryLine, newLine);
	    newLine.setLineId(null);
	    newLine.setImDeliveryHead(null);
	    //newLine.setStatus(status);
	    newLine.setCreatedBy(null);
	    newLine.setCreationDate(null);
	    newLine.setLastUpdatedBy(null);
	    newLine.setLastUpdateDate(null);
	    newLine.setStatus(null);
	    //將原出貨數量記錄在ReturnQuantity
	    newLine.setReturnQuantity(newLine.getShipQuantity());
	    newLine.setShipQuantity(null);
	    //將銷售及出貨相關金額、稅別清除
	    newLine.setOriginalSalesAmount(null);
	    newLine.setActualSalesAmount(null);
	    newLine.setTaxAmount(null);
	    newLine.setOriginalShipAmount(null);
	    newLine.setActualShipAmount(null);
	    newLine.setShipTaxAmount(null);
	    newLine.setReserve2(String.valueOf(imDeliveryLine.getIndexNo()));//記錄原出貨單的indexNo
	    newLine.setIndexNo(null);
	        
	    newLines.add(newLine);
	}
	deliveryHead.setImDeliveryLines(newLines);
    }
    
    /**
     * 更新退貨單主檔及明細檔的Status
     * 
     * @param deliveryHeadId
     * @param status
     * @return String
     * @throws Exception
     */
    public String updateImDeliveryStatusForReturn(Long deliveryHeadId, String status, String loginUser)
	    throws Exception {

	try {
	    ImDeliveryHead deliveryHead = (ImDeliveryHead) imDeliveryHeadDAO
		    .findByPrimaryKey(ImDeliveryHead.class, deliveryHeadId);
	    if (deliveryHead != null) {
		if(!StringUtils.hasText(loginUser)){
		    loginUser = deliveryHead.getLastUpdatedBy();
		}
		deliveryHead.setStatus(status);
		deliveryHead.setLastUpdatedBy(loginUser);
		deliveryHead.setLastUpdateDate(new Date());	
		/*List<ImDeliveryLine> deliveryLines = deliveryHead
			.getImDeliveryLines();
		if (deliveryLines != null && deliveryLines.size() > 0) {
		    for (ImDeliveryLine deliveryLine : deliveryLines) {
			deliveryLine.setStatus(status);
			deliveryLine.setLastUpdatedBy(loginUser);
			deliveryLine.setLastUpdateDate(new Date());
		    }
		}*/
		modifyImDelivery(deliveryHead);
		return "Success";
	    } else {
		throw new NoSuchDataException("銷貨退貨單主檔查無主鍵：" + deliveryHeadId
			+ "的資料！");
	    }
	} catch (Exception ex) {
	    log.error("更新銷貨退貨單狀態時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新銷貨退貨單狀態時發生錯誤，原因：" + ex.getMessage());

	}
    }
    
    /**
     * 依據退貨單查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List findReturnList(HashMap conditionMap) throws Exception{
	
        try{
	    String orderNo_Start = (String) conditionMap.get("orderNo_Start");
            String orderNo_End = (String) conditionMap.get("orderNo_End");		   	   
	    conditionMap.put("orderNo_Start", orderNo_Start.trim());
	    conditionMap.put("orderNo_End", orderNo_End.trim());
	   
	    return imDeliveryHeadDAO.findReturnList(conditionMap);
        }catch (Exception ex) {
	    log.error("查詢退貨單時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢退貨單時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 依據網購發票維護螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List findInvoiceList(HashMap conditionMap) throws Exception{
	
        try{
	    String shipNo_Start = (String) conditionMap.get("shipNo_Start");
            String shipNo_End = (String) conditionMap.get("shipNo_End");		   	   
	    conditionMap.put("shipNo_Start", shipNo_Start.trim());
	    conditionMap.put("shipNo_End", shipNo_End.trim());
	   
	    return imDeliveryHeadDAO.findInvoiceList(conditionMap);
        }catch (Exception ex) {
	    log.error("查詢網購出貨單的發票號碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢網購出貨單的發票號碼時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 產生UUID後寫入出貨單的列印批號
     * 
     * @param deliveryHeads
     * @param loginUser
     * @return String
     * @throws Exception
     */
    public String updateImDeliveryForPrintInvoice(List deliveryHeads, String loginUser)throws Exception {
	try{
	    String uuid = UUID.randomUUID().toString();
	    for(int i = 0; i < deliveryHeads.size(); i++){
		ImDeliveryHead deliveryHead = (ImDeliveryHead)deliveryHeads.get(i);
		if(deliveryHead.getInvoiceNo() == null){
		    throw new ValidationErrorException("出貨單號：" + deliveryHead.getOrderNo() + "的發票號碼未輸入！");
		}
		deliveryHead.setInvoicePrintLotNo(uuid);
		UserUtils.setOpUserAndDate(deliveryHead, loginUser);
		modifyImDelivery(deliveryHead);
	    }
	    return uuid;
	}catch (FormException fe) {
	    log.error("發票列印作業產生序號時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	}catch (Exception ex) {
	    log.error("發票列印作業產生序號時發生錯誤，原因：" + ex.toString());
	    throw new Exception("發票列印作業產生序號時發生錯誤，原因：" + ex.getMessage());
	}	
    }
    
    /**
     * 將發票號碼寫入出貨單
     * 
     * @param deliveryHeads
     * @param loginUser
     * @return String
     * @throws Exception
     */
    public String updateImDeliveryForSaveInvoice(List deliveryHeads, String beginInvoiceNo, String loginUser)throws Exception {
	try{
	    
	    String[] invoiceArray = checkInvoice(beginInvoiceNo);
	    String invoiceLocus = invoiceArray[0];
	    int serialNo = Integer.parseInt(invoiceArray[1]);
	    //依據所勾選的出貨單配予發票號碼
	    StringBuffer actualInvoiceNo = new StringBuffer(invoiceLocus);
	    for(int i = 0; i < deliveryHeads.size(); i++){
		actualInvoiceNo.delete(INVOICE_LOCUS_LENGTH, actualInvoiceNo.toString().length());
		ImDeliveryHead deliveryHead = (ImDeliveryHead)deliveryHeads.get(i);	
		actualInvoiceNo.append(CommonUtils.insertCharacterWithFixLength(String.valueOf(serialNo++), 8, CommonUtils.ZERO));
		List queryResult = imDeliveryHeadDAO.findByProperty("ImDeliveryHead", "invoiceNo", actualInvoiceNo.toString());
		if(queryResult != null && queryResult.size() > 0){
		    throw new ValidationErrorException("發票號碼：" + actualInvoiceNo.toString() + "已存在！");
		}		
		deliveryHead.setInvoiceNo(actualInvoiceNo.toString());		
		deliveryHead.setInvoiceDate(deliveryHead.getShipDate());//發票日期暫時先存出貨日期
		UserUtils.setOpUserAndDate(deliveryHead, loginUser);
		modifyImDelivery(deliveryHead);
	    }
	    return "發票號碼存檔成功！";
	}catch (FormException fe) {
	    log.error("儲存網購發票號碼時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	}catch (Exception ex) {
	    log.error("儲存網購發票號碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("儲存網購發票號碼時發生錯誤，原因：" + ex.getMessage());
	}	
    }
    
    /**
     * 清除出貨單上的發票號碼
     * 
     * @param deliveryHeads
     * @param loginUser
     * @return String
     * @throws Exception
     */
    public String updateImDeliveryForClearInvoice(List deliveryHeads, String loginUser)throws Exception {
	try{	    
	    //將原invoiceNo設為null	    
	    for(int i = 0; i < deliveryHeads.size(); i++){
		ImDeliveryHead deliveryHead = (ImDeliveryHead)deliveryHeads.get(i);
		deliveryHead.setInvoiceNo(null);
		deliveryHead.setInvoiceDate(null);
		modifyImDelivery(deliveryHead);
	    }

	    return "發票號碼清除成功！";
	}catch (Exception ex) {
	    log.error("清除網購發票號碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("清除網購發票號碼時發生錯誤，原因：" + ex.getMessage());
	}	
    }
    
    /**
     * 檢查發票格式
     * 
     * @param invoiceNo
     * @return String[]
     * @throws ValidationErrorException
     */
    public String[] checkInvoice(String invoiceNo) throws ValidationErrorException{
	 if(!StringUtils.hasText(invoiceNo)){
	     throw new ValidationErrorException("請輸入發票號碼！");	
	 }else if(invoiceNo.length() != 10){
	     throw new ValidationErrorException("發票號碼長度應為10碼！");
	 }
	 String invoiceLocus = invoiceNo.substring(0, 2);
	 String subInvoiceNo = invoiceNo.substring(2);
	 
	 if(!ValidateUtil.isEnglishAlphabetNoSpace(invoiceLocus)){
	     throw new ValidationErrorException("發票字軌應為英文字母！");
	 }else if(!ValidateUtil.isNumber(subInvoiceNo)){
	     throw new ValidationErrorException("發票號碼後8碼應為數字！");
	 }
	 
	 return new String[]{invoiceLocus.toUpperCase(), subInvoiceNo};
    }
    
    public Double calculateTaxAmount(String taxType, Double taxRate, Double actualAmount){
	
        Double taxAmount = 0D;
	if("3".equals(taxType) && taxRate != null && taxRate != 0D && actualAmount != null && actualAmount != 0D){
	    Double amount = actualAmount /(1 + taxRate/100);
	    taxAmount = actualAmount - amount;
	}
	
	return CommonUtils.round(taxAmount, 0);
    }
    
    public String checkShipQuantity(ImDeliveryHead deliveryHead){
	
	String returnMsg = null;
	String tabName = "明細資料頁籤";
	List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
	if (deliveryLines != null) {
	    for (int i = 0; i < deliveryLines.size(); i++) {
		ImDeliveryLine deliveryLine = (ImDeliveryLine) deliveryLines.get(i);
		Double shipQuantity = deliveryLine.getShipQuantity();
		shipQuantity = CommonUtils.round(shipQuantity, 2);
		deliveryLine.setShipQuantity(shipQuantity);
		
		if(shipQuantity == 0D){
		    returnMsg = tabName + "中第" + (i + 1)
			    + "項明細的實際出貨數為零，是否進行出貨！";
		}
	    }
	}
	
	return returnMsg;
    }
    
    public void executeDailyBalance(ImDeliveryHead delievryHead, String opUser) throws Exception{
	
	String brandCode = delievryHead.getBrandCode();
	String orderTypeCode = delievryHead.getOrderTypeCode();
	String orderNo = delievryHead.getOrderNo();
	String identification = MessageStatus.getIdentificationMsg(brandCode, orderTypeCode, orderNo);
	System.out.println(identification);
	Long salesOrderId = delievryHead.getSalesOrderId();
	if(salesOrderId != null){
	    SoSalesOrderHead salesOrder = (SoSalesOrderHead) soSalesOrderHeadDAO.findByPrimaryKey(SoSalesOrderHead.class, salesOrderId);
	    if(salesOrder != null){
		//更新銷貨單Status為CLOSE、將出貨單的出貨日期回寫至銷貨單
		salesOrder.setStatus(OrderStatus.CLOSE);
		List<SoSalesOrderItem> salesOrderItems = salesOrder.getSoSalesOrderItems();
		if (salesOrderItems != null && salesOrderItems.size() > 0) {
		    for (SoSalesOrderItem salesOrderItem : salesOrderItems) {
			salesOrderItem.setStatus(OrderStatus.CLOSE);
			salesOrderItem.setShippedDate(delievryHead.getShipDate());
			//TODO:將出貨數量回寫至銷貨單<暫時不作>
		    }
		    soSalesOrderHeadDAO.update(salesOrder);
		}else{
		    throw new ValidationErrorException("查無" + identification + "的銷售單明細資料！");
		}
		
		//更新出貨單Status為CLOSE	
		delievryHead.setStatus(OrderStatus.CLOSE);
		List<ImDeliveryLine> deliveryLines = delievryHead.getImDeliveryLines();
		System.out.println(deliveryLines.size());
		if (deliveryLines != null && deliveryLines.size() > 0) {
		    for (ImDeliveryLine deliveryLine : deliveryLines) {
			deliveryLine.setStatus(OrderStatus.CLOSE);
			if(!"Y".equals(deliveryLine.getIsServiceItem())){
			    //庫存主檔的OUT_UNCOMMIT_QTY和STOCK_ON_HAND_QTY皆扣掉出貨單實際出貨數
				System.out.println("item_code:"+deliveryLine.getItemCode());
			    imOnHandDAO.updateOutOnHand(ORGANIZATION_CODE, deliveryLine.getItemCode(), 
				    deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), 
				    deliveryLine.getShipQuantity(), opUser);
			    //產生交易明細檔
			    ImTransation transation = new ImTransation();
			    transation.setBrandCode(brandCode);
			    transation.setTransationDate(delievryHead.getShipDate());
			    transation.setOrderTypeCode(orderTypeCode);
			    transation.setOrderNo(orderNo);
			    transation.setLineId(deliveryLine.getIndexNo());
			    transation.setItemCode(deliveryLine.getItemCode());
			    transation.setWarehouseCode(deliveryLine.getWarehouseCode());
			    transation.setLotNo(deliveryLine.getLotNo());
			    transation.setQuantity(deliveryLine.getShipQuantity() * -1);
			    transation.setCostAmount(deliveryLine.getActualShipAmount() * -1);
			    transation.setOrderAmount(deliveryLine.getActualShipAmount() * -1);//20100803 add by joeywu 
			    transation.setCreatedBy(opUser);
			    transation.setCreationDate(new Date());
			    transation.setLastUpdatedBy(opUser);
			    transation.setLastUpdatedDate(new Date());
			    imTransationDAO.save(transation);
			}
		    }
		    imDeliveryHeadDAO.update(delievryHead);
		}else{
		    throw new ValidationErrorException("查無" + identification + "的出貨單明細資料！");
		}		
	    }else{
                throw new NoSuchDataException("依據" + identification + "的銷售單主鍵：" + salesOrderId + "，查無對應的銷貨單資料！");
	    }
	}else{
	    throw new ValidationErrorException(identification + "的銷售單主鍵為空值！");
	}	
    }
    
    public void executeDailyBalanceForReturn(ImDeliveryHead delievryHead, String opUser) throws Exception{
	
	String brandCode = delievryHead.getBrandCode();
	String orderTypeCode = delievryHead.getOrderTypeCode();
	String orderNo = delievryHead.getOrderNo();
	String identification = MessageStatus.getIdentificationMsg(brandCode, orderTypeCode, orderNo);	

	//更新退貨單Status為CLOSE	
	delievryHead.setStatus(OrderStatus.CLOSE);
	List<ImDeliveryLine> deliveryLines = delievryHead.getImDeliveryLines();
	if (deliveryLines != null && deliveryLines.size() > 0) {
	    for (ImDeliveryLine deliveryLine : deliveryLines) {
	        deliveryLine.setStatus(OrderStatus.CLOSE);
	        if(!"Y".equals(deliveryLine.getIsServiceItem())){
		    //庫存主檔的OUT_UNCOMMIT_QTY和STOCK_ON_HAND_QTY皆增加退貨單退貨數
		    imOnHandDAO.updateOutOnHand(ORGANIZATION_CODE, deliveryLine.getItemCode(), 
		        deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), 
		        deliveryLine.getShipQuantity(), opUser);
		    //產生交易明細檔
		    ImTransation transation = new ImTransation();
		    transation.setBrandCode(brandCode);
		    transation.setTransationDate(delievryHead.getShipDate());
		    transation.setOrderTypeCode(orderTypeCode);
		    transation.setOrderNo(orderNo);
		    transation.setLineId(deliveryLine.getIndexNo());
		    transation.setItemCode(deliveryLine.getItemCode());
		    transation.setWarehouseCode(deliveryLine.getWarehouseCode());
		    transation.setLotNo(deliveryLine.getLotNo());
		    transation.setQuantity(deliveryLine.getShipQuantity() * -1);
		    transation.setCostAmount(deliveryLine.getActualShipAmount()* -1);
		    transation.setCreatedBy(opUser);
		    transation.setCreationDate(new Date());
		    transation.setLastUpdatedBy(opUser);
		    transation.setLastUpdatedDate(new Date());
		    imTransationDAO.save(transation);
	        }
            }
	    imDeliveryHeadDAO.update(delievryHead);
	}else{
	    throw new ValidationErrorException("查無" + identification + "的退貨單明細資料！");
	}
    }
    
  
    /**
     * 檢核退貨明細資料(ImDeliveryLine)無原出貨單
     * 
     * @param deliveryHead
     * @param beforeChangeStatus
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     * @throws Exception
     */
    private void checkImDeliveryLinesForReturn(ImDeliveryHead deliveryHead, String warehouseManager, HashMap conditionMap, 
	    String programId, String identification, List errorMsgs){

	String message = null;
	String tabName = "明細資料頁籤";
	boolean isHaveReturnItem = false;
	try{
	    String brandCode = deliveryHead.getBrandCode();
	    String defaultWarehouseCode = deliveryHead.getDefaultWarehouseCode();
	    List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
	    if (deliveryLines != null && deliveryLines.size() > 0) {
	        for (int i = 0; i < deliveryLines.size(); i++) {
	            try{
		        ImDeliveryLine deliveryLine = (ImDeliveryLine) deliveryLines.get(i);
		        if(!"1".equals(deliveryLine.getIsDeleteRecord())){
		            //退貨數量不可小於零
		            Double returnQuantity = deliveryLine.getShipQuantity();
		            if(returnQuantity == null){
		                returnQuantity = 0D;
		            }
		            returnQuantity = CommonUtils.round(returnQuantity.doubleValue(), 2);
		            deliveryLine.setShipQuantity(returnQuantity);
		            if(!isHaveReturnItem && returnQuantity > 0D){
		        	isHaveReturnItem = true;
		            }
		            if (returnQuantity < 0D) {
		                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的退貨數量不可小於零！");
		            }        
		            String itemCode = deliveryLine.getItemCode();
		            String warehouseCode = deliveryLine.getWarehouseCode();
		            String lotNo = deliveryLine.getLotNo();
		            if(!StringUtils.hasText(itemCode)){
	                        throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的品號！");
		            }else if(!StringUtils.hasText(warehouseCode)){
		                throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的庫別！");
		            }else if(!ValidateUtil.isEnglishAlphabetOrNumber(itemCode)){
		                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號必須為A~Z、a~z、0~9！");
		            }else if(!ValidateUtil.isEnglishAlphabetOrNumber(warehouseCode)){
	                        throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的庫別必須為A~Z、a~z、0~9！");
		            }
		    
		            itemCode = itemCode.trim().toUpperCase();
		            warehouseCode = warehouseCode.trim().toUpperCase();
		            deliveryLine.setItemCode(itemCode);
		            deliveryLine.setWarehouseCode(warehouseCode);		
		            ImItem imItem = imItemDAO.findItem(brandCode, itemCode);
		            if(imItem == null){
		                throw new NoSuchObjectException("查無" + tabName + "中第" + (i + 1) + "項明細的品號！");
		            }
		            /*ImWarehouse warehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, warehouseCode, null);
		            if(warehouse == null){
		                throw new NoSuchObjectException("查無" + tabName + "中第" + (i + 1) + "項明細的庫別！");
		            }else if(!warehouseManager.equals(warehouse.getWarehouseManager())){
		                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的庫別與" + headTabName + "預設庫別的倉管人員不同！");
		            }*/
		            deliveryLine.setWarehouseCode(defaultWarehouseCode);//將line的WarehouseCode替換成head的defaultWarehouseCode
		            deliveryLine.setIsComposeItem(imItem.getIsComposeItem());
		            deliveryLine.setIsServiceItem(imItem.getIsServiceItem());
		
		            if(!StringUtils.hasText(lotNo)){
		                throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的批號！");
		            }else{
		                deliveryLine.setLotNo(lotNo.trim());
		            }
		            //原單價輸入檢核
		            Double originalUnitPrice = deliveryLine.getOriginalUnitPrice();
		            if(originalUnitPrice == null){
		                throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的原單價！");
		            }else{
		                originalUnitPrice = CommonUtils.round(originalUnitPrice, 0);
		                if(originalUnitPrice < 0D){
		                    throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的原單價不可小於零！");
		                }
		                deliveryLine.setOriginalUnitPrice(originalUnitPrice);
		            }		
		
		            Double actualUnitPrice = deliveryLine.getActualUnitPrice();
		            if(actualUnitPrice == null){
		                throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的實際單價！");
		            }else{
		                actualUnitPrice = CommonUtils.round(actualUnitPrice, 0);
		                deliveryLine.setActualUnitPrice(actualUnitPrice);
		                if(actualUnitPrice < 0D){
		                    throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的實際單價不可小於零！");
		                }
		            }		
		            if(originalUnitPrice < actualUnitPrice){
		                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的原單價不可小於實際單價！");
		            }
		        
		            //item的折扣率
		            Double itemDiscountRate = null;
		            if(originalUnitPrice == 0D){
		                itemDiscountRate = 100D;
		            }else{
		                itemDiscountRate = CommonUtils.round((actualUnitPrice/originalUnitPrice) * 100, 1);
		            }
		            deliveryLine.setDiscountRate(itemDiscountRate);
		        }
	            }catch(Exception ex){
		        message = ex.getMessage();
		        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
			        deliveryHead.getLastUpdatedBy());
		        errorMsgs.add(message);
		        log.error(message);	            
		    }
	        }
	        if(!isHaveReturnItem){
                    message = tabName + "中至少一筆明細的退貨數量大於零！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, deliveryHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);          
	        }
	}else{
	    message = tabName + "中至少需輸入一筆資料！";
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, deliveryHead.getLastUpdatedBy());
            errorMsgs.add(message);
	    log.error(message);
	}
	}catch(Exception ex){
	    message = "檢核" + tabName + "時發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, deliveryHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }
    
    /**
     * 檢核退貨主檔資料(ImDeliveryHead)無原出貨單
     * 
     * @param deliveryHead
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     * @throws Exception
     */
    private String checkDeliveryHeadForReturn(ImDeliveryHead deliveryHead, HashMap conditionMap, 
	    String programId, String identification, List errorMsgs){
	
	String message = null;
	String tabName = "主檔資料";
	String dateType = "退貨日期";
	String warehouseManager = null;
	try{
	    String brandCode = deliveryHead.getBrandCode();
	    String orderTypeCode = deliveryHead.getOrderTypeCode();
	    String customerCode = deliveryHead.getCustomerCode();
	    String superintendentCode = deliveryHead.getSuperintendentCode();
	    String defaultWarehouseCode = deliveryHead.getDefaultWarehouseCode();
	    String promotionCode = deliveryHead.getPromotionCode();
	
	    ValidateUtil.isAfterClose(brandCode, orderTypeCode, dateType, deliveryHead.getShipDate(),deliveryHead.getSchedule());
	    if(StringUtils.hasText(customerCode)){
	        customerCode = customerCode.trim().toUpperCase();    
	        BuCustomerWithAddressView customerWithAddressView = buCustomerWithAddressViewDAO.findCustomerByType(brandCode, customerCode, "customerCode", null);
	        if(customerWithAddressView == null){
	            message = "查無" + tabName + "的客戶代號！";
                    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
                	    deliveryHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
                }else{
		    customerCode = customerWithAddressView.getCustomerCode();
		    deliveryHead.setCustomerCode(customerCode);
	        }
	    }else{
		deliveryHead.setCustomerCode(null);
	    }
	
	    if(!StringUtils.hasText(deliveryHead.getShopCode())){
		message = "請選擇" + tabName + "的專櫃代號！";
                siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
                	deliveryHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	    
	    if(StringUtils.hasText(superintendentCode)){
	        superintendentCode = superintendentCode.trim().toUpperCase();
	        deliveryHead.setSuperintendentCode(superintendentCode);
	        BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, superintendentCode);
	        if(employeeWithAddressView == null){
	            message = "查無" + tabName + "的訂單負責人！";
	            siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
	        	    deliveryHead.getLastUpdatedBy());
	            errorMsgs.add(message);
		    log.error(message);
	        }
	    }else{
		deliveryHead.setSuperintendentCode(null);
	    }	
	    	
	    if (!StringUtils.hasText(deliveryHead.getTaxType())) {
		message = "請選擇" + tabName + "的稅別！";
	        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
	        	deliveryHead.getLastUpdatedBy());
	        errorMsgs.add(message);
		log.error(message);
	    }else if(("1".equals(deliveryHead.getTaxType()) || "2".equals(deliveryHead.getTaxType())) && (deliveryHead.getTaxRate() == null || deliveryHead.getTaxRate() != 0D)){
		message = tabName + "的稅別為免稅或零稅時，其稅率應為0%！";
	        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
	        	deliveryHead.getLastUpdatedBy());
	        errorMsgs.add(message);
		log.error(message);
	    }else if("3".equals(deliveryHead.getTaxType()) && (deliveryHead.getTaxRate() == null || deliveryHead.getTaxRate() != 5D)){
		message = tabName + "的稅別為應稅時，其稅率應為5%！";
	        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
	        	deliveryHead.getLastUpdatedBy());
	        errorMsgs.add(message);
		log.error(message);
	    }
	    
	    if(!StringUtils.hasText(defaultWarehouseCode)){
		message = "請選擇" + tabName + "的庫別！";
	        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
	        	deliveryHead.getLastUpdatedBy());
	        errorMsgs.add(message);
		log.error(message);
	    }else{
	        defaultWarehouseCode = defaultWarehouseCode.trim().toUpperCase();
	        deliveryHead.setDefaultWarehouseCode(defaultWarehouseCode);
	        ImWarehouse warehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, defaultWarehouseCode, null);
	        if(warehouse == null){
		    message = "查無" + tabName + "的庫別！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
			    deliveryHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);		    
	        }else{
		    warehouseManager = warehouse.getWarehouseManager();
		    if(!StringUtils.hasText(warehouseManager)){
		        message = tabName + "的庫別：" + defaultWarehouseCode + "其倉管人員為空值！";
		        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
		                deliveryHead.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);	
		    }
	        }
	    }
	
	    //活動代號檢核
	    if (StringUtils.hasText(promotionCode)) {
	        promotionCode = promotionCode.trim().toUpperCase();
	        deliveryHead.setPromotionCode(promotionCode);
	        if(imPromotionDAO.findByBrandCodeAndPromotionCode(brandCode, promotionCode) == null){
		    message = "查無" + tabName + "的活動代號！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
			    deliveryHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
	        }
	    }
	
	    if(deliveryHead.getDiscountRate() != null && deliveryHead.getDiscountRate() < 0D){
	        message = tabName + "的折扣比率不可小於0%！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
			deliveryHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	}catch(Exception ex){
	    message = "檢核銷退單" + tabName + "時發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
		    deliveryHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
	
	return warehouseManager;
    }
    
    public void convertToPositiveForReturn(ImDeliveryHead deliveryHead){
	
	Double totalOriginalShipAmount = deliveryHead.getTotalOriginalShipAmount();
	Double totalActualShipAmount = deliveryHead.getTotalActualShipAmount();
	Double totalShipTaxAmount = deliveryHead.getShipTaxAmount();
	if(totalOriginalShipAmount != null && totalOriginalShipAmount != 0D){
	    totalOriginalShipAmount = totalOriginalShipAmount * -1;
	    deliveryHead.setTotalOriginalShipAmount(totalOriginalShipAmount);
	}
	if(totalActualShipAmount != null && totalActualShipAmount != 0D){
	    totalActualShipAmount = totalActualShipAmount * -1;
	    deliveryHead.setTotalActualShipAmount(totalActualShipAmount);
	}
	if(totalShipTaxAmount != null && totalShipTaxAmount != 0D){
	    totalShipTaxAmount = totalShipTaxAmount * -1;
	    deliveryHead.setShipTaxAmount(totalShipTaxAmount);
	}
	List<ImDeliveryLine> imDeliveryLines = deliveryHead.getImDeliveryLines();
	if(imDeliveryLines != null){
	    for(ImDeliveryLine imDeliveryLine : imDeliveryLines){
	        Double shipQuantity = imDeliveryLine.getShipQuantity();
	        Double originalShipAmount = imDeliveryLine.getOriginalShipAmount();
	        Double actualShipAmount = imDeliveryLine.getActualShipAmount();
	        Double shipTaxAmount = imDeliveryLine.getShipTaxAmount();
	        if(shipQuantity != null && shipQuantity != 0D){
		    shipQuantity = shipQuantity * -1;
		    imDeliveryLine.setShipQuantity(shipQuantity);
	        }
	        if(originalShipAmount != null && originalShipAmount != 0D){
		    originalShipAmount = originalShipAmount * -1;
		    imDeliveryLine.setOriginalShipAmount(originalShipAmount);
	        }
	        if(actualShipAmount != null && actualShipAmount != 0D){
		    actualShipAmount = actualShipAmount * -1;
		    imDeliveryLine.setActualShipAmount(actualShipAmount);
	        }
	        if(shipTaxAmount != null && shipTaxAmount != 0D){
		    shipTaxAmount = shipTaxAmount * -1;
		    imDeliveryLine.setShipTaxAmount(shipTaxAmount);
	        }
	    }
	}
    }
    
    public void refreshDelivery(String organizationCode, ImDeliveryHead deliveryHead) throws Exception{
	
	try{
	    String brandCode = deliveryHead.getBrandCode();
	    String customerCode = deliveryHead.getCustomerCode();
	    String shopCode = deliveryHead.getShopCode();	    
	    String superintendentCode = deliveryHead.getSuperintendentCode();
	    String countryCode = deliveryHead.getCountryCode();
	    String paymentTermCode = deliveryHead.getPaymentTermCode();
	    String currencyCode = deliveryHead.getCurrencyCode();
	    String defaultWarehouseCode = deliveryHead.getDefaultWarehouseCode();
	    String promotionCode = deliveryHead.getPromotionCode();
	    	    
	    String customerName = "查無此客戶資料";	    
	    String shopName = deliveryHead.getShopCode();
	    String superintendentName = "查無此員工資料";
	    String countryName = "查無此國別資料";
	    String paymentTermName = "查無此付款條件資料";
	    String currencyName = "查無此幣別資料";
	    String defaultWarehouseName = "查無此庫號資料";
	    String promotionName = "查無此活動資料";
	    //客戶姓名
	    if(StringUtils.hasText(customerCode)){
		BuCustomerWithAddressView customerWithAddressView = buCustomerWithAddressViewDAO.findCustomerByType
	            (brandCode, customerCode, "customerCode", null);
		if(customerWithAddressView != null){
		    customerName = customerWithAddressView.getShortName();
		}
		deliveryHead.setCustomerName(customerName);
	    }
	    //專櫃名稱
	    BuShop shop = buShopDAO.findById(shopCode);
	    if(shop != null){
		shopName = shopName + "-" + shop.getShopCName();
	    }
	    deliveryHead.setShopName(shopName);
	    //訂單負責人
	    if(StringUtils.hasText(superintendentCode)){
	        BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, superintendentCode);
	        if(employeeWithAddressView != null){
	            superintendentName = employeeWithAddressView.getChineseName();
                }
	        deliveryHead.setSuperintendentName(superintendentName);
	    }
	    //國別
	    if(StringUtils.hasText(countryCode)){
		BuCountry country = buCountryDAO.findById(countryCode);
	        if(country != null){
	            countryName = country.getCountryCName();
                }
	        deliveryHead.setCountryName(countryName);
	    }
	    //付款條件
	    if(StringUtils.hasText(paymentTermCode)){
		BuPaymentTermId paymentTermId = new BuPaymentTermId();
		paymentTermId.setPaymentTermCode(paymentTermCode);
		paymentTermId.setOrganizationCode(organizationCode);
		BuPaymentTerm paymentTerm = buPaymentTermDAO.findById(paymentTermId);
		if(paymentTerm != null){
		    paymentTermName = paymentTerm.getName();
		}
		deliveryHead.setPaymentTermName(paymentTermName);
	    }
	    //幣別
	    if(StringUtils.hasText(currencyCode)){
		BuCurrency currency = buCurrencyDAO.findById(currencyCode);
		if(currency != null){
		    currencyName = currency.getCurrencyCName();
		}
		deliveryHead.setCurrencyName(currencyName);
	    }
	    //預設庫號
	    if(StringUtils.hasText(defaultWarehouseCode)){
		ImWarehouse warehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, defaultWarehouseCode);
		if(warehouse != null){
		    defaultWarehouseName = warehouse.getWarehouseName();
		}
		deliveryHead.setDefaultWarehouseName(defaultWarehouseName);
	    }
	    //活動代號
	    if(StringUtils.hasText(promotionCode)){
		ImPromotion promotion  = imPromotionDAO.findByBrandCodeAndPromotionCode(brandCode, promotionCode);
		if(promotion != null){
		    promotionName = promotion.getPromotionName();
		}
		deliveryHead.setPromotionName(promotionName);
	    }	    
	}catch(Exception ex){
	    log.error("查詢出貨單相關資訊時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢出貨單相關資訊時發生錯誤，原因：" + ex.getMessage());
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
	    String formStatus = httpRequest.getProperty("formStatus");// 狀態
	    HashMap map = new HashMap();
	    map.put("brandCode", brandCode);
	    map.put("itemCName", "查無資料");
	    map.put("warehouseName", "查無資料");
	    //==============================================================
	    List<ImDeliveryLine> imDeliveryLines = imDeliveryLineDAO.findPageLine(headId, iSPage, iPSize);
	    if(imDeliveryLines != null && imDeliveryLines.size() > 0){
		// 取得第一筆的INDEX
		Long firstIndex = imDeliveryLines.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		Long maxIndex = imDeliveryLineDAO.findPageLineMaxIndex(headId);
		if(OrderStatus.SAVE.equals(formStatus)){
	            //可編輯狀態
		    refreshDeliveryLineDataForModify(map, imDeliveryLines);
	        }else{
	            //不可編輯狀態
		    refreshDeliveryLineDataForReadOnly(map, imDeliveryLines);
	        }
		result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, imDeliveryLines, gridDatas,
			firstIndex, maxIndex));	
	    }else{
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));   
	    }
	    
	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的出貨明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的出貨明細失敗！");
	}	
    }
    
    /**
     * 更新imDeliveryLines相關資料(狀態為不可編輯時)
     * 
     * @param parameterMap
     * @param imDeliveryLines
     */
    private void refreshDeliveryLineDataForReadOnly(HashMap parameterMap, List<ImDeliveryLine> imDeliveryLines){

	for(ImDeliveryLine imDeliveryLine : imDeliveryLines){
	    getDeliveryLineCommonData(parameterMap, imDeliveryLine);
	}
    }
    
    /**
     * 更新imDeliveryLines相關資料(狀態為可編輯時)
     * 
     * @param parameterMap
     * @param imDeliveryLines
     */
    private void refreshDeliveryLineDataForModify(HashMap parameterMap, List<ImDeliveryLine> imDeliveryLines){
	
	for(ImDeliveryLine imDeliveryLine : imDeliveryLines){
	    getDeliveryLineCommonData(parameterMap, imDeliveryLine);
	    countItemRelationAmount(imDeliveryLine);
	}	
    }
    
    /**
     * 取得出貨明細必要資訊
     * 
     * @param parameterMap
     * @param imDeliveryLine
     */
    private void getDeliveryLineCommonData(HashMap parameterMap, ImDeliveryLine imDeliveryLine){
	
	String brandCode = (String)parameterMap.get("brandCode");
	String itemCName = "查無資料";
	String warehouseName = "查無資料";
	String itemCode = imDeliveryLine.getItemCode();
	String warehouseCode = imDeliveryLine.getWarehouseCode();
	//品名
	ImItem itemPO = imItemDAO.findItem(brandCode, itemCode);
	if(itemPO != null){
	    imDeliveryLine.setItemCName(itemPO.getItemCName());
	}else{
	    imDeliveryLine.setItemCName(itemCName);
	}
	    
        //庫別名稱
	ImWarehouse warehouse = (ImWarehouse) imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, warehouseCode, null);
	if(warehouse != null){
	    imDeliveryLine.setWarehouseName(warehouse.getWarehouseName());
	}else{
	    imDeliveryLine.setWarehouseName(warehouseName);
	}
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
		throw new ValidationErrorException("傳入的出貨單主鍵為空值！");
	    }
	    String status = httpRequest.getProperty("status");
	    String errorMsg = null;
	    
	    if (OrderStatus.SAVE.equals(status)) {
		// 將STRING資料轉成List Properties record data
		List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);		
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
		        // 先載入HEAD_ID OR LINE DATA
			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
			if(lineId != null){
			    ImDeliveryLine deliveryLinePO = imDeliveryLineDAO.findItemByIdentification(headId, lineId);
			    if(deliveryLinePO != null){
			        AjaxUtils.setPojoProperties(deliveryLinePO, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
			        imDeliveryLineDAO.update(deliveryLinePO);
			    }
			}
		    }
		}
	    }
	    
	    return AjaxUtils.getResponseMsg(errorMsg);
        }catch(Exception ex){
            log.error("更新出貨明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新出貨明細失敗！"); 
        }	
    }
    
    /**
     * 處理AJAX參數(line實際出貨數量變動時計算)
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
	    Double originalUnitPrice = NumberUtils.getDouble(httpRequest.getProperty("originalUnitPrice"));
	    Double actualUnitPrice = NumberUtils.getDouble(httpRequest.getProperty("actualUnitPrice"));
	    Double shipQuantity = NumberUtils.getDouble(httpRequest.getProperty("shipQuantity"));	    
	    ImDeliveryLine imDeliveryLine = new ImDeliveryLine();
	    imDeliveryLine.setOriginalUnitPrice(originalUnitPrice);
	    imDeliveryLine.setActualUnitPrice(actualUnitPrice);
	    imDeliveryLine.setShipQuantity(shipQuantity);
	    imDeliveryLine.setTaxType("3"); //暫時塞預設值
	    imDeliveryLine.setTaxRate(5D);  //暫時塞預設值
	    countItemRelationAmount(imDeliveryLine);	    

	    properties.setProperty("OriginalShipAmount", AjaxUtils.getPropertiesValue(imDeliveryLine.getOriginalShipAmount(), "0.00"));
	    properties.setProperty("ActualShipAmount", AjaxUtils.getPropertiesValue(imDeliveryLine.getActualShipAmount(), "0.00"));
	    properties.setProperty("ShipQuantity", AjaxUtils.getPropertiesValue(imDeliveryLine.getShipQuantity(), "0.00"));
	    result.add(properties);
	    
	    return result;
	}catch (Exception ex) {
	    log.error("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料發生錯誤，原因：" + ex.getMessage());
	    throw new ValidationErrorException("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料失敗！");
	}
    }
    
    /**
     * 合計所有Item的金額，包括原總出貨金額、總實際出貨金額、稅金總額
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws ValidationErrorException
     */
    public List<Properties> executeCountTotalAmount(Properties httpRequest) throws ValidationErrorException {

	try{
	    List<Properties> result = new ArrayList();
	    Properties properties = new Properties();
	    //===================取得傳遞的的參數===================
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	    String formStatus = httpRequest.getProperty("formStatus");// 狀態
	    HashMap conditionMap = new HashMap();
	    ImDeliveryHead deliveryHeadPO = findImDeliveryHeadById(headId);	    
	    if(deliveryHeadPO == null){
                throw new ValidationErrorException("查無出貨單主鍵：" + headId + "的資料！");
	    }	    
	    if(OrderStatus.SAVE.equals(formStatus)){
		countAllTotalAmount(deliveryHeadPO, conditionMap);
		executeMergeDelivery(deliveryHeadPO);
	    }
	    //=========================計算商品總數================
	    List<ImDeliveryLine> deliveryLines = deliveryHeadPO.getImDeliveryLines();
	    Double totalItemQuantity = 0D;
	    if(deliveryLines != null){
	        for(ImDeliveryLine deliveryLine : deliveryLines){
	            Double itemQuantity = deliveryLine.getShipQuantity();
	            if(itemQuantity != null){
	        	totalItemQuantity += itemQuantity;
	            }
		}
            }
	    //===================================================
	    Double totalOriginalShipAmount = deliveryHeadPO.getTotalOriginalShipAmount();
	    Double totalActualShipAmount = deliveryHeadPO.getTotalActualShipAmount();
	    Double shipTaxAmount = deliveryHeadPO.getShipTaxAmount();
	    Double totalDeductionAmount = 0D;
	    Double totalNoneTaxShipAmount = 0D;
	    if(totalOriginalShipAmount != null && totalActualShipAmount != null){
		totalDeductionAmount = totalOriginalShipAmount - totalActualShipAmount;
	    }
	    if(totalActualShipAmount != null){
		if(shipTaxAmount == null){
		    totalNoneTaxShipAmount = totalActualShipAmount;
		}else{
		    totalNoneTaxShipAmount = totalActualShipAmount - shipTaxAmount;
		}
	    }
	    	    
	    properties.setProperty("TotalOriginalShipAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalOriginalShipAmount, 0), "0.0"));
	    properties.setProperty("TotalDeductionAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalDeductionAmount, 0), "0.0"));
	    properties.setProperty("ShipTaxAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(shipTaxAmount, 0), "0.0"));
	    properties.setProperty("TotalOtherExpense", AjaxUtils.getPropertiesValue(deliveryHeadPO.getTotalOtherExpense(), "0.0"));
	    properties.setProperty("TotalActualShipAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalActualShipAmount, 0), "0.0"));
	    properties.setProperty("TotalNoneTaxShipAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalNoneTaxShipAmount, 0), "0.0"));
	    properties.setProperty("TotalItemQuantity", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalItemQuantity, 0), "0.0"));	    
	    result.add(properties);
	    
	    return result;
	}catch (Exception ex) {
	    log.error("出貨單金額統計失敗，原因：" + ex.toString());
	    throw new ValidationErrorException("出貨單金額統計失敗！");
	} 
    }
    
    /**
     * 執行出貨單merge
     * 
     * @param updateObj
     */
    public void executeMergeDelivery(ImDeliveryHead updateObj) {
	imDeliveryHeadDAO.merge(updateObj);
    }
    
    /**
     * 更新出貨單主檔及明細檔
     * 
     * @param deliveryHead
     * @param loginUser
     * @return String
     * @throws Exception
     */
    public String updateAJAXDelivery(ImDeliveryHead deliveryHead,
	    HashMap conditionMap, String loginUser) throws FormException, Exception {

	try {
	    ImDeliveryHead deliveryHeadPO = getActualDelivery(deliveryHead);
	    //=========================================================================
	    String beforeChangeStatus = (String)conditionMap.get("beforeChangeStatus");
	    String organizationCode = (String)conditionMap.get("organizationCode");
	    //=========================================================================	    
	    Double lineShipTaxAmount = countAllTotalAmount(deliveryHeadPO, conditionMap);
	    Double actualShipTaxAmount = deliveryHeadPO.getShipTaxAmount();	    
            Double balanceAmt = actualShipTaxAmount - lineShipTaxAmount;           
            
	    if(OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(deliveryHeadPO.getStatus())){
		checkImDeliveryHead(deliveryHeadPO);
		checkImDeliveryLines(deliveryHeadPO);
		List<ImDeliveryLine> deliveryLines = deliveryHeadPO.getImDeliveryLines();
		if(deliveryLines != null){
		    //預計出貨數不等於實際出貨數時，將差額補回庫存
		    for(ImDeliveryLine deliveryLine : deliveryLines){
		        double salesQuantity = deliveryLine.getSalesQuantity().doubleValue();
		        double shipQuantity = deliveryLine.getShipQuantity().doubleValue();
		        if(salesQuantity != shipQuantity){
			    if(!"Y".equals(deliveryLine.getIsServiceItem())){
			        imOnHandDAO.updateOutUncommitQuantity(organizationCode, deliveryLine.getItemCode()
				    , deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), 
				    (salesQuantity - shipQuantity), loginUser);
			    }
		        }    
		    }
		    //將差額補到出貨數量和實際出貨金額不為空值且不為零的明細其稅額
		    for(int i = deliveryLines.size() - 1; i >= 0; i--){
			ImDeliveryLine deliveryLineBean = (ImDeliveryLine)deliveryLines.get(i);
			Double actualShipQuantity = deliveryLineBean.getShipQuantity();
			Double actualShipAmount = deliveryLineBean.getActualShipAmount();
			if(actualShipQuantity != null && actualShipQuantity != 0D && actualShipAmount != null && actualShipAmount != 0D){
			    Double lastItemShipTaxAmt = deliveryLineBean.getShipTaxAmount();
			    if(lastItemShipTaxAmt == null){
			        lastItemShipTaxAmt = 0D;
			    }
			    deliveryLineBean.setShipTaxAmount(lastItemShipTaxAmt + balanceAmt);
			    break;
			}
		    }
		}
	    }
	    UserUtils.setOpUserAndDate(deliveryHeadPO, loginUser);	  
	    imDeliveryHeadDAO.merge(deliveryHeadPO);
	    
	    return deliveryHeadPO.getOrderTypeCode() + "-" + deliveryHeadPO.getOrderNo() + "存檔成功！";
	} catch (FormException fe) {
	    log.error("出貨單存檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("出貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("出貨單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    private ImDeliveryHead getActualDelivery(ImDeliveryHead deliveryHead) throws Exception{
	 
        // load line
	ImDeliveryHead deliveryHeadPO = findImDeliveryHeadById(deliveryHead.getHeadId());
	if(deliveryHeadPO == null){
	    throw new ValidationErrorException("查無出貨單主鍵：" + deliveryHead.getHeadId() + "的資料！");
	}
	deliveryHead.setImDeliveryLines(deliveryHeadPO.getImDeliveryLines());
	BeanUtils.copyProperties(deliveryHead, deliveryHeadPO);

	return deliveryHeadPO;
    }
    
    public void refreshDeliveryForReturn(ImDeliveryHead deliveryHead) throws Exception{
	
	try{
	    String brandCode = deliveryHead.getBrandCode();
	    String customerCode = deliveryHead.getCustomerCode();	    
	    String superintendentCode = deliveryHead.getSuperintendentCode();
	    String promotionCode = deliveryHead.getPromotionCode();
	    	    
	    String customerName = "查無此客戶資料";
	    String superintendentName = "查無此員工資料";
	    String promotionName = "查無此活動資料";
	    //客戶姓名
	    if(StringUtils.hasText(customerCode)){
		BuCustomerWithAddressView customerWithAddressView = buCustomerWithAddressViewDAO.findCustomerByType
	            (brandCode, customerCode, "customerCode", null);
		if(customerWithAddressView != null){
		    customerName = customerWithAddressView.getShortName();
		}
		deliveryHead.setCustomerName(customerName);
	    }
	    //訂單負責人
	    if(StringUtils.hasText(superintendentCode)){
	        BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, superintendentCode);
	        if(employeeWithAddressView != null){
	            superintendentName = employeeWithAddressView.getChineseName();
                }
	        deliveryHead.setSuperintendentName(superintendentName);
	    }	    
	    //活動代號
	    if(StringUtils.hasText(promotionCode)){
		ImPromotion promotion  = imPromotionDAO.findByBrandCodeAndPromotionCode(brandCode, promotionCode);
		if(promotion != null){
		    promotionName = promotion.getPromotionName();
		}
		deliveryHead.setPromotionName(promotionName);
	    }	    
	}catch(Exception ex){
	    log.error("查詢銷退單相關資訊時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢銷退單相關資訊時發生錯誤，原因：" + ex.getMessage());
	}	
    }
    
    public List<Properties> getAJAXPageDataForReturn(Properties httpRequest) throws Exception{
	
	try{
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
	    //======================帶入Head的值=========================
	    String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
	    String formStatus = httpRequest.getProperty("formStatus");// 狀態
	    String defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode");// 預設倉別
	    String taxType = httpRequest.getProperty("taxType");// 稅別
	    String taxRate = httpRequest.getProperty("taxRate");// 稅率
	    
	    HashMap map = new HashMap();
	    map.put("brandCode", brandCode);
	    map.put("formStatus", formStatus);
	    map.put("warehouseCode", defaultWarehouseCode);
	    map.put("taxType", taxType);
	    map.put("taxRate", taxRate);
	    getDefaultParameter(map);
	    //==============================================================
	    List<ImDeliveryLine> imDeliveryLines = imDeliveryLineDAO.findPageLine(headId, iSPage, iPSize);
	    if(imDeliveryLines != null && imDeliveryLines.size() > 0){
		// 取得第一筆的INDEX
		Long firstIndex = imDeliveryLines.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		Long maxIndex = imDeliveryLineDAO.findPageLineMaxIndex(headId);
		if(OrderStatus.SAVE.equals(formStatus) || OrderStatus.REJECT.equals(formStatus)){
	            //可編輯狀態
		    refreshReturnLineDataForModify(map, imDeliveryLines);
	        }else{
	            //不可編輯狀態
	            refreshReturnLineDataForReadOnly(map, imDeliveryLines);
	        }
		map.put("itemCName", "查無資料");
		map.put("warehouseName", "查無資料");
		result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES_RETURN, GRID_FIELD_DEFAULT_VALUES_RETURN, imDeliveryLines, gridDatas,
			firstIndex, maxIndex));	
	    }else{
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES_RETURN, GRID_FIELD_DEFAULT_VALUES_RETURN, map,gridDatas));   
	    }
	    
	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的出貨明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的出貨明細失敗！");
	}	
    }
    
    /**
     * item帶入head的預設值
     * 
     * @param parameterMap
     */
    private void getDefaultParameter(HashMap parameterMap){
	
        String taxType = (String)parameterMap.get("taxType");
	String brandCode = (String)parameterMap.get("brandCode");
	String warehouseCode = (String)parameterMap.get("warehouseCode");
	//taxType、taxRate預設值
	if("1".equals(taxType) || "2".equals(taxType)){
	    parameterMap.put("taxRate", 0D);
	}else if("3".equals(taxType)){
	    parameterMap.put("taxRate", 5D);
	}else{
            parameterMap.put("taxType", "3");
	    parameterMap.put("taxRate", 5D);
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
     * 更新imDeliveryLines相關資料(銷退狀態為不可編輯時)
     * 
     * @param parameterMap
     * @param imDeliveryLines
     */
    private void refreshReturnLineDataForReadOnly(HashMap parameterMap, List<ImDeliveryLine> imDeliveryLines){

	for(ImDeliveryLine imDeliveryLine : imDeliveryLines){
	    getReturnLineCommonData(parameterMap, imDeliveryLine);
	    Double shipQuantity = imDeliveryLine.getShipQuantity();
	    Double originalShipAmount = imDeliveryLine.getOriginalShipAmount();
	    Double actualShipAmount = imDeliveryLine.getActualShipAmount();
	    Double shipTaxAmount = imDeliveryLine.getShipTaxAmount();
	    if(shipQuantity != null && shipQuantity != 0D){
		shipQuantity = shipQuantity * -1;
		imDeliveryLine.setShipQuantity(shipQuantity);
	    }
	    if(originalShipAmount != null && originalShipAmount != 0D){
		originalShipAmount = originalShipAmount * -1;
		imDeliveryLine.setOriginalShipAmount(originalShipAmount);
	    }
	    if(actualShipAmount != null && actualShipAmount != 0D){
		actualShipAmount = actualShipAmount * -1;
		imDeliveryLine.setActualShipAmount(actualShipAmount);
	    }
	    if(shipTaxAmount != null && shipTaxAmount != 0D){
		shipTaxAmount = shipTaxAmount * -1;
		imDeliveryLine.setShipTaxAmount(shipTaxAmount);
	    }
	    
	    Double originalDeliveryQty = imDeliveryLine.getReturnQuantity();
	    Double returnedQuantity = imDeliveryLine.getReturnedQuantity();
	    if(originalDeliveryQty != null){
		if(returnedQuantity != null){
		    imDeliveryLine.setReturnableQuantity(originalDeliveryQty - returnedQuantity);
		}else{
		    imDeliveryLine.setReturnableQuantity(originalDeliveryQty);
		}
	    }
	}
    }
    
    /**
     * 更新imDeliveryLines相關資料(銷退狀態為可編輯時)
     * 
     * @param parameterMap
     * @param imDeliveryLines
     */
    private void refreshReturnLineDataForModify(HashMap parameterMap, List<ImDeliveryLine> imDeliveryLines){
	
	boolean isExecCovert = false;
	for(ImDeliveryLine imDeliveryLine : imDeliveryLines){
	    getReturnLineCommonData(parameterMap, imDeliveryLine);
	    countItemRelationAmountForReturn(imDeliveryLine, isExecCovert);
	    Double originalDeliveryQty = imDeliveryLine.getReturnQuantity();
	    Double returnedQuantity = imDeliveryLine.getReturnedQuantity();
	    if(originalDeliveryQty != null){
		if(returnedQuantity != null){
		    imDeliveryLine.setReturnableQuantity(originalDeliveryQty - returnedQuantity);
		}else{
		    imDeliveryLine.setReturnableQuantity(originalDeliveryQty);
		}
	    }
	}	
    }
    
    /**
     * 取得出貨明細必要資訊
     * 
     * @param parameterMap
     * @param imDeliveryLine
     */
    private void getReturnLineCommonData(HashMap parameterMap, ImDeliveryLine imDeliveryLine){
	
	String brandCode = (String)parameterMap.get("brandCode");
	String itemCName = "查無資料";
	String warehouseName = "查無資料";
	String itemCode = imDeliveryLine.getItemCode();
	String warehouseCode = imDeliveryLine.getWarehouseCode();
	//品名
	if(StringUtils.hasText(itemCode)){
	    itemCode = itemCode.trim().toUpperCase();
	    ImItem itemPO = imItemDAO.findItem(brandCode, itemCode);
	    if(itemPO != null){
	        imDeliveryLine.setItemCName(itemPO.getItemCName());
	    }else{
	        imDeliveryLine.setItemCName(itemCName);
	    }
	}else{
	    imDeliveryLine.setItemCName("");
	}
	    
        //庫別名稱
	if(StringUtils.hasText(warehouseCode)){
	    warehouseCode = warehouseCode.trim().toUpperCase();
	    ImWarehouse warehouse = (ImWarehouse) imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, warehouseCode, null);
	    if(warehouse != null){
	        imDeliveryLine.setWarehouseName(warehouse.getWarehouseName());
	    }else{
	        imDeliveryLine.setWarehouseName(warehouseName);
	    }
	}else{
	    imDeliveryLine.setWarehouseName("");
	}
    }
    
    /**
     * 取得暫時單號存檔
     * 
     * @param deliveryHead
     * @throws Exception
     */
    public void saveTmp(ImDeliveryHead deliveryHead) throws Exception{
	
        try{
	    String tmpOrderNo = AjaxUtils.getTmpOrderNo();
	    deliveryHead.setOrderNo(tmpOrderNo);
	    deliveryHead.setLastUpdateDate(new Date());
	    deliveryHead.setCreationDate(new Date());
	    imDeliveryHeadDAO.save(deliveryHead);	    
	}catch(Exception ex){	   
	    log.error("取得暫時單號儲存銷退單發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得暫時單號儲存銷退單發生錯誤，原因：" + ex.getMessage());
	}	
    }
    
    public List<Properties> executeCopyOrigDelivery(Properties httpRequest) throws Exception{
	
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try{
	    //======================取得複製時所需的必要資訊========================
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	    if(headId == 0L){
		throw new ValidationErrorException("無法取得銷退單的主鍵值！");
	    }
	    String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	    String status = httpRequest.getProperty("status");
	    String employeeCode = httpRequest.getProperty("employeeCode");
	    String origDeliveryOrderTypeCode = httpRequest.getProperty("origDeliveryOrderTypeCode");
	    String origDeliveryOrderNo = httpRequest.getProperty("origDeliveryOrderNo");	    
	    String returnDate = httpRequest.getProperty("returnDate");
	    String shopCodeArray = "";
	    String defaultWarehouseCodeArray = "";
	    HashMap map = new HashMap();
	    map.put("brandCode", brandCode);
	    map.put("employeeCode", employeeCode);
	    map.put("shopCodeArray", shopCodeArray);
	    map.put("defaultWarehouseCodeArray", defaultWarehouseCodeArray);
	    //======================get current return object==================
	    ImDeliveryHead deliveryHead = (ImDeliveryHead)imDeliveryHeadDAO.findByPrimaryKey(ImDeliveryHead.class, headId);
	    if(deliveryHead == null){
		throw new NoSuchObjectException("依據主鍵：" + headId + "查無銷退單的資料！");
	    }
	    //======================delete delivery line==========================
	    deleteDeliveryLine(headId);
	    //======================查詢欲複製的出貨單=============================
	    if(StringUtils.hasText(origDeliveryOrderTypeCode) && StringUtils.hasText(origDeliveryOrderNo)){
		ImDeliveryHead deliveryHeadPO = imDeliveryHeadDAO.findDeliveryByIdentification(brandCode, origDeliveryOrderTypeCode, origDeliveryOrderNo);
		if(deliveryHeadPO != null && (OrderStatus.FINISH.equals(deliveryHeadPO.getStatus()) || OrderStatus.CLOSE.equals(deliveryHeadPO.getStatus()))){		    
		    refreshDeliveryForReturn(deliveryHeadPO);
		    properties.setProperty("OrigDeliveryOrderTypeCode", origDeliveryOrderTypeCode);
		    properties.setProperty("OrigDeliveryOrderNo", origDeliveryOrderNo);
		    properties.setProperty("OrigShipDate", AjaxUtils.getPropertiesValue(DateUtils.format(deliveryHeadPO.getShipDate(), "yyyy/MM/dd"), ""));
		    properties.setProperty("ReturnDate", returnDate);
		    properties.setProperty("CustomerCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getCustomerCode(), ""));
		    properties.setProperty("CustomerName", AjaxUtils.getPropertiesValue(deliveryHeadPO.getCustomerName(), ""));
		    properties.setProperty("CustomerPoNo", AjaxUtils.getPropertiesValue(deliveryHeadPO.getCustomerPoNo(), ""));
		    properties.setProperty("QuotationCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getQuotationCode(), ""));
		    properties.setProperty("PaymentTermCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getPaymentTermCode(), ""));		    
		    properties.setProperty("CountryCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getCountryCode(), ""));
		    properties.setProperty("CurrencyCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getCurrencyCode(), ""));	    
		    properties.setProperty("ShopCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getShopCode(), ""));	    
		    properties.setProperty("ContactPerson", AjaxUtils.getPropertiesValue(deliveryHeadPO.getContactPerson(), ""));    
		    properties.setProperty("ContactTel", AjaxUtils.getPropertiesValue(deliveryHeadPO.getContactTel(), ""));    
		    properties.setProperty("Receiver", AjaxUtils.getPropertiesValue(deliveryHeadPO.getReceiver(), ""));	    
		    properties.setProperty("SuperintendentCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getSuperintendentCode(), ""));	    
		    properties.setProperty("SuperintendentName", AjaxUtils.getPropertiesValue(deliveryHeadPO.getSuperintendentName(), ""));		    
		    properties.setProperty("InvoiceTypeCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getInvoiceTypeCode(), ""));		    
		    properties.setProperty("GuiCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getGuiCode(), ""));
		    properties.setProperty("TaxType", AjaxUtils.getPropertiesValue(deliveryHeadPO.getTaxType(), "3"));    
		    properties.setProperty("TaxRate", AjaxUtils.getPropertiesValue(deliveryHeadPO.getTaxRate(), "5.0"));
		    properties.setProperty("ScheduleCollectionDate", AjaxUtils.getPropertiesValue(DateUtils.format(deliveryHeadPO.getScheduleCollectionDate(), "yyyy/MM/dd"), ""));
		    properties.setProperty("InvoiceCity", AjaxUtils.getPropertiesValue(deliveryHeadPO.getInvoiceCity(), ""));    
		    properties.setProperty("InvoiceArea", AjaxUtils.getPropertiesValue(deliveryHeadPO.getInvoiceArea(), ""));	    
		    properties.setProperty("InvoiceZipCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getInvoiceZipCode(), ""));
		    properties.setProperty("InvoiceAddress", AjaxUtils.getPropertiesValue(deliveryHeadPO.getInvoiceAddress(), ""));
		    properties.setProperty("ShipCity", AjaxUtils.getPropertiesValue(deliveryHeadPO.getShipCity(), ""));		      
		    properties.setProperty("ShipArea", AjaxUtils.getPropertiesValue(deliveryHeadPO.getShipArea(), ""));
		    properties.setProperty("ShipZipCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getShipZipCode(), ""));	    
		    properties.setProperty("ShipAddress", AjaxUtils.getPropertiesValue(deliveryHeadPO.getShipAddress(), ""));    
		    properties.setProperty("DefaultWarehouseCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getDefaultWarehouseCode(), ""));  
		    properties.setProperty("PromotionCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getPromotionCode(), ""));
		    properties.setProperty("PromotionName", AjaxUtils.getPropertiesValue(deliveryHeadPO.getPromotionName(), ""));		    
		    properties.setProperty("DiscountRate", AjaxUtils.getPropertiesValue(deliveryHeadPO.getDiscountRate(), "100.0"));
		    properties.setProperty("SufficientQuantityDelivery", AjaxUtils.getPropertiesValue(deliveryHeadPO.getSufficientQuantityDelivery(), "N"));
		    //properties.setProperty("InvoiceNo", AjaxUtils.getPropertiesValue(deliveryHeadPO.getInvoiceNo(), ""));
		    properties.setProperty("Remark1", AjaxUtils.getPropertiesValue(deliveryHeadPO.getRemark1(), ""));
		    properties.setProperty("Remark2", AjaxUtils.getPropertiesValue(deliveryHeadPO.getRemark2(), ""));	    
		    properties.setProperty("HomeDelivery", AjaxUtils.getPropertiesValue(deliveryHeadPO.getHomeDelivery(), ""));
		    properties.setProperty("PaymentCategory", AjaxUtils.getPropertiesValue(deliveryHeadPO.getPaymentCategory(), ""));
		    properties.setProperty("AttachedInvoice", AjaxUtils.getPropertiesValue(deliveryHeadPO.getAttachedInvoice(), ""));
		    //===============取得專櫃的下拉選項===============
		    BuShop shopPO = buShopDAO.findById(deliveryHeadPO.getShopCode());
		    if(shopPO != null){
		        List<String> tn = new ArrayList(); 
			List<String> tv = new ArrayList();
			tn.add(shopPO.getShopCode() + "(" + shopPO.getShopCName() + ")");
			tv.add(shopPO.getShopCode());
			shopCodeArray = AjaxUtils.getHtmlInput2DString(tn,tv);
		    }
		    //===============取得預設庫別的下拉選項===============
		    ImWarehouse warehousePO = (ImWarehouse)imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, deliveryHeadPO.getDefaultWarehouseCode());
		    if(warehousePO != null){
			List<String> tn = new ArrayList(); 
			List<String> tv = new ArrayList();
			tn.add(warehousePO.getWarehouseCode() + "(" + warehousePO.getWarehouseName() + ")");
			tv.add(warehousePO.getWarehouseCode());
			defaultWarehouseCodeArray = AjaxUtils.getHtmlInput2DString(tn,tv);
		    }
		    //================copy Line Data==================
		    List<ImDeliveryLine> deliveryLines = deliveryHeadPO.getImDeliveryLines();
		    if(deliveryLines != null && deliveryLines.size() > 0){
			List<ImDeliveryLine> newLines = new ArrayList(0);
			for(ImDeliveryLine deliveryLinePO : deliveryLines){
			    ImDeliveryLine newLine = new ImDeliveryLine();
			    BeanUtils.copyProperties(deliveryLinePO, newLine);
			    newLine.setLineId(null);
			    newLine.setImDeliveryHead(null);
			    newLine.setSalesOrderId(null);
			    newLine.setCreatedBy(null);
			    newLine.setCreationDate(null);
			    newLine.setLastUpdatedBy(null);
			    newLine.setLastUpdateDate(null);
			    newLine.setStatus(null);
			    //將原出貨數量記錄在ReturnQuantity
			    newLine.setReturnQuantity(newLine.getShipQuantity());
			    newLine.setShipQuantity(null);
			    //將銷售及出貨相關金額、稅別清除
			    newLine.setOriginalSalesAmount(null);
			    newLine.setActualSalesAmount(null);
			    newLine.setTaxAmount(null);
			    newLine.setOriginalShipAmount(null);
			    newLine.setActualShipAmount(null);
			    newLine.setShipTaxAmount(null);
			    newLine.setReserve2(String.valueOf(deliveryLinePO.getLineId()));//記錄原出貨單的lineId
			    newLine.setIndexNo(null);			        
			    newLines.add(newLine);
			}
			//====================copy head data======================
			copyOrigDeliveryHead(deliveryHead, deliveryHeadPO, origDeliveryOrderTypeCode, origDeliveryOrderNo);			
			deliveryHead.setImDeliveryLines(newLines);
			imDeliveryHeadDAO.update(deliveryHead);
		    }
		}else{
		    revertToInitially(properties, map);
		    shopCodeArray = (String)map.get("shopCodeArray");
		    defaultWarehouseCodeArray = (String)map.get("defaultWarehouseCodeArray");
		    properties.setProperty("OrigDeliveryOrderTypeCode", origDeliveryOrderTypeCode);
		    properties.setProperty("OrigDeliveryOrderNo", origDeliveryOrderNo);
		    properties.setProperty("FindOrigDeliveryResult", "N");
		    if(deliveryHeadPO != null){
			properties.setProperty("FindOrigDeliveryResult", OrderStatus.getChineseWord(deliveryHeadPO.getStatus()));
		    }
		}
	    }else{
		revertToInitially(properties, map);
		shopCodeArray = (String)map.get("shopCodeArray");
		defaultWarehouseCodeArray = (String)map.get("defaultWarehouseCodeArray");
		properties.setProperty("OrigDeliveryOrderTypeCode", origDeliveryOrderTypeCode);
	    }
	    properties.setProperty("ShopCodeArray", shopCodeArray);
	    properties.setProperty("DefaultWarehouseCodeArray", defaultWarehouseCodeArray);
	    result.add(properties);
	    
	    return result;
	}catch(Exception ex){
	    log.error("複製出貨單別發生錯誤，原因：" + ex.toString());
	    throw new Exception("複製出貨單別發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 刪除delivery明細
     * 
     * @param headId
     * @throws Exception
     */
    public void deleteDeliveryLine(Long headId) throws Exception{
	
	try{
	    ImDeliveryHead deliveryHeadPO = (ImDeliveryHead) imDeliveryHeadDAO.findByPrimaryKey(ImDeliveryHead.class, headId);
	    if(deliveryHeadPO == null){
    	        throw new ValidationErrorException("查無delivery主鍵：" + headId + "的資料！");
    	    }
	    deliveryHeadPO.setImDeliveryLines(new ArrayList<ImDeliveryLine>(0));
	    imDeliveryHeadDAO.update(deliveryHeadPO);
	}catch (Exception ex){
	    log.error("刪除delivery明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("刪除delivery明細失敗！");
	}	
    }
    
    private void revertToInitially(Properties properties, HashMap map) throws Exception{
	
	String brandCode = (String)map.get("brandCode");
	String employeeCode = (String)map.get("employeeCode");
	String shopCodeArray = (String)map.get("shopCodeArray");
	String defaultWarehouseCodeArray = (String)map.get("defaultWarehouseCodeArray");
	Date currentDate = new Date();
	ImDeliveryHead deliveryHead = new ImDeliveryHead();
	deliveryHead.setBrandCode(brandCode);
	deliveryHead.setSuperintendentCode(employeeCode);
	refreshDeliveryForReturn(deliveryHead);	
	properties.setProperty("OrigDeliveryOrderNo", "");
	properties.setProperty("OrigShipDate", "");
	properties.setProperty("ReturnDate", DateUtils.format(currentDate, "yyyy/MM/dd"));
	properties.setProperty("CustomerCode", "");
	properties.setProperty("CustomerName", "");
	properties.setProperty("CustomerPoNo", "");
	properties.setProperty("QuotationCode", "");
	properties.setProperty("PaymentTermCode", "");		    
	properties.setProperty("CountryCode", "");
	properties.setProperty("CurrencyCode", "NTD");	    
	properties.setProperty("ShopCode", "");	    
	properties.setProperty("ContactPerson", "");    
	properties.setProperty("ContactTel", "");    
	properties.setProperty("Receiver", "");	    
	properties.setProperty("SuperintendentCode", employeeCode);	    
	properties.setProperty("SuperintendentName", deliveryHead.getSuperintendentName());		    
	properties.setProperty("InvoiceTypeCode", "");		    
	properties.setProperty("GuiCode", "");
	properties.setProperty("TaxType", "3");    
	properties.setProperty("TaxRate", "5.0");
	properties.setProperty("ScheduleCollectionDate", DateUtils.format(currentDate, "yyyy/MM/dd"));
	properties.setProperty("InvoiceCity", "");    
	properties.setProperty("InvoiceArea", "");	    
	properties.setProperty("InvoiceZipCode", "");
	properties.setProperty("InvoiceAddress", "");
	properties.setProperty("ShipCity", "");		      
	properties.setProperty("ShipArea", "");
	properties.setProperty("ShipZipCode", "");	    
	properties.setProperty("ShipAddress", "");    
	properties.setProperty("DefaultWarehouseCode", "");  
	properties.setProperty("PromotionCode", "");
	properties.setProperty("PromotionName", "");		    
	properties.setProperty("DiscountRate", "100.0");
	properties.setProperty("SufficientQuantityDelivery", "N");
	properties.setProperty("InvoiceNo", "");
	properties.setProperty("Remark1", "");
	properties.setProperty("Remark2", "");	    
	properties.setProperty("HomeDelivery", "");
	properties.setProperty("PaymentCategory", "");
	properties.setProperty("AttachedInvoice", "");
	//===============取得專櫃的下拉選項===============
	List<BuShop> shops = buShopDAO.getShopForEmployee(brandCode, employeeCode, null);
	if(shops != null && shops.size() > 0){
	    List<String> tn = new ArrayList(); 
	    List<String> tv = new ArrayList();
	    for(int i = 0; i < shops.size(); i++){
		BuShop shopPO = (BuShop)shops.get(i);
                tn.add(shopPO.getShopCode() + "(" + shopPO.getShopCName() + ")");
		tv.add(shopPO.getShopCode());
		if(i == 0){
		    properties.setProperty("ShopCode", shopPO.getShopCode());	 
		}
	    }
	    shopCodeArray = AjaxUtils.getHtmlInput2DString(tn,tv);
        }
	map.put("shopCodeArray", shopCodeArray);
	//===============取得預設庫別的下拉選項=============
	List<ImWarehouse> warehouses = imWarehouseDAO.getWarehouseByWarehouseEmployee(brandCode, employeeCode, null);
	if(warehouses != null && warehouses.size() > 0){
	    List<String> tn = new ArrayList(); 
	    List<String> tv = new ArrayList();
	    for(int j = 0; j < warehouses.size(); j++){
		ImWarehouse warehousePO = (ImWarehouse)warehouses.get(j);
		tn.add(warehousePO.getWarehouseCode() + "(" + warehousePO.getWarehouseName() + ")");
		tv.add(warehousePO.getWarehouseCode());
		if(j == 0){
		    properties.setProperty("DefaultWarehouseCode", warehousePO.getWarehouseCode());	 
		}
	    }
	    defaultWarehouseCodeArray = AjaxUtils.getHtmlInput2DString(tn,tv);
	}
	map.put("defaultWarehouseCodeArray", defaultWarehouseCodeArray);
    }
    
    /**
     * 更新PAGE的LINE(銷退單)
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> updateAJAXPageLinesDataForReturn(Properties httpRequest) throws Exception{
	
        try{
            String status = httpRequest.getProperty("status");
	    String errorMsg = null;	    
	    if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)) {
		String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		if(headId == null){
		    throw new ValidationErrorException("傳入的銷退單主鍵為空值！");
		}
		    
                ImDeliveryHead deliveryHead = new ImDeliveryHead();
                deliveryHead.setHeadId(headId);
		// 將STRING資料轉成List Properties record data
		List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES_RETURN);		
		// Get INDEX NO
		int indexNo = imDeliveryLineDAO.findPageLineMaxIndex(headId).intValue();
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
		        // 先載入HEAD_ID OR LINE DATA
			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
			String itemCode = upRecord.getProperty(GRID_FIELD_NAMES_RETURN[1]);
			if (StringUtils.hasText(itemCode)) {			  
			    ImDeliveryLine deliveryLinePO = imDeliveryLineDAO.findItemByIdentification(headId, lineId);
			    if(deliveryLinePO != null){	
			        AjaxUtils.setPojoProperties(deliveryLinePO, upRecord, GRID_FIELD_NAMES_RETURN, GRID_FIELD_TYPES_RETURN);
			        imDeliveryLineDAO.update(deliveryLinePO);
			    }else{
				indexNo++;
				ImDeliveryLine deliveryLine = new ImDeliveryLine();
				AjaxUtils.setPojoProperties(deliveryLine, upRecord, GRID_FIELD_NAMES_RETURN, GRID_FIELD_TYPES_RETURN);
				deliveryLine.setIndexNo(Long.valueOf(indexNo));
				deliveryLine.setImDeliveryHead(deliveryHead);
				imDeliveryLineDAO.save(deliveryLine);
			    }			
			}
		    }
		}		
	    }
	    
	    return AjaxUtils.getResponseMsg(errorMsg);
        }catch(Exception ex){
            log.error("更新銷退明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新銷退明細失敗！"); 
        }	
    }
    
    /**
     * 處理AJAX參數(銷退單line變動時計算)
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws ValidationErrorException
     */
    public List<Properties> getAJAXItemDataForReturn(Properties httpRequest) throws ValidationErrorException{
	
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String itemIndexNo = null;
	try{
	    itemIndexNo = httpRequest.getProperty("itemIndexNo");
	    String brandCode = httpRequest.getProperty("brandCode");
	    String priceType = httpRequest.getProperty("priceType");// 價格類型
	    String itemCode = httpRequest.getProperty("itemCode");
	    String warehouseCode = httpRequest.getProperty("warehouseCode"); 
	    String actionId = httpRequest.getProperty("actionId");
	    String returnDate = httpRequest.getProperty("returnDate");
	    String taxType = httpRequest.getProperty("taxType");// 稅別
	    String watchSerialNo = httpRequest.getProperty("watchSerialNo");// 手錶序號
	    Double taxRate = NumberUtils.getDouble(httpRequest.getProperty("taxRate"));// 稅率
	    Date returnDateTmp = DateUtils.parseDate("yyyy/MM/dd", returnDate);
	    Double originalUnitPrice = NumberUtils.getDouble(httpRequest.getProperty("originalUnitPrice"));
	    Double actualUnitPrice = NumberUtils.getDouble(httpRequest.getProperty("actualUnitPrice"));
	    Double returnQuantity = NumberUtils.getDouble(httpRequest.getProperty("returnQuantity"));
	    
	    HashMap map = new HashMap();
	    map.put("brandCode", brandCode);
	    ImDeliveryLine imDeliveryLine = new ImDeliveryLine();
	    imDeliveryLine.setItemCode(itemCode);
	    imDeliveryLine.setWarehouseCode(warehouseCode);	    
	    getReturnLineCommonData(map, imDeliveryLine);
	    if("1".equals(actionId)){
		List itemPrices = imItemPriceDAO.getItemPriceByBeginDate(brandCode, itemCode, priceType, returnDateTmp, null);
		if(itemPrices != null && itemPrices.size() > 0){
	            Object[] objArray = (Object[])itemPrices.get(0);
	            BigDecimal unitPrice = (BigDecimal)objArray[1];
	            if(unitPrice != null){
	        	imDeliveryLine.setOriginalUnitPrice(unitPrice.doubleValue());
	            }
	        }
	    }else {
		imDeliveryLine.setOriginalUnitPrice(originalUnitPrice);
		imDeliveryLine.setActualUnitPrice(actualUnitPrice);
		imDeliveryLine.setShipQuantity(returnQuantity);
		imDeliveryLine.setWatchSerialNo(watchSerialNo);
	    }
	    imDeliveryLine.setTaxType(taxType);
	    imDeliveryLine.setTaxRate(taxRate);
	    countItemRelationAmountForReturn(imDeliveryLine, false);	    

	    properties.setProperty("ItemCode", AjaxUtils.getPropertiesValue(imDeliveryLine.getItemCode(), ""));
	    properties.setProperty("ItemCName", AjaxUtils.getPropertiesValue(imDeliveryLine.getItemCName(), ""));
	    properties.setProperty("WarehouseCode", AjaxUtils.getPropertiesValue(imDeliveryLine.getWarehouseCode(), ""));
	    properties.setProperty("WarehouseName", AjaxUtils.getPropertiesValue(imDeliveryLine.getWarehouseName(), ""));
	    properties.setProperty("OriginalUnitPrice", AjaxUtils.getPropertiesValue(imDeliveryLine.getOriginalUnitPrice(), ""));
	    properties.setProperty("ActualUnitPrice", AjaxUtils.getPropertiesValue(imDeliveryLine.getActualUnitPrice(), ""));
	    properties.setProperty("ShipQuantity", AjaxUtils.getPropertiesValue(imDeliveryLine.getShipQuantity(), ""));
	    properties.setProperty("OriginalShipAmount", AjaxUtils.getPropertiesValue(imDeliveryLine.getOriginalShipAmount(), ""));
	    properties.setProperty("ActualShipAmount", AjaxUtils.getPropertiesValue(imDeliveryLine.getActualShipAmount(), ""));
	    properties.setProperty("ShipTaxAmount", AjaxUtils.getPropertiesValue(imDeliveryLine.getShipTaxAmount(), ""));
	    properties.setProperty("WatchSerialNo", AjaxUtils.getPropertiesValue(imDeliveryLine.getWatchSerialNo(), ""));	    
	    result.add(properties);
	    
	    return result;
	}catch (Exception ex) {
	    log.error("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料發生錯誤，原因：" + ex.getMessage());
	    throw new ValidationErrorException("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料失敗！");
	}
    }
    
    /**
     * 合計所有Item的金額，包括原總銷退金額、總實際銷退金額、稅金總額
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws ValidationErrorException
     */
    public List<Properties> performCountTotalAmountForReturn(Properties httpRequest) throws ValidationErrorException {

	try{
	    List<Properties> result = new ArrayList();
	    Properties properties = new Properties();
	    //===================取得傳遞的的參數===================
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	    String formStatus = httpRequest.getProperty("formStatus");// 狀態
	    Double totalItemQuantity = 0D;
	    HashMap conditionMap = new HashMap();	    
	    ImDeliveryHead deliveryHeadPO = findImDeliveryHeadById(headId);	    
	    if(deliveryHeadPO == null){
                throw new ValidationErrorException("查無銷退單主鍵：" + headId + "的資料！");
	    }	    
	    if(OrderStatus.SAVE.equals(formStatus) || OrderStatus.REJECT.equals(formStatus)){
		Map countResultMap = countAllRelationAmountForReturn(deliveryHeadPO, conditionMap, false);
		totalItemQuantity = (Double)countResultMap.get("totalItemQuantity");
		//executeMergeDelivery(deliveryHeadPO);
	    }else{
		convertToPositiveForReturn(deliveryHeadPO);
		//=========================計算商品總數================
		List<ImDeliveryLine> deliveryLines = deliveryHeadPO.getImDeliveryLines();
		if(deliveryLines != null){
		    for(ImDeliveryLine deliveryLine : deliveryLines){
			if(!AjaxUtils.IS_DELETE_RECORD_TRUE.equals(deliveryLine.getIsDeleteRecord())){
		            Double itemQuantity = deliveryLine.getShipQuantity();
		            if(itemQuantity != null){
		                totalItemQuantity += itemQuantity;
		            }
			}
		    }
	        }
	    }    
	    //=============================================================
	    Double totalOriginalShipAmount = deliveryHeadPO.getTotalOriginalShipAmount();
	    Double totalActualShipAmount = deliveryHeadPO.getTotalActualShipAmount();
	    Double shipTaxAmount = deliveryHeadPO.getShipTaxAmount();
	    Double totalDeductionAmount = 0D;
	    Double totalNoneTaxShipAmount = 0D;
	    if(totalOriginalShipAmount != null && totalActualShipAmount != null){
		totalDeductionAmount = totalOriginalShipAmount - totalActualShipAmount;
	    }
	    if(totalActualShipAmount != null){
		if(shipTaxAmount == null){
		    totalNoneTaxShipAmount = totalActualShipAmount;
		}else{
		    totalNoneTaxShipAmount = totalActualShipAmount - shipTaxAmount;
		}
	    }
	    	    
	    properties.setProperty("TotalOriginalShipAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalOriginalShipAmount, 0), "0.0"));
	    properties.setProperty("TotalDeductionAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalDeductionAmount, 0), "0.0"));
	    properties.setProperty("ShipTaxAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(shipTaxAmount, 0), "0.0"));
	    properties.setProperty("TotalOtherExpense", AjaxUtils.getPropertiesValue(deliveryHeadPO.getTotalOtherExpense(), "0.0"));
	    properties.setProperty("TotalActualShipAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalActualShipAmount, 0), "0.0"));
	    properties.setProperty("TotalNoneTaxShipAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalNoneTaxShipAmount, 0), "0.0"));
	    properties.setProperty("TotalItemQuantity", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalItemQuantity, 0), "0.0"));	    
	    result.add(properties);
	    
	    return result;
	}catch (Exception ex) {
	    log.error("銷退單金額統計失敗，原因：" + ex.toString());
	    throw new ValidationErrorException("銷退單金額統計失敗！");
	} 
    }
    
    /**
     * 更新出貨單主檔及明細檔
     * 
     * @param deliveryHead
     * @param loginUser
     * @return String
     * @throws Exception
     */
    public String updateAJAXDeliveryForReturn(ImDeliveryHead deliveryHead,
	    HashMap conditionMap, String loginUser, boolean isCommitOnHand) throws FormException, Exception {

	try {
	    ImDeliveryHead deliveryHeadPO = getActualDeliveryForReturn(deliveryHead);
	    //=========================================================================
	    String origDeliveryOrderNo = deliveryHeadPO.getOrigDeliveryOrderNo();
	    String beforeChangeStatus = (String)conditionMap.get("beforeChangeStatus");
	    String organizationCode = (String)conditionMap.get("organizationCode");
	   //=========================================================================
	    if(OrderStatus.SAVE.equals(deliveryHeadPO.getStatus()) || OrderStatus.REJECT.equals(deliveryHeadPO.getStatus())
		    || OrderStatus.VOID.equals(deliveryHeadPO.getStatus())){
		if(StringUtils.hasText(origDeliveryOrderNo)){
		    ImDeliveryHead orgiDeliveryHead = null;
		    copyOrigDeliveryHead(deliveryHeadPO, orgiDeliveryHead, 
	            deliveryHeadPO.getOrigDeliveryOrderTypeCode(), origDeliveryOrderNo);
		}
		sortDeliveryLine(deliveryHeadPO);
	    }  
	    //==========================Reject時convert數量========================
	    if(OrderStatus.REJECT.equals(deliveryHeadPO.getStatus()) && OrderStatus.SIGNING.equals(beforeChangeStatus)){
		if(StringUtils.hasText(origDeliveryOrderNo)){
		    //modifyOrigDeliveryReturnedQuantity(deliveryHeadPO.getImDeliveryLines());
		}
		convertToPositiveForReturn(deliveryHeadPO);		
	    }else if((OrderStatus.SIGNING.equals(deliveryHeadPO.getStatus()))){
		if(OrderStatus.SAVE.equals(beforeChangeStatus) || OrderStatus.REJECT.equals(beforeChangeStatus)){
		    if(StringUtils.hasText(origDeliveryOrderNo)){		    
			ImDeliveryHead orgiDeliveryHead = null;
			copyOrigDeliveryHead(deliveryHeadPO, orgiDeliveryHead, 
				deliveryHeadPO.getOrigDeliveryOrderTypeCode(), origDeliveryOrderNo);
			//============重新取得原出貨單的已退數量更新退貨單的line====================
			imDeliveryLineService.saveLineDateForReturn(deliveryHeadPO);			
			//============check line data==============================
			checkLineDataForReturn(deliveryHeadPO);
			//modifyOrigDeliveryReturnedQuantity(deliveryHeadPO.getImDeliveryLines());			
		    }else{
	                origDeliveryOrderNo = null;
	                deliveryHeadPO.setOrigDeliveryOrderNo(origDeliveryOrderNo);
	                deliveryHeadPO.setOrigDeliveryOrderTypeCode(null);	           	        
		        //String warehouseManager = checkDeliveryHeadForReturn(deliveryHeadPO);
		        //checkImDeliveryLinesForReturn(deliveryHeadPO, beforeChangeStatus, warehouseManager);
	           }
		   sortDeliveryLine(deliveryHeadPO);
		   removeEmptyShipQuantity(deliveryHeadPO);		   
		   Map countResultMap = countAllRelationAmountForReturn(deliveryHeadPO, conditionMap, true);
		   Double lineShipTaxAmount = (Double)countResultMap.get("shipTaxAmount");		   
		   Double actualShipTaxAmount = deliveryHeadPO.getShipTaxAmount();
		   Double balanceAmt = actualShipTaxAmount - lineShipTaxAmount;
		   balanceTaxAmount(deliveryHeadPO.getImDeliveryLines(), balanceAmt);
		}
		//更新庫存
		if(isCommitOnHand){
		    List<ImDeliveryLine> deliveryLines = deliveryHeadPO.getImDeliveryLines();
		    if(deliveryLines != null){
	                for(int i = 0; i < deliveryLines.size(); i++){
	                    ImDeliveryLine deliveryLine = (ImDeliveryLine) deliveryLines.get(i);		    
	                    Double returnQuantity = deliveryLine.getShipQuantity();
	                    returnQuantity = returnQuantity * -1;
	                    if(!"Y".equals(deliveryLine.getIsServiceItem())){
		                imOnHandDAO.updateOutUncommitQuantity(organizationCode, deliveryLine.getItemCode()
			            , deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), returnQuantity, loginUser);
	                    }             
		        }
		    }
	        }	
	    }
	    
	    return modifyAjaxImDelivery(deliveryHeadPO, loginUser);
	} catch (FormException fe) {
	    log.error("銷退單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("銷退單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷退單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
   
    /**
     * 檢核退貨明細資料(ImDeliveryLine)
     * 
     * @param deliveryHead
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     * @throws Exception
     */
    private void checkLineDataForReturn(ImDeliveryHead deliveryHead)
	    throws ValidationErrorException, NoSuchObjectException, Exception {

	String tabName = "明細資料頁籤";
	List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
	if (deliveryLines != null && deliveryLines.size() > 0) {
	    double shipQtyAmt = 0;
	    for (int i = 0; i < deliveryLines.size(); i++) {
		ImDeliveryLine deliveryLine = (ImDeliveryLine) deliveryLines.get(i);			
		Double returnQuantity = deliveryLine.getReturnQuantity();//原出貨數量
		Double returnedQuantity = deliveryLine.getReturnedQuantity(); //原出貨單的已退貨量
		Double shipQuantity = deliveryLine.getShipQuantity(); //目前欲退貨數量
		if(returnQuantity == null){
		    returnQuantity = 0D;
		}
		if(returnedQuantity == null){
		    returnedQuantity = 0D;
		}
		if(shipQuantity == null){
		    shipQuantity = 0D;
		}
		shipQuantity = CommonUtils.round(shipQuantity.doubleValue(), 2);
		deliveryLine.setShipQuantity(shipQuantity);
		shipQtyAmt += shipQuantity.doubleValue();//統計退貨數量
		double returnableQuantity = returnQuantity - returnedQuantity;//可退貨數量
		if (returnableQuantity < shipQuantity) {
		    throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的退貨數量不可大於可退數量！");
		}else if (shipQuantity < 0D) {
		    throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的退貨數量不可小於零！");
		}		
	    }
	    if(shipQtyAmt == 0D){
                throw new ValidationErrorException(tabName + "中至少一筆明細的退貨數量大於零！");
	    }
	}else{
	    throw new ValidationErrorException("明細資料頁籤至少需輸入一筆資料！");
	}
    }
    
    /**
     * 更新至Delivery主檔或明細檔(merge)
     * 
     * @param updateObj
     * @param loginUser
     */
    private void mergeImDelivery(ImDeliveryHead updateObj, String loginUser) {
	
	updateObj.setLastUpdatedBy(loginUser);
	updateObj.setLastUpdateDate(new Date());
	imDeliveryHeadDAO.merge(updateObj);
    }
    
    /**
     * 暫存單號取實際單號並更新至Delivery主檔及明細檔
     * 
     * @param deliveryHead
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String modifyAjaxImDelivery(ImDeliveryHead deliveryHead, String loginUser)
	    throws ObtainSerialNoFailedException, FormException, Exception {
	
	if (AjaxUtils.isTmpOrderNo(deliveryHead.getOrderNo())) {
	    String serialNo = buOrderTypeService.getOrderSerialNo(
		    deliveryHead.getBrandCode(), deliveryHead
			    .getOrderTypeCode());
	    if (!serialNo.equals("unknow")) {
		deliveryHead.setOrderNo(serialNo);
	    } else {
		throw new ObtainSerialNoFailedException("取得"
			+ deliveryHead.getOrderTypeCode() + "單號失敗！");
	    }
	}	
	modifyImDelivery(deliveryHead, loginUser);	
	return deliveryHead.getOrderTypeCode() + "-" + deliveryHead.getOrderNo() + "存檔成功！";
    }
    
    /**
     * 移除空值或零的出貨數量
     * 
     * @param deliveryLines
     */
    private void removeEmptyShipQuantity(ImDeliveryHead deliveryHead){
	
	List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
	if(deliveryLines != null && deliveryLines.size() > 0){
	    for(int j = deliveryLines.size() - 1; j >= 0; j--){
	        ImDeliveryLine lineBean = (ImDeliveryLine)deliveryLines.get(j);
	        Double ShipQuantity = lineBean.getShipQuantity();
	        if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(lineBean.getIsDeleteRecord()) || 
	            ShipQuantity == null || ShipQuantity == 0D){
		    deliveryLines.remove(j);
	        }
	    }
	}
    }
    
    /**
     * 將差額補到shipQuantity和actualShipQuantity不為空值且不為零的明細其稅額
     * 
     * @param deliveryLines
     * @param balanceAmt
     */
    private void balanceTaxAmount(List<ImDeliveryLine> deliveryLines, Double balanceAmt){
	
	//將差額補到退貨數量和實際退貨金額不為空值且不為零的明細其稅額
	if(deliveryLines != null && deliveryLines.size() > 0){
            for(int i = deliveryLines.size() - 1; i >= 0; i--){
                ImDeliveryLine deliveryLineBean = (ImDeliveryLine)deliveryLines.get(i);
	        Double actualShipQuantity = deliveryLineBean.getShipQuantity();
	        Double actualShipAmount = deliveryLineBean.getActualShipAmount();
	        if(actualShipQuantity != null && actualShipQuantity != 0D && actualShipAmount != null && actualShipAmount != 0D){
	            Double lastItemShipTaxAmt = deliveryLineBean.getShipTaxAmount();
                    if(lastItemShipTaxAmt == null){
	                lastItemShipTaxAmt = 0D;
                    }
	            deliveryLineBean.setShipTaxAmount(lastItemShipTaxAmt + balanceAmt);
	            break;
	        }
            }
	}
    }
    
    /**
     * 更新原出貨單明細的已退數量
     * 
     * @param deliveryLines
     * @throws ValidationErrorException
     */
    private void modifyOrigDeliveryReturnedQuantity(ImDeliveryHead deliveryHead, HashMap conditionMap, 
	    String programId, String identification, List errorMsgs) {
	
	String message = null;
	String tabName = "明細資料頁籤";
	try{
	    List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
	    if(deliveryLines != null && deliveryLines.size() > 0){
	        for(int i = 0; i < deliveryLines.size(); i++){
	            try{
		        ImDeliveryLine deliveryLine = (ImDeliveryLine) deliveryLines.get(i);		        
		        String origiLineId = deliveryLine.getReserve2();
        	        if(!StringUtils.hasLength(origiLineId)){
        	            throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細所對應的出貨單明細資訊為空值！");
        	        }
        	        //原出貨單明細
		        ImDeliveryLine origDeliveryLinePO = (ImDeliveryLine)imDeliveryLineDAO.findByPrimaryKey(ImDeliveryLine.class, 
			        Long.valueOf(origiLineId));
		        if(origDeliveryLinePO == null){
	                    throw new ValidationErrorException("查無" + tabName + "中第" + (i + 1) + "項明細所對應的出貨單主鍵：" + 
	                	    Long.valueOf(origiLineId) + "的資訊！");
	                }
		        Double returnQuantity = deliveryLine.getShipQuantity();
		        if(returnQuantity == null){
		            returnQuantity = 0D;
		        }
        	        //更新原出貨單明細已退數量     
                        Double returnedQuantity = origDeliveryLinePO.getReturnedQuantity();
                        if(returnedQuantity == null){
                            origDeliveryLinePO.setReturnedQuantity(returnQuantity);
                        }else{
                            origDeliveryLinePO.setReturnedQuantity(returnedQuantity + returnQuantity);
                        }
                        imDeliveryLineDAO.update(origDeliveryLinePO);
	            }catch(Exception ex){
		        message = ex.getMessage();
		        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
			        deliveryHead.getLastUpdatedBy());
		        errorMsgs.add(message);
		        log.error(message);	            
		    }
	        }
	    }
	}catch (Exception ex) {
	    message = "更新原出貨單明細的已退數量失敗，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
		    deliveryHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }
    
    /**
     * 將Head的稅別、稅額、預設庫別替換line的相關欄位
     * 
     * @param httpRequest
     * @return
     * @throws ValidationErrorException
     */
    public List<Properties> updateReturnRelationData(Properties httpRequest) throws ValidationErrorException {
	
	try{
	    String formStatus = httpRequest.getProperty("formStatus");// 狀態
	    if(OrderStatus.SAVE.equals(formStatus) || OrderStatus.REJECT.equals(formStatus)){
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
		//======================帶入Head的值=========================
		String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
		String defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode");// 預設倉別
		String taxType = httpRequest.getProperty("taxType");// 稅別
		String taxRate = httpRequest.getProperty("taxRate");// 稅率
		String returnDate = httpRequest.getProperty("returnDate");//退貨日期
		String origDeliveryOrderNo = httpRequest.getProperty("origDeliveryOrderNo");//原出貨單號
		Date returnDateTmp = DateUtils.parseDate("yyyy/MM/dd", returnDate);
		    
		HashMap map = new HashMap();
		map.put("brandCode", brandCode);
		map.put("warehouseCode", defaultWarehouseCode);
		map.put("taxType", taxType);
		map.put("taxRate", taxRate);
		map.put("lotNo", GRID_FIELD_DEFAULT_VALUES_RETURN[5]);
		getDefaultParameter(map);
		ImDeliveryHead deliveryHeadPO = findImDeliveryHeadById(headId);
		if(StringUtils.hasText(origDeliveryOrderNo)){
		    //============重新取得原出貨單的已退數量更新退貨單的line===========
		    imDeliveryLineService.saveLineDateForReturn(deliveryHeadPO);
		}else{
		    resetReturnItemData(deliveryHeadPO, map);
		    //countAllRelationAmountForReturn(deliveryHeadPO, map, false);
		    imDeliveryHeadDAO.update(deliveryHeadPO);
		}		
	    }
	    
	    return AjaxUtils.getResponseMsg(null);
	}catch (Exception ex) {
	    log.error("更新銷退明細相關欄位發生錯誤，原因：" + ex.toString());
	    throw new ValidationErrorException("更新銷退明細相關欄位失敗！");
	}
    }
    
    /**
     * 替換line的相關欄位
     * 
     * @param deliveryHead
     * @param conditionMap
     */
    private void resetReturnItemData(ImDeliveryHead deliveryHead, HashMap conditionMap){
	
	String defaultWarehouseCode = (String)conditionMap.get("warehouseCode");
	String defaultTaxType = (String)conditionMap.get("taxType");   
	Double defaultTaxRate = (Double)conditionMap.get("taxRate");
	String defaultLotNo = (String)conditionMap.get("lotNo");
	deliveryHead.setDefaultWarehouseCode(defaultWarehouseCode);
	deliveryHead.setTaxType(defaultTaxType);
	deliveryHead.setTaxRate(defaultTaxRate);
	
	List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
	if(deliveryLines != null && deliveryLines.size() > 0){
	    for(ImDeliveryLine deliveryLine : deliveryLines){
		deliveryLine.setWarehouseCode(defaultWarehouseCode);
		deliveryLine.setTaxType(defaultTaxType);
		deliveryLine.setTaxRate(defaultTaxRate);
		deliveryLine.setLotNo(defaultLotNo);
	    }
	}	
    }
    
    private ImDeliveryHead getActualDeliveryForReturn(ImDeliveryHead deliveryHead) throws Exception{
	 
        // load line
	ImDeliveryHead deliveryHeadPO = findImDeliveryHeadById(deliveryHead.getHeadId());
	if(deliveryHeadPO == null){
	    throw new ValidationErrorException("查無銷退單主鍵：" + deliveryHead.getHeadId() + "的資料！");
	}
	Double totalOriginalShipAmount = deliveryHeadPO.getTotalOriginalShipAmount();
	Double totalActualShipAmount = deliveryHeadPO.getTotalActualShipAmount();
	Double shipTaxAmount = deliveryHeadPO.getShipTaxAmount();
	
	deliveryHead.setImDeliveryLines(deliveryHeadPO.getImDeliveryLines());
	BeanUtils.copyProperties(deliveryHead, deliveryHeadPO);
	deliveryHeadPO.setTotalOriginalShipAmount(totalOriginalShipAmount);
	deliveryHeadPO.setTotalActualShipAmount(totalActualShipAmount);
	deliveryHeadPO.setShipTaxAmount(shipTaxAmount);

	return deliveryHeadPO;
    }
    
    private void copyOrigDeliveryHead(ImDeliveryHead deliveryHead, ImDeliveryHead origDeliveryHead, 
	    String origDeliveryOrderTypeCode, String origDeliveryOrderNo){
	
	deliveryHead.setOrigDeliveryOrderTypeCode(origDeliveryOrderTypeCode);
	deliveryHead.setOrigDeliveryOrderNo(origDeliveryOrderNo);
	deliveryHead.setReturnDate(origDeliveryHead.getShipDate());
	deliveryHead.setCustomerCode(origDeliveryHead.getCustomerCode());
	deliveryHead.setCustomerPoNo(origDeliveryHead.getCustomerPoNo());
	deliveryHead.setQuotationCode(origDeliveryHead.getQuotationCode());
	deliveryHead.setPaymentTermCode(origDeliveryHead.getPaymentTermCode());
	deliveryHead.setCountryCode(origDeliveryHead.getCountryCode());
	deliveryHead.setCurrencyCode(origDeliveryHead.getCurrencyCode());
	deliveryHead.setShopCode(origDeliveryHead.getShopCode());
	deliveryHead.setContactPerson(origDeliveryHead.getContactPerson());
	deliveryHead.setContactTel(origDeliveryHead.getContactTel());
	deliveryHead.setReceiver(origDeliveryHead.getReceiver());
	deliveryHead.setSuperintendentCode(origDeliveryHead.getSuperintendentCode());
	deliveryHead.setInvoiceTypeCode(origDeliveryHead.getInvoiceTypeCode());
	deliveryHead.setGuiCode(origDeliveryHead.getGuiCode());
	deliveryHead.setTaxType(origDeliveryHead.getTaxType());
	deliveryHead.setTaxRate(origDeliveryHead.getTaxRate());
	deliveryHead.setScheduleCollectionDate(origDeliveryHead.getScheduleCollectionDate());
	deliveryHead.setInvoiceCity(origDeliveryHead.getInvoiceCity());
	deliveryHead.setInvoiceArea(origDeliveryHead.getInvoiceArea());
	deliveryHead.setInvoiceZipCode(origDeliveryHead.getInvoiceZipCode());
	deliveryHead.setInvoiceAddress(origDeliveryHead.getInvoiceAddress());	
	deliveryHead.setShipCity(origDeliveryHead.getShipCity());
	deliveryHead.setShipArea(origDeliveryHead.getShipArea());
	deliveryHead.setShipZipCode(origDeliveryHead.getShipZipCode());
	deliveryHead.setShipAddress(origDeliveryHead.getShipAddress());
	deliveryHead.setDefaultWarehouseCode(origDeliveryHead.getDefaultWarehouseCode());
	deliveryHead.setPromotionCode(origDeliveryHead.getPromotionCode());
	deliveryHead.setDiscountRate(origDeliveryHead.getDiscountRate());
	deliveryHead.setSufficientQuantityDelivery(origDeliveryHead.getSufficientQuantityDelivery());
	deliveryHead.setRemark1(origDeliveryHead.getRemark1());
	deliveryHead.setRemark2(origDeliveryHead.getRemark2());
	deliveryHead.setHomeDelivery(origDeliveryHead.getHomeDelivery());
	deliveryHead.setPaymentCategory(origDeliveryHead.getPaymentCategory());
	deliveryHead.setAttachedInvoice(origDeliveryHead.getAttachedInvoice());	
    }
    
    private void sortDeliveryLine(ImDeliveryHead deliveryHead) throws Exception{
	
	List<ImDeliveryLine> deliveryLines = imDeliveryLineDAO.findByHeadId(deliveryHead.getHeadId());
	deliveryLines = StringTools.setBeanValue(deliveryLines, "indexNo", null);
	deliveryHead.setImDeliveryLines(deliveryLines);	
    }
    
    /**
     * 匯入銷退商品明細
     * 
     * @param headId
     * @param taxType
     * @param taxRate
     * @param deliveryLines
     * @throws Exception
     */
    public void executeImportItems(Long headId, Date shipDate, String priceType, String taxType, 
	        Double taxRate, Double discountRate, List deliveryLines) 
            throws Exception{
	
        try{
            ImDeliveryHead deliveryHead = findImDeliveryHeadById(headId);         
	    if(deliveryHead == null){
		throw new NoSuchObjectException("查無銷退單主鍵：" + headId + "的資料");
	    } 
	    
	    if(deliveryLines != null && deliveryLines.size() > 0){
		for(int i = 0; i < deliveryLines.size(); i++){
		    ImDeliveryLine deliveryLine = (ImDeliveryLine)deliveryLines.get(i);
		    //實際單價取整數
		    Double actualUnitPrice = deliveryLine.getActualUnitPrice();
		    if(actualUnitPrice != null){
	                actualUnitPrice = CommonUtils.round(actualUnitPrice, 0);
	                deliveryLine.setActualUnitPrice(actualUnitPrice);
	            }
		    //取得原單價
		    String itemCode = deliveryLine.getItemCode();
		    if(StringUtils.hasText(itemCode)){
	                itemCode = itemCode.trim().toUpperCase();
	                deliveryLine.setItemCode(itemCode);
	                List result = imItemPriceDAO.getItemPriceByBeginDate(deliveryHead.getBrandCode(), itemCode, priceType, shipDate, null);
	                if(result != null && result.size() > 0){
	                    Object[] objArray = (Object[])result.get(0);
	                    BigDecimal unitPrice = (BigDecimal)objArray[1];
	                    if(unitPrice != null){
	                        deliveryLine.setOriginalUnitPrice(unitPrice.doubleValue());
	                    } 
	                }
	            }
		    
		    if(deliveryLine.getOriginalUnitPrice() == null && actualUnitPrice != null){
	            	if(actualUnitPrice == 0D || discountRate == null || discountRate == 100D){
	                    deliveryLine.setOriginalUnitPrice(actualUnitPrice);
	                }else{
	                    deliveryLine.setOriginalUnitPrice(CommonUtils.round(actualUnitPrice / (discountRate/100), 0));
	                }
	            }
		    //置入taxType、taxRate
		    deliveryLine.setTaxType(taxType);         
	            deliveryLine.setTaxRate(taxRate);	           
		}
		deliveryHead.setImDeliveryLines(deliveryLines);
	    }else{
		deliveryHead.setImDeliveryLines(new ArrayList(0));
	    }
	    imDeliveryHeadDAO.update(deliveryHead);	    
        }catch (Exception ex) {
	    log.error("銷退商品明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷退商品明細匯入時發生錯誤，原因：" + ex.getMessage());
	}        
    }
    
    /**
     * 將銷貨資料更新至銷貨單主檔及明細檔(AJAX)
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updateAJAXSalesOrderReturn(Map parameterMap) throws FormException, Exception {
	
	HashMap resultMap = new HashMap();
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Long headId = getDeliveryHeadId(formLinkBean);
            ImDeliveryHead deliveryHeadPO = getActualDelivery(headId);  	    
    	    //====================取得條件資料======================
            HashMap conditionMap = getSoReturnConditionData(parameterMap);
            String beforeChangeStatus = (String)conditionMap.get("beforeChangeStatus");
	    String formStatus = (String)conditionMap.get("formStatus");
	    String employeeCode = (String)conditionMap.get("employeeCode");
	    String organizationCode = (String)conditionMap.get("organizationCode"); 
	    String origDeliveryOrderNo = (String)conditionMap.get("origDeliveryOrderNo");
	    String isCommitOnHand = (String)conditionMap.get("isCommitOnHand");
	    	    
	    if(OrderStatus.SAVE.equals(formStatus)){
		//刪除於SI_PROGRAM_LOG的原識別碼資料
	    	String identification = MessageStatus.getIdentification(deliveryHeadPO.getBrandCode(), 
	    		deliveryHeadPO.getOrderTypeCode(), deliveryHeadPO.getOrderNo());
	    	siProgramLogAction.deleteProgramLog(PROGRAM_ID_RETURN, null, identification);
		sortDeliveryLine(deliveryHeadPO);
	        AjaxUtils.copyJSONBeantoPojoBean(formBindBean, deliveryHeadPO); 
	    }else if (OrderStatus.REJECT.equals(formStatus)) {
		if(StringUtils.hasText(origDeliveryOrderNo)){
		    modifyOrigDeliveryReturnedQuantity(deliveryHeadPO);
		}
		convertToPositiveForReturn(deliveryHeadPO);	
	    }else if((OrderStatus.SAVE.equals(beforeChangeStatus) || OrderStatus.REJECT.equals(beforeChangeStatus)) &&
		    OrderStatus.SIGNING.equals(formStatus)){
		sortDeliveryLine(deliveryHeadPO);
		removeEmptyShipQuantity(deliveryHeadPO);
		Map countResultMap = countAllRelationAmountForReturn(deliveryHeadPO, conditionMap, true);
		Double lineShipTaxAmount = (Double)countResultMap.get("shipTaxAmount");		
		Double actualShipTaxAmount = deliveryHeadPO.getShipTaxAmount();
		Double balanceAmt = actualShipTaxAmount - lineShipTaxAmount;
		balanceTaxAmount(deliveryHeadPO.getImDeliveryLines(), balanceAmt);		
	    }	    
	    //更新庫存
	    if("Y".equals(isCommitOnHand)){
                List<ImDeliveryLine> deliveryLines = deliveryHeadPO.getImDeliveryLines();
		if(deliveryLines != null){
	            for(int i = 0; i < deliveryLines.size(); i++){
	                ImDeliveryLine deliveryLine = (ImDeliveryLine) deliveryLines.get(i);		    
	                Double returnQuantity = deliveryLine.getShipQuantity();
	                returnQuantity = returnQuantity * -1;
	                if(!"Y".equals(deliveryLine.getIsServiceItem())){
		            imOnHandDAO.updateOutUncommitQuantity(organizationCode, deliveryLine.getItemCode()
			            , deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), returnQuantity, employeeCode);
	                }             
		    }
		}
	    }	    	    
	    deliveryHeadPO.setStatus(formStatus);
	    String resultMsg = modifyAjaxImDelivery(deliveryHeadPO, employeeCode);
	    	    
	    resultMap.put("entityBean", deliveryHeadPO);
    	    resultMap.put("resultMsg", resultMsg);
    	    
            return resultMap;
        }catch (FormException fe) {
	    log.error("銷退單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("銷退單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷退單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    public ImDeliveryHead getActualDelivery(Long headId) throws FormException, Exception{
	       
	ImDeliveryHead deliveryHeadPO  = findImDeliveryHeadById(headId);
    	if(deliveryHeadPO == null){
    	    throw new NoSuchObjectException("查無Delivery主鍵：" + headId + "的資料！");
    	}
	return deliveryHeadPO;
    }
    
    public Long getDeliveryHeadId(Object bean) throws FormException, Exception{
	   
	Long headId = null;
	String id = (String)PropertyUtils.getProperty(bean, "headId");
        System.out.println("headId=" + id);
	if(StringUtils.hasText(id)){
            headId = NumberUtils.getLong(id);
        }else{
    	    throw new ValidationErrorException("傳入的Delivery主鍵為空值！");
        }
	   
	return headId;
    }
    
    private HashMap getSoReturnConditionData(Map parameterMap) throws FormException, Exception{
        
	Object formBindBean = parameterMap.get("vatBeanFormBind");
	Object formLinkBean = parameterMap.get("vatBeanFormLink");
	Object otherBean = parameterMap.get("vatBeanOther");
	HashMap conditionMap = new HashMap();
	//取出參數
	String origDeliveryOrderTypeCode = (String)PropertyUtils.getProperty(formBindBean, "origDeliveryOrderTypeCode");
	String origDeliveryOrderNo = (String)PropertyUtils.getProperty(formBindBean, "origDeliveryOrderNo");	
 	String brandCode = (String)PropertyUtils.getProperty(formLinkBean, "brandCode");
	String orderTypeCode = (String)PropertyUtils.getProperty(formLinkBean, "orderTypeCode");
	String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
	String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
	String priceType = (String)PropertyUtils.getProperty(otherBean, "priceType");
 	String organizationCode = (String)PropertyUtils.getProperty(otherBean, "organizationCode");
 	String isCommitOnHand = (String)PropertyUtils.getProperty(otherBean, "isCommitOnHand");
 	
 	conditionMap.put("origDeliveryOrderTypeCode", origDeliveryOrderTypeCode);
 	conditionMap.put("origDeliveryOrderNo", origDeliveryOrderNo);
 	conditionMap.put("brandCode", brandCode);
 	conditionMap.put("orderTypeCode", orderTypeCode);
 	conditionMap.put("beforeChangeStatus", beforeChangeStatus);
 	conditionMap.put("formStatus", formStatus);
 	conditionMap.put("employeeCode", employeeCode);
 	conditionMap.put("priceType", priceType);
 	conditionMap.put("organizationCode", organizationCode);
 	conditionMap.put("isCommitOnHand", isCommitOnHand);
 	
 	System.out.println("[origDeliveryOrderTypeCode]=" + origDeliveryOrderTypeCode);
 	System.out.println("[origDeliveryOrderNo]=" + origDeliveryOrderNo);
 	System.out.println("[brandCode]=" + brandCode);
 	System.out.println("[orderTypeCode]=" + orderTypeCode);
 	System.out.println("[beforeChangeStatus]=" + beforeChangeStatus);
 	System.out.println("[formStatus]=" + formStatus);
 	System.out.println("[employeeCode]=" + employeeCode);
 	System.out.println("[priceType]=" + priceType);
 	System.out.println("[organizationCode]=" + organizationCode);
 	System.out.println("[isCommitOnHand]=" + isCommitOnHand);
 	
 	return conditionMap;
    }

    /**
     * 更新至Delivery主檔
     * 
     * @param updateObj
     * @param loginUser
     */
    private void modifyImDelivery(ImDeliveryHead updateObj, String loginUser) {
	
	updateObj.setLastUpdatedBy(loginUser);
	updateObj.setLastUpdateDate(new Date());
	imDeliveryHeadDAO.update(updateObj);
    }
    
    /**
     *  取單號後更新Delivery主檔
     * 
     * @param parameterMap
     * @return Map
     * @throws FormException
     * @throws Exception
     */
    public Map updateDeliveryWithActualOrderNO(Map parameterMap) throws FormException, Exception {
        
        HashMap resultMap = new HashMap();
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
    	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    Long headId = getDeliveryHeadId(formLinkBean); 
    	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
    	    //取得欲更新的bean
    	    ImDeliveryHead deliveryHeadPO = getActualDelivery(headId);
    	    //刪除於SI_PROGRAM_LOG的原識別碼資料
    	    String identification = MessageStatus.getIdentification(deliveryHeadPO.getBrandCode(), 
    	            deliveryHeadPO.getOrderTypeCode(), deliveryHeadPO.getOrderNo());
    	    siProgramLogAction.deleteProgramLog(PROGRAM_ID_RETURN, null, identification);
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, deliveryHeadPO);
	    String resultMsg = modifyAjaxImDelivery(deliveryHeadPO, employeeCode);
    	    resultMap.put("entityBean", deliveryHeadPO);
            resultMap.put("resultMsg", resultMsg);
    	
    	    return resultMap;      
        } catch (FormException fe) {
	    log.error("Delivery存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("Delivery存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("Delivery存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
      
    /**
     * 更新檢核後的銷退單
     * 
     * @param parameterMap
     * @return List
     * @throws Exception
     */
    public List updateCheckedSalesOrderReturnData(Map parameterMap) throws Exception {
        
	List errorMsgs = new ArrayList(0);
	try{
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Long headId = getDeliveryHeadId(formLinkBean);          
            ImDeliveryHead deliveryHeadPO = getActualDelivery(headId);
            String identification = MessageStatus.getIdentification(deliveryHeadPO.getBrandCode(), 
        	    deliveryHeadPO.getOrderTypeCode(), deliveryHeadPO.getOrderNo());
            HashMap conditionMap = getSoReturnConditionData(parameterMap); 
            String employeeCode = (String)conditionMap.get("employeeCode");
            String origDeliveryOrderTypeCode = deliveryHeadPO.getOrigDeliveryOrderTypeCode();
            String origDeliveryOrderNo = deliveryHeadPO.getOrigDeliveryOrderNo();
            
            siProgramLogAction.deleteProgramLog(PROGRAM_ID_RETURN, null, identification);
            if(StringUtils.hasText(origDeliveryOrderNo)){		    
                ImDeliveryHead orgiDeliveryHead = checkImDeliveryHeadForReturn(deliveryHeadPO, conditionMap, 
                	PROGRAM_ID_RETURN, identification, errorMsgs);
                if(orgiDeliveryHead != null && errorMsgs.size() == 0){
                    copyOrigDeliveryHead(deliveryHeadPO, orgiDeliveryHead, origDeliveryOrderTypeCode, origDeliveryOrderNo);
                    checkLineDataForReturn(deliveryHeadPO, orgiDeliveryHead, conditionMap, PROGRAM_ID_RETURN,
                	    identification, errorMsgs);
                    if(errorMsgs.size() == 0){
                	modifyOrigDeliveryReturnedQuantity(deliveryHeadPO, conditionMap, PROGRAM_ID_RETURN,
                                identification, errorMsgs);
                    } 
                }
            }else{
	        deliveryHeadPO.setOrigDeliveryOrderTypeCode(null);
	        deliveryHeadPO.setOrigDeliveryOrderNo(null);	        	           	        
		String warehouseManager = checkDeliveryHeadForReturn(deliveryHeadPO, conditionMap, PROGRAM_ID_RETURN,
                        identification, errorMsgs);
		checkImDeliveryLinesForReturn(deliveryHeadPO, warehouseManager, conditionMap, PROGRAM_ID_RETURN,
                        identification, errorMsgs);
            }	    
            modifyImDelivery(deliveryHeadPO, employeeCode);
	    
	    return errorMsgs;
	}catch (Exception ex) {
	    log.error("銷退單檢核後存檔失敗，原因：" + ex.toString());
	    throw new Exception("銷退單檢核後存檔失敗，原因：" + ex.getMessage());
	}
    }    
   
    /**
     * 檢核銷退單明細(原出貨單)
     * 
     * @param deliveryHead
     * @param orgiDeliveryHead
     * @param conditionMap
     * @param programId
     * @param identification
     * @param errorMsgs
     */
    public void checkLineDataForReturn(ImDeliveryHead deliveryHead, ImDeliveryHead orgiDeliveryHead, 
	    HashMap conditionMap, String programId, String identification, List errorMsgs) {
	
	String message = null;
	String tabName = "明細資料頁籤";
	boolean isHaveReturnItem = false;
	try{
	    Long origHeadId = orgiDeliveryHead.getHeadId();//原出貨單的headId
	    String brandCode = orgiDeliveryHead.getBrandCode();
	    String origOrderTypeCode = orgiDeliveryHead.getOrderTypeCode();
	    String origOrderNo = orgiDeliveryHead.getOrderNo();
	    String origID = MessageStatus.getIdentificationMsg(brandCode, origOrderTypeCode, origOrderNo);    
            List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
            if(deliveryLines != null && deliveryLines.size() > 0){
	        for(int i = 0; i < deliveryLines.size(); i++){
	            try{
	                ImDeliveryLine deliveryLine = (ImDeliveryLine)deliveryLines.get(i);
	                if(!"1".equals(deliveryLine.getIsDeleteRecord())){
	                    Double shipQuantity = deliveryLine.getShipQuantity(); //目前欲退貨數量
	                    if(shipQuantity == null){
			        shipQuantity = 0D;
			    }
			    shipQuantity = CommonUtils.round(shipQuantity.doubleValue(), 2);
			    deliveryLine.setShipQuantity(shipQuantity);
			    if(!isHaveReturnItem && shipQuantity > 0D){
		                isHaveReturnItem = true;
		            }	                
	                    //==========================取得對應的原出貨單明細===============================	            
		            String reserve2 = deliveryLine.getReserve2();
		            if(!StringUtils.hasLength(reserve2)){
			        throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細所對應的出貨單明細資訊為空值！");
		            }
		            ImDeliveryLine origDeliveryLinePO = (ImDeliveryLine)imDeliveryLineDAO.
		                findByPrimaryKey(ImDeliveryLine.class, Long.valueOf(reserve2));
		            if(origDeliveryLinePO == null){
			        throw new ValidationErrorException("查無" + tabName + "中第" + (i + 1) + "項明細所對應的出貨單主鍵：" + Long.valueOf(reserve2) + "的資訊！");
		            }else if(origHeadId != origDeliveryLinePO.getImDeliveryHead().getHeadId()){
		                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細並非出貨單：" + origID + "的出貨明細資料！");
		            }
		            //===========================更新資訊========================================
		            deliveryLine.setReturnQuantity(origDeliveryLinePO.getShipQuantity());
		            deliveryLine.setReturnedQuantity(origDeliveryLinePO.getReturnedQuantity());
		        
		            Double returnQuantity = deliveryLine.getReturnQuantity();//原出貨數量
			    Double returnedQuantity = deliveryLine.getReturnedQuantity(); //原出貨單的已退貨量
			
			    if(returnQuantity == null){
			        returnQuantity = 0D;
			    }
			    if(returnedQuantity == null){
			        returnedQuantity = 0D;
			    }
			
			
			    double returnableQuantity = returnQuantity - returnedQuantity;//可退貨數量
			    if (returnableQuantity < shipQuantity) {
			        throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的退貨數量不可大於可退數量！");
			    }else if (shipQuantity < 0D) {
			        throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的退貨數量不可小於零！");
			    }
	                }
	            }catch(Exception ex){
		        message = ex.getMessage();
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
				deliveryHead.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);	            
		    }
	        }
	        if(!isHaveReturnItem){
	            message = tabName + "中至少一筆明細的退貨數量大於零！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
			    deliveryHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}	    
            }else{
	        message = tabName + "中至少需輸入一筆資料！";
	        siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
	        	deliveryHead.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
            } 
	} catch (Exception ex) {
	    message = "檢核銷退單" + tabName + "時發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, 
		    deliveryHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }
    
    /**
     * 更新原出貨單明細的已退數量
     * 
     * @param deliveryHead
     * @throws FormException
     * @throws Exception
     */
    private void modifyOrigDeliveryReturnedQuantity(ImDeliveryHead deliveryHead) throws FormException, Exception{
	
	String tabName = "明細資料頁籤";
	try{
	    List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
	    if(deliveryLines != null && deliveryLines.size() > 0){
	        for(int i = 0; i < deliveryLines.size(); i++){
		    ImDeliveryLine deliveryLine = (ImDeliveryLine) deliveryLines.get(i);		        
		    String origiLineId = deliveryLine.getReserve2();
        	    if(!StringUtils.hasLength(origiLineId)){
        	        throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細所對應的出貨單明細資訊為空值！");
        	    }
        	    //原出貨單明細
		    ImDeliveryLine origDeliveryLinePO = (ImDeliveryLine)imDeliveryLineDAO.findByPrimaryKey(ImDeliveryLine.class, 
	                Long.valueOf(origiLineId));
		    if(origDeliveryLinePO == null){
	                throw new ValidationErrorException("查無" + tabName + "中第" + (i + 1) + "項明細所對應的出貨單主鍵：" + 
	                        Long.valueOf(origiLineId) + "的資訊！");
	            }
		    Double returnQuantity = deliveryLine.getShipQuantity();
		    if(returnQuantity == null){
		        returnQuantity = 0D;
		    }
        	    //更新原出貨單明細已退數量     
                    Double returnedQuantity = origDeliveryLinePO.getReturnedQuantity();
                    if(returnedQuantity == null){
                        origDeliveryLinePO.setReturnedQuantity(returnQuantity);
                    }else{
                        origDeliveryLinePO.setReturnedQuantity(returnedQuantity + returnQuantity);
                    }
                    imDeliveryLineDAO.update(origDeliveryLinePO);
	        }
	    }
	} catch (FormException fe) {
	    log.error("更新原出貨單明細的已退數量失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("更新原出貨單明細的已退數量發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新原出貨單明細的已退數量發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    public static Object[] startProcessForReturn(ImDeliveryHead form) throws ProcessFailedException{       
        
        try{           
	    String packageId = "So_SalesOrder";         
            String processId = "return";           
            String version = "20090606";
            String sourceReferenceType = "SOReturn (1)";
            HashMap context = new HashMap();	    
	    context.put("formId", form.getHeadId());
	    return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
        }catch (Exception ex){
	    log.error("銷退流程啟動失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("銷退流程啟動失敗！");
	}	      
    }
    
    public static Object[] completeAssignmentForReturn(long assignmentId, boolean approveResult) throws ProcessFailedException{
	   
        try{           
	    HashMap context = new HashMap();
	    context.put("approveResult", approveResult);
	       
	    return ProcessHandling.completeAssignment(assignmentId, context);
	}catch (Exception ex){
	    log.error("完成銷退工作任務失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("完成銷退工作任務失敗！");
	}
    }
    
    public String getIdentification(Long headId) throws Exception{
	   
        String id = null;
	try{
	    ImDeliveryHead deliveryHead = findImDeliveryHeadById(headId);
	    if(deliveryHead != null){
                id = MessageStatus.getIdentification(deliveryHead.getBrandCode(), 
		        deliveryHead.getOrderTypeCode(), deliveryHead.getOrderNo());
	    }else{
	        throw new NoSuchDataException("Delivery主檔查無主鍵：" + headId + "的資料！");
	    }
	    
	    return id;
	}catch(Exception ex){
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
	}	   
    }
    
    /**
     *  更新Delivery主檔
     * 
     * @param parameterMap
     * @throws FormException
     * @throws Exception
     */
    public void updateDelivery(Map parameterMap) throws FormException, Exception {
        
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
    	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    Long headId = getDeliveryHeadId(formLinkBean); 
    	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
    	    //取得欲更新的bean
    	    ImDeliveryHead deliveryHeadPO = getActualDelivery(headId);
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, deliveryHeadPO);
	    modifyImDelivery(deliveryHeadPO, employeeCode);    
        } catch (FormException fe) {
	    log.error("Delivery存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("Delivery存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("Delivery存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
	public void getMailListMethod() throws Exception{
			
			log.info("ddd:"+"123456");
			
			throw new Exception("返回停止");
	}
    
}
