package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImPickHead;
import tw.com.tm.erp.hbm.bean.ImPickItem;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.ImPickDAO;
import tw.com.tm.erp.hbm.dao.ImStorageOnHandDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.SiProgramLogDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;


/**
 * 報單單頭 Service 
 * 
 * @author MyEclipse Persistence Tools
 */
public class ImPickService {
	
	public static final String PROGRAM_ID = "IM_PICK";

	private static final Log log = LogFactory.getLog(ImPickService.class);

	private ImPickDAO imPickDAO;

	public void setImPickDAO(ImPickDAO imPickDAO) {
		this.imPickDAO = imPickDAO;
	}
	
	private BuOrderTypeService buOrderTypeService;
	
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}
	
	private ImWarehouseService imWarehouseService;
	
	public void setImWarehouseService(ImWarehouseService imWarehouseService) {
		this.imWarehouseService = imWarehouseService;
	}
	
	private BuBrandService buBrandService;
	
	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}
	
	private ImItemService imItemService;
	
	public void setImItemService(ImItemService imItemService) {
		this.imItemService = imItemService;
	}
	
	private NativeQueryDAO nativeQueryDAO;
	
	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}
	
	private SiProgramLogAction siProgramLogAction;
	
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}
	
	private SiProgramLogDAO siProgramLogDAO;
	
	public void setSiProgramLogDAO(SiProgramLogDAO siProgramLogDAO) {
		this.siProgramLogDAO = siProgramLogDAO;
	}
	
	private ImStorageOnHandDAO imStorageOnHandDAO;
	
	public void setImStorageOnHandDAO(ImStorageOnHandDAO imStorageOnHandDAO) {
		this.imStorageOnHandDAO = imStorageOnHandDAO;
	}
	
	private BaseDAO baseDAO;
	
	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
	
	
	/**
	 * 明細欄位
	 */
	public static final String[] GRID_FIELD_NAMES = { 
		"indexNo", "itemCode", "itemCName", "blockQuantity",   
		"commitQuantity",  "storageInNo", "storageLotNo", "warehouseCode", 
		"storageCode", "remark", "arrivalWarehouse1", "arrivalWarehouse2",
		"arrivalWarehouse3", "arrivalWarehouse4", "arrivalWarehouse5", "arrivalWarehouse6",
		"arrivalWarehouse7", "arrivalWarehouse8", "arrivalWarehouse9", "arrivalWarehouse10",
		"lineId", "isLockRecord", "isDeleteRecord", "message"
	};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
		"", "", "", "",
		"", "", "", "", 
		"", "", "", "",
		"", "", "", "",
		"", "", "", "",
		"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
	};         

	public static final int[] GRID_FIELD_TYPES = { 
		AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, 
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, 
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, 
		AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
	};
	
	
	/**
	 * 查詢明細欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"orderTypeCode", "orderNo", "pickDate", 
		"warehouseCode", "status", "headId"
	};

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {
		"", "", "",
		"", "", "",
	};
	
	
	public ImPickHead findById(Long headId){
		ImPickHead imPickHead = (ImPickHead)imPickDAO.findById("ImPickHead", headId);
		return imPickHead;
	}
	
	public ImPickHead findById(Object vatBeanFormLink) throws Exception {
		Long headId = null;
		try {
			String headIdString = (String)PropertyUtils.getProperty(vatBeanFormLink, AjaxUtils.HEAD_ID);
			headId =  StringUtils.hasText(headIdString)? Long.valueOf(headIdString):null;
			ImPickHead head = findById(headId);
			return head;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	/**
	 * 取得寫入programlog的辨識值，通常為品牌+單別+單號
	 */
	public String getIdentification(Map parameterMap, Map returnMap) throws Exception {
		ImPickHead head = (ImPickHead)returnMap.get(AjaxUtils.AJAX_FORM);
		return MessageStatus.getIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
	}
	
	/**
	 * 前端資料塞入bean
	 * @param parameterMap
	 * @return
	 */
	public void updateHeadBean(Map parameterMap,Map returnMap)throws FormException, Exception {
		Object head = null;
		try{
			Object formBindBean = parameterMap.get(AjaxUtils.VAT_BEAN_FORM_BIND);
			head = returnMap.get(AjaxUtils.AJAX_FORM);
			Object formOtherBean = parameterMap.get(AjaxUtils.VAT_BEAN_OTHER);
			String loginEmployeeCode = (String)PropertyUtils.getProperty(formOtherBean, "loginEmployeeCode");
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, head);
			PropertyUtils.setProperty(head, "lastUpdatedBy", loginEmployeeCode);
			PropertyUtils.setProperty(head, "lastUpdateDate", new Date());
			imPickDAO.update(head);
			returnMap.put(AjaxUtils.AJAX_FORM, head);
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	/**
	 * 檢查
	 */
	public void check(Map parameterMap, Map returnMap) throws Exception {
		log.info("check");
		ImPickHead head = null;
		try{
			Object otherBean    = parameterMap.get("vatBeanOther");
			String identification = (String)PropertyUtils.getProperty(otherBean, "identification");
			head = (ImPickHead)returnMap.get(AjaxUtils.AJAX_FORM);
			deleteProgramLog(identification, 0);
			checkHead(head, identification);
			checkItem(head, identification);
		}catch (Exception ex) {
			throw new Exception("檢核發生錯誤");
		}
	}
	
	/**檢核是否有porgramLog
	 * @param opType 0=delete, 1=checkSum
	 * @return List
	 * @throws ValidationErrorException
	 */
	public Integer deleteProgramLog(String identification, Integer opType) throws Exception {
		log.info("deleteProgramLog");
		Integer errorCnt = 0;
		try{
			if( 0 == opType ){
				// clear 原有 ERROR RECORD
				siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
			}else if( 1 == opType ){
				List errorLogs = siProgramLogDAO.findByIdentification(PROGRAM_ID, null, identification);
				if(null!=errorLogs)
					errorCnt = errorLogs.size();
			}
			return errorCnt;
		}catch (Exception ex) {
			log.error("刪除 Program Log 失敗，原因：" + ex.toString());
			throw new Exception("刪除 Program Log 失敗，原因：" + ex.getMessage());
		}
	}
	
	/**
	 * 檢查單頭
	 */
	public void checkHead(ImPickHead head,String identification) throws Exception {
		log.info("checkHead");
		if(null == head.getPickDate())
			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入挑貨日期", head.getLastUpdatedBy());
		if(!StringUtils.hasText(head.getWarehouseCode()))
			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入庫別", head.getLastUpdatedBy());
	}

	/**
	 * 檢查單頭
	 */
	public void checkItem(ImPickHead head,String identification) throws Exception {
		log.info("checkItem");
		List<ImPickItem> imPickItems = head.getImPickItems();
		int itemCount = 0;
		for (Iterator iterator = imPickItems.iterator(); iterator.hasNext();) {
			ImPickItem item = (ImPickItem) iterator.next();
			if(!AjaxUtils.IS_DELETE_RECORD_TRUE.equals(item.getIsDeleteRecord())){
				itemCount++;
				
				ImItem imItem = imItemService.findItem(head.getBrandCode(), item.getItemCode());
				
				if(null == imItem)
					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項品號輸入錯誤", head.getLastUpdatedBy());
				
				if(null == item.getBlockQuantity() || item.getBlockQuantity() <= 0)
					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項預扣數量請大於零", head.getLastUpdatedBy());
				
				if(!StringUtils.hasText(item.getStorageCode()))
					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項請輸入轉出儲位", head.getLastUpdatedBy());
				
				if(!StringUtils.hasText(item.getStorageInNo()))
					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項請輸入進貨日", head.getLastUpdatedBy());
				
				if(!StringUtils.hasText(item.getStorageLotNo()))
					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項請輸入效期", head.getLastUpdatedBy());
				
				if(NumberUtils.getDouble(item.getArrivalWarehouse1()) > 0 && !StringUtils.hasText(head.getArrivalWarehouseCode1()))
				    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項有輸入收庫量1，請選擇收貨庫1", head.getLastUpdatedBy());
				
				if(NumberUtils.getDouble(item.getArrivalWarehouse2()) > 0 && !StringUtils.hasText(head.getArrivalWarehouseCode2()))
				    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項有輸入收庫量2，請選擇收貨庫2", head.getLastUpdatedBy());
				
				if(NumberUtils.getDouble(item.getArrivalWarehouse3()) > 0 && !StringUtils.hasText(head.getArrivalWarehouseCode3()))
				    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項有輸入收庫量3，請選擇收貨庫3", head.getLastUpdatedBy());
				
				if(NumberUtils.getDouble(item.getArrivalWarehouse4()) > 0 && !StringUtils.hasText(head.getArrivalWarehouseCode4()))
				    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項有輸入收庫量4，請選擇收貨庫4", head.getLastUpdatedBy());
				
				if(NumberUtils.getDouble(item.getArrivalWarehouse5()) > 0 && !StringUtils.hasText(head.getArrivalWarehouseCode5()))
				    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項有輸入收庫量5，請選擇收貨庫5", head.getLastUpdatedBy());
				
				if(NumberUtils.getDouble(item.getArrivalWarehouse6()) > 0 && !StringUtils.hasText(head.getArrivalWarehouseCode6()))
				    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項有輸入收庫量6，請選擇收貨庫6", head.getLastUpdatedBy());
				
				if(NumberUtils.getDouble(item.getArrivalWarehouse7()) > 0 && !StringUtils.hasText(head.getArrivalWarehouseCode7()))
				    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項有輸入收庫量7，請選擇收貨庫7", head.getLastUpdatedBy());
				
				if(NumberUtils.getDouble(item.getArrivalWarehouse8()) > 0 && !StringUtils.hasText(head.getArrivalWarehouseCode8()))
				    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項有輸入收庫量8，請選擇收貨庫8", head.getLastUpdatedBy());
				
				if(NumberUtils.getDouble(item.getArrivalWarehouse9()) > 0 && !StringUtils.hasText(head.getArrivalWarehouseCode9()))
				    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項有輸入收庫量9，請選擇收貨庫9", head.getLastUpdatedBy());
				
				if(NumberUtils.getDouble(item.getArrivalWarehouse10()) > 0 && !StringUtils.hasText(head.getArrivalWarehouseCode10()))
				    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項有輸入收庫量10，請選擇收貨庫10", head.getLastUpdatedBy());
				
			}
		}

		if(0 == itemCount)
			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請至少輸入一筆正確可用資料", head.getLastUpdatedBy());	
	}
	
	/**
	 * 檢核後的更新
	 */
	public void updateAfterCheck(Map parameterMap, Map returnMap) throws FormException, Exception {
		log.info("updateAfterCheck");
		Object otherBean    = parameterMap.get("vatBeanOther");
		String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
		String identification = (String)PropertyUtils.getProperty(otherBean, "identification");
		ImPickHead head = (ImPickHead)returnMap.get(AjaxUtils.AJAX_FORM);
		updateStorageOnHand(head, formStatus, identification, false);
		deleteLine(parameterMap, returnMap);
		setOrderNo(parameterMap, returnMap);
		head.setStatus(formStatus);
		imPickDAO.update(head);
		returnMap.put(AjaxUtils.AJAX_FORM, head);
	}

	/**
	 * 檢核後的更新
	 */
	public void updateSave(Map parameterMap, Map returnMap) throws FormException, Exception {
		log.info("updateSave");
		Object otherBean    = parameterMap.get("vatBeanOther");
		String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
		ImPickHead head = (ImPickHead)returnMap.get(AjaxUtils.AJAX_FORM);
		setOrderNo(parameterMap, returnMap);
		head.setStatus(formStatus);
		imPickDAO.update(head);
		returnMap.put(AjaxUtils.AJAX_FORM, head);
	}
	
	/**
	 * 檢核後的更新
	 */
	public void updateVoid(Map parameterMap, Map returnMap) throws FormException, Exception {
		log.info("updateVoid");
		Object otherBean    = parameterMap.get("vatBeanOther");
		String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
		ImPickHead head = (ImPickHead)returnMap.get(AjaxUtils.AJAX_FORM);
		head.setStatus(formStatus);
		imPickDAO.update(head);
		returnMap.put(AjaxUtils.AJAX_FORM, head);
	}
	
	public void updateStorageOnHand(ImPickHead head, String nextStatus, String identification, boolean isReject) throws FormException, Exception{
		try{
			log.info("updateStorageOnHand");
			//--------------------------------計算總數--------------------------------
			String status = head.getStatus();
			Map imStorageOnHandMap = new HashMap();
			List<ImPickItem> items = head.getImPickItems();

			for (Iterator iterator = items.iterator(); iterator.hasNext();) {
				ImPickItem item = (ImPickItem) iterator.next();
				if(AjaxUtils.IS_DELETE_RECORD_FALSE.equals(item.getIsDeleteRecord())){

					StringBuffer keyBuffer = new StringBuffer("");
					keyBuffer.append(item.getItemCode()).append("#").append(item.getWarehouseCode());
					keyBuffer.append("#");
					if(null != item.getStorageCode())
						keyBuffer.append(item.getStorageCode());
					keyBuffer.append("#");
					if(null != item.getStorageInNo())
						keyBuffer.append(item.getStorageInNo());
					keyBuffer.append("#");
					if(null != item.getStorageLotNo())
						keyBuffer.append(item.getStorageLotNo());
					String keyString = keyBuffer.toString();

					if(OrderStatus.SAVE.equals(status) && OrderStatus.SIGNING.equals(nextStatus)){
						if(null == imStorageOnHandMap.get(keyString)){
							imStorageOnHandMap.put(keyString, item.getBlockQuantity());	
						}else{
							Double quantity = (Double)imStorageOnHandMap.get(keyString);
							imStorageOnHandMap.put(keyString, quantity + item.getBlockQuantity() );
						}
					}else if(OrderStatus.SIGNING.equals(status) && OrderStatus.FINISH.equals(nextStatus)){
						if(null == imStorageOnHandMap.get(keyString)){
							imStorageOnHandMap.put(keyString, ((item.getBlockQuantity() - item.getCommitQuantity()) * -1) );	
						}else{
							Double quantity = (Double)imStorageOnHandMap.get(keyString);
							imStorageOnHandMap.put(keyString, quantity + ((item.getBlockQuantity() - item.getCommitQuantity()) * -1) );
						}
					}
				}
			}

			//--------------------------------扣庫存--------------------------------
			Iterator imStorageOnHand = imStorageOnHandMap.keySet().iterator();
			while (imStorageOnHand.hasNext()) {
				String keyString = (String) imStorageOnHand.next();
				Double quantity = (Double)imStorageOnHandMap.get(keyString);
				String [] keyStringSet = keyString.split("#");

				String itemCode = null;
				String warehouseCode = null;
				String storageCode = null;
				String storageInNo = null;
				String storageLotNo = null;

				if(keyStringSet.length > 0 && StringUtils.hasText(keyStringSet[0]))
					itemCode = keyStringSet[0];
				if(keyStringSet.length > 1 && StringUtils.hasText(keyStringSet[1]))
					warehouseCode = keyStringSet[1];
				if(keyStringSet.length > 2 && StringUtils.hasText(keyStringSet[2]))
					storageCode = keyStringSet[2];
				if(keyStringSet.length > 3 && StringUtils.hasText(keyStringSet[3]))
					storageInNo = keyStringSet[3];
				if(keyStringSet.length > 4 && StringUtils.hasText(keyStringSet[4]))
					storageLotNo = keyStringSet[4];
				
				log.info("keyString = " + keyString);
				log.info("quantity = " + quantity);
				log.info("itemCode = " + itemCode);
				log.info("warehouseCode = " + warehouseCode);
				log.info("storageCode = " + storageCode);
				log.info("storageInNo = " + storageInNo);
				log.info("storageLotNo = " + storageLotNo);

				if(isReject)
					quantity = quantity * -1;
				if(0 != quantity){
					imStorageOnHandDAO.updateBlockUncommitQuantity("TM", head.getBrandCode(), 
							itemCode, warehouseCode, storageCode, storageInNo, storageLotNo, quantity, head.getLastUpdatedBy(), true);
				}
			}
		}catch(FormException ex){
			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, ex.getMessage(), head.getLastUpdatedBy());
			throw ex;
		}catch(Exception ex){
			throw ex;
		}
	}
	
	
	/**
	 * 刪除單身
	 */
	public void deleteLine(Map parameterMap, Map returnMap) throws Exception {
	    	log.info("deleteLine");
		ImPickHead head = (ImPickHead)returnMap.get(AjaxUtils.AJAX_FORM);
		List<ImPickItem> items = head.getImPickItems();
		if(items != null && items.size() > 0){
			for(int i = items.size() - 1; i >= 0; i--){
				ImPickItem item = items.get(i);         
				if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(item.getIsDeleteRecord())){
					items.remove(item);
				}
			}
		}
		for (int i = 0; i < items.size(); i++) {
			ImPickItem line = items.get(i); 
			line.setIndexNo(i+1L);
		}
		head.setImPickItems(items);
		returnMap.put(AjaxUtils.AJAX_FORM,head);
	}

	/**
	 * 設定單號
	 */
	public void setOrderNo(Map parameterMap, Map returnMap) throws ObtainSerialNoFailedException {
	    	log.info("deleteLine");
		ImPickHead head = (ImPickHead)returnMap.get(AjaxUtils.AJAX_FORM);
		returnMap.put(AjaxUtils.AJAX_FORM, setOrderNo(head));
	}
	
	public ImPickHead setOrderNo(ImPickHead imPickHead) throws ObtainSerialNoFailedException {
		String orderNo = imPickHead.getOrderNo();
		if (null == orderNo || AjaxUtils.isTmpOrderNo(orderNo)) {
			try {
				String serialNo = buOrderTypeService.getOrderSerialNo(imPickHead.getBrandCode(), imPickHead.getOrderTypeCode());
				if ("unknow".equals(serialNo)) 
					throw new ObtainSerialNoFailedException("取得" + imPickHead.getBrandCode() + "-" + imPickHead.getOrderTypeCode() + "單號失敗！");
				else{
					imPickHead.setOrderNo(serialNo);
				}	
			} catch (Exception ex) {
				throw new ObtainSerialNoFailedException("取得" + imPickHead.getOrderTypeCode() + "單號失敗！");
			}
		}
		return imPickHead;
	}
	
	public Map executeInitial(Map parameterMap) throws Exception {
		HashMap returnMap = new HashMap();
		Map multiList = new HashMap(0);
		ImPickHead imPickHead = null;
		try {
			
			Object otherBean = parameterMap.get(AjaxUtils.VAT_BEAN_OTHER);
			String formIdString = (String)PropertyUtils.getProperty(otherBean, AjaxUtils.FORM_ID);
			Long formId =  NumberUtils.getLong(formIdString);
			String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
			
			if(0L != formId)
				imPickHead = findById(formId);
			
			if(null == imPickHead){
				imPickHead = new ImPickHead();
				imPickHead.setBrandCode(loginBrandCode);
				imPickHead.setOrderTypeCode(orderTypeCode);
				imPickHead.setOrderNo(AjaxUtils.getTmpOrderNo());
				imPickHead.setPickDate(new Date());
				imPickHead.setStatus(OrderStatus.SAVE);
				imPickHead.setCreatedBy(loginEmployeeCode);
				imPickHead.setCreatedByName(UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));
				imPickHead.setCreationDate(new Date());
				imPickDAO.save(imPickHead);
			}else{
				imPickHead.setCreatedByName(UserUtils.getUsernameByEmployeeCode(imPickHead.getCreatedBy()));
			}
			
			returnMap.put("statusName", OrderStatus.getChineseWord(imPickHead.getStatus()));
			returnMap.put("brandName", buBrandService.findBrandNameById(imPickHead.getBrandCode()));
			returnMap.put("identification", MessageStatus.getIdentification(imPickHead.getBrandCode(), imPickHead.getOrderTypeCode(), imPickHead.getOrderNo()) );
			returnMap.put("programId",PROGRAM_ID);
			
			List<BuOrderType> allOrderTypeCodes = buOrderTypeService.findOrderbyType(loginBrandCode ,"IMP");
			multiList.put("allOrderTypeCodes" , AjaxUtils.produceSelectorData(allOrderTypeCodes, "orderTypeCode", "name", true, false ));

			List<ImWarehouse> allWarehouses = imWarehouseService.getWarehouseByWarehouseEmployee(imPickHead.getBrandCode(),imPickHead.getCreatedBy(), null);
			multiList.put("allWarehouses" , AjaxUtils.produceSelectorData(allWarehouses, "warehouseCode", "warehouseName", true, true ));

			returnMap.put(AjaxUtils.AJAX_FORM, imPickHead);
			returnMap.put(AjaxUtils.AJAX_MULTILIST, multiList);
			return returnMap;
		} catch (Exception ex) {
			log.error("挑貨單初始化失敗，原因：" + ex.toString());
			throw new Exception("挑貨單初始化失敗，原因：" + ex.getMessage());
		}
	}
	
	/**
	 * 取得明細資料
	 */
	public List<Properties> getAJAXPageLineData(Properties httpRequest) throws Exception{
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();

			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			String brandCode = httpRequest.getProperty("brandCode");

			ImPickHead head = findById(headId);
			
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			HashMap findObjs = new HashMap();
			findObjs.put("and model.imPickHead.headId = :headId", headId);

			Map searchMap = imPickDAO.search("ImPickItem as model", findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE); 
			List<ImPickItem> imPickItems = (List<ImPickItem>)searchMap.get(BaseDAO.TABLE_LIST); 

			HashMap parameterMap = new HashMap();
			parameterMap.put("brandCode", brandCode);
			parameterMap.put("warehouseCode", head.getWarehouseCode());
			
			if (imPickItems != null && imPickItems.size() > 0) {
				// 取得第一筆的INDEX
				Long firstIndex = imPickItems.get(0).getIndexNo(); 
				// 取得最後一筆 INDEX
				Long maxIndex = (Long)imPickDAO.search("ImPickItem as model", "count(model.imPickHead.headId) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				setPickItemInfo(parameterMap, imPickItems);
				result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, imPickItems, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, parameterMap, gridDatas));
			}
			
			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的明細失敗！");
		}
	}
	
	/**
	 * 設定明細資訊
	 */
	private void setPickItemInfo(HashMap parameterMap, List<ImPickItem> imPickItems) throws Exception {
		for (Iterator iterator = imPickItems.iterator(); iterator.hasNext();) {
			ImPickItem imPickItem = (ImPickItem) iterator.next();
			ImItem item = imItemService.findItem((String)parameterMap.get("brandCode"), imPickItem.getItemCode());
			if(null != item)
				imPickItem.setItemCName(item.getItemCName());
			else
				imPickItem.setItemCName(MessageStatus.ITEM_NOT_FOUND);
		}
	}
	
	/**
	 * 更新明細資料
	 */
	public List<Properties> updateAJAXPageLineData(Properties httpRequest)throws Exception {
		String errorMsg = null;
		Long indexNo = 0L;
		try {
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			String warehouseCode = httpRequest.getProperty("warehouseCode");
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
			Date execDate = new Date();
			
			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
			// Get INDEX NO 
			
			ImPickHead imPickHead = findById(headId);
			List<ImPickItem> imPickItems = imPickHead.getImPickItems();
			if(null != imPickItems && imPickItems.size() > 0)
				indexNo = imPickItems.size() + 0L;
			
			Map findObjs = new HashMap();
			findObjs.put("and model.imPickHead.headId = :headId", headId);
			if(OrderStatus.SAVE.equals(imPickHead.getStatus()) || OrderStatus.REJECT.equals(imPickHead.getStatus())){
				// 考慮狀態
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						// 先載入HEAD_ID OR LINE DATA
						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
						String itemCode = upRecord.getProperty("itemCode");
						ImPickItem imPickItem = null;
						if(StringUtils.hasText(itemCode)){
							if(0L != lineId){
								imPickItem = (ImPickItem)imPickDAO.findFirstByProperty("ImPickItem", "and imPickHead.headId = ? and lineId = ?", new Object[]{ headId, lineId } );
								log.info( "imPickItem = " + imPickItem + "\nlineId = " + lineId);
								if ( imPickItem != null ) {
									log.info( "更新 = " + headId + " | "+ lineId  );
									AjaxUtils.setPojoProperties(imPickItem, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
									imPickItem.setBlockQuantity(
										NumberUtils.getDouble(imPickItem.getArrivalWarehouse1()) +
										NumberUtils.getDouble(imPickItem.getArrivalWarehouse2()) + 
										NumberUtils.getDouble(imPickItem.getArrivalWarehouse3()) + 
										NumberUtils.getDouble(imPickItem.getArrivalWarehouse4()) + 
										NumberUtils.getDouble(imPickItem.getArrivalWarehouse5()) + 
										NumberUtils.getDouble(imPickItem.getArrivalWarehouse6()) + 
										NumberUtils.getDouble(imPickItem.getArrivalWarehouse7()) + 
										NumberUtils.getDouble(imPickItem.getArrivalWarehouse8()) + 
										NumberUtils.getDouble(imPickItem.getArrivalWarehouse9()) + 
										NumberUtils.getDouble(imPickItem.getArrivalWarehouse10()) 
									);
									imPickItem.setCommitQuantity(0D);
									imPickItem.setLastUpdatedBy(loginEmployeeCode);
									imPickItem.setLastUpdateDate(execDate);
									imPickDAO.update(imPickItem);
								} else {
									throw new Exception("查無lineId" + lineId + "對應之明細資訊");
								}
							}else{
								indexNo++;
								log.info( "新增 = " + headId + " | "+  indexNo);
								imPickItem = new ImPickItem(); 
								AjaxUtils.setPojoProperties(imPickItem, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								imPickItem.setImPickHead(imPickHead);
								imPickItem.setWarehouseCode(warehouseCode);
								imPickItem.setBlockQuantity(
									NumberUtils.getDouble(imPickItem.getArrivalWarehouse1()) +
									NumberUtils.getDouble(imPickItem.getArrivalWarehouse2()) + 
									NumberUtils.getDouble(imPickItem.getArrivalWarehouse3()) + 
									NumberUtils.getDouble(imPickItem.getArrivalWarehouse4()) + 
									NumberUtils.getDouble(imPickItem.getArrivalWarehouse5()) + 
									NumberUtils.getDouble(imPickItem.getArrivalWarehouse6()) + 
									NumberUtils.getDouble(imPickItem.getArrivalWarehouse7()) + 
									NumberUtils.getDouble(imPickItem.getArrivalWarehouse8()) + 
									NumberUtils.getDouble(imPickItem.getArrivalWarehouse9()) + 
									NumberUtils.getDouble(imPickItem.getArrivalWarehouse10()) 
								);
								imPickItem.setCommitQuantity(0D);
								imPickItem.setCreatedBy(loginEmployeeCode);
								imPickItem.setCreationDate(execDate);
								imPickItem.setLastUpdatedBy(loginEmployeeCode);
								imPickItem.setLastUpdateDate(execDate);
								imPickItem.setIndexNo(indexNo);
								imPickDAO.save(imPickItem);
							}
						}
					}
				}
			}
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新儲位單明細資料發生錯誤，原因：" + ex.toString());
			throw new Exception("更新儲位單明細資料失敗！");
		}   
	}
	
	/**
     * 匯出挑貨單 by sql
     * @return
     * @throws Exception
     */
	public SelectDataInfo getAJAXExportDataBySql(HttpServletRequest httpRequest) throws Exception{
		Object[] object = null;
		List rowData = new ArrayList();
		StringBuffer sql = new StringBuffer();
		try {
			Long headId = NumberUtils.getLong(httpRequest.getParameter("headId"));
			sql.append("")
			.append(" SELECT '品號' AS ITEM_CODE, ")
			.append(" '進倉日' AS STORAGE_IN_NO, ")
			.append(" '批號效期' AS STORAGE_LOT_NO, ")
			.append(" '儲位' AS STORAGE_CODE, ")
			.append(" '預扣量' AS BLOCK_QUANTITY, ")
			.append(" ARRIVAL_WAREHOUSE_CODE1 AS ARRIVAL_WAREHOUSE1, ")
			.append(" ARRIVAL_WAREHOUSE_CODE2 AS ARRIVAL_WAREHOUSE2, ")
			.append(" ARRIVAL_WAREHOUSE_CODE3 AS ARRIVAL_WAREHOUSE3, ")
			.append(" ARRIVAL_WAREHOUSE_CODE4 AS ARRIVAL_WAREHOUSE4, ")
			.append(" ARRIVAL_WAREHOUSE_CODE5 AS ARRIVAL_WAREHOUSE5, ")
			.append(" ARRIVAL_WAREHOUSE_CODE6 AS ARRIVAL_WAREHOUSE6, ")
			.append(" ARRIVAL_WAREHOUSE_CODE7 AS ARRIVAL_WAREHOUSE7, ")
			.append(" ARRIVAL_WAREHOUSE_CODE8 AS ARRIVAL_WAREHOUSE8, ")
			.append(" ARRIVAL_WAREHOUSE_CODE9 AS ARRIVAL_WAREHOUSE9, ")
			.append(" ARRIVAL_WAREHOUSE_CODE10 AS ARRIVAL_WAREHOUSE10 ")
			.append(" FROM IM_PICK_HEAD WHERE HEAD_ID = ")
			.append(headId)
			.append(" UNION ALL  ")
			.append(" SELECT * FROM (SELECT ITEM_CODE, ")
			.append(" STORAGE_IN_NO, ")
			.append(" STORAGE_LOT_NO, ")
			.append(" STORAGE_CODE, ")
			.append(" TO_CHAR(BLOCK_QUANTITY), ")
			.append(" TO_CHAR(DECODE(ARRIVAL_WAREHOUSE1,0,NULL,ARRIVAL_WAREHOUSE1)), ")
			.append(" TO_CHAR(DECODE(ARRIVAL_WAREHOUSE2,0,NULL,ARRIVAL_WAREHOUSE2)), ")
			.append(" TO_CHAR(DECODE(ARRIVAL_WAREHOUSE3,0,NULL,ARRIVAL_WAREHOUSE3)), ")
			.append(" TO_CHAR(DECODE(ARRIVAL_WAREHOUSE4,0,NULL,ARRIVAL_WAREHOUSE4)), ")
			.append(" TO_CHAR(DECODE(ARRIVAL_WAREHOUSE5,0,NULL,ARRIVAL_WAREHOUSE5)), ")
			.append(" TO_CHAR(DECODE(ARRIVAL_WAREHOUSE6,0,NULL,ARRIVAL_WAREHOUSE6)), ")
			.append(" TO_CHAR(DECODE(ARRIVAL_WAREHOUSE7,0,NULL,ARRIVAL_WAREHOUSE7)), ")
			.append(" TO_CHAR(DECODE(ARRIVAL_WAREHOUSE8,0,NULL,ARRIVAL_WAREHOUSE8)), ")
			.append(" TO_CHAR(DECODE(ARRIVAL_WAREHOUSE9,0,NULL,ARRIVAL_WAREHOUSE9)), ")
			.append(" TO_CHAR(DECODE(ARRIVAL_WAREHOUSE10,0,NULL,ARRIVAL_WAREHOUSE10)) ")
			.append(" FROM IM_PICK_HEAD H, IM_PICK_ITEM L ")
			.append(" WHERE H.HEAD_ID = L.HEAD_ID AND H.HEAD_ID = ")
			.append(headId)
			.append(" ORDER BY L.INDEX_NO) ");

			object = new Object[] { 
					"itemCode", "storageInNo", "storageLotNo", "storageCode",
					"blockQuantity", "arrivalWarehouse1", "arrivalWarehouse2", "arrivalWarehouse3",
					"arrivalWarehouse4", "arrivalWarehouse5", "arrivalWarehouse6", "arrivalWarehouse7",
					"arrivalWarehouse8", "arrivalWarehouse9", "arrivalWarehouse10"
			};

			List lists = nativeQueryDAO.executeNativeSql(sql.toString());
			for (int i = 0; i < (lists.size() > 65535 ? 65535 : lists.size()); i++) {
				Object[] getObj = (Object[])lists.get(i);
				Object[] dataObject = new Object[object.length];
				for (int j = 0; j < (object.length); j++) {
					dataObject[j] = getObj[j];
				}
				rowData.add(dataObject);
			}
			return new SelectDataInfo(object, rowData);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("匯出excel發生錯誤，原因：" + e.getMessage());
		}
	}
	
	/**
	 * 匯入挑貨單
	 * @param headId
	 * @param lineLists
	 * @throws Exception
	 */
	public void executeImportLists(Long headId, List lists) throws Exception{
	    	log.info("executeImportLists");
		try{
			ImPickHead imPickHead = findById(headId);
			String status = imPickHead.getStatus();
			if(lists != null && lists.size() > 0){
				//刪掉line重新載入
				if(OrderStatus.SAVE.equals(status)||OrderStatus.REJECT.equals(status)||OrderStatus.UNCONFIRMED.equals(status)){
					imPickHead.setImPickItems(null);
					for(int i = 0; i < lists.size(); i++){
						ImPickItem item = (ImPickItem)lists.get(i);
						item.setImPickHead(imPickHead);
						item.setWarehouseCode(imPickHead.getWarehouseCode());
						item.setBlockQuantity(
							NumberUtils.getDouble(item.getArrivalWarehouse1()) +
							NumberUtils.getDouble(item.getArrivalWarehouse2()) + 
							NumberUtils.getDouble(item.getArrivalWarehouse3()) + 
							NumberUtils.getDouble(item.getArrivalWarehouse4()) + 
							NumberUtils.getDouble(item.getArrivalWarehouse5()) + 
							NumberUtils.getDouble(item.getArrivalWarehouse6()) + 
							NumberUtils.getDouble(item.getArrivalWarehouse7()) + 
							NumberUtils.getDouble(item.getArrivalWarehouse8()) + 
							NumberUtils.getDouble(item.getArrivalWarehouse9()) + 
							NumberUtils.getDouble(item.getArrivalWarehouse10()) 
						);
						item.setCommitQuantity(0D);
						item.setIndexNo(i+1L);
						imPickDAO.save(item);
					}
					imPickHead.setImPickItems(lists);
				}
			}
		}catch (Exception ex) {
			log.error("挑貨單明細匯入時發生錯誤，原因：" + ex.toString());
			throw new Exception("挑貨單明細匯入時發生錯誤，原因：" + ex.getMessage());
		}        
	}
	
	/**
	 * 更換庫別
	 */
	public List<Properties> updateChangeWarehouseCode(Properties httpRequest) throws Exception{
		log.info("updateChangeWarehouseCode");
		List<Properties> result = new ArrayList();
		Properties properties   = new Properties();
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		String warehouseCode = httpRequest.getProperty("warehouseCode");
		String arrivalWarehouseCode1 = httpRequest.getProperty("arrivalWarehouseCode1");
		String arrivalWarehouseCode2 = httpRequest.getProperty("arrivalWarehouseCode2");
		String arrivalWarehouseCode3 = httpRequest.getProperty("arrivalWarehouseCode3");
		String arrivalWarehouseCode4 = httpRequest.getProperty("arrivalWarehouseCode4");
		String arrivalWarehouseCode5 = httpRequest.getProperty("arrivalWarehouseCode5");
		String arrivalWarehouseCode6 = httpRequest.getProperty("arrivalWarehouseCode6");
		String arrivalWarehouseCode7 = httpRequest.getProperty("arrivalWarehouseCode7");
		String arrivalWarehouseCode8 = httpRequest.getProperty("arrivalWarehouseCode8");
		String arrivalWarehouseCode9 = httpRequest.getProperty("arrivalWarehouseCode9");
		String arrivalWarehouseCode10 = httpRequest.getProperty("arrivalWarehouseCode10");
		
		try{
			ImPickHead imPickHead = findById(headId);

			imPickHead.setWarehouseCode(warehouseCode);
			List<ImPickItem> imPickItems = imPickHead.getImPickItems();
			for (Iterator iterator = imPickItems.iterator(); iterator.hasNext();) {
				ImPickItem imPickItem = (ImPickItem) iterator.next();
				imPickItem.setWarehouseCode(warehouseCode);
			}

			imPickHead.setArrivalWarehouseCode1(arrivalWarehouseCode1);
			imPickHead.setArrivalWarehouseCode2(arrivalWarehouseCode2);
			imPickHead.setArrivalWarehouseCode3(arrivalWarehouseCode3);
			imPickHead.setArrivalWarehouseCode4(arrivalWarehouseCode4);
			imPickHead.setArrivalWarehouseCode5(arrivalWarehouseCode5);
			imPickHead.setArrivalWarehouseCode6(arrivalWarehouseCode6);
			imPickHead.setArrivalWarehouseCode7(arrivalWarehouseCode7);
			imPickHead.setArrivalWarehouseCode8(arrivalWarehouseCode8);
			imPickHead.setArrivalWarehouseCode9(arrivalWarehouseCode9);
			imPickHead.setArrivalWarehouseCode10(arrivalWarehouseCode10);
			
			imPickDAO.update(imPickHead);
			result.add(properties);
			return result;
		}catch (Exception ex) {
			log.error("更換庫別時發生錯誤，原因：" + ex.getMessage());
			throw new Exception("更換庫別時發生錯誤失敗！");
		}
	}
	
	/**執行挑貨單查詢初始化
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception{
    	log.info("executeSearchInitial");
        HashMap resultMap = new HashMap();
        try{
            Object otherBean = parameterMap.get("vatBeanOther");
		    String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");
		    String employeeCode     = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
		    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
		    BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode) );
		    if(null == buOrderType)
		    	throw new Exception("查無對應之單據類別");
		    Map multiList = new HashMap(0);
		    List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(brandCode, buOrderType.getTypeCode());
		    multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
		    List<ImWarehouse> allWarehouses = imWarehouseService.getWarehouseByWarehouseEmployee(brandCode, employeeCode, null);
			multiList.put("allWarehouses" , AjaxUtils.produceSelectorData(allWarehouses, "warehouseCode", "warehouseName", true, true ));
		    resultMap.put("multiList",multiList);
			
		    return resultMap;
        }catch (Exception ex) {
        	log.error("儲位單查詢初始化失敗，原因：" + ex.toString());
        	throw new Exception("儲位單查詢初始化失敗，原因：" + ex.toString());
        }           
    }
    
    /**
	 * 取得查詢的單身
	 */
	public List<Properties> getAJAXSearchPageData(Properties httpRequest)throws Exception {
		try{
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			//======================帶入Head的值=========================

			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 品牌代號
			String orderNo = httpRequest.getProperty("orderNo" ); // 單號
			Date startDate = DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH,httpRequest.getProperty("startDate") );
			Date endDate = DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH,httpRequest.getProperty("endDate") );
			String warehouseCode = httpRequest.getProperty("warehouseCode");
			String status = httpRequest.getProperty("status");
			
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put(" and model.brandCode = :brandCode",brandCode);
			findObjs.put(" and model.orderTypeCode = :orderTypeCode",orderTypeCode);
			findObjs.put(" and model.orderNo NOT LIKE :TMP","TMP%");
			findObjs.put(" and model.orderNo = :orderNo",orderNo);
			findObjs.put(" and model.pickDate >= :startDate",startDate);
			findObjs.put(" and model.pickDate <= :endDate",endDate);
			findObjs.put(" and model.warehouseCode = :warehouseCode",warehouseCode);
			findObjs.put(" and model.status = :status",status);
			//==============================================================	    
			Map headMap = baseDAO.search( "ImPickHead as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
			List<ImStorageHead> heads = (List<ImStorageHead>) headMap.get(BaseDAO.TABLE_LIST); 
			if (heads != null && heads.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
				Long maxIndex = (Long)baseDAO.search("ImPickHead as model", "count(model.headId) as rowCount" ,findObjs, "order by lastUpdateDate desc", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,heads, gridDatas, firstIndex, maxIndex));
			}else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
			}
			return result;
		}catch(Exception ex){
			log.error("載入頁面顯示的挑貨單查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的挑貨單查詢失敗！");
		}		
	}
	
	/**
	 * 將挑貨查詢結果存檔
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
	public List<Properties> getSearchSelection(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try{
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
			if(result.size() > 0 ){
				pickerResult.put("imPickResult", result);
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
	 * 各單據呼叫儲位單扣庫存
	 */
	public void updateBlockQty(Map pickMap, Object objHead) throws Exception {
		log.info("updateBlockQty");
		String brandCode = BeanUtils.getNestedProperty(objHead, "brandCode");
		String sourceOrderTypeCode = BeanUtils.getNestedProperty(objHead, "sourceOrderTypeCode");
		String sourceOrderNo = BeanUtils.getNestedProperty(objHead, "sourceOrderNo");
		ImPickHead head = (ImPickHead)baseDAO.findFirstByProperty("ImPickHead", "",
				" and brandCode = ? and orderTypeCode = ? and orderNo = ? ",
				new String[] {brandCode, sourceOrderTypeCode, sourceOrderNo} , "");
		if(null == head)
			throw new Exception("查無對應挑貨單");
		
		List objItems = (List)BeanUtil.getBeanValue(pickMap, objHead, "beanItem");
		for (Iterator iterator = objItems.iterator(); iterator.hasNext();) {
			Object objItem = (Object) iterator.next();
		}
		
		baseDAO.update(head);
	}

	public static Object[] startProcess(ImPickHead head) throws ProcessFailedException{       
		log.info("startProcess");
		try{           
			String packageId = "Im_Pick";         
			String processId = "create";           
			String version = "20111124";
			String sourceReferenceType = "";
			HashMap context = new HashMap();
			context.put("formId", head.getHeadId());
			return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		}catch (Exception ex){
			log.error("挑貨單流程啟動失敗，原因：" + ex.toString());
			throw new ProcessFailedException("挑貨單流程啟動失敗！");
		}	      
	}
	
	public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws ProcessFailedException{
		log.info("completeAssignment");
		try{           
			HashMap context = new HashMap();
			context.put("approveResult", approveResult);
			return ProcessHandling.completeAssignment(assignmentId, context);
		}catch (Exception ex){
			log.error("完成挑貨單任務失敗，原因：" + ex.toString());
			throw new ProcessFailedException("完成挑貨單任務失敗！");
		}
	}
	
	public List executeBatchImport(List<ImPickHead> imPickHeads) throws Exception {
		try {
			if (imPickHeads != null) {
				for (int i = 0; i < imPickHeads.size(); i++) {
					ImPickHead imPickHead = (ImPickHead) imPickHeads.get(i);
					imPickHead.setStatus(OrderStatus.SAVE);
					setOrderNo(imPickHead);
					imPickDAO.save(imPickHead);
				}
			}
			return imPickHeads;
		} catch (Exception ex) {
			log.error("執行調撥資料批次匯入失敗，原因：" + ex.toString());
			throw ex;
		}
	}
	
	/**
	 * 取得CC開窗URL字串
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getReportConfig(Map parameterMap) throws Exception  {
	    	log.info("getReportConfig");
		try{
			
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
			String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
			String orderNo = (String)PropertyUtils.getProperty(otherBean, "orderNo");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			
			Map returnMap = new HashMap(0);
			Map parameters = new HashMap(0);
			//CC後面要代的參數使用parameters傳遞			
			parameters.put("prompt0", brandCode);
			parameters.put("prompt1", orderTypeCode);
			parameters.put("prompt2", orderNo);

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
	 * 明細數量倒扣
	 */
	public List<Properties> updateResetItems(Properties httpRequest) throws Exception{
		log.info("updateResetItems");
		List<Properties> result = new ArrayList();
		Properties properties   = new Properties();
		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		HashMap itemMap = new HashMap();
		try{
			ImPickHead imPickHead = findById(headId);
			List<ImPickItem> imPickItems = imPickHead.getImPickItems();
			for (Iterator iterator = imPickItems.iterator(); iterator.hasNext();) {
				ImPickItem imPickItem = (ImPickItem) iterator.next();
				
				//用來計算若一筆明細拆三筆(同品號)，則自動拆分
				if(null != itemMap.get("itemCode")){
				    if(!itemMap.get("itemCode").equals(imPickItem.getItemCode()))
					itemMap = new HashMap();
				}
				
				Double blockQuantity = 	NumberUtils.getDouble(imPickItem.getBlockQuantity()); 
				Double arrivalWarehouse1 = NumberUtils.getDouble(imPickItem.getArrivalWarehouse1());
				if(null != itemMap.get("arrivalWarehouse1"))
				    arrivalWarehouse1 -= (Double)itemMap.get("arrivalWarehouse1");
					
				Double arrivalWarehouse2 = NumberUtils.getDouble(imPickItem.getArrivalWarehouse2());
				if(null != itemMap.get("arrivalWarehouse2"))
				    arrivalWarehouse2 -= (Double)itemMap.get("arrivalWarehouse2");
				
				Double arrivalWarehouse3 = NumberUtils.getDouble(imPickItem.getArrivalWarehouse3());
				if(null != itemMap.get("arrivalWarehouse3"))
				    arrivalWarehouse3 -= (Double)itemMap.get("arrivalWarehouse3");
				
				Double arrivalWarehouse4 = NumberUtils.getDouble(imPickItem.getArrivalWarehouse4());
				if(null != itemMap.get("arrivalWarehouse4"))
				    arrivalWarehouse4 -= (Double)itemMap.get("arrivalWarehouse4");
				
				Double arrivalWarehouse5 = NumberUtils.getDouble(imPickItem.getArrivalWarehouse5());
				if(null != itemMap.get("arrivalWarehouse5"))
				    arrivalWarehouse5 -= (Double)itemMap.get("arrivalWarehouse5");
				
				Double arrivalWarehouse6 = NumberUtils.getDouble(imPickItem.getArrivalWarehouse6());
				if(null != itemMap.get("arrivalWarehouse6"))
				    arrivalWarehouse6 -= (Double)itemMap.get("arrivalWarehouse6");
				
				Double arrivalWarehouse7 = NumberUtils.getDouble(imPickItem.getArrivalWarehouse7());
				if(null != itemMap.get("arrivalWarehouse7"))
				    arrivalWarehouse7 -= (Double)itemMap.get("arrivalWarehouse7");
				
				Double arrivalWarehouse8 = NumberUtils.getDouble(imPickItem.getArrivalWarehouse8());
				if(null != itemMap.get("arrivalWarehouse8"))
				    arrivalWarehouse8 -= (Double)itemMap.get("arrivalWarehouse8");
				
				Double arrivalWarehouse9 = NumberUtils.getDouble(imPickItem.getArrivalWarehouse9());
				if(null != itemMap.get("arrivalWarehouse9"))
				    arrivalWarehouse9 -= (Double)itemMap.get("arrivalWarehouse9");
				
				Double arrivalWarehouse10 = NumberUtils.getDouble(imPickItem.getArrivalWarehouse10());
				if(null != itemMap.get("arrivalWarehouse10"))
				    arrivalWarehouse10 -= (Double)itemMap.get("arrivalWarehouse10");
				
				if(blockQuantity - arrivalWarehouse1 >= 0){
				    imPickItem.setArrivalWarehouse1(arrivalWarehouse1);
				    blockQuantity -= arrivalWarehouse1;
				    itemMap.put("arrivalWarehouse1", arrivalWarehouse1);
				}else{
				    imPickItem.setArrivalWarehouse1(blockQuantity);
				    blockQuantity = 0D;
				    itemMap.put("arrivalWarehouse1", blockQuantity);
				}
				
				if(blockQuantity - arrivalWarehouse2 >= 0){
				    imPickItem.setArrivalWarehouse2(arrivalWarehouse2);
				    blockQuantity -= arrivalWarehouse2;
				    itemMap.put("arrivalWarehouse2", arrivalWarehouse2);
				}else{
				    imPickItem.setArrivalWarehouse2(blockQuantity);
				    blockQuantity = 0D;
				    itemMap.put("arrivalWarehouse2", blockQuantity);
				    
				}
				
				if(blockQuantity - arrivalWarehouse3 >= 0){
				    imPickItem.setArrivalWarehouse3(arrivalWarehouse3);
				    blockQuantity -= arrivalWarehouse3;
				    itemMap.put("arrivalWarehouse3", arrivalWarehouse3);
				}else{
				    imPickItem.setArrivalWarehouse3(blockQuantity);
				    blockQuantity = 0D;
				    itemMap.put("arrivalWarehouse3", blockQuantity);
				}
				
				if(blockQuantity - arrivalWarehouse4 >= 0){
				    imPickItem.setArrivalWarehouse4(arrivalWarehouse4);
				    blockQuantity -= arrivalWarehouse4;
				    itemMap.put("arrivalWarehouse4", arrivalWarehouse4);
				}else{
				    imPickItem.setArrivalWarehouse4(blockQuantity);
				    blockQuantity = 0D;
				    itemMap.put("arrivalWarehouse4", blockQuantity);
				}
				
				if(blockQuantity - arrivalWarehouse5 >= 0){
				    imPickItem.setArrivalWarehouse5(arrivalWarehouse5);
				    blockQuantity -= arrivalWarehouse5;
				    itemMap.put("arrivalWarehouse5", arrivalWarehouse5);
				}else{
				    imPickItem.setArrivalWarehouse5(blockQuantity);
				    blockQuantity = 0D;
				    itemMap.put("arrivalWarehouse5", blockQuantity);
				}
				
				if(blockQuantity - arrivalWarehouse6 >= 0){
				    imPickItem.setArrivalWarehouse6(arrivalWarehouse6);
				    blockQuantity -= arrivalWarehouse6;
				    itemMap.put("arrivalWarehouse6", arrivalWarehouse6);
				}else{
				    imPickItem.setArrivalWarehouse6(blockQuantity);
				    blockQuantity = 0D;
				    itemMap.put("arrivalWarehouse6", blockQuantity);
				}
				
				if(blockQuantity - arrivalWarehouse7 >= 0){
				    imPickItem.setArrivalWarehouse7(arrivalWarehouse7);
				    blockQuantity -= arrivalWarehouse7;
				    itemMap.put("arrivalWarehouse7", arrivalWarehouse7);
				}else{
				    imPickItem.setArrivalWarehouse7(blockQuantity);
				    blockQuantity = 0D;
				    itemMap.put("arrivalWarehouse7", blockQuantity);
				}
				
				if(blockQuantity - arrivalWarehouse8 >= 0){
				    imPickItem.setArrivalWarehouse8(arrivalWarehouse8);
				    blockQuantity -= arrivalWarehouse8;
				    itemMap.put("arrivalWarehouse8", arrivalWarehouse8);
				}else{
				    imPickItem.setArrivalWarehouse8(blockQuantity);
				    blockQuantity = 0D;
				    itemMap.put("arrivalWarehouse8", blockQuantity);
				}
				
				if(blockQuantity - arrivalWarehouse9 >= 0){
				    imPickItem.setArrivalWarehouse9(arrivalWarehouse9);
				    blockQuantity -= arrivalWarehouse9;
				    itemMap.put("arrivalWarehouse9", arrivalWarehouse9);
				}else{
				    imPickItem.setArrivalWarehouse9(blockQuantity);
				    blockQuantity = 0D;
				    itemMap.put("arrivalWarehouse9", blockQuantity);
				}
				
				if(blockQuantity - arrivalWarehouse10 >= 0){
				    imPickItem.setArrivalWarehouse10(arrivalWarehouse10);
				    blockQuantity -= arrivalWarehouse10;
				    itemMap.put("arrivalWarehouse10", arrivalWarehouse10);
				}else{
				    imPickItem.setArrivalWarehouse10(blockQuantity);
				    blockQuantity = 0D;
				    itemMap.put("arrivalWarehouse10", blockQuantity);
				}
				
			}
			
			imPickDAO.update(imPickHead);
			result.add(properties);
			return result;
		}catch (Exception ex) {
			log.error("更換庫別時發生錯誤，原因：" + ex.getMessage());
			throw new Exception("更換庫別時發生錯誤失敗！");
		}
	}
}