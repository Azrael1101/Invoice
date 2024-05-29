package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import tw.com.tm.erp.hbm.dao.BaseDAO;

import javax.servlet.http.HttpServletRequest;

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
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHand;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHandView;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceLine;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemDiscount;
import tw.com.tm.erp.hbm.bean.ImItemDiscountHead;
import tw.com.tm.erp.hbm.bean.ImItemDiscountLine;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImOnHandId;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImReceiveExpense;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.ImSysLog;
import tw.com.tm.erp.hbm.bean.ImTransation;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployee;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployeeId;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.bean.PoVerificationSheet;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceHeadDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.ImTransationDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.PoVerificationSheetDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;
import tw.com.tm.erp.hbm.dao.ImSysLogDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationItemDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
/**
 * 調整單 Service
 *
 * @author T02049
 *
 */
public class ImAdjustmentHeadService {
	private static final Log log = LogFactory.getLog(ImAdjustmentHeadService.class);
	public static final String PROGRAM_ID= "IM_ADJUSTMENT_HEAD";
	private ImAdjustmentHeadDAO imAdjustmentHeadDAO;
	private BuOrderTypeService buOrderTypeService;
	private ImItemDAO imItemDAO;
	private ImTransationDAO imTransationDAO;
	private ImWarehouseDAO imWarehouseDAO;
	private ImItemService imItemService;
	// private ImReceiveHeadService imReceiveHeadService;
	private ImReceiveHeadDAO imReceiveHeadDAO;
	private ImOnHandDAO imOnHandDAO;
	private PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO;
	private FiInvoiceHeadService fiInvoiceHeadService;
	private NativeQueryDAO nativeQueryDAO;
	private ImAdjustmentLineService imAdjustmentLineService;
	private ImAdjustmentLineDAO imAdjustmentLineDAO;
	private PoPurchaseOrderLineService poPurchaseOrderLineService;
	private ImMovementService imMovementService;
	private SiProgramLogAction siProgramLogAction;
	private PoVerificationSheetDAO       poVerificationSheetDAO;
	private FiInvoiceHeadDAO       fiInvoiceHeadDAO;
	private ImSysLogDAO imSysLogDAO;
	private BuEmployeeDAO buEmployeeDAO;
	private CmDeclarationHead cmDeclarationHead;
	private CmDeclarationItem cmDeclarationItem;
	private CmDeclarationHeadDAO cmDeclarationHeadDAO;
	private CmDeclarationItemDAO cmDeclarationItemDAO;
	private CmDeclarationOnHandDAO cmDeclarationOnHandDAO;
	private CmDeclarationOnHandViewDAO cmDeclarationOnHandViewDAO;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	private BaseDAO baseDAO;
	
	public static final String[] GRID_FIELD_NAMES = { "indexNo", "itemCode", "warehouseCode", "lotNo", "difQuantity", "localUnitCost", "amount",
		"sourceItemCode", "accountCode", "reason", "lineId",
		"status", "isLockRecord", "isDeleteRecord", "message" };
	
	public static final String[] GRID_FIELDS_NAMES = { "indexNo", "originalDeclarationNo", "originalDeclarationSeq", "reserve2", "reserve3", 
		"customsItemCode", "itemCName", "orgImportDate", "expiryDate", "extensionDate", "unit", "qty", "reserve1", "lineId"};

