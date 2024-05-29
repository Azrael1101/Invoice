package tw.com.tm.erp.hbm.service;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.BuOrganization;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.FiBudgetModHead;
import tw.com.tm.erp.hbm.bean.ImDistributionHead;
import tw.com.tm.erp.hbm.bean.ImDistributionItem;
import tw.com.tm.erp.hbm.bean.ImDistributionLine;
import tw.com.tm.erp.hbm.bean.ImDistributionShop;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.WfApprovalResult;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.ImDistributionHeadDAO;
import tw.com.tm.erp.hbm.dao.ImDistributionItemDAO;
import tw.com.tm.erp.hbm.dao.ImDistributionLineDAO;
import tw.com.tm.erp.hbm.dao.ImDistributionShopDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.importdb.ImMovementImportData;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.QuantityUtils;
import tw.com.tm.erp.utils.UserUtils;

/**eComposeLine
 * 配貨單 Service
 *
 * @author T02049
 *
 */
public class ImDistributionHeadService {
	private static final Log log = LogFactory.getLog(ImDistributionHeadService.class);
	private ImDistributionHeadDAO imDistributionHeadDAO;
	private BuOrderTypeService buOrderTypeService;
	private ImMovementService imMovementService;
	private BuShopService buShopService;
	private ImWarehouseService imWarehouseService;
	private BuLocationService buLocationService;
	private ImItemService imItemService;
	private ImOnHandDAO imOnHandDAO;
	private BuBrandService buBrandService;
	private WfApprovalResultService wfApprovalResultService;
	private ImItemOnHandViewDAO imItemOnHandViewDAO;
	private BuBrandDAO buBrandDAO;
	private ImWarehouseDAO imWarehouseDAO;
	private ImItemDAO imItemDAO;
	private ImDistributionItemDAO imDistributionItemDAO;
	private ImDistributionLineDAO imDistributionLineDAO;
	private BuShopDAO buShopDAO;
	private ImDistributionShopDAO imDistributionShopDAO;
	private SiProgramLogAction siProgramLogAction;
	private ImReceiveHeadDAO imReceiveHeadDAO;
	private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
	private BuCommonPhraseService buCommonPhraseService;
    private ImItemCategoryService imItemCategoryService;

	private static final String DEFAULT_LOT_NO = "000000000000" ;
	public static final String PROGRAM_ID= "IM_DISTRIBUTION";

	/**
	 * 查詢欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = {
		"orderNo", "defaultWarehouseCode", "lastUpdateDate", "headId" };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {
		"", "", "", "" };

	public static final String[] GRID_FIELD_NAMESITEM = {
	"indexNo", "itemCode", "itemName", "lotNo", "quantity",
	"lineId", "isLockRecord", "isDeleteRecord", "message"};

	public static final String[] GRID_FIELD_DEFAULT_VALUESITEM = {
	"", "", "", "", "", "",
	AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE,""};

	public static final int[] GRID_FIELD_TYPESITEM = {
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};

	public static final String[] GRID_FIELD_NAMESSHOP = {
	"indexNo", "shopCode", "shopName", "warehouseCode",
	"lineId", "isLockRecord", "isDeleteRecord", "message"};

	public static final String[] GRID_FIELD_DEFAULT_VALUESSHOP = {
	"", "", "", "",
	"",	AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE,""};

	public static final int[] GRID_FIELD_TYPESSHOP = {
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};

	public static final String[] GRID_FIELD_NAMESVIEW = {
	"indexNo", "itemBrand", "category02", "category13", "itemCode",
	"itemName", "supplierItemCode", "unitPrice", "lotNo", "quantity",
	"shops", "shopQuantitys", "quantitys" , "lineId"};

	public static final String[] GRID_FIELD_DEFAULT_VALUESVIEW = {
	"", "", "", "", "",
	"", "", "", "", "",
	"", "",	"", ""};

	public static final int[] GRID_FIELD_TYPESVIEW = {
	AjaxUtils.FIELD_TYPE_LONG,  AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG};

	/**
	 * save and update
	 *
	 * @param modifyObj
	 * @return
	 * @throws Exception
	 */
	public String create(ImDistributionHead modifyObj) throws FormException, Exception {
		log.info("ImDistributionHeadService.create");
		if (null != modifyObj) {
			insertDetails(modifyObj);
			if (modifyObj.getHeadId() == null) {
				return save(modifyObj);
			} else {
				return update(modifyObj);
			}
		} else {
			throw new FormException("查無表單主檔資料");
		}
	}

	/**
	 * save
	 *
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String save(ImDistributionHead saveObj) throws FormException, Exception {
		//log.info("ImDistributionHeadService.save");
		//doAllValidate(saveObj);
		//saveObj.setOrderNo(buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), saveObj.getOrderTypeCode()));
		//saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		//saveObj.setLastUpdateDate(new Date());
		//saveObj.setCreationDate(new Date());
		imDistributionHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}

	public void getOrderNoAndSave(ImDistributionHead saveObj) throws Exception{
		saveObj.setOrderNo(buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), saveObj.getOrderTypeCode()));
		imDistributionHeadDAO.save(saveObj);
	}

	/**
	 * update
	 *
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String update(ImDistributionHead updateObj) throws FormException, Exception {
		log.info("ImDistributionHeadService.update");
		doAllValidate(updateObj);
		updateObj.setLastUpdateDate(new Date());
		imDistributionHeadDAO.update(updateObj);
		// remove item null head id
		imDistributionHeadDAO.deleteNullField(ImDistributionHead.TABLE_NAME, "HEAD_ID");
		imDistributionHeadDAO.deleteNullField(ImDistributionLine.TABLE_NAME, "HEAD_ID");
		imDistributionHeadDAO.deleteNullField(ImDistributionItem.TABLE_NAME, "HEAD_ID");
		imDistributionHeadDAO.deleteNullField(ImDistributionShop.TABLE_NAME, "HEAD_ID");
		return MessageStatus.SUCCESS;
	}

	/**
	 * 新增Detail,並計算配貨資料
	 *
	 * @param headObj
	 * @throws Exception
	 */
	private void insertDetails(ImDistributionHead headObj) throws Exception {
		log.info("ImDistributionHeadService.insertDetails");
		String items[] = null;
		String shops[] = null;
		String temp = null;
		if (StringUtils.hasText(headObj.getAllDistributionItems())) {
			temp = StringUtils.replace(headObj.getAllDistributionItems(), "'", "");
			items = temp.split(",");
		}

		if (StringUtils.hasText(headObj.getAllDistributionShops())) {
			temp = StringUtils.replace(headObj.getAllDistributionShops(), "'", "");
			shops = temp.split(",");
		}

		if ((null != items && items.length > 0) && (null != shops && shops.length > 0)) {
			List<ImDistributionLine> lines = headObj.getImDistributionLines();
			lines.clear();
			for (String item : items) {
				for (String shop : shops) {
					ImDistributionLine line = new ImDistributionLine();
					line.setItemCode(item);
					line.setShopCode(shop);
					lines.add(line);
				}
			}
			calculate(headObj, shops, items);
		}
	}

	/**
	 * get Distribution Line
	 *
	 * @param headObj
	 * @param item
	 * @param shop
	 * @return
	 */
	public ImDistributionLine getDistributionLine(ImDistributionHead headObj, String item, String shop) {
		log.info("ImDistributionHeadService.getDistributionLine item =" + item + ", shop =" + shop);
		if (null != headObj && StringUtils.hasText(item) && StringUtils.hasText(shop)) {
			List<ImDistributionLine> lines = headObj.getImDistributionLines();
			for (ImDistributionLine line : lines) {
				if (item.equalsIgnoreCase(line.getItemCode()) && shop.equalsIgnoreCase(line.getShopCode())) {
					return line;
				}
			}
		}
		return null;
	}

