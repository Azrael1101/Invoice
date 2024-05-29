package tw.com.tm.erp.hbm.service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.ImStorageAction;
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
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuCustomerWithAddressView;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuPaymentTerm;
import tw.com.tm.erp.hbm.bean.BuPaymentTermId;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHand;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemSerial;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImPriceAdjustment;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.ImTransation;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoPostingTally;
import tw.com.tm.erp.hbm.bean.SoPostingTallyId;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCountryDAO;
import tw.com.tm.erp.hbm.dao.BuCurrencyDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.hbm.dao.BuPaymentTermDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImItemSerialDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.ImTransationDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.SoPostingTallyDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderItemDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DES;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class ImDeliveryMainService {

	private static final Log log = LogFactory.getLog(ImDeliveryMainService.class);

	public static final String PROGRAM_ID_RETURN= "SO_RETURN";

	private static final int INVOICE_LOCUS_LENGTH = 2;

	private static final String ORGANIZATION_CODE = "TM";

	public static final String PROGRAM_ID= "IM_DELIVERY";

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

	private BuShopDAO buShopDAO;

	private BuCountryDAO buCountryDAO;

	private BuPaymentTermDAO buPaymentTermDAO;

	private BuCurrencyDAO buCurrencyDAO;

	private BuBrandDAO buBrandDAO;

	private BuCommonPhraseHeadDAO buCommonPhraseHeadDAO;

	private ImDeliveryLineService imDeliveryLineService;

	private BuBasicDataService buBasicDataService;

	private BuCommonPhraseService buCommonPhraseService;

	private SiProgramLogAction siProgramLogAction;

	private ImWarehouseService imWarehouseService;

	private CmDeclarationOnHandDAO cmDeclarationOnHandDAO;

	private CmDeclarationHeadDAO cmDeclarationHeadDAO;

	private BuOrderTypeDAO buOrderTypeDAO;

	private BuShopMachineService buShopMachineService;

	private ImItemCategoryService imItemCategoryService;

	private BuCustomerWithAddressViewService buCustomerWithAddressViewService;

	private ImItemSerialDAO imItemSerialDAO;

	private BuShopService buShopService;
	
	private SoPostingTallyDAO soPostingTallyDAO;

	private ImStorageAction imStorageAction;
	
	private ImStorageService imStorageService;
	
	private BuCommonPhraseLine buCommonPhraseLine;
	
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	
	public static final String[] GRID_FIELD_NAMES = { 
		"indexNo", "itemCode", "itemCName", "warehouseCode", "warehouseName", 
		"originalForeignUnitPrice", "originalUnitPrice", "actualForeignUnitPrice", "actualUnitPrice", "salesQuantity",
		"shipQuantity", "originalForeignShipAmt", "originalShipAmount", "actualForeignShipAmt",	"actualShipAmount", 
		"shipTaxAmount", "importDeclNo", "importDeclDate", "importDeclType", "importDeclSeq",
		"lotNo", "combineCode", "lineId", "isLockRecord", "message"};

	public static final int[] GRID_FIELD_TYPES = { 
		AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = {
		"","", "", "", "",
		"", "", "", "", "",
		"", "", "", "", "",
		"", "", "", "", "",
		"", "", "", AjaxUtils.IS_LOCK_RECORD_FALSE, ""};

	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"model.headId", "model.orderTypeCode", "model.orderNo", "model.salesOrderDate", "model.customerCode",
		"model2.orderTypeCode", "model2.orderNo", "model.status", "model.lastUpdateDate"};

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {
		"", "", "", "", "",
		"", "", "", ""
	};

	public static final String[] GRID_FIELD_NAMES_RETURN = { 
		"indexNo", "itemCode", "itemCName", "warehouseCode", "warehouseName",
		"lotNo","originalForeignUnitPrice" , "originalUnitPrice", "actualForeignUnitPrice","actualUnitPrice",
		"returnableQuantity", "shipQuantity", "originalForeignShipAmt", "originalShipAmount", "actualForeignShipAmt",
		"actualShipAmount", "taxType", "taxRate", "shipTaxAmount", "isTax",
		"watchSerialNo", "watchSerialNoPicker", "reserve2", "importDeclNo", "importDeclDate", 
		"importDeclType", "importDeclSeq", "lineId", "isLockRecord", "isDeleteRecord",
		"message"};

	public static final int[] GRID_FIELD_TYPES_RETURN = { 
		AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, 
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
		AjaxUtils.FIELD_TYPE_STRING};

	public static final String[] GRID_FIELD_DEFAULT_VALUES_RETURN = {
		"", "", "", "", "",
		"", "", "", "", "", 
		"", "", "", "", "", 
		"", "", "", "", "",
		"", "", "", "", "", 
		"", "", "", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE,
		""};

	public static final String[] GRID_SEARCH_FIELD_NAMES_RETURN = { 
		"orderTypeCode", "orderNo", "shipDate", "customerName", "origDeliveryOrderTypeCode", 
		"origDeliveryOrderNo", "statusName", "lastUpdateDate", "customsNo", "headId"
	};

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES_RETURN = { 
		"", "", "", "", "",
		"", "", "", "", ""
	};


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
						"ImDeliveryLine", "salesOrderId", SalesOrderHeadId, "indexNo");

				if (imDeliveryLines != null && imDeliveryLines.size() > 0) {

					String userId = salesOrderHead.getCreatedBy();

					ImDeliveryHead deliveryHead = copySalesOrderHeadToDeliveryHead(
							salesOrderHead, userId);

					imDeliveryHeadDAO.save(deliveryHead); // save出貨單主檔

					// =====將出貨單主檔的PK儲存至明細檔，建立關連=====
					for (int i = 0; i < imDeliveryLines.size(); i++) {
						ImDeliveryLine deliveryLine = (ImDeliveryLine) imDeliveryLines.get(i);
						deliveryLine.setImDeliveryHead(deliveryHead);
						//deliveryLine.setCreatedBy(null);
						//deliveryLine.setCreationDate(null);
						deliveryLine.setLastUpdatedBy(deliveryHead.getLastUpdatedBy());
						deliveryLine.setLastUpdateDate(new Date());
						deliveryLine.setStatus(null);
						if(!"T2".equals(salesOrderHead.getBrandCode()))
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
			log.error("abcd:"+headId);
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
			HashMap conditionMap, String loginUser, List errorMsgs) throws FormException, Exception {

		String beforeChangeStatus = (String)conditionMap.get("beforeChangeStatus");
		String organizationCode = (String)conditionMap.get("organizationCode");
		String formAction = (String)conditionMap.get("formAction");
		try {
			Double lineShipTaxAmount = countAllTotalAmount(deliveryHead, conditionMap);
			Double actualShipTaxAmount = deliveryHead.getShipTaxAmount();	    
			Double balanceAmt = actualShipTaxAmount - lineShipTaxAmount;
			String identification = MessageStatus.getIdentification(deliveryHead.getBrandCode(), 
					deliveryHead.getOrderTypeCode(), deliveryHead.getOrderNo());
			String message = null;
			deliveryHead.setStatus(formAction);
			if(OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(deliveryHead.getStatus())){
				List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
				if(deliveryLines != null){
					//預計出貨數不等於實際出貨數時，將差額補回庫存
					for(ImDeliveryLine deliveryLine : deliveryLines){
						try{
							Double salesQuantity = deliveryLine.getSalesQuantity();
							Double shipQuantity = deliveryLine.getShipQuantity();
							if(salesQuantity != shipQuantity){
								if(shipQuantity > salesQuantity){
									throw new Exception("實際出貨量不可大於預計出貨量");
								}
								if(shipQuantity < 0){
									throw new Exception("實際出貨量不可為負數");
								}
								if(!"Y".equals(deliveryLine.getIsServiceItem())){
									imOnHandDAO.updateOutUncommitQuantity(organizationCode, deliveryLine.getItemCode()
											, deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), 
											(salesQuantity - shipQuantity), loginUser, deliveryHead.getBrandCode());
								}
							}
						}catch(Exception ex){
							message = ex.getMessage();
							siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, deliveryHead.getLastUpdatedBy());
							errorMsgs.add(message);
							log.error(message);
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

			if(errorMsgs.size() > 0){
				throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
			}
			imDeliveryHeadDAO.update(deliveryHead);
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
	public void insertImDelivery(Object saveObj) {
		imDeliveryHeadDAO.save(saveObj);
	}

	/**
	 * 更新至至ImDeliveryHead和ImDeliveryLine
	 * 
	 * @param updateObj
	 */
	public void modifyImDelivery(Object updateObj) {
		imDeliveryHeadDAO.update(updateObj);
	}

	/**
	 * 更新至Delivery主檔
	 * 
	 * @param updateObj
	 * @param loginUser
	 */
	public void modifyImDelivery(ImDeliveryHead updateObj, String loginUser) {
		updateObj.setLastUpdatedBy(loginUser);
		updateObj.setLastUpdateDate(new Date());
		imDeliveryHeadDAO.update(updateObj);
	}
	
	/**
	 * 更新至ImDeliveryLine
	 * 
	 * @param updateObj
	 */
	public void modifyImDeliveryLine(Object updateObj) {
		imDeliveryLineDAO.update(updateObj);
	}

	/**
	 * 合計所有Line的金額，包括原總出貨金額、總實際出貨金額、出貨稅金總額
	 * 
	 * @param deliveryHead
	 */
	public Double countAllTotalAmount(ImDeliveryHead deliveryHead, HashMap conditionMap) throws FormException, Exception {
		try {
			List<ImDeliveryLine> imDeliveryLines = deliveryHead.getImDeliveryLines();
			Double totalOriginalShipAmount = 0D;
			Double totalActualShipAmount = 0D;
			Double taxFrnAmount = 0D;
			Double shipTaxAmount = 0D;
			//Double actualShipTaxAmount = 0D;
			Double originalTotalFrnShipAmt = 0D;
			Double actualTotalFrnShipAmt = 0D;
			Double expenseForeignAmount = 0D;
			//Double expenseLocalAmount = 0D;
			Double commissionRate = NumberUtils.getDouble(deliveryHead.getExportCommissionRate());
			Double exchangeRate = NumberUtils.getDouble(deliveryHead.getExportExchangeRate());
			if(0 == exchangeRate){
				exchangeRate = 1D;
				deliveryHead.setExportExchangeRate(exchangeRate);
			}
			
			Double totalOtherFrnExpense = NumberUtils.getDouble(deliveryHead.getExportExpense());
			
			for (ImDeliveryLine deliveryLine : imDeliveryLines) {
				
				countItemRelationAmount(deliveryLine);
				
				//本幣總額
				totalOriginalShipAmount += deliveryLine.getOriginalShipAmount();
				totalActualShipAmount += deliveryLine.getActualShipAmount();
				
				//稅費
				originalTotalFrnShipAmt += deliveryLine.getOriginalForeignShipAmt();
				actualTotalFrnShipAmt += deliveryLine.getActualForeignShipAmt();
			}
			
			//小數點位數
			int dec = 2;
			//無條件進位
			boolean cellUp = false;
			//如果POS銷售單以整數計
			if("IOP".equals(deliveryHead.getOrderTypeCode())){
				dec = 0;
				cellUp = true;
			}
			
			//手續費
			expenseForeignAmount = NumberUtils.round(actualTotalFrnShipAmt * commissionRate/100, dec, cellUp);
			deliveryHead.setExpenseForeignAmount(expenseForeignAmount);
			deliveryHead.setExpenseLocalAmount(expenseForeignAmount*exchangeRate);
			
			//原始出貨金額
			deliveryHead.setOriginalTotalFrnShipAmt(NumberUtils.round(originalTotalFrnShipAmt, dec, cellUp));
			deliveryHead.setTotalOriginalShipAmount(NumberUtils.round(totalOriginalShipAmount, dec, cellUp));
			
			//實際出貨金額
			deliveryHead.setActualTotalFrnShipAmt(NumberUtils.round(actualTotalFrnShipAmt, dec, cellUp));
			deliveryHead.setTotalActualShipAmount(NumberUtils.round(totalActualShipAmount, dec, cellUp));
			
			//其他費用
			deliveryHead.setExportExpense(totalOtherFrnExpense);
			deliveryHead.setTotalOtherExpense(totalOtherFrnExpense*exchangeRate);
			
			//手續費
			taxFrnAmount = calculateTaxAmount(deliveryHead.getBrandCode(), deliveryHead.getOrderTypeCode(), 
					deliveryHead.getTaxType(), deliveryHead.getTaxRate(), actualTotalFrnShipAmt+expenseForeignAmount+totalOtherFrnExpense);
			deliveryHead.setShipTaxAmount(NumberUtils.round(taxFrnAmount*exchangeRate, dec, cellUp));
			return shipTaxAmount;
		}catch (Exception ex) {
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
		
		Double originalShipAmount = 0D; // 出貨金額
		Double actualShipAmount = 0D; // 實際出貨金額
		Double originalForeignShipAmt = 0D; // 原幣出貨金額
		Double actualForeignShipAmt = 0D; // 原幣實際出貨金額
		Double shipTaxAmount = 0D; // 出貨稅金
		Double originalForeignUnitPrice = NumberUtils.getDouble(deliveryLine.getOriginalForeignUnitPrice());
		Double actualForeignUnitPrice = NumberUtils.getDouble(deliveryLine.getActualForeignUnitPrice());
		
		Double originalUnitPrice = NumberUtils.getDouble(deliveryLine.getOriginalUnitPrice());
		Double actualUnitPrice = NumberUtils.getDouble(deliveryLine.getActualUnitPrice()); // 實際售價
		
		Double exportExchangeRate = NumberUtils.getDouble(deliveryLine.getImDeliveryHead().getExportExchangeRate());
		if(0 == exportExchangeRate)
			exportExchangeRate = 1D;
		Double shipQuantity = NumberUtils.getDouble(deliveryLine.getShipQuantity()); // 實際出貨數量
		String taxType = deliveryLine.getTaxType(); // 稅別
		Double taxRate = NumberUtils.getDouble(deliveryLine.getTaxRate()); // 稅率

		if(originalUnitPrice != null && actualUnitPrice != null && shipQuantity != null){
			//直接改為出貨數量*原單價 2011.4.11
			//originalForeignShipAmt = ( 0 == originalForeignUnitPrice && 0 != originalUnitPrice ) ? NumberUtils.round( (originalUnitPrice * shipQuantity) / exportExchangeRate, 2) : NumberUtils.round(originalForeignUnitPrice * shipQuantity, 2); // 原幣出貨金額
			originalForeignShipAmt = NumberUtils.round(originalForeignUnitPrice * shipQuantity, 2); // 原幣出貨金額
			//originalShipAmount = ( 0 == originalForeignUnitPrice && 0 != originalUnitPrice ) ? originalUnitPrice * shipQuantity : NumberUtils.round(originalForeignUnitPrice * exportExchangeRate * shipQuantity, 2); // 出貨金額
			originalShipAmount = originalUnitPrice * shipQuantity; // 出貨金額
			actualForeignShipAmt = ( 0 == originalForeignUnitPrice && 0 != originalUnitPrice ) ? NumberUtils.round( (actualUnitPrice * shipQuantity) / exportExchangeRate, 2) : NumberUtils.round(actualForeignUnitPrice * shipQuantity, 2); // 原幣實際出貨金額
			actualShipAmount = ( 0 == actualForeignUnitPrice && 0 != actualUnitPrice ) ? actualUnitPrice * shipQuantity : NumberUtils.round(actualForeignUnitPrice * exportExchangeRate * shipQuantity, 2); // 實際出貨金額
			shipTaxAmount = calculateTaxAmount(deliveryLine.getImDeliveryHead().getBrandCode(), deliveryLine.getImDeliveryHead().getOrderTypeCode(), 
					taxType, taxRate, actualShipAmount); // 出貨稅金
		}

		deliveryLine.setOriginalShipAmount(originalShipAmount);
		deliveryLine.setOriginalForeignShipAmt(originalForeignShipAmt);
		deliveryLine.setActualShipAmount(actualShipAmount);
		deliveryLine.setActualForeignShipAmt(actualForeignShipAmt);
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
			conditionMap.put("customerCode", customerCode.trim().toUpperCase());
			conditionMap.put("shipNo_Start", shipNo_Start.trim());
			conditionMap.put("shipNo_End", shipNo_End.trim());
			conditionMap.put("salesNo_Start", salesNo_Start.trim());
			conditionMap.put("salesNo_End", salesNo_End.trim());
			conditionMap.put("defaultWarehouseCode", defaultWarehouseCode.trim()
					.toUpperCase());

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
					shipQuantity = NumberUtils.round(shipQuantity, 2);
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
	 * @param isExecCovert 是否為負數
	 */
	public Map countAllRelationAmountForReturn(ImDeliveryHead deliveryHead, HashMap conditionMap, boolean isExecCovert) throws FormException, Exception {
		try {
			HashMap returnMap = new HashMap();
			List<ImDeliveryLine> imDeliveryLines = deliveryHead.getImDeliveryLines();
			Double totalOriginalShipAmount = 0D;   //原總退貨金額
			Double totalOriginalForeignShipAmt = 0D; //原幣總退貨金額
			Double totalActualShipAmount = 0D;     //總實際退貨金額
			Double totalActualForeignShipAmt = 0D; //原幣總實際退貨金額
			Double shipTaxFrnAmount = 0D;
			Double shipTaxAmount = 0D;
			String taxType = deliveryHead.getTaxType(); //稅別
			Double taxRate = deliveryHead.getTaxRate(); //稅率
			Double exportCommissionRate = NumberUtils.getDouble(deliveryHead.getExportCommissionRate())/100; //手續費
			Double exchangeRate = deliveryHead.getExportExchangeRate();
			Double totalItemQuantity = 0D;
			Double totalOtherFrnExpense = NumberUtils.getDouble(deliveryHead.getExportExpense());
			for (ImDeliveryLine deliveryLine : imDeliveryLines) {
				deliveryLine.setTaxType(taxType);//line的稅別與head相同
				deliveryLine.setTaxRate(taxRate);//line的稅率與head相同
				countItemRelationAmountForReturn(deliveryLine, isExecCovert);
				if(!AjaxUtils.IS_DELETE_RECORD_TRUE.equals(deliveryLine.getIsDeleteRecord())){
					totalOriginalShipAmount += deliveryLine.getOriginalShipAmount();
					totalOriginalForeignShipAmt +=deliveryLine.getOriginalForeignShipAmt();
					totalActualShipAmount += deliveryLine.getActualShipAmount();
					totalActualForeignShipAmt += deliveryLine.getActualForeignShipAmt();
					//shipTaxAmount += deliveryLine.getShipTaxAmount();
					totalItemQuantity += deliveryLine.getShipQuantity();		           
				}
			}

			//小數點位數
			int dec = 2;
			//無條件進位
			boolean cellUp = false;
			//如果POS銷售單以整數計
			if("IRP".equals(deliveryHead.getOrderTypeCode()) || "IBT".equals(deliveryHead.getOrderTypeCode())){
				dec = 0;
				cellUp = true;
			}
			
			//總金額 為 銷貨總金額加乘手續費再加其他費用
			Double totalFrnAmount = totalOriginalForeignShipAmt*(1+exportCommissionRate) + totalOtherFrnExpense;
			deliveryHead.setOriginalTotalFrnShipAmt(NumberUtils.round(totalFrnAmount, dec, cellUp));
			deliveryHead.setTotalOriginalShipAmount(NumberUtils.round(totalFrnAmount*exchangeRate, dec, cellUp));
			//其他費用
			deliveryHead.setTotalOtherExpense(totalOtherFrnExpense*exchangeRate);
			//實際總金額 
			Double totalActualFrnAmount = 0D;
			if("ERP".equals(deliveryHead.getOrderTypeCode()))
			{
			    //2016.04.14 Maco
			    totalActualFrnAmount = totalActualForeignShipAmt;
			}
			else
			{
			    totalActualFrnAmount = totalActualForeignShipAmt*(1+exportCommissionRate) + totalOtherFrnExpense;
			}
			deliveryHead.setActualTotalFrnShipAmt(NumberUtils.round(totalActualFrnAmount, dec, cellUp));
			deliveryHead.setTotalActualShipAmount(NumberUtils.round(totalActualFrnAmount*exchangeRate, dec, cellUp));
			//營業稅
			if("ERP".equals(deliveryHead.getOrderTypeCode()))
			{
			    //2016.04.14 Maco
			    shipTaxFrnAmount = calculateTaxAmount(deliveryHead.getBrandCode(), deliveryHead.getOrderTypeCode(), deliveryHead.getTaxType(), deliveryHead.getTaxRate(), totalActualForeignShipAmt*(1+exportCommissionRate));
			}
			else
			{
			    shipTaxFrnAmount = calculateTaxAmount(deliveryHead.getBrandCode(), deliveryHead.getOrderTypeCode(), deliveryHead.getTaxType(), deliveryHead.getTaxRate(), totalActualForeignShipAmt);
			}
			
			shipTaxAmount = NumberUtils.round(shipTaxFrnAmount*exchangeRate, dec, cellUp);
			deliveryHead.setShipTaxAmount(shipTaxAmount);
			//手續費
			deliveryHead.setExpenseForeignAmount(NumberUtils.round(totalActualFrnAmount*exportCommissionRate, dec, cellUp));
			deliveryHead.setExpenseLocalAmount(NumberUtils.round(deliveryHead.getExpenseForeignAmount()*exchangeRate, dec, cellUp));

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

		Double originalUnitPrice = NumberUtils.getDouble(deliveryLine.getOriginalUnitPrice());
		Double originalForeignUnitPrice = NumberUtils.getDouble(deliveryLine.getOriginalForeignUnitPrice());
		Double actualUnitPrice = NumberUtils.getDouble(deliveryLine.getActualUnitPrice()); // 實際售價
		Double actualForeignUnitPrice = NumberUtils.getDouble(deliveryLine.getActualForeignUnitPrice());
		Double returnQuantity = NumberUtils.getDouble(deliveryLine.getShipQuantity()); // 實際退貨數量
		if(returnQuantity != null){
			if(isExecCovert){
				returnQuantity = returnQuantity * -1;
			}
			returnQuantity =  NumberUtils.round(returnQuantity, 2);
		}else{
			returnQuantity = 0D;
		}

		deliveryLine.setShipQuantity(returnQuantity);

		Double originalReturnAmount = NumberUtils.round(originalUnitPrice * returnQuantity, 0); // 原退貨金額
		Double originalForeignReturnAmount = NumberUtils.round(originalForeignUnitPrice * returnQuantity, 2); // 原幣原退貨金額
		Double actualReturnAmount = NumberUtils.round(actualUnitPrice * returnQuantity, 0); // 實際退貨金額
		Double actualForeignReturnAmount = NumberUtils.round(actualForeignUnitPrice * returnQuantity, 2); // 原幣實際退貨金額
		String taxType = deliveryLine.getTaxType(); // 稅別
		Double taxRate = deliveryLine.getTaxRate(); // 稅率
		Double returnTaxAmount = calculateTaxAmount(deliveryLine.getImDeliveryHead().getBrandCode(), deliveryLine.getImDeliveryHead().getOrderTypeCode(), 
				taxType, taxRate, actualReturnAmount); // 退貨稅金
		deliveryLine.setOriginalShipAmount(originalReturnAmount);
		deliveryLine.setOriginalForeignShipAmt(originalForeignReturnAmount);
		deliveryLine.setActualShipAmount(actualReturnAmount);
		deliveryLine.setActualForeignShipAmt(actualForeignReturnAmount);
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

	public Double calculateTaxAmount(String brandCode, String orderTypeCode, String taxType, Double taxRate, Double actualAmount){
		BuOrderTypeId buOrderTypeId = new BuOrderTypeId(brandCode, orderTypeCode);
		BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
		Double taxAmount = 0D;
		if("3".equals(taxType) && taxRate != null && taxRate != 0D && actualAmount != null && actualAmount != 0D){
			//如果是F 則稅外加
    		if ("F".equals(buOrderType.getOrderCondition())) {
    			taxAmount = (actualAmount * taxRate) / 100;
			}else{
				Double salesAmount = actualAmount / (1 + taxRate / 100);
    			taxAmount = actualAmount - salesAmount;
			}
		}
		return NumberUtils.round(taxAmount, 2);
	}

	public String checkShipQuantity(ImDeliveryHead deliveryHead){

		String returnMsg = null;
		String tabName = "明細資料頁籤";
		List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
		if (deliveryLines != null) {
			for (int i = 0; i < deliveryLines.size(); i++) {
				ImDeliveryLine deliveryLine = (ImDeliveryLine) deliveryLines.get(i);
				Double shipQuantity = deliveryLine.getShipQuantity();
				shipQuantity = NumberUtils.round(shipQuantity, 2);
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
		log.info("executeDailyBalance..."+
			delievryHead.getBrandCode()+"-"+delievryHead.getOrderTypeCode()+delievryHead.getOrderNo());
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
				}else{
					throw new ValidationErrorException("查無" + identification + "的銷售單明細資料！");
				}

				//更新出貨單Status為CLOSE	
				delievryHead.setStatus(OrderStatus.CLOSE);
				List<ImDeliveryLine> deliveryLines = delievryHead.getImDeliveryLines();
				if (deliveryLines != null && deliveryLines.size() > 0) {
					for(int i = 0 ; i < deliveryLines.size() ; i ++){
						ImDeliveryLine deliveryLine = deliveryLines.get(i);
						deliveryLine.setStatus(OrderStatus.CLOSE);
						String itemCode = deliveryLine.getItemCode();
						if(!"Y".equals(deliveryLine.getIsServiceItem())){
							//庫存主檔的OUT_UNCOMMIT_QTY和STOCK_ON_HAND_QTY皆扣掉出貨單實際出貨數
							imOnHandDAO.updateOutOnHand(ORGANIZATION_CODE, delievryHead.getBrandCode(), itemCode, 
									deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), 
									deliveryLine.getShipQuantity(), opUser);

							//庫存主檔的OUT_UNCOMMIT_QTY和STOCK_ON_HAND_QTY皆扣掉出貨單實際出貨數
							String customsWarehouseCode = null;
							ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, delievryHead.getDefaultWarehouseCode(), "Y");
							if(null==imWarehouse){
								throw new FormException("倉庫代碼：" + delievryHead.getDefaultWarehouseCode() + "查無其對應報關倉別！");
							}else{
								customsWarehouseCode = imWarehouse.getCustomsWarehouseCode();	// FW, FD...
							}

							String declarationNo  = deliveryLine.getImportDeclNo();
							Long   declarationSeq = deliveryLine.getImportDeclSeq();
							ImItem item = imItemDAO.findItem(brandCode, itemCode);
							if(null != declarationNo && null != declarationSeq)
								cmDeclarationOnHandDAO.updateOutOnHand(declarationNo, declarationSeq, itemCode, 
										customsWarehouseCode, brandCode, deliveryLine.getShipQuantity(), opUser);
							else if("F".equals(item.getIsTax()))
								throw new ValidationErrorException("單別： " + delievryHead.getOrderNo() + "，單號： " + delievryHead.getOrderNo()
										+ "，明細第" + (i+1) + "項報關單號且報關項次皆未設定");

							Double originalUnitPrice = deliveryLine.getOriginalUnitPrice();
							String declType = new String("");
							if(StringUtils.hasText(declarationNo)){
								declType =cmDeclarationHeadDAO.getDeclarationTypeByNo(declarationNo);
								if(!StringUtils.hasText(declType))
									throw new ValidationErrorException("查無報單:"+declarationNo +"類別資料！");
							}
							//產生交易明細檔 銷貨在transation裡面算負的
							ImTransation transation = new ImTransation();
							transation.setBrandCode(brandCode);
							transation.setTransationDate(delievryHead.getShipDate());
							transation.setOrderTypeCode(orderTypeCode);
							transation.setOrderNo(orderNo);
							transation.setLineId(deliveryLine.getIndexNo());
							transation.setItemCode(itemCode);
							transation.setWarehouseCode(deliveryLine.getWarehouseCode());
							transation.setLotNo(deliveryLine.getLotNo());
							transation.setQuantity(deliveryLine.getShipQuantity() * -1);
							transation.setCostAmount(deliveryLine.getActualShipAmount() * -1);
							transation.setOrderAmount(deliveryLine.getActualShipAmount() * -1);//20100803 add by joeywu 
							transation.setCreatedBy(opUser);
							transation.setCreationDate(new Date());
							transation.setLastUpdatedBy(opUser);
							transation.setLastUpdatedDate(new Date());
							transation.setDeclarationNo(delievryHead.getExportDeclNo());
							transation.setDeclarationDate(delievryHead.getExportDeclDate());
							transation.setOriginalDeclarationNo(deliveryLine.getImportDeclNo());
							transation.setOriginalDeclarationDate(deliveryLine.getImportDeclDate());
							transation.setOriginalDeclarationSeq(deliveryLine.getImportDeclSeq());
							transation.setCustomerPoNo(delievryHead.getCustomerPoNo());
							//System.out.println("delievryHead.getCustomerPoNo() = " + delievryHead.getCustomerPoNo());
							List itemPrices = imItemPriceDAO.getItemPriceByBeginDate(brandCode, transation.getItemCode(), "1", transation.getTransationDate(), "Y");
							if(itemPrices != null && itemPrices.size() > 0){
								Object[] objArray = (Object[])itemPrices.get(0);
								BigDecimal unitPrice = (BigDecimal)objArray[1];
								if(unitPrice != null){
									transation.setOriginalPriceAmount(unitPrice.doubleValue() * transation.getQuantity());
								} 
							}
							//transation.setOriginalPriceAmount(originalUnitPrice);
							transation.setOriginalDeclarationType(declType);
							imTransationDAO.save(transation);
						}
					}
					imDeliveryHeadDAO.update(delievryHead);
					soSalesOrderHeadDAO.update(salesOrder);
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
			for(int i = 0 ; i < deliveryLines.size() ; i ++){
				ImDeliveryLine deliveryLine = deliveryLines.get(i);
				deliveryLine.setStatus(OrderStatus.CLOSE);
				String itemCode = deliveryLine.getItemCode();
				if(!"Y".equals(deliveryLine.getIsServiceItem())){
					//庫存主檔的OUT_UNCOMMIT_QTY和STOCK_ON_HAND_QTY皆增加退貨單退貨數
					imOnHandDAO.updateOutOnHand(ORGANIZATION_CODE, brandCode, itemCode, 
							deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), 
							deliveryLine.getShipQuantity(), opUser);

					//庫存主檔的OUT_UNCOMMIT_QTY和STOCK_ON_HAND_QTY皆扣掉出貨單實際出貨數
					String customsWarehouseCode = null;
					ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, delievryHead.getDefaultWarehouseCode(), "Y");
					if(null==imWarehouse){
						throw new FormException("倉庫代碼：" + delievryHead.getDefaultWarehouseCode() + "查無其對應報關倉別！");
					}else{
						customsWarehouseCode = imWarehouse.getCustomsWarehouseCode();	// FW, FD...
					}

					String declarationNo  = null;
					Long   declarationSeq = null;
					if("ERF".equals(orderTypeCode)){	//如果是保稅銷退 才要使用單頭的報關號碼
						declarationNo  = delievryHead.getExportDeclNo();
						declarationSeq = deliveryLine.getIndexNo();
					}else{
						declarationNo  = deliveryLine.getImportDeclNo();
						declarationSeq = deliveryLine.getImportDeclSeq();
					}

					ImItem item = imItemDAO.findItem(brandCode, itemCode);
					if(null != declarationNo && null != declarationSeq)
						cmDeclarationOnHandDAO.updateOutOnHand(declarationNo, declarationSeq, itemCode, 
								customsWarehouseCode, brandCode, deliveryLine.getShipQuantity(), opUser);
					else if("F".equals(item.getIsTax()))
						throw new ValidationErrorException("單別： " + delievryHead.getOrderNo() + "，單號： " + delievryHead.getOrderNo()
								+ "，明細第" + (i+1) + "項報關單號且報關項次皆未設定");

					Double originalUnitPrice = deliveryLine.getOriginalUnitPrice();
					String declType = new String("");
					if(StringUtils.hasText(declarationNo)){
						declType =cmDeclarationHeadDAO.getDeclarationTypeByNo(declarationNo);
						if(!StringUtils.hasText(declType))
							throw new ValidationErrorException("查無報單:"+declarationNo +"類別資料！");
					}
					//產生交易明細檔
					ImTransation transation = new ImTransation();
					transation.setBrandCode(brandCode);
					transation.setTransationDate(delievryHead.getShipDate());
					transation.setOrderTypeCode(orderTypeCode);
					transation.setOrderNo(orderNo);
					transation.setLineId(deliveryLine.getIndexNo());
					transation.setItemCode(itemCode);
					transation.setWarehouseCode(deliveryLine.getWarehouseCode());
					transation.setLotNo(deliveryLine.getLotNo());
					transation.setQuantity(deliveryLine.getShipQuantity() * -1);
					transation.setCostAmount(deliveryLine.getActualShipAmount()* -1);
					transation.setCreatedBy(opUser);
					transation.setCreationDate(new Date());
					transation.setLastUpdatedBy(opUser);
					transation.setLastUpdatedDate(new Date());
					transation.setDeclarationNo(delievryHead.getExportDeclNo());
					transation.setDeclarationDate(delievryHead.getExportDeclDate());
					transation.setOriginalDeclarationNo(deliveryLine.getImportDeclNo());
					transation.setOriginalDeclarationDate(deliveryLine.getImportDeclDate());
					transation.setOriginalDeclarationSeq(deliveryLine.getImportDeclSeq());
					transation.setCustomerPoNo(delievryHead.getOrigDeliveryOrderNo());
					List itemPrices = imItemPriceDAO.getItemPriceByBeginDate(brandCode, transation.getItemCode(), "1", transation.getTransationDate(), "Y");
					if(itemPrices != null && itemPrices.size() > 0){
						Object[] objArray = (Object[])itemPrices.get(0);
						BigDecimal unitPrice = (BigDecimal)objArray[1];
						if(unitPrice != null){
							transation.setOriginalPriceAmount(unitPrice.doubleValue()* transation.getQuantity());
						}
					}
					//transation.setOriginalPriceAmount(originalUnitPrice);
					transation.setOriginalDeclarationType(declType);
					imTransationDAO.save(transation);
				}
			}
			imDeliveryHeadDAO.update(delievryHead);
		}else{
			throw new ValidationErrorException("查無" + identification + "的退貨單明細資料！");
		}
	}


	public void executeDailyBalanceRevert(ImDeliveryHead delievryHead, String soStatus, String opUser) throws Exception{
		String brandCode = delievryHead.getBrandCode();
		String orderTypeCode = delievryHead.getOrderTypeCode();
		String orderNo = delievryHead.getOrderNo();
		String identification = MessageStatus.getIdentificationMsg(brandCode, orderTypeCode, orderNo);
		List<ImDeliveryLine> deliveryLines = delievryHead.getImDeliveryLines();
		if (deliveryLines != null && deliveryLines.size() > 0) {
			for(int i = 0 ; i < deliveryLines.size() ; i ++){
				ImDeliveryLine deliveryLine = deliveryLines.get(i);
				deliveryLine.setStatus(OrderStatus.SAVE);
				String itemCode = deliveryLine.getItemCode();
				if(!"Y".equals(deliveryLine.getIsServiceItem())){
					//庫存主檔的OUT_UNCOMMIT_QTY和STOCK_ON_HAND_QTY皆扣掉出貨單實際出貨數
					imOnHandDAO.updateOutOnHand(ORGANIZATION_CODE, delievryHead.getBrandCode(), itemCode, 
							deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), 
							deliveryLine.getShipQuantity() * -1, opUser);

					//庫存主檔的OUT_UNCOMMIT_QTY和STOCK_ON_HAND_QTY皆扣掉出貨單實際出貨數
					String customsWarehouseCode = null;
					ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, delievryHead.getDefaultWarehouseCode(), "Y");
					if(null==imWarehouse){
						throw new FormException("倉庫代碼：" + delievryHead.getDefaultWarehouseCode() + "查無其對應報關倉別！");
					}else{
						customsWarehouseCode = imWarehouse.getCustomsWarehouseCode();	// FW, FD...
					}

					String declarationNo  = deliveryLine.getImportDeclNo();
					Long   declarationSeq = deliveryLine.getImportDeclSeq();
					ImItem item = imItemDAO.findItem(brandCode, itemCode);
					if(null != declarationNo && null != declarationSeq)
						cmDeclarationOnHandDAO.updateOutOnHand(declarationNo, declarationSeq, itemCode, 
								customsWarehouseCode, brandCode, deliveryLine.getShipQuantity() * -1, opUser);
					else if("F".equals(item.getIsTax()))
						throw new ValidationErrorException("單別： " + delievryHead.getOrderNo() + "，單號： " + delievryHead.getOrderNo()
								+ "，明細第" + (i+1) + "項報關單號且報關項次皆未設定");
				}
			}
			deleteImTransation(delievryHead);
			delievryHead.setStatus(soStatus);
			imDeliveryHeadDAO.update(delievryHead);
		}else{
			throw new ValidationErrorException("查無" + identification + "的出貨單明細資料！");
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
			BuBrand buBrand = buBrandDAO.findById(brandCode);
			BuOrderTypeId buOrderTypeId = new BuOrderTypeId(brandCode,deliveryHead.getOrderTypeCode());
			BuOrderType buOrderType = buOrderTypeDAO.findById(buOrderTypeId);

			if(buBrand == null)
				throw new Exception("查無品牌" + brandCode + "之資訊，請洽資訊人員");
			if(buOrderType == null)
				throw new Exception("查無退貨單別" + deliveryHead.getOrderTypeCode() + "之資訊，請洽資訊人員");
			if("2".equals(buBrand.getBuBranch().getBranchCode()) && buOrderType.getTaxCode() == null)
				throw new Exception("查無退貨單別" + deliveryHead.getOrderTypeCode() + "之稅別，請洽資訊人員");

			String defaultWarehouseCode = deliveryHead.getDefaultWarehouseCode().trim().toUpperCase();
			List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
			if (deliveryLines != null && deliveryLines.size() > 0) {
				for (int i = 0; i < deliveryLines.size(); i++) {
					try{
						ImDeliveryLine deliveryLine = (ImDeliveryLine) deliveryLines.get(i);
						if(!"1".equals(deliveryLine.getIsDeleteRecord())){
							//退貨數量不可小於零
							Double returnQuantity = NumberUtils.getDouble(deliveryLine.getShipQuantity());
							returnQuantity = NumberUtils.round(returnQuantity.doubleValue(), 2);
							deliveryLine.setShipQuantity(returnQuantity);
							if(!isHaveReturnItem && returnQuantity > 0D)
								isHaveReturnItem = true;

							if (returnQuantity < 0D) 
								throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的退貨數量不可小於零！");

							String itemCode = deliveryLine.getItemCode();

							if(!StringUtils.hasText(itemCode)){
								throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的品號！");
							}else if(!StringUtils.hasText(defaultWarehouseCode)){
								throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的庫別！");
							}else if(!ValidateUtil.isEnglishAlphabetOrNumber(itemCode)){
								throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號必須為A~Z、a~z、0~9！");
							}else if(!ValidateUtil.isEnglishAlphabetOrNumber(defaultWarehouseCode)){
								throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的庫別必須為A~Z、a~z、0~9！");
							}

							ImItem imItem = imItemDAO.findItem(brandCode, itemCode);
							if(imItem == null)
								throw new NoSuchObjectException("查無" + tabName + "中第" + (i + 1) + "項明細的品號！");

							ImWarehouse warehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, defaultWarehouseCode, null);
							deliveryLine.setWarehouseCode(defaultWarehouseCode);
							deliveryLine.setIsComposeItem(imItem.getIsComposeItem());
							deliveryLine.setIsServiceItem(imItem.getIsServiceItem());

							//2010/05/12 lotNo都先設定為
							deliveryLine.setLotNo(SystemConfig.LOT_NO);

							//原單價輸入檢核
							Double originalUnitPrice = deliveryLine.getOriginalUnitPrice();
							if(originalUnitPrice == null){
								throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的原單價！");
							}else{
								originalUnitPrice = NumberUtils.round(originalUnitPrice, 0);
								if(originalUnitPrice < 0D)
									throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的原單價不可小於零！");
								deliveryLine.setOriginalUnitPrice(originalUnitPrice);
							}		

							Double actualUnitPrice = deliveryLine.getActualUnitPrice();
							if(actualUnitPrice == null){
								throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的實際單價！");
							}else{
								actualUnitPrice = NumberUtils.round(actualUnitPrice, 0);
								deliveryLine.setActualUnitPrice(actualUnitPrice);
								//if(actualUnitPrice <= 0D)
									//throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的實際單價不可小等於零！");
							}

							//if(originalUnitPrice < actualUnitPrice)
								//throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的原單價不可小於實際單價！");

							//如果有輸入手錶序號
							if(StringUtils.hasText(deliveryLine.getWatchSerialNo())){
								ImItemSerial imItemSerial = (ImItemSerial)imItemSerialDAO.
								findFirstByProperty("ImItemSerial", "and brandCode = ? and itemCode = ? and serial = ?", 
										new Object[]{brandCode, deliveryLine.getItemCode(), deliveryLine.getWatchSerialNo()});
								if(null == imItemSerial || null == imItemSerial.getIsUsed() || "N".equals(imItemSerial.getIsUsed()))
									throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的手錶序號不存在或是為不可退貨狀態！");	
							}

							if("2".equals(buBrand.getBuBranch().getBranchCode()) && !buOrderType.getTaxCode().equals(imItem.getIsTax())
									&& !"IRP".equals(deliveryHead.getOrderTypeCode()) && !"IBT".equals(deliveryHead.getOrderTypeCode()))
								throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的商品稅別不符！");

							//T2POS銷退 確認明細項中報關單號等是否正確
							if("2".equals(buBrand.getBuBranch().getBranchCode())){
								if( ("IRP".equals(deliveryHead.getOrderTypeCode()) || "IBT".equals(deliveryHead.getOrderTypeCode()) ) 
										&& "F".equals(imItem.getIsTax())){
									if(warehouse.getCustomsWarehouseCode() == null)
										throw new ValidationErrorException("查無庫別" + defaultWarehouseCode + "之海關倉代碼");
									if(deliveryLine.getImportDeclNo()==null || deliveryLine.getImportDeclSeq() == null)
										throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細中報關單號或項次不得為空值！");
									CmDeclarationOnHand cmDeclarationOnHand = cmDeclarationOnHandDAO.
									getOnHandById(deliveryLine.getImportDeclNo(), deliveryLine.getImportDeclSeq(), warehouse.getCustomsWarehouseCode(), brandCode);
									if(cmDeclarationOnHand == null){
										throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細查無該報關單庫存！");
									}else if(!itemCode.equals(cmDeclarationOnHand.getCustomsItemCode())){
										throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細與原報單庫存中品名不同！");
									}
									//T2保稅銷退 確認明細項中報關單號等是否正確
								}else if("ERF".equals(deliveryHead.getOrderTypeCode())){
									CmDeclarationOnHand cmDeclarationOnHand = cmDeclarationOnHandDAO.getOnHandById(deliveryHead.getExportDeclNo(), deliveryLine.getIndexNo(), warehouse.getCustomsWarehouseCode(), brandCode);
									if(cmDeclarationOnHand != null && !itemCode.equals(cmDeclarationOnHand.getCustomsItemCode())){
										throw new ValidationErrorException(tabName + "中明細第" + (i + 1) + "項報關單庫存項次並非該商品！");
									}
								}
							}

							//item的折扣率
							Double itemDiscountRate = null;
							if(originalUnitPrice == 0D){
								itemDiscountRate = 100D;
							}else{
								itemDiscountRate = NumberUtils.round((actualUnitPrice/originalUnitPrice) * 100, 1);
							}
							deliveryLine.setDiscountRate(itemDiscountRate);
						}
					}catch(Exception ex){
						message = ex.getMessage();
						siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
								message, deliveryHead.getLastUpdatedBy());
						errorMsgs.add(message);
						log.error(ex.toString());	            
					}
				}
				if(!isHaveReturnItem){
					message = tabName + "中至少一筆明細的退貨數量大於零！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
							message, deliveryHead.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);          
				}
			}else{
				message = tabName + "中至少需輸入一筆資料！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
						message, deliveryHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}
		}catch(Exception ex){
			message = "檢核" + tabName + "時發生錯誤，原因：" + ex.toString();
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
					message, deliveryHead.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		}
	}

	/**
	 * 檢核退貨主檔資料(ImDeliveryHead),無原出貨單
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
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
							message, deliveryHead.getLastUpdatedBy());
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
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
						message, deliveryHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}

			if(StringUtils.hasText(superintendentCode)){
				superintendentCode = superintendentCode.trim().toUpperCase();
				deliveryHead.setSuperintendentCode(superintendentCode);
				BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, superintendentCode);
				if(employeeWithAddressView == null){
					message = "查無" + tabName + "的訂單負責人！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification,
							message, deliveryHead.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}
			}else{
				deliveryHead.setSuperintendentCode(null);
			}	

			if (!StringUtils.hasText(deliveryHead.getTaxType())) {
				message = "請選擇" + tabName + "的稅別！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
						message, deliveryHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}else if(("1".equals(deliveryHead.getTaxType()) || "2".equals(deliveryHead.getTaxType())) && (deliveryHead.getTaxRate() == null || deliveryHead.getTaxRate() != 0D)){
				message = tabName + "的稅別為免稅或零稅時，其稅率應為0%！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
						message, deliveryHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}else if("3".equals(deliveryHead.getTaxType()) && (deliveryHead.getTaxRate() == null || deliveryHead.getTaxRate() != 5D)){
				message = tabName + "的稅別為應稅時，其稅率應為5%！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
						message, deliveryHead.getLastUpdatedBy());
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
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
							message, deliveryHead.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);		    
				}else{
					warehouseManager = warehouse.getWarehouseManager();
					if(!StringUtils.hasText(warehouseManager)){
						message = tabName + "的庫別：" + defaultWarehouseCode + "其倉管人員為空值！";
						siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
								message, deliveryHead.getLastUpdatedBy());
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
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
							message, deliveryHead.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}
			}

			if(deliveryHead.getDiscountRate() != null && deliveryHead.getDiscountRate() < 0D){
				message = tabName + "的折扣比率不可小於0%！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification,
						message, deliveryHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}

			//BuBrand buBrand = buBrandDAO.findById(brandCode);
			if("ERF".equals(deliveryHead.getOrderTypeCode())){
				log.info("若為T2非POS銷退 則檢查單頭之報關單號	");
				List<CmDeclarationHead> cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", deliveryHead.getExportDeclNo());
				if(cmDeclarationHeads == null || cmDeclarationHeads.size() == 0){
					message = "查無" +tabName + "的報關單";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
							message, deliveryHead.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}
			}
		}catch(Exception ex){
			message = "檢核銷退單" + tabName + "時發生錯誤，原因：" + ex.getMessage();
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, 
					message, deliveryHead.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		}
		return warehouseManager;
	}

	/**
	 * 將把資料庫數量存成正數
	 * 
	 * @throws Exception
	 */
	public void convertToPositiveForReturn(ImDeliveryHead deliveryHead){
		log.info("convertToPositiveForReturn");

		Double totalOriginalShipAmount = Math.abs(NumberUtils.getDouble(deliveryHead.getTotalOriginalShipAmount()));
		Double totalActualShipAmount = Math.abs(NumberUtils.getDouble(deliveryHead.getTotalActualShipAmount()));
		Double totalShipTaxAmount = Math.abs(NumberUtils.getDouble(deliveryHead.getShipTaxAmount()));
		Double originalTotalFrnShipAmt = Math.abs(NumberUtils.getDouble(deliveryHead.getOriginalTotalFrnShipAmt()));
		Double actualTotalFrnShipAmt = Math.abs(NumberUtils.getDouble(deliveryHead.getActualTotalFrnShipAmt()));
		Double expenseForeignAmount = Math.abs(NumberUtils.getDouble(deliveryHead.getExpenseForeignAmount()));
		Double expenseLocalAmount = Math.abs(NumberUtils.getDouble(deliveryHead.getExpenseLocalAmount()));

		deliveryHead.setTotalOriginalShipAmount(totalOriginalShipAmount);
		deliveryHead.setTotalActualShipAmount(totalActualShipAmount);
		deliveryHead.setShipTaxAmount(totalShipTaxAmount);
		deliveryHead.setOriginalTotalFrnShipAmt(originalTotalFrnShipAmt);
		deliveryHead.setActualTotalFrnShipAmt(actualTotalFrnShipAmt);
		deliveryHead.setExpenseForeignAmount(expenseForeignAmount);
		deliveryHead.setExpenseLocalAmount(expenseLocalAmount);

		List<ImDeliveryLine> imDeliveryLines = deliveryHead.getImDeliveryLines();
		if(imDeliveryLines != null){
			for(ImDeliveryLine imDeliveryLine : imDeliveryLines){
				Double shipQuantity = Math.abs(NumberUtils.getDouble(imDeliveryLine.getShipQuantity()));
				Double originalShipAmount = Math.abs(NumberUtils.getDouble(imDeliveryLine.getOriginalShipAmount()));
				Double originalForeignShipAmt = Math.abs(NumberUtils.getDouble(imDeliveryLine.getOriginalForeignShipAmt()));
				Double actualShipAmount = Math.abs(NumberUtils.getDouble(imDeliveryLine.getActualShipAmount()));
				Double actualForeignShipAmt = Math.abs(NumberUtils.getDouble(imDeliveryLine.getActualForeignShipAmt()));
				Double shipTaxAmount = Math.abs(NumberUtils.getDouble(imDeliveryLine.getShipTaxAmount()));
				imDeliveryLine.setShipQuantity(shipQuantity);
				imDeliveryLine.setOriginalShipAmount(originalShipAmount);
				imDeliveryLine.setOriginalForeignShipAmt(originalForeignShipAmt);
				imDeliveryLine.setActualShipAmount(actualShipAmount);
				imDeliveryLine.setActualForeignShipAmt(actualForeignShipAmt);
				imDeliveryLine.setShipTaxAmount(shipTaxAmount);
			}
		}
	}

	/**
	 * 客戶姓名、專櫃名稱、員工資料、國別、付款條件、幣別、庫名、活動
	 * 
	 * 
	 * @throws Exception
	 */

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
			if(StringUtils.hasText(shopCode)){
				BuShop shop = buShopDAO.findById(shopCode);
				if(shop != null){
					shopName = shopName + "-" + shop.getShopCName();
				}
				deliveryHead.setShopName(shopName);
			}
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

	/**
	 * 取得出貨單明細
	 * 
	 * @param httpRequest
	 */
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
			log.info("wareHouse is null");
		}else{
			imDeliveryLine.setWarehouseName(warehouseName);
			log.info("wareHouse is not null");
		}
	}

	/**
	 * 更新出貨單的明細
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
			Double shipQuantity = NumberUtils.getDouble(httpRequest.getProperty("shipQuantity"));
			Double originalUnitPrice = NumberUtils.getDouble(httpRequest.getProperty("originalUnitPrice"));
			Double originalForeignUnitPrice = NumberUtils.getDouble(httpRequest.getProperty("originalUnitPrice"));
			Double actualUnitPrice = NumberUtils.getDouble(httpRequest.getProperty("originalUnitPrice"));
			Double actualForeignUnitPrice = NumberUtils.getDouble(httpRequest.getProperty("originalUnitPrice"));
			String taxType = httpRequest.getProperty("taxType");
			Double taxRate = NumberUtils.getDouble(httpRequest.getProperty("taxRate"));

			ImDeliveryLine imDeliveryLine = new ImDeliveryLine();
			imDeliveryLine.setOriginalUnitPrice(originalUnitPrice);
			imDeliveryLine.setOriginalForeignUnitPrice(originalForeignUnitPrice);
			imDeliveryLine.setActualUnitPrice(actualUnitPrice);
			imDeliveryLine.setActualForeignUnitPrice(actualForeignUnitPrice);
			imDeliveryLine.setShipQuantity(shipQuantity);
			imDeliveryLine.setTaxType(taxType); //暫時塞預設值
			imDeliveryLine.setTaxRate(taxRate);  //暫時塞預設值
			countItemRelationAmount(imDeliveryLine);	    

			properties.setProperty("OriginalShipAmount", AjaxUtils.getPropertiesValue(imDeliveryLine.getOriginalShipAmount(), "0.00"));
			properties.setProperty("OriginalForeignShipAmt", AjaxUtils.getPropertiesValue(imDeliveryLine.getOriginalForeignShipAmt(), "0.00"));
			properties.setProperty("ActualShipAmount", AjaxUtils.getPropertiesValue(imDeliveryLine.getActualShipAmount(), "0.00"));
			properties.setProperty("ActualForeignShipAmt", AjaxUtils.getPropertiesValue(imDeliveryLine.getActualForeignShipAmt(), "0.00"));
			properties.setProperty("ShipTaxAmount", AjaxUtils.getPropertiesValue(imDeliveryLine.getShipTaxAmount(), "0.00"));
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
			//String formStatus = httpRequest.getProperty("formStatus");// 狀態
			HashMap conditionMap = new HashMap();
			ImDeliveryHead deliveryHeadPO = findImDeliveryHeadById(headId);	    
			if(deliveryHeadPO == null){
				throw new ValidationErrorException("查無出貨單主鍵：" + headId + "的資料！");
			}
			countAllTotalAmount(deliveryHeadPO, conditionMap);
			imDeliveryHeadDAO.update(deliveryHeadPO);
			//=========================計算商品總數================
			List<ImDeliveryLine> deliveryLines = deliveryHeadPO.getImDeliveryLines();
			Double totalItemQuantity = 0D;
			if(deliveryLines != null){
				for(ImDeliveryLine deliveryLine : deliveryLines){
					totalItemQuantity += NumberUtils.getDouble(deliveryLine.getShipQuantity());
				}
			}

			//===================================================

			Double totalOriginalShipAmount = Math.abs(NumberUtils.getDouble(deliveryHeadPO.getTotalOriginalShipAmount()));
			Double originalTotalFrnShipAmt = Math.abs(NumberUtils.getDouble(deliveryHeadPO.getOriginalTotalFrnShipAmt()));
			Double totalActualShipAmount = Math.abs(NumberUtils.getDouble(deliveryHeadPO.getTotalActualShipAmount()));
			Double actualTotalFrnShipAmt = Math.abs(NumberUtils.getDouble(deliveryHeadPO.getActualTotalFrnShipAmt()));
			Double shipTaxAmount = Math.abs(NumberUtils.getDouble(deliveryHeadPO.getShipTaxAmount()));
			Double shipTaxFrnAmount = Math.abs(NumberUtils.getDouble(deliveryHeadPO.getShipTaxAmount()/deliveryHeadPO.getExportExchangeRate()));
			Double expenseForeignAmount = Math.abs(deliveryHeadPO.getExpenseForeignAmount());
			Double expenseLocalAmount = Math.abs(deliveryHeadPO.getExpenseLocalAmount());
			Double totalOtherFrnExpense = Math.abs(deliveryHeadPO.getExportExpense());
			Double totalOtherExpense = Math.abs(deliveryHeadPO.getTotalOtherExpense());
			totalItemQuantity = Math.abs(totalItemQuantity);
			Double totalDeductionAmount = 0D;
			Double totalDeductionFrnAmount = 0D;
			Double totalNoneTaxShipAmount = 0D;
			Double totalNoneTaxFrnShipAmount = 0D;
			Double totalAmount = totalActualShipAmount + expenseLocalAmount + totalOtherExpense + shipTaxAmount;
			Double totalForeignAmount = actualTotalFrnShipAmt + expenseForeignAmount + totalOtherFrnExpense + shipTaxFrnAmount; 

			BuOrderTypeId buOrderTypeId = new BuOrderTypeId(deliveryHeadPO.getBrandCode(), deliveryHeadPO.getOrderTypeCode());
	    	BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
	    	
	    	if ("F".equals(buOrderType.getOrderCondition())){
	    		totalNoneTaxShipAmount = totalAmount - shipTaxAmount;
				totalNoneTaxFrnShipAmount = totalForeignAmount- shipTaxFrnAmount;
			}else{
				totalAmount -= shipTaxAmount;
		    	totalForeignAmount -= shipTaxFrnAmount;
				totalNoneTaxShipAmount = totalActualShipAmount - shipTaxAmount;
				totalNoneTaxFrnShipAmount = totalActualShipAmount - shipTaxFrnAmount;				
			}
			
			totalDeductionAmount = totalOriginalShipAmount - totalActualShipAmount;
			totalDeductionFrnAmount = originalTotalFrnShipAmt - actualTotalFrnShipAmt;
			
			properties.setProperty("TotalOriginalShipAmount", OperationUtils.roundToStr(totalOriginalShipAmount, 2));
			properties.setProperty("TotalActualShipAmount", OperationUtils.roundToStr(totalActualShipAmount+shipTaxAmount, 2));
			properties.setProperty("ShipTaxAmount", OperationUtils.roundToStr(shipTaxAmount, 2));
			properties.setProperty("ShipTaxFrnAmount", OperationUtils.roundToStr(shipTaxFrnAmount, 2));
			properties.setProperty("TotalNoneTaxShipAmount", OperationUtils.roundToStr(totalNoneTaxShipAmount, 2));
			properties.setProperty("TotalNoneTaxFrnShipAmount", OperationUtils.roundToStr(totalNoneTaxFrnShipAmount, 2));
			properties.setProperty("TotalItemQuantity", OperationUtils.roundToStr(totalItemQuantity, 0));
			properties.setProperty("OriginalTotalFrnShipAmt", OperationUtils.roundToStr(originalTotalFrnShipAmt, 2));
			properties.setProperty("ActualTotalFrnShipAmt", OperationUtils.roundToStr(actualTotalFrnShipAmt+shipTaxFrnAmount, 2));
			properties.setProperty("TotalDeductionAmount", OperationUtils.roundToStr(totalDeductionAmount, 2));
			properties.setProperty("TotalDeductionFrnAmount", OperationUtils.roundToStr(totalDeductionFrnAmount, 2));
			properties.setProperty("ExpenseForeignAmount", OperationUtils.roundToStr(expenseForeignAmount, 2));
			properties.setProperty("ExpenseLocalAmount", OperationUtils.roundToStr(expenseLocalAmount, 2));
			properties.setProperty("TotalOtherFrnExpense", OperationUtils.roundToStr(totalOtherFrnExpense, 2));
			properties.setProperty("TotalOtherExpense", OperationUtils.roundToStr(totalOtherExpense, 2));
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
										(salesQuantity - shipQuantity), loginUser, deliveryHead.getBrandCode());
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
		log.info("refreshDeliveryForReturn");
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
				log.info("buCustomerWithAddressViewDAO.findCustomerByType");
				BuCustomerWithAddressView customerWithAddressView = buCustomerWithAddressViewDAO.findCustomerByType
				(brandCode, customerCode, "customerCode", null);
				if(customerWithAddressView != null){
					customerName = customerWithAddressView.getShortName();
				}
				deliveryHead.setCustomerName(customerName);
			}
			//訂單負責人
			if(StringUtils.hasText(superintendentCode)){
				log.info("buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode");
				BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, superintendentCode);
				if(employeeWithAddressView != null){
					superintendentName = employeeWithAddressView.getChineseName();
				}
				deliveryHead.setSuperintendentName(superintendentName);
			}	    
			//活動代號
			if(StringUtils.hasText(promotionCode)){
				log.info("imPromotionDAO.findByBrandCodeAndPromotionCode");
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
				if(OrderStatus.SAVE.equals(formStatus) || OrderStatus.REJECT.equals(formStatus) || OrderStatus.UNCONFIRMED.equals(formStatus)){
					//可編輯狀態
					refreshReturnLineDataForModify(map, imDeliveryLines);
				}else{
					//不可編輯狀態
					refreshReturnLineDataForReadOnly(map, imDeliveryLines);
				}
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
			if(imDeliveryLine.getOriginalForeignUnitPrice() == null)
				imDeliveryLine.setOriginalForeignUnitPrice(imDeliveryLine.getOriginalUnitPrice());
			if(imDeliveryLine.getOriginalForeignSalesAmt() == null)
				imDeliveryLine.setOriginalForeignSalesAmt(imDeliveryLine.getOriginalSalesAmount());
			if(imDeliveryLine.getOriginalForeignShipAmt() == null)
				imDeliveryLine.setOriginalForeignShipAmt(imDeliveryLine.getOriginalShipAmount());
			Double originalForeignShipAmt = imDeliveryLine.getOriginalForeignShipAmt();
			Double actualForeignShipAmt = imDeliveryLine.getActualForeignShipAmt();

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
			if(originalForeignShipAmt != null && originalForeignShipAmt != 0D){
				originalForeignShipAmt = originalForeignShipAmt * -1;
				imDeliveryLine.setOriginalForeignShipAmt(originalForeignShipAmt);
			}
			if(actualForeignShipAmt != null && actualForeignShipAmt != 0D){
				actualForeignShipAmt = actualForeignShipAmt * -1;
				imDeliveryLine.setActualForeignShipAmt(actualForeignShipAmt);
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
				imDeliveryLine.setIsTax(itemPO.getIsTax());
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
		log.info("executeCopyOrigDelivery");
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try{
			//======================取得複製時所需的必要資訊========================
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
			if(headId == 0L){
				throw new ValidationErrorException("無法取得銷退單的主鍵值！");
			}
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
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
			log.info("======================get current return object==================");
			ImDeliveryHead deliveryHead = (ImDeliveryHead)imDeliveryHeadDAO.findByPrimaryKey(ImDeliveryHead.class, headId);
			if(deliveryHead == null){
				throw new NoSuchObjectException("依據主鍵：" + headId + "查無銷退單的資料！");
			}
			//======================delete delivery line==========================
			log.info("======================delete delivery line==========================");
			deleteDeliveryLine(headId);
			//======================查詢欲複製的出貨單=============================
			log.info("======================查詢欲複製的出貨單=============================");
			if(StringUtils.hasText(origDeliveryOrderTypeCode) && StringUtils.hasText(origDeliveryOrderNo)){
				ImDeliveryHead deliveryHeadPO = imDeliveryHeadDAO.findDeliveryByIdentification(brandCode, origDeliveryOrderTypeCode, origDeliveryOrderNo);
				log.info("imDeliveryHeadDAO.findDeliveryByIdentification.finish");
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
					properties.setProperty("Remark1", AjaxUtils.getPropertiesValue(deliveryHeadPO.getRemark1(), ""));
					properties.setProperty("Remark2", AjaxUtils.getPropertiesValue(deliveryHeadPO.getRemark2(), ""));	    
					properties.setProperty("HomeDelivery", AjaxUtils.getPropertiesValue(deliveryHeadPO.getHomeDelivery(), ""));
					properties.setProperty("PaymentCategory", AjaxUtils.getPropertiesValue(deliveryHeadPO.getPaymentCategory(), ""));
					properties.setProperty("AttachedInvoice", AjaxUtils.getPropertiesValue(deliveryHeadPO.getAttachedInvoice(), ""));
					properties.setProperty("ExportExchangeRate", AjaxUtils.getPropertiesValue(deliveryHeadPO.getExportExchangeRate(), ""));
					properties.setProperty("ExportCommissionRate", AjaxUtils.getPropertiesValue(deliveryHeadPO.getExportCommissionRate(), ""));
					properties.setProperty("PosMachineCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getPosMachineCode(), ""));
					properties.setProperty("CasherCode", AjaxUtils.getPropertiesValue(deliveryHeadPO.getCasherCode(), ""));
					properties.setProperty("CasherName", AjaxUtils.getPropertiesValue(UserUtils.getUsernameByEmployeeCode(deliveryHeadPO.getCasherCode()), ""));
					properties.setProperty("DepartureDate", AjaxUtils.getPropertiesValue(DateUtils.format(deliveryHeadPO.getDepartureDate(), "yyyy/MM/dd"), ""));
					properties.setProperty("FlightNo", AjaxUtils.getPropertiesValue(deliveryHeadPO.getFlightNo(), ""));
					properties.setProperty("PassportNo", AjaxUtils.getPropertiesValue(deliveryHeadPO.getPassportNo(), ""));
					properties.setProperty("LadingNo", AjaxUtils.getPropertiesValue(deliveryHeadPO.getLadingNo(), ""));
					properties.setProperty("TransactionSeqNo", AjaxUtils.getPropertiesValue(deliveryHeadPO.getTransactionSeqNo(), ""));
					properties.setProperty("SalesInvoicePage", AjaxUtils.getPropertiesValue(deliveryHeadPO.getSalesInvoicePage(), ""));
					properties.setProperty("TransactionTime", AjaxUtils.getPropertiesValue(deliveryHeadPO.getTransactionTime(), ""));
					properties.setProperty("CustomsNo", AjaxUtils.getPropertiesValue(deliveryHeadPO.getCustomsNo(), ""));
					//===============取得專櫃的下拉選項===============
					log.info("==========取得專櫃的下拉選項===========");
					BuShop shopPO = buShopDAO.findById(deliveryHeadPO.getShopCode());
					if(shopPO != null){
						List<BuShop> shops = new ArrayList<BuShop>(0);
						shops.add(shopPO);
						shops = AjaxUtils.produceSelectorData(shops, "shopCode", "shopCName", true, false);
						shopCodeArray = AjaxUtils.parseSelectorData(shops);
						log.info("shopCodeArray =" + shopCodeArray);
					}
					//===============取得預設庫別的下拉選項=============
					log.info("=========取得預設庫別的下拉選項===========");
					ImWarehouse warehousePO = (ImWarehouse)imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, deliveryHeadPO.getDefaultWarehouseCode());
					if(warehousePO != null){
						List<ImWarehouse> warehouses = new ArrayList<ImWarehouse>(0);
						warehouses.add(warehousePO);
						warehouses = AjaxUtils.produceSelectorData(warehouses, "warehouseCode", "warehouseName", true, false);
						defaultWarehouseCodeArray = AjaxUtils.parseSelectorData(warehouses);
						log.info("defaultWarehouseCodeArray =" + defaultWarehouseCodeArray);	
					}
					//================copy Line Data==================
					log.info("=============copy Line Data=================");
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
							if(NumberUtils.getDouble(newLine.getActualForeignUnitPrice()) == 0)
								newLine.setActualForeignUnitPrice(newLine.getActualUnitPrice());
							if(NumberUtils.getDouble(newLine.getOriginalForeignUnitPrice()) == 0)
								newLine.setOriginalForeignUnitPrice(newLine.getOriginalUnitPrice());
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
						log.info("================copy head data=============");
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
		properties.setProperty("CustomsNo", "");
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
		shops = AjaxUtils.produceSelectorData(shops, "shopCode", "shopCName", true, false);
		shopCodeArray = AjaxUtils.parseSelectorData(shops);
		log.info("shopCodeArray =" + shopCodeArray);
		map.put("shopCodeArray", shopCodeArray);
		//===============取得預設庫別的下拉選項=============
		List<ImWarehouse> warehouses = imWarehouseDAO.getWarehouseByWarehouseEmployee(brandCode, employeeCode, null);
		warehouses = AjaxUtils.produceSelectorData(warehouses, "warehouseCode", "warehouseName", true, false);
		defaultWarehouseCodeArray = AjaxUtils.parseSelectorData(warehouses);
		log.info("defaultWarehouseCodeArray =" + defaultWarehouseCodeArray);
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
			if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status) || OrderStatus.UNCONFIRMED.equals(status)) {
				String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
				int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
				int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
				Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
				if(headId == null){
					throw new ValidationErrorException("傳入的銷退單主鍵為空值！");
				}
				ImDeliveryHead deliveryHead = this.findImDeliveryHeadById(headId);
				//deliveryHead.setHeadId(headId);
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
								if(!"ERF".equals(deliveryHead.getOrderTypeCode())){
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
			Double originalForeignUnitPrice = NumberUtils.getDouble(httpRequest.getProperty("originalForeignUnitPrice"));
			Double actualForeignUnitPrice = NumberUtils.getDouble(httpRequest.getProperty("actualForeignUnitPrice"));
			Double returnQuantity = NumberUtils.getDouble(httpRequest.getProperty("returnQuantity"));
			String exportExchangeRate = httpRequest.getProperty("exportExchangeRate");
			Double exchangeRate = NumberUtils.getDouble(exportExchangeRate)==0D?1D:NumberUtils.getDouble(exportExchangeRate);
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 稅率

			HashMap map = new HashMap();
			map.put("brandCode", brandCode);
			ImDeliveryHead imDeliveryHead = findImDeliveryHeadById(headId);
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
						imDeliveryLine.setOriginalForeignUnitPrice(unitPrice.doubleValue()/exchangeRate);
					}
				}
			}else {
				imDeliveryLine.setOriginalUnitPrice(originalForeignUnitPrice*exchangeRate);
				imDeliveryLine.setOriginalForeignUnitPrice(originalForeignUnitPrice);
				imDeliveryLine.setActualUnitPrice(actualForeignUnitPrice*exchangeRate);
				imDeliveryLine.setActualForeignUnitPrice(actualForeignUnitPrice);
				imDeliveryLine.setWatchSerialNo(watchSerialNo);
			}

			imDeliveryLine.setShipQuantity(returnQuantity);
			imDeliveryLine.setTaxType(taxType);
			imDeliveryLine.setTaxRate(taxRate);
			imDeliveryLine.setImDeliveryHead(imDeliveryHead);
			countItemRelationAmountForReturn(imDeliveryLine, false);	    
			properties.setProperty("ItemCode", AjaxUtils.getPropertiesValue(imDeliveryLine.getItemCode(), ""));
			properties.setProperty("ItemCName", AjaxUtils.getPropertiesValue(imDeliveryLine.getItemCName(), ""));
			properties.setProperty("IsTax", AjaxUtils.getPropertiesValue(imDeliveryLine.getIsTax(), ""));
			properties.setProperty("WarehouseCode", AjaxUtils.getPropertiesValue(imDeliveryLine.getWarehouseCode(), ""));
			properties.setProperty("WarehouseName", AjaxUtils.getPropertiesValue(imDeliveryLine.getWarehouseName(), ""));
			properties.setProperty("OriginalUnitPrice", AjaxUtils.getPropertiesValue(imDeliveryLine.getOriginalUnitPrice(), ""));
			properties.setProperty("OriginalForeignUnitPrice", AjaxUtils.getPropertiesValue(imDeliveryLine.getOriginalForeignUnitPrice(), ""));
			properties.setProperty("ActualUnitPrice", AjaxUtils.getPropertiesValue(imDeliveryLine.getActualUnitPrice(), ""));
			properties.setProperty("ActualForeignUnitPrice", AjaxUtils.getPropertiesValue(imDeliveryLine.getActualForeignUnitPrice(), ""));
			properties.setProperty("ShipQuantity", AjaxUtils.getPropertiesValue(imDeliveryLine.getShipQuantity(), ""));
			properties.setProperty("OriginalShipAmount", AjaxUtils.getPropertiesValue(imDeliveryLine.getOriginalShipAmount(), ""));
			properties.setProperty("OriginalForeignShipAmt", AjaxUtils.getPropertiesValue(imDeliveryLine.getOriginalForeignShipAmt(),""));
			properties.setProperty("ActualShipAmount", AjaxUtils.getPropertiesValue(imDeliveryLine.getActualShipAmount(), ""));
			properties.setProperty("ActualForeignShipAmt", AjaxUtils.getPropertiesValue(imDeliveryLine.getActualForeignShipAmt(), ""));
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
	public List<Properties> updateTotalAmountForReturn(Properties httpRequest) throws ValidationErrorException {
		try{
			List<Properties> result = new ArrayList();
			Properties properties = new Properties();
			//===================取得傳遞的的參數===================
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
			//String formStatus = httpRequest.getProperty("formStatus");// 狀態
			String taxType = httpRequest.getProperty("taxType");// 稅率
			Double taxRate = NumberUtils.getDouble(httpRequest.getProperty("taxRate"));// 稅率
			Double exportCommissionRate = NumberUtils.getDouble(httpRequest.getProperty("exportCommissionRate"));// 手續費率
			Double exportExchangeRate = NumberUtils.getDouble(httpRequest.getProperty("exportExchangeRate"));// 幣別費率
			if(0 == exportExchangeRate)
				exportExchangeRate = 1D;
			Double exportExpense = NumberUtils.getDouble(httpRequest.getProperty("exportExpense"));// 其他費用
			Double totalItemQuantity = 0D;
			HashMap conditionMap = new HashMap();	    
			ImDeliveryHead deliveryHeadPO = findImDeliveryHeadById(headId);	    
			if(deliveryHeadPO == null){
				throw new ValidationErrorException("查無銷退單主鍵：" + headId + "的資料！");
			}
			
			String formStatus = deliveryHeadPO.getStatus();
			deliveryHeadPO.setExportCommissionRate(exportCommissionRate);
			deliveryHeadPO.setTaxType(taxType);
			deliveryHeadPO.setTaxRate(taxRate);
			deliveryHeadPO.setExportExpense(exportExpense);
			deliveryHeadPO.setExportExchangeRate(exportExchangeRate);
			if(OrderStatus.SAVE.equals(formStatus) || OrderStatus.REJECT.equals(formStatus) || OrderStatus.UNCONFIRMED.equals(formStatus)){
				Map countResultMap = countAllRelationAmountForReturn(deliveryHeadPO, conditionMap, false);
				totalItemQuantity = (Double)countResultMap.get("totalItemQuantity");
				imDeliveryHeadDAO.update(deliveryHeadPO);
			}else{
				//convertToPositiveForReturn(deliveryHeadPO);
				//=========================計算商品總數================
				List<ImDeliveryLine> deliveryLines = deliveryHeadPO.getImDeliveryLines();
				if(deliveryLines != null){
					for(ImDeliveryLine deliveryLine : deliveryLines){
						if(!AjaxUtils.IS_DELETE_RECORD_TRUE.equals(deliveryLine.getIsDeleteRecord())){
							totalItemQuantity += NumberUtils.getDouble(deliveryLine.getShipQuantity());
						}
					}
				}
			}

			//=============================================================

			Double totalOriginalShipAmount = Math.abs(NumberUtils.getDouble(deliveryHeadPO.getTotalOriginalShipAmount()));
			Double originalTotalFrnShipAmt = Math.abs(NumberUtils.getDouble(deliveryHeadPO.getOriginalTotalFrnShipAmt()));
			Double totalActualShipAmount = Math.abs(NumberUtils.getDouble(deliveryHeadPO.getTotalActualShipAmount()));
			Double actualTotalFrnShipAmt = Math.abs(NumberUtils.getDouble(deliveryHeadPO.getActualTotalFrnShipAmt()));
			Double shipTaxAmount = Math.abs(NumberUtils.getDouble(deliveryHeadPO.getShipTaxAmount()));
			Double shipTaxFrnAmount = Math.abs(NumberUtils.getDouble(deliveryHeadPO.getShipTaxAmount()/deliveryHeadPO.getExportExchangeRate()));
			Double expenseForeignAmount = Math.abs(deliveryHeadPO.getExpenseForeignAmount());
			Double expenseLocalAmount = Math.abs(deliveryHeadPO.getExpenseLocalAmount());
			Double totalOtherFrnExpense = Math.abs(deliveryHeadPO.getExportExpense());
			Double totalOtherExpense = Math.abs(deliveryHeadPO.getExportExpense())*exportExchangeRate;
			totalItemQuantity = Math.abs(totalItemQuantity);
			Double totalDeductionAmount = 0D;
			Double totalDeductionFrnAmount = 0D;
			Double totalNoneTaxShipAmount = 0D;
			Double totalNoneTaxFrnShipAmount = 0D;
			Double totalForeignAmount = actualTotalFrnShipAmt + expenseForeignAmount + totalOtherFrnExpense + shipTaxFrnAmount; 
			Double totalAmount = totalActualShipAmount + expenseLocalAmount + totalOtherExpense + shipTaxAmount;

			if(!"T2".equals(deliveryHeadPO.getBrandCode())){
				totalNoneTaxShipAmount = totalActualShipAmount - shipTaxAmount;
				totalNoneTaxFrnShipAmount = totalActualShipAmount - shipTaxFrnAmount;
			}else{
				totalNoneTaxShipAmount = totalAmount - shipTaxAmount;
				totalNoneTaxFrnShipAmount = totalForeignAmount- shipTaxFrnAmount;
			}
			
			totalDeductionAmount = totalOriginalShipAmount - totalActualShipAmount;
			totalDeductionFrnAmount = originalTotalFrnShipAmt - actualTotalFrnShipAmt;

			properties.setProperty("TotalOriginalShipAmount", OperationUtils.roundToStr(totalOriginalShipAmount, 2));
			properties.setProperty("TotalActualShipAmount", OperationUtils.roundToStr(totalActualShipAmount, 2));
			properties.setProperty("ShipTaxAmount", OperationUtils.roundToStr(shipTaxAmount, 2));
			properties.setProperty("ShipTaxFrnAmount", OperationUtils.roundToStr(shipTaxFrnAmount, 2));
			properties.setProperty("TotalNoneTaxShipAmount", OperationUtils.roundToStr(totalNoneTaxShipAmount, 2));
			properties.setProperty("TotalNoneTaxFrnShipAmount", OperationUtils.roundToStr(totalNoneTaxFrnShipAmount, 2));
			properties.setProperty("TotalItemQuantity", OperationUtils.roundToStr(totalItemQuantity, 0));
			properties.setProperty("OriginalTotalFrnShipAmt", OperationUtils.roundToStr(originalTotalFrnShipAmt, 2));
			properties.setProperty("ActualTotalFrnShipAmt", OperationUtils.roundToStr(actualTotalFrnShipAmt, 2));
			properties.setProperty("TotalDeductionAmount", OperationUtils.roundToStr(totalDeductionAmount, 2));
			properties.setProperty("TotalDeductionFrnAmount", OperationUtils.roundToStr(totalDeductionFrnAmount, 2));
			properties.setProperty("ExpenseForeignAmount", OperationUtils.roundToStr(expenseForeignAmount, 2));
			properties.setProperty("ExpenseLocalAmount", OperationUtils.roundToStr(expenseLocalAmount, 2));
			properties.setProperty("TotalOtherFrnExpense", OperationUtils.roundToStr(totalOtherFrnExpense, 2));
			properties.setProperty("TotalOtherExpense", OperationUtils.roundToStr(totalOtherExpense, 2));
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
					modifyOrigDeliveryReturnedQuantity(deliveryHeadPO);
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
					}else{
						origDeliveryOrderNo = null;
						deliveryHeadPO.setOrigDeliveryOrderNo(origDeliveryOrderNo);
						deliveryHeadPO.setOrigDeliveryOrderTypeCode(null);	           	        
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
					updateReturnOnHand(deliveryHeadPO, deliveryHeadPO.getCreatedBy(), organizationCode, false);
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
				shipQuantity = NumberUtils.round(shipQuantity.doubleValue(), 2);
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
			String serialNo = buOrderTypeService.getOrderSerialNo(deliveryHead.getBrandCode(), deliveryHead.getOrderTypeCode());
			
			//for 儲位用
			if(imStorageAction.isStorageExecute(deliveryHead)){
				//取得儲位單正式的單號 2011.11.11 by Caspar
				ImStorageHead imStorageHead = imStorageAction.updateOrderNo(deliveryHead);

				//更新儲位單SOURCE ORDER_NO
				imStorageHead.setSourceOrderNo(serialNo);
				imStorageService.updateHead(imStorageHead, loginUser);
			}
			
			if (!serialNo.equals("unknow")) {
				deliveryHead.setOrderNo(serialNo);
			} else {
				throw new ObtainSerialNoFailedException("取得" + deliveryHead.getOrderTypeCode() + "單號失敗！");
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
				String priceType = httpRequest.getProperty("priceType");//價格類型
				String exportExchangeRate = httpRequest.getProperty("exportExchangeRate");// 幣別費率
				Double exchangeRate = NumberUtils.getDouble(exportExchangeRate)==0D?1D:NumberUtils.getDouble(exportExchangeRate);
				HashMap map = new HashMap();
				map.put("brandCode", brandCode);
				map.put("warehouseCode", defaultWarehouseCode);
				map.put("taxType", taxType);
				map.put("taxRate", taxRate);
				map.put("lotNo", GRID_FIELD_DEFAULT_VALUES_RETURN[5]);
				map.put("returnDateTmp", returnDateTmp);
				map.put("priceType", priceType);
				map.put("exchangeRate", exchangeRate);
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
		String priceType = (String)conditionMap.get("priceType");
		Date returnDateTmp = (Date)conditionMap.get("returnDateTmp");
		Double exchangeRate = (Double)conditionMap.get("exchangeRate");
		deliveryHead.setDefaultWarehouseCode(defaultWarehouseCode);
		deliveryHead.setTaxType(defaultTaxType);
		deliveryHead.setTaxRate(defaultTaxRate);

		List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
		if(deliveryLines != null && deliveryLines.size() > 0){
			for(ImDeliveryLine deliveryLine : deliveryLines){
				List itemPrices = imItemPriceDAO.getItemPriceByBeginDate(deliveryHead.getBrandCode(), deliveryLine.getItemCode(), priceType, returnDateTmp, null);
				if(itemPrices != null && itemPrices.size() > 0){
					Object[] objArray = (Object[])itemPrices.get(0);
					BigDecimal unitPrice = (BigDecimal)objArray[1];
					if(unitPrice != null){
						deliveryLine.setOriginalUnitPrice(unitPrice.doubleValue());
						deliveryLine.setOriginalForeignUnitPrice(unitPrice.doubleValue()/exchangeRate);
					}
				}
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
		deliveryHead.setCustomsNo(origDeliveryHead.getCustomsNo());
		deliveryHead.setQuotationCode(origDeliveryHead.getQuotationCode());
		deliveryHead.setPaymentTermCode(origDeliveryHead.getPaymentTermCode());
		deliveryHead.setCountryCode(origDeliveryHead.getCountryCode());
		deliveryHead.setCurrencyCode(origDeliveryHead.getCurrencyCode());
		//deliveryHead.setShopCode(origDeliveryHead.getShopCode());
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
		//deliveryHead.setDefaultWarehouseCode(origDeliveryHead.getDefaultWarehouseCode());
		deliveryHead.setPromotionCode(origDeliveryHead.getPromotionCode());
		deliveryHead.setDiscountRate(origDeliveryHead.getDiscountRate());
		deliveryHead.setSufficientQuantityDelivery(origDeliveryHead.getSufficientQuantityDelivery());
		deliveryHead.setRemark1(origDeliveryHead.getRemark1());
		deliveryHead.setRemark2(origDeliveryHead.getRemark2());
		deliveryHead.setHomeDelivery(origDeliveryHead.getHomeDelivery());
		deliveryHead.setPaymentCategory(origDeliveryHead.getPaymentCategory());
		deliveryHead.setAttachedInvoice(origDeliveryHead.getAttachedInvoice());
		deliveryHead.setCustomsNo(origDeliveryHead.getCustomsNo());
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
			Double taxRate, Double discountRate, Double exportExchangeRate, List deliveryLines) 
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
					deliveryLine.setActualForeignUnitPrice(NumberUtils.round(NumberUtils.getDouble(deliveryLine.getActualForeignUnitPrice()), 0));
					Double actualUnitPrice = NumberUtils.getDouble(deliveryLine.getActualForeignUnitPrice()*exportExchangeRate);
					actualUnitPrice = NumberUtils.round(actualUnitPrice, 0);
					deliveryLine.setActualUnitPrice(actualUnitPrice);
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
								deliveryLine.setOriginalForeignUnitPrice(unitPrice.doubleValue()/exportExchangeRate);
							} 
						}
					}
					if(deliveryLine.getOriginalUnitPrice() == null && actualUnitPrice != null){
						if(actualUnitPrice == 0D || discountRate == null || discountRate == 100D){
							deliveryLine.setOriginalUnitPrice(actualUnitPrice);
							deliveryLine.setOriginalForeignUnitPrice(actualUnitPrice/exportExchangeRate);
						}else{
							deliveryLine.setOriginalUnitPrice(NumberUtils.round(actualUnitPrice / (discountRate/100), 0));
							deliveryLine.setOriginalForeignUnitPrice(NumberUtils.round((actualUnitPrice/exportExchangeRate) / (discountRate/100), 0));
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

			if(OrderStatus.SAVE.equals(formStatus) || OrderStatus.VOID.equals(formStatus)){

			}else if (OrderStatus.REJECT.equals(formStatus)) {
				//駁回
				if(StringUtils.hasText(origDeliveryOrderNo)){
					modifyOrigDeliveryReturnedQuantity(deliveryHeadPO);
				}
				convertToPositiveForReturn(deliveryHeadPO);
				
			}else if(OrderStatus.UNCONFIRMED.equals(formStatus)){
				//反確認
				executeReverter(headId, employeeCode);

			}else if(!OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus)){
				removeEmptyShipQuantity(deliveryHeadPO);
				Map countResultMap = countAllRelationAmountForReturn(deliveryHeadPO, conditionMap, true);
				Double lineShipTaxAmount = NumberUtils.getDouble((Double)countResultMap.get("shipTaxAmount"));
				Double actualShipTaxAmount = NumberUtils.getDouble(deliveryHeadPO.getShipTaxAmount());
				Double balanceAmt = actualShipTaxAmount - lineShipTaxAmount;
				balanceTaxAmount(deliveryHeadPO.getImDeliveryLines(), balanceAmt);
				
			}else if(OrderStatus.SIGNING.equals(formStatus)){
				//更新庫存
				if("Y".equals(isCommitOnHand)){
					
					//for 儲位用
					if(imStorageAction.isStorageExecute(deliveryHeadPO)){
						//取單號後，扣庫存前，執行更新儲位單頭與單身，比對單據明細與儲位明細
						executeStorage(deliveryHeadPO);
					}
					
					updateReturnOnHand(deliveryHeadPO, employeeCode, organizationCode, false);
					
				  	//for 儲位用
					if(imStorageAction.isStorageExecute(deliveryHeadPO)){
						//異動儲位庫存
						imStorageService.updateStorageOnHandBySource(deliveryHeadPO, deliveryHeadPO.getStatus(), PROGRAM_ID, null, false);
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

	/**
	 * 將把庫存資料以及手錶序號還原
	 * 
	 * @param isReject true代表還原
	 * @throws Exception
	 */
	public void updateReturnOnHand(ImDeliveryHead deliveryHeadPO, String employeeCode, String organizationCode, boolean isReject) throws Exception{
		log.info("updateReturnOnHand");
		
		ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(deliveryHeadPO.getBrandCode(), deliveryHeadPO.getDefaultWarehouseCode(), "Y");
		if(imWarehouse == null){
			throw new Exception("查無庫別:" + deliveryHeadPO.getShopCode());
		}
		
		List<ImDeliveryLine> deliveryLines = deliveryHeadPO.getImDeliveryLines();
		if(deliveryLines != null){
			for(int i = 0; i < deliveryLines.size(); i++){
				ImDeliveryLine deliveryLine = (ImDeliveryLine) deliveryLines.get(i);
				Double returnQuantity = deliveryLine.getShipQuantity();
				//signing加out，finish扣out，close加stock
				if(!isReject)
					returnQuantity = returnQuantity * -1;
				if(!"Y".equals(deliveryLine.getIsServiceItem())){
					if("T2".equals(deliveryHeadPO.getBrandCode()) && "F".equals(deliveryLine.getIsTax())){
						String declNo = null;
						Long seq = null;
						//反確認也用這一隻
						//若為T2POS銷退且商品需要退回CM庫存則退回Line之cm庫存
						//if("IOP".equals(deliveryHeadPO.getOrderTypeCode()) || "IRP".equals(deliveryHeadPO.getOrderTypeCode()) || "IBT".equals(deliveryHeadPO.getOrderTypeCode())){
							declNo = deliveryLine.getImportDeclNo();
							seq = deliveryLine.getImportDeclSeq();
							//若為T2非POS銷退 則退回單頭之cm庫存
						//}else if("ESF".equals(deliveryHeadPO.getOrderTypeCode()) || "ERF".equals(deliveryHeadPO.getOrderTypeCode())){
							//declNo = deliveryHeadPO.getExportDeclNo();
							//seq = deliveryLine.getIndexNo();
						//}

						if(null != declNo && null != seq){
							//結案的話庫存扣StockonHand
							if(OrderStatus.CLOSE.equals(deliveryHeadPO.getStatus()))
								cmDeclarationOnHandDAO.updateStockOnHand(declNo, seq, deliveryLine.getItemCode(), 
										imWarehouse.getCustomsWarehouseCode(), deliveryHeadPO.getBrandCode(), returnQuantity, employeeCode);
							else
								cmDeclarationOnHandDAO.updateOutUncommitQuantity(declNo, seq, deliveryLine.getItemCode(), 
										imWarehouse.getCustomsWarehouseCode(), deliveryHeadPO.getBrandCode(), returnQuantity, employeeCode);
						}else{
							throw new Exception("查無品號" + deliveryLine.getItemCode() + "設置之報關單別單號");
						}
					}
					
					//結案的話庫存扣StockonHand
					if(OrderStatus.CLOSE.equals(deliveryHeadPO.getStatus()))
						imOnHandDAO.updateStockOnHand(organizationCode, deliveryHeadPO.getBrandCode(), deliveryLine.getItemCode(), 
								deliveryLine.getWarehouseCode(),  deliveryLine.getLotNo(), returnQuantity, employeeCode);
					else
						imOnHandDAO.updateOutUncommitQuantity(organizationCode, deliveryLine.getItemCode(), deliveryLine.getWarehouseCode(), 
								deliveryLine.getLotNo(), returnQuantity, employeeCode, deliveryHeadPO.getBrandCode());
				}
				if(StringUtils.hasText(deliveryLine.getWatchSerialNo())){
					ImItemSerial imItemSerial = (ImItemSerial)imItemSerialDAO.
					findFirstByProperty("ImItemSerial", "and brandCode = ? and itemCode = ? and serial = ?", 
							new Object[]{deliveryHeadPO.getBrandCode(), deliveryLine.getItemCode(), deliveryLine.getWatchSerialNo()});
					if(isReject)
						imItemSerial.setIsUsed("Y");
					else
						imItemSerial.setIsUsed("N");
					imItemSerial.setLastUpdateDate(new Date());
					imItemSerialDAO.update(imItemSerial);
				}
			}
		}
		
		if(isReject)
			deliveryHeadPO.setWarehouseStatus("SAVE");
		else
			deliveryHeadPO.setWarehouseStatus("FINISH");
	}

	/**
	 * 執行反確認，把該退還的東西退回
	 */
	public void executeUnconfiremd(ImDeliveryHead deliveryHeadPO, String employeeCode) throws Exception {
		ValidateUtil.isAfterClose(deliveryHeadPO.getBrandCode(), deliveryHeadPO.getOrderTypeCode(), "銷退日期", deliveryHeadPO.getShipDate(),deliveryHeadPO.getSchedule());
		if(OrderStatus.FINISH.equals(deliveryHeadPO.getStatus()) || OrderStatus.CLOSE.equals(deliveryHeadPO.getStatus())){
			updateReturnOnHand(deliveryHeadPO, employeeCode, "TM", true);
			if(StringUtils.hasText(deliveryHeadPO.getOrigDeliveryOrderNo())){
				modifyOrigDeliveryReturnedQuantity(deliveryHeadPO);
			}
			convertToPositiveForReturn(deliveryHeadPO);
		}
		//如果已經結案了，砍掉transation
		if(OrderStatus.CLOSE.equals(deliveryHeadPO.getStatus())){
			deleteImTransation(deliveryHeadPO);
		}
	}

	/**
	 * 砍掉交易記錄
	 */
	public void deleteImTransation(ImDeliveryHead deliveryHeadPO){
		List<ImTransation> trans = imTransationDAO.findTransationByIdentification( 
				deliveryHeadPO.getBrandCode(), deliveryHeadPO.getOrderTypeCode(), deliveryHeadPO.getOrderNo());
		for (Iterator iterator = trans.iterator(); iterator.hasNext();) {
			ImTransation imTransation = (ImTransation) iterator.next();
			imTransationDAO.delete(imTransation);
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
		log.info("getDeliveryHeadId headId=" + id);
		if(StringUtils.hasText(id)){
			headId = NumberUtils.getLong(id);
		}else{
			throw new ValidationErrorException("傳入的Delivery主鍵為空值！");
		}

		return headId;
	}

	private HashMap getSoReturnConditionData(Map parameterMap) throws FormException, Exception{

		Object formBindBean = parameterMap.get("vatBeanFormBind");
		Object otherBean = parameterMap.get("vatBeanOther");
		HashMap conditionMap = new HashMap();
		//取出參數
		String origDeliveryOrderTypeCode = (String)PropertyUtils.getProperty(formBindBean, "origDeliveryOrderTypeCode");
		String origDeliveryOrderNo = (String)PropertyUtils.getProperty(formBindBean, "origDeliveryOrderNo");	
		String brandCode = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
		String orderTypeCode = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");
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
	 *  更新Delivery主檔
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws FormException
	 * @throws Exception
	 */
	public Map updateDelivery(Map parameterMap) throws FormException, Exception {

		HashMap resultMap = new HashMap();
		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			Long headId = getDeliveryHeadId(formLinkBean); 
			String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
			//取得欲更新的bean
			ImDeliveryHead deliveryHeadPO = getActualDelivery(headId);
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, deliveryHeadPO);
			HashMap conditionMap = new HashMap();
			countAllTotalAmount(deliveryHeadPO, conditionMap);
			modifyImDelivery(deliveryHeadPO, employeeCode);
			resultMap.put("entityBean", deliveryHeadPO);
			String identification = MessageStatus.getIdentification(deliveryHeadPO.getBrandCode(), 
					deliveryHeadPO.getOrderTypeCode(), deliveryHeadPO.getOrderNo());
			siProgramLogAction.deleteProgramLog(PROGRAM_ID_RETURN, null, identification);
			return resultMap;      
		}catch (Exception ex) {
			log.error("Delivery存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception(ex.getMessage());
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
			if(StringUtils.hasText(origDeliveryOrderNo)){
				ImDeliveryHead orgiDeliveryHead = checkImDeliveryHeadForReturn(deliveryHeadPO, conditionMap, 
						PROGRAM_ID_RETURN, identification, errorMsgs);
				if(orgiDeliveryHead != null && errorMsgs.size() == 0){
					copyOrigDeliveryHead(deliveryHeadPO, orgiDeliveryHead, origDeliveryOrderTypeCode, origDeliveryOrderNo);
					//T2的話POS才會有原出貨單
					checkLineDataForReturn(deliveryHeadPO, orgiDeliveryHead, conditionMap, PROGRAM_ID_RETURN,
							identification, errorMsgs);
					if(errorMsgs.size() == 0){
						modifyOrigDeliveryReturnedQuantity(deliveryHeadPO, conditionMap, PROGRAM_ID_RETURN, identification, errorMsgs);
					} 
				}
			}else{
				//無原出貨單
				deliveryHeadPO.setOrigDeliveryOrderTypeCode(null);
				deliveryHeadPO.setOrigDeliveryOrderNo(null);	        	           	        
				String warehouseManager = checkDeliveryHeadForReturn(deliveryHeadPO, conditionMap, PROGRAM_ID_RETURN, identification, errorMsgs);
				checkImDeliveryLinesForReturn(deliveryHeadPO, warehouseManager, conditionMap, PROGRAM_ID_RETURN, identification, errorMsgs);
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
							Double shipQuantity = NumberUtils.getDouble(deliveryLine.getShipQuantity()); //目前欲退貨數量
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

							Double returnQuantity = NumberUtils.getDouble(deliveryLine.getReturnQuantity());//原出貨數量
							Double returnedQuantity = NumberUtils.getDouble(deliveryLine.getReturnedQuantity()); //原出貨單的已退貨量

							double returnableQuantity = returnQuantity - returnedQuantity;//可退貨數量
							if (returnableQuantity < shipQuantity) {
								throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的退貨數量不可大於可退數量！");
							}else if (shipQuantity < 0D) {
								throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的退貨數量不可小於零！");
							}

							//若為T2POS銷退則確認LINE的報關單是否存在
							ImWarehouse warehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, orgiDeliveryHead.getDefaultWarehouseCode(), null);
							if("F".equals(deliveryLine.getIsTax()) && ("IRP".equals(orgiDeliveryHead.getOrderTypeCode()) || "IBT".equals(orgiDeliveryHead.getOrderTypeCode()))){
								//log.info("檢查物品是否存在cmOnhand");
								CmDeclarationOnHand cmDeclarationOnHand = cmDeclarationOnHandDAO.getOnHandById(deliveryLine.getImportDeclNo(), deliveryLine.getImportDeclSeq(), warehouse.getCustomsWarehouseCode(), brandCode);
								if(cmDeclarationOnHand == null){
									throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細查無原報單！");
								}
								if(!(deliveryLine.getItemCode()).equals(cmDeclarationOnHand.getCustomsItemCode())){
									throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細商品品號("+deliveryLine.getItemCode()+")與原報單中的品號("+cmDeclarationOnHand.getCustomsItemCode()+")不同！");
								}
							}
							
							//2011/7/19 lotNo都先設定為
							deliveryLine.setLotNo(SystemConfig.LOT_NO);
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
		log.info("modifyOrigDeliveryReturnedQuantity headid = " + deliveryHead.getHeadId());
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

	public static Object[] startProcessForReturnT2(ImDeliveryHead form) throws ProcessFailedException{       
		try{           
			String packageId = "So_SalesOrder";         
			String processId = "return";           
			String version = "20091019";
			String sourceReferenceType = "soSalesorderReturn";
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

	public static Object[] reverterProcess(ImDeliveryHead form) throws ProcessFailedException{       
		try{           
			String packageId = "So_SalesOrder";         
			String processId = "return";
			String version = "20090606";
			if("T2".equals(form.getBrandCode()))
				version = "20091019";
			String sourceReferenceType = "soSalesorderReturn";
			HashMap context = new HashMap();	    
			context.put("formId", form.getHeadId());
			return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		}catch (Exception ex){
			log.error("銷退流程啟動失敗，原因：" + ex.toString());
			throw new ProcessFailedException("銷退流程啟動失敗！");
		}	      
	}
	
	public List<Properties> executeInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginUser = (String)PropertyUtils.getProperty(otherBean, "loginUser");

			Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
			ImDeliveryHead form = formId != 0L ? findImDeliveryHeadById(formId) : null;
			//Map multiList = new HashMap(0);
			if(form != null){
				SoSalesOrderHead orderHead = (SoSalesOrderHead) soSalesOrderHeadDAO.findByPrimaryKey(SoSalesOrderHead.class, form.getSalesOrderId());
				BuOrderTypeId orderTypeId = new BuOrderTypeId();
				orderTypeId.setBrandCode(orderHead.getBrandCode());
				orderTypeId.setOrderTypeCode(orderHead.getOrderTypeCode());

				String salesOrderType = orderHead.getOrderTypeCode();            
				BuOrderType orderType = buOrderTypeService.findById(orderTypeId);
				if(orderType != null){
					salesOrderType +=  " " + "-" + " " + orderType.getName();
				}
				String shipAddress = StringTools.replaceNullToSpace(form.getShipZipCode()) + " " + StringTools.replaceNullToSpace(form.getShipCity()) + StringTools.replaceNullToSpace(form.getShipArea()) + StringTools.replaceNullToSpace(form.getShipAddress());
				String invoiceAddress = StringTools.replaceNullToSpace(form.getInvoiceZipCode()) + " " + StringTools.replaceNullToSpace(form.getInvoiceCity()) + StringTools.replaceNullToSpace(form.getInvoiceArea()) + StringTools.replaceNullToSpace(form.getInvoiceAddress());
				resultMap.put("salesOrderType",salesOrderType);
				resultMap.put("salesOrderNo",orderHead.getOrderNo());
				resultMap.put("shipAddresss", shipAddress);
				resultMap.put("invoiceAddresss", invoiceAddress);
				resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
				String employeeName = UserUtils.getUsernameByEmployeeCode(orderHead.getCreatedBy());
				resultMap.put("createdByName",employeeName);
				this.refreshDelivery("TM",form);
				if("Y".equals(form.getAttachedInvoice()))
					resultMap.put("attachedInvoiceExpress","是");
				else
					resultMap.put("attachedInvoiceExpress","否");
				this.countAllTotalAmount(form,new HashMap(0));
				imDeliveryHeadDAO.update(form);
			}

			BuBrand buBrand = buBrandDAO.findById(brandCode);

			List <BuOrderType> orderTypes = buOrderTypeService.findOrderbyType(brandCode,"IOU");
			List <BuCommonPhraseLine> invoiceTypeCodes = buCommonPhraseHeadDAO.findById("InvoiceType").getBuCommonPhraseLines();
			List <BuCommonPhraseLine> taxTypes =  buCommonPhraseHeadDAO.findById("TaxType").getBuCommonPhraseLines();
			List <BuCommonPhraseLine> homeDeliverys = buCommonPhraseHeadDAO.findById("DeliveryType").getBuCommonPhraseLines();
			List <BuCommonPhraseLine> paymentCategorys = buCommonPhraseHeadDAO.findById("PaymentCategory").getBuCommonPhraseLines();
			List allCurrency = buBasicDataService.findCurrencyByEnable(null);

			resultMap.put("brandName", buBrand.getBrandName());
			resultMap.put("form", form);
			resultMap.put("branchCode",buBrand.getBuBranch().getBranchCode());
			resultMap.put("orderTypes", AjaxUtils.produceSelectorData(orderTypes ,"orderTypeCode" ,"name" ,  true ,false) );
			resultMap.put("invoiceTypeCodes", AjaxUtils.produceSelectorData(invoiceTypeCodes ,"lineCode" ,"name" ,  false , true) );
			resultMap.put("taxTypes", AjaxUtils.produceSelectorData(taxTypes ,"lineCode" ,"name" ,  false , true) );
			resultMap.put("homeDeliverys", AjaxUtils.produceSelectorData(homeDeliverys ,"lineCode" ,"name" ,  false , true) );
			resultMap.put("paymentCategorys", AjaxUtils.produceSelectorData(paymentCategorys ,"lineCode" ,"name" ,  false , true) );
			resultMap.put("allCurrency", allCurrency = AjaxUtils.produceSelectorData(allCurrency  ,"currencyCode" ,"currencyCName",  false,  false) );

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

	public List<Properties> executeSearchInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);

		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			Map multiList = new HashMap(0);
			List<BuOrderType> allSalesType = buOrderTypeService.findOrderbyType(brandCode,"SO");
			List<BuOrderType> allShipType = buOrderTypeService.findOrderbyType(brandCode,"IOU");
			multiList.put( "allSalesType", AjaxUtils.produceSelectorData(allSalesType ,"orderTypeCode" ,"name" ,  true,  true));
			multiList.put( "allShipType", AjaxUtils.produceSelectorData(allShipType ,"orderTypeCode" ,"name"   ,  true,  false));
			resultMap.put("multiList",multiList);
			//log.info( "結束" );				
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

	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{

		try{

			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			//======================帶入Head的值=========================

			String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
			String customerCode = httpRequest.getProperty("customerCode");
			//Date scheduleShipDate_Start = (Date) conditionMap.get("scheduleShipDate_Start");
			//Date scheduleShipDate_End = (Date) conditionMap.get("scheduleShipDate_End");
			String status = httpRequest.getProperty("status");
			String shipTypeCode = httpRequest.getProperty("shipTypeCode");
			String shipNo_Start = httpRequest.getProperty("shipNo_Start");
			String shipNo_End = httpRequest.getProperty("shipNo_End");
			String shipDateS = httpRequest.getProperty("shipDateS");
			Date shipDate_Start = DateUtils.parseDate("yyyy/MM/dd", shipDateS);
			String shipDateE = httpRequest.getProperty("shipDateE");
			Date shipDate_End = DateUtils.parseDate("yyyy/MM/dd", shipDateE);
			String salesTypeCode = httpRequest.getProperty("salesTypeCode");
			String salesNo_Start = httpRequest.getProperty("salesNo_Start");
			String salesNo_End = httpRequest.getProperty("salesNo_End");
			String salesDateS = httpRequest.getProperty("salesDateS");
			Date salesDate_Start = DateUtils.parseDate("yyyy/MM/dd", salesDateS);
			String salesDateE = httpRequest.getProperty("salesDateE");
			Date salesDate_End = DateUtils.parseDate("yyyy/MM/dd", salesDateE);
			String defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode");

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put(" and model.salesOrderId = model2.headId and model.brandCode = :brandCode",brandCode);    
			findObjs.put(" and model.customerCode = :customerCode",customerCode.trim().toUpperCase());
			findObjs.put(" and model.status = :status",status);
			findObjs.put(" and model.orderTypeCode = :shipTypeCode",shipTypeCode);
			findObjs.put(" and model.orderNo >= :shipNo_Start",shipNo_Start);
			findObjs.put(" and model.orderNo <= :shipNo_End",shipNo_End);
			findObjs.put(" and model2.orderTypeCode = :salesTypeCode",salesTypeCode);
			findObjs.put(" and model2.orderNo >= :salesNo_Start",salesNo_Start);
			//findObjs.put(" and model.scheduleShipDate >= :scheduleShipDate_Start",scheduleShipDate_Start);
			//findObjs.put(" and model.scheduleShipDate <= :scheduleShipDate_End",scheduleShipDate_End);
			findObjs.put(" and model.shipDate >= :shipDate_Start",shipDate_Start);
			findObjs.put(" and model.shipDate <= :shipDate_End",shipDate_End);
			findObjs.put(" and model2.salesOrderDate >= :salesDate_Start",salesDate_Start);
			findObjs.put(" and model2.salesOrderDate <= :salesDate_End",salesDate_End);
			findObjs.put(" and model.defaultWarehouseCode = :defaultWarehouseCode",defaultWarehouseCode);
			//==============================================================	    

			Map imDeliveryHeadMap = imDeliveryHeadDAO.search( "ImDeliveryHead as model, SoSalesOrderHead as model2", "model, model2",findObjs, " order by model.orderNo desc ", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
			List imDeliverys = (List) imDeliveryHeadMap.get(BaseDAO.TABLE_LIST); 

			log.info("imDeliverys.size "+ imDeliverys.size());	
			if (imDeliverys != null && imDeliverys.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
				Long maxIndex = (Long)imDeliveryHeadDAO.search("ImDeliveryHead as model, SoSalesOrderHead as model2", "count(model.headId) as rowCount" ,findObjs, iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				log.info("maxIndex = " + maxIndex);
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, imDeliverys, gridDatas, firstIndex, maxIndex));
			}else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
			}

			return result;
		}catch(Exception ex){
			log.error("載入頁面顯示的選單查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的選單功能查詢失敗！");
		}	
	}

	/**
	 * 更新出貨單主檔及明細檔
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws Exception
	 */
	public ImDeliveryHead updateAJAXMovement(Map parameterMap , ImDeliveryHead imDeliveryHead) throws FormException, Exception {
		Map resultMap = new HashMap(0);
		try{
			if(imDeliveryHead != null){
				resultMap = updateDelivery(parameterMap);
				imDeliveryHead = (ImDeliveryHead)resultMap.get("entityBean");
			}else{
				log.error("查無出貨單主檔");
				throw new Exception("查無出貨單主檔");
			}
			return imDeliveryHead;
		}catch(FormException fe){
			log.error("出貨單存檔時發生錯誤，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		}catch(Exception ex){
			log.error("出貨單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("出貨單存檔時發生錯誤，原因：" + ex.getMessage());
		}

	}

	public List<Properties> executeSearchInitialReturn(Map parameterMap) throws Exception {
		HashMap returnMap = new HashMap();
		Map multiList = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean,"loginBrandCode");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean,"loginEmployeeCode");
			BuBrand buBrand = buBrandDAO.findById(brandCode);
			List <BuCommonPhraseLine> allStatus = buCommonPhraseService.findEnableLineById("FormStatus");
			multiList.put("allStatus" , AjaxUtils.produceSelectorData(allStatus  ,"lineCode" ,"name",  false,  true));
			returnMap.put("brandCode",brandCode);
			returnMap.put("brandName",buBrand.getBrandName());
			returnMap.put("multiList",multiList);
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (Exception ex) {
			log.error("採購預算單初始化失敗，原因：" + ex.toString());
			throw new Exception("採購預算單初始化失敗，原因：" + ex.getMessage());
		}
	}

	public List<Properties> getAJAXSearchPageDataReturn(Properties httpRequest) throws Exception {
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================
			String brandCode 		= httpRequest.getProperty("brandCode");
			String orderTypeCode	= httpRequest.getProperty("orderTypeCode");
			String incharge = httpRequest.getProperty("incharge");
			String startOrderNo		= httpRequest.getProperty("startOrderNo");
			String endOrderNo 		= httpRequest.getProperty("endOrderNo");
			String status			= httpRequest.getProperty("status");
			Date startShipDate 		= DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("startShipDate") );
			Date endShipDate 		= DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("endShipDate") );
			String transactionSeqNo = httpRequest.getProperty("transactionSeqNo");
			String customerPoNo 	= httpRequest.getProperty("customerPoNo");
			String customsNo 	= httpRequest.getProperty("customsNo");
			String customerCode 	= httpRequest.getProperty("customerCode");

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put(" and d.brandCode  = :brandCode", brandCode);
			findObjs.put(" and d.orderTypeCode  = :orderTypeCode", orderTypeCode);
			findObjs.put(" and b.incharge  = :incharge", incharge);
			findObjs.put(" and d.orderNo NOT LIKE :TMP","TMP%");
			findObjs.put(" and d.orderNo  >= :startOrderNo", startOrderNo);
			findObjs.put(" and d.orderNo  <= :endOrderNo", endOrderNo);
			findObjs.put(" and d.status  = :status", status);
			findObjs.put(" and d.shipDate  >= :startShipDate", startShipDate);
			findObjs.put(" and d.shipDate  <= :endShipDate", endShipDate);
			findObjs.put(" and d.transactionSeqNo = :transactionSeqNo",transactionSeqNo);
			findObjs.put(" and d.customerPoNo = :customerPoNo",customerPoNo);
			findObjs.put(" and d.customsNo = :customerPoNo",customsNo);
			findObjs.put(" and d.customerCode = :customerCode",customerCode);
			
			String orderByKey = "";
			orderByKey = "order by d.lastUpdateDate desc ";

			// ==============================================================
//			Map headsMap = imDeliveryHeadDAO.search("ImDeliveryHead", findObjs, orderByKey,
//					iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
//			List<ImDeliveryHead> heads = (List<ImDeliveryHead>) headsMap.get(BaseDAO.TABLE_LIST);
			List<ImDeliveryHead> heads = imDeliveryHeadDAO.getDeliveryListForIncharge(findObjs, orderByKey, iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
			List deliveryListSize = imDeliveryHeadDAO.getDeliveryListForIncharge(findObjs, orderByKey, iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT); //取得總筆數
			if (heads != null && heads.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = Long.valueOf(deliveryListSize.get(0).toString()); // 取得最後一筆 INDEX
				for (Iterator iterator = heads.iterator(); iterator.hasNext();) {
					ImDeliveryHead head = (ImDeliveryHead) iterator.next();
					this.setHeadNames(head);
				}
				result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES_RETURN,
						GRID_SEARCH_FIELD_DEFAULT_VALUES_RETURN, heads, gridDatas,firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES_RETURN,
						GRID_SEARCH_FIELD_DEFAULT_VALUES_RETURN, map, gridDatas));
			}
			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的選單查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的選單功能查詢失敗！");
		}
	}


	public void setHeadNames(ImDeliveryHead head) throws Exception{
		if(StringUtils.hasText(head.getCustomerCode())){
			BuCustomerWithAddressView buCustomerWithAddressView = buCustomerWithAddressViewService.
			findCustomerByType(head.getBrandCode(), head.getCustomerCode(), "customerCode", null);
			if(buCustomerWithAddressView != null){
				head.setCustomerName(buCustomerWithAddressView.getShortName());
			}
		}
		if(StringUtils.hasText(head.getStatus())){
			head.setStatusName(OrderStatus.getChineseWord(head.getStatus()));
		}
	}


	public List<Properties> saveSearchResultReturn(Properties httpRequest) throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES_RETURN);
		return AjaxUtils.getResponseMsg(errorMsg);
	}


	public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception {
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean,AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty( pickerBean, AjaxUtils.SEARCH_KEY);
			log.info("getSearchSelection.picker_parameter:" + timeScope + "/" + searchKeys.toString());
			List<Properties> result = AjaxUtils.getSelectedResults(timeScope,searchKeys);
			log.info("getSearchSelection.result:" + result.size());
			if (result.size() > 0)
				pickerResult.put("result", result);
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

	/**
	 * 確認出貨單是否有錯誤
	 * 
	 * @param imDeliveryHead
	 * @return errorMsgs
	 * @throws Exception
	 */
	public List checkDeliveryData (ImDeliveryHead imDeliveryHead) throws Exception{
		List errorMsgs = new ArrayList(0);
		String message = null;
		String identification = MessageStatus.getIdentification(imDeliveryHead.getBrandCode(), 
				imDeliveryHead.getOrderTypeCode(), imDeliveryHead.getOrderNo());
		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
		String dateType = "出貨日期";
		try{
			if(null == imDeliveryHead.getShipDate()){
				throw new Exception("請輸入預計出貨日期！");
			}
			ValidateUtil.isAfterClose(imDeliveryHead.getBrandCode(), imDeliveryHead.getOrderTypeCode(), dateType, imDeliveryHead.getShipDate(),imDeliveryHead.getSchedule());
		}catch (Exception ex) {
			message = ex.getMessage();
			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, imDeliveryHead.getLastUpdatedBy());
			errorMsgs.add(message);
			throw new Exception();
		}

		return errorMsgs;
	}

	public static Object[] completeAssignment(long assignmentId, boolean approveResult ) throws Exception{       
		try{           
			HashMap context = new HashMap();
			context.put("approveResult", approveResult);
			context.put("form", approveResult);
			return ProcessHandling.completeAssignment(assignmentId, context);
		}catch (Exception e){
			log.error("完成任務時發生錯誤："+e.getMessage());
			throw new Exception(e);
		}
	}

	public List<Properties> executeReturnInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		log.info("executeReturnInitial");
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginUser = (String)PropertyUtils.getProperty(otherBean, "loginUser");
			String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
			String localCurrencyCode = buCommonPhraseService.getBuCommonPhraseLineName("SystemConfig", "LocalCurrency");
			Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
			ImDeliveryHead form = formId != 0L ? findImDeliveryHeadById(formId) : null;
			if(form != null){
				loginUser = form.getCreatedBy();
			}
			
			BuOrderTypeId buOrderTypeId = new BuOrderTypeId(brandCode, orderTypeCode);
			BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
			resultMap.put("orderCondition", buOrderType.getOrderCondition());
			
			if(formId != 0L){
				if(form != null){
					resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
				}else{
					throw new Exception("找不到此單號");
				}
			}else{
				form = new ImDeliveryHead();
				form.setOrderTypeCode(orderTypeCode); 
				form.setStatus("SAVE");
				form.setWarehouseStatus("SAVE");
				form.setShipDate(DateUtils.parseDate(DateUtils.format(new Date())));
				form.setBrandCode(brandCode);
				form.setCurrencyCode(localCurrencyCode);
				form.setSuperintendentCode(loginUser);
				form.setScheduleCollectionDate(DateUtils.parseDate(DateUtils.format(new Date())));
				if("F".equals(buOrderType.getTaxCode())){
					form.setTaxType("1");
					form.setTaxRate(0D);
				}else{
					form.setTaxType("3");
					form.setTaxRate(5D);
				}
				form.setDiscountRate(100D);
				form.setExportCommissionRate(0D);
				form.setExportExchangeRate(1D);
				form.setCreatedBy(loginUser);
				form.setLastUpdatedBy(loginUser);
				saveTmp(form);
			}
			this.refreshDelivery("TM",form);
			resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			
			BuBrand buBrand = buBrandDAO.findById(brandCode);
			resultMap.put("brandName", buBrand.getBrandName());
			resultMap.put("branchCode",buBrand.getBuBranch().getBranchCode());

			List<BuCommonPhraseLine> allInvoiceType = buCommonPhraseHeadDAO.findById("InvoiceType").getBuCommonPhraseLines();
			List<BuCommonPhraseLine> allTaxType = buCommonPhraseHeadDAO.findById("TaxType").getBuCommonPhraseLines();
			List<BuCommonPhraseLine> allDeliveryType = buCommonPhraseHeadDAO.findById("DeliveryType").getBuCommonPhraseLines();
			List<BuCommonPhraseLine> allPaymentCategory = buCommonPhraseHeadDAO.findById("PaymentCategory").getBuCommonPhraseLines();
			List allItemCategory = imItemCategoryService.findByCategoryType(buBrand.getBrandCode(), "ITEM_CATEGORY");

			List allCountry = buBasicDataService.findCountryByEnable(null);
			List allCurrency = buBasicDataService.findCurrencyByEnable(null);
			List allPaymentTerm = buBasicDataService.findPaymentTermByOrganizationAndEnable("TM", null);
			List <BuShop> allShop = new ArrayList();
			List <ImWarehouse> allAvailableWarehouse = new ArrayList();
			List<BuOrderType> allReturnOrderType = buOrderTypeService.findOrderbyType(brandCode,"IR");
			List<BuOrderType> allDeliveryOrderType = buOrderTypeService.findOrderbyType(brandCode,"IOU");
			//if((OrderStatus.SAVE.equals(form.getStatus()) || OrderStatus.REJECT.equals(form.getStatus()) || OrderStatus.UNCONFIRMED.equals(form.getStatus())) 
			//		&& !StringUtils.hasText(form.getOrigDeliveryOrderNo())){
				allShop = buBasicDataService.getShopForEmployee(brandCode, loginUser, "Y");
				allAvailableWarehouse = imWarehouseService.getWarehouseByWarehouseEmployee(brandCode, loginUser, null);
			//}else{
			//	BuShop shop = buShopService.findById(form.getShopCode());
			//	if(null == shop)
			//		throw new Exception("查無專櫃代號：" + form.getShopCode() +"之相關資訊！");
			//	allShop.add(shop);
			//	ImWarehouse warehouse = imWarehouseService.findById(form.getDefaultWarehouseCode());
			//	allAvailableWarehouse.add(warehouse);
			//}

			if(allAvailableWarehouse == null || allAvailableWarehouse.size() == 0)
				throw new Exception("查無品牌代號：" + brandCode +  "、工號：" + loginUser + "可使用的庫別資料！");
			if(allReturnOrderType == null)
				throw new Exception("查無退貨單別，請聯絡系統管理人員處理！");
			if(allDeliveryOrderType == null)
				throw new Exception("查無出貨單別，請聯絡系統管理人員處理！");
			if(allShop == null || allShop.size() == 0)
				throw new Exception("查無可用之專櫃店名，請聯絡系統管理人員處理！");
			allDeliveryOrderType = this.setAllDeliveryOrderType(allDeliveryOrderType,orderTypeCode);
			this.setReturnOrderType(allReturnOrderType,orderTypeCode,resultMap);

			List allshopMachine = buShopMachineService.findByShopCode(allShop.get(0).getShopCode());
			resultMap.put("allInvoiceType", AjaxUtils.produceSelectorData(allInvoiceType ,"lineCode" ,"name" ,false ,true));
			resultMap.put("allTaxType", AjaxUtils.produceSelectorData(allTaxType ,"lineCode" ,"name" ,false ,false));
			resultMap.put("allDeliveryType", AjaxUtils.produceSelectorData(allDeliveryType ,"lineCode" ,"name" ,false ,true));
			resultMap.put("allPaymentCategory", AjaxUtils.produceSelectorData(allPaymentCategory ,"lineCode" ,"name" ,false ,true));
			resultMap.put("allItemCategory", AjaxUtils.produceSelectorData(allItemCategory ,"categoryCode" ,"categoryName" ,false ,false));
			resultMap.put("allCountry", AjaxUtils.produceSelectorData(allCountry ,"countryCode" ,"countryCName" ,false ,true));
			resultMap.put("allCurrency", AjaxUtils.produceSelectorData(allCurrency ,"currencyCode" ,"currencyCName" ,false ,true));
			resultMap.put("allPaymentTerm", AjaxUtils.produceSelectorData(allPaymentTerm ,"paymentTermCode" ,"name" ,false ,true));
			resultMap.put("allShop", AjaxUtils.produceSelectorData(allShop ,"shopCode" ,"shopCName" ,  true ,false));
			resultMap.put("allAvailableWarehouse", AjaxUtils.produceSelectorData(allAvailableWarehouse ,"warehouseCode" ,"warehouseName" ,true ,false));
			resultMap.put("allReturnOrderType", AjaxUtils.produceSelectorData(allReturnOrderType ,"orderTypeCode" ,"name" ,true ,false));
			resultMap.put("allDeliveryOrderType", AjaxUtils.produceSelectorData(allDeliveryOrderType ,"orderTypeCode" ,"name" ,true ,false));
			resultMap.put("allshopMachine", AjaxUtils.produceSelectorData(allshopMachine ,"posMachineCode" ,"posMachineCode" ,false ,true));

			BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, form.getCreatedBy());
			if(employeeWithAddressView != null){
				resultMap.put("createdByName", employeeWithAddressView.getChineseName());
			}else{
				throw new Exception("查無填單人員資料，請洽服務人員");
			}
			resultMap.put("form", form);

			//for 儲位用
    		if(imStorageAction.isStorageExecute(form)){
    			//建立儲位單
    			Map storageMap = new HashMap();
    			storageMap.put("storageTransactionDate", "shipDate");
    			storageMap.put("storageTransactionType", ImStorageService.OUT);
    			//轉出或轉入
    			//if("IMR".equals(buOrderType.getTypeCode())){
    				storageMap.put("deliveryWarehouseCode", "defaultWarehouseCode");
    				//storageMap.put("arrivalWarehouseCode", "defaultWarehouseCode");
    			//}else{
    				//storageMap.put("deliveryWarehouseCode", "defaultWarehouseCode");
    				//storageMap.put("arrivalWarehouseCode", "");
    			//}
    			
    			ImStorageHead imStorageHead = imStorageAction.executeImStorageHead(storageMap, form);

    			resultMap.put("storageHeadId", imStorageHead.getStorageHeadId());
    			resultMap.put("beanHead", "ImDeliveryHead");
    			resultMap.put("beanItem", "imDeliveryLines");
    			resultMap.put("quantity", "shipQuantity");
    			resultMap.put("storageTransactionType", ImStorageService.OUT);
    			resultMap.put("storageStatus", "#F.status");
    			//轉出或轉入
    			//if("IMR".equals(buOrderType.getTypeCode())){
    				resultMap.put("deliveryWarehouse", "#F.defaultWarehouseCode");
    				//resultMap.put("arrivalWarehouse", "#F.defaultWarehouseCode");
    			//}else{
    				//resultMap.put("deliveryWarehouse", "#F.defaultWarehouseCode");
    				//resultMap.put("arrivalWarehouse", "");
    			//}
    		}
    		
		}catch(Exception ex){
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type"   , "ALERT");
			messageMap.put("message", "表單初始化失敗，原因："+ex.getMessage());
			resultMap.put("vatMessage",messageMap);
		}
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}

	/**
	 * 根據退貨單單號找尋可以被退回的單別
	 * @param allDeliveryOrderType
	 * @param orderTypeCode
	 */
	public List<BuOrderType> setAllDeliveryOrderType(List<BuOrderType> allDeliveryOrderType , String orderTypeCode){
		Map deliveryOrderMap = new HashMap();
		for(int  i = 0; i < allDeliveryOrderType.size(); i ++){
			BuOrderType orderType = (BuOrderType)allDeliveryOrderType.get(i);
			if(null != orderType.getNextOrderTypeCode()){
				String[] nextOrderTypeCode = orderType.getNextOrderTypeCode().split(",");
				if(nextOrderTypeCode != null && nextOrderTypeCode.length > 0){
					for (int j = 0; j < nextOrderTypeCode.length; j++) {
						ArrayList orderList = (ArrayList)deliveryOrderMap.get(nextOrderTypeCode[j]);
						if(orderList != null){
							orderList.add(orderType);
						}else{
							orderList = new ArrayList();
							orderList.add(orderType);
							deliveryOrderMap.put(nextOrderTypeCode[j], orderList);
						}
					}
				}
			}
		}
		return (ArrayList)deliveryOrderMap.get(orderTypeCode);
	}

	public void setReturnOrderType(List<BuOrderType> allReturnOrderType , String orderTypeCode , Map resultMap){

		for(int i = 0; i <  allReturnOrderType.size(); i++){
			BuOrderType orderType = (BuOrderType)allReturnOrderType.get(i);
			BuOrderTypeId orderTypeId = orderType.getId();
			if(orderTypeId.getOrderTypeCode().equals(orderTypeCode)){
				//$reportFunctionCode = orderType.getReportFunctionCode();
				//$reportFileName = orderType.getReportFileName();
				resultMap.put("priceType" ,orderType.getPriceType());
				break;
			}
		}
	}

	public List<Properties> executeFindCMHead(Properties httpRequest) throws Exception{

		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try{
			//======================取得複製時所需的必要資訊========================
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
			if(headId == 0L){
				throw new ValidationErrorException("無法取得銷退單的主鍵值！");
			}
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String employeeCode = httpRequest.getProperty("employeeCode");
			String exportDeclNo = httpRequest.getProperty("exportDeclNo");
			String defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode");

			HashMap map = new HashMap();
			map.put("brandCode", brandCode);
			map.put("employeeCode", employeeCode);
			//======================get current return object==================
			ImDeliveryHead deliveryHead = (ImDeliveryHead)imDeliveryHeadDAO.findByPrimaryKey(ImDeliveryHead.class, headId);
			if(deliveryHead == null){
				throw new NoSuchObjectException("依據主鍵：" + headId + "查無銷退單的資料！");
			}
			//======================delete delivery line==========================
			deleteDeliveryLine(headId);
			//================copy Line Data==================
			List <CmDeclarationHead> cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", exportDeclNo);
			if(cmDeclarationHeads != null && cmDeclarationHeads.size() > 0){
				CmDeclarationHead cmDeclarationHead = cmDeclarationHeads.get(0);
				List<CmDeclarationItem> cmDeclarationItems = cmDeclarationHead.getCmDeclarationItems();
				log.info("cmDeclarationItems.size() = " + cmDeclarationItems.size());
				List<ImDeliveryLine> newLines = new ArrayList(0);
				for(CmDeclarationItem cmDeclarationItem : cmDeclarationItems){
					ImDeliveryLine newLine = new ImDeliveryLine();
					newLine.setWarehouseCode(defaultWarehouseCode);
					newLine.setImportDeclNo(cmDeclarationItem.getODeclNo());
					newLine.setImportDeclDate(cmDeclarationHead.getDeclDate());
					newLine.setImportDeclType(cmDeclarationHead.getDeclType());
					newLine.setImportDeclSeq(cmDeclarationItem.getOItemNo());
					newLine.setIndexNo(cmDeclarationItem.getItemNo());
					newLine.setShipQuantity(cmDeclarationItem.getQty());
					newLines.add(newLine);
				}
				deliveryHead.setImDeliveryLines(newLines);
				imDeliveryHeadDAO.update(deliveryHead);
				properties.setProperty("ExportDeclDate", AjaxUtils.getPropertiesValue(DateUtils.format(cmDeclarationHead.getDeclDate(), "yyyy/MM/dd"), ""));
				properties.setProperty("ExportDeclType", cmDeclarationHead.getDeclType());
			}

			result.add(properties);
			return result;
		}catch(Exception ex){
			log.error("查詢報關單發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢報關單發生錯誤，原因：" + ex.getMessage());
		}
	}

	public List<Properties> executeFindCM(Properties httpRequest) throws Exception{

		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try{
			//======================取得複製時所需的必要資訊========================
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
			if(headId == 0L){
				throw new ValidationErrorException("無法取得銷退單的主鍵值！");
			}
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String employeeCode = httpRequest.getProperty("employeeCode");
			String exportDeclNo = httpRequest.getProperty("exportDeclNo");

			HashMap map = new HashMap();
			map.put("brandCode", brandCode);
			map.put("employeeCode", employeeCode);
			//======================get current return object==================
			ImDeliveryHead deliveryHead = (ImDeliveryHead)imDeliveryHeadDAO.findByPrimaryKey(ImDeliveryHead.class, headId);
			if(deliveryHead == null){
				throw new NoSuchObjectException("依據主鍵：" + headId + "查無銷退單的資料！");
			}
			//================find CMData==================
			List <CmDeclarationHead> cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", exportDeclNo);
			if(cmDeclarationHeads != null && cmDeclarationHeads.size() > 0){
				CmDeclarationHead cmDeclarationHead = cmDeclarationHeads.get(0);
				properties.setProperty("ExportDeclDate", AjaxUtils.getPropertiesValue(DateUtils.format(cmDeclarationHead.getDeclDate(), "yyyy/MM/dd"), ""));
				properties.setProperty("ExportDeclType", cmDeclarationHead.getDeclType());
				properties.setProperty("ExportExchangeRate", String.valueOf(cmDeclarationHead.getExchangeRate()));
			}

			result.add(properties);
			return result;
		}catch(Exception ex){
			log.error("查詢報關單發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢報關單發生錯誤，原因：" + ex.getMessage());
		}
	}


	public List updateCheckData (ImDeliveryHead imDeliveryHead) throws Exception{
		List errorMsgs = new ArrayList(0);
		String message = null;
		String identification = MessageStatus.getIdentification(imDeliveryHead.getBrandCode(), 
				imDeliveryHead.getOrderTypeCode(), imDeliveryHead.getOrderNo());
		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
		try{
			String taxCode = buOrderTypeService.getTaxCode(imDeliveryHead.getBrandCode(), imDeliveryHead.getOrderTypeCode());
			if("F".equals(taxCode)){
				if(imDeliveryHead.getExportDeclNo()==null){
					throw new Exception("報關單號不得為空！");
				}else if(imDeliveryHead.getLatestExportDeclType() != null 
						&& !imDeliveryHead.getExportDeclType().equals(imDeliveryHead.getLatestExportDeclType())){
					throw new Exception("報關單與退關後報關單類別不同！");
				}else{
					List<CmDeclarationHead> cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty(
							"CmDeclarationHead"," and declNo = ?",new Object[]{imDeliveryHead.getExportDeclNo()});
					log.info("declNo = " + imDeliveryHead.getExportDeclNo());
					if(cmDeclarationHeads == null || cmDeclarationHeads.size()==0){
						throw new Exception("查無單號"+imDeliveryHead.getExportDeclNo()+"之報關單！");
					}
				}
				updateResetExchangeRate(imDeliveryHead);
			}
		}catch (Exception ex) {
			log.error("出貨單檢核後存檔失敗，原因：" + ex.toString());
			message = ex.getMessage();
			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
					message, imDeliveryHead.getLastUpdatedBy());
			errorMsgs.add(message);
			throw new Exception();
		}

		return errorMsgs;
	}

	public void updateResetExchangeRate(ImDeliveryHead head){
		//重新算Head的匯率金額
		Double rate = head.getExportExchangeRate()==null?1D:head.getExportExchangeRate();
		//head.setOriginalTotalFrnSalesAmt(NumberUtils.getDouble(head.getTotalOriginalSalesAmount()/rate));
		//head.setOriginalTotalFrnShipAmt(NumberUtils.getDouble(head.getTotalOriginalShipAmount()/rate));
		//head.setActualTotalFrnSalesAmt(NumberUtils.getDouble(head.getTotalActualSalesAmount()/rate));
		head.setTotalActualSalesAmount(NumberUtils.round(head.getActualTotalFrnSalesAmt()*rate, 2));
		//head.setActualTotalFrnShipAmt(NumberUtils.getDouble(head.getTotalActualShipAmount()/rate));
		head.setTotalActualShipAmount(NumberUtils.round(head.getActualTotalFrnShipAmt()*rate, 2));

		List<ImDeliveryLine> imDeliveryItems = head.getImDeliveryLines();
		//重新算Line的匯率金額
		for (Iterator iterator = imDeliveryItems.iterator(); iterator.hasNext();) {
			ImDeliveryLine line = (ImDeliveryLine) iterator.next();
			//line.setOriginalForeignUnitPrice(NumberUtils.getDouble(line.getOriginalUnitPrice()/rate));
			//line.setOriginalForeignSalesAmt(NumberUtils.getDouble(line.getOriginalSalesAmount()/rate));
			//line.setOriginalForeignShipAmt(NumberUtils.getDouble(line.getOriginalShipAmount()/rate));
			//line.setActualForeignUnitPrice(NumberUtils.getDouble(line.getActualUnitPrice()/rate));
			line.setActualUnitPrice(NumberUtils.round(line.getActualForeignUnitPrice() * rate , 4));
			//line.setActualForeignSalesAmt(NumberUtils.getDouble(line.getActualSalesAmount()/rate));
			line.setActualSalesAmount(NumberUtils.round(line.getActualForeignSalesAmt() * rate , 2));
			//line.setActualForeignShipAmt(NumberUtils.getDouble(line.getActualShipAmount()/rate));
			line.setActualShipAmount(NumberUtils.round(line.getActualForeignShipAmt() * rate , 2));
		}

		SoSalesOrderHead so = (SoSalesOrderHead) soSalesOrderHeadDAO.
		findByPrimaryKey(SoSalesOrderHead.class, head.getSalesOrderId());
		so.setExportExchangeRate(rate);
		//so.setOriginalTotalFrnSalesAmt(NumberUtils.getDouble(so.getTotalOriginalSalesAmount()/rate));
		//so.setActualTotalFrnSalesAmt(NumberUtils.getDouble(so.getTotalActualSalesAmount()/rate));
		so.setTotalActualSalesAmount(NumberUtils.round(so.getActualTotalFrnSalesAmt()*rate, 2));

		List<SoSalesOrderItem> soSalesOrderItems = so.getSoSalesOrderItems();
		for (Iterator iterator = soSalesOrderItems.iterator(); iterator.hasNext();) {
			SoSalesOrderItem item = (SoSalesOrderItem) iterator.next();
			//item.setOriginalForeignUnitPrice(NumberUtils.getDouble(item.getOriginalUnitPrice()/rate));
			//item.setOriginalForeignSalesAmt(NumberUtils.getDouble(item.getOriginalSalesAmount()/rate));
			item.setActualUnitPrice(NumberUtils.round(item.getActualForeignUnitPrice()*rate, 2));
			item.setActualSalesAmount(NumberUtils.round(item.getActualForeignSalesAmt()*rate, 2));
		}
		soSalesOrderHeadDAO.update(so);
	}

	public List<Properties> findShopMachineByShopCodeForAJAX(Properties httpRequest) throws Exception{
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try{
			String shopCode = httpRequest.getProperty("shopCode");// 品牌代號
			//String objectName = httpRequest.getProperty("objectName");// 品牌代號
			List allshopMachine = buShopMachineService.findByShopCode(shopCode);
			allshopMachine = AjaxUtils.produceSelectorData(allshopMachine, "posMachineCode", "posMachineCode", false, true);
			properties.setProperty("allshopMachine", AjaxUtils.parseSelectorData(allshopMachine));
			result.add(properties);
			return result;
		}catch(Exception ex){
			log.error("查詢櫃別機台發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢櫃別機台發生錯誤，原因：" + ex.getMessage());
		}
	}

	public List<Properties> findEncryTextPrint(Properties httpRequest) throws Exception{
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try{
			String brandCode = httpRequest.getProperty("brandCode");
			String employeeCode = httpRequest.getProperty("employeeCode");
			log.info("employeeCode = " + employeeCode);
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");
			log.info("orderTypeCode = " + orderTypeCode);
			BuOrderTypeId id = new BuOrderTypeId(brandCode, orderTypeCode);
			BuOrderType buOrderType = buOrderTypeDAO.findById(id);
			BuCommonPhraseLine reportConfig = buCommonPhraseService.getBuCommonPhraseLine("ReportURL", "CrystalReportURL");
			if(reportConfig != null){
				String reportUrl = reportConfig.getAttribute1();
				properties.setProperty("reportUrl", reportUrl);
				log.info("reportUrl = " + reportUrl);
			}
			String reportFunctionCode = buOrderType.getReportFunctionCode();
			log.info("reportFunctionCode = " + reportFunctionCode);
			String reportFileName = buOrderType.getReportFileName();
			log.info("reportFileName = " + reportFileName);
			String permissionInfo = brandCode + "@@" + employeeCode + "@@" + reportFunctionCode + "@@";
			log.info("permissionInfo = " + permissionInfo);
			String encryText = new DES().encrypt(permissionInfo + String.valueOf(new Date().getTime()));
			log.info("encryText = " + encryText);
			properties.setProperty("encryText", encryText);
			properties.setProperty("reportFileName", reportFileName);
			result.add(properties);
			return result;
		}catch(Exception ex){
			log.error("查詢encryText發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢encryText發生錯誤，原因：" + ex.getMessage());
		}
	}

	public List<Properties> findEncryTextAddress(Properties httpRequest) throws Exception{
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try{
			String brandCode = httpRequest.getProperty("brandCode");
			log.info("brandCode = " + brandCode);
			String employeeCode = httpRequest.getProperty("employeeCode");
			log.info("employeeCode = " + employeeCode);
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");
			log.info("orderTypeCode = " + orderTypeCode);
			BuCommonPhraseLine reportConfig = buCommonPhraseService.getBuCommonPhraseLine("ReportURL", "CrystalReportURL");
			if(reportConfig != null){
				String reportUrl = reportConfig.getAttribute1();
				properties.setProperty("reportUrl", reportUrl);
				log.info("reportUrl = " + reportUrl);
			}
			String permissionInfo = brandCode + "@@" + employeeCode + "@@" + brandCode + "_IM0208@@";
			log.info("permissionInfo = " + permissionInfo);
			String encryText = new DES().encrypt(permissionInfo + String.valueOf(new Date().getTime()));
			log.info("encryText = " + encryText);
			properties.setProperty("encryText", encryText);
			result.add(properties);
			return result;
		}catch(Exception ex){
			log.error("查詢encryText發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢encryText發生錯誤，原因：" + ex.getMessage());
		}
	}

	public void updateDeliveryExport(ImDeliveryHead imDeliveryHead){
		if(imDeliveryHead.getLatestExportDeclNo() == null)
			imDeliveryHead.setExportDeclNoLog(imDeliveryHead.getLatestExportDeclNo());
		else
			imDeliveryHead.setExportDeclNoLog(","+imDeliveryHead.getLatestExportDeclNo());
		SoSalesOrderHead soSalesOrderHead = (SoSalesOrderHead) soSalesOrderHeadDAO
		.findByPrimaryKey(SoSalesOrderHead.class, imDeliveryHead.getSalesOrderId());
		soSalesOrderHead.setExportDeclNo(imDeliveryHead.getExportDeclNo());
		soSalesOrderHead.setExportDeclDate(imDeliveryHead.getExportDeclDate());
		soSalesOrderHead.setExportDeclType(imDeliveryHead.getExportDeclType());
		soSalesOrderHead.setLatestExportDeclNo(imDeliveryHead.getLatestExportDeclNo());
		soSalesOrderHead.setLatestExportDeclDate(imDeliveryHead.getLatestExportDeclDate());
		soSalesOrderHead.setLatestExportDeclType(imDeliveryHead.getLatestExportDeclType());
		soSalesOrderHead.setExportDeclNoLog(imDeliveryHead.getExportDeclNoLog());
		soSalesOrderHeadDAO.update(soSalesOrderHead);
		imDeliveryHeadDAO.update(imDeliveryHead);
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
			String headId = (String)PropertyUtils.getProperty(otherBean, "formId");
			String displayAmt = (String)PropertyUtils.getProperty(otherBean, "displayAmt");
			
			if ("TMP".equals(orderNo.substring(0, 3))){
				SoSalesOrderHead soHead = (SoSalesOrderHead)soSalesOrderHeadDAO.findByOrderNo(Long.valueOf(headId));
				if (null != soHead)
					orderNo = soHead.getOrderNo();
			}
			
			Map returnMap = new HashMap(0);
			Map parameters = new HashMap(0);
			if("T2".equals(brandCode) && ( "SOE".equals(orderTypeCode) || "SOH".equals(orderTypeCode) || "IOH".equals(orderTypeCode) )){
				//CC後面要代的參數使用parameters傳遞			
				parameters.put("prompt0", brandCode);
				parameters.put("prompt1", orderTypeCode);
				parameters.put("prompt2", orderNo);
				parameters.put("prompt3", orderNo);
				parameters.put("prompt4", "Y");
			}
			else if("T2".equals(brandCode)){	
				parameters.put("prompt0", brandCode);
				parameters.put("prompt1", orderTypeCode);
				parameters.put("prompt2", "");
				parameters.put("prompt3", "");
				parameters.put("prompt4", orderNo);
				parameters.put("prompt5", orderNo);
			}
			else{
				parameters.put("prompt0", brandCode);
				parameters.put("prompt1", orderTypeCode);
				parameters.put("prompt2", orderNo);
				parameters.put("prompt3", orderNo);
				parameters.put("prompt4", displayAmt);
				//parameters.put("prompt5", );
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
	 * 執行反確認單據反轉
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public Object executeReverter(Long headId, String employeeCode) throws Exception {
		log.info("executeReverter headId = " + headId);
		log.info("executeReverter employeeCode = " + employeeCode);
		ImDeliveryHead head = findImDeliveryHeadById(headId);
		if(null == head)
			throw new Exception("查無出貨單主鍵: " + headId +" 對應之出貨單");
		String beforeChangeStatus = head.getStatus();
		log.info("beforeChangeStatus = " + beforeChangeStatus);
		BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(head.getBrandCode(), employeeCode);
		if(employeeWithAddressView == null){
			throw new Exception("查無員工代號: " + employeeCode +" 對應之員工");
		}
		
		String dateType = "日期";
		if(OrderStatus.FINISH.equals(beforeChangeStatus) || OrderStatus.CLOSE.equals(beforeChangeStatus)){
			ValidateUtil.isAfterClose(head.getBrandCode(), head.getOrderTypeCode(), dateType, head.getShipDate(),head.getSchedule());
			updateReturnOnHand(head, employeeCode, "TM", true);
			
			//for 儲位用
			if(imStorageAction.isStorageExecute(head)){
				//異動儲位庫存
				imStorageService.updateStorageOnHandBySource(head, OrderStatus.SAVE, PROGRAM_ID, null, true);
			}
			
			imTransationDAO.deleteTransationByIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
			//出貨單反確認
			if(null != head.getSalesOrderId()){
				SoSalesOrderHead soHead = (SoSalesOrderHead)soSalesOrderHeadDAO.findByPrimaryKey(SoSalesOrderHead.class, head.getSalesOrderId());
				if(null == soHead)
					throw new Exception("查無銷貨單主鍵: " + head.getSalesOrderId() +" 對應之出貨單");
				imDeliveryHeadDAO.delete(head);
				if(!"IOP".equals(head.getOrderTypeCode())){
					soHead.setStatus(OrderStatus.SAVE);
				}else{
					soHead.setStatus(OrderStatus.UNCONFIRMED);
					SoPostingTally postingTally = 
						(SoPostingTally)soPostingTallyDAO.findByPrimaryKey(SoPostingTally.class, new SoPostingTallyId(soHead.getPosMachineCode(), soHead.getSalesOrderDate()));
					if(null == postingTally)
						throw new Exception("查無銷售日期: " + soHead.getSalesOrderDate() + " ，庫別: " + soHead.getShopCode() + " ，機台: " + soHead.getPosMachineCode() +" 對應之銷售帳務紀錄");
					postingTally.setIsPosting("N");
					soPostingTallyDAO.update(postingTally);
				}
				//流程設置起始人
				soHead.setCreatedBy(employeeCode);
				soHead.setLastUpdateDate(new Date());
				soSalesOrderHeadDAO.update(soHead);
				return soHead;
			//退貨單反確認
			}else{
				log.info("modifyOrigDeliveryReturnedQuantity");
				if(StringUtils.hasText(head.getOrigDeliveryOrderNo())){
					modifyOrigDeliveryReturnedQuantity(head);
				}
				convertToPositiveForReturn(head);
				head.setStatus(OrderStatus.SAVE);
				head.setCreatedBy(employeeCode);
				head.setLastUpdateDate(new Date());
				imDeliveryHeadDAO.update(head);
				return head;
			}
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
		String brandcode = org.apache.commons.beanutils.BeanUtils.getNestedProperty(bean, "brandCode");
		String orderTypeCode = org.apache.commons.beanutils.BeanUtils.getNestedProperty(bean, "orderTypeCode");
		//起流程
    	if(bean.getClass().equals(tw.com.tm.erp.hbm.bean.SoSalesOrderHead.class)){
			if(!"IOP".equals(orderTypeCode)){
				if("T2".equals(brandcode))
					SoSalesOrderMainService.startProcessT2((SoSalesOrderHead)bean);
				else
					SoSalesOrderMainService.startProcess((SoSalesOrderHead)bean);
			}
    	}else{
    		if("T2".equals(brandcode))
    			ImDeliveryMainService.startProcessForReturnT2((ImDeliveryHead)bean);
    		else
    			ImDeliveryMainService.startProcessForReturn((ImDeliveryHead)bean);
    	}
	}
	
	/**
	 * 執行儲位單 更新 與 比對
	 */
	public void executeStorage(ImDeliveryHead imDeliveryHead) throws Exception {
		//更新儲位單頭 2011.11.11 by Caspar
		Map storageMap = new HashMap();
		storageMap.put("storageTransactionDate", "shipDate");
		storageMap.put("storageTransactionType", ImStorageService.OUT);
		storageMap.put("deliveryWarehouseCode", "defaultWarehouseCode");
		//storageMap.put("arrivalWarehouseCode", "arrivalWarehouseCode");
		ImStorageHead imStorageHead = imStorageAction.executeImStorageHead(storageMap, imDeliveryHead);
		
		//更新儲位單身與比對 2011.11.11 by Caspar
		storageMap.put("beanItem", "imDeliveryLines");
		storageMap.put("quantity", "shipQuantity");
		imStorageAction.executeImStorageItem(storageMap, imDeliveryHead, imStorageHead);
	}
	
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

	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}

	public void setBuCommonPhraseHeadDAO(BuCommonPhraseHeadDAO buCommonPhraseHeadDAO) {
		this.buCommonPhraseHeadDAO = buCommonPhraseHeadDAO;
	}

	public void setImDeliveryLineService(ImDeliveryLineService imDeliveryLineService) {
		this.imDeliveryLineService = imDeliveryLineService;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setImWarehouseService(ImWarehouseService imWarehouseService) {
		this.imWarehouseService = imWarehouseService;
	}

	public void setCmDeclarationOnHandDAO(
			CmDeclarationOnHandDAO cmDeclarationOnHandDAO) {
		this.cmDeclarationOnHandDAO = cmDeclarationOnHandDAO;
	}

	public void setCmDeclarationHeadDAO(CmDeclarationHeadDAO cmDeclarationHeadDAO) {
		this.cmDeclarationHeadDAO = cmDeclarationHeadDAO;
	}

	public void setBuOrderTypeDAO(BuOrderTypeDAO buOrderTypeDAO) {
		this.buOrderTypeDAO = buOrderTypeDAO;
	} 

	public void setBuShopMachineService(BuShopMachineService buShopMachineService) {
		this.buShopMachineService = buShopMachineService;
	}

	public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}

	public void setBuCustomerWithAddressViewService(
			BuCustomerWithAddressViewService buCustomerWithAddressViewService) {
		this.buCustomerWithAddressViewService = buCustomerWithAddressViewService;
	}

	public void setImItemSerialDAO(ImItemSerialDAO imItemSerialDAO) {
		this.imItemSerialDAO = imItemSerialDAO;
	}

	public void setBuShopService(BuShopService buShopService) {
		this.buShopService = buShopService;
	}

	public void setSoPostingTallyDAO(SoPostingTallyDAO soPostingTallyDAO) {
		this.soPostingTallyDAO = soPostingTallyDAO;
	}
	
	public void setImStorageAction(ImStorageAction imStorageAction) {
		this.imStorageAction = imStorageAction;
	}

	public void setImStorageService(ImStorageService imStorageService) {
		this.imStorageService = imStorageService;
	}
	
	public void setBuCommonPhraseLine(BuCommonPhraseLine buCommonPhraseLine){
		this.buCommonPhraseLine = buCommonPhraseLine;
	}
	
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO){
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
	
	/**
     * ESP,ESF出貨單 船務送出後自動發信
     *
     * @param subject
     * @param templateFileName
     * @param display
     * @param mailAddress
     * @param attachFiles
     * @param cidMap
     */
	public void getMailList(ImDeliveryHead head, String subject, String templateFileName, Map display,
			List mailAddress, List attachFiles, Map cidMap) throws Exception {
		
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

			// #1 取得配置檔得到 寄信的報表與
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("MailList"
					+ orderTypeCode, orderTypeCode); // +orderTypeCode
			// itemCategory
			if (null == buCommonPhraseLine) {
				// 寄給故障維謢人員
				mailAddress.add("Developer@tasameng.com.tw");
				subjectError = "MailList" + orderTypeCode + itemCategory + "無配置檔異常 ";
				description = "";
			} else {
				
				reportFileName = buCommonPhraseLine.getAttribute1();
				
				mailAddress.add("Developer@tasameng.com.tw"); // 資訊部
				
				// 修改為寄給多個單位，以分號分隔email地址 by Weichun 2011.09.15
				String[] attribute2 = buCommonPhraseLine.getAttribute2().split(";");
				for (int i = 0; i < attribute2.length; i++) {
					if (StringUtils.hasText(attribute2[i])) {						
						mailAddress.add(attribute2[i]);
					}
				}

				// 前半部主旨
				description = buCommonPhraseLine.getDescription();
			}
			String createdBy = head.getCreatedBy();
			if(createdBy != "T00700"){
				mailAddress.add(createdBy+"@tasameng.com.tw");
			}else{
				log.error("起單人已有在信裡");
			}
			//String supplierCode = AjaxUtils.getPropertiesValue(head.getSupplierCode(), "");
			String orderNo = head.getOrderNo();

			// 設定主旨
//			if (StringUtils.hasText(supplierCode)) {
//				supplierCode = supplierCode.concat("-");
//			}

			subject = description.concat(categoryName).concat(orderTypeCode).concat(orderNo);
			if (StringUtils.hasText(subjectError)) {
				subject = subjectError + subject;
			}
			
			String fileName = itemCategory.concat("-").concat(orderNo);			
			
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
    public List<Properties> updateCustomsStatus(Map parameterMap) throws Exception{    	
    	Map returnMap = new HashMap(0);
    	MessageBox msgBox = new MessageBox();
    	Map resultMap = new HashMap();
    	String uuid = UUID.randomUUID().toString();
    	try{
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    
    	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
    	    
    	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);
    	    String id = (String)PropertyUtils.getProperty(otherBean, "headId");    	    
    	    Long headId = NumberUtils.getLong(id);
    	    
    	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
    	    String orderNo = (String)PropertyUtils.getProperty(otherBean, "orderNo");
    	    
    	    ImDeliveryHead imDeliveryHead = imDeliveryHeadDAO.findDeliveryByIdentification(loginBrandCode,orderTypeCode,orderNo);
    	    
    	    
    	    String tranStatus = (String)PropertyUtils.getProperty(otherBean, "tranStatus");
    	    if(tranStatus.equals("cancel")){
    	    	imDeliveryHead.setTranAllowUpload("D");
    	    }else{
    	    	imDeliveryHead.setTranAllowUpload("I");
    	    }
    	    
    	    imDeliveryHead.setCustomsStatus("A");
    	    
    	    if(orderTypeCode.equals("IRP")||orderTypeCode.equals("IBT")){
    	    	imDeliveryHead.setTranRecordStatus("NF15");  
    	    }
    	    else{
    	    	throw new Exception("上傳失敗，原因：");
    	    }
    	    SiSystemLogUtils.createSystemLog("update_customs_status",
					MessageStatus.LOG_INFO, 
					"銷退單海關單點上傳 銷售單號 = "+orderTypeCode+","+imDeliveryHead.getCustomerPoNo()+
					" 上傳狀態為"+imDeliveryHead.getTranAllowUpload()+
					" user = "+loginEmployeeCode, imDeliveryHead.getShipDate(),uuid, "SYS");
    	    imDeliveryHeadDAO.save(imDeliveryHead);
    	    
    	    msgBox.setMessage("已將單據送簽海關");
    	    
    	}catch(Exception ex){
    	    log.error("上傳海關失敗，原因：" + ex.toString());
    	    msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
    	    throw new Exception("上傳失敗，原因：" + ex.toString());

    	}
    	returnMap.put("vatMessage", msgBox);
    	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
	
}