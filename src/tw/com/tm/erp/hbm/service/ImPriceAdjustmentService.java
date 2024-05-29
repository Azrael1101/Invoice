package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCompose;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.bean.ImItemPriceView;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImPriceAdjustment;
import tw.com.tm.erp.hbm.bean.ImPriceList;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImPriceAdjustmentDAO;
import tw.com.tm.erp.hbm.dao.ImPriceListDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.UserUtils;

public class ImPriceAdjustmentService {
	private static final Log log = LogFactory
			.getLog(ImPriceAdjustmentService.class);
	private ImPriceAdjustmentDAO imPriceAdjustmentDAO;
	private ImPriceListDAO imPriceListDAO;
	private ImItemPriceDAO imItemPriceDAO;
	private ImItemDAO imItemDAO;
	private ImItemPriceViewDAO imItemPriceViewDAO;
	private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
	private BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO;
	private BuOrderTypeService buOrderTypeService;
	private ImPriceListService imPriceListService;
	private ImMovementService imMovementService;
	private BuCommonPhraseService buCommonPhraseService;
	private BuBasicDataService buBasicDataService;
	private BuBrandService buBrandService;
	private ImItemService imItemService;
	private NativeQueryDAO nativeQueryDAO;
	private BuBrandDAO buBrandDAO;

	public static final String[] GRID_FIELD_NAMES = { "indexNo","category01", "itemCode", "itemCName", "foreignCost", "localCost", "unitPrice",
	"costRate", "priceId","lineId", "isLockRecord", "isDeleteRecord", "message"};//unitPrice送簽價格,"grossProfitRate", "originalQuotationPrice", "newQuotationPrice"

	public static final String[] PAJ_GRID_FIELD_NAMES = { "indexNo","itemCode", "itemCName", "originalQuotationPrice","newQuotationPrice", "originalPrice", "unitPrice",
	"grossProfitRate","lineId", "isLockRecord", "isDeleteRecord", "message"};//unitPrice送簽價格,,

	public static final int[] GRID_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
	};

	public static final int[] PAJ_GRID_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
	};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = { "0", "", "", "", "0", "0", "0", "0", "", "",AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""};

	public static final String[] PAJ_GRID_FIELD_DEFAULT_VALUES = { "0", "", "", "0", "0", "0", "0", "0", "",AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""};

	public static final String[] GRID_SEARCH_FIELD_NAMES = {
		"orderTypeCode", "orderNo", "description",
		"priceType",   "enableDate", "status", "headId"};

	public static final int[] GRID_SEARCH_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG};

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {
	"", "", "", "",
		"", "",  "0"};

	/* spring IoC */
	public void setImPriceAdjustmentDAO(
			ImPriceAdjustmentDAO imPriceAdjustmentDAO) {
		this.imPriceAdjustmentDAO = imPriceAdjustmentDAO;
	}

	public void setImItemPriceDAO(ImItemPriceDAO imItemPriceDAO) {
		this.imItemPriceDAO = imItemPriceDAO;
	}

	public void setImItemPriceViewDAO(ImItemPriceViewDAO imItemPriceViewDAO) {
		this.imItemPriceViewDAO = imItemPriceViewDAO;
	}

	public void setImItemCurrentPriceViewDAO(
			ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
		this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	public void setBuSupplierWithAddressViewDAO(BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO) {
		this.buSupplierWithAddressViewDAO = buSupplierWithAddressViewDAO;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setImPriceListService(ImPriceListService imPriceListService) {
		this.imPriceListService = imPriceListService;
	}

	public void setImItemService(ImItemService imItemService) {
		this.imItemService = imItemService;
	}

	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}

	public void setImPriceListDAO(ImPriceListDAO imPriceListDAO) {
		this.imPriceListDAO = imPriceListDAO;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}

	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}

	public void setImMovementService(ImMovementService imMovementService) {
		this.imMovementService = imMovementService;
	}


	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
	    this.buBrandDAO = buBrandDAO;
	}
	public List<Properties> getAJAXSupplierName(Properties httpRequest) throws FormException {
	    List reList = new ArrayList();
	    String brandCode = httpRequest.getProperty("brandCode");
	    String supplierCode = httpRequest.getProperty("supplierCode");
	    BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewDAO.findById(supplierCode, brandCode);
	    Properties p = new Properties();
	    p.setProperty("SupplierName","查無此供應商名稱");
	    if(buSupplierWithAddressView != null){
		p.setProperty("SupplierName", buSupplierWithAddressView.getChineseName());
	    }
	    reList.add(p);
	    return reList;
	}

	/**
	 * 儲存商品定價/變價/組合商品送簽單
	 *
	 * @param imPriceAdjustment
	 * @return String (Result message)
	 * @throws Exception
	 */
	public String save(ImPriceAdjustment imPriceAdjustment) throws Exception {

		try {
			System.out.println("Save imPriceAdjustment");
			if (OrderStatus.SIGNING.equals(imPriceAdjustment.getStatus())) {
				if (this.checkImPriceListItemData(imPriceAdjustment))
					return saveImPriceAdjustment(imPriceAdjustment);
				else
					return "送簽單存檔時發生錯誤!";
			} else {
				return saveImPriceAdjustment(imPriceAdjustment);
			}

		} catch (FormException fe) {
			log.error("送簽單存檔時發生錯誤，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("送簽單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("送簽單存檔時發生錯誤，原因：" + ex.getMessage());
		}

	}
	/**
	 * save temp_order_no
	 * @param saveObj
	 * @exception FormException ,Exception
	 * @return String
	 */
	public String saveTmp(ImPriceAdjustment imAdjustmentHead) throws FormException ,Exception{
		log.info("ImPriceAdjustmentService.saveTmp");
		String tmpOrderNo = AjaxUtils.getTmpOrderNo();
		imAdjustmentHead.setOrderNo(tmpOrderNo);
		imAdjustmentHead.setLastUpdateDate(new Date());
		imAdjustmentHead.setCreationDate(new Date());
		imPriceAdjustmentDAO.save(imAdjustmentHead);
		return MessageStatus.SUCCESS;
	}

	private String saveImPriceAdjustment(ImPriceAdjustment imPriceAdjustment)
			throws ObtainSerialNoFailedException {
		if (imPriceAdjustment.getHeadId() == null
				|| imPriceAdjustment.getHeadId() == 0) {

			// 新增
			imPriceAdjustment.setLastUpdateDate(new Date());
			imPriceAdjustment.setCreationDate(new Date());
			// 取得最新一筆流水號
			String serialNo = buOrderTypeService.getOrderSerialNo(
					imPriceAdjustment.getBrandCode(), imPriceAdjustment
							.getOrderTypeCode());
			if (!serialNo.equals("unknow")) {
				imPriceAdjustment.setOrderNo(serialNo);
				imPriceAdjustmentDAO.save(imPriceAdjustment);
			} else {
				throw new ObtainSerialNoFailedException("取得"
						+ imPriceAdjustment.getOrderTypeCode() + "單號失敗！");
			}
			return imPriceAdjustment.getOrderTypeCode() + "-" + serialNo
					+ "存檔成功！";

		} else {
			imPriceAdjustment.setLastUpdateDate(new Date());
			imPriceAdjustmentDAO.update(imPriceAdjustment);
			return imPriceAdjustment.getOrderTypeCode() + "-"
					+ imPriceAdjustment.getOrderNo() + "存檔成功！";
		}
	}

	/**
	 * 依據 pk 查詢送簽單
	 *
	 * @param id
	 * @return ImPriceAdjustment
	 */
	public ImPriceAdjustment findById(Long id) {
		return imPriceAdjustmentDAO.findById(id);
	}

	/**
	 * 依據查詢條件查詢送簽單
	 *
	 * @param brandCode
	 * @param orderType
	 * @param startOrderNo
	 * @param endOrderNo
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param employeeCode
	 * @param categorySearchString
	 * @return List<ImPriceAdjustment>
	 * @throws Exception
	 */
	public List<ImPriceAdjustment> findPriceAdjustmentByValue(HashMap findObjs)
			throws Exception {
		try {

			return imPriceAdjustmentDAO.findPriceAdjustmentByValue(findObjs);

		} catch (Exception ex) {
			log.error("查詢送簽單時發生錯誤，原因：" + ex.toString());
			throw new NoSuchDataException("查詢送簽單時發生錯誤：" + ex.getMessage());
		}
	}

	/**
	 * 依據調價比例及進位方式設定變價價格
	 *
	 * @param priceList
	 * @param percentage
	 *            調價比例
	 * @param calculateType
	 *            進位方式 1)四捨五入 2)無條件捨去 3)無條件進位
	 * @param scale
	 *            第幾位進位
	 * @return List<ImPriceList>
	 */
	public List<ImPriceList> calculateAdjustmentAmount(
			List<ImPriceList> priceList, Integer percentage,
			String calculateType, Integer scale) {
		List<ImPriceList> result = new ArrayList(0);
		if (scale >= 0) {
			double percent = (double) percentage / 100;
			for (ImPriceList price : priceList) {
				// 依據調整比例計算調整後金額
				if ("2".equals(calculateType)) { // 無條件捨去

					price.setUnitPrice(OperationUtils.floorUp(
							price.getOriginalPrice() * percent, scale)
							.doubleValue());

				} else if ("3".equals(calculateType)) {// 無條件進位
					price.setUnitPrice(OperationUtils.ceilUp(
							price.getOriginalPrice() * percent, scale)
							.doubleValue());

				} else { // 四捨五入
					price.setUnitPrice(OperationUtils.roundUp(
							price.getOriginalPrice() * percent, scale)
							.doubleValue());
				}
				result.add(price);
			}
			return result;
		} else {
			return priceList;
		}

	}

	/**
	 * 啟動ImItemPrice之價格，於Process中使用
	 *
	 * @param headId
	 * @throws Exception
	 */
	public void updateStandardPrice(Long headId) throws Exception {
		try {
			System.out.println("mm");
			ImPriceAdjustment imPriceAdjustment = imPriceAdjustmentDAO.findById(headId);

			System.out.println("rr");
			if (imPriceAdjustment != null) {
				java.util.Date enableDate = imPriceAdjustment.getEnableDate();
				List<ImPriceList> imPriceLists = imPriceAdjustment.getImPriceLists();
				//啟用價格，並依啟用單上之價格更新
				for (ImPriceList imPriceList : imPriceLists) {

					ImItemPrice imItemPrice = imItemPriceDAO.findById(imPriceList.getPriceId());
					System.out.println("zz");
					if (imItemPrice != null) {
						System.out.println("nn");
						imItemPrice.setBeginDate(enableDate);
						System.out.println("aa");
						imItemPrice.setUnitPrice(imPriceList.getUnitPrice());
						System.out.println("bb");
						imItemPrice.setEnable("Y");
						imItemPrice.setPriceAdjustId(headId);
						imItemPrice.setLastUpdateDate(new Date());
						imItemPrice.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
						imItemPriceDAO.update(imItemPrice);
					} else {
						throw new NoSuchDataException("查無商品價格(ImItemPrice)，ID:"
								+ imPriceList.getPriceId() + "，標準售價啟動失敗");
					}
					System.out.println("cc");
					//更改標準進貨成本
					ImItem imItem = imItemDAO.findItem(imPriceAdjustment.getBrandCode(), imPriceList.getItemCode());
					if (imItem != null) {
						System.out.println("dd");
						imItem.setStandardPurchaseCost(imPriceList.getLocalCost());
						imItemPrice.setLastUpdateDate(new Date());
						imItemPrice.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
						System.out.println("ee");
						imItemDAO.update(imItem);
					}

				}
			} else {
				throw new NoSuchDataException("查無商品定價單(ImPriceAdjustment)，ID:"
						+ headId + "，標準售價啟動失敗");
			}
		} catch (Exception ex) {
			log.error("ID:" + headId + ",標準售價啟動時發生錯誤，原因：" + ex.toString());
			throw new Exception("ID:" + headId + ",標準售價啟動時發生錯誤，原因："
					+ ex.getMessage());
		}
	}

	/**
	 * 取得商品名稱及單位(Only for Update using)
	 *
	 * @param imPriceLists
	 * @throws Exception
	 */
	public void reflashImPriceListItemData(List<ImPriceList> imPriceLists)
			throws Exception {

		for (ImPriceList imPriceList : imPriceLists) {
			if ((imPriceList.getItemCode() != null && !("".equals(imPriceList.getItemCode())))) {
				try {
					imPriceList.setItemCode(imPriceList.getItemCode().toUpperCase());
					ImItem imItem = imItemDAO.findById(imPriceList.getItemCode());
					if (imItem != null) {
						imPriceList.setSalesUnit(imItem.getSalesUnit());
						imPriceList.setItemName(imItem.getItemCName());
						imPriceList.setCategory01(imItem.getCategory13());
					} else {
						imPriceList.setSalesUnit("");
						imPriceList.setItemName("查無商品資料");
					}
				} catch (Exception ex) {
					log.error(imPriceList.getItemCode() + "查詢商品資料時發生錯誤，原因："
							+ ex.toString());
					throw new Exception(imPriceList.getItemCode()
							+ "查詢商品資料時發生錯誤，原因：" + ex.getMessage());
				}
			}
		}
	}

	/**
	 * 檢查 ImPriceAdjustment 之正確性
	 *
	 * @param checkObj
	 *            (ImPriceAdjustment)
	 * @return
	 * @throws Exception
	 */
	public boolean checkImPriceListItemData(ImPriceAdjustment checkObj)
			throws Exception {
	    	if (null == checkObj.getEnableDate())
	    	    throw new FormException("啟用日期為空白，請重新輸入!");
		if (!StringUtils.hasText(checkObj.getBrandCode()))
			throw new FormException("品牌代號為空白，請重新輸入!");
		if (!StringUtils.hasText(checkObj.getPriceType()))
			throw new FormException("價格類別為空白，請重新輸入!");
		if("PAP".equals(checkObj.getOrderTypeCode())){
			if (!StringUtils.hasText(checkObj.getSupplierCode()))
				throw new FormException("供應商代號為空白，請重新輸入!");
			else{
				BuSupplierWithAddressView buSupplierWithAddressView =
					buSupplierWithAddressViewDAO.findById(checkObj.getSupplierCode() , checkObj.getBrandCode());
				if( null == buSupplierWithAddressView ){
					throw new FormException("供應商代號錯誤，請重新輸入!");
				}
				if(!(StringUtils.hasText(checkObj.getCurrencyCode()))){
					throw new FormException("幣別資料為空白!");
				}
			}
		}
		List<ImPriceList> imPriceLists = checkObj.getImPriceLists();
		if (imPriceLists.size() == 0)
			throw new FormException("您尚未輸入明細資料，請重新輸入!");
		int i = 1;
		for (ImPriceList imPriceList : imPriceLists) {
			if (StringUtils.hasText(imPriceList.getItemCode())) {
				this.checkPriceItemByValue(checkObj.getBrandCode(), checkObj
						.getOrderTypeCode(), checkObj.getOrderNo(), i, checkObj
						.getPriceType(), imPriceList);
				imPriceList.setReserve1("Okay ");

			} else {
				throw new FormException("明細資料第" + i + "筆品號未輸入，請重新輸入!");
			}

			i++;
		}

		return true;
	}

	/**
	 * 檢查送簽單之明細品號之正確性 PAJ)變價單 - 品號必須為價格資料已啟用之商品 PDP)定價單 - 品號必須為價格資料未啟用之商品
	 * CIA)組合商品送簽單 - 品號必須為價格資料未啟用之組合商品
	 *
	 * @param brandCode
	 * @param orderTypeCode
	 * @param orderNo
	 * @param lineNo
	 * @param priceType
	 * @param imPriceList
	 * @throws FormException
	 * @throws Exception
	 */
	private void checkPriceItemByValue(String brandCode, String orderTypeCode,
			String orderNo, int lineNo, String priceType,
			ImPriceList imPriceList) throws FormException {

		if ("PAJ".equals(orderTypeCode)) {
			List<ImItemCurrentPriceView> imItemCurrentPriceView = imItemCurrentPriceViewDAO
					.findCurrentPriceByValue(brandCode, imPriceList
							.getItemCode(), imPriceList.getItemCode(),
							priceType, "");
			if (imItemCurrentPriceView.size() == 0) {
				throw new NoSuchObjectException("商品變價單" + orderTypeCode + "-"
						+ orderNo + "中，第" + lineNo + "筆資料("
						+ imPriceList.getItemCode() + ")，查無已啟用之商品資料");
			}
		} else if ("PAP".equals(orderTypeCode)) {
			List<ImItemPriceView> imItemPriceView = imItemPriceViewDAO
					.findPriceViewByValue(brandCode, imPriceList.getItemCode(),
							imPriceList.getItemCode(), "", "", priceType, "N",
							"", "");
			if (imItemPriceView.size() == 0) {
				throw new NoSuchObjectException("商品定價送簽單" + orderTypeCode + "-"
						+ orderNo + "中，第" + lineNo + "筆資料("
						+ imPriceList.getItemCode() + ")，查無未啟用之商品資料");
			}/*else{
				if(!(StringUtils.hasText(imPriceList.getCurrencyCode()))){
					throw new NoSuchObjectException("商品定價送簽單" + orderTypeCode + "-"
							+ orderNo + "中，第" + lineNo + "筆資料("
							+ imPriceList.getItemCode() + ")，幣別資料為空白");
				}
			}*/
		} else if ("CIA".equals(orderTypeCode)) {
			List<ImItemPriceView> imItemPriceView = imItemPriceViewDAO
					.findPriceViewByValue(brandCode, imPriceList.getItemCode(),
							imPriceList.getItemCode(), "", "", priceType, "N",
							"", "and model.isComposeItem = 'Y'");
			if (imItemPriceView.size() == 0) {
				throw new NoSuchObjectException("組合商品送簽單" + orderTypeCode + "-"
						+ orderNo + "中，第" + lineNo + "筆資料("
						+ imPriceList.getItemCode() + ")，查無未啟用之組合商品資料");
			}
		} else {
			throw new FormException("單別資料錯誤「" + orderTypeCode + "」");
		}

	}


	/**
	 * 啟動ImItemPrice之價格，於Process中使用
	 *  查看資料庫中是否有已生效的價格資訊，如果有即使用此筆資料進行更新
	 *  如果沒有，使用ItemCode將整筆Item取出，並在Price中新增一筆價格資料
	 *  此用意為如價格在經過第一次調整後，user發現價格有誤時，
	 *  user可以再次新建一張變價單進行更新
	 * @param headId
	 * @throws Exception
	 */
	public String updateAdjustUnitPrice(Long headId) throws Exception {
		try {
			System.out.println("anber");
			String result = "";
			ImPriceAdjustment imPriceAdjustment = imPriceAdjustmentDAO.findById(headId);
			if (imPriceAdjustment != null) {
				java.util.Date enableDate = imPriceAdjustment.getEnableDate();
				List<ImPriceList> imPriceLists = imPriceAdjustment
						.getImPriceLists();

				for (ImPriceList imPriceList : imPriceLists) {
					//如果新價格<>原價才進行修改
					if(imPriceList.getOriginalPrice() != imPriceList.getUnitPrice()){
						// 查看資料庫中是否有已生效的價格資訊，如果有即使用此筆資料進行更新
						// 如果沒有，使用ItemCode將整筆Item取出，並在Price中新增一筆價格資料
						// 此用意為如價格在經過第一次調整後，user發現價格有誤時，
						// user可以再次新建一張變價單進行更新
						// 需增加判斷品牌(brandCode)，避免不同品牌，品號相同，發生資料修改錯誤的狀況 modify by Weichun 2011.06.10
						ImItemPrice imItemPrice = imItemPriceDAO.findEnablePrice(imPriceList.getItemCode(),
								imPriceAdjustment.getPriceType(), enableDate, imPriceAdjustment.getBrandCode());
						System.out.println("a:"+imItemPrice);
						if (imItemPrice == null) {
							ImItem item = imItemDAO.getLockedImItem(imPriceAdjustment.getBrandCode(), imPriceList.getItemCode());
							System.out.println("b:"+item);
							if (item != null) {

								List<ImItemPrice> imItemPrices = item.getImItemPrices();
								ImItemPrice imPrice = new ImItemPrice();
								imPrice.setPriceAdjustId(headId);
								imPrice.setItemCode(imPriceList.getItemCode());
								imPrice.setTypeCode(imPriceAdjustment.getPriceType());
								imPrice.setCurrencyCode(imPriceList.getCurrencyCode());
								imPrice.setUnitPrice(imPriceList.getUnitPrice());
								imPrice.setIsTax(imPriceList.getIsTax());
								imPrice.setTaxCode(imPriceList.getTaxCode());
								imPrice.setBeginDate(enableDate);
								imPrice.setEnable("Y");
								imPrice.setCreatedBy(imPriceAdjustment.getLastUpdatedBy());
								imPrice.setCreationDate(new Date());
								imPrice.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
								imPrice.setLastUpdateDate(new Date());
								imPrice.setIndexNo(imItemPrices.size()+1L);

								//imItemPrices.add(imItemPrice);
								//System.out.println("d:"+imItem.getImItemPrices().size());
								//imItem.setImItemPrices(imItemPrices);
								//System.out.println("e:"+imItem.getImItemPrices().get(1));
								//imItemDAO.update(imItem);
								imItemPriceDAO.save(imPrice);
								result =  "imItemPriceDAO.save";

							} else {
								throw new NoSuchDataException("查無商品資訊(ImItem)，ID:"
										+ imPriceList.getItemCode() + "，變價作業啟動失敗");
							}
						} else {
							System.out.println("c:"+headId);
							imItemPrice.setPriceAdjustId(headId);
							imItemPrice.setUnitPrice(imPriceList.getUnitPrice());
							imItemPrice.setBeginDate(enableDate);
							imItemPrice.setEnable("Y");
							imItemPrice.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
							imItemPrice.setLastUpdateDate(new Date());
							imItemPriceDAO.update(imItemPrice);
							result =  "imItemPriceDAO.update";
						}
					}
					//更改原廠 報價欄位
					if(imPriceList.getOriginalQuotationPrice() != imPriceList.getNewQuotationPrice()){
						ImItem imItem = imItemDAO.getLockedImItem(imPriceAdjustment.getBrandCode(), imPriceList.getItemCode());
						System.out.println("d:"+imItem);
						if (imItem != null) {
							imItem.setSupplierQuotationPrice(imPriceList.getNewQuotationPrice());
							imItem.setLastUpdatedBy(imPriceAdjustment.getLastUpdatedBy());
							imItem.setLastUpdateDate(new Date());
							imItemDAO.update(imItem);
							result =  "imItemDAO.update";
						} else {
							throw new NoSuchDataException("查無商品資訊(ImItem)，ID:"
									+ imPriceList.getItemCode() + "，變價作業啟動失敗(查無商品資料)");
						}
					}

				}

			} else {
				throw new NoSuchDataException("查無變價單(ImPriceAdjustment)，ID:"
						+ headId + "，變價作業啟動失敗");
			}
			return result;
		} catch (Exception ex) {
			log.error("ID:" + headId + ",變價作業啟動時發生錯誤，原因：" + ex.toString());
			throw new Exception("ID:" + headId + ",變價作業啟動時發生錯誤，原因："
					+ ex.getMessage());
		}
	}

	public void composeItem(String itemCode, Long quantity) throws NoSuchDataException{
		ImItem imItem = imItemDAO.findById(itemCode);
		if (imItem != null) {
			if("Y".equals(imItem.getIsComposeItem())){
				List<ImItemCompose> composeItems = imItem.getImItemComposes();
				for(ImItemCompose item : composeItems){
					Long reserveQuantity =  quantity * item.getQuantity();

				}
			}else{

			}
		}else{
			throw new NoSuchDataException("查無商品資訊(ImItem)，ID:"
					+ itemCode + "，商品組合失敗");
		}
	}

	public boolean isMarkdown(Long headId){
		ImPriceAdjustment imPriceAdjustment = imPriceAdjustmentDAO.findById(headId);
		boolean isMarkdown =true; //
		if (imPriceAdjustment != null) {
			List<ImPriceList> imPriceLists = imPriceAdjustment.getImPriceLists();
			for(ImPriceList imPriceList:imPriceLists){
				if (imPriceList.getOriginalPrice() < imPriceList.getUnitPrice() ) //漲價
					isMarkdown = false;
			}

		}
		return isMarkdown;
	}

	public void changeLocalCost(Long headId) throws Exception{
		imItemDAO.changeLocalCost(String.valueOf(headId));
	}


	public boolean isOverCEOLimitation(Long headId){
		ImPriceAdjustment imPriceAdjustment = imPriceAdjustmentDAO.findById(headId);
		boolean isOverCEOLimitation =true; //
		if (imPriceAdjustment != null) {
			List<ImPriceList> imPriceLists = imPriceAdjustment.getImPriceLists();
			for(ImPriceList imPriceList:imPriceLists){
				System.out.println(imPriceList.getGrossProfitRate());
				System.out.println(imPriceList.getGrossProfitRate() >= 5D);
				System.out.println(imPriceList.getGrossProfitRate() <= -5D);
				if (imPriceList.getGrossProfitRate() > -5D && imPriceList.getGrossProfitRate() < 5D) //毛利率差異+-5%將送總經理審核
					isOverCEOLimitation = false;

			}

		}
		return isOverCEOLimitation;
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
			NoSuchMethodException ,Exception{
		log.info("ImPriceAdjustmentService.getAJAXPageData");

		// 要顯示的HEAD_ID
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		String orderTypeCode = httpRequest.getProperty("orderTypeCode");
		List<Properties> re = new ArrayList();
		List<Properties> gridDatas = new ArrayList();
		int iSPage = AjaxUtils.getStartPage(httpRequest);
		int iPSize = AjaxUtils.getPageSize(httpRequest);

		log.info("ImPriceAdjustmentService.getAJAXPageData headId=" + headId + ",iSPage=" + iSPage + ",iPSize=" + iPSize);
		log.error("ImPriceAdjustmentService.getAJAXPageData headId=" + headId + ",iSPage=" + iSPage + ",iPSize=" + iPSize);
		ImPriceAdjustment imPriceAdjustment=imPriceAdjustmentDAO.findById(headId);
		String brandCode = imPriceAdjustment == null ? "" : imPriceAdjustment.getBrandCode();

		List<ImPriceList> imPriceLists =  imPriceListService.findPageLine(headId, iSPage, iPSize);
		if (null != imPriceLists && imPriceLists.size() > 0) {
			log.info("ImPriceListService.getAJAXPageData AjaxUtils.imPriceList=" + imPriceLists.size());
			//String sqlItemCodes="";
			for(Iterator iter = imPriceLists.iterator();iter.hasNext();){
				ImPriceList imPriceObj = (ImPriceList)iter.next();
				ImItem imItem = imItemDAO.findItem(brandCode, imPriceObj.getItemCode());
				imPriceObj.setItemCName(imItem==null ? "":imItem.getItemCName());
			}
			// 取得第一筆的INDEX
			Long firstIndex = imPriceLists.get(0).getIndexNo();
			// 取得最後一筆 INDEX
			Long maxIndex = imPriceListService.findPageLineMaxIndex(Long.valueOf(headId));
			if("PAP".equals(orderTypeCode)){
				re.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, imPriceLists, gridDatas,
						firstIndex, maxIndex));
			}else{
				re.add(AjaxUtils.getAJAXPageData(httpRequest, PAJ_GRID_FIELD_NAMES, PAJ_GRID_FIELD_DEFAULT_VALUES, imPriceLists, gridDatas,
						firstIndex, maxIndex));
			}


		} else {
			if("PAP".equals(orderTypeCode)){
				re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));
			}else{
				re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, PAJ_GRID_FIELD_NAMES, PAJ_GRID_FIELD_DEFAULT_VALUES, gridDatas));
			}

		}
		log.error("------is Here");
		return re;
	}
	/**
	 * AJAX Line calc costRate
	 * @param Properties
	 * @throws Excepiton
	 */
	public List<Properties> getAJAXGrossProfitRate(Properties httpRequest)throws FormException{
		log.info("ImPriceAdjustmentService.getAJAXGrossProfitRate");
		List re = new ArrayList();
		String originalPrice = httpRequest.getProperty("originalPrice");
		String originalQuotationPrice = httpRequest.getProperty("originalQuotationPrice");
		String exchangeRate = httpRequest.getProperty("exchangeRate");
		String newQuotationPrice = httpRequest.getProperty("newQuotationPrice");
		String unitPrice = httpRequest.getProperty("unitPrice");
		String grossProfitRate = calGrossProfitRate(originalPrice,originalQuotationPrice,exchangeRate,unitPrice,newQuotationPrice);
		try{
			Properties pro = new Properties();
			pro.setProperty("GrossProfitRate", grossProfitRate);
			re.add(pro);
		}catch(Exception e){
			e.printStackTrace();
		}
		return re;
	}


	/**
	 * AJAX Line calc costRate
	 * @param Properties
	 * @throws Excepiton
	 */
	public List<Properties> getAJAXCalcCostRate(Properties httpRequest)throws FormException{
		log.info("ImPriceAdjustmentService.getAJAXLineData");
		List re = new ArrayList();
		String localCost = httpRequest.getProperty("localCost");
		String unitPrice = httpRequest.getProperty("unitPrice");
		String resultRate = calCostRate(localCost,unitPrice);
		try{
			Properties pro = new Properties();
			pro.setProperty("CostRate", resultRate);
			re.add(pro);
		}catch(Exception e){
			e.printStackTrace();
		}
		return re;
	}
	public List<String> getAJAXLineJSONData(Map jMap) throws FormException{
		System.out.println(jMap);
		return new ArrayList();
	}
	public List getItemInfo(Properties httpRequest) throws Exception{
		List result = new ArrayList();
		String brandCode = httpRequest.getProperty("brandCode");
		String orderTypeCode = httpRequest.getProperty("orderTypeCode");
		String itemCode = httpRequest.getProperty("itemCode");
		String priceType = httpRequest.getProperty("priceType");
		String exchangeRate = httpRequest.getProperty("exchangeRate");
		String ratio = httpRequest.getProperty("ratio");
		StringBuffer nativeSQL = new StringBuffer("");
		if("PAP".equals(orderTypeCode)){
			nativeSQL = new StringBuffer("SELECT ITEM_CODE, NVL(ITEM_C_NAME,' ') AS ITEM_C_NAME, PRICE_ID, CATEGORY13, SUPPIER_QUOTATION_PRICE AS FOREIGN_COST, (NVL(SUPPIER_QUOTATION_PRICE,0)  * ")
			.append( exchangeRate+" * "+ ratio ).append(") AS LOCAL_COST, ROUND((NVL(SUPPIER_QUOTATION_PRICE,0) * ").append( exchangeRate+" * "+ ratio ).append(" /UNIT_PRICE) * 100,2) AS COST_RATE , UNIT_PRICE FROM IM_ITEM_PRICE_VIEW WHERE BRAND_CODE ='")
			.append(brandCode).append("' AND ITEM_CODE ='").append(itemCode).append("' AND TYPE_CODE ='").append(priceType).append("' AND PRICE_ENABLE='N' AND ITEM_ENABLE='Y'");
		}else{
			nativeSQL = new StringBuffer("SELECT ITEM_C_NAME, SUPPIER_QUOTATION_PRICE, UNIT_PRICE FROM IM_ITEM_CURRENT_PRICE_VIEW WHERE BRAND_CODE ='").append(brandCode).append("'")
			.append(" AND ITEM_CODE ='").append(itemCode).append("'").append(" AND TYPE_CODE ='").append(priceType).append("'");
		}
		log.info("nativeSQL = " + nativeSQL.toString());
		result = nativeQueryDAO.executeNativeSql(nativeSQL.toString());
		return result;
	}
	/**
	 * AJAX Line Change
	 *
	 * @param Properties
	 * @throws Exception
	 */
	public List<Properties> getAJAXLineData(Properties httpRequest) throws FormException {

		log.info("ImPriceAdjustmentService.getAJAXLineData");
		List re = new ArrayList();
		String brandCode = httpRequest.getProperty("brandCode");
		String orderTypeCode = httpRequest.getProperty("orderTypeCode");
		String itemCode = httpRequest.getProperty("itemCode");
		//String lineId = httpRequest.getProperty("lineId");
		//String headId = httpRequest.getProperty("headId");
		String exchangeRate = httpRequest.getProperty("exchangeRate");
		//String ratio = httpRequest.getProperty("ratio");
		//String priceType = httpRequest.getProperty("priceType");
		String newQuotationPrice = httpRequest.getProperty("newQuotationPrice");
		System.out.println("ImPriceAdjsutmentService.getAJAXLineData brandCode=" + brandCode + ",orderTypeCode=" + orderTypeCode
				+ ",itemCode=" + itemCode);
		if (StringUtils.hasText(brandCode) && StringUtils.hasText(orderTypeCode) && StringUtils.hasText(itemCode)) {
			try {
				Properties pro = new Properties();
				//StringBuffer nativeSQL = new StringBuffer("");
				List resultList = getItemInfo(httpRequest);
				String itemCName = "";
				String unitPrice = "";
				if("PAP".equals(orderTypeCode)){
					itemCName = AjaxUtils.getPropertiesValue(resultList.isEmpty()?null:((Object[])resultList.get(0))[1],
							MessageStatus.DATA_NOT_FOUND);
					String category01 = AjaxUtils.getPropertiesValue(resultList.isEmpty()?null:((Object[])resultList.get(0))[3], " ");
					unitPrice = AjaxUtils.getPropertiesValue(resultList.isEmpty()?null:((Object[])resultList.get(0))[7], "0");
					String foreignCost = AjaxUtils.getPropertiesValue(resultList.isEmpty()?null:((Object[])resultList.get(0))[4], "0");
					String localCost = AjaxUtils.getPropertiesValue(resultList.isEmpty()?null:((Object[])resultList.get(0))[5],"0");
					String costRate = calCostRate(localCost,unitPrice);
					pro.setProperty("Category01",category01);
					pro.setProperty("ForeignCost", foreignCost);
					pro.setProperty("LocalCost", localCost);
					pro.setProperty("CostRate", costRate);
				}else{
					itemCName = AjaxUtils.getPropertiesValue(resultList.isEmpty()?null:((Object[])resultList.get(0))[0],
							MessageStatus.DATA_NOT_FOUND);
					unitPrice = AjaxUtils.getPropertiesValue(resultList.isEmpty()?null:((Object[])resultList.get(0))[2], "0");
					String suppierQuotationPrice = AjaxUtils.getPropertiesValue(resultList.isEmpty()?null:((Object[])resultList.get(0))[1],"0");
					pro.setProperty("OrginalPrice",unitPrice);
					pro.setProperty("OrginalQuotationPrice",suppierQuotationPrice);
					String grossProfitRate = calGrossProfitRate(unitPrice,suppierQuotationPrice,exchangeRate,unitPrice,newQuotationPrice);
					pro.setProperty("GrossProfitRate", grossProfitRate);
				}
				System.out.println("ImPriceListService.getAJAXLineData itemCName=" + itemCName);
				pro.setProperty("UnitPrice", unitPrice);
				pro.setProperty("ItemCName", itemCName);

				re.add(pro);
			} catch (Exception e) {
				e.printStackTrace();
			}

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
		log.info("ImPriceAdjustmentService.updateAJAXPageLinesData");
		String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		String status = httpRequest.getProperty("status");
		String brandCode = httpRequest.getProperty("brandCode");
		//Double exchangeRate = NumberUtils.getDouble(httpRequest.getProperty("exchangeRate"));
		String orderTypeCode = httpRequest.getProperty("orderTypeCode");
		String errorMsg = null;
		log.info("ImPriceAdjustmentService.updateAJAXPageLinesData gridLineFirstIndex=" + gridLineFirstIndex + ",gridRowCount="
				+ gridRowCount + ",headId=" + headId + ",status=" + status + ",gridData=" + gridData);

		if (OrderStatus.SAVE.equalsIgnoreCase(status)) {
			// 取得LINE MAX INDEX
			// ImPriceAdjustment head = (ImPriceAdjustment)
			// imPriceAdjustmentDAO.findByPrimaryKey(headId);
			// CREATE TMP HEAD
			ImPriceAdjustment head = new ImPriceAdjustment();
			head.setHeadId(headId);
			head.setBrandCode(brandCode);
			head.setOrderTypeCode(orderTypeCode);
			// 將STRING資料轉成List Properties record data
			if("PAP".equals(orderTypeCode)){

			}else{

			}
			List<Properties> upRecords = null;
			if("PAP".equals(orderTypeCode)){
				upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
						ImPriceAdjustmentService.GRID_FIELD_NAMES);
			}else{
				upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
						ImPriceAdjustmentService.PAJ_GRID_FIELD_NAMES);
			}
			// Get INDEX NO
			int indexNo = imPriceListService.findPageLineMaxIndex(headId).intValue();
			if (null != upRecords && null != head) {
				for (Properties upRecord : upRecords) {
					// 先載入HEAD_ID OR LINE DATA
					Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));

					log.info("head Id=" + head.getHeadId() + " ,line Id=" + lineId);
					String itemCode = "";
					if("PAP".equals(orderTypeCode)){
						itemCode = upRecord.getProperty(GRID_FIELD_NAMES[2]);
					}else{
						itemCode = upRecord.getProperty(PAJ_GRID_FIELD_NAMES[1]);
						System.out.println(PAJ_GRID_FIELD_NAMES[1]+"--"+PAJ_GRID_FIELD_NAMES[0]+"--"+PAJ_GRID_FIELD_NAMES[2]);
					}
					// 如果沒有ITEM CODE 就不會新增或修改
					if (StringUtils.hasText(itemCode)) {
						ImPriceList imPriceList = imPriceListService.findLine(headId,lineId);
						if (null != imPriceList) {
							// UPDATE Properties
							// setLine(poPurchaseOrderLine, upRecord);
							if("PAP".equals(orderTypeCode)){
								AjaxUtils.setPojoProperties(imPriceList, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
							}else{
								AjaxUtils.setPojoProperties(imPriceList, upRecord, PAJ_GRID_FIELD_NAMES, PAJ_GRID_FIELD_TYPES);
							}

							// LINE 計算
							imPriceListDAO.update(imPriceList);
						} else {
							// INSERT Properties
							indexNo++;
							imPriceList = new ImPriceList();
							// setLine(poPurchaseOrderLine, upRecord);
							if("PAP".equals(orderTypeCode)){
								AjaxUtils.setPojoProperties(imPriceList, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
							}else{
								AjaxUtils.setPojoProperties(imPriceList, upRecord, PAJ_GRID_FIELD_NAMES, PAJ_GRID_FIELD_TYPES);
							}

							imPriceList.setIndexNo(Long.valueOf(indexNo));
							imPriceList.setImPriceAdjustment(head);
							// LINE 計算
							imPriceListDAO.save(imPriceList);
						}
						// return new page
						// getAJAXPageData(httpRequest);
					} else {
						// line 沒有 ITEM_CODE
					}
				}
			} else {
				errorMsg = "沒有定價單單頭資料";
			}
		} else {
			// 如果狀態不是暫存 是否要提示訊息
		}

		return AjaxUtils.getResponseMsg(errorMsg);
	}
	public Map updateAJAXHeadData(Map parameterMap)throws FormException,Exception{
		 List<Properties> result = new ArrayList();
		 StringBuffer retMessage = new StringBuffer();
		 Map resultMap = new HashMap();
		 Properties pros = new Properties();
		 Object formBindBean = parameterMap.get("vatBeanFormBind");
		 Object formLinkBean = parameterMap.get("vatBeanFormLink");
		 Object otherBean = parameterMap.get("vatBeanOther");
		 String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
		 String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		 Long processId = NumberUtils.getLong( (String)PropertyUtils.getProperty(otherBean, "processId"));
		 try{
			 System.out.println("---------is-------Start1");
			 ImPriceAdjustment imPriceObject = getActualImPriceAdjustment(formLinkBean);
			 System.out.println("---------is-------Start2");
			 AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imPriceObject);
			 System.out.println("---------is-------Start3CopyOver");
			 String tmpStuts = imMovementService.getStatus(formAction, imPriceObject.getHeadId(), processId, imPriceObject.getStatus(), "", imPriceObject.getOrderTypeCode(), imPriceObject.getOrderNo());
			 imPriceObject.setStatus(tmpStuts);
			 String retnMessage = modifyImPriceAdjustmentHead(imPriceObject,employeeCode, formAction);
			 System.out.println("---------is-------Start4");
			 resultMap.put("entityBean", imPriceObject);
			 resultMap.put("resultMsg", retnMessage);
		 }catch(Exception ex){
			 retMessage.append("變價單存檔發生錯誤，原因:"+ex.toString());
			 System.out.println(retMessage);
			 log.error(retMessage);
		 }
		 return resultMap;

	}
	private ImPriceAdjustment getActualImPriceAdjustment(Object bean)throws FormException,Exception{
		ImPriceAdjustment imPriceObject = null;
		String headId = (String)PropertyUtils.getProperty(bean, "headId");
		if(StringUtils.hasText(headId)){
			 imPriceObject = imPriceAdjustmentDAO.findById(NumberUtils.getLong(headId));
			if(imPriceObject == null){
				throw new NoSuchObjectException("查無變價單主鍵"+headId+"資料");
			}
		}else{
			throw new ValidationErrorException("傳入變價單主鍵"+headId+"為空值");
		}

		return imPriceObject;
	}

	private String modifyImPriceAdjustmentHead(ImPriceAdjustment imPriceObject,String loginId , String formAction)throws ObtainSerialNoFailedException, FormException,Exception{
		if(AjaxUtils.isTmpOrderNo(imPriceObject.getOrderNo())){
			String serialNo = buOrderTypeService.getOrderSerialNo(imPriceObject.getBrandCode(), imPriceObject.getOrderTypeCode());
			if(serialNo.equals("unknow")){
				throw new ObtainSerialNoFailedException("取得"+imPriceObject.getOrderTypeCode()+"單號失敗");
			}else{
				imPriceObject.setOrderNo(serialNo);
			}
		}
		if(OrderStatus.FORM_SUBMIT.equals(formAction)){
			imPriceObject.setLastUpdatedBy(loginId);
			imPriceObject.setLastUpdateDate(new Date());
			if(checkImPriceListItemData(imPriceObject)){
				System.out.println("--------is---Here--------"+new Date());
				imPriceAdjustmentDAO.update(imPriceObject);
				System.out.println("--------is----Over------"+new Date());
			}
		}else{
			log.error(new Date());
			imPriceAdjustmentDAO.update(imPriceObject);
			log.error(new Date());
		}

		return imPriceObject.getOrderTypeCode()+"-"+imPriceObject.getOrderNo()+"存檔成功";
	}

	private String calCostRate(String localCost,String unitPrice){
		String calResult="0";
		double tmpResult = 0L;
		if(null==localCost || null == unitPrice || "0".equals(unitPrice)){
			return calResult;
		}else{
			tmpResult = (Double.parseDouble(localCost)/Double.parseDouble(unitPrice))*100;
			calResult = NumberUtils.roundToStr(tmpResult*100, 0);
		}

		return calResult;
	}

	private String calGrossProfitRate(String oPrice,String oQuotation,String exchangeRate,String unitPrice,String newQuotation){
		String calResult="0";
		double tmpResult = 0L;
		double tmpNew = 0L;
		if(null==newQuotation || "0".equals(newQuotation) || null == unitPrice || "0".equals(unitPrice) || null == oPrice || "0".equals(oPrice)){
			return calResult;
		}else{
			tmpResult = (Double.parseDouble(oPrice)-(Double.parseDouble(oQuotation)*Double.parseDouble(exchangeRate)))/Double.parseDouble(oPrice);
			tmpNew =  (Double.parseDouble(unitPrice)-(Double.parseDouble(newQuotation)*Double.parseDouble(exchangeRate)))/Double.parseDouble(unitPrice);

			calResult = NumberUtils.roundToStr((tmpResult-tmpNew)*100, 0);
		}

		return calResult;
	}
	public List<Properties> executeInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
			Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
			System.out.println("formId:"+formId);
			Map multiList = new HashMap(0);

			ImPriceAdjustment form = null == formId ? executeNewImPriceAdjustment(otherBean, resultMap):findExistImPriceAdjustment(otherBean, resultMap);
			if(form != null && form.getSupplierCode() != null ){
			    String supplierCode = form.getSupplierCode();
			    String brandCode = form.getBrandCode();
			    String supplierName ="";
			    BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(brandCode, supplierCode);
			    if(buSWAV != null){
				supplierName = AjaxUtils.getPropertiesValue(buSWAV.getChineseName(), "");
			    }
			    form.setSupplierName(supplierName);
			}
			form.setReserve3(UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(form.getBrandCode() ,"PA");
			List<BuCurrency> allCurrency = buBasicDataService.findCurrencyList("", "", "");
			BuCurrency tmpC = new BuCurrency();
			tmpC.setCurrencyCode("TW");
			tmpC.setCurrencyCName("新台幣");
			allCurrency.add(0,tmpC);
			List<BuCommonPhraseLine> allPriceType =  buCommonPhraseService.getCommonPhraseLinesById("PriceType",false);
			multiList.put("allCurrencys", AjaxUtils.produceSelectorData(allCurrency ,"currencyCode" ,"currencyCName",  true,  false));
			multiList.put("allPriceTypes" , AjaxUtils.produceSelectorData(allPriceType  ,"lineCode" ,"name",  true,  true));
			multiList.put("allOrderTypes" , AjaxUtils.produceSelectorData(allOrderTypes ,"orderTypeCode" ,"name",  true,  true));
			resultMap.put("multiList",multiList);
			resultMap.put("form", form);
			String brandName = (buBrandService.findById(form.getBrandCode())).getBrandName();
			resultMap.put("brandName", brandName);
			resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
			//resultMap.put("inputFormDate", form.getLastUpdateDate());
			System.out.println("executeInitial.OrderTypeCode:" + form.getOrderTypeCode() +" OrderNo:" + form.getOrderNo());
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
	public ImPriceAdjustment findExistImPriceAdjustment(Object otherBean,Map resultMap)throws Exception{
		String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
		Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
		ImPriceAdjustment form = imPriceAdjustmentDAO.findById(formId);
		if(form != null){
			reflashImPriceListItemData(form.getImPriceLists());
			//form.setImPriceLists(null);
			return form;
		}else{
			throw new NoSuchDataException("查無此單號("+formId+")，於按下「確認」鍵後，將關閉本視窗！");
		}
	}

	public ImPriceAdjustment executeNewImPriceAdjustment(Object otherBean,Map resultMap)throws Exception{
		String loginBrandCode     = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
		String loginEmployeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode      = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
		ImPriceAdjustment form = new ImPriceAdjustment();
		form.setBrandCode(loginBrandCode);
		form.setOrderTypeCode(orderTypeCode);
		form.setPriceType("1");
		form.setSupplierCode("");
		form.setEnableDate(null);
		form.setExchangeRate(1D);
		form.setRatio(1D);
		form.setImPriceLists(null);
		form.setDescription("");
		form.setStatus(OrderStatus.SAVE);
		form.setCreatedBy(loginEmployeeCode);
		saveTmp(form);
		return form;

	}
	public static Object[] startProcess(ImPriceAdjustment form) throws Exception{
        try
        {
            String packageId = "Im_ItemPriceAdjustment";
            String processId = "StandardPriceApproval";
            String version = "20090824";
            String sourceReferenceType = "";
            HashMap context = new HashMap();
            context.put("brandCode", form.getBrandCode());
            context.put("formId", form.getHeadId());
            context.put("orderType", form.getOrderTypeCode());
            context.put("orderNo", form.getOrderNo());
            context.put("isThreePointMoving", false);
            return ProcessHandling.start(packageId, processId,
            		version, sourceReferenceType, context);
        } catch(Exception e){
        	log.error("商品定價變價流程執行時發生錯誤，原因：" + e.toString());
			throw new ProcessFailedException(e.getMessage());
        }

	 }

	 public static Object[] completeAssignment(long assignmentId, boolean approveResult,String approvalComment ) throws Exception{
        try
        {

            HashMap context = new HashMap();
            context.put("approveResult", approveResult);
            context.put("approvalComment", approvalComment);
            context.put("form", approveResult);
            return ProcessHandling.completeAssignment(assignmentId, context);
        }
        catch (Exception e){
            log.error("完成商品定價變價任務時發生錯誤："+e.getMessage());
            throw new Exception(e);
        }

	 }
	 /**
      * 匯入商品定價變價明細
      *
      * @param headId
      * @param priceLists
      * @throws Exception
      */
     public void executeImportPriceLists(Long headId, List priceLists) throws Exception{

         try{
     	imPriceListService.deletePriceLists(headId);
     	if(priceLists != null && priceLists.size() > 0){
     	    ImPriceAdjustment imPriceAdjustmentTmp = new ImPriceAdjustment();
     	   imPriceAdjustmentTmp.setHeadId(headId);
     	    for(int i = 0; i < priceLists.size(); i++){
     		ImPriceList  imPriceList = (ImPriceList)priceLists.get(i);
     		imPriceList.setImPriceAdjustment(imPriceAdjustmentTmp);
     		imPriceList.setIndexNo(i + 1L);
     		imPriceListDAO.save(imPriceList);
     	    }
     	}
         }catch (Exception ex) {
 	        log.error("商品定價變價明細匯入時發生錯誤，原因：" + ex.toString());
 	        throw new Exception("商品定價變價明細匯入時發生錯誤，原因：" + ex.getMessage());
 	    }
     }
         public List<Properties> executeSearchInitial(Map parameterMap) throws Exception{
    		Map resultMap = new HashMap(0);

    		try{
    			Object otherBean = parameterMap.get("vatBeanOther");
    			String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
    			Map multiList = new HashMap(0);


    			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(loginBrandCode ,"PA");
    			multiList.put("allOrderTypes"        , AjaxUtils.produceSelectorData(allOrderTypes         ,"orderTypeCode" ,"name"         ,  true,  true));
    			resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
    			resultMap.put("multiList",multiList);

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
    	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
    	    String startOrderNo = httpRequest.getProperty("startOrderNo");
    	    String endOrderNo = httpRequest.getProperty("endOrderNo");

    	    String startDate = httpRequest.getProperty("startDate");
    	    String endDate = httpRequest.getProperty("endDate");

    	    String status = httpRequest.getProperty("status");

    	    HashMap map = new HashMap();
    	    HashMap findObjs = new HashMap();
    	    findObjs.put("brandCode",brandCode);
    	    findObjs.put("orderTypeCode",orderTypeCode) ;
    	    findObjs.put("startOrderNo",startOrderNo) ;
    	    findObjs.put("endOrderNo",endOrderNo) ;
    	    findObjs.put("startDate",startDate) ;
    	    findObjs.put("endDate",endDate) ;
    	    findObjs.put("status",status) ;


    	    HashMap tmpResultMap = imPriceAdjustmentDAO.findPageLine(findObjs, iSPage, iPSize);
    	    List<ImPriceAdjustment> imPriceAdjustments = null;
    	    if(!tmpResultMap.isEmpty()){
    		imPriceAdjustments = (List<ImPriceAdjustment>)tmpResultMap.get("form");
    	    }

    	    if (imPriceAdjustments != null && imPriceAdjustments.size() > 0) {
    	    	Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
    	    	Long maxIndex = (Long)imPriceAdjustmentDAO.findPageLine(findObjs,-1,iPSize).get("recordCount");	// 取得最後一筆 INDEX

    	    	for(ImPriceAdjustment imPriceAdjustment: imPriceAdjustments){
    	    		imPriceAdjustment.setStatus(OrderStatus.getChineseWord(imPriceAdjustment.getStatus()));
    	    	}
    	    	log.info("ImPriceAdjustment.AjaxUtils.getAJAXPageData ");
    	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,imPriceAdjustments, gridDatas, firstIndex, maxIndex));
    	    } else {
    	    	log.info("ImPriceAdjustment.AjaxUtils.getAJAXPageDataDefault ");
    	    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));

    	    }

    	    return result;
    	}catch(Exception ex){
    	    log.error("載入頁面顯示的調撥查詢發生錯誤，原因：" + ex.toString());
    	    throw new Exception("載入頁面顯示的調撥查詢失敗！");
    	}
    }

    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
     String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception{
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
		log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
		Object pickerBean = parameterMap.get("vatBeanPicker");
		String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
		ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
		log.info("getSearchSelection.picker_parameter:" + timeScope +"/"+ searchKeys.toString());

		List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
		log.info("getSearchSelection.result:" + result.size());
		if(result.size() > 0 )
			pickerResult.put("result", result);
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
}
