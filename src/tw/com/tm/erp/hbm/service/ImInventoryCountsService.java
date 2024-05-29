package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsHead;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsLine;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsList;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImReceiveExpense;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.TmpAppStockStatistics;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.ImInventoryCountsHeadDAO;
import tw.com.tm.erp.hbm.dao.ImInventoryCountsLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;
import tw.com.tm.erp.utils.sp.TmpAppStockStatisticsService;


/**
 * @author T15394
 *
 */
/**
 * @author T15394
 *
 */
public class ImInventoryCountsService {

	private static final Log log = LogFactory.getLog(ImInventoryCountsService.class);

	public static final String PROGRAM_ID = "IM_INVENTORY";
	
	public static final String PROGRAM_ID_LIST = "IM_INVENTORY_LIST";
	
	public static final String PROGRAM_ID_IMPORT = "PROGRAM_ID_IMPORT";

	public static final String REPORT_FUNCTION_CODE = "IM0123";

	public static final String REPORT_FUNCTION_CODE_LIST = "IM0120";

	public static final String REPORT_FUNCTION_CODE_DIFFERENT_ONE = "IM0121";

	public static final String REPORT_FUNCTION_CODE_DIFFERENT_TWO = "IM0122";

	public static final String REPORT_FILE_NAME = "im0123.rpt";

	public static final String REPORT_FILE_NAME_LIST = "im0120.rpt";

	public static final String REPORT_FILE_NAME_DIFFERENT_ONE = "im0121.rpt";

	public static final String REPORT_FILE_NAME_DIFFERENT_TWO = "im0122.rpt";

	public static final String[] GRID_FIELD_NAMES = { 
		"indexNo", "itemCode", "itemName", "eanCode", "countsQty", 
		"countsQtyFinal", "description", "lineId", "isLockRecord", "isDeleteRecord",
		"message"};

	public static final int[] GRID_FIELD_TYPES = { 
		AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, 
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
		"0", "", "", "", "", "", 
		"", "", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, 
		""};

	public static final String[] GRID_SEARCH_FIELD_NAMES = {
	    	"orderTypeCode", "orderNo", "countsDate", "actualCountsDate", 
	    	"lastImportDate", "countsId", "countsLotNo", "countsType", 
	    	"warehouseCode", "status", "headId"};