	public static final int[] GRID_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING };
	
	public static final int[] GRID_FIELDS_TYPES = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_DATE, 
		AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_LONG};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = { "0", "", "", "00000000000", "0", "0", "0", "", "", "", "", "",
		AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "" };
	
	public static final String[] GRID_FIELDS_DEFAULT_VALUES = { "", "", "", "", "", "", "", "", "", "", "", "0", "0", "0"};

	
	/**
     * 報單展延查詢picker用的欄位
     */
    public static final String[] GRID_SEARCH_FIELD_NAMES = {
	"orderNo", "fileNo",  
	"statusName", "lastUpdateDate","headId"
    };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"",""
    };
	
	/**
	 * AJAX Line Change
	 *
	 * @param Properties
	 * @throws Exception
	 */
	public List<Properties> getAJAXLineData(Properties httpRequest) throws FormException {

		log.info("ImAdjustmentHeadService.getAJAXLineData");
		List re = new ArrayList();
		String brandCode = httpRequest.getProperty("brandCode");
		String orderTypeCode = httpRequest.getProperty("orderTypeCode");
		String itemCode = httpRequest.getProperty("itemCode");
		String lineId = httpRequest.getProperty("lineId");
		String headId = httpRequest.getProperty("headId");
		String difQuantity = httpRequest.getProperty("difQuantity");
		String localUnitCost = httpRequest.getProperty("localUnitCost");
		Double dCost = StringUtils.hasText(localUnitCost) ? new Double(localUnitCost) : new Double(0.0d);
		String priceType = httpRequest.getProperty("priceType");
		String newQuotationPrice = httpRequest.getProperty("newQuotationPrice");
		System.out.println("ImAdjsutmentHeadService.getAJAXLineData brandCode=" + brandCode + ",orderTypeCode="
				+ orderTypeCode + ",itemCode=" + itemCode);
		if (StringUtils.hasText(brandCode) && StringUtils.hasText(orderTypeCode) && StringUtils.hasText(itemCode)) {

			try {
				Properties pro = new Properties();
				// ImPriceList objImPriceList =
				// imPriceListService.findLine(headId, lineId);
				if ("IDA".equalsIgnoreCase(orderTypeCode) || "ICA".equalsIgnoreCase(orderTypeCode) || "ACA".equalsIgnoreCase(orderTypeCode) // T2新增"ACA 拆併貨調整單" by Weichun 2011.11.22
						|| "ACA".equalsIgnoreCase(orderTypeCode) || "IAJ".equalsIgnoreCase(orderTypeCode)) {
					if ("IAJ".equalsIgnoreCase(orderTypeCode) && dCost <= 0) {
						dCost = poPurchaseOrderLineService.getOriginalUnitPriceDouble(brandCode, orderTypeCode, itemCode);
					} else if (dCost <= 0) {
						dCost = getOldestUnitPrice(itemCode);
					}
					localUnitCost = dCost.toString();
				} else {
					localUnitCost = "0.0";
				}
				double tmpDif = StringUtils.hasText(difQuantity) ? Double.parseDouble(difQuantity) : 0.0d;
				double tmpAmount = 0.0;
				double tmpLocal = Double.parseDouble(localUnitCost);
				tmpAmount = tmpDif * tmpLocal;

				System.out.println("ImAdjustmentHeadService.getAJAXLineData localUnitCost=" + localUnitCost);
				pro.setProperty("LocalUnitCost", localUnitCost);
				pro.setProperty("Amount", String.valueOf(tmpAmount));
				re.add(pro);
			} catch (Exception e) {
				e.printStackTrace();
			}

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
		log.info("ImAdjustmentHeadService.getAJAXPageData");

		// 要顯示的HEAD_ID
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		List<Properties> re = new ArrayList();
		List<Properties> gridDatas = new ArrayList();
		int iSPage = AjaxUtils.getStartPage(httpRequest);
		int iPSize = AjaxUtils.getPageSize(httpRequest);

		log.info("ImAdjustmentHeadService.getAJAXPageData headId=" + headId + ",iSPage=" + iSPage + ",iPSize=" + iPSize);
		List<ImAdjustmentLine> imAdjustmentLines = imAdjustmentLineService.findPageLine(headId, iSPage, iPSize);
		if (null != imAdjustmentLines && imAdjustmentLines.size() > 0) {
			log.info("ImAdjustmentHeadService.getAJAXPageData AjaxUtils.imAdjustmentLines=" + imAdjustmentLines.size());


			// 取得第一筆的INDEX
			Long firstIndex = imAdjustmentLines.get(0).getIndexNo();
			// 取得最後一筆 INDEX
			Long maxIndex = imAdjustmentLineService.findPageLineMaxIndex(Long.valueOf(headId));

			re.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, imAdjustmentLines, gridDatas,
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
		log.info("ImAdjustmentHeadService.updateAJAXPageLinesData");
		String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		String status = httpRequest.getProperty("status");
		String brandCode = httpRequest.getProperty("brandCode");
		Double exchangeRate = NumberUtils.getDouble(httpRequest.getProperty("exchangeRate"));
		String orderTypeCode = httpRequest.getProperty("orderTypeCode");
		String errorMsg = null;
		log.info("ImAdjustmentHeadService.updateAJAXPageLinesData gridLineFirstIndex=" + gridLineFirstIndex + ",gridRowCount="
				+ gridRowCount + ",headId=" + headId + ",status=" + status + ",gridData=" + gridData);

			// 取得LINE MAX INDEX
			// PoPurchaseOrderHead head = (PoPurchaseOrderHead)
			// poPurchaseOrderHeadDAO.findByPrimaryKey(PoPurchaseOrderHead.class,
			// headId);
			// CREATE TMP HEAD
			ImAdjustmentHead head = new ImAdjustmentHead();
			head.setHeadId(headId);
			head.setBrandCode(brandCode);
			head.setOrderTypeCode(orderTypeCode);
			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
					ImAdjustmentHeadService.GRID_FIELD_NAMES);
			// Get INDEX NO
			int indexNo = imAdjustmentLineService.findPageLineMaxIndex(headId).intValue();
			if (null != upRecords && null != head) {
				for (Properties upRecord : upRecords) {
					// 先載入HEAD_ID OR LINE DATA
					Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
					log.info("head Id=" + head.getHeadId() + " ,line Id=" + lineId);
					String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);
					// 如果沒有ITEM CODE 就不會新增或修改
					if (StringUtils.hasText(itemCode)) {
						ImAdjustmentLine imAdjustmentLine = imAdjustmentLineService.findLine(head.getHeadId(), lineId);
						if (null != imAdjustmentLine) {
							// UPDATE Properties
							// setLine(poPurchaseOrderLine, upRecord);
							AjaxUtils.setPojoProperties(imAdjustmentLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
							// LINE 計算
							imAdjustmentLineDAO.update(imAdjustmentLine);
						} else {
							// INSERT Properties
							indexNo++;
							imAdjustmentLine = new ImAdjustmentLine();
							// setLine(poPurchaseOrderLine, upRecord);
							AjaxUtils.setPojoProperties(imAdjustmentLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
							imAdjustmentLine.setIndexNo(Long.valueOf(indexNo));
							imAdjustmentLine.setImAdjustmentHead(head);
							// LINE 計算
							imAdjustmentLineDAO.save(imAdjustmentLine);
						}
					} else {
						// line 沒有 ITEM_CODE
					}
				}
			} else {
				errorMsg = "沒有拆/拼/變更貨號單單頭資料";
			}

		return AjaxUtils.getResponseMsg(errorMsg);
	}
	public Map executeInitial(Map parameterMap) throws Exception{

        HashMap resultMap = new HashMap();
        Map multiMap = new HashMap();
        try{
		    Object otherBean = parameterMap.get("vatBeanOther");
		    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
		    String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
			Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;			
		
			List allArrivalWarehouses = imMovementService.getWarehouseInfo(loginBrandCode, null);
			multiMap.put("allArrivalWarehouses",AjaxUtils.produceSelectorData(allArrivalWarehouses  ,"warehouseCode" ,"warehouseName",  true,  false));
			resultMap.put("multiList",multiMap);
			return resultMap;
        }catch (Exception ex) {
	        log.error("拆/拼/變更貨號單初始化失敗，原因：" + ex.toString());
	        throw new Exception("拆/拼/變更貨號單初始化失敗，原因：" + ex.toString());
	    }
    }
	/**
	 * save and update
	 *
	 * @param modifyObj
	 * @return
	 * @throws Exception
	 */
	public String create(ImAdjustmentHead modifyObj) throws FormException, Exception {
		log.info("ImAdjustmentHeadService.create ");
		if (null != modifyObj) {
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
	 * save tmp
	 *
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String saveTmp(ImAdjustmentHead saveObj) throws FormException, Exception {
		log.info("ImAdjustmentHeadService.saveTmp");
		String tmpOrderNo = AjaxUtils.getTmpOrderNo();
		saveObj.setOrderNo(tmpOrderNo);
		//saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imAdjustmentHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * save
	 *
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String save(ImAdjustmentHead saveObj) throws FormException, Exception {
		log.info("ImAdjustmentHeadService.save ");
		System.out.println("line size is " + saveObj.getImAdjustmentLines().size());
		if(!"SAVE".equals(saveObj.getStatus())){
			doAllValidate(saveObj);
			doOnHand(saveObj, false);
			// 將差異數量寫到 進貨單
			// modifyReceiveDiffQty(saveObj);
			// 20090119 shan 將調整數量寫回PO才可以在核銷
			modifyPOReceiptedQuantity(saveObj);
		}
		saveObj.setOrderNo(buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), saveObj.getOrderTypeCode()));
		saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		// 指定批號
		setLotNo(saveObj);
		// 扣掉ON)HAND

		// save
		imAdjustmentHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;

	}

	/**
	 * update
	 *
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String update(ImAdjustmentHead updateObj) throws FormException, Exception {
		log.info("ImAdjustmentHeadService.update ");
		System.out.println("line size is " + updateObj.getImAdjustmentLines().size());
		System.out.println("headObj.getOnHandStatus() updateObj=" + updateObj.getOnHandStatus());
		if(!"SAVE".equals(updateObj.getStatus())){
			doAllValidate(updateObj);
			doOnHand(updateObj, false);
			// 將差異數量寫到 進貨單
			// modifyReceiveDiffQty(updateObj);
			// 20090119 shan 將調整數量寫回PO才可以再核銷
			modifyPOReceiptedQuantity(updateObj);
		}
		updateObj.setLastUpdateDate(new Date());
		// 藉由進貨單去作,目前做法是將數量寫到進貨單 差異量
		setLotNo(updateObj);
		imAdjustmentHeadDAO.update(updateObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * search
	 *
	 * @param findObjs
	 * @return
	 */
	public List<ImAdjustmentHead> find(HashMap findObjs) {
		return imAdjustmentHeadDAO.find(findObjs);
	}

	/**
	 * 重新載入View 資料 for picker
	 *
	 * @param headObj
	 * @throws FormException
	 */
	public void reloadViewData(ImAdjustmentHead headObj) throws FormException {
		log.info("ImAdjustmentHeadService.reloadViewData");
		setImAdjustmentItem(headObj);
		setImWarehouseItem(headObj);
		countItemTotalAmount(headObj);
	}

	/**
	 * 計算 ITEM 數量
	 *
	 * @param headObj
	 */
	public void countItemTotalAmount(ImAdjustmentHead headObj) {
		log.info("ImAdjustmentHeadService.countItemTotalAmount");
		if (null != headObj) {
			List<ImAdjustmentLine> imAdjustmentLines = headObj.getImAdjustmentLines();
			if (null != imAdjustmentLines) {
				for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
					// 全部都KEY 差異數量就好ㄌ
					/*
					 * if (imAdjustmentLine.getDifQuantity() == 0){
					 * imAdjustmentLine.setDifQuantity(imAdjustmentLine.getActualQuantity() -
					 * imAdjustmentLine.getQuantity()); }
					 */

					// 計算單項合計
					log.info("ImAdjustmentHeadService.countItemTotalAmount type=" + headObj.getOrderTypeCode() + ","
							+ imAdjustmentLine.getDifQuantity() + "*" + imAdjustmentLine.getLocalUnitCost());
					if ("IDA".equalsIgnoreCase(headObj.getOrderTypeCode()) || "ICA".equalsIgnoreCase(headObj.getOrderTypeCode()) || "ACA".equalsIgnoreCase(headObj.getOrderTypeCode()) // T2新增"ACA 拆併貨調整單" by Weichun 2011.11.22
							|| "IAJ".equalsIgnoreCase(headObj.getOrderTypeCode())) {
						imAdjustmentLine.setAmount(imAdjustmentLine.getDifQuantity() * imAdjustmentLine.getLocalUnitCost());

						imAdjustmentLine.setLocalUnitCost(NumberUtils.round(imAdjustmentLine.getLocalUnitCost(), 2));
						imAdjustmentLine.setAmount(NumberUtils.round(imAdjustmentLine.getAmount(), 0));
					}
				}
			}
		}
	}

	/**
	 * 指定ITEM VIEW 資料
	 *
	 * @param headObj
	 * @throws FormException
	 */
	public void setImAdjustmentItem(ImAdjustmentHead headObj) throws FormException {
		log.info("ImAdjustmentHeadService.setImReceiveItem");
		if (null != headObj) {
			List<ImAdjustmentLine> imAdjustmentLines = headObj.getImAdjustmentLines();
			if (null != imAdjustmentLines) {
				for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
					String itemCode = imAdjustmentLine.getItemCode();
					if (StringUtils.hasText(itemCode)) {

						ImItem imItem = imItemDAO.findById(itemCode);
						if (null != imItem) {
							imAdjustmentLine.setItemCName(imItem.getItemCName());
						} else {
							imAdjustmentLine.setItemCName("查無品號");
						}

						// 指定預設
						log.info("ImAdjustmentHeadService.setImReceiveItem headObj.getOrderTypeCode()=" + headObj.getOrderTypeCode());
						if ("IDA".equalsIgnoreCase(headObj.getOrderTypeCode()) || "ICA".equalsIgnoreCase(headObj.getOrderTypeCode())
								|| "ACA".equalsIgnoreCase(headObj.getOrderTypeCode()) // T2新增"ACA 拆併貨調整單" by Weichun 2011.11.22
								|| "IAJ".equalsIgnoreCase(headObj.getOrderTypeCode())) {

							if ("IAJ".equalsIgnoreCase(headObj.getOrderTypeCode())) {
								// 取得零售價
								if (null != imAdjustmentLine.getLocalUnitCost() && imAdjustmentLine.getLocalUnitCost() < 0) {
									imAdjustmentLine.setLocalUnitCost(poPurchaseOrderLineService.getOriginalUnitPriceDouble(headObj
											.getBrandCode(), headObj.getOrderTypeCode(), itemCode));
								}
							} else {

								if (null != imAdjustmentLine.getLocalUnitCost() && imAdjustmentLine.getLocalUnitCost() < 0) {
									// 如果是 < 0 取得月平均
									// imAdjustmentLine.setLocalUnitCost(getAverageUnitCost(itemCode,
									// headObj.getBrandCode()));
									// 抓取最早的定價
									imAdjustmentLine.setLocalUnitCost(getOldestUnitPrice(itemCode));
								}
							}

							log.info("ImAdjustmentHeadService.setImReceiveItem imAdjustmentLine.getLotNo()=" + imAdjustmentLine.getLotNo());
							if (!StringUtils.hasText(imAdjustmentLine.getLotNo()))
								imAdjustmentLine.setLotNo(SystemConfig.LOT_NO);
						} else {
							if (null != imAdjustmentLine.getLocalUnitCost() && imAdjustmentLine.getLocalUnitCost() < 0) {
								imAdjustmentLine.setLocalUnitCost(0D);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 指定Warehouse VIEW 資料
	 *
	 * @param headObj
	 */
	public void setImWarehouseItem(ImAdjustmentHead headObj) {
		log.info("ImAdjustmentHeadService.setImWarehouseItem");
		if (null != headObj) {
			List<ImAdjustmentLine> imAdjustmentLines = headObj.getImAdjustmentLines();
			if (null != imAdjustmentLines) {
				for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
					if (StringUtils.hasText(imAdjustmentLine.getWarehouseCode())) {
						HashMap findObjs = new HashMap();
						findObjs.put("warehouseCode", imAdjustmentLine.getWarehouseCode());
						findObjs.put("brandCode", headObj.getBrandCode());
						List<ImWarehouse> imWarehouses = imWarehouseDAO.find(findObjs);
						if (null != imWarehouses && imWarehouses.size() > 0) {
							ImWarehouse imWarehouse = imWarehouses.get(0);
							imAdjustmentLine.setWarehouseName(imWarehouse.getWarehouseName());
						} else {
							imAdjustmentLine.setWarehouseName("查無庫號");
						}
					}
				}
			}
		}
	}

	/**
	 * 匯入 IM_Receive 的資料 應該沒有用到ㄌ
	 *
	 * @param headObj
	 * @throws FormException
	 */
	public void importIMReceive(ImAdjustmentHead headObj) throws FormException {
		log.info("ImAdjustmentHeadService.importIMReceive");
		String orderNo = headObj.getSourceOrderNo();
		String orderTypd = headObj.getSourceOrderTypeCode();
		String brandCode = headObj.getBrandCode();
		if (StringUtils.hasText(orderNo) && StringUtils.hasText(orderTypd) && StringUtils.hasText(brandCode)) {
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("orderTypeCode", orderTypd);
			findObjs.put("startOrderNo", orderNo);
			findObjs.put("endOrderNo", orderNo);
			List<ImReceiveHead> imReceiveHeads = imReceiveHeadDAO.find(findObjs);
			if (null != imReceiveHeads && imReceiveHeads.size() > 0) {
				for (ImReceiveHead imReceiveHead : imReceiveHeads) {
					String warehouseCode = imReceiveHead.getDefaultWarehouseCode();
					if (OrderStatus.CLOSE.equalsIgnoreCase(imReceiveHead.getStatus())) {
						throw new FormException("進貨單已經關閉" + orderNo);
					}
					List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
					List<ImAdjustmentLine> imAdjustmentLines = headObj.getImAdjustmentLines();
					imAdjustmentLines.clear();
					for (ImReceiveItem imReceiveItem : imReceiveItems) {
						ImAdjustmentLine imAdjustmentLine = new ImAdjustmentLine();
						imAdjustmentLine.setItemCode(imReceiveItem.getItemCode());
						imAdjustmentLine.setItemCName(imReceiveItem.getItemCName());
						imAdjustmentLine.setQuantity(imReceiveItem.getQuantity());
						imAdjustmentLine.setActualQuantity(imReceiveItem.getQuantity());
						imAdjustmentLine.setWarehouseCode(warehouseCode);
						imAdjustmentLines.add(imAdjustmentLine);
					}
				}
				reloadViewData(headObj);
			} else {
				throw new FormException("查無進貨單單號" + orderNo);
			}
		}
	}

	/**
	 * close order
	 * 要將資料寫到ON_HAND 要自己寫LOG
	 *
	 * @param imReceiveHead
	 */
	public void closeOrder(ImAdjustmentHead imAdjustmentHead) {
		log.info("ImAdjustmentHeadService.closeOrder");
		imAdjustmentHead.setStatus(OrderStatus.CLOSE);
	}

	/**
	 * 將差異數量寫回 進貨單 再由進貨單去作核銷 跟 ON_HAND的異動 沒有用到
	 *
	 * @param headObj
	 * @throws FormException
	 */
	private void modifyReceiveDiffQty(ImAdjustmentHead headObj) throws FormException {
		log.info("ImAdjustmentHeadService.modifyReceiveDiffQty");
		String orderNo = headObj.getSourceOrderNo();
		String orderTypd = headObj.getSourceOrderTypeCode();
		String brandCode = headObj.getBrandCode();
		if (StringUtils.hasText(orderNo) && StringUtils.hasText(orderTypd) && StringUtils.hasText(brandCode)) {
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("orderTypeCode", orderTypd);
			findObjs.put("startOrderNo", orderNo);
			findObjs.put("endOrderNo", orderNo);
			List<ImReceiveHead> imReceiveHeads = imReceiveHeadDAO.find(findObjs);
			if (null != imReceiveHeads && imReceiveHeads.size() > 0) {
				ImReceiveHead imReceiveHead = imReceiveHeads.get(0);
				if (null != imReceiveHead) {
					if (OrderStatus.CLOSE.equalsIgnoreCase(imReceiveHead.getStatus())) {
						throw new FormException("進貨單已經關閉 " + orderNo);
					}
					// 20090119 shan
					// else
					// if(OrderStatus.FINISH.equalsIgnoreCase(imReceiveHead.getWarehouseStatus())){
					// throw new FormException("請勿重覆核銷進貨單 " + orderNo);
					// }
					List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
					List<ImAdjustmentLine> imAdjustmentLines = headObj.getImAdjustmentLines();
					int lineSize = imAdjustmentLines.size();
					for (int index = 0; index < lineSize; index++) {
						ImAdjustmentLine imAdjustmentLine = imAdjustmentLines.get(index);
						Double diffQty = imAdjustmentLine.getDifQuantity();
						for (ImReceiveItem imReceiveItem : imReceiveItems) {
							if (imAdjustmentLine.getItemCode().equals(imReceiveItem.getItemCode())) {
								imReceiveItem.setDiffQty(imReceiveItem.getDiffQty() + diffQty);
								break;
							}
						}
					}
				}
				imReceiveHeadDAO.update(imReceiveHead);
			} else {
				throw new FormException("查無進貨單單號" + orderNo);
			}
		}
	}


	/**
	 * 指定批號
	 *
	 * @param headObj
	 */
	private void setLotNo(ImAdjustmentHead headObj) {
		log.info("ImAdjustmentHeadService.setLotNo " + headObj.getOrderNo());
		String sourceOrderTypeCode = headObj.getSourceOrderTypeCode();
		String lotNo = "000000000000";
		if ("IRF".equalsIgnoreCase(sourceOrderTypeCode) || "IRL".equalsIgnoreCase(sourceOrderTypeCode)) {
			String orderNo = headObj.getSourceOrderNo();
			String orderTypd = headObj.getSourceOrderTypeCode();
			String brandCode = headObj.getBrandCode();
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("orderTypeCode", orderTypd);
			findObjs.put("startOrderNo", orderNo);
			findObjs.put("endOrderNo", orderNo);
			List<ImReceiveHead> imReceiveHeads = imReceiveHeadDAO.find(findObjs);
			if (null != imReceiveHeads && imReceiveHeads.size() > 0) {
				
				ImReceiveHead imReceiveHead = imReceiveHeads.get(0);
				if (null != imReceiveHead) {
					log.info("iLLLLLLLLOT " + imReceiveHead.getLotNo());
					lotNo = imReceiveHead.getLotNo();
				}
			}
		}
		List<ImAdjustmentLine> imAdjustmentLines = headObj.getImAdjustmentLines();
		for (int index = 0; index < imAdjustmentLines.size(); index++) {
			ImAdjustmentLine imAdjustmentLine = imAdjustmentLines.get(index);
			if (!StringUtils.hasText(imAdjustmentLine.getLotNo()))
				log.info("SETLOT " + imAdjustmentLine.getLotNo());
				imAdjustmentLine.setLotNo(lotNo);
		}
	}

	/**
	 * 將差異數量寫回 採購單欄位.ReceiptedQuantity 進貨單
	 *
	 * @param headObj
	 * @throws Exception
	 */
	private void modifyPOReceiptedQuantity2(ImAdjustmentHead headObj) throws Exception {
		log.info("ImAdjustmentHeadService.modifyPOReceiptedQuantity");
		String orderNo = headObj.getSourceOrderNo();
		String orderTypd = headObj.getSourceOrderTypeCode();
		String brandCode = headObj.getBrandCode();
		String sourceOrderTypeCode = headObj.getSourceOrderTypeCode();
		if (OrderStatus.FINISH.equalsIgnoreCase(headObj.getStatus())) {
			if ("IRF".equalsIgnoreCase(sourceOrderTypeCode) || "IRL".equalsIgnoreCase(sourceOrderTypeCode)) {

				// 進貨單 取得 採購單 IRL , IRF
				if (StringUtils.hasText(orderNo) && StringUtils.hasText(orderTypd) && StringUtils.hasText(brandCode)) {
					HashMap findObjs = new HashMap();
					findObjs.put("brandCode", brandCode);
					findObjs.put("orderTypeCode", orderTypd);
					findObjs.put("startOrderNo", orderNo);
					findObjs.put("endOrderNo", orderNo);

					// 進貨單
					List<ImReceiveHead> imReceiveHeads = imReceiveHeadDAO.find(findObjs);
					// 調整單
					List<ImAdjustmentLine> imAdjustmentLines = headObj.getImAdjustmentLines();
					if (null != imReceiveHeads && imReceiveHeads.size() > 0) {
						ImReceiveHead imReceiveHead = imReceiveHeads.get(0);
						if (null != imReceiveHead) {
							List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
							if ("IRL".equalsIgnoreCase(sourceOrderTypeCode)) {
								List<PoPurchaseOrderHead> poL = poPurchaseOrderHeadDAO.find(findObjs);
								PoPurchaseOrderHead poPurchaseOrderHead = poL.get(0);
								
								
								for (ImReceiveItem imReceiveItem : imReceiveItems) {
									String poNo = imReceiveItem.getPoOrderNo();
									findObjs.put("startOrderNo", poNo);
									findObjs.put("endOrderNo", poNo);
									findObjs.put("orderTypeCode", "POL");
									
									if (null == poPurchaseOrderHead) {
										throw new FormException("查無單別:" + headObj.getSourceOrderTypeCode() + ",單號:"
												+ headObj.getSourceOrderNo());
									}									
								}
								log.info("更新PO已到數量");
								updatePOReceiptedQuantity(poPurchaseOrderHead, imAdjustmentLines);
							} else {
								for (ImReceiveItem imReceiveItem : imReceiveItems) {
									String invoiceNo = imReceiveItem.getInvoiceNo();																		
									if (StringUtils.hasText(invoiceNo)) {
										HashMap findInvoiceObjs = new HashMap();
										findInvoiceObjs.put("brandCode", brandCode);
										findInvoiceObjs.put("invoiceNo", invoiceNo);
										List<FiInvoiceHead> fiInvoiceHeads = fiInvoiceHeadService.find(findInvoiceObjs);
										if (null != fiInvoiceHeads && fiInvoiceHeads.size() > 0) {
											FiInvoiceHead fiInvoiceHead = fiInvoiceHeads.get(0);
											List<FiInvoiceLine> fiInvoiceLines = fiInvoiceHead.getFiInvoiceLines();																				
											for (FiInvoiceLine fiInvoiceLine : fiInvoiceLines) {
												Long poHId = fiInvoiceLine.getPoPurchaseOrderHeadId();
												PoPurchaseOrderHead poPurchaseOrderHead = (PoPurchaseOrderHead)poPurchaseOrderHeadDAO.findByPrimaryKey(PoPurchaseOrderHead.class,poHId);
												if (null == poPurchaseOrderHead) {
													throw new FormException("查無單別:" + headObj.getSourceOrderTypeCode() + ",單號:"
															+ headObj.getSourceOrderNo());
												}
												List<PoPurchaseOrderLine> poPurchaseOrderLines = poPurchaseOrderHead.getPoPurchaseOrderLines();
												for (PoPurchaseOrderLine poPurchaseOrderLine : poPurchaseOrderLines) {
													log.info("找sheet"+imReceiveItem.getLineId()+"Po"+poPurchaseOrderLine.getLineId());
													PoVerificationSheet verificationSheet = new PoVerificationSheet();
													List<PoVerificationSheet> verificationSheets = poVerificationSheetDAO.findByProperty("PoVerificationSheet", 
															new String[]{"imReceiveLineId", "poOrderLineId"}, new Object[]{imReceiveItem.getLineId(),poPurchaseOrderLine.getLineId() });
													verificationSheet = verificationSheets.get(0);
													log.info("verificationSheet=="+verificationSheet);
													verificationSheet.setAdjustmentOrderNo(headObj.getOrderNo());
													verificationSheet.setAdjustmentOrderType(headObj.getOrderTypeCode());
												}
												log.info("更新PO已到數量2");
												updatePOReceiptedQuantity(poPurchaseOrderHead, imAdjustmentLines);												
											}
										} else {
											throw new FormException("查無單別:" + headObj.getSourceOrderTypeCode() + ",單號:"
													+ headObj.getSourceOrderNo() + " 發票");
										}
									} else {
										throw new FormException("查無單別:" + headObj.getSourceOrderTypeCode() + ",單號:"
												+ headObj.getSourceOrderNo() + " 發票");
									}
								}
							}
						}
					} else {
						throw new FormException("查無進貨單單號" + orderNo);
					}					
					
				}
			}
		}
	}
	
	public void modifyPOReceiptedQuantity(ImAdjustmentHead headObj) throws Exception {
	    try{

		log.info("ImAdjustmentHeadService.modifyPOReceiptedQuantity2");
		String sourceorderNo = headObj.getSourceOrderNo();		
		String brandCode = headObj.getBrandCode();
		String sourceOrderTypeCode = headObj.getSourceOrderTypeCode();
		if (OrderStatus.FINISH.equalsIgnoreCase(headObj.getStatus())&& "IRF".equalsIgnoreCase(sourceOrderTypeCode)) {
			List<ImAdjustmentLine> imAdjustmentLines = headObj.getImAdjustmentLines();
			if(null !=imAdjustmentLines){
				for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
					log.info("找調整單imAdjustmentLine.getItemCode()"+imAdjustmentLine.getItemCode()+" "+imAdjustmentLine.getLineId());
					log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+brandCode +"  "+ sourceOrderTypeCode+"  "+sourceorderNo);
					List<ImReceiveHead> imReceiveHeads = imReceiveHeadDAO.findImReceiveHead(brandCode,sourceOrderTypeCode,sourceorderNo);
					ImReceiveHead imReceiveHead = imReceiveHeads.get(0);
					if(null != imReceiveHead ){
						log.info("找進貨單"+imReceiveHead.getOrderNo());										
						if(imReceiveHead.getOrderTypeCode().equals(sourceOrderTypeCode) && imReceiveHead.getOrderNo().equals(sourceorderNo)){
							List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
							log.info("找進貨單明細");							
							for (ImReceiveItem imReceiveItem2 : imReceiveItems) {
								if(imReceiveItem2.getItemCode().equalsIgnoreCase(imAdjustmentLine.getItemCode())){
									log.info("imReceiveItem2.getItemCode()"+imReceiveItem2.getItemCode()+"Invoice"+imReceiveItem2.getInvoiceNo());
									FiInvoiceHead fiInvoiceHead = (FiInvoiceHead)fiInvoiceHeadDAO.findPoInvoice(brandCode,imReceiveItem2.getInvoiceNo());
									log.info("找invoice單"+fiInvoiceHead.getInvoiceNo());
									List<FiInvoiceLine> fiInvoiceLines = fiInvoiceHead.getFiInvoiceLines();
									log.info("InVoiceSize"+fiInvoiceLines.size());
									if(null != fiInvoiceLines){											
										for (FiInvoiceLine fiInvoiceLine : fiInvoiceLines) {
											log.info("用INVOICE找採購TypeCode"+fiInvoiceLine.getSourceOrderTypeCode()+"OrderNo"+fiInvoiceLine.getSourceOrderNo()+"ImReceiveitemPoNo"+imReceiveItem2.getPoNo());
											if(fiInvoiceLine.getSourceOrderNo().equals(imReceiveItem2.getPoNo())){											
											PoPurchaseOrderHead poPurchaseOrderHead = (PoPurchaseOrderHead)poPurchaseOrderHeadDAO.findPoInvoice(brandCode,fiInvoiceLine.getSourceOrderTypeCode(),fiInvoiceLine.getSourceOrderNo());
											log.info("找採購單"+poPurchaseOrderHead.getOrderNo());
											List <PoPurchaseOrderLine> poPurchaseOrderLine = poPurchaseOrderHead.getPoPurchaseOrderLines();
											log.info("找採購單明細");
											for (PoPurchaseOrderLine poPurchaseOrderLine2 : poPurchaseOrderLine) {
												log.info("調整與採購比對");
												PoVerificationSheet verificationSheet = (PoVerificationSheet)poVerificationSheetDAO
												.findVerificationSheet(imAdjustmentLine.getItemCode(),imReceiveHead.getOrderTypeCode(),imReceiveHead.getOrderNo(),poPurchaseOrderHead.getOrderTypeCode(),poPurchaseOrderHead.getOrderNo());
												log.info("FindVerificationSheet"+verificationSheet);												
												if(imAdjustmentLine.getItemCode().equals(poPurchaseOrderLine2.getItemCode())){
													Double recQty = poPurchaseOrderLine2.getReceiptedQuantity();
													Double difQty = imAdjustmentLine.getDifQuantity();
													log.info("recQty="+recQty+"difQty="+difQty);
													log.info("poPurchaseOrderLine.getItemCode()="+poPurchaseOrderLine2.getItemCode()+"poPurchaseOrderLine.LINEID"+poPurchaseOrderLine2.getLineId());
													log.info("imAdjustmentLine.getItemCode()="+imAdjustmentLine.getItemCode()+"imAdjustmentLine.LINEID"+imAdjustmentLine.getLineId());												
													log.info(imAdjustmentLine.getDifQuantity().toString());
													//-----------------------------------------------------------------------------
													// 採購單 核銷數量 + (負數 差異數量)
													poPurchaseOrderLine2.setReceiptedQuantity(recQty + difQty);	
													insertAdjSheep( verificationSheet,headObj,imAdjustmentLine,poPurchaseOrderHead,poPurchaseOrderLine2);																						
													break;
												}/*else{
													throw new Exception("找不到核銷的調整品項 " + imAdjustmentLine.getItemCode());
													}*/																							
												}
											}
										}
									}
								}
							}
						}		
					}
				}
			}
		}
		if (OrderStatus.FINISH.equalsIgnoreCase(headObj.getStatus())) {
			if ("IRL".equalsIgnoreCase(sourceOrderTypeCode)) {
				String orderNo = headObj.getSourceOrderNo();
				String orderTypd = headObj.getSourceOrderTypeCode();
				if (OrderStatus.FINISH.equalsIgnoreCase(headObj.getStatus())) {
				// 進貨單 取得 採購單 IRL , IRF
				if (StringUtils.hasText(orderNo) && StringUtils.hasText(orderTypd) && StringUtils.hasText(brandCode)) {
					HashMap findObjs = new HashMap();
					findObjs.put("brandCode", brandCode);
					findObjs.put("orderTypeCode", orderTypd);
					findObjs.put("startOrderNo", orderNo);
					findObjs.put("endOrderNo", orderNo);

					// 進貨單
					List<ImReceiveHead> imReceiveHeads = imReceiveHeadDAO.find(findObjs);
					// 調整單
					List<ImAdjustmentLine> imAdjustmentLines = headObj.getImAdjustmentLines();
					if (null != imReceiveHeads && imReceiveHeads.size() > 0) {
						ImReceiveHead imReceiveHead = imReceiveHeads.get(0);
						if (null != imReceiveHead) {
							List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
							for (ImReceiveItem imReceiveItem : imReceiveItems) {
								String poNo = imReceiveItem.getPoOrderNo();
								findObjs.put("startOrderNo", poNo);
								findObjs.put("endOrderNo", poNo);
								findObjs.put("orderTypeCode", "POL");
								
																	
							}
							if ("IRL".equalsIgnoreCase(sourceOrderTypeCode)) {
								List<PoPurchaseOrderHead> poL = poPurchaseOrderHeadDAO.find(findObjs);
								PoPurchaseOrderHead poPurchaseOrderHead = poL.get(0);
								log.info("// 調整單"+poPurchaseOrderHead.getHeadId());
								if (null == poPurchaseOrderHead) {
									throw new FormException("查無單別:" + headObj.getSourceOrderTypeCode() + ",單號:"
											+ headObj.getSourceOrderNo());
								}
								
								log.info("更新PO已到數量");
								updatePOReceiptedQuantity(poPurchaseOrderHead, imAdjustmentLines);
							} else {
								for (ImReceiveItem imReceiveItem : imReceiveItems) {
									String invoiceNo = imReceiveItem.getInvoiceNo();																		
									if (StringUtils.hasText(invoiceNo)) {
										HashMap findInvoiceObjs = new HashMap();
										findInvoiceObjs.put("brandCode", brandCode);
										findInvoiceObjs.put("invoiceNo", invoiceNo);
										List<FiInvoiceHead> fiInvoiceHeads = fiInvoiceHeadService.find(findInvoiceObjs);
										if (null != fiInvoiceHeads && fiInvoiceHeads.size() > 0) {
											FiInvoiceHead fiInvoiceHead = fiInvoiceHeads.get(0);
											List<FiInvoiceLine> fiInvoiceLines = fiInvoiceHead.getFiInvoiceLines();																				
											for (FiInvoiceLine fiInvoiceLine : fiInvoiceLines) {
												Long poHId = fiInvoiceLine.getPoPurchaseOrderHeadId();
												PoPurchaseOrderHead poPurchaseOrderHead = (PoPurchaseOrderHead)poPurchaseOrderHeadDAO.findByPrimaryKey(PoPurchaseOrderHead.class,poHId);
												if (null == poPurchaseOrderHead) {
													throw new FormException("查無單別:" + headObj.getSourceOrderTypeCode() + ",單號:"
															+ headObj.getSourceOrderNo());
												}
												List<PoPurchaseOrderLine> poPurchaseOrderLines = poPurchaseOrderHead.getPoPurchaseOrderLines();
												for (PoPurchaseOrderLine poPurchaseOrderLine : poPurchaseOrderLines) {
													log.info("找sheet"+imReceiveItem.getLineId()+"Po"+poPurchaseOrderLine.getLineId());
													PoVerificationSheet verificationSheet = new PoVerificationSheet();
													List<PoVerificationSheet> verificationSheets = poVerificationSheetDAO.findByProperty("PoVerificationSheet", 
															new String[]{"imReceiveLineId", "poOrderLineId"}, new Object[]{imReceiveItem.getLineId(),poPurchaseOrderLine.getLineId() });
													verificationSheet = verificationSheets.get(0);
													log.info("verificationSheet=="+verificationSheet);
													verificationSheet.setAdjustmentOrderNo(headObj.getOrderNo());
													verificationSheet.setAdjustmentOrderType(headObj.getOrderTypeCode());
												}
												log.info("更新PO已到數量2");
												updatePOReceiptedQuantity(poPurchaseOrderHead, imAdjustmentLines);												
											}
										} else {
											throw new FormException("查無單別:" + headObj.getSourceOrderTypeCode() + ",單號:"
													+ headObj.getSourceOrderNo() + " 發票");
										}
									} else {
										throw new FormException("查無單別:" + headObj.getSourceOrderTypeCode() + ",單號:"
												+ headObj.getSourceOrderNo() + " 發票");
									}
								}
							}
						}
					} else {
						throw new FormException("查無進貨單單號" + orderNo);
					}					
					
				}
			}
		}
	}

	    }catch(FormException e){
		e.printStackTrace();
		throw e;
	    }catch(Exception ex){
		ex.printStackTrace();
	    }
	}	
	
	public void insertAdjSheep(PoVerificationSheet verificationSheet, ImAdjustmentHead headObj, ImAdjustmentLine imAdjustmentLine,
			PoPurchaseOrderHead poPurchaseOrderHead, PoPurchaseOrderLine poPurchaseOrderLine2)throws Exception {
		// TODO Auto-generated method stub
		log.info("copyOrgSheep2Adj");
		PoVerificationSheet poverificationSheet = new PoVerificationSheet();
		poverificationSheet.setImReceiveOrderType(verificationSheet.getImReceiveOrderType());
		poverificationSheet.setImReceiveOrderNo(verificationSheet.getImReceiveOrderNo());
		poverificationSheet.setImReceiveLineId(verificationSheet.getImReceiveLineId());
		poverificationSheet.setItemCode(verificationSheet.getItemCode());
		poverificationSheet.setInvoiceNo(verificationSheet.getInvoiceNo());
		poverificationSheet.setInoviceLotNo(verificationSheet.getInoviceLotNo());
		poverificationSheet.setPoOrderType(verificationSheet.getPoOrderType());
		poverificationSheet.setPoOrderNo(verificationSheet.getPoOrderNo());
		poverificationSheet.setPoOrderLineId(verificationSheet.getPoOrderLineId());
		poverificationSheet.setQuantity(imAdjustmentLine.getDifQuantity());
		poverificationSheet.setStatus(verificationSheet.getStatus());
		poverificationSheet.setBrandCode(verificationSheet.getBrandCode());
		poverificationSheet.setAdjustmentOrderType(headObj.getOrderTypeCode());
		poverificationSheet.setAdjustmentOrderNo(headObj.getOrderNo());
		//poverificationSheet.setAdjQuantity(imAdjustmentLine.getDifQuantity());
		poverificationSheet.setPoQuantity(poPurchaseOrderLine2.getQuantity());
		poverificationSheet.setPoUnitCost(poPurchaseOrderLine2.getLocalUnitCost());
		poverificationSheet.setBudgetYear(poPurchaseOrderHead.getBudgetYear());
		poverificationSheet.setCreatedBy(headObj.getCreatedBy());
		poverificationSheet.setCreationDate(new Date());
		log.info("updateSheet");
		poVerificationSheetDAO.save(poverificationSheet);
		poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);
	}

	/**
	 * 調整採購單 進貨差異數量 沒有作金額的計算
	 * @param poPurchaseOrderHead
	 * @param imAdjustmentLines
	 * @throws FormException
	 * @throws Exception
	 */
	private void updatePOReceiptedQuantity(PoPurchaseOrderHead poPurchaseOrderHead, List<ImAdjustmentLine> imAdjustmentLines)
			throws FormException, Exception {
		log.info("ImAdjustmentHeadService.updatePOReceiptedQuantity"+"此段會重複扣!!");
		List<PoPurchaseOrderLine> poPurchaseOrderLines = poPurchaseOrderHead.getPoPurchaseOrderLines();
		log.info("imAdjustmentLinesSize="+imAdjustmentLines.size());
		for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
			log.info("poPurchaseOrderLinesSize="+poPurchaseOrderLines.size());			
			for (PoPurchaseOrderLine poPurchaseOrderLine : poPurchaseOrderLines) {
				if (poPurchaseOrderLine.getItemCode().equalsIgnoreCase(imAdjustmentLine.getItemCode())) {
					Double recQty = poPurchaseOrderLine.getReceiptedQuantity();
					Double difQty = imAdjustmentLine.getDifQuantity();
					log.info("recQty="+recQty+"difQty="+difQty);
					log.info("poPurchaseOrderLine.getItemCode()="+poPurchaseOrderLine.getItemCode()+"poPurchaseOrderLine.LINEID"+poPurchaseOrderLine.getLineId());
					log.info("imAdjustmentLine.getItemCode()="+imAdjustmentLine.getItemCode()+"imAdjustmentLine.LINEID"+imAdjustmentLine.getLineId());					
					// 採購單 核銷數量 + (負數 差異數量)
					poPurchaseOrderLine.setReceiptedQuantity(recQty + difQty);
					break;
				}
			}
		}
		poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);
	}

	/**
	 * 將調整單的資料寫到ON_HAND
	 *
	 * @param headObj
	 * @throws FormException
	 */
	private void doOnHand(ImAdjustmentHead headObj, boolean reject) throws FormException {
		log.info("ImAdjustmentHeadService.doLocalOnHand");
		if (OrderStatus.FINISH.equalsIgnoreCase(headObj.getStatus())) {
			String organizationCode = UserUtils.getOrganizationCodeByBrandCode(headObj.getBrandCode());
			if (organizationCode == null) {
				throw new FormException("依據品牌代碼：" + headObj.getBrandCode() + "查無其組織代號！");
			}

			if (reject) {
				// 回復
				headObj.setOnHandStatus("N");
			} else {
				headObj.setOnHandStatus("Y");
			}

			List<ImAdjustmentLine> items = headObj.getImAdjustmentLines();
			for (ImAdjustmentLine item : items) {
				Double otherUncommitQty = item.getDifQuantity();
				if (reject) {
					otherUncommitQty = 0 - otherUncommitQty;
				}
				ImItem imItem = imItemDAO.findById(item.getItemCode());
				long salesRatio = 1;
				if ((null != imItem.getSalesRatio()) && (imItem.getSalesRatio() > 0))
					salesRatio = imItem.getSalesRatio();
				otherUncommitQty = salesRatio * otherUncommitQty;
				modifyOnHandQty(organizationCode, item.getItemCode(), item.getLotNo(), item.getWarehouseCode(), otherUncommitQty, headObj
						.getLastUpdatedBy(), headObj.getBrandCode());
			}
		}
	}

	/**
	 * 變更ON_HAND數量
	 *
	 * @param organizationCode
	 * @param itemCode
	 * @param lotNo
	 * @param warehouseCode
	 * @param otherUncommitQty
	 * @param lastUpdatedBy
	 */
	private void modifyOnHandQty(String organizationCode, String itemCode, String lotNo, String warehouseCode, Double otherUncommitQty,
			String lastUpdatedBy, String brandCode) {
		log.info("ImReceiveHeadService.modifyOnHandQty organizationCode=" + organizationCode + ",itemCode=" + itemCode + ",lotNo=" + lotNo
				+ ",warehouseCode=" + warehouseCode + ",otherUncommitQty=" + otherUncommitQty + ",lastUpdatedBy=" + lastUpdatedBy);
		ImOnHandId id = new ImOnHandId();
		id.setOrganizationCode(organizationCode);
		id.setItemCode(itemCode);
		id.setLotNo(lotNo); // 設定批號
		id.setWarehouseCode(warehouseCode);
		// imOnHandDAO.findById(id);
		ImOnHand imOnHand = imOnHandDAO.findByIdentification(organizationCode, brandCode, itemCode, warehouseCode, lotNo  );

		if (null != imOnHand) { // update
			imOnHand.setOtherUncommitQty(imOnHand.getOtherUncommitQty() + otherUncommitQty);
			imOnHand.setLastUpdateDate(new Date());
			imOnHand.setLastUpdatedBy(lastUpdatedBy);
			imOnHandDAO.update(imOnHand);
		} else { // insert
			imOnHand = new ImOnHand();
			imOnHand.setId(id);
			imOnHand.setBrandCode(brandCode);
			imOnHand.setStockOnHandQty(0D);
			imOnHand.setOutUncommitQty(0D);
			imOnHand.setMoveUncommitQty(0D);
			imOnHand.setInUncommitQty(0D);
			imOnHand.setOtherUncommitQty(otherUncommitQty);
			imOnHand.setCreationDate(new Date());
			imOnHand.setCreatedBy(lastUpdatedBy);
			imOnHand.setLastUpdateDate(new Date());
			imOnHand.setLastUpdatedBy(lastUpdatedBy);
			imOnHandDAO.save(imOnHand);
		}
	}

	/**
	 * 檢核
	 *
	 * @param headObj
	 * @return
	 * @throws Exception
	 */
	private boolean doAllValidate(ImAdjustmentHead headObj) throws FormException, Exception {
		log.info("ImAdjustmentHeadService.doAllValidate ");
		boolean isError = false;
		String message = null;
		String tabName = "明細資料頁籤";

		StringBuffer errorMessage = new StringBuffer();
		StringBuffer lineErrorMessage = new StringBuffer();
		// if (OrderStatus.SIGNING.equalsIgnoreCase(headObj.getStatus())) {
		String identification = MessageStatus.getIdentification(headObj.getBrandCode(),
    			headObj.getOrderTypeCode(), headObj.getOrderNo());
		if (removeDetailItemCode(headObj)) {
			errorMessage.append(MessageStatus.ERROR_NO_DETAIL+"<br>");
		}


		String dateType = "調整單日期";
		if(null == headObj.getAdjustmentDate()){
		    lineErrorMessage.append("調單日期必需填入\n");
		    errorMessage.append("調單日期必需填入<br>");
		}
		try{
		    ValidateUtil.isAfterClose(headObj.getBrandCode(), headObj.getOrderTypeCode(), dateType, headObj.getAdjustmentDate(),headObj.getSchedule() );
		}catch(Exception ex){
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, ex.getMessage(), headObj.getCreatedBy());
		}

		// 判斷 單別
		String sourceOrderTypeCode = headObj.getSourceOrderTypeCode();
		String sourceOrderNo = headObj.getSourceOrderNo();
		String orderTypeCode = headObj.getOrderTypeCode();
		log.info("ImAdjustmentHeadService.doAllValidate sourceOrderTypeCode=" + sourceOrderTypeCode);
		if ("AIR".equals(orderTypeCode)) {
			if (("IRF".equalsIgnoreCase(sourceOrderTypeCode) || "IRL".equalsIgnoreCase(sourceOrderTypeCode))
					&& (!StringUtils.hasText(sourceOrderNo)))
			    errorMessage.append("請輸入來源單號(進貨單)<br>");
		} else if ("ICA".equals(orderTypeCode) || "ACA".equals(orderTypeCode)) { // T1拆併貨單據類別：ICA；T2拆併貨單據類別：ACA
		    if ("DISTRIBUTE".equalsIgnoreCase(sourceOrderTypeCode) || "COMBINE".equalsIgnoreCase(sourceOrderTypeCode) || "CHANGE_ITEM".equalsIgnoreCase(sourceOrderTypeCode) ) {
//			HashMap totalAmount = new HashMap();
			List<ImAdjustmentLine> imAdjustmentLines = headObj.getImAdjustmentLines();
			for (int i = 0; i < imAdjustmentLines.size(); i++) {
			    ImAdjustmentLine line = (ImAdjustmentLine) imAdjustmentLines.get(i);
			    String lotNo = line.getLotNo();
			    String itemCode = line.getItemCode();
			    Double difQuantity = line.getDifQuantity();
			    String sourceItemCode = line.getSourceItemCode();
			    String warehouseCode = line.getWarehouseCode();

			    // 檢查品號
			    if( !StringUtils.hasText(itemCode) ){
				log.error(tabName + "中第" + (i + 1) + "項明細的品號不能為空！");
				lineErrorMessage.append(tabName + "中第" + (i + 1) + "項明細的品號不能為空！\n");
			    }else{
				ImItem imItem = imItemDAO.findItem(headObj.getBrandCode(), itemCode);
				if(null == imItem){
				    log.error("查無"+tabName + "中第" + (i + 1) + "項明細品號資料！");
				    lineErrorMessage.append("查無"+tabName + "中第" + (i + 1) + "項明細品號資料\n");
				}else{
				    // 批號檢查
				    if( !StringUtils.hasText(lotNo) ){
					log.error(tabName + "中第" + (i + 1) + "項明細的批號不能為空！");
					lineErrorMessage.append(tabName + "中第" + (i + 1) + "項明細的批號不能為空！\n");
				    }else{
					// 若商品不用批號管理 檢查批號預設值
					String lotControl = imItem.getLotControl();
					log.info( "lotControl = " + lotControl );
					log.info( "lotNo = " + lotNo );
					if("N".equalsIgnoreCase(lotControl) && !lotNo.equals(SystemConfig.LOT_NO)){
					    log.error("查" + tabName + "中第" + (i + 1) + "項明細商品不用批號管理則批號應為預設值"+SystemConfig.LOT_NO+"(12個0)！");
					    lineErrorMessage.append("查" + tabName + "中第" + (i + 1) + "項明細商品不用批號管理則批號應為預設值"+SystemConfig.LOT_NO+"(12個0)！\n");
					}
				    }
				}
			    }

			    // 檢查庫別
			    if( !StringUtils.hasText(warehouseCode) ){
				log.error(tabName + "中第" + (i + 1) + "項明細的庫別不能為空！");
				lineErrorMessage.append(tabName + "中第" + (i + 1) + "項明細的庫別不能為空！\n");
			    }else{
				ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(headObj.getBrandCode(), warehouseCode, "Y");
				if(imWarehouse == null){
				    log.error("查無" + tabName + "中第" + (i + 1) + "項明細商品的庫別資料！");
				    lineErrorMessage.append("查無" + tabName + "中第" + (i + 1) + "項明細商品的庫別資料！\n");
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
				    if(!StringUtils.hasText(sourceItemCode)){
					log.error("查" + tabName + "中第" + (i + 1) + "項明細商品"+key+"差異數量為正數，應輸入關聯品號");
					lineErrorMessage.append("查" + tabName + "中第" + (i + 1) + "項明細商品"+key+"差異數量為正數，應輸入關聯品號\n");
//					throw new Exception("查" + tabName + "中第" + (i + 1) + "項明細商品拆貨差異數量為正數，應輸入關聯品號");
				    } else{
					itemCode = sourceItemCode;
				    }
				}
			    }else if("COMBINE".equalsIgnoreCase(sourceOrderTypeCode)){
				if(difQuantity < 0){
				    if(!StringUtils.hasText(sourceItemCode)){
					log.error("查" + tabName + "中第" + (i + 1) + "項明細商品併貨差異數量為負數，應輸入關聯品號");
					lineErrorMessage.append("查" + tabName + "中第" + (i + 1) + "項明細商品併貨差異數量為負數，應輸入關聯品號\n");
//					throw new Exception("查" + tabName + "中第" + (i + 1) + "項明細商品併貨差異數量為負數，應輸入關聯品號");
				    } else{
					itemCode = sourceItemCode;
				    }
				}
			    }

//			    if (imAdjustmentLine.getDifQuantity() > 0 && "DISTRIBUTE".equalsIgnoreCase(sourceOrderTypeCode)) {
//				if (!StringUtils.hasText(sourceItemCode)) {// 負項一定要有來源單號
//				    lineErrorMessage.append(itemCode + "關聯品號沒有輸入<br>");
//				} else {
//				    itemCode = sourceItemCode;
//				}
//			    } else if (imAdjustmentLine.getDifQuantity() < 0 && "COMBINE".equalsIgnoreCase(sourceOrderTypeCode)) {
//				if (!StringUtils.hasText(sourceItemCode)) {// 正項一定要有來源單號
//				    lineErrorMessage.append(itemCode + "關聯品號沒有輸入<br>");
//				} else {
//				    itemCode = sourceItemCode;
//				}
//			    }
//			    Double itemTotal = (Double) totalAmount.get(itemCode);
//			    if (null == itemTotal)
//				itemTotal = 0D;
//			    itemTotal = itemTotal + line.getLocalUnitCost() * line.getDifQuantity();
//			    totalAmount.put(itemCode, itemTotal);
			}

			/* 正數量 負數量要一致
				Set keys = totalAmount.keySet();
				if (null != keys) {
					Iterator keyI = keys.iterator();
					while (keyI.hasNext()) {
						String itemCode = (String) keyI.next();
						Double itemTotal = (Double) totalAmount.get(itemCode);
						if (itemTotal != 0) {
							throw new FormException(itemCode + "品號金額沒有平衡");
						}
					}
				}
			 */
		    } else {
			lineErrorMessage.append("請選擇[拆/併/換]貨單別必需填入\n");
			errorMessage.append("請選擇[拆/併/換]貨單別<br>");
		    }

		}

		if ("Y".equalsIgnoreCase(headObj.getOnHandStatus())) {
		    errorMessage.append(headObj.getOrderNo() + "已經做過ON_HAND,請再確認資料後重新執行核銷動作<br>");
		}

		// 等到確認才開啟
		// doValidate(headObj);

		// detail validate
		/*
		 * 明細確認 List<PoPurchaseOrderLine> items =
		 * headObj.getPoPurchaseOrderLines(); for(PoPurchaseOrderLine item :
		 * items){ poPurchaseOrderLineService.doValidate(item); }
		 */
		// }
		if(errorMessage.length()>0){
		    isError = true;
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, errorMessage.toString(), headObj.getCreatedBy());
		}
		if(lineErrorMessage.length()>0){
		    isError = true;
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, lineErrorMessage.toString(), headObj.getCreatedBy());
		}
		if(lineErrorMessage.length() > 0){
		    throw new Exception(lineErrorMessage.toString());

		}
		return isError;
	}

	/**
	 * 移除空白的Detail
	 *
	 * @param headObj
	 * @return boolean 是否還有 Detail
	 */
	private boolean removeDetailItemCode(ImAdjustmentHead headObj) {
		log.info("PoPurchaseOrderHeadService.checkDetailItemCode");
		List<ImAdjustmentLine> imAdjustmentLines = headObj.getImAdjustmentLines();
		Iterator<ImAdjustmentLine> it = imAdjustmentLines.iterator();
		while (it.hasNext()) {
			ImAdjustmentLine line = it.next();
			if (!StringUtils.hasText(line.getItemCode())) {
				it.remove();
				imAdjustmentLines.remove(line);
			}
		}
		// log.info("is empty " + items.isEmpty());
		return imAdjustmentLines.isEmpty();
		// return false;
	}

	/**
	 * 日結處理程序(Anber)
	 *
	 * @param ImAdjustmentHead
	 * @param opUser
	 * @throws FormException
	 * @throws NoSuchDataException
	 */
	public void executeDailyBalance(ImAdjustmentHead ImAdjustmentHead, String opUser) throws FormException, NoSuchDataException {
		log.info("ImAdjustmentHeadService.executeDailyBalance");
		String brandCode = ImAdjustmentHead.getBrandCode();
		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);

		String orderTypeCode = ImAdjustmentHead.getOrderTypeCode();
		String orderNo = ImAdjustmentHead.getOrderNo();
		String identification = MessageStatus.getIdentificationMsg(brandCode, orderTypeCode, orderNo);

		// 更新出貨單Status為CLOSE
		ImAdjustmentHead.setStatus(OrderStatus.CLOSE);
		List<ImAdjustmentLine> ImAdjustmentLines = ImAdjustmentHead.getImAdjustmentLines();
		if (ImAdjustmentLines != null && ImAdjustmentLines.size() > 0) {
			for (ImAdjustmentLine ImAdjustmentLine : ImAdjustmentLines) {
				imOnHandDAO.updateOtherOnHand(organizationCode, ImAdjustmentLine.getItemCode(), ImAdjustmentLine.getWarehouseCode(),
						ImAdjustmentLine.getLotNo(), ImAdjustmentLine.getDifQuantity(), opUser);

				// 產生交易明細檔 出庫
				ImTransation transation = new ImTransation();
				transation.setBrandCode(brandCode);
				transation.setTransationDate(ImAdjustmentHead.getAdjustmentDate());
				transation.setOrderTypeCode(orderTypeCode);
				transation.setOrderNo(orderNo);
				transation.setLineId(ImAdjustmentLine.getIndexNo());
				transation.setItemCode(ImAdjustmentLine.getItemCode());
				transation.setWarehouseCode(ImAdjustmentLine.getWarehouseCode());				
				if(ImAdjustmentHead.getAffectCost()==null || ImAdjustmentHead.getAffectCost().equals("")){
					transation.setAffectCost("N");
				}else{
					transation.setAffectCost(ImAdjustmentHead.getAffectCost());
				}				
				transation.setLotNo(ImAdjustmentLine.getLotNo());
				transation.setQuantity(ImAdjustmentLine.getDifQuantity());
				transation.setCostAmount(ImAdjustmentLine.getAmount());
				transation.setCreatedBy(opUser);
				transation.setCreationDate(new Date());
				transation.setLastUpdatedBy(opUser);
				transation.setLastUpdatedDate(new Date());
				imTransationDAO.save(transation);
			}
			imAdjustmentHeadDAO.update(ImAdjustmentHead);

		} else {
			throw new ValidationErrorException("查無" + identification + "的調整單明細資料！");
		}

	}

	/**
	 *
	 * @param itemCode
	 * @return
	 * @throws ValidationErrorException
	 */
	private Double getOldestUnitPrice(String itemCode) throws ValidationErrorException {
		log.info("ImAdjustmentHeadService.getOldestUnitPrice");
		// String brandCode,String typeCode
		Double price = 0D;
		try {
			StringBuffer nativeSql = new StringBuffer("SELECT UNIT_PRICE FROM IM_ITEM_PRICE where enable = 'Y' and item_code = '");
			nativeSql.append(itemCode);
			nativeSql.append("' and begin_date = (select MIN(begin_date) from IM_ITEM_PRICE where item_code = '");
			nativeSql.append(itemCode);
			nativeSql.append("' and enable = 'Y')");
			List results = nativeQueryDAO.executeNativeSql(nativeSql.toString());
			if (null != results && results.size() > 0) {
				price = ((BigDecimal) results.get(0)).doubleValue();
			}
		} catch (Exception ex) {
			throw new ValidationErrorException("調整單查無" + itemCode + "定價!!");
		}

		return price;
	}

	/**
	 * 取得最後單價(進貨成本)
	 *
	 * @param itemCode
	 *            品號
	 * @return
	 */
	private Double getAverageUnitCost(String itemCode, String brandCode) {
		log.info("PoPurchaseOrderLineService.getAverageUnitCost itemCode=" + itemCode);
		BigDecimal averageUnitCost = new BigDecimal(0);
		try {
			if (StringUtils.hasText(itemCode)) {
				String nativeSql = "SELECT H.ITEM_CODE, H.YEAR, H.MONTH, H.AVERAGE_UNIT_COST FROM IM_MONTHLY_BALANCE_HEAD H , (SELECT BRAND_CODE, ITEM_CODE, MAX (YEAR||MONTH) YM FROM IM_MONTHLY_BALANCE_HEAD WHERE BRAND_CODE ='"
						+ brandCode
						+ "' AND ITEM_CODE='"
						+ itemCode
						+ "' GROUP BY BRAND_CODE, ITEM_CODE) MAX_DAY WHERE 1=1 AND H.BRAND_CODE = MAX_DAY.BRAND_CODE AND H.ITEM_CODE=MAX_DAY.ITEM_CODE AND (H.YEAR|| H.MONTH) = YM";
				List reList = nativeQueryDAO.executeNativeSql(nativeSql);
				if (null != reList && reList.size() > 0) {
					Object[] result = (Object[]) reList.get(0);
					if (result.length >= 4)
						averageUnitCost = (BigDecimal) result[3];
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Double(averageUnitCost.doubleValue());
	}
	    /**
	     * 匯入拆/拼/變更貨號單明細
	     *
	     * @param headId
	     * @param priceLists
	     * @throws Exception
	     */
	    public void executeImportImAdjustmentLists(Long headId, List lineLists) throws Exception{

	        try{
	        	imAdjustmentLineService.deleteLineLists(headId);
		    	if(lineLists != null && lineLists.size() > 0){
		    	    ImAdjustmentHead imAdjustmentHeadTmp = new ImAdjustmentHead();
		    	imAdjustmentHeadTmp.setHeadId(headId);
		    	    for(int i = 0; i < lineLists.size(); i++){
		    		ImAdjustmentLine  imAdjustmentLine = (ImAdjustmentLine)lineLists.get(i);
		    		imAdjustmentLine.setImAdjustmentHead(imAdjustmentHeadTmp);
		    		imAdjustmentLine.setIndexNo(i+1L);
		    		imAdjustmentLineDAO.save(imAdjustmentLine);
		    	    }
		    	}
	        }catch (Exception ex) {
		        log.error("拆/拼/變更貨號單明細匯入時發生錯誤，原因：" + ex.toString());
		        throw new Exception("拆/拼/變更貨號單明細匯入時發生錯誤，原因：" + ex.getMessage());
		    }
	    }
	    /**
	     * 檢核拆/拼/變更貨號單相關資料
	     *
	     * @param poPurchaseOrderHead
	     * @throws ValidationErrorException
	     * @throws NoSuchObjectException
	     */
	    public void checkImAdjustmentData(ImAdjustmentHead imAdjustment, String programId, String identification)
	        throws ValidationErrorException {

		String message = null;
		List errorMsgs = new ArrayList(0);
		try{
		    if(doAllValidate(imAdjustment)){
			errorMsgs.add("錯誤發生");
		    }

		}catch(Exception ex){
		    message = "檢核拆/拼/變更貨號單時發生錯誤，原因：" + ex.toString();
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imAdjustment.getCreatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}

		if(errorMsgs.size() > 0){
		    throw new ValidationErrorException("拆/拼/變更貨號單未通過檢核，請點選訊息提示按鈕檢視錯誤訊息！");
		}
	    }
	    public String getIdentification(Long headId) throws Exception{

		   String id = null;
		   try{
		       ImAdjustmentHead imAdjustment = findById(headId);
		       if(imAdjustment != null){
			   id = MessageStatus.getIdentification(imAdjustment.getBrandCode(),
				   imAdjustment.getOrderTypeCode(), imAdjustment.getOrderNo());
		       }else{
		           throw new NoSuchDataException("拆/拼/變更貨號單主檔查無主鍵：" + headId
				+ "的資料！");
		       }
		       return id;
		   }catch(Exception ex){
		       log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
		       throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());
		   }
	    }
	    /**
	     * 更新拆/拼/變更貨號單主檔及明細檔
	     *
	     * @param parameterMap
	     * @return Map
	     * @throws Exception
	     */
	    public Map updateAJAXImAdjustment(Map parameterMap) throws FormException, Exception {

	        HashMap resultMap = new HashMap();
	        try{
		    	Object formBindBean = parameterMap.get("vatBeanFormBind");
		    	Object formLinkBean = parameterMap.get("vatBeanFormLink");
		    	Object otherBean = parameterMap.get("vatBeanOther");
		    	String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
		    	String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
		    	String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
		    	//String processId = (String)PropertyUtils.getProperty(otherBean, "processId");
		    	//String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");

		    	if(!StringUtils.hasText(formStatus)){
		    	    throw new ValidationErrorException("單據狀態參數為空值，無法執行存檔！");
		    	}
		    	//取得欲更新的bean

		    	ImAdjustmentHead imAdjustment = getActualImAdjustment(formLinkBean);


		        AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imAdjustment);
		         if(OrderStatus.SAVE.equals(formStatus)||OrderStatus.VOID.equals(formStatus)){
		             //無需任何事，下面更改status
		         }else if(OrderStatus.SAVE.equals(beforeChangeStatus)||OrderStatus.SIGNING.equals(formStatus) || OrderStatus.REJECT.equals(beforeChangeStatus)){
		          //========================檢核資料======================================
		    		System.out.println("----------validate InventoryAdjustment start----------");
		    		String identification = MessageStatus.getIdentification(imAdjustment.getBrandCode(),
		    			imAdjustment.getOrderTypeCode(), imAdjustment.getOrderNo());
		    		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
		    		checkImAdjustmentData(imAdjustment,PROGRAM_ID,identification);

		    		System.out.println("----------validate InventoryAdjustment End----------");
		    	    //============remove delete mark record=============
		    	    //removeDeleteMarkLineForItem(promotionPO);
		    		removeDetailItemCode(imAdjustment);
		    		doOnHand(imAdjustment, false);
				setLotNo(imAdjustment);
		        }
		        imAdjustment.setStatus(formStatus);
		    	String resultMsg = modifyImAdjustment(imAdjustment, employeeCode);
		    	resultMap.put("entityBean", imAdjustment);
		    	resultMap.put("resultMsg", resultMsg);

		    	return resultMap;
		    	//=============================================
		        } catch (FormException fe) {
		        log.error("拆/拼/變更貨號單存檔失敗，原因：" + fe.toString());
		        throw new FormException(fe.getMessage());
		    } catch (Exception ex) {
		        log.error("拆/拼/變更貨號單存檔時發生錯誤，原因：" + ex.toString());
		        throw new Exception("拆/拼/變更貨號單存檔時發生錯誤，原因：" + ex.getMessage());
		    }
	    }


	    private ImAdjustmentHead getActualImAdjustment(Object bean) throws FormException, Exception{

		ImAdjustmentHead imAdjustmentHead = null;
	        String id = (String)PropertyUtils.getProperty(bean, "headId");
	        System.out.println("headId=" + id);
		if(StringUtils.hasText(id)){
	            	Long headId = NumberUtils.getLong(id);
	            	imAdjustmentHead = findById(headId);
        	    	if(imAdjustmentHead  == null){
        	    	    throw new NoSuchObjectException("查無拆/拼/變更貨號單主鍵：" + headId + "的資料！");
        	    	}
	        }else{
	    		throw new ValidationErrorException("傳入的拆/拼/變更貨號單主鍵為空值！");
	        }
		    return imAdjustmentHead;
	    }
	    /**
	     * 暫存單號取實際單號並更新至ImAdjustment主檔
	     *
	     * @param ImAdjustmentHead
	     * @param loginUser
	     * @return String
	     * @throws ObtainSerialNoFailedException
	     * @throws FormException
	     * @throws Exception
	     */
	    private String modifyImAdjustment(ImAdjustmentHead imAdjustment, String loginUser)
		    throws ObtainSerialNoFailedException, FormException, Exception {

	        if (AjaxUtils.isTmpOrderNo(imAdjustment.getOrderNo())) {
		       String serialNo = buOrderTypeService.getOrderSerialNo(
			       imAdjustment.getBrandCode(), imAdjustment.getOrderTypeCode());
		       if (!serialNo.equals("unknow")) {
			   imAdjustment.setOrderNo(serialNo);
		       } else {
			   throw new ObtainSerialNoFailedException("取得"
			       + imAdjustment.getOrderTypeCode() + "單號失敗！");
		       }
	        }
	        imAdjustment.setLastUpdatedBy(loginUser);
	        imAdjustment.setLastUpdateDate(new Date());
	        System.out.println("----------insert InventoryAdjustment start----------");
	        imAdjustmentHeadDAO.update(imAdjustment);
	        System.out.println("----------insert InventoryAdjustment End----------");
	        return imAdjustment.getOrderTypeCode() + "-" + imAdjustment.getOrderNo() + "存檔成功！";
	    }
	    public static Object[] startProcess(ImAdjustmentHead form) throws ProcessFailedException{

	        try{
		       String packageId = "Im_InventoryAdjustment";
		       String processId = "generalCreate";
		       String version = "20090817";
		       String sourceReferenceType = "(20090817)";
		       HashMap context = new HashMap();
		       context.put("brandCode", form.getBrandCode());
		       context.put("formId", form.getHeadId());
		       context.put("orderType", form.getOrderTypeCode());
		       context.put("orderNo", form.getOrderNo());
		       return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		   }catch (Exception ex){
		       log.error("拆/拼/變更貨號流程啟動失敗，原因：" + ex.toString());
		       throw new ProcessFailedException("拆/拼/變更貨號單流程啟動失敗！");
		   }
	    }
	    public static Object[] completeAssignment(long assignmentId, boolean approveResult,String approvalComment) throws ProcessFailedException{

		 	   try{
		 	       HashMap context = new HashMap();
		 	       context.put("approveResult", approveResult);
		 	       context.put("approvalComment", approvalComment);
		 	       return ProcessHandling.completeAssignment(assignmentId, context);
		 	   }catch (Exception ex){
		 	       log.error("完成拆/拼/變更貨號工作任務失敗，原因：" + ex.toString());
		 	       throw new ProcessFailedException("完成拆/拼/變更貨號工作任務失敗！");
		 	   }
	        }
	    public Long getImAdjustmentHeadId(Object bean) throws FormException, Exception{

		   Long headId = null;
		   String id = (String)PropertyUtils.getProperty(bean, "headId");
	        System.out.println("headId=" + id);
		   if(StringUtils.hasText(id)){
	            headId = NumberUtils.getLong(id);
	        }else{
	    	       throw new ValidationErrorException("傳入的拆/拼/變更貨號單主鍵為空值！");
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
	    public Map updateImAdjustmentHeadWithActualOrderNO(Map parameterMap) throws FormException, Exception {

	        HashMap resultMap = new HashMap();
	        try{
	            Object formBindBean = parameterMap.get("vatBeanFormBind");
	    	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    	    Object otherBean = parameterMap.get("vatBeanOther");
	    	    Long headId = getImAdjustmentHeadId(formLinkBean);
	    	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
	    	       //取得欲更新的bean
	    	    ImAdjustmentHead imAdjHead = getActualImAdjustment(formLinkBean);
	    	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imAdjHead);
	    	    String resultMsg = modifyImAdjustment(imAdjHead, employeeCode);
	    	    resultMap.put("entityBean", imAdjHead);
	            resultMap.put("resultMsg", resultMsg);
	            return resultMap;
	        } catch (FormException fe) {
		       log.error("拆/拼/變更貨號單存檔失敗，原因：" + fe.toString());
		       throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		       log.error("拆/拼/變更貨號單存檔時發生錯誤，原因：" + ex.toString());
		       throw new Exception("拆/拼/變更貨號單存檔時發生錯誤，原因：" + ex.getMessage());
		}
	   }
	/**
	 * find by pk
	 *
	 * @param headId
	 * @return
	 */
	public ImAdjustmentHead findById(Long headId) {
		return imAdjustmentHeadDAO.findById(headId);
	}

	public void setImAdjustmentHeadDAO(ImAdjustmentHeadDAO imAdjustmentHeadDAO) {
		this.imAdjustmentHeadDAO = imAdjustmentHeadDAO;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
		this.imWarehouseDAO = imWarehouseDAO;
	}

	public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
		this.imOnHandDAO = imOnHandDAO;
	}

	public void setImReceiveHeadDAO(ImReceiveHeadDAO imReceiveHeadDAO) {
		this.imReceiveHeadDAO = imReceiveHeadDAO;
	}

	public void setImTransationDAO(ImTransationDAO imTransationDAO) {
		this.imTransationDAO = imTransationDAO;
	}

	public void setFiInvoiceHeadService(FiInvoiceHeadService fiInvoiceHeadService) {
		this.fiInvoiceHeadService = fiInvoiceHeadService;
	}

	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}
	
	public void setImItemService(ImItemService imItemService) {
		this.imItemService = imItemService;
	}

	public void setPoPurchaseOrderLineService(PoPurchaseOrderLineService poPurchaseOrderLineService) {
		this.poPurchaseOrderLineService = poPurchaseOrderLineService;
	}

	public void setImAdjustmentLineService(ImAdjustmentLineService imAdjustmentLineService) {
		this.imAdjustmentLineService = imAdjustmentLineService;
	}

	public void setPoPurchaseOrderHeadDAO(PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO) {
		this.poPurchaseOrderHeadDAO = poPurchaseOrderHeadDAO;
	}
	public void setImMovementService(ImMovementService imMovementService) {
		this.imMovementService = imMovementService;
	}
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	    this.siProgramLogAction = siProgramLogAction;
	}
	public void setImAdjustmentLineDAO(ImAdjustmentLineDAO imAdjustmentLineDAO) {
	    this.imAdjustmentLineDAO = imAdjustmentLineDAO;
	}
	public void setPoVerificationSheetDAO(PoVerificationSheetDAO poVerificationSheetDAO) {
	    this.poVerificationSheetDAO = poVerificationSheetDAO;
	}
	public void setFiInvoiceHeadDAO(FiInvoiceHeadDAO fiInvoiceHeadDAO) {
	    this.fiInvoiceHeadDAO = fiInvoiceHeadDAO;
	}
	public void setImSysLogDAO(ImSysLogDAO imSysLogDAO) {
		this.imSysLogDAO = imSysLogDAO;
	}
	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO){
		this.buEmployeeDAO = buEmployeeDAO;
	}
	public void setCmDeclarationHead(CmDeclarationHead cmDeclarationHead){
		this.cmDeclarationHead = cmDeclarationHead;
	}
	public void setCmDeclarationItem(CmDeclarationItem cmDeclarationItem){
		this.cmDeclarationItem = cmDeclarationItem;
	}
	public void setCmDeclarationHeadDAO(CmDeclarationHeadDAO cmDeclarationHeadDAO){
		this.cmDeclarationHeadDAO = cmDeclarationHeadDAO;
	}
	public void setCmDeclarationItemDAO(CmDeclarationItemDAO cmDeclarationItemDAO){
		this.cmDeclarationItemDAO = cmDeclarationItemDAO;
	}
	public void setCmDeclarationOnHandDAO(CmDeclarationOnHandDAO cmDeclarationOnHandDAO){
		this.cmDeclarationOnHandDAO = cmDeclarationOnHandDAO;
	}
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO){
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
	public void setBaseDAO(BaseDAO baseDAO) {
    	this.baseDAO = baseDAO;
    }
	
	public void setCmDeclarationOnHandViewDAO(CmDeclarationOnHandViewDAO cmDeclarationOnHandViewDAO) {
		this.cmDeclarationOnHandViewDAO = cmDeclarationOnHandViewDAO;
	}

	/**
	 * 拆併貨單據作廢
	 *
	 * @param modifyObj
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public String executeVoid(ImAdjustmentHead modifyObj) throws FormException, Exception {
		log.info("ImAdjustmentHeadService.executeVoid ");
		if (null != modifyObj) {
			log.info("ImAdjustmentHeadService.executeVoid ===> void");
			System.out.println("line size is " + modifyObj.getImAdjustmentLines().size());
			System.out.println("headObj.getOnHandStatus() modifyObj=" + modifyObj.getOnHandStatus());
			try {
				if ("VOID".equalsIgnoreCase(modifyObj.getStatus())) {

					String organizationCode = UserUtils.getOrganizationCodeByBrandCode(modifyObj.getBrandCode());
					if (organizationCode == null) {
						throw new FormException("依據品牌代碼：" + modifyObj.getBrandCode() + "查無其組織代號！");
					}

					modifyObj.setOnHandStatus("N");

					List<ImAdjustmentLine> items = modifyObj.getImAdjustmentLines();
					if (items.size() > 0) {
						for (ImAdjustmentLine item : items) {
							Double otherUncommitQty = item.getDifQuantity();
							otherUncommitQty = 0 - otherUncommitQty;
							ImItem imItem = imItemDAO.findById(item.getItemCode());
							long salesRatio = 1;
							if ((null != imItem.getSalesRatio()) && (imItem.getSalesRatio() > 0))
								salesRatio = imItem.getSalesRatio();
							otherUncommitQty = salesRatio * otherUncommitQty;
							modifyOnHandQty(organizationCode, item.getItemCode(), item.getLotNo(), item.getWarehouseCode(),
									otherUncommitQty, modifyObj.getLastUpdatedBy(), modifyObj.getBrandCode());
						}
					}

				}
				modifyObj.setLastUpdateDate(new Date());
				// 藉由進貨單去作,目前做法是將數量寫到進貨單 差異量
				setLotNo(modifyObj);
				imAdjustmentHeadDAO.update(modifyObj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new FormException("查無表單主檔資料");
		}
		return MessageStatus.SUCCESS;
	}
	
	private HashMap getRequestParameter(Map parameterMap, boolean isSubmitAction)
		throws Exception {

		Object otherBean = parameterMap.get("vatBeanOther");
		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
		String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
		String isSpecial = (String)PropertyUtils.getProperty(otherBean, "special"); // 特殊展延單別註記
		HashMap conditionMap = new HashMap();
		conditionMap.put("loginBrandCode", loginBrandCode);
		conditionMap.put("loginEmployeeCode", loginEmployeeCode);
		conditionMap.put("orderTypeCode", orderTypeCode);
		conditionMap.put("formId", formId);
		conditionMap.put("isSpecial",isSpecial);
		if (isSubmitAction) {
			String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formStatus = (String) PropertyUtils.getProperty(otherBean,"formStatus");
			conditionMap.put("beforeChangeStatus", beforeChangeStatus);
			conditionMap.put("formStatus", formStatus);
		}
		return conditionMap;
	}
	
	
	/**報單展延初始化
     * 
     * @param parameterMap
     * @return Map
     * @throws Extention
     */
    public Map executeExtentionInitial(Map parameterMap) throws Exception{
    	log.info("executeInitial");
    	
    	
    	HashMap resultMap = new HashMap();
    	ImAdjustmentHead imAdjustmentHead = null;
    	Date day = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    	String tmpOrderNo = AjaxUtils.getTmpOrderNo(); // 產生暫存單號
    	
		try {
			HashMap argumentMap = getRequestParameter(parameterMap, false);
			
			Long formId = NumberUtils.getLong((Long) argumentMap.get("formId"));
			
			Object otherBean = parameterMap.get("vatBeanOther");
//			String isSpecial = (String)PropertyUtils.getProperty(otherBean, "special");
			
			if(formId > 0){
				
				imAdjustmentHead = findImAdjustmentHead(argumentMap, resultMap);
			}else{
				
				imAdjustmentHead = executeNew(parameterMap);
			}
			//特殊展延單
//			Map multiList = new HashMap(0);
//			List<BuCommonPhraseLine> allCustomsWarehouseCodes = baseDAO.findByProperty("BuCommonPhraseLine", new String[] {
//					"id.buCommonPhraseHead.headCode", "enable" }, new Object[] { "CustomsWarehouseCode", "Y" }, "indexNo");
//			multiList.put("allCustomsWarehouseCodes", AjaxUtils.produceSelectorData(allCustomsWarehouseCodes, "lineCode",
//					"name", false, true));
//			resultMap.put("multiList", multiList);
			
				
				resultMap.put("form", imAdjustmentHead);
				resultMap.put("orderTypeCode",  (String)argumentMap.get("orderTypeCode"));
				resultMap.put("orderNo",  		tmpOrderNo);
				resultMap.put("createByName",   UserUtils.getUsernameByEmployeeCode(imAdjustmentHead.getCreatedBy()));
				resultMap.put("statusName", 	OrderStatus.getChineseWord(imAdjustmentHead.getStatus()));
				resultMap.put("creationDate", 	sdf.format(day));
				
				resultMap.put("isSpecial", 	imAdjustmentHead.getIsSpecial());
//				resultMap.put("extentionTime", 	imAdjustmentHead.getExtentionTime());
//				resultMap.put("extentionTimeType", 	imAdjustmentHead.getExtentionTimeType());
				
			return resultMap;
		} catch (Exception ex) {
			log.error("報單展延初始化失敗，原因：" + ex.toString());
			throw new Exception("報單展延初始化失敗，原因：" + ex.toString());
		}
		
	}
    
    /**
	 * 取得新的報單展延表頭
	 * @param otherBean
	 * @return
	 * @throws Exception
	 */
	public ImAdjustmentHead executeNew(Map parameterMap)throws Exception{
		log.info("executeNew");
		ImAdjustmentHead form = new ImAdjustmentHead();
		
		try {
			
			HashMap argumentMap = getRequestParameter(parameterMap, false);
			
			String tmpOrderNo = AjaxUtils.getTmpOrderNo(); // 產生暫存單號
        	
        	form.setOrderTypeCode((String)argumentMap.get("orderTypeCode"));
        	form.setBrandCode((String)argumentMap.get("loginBrandCode"));
            form.setOrderNo(tmpOrderNo); // 寫入暫存單號    	 
			form.setStatus(OrderStatus.SAVE );
			form.setCreatedBy((String)argumentMap.get("loginEmployeeCode"));
			form.setLastUpdatedBy((String)argumentMap.get("loginEmployeeCode"));
			form.setLastUpdateDate(new Date());
			//form.setAdjustmentDate( DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH, DateUtils.getCurrentDateStr(DateUtils.C_DATE_PATTON_SLASH)) );
			form.setCreationDate(new Date());	
			form.setAffectCost("N");
			form.setTaxType("P");
			form.setIsSpecial(((String)(argumentMap.get("isSpecial"))).equals("Y")?"Y":"N");
			
			//非特殊展延單，預設12個月
			if(!((String)(argumentMap.get("isSpecial"))).equals("Y")){
				form.setExtentionTime(12);
				form.setExtentionTimeType("month");
			}
			saveModHead(form);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("建立報單展延主檔失敗,原因:"+e.toString());
			throw new Exception("建立報單展延主檔失敗,原因:"+e.toString());
		}
		return form;
	}
	 
	public void saveModHead(ImAdjustmentHead imAdjustmentHead) throws Exception{
	    	
	    	try{
	    		
	    		String tmpOrderNo = AjaxUtils.getTmpOrderNo();
	    		imAdjustmentHead.setOrderNo(tmpOrderNo);
	    		imAdjustmentHeadDAO.save(imAdjustmentHead);
	    	    
	    	}catch(Exception ex){
	    		ex.printStackTrace();
	    	    log.error("取得暫時單號發生錯誤，原因：" + ex.toString());
	    	    throw new Exception("取得暫時單號儲存商發生錯誤，原因：" + ex.getMessage());
	    	}	
	    }
	
	public Map updateAJAXExtention(Map parameterMap) throws Exception {
		log.info("updateAJAXExtention-----");
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap();
		String resultMsg = null;
		
		//try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			String beforeStaus = (String)PropertyUtils.getProperty(formBindBean, "status");
			
			String nextStatus =  getStatus(formAction,beforeStaus);
			
			String fileNo = (String)PropertyUtils.getProperty(formBindBean, "fileNo");
			ImAdjustmentHead head = this.getActualImAdjustment(formLinkBean);
			
			this.setOrderNo(head);
			
			Boolean check = validateItem(parameterMap);
			
			log.info("check=" + check);
			if(check.equals(true)){
				log.info("回寫報單");
				List<ImAdjustmentLine> imAdjustmentLines = head.getImAdjustmentLines();
				for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
					String originalDeclarationNo = imAdjustmentLine.getOriginalDeclarationNo();
							
					CmDeclarationHead cdh = cmDeclarationHeadDAO.findOneCmDeclaration(originalDeclarationNo);
					
					if(cdh.getDeclType().equals("D7")&& cdh.getIsExtention() != "Y" ){
						log.info("D7 回寫進貨單");
								
						ImReceiveHead irh = imReceiveHeadDAO.findOneImReceive(originalDeclarationNo);
										
						irh.setOrgImportDate(imAdjustmentLine.getOrgImportDate());
						irh.setImportDate(DateUtils.addMonths(imAdjustmentLine.getOrgImportDate(), 12));
						
						List<ImReceiveItem> imReceiveItems = irh.getImReceiveItems();
						for (ImReceiveItem imReceiveItem : imReceiveItems) {											
							imReceiveItem.setOrgImportDate(imReceiveItem.getOriginalDeclarationDate());
							imReceiveItem.setOriginalDeclarationDate(DateUtils.addMonths(imReceiveItem.getOriginalDeclarationDate(), 12));
						}
					}
						
					cdh.setFileNo(fileNo);
					cdh.setIsExtention("Y");
					cdh.setOrgImportDate(imAdjustmentLine.getOrgImportDate());
					cdh.setImportDate(DateUtils.addMonths(imAdjustmentLine.getOrgImportDate(), 12));
								
					cmDeclarationHeadDAO.update(cdh);
				}
			}	
		
			head.setStatus(nextStatus);
			head.setLastUpdatedBy(loginEmployeeCode);
			head.setLastUpdateDate(new Date());
			imAdjustmentHeadDAO.update(head);
			
			resultMsg = head.getOrderTypeCode() + "-" + head.getOrderNo() + "存檔成功！ 是否繼續新增？";
			resultMap.put("entityBean", head);
			resultMap.put("resultMsg", resultMsg);
			resultMap.put("vatMessage", msgBox);

		/*}  catch (Exception ex) {
			ex.printStackTrace();
			log.error("報單展延存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception(ex.toString());
		}*/
			return resultMap;
		}
	
	public Map updateAJAXExtention2(Map parameterMap) throws Exception {
		log.info("updateAJAXExtention-----");
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap();
		String resultMsg = null;
		
		//try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			String beforeStaus = (String)PropertyUtils.getProperty(formBindBean, "status");
			
			//特殊展延單
			String extentionTimeType = (String)PropertyUtils.getProperty(formBindBean, "extentionTimeType");
//			String isSpecial = (String)PropertyUtils.getProperty(formBindBean, "isSpecial");
			
			String extentionTimeStr = (String)PropertyUtils.getProperty(formBindBean, "extentionTime");
			
			String nextStatus =  getStatus(formAction,beforeStaus);
			
			String fileNo = (String)PropertyUtils.getProperty(formBindBean, "fileNo");
			ImAdjustmentHead head = this.getActualImAdjustment(formLinkBean);
			
			this.setOrderNo(head);
			
			Boolean check = validateItem(parameterMap);
		
//			ImAdjustmentHead imAdjustmentHead = this.getActualImAdjustment(formLinkBean);
//			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imAdjustmentHead);
			
			int extentionTime = 0;
			
			if(!("".equals(extentionTimeStr))){
				extentionTime = Integer.valueOf(extentionTimeStr);
			}
			
			log.info("check=" + check);
			if(check.equals(true)){
				log.info("回寫報單");
//				List<ImAdjustmentLine> imAdjustmentLines = imAdjustmentHead.getImAdjustmentLines();
				List<ImAdjustmentLine> imAdjustmentLines = head.getImAdjustmentLines();
				
				Date expiryDate = new Date();
				
				for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
					String originalDeclarationNo = imAdjustmentLine.getOriginalDeclarationNo();
					
					CmDeclarationHead cdh = cmDeclarationHeadDAO.findOneCmDeclaration(originalDeclarationNo);
						
					cdh.setFileNo(fileNo);
					cdh.setIsExtention("Y");
					
					cmDeclarationHeadDAO.update(cdh);
					
					//特殊展延單
					
					//展延前的原始屆期日
					Date orgExpiryDate = cmDeclarationOnHandDAO.getLockedOnHand(imAdjustmentLine.getOriginalDeclarationNo(),
							imAdjustmentLine.getOriginalDeclarationSeq(), imAdjustmentLine.getCustomsItemCode(),  
							imAdjustmentLine.getWarehouseCode(),"T2").get(0).getExpiryDate();  //原屆期日
					
					if(orgExpiryDate == null){
						orgExpiryDate = DateUtils.addMonths(imAdjustmentLine.getOrgImportDate(),24);
					}

					if("month".equals(extentionTimeType)){
						expiryDate = DateUtils.addMonths(orgExpiryDate,extentionTime);
					}else if("day".equals(extentionTimeType)){
						expiryDate = DateUtils.addDays(orgExpiryDate,extentionTime);
					}

					
					log.info("NO = "+imAdjustmentLine.getOriginalDeclarationNo() + "INDEXNO=" + imAdjustmentLine.getOriginalDeclarationSeq() +"ITEMCODE = "+imAdjustmentLine.getCustomsItemCode());
					
					
					cmDeclarationOnHandDAO.updateInOnHand2(imAdjustmentLine.getOriginalDeclarationNo(),
							imAdjustmentLine.getOriginalDeclarationSeq(), imAdjustmentLine.getCustomsItemCode(),  
							imAdjustmentLine.getWarehouseCode(),"T2",expiryDate);
					
					
				}
			}	
			head.setStatus(nextStatus);
			head.setLastUpdatedBy(loginEmployeeCode);
			head.setLastUpdateDate(new Date());