	/**
	 * get shop distribution all item quantity
	 *
	 * @param headObj
	 * @param shop
	 * @return
	 */
	public Double getShopDistributionAllItemQty(ImDistributionHead headObj, String shop) {
		log.info("ImDistributionHeadService.getShopDistributionAllItemQty shop =" + shop);
		Double allDistributionQty = 0D;
		if (null != headObj && StringUtils.hasText(shop)) {
			List<ImDistributionLine> lines = headObj.getImDistributionLines();
			for (ImDistributionLine line : lines) {
				if (shop.equalsIgnoreCase(line.getShopCode())) {
					if (null != line.getDistributionQuantity())
						allDistributionQty = allDistributionQty + line.getDistributionQuantity();
				}
			}
		}
		return allDistributionQty;
	}

	/**
	 * search
	 *
	 * @param findObjs
	 * @return
	 */
	public List<ImDistributionHead> find(HashMap findObjs) {
		log.info("ImDistributionHeadService.find");
		return imDistributionHeadDAO.find(findObjs);
	}

	/**
	 * 計算配貨資料
	 *
	 * @param calculateObj
	 * @throws Exception
	 */
	public void calculate(ImDistributionHead calculateObj, String shops[], String items[]) throws Exception {
		log.info("ImDistributionHeadService.calculate");
		BuOrganization buOrganization = buBrandService.findOrganizationByBrandCode(calculateObj.getBrandCode());
		String organizationCode = buOrganization.getOrganizationCode();
		String deliveryWarehouseCode = calculateObj.getDefaultWarehouseCode();

		for (String itemCode : items) {
			ImItem imItem = imItemService.findById(itemCode);
			String iLevel = imItem.getItemLevel();

			// 取得 ITEM ALL 配貨數量
//			Double currentStockOnHandQty = imOnHandDAO
//					.getCurrentStockOnHandQty(organizationCode, itemCode, deliveryWarehouseCode, null);
			Double currentStockOnHandQty = imItemOnHandViewDAO.getOnHandQtyByItemCode(organizationCode, itemCode, deliveryWarehouseCode, null);
			if (null == currentStockOnHandQty) {
				currentStockOnHandQty = 0D;
			}

			// 活動 預留數量
			Double promotionQty = 0D;
			// 扣掉活動預留數量之後要配貨的數量
			Double currentStockOnHandDelPromotionQty = currentStockOnHandQty - promotionQty;

			// 配貨數量
			Double distributionAllQty = currentStockOnHandDelPromotionQty;
			log.info("ImDistributionHeadService.calculate.distributionAllQty = " + distributionAllQty);

			if ((null != distributionAllQty) && (distributionAllQty > 0D)) {

				for (String shopCode : shops) {
					ImDistributionLine selLine = getDistributionLine(calculateObj, itemCode, shopCode);
					BuShop buShop = buShopService.findById(shopCode);
					String sLevel = buShop.getShopLevel();

					Double calculateQuantity = 0D; // 此SHOP配貨數量

					log.info("ImDistributionHeadService.calculate " + shopCode + " >= " + itemCode + " = " + sLevel + " >= " + iLevel);
					if (sLevel == null || iLevel == null || sLevel.compareTo(iLevel) >= 0) {
						// 公式A
						// shop warehouse code , 要配貨的 WAREHOUSE CODE
						String arrivalWarehouseCode = buShop.getSalesWarehouseCode();
						ImWarehouse imWarehouse = imWarehouseService.findById(arrivalWarehouseCode);

						// warehouse capacity
						Long warehouseCapacity = imWarehouse.getWarehouseCapacity();
						if (null == warehouseCapacity)
							warehouseCapacity = 0L;

						log.info("ImDistributionHeadService.calculate shopCode =" + shopCode + " , arrivalWarehouseCode ="
								+ arrivalWarehouseCode + " , warehouseCapacity =" + warehouseCapacity);
						// 取得目前此儲位所有商品的 capacity
						Double warehouseAllProductCount = imItemOnHandViewDAO.getOnHandQtyByItemCode(organizationCode,null,
								arrivalWarehouseCode,null);
						if (null == warehouseAllProductCount)
							warehouseAllProductCount = 0D;

						// 還有多少容積可以放置商品 = warehouseCapacity(倉庫的容積) -
						// warehouseAllProductCount(倉庫所有商品的數量) - 其他商品已經配貨的數量
						Double availableSize = warehouseCapacity - warehouseAllProductCount
								- getShopDistributionAllItemQty(calculateObj, shopCode);

						log.info("ImDistributionHeadService.calculate warehouseCapacity - warehouseAllProductCount =  availableSize => "
								+ warehouseCapacity + " - " + warehouseAllProductCount + " = " + availableSize);

						if (availableSize > 0) {
							// 取得此商品在此儲位目前的庫存數量
							Double shopStockOnHandQty = imItemOnHandViewDAO.getOnHandQtyByItemCode(organizationCode, itemCode, arrivalWarehouseCode,null);
							if (null == shopStockOnHandQty)
								shopStockOnHandQty = 0D;

							// 取得此商品在此儲位可以分配的最大數量
							Double distributionSizeA = availableSize - shopStockOnHandQty;

							// 公式B
							// 店櫃營業額比例
							Double saleRate = 0.1D;
							// 利用營業額比例取得配貨數量
							Double distributionSizeB = currentStockOnHandDelPromotionQty * saleRate;

							log.info("ImDistributionHeadService.calculate distributionSizeA=" + distributionSizeA + " , distributionSizeB="
									+ distributionSizeB);
							// 計算出來的配貨數量
							if (distributionSizeA > distributionSizeB) {
								calculateQuantity = distributionSizeB;
							} else {
								calculateQuantity = distributionSizeA;
							}
						} else {
							calculateQuantity = 0D;
						}
					}

					if (distributionAllQty >= calculateQuantity) {
						selLine.setDistributionQuantity(new Double(calculateQuantity.intValue()));
						distributionAllQty = distributionAllQty - calculateQuantity; // 剩餘數量
					} else {
						selLine.setDistributionQuantity(new Double(distributionAllQty.intValue()));
						distributionAllQty = 0D; // 剩餘數量
					}
					selLine.setQuantity(0d);
				}
			} else {
				for (String shopCode : shops) {
					ImDistributionLine selLine = getDistributionLine(calculateObj, itemCode, shopCode);
					selLine.setDistributionQuantity(0D);
					selLine.setQuantity(0D);
				}
			}
		}
	}

	/**
	 * 檢核
	 *
	 * @param headObj
	 * @return
	 * @throws Exception
	 */
	private void doAllValidate(ImDistributionHead headObj) throws FormException, Exception {
	}

