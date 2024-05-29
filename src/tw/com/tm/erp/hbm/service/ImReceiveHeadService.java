package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeApproval;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.bean.ImLetterOfCreditHead;
import tw.com.tm.erp.hbm.bean.ImLetterOfCreditLine;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImOnHandId;
import tw.com.tm.erp.hbm.bean.ImReceiveExpense;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveInvoice;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.ImTransation;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.bean.PoVerificationSheet;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeApprovalDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImLetterOfCreditHeadDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.ImTransationDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.PoVerificationSheetDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;

/**
 * 進貨單 Service
 * 
 * @author MyEclipse Persistence Tools
 * 
 */
public class ImReceiveHeadService {
	private static final Log log = LogFactory.getLog(ImReceiveHeadService.class);
	private ImReceiveHeadDAO imReceiveHeadDAO;
	private CmDeclarationHeadDAO cmDeclarationHeadDAO;
	private FiInvoiceHeadDAO fiInvoiceHeadDAO;
	private BuOrderTypeService buOrderTypeService;
	private BuBasicDataService buBasicDataService;
	private ImReceiveItemService imReceiveItemService;
	private ImOnHandDAO imOnHandDAO;
	private PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO;
	private PoVerificationSheetDAO poVerificationSheetDAO;
	private ImItemDAO imItemDAO;
	private BuCommonPhraseService buCommonPhraseService;
	// private FiBudgetHeadDAO fiBudgetHeadDAO;
	private BuAddressBookDAO buAddressBookDAO;
	private ImTransationDAO imTransationDAO;
	// private PoPurchaseOrderHeadService poPurchaseOrderHeadService;
	private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
	private ImItemService imItemService;
	private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
	private ImLetterOfCreditService imLetterOfCreditService;
	private ImLetterOfCreditHeadDAO imLetterOfCreditHeadDAO;
	private BuSupplierDAO buSupplierDAO;
	private BuOrderTypeApprovalDAO buOrderTypeApprovalDAO;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;  //進貨單寄信用  
	private BuEmployeeDAO buEmployeeDAO;  //進貨單寄信用 

	public static final String[] GRID_FIELD_NAMES = { "indexNo", "itemCode", "itemCName", "quantity", "foreignUnitPrice", "localUnitPrice",
			"foreignAmount", "localAmount", "expenseApportionmentAmount", "invoiceNo", "poOrderNo", "lineId", "status", "isLockRecord",
			"isDeleteRecord", "message" };