//			log.info(" 1 = " + head.getExtentionTime() + " 2 = " + extentionTime);
//			head.setExtentionTime(extentionTime);
			
//			head.setExtentionTimeType(extentionTimeType);
//			head.setIsSpecial(isSpecial == null ? "" : isSpecial);
			imAdjustmentHeadDAO.update(head);
			
			resultMsg = head.getOrderTypeCode() + "-" + head.getOrderNo() + "存檔成功！ 是否繼續新增？";
			resultMap.put("entityBean", head);
			resultMap.put("resultMsg", resultMsg);
			resultMap.put("vatMessage", msgBox);

		/*}  catch (Exception ex) {
			ex.printStackTrace();
			log.error("報單展延存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception(ex.toString());
		}*/
			return resultMap;
		}
	
	/**
	 * 
	 * @param declarationNo
	 * @param declarationSeq
	 * @param itemCode
	 * @param warehouseCode
	 * @param brandCode
	 * @param currentExpiryDate
	 * @return
	 */
	public Date addExpiryDate(String declarationNo, Long declarationSeq,
			String itemCode, String warehouseCode, String brandCode){
		
		Date currentExpiryDate = cmDeclarationOnHandDAO.getLockedOnHand(declarationNo, declarationSeq, 
				itemCode, warehouseCode, brandCode).get(0).getExpiryDate();
	
		
		
		return currentExpiryDate;
	}
	
	public Boolean validateItem(Map parameterMap) throws Exception {
		
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");
		
		
		String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
		String fileNo = (String)PropertyUtils.getProperty(formBindBean, "fileNo");
		String customsWarehouseCode = (String)PropertyUtils.getProperty(formBindBean, "customsWarehouseCode");
		String extentionTimeStr = (String)PropertyUtils.getProperty(formBindBean, "extentionTime");
		String isSpecial = (String)PropertyUtils.getProperty(formBindBean, "isSpecial");
		ImAdjustmentHead head = this.getActualImAdjustment(formLinkBean);
		
		
		if (formAction.equals("SUBMIT") ){
			
			if ( "".equals(extentionTimeStr.trim()) ){
				log.info("沒展延時間");
				throw new ValidationErrorException("展延時間請勿空白");
			}
			
			if (!StringUtils.hasText(fileNo)){
				log.info("沒文號");
				throw new ValidationErrorException("請輸入文號");
			}else {					
				log.info("判斷文號是否展延");
//				List<CmDeclarationHead> cmfileno = cmDeclarationHeadDAO.findFileNo(fileNo);
				if("".equals(customsWarehouseCode)){
					throw new ValidationErrorException("沒有關別");
				}
				List<ImAdjustmentHead> imAdjustmentHeads = imAdjustmentHeadDAO.findExtensionByFileNo(fileNo, customsWarehouseCode);
									
				log.info("文號測試: " + imAdjustmentHeads==null?"文號沒被使用":imAdjustmentHeads.size());
				
				if (imAdjustmentHeads == null || imAdjustmentHeads.size() == 0 ){						
					log.info("回寫");
					List<ImAdjustmentLine> imAdjustmentLines = head.getImAdjustmentLines();
				
					for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
						
						String originalDeclarationNo = imAdjustmentLine.getOriginalDeclarationNo();
						
						if (StringUtils.hasText(originalDeclarationNo)){	        				
							CmDeclarationHead cdh = cmDeclarationHeadDAO.findOneCmDeclaration(originalDeclarationNo);
						
							//若文號不是空的 且 不是特殊展延  無法進行展延作業
							if (cdh.getFileNo() != null && !("Y".equals(isSpecial))){
								log.info("單號重複");		
								throw new ValidationErrorException("單號:" + cdh.getDeclNo() + "已一般展延過一次，請勿重複展延");
								
							}	
						}
					}
				
				}else{
					throw new ValidationErrorException("此文號已展延");
					}
			}	
			return true;
			
		}else{
			
			return false;
		}
		
		
	}
	
	public ImAdjustmentHead getActualExtention(Object bean) throws FormException, Exception {
		
		ImAdjustmentHead imAdjustmentHead = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
	

		if (StringUtils.hasText(id)) {
			Long headId = NumberUtils.getLong(id);
			imAdjustmentHead = findById(headId);
			if (imAdjustmentHead == null) {
				throw new NoSuchObjectException("查無報單展延主鍵：" + headId + "的資料！");
			}
			
		} else {
			throw new ValidationErrorException("傳入的報單展延主鍵為空值！");
		}

		return imAdjustmentHead;
	}
	
	 public static Object[] startExtentionProcess(ImAdjustmentHead form) throws ProcessFailedException{

	        try{
		       String packageId = "Im_GeneralAdjustment";
		       String processId = "process";
		       String version = "20161012";
		       String sourceReferenceType = "(20161012)";
		       HashMap context = new HashMap();
		       context.put("brandCode", form.getBrandCode());
		       context.put("formId", form.getHeadId());
		       context.put("orderType", form.getOrderTypeCode());
		       context.put("orderNo", form.getOrderNo());
		       return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		   }catch (Exception ex){
		       log.error("報單展延流程啟動失敗，原因：" + ex.toString());
		       throw new ProcessFailedException("報單展延流程啟動失敗！");
		   }
	    }
	 

		/**AJAX Load Page Data
		 * @param headObj
		 * @throws NoSuchMethodException
		 * @throws InvocationTargetException
		 * @throws IllegalAccessException
		 */
	    public List<Properties> getAJAXExtentionPageData(Properties httpRequest) throws Exception {
	    	
	    	List<Properties> re        = new ArrayList();
	    	List<Properties> gridDatas = new ArrayList();
	    	
	    	// 要顯示的HEAD_ID
	    	Long headId   = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    	
	    	int iSPage = AjaxUtils.getStartPage(httpRequest);
	    	int iPSize = AjaxUtils.getPageSize(httpRequest);
	    	
	    	log.info("headId++++++++++" + headId);
    		
	    	HashMap findObjs = new HashMap();
	 	    findObjs.put("and model.imAdjustmentHead.headId = :headId", headId);

	 	    Map searchMap = baseDAO.search("ImAdjustmentLine as model", findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
	 	    List<ImAdjustmentLine> imAdjustmentLines = (List<ImAdjustmentLine>)searchMap.get(BaseDAO.TABLE_LIST);

	 	    HashMap map = new HashMap();
	 	    map.put("headId", headId);
    		    	
	    	if (null != imAdjustmentLines && imAdjustmentLines.size() > 0) {
	    			    			
	    		// 取得第一筆的INDEX
	    		Long firstIndex = imAdjustmentLines.get(0).getIndexNo();
	    		// 取得最後一筆 INDEX
	    		Long maxIndex = imAdjustmentLineService.findPageLineMaxIndex(Long.valueOf(headId));
	    		re.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELDS_NAMES, GRID_FIELDS_DEFAULT_VALUES, imAdjustmentLines, gridDatas, firstIndex, maxIndex));
	    	} else {
	    		re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELDS_NAMES, GRID_FIELDS_DEFAULT_VALUES, gridDatas));
	    	}
	    	return re;
	    }

	    public List<Properties> updateAJAXExtentionPageLinesData(Properties httpRequest) 
	    	throws NumberFormatException, FormException {
	    	
	    	String errorMsg        = null;
	    	
	    	try{
	    		
		    	String gridData        = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		    	int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		    	int gridRowCount       = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		    	Long headId            = NumberUtils.getLong(httpRequest.getProperty("headId"));
		    	//String originalDeclarationNo =  httpRequest.getProperty("originalDeclarationNo");
		    	//log.info("originalDeclarationNo-----" + originalDeclarationNo);
		    	
		    	ImAdjustmentHead head = imAdjustmentHeadDAO.findById(headId);
		    	
		    	// 將STRING資料轉成List Properties record data
		    	List<Properties> upRecords = AjaxUtils.getGridFieldValue( gridData, gridLineFirstIndex, gridRowCount,
		    								  ImAdjustmentHeadService.GRID_FIELDS_NAMES);
		    	log.info("upRecords=" + upRecords.size());

		    	int indexNo = imAdjustmentLineService.findPageLineMaxIndex(headId).intValue();	// Get Max INDEX NO
		    	
		    	if (null != upRecords && null != head) {
		    	    for (Properties upRecord : upRecords) {
		    		// 先載入HEAD_ID OR LINE DATA
		    	    	String originalDeclarationNo ="";
		    	    	//String ExtensionDate = "";
		    	    	
		    	    	originalDeclarationNo = upRecord.getProperty(GRID_FIELDS_NAMES[1]);
		    	    	Date ExtensionDate = DateUtils.parseDate(upRecord.getProperty(GRID_FIELDS_NAMES[7]));
		    	    	
		    	    	Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
		    	    	log.info("originalDeclarationNo-----" + originalDeclarationNo);
		    		if (StringUtils.hasText(originalDeclarationNo)) {
		    		
		    			ImAdjustmentLine imAdjustmentLine = imAdjustmentLineService.findLine(head.getHeadId(), lineId);
		    		
		    			if (null != imAdjustmentLine) {
		    			
		    				log.info("UPDATE");
		    			
		    				AjaxUtils.setPojoProperties(imAdjustmentLine, upRecord, GRID_FIELDS_NAMES, GRID_FIELDS_TYPES);
		    				imReceiveHeadDAO.update(imAdjustmentLine);	// UPDATE 
		    		
		    			} else {
		    			
		    				log.info("SAVE");
		    			
		    				indexNo++;
		    				imAdjustmentLine = new ImAdjustmentLine();
		    				AjaxUtils.setPojoProperties(imAdjustmentLine, upRecord, GRID_FIELDS_NAMES, GRID_FIELDS_TYPES);
		    				imAdjustmentLine.setIndexNo(Long.valueOf(indexNo));
		    				imAdjustmentLine.setImAdjustmentHead(head);
		    				imAdjustmentHeadDAO.save(imAdjustmentLine);	// INSERT
		    		    	}
		    		}
		    	    }
		    	} else {
		    	    errorMsg = "沒有報單展延單頭資料";
		    	}
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		errorMsg = e.getMessage();
	    	}
	    	
	    	return AjaxUtils.getResponseMsg(errorMsg);
	        }
    
	    

	/**
	 * 依formId取得實際地點主檔 in 送出
	 * 
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */    
	    
	public ImAdjustmentHead executeFindActual(Map parameterMap)
			throws FormException, Exception {

	    	Object otherBean = parameterMap.get("vatBeanOther");

	    	ImAdjustmentHead imAdjustmentHead = null;		


		try {			
			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
			
			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;
				
			imAdjustmentHead = null == formId ? this.executeNew(parameterMap) : this.findById(formId);

			parameterMap.put("entityBean", imAdjustmentHead);
			log.info("executefind:"+imAdjustmentHead.getHeadId());
	
				return imAdjustmentHead;
			} catch (Exception e) {

				log.error("取得實際主檔失敗,原因:" + e.toString());
				throw new Exception("取得實際主檔失敗,原因:" + e.toString());
			}
	}
	
	/**
	 * 驗證主檔
	 * 
	 * @param parameterMap
	 * @throws ValidationErrorException
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */

	/*public void validateHead(Map parameterMap) throws Exception {

				
		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		ImAdjustmentHead head = this.getActualImAdjustment(formLinkBean);
		List<ImAdjustmentLine> imAdjustmentLines = head.getImAdjustmentLines();
		
		log.info(imAdjustmentLines.size());
	
		for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
			
			String originalDeclarationNo = imAdjustmentLine.getOriginalDeclarationNo();
			Long originalDeclarationSeq = imAdjustmentLine.getOriginalDeclarationSeq();
			String customsItemCode = imAdjustmentLine.getCustomsItemCode();
			String itemCName = imAdjustmentLine.getItemCName();
			Date orgImportDate = imAdjustmentLine.getOrgImportDate();
			Date expiryDate = imAdjustmentLine.getExpiryDate();
			String unit = imAdjustmentLine.getUnit();
			
			
			log.info("originalDeclarationNo=---" + originalDeclarationNo);
					
		if(!StringUtils.hasText(originalDeclarationNo)) {
			throw new ValidationErrorException("報關單號不可為空！");
		//}else if (!StringUtils.hasText((String)originalDeclarationSeq)) {
		//	throw new ValidationErrorException("項次不可為空！");
		}else if (!StringUtils.hasText(customsItemCode)) {
			throw new ValidationErrorException("品號不可為空！");
		}else if (!StringUtils.hasText(itemCName)) {
			throw new ValidationErrorException("品名不可為空！");
		//}else if (!StringUtils.hasText(DateUtils.parseDate(orgImportDate))) {
			//throw new ValidationErrorException("進倉日不可為空！");
		//}else if (!StringUtils.hasText(expiryDate)) {
		//	throw new ValidationErrorException("屆期日不可為空！");
		}else if (!StringUtils.hasText(unit)) {
			throw new ValidationErrorException("單位不可為空！");
		}		
	}
	}*/
 
	/**
	 * 前端資料塞入bean
	 * 
	 * @param parameterMap
	 * @return
	 */
	public Map updateImAdjustmentExtentionBean(Map parameterMap)throws FormException, Exception {
		Map resultMap = new HashMap();
		
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
		    Object formLinkBean = parameterMap.get("vatBeanFormLink");
		    Object otherBean = parameterMap.get("vatBeanOther");
		    
		    String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		    
			ImAdjustmentHead imAdjustmentHead = this.getActualImAdjustment(formLinkBean);
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imAdjustmentHead);
			
			this.setOrderNo(imAdjustmentHead);

			imAdjustmentHead.setLastUpdatedBy(loginEmployeeCode);
			imAdjustmentHead.setLastUpdateDate(new Date());
			
			resultMap.put("entityBean", imAdjustmentHead);
			return resultMap;
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}

	}	
	
	/**
	 * 報單展延匯出明細 
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
			log.info("headId=" + headId + "+++" + "brandCode=" + brandCode + "+++" + "exportBeanName=" + exportBeanName );
			
			Object[] object = null;
		    
			object = new Object[] { "indexNo", "originalDeclarationNo", "originalDeclarationSeq", 
					    "reserve2", "reserve3", "customsItemCode", "itemCName", "orgImportDate", 
					    "expiryDate", "extensionDate", "unit",	"qty", "reserve1" };
			
			List<Object[]> imAdjustmentLine = imAdjustmentHeadDAO.findExtensionPageLine(headId, brandCode, exportBeanName);

			// 按excel表的欄位順序將資料放入Object[]，再一筆筆放到List
			List rowData = new ArrayList();
			for (int i = 0; i < imAdjustmentLine.size(); i++) {
				Object[] dataObject = (Object[]) imAdjustmentLine.get(i);
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
			log.error("載入頁面顯示報單展延明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示報單展延明細失敗！");
		}
	}
	
	/**
	 * 匯入報單展延明細
	 *
	 * @param headId
	 * @param promotionItems
	 * @throws Exception
	 */
	public void executeImportExtentionLine(Long headId, List extentionItems) throws Exception {
		log.info("executeImportMovementItems.." + headId);
		log.info("executeImportMovementItems..size" + extentionItems.size());
	
		List<ImAdjustmentLine> items = new ArrayList(0);
		try {
			// deleteMovementItems(headId);

			ImAdjustmentHead imAdjustmentHead = imAdjustmentHeadDAO.findById(headId);
			
			String brandCode = imAdjustmentHead.getBrandCode();
			//log.info("brandCode++++" + brandCode);
			
			if (imAdjustmentHead == null)
				throw new NoSuchObjectException("查無報單展延主鍵：" + headId + "的資料");
		
			if (extentionItems != null && extentionItems.size() > 0) {
				for (int i = 0; i < extentionItems.size(); i++) {
					
					ImAdjustmentLine imAdjustmentLine = (ImAdjustmentLine) extentionItems.get(i);
					
					CmDeclarationItem cmDeclarationItems = cmDeclarationItemDAO.findOneCmDeclarationItem
						(imAdjustmentLine.getOriginalDeclarationNo() , imAdjustmentLine.getOriginalDeclarationSeq());
						
					log.info("cmDeclarationItems+++" + cmDeclarationItems);
					
					String unit = cmDeclarationItems.getUnit();
					String ODeclNo = cmDeclarationItems.getODeclNo();
					Long OItemNo = cmDeclarationItems.getOItemNo();
					
					/*log.info("111unit++++++"  +unit);
					log.info("11111---" + brandCode);
					log.info("11111----" +imAdjustmentLine.getOriginalDeclarationNo());
					log.info("11111---" + imAdjustmentLine.getOriginalDeclarationSeq());
					log.info("11111---" + imAdjustmentLine.getCustomsItemCode());
					log.info("11111---" + imAdjustmentLine.getWarehouseCode());
					*/
					
					Double onHeandQty =  cmDeclarationOnHandDAO.getCurrentOnHandQtyByProperty
						(brandCode,imAdjustmentLine.getOriginalDeclarationNo(),imAdjustmentLine.getOriginalDeclarationSeq(),
						 imAdjustmentLine.getCustomsItemCode(),"FD");
					
					log.info("onHeandQty+++" + onHeandQty);
					
					if(null == onHeandQty){
						throw new Exception("請檢查報單號碼、報單項次及品號組合是否正確");
					}
					//log.info("ODeclNo++++++++" + ODeclNo);
					//log.info("OItemNo++++++++" + OItemNo);
					imAdjustmentLine.setIndexNo(i + 1L);
					imAdjustmentLine.setReserve2(ODeclNo);//原D8報關單號
					imAdjustmentLine.setReserve3(OItemNo.toString());//原D8報關單項次
					imAdjustmentLine.setExtensionDate(DateUtils.addDays(imAdjustmentLine.getExpiryDate() , 365));//展延日期
					imAdjustmentLine.setUnit(unit);//單位
					imAdjustmentLine.setReserve1(onHeandQty.toString().substring(0, onHeandQty.toString().indexOf(".")));//存貨數量
					
					log.info("SET-imMovementItemIndexNo:"+(i + 1L));
					items.add(imAdjustmentLine);
				}
				imAdjustmentHead.setImAdjustmentLines(items);
			} else {
				imAdjustmentHead.setImAdjustmentLines(new ArrayList(0));
			}
			imAdjustmentHeadDAO.update(imAdjustmentHead);
		} catch (NoSuchObjectException ns) {
			log.error("刪除報單展延明細失敗，原因：" + ns.toString());
			throw new FormException(ns.getMessage());
		} catch (Exception ex) {
			//ex.printStackTrace();
			log.error("報單展延匯入時發生錯誤，原因：" + ex.toString());
			throw new Exception("報單展延匯入時發生錯誤，" + ex.getMessage());
		}
	}
	
	/**
	 * 匯入報單展延明細(特殊展延單)
	 * 
	 * @param headId
	 * @param extentionDate
	 * @param dateType
	 * @param special
	 * @param extentionItems
	 * @throws Exception
	 */
	public void executeImportExtentionLine(Long headId,int extentionDate, String dateType, String special,  List extentionItems) throws Exception {
		log.info("這是特殊展延單");
		log.info("executeImportMovementItems.." + headId);
		log.info("executeImportMovementItems..extentionDate" + extentionDate);
		log.info("executeImportMovementItems..dateType" + dateType);
		log.info("executeImportMovementItems..size" + extentionItems.size());
	
		List<ImAdjustmentLine> items = new ArrayList(0);
		try {

			ImAdjustmentHead imAdjustmentHead = imAdjustmentHeadDAO.findById(headId);
			
			String brandCode = imAdjustmentHead.getBrandCode();
			
			if (imAdjustmentHead == null)
				throw new NoSuchObjectException("查無報單展延主鍵：" + headId + "的資料");
		
			if (extentionItems != null && extentionItems.size() > 0) {
				
				//取出第一筆資料的關別
				String customsWarehouseCode = ((ImAdjustmentLine) extentionItems.get(0)).getWarehouseCode();
				
				//透過常用字彙，查詢可展延庫別
				BuCommonPhraseLine buCommonPhraseLine = null;
				
				//存放可展延庫別之陣列
				List<String> customsWarehouseCodeList = new ArrayList();
				
				//檢核可展延關別
				if("Y".equals(special)){
					buCommonPhraseLine = buCommonPhraseLineDAO.findById("ImAdjustmentExtentionCustomsWarehouseCodeConfig","special");
					customsWarehouseCodeList = Arrays.asList((buCommonPhraseLine.getAttribute1()).split(","));
					if(!customsWarehouseCodeList.contains(customsWarehouseCode)){
						throw new Exception("特殊展延單無法對"+buCommonPhraseLine.getAttribute1()+"之外的關別進行展延");
					}
				}else if("N".equals(special)){
					buCommonPhraseLine = buCommonPhraseLineDAO.findById("ImAdjustmentExtentionCustomsWarehouseCodeConfig","general");
					customsWarehouseCodeList = Arrays.asList((buCommonPhraseLine.getAttribute1()).split(","));
					if(!customsWarehouseCodeList.contains(customsWarehouseCode)){
						throw new Exception("一般展延單僅可對"+buCommonPhraseLine.getAttribute1()+"關別進行展延");
					}
				}else{
					throw new Exception("special欄位異常不等於Y或N");
				}
				
				CmDeclarationOnHandView cmDeclarationOnHandView = null;
				
				for (int i = 0; i < extentionItems.size(); i++) {
					
					ImAdjustmentLine imAdjustmentLine = (ImAdjustmentLine) extentionItems.get(i);

					//以第一筆資料的關別對後面資料進行比對
					if(!customsWarehouseCode.equals(imAdjustmentLine.getWarehouseCode())){
						throw new Exception("匯入資料關別不一致，第1筆資料關別為" + customsWarehouseCode + "，第" + (i+1) + "筆資料關別為" + imAdjustmentLine.getWarehouseCode());
					}
					
					CmDeclarationItem cmDeclarationItems = cmDeclarationItemDAO.findOneCmDeclarationItem
						(imAdjustmentLine.getOriginalDeclarationNo() , imAdjustmentLine.getOriginalDeclarationSeq());
					
					String unit = cmDeclarationItems.getUnit();
					String ODeclNo = cmDeclarationItems.getODeclNo();
					Long OItemNo = cmDeclarationItems.getOItemNo();
					
					Double onHeandQty =  cmDeclarationOnHandDAO.getCurrentOnHandQtyByProperty
						(brandCode,imAdjustmentLine.getOriginalDeclarationNo(),imAdjustmentLine.getOriginalDeclarationSeq(),
						 imAdjustmentLine.getCustomsItemCode(),customsWarehouseCode);
					
					log.info("onHeandQty+++" + onHeandQty);
					
					if(null == onHeandQty){
						throw new Exception("請檢查報單號碼、報單項次及品號組合是否正確");
					}
					//log.info("ODeclNo++++++++" + ODeclNo);
					//log.info("OItemNo++++++++" + OItemNo);
					imAdjustmentLine.setIndexNo(i + 1L);
					imAdjustmentLine.setReserve2(ODeclNo);//原D8報關單號
					imAdjustmentLine.setReserve3(OItemNo.toString());//原D8報關單項次
					
					cmDeclarationOnHandView = cmDeclarationOnHandViewDAO.findByPk(imAdjustmentLine.getOriginalDeclarationNo(), imAdjustmentLine.getOriginalDeclarationSeq(), customsWarehouseCode);
					
					if(cmDeclarationOnHandView == null)
						throw new Exception("查無報單資料");
					
					imAdjustmentLine.setOrgImportDate(cmDeclarationOnHandView.getOriginalDate());
					imAdjustmentLine.setExpiryDate(cmDeclarationOnHandView.getExpiryDate());
					
					//特殊展延單
					if("day".equals(dateType)){
						imAdjustmentLine.setExtensionDate(DateUtils.addDays(cmDeclarationOnHandView.getExpiryDate() , extentionDate));//展延日期
					}else if("month".equals(dateType)){
						imAdjustmentLine.setExtensionDate(DateUtils.addMonths(cmDeclarationOnHandView.getExpiryDate() , extentionDate));//展延日期
					}else if("year".equals(dateType)){
						imAdjustmentLine.setExtensionDate(DateUtils.addMonths(cmDeclarationOnHandView.getExpiryDate() , extentionDate*12));//展延日期			
					}else{
						throw new Exception("請檢查展延時間、展延時間類別是否正確");
					}
					
					imAdjustmentLine.setUnit(unit);//單位
					imAdjustmentLine.setReserve1(onHeandQty.toString().substring(0, onHeandQty.toString().indexOf(".")));//存貨數量
					
					log.info("SET-imMovementItemIndexNo:"+(i + 1L));
					items.add(imAdjustmentLine);
				}
				imAdjustmentHead.setImAdjustmentLines(items);
				imAdjustmentHead.setCustomsWarehouseCode(customsWarehouseCode);
			} else {
				imAdjustmentHead.setImAdjustmentLines(new ArrayList(0));
			}
			imAdjustmentHeadDAO.update(imAdjustmentHead);
		} catch (NoSuchObjectException ns) {
			log.error("刪除報單展延明細失敗，原因：" + ns.toString());
			throw new FormException(ns.getMessage());
		} catch (Exception ex) {
			//ex.printStackTrace();
			log.error("報單展延匯入時發生錯誤，原因：" + ex.toString());
			throw new Exception("報單展延匯入時發生錯誤，" + ex.getMessage());
		}
	}
	
	