	/**
	 * 建立調撥單
	 *
	 * @param headObj
	 * @throws Exception
	 */
	public void createMovement(ImDistributionHead headObj) throws FormException, Exception {
		log.info("ImDistributionHeadService.createMovement");
		if (!OrderStatus.CLOSE.equalsIgnoreCase(headObj.getStatus())) {
			String deliveryWarehouseCode = headObj.getDefaultWarehouseCode();
			ImWarehouse deliveryWarehouse = imWarehouseService.findById(deliveryWarehouseCode);

			if (null == deliveryWarehouse)
				throw new FormException("查無入庫倉庫" + deliveryWarehouseCode);

			Long deliveryLocationId = deliveryWarehouse.getLocationId();
			BuLocation deliveryLocation = buLocationService.findById(deliveryLocationId);

			if (null == deliveryLocation)
				throw new FormException("查無入庫倉庫地址資料" + deliveryLocationId);

			String deliveryWarehouseName = deliveryWarehouse.getWarehouseName();
			String deliveryContactPerson = deliveryWarehouse.getWarehouseManager();
			String deliveryAddress = deliveryLocation.getAddress();
			String deliveryArea = deliveryLocation.getArea();
			String deliveryCity = deliveryLocation.getCity();
			// String deliveryLocationName = deliveryLocation.getLocationName();
			String deliveryZipCode = deliveryLocation.getZipCode();

			List<SortedSet> shopsAndItems = getShopsAndItems(headObj);

			log.info("ImDistributionHeadService.createMovement shopsAndItems size =" + shopsAndItems.size());
			if (null != shopsAndItems && shopsAndItems.size() >= 2) {
				// List<ImDistributionLine> lines =
				// headObj.getImDistributionLines();
				SortedSet shops = shopsAndItems.get(0);
				SortedSet items = shopsAndItems.get(1);
				log.info("ImDistributionHeadService.createMovement shops size =" + shops.size() + " items size =" + items.size());
				Iterator shopsI = shops.iterator();
				while (shopsI.hasNext()) {
					ImMovementHead imMovementHead = new ImMovementHead();
					String arrivalShopCode = (String) shopsI.next();
					// 20090113 shan
					//imMovementHead.setStatus(OrderStatus.WAIT_IN);
					imMovementHead.setStatus(OrderStatus.UNCONFIRMED);
					imMovementHead.setBrandCode(headObj.getBrandCode());
					imMovementHead.setOrderTypeCode("IMD");
					imMovementHead.setCreatedBy(headObj.getCreatedBy());
					imMovementHead.setCreationDate(headObj.getCreationDate());
					imMovementHead.setLastUpdatedBy(headObj.getLastUpdatedBy());
					imMovementHead.setLastUpdateDate(headObj.getLastUpdateDate());

					imMovementHead.setDeliveryContactPerson(deliveryContactPerson);
					imMovementHead.setDeliveryDate(headObj.getLastUpdateDate());
					imMovementHead.setDeliveryWarehouseCode(deliveryWarehouseCode);
					imMovementHead.setDeliveryWarehouseName(deliveryWarehouseName);

					imMovementHead.setDeliveryAddress(deliveryAddress);
					imMovementHead.setDeliveryArea(deliveryArea);
					imMovementHead.setDeliveryCity(deliveryCity);
					imMovementHead.setDeliveryZipCode(deliveryZipCode);

					// Arrival
					BuShop arrivalBuShop = buShopService.findById(arrivalShopCode);
					String arrivalWarehouseCode = arrivalBuShop.getSalesWarehouseCode();
					ImWarehouse arrivalImWarehouse = imWarehouseService.findById(arrivalWarehouseCode);
					if (null == arrivalImWarehouse)
						throw new FormException("查無配貨店櫃" + arrivalWarehouseCode);

					Long arrivalLocationId = arrivalImWarehouse.getLocationId();
					BuLocation arrivalocation = buLocationService.findById(arrivalLocationId);
					if (null == arrivalocation)
						throw new FormException("查無配貨店櫃地址資料" + arrivalLocationId);

					imMovementHead.setArrivalContactPerson(arrivalImWarehouse.getWarehouseManager());
					imMovementHead.setArrivalDate(headObj.getArrivalDate());
					imMovementHead.setArrivalWarehouseCode(arrivalWarehouseCode);
					imMovementHead.setArrivalWarehouseName(arrivalImWarehouse.getWarehouseName());

					imMovementHead.setArrivalAddress(arrivalocation.getAddress());
					imMovementHead.setArrivalArea(arrivalocation.getArea());
					imMovementHead.setArrivalCity(arrivalocation.getCity());
					imMovementHead.setArrivalZipCode(arrivalocation.getZipCode());

					Iterator itemI = items.iterator();
					// create line
					List<ImMovementItem> imMovementItems = new ArrayList();
					while (itemI.hasNext()) {
						String itemCode = (String) itemI.next();
						log.info("ImDistributionHeadService.getShopsAndItems itemCode=" + itemCode + ",arrivalShopCode=" + arrivalShopCode);
						ImItem imItem = imItemService.findItem(headObj.getBrandCode(), itemCode);
						ImDistributionLine line = getDistributionLine(headObj, itemCode, arrivalShopCode);
						ImMovementItem imMovementItem = new ImMovementItem();
						imMovementItem.setItemCode(itemCode);
						imMovementItem.setDeliveryQuantity(line.getQuantity());
						imMovementItem.setArrivalQuantity(line.getQuantity());
						imMovementItem.setDeliveryWarehouseCode(deliveryWarehouseCode);
						imMovementItem.setArrivalWarehouseCode(arrivalWarehouseCode);
						imMovementItem.setItemName(imItem.getItemCName());
						imMovementItem.setOriginalDeliveryQuantity(line.getQuantity());
						imMovementItems.add(imMovementItem);
					}
					imMovementHead.setImMovementItems(imMovementItems);

					//20090114 shan 指定批號
					ImMovementImportData imMovementImportData = new ImMovementImportData();
					String orgCode = UserUtils.getOrganizationCodeByBrandCode(imMovementHead.getBrandCode());
					List movementItemsWithLotNo = imMovementImportData.getMovementItemsWithLotNo(imMovementHead,orgCode,DEFAULT_LOT_NO);
					imMovementHead.setImMovementItems(movementItemsWithLotNo);

					imMovementService.create(imMovementHead, OrderStatus.SAVE);

					// 20090113 shan
					try {
						Object[] processInfo = ImMovementService.startProcess(imMovementHead);
						logApprovalResult(processInfo, imMovementHead, wfApprovalResultService);
					} catch (Exception ex) {
						log.error("POS調撥單別：" + imMovementHead.getOrderTypeCode() + "、單號：" + imMovementHead.getOrderNo()
								+ "匯入成功，但啟動流程並記錄歷程時發生錯誤，原因：" + ex.toString());

					}
				}
			}
			headObj.setStatus(OrderStatus.SAVE);
			// 奇怪 WHY 要放到最後 還去新增
			update(headObj);
		}
	}

	/**
	 * 取得所有要配貨的SHOP AND ITEM
	 *
	 * @param headObj
	 * @return
	 */
	public List<SortedSet> getShopsAndItems(ImDistributionHead headObj) {
		log.info("ImDistributionHeadService.getShopsAndItems");
		List shopsAndItems = new ArrayList();
		if (null != headObj) {
			List<ImDistributionLine> lines = headObj.getImDistributionLines();
			SortedSet items = new TreeSet();
			SortedSet shops = new TreeSet();

			for (ImDistributionLine line : lines) {
				log.info("ImDistributionHeadService.getShopsAndItems shop=" + line.getShopCode() + " item=" + line.getItemCode());
				items.add(line.getItemCode());
				shops.add(line.getShopCode());
			}
			shopsAndItems.add(shops);
			shopsAndItems.add(items);
		}
		log.info("ImDistributionHeadService.getShopsAndItems return ");
		return shopsAndItems;
	}

