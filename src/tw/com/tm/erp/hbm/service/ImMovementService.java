package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.CmMovementLine;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImItemOnHandView;
import tw.com.tm.erp.hbm.bean.ImMonthlyBalanceHead;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImTransation;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCountryDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImMonthlyBalanceHeadDAO;
import tw.com.tm.erp.hbm.dao.ImMonthlyBalanceLineDAO;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.hbm.dao.ImMovementItemDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.ImTransationDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.IslandExportDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.ArraysUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;
import tw.com.tm.erp.hbm.dao.CmDeclarationItemDAO;
import tw.com.tm.erp.hbm.dao.ImSysLogDAO;
import tw.com.tm.erp.hbm.bean.ImSysLog;
//for 儲位用
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.action.ImStorageAction;

public class ImMovementService {
	public static final String PROGRAM_ID = "IM_MOVEMENT";
	public static final String SCAN_CODE = "_SCAN_CODE";
	private static final Log log = LogFactory.getLog(ImMovementService.class);

	private static ImMovementHeadDAO imMovementHeadDAO;
	private ImMovementItemDAO imMovementItemDAO;
	private ImItemDAO imItemDAO;
	private ImTransationDAO imTransationDAO;
	private ImItemOnHandViewDAO imItemOnHandViewDAO;
	private ImWarehouseDAO imWarehouseDAO;
	private BuBrandDAO buBrandDAO;
	private ImReceiveHeadDAO imReceiveHeadDAO;
	private BuOrderTypeDAO buOrderTypeDAO;
	private BuOrderTypeService buOrderTypeService;
	private ImOnHandDAO imOnHandDAO;
	private ImItemCategoryDAO imItemCategoryDAO;
	private ImMonthlyBalanceLineDAO imMonthlyBalanceLineDAO;
	private ImMonthlyBalanceHeadDAO imMonthlyBalanceHeadDAO;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
	private CmDeclarationOnHandDAO cmDeclarationOnHandDAO;
	private ImItemPriceDAO imItemPriceDAO;
	private CmDeclarationHeadDAO cmDeclarationHeadDAO;
	private BuCountryDAO buCountryDAO;
	private BaseDAO baseDAO;

	private CmDeclarationItemDAO cmDeclarationItemDAO;
	//for 儲位用
	private ImStorageAction				 imStorageAction;
	private ImStorageService			 imStorageService;

	//Steve
	private ImSysLogDAO imSysLogDAO;
	//Steve

	// private static String[] threePointArray = new String[]{"IMD", "IML"
	// "IMS"};
	// private static String[] signingArray = new String[]{"IML", "IMR", "IMW"};

	private SiProgramLogAction siProgramLogAction;

	private static String[] threePointArray = new String[0];
	private static String[] signingArray = new String[0];
	private static String defaultLotNo = "000000000000";
	public final static String FORM_SUBMIT = "SUBMIT"; // FORM 的動作
	public final static String FORM_SUBMIT_BG = "SUBMIT_BG"; // FORM 的動作
	public final static String FORM_SAVE = "SAVE"; //
	public final static String FORM_VOID = "VOID"; //
	public final static String MOVE_THREE_TYPE = "THREE";
	public final static String MOVE_SIGN_TYPE = "Y";
	// public final static String ON_HAND_MODE_ACCEPT="ACCEPT";
	// public final static String ON_HAND_MODE_NORMAL="NORMAL";
	public final static String TAX_MODE_FREE = "F";
	public final static String NCV_TYPE = "61";
	public final static String MOVEMENT_TYPE_GIFT = "GIFT";
	public final static String MOVEMENT_TYPE_TEST = "TEST";
	public final static String MOVEMENT_TYPE_DISPLAY = "DISPLAY";
	public final static String MOVEMENT_TYPE_NORMAL = "NORMAL";
	public final static String MOVEMENT_TYPE_ACCEPT = "ACCEPT";
	public final static String MOVEMENT_IMPORT = "ImMovementImportDataT2";

	public static final String[] GRID_FIELD_NAMES = { "indexNo", "boxNo", "itemCode", "reserve5", "itemName",
			"deliveryWarehouseCode", "lotNo", "stockOnHandQuantity", "originalDeliveryQuantity", "deliveryQuantity","whComfirmedQuantity",
			"arrivalWarehouseCode", "arrivalQuantity", "originalDeclarationNo", "reserve4", "originalDeclarationSeq",
			"originalDeclarationDate", "lineId", "isLockRecord", "isDeleteRecord", "returnMessage" };

	public static final int[] GRID_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_LONG,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
			AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING };

	public static final String[] GRID_FIELD_DEFAULT_VALUES = { "", "", "", "", "", "", "", "0.0", "0.0", "0.0", "0.0", "", "0.0",
			"", "", "", "", "", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "" };

	public static final String[] GRID_SEARCH_FIELD_NAMES = { "orderTypeCode", "orderNo", "deliveryDate",
			"deliveryWarehouseCode", "arrivalDate", "arrivalWarehouseCode", "originalOrderTypeCode", "originalOrderNo",
			"itemCount", "status", "remark1", "headId" };

	public static final int[] GRID_SEARCH_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "", "", "", "", "", "", "", "", "0", "", "0" };

	public void setImMovementHeadDAO(ImMovementHeadDAO imMovementHeadDAO) {
		this.imMovementHeadDAO = imMovementHeadDAO;
	}

	public void setImMovementItemDAO(ImMovementItemDAO imMovementItemDAO) {
		this.imMovementItemDAO = imMovementItemDAO;
	}

	public void setCmDeclarationOnHandDAO(CmDeclarationOnHandDAO cmDeclarationOnHandDAO) {
		this.cmDeclarationOnHandDAO = cmDeclarationOnHandDAO;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}

	public void setImItemOnHandViewDAO(ImItemOnHandViewDAO imItemOnHandViewDAO) {
		this.imItemOnHandViewDAO = imItemOnHandViewDAO;
	}

	public void setImReceiveHeadDAO(ImReceiveHeadDAO imReceiveHeadDAO) {
		this.imReceiveHeadDAO = imReceiveHeadDAO;
	}

	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
		this.imWarehouseDAO = imWarehouseDAO;
	}

	public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
		this.imOnHandDAO = imOnHandDAO;
	}

	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}

	public void setImTransationDAO(ImTransationDAO imTransationDAO) {
		this.imTransationDAO = imTransationDAO;
	}

	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}

	public void setBuOrderTypeDAO(BuOrderTypeDAO buOrderTypeDAO) {
		this.buOrderTypeDAO = buOrderTypeDAO;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setCmDeclarationHeadDAO(CmDeclarationHeadDAO cmDeclarationHeadDAO) {
		this.cmDeclarationHeadDAO = cmDeclarationHeadDAO;
	}

	public void setImItemPriceDAO(ImItemPriceDAO imItemPriceDAO) {
		this.imItemPriceDAO = imItemPriceDAO;
	}

	public void setImMonthlyBalanceLineDAO(ImMonthlyBalanceLineDAO imMonthlyBalanceLineDAO) {
		this.imMonthlyBalanceLineDAO = imMonthlyBalanceLineDAO;
	}

	public void setImMonthlyBalanceHeadDAO(ImMonthlyBalanceHeadDAO imMonthlyBalanceHeadDAO) {
		this.imMonthlyBalanceHeadDAO = imMonthlyBalanceHeadDAO;
	}

	public List<ImMovementHead> find(HashMap findObjs) {
		return imMovementHeadDAO.find(findObjs);
	}

	public List<ImMovementHead> findUnlimited(HashMap findObjs) {
		return imMovementHeadDAO.find(findObjs);
	}

	public HashMap findPageLine(HashMap findObjs, int startPage, int pageSize, String searchType) {
		return imMovementHeadDAO.findPageLine(findObjs, startPage, pageSize, searchType);
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setImItemCurrentPriceViewDAO(ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
		this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
	}

	public void setBuCountryDAO(BuCountryDAO buCountryDAO) {
		this.buCountryDAO = buCountryDAO;
	}

	public void setImStorageAction(ImStorageAction imStorageAction) {
		this.imStorageAction = imStorageAction;
	}
	
	public void setImStorageService(ImStorageService imStorageService) {
		this.imStorageService = imStorageService;
	}

	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
	

	 public void setCmDeclarationItemDAO(CmDeclarationItemDAO cmDeclarationItemDAO) {
			this.cmDeclarationItemDAO = cmDeclarationItemDAO;
	 }
	

	//Steve
    public void setImSysLogDAO(ImSysLogDAO imSysLogDAO) {
		this.imSysLogDAO = imSysLogDAO;
	}
    //Steve
	

	public String create(ImMovementHead modifyObj, String formAction) throws Exception {
		try {
			if (null != modifyObj) {

				if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
					log.info("checking ImMovement ");
					checkImMovement(modifyObj);
					log.info("check ImMovement...OK ");

					if (modifyObj.getHeadId() == null)
						save(modifyObj);
					else
						update(modifyObj);

					if (!OrderStatus.SIGNING.equalsIgnoreCase(modifyObj.getStatus())) {
						log.info("modify ImMovement...OK ");
						updateMoveUncommitQuantity(modifyObj, false);
						log.info("update On Hand...OK ");
					}
				} else {
					if (modifyObj.getHeadId() == null)
						save(modifyObj);
					else
						update(modifyObj);

				}
				return modifyObj.getOrderTypeCode() + "-" + modifyObj.getOrderNo() + "存檔成功";
			} else {
				throw new FormException("");
			}
		} catch (FormException fe) {
			log.error("調撥單存檔時發生錯誤，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("調撥單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("調撥單存檔時發生錯誤，原因：" + ex.getMessage());
		}

	}

	/**
	 * save
	 *
	 * @param saveObj
	 * @return
	 * @throws ObtainSerialNoFailedException
	 */
	public void save(ImMovementHead saveObj) throws ObtainSerialNoFailedException {
		insertMovement(saveObj);
	}

	/**
	 * save
	 *
	 * @param saveObj
	 * @return
	 * @throws ObtainSerialNoFailedException
	 */
	public String save(List<ImMovementHead> saveObjs) {
		StringBuffer msg = new StringBuffer();
		for (ImMovementHead saveObj : saveObjs) {
			try {
				String serialNo = buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), saveObj.getOrderTypeCode());
				if ("unknow".equals(serialNo)) {
					throw new ObtainSerialNoFailedException("取得" + saveObj.getBrandCode() + "-" + saveObj.getOrderTypeCode()
							+ "單號失敗！");
				} else {
					saveObj.setOrderNo(serialNo);
					resetLineReserve(saveObj.getImMovementItems());
					saveObj.setLastUpdateDate(new Date());
					saveObj.setCreationDate(new Date());
					imMovementHeadDAO.save(saveObj);
				}
			} catch (Exception ex) {
				msg.append(ex.getMessage());
				msg.append('\n');
				//ex.printStackTrace();
			}
		}
		return msg.toString();
	}

	/**
	 * update
	 *
	 * @param updateObj
	 * @return
	 */
	public void update(ImMovementHead updateObj) {
		modifyMovement(updateObj);
	}

	public ImMovementHead findById(Long headId) {
		return imMovementHeadDAO.findById(headId);
	}

	public void resetLineReserve(List<ImMovementItem> resetObj) {
		for (ImMovementItem item : resetObj) {
			item.setReserve1("Okay ");
			item.setReserve2("");
			item.setReserve3("");
			item.setReserve4("");
			item.setReserve5("");
			item.setArrivalQuantity(item.getDeliveryQuantity());
		}
	}

	public void checkImMovement(ImMovementHead checkObj) throws Exception {
		log.info("checkImMovement...old");
		ArrayList removeArray = new ArrayList(0);
		boolean isThreePointMoving = this.isThreePointMoving(checkObj.getBrandCode(), checkObj.getOrderTypeCode());

		log.info("1. checking brand....");
		BuBrand buBrand = buBrandDAO.findById(checkObj.getBrandCode());
		if (buBrand == null)
			throw new ValidationErrorException("品牌代號(" + checkObj.getBrandCode() + ")錯誤，請通知系統管理員");
		log.info("2. checking close date....");
		if (OrderStatus.WAIT_IN.equals(checkObj.getStatus()) || OrderStatus.SIGNING.equals(checkObj.getStatus())) {
			System.out.println("2.1 checking delivery close date....");
			ValidateUtil.isAfterClose(checkObj.getBrandCode(), checkObj.getOrderTypeCode(), "轉出日期", checkObj
					.getDeliveryDate(),checkObj.getSchedule());
		} else if (OrderStatus.FINISH.equals(checkObj.getStatus())) {
			log.info("2.2 checking arrival close date....");
			if (checkObj.getArrivalDate() == null)
				throw new ValidationErrorException("收貨日期不可為空白");
			ValidateUtil.isAfterClose(checkObj.getBrandCode(), checkObj.getOrderTypeCode(), "收貨日期", checkObj
					.getArrivalDate(),checkObj.getSchedule());
			log.info("2.3 checking arrival date is after delivery date....");
			String deliveryDateString = DateUtils.format(checkObj.getDeliveryDate(), "yyyyMMdd");
			String arrivalDateString = DateUtils.format(checkObj.getArrivalDate(), "yyyyMMdd");

			if (arrivalDateString.compareTo(deliveryDateString) < 0)
				throw new ValidationErrorException("收貨日期(" + arrivalDateString + ")不可小於轉出日期(" + deliveryDateString + ")"
						+ arrivalDateString.compareTo(deliveryDateString));
		}
		log.info("3. checking delivery warehouse....");
		// ----------轉入倉管人員送出時，不再檢查倉庫權限----------------------
		if (OrderStatus.WAIT_OUT.equals(checkObj.getStatus()) || OrderStatus.WAIT_IN.equals(checkObj.getStatus())
				|| OrderStatus.SIGNING.equals(checkObj.getStatus())) {
			ImWarehouse deliveryWarehouse = this.isCurrectDeliveryWarehouse(checkObj.getBrandCode(), checkObj
					.getOrderTypeCode(), checkObj.getDeliveryWarehouseCode(), checkObj.getLastUpdatedBy());
			if (null == deliveryWarehouse)
				throw new ValidationErrorException("轉出倉別代號(" + checkObj.getDeliveryWarehouseCode() + ")錯誤或調渡人員("
						+ checkObj.getLastUpdatedBy() + ")或倉管人員(" + checkObj.getDeliveryContactPerson() + ")無此倉庫權限");
			else
				checkObj.setCustomsDeliveryWarehouseCode(deliveryWarehouse.getCustomsWarehouseCode());
		}
		log.info("4. checking arrival warehouse....");
		ImWarehouse arrivalWarehouse = this.isCurrectArrivalWarehouse(checkObj.getBrandCode(), checkObj.getOrderTypeCode(),
				checkObj.getArrivalWarehouseCode(), checkObj.getLastUpdatedBy());
		if (null == arrivalWarehouse)
			throw new ValidationErrorException("轉入倉別代號(" + checkObj.getArrivalWarehouseCode() + ")錯誤"
					+ checkObj.getArrivalWarehouseCode().length());
		else
			checkObj.setCustomsArrivalWarehouseCode(arrivalWarehouse.getCustomsWarehouseCode());

		log.info("5. checking delivery warehouse manager....");
		if ("unknow".equals(UserUtils.getLoginNameByEmployeeCode(checkObj.getDeliveryContactPerson())))
			throw new ValidationErrorException("出庫倉管人員(" + checkObj.getDeliveryContactPerson() + ")錯誤");
		log.info("6. checking arrival warehouse manager....");
		if ("unknow".equals(UserUtils.getLoginNameByEmployeeCode(checkObj.getArrivalContactPerson())))
			throw new ValidationErrorException("入庫倉管人員(" + checkObj.getArrivalContactPerson() + ")錯誤");

		List<ImMovementItem> imMovementItems = checkObj.getImMovementItems();

		int i = 0;
		String status = "";
		if (OrderStatus.SAVE.equals(checkObj.getStatus())) {
			status = OrderStatus.SAVE;
		} else if (OrderStatus.WAIT_OUT.equals(checkObj.getStatus())) {
			status = OrderStatus.SAVE;
		} else if (OrderStatus.WAIT_IN.equals(checkObj.getStatus())) {
			status = isThreePointMoving ? OrderStatus.WAIT_OUT : OrderStatus.SAVE;
		} else if (OrderStatus.FINISH.equals(checkObj.getStatus())) {
			status = OrderStatus.WAIT_IN;
		} else if (OrderStatus.SIGNING.equals(checkObj.getStatus())) {
			status = OrderStatus.SAVE;
		} else {
			status = checkObj.getStatus();
		}
		log.info("7. checking items data....");
		if (imMovementItems == null || imMovementItems.size() == 0)
			throw new ValidationErrorException("尚未輸入明細資料，請重新確認");
		BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(checkObj.getBrandCode(), checkObj
				.getOrderTypeCode()));
		if (buOrderType == null)
			throw new ValidationErrorException("查無單別資料-OrderType(" + checkObj.getOrderTypeCode() + ")，請通知系統管理員");

		BuCommonPhraseLine orderTypeSetting = buCommonPhraseLineDAO.findById("ImMovementOrderType", checkObj.getBrandCode()
				+ checkObj.getOrderTypeCode());
		if (orderTypeSetting == null)
			throw new ValidationErrorException("查無單別資料-CommonPhraase(" + checkObj.getOrderTypeCode() + ")，請通知系統管理員");
		for (ImMovementItem imMovementItem : imMovementItems) {
			i++;
			if (StringUtils.hasText(imMovementItem.getItemCode())) {
				// Map checkInfo = new HashMap();
				// checkInfo.put("brandCode", checkObj.getBrandCode());
				// checkInfo.put("orderTypeCode", checkObj.getOrderTypeCode());
				// checkInfo.put("deliveryContactPerson",
				// checkObj.getDeliveryContactPerson());
				// checkInfo.put("arrivalContactPerson",
				// checkObj.getArrivalContactPerson());
				// checkInfo.put("beforestatus", checkObj.getStatus());
				// checkInfo.put("itemLevelControl",
				// buBrand.getItemLevelControl());
				// checkInfo.put("buOrderType",buOrderType);
				// checkInfo.put("orderTypeSetting",orderTypeSetting);
				// checkInfo.put("taxType", checkObj.getTaxType());
				// checkInfo.put("customsDeliveryWarehouseCode",
				// checkObj.getCustomsDeliveryWarehouseCode());
				// checkInfo.put("customsArrivalWarehouseCode",
				// checkObj.getCustomsArrivalWarehouseCode());
				// checkInfo.put("isThreePointMoving", isThreePointMoving);
				// this.reflashImMovementItem(checkInfo,imMovementItem);

				// call old reflashImMovementItem
				this.reflashImMovementItem(checkObj.getBrandCode(), checkObj.getOrderTypeCode(), checkObj
						.getDeliveryContactPerson(), checkObj.getArrivalContactPerson(), status, buBrand
						.getItemLevelControl(), imMovementItem);
				if ("Okay".equals(imMovementItem.getReserve1().trim())) {
					imMovementItem.setArrivalQuantity(imMovementItem.getDeliveryQuantity());
				} else {
					throw new ValidationErrorException("第" + i + "項,"
							+ StringTools.StringDelete(imMovementItem.getReserve1(), "Err - "));
				}
			} else {
				removeArray.add(imMovementItem);
			}

		}
		log.info("before remove size:" + imMovementItems.size());
		for (int j = removeArray.size(); 0 < j; j--) {
			imMovementItems.remove(removeArray.get(j - 1));
		}

		System.out.println("after remove size:" + imMovementItems.size());
	}

	/*
	  public void reflashImMovement(ImMovementHead reflashObj) throws FormException, NoSuchDataException {
		log.info("reflashImMovement");
		BuBrand buBrand = buBrandDAO.findById(reflashObj.getBrandCode());
		if (buBrand != null) {
			List<ImMovementItem> imMovementItems = reflashObj.getImMovementItems();
			BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(reflashObj.getBrandCode(), reflashObj
					.getOrderTypeCode()));
			if (buOrderType == null)
				throw new ValidationErrorException("查無單別資料-OrderType(" + reflashObj.getOrderTypeCode() + ")，請通知系統管理員");

			BuCommonPhraseLine orderTypeSetting = buCommonPhraseLineDAO.findById("ImMovementOrderType", reflashObj
					.getBrandCode()
					+ reflashObj.getOrderTypeCode());
			if (orderTypeSetting == null)
				throw new ValidationErrorException("查無單別資料-CommonPhraase(" + reflashObj.getOrderTypeCode() + ")，請通知系統管理員");
			int i = 0;
			Map checkInfo = new HashMap();
			checkInfo.put("brandCode", reflashObj.getBrandCode());
			checkInfo.put("orderTypeCode", reflashObj.getOrderTypeCode());
			checkInfo.put("deliveryContactPerson", reflashObj.getDeliveryContactPerson());
			checkInfo.put("arrivalContactPerson", reflashObj.getArrivalContactPerson());
			checkInfo.put("beforestatus", reflashObj.getStatus());
			checkInfo.put("itemLevelControl", buBrand.getItemLevelControl());
			checkInfo.put("buOrderType", buOrderType);
			checkInfo.put("orderTypeSetting", orderTypeSetting);
			checkInfo.put("taxType", reflashObj.getTaxType());
			checkInfo.put("customsDeliveryWarehouseCode", reflashObj.getCustomsDeliveryWarehouseCode());
			checkInfo.put("customsArrivalWarehouseCode", reflashObj.getCustomsArrivalWarehouseCode());
			System.out.println("headId:" + reflashObj.getHeadId());
			for (ImMovementItem imMovementItem : imMovementItems) {
				if (!StringUtils.hasText(imMovementItem.getItemCode())) {
					imMovementItem.setItemCode("");
				}
				this.reflashImMovementItem(checkInfo, imMovementItem);

				i++;
			}
		} else {
			throw new FormException("查詢品牌資訊(" + reflashObj.getBrandCode() + ")請通知資訊人員");
		}
	}
	*/

	public void reflashImMovementItem(Map checkInfo, ImMovementItem itemObj) throws FormException, NoSuchDataException {
		log.info("reflashImMovementItem...New");
		// 判斷為兩點或三點調撥
		StringBuffer errorString = new StringBuffer("");
		String brandCode = (String) checkInfo.get("brandCode");
		String orderTypeCode = (String) checkInfo.get("orderTypeCode");
		String itemCategory = (String) checkInfo.get("itemCategory");
		String deliveryContactPerson = (String) checkInfo.get("deliveryContactPerson");
		String arrivalContactPerson = (String) checkInfo.get("arrivalContactPerson");
		String beforeStatus = (String) checkInfo.get("beforestatus");
		String itemLevelControl = (String) checkInfo.get("itemLevelControl");
		String taxType = (String) checkInfo.get("taxType");
		String customsDeliveryWarehouseCode = (String) checkInfo.get("customsDeliveryWarehouseCode");
		String customsArrivalWarehouseCode = (String) checkInfo.get("customsArrivalWarehouseCode");
		String waitingWarehouseCode = (String) checkInfo.get("waitingWarehouseCode");
		Boolean isThreePointMoving = (Boolean) checkInfo.get("isThreePointMoving");

		BuCommonPhraseLine orderTypeSetting = (BuCommonPhraseLine) checkInfo.get("orderTypeSetting");
		String needItemCategory = StringUtils.hasText(orderTypeSetting.getAttribute3()) ? orderTypeSetting.getAttribute3()
				: "N";
		String checkItemCategory = StringUtils.hasText(orderTypeSetting.getParameter4()) ? orderTypeSetting.getParameter4()
				: "N";
		String allowMoreQty = StringUtils.hasText(orderTypeSetting.getAttribute4()) ? orderTypeSetting.getAttribute4() : "N";
		String movementMode = StringUtils.hasText(orderTypeSetting.getAttribute5()) ? orderTypeSetting.getAttribute5()
				: "NORMAL";
		String itemLimitField = StringUtils.hasText(orderTypeSetting.getParameter1()) ? orderTypeSetting.getParameter1()
				: "";
		String itemLimitOpt = StringUtils.hasText(orderTypeSetting.getParameter2()) ? orderTypeSetting.getParameter2() : "";
		String itemLimitValue = StringUtils.hasText(orderTypeSetting.getParameter3()) ? orderTypeSetting.getParameter3()
				: "";
		String showOriginalDeliveryQty = StringUtils.hasText(orderTypeSetting.getReserve2()) ? orderTypeSetting
				.getReserve2() : "N";
		Boolean disallowMinusStock = (Boolean) checkInfo.get("disallowMinusStock");
		// boolean isThreePointMoving = this.isThreePointMoving(orderTypeCode);
		log.info("beforeStatus:" + beforeStatus + "/isThreePointMoving:" + isThreePointMoving);
		// status為原Status往前推一層之狀態
		// CLOSE.FINISH.WAIT_OUT.WAIT_IN 此些狀態，於reflashImMovementItem時僅查出品名
		ImItem imItem = imItemDAO.findItem(brandCode, itemObj.getItemCode());
		if (imItem != null) {
			if (AjaxUtils.IS_DELETE_RECORD_TRUE.equals(itemObj.getIsDeleteRecord())) {
				log.info("delete record....");
				itemObj.setItemName(imItem.getItemCName());
				// itemObj.setOriginalDeliveryQuantity(0D);
				// itemObj.setDeliveryQuantity(0D);
				// itemObj.setArrivalQuantity(0D);
			} else {
				log.info("check 業種");
				if ("Y".equalsIgnoreCase(needItemCategory) && "Y".equalsIgnoreCase(checkItemCategory)) {
					if (!itemCategory.equalsIgnoreCase(imItem.getItemCategory()))
						this.appendErrorMessage(errorString, "業種不符(" + itemCategory + "/" + imItem.getItemCategory() + ")");
				}
				if ("T2".equals(brandCode)) {
					log.info("check 稅別");
					if (!taxType.equalsIgnoreCase(imItem.getIsTax()))
						this.appendErrorMessage(errorString, "稅別不符(" + taxType + "/" + imItem.getIsTax() + ")");
				}
				log.info("check 商品類別");
				if (StringUtils.hasText(itemLimitOpt)) {
					boolean foundString = itemLimitValue.indexOf(imItem.getCategory02()) != -1;
					if ((!foundString && "IN".equalsIgnoreCase(itemLimitOpt))
							|| (foundString && "NOTIN".equalsIgnoreCase(itemLimitOpt)))
						this.appendErrorMessage(errorString, "商品類別不符合此單據設定(" + itemLimitValue + "/" + imItem.getCategory02()
								+ ")");

				}
				log.info("check 報單 taxType:" + taxType + " delvieryWarehouseCode:" + customsDeliveryWarehouseCode
						+ " arrivalWarehouseCode:" + customsArrivalWarehouseCode);

				if (TAX_MODE_FREE.equalsIgnoreCase(taxType)
						&& !customsDeliveryWarehouseCode.equalsIgnoreCase(customsArrivalWarehouseCode)) {

					if (!StringUtils.hasText(itemObj.getOriginalDeclarationNo()))
						this.appendErrorMessage(errorString, "未輸入報單號碼");
					if (itemObj.getOriginalDeclarationSeq() == null || itemObj.getOriginalDeclarationSeq() == 0D)
						this.appendErrorMessage(errorString, "未輸入報單序號");
					if (null == itemObj.getBoxNo() || 0L == itemObj.getBoxNo())
						this.appendErrorMessage(errorString, "未輸入箱號");
				} else {
					itemObj.setOriginalDeclarationNo(null);
					itemObj.setOriginalDeclarationSeq(null);
					itemObj.setOriginalDeclarationDate(null);
				}
				
				if((orderTypeCode != null && !orderTypeCode.equalsIgnoreCase("IMS")) && itemObj.getDeliveryQuantity()<=0){
					this.appendErrorMessage(errorString, "轉出數量不得為0或負數!!");
				}

				log.info("status-before:" + beforeStatus);
				if (OrderStatus.FINISH.equals(beforeStatus) || OrderStatus.CLOSE.equals(beforeStatus)
						|| OrderStatus.WAIT_OUT.equals(beforeStatus) || OrderStatus.WAIT_IN.equals(beforeStatus)
						|| OrderStatus.SIGNING.equals(beforeStatus)) {
					System.out.println("7.1 just get item name");
					itemObj.setItemName(imItem.getItemCName());
					if (OrderStatus.WAIT_OUT.equals(beforeStatus)) {
						if (!"Y".equalsIgnoreCase(allowMoreQty)) { // 實際出貨數是否不可大於預算出貨數
							if (isThreePointMoving && itemObj.getOriginalDeliveryQuantity() < itemObj.getDeliveryQuantity()) {
								this.appendErrorMessage(errorString, "實際出庫數(" + itemObj.getOriginalDeliveryQuantity()
										+ ")不可大於預計出庫數(" + itemObj.getDeliveryQuantity() + ")");
							}
						}
						if ("2".equals(imItem.getLotControl()) && itemObj.getDeliveryQuantity() > 1) // 序號管理
							this.appendErrorMessage(errorString, "此商品為序號管理，實際出庫數不可>1");

					} else if (OrderStatus.WAIT_IN.equals(beforeStatus)) {
						if (null == itemObj.getArrivalQuantity())
							itemObj.setArrivalQuantity(itemObj.getDeliveryQuantity()); // 到貨數量等於出貨數量 modified by Weichun 2010.08.12
							//this.appendErrorMessage(errorString, "此到貨數量不可為空白");
					}

				} else {
					log.info("7.1 check item data");
					// --------------------------------------------------------//
					// 更新Lot No 依據是否需要輸入業種及調撥單的模式
					// T2於F00調至F99時，在LotNo中輸入的為未來到F99的效期或序號
					// 在F00(待驗倉)但其商品批號為defaultLotNo(000000000000)
					// 要使用此defaultLotNo查詢F00的庫存，才會有值
					// 因此需判斷此單別為ACCEPT模式時，要用上敘方式執行調撥
					// --------------------------------------------------------//
					String deliveryLotNo = new String("");
					deliveryLotNo = "Y".equalsIgnoreCase(needItemCategory)
							&& MOVEMENT_TYPE_ACCEPT.equalsIgnoreCase(movementMode) ? this.defaultLotNo : itemObj.getLotNo();
					log.info("item data:" + itemObj.getDeliveryWarehouseCode() + "/" + itemObj.getItemCode() + "/"
							+ deliveryLotNo + "/" + deliveryContactPerson + "/" + needItemCategory + "/" + movementMode);
					if (StringUtils.hasText(itemObj.getItemCode())
							&& StringUtils.hasText(itemObj.getDeliveryWarehouseCode()) && StringUtils.hasText(deliveryLotNo)) {

						try {
							itemObj.setItemCode(itemObj.getItemCode().toUpperCase());

							HashMap findObjs = new HashMap();
							findObjs.put("brandCode", brandCode);
							findObjs.put("itemName", "");
							findObjs.put("warehouseManager", deliveryContactPerson);
							findObjs.put("startItemCode", itemObj.getItemCode());
							findObjs.put("endItemCode", itemObj.getItemCode());
							findObjs.put("startWarehouseCode", itemObj.getDeliveryWarehouseCode());
							findObjs.put("endWarehouseCode", itemObj.getDeliveryWarehouseCode());
							findObjs.put("startLotNo", deliveryLotNo);
							findObjs.put("endLotNo", deliveryLotNo);
							findObjs.put("categorySearchString", "");

							String itemCode = new String("");
							String itemCName = new String("");
							String lotControl = new String("");
							String itemLevel = new String("");
							Double currentOnHandQty = new Double(0D);
							List<ImItemOnHandView> queryResult = imItemOnHandViewDAO.find(findObjs);
							log.info("7.1.1 find imItemOnHandView " + itemObj.getItemCode() + " query result size:"
									+ queryResult.size());

							// if (queryResult.size() > 0 ||
							// !disallowMinusStock) {
							if (queryResult.size() > 0) {
								ImItemOnHandView imItemOnHandView = (ImItemOnHandView) queryResult.get(0);
								itemCode = imItemOnHandView.getId().getItemCode().toUpperCase();
								itemCName = imItemOnHandView.getItemCName();
								itemLevel = imItemOnHandView.getItemLevel();
								lotControl = StringUtils.hasText(imItemOnHandView.getLotControl()) ? imItemOnHandView
										.getLotControl() : "N";
								currentOnHandQty = imItemOnHandView.getCurrentOnHandQty();
							} else {
								itemCode = imItem.getItemCode().toUpperCase();
								itemCName = imItem.getItemCName();
								lotControl = StringUtils.hasText(imItem.getLotControl()) ? imItem.getLotControl() : "N";
								itemLevel = imItem.getItemLevel();
								currentOnHandQty = 0D;
							}
							log.info("assign info from imItemOnHandView");
							itemObj.setItemCode(itemCode);
							itemObj.setItemName(itemCName);
							itemObj.setLotControl(lotControl);

							log.info("check lot control in imItemOnHandView");
							// 批號管理
							if ("1".equals(lotControl) || "2".equals(lotControl)) {
								if (!StringUtils.hasText(itemObj.getLotNo()) || defaultLotNo.equals(itemObj.getLotNo()))
									this.appendErrorMessage(errorString, "批號不可為空白或" + defaultLotNo);

							} else {
								if (waitingWarehouseCode.equalsIgnoreCase(itemObj.getDeliveryWarehouseCode())) {
								} else {
									itemObj.setLotNo(defaultLotNo);
									log.info("set default Lot NO as " + defaultLotNo);
								}

							}

							itemObj.setStockOnHandQuantity(currentOnHandQty);
							if (itemObj.getDeliveryQuantity() == null)
								itemObj.setDeliveryQuantity(0D);
							if (itemObj.getOriginalDeliveryQuantity() == null)
								itemObj.setOriginalDeliveryQuantity(0D);
							Double quantity = isThreePointMoving ? itemObj.getOriginalDeliveryQuantity() : itemObj
									.getDeliveryQuantity();
							// SIGNING要確認數量
							log.info("isThreePointMoving:" + isThreePointMoving);
							log.info("check quantity...." + quantity);
							if ("2".equals(lotControl) && quantity > 1) {
								log.info("sequence control...");
								this.appendErrorMessage(errorString, "此商品為序號管理，實際出庫數(" + quantity + ")不可大於1");
							} else if (quantity == null || quantity <= 0) {
								log.info("delivery quantity check...");
								this.appendErrorMessage(errorString, "轉出數量輸入錯誤");
							} else if (disallowMinusStock) {
								if (OrderStatus.SAVE.equals(beforeStatus) && quantity > itemObj.getStockOnHandQuantity()) {
									log.info("arrival quantity check...");
									this.appendErrorMessage(errorString, "轉出數量(" + quantity + ")不可大於庫存數量("
											+ itemObj.getStockOnHandQuantity() + ")");
								}
							} else if (itemObj.getArrivalWarehouseCode() != null) {
								log.info("check arrival warehouse");
								HashMap findWarehouseMap = new HashMap();
								findWarehouseMap.put("brandCode", brandCode);
								findWarehouseMap.put("warehouseCode", itemObj.getArrivalWarehouseCode());
								findWarehouseMap.put("warehouseName", "");
								findWarehouseMap.put("storage", "");
								findWarehouseMap.put("storageArea", "");
								findWarehouseMap.put("storageBin", "");
								findWarehouseMap.put("warehouseTypeId", 0L);
								findWarehouseMap.put("categoryCode", "");
								findWarehouseMap.put("locationId", 0L);
								findWarehouseMap.put("taxTypeCode", "");
								findWarehouseMap.put("warehouseManager", "");
								findWarehouseMap.put("enable", "Y");
								List<ImWarehouse> imWarehouses = imWarehouseDAO.find(findWarehouseMap);
								if (imWarehouses.size() == 0) {
									this.appendErrorMessage(errorString, "轉入庫別輸入錯誤(" + itemObj.getArrivalWarehouseCode()
											+ ")");
								} else {
									// 商品等級
									log.info("check item level");
									if ("Y".equals(itemLevelControl)) {
										if ("S".equals(imWarehouses.get(0).getCategoryCode())) {
											BuShopService buShopService = (BuShopService) SpringUtils
													.getApplicationContext().getBean("buShopService");
											String shopLevel = buShopService.findShopLevelbyWarehouseCode(imWarehouses
													.get(0).getWarehouseCode());

											if (shopLevel != null) {
												if (itemLevel.compareToIgnoreCase(shopLevel) < 0)
													this.appendErrorMessage(errorString, "商品與店櫃等級不符(商品:" + itemLevel
															+ "級，店櫃" + shopLevel + "級)");

											} else {
												this.appendErrorMessage(errorString, "轉入庫別為店櫃類型，但於店櫃設定中未設定出貨庫別");
											}
										}
									}
								}
							} else {
								this.appendErrorMessage(errorString, "您尚未輸入轉入庫別");
							}
							// }else{
							// itemObj.setStockOnHandQuantity(0D);
							// itemObj.setItemName("品號、庫號或批號輸入錯誤");
							// this.appendErrorMessage(errorString,
							// "查無品號("+itemObj.getItemCode()+
							// ")庫號("+itemObj.getDeliveryWarehouseCode()+
							// ")及批號("+deliveryLotNo+")之庫存資料");
							// }
						} catch (RuntimeException re) {
							re.printStackTrace();
						}

					} else {
						itemObj.setItemName("轉出庫別、品號、批號不可為空白");
						this.appendErrorMessage(errorString, "轉出庫別、品號、批號不可為空白");
					}
				}
			}
		} else {
			itemObj.setItemName("查無此品號");
			this.appendErrorMessage(errorString, "品牌:" + brandCode + ",品號(" + itemObj.getItemCode() + ")輸入錯誤");
		}
		// log.info("errorString:"+errorString.toString());
		if (errorString.length() > 0) {
			// itemObj.setReserve1("Err - 品號"+itemObj.getItemCode()+
			// "，"+errorString.toString());
			// itemObj.setItemName(errorString.toString());
			itemObj.setReturnMessage(errorString.toString());
			log.info(errorString.toString());
			// itemObj.setMessage(errorString.toString());
		} else {
			itemObj.setReserve1("Okay");
		}
	}

	public String getStatus(String formActoin, Long formId, Long processId, String formStatus, String approvalResult,
			String orderTypeCode, String orderNo) {
		log.info("5.2 getStatus");
		log.info("formActoin:" + formActoin + " formId:" + formId + " formId:" + formId + " processId:" + processId
				+ " formStatus:" + formStatus + " approvalResult:" + approvalResult + " orderTypeCode:" + orderTypeCode
				+ " orderNo:" + orderNo);
		String status = OrderStatus.UNKNOW;
		// 判斷為兩點或三點調撥
		// boolean isThreePointMoving = this.isThreePointMoving(orderTypeCode) ;
		// boolean isSigning = this.isSigningOrderType(orderTypeCode) ;

		if (FORM_SUBMIT.equalsIgnoreCase(formActoin) || FORM_SUBMIT_BG.equalsIgnoreCase(formActoin)) {
			if (formId == null || AjaxUtils.isTmpOrderNo(orderNo)) {
				// if(isSigning)
				status = OrderStatus.SIGNING;
				// else
				// status = isThreePointMoving ? OrderStatus.WAIT_OUT :
				// OrderStatus.WAIT_IN;
			} else if (processId != null || formStatus.equals(OrderStatus.UNCONFIRMED)) {
				if (formStatus.equals(OrderStatus.SAVE) || formStatus.equals(OrderStatus.UNCONFIRMED)
						|| formStatus.equals(OrderStatus.REJECT)) {
					// if(isSigning)
					status = OrderStatus.SIGNING;
					// else
					// status = isThreePointMoving ? OrderStatus.WAIT_OUT :
					// OrderStatus.WAIT_IN;

				} else if (formStatus.equals(OrderStatus.SIGNING)) {
					status = OrderStatus.SIGNING; // 簽核狀態於流程中做切換
				} else if (formStatus.equals(OrderStatus.WAIT_IN)) {
					status = OrderStatus.FINISH;
				} else if (formStatus.equals(OrderStatus.WAIT_OUT)) {
					status = OrderStatus.WAIT_IN;
				}
			}
		} else if (FORM_SAVE.equalsIgnoreCase(formActoin)) {
			// 新增VOID、REJECT，作廢、駁回時，也要改狀態為暫存。 Weichun 2010/10/04
			if (formStatus.equals(OrderStatus.SAVE) || formStatus.equals(OrderStatus.UNCONFIRMED)
					|| formStatus.equals(OrderStatus.VOID) || formStatus.equals(OrderStatus.REJECT)) {
				status = OrderStatus.SAVE;
			} else if (formStatus.equals(OrderStatus.SIGNING)) {
				status = OrderStatus.SIGNING;
			} else if (formStatus.equals(OrderStatus.WAIT_IN)) {
				status = OrderStatus.WAIT_IN;
			} else if (formStatus.equals(OrderStatus.WAIT_OUT)) {
				status = OrderStatus.WAIT_OUT;
			}

		} else if (FORM_VOID.equalsIgnoreCase(formActoin)) {
			status = OrderStatus.VOID;
		}
		log.info("    the new status is " + status);
		return status;
	}

	public static String getStatus(String formActoin, Long formId, Long processId, String formStatus, String approvalResult,
			String orderTypeCode) {
		String status = OrderStatus.UNKNOW;

		if (FORM_SUBMIT.equalsIgnoreCase(formActoin)) {
			if (formId == null) {
				status = OrderStatus.SIGNING;

			} else if (processId != null || formStatus.equals(OrderStatus.UNCONFIRMED)) {
				if (formStatus.equals(OrderStatus.SAVE) || formStatus.equals(OrderStatus.UNCONFIRMED)
						|| formStatus.equals(OrderStatus.REJECT)) {
					status = OrderStatus.SIGNING;
				} else if (formStatus.equals(OrderStatus.SIGNING)) {
					status = OrderStatus.SIGNING;
				} else if (formStatus.equals(OrderStatus.WAIT_IN)) {
					status = OrderStatus.FINISH;
				} else if (formStatus.equals(OrderStatus.WAIT_OUT)) {
					status = OrderStatus.WAIT_IN;
				}
			}
		} else if (FORM_SAVE.equalsIgnoreCase(formActoin)) {
			if (formStatus.equals(OrderStatus.SAVE) || formStatus.equals(OrderStatus.UNCONFIRMED)) {
				status = OrderStatus.SAVE;
			} else if (formStatus.equals(OrderStatus.SIGNING)) {
				status = OrderStatus.SIGNING;
			} else if (formStatus.equals(OrderStatus.WAIT_IN)) {
				status = OrderStatus.WAIT_IN;
			} else if (formStatus.equals(OrderStatus.WAIT_OUT)) {
				status = OrderStatus.WAIT_OUT;
			}

		} else if (FORM_VOID.equalsIgnoreCase(formActoin)) {
			status = OrderStatus.VOID;
		}

		return status;
	}

	public List getDeliveryWarehouseManager(Long headId) {
		return imMovementItemDAO.getDeliveryWarehouseManager(headId);
	}

	public List getArrivalWarehouseManager(Long headId) {
		return imMovementItemDAO.getArrivalWarehouseManager(headId);
	}

	public void updateMoveUncommitQuantity(ImMovementHead imMovementHead, boolean isAllowMinus) throws FormException,
			NoSuchDataException {
		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(imMovementHead.getBrandCode());
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("SystemConfig", "DefaultLotNo");
		String defaultLotNo = buCommonPhraseLine != null ? buCommonPhraseLine.getName() : "000000000000";

		boolean isThreePointMoving = this.isThreePointMoving(imMovementHead.getBrandCode(), imMovementHead
				.getOrderTypeCode());
		BuBrand buBrand = buBrandDAO.findById(imMovementHead.getBrandCode());
		String waitingWarehouseCode = "";
		if (null == buBrand) {
			throw new NoSuchDataException("依據品牌：" + imMovementHead.getBrandCode() + "查無其品牌代號！");
		} else {
			waitingWarehouseCode = buBrand.getDefaultWarehouseCode1() == null ? "" : buBrand.getDefaultWarehouseCode1();
		}
		if (!(StringUtils.hasText(organizationCode))) {
			throw new NoSuchDataException("依據品牌：" + imMovementHead.getBrandCode() + "查無其組織代號！");
		}

		List<ImMovementItem> imMovementItems = imMovementHead.getImMovementItems();
		for (ImMovementItem imMovementItem : imMovementItems) {
			// ==========預訂可用庫存==========
			Double quantity = 0D;
			String warehouseCode = "";
			String lotNo = imMovementItem.getLotNo();
			log.info("waitingWarehouseCode:" + waitingWarehouseCode);
			// When status of the from is WAIT_OUT, the moveUncommitQty must to
			// be subtracted
			if (OrderStatus.WAIT_OUT.equals(imMovementHead.getStatus())) {
				quantity = imMovementItem.getOriginalDeliveryQuantity() * -1;
				warehouseCode = imMovementItem.getDeliveryWarehouseCode();
			} else if (OrderStatus.WAIT_IN.equals(imMovementHead.getStatus())) {
				if (isThreePointMoving)
					quantity = imMovementItem.getOriginalDeliveryQuantity() - imMovementItem.getDeliveryQuantity();
				else
					quantity = imMovementItem.getDeliveryQuantity() * -1;
				warehouseCode = imMovementItem.getDeliveryWarehouseCode();
			} else if (OrderStatus.FINISH.equals(imMovementHead.getStatus())) {
				quantity = imMovementItem.getArrivalQuantity();
				warehouseCode = imMovementItem.getArrivalWarehouseCode();
				if (waitingWarehouseCode.equals(imMovementItem.getDeliveryWarehouseCode())
						&& !(waitingWarehouseCode.equals(imMovementItem.getArrivalWarehouseCode())))
					// to check item lot control flag, if the lotControl is "Y", the lotNo will be kept on original "N".equals(imMovementItem.getLotControl())
					lotNo = defaultLotNo;

			}
			if (quantity != 0) {
				if (isAllowMinus) {
					imOnHandDAO
							.updateMoveUncommitQuantityAllowMinus(organizationCode, imMovementItem.getItemCode(),
									warehouseCode, lotNo, quantity, imMovementHead.getLastUpdatedBy(), imMovementHead
											.getBrandCode());
				} else {
					imOnHandDAO.updateMoveUncommitQuantity(organizationCode, imMovementItem.getItemCode(), warehouseCode,
							lotNo, quantity, imMovementHead.getLastUpdatedBy(), imMovementHead.getBrandCode());
				}
			}
		}

	}

	public ImWarehouse isCurrectDeliveryWarehouse(String brandCode, String orderTypeCode, String warehouseCode,
			String warehouseEmployeeCode) {
		log.info("isCurrectDeliveryWarehouse...brandCode:" + brandCode + " warehouseCode:" + warehouseCode
				+ " orderTypeCode:" + orderTypeCode + " warehouseEmployeeCode:" + warehouseEmployeeCode);

		HashMap findObjs = new HashMap();
		findObjs.put("brandCode", brandCode);
		findObjs.put("warehouseCode", warehouseCode);
		findObjs.put("orderTypeCode", orderTypeCode);
		findObjs.put("employeeCode", warehouseEmployeeCode);

		List<ImWarehouse> imWarehouses = imWarehouseDAO.findMovementWarehouse("delivery", findObjs);
		log.info("imWarehouses.size:" + imWarehouses.size());
		if (imWarehouses.size() > 0) {
			return imWarehouses.get(0);
		} else {
			return null;
		}
	}

	public ImWarehouse isCurrectArrivalWarehouse(String brandCode, String orderTypeCode, String warehouseCode,
			String warehouseEmployeeCode) {
		log.info("isCurrectArrivalWarehouse...brandCode:" + brandCode + " warehouseCode:" + warehouseCode
				+ " orderTypeCode:" + orderTypeCode + " warehouseEmployeeCode:" + warehouseEmployeeCode);
		HashMap findObjs = new HashMap();
		findObjs.put("brandCode", brandCode);
		findObjs.put("warehouseCode", warehouseCode);
		findObjs.put("orderTypeCode", orderTypeCode);
		findObjs.put("employeeCode", warehouseEmployeeCode);

		List<ImWarehouse> imWarehouses = imWarehouseDAO.findMovementWarehouse("arrival", findObjs);
		if (imWarehouses.size() > 0) {
			return imWarehouses.get(0);
		} else {
			return null;
		}

	}

	/**
	 * 依據品牌代號、單別、單號查詢調撥單
	 *
	 * @param brandCode
	 * @param orderTypeCode
	 * @param orderNo
	 * @return ImMovementHead
	 * @throws Exception
	 */
	public ImMovementHead findMovementByIdentification(String brandCode, String orderTypeCode, String orderNo)
			throws Exception {
		try {
			return imMovementHeadDAO.findMovementByIdentification(brandCode, orderTypeCode, orderNo);
		} catch (Exception ex) {
			log.error("依據品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "查詢調撥單時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "查詢調撥單時發生錯誤，原因："
					+ ex.getMessage());
		}
	}

	/**
	 * 刪除調撥單
	 *
	 * @param imMovementItems
	 * @throws Exception
	 */
	public void deleteImMovementItems(List imMovementItems) throws Exception {

		try {
			for (int i = 0; i < imMovementItems.size(); i++) {
				imMovementItemDAO.delete(imMovementItems.get(i));
			}
		} catch (Exception ex) {
			log.error("刪除調撥單明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("刪除調撥單明細時發生錯誤！");
		}
	}

	public void executeDailyBalance(ImMovementHead imMovementHead, String opUser) throws Exception {
		String brandCode = imMovementHead.getBrandCode();
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("SystemConfig", "DefaultLotNo");
		String defaultLotNo = buCommonPhraseLine != null ? buCommonPhraseLine.getName() : "000000000000";

		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
		BuBrand buBrand = buBrandDAO.findById(imMovementHead.getBrandCode());
		String waitingWarehouseCode = "";
		if (null == buBrand)
			throw new NoSuchDataException("依據品牌：" + imMovementHead.getBrandCode() + "查無其品牌代號！");
		else {
			waitingWarehouseCode = buBrand.getDefaultWarehouseCode1() == null ? "" : buBrand.getDefaultWarehouseCode1();
		}
		if (null == imMovementHead.getDeliveryDate()) {
			throw new NoSuchDataException("單號" + imMovementHead.getOrderTypeCode() + "-" + imMovementHead.getOrderNo()
					+ "的出貨日期為null");
		} else {
			String monthlyCloseMonth = null == buBrand.getMonthlyBalanceMonth() ? "200001" : buBrand
					.getMonthlyBalanceMonth();
			String deliveryYYYYMM = DateUtils.format(imMovementHead.getDeliveryDate(), "yyyyMM");
			log.info(deliveryYYYYMM + "/" + monthlyCloseMonth);
			log.info("beforeCloseMonth:" + (Integer.valueOf(deliveryYYYYMM) <= Integer.valueOf(monthlyCloseMonth)));
			boolean beforeCloseMonth = Integer.valueOf(deliveryYYYYMM) <= Integer.valueOf(monthlyCloseMonth) ? true : false;
			String deliveryYYYY = DateUtils.format(imMovementHead.getDeliveryDate(), "yyyy");
			String deliveryMM = DateUtils.format(imMovementHead.getDeliveryDate(), "MM");
			String orderTypeCode = imMovementHead.getOrderTypeCode();
			String orderNo = imMovementHead.getOrderNo();
			String identification = MessageStatus.getIdentificationMsg(brandCode, orderTypeCode, orderNo);

			String status = OrderStatus.FINISH.equals(imMovementHead.getStatus()) ? OrderStatus.CLOSE : imMovementHead
					.getStatus();

			// 更新出貨單Status為CLOSE
			imMovementHead.setStatus(status);
			List<ImMovementItem> imMovementItems = imMovementHead.getImMovementItems();
			if (imMovementItems != null && imMovementItems.size() > 0) {
				for (ImMovementItem imMovementItem : imMovementItems) {
					imMovementItem.setStatus(status);
					String arrivalLotNo = imMovementItem.getLotNo();
					if (waitingWarehouseCode.equals(imMovementItem.getDeliveryWarehouseCode())
							&& !(waitingWarehouseCode.equals(imMovementItem.getArrivalWarehouseCode())))
						// TODO to check item lot control flag, if the
						// lotControl is "Y", the lotNo will be kept on original

						arrivalLotNo = defaultLotNo;
					this.updateOnHandAndTransaction(organizationCode, brandCode, orderTypeCode, orderNo, imMovementItem,
							imMovementHead.getDeliveryDate(), imMovementItem.getDeliveryWarehouseCode(), imMovementHead
									.getArrivalDate(), imMovementItem.getArrivalWarehouseCode(), arrivalLotNo,
							imMovementItem.getDeliveryQuantity(), opUser);
					if (beforeCloseMonth) {
						log.info("updatePeriodMovementQantity..." + imMovementHead.getHeadId() + "/" + deliveryYYYY
								+ deliveryMM);
						ImMonthlyBalanceHead imMonthlyBalanceHead = imMonthlyBalanceHeadDAO.findById(brandCode,
								imMovementItem.getItemCode(), deliveryYYYY, deliveryMM);
						if (null != imMonthlyBalanceHead) {
							log.info("imMonthlyBalanceHead is not null..." + imMovementHead.getHeadId());
							log.info(brandCode + "/" + deliveryYYYY + "/" + deliveryMM + "/" + imMovementItem.getItemCode()
									+ "/");
							log.info(imMovementItem.getDeliveryWarehouseCode() + "/" + arrivalLotNo + "/");
							log.info(imMovementItem.getDeliveryQuantity() * -1 + "/"
									+ imMonthlyBalanceHead.getAverageUnitCost());

							imMonthlyBalanceLineDAO.updatePeriodMovementQantity(brandCode, deliveryYYYY, deliveryMM,
									imMovementItem.getItemCode(), imMovementItem.getDeliveryWarehouseCode(), arrivalLotNo,
									imMovementItem.getDeliveryQuantity() * -1, imMonthlyBalanceHead.getAverageUnitCost());
							imMonthlyBalanceLineDAO.updatePeriodMovementQantity(brandCode, deliveryYYYY, deliveryMM,
									imMovementItem.getItemCode(), imMovementItem.getArrivalWarehouseCode(), arrivalLotNo,
									imMovementItem.getDeliveryQuantity(), imMonthlyBalanceHead.getAverageUnitCost());
						} else {
							throw new ValidationErrorException("於ImMonthlyBalanceHead查無" + deliveryYYYY + deliveryMM + "品號："
									+ imMovementItem.getItemCode() + "資料！");
						}
					}

				}

				imMovementHeadDAO.update(imMovementHead);
			} else {
				throw new ValidationErrorException("查無" + identification + "的調撥單明細資料！");
			}
		}

	}

	public void executeDailyBalance1(ImMovementHead imMovementHead, String opUser) throws Exception {
		String brandCode = imMovementHead.getBrandCode();
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("SystemConfig", "DefaultLotNo");
		String defaultLotNo = buCommonPhraseLine != null ? buCommonPhraseLine.getName() : "000000000000";

		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
		// String adjustWarehouseCode = "";
		// String monthlyBalanceMonth ="";
		BuBrand buBrand = buBrandDAO.findById(imMovementHead.getBrandCode());
		String waitingWarehouseCode = "";
		if (null == buBrand)
			throw new NoSuchDataException("依據品牌：" + imMovementHead.getBrandCode() + "查無其品牌代號！");
		else {
			// adjustWarehouseCode = buBrand.getDefaultWarehouseCode4() == null
			// ? "": buBrand.getDefaultWarehouseCode4();
			// monthlyBalanceMonth = buBrand.getMonthlyBalanceMonth();
			waitingWarehouseCode = buBrand.getDefaultWarehouseCode1() == null ? "" : buBrand.getDefaultWarehouseCode1();
		}

		String orderTypeCode = imMovementHead.getOrderTypeCode();
		String orderNo = imMovementHead.getOrderNo();
		String identification = MessageStatus.getIdentificationMsg(brandCode, orderTypeCode, orderNo);
		// String deliveryYYYYMM =
		// DateUtils.format(imMovementHead.getDeliveryDate(), "yyyyMM");
		// String arrivalYYYYMM =
		// DateUtils.format(imMovementHead.getArrivalDate(), "yyyyMM");
		// String deliveryWarehouse ="";
		// String arrivalWarehouse = "";
		// Date arrivalDate= null;
		// Date deliveryDate = null;
		String status = OrderStatus.FINISH.equals(imMovementHead.getStatus()) ? OrderStatus.CLOSE : imMovementHead
				.getStatus();
		// Double arrivalQuantity = new Double(0);
		// 到貨月與出貨月不同
		// boolean sameMonth = deliveryYYYYMM.equals(arrivalYYYYMM);
		// System.out.println(deliveryYYYYMM+"/"+arrivalYYYYMM);

		// 更新出貨單Status為CLOSE
		imMovementHead.setStatus(status);
		List<ImMovementItem> imMovementItems = imMovementHead.getImMovementItems();
		if (imMovementItems != null && imMovementItems.size() > 0) {
			for (ImMovementItem imMovementItem : imMovementItems) {
				imMovementItem.setStatus(status);
				// if(sameMonth){
				String arrivalLotNo = imMovementItem.getLotNo();
				if (waitingWarehouseCode.equals(imMovementItem.getDeliveryWarehouseCode())
						&& !(waitingWarehouseCode.equals(imMovementItem.getArrivalWarehouseCode())))
					// TODO to check item lot control flag, if the lotControl is
					// "Y", the lotNo will be kept on original
					// "N".equals(imMovementItem.getLotControl())
					arrivalLotNo = defaultLotNo;
				this.updateOnHandAndTransaction(organizationCode, brandCode, orderTypeCode, orderNo, imMovementItem,
						imMovementHead.getDeliveryDate(), imMovementItem.getDeliveryWarehouseCode(), imMovementHead
								.getArrivalDate(), imMovementItem.getArrivalWarehouseCode(), arrivalLotNo, imMovementItem
								.getArrivalQuantity(), opUser);
				// }else{
				// if(OrderStatus.FINISH.equals(imMovementHead.getStatus())){
				//
				// if(monthlyBalanceMonth.compareTo(deliveryYYYYMM) <
				// 0){//跨月調撥單，尚未執行月結作業
				// deliveryWarehouse =
				// imMovementItem.getDeliveryWarehouseCode();
				// arrivalWarehouse = adjustWarehouseCode;
				// arrivalQuantity = imMovementItem.getDeliveryQuantity();
				// arrivalDate =
				// DateUtils.getLastDateOfMonth(imMovementHead.getDeliveryDate());
				//
				// this.updateOnHandAndTransaction(organizationCode, brandCode,
				// orderTypeCode, orderNo,
				// imMovementItem, imMovementHead.getDeliveryDate(),
				// deliveryWarehouse,
				// arrivalDate, arrivalWarehouse, arrivalQuantity, opUser);
				//
				// deliveryWarehouse = adjustWarehouseCode;
				// arrivalWarehouse = imMovementItem.getArrivalWarehouseCode();
				// arrivalQuantity = imMovementItem.getArrivalQuantity();
				// deliveryDate = imMovementHead.getArrivalDate();
				// this.updateOnHandAndTransaction(organizationCode, brandCode,
				// orderTypeCode, orderNo,
				// imMovementItem, deliveryDate, deliveryWarehouse,
				// imMovementHead.getArrivalDate(), arrivalWarehouse,
				// arrivalQuantity, opUser);
				// }else{//跨月調撥單，己做過月結，當status 為 FINISH時，需從在途倉調入到貨倉
				// deliveryWarehouse = adjustWarehouseCode;
				// arrivalWarehouse = imMovementItem.getArrivalWarehouseCode();
				// arrivalQuantity = imMovementItem.getArrivalQuantity();
				// deliveryDate = imMovementHead.getArrivalDate();
				// this.updateOnHandAndTransaction(organizationCode, brandCode,
				// orderTypeCode, orderNo,
				// imMovementItem, deliveryDate, deliveryWarehouse,
				// imMovementHead.getArrivalDate(), arrivalWarehouse,
				// arrivalQuantity, opUser);
				// }
				//
				// }else
				// if(OrderStatus.WAIT_IN.equals(imMovementHead.getStatus())){
				// // only for monthly close use
				// deliveryWarehouse =
				// imMovementItem.getDeliveryWarehouseCode();
				// arrivalWarehouse = adjustWarehouseCode;
				// arrivalQuantity = imMovementItem.getDeliveryQuantity();
				// arrivalDate =
				// DateUtils.getLastDateOfMonth(imMovementHead.getDeliveryDate());
				// this.updateOnHandAndTransaction(organizationCode, brandCode,
				// orderTypeCode, orderNo,
				// imMovementItem, imMovementHead.getDeliveryDate(),
				// deliveryWarehouse,
				// arrivalDate, arrivalWarehouse, arrivalQuantity, opUser);
				// }

				// }
			}

			imMovementHeadDAO.update(imMovementHead);
		} else {
			throw new ValidationErrorException("查無" + identification + "的調撥單明細資料！");
		}

	}

	public void updateOnHandAndTransaction(String organizationCode, String brandCode, String orderTypeCode, String orderNo,
			ImMovementItem imMovementItem, Date deliveryDate, String deliveryWarehouse, Date arrivalDate,
			String arrivalWarehouse, String arrivalLotNo, Double arrivalQuantity, String opUser) throws FormException {
		if (imMovementItem.getDeliveryQuantity() != 0D) {

			imOnHandDAO.updateMoveOnHand(organizationCode, imMovementItem.getItemCode(), deliveryWarehouse, imMovementItem
					.getLotNo(), imMovementItem.getDeliveryQuantity() * -1, opUser, brandCode);

			imOnHandDAO.updateMoveOnHand(organizationCode, imMovementItem.getItemCode(), arrivalWarehouse, arrivalLotNo,
					arrivalQuantity, opUser, brandCode);

			Double averageUnitCost = 0D;
			// System.out.println("ff");
			// DateUtils.format(deliveryDate, "yyyy")
			// DateUtils.format(deliveryDate, "MM")
			// ImMonthlyBalanceHead imMonthlyBalanceHead =
			// (ImMonthlyBalanceHead)
			// imMonthlyBalanceHeadDAO.findById(brandCode,
			// imMovementItem.getItemCode(),"2009","04");

			// System.out.println("ee");
			// Double averageUnitCost = (imMonthlyBalanceHead ==null ? 0D :
			// imMonthlyBalanceHead.getAverageUnitCost());
			// System.out.println("averageUnitCost:"+averageUnitCost);
			// 產生交易明細檔 出庫

			ImTransation transationOut = new ImTransation();
			transationOut.setBrandCode(brandCode);
			transationOut.setTransationDate(deliveryDate);
			transationOut.setOrderTypeCode(orderTypeCode);
			transationOut.setOrderNo(orderNo);
			transationOut.setLineId(imMovementItem.getIndexNo());
			transationOut.setItemCode(imMovementItem.getItemCode());
			transationOut.setWarehouseCode(deliveryWarehouse);
			transationOut.setLotNo(imMovementItem.getLotNo());
			transationOut.setQuantity(imMovementItem.getDeliveryQuantity() * -1);
			transationOut.setReverseWarehouseCode(imMovementItem.getArrivalWarehouseCode());
			transationOut.setCostAmount(0D);
			transationOut.setAverageUnitCost(averageUnitCost * imMovementItem.getDeliveryQuantity() * -1);
			transationOut.setCreatedBy(opUser);
			transationOut.setCreationDate(new Date());
			transationOut.setLastUpdatedBy(opUser);
			transationOut.setLastUpdatedDate(new Date());
			imTransationDAO.save(transationOut);

			// 產生交易明細檔入庫
			ImTransation transationIn = new ImTransation();
			transationIn.setBrandCode(brandCode);
			transationIn.setTransationDate(deliveryDate);
			transationIn.setOrderTypeCode(orderTypeCode);
			transationIn.setOrderNo(orderNo);
			transationIn.setLineId(imMovementItem.getIndexNo());
			transationIn.setItemCode(imMovementItem.getItemCode());
			transationIn.setWarehouseCode(arrivalWarehouse);
			transationIn.setLotNo(arrivalLotNo);
			transationIn.setQuantity(arrivalQuantity);
			transationIn.setReverseWarehouseCode(imMovementItem.getDeliveryWarehouseCode());
			transationIn.setCostAmount(0D);
			transationIn.setAverageUnitCost(averageUnitCost * imMovementItem.getDeliveryQuantity());
			transationIn.setReserve1(arrivalDate.toString().substring(0, 10).replaceAll("-", "/"));
			transationIn.setCreatedBy(opUser);
			transationIn.setCreationDate(new Date());
			transationIn.setLastUpdatedBy(opUser);
			transationIn.setLastUpdatedDate(new Date());
			imTransationDAO.save(transationIn);
		}
	}

	/**
	 * 依據品牌代號、POS單別、POS單號查詢調撥單
	 *
	 * @param brandCode
	 * @param originalOrderTypeCode
	 * @param originalOrderNo
	 * @return ImMovementHead
	 * @throws Exception
	 */
	public ImMovementHead findPOSMovementByIdentification(String brandCode, String originalOrderTypeCode,
			String originalOrderNo) throws Exception {

		try {
			return imMovementHeadDAO.findPOSMovementByIdentification(brandCode, originalOrderTypeCode, originalOrderNo);
		} catch (Exception ex) {
			log.error("依據品牌代號：" + brandCode + "、POS調撥單別：" + originalOrderTypeCode + "、POS調撥單號：" + originalOrderNo
					+ "查詢調撥單時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據品牌代號：" + brandCode + "、POS調撥單別：" + originalOrderTypeCode + "、POS調撥單號：" + originalOrderNo
					+ "查詢調撥單時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 流程起始
	 *
	 * @param form
	 * @return
	 * @throws Exception
	 */
	public static Object[] startProcess(ImMovementHead form) throws Exception {
		log.info("startProcess");
		try {
			String packageId = "Im_Movement";
			String processId = "approval";
			// String version = "T2".equals(form.getBrandCode())?"3":"2";
			// String sourceReferenceType =
			// "T2".equals(form.getBrandCode())?"ImMovement (3)":"ImMovement
			// (2)";;

			String version = "3";
			String sourceReferenceType = "ImMovement (3)";

			HashMap context = new HashMap();
			context.put("brandCode", form.getBrandCode());
			context.put("formId", form.getHeadId());
			context.put("orderType", form.getOrderTypeCode());
			context.put("orderNo", form.getOrderNo());
			context.put("isThreePointMoving", false);
			Object[] object = ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);

			return object;
		} catch (Exception e) {
			//e.printStackTrace();
			log.error("調撥單流程執行時發生錯誤，原因：" + e.toString());
			throw new ProcessFailedException(e.getMessage());
		}

	}

	public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws Exception {
		try {

			HashMap context = new HashMap();
			context.put("approveResult", approveResult);
			context.put("form", approveResult);
			return ProcessHandling.completeAssignment(assignmentId, context);
		} catch (Exception e) {
			log.error("完成任務時發生錯誤：" + e.getMessage());
			throw new Exception(e);
		}

	}

	/*
	 * public static Object[] completeAssignment(long processId, String
	 * receiverEmployeeCode, boolean approveResult){
	 *
	 *
	 * HashMap assignmentCriteria = new HashMap(0);
	 * assignmentCriteria.put("packageId", "Im_Movement");
	 * assignmentCriteria.put("processDefId", "approval");
	 * assignmentCriteria.put("version", "1");
	 * assignmentCriteria.put("activityDefId", "confirmedByDeliver");
	 * assignmentCriteria.put("processId", processId);
	 *
	 * String loginUsername =
	 * UserUtils.getLoginNameByEmployeeCode(receiverEmployeeCode);
	 * assignmentCriteria.put("loginUsername", loginUsername); HashMap context =
	 * new HashMap(); context.put("approveResult", approveResult);
	 *
	 * return ProcessHandling.completeAssignment(assignmentCriteria, context); }
	 */
	public Object[] createOrUpdatePOSMovement(ImMovementHead movementHead) throws Exception {

		String identification = null;
		List errorMsgs = new ArrayList(0);
		if (movementHead.getHeadId() == null) {
			String serialNo = buOrderTypeService.getOrderSerialNo(movementHead.getBrandCode(), movementHead
					.getOrderTypeCode());
			if ("unknow".equals(serialNo)) {
				throw new ObtainSerialNoFailedException("取得" + movementHead.getBrandCode() + "-"
						+ movementHead.getOrderTypeCode() + "單號失敗！");
			} else {
				movementHead.setOrderNo(serialNo);
				resetLineReserve(movementHead.getImMovementItems());
				System.out.println("======== SAVE IM_MOVEMENT ========");
				System.out.println("ImMovementHead =====> " + movementHead.toString());
				imMovementHeadDAO.save(movementHead);
				System.out.println("======== SAVE FINISH ========");
				identification = MessageStatus.getIdentification(movementHead.getBrandCode(), movementHead
						.getOrderTypeCode(), movementHead.getOrderNo());
				// 庫存更新
				// updateMoveUncommitQuantity(movementHead,true);
				updateMoveOnHand(movementHead, false, identification, errorMsgs, "NORMAL", true);
			}
		} else {
			imMovementHeadDAO.update(movementHead);
			identification = MessageStatus.getIdentification(movementHead.getBrandCode(), movementHead.getOrderTypeCode(),
					movementHead.getOrderNo());
			// 庫存更新
			// updateMoveUncommitQuantity(movementHead,true);
			updateMoveOnHand(movementHead, false, identification, errorMsgs, "NORMAL", true);
		}

		return new Object[] { movementHead.getHeadId(), errorMsgs };
	}

	public void insertMovement(ImMovementHead saveObj) throws ObtainSerialNoFailedException {

		String serialNo = buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), saveObj.getOrderTypeCode());
		if ("unknow".equals(serialNo)) {
			throw new ObtainSerialNoFailedException("取得" + saveObj.getBrandCode() + "-" + saveObj.getOrderTypeCode()
					+ "單號失敗！");
		} else {
			saveObj.setOrderNo(serialNo);
			resetLineReserve(saveObj.getImMovementItems());
			ImWarehouse imDeliveryWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, saveObj
					.getDeliveryWarehouseCode());
			ImWarehouse imArrivalWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, saveObj
					.getArrivalWarehouseCode());
			// ----------------------------------------------------------------
			// Generate OriginalOrderType and OrderNo for POS data transfer user
			// ----------------------------------------------------------------
			if (!StringUtils.hasText(saveObj.getOriginalOrderTypeCode())) {
				if ("S".equals(imArrivalWarehouse.getCategoryCode())) {
					BuOrderTypeId buOrderTypeId = new BuOrderTypeId(saveObj.getBrandCode(), saveObj.getOrderTypeCode());
					BuOrderType buOrderType = buOrderTypeDAO.findById(buOrderTypeId);
					String originalOrderTypeCode = buOrderType.getNextOrderTypeCode();
					if (originalOrderTypeCode != null) {
						String originalSerialNo = buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(),
								originalOrderTypeCode);
						if ("unknow".equals(originalSerialNo)) {
							throw new ObtainSerialNoFailedException("取得" + saveObj.getBrandCode() + "-"
									+ originalOrderTypeCode + "單號失敗！");
						} else {
							if ("S".equals(imDeliveryWarehouse.getCategoryCode()))
								originalOrderTypeCode = "TT";
							else if ("W".equals(imDeliveryWarehouse.getCategoryCode()))
								originalOrderTypeCode = "TR";
							saveObj.setOriginalOrderTypeCode(originalOrderTypeCode);
							saveObj.setOriginalOrderNo(originalSerialNo);
						}
					} else {
						throw new ObtainSerialNoFailedException("此調撥單之收貨單位為專櫃(" + saveObj.getArrivalWarehouseCode()
								+ ")，其單別(" + saveObj.getOrderTypeCode() + ")設定未訂定POS調撥單別，請通知資訊單位處理");
					}
				}
			}
			saveObj.setLastUpdateDate(new Date());
			saveObj.setCreationDate(new Date());
			imMovementHeadDAO.save(saveObj);
		}
	}

	public void modifyMovement(ImMovementHead updateObj) {
		updateObj.setLastUpdateDate(new Date());
		resetLineReserve(updateObj.getImMovementItems());
		//imMovementHeadDAO.update(updateObj);
		imMovementHeadDAO.merge(updateObj);
	}

	public List executeBatchImport(List<ImMovementHead> imMovementHeads, List assembly) throws Exception {

		try {
			if (imMovementHeads != null) {
				for (int i = 0; i < imMovementHeads.size(); i++) {
					ImMovementHead movementHead = (ImMovementHead) imMovementHeads.get(i);
					if (movementHead.getHeadId() == null) {
						insertMovement(movementHead);
					} else {
						modifyMovement(movementHead);
					}
					assembly.add(movementHead.getBrandCode() + "-" + movementHead.getOrderTypeCode() + "-"
							+ movementHead.getOrderNo());
				}
			}

			return assembly;
		} catch (Exception ex) {
			log.error("執行調撥資料批次匯入失敗，原因：" + ex.toString());
			throw ex;
		}
	}

	public void restoreMovement(ImMovementHead imMovementHead, String afterStatus) throws FormException,
			NoSuchDataException, ObtainSerialNoFailedException {
		Double restoreQuantity = new Double(0D);
		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(imMovementHead.getBrandCode());
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("SystemConfig", "DefaultLotNo");
		String defaultLotNo = buCommonPhraseLine != null ? buCommonPhraseLine.getName() : "000000000000";
		List<ImMovementItem> imMovementItems = imMovementHead.getImMovementItems();
		if (OrderStatus.FINISH.equals(imMovementHead.getStatus()) || OrderStatus.WAIT_IN.equals(imMovementHead.getStatus())
				|| OrderStatus.WAIT_OUT.equals(imMovementHead.getStatus())) {
			for (ImMovementItem imMovementItem : imMovementItems) {
				restoreQuantity = OrderStatus.WAIT_OUT.equals(imMovementHead.getStatus()) ? imMovementItem
						.getOriginalDeliveryQuantity() : imMovementItem.getDeliveryQuantity();
				imOnHandDAO.updateMoveUncommitQuantity(organizationCode, imMovementItem.getItemCode(), imMovementItem
						.getDeliveryWarehouseCode(), imMovementItem.getLotNo(), restoreQuantity, imMovementHead
						.getLastUpdatedBy());

				if (OrderStatus.FINISH.equals(imMovementHead.getStatus())) {
					imOnHandDAO.updateMoveUncommitQuantity(organizationCode, imMovementItem.getItemCode(), imMovementItem
							.getArrivalWarehouseCode(), defaultLotNo, restoreQuantity * -1, imMovementHead
							.getLastUpdatedBy());
				}
			}
			this.update(imMovementHead);
		}
	}

	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================

			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
			String deliveryWarehouseCode = httpRequest.getProperty("deliveryWarehouseCode");// 出貨庫別
			String arrivalWarehouseCode = httpRequest.getProperty("arrivalWarehouseCode");// 轉入庫別
			String deliveryWarehouseManager = httpRequest.getProperty("deliveryWarehouseManager");
			String arrivalWarehouseManager = httpRequest.getProperty("arrivalWarehouseManager");
			String itemCategory = httpRequest.getProperty("itemCategory");
			String status = httpRequest.getProperty("status");
			String taxType = httpRequest.getProperty("taxType");
			String customsDeliveryWarehouseCode = httpRequest.getProperty("customsDeliveryWarehouseCode");
			String customsArrivalWarehouseCode = httpRequest.getProperty("customsArrivalWarehouseCode");
			String customsArrivalStoreCode = httpRequest.getProperty("customsArrivalStoreCode");
			HashMap map = new HashMap();
			map.put("brandCode", brandCode);
			map.put("orderTypeCode", orderTypeCode);
			map.put("deliveryWarehouseCode", deliveryWarehouseCode);
			map.put("deliveryWarehouseManager", deliveryWarehouseManager);
			map.put("arrivalWarehouseCode", arrivalWarehouseCode);
			map.put("arrivalWarehouseManager", arrivalWarehouseManager);
			map.put("itemCategory", itemCategory);
			map.put("status", status);
			map.put("taxType", taxType);
			map.put("customsDeliveryWarehouseCode", customsDeliveryWarehouseCode);
			map.put("customsArrivalWarehouseCode", customsArrivalWarehouseCode);
			map.put("customsArrivalStoreCode", customsArrivalStoreCode);
			log.info("customsDeliveryWarehouseCode : " + customsDeliveryWarehouseCode);
			log.info("customsArrivalWarehouseCode : " + customsArrivalWarehouseCode);
			log.info("customsArrivalStoreCode : " + customsArrivalStoreCode);
			// ==============================================================

			log.info("imMovementItems.headId" + headId);
			List<ImMovementItem> imMovementItems = imMovementItemDAO.findPageLine(headId, iSPage, iPSize);

			log.info("headId:" + headId);

			if (imMovementItems != null && imMovementItems.size() > 0) {

				// 取得第一筆的INDEX
				Long firstIndex = imMovementItems.get(0).getIndexNo();
				// 取得最後一筆 INDEX
				Long maxIndex = imMovementItemDAO.findPageLineMaxIndex(headId);
				// assignItemDefaultValue( map, imMovementItems, true);
				refreshImMovement(map, imMovementItems);
				log.info("ImMovement.AjaxUtils.getAJAXPageData ");

				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,
						imMovementItems, gridDatas, firstIndex, maxIndex));
			} else {
				log.info("ImMovement.AjaxUtils.getAJAXPageDataDefault ");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,
						gridDatas));

			}

			return result;
		} catch (Exception ex) {
			//ex.printStackTrace();
			log.error("載入頁面顯示的調撥明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的調撥明細失敗！");
		}
	}

	/**
	 * 更新ImItem相關資料(狀態為可編輯時)
	 *
	 * @param parameterMap
	 * @param salesOrderItems
	 */
	private void refreshImMovement(HashMap parameterMap, List<ImMovementItem> imMovementItems) throws Exception {
		log.info("reflashImMovement...new");
		String brandCode = (String) parameterMap.get("brandCode");
		String orderTypeCode = (String) parameterMap.get("orderTypeCode");
		String deliveryWarehouseCode = (String) parameterMap.get("deliveryWarehouseCode");
		// String deliveryWarehouseManager =
		// (String)parameterMap.get("deliveryWarehouseManager");
		// String arrivalWarehouseManager =
		// (String)parameterMap.get("arrivalWarehouseManager");
		// String status = (String)parameterMap.get("status");
		BuBrand buBrand = buBrandDAO.findById(brandCode);
		String waitingWarehouseCode = new String("");
		boolean isThreePointMoving = this.isThreePointMoving(brandCode, orderTypeCode);
		Boolean disallowMinusStock = new Boolean(true);
		if (buBrand == null)
			throw new ValidationErrorException("品牌代號(" + brandCode + ")錯誤，請通知系統管理員");
		else {
			waitingWarehouseCode = buBrand.getDefaultWarehouseCode1() == null ? "" : buBrand.getDefaultWarehouseCode1();
		}
		ImWarehouse imDeliveryWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class,
				deliveryWarehouseCode);
		if (null == imDeliveryWarehouse) {
			throw new ValidationErrorException("出庫庫別代號(" + brandCode + ")錯誤，請重新輸入");
		} else {
			disallowMinusStock = "Y".equals(imDeliveryWarehouse.getAllowMinusStock()) ? false : true;
		}
		BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(brandCode, orderTypeCode));
		if (buOrderType == null)
			throw new ValidationErrorException("查無單別資料-OrderType(" + orderTypeCode + ")，請通知系統管理員");

		BuCommonPhraseLine orderTypeSetting = buCommonPhraseLineDAO.findById("ImMovementOrderType", brandCode
				+ orderTypeCode);
		if (orderTypeSetting == null)
			throw new ValidationErrorException("查無單別資料-CommonPhraase(" + orderTypeCode + ")，請通知系統管理員");
		Map checkInfo = new HashMap();
		checkInfo.put("brandCode", brandCode);
		checkInfo.put("orderTypeCode", orderTypeCode);
		checkInfo.put("deliveryContactPerson", (String) parameterMap.get("deliveryWarehouseManager"));
		checkInfo.put("arrivalContactPerson", (String) parameterMap.get("arrivalWarehouseManager"));
		checkInfo.put("beforestatus", (String) parameterMap.get("status"));
		checkInfo.put("itemLevelControl", buBrand.getItemLevelControl());
		checkInfo.put("itemCategory", (String) parameterMap.get("itemCategory"));
		checkInfo.put("buOrderType", buOrderType);
		checkInfo.put("orderTypeSetting", orderTypeSetting);
		checkInfo.put("taxType", (String) parameterMap.get("taxType"));
		checkInfo.put("customsDeliveryWarehouseCode", (String) parameterMap.get("customsDeliveryWarehouseCode"));
		checkInfo.put("customsArrivalWarehouseCode", (String) parameterMap.get("customsArrivalWarehouseCode"));
		checkInfo.put("isThreePointMoving", isThreePointMoving);
		checkInfo.put("waitingWarehouseCode", waitingWarehouseCode);
		checkInfo.put("disallowMinusStock", disallowMinusStock);
		if (buBrand != null) {
			for (ImMovementItem imMovementItem : imMovementItems) {
				if (!StringUtils.hasText(imMovementItem.getItemCode())) {
					imMovementItem.setItemCode("");
				}
				// call new reflashImMovementItem
				this.reflashImMovementItem(checkInfo, imMovementItem);

			}
		} else {
			throw new FormException("查詢品牌資訊(" + brandCode + ")請通知資訊人員");
		}
	}

	/**
	 * 取得暫時單號存檔
	 *
	 * @param salesOrderHead
	 * @throws Exception
	 */
	public void saveTmp(ImMovementHead imMovementHead) throws Exception {

		try {
			String tmpOrderNo = AjaxUtils.getTmpOrderNo();
			System.out.println("tmpOrderNo:" + tmpOrderNo);
			imMovementHead.setOrderNo(tmpOrderNo);
			imMovementHead.setLastUpdateDate(new Date());
			imMovementHead.setCreationDate(new Date());
			imMovementHeadDAO.save(imMovementHead);
		} catch (Exception ex) {
			log.error("取得暫時單號儲存調撥單發生錯誤，原因：" + ex.toString());
			throw new Exception("取得暫時單號儲存調撥單發生錯誤，原因：" + ex.getMessage());
		}
	}

	public void assignItemDefaultValue(HashMap parameterMap, List<ImMovementItem> imMovementItems, boolean isReplace)
			throws Exception {
		String customsDeliveryWarehouseCode = (String) parameterMap.get("customsDeliveryWarehouseCode");
		String customsArrivalWarehouseCode = (String) parameterMap.get("customsArrivalWarehouseCode");
		String taxType = (String) parameterMap.get("taxType");
		try {
			for (ImMovementItem imMovementItem : imMovementItems) {
				if (isReplace) {
					imMovementItem.setDeliveryWarehouseCode((String) parameterMap.get("deliveryWarehouseCode"));
					imMovementItem.setArrivalWarehouseCode((String) parameterMap.get("arrivalWarehouseCode"));

				} else {
					if (!StringUtils.hasText(imMovementItem.getDeliveryWarehouseCode())) {
						imMovementItem.setDeliveryWarehouseCode((String) parameterMap.get("deliveryWarehouseCode"));
					}
					if (!StringUtils.hasText(imMovementItem.getArrivalWarehouseCode())) {
						imMovementItem.setArrivalWarehouseCode((String) parameterMap.get("arrivalWarehouseCode"));
					}
				}
				if ("P".equalsIgnoreCase(taxType) || customsDeliveryWarehouseCode == customsArrivalWarehouseCode) {
					imMovementItem.setOriginalDeclarationNo(null);
					imMovementItem.setOriginalDeclarationSeq(null);
					imMovementItem.setOriginalDeclarationDate(null);
				}
				log.info("Line-deliveryWarehouseCode:" + imMovementItem.getDeliveryWarehouseCode());
				log.info("Line-arrivalWarehouseCode:" + imMovementItem.getArrivalWarehouseCode());
				log.info("Line-OriginalDeclarationNo:" + imMovementItem.getOriginalDeclarationNo());
				log.info("Line-OriginalDeclarationSeq:" + imMovementItem.getOriginalDeclarationSeq());
				log.info("Line-OriginalDeclarationDate:" + imMovementItem.getOriginalDeclarationDate());
			}
			refreshImMovement(parameterMap, imMovementItems);
			resetLineReserve(imMovementItems);
		} catch (Exception ex) {
			log.error("取得暫時單號儲存調撥單發生錯誤，原因：" + ex.toString());
			throw new Exception("取得暫時單號儲存調撥單發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 更新PAGE的LINE
	 *
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData....");
		try {
			ImMovementHead imh = null;
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			imh = imMovementHeadDAO.findById(headId);
			if (headId == null) {
				throw new ValidationErrorException("傳入的調撥單主鍵為空值！");
			}
			String status = httpRequest.getProperty("status");
			String errorMsg = null;

			// log.info("updateAJAXPageLinesData.gridData:"+gridData);
			// log.info("updateAJAXPageLinesData.gridLineFirstIndex:"+gridLineFirstIndex);
			// log.info("updateAJAXPageLinesData.headId:"+headId);
			// log.info("updateAJAXPageLinesData.gridRowCount:"+gridRowCount);
			log.info("updateAJAXPageLinesData.status:" + status);
			if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)
					|| OrderStatus.UNCONFIRMED.equals(status) || OrderStatus.WAIT_OUT.equals(status)
					|| OrderStatus.WAIT_IN.equals(status)) {
				ImMovementHead imMovementHead = new ImMovementHead();
				imMovementHead.setHeadId(headId);
				// 將STRING資料轉成List Properties record data
				List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
						GRID_FIELD_NAMES);
				// Get INDEX NO
				//System.out.println("babu111111111:"+upRecords.size());
				int indexNo = imMovementItemDAO.findPageLineMaxIndex(headId).intValue();
				log.info("updateAJAXPageLinesData.maxIndexNo:" + indexNo);
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						System.out.println("babu1:"+upRecord.getProperty("itemCode"));
						System.out.println("babu2:"+upRecord.getProperty("itemName"));
						
						
						// 先載入HEAD_ID OR LINE DATA
						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
						log.info("updateAJAXPageLinesData.lineId:" + lineId);
						String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[2]);
						// 將arrivalQuantity = deliveryQuantity
						upRecord.setProperty(GRID_FIELD_NAMES[12], upRecord.getProperty(GRID_FIELD_NAMES[9]));
						
						
						
						if (StringUtils.hasText(itemCode)) {
							ImMovementItem imMovementItemUp = imMovementItemDAO.findItemByIdentification(imMovementHead
									.getHeadId(), lineId);
							
							
							if (imMovementItemUp != null) {
								AjaxUtils.setPojoProperties(imMovementItemUp, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								imMovementItemDAO.update(imMovementItemUp);
								if(imMovementItemUp.getItemCode()!=null||imMovementItemUp.getItemName()!=null){
									System.out.println("Babu'");
									
									/*Steve*/
									
									ImSysLog ims = new ImSysLog();
									ims.setHeadId(imMovementHead.getHeadId());
									ims.setAction("更新調撥單明細");
									ims.setCreate_date(new Date());
									ims.setCreated_by(imh.getCreatedBy());
									ims.setIdentification(imh.getBrandCode()+imh.getOrderTypeCode()+imh.getOrderNo());
									ims.setMessage(imMovementItemUp.getItemName());
									ims.setModule_name("Im_Movement");
									imSysLogDAO.save(ims);
									ims=null;
								    
								    /*Steve*/
								}
							} else {
								indexNo++;
								ImMovementItem imMovementItem = new ImMovementItem();
								AjaxUtils.setPojoProperties(imMovementItem, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								imMovementItem.setIndexNo(Long.valueOf(indexNo));
								log.info("SET-imMovementItemIndexNo:"+indexNo+",itemcode:"+itemCode);
								imMovementItem.setImMovementHead(imMovementHead);
								imMovementItemDAO.save(imMovementItem);
							}
						}
					}
				}
			}

			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("更新調撥明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新調撥明細失敗！");
		}
	}

	/**
	 *
	 *
	 * @param httpRequest
	 * @return
	 * @throws ValidationErrorException
	 */
	public List<Properties> updateItemRelationData(Properties httpRequest) throws ValidationErrorException {

		try {
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
			// ======================帶入Head的值=========================
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
			String deliveryWarehouseCode = httpRequest.getProperty("deliveryWarehouseCode");// 出貨庫別
			String arrivalWarehouseCode = httpRequest.getProperty("arrivalWarehouseCode");// 轉入庫別
			String deliveryWarehouseManager = httpRequest.getProperty("deliveryWarehouseManager");
			String arrivalWarehouseManager = httpRequest.getProperty("arrivalWarehouseManager");
			String itemCategory = httpRequest.getProperty("itemCategory");
			String status = httpRequest.getProperty("status");
			String taxType = httpRequest.getProperty("taxType");
			String customsDeliveryWarehouseCode = httpRequest.getProperty("customsDeliveryWarehouseCode");
			String customsArrivalWarehouseCode = httpRequest.getProperty("customsArrivalWarehouseCode");

			HashMap map = new HashMap();
			map.put("brandCode", brandCode);
			map.put("orderTypeCode", orderTypeCode);
			map.put("deliveryWarehouseCode", deliveryWarehouseCode);
			map.put("deliveryWarehouseManager", deliveryWarehouseManager);
			map.put("arrivalWarehouseCode", arrivalWarehouseCode);
			map.put("arrivalWarehouseManager", arrivalWarehouseManager);
			map.put("itemCategory", itemCategory);
			map.put("status", status);
			map.put("taxType", taxType);
			map.put("customsDeliveryWarehouseCode", customsDeliveryWarehouseCode);
			map.put("customsArrivalWarehouseCode", customsArrivalWarehouseCode);

			if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)
					|| OrderStatus.UNCONFIRMED.equals(status)) {
				ImMovementHead imMovementHeadPO = findById(headId);
				if (imMovementHeadPO == null) {
					throw new ValidationErrorException("查無調撥單主鍵：" + headId + "的資料！");
				}
				removeAJAXLine(imMovementHeadPO);
				assignItemDefaultValue(map, imMovementHeadPO.getImMovementItems(), true);
				imMovementHeadDAO.merge(imMovementHeadPO);
			}

			return AjaxUtils.getResponseMsg(null);
		} catch (Exception ex) {
			log.error("更新調撥明細相關欄位發生錯誤，原因：" + ex.toString());
			throw new ValidationErrorException("更新調撥明細相關欄位失敗！");
		}
	}

	/**
	 * remove delete mark record
	 *
	 * @param salesOrderHead
	 */
	private void removeAJAXLine(ImMovementHead imMovementHead) {

		List<ImMovementItem> imMovementItems = imMovementHead.getImMovementItems();
		if (imMovementItems != null) {
			for (int i = imMovementItems.size() - 1; i >= 0; i--) {
				ImMovementItem imMovementItem = imMovementItems.get(i);
				if (AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imMovementItem.getIsDeleteRecord())) {
					imMovementItems.remove(imMovementItem);
				}
			}
		}
	}

	/**
	 * 初始化畫面
	 *
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> executeInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			//String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			//String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String itemCategory = (String) PropertyUtils.getProperty(otherBean, "itemCategory");
			String warehouseManager = new String();
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("SystemConfig", "DefaultLotNo");

			defaultLotNo = buCommonPhraseLine != null ? buCommonPhraseLine.getName() : "000000000000";
			
			Map multiList = new HashMap(0);

			ImMovementHead form = null == formId ? executeNewImMovement(otherBean, resultMap) : findImMovement(otherBean,resultMap);

			BuCommonPhraseLine orderTypeSetting = buCommonPhraseLineDAO.findById("ImMovementOrderType", form.getBrandCode() 
					+ form.getOrderTypeCode());
			resultMap.put("itemLimitField", orderTypeSetting != null ? orderTypeSetting.getParameter1() : "");
			resultMap.put("itemLimitOpt", orderTypeSetting != null ? orderTypeSetting.getParameter2() : "");
			resultMap.put("itemLimitValue", orderTypeSetting != null ? orderTypeSetting.getParameter3() : "");
			resultMap.put("checkItemCategory", orderTypeSetting != null ? orderTypeSetting.getParameter4() : "N"); // 收貨時是否可輸入到貨數
			// //是否check業種子類
			resultMap.put("voidMode", orderTypeSetting != null ? orderTypeSetting.getParameter5() : "NN"); // 作廢模式
			// O:WAIT_OUT可作廢
			// 作廢模式
			// I:WAIT_IN可作廢
			// B:WAIT_OUT與WAIT_IN可作廢
			resultMap.put("itemCategoryMode", orderTypeSetting != null ? orderTypeSetting.getAttribute3() : "N"); // 是否可需輸入業種子類代號
			resultMap.put("allowMoreQty", orderTypeSetting != null ? orderTypeSetting.getAttribute4() : "N"); // 是否出貨量可大於預計出貨量
			resultMap.put("movementType", orderTypeSetting != null ? orderTypeSetting.getAttribute5() : "NORMAL"); // 調撥單模式
			resultMap.put("showOriginalDeliveryQty", orderTypeSetting != null ? orderTypeSetting.getReserve2() : "N"); // 調撥單模式
			String itemCategoryMode = orderTypeSetting != null ? orderTypeSetting.getAttribute3() : "N";
			
			List<ImWarehouse> allDeliveryWarehouses = new ArrayList(0);
			if (OrderStatus.SAVE.equalsIgnoreCase(form.getStatus()) || OrderStatus.REJECT.equalsIgnoreCase(form.getStatus())
					|| OrderStatus.UNCONFIRMED.equalsIgnoreCase(form.getStatus())) {
				allDeliveryWarehouses = this.getWarehouseInfo("delivery", form.getOrderTypeCode(), form.getBrandCode(), form
						.getCreatedBy(), itemCategoryMode);
			} else {
				allDeliveryWarehouses = this.getWarehouseInfo("delivery", form.getOrderTypeCode(), form.getBrandCode(),
						null, itemCategoryMode);
			}

			List<ImWarehouse> allArrivalWarehouses = this.getWarehouseInfo("arrival", form.getOrderTypeCode(), form.getBrandCode(), null,
			 itemCategoryMode);
			List<ImWarehouse> allArrivalStore = null;
			
			// 如果是贈品、試用品、陳列品
			if ("WGF".equals(form.getOrderTypeCode()) || "WGP".equals(form.getOrderTypeCode())
					|| "WHF".equals(form.getOrderTypeCode()) || "WHP".equals(form.getOrderTypeCode())
					|| "WFF".equals(form.getOrderTypeCode()) || "WFP".equals(form.getOrderTypeCode()))
				allArrivalStore = this.getWarehouseInfo("arrival", "DTP", form.getBrandCode(), null, itemCategoryMode);
			else
				allArrivalStore = new ArrayList();

			List<ImItemCategory> allItemCategories = imItemCategoryDAO.findByCategoryType(form.getBrandCode(),
					"ITEM_CATEGORY");
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(form.getBrandCode(), "IM");
			List<BuCommonPhraseLine> allReportList = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"ImMovementReport", "attribute1='" + form.getBrandCode() + "'");

			if (null == formId || OrderStatus.SAVE.equals(form.getStatus())
					|| OrderStatus.UNCONFIRMED.equals(form.getStatus())) {
				if ("Y".equalsIgnoreCase(itemCategoryMode))
					itemCategory = StringUtils.hasText(form.getItemCategory()) ? form.getItemCategory() : allItemCategories
							.get(0).getId().getCategoryCode();
				else
					itemCategory = null;
				warehouseManager = this.getWarehouseManager(form.getBrandCode(), form.getDeliveryWarehouseCode(), form
						.getCustomsDeliveryWarehouseCode(), itemCategoryMode, itemCategory);

				form.setDeliveryContactPerson(warehouseManager);
				form.setDeliveryContactPersonName(UserUtils.getUsernameByEmployeeCode(form.getDeliveryContactPerson()));
				warehouseManager = this.getWarehouseManager(form.getBrandCode(), form.getArrivalWarehouseCode(), form
						.getCustomsArrivalWarehouseCode(), itemCategoryMode, itemCategory);
				form.setArrivalContactPerson(warehouseManager);
				form.setArrivalContactPersonName(UserUtils.getUsernameByEmployeeCode(form.getArrivalContactPerson()));

			}
			
			BuCommonPhraseLine taxName = buCommonPhraseLineDAO.findById("TaxCatalog", form.getTaxType());
			resultMap.put("taxTypeName", taxName != null ? taxName.getName() : "");
			multiList.put("allDeliveryWarehouses", AjaxUtils.produceSelectorData(allDeliveryWarehouses, "warehouseCode",
					"warehouseName", true, false));
			
			multiList.put("allArrivalWarehouses", AjaxUtils.produceSelectorData(allArrivalWarehouses, "warehouseCode",
					"warehouseName", true, false));
			multiList.put("allArrivalStore", AjaxUtils.produceSelectorData(allArrivalStore, "warehouseCode",
					"warehouseName", true, true)); // 轉入店別
			multiList.put("allOrderTypes", AjaxUtils
					.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
			multiList.put("allItemCategories", AjaxUtils.produceSelectorData(allItemCategories, "categoryCode",
					"categoryName", true, !"Y".equalsIgnoreCase((String) resultMap.get("itemCategoryMode"))));
			multiList.put("allReportList", AjaxUtils.produceSelectorData(allReportList, "lineCode", "name", true, false));

			this.setMoveTypeArray(allOrderTypes);
			resultMap.put("isThreePointMoving", this.isThreePointMoving(form.getBrandCode(), form.getOrderTypeCode()));
			resultMap.put("multiList", multiList);
			// resultMap.put("moveFlowType",
			// this.isThreePointMoving(orderTypeCode));
			// resultMap.put("moveSignType",
			// this.isSigningOrderType(orderTypeCode));
			//System.out.println("executeInitial.OrderTypeCode:" + form.getOrderTypeCode() + " OrderNo:" + form.getOrderNo());
			
			//for 儲位用
    		if(imStorageAction.isStorageExecute(form)){
    			//建立儲位單
    			Map storageMap = new HashMap();
    			storageMap.put("storageTransactionDate", "deliveryDate");
    			storageMap.put("storageTransactionType", ImStorageService.MOVE);
    			storageMap.put("deliveryWarehouseCode", "deliveryWarehouseCode");
    			storageMap.put("arrivalWarehouseCode", "arrivalWarehouseCode");
    			
    			ImStorageHead imStorageHead = imStorageAction.executeImStorageHead(storageMap, form);

    			resultMap.put("storageHeadId", imStorageHead.getStorageHeadId());
    			resultMap.put("beanHead", "ImMovementHead");
    			resultMap.put("beanItem", "imMovementItems");
    			resultMap.put("quantity", "deliveryQuantity");
    			resultMap.put("storageTransactionType", ImStorageService.MOVE);
    			resultMap.put("storageStatus", "#F.status");
    			resultMap.put("deliveryWarehouse", "#F.deliveryWarehouseCode");
    			resultMap.put("arrivalWarehouse", "#F.arrivalWarehouseCode");
    		}
			
		} catch (Exception ex) {
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "表單初始化失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}
		
		

		return AjaxUtils.parseReturnDataToJSON(resultMap);

	}

	/**
	 * 建立新的調撥單
	 *
	 * @param otherBean
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	public ImMovementHead executeNewImMovement(Object otherBean, Map resultMap) throws Exception {
		log.info("executeNewImMovement....");
		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
		String itemCategory = (String) PropertyUtils.getProperty(otherBean, "itemCategory");
		String itemCategoryMode = (String) resultMap.get("itemCategoryMode");

		List<ImWarehouse> allDeliveryWarehouses = this.getWarehouseInfo("delivery", orderTypeCode, loginBrandCode,
				loginEmployeeCode, itemCategoryMode);
		List<ImWarehouse> allArrivalWarehouses = this.getWarehouseInfo("arrival", orderTypeCode, loginBrandCode, null,
				itemCategoryMode);
		List<ImItemCategory> allItemCategories = imItemCategoryDAO.findByCategoryType(loginBrandCode, "ITEM_CATEGORY");
		BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(loginBrandCode, orderTypeCode));
		if (buOrderType != null) {
			ImMovementHead form = new ImMovementHead();
			form.setBrandCode(loginBrandCode);
			form.setOrderTypeCode(orderTypeCode);
			form.setStatus("SAVE");
			form.setCreatedBy(loginEmployeeCode);
			form.setDeliveryDate(new Date());
			form.setDeliveryContactPerson("");
			form.setDeliveryContactPersonName("");
			form.setDeliveryCity("");
			form.setDeliveryArea("");
			form.setDeliveryZipCode("");
			form.setDeliveryAddress("");
			form.setArrivalContactPerson("");
			form.setArrivalContactPersonName("");
			form.setArrivalCity("");
			form.setArrivalArea("");
			form.setArrivalZipCode("");
			form.setArrivalAddress("");
			form.setRemark1("");
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setLastUpdateDate(null);
			form.setTaxType(buOrderType.getTaxCode());
			form.setBoxCount(0D);
			form.setItemCount(0D);
			form.setPackedBy("");
			form.setComfirmedBy("");
			form.setItemCategory(itemCategory);
			BuLocationService buLocationService = (BuLocationService) SpringUtils.getApplicationContext().getBean(
					"buLocationService");
			if (allDeliveryWarehouses.size() > 0) {
				form.setDeliveryWarehouseCode(allDeliveryWarehouses.get(0).getWarehouseCode());
				form.setDeliveryWarehouseName(allDeliveryWarehouses.get(0).getWarehouseName());
				// form.setDeliveryContactPerson(
				// allDeliveryWarehouses.get(0).getWarehouseManager());
				form.setCustomsDeliveryWarehouseCode(allDeliveryWarehouses.get(0).getCustomsWarehouseCode());
				// form.setDeliveryContactPersonName(UserUtils.getUsernameByEmployeeCode(form.getDeliveryContactPerson()));
				BuLocation buLocation = buLocationService.findById(allDeliveryWarehouses.get(0).getLocationId());
				if (null != buLocation) {
					form.setDeliveryCity(buLocation.getCity());
					form.setDeliveryArea(buLocation.getArea());
					form.setDeliveryZipCode(buLocation.getZipCode());
					form.setDeliveryAddress(buLocation.getAddress());
				}
			} else {
				throw new ValidationErrorException("您無任何的倉庫調渡權限，於按下「確認」鍵後，將關閉本視窗！");
			}
			if (allArrivalWarehouses.size() > 0) {
				form.setArrivalWarehouseCode(allArrivalWarehouses.get(0).getWarehouseCode());
				form.setArrivalWarehouseName(allArrivalWarehouses.get(0).getWarehouseName());
				form.setCustomsArrivalWarehouseCode(allArrivalWarehouses.get(0).getCustomsWarehouseCode());
				// form.setArrivalContactPersonName(UserUtils.getUsernameByEmployeeCode(form.getArrivalContactPerson()));
				BuLocation buLocation = buLocationService.findById(allArrivalWarehouses.get(0).getLocationId());
				if (null != buLocation) {
					form.setArrivalCity(buLocation.getCity());
					form.setArrivalArea(buLocation.getArea());
					form.setArrivalZipCode(buLocation.getZipCode());
					form.setArrivalAddress(buLocation.getAddress());
				}
				this.saveTmp(form);
				resultMap.put("totalOriginalDeliveryQuantity", 0D);
				resultMap.put("totalDeliveryQuantity", 0D);
				resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
				resultMap.put("brandName", buBrandDAO.findById(form.getBrandCode()).getBrandName());
				resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));
				resultMap.put("packedByName", "");
				resultMap.put("comfirmedByName", "");
				resultMap.put("receiptedByName", "");
				resultMap.put("allowMinusStock", allDeliveryWarehouses.get(0).getAllowMinusStock());

				// form.setImMovementItems(null);
				resultMap.put("form", form);
				return form;
			} else {
				throw new NoSuchDataException("查無任何查無轉出庫別，於按下「確認」鍵後，將關閉本視窗！");
			}
		} else {
			throw new NoSuchDataException("單別錯誤，請聯絡資訊單位！");
		}
	}

	/**
	 * 找出該張調撥單的資料
	 *
	 * @param otherBean
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	public ImMovementHead findImMovement(Object otherBean, Map resultMap) throws Exception {
		log.info("findImMovement....");
		Double totalOriginalDeliveryQuantity = 0D;
		Double totalDeliveryQuantity = 0D;
		String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;

		ImMovementHead form = imMovementHeadDAO.findById(formId);
		MessageBox msgBox = new MessageBox();
		if (null != form) {

			System.out.println("OrderStatus:" + form.getStatus());

			ImWarehouse imDeliveryWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, form
					.getDeliveryWarehouseCode());
			if (null != imDeliveryWarehouse) { // 轉出庫別
				form.setDeliveryWarehouseName(imDeliveryWarehouse.getWarehouseName());
				form.setCustomsDeliveryWarehouseCode(imDeliveryWarehouse.getCustomsWarehouseCode());

			} else {
				//throw new ValidationErrorException("查無轉出庫別資料！");
				msgBox.setMessage("查無轉出庫別資料！");
				form.setDeliveryWarehouseName("");
				form.setCustomsDeliveryWarehouseCode("");
			}
			if (!(OrderStatus.SAVE.equals(form.getStatus()) && OrderStatus.UNCONFIRMED.equals(form.getStatus()))) {

				ImWarehouse imArrivalWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, form
						.getArrivalWarehouseCode());
				if (null != imArrivalWarehouse) { // 轉入庫別
					form.setArrivalWarehouseName(imArrivalWarehouse.getWarehouseName());
					form.setCustomsArrivalWarehouseCode(imArrivalWarehouse.getCustomsWarehouseCode());
				} else {
					//throw new ValidationErrorException("查無轉入庫別資料！");
					msgBox.setMessage("查無轉入庫別資料！");
					form.setArrivalWarehouseName("");
					form.setCustomsArrivalWarehouseCode("");
				}

				ImWarehouse imArrivalStore = null; // 轉入店別
				if (form.getArrivalStoreCode() != null && !"".equals(form.getArrivalStoreCode()))
					imArrivalStore = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, form
							.getArrivalStoreCode());
				if (null != imArrivalStore) {
					form.setCustomsArrivalStoreCode(imArrivalStore.getCustomsWarehouseCode());
				} else {
					log.info("該調撥單無轉入店別資料");
				}

				if (StringUtils.hasText(form.getDeliveryContactPerson()))
					form.setDeliveryContactPersonName(UserUtils.getUsernameByEmployeeCode(form.getDeliveryContactPerson()));
				if (StringUtils.hasText(form.getArrivalContactPerson()))
					form.setArrivalContactPersonName(UserUtils.getUsernameByEmployeeCode(form.getArrivalContactPerson()));
				
				// 開啟單據時，不寫入轉入日期 by Weichun 2012.03.08
				//if (OrderStatus.WAIT_IN.equals(form.getStatus()) && null == form.getArrivalDate())
				//	form.setArrivalDate(new Date());
				// form.setLastUpdatedBy((String)PropertyUtils.getProperty(otherBean,
				// "loginEmployeeCode"));
				// form.setLastUpdateDate(new Date());

			}

			System.out.println("findImMovement.reflashImMovement.");
			HashMap map = new HashMap();
			map.put("brandCode", form.getBrandCode());
			map.put("orderTypeCode", form.getOrderTypeCode());
			map.put("deliveryWarehouseCode", form.getDeliveryWarehouseCode());
			map.put("deliveryWarehouseManager", form.getDeliveryContactPerson());
			map.put("arrivalWarehouseCode", form.getArrivalWarehouseCode());
			map.put("arrivalWarehouseManager", form.getArrivalContactPerson());
			map.put("itemCategory", form.getItemCategory());
			map.put("status", form.getStatus());
			map.put("taxType", form.getTaxType());
			map.put("customsDeliveryWarehouseCode", form.getCustomsDeliveryWarehouseCode());
			map.put("customsArrivalWarehouseCode", form.getCustomsArrivalWarehouseCode());
			map.put("customsArrivalStoreCode", form.getCustomsArrivalStoreCode());
			// this.refreshImMovement(map,form.getImMovementItems());

			System.out.println("findImMovement.OrderTypeCode:" + form.getOrderTypeCode() + " OrderNo:" + form.getOrderNo());
			for (int j = 0; j < form.getImMovementItems().size(); j++) {
				ImMovementItem imMovementItem = (ImMovementItem) form.getImMovementItems().get(j);
				if (imMovementItem.getOriginalDeliveryQuantity() != null)
					totalOriginalDeliveryQuantity = totalOriginalDeliveryQuantity
							+ imMovementItem.getOriginalDeliveryQuantity();
				if (imMovementItem.getDeliveryQuantity() != null)
					totalDeliveryQuantity = totalDeliveryQuantity + imMovementItem.getDeliveryQuantity();
			}

			resultMap.put("totalOriginalDeliveryQuantity", totalOriginalDeliveryQuantity);
			resultMap.put("totalDeliveryQuantity", totalDeliveryQuantity);
			// form.setImMovementItems(null);
			resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			resultMap.put("brandName", buBrandDAO.findById(form.getBrandCode()).getBrandName());
			resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
			resultMap.put("packedByName", StringUtils.hasText(form.getPackedBy()) ? UserUtils.getUsernameByEmployeeCode(form
					.getPackedBy()) : "");
			resultMap.put("comfirmedByName", StringUtils.hasText(form.getComfirmedBy()) ? UserUtils
					.getUsernameByEmployeeCode(form.getComfirmedBy()) : "");
			resultMap.put("receiptedByName", StringUtils.hasText(form.getReceiptedBy()) ? UserUtils
					.getUsernameByEmployeeCode(form.getReceiptedBy()) : "");
			resultMap.put("allowMinusStock", imDeliveryWarehouse == null ? "N" : imDeliveryWarehouse.getAllowMinusStock());
			resultMap.put("form", form);

			if (msgBox.getMessage() != null && msgBox.getMessage().length() > 0) // 如果有錯誤訊息，才送到前端 by Weichun 2010/10/19
				resultMap.put("vatMessage", msgBox);
			return form;
		} else {
			throw new NoSuchDataException("查無此單號(" + formId + ")，於按下「確認」鍵後，將關閉本視窗！");

		}

	}

	/**
	 * 取得CC開窗URL字串
	 *
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getReportConfig(Map parameterMap) throws Exception {
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");
			String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			String reportFunctionCode = (String) PropertyUtils.getProperty(otherBean, "reportFunctionCode");
			String orderNo = (String) PropertyUtils.getProperty(otherBean, "orderNo");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String reportUrl = new String("");
			Map returnMap = new HashMap(0);
			// CC後面要代的參數使用parameters傳遞
			Map parameters = new HashMap(0);
			if (brandCode.indexOf("T2") > -1) { // 含有『T2』的品牌 by Weichun 2010.12.31
				if ("T2_W00IM0163".equals(reportFunctionCode) || "T2A_W00IM0163".equals(reportFunctionCode)) {
					parameters.put("prompt0", orderTypeCode);
					parameters.put("prompt1", orderNo);
					parameters.put("prompt2", orderNo);
					parameters.put("prompt3", "");
				} else {
					parameters.put("prompt0", brandCode);
					parameters.put("prompt1", orderTypeCode);
					parameters.put("prompt2", "");
					parameters.put("prompt3", "");
					parameters.put("prompt4", orderNo);
					parameters.put("prompt5", orderNo);
				}
			} else {
				parameters.put("prompt0", brandCode);
				parameters.put("prompt1", orderTypeCode);
				parameters.put("prompt2", orderNo);
				parameters.put("prompt3", orderNo);

			}
			if (StringUtils.hasText(reportFunctionCode)){
				reportUrl = SystemConfig.getReportURLByFunctionCode(brandCode, reportFunctionCode, loginEmployeeCode,parameters);
			}else{
				reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, parameters);
			}
			System.out.println("reportUrl" + " :::: " + reportUrl);
			returnMap.put("reportUrl", reportUrl);
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (IllegalAccessException iae) {
			System.out.println(iae.getMessage());
			throw new IllegalAccessException(iae.getMessage());
		} catch (InvocationTargetException ite) {
			System.out.println(ite.getMessage());
			throw new InvocationTargetException(ite, ite.getMessage());
		} catch (NoSuchMethodException nse) {
			System.out.println(nse.getMessage());
			throw new NoSuchMethodException("NoSuchMethodException:" + nse.getMessage());
		}
	}

	public List<ImWarehouse> getWarehouseInfo(String brandCode, String employeeCode) throws Exception {
		HashMap findObjs = new HashMap();
		findObjs.put("brandCode", brandCode);
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
		findObjs.put("employeeCode", employeeCode);
		if (null != employeeCode) {
			return imWarehouseDAO.findWarehouseBelong2Employee(employeeCode, findObjs);

		} else {
			return imWarehouseDAO.find(findObjs);
			// return imWarehouseDAO.findMovementWarehouse("arrival",findObjs);
		}
	}

	public List<ImWarehouse> getWarehouseInfo(String type, String orderTypeCode, String brandCode, String employeeCode,
			String itemCategoryMode) throws Exception {
		HashMap findObjs = new HashMap();
		findObjs.put("brandCode", brandCode);
		findObjs.put("warehouseCode", "");
		findObjs.put("orderTypeCode", orderTypeCode);
		findObjs.put("enable", "Y");
		findObjs.put("employeeCode", employeeCode);
		findObjs.put("itemCategoryMode", itemCategoryMode);
		return imWarehouseDAO.findMovementWarehouse(type, findObjs);

	}

	public boolean isThreePointMoving(String brandCode, String orderTypeCode) {
		log.info("isThreePointMoving...");
		// Arrays.sort(threePointArray);
		BuOrderType orderType = buOrderTypeDAO.findById(new BuOrderTypeId(brandCode, orderTypeCode));
		if (null != orderType)
			return "THREE".equalsIgnoreCase(orderType.getMoveFlowType());
		else
			return false;

	}

	public boolean isSigningOrderType(String orderTypeCode) {
		Arrays.sort(signingArray);
		return Arrays.binarySearch(signingArray, orderTypeCode) < 0 ? false : true;

	}
/*
	public ImMovementHead updateAJAXMovement(Map parameterMap) throws FormException, Exception {
		System.out.println("4.updateAJAXMovement");
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");

			// 取得欲更新的bean
			ImMovementHead MovementPO = getActualMovement(formLinkBean);
			System.out.println("======Start copyJSONBeantoPojoBean=========");
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, MovementPO);
			System.out.println("======Finish copyJSONBeantoPojoBean=========");
			String beforeStatus = MovementPO.getStatus();
			System.out.println("beforeStatus=" + beforeStatus);
			update(MovementPO);
			return MovementPO;
		} catch (FormException fe) {
			fe.printStackTrace();
			log.error("調撥單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.toString());

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("調撥單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception(ex.toString());
		}
	}
*/
	
	
	
	
	public HashMap updateAJAXMovement(Map parameterMap) throws FormException, Exception {
		System.out.println("4.updateAJAXMovement");
		List errorMsgs = new ArrayList(0);

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");

			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			Long processId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "processId"));

			// 取得欲更新的bean
			ImMovementHead MovementPO = getActualMovement(formLinkBean);
			String identification = MessageStatus.getIdentification(MovementPO.getBrandCode(),
					MovementPO.getOrderTypeCode(), MovementPO.getOrderNo());

			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, MovementPO);
			String beforeStatus = MovementPO.getStatus();
			String formStatus = getStatus(formAction, MovementPO.getHeadId(), processId, 
					MovementPO.getStatus(), "",MovementPO.getOrderTypeCode(), MovementPO.getOrderNo());
			MovementPO.setStatus(formStatus);
			MovementPO.setLastUpdatedBy(employeeCode);
			this.setOriginalOrder(MovementPO, identification, errorMsgs);
			
			String resultMsg = this.saveAjaxData(MovementPO, formAction, identification, errorMsgs, beforeStatus);
			resultMap.put("orderNo", MovementPO.getOrderNo());
			resultMap.put("status", formStatus);
			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", MovementPO);

		} catch (FormException fe) {
			fe.printStackTrace();
			log.error("調撥單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.toString());

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("調撥單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception(ex.toString());
		}

		resultMap.put("vatMessage", msgBox);
		return resultMap;
	}
	
	public HashMap updateAJAXMovementParcial(Map parameterMap) throws FormException, Exception {
		System.out.println("4.updateAJAXMovement");
		List errorMsgs = new ArrayList(0);

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			
			// 取得欲更新的bean
			ImMovementHead MovementPO = getActualMovement(formLinkBean);

			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, MovementPO);
			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			MovementPO.setLastUpdatedBy(employeeCode);
			MovementPO.setLastUpdateDate(new Date());
			imMovementHeadDAO.update(MovementPO);
			resultMap.put("entityBean", MovementPO);

		} catch (FormException fe) {
			fe.printStackTrace();
			log.error("調撥單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.toString());

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("調撥單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception(ex.toString());
		}

		resultMap.put("vatMessage", msgBox);
		return resultMap;
	}
	
	public HashMap updateAJAXMovementOnHand(Map parameterMap) throws FormException, Exception {
		System.out.println("4.updateAJAXMovementOnHand");
		List errorMsgs = new ArrayList(0);

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		try {
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");

			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			Long processId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "processId"));

			// 取得欲更新的bean
			ImMovementHead MovementPO = getActualMovement(formLinkBean);
			String identification = MessageStatus.getIdentification(MovementPO.getBrandCode(),
					MovementPO.getOrderTypeCode(), MovementPO.getOrderNo());
		
			String beforeStatus = MovementPO.getStatus();
			System.out.println("beforeStatus=" + beforeStatus);

			String formStatus = getStatus(formAction, MovementPO.getHeadId(), processId, MovementPO.getStatus(), "",
					MovementPO.getOrderTypeCode(), MovementPO.getOrderNo());
			MovementPO.setStatus(formStatus);

			MovementPO.setLastUpdatedBy(employeeCode);
			this.setOriginalOrder(MovementPO, identification, errorMsgs);
			String resultMsg = this.saveAjaxData(MovementPO, formAction, identification, errorMsgs, beforeStatus);
			log.info("update On Hand...OK ");

			//log.info("資料儲存完畢...");
			resultMap.put("orderNo", MovementPO.getOrderNo());
			//log.info("放入單號...");
			resultMap.put("status", formStatus);
			//log.info("放入單據狀態...");
			resultMap.put("resultMsg", resultMsg);
			//log.info("放入執行結果...");
			resultMap.put("entityBean", MovementPO);
			log.info("放入entityBean...");

		} catch (FormException fe) {
			fe.printStackTrace();
			log.error("調撥單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.toString());

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("調撥單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception(ex.toString());
		}

		resultMap.put("vatMessage", msgBox);

		return resultMap;
	}
	
	private ImMovementHead getActualMovement(Object bean) throws FormException, Exception {
		log.info("5.1 getActualMovement");
		ImMovementHead imMovementHead = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("getActualMovement headId=" + id);

		if (StringUtils.hasText(id)) {
			Long headId = NumberUtils.getLong(id);
			imMovementHead = findById(headId);
			if (imMovementHead == null) {
				throw new NoSuchObjectException("查無促銷單主鍵：" + headId + "的資料！");
			}
			log.info("order_no:" + imMovementHead.getOrderNo());
		} else {
			throw new ValidationErrorException("傳入的促銷單主鍵為空值！");
		}

		return imMovementHead;
	}

	public String saveAjaxData(ImMovementHead modifyObj, String formAction, String identification, List errorMsgs,
			String beforeStatus) throws Exception {
		log.info("5.3 saveAjaxData");
		/*移倉變數宣告 Start*/
		List lst_STORET1;
		List lst_STORET2;
		String RefBillNo = "";
		String exportStore = "";
		String arrStore = "";
		String arg1 = "";
		/*移倉變數宣告 End*/
		String returnResult = new String("");
		try {
			siProgramLogAction.deleteProgramLog("IM_MOVEMENT", null, identification);
			if (null != modifyObj) {
				BuCommonPhraseLine orderTypeSetting = buCommonPhraseLineDAO.findById("ImMovementOrderType", 
						modifyObj.getBrandCode() + modifyObj.getOrderTypeCode());
				if (orderTypeSetting == null)
					throw new ValidationErrorException("查無單別資料-CommonPhraase(" + modifyObj.getOrderTypeCode() + ")，請通知系統管理員");

				if (OrderStatus.FORM_SUBMIT.equals(formAction) || FORM_SUBMIT_BG.equals(formAction)) {
					if (OrderStatus.FORM_SUBMIT.equals(formAction))
						checkImMovement(modifyObj, identification, errorMsgs, orderTypeSetting, beforeStatus);
					modifyObj.setLastUpdateDate(new Date());

					// 執行庫存異動
					log.info("beforeStatus:" + beforeStatus + " formStatus:" + modifyObj.getStatus());
					if (errorMsgs.size() == 0) {
						if ((OrderStatus.SIGNING.equalsIgnoreCase(modifyObj.getStatus()) && (OrderStatus.UNCONFIRMED.equalsIgnoreCase(beforeStatus)
								|| OrderStatus.SAVE.equalsIgnoreCase(beforeStatus) || OrderStatus.REJECT.equalsIgnoreCase(beforeStatus)))
								|| (OrderStatus.WAIT_IN.equalsIgnoreCase(modifyObj.getStatus()))
								|| (OrderStatus.FINISH.equalsIgnoreCase(modifyObj.getStatus()))) {
							log.info("start update On Hand... head_id = " + modifyObj.getHeadId());
							updateMoveOnHand(modifyObj, false, identification, errorMsgs, 
									StringUtils.hasText(orderTypeSetting.getAttribute5()) ? orderTypeSetting.getAttribute5() : "NORMAL", false);
							
							//for 儲位用
				    		if(imStorageAction.isStorageExecute(modifyObj)){
				    			//異動儲位庫存，目前只能送出增加庫存，若為駁回isReject應為true
				    			imStorageService.updateStorageOnHandBySource(modifyObj, modifyObj.getStatus(), null, null, false);
				    		}
						}
						
						sortMovementItem(modifyObj);
						update(modifyObj);
						returnResult = modifyObj.getOrderTypeCode() + "-" + modifyObj.getOrderNo() + "存檔成功";
						
						//insertToTradeVan(modifyObj);
						
						/*
						//===================送移倉資料至關貿=========================
						if(modifyObj.getOrderTypeCode().equals("MDF")){
						System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUUUUU");
						lst_STORET1 = new ArrayList(11);
						lst_STORET2 = new ArrayList(15);
						Map map_items = new HashMap();
						
						IslandExportDAO islandExportDAO = (IslandExportDAO) SpringUtils
					    .getApplicationContext().getBean("islandExportDAO");
						 String ifInsertStore;
						//islandExportDAO.INSERT_STORE01T1(new ArrayList(0));
						ifInsertStore = islandExportDAO.checkIfInsert(modifyObj.getOrderTypeCode(),modifyObj.getOrderNo());
						arg1 = modifyObj.getOrderTypeCode().substring(1);
						RefBillNo = arg1+modifyObj.getOrderNo().substring(3);
						exportStore = islandExportDAO.getStoreNo(modifyObj.getDeliveryWarehouseCode());
						arrStore = islandExportDAO.getStoreNo_ARR(modifyObj.getArrivalWarehouseCode());
						
						if(ifInsertStore.equals("0")){
						  if(modifyObj.getStatus().equals("FINISH")){
							    
								System.out.println("RefBillNo:"+RefBillNo);
								lst_STORET1.add(0, "11");
								lst_STORET1.add(1, "2");
								lst_STORET1.add(2, null);
								lst_STORET1.add(3, "9");
								lst_STORET1.add(4, "AD910");
								lst_STORET1.add(5, "2");
								lst_STORET1.add(6, "R");
								lst_STORET1.add(7, RefBillNo);
								lst_STORET1.add(8, exportStore);
								lst_STORET1.add(9, arrStore);
								lst_STORET1.add(10, "T-BGCG084");
								islandExportDAO.INSERT_STORE01T1(lst_STORET1,map_items);
						  }else if(modifyObj.getStatus().equals("VOID")){
							  throw new Exception ("無法作廢資料 , 原因:此調撥/調整單號查無任何移倉資料!!");
						  }
						}else{
							
							String sumOfMsgFun = islandExportDAO.getSumMsgFun(arg1+RefBillNo);
							if(sumOfMsgFun.equals("0")){
								if(modifyObj.getStatus().equals("FINISH")){
									
									System.out.println("RefBillNo:"+RefBillNo);
									lst_STORET1.add(0, "11");
									lst_STORET1.add(1, "2");
									lst_STORET1.add(2, null);
									lst_STORET1.add(3, "9");
									lst_STORET1.add(4, exportStore);
									lst_STORET1.add(5, "2");
									lst_STORET1.add(6, "R");
									lst_STORET1.add(7, RefBillNo);
									lst_STORET1.add(8, "1");
									lst_STORET1.add(9, "1");
									lst_STORET1.add(10, "T-BGCG084");
									islandExportDAO.INSERT_STORE01T1(lst_STORET1,map_items);
								}else if(modifyObj.getStatus().equals("VOID")){
									  throw new Exception ("無法作廢資料 , 原因:此調撥/調整單號查無任何移倉資料!!");
								}
							}else if(sumOfMsgFun.equals("1")){
								if(modifyObj.getStatus().equals("FINISH")){
									throw new Exception ("此調撥單號已新增過移倉資料!!");
								}else if(modifyObj.getStatus().equals("VOID")){
									
									System.out.println("RefBillNo:"+RefBillNo);
									lst_STORET1.add(0, "11");
									lst_STORET1.add(1, "2");
									lst_STORET1.add(2, null);
									lst_STORET1.add(3, "9");
									lst_STORET1.add(4, exportStore);
									lst_STORET1.add(5, "2");
									lst_STORET1.add(6, "R");
									lst_STORET1.add(7, RefBillNo);
									lst_STORET1.add(8, "1");
									lst_STORET1.add(9, "1");
									lst_STORET1.add(10, "T-BGCG084");
									islandExportDAO.INSERT_STORE01T1(lst_STORET1,map_items);  
								}else{
									throw new Exception ("資料異常無法傳送移倉資料至離島免稅系統!!");
								}
							}
						}
						}     
						     // ifInsertStore = imOnHandDAO.checkIfInsert(imMovementHead.getOrderTypeCode(),cmMovementHead.getOrderNo());
						//=========================================================
						*/
						
					}

				} else {
					System.out.println("start update movement");
					sortMovementItem(modifyObj);
					update(modifyObj);
					System.out.println("update movement success");
					returnResult = modifyObj.getOrderTypeCode() + "-" + modifyObj.getOrderNo() + "存檔成功";
				}
			} else {
				messageHandle(MessageStatus.LOG_ERROR, identification, "調撥單存檔時發生錯誤，原因：存檔物件為空值", "", errorMsgs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			messageHandle(MessageStatus.LOG_ERROR, identification, ex.getMessage(), modifyObj.getLastUpdatedBy(), errorMsgs);
			//throw ex;
		}
		log.info("errorMsgs.size():" + errorMsgs.size());
		if (errorMsgs.size() > 0) {
			log.info("error test log =====> 1");
			throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		} else {
			log.info("error test log =====> 2");
			removeDeleteMarkLineForItem(modifyObj);
		}
		log.info("回傳結果...");
		return returnResult;

	}
	
	private void insertToTradeVan(ImMovementHead modifyObj)throws FormException, Exception{
    	
		//===================送移倉資料至關貿=========================
		
		/*移倉變數宣告 Start*/
		List lst_STORET1;
		int i = 0;
		String RefBillNo = "";
		String exportStore = "";
		String arrStore = "";
		String arg1 = "";
		String orderType = "";
		String orderNo = "";
		/*移倉變數宣告 End*/
		//MACO 2016.01.30 superviseCode
		String superviseCodeT2=buCommonPhraseLineDAO.findById("SuperviseCode","T2Warehouse").getName();
		String superviseCodeMS=buCommonPhraseLineDAO.findById("SuperviseCode","MSWarehouse").getName();

		
		if(modifyObj.getOrderTypeCode().equals("MDF")){
		System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUUUUU");
		lst_STORET1 = new ArrayList(13);
		
		Map map_items = new HashMap();
		
		IslandExportDAO islandExportDAO = (IslandExportDAO) SpringUtils
	    .getApplicationContext().getBean("islandExportDAO");
		 String ifInsertStore;
		//islandExportDAO.INSERT_STORE01T1(new ArrayList(0));
		
		arg1 = modifyObj.getOrderTypeCode().substring(1);
		
		orderType = modifyObj.getOrderTypeCode();
		orderNo = modifyObj.getOrderNo();
		RefBillNo = orderType.substring(1,3)+orderNo;
		ifInsertStore = islandExportDAO.checkIfInsert(RefBillNo);
		exportStore = islandExportDAO.getStoreNo(modifyObj.getDeliveryWarehouseCode());
		arrStore = islandExportDAO.getStoreNo_ARR(modifyObj.getArrivalWarehouseCode());
		
		if(ifInsertStore.equals("0")){
		  if(modifyObj.getStatus().equals("FINISH")){
			    
			  lst_STORET1.add(0, "11");
				lst_STORET1.add(1, "2");
				lst_STORET1.add(2, "");
				lst_STORET1.add(3, "9");
				lst_STORET1.add(4, superviseCodeMS);
				lst_STORET1.add(5, "2");
				lst_STORET1.add(6, "R");
				lst_STORET1.add(7, RefBillNo);
				System.out.println("8888888888888888:"+RefBillNo);
				lst_STORET1.add(8, exportStore);
				lst_STORET1.add(9, arrStore);
				lst_STORET1.add(10, "T-BG"+superviseCodeT2);
				lst_STORET1.add(11, orderType);
				lst_STORET1.add(12, orderNo); 
				//lst_STORET1.add(13, "0"); 
				List<ImMovementItem> imt = modifyObj.getImMovementItems();
				System.out.println("item.size:"+imt.size());
				for (ImMovementItem item : imt){
					System.out.println("babu0:"+item.getOriginalDeclarationNo()+"::::"+item.getOriginalDeclarationSeq());
				   CmDeclarationItem cmDeclarationItem = (CmDeclarationItem)cmDeclarationItemDAO.findOneCmDeclarationItem(item.getOriginalDeclarationNo(), item.getOriginalDeclarationSeq());
				    	
				   List lst_STORET2 = new ArrayList(15);		
				   lst_STORET2.add(0, "1");
				   System.out.println("babu333333333:"+lst_STORET2.size());
				   lst_STORET2.add(1, "1");
				   lst_STORET2.add(2, item.getItemCode());
				   System.out.println("test1:"+item.getDeliveryQuantity());
				   System.out.println("babu11111111111:"+lst_STORET2.size());
				   lst_STORET2.add(3, item.getDeliveryQuantity());
				   System.out.println("babu2222222222:"+item.getDeliveryQuantity());
				   lst_STORET2.add(4, cmDeclarationItem.getUnit());
				   System.out.println("babu4444444444:"+cmDeclarationItem.getUnit());
				   lst_STORET2.add(5, exportStore);
				   System.out.println("babu55555555:"+exportStore);
				   String date = modifyObj.getDeliveryDate().toString();
				   System.out.println("babu5.5.5:"+date);
				   date = date.substring(0, 10);
				   System.out.println("babu6666666666666:"+date);
				   String dafter[] = date.split("-");
				   String afterS = dafter[0]+dafter[1]+dafter[2];
				   System.out.println("afterS:"+afterS);
				   lst_STORET2.add(6, afterS);
				   
				   lst_STORET2.add(7, "R");
				   System.out.println("babu777777777777777:");
				   lst_STORET2.add(8, "1");
				   System.out.println("babu88888888888888888:");
				   lst_STORET2.add(9, RefBillNo);
				   System.out.println("babu99999999999:"+RefBillNo);
				   lst_STORET2.add(10, item.getOriginalDeclarationNo());
				   System.out.println("babu10000000000:"+item.getOriginalDeclarationNo());
				   lst_STORET2.add(11, item.getOriginalDeclarationSeq());
				   System.out.println("babu1111111111:"+item.getOriginalDeclarationNo());
				   lst_STORET2.add(12, "1");
				   
				   lst_STORET2.add(13, cmDeclarationItem.getODeclNo());//cmDeclarationItem.getODeclNo()
				   System.out.println("test13:"+cmDeclarationItem.getODeclNo());
				   lst_STORET2.add(14, cmDeclarationItem.getOItemNo());//cmDeclarationItem.getOItemNo()
				   System.out.println("test14:"+cmDeclarationItem.getOItemNo().intValue());
				   map_items.put(i, lst_STORET2);
				   i++;
				}
				System.out.println("Test");
				islandExportDAO.INSERT_STORE01T1(lst_STORET1,map_items);
				
		  }else if(modifyObj.getStatus().equals("VOID")){
			  throw new Exception ("無法作廢資料 , 原因:此調撥/調整單號查無任何移倉資料!!");
		  }
		}else{
			
			
			String sumOfMsgFun = islandExportDAO.getSumMsgFun(RefBillNo);
			if(sumOfMsgFun.equals("0")){
				if(modifyObj.getStatus().equals("FINISH")){
					
					lst_STORET1.add(0, "11");
					lst_STORET1.add(1, "2");
					lst_STORET1.add(2, "");
					lst_STORET1.add(3, "9");
					lst_STORET1.add(4, superviseCodeMS);
					lst_STORET1.add(5, "2");
					lst_STORET1.add(6, "R");
					lst_STORET1.add(7, RefBillNo);
					System.out.println("8888888888888888:"+RefBillNo);
					lst_STORET1.add(8, exportStore);
					lst_STORET1.add(9, arrStore);
					lst_STORET1.add(10, "T-BG"+superviseCodeT2);
					lst_STORET1.add(11, orderType);
					lst_STORET1.add(12, orderNo); 
					//lst_STORET1.add(13, "0"); 
					List<ImMovementItem> imt = modifyObj.getImMovementItems();
					System.out.println("item.size:"+imt.size());
					for (ImMovementItem item : imt){
						System.out.println("babu0:"+item.getOriginalDeclarationNo()+"::::"+item.getOriginalDeclarationSeq());
					   CmDeclarationItem cmDeclarationItem = (CmDeclarationItem)cmDeclarationItemDAO.findOneCmDeclarationItem(item.getOriginalDeclarationNo(), item.getOriginalDeclarationSeq());
					    	
					   List lst_STORET2 = new ArrayList(15);		
					   lst_STORET2.add(0, "1");
					   lst_STORET2.add(1, "1");
					   lst_STORET2.add(2, item.getItemCode());
					   System.out.println("test1:"+item.getDeliveryQuantity());
					   
					   lst_STORET2.add(3, item.getDeliveryQuantity());
					   
					   lst_STORET2.add(4, cmDeclarationItem.getUnit());
					   
					   lst_STORET2.add(5, exportStore);
					   
					   String date = modifyObj.getDeliveryDate().toString();
					   date = date.substring(0, 10);
					   String dafter[] = date.split("-");
					   String afterS = dafter[0]+dafter[1]+dafter[2];
					   System.out.println("afterS:"+afterS);
					   lst_STORET2.add(6, afterS);
					   
					   lst_STORET2.add(7, "R");
					  
					   lst_STORET2.add(8, "1");
					  
					   lst_STORET2.add(9, RefBillNo);
					  
					   lst_STORET2.add(10, item.getOriginalDeclarationNo());
					   
					   lst_STORET2.add(11, item.getOriginalDeclarationSeq());
					  
					   lst_STORET2.add(12, "1");
					   
					   lst_STORET2.add(13, cmDeclarationItem.getODeclNo());//cmDeclarationItem.getODeclNo()
					   System.out.println("test13:"+cmDeclarationItem.getODeclNo());
					   lst_STORET2.add(14, cmDeclarationItem.getOItemNo());//cmDeclarationItem.getOItemNo()
					   System.out.println("test14:"+cmDeclarationItem.getOItemNo().intValue());
					   map_items.put(i, lst_STORET2);
					   i++;
					}
					System.out.println("Test");
					islandExportDAO.INSERT_STORE01T1(lst_STORET1,map_items);
				}else{
					throw new Exception ("無法作廢資料 , 原因:此調撥/調整單號查無任何移倉資料!!");
				}
			}else if(sumOfMsgFun.equals("1")){
				if(modifyObj.getStatus().equals("FINISH")){
					throw new Exception ("此調撥單號已新增過移倉資料!!");
				}else if(modifyObj.getStatus().equals("VOID")){
					lst_STORET1.add(0, "11");
					lst_STORET1.add(1, "2");
					lst_STORET1.add(2, "");
					lst_STORET1.add(3, "9");
					lst_STORET1.add(4, superviseCodeMS);
					lst_STORET1.add(5, "2");
					lst_STORET1.add(6, "R");
					lst_STORET1.add(7, RefBillNo);
					System.out.println("8888888888888888:"+RefBillNo);
					lst_STORET1.add(8, exportStore);
					lst_STORET1.add(9, arrStore);
					lst_STORET1.add(10, "T-BG"+superviseCodeT2);
					lst_STORET1.add(11, orderType);
					lst_STORET1.add(12, orderNo); 
					//lst_STORET1.add(13, "0"); 
					List<ImMovementItem> imt = modifyObj.getImMovementItems();
					System.out.println("item.size:"+imt.size());
					for (ImMovementItem item : imt){
						System.out.println("babu0:"+item.getOriginalDeclarationNo()+"::::"+item.getOriginalDeclarationSeq());
					   CmDeclarationItem cmDeclarationItem = (CmDeclarationItem)cmDeclarationItemDAO.findOneCmDeclarationItem(item.getOriginalDeclarationNo(), item.getOriginalDeclarationSeq());
					    	
					   List lst_STORET2 = new ArrayList(15);		
					   lst_STORET2.add(0, "1");
					   lst_STORET2.add(1, "1");
					   lst_STORET2.add(2, item.getItemCode());
					   System.out.println("test1:"+item.getDeliveryQuantity());
					   
					   lst_STORET2.add(3, item.getDeliveryQuantity());
					   
					   lst_STORET2.add(4, cmDeclarationItem.getUnit());
					   
					   lst_STORET2.add(5, exportStore);
					   
					   String date = modifyObj.getDeliveryDate().toString();
					   date = date.substring(0, 10);
					   String dafter[] = date.split("-");
					   String afterS = dafter[0]+dafter[1]+dafter[2];
					   System.out.println("afterS:"+afterS);
					   lst_STORET2.add(6, afterS);
					   
					   lst_STORET2.add(7, "R");
					  
					   lst_STORET2.add(8, "1");
					  
					   lst_STORET2.add(9, RefBillNo);
					  
					   lst_STORET2.add(10, item.getOriginalDeclarationNo());
					   
					   lst_STORET2.add(11, item.getOriginalDeclarationSeq());
					  
					   lst_STORET2.add(12, "1");
					   
					   lst_STORET2.add(13, cmDeclarationItem.getODeclNo());//cmDeclarationItem.getODeclNo()
					   System.out.println("test13:"+cmDeclarationItem.getODeclNo());
					   lst_STORET2.add(14, cmDeclarationItem.getOItemNo());//cmDeclarationItem.getOItemNo()
					   System.out.println("test14:"+cmDeclarationItem.getOItemNo().intValue());
					   map_items.put(i, lst_STORET2);
					   i++;
					}
					System.out.println("Test");
					islandExportDAO.INSERT_STORE01T1(lst_STORET1,map_items);
					
				}else{
					throw new Exception ("資料異常無法傳送移倉資料至離島免稅系統!!");
				}
			}
		}
		}     
		     // ifInsertStore = imOnHandDAO.checkIfInsert(imMovementHead.getOrderTypeCode(),cmMovementHead.getOrderNo());
		//=========================================================
    	
    }
	

	public List<Properties> executeSearchInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			Map multiList = new HashMap(0);
			BuCommonPhraseLine orderTypeSetting = buCommonPhraseLineDAO.findById("ImMovementOrderType", loginBrandCode
					+ orderTypeCode);
			if (null != orderTypeSetting) {

				String itemCategoryMode = orderTypeSetting.getAttribute3();
				// List<ImWarehouse> allDeliveryWarehouses =
				// this.getWarehouseInfo(orderTypeCode,loginBrandCode,
				// loginEmployeeCode, itemCategoryMode);
				List<ImWarehouse> allDeliveryWarehouses = this.getWarehouseInfo("delivery", orderTypeCode, loginBrandCode,
						null, itemCategoryMode);
				List<ImWarehouse> allArrivalWarehouses = this.getWarehouseInfo("arrival", orderTypeCode, loginBrandCode,
						null, itemCategoryMode);

				List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(loginBrandCode, "IM");

				multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true,
						false));
				multiList.put("allDeliveryWarehouses", AjaxUtils.produceSelectorData(allDeliveryWarehouses, "warehouseCode",
						"warehouseName", true, true));

				multiList.put("allArrivalWarehouses", AjaxUtils.produceSelectorData(allArrivalWarehouses, "warehouseCode",
						"warehouseName", true, true));

				resultMap.put("itemLimitField", orderTypeSetting != null ? orderTypeSetting.getParameter1() : "");
				resultMap.put("itemLimitOpt", orderTypeSetting != null ? orderTypeSetting.getParameter2() : "");
				resultMap.put("itemLimitValue", orderTypeSetting != null ? orderTypeSetting.getParameter3() : "");
				resultMap.put("checkItemCategory", orderTypeSetting != null ? orderTypeSetting.getParameter4() : "N"); // 收貨時是否可輸入到貨數
				// //是否check業種子類
				resultMap.put("voidMode", orderTypeSetting != null ? orderTypeSetting.getParameter5() : "N"); // 作廢模式
				// O:WAIT_OUT可作廢
				// 作廢模式
				// I:WAIT_IN可作廢
				// B:WAIT_OUT與WAIT_IN可作廢
				resultMap.put("itemCategoryMode", orderTypeSetting != null ? orderTypeSetting.getAttribute3() : "N"); // 是否可需輸入業種子類代號
				resultMap.put("allowMoreQty", orderTypeSetting != null ? orderTypeSetting.getAttribute4() : "N"); // 是否出貨量可大於預計出貨量
				resultMap.put("movementType", orderTypeSetting != null ? orderTypeSetting.getAttribute5() : "NORMAL"); // 調撥單模式
				resultMap.put("showOriginalDeliveryQty", orderTypeSetting != null ? orderTypeSetting.getReserve2() : "N"); // 調撥單模式

				resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());
				resultMap.put("multiList", multiList);
			} else {
				log.error("表單初始化失敗，原因：單別(" + orderTypeCode + ")未設定於片語檔，請通知系統管理人員");
			}
		} catch (Exception ex) {
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "表單初始化失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}

		return AjaxUtils.parseReturnDataToJSON(resultMap);

	}

	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================

			String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
			String startOrderNo = httpRequest.getProperty("startOrderNo");
			String endOrderNo = httpRequest.getProperty("endOrderNo");
			String deliveryWarehouseCode = httpRequest.getProperty("deliveryWarehouseCode");// 出貨庫別
			String startDeliveryDate = httpRequest.getProperty("startDeliveryDate");
			String endDeliveryDate = httpRequest.getProperty("endDeliveryDate");
			String arrivalWarehouseCode = httpRequest.getProperty("arrivalWarehouseCode");// 轉入庫別
			String startArrivalDate = httpRequest.getProperty("startArrivalDate");
			String endArrivalDate = httpRequest.getProperty("endArrivalDate");
			String originalOrderTypeCode = httpRequest.getProperty("originalOrderTypeCode");
			String originalStartOrderNo = httpRequest.getProperty("originalStartOrderNo");
			String originalEndOrderNo = httpRequest.getProperty("originalEndOrderNo");
			String packedBy = httpRequest.getProperty("packedBy");
			String comfirmedBy = httpRequest.getProperty("comfirmedBy");
			String receiptedBy = httpRequest.getProperty("receiptedBy");
			String cmMovement = httpRequest.getProperty("cmMovement");
			String itemCode = httpRequest.getProperty("itemCode");
			String status = httpRequest.getProperty("status");
			String sortKey = httpRequest.getProperty("sortKey");
			String sortSeq = httpRequest.getProperty("sortSeq");
			String cmMovementIsNotNull = httpRequest.getProperty("cmMovementIsNotNull");
			String description = httpRequest.getProperty("description"); // 備註
			String customsWarehouseCode = httpRequest.getProperty("customsWarehouseCode");

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("orderTypeCode", orderTypeCode);
			findObjs.put("startOrderNo", startOrderNo);
			findObjs.put("endOrderNo", endOrderNo);
			findObjs.put("deliveryWarehouseCode", deliveryWarehouseCode);
			findObjs.put("startDeliveryDate", startDeliveryDate);
			findObjs.put("endDeliveryDate", endDeliveryDate);
			findObjs.put("arrivalWarehouseCode", arrivalWarehouseCode);
			findObjs.put("startArrivalDate", startArrivalDate);
			findObjs.put("endArrivalDate", endArrivalDate);
			findObjs.put("status", status);
			findObjs.put("packedBy", packedBy);
			findObjs.put("comfirmedBy", comfirmedBy);
			findObjs.put("receiptedBy", receiptedBy);
			findObjs.put("originalOrderTypeCode", originalOrderTypeCode);
			findObjs.put("originalStartOrderNo", originalStartOrderNo);
			findObjs.put("originalEndOrderNo", originalEndOrderNo);
			findObjs.put("sortKey", sortKey);
			findObjs.put("sortSeq", sortSeq);
			findObjs.put("cmMovement", cmMovement);
			findObjs.put("remark", description); // 備註欄位

			findObjs.put("itemCode", itemCode);
			findObjs.put("cmMovementIsNotNull", cmMovementIsNotNull);
			findObjs.put("customsWarehouseCode", customsWarehouseCode);
			//
			// log.info("deliveryWarehouseCode:"+deliveryWarehouseCode);
			// log.info("arrivalWarehouseCode:"+arrivalWarehouseCode);
			// log.info("startDeliveryDate:"+startDeliveryDate);
			// log.info("endDeliveryDate:"+endDeliveryDate);
			// log.info("startArrivalDate:"+startArrivalDate);
			// log.info("endArrivalDate:"+endArrivalDate);
			// ==============================================================

			List<ImMovementHead> imMovementHeads = (List<ImMovementHead>) imMovementHeadDAO.findPageLine(findObjs, iSPage,
					iPSize, imMovementHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");

			log.info("imMovementHeads.size" + imMovementHeads.size());

			if (imMovementHeads != null && imMovementHeads.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) imMovementHeadDAO.findPageLine(findObjs, -1, iPSize,
						imMovementHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆
				// INDEX

				for (ImMovementHead imMovementHead : imMovementHeads) {
					imMovementHead.setStatus(OrderStatus.getChineseWord(imMovementHead.getStatus()));
					List<ImMovementItem> items = imMovementHead.getImMovementItems();
				}

				log.info("ImMovement.AjaxUtils.getAJAXPageData ");
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,
						imMovementHeads, gridDatas, firstIndex, maxIndex));

			} else {
				log.info("ImMovement.AjaxUtils.getAJAXPageDataDefault ");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));

			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的調撥查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的調撥查詢失敗！");
		}
	}

	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}

	/**
	 * 匯入促銷商品明細
	 *
	 * @param headId
	 * @param promotionItems
	 * @throws Exception
	 */
	public void executeImportMovementItems(Long headId, List movementItems) throws Exception {
		log.info("executeImportMovementItems.." + headId);
		log.info("executeImportMovementItems..size" + movementItems.size());
		List<ImMovementItem> items = new ArrayList(0);
		try {
			// deleteMovementItems(headId);

			ImMovementHead imMovementHead = imMovementHeadDAO.findById(headId);
			if (imMovementHead == null)
				throw new NoSuchObjectException("查無調撥單主鍵：" + headId + "的資料");

			log.info("deleteMovementItems..size" + imMovementHead.getImMovementItems().size());
			if (movementItems != null && movementItems.size() > 0) {
				for (int i = 0; i < movementItems.size(); i++) {
					ImMovementItem imMovementItem = (ImMovementItem) movementItems.get(i);
					imMovementItem.setIndexNo(i + 1L);
					log.info("SET-imMovementItemIndexNo:"+(i + 1L));
					items.add(imMovementItem);
				}
				imMovementHead.setImMovementItems(items);
			} else {
				imMovementHead.setImMovementItems(new ArrayList(0));
			}
			imMovementHeadDAO.update(imMovementHead);
		} catch (NoSuchObjectException ns) {
			log.error("刪除調撥商品明細失敗，原因：" + ns.toString());
			throw new FormException(ns.getMessage());
		} catch (Exception ex) {
			log.error("調撥商品明細匯入時發生錯誤，原因：" + ex.toString());
			throw new Exception("調撥商品明細匯入時發生錯誤，原因：" + ex.getMessage());
		}
	}

	public void deleteMovementItems(Long headId) throws FormException, Exception {

		try {
			ImMovementHead imMovementHead = imMovementHeadDAO.findById(headId);
			if (imMovementHead == null) {
				throw new NoSuchObjectException("查無調撥單主鍵：" + headId + "的資料");
			}
			imMovementHead.setImMovementItems(new ArrayList(0));
			imMovementHeadDAO.update(imMovementHead);
		} catch (FormException fe) {
			log.error("刪除調撥商品明細失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("刪除調撥商品明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("刪除調撥商品明細時發生錯誤，原因：" + ex.getMessage());
		}
	}

	public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception {
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			log.info("getSearchSelection.picker_parameter:" + timeScope + "/" + searchKeys.toString());

			List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
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

	/*
	 * public HashMap findPageLine(HashMap findObjs, int startPage, int
	 * pageSize) { return imMovementHeadDAO.findPageLine(findObjs, startPage,
	 * pageSize); }
	 */
	public List<Properties> updateAllSearchData(Map parameterMap) throws Exception {
		log.info("updateAllSearchData....");
		Map resultMap = new HashMap(0);
		log.info(parameterMap.keySet().toString());
		try {
			Object pickerBean = parameterMap.get("vatBeanPicker");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");
			String timeScope = (String) PropertyUtils.getProperty(otherBean, AjaxUtils.TIME_SCOPE);
			System.out.println(AjaxUtils.TIME_SCOPE + ":" + timeScope);
			String isAllClick = (String) PropertyUtils.getProperty(otherBean, "isAllClick");
			log.info("timeScope:" + timeScope);

			String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");// 品牌代號
			String orderTypeCode = (String) PropertyUtils.getProperty(formBindBean, "orderTypeCode");// 單別
			String startOrderNo = (String) PropertyUtils.getProperty(formBindBean, "startOrderNo");
			String endOrderNo = (String) PropertyUtils.getProperty(formBindBean, "endOrderNo");
			String deliveryWarehouseCode = (String) PropertyUtils.getProperty(formBindBean, "deliveryWarehouseCode");// 出貨庫別
			String startDeliveryDate = (String) PropertyUtils.getProperty(formBindBean, "startDeliveryDate");
			String endDeliveryDate = (String) PropertyUtils.getProperty(formBindBean, "endDeliveryDate");
			String arrivalWarehouseCode = (String) PropertyUtils.getProperty(formBindBean, "arrivalWarehouseCode");// 轉入庫別
			String startArrivalDate = (String) PropertyUtils.getProperty(formBindBean, "startArrivalDate");
			String endArrivalDate = (String) PropertyUtils.getProperty(formBindBean, "endArrivalDate");
			String originalOrderTypeCode = (String) PropertyUtils.getProperty(formBindBean, "originalOrderTypeCode");
			String originalStartOrderNo = (String) PropertyUtils.getProperty(formBindBean, "originalStartOrderNo");
			String originalEndOrderNo = (String) PropertyUtils.getProperty(formBindBean, "originalEndOrderNo");
			String itemCode = (String) PropertyUtils.getProperty(formBindBean, "itemCode");
			String status = (String) PropertyUtils.getProperty(formBindBean, "status");
			log.info("isAllClick:" + isAllClick);
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("orderTypeCode", orderTypeCode);
			findObjs.put("startOrderNo", startOrderNo);
			findObjs.put("endOrderNo", endOrderNo);
			findObjs.put("deliveryWarehouseCode", deliveryWarehouseCode);
			findObjs.put("startDeliveryDate", startDeliveryDate);
			findObjs.put("endDeliveryDate", endDeliveryDate);
			findObjs.put("arrivalWarehouseCode", arrivalWarehouseCode);
			findObjs.put("startArrivalDate", startArrivalDate);
			findObjs.put("endArrivalDate", endArrivalDate);
			findObjs.put("status", status);
			findObjs.put("originalOrderTypeCode", originalOrderTypeCode);
			findObjs.put("originalStartOrderNo", originalStartOrderNo);
			findObjs.put("originalEndOrderNo", originalEndOrderNo);
			findObjs.put("itemCode", itemCode);
			List allDataList = (List) imMovementHeadDAO.findPageLine(findObjs, 0, 100,
					imMovementHeadDAO.QUARY_TYPE_SELECT_ALL).get("form");

			if (allDataList.size() > 0)
				AjaxUtils.updateAllResult(timeScope, isAllClick, allDataList);

			return AjaxUtils.parseReturnDataToJSON(resultMap);
		} catch (IllegalAccessException iae) {
			System.out.println(iae.getMessage());
			throw new IllegalAccessException(iae.getMessage());
		} catch (InvocationTargetException ite) {
			System.out.println(ite.getMessage());
			throw new InvocationTargetException(ite, ite.getMessage());
		} catch (NoSuchMethodException nse) {
			System.out.println(nse.getMessage());
			throw new NoSuchMethodException("NoSuchMethodException:" + nse.getMessage());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw new NoSuchMethodException("Exception:" + ex.getMessage());
		}
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
	public List getExportBarCode(Long headId, String brandCode, String orderTypeCode) throws Exception {
		List<ImMovementItem> re = new ArrayList();
		if (null != headId) {
			List<ImMovementItem> imMovementItems = imMovementHeadDAO.getItemsByOrder(headId);
			BuOrderTypeId buOrderTypeId = new BuOrderTypeId();
			buOrderTypeId.setBrandCode(brandCode);
			buOrderTypeId.setOrderTypeCode(orderTypeCode);
			BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
			String priceType = "1";
			String itemCodes = "";
			List itemCodeList = new ArrayList();
			Map tmpPriceViewMap = new HashMap();
			if (null != buOrderType) {
				priceType = buOrderType.getPriceType();
			}
			for (ImMovementItem imMovementItem : imMovementItems) {
				itemCodeList.add(imMovementItem.getItemCode());
			}
			try {
				itemCodes = StringTools.parseSQLinStatement(itemCodeList);
			} catch (Exception e) {
				System.out.println("-----" + e.getMessage());
			}
			List<ImItemCurrentPriceView> imItemCurrentPriceViews = imItemCurrentPriceViewDAO.findCurrentPriceList(brandCode,
					itemCodes, priceType);
			for (ImItemCurrentPriceView imItemCurrentPriceView : imItemCurrentPriceViews) {
				tmpPriceViewMap.put(imItemCurrentPriceView.getItemCode(), imItemCurrentPriceView);
			}
			for (ImMovementItem imMovementItem : imMovementItems) {
				String itemCode = imMovementItem.getItemCode();
				ImItemCurrentPriceView imPriceView = (ImItemCurrentPriceView) tmpPriceViewMap.get(itemCode);
				if(null != imPriceView){
					imMovementItem.setReserve1(imPriceView.getItemCName());
					if(null != imPriceView.getCategory14()){
						BuCountry buCountry = buCountryDAO.findById(imPriceView.getCategory14());
						if(null != buCountry)
							imMovementItem.setReserve2(buCountry.getCountryCName());
					}
					imMovementItem.setReserve3(imPriceView.getCategory08());
				}
    			//設置日期(商品最近進貨日期)
    			Date orderDate = imReceiveHeadDAO.findOrderDateByItemCode(brandCode, itemCode );
    			if(null != orderDate){
    				Calendar cal = Calendar.getInstance();
    				cal.setTime(orderDate);
    				if("T1CO".equals(brandCode))
    					cal.add(Calendar.MONTH, -1);
    				imMovementItem.setReserve4(cal.get(Calendar.YEAR) + "." + String.format("%02d", cal.get(Calendar.MONTH)));
    			}

				imMovementItem.setUnitPrice(imPriceView.getUnitPrice());
				DecimalFormat df1 = new DecimalFormat( "#,###,###,###,##0" );
				imMovementItem.setUnitPrice_format(df1.format(imPriceView.getUnitPrice()));
				imMovementItem.setDeliveryQuantity_format(df1.format(imMovementItem.getDeliveryQuantity()));
				re.add(imMovementItem);
			}
		}
		return re;
	}

	public void setMoveTypeArray(List<BuOrderType> allOrderTypes) {
		log.info("setMoveTypeArray...");
		for (BuOrderType buOrderType : allOrderTypes) {
			if (MOVE_THREE_TYPE.equals(buOrderType.getMoveFlowType())) {
				threePointArray = ArraysUtils.append(threePointArray, buOrderType.getId().getOrderTypeCode());
				// System.out.println("threePointArray:"+buOrderType.getId().getOrderTypeCode());
			}
			if (MOVE_SIGN_TYPE.equals(buOrderType.getMoveSignType())) {
				signingArray = ArraysUtils.append(signingArray, buOrderType.getId().getOrderTypeCode());
				// System.out.println("signingArray:"+buOrderType.getId().getOrderTypeCode());
			}
		}
		// System.out.println("threePointArray:"+threePointArray.length);
		// System.out.println("signingArray:"+signingArray.length);

	}

	/**
	 * 取得訊息提示用
	 *
	 * @param headId
	 * @return
	 * @throws Exception
	 */
	public String getIdentification(Long headId) throws Exception {

		String id = null;
		try {
			ImMovementHead imMovementHead = (ImMovementHead) imMovementHeadDAO.findById(headId);
			if (imMovementHead != null) {
				id = MessageStatus.getIdentification(imMovementHead.getBrandCode(), imMovementHead.getOrderTypeCode(),
						imMovementHead.getOrderNo());
			} else {
				throw new NoSuchDataException("調撥單主檔查無主鍵：" + headId + "的資料！");
			}

			return id;
		} catch (Exception ex) {
			log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 取單號後更新更新主檔
	 *
	 * @param parameterMap
	 * @return Map
	 * @throws FormException
	 * @throws Exception
	 */
	public Map updateImMovementWithActualOrderNO(Map parameterMap) throws FormException, Exception {

		Map resultMap = new HashMap();
		try {

			resultMap = this.updateImMovementBean(parameterMap);
			ImMovementHead imMovementHead = (ImMovementHead) resultMap.get("entityBean");

			// 刪除於SI_PROGRAM_LOG的原識別碼資料
			String identification = MessageStatus.getIdentification(imMovementHead.getBrandCode(), imMovementHead
					.getOrderTypeCode(), imMovementHead.getOrderNo());
			siProgramLogAction.deleteProgramLog("IM_MOVEMENT", null, identification);

			this.setOrderNo(imMovementHead);
			String resultMsg = imMovementHead.getOrderTypeCode() + imMovementHead.getOrderNo() + "存檔成功！是否繼續新增？";
			resultMap.put("resultMsg", resultMsg);

			return resultMap;
		} catch (FormException fe) {
			log.error("調撥單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("調撥單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("調整單存檔時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 前端資料塞入bean
	 *
	 * @param parameterMap
	 * @return
	 */
	public Map updateImMovementBean(Map parameterMap) throws FormException, Exception {
		Map resultMap = new HashMap();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			// 取得欲更新的bean
			// ImAdjustmentHead imAdjustmentHead =
			// this.getActualImAdjustment(formLinkBean);
			ImMovementHead MovementPO = getActualMovement(formLinkBean);
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, MovementPO);
			resultMap.put("entityBean", MovementPO);
			return resultMap;
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("調整單資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}

	/** 檢核 HeadId 是否有值 */
	public Long getImMovementHeadId(Object bean) throws FormException, Exception {
		Long headId = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("headId=" + id);
		if (StringUtils.hasText(id)) {
			headId = NumberUtils.getLong(id);
		} else {
			throw new ValidationErrorException("傳入的調撥單主鍵為空值！");
		}
		return headId;
	}

	/**
	 * 檢核 ImReceive 資料
	 *
	 * @param imReceiveHead
	 * @param conditionMap
	 * @return List
	 * @throws ValidationErrorException
	 */
	public List updateCheckedImMovementData(Map parameterMap) throws Exception {
		log.info("updateCheckedImMovementData");
		List errorMsgs = new ArrayList(0);

		try {
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			// String orderTypeTaxCode =
			// (String)PropertyUtils.getProperty(otherBean, "orderTypeTaxCode");
			// String typeCode = (String)PropertyUtils.getProperty(otherBean,
			// "typeCode");

			ImMovementHead MovementPO = getActualMovement(formLinkBean);

			BuBrand buBrand = buBrandDAO.findById(MovementPO.getBrandCode());
			String branchCode = buBrand.getBuBranch().getBranchCode(); // 是否 T2

			String identification = MessageStatus.getIdentification(MovementPO.getBrandCode(),
					MovementPO.getOrderTypeCode(), MovementPO.getOrderNo());

			// clear 原有 ERROR RECORD
			siProgramLogAction.deleteProgramLog("IM_MOVEMENT", null, identification);
			// check imReceiveHead Data
			// checkImMovement( MovementPO, identification, errorMsgs);
			BuCommonPhraseLine orderTypeSetting = buCommonPhraseLineDAO.findById("ImMovementOrderType", MovementPO
					.getBrandCode()
					+ MovementPO.getOrderTypeCode());
			if (orderTypeSetting == null)
				throw new ValidationErrorException("查無單別資料-CommonPhraase(" + MovementPO.getOrderTypeCode() + ")，請通知系統管理員");

			checkImMovement(MovementPO, identification, errorMsgs, orderTypeSetting, MovementPO.getStatus());

			return errorMsgs;
		} catch (Exception ex) {
			log.error("調撥單檢核後存檔失敗，原因：" + ex.toString());
			throw new Exception("調撥單檢核後存檔失敗，原因：" + ex.getMessage());
		}
	}

	public void checkImMovement(ImMovementHead checkObj, String identification, List errorMsgs,
			BuCommonPhraseLine orderTypeSetting, String beforeStatus) throws Exception {
		log.info("5.3.1 checkImMovement...New");
		ArrayList removeArray = new ArrayList(0);
		boolean isThreePointMoving = this.isThreePointMoving(checkObj.getBrandCode(), checkObj.getOrderTypeCode());
		String message = null;
		String user = checkObj.getLastUpdatedBy();
		String waitingWarehouseCode = new String("");
		Double itemCount = new Double(0);
		Double maxBoxCount = new Double(0);
		Double nowBoxCount = new Double(0);
		String itemCountField = new String("");
		Boolean disallowMinusStock = new Boolean(true);
		try {
			List<ImMovementItem> imMovementItems = checkObj.getImMovementItems();
			log.info("5.3.1.1 check brand....");
			BuBrand buBrand = buBrandDAO.findById(checkObj.getBrandCode());
			if (buBrand == null)
				this.messageHandle(MessageStatus.LOG_ERROR, identification, "品牌代號(" + checkObj.getBrandCode()
						+ ")錯誤，請通知系統管理員", user, errorMsgs);
			else {
				waitingWarehouseCode = buBrand.getDefaultWarehouseCode1() == null ? "" : buBrand.getDefaultWarehouseCode1();
			}
			log.info("5.3.1.2 check orderType....");
			BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(checkObj.getBrandCode(), checkObj
					.getOrderTypeCode()));
			if (buOrderType == null)
				this.messageHandle(MessageStatus.LOG_ERROR, identification, "查無單別資料-OrderType("
						+ checkObj.getOrderTypeCode() + ")，請通知系統管理員", user, errorMsgs);

			log.info("5.3.1.4 check item category....");
			if ("Y".equalsIgnoreCase(orderTypeSetting.getAttribute3())) {
				// System.out.println(checkObj.getItemCategory());
				if (!StringUtils.hasText(checkObj.getItemCategory()))
					this.messageHandle(MessageStatus.LOG_ERROR, identification, "此單別需選擇業種", user, errorMsgs);
			}

			log.info("5.3.1.5 check items size....");
			if (imMovementItems == null || imMovementItems.size() == 0)
				this.messageHandle(MessageStatus.LOG_ERROR, identification, "尚未輸入明細資料，請重新確認", user, errorMsgs);

			log.info("5.3.1.6 check date....");
			if (OrderStatus.WAIT_IN.equals(checkObj.getStatus()) || OrderStatus.SIGNING.equals(checkObj.getStatus())) {
				System.out.println("        check delivery close date....");
				ValidateUtil.isAfterClose(checkObj.getBrandCode(), checkObj.getOrderTypeCode(), "轉出日期", checkObj
						.getDeliveryDate(),checkObj.getSchedule());
			} else if (OrderStatus.FINISH.equals(checkObj.getStatus())) {
				System.out.println("        check arrival close date....");
				if (checkObj.getArrivalDate() == null)
					this.messageHandle(MessageStatus.LOG_ERROR, identification, "收貨日期不可為空白", user, errorMsgs);
				// else
				// ValidateUtil.isAfterClose(checkObj.getBrandCode(),
				// checkObj.getOrderTypeCode(), "收貨日期",
				// checkObj.getArrivalDate());

				String deliveryDateString = DateUtils.format(checkObj.getDeliveryDate(), "yyyyMMdd");
				String arrivalDateString = DateUtils.format(checkObj.getArrivalDate(), "yyyyMMdd");
				log.info("        check arrival date is after delivery date...." + deliveryDateString + "/"
						+ arrivalDateString);

				if (arrivalDateString.compareTo(deliveryDateString) < 0)
					this.messageHandle(MessageStatus.LOG_ERROR, identification, "收貨日期(" + arrivalDateString + ")不可小於轉出日期("
							+ deliveryDateString + ")", user, errorMsgs);
			}
			log.info("5.3.1.7 check delivery warehouse....");
			// ----------轉入倉管人員送出時，不再檢查倉庫權限----------------------
			if (OrderStatus.SAVE.equals(beforeStatus) || OrderStatus.WAIT_OUT.equals(beforeStatus)
					|| OrderStatus.UNCONFIRMED.equals(beforeStatus) || OrderStatus.REJECT.equals(beforeStatus)
			// || OrderStatus.WAIT_IN.equals(
			// checkObj.getStatus())||OrderStatus.SIGNING.equals(
			// checkObj.getStatus())
			) {
				ImWarehouse deliveryWarehouse = this.isCurrectDeliveryWarehouse(checkObj.getBrandCode(), checkObj
						.getOrderTypeCode(), checkObj.getDeliveryWarehouseCode(), checkObj.getLastUpdatedBy());

				if (null == deliveryWarehouse)
					this.messageHandle(MessageStatus.LOG_ERROR, identification, "轉出倉別代號("
							+ checkObj.getDeliveryWarehouseCode() + ")錯誤或調渡人員(" + checkObj.getLastUpdatedBy() + ")無此倉庫權限",
							user, errorMsgs);
				else {
					checkObj.setCustomsDeliveryWarehouseCode(deliveryWarehouse.getCustomsWarehouseCode());
					disallowMinusStock = "Y".equals(deliveryWarehouse.getAllowMinusStock()) ? false : true;

				}
			} else {
				ImWarehouse deliveryWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(checkObj.getBrandCode(), checkObj
						.getDeliveryWarehouseCode(), "Y");
				/*ImWarehouse deliveryWarehouseOld = (ImWarehouse) imWarehouseDAO.findById("ImWarehouse", checkObj
						.getDeliveryWarehouseCode());*/
				if (null == deliveryWarehouse)
					this.messageHandle(MessageStatus.LOG_ERROR, identification, "轉出倉別代號("
							+ checkObj.getDeliveryWarehouseCode() + ")錯誤", user, errorMsgs);
				else {
					checkObj.setCustomsDeliveryWarehouseCode(deliveryWarehouse.getCustomsWarehouseCode());
					disallowMinusStock = "Y".equals(deliveryWarehouse.getAllowMinusStock()) ? false : true;
				}
			}
			log.info("5.3.1.8 check arrival warehouse....");
			ImWarehouse arrivalWarehouse = this.isCurrectArrivalWarehouse(checkObj.getBrandCode(), checkObj
					.getOrderTypeCode(), checkObj.getArrivalWarehouseCode(), checkObj.getLastUpdatedBy());
			if (null == arrivalWarehouse)
				this.messageHandle(MessageStatus.LOG_ERROR, identification, "轉入倉別代號(" + checkObj.getArrivalWarehouseCode()
						+ ")錯誤" + checkObj.getArrivalWarehouseCode().length(), user, errorMsgs);
			else
				checkObj.setCustomsArrivalWarehouseCode(arrivalWarehouse.getCustomsWarehouseCode());
			log.info("5.3.1.9 check delivery warehouse manager....");
			if ("unknow".equals(UserUtils.getLoginNameByEmployeeCode(checkObj.getDeliveryContactPerson())))
				this.messageHandle(MessageStatus.LOG_ERROR, identification, "查無出庫倉管人員("
						+ checkObj.getDeliveryContactPerson() + ")資料", user, errorMsgs);

			log.info("5.3.1.10 check arrival warehouse manager....");
			if ("unknow".equals(UserUtils.getLoginNameByEmployeeCode(checkObj.getArrivalContactPerson())))
				this.messageHandle(MessageStatus.LOG_ERROR, identification, "查無入庫倉管人員(" + checkObj.getArrivalContactPerson()
						+ ")資料", user, errorMsgs);

			log.info("5.3.1.11 get before status....");
			int i = 0;
			// String status = "";
			// if(OrderStatus.SAVE.equals( checkObj.getStatus())){
			// status = OrderStatus.SAVE;
			// }else if (OrderStatus.WAIT_OUT.equals( checkObj.getStatus())){
			// status = OrderStatus.SIGNING;
			// }else if (OrderStatus.WAIT_IN.equals( checkObj.getStatus())){
			// status = isThreePointMoving? OrderStatus.WAIT_OUT:
			// OrderStatus.SIGNING;
			// }else if (OrderStatus.FINISH.equals( checkObj.getStatus())){
			// status = OrderStatus.WAIT_IN;
			// }else if (OrderStatus.SIGNING.equals( checkObj.getStatus())){
			// status = OrderStatus.SAVE;
			// }else{
			// status = checkObj.getStatus();
			// }
			System.out.println("before status:" + beforeStatus);
			System.out.println("current status:" + checkObj.getStatus());
			if ("Y".equalsIgnoreCase(orderTypeSetting.getAttribute3())
					&& MOVEMENT_TYPE_ACCEPT.equalsIgnoreCase(orderTypeSetting.getAttribute5())) {
				List messageLists = this.checkAcceptMovementInReceive(checkObj, identification, errorMsgs);
				if (null != messageLists) {
					for (int j = 0; j < messageLists.size(); j++) {
						this.messageHandle(MessageStatus.LOG_ERROR, identification, (String) messageLists.get(j), user,
								errorMsgs);

					}
				}
			}
			if (OrderStatus.SAVE.equals(checkObj.getStatus()) || OrderStatus.UNCONFIRMED.equals(checkObj.getStatus())
					|| OrderStatus.SIGNING.equals(checkObj.getStatus()) || OrderStatus.WAIT_OUT.equals(checkObj.getStatus())) {
				itemCountField = isThreePointMoving ? "ORIGINAL_DELIVERY_QUANTITY" : "DELIVERY_QUANTITY";
			} else if (OrderStatus.WAIT_IN.equals(checkObj.getStatus())) {
				itemCountField = "DELIVERY_QUANTITY";
			} else if (OrderStatus.FINISH.equals(checkObj.getStatus())) {
				itemCountField = "ARRIVAL_QUANTITY";
			}
			log.info("5.3.1.12 check every item details....");
			Map checkInfo = new HashMap();
			checkInfo.put("brandCode", checkObj.getBrandCode());
			checkInfo.put("orderTypeCode", checkObj.getOrderTypeCode());
			checkInfo.put("itemCategory", checkObj.getItemCategory());
			checkInfo.put("deliveryContactPerson", checkObj.getDeliveryContactPerson());
			checkInfo.put("arrivalContactPerson", checkObj.getArrivalContactPerson());
			checkInfo.put("beforestatus", beforeStatus);
			checkInfo.put("itemLevelControl", buBrand.getItemLevelControl());
			checkInfo.put("buOrderType", buOrderType);
			checkInfo.put("orderTypeSetting", orderTypeSetting);
			checkInfo.put("taxType", checkObj.getTaxType());
			checkInfo.put("customsDeliveryWarehouseCode", checkObj.getCustomsDeliveryWarehouseCode());
			checkInfo.put("customsArrivalWarehouseCode", checkObj.getCustomsArrivalWarehouseCode());
			checkInfo.put("isThreePointMoving", isThreePointMoving);
			checkInfo.put("waitingWarehouseCode", waitingWarehouseCode);
			checkInfo.put("disallowMinusStock", disallowMinusStock);
			for (ImMovementItem imMovementItem : imMovementItems) {
				i++;
				if (StringUtils.hasText(imMovementItem.getItemCode())
						&& StringUtils.hasText(imMovementItem.getIsDeleteRecord())
						&& AjaxUtils.IS_DELETE_RECORD_FALSE.equals(imMovementItem.getIsDeleteRecord())) {
					// call new reflashImMovementItem
					this.reflashImMovementItem(checkInfo, imMovementItem);
					imMovementItem.setArrivalQuantity(imMovementItem.getDeliveryQuantity());
					if (StringUtils.hasText(imMovementItem.getReturnMessage())) {
						this.messageHandle(MessageStatus.LOG_ERROR, identification, "第" + i + "項,"
								+ StringTools.StringDelete(imMovementItem.getReturnMessage(), "Err - "), user, errorMsgs);
					}

					// 檢核出貨的數量總和不可為0
					if ("ORIGINAL_DELIVERY_QUANTITY".equals(itemCountField))
						itemCount = itemCount
								+ (null == imMovementItem.getOriginalDeliveryQuantity() ? 0D : imMovementItem
										.getOriginalDeliveryQuantity());

					else if ("DELIVERY_QUANTITY".equals(itemCountField))
						itemCount = itemCount
								+ (null == imMovementItem.getDeliveryQuantity() ? 0D : imMovementItem.getDeliveryQuantity());
					else {
						itemCount = itemCount
								+ (null == imMovementItem.getArrivalQuantity() ? 0D : imMovementItem.getArrivalQuantity());
					}
					log.info(itemCountField + ":" + itemCount + " ORIGINAL_DELIVERY_QUANTITY:"
							+ imMovementItem.getOriginalDeliveryQuantity() + " DELIVERY_QUANTITY:"
							+ imMovementItem.getDeliveryQuantity() + " ARRIVAL_QUANTITY:"
							+ imMovementItem.getArrivalQuantity());
					try {
						nowBoxCount = (null == imMovementItem.getBoxNo() ? 0L : Double.valueOf(imMovementItem.getBoxNo()));
						if (maxBoxCount < nowBoxCount)
							maxBoxCount = nowBoxCount;
					} catch (Exception ex) {
						this.messageHandle(MessageStatus.LOG_ERROR, identification, "第" + i + "項,箱號應為數字，請重新輸入", user,
								errorMsgs);
					}

				} else {
					if (OrderStatus.SAVE.equals(beforeStatus) || OrderStatus.REJECT.equals(beforeStatus)
							|| OrderStatus.UNCONFIRMED.equals(beforeStatus)) {
						removeArray.add(imMovementItem);

					}
				}

			}
			checkObj.setItemCount(itemCount);
			checkObj.setBoxCount(maxBoxCount);
			if (0D == itemCount)
				// if(OrderStatus.WAIT_OUT.equals(checkObj.getStatus()))
				this.messageHandle(MessageStatus.LOG_ERROR, identification, itemCountField + "商品總數不可為零", user, errorMsgs);
			log.info("before remove size:" + imMovementItems.size());
			for (int j = removeArray.size(); 0 < j; j--) {
				imMovementItems.remove(removeArray.get(j - 1));
			}

			System.out.println("after remove size:" + imMovementItems.size());
		} catch (Exception ex) {
			messageHandle(MessageStatus.LOG_ERROR, identification, ex.getMessage(), checkObj.getLastUpdatedBy(), errorMsgs);

		}
		// System.out.println(errorMsgs.size() );

		// if(errorMsgs.size() > 0){
		// throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		// }else{
		// removeDeleteMarkLineForItem(checkObj);
		// }
	}

	public void messageHandle(String messageStatus, String identification, String message, String user, List errorMsgs)
			throws Exception {

		siProgramLogAction.createProgramLog("IM_MOVEMENT", MessageStatus.LOG_ERROR, identification, message, user);
		errorMsgs.add(message);
		log.error("ERROR:" + message);
	}

	/*
	 * 暫存單號取實際單號並更新至Movement主檔
	 *
	 * @param headId @param loginUser @return ImPromotion @throws
	 * ObtainSerialNoFailedException @throws FormException @throws Exception
	 */
	public ImMovementHead saveActualOrderNo(Long headId, String loginUser) throws ObtainSerialNoFailedException,
			FormException, Exception {
		ImMovementHead imMovementPO = this.findById(headId);
		if (imMovementPO == null) {
			throw new NoSuchObjectException("查無調撥單主鍵：" + headId + "的資料！");
		} else { // 取得正式的單號
			
			//for 儲位用
			if(imStorageAction.isStorageExecute(imMovementPO)){
				//取得儲位單正式的單號 2011.11.11 by Caspar
				ImStorageHead imStorageHead = imStorageAction.updateOrderNo(imMovementPO);
			
				this.setOrderNo(imMovementPO);
				//更新儲位單SOURCE ORDER_NO
				imStorageHead.setSourceOrderNo(imMovementPO.getOrderNo());
				imStorageService.updateHead(imStorageHead, imMovementPO.getLastUpdatedBy());
			
			}else{
				this.setOrderNo(imMovementPO);
			}
			
		}
		imMovementPO.setLastUpdatedBy(loginUser);
		imMovementPO.setLastUpdateDate(new Date());
		imMovementHeadDAO.update(imMovementPO);
		return imMovementPO;
	}

	/**
	 * 若是暫存單號,則取得新單號
	 *
	 * @param head
	 */
	private void setOrderNo(ImMovementHead head) throws ObtainSerialNoFailedException {
		String orderNo = head.getOrderNo();
		log.info("3.setOrderNo...original_order=" + orderNo);
		if (AjaxUtils.isTmpOrderNo(orderNo)) {
			try {
				String serialNo = buOrderTypeService.getOrderSerialNo(head.getBrandCode(), head.getOrderTypeCode());
				if ("unknow".equals(serialNo))
					throw new ObtainSerialNoFailedException("取得" + head.getBrandCode() + "-" + head.getOrderTypeCode()
							+ "單號失敗！");
				else {
					head.setOrderNo(serialNo);
					log.info("the order no. is " + serialNo);
				}
			} catch (Exception ex) {
				throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode() + "單號失敗！");
			}
		}
	}

	private void appendErrorMessage(StringBuffer errorString, String message) {

		String prefixString = 0 == errorString.length() ? "#" : " #";
		errorString.append(prefixString + message);
		log.info("appendErrorMessage:" + errorString.toString());
	}

	public void updateMoveOnHand(ImMovementHead imMovementHead, boolean allowCmStockMinus, String identification,
			List errorMsgs, String onHandMode, boolean allowImStockMinus) throws Exception {
		log.info("updateMoveOnHand....");
		ArrayList removeArray = new ArrayList(0);
		String user = imMovementHead.getLastUpdatedBy();
		String organizationCode = new String();
		String cmWarehouseCode = new String();
		String warehouseCode = new String();
		String cmDeliveryWarehouseCode = new String();
		String cmArrivalWarehouseCode = new String();
		Boolean disallowMinusStock = new Boolean(true);
		String lotControl = new String("N");
		try {
			boolean isThreePointMoving = this.isThreePointMoving(imMovementHead.getBrandCode(), imMovementHead
					.getOrderTypeCode());
			organizationCode = UserUtils.getOrganizationCodeByBrandCode(imMovementHead.getBrandCode());
			if (!(StringUtils.hasText(organizationCode)))
				this.messageHandle(MessageStatus.LOG_ERROR, identification, "依據品牌：" + imMovementHead.getBrandCode()
						+ "查無其組織代號！", user, errorMsgs);
			BuBrand buBrand = buBrandDAO.findById(imMovementHead.getBrandCode());
			if (null != buBrand) {
				lotControl = StringUtils.hasText(buBrand.getLotControl()) ? buBrand.getLotControl() : "N";
			} else {
				this.messageHandle(MessageStatus.LOG_ERROR, identification, "查無品牌：" + imMovementHead.getBrandCode() + "資訊！",
						user, errorMsgs);
			}
			warehouseCode = OrderStatus.FINISH.equals(imMovementHead.getStatus()) ? imMovementHead.getArrivalWarehouseCode()
					: imMovementHead.getDeliveryWarehouseCode();

			ImWarehouse imDeliveryWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class,
					imMovementHead.getDeliveryWarehouseCode());
			if (null == imDeliveryWarehouse) {
				messageHandle(MessageStatus.LOG_ERROR, identification, "查無出庫庫別(" + imMovementHead.getDeliveryWarehouseCode()
						+ ")資料", imMovementHead.getLastUpdatedBy(), errorMsgs);
			} else {

				cmDeliveryWarehouseCode = StringUtils.hasText(imDeliveryWarehouse.getCustomsWarehouseCode()) ? imDeliveryWarehouse
						.getCustomsWarehouseCode()
						: "";
				if (!allowImStockMinus)
					disallowMinusStock = "Y".equals(imDeliveryWarehouse.getAllowMinusStock()) ? false : true;
				else
					disallowMinusStock = !allowImStockMinus;

				log.info("warehouseCode=" + imMovementHead.getDeliveryWarehouseCode() + "/cmDeliveryWarehouseCode="
						+ cmDeliveryWarehouseCode);
				ImWarehouse imArrivalWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class,
						imMovementHead.getArrivalWarehouseCode());
				if (null == imArrivalWarehouse) {
					messageHandle(MessageStatus.LOG_ERROR, identification, "查無出庫庫別("
							+ imMovementHead.getArrivalWarehouseCode() + ")資料", imMovementHead.getLastUpdatedBy(), errorMsgs);
				} else {
					cmArrivalWarehouseCode = StringUtils.hasText(imArrivalWarehouse.getCustomsWarehouseCode()) ? imArrivalWarehouse
							.getCustomsWarehouseCode()
							: "";

					log.info("warehouseCode=" + imMovementHead.getArrivalWarehouseCode() + "/cmDeliveryWarehouseCode="
							+ cmArrivalWarehouseCode);
					boolean diffCustomsWarehouse = TAX_MODE_FREE.equalsIgnoreCase(imMovementHead.getTaxType())
							&& !cmDeliveryWarehouseCode.equals(cmArrivalWarehouseCode);

					cmWarehouseCode = OrderStatus.FINISH.equals(imMovementHead.getStatus()) ? imArrivalWarehouse
							.getCustomsWarehouseCode() : imDeliveryWarehouse.getCustomsWarehouseCode();
					log.info("warehouseCode=" + warehouseCode + "/cmWarehouseCode=" + cmWarehouseCode
							+ "/diffCustomsWarehouse=" + diffCustomsWarehouse);
					Map imOnHandMaps = new HashMap();
					Map cmOnHandMaps = new HashMap();
					Double quantity = new Double(0);
					String lotNo = "";
					int i = 1;
					List<ImMovementItem> imMovementItems = imMovementHead.getImMovementItems();
					log.info("collect onhand info");
					for (ImMovementItem imMovementItem : imMovementItems) {
						// 如果在待轉出時刪除資料，將把未結庫存回復
						if (StringUtils.hasText(imMovementItem.getIsDeleteRecord())
								&& AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imMovementItem.getIsDeleteRecord())) {
							imMovementItem.setDeliveryQuantity(0D);
							imMovementItem.setArrivalQuantity(0D);
							removeArray.add(imMovementItem);
						}
						// When status of the Form is WAIT_OUT, the moveUncommitQty must to be subtracted
						if (OrderStatus.SIGNING.equals(imMovementHead.getStatus())) {
							quantity = (isThreePointMoving ? imMovementItem.getOriginalDeliveryQuantity() : imMovementItem
									.getDeliveryQuantity())
									* -1;
							lotNo = MOVEMENT_TYPE_ACCEPT.equalsIgnoreCase(onHandMode) ? defaultLotNo : imMovementItem
									.getLotNo().trim();
						} else if (OrderStatus.WAIT_IN.equals(imMovementHead.getStatus())) {
							if (isThreePointMoving) {
								quantity = imMovementItem.getOriginalDeliveryQuantity()
										- imMovementItem.getDeliveryQuantity();
							} else {
								// quantity = imMovementItem.getDeliveryQuantity() * -1;
								quantity = 0D;
							}
							lotNo = MOVEMENT_TYPE_ACCEPT.equalsIgnoreCase(onHandMode) ? defaultLotNo : imMovementItem
									.getLotNo().trim();
						} else if (OrderStatus.FINISH.equals(imMovementHead.getStatus())) {
							quantity = imMovementItem.getArrivalQuantity();
							lotNo = "Y".equalsIgnoreCase(lotControl) ? imMovementItem.getLotNo().trim() : defaultLotNo;

						} else {
							quantity = (isThreePointMoving ? imMovementItem.getOriginalDeliveryQuantity() : imMovementItem
									.getDeliveryQuantity())
									* -1;
							lotNo = "Y".equalsIgnoreCase(lotControl) ? imMovementItem.getLotNo().trim() : defaultLotNo;
						}
						log.info(i + ".itemCode=" + imMovementItem.getItemCode() + "lotNo=" + lotNo + "/quantity="
								+ quantity);
						this.collectImOnHand(imMovementItem, imOnHandMaps, i, warehouseCode, lotNo, quantity);
						if (diffCustomsWarehouse)
							this.collectCmOnHand(imMovementItem, cmOnHandMaps, i, cmWarehouseCode, quantity);
						i++;
					}
					log.info("updateImOnHandByMap head_id = " + imMovementHead.getHeadId());
					this.updateImOnHandByMap(organizationCode, imMovementHead.getBrandCode(), imOnHandMaps,
							disallowMinusStock, user, identification, errorMsgs);
					if (diffCustomsWarehouse)
						this.updateCmOnHandByMap(imMovementHead.getBrandCode(), cmOnHandMaps, allowCmStockMinus, user,
								identification, errorMsgs);
					for (int j = removeArray.size(); 0 < j; j--) { // 移除註記刪除的item
						imMovementItems.remove(removeArray.get(j - 1));
					}
					
				}
			}
		} catch (Exception ex) {
			messageHandle(MessageStatus.LOG_ERROR, identification, "更新調撥單庫存時(updateMoveOnHand)發生錯誤，原因:" + ex.getMessage(),
					imMovementHead.getLastUpdatedBy(), errorMsgs);
			//throw ex;
		}

	}

	private void collectImOnHand(ImMovementItem imMovementItem, Map collectMaps, int lineId, String warehouseCode,
			String lotNo, Double quantity) throws Exception {
		log.info("collectImOnHand...." + imMovementItem.getItemCode().trim() + warehouseCode + lotNo + "/" + quantity);
		String key = imMovementItem.getItemCode().trim() + warehouseCode + lotNo;
		try {
			if (collectMaps.containsKey(key)) {
				Map imOnHandMap = (Map) collectMaps.get(key);
				imOnHandMap.put("lineId", (String) imOnHandMap.get("lineId") + String.valueOf(lineId).trim());
				Double newQuantity = (Double) imOnHandMap.get("quantity") + quantity;
				imOnHandMap.put("quantity", newQuantity);
			} else {
				Map imOnHandMap = new HashMap();
				imOnHandMap.put("lineId", String.valueOf(lineId).trim());
				imOnHandMap.put("itemCode", imMovementItem.getItemCode().trim());
				imOnHandMap.put("warehouseCode", warehouseCode);
				imOnHandMap.put("lotNo", lotNo);
				imOnHandMap.put("quantity", quantity);
				collectMaps.put(key, imOnHandMap);
			}
		} catch (Exception ex) {
			throw new Exception("庫存統計時發生錯誤，原因：" + ex.getMessage());

		}
	}

	private void collectCmOnHand(ImMovementItem imMovementItem, Map collectMaps, int lineId, String cmWarehouseCode,
			Double quantity) throws Exception {
		log.info("collectCmOnHand....");
		try {
			String key = imMovementItem.getItemCode().trim() + cmWarehouseCode
					+ imMovementItem.getOriginalDeclarationNo().trim()
					+ String.valueOf(imMovementItem.getOriginalDeclarationSeq()).trim();
			if (collectMaps.containsKey(key)) {
				Map cmOnHandMap = (Map) collectMaps.get(key);
				cmOnHandMap.put("lineId", (String) cmOnHandMap.get("lineId") + String.valueOf(lineId).trim());
				Double newQuantity = (Double) cmOnHandMap.get("quantity") + quantity;
				cmOnHandMap.put("quantity", newQuantity);
			} else {
				Map cmOnHandMap = new HashMap();
				cmOnHandMap.put("lineId", String.valueOf(lineId).trim());
				cmOnHandMap.put("itemCode", imMovementItem.getItemCode().trim());
				cmOnHandMap.put("cmWarehouseCode", cmWarehouseCode.trim());
				cmOnHandMap.put("declarationNo", imMovementItem.getOriginalDeclarationNo().trim());
				cmOnHandMap.put("declarationSeq", imMovementItem.getOriginalDeclarationSeq());
				cmOnHandMap.put("quantity", quantity);
				collectMaps.put(key, cmOnHandMap);
			}
		} catch (Exception ex) {
			throw new Exception("報關單庫存統計時發生錯誤，原因：" + ex.getMessage());

		}
	}

	/**
	 * 更新庫存
	 *
	 * @param organizationCode
	 * @param brandCode
	 * @param collectMaps
	 * @param disallowMinusStock
	 * @param employeeCode
	 * @param identification
	 * @param errorMsgs
	 * @throws Exception
	 */
	private void updateImOnHandByMap(String organizationCode, String brandCode, Map collectMaps, boolean disallowMinusStock,
			String employeeCode, String identification, List errorMsgs) throws Exception {
		log.info("updateImOnHandByMap....");
		Iterator it = collectMaps.values().iterator();
		String lineId = new String("");
		String itemCode = new String("");
		String warehouseCode = new String("");
		String lotNo = new String("");
		Double quantity = new Double(0);
		try {
			while (it.hasNext()) {
				Map modifyObj = (Map) it.next();
				if (null != modifyObj) {
					quantity = (Double) modifyObj.get("quantity");
					if (quantity != 0) {
						lineId = (String) modifyObj.get("lineId");
						itemCode = (String) modifyObj.get("itemCode");
						warehouseCode = (String) modifyObj.get("warehouseCode");
						lotNo = (String) modifyObj.get("lotNo");
						log.info("updateImOnHandByMap.itemCode=" + itemCode + "/" + "warehouseCode=" + warehouseCode
								+ "/lotNo=" + lotNo + "/quantity=" + quantity);
						int tryTimes = 3; // 嘗試連接次數
						int interval = 5000; // 等待時間
						for (int i = 0; i < tryTimes; i++) { // 如果更新失敗，經過5秒後重試
							try {
								if (disallowMinusStock) { // 不允許負庫存
									log.info("disallowMinusStock... itemCode = " + itemCode + " and warehouseCode = " + warehouseCode + " and quantity = " + quantity);
									imOnHandDAO.updateMoveUncommitQuantity(organizationCode, itemCode, warehouseCode, lotNo,
											quantity, employeeCode, brandCode);
								} else { // 允許負庫存
									log.info("allowMinusStock... itemCode = " + itemCode + " and warehouseCode = " + warehouseCode + " and quantity = " + quantity);
									imOnHandDAO.updateMoveUncommitQuantityAllowMinus(organizationCode, itemCode,
											warehouseCode, lotNo, quantity, employeeCode, brandCode);

								}
								break;
							} catch (Exception e) {
								try {
									Thread.sleep(interval);
								} catch (InterruptedException ie) {
									ie.printStackTrace();
								}
								if (i == tryTimes - 1) {
									messageHandle(MessageStatus.LOG_ERROR, identification, "更新調撥單庫存時(updateImOnHandByMap."
											+ disallowMinusStock + ")發生錯誤，原因:" + e.getMessage(), employeeCode, errorMsgs);
									//throw e;
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			messageHandle(MessageStatus.LOG_ERROR, identification, "更新調撥單庫存時(updateImOnHandByMap." + disallowMinusStock
					+ ")發生錯誤，原因:" + ex.getMessage(), employeeCode, errorMsgs);
			// throw new NoSuchObjectException(ex.getMessage()+"");
			//throw ex;
		}
	}

	private void updateCmOnHandByMap(String brandCode, Map collectMaps, boolean isAllowMinus, String employeeCode,
			String identification, List errorMsgs) throws Exception {
		log.info("updateCmOnHandByMap....");
		Iterator it = collectMaps.values().iterator();
		String lineId = new String("");
		String itemCode = new String("");
		String cmWarehouseCode = new String("");
		String declarationNo = new String("");
		Long declarationSeq = new Long(0);
		Double quantity = new Double(0);
		try {
			while (it.hasNext()) {
				Map modifyObj = (Map) it.next();
				if (null != modifyObj) {
					quantity = (Double) modifyObj.get("quantity");
					if (quantity != 0) {
						lineId = (String) modifyObj.get("lineId");
						itemCode = (String) modifyObj.get("itemCode");
						cmWarehouseCode = (String) modifyObj.get("cmWarehouseCode");
						declarationNo = (String) modifyObj.get("declarationNo");
						declarationSeq = (Long) modifyObj.get("declarationSeq");
						log.info("itemCode=" + itemCode + "/" + "cmWarehouseCode=" + cmWarehouseCode + "/declarationNo="
								+ declarationNo + "/declarationSeq=" + declarationSeq + "/quantity=" + quantity);
						cmDeclarationOnHandDAO.updateMoveUncommitQuantity(declarationNo, declarationSeq, itemCode,
								cmWarehouseCode, brandCode, quantity, employeeCode);
					}
				}

			}
		} catch (Exception ex) {
			messageHandle(MessageStatus.LOG_ERROR, identification, "更新調撥單報關庫存時(updateCmOnHandByMap)發生錯誤，原因:"
					+ ex.getMessage(), employeeCode, errorMsgs);
			// throw new NoSuchObjectException(ex.getMessage()+lineId);
		}

	}

	private void updateImStockOnHandByMap(String organizationCode, String brandCode, Map collectMaps, String employeeCode,
			String identification, List errorMsgs) throws Exception {
		log.info("updateImStockOnHandByMap....");
		Iterator it = collectMaps.values().iterator();
		String lineId = new String("");
		String itemCode = new String("");
		String warehouseCode = new String("");
		String lotNo = new String("");
		Double quantity = new Double(0);
		try {
			while (it.hasNext()) {
				Map modifyObj = (Map) it.next();
				if (null != modifyObj) {
					quantity = (Double) modifyObj.get("quantity");
					if (quantity != 0) {
						lineId = (String) modifyObj.get("lineId");
						itemCode = (String) modifyObj.get("itemCode");
						warehouseCode = (String) modifyObj.get("warehouseCode");
						lotNo = (String) modifyObj.get("lotNo");
						log.info("updateImStockOnHandByMap.itemCode=" + itemCode + "/" + "warehouseCode=" + warehouseCode
								+ "/lotNo=" + lotNo + "/quantity=" + quantity);
						imOnHandDAO.updateStockOnHand(organizationCode, brandCode, itemCode, warehouseCode, lotNo, quantity,
								employeeCode);

					}
				}

			}
		} catch (Exception ex) {
			messageHandle(MessageStatus.LOG_ERROR, identification, "更新調撥單庫存時(updateImOnHandByMap." + ")發生錯誤，原因:"
					+ ex.getMessage(), employeeCode, errorMsgs);
			// throw new NoSuchObjectException(ex.getMessage()+"");
		}

	}

	private void updateCmStockOnHandByMap(String brandCode, Map collectMaps, String employeeCode, String identification,
			List errorMsgs) throws Exception {
		log.info("updateCmOnHandByMap....");
		Iterator it = collectMaps.values().iterator();
		String lineId = new String("");
		String itemCode = new String("");
		String cmWarehouseCode = new String("");
		String declarationNo = new String("");
		Long declarationSeq = new Long(0);
		Double quantity = new Double(0);
		try {
			while (it.hasNext()) {
				Map modifyObj = (Map) it.next();
				if (null != modifyObj) {
					quantity = (Double) modifyObj.get("quantity");
					if (quantity != 0) {
						lineId = (String) modifyObj.get("lineId");
						itemCode = (String) modifyObj.get("itemCode");
						cmWarehouseCode = (String) modifyObj.get("cmWarehouseCode");
						declarationNo = (String) modifyObj.get("declarationNo");
						declarationSeq = (Long) modifyObj.get("declarationSeq");
						log.info("itemCode=" + itemCode + "/" + "cmWarehouseCode=" + cmWarehouseCode + "/declarationNo="
								+ declarationNo + "/declarationSeq=" + declarationSeq + "/quantity=" + quantity);
						cmDeclarationOnHandDAO.updateStockOnHand(declarationNo, declarationSeq, itemCode, cmWarehouseCode,
								brandCode, quantity, cmWarehouseCode);
					}
				}

			}
		} catch (Exception ex) {
			messageHandle(MessageStatus.LOG_ERROR, identification, "更新調撥單報關庫存時(updateCmOnHandByMap)發生錯誤，原因:"
					+ ex.getMessage(), employeeCode, errorMsgs);
			// throw new NoSuchObjectException(ex.getMessage()+lineId);
		}

	}

	/**
	 * 重新排序Index_No
	 *
	 * @param imMovementHead
	 * @throws Exception
	 */
	private void sortMovementItem(ImMovementHead imMovementHead) throws Exception {
		log.info("sort line data .....");
		List<ImMovementItem> imMovementItems = imMovementItemDAO.findByHeadId(imMovementHead.getHeadId(), "boxNo, itemCode");
		imMovementItems = StringTools.setBeanValue(imMovementItems, "indexNo", null);
		Long i = 1L;
		for (ImMovementItem item : imMovementItems) {
		    item.setIndexNo(i);
		    log.info("SET-imMovementItemIndexNo:"+i);
		    i++;
		}
		imMovementHead.setImMovementItems(imMovementItems);
		imMovementHeadDAO.merge(imMovementHead);
	}

	private void copyMovementToAdjustment(Long headId) throws ValidationErrorException {
		ImMovementHead movement = this.findById(headId);
		if (null != movement) {
			BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(movement.getBrandCode(), movement
					.getOrderTypeCode()));
			if (buOrderType == null)
				throw new ValidationErrorException("查無單別資料-OrderType(" + movement.getOrderTypeCode() + ")，請通知系統管理員");

			BuCommonPhraseLine orderTypeSetting = buCommonPhraseLineDAO.findById("ImMovementOrderType", movement
					.getBrandCode()
					+ movement.getOrderTypeCode());
			if (orderTypeSetting == null)
				throw new ValidationErrorException("查無單別資料-CommonPhraase(" + movement.getOrderTypeCode() + ")，請通知系統管理員");

			String type = StringUtils.hasText(orderTypeSetting.getAttribute5()) ? orderTypeSetting.getAttribute5()
					: "NORMAL";
			if (MOVEMENT_TYPE_GIFT.equalsIgnoreCase(type) || MOVEMENT_TYPE_TEST.equalsIgnoreCase(type)
					|| MOVEMENT_TYPE_DISPLAY.equalsIgnoreCase(type)) {
				ImAdjustmentHead adjustment = new ImAdjustmentHead();

				// adjustment.setHeadId();
				adjustment.setBrandCode(movement.getBrandCode());
				adjustment.setOrderTypeCode(buOrderType.getNextOrderTypeCode());
				// adjustment.setOrderNo();
				adjustment.setTaxType(movement.getTaxType());
				adjustment.setAdjustmentDate(movement.getDeliveryDate());
				adjustment.setAdjustmentType(NCV_TYPE);
				adjustment.setDefaultWarehouseCode(movement.getArrivalWarehouseCode());
				adjustment.setReason("");
				adjustment.setAffectCost("N");
				adjustment.setSourceOrderTypeCode(movement.getOrderTypeCode());
				adjustment.setSourceOrderNo(movement.getOrderNo());
				adjustment.setDeclarationType("");
				adjustment.setDeclarationNo("");
				adjustment.setDeclarationDate(null);
				adjustment.setDeclarationType("");
				// adjustment.setBoxQty(movement.getBoxCount());
				adjustment.setStatus(OrderStatus.SAVE);
				adjustment.setRemark1("");
				adjustment.setRemark2("");
				adjustment.setCreatedBy(movement.getLastUpdatedBy());
				adjustment.setCreationDate(new Date());
				adjustment.setLastUpdatedBy(movement.getLastUpdatedBy());
				adjustment.setLastUpdateDate(new Date());
				List<ImMovementItem> movementItems = movement.getImMovementItems();
				String relativeItemCode = new String();
				String relativeLotNo = new String();
				List<ImAdjustmentLine> adjustmentLines = new ArrayList();
				boolean isSaveRelativeData = false;
				for (ImMovementItem movementItem : movementItems) {
					ImItem item = imItemDAO.findItem(movement.getBrandCode(), movementItem.getItemCode());
					if (null != item) {
						isSaveRelativeData = false;
						ImAdjustmentLine adjustmentLine = new ImAdjustmentLine();
						adjustmentLine.setItemCode(movementItem.getItemCode());
						adjustmentLine.setWarehouseCode(movementItem.getArrivalWarehouseCode());
						adjustmentLine.setDifQuantity(movementItem.getDeliveryQuantity() * -1);
						adjustmentLine.setCustomsItemCode(movementItem.getItemCode());
						adjustmentLine.setAmount(0D);
						adjustmentLine.setLotNo(movementItem.getLotNo());
						adjustmentLine.setCreatedBy(movement.getLastUpdatedBy());
						adjustmentLine.setCreationDate(new Date());
						adjustmentLine.setLastUpdatedBy(movement.getLastUpdatedBy());
						adjustmentLine.setLastUpdateDate(new Date());
						adjustmentLines.add(adjustmentLine);
						if (TAX_MODE_FREE.equalsIgnoreCase(movement.getTaxType())
								&& MOVEMENT_TYPE_TEST.equalsIgnoreCase(type)) {
							relativeItemCode = item.getTaxRelativeItemCode();
							relativeLotNo = movementItem.getLotNo();
							isSaveRelativeData = true;
						} else if (MOVEMENT_TYPE_GIFT.equalsIgnoreCase(type)) {
							relativeItemCode = item.getTaxRelativeItemCode();
							relativeLotNo = defaultLotNo;
							isSaveRelativeData = true;
						}
						if (isSaveRelativeData) {
							ImAdjustmentLine relativeAdjustmentLine = new ImAdjustmentLine();
							adjustmentLine.setItemCode(relativeItemCode);
							adjustmentLine.setWarehouseCode(movementItem.getArrivalWarehouseCode());
							adjustmentLine.setDifQuantity(movementItem.getDeliveryQuantity());
							adjustmentLine.setCustomsItemCode(relativeItemCode);
							adjustmentLine.setAmount(0D);
							adjustmentLine.setLotNo(relativeLotNo);
							adjustmentLine.setCreatedBy(movement.getLastUpdatedBy());
							adjustmentLine.setCreationDate(new Date());
							adjustmentLine.setLastUpdatedBy(movement.getLastUpdatedBy());
							adjustmentLine.setLastUpdateDate(new Date());
							adjustmentLines.add(adjustmentLine);
							adjustmentLines.add(adjustmentLine);
						}
					} else {
						throw new ValidationErrorException("查無商品資料(" + movementItem.getItemCode() + ")，請通知系統管理員");
					}
				}
			}
		}
	}

	private String getWarehouseManager(String brandCode, String warehouseCode, String customsWarehouseCode,
			String itemCategoryMode, String itemCategory) {
		log.info("getWarehouseManager...." + brandCode + "/" + warehouseCode + "/" + customsWarehouseCode + "/"
				+ itemCategoryMode + "/" + itemCategory);
		String result = new String();
		if ("Y".equalsIgnoreCase(itemCategoryMode) && StringUtils.hasText(customsWarehouseCode)) {
			BuCommonPhraseLine warehouseManager = buCommonPhraseLineDAO.findById("WarehouseManagerByCategory", brandCode
					+ customsWarehouseCode + itemCategory);
			result = warehouseManager != null ? warehouseManager.getAttribute2() : null;

		} else {
			ImWarehouse warehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, warehouseCode);
			result = warehouse != null ? warehouse.getWarehouseManager() : null;
		}
		return result;
	}

	/**
	 * remove delete mark record(item)
	 *
	 * @param promotion
	 */
	private void removeDeleteMarkLineForItem(ImMovementHead movement) throws Exception{
		log.info("移除有{刪除註記}的資料...");
		List<ImMovementItem> movementItems = movement.getImMovementItems();
		log.info("移除有{刪除註記}的資料 ====> 1");
		if (movementItems != null && movementItems.size() > 0) {
			log.info("移除有{刪除註記}的資料 ====> 2");
			for (int i = movementItems.size() - 1; i >= 0; i--) {
				log.info("移除有{刪除註記}的資料 ====> 3."+i);
				ImMovementItem movementItem = (ImMovementItem) movementItems.get(i);
				log.info("移除有{刪除註記}的資料 ====> 4");
				if (AjaxUtils.IS_DELETE_RECORD_TRUE.equals(movementItem.getIsDeleteRecord())) {
					log.info("移除有{刪除註記}的資料 ====> 5");
					movementItems.remove(movementItem);
					log.info("移除有{刪除註記}的資料 ====> final");
				}
			}
			Long i = 1L; // 重新排序INDEX_NO by Weichun 2010/12/17
			for (ImMovementItem item : movementItems) {
			    item.setIndexNo(i);
			    log.info("SET-imMovementItemIndexNo:"+i);
			    i++;
			}
			movement.setImMovementItems(movementItems);
		}
	}

	public void reflashImMovement(ImMovementHead reflashObj) throws FormException, NoSuchDataException {
		BuBrand buBrand = buBrandDAO.findById(reflashObj.getBrandCode());
		if (buBrand != null) {
			List<ImMovementItem> imMovementItems = reflashObj.getImMovementItems();
			int i = 0;
			for (ImMovementItem imMovementItem : imMovementItems) {
				if (!StringUtils.hasText(imMovementItem.getItemCode())) {
					imMovementItem.setItemCode("");
				}
				// call old reflashImMovementItem
				this.reflashImMovementItem(reflashObj.getBrandCode(), reflashObj.getOrderTypeCode(), reflashObj
						.getDeliveryContactPerson(), reflashObj.getArrivalContactPerson(), reflashObj.getStatus(), buBrand
						.getItemLevelControl(), imMovementItem);

				i++;
			}
		} else {
			throw new FormException("查詢品牌資訊(" + reflashObj.getBrandCode() + ")請通知資訊人員");
		}

	}

	public void reflashImMovementItem(String brandCode, String orderTypeCode, String deliveryContactPerson,
			String arrivalContactPerson, String status, String itemLevelControl, ImMovementItem itemObj)
			throws FormException, NoSuchDataException {
		// 判斷為兩點或三點調撥

		boolean isThreePointMoving = false;
		if ("IMD".equals(orderTypeCode) || "IMS".equals(orderTypeCode) || "IML".equals(orderTypeCode))
			isThreePointMoving = true;
		log.info("status:" + status);
		// status為原Status往前推一層之狀態
		// CLOSE.FINISH.WAIT_OUT.WAIT_IN 此些狀態，於reflashImMovementItem時僅查出品名
		if (OrderStatus.FINISH.equals(status) || OrderStatus.CLOSE.equals(status) || OrderStatus.WAIT_OUT.equals(status)
				|| OrderStatus.WAIT_IN.equals(status)) {
			System.out.println("7.1 just get item name");
			ImItem queryResult = imItemDAO.findItem(brandCode, itemObj.getItemCode());
			if (queryResult != null) {
				itemObj.setItemName(queryResult.getItemCName());
				itemObj.setReserve1("Okay ");
				if (OrderStatus.WAIT_OUT.equals(status)) {
					if (isThreePointMoving && itemObj.getOriginalDeliveryQuantity() < itemObj.getDeliveryQuantity()) {
						itemObj.setReserve1("Err - 實際出庫數(" + itemObj.getOriginalDeliveryQuantity() + ")不可大於預計出庫數("
								+ itemObj.getDeliveryQuantity() + ")");
					} else {
						itemObj.setReserve1("Okay ");
					}
				}
			} else {
				itemObj.setItemName("查無此品號");
				itemObj.setReserve1("Err - 品牌:" + brandCode + ",品號(" + itemObj.getItemCode() + ")輸入錯誤");
			}
		} else {
			log.info("7.1 check item data");
			log.info("item data:" + itemObj.getDeliveryWarehouseCode() + "/" + itemObj.getItemCode() + "/"
					+ itemObj.getLotNo() + "/" + deliveryContactPerson);
			if (StringUtils.hasText(itemObj.getDeliveryWarehouseCode())) {
				if (StringUtils.hasText(itemObj.getItemCode())) {
					if (StringUtils.hasText(itemObj.getLotNo())) {
						try {

							itemObj.setItemCode(itemObj.getItemCode().toUpperCase());
							HashMap findObjs = new HashMap();
							findObjs.put("brandCode", brandCode);
							findObjs.put("itemName", "");
							findObjs.put("warehouseManager", deliveryContactPerson);
							findObjs.put("startItemCode", itemObj.getItemCode());
							findObjs.put("endItemCode", itemObj.getItemCode());
							findObjs.put("startWarehouseCode", itemObj.getDeliveryWarehouseCode());
							findObjs.put("endWarehouseCode", itemObj.getDeliveryWarehouseCode());
							findObjs.put("startLotNo", itemObj.getLotNo());
							findObjs.put("endLotNo", itemObj.getLotNo());
							findObjs.put("categorySearchString", "");

							List<ImItemOnHandView> queryResult = imItemOnHandViewDAO.find(findObjs);
							log.info("7.1.1 find imItemOnHandView " + itemObj.getItemCode() + "query result size:"
									+ queryResult.size());

							if (queryResult.size() > 0) {
								ImItemOnHandView imItemOnHandView = (ImItemOnHandView) queryResult.get(0);

								itemObj.setItemCode(imItemOnHandView.getId().getItemCode().toUpperCase());
								itemObj.setItemName(imItemOnHandView.getItemCName());
								itemObj.setLotControl(imItemOnHandView.getLotControl());
								/*
								 * if("N".equals(imItemOnHandView.getLotControl())){
								 * BuCommonPhraseLine buCommonPhraseLine =
								 * buCommonPhraseLineDAO.findById("SystemConfig",
								 * "DefaultLotNo"); itemObj.setLotNo(
								 * buCommonPhraseLine != null?
								 * buCommonPhraseLine.getName():"000000000000"); }
								 */
								itemObj.setStockOnHandQuantity(imItemOnHandView.getCurrentOnHandQty());

								/*
								 * if (StringUtils.hasText(itemObj.getLotNo())) {
								 * itemObj.setLotNo(imItemOnHandView.getId().getLotNo()); }
								 * else { itemObj.setReserve1("Err - 您尚未輸入批號"); }
								 */

								//
								if (itemObj.getDeliveryQuantity() == null)
									itemObj.setDeliveryQuantity(0D);

								if (itemObj.getOriginalDeliveryQuantity() == null)
									itemObj.setOriginalDeliveryQuantity(0D);

								itemObj.setStockOnHandQuantity(imItemOnHandView.getCurrentOnHandQty());
								Double quantity = isThreePointMoving ? itemObj.getOriginalDeliveryQuantity() : itemObj
										.getDeliveryQuantity();
								// TODO SIGNING要確認數量
								if (quantity == null || quantity == 0) {
									itemObj.setReserve1("Err - 您尚未輸入轉出數量");
								} else if (OrderStatus.SAVE.equals(status) && quantity > itemObj.getStockOnHandQuantity()) {
									itemObj.setReserve1("Err - 轉出數量(" + quantity + ")不可大於庫存數量("
											+ itemObj.getStockOnHandQuantity() + ")");
								} else if (itemObj.getArrivalWarehouseCode() != null) {
									HashMap findWarehouseMap = new HashMap();
									findWarehouseMap.put("brandCode", brandCode);
									findWarehouseMap.put("warehouseCode", itemObj.getArrivalWarehouseCode());
									findWarehouseMap.put("warehouseName", "");
									findWarehouseMap.put("storage", "");
									findWarehouseMap.put("storageArea", "");
									findWarehouseMap.put("storageBin", "");
									findWarehouseMap.put("warehouseTypeId", 0L);
									findWarehouseMap.put("categoryCode", "");
									findWarehouseMap.put("locationId", 0L);
									findWarehouseMap.put("taxTypeCode", "");
									findWarehouseMap.put("warehouseManager", arrivalContactPerson);
									findWarehouseMap.put("enable", "Y");
									List<ImWarehouse> imWarehouses = imWarehouseDAO.find(findWarehouseMap);
									if (imWarehouses.size() == 0) {
										itemObj.setReserve1("Err -  轉入庫別輸入錯誤");
									} else {
										// 商品等級
										if ("Y".equals(itemLevelControl)) {
											if ("S".equals(imWarehouses.get(0).getCategoryCode())) {
												BuShopService buShopService = (BuShopService) SpringUtils
														.getApplicationContext().getBean("buShopService");
												String shopLevel = buShopService.findShopLevelbyWarehouseCode(imWarehouses
														.get(0).getWarehouseCode());
												String itemLevel = imItemOnHandView.getItemLevel();
												if (shopLevel != null) {
													if (itemLevel.compareToIgnoreCase(shopLevel) < 0) {
														itemObj.setReserve1("Err -  商品與店櫃等級不符(商品:" + itemLevel + "級，店櫃"
																+ shopLevel + "級)");
													} else {
														itemObj.setReserve1("Okay ");
													}
												} else {
													itemObj.setReserve1("Err -  轉入庫別為店櫃類型，但於店櫃設定中未設定出貨庫別");
												}
											} else {
												itemObj.setReserve1("Okay ");
											}
										} else {
											itemObj.setReserve1("Okay ");
										}
									}
								} else {
									itemObj.setReserve1("Err -  您尚未輸入轉入庫別");
								}
							} else {
								itemObj.setStockOnHandQuantity(0D);
								itemObj.setItemName("品號、庫號或批號輸入錯誤");
								itemObj.setReserve1("Err - 查無品號(" + itemObj.getItemCode() + ")庫號("
										+ itemObj.getDeliveryWarehouseCode() + ")及批號(" + itemObj.getLotNo() + ")之庫存資料");
							}

						} catch (RuntimeException re) {
							re.printStackTrace();
						}
					} else {
						itemObj.setItemName("批號為空白");
						itemObj.setReserve1("Err - 批號為空白");
					}
				} else {
					itemObj.setItemName("品號為空白");
					itemObj.setReserve1("Err - 品號為空白");

				}
			} else {
				itemObj.setReserve1("Err - 轉出庫別為空白");
			}
		}
	}

	/**
	 * 作廢、駁回
	 *
	 * @param headId
	 * @param employeeCode
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public Map updateImMovementToVoid(Long headId, String employeeCode, String status) throws Exception {
		log.info("updateImMovementToVoid...");
		ImMovementHead head = this.findById(headId);
		List errorMsgs = new ArrayList(0);
		Map resultMap = new HashMap();
		try {
			if (null != head) {
				String identification = MessageStatus.getIdentification(head.getBrandCode(), head.getOrderTypeCode(), head
						.getOrderNo());
				siProgramLogAction.deleteProgramLog("IM_MOVEMENT", null, identification);
				BuCommonPhraseLine orderTypeSetting = buCommonPhraseLineDAO.findById("ImMovementOrderType", head
						.getBrandCode()
						+ head.getOrderTypeCode());
				if (orderTypeSetting == null)
					throw new ValidationErrorException("查無單別資料-CommonPhraase(" + head.getOrderTypeCode() + ")，請通知系統管理員");

				if (!StringUtils.hasText(head.getCmMovementNo())) {
					this.updateMoveOnHandtoVoid(head, false, identification, errorMsgs, StringUtils.hasText(orderTypeSetting
							.getAttribute5()) ? orderTypeSetting.getAttribute5() : "NORMAL");

					if (errorMsgs.size() > 0) { // 判斷是否有錯誤訊息，如果有，則不可作廢，前端顯示錯誤訊息 by Weichun 2010.07.13
						String message = "";
						for (int i = 0; i < errorMsgs.size(); i++) {
							if ("".equals(message))
								message += errorMsgs.get(i);
							else
								message = message + "；" + errorMsgs.get(i);
						}
						throw new ValidationErrorException("調撥單作廢失敗，原因：" + message + "，請通知系統管理員。");
					}else{
						head.setStatus(status);
						head.setLastUpdateDate(new Date());
						head.setLastUpdatedBy(employeeCode);
						this.update(head);
						String resultMsg = head.getOrderTypeCode() + head.getOrderNo()
								+ (OrderStatus.VOID.equalsIgnoreCase(status) ? "存廢" : "駁回") + "成功！";
						// resultMap.put("form", head);
						resultMap.put("entityBean", head);
						resultMap.put("resultMsg", resultMsg);
						return resultMap;
					}
					// resultMap.put("", head)
				} else {
					throw new NoSuchDataException("依據調撥單：" + head.getOrderTypeCode() + head.getOrderNo() + "已移倉(移倉單號:"
							+ head.getCmMovementNo() + ")不可作廢");
				}

			} else {
				throw new NoSuchDataException("查無此調撥單(" + headId + ")");
			}
		} catch (Exception ex) {
			throw new NoSuchDataException("調撥單作廢失敗,原因：" + ex.toString());

		}
	}

	/**
	 * 作廢時，更新庫存
	 *
	 * @param imMovementHead
	 * @param allowCmStockMinus
	 * @param identification
	 * @param msgLists
	 * @param onHandMode
	 * @throws Exception
	 */
	private void updateMoveOnHandtoVoid(ImMovementHead imMovementHead, boolean allowCmStockMinus, String identification,
			List msgLists, String onHandMode) throws Exception {
		log.info("restoreMoveOnHand....");
		String user = imMovementHead.getLastUpdatedBy();
		String organizationCode = new String();
		String cmDeliveryWarehouseCode = new String();
		String cmArrivalWarehouseCode = new String();
		String lotControl = new String("N");
		Boolean disallowMinusStock = new Boolean(true);
		try {
			boolean isThreePointMoving = this.isThreePointMoving(imMovementHead.getBrandCode(), imMovementHead
					.getOrderTypeCode());
			organizationCode = UserUtils.getOrganizationCodeByBrandCode(imMovementHead.getBrandCode());
			if (!(StringUtils.hasText(organizationCode)))
				this.messageHandle(MessageStatus.LOG_ERROR, identification, "依據品牌：" + imMovementHead.getBrandCode()
						+ "查無其組織代號！", user, msgLists);

			BuBrand buBrand = buBrandDAO.findById(imMovementHead.getBrandCode());
			if (null != buBrand) {
				lotControl = StringUtils.hasText(buBrand.getLotControl()) ? buBrand.getLotControl() : "N";
			} else {
				this.messageHandle(MessageStatus.LOG_ERROR, identification, "查無品牌：" + imMovementHead.getBrandCode() + "資訊！",
						user, msgLists);
			}
			ImWarehouse imDeliveryWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class,
					imMovementHead.getDeliveryWarehouseCode());
			if (null == imDeliveryWarehouse) {
				messageHandle(MessageStatus.LOG_ERROR, identification, "查無出庫庫別(" + imMovementHead.getDeliveryWarehouseCode()
						+ ")資料", imMovementHead.getLastUpdatedBy(), msgLists);
			} else {

				cmDeliveryWarehouseCode = imDeliveryWarehouse.getCustomsWarehouseCode();
				log.info("warehouseCode=" + imMovementHead.getDeliveryWarehouseCode() + "/cmDeliveryWarehouseCode="
						+ cmDeliveryWarehouseCode);
				ImWarehouse imArrivalWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class,
						imMovementHead.getArrivalWarehouseCode());
				if (null == imArrivalWarehouse) {
					messageHandle(MessageStatus.LOG_ERROR, identification, "查無出庫庫別("
							+ imMovementHead.getArrivalWarehouseCode() + ")資料", imMovementHead.getLastUpdatedBy(), msgLists);
				} else {
					cmArrivalWarehouseCode = imArrivalWarehouse.getCustomsWarehouseCode();
					log.info("warehouseCode=" + imMovementHead.getArrivalWarehouseCode() + "/cmDeliveryWarehouseCode="
							+ cmArrivalWarehouseCode);
					boolean updateCmOnHand = TAX_MODE_FREE.equalsIgnoreCase(imMovementHead.getTaxType())
							&& !cmDeliveryWarehouseCode.equals(cmArrivalWarehouseCode);

					Map imDeliveryOnHandMaps = new HashMap();
					Map cmDeliveryOnHandMaps = new HashMap();
					Map imArrivalOnHandMaps = new HashMap();
					Map cmArrivalOnHandMaps = new HashMap();
					Double quantity = 0D;
					String lotNo = "";
					int i = 1;
					List<ImMovementItem> imMovementItems = imMovementHead.getImMovementItems();
					log.info("collect onhand info status:" + imMovementHead.getStatus());
					for (ImMovementItem imMovementItem : imMovementItems) {
						// When status of the from is WAIT_OUT, the
						// moveUncommitQty must to be subtracted
						if (OrderStatus.SIGNING.equals(imMovementHead.getStatus())
								|| OrderStatus.WAIT_OUT.equals(imMovementHead.getStatus())
								|| OrderStatus.WAIT_IN.equals(imMovementHead.getStatus())) {

							if (OrderStatus.WAIT_IN.equals(imMovementHead.getStatus())) {
								quantity = imMovementItem.getDeliveryQuantity();
							} else {
								if (isThreePointMoving)
									quantity = imMovementItem.getOriginalDeliveryQuantity();
							}

							lotNo = MOVEMENT_TYPE_ACCEPT.equalsIgnoreCase(onHandMode) ? defaultLotNo : imMovementItem
									.getLotNo().trim();
							log.info(i + ".lotNo=" + lotNo + "/quantity=" + quantity);
							this.collectImOnHand(imMovementItem, imDeliveryOnHandMaps, i, imMovementHead
									.getDeliveryWarehouseCode(), lotNo, quantity);
							if (updateCmOnHand)
								this.collectCmOnHand(imMovementItem, cmDeliveryOnHandMaps, i, cmDeliveryWarehouseCode,
										quantity);
						}

						if (OrderStatus.FINISH.equals(imMovementHead.getStatus())
								|| OrderStatus.CLOSE.equals(imMovementHead.getStatus())) {
							lotNo = "Y".equalsIgnoreCase(lotControl) ? imMovementItem.getLotNo().trim() : defaultLotNo;
							this.collectImOnHand(imMovementItem, imDeliveryOnHandMaps, i, imMovementHead
									.getDeliveryWarehouseCode(), lotNo, imMovementItem.getArrivalQuantity());
							this.collectImOnHand(imMovementItem, imArrivalOnHandMaps, i, imMovementHead
									.getArrivalWarehouseCode(), lotNo, imMovementItem.getArrivalQuantity() * -1D);
							if (updateCmOnHand) {
								this.collectCmOnHand(imMovementItem, cmDeliveryOnHandMaps, i, cmDeliveryWarehouseCode,
										imMovementItem.getArrivalQuantity());
								this.collectCmOnHand(imMovementItem, cmArrivalOnHandMaps, i, cmArrivalWarehouseCode,
										imMovementItem.getArrivalQuantity() * -1D);
							}

						}
						i++;
					}
					if (OrderStatus.SIGNING.equals(imMovementHead.getStatus())
							|| OrderStatus.WAIT_OUT.equals(imMovementHead.getStatus())
							|| OrderStatus.WAIT_IN.equals(imMovementHead.getStatus())) {
						log.info("SIGNING/WAIT_OUT/WAIT_IN...");
						this.updateImOnHandByMap(organizationCode, imMovementHead.getBrandCode(), imDeliveryOnHandMaps,
								disallowMinusStock, user, identification, msgLists);
						if (updateCmOnHand)
							this.updateCmOnHandByMap(imMovementHead.getBrandCode(), cmDeliveryOnHandMaps, allowCmStockMinus,
									user, identification, msgLists);

					}
					if (OrderStatus.FINISH.equals(imMovementHead.getStatus())) {
						log.info("FINISH...");
						this.updateImOnHandByMap(organizationCode, imMovementHead.getBrandCode(), imDeliveryOnHandMaps,
								disallowMinusStock, user, identification, msgLists);
						this.updateImOnHandByMap(organizationCode, imMovementHead.getBrandCode(), imArrivalOnHandMaps,
								disallowMinusStock, user, identification, msgLists);
						if (updateCmOnHand) {
							this.updateCmOnHandByMap(imMovementHead.getBrandCode(), cmDeliveryOnHandMaps, allowCmStockMinus,
									user, identification, msgLists);
							this.updateCmOnHandByMap(imMovementHead.getBrandCode(), cmArrivalOnHandMaps, allowCmStockMinus,
									user, identification, msgLists);
						}

					}

					if (OrderStatus.CLOSE.equals(imMovementHead.getStatus())) {
						log.info("CLOSE...");
						imTransationDAO.deleteTransationByIdentification(imMovementHead.getBrandCode(), imMovementHead
								.getOrderTypeCode(), imMovementHead.getOrderNo());

						this.updateImStockOnHandByMap(organizationCode, imMovementHead.getBrandCode(), imDeliveryOnHandMaps,
								user, identification, msgLists);
						this.updateImStockOnHandByMap(organizationCode, imMovementHead.getBrandCode(), imArrivalOnHandMaps,
								user, identification, msgLists);
						if (updateCmOnHand) {
							this.updateCmStockOnHandByMap(imMovementHead.getBrandCode(), cmDeliveryOnHandMaps, user,
									identification, msgLists);
							this.updateCmStockOnHandByMap(imMovementHead.getBrandCode(), cmArrivalOnHandMaps, user,
									identification, msgLists);
						}
					}
				}
			}
		} catch (Exception ex) {
			messageHandle(MessageStatus.LOG_ERROR, identification, ex.getMessage(), imMovementHead.getLastUpdatedBy(),
					msgLists);

		}

	}

	public List checkAcceptMovementInReceive(ImMovementHead imMovementHead, String identification, List msgLists)
			throws Exception {
		log.info("checkAcceptMovementInReceive....");
		List messageLists = new ArrayList(0);
		try {
			if (StringUtils.hasText(imMovementHead.getOriginalOrderTypeCode())
					&& StringUtils.hasText(imMovementHead.getOriginalOrderNo())) {
				String imReceiveType = getAcceptMovementType(imMovementHead.getBrandCode(), imMovementHead
						.getOriginalOrderTypeCode(), imMovementHead.getOriginalOrderNo(), imMovementHead.getOrderTypeCode(),
						imMovementHead.getOrderNo());
				if (imReceiveType != null) {
					messageLists = imMovementHeadDAO.checkMoveToReceive(imMovementHead, imReceiveType);
				} else {
					messageHandle(MessageStatus.LOG_ERROR, identification, "查無進貨單號("
							+ imMovementHead.getOriginalOrderTypeCode() + "-" + imMovementHead.getOriginalOrderNo() + ")資料",
							imMovementHead.getLastUpdatedBy(), msgLists);
				}
			} else {
				messageHandle(MessageStatus.LOG_ERROR, identification, "來源單別及單號不可為空白", imMovementHead.getLastUpdatedBy(),
						msgLists);
			}

		} catch (Exception ex) {
			messageHandle(MessageStatus.LOG_ERROR, identification, ex.getMessage(), imMovementHead.getLastUpdatedBy(),
					msgLists);
		}
		return messageLists;
	}

	public List<Properties> updatePickerData(Map parameterMap) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		log.info("updatePickerData....");
		Map returnMap = new HashMap(0);
		int startLineId = 0;
		Object otherBean = parameterMap.get("vatBeanOther");
		Object pickerBean = parameterMap.get("vatBeanPicker");
		String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
		log.info("formIdString:" + formIdString);
		String deliveryWarehouseCode = (String) PropertyUtils.getProperty(otherBean, "deliveryWarehouseCode");
		log.info("deliveryWarehouseCode:" + deliveryWarehouseCode);
		String arrivalWarehouseCode = (String) PropertyUtils.getProperty(otherBean, "arrivalWarehouseCode");
		log.info("arrivalWarehouseCode:" + arrivalWarehouseCode);
		Integer lineId = (Integer) PropertyUtils.getProperty(otherBean, "lineId");
		log.info("lineId:" + lineId);
		List<Object> pickResults = (List<Object>) PropertyUtils.getProperty(pickerBean, "imOnHandResult");
		log.info("pickResult.size:" + pickResults.size());

		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
		// Integer lineId = StringUtils.hasText(lineIdString)?
		// Integer.valueOf(lineIdString)-1:null;
		try {
			if (null != formId && null != pickResults && pickResults.size() > 0) {
				ImMovementHead head = findById(formId);
				if (null != head) {

					List<ImMovementItem> items = head.getImMovementItems();
					log.info("items.size:" + items.size());
					String itemCode = new String("");
					String lotNo = new String("");
					if (items.size() > 0 && lineId <= items.size()) {
						log.info("lineId -1 <= items.size():" + (lineId <= items.size()));
						Object firstResult = pickResults.get(0);
						itemCode = (String) PropertyUtils.getProperty(firstResult, "id_itemCode");
						lotNo = (String) PropertyUtils.getProperty(firstResult, "id_lotNo");
						log.info("itemCode:" + itemCode + "/lotNo=" + lotNo);
						ImMovementItem item = items.get(lineId - 1);
						log.info("success get item data ");
						item.setItemCode(itemCode);
						item.setDeliveryWarehouseCode(deliveryWarehouseCode);
						item.setArrivalWarehouseCode(arrivalWarehouseCode);
						item.setOriginalDeliveryQuantity(0D);
						item.setDeliveryQuantity(0D);
						item.setArrivalQuantity(0D);
						item.setOriginalDeclarationNo("");
						item.setOriginalDeclarationSeq(0L);
						item.setOriginalDeclarationDate(null);
						item.setLotNo(lotNo);
						startLineId = 1;
					}

					log.info("pickResults.size():" + pickResults.size());
					for (int i = startLineId; i < pickResults.size(); i++) {
						Object pickResult = pickResults.get(i);
						log.info("aa");
						itemCode = (String) PropertyUtils.getProperty(pickResult, "id_itemCode");
						lotNo = (String) PropertyUtils.getProperty(pickResult, "id_lotNo");
						log.info("itemCode:" + itemCode + "/lotNo=" + lotNo);
						ImMovementItem newItem = new ImMovementItem();
						newItem.setItemCode(itemCode);
						newItem.setDeliveryWarehouseCode(deliveryWarehouseCode);
						newItem.setArrivalWarehouseCode(arrivalWarehouseCode);
						newItem.setLotNo(lotNo);
						newItem.setOriginalDeliveryQuantity(0D);
						newItem.setDeliveryQuantity(0D);
						newItem.setArrivalQuantity(0D);
						newItem.setOriginalDeclarationNo("");
						newItem.setOriginalDeclarationSeq(0L);
						newItem.setOriginalDeclarationDate(null);
						items.add(newItem);
					}
					head.setImMovementItems(items);
					log.info("items.size:" + items.size());
					imMovementHeadDAO.update(head);
				}
			}

			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (IllegalAccessException iae) {
			System.out.println(iae.getMessage());
			throw new IllegalAccessException(iae.getMessage());
		} catch (InvocationTargetException ite) {
			System.out.println(ite.getMessage());
			throw new InvocationTargetException(ite, ite.getMessage());
		} catch (NoSuchMethodException nse) {
			System.out.println(nse.getMessage());
			throw new NoSuchMethodException("NoSuchMethodException:" + nse.getMessage());
		}
	}

	public List<Properties> changeItemCategory(Map parameterMap) throws Exception {
		log.info("changeItemCategory....");
		Map returnMap = new HashMap(0);
		BuLocationService buLocationService = (BuLocationService) SpringUtils.getApplicationContext().getBean(
				"buLocationService");
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");
			log.info("brandCode:" + brandCode);
			String itemCategoryMode = (String) PropertyUtils.getProperty(otherBean, "itemCategoryMode");
			log.info("itemCategoryMode:" + itemCategoryMode);
			String deliveryWarehouseCode = (String) PropertyUtils.getProperty(otherBean, "deliveryWarehouseCode");
			log.info("deliveryWarehouseCode:" + deliveryWarehouseCode);
			String arrivalWarehouseCode = (String) PropertyUtils.getProperty(otherBean, "arrivalWarehouseCode");
			log.info("arrivalWarehouseCode:" + arrivalWarehouseCode);
			String itemCategory = (String) PropertyUtils.getProperty(otherBean, "itemCategory");
			log.info("itemCategory:" + itemCategory);
			String customsDeliveryWarehouseCode = null;
			String customsArrivalWarehouseCode = null;
			String customsArrivalStoreCode = null;
			String arrivalStoreCode = (String) PropertyUtils.getProperty(otherBean, "arrivalStoreCode"); // 轉入店別 2010.07.06

			ImWarehouse deliveryWarehouse = (ImWarehouse) imWarehouseDAO.findById("ImWarehouse", deliveryWarehouseCode); // 轉出庫
			if (null != deliveryWarehouse) {
				customsDeliveryWarehouseCode = deliveryWarehouse.getCustomsWarehouseCode();
				BuLocation deliveryLocation = buLocationService.findById(deliveryWarehouse.getLocationId());
				if (null != deliveryLocation) {
					log.info("found Location..." + deliveryWarehouse.getLocationId());
					log.info(deliveryLocation.getCity() + "/" + deliveryLocation.getArea() + "/"
							+ deliveryLocation.getZipCode() + "/" + deliveryLocation.getAddress());
					returnMap.put("deliveryCity", StringUtils.hasText(deliveryLocation.getCity()) ? deliveryLocation
							.getCity() : "");
					returnMap.put("deliveryArea", StringUtils.hasText(deliveryLocation.getArea()) ? deliveryLocation
							.getArea() : "");
					returnMap.put("deliveryZipCode", StringUtils.hasText(deliveryLocation.getZipCode()) ? deliveryLocation
							.getZipCode() : "");
					returnMap.put("deliveryAddress", StringUtils.hasText(deliveryLocation.getAddress()) ? deliveryLocation
							.getAddress() : "");
					log.info(returnMap.get("deliveryCity") + "/" + returnMap.get("deliveryArea") + "/"
							+ returnMap.get("deliveryZipCode") + "/" + returnMap.get("deliveryAddress"));
				} else {

					log.info("not found Location..." + deliveryWarehouse.getLocationId());
					returnMap.put("deliveryCity", "");
					returnMap.put("deliveryArea", "");
					returnMap.put("deliveryZipCode", "");
					returnMap.put("deliveryAddress", "");
				}

			}
			log.info("customsDeliveryWarehouseCode:" + customsDeliveryWarehouseCode);

			ImWarehouse arrivalWarehouse = (ImWarehouse) imWarehouseDAO.findById("ImWarehouse", arrivalWarehouseCode); // 轉入庫
			if (null != arrivalWarehouse) {
				customsArrivalWarehouseCode = arrivalWarehouse.getCustomsWarehouseCode();
				BuLocation arrivalLocation = buLocationService.findById(arrivalWarehouse.getLocationId());
				if (null != arrivalLocation) {
					returnMap.put("arrivalCity", StringUtils.hasText(arrivalLocation.getCity()) ? arrivalLocation.getCity()
							: "");
					returnMap.put("arrivalArea", StringUtils.hasText(arrivalLocation.getArea()) ? arrivalLocation.getArea()
							: "");
					returnMap.put("arrivalZipCode", StringUtils.hasText(arrivalLocation.getZipCode()) ? arrivalLocation
							.getZipCode() : "");
					returnMap.put("arrivalAddress", StringUtils.hasText(arrivalLocation.getAddress()) ? arrivalLocation
							.getAddress() : "");
				} else {
					returnMap.put("arrivalCity", "");
					returnMap.put("arrivalArea", "");
					returnMap.put("arrivalZipCode", "");
					returnMap.put("arrivalAddress", "");
				}
			}

			// 轉入店別
			ImWarehouse arrivalStore = (ImWarehouse) imWarehouseDAO.findById("ImWarehouse", arrivalStoreCode);
			if (null != arrivalStore) {
				customsArrivalStoreCode = arrivalStore.getCustomsWarehouseCode();
			} else {
				customsArrivalStoreCode = "";
			}
			log.info("customsArrivalStoreCode ::: " + customsArrivalStoreCode);

			if ("T2".equalsIgnoreCase(brandCode)) {
				if (!StringUtils.hasText(customsDeliveryWarehouseCode))
					throw new NoSuchDataException("查無出庫庫別(" + deliveryWarehouseCode + ")的關別代號");
				if (!StringUtils.hasText(customsArrivalWarehouseCode))
					throw new NoSuchDataException("查無轉入庫別(" + arrivalWarehouseCode + ")的關別代號");
			}
			String deliveryWarehouseMangager = this.getWarehouseManager(brandCode, deliveryWarehouseCode,
					customsDeliveryWarehouseCode, itemCategoryMode, itemCategory);
			String deliveryWarehouseMangagerName = new String("");
			if (StringUtils.hasText(deliveryWarehouseMangager)) {
				deliveryWarehouseMangagerName = UserUtils.getUsernameByEmployeeCode(deliveryWarehouseMangager);
			}

			String arrivalWarehouseMangager = this.getWarehouseManager(brandCode, arrivalWarehouseCode,
					customsArrivalWarehouseCode, itemCategoryMode, itemCategory);
			String arrivalWarehouseMangagerName = new String("");
			if (StringUtils.hasText(arrivalWarehouseMangager)) {
				arrivalWarehouseMangagerName = UserUtils.getUsernameByEmployeeCode(arrivalWarehouseMangager);
			}
			log.info(returnMap.get("deliveryCity") + "/" + returnMap.get("deliveryArea") + "/"
					+ returnMap.get("deliveryZipCode") + "/" + returnMap.get("deliveryAddress"));

			returnMap.put("deliveryWarehouseMangager", deliveryWarehouseMangager);
			returnMap.put("deliveryWarehouseMangagerName", deliveryWarehouseMangagerName);
			returnMap.put("customsDeliveryWarehouseCode", customsDeliveryWarehouseCode);
			returnMap.put("arrivalWarehouseMangager", arrivalWarehouseMangager);
			returnMap.put("arrivalWarehouseMangagerName", arrivalWarehouseMangagerName);
			returnMap.put("customsArrivalWarehouseCode", customsArrivalWarehouseCode);
			returnMap.put("customsArrivalStoreCode", customsArrivalStoreCode); // 轉入店別倉庫
			returnMap.put("allowMinusStock", deliveryWarehouse.getAllowMinusStock());
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (IllegalAccessException iae) {
			System.out.println(iae.getMessage());
			throw new IllegalAccessException(iae.getMessage());
		} catch (InvocationTargetException ite) {
			System.out.println(ite.getMessage());
			throw new InvocationTargetException(ite, ite.getMessage());
		} catch (NoSuchMethodException nse) {
			System.out.println(nse.getMessage());
			throw new NoSuchMethodException(nse.getMessage());
		}
	}
    //Steve 增加TI單判別
	private void setOriginalOrder(ImMovementHead imMovementHead, String identification, List errorMsgs) throws Exception {
		// ----------------------------------------------------------------
		// Generate OriginalOrderType and OrderNo for POS data transfer user
		// ----------------------------------------------------------------
		String user = imMovementHead.getLastUpdatedBy();
		try {
			BuOrderTypeId buOrderTypeId = new BuOrderTypeId(imMovementHead.getBrandCode(), imMovementHead.getOrderTypeCode());
			BuOrderType buOrderType = buOrderTypeDAO.findById(buOrderTypeId);
			String originalOrderTypeCode = buOrderType.getNextOrderTypeCode();
			if (originalOrderTypeCode != null) {
				if (!StringUtils.hasText(imMovementHead.getOriginalOrderTypeCode())) {
					ImWarehouse imDeliveryWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class,
							imMovementHead.getDeliveryWarehouseCode());
					if (null == imDeliveryWarehouse)
						this.messageHandle(MessageStatus.LOG_ERROR, identification, "查無轉出倉別代號("
								+ imMovementHead.getDeliveryWarehouseCode(), user, errorMsgs);
					else {
						ImWarehouse imArrivalWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class,
								imMovementHead.getArrivalWarehouseCode());
						if (null == imArrivalWarehouse)
							this.messageHandle(MessageStatus.LOG_ERROR, identification, "查無轉出倉別代號("
									+ imMovementHead.getArrivalWarehouseCode(), user, errorMsgs);
						else {
							if ("S".equals(imArrivalWarehouse.getCategoryCode())) {

								String originalSerialNo = buOrderTypeService.getOrderSerialNo(imMovementHead.getBrandCode(),
										originalOrderTypeCode);
								if ("unknow".equals(originalSerialNo)) {
									this.messageHandle(MessageStatus.LOG_ERROR, identification, "取得"
											+ imMovementHead.getBrandCode() + "-" + originalOrderTypeCode + "單號失敗！", user,
											errorMsgs);
								} else {
									if ("S".equals(imDeliveryWarehouse.getCategoryCode()))
										originalOrderTypeCode = "TT";
									else if ("W".equals(imDeliveryWarehouse.getCategoryCode()))
										originalOrderTypeCode = "TR";
									imMovementHead.setOriginalOrderTypeCode(originalOrderTypeCode);
									imMovementHead.setOriginalOrderNo(originalSerialNo);
								}
                             //Add By Steve
							}else if("W".equals(imArrivalWarehouse.getCategoryCode())){
								
								String originalSerialNo = buOrderTypeService.getOrderSerialNo(imMovementHead.getBrandCode(),
										originalOrderTypeCode);
								if ("unknow".equals(originalSerialNo)) {
									this.messageHandle(MessageStatus.LOG_ERROR, identification, "取得"
											+ imMovementHead.getBrandCode() + "-" + originalOrderTypeCode + "單號失敗！", user,
											errorMsgs);
								}else{
									if ("W".equals(imDeliveryWarehouse.getCategoryCode()))
										originalOrderTypeCode = "TI";
									
									imMovementHead.setOriginalOrderTypeCode(originalOrderTypeCode);
									imMovementHead.setOriginalOrderNo(originalSerialNo);
								}
							}//Add By Steve
						}
					}
				}
			}
		} catch (Exception ex) {

		}
	}

	public List<Properties> executeModifyInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		Map multiList = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "headId");
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			ImMovementHead form = imMovementHeadDAO.findById(formId);
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(loginBrandCode, "IM");
			multiList.put("allOrderTypes", AjaxUtils
					.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
			if (null != form) {
				resultMap.put("orderTypeCode", form.getOrderTypeCode());
				resultMap.put("orderNo", form.getOrderNo());
				resultMap.put("originalDeliveryDate", DateUtils
						.format(form.getDeliveryDate(), DateUtils.C_DATE_PATTON_SLASH));
				resultMap.put("originalOrderTypeCode", StringUtils.hasText(form.getOriginalOrderTypeCode()) ? form
						.getOriginalOrderTypeCode() : "");
				resultMap.put("originalOrderNo", StringUtils.hasText(form.getOriginalOrderNo()) ? form.getOriginalOrderNo()
						: "");
			}
			resultMap.put("multiList", multiList);

		} catch (Exception ex) {
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "表單初始化失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}

		return AjaxUtils.parseReturnDataToJSON(resultMap);

	}

	public HashMap updateDate(Map parameterMap) throws Exception {
		log.info("service.updateDeliveryDate...");
		HashMap resultMap = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "headId");
			String deliveryDateString = (String) PropertyUtils.getProperty(otherBean, "deliveryDate");
			String originalOrderTypeCode = (String) PropertyUtils.getProperty(otherBean, "originalOrderTypeCode");
			String originalOrderNo = (String) PropertyUtils.getProperty(otherBean, "originalOrderNo");
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			log.info("deliveryDateString:" + deliveryDateString);
			Date deliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, deliveryDateString);
			log.info("deliveryDate:" + deliveryDate);
			ImMovementHead form = imMovementHeadDAO.findById(formId);

			if (null != form) {
				if (StringUtils.hasText(form.getCmMovementNo())) {
					throw new Exception("此調撥單已移倉(移倉單號:" + form.getCmMovementNo() + ")，取消移倉單後，方可進行修改");
				} else {
					ValidateUtil.isAfterClose(form.getBrandCode(), form.getOrderTypeCode(), "轉出日期", deliveryDate,form.getSchedule());
					form.setDeliveryDate(deliveryDate);
					form.setArrivalDate(deliveryDate);
					form.setOriginalOrderTypeCode(originalOrderTypeCode);
					form.setOriginalOrderNo(originalOrderNo);
					form.setLastUpdatedBy(loginEmployeeCode);
					form.setLastUpdateDate(new Date());

					if (OrderStatus.CLOSE.equalsIgnoreCase(form.getStatus())) {
						List<ImTransation> transactions = imTransationDAO.findTransationByIdentification(
								form.getBrandCode(), form.getOrderTypeCode(), form.getOrderNo());
						if (transactions.size() > 0) {
							for (ImTransation transaction : transactions) {
								transaction.setTransationDate(deliveryDate);
								imTransationDAO.update(transaction);
							}
						} else {
							throw new Exception("更新調撥交易資料時發生錯誤，原因:查無交易資料(" + form.getBrandCode() + "/"
									+ form.getOrderTypeCode() + "/" + form.getOrderNo() + ")");
						}

					}
					this.update(form);
				}
			} else {
				throw new Exception("查無調撥單資料(" + formIdString + ")");
			}

		} catch (FormException ex) {
			throw new Exception(ex.toString());
		} catch (Exception ex) {
			throw new Exception(ex.toString());

		}
		return resultMap;

	}

	public List<ImMovementHead> executeBatchImportT2(List<ImMovementHead> imMovementHeads) throws Exception {
		List<ImMovementHead> newMovementHeads = new ArrayList<ImMovementHead>(0);
		try {
			log.info("executeBatchImportT2");
			
			if (imMovementHeads != null) {
				for (int i = 0; i < imMovementHeads.size(); i++) {
					ImMovementHead movementHead = (ImMovementHead) imMovementHeads.get(i);
					BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(movementHead.getBrandCode(), movementHead.getOrderTypeCode()));
					BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("ImMovementOrderType", movementHead.getBrandCode() + movementHead.getOrderTypeCode());
					if (null == buOrderType)
						throw new Exception("取得 " + movementHead.getBrandCode() + " " + movementHead.getOrderTypeCode() + " 的單別失敗！");
					if (null == buCommonPhraseLine)
						throw new Exception("取得 " + movementHead.getBrandCode() + " " + movementHead.getOrderTypeCode() +
								" 的ImMovementOrderType(BuCommonPhraseLine)失敗！");

					if (!StringUtils.hasText(movementHead.getOrderNo())) {
						log.info("insertMovementBatch");
						insertMovementBatch(movementHead, buOrderType, buCommonPhraseLine);
						newMovementHeads.add(movementHead);
					} else {
						log.info("modifyMovementBatch");
						if ("THREE".equals(buOrderType.getMoveFlowType()) || "TWO".equals(buOrderType.getMoveFlowType())
								&& "Y".equals(buCommonPhraseLine.getReserve2())) {
							modifyMovementBatch(movementHead);
						}
					}
				}
			}
		} catch (Exception ex) {
			log.error("執行調撥資料批次匯入失敗，原因：" + ex.toString());
			throw ex;
		}
		return newMovementHeads;
	}
	
	public List<ImMovementHead> executeBatchImportT2hw(List<ImMovementHead> imMovementHeads) throws Exception {
		List<ImMovementHead> newMovementHeads = new ArrayList<ImMovementHead>(0);
		try {
			log.info("executeBatchImportT2hw");
			
			if (imMovementHeads != null) {
				for (int i = 0; i < imMovementHeads.size(); i++) {
					ImMovementHead movementHead = (ImMovementHead) imMovementHeads.get(i);
					BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(movementHead.getBrandCode(), movementHead.getOrderTypeCode()));
					BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("ImMovementOrderType", movementHead.getBrandCode() + movementHead.getOrderTypeCode());
					if (null == buOrderType)
						throw new Exception("取得 " + movementHead.getBrandCode() + " " + movementHead.getOrderTypeCode() + " 的單別失敗！");
					if (null == buCommonPhraseLine)
						throw new Exception("取得 " + movementHead.getBrandCode() + " " + movementHead.getOrderTypeCode() +
								" 的ImMovementOrderType(BuCommonPhraseLine)失敗！");

					if (!StringUtils.hasText(movementHead.getOrderNo())) {
						log.info("insertMovementBatch");
						//insertMovementBatch(movementHead, buOrderType, buCommonPhraseLine);
						//newMovementHeads.add(movementHead);
					} else {
						log.info("modifyMovementBatch");
						if ("THREE".equals(buOrderType.getMoveFlowType()) || "TWO".equals(buOrderType.getMoveFlowType())
								&& "Y".equals(buCommonPhraseLine.getReserve2())) {
							modifyMovementBatchhw(movementHead,buCommonPhraseLine);
						}
					}
				}
			}
		} catch (Exception ex) {
			log.error("執行調撥資料批次匯入失敗，原因：" + ex.toString());
			throw ex;
		}
		return newMovementHeads;
	}

	public void insertMovementBatch(ImMovementHead saveObj, BuOrderType buOrderType, BuCommonPhraseLine buCommonPhraseLine)
			throws Exception {
		try {
			String serialNo = buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), saveObj.getOrderTypeCode());
			if ("unknow".equals(serialNo))
				throw new Exception("取得" + saveObj.getBrandCode() + "-" + saveObj.getOrderTypeCode() + "單號失敗！");
			saveObj.setTaxType(buOrderType.getTaxCode());
			saveObj.setOrderNo(serialNo);
			saveObj.setStatus(OrderStatus.SAVE);
			saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
			saveObj.setCreationDate(saveObj.getLastUpdateDate());
			if ("THREE".equals(buOrderType.getMoveFlowType()) || "TWO".equals(buOrderType.getMoveFlowType())
					&& "Y".equals(buCommonPhraseLine.getReserve2())) {
				resetLineBatch(saveObj, buOrderType);
			}
			imMovementHeadDAO.save(saveObj);
		} catch (Exception e) {
			siProgramLogAction.createProgramLog(MOVEMENT_IMPORT, MessageStatus.LOG_ERROR, "ImMovementImportDataT2", e.getMessage(), saveObj.getLastUpdatedBy());
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	public void modifyMovementBatch(ImMovementHead obj) throws Exception {
		try {
			ImMovementHead head = findMovementByIdentification(obj.getBrandCode(), obj.getOrderTypeCode(), obj.getOrderNo());
			Map aggregateQuantitys = null;
			if (head == null) {
				throw new Exception("調撥單別" + obj.getOrderTypeCode() + "單號" + obj.getOrderNo() + "查無此調撥單");
			} else {
				List<ImMovementItem> importItems = obj.getImMovementItems();
				if (null != importItems && importItems.size() == 0)
					throw new Exception("調撥單別" + obj.getOrderTypeCode() + "單號" + obj.getOrderNo() + "查無輸入明細項目");
				ImMovementItem importItem = importItems.get(0);
				boolean haveBoxNo = NumberUtils.getLong(importItem.getBoxNo()) > 0;
				if (OrderStatus.SAVE.equals(head.getStatus())) {
					if (haveBoxNo) {
						// 帶箱號
						log.info("帶箱號");
						setMovementItemQuantity(head, obj);
					} else if (OrderStatus.WAIT_OUT.equals(head.getStatus())) {
						// 不帶箱號
						log.info("不帶箱號");
						aggregateQuantitys = getAggregateDeliveryQuantitys(obj);
						aggregateLineBatchWAIT_OUT(head, aggregateQuantitys);
					}
					head.setReserve2(obj.getReserve2());
					head.setComfirmedBy(obj.getPackedBy());
					head.setLastUpdatedBy(obj.getLastUpdatedBy());
					imMovementHeadDAO.update(head);
				} else {
					throw new Exception("調撥單別" + obj.getOrderTypeCode() + "單號" + obj.getOrderNo() + "狀態不為暫存");
				}
				/*
				 * if(OrderStatus.SAVE.equals(head.getStatus())){
				 * log.info("SAVE"); head.setImMovementItems(importItems);
				 * head.setPackedBy(obj.getLastUpdatedBy());
				 * head.setLastUpdatedBy(obj.getLastUpdatedBy());
				 * resetLineBatch(head); imMovementHeadDAO.update(head); //帶箱號
				 * }else if(OrderStatus.WAIT_OUT.equals(head.getStatus()) &&
				 * haveBoxNo){ setMovementItemQuantity(head, obj);
				 * head.setComfirmedBy(obj.getLastUpdatedBy());
				 * head.setLastUpdatedBy(obj.getLastUpdatedBy());
				 * imMovementHeadDAO.update(head); //帶箱號 }else
				 * if(OrderStatus.WAIT_IN.equals(head.getStatus()) &&
				 * haveBoxNo){ setMovementItemQuantity(head, obj);
				 * head.setComfirmedBy(obj.getLastUpdatedBy());
				 * head.setLastUpdatedBy(obj.getLastUpdatedBy());
				 * imMovementHeadDAO.update(head); //無箱號 }else
				 * if(OrderStatus.WAIT_OUT.equals(head.getStatus())){
				 * log.info("WAIT_OUT"); aggregateQuantitys =
				 * getAggregateDeliveryQuantitys(obj);
				 * head.setComfirmedBy(obj.getLastUpdatedBy());
				 * head.setLastUpdatedBy(obj.getLastUpdatedBy());
				 * aggregateLineBatchWAIT_OUT(head, aggregateQuantitys);
				 * imMovementHeadDAO.update(head); //無箱號 }else
				 * if(OrderStatus.WAIT_IN.equals(head.getStatus())){
				 * log.info("WAIT_IN"); aggregateQuantitys =
				 * getAggregateArrivalQuantitys(obj);
				 * head.setReceiptedBy(obj.getLastUpdatedBy());
				 * head.setLastUpdatedBy(obj.getLastUpdatedBy());
				 * aggregateLineBatchWAIT_IN(head, aggregateQuantitys);
				 * imMovementHeadDAO.update(head); }
				 */
			}
		} catch (Exception e) {
			siProgramLogAction.createProgramLog(MOVEMENT_IMPORT, MessageStatus.LOG_ERROR, "ImMovementImportDataT2", e
					.getMessage(), obj.getLastUpdatedBy());
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	public enum CheckOrderType {
	    WTF,WFF,WGF,WHF,WTP,WFP,WGP,WHP,YTR
	}
	
	public void modifyMovementBatchhw(ImMovementHead obj, BuCommonPhraseLine buCommonPhraseLine) throws Exception {
		System.out.println("obj.getOrderNo():"+obj.getOrderNo());
		System.out.println("obj.getLastUpdateDate():"+obj.getLastUpdateDate());
		System.out.println("obj.getPackedBy():"+obj.getPackedBy());
		//System.out.println("babu覆核人員:"+sourceFileName);
		System.out.println("obj.getStatus():"+obj.getStatus());
		
		
		
		boolean allowCheck = false;
		String orderTypeCode = obj.getOrderTypeCode();
		
		try {
			CheckOrderType checkType = CheckOrderType.valueOf(orderTypeCode);
			switch(checkType){
			
			   case WTF:
			   allowCheck = true;
			   break;
			   
			   case WFF:
			   allowCheck = true;
			   break;
				   
			   case WGF:
			   allowCheck = true;
			   break;
				   
			   case WHF:
			   allowCheck = true;
			   break;
				   
			   case WTP:
			   allowCheck = true;
			   break;
				   
			   case WFP:
			   allowCheck = true;
			   break;
				   
			   case WGP:
			   allowCheck = true;
			   break;
				   
			   case WHP:
			   allowCheck = true;
			   break;
				   
			   case YTR:
			   allowCheck = true;
			   break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		try {
			ImMovementHead head = findMovementByIdentification(obj.getBrandCode(), obj.getOrderTypeCode(), obj.getOrderNo());
			System.out.println("babu1000第二次:"+head.getStatus());
			Map aggregateQuantitys = null;
			
			if (head == null) {
				throw new Exception("調撥單別" + obj.getOrderTypeCode() + "單號" + obj.getOrderNo() + "查無此調撥單");
			} else {
				Double comfirmedTimes;
				comfirmedTimes = head.getWhComfirmedTimes();
				if(comfirmedTimes==null){
					comfirmedTimes=0.0;
				}
				
				List<ImMovementItem> importItems = obj.getImMovementItems();
				if (null != importItems && importItems.size() == 0)
					throw new Exception("調撥單別" + obj.getOrderTypeCode() + "單號" + obj.getOrderNo() + "查無輸入明細項目");
				ImMovementItem importItem = importItems.get(0);
				boolean haveBoxNo = NumberUtils.getLong(importItem.getBoxNo()) > 0;
				if (OrderStatus.SAVE.equals(head.getStatus())) {
					if (haveBoxNo) {
						// 帶箱號
						log.info("帶箱號");
						setMovementItemQuantity(head, obj);
					} else if (OrderStatus.WAIT_OUT.equals(head.getStatus())) {
						// 不帶箱號
						log.info("不帶箱號");
						aggregateQuantitys = getAggregateDeliveryQuantitys(obj);
						aggregateLineBatchWAIT_OUT(head, aggregateQuantitys);
					}
					head.setReserve2(obj.getReserve2());
					head.setComfirmedBy(obj.getPackedBy());
					head.setLastUpdatedBy(obj.getLastUpdatedBy());
					imMovementHeadDAO.update(head);
				} else if(allowCheck==true){ 
					if(buCommonPhraseLine.getReserve4()!="Y"){
				  	  System.out.println("覆核7");
					  	if(head.getStatus().equals("FINISH")||head.getStatus().equals("CLOSE")){
							  System.out.println("覆核1");
							
								System.out.println("覆核2");
								if(buCommonPhraseLine.getReserve5().equals("Y")){
									System.out.println("覆核3");
									setMovementItemQuantityHw(head, obj);
								}else{
									System.out.println("覆核4");
									//aggregateQuantitys = getAggregateDeliveryQuantitys(obj);
									//aggregateLineBatchWAIT_OUT(head, aggregateQuantitys);
								}
								comfirmedTimes++;
							  	head.setWhComfirmedDate(obj.getLastUpdateDate());
							  	head.setWhComfirmedBy(obj.getPackedBy());
							  //	head.setWhImportText(sourceFileName);
							  	head.setWhComfirmedTimes(comfirmedTimes);
							  	imMovementHeadDAO.update(head);
							System.out.println("覆核5");
						  }
				    }
				}else{
					throw new Exception("調撥單別" + obj.getOrderTypeCode() + "單號" + obj.getOrderNo() + "狀態不為暫存或是此單據不可覆核");
				}
				
			}
		} catch (Exception e) {
			siProgramLogAction.createProgramLog(MOVEMENT_IMPORT, MessageStatus.LOG_ERROR, "ImMovementImportDataT2", e
					.getMessage(), obj.getLastUpdatedBy());
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	public void resetLineBatch(ImMovementHead obj, BuOrderType buOrderType) {
		List<ImMovementItem> resetObj = obj.getImMovementItems();
		for (ImMovementItem item : resetObj) {
			item.setOriginalDeliveryQuantity(item.getDeliveryQuantity());
			item.setDeliveryQuantity(0D);
		}
	}

	public void setMovementItemQuantity(ImMovementHead head, ImMovementHead obj) throws Exception {
		Map importMap = new HashMap();
		List<ImMovementItem> importItems = obj.getImMovementItems();
		// ImMovementItem importItem = importItems.get(0);
		List<ImMovementItem> items = head.getImMovementItems();

		for (Iterator iterator = importItems.iterator(); iterator.hasNext();) {
			ImMovementItem importItem = (ImMovementItem) iterator.next();
			String key = importItem.getItemCode() + "#" + importItem.getBoxNo();
			if (null == importMap.get(key))
				importMap.put(key, NumberUtils.getDouble(importItem.getDeliveryQuantity()));
			else
				importMap.put(key, (Double) importMap.get(key) + NumberUtils.getDouble(importItem.getDeliveryQuantity()));
			importMap.put(key+SCAN_CODE, importItem.getScanCode());
		}

		String itemCode = null;
		Long boxNo = null;
		Double quantity = null;
		String key = null;

		for (int i = 0; i < items.size(); i++) {
			boolean haveNext = false;
			ImMovementItem item = items.get(i);
			// 取出屬於該物品的品號
			itemCode = item.getItemCode();
			// log.info("itemCode = "+itemCode);
			// 取出屬於該物品的箱號
			boxNo = item.getBoxNo();
			// log.info("boxNo = "+boxNo);
			key = itemCode + "#" + boxNo;
			// log.info("key = "+key);
			quantity = (Double) importMap.get(key);
			// log.info("quantity = "+quantity);
			if (null == quantity || quantity == 0) {
				item.setDeliveryQuantity(0D);
				// throw new
				// Exception("調撥單別"+obj.getOrderTypeCode()+"單號"+obj.getOrderNo()+
				// "明細品號"+item.getItemCode()+"箱號"+item.getBoxNo()+"與匯入的明細不符");
				continue;
			} else {
				// quantity = importItem.getDeliveryQuantity();
				// 判斷相同品號箱號是否在調撥單裡面重出現
				for (int ii = i; ii < items.size() - 1; ii++) {
					ImMovementItem item_bak = items.get(ii + 1);
					if (itemCode.equals(item_bak.getItemCode()) && boxNo.longValue() == item_bak.getBoxNo().longValue()) {
						haveNext = true;
						break;
					}
				}

				// 如果有多個數量要複合
				if (haveNext) {
					log.info("haveNext");
					// log.info("quantity = " + quantity);
					// if(OrderStatus.WAIT_OUT.equals(head.getStatus())){
					if (quantity - item.getOriginalDeliveryQuantity() > 0) {
						quantity -= item.getOriginalDeliveryQuantity();
						item.setDeliveryQuantity(item.getOriginalDeliveryQuantity());
					} else {
						item.setDeliveryQuantity(quantity);
						quantity = 0D;
					}
					/*
					 * }else if(OrderStatus.WAIT_IN.equals(head.getStatus())){
					 * if(quantity - item.getDeliveryQuantity() > 0){ quantity -=
					 * item.getDeliveryQuantity();
					 * item.setArrivalQuantity(item.getDeliveryQuantity());
					 * }else{ item.setArrivalQuantity(quantity); quantity = 0D; } }
					 */
					// log.info("quantity = " + quantity);
					importMap.put(key, quantity);
				} else {
					// if(OrderStatus.WAIT_OUT.equals(head.getStatus()))
					item.setDeliveryQuantity(quantity);
					importMap.remove(key);
					importMap.remove(key+SCAN_CODE);
					// log.info("importMap.remove " + key);
					// else if(OrderStatus.WAIT_IN.equals(head.getStatus()))
					// item.setArrivalQuantity(quantity);
				}
			}
		}

		createLineBatchWAIT_OUT(head, items, importMap);
		// log.info("createLineBatchWAIT_OUT FINISH");
	}
	
	public void setMovementItemQuantityHw(ImMovementHead head, ImMovementHead obj) throws Exception {
		Map importMap = new HashMap();
		Map tmp = new HashMap();
		String status = head.getStatus();
		System.out.println("XXXX:"+status);
		List<ImMovementItem> importItems = obj.getImMovementItems();
		// ImMovementItem importItem = importItems.get(0);
		List<ImMovementItem> items = head.getImMovementItems();
		//Double[] whComfirmedQuantity = new Double[items.size()];
		Map whComfirmedQuantity = new HashMap();
		int z = 0;

        for (Iterator iterator = importItems.iterator(); iterator.hasNext();z++) {
			ImMovementItem importItem = (ImMovementItem) iterator.next();
			String key = importItem.getItemCode() + "#" + importItem.getBoxNo();
			if (null == importMap.get(key)){
				importMap.put(key, NumberUtils.getDouble(importItem.getDeliveryQuantity()));
			    System.out.println(key+"-"+NumberUtils.getDouble(importItem.getDeliveryQuantity()));
			    System.out.println("T11:"+importItem.getDeliveryQuantity());
			    whComfirmedQuantity.put(importItem.getItemCode(), NumberUtils.getDouble(importItem.getDeliveryQuantity()));
			    //whComfirmedQuantity[z] = NumberUtils.getDouble(importItem.getDeliveryQuantity());
			}else{
				importMap.put(key, (Double) importMap.get(key) + NumberUtils.getDouble(importItem.getDeliveryQuantity()));
			    System.out.println(key+"-"+(Double) importMap.get(key) + NumberUtils.getDouble(importItem.getDeliveryQuantity()));
			    System.out.println("T12");
			    whComfirmedQuantity.put(importItem.getItemCode(), NumberUtils.getDouble(importItem.getDeliveryQuantity()));
			    //whComfirmedQuantity[z] = NumberUtils.getDouble(importItem.getDeliveryQuantity());
			}
		}

		String itemCode = null;
		Long boxNo = null;
		Double quantity = null;
		Double quantity2 = null;
		String key = null;
        
		
		for (int i = 0; i < items.size(); i++) {
			System.out.println("Items.size:"+items.size());
			boolean haveNext = false;
			ImMovementItem item = items.get(i);
			// 取出屬於該物品的品號
			itemCode = item.getItemCode();
			 log.info("itemCode = "+itemCode);
			// 取出屬於該物品的箱號
			boxNo = item.getBoxNo();
			// log.info("boxNo = "+boxNo);
			key = itemCode + "#" + boxNo;
			// log.info("key = "+key);
			quantity = (Double) importMap.get(key);
			quantity2 = (Double) whComfirmedQuantity.get(key);
			 log.info("quantity2 = "+quantity);
			if (null == quantity || quantity == 0) {
				log.info("bbb1");
			 if(status.equals("SAVE")){	
				 log.info("BBBB1");
				item.setDeliveryQuantity(0D);
			 }
			 
			 if (null == quantity2 || quantity2 == 0) {
					log.info("bbbb1");
				if(status.equals("FINISH")||status.equals("CLOSE")){	
					log.info("BBBB2");
					item.setWhComfirmedQuantity(0D);
				}	
			 }
			 // throw new
				// Exception("調撥單別"+obj.getOrderTypeCode()+"單號"+obj.getOrderNo()+
				// "明細品號"+item.getItemCode()+"箱號"+item.getBoxNo()+"與匯入的明細不符");
				continue;
			} else {
				log.info("bbb2:"+items.size());
				// quantity = importItem.getDeliveryQuantity();
				// 判斷相同品號箱號是否在調撥單裡面重出現
				for (int ii = i; ii < items.size() - 1; ii++) {
					ImMovementItem item_bak = items.get(ii + 1);
					if (itemCode.equals(item_bak.getItemCode()) && boxNo.longValue() == item_bak.getBoxNo().longValue()) {
						haveNext = true;
						break;
					}
				}
				log.info("bbb3");
				// 如果有多個數量要複合
				if(status.equals("SAVE")){
				 if (haveNext) {
					log.info("haveNextbabu");
					// log.info("quantity = " + quantity);
					// if(OrderStatus.WAIT_OUT.equals(head.getStatus())){
					if (quantity - item.getOriginalDeliveryQuantity() > 0) {
						quantity -= item.getOriginalDeliveryQuantity();
						item.setDeliveryQuantity(item.getOriginalDeliveryQuantity());
						//item.setWhComfirmedQuantity(item.getOriginalDeliveryQuantity());
					} else {
						item.setDeliveryQuantity(quantity);
						//item.setWhComfirmedQuantity(item.getOriginalDeliveryQuantity());
						quantity = 0D;
					}
					
					
					// * }else if(OrderStatus.WAIT_IN.equals(head.getStatus())){
					// * if(quantity - item.getDeliveryQuantity() > 0){ quantity -=
					// * item.getDeliveryQuantity();
					// * item.setArrivalQuantity(item.getDeliveryQuantity());
					// * }else{ item.setArrivalQuantity(quantity); quantity = 0D; } }
					 
					// log.info("quantity = " + quantity);
					importMap.put(key, quantity);
				 }
				}else if(status.equals("FINISH")||status.equals("CLOSE")){
					log.info("進入比對1111");
					Iterator iterator = whComfirmedQuantity.entrySet().iterator();
					String[] itQty = {};
					while(iterator.hasNext()){ 
					Object o = iterator.next(); 
					String iCode = o.toString(); 
					//System.out.println("ICODE:"+iCode);
					itQty =  iCode.split("=");
					
					
					
					
					if(items.get(i).getItemCode().equals(itQty[0])){
						//item.setWhComfirmedQuantity((Double)whComfirmedQuantity.get(iCode));
						//log.info("進入比對2222");
						if (haveNext) {
							//log.info("haveNext");
							// log.info("quantity = " + quantity);
							// if(OrderStatus.WAIT_OUT.equals(head.getStatus())){
							if (quantity - item.getDeliveryQuantity() > 0) {
								
								quantity -= item.getDeliveryQuantity();
								//item.setDeliveryQuantity(item.getOriginalDeliveryQuantity());
								item.setWhComfirmedQuantity(item.getDeliveryQuantity());
								//whComfirmedQuantity.put(key, quantity);
								
							} else {
								//log.info("haveNext2");
								//item.setDeliveryQuantity(quantity);
								item.setWhComfirmedQuantity(quantity);
								quantity = 0D;
								
							}
							//whComfirmedQuantity.put(key, quantity);
							importMap.put(key, quantity);
							log.info("haveNext1(扣):"+quantity+" importMap的數量:"+importMap.get(key));
						}else{
							//System.out.println("品號以及覆核數量:"+iCode);
							item.setWhComfirmedQuantity(quantity);
							importMap.remove(key);
						}
					}
					}
					
					//whComfirmedQuantity.remove(key);
					
				}
				/*
				else {
					log.info("bbb4");
					// if(OrderStatus.WAIT_OUT.equals(head.getStatus()))
					item.setDeliveryQuantity(quantity);
					importMap.remove(key);
					// log.info("importMap.remove " + key);
					// else if(OrderStatus.WAIT_IN.equals(head.getStatus()))
					// item.setArrivalQuantity(quantity);
				}*/
			}
		}
		
			createLineBatchWAIT_OUT_MOD(head, items, importMap);
		
		// log.info("createLineBatchWAIT_OUT FINISH");
	}
	
	public void createLineBatchWAIT_OUT_MOD(ImMovementHead head, List<ImMovementItem> items, Map importMap) throws Exception {
		// log.info("createLineBatchWAIT_OUT S");
		// 如果有多的品號則新增
		Long indexNo = Long.valueOf(items.size()) + 1;
		Set keys = importMap.keySet();
		StringBuffer errItems = new StringBuffer();
		// log.info("keys.size() = " + keys.size());
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			// log.info("indexNo = " + indexNo);
			Object object = (Object) iterator.next();
			Double quantity = (Double) importMap.get(object);
			// log.info("quantity = " + quantity);
			String itemCode = object.toString();
			Long boxNo = null;
			// log.info("object.toString() = " + object.toString());
			if (itemCode.indexOf("#") > 0) {
				// log.info("itemCode.substring(itemCode.indexOf(#)+1) = " +
				// itemCode.substring(itemCode.indexOf("#")+1));
				boxNo = NumberUtils.getLong(itemCode.substring(itemCode.indexOf("#") + 1));
				itemCode = itemCode.substring(0, itemCode.indexOf("#"));
			}
			
				
				//errItems.append("箱號:"+boxNo+" 品號:"+itemCode+"不在調撥單品項內!!\n");
			errItems.append("調撥單:"+head.getOrderTypeCode()+"-"+head.getOrderNo()+" 箱號:"+boxNo+" 品號:"+itemCode+"不在調撥單品項內!!\n");
			
		}
		if(errItems.length()>0){
			throw new Exception(errItems.toString());
		}
		
		// log.info("createLineBatchWAIT_OUT D");
	}

	public Map getAggregateDeliveryQuantitys(ImMovementHead obj) {
		Map aggregates = new HashMap();
		List<ImMovementItem> objItems = obj.getImMovementItems();
		for (Iterator iterator = objItems.iterator(); iterator.hasNext();) {
			ImMovementItem imMovementItem = (ImMovementItem) iterator.next();
			if (null == aggregates.get(imMovementItem.getItemCode()))
				aggregates.put(imMovementItem.getItemCode(), NumberUtils.getDouble(imMovementItem.getDeliveryQuantity()));
			else
				aggregates.put(imMovementItem.getItemCode(), (Double) aggregates.get(imMovementItem.getItemCode())
						+ NumberUtils.getDouble(imMovementItem.getDeliveryQuantity()));
			aggregates.put(imMovementItem.getItemCode()+SCAN_CODE, imMovementItem.getScanCode());
		}
		return aggregates;
	}

	public Map getAggregateArrivalQuantitys(ImMovementHead obj) {
		Map aggregates = new HashMap();
		List<ImMovementItem> objItems = obj.getImMovementItems();
		for (Iterator iterator = objItems.iterator(); iterator.hasNext();) {
			ImMovementItem imMovementItem = (ImMovementItem) iterator.next();
			if (null == aggregates.get(imMovementItem.getItemCode()))
				aggregates.put(imMovementItem.getItemCode(), NumberUtils.getDouble(imMovementItem.getArrivalQuantity()));
			else
				aggregates.put(imMovementItem.getItemCode(), (Double) aggregates.get(imMovementItem.getItemCode())
						+ NumberUtils.getDouble(imMovementItem.getArrivalQuantity()));
		}
		return aggregates;
	}

	public void aggregateLineBatchWAIT_OUT(ImMovementHead head, Map aggregates) throws Exception {
		log.info("aggregateLineBatchWAIT_OUT");
		List<ImMovementItem> items = head.getImMovementItems();
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			ImMovementItem item = (ImMovementItem) iterator.next();
			// 實際出貨量
			Double aggregateQuantity = NumberUtils.getDouble((Double) aggregates.get(item.getItemCode()));
			// 預定出貨量
			Double originalDeliveryQuantity = NumberUtils.getDouble(item.getOriginalDeliveryQuantity());
			if (aggregateQuantity - originalDeliveryQuantity > 0) {
				item.setDeliveryQuantity(originalDeliveryQuantity);
				aggregates.put(item.getItemCode(), aggregateQuantity - originalDeliveryQuantity);
			} else if (aggregateQuantity - originalDeliveryQuantity == 0) {
				item.setDeliveryQuantity(originalDeliveryQuantity);
				aggregates.remove(item.getItemCode());
				aggregates.remove(item.getItemCode()+SCAN_CODE);
			} else {
				item.setDeliveryQuantity(aggregateQuantity);
				aggregates.remove(item.getItemCode());
				aggregates.remove(item.getItemCode()+SCAN_CODE);
			}
		}
		createLineBatchWAIT_OUT(head, items, aggregates);
	}

	/**
	 * 盤點完若有多餘的品號箱號則新增
	 */
	public void createLineBatchWAIT_OUT(ImMovementHead head, List<ImMovementItem> items, Map importMap) throws Exception {
		// log.info("createLineBatchWAIT_OUT S");
		// 如果有多的品號則新增
		Long indexNo = Long.valueOf(items.size()) + 1;
		Set keys = importMap.keySet();
		// log.info("keys.size() = " + keys.size());
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			// log.info("indexNo = " + indexNo);
			Object object = (Object) iterator.next();
			if(object.toString().indexOf(SCAN_CODE) < 0){
				Double quantity = (Double) importMap.get(object);
				// log.info("quantity = " + quantity);
				String itemCode = object.toString();
				Long boxNo = null;
				// log.info("object.toString() = " + object.toString());
				if (itemCode.indexOf("#") > 0) {
					// log.info("itemCode.substring(itemCode.indexOf(#)+1) = " +
					// itemCode.substring(itemCode.indexOf("#")+1));
					boxNo = NumberUtils.getLong(itemCode.substring(itemCode.indexOf("#") + 1));
					itemCode = itemCode.substring(0, itemCode.indexOf("#"));
				}
				ImMovementItem imMovementItem = new ImMovementItem();
				imMovementItem.setItemCode(itemCode);
				imMovementItem.setScanCode((String)importMap.get(object.toString()+SCAN_CODE));
				// log.info("itemCode = " + itemCode);
				imMovementItem.setBoxNo(boxNo);
				// log.info("boxNo = " + boxNo);
				imMovementItem.setDeliveryQuantity((Double) importMap.get(object));
				imMovementItem.setOriginalDeliveryQuantity(0D);
				// log.info("0");
				imMovementItem.setArrivalWarehouseCode(head.getArrivalWarehouseCode());
				imMovementItem.setDeliveryWarehouseCode(head.getDeliveryWarehouseCode());
				// log.info("1");
				if (null == imMovementItem.getLotNo())
					imMovementItem.setLotNo(SystemConfig.LOT_NO);
				imMovementItem.setIndexNo(indexNo++);
				log.info("SET-imMovementItemIndexNo:"+indexNo);
				// log.info("2");
				imMovementItem.setImMovementHead(head);
				items.add(imMovementItem);
			}
		}
		head.setImMovementItems(items);
		// log.info("createLineBatchWAIT_OUT D");
	}

	public void aggregateLineBatchWAIT_IN(ImMovementHead head, Map aggregates) throws Exception {
		List<ImMovementItem> items = head.getImMovementItems();
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			ImMovementItem item = (ImMovementItem) iterator.next();
			// 到貨量
			Double aggregateQuantity = NumberUtils.getDouble((Double) aggregates.get(item.getItemCode()));
			// 實際出貨量
			Double deliveryQuantity = NumberUtils.getDouble(item.getDeliveryQuantity());
			if (aggregateQuantity - deliveryQuantity > 0) {
				item.setArrivalQuantity(deliveryQuantity);
				aggregates.put(item.getItemCode(), aggregateQuantity - deliveryQuantity);
			} else if (aggregateQuantity - deliveryQuantity == 0) {
				item.setArrivalQuantity(deliveryQuantity);
				aggregates.remove(item.getItemCode());
			} else {
				item.setArrivalQuantity(aggregateQuantity);
				aggregates.remove(item.getItemCode());
			}
		}
		Set keys = aggregates.keySet();
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			// throw new
			// Exception("調撥單別"+head.getOrderTypeCode()+"單號"+head.getOrderNo()+
			// "明細中多出商品品號:" + object.toString() + "之資訊，與原調撥單不符");
			ImMovementItem imMovementItem = new ImMovementItem();
			imMovementItem.setItemCode(object.toString());
			imMovementItem.setArrivalQuantity((Double) aggregates.get(object));
			imMovementItem.setDeliveryQuantity(0D);
			imMovementItem.setOriginalDeliveryQuantity(0D);
			imMovementItem.setArrivalWarehouseCode(head.getArrivalWarehouseCode());
			imMovementItem.setDeliveryWarehouseCode(head.getDeliveryWarehouseCode());
			if (null == imMovementItem.getLotNo())
				imMovementItem.setLotNo(SystemConfig.LOT_NO);
			// imMovementItem.setIndexNo(indexNo++);
			imMovementItem.setImMovementHead(head);
			items.add(imMovementItem);
		}
		head.setImMovementItems(items);
	}

	public void updateLineBatch(ImMovementHead saveObj) {
		List<ImMovementItem> resetObj = saveObj.getImMovementItems();
		BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(saveObj.getBrandCode(), saveObj
				.getOrderTypeCode()));
		if ("THREE".equals(buOrderType.getMoveFlowType())) {
			for (ImMovementItem item : resetObj) {
				item.setOriginalDeliveryQuantity(item.getDeliveryQuantity());
				item.setDeliveryQuantity(null);
			}
		}
	}

	public List<Properties> updateOriginalQtyToArrival(Map parameterMap) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		System.out.println("assignOriginalQtyToArrival....");
		Map returnMap = new HashMap(0);
		int startLineId = 0;
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			log.info("formIdString:" + formIdString);
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;

			if (null != formId) {
				ImMovementHead head = findById(formId);
				if (null != head) {
					List<ImMovementItem> items = head.getImMovementItems();
					System.out.println("自訂事件更新前..總數量為:"+head.getItemCount());
					for (ImMovementItem item : items) {
						item.setDeliveryQuantity(item.getOriginalDeliveryQuantity());
						log.info(item.getIndexNo() + ".itemCode=" + item.getItemCode() + "OriginalDeliveryQuantity="
								+ item.getOriginalDeliveryQuantity() + "/DeliveryQuantity=" + item.getDeliveryQuantity());
					}
					imMovementHeadDAO.update(head);
					System.out.println("自訂事件更新結束..總數量為:"+head.getItemCount());
				}
			}

			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (IllegalAccessException iae) {
			System.out.println(iae.getMessage());
			throw new IllegalAccessException(iae.getMessage());
		} catch (InvocationTargetException ite) {
			System.out.println(ite.getMessage());
			throw new InvocationTargetException(ite, ite.getMessage());
		} catch (NoSuchMethodException nse) {
			System.out.println(nse.getMessage());
			throw new NoSuchMethodException("NoSuchMethodException:" + nse.getMessage());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw new NoSuchMethodException("Exception:" + ex.getMessage());
		}
	}

	public String getAcceptMovementType(String brandCode, String originalOrderTypeCode, String originalOrderNo,
			String moveOrderType, String moveOrderNo) {
		String imReceiveType = null;
		if (StringUtils.hasText(brandCode) && StringUtils.hasText(originalOrderTypeCode)
				&& StringUtils.hasText(originalOrderNo)) {
			List<ImReceiveHead> imReceiveHeads = imReceiveHeadDAO.findByProperty("ImReceiveHead", new String[] {
					"brandCode", "orderTypeCode", "orderNo" }, new String[] { brandCode, originalOrderTypeCode,
					originalOrderNo });
			if (imReceiveHeads != null) {
				ImReceiveHead imReceiveHead = imReceiveHeads.get(0);
				String defectMovOrderTypeCode = StringUtils.hasText(imReceiveHead.getDefectMovOrderTypeCode()) ? imReceiveHead
						.getDefectMovOrderTypeCode()
						: "";
				String defectMovOrderNo = StringUtils.hasText(imReceiveHead.getDefectMovOrderNo()) ? imReceiveHead
						.getDefectMovOrderNo() : "";
				String sampleMovOrderTypeCode = StringUtils.hasText(imReceiveHead.getSampleMovOrderTypeCode()) ? imReceiveHead
						.getSampleMovOrderTypeCode()
						: "";
				String sampleMovOrderNo = StringUtils.hasText(imReceiveHead.getSampleMovOrderNo()) ? imReceiveHead
						.getSampleMovOrderNo() : "";
				String shortMovOrderTypeCode = StringUtils.hasText(imReceiveHead.getShortMovOrderTypeCode()) ? imReceiveHead
						.getShortMovOrderTypeCode() : "";
				String shortMovOrderNo = StringUtils.hasText(imReceiveHead.getShortMovOrderNo()) ? imReceiveHead
						.getShortMovOrderNo() : "";

				if (moveOrderType.equals(defectMovOrderTypeCode) && moveOrderNo.equals(defectMovOrderNo)) {
					imReceiveType = imMovementHeadDAO.IM_RECEIVE_DEFECT;
				} else if (moveOrderType.equals(sampleMovOrderTypeCode) && moveOrderNo.equals(sampleMovOrderNo)) {
					imReceiveType = imMovementHeadDAO.IM_RECEIVE_SAMPLE;
				} else if (moveOrderType.equals(shortMovOrderTypeCode) && moveOrderNo.equals(shortMovOrderNo)) {
					imReceiveType = imMovementHeadDAO.IM_RECEIVE_SHORT;
				} else {
					imReceiveType = imMovementHeadDAO.IM_RECEIVE_ACCEPT;
				}
			}
		}
		return imReceiveType;
	}

	public void executeMainDailyBalance(ImMovementHead imMovementHead, String opUser) throws Exception {
		String brandCode = imMovementHead.getBrandCode();
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("SystemConfig", "DefaultLotNo");
		String defaultLotNo = buCommonPhraseLine != null ? buCommonPhraseLine.getName() : "000000000000";
        //System.out.println(imMovementHead.getBrandCode()+"/"+imMovementHead.getOrderTypeCode()+imMovementHead.getOrderNo());
		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
		BuBrand buBrand = buBrandDAO.findById(imMovementHead.getBrandCode());
		String waitingWarehouseCode = "";
		if (null == buBrand)
			throw new NoSuchDataException("依據品牌：" + imMovementHead.getBrandCode() + "查無其品牌代號！");
		else {
			waitingWarehouseCode = buBrand.getDefaultWarehouseCode1() == null ? "" : buBrand.getDefaultWarehouseCode1();
		}
		if (null == imMovementHead.getDeliveryDate()) {
			throw new NoSuchDataException("單號" + imMovementHead.getOrderTypeCode() + "-" + imMovementHead.getOrderNo()
					+ "的出貨日期為null");
		} else {
			String monthlyCloseMonth = null == buBrand.getMonthlyBalanceMonth() ? "200001" : buBrand
					.getMonthlyBalanceMonth();
			String deliveryYYYYMM = DateUtils.format(imMovementHead.getDeliveryDate(), "yyyyMM");
			boolean beforeCloseMonth = Integer.valueOf(deliveryYYYYMM) <= Integer.valueOf(monthlyCloseMonth) ? true : false;
			String deliveryYYYY = DateUtils.format(imMovementHead.getDeliveryDate(), "yyyy");
			String deliveryMM = DateUtils.format(imMovementHead.getDeliveryDate(), "MM");
			String orderTypeCode = imMovementHead.getOrderTypeCode();
			String orderNo = imMovementHead.getOrderNo();
			String cmDeliveryWarehouseCode = new String("");
			String cmArrivalWarehouseCode = new String("");
			String identification = MessageStatus.getIdentificationMsg(brandCode, orderTypeCode, orderNo);

			String status = OrderStatus.FINISH.equals(imMovementHead.getStatus()) ? OrderStatus.CLOSE : imMovementHead
					.getStatus();

			ImWarehouse imDeliveryWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class,
					imMovementHead.getDeliveryWarehouseCode());
			if (null != imDeliveryWarehouse) {
				cmDeliveryWarehouseCode = StringUtils.hasText(imDeliveryWarehouse.getCustomsWarehouseCode()) ? imDeliveryWarehouse
						.getCustomsWarehouseCode()
						: "";
			}
			ImWarehouse imArrivalWarehouse = (ImWarehouse) imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, imMovementHead
					.getArrivalWarehouseCode());
			if (null != imArrivalWarehouse) {
				cmArrivalWarehouseCode = StringUtils.hasText(imArrivalWarehouse.getCustomsWarehouseCode()) ? imArrivalWarehouse
						.getCustomsWarehouseCode()
						: "";
			}
			boolean diffCustomsWarehouse = TAX_MODE_FREE.equalsIgnoreCase(imMovementHead.getTaxType())
					&& !cmDeliveryWarehouseCode.equals(cmArrivalWarehouseCode);

			// 更新出貨單Status為CLOSE
			imMovementHead.setStatus(status);

			List<ImMovementItem> imMovementItems = imMovementHead.getImMovementItems();

			if (imMovementItems != null && imMovementItems.size() > 0) {
				for (ImMovementItem imMovementItem : imMovementItems) {
					imMovementItem.setStatus(status);
					String arrivalLotNo = imMovementItem.getLotNo();
					if (waitingWarehouseCode.equals(imMovementItem.getDeliveryWarehouseCode())
							&& !(waitingWarehouseCode.equals(imMovementItem.getArrivalWarehouseCode())))
						// TODO to check item lot control flag, if the
						// lotControl is "Y", the lotNo will be kept on original

						arrivalLotNo = defaultLotNo;

					this.updateMainOnHandAndTransaction(organizationCode, brandCode, orderTypeCode, orderNo, imMovementItem,
							imMovementHead.getDeliveryDate(), imMovementItem.getDeliveryWarehouseCode(), imMovementHead
									.getArrivalDate(), imMovementItem.getArrivalWarehouseCode(), arrivalLotNo,
							imMovementItem.getDeliveryQuantity(), opUser, imMovementHead.getCmMovementNo());

					if (diffCustomsWarehouse) {
						this.updateMainCmOnHand(imMovementItem.getOriginalDeclarationNo(), imMovementItem
								.getOriginalDeclarationSeq(), imMovementItem.getItemCode(), cmDeliveryWarehouseCode,
								cmArrivalWarehouseCode, imMovementHead.getBrandCode(), imMovementItem.getDeliveryQuantity(),
								opUser);
					}

				}

				imMovementHeadDAO.update(imMovementHead);
			} else {
				throw new ValidationErrorException("查無" + identification + "的調撥單明細資料！");
			}
		}
	}

	public void updateMainOnHandAndTransaction(String organizationCode, String brandCode, String orderTypeCode,
			String orderNo, ImMovementItem imMovementItem, Date deliveryDate, String deliveryWarehouse, Date arrivalDate,
			String arrivalWarehouse, String arrivalLotNo, Double arrivalQuantity, String opUser, String cmMovementNo)
			throws FormException {

		if (imMovementItem.getDeliveryQuantity() != 0D) {
			String declNo = new String("");
			Double originalPriceAmount = new Double(0);
			if (StringUtils.hasText(imMovementItem.getOriginalDeclarationNo())) {
				declNo = cmDeclarationHeadDAO.getDeclarationTypeByNo(imMovementItem.getOriginalDeclarationNo());
				if (!StringUtils.hasText(declNo))
					throw new ValidationErrorException("查無來源報單:" + imMovementItem.getOriginalDeclarationNo() + "類別資料！");
			}

			List itemPrices = imItemPriceDAO.getItemPriceByBeginDate(brandCode, imMovementItem.getItemCode(), "1",
					deliveryDate, "Y");
			if (itemPrices != null && itemPrices.size() > 0) {
				Object[] objArray = (Object[]) itemPrices.get(0);
				BigDecimal unitPrice = (BigDecimal) objArray[1];
				if (unitPrice != null) {
					originalPriceAmount = unitPrice.doubleValue();
				}
			}
			imOnHandDAO.updateMoveOnHand(organizationCode, imMovementItem.getItemCode(), deliveryWarehouse, imMovementItem
					.getLotNo(), imMovementItem.getDeliveryQuantity() * -1, opUser, brandCode);

			imOnHandDAO.updateMoveOnHand(organizationCode, imMovementItem.getItemCode(), arrivalWarehouse, arrivalLotNo,
					arrivalQuantity, opUser, brandCode);

			Double averageUnitCost = 0D;

			// 產生交易明細檔 出庫

			ImTransation transationOut = new ImTransation();
			transationOut.setBrandCode(brandCode);
			transationOut.setTransationDate(deliveryDate);
			transationOut.setOrderTypeCode(orderTypeCode);
			transationOut.setOrderNo(orderNo);
			transationOut.setLineId(imMovementItem.getIndexNo());
			transationOut.setItemCode(imMovementItem.getItemCode());
			transationOut.setWarehouseCode(deliveryWarehouse);
			transationOut.setLotNo(imMovementItem.getLotNo());
			transationOut.setQuantity(imMovementItem.getDeliveryQuantity() * -1);
			transationOut.setReverseWarehouseCode(imMovementItem.getArrivalWarehouseCode());
			transationOut.setCostAmount(0D);
			transationOut.setOrderAmount(0D);
			transationOut.setAverageUnitCost(averageUnitCost * imMovementItem.getDeliveryQuantity() * -1);
			transationOut.setCreatedBy(opUser);
			transationOut.setCreationDate(new Date());
			transationOut.setLastUpdatedBy(opUser);
			transationOut.setLastUpdatedDate(new Date());
			transationOut.setDeclarationDate(null);
			transationOut.setDeclarationNo(null);
			transationOut.setDeclarationType(null);
			transationOut.setOriginalDeclarationNo(imMovementItem.getOriginalDeclarationNo());
			transationOut.setOriginalDeclarationSeq(imMovementItem.getOriginalDeclarationSeq());
			transationOut.setOriginalDeclarationDate(imMovementItem.getOriginalDeclarationDate());
			transationOut.setCmMovementNo(cmMovementNo);
			transationOut.setOriginalPriceAmount(originalPriceAmount* transationOut.getQuantity());
			transationOut.setOriginalDeclarationType(declNo);
			transationOut.setAdjustmentType(null);

			imTransationDAO.save(transationOut);

			// 產生交易明細檔入庫
			ImTransation transationIn = new ImTransation();
			transationIn.setBrandCode(brandCode);
			transationIn.setTransationDate(deliveryDate);
			transationIn.setOrderTypeCode(orderTypeCode);
			transationIn.setOrderNo(orderNo);
			transationIn.setLineId(imMovementItem.getIndexNo());
			transationIn.setItemCode(imMovementItem.getItemCode());
			transationIn.setWarehouseCode(arrivalWarehouse);
			transationIn.setLotNo(arrivalLotNo);
			transationIn.setQuantity(arrivalQuantity);
			transationIn.setReverseWarehouseCode(imMovementItem.getDeliveryWarehouseCode());
			transationIn.setCostAmount(0D);
			transationIn.setOrderAmount(0D);
			transationIn.setAverageUnitCost(averageUnitCost * imMovementItem.getDeliveryQuantity());
			transationIn.setReserve1(arrivalDate.toString().substring(0, 10).replaceAll("-", "/"));
			transationIn.setCreatedBy(opUser);
			transationIn.setCreationDate(new Date());
			transationIn.setLastUpdatedBy(opUser);
			transationIn.setLastUpdatedDate(new Date());
			transationOut.setDeclarationDate(null);
			transationOut.setDeclarationNo(null);
			transationOut.setDeclarationType(null);
			transationIn.setOriginalDeclarationNo(imMovementItem.getOriginalDeclarationNo());
			transationIn.setOriginalDeclarationSeq(imMovementItem.getOriginalDeclarationSeq());
			transationIn.setOriginalDeclarationDate(imMovementItem.getOriginalDeclarationDate());
			transationIn.setCmMovementNo(cmMovementNo);
			transationIn.setOriginalPriceAmount(originalPriceAmount* transationIn.getQuantity());
			transationIn.setOriginalDeclarationType(declNo);
			transationIn.setAdjustmentType(null);
			imTransationDAO.save(transationIn);
		}
	}

	public void updateMainCmOnHand(String declarationNo, Long declarationSeq, String itemCode,
			String cmDeliveryWarehouseCode, String cmArrivalWarehouseCode, String brandCode, Double quantity, String opUser)
			throws FormException {

		cmDeclarationOnHandDAO.updateMoveOnHand(declarationNo, declarationSeq, itemCode, cmDeliveryWarehouseCode, brandCode,
				quantity * -1, opUser);

		cmDeclarationOnHandDAO.updateMoveOnHand(declarationNo, declarationSeq, itemCode, cmArrivalWarehouseCode, brandCode,
				quantity, opUser);
	}

	public void updateTransportDataById(Long[] transportIdArray, String transportStatus) throws Exception {

		String errorMsg = null;
		try {
			List<ImMovementHead> movementHeads = imMovementHeadDAO.findTransportDataById(transportIdArray);
			if (movementHeads != null && movementHeads.size() > 0) {
				for (int i = 0; i < movementHeads.size(); i++) {
					ImMovementHead movementHead = (ImMovementHead) movementHeads.get(i);
					movementHead.setTransport(transportStatus);
					imMovementHeadDAO.update(movementHead);
				}
			}
		} catch (Exception ex) {
			errorMsg = "依據識別碼更新調撥資料傳送狀態發生錯誤，原因：" + ex.toString();
			log.error(errorMsg);
			throw new Exception(errorMsg);
		}
	}

	public List<Properties> getAJAXWarehouseInfoByOrderType(Map parameterMap) throws Exception {
		log.info("getItemCategoryRelatedList....");
		Map returnMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			BuCommonPhraseLine orderTypeSetting = buCommonPhraseLineDAO.findById("ImMovementOrderType", brandCode
					+ orderTypeCode);
			if (null != orderTypeSetting) {
				String itemCategoryMode = orderTypeSetting.getAttribute3();
				List<ImWarehouse> allDeliveryWarehouses = this.getWarehouseInfo("delivery", orderTypeCode, brandCode,
						employeeCode, itemCategoryMode);
				List<ImWarehouse> allArrivalWarehouses = this.getWarehouseInfo("arrival", orderTypeCode, brandCode, null,
						itemCategoryMode);
				returnMap.put("allDeliveryWarehouses", AjaxUtils.produceSelectorData(allDeliveryWarehouses, "warehouseCode",
						"warehouseName", true, true));
				returnMap.put("allArrivalWarehouses", AjaxUtils.produceSelectorData(allArrivalWarehouses, "warehouseCode",
						"warehouseName", true, true));

			}

			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (IllegalAccessException iae) {
			System.out.println(iae.getMessage());
			throw new IllegalAccessException(iae.getMessage());
		} catch (InvocationTargetException ite) {
			System.out.println(ite.getMessage());
			throw new InvocationTargetException(ite, ite.getMessage());
		} catch (NoSuchMethodException nse) {
			System.out.println(nse.getMessage());
			throw new NoSuchMethodException(nse.getMessage());
		}
	}

	/**
	 * 反轉
	 *
	 * @param headId
	 * @param owner
	 * @return
	 * @throws Exception
	 */
	public Object executeReverter(Long headId, String owner) throws Exception {
		log.info("reviveImMovementFromClose...");
		Map returnMap = new HashMap(0);
		List errorMsgs = new ArrayList(0);
		ImMovementHead objMovement = imMovementHeadDAO.findById(headId);
		if (null != objMovement) {
			String identification = MessageStatus.getIdentification(objMovement.getBrandCode(), objMovement
					.getOrderTypeCode(), objMovement.getOrderNo());
			if (OrderStatus.CLOSE.equals(objMovement.getStatus()) || OrderStatus.FINISH.equals(objMovement.getStatus())) {
				log.info("反確調撥單號" + objMovement.getOrderTypeCode() + objMovement.getOrderNo() + "狀態:"
						+ objMovement.getStatus());
				if (!StringUtils.hasText(objMovement.getCmMovementNo())) {
					ValidateUtil.isAfterClose(objMovement.getBrandCode(), objMovement.getOrderTypeCode(), "出貨日期",
							objMovement.getDeliveryDate(),objMovement.getSchedule());
					try {
						BuCommonPhraseLine orderTypeSetting = buCommonPhraseLineDAO.findById("ImMovementOrderType",
								objMovement.getBrandCode() + objMovement.getOrderTypeCode());
						if (orderTypeSetting == null)
							throw new ValidationErrorException("查無單別資料-CommonPhraase(" + objMovement.getOrderTypeCode()
									+ ")，請通知系統管理員");

						this.updateMoveOnHandtoVoid(objMovement, false, identification, errorMsgs, StringUtils
								.hasText(orderTypeSetting.getAttribute5()) ? orderTypeSetting.getAttribute5() : "NORMAL");
						objMovement.setStatus(OrderStatus.SAVE);
						objMovement.setCreatedBy(owner);
						objMovement.setProcessId(null); // 反轉時，清空PROCESS_ID
						log.info("調撥單反確資料更新");
						imMovementHeadDAO.update(objMovement);
						// log.info("調撥單反確流程重啟");
						// this.startProcess(objMovement);
						return objMovement;
					} catch (Exception ex) {
						throw new Exception("調撥單反確失敗,原因:" + ex.getMessage());
					}
				} else {
					throw new ValidationErrorException("依據調撥單" + objMovement.getOrderTypeCode() + objMovement.getOrderNo()
							+ "已移倉(移倉單號:" + objMovement.getCmMovementNo() + ")不可反確");
				}
			} else {
				throw new ValidationErrorException("查無調撥單" + objMovement.getOrderTypeCode() + objMovement.getOrderNo()
						+ "狀態不為簽核完成或結案，不可反確");
			}

		} else {
			throw new NoSuchDataException("查無調撥單資料(HEAD_ID:" + headId + ")");
		}
		// return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	public void executeReverterProcess(Object bean) throws Exception {
		this.startProcess((ImMovementHead) bean);
	}

	/**
	 * 調撥單匯出明細 add by Weichun 2010.06.22
	 *
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public SelectDataInfo exportExcelDetail(HttpServletRequest httpRequest) throws Exception {
		try {

			Long headId = NumberUtils.getLong(httpRequest.getParameter("headId")); // 要顯示的HEAD_ID
			String brandCode = httpRequest.getParameter("brandCode"); // 品牌代號
			String exportBeanName = httpRequest.getParameter("exportBeanName"); // 調撥單種類
			// 可用庫存excel表的欄位順序
			Object[] object = null;
			if ("T2_IM_MOVEMENT_ITEM_3".equals(exportBeanName))
				object = new Object[] { "indexNo", "boxNo", "itemCode", "itemName", "unitPrice", "deliveryWarehouseCode",
						"lotNo", "originalDeliveryQuantity", "arrivalWarehouseCode", "originalDeclarationNo",
						"originalDeclarationSeq", "originalDeclarationDate" };
			else
				object = new Object[] { "indexNo", "boxNo", "itemCode", "itemName", "unitPrice", "deliveryWarehouseCode",
						"lotNo", "deliveryQuantity", "arrivalWarehouseCode", "originalDeclarationNo",
						"originalDeclarationSeq", "originalDeclarationDate" };

			List<Object[]> imMovementItem = imMovementItemDAO.findPageLineIncludePrice(headId, brandCode, exportBeanName);

			// 按excel表的欄位順序將資料放入Object[]，再一筆筆放到List
			List rowData = new ArrayList();
			for (int i = 0; i < imMovementItem.size(); i++) {
				Object[] dataObject = (Object[]) imMovementItem.get(i);
				for (int j = 0; j < object.length; j++) {
					String actualValue = null;
					if (object[j] != null) {
						actualValue = dataObject[j] != null ? String.valueOf(dataObject[j]) : null;
					}
					dataObject[j] = actualValue;
				}
				rowData.add(dataObject);
			}
			return new SelectDataInfo(object, rowData);
		} catch (Exception ex) {
			// ex.printStackTrace();
			log.error("載入頁面顯示的調撥明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的調撥明細失敗！");
		}
	}

	/**
	 * 匯出條碼
	 *
	 * @param httpRequest
	 */
	public void exportBarCode(HttpServletRequest httpRequest) throws Exception {
		try {
			Long headId = Long.parseLong(httpRequest.getParameter("headId"));
			String brandCode = httpRequest.getParameter("brandCode");
			String orderTypeCode = httpRequest.getParameter("orderTypeCode");
			HttpSession httpSession = httpRequest.getSession();
			if (httpSession != null) {
				httpSession.setAttribute("detailEntityBeans", getExportBarCode(headId, brandCode, orderTypeCode));
			}
		} catch (Exception ex) {
			//ex.printStackTrace();
			log.error("調撥單匯出條碼發生錯誤，原因：" + ex.toString());
			throw new Exception("調撥單匯出條碼發生錯誤！");
		}
	}

	/**
	 * 更新PROCESS_ID，避免重複起流程 2010.07.07
	 *
	 * @param headId
	 * @param ProcessId
	 * @return
	 * @throws Exception
	 */
	public int updateProcessId(Long headId, Long ProcessId) throws Exception {
		int result = 0;
		try {
			result = imMovementHeadDAO.updateProcessId(headId, ProcessId);
			System.out.println("更新資料筆數 ::: " + result + "筆");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("調撥單更新PROCESS_ID錯誤，原因：" + e.getMessage());
			throw new Exception("調撥單更新PROCESS_ID錯誤！");
		}
		return result;
	}

	/**
	 * 建立一張新的調撥單（for 贈品、試用品） 2010.07.14
	 *
	 * @param imMovementHead
	 * @throws Exception
	 */
	public ImMovementHead createNewImMovement(ImMovementHead imMovementHead, Date date) throws Exception {
		log.info("============ 自動新增贈品調撥單 WHF&WHP ============");
		try {
			// 取得下一階段調撥單的type
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("ImMovementOrderType", imMovementHead
					.getBrandCode() + imMovementHead.getOrderTypeCode());

			// 取得調撥單號
			String serialNo = buOrderTypeService.getOrderSerialNo(imMovementHead.getBrandCode(), buCommonPhraseLine
					.getReserve3());
			// List<ImMovementItem> imMovementItemList = imMovementHead.getImMovementItems();

			// 取得轉入庫別的資料
			BuLocationService buLocationService = (BuLocationService) SpringUtils.getApplicationContext().getBean(
					"buLocationService");
			ImWarehouse arrivalWarehouse = (ImWarehouse) imWarehouseDAO.findById("ImWarehouse", imMovementHead
					.getArrivalStoreCode());
			String customsArrivalWarehouseCode = "";
			BuLocation arrivalLocation = null;
			if (null != arrivalWarehouse) {
				customsArrivalWarehouseCode = arrivalWarehouse.getCustomsWarehouseCode();
				arrivalLocation = buLocationService.findById(arrivalWarehouse.getLocationId());
			}
			else
				throw new ValidationErrorException("轉入庫別不存在：" + imMovementHead.getArrivalStoreCode());

			// 轉入庫別倉管人員
			String arrivalWarehouseMangager = this.getWarehouseManager(imMovementHead.getBrandCode(), imMovementHead
					.getArrivalStoreCode(), customsArrivalWarehouseCode, buCommonPhraseLine.getAttribute3(), imMovementHead
					.getItemCategory());

			ImMovementHead newImMovementHead = new ImMovementHead();
			//newImMovementHead.setHeadId(null); // 自動產生HEAD_ID
			newImMovementHead.setBrandCode(imMovementHead.getBrandCode()); // 品牌代號
			newImMovementHead.setOrderTypeCode(buCommonPhraseLine.getReserve3()); // 單別
			newImMovementHead.setOrderNo(serialNo); // 單號
			newImMovementHead.setDeliveryDate(date); // 轉出日期
			newImMovementHead.setDeliveryWarehouseCode(imMovementHead.getArrivalWarehouseCode()); // 轉出庫別為前一張調撥單的轉入庫別
			newImMovementHead.setDeliveryAddress(imMovementHead.getArrivalAddress());
			newImMovementHead.setDeliveryArea(imMovementHead.getArrivalArea());
			newImMovementHead.setDeliveryCity(imMovementHead.getArrivalCity());
			newImMovementHead.setDeliveryZipCode(imMovementHead.getArrivalZipCode());
			newImMovementHead.setDeliveryContactPerson(imMovementHead.getArrivalContactPerson());
			newImMovementHead.setArrivalDate(date); // 轉入日期
			newImMovementHead.setArrivalWarehouseCode(imMovementHead.getArrivalStoreCode()); // 轉入庫別為前一張單的轉入店別
			newImMovementHead.setArrivalAddress(arrivalLocation.getAddress());
			newImMovementHead.setArrivalArea(arrivalLocation.getArea());
			newImMovementHead.setArrivalCity(arrivalLocation.getCity());
			newImMovementHead.setArrivalZipCode(arrivalLocation.getZipCode());
			newImMovementHead.setArrivalContactPerson(arrivalWarehouseMangager);
			newImMovementHead.setArrivalStoreCode(null); // 轉入店別清空
			newImMovementHead.setStatus("SAVE"); // 單據狀態為暫存單
			newImMovementHead.setCreatedBy(arrivalWarehouseMangager); // 建單人
			newImMovementHead.setCreationDate(new Date());
			newImMovementHead.setLastUpdatedBy("SYS");
			newImMovementHead.setLastUpdateDate(new Date());
			newImMovementHead.setTaxType("P"); // 稅別
			newImMovementHead.setBoxCount(imMovementHead.getBoxCount()); // 箱數
			newImMovementHead.setItemCount(imMovementHead.getItemCount()); // 件數
			newImMovementHead.setOriginalOrderTypeCode(imMovementHead.getOrderTypeCode()); // 來源單別
			newImMovementHead.setOriginalOrderNo(imMovementHead.getOrderNo()); // 來源單號
			List<ImMovementItem> list = new ArrayList();
			if ("WHF".equals(imMovementHead.getOrderTypeCode()) || "WHP".equals(imMovementHead.getOrderTypeCode())) {// 贈品
				ImMovementItem imMovementItem = (ImMovementItem) imMovementHead.getImMovementItems().get(0);
				ImMovementItem newImMovementItem = new ImMovementItem();
				newImMovementItem.setItemCode("00GWP"); // 贈品品號：00GWP
				newImMovementItem.setDeliveryWarehouseCode(imMovementItem.getArrivalWarehouseCode()); // 轉出庫別
				newImMovementItem.setLotNo("000000000000"); // 批號
				newImMovementItem.setOriginalDeliveryQuantity(imMovementHead.getItemCount());  // 預出數量
				newImMovementItem.setDeliveryQuantity(imMovementHead.getItemCount()); // 實出數量
				newImMovementItem.setArrivalWarehouseCode(imMovementHead.getArrivalStoreCode()); // 轉入庫別
				newImMovementItem.setArrivalQuantity(imMovementHead.getItemCount()); // 轉入數量
				newImMovementItem.setReserve1("Okay");
				newImMovementItem.setBoxNo(1L); // 箱號預設：1
				list.add(newImMovementItem);
			}else if("WFF".equals(imMovementHead.getOrderTypeCode()) || "WFP".equals(imMovementHead.getOrderTypeCode())){ // 試用品 需新增單據

			}
			newImMovementHead.setImMovementItems(list);
			imMovementHeadDAO.save(newImMovementHead);

			//ImMovementHead imt = imMovementHeadDAO.findMovementByIdentification(imMovementHead.getBrandCode(), newImMovementHead.getOrderTypeCode(), newImMovementHead.getOrderNo());
			return newImMovementHead;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("建立新的暫存調撥單失敗，原因：" + e.toString());
			throw new Exception("建立新的暫存調撥單失敗，原因：" + e.getMessage());
		}
	}


	/**
	 * 調撥單單據複製初始化畫面
	 *
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> executeCopyInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			Object otherBean = parameterMap.get("vatBeanOther");
			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");

			Map multiList = new HashMap(0);
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(brandCode, "IM");

			multiList.put("allOrderTypes", AjaxUtils
					.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, true));

			this.setMoveTypeArray(allOrderTypes);
			resultMap.put("multiList", multiList);

		} catch (Exception ex) {
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "表單初始化失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}

		return AjaxUtils.parseReturnDataToJSON(resultMap);

	}

	/**
	 * 調撥單單據狀態
	 *
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getImStatus(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			log.info("getImStatus.parameterMap:" + parameterMap.keySet().toString());
			Object otherBean = parameterMap.get("vatBeanOther");
			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");

			String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode"); // 原單據單別
			String orderNo = (String) PropertyUtils.getProperty(otherBean, "orderNo"); // 原單據單別
			ImMovementHead imMovementHead = this.findMovementByIdentification(brandCode, orderTypeCode, orderNo);
			if (imMovementHead == null) {
				throw new ValidationErrorException("單別單號：" + orderTypeCode + orderNo + "不存在");
			}
			resultMap.put("status", imMovementHead.getStatus());
			resultMap.put("statusCName", OrderStatus.getChineseWord(imMovementHead.getStatus()));

		} catch (Exception ex) {
			log.error("取得調撥單單據狀態失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "取得調撥單單據狀態失敗，原因：" + ex.getMessage());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}

	/**
	 * 調撥單單據複製
	 *
	 * @param imMovementHead
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public ImMovementHead createToNewImMovement(ImMovementHead imMovementHead, Map parameterMap) throws Exception {
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String preOrderTypeCode = (String) PropertyUtils.getProperty(otherBean, "preOrderTypeCode"); // 預產生單據單別
			String deliverWarehouseCode = (String) PropertyUtils.getProperty(otherBean, "deliverWarehouseCode"); // 預轉出庫別
			String arrivalWarehouseCode = (String) PropertyUtils.getProperty(otherBean, "arrivalWarehouseCode"); // 預轉入庫別
			String reserveDate = (String) PropertyUtils.getProperty(otherBean, "reserveDate"); // 預轉出日期
			// 取得調撥單號
			String serialNo = buOrderTypeService.getOrderSerialNo(imMovementHead.getBrandCode(), preOrderTypeCode);
log.info("1");
			// 取得轉出庫別的資料
			BuLocationService buLocationService = (BuLocationService) SpringUtils.getApplicationContext().getBean(
					"buLocationService");
			log.info("-A");
			//ImWarehouse deliveryWarehouseBean = imWarehouseDAO.findByWarehouseId(deliverWarehouseCode);
			ImWarehouse deliveryWarehouseBean = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, deliverWarehouseCode, "Y");
			log.info("A");
			ImWarehouse deliveryWarehouse = (ImWarehouse) imWarehouseDAO.findById("ImWarehouse", deliveryWarehouseBean.getWarehouseId());
			log.info("B");
			String customsDeliveryWarehouseCode = "";
			BuLocation deliveryLocation = null;
			if (null != deliveryWarehouse) {
				customsDeliveryWarehouseCode = deliveryWarehouse.getCustomsWarehouseCode();
				log.info("C");
				deliveryLocation = buLocationService.findById(deliveryWarehouse.getLocationId());
			} else
				throw new ValidationErrorException("轉出庫別不存在：" + deliverWarehouseCode);
			log.info("2");
			// 轉出庫別倉管人員
			String deliveryWarehouseMangager = this.getWarehouseManager(imMovementHead.getBrandCode(), deliverWarehouseCode,
					customsDeliveryWarehouseCode, preOrderTypeCode, imMovementHead.getItemCategory());
			log.info("3");
			// 取得轉入庫別的資料
			//ImWarehouse arrivalWarehouseBean = imWarehouseDAO.findByWarehouseId(arrivalWarehouseCode);
			ImWarehouse arrivalWarehouseBean = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, arrivalWarehouseCode, "Y");
			ImWarehouse arrivalWarehouse = (ImWarehouse) imWarehouseDAO.findById("ImWarehouse", arrivalWarehouseBean.getWarehouseId());
			log.info("4");
			String customsArrivalWarehouseCode = "";
			BuLocation arrivalLocation = null;
			if (null != arrivalWarehouse) {
				customsArrivalWarehouseCode = arrivalWarehouse.getCustomsWarehouseCode();
				arrivalLocation = buLocationService.findById(arrivalWarehouse.getLocationId());
			} else
				throw new ValidationErrorException("轉入庫別不存在：" + arrivalWarehouseCode);
			log.info("5");
			// 轉入庫別倉管人員
			String arrivalWarehouseMangager = this.getWarehouseManager(imMovementHead.getBrandCode(), arrivalWarehouseCode,
					customsArrivalWarehouseCode, preOrderTypeCode, imMovementHead.getItemCategory());

			ImMovementHead newImMovementHead = new ImMovementHead();
			//newImMovementHead.setHeadId(null); // 自動產生HEAD_ID
			newImMovementHead.setBrandCode(brandCode); // 品牌代號
			newImMovementHead.setOrderTypeCode(preOrderTypeCode); // 單別
			newImMovementHead.setOrderNo(serialNo); // 單號
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			newImMovementHead.setDeliveryDate(sdf.parse(reserveDate)); // 轉出日期
			newImMovementHead.setDeliveryWarehouseCode(deliverWarehouseCode); // 轉出庫別
			newImMovementHead.setDeliveryAddress(deliveryLocation.getAddress());
			newImMovementHead.setDeliveryArea(deliveryLocation.getArea());
			newImMovementHead.setDeliveryCity(deliveryLocation.getCity());
			newImMovementHead.setDeliveryZipCode(deliveryLocation.getZipCode());
			newImMovementHead.setDeliveryContactPerson(employeeCode);
			newImMovementHead.setArrivalWarehouseCode(arrivalWarehouseCode); // 轉入庫別
			newImMovementHead.setArrivalAddress(arrivalLocation.getAddress());
			newImMovementHead.setArrivalArea(arrivalLocation.getArea());
			newImMovementHead.setArrivalCity(arrivalLocation.getCity());
			newImMovementHead.setArrivalZipCode(arrivalLocation.getZipCode());
			newImMovementHead.setArrivalContactPerson(arrivalWarehouseMangager);
			newImMovementHead.setArrivalStoreCode(null); // 轉入店別清空
			newImMovementHead.setStatus("SAVE"); // 單據狀態為暫存單
			newImMovementHead.setCreatedBy(employeeCode); // 建單人
			newImMovementHead.setCreationDate(new Date());
			newImMovementHead.setLastUpdatedBy("SYS");
			newImMovementHead.setLastUpdateDate(new Date());
			newImMovementHead.setTaxType(imMovementHead.getTaxType()); // 稅別
			newImMovementHead.setBoxCount(imMovementHead.getBoxCount()); // 箱數
			newImMovementHead.setItemCount(imMovementHead.getItemCount()); // 件數

			List<ImMovementItem> list = new ArrayList();

			List<ImMovementItem> imMovementItems = imMovementHead.getImMovementItems();
			for (int i = 0; i < imMovementItems.size(); i++) {
				ImMovementItem newImMovementItem = new ImMovementItem();
				newImMovementItem.setItemCode(((ImMovementItem) imMovementItems.get(i)).getItemCode());
				newImMovementItem.setDeliveryWarehouseCode(deliverWarehouseCode); // 轉出庫別
				newImMovementItem.setLotNo("000000000000"); // 批號
				newImMovementItem.setOriginalDeliveryQuantity(((ImMovementItem) imMovementItems.get(i))
						.getOriginalDeliveryQuantity()); // 預出數量
				newImMovementItem.setDeliveryQuantity(((ImMovementItem) imMovementItems.get(i)).getDeliveryQuantity()); // 實出數量
				newImMovementItem.setArrivalWarehouseCode(arrivalWarehouseCode); // 轉入庫別
				newImMovementItem.setArrivalQuantity(((ImMovementItem) imMovementItems.get(i)).getArrivalQuantity()); // 轉入數量
				newImMovementItem.setReserve1("Okay");
				newImMovementItem.setBoxNo(((ImMovementItem) imMovementItems.get(i)).getBoxNo()); // 箱號
				list.add(newImMovementItem);
			}
			newImMovementHead.setImMovementItems(list);

			imMovementHeadDAO.save(newImMovementHead);

			//ImMovementHead imt = imMovementHeadDAO.findMovementByIdentification(imMovementHead.getBrandCode(), newImMovementHead.getOrderTypeCode(), newImMovementHead.getOrderNo());
			return newImMovementHead;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("建立新的暫存調撥單失敗，原因：" + e.toString());
			throw new Exception("建立新的暫存調撥單失敗，原因：" + e.getMessage());
		}
	}


	/**
	 * 查詢調撥單 for 調撥單據複製
	 *
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> searchAJAXSearchPageData(Properties httpRequest) throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest); // 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest); // 取得每頁大小

			// ======================帶入Head的值=========================

			String brandCode = httpRequest.getProperty("loginBrandCode"); // 品牌代號
			String deliveryDate = httpRequest.getProperty("deliveryDate"); // 轉出日期
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
			String orderNo = httpRequest.getProperty("orderNo"); // 單號
			String status = httpRequest.getProperty("status"); // 單據狀態
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("deliveryDate", deliveryDate);
			findObjs.put("orderTypeCode", orderTypeCode);
			findObjs.put("orderNo", orderNo);
			findObjs.put("status", status);

			List<ImMovementHead> imMovementHeads = (List<ImMovementHead>) imMovementHeadDAO.searchPageLine(findObjs, iSPage,
					iPSize, imMovementHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");

			log.info("imMovementHeads.size" + imMovementHeads.size());

			if (imMovementHeads != null && imMovementHeads.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) imMovementHeadDAO.findPageLine(findObjs, -1, iPSize,
						imMovementHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆
				// INDEX

				for (ImMovementHead imMovementHead : imMovementHeads) {
					imMovementHead.setStatus(OrderStatus.getChineseWord(imMovementHead.getStatus()));
					List<ImMovementItem> items = imMovementHead.getImMovementItems();
				}

				log.info("ImMovement.AjaxUtils.getAJAXPageData ");
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,
						imMovementHeads, gridDatas, firstIndex, maxIndex));

			} else {
				log.info("ImMovement.AjaxUtils.getAJAXPageDataDefault ");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));

			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的調撥查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的調撥查詢失敗！");
		}
	}

	/**
	 * 全選更新
	 *
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> updateAllSelected(Map parameterMap) throws Exception {
		log.info("updateAllSearchData....");
		Map resultMap = new HashMap(0);
		log.info(parameterMap.keySet().toString());
		try {
			Object pickerBean = parameterMap.get("vatBeanPicker");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");
			String timeScope = (String) PropertyUtils.getProperty(otherBean, AjaxUtils.TIME_SCOPE);
			System.out.println(AjaxUtils.TIME_SCOPE + ":" + timeScope);
			String isAllClick = (String) PropertyUtils.getProperty(otherBean, "isAllClick");
			log.info("timeScope:" + timeScope);

			String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");// 品牌代號
			String orderTypeCode = (String) PropertyUtils.getProperty(formBindBean, "orderTypeCode");// 單別
			String orderNo = (String) PropertyUtils.getProperty(formBindBean, "orderNo"); // 單號
			String deliveryDate = (String) PropertyUtils.getProperty(formBindBean, "deliveryDate");// 單據轉出日期
			String status = (String) PropertyUtils.getProperty(formBindBean, "status"); // 狀態
			log.info("isAllClick:" + isAllClick);
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("orderTypeCode", orderTypeCode);
			findObjs.put("orderNo", orderNo);
			findObjs.put("deliveryDate", deliveryDate);
			findObjs.put("status", status);
			List allDataList = (List) imMovementHeadDAO.searchPageLine(findObjs, 0, 100,
					imMovementHeadDAO.QUARY_TYPE_SELECT_ALL).get("form");

			if (allDataList.size() > 0)
				AjaxUtils.updateAllResult(timeScope, isAllClick, allDataList);

			return AjaxUtils.parseReturnDataToJSON(resultMap);
		} catch (IllegalAccessException iae) {
			System.out.println(iae.getMessage());
			throw new IllegalAccessException(iae.getMessage());
		} catch (InvocationTargetException ite) {
			System.out.println(ite.getMessage());
			throw new InvocationTargetException(ite, ite.getMessage());
		} catch (NoSuchMethodException nse) {
			System.out.println(nse.getMessage());
			throw new NoSuchMethodException("NoSuchMethodException:" + nse.getMessage());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw new NoSuchMethodException("Exception:" + ex.getMessage());
		}
	}


	public String getProcessSubject(ImMovementHead imMovementHead){
		String subject = new String("");
		subject = MessageStatus.getJobManagerMsg(imMovementHead.getBrandCode(), imMovementHead.getOrderTypeCode(), imMovementHead.getOrderNo());
		subject = subject+ "["+imMovementHead.getDeliveryWarehouseCode()+" to "+imMovementHead.getArrivalWarehouseCode() +"]";
		subject = subject+ (StringUtils.hasText(imMovementHead.getOriginalOrderTypeCode())? imMovementHead.getOriginalOrderTypeCode():"");
		subject = subject+ (StringUtils.hasText(imMovementHead.getOriginalOrderNo())? imMovementHead.getOriginalOrderNo():"");
		return subject;
	}


	/**
	 * 重新排序INDEX_NO
	 *
	 * @param movement
	 * @throws Exception
	 */
	public void updateItemIndex(ImMovementHead movement) throws Exception {
		try {
			log.info("資料更新完畢，重新排序INDEX_NO...");
			List<ImMovementItem> movementItems = movement.getImMovementItems();

			Long i = 1L; // 重新排序INDEX_NO by Weichun 2011/03/03
			Double itemCount = 0D; // 重新計算件數 by Weichun 2011/09/06
			for (ImMovementItem item : movementItems) {
				item.setIndexNo(i);
				log.info("SET-imMovementItemIndexNo:"+i);
				i++;
				itemCount += item.getDeliveryQuantity();
			}
			movement.setImMovementItems(movementItems);
			movement.setItemCount(itemCount);
			imMovementHeadDAO.update(movement);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
}