package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationItemDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationLogDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemEanPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveItemDAO;
import tw.com.tm.erp.hbm.dao.ImTransationDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.CmDeclarationLog;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHand;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemEanPriceView;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class ImReceiveAdjustmentService {

	private static final Log log = LogFactory.getLog(ImReceiveAdjustmentService.class);
	public static final String PROGRAM_ID= "IM_RECEIVE_ADJUSTMENT";
	public static final String MORE = "0";	// 溢到
	public static final String LESS = "1";	// 短到
	public static final String ADD = "2";	// 新增

	public static final String O = "O";			// 短到
	public static final String N = "N";			// 溢到
	public static final String A = "A";			// 新增

	/**
	 * spring Ioc
	 */
	private BaseDAO baseDAO;

	private ImAdjustmentHeadDAO imAdjustmentHeadDAO;
	private ImAdjustmentLineDAO imAdjustmentLineDAO;
	private ImTransationDAO imTransationDAO;
	private CmDeclarationLogDAO cmDeclarationLogDAO;
	private CmDeclarationHeadDAO cmDeclarationHeadDAO;
	private CmDeclarationItemDAO cmDeclarationItemDAO;
	private ImReceiveHeadDAO imReceiveHeadDAO;
	private ImItemEanPriceViewDAO imItemEanPriceViewDAO;
	private ImItemPriceDAO imItemPriceDAO;
	private ImWarehouseDAO imWarehouseDAO;
	private ImItemDAO imItemDAO;
	private BuBrandDAO buBrandDAO;
	private BuOrderTypeDAO buOrderTypeDAO;
	private ImOnHandDAO imOnHandDAO;
	private CmDeclarationOnHandDAO cmDeclarationOnHandDAO;

	private BuOrderTypeService buOrderTypeService;
	private CmDeclarationLogService cmDeclarationLogService;

	private SiProgramLogAction siProgramLogAction;

	//for 儲位用
	private ImStorageAction				 imStorageAction;
	private ImStorageService			 imStorageService;

	public void setImItemEanPriceViewDAO(ImItemEanPriceViewDAO imItemEanPriceViewDAO) {
		this.imItemEanPriceViewDAO = imItemEanPriceViewDAO;
	}

	public void setImTransationDAO(ImTransationDAO imTransationDAO) {
		this.imTransationDAO = imTransationDAO;
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

	public void setCmDeclarationLogDAO(CmDeclarationLogDAO cmDeclarationLogDAO) {
		this.cmDeclarationLogDAO = cmDeclarationLogDAO;
	}

	public void setCmDeclarationHeadDAO(CmDeclarationHeadDAO cmDeclarationHeadDAO) {
		this.cmDeclarationHeadDAO = cmDeclarationHeadDAO;
	}

	public void setCmDeclarationItemDAO(CmDeclarationItemDAO cmDeclarationItemDAO) {
		this.cmDeclarationItemDAO = cmDeclarationItemDAO;
	}

	public void setImItemPriceDAO(ImItemPriceDAO imItemPriceDAO) {
		this.imItemPriceDAO = imItemPriceDAO;
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

	public void setCmDeclarationLogService(
			CmDeclarationLogService cmDeclarationLogService) {
		this.cmDeclarationLogService = cmDeclarationLogService;
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
	 * 進貨短溢調整單明細欄位
	 */
	public static final String[] GRID_FIELD_MOREORLESS_NAMES = {
		"indexNo", 		"moreOrLessType", 			"itemCode",
		"itemCName",		"lotNo", 				"localUnitCost",
		"amount",		"warehouseCode",			"difQuantity",
		"originalDeclarationNo", "originalDeclarationSeq", 		"boxNo",
		"weight",		"customsItemCode",
		"lineId", "isLockRecord", "isDeleteRecord", "message", "originalDeclarationDate"
	};

	public static final String[] GRID_FIELD_MOREORLESS_DEFAULT_VALUES = {
		"", 	"", "",
		"", 	"", "0.0",
		"0.0", 	"", "0",
		"",   	"", "",
		"0.0",	"",
		"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "",""
	};

	public static final int[] GRID_FIELD_MOREORLESS_TYPES = {
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE
	};

	/**
	 * 調整單查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = {
		"orderNo", "adjustmentDate", "sourceOrderNo",
		"declarationNo", "statusName", "lastUpdateDate",
		"orderTypeCode", "headId"
	};

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {
		"", "", "",
		"", "", "",
		"", ""
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
	private Set[] aggregateOrderItemsQty(ImAdjustmentHead imAdjustmentHead, String employeeCode, boolean isReverse)
	throws FormException, Exception{

		StringBuffer key = new StringBuffer();
		StringBuffer cmKey = new StringBuffer();
		HashMap imMap = new HashMap();
		HashMap cmMap = new HashMap();

		String brandCode = imAdjustmentHead.getBrandCode();
		String taxType = imAdjustmentHead.getTaxType();

		int adjustmentType = NumberUtils.getInt(imAdjustmentHead.getAdjustmentType());

		List<ImAdjustmentLine> imAdjustmentLines = imAdjustmentHead.getImAdjustmentLines();
		log.info( "imAdjustmentLines.size() = " + imAdjustmentLines.size() );
		for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
			imAdjustmentLine.setLastUpdateDate( new Date() );
			imAdjustmentLine.setLastUpdatedBy( employeeCode );

			Double quantity = imAdjustmentLine.getDifQuantity();
			String warehouseCode = imAdjustmentLine.getWarehouseCode();

			String declarationNo = imAdjustmentLine.getOriginalDeclarationNo(); // 單身的報單
			Long declarationSeq = imAdjustmentLine.getOriginalDeclarationSeq(); // 單身的報單項次

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

			log.info( "imAdjustmentLine.getDifQuantity = " + quantity );
			log.info( "imAdjustmentLine.getOriginalDeclarationNo = " + declarationNo );
			log.info( "imAdjustmentLine.getOriginalDeclarationSeq = " + declarationSeq );
			log.info( "imAdjustmentLine.getItemCode = " + customsItemCode );
			log.info( "imAdjustmentLine.getWarehouseCode = " + warehouseCode );

			// 保稅商品集合
			if("F".equals(taxType)){
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
				cmKey.append(customsWarehouseCode );
				if (cmMap.get(cmKey.toString()) == null) {
					cmMap.put(cmKey.toString(), quantity);
				} else {
					cmMap.put(cmKey.toString(), quantity + ((Double) cmMap.get(cmKey.toString())));
				}
			}

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

		return new Set[]{imMap.entrySet(), cmMap.entrySet()};
	}

	/**
	 * 檢核進貨短溢單主檔,明細檔
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public List checkedImReceiveMoreOrLessAdjustment(Map parameterMap)throws FormException, Exception{
		List errorMsgs = new ArrayList(0);
		String message = null;
		String identification = null;
		ImAdjustmentHead imAdjustmentHead = null;
		try{

			Object formLinkBean = parameterMap.get("vatBeanFormLink");

			imAdjustmentHead = this.getActualImAdjustment(formLinkBean);

			String status = imAdjustmentHead.getStatus();
			if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status) ) {

				identification = MessageStatus.getIdentification(imAdjustmentHead.getBrandCode(),
						imAdjustmentHead.getOrderTypeCode(), imAdjustmentHead.getOrderNo());

				siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

				validateImReceiveMoreOrLessAdjustment( imAdjustmentHead, PROGRAM_ID, identification, errorMsgs, formLinkBean );

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
					imAdjustmentLineDAO.delete(imAdjustmentLine);
				}
			}
		}
	}

	/**
	 * 進貨溢卸調整單初始化 bean 額外顯示欄位
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);

		try{
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
//			String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

			String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);

			ImAdjustmentHead imAdjustmentHead = this.getActualHead(otherBean, resultMap);

			Map multiList = new HashMap(0);
			resultMap.put("form", imAdjustmentHead);
			resultMap.put("statusName", OrderStatus.getChineseWord(imAdjustmentHead.getStatus()));
			resultMap.put("brandName",buBrandDAO.findById(imAdjustmentHead.getBrandCode()).getBrandName());
			resultMap.put("createByName", employeeName);
//			resultMap.putAll( this.getOtherColumn(cmMovementHead) );

			List<BuOrderType> allSourceOrderTypeCode = buOrderTypeDAO.findOrderbyType( imAdjustmentHead.getBrandCode(), "IMR", "F", "Y" );
			List<BuCommonPhraseLine> allTaxTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"TaxCatalog", "Y"}, "indexNo" );
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(imAdjustmentHead.getBrandCode() ,"AIR");
			List<BuCommonPhraseLine> allAdjustmentTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"AdjustmentType", "Y"}, "indexNo");
			List<BuCommonPhraseLine> allMoreOrLessTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"MoreOrLessType", "Y"}, "indexNo");

			String sourceOrderTypeCode = imAdjustmentHead.getSourceOrderTypeCode();
			String taxType = imAdjustmentHead.getTaxType();
			String orderType = imAdjustmentHead.getOrderTypeCode();
			String adjustmentType = imAdjustmentHead.getAdjustmentType();

			multiList.put("allSourceOrderTypeCode"	, AjaxUtils.produceSelectorData(allSourceOrderTypeCode, "orderTypeCode", "name", true, true, sourceOrderTypeCode != null ? sourceOrderTypeCode : "" ));
			multiList.put("allTaxTypes"	, AjaxUtils.produceSelectorData(allTaxTypes, "lineCode", "name", true, true, taxType != null ? taxType : "" ));
			multiList.put("allOrderTypes"	, AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, true, orderType != null ? orderType : "" ));
			multiList.put("allAdjustmentTypes"	, AjaxUtils.produceSelectorData(allAdjustmentTypes, "lineCode", "name", true, true, adjustmentType != null ? adjustmentType : "" ));
			multiList.put("allMoreOrLessTypes"	, AjaxUtils.produceSelectorData(allMoreOrLessTypes, "lineCode", "name", false, false));

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
				resultMap.put("arrivalWarehouse", "#F.defaultWarehouseCode");
			}

			return resultMap;
		}catch(Exception ex){
			log.error("進貨短溢調整單初始化失敗，原因：" + ex.toString());
			throw new Exception("進貨短溢調整單初始化失敗，原因：" + ex.toString());

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

			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
//			String adjustmentType = (String)PropertyUtils.getProperty(otherBean, "adjustmentType");

			List<BuCommonPhraseLine> allAdjustmentTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"AdjustmentType", "Y"}, "indexNo");
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(loginBrandCode ,"AIR");

			Map multiList = new HashMap(0);
			multiList.put("allAdjustmentTypes"	, AjaxUtils.produceSelectorData(allAdjustmentTypes, "lineCode", "name", true, true));
			multiList.put("allOrderTypeCodes"	, AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, true));

			resultMap.put("orderTypeCode",orderTypeCode);
			resultMap.put("multiList",multiList);
			return resultMap;
		}catch(Exception ex){
			log.error("查詢進貨短溢調整單初始化失敗，原因：" + ex.toString());
			throw new Exception("查詢進貨短溢調整單初始化失敗，原因：" + ex.toString());

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
			head.setAffectCost("Y"); // 影響成本
			head.setBoxQty(0L);
			this.setOtherColumn(head);

			head.setCreatedBy(loginEmployeeCode);
			head.setCreationDate(new Date());
			head.setLastUpdatedBy(loginEmployeeCode);
			head.setLastUpdateDate(new Date());

			this.saveTmpHead(head);

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
	 * 撈符合的進貨單(倉管驗收即可)
	 * @param brandCode
	 * @param orderTypeCode
	 * @param taxType
	 * @param sourceOrderNo
	 * @return
	 */
	public ImReceiveHead findOneImReceive(String brandCode, String sourceOrderTypeCode, String sourceOrderNo, String status){
		return (ImReceiveHead)imReceiveHeadDAO.findFirstByProperty(
				"ImReceiveHead","and brandCode = ? and orderTypeCode = ? and orderNo = ? and warehouseStatus = ? ",  // and taxType = ?	!!! 確認撈進貨單
				new Object[] { brandCode, sourceOrderTypeCode, sourceOrderNo, OrderStatus.FINISH });  // taxType,

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
	 * 撈一筆進貨單明細商品	!!! encode? 去撈 isTax 保稅/完稅
	 * @param brandCode
	 * @param orderTypeCode
	 * @param taxType
	 * @param sourceOrderNo
	 * @return
	 */
	private ImItem findOneImItem(String brandCode, String itemCode, String taxType ){
		return (ImItem)imItemDAO.findFirstByProperty("ImItem", "and brandCode = ? and itemCode = ? and isTax = ? ", new Object[] { brandCode, itemCode , taxType });
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
			String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
			Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;

			imAdjustmentHead = null == formId ? this.executeNew(otherBean) : this.executeFind(formId);

		} catch (Exception e) {
			log.error("取得實際調整單主檔失敗,原因:"+e.toString());
			throw new Exception("取得實際調整單主檔失敗,原因:"+e.toString());
		}
		return imAdjustmentHead;
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
	 * ajax 第一次載入進貨短溢調整單明細時,取得分頁
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXMoreOrLessPageData(Properties httpRequest) throws Exception{
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();

			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			String brandCode = httpRequest.getProperty("brandCode");
			String taxType = httpRequest.getProperty("taxType");

			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			HashMap findObjs = new HashMap();
			findObjs.put("and model.imAdjustmentHead.headId = :headId", headId);

			Map searchMap = baseDAO.search("ImAdjustmentLine as model", findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
			List<ImAdjustmentLine> imAdjustmentLines = (List<ImAdjustmentLine>)searchMap.get(BaseDAO.TABLE_LIST);

			HashMap map = new HashMap();
			map.put("headId", headId);

			if (imAdjustmentLines != null && imAdjustmentLines.size() > 0) {

				// 取得第一筆的INDEX
				Long firstIndex = imAdjustmentLines.get(0).getIndexNo();
				// 取得最後一筆 INDEX
				Long maxIndex = (Long)baseDAO.search("ImAdjustmentLine as model", "count(model.imAdjustmentHead.headId) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT);
				result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_MOREORLESS_NAMES, GRID_FIELD_MOREORLESS_DEFAULT_VALUES,imAdjustmentLines, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_MOREORLESS_NAMES, GRID_FIELD_MOREORLESS_DEFAULT_VALUES, map,gridDatas));
			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的進貨短溢調整單明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的進貨短溢調整單明細失敗！");
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
		try {
			String brandCode = httpRequest.getProperty("brandCode");
			String taxType = httpRequest.getProperty("taxType");
			String defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode");
			String sourceOrderNo = httpRequest.getProperty("sourceOrderNo");
			String declarationNo = httpRequest.getProperty("declarationNo");
			String itemCode = httpRequest.getProperty("itemCode").trim().toUpperCase();

			if( StringUtils.hasText(itemCode)){
				ImItemEanPriceView imItemEanPriceView = imItemEanPriceViewDAO.getItemInfoByProperty(brandCode, itemCode, taxType);

				if(null != imItemEanPriceView){
					CmDeclarationHead cmDeclarationHead = cmDeclarationHeadDAO.findOneCmDeclaration(declarationNo);
					if( cmDeclarationHead != null ){
						List<CmDeclarationItem> cmDeclarationItems = cmDeclarationHead.getCmDeclarationItems();
						String itemNo = String.valueOf( cmDeclarationItems.get( cmDeclarationItems.size() - 1 ).getItemNo() + 1 );


						properties.setProperty("moreOrLessType", ADD);
						properties.setProperty("itemCode", imItemEanPriceView.getItemCode());
						properties.setProperty("itemCName", imItemEanPriceView.getItemCName());
						properties.setProperty("lotNo", SystemConfig.LOT_NO);
						properties.setProperty("localUnitCost", String.valueOf(imItemEanPriceView.getUnitPrice()));   	// !!! 售價
						properties.setProperty("amount", "0.0"); 														// !!! 總成本
						properties.setProperty("warehouseCode", defaultWarehouseCode);
						properties.setProperty("difQuantity", "0");
						properties.setProperty("originalDeclarationNo", declarationNo );
						properties.setProperty("originalDeclarationSeq", itemNo ); // 撈報關單最後一筆 +1,有可能重複問題
						properties.setProperty("originalDeclarationDate", String.valueOf(cmDeclarationHead.getImportDate()) ); // 撈單頭報單importDate
						properties.setProperty("boxNo", "" ); // !!!
						properties.setProperty("weight", "" ); // !!!
						properties.setProperty("customsItemCode", imItemEanPriceView.getItemCode() );

					}else{
						throw new Exception("查無此報關單");
					}
				}else{
					properties.setProperty("moreOrLessType", ADD);
					properties.setProperty("itemCode", itemCode);
					properties.setProperty("itemCName", "查無此商品");
					properties.setProperty("lotNo", SystemConfig.LOT_NO);
					properties.setProperty("localUnitCost", "0.0");
					properties.setProperty("amount", "0.0");
					properties.setProperty("warehouseCode", "");
					properties.setProperty("difQuantity", "0");
					properties.setProperty("originalDeclarationNo", "");
					properties.setProperty("originalDeclarationSeq", "");
					properties.setProperty("originalDeclarationDate", "" );
					properties.setProperty("boxNo", "");
					properties.setProperty("weight", "0.0");
					properties.setProperty("customsItemCode", "" );
				}

			}else{
				properties.setProperty("moreOrLessType", ADD);
				properties.setProperty("itemCode", itemCode);
				properties.setProperty("itemCName", "");
				properties.setProperty("localUnitCost", "0.0");
				properties.setProperty("amount", "0.0");
				properties.setProperty("warehouseCode", "");
				properties.setProperty("difQuantity", "0");
				properties.setProperty("originalDeclarationNo", "");
				properties.setProperty("originalDeclarationSeq", "");
				properties.setProperty("originalDeclarationDate", "" );
				properties.setProperty("boxNo", "");
				properties.setProperty("weight", "0.0");
				properties.setProperty("customsItemCode", "" );
			}

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

			Map returnMap = new HashMap(0);
			//CC後面要代的參數使用parameters傳遞
			Map parameters = new HashMap(0);
			parameters.put("prompt0", brandCode);
			parameters.put("prompt1", orderTypeCode);
			parameters.put("prompt2", "");
			parameters.put("prompt3", "");
			parameters.put("prompt4", orderNo);
			parameters.put("prompt5", orderNo);
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
	 * 將進貨單明細欄位塞入調整單明細
	 * @param imAdjustmentHead
	 * @param imReceiveHead
	 */
	private void saveImReceiveLineCopyImReceiveItem(ImAdjustmentHead imAdjustmentHead, ImReceiveHead imReceiveHead)throws Exception{
		List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
		Long i = 0L;
		String employeeCode = imAdjustmentHead.getLastUpdatedBy();
		Date date = new Date();

		String taxType = imAdjustmentHead.getTaxType();
		String brandCode = imAdjustmentHead.getBrandCode();

		Double exchangeRate = imReceiveHead.getExchangeRate();

		for (ImReceiveItem imReceiveItem : imReceiveItems) {
			Double shortQuantity = imReceiveItem.getShortQuantity(); // 短到數量
			if( shortQuantity != 0 ){
				i++;
				ImAdjustmentLine imAdjustmentLine = new ImAdjustmentLine();

				Double foreignUnitPrice = imReceiveItem.getForeignUnitPrice(); // 原幣售價 = 實際為成本

				String itemCode = imReceiveItem.getItemCode();

				ImItemEanPriceView imItemEanPriceView = imItemEanPriceViewDAO.getItemInfoByProperty(brandCode, itemCode, taxType);
				if(null == imItemEanPriceView){
					throw new Exception( "查品牌:"+brandCode+ " 品號:"+ itemCode + " 無資料");
				}
				imAdjustmentLine.setIndexNo(i);
				imAdjustmentLine.setImAdjustmentHead( new ImAdjustmentHead(imAdjustmentHead.getHeadId()));
				imAdjustmentLine.setItemCode( itemCode ); 	// 品號
				imAdjustmentLine.setLotNo( imReceiveItem.getLotNo() != null ? imReceiveItem.getLotNo() : SystemConfig.LOT_NO); 	// 批號

				imAdjustmentLine.setItemCName( imReceiveItem.getItemCName() );	// 品名
				imAdjustmentLine.setLocalUnitCost( imItemEanPriceView.getUnitPrice() );			// 售價		!!!
				imAdjustmentLine.setAmount( foreignUnitPrice * exchangeRate * shortQuantity * -1 );	// 總成本		總成本計算方式 = 成本(來源進貨單明細的原幣售價) * 來源進貨單頭的匯率 * 短溢數量 * -1,若是新增品項, 預設0使用者會自行修正
				imAdjustmentLine.setWarehouseCode( imAdjustmentHead.getDefaultWarehouseCode() ); // 預設塞品牌預設第4個倉庫(預設調整倉)
				imAdjustmentLine.setDifQuantity( shortQuantity * -1 );				// 數量 * -1
				imAdjustmentLine.setOriginalDeclarationNo( imReceiveHead.getDeclarationNo() );		// 原報單單號(進貨單頭報關單號)
				imAdjustmentLine.setOriginalDeclarationSeq( imReceiveItem.getIndexNo() ); 	// 原報單項次(進貨單身indexNo)
				imAdjustmentLine.setOriginalDeclarationDate( imReceiveHead.getImportDate() ); 	// 原報單日期(進貨單頭報關日期) 20101008改成抓importDate
				// 箱號	!!!
				// 重量	!!!
				imAdjustmentLine.setCustomsItemCode( itemCode );				// 海關料號
				if((shortQuantity * -1) > 0 ){
					imAdjustmentLine.setMoreOrLessType(MORE); 				// 溢到
				}else{
					imAdjustmentLine.setMoreOrLessType(LESS); 				// 短到
				}
				imAdjustmentLine.setCreatedBy(employeeCode);
				imAdjustmentLine.setCreationDate(date);

				imAdjustmentLineDAO.save(imAdjustmentLine);
			}
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
	 * 設定調撥單主檔其他欄位
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
					if( "AIF".equals(orderTypeCode) ){
						head.setAdjustmentType("71");
					}
				}else {
					throw new Exception("請確認 BuOrderType table 有無此:" + orderTypeCode + "與" + brandCode);
				}

				BuBrand buBrand = buBrandDAO.findById(brandCode);
				if(buBrand != null){
					head.setDefaultWarehouseCode( buBrand.getDefaultWarehouseCode4() == null ? "": buBrand.getDefaultWarehouseCode4() );
				}
			}

		}catch(Exception ex){
			log.error("設定調撥單主檔其他欄位時發生錯誤，原因：" + ex.toString());
			throw new Exception("設定調撥單主檔其他欄位時發生錯誤，原因：" + ex.toString());

		}
	}

	/**
	 * 進貨溢卸依formAction取得下個狀態
	 */
	public void setNextStatus(ImAdjustmentHead head, String formAction, String approvalResult){

		if(OrderStatus.FORM_SAVE.equals(formAction)){
			head.setStatus(OrderStatus.SAVE);
		}else if( (OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction)) ){

			if(OrderStatus.SAVE.equals( head.getStatus() ) || OrderStatus.REJECT.equals( head.getStatus() ) ){
				head.setStatus(OrderStatus.SIGNING);
			} else if(OrderStatus.SIGNING.equals( head.getStatus() )){
				if("true".equals(approvalResult)){
					head.setStatus(OrderStatus.FINISH);
				}else{
					head.setStatus(OrderStatus.REJECT);
				}
			}

		}else if(OrderStatus.FORM_VOID.equals(formAction)){
			head.setStatus(OrderStatus.VOID);
		}

	}

	/**
	 * 進貨溢謝調整單取得下個狀態
	 */
	public void setReverseNextStatus(ImAdjustmentHead head){

		if( OrderStatus.CLOSE.equals(head.getStatus()) ){
			head.setStatus(OrderStatus.SAVE);
		}else if(OrderStatus.FINISH.equals(head.getStatus()) ){
			head.setStatus(OrderStatus.SAVE);
		}
	}

	/**
	 * 若是暫存單號,則取得新單號
	 * @param head
	 */
	private void setImReceiveAdjustment(ImAdjustmentHead head)throws ObtainSerialNoFailedException{
		String orderNo = head.getOrderNo();
		if (AjaxUtils.isTmpOrderNo(orderNo)) {
			try {
				String serialNo = buOrderTypeService.getOrderSerialNo(head.getBrandCode(), head.getOrderTypeCode());
				if ("unknow".equals(serialNo))
					throw new ObtainSerialNoFailedException("取得" + head.getBrandCode() + "-" + head.getOrderTypeCode() + "單號失敗！");
				else{
					
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
		}
	}

	/**
	 * 啟動流程
	 * @param form
	 * @return
	 * @throws ProcessFailedException
	 */
	public static Object[] startProcess(ImAdjustmentHead form) throws ProcessFailedException{

		try{
			String packageId = "Im_ReceiveAdjustment";
			String processId = "process";
			String version = "20091023";
			String sourceReferenceType = "ImReceiveAdj";
			HashMap context = new HashMap();
			context.put("formId", form.getHeadId());
			return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		}catch (Exception ex){
			ex.printStackTrace();
			log.error("進貨溢卸調整單流程啟動失敗，原因：" + ex.toString());
			throw new ProcessFailedException("進貨溢卸調整單流程啟動失敗！"+ ex.getMessage());
		}
	}

	/**
	 * ajax 取得進貨單主檔與明細檔欄位並更新
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> updateAJAXByImReceiveData(Properties httpRequest) throws Exception{
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try {
			Long headId = NumberUtils.getLong( httpRequest.getProperty("headId") );
			String brandCode = httpRequest.getProperty("brandCode");
			String taxType = httpRequest.getProperty("taxType");
			String sourceOrderTypeCode = httpRequest.getProperty("sourceOrderTypeCode");
			String sourceOrderNo = httpRequest.getProperty("sourceOrderNo").trim().toUpperCase(); //來源進貨單號

			log.info( "取得調整單主檔與明細檔欄位");
			log.info( "headId = " + headId );
			log.info( "brandCode = " + brandCode );
			log.info( "taxType = " + taxType );
			log.info( "sourceOrderTypeCode = " + sourceOrderTypeCode );
			log.info( "sourceOrderNo = " + sourceOrderNo );

			// 撈調整單
			ImAdjustmentHead imAdjustmentHead = this.executeFind(headId);

			// 刪除調整單明細
			this.deleteImAdjustmentLine( imAdjustmentHead );

			// 撈進貨單
			ImReceiveHead imReceiveHead = this.findOneImReceive(brandCode, sourceOrderTypeCode, sourceOrderNo, imAdjustmentHead.getStatus());
			// 倉庫驗收完送出到船務 !!!
			if( StringUtils.hasText(sourceOrderNo) ){

				if(imReceiveHead != null){
					// 將進貨單明細塞入調整單明細
					this.saveImReceiveLineCopyImReceiveItem(imAdjustmentHead, imReceiveHead);

					properties.setProperty("sourceOrderNo", sourceOrderNo);
					properties.setProperty("sourceOrderNoMemo", "");
					properties.setProperty("declarationDate", AjaxUtils.getPropertiesValue(DateUtils.format( imReceiveHead.getDeclarationDate(), "yyyy/MM/dd"),""));
					properties.setProperty("declarationNo", AjaxUtils.getPropertiesValue(imReceiveHead.getDeclarationNo(),""));
					properties.setProperty("declarationType", AjaxUtils.getPropertiesValue(imReceiveHead.getDeclarationType(),""));
//					properties.setProperty("defaultWarehouseCode", AjaxUtils.getPropertiesValue(imReceiveHead.getDefaultWarehouseCode(), ""));

				} else {
					properties.setProperty("sourceOrderNo", sourceOrderNo);
					properties.setProperty("sourceOrderNoMemo", "查無此進貨單");
					properties.setProperty("declarationDate", "");
					properties.setProperty("declarationNo", "");
					properties.setProperty("declarationType", "");
//					properties.setProperty("defaultWarehouseCode", "");
				}
			}else{
				properties.setProperty("sourceOrderNo", sourceOrderNo);
				properties.setProperty("sourceOrderNoMemo", "");
				properties.setProperty("declarationDate", "");
				properties.setProperty("declarationNo", "");
				properties.setProperty("declarationType", "");
//				properties.setProperty("defaultWarehouseCode", "");
			}
			result.add(properties);
			return result;

		} catch (Exception ex) {
			log.error("取得進貨單資料發生錯誤，原因：" + ex.toString());
			throw new Exception("取得進貨單資料失敗！");
		}

	}

	/**
	 * ajax  更新進貨短溢調整單明細
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> updateAJAXMoreOrLessPageLinesData(Properties httpRequest) throws Exception{
		String errorMsg = null;
		try {
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
			String status = httpRequest.getProperty("status");

			if (headId == null) {
				throw new ValidationErrorException("傳入的進貨短溢調整單主鍵為空值！");
			}

			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_MOREORLESS_NAMES);
			// Get INDEX NO 取得最後一筆 INDEX
			int indexNo = imAdjustmentLineDAO.findPageLineMaxIndex(headId).intValue();

//			List<ImAdjustmentLine> result = baseDAO.findByProperty("ImAdjustmentLine", "headId", headId, "indexNo");
//			int indexNo = result.get( result.size() - 1 ).getIndexNo().intValue();
			log.info( "MaxIndexNo = " + indexNo );

			if(OrderStatus.SAVE.equals(status)||OrderStatus.REJECT.equals(status)){
				// 考慮狀態
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						// 先載入HEAD_ID OR LINE DATA

						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));

						String itemCode = upRecord.getProperty(GRID_FIELD_MOREORLESS_NAMES[2]);

						if (StringUtils.hasText(itemCode)) {

							ImAdjustmentLine imAdjustmentLine = imAdjustmentLineDAO.findFirstByProperty("ImAdjustmentLine", "and imAdjustmentHead.headId = ? and lineId = ?", new Object[]{ headId, lineId } );
							log.info( "imAdjustmentLine = " + imAdjustmentLine + "\nlineId = " + lineId);
							Date date = new Date();
							if ( imAdjustmentLine != null ) {
								log.info( "更新 = " + headId + " | "+ lineId  );
								double quantityBefore = imAdjustmentLine.getDifQuantity().doubleValue();
								double quantityAfter = NumberUtils.getDouble(upRecord.getProperty(GRID_FIELD_MOREORLESS_NAMES[8])).doubleValue();

								long originalDeclarationSeqBefore = imAdjustmentLine.getOriginalDeclarationSeq().longValue();
								long originalDeclarationSeqAfter = NumberUtils.getLong(upRecord.getProperty(GRID_FIELD_MOREORLESS_NAMES[10])).longValue();
								String moreOrLessType = imAdjustmentLine.getMoreOrLessType();

								log.info("quantityBefore = " + quantityBefore);
								log.info("quantityAfter = " + quantityAfter);
								log.info("originalDeclarationSeqBefore = " + originalDeclarationSeqBefore);
								log.info("originalDeclarationSeqAfter = " + originalDeclarationSeqAfter);

								log.info("imAdjustmentLine.getMoreOrLessType() = " + imAdjustmentLine.getMoreOrLessType());

								if( MORE.equals(moreOrLessType) || LESS.equals(moreOrLessType)){
									log.info("setMessage = setMessage");
									if( quantityBefore != quantityAfter ){
										if(MORE.equals(moreOrLessType)){
											// 修改後的溢到不可小於0
											if( quantityAfter < 0 ){
												imAdjustmentLine.setMessage("溢到修正數量不可小於0("+quantityBefore+", "+ quantityAfter +" )");
											}else{
												AjaxUtils.setPojoProperties(imAdjustmentLine,upRecord, GRID_FIELD_MOREORLESS_NAMES, GRID_FIELD_MOREORLESS_TYPES);
												imAdjustmentLine.setMessage("");
												imAdjustmentLine.setLastUpdatedBy(loginEmployeeCode);
												imAdjustmentLine.setLastUpdateDate(date);
												imAdjustmentLineDAO.update(imAdjustmentLine);
											}
										}else if(LESS.equals(moreOrLessType)){  // < 0  短到不可修改數量
											/*if( quantityBefore != quantityAfter ){
												imAdjustmentLine.setMessage("短到不得修正數量("+quantityBefore+", "+ quantityAfter +" )");
											}*/
											AjaxUtils.setPojoProperties(imAdjustmentLine,upRecord, GRID_FIELD_MOREORLESS_NAMES, GRID_FIELD_MOREORLESS_TYPES);
											imAdjustmentLine.setMessage("");
											imAdjustmentLine.setLastUpdatedBy(loginEmployeeCode);
											imAdjustmentLine.setLastUpdateDate(date);
											imAdjustmentLineDAO.update(imAdjustmentLine);
										}

									}else if(originalDeclarationSeqBefore != originalDeclarationSeqAfter ){
										imAdjustmentLine.setMessage("短溢到不得修正項次("+originalDeclarationSeqBefore+", "+ originalDeclarationSeqAfter +" )");
									}else{
										AjaxUtils.setPojoProperties(imAdjustmentLine,upRecord, GRID_FIELD_MOREORLESS_NAMES, GRID_FIELD_MOREORLESS_TYPES);
										imAdjustmentLine.setMessage("");
										imAdjustmentLine.setLastUpdatedBy(loginEmployeeCode);
										imAdjustmentLine.setLastUpdateDate(date);
										imAdjustmentLineDAO.update(imAdjustmentLine);
									}
								}else if(ADD.equalsIgnoreCase(imAdjustmentLine.getMoreOrLessType())){
									AjaxUtils.setPojoProperties(imAdjustmentLine,upRecord, GRID_FIELD_MOREORLESS_NAMES, GRID_FIELD_MOREORLESS_TYPES);
									imAdjustmentLine.setMessage("");
									imAdjustmentLine.setLastUpdatedBy(loginEmployeeCode);
									imAdjustmentLine.setLastUpdateDate(date);
									imAdjustmentLineDAO.update(imAdjustmentLine);
								}
							} else {
								indexNo++;
								log.info( "新增 = " + headId + " | "+  indexNo);
								imAdjustmentLine = new ImAdjustmentLine();

								AjaxUtils.setPojoProperties(imAdjustmentLine,upRecord, GRID_FIELD_MOREORLESS_NAMES,GRID_FIELD_MOREORLESS_TYPES);
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
			log.error("更新進貨短溢調整單明細資料發生錯誤，原因：" + ex.toString());
			throw new Exception("更新進貨短溢調整單明細資料失敗！");
		}
	}

	/**
	 * 檢核,存取進貨短溢調整單主檔,明細檔,重新設定狀態
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateImReceiveMoreOrLessAdjustment(Map parameterMap, String formAction)throws Exception{
		Map resultMap = new HashMap();
		List errorMsgs = null;
		String resultMsg = null;
		try {

			Object otherBean = parameterMap.get("vatBeanOther");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");

			String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");

			ImAdjustmentHead head = this.getActualImAdjustment(formLinkBean);

			String identification = MessageStatus.getIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());

			String beforeStatus = head.getStatus();
			
			// 檢核
			if( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ){
				this.deleteLine(head);
				errorMsgs  = this.checkedImReceiveMoreOrLessAdjustment(parameterMap);
			}

			if( errorMsgs == null || errorMsgs.size() == 0 ){
				// 設定單號
				this.setImReceiveAdjustment(head);

				// 成功則設定下個狀態
				this.setNextStatus(head, formAction, approvalResult);

				// 更新進貨短溢調整單主檔明細檔,im和cm庫存
				if( ( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ) ){

					// 更新調整單主檔明細檔
					this.updateImAdjustment(head, employeeCode );

					if( OrderStatus.FINISH.equals(head.getStatus())){	// 簽核完成後,下個階段按下送出才能扣庫存

						// 更新進貨單主檔明細檔
						this.updateImReceive(head, employeeCode, false);

						// 新增報單明細的log, 更新報關單明細的數量,更新cm,im庫存,
						this.updateStockAndLog(head, employeeCode, false, beforeStatus);

						//for 儲位用
						if(imStorageAction.isStorageExecute(head)){
							//異動儲位庫存，目前只能送出增加庫存，若為駁回isReject應為true
							boolean isReject = false;
							imStorageService.updateStorageOnHandBySource(head, head.getStatus(), PROGRAM_ID, identification, isReject);
						}
					}
				}
				resultMsg = head.getOrderTypeCode() + "-"+ head.getOrderNo() + "存檔成功！ 是否繼續新增？";

			} else if( errorMsgs.size() > 0 ){
				if( OrderStatus.FORM_SUBMIT.equals(formAction) ){
					throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
				}
			}
			resultMap.put("entityBean", head);
			resultMap.put("resultMsg", resultMsg);
		} catch( ValidationErrorException ve ){
			log.error("進貨短溢調整單檢核時發生錯誤，原因：" + ve.toString());
			throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		} catch (Exception e) {
			log.error("進貨短溢調整單存檔時發生錯誤，原因：" + e.toString());
			throw new Exception("進貨短溢調整單存檔時發生錯誤，原因：" + e.getMessage());
		}

		return resultMap;
	}

	/**
	 * 檢核,存取調整單主檔,明細檔,重新設定狀態 for 反轉
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Object executeReverter(Long headId, String employeeCode)throws Exception{
		ImAdjustmentHead head = null;
		try {
			head = executeFind(headId);

			String brandCode = head.getBrandCode();
			String orderTypeCode = head.getOrderTypeCode();
			String orderNo = head.getOrderNo();

			String beforeStatus = head.getStatus();

			// 檢核是否關帳
			if( !(OrderStatus.CLOSE.equalsIgnoreCase(beforeStatus) || OrderStatus.FINISH.equalsIgnoreCase(beforeStatus)) ){
				throw new Exception("品牌: " + brandCode + " 單別: " + orderTypeCode +  " 單號: " + orderNo + " 狀態不為CLOSE或FINISH");
			}
			ValidateUtil.isAfterClose(brandCode, orderTypeCode, "核准日期", head.getAdjustmentDate(),head.getSchedule());

			// 檢核成功則設定下個狀態
			setReverseNextStatus(head);

			// 更新調整單主檔明細檔
			updateImAdjustment(head, employeeCode );

			// 更新進貨單主檔明細檔
			updateImReceive(head, employeeCode, true);

			// 新增報單明細的log, 更新報關單明細的數量,更新cm,im庫存,
			updateStockAndLog(head, employeeCode, true, beforeStatus);

			// 清掉流程
			head.setProcessId(null);

			if( (OrderStatus.CLOSE.equalsIgnoreCase(beforeStatus) ) ){
				imTransationDAO.deleteTransationByIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
			}

			return head;

		} catch (Exception e) {
			log.error("進貨溢謝調整單反轉時發生錯誤，原因：" + e.toString());
			throw new Exception(e.getMessage());
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
		Object processObj[] = ImReceiveAdjustmentService.startProcess((ImAdjustmentHead)bean);
		head.setProcessId((Long)processObj[0]);
		imAdjustmentHeadDAO.update(head);
	}

	/**
	 * 更新bean
	 * @param head
	 */
	public void update(ImAdjustmentHead head) {
		try {
			imAdjustmentHeadDAO.update(head);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("e = " + e.toString());
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

			this.setImReceiveAdjustment(imAdjustmentHead);
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
	 * 更新進貨單主檔,明細檔
	 * @param head
	 * @param employeeCode
	 * @throws Exception
	 */
	private void updateImReceive(ImAdjustmentHead head, String employeeCode, boolean isReverter )throws Exception{
		Date date = new Date();

		try{
			String sourceOrderNo = head.getSourceOrderNo();
			ImReceiveHead imReceiveHead = this.findOneImReceive( head.getBrandCode(), head.getSourceOrderTypeCode(), sourceOrderNo, head.getStatus());
			if( imReceiveHead != null ){
				// 進貨單 head update
//				imReceiveHead.setStatus(OrderStatus.FINISH);					// 變啥狀態 !!!
//				imReceiveHead.setCmMovementNo( cmMovementHead.getOrderNo() );
				imReceiveHead.setLastUpdateDate( date );
				imReceiveHead.setLastUpdatedBy( employeeCode );

				// 進貨單 line update 只更新進貨短溢的
				List<ImAdjustmentLine> imAdjustmentLines = head.getImAdjustmentLines();
				List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
				for (ImReceiveItem imReceiveItem : imReceiveItems) {
					Double shortQuantity = imReceiveItem.getShortQuantity(); // 短到數量
					Double diffQty = imReceiveItem.getDiffQty();
					if( shortQuantity != 0 ){ // && diffQty != null && diffQty != 0
						for (ImAdjustmentLine imAdjustmentLine : imAdjustmentLines) {
							if( imReceiveItem.getItemCode().equals(imAdjustmentLine.getItemCode()) && (MORE.equals(imAdjustmentLine.getMoreOrLessType()) || LESS.equals(imAdjustmentLine.getMoreOrLessType())) ){

								if(isReverter){
									imReceiveItem.setDiffQty( diffQty + imAdjustmentLine.getDifQuantity() * -1 );	// 調整的差異量 * -1
								}else{
									imReceiveItem.setDiffQty( imAdjustmentLine.getDifQuantity() );	// 調整的差異量
								}
								imReceiveItem.setLastUpdateDate( date );
								imReceiveItem.setLastUpdatedBy( employeeCode );
								break;
							}
						}
					}
				}
				imReceiveHeadDAO.update(imReceiveHead);
			}else{
				throw new Exception("查無進貨單品牌:"+head.getBrandCode()+"來源單別:"+head.getSourceOrderTypeCode()+"來源單號:"+sourceOrderNo+"狀態"+head.getStatus());
			}

		}catch (Exception ex) {
			log.error("更新進貨單主檔明細檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新進貨單主檔明細檔時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 新增報單明細log, 更新報單明細數量, 更新cm_on_hand 和 im_on_hand 庫存
	 * @param imAdjustmentHead
	 * @param employeeCode
	 * @throws Exception
	 */
	private void updateStockAndLog(ImAdjustmentHead imAdjustmentHead, String employeeCode, boolean isReverter,
			String beforeStatus) throws Exception {
		log.info("======<updateStockAndLog>=======");
		List errorMsgs = new ArrayList();
		Map reverterMap = new HashMap();
		try {
			//以下註解是因為保稽需修正為CmDeclarationLog單據改寫,不透過AIF單做修正
//			String declarationNo = imAdjustmentHead.getDeclarationNo();
//
//			List<ImAdjustmentLine> lines = imAdjustmentHead.getImAdjustmentLines();
//			for (ImAdjustmentLine imAdjustmentLine : lines) {
//				System.out.println("報關單號：" + declarationNo);
//				System.out.println("報關單項次：" + imAdjustmentLine.getOriginalDeclarationSeq());
//				if (declarationNo != null && imAdjustmentLine.getOriginalDeclarationSeq() != null) {
//					CmDeclarationItem cmDeclarationItem = (CmDeclarationItem) cmDeclarationItemDAO.findFirstByProperty(
//							"CmDeclarationItem", "and declNo = ? and itemNo = ?", new Object[] { declarationNo,
//									imAdjustmentLine.getOriginalDeclarationSeq() });
//
//					// 存檔報單log !!! 確認新增的模式欄位
//					saveOneCmDeclarationLog(cmDeclarationItem, imAdjustmentLine, imAdjustmentHead, employeeCode, isReverter,
//							reverterMap);
//				}
//				// 更新報關單明細的數量 !!! 確認報單新增模式的欄位
//				 updateOneCmDeclarationQty(declarationNo, imAdjustmentLine, employeeCode, isReverter); // 20100920
//
//			}
//			// 更新庫存
//			updateAggregateStock(imAdjustmentHead, employeeCode, errorMsgs, isReverter, beforeStatus);
//			if (null != errorMsgs && errorMsgs.size() > 0) {
//				String identification = MessageStatus.getIdentification(imAdjustmentHead.getBrandCode(), imAdjustmentHead
//						.getOrderTypeCode(), imAdjustmentHead.getOrderNo());
//				throw new Exception(identification + "更新庫存時發生錯誤");
//			}
//
//			// 更新indexNo
//			if ("T2".equals(imAdjustmentHead.getBrandCode())) // 非T2單據不需檢查報單
//				updateCmDeclarationLogIndexNo(imAdjustmentHead);			
			
			// 更新庫存
			updateAggregateStock(imAdjustmentHead, employeeCode, errorMsgs, isReverter, beforeStatus);
			if (null != errorMsgs && errorMsgs.size() > 0) {
				String identification = MessageStatus.getIdentification(imAdjustmentHead.getBrandCode(), imAdjustmentHead
						.getOrderTypeCode(), imAdjustmentHead.getOrderNo());
				throw new Exception(identification + "更新庫存時發生錯誤");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("更新庫存,新增log,更新報關單時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新庫存,新增log,更新報關單時發生錯誤，原因：" + ex.getMessage());
		}
		log.info("======<updateStockAndLog/>=======");
	}


	/**
	 * 存檔報單明細log
	 * @param imAdjustmentLine
	 * @param imAdjustmentHead
	 * @param employeeCode
	 */
	private void saveOneCmDeclarationLog(CmDeclarationItem cmDeclarationItem, ImAdjustmentLine imAdjustmentLine, ImAdjustmentHead imAdjustmentHead, String employeeCode, boolean isReverter, Map reverterMap )throws Exception{

		Date date = new Date();
		CmDeclarationLog cmDeclarationLog = new CmDeclarationLog();
		try{
			String declarationType = imAdjustmentHead.getDeclarationType();
			String originalDeclarationNo = imAdjustmentHead.getDeclarationNo();
			Long originalDeclarationSeq = imAdjustmentLine.getOriginalDeclarationSeq();
			String moreOrLessType = imAdjustmentLine.getMoreOrLessType();
			Double difQuantity = imAdjustmentLine.getDifQuantity();
			String sourceOrderNo = imAdjustmentHead.getOrderTypeCode()+imAdjustmentHead.getOrderNo();

			log.info( "declarationType = " + declarationType );
			log.info( "originalDeclarationNo = " + originalDeclarationNo );
			log.info( "originalDeclarationSeq = " + originalDeclarationSeq );
			log.info( "moreOrLessType = " + moreOrLessType );
			log.info( "difQuantity = " + difQuantity );
			log.info( "sourceOrderNo = " + sourceOrderNo );

			CmDeclarationHead cmDeclarationHead = cmDeclarationHeadDAO.findOneCmDeclaration(originalDeclarationNo);
			Long headId = null;
			if( cmDeclarationHead!= null ){
				headId = cmDeclarationHead.getHeadId();
			}else{
				throw new Exception("查無報關單號"+originalDeclarationNo);
			}

			if( difQuantity != 0D ){ // 數量不為0的才修改報單紀錄
				if(!isReverter){
					if( cmDeclarationItem != null ){
						Double qty = cmDeclarationItem.getQty();
						CmDeclarationLog cmDeclarationLogNew = new CmDeclarationLog();

						log.info( "qty = " + qty );
						log.info( "headId = " + headId );

						// 修改報單的舊紀錄
						cmDeclarationLog.setIdentify( headId );
						cmDeclarationLog.setDeclType( declarationType );
						cmDeclarationLog.setDeclNo( originalDeclarationNo );
						cmDeclarationLog.setItemNo( originalDeclarationSeq );
						cmDeclarationLog.setModType("O");
						cmDeclarationLog.setQty(qty);
//						cmDeclarationLog.setBeforeChangeValue( String.valueOf() );
//						cmDeclarationLog.setAfterChangeValue(String.valueOf(qty + imAdjustmentLine.getDifQuantity()));
//						cmDeclarationLog.setSourceOrderTypeCode(imAdjustmentHead.getSourceOrderTypeCode());
//						cmDeclarationLog.setSourceOrderNo(imAdjustmentHead.getSourceOrderNo());
//						cmDeclarationLog.setDeclarationNo(imAdjustmentLine.getOriginalDeclarationNo());
//						cmDeclarationLog.setDeclarationType(imAdjustmentHead.getDeclarationType());
//						cmDeclarationLog.setEmployeeCode(employeeCode);
						cmDeclarationLog.setCreationDate(date);
						cmDeclarationLog.setCreatedBy(employeeCode);
						cmDeclarationLog.setLastUpdateDate(date);
						cmDeclarationLog.setLastUpdatedBy(employeeCode);
						cmDeclarationLog.setSourceOrderNo(sourceOrderNo); // 設定短溢到來源單號


						// 新增修改報單的新紀錄
						cmDeclarationLogNew.setIdentify( headId );
						cmDeclarationLogNew.setDeclType( declarationType );
						cmDeclarationLogNew.setDeclNo( originalDeclarationNo );
						cmDeclarationLogNew.setItemNo( originalDeclarationSeq );
						cmDeclarationLogNew.setModType("N");

						cmDeclarationLogNew.setQty(qty + difQuantity);

						cmDeclarationLogNew.setQtyMod("Y");

						cmDeclarationLogNew.setCreationDate(date);
						cmDeclarationLogNew.setCreatedBy(employeeCode);
						cmDeclarationLogNew.setLastUpdateDate(date);
						cmDeclarationLogNew.setLastUpdatedBy(employeeCode);
						cmDeclarationLogNew.setSourceOrderNo(sourceOrderNo); // 設定短溢到來源單號

						cmDeclarationLogDAO.save(cmDeclarationLogNew);
					}else{

						// 新增品項的紀錄
						cmDeclarationLog.setIdentify( headId ) ;
						cmDeclarationLog.setDeclType( declarationType );
						cmDeclarationLog.setDeclNo( originalDeclarationNo );
						cmDeclarationLog.setItemNo( originalDeclarationSeq );
						cmDeclarationLog.setModType("A");
						cmDeclarationLog.setPrdtNo(imAdjustmentLine.getItemCode());
						cmDeclarationLog.setPrdtNoMod("Y");

						cmDeclarationLog.setQty(difQuantity);
						cmDeclarationLog.setQtyMod("Y");

//						cmDeclarationLog.setFieldName("ITEM_NO");
//						cmDeclarationLog.setBeforeChangeValue( "" );
//						cmDeclarationLog.setAfterChangeValue(String.valueOf(imAdjustmentLine.getOriginalDeclarationSeq()));
//						cmDeclarationLog.setSourceOrderTypeCode(imAdjustmentHead.getSourceOrderTypeCode());
//						cmDeclarationLog.setSourceOrderNo(imAdjustmentHead.getSourceOrderNo());
//						cmDeclarationLog.setDeclarationNo(imAdjustmentLine.getOriginalDeclarationNo());
//						cmDeclarationLog.setDeclarationType(imAdjustmentHead.getDeclarationType());
//						cmDeclarationLog.setEmployeeCode(employeeCode);
						cmDeclarationLog.setCreationDate(date);
						cmDeclarationLog.setCreatedBy(employeeCode);
						cmDeclarationLog.setLastUpdateDate(date);
						cmDeclarationLog.setLastUpdatedBy(employeeCode);
						cmDeclarationLog.setSourceOrderNo(sourceOrderNo); // 設定短溢到來源單號
					}
					cmDeclarationLogDAO.save(cmDeclarationLog);
				}else{
					// 反轉
					// 刪除之前的log含短溢到和新增品項
					if( cmDeclarationItem != null ){
						Double qty = cmDeclarationItem.getQty(); // 修正前的報單數量
						// 短溢到
						if(MORE.equals(moreOrLessType) || LESS.equals(moreOrLessType)){
							Double qtyAfter = qty + difQuantity; // 修正後的報單數量

							// 刪除修正前的log
							if(!reverterMap.containsKey(headId+originalDeclarationNo+originalDeclarationSeq+O+qty)){
								// 加入並刪除log
								CmDeclarationLog deleteCmDeclarationLog = (CmDeclarationLog)cmDeclarationLogDAO.findFirstByProperty("CmDeclarationLog", "and identify = ? and declNo = ? and itemNo = ? and modType = ? and qty = ? and sourceOrderNo = ?", new Object[]{headId, originalDeclarationNo, originalDeclarationSeq, O, qty, sourceOrderNo});
								if(null == deleteCmDeclarationLog){
									throw new Exception("查無報單Log PK:" + headId + "報單:"+ originalDeclarationNo + " 報單號碼:"+ originalDeclarationSeq + " 短溢類別:" + O + " 修正前的報單數量:" + qty + " 短溢單" +  sourceOrderNo);
								}
								deleteCmDeclarationLog.setIdentify(null);
								cmDeclarationLogDAO.update(deleteCmDeclarationLog);
								reverterMap.put(headId+originalDeclarationNo+originalDeclarationSeq+O+qty, headId+originalDeclarationNo+originalDeclarationSeq+O+qty);
							}else{
								throw new Exception("報單Log 含有相同 PK:" + headId + "報單:"+ originalDeclarationNo + " 報單號碼:"+ originalDeclarationSeq + " 修正前的報單數量:" + qty );
							}
							// 刪除修正後的log
							if(!reverterMap.containsKey(headId+originalDeclarationNo+originalDeclarationSeq+N+qtyAfter)){
								// 加入並刪除log
								CmDeclarationLog deleteCmDeclarationLog = (CmDeclarationLog)cmDeclarationLogDAO.findFirstByProperty("CmDeclarationLog", "and identify = ? and declNo = ? and itemNo = ? and modType = ? and qty = ? and sourceOrderNo = ?", new Object[]{headId, originalDeclarationNo, originalDeclarationSeq, N, qtyAfter, sourceOrderNo});
								if(null == deleteCmDeclarationLog){
									throw new Exception("查無報單Log PK:" + headId + "報單:"+ originalDeclarationNo + " 報單號碼:"+ originalDeclarationSeq + " 短溢類別:" + N + " 修正後的報單數量:" + qtyAfter + " 短溢單" +  sourceOrderNo);
								}
								deleteCmDeclarationLog.setIdentify(null);
								cmDeclarationLogDAO.update(deleteCmDeclarationLog);
								reverterMap.put(headId+originalDeclarationNo+originalDeclarationSeq+N+qtyAfter, headId+originalDeclarationNo+originalDeclarationSeq+N+qtyAfter);
							}else{
								throw new Exception("報單Log 含有相同 PK:" + headId + "報單:"+ originalDeclarationNo + " 報單號碼:"+ originalDeclarationSeq + " 修正後的報單數量:" + qtyAfter );
							}
						}else if(ADD.equals(moreOrLessType)){
							// 刪除新增品項的log  P.S:新增品項的數量只在短溢卸單的明細裡調整數量:difQuantity，原報單明細並沒有
							if(!reverterMap.containsKey(headId+originalDeclarationNo+originalDeclarationSeq+A+difQuantity)){
								// 加入並刪除log
								CmDeclarationLog deleteCmDeclarationLog = (CmDeclarationLog)cmDeclarationLogDAO.findFirstByProperty("CmDeclarationLog", "and identify = ? and declNo = ? and itemNo = ? and modType = ? and qty = ? and sourceOrderNo = ?", new Object[]{headId, originalDeclarationNo, originalDeclarationSeq, A, difQuantity, sourceOrderNo});
								if(null == deleteCmDeclarationLog){
									throw new Exception("查無報單Log PK:" + headId + "報單:"+ originalDeclarationNo + " 報單號碼:"+ originalDeclarationSeq + " 短溢類別:" + A + " 新增品項的報單數量:" + difQuantity + " 短溢單" +  sourceOrderNo);
								}
								deleteCmDeclarationLog.setIdentify(null);
								cmDeclarationLogDAO.update(deleteCmDeclarationLog);
								reverterMap.put(headId+originalDeclarationNo+originalDeclarationSeq+A+difQuantity, headId+originalDeclarationNo+originalDeclarationSeq+A+difQuantity);
							}else{
								throw new Exception("報單Log 含有相同 PK:" + headId + "報單:"+ originalDeclarationNo + " 報單號碼:"+ originalDeclarationSeq + " 新增品項的 報單數量:" + difQuantity );
							}
						}
					}
				}

			}
		}catch (Exception ex) {
			ex.printStackTrace();
			log.error("存檔報單明細log時發生錯誤，原因：" + ex.toString());
			throw new Exception("存檔報單明細log時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 更新報關單明細的數量或新增報單明細
	 * @param cmDeclarationItem
	 * @param imAdjustmentLine
	 */
	private void updateOneCmDeclarationQty(String declarationNo, ImAdjustmentLine imAdjustmentLine, 
			String employeeCode, boolean isReverter )throws Exception{
		try{
			String moreOrLessType = imAdjustmentLine.getMoreOrLessType();
			Double difQuantity = imAdjustmentLine.getDifQuantity();
			if(difQuantity != 0D){ // 數量不為0才會修改原報單明細數量
				//不是反確認
				if(!isReverter){
					//新增報關明細
					if(ADD.equals(moreOrLessType)){
						CmDeclarationHead cmDeclarationHead = cmDeclarationHeadDAO.findOneCmDeclaration(declarationNo);
						
						CmDeclarationItem cmDeclarationItem = new CmDeclarationItem();
						
						cmDeclarationItem.setDeclNo(declarationNo);
						cmDeclarationItem.setItemNo(imAdjustmentLine.getOriginalDeclarationSeq());
						cmDeclarationItem.setQty(0D);
						cmDeclarationItem.setIndexNo(imAdjustmentLine.getOriginalDeclarationSeq());
						cmDeclarationItem.setAdj(moreOrLessType);
						cmDeclarationItem.setCreatedBy(employeeCode);
						cmDeclarationItem.setCreationDate(new Date());
						cmDeclarationItem.setLastUpdatedBy(employeeCode);
						cmDeclarationItem.setLastUpdateDate(new Date());
						cmDeclarationItem.setCmDeclarationHead(cmDeclarationHead);
						cmDeclarationItemDAO.save(cmDeclarationItem);
					//修改報關明細	
					}else{
						CmDeclarationItem cmDeclarationItem = (CmDeclarationItem) cmDeclarationItemDAO.findFirstByProperty(
								"CmDeclarationItem", "and declNo = ? and itemNo = ?", new Object[] { declarationNo,
										imAdjustmentLine.getOriginalDeclarationSeq() });
						
						//cmDeclarationItem.setQty(cmDeclarationItem.getQty() + difQuantity);
						cmDeclarationItem.setAdj(moreOrLessType);
						cmDeclarationItem.setLastUpdatedBy(employeeCode);
						cmDeclarationItem.setLastUpdateDate(new Date());
						cmDeclarationItemDAO.update(cmDeclarationItem);
					}
				}else{
					if(ADD.equals(moreOrLessType)){
						CmDeclarationItem cmDeclarationItem = (CmDeclarationItem) cmDeclarationItemDAO.findFirstByProperty(
								"CmDeclarationItem", "and declNo = ? and itemNo = ?", new Object[] { declarationNo,
										imAdjustmentLine.getOriginalDeclarationSeq() });
						cmDeclarationItemDAO.delete(cmDeclarationItem);
					}else{
						CmDeclarationItem cmDeclarationItem = (CmDeclarationItem) cmDeclarationItemDAO.findFirstByProperty(
								"CmDeclarationItem", "and declNo = ? and itemNo = ?", new Object[] { declarationNo,
										imAdjustmentLine.getOriginalDeclarationSeq() });
						
						//cmDeclarationItem.setQty(cmDeclarationItem.getQty() - difQuantity);
						cmDeclarationItem.setAdj(null);
						cmDeclarationItem.setLastUpdatedBy(employeeCode);
						cmDeclarationItem.setLastUpdateDate(new Date());
						cmDeclarationItemDAO.update(cmDeclarationItem);
					}
				}
			}
		}catch (Exception ex) {
			log.error("存檔報關單明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("存檔報關單明細時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 更新 cm_on_hand 和 im_on_hand 庫存
	 * @param imAdjustmentLine
	 * @param imAdjustmentHead
	 * @param employeeCode
	 * @throws FormException
	 */
	private void updateOneStock(ImAdjustmentLine imAdjustmentLine, ImAdjustmentHead imAdjustmentHead, String employeeCode, boolean isReverter)
	throws Exception {
		log.info( "======<updateOneStockAndLog>=======");
		try{
			String brandCode = imAdjustmentHead.getBrandCode();								// 共用 PK

			Double quantity = imAdjustmentLine.getDifQuantity();  							// 共用 短到數量

			if(isReverter){
				quantity = quantity * - 1;
			}

			if( quantity != 0 ){
				// 更新cm,im庫存
				this.updateStock( imAdjustmentLine, brandCode, quantity, employeeCode );
			}
		}catch (Exception ex) {
			log.error("存檔庫存時發生錯誤，原因：" + ex.toString());
			throw new Exception("存檔庫存時發生錯誤，原因：" + ex.getMessage());
		}
		log.info( "======<updateOneStockAndLog/>=======");
	}

	/**
	 * 更新庫存(cm_decaration_on_hand, im_on_hand)
	 * @param imAdjustmentLine
	 * @param brandCode
	 * @param quantity
	 * @param loginUser
	 * @throws FormException
	 */
	private void updateStock( ImAdjustmentLine imAdjustmentLine, String brandCode, Double quantity, String loginUser )
	throws Exception {
		try{
			log.info( "======<updateStock>=======");

			String moreOrLessType = imAdjustmentLine.getMoreOrLessType();

			// im_on_hand 用
			String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode); 	// 組織代碼 PK
			String itemCode = imAdjustmentLine.getItemCode();  								// 商品 PK
			String warehouseCode = imAdjustmentLine.getWarehouseCode(); 					// 倉庫 PK
			String lotNo = imAdjustmentLine.getLotNo();										// 批號 PK

			log.info( "organizationCode = " + organizationCode );
			log.info( "itemCode = " + itemCode );
			log.info( "warehouseCode = " + warehouseCode );
			log.info( "lotNo = " + lotNo );
			log.info( "quantity = " + quantity );

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

			// cm_on_hand 加庫存
			log.info("======<cm_decaration_on_hand>======");

			if( moreOrLessType == null || MORE.equals(moreOrLessType) || LESS.equals(moreOrLessType) || ADD.equals(moreOrLessType) ){
				this.updateOtherUncommitQuantity( moreOrLessType, originalDeclarationNo, originalDeclarationSeq,
						customsItemCode, customsWarehouseCode, brandCode, quantity, loginUser );
			}
			log.info("======<cm_decaration_on_hand/>======");

			// im_on_hand 加庫存
			log.info("======<im_on_hand>======");
			if( moreOrLessType == null || MORE.equals(moreOrLessType) || LESS.equals(moreOrLessType) || ADD.equals(moreOrLessType)  ){
				log.info("要做im_on_hand");
				imOnHandDAO.updateOtherUncommitQuantity( organizationCode, itemCode,
						warehouseCode, lotNo, brandCode, quantity, loginUser);
			}
			log.info("======<im_on_hand/>======");

			log.info( "======<updateStock/>=======");

		}catch (Exception ex) {
			log.error("存取庫存時發生錯誤，原因：" + ex.toString());
			throw new Exception("存取庫存時發生錯誤，原因：" + ex.getMessage());
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
		//==============相同的商品、庫別、批號產生產生集合、相同的報單號碼、報單項次、商品、關別集合============
		Set[] aggregateResult = aggregateOrderItemsQty(imAdjustmentHead, employeeCode, isReverse);
		Iterator it = aggregateResult[0].iterator(); //ImOnHand扣庫存用
		Iterator cmIt = aggregateResult[1].iterator(); //CmOnHand扣庫存用
		//======================================預扣報單庫存量=======================================================
		while(cmIt.hasNext()){
			try{
				Map.Entry cmEntry = (Map.Entry) cmIt.next();
				Double unCommitQty = (Double) cmEntry.getValue();
				String[] cmkeyArray = StringUtils.delimitedListToStringArray((String)cmEntry.getKey(), "{$}");

				if(OrderStatus.CLOSE.equalsIgnoreCase(beforeStatus) && isReverse ){
					cmDeclarationOnHandDAO.updateStockOnHandByOther(cmkeyArray[0], Long.valueOf(cmkeyArray[1]), cmkeyArray[2], cmkeyArray[3],
							brandCode, unCommitQty, employeeCode); // modified by Weichun 2011.09.27
				}else{ // FINISH
					cmDeclarationOnHandDAO.updateOtherUncommitQuantity(cmkeyArray[0], Long.valueOf(cmkeyArray[1]), cmkeyArray[2], cmkeyArray[3],
							brandCode, unCommitQty, employeeCode, "Y"); // modified by Weichun 2011.09.27
				}
			}catch(Exception ex){
				errorMsg = "預扣" + identification + "的報單庫存量時發生錯誤，原因：";
				log.error(errorMsg + ex.toString());
				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, errorMsg + ex.getMessage(), employeeCode);
				errorMsgs.add(errorMsg);
			}
		}
		//======================================預扣實體庫別庫存量=======================================================
		while (it.hasNext()) {
			try{
				Map.Entry entry = (Map.Entry) it.next();
				Double unCommitQty = (Double) entry.getValue();
				String[] keyArray = StringUtils.delimitedListToStringArray((String) entry.getKey(), "{$}");
				if(OrderStatus.CLOSE.equalsIgnoreCase(beforeStatus) && isReverse ){
					imOnHandDAO.updateStockOnHand(organizationCode,  brandCode, keyArray[0], keyArray[1], keyArray[2], unCommitQty, employeeCode);
				}else{ // FINISH
					imOnHandDAO.updateOtherUncommitQuantity(organizationCode,  keyArray[0], keyArray[1], keyArray[2], brandCode, unCommitQty, employeeCode);
				}
			}catch(Exception ex){
				errorMsg = "預扣" + identification + "庫別庫存量時發生錯誤，原因：";
				log.error(errorMsg + ex.toString());
				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, errorMsg + ex.getMessage(), employeeCode);
				errorMsgs.add(errorMsg);
			}
		}
	}

	/**
	 * 更新報關單indexNo
	 * @param imAdjustmentHead
	 * @throws Exception
	 */
	private void updateCmDeclarationLogIndexNo(ImAdjustmentHead imAdjustmentHead)throws Exception{
		Long indexNo = 1L;
		try {
			String originalDeclarationNo = imAdjustmentHead.getDeclarationNo();
			CmDeclarationHead cmDeclarationHead = cmDeclarationHeadDAO.findOneCmDeclaration(originalDeclarationNo);
			if(cmDeclarationHead == null){
				throw new Exception("查無報關單號"+originalDeclarationNo);
			}
			Long headId = cmDeclarationHead.getHeadId();

			List<CmDeclarationLog> cmDeclarationLogs =  cmDeclarationLogService.findcmDeclarationLogsById(headId);
			for (CmDeclarationLog cmDeclarationLog : cmDeclarationLogs) {
				cmDeclarationLog.setIndexNo(indexNo);
				indexNo++;
				cmDeclarationLogDAO.update(cmDeclarationLog);
			}

		} catch (Exception e) {
			log.error("更新報關單log索引時發生錯誤，原因：" + e.toString());
			throw new Exception("更新報關單log索引時發生錯誤，原因：" + e.getMessage());
		}
	}

	/**
	 * 更新未結調整數量
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @param quantity
	 * @param loginUser
	 * @throws FormException
	 */
	public void updateOtherUncommitQuantity( String moreOrLessType, String declarationNo, Long declarationSeq, String customsItemCode, String customsWarehouseCode, String brandCode, Double quantity,
			String loginUser) throws FormException {
		try {

			List<CmDeclarationOnHand> lockedOnHands = cmDeclarationOnHandDAO.getLockedOnHand(declarationNo, declarationSeq, customsItemCode, customsWarehouseCode, brandCode);
			CmDeclarationOnHand lockedOnHand = null;
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				if (quantity < 0) {
					Double availableQuantity = cmDeclarationOnHandDAO.getCurrentOnHandQtyByProperty(brandCode, declarationNo, declarationSeq, customsItemCode, customsWarehouseCode);

					if (availableQuantity == null) {
						throw new NoSuchObjectException("更新庫存時，查無品號：" + customsItemCode + ",關別：" + customsWarehouseCode + "的庫存量！");
					} else if (quantity > availableQuantity) {
						throw new InsufficientQuantityException("品號：" + customsItemCode + ",關別：" + customsWarehouseCode + "可用庫存量不足！");
					}
				}
				if(MORE.equals(moreOrLessType) || LESS.equals(moreOrLessType)){
					lockedOnHand = lockedOnHands.get(0);
					Double otherUncommitQty = NumberUtils.getDouble(lockedOnHand.getOtherUncommitQty());
					lockedOnHand.setOtherUncommitQty(otherUncommitQty + quantity);
					lockedOnHand.setLastUpdatedBy(loginUser);
					lockedOnHand.setLastUpdateDate(new Date());
					cmDeclarationOnHandDAO.update(lockedOnHand);
				}else if(ADD.equals(moreOrLessType)){
					throw new FormException("品號：" + customsItemCode + ",庫別：" + customsWarehouseCode + "為新增品項不得有重複的庫存資料！");
				}
			} else {
				if(MORE.equals(moreOrLessType) || LESS.equals(moreOrLessType)){
					throw new FormException("查無品號：" + customsItemCode + ",庫別：" + customsWarehouseCode + "的庫存資料！");
				}else if(ADD.equals(moreOrLessType)){
					log.info( "declarationNo = " + declarationNo );
					log.info( "declarationSeq = " + declarationSeq );
					log.info( "customsWarehouseCode = " + customsWarehouseCode );
					log.info( "brandCode = " + brandCode );

					lockedOnHand = new CmDeclarationOnHand();
					lockedOnHand.setDeclarationNo(declarationNo);
					lockedOnHand.setDeclarationSeq(declarationSeq);
					lockedOnHand.setCustomsItemCode(customsItemCode);
					lockedOnHand.setCustomsWarehouseCode(customsWarehouseCode);
					lockedOnHand.setBrandCode(brandCode);
					lockedOnHand.setMoveUncommitQty(0D);
					lockedOnHand.setOnHandQuantity(0D);
					lockedOnHand.setInUncommitQty(0D);
					lockedOnHand.setOutUncommitQty(0D);
					lockedOnHand.setOtherUncommitQty(quantity);
					lockedOnHand.setCreatedBy(loginUser);
					lockedOnHand.setCreationDate(new Date());
					lockedOnHand.setLastUpdatedBy(loginUser);
					lockedOnHand.setLastUpdateDate(new Date());
					cmDeclarationOnHandDAO.save(lockedOnHand);
				}
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品號：" + customsItemCode + ",關別：" + customsWarehouseCode + "已鎖定，請稍後再試！");
		}
	}

	/**
	 * 檢核進貨短溢調整主檔,明細檔
	 * @param Head
	 * @param programId
	 * @param identification
	 * @param errorMsgs
	 * @throws ValidationErrorException
	 * @throws NoSuchObjectException
	 */
	private void validateImReceiveMoreOrLessAdjustment(ImAdjustmentHead head, String programId, String identification, List errorMsgs, Object formLinkBean) throws ValidationErrorException, NoSuchObjectException {
		validteHead(head, programId, identification, errorMsgs, formLinkBean);
		validteMoreOrLessLine(head, programId, identification, errorMsgs);
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
	private void validteHead(ImAdjustmentHead head, String programId, String identification, List errorMsgs, Object formLinkBean) throws ValidationErrorException, NoSuchObjectException {
		String message = null;
		String tabName = "主檔資料";
		try {
			String brandCode = head.getBrandCode();
			String orderTypeCode = head.getOrderTypeCode();
			String sourceOrderTypeCode = head.getSourceOrderTypeCode();
			String sourceOrderNo = head.getSourceOrderNo();
			String defaultWarehouseCode = head.getDefaultWarehouseCode();
			Date adjustmentDate = head.getAdjustmentDate();

			if( !StringUtils.hasText(sourceOrderTypeCode) ){
				message = "請輸入" + tabName + "的來源進貨單別！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}
			if(!StringUtils.hasText(sourceOrderNo)){
				message = "請輸入" + tabName + "的來源進貨單號！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}else{
				ImReceiveHead imReceiveHead = null;
				if( StringUtils.hasText(sourceOrderTypeCode) ){
					imReceiveHead = this.findOneImReceive( brandCode, sourceOrderTypeCode, sourceOrderNo, head.getStatus());
				}
				if( imReceiveHead == null ){
					message = "查無" + tabName + "的來源進貨單號！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}
			}

			if( !StringUtils.hasText(defaultWarehouseCode) ){
				message = "請輸入" + tabName + "的庫別！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}else{
				ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, defaultWarehouseCode, "Y");
				if(imWarehouse == null){
					message = "查無" + tabName + "的庫別！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}
			}
			if( adjustmentDate == null ){
				message = "請輸入" + tabName + "的核准日期！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}else{
				ValidateUtil.isAfterClose(brandCode, orderTypeCode, "核准日期", head.getAdjustmentDate(),head.getSchedule());
			}
		} catch (Exception e) {
			message = "檢核調整單主檔單" + tabName + "時發生錯誤，原因：" + e.toString();
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		}
	}

	/**
	 * 檢核進貨短溢調整單明細檔
	 * @param head
	 * @param programId
	 * @param identification
	 * @param errorMsgs
	 * @throws ValidationErrorException
	 * @throws NoSuchObjectException
	 */
	private void validteMoreOrLessLine(ImAdjustmentHead head, String programId,
			String identification, List errorMsgs) throws ValidationErrorException, NoSuchObjectException {
		String message = null;
		String tabName = "明細資料頁籤";
		Map orderNoMap = new HashMap();
//		Map seqMap = new HashMap();
		Map imReceiveItemMap = new HashMap();
		ImReceiveHead imReceiveHead = null;
		try{
			String brandCode = head.getBrandCode();
			String taxType = head.getTaxType();
			String sourceOrderTypeCode = head.getSourceOrderTypeCode();
			String sourceOrderNo = head.getSourceOrderNo();

			List lines = head.getImAdjustmentLines();

			if( StringUtils.hasText(sourceOrderTypeCode) && StringUtils.hasText(sourceOrderNo) ){
				imReceiveHead = this.findOneImReceive(brandCode, sourceOrderTypeCode, sourceOrderNo, head.getStatus());
				if( imReceiveHead != null ){
					List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();
					for (ImReceiveItem imReceiveItem : imReceiveItems) {
						imReceiveItemMap.put( imReceiveItem.getItemCode(), imReceiveItem.getIndexNo() );
					}
				}
			}
			int size = lines.size();
			if( size > 0 ){
				for (int i = 0; i < size; i++) {
					ImAdjustmentLine line = (ImAdjustmentLine) lines.get(i);
					String itemCode = line.getItemCode().trim().toUpperCase();
					String indexNo = String.valueOf( line.getIndexNo() );
					String lotNo = line.getLotNo();
					boolean isMoreOrLess = imReceiveItemMap.containsKey(itemCode);

//					if( orderNoMap.containsKey( itemCode ) ){
//					message = tabName + "中第" + (i + 1) + "項明細的商品" + itemCode + "重複！";
//					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//					errorMsgs.add(message);
//					log.error(message);
//					}else{
//					orderNoMap.put( itemCode ,  indexNo );
//					}

					if (!StringUtils.hasText(itemCode)) {
						message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的商品！";
						siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
						errorMsgs.add(message);
						log.error(message);
					} else {

						// 檢核調整單明細商品是否存在
						ImItem imItem = this.findOneImItem( brandCode, itemCode, taxType );

						if( imItem == null ){
							message = tabName + "中第" + (i + 1) + "項明細的商品不存在！";
							siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
							errorMsgs.add(message);
							log.error(message);
						} else {
							String moreOrLessType = line.getMoreOrLessType();
							Double amount = line.getAmount();
							String warehouseCode = line.getWarehouseCode();
							Double quantity = line.getDifQuantity();
							Long originalDeclarationSeq = line.getOriginalDeclarationSeq();
							String originalDeclarationNo = line.getOriginalDeclarationNo();
							String lineMessage = line.getMessage();

							log.info( "quantity.intValue() = " + quantity.intValue() );

							// 批號檢查
							if( !StringUtils.hasText(lotNo) ){
								message = tabName + "中第" + (i + 1) + "項明細的批號不能為空！";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							}else{
								// 若商品不用批號管理 檢查批號預設值
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

							// 新增項次不用檢核品號 Caspar 20121113
							// 檢核下拉霸與商品的關係,調整明細是否存在在進貨明細
							/*if( ADD.equals(moreOrLessType)  && isMoreOrLess ){
				message = tabName + "中第" + (i + 1) + "項明細商品的短溢調整類別不符,應為短溢到！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }else */if( (MORE.equals(moreOrLessType) || LESS.equals(moreOrLessType)) && !isMoreOrLess ) {
			    	message = tabName + "中第" + (i + 1) + "項明細商品的短溢調整類別不符,應為新增！";
			    	siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    	errorMsgs.add(message);
			    	log.error(message);
			    }
			    // 總成本
//			    if(amount == 0D){
//			    message = tabName + "中第" + (i + 1) + "項明細商品的總成本不能為0";
//			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//			    errorMsgs.add(message);
//			    log.error(message);
//			    }

			    // 倉庫
			    ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(head.getBrandCode(), warehouseCode, "Y");
			    if(imWarehouse == null){
			    	message = "查無" + tabName + "中第" + (i + 1) + "項明細商品的庫別！";
			    	siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    	errorMsgs.add(message);
			    	log.error(message);
			    }

			    // 數量
			    if( (MORE.equals(moreOrLessType) || LESS.equals(moreOrLessType) ) && isMoreOrLess && !ValidateUtil.isInteger(String.valueOf(quantity.intValue())) ){
			    	message = tabName + "中第" + (i + 1) + "項明細短溢到商品的數量必須為整數！";
			    	siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    	errorMsgs.add(message);
			    	log.error(message);
			    }else if( ADD.equals(moreOrLessType) && !isMoreOrLess  && !ValidateUtil.isNumber(String.valueOf(quantity.intValue())) ){
			    	message = tabName + "中第" + (i + 1) + "項明細新增品項商品的數量必須為正整數！";
			    	siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    	errorMsgs.add(message);
			    	log.error(message);
			    }

			    /*if(quantity == 0D && (LESS.equals(moreOrLessType) || ADD.equals(moreOrLessType)) ){
			    	if(LESS.equals(moreOrLessType)){
			    		message = tabName + "中第" + (i + 1) + "項明細短到商品的數量不能為0";
			    	}else if(ADD.equals(moreOrLessType)){
			    		message = tabName + "中第" + (i + 1) + "項明細新增商品的數量不能為0";
			    	}
			    	siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    	errorMsgs.add(message);
			    	log.error(message);
			    }*/

			    // 報單
			    if(!StringUtils.hasText(originalDeclarationNo)){
			    	message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的報單！";
			    	siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    	errorMsgs.add(message);
			    	log.error(message);
			    }

			    //	報關項次
//			    if(seqMap.containsKey( originalDeclarationSeq )){
//			    message = tabName + "中第" + (i + 1) + "項明細的報單項次" + originalDeclarationSeq + "重複！";
//			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
//			    errorMsgs.add(message);
//			    log.error(message);
//			    }else{
//			    seqMap.put( originalDeclarationSeq, indexNo);
//			    }

			    if(originalDeclarationSeq == 0L){
			    	message = tabName + "中第" + (i + 1) + "項明細商品的報單項次不能為0！";
			    	siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    	errorMsgs.add(message);
			    	log.error(message);
			    }else if(!ValidateUtil.isNumber(String.valueOf(originalDeclarationSeq)) ){
			    	message = tabName + "中第" + (i + 1) + "項明細商品的報單項次必須為正整數！";
			    	siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    	errorMsgs.add(message);
			    	log.error(message);
			    }else if(StringUtils.hasText(lineMessage)){
			    	message = tabName + "中第" + (i + 1) + "項明細商品"+lineMessage+"！";
			    	siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    	errorMsgs.add(message);
			    	log.error(message);
			    }else{
			    	// 由於新增品項的項次由使用者輸入, 因此需要檢核
			    	if(ADD.equals(moreOrLessType)){
			    		// 檢核新增品項是否存在, 怕選到沒有短溢到的報單項次
			    		CmDeclarationItem cmDeclarationItem =  (CmDeclarationItem)cmDeclarationItemDAO.findFirstByProperty("CmDeclarationItem", "and declNo = ? and item_no = ? ", new Object[]{ originalDeclarationNo, originalDeclarationSeq} );
			    		if(null != cmDeclarationItem){
			    			message = tabName + "中第" + (i + 1) + "項明細商品的報單項次已存在！";
			    			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    			errorMsgs.add(message);
			    			log.error(message);
			    		}
			    	}
			    }


						}
					}

				}
			}else{
				message = tabName + "中請至少輸入一筆進貨短溢調整單明細！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}
		} catch (Exception e) {
			message = "檢核進貨短溢單明細檔" + tabName + "時發生錯誤，原因：" + e.toString();
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		}
	}

}