	/**
	 * 取得要配貨的WAREHOUSE ON HAND資料,會加上 allDistributionItems and imItems
	 *
	 * @param headObj
	 * @throws FormException
	 */
	public List getWarehouseOnHandItem(ImDistributionHead headObj) throws FormException {
		log.info("ImDistributionHeadService.getWarehouseOnHand");
		List reImItems = new ArrayList();
		try{

		String warehouseCode = headObj.getDefaultWarehouseCode();
		if (StringUtils.hasText(warehouseCode)) {
			List itemAndQtys = imOnHandDAO.findAllBrandShopOnHand("TM", "'" + warehouseCode + "'");
			if (itemAndQtys.size() > 0) {
				StringBuffer allDistributionItems = new StringBuffer();
				for (int index = 0; index < itemAndQtys.size(); index++) {
					Object[] itemAndQty = (Object[]) itemAndQtys.get(index);
					if (itemAndQty.length == 4) {
						String itemCode = (String) itemAndQty[2];
						Double qty = (Double) itemAndQty[3];
						if (null != qty && qty > 0) {
							reImItems.add(imItemService.findById(itemCode));
							allDistributionItems.append("'");
							allDistributionItems.append(itemCode);
							allDistributionItems.append("'");
							allDistributionItems.append(",");
						}
					} else {
						throw new FormException("findAllBrandShopOnHand 查詢資料有誤!!");
					}
				}
				if (allDistributionItems.length() > 1)
					allDistributionItems.deleteCharAt(allDistributionItems.length() - 1);
				headObj.setAllDistributionItems(allDistributionItems.toString());
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return reImItems;
	}

	/**
	 * 初始化 bean 額外顯示欄位
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		Map multiList = new HashMap(0);
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			ImDistributionHead imDistributionHead = this.findImDistributionHead(formId, otherBean, resultMap);
			List allOrderTypes = buOrderTypeService.findOrderbyType(imDistributionHead.getBrandCode(),"ID");
			multiList.put("allOrderTypes" , AjaxUtils.produceSelectorData(allOrderTypes  ,"orderTypeCode" ,"name",  true,  false));
			List allWarehouses = imWarehouseDAO.findByProperty("ImWarehouse","and brandCode = ? and categoryCode = ?",new Object[]{imDistributionHead.getBrandCode() , "W"});
			multiList.put("allWarehouses" , AjaxUtils.produceSelectorData(allWarehouses  ,"warehouseCode" ,"warehouseName",  true,  true));
			List<BuShop> allShops = buShopDAO.findShopByBrandAndEnable(brandCode, null);
			multiList.put("allShops" , AjaxUtils.produceSelectorData(allShops  ,"shopCode" ,"shopCName",  false,  false));
			List allReceiveOrderTypeCodes = buOrderTypeService.findOrderbyType(imDistributionHead.getBrandCode(),"IMR");
			multiList.put("allReceiveOrderTypeCodes" , AjaxUtils.produceSelectorData(allReceiveOrderTypeCodes  ,"orderTypeCode" ,"name",  true,  false));
			List allItemCategorys = imItemCategoryService.findByCategoryType(imDistributionHead.getBrandCode(), "ITEM_CATEGORY");
			multiList.put("allItemCategorys" , AjaxUtils.produceSelectorData(allItemCategorys  ,"categoryCode" ,"categoryName",  false,  false));
			resultMap.put("multiList",multiList);
			return resultMap;
		}catch(Exception ex){
			log.error("目標初始化失敗，原因：" + ex.toString());
			throw new Exception("目標單初始化失敗，原因：" + ex.toString());
		}
	}

	/**
	 * 查詢配貨單並且使畫面初使化
	 * @return FiBudgetModHead
	 * @throws Exception
	 */
	public ImDistributionHead findImDistributionHead(long formId, Object otherBean, Map returnMap) throws FormException, Exception {
		ImDistributionHead form = formId>0?findById(formId):executeNew(otherBean);
		if (form != null) {
			BuBrand buBrand = buBrandDAO.findById(form.getBrandCode());
			returnMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			returnMap.put("brandName", buBrand.getBrandName());
			returnMap.put("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode(form.getLastUpdatedBy()));
			returnMap.put("form", form);
			return form;
		} else {
			throw new FormException("查無配貨單單主鍵：" + formId + "的資料！");
		}
	}

	/**
	 * 產生新的配貨單
	 * @param otherBean
	 * @return
	 * @throws Exception
	 */
	public ImDistributionHead executeNew(Object otherBean)throws Exception{
		ImDistributionHead form = new ImDistributionHead();
		try {
			String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
			form.setOrderNo(AjaxUtils.getTmpOrderNo());
			form.setOrderTypeCode(orderTypeCode);
			form.setBrandCode(loginBrandCode);
			form.setStatus(OrderStatus.SAVE);
			form.setCreatedBy(loginEmployeeCode);
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setLastUpdateDate(new Date());
			form.setCreationDate(new Date());
			this.save(form);
		} catch (Exception e) {
			log.error("建立配貨單失敗,原因:"+e.toString());
			throw new Exception("建立配貨單失敗,原因:"+e.toString());
		}
		return form;
	}

	public List<Properties> getAJAXLinePageData(Properties httpRequest) throws Exception {
		try {
			List<Properties> result = new ArrayList();
			String div = httpRequest.getProperty("div");// 要顯示的HEAD_ID
			if("2".equals(div))
				this.getAJAXLinePageDataItem(httpRequest, result);
			else if("3".equals(div))
				this.getAJAXLinePageDataShop(httpRequest, result);
			else if("4".equals(div))
				this.getAJAXLinePageDataView(httpRequest, result);
			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的明細失敗！");
		}
	}

	public void getAJAXLinePageDataItem(Properties httpRequest, List<Properties> result) throws Exception{
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
		int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
		List<Properties> gridDatas = new ArrayList();
		ImDistributionHead imDistributionHead = findById(headId);
		List<ImDistributionItem> imDistributionItems = imDistributionHeadDAO.
		findPageLine("ImDistributionItem", "imDistributionHead.", headId, iSPage, iPSize);
		if (imDistributionItems != null && imDistributionItems.size() > 0) {
			for (Iterator iterator = imDistributionItems.iterator(); iterator.hasNext();) {
				ImDistributionItem imDistributionItem = (ImDistributionItem) iterator.next();
				ImItem imItem = imItemDAO.findItem(imDistributionHead.getBrandCode(), imDistributionItem.getItemCode());
				if(imItem!= null)
					imDistributionItem.setItemName(imItem.getItemCName());
				else
					imDistributionItem.setItemName("查無此商品");
			}
			Long firstIndex = imDistributionItems.get(0).getIndexNo();
			Long maxIndex = ((ImDistributionItem)imDistributionHeadDAO.
				findPageLineMaxIndex("ImDistributionItem", "imDistributionHead.", headId)).getIndexNo();
			result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMESITEM,
					GRID_FIELD_DEFAULT_VALUESITEM,imDistributionItems, gridDatas, firstIndex, maxIndex));
		} else {
			result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
					GRID_FIELD_NAMESITEM, GRID_FIELD_DEFAULT_VALUESITEM,gridDatas));
		}
	}