	public static final int[] GRID_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
			AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING };

	public static final String[] GRID_FIELD_DEFAULT_VALUES = { "0", "", "", "0", "0", "0", "0", "0", "0", "", "", "0", "",
			AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "" };

	/*
	 * public static final String[] GRID_FIELD_NAMES = { "indexNo", "itemCode",
	 * "itemCName", "unitPrice", "lastLocalUnitCost", "quantity",
	 * "foreignUnitCost", "foreignPurchaseAmount", "unitPriceAmount",
	 * "actualPurchaseQuantity", "lineId", "status", "isLockRecord",
	 * "isDeleteRecord", "message" };
	 */
	/**
	 * save and update
	 * 
	 * @param modifyObj
	 * @return
	 * @throws Exception
	 */
	public String create(ImReceiveHead modifyObj) throws Exception {
		log.info("ImReceiveHeadService.create");
		if (null != modifyObj) {
			setDefaultBarCodeCount(modifyObj);
			if (modifyObj.getHeadId() == null) {
				return save(modifyObj);
			} else {
				return update(modifyObj);
			}
		}
		return "";
	}

	/**
	 * save
	 * 
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String save(ImReceiveHead saveObj) throws Exception {
		log.info("ImReceiveHeadService.save");
		doAllValidate(saveObj);
		// 設定批號,為了要跟FI相同,所以使用相同的ORDER TYPE,建立批號
		if (ImReceiveHead.IM_RECEIVE_LOCAL.equalsIgnoreCase(saveObj.getOrderTypeCode())) { // 國內IRL
			if (!StringUtils.hasText(saveObj.getLotNo())) {
				log.info("1 ImReceiveHeadService.save FiInvoiceHead.FI_INVOICE_ORDER_TYPE=" + FiInvoiceHead.FI_INVOICE_ORDER_TYPE
						+ ",BrandCode=" + saveObj.getBrandCode());
				saveObj.setLotNo(buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), FiInvoiceHead.FI_INVOICE_ORDER_TYPE));
			}
		} else if (ImReceiveHead.IM_RECEIVE_FOREIGN.equalsIgnoreCase(saveObj.getOrderTypeCode())) {
			// 20081112 shan 因為 ON HAND 要用到
			if (!StringUtils.hasText(saveObj.getLotNo())) {
				log.info("2 ImReceiveHeadService.save FiInvoiceHead.FI_INVOICE_ORDER_TYPE=" + FiInvoiceHead.FI_INVOICE_ORDER_TYPE
						+ ",BrandCode=" + saveObj.getBrandCode());
				saveObj.setLotNo(buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), FiInvoiceHead.FI_INVOICE_ORDER_TYPE));
			}
		} else {
			log.info("3 ImReceiveHeadService.save SystemConfig.LOT_NO=" + SystemConfig.LOT_NO);
			saveObj.setLotNo(SystemConfig.LOT_NO);
		}

		createFiInvoiceRelation(saveObj);
		countHeadTotalAmount(saveObj);

		// 如果是退貨單就不會再產生單號,會依據PRL,PRF給的單號
		if (!(ImReceiveHead.RECEIVE_RETURN_ORDER_LOCAL.equalsIgnoreCase(saveObj.getOrderTypeCode()) || ImReceiveHead.RECEIVE_RETURN_ORDER_FOREIGN
				.equalsIgnoreCase(saveObj.getOrderTypeCode())))
			saveObj.setOrderNo(buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), saveObj.getOrderTypeCode()));

		saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imReceiveHeadDAO.save(saveObj);
		doVerification(saveObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String update(ImReceiveHead updateObj) throws Exception {
		log.info("ImReceiveHeadService.update");
		doAllValidate(updateObj);
		createFiInvoiceRelation(updateObj);
		countHeadTotalAmount(updateObj);
		updateObj.setLastUpdateDate(new Date());
		imReceiveHeadDAO.update(updateObj);
		doVerification(updateObj);

		// remove item null head id
		imReceiveHeadDAO.deleteNullField(ImReceiveInvoice.TABLE_NAME, ImReceiveInvoice.HEAD_ID);
		imReceiveHeadDAO.deleteNullField(ImReceiveExpense.TABLE_NAME, ImReceiveExpense.HEAD_ID);

		return MessageStatus.SUCCESS;
	}

	/**
	 * save tmp
	 * 
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String saveTmp(ImReceiveHead saveObj) throws FormException, Exception {
		log.info("ImReceiveHead.saveTmp");
		String tmpOrderNo = AjaxUtils.getTmpOrderNo();

		// 設定批號,為了要跟FI相同,所以使用相同的ORDER TYPE,建立批號
		if (ImReceiveHead.IM_RECEIVE_LOCAL.equalsIgnoreCase(saveObj.getOrderTypeCode())) { // 國內IRL
			if (!StringUtils.hasText(saveObj.getLotNo())) {
				saveObj.setLotNo(buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), FiInvoiceHead.FI_INVOICE_ORDER_TYPE));
			}
		} else if (ImReceiveHead.IM_RECEIVE_FOREIGN.equalsIgnoreCase(saveObj.getOrderTypeCode())) {
			// 20081112 shan 因為 ON HAND 要用到
			if (!StringUtils.hasText(saveObj.getLotNo())) {
				saveObj.setLotNo(buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), FiInvoiceHead.FI_INVOICE_ORDER_TYPE));
			}
		} else {
			saveObj.setLotNo(SystemConfig.LOT_NO);
		}

		saveObj.setOrderNo(tmpOrderNo);
		saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imReceiveHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * 複製前的初始化 , [應該不會再用到]
	 * 
	 * @param orig
	 * @param lastUpdatedBy
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 */
	public void copyInit(ImReceiveHead orig, String lastUpdatedBy) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, InstantiationException {
		log.info("ImReceiveHeadService.copyInit ");
		orig.setHeadId(null);
		orig.setOrderNo(null);
		orig.setStatus(OrderStatus.SAVE);
		orig.setLastUpdatedBy(lastUpdatedBy);

		List<ImReceiveExpense> newExpensesItems = new ArrayList();
		// List<ImReceiveExpense> expensesItems = orig.getImReceiveExpenses();
		copyListProperties(orig.getImReceiveExpenses(), newExpensesItems);
		/*
		 * for (ImReceiveExpense item : expensesItems) { ImReceiveExpense
		 * newItem = new ImReceiveExpense(); BeanUtils.copyProperties(newItem,
		 * item); newItem.setLineId(null); newItem.setImReceiveHead(null);
		 * newExpensesItems.add(newItem); }
		 */
		orig.setImReceiveExpenses(newExpensesItems);

		List<ImReceiveInvoice> newInvoiceItems = new ArrayList();
		// List<ImReceiveInvoice> invoiceItems = orig.getImReceiveInvoices();
		copyListProperties(orig.getImReceiveInvoices(), newInvoiceItems);
		/*
		 * for (ImReceiveInvoice item : invoiceItems) { ImReceiveInvoice newItem =
		 * new ImReceiveInvoice(); BeanUtils.copyProperties(newItem, item);
		 * newItem.setLineId(null); newItem.setImReceiveHead(null);
		 * newInvoiceItems.add(newItem); }
		 */
		orig.setImReceiveInvoices(newInvoiceItems);

		List<ImReceiveItem> newReceiveItems = new ArrayList();
		// List<ImReceiveItem> receiveItems = orig.getImReceiveItems();
		copyListProperties(orig.getImReceiveItems(), newReceiveItems);
		/*
		 * for (ImReceiveItem item : receiveItems) { ImReceiveItem newItem = new
		 * ImReceiveItem(); BeanUtils.copyProperties(newItem, item);
		 * newItem.setLineId(null); newItem.setImReceiveHead(null);
		 * newReceiveItems.add(newItem); }
		 */
		orig.setImReceiveItems(newReceiveItems);

	}

	/**
	 * 用來設定 送簽報關單的預設資料(從原始報關單中抓取 CmDeclarationHead)
	 * 
	 * @param target
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	public void setCmDeclarationData(ImReceiveHead target) throws IllegalArgumentException, SecurityException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
		log.info("ImReceiveHeadService.setCmDeclarationData DeclarationNo =" + target.getDeclarationNo());
		target.setDeclarationType(null);
		target.setDeclarationDate(null);
		target.setImportDate(null);
		target.setBondNo(null);
		target.setReferenceBillNo(null);
		target.setReleaseTime(null);
		target.setReleasePackage(null);
		target.setReleaseCondition(null);
		target.setStoragePlace(null);
		target.setPackageUnit(null);
		target.setWeight(null);
		target.setVesselSign(null);
		target.setVoyageNo(null);
		target.setShipCode(null);
		target.setExporter(null);
		target.setClearanceType(null);
		target.setDeclarationBoxNo(null);
		target.setInbondNo(null);
		target.setOutbondNo(null);
		// target.setImReceiveItems(null);

		if (StringUtils.hasText(target.getDeclarationNo())) {

			List cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", target.getDeclarationNo());
			if ((null != cmDeclarationHeads) && cmDeclarationHeads.size() > 0) {
				CmDeclarationHead cmDeclarationHead = (CmDeclarationHead) cmDeclarationHeads.get(0);

				target.setDeclarationType(cmDeclarationHead.getDeclType());
				target.setDeclarationDate(cmDeclarationHead.getDeclDate());
				target.setImportDate(cmDeclarationHead.getImportDate());
				target.setBondNo(cmDeclarationHead.getBondNo());
				target.setReferenceBillNo(cmDeclarationHead.getRefBillNo());
				target.setReleaseTime(cmDeclarationHead.getRlsTime());
				target.setReleasePackage(cmDeclarationHead.getRlsPkg());
				target.setReleaseCondition(cmDeclarationHead.getExtraCond());
				target.setStoragePlace(cmDeclarationHead.getStgPlace());
				target.setPackageUnit(cmDeclarationHead.getPkgUnit());
				target.setWeight(cmDeclarationHead.getGWgt());
				target.setVesselSign(cmDeclarationHead.getVesselSign());
				target.setVoyageNo(cmDeclarationHead.getVoyageNo());
				target.setShipCode(cmDeclarationHead.getShipCode());
				target.setExporter(cmDeclarationHead.getExporter());
				target.setClearanceType(cmDeclarationHead.getClearType());
				target.setDeclarationBoxNo(cmDeclarationHead.getBoxNo());
				target.setInbondNo(cmDeclarationHead.getInbondNo());
				target.setOutbondNo(cmDeclarationHead.getOutbondNo());

				List<ImReceiveItem> imReceiveItems = new ArrayList();
				List<CmDeclarationItem> cmItems = cmDeclarationHead.getCmDeclarationItems();

				for (CmDeclarationItem cmItem : cmItems) {
					ImReceiveItem imReceiveItem = new ImReceiveItem();
					imReceiveItem.setDeclarationItemNo(cmItem.getItemNo());
					imReceiveItem.setDeclarationItemCode(cmItem.getPrdtNo());
					imReceiveItem.setDeclarationItemName(cmItem.getDescrip());
					imReceiveItem.setDeclarationBrand(cmItem.getBrand());
					imReceiveItem.setDeclarationModel(cmItem.getModel());
					imReceiveItem.setDeclarationSpecification(cmItem.getSpec());
					imReceiveItem.setDeclarationNetWeight(cmItem.getNWght());
					imReceiveItem.setDeclarationQty(cmItem.getQty());
					imReceiveItem.setQuantity(cmItem.getQty());
					imReceiveItem.setDeclarationUnit(cmItem.getUnit());
					imReceiveItems.add(imReceiveItem);
				}
				target.setImReceiveItems(imReceiveItems);
				cmItems = null;
			}
			// cmDeclarationHead = null;
		}
	}

	/**
	 * 暫存 TMP ORDER 並計算所有的合計 Master , Detail , 費用分攤 , 存檔 [應該不會再用到ㄌ]
	 * 
	 * @param countObj
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws FormException
	 */
	public ImReceiveHead updateOrderTotalAmount(ImReceiveHead countObj) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, InstantiationException, FormException {
		log.info("ImReceiveHeadService.countHeadTotalAmount by headId=" + countObj.getHeadId());

		ImReceiveHead newPoHead = getMergeHeadObj(countObj);
		AjaxUtils.removeAJAXLine(newPoHead.getImReceiveItems());
		createFiInvoiceRelation(newPoHead);
		countHeadTotalAmount(newPoHead);
		imReceiveHeadDAO.merge(newPoHead);
		return newPoHead;
	}

	/**
	 * 計算所有的合計 Master , Detail , 費用分攤
	 * 
	 * @param countObj
	 */
	public void countHeadTotalAmount(ImReceiveHead countObj) {
		countHeadTotalAmount(countObj, true);
	}

	/**
	 * 計算所有的合計 Master , Detail , 費用分攤
	 * 
	 * @param countObj
	 */
	public void countHeadTotalAmount(ImReceiveHead countObj, boolean executeExpenseAmount) {
		log.info("ImReceiveHeadService.countHeadTotalAmount");
		Double exchangeRate = countObj.getExchangeRate();
		// 費用合計
		if (executeExpenseAmount)
			setExpenseAmount(countObj);
		List<ImReceiveItem> imReceiveItems = countObj.getImReceiveItems();
		countObj.setTotalForeignPurchaseAmount(new Double(0));
		countObj.setTotalLocalPurchaseAmount(new Double(0));
		if (null != countObj) {
			for (ImReceiveItem imReceiveItem : imReceiveItems) {
				if (null != imReceiveItem) {
					countItemTotalAmount(countObj, imReceiveItem);
					countObj.setTotalForeignPurchaseAmount(countObj.getTotalForeignPurchaseAmount() + imReceiveItem.getForeignAmount());
					// log.info("countObj.getTotalLocalPurchaseAmount() +
					// imReceiveItem.getLocalAmount()=" +
					// countObj.getTotalLocalPurchaseAmount() + "+" +
					// imReceiveItem.getLocalAmount());
					// 20090217 sha 調整依照外幣合計 計算台幣
					// countObj.setTotalLocalPurchaseAmount(countObj.getTotalLocalPurchaseAmount()
					// + imReceiveItem.getLocalAmount());
				}
			}
		}

		// 20080906 shan
		countItemExpenseApportionment(countObj);

		// 20090123 shan
		countObj.setTotalForeignPurchaseAmount(NumberUtils.round(countObj.getTotalForeignPurchaseAmount(), 2));
		// log.info("ImReceiveHeadService.countHeadTotalAmount
		// countObj.getTotalForeignPurchaseAmount()=" +
		// countObj.getTotalForeignPurchaseAmount());

		// 20090217 shan
		countObj.setTotalLocalPurchaseAmount(NumberUtils.round(countObj.getTotalForeignPurchaseAmount() * exchangeRate, 0));

		countObj.setTaxAmount(NumberUtils.round(countObj.getTaxRate() * countObj.getTotalLocalPurchaseAmount(), 0));

		// 20090301 shan
		if (null == countObj.getLcUseAmount() || countObj.getLcUseAmount() == 0) {
			countObj.setLcUseAmount(countObj.getTotalForeignPurchaseAmount());
		}
	}

	/**
	 * 計算Detail的合計
	 * 
	 * @param countObj
	 */
	public void countItemTotalAmount(ImReceiveHead headObj, ImReceiveItem itemObj) {
		log.info("ImReceiveHeadService.countItemTotalAmount");
		if ((null != headObj) && (null != itemObj))
			if ((null != itemObj.getForeignUnitPrice()) && (null != headObj.getExchangeRate())) {

				if (null == itemObj.getUnitPrice() || itemObj.getUnitPrice() == 0) {
					try {
						itemObj.setUnitPrice(getOriginalUnitPriceDouble(headObj.getBrandCode(), headObj.getOrderTypeCode(), itemObj
								.getItemCode()));
					} catch (FormException e) {
						e.printStackTrace();
					}
				}

				// 原幣單價 小數位數三位
				itemObj.setForeignUnitPrice(NumberUtils.round(itemObj.getForeignUnitPrice(), 3));
				itemObj.setLocalUnitPrice(NumberUtils.round(itemObj.getForeignUnitPrice() * headObj.getExchangeRate(), 4));
				// 20080908 shan change foreign quantity from quantity field
				itemObj.setLocalAmount(NumberUtils.round((itemObj.getForeignUnitPrice() * headObj.getExchangeRate())
						* (itemObj.getQuantity() + itemObj.getDiffQty()), 4));
				itemObj.setForeignAmount(NumberUtils.round(itemObj.getForeignUnitPrice() * (itemObj.getQuantity() + itemObj.getDiffQty()),
						2));
			}
	}

	/**
	 * 費用攤提 全部的費用 * (Item Amount / All Item Amount)
	 * 
	 * @param headObj
	 */
	public void countItemExpenseApportionment(ImReceiveHead headObj) {
		log.info("ImReceiveHeadService.countItemExpenseApportionment");
		if (null != headObj) {
			Double expenseAmount = headObj.getExpenseLocalAmount();
			Double surplusExpenseAmount = expenseAmount;
			Double allItemAmount = new Double(0);
			List<ImReceiveItem> items = headObj.getImReceiveItems();
			for (ImReceiveItem item : items) {
				allItemAmount = allItemAmount + item.getLocalAmount();
			}
			if (allItemAmount > 0) {
				for (ImReceiveItem item : items) {
					Double expenseApportionmentAmount = new Double(0);
					if (expenseAmount > 0d) {
						log.info("ImReceiveHeadService.countItemExpenseApportionment = " + expenseAmount + " * (" + item.getLocalAmount()
								+ " / " + allItemAmount + ")");
						expenseApportionmentAmount = NumberUtils.round(expenseAmount * (item.getLocalAmount() / allItemAmount), 0);
					}
					item.setExpenseApportionmentAmount(expenseApportionmentAmount);
					surplusExpenseAmount = surplusExpenseAmount - item.getExpenseApportionmentAmount();
				}

				// 20090301 shan
				if (surplusExpenseAmount != 0 && items.size() > 0) {
					ImReceiveItem firstItem = items.get(0);
					if (null != firstItem)
						firstItem.setExpenseApportionmentAmount(firstItem.getExpenseApportionmentAmount() + surplusExpenseAmount);
				}
			}
		}
	}

	/**
	 * 依據訂單明細的InvoiceNo設定 Invoice 合計
	 * 
	 * @param headObj
	 * @throws FormException
	 */
	private void createFiInvoiceRelation(ImReceiveHead headObj) throws FormException {
		log.info("ImReceiveHeadService.createFiInvoiceRelation");
		if (null != headObj) {
			log.info("ImReceiveHeadService.createFiInvoiceRelation clear ImReceiveInvoices");
			headObj.setImReceiveInvoices(new ArrayList());
			if (ImReceiveHead.IM_RECEIVE_FOREIGN.equalsIgnoreCase(headObj.getOrderTypeCode())) {
				List<ImReceiveItem> items = headObj.getImReceiveItems();
				if (null != items) {
					for (ImReceiveItem item : items) {
						boolean addNew = true;
						String invoiceNo = item.getInvoiceNo();
						List<ImReceiveInvoice> imReceiveInvoices = headObj.getImReceiveInvoices();
						if (StringUtils.hasText(invoiceNo)) {
							for (ImReceiveInvoice imReceiveInvoice : imReceiveInvoices) {
								if (null != imReceiveInvoice && imReceiveInvoice.getInvoiceNo().equals(invoiceNo)) {
									addNew = false;
								}
							}
						} else {
							addNew = false;
						}
						if (addNew) {
							log.info("ImReceiveHeadService.createFiInvoiceRelation addNew invoiceNo=" + invoiceNo);
							List fihs = fiInvoiceHeadDAO.findByProperty("FiInvoiceHead", "invoiceNo", invoiceNo);
							if ((null != fihs) && (fihs.size() > 0)) {
								// 建立invoice到 ImReceiveHead
								FiInvoiceHead fiInvoiceHead = (FiInvoiceHead) fihs.get(0);
								ImReceiveInvoice newImReceiveInvoice = new ImReceiveInvoice();
								newImReceiveInvoice.setInvoiceNo(invoiceNo);
								newImReceiveInvoice.setFiInvoiceHead(fiInvoiceHead);
								imReceiveInvoices.add(newImReceiveInvoice);
								headObj.setInvoiceLocalAmount(headObj.getInvoiceLocalAmount() + fiInvoiceHead.getTotalLocalInvoiceAmount());
								headObj.setInvoiceForeignAmount(headObj.getInvoiceForeignAmount()
										+ fiInvoiceHead.getTotalForeignInvoiceAmount());

								// 20090219 shan 將進貨單的資料寫到 FI_INVOICE
								fiInvoiceHead.setLotNo(headObj.getLotNo());
								fiInvoiceHead.setReceiveOrderTypeCode(headObj.getOrderTypeCode());
								fiInvoiceHead.setReceiveOrderNo(headObj.getOrderNo());
								fiInvoiceHeadDAO.merge(fiInvoiceHead);
							} else { // 查無發票資料
								throw new FormException(invoiceNo + " 查無發票資料!!");
							}
						}
					}
				}
			}
		}
		log.info("ImReceiveHeadService.createFiInvoiceRelation headObj.getImReceiveInvoices().size() ="
				+ headObj.getImReceiveInvoices().size());
	}

	/**
	 * remove invoice not in item [應該不會用到了]
	 * 
	 * @param headObj
	 */
	private void removeImReceiveInvoice(ImReceiveHead headObj) {
		log.info("ImReceiveHeadService.removeImReceiveInvoice");
		List<ImReceiveItem> items = headObj.getImReceiveItems();
		List<ImReceiveInvoice> invoices = headObj.getImReceiveInvoices();
		List<ImReceiveInvoice> removeObjs = new ArrayList();
		if ((null != invoices) && (invoices.size() > 0))
			for (ImReceiveInvoice invoice : invoices) {
				boolean remove = true;
				for (ImReceiveItem item : items) {
					log.info(" invoiceNo item.InvoiceNo -> " + item.getInvoiceNo() + " = invoice.InvoiceNo -> " + invoice.getInvoiceNo());
					// 移除不存在 ITEM 的 INVOICE
					if (null != item.getInvoiceNo() && null != invoice.getInvoiceNo()) {
						if (item.getInvoiceNo().equals(invoice.getInvoiceNo())) {
							remove = false;
							break;
						}
					}
				}
				if (remove)
					removeObjs.add(invoice);
			}
		for (ImReceiveInvoice invoice : removeObjs) {
			invoices.remove(invoice);
		}
	}

	/**
	 * 其他費用合計
	 * 
	 * @param headObj
	 */
	public void setExpenseAmount(ImReceiveHead headObj) {
		log.info("ImReceiveHeadService.setExpenseAmount");
		// expense 合計
		Double expenseForeignAmount = new Double(0);
		Double expenseLocalAmount = new Double(0);
		if (null != headObj) {
			for (ImReceiveExpense expense : headObj.getImReceiveExpenses()) {
				expenseLocalAmount = expenseLocalAmount + expense.getLocalAmount();
				expenseForeignAmount = expenseForeignAmount + expense.getForeignAmount();
			}
			headObj.setExpenseForeignAmount(expenseForeignAmount);
			headObj.setExpenseLocalAmount(expenseLocalAmount);
		}
	}

	/**
	 * 指定ITEM VIEW 資料
	 * 
	 * @param headObj
	 * @throws FormException
	 */
	public void setImReceiveItem(ImReceiveHead headObj) throws FormException {
		log.info("ImReceiveHeadService.setImReceiveItem");
		if (null != headObj) {
			List<ImReceiveItem> imReceiveItems = headObj.getImReceiveItems();
			if (null != imReceiveItems) {
				for (ImReceiveItem imReceiveItem : imReceiveItems) {
					String itemCode = imReceiveItem.getItemCode();
					if (StringUtils.hasText(itemCode)) {
						ImItem imItem = imItemDAO.findById(itemCode);
						if (null != imItem) {
							imReceiveItem.setItemCName(imItem.getItemCName());
						} else {
							imReceiveItem.setItemCName(MessageStatus.DATA_NOT_FOUND);
						}
					}
				}
			}
		}
	}

	/**
	 * 重新載入View 資料 for picker
	 * 
	 * @param headObj
	 * @throws FormException
	 */
	public void reloadViewData(ImReceiveHead headObj) throws FormException {
		log.info("ImReceiveHeadService.reloadViewData");
		// 只有在UPDATE的狀態下才去作
		// createFiInvoiceRelation(headObj);
		setExpenseAmount(headObj);
		setImReceiveItem(headObj);
		// add set cm declaracation data
	}

	/**
	 * 利用供應商代號設定 countryCode,currencyCode,exchangeRate
	 * 
	 * @param headObj
	 */
	public void setFormDataBySupplierCode(String organizationCode, ImReceiveHead headObj) {
		log.info("ImReceiveHeadService.setFormDataBySupplierCode");
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

				// 20090223 shan add
				countHeadTotalAmount(headObj);
			}
		}
	}

	/**
	 * 處理AJAX 供應商及相關資料
	 * 
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataBySupplierCode(Properties httpRequest) {
		log.info("ImReceiveHeadService.getFormDataBySupplierCode");
		List re = new ArrayList();
		String supplierCode = httpRequest.getProperty("supplierCode");
		String organizationCode = httpRequest.getProperty("organizationCode");
		String brandCode = httpRequest.getProperty("brandCode");
		String orderDate = httpRequest.getProperty("orderDate");
		// String orderTypeCode = httpRequest.getProperty("orderTypeCode");

		log.info("ImReceiveHeadService.getFormDataBySupplierCode supplierCode=" + supplierCode + ",organizationCode=" + organizationCode
				+ ",brandCode=" + brandCode + ",orderDate=" + orderDate);
		BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(brandCode, supplierCode);
		if (null != buSWAV) {
			Properties pro = new Properties();
			pro.setProperty("CountryCode", AjaxUtils.getPropertiesValue(buSWAV.getCountryCode(), ""));
			pro.setProperty("CurrencyCode", AjaxUtils.getPropertiesValue(buSWAV.getCurrencyCode(), ""));
			pro.setProperty("TaxType", AjaxUtils.getPropertiesValue(buSWAV.getTaxType(), ""));
			pro.setProperty("SupplierName", AjaxUtils.getPropertiesValue(buSWAV.getChineseName(), ""));
			pro.setProperty("TaxRate", AjaxUtils.getPropertiesValue(getTaxRate(buSWAV.getTaxType()), "0"));

			// 20090204 shan
			if (null != orderDate) {
				Date od = DateUtils.parseDate("yyyy/MM/dd", orderDate);
				pro.setProperty("ExchangeRate", getExchangeRateByCurrencyCode(organizationCode, buSWAV.getCurrencyCode(), od));
			} else {
				pro.setProperty("ExchangeRate", "1");
			}

			BuAddressBook buAddressBook = (BuAddressBook) buAddressBookDAO.findByPrimaryKey(BuAddressBook.class, buSWAV.getAddressBookId());
			if (null != buAddressBook) {

				BuSupplier buSupplier = buSupplierDAO.findSupplierByAddressBookIdAndBrandCode(buAddressBook.getAddressBookId(), brandCode);
				if (null != buSupplier) {
					pro.setProperty("TradeTeam", AjaxUtils.getPropertiesValue(buSupplier.getPriceTermCode(), ""));
					pro.setProperty("PaymentTermCode", AjaxUtils.getPropertiesValue(buSupplier.getPaymentTermCode(), ""));
				}

				pro.setProperty("ContactPerson", AjaxUtils.getPropertiesValue(buAddressBook.getContractPerson(), ""));
			} else {
				pro.setProperty("ContactPerson", "");
			}
			re.add(pro);
		} else {
			Properties pro = new Properties();
			pro.setProperty("CountryCode", "");
			pro.setProperty("CurrencyCode", "");
			pro.setProperty("TaxType", "");
			pro.setProperty("SupplierName", "查無供應商資料");
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
	 * 取得稅率
	 * 
	 * @param headObj
	 */
	private String getTaxRate(String taxType) {
		log.info("ImReceiveHeadService.getTaxRate");
		Double tmp = new Double(0);
		if (StringUtils.hasText(taxType)) {
			if (PoPurchaseOrderHead.PURCHASE_ORDER_TAX.equalsIgnoreCase(taxType)) {
				tmp = new Double(0.05);
			}
		}
		return tmp.toString();
	}

	/**
	 * 利用幣別指定匯率
	 * 
	 * @param headObj
	 */
	private String getExchangeRateByCurrencyCode(String organizationCode, String currencyCode, Date currenceDate) {
		log.info("ImReceiveHeadService.getExchangeRateByCurrencyCode");
		Double tmp = new Double(1);
		if (StringUtils.hasText(organizationCode) && (StringUtils.hasText(currencyCode))) {
			BuExchangeRate bxr = buBasicDataService.getLastExchangeRate(organizationCode, currencyCode, buCommonPhraseService
					.getBuCommonPhraseLineName("SystemConfig", "LocalCurrency"), currenceDate);
			if (null != bxr) {
				tmp = bxr.getExchangeRate();
			}
		}
		return tmp.toString();
	}

	/**
	 * 指定稅率
	 * 
	 * @param headObj
	 */
	public void setTaxRate(ImReceiveHead headObj) {
		log.info("ImReceiveHeadService.setTaxRate");
		if ((null != headObj) && (null != headObj.getOrderTypeCode()) && (null != headObj.getTaxType())) {
			if (headObj.getOrderTypeCode().equalsIgnoreCase(PoPurchaseOrderHead.PURCHASE_ORDER_LOCAL)
					&& headObj.getTaxType().equalsIgnoreCase(PoPurchaseOrderHead.PURCHASE_ORDER_TAX)) {
				headObj.setTaxRate(new Double(0.05d));
			} else {
				headObj.setTaxRate(new Double(0));
			}
		}
	}

	/**
	 * 利用幣別指定匯率
	 * 
	 * @param headObj
	 */
	public void setExchangeRateByCurrencyCode(String organizationCode, ImReceiveHead headObj) {
		log.info("ImReceiveHeadService.setExchangeRateByCurrencyCode");
		headObj.setExchangeRate(new Double(1));
		if ((null != organizationCode) && (null != headObj) && (null != headObj.getCurrencyCode())) {

			BuExchangeRate bxr = buBasicDataService.getLastExchangeRate(organizationCode, headObj.getCurrencyCode(), buCommonPhraseService
					.getBuCommonPhraseLineName("SystemConfig", "LocalCurrency"));
			if (null != bxr) {
				headObj.setExchangeRate(bxr.getExchangeRate());
			}
		}
	}

	/**
	 * AJAX利用幣別指定匯率
	 * 
	 * @param headObj
	 */
	public List<Properties> getAJAXExchangeRateByCurrencyCode(Properties httpRequest) {
		log.info("ImReceiveHeadService.getExchangeRateByCurrencyCode");
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
		//
		return re;
	}

	/**
	 * AJAX TaxRate Change
	 * 
	 * @param headObj(brandCode,orderTypeCode,itemCode)
	 * @throws FormException
	 */
	public List<Properties> getAJAXTaxRate(Properties httpRequest) throws FormException {
		log.info("ImReceiveHeadService.getAJAXTaxRate");
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
	 * AJAX Line Change
	 * 
	 * @param headObj(brandCode,orderTypeCode,itemCode)
	 * @throws FormException
	 */
	public List<Properties> getAJAXLineData(Properties httpRequest) throws FormException {
		log.info("ImReceiveHeadService.getAJAXLineData");
		List re = new ArrayList();
		String brandCode = httpRequest.getProperty("brandCode");
		String orderTypeCode = httpRequest.getProperty("orderTypeCode");
		String itemCode = httpRequest.getProperty("itemCode");
		if (StringUtils.hasText(brandCode) && StringUtils.hasText(orderTypeCode) && StringUtils.hasText(itemCode)) {
			Properties pro = new Properties();
			String itemCName = imItemService.getItemCName(brandCode, itemCode);
			if (!StringUtils.hasText(itemCName))
				itemCName = MessageStatus.DATA_NOT_FOUND;
			pro.setProperty("ItemCName", itemCName);

			re.add(pro);
		}
		return re;
	}

	/**
	 * 移除空白的Detail
	 * 
	 * @param headObj
	 * @return boolean 是否還有 Detail
	 */
	private boolean removeDetailItemCode(ImReceiveHead headObj) {
		log.info("ImReceiveHeadService.checkDetailItemCode");
		List<ImReceiveItem> items = headObj.getImReceiveItems();
		Iterator<ImReceiveItem> it = items.iterator();
		while (it.hasNext()) {
			ImReceiveItem line = it.next();
			// if (!StringUtils.hasText(line.getItemCode())) {return true; }

			if (!StringUtils.hasText(line.getItemCode())) {
				it.remove();
				items.remove(line);
			}
		}
		return items.isEmpty();
		// return false;
	}

	/**
	 * 移除空白的Detail
	 * 
	 * @param headObj
	 * @return boolean 是否還有 Detail
	 */

	private boolean removeReceiveExpenses(ImReceiveHead headObj) {
		log.info("ImReceiveHeadService.removeReceiveExpenses");
		List<ImReceiveExpense> items = headObj.getImReceiveExpenses();
		Iterator<ImReceiveExpense> it = items.iterator();
		while (it.hasNext()) {
			ImReceiveExpense line = it.next();
			// if (!StringUtils.hasText(line.getItemCode())) {return true; }
			log.info("ImReceiveHeadService.removeReceiveExpenses line.getSupplierCode()=" + line.getSupplierCode());
			if (!StringUtils.hasText(line.getSupplierCode())) {
				it.remove();
				items.remove(line);
			}
		}
		return items.isEmpty();
		// return false;
	}

	/**
	 * do master validate
	 * 
	 * @param headObj
	 * @throws FormException
	 */
	public void doValidate(ImReceiveHead headObj) throws FormException {
		log.info("ImReceiveHeadService.doValidate");

		if (null == headObj.getOrderDate())
			throw new FormException("請輸入歸帳日期");
		if (!StringUtils.hasText(headObj.getSupplierCode()))
			throw new FormException("請輸入廠商代號");

		if (ImReceiveHead.IM_RECEIVE_LOCAL.equalsIgnoreCase(headObj.getOrderTypeCode())) {
			// if (null == headObj.getGuiNo())
			// throw new FormException("請輸入廠商代號");
			// check po 單是否存在
			checkPoNo(headObj);
		} else if (ImReceiveHead.IM_RECEIVE_FOREIGN.equalsIgnoreCase(headObj.getOrderTypeCode())) {
			// 報單是否已經被使用 查詢IM_RECEIVE報單是否已經被使用掉
			List<ImReceiveItem> items = headObj.getImReceiveItems();
			for (ImReceiveItem item : items) {
				imReceiveItemService.doValidate(headObj, item);
			}
		}
	}

	/**
	 * 檢核
	 * 
	 * @param headObj
	 * @return
	 * @throws FormException,Exception
	 * @throws
	 * @throws Exception
	 */
	private void doAllValidate(ImReceiveHead headObj) throws FormException, Exception {
		log.info("ImReceiveHeadService.doAllValidate");
		boolean noLine = removeDetailItemCode(headObj);
		boolean noExpenses = removeReceiveExpenses(headObj);

		String dateType = "進貨日期";
		ValidateUtil.isAfterClose(headObj.getBrandCode(), headObj.getOrderTypeCode(), dateType, headObj.getOrderDate(),"PO");

		if (OrderStatus.SIGNING.equalsIgnoreCase(headObj.getStatus())) {
			if (noLine) {
				throw new FormException(MessageStatus.ERROR_NO_DETAIL);
			}
			doValidate(headObj);
		}
	}

	/**
	 * 國外 create On Hand
	 * 
	 * @param headObj
	 * @throws FormException
	 */
	private void doForeignOnHand(ImReceiveHead headObj, boolean reject) throws FormException {
		log.info("ImReceiveHeadService.doForeignOnHand HeadId=" + headObj.getHeadId());
		// String organizationCode =
		// UserUtils.getOrganizationCodeByLoginName(headObj.getLastUpdatedBy());
		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(headObj.getBrandCode());
		if (organizationCode == null) {
			throw new FormException("依據品牌代碼：" + headObj.getBrandCode() + "查無其組織代號！");
		}

		if (reject) {
			headObj.setOnHandStatus("N");
		} else {
			if ("Y".equalsIgnoreCase(headObj.getOnHandStatus())) {
				throw new FormException(headObj.getOrderNo() + "已經做過ON_HAND,請再確認資料後重新執行核銷動作");
			} else {
				headObj.setOnHandStatus("Y");
			}
		}

		List<ImReceiveItem> items = headObj.getImReceiveItems();
		for (ImReceiveItem item : items) {
			// List<FiInvoiceHead> fiInvoiceHead =
			// fiInvoiceHeadDAO.findByProperty("FiInvoiceHead", "invoiceNo",
			// item.getInvoiceNo());
			// if (null != fiInvoiceHead && fiInvoiceHead.size() > 0) {
			// 20080908 shan
			Double inUncommitQty = item.getQuantity() + item.getDiffQty();
			// Double inUncommitQty = item.getDeclarationQty();

			// 計算可以銷售的數量
			ImItem imItem = imItemDAO.findById(item.getItemCode());

			// 20081106 shan
			long salesRatio = 1;
			if ((null != imItem.getSalesRatio()) && (imItem.getSalesRatio() > 0))
				salesRatio = imItem.getSalesRatio();
			inUncommitQty = salesRatio * inUncommitQty;

			if (reject) {
				inUncommitQty = 0 - inUncommitQty;
			}
			//2010.01.20
			//modifyOnHandQty(organizationCode, item.getItemCode(), headObj.getLotNo(), headObj.getDefaultWarehouseCode(), inUncommitQty,
			//		headObj.getLastUpdatedBy());
			modifyOnHandQty(organizationCode, item.getItemCode(), headObj.getLotNo(), headObj.getDefaultWarehouseCode(), inUncommitQty,
				headObj.getLastUpdatedBy(), headObj.getBrandCode());

			/*
			 * } else { throw new FormException(item.getInvoiceNo() + "
			 * 發票編號有問題"); }
			 */
		}
	}

	/**
	 * 國內 create On Hand
	 * 
	 * @param headObj
	 * @throws FormException
	 */
	private void doLocalOnHand(ImReceiveHead headObj, boolean reject) throws FormException {
		log.info("ImReceiveHeadService.doLocalOnHand");
		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(headObj.getBrandCode());
		if (organizationCode == null) {
			throw new FormException("依據品牌代碼：" + headObj.getBrandCode() + "查無其組織代號！");
		}

		if (reject) {
			headObj.setOnHandStatus("N");
		} else {
			if ("Y".equalsIgnoreCase(headObj.getOnHandStatus())) {
				throw new FormException(headObj.getOrderNo() + "已經做過ON_HAND,請再確認資料後重新執行核銷動作");
			} else {
				headObj.setOnHandStatus("Y");
			}
		}

		List<ImReceiveItem> items = headObj.getImReceiveItems();
		for (ImReceiveItem item : items) {
			Double inUncommitQty = item.getQuantity() + item.getDiffQty();
			if (reject) {
				inUncommitQty = 0 - inUncommitQty;
			}
			// 計算可以銷售的數量
			ImItem imItem = imItemDAO.findById(item.getItemCode());

			// 20081106 shan
			long salesRatio = 1;
			if ((null != imItem.getSalesRatio()) && (imItem.getSalesRatio() > 0))
				salesRatio = imItem.getSalesRatio();
			inUncommitQty = salesRatio * inUncommitQty;

			//2010.01.20 
			//modifyOnHandQty(organizationCode, item.getItemCode(), headObj.getLotNo(), headObj.getDefaultWarehouseCode(), inUncommitQty,
			//		headObj.getLastUpdatedBy());
			modifyOnHandQty(organizationCode, item.getItemCode(), headObj.getLotNo(), headObj.getDefaultWarehouseCode(), inUncommitQty,
				headObj.getLastUpdatedBy(), headObj.getBrandCode());

			/*
			 * ImOnHandId id = new ImOnHandId();
			 * id.setOrganizationCode(organizationCode);
			 * id.setItemCode(item.getItemCode());
			 * id.setLotNo(headObj.getLotNo()); // 設定批號
			 * id.setWarehouseCode(headObj.getDefaultWarehouseCode()); ImOnHand
			 * imOnHand = imOnHandDAO.findById(id); if (null != imOnHand) { //
			 * update inUncommitQty = imOnHand.getInUncommitQty() +
			 * inUncommitQty; imOnHand.setInUncommitQty(inUncommitQty);
			 * imOnHand.setLastUpdateDate(new Date());
			 * imOnHand.setLastUpdatedBy(headObj.getLastUpdatedBy());
			 * imOnHandDAO.update(imOnHand); } else { // insert imOnHand = new
			 * ImOnHand(); imOnHand.setId(id);
			 * imOnHand.setInUncommitQty(inUncommitQty);
			 * imOnHand.setCreationDate(new Date());
			 * imOnHand.setCreatedBy(headObj.getLastUpdatedBy());
			 * imOnHand.setLastUpdateDate(new Date());
			 * imOnHand.setLastUpdatedBy(headObj.getLastUpdatedBy());
			 * imOnHandDAO.save(imOnHand); }
			 */
		}
	}

	/**
	 * 變更ON_HAND數量
	 * 
	 * @param organizationCode
	 * @param itemCode
	 * @param lotNo
	 * @param warehouseCode
	 * @param inUncommitQty
	 * @param lastUpdatedBy
	 */
	private void modifyOnHandQty(String organizationCode, String itemCode,      String lotNo, String warehouseCode, 
				     Double inUncommitQty,    String lastUpdatedBy, String brandCode) {
		log.info("ImReceiveHeadService.modifyOnHandQty organizationCode=" + organizationCode + ",itemCode=" + itemCode + ",lotNo=" + lotNo
				+ ",warehouseCode=" + warehouseCode + ",inUncommitQty=" + inUncommitQty + ",lastUpdatedBy=" + lastUpdatedBy);
		ImOnHandId id = new ImOnHandId();
		id.setOrganizationCode(organizationCode);
		id.setItemCode(itemCode);
		id.setLotNo(lotNo); // 設定批號
		id.setWarehouseCode(warehouseCode);
		//ImOnHand imOnHand = imOnHandDAO.findById(id);		// 2010.01.20
		ImOnHand imOnHand = imOnHandDAO.findByIdentification(organizationCode, brandCode, itemCode, warehouseCode, lotNo);

		if (null == inUncommitQty)
			inUncommitQty = 0D;

		if (null != imOnHand) { // update
			inUncommitQty = imOnHand.getInUncommitQty() + inUncommitQty;
			imOnHand.setInUncommitQty(inUncommitQty);
			imOnHand.setLastUpdateDate(new Date());
			imOnHand.setLastUpdatedBy(lastUpdatedBy);
			imOnHandDAO.update(imOnHand);
		} else { // insert
			imOnHand = new ImOnHand();
			imOnHand.setBrandCode(brandCode);	// 2010.01.20
			imOnHand.setId(id);
			imOnHand.setStockOnHandQty(0D);
			imOnHand.setOutUncommitQty(0D);
			imOnHand.setMoveUncommitQty(0D);
			imOnHand.setOtherUncommitQty(0D);
			imOnHand.setInUncommitQty(inUncommitQty);
			imOnHand.setCreationDate(new Date());
			imOnHand.setCreatedBy(lastUpdatedBy);
			imOnHand.setLastUpdateDate(new Date());
			imOnHand.setLastUpdatedBy(lastUpdatedBy);
			imOnHandDAO.save(imOnHand);
		}
	}

	/**
	 * 國外核銷,並寫入 po_verification_sheet
	 * 
	 * @param headObj
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	private void doForeignVerification(ImReceiveHead imReceiveHead) throws FormException, Exception {
		log.info("ImReceiveHeadService.doForeignVerification");
		if ("Y".equalsIgnoreCase(imReceiveHead.getVerificationStatus())) {
			throw new FormException(imReceiveHead.getOrderNo() + "已經核銷過了,請再確認資料後重新執行核銷動作");
		} else {
			imReceiveHead.setVerificationStatus("Y");
		}

		List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
		String imReceiveOrderNo = imReceiveHead.getOrderNo();
		String imReceiveOrderType = imReceiveHead.getOrderTypeCode();
		for (ImReceiveItem imReceiveItem : imReceiveItems) {
			String invoiceNo = imReceiveItem.getInvoiceNo();
			String itemCode = imReceiveItem.getItemCode();

			// 20080908 shan
			Double qty = imReceiveItem.getQuantity() + imReceiveItem.getDiffQty(); // 要核銷的數量
			// Double qty = imReceiveItem.getDeclarationQty(); // 要核銷的數量

			Long imReceiveLineId = imReceiveItem.getLineId();
			/*
			 * HashMap findFIObjs = new HashMap(); findFIObjs.put("invoiceNo",
			 * invoiceNo ); List<FiInvoiceHead> invoices =
			 * fiInvoiceHeadDAO.find(findFIObjs);
			 */

			HashMap findPOObjs = new HashMap();
			findPOObjs.put("invoiceNo", invoiceNo);
			findPOObjs.put("itemCode", itemCode);
			findPOObjs.put("brandCode", imReceiveHead.getBrandCode());
			// findPOObjs.put("status", OrderStatus.FINISH); // 簽核完成

			// 取得可以核銷的 PO單
			List<PoPurchaseOrderHead> poPurchaseOrderHeads = poPurchaseOrderHeadDAO.getVerificationPOList(findPOObjs);

			for (int index = 0; index < poPurchaseOrderHeads.size(); index++) {
				PoPurchaseOrderHead poPurchaseOrderHead = poPurchaseOrderHeads.get(index);
				String poOrderNo = poPurchaseOrderHead.getOrderNo();
				String poOrderType = poPurchaseOrderHead.getOrderTypeCode();
				// 核銷數量到 PoPurchaseOrderLine
				List<PoPurchaseOrderLine> poPurchaseOrderLines = poPurchaseOrderHead.getPoPurchaseOrderLines();
				for (PoPurchaseOrderLine poPurchaseOrderLine : poPurchaseOrderLines) {
					Long poOrderLineId = poPurchaseOrderLine.getLineId();
					if (itemCode.equals(poPurchaseOrderLine.getItemCode())) {
						if (index == (poPurchaseOrderHeads.size() - 1)) { // 處理最後一筆
							// update 核銷數量到 po detail ReceiptedQuantity
							poPurchaseOrderLine.setReceiptedQuantity(poPurchaseOrderLine.getReceiptedQuantity() + qty);
							poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);
							// 核銷 insert po verification sheet
							insertVerificationSheet(imReceiveLineId, imReceiveOrderNo, imReceiveOrderType, imReceiveHead.getLotNo(),
									invoiceNo, itemCode, poOrderLineId, poOrderNo, poOrderType, qty, poPurchaseOrderHead.getBrandCode());
							qty = new Double(0);
						} else {
							// Double remainder =
							// poPurchaseOrderLine.getQuantity() -
							// poPurchaseOrderLine.getReceiptedQuantity();
							Double remainder = poPurchaseOrderLine.getQuantity()
									- (poPurchaseOrderLine.getQuantity() - poPurchaseOrderLine.getActualPurchaseQuantity())
									- poPurchaseOrderLine.getReceiptedQuantity();
							if (qty > remainder) {
								// update 核銷數量到 po detail ReceiptedQuantity
								poPurchaseOrderLine.setReceiptedQuantity(poPurchaseOrderLine.getReceiptedQuantity() + remainder);
								poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);
								// 核銷 insert po verification sheet
								insertVerificationSheet(imReceiveLineId, imReceiveOrderNo, imReceiveOrderType, imReceiveHead.getLotNo(),
										invoiceNo, itemCode, poOrderLineId, poOrderNo, poOrderType, remainder, poPurchaseOrderHead
												.getBrandCode());
								qty = qty - remainder;
							} else if (qty < remainder) {
								// update 核銷數量到 po detail ReceiptedQuantity
								poPurchaseOrderLine.setReceiptedQuantity(poPurchaseOrderLine.getReceiptedQuantity() + qty);
								poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);
								// 核銷 insert po verification sheet
								insertVerificationSheet(imReceiveLineId, imReceiveOrderNo, imReceiveOrderType, imReceiveHead.getLotNo(),
										invoiceNo, itemCode, poOrderLineId, poOrderNo, poOrderType, qty, poPurchaseOrderHead.getBrandCode());
								qty = new Double(0);
								break;
							} else {
								// update 核銷數量到 po detail ReceiptedQuantity
								poPurchaseOrderLine.setReceiptedQuantity(poPurchaseOrderLine.getReceiptedQuantity() + qty);
								poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);
								// 核銷 insert po verification sheet
								insertVerificationSheet(imReceiveLineId, imReceiveOrderNo, imReceiveOrderType, imReceiveHead.getLotNo(),
										invoiceNo, itemCode, poOrderLineId, poOrderNo, poOrderType, qty, poPurchaseOrderHead.getBrandCode());
								qty = new Double(0);
								break;
							}
						}
					}
				}

				// 判斷是否要,PO單 結案
				// poPurchaseOrderHeadDAO.closeOrder(poPurchaseOrderHead, true);
				// poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);

			}
		}
	}

	/*
	 * private void minusBudget(String brandCode, String year, Double useBudget) {
	 * log.info("ImReceiveHeadService.minusBudget brandCode" + brandCode +
	 * ",year=" + year + ",useBudget=" + useBudget); HashMap findObjs = new
	 * HashMap(); findObjs.put("brandCode", brandCode);
	 * findObjs.put("budgetYear", year); findObjs.put("orderTypeCode", "PO");
	 * List<FiBudgetHead> budgets = fiBudgetHeadDAO.find(findObjs); if
	 * (budgets.size() > 0) { FiBudgetHead budget = budgets.get(0);
	 * budget.setTotalAppliedBudget(budget.getTotalAppliedBudget() + useBudget);
	 * fiBudgetHeadDAO.update(budget); } }
	 */

	/**
	 * 國內核銷
	 * 
	 * 修改採購單 新增核銷RECORD
	 * 
	 * @param imReceiveHead
	 * @throws FormException
	 */
	private void doLocalVerification(ImReceiveHead imReceiveHead) throws FormException, Exception {
		log.info("ImReceiveHeadService.doLocalVerification HeadId=" + imReceiveHead.getHeadId());
		if ("Y".equalsIgnoreCase(imReceiveHead.getVerificationStatus())) {
			throw new FormException(imReceiveHead.getOrderNo() + "已經核銷過了,請再確認資料後重新執行核銷動作");
		} else {
			imReceiveHead.setVerificationStatus("Y");
		}

		List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
		String guiNo = imReceiveHead.getGuiNo();
		String lotNo = imReceiveHead.getLotNo();
		String imReceiveOrderNo = imReceiveHead.getOrderNo();
		String imReceiveOrderType = imReceiveHead.getOrderTypeCode();
		for (ImReceiveItem imReceiveItem : imReceiveItems) {
			imReceiveItem.getInvoiceNo();
			String itemCode = imReceiveItem.getItemCode();
			Double qty = imReceiveItem.getQuantity() + imReceiveItem.getDiffQty(); // 要核銷的數量
			Long imReceiveLineId = imReceiveItem.getLineId();
			// 取得可以核銷的 PO單
			List<PoPurchaseOrderHead> poPurchaseOrderHeads = poPurchaseOrderHeadDAO.findAllNoCloseHead(OrderStatus.FINISH, imReceiveHead
					.getBrandCode(), imReceiveItem.getPoOrderNo(), PoPurchaseOrderHead.PURCHASE_ORDER_LOCAL);

			log.info("ImReceiveHeadService.doLocalVerification brandCode=" + imReceiveHead.getBrandCode() + ",PoOrderNo="
					+ imReceiveItem.getPoOrderNo() + ",OrderTypeCode=" + PoPurchaseOrderHead.PURCHASE_ORDER_LOCAL);

			for (int index = 0; index < poPurchaseOrderHeads.size(); index++) {
				PoPurchaseOrderHead poPurchaseOrderHead = poPurchaseOrderHeads.get(index);
				String poOrderNo = poPurchaseOrderHead.getOrderNo();
				String poOrderType = poPurchaseOrderHead.getOrderTypeCode();
				// 核銷數量到 PoPurchaseOrderLine
				List<PoPurchaseOrderLine> poPurchaseOrderLines = poPurchaseOrderHead.getPoPurchaseOrderLines();
				for (PoPurchaseOrderLine poPurchaseOrderLine : poPurchaseOrderLines) {
					Long poOrderLineId = poPurchaseOrderLine.getLineId();
					if (itemCode.equals(poPurchaseOrderLine.getItemCode())) {
						if (index == (poPurchaseOrderHeads.size() - 1)) { // 處理最後一筆
							// update 核銷數量到 po detail ReceiptedQuantity
							poPurchaseOrderLine.setReceiptedQuantity(poPurchaseOrderLine.getReceiptedQuantity() + qty);
							poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);
							// 核銷 insert po verification sheet
							insertVerificationSheet(imReceiveLineId, imReceiveOrderNo, imReceiveOrderType, lotNo, guiNo, itemCode,
									poOrderLineId, poOrderNo, poOrderType, qty, poPurchaseOrderHead.getBrandCode());
							qty = new Double(0);
						} else {
							// Double remainder =
							// poPurchaseOrderLine.getQuantity() -
							// poPurchaseOrderLine.getReceiptedQuantity();
							// 20081020 shan 為了結案的數量 , 所以改成 要先檢查 "實際應到貨數量" ,
							// 如果不夠該商品就不會參加核銷
							Double remainder = poPurchaseOrderLine.getQuantity()
									- (poPurchaseOrderLine.getQuantity() - poPurchaseOrderLine.getActualPurchaseQuantity())
									- poPurchaseOrderLine.getReceiptedQuantity();
							if (qty > remainder) {
								// update 核銷數量到 po detail ReceiptedQuantity
								poPurchaseOrderLine.setReceiptedQuantity(poPurchaseOrderLine.getReceiptedQuantity() + remainder);
								poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);
								// 核銷 insert po verification sheet
								insertVerificationSheet(imReceiveLineId, imReceiveOrderNo, imReceiveOrderType, lotNo, guiNo, itemCode,
										poOrderLineId, poOrderNo, poOrderType, remainder, poPurchaseOrderHead.getBrandCode());
								qty = qty - remainder;
							} else if (qty < remainder) {
								// update 核銷數量到 po detail ReceiptedQuantity
								poPurchaseOrderLine.setReceiptedQuantity(poPurchaseOrderLine.getReceiptedQuantity() + qty);
								poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);
								// 核銷 insert po verification sheet
								insertVerificationSheet(imReceiveLineId, imReceiveOrderNo, imReceiveOrderType, lotNo, guiNo, itemCode,
										poOrderLineId, poOrderNo, poOrderType, qty, poPurchaseOrderHead.getBrandCode());
								qty = new Double(0);
								break;
							} else {
								// update 核銷數量到 po detail ReceiptedQuantity
								poPurchaseOrderLine.setReceiptedQuantity(poPurchaseOrderLine.getReceiptedQuantity() + qty);
								poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);
								// 核銷 insert po verification sheet
								insertVerificationSheet(imReceiveLineId, imReceiveOrderNo, imReceiveOrderType, lotNo, guiNo, itemCode,
										poOrderLineId, poOrderNo, poOrderType, qty, poPurchaseOrderHead.getBrandCode());
								qty = new Double(0);
								break;
							}
						}
					}
				}

				// 判斷是否要,PO單 結案
				// poPurchaseOrderHeadDAO.closeOrder(poPurchaseOrderHead, true);
				// poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);

			}
		}
	}

	/**
	 * 寄送MAIL給採購人員
	 */
	public void sendMailToSuperintendent(ImReceiveHead imReceiveHead) {
		log.info("ImReceiveHeadService.sendMailToSuperintendent HeadId=" + imReceiveHead.getHeadId());
		// imReceiveHead.getim
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
						PoPurchaseOrderHead poPurchaseOrderHead = (PoPurchaseOrderHead) poPurchaseOrderHeadDAO.findByPrimaryKey(
								PoPurchaseOrderHead.class, orderHeadId);
						if (null != poPurchaseOrderHead) {
							try {
								sendMailToSuperintendent(brandCode, PoPurchaseOrderHead.PURCHASE_ORDER_LOCAL, poPurchaseOrderHead
										.getOrderNo());
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 核銷單 取消
	 * 
	 * @param imReceiveHead
	 */
	public void doVerificationReject(ImReceiveHead imReceiveHead) {
		// 取得入庫單中的所有的核銷單
		/*
		 * imReceiveHead private String imReceiveOrderType; private String
		 * imReceiveOrderNo; poVerificationSheetDAO.f
		 */
		// poVerificationSheetDAO.findByProperty(entityBean, fieldName,
		// fieldValue);*/
		// 更新PO ITEM
		// REMOVE 核銷單紀錄
	}

	/**
	 * insert verification sheet 產生核銷單
	 * 
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
	private void insertVerificationSheet(Long imReceiveLineId, String imReceiveOrderNo, String imReceiveOrderType, String inoviceLotNo,
			String invoiceNo, String itemCode, Long poOrderLineId, String poOrderNo, String poOrderType, Double quantity, String brandCode) {
		log.info("ImReceiveHeadService.insertVerificationSheet imReceiveLineId=" + imReceiveLineId + ",imReceiveOrderNo="
				+ imReceiveOrderNo + ",imReceiveOrderType=" + imReceiveOrderType + ",inoviceLotNo=" + inoviceLotNo + ",invoiceNo="
				+ invoiceNo + ",itemCode=" + itemCode + ",poOrderLineId=" + poOrderLineId + ",poOrderNo=" + poOrderNo + ",poOrderType="
				+ poOrderType + ",quantity=" + quantity);
		PoVerificationSheet verificationSheet = new PoVerificationSheet();
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
		poVerificationSheetDAO.save(verificationSheet);
	}

	/**
	 * 建立條碼
	 */
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
						ImItemCurrentPriceView itempricevies = imItemCurrentPriceViewDAO.findCurrentPriceByValue(item.getBrandCode(), item
								.getItemCode(), imItemPrice.getTypeCode());
						itempricevies.getItemCode();
						itempricevies.getBrandCode();
						itempricevies.getItemCName();
						// 商品類別 中類

					}
				}
			}

		}

	}

	/**
	 * close order
	 * 
	 * @param imReceiveHead
	 */
	public void closeOrder(ImReceiveHead imReceiveHead) {
		log.info("ImReceiveHeadService.closeOrder");
		imReceiveHead.setStatus(OrderStatus.CLOSE);
	}

	/**
	 * check po no 存在
	 * 
	 * @param imReceiveHead
	 * @throws FormException
	 */
	public void checkPoNo(ImReceiveHead imReceiveHead) throws FormException {
		log.info("ImReceiveHeadService.checkPoNo");
		if (StringUtils.hasText(imReceiveHead.getDefPoOrderNo())) {
			List pos = poPurchaseOrderHeadDAO.findByProperty("PoPurchaseOrderHead", "orderNo", imReceiveHead.getDefPoOrderNo());
			if ((null == pos) || (pos.size() == 0)) {
				imReceiveHead.setDefPoOrderNo("");
				throw new FormException("預設採購單號 查無資料 !!");
			}
		}
		List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();

		if ((imReceiveItems.size() == 0) || ((imReceiveItems.size() == 1) && (!StringUtils.hasText(imReceiveItems.get(0).getItemCode())))) {

		} else { // 檢查PO資料是否有問題
			for (ImReceiveItem imReceiveItem : imReceiveItems) {
				if (StringUtils.hasText(imReceiveItem.getPoOrderNo())) {
					HashMap findMap = new HashMap();
					findMap.put("orderTypeCode", PoPurchaseOrderHead.PURCHASE_ORDER_LOCAL);
					findMap.put("status", OrderStatus.FINISH);
					findMap.put("startOrderNo", imReceiveItem.getPoOrderNo());
					findMap.put("endOrderNo", imReceiveItem.getPoOrderNo());
					findMap.put("brandCode", imReceiveHead.getBrandCode());
					//findMap.put("closeOrder", PoPurchaseOrderHead.CLOSE_ORDER_N);
					List<PoPurchaseOrderHead> pos = poPurchaseOrderHeadDAO.find(findMap);
					// List<PoPurchaseOrderHead> pos =
					// poPurchaseOrderHeadDAO.findByProperty("PoPurchaseOrderHead",
					// "orderNo", imReceiveItem.getPoOrderNo());
					if ((null != pos) && (pos.size() == 0)) {
						throw new FormException("採購單號" + imReceiveItem.getPoOrderNo() + "查無資料");
					} else {
						boolean checkItemExist = false;
						PoPurchaseOrderHead po = pos.get(0);

						if (!OrderStatus.FINISH.equals(po.getStatus())) {
							throw new FormException("採購單號" + imReceiveItem.getPoOrderNo() + "尚未簽核完成");
						}

						List<PoPurchaseOrderLine> poLines = po.getPoPurchaseOrderLines();
						for (PoPurchaseOrderLine poLine : poLines) {
							if (poLine.getItemCode().equals(imReceiveItem.getItemCode())) {
								checkItemExist = true;
								break;
							}
						}

						if (!checkItemExist) {
							throw new FormException("採購單號" + imReceiveItem.getPoOrderNo() + "查無" + imReceiveItem.getItemCode() + "品號資料");
						}
					}
				} else {
					throw new FormException("請輸入採購單號");
				}
			}
		}
	}

	/**
	 * set Default BarCode Count
	 * 
	 * @param modifyObj
	 */
	private void setDefaultBarCodeCount(ImReceiveHead modifyObj) {
		List<ImReceiveItem> imReceiveItems = modifyObj.getImReceiveItems();
		for (ImReceiveItem imReceiveItem : imReceiveItems) {
			if (imReceiveItem.getBarcodeCount() == 0)
				imReceiveItem.setBarcodeCount(imReceiveItem.getQuantity());
		}
	}

	/**
	 * 建立 On Hand , Verification , 後續動作
	 * 
	 * @param modifyObj
	 * @throws Exception
	 */
	private void doVerification(ImReceiveHead modifyObj) throws Exception {
		log.info("ImReceiveHeadService.doVerification " + modifyObj.getWarehouseStatus());
		// 國內IRL 送簽之後做核銷 (on hand,核銷)
		if (ImReceiveHead.IM_RECEIVE_LOCAL.equalsIgnoreCase(modifyObj.getOrderTypeCode())) {
			if (OrderStatus.FINISH.equalsIgnoreCase(modifyObj.getWarehouseStatus()) && "N".equalsIgnoreCase(modifyObj.getOnHandStatus())) {
				doLocalVerification(modifyObj);
				doLocalOnHand(modifyObj, false);
				doAfterVerification(modifyObj);
			}
		} else if (ImReceiveHead.IM_RECEIVE_FOREIGN.equalsIgnoreCase(modifyObj.getOrderTypeCode())) {
			// 國外IRF 送簽之後做核銷(on hand,核銷)
			if (OrderStatus.FINISH.equalsIgnoreCase(modifyObj.getWarehouseStatus()) && "N".equalsIgnoreCase(modifyObj.getOnHandStatus())) {
				doForeignVerification(modifyObj);
				doForeignOnHand(modifyObj, false);
				doAfterVerification(modifyObj);
			}
		} else if (ImReceiveHead.RECEIVE_RETURN_ORDER_FOREIGN.equalsIgnoreCase(modifyObj.getOrderTypeCode())
				|| ImReceiveHead.RECEIVE_RETURN_ORDER_LOCAL.equalsIgnoreCase(modifyObj.getOrderTypeCode())) {
			if (OrderStatus.FINISH.equalsIgnoreCase(modifyObj.getWarehouseStatus()) && "N".equalsIgnoreCase(modifyObj.getOnHandStatus())) {
				doLocalOnHand(modifyObj, false);
			}
		}
	}

	/**
	 * 寄送MAIL給PO單負責人
	 * 
	 * @param modifyObj
	 */
	private void doAfterVerification(ImReceiveHead modifyObj) {
		log.info("ImReceiveHeadService.doAfterVerification");
		// 寄送MAIL給PO單負責人
		sendMailToSuperintendent(modifyObj);
	}

	/**
	 * 條碼匯出資料
	 * 
	 * @param headId
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws FormException
	 */
	public List getExportBarCode(Long headId, String brandCode, String orderTypeCode) throws IllegalAccessException,
			InvocationTargetException, FormException {
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

	/**
	 * 取得商品的原始售價
	 * 
	 * @param brandCode
	 * @param orderTypeCode
	 * @param itemCode
	 * @return
	 * @throws FormException
	 */
	public Double getOriginalUnitPriceDouble(String brandCode, String orderTypeCode, String itemCode) throws FormException {
		log.info("PoPurchaseOrderLineService.getOriginalUnitPriceDouble BrandCode=" + brandCode + ",OrderTypeCode=" + orderTypeCode);
		Double re = 0D;
		BuOrderTypeId buOrderTypeId = new BuOrderTypeId();
		buOrderTypeId.setBrandCode(brandCode);
		buOrderTypeId.setOrderTypeCode(orderTypeCode);
		BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
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

	/**
	 * search
	 * 
	 * @param findObjs
	 * @return
	 */
	public List<ImReceiveHead> find(HashMap findObjs) {
		log.info("ImReceiveHeadService.find");
		return imReceiveHeadDAO.find(findObjs);
	}

	public List<ImReceiveHead> findReceiveByProperty(String brandCode, String status) {
		log.info("ImReceiveHeadService.findReceiveByProperty");
		return imReceiveHeadDAO.findReceiveByProperty(brandCode, status);
	}

	public void deleteTmpOrder() {
		log.info("ImReceiveHeadService.deleteTmpOrder");
		imReceiveHeadDAO.removeTmpOrder();
	}

	/**
	 * 日結處理程序(Anber)
	 * 
	 * @param ImReceiveHead
	 * @param opUser
	 * @throws FormException
	 * @throws NoSuchDataException
	 */
	public void executeDailyBalance(ImReceiveHead imReceiveHead, String opUser) throws FormException {
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
				imOnHandDAO.updateInOnHand(organizationCode, imReceiveItem.getItemCode(), imReceiveHead.getDefaultWarehouseCode(),
						imReceiveHead.getLotNo(), imReceiveItem.getQuantity(), opUser);

				// 產生交易明細檔 出庫

				Double costAmount = new Double(0);
				costAmount = (imReceiveItem.getLocalAmount() == null ? 0D : imReceiveItem.getLocalAmount())
						+ (imReceiveItem.getExpenseApportionmentAmount() == null ? 0D : imReceiveItem.getExpenseApportionmentAmount());
				ImTransation transation = new ImTransation();
				transation.setBrandCode(brandCode);
				transation.setTransationDate(imReceiveHead.getOrderDate());
				transation.setOrderTypeCode(orderTypeCode);
				transation.setOrderNo(orderNo);
				transation.setLineId(imReceiveItem.getIndexNo());
				transation.setItemCode(imReceiveItem.getItemCode());
				transation.setWarehouseCode(imReceiveHead.getDefaultWarehouseCode());
				transation.setLotNo(imReceiveHead.getLotNo());
				transation.setQuantity(imReceiveItem.getQuantity());
				transation.setCostAmount(costAmount);
				transation.setCreatedBy(opUser);
				transation.setCreationDate(new Date());
				transation.setLastUpdatedBy(opUser);
				transation.setLastUpdatedDate(new Date());
				imTransationDAO.save(transation);
			}
			imReceiveHeadDAO.update(imReceiveHead);

		} else {
			throw new ValidationErrorException("查無" + identification + "的調撥單明細資料！");
		}

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
		log.info("ImReceiveHeadService.getAJAXPageData");

		// 要顯示的HEAD_ID
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		int iSPage = AjaxUtils.getStartPage(httpRequest);
		int iPSize = AjaxUtils.getPageSize(httpRequest);

		List<Properties> re = new ArrayList();
		List<ImReceiveItem> imReceiveItems = null;
		List<Properties> gridDatas = new ArrayList();
		imReceiveItems = imReceiveItemService.findPageLine(headId, iSPage, iPSize);
		if (null != imReceiveItems && imReceiveItems.size() > 0) {
			log.info("ImReceiveHeadService.getAJAXPageData AjaxUtils.imReceiveItems=" + imReceiveItems.size());

			// 取得第一筆的INDEX
			Long firstIndex = imReceiveItems.get(0).getIndexNo();
			// 取得最後一筆 INDEX
			Long maxIndex = imReceiveItemService.findPageLineMaxIndex(Long.valueOf(headId));

			re.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, imReceiveItems, gridDatas,
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
	 */
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws NumberFormatException, FormException, Exception {
		log.info("ImReceiveHeadService.updateAJAXPageData");
		String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		Double exchangeRate = NumberUtils.getDouble(httpRequest.getProperty("exchangeRate"));
		String errorMsg = null;
		log.info("ImReceiveHeadService.updateAJAXPageData gridLineFirstIndex=" + gridLineFirstIndex + ",gridRowCount=" + gridRowCount
				+ ",headId=" + headId + ",exchangeRate=" + exchangeRate + ",gridData=" + gridData);

		// 取得LINE MAX INDEX
		// PoPurchaseOrderHead head = (PoPurchaseOrderHead)
		// poPurchaseOrderHeadDAO.findByPrimaryKey(PoPurchaseOrderHead.class,
		// headId);
		// CREATE TMP HEAD
		ImReceiveHead head = imReceiveHeadDAO.findById(headId);
		head.setExchangeRate(exchangeRate);
		// 將STRING資料轉成List Properties record data
		List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
				ImReceiveHeadService.GRID_FIELD_NAMES);
		// Get INDEX NO
		int indexNo = imReceiveItemService.findPageLineMaxIndex(headId).intValue();
		if (null != upRecords && null != head) {
			for (Properties upRecord : upRecords) {
				// 先載入HEAD_ID OR LINE DATA
				Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
				log.info("head Id=" + head.getHeadId() + " ,line Id=" + lineId);
				ImReceiveItem imReceiveItem = imReceiveItemService.findLine(head.getHeadId(), lineId);
				String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);
				log.info("head Id=" + head.getHeadId() + " ,line Id=" + lineId + ",itemCode=" + itemCode);
				// 如果沒有ITEM CODE 就不會新增或修改
				if (StringUtils.hasText(itemCode)) {
					if (null != imReceiveItem) {
						// UPDATE Properties
						// setLine(imReceiveItem,upRecord);
						AjaxUtils.setPojoProperties(imReceiveItem, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
						// LINE 計算
						countItemTotalAmount(head, imReceiveItem);
						imReceiveHeadDAO.update(imReceiveItem);
					} else {
						// INSERT Properties
						indexNo++;
						imReceiveItem = new ImReceiveItem();
						// setLine(imReceiveItem,upRecord);
						AjaxUtils.setPojoProperties(imReceiveItem, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
						imReceiveItem.setIndexNo(Long.valueOf(indexNo));
						imReceiveItem.setImReceiveHead(head);
						// LINE 計算
						countItemTotalAmount(head, imReceiveItem);
						imReceiveHeadDAO.save(imReceiveItem);
					}
					// return new page
					// getAJAXPageData(httpRequest);
				}
			}
		} else {
			errorMsg = "沒有進貨單單頭資料";
		}

		return AjaxUtils.getResponseMsg(errorMsg);
	}

	/**
	 * AJAX save and update ImReceive
	 * 
	 * @param modifyObj
	 * @return
	 * @throws Exception
	 */
	public String createAJAX(ImReceiveHead modifyObj) throws FormException, Exception {
		log.info("ImReceiveHeadService.createAJAX");
		if (null != modifyObj) {
			if (null == modifyObj.getHeadId() || modifyObj.getHeadId().equals(0L)) {
				// List<ImReceiveItem> line = modifyObj.getImReceiveItems();
				// System.out.println(" size=" + line.size());
				modifyObj.setImReceiveItems(null);
				modifyObj.setImReceiveExpenses(null);
				modifyObj.setImReceiveInvoices(null);
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
	public String updateAJAXHead(ImReceiveHead updateObj) throws FormException, Exception {
		log.info("ImReceiveHeadService.updateAJAXHead");

		//T4NG
		String code = updateObj.getBrandCode() + updateObj.getOrderTypeCode() + "001";
		BuOrderTypeApproval buOrderTypeApproval = buOrderTypeApprovalDAO.findById(code);
//		if(buOrderTypeApproval == null){
		if("T4NG".equals(updateObj.getBrandCode()) && (!OrderStatus.SAVE.equals(updateObj.getStatus()) || !OrderStatus.VOID.equals(updateObj.getStatus()) ) ){
			updateObj.setWarehouseInDate(updateObj.getOrderDate());
			updateObj.setWarehouseStatus(OrderStatus.FINISH);
		}
		
		if (AjaxUtils.isTmpOrderNo(updateObj.getOrderNo())) {
			updateObj.setOrderNo(buOrderTypeService.getOrderSerialNo(updateObj.getBrandCode(), updateObj.getOrderTypeCode()));
		}

		ImReceiveHead newPoHead = getMergeHeadObj(updateObj);
		newPoHead.setLastUpdateDate(new Date());
		// remove line record
		AjaxUtils.removeAJAXLine(newPoHead.getImReceiveItems());

		createFiInvoiceRelation(newPoHead);
		doAllValidate(newPoHead);

		countHeadTotalAmount(newPoHead);

		// 20090301 shan start
		modifyReceiveToLC(newPoHead, newPoHead.getReserve1(), newPoHead.getLcNo(), newPoHead.getLcUseAmount());
		newPoHead.setReserve1(newPoHead.getLcNo());

		modifyReceiveToLC(newPoHead, newPoHead.getReserve2(), newPoHead.getLcNo1(), newPoHead.getLcUseAmount1());
		newPoHead.setReserve2(newPoHead.getLcNo());
		// end

		// 將Quantity數量給BarcodeCount
		Iterator<ImReceiveItem> it = newPoHead.getImReceiveItems().iterator();
		while (it.hasNext()) {
			ImReceiveItem item = it.next();
			item.setBarcodeCount(item.getQuantity());
		}
		//T4NG
//		if(buOrderTypeApproval == null){
		if("T4NG".equals(updateObj.getBrandCode()) && (!OrderStatus.SAVE.equals(updateObj.getStatus()) || !OrderStatus.VOID.equals(updateObj.getStatus()) ) ){
			newPoHead.setStatus(OrderStatus.FINISH);
		}
		imReceiveHeadDAO.merge(newPoHead);
		// imReceiveHeadDAO.update(newPoHead);
		doVerification(newPoHead);
		return MessageStatus.SUCCESS;

	}

	/**
	 * 取得 新舊 HEAD MERGE OBJ
	 * 
	 * @param updateObj
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 */
	private ImReceiveHead getMergeHeadObj(ImReceiveHead updateObj) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, InstantiationException {
		log.info("ImReceiveHeadService.getMergeHeadObj");
		// load line
		ImReceiveHead newPoHead = new ImReceiveHead();

		ImReceiveHead oldPoHead = findById(updateObj.getHeadId());
		BeanUtils.copyProperties(newPoHead, updateObj);

		List defauleNames = new ArrayList();
		List defauleValues = new ArrayList();
		defauleNames.add("imReceiveHead");
		defauleValues.add(newPoHead);

		// create New ImReceiveExpenses
		List<ImReceiveItem> newImReceiveItems = new ArrayList();
		// List<ImReceiveItem> imReceiveItems = oldPoHead.getImReceiveItems();
		copyListProperties(oldPoHead.getImReceiveItems(), newImReceiveItems, defauleNames, defauleValues);
		/*
		 * if (null != imReceiveItems && imReceiveItems.size() > 0) { for
		 * (ImReceiveItem imReceiveItem : imReceiveItems) { ImReceiveItem
		 * tmpImReceiveItem = new ImReceiveItem();
		 * BeanUtils.copyProperties(tmpImReceiveItem, imReceiveItem);
		 * tmpImReceiveItem.setImReceiveHead(newPoHead);
		 * newImReceiveItems.add(tmpImReceiveItem); } }
		 */
		newPoHead.setImReceiveItems(newImReceiveItems);

		// create New ImReceiveExpenses
		List<ImReceiveExpense> newImReceiveExpenses = new ArrayList();
		log.info("ImReceiveHeadService.getMergeHeadObj updateObj.getImReceiveExpenses().size()=" + updateObj.getImReceiveExpenses().size());

		// List<ImReceiveExpense> imReceiveExpenses =
		// updateObj.getImReceiveExpenses();
		copyListProperties(updateObj.getImReceiveExpenses(), newImReceiveExpenses, defauleNames, defauleValues);

		for (int index = 0; index < newImReceiveExpenses.size(); index++) {
			Long indexNo = Long.valueOf(index + 1);
			newImReceiveExpenses.get(index).setIndexNo(indexNo);
		}

		/*
		 * if (null != imReceiveExpenses && imReceiveExpenses.size() > 0) { for
		 * (ImReceiveExpense imReceiveExpense : imReceiveExpenses) {
		 * ImReceiveExpense tmpImReceiveExpense = new ImReceiveExpense();
		 * BeanUtils.copyProperties(tmpImReceiveExpense, imReceiveExpense);
		 * tmpImReceiveExpense.setImReceiveHead(newPoHead);
		 * newImReceiveExpenses.add(tmpImReceiveExpense); } }
		 */
		log.info("ImReceiveHeadService.getMergeHeadObj newImReceiveExpenses.size()=" + newImReceiveExpenses.size());
		newPoHead.setImReceiveExpenses(newImReceiveExpenses);

		// List<ImReceiveInvoice> newInvoiceItems = new ArrayList();
		// List<ImReceiveInvoice> invoiceItems =
		// updateObj.getImReceiveInvoices();
		// this.copyListProperties(updateObj.getImReceiveInvoices(),
		// newInvoiceItems);
		/*
		 * if (null != invoiceItems && invoiceItems.size() > 0) { for
		 * (ImReceiveInvoice invoice : invoiceItems) { ImReceiveInvoice
		 * tmpImReceiveInvoice = new ImReceiveInvoice();
		 * BeanUtils.copyProperties(tmpImReceiveInvoice, invoice);
		 * tmpImReceiveInvoice.setImReceiveHead(newPoHead);
		 * newInvoiceItems.add(tmpImReceiveInvoice); } }
		 */
		// newPoHead.setImReceiveInvoices(newInvoiceItems);
		return newPoHead;
	}

	/**
	 * AJAX 合計計算
	 * 
	 * @param headObj
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	public List<Properties> updateAJAXHeadTotalAmount(Properties httpRequest) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		log.info("ImReceiveHeadService.updateAJAXHeadTotalAmount ");
		List re = new ArrayList();
		String headId = httpRequest.getProperty("headId");
		String taxType = httpRequest.getProperty("taxType");
		String taxRate = httpRequest.getProperty("taxRate");
		String exchangeRate = httpRequest.getProperty("exchangeRate");
		String expenseForeignAmount = httpRequest.getProperty("expenseForeignAmount"); // 費用合計(foreign)
		String expenseLocalAmount = httpRequest.getProperty("expenseLocalAmount"); // 費用合計(foreign)

		log.info("ImReceiveHeadService.getAJAXHeadTotalAmount headId=" + headId + ",taxType=" + taxType + ",exchangeRate=" + exchangeRate
				+ ",expenseForeignAmount=" + expenseForeignAmount + ",expenseLocalAmount=" + expenseLocalAmount);
		if (StringUtils.hasText(headId)) {
			ImReceiveHead countObj = findById(NumberUtils.getLong(headId));
			// 移除LINE
			AjaxUtils.removeAJAXLine(countObj.getImReceiveItems());
			// 加上要計算的欄位 getTaxType , getExchangeRate
			Double der = NumberUtils.getDouble(exchangeRate);
			Double defa = NumberUtils.getDouble(expenseForeignAmount);
			Double dela = NumberUtils.getDouble(expenseLocalAmount);
			Double dtr = NumberUtils.getDouble(taxRate);
			countObj.setTaxRate(dtr);
			countObj.setTaxType(taxType);
			countObj.setExchangeRate(der);
			countObj.setExpenseForeignAmount(defa);
			countObj.setExpenseLocalAmount(dela);
			countHeadTotalAmount(countObj, false);
			imReceiveHeadDAO.merge(countObj);
			Double totalLocalPurchaseAmount = countObj.getTotalLocalPurchaseAmount();
			Double totalForeignPurchaseAmount = countObj.getTotalForeignPurchaseAmount();
			Properties pro = new Properties();
			pro.setProperty("TotalLocalPurchaseAmount", NumberUtils.roundToStr(totalLocalPurchaseAmount, 0));
			log.info("ImReceiveHeadService.updateAJAXHeadTotalAmount totalForeignPurchaseAmount=" + totalForeignPurchaseAmount
					+ ", NumberUtils.roundToStr(totalForeignPurchaseAmount, 2)=" + NumberUtils.roundToStr(totalForeignPurchaseAmount, 2));
			pro.setProperty("TotalForeignPurchaseAmount", NumberUtils.roundToStr(totalForeignPurchaseAmount, 2));
			pro.setProperty("ExpenseForeignAmount", NumberUtils.roundToStr(countObj.getExpenseForeignAmount(), 4));
			pro.setProperty("ExpenseLocalAmount", NumberUtils.roundToStr(countObj.getExpenseLocalAmount(), 2));
			pro.setProperty("TaxAmount", NumberUtils.roundToStr(countObj.getTaxAmount(), 0));
			re.add(pro);
		}
		return re;
	}

	/**
	 * 複製 LIST 物件
	 * 
	 * @param origs
	 * @param dests
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private void copyListProperties(List origs, List dests) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		copyListProperties(origs, dests, null, null);
	}

	/**
	 * 複製 LIST 物件
	 * 
	 * @param origs
	 * @param dests
	 * @param defauleNames
	 * @param defauleValues
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private void copyListProperties(List origs, List dests, List defauleNames, List defauleValues) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		log.info("ImReceiveHeadService.copyListProperties");
		for (Object orig : origs) {
			Object dest = ConstructorUtils.invokeConstructor(orig.getClass(), null);
			BeanUtils.copyProperties(dest, orig);

			// add default

			if (null != defauleNames && null != defauleValues) {
				for (int index = 0; index < defauleNames.size(); index++) {
					String defaultName = (String) defauleNames.get(index);
					Object defaultValue = defauleValues.get(index);
					log.info("ImReceiveHeadService.copyListProperties bef defaultName=" + defaultName + ",defaultValue=" + defaultValue);
					BeanUtils.setProperty(dest, defaultName, defaultValue);
				}
			}

			dests.add(dest);
		}
	}

	/**
	 * send mail to po superintendent
	 * 
	 * @param brandCode
	 * @param orderType
	 * @param orderNo
	 * @throws FormException
	 */
	private void sendMailToSuperintendent(String brandCode, String orderType, String orderNo) throws FormException {
		log
				.info("ImReceiveHeadService.sendMailToSuperintendent orderNo=" + orderNo + ",orderType=" + orderType + ",brandCode="
						+ brandCode);
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
			BuEmployeeWithAddressView buEmployeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode,
					superintendentCode);
			log.info("PoPurchaseOrderHeadService.sendMailToSuperintendent brandCode=" + brandCode + ", superintendentCode="
					+ superintendentCode);
			if (null != buEmployeeWithAddressView) {
				String eMail = buEmployeeWithAddressView.getEMail();
				log.info("PoPurchaseOrderHeadService.sendMailToSuperintendent eMail=" + eMail);
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

	/**
	 * 將進貨單的資料寫到LC單
	 * 
	 * @param modifyObj
	 * @throws Exception
	 */
	private void modifyReceiveToLC(ImReceiveHead imReceiveHead, String oldLcNo, String lcNo, Double lcUseAmount) throws Exception {
		log.info("ImReceiveHeadService.updateReceiveToLC oldLcNo=" + oldLcNo + ",lcNo=" + lcNo + ",lcUseAmount=" + lcUseAmount);
		if (null != imReceiveHead) {
			String brandCode = imReceiveHead.getBrandCode();
			String receiveOrderNo = imReceiveHead.getOrderNo();
			String loginUser = imReceiveHead.getLastUpdatedBy();
			String orderTypeCode = imReceiveHead.getOrderTypeCode();

			// 用來更新狀態用的
			HashMap statusConditionMap = new HashMap();
			statusConditionMap.put("beforeChangeStatus", OrderStatus.SAVE);

			// remove old lcno
			if (oldLcNo != null) {
				HashMap removeConditionMap = new HashMap();
				removeConditionMap.put("lcNo_Start", oldLcNo == null ? "" : oldLcNo);
				removeConditionMap.put("lcNo_End", oldLcNo == null ? "" : oldLcNo);
				removeConditionMap.put("brandCode", brandCode);

				List<ImLetterOfCreditHead> removeImLetterOfCreditHeads = imLetterOfCreditHeadDAO.findLetterOfCreditList(removeConditionMap);
				System.out.println(removeImLetterOfCreditHeads.size());
				if (null != removeImLetterOfCreditHeads && removeImLetterOfCreditHeads.size() > 0) {
					ImLetterOfCreditHead imLetterOfCreditHead = removeImLetterOfCreditHeads.get(0);
					if (OrderStatus.CLOSE.equalsIgnoreCase(imLetterOfCreditHead.getStatus())) {
						throw new FormException("LC已經結案 LC NO. " + oldLcNo);
					} else {
						List<ImLetterOfCreditLine> imLetterOfCreditLines = imLetterOfCreditHead.getImLetterOfCreditLines();
						for (int index = 0; index < imLetterOfCreditLines.size(); index++) {
							ImLetterOfCreditLine imLetterOfCreditLine = imLetterOfCreditLines.get(index);
							if (imLetterOfCreditLine != null) {
								imLetterOfCreditLine.setBrandCode(brandCode);
								imLetterOfCreditLine.setOrderTypeCode(orderTypeCode);
								if (receiveOrderNo.equals(imLetterOfCreditLine.getReceiveNo())) {
									imLetterOfCreditLines.remove(index);
									break;
								}
							} else {
								imLetterOfCreditLines.remove(index);
							}
						}
						imLetterOfCreditService.countAllTotalAmount(imLetterOfCreditHead, statusConditionMap, loginUser);
						imLetterOfCreditHeadDAO.update(imLetterOfCreditHead);
					}
				}
			}
			if (StringUtils.hasText(lcNo)) {
				// add new lcno
				HashMap addConditionMap = new HashMap();
				addConditionMap.put("lcNo_Start", lcNo);
				addConditionMap.put("lcNo_End", lcNo);
				addConditionMap.put("brandCode", brandCode);
				List<ImLetterOfCreditHead> addImLetterOfCreditHeads = imLetterOfCreditHeadDAO.findLetterOfCreditList(addConditionMap);
				if (null != addImLetterOfCreditHeads && addImLetterOfCreditHeads.size() > 0) {
					ImLetterOfCreditHead imLetterOfCreditHead = addImLetterOfCreditHeads.get(0);
					if (OrderStatus.CLOSE.equalsIgnoreCase(imLetterOfCreditHead.getStatus())) {
						throw new FormException("LC已經結案 LC NO. " + oldLcNo);
					} else {
						List<ImLetterOfCreditLine> imLetterOfCreditLines = imLetterOfCreditHead.getImLetterOfCreditLines();
						if (null == imLetterOfCreditLines) {
							imLetterOfCreditLines = new ArrayList();
							imLetterOfCreditHead.setImLetterOfCreditLines(imLetterOfCreditLines);
						}
						ImLetterOfCreditLine imLetterOfCreditLine = new ImLetterOfCreditLine();
						imLetterOfCreditLine.setReceiveNo(receiveOrderNo);
						imLetterOfCreditLine.setReceiveAmount(lcUseAmount);
						imLetterOfCreditLines.add(imLetterOfCreditLine);
						imLetterOfCreditLine.setBrandCode(brandCode);
						imLetterOfCreditLine.setOrderTypeCode(orderTypeCode);
						imLetterOfCreditService.countAllTotalAmount(imLetterOfCreditHead, statusConditionMap, loginUser);
						imLetterOfCreditHeadDAO.update(imLetterOfCreditHead);
					}
				}
			}
		}
	}

	public String updateUncomfirmOrder(ImReceiveHead modifyObj) throws FormException, Exception {
		try {
			log.info("ImReceiveHeadService.updateUncomfirmOrder");
			if (null != modifyObj) {
				// 先確認沒有關帳
				ValidateUtil.isAfterClose(modifyObj.getBrandCode(),modifyObj.getOrderTypeCode(), "轉出日期",modifyObj.getOrderDate(),"PO");

				String organizationCode = UserUtils.getOrganizationCodeByBrandCode(modifyObj.getBrandCode());
				if (organizationCode == null) {
					throw new FormException("依據品牌代碼：" + modifyObj.getBrandCode() + "查無其組織代號！");
				}

				// 取得OnHand與ImReceiveItem相關的資料
				List imOnHandsAndImReceiveItems = imOnHandDAO.getOnHandByImReceiveHead(String.valueOf(modifyObj.getHeadId()),
						organizationCode);

				if (imOnHandsAndImReceiveItems != null) {
					StringBuffer lineIds = new StringBuffer();

					if (OrderStatus.CLOSE.equals(modifyObj.getStatus())) {// close走這
						// 以lotno得到imTransation的資料
						String fieldNames[] = {"lotNo","brandCode"};
						Object fieldValue[] = {modifyObj.getLotNo(),modifyObj.getBrandCode()};
						List imTransations = imTransationDAO.findByProperty("ImTransation", fieldNames, fieldValue, "itemCode");
						// 如果不是空值，以及imTransation的資料數與OnHand資料相符合，才可以反確認，因為不符合表示有其它因素，動過此張進貨單
						if (imTransations != null && (imTransations.size() == imOnHandsAndImReceiveItems.size())) {
							Iterator it = imOnHandsAndImReceiveItems.iterator();
							int index = 0;
							// 依序扣掉onhand stock的數量
							while (it.hasNext()) {
								Object[] imOnHandAndImReceiveItem = (Object[]) it.next();
								ImOnHand imOnHand = (ImOnHand) imOnHandAndImReceiveItem[0];
								ImReceiveItem imReceiveItem = (ImReceiveItem) imOnHandAndImReceiveItem[1];
								ImTransation imTransation = (ImTransation) imTransations.get(index++);

								Double stockQty = imOnHand.getStockOnHandQty() - imTransation.getQuantity();
								imOnHand.setStockOnHandQty(stockQty);
								imOnHand.setLastUpdateDate(new Date());
								imOnHand.setLastUpdatedBy(modifyObj.getLastUpdatedBy());
								imOnHandDAO.update(imOnHand);

								lineIds.append("'").append(imReceiveItem.getLineId()).append("',");
							}
							// 依lotno刪掉imTransation的資料
							imTransationDAO.deleteByLotNo(modifyObj.getLotNo());
						} else {
							throw new FormException("進貨單:" + modifyObj.getOrderNo() + "不可反確認");
						}
					} else if ((OrderStatus.FINISH.equals(modifyObj.getStatus()) || OrderStatus.SIGNING.equals(modifyObj.getStatus()))
							&& OrderStatus.FINISH.equalsIgnoreCase(modifyObj.getWarehouseStatus())) {// FINISH走這
						Iterator it = imOnHandsAndImReceiveItems.iterator();
						// 依序扣掉onhand in的數量
						while (it.hasNext()) {
							Object[] imOnHandAndImReceiveItem = (Object[]) it.next();
							ImOnHand imOnHand = (ImOnHand) imOnHandAndImReceiveItem[0];
							ImReceiveItem imReceiveItem = (ImReceiveItem) imOnHandAndImReceiveItem[1];
							Double inUncommitQty = imReceiveItem.getQuantity() + imReceiveItem.getDiffQty();
							ImItem imItem = imItemDAO.findById(imReceiveItem.getItemCode());
							long salesRatio = 1;
							if ((null != imItem.getSalesRatio()) && (imItem.getSalesRatio() > 0))
								salesRatio = imItem.getSalesRatio();
							inUncommitQty = salesRatio * inUncommitQty;
							inUncommitQty = imOnHand.getInUncommitQty() - inUncommitQty;
							imOnHand.setInUncommitQty(inUncommitQty);
							imOnHand.setLastUpdateDate(new Date());
							imOnHand.setLastUpdatedBy(modifyObj.getLastUpdatedBy());
							imOnHandDAO.update(imOnHand);

							lineIds.append("'").append(imReceiveItem.getLineId()).append("',");
						}
					}
					// 刪掉poVerificationSheet
					lineIds.deleteCharAt(lineIds.length() - 1);
					poVerificationSheetDAO.deleteByimReceiveLineId(lineIds.toString());
					// 訂單改成暫存
					modifyObj.setVerificationStatus("N");
					modifyObj.setOnHandStatus("N");
					modifyObj.setWarehouseInDate(null);
					modifyObj.setStatus(OrderStatus.SAVE);
					modifyObj.setWarehouseStatus("SAVE");
					imReceiveHeadDAO.merge(modifyObj);
				} else {
					throw new FormException("此進貨單:" + modifyObj.getOrderNo() + "不可反確認");
				}
				return MessageStatus.SUCCESS;
			} else {
				throw new FormException("查無表單主檔資料");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new FormException(e.getMessage());
		}
	}

	/**
	 * find by pk
	 * 
	 * @param headId
	 * @return
	 */
	public ImReceiveHead findById(Long headId) {
		log.info("ImReceiveHeadService.findById");
		return (ImReceiveHead) imReceiveHeadDAO.findByPrimaryKey(ImReceiveHead.class, headId);
	}

	public void setImReceiveHeadDAO(ImReceiveHeadDAO imReceiveHeadDAO) {
		this.imReceiveHeadDAO = imReceiveHeadDAO;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setCmDeclarationHeadDAO(CmDeclarationHeadDAO cmDeclarationHeadDAO) {
		this.cmDeclarationHeadDAO = cmDeclarationHeadDAO;
	}

	public void setFiInvoiceHeadDAO(FiInvoiceHeadDAO fiInvoiceHeadDAO) {
		this.fiInvoiceHeadDAO = fiInvoiceHeadDAO;
	}

	/*
	 * public void setBuSupplierWithAddressViewDAO( BuSupplierWithAddressViewDAO
	 * buSupplierWithAddressViewDAO) { this.buSupplierWithAddressViewDAO =
	 * buSupplierWithAddressViewDAO; }
	 */

	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}

	public void setImReceiveItemService(ImReceiveItemService imReceiveItemService) {
		this.imReceiveItemService = imReceiveItemService;
	}

	/*
	 * public void setImReceiveExpenseService(ImReceiveExpenseService
	 * imReceiveExpenseService) { this.imReceiveExpenseService =
	 * imReceiveExpenseService; }
	 * 
	 * public void setImReceiveInvoiceDAO(ImReceiveInvoiceDAO
	 * imReceiveInvoiceDAO) { this.imReceiveInvoiceDAO = imReceiveInvoiceDAO; }
	 */
	public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
		this.imOnHandDAO = imOnHandDAO;
	}

	public void setPoPurchaseOrderHeadDAO(PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO) {
		this.poPurchaseOrderHeadDAO = poPurchaseOrderHeadDAO;
	}

	public void setPoVerificationSheetDAO(PoVerificationSheetDAO poVerificationSheetDAO) {
		this.poVerificationSheetDAO = poVerificationSheetDAO;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	/*
	 * public void setFiBudgetHeadDAO(FiBudgetHeadDAO fiBudgetHeadDAO) {
	 * this.fiBudgetHeadDAO = fiBudgetHeadDAO; }
	 */

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setBuAddressBookDAO(BuAddressBookDAO buAddressBookDAO) {
		this.buAddressBookDAO = buAddressBookDAO;
	}

	public void setImItemCurrentPriceViewDAO(ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
		this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
	}

	public void setImItemService(ImItemService imItemService) {
		this.imItemService = imItemService;
	}

	public void setImTransationDAO(ImTransationDAO imTransationDAO) {
		this.imTransationDAO = imTransationDAO;
	}

	public void setBuEmployeeWithAddressViewDAO(BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
		this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
	}

	public void setBuSupplierDAO(BuSupplierDAO buSupplierDAO) {
		this.buSupplierDAO = buSupplierDAO;
	}

	public void setImLetterOfCreditService(ImLetterOfCreditService imLetterOfCreditService) {
		this.imLetterOfCreditService = imLetterOfCreditService;
	}

	public void setImLetterOfCreditHeadDAO(ImLetterOfCreditHeadDAO imLetterOfCreditHeadDAO) {
		this.imLetterOfCreditHeadDAO = imLetterOfCreditHeadDAO;
	}

	public BuOrderTypeApprovalDAO getBuOrderTypeApprovalDAO() {
		return buOrderTypeApprovalDAO;
	}

	public void setBuOrderTypeApprovalDAO(BuOrderTypeApprovalDAO buOrderTypeApprovalDAO) {
		this.buOrderTypeApprovalDAO = buOrderTypeApprovalDAO;
	}  
	
	 public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
	    	this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	 }
	 
	 public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
			this.buEmployeeDAO = buEmployeeDAO;
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
