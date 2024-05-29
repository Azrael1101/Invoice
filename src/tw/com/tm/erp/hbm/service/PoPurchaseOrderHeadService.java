package tw.com.tm.erp.hbm.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
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
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.BuOrderTypeApproval;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.FiBudgetHead;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImItemOnHandView;
import tw.com.tm.erp.hbm.bean.ImOnHandView;
import tw.com.tm.erp.hbm.bean.ImPriceAdjustment;
import tw.com.tm.erp.hbm.bean.ImPriceList;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionCustomer;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeApprovalDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierDAO;
import tw.com.tm.erp.hbm.dao.FiBudgetHeadDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveItemDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderLineDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.StringTools;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.action.SiProgramLogAction;

/**
 * 採購單
 * 
 * @author Mac8
 * 
 */
public class PoPurchaseOrderHeadService {
	private static final Log log = LogFactory.getLog(PoPurchaseOrderHeadService.class);
	public static final String PROGRAM_ID= "PO_POPURCHASE_ORDER_HEAD";
	private PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO;
	private BuOrderTypeService buOrderTypeService;
	private BuBasicDataService buBasicDataService;
	private FiBudgetHeadService fiBudgetHeadService;
	private BuCommonPhraseService buCommonPhraseService;
	private BuAddressBookDAO buAddressBookDAO;
	// private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
	private PoPurchaseOrderLineService poPurchaseOrderLineService;
	private PoPurchaseOrderLineDAO poPurchaseOrderLineDAO;
	private ImItemService imItemService;
	private BuEmployeeWithAddressViewService buEmployeeWithAddressViewService;
	private FiBudgetHeadDAO fiBudgetHeadDAO;
	private NativeQueryDAO nativeQueryDAO;
	private ImOnHandDAO imOnHandDAO;
	private ImReceiveHeadService imReceiveHeadService;
	private BuSupplierDAO buSupplierDAO;
	private ImItemOnHandViewDAO imItemOnHandViewDAO;
	private ImItemDAO imItemDAO;
	private FiInvoiceLineDAO fiInvoiceLineDAO;
	private ImReceiveItemDAO imReceiveItemDAO;
	private BuOrderTypeApprovalDAO buOrderTypeApprovalDAO;
	private SiProgramLogAction siProgramLogAction;

	public static final String[] GRID_FIELD_NAMES = { "indexNo", "itemCode", "itemCName", "onHandQty", "unitPrice", "lastLocalUnitCost", "quantity",
			"foreignUnitCost", "foreignPurchaseAmount", "unitPriceAmount", "actualPurchaseQuantity", "lineId",
			"status", "isLockRecord", "isDeleteRecord", "message" };