	public static final int[] GRID_SEARCH_FIELD_TYPES = { 
	    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE  , AjaxUtils.FIELD_TYPE_DATE,
	    	AjaxUtils.FIELD_TYPE_DATE  , AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG};

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
	    	"", "", "", "",
	    	"", "", "", "",
	    	"", "", "0"};   

	
	public static final String[] GRID_FIELD_LIST_NAMES = { 
		"indexNo", "warehouseCode", "warehouseName", "customsWarehouseCode", "categoryCode", 
		"superintendentCode", "superintendentName", "countsDate", "startNo", "endNo", 
		"isLockRecord", "headId", "isDeleteRecord", "message"};

	public static final int[] GRID_FIELD_LIST_TYPES = { 
		AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,   AjaxUtils.FIELD_TYPE_INT,    AjaxUtils.FIELD_TYPE_INT,    
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};

	public static final String[] GRID_FIELD_LIST_DEFAULT_VALUES = { 
		"0", "", "", "", "",
		"", "", "", "0", "0", 
		"", "", AjaxUtils.IS_DELETE_RECORD_FALSE, ""};
	
	private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;

	private ImInventoryCountsHeadDAO imInventoryCountsHeadDAO;

	private ImInventoryCountsLineDAO imInventoryCountsLineDAO;

	private ImItemDAO imItemDAO;

	private ImWarehouseDAO imWarehouseDAO;

	private BuBrandDAO buBrandDAO;

	private BuOrderTypeService buOrderTypeService;

	private TmpAppStockStatisticsService tmpAppStockStatisticsService;

	private ImInventoryCountsLineService imInventoryCountsLineService;

	private BuCommonPhraseService buCommonPhraseService;

	private SiProgramLogAction siProgramLogAction;

	private ImItemEancodeDAO imItemEancodeDAO;

	/**
	 * 將盤點資料新增或更新至盤點單主檔及明細檔
	 * 
	 * @param inventoryCountsHead
	 * @param conditionMap
	 * @param loginUser
	 * @return String
	 * @throws Exception
	 */
	public String saveOrUpdateImInventoryCounts(ImInventoryCountsHead inventoryCountsHead, HashMap conditionMap, String loginUser) throws Exception {

		try {
			String beforeChangeStatus = (String)conditionMap.get("beforeChangeStatus");
			String status = inventoryCountsHead.getStatus();

			updateImInventoryCountsLineStatus(inventoryCountsHead, status);
			//檢核盤點主檔資料
			//checkInventoryCountsHead(inventoryCountsHead);
			//狀態從SAVE變成COUNTING時call stored procedure 取庫存量(帶量盤點)
			if(OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.COUNTING.equals(status) && "1".equals(inventoryCountsHead.getCountsType())){
				HashMap map = new HashMap();
				map.put("brandCode", inventoryCountsHead.getBrandCode());
				map.put("warehouseCode", inventoryCountsHead.getWarehouseCode());
				map.put("onHandDate", DateUtils.format(inventoryCountsHead.getInventoryDate(), DateUtils.C_DATA_PATTON_YYYYMMDD));
				getOnHandQty(inventoryCountsHead, map, loginUser);		
			}	    

			//狀態從COUNTING變成COUNT_FINISH時檢核明細資料頁籤至少需輸入一筆資料
			if(OrderStatus.COUNTING.equals(beforeChangeStatus) && OrderStatus.COUNT_FINISH.equals(status)){
				//List<ImInventoryCountsLine> inventoryCountsLines = imInventoryCountsLineDAO.findByHeadId(inventoryCountsHead.getHeadId());
				//inventoryCountsLines = StringTools.setBeanValue(inventoryCountsLines, "indexNo", null);
				//inventoryCountsHead.setImInventoryCountsLines(inventoryCountsLines);
				//checkInventoryCountsLine(inventoryCountsHead);
			}
			String message = insertOrUpdateImInventoryCounts(inventoryCountsHead, loginUser);

			return message;
		} catch (FormException fe) {
			log.error("盤點單存檔時發生錯誤，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("盤點單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("盤點單存檔時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 判斷是否為新資料，並將盤點資料新增或更新至盤點單主檔及明細檔
	 * 
	 * @param inventoryCountsHead
	 * @param loginUser
	 * @return String
	 * @throws ObtainSerialNoFailedException
	 * @throws FormException
	 * @throws Exception
	 */
	private String insertOrUpdateImInventoryCounts(ImInventoryCountsHead inventoryCountsHead, String loginUser)
	throws ObtainSerialNoFailedException, FormException, Exception {

		UserUtils.setOpUserAndDate(inventoryCountsHead, loginUser);
		//UserUtils.setUserAndDate(inventoryCountsHead.getImInventoryCountsLines(), loginUser);

		if (inventoryCountsHead.getHeadId() == null
				&& !StringUtils.hasText(inventoryCountsHead.getOrderNo())) {

			String serialNo = buOrderTypeService.getOrderSerialNo(
					inventoryCountsHead.getBrandCode(), inventoryCountsHead
					.getOrderTypeCode());
			if (!serialNo.equals("unknow")) {
				inventoryCountsHead.setOrderNo(serialNo);
				insertImInventoryCounts(inventoryCountsHead);
				return inventoryCountsHead.getOrderTypeCode() + "-" + serialNo
				+ "存檔成功！";
			} else {
				throw new ObtainSerialNoFailedException("取得"
						+ inventoryCountsHead.getOrderTypeCode() + "單號失敗！");
			}
		} else {
			imInventoryCountsLineService.deleteInventoryCountsLine(inventoryCountsHead);
			ImInventoryCountsHead inventoryCountsHeadPO = (ImInventoryCountsHead) imInventoryCountsHeadDAO
			.findByPrimaryKey(ImInventoryCountsHead.class, inventoryCountsHead.getHeadId());
			if(inventoryCountsHeadPO == null){
				throw new ValidationErrorException("查無盤點單主鍵：" + inventoryCountsHead.getHeadId() + "的資料！");
			}
			List<ImInventoryCountsLine> actualSaveInventoryCountsLines = new ArrayList(0); 	    
			List<ImInventoryCountsLine> inventoryCountsLines = inventoryCountsHead.getImInventoryCountsLines();
			if(inventoryCountsLines != null){
				for(ImInventoryCountsLine inventoryCountsLine : inventoryCountsLines){
					ImInventoryCountsLine newInventoryCountsLine = new ImInventoryCountsLine();
					BeanUtils.copyProperties(inventoryCountsLine, newInventoryCountsLine);
					newInventoryCountsLine.setImInventoryCountsHead(null);
					actualSaveInventoryCountsLines.add(newInventoryCountsLine);
				}
			}
			String isPrinted = inventoryCountsHeadPO.getIsPrinted();
			Long printedTimes = inventoryCountsHeadPO.getPrintedTimes();
			BeanUtils.copyProperties(inventoryCountsHead, inventoryCountsHeadPO);
			inventoryCountsHeadPO.setIsPrinted(isPrinted);
			inventoryCountsHeadPO.setPrintedTimes(printedTimes);
			inventoryCountsHeadPO.setImInventoryCountsLines(actualSaveInventoryCountsLines);

			modifyImInventoryCounts(inventoryCountsHeadPO);
			return inventoryCountsHead.getOrderTypeCode() + "-"
			+ inventoryCountsHead.getOrderNo() + "存檔成功！";
		}
	}

	/**
	 * 新增至盤點單主檔或明細檔
	 * 
	 * @param saveObj
	 */
	private void insertImInventoryCounts(Object saveObj) {
		imInventoryCountsHeadDAO.save(saveObj);
	}

	/**
	 * 更新至盤點單主檔或明細檔
	 * 
	 * @param updateObj
	 */
	private void modifyImInventoryCounts(Object updateObj) {
		imInventoryCountsHeadDAO.update(updateObj);
	}

	/**
	 * 檢核盤點單主檔資料(ImInventoryCountsHead)
	 * 
	 * @param inventoryCountsHead
	 * @throws ValidationErrorException
	 * @throws NoSuchObjectException
	 */
	private void checkInventoryCountsHead(ImInventoryCountsHead inventoryCountsHead, HashMap conditionMap, 
			String programId, String identification, List errorMsgs) 
	throws ValidationErrorException, NoSuchObjectException{
		String message = null;
		String tabName = "主檔資料";
		try{
			String brandCode = inventoryCountsHead.getBrandCode();
			String warehouseCode = inventoryCountsHead.getWarehouseCode();
			String superintendentCode = inventoryCountsHead.getSuperintendentCode();
			String actualSuperintendentCode = inventoryCountsHead.getActualSuperintendentCode();

			if(inventoryCountsHead.getCountsDate() == null){
				message = "請輸入" + tabName + "的實際盤點日！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
						identification, message, inventoryCountsHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);	        
			}

			if(inventoryCountsHead.getInventoryDate() == null){
				message = "請輸入" + tabName + "的庫存日期！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
						identification, message, inventoryCountsHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);	        
			}

			if(!StringUtils.hasText(warehouseCode)){
				message = "請輸入" + tabName + "的庫別！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
						identification, message, inventoryCountsHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);        
			}else{
				warehouseCode = warehouseCode.trim().toUpperCase();
				inventoryCountsHead.setWarehouseCode(warehouseCode);
				if(imWarehouseDAO.findByBrandCodeAndWarehouseCode(inventoryCountsHead.getBrandCode(), warehouseCode, null) == null){
					message = "查無" + tabName + "，庫別: " + warehouseCode + " 的庫別資料！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
							identification, message, inventoryCountsHead.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);  
				}
			}

			if(!StringUtils.hasText(inventoryCountsHead.getCountsId())){
				message = "請輸入" + tabName + "的盤點代號！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
						identification, message, inventoryCountsHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}else{
				inventoryCountsHead.setCountsId(inventoryCountsHead.getCountsId().trim().toUpperCase());
			}

			if(!StringUtils.hasText(inventoryCountsHead.getCountsType())){
				message = "請選擇" + tabName + "的盤點方式！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
						identification, message, inventoryCountsHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);      
			}else if("2".equals(inventoryCountsHead.getCountsType()) && !StringUtils.hasText(inventoryCountsHead.getCountsLotNo())){
				message = "請輸入" + tabName + "的盤點批號！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
						identification, message, inventoryCountsHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);   
			}

			if(StringUtils.hasText(inventoryCountsHead.getCountsLotNo())){
				inventoryCountsHead.setCountsLotNo(inventoryCountsHead.getCountsLotNo().trim().toUpperCase());
				if("2".equals(inventoryCountsHead.getCountsType())){
					if(imInventoryCountsHeadDAO.findInventoryCountsByProperty
							(inventoryCountsHead.getCountsId(), inventoryCountsHead.getCountsLotNo(), inventoryCountsHead.getHeadId()) != null){   
						message = "盤點代號：" + inventoryCountsHead.getCountsId() + "、盤點批號：" + inventoryCountsHead.getCountsLotNo() + "已存在，請勿重複建立！";
						siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
								identification, message, inventoryCountsHead.getLastUpdatedBy());
						errorMsgs.add(message);
						log.error(message);
					}
				}
			}

			if(!StringUtils.hasText(superintendentCode)){
				message = "請輸入" + tabName + "的盤點人！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
						identification, message, inventoryCountsHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);      
			}else{
				superintendentCode = superintendentCode.trim().toUpperCase();
				inventoryCountsHead.setSuperintendentCode(superintendentCode);
				BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, superintendentCode);
				if (employeeWithAddressView == null) {
					message = "查無盤點人：" + superintendentCode + " 的資料！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
							identification, message, inventoryCountsHead.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);  
				} 
			}

			if(StringUtils.hasText(actualSuperintendentCode)){
				actualSuperintendentCode = actualSuperintendentCode.trim().toUpperCase();
				inventoryCountsHead.setActualSuperintendentCode(actualSuperintendentCode);
				BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.
				findbyBrandCodeAndEmployeeCode(brandCode, actualSuperintendentCode);
				if (employeeWithAddressView == null) {
					message = "查無實盤人：" + actualSuperintendentCode + " 的資料！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
							identification, message, inventoryCountsHead.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				} 
			}

		}catch(Exception ex){
			message = "檢核盤點單" + tabName + "時發生錯誤，原因：" + ex.toString();
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, inventoryCountsHead.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		}
	}

	private void checkAfterCounting(ImInventoryCountsHead inventoryCountsHead, HashMap conditionMap, 
			String programId, String identification, List errorMsgs){
		String message = null;
		try{
			// check tax_code
			String loginBrandCode = (String)conditionMap.get("loginBrandCode");
			String orderTypeCode = (String)conditionMap.get("orderTypeCode");
			BuOrderTypeId buOrderTypeId = new BuOrderTypeId();
			buOrderTypeId.setBrandCode(loginBrandCode);
			buOrderTypeId.setOrderTypeCode(orderTypeCode);
			String taxCode = buOrderTypeService.findById(buOrderTypeId).getTaxCode(); 
			Long headId = inventoryCountsHead.getHeadId();
			List<ImInventoryCountsLine> headLineList = imInventoryCountsLineDAO.findByHeadId(headId);

			//檢查商品項次是否正確
			/*
			for(int i=0;i<headLineList.size();i++){
				ImInventoryCountsLine line = headLineList.get(i);
				String itemCode = line.getItemCode();
				ImItem imItem = imItemDAO.findById(itemCode);
				String itemName = imItem.getItemCName();
				Long indexNo = line.getIndexNo();
				if(!taxCode.equals(imItem.getIsTax())){
					message = "項次"+indexNo+"商品"+itemCode+":"+itemName+" 稅別錯誤";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, inventoryCountsHead.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);	  
				}
			}
			 */

			String brandCode = inventoryCountsHead.getBrandCode();
			String actualSuperintendentCode = inventoryCountsHead.getActualSuperintendentCode();
			if(!StringUtils.hasText(actualSuperintendentCode)){
				message = "請輸入實盤人！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
						identification, message, inventoryCountsHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}else{
				actualSuperintendentCode = actualSuperintendentCode.trim().toUpperCase();
				inventoryCountsHead.setActualSuperintendentCode(actualSuperintendentCode);
				BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.
				findbyBrandCodeAndEmployeeCode(brandCode, actualSuperintendentCode);
				if (employeeWithAddressView == null) {
					message = "查無實盤人(" + actualSuperintendentCode + ")的資料！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
							identification, message, inventoryCountsHead.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				} 
			}

			if(inventoryCountsHead.getImInventoryCountsLines() == null || inventoryCountsHead.getImInventoryCountsLines().size() == 0){
				message = "明細資料頁籤無任何盤點資料，請重新匯入盤點檔！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
						identification, message, inventoryCountsHead.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			}	    

		}catch(Exception ex){
			message = "盤點後執行檢核發生錯誤，原因：" + ex.toString();
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, 
					identification, message, inventoryCountsHead.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		}
	}

	/**
	 * 更新盤點明細檔的Status
	 * 
	 * @param inventoryCountsHead
	 * @param status
	 */
	public void updateImInventoryCountsLineStatus(ImInventoryCountsHead inventoryCountsHead, String status){

		List<ImInventoryCountsLine> imInventoryCountsLines = inventoryCountsHead.getImInventoryCountsLines();
		if(imInventoryCountsLines != null && imInventoryCountsLines.size() > 0){
			for(ImInventoryCountsLine imInventoryCountsLine : imInventoryCountsLines){
				imInventoryCountsLine.setStatus(status);
			}
		}	
	}

	/**
	 * 更新盤點單主檔及明細檔的Status
	 * 
	 * @param inventoryCountsHeadId
	 * @param status
	 * @param loginUser
	 * @return String
	 * @throws Exception
	 */
	public String updateImInventoryCountsStatus(Long inventoryCountsHeadId, String status, String loginUser)
	throws Exception {

		try {
			ImInventoryCountsHead inventoryCountsHead = (ImInventoryCountsHead) imInventoryCountsHeadDAO
			.findByPrimaryKey(ImInventoryCountsHead.class, inventoryCountsHeadId);
			if (inventoryCountsHead != null) {
				inventoryCountsHead.setStatus(status);
				inventoryCountsHead.setLastUpdatedBy(loginUser);
				inventoryCountsHead.setLastUpdateDate(new Date());
				List<ImInventoryCountsLine> inventoryCountsLines = inventoryCountsHead.getImInventoryCountsLines();

				if (inventoryCountsLines != null && inventoryCountsLines.size() > 0) {
					for (ImInventoryCountsLine inventoryCountsLine : inventoryCountsLines) {
						inventoryCountsLine.setStatus(status);
						inventoryCountsLine.setLastUpdatedBy(loginUser);
						inventoryCountsLine.setLastUpdateDate(new Date());
					}	    
				}

				modifyImInventoryCounts(inventoryCountsHead);
				return "Success";
			} else {
				throw new NoSuchDataException("盤點單主檔查無主鍵：" + inventoryCountsHeadId + "的資料！");
			}
		} catch (Exception ex) {
			log.error("更新盤點單狀態時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新盤點單狀態時發生錯誤，原因：" + ex.getMessage());

		}
	}

	/**
	 * 依據primary key為查詢條件，取得盤點單主檔
	 * 
	 * @param headId
	 * @return ImInventoryCountsHead
	 * @throws Exception
	 */
	public ImInventoryCountsHead findImInventoryCountsHeadById(Long headId) throws Exception {
		try {
			ImInventoryCountsHead inventoryCountsHead = (ImInventoryCountsHead) imInventoryCountsHeadDAO
			.findByPrimaryKey(ImInventoryCountsHead.class, headId);
			return inventoryCountsHead;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢盤點單主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢盤點單主檔時發生錯誤，原因："
					+ ex.getMessage());
		}
	}

	/**
	 * 依據盤點單查詢螢幕的輸入條件進行查詢
	 * 
	 * @param conditionMap
	 * @return List
	 * @throws Exception
	 */
	public List findInventoryCountsList(HashMap conditionMap) throws Exception {

		try {
			String orderNo_Start = (String) conditionMap.get("orderNo_Start");
			String orderNo_End = (String) conditionMap.get("orderNo_End");	
			String warehouseCode = (String) conditionMap.get("warehouseCode");
			String countsId = (String) conditionMap.get("countsId");
			String countsLotNo_Start = (String) conditionMap.get("countsLotNo_Start");
			String countsLotNo_End = (String) conditionMap.get("countsLotNo_End");

			conditionMap.put("orderNo_Start", orderNo_Start.trim());
			conditionMap.put("orderNo_End", orderNo_End.trim());
			conditionMap.put("warehouseCode", warehouseCode.trim().toUpperCase());
			conditionMap.put("countsId", countsId.trim());
			conditionMap.put("countsLotNo_Start", countsLotNo_Start.trim());
			conditionMap.put("countsLotNo_End", countsLotNo_End.trim());

			return imInventoryCountsHeadDAO.findInventoryCountsList(conditionMap);
		} catch (Exception ex) {
			log.error("查詢盤點單時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢盤點單時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 依據盤點單查詢螢幕的輸入條件進行查詢
	 * 
	 * @param conditionMap
	 * @return List
	 * @throws Exception
	 */
	public List find(HashMap conditionMap) throws Exception {
		return imInventoryCountsHeadDAO.findInventoryCountsList(conditionMap);
	}

	/**
	 * 更新至盤點單主檔或明細檔
	 * 
	 * @param updateObj
	 */
	public void updateImInventoryCounts(Object updateObj) throws Exception{
		try {
			imInventoryCountsHeadDAO.update(updateObj);
		} catch (Exception ex) {
			log.error("更新盤點單時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新盤點單時發生錯誤，原因：" + ex.getMessage());
		}
	}

	public void refreshImInventoryCountsLines(List refreshObjs) {

		for (int i = 0; i < refreshObjs.size(); i++) {
			ImInventoryCountsLine inventoryCountsLine = (ImInventoryCountsLine)refreshObjs.get(i);
			ImItem item = imItemDAO.findById(inventoryCountsLine.getItemCode());
			if(item != null){
				inventoryCountsLine.setItemName(item.getItemCName());
			}
		} 
	}

	private void getOnHandQty(ImInventoryCountsHead inventoryCountsHead, HashMap conditionMap, String loginUser) throws Exception{
		BuOrderTypeId buOrderTypeId = new BuOrderTypeId();
		buOrderTypeId.setBrandCode(inventoryCountsHead.getBrandCode());
		buOrderTypeId.setOrderTypeCode(inventoryCountsHead.getOrderTypeCode());
		String taxCode = buOrderTypeService.findById(buOrderTypeId).getTaxCode();
		BuBrand buBrand = buBrandDAO.findById(inventoryCountsHead.getBrandCode());
		String branchCode = buBrand.getBuBranch().getBranchCode();
		List tmpAppStockStatisticsBeans = tmpAppStockStatisticsService.getStockStatistics(conditionMap);
		List inventoryCountsLines = inventoryCountsHead.getImInventoryCountsLines();
		if(tmpAppStockStatisticsBeans != null && tmpAppStockStatisticsBeans.size() > 0){
			for(int i = 0; i < tmpAppStockStatisticsBeans.size(); i++){
				TmpAppStockStatistics stockStatistic = (TmpAppStockStatistics)tmpAppStockStatisticsBeans.get(i);
				ImInventoryCountsLine inventoryCountsLine = new ImInventoryCountsLine();
				inventoryCountsLine.setItemCode(stockStatistic.getItemCode());
				inventoryCountsLine.setOnHandQty(stockStatistic.getEndOnHandQty());
				inventoryCountsLine.setCountsQty(0D);
				inventoryCountsLine.setCountsQtyFinal(0D);
				ImItem imItem = imItemDAO.findById(stockStatistic.getItemCode());
				//inventoryCountsLine.setStatus(inventoryCountsHead.getStatus());
				//inventoryCountsLine.setCreatedBy(loginUser);
				//inventoryCountsLine.setCreationDate(new Date());
				//inventoryCountsLine.setLastUpdatedBy(loginUser);
				//inventoryCountsLine.setLastUpdateDate(new Date());
				//inventoryCountsLine.setIndexNo(i + 1L);
				//如果是T2的帶量盤點還要判斷稅別
				if("2".equals(branchCode)){
					if(imItem.equals(taxCode))
						inventoryCountsLines.add(inventoryCountsLine);
				}else{
					inventoryCountsLines.add(inventoryCountsLine);
				}
			}
		}
	}

	/**
	 * 依據品牌代號、單別、單號查詢盤點單
	 * 
	 * @param brandCode
	 * @param orderTypeCode
	 * @param orderNo
	 * @return ImInventoryCountsHead
	 * @throws Exception
	 */
	public ImInventoryCountsHead findInventoryCountsByIdentification(String brandCode, String orderTypeCode, String orderNo) throws Exception{

		try{
			return imInventoryCountsHeadDAO.findInventoryCountsByIdentification(brandCode, orderTypeCode, orderNo);
		}catch (Exception ex) {
			log.error("依據品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "查詢盤點單時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "查詢盤點單時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 批次產生盤點單主檔(3.0)
	 * 
	 * @param conditionMap
	 * @param loginUser
	 * @return String
	 * @throws Exception
	 */
	public List <ImInventoryCountsHead> executeBatchCreateImInventoryCounts3(HashMap parameterMap, String loginEmployeeCode) throws Exception {

		try {	    
			//檢核產生盤點主檔必要資訊
			log.info("executeBatchCreateImInventoryCounts3(HashMap parameterMap, String loginEmployeeCode) begin!!");
			checkBatchCreateInventoryCountsData(parameterMap);
			//產生盤點主檔
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean    = parameterMap.get("vatBeanOther");	        	
			//取出參數
			String countsId = (String)PropertyUtils.getProperty(formBindBean, "countsId");
			String warehouseCode = (String)PropertyUtils.getProperty(formBindBean, "warehouseCode");
			String serialNo_begin = (String)PropertyUtils.getProperty(formBindBean, "serialNo_begin");
			String serialNo_end = (String)PropertyUtils.getProperty(formBindBean, "serialNo_end");	    
			String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
			String loginEmployeeCodeString = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			String brandCode = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
			String orderTypeCode = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");
			StringBuffer message = new StringBuffer(orderTypeCode + "-");	    
			int times = Integer.parseInt(serialNo_end) - Integer.parseInt(serialNo_begin) + 1;
			List <ImInventoryCountsHead> imInventoryCountsHeads = new ArrayList<ImInventoryCountsHead>(0);
			for(int i = 0; i < times; i++){
				String actualCountsLotNo = warehouseCode + CommonUtils.
				insertCharacterWithFixLength(String.valueOf(Integer.parseInt(serialNo_begin) + i), 4, CommonUtils.ZERO);
				log.info("盤點測試批號1:"+actualCountsLotNo);
				if(imInventoryCountsHeadDAO.findInventoryCountsByCountsIdAndLotNo(countsId, actualCountsLotNo, null) != null){
					throw new ValidationErrorException("盤點代號：" + countsId + "、盤點批號：" + actualCountsLotNo + "已存在，請勿重複建立！");
				}

				ImInventoryCountsHead inventoryCountsHead = new ImInventoryCountsHead();
				//設定批次的盤點批號
				inventoryCountsHead.setCountsLotNo(actualCountsLotNo);
				//設定批次的盤點單號
				String serialNo = buOrderTypeService.getOrderSerialNo(brandCode, orderTypeCode);
				if (!serialNo.equals("unknow")) {
					inventoryCountsHead.setOrderNo(serialNo);
				} else {
					throw new ObtainSerialNoFailedException("批次取得" + inventoryCountsHead.getOrderTypeCode() + "單號失敗！");
				}    	    

				String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCodeString);
				inventoryCountsHead.setActualSuperintendentName(employeeName);
				inventoryCountsHead.setSuperintendentName(employeeName);
				inventoryCountsHead.setCountsDate(new Date());
				inventoryCountsHead.setInventoryDate(new Date());
				inventoryCountsHead.setStatus(formStatus);
				inventoryCountsHead.setCountsType("2");			
				inventoryCountsHead.setIsImportedFile("N");
				inventoryCountsHead.setImportedTimes(0L);
				inventoryCountsHead.setIsPrinted("N");
				inventoryCountsHead.setPrintedTimes(0L);    	    

				inventoryCountsHead.setCreatedBy(loginEmployeeCodeString);
				inventoryCountsHead.setCreationDate(new Date());
				inventoryCountsHead.setLastUpdatedBy(loginEmployeeCodeString);
				inventoryCountsHead.setLastUpdateDate(new Date());			
				/*			
			inventoryCountsHead.setLastUpdatedBy(loginEmployeeCodeString);
			inventoryCountsHead.setLastUpdateDate(DateUtils.parseDate(DateUtils.format(new Date())));    	    
				 */
				AjaxUtils.copyJSONBeantoPojoBean(formBindBean, inventoryCountsHead);
				insertImInventoryCounts(inventoryCountsHead);    		    	    
				imInventoryCountsHeads.add(inventoryCountsHead);
				if(i == 0){
					message.append(inventoryCountsHead.getOrderNo());
				}else if(i == (times - 1)){
					message.append("~" + inventoryCountsHead.getOrderNo());
				}    	    
			}

			return imInventoryCountsHeads;

		} catch (FormException fe) {
			log.error("批次產生盤點單時發生錯誤，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			//log.error("批次產生盤點單時發生錯誤，原因：" + ex.toString());
			throw new Exception("批次產生盤點單時發生錯誤，原因：" + ex.getMessage());
		}
	}  

	/**
	 * 批次產生盤點單主檔
	 * 
	 * @param conditionMap
	 * @param loginUser
	 * @return String
	 * @throws Exception
	 */
	public String executeBatchCreateImInventoryCounts(HashMap parameterMap, String loginUser) throws Exception {

		try {	    
			//檢核產生盤點主檔必要資訊
			checkBatchCreateInventoryCountsInfo(parameterMap);
			//產生盤點主檔
			List inventoryCounts = produceInventoryCounts(parameterMap);
			String orderTypeCode = (String)parameterMap.get("orderTypeCode");
			StringBuffer message = new StringBuffer(orderTypeCode + "-");
			for(int i = 0; i < inventoryCounts.size(); i++){
				ImInventoryCountsHead inventoryCountsHead = (ImInventoryCountsHead)inventoryCounts.get(i);
				insertOrUpdateImInventoryCounts(inventoryCountsHead, loginUser);
				if(i == 0){
					message.append(inventoryCountsHead.getOrderNo());
				}else if(i == (inventoryCounts.size() - 1)){
					message.append("~" + inventoryCountsHead.getOrderNo());
				}	
			}
			message.append("存檔成功！"); 
			return message.toString();
		} catch (FormException fe) {
			log.error("批次產生盤點單時發生錯誤，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("批次產生盤點單時發生錯誤，原因：" + ex.toString());
			throw new Exception("批次產生盤點單時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 盤點單批次建立檢核(3.0)
	 * 
	 * @param parameterMap
	 * @throws ValidationErrorException
	 * @throws NoSuchObjectException
	 */    
	private void checkBatchCreateInventoryCountsData(HashMap parameterMap) 
	throws ValidationErrorException, NoSuchObjectException, Exception{

		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");
			String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");    	    
			String orderTypeCode = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");
			String countsDate = (String)PropertyUtils.getProperty(formBindBean, "countsDate");
			String countsId = (String)PropertyUtils.getProperty(formBindBean, "countsId");
			String superintendentCode = (String)PropertyUtils.getProperty(formBindBean, "superintendentCode");
			String actualSuperintendentCode = (String)PropertyUtils.getProperty(formBindBean, "actualSuperintendentCode");
			String warehouseCode = (String)PropertyUtils.getProperty(formBindBean, "warehouseCode");
			String serialNo_begin = (String)PropertyUtils.getProperty(formBindBean, "serialNo_begin");
			String serialNo_end = (String)PropertyUtils.getProperty(formBindBean, "serialNo_end");

			if ( OrderStatus.COUNTING.equals(formStatus) ) {
				if(!StringUtils.hasText(orderTypeCode)){
					throw new ValidationErrorException("請選擇單別！ ！");
				}else if(!StringUtils.hasText(countsDate)){
					throw new ValidationErrorException("請輸入實際盤點日！");
				}else if(!StringUtils.hasText(countsId)){
					throw new ValidationErrorException("請輸入盤點代號！");
				}else{
					countsId = countsId.trim().toUpperCase();
					PropertyUtils.setProperty(formBindBean, "countsId", countsId);
				}

				if(!StringUtils.hasText(superintendentCode)){
					throw new ValidationErrorException("請輸入盤點人！");
				}else{
					superintendentCode = superintendentCode.trim().toUpperCase();
					PropertyUtils.setProperty(formBindBean, "superintendentCode", superintendentCode);
					BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findById(superintendentCode);
					if (employeeWithAddressView == null) {
						throw new NoSuchObjectException("查無盤點人：" + superintendentCode + "的資料！");
					} 
				}

				if(StringUtils.hasText(actualSuperintendentCode)){
					actualSuperintendentCode = actualSuperintendentCode.trim().toUpperCase();
					PropertyUtils.setProperty(formBindBean, "superintendentCode", superintendentCode);
					BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findById(actualSuperintendentCode);
					if (employeeWithAddressView == null) {
						throw new NoSuchObjectException("查無實盤人：" + superintendentCode + "的資料！");
					} 
				}

				if(!StringUtils.hasText(warehouseCode)){
					throw new ValidationErrorException("請輸入庫別！");
				}else{
					warehouseCode = warehouseCode.trim().toUpperCase();
					PropertyUtils.setProperty(formBindBean, "warehouseCode", warehouseCode);
					if(imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, warehouseCode)== null){
						throw new NoSuchObjectException("查無庫別資料！");
					}
				}  
			}

			if(!StringUtils.hasText(serialNo_begin)){
				throw new ValidationErrorException("請輸入流水號的起始號碼！");
			}else if(!ValidateUtil.isNumber(serialNo_begin)){
				throw new ValidationErrorException("流水號的起始號碼必須為數字！");
			}else if(Integer.parseInt(serialNo_begin) <= 0){
				throw new ValidationErrorException("流水號的起始號碼必須大於零！");
			}

			if(!StringUtils.hasText(serialNo_end)){
				throw new ValidationErrorException("請輸入流水號的結束號碼！");
			}else if(!ValidateUtil.isNumber(serialNo_end)){
				throw new ValidationErrorException("流水號的結束號碼必須為數字！");
			}else if(Integer.parseInt(serialNo_end) <= 0){
				throw new ValidationErrorException("流水號的結束號碼必須大於零！");
			}

			if(Integer.parseInt(serialNo_begin) > Integer.parseInt(serialNo_end)){
				throw new ValidationErrorException("流水號的起始號碼不可大於結束號碼！");
			}
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}    


	/**
	 * 盤點單批次建立檢核
	 * 
	 * @param parameterMap
	 * @throws ValidationErrorException
	 * @throws NoSuchObjectException
	 */
	private void checkBatchCreateInventoryCountsInfo(HashMap parameterMap) 
	throws ValidationErrorException, NoSuchObjectException{

		String orderTypeCode = (String)parameterMap.get("orderTypeCode");
		Date countsDate = (Date)parameterMap.get("countsDate");
		String countsId = (String)parameterMap.get("countsId");
		String superintendentCode = (String)parameterMap.get("superintendentCode");
		String actualSuperintendentCode = (String)parameterMap.get("actualSuperintendentCode");
		String warehouseCode = (String)parameterMap.get("warehouseCode");
		String serialNo_begin = (String)parameterMap.get("serialNo_begin");
		String serialNo_end = (String)parameterMap.get("serialNo_end");	

		if(!StringUtils.hasText(orderTypeCode)){
			throw new ValidationErrorException("請選擇單別！");
		}else if(countsDate == null){
			throw new ValidationErrorException("請輸入實際盤點日！");
		}else if(!StringUtils.hasText(countsId)){
			throw new ValidationErrorException("請輸入盤點代號！");
		}else{
			countsId = countsId.trim().toUpperCase();
			parameterMap.put("countsId", countsId);
		}

		if(!StringUtils.hasText(superintendentCode)){
			throw new ValidationErrorException("請輸入盤點人！");
		}else{
			superintendentCode = superintendentCode.trim().toUpperCase();
			parameterMap.put("superintendentCode", superintendentCode);
			BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO
			.findById(superintendentCode);
			if (employeeWithAddressView == null) {
				throw new NoSuchObjectException("查無盤點人：" + superintendentCode + "的資料！");
			} 
		}

		if(StringUtils.hasText(actualSuperintendentCode)){
			actualSuperintendentCode = actualSuperintendentCode.trim().toUpperCase();
			parameterMap.put("actualSuperintendentCode", actualSuperintendentCode);
			BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO
			.findById(actualSuperintendentCode);
			if (employeeWithAddressView == null) {
				throw new NoSuchObjectException("查無實盤人：" + superintendentCode + "的資料！");
			} 
		}

		if(!StringUtils.hasText(warehouseCode)){
			throw new ValidationErrorException("請輸入庫別！");
		}else{
			warehouseCode = warehouseCode.trim().toUpperCase();
			parameterMap.put("warehouseCode", warehouseCode);
			if(imWarehouseDAO.findByPrimaryKey(ImWarehouse.class, warehouseCode)== null){
				throw new NoSuchObjectException("查無庫別資料！");
			}
		}

		if(!StringUtils.hasText(serialNo_begin)){
			throw new ValidationErrorException("請輸入流水號的起始號碼！");
		}else if(!ValidateUtil.isNumber(serialNo_begin)){
			throw new ValidationErrorException("流水號的起始號碼必須為數字！");
		}else if(Integer.parseInt(serialNo_begin) <= 0){
			throw new ValidationErrorException("流水號的起始號碼必須大於零！");
		}

		if(!StringUtils.hasText(serialNo_end)){
			throw new ValidationErrorException("請輸入流水號的結束號碼！");
		}else if(!ValidateUtil.isNumber(serialNo_end)){
			throw new ValidationErrorException("流水號的結束號碼必須為數字！");
		}else if(Integer.parseInt(serialNo_end) <= 0){
			throw new ValidationErrorException("流水號的結束號碼必須大於零！");
		}

		if(Integer.parseInt(serialNo_begin) > Integer.parseInt(serialNo_end)){
			throw new ValidationErrorException("流水號的起始號碼不可大於結束號碼！");
		}
	}

	private List produceInventoryCounts(HashMap parameterMap) throws ValidationErrorException{

		List inventoryCounts = new ArrayList(0);
		//取出設值參數
		String brandCode = (String)parameterMap.get("brandCode");
		String orderTypeCode = (String)parameterMap.get("orderTypeCode");
		Date countsDate = (Date)parameterMap.get("countsDate");
		String countsId = (String)parameterMap.get("countsId");
		String superintendentCode = (String)parameterMap.get("superintendentCode");
		String actualSuperintendentCode = (String)parameterMap.get("actualSuperintendentCode");
		String warehouseCode = (String)parameterMap.get("warehouseCode");
		String serialNo_begin = (String)parameterMap.get("serialNo_begin");
		String serialNo_end = (String)parameterMap.get("serialNo_end");
		//String description = (String)parameterMap.get("description");
		String status = (String)parameterMap.get("status");
		Date inventoryDate = (Date)parameterMap.get("inventoryDate");

		int times = Integer.parseInt(serialNo_end) - Integer.parseInt(serialNo_begin) + 1;
		for(int i = 0; i < times; i++){
			String actualCountsLotNo = warehouseCode + CommonUtils.
			insertCharacterWithFixLength(String.valueOf(Integer.parseInt(serialNo_begin) + i), 4, CommonUtils.ZERO);
			log.info("盤點測試批號2:"+actualCountsLotNo);
			if(imInventoryCountsHeadDAO.findInventoryCountsByCountsIdAndLotNo(countsId, actualCountsLotNo, null) != null){
				throw new ValidationErrorException("盤點代號：" + countsId + "、盤點批號：" + actualCountsLotNo + "已存在，請勿重複建立！");
			}
			ImInventoryCountsHead inventoryCountsHead = new ImInventoryCountsHead();
			inventoryCountsHead.setBrandCode(brandCode);
			inventoryCountsHead.setOrderTypeCode(orderTypeCode);
			inventoryCountsHead.setCountsDate(countsDate);
			inventoryCountsHead.setInventoryDate(inventoryDate);
			inventoryCountsHead.setWarehouseCode(warehouseCode);
			inventoryCountsHead.setCountsId(countsId);
			inventoryCountsHead.setCountsType("2");
			inventoryCountsHead.setCountsLotNo(actualCountsLotNo);
			inventoryCountsHead.setSuperintendentCode(superintendentCode);
			inventoryCountsHead.setActualSuperintendentCode(actualSuperintendentCode);
			//inventoryCountsHead.setDescription(description);
			inventoryCountsHead.setIsImportedFile("N");
			inventoryCountsHead.setImportedTimes(0L);
			inventoryCountsHead.setIsPrinted("N");
			inventoryCountsHead.setPrintedTimes(0L);
			inventoryCountsHead.setStatus(status);
			inventoryCounts.add(inventoryCountsHead);
		}

		return inventoryCounts;
	}

	public void deleteImInventoryCountsLines(List imInventoryCountsLines) throws Exception{

		try {
			for(int i = 0; i < imInventoryCountsLines.size(); i++){
				imInventoryCountsLineDAO.delete(imInventoryCountsLines.get(i));
			}

		} catch (Exception ex) {
			log.error("刪除盤點單明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("刪除盤點單明細時發生錯誤！");
		}
	}

	/**
	 * 依據盤點代號、盤點批號查詢盤點單
	 * 
	 * @param countsId
	 * @param countsLotNo
	 * @return ImInventoryCountsHead
	 */
	public ImInventoryCountsHead findInventoryCountsByCountsIdAndLotNo(
			String countsId, String countsLotNo) throws Exception{

		try{
			return imInventoryCountsHeadDAO.findInventoryCountsByCountsIdAndLotNo(countsId, countsLotNo, null);
		}catch (Exception ex) {
			log.error("依據盤點代號：" + countsId + "、盤點批號：" + countsLotNo + "查詢盤點單時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據盤點代號：" + countsId + "、盤點批號：" + countsLotNo + "查詢盤點單時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 更新盤點單列印次數
	 * 
	 * @param headId
	 * @throws Exception
	 */
	public void updateInventoryCountsForPrint(String id) throws Exception{

		try{
			if(StringUtils.hasText(id)){
				Long headId = Long.valueOf(id);
				ImInventoryCountsHead inventoryCountsHead = (ImInventoryCountsHead) imInventoryCountsHeadDAO
				.findByPrimaryKey(ImInventoryCountsHead.class, headId);
				if(inventoryCountsHead != null){
					inventoryCountsHead.setIsPrinted("Y");
					Long printedTimes = inventoryCountsHead.getPrintedTimes();
					if(printedTimes == null){
						printedTimes = 0L;
					}
					printedTimes += 1;
					inventoryCountsHead.setPrintedTimes(printedTimes);
					modifyImInventoryCounts(inventoryCountsHead);
				}
			}
		}catch (Exception ex) {
			log.error("依據主鍵：" + id + "更新盤點單列印次數時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + id + "更新盤點單列印次數時發生錯誤，原因："
					+ ex.getMessage());
		}
	}

	/**
	 * 執行盤點單初始化
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws Exception
	 */
	public Map executeInitial(Map parameterMap) throws Exception{

		HashMap resultMap = new HashMap();
		try{
			HashMap argumentMap = getRequestParameter(parameterMap, false);
			Long formId = (Long)argumentMap.get("formId");
			String orderTypeCode      = (String)argumentMap.get("orderTypeCode");
			String loginBrandCode     = (String)argumentMap.get("loginBrandCode");
			BuOrderTypeId buOrderTypeId = new BuOrderTypeId();
			buOrderTypeId.setBrandCode(loginBrandCode);
			buOrderTypeId.setOrderTypeCode(orderTypeCode);
			BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
			BuCommonPhraseLine taxName = buCommonPhraseService.getBuCommonPhraseLine("TaxCatalog", buOrderType.getTaxCode());
			resultMap.put("taxCode", buOrderType.getTaxCode());
			resultMap.put("taxName", taxName.getName());
			ImInventoryCountsHead form = null;
			//建立暫存單
			if(formId == null)
				form = createNewImInventoryCounts(argumentMap, resultMap);
			else
				form = findImInventoryCounts(argumentMap, resultMap);

			//把庫別人名等等塞入
			refreshImInventoryCounts(form);
			Map multiList = new HashMap(0);
			List<BuCommonPhraseLine> allCountsType = buCommonPhraseService.getCommonPhraseLinesById("InventoryCountsType", false);
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(form.getBrandCode() ,"ICF");
			multiList.put("allCountsType", AjaxUtils.produceSelectorData(allCountsType, "lineCode", "name", false, false));
			multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));	    
			resultMap.put("multiList",multiList);

			return resultMap;       	
		}catch (Exception ex) {
			log.error("盤點單初始化失敗，原因：" + ex.toString());
			throw new Exception("盤點單初始化失敗，原因：" + ex.toString());
		}           
	}

	/**
	 * 取得暫時單號存檔
	 * 
	 * @param inventoryCountsHead
	 * @throws Exception
	 */
	public void saveTmp(ImInventoryCountsHead inventoryCountsHead) throws Exception{

		try{
			String tmpOrderNo = AjaxUtils.getTmpOrderNo();
			inventoryCountsHead.setOrderNo(tmpOrderNo);
			inventoryCountsHead.setLastUpdateDate(new Date());
			inventoryCountsHead.setCreationDate(new Date());
			imInventoryCountsHeadDAO.save(inventoryCountsHead);	    
		}catch(Exception ex){
			log.error("取得暫時單號儲存盤點單發生錯誤，原因：" + ex.toString());
			throw new Exception("取得暫時單號儲存盤點單發生錯誤！");
		}	
	}

	public Long getInventoryCountsHeadId(Object bean) throws FormException, Exception{

		Long headId = null;
		String id = (String)PropertyUtils.getProperty(bean, "headId");
		System.out.println("headId=" + id);
		if(StringUtils.hasText(id)){
			headId = NumberUtils.getLong(id);
		}else{
			throw new ValidationErrorException("傳入的盤點單主鍵為空值！");
		}

		return headId;
	}

	public ImInventoryCountsHead getActualInventoryCounts(Long headId) throws FormException, Exception{

		ImInventoryCountsHead inventoryCountsHeadPO  = findImInventoryCountsHeadById(headId);
		if(inventoryCountsHeadPO == null){
			throw new NoSuchObjectException("查無盤點單主鍵：" + headId + "的資料！");
		}
		return inventoryCountsHeadPO;
	}

	private HashMap getRequestParameter(Map parameterMap, boolean isSubmitAction) throws Exception{

		Object otherBean = parameterMap.get("vatBeanOther");
		String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");		
		String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
		String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
		Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
		HashMap conditionMap = new HashMap();
		conditionMap.put("loginBrandCode", loginBrandCode);
		conditionMap.put("loginEmployeeCode", loginEmployeeCode);
		conditionMap.put("orderTypeCode", orderTypeCode);
		conditionMap.put("formId", formId);
		if(isSubmitAction){
			String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
			conditionMap.put("beforeChangeStatus", beforeChangeStatus);
			conditionMap.put("formStatus", formStatus);
		}

		return conditionMap;
	}

	/** 產生一筆暫存資訊
	 * 
	 * 
	 * 
	 **/
	public ImInventoryCountsHead createNewImInventoryCounts(Map argumentMap, Map resultMap) throws Exception {

		try{

			String loginBrandCode     = (String)argumentMap.get("loginBrandCode");		
			String loginEmployeeCode  = (String)argumentMap.get("loginEmployeeCode");
			String orderTypeCode      = (String)argumentMap.get("orderTypeCode");

			ImInventoryCountsHead form = new ImInventoryCountsHead();
			form.setBrandCode(loginBrandCode);
			form.setOrderTypeCode(orderTypeCode);
			form.setCountsDate(DateUtils.parseDate(DateUtils.format(new Date())));
			form.setInventoryDate(DateUtils.parseDate(DateUtils.format(new Date())));
			form.setSuperintendentCode(loginEmployeeCode);
			form.setActualSuperintendentCode(loginEmployeeCode);	    
			form.setStatus(OrderStatus.SAVE);
			form.setCreatedBy(loginEmployeeCode);
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setCountsType("2");
			form.setIsImportedFile("N");
			form.setImportedTimes(0L);
			form.setIsPrinted("N");
			form.setPrintedTimes(0L);	    
			saveTmp(form);
			resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			resultMap.put("brandName", buBrandDAO.findById(form.getBrandCode()).getBrandName());
			resultMap.put("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));
			resultMap.put("form", form);

			return form;
		}catch (Exception ex) {
			log.error("產生新盤點單失敗，原因：" + ex.toString());
			throw new Exception("產生新盤點單發生錯誤！");
		} 	
	}

	public ImInventoryCountsHead findImInventoryCounts(Map argumentMap, Map resultMap) 
	throws FormException, Exception {

		try{
			Long formId = (Long)argumentMap.get("formId");
			ImInventoryCountsHead form = findImInventoryCountsHeadById(formId);
			if(form != null){
				resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
				resultMap.put("brandName", buBrandDAO.findById(form.getBrandCode()).getBrandName());
				resultMap.put("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode(form.getLastUpdatedBy()));
				resultMap.put("form", form);

				return form;
			}else{
				throw new NoSuchObjectException("查無盤點單主鍵：" + formId + "的資料！");
			}	    
		}catch (FormException fe) {
			log.error("查詢盤點單失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		}catch (Exception ex) {
			log.error("查詢盤點單發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢盤點單發生錯誤！");
		}	
	}

	/**
	 * 更新盤點單
	 * 
	 * @param form
	 */
	private void refreshImInventoryCounts(ImInventoryCountsHead form){
log.info("T1");
		String brandCode = form.getBrandCode();
		//庫別
		String warehouseName = "";
		if(StringUtils.hasText(form.getWarehouseCode())){
			ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, form.getWarehouseCode(), null);
			log.info("T2");
			if(imWarehouse == null){
				warehouseName = "查無此庫別資料";
			}else{
				warehouseName = imWarehouse.getWarehouseName();
			}
		}
		form.setWarehouseName(warehouseName);
	
		//盤點人
		String superintendentName = "";
		if(StringUtils.hasText(form.getSuperintendentCode())){        
			BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.
			findbyBrandCodeAndEmployeeCode(brandCode, form.getSuperintendentCode());
			if(employeeWithAddressView == null) {
				superintendentName = "查無此員工資料";
			}else{
				superintendentName = employeeWithAddressView.getChineseName();
			}
		}	
		form.setSuperintendentName(superintendentName);

		//實盤人
		String actualSuperintendentName = "";
		if(StringUtils.hasText(form.getActualSuperintendentCode())){          
			BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.
			findbyBrandCodeAndEmployeeCode(brandCode, form.getActualSuperintendentCode());
			if(employeeWithAddressView == null) {
				actualSuperintendentName = "查無此員工資料";
			}else{
				actualSuperintendentName = employeeWithAddressView.getChineseName();
			}
		}	
		form.setActualSuperintendentName(actualSuperintendentName);	
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
			HashMap map = new HashMap();
			map.put("headId", headId);
			map.put("startPage", iSPage);
			map.put("pageSize", iPSize);
			map.put("brandCode", brandCode);
			//======================取得頁面所需資料===========================
			List<ImInventoryCountsLine> inventoryCountsLines = imInventoryCountsLineDAO.findPageLine(headId, iSPage, iPSize);
			if(inventoryCountsLines != null && inventoryCountsLines.size() > 0){
				// 取得第一筆的INDEX
				Long firstIndex = inventoryCountsLines.get(0).getIndexNo();
				// 取得最後一筆 INDEX
				Long maxIndex = imInventoryCountsLineDAO.findPageLineMaxIndex(headId);
				refreshItemData(map, inventoryCountsLines);
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, inventoryCountsLines, gridDatas,
						firstIndex, maxIndex));	
			}else{
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));
			}

			return result;
		}catch(Exception ex){
			log.error("載入頁面顯示的盤點明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的盤點明細失敗！");
		}
	}

	/**
	 * 更新盤點單商品明細資料
	 * 
	 * @param parameterMap
	 * @param inventoryCountsLines
	 */
	private void refreshItemData(HashMap parameterMap, List<ImInventoryCountsLine> inventoryCountsLines){

		for(ImInventoryCountsLine inventoryCountsLine : inventoryCountsLines){
			getInventoryCountsItemRelationData(parameterMap, inventoryCountsLine);
		}
	}

	private void getInventoryCountsItemRelationData(HashMap parameterMap, ImInventoryCountsLine inventoryCountsLine){

		String brandCode = (String)parameterMap.get("brandCode");
		String itemName = "查無此商品資料";
		String itemCode = inventoryCountsLine.getItemCode();
		//品名
		if(StringUtils.hasText(itemCode)){
			ImItem itemPO = imItemDAO.findItem(brandCode, itemCode);
			if(itemPO != null){
				inventoryCountsLine.setItemName(itemPO.getItemCName());
			}else{
				inventoryCountsLine.setItemName(itemName);
			}      
		}else{
			inventoryCountsLine.setItemName("");
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
		    //String status = httpRequest.getProperty("status");
		    String employeeCode = httpRequest.getProperty("employeeCode");
		    String errorMsg = null;
		    String uuid = UUID.randomUUID().toString();
		    Date updateDate = new Date();

		    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		    if(headId == null){
			throw new ValidationErrorException("傳入的盤點單主鍵為空值！");
		    }

		    ImInventoryCountsHead head = findImInventoryCountsHeadById(headId);
		    int indexNo = imInventoryCountsLineDAO.findPageLineMaxIndex(headId).intValue();	// 取得LINE MAX INDEX

		    String canBeMod = httpRequest.getProperty("canBeMod");
		    
		    // 將STRING資料轉成List Properties record data
		    List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
		    if (upRecords != null) {
			for (Properties upRecord : upRecords) {
			    // 先載入HEAD_ID OR LINE DATA
			    Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
			    String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);
			    Double countsQty =  NumberUtils.getDouble((upRecord.getProperty(GRID_FIELD_NAMES[4])));
			    String isDeleteRecord =  upRecord.getProperty(GRID_FIELD_NAMES[9]);
			    //String description = upRecord.getProperty(GRID_FIELD_NAMES[6]);

			    if (StringUtils.hasText(itemCode)) {			  
				ImInventoryCountsLine inventoryCountsLinePO = imInventoryCountsLineDAO.findItemByIdentification(headId, lineId);
				if(inventoryCountsLinePO != null){
				    //在COUNTING與FINISH的時候可以修改明細數量 並且記錄
				    if(0 != countsQty.compareTo(inventoryCountsLinePO.getCountsQty()) &&
					    ( OrderStatus.COUNTING.equals(head.getStatus()) || (OrderStatus.FINISH.equals(head.getStatus()))&&"Y".equals(canBeMod)) ){
					log.info("修改明細");
					SiSystemLogUtils.createSystemLog(PROGRAM_ID, itemCode, 
						"明細項次  "+inventoryCountsLinePO.getIndexNo()+" 數量由 "+inventoryCountsLinePO.getCountsQty()+" 修改為 " + countsQty,
						updateDate, head.getCountsId() + head.getCountsLotNo(), employeeCode, head.getBrandCode());

					inventoryCountsLinePO.setCountsQty(countsQty);
					inventoryCountsLinePO.setCountsQtyFinal(countsQty);
					//inventoryCountsLinePO.setDescription(description);
					inventoryCountsLinePO.setLastUpdateDate(updateDate);
					inventoryCountsLinePO.setLastUpdatedBy(employeeCode);
					imInventoryCountsLineDAO.update(inventoryCountsLinePO);

				    }else if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(isDeleteRecord) && 
					    OrderStatus.FINISH.equals(head.getStatus()) && "Y".equals(canBeMod)){
					log.info("刪除明細");
					inventoryCountsLinePO.setIsDeleteRecord(isDeleteRecord);
					imInventoryCountsLineDAO.update(inventoryCountsLinePO);
				    }
				}else{

				    //在FINISH的時候可以新增明細與數量 並且記錄
				    if(OrderStatus.FINISH.equals(head.getStatus()) && "Y".equals(canBeMod)){
					indexNo++;
					inventoryCountsLinePO = new ImInventoryCountsLine();
					inventoryCountsLinePO.setImInventoryCountsHead(head);
					inventoryCountsLinePO.setItemCode(itemCode);
					inventoryCountsLinePO.setCountsQty(countsQty);
					inventoryCountsLinePO.setCountsQtyFinal(countsQty);
					inventoryCountsLinePO.setIndexNo(Long.valueOf(indexNo));
					inventoryCountsLinePO.setLastUpdateDate(updateDate);
					inventoryCountsLinePO.setLastUpdatedBy(employeeCode);
					inventoryCountsLinePO.setCreationDate(updateDate);
					inventoryCountsLinePO.setCreatedBy(employeeCode);
					imInventoryCountsLineDAO.save(inventoryCountsLinePO);

					SiSystemLogUtils.createSystemLog(PROGRAM_ID, itemCode, 
						"新增明細項次  "+inventoryCountsLinePO.getIndexNo()+" 數量為 " + countsQty,
						updateDate, head.getCountsId() + head.getCountsLotNo(), employeeCode, head.getBrandCode());
				    }
				}
			    }
			}
		    }		

		    return AjaxUtils.getResponseMsg(errorMsg);
		}catch(Exception ex){
		    log.error("更新盤點明細時發生錯誤，原因：" + ex.toString());
		    throw new Exception("更新盤點明細失敗！"); 
		}	
	}

	/**
	 * 將盤點資料更新至盤點單主檔及明細檔(AJAX)
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws Exception
	 */
	public Map updateAJAXInventoryCounts(Map parameterMap) throws FormException, Exception {
		HashMap resultMap = new HashMap();
		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Long headId = getInventoryCountsHeadId(formLinkBean);
			ImInventoryCountsHead inventoryCountsHead = getActualInventoryCounts(headId);         	    
			//====================取得條件資料======================
			HashMap conditionMap = getRequestParameter(parameterMap, true);
			String beforeChangeStatus = (String)conditionMap.get("beforeChangeStatus");
			String formStatus = (String)conditionMap.get("formStatus");
			String loginEmployeeCode = (String)conditionMap.get("loginEmployeeCode");
			if(OrderStatus.SAVE.equals(formStatus)){
				//刪除於SI_PROGRAM_LOG的原識別碼資料
				String identification = MessageStatus.getIdentification(inventoryCountsHead.getBrandCode(), 
						inventoryCountsHead.getOrderTypeCode(), inventoryCountsHead.getOrderNo());
				siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
				AjaxUtils.copyJSONBeantoPojoBean(formBindBean, inventoryCountsHead); 
			}else if(OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.COUNTING.equals(formStatus) 
					&& "1".equals(inventoryCountsHead.getCountsType())){
				//狀態從SAVE變成COUNTING時call stored procedure 取庫存量(帶量盤點)
				inventoryCountsHead.setImInventoryCountsLines(new ArrayList(0));
				HashMap map = new HashMap();
				map.put("brandCode", inventoryCountsHead.getBrandCode());
				map.put("warehouseCode", inventoryCountsHead.getWarehouseCode());
				map.put("onHandDate", DateUtils.format(inventoryCountsHead.getInventoryDate(), DateUtils.C_DATA_PATTON_YYYYMMDD));
				getOnHandQty(inventoryCountsHead, map, loginEmployeeCode);
			}	

			inventoryCountsHead.setStatus(formStatus);
			String resultMsg = modifyAjaxImInventoryCountsHead(inventoryCountsHead, loginEmployeeCode);	    	    
			resultMap.put("entityBean", inventoryCountsHead);
			resultMap.put("resultMsg", resultMsg);
			return resultMap;
		}catch (FormException fe) {
			log.error("盤點單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("盤點單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("盤點單存檔時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 暫存單號取實際單號並更新至盤點單主檔
	 * 
	 * @param deliveryHead
	 * @param loginUser
	 * @return String
	 * @throws ObtainSerialNoFailedException
	 * @throws FormException
	 * @throws Exception
	 */
	private String modifyAjaxImInventoryCountsHead(ImInventoryCountsHead inventoryCountsHead, String loginUser)
	throws ObtainSerialNoFailedException, FormException, Exception {
		if (AjaxUtils.isTmpOrderNo(inventoryCountsHead.getOrderNo())) {
			String serialNo = buOrderTypeService.getOrderSerialNo(
					inventoryCountsHead.getBrandCode(), inventoryCountsHead.getOrderTypeCode());
			if (!serialNo.equals("unknow")) {
				inventoryCountsHead.setOrderNo(serialNo);
			} else {
				throw new ObtainSerialNoFailedException("取得"
						+ inventoryCountsHead.getOrderTypeCode() + "單號失敗！");
			}
		}	
		modifyImInventoryCounts(inventoryCountsHead, loginUser);	
		return inventoryCountsHead.getOrderTypeCode() + "-" + inventoryCountsHead.getOrderNo() + "存檔成功！";
	}

	/**
	 * 更新至盤點單主檔
	 * 
	 * @param updateObj
	 * @param loginUser
	 */
	private void modifyImInventoryCounts(ImInventoryCountsHead updateObj, String loginUser) {
		updateObj.setLastUpdatedBy(loginUser);
		updateObj.setLastUpdateDate(new Date());
		imInventoryCountsHeadDAO.update(updateObj);
	}

	/**
	 *  取單號後更新盤點單主檔
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws FormException
	 * @throws Exception
	 */
	public Map updateInventoryCountsWithActualOrderNO(Map parameterMap) throws FormException, Exception {
		HashMap resultMap = new HashMap();
		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			Long headId = getInventoryCountsHeadId(formLinkBean);
			String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			//取得欲更新的bean
			ImInventoryCountsHead inventoryCountsHead = getActualInventoryCounts(headId);
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, inventoryCountsHead);	    
			String resultMsg = modifyAjaxImInventoryCountsHead(inventoryCountsHead, employeeCode);
			resultMap.put("entityBean", inventoryCountsHead);
			resultMap.put("resultMsg", resultMsg);

			return resultMap;      
		} catch (FormException fe) {
			log.error("盤點單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("盤點單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("盤點單存檔時發生錯誤，原因：" + ex.getMessage());
		}
	}   

	/**
	 * 更新檢核後的盤點單
	 * 
	 * @param parameterMap
	 * @return List
	 * @throws Exception
	 */
	public List updateCheckedInventoryCountsData(Map parameterMap) throws Exception {
		List errorMsgs = new ArrayList(0);
		try{
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Long headId = getInventoryCountsHeadId(formLinkBean);
			ImInventoryCountsHead inventoryCountsHead = getActualInventoryCounts(headId);
			String identification = MessageStatus.getIdentification(inventoryCountsHead.getBrandCode(), 
					inventoryCountsHead.getOrderTypeCode(), inventoryCountsHead.getOrderNo());
			//====================取得條件資料======================         
			HashMap conditionMap = getRequestParameter(parameterMap, true);
			String beforeChangeStatus = (String)conditionMap.get("beforeChangeStatus");
			String formStatus = (String)conditionMap.get("formStatus");
			String loginEmployeeCode = (String)conditionMap.get("loginEmployeeCode"); 
			siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
			if(OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.COUNTING.equals(formStatus)){
				checkInventoryCountsHead(inventoryCountsHead, conditionMap, PROGRAM_ID, identification, errorMsgs);
			}else{
				checkAfterCounting(inventoryCountsHead, conditionMap, PROGRAM_ID, identification, errorMsgs);
			}            	    
			modifyImInventoryCounts(inventoryCountsHead, loginEmployeeCode);

			return errorMsgs;
		}catch (Exception ex) {
			log.error("盤點單檢核後存檔失敗，原因：" + ex.toString());
			throw new Exception("盤點單檢核後存檔失敗，原因：" + ex.getMessage());
		}	
	}

	public String getIdentification(Long headId) throws Exception{
		String id = null;
		try{
			ImInventoryCountsHead inventoryCountsHead = findImInventoryCountsHeadById(headId);
			if(inventoryCountsHead != null){
				id = MessageStatus.getIdentification(inventoryCountsHead.getBrandCode(), 
						inventoryCountsHead.getOrderTypeCode(), inventoryCountsHead.getOrderNo());
			}else{
				throw new NoSuchDataException("盤點單主檔查無主鍵：" + headId + "的資料！");
			}
			return id;
		}catch(Exception ex){
			log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
		}	   
	}



	/**
	 * 執行盤點單查詢初始化
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws Exception
	 */
	public Map executeSearchInitial(Map parameterMap) throws Exception{
		HashMap resultMap = new HashMap();
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
			Map multiList = new HashMap(0);
			List<BuCommonPhraseLine> allCountsType = buCommonPhraseService.getCommonPhraseLinesById("InventoryCountsType", false);
			//List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(brandCode ,"ICF");
			multiList.put("allCountsType", AjaxUtils.produceSelectorData(allCountsType, "lineCode", "name", false, true));
			//multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));	    
			resultMap.put("multiList",multiList);
			return resultMap;       	
		}catch (Exception ex) {
			log.error("盤點單查詢初始化失敗，原因：" + ex.toString());
			throw new Exception("盤點單查詢初始化失敗，原因：" + ex.toString());
		}           
	}

	/**
	 * 顯示查詢頁面的line
	 * 
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{
		try{
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小  	  
			//======================帶入Head的值=========================	    
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
			String startOrderNo = httpRequest.getProperty("startOrderNo");
			String endOrderNo = httpRequest.getProperty("endOrderNo");
			String status = httpRequest.getProperty("status");
			String warehouseCode = httpRequest.getProperty("warehouseCode");
			String startCountsDate = httpRequest.getProperty("startCountsDate");
			String endCountsDate = httpRequest.getProperty("endCountsDate");
			Date actualStartCountsDate = null;
			Date actualEndCountsDate = null;
			if(StringUtils.hasText(startCountsDate))
				actualStartCountsDate = DateUtils.parseDate("yyyy/MM/dd", startCountsDate);
			if(StringUtils.hasText(endCountsDate))
				actualEndCountsDate = DateUtils.parseDate("yyyy/MM/dd", endCountsDate);
			String countsId = httpRequest.getProperty("countsId");
			String countsType = httpRequest.getProperty("countsType");
			String startCountsLotNo = httpRequest.getProperty("startCountsLotNo");
			String endCountsLotNo = httpRequest.getProperty("endCountsLotNo");

			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("orderTypeCode", orderTypeCode);
			findObjs.put("startOrderNo", startOrderNo);
			findObjs.put("endOrderNo", endOrderNo); 	   
			findObjs.put("status", status);
			findObjs.put("warehouseCode", warehouseCode);  	    
			findObjs.put("startCountsDate", actualStartCountsDate);
			findObjs.put("endCountsDate", actualEndCountsDate);
			findObjs.put("countsId", countsId);
			findObjs.put("countsType", countsType);
			findObjs.put("startCountsLotNo", startCountsLotNo);
			findObjs.put("endCountsLotNo", endCountsLotNo);
			//==============================================================	      	   
			List<ImInventoryCountsHead> inventoryCountsHeads = 
				(List<ImInventoryCountsHead>)imInventoryCountsHeadDAO.findPageLine(findObjs, 
						iSPage, iPSize, ImInventoryCountsHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");
			System.out.println("ResultSize=["+ inventoryCountsHeads.size() + "]");
			if (inventoryCountsHeads != null && inventoryCountsHeads.size() > 0) {    	    
				Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;    // 取得第一筆的INDEX 	
				Long maxIndex = (Long)imInventoryCountsHeadDAO.findPageLine(findObjs, -1, iPSize, 
						ImInventoryCountsHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount");	// 取得最後一筆 INDEX

				for(ImInventoryCountsHead inventoryCountsHead: inventoryCountsHeads){
					inventoryCountsHead.setCountsType(buCommonPhraseService.getBuCommonPhraseLineName("InventoryCountsType", 
							inventoryCountsHead.getCountsType()));   	    	    
					inventoryCountsHead.setStatus(OrderStatus.getChineseWord(inventoryCountsHead.getStatus()));
				}
				result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, 
						inventoryCountsHeads, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, gridDatas));
			}

			return result;
		}catch(Exception ex){
			log.error("載入頁面顯示的盤點查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的盤點查詢失敗！");
		}	
	}

	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}

	public Map getSearchSelection(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try{
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
			if(result.size() > 0 )
				pickerResult.put("result", result);

			resultMap.put("vatBeanPicker", pickerResult);
			resultMap.put("topLevel",  new String[]{"vatBeanPicker"});
			return resultMap;
		}catch(Exception ex){
			log.error("執行盤點單檢視失敗，原因：" + ex.toString());
			throw new Exception("執行盤點單檢視失敗，原因：" + ex.getMessage());		
		}
	}

	public void updateAllSearchData(Map parameterMap) throws FormException, Exception{
		try{
			Object pickerBean = parameterMap.get("vatBeanPicker");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			//Object otherBean = parameterMap.get("vatBeanOther");
			//String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			//ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			//======================帶入Head的值=========================	    
			String brandCode = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
			String orderTypeCode = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");
			String startOrderNo = (String)PropertyUtils.getProperty(formBindBean, "startOrderNo");
			String endOrderNo = (String)PropertyUtils.getProperty(formBindBean, "endOrderNo");
			String status = (String)PropertyUtils.getProperty(formBindBean, "status");
			String warehouseCode = (String)PropertyUtils.getProperty(formBindBean, "warehouseCode");
			String startCountsDate = (String)PropertyUtils.getProperty(formBindBean, "startCountsDate");
			String endCountsDate = (String)PropertyUtils.getProperty(formBindBean, "endCountsDate");
			Date actualStartCountsDate = null;
			Date actualEndCountsDate = null;
			if(StringUtils.hasText(startCountsDate))
				actualStartCountsDate = DateUtils.parseDate("yyyy/MM/dd", startCountsDate);
			if(StringUtils.hasText(endCountsDate))
				actualEndCountsDate = DateUtils.parseDate("yyyy/MM/dd", endCountsDate);

			String countsId = (String)PropertyUtils.getProperty(formBindBean, "countsId");
			String countsType = (String)PropertyUtils.getProperty(formBindBean, "countsType");
			String startCountsLotNo = (String)PropertyUtils.getProperty(formBindBean, "startCountsLotNo");
			String endCountsLotNo = (String)PropertyUtils.getProperty(formBindBean, "endCountsLotNo");

			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("orderTypeCode", orderTypeCode);
			findObjs.put("startOrderNo", startOrderNo);
			findObjs.put("endOrderNo", endOrderNo); 	   
			findObjs.put("status", status);
			findObjs.put("warehouseCode", warehouseCode);  	    
			findObjs.put("startCountsDate", actualStartCountsDate);
			findObjs.put("endCountsDate", actualEndCountsDate);
			findObjs.put("countsId", countsId);
			findObjs.put("countsType", countsType);
			findObjs.put("startCountsLotNo", startCountsLotNo);
			findObjs.put("endCountsLotNo", endCountsLotNo);

			List<ImInventoryCountsHead> inventoryCountsHeads = 
				(List<ImInventoryCountsHead>)imInventoryCountsHeadDAO.findPageLine(findObjs, 
						-1, -1, ImInventoryCountsHeadDAO.QUARY_TYPE_SELECT_ALL).get("form");
		}catch(Exception ex){
			log.error("更新選取盤點資料失敗，原因：" + ex.toString());
			throw new Exception("更新選取盤點資料失敗，原因：" + ex.getMessage());		
		}
	}

	/**
	 *  更新盤點單主檔
	 * 
	 * @param parameterMap
	 * @throws FormException
	 * @throws Exception
	 */
	public ImInventoryCountsHead updateInventoryCounts(Map parameterMap) throws FormException, Exception {

		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			Long headId = getInventoryCountsHeadId(formLinkBean);
			String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			//取得欲更新的bean
			ImInventoryCountsHead inventoryCountsHead = getActualInventoryCounts(headId);
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, inventoryCountsHead);
			modifyImInventoryCounts(inventoryCountsHead, employeeCode);
			return inventoryCountsHead;
		} catch (FormException fe) {
			log.error("盤點單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("盤點單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("盤點單存檔時發生錯誤，原因：" + ex.getMessage());
		}
	}

	public static Object[] startProcess(ImInventoryCountsHead form) throws ProcessFailedException{       

		try{           
			String packageId = "Im_InventoryCounts";         
			String processId = "process";           
			String version = "20091001";
			String sourceReferenceType = "inventoryCounts";
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

	public List<ImInventoryCountsHead> executeBatchImportT2(List<ImInventoryCountsHead> imInventoryCountsHeads, List assembly) throws Exception{
		
		try{
			String uuid = UUID.randomUUID().toString();
			log.info("executeBatchImportT2");
			if(imInventoryCountsHeads != null){
				for(int i = 0; i < imInventoryCountsHeads.size(); i++){
					ImInventoryCountsHead importHead = (ImInventoryCountsHead)imInventoryCountsHeads.get(i);
					ImInventoryCountsHead imInventoryCountsHead = imInventoryCountsHeadDAO.
						findInventoryCountsByCountsIdAndLotNo(importHead.getCountsId(), importHead.getCountsLotNo(), null);
					if(null == imInventoryCountsHead){
						throw new Exception("查無盤點代號 : " + importHead.getCountsId() + " ，批號: " + importHead.getCountsLotNo()+ " 對應之盤點單");
					}
					
					if(OrderStatus.FINISH.equals(imInventoryCountsHead.getStatus())){
						throw new Exception("盤點代號 : " + importHead.getCountsId() + " ，批號: " + importHead.getCountsLotNo()+ " 的狀態為完成已無法匯入");
					}
						
				}
				
				for(int i = 0; i < imInventoryCountsHeads.size(); i++){
					ImInventoryCountsHead importHead = (ImInventoryCountsHead)imInventoryCountsHeads.get(i);
					ImInventoryCountsHead imInventoryCountsHead = imInventoryCountsHeadDAO.
						findInventoryCountsByCountsIdAndLotNo(importHead.getCountsId(), importHead.getCountsLotNo(), null);
					
					List<ImInventoryCountsLine> imInventoryCountsLines = importHead.getImInventoryCountsLines();
					for (int y=imInventoryCountsLines.size()-1; y>=0 ; y--) {
						ImInventoryCountsLine line = (ImInventoryCountsLine)imInventoryCountsLines.get(y);
						line.setCountsQtyFinal(line.getCountsQty());
						if( 0 == NumberUtils.getDouble(line.getCountsQty())){
							imInventoryCountsLines.remove(line);
						}
					}
					
					for (Iterator iterator = imInventoryCountsLines.iterator(); iterator.hasNext();) {
						ImInventoryCountsLine imInventoryCountsLine = (ImInventoryCountsLine) iterator.next();
						if(imInventoryCountsLine.getEanCode().length() >= 13)
							imInventoryCountsLine.setEanCode(imInventoryCountsLine.getEanCode().substring(0,13));
						String itemcode = imItemEancodeDAO.getOneItemCodeByProperty(importHead.getBrandCode(), imInventoryCountsLine.getEanCode());
						//如果是國際碼，把商品品號匯入
						if(StringUtils.hasText(itemcode)){
							imInventoryCountsLine.setItemCode(itemcode);
						//如果不是，轉成商品品號
						}else{
							imInventoryCountsLine.setItemCode(imInventoryCountsLine.getEanCode());
							imInventoryCountsLine.setEanCode(null);
						}
					}
					
					imInventoryCountsHead.setImInventoryCountsLines(imInventoryCountsLines);
					imInventoryCountsHead.setImportedTimes(NumberUtils.getLong(imInventoryCountsHead.getImportedTimes()) + 1);
					imInventoryCountsHead.setLastUpdatedBy(importHead.getLastUpdatedBy());
					imInventoryCountsHead.setLastUpdateDate(importHead.getLastUpdateDate());
					imInventoryCountsHead.setActualCountsDate(importHead.getActualCountsDate());
					imInventoryCountsHead.setInventoryDate(importHead.getActualCountsDate());
					imInventoryCountsHead.setLastImportDate(importHead.getLastImportDate());
					imInventoryCountsHead.setReserve2(importHead.getReserve2());
					imInventoryCountsHead.setActualSuperintendentCode(importHead.getActualSuperintendentCode());
					imInventoryCountsHead.setIsImportedFile("Y");
					imInventoryCountsHead.setStatus(OrderStatus.COUNTING);
					imInventoryCountsHeadDAO.update(imInventoryCountsHead);
					SiSystemLogUtils.createSystemLog(PROGRAM_ID_IMPORT, MessageStatus.LOG_INFO, 
							"盤點代號 : " + importHead.getCountsId() + " ，批號: " + importHead.getCountsLotNo()+ " 匯入完成，匯入次數: " + imInventoryCountsHead.getImportedTimes() , new Date(), uuid, "SYS");
					assembly.add(imInventoryCountsHead);
					SiSystemLogUtils.deleteSystemLog(PROGRAM_ID, imInventoryCountsHead.getCountsId() + imInventoryCountsHead.getCountsLotNo());
				}
			}
			return assembly;
		}catch(Exception ex){
			log.error("執行調撥資料批次匯入失敗，原因：" + ex.toString());
			throw ex;
		}
	}

	
	/**
	 * 執行盤點清單初始化
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws Exception
	 */
	public Map executeListInitial(Map parameterMap) throws Exception{
		HashMap resultMap = new HashMap();
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");	
			String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
			String countsId = (String)PropertyUtils.getProperty(otherBean, "countsId");
			resultMap.put("createdBy", loginEmployeeCode);
			resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));
			
			BuBrand bubrand = buBrandDAO.findById(loginBrandCode);
			if(null == bubrand)
				throw new Exception("查無品牌代號: "+ loginBrandCode +" 對應之品牌");
			resultMap.put("brandCode", loginBrandCode);
			resultMap.put("brandName", bubrand.getBrandName());
			
			BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(loginBrandCode,orderTypeCode));
			if(null == buOrderType)
				throw new Exception("查無品牌: "+ bubrand.getBrandName() +" ，單別: "+orderTypeCode+" 對應之盤點單");
			if(!StringUtils.hasText(buOrderType.getTaxCode()))
				throw new Exception("查無品牌: "+ bubrand.getBrandName() +" ，單別: "+orderTypeCode+" 對應之稅別");
			resultMap.put("taxTypeCode", buOrderType.getTaxCode());
			
			BuCommonPhraseLine taxName = buCommonPhraseService.getBuCommonPhraseLine("TaxCatalog", buOrderType.getTaxCode());
			if(null == taxName)
				throw new Exception("查無稅別代號: "+ buOrderType.getTaxCode() +" 對應之稅別名稱");
			resultMap.put("taxName", taxName.getName());
			
			
			
			Map multiList = new HashMap(0);
			List<BuCommonPhraseLine> allCountsType = buCommonPhraseService.getCommonPhraseLinesById("InventoryCountsType", false);
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(loginBrandCode ,orderTypeCode);
			BuCommonPhraseLine inventoryCountsId = buCommonPhraseService.getBuCommonPhraseLine("InventoryCountsId", countsId);
			if(null == inventoryCountsId || null == inventoryCountsId.getAttribute1())
				throw new Exception("查無盤點代號: "+ countsId +" 對應之盤點名稱");
			List<BuCommonPhraseLine> allCountsId = buCommonPhraseService.getCommonPhraseLinesById("InventoryCountsId", false);
			List<BuCommonPhraseLine> allCustomsWarehouseCode = buCommonPhraseService.getCommonPhraseLinesById("CustomsWarehouseCode", false);
			List<ImWarehouse> allWarehouseCode = imWarehouseDAO.findByBrandCodeOrder(loginBrandCode, "Y");
			
			multiList.put("allCountsType", AjaxUtils.produceSelectorData(allCountsType, "lineCode", "name", false, false));
			multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false, orderTypeCode));
			multiList.put("allCountsId", AjaxUtils.produceSelectorData(allCountsId, "attribute1", "name", true, false, inventoryCountsId.getAttribute1()));
			multiList.put("allCustomsWarehouseCode", AjaxUtils.produceSelectorData(allCustomsWarehouseCode, "lineCode", "name", true, false));
			multiList.put("allWarehouseCode", AjaxUtils.produceSelectorData(allWarehouseCode, "warehouseCode", "warehouseName", true, true));
			resultMap.put("multiList",multiList);

			return resultMap;       	
		}catch (Exception ex) {
			log.error("盤點單初始化失敗，原因：" + ex.getMessage());
			throw ex;
		}           
	}
	
	
	public List<Properties> getAJAXPageDataForList(Properties httpRequest) throws Exception{
		try{
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String countsId = httpRequest.getProperty("countsId");// 盤點代號
			String customsWarehouseCode = httpRequest.getProperty("customsWarehouseCode");// 關別
			String warehouseCode = httpRequest.getProperty("warehouseCode");// 庫別
			String taxTypeCode = httpRequest.getProperty("taxTypeCode");// 品牌代號
			
			//======================帶入Search的值=========================
			HashMap findObjs = new HashMap();
			findObjs.put(" and BRAND_CODE  = :brandCode", brandCode);
			findObjs.put(" and COUNTS_ID  = :countsId", countsId);
			findObjs.put(" and CUSTOMS_WAREHOUSE_CODE  = :customsWarehouseCode", customsWarehouseCode);
			findObjs.put(" and WAREHOUSE_CODE  = :warehouseCode", warehouseCode);
			findObjs.put(" and TAX_TYPE_CODE  = :taxTypeCode", taxTypeCode);
			
			//==============================================================
			Map headsMap = imInventoryCountsHeadDAO.search("ImInventoryCountsList", findObjs, "order by warehouseCode, startNo", 
					iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
			List<ImInventoryCountsList> lists = (List<ImInventoryCountsList>) headsMap.get(BaseDAO.TABLE_LIST);
			
			if(lists != null && lists.size() > 0){
				setListLineDefaultValues(lists);
				// 取得第一筆的INDEX
				Long firstIndex = lists.get(0).getIndexNo();
				// 取得最後一筆 INDEX
				Long maxIndex = (Long) imInventoryCountsHeadDAO.search("ImInventoryCountsList","count(HEAD_ID) as rowCount", findObjs, "order by warehouseCode, startNo",
						iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_LIST_NAMES, GRID_FIELD_LIST_DEFAULT_VALUES, lists, gridDatas,
						firstIndex, maxIndex));	
			}else{
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_LIST_NAMES, GRID_FIELD_LIST_DEFAULT_VALUES, gridDatas));
			}
			
			return result;
		}catch(Exception ex){
			log.error("載入頁面顯示的盤點清單發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的盤點清單失敗！");
		}
	}
	
	public void setListLineDefaultValues( List<ImInventoryCountsList> lists ) throws Exception{
		Long indexNo = 1L;
		for (Iterator iterator = lists.iterator(); iterator.hasNext();) {
			ImInventoryCountsList list = (ImInventoryCountsList) iterator.next();
			list.setIndexNo(indexNo++);
			ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(list.getBrandCode(), list.getWarehouseCode(), null);
			if(null != imWarehouse){
				list.setWarehouseName(imWarehouse.getWarehouseName());
			}else{
				list.setWarehouseName("查無此庫別");
			}
			
			if(StringUtils.hasText(list.getSuperintendentCode())){
				BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(list.getBrandCode(), list.getSuperintendentCode());
				if(employeeWithAddressView != null) {
					list.setSuperintendentName(employeeWithAddressView.getChineseName());
				}
			}
		}
	}
	
	public List<Properties> updateAJAXPageDataForList(Properties httpRequest) throws Exception{
		try{
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			String errorMsg = null;
			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_LIST_NAMES);
			String employeeCode = httpRequest.getProperty("employeeCode");// 員工代號
			
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String countsId = httpRequest.getProperty("countsId");// 盤點代號
			String customsWarehouseCode = httpRequest.getProperty("customsWarehouseCode");// 關別
			String taxTypeCode = httpRequest.getProperty("taxTypeCode");// 品牌代號
			
			//======================帶入Search的值=========================
			HashMap findObjs = new HashMap();
			findObjs.put(" and BRAND_CODE  = :brandCode", brandCode);
			findObjs.put(" and COUNTS_ID  = :countsId", countsId);
			findObjs.put(" and CUSTOMS_WAREHOUSE_CODE  = :customsWarehouseCode", customsWarehouseCode);
			findObjs.put(" and TAX_TYPE_CODE  = :taxTypeCode", taxTypeCode);

			// Get INDEX NO
			Long maxIndex = (Long) imInventoryCountsHeadDAO.search("ImInventoryCountsList","count(HEAD_ID) as rowCount", findObjs, "order by warehouseCode, startNo",
					iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
			if (upRecords != null) {
				for (Properties upRecord : upRecords) {
					// 先載入HEAD_ID OR LINE DATA
					Long headId = NumberUtils.getLong(upRecord.getProperty("headId"));
					String warehouseCode = upRecord.getProperty("warehouseCode");
					if (StringUtils.hasText(warehouseCode) && customsWarehouseCode.equals(upRecord.getProperty("customsWarehouseCode"))) {
						ImInventoryCountsList list = (ImInventoryCountsList)imInventoryCountsHeadDAO.findById("ImInventoryCountsList", headId);
						if(list != null){
							AjaxUtils.setPojoProperties(list, upRecord, GRID_FIELD_LIST_NAMES, GRID_FIELD_LIST_TYPES);
							list.setLastUpdatedBy(employeeCode);
							list.setLastUpdateDate(new Date());
							imInventoryCountsHeadDAO.update(list);
						}else{
							maxIndex++;
							list = new ImInventoryCountsList();
							AjaxUtils.setPojoProperties(list, upRecord, GRID_FIELD_LIST_NAMES, GRID_FIELD_LIST_TYPES);
							list.setBrandCode(brandCode);
							list.setCountsId(countsId);
							list.setTaxTypeCode(taxTypeCode);
							list.setCreationDate(new Date());
							list.setLastUpdateDate(new Date());
							list.setCreatedBy(employeeCode);
							list.setLastUpdatedBy(employeeCode);
							list.setIndexNo(Long.valueOf(maxIndex));
							imInventoryCountsHeadDAO.save(list);
						}
					}
				}
			}
			
			//重新排序
			findObjs = new HashMap();
			findObjs.put(" and BRAND_CODE  = :brandCode", brandCode);
			findObjs.put(" and COUNTS_ID  = :countsId", countsId);
			findObjs.put(" and CUSTOMS_WAREHOUSE_CODE  = :customsWarehouseCode", customsWarehouseCode);
			findObjs.put(" and TAX_TYPE_CODE  = :taxTypeCode", taxTypeCode);
			//==============================================================
			Map headsMap = imInventoryCountsHeadDAO.search("ImInventoryCountsList", findObjs, "order by warehouseCode, startNo", 
					0, 0, BaseDAO.QUERY_SELECT_ALL);
			List<ImInventoryCountsList> lists = (List<ImInventoryCountsList>) headsMap.get(BaseDAO.TABLE_LIST);
			Long indexNo = 1L;
			for (Iterator iterator = lists.iterator(); iterator.hasNext();) {
				ImInventoryCountsList imInventoryCountsList = (ImInventoryCountsList) iterator.next();
				imInventoryCountsList.setIndexNo(indexNo++);
				imInventoryCountsHeadDAO.update(imInventoryCountsList);
			}
			return AjaxUtils.getResponseMsg(errorMsg);
		}catch(Exception ex){
			log.error("載入頁面顯示的盤點清單發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的盤點清單失敗！"); 
		}	
	}
	
	/**
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> getWarehouseInfo(Properties httpRequest) throws Exception{

		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		String brandCode = null;
		String warehouseCode = null;
		try {
			brandCode = httpRequest.getProperty("brandCode");
			warehouseCode = httpRequest.getProperty("warehouseCode");
			ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, warehouseCode, null);
			if(null != imWarehouse){
				properties.setProperty("WarehouseName", imWarehouse.getWarehouseName());
				properties.setProperty("CustomsWarehouseCode", imWarehouse.getCustomsWarehouseCode());
				properties.setProperty("CategoryCode", imWarehouse.getCategoryCode());
			}else{
				properties.setProperty("WarehouseName", "查無此庫別");	
			}
			result.add(properties);	

			return result;	        
		} catch (Exception ex) {
			log.error("依據庫別代號：" + warehouseCode + "查詢庫別資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢庫別資料失敗！");
		}        
	}
	
	/**
	 *  批次建立盤點清單
	 * @param parameterMap
	 * @throws FormException
	 * @throws Exception
	 */
	public void updateInventoryCountLists(Map parameterMap) throws FormException, Exception {
		List errorMsgs = new ArrayList();
		String message = null;
		
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		Object otherBean = parameterMap.get("vatBeanOther");
		String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		
		String brandCode = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
		String countsId = (String)PropertyUtils.getProperty(formBindBean, "countsId");
		String customsWarehouseCode = (String)PropertyUtils.getProperty(formBindBean, "customsWarehouseCode");
		String warehouseCode = (String)PropertyUtils.getProperty(formBindBean, "warehouseCode");
		String taxTypeCode = (String)PropertyUtils.getProperty(formBindBean, "taxTypeCode");
		//======================帶入Search的值=========================
		HashMap findObjs = new HashMap();
		findObjs.put(" and BRAND_CODE  = :brandCode", brandCode);
		findObjs.put(" and COUNTS_ID  = :countsId", countsId);
		findObjs.put(" and CUSTOMS_WAREHOUSE_CODE  = :customsWarehouseCode", customsWarehouseCode);
		findObjs.put(" and WAREHOUSE_CODE  = :warehouseCode", warehouseCode);
		findObjs.put(" and TAX_TYPE_CODE  = :taxTypeCode", taxTypeCode);
		
		//==============================================================
		Map headsMap = imInventoryCountsHeadDAO.search("ImInventoryCountsList", findObjs, "order by warehouseCode, startNo", 
				0, 0, BaseDAO.QUERY_SELECT_ALL);
		List<ImInventoryCountsList> lists = (List<ImInventoryCountsList>) headsMap.get(BaseDAO.TABLE_LIST);
		//刪除掉delete的資料且批次建立
		String identification = brandCode + countsId + taxTypeCode;
		siProgramLogAction.deleteProgramLog(PROGRAM_ID_LIST, null, identification);
		Long indexNo = 1L;
		//HashMap listMap = new HashMap();
		for (Iterator iterator = lists.iterator(); iterator.hasNext();) {
			ImInventoryCountsList list = (ImInventoryCountsList) iterator.next();
			if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(list.getIsDeleteRecord())){
				//imInventoryCountsHeadDAO.delete(list);
			}else{
				/*
				if(null == listMap.get(list.getWarehouseCode())){
					listMap.put(list.getWarehouseCode(), list);
				}else{
					message = "項次: " +list.getIndexNo() +  " ，庫別代號: "+ list.getWarehouseCode() + " 重覆設定";
					siProgramLogAction.createProgramLog(PROGRAM_ID_LIST, MessageStatus.LOG_ERROR, identification, message, employeeCode);
					errorMsgs.add(message);
					log.error(message);
					indexNo++;
					continue;
				}
				*/
				int startNo = NumberUtils.getInt(list.getStartNo());
				int endNo = NumberUtils.getInt(list.getEndNo());
				if(startNo == 0 || endNo == 0){
					message = "項次: " +list.getIndexNo() +  " ，庫別代號: "+ list.getWarehouseCode() + " 之起訖流水號尚未設定！";
					siProgramLogAction.createProgramLog(PROGRAM_ID_LIST, MessageStatus.LOG_ERROR, identification, message, employeeCode);
					errorMsgs.add(message);
					log.error(message);
				}
				/*if("S01".equals(countsId) && (startNo > 100 || endNo > 100)){
					message = "項次: " +list.getIndexNo() +  " ，庫別代號: "+ list.getWarehouseCode() + " 之起訖流水號不可大於一百！";
					siProgramLogAction.createProgramLog(PROGRAM_ID_LIST, MessageStatus.LOG_ERROR, identification, message, employeeCode);
					errorMsgs.add(message);
					log.error(message);
				}*/
				if(startNo > 9999 || endNo > 9999){
					message = "項次: " +list.getIndexNo() +  " ，庫別代號: "+ list.getWarehouseCode() + " 之起訖流水號不可大於九千九百九十九！";
					siProgramLogAction.createProgramLog(PROGRAM_ID_LIST, MessageStatus.LOG_ERROR, identification, message, employeeCode);
					errorMsgs.add(message);
					log.error(message);
				}
				if(endNo - startNo < 0){
					message = "項次: " +list.getIndexNo() +  " ，庫別代號: "+ list.getWarehouseCode() + " 之起始流水號不得大於結束流水號！";
					siProgramLogAction.createProgramLog(PROGRAM_ID_LIST, MessageStatus.LOG_ERROR, identification, message, employeeCode);
					errorMsgs.add(message);
					log.error(message);
				}
				ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, list.getWarehouseCode(), null);
				if(null == imWarehouse){
					message = "項次: " +list.getIndexNo() +  " ，查無庫別代號: "+ list.getWarehouseCode() + " 之對應庫別！";
					siProgramLogAction.createProgramLog(PROGRAM_ID_LIST, MessageStatus.LOG_ERROR, identification, message, employeeCode);
					errorMsgs.add(message);
					log.error(message);
				}
				if(StringUtils.hasText(list.getSuperintendentCode())){
					BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(list.getBrandCode(), list.getSuperintendentCode());
					if(employeeWithAddressView == null) {
						message = "項次: " +list.getIndexNo() +  " ，查無盤點人代號: "+ list.getSuperintendentCode() + " 之盤點人資訊！";
						siProgramLogAction.createProgramLog(PROGRAM_ID_LIST, MessageStatus.LOG_ERROR, identification, message, employeeCode);
						errorMsgs.add(message);
						log.error(message);
					}
				}
				createImInventoryCountsHeadByList(parameterMap, list);
				list.setIsLockRecord(null);
				imInventoryCountsHeadDAO.update(list);
			}
		}
		
		//重新排序
		findObjs.put(" and BRAND_CODE  = :brandCode", brandCode);
		findObjs.put(" and COUNTS_ID  = :countsId", countsId);
		findObjs.put(" and CUSTOMS_WAREHOUSE_CODE  = :customsWarehouseCode", customsWarehouseCode);
		findObjs.put(" and TAX_TYPE_CODE  = :taxTypeCode", taxTypeCode);
		//==============================================================
		headsMap = imInventoryCountsHeadDAO.search("ImInventoryCountsList", findObjs, "order by warehouseCode, startNo", 
				0, 0, BaseDAO.QUERY_SELECT_ALL);
		lists = (List<ImInventoryCountsList>) headsMap.get(BaseDAO.TABLE_LIST);
		
		for (int i=lists.size()-1; i>=0 ; i--) {
			ImInventoryCountsList list = lists.get(i);
			if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(list.getIsDeleteRecord())){
				imInventoryCountsHeadDAO.delete(list);
				lists.remove(list);
			}
		}
		
		for (Iterator iterator = lists.iterator(); iterator.hasNext();) {
			ImInventoryCountsList imInventoryCountsList = (ImInventoryCountsList) iterator.next();
			imInventoryCountsList.setIndexNo(indexNo++);
			imInventoryCountsHeadDAO.update(imInventoryCountsList);
		}
		
		if(errorMsgs.size() > 0)
			throw new FormException("執行盤點單清單建立失敗");
	}
	
	public void createImInventoryCountsHeadByList(Map parameterMap, ImInventoryCountsList list) throws Exception{
		try{
			log.info("createImInventoryCountsHeadByList");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			String brandCode = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
			String orderTypeCode = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");
			String createdBy = (String)PropertyUtils.getProperty(formBindBean, "createdBy");
			String countsLotNo = "";
			int startNo = list.getStartNo();
			int endNo = list.getEndNo();

			for (int i = 0; i <= endNo - startNo; i++) {
			 	
				
				if(String.valueOf(startNo+i).length()==4){
					countsLotNo = list.getWarehouseCode() + list.getTaxTypeCode() + CommonUtils.insertCharacterWithLimitedLength(String.valueOf(startNo+i), 4, 4, "0", "L");
				}else{
					countsLotNo = list.getWarehouseCode() + list.getTaxTypeCode() + CommonUtils.insertCharacterWithLimitedLength(String.valueOf(startNo+i), 3, 3, "0", "L");
				}
				
				ImInventoryCountsHead head = imInventoryCountsHeadDAO.findInventoryCountsByCountsIdAndLotNo(list.getCountsId(), countsLotNo, null);
				if(null == head){
					head = new ImInventoryCountsHead();
					head.setBrandCode(brandCode);
					head.setOrderTypeCode(orderTypeCode);
					String orderNo = buOrderTypeService.getOrderSerialNo(brandCode, orderTypeCode);
					if (!orderNo.equals("unknow")) {
						head.setOrderNo(orderNo);
					} else {
						throw new ObtainSerialNoFailedException("取得" + orderTypeCode + "單號失敗！");
					}
					head.setCountsDate(list.getCountsDate());
					head.setWarehouseCode(list.getWarehouseCode());
					head.setCountsId(list.getCountsId());
					head.setCountsType("2");
					head.setCountsLotNo(countsLotNo);
					head.setSuperintendentCode(list.getSuperintendentCode());
					resetImInventoryCountsHead(head);
					SiSystemLogUtils.deleteSystemLog(PROGRAM_ID, head.getCountsId() + head.getCountsLotNo());
					
					head.setCreatedBy(createdBy);
					head.setCreationDate(new Date());
					head.setLastUpdatedBy(createdBy);
					head.setLastUpdateDate(new Date());
					imInventoryCountsHeadDAO.save(head);
				}else{
					
					if(OrderStatus.FINISH.equals(head.getStatus())){
						throw new Exception("盤點單代號:" + head.getCountsId() + " 批號:" + head.getCountsLotNo() + " 已為結束無法清除");
					}
					
					//清除盤點單記錄
					if("Y".equals(list.getIsLockRecord())){
						resetImInventoryCountsHead(head);
						SiSystemLogUtils.deleteSystemLog(PROGRAM_ID, head.getCountsId() + head.getCountsLotNo());
					}
					
					head.setSuperintendentCode(list.getSuperintendentCode());
					head.setCountsDate(list.getCountsDate());
					head.setLastUpdatedBy(createdBy);
					head.setLastUpdateDate(new Date());
					imInventoryCountsHeadDAO.update(head);
				}
			}
		}catch(Exception ex){
			log.error("批次建立盤點單發生錯誤，原因: " + ex.getMessage());
			throw ex;
		}
	}
	
	public String getIdentificationList(String bradnCode, String countsId , String taxTypeCode) throws Exception{
		String id = null;
		try{
			id = bradnCode + countsId + taxTypeCode;
			return id;
		}catch(Exception ex){
			log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
		}	   
	}
	
	/**
	 * 重置盤點清單
	 * @param ImInventoryCountsHead
	 * @throws Exception
	 */
	public void resetImInventoryCountsHead(ImInventoryCountsHead head) throws Exception{
		head.setStatus(OrderStatus.SAVE);
		head.setInventoryDate(null);
		head.setLastImportDate(null);
		head.setActualSuperintendentCode(null);
		head.setImportedTimes(0L);
		head.setIsImportedFile(null);
		head.setReserve2("0");
	}
	
	/**
	 * 清除盤點紀錄
	 * @param ImInventoryCountsHead
	 * @throws Exception
	 */
	public List<Properties> executeListDataClean(Properties httpRequest) throws Exception{
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try{
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String employeeCode = httpRequest.getProperty("employeeCode");// 員工代號
			String countsId = httpRequest.getProperty("countsId");// 盤點代號起
			String countsLotNoS = httpRequest.getProperty("countsLotNoS");// 盤點批號起
			String countsLotNoE = httpRequest.getProperty("countsLotNoE");// 盤點批號迄
			
			//======================帶入Search的值=========================
			HashMap findObjs = new HashMap();
			findObjs.put(" and BRAND_CODE   = :brandCode", brandCode);
			findObjs.put(" and COUNTS_ID   	= :countsId", countsId);
			findObjs.put(" and COUNTS_LOT_NO   >= :countsLotNoS", countsLotNoS);
			findObjs.put(" and COUNTS_LOT_NO   <= :countsLotNoE", countsLotNoE);

			Map headsMap = imInventoryCountsHeadDAO.search("ImInventoryCountsHead", findObjs, "", 
					0, 0, BaseDAO.QUERY_SELECT_ALL);
			List<ImInventoryCountsHead> lists = (List<ImInventoryCountsHead>) headsMap.get(BaseDAO.TABLE_LIST);
			for (Iterator iterator = lists.iterator(); iterator.hasNext();) {
				ImInventoryCountsHead head = (ImInventoryCountsHead) iterator.next();
				if(OrderStatus.FINISH.equals(head.getStatus())){
					throw new Exception("盤點單代號:" + head.getCountsId() + " 批號:" + head.getCountsLotNo() + " 已為結束無法清除");
				}
				resetImInventoryCountsHead(head);
				
				SiSystemLogUtils.deleteSystemLog(PROGRAM_ID, head.getCountsId() + head.getCountsLotNo());
				
				head.setLastUpdateDate(new Date());
				head.setLastUpdatedBy(employeeCode);
				imInventoryCountsHeadDAO.update(head);
				
				SiSystemLogUtils.deleteSystemLog(PROGRAM_ID, head.getCountsId() + head.getCountsLotNo());
			}
			properties.setProperty("RESULT", "SUCCESS");	    
		}catch(Exception ex){
			log.error("清除盤點清單發生錯誤，原因：" + ex.toString());
			throw new Exception("清除盤點清單失敗！"); 
		}
		result.add(properties);
		return result;
	}
	
	/**
	 * 鎖住盤點清單
	 * @param ImInventoryCountsHead
	 * @throws Exception
	 */	
	public List<Properties> executeListDataFinish(Properties httpRequest) throws Exception{
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try{
			
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String employeeCode = httpRequest.getProperty("employeeCode");// 員工代號
			String countsId = httpRequest.getProperty("countsId");// 盤點代號起
			String warehouseCode = httpRequest.getProperty("warehouseCode");// 盤點代號起
			String [] warehouseCodeLists = null;
			if(StringUtils.hasText(warehouseCode)){
				warehouseCodeLists = warehouseCode.split(",");
			}
			
			String countsLotNoS = httpRequest.getProperty("countsLotNoS");// 盤點批號起
			String countsLotNoE = httpRequest.getProperty("countsLotNoE");// 盤點批號迄
			
			//======================帶入Search的值=========================
			HashMap findObjs = new HashMap();
			findObjs.put(" and BRAND_CODE   = :brandCode", brandCode);
			findObjs.put(" and WAREHOUSE_CODE  in (:warehouseCode)", warehouseCodeLists);
			findObjs.put(" and COUNTS_ID   	= :countsId", countsId);
			findObjs.put(" and COUNTS_LOT_NO   >= :countsLotNoS", countsLotNoS);
			findObjs.put(" and COUNTS_LOT_NO   <= :countsLotNoE", countsLotNoE);

			Map headsMap = imInventoryCountsHeadDAO.search("ImInventoryCountsHead", findObjs, "", 0, 0, BaseDAO.QUERY_SELECT_ALL);
			List<ImInventoryCountsHead> lists = (List<ImInventoryCountsHead>) headsMap.get(BaseDAO.TABLE_LIST);
			for (Iterator iterator = lists.iterator(); iterator.hasNext();) {
				ImInventoryCountsHead head = (ImInventoryCountsHead) iterator.next();
				head.setStatus(OrderStatus.FINISH);
				head.setLastUpdateDate(new Date());
				head.setLastUpdatedBy(employeeCode);
				imInventoryCountsHeadDAO.update(head);
			}
			properties.setProperty("RESULT", "SUCCESS");	    
		}catch(Exception ex){
			log.error("鎖定盤點單發生錯誤，原因：" + ex.toString());
			throw new Exception("鎖定盤點單發生錯誤！"); 
		}
		result.add(properties);
		return result;
	}
	
	/**
	 * 解鎖盤點清單
	 * @param ImInventoryCountsHead
	 * @throws Exception
	 */
	public List<Properties> executeListDataCounting(Properties httpRequest) throws Exception{
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try{
			
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String employeeCode = httpRequest.getProperty("employeeCode");// 員工代號
			String countsId = httpRequest.getProperty("countsId");// 盤點代號起
			String warehouseCode = httpRequest.getProperty("warehouseCode");// 盤點代號起
			String [] warehouseCodeLists = null;
			if(StringUtils.hasText(warehouseCode)){
				warehouseCodeLists = warehouseCode.split(",");
			}
			
			String countsLotNoS = httpRequest.getProperty("countsLotNoS");// 盤點批號起
			String countsLotNoE = httpRequest.getProperty("countsLotNoE");// 盤點批號迄
			
			//======================帶入Search的值=========================
			HashMap findObjs = new HashMap();
			findObjs.put(" and BRAND_CODE   = :brandCode", brandCode);
			findObjs.put(" and WAREHOUSE_CODE  in (:warehouseCode)", warehouseCodeLists);
			findObjs.put(" and COUNTS_ID   	= :countsId", countsId);
			findObjs.put(" and COUNTS_LOT_NO   >= :countsLotNoS", countsLotNoS);
			findObjs.put(" and COUNTS_LOT_NO   <= :countsLotNoE", countsLotNoE);

			Map headsMap = imInventoryCountsHeadDAO.search("ImInventoryCountsHead", findObjs, "", 0, 0, BaseDAO.QUERY_SELECT_ALL);
			List<ImInventoryCountsHead> lists = (List<ImInventoryCountsHead>) headsMap.get(BaseDAO.TABLE_LIST);
			for (Iterator iterator = lists.iterator(); iterator.hasNext();) {
				ImInventoryCountsHead head = (ImInventoryCountsHead) iterator.next();
				head.setStatus(OrderStatus.COUNTING);
				head.setLastUpdateDate(new Date());
				head.setLastUpdatedBy(employeeCode);
				imInventoryCountsHeadDAO.update(head);
			}
			properties.setProperty("RESULT", "SUCCESS");	    
		}catch(Exception ex){
			log.error("解鎖盤點單發生錯誤，原因：" + ex.toString());
			throw new Exception("解鎖盤點單發生錯誤！"); 
		}
		result.add(properties);
		return result;
	}
	
	/**
	 * 刪除明細
	 */
	public List<Properties> deleteLines(Properties httpRequest) throws Exception{
	    log.info("deleteLines");
	    List<Properties> result = new ArrayList();
	    Properties properties   = new Properties();
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String employeeCode = httpRequest.getProperty("employeeCode");

	    try{
		ImInventoryCountsHead head = (ImInventoryCountsHead) imInventoryCountsHeadDAO
		.findByPrimaryKey(ImInventoryCountsHead.class, headId);

		Date updateDate = new Date();

		List<ImInventoryCountsLine> lines = head.getImInventoryCountsLines();
		
		for (int i = lines.size()-1; i >= 0 ; i--) {
		    ImInventoryCountsLine line = lines.get(i);
		    if (AjaxUtils.IS_DELETE_RECORD_TRUE.equals(line.getIsDeleteRecord())) {
			log.info("品號:" + line.getItemCode() + "刪除");
			SiSystemLogUtils.createSystemLog(PROGRAM_ID, line.getItemCode(), 
				"明細項次  "+line.getIndexNo()+" 數量由 "+line.getCountsQty()+" 修改為刪除",
				updateDate, head.getCountsId() + head.getCountsLotNo(), employeeCode, head.getBrandCode());
			lines.remove(i);
		    }
		}

		long indexNo = 1L;
		for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
		    ImInventoryCountsLine line = (ImInventoryCountsLine) iterator.next();
		    line.setIndexNo(indexNo++);
		}
	    	
		imInventoryCountsHeadDAO.update(head);
		result.add(properties);
		return result;
	    }catch (Exception ex) {
		log.error("刪除明細時發生錯誤，原因：" + ex.getMessage());
		throw new Exception("刪除明細時發生錯誤失敗！");
	    }
	}
	
	
	/* Spring IoC */
	public void setBuEmployeeWithAddressViewDAO(
			BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
		this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
	}

	public void setImInventoryCountsHeadDAO(ImInventoryCountsHeadDAO imInventoryCountsHeadDAO) {
		this.imInventoryCountsHeadDAO = imInventoryCountsHeadDAO;
	}

	public void setImInventoryCountsLineDAO(ImInventoryCountsLineDAO imInventoryCountsLineDAO) {
		this.imInventoryCountsLineDAO = imInventoryCountsLineDAO;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
		this.imWarehouseDAO = imWarehouseDAO;
	}

	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setTmpAppStockStatisticsService(TmpAppStockStatisticsService tmpAppStockStatisticsService) {
		this.tmpAppStockStatisticsService = tmpAppStockStatisticsService;
	}

	public void setImInventoryCountsLineService(ImInventoryCountsLineService imInventoryCountsLineService) {
		this.imInventoryCountsLineService = imInventoryCountsLineService;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setImItemEancodeDAO(ImItemEancodeDAO imItemEancodeDAO) {
		this.imItemEancodeDAO = imItemEancodeDAO;
	}

} 