//	特殊展延,明細匯入後,改變時間回寫單身
//	public List<Properties> changeExtentionTime(Properties httpRequest) throws Exception {
//		log.info("changeExtentionTime");	
//		List<Properties> result = new ArrayList();
//		try {
//			
//			String headId             = httpRequest.getProperty("headId");
//			String extentionTime      = httpRequest.getProperty("extentionTime");
//			String extentionTimeType  = httpRequest.getProperty("extentionTimeType");
//			
//			log.info("headId = " + headId +";extentionTime = " + extentionTime + ";extentionTimeType = " +extentionTimeType);
//
//			
//			ImAdjustmentHead imAdjustmentHead = null;
//			List<ImAdjustmentLine> imAdjustmentLines = null;
//			
//			if("".equals(headId)){
//				throw new Exception("headId空白");
//			}else{
//				imAdjustmentHead = findById(Long.parseLong(headId));
//			}
//			
//			if(imAdjustmentHead == null){
//				throw new Exception("查無資料");
//			}
//		
//			imAdjustmentHead.setExtentionTime(Integer.parseInt(extentionTime));
//			imAdjustmentHead.setExtentionTimeType(extentionTimeType);
//			
//			imAdjustmentLines = imAdjustmentHead.getImAdjustmentLines();
//			
//			if(imAdjustmentLines != null && imAdjustmentLines.size() > 0){
//				log.info("明細有資料,將展延時間回寫屆期日,並存檔");
//				
//				Integer extentionDate = Integer.parseInt(extentionTime);
//				
//				for(ImAdjustmentLine imAdjustmentLine : imAdjustmentLines){
//					imAdjustmentLine.getExpiryDate();
//					
//					
//					if("day".equals(extentionTimeType)){
//						imAdjustmentLine.setExtensionDate(DateUtils.addDays(imAdjustmentLine.getExpiryDate() , extentionDate));//展延日期
//					}else if("month".equals(extentionTimeType)){
//						imAdjustmentLine.setExtensionDate(DateUtils.addMonths(imAdjustmentLine.getExpiryDate() , extentionDate));//展延日期
//					}else if("year".equals(extentionTimeType)){
//						imAdjustmentLine.setExtensionDate(DateUtils.addMonths(imAdjustmentLine.getExpiryDate() , extentionDate*12));//展延日期			
//					}else{
//						throw new Exception("請檢查展延時間、展延時間類別是否正確");
//					}
//				}
//				
//				
//				imAdjustmentHeadDAO.update(imAdjustmentHead);
//				
//			}else{
//				log.info("明細無資料,不回寫,不存檔");
//			}
//			
//			
//			
//			
//			return result;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			log.error("展延時間回寫入明細發生錯誤，原因：" + ex.toString());
//			throw new Exception("展延時間回寫入明細發生錯誤，原因：" + ex.getMessage());
//		}
//	}
	
	private String getStatus(String formAction,String beforeStatus) {
		
		log.info("取得下個狀態");
		
		String status = null;
		//BuEmployee bump = buEmployeeDAO.findById(loginEmployeeCode);
		
		//log.info("登入工號:"+loginEmployeeCode);
		//log.info("是否為主管:"+bump.getIsDepartmentManager());
		log.info("送出前狀態:"+beforeStatus);
		//若動作為送出
		if(OrderStatus.FORM_SUBMIT.equals(formAction)){
			//若送出前狀態為暫存或駁回
			if(beforeStatus.equals(OrderStatus.SAVE)||beforeStatus.equals(OrderStatus.REJECT)){
			
				status = OrderStatus.FINISH;
			}
		}
		//動作為暫存
		else if(OrderStatus.FORM_SAVE.equals(formAction)){
			status = OrderStatus.SAVE;
		}
		//動作為作廢
		else if(OrderStatus.FORM_VOID.equals(formAction)){
			status = OrderStatus.VOID;
		}
		log.info("送出後狀態:"+status);
		return status;
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
				if ("unknow".equals(serialNo)) 
					throw new ObtainSerialNoFailedException("取得" + head.getBrandCode() + "-" + head.getOrderTypeCode() + "單號失敗！");
				else
					head.setOrderNo(serialNo);
			} catch (Exception ex) {
					throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode() + "單號失敗！");
			}
		}
	}
	
	/**
     * 查詢報單展延初始化
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeExtentionSearchInitial(Map parameterMap) throws Exception{
    	Map resultMap = new HashMap(0);
    	
    	try{
    		Object otherBean = parameterMap.get("vatBeanOther");
    		String isSpecial = (String)PropertyUtils.getProperty(otherBean, "isSpecial");
    		resultMap.put("isSpecial", isSpecial);
    		resultMap.put("orderTypeCode",  "ACE");
    		return resultMap;
		}catch(Exception ex){
			log.error("查詢調整單初始化失敗，原因：" + ex.toString());
			throw new Exception("查詢調整單初始化失敗，原因：" + ex.toString());

		}
    }
	

    /**
     * ajax 取得報單展延search分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{
    		log.info("getAJAXSearchPageData");
    	try{
    		List<Properties> result = new ArrayList();
    		List<Properties> gridDatas = new ArrayList();
    		int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
    		int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

    		//======================帶入Head的值=========================

    		String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
    		String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 品牌代號
    		String orderNo = httpRequest.getProperty("orderNo" );
    		String status = httpRequest.getProperty("status");
    		String fileNo = httpRequest.getProperty("fileNo");
    		String isSpecial = httpRequest.getProperty("isSpecial");
    		Date createDateStart = DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("createDateStart") );
    		Date createDateEnd = DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("createDateEnd") );
    		  		
    		HashMap map = new HashMap();
//    		HashMap findObjs = new HashMap();
//    		findObjs.put(" and model.brandCode = :brandCode",brandCode);
//    		findObjs.put(" and model.orderTypeCode = :orderTypeCode",orderTypeCode);
//    		findObjs.put(" and model.orderNo NOT LIKE :TMP","TMP%");
//    		findObjs.put(" and model.orderNo = :orderNo",orderNo);
//    		findObjs.put(" and model.status = :status",status);
//    		findObjs.put(" and model.fileNo = :fileNo",fileNo);
//    		//isSpecial,有三種值,Y N 空白,Y為特殊,N及空白為一般
//    		if("Y".equals(isSpecial)){
//    			findObjs.put(" and model.isSpecial = :isSpecial",isSpecial);
//    		}else{
//    			//findObjs.put(" and ( model.isSpecial <> :isSpecial or  model.isSpecial is null)", "Y");
//    			findObjs.put(" and (model.isSpecial is null or model.isSpecial = :isSpecial)", isSpecial+")");
//    			//findObjs.put(" or model.isSpecial :xxx)", "is null");
//    		}    		
//    		findObjs.put(" and model.creationDate >= :createDateStart",createDateStart);
//    		findObjs.put(" and model.creationDate <= :createDateEnd",createDateEnd);
    		
    		HashMap fos = new HashMap();
    		fos.put("brandCode", brandCode);
    		fos.put("orderTypeCode", orderTypeCode);
    		fos.put("orderNo", orderNo);
    		fos.put("status", status);
    		fos.put("fileNo", fileNo);
    		fos.put("isSpecial", isSpecial);
    		fos.put("createDateStart", createDateStart);
    		fos.put("createDateEnd", createDateEnd);
    		
    		        
    		//==============================================================

//    		Map imAdjustmentHeadMap = imAdjustmentHeadDAO.search( "ImAdjustmentHead as model", findObjs, "order by orderNo Desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
//    		List<ImAdjustmentHead> imAdjustmentHeads = (List<ImAdjustmentHead>) imAdjustmentHeadMap.get(BaseDAO.TABLE_LIST);
//    		
//    		if (imAdjustmentHeads != null && imAdjustmentHeads.size() > 0) {
//    			this.setImAdjustmentStatusName(imAdjustmentHeads);
//    			Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
//    			Long maxIndex = (Long)imAdjustmentHeadDAO.search("ImAdjustmentHead as model", "count(model.headId) as rowCount" ,findObjs, "order by lastUpdateDate desc", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
//    			
//    			result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, imAdjustmentHeads, gridDatas, firstIndex, maxIndex));
//    		}else {
//    			result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,  map,gridDatas));
//    		}
    		
    		List<ImAdjustmentHead> imAdjustmentHeads = (List<ImAdjustmentHead>) imAdjustmentHeadDAO.findExtension(fos, iSPage, iPSize, imAdjustmentHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");
    		if (imAdjustmentHeads != null && imAdjustmentHeads.size() > 0) {
    			this.setImAdjustmentStatusName(imAdjustmentHeads);
    			Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;
    			Long maxIndex = (Long) imAdjustmentHeadDAO.findExtension(fos, -1, iPSize,
    					imAdjustmentHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount");
    				
    			result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, imAdjustmentHeads, gridDatas, firstIndex, maxIndex));
    		}else{
    			result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,  map,gridDatas));
    		}
    		
    		return result;
    	}catch(Exception ex){
    		log.error("載入頁面顯示的報單展延查詢發生錯誤，原因：" + ex.toString());
    		throw new Exception("載入頁面顯示的報單展延查詢失敗！");
    	}
    }
    
    /**
     * 將報單展延主檔查詢結果存檔
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
    
    public ImAdjustmentHead findImAdjustmentHead(Map argumentMap,
			Map resultMap) throws FormException, Exception {
		try {
			Long formId = (Long) argumentMap.get("formId");
			
			ImAdjustmentHead form = findById(formId);
			if (form != null) {
				resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			//	resultMap.put("createByName",   UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
				resultMap.put("form", form);
				
				return form;
			} else {
				throw new NoSuchObjectException("查無報單展延主鍵：" + formId + "的資料！");
			}
		} catch (FormException fe) {
			log.error("查詢報單展延失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("查詢報單展延發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢報關單發生錯誤！");
		}
	}
    
    /**
     * 設定中文狀態名稱
     * @param imAdjustmentHeads
     */
    private void setImAdjustmentStatusName(List<ImAdjustmentHead> imAdjustmentHeads){
		for (ImAdjustmentHead imAdjustmentHead : imAdjustmentHeads) {
		    imAdjustmentHead.setStatusName(OrderStatus.getChineseWord(imAdjustmentHead.getStatus()));
		}
    }
    
    public static Object[] completeExtenionAssignment(long assignmentId, boolean approveResult, String formAction) throws ProcessFailedException{

    	try{
    	    HashMap context = new HashMap();
    	    context.put("approveResult", approveResult);
    	    context.put("formAction", formAction);

    	    return ProcessHandling.completeAssignment(assignmentId, context);
    	}catch (Exception ex){
    	    log.error("完成報單展延工作任務失敗，原因：" + ex.toString());
    	    throw new ProcessFailedException("完成報單展延工作任務失敗！"+ ex.getMessage());
    	}
        }
  
}