	public static final int[] GRID_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
			AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
			AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING };

	public static final String[] GRID_FIELD_DEFAULT_VALUES = { "0", "", "", "0", "0", "0", "0", "0", "0", "0", "0", "", "",
			AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "" };

	public static final String PO_WAREHOUSE_CODE = "00"; // BRAND_CODE + 00
	// -> 進貨單 預設
	// WAREHOUSE_CODE
	public static final String PO_RETURN_WAREHOUSE_CODE = "AE"; // BRAND_CODE +

	/**
	 * save and update
	 * 
	 * @param modifyObj
	 * @return
	 * @throws Exception
	 */
	public String create(PoPurchaseOrderHead modifyObj) throws FormException, Exception {
		log.info("PoPurchaseOrderHeadService.create");
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

	/**
	 * save
	 * 
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String save(PoPurchaseOrderHead saveObj) throws FormException, Exception {
		log.info("PoPurchaseOrderHeadService.save");
		// 20081013 shan if close order is null
		if (!StringUtils.hasText(saveObj.getCloseOrder()))
			saveObj.setCloseOrder(PoPurchaseOrderHead.CLOSE_ORDER_N);

		doAllValidate(saveObj);
		saveObj.setOrderNo(buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), saveObj.getOrderTypeCode()));
		countHeadTotalAmount(saveObj);
		saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		
		poPurchaseOrderHeadDAO.save(saveObj);
		// log.info("PoPurchaseOrderHeadService.save end ");
		return MessageStatus.SUCCESS;

	}

	/**
	 * save tmp
	 * 
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String saveTmp(PoPurchaseOrderHead saveObj) throws FormException, Exception {
		log.info("PoPurchaseOrderHeadService.saveTmp");
		String tmpOrderNo = AjaxUtils.getTmpOrderNo();
		saveObj.setOrderNo(tmpOrderNo);
		saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		poPurchaseOrderHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String update(PoPurchaseOrderHead updateObj) throws FormException, Exception {
		log.info("PoPurchaseOrderHeadService.update");
		// 20081013 shan if close order is null
		if (!StringUtils.hasText(updateObj.getCloseOrder()))
			updateObj.setCloseOrder(PoPurchaseOrderHead.CLOSE_ORDER_N);

		doAllValidate(updateObj);
		countHeadTotalAmount(updateObj);
		updateObj.setLastUpdateDate(new Date());
		poPurchaseOrderHeadDAO.update(updateObj);
		// log.info("PoPurchaseOrderHeadService.update end ");
		return MessageStatus.SUCCESS;
	}

	/**
	 * AJAX save and update
	 * 
	 * @param modifyObj
	 * @return
	 * @throws Exception
	 */
	public String createAJAX(PoPurchaseOrderHead modifyObj) throws FormException, Exception {
		log.info("PoPurchaseOrderHeadService.createAJAX");
		if (null != modifyObj) {
			if (null == modifyObj.getHeadId() || modifyObj.getHeadId().equals(0L)) {
				modifyObj.setPoPurchaseOrderLines(null);
				setDefActualPurchaseQuantity(modifyObj);
				return save(modifyObj);
			} else {
				return updateAJAXHead(modifyObj);
			}
		} else {
			throw new FormException("查無表單主檔資料");
		}
	}

	/**
	 * update head for ajax
	 * 
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String updateAJAXHead(PoPurchaseOrderHead updateObj) throws FormException, Exception {
		log.info("PoPurchaseOrderHeadService.updateAJAXHead");
		// 20081013 shan if close order is null
		if (!StringUtils.hasText(updateObj.getCloseOrder()))
			updateObj.setCloseOrder(PoPurchaseOrderHead.CLOSE_ORDER_N);

		if (AjaxUtils.isTmpOrderNo(updateObj.getOrderNo())) {
			updateObj.setOrderNo(buOrderTypeService.getOrderSerialNo(updateObj.getBrandCode(), updateObj.getOrderTypeCode()));
		}

		// load line
		PoPurchaseOrderHead newPoHead = findById(updateObj.getHeadId());
		updateObj.setPoPurchaseOrderLines(newPoHead.getPoPurchaseOrderLines());
		BeanUtils.copyProperties(newPoHead, updateObj);

		// remove line record
		// removeAJAXLine(newPoHead);
		AjaxUtils.removeAJAXLine(newPoHead.getPoPurchaseOrderLines());

		// 如果是退貨單
		if (PoPurchaseOrderHead.PURCHASE_RETURN_ORDER_FOREIGN.equals(newPoHead.getOrderTypeCode())
				|| PoPurchaseOrderHead.PURCHASE_RETURN_ORDER_LOCAL.equals(newPoHead.getOrderTypeCode())) {
			// 合計
			countHeadTotalAmount(newPoHead);

			// 針對退貨單 作 ON_HAND & BUDGET 調整
			modifyOnHandReturnReceiveOrder(newPoHead);
		} else {
			countHeadTotalAmount(newPoHead);
		}
		newPoHead.setLastUpdateDate(new Date());
		
		//T4NG
		String code = newPoHead.getBrandCode() + newPoHead.getOrderTypeCode() + "001";
		BuOrderTypeApproval buOrderTypeApproval = buOrderTypeApprovalDAO.findById(code);
		if("T4NG".equals(updateObj.getBrandCode()) && (!OrderStatus.SAVE.equals(newPoHead.getStatus()) || !OrderStatus.VOID.equals(newPoHead.getStatus()) )){
			newPoHead.setStatus(OrderStatus.FINISH);
		}
		
		poPurchaseOrderHeadDAO.update(newPoHead);
		// log.info("PoPurchaseOrderHeadService.update end ");
		return MessageStatus.SUCCESS;
	}

	/**
	 * 複製前的初始化
	 * 
	 * @param orig
	 * @param lastUpdatedBy
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void copyInit(PoPurchaseOrderHead orig, String lastUpdatedBy) throws IllegalAccessException, InvocationTargetException {
		log.info("PoPurchaseOrderHeadService.copyInit");
		orig.setHeadId(null);
		orig.setOrderNo(null);
		orig.setStatus(OrderStatus.SAVE);
		orig.setLastUpdatedBy(lastUpdatedBy);
		List<PoPurchaseOrderLine> items = orig.getPoPurchaseOrderLines();
		List<PoPurchaseOrderLine> newItems = new ArrayList();
		for (PoPurchaseOrderLine item : items) {
			PoPurchaseOrderLine newItem = new PoPurchaseOrderLine();
			BeanUtils.copyProperties(newItem, item);
			newItem.setLineId(null);
			newItem.setPoPurchaseOrderHead(null);
			newItems.add(newItem);
		}
		orig.setPoPurchaseOrderLines(newItems);
	}

	/**
	 * find by pk
	 * 
	 * @param headId
	 * @return
	 */
	public PoPurchaseOrderHead findById(Long headId) {
		log.info("PoPurchaseOrderHeadService.findById");
		PoPurchaseOrderHead poPurchaseOrderHead = (PoPurchaseOrderHead) poPurchaseOrderHeadDAO.findByPrimaryKey(PoPurchaseOrderHead.class,
				headId);

		// 依商品編號撈取目前庫存數量
		try {
			if (poPurchaseOrderHead.getPoPurchaseOrderLines().size() > 0) {
				Iterator<PoPurchaseOrderLine> it = poPurchaseOrderHead.getPoPurchaseOrderLines().iterator();
				StringBuffer itemCodes = new StringBuffer();
				while (it.hasNext()) {
					PoPurchaseOrderLine bean = it.next();
					itemCodes.append("'").append(bean.getItemCode()).append("',");
				}
				itemCodes.deleteCharAt(itemCodes.length() - 1);
				List onHandQtyByItemCodes = imItemOnHandViewDAO.getOnHandQtyByItemCodes(itemCodes.toString());
				HashMap itemCodeAndOnHandQty = new HashMap();
				if (onHandQtyByItemCodes.size() > 0) {
					for (int index = 0; index < onHandQtyByItemCodes.size(); index++) {
						Object[] itemCodeAndQty = (Object[]) onHandQtyByItemCodes.get(index);
						if (itemCodeAndQty.length == 2) {
							String itemCode = (String) itemCodeAndQty[0];
							Double qty = (Double) itemCodeAndQty[1];
							itemCodeAndOnHandQty.put(itemCode, qty);
						}
					}
				}
				Iterator<PoPurchaseOrderLine> iterator = poPurchaseOrderHead.getPoPurchaseOrderLines().iterator();
				while (iterator.hasNext()) {
					PoPurchaseOrderLine bean = iterator.next();
					if (itemCodeAndOnHandQty.containsKey(bean.getItemCode())) {
						bean.setOnHandQty(itemCodeAndOnHandQty.get(bean.getItemCode()).toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return poPurchaseOrderHead;
	}

	/**
	 * 計算所有的合計
	 * 
	 * @param countObj
	 * @throws FormException
	 * @throws NumberFormatException
	 */
	public void countHeadTotalAmount(PoPurchaseOrderHead countObj) throws NumberFormatException, FormException {
		log.info("PoPurchaseOrderHeadService.countHeadTotalAmount");
		List<PoPurchaseOrderLine> poPurchaseOrderLines = countObj.getPoPurchaseOrderLines();
		if (null != poPurchaseOrderLines) {
			countObj.setTotalForeignPurchaseAmount(new Double(0));
			countObj.setTotalLocalPurchaseAmount(new Double(0));
			countObj.setTotalUnitPriceAmount(new Double(0));
			countObj.setTotalProductCounts(new Double(0));
			log.info("PoPurchaseOrderHeadService.poPurchaseOrderLines.size =" + poPurchaseOrderLines.size());
			for (PoPurchaseOrderLine poPurchaseOrderLine : poPurchaseOrderLines) {
				countItemTotalAmount(countObj, poPurchaseOrderLine);
				countObj.setTotalForeignPurchaseAmount(countObj.getTotalForeignPurchaseAmount()
						+ poPurchaseOrderLine.getForeignPurchaseAmount());
				countObj.setTotalLocalPurchaseAmount(countObj.getTotalLocalPurchaseAmount() + poPurchaseOrderLine.getLocalPurchaseAmount());
				countObj.setTotalProductCounts(countObj.getTotalProductCounts() + poPurchaseOrderLine.getQuantity());
				countObj.setTotalUnitPriceAmount(countObj.getTotalUnitPriceAmount() + poPurchaseOrderLine.getUnitPriceAmount());
			}
			setTaxRate(countObj);
			countObj.setTotalForeignPurchaseAmount(NumberUtils.round(countObj.getTotalForeignPurchaseAmount(), 4));
			countObj.setTaxAmount(NumberUtils.round(countObj.getTaxRate() * countObj.getTotalLocalPurchaseAmount(), 0));
			countObj.setTotalLocalPurchaseAmount(NumberUtils.round(countObj.getTotalLocalPurchaseAmount(), 0));
			// 如果不是一般採購就不扣預算
			if ("buy".equalsIgnoreCase(countObj.getPurchaseType())) {
				log.info("PoPurchaseOrderHeadService.countItemTotalAmount countObj.getPurchaseType()=" + countObj.getPurchaseType()
						+ ",countObj.getTotalUnitPriceAmount()1=" + countObj.getTotalUnitPriceAmount());
				countObj.setTotalUnitPriceAmount(NumberUtils.round(countObj.getTotalUnitPriceAmount(), 0));
				log.info("PoPurchaseOrderHeadService.countItemTotalAmount countObj.getPurchaseType()=" + countObj.getPurchaseType()
						+ ",countObj.getTotalUnitPriceAmount()2=" + countObj.getTotalUnitPriceAmount());
			} else
				countObj.setTotalUnitPriceAmount(0D);
			countHeadBudget(countObj);
		}
	}

	/**
	 * 計算ITEM的合計
	 * 
	 * @param countObj
	 * @throws FormException
	 * @throws NumberFormatException
	 */
	public void countItemTotalAmount(PoPurchaseOrderHead headObj, PoPurchaseOrderLine itemObj) throws NumberFormatException, FormException {
		log.info("PoPurchaseOrderHeadService.countItemTotalAmount");
		if ((null != headObj) && (null != itemObj))
			log.info("PoPurchaseOrderHeadService.countItemTotalAmount ForeignUnitCost=" + itemObj.getForeignUnitCost() + ",ExchangeRate="
					+ headObj.getExchangeRate() + ",Quantity=" + itemObj.getQuantity());
		if ((null != itemObj.getForeignUnitCost()) && (null != headObj.getExchangeRate()) && (null != itemObj.getQuantity())) {

			// 變更退貨單 數量為負
			changeReturnQuantity(headObj, itemObj);

			// 20090122 shan add
			itemObj.setUnitPrice(Double.valueOf(poPurchaseOrderLineService.getOriginalUnitPrice(headObj.getBrandCode(), headObj
					.getOrderTypeCode(), itemObj.getItemCode())));

			// 指定成本
			// 20090302 shan 如果是退貨單 金額非0 不做修改
			if (PoPurchaseOrderHead.PURCHASE_RETURN_ORDER_FOREIGN.equals(headObj.getOrderTypeCode())
					|| PoPurchaseOrderHead.PURCHASE_RETURN_ORDER_LOCAL.equals(headObj.getOrderTypeCode())) {
				if (itemObj.getLastLocalUnitCost() == 0)
					itemObj.setLastLocalUnitCost(Double.valueOf(poPurchaseOrderLineService.getAverageUnitCost(itemObj.getItemCode(),
							headObj.getBrandCode())));
			} else {
				itemObj.setLastLocalUnitCost(Double.valueOf(poPurchaseOrderLineService.getAverageUnitCost(itemObj.getItemCode(), headObj
						.getBrandCode())));
			}
			List imItemCurrentPriceViews = imItemService.getImItemCurrentPriceView(headObj.getBrandCode(), itemObj.getItemCode());
			String itemCName = null;
			Double supplierQuotationPrice = 0d;
			if ((null != imItemCurrentPriceViews) && (imItemCurrentPriceViews.size() > 0)) {
				ImItemCurrentPriceView imItemCurrentPriceView = (ImItemCurrentPriceView) imItemCurrentPriceViews.get(0);
				if(StringUtils.hasText(imItemCurrentPriceView.getItemCName())){
					itemCName = imItemCurrentPriceView.getItemCName();
				}else{
					itemCName = "商品未命名";
				}	
				if(itemObj.getForeignUnitCost() == null || itemObj.getForeignUnitCost() == 0d){
					itemObj.setForeignUnitCost(imItemCurrentPriceView.getSupplierQuotationPrice());
				}
			}

			itemObj.setItemCName(itemCName);
			if (PoPurchaseOrderHead.PURCHASE_RETURN_ORDER_FOREIGN.equals(headObj.getOrderTypeCode())
					|| PoPurchaseOrderHead.PURCHASE_RETURN_ORDER_LOCAL.equals(headObj.getOrderTypeCode())) {
				// 台幣單價
				itemObj.setLocalUnitCost(itemObj.getLastLocalUnitCost());
				// 台幣單價合計
				itemObj.setLocalPurchaseAmount(NumberUtils.round(itemObj.getLocalUnitCost() * itemObj.getQuantity(), 2));
				itemObj.setForeignUnitCost(itemObj.getLastLocalUnitCost());
			}else{
				// 台幣單價
				itemObj.setLocalUnitCost(NumberUtils.round(itemObj.getForeignUnitCost() * headObj.getExchangeRate(), 4));
				// 台幣單價合計
				itemObj.setLocalPurchaseAmount(NumberUtils.round(itemObj.getLocalUnitCost() * itemObj.getQuantity(), 2));
			}
			
			// 台幣售價合計
			itemObj.setUnitPriceAmount(NumberUtils.round(itemObj.getUnitPrice() * itemObj.getQuantity(), 2));
			// 原幣合計
			itemObj.setForeignPurchaseAmount(NumberUtils.round(itemObj.getForeignUnitCost() * itemObj.getQuantity(), 4));

			log.info("PoPurchaseOrderHeadService.countItemTotalAmount itemObj.setUnitPriceAmount=" + itemObj.getUnitPriceAmount());
		}
	}

	/**
	 * 計算PO單預算
	 * 
	 * @param headObj
	 */
	public void countHeadBudget(PoPurchaseOrderHead headObj) {
		log.info("PoPurchaseOrderHeadService.countHeadBudget");
		HashMap findObjs = new HashMap();
		findObjs.put("brandCode", headObj.getBrandCode());
		findObjs.put("budgetYear", headObj.getBudgetYear());
		List fiBudgetHeads = fiBudgetHeadService.find(findObjs);
		if (null != fiBudgetHeads && fiBudgetHeads.size() > 0) {
			FiBudgetHead fiBudgetHead = (FiBudgetHead) fiBudgetHeads.get(0);
			headObj.setTotalBudget(fiBudgetHead.getTotalBudget());
			headObj.setTotalAppliedBudget(fiBudgetHead.getTotalAppliedBudget());
			headObj.setTotalSigningBudget(fiBudgetHead.getTotalSigningBudget());
			Double totalRemainderBudger = NumberUtils.getDouble(fiBudgetHead.getTotalBudget())
					- NumberUtils.getDouble(fiBudgetHead.getTotalAppliedBudget())
					- NumberUtils.getDouble(fiBudgetHead.getTotalSigningBudget());
			if (OrderStatus.SAVE.equals(headObj.getStatus()) || OrderStatus.REJECT.equals(headObj.getStatus())) {
				totalRemainderBudger = totalRemainderBudger - NumberUtils.getDouble(headObj.getTotalUnitPriceAmount());
			}
			headObj.setTotalRemainderBudget(totalRemainderBudger);
		}
	}

	/**
	 * 取得比例預算
	 * 
	 * 採購已使用預算 (未簽核)本次送簽(PO)單 = LOCAL_PURCHASE_AMOUNT(台幣採購金額合計) (PO)已簽核但未核銷 =
	 * 未核銷數量(PO.STATUS=FINISH , PO單數量 actualPurchaseQuantity - 已核銷數量
	 * RECEIPTED_QUANTITY receiptedQuantity ) * PO單 台幣採購單價 localUnitCost
	 * 
	 * 已核銷 = 已核銷入庫單台幣合計(IM_RECEIVE VERIFICATION_STATUS = Y , ITEM_CODE ,
	 * LOCAL_AMOUNT )
	 * 
	 * 採購已使用預算/預算月份合計
	 * 
	 * @param headObj
	 * @return
	 */
	public double getBudget(PoPurchaseOrderHead headObj) {
		log.info("PoPurchaseOrderHeadService.getBudget");

		// List<PoPurchaseOrderLine> lines = headObj.getPoPurchaseOrderLines();

		Double allItemTotalAmount = headObj.getTotalLocalPurchaseAmount();

		Double allBugetItemAmount = new Double(0);
		// 利用itemCode取得月份預算合計
		Long month = new Long(headObj.getLastUpdateDate().getMonth());
		HashMap findObjs = new HashMap();
		findObjs.put("brandCode", headObj.getBrandCode());
		findObjs.put("budgetYear", headObj.getBudgetYear());
		findObjs.put("orderTypeCode", FiBudgetHead.BUDGET_ORDER_TYPE_CODE_PO);
		List<FiBudgetHead> budgets = fiBudgetHeadService.find(findObjs);
		if ((null != budgets) && (budgets.size() > 0)) {
			FiBudgetHead budget = budgets.get(0);
			if (FiBudgetHead.BUDGET_CHECK_TYPE_MONTH.equalsIgnoreCase(budget.getBudgetCheckType())) {
				allBugetItemAmount = allBugetItemAmount + fiBudgetHeadService.getHeadTotalAmount(month.intValue(), budget);
			} else {
				allBugetItemAmount = allBugetItemAmount + budget.getTotalBudget();
			}
			allItemTotalAmount = allItemTotalAmount + budget.getTotalAppliedBudget();
		}

		if (allItemTotalAmount > 0 && allBugetItemAmount > 0) {
			return allItemTotalAmount / allBugetItemAmount;
		} else {
			return 0;
		}
	}

	/**
	 * 重新整理頁面顯示的資料
	 * 
	 * @param headObj
	 */
	public void reloadHeadViewData(PoPurchaseOrderHead headObj) {
		log.info("PoPurchaseOrderHeadService.reloadHeadViewData");
		// head
		// 採購單負責人
		if (StringUtils.hasText(headObj.getSuperintendentCode())) {
			BuEmployeeWithAddressView buEmployeeWithAddressView = buEmployeeWithAddressViewService
					.findById(headObj.getSuperintendentCode());
			if (null != buEmployeeWithAddressView) {
				headObj.setSuperintendentName(buEmployeeWithAddressView.getChineseName());
			} else {
				headObj.setSuperintendentName(MessageStatus.DATA_NOT_FOUND);
			}
		}
		// 廠商
		if (StringUtils.hasText(headObj.getSupplierCode())) {
			BuSupplierWithAddressView buSupplierWithAddressView = buBasicDataService.findEnableSupplierById(headObj.getBrandCode(), headObj
					.getSupplierCode());
			if (null != buSupplierWithAddressView) {
				headObj.setSupplierName(buSupplierWithAddressView.getChineseName());
			} else {
				headObj.setSupplierName(MessageStatus.DATA_NOT_FOUND);
			}
		}
	}

	/**
	 * 利用供應商代號設定 countryCode,currencyCode,exchangeRate
	 * 
	 * @param headObj
	 */
	public void setFormDataBySupplierCode(String organizationCode, PoPurchaseOrderHead headObj) {
		log.info("PoPurchaseOrderHeadService.setFormDataBySupplierCode");
		if ((null != headObj) && (StringUtils.hasText(headObj.getSupplierCode())) && StringUtils.hasText(headObj.getBrandCode())) {
			BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(headObj.getBrandCode(), headObj.getSupplierCode());
			if (null != buSWAV) {
				headObj.setCountryCode(buSWAV.getCountryCode());
				headObj.setCurrencyCode(buSWAV.getCurrencyCode());
				headObj.setTaxType(buSWAV.getTaxType());
				headObj.setSupplierName(buSWAV.getChineseName());
				setTaxRate(headObj);
				if (null != buSWAV.getTaxRate())
					headObj.setTaxRate(new Double(buSWAV.getTaxRate()));
				setExchangeRateByCurrencyCode(organizationCode, headObj);
				BuAddressBook buAddressBook = (BuAddressBook) buAddressBookDAO.findByPrimaryKey(BuAddressBook.class, buSWAV
						.getAddressBookId());
				if (null != buAddressBook)
					headObj.setContactPerson(buAddressBook.getContractPerson());
			}
		}
	}
	public String getSupplierName(String brandCode,String supplierCode){
	    String supplierName ="";
	    BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(brandCode, supplierCode);
	    if(buSWAV != null){
		supplierName = AjaxUtils.getPropertiesValue(buSWAV.getChineseName(), "");
	    }
	    return supplierName;
	}
	/**
	 * 處理AJAX 供應商及相關資料
	 * 
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataBySupplierCode(Properties httpRequest) {
		log.info("PoPurchaseOrderHeadService.getAJAXFormDataBySupplierCode");
		List re = new ArrayList();
		String supplierCode = httpRequest.getProperty("supplierCode");
		String organizationCode = httpRequest.getProperty("organizationCode");
		String brandCode = httpRequest.getProperty("brandCode");
		// String orderTypeCode = httpRequest.getProperty("orderTypeCode");

		log.info("PoPurchaseOrderHeadService.getAJAXFormDataBySupplierCode brandCode=" + brandCode + ",supplierCode=" + supplierCode);
		BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(brandCode, supplierCode);
		if (null != buSWAV) {
			Properties pro = new Properties();
			pro.setProperty("CountryCode", AjaxUtils.getPropertiesValue(buSWAV.getCountryCode(), ""));
			pro.setProperty("CurrencyCode", AjaxUtils.getPropertiesValue(buSWAV.getCurrencyCode(), ""));
			pro.setProperty("TaxType", AjaxUtils.getPropertiesValue(buSWAV.getTaxType(), ""));
			pro.setProperty("SupplierName", AjaxUtils.getPropertiesValue(buSWAV.getChineseName(), ""));
			pro.setProperty("TaxRate", AjaxUtils.getPropertiesValue(getTaxRate(buSWAV.getTaxType()), "0"));
			pro.setProperty("ExchangeRate", AjaxUtils.getPropertiesValue(getExchangeRateByCurrencyCode(organizationCode, buSWAV
					.getCurrencyCode()), "1"));
			BuAddressBook buAddressBook = (BuAddressBook) buAddressBookDAO.findByPrimaryKey(BuAddressBook.class, buSWAV.getAddressBookId());
			if (null != buAddressBook) {

				BuSupplier buSupplier = buSupplierDAO.findSupplierByAddressBookIdAndBrandCode(buAddressBook.getAddressBookId(), brandCode);
				if (null != buSupplier) {
					pro.setProperty("TradeTeam", AjaxUtils.getPropertiesValue(buSupplier.getPriceTermCode(), ""));
					pro.setProperty("PaymentTermCode", AjaxUtils.getPropertiesValue(buSupplier.getPaymentTermCode(), ""));
				}

				pro.setProperty("ContactPerson", AjaxUtils.getPropertiesValue(buAddressBook.getContractPerson(), ""));
			} else
				pro.setProperty("ContactPerson", "");
			re.add(pro);
		} else {
			Properties pro = new Properties();
			pro.setProperty("CountryCode", "");
			pro.setProperty("CurrencyCode", "");
			pro.setProperty("TaxType", "");
			pro.setProperty("SupplierName", "");
			pro.setProperty("TaxRate", "0");
			pro.setProperty("ExchangeRate", "0");
			pro.setProperty("ContactPerson", "");
			pro.setProperty("TradeTeam", "");
			pro.setProperty("PaymentTermCode", "");
			re.add(pro);
		}
		return re;
	}

	/**
	 * AJAX Line Change
	 * 
	 * @param headObj(brandCode,orderTypeCode,itemCode)
	 * @throws FormException
	 */
	public List<Properties> getAJAXLineData(Properties httpRequest) throws FormException {
		log.info("PoPurchaseOrderHeadService.getAJAXLineData");
		List re = new ArrayList();
		String brandCode = httpRequest.getProperty("brandCode");
		String orderTypeCode = httpRequest.getProperty("orderTypeCode");
		String itemCode = httpRequest.getProperty("itemCode");
		System.out.println("PoPurchaseOrderHeadService.getAJAXLineData brandCode=" + brandCode + ",orderTypeCode=" + orderTypeCode
				+ ",itemCode=" + itemCode);
		if (StringUtils.hasText(brandCode) && StringUtils.hasText(orderTypeCode) && StringUtils.hasText(itemCode)) {

			try {
				Properties pro = new Properties();
				String unitPrice = AjaxUtils.getPropertiesValue(poPurchaseOrderLineService.getOriginalUnitPrice(brandCode, orderTypeCode,
						itemCode), "0");
				String foreignUnitCost = AjaxUtils.getPropertiesValue(poPurchaseOrderLineService.getLastUnitCost(itemCode), "0");
				String lastLocalUnitCost = AjaxUtils.getPropertiesValue(poPurchaseOrderLineService.getAverageUnitCost(itemCode, brandCode),
						"0");
				String lastForeignUnitCost = foreignUnitCost;
				System.out.println("imItemService.getItemCName brandCode=" + brandCode + ",itemCode=" + itemCode);
				ImItem imItem = imItemService.findItem(brandCode, itemCode);
				String itemCName = imItem == null?"無此商品主檔":imItem.getItemCName(); 
				    //AjaxUtils.getPropertiesValue(imItemService.getItemCName(brandCode, itemCode),
						//MessageStatus.DATA_NOT_FOUND);
				System.out.println("PoPurchaseOrderHeadService.getAJAXLineData itemCName=" + itemCName);

				// 依商品編號撈取庫存數量
				List onHandQtyByItemCodes = imItemOnHandViewDAO.getOnHandQtyByItemCodes("'" + itemCode + "'");
				Double qty = new Double(0);
				if (onHandQtyByItemCodes.size() > 0) {
					for (int index = 0; index < onHandQtyByItemCodes.size(); index++) {
						Object[] itemCodeAndQty = (Object[]) onHandQtyByItemCodes.get(index);
						if (itemCodeAndQty.length == 2) {
							String item = (String) itemCodeAndQty[0];
							qty = (Double) itemCodeAndQty[1];
						}
					}
				}

				String onHandQty = AjaxUtils.getPropertiesValue(qty, "0");
				System.out.println("PoPurchaseOrderHeadService.getAJAXLineData onHandQty=" + onHandQty);

				pro.setProperty("UnitPrice", unitPrice);
				pro.setProperty("ForeignUnitCost", foreignUnitCost);
				pro.setProperty("LastForeignUnitCost", lastForeignUnitCost);
				pro.setProperty("LastLocalUnitCost", lastLocalUnitCost);
				pro.setProperty("ItemCName", itemCName);
				pro.setProperty("onHandQty", onHandQty);
				re.add(pro);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return re;
	}

	/**
	 * 利用幣別指定匯率
	 * 
	 * @param headObj
	 */
	public void setExchangeRateByCurrencyCode(String organizationCode, PoPurchaseOrderHead headObj) {
		log.info("PoPurchaseOrderHeadService.setExchangeRateByCurrencyCode");
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
	 * 指定稅率
	 * 
	 * @param headObj
	 */
	public void setTaxRate(PoPurchaseOrderHead headObj) {
		log.info("PoPurchaseOrderHeadService.setTaxRate headObj.getTaxType=" + headObj.getTaxType());
		if ((null != headObj) && (null != headObj.getOrderTypeCode()) && (null != headObj.getTaxType())) {
			if (headObj.getTaxType().equalsIgnoreCase(PoPurchaseOrderHead.PURCHASE_ORDER_TAX)) {
				headObj.setTaxRate(new Double(0.05d));
			} else {
				headObj.setTaxRate(new Double(0));
			}
		}
	}

	/**
	 * AJAX TaxRate Change
	 * 
	 * @param headObj(brandCode,orderTypeCode,itemCode)
	 * @throws FormException
	 */
	public List<Properties> getAJAXTaxRate(Properties httpRequest) throws FormException {
		log.info("PoPurchaseOrderHeadService.getAJAXTaxRate");
		List re = new ArrayList();
		String taxType = httpRequest.getProperty("taxType");
		if (StringUtils.hasText(taxType)) {
			Properties pro = new Properties();
			pro.setProperty("TaxRate", getTaxRate(taxType));
			re.add(pro);
		}
		return re;
	}

	/**
	 * AJAX 取得預算
	 * 
	 * @param headObj
	 */
	public List<Properties> getAJAXHeadBudget(Properties httpRequest) {
		log.info("PoPurchaseOrderHeadService.getAJAXHeadBudget");
		String brandCode = httpRequest.getProperty("brandCode");
		String budgetYear = httpRequest.getProperty("budgetYear");
		HashMap findObjs = new HashMap();
		findObjs.put("brandCode", brandCode);
		findObjs.put("budgetYear", budgetYear);
		List fiBudgetHeads = fiBudgetHeadService.find(findObjs);
		List re = new ArrayList();
		if (null != fiBudgetHeads && fiBudgetHeads.size() > 0) {
			FiBudgetHead fiBudgetHead = (FiBudgetHead) fiBudgetHeads.get(0);
			Properties pro = new Properties();
			pro.setProperty("TotalBudget", AjaxUtils.getPropertiesValue(fiBudgetHead.getTotalBudget(), "0"));
			pro.setProperty("TotalAppliedBudget", AjaxUtils.getPropertiesValue(fiBudgetHead.getTotalAppliedBudget(), "0"));
			pro.setProperty("TotalSigningBudget", AjaxUtils.getPropertiesValue(fiBudgetHead.getTotalSigningBudget(), "0"));
			re.add(pro);
		} else {
			Properties pro = new Properties();
			pro.setProperty("TotalBudget", "0");
			pro.setProperty("TotalAppliedBudget", "0");
			pro.setProperty("TotalSigningBudget", "0");
			re.add(pro);
		}
		return re;
	}

	/**
	 * AJAX 取得預算及合計資料 , 如果有移除的動作這個階段不會作 , 要等到 SAVE OR SUBMIT
	 * 
	 * @param headObj
	 * @throws FormException
	 * @throws NumberFormatException
	 */
	public List<Properties> getAJAXHeadTotalAmount(Properties httpRequest) throws NumberFormatException, FormException {
		log.info("PoPurchaseOrderHeadService.getAJAXHeadTotalAmount ");
		List re = new ArrayList();
		String headId = httpRequest.getProperty("headId");
		String taxType = httpRequest.getProperty("taxType");
		String exchangeRate = httpRequest.getProperty("exchangeRate");
		String purchaseType = httpRequest.getProperty("purchaseType");

		log.info("PoPurchaseOrderHeadService.getAJAXHeadTotalAmount headId=" + headId + ",taxType=" + taxType + ",exchangeRate="
				+ exchangeRate);
		if (StringUtils.hasText(headId)) {
			PoPurchaseOrderHead countObj = findById(NumberUtils.getLong(headId));
			// 加上要計算的欄位 getTaxType , getExchangeRate
			countObj.setTaxType(taxType);
			countObj.setExchangeRate(NumberUtils.getDouble(exchangeRate));
			countObj.setPurchaseType(purchaseType);
			countHeadTotalAmount(countObj);
			Double totalLocalPurchaseAmount = countObj.getTotalLocalPurchaseAmount();
			Double totalForeignPurchaseAmount = countObj.getTotalForeignPurchaseAmount();
			Double taxAmount = countObj.getTaxAmount();
			Double totalProductCounts = countObj.getTotalProductCounts();
			Double totalBudget = countObj.getTotalBudget();
			Double totalAppliedBudget = countObj.getTotalAppliedBudget();
			Double totalRemainderBudget = countObj.getTotalRemainderBudget();
			Double totalUnitPriceAmount = countObj.getTotalUnitPriceAmount();

			log.info("PoPurchaseOrderHeadService.getAJAXHeadTotalAmount headId=" + headId + ",totalLocalPurchaseAmount="
					+ totalLocalPurchaseAmount);

			DecimalFormat formatter = new DecimalFormat("#.00");
			Properties pro = new Properties();
			pro.setProperty("TotalLocalPurchaseAmount", NumberUtils.roundToStr(totalLocalPurchaseAmount, 0));
			pro.setProperty("TotalForeignPurchaseAmount", NumberUtils.roundToStr(totalForeignPurchaseAmount, 4));
			pro.setProperty("TaxAmount", formatter.format(taxAmount));
			pro.setProperty("TotalProductCounts", formatter.format(totalProductCounts));
			pro.setProperty("TotalBudget", NumberUtils.roundToStr(totalBudget, 4));
			pro.setProperty("TotalAppliedBudget", NumberUtils.roundToStr(totalAppliedBudget, 4));
			pro.setProperty("TotalRemainderBudget", NumberUtils.roundToStr(totalRemainderBudget, 4));
			pro.setProperty("TotalUnitPriceAmount", NumberUtils.roundToStr(totalUnitPriceAmount, 4));
			re.add(pro);
		}

		return re;
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
			NoSuchMethodException,Exception {
		log.info("PoPurchaseOrderHeadService.getAJAXPageData");

		// 要顯示的HEAD_ID
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		List<Properties> re = new ArrayList();
		List<Properties> gridDatas = new ArrayList();
		int iSPage = AjaxUtils.getStartPage(httpRequest);
		int iPSize = AjaxUtils.getPageSize(httpRequest);
		
		log.info("PoPurchaseOrderHeadService.getAJAXPageData headId=" + headId + ",iSPage=" + iSPage + ",iPSize=" + iPSize);
		List<PoPurchaseOrderLine> poPurchaseOrderLines = poPurchaseOrderLineService.findPageLine(headId, iSPage, iPSize);
		if (null != poPurchaseOrderLines && poPurchaseOrderLines.size() > 0) {
			log.info("PoPurchaseOrderHeadService.getAJAXPageData AjaxUtils.poPurchaseOrderLines=" + poPurchaseOrderLines.size());
			// 依商品編號撈取目前庫存數量
			try {
				Iterator<PoPurchaseOrderLine> it = poPurchaseOrderLines.iterator();
				StringBuffer itemCodes = new StringBuffer();
				String brandCode = ((PoPurchaseOrderLine)poPurchaseOrderLines.get(0)).getPoPurchaseOrderHead().getBrandCode();
				while (it.hasNext()) {
					PoPurchaseOrderLine bean = it.next();
					itemCodes.append("'").append(bean.getItemCode()).append("',");
					
				}
				itemCodes.deleteCharAt(itemCodes.length() - 1);
				Map itemCNameMap = imItemDAO.getItemCNameByItemCodes(itemCodes.toString(), brandCode);
				List onHandQtyByItemCodes = imItemOnHandViewDAO.getOnHandQtyByItemCodes(itemCodes.toString());
				HashMap itemCodeAndOnHandQty = new HashMap();
				if (onHandQtyByItemCodes.size() > 0) {
					for (int index = 0; index < onHandQtyByItemCodes.size(); index++) {
						Object[] itemCodeAndQty = (Object[]) onHandQtyByItemCodes.get(index);
						if (itemCodeAndQty.length == 2) {
							String itemCode = (String) itemCodeAndQty[0];
							Double qty = (Double) itemCodeAndQty[1];
							itemCodeAndOnHandQty.put(itemCode, qty);
						}
					}
				}
				PoPurchaseOrderHead head =  findById(headId);
				Iterator<PoPurchaseOrderLine> iterator = poPurchaseOrderLines.iterator();
				while (iterator.hasNext()) {
					PoPurchaseOrderLine bean = iterator.next();
					countItemTotalAmount(head, bean);
					bean.setItemCName((String)itemCNameMap.get(bean.getItemCode()));
					if (itemCodeAndOnHandQty.containsKey(bean.getItemCode())) {
						bean.setOnHandQty(itemCodeAndOnHandQty.get(bean.getItemCode()).toString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 取得第一筆的INDEX
			Long firstIndex = poPurchaseOrderLines.get(0).getIndexNo();
			// 取得最後一筆 INDEX
			Long maxIndex = poPurchaseOrderLineService.findPageLineMaxIndex(Long.valueOf(headId));

			re.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, poPurchaseOrderLines, gridDatas,
					firstIndex, maxIndex));

		} else {
			re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));
		}
		return re;
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
		log.info("PoPurchaseOrderHeadService.updateAJAXPageLinesData");
		String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		String status = httpRequest.getProperty("status");
		String brandCode = httpRequest.getProperty("brandCode");
		Double exchangeRate = NumberUtils.getDouble(httpRequest.getProperty("exchangeRate"));
		String orderTypeCode = httpRequest.getProperty("orderTypeCode");
		String errorMsg = null;
		log.info("PoPurchaseOrderHeadService.updateAJAXPageLinesData gridLineFirstIndex=" + gridLineFirstIndex + ",gridRowCount="
				+ gridRowCount + ",headId=" + headId + ",status=" + status + ",gridData=" + gridData);

		if (OrderStatus.SAVE.equalsIgnoreCase(status)) {
			// 取得LINE MAX INDEX
			// PoPurchaseOrderHead head = (PoPurchaseOrderHead)
			// poPurchaseOrderHeadDAO.findByPrimaryKey(PoPurchaseOrderHead.class,
			// headId);
			// CREATE TMP HEAD
			PoPurchaseOrderHead head = new PoPurchaseOrderHead();
			head.setHeadId(headId);
			head.setExchangeRate(exchangeRate);
			head.setBrandCode(brandCode);
			head.setOrderTypeCode(orderTypeCode);
			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
					PoPurchaseOrderHeadService.GRID_FIELD_NAMES);
			// Get INDEX NO
			int indexNo = poPurchaseOrderLineService.findPageLineMaxIndex(headId).intValue();
			if (null != upRecords && null != head) {
				for (Properties upRecord : upRecords) {
					// 先載入HEAD_ID OR LINE DATA
					Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
					log.info("head Id=" + head.getHeadId() + " ,line Id=" + lineId);
					String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);
					// 如果沒有ITEM CODE 就不會新增或修改
					if (StringUtils.hasText(itemCode)) {
						PoPurchaseOrderLine poPurchaseOrderLine = poPurchaseOrderLineService.findLine(head.getHeadId(), lineId);
						if (null != poPurchaseOrderLine) {
							// UPDATE Properties
							// setLine(poPurchaseOrderLine, upRecord);
							AjaxUtils.setPojoProperties(poPurchaseOrderLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
							// LINE 計算

							// 依商品編號撈取庫存敗量
							List onHandQtyByItemCodes = imItemOnHandViewDAO.getOnHandQtyByItemCodes("'" + poPurchaseOrderLine.getItemCode()
									+ "'");
							if (onHandQtyByItemCodes.size() > 0) {
								for (int index = 0; index < onHandQtyByItemCodes.size(); index++) {
									Object[] itemCodeAndQty = (Object[]) onHandQtyByItemCodes.get(index);
									if (itemCodeAndQty.length == 2) {
										String item = (String) itemCodeAndQty[0];
										Double qty = (Double) itemCodeAndQty[1];
										poPurchaseOrderLine.setOnHandQty(qty.toString());
									}
								}
							}

							countItemTotalAmount(head, poPurchaseOrderLine);
							poPurchaseOrderLineDAO.update(poPurchaseOrderLine);
						} else {
							// INSERT Properties
							indexNo++;
							poPurchaseOrderLine = new PoPurchaseOrderLine();
							// setLine(poPurchaseOrderLine, upRecord);
							AjaxUtils.setPojoProperties(poPurchaseOrderLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
							poPurchaseOrderLine.setIndexNo(Long.valueOf(indexNo));
							poPurchaseOrderLine.setPoPurchaseOrderHead(head);
							// LINE 計算
							countItemTotalAmount(head, poPurchaseOrderLine);
							poPurchaseOrderLineDAO.save(poPurchaseOrderLine);
						}
						// return new page
						// getAJAXPageData(httpRequest);
					} else {
						// line 沒有 ITEM_CODE
					}
				}
			} else {
				errorMsg = "沒有採購單單頭資料";
			}
		} else {
			// 如果狀態不是暫存 是否要提示訊息
		}

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
		log.info("PoPurchaseOrderHeadService.deleteAJAXAllLinesData");
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
		log.info("PoPurchaseOrderHeadService.getTaxRate taxType=" + taxType);
		Double tmp = new Double(0);
		if (StringUtils.hasText(taxType)) {
			if (PoPurchaseOrderHead.PURCHASE_ORDER_TAX.equalsIgnoreCase(taxType)) {
				tmp = new Double(0.05);
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
		log.info("PoPurchaseOrderHeadService.getAJAXExchangeRateByCurrencyCode");
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

	/**
	 * 利用幣別指定匯率
	 * 
	 * @param headObj
	 */
	public String getExchangeRateByCurrencyCode(String organizationCode, String currencyCode) {
		log.info("PoPurchaseOrderHeadService.getExchangeRateByCurrencyCode organizationCode=" + organizationCode + ",currencyCode="
				+ currencyCode);
		Double tmp = new Double(1);
		if (StringUtils.hasText(organizationCode) && (StringUtils.hasText(currencyCode))) {
			BuExchangeRate bxr = buBasicDataService.getLastExchangeRate(organizationCode, currencyCode, buCommonPhraseService
					.getBuCommonPhraseLineName("SystemConfig", "LocalCurrency"));
			if (null != bxr) {
				tmp = bxr.getExchangeRate();
			}
		}
		return tmp.toString();
	}

	/**
	 * do master validate
	 * 
	 * @param headObj
	 */
	public void doValidate(PoPurchaseOrderHead headObj) {
		log.info("PoPurchaseOrderHeadService.doValidate");

	}

	/**
	 * 移除所有 TMP 單據
	 */
	public void deleteTmpOrder() {
		log.info("PoPurchaseOrderHeadService.deleteTmpOrder");
		poPurchaseOrderHeadDAO.removeTmpOrder();
	}

	/**
	 * 檢核
	 * 
	 * @param headObj
	 * @return
	 * @throws Exception
	 */
	private boolean doAllValidate(PoPurchaseOrderHead headObj) throws FormException, Exception {
		log.info("PoPurchaseOrderHeadService.doAllValidate");
		String identification = MessageStatus.getIdentification(headObj.getBrandCode(), 
    			headObj.getOrderTypeCode(), headObj.getOrderNo());
		boolean isError = false;
		// 20090103 shan 因為LINE要分開儲存了
		boolean noLine = removeDetailItemCode(headObj);
		StringBuffer errorMessage = new StringBuffer();
		if (OrderStatus.SIGNING.equalsIgnoreCase(headObj.getStatus())) {
			// 20090116 shan
			if (noLine) {
				//throw new FormException(MessageStatus.ERROR_NO_DETAIL);
				errorMessage.append("請輸入明細資料<br>");
			}
			if (null == headObj.getPurchaseOrderDate()){
				//throw new FormException("請輸入採購日期");
				errorMessage.append("請輸入採購日期<br>");
			}
				
			if (null == headObj.getSupplierCode()){
				//throw new FormException("請輸入廠商代號");
				errorMessage.append("請輸入廠商代號<br>");
			}
				
			if (!StringUtils.hasText( headObj.getSuperintendentCode())){
				//throw new FormException("請輸入採購單負責人");
				errorMessage.append("請輸入採購單負責人<br>");
			}
			if (StringUtils.hasText(headObj.getBudgetYear())) {
				HashMap findObjs = new HashMap();
				findObjs.put("brandCode", headObj.getBrandCode());
				findObjs.put("budgetYear", headObj.getBudgetYear());
				findObjs.put("orderTypeCode", "PO");
				List<FiBudgetHead> budgets = fiBudgetHeadDAO.find(findObjs);
				if (null == budgets){
					//throw new FormException("請先建立預算資料");
					errorMessage.append("請先建立預算資料<br>");
				}
					
			} else {
				//throw new FormException("請選擇預算年度");
				errorMessage.append("請選擇預算年度<br>");
			}
			if(errorMessage.length() != 0){
				//insert into errorMessage;
				isError = true;
				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, errorMessage.toString(), headObj.getCreatedBy());
			}
			
			/*
			 * if (checkDetailItemCode(headObj)) { throw new
			 * FormException(MessageStatus.ERROR_NO_DETAIL); }
			 */
			// 等到確認才開啟
			// doValidate(headObj);
			// detail validate
			/*
			 * 明細確認 List<PoPurchaseOrderLine> items =
			 * headObj.getPoPurchaseOrderLines(); for(PoPurchaseOrderLine item :
			 * items){ poPurchaseOrderLineService.doValidate(item); }
			 */
		}
		return isError;
	}

	/**
	 * 移除空白的Detail
	 * 
	 * @param headObj
	 * @return boolean 是否還有 Detail
	 */
	private boolean removeDetailItemCode(PoPurchaseOrderHead headObj) {
		log.info("PoPurchaseOrderHeadService.checkDetailItemCode");
		List<PoPurchaseOrderLine> items = headObj.getPoPurchaseOrderLines();
		Iterator<PoPurchaseOrderLine> it = items.iterator();
		while (it.hasNext()) {
			PoPurchaseOrderLine line = it.next();
			/*
			 * if (!StringUtils.hasText(line.getItemCode())) { return true; }
			 */

			if (!StringUtils.hasText(line.getItemCode())) {
				it.remove();
				items.remove(line);
			}
		}
		// log.info("is empty " + items.isEmpty());
		return items.isEmpty();
		// return false;
	}

	/**
	 * set default actual purchase quantity
	 * 
	 * @param headObj
	 */
	private void setDefActualPurchaseQuantity(PoPurchaseOrderHead headObj) {
		log.info("PoPurchaseOrderHeadService.setDefActualPurchaseQuantity");
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
	public List<PoPurchaseOrderHead> updateAllPOBudget() throws FormException {
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
	public void updateBudget(PoPurchaseOrderHead poPurchaseOrderHead) throws FormException {
		// 進貨 totalUnitPriceAmount = + , 退貨單 totalUnitPriceAmount = -
		log.info("PoPurchaseOrderHeadService.minusBudget poPurchaseOrderHead.getHeadId()=" + poPurchaseOrderHead.getHeadId()
				+ ",poPurchaseOrderHead.orderNo=" + poPurchaseOrderHead.getOrderNo() + ",year=" + poPurchaseOrderHead.getBudgetYear());

		Double totalAppliedBudget = 0D;
		Double totalSigningBudget = 0D;
		Double totalUnitPriceAmount = 0D;
		Double totalRemainderBudget = 0D;
		Double totalBudget = 0D;

		if (null != poPurchaseOrderHead) {
			String brandCode = poPurchaseOrderHead.getBrandCode();
			String status = poPurchaseOrderHead.getStatus();
			String year = poPurchaseOrderHead.getBudgetYear();

			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("budgetYear", year);
			findObjs.put("orderTypeCode", "PO");
			List<FiBudgetHead> budgets = fiBudgetHeadDAO.find(findObjs);

			if (null != budgets && budgets.size() > 0) {
				FiBudgetHead budget = budgets.get(0);
				totalBudget = budget.getTotalBudget();
			}

			// String sqlTotalAppliedBudget = "select
			// sum(TOTAL_UNIT_PRICE_AMOUNT) as totalAppliedBudget from
			// po_purchase_order_head where (status = 'FINISH' or status =
			// 'CLOSE') and PURCHASE_TYPE = 'buy' and BUDGET_YEAR ='"
			// + year + "' and brand_code ='" + brandCode + "'";
			// List list =
			// nativeQueryDAO.executeNativeSql(sqlTotalAppliedBudget);
			// if (null != list && list.size() > 0) {
			// Object obj = (Object) list.get(0);
			// if (null != obj) {
			// totalAppliedBudget = ((BigDecimal) obj).doubleValue();
			// }
			// }
			totalAppliedBudget = getTotalAppliedBudget(year, brandCode);

			// String sqlTotalSigningBudget = "select
			// sum(TOTAL_UNIT_PRICE_AMOUNT) as totalSigningBudget from
			// po_purchase_order_head where status = 'SIGNING' and PURCHASE_TYPE
			// = 'buy' and BUDGET_YEAR ='"
			// + year + "' and brand_code ='" + brandCode + "'";
			// list = nativeQueryDAO.executeNativeSql(sqlTotalSigningBudget);
			// if (null != list && list.size() > 0) {
			// Object obj = (Object) list.get(0);
			// if (null != obj) {
			// totalSigningBudget = ((BigDecimal) obj).doubleValue();
			// }
			// }
			totalSigningBudget = getTotalSigningBudget(year, brandCode);

			if (null != poPurchaseOrderHead.getTotalUnitPriceAmount()) {

				totalUnitPriceAmount = poPurchaseOrderHead.getTotalUnitPriceAmount();
				if ((!OrderStatus.SIGNING.equals(status)) && (!OrderStatus.FINISH.equals(status)) && (!OrderStatus.VOID.equals(status))) {
					totalRemainderBudget = totalBudget - totalAppliedBudget - totalSigningBudget - totalUnitPriceAmount;
				} else {
					totalRemainderBudget = totalBudget - totalAppliedBudget - totalSigningBudget;
				}
			}

			poPurchaseOrderHead.setTotalAppliedBudget(totalAppliedBudget);
			poPurchaseOrderHead.setTotalBudget(totalBudget);
			poPurchaseOrderHead.setTotalRemainderBudget(totalRemainderBudget);
			poPurchaseOrderHead.setTotalSigningBudget(totalSigningBudget);

			modifyBudget(brandCode, year, totalAppliedBudget, totalSigningBudget);
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
	private FiBudgetHead modifyBudget(String brandCode, String budgetYear, double totalAppliedBudget, double totalSigningBudget) {
		log.info("PoPurchaseOrderHeadService.modifyBudget brandCode=" + brandCode + ",budgetYear=" + budgetYear + ",totalAppliedBudget="
				+ totalAppliedBudget + ",totalSigningBudget=" + totalSigningBudget);
		HashMap findObjs = new HashMap();
		findObjs.put("brandCode", brandCode);
		findObjs.put("budgetYear", budgetYear);
		findObjs.put("orderTypeCode", "PO");
		List<FiBudgetHead> budgets = fiBudgetHeadDAO.find(findObjs);
		if (null != budgets && budgets.size() > 0) {
			FiBudgetHead fiBudgetHead = budgets.get(0);
			fiBudgetHead.setTotalAppliedBudget(totalAppliedBudget);
			fiBudgetHead.setTotalSigningBudget(totalSigningBudget);
			fiBudgetHeadDAO.update(fiBudgetHead);
			return fiBudgetHead;
		}
		return null;
	}

	// 退貨單的轉換
	private void changeReturnQuantity(PoPurchaseOrderHead poPurchaseOrderHead, PoPurchaseOrderLine poPurchaseOrderLine) {
		log.info("PoPurchaseOrderHeadService.changeReturnQuantity");
		if (PoPurchaseOrderHead.PURCHASE_RETURN_ORDER_FOREIGN.equals(poPurchaseOrderHead.getOrderTypeCode())
				|| PoPurchaseOrderHead.PURCHASE_RETURN_ORDER_LOCAL.equals(poPurchaseOrderHead.getOrderTypeCode())) {
			if (poPurchaseOrderLine.getQuantity() > 0)
				poPurchaseOrderLine.setQuantity(poPurchaseOrderLine.getQuantity() * -1);
		}
	}

	// 寫入ON_HAND,寫入預算 , UNCONFIRMED 才可以寫 , FINISH 完成
	private void modifyOnHandReturnReceiveOrder(PoPurchaseOrderHead poPurchaseOrderHead) throws Exception {
		log.info("PoPurchaseOrderHeadService.modifyOnHandReturnReceiveOrder");
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
		log.info("PoPurchaseOrderHeadService.movePOToDeliveryAndModifyOnHand");

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
		// log.info("PoPurchaseOrderHeadService.movePOToDeliveryAndModifyOnHand
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
		log.info("PoPurchaseOrderHeadService.close");
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
		log.info("PoPurchaseOrderHeadService.updateUncomfirmOrder");
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
    /**
     * 更新採購主檔及明細檔
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updateAJAXPoPurchase(Map parameterMap) throws FormException, Exception {
        
        HashMap resultMap = new HashMap();
        try{
	    	Object formBindBean = parameterMap.get("vatBeanFormBind");
	    	Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    	Object otherBean = parameterMap.get("vatBeanOther");
	    	String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	    	String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
	    	String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
	    	String processId = (String)PropertyUtils.getProperty(otherBean, "processId");
	    	String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
	    	Long headId = getPoPurchaseHeadId(formLinkBean);
	    	if(!StringUtils.hasText(formStatus)){
	    	    throw new ValidationErrorException("單據狀態參數為空值，無法執行存檔！");	
	    	}
	    	//取得欲更新的bean
	    	
	    	PoPurchaseOrderHead poPurchase = getActualPoPurchase(headId);
	    	poPurchase.setStatus(formStatus);
	    	if(OrderStatus.SAVE.equals(formStatus) || OrderStatus.VOID.equals(formStatus) ||
	    		((OrderStatus.SAVE.equals(beforeChangeStatus) || OrderStatus.REJECT.equals(beforeChangeStatus))
	    		    && OrderStatus.SIGNING.equals(formStatus))){
	    	    	setParameterToBean(otherBean, poPurchase);
	        	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, poPurchase);       	    
	    	}
	    	
	    	//========================檢核資料======================================
	    	if((OrderStatus.SAVE.equals(beforeChangeStatus) || OrderStatus.REJECT.equals(beforeChangeStatus)) 
	    		&& OrderStatus.SIGNING.equals(formStatus)){  
	    		System.out.println("----------validate Po start----------");
	    		String identification = MessageStatus.getIdentification(poPurchase.getBrandCode(), 
	    			poPurchase.getOrderTypeCode(), poPurchase.getOrderNo());
	    		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification); 
	    		checkPoPurchaseData(poPurchase,PROGRAM_ID,identification);
	    		System.out.println("----------validate Po End----------");
	    	    //============remove delete mark record=============
	    	    removeDetailItemCode(poPurchase);
	    	    //removeDeleteMarkLineForShop(promotionPO);
	    	}
	    	
	    	String resultMsg = modifyPoPurchase(poPurchase, employeeCode);
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
    private void setParameterToBean(Object bean, PoPurchaseOrderHead poPurchase) throws ValidationErrorException, Exception{
 	   
	 	       String invoiceTypeCode = (String)PropertyUtils.getProperty(bean, "invoiceTypeCode");
	    	   String taxType = (String)PropertyUtils.getProperty(bean, "taxType");
	    	   String taxRate = (String)PropertyUtils.getProperty(bean, "taxRate");
	    	   String countryCode = (String)PropertyUtils.getProperty(bean, "countryCode");
	    	   String currencyCode = (String)PropertyUtils.getProperty(bean, "currencyCode");       	
	    	   String exchangeRate = (String)PropertyUtils.getProperty(bean, "exchangeRate");
	    	   String quotationCode = (String)PropertyUtils.getProperty(bean, "quotationCode");
	    	   String contactPerson = (String)PropertyUtils.getProperty(bean, "contactPerson");
	    	   String paymentTermCode = (String)PropertyUtils.getProperty(bean, "paymentTermCode");
	    	   String defaultWarehouseCode = (String)PropertyUtils.getProperty(bean, "defaultWarehouseCode");
	    	   String isPartialShipment = (String)PropertyUtils.getProperty(bean, "isPartialShipment");
	    	   String paymentTermCode1 = (String)PropertyUtils.getProperty(bean, "paymentTermCode1");
	    	   String budgetYear = (String)PropertyUtils.getProperty(bean, "budgetYear");
	    	   String reserve1 = (String)PropertyUtils.getProperty(bean, "reserve1");
	    	   String scheduleReceiptDate = (String)PropertyUtils.getProperty(bean, "scheduleReceiptDate");
	    	   String reserve2 = (String)PropertyUtils.getProperty(bean, "reserve2");
	    	   String reserve3 = (String)PropertyUtils.getProperty(bean, "reserve3");
	    	   String tradeTermCode = (String)PropertyUtils.getProperty(bean, "tradeTermCode");
	    	   String packaging = (String)PropertyUtils.getProperty(bean, "packaging");
	    	   poPurchase.setInvoiceTypeCode(invoiceTypeCode);
	    	   poPurchase.setTaxRate(NumberUtils.getDouble(taxRate));
	    	   poPurchase.setTaxType(taxType);
	    	   poPurchase.setCountryCode(countryCode);
	    	   poPurchase.setCurrencyCode(currencyCode);
	    	   poPurchase.setExchangeRate(NumberUtils.getDouble(exchangeRate));
	    	   poPurchase.setQuotationCode(quotationCode);
	    	   poPurchase.setContactPerson(contactPerson);
	    	   poPurchase.setPaymentTermCode(paymentTermCode);
	    	   poPurchase.setDefaultWarehouseCode(defaultWarehouseCode);
	    	   poPurchase.setPaymentTermCode1(paymentTermCode1);
	    	   poPurchase.setBudgetYear(budgetYear);
	    	   poPurchase.setReserve1(reserve1);
	    	   poPurchase.setScheduleReceiptDate(DateUtils.parseDate(scheduleReceiptDate));
	    	   poPurchase.setReserve2(reserve2);
	    	   poPurchase.setReserve3(reserve3);
	    	   poPurchase.setTradeTermCode(tradeTermCode);
	    	   poPurchase.setPackaging(packaging);
	    	   
	    	   if("Y".equals(isPartialShipment)){
	    	       poPurchase.setIsPartialShipment("Y");
	    	   }else{
	    	       poPurchase.setIsPartialShipment("N");
	    	   }
	    	 
        }
    
    private PoPurchaseOrderHead getActualPoPurchase(Long headId) throws FormException, Exception{
        
        PoPurchaseOrderHead poPurchase = null;
        poPurchase = findById(headId);
	if(poPurchase  == null){
	     throw new NoSuchObjectException("查無採購單主鍵：" + headId + "的資料！");
	}
        return poPurchase;
    }
    /**
     * 暫存單號取實際單號並更新至PoPurchase主檔
     * 
     * @param PoPurchaseOrderHead
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String modifyPoPurchase(PoPurchaseOrderHead poPurchase, String loginUser)
	    throws ObtainSerialNoFailedException, FormException, Exception {
	
        if (AjaxUtils.isTmpOrderNo(poPurchase.getOrderNo())) {
	       String serialNo = buOrderTypeService.getOrderSerialNo(
	           poPurchase.getBrandCode(), poPurchase.getOrderTypeCode());
	       if (!serialNo.equals("unknow")) {
	    	   poPurchase.setOrderNo(serialNo);
	       } else {
		   throw new ObtainSerialNoFailedException("取得"
		       + poPurchase.getOrderTypeCode() + "單號失敗！");
	       }
        }
        poPurchase.setLastUpdatedBy(loginUser);
        poPurchase.setLastUpdateDate(new Date());
        System.out.println("----------insert Po start----------");
        poPurchaseOrderHeadDAO.update(poPurchase);
        System.out.println("----------insert Po End----------");
        return poPurchase.getOrderTypeCode() + "-" + poPurchase.getOrderNo() + "存檔成功！";
    }
    public static Object[] startProcess(PoPurchaseOrderHead form) throws ProcessFailedException{       
        
        try{           
	       String packageId = "Po_Purchase";         
	       String processId = "approval";           
	       String version = "20090803";
	       String sourceReferenceType = "PoPurchase (1)";
	       HashMap context = new HashMap();
	       context.put("brandCode", form.getBrandCode());
	       context.put("formId", form.getHeadId());
	       context.put("orderType", form.getOrderTypeCode());
	       context.put("orderNo", form.getOrderNo());
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
     * 
     * @param headId
     * @param lineLists
     * @throws Exception
     */
    public void executeImportPoLists(Long headId, List lineLists) throws Exception{
        
        try{
        	poPurchaseOrderLineService.deleteLineLists(headId);
	    	if(lineLists != null && lineLists.size() > 0){
	    	    PoPurchaseOrderHead poPurchaseOrderHeadTmp = new PoPurchaseOrderHead();
	    	    poPurchaseOrderHeadTmp.setHeadId(headId);
	    	    for(int i = 0; i < lineLists.size(); i++){
	    	    	PoPurchaseOrderLine  poPurchaseOrderLine = (PoPurchaseOrderLine)lineLists.get(i);
	    	    	poPurchaseOrderLine.setPoPurchaseOrderHead(poPurchaseOrderHeadTmp);
	    	    	poPurchaseOrderLine.setIndexNo(i+1L);
	    	    	poPurchaseOrderLineDAO.save(poPurchaseOrderLine);
	    	    }      	    
	    	}     	
        }catch (Exception ex) {
	        log.error("採購單明細匯入時發生錯誤，原因：" + ex.toString());
	        throw new Exception("採購單明細匯入時發生錯誤，原因：" + ex.getMessage());
	    }        
    }
    /**
     * 檢核採購單相關資料
     * 
     * @param poPurchaseOrderHead
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    public void checkPoPurchaseData(PoPurchaseOrderHead poPurchase, String programId, String identification) 
        throws ValidationErrorException {
        
	String message = null;
	List errorMsgs = new ArrayList(0);
	try{
	    if(doAllValidate(poPurchase)){
		errorMsgs.add("錯誤發生");
	    }
	    
	}catch(Exception ex){
	    message = "檢核採購單時發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, poPurchase.getCreatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
	
	if(errorMsgs.size() > 0){
	    throw new ValidationErrorException("採購單未通過檢核，請點選訊息提示按鈕檢視錯誤訊息！");
	}
    }
    public String getIdentification(Long headId) throws Exception{
	   
	   String id = null;
	   try{
	       PoPurchaseOrderHead poPurchase = findById(headId);
	       if(poPurchase != null){
		   id = MessageStatus.getIdentification(poPurchase.getBrandCode(), 
			   poPurchase.getOrderTypeCode(), poPurchase.getOrderNo());
	       }else{
	           throw new NoSuchDataException("採購單主檔查無主鍵：" + headId
			+ "的資料！");
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
    /**
     * 取單號後更新採購單主檔
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updatePoPurchaseWithActualOrderNO(Map parameterMap) throws FormException, Exception {
        
        HashMap resultMap = new HashMap();
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
    	       Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	       Object otherBean = parameterMap.get("vatBeanOther");
    	       Long headId = getPoPurchaseHeadId(formLinkBean); 
    	       String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
    	       //取得欲更新的bean
    	       PoPurchaseOrderHead poPurchase = getActualPoPurchase(headId);
    	       setParameterToBean(otherBean, poPurchase);
	       AjaxUtils.copyJSONBeantoPojoBean(formBindBean, poPurchase);
    	       String resultMsg = modifyPoPurchase(poPurchase, employeeCode);
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
    /**
     * 暫存單號取實際單號並更新至PoPurchaseOrderHead主檔
     * 
     * @param headId
     * @param loginUser
     * @return PoPurchaseOrderHead
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    public PoPurchaseOrderHead saveActualOrderNo(Long headId, String loginUser)
	    throws ObtainSerialNoFailedException, FormException, Exception {
	
	PoPurchaseOrderHead poPurchase = findById(headId);
	if(poPurchase == null){
	    throw new NoSuchObjectException("查無採購單主鍵：" + headId + "的資料！");
	}else if (AjaxUtils.isTmpOrderNo(poPurchase.getOrderNo())) {
	    String serialNo = buOrderTypeService.getOrderSerialNo(
		    poPurchase.getBrandCode(), poPurchase.getOrderTypeCode());
	    if (!serialNo.equals("unknow")) {
		poPurchase.setOrderNo(serialNo);
	    } else {
		throw new ObtainSerialNoFailedException("取得"
		    + poPurchase.getOrderTypeCode() + "單號失敗！");
	    }
        }
	poPurchase.setLastUpdatedBy(loginUser);
	poPurchase.setLastUpdateDate(new Date());
        poPurchaseOrderHeadDAO.update(poPurchase);

        return poPurchase;
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

	public void setBuAddressBookDAO(BuAddressBookDAO buAddressBookDAO) {
		this.buAddressBookDAO = buAddressBookDAO;
	}

	public void setPoPurchaseOrderLineService(PoPurchaseOrderLineService poPurchaseOrderLineService) {
		this.poPurchaseOrderLineService = poPurchaseOrderLineService;
	}

	public void setImItemService(ImItemService imItemService) {
		this.imItemService = imItemService;
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
	
	
}