	public void getAJAXLinePageDataShop(Properties httpRequest, List<Properties> result) throws Exception{
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
		int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
		List<Properties> gridDatas = new ArrayList();
		ImDistributionHead imDistributionHead = findById(headId);
		List<ImDistributionShop> imDistributionShops = imDistributionHeadDAO.
		findPageLine("ImDistributionShop", "imDistributionHead.", headId, iSPage, iPSize);
		if (imDistributionShops != null && imDistributionShops.size() > 0) {
			for (Iterator iterator = imDistributionShops.iterator(); iterator.hasNext();) {
				ImDistributionShop imDistributionShop = (ImDistributionShop) iterator.next();
				BuShop buShop = buShopDAO.findShopByBrandCodeAndShopCode(
						imDistributionHead.getBrandCode(), imDistributionShop.getShopCode());
				if(buShop!= null)
					imDistributionShop.setShopName(buShop.getShopCName());
				else
					imDistributionShop.setShopName("查無此店別");
			}
			Long firstIndex = imDistributionShops.get(0).getIndexNo();
			Long maxIndex = ((ImDistributionShop)imDistributionHeadDAO.
				findPageLineMaxIndex("ImDistributionShop", "imDistributionHead.", headId)).getIndexNo();
			result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMESSHOP,
					GRID_FIELD_DEFAULT_VALUESSHOP,imDistributionShops, gridDatas, firstIndex, maxIndex));
		} else {
			result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
					GRID_FIELD_NAMESSHOP, GRID_FIELD_DEFAULT_VALUESSHOP,gridDatas));
		}
	}

	public void getAJAXLinePageDataView(Properties httpRequest, List<Properties> result) throws Exception{
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
		int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
		List<Properties> gridDatas = new ArrayList();
		ImDistributionHead imDistributionHead = findById(headId);
		List<ImDistributionItem> imDistributionItems = imDistributionHeadDAO.
		findPageLine("ImDistributionItem", "imDistributionHead.", headId, iSPage, iPSize);
		if (imDistributionItems != null && imDistributionItems.size() > 0) {
			for (Iterator iterator = imDistributionItems.iterator(); iterator.hasNext();) {
				ImDistributionItem imDistributionItem = (ImDistributionItem) iterator.next();
				ImItemCurrentPriceView view = imItemCurrentPriceViewDAO.
					findCurrentPrice(imDistributionHead.getBrandCode(),imDistributionItem.getItemCode(),"1");
				if(view!= null){
					imDistributionItem.setItemName(view.getItemCName());
					imDistributionItem.setItemBrand(view.getItemBrand());
					imDistributionItem.setCategory02(view.getCategory02());
					imDistributionItem.setCategory13(view.getCategory13());
					imDistributionItem.setSupplierItemCode(view.getSupplierItemCode());
					imDistributionItem.setUnitPrice(view.getUnitPrice());
				}else
					imDistributionItem.setItemName("查無此商品");
			}
			Long firstIndex = imDistributionItems.get(0).getIndexNo();
			Long maxIndex = ((ImDistributionItem)imDistributionHeadDAO.
				findPageLineMaxIndex("ImDistributionItem", "imDistributionHead.", headId)).getIndexNo();
			result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMESVIEW,
					GRID_FIELD_DEFAULT_VALUESVIEW,imDistributionItems, gridDatas, firstIndex, maxIndex));
		} else {
			result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
					GRID_FIELD_NAMESVIEW, GRID_FIELD_DEFAULT_VALUESVIEW,gridDatas));
		}
	}


	public List<Properties> updateOrSaveAJAXPageLinesData(Properties httpRequest) throws Exception {
		try{
			String errorMsg = null;
			String div = httpRequest.getProperty("div");
			if("2".equals(div))
				updateOrSaveAJAXPageLinesDataItem(httpRequest);
			if("3".equals(div))
				updateOrSaveAJAXPageLinesDataShop(httpRequest);
			if("4".equals(div))
				updateOrSaveAJAXPageLinesDataView(httpRequest);
		    return AjaxUtils.getResponseMsg(errorMsg);
        }catch(Exception ex){
            log.error("更新配貨單商品時發生錯誤，原因：" + ex.toString());
            throw new Exception("更新配貨單商品失敗！");
        }
	}

	public void updateOrSaveAJAXPageLinesDataItem(Properties httpRequest) throws Exception{
		String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		String saveMode = httpRequest.getProperty("saveMode");
		if("beforeLock".equals(saveMode)){
		    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMESITEM);
			if (upRecords != null) {
			    for (Properties upRecord : upRecords) {
				Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
				ImDistributionItem imDistributionItem = (ImDistributionItem)imDistributionItemDAO.findById("ImDistributionItem", lineId);
				    if(imDistributionItem != null){
				    	AjaxUtils.setPojoProperties(imDistributionItem, upRecord, GRID_FIELD_NAMESITEM, GRID_FIELD_TYPESITEM);
				    	imDistributionItemDAO.update(imDistributionItem);
				    }
			    }
			}
		}
	}

	public void updateOrSaveAJAXPageLinesDataShop(Properties httpRequest) throws Exception{
		String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		String saveMode = httpRequest.getProperty("saveMode");
		if("beforeLock".equals(saveMode)){
		    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		    Long headId = Long.valueOf(httpRequest.getProperty("headId"));
		    ImDistributionHead imDistributionHead = this.findById(headId);
		    Long indexNo = 1L;
		    Object object = imDistributionHeadDAO.findPageLineMaxIndex("ImDistributionShop", "imDistributionHead.", headId);
		    if(object != null)
		    	indexNo = ((ImDistributionShop)object).getIndexNo()+1;
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMESSHOP);
			if (upRecords != null) {
			    for (Properties upRecord : upRecords) {
				Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
				String shopCode = upRecord.getProperty("shopCode");
				if(StringUtils.hasText(shopCode)){
					ImDistributionShop imDistributionShop = (ImDistributionShop)imDistributionShopDAO.findById("ImDistributionShop", lineId);
					    if(imDistributionShop != null){
					    	AjaxUtils.setPojoProperties(imDistributionShop, upRecord, GRID_FIELD_NAMESSHOP, GRID_FIELD_TYPESSHOP);
					    	imDistributionShopDAO.update(imDistributionShop);
					    }else{
					    	imDistributionShop = new ImDistributionShop();
					    	imDistributionShop.setImDistributionHead(imDistributionHead);
					    	AjaxUtils.setPojoProperties(imDistributionShop, upRecord, GRID_FIELD_NAMESSHOP, GRID_FIELD_TYPESSHOP);
					    	imDistributionShop.setIndexNo(indexNo++);
					    	imDistributionShopDAO.save(imDistributionShop);
					    }
				    }
			    }
			}
		}
	}

	public void updateOrSaveAJAXPageLinesDataView(Properties httpRequest) throws Exception{
		String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		String saveMode = httpRequest.getProperty("saveMode");
		if("afterLock".equals(saveMode)){
		    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMESVIEW);
			if (upRecords != null) {
			    for (Properties upRecord : upRecords) {
			    	Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
					ImDistributionItem imDistributionItem = (ImDistributionItem)imDistributionItemDAO.findById("ImDistributionItem", lineId);
				    if(imDistributionItem != null){
				    	AjaxUtils.setPojoProperties(imDistributionItem, upRecord, GRID_FIELD_NAMESVIEW, GRID_FIELD_TYPESVIEW);
				    	imDistributionItemDAO.update(imDistributionItem);
				    }
			    }
			}
		}
	}

	/**
	 * 查詢該倉庫下有哪些物品數量大於零
	 */
	public List<Properties> updateChangeWarehouseCode(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	try{
    		Long headId = Long.valueOf(httpRequest.getProperty("headId"));
    		String brandCode = httpRequest.getProperty("brandCode");// 要顯示的HEAD_ID
    		String defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode");
    		ImDistributionHead imDistributionHead = this.findById(headId);
    		Long indexNo = 1L;
    		if(headId == 0)
    			throw new Exception("查無headId");
    		if(imDistributionHead == null)
    			throw new Exception("查無headId = "+headId+"之配貨單");
    		List items = imItemOnHandViewDAO.getOnHandQtyByWareHouseCodes(brandCode, defaultWarehouseCode);
    		List<ImDistributionItem> imDistributionItems = new ArrayList<ImDistributionItem>(0);
    		if (items.size() > 0) {
    			for (int index = 0; index < items.size(); index++) {
            	    Object[] item = (Object[]) items.get(index);
					Double currentOnHand = (Double)item[1];
					if(currentOnHand <= 0)
						continue;
					ImDistributionItem imDistributionItem = new ImDistributionItem();
					imDistributionItem.setImDistributionHead(imDistributionHead);
					imDistributionItem.setItemCode((String)item[0]);
					imDistributionItem.setQuantity(currentOnHand);
					imDistributionItem.setIsDeleteRecord(AjaxUtils.IS_DELETE_RECORD_FALSE);
					imDistributionItem.setIsLockRecord(AjaxUtils.IS_LOCK_RECORD_FALSE);
					imDistributionItem.setIndexNo(indexNo++);
					imDistributionItems.add(imDistributionItem);
					imDistributionItemDAO.save(imDistributionItem);
				}
    		}
    		imDistributionHead.setImDistributionItems(imDistributionItems);
    		imDistributionHeadDAO.update(imDistributionHead);
    	    result.add(properties);
    		return result;
    	}catch(Exception ex){
    	    log.error("切換庫別發生錯誤，原因：" + ex.toString());
    	    throw new Exception("切換庫別發生錯誤，原因：" + ex.getMessage());
    	}
	}

	/**
	 * 查詢該倉庫下有哪些物品數量大於零
	 */
	public List<Properties> updateChangeReceive(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	try{
    		Long headId = Long.valueOf(httpRequest.getProperty("headId"));
    		String brandCode = httpRequest.getProperty("brandCode");
    		String receiveOrderNo = httpRequest.getProperty("receiveOrderNo");
    		String receiveOrderTypeCode = httpRequest.getProperty("receiveOrderTypeCode");
    		ImDistributionHead imDistributionHead = this.findById(headId);
    		Long indexNo = 1L;
    		if(headId == 0)
    			throw new Exception("查無headId");
    		if(imDistributionHead == null)
    			throw new Exception("查無headId = "+headId+"之配貨單");

    		List<ImReceiveHead> imReceiveHeads = imReceiveHeadDAO.findByProperty("ImReceiveHead",
    				" and brandCode = ? and orderNo = ? and orderTypeCode = ?", new Object[]{brandCode,receiveOrderNo,receiveOrderTypeCode});
    		List<ImDistributionItem> imDistributionItems = new ArrayList<ImDistributionItem>(0);
    		if(imReceiveHeads == null || imReceiveHeads.size() == 0){
    			properties.setProperty("errorMsg", "查無此進貨單");
    		}else{
    			ImReceiveHead imReceiveHead = imReceiveHeads.get(0);
    			if(!OrderStatus.FINISH.equals(imReceiveHead.getStatus())){
    				properties.setProperty("errorMsg", "此進貨單的狀態不是完成");
    			}else{
	    			List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
		    		for (Iterator iterator = imReceiveItems.iterator(); iterator.hasNext();) {
		    			ImReceiveItem imReceiveItem = (ImReceiveItem) iterator.next();
						ImDistributionItem imDistributionItem = new ImDistributionItem();
						imDistributionItem.setImDistributionHead(imDistributionHead);
						imDistributionItem.setItemCode(imReceiveItem.getItemCode());
						imDistributionItem.setQuantity(imReceiveItem.getQuantity());
						imDistributionItem.setIsDeleteRecord(AjaxUtils.IS_DELETE_RECORD_FALSE);
						imDistributionItem.setIsLockRecord(AjaxUtils.IS_LOCK_RECORD_FALSE);
						imDistributionItem.setIndexNo(indexNo++);
						imDistributionItems.add(imDistributionItem);
						imDistributionItemDAO.save(imDistributionItem);
					}
		    		properties.setProperty("itemCategory", imReceiveHead.getItemCategory());
    			}
    		}
    		imDistributionHead.setImDistributionItems(imDistributionItems);
    		imDistributionHeadDAO.update(imDistributionHead);
    	    result.add(properties);
    		return result;
    	}catch(Exception ex){
    	    log.error("查詢進貨單發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢進貨單發生錯誤，原因：" + ex.getMessage());
    	}
	}

    public List<Properties> executeFindShopName(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	try{
    	    String brandCode = httpRequest.getProperty("brandCode");
    	    String shopCode = httpRequest.getProperty("shopCode");
    	    BuShop buShop = buShopDAO.findShopByBrandCodeAndShopCode(brandCode, shopCode);
    	    if(buShop!= null){
    	    	properties.setProperty("ShopName", buShop.getShopCName());
    	    	properties.setProperty("WarehouseCode", buShop.getSalesWarehouseCode());
    	    }else
    	    	properties.setProperty("ShopName", "查無此店");
    	    result.add(properties);
    		return result;
    	}catch(Exception ex){
    	    log.error("查詢店名發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢店名發生錯誤，原因：" + ex.getMessage());
    	}
	}

	/**
	 * 凍結頁面的資料 並且檢查是否有誤
	 * 若無誤則產生distributionLine
	 */
    public List<Properties> updateComposeLine(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	List errorMsgs = new ArrayList(0);
    	String message = "";
    	try{
    		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
    		String actualWarehouseCode = httpRequest.getProperty("actualWarehouseCode");
    		ImDistributionHead head = this.findById(headId);
    		String identification = MessageStatus.getIdentification(head.getBrandCode(),head.getOrderTypeCode(), head.getOrderNo());
            siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
    		List<ImDistributionShop>  imDistributionShops = head.getImDistributionShops();
    		if(imDistributionShops == null || imDistributionShops.size() == 0){
    			message = "請至少輸入一筆商店明細資料";
                siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR,
                		identification, message, head.getLastUpdatedBy());
                errorMsgs.add(message);
    		}

    		for (int i = imDistributionShops.size()-1; i >=0; i--) {
    			ImDistributionShop imDistributionShop = (ImDistributionShop) imDistributionShops.get(i);
				if(AjaxUtils.IS_DELETE_RECORD_FALSE.equals(imDistributionShop.getIsDeleteRecord())){
					BuShop buShop = buShopDAO.findShopByBrandCodeAndShopCode(
							head.getBrandCode(), imDistributionShop.getShopCode());
					if(buShop== null){
						message = "商店明細中第" + imDistributionShop.getIndexNo() +"項 店別不存在";
		                siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR,
		                		identification, message, head.getLastUpdatedBy());
		                errorMsgs.add(message);
					}
				}else{
					imDistributionShops.remove(i);
				}
			}
    		String shopCodes = "";
            if(errorMsgs.size() > 0){
            	properties.setProperty("errorMsg", "檢核未通過");
            }else{
            	List<ImDistributionItem>  imDistributionItems = head.getImDistributionItems();
				for (int i = imDistributionItems.size()-1; i >=0; i--) {
					ImDistributionItem item = (ImDistributionItem) imDistributionItems.get(i);
					if(AjaxUtils.IS_DELETE_RECORD_FALSE.equals(item.getIsDeleteRecord())){
		    			StringBuffer sb1 = new StringBuffer();
		    			StringBuffer sb2 = new StringBuffer();
		    			StringBuffer sb3 = new StringBuffer();
						for (Iterator iterator1 = imDistributionShops.iterator(); iterator1.hasNext();) {
		    				ImDistributionShop shop = (ImDistributionShop) iterator1.next();
		    				Double currentQuantity = NumberUtils.getDouble(imOnHandDAO.getCurrentStockOnHandQty("TM",
		    						item.getItemCode(), shop.getWarehouseCode(), null, head.getBrandCode()));
							sb1.append(shop.getShopCode()+",");
							sb2.append(currentQuantity+",");
							sb3.append(0+",");
		    			}
						Double actualQuantity = NumberUtils.getDouble(imOnHandDAO.getCurrentStockOnHandQty("TM",
	    						item.getItemCode(), actualWarehouseCode, null, head.getBrandCode()));
						item.setQuantity(item.getQuantity()+actualQuantity);
						item.setShops(sb1.toString().substring(0, sb1.toString().length()-1));
						item.setShopQuantitys(sb2.toString().substring(0, sb2.toString().length()-1));
						item.setQuantitys(sb3.toString().substring(0, sb3.toString().length()-1));
						shopCodes = item.getShops();
						imDistributionItemDAO.update(item);
					}else{
						imDistributionItems.remove(i);
					}
				}

				for (Iterator iterator = imDistributionItems.iterator(); iterator.hasNext();) {
					ImDistributionItem item = (ImDistributionItem) iterator.next();
					String [] quantitys = item.getQuantitys().split(",");
					Double limit = NumberUtils.getDouble(item.getQuantity());
					for (int i = 0; i < quantitys.length; i++) {
						Double quantity = NumberUtils.getDouble(quantitys[i]);
						limit -= quantity;
					}
					if(limit < 0){
						message = "商品明細第"+item.getIndexNo()+"項，品號"+item.getItemCode()+"之數量分配超過庫存允許之上限！";
		                siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		                errorMsgs.add(message);
		                log.error(message);
					}
				}
            }
            update(head);
            properties.setProperty("shopCodes", shopCodes);
    	    result.add(properties);
    		return result;
    	}catch(Exception ex){
    	    log.error("查詢店名發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢店名發生錯誤，原因：" + ex.getMessage());
    	}
	}


	/**
	 * 解凍頁面的資料
	 * 並刪除distributionLine
	 */
    public List<Properties> updateDisableLine(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
	    result.add(properties);
		return result;
    }

    /**
     * 匯入促銷商品明細
     *
     * @param headId
     * @param promotionItems
     * @throws Exception
     */
    public void executeImport(Long headId, List lines) throws Exception{
        try{
        	ImDistributionHead head = findById(headId);
        	List<ImDistributionShop> shops = head.getImDistributionShops();
	    	if(lines != null && lines.size() > 0){
	    	    for(int i = 0; i < lines.size(); i++){
	    	    	ImDistributionItem  item = (ImDistributionItem)lines.get(i);
	    	    	StringBuffer sb1 = new StringBuffer();
	    	    	for (Iterator iterator = shops.iterator(); iterator.hasNext();) {
						ImDistributionShop shop = (ImDistributionShop) iterator.next();
						Double currentQuantity = NumberUtils.getDouble(imOnHandDAO.getCurrentStockOnHandQty("TM",
	    						item.getItemCode(), shop.getWarehouseCode(), null, head.getBrandCode()));
						sb1.append(currentQuantity+",");
					}
	    	    	item.setShopQuantitys(sb1.toString().substring(0, sb1.toString().length()-1));
	    	    	item.setIsDeleteRecord(AjaxUtils.IS_DELETE_RECORD_FALSE);
	    	    	item.setIsLockRecord(AjaxUtils.IS_LOCK_RECORD_FALSE);
	    	    	item.setImDistributionHead(head);
	    	    	imDistributionItemDAO.save(item);
	    	    }
	    	}
	    	head.setImDistributionItems(lines);
	    	this.update(head);
        }catch (Exception ex) {
	        log.error("盤點商品明細匯入時發生錯誤，原因：" + ex.toString());
	        throw new Exception("盤點商品明細匯入時發生錯誤，原因：" + ex.getMessage());
	    }
    }

    public List resetXLS(byte[] buf) throws Exception{
    	List entityBeanList = new ArrayList(0);
    	//讀取XLS
    	ByteArrayInputStream inputStream = new ByteArrayInputStream(buf);
	    Workbook book = Workbook.getWorkbook(inputStream);
	    Sheet sheet = book.getSheet(0);
	    int rows = sheet.getRows();
	    for (int i = 1; i < rows; i++) {
	    	ImDistributionItem item = new ImDistributionItem();
			Cell cell = sheet.getCell(3, i);
			item.setItemCode(cell.getContents());
			cell = sheet.getCell(7, i);
			item.setQuantity(NumberUtils.getDouble(cell.getContents()));
			StringBuffer shops = new StringBuffer();
			StringBuffer quantity = new StringBuffer();
			for(int ii = 8 ; ii < sheet.getColumns() ; ii++){
				shops.append(sheet.getCell(ii, 0).getContents()+",");
				quantity.append(NumberUtils.getDouble(sheet.getCell(ii, i).getContents()).toString()+",");
			}
			item.setShops(shops.toString().substring(0, shops.toString().length()-1));
			item.setQuantitys(quantity.toString().substring(0, quantity.toString().length()-1));
			item.setIndexNo(i+0L);
			entityBeanList.add(item);
	    }
	    return entityBeanList;
    }

	/**
	 * 寫Approval Log
	 * @param processInfo
	 * @param movementHead
	 * @param wfApprovalResultService
	 */
	private void logApprovalResult(Object[] processInfo, ImMovementHead movementHead, WfApprovalResultService wfApprovalResultService) {
		WfApprovalResult approvalResult = new WfApprovalResult();
		approvalResult.setProcessId((Long) processInfo[0]);
		approvalResult.setBrandCode(movementHead.getBrandCode());
		approvalResult.setOrderTypeCode(movementHead.getOrderTypeCode());
		approvalResult.setOrderNo(movementHead.getOrderNo());
		approvalResult.setFormName(movementHead.getOrderTypeCode() + " - " + movementHead.getOrderNo());
		approvalResult.setActivityId((Long) processInfo[1]);
		approvalResult.setActivityName((String) processInfo[2]);
		approvalResult.setApprover(movementHead.getLastUpdatedBy());
		approvalResult.setDateTime(new Date());
		wfApprovalResultService.save(approvalResult);
	}

	public String getIdentification(Long headId) throws Exception{
        String id = null;
		try{
			ImDistributionHead imDistributionHead = this.findById(headId);
		    if(imDistributionHead != null)
		    	id= MessageStatus.getIdentification(imDistributionHead.getBrandCode(),imDistributionHead.getOrderTypeCode(), imDistributionHead.getOrderNo());
		    else
		        throw new NoSuchDataException("配貨單查無主鍵：" + headId + "的資料！");
		    log.info("id = " + id);
		    return id;
		}catch(Exception ex){
		    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
		    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());
		}
    }

	/**
     *  更新DistributionHead主檔
     *
     * @param parameterMap
     * @return Map
     * @throws FormException
     * @throws Exception
     */
    public Map updateParameter(Map parameterMap) throws FormException, Exception {
        HashMap resultMap = new HashMap();
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
    	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    Long headId = getHeadIdFromBean(formLinkBean);
    	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    	    ImDistributionHead head = findById(headId);
		    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, head);
		    head.setLastUpdatedBy(employeeCode);
		    //String resultMsg =  updateGetOrderNo(head);
	    	//resultMap.put("resultMsg", resultMsg);
	    	resultMap.put("entityBean", head);
   	    	return resultMap;
        }catch (Exception ex) {
        	log.error("配貨單存檔時發生錯誤，原因：" + ex.toString());
        	throw new Exception(ex.getMessage());
        }
    }

    public Long getHeadIdFromBean(Object bean) throws FormException, Exception{
    	Long headId = null;
    	String id = (String)PropertyUtils.getProperty(bean, "headId");
    	if(StringUtils.hasText(id))
    		headId = NumberUtils.getLong(id);
        else
        	throw new ValidationErrorException("傳入的主鍵為空值！");
    	return headId;
	}

    public List updateCheck(Map parameterMap , Map resultMap) throws FormException, Exception {
    	try{
	    	List errorMsgs = new ArrayList(0);
	    	String message = "";
		    Object formLinkBean = parameterMap.get("vatBeanFormLink");
		    Object otherBean = parameterMap.get("vatBeanOther");
		    Long headId = getHeadIdFromBean(formLinkBean);
		    String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
		    ImDistributionHead head = findById(headId);
		    String identification = MessageStatus.getIdentification(
		    		head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
	    	siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
		    List<ImDistributionItem>  imDistributionItems = head.getImDistributionItems();
		    for (Iterator iterator = imDistributionItems.iterator(); iterator.hasNext();) {
				ImDistributionItem item = (ImDistributionItem) iterator.next();
				String [] quantitys = item.getQuantitys().split(",");
				Double limit = NumberUtils.getDouble(item.getQuantity());
				for (int i = 0; i < quantitys.length; i++) {
					Double quantity = NumberUtils.getDouble(quantitys[i]);
					limit -= quantity;
				}
				if(limit < 0){
					message = "商品明細第"+item.getIndexNo()+"項，品號"+item.getItemCode()+"之數量分配超過庫存允許之上限！";
	                siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	                errorMsgs.add(message);
	                log.error(message);
				}
			}

			if(errorMsgs.size() > 0){
			    throw new FormException(MessageStatus.VALIDATION_FAILURE);
			}

			List <ImDistributionShop> shops = head.getImDistributionShops();
			List <ImDistributionLine> lines = new ArrayList<ImDistributionLine>(0);
			Long index = 1L;
			for (Iterator iterator = imDistributionItems.iterator(); iterator.hasNext();) {
				ImDistributionItem item = (ImDistributionItem) iterator.next();
				String [] shopQuantitys = item.getShopQuantitys().split(",");
				String [] quantitys = item.getQuantitys().split(",");
				for (int i=0 ; i<shops.size() ; i++) {
					ImDistributionShop shop = (ImDistributionShop) shops.get(i);
					ImDistributionLine line = new ImDistributionLine();
					line.setImDistributionHead(head);
					line.setItemCode(item.getItemCode());
					line.setShopCode(shop.getShopCode());
					line.setDistributionQuantity(NumberUtils.getDouble(quantitys[i]));
					line.setQuantity(NumberUtils.getDouble(quantitys[i])+NumberUtils.getDouble(shopQuantitys[i]));
					line.setCurrentQuantity(NumberUtils.getDouble(shopQuantitys[i]));
					line.setCreatedBy(head.getCreatedBy());
					line.setCreationDate(head.getCreationDate());
					line.setLastUpdatedBy(head.getLastUpdatedBy());
					line.setLastUpdateDate(head.getLastUpdateDate());
					line.setIsDeleteRecord(AjaxUtils.IS_DELETE_RECORD_FALSE);
					line.setIsLockRecord(AjaxUtils.IS_LOCK_RECORD_FALSE);
					line.setIndexNo(index++);
					lines.add(line);
				}
			}
		    head.setStatus(formStatus);
		    head.setImDistributionLines(lines);
		    String resultMsg =  updateGetOrderNo(head);
	    	resultMap.put("resultMsg", resultMsg);
		    this.update(head);
		    resultMap.put("entityBean", head);
	    	return errorMsgs;
    	}catch(FormException ex){
    		log.error(ex.toString());
    		throw new FormException(ex.getMessage());
    	}catch(Exception ex){
    		log.error(ex.toString());
    		throw new Exception(ex.getMessage());
    	}
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
    public String updateGetOrderNo(ImDistributionHead head)
	    throws ObtainSerialNoFailedException, FormException, Exception {
		if (AjaxUtils.isTmpOrderNo(head.getOrderNo())) {
		    String serialNo = buOrderTypeService.getOrderSerialNo(head.getBrandCode(), head.getOrderTypeCode());
		    if (!serialNo.equals("unknow"))
		    	head.setOrderNo(serialNo);
		    else
		    	throw new ObtainSerialNoFailedException("取得"+ head.getOrderTypeCode() + "單號失敗！");
		}
		update(head);
		return head.getOrderTypeCode() + "-" + head.getOrderNo() + "存檔成功！";
    }

    public static Object[] startProcess(ImDistributionHead form) throws ProcessFailedException{
        try{
	    	String packageId = "Im_Distribution";
            String processId = "process";
            String version = "20091217";
            String sourceReferenceType = "ImDistribution";
            HashMap context = new HashMap();
            context.put("formId", form.getHeadId());
	    	return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
        }catch (Exception ex){
           	log.error("銷退流程啟動失敗，原因：" + ex.toString());
    	    throw new ProcessFailedException("銷退流程啟動失敗！");
    	}
    }

	/**
	 * 完成任務工作
	 * @param assignmentId
	 * @param approveResult
	 * @return
	 * @throws ProcessFailedException
	 */
	public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws ProcessFailedException{
        try{
		    HashMap context = new HashMap();
		    context.put("approveResult", approveResult);
		    return ProcessHandling.completeAssignment(assignmentId, context);
		}catch (Exception ex){
		    log.error("完成調整工作任務失敗，原因：" + ex.toString());
		    throw new ProcessFailedException("完成調整工作任務失敗！");
		}
    }

	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {
	try {
		List<Properties> result = new ArrayList();
		List<Properties> gridDatas = new ArrayList();
		int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
		// ======================帶入Head的值=========================
		String orderNoS = httpRequest.getProperty("orderNoS");
		String orderNoE = httpRequest.getProperty("orderNoE");

		HashMap map = new HashMap();
		HashMap findObjs = new HashMap();
		findObjs.put(" and ORDER_TYPE_CODE  >= :orderNoS", orderNoS);
		findObjs.put(" and ORDER_TYPE_CODE  <= :orderNoE", orderNoE);
		findObjs.put(" and ORDER_NO NOT LIKE :TMP","TMP%");

		// ==============================================================
		Map fiBudgetMap = imDistributionHeadDAO.search("ImDistributionHead", findObjs,
			iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
		List<ImDistributionHead> imDistributionHeads = (List<ImDistributionHead>)
			fiBudgetMap.get(BaseDAO.TABLE_LIST);
		//log.info("imDistributionHeads.size" + imDistributionHeads.size());
		if (imDistributionHeads != null && imDistributionHeads.size() > 0) {
			Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
			Long maxIndex = (Long) imDistributionHeadDAO.search("ImDistributionHead",
					"count(HEAD_ID) as rowCount", findObjs, iSPage, iPSize,
					BaseDAO.QUERY_RECORD_COUNT).get(
					BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
			result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES,
					GRID_SEARCH_FIELD_DEFAULT_VALUES, imDistributionHeads, gridDatas,
					firstIndex, maxIndex));
		} else {
			result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES,
					GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
		}

		return result;
	} catch (Exception ex) {
		log.error("載入頁面顯示的選單查詢發生錯誤，原因：" + ex.toString());
		throw new Exception("載入頁面顯示的選單功能查詢失敗！");
	}
}
	/**
	 * 將儲存勾選的值
	 * @return
	 * @throws Exception
	 */
	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}

	/**
	 * 將回傳勾選的值
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception {
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			//log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			//log.info("getSearchSelection.picker_parameter:" + timeScope + "/" + searchKeys.toString());
			List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
			//log.info("getSearchSelection.result:" + result.size());
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
	 * find by pk
	 *
	 * @param headId
	 * @return
	 */
	public ImDistributionHead findById(Long headId) {
		return (ImDistributionHead) imDistributionHeadDAO.findByPrimaryKey(ImDistributionHead.class, headId);
	}

	public void setImDistributionHeadDAO(ImDistributionHeadDAO imDistributionHeadDAO) {
		this.imDistributionHeadDAO = imDistributionHeadDAO;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setImMovementService(ImMovementService imMovementService) {
		this.imMovementService = imMovementService;
	}

	public void setBuShopService(BuShopService buShopService) {
		this.buShopService = buShopService;
	}

	public void setImWarehouseService(ImWarehouseService imWarehouseService) {
		this.imWarehouseService = imWarehouseService;
	}

	public void setBuLocationService(BuLocationService buLocationService) {
		this.buLocationService = buLocationService;
	}

	public void setImItemService(ImItemService imItemService) {
		this.imItemService = imItemService;
	}

	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}

	public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
		this.wfApprovalResultService = wfApprovalResultService;
	}

	public ImOnHandDAO getImOnHandDAO() {
		return imOnHandDAO;
	}

	public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
		this.imOnHandDAO = imOnHandDAO;
	}

	public ImItemOnHandViewDAO getImItemOnHandViewDAO() {
		return imItemOnHandViewDAO;
	}

	public void setImItemOnHandViewDAO(ImItemOnHandViewDAO imItemOnHandViewDAO) {
		this.imItemOnHandViewDAO = imItemOnHandViewDAO;
	}

	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}

	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
		this.imWarehouseDAO = imWarehouseDAO;
	}

	public void setImDistributionItemDAO(ImDistributionItemDAO imDistributionItemDAO) {
		this.imDistributionItemDAO = imDistributionItemDAO;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	public void setBuShopDAO(BuShopDAO buShopDAO) {
		this.buShopDAO = buShopDAO;
	}

	public void setImDistributionShopDAO(ImDistributionShopDAO imDistributionShopDAO) {
		this.imDistributionShopDAO = imDistributionShopDAO;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setImReceiveHeadDAO(ImReceiveHeadDAO imReceiveHeadDAO) {
		this.imReceiveHeadDAO = imReceiveHeadDAO;
	}

	public void setImItemCurrentPriceViewDAO(
			ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
		this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
	}

	public BuCommonPhraseService getBuCommonPhraseService() {
		return buCommonPhraseService;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setImDistributionLineDAO(ImDistributionLineDAO imDistributionLineDAO) {
		this.imDistributionLineDAO = imDistributionLineDAO;
	}

	public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}

}