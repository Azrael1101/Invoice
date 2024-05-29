package tw.com.tm.erp.importdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.dao.BuLocationDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.User;

/**
 * 調撥單匯入
 *
 * fix 20081210 line id 不匯入
 *
 * @author T02049
 *
 */

public class ImMovementImportData implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(ImMovementImportData.class);
	public static final String split[] = { "{#}" };

	// private static final String ORGANIZATION_CODE = "TM";

	private static ApplicationContext context = SpringUtils.getApplicationContext();

	public ImportInfo initial(HashMap uiProperties) {
		log.info("initial");
		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImMovementHead.class.getName());
		imInfo.setSplit(split);

		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("H");
		imInfo.setSaveKeyField(true);

		// set field name
		imInfo.addFieldName("h");
		imInfo.addFieldName("brandCode");
		imInfo.addFieldName("orderTypeCode");
		imInfo.addFieldName("orderNo");
		imInfo.addFieldName("deliveryDate");
		imInfo.addFieldName("deliveryWarehouseCode");
		imInfo.addFieldName("arrivalWarehouseCode");
		imInfo.addFieldName("remark1");

		// set field type
		imInfo.setFieldType("h", "java.lang.String");
		imInfo.setFieldType("brandCode", "java.lang.String");
		imInfo.setFieldType("orderTypeCode", "java.lang.String");
		imInfo.setFieldType("orderNo", "java.lang.String");
		imInfo.setFieldType("deliveryDate", "java.util.Date");
		imInfo.setFieldType("deliveryWarehouseCode", "java.lang.String");
		imInfo.setFieldType("arrivalWarehouseCode", "java.lang.String");
		imInfo.setFieldType("remark1", "java.lang.String");

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyyMMdd");

		// set default value
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
		HashMap defaultValue = new HashMap();
		defaultValue.put("creationDate", new Date());
		defaultValue.put("lastUpdateDate", new Date());
		defaultValue.put("createdBy", user.getEmployeeCode());
		defaultValue.put("lastUpdatedBy", user.getEmployeeCode());
		defaultValue.put("fileName", uiProperties.get(ImportDBService.UPLOAD_FILE_NAME));
		imInfo.setDefaultValue(defaultValue);

		// add detail
		imInfo.addDetailImportInfos(getImMovementItems());
		return imInfo;
	}

	/**
	 * im item price detail config
	 *
	 * @return
	 */
	private ImportInfo getImMovementItems() {
		log.info("getImMovementItems");
		ImportInfo imInfo = new ImportInfo();

		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("D");
		imInfo.setSaveKeyField(true);
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImMovementItem.class.getName());

		imInfo.addFieldName("d");
		// imInfo.addFieldName("lineId");
		imInfo.addFieldName("deliveryWarehouseCode");
		imInfo.addFieldName("itemCode");
		imInfo.addFieldName("deliveryQuantity");

		imInfo.setFieldType("d", "java.lang.String");
		// imInfo.setFieldType("lineId", "java.lang.Long");
		imInfo.setFieldType("deliveryWarehouseCode", "java.lang.String");
		imInfo.setFieldType("itemCode", "java.lang.String");
		imInfo.setFieldType("deliveryQuantity", "java.lang.Double");

		return imInfo;
	}

	public String updateDB(List entityBeans, ImportInfo info) throws Exception {
		log.info("updateDB");
		StringBuffer reMsg = new StringBuffer();
		try {
			HashMap uiProperties = info.getUiProperties();
			User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
			String defaultLotNo = (String) uiProperties.get("defaultLotNo");// UI輸入的預設批號
			String organizationCode = user.getOrganizationCode();
			String employeeCode = user.getEmployeeCode();
			String brandCode = null;
			String orderTypeCode = null;
			String orderNo = null;
			List<ImMovementHead> imMovementHeads = new ArrayList(0);
			List assembly = new ArrayList(0);

			ImMovementService imMovementService = (ImMovementService) context.getBean("imMovementService");
			ImWarehouseDAO imWarehouseDAO = (ImWarehouseDAO) context.getBean("imWarehouseDAO");
			ImOnHandDAO imOnHandDAO = (ImOnHandDAO) context.getBean("imOnHandDAO");
			BuLocationDAO buLocationDAO = (BuLocationDAO) context.getBean("buLocationDAO");
			for (int index = 0; index < entityBeans.size(); index++) {
			        ImMovementHead entityBean = (ImMovementHead) entityBeans.get(index);
			        String dwcode = entityBean.getDeliveryWarehouseCode();
			        String awcode = entityBean.getArrivalWarehouseCode();
			        brandCode = entityBean.getBrandCode();
			        orderTypeCode = entityBean.getOrderTypeCode();
			        orderNo = entityBean.getOrderNo();
			        String identification = MessageStatus.getIdentificationMsg(brandCode, orderTypeCode, orderNo);

				/*if (!"IMV".equals(orderTypeCode) && !"IMD".equals(orderTypeCode)) {
				        throw new ValidationErrorException("品牌：" + brandCode + "其單別：" + orderTypeCode + "無法執行匯入！");
				} else*/ if ("IMD".equals(orderTypeCode) && !StringUtils.hasText(orderNo)) {
				        throw new ValidationErrorException("品牌：" + brandCode + "、其單別：" + orderTypeCode + "必須有單號才能執行匯入！");
				} else {
				        // 取得出庫人員
					HashMap findObjs = new HashMap();
					findObjs.put("warehouseCode", dwcode);
					findObjs.put("brandCode", brandCode);
					List<ImWarehouse> wRecords = imWarehouseDAO.find(findObjs);
					if (null != wRecords && wRecords.size() > 0) {
					        ImWarehouse deliveryWarehouse = (ImWarehouse)wRecords.get(0);
					        entityBean.setDeliveryContactPerson(deliveryWarehouse.getWarehouseManager());
					        Long deliveryLocationId = deliveryWarehouse.getLocationId();
						if(deliveryLocationId != null){
						        BuLocation deliveryLocation = (BuLocation) buLocationDAO.findByPrimaryKey(BuLocation.class, deliveryLocationId);
				                        if(deliveryLocation != null){
							        entityBean.setDeliveryAddress(deliveryLocation.getAddress());
							        entityBean.setDeliveryArea(deliveryLocation.getArea());
							        entityBean.setDeliveryCity(deliveryLocation.getCity());
							        entityBean.setDeliveryZipCode(deliveryLocation.getZipCode());
				                        }
						}
					}else{
						throw new NoSuchDataException("查無出庫單位：" + dwcode + "的資料！");
					}
					// 取得入庫人員
					findObjs.put("warehouseCode", awcode);
					wRecords = imWarehouseDAO.find(findObjs);
					if (null != wRecords && wRecords.size() > 0) {
					        ImWarehouse arrivalWarehouse = (ImWarehouse)wRecords.get(0);
						entityBean.setArrivalContactPerson(arrivalWarehouse.getWarehouseManager());
						Long arrivalLocationId = arrivalWarehouse.getLocationId();
						if(arrivalLocationId != null){
						    BuLocation arrivalLocation = (BuLocation) buLocationDAO.findByPrimaryKey(BuLocation.class, arrivalLocationId);
				                    if(arrivalLocation != null){
				                	entityBean.setArrivalAddress(arrivalLocation.getAddress());
				                	entityBean.setArrivalArea(arrivalLocation.getArea());
				                	entityBean.setArrivalCity(arrivalLocation.getCity());
				                	entityBean.setArrivalZipCode( arrivalLocation.getZipCode());
				                    }
						}
					}else{
						throw new NoSuchDataException("查無入庫單位：" + awcode + "的資料！");
					}

					ImMovementHead movementHeadPO = null;
					// orderNo為空值新增調撥單，不為空值更新調撥單
					if (StringUtils.hasText(orderNo)) {
					        movementHeadPO = imMovementService.findMovementByIdentification(brandCode, orderTypeCode, orderNo);
						if (movementHeadPO != null) {
					                if ("IMV".equals(orderTypeCode)) {
							        if (OrderStatus.FORM_SAVE.equals(movementHeadPO.getStatus())) {
							                try{
									        movementHeadPO = setDate(movementHeadPO, entityBean, defaultLotNo, organizationCode, employeeCode, imMovementService, imOnHandDAO);
									        imMovementHeads.add(movementHeadPO);
							                }catch(Exception ex){
							                        throw new Exception(identification + "匯入發生錯誤；" + ex.toString());
							                }
								} else {
								        throw new ValidationErrorException(identification + "的單據狀態不為" + OrderStatus.getChineseWord(OrderStatus.FORM_SAVE) + "，無法執行匯入！");
								}
							} else {
							        if (OrderStatus.WAIT_OUT.equals(movementHeadPO.getStatus())) {
							                try{
									        movementHeadPO = setDate(movementHeadPO, entityBean, defaultLotNo, organizationCode, employeeCode, imMovementService, imOnHandDAO);
									        imMovementHeads.add(movementHeadPO);
							                }catch(Exception ex){
							                        throw new Exception(identification + "匯入發生錯誤；" + ex.toString());
							                }
								} else {
								        throw new ValidationErrorException(identification + "的單據狀態不為" + OrderStatus.getChineseWord(OrderStatus.WAIT_OUT) + "，無法執行匯入！");
								}
							}
						} else {
						        throw new NoSuchDataException("查無" +identification + "的調撥資料！");
						}
					} else {
						entityBean = setDate(movementHeadPO, entityBean, defaultLotNo, organizationCode, employeeCode, imMovementService, imOnHandDAO);
						imMovementHeads.add(entityBean);
					}
				}

			}
			imMovementService.executeBatchImport(imMovementHeads, assembly);
			reMsg.append("調撥檔匯入成功！<br>");
			reMsg.append("匯入單號如下：<br>");
			for (int k = 0; k < assembly.size(); k++) {
			        reMsg.append(assembly.get(k) + "<br>");
			}
		} catch (Exception ex) {
			log.error("調撥檔匯入失敗！原因：" + ex.toString());
			reMsg.append("調撥檔匯入失敗！原因：" + ex.getMessage());
		}
		return reMsg.toString();
	}

	private ImMovementHead setDate(ImMovementHead movementHeadPO, ImMovementHead movementHead, String defaultLotNo,
			String organizationCode, String employeeCode, ImMovementService imMovementService, ImOnHandDAO imOnHandDAO) throws Exception {

		String orderTypeCode = movementHead.getOrderTypeCode();
		List imMovementItems = movementHead.getImMovementItems();
		List imMovementItemsPO = null;
		if (movementHeadPO != null) {
			imMovementItemsPO = movementHeadPO.getImMovementItems();
		}
		List actualSaveMovementItems = new ArrayList(0);
		// set line data
		if ("IMV".equals(orderTypeCode) || "IMI".equals(orderTypeCode)) {
			if (imMovementItems != null && imMovementItems.size() > 0) {
				actualSaveMovementItems = getMovementItemsWithLotNo(movementHead, organizationCode, defaultLotNo);
			}
		} else {
			if (imMovementItemsPO != null && imMovementItemsPO.size() > 0 && imMovementItems != null && imMovementItems.size() > 0) {
				TreeMap deliveryQuantityMap = getDeliveryQuantityToMap(imMovementItems, "N");
				TreeMap origDeliveryQuantityMap = getDeliveryQuantityToMap(imMovementItemsPO, "Y");
				doValidate(origDeliveryQuantityMap, deliveryQuantityMap);
				// 分配實際配貨數量
				distributeDeliveryQuantity(imMovementItemsPO, deliveryQuantityMap);
			}
		}

		// set head data
		if (movementHeadPO != null) {
			if ("IMV".equals(orderTypeCode)) {
				// 先刪除原調撥明細資料
				if (imMovementItemsPO != null && imMovementItemsPO.size() > 0) {
					Long headId = movementHeadPO.getHeadId();
					imMovementService.deleteImMovementItems(imMovementItemsPO);
					movementHeadPO = imMovementService.findById(headId);
					if (movementHeadPO == null) {
						throw new NoSuchObjectException("查無盤點單主鍵：" + headId + "的資料！");
					}
				}
				movementHeadPO.setImMovementItems(actualSaveMovementItems);
			}
			movementHeadPO.setLastUpdatedBy(employeeCode);
			movementHeadPO.setLastUpdateDate(new Date());

			return movementHeadPO;
		} else {
			movementHead.setStatus(OrderStatus.UNCONFIRMED);
			movementHead.setCreatedBy(employeeCode);
			movementHead.setCreationDate(new Date());
			movementHead.setLastUpdatedBy(employeeCode);
			movementHead.setLastUpdateDate(new Date());
			movementHead.setImMovementItems(actualSaveMovementItems);
			return movementHead;
		}
	}


	/**
	 * 設定調撥單的批號
	 * @param movementHead
	 * @param organizationCode
	 * @param defaultLotNo
	 * @param employeeCode
	 * @return
	 * @throws ValidationErrorException
	 * @throws NoSuchObjectException
	 */
	public List getMovementItemsWithLotNo(ImMovementHead movementHead, String organizationCode, String defaultLotNo
		) throws ValidationErrorException, NoSuchObjectException {
		List actualSaveMovementItems = new ArrayList();
		ImOnHandDAO imOnHandDAO = (ImOnHandDAO) context.getBean("imOnHandDAO");
		List imMovementItems = movementHead.getImMovementItems();
		for (int i = 0; i < imMovementItems.size(); i++) {
			ImMovementItem imMovementItem = (ImMovementItem) imMovementItems.get(i);
			String itemCode = imMovementItem.getItemCode();
			String warehouseCode = imMovementItem.getDeliveryWarehouseCode();
			Double deliveryQuantity = imMovementItem.getDeliveryQuantity();
			if (deliveryQuantity != null) {
				List<ImOnHand> lockedOnHands = null;
				try {
					lockedOnHands = imOnHandDAO.getLockedOnHand(organizationCode, itemCode, warehouseCode, null);
				} catch (CannotAcquireLockException cale) {
					throw new ValidationErrorException("品號：" + itemCode + ",庫別：" + warehouseCode + "已鎖定，請稍後再試！");
				}
				if (lockedOnHands != null && lockedOnHands.size() > 0) {
					TreeMap lotNoMap = getRequiredPropertyToMap(lockedOnHands);
					Double availableQuantityAmount = getCurrentStockOnHandQty(lotNoMap);
					if (availableQuantityAmount < deliveryQuantity) {
						throw new ValidationErrorException("品號：" + itemCode + ",庫別：" + warehouseCode + "可用庫存量不足！");
					} else {
						// 分配批號
						distributeLotNo(movementHead, actualSaveMovementItems, imMovementItem, lotNoMap, defaultLotNo);
					}
				} else {
					throw new NoSuchObjectException("查無品號：" + itemCode + ",庫別：" + warehouseCode + "的庫存資料！");
				}
			} else {
				throw new ValidationErrorException("品號：" + itemCode + ",庫別：" + warehouseCode + "的數量為空值！");
			}
		}
		return actualSaveMovementItems ;
	}

	/**
	 * 從ImOnHand資料中取得必要資訊，放置到TreeMap
	 *
	 * @param lockedOnHands
	 * @param onHandsMap
	 */
	private TreeMap getRequiredPropertyToMap(List<ImOnHand> lockedOnHands) {

		TreeMap lotNoMap = new TreeMap();
		for (ImOnHand onHand : lockedOnHands) {
			Double availableQuantity = onHand.getStockOnHandQty() - onHand.getOutUncommitQty() + onHand.getInUncommitQty()
					+ onHand.getMoveUncommitQty() + onHand.getOtherUncommitQty();

			lotNoMap.put(onHand.getId().getLotNo(), availableQuantity);
		}
		return lotNoMap;
	}

	private Double getCurrentStockOnHandQty(TreeMap lotNoMap) {

		Set set = lotNoMap.entrySet();
		Map.Entry[] lotNoEntry = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
		Double availableQuantityAmount = 0D;
		for (Map.Entry entry : lotNoEntry) {
			availableQuantityAmount += (Double) entry.getValue();
		}

		return availableQuantityAmount;
	}

	/**
	 * 分配批號
	 *
	 * @param movementHead
	 * @param actualSaveMovementItems
	 * @param imMovementItem
	 * @param lotNoMap
	 * @param defaultLotNo
	 * @param employeeCode
	 */
	private void distributeLotNo(ImMovementHead movementHead, List actualSaveMovementItems, ImMovementItem imMovementItem,
			TreeMap lotNoMap, String defaultLotNo) {

		String arrivalWarehouseCode = movementHead.getArrivalWarehouseCode();
		Set set = lotNoMap.entrySet();
		Map.Entry[] lotNoEntry = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
		Double deliveryQuantity = imMovementItem.getDeliveryQuantity();
		if (lotNoEntry.length == 1) {
			imMovementItem.setArrivalWarehouseCode(arrivalWarehouseCode);
			imMovementItem.setLotNo((String) lotNoEntry[0].getKey()); // 只有一組批號時，不管預設批號
			actualSaveMovementItems.add(imMovementItem);
		} else {
			// =====================UI輸入的預設批號非空值且存在時=======================
			if (StringUtils.hasText(defaultLotNo) && lotNoMap.get(defaultLotNo.trim()) != null) {
				if (deliveryQuantity == 0D) {
					imMovementItem.setArrivalWarehouseCode(arrivalWarehouseCode);
					imMovementItem.setLotNo(defaultLotNo.trim());
					actualSaveMovementItems.add(imMovementItem);
				} else {
					Double availableQuantity = (Double) lotNoMap.get(defaultLotNo.trim());
					if (availableQuantity > 0) {
						ImMovementItem newMovementItem = new ImMovementItem();
						BeanUtils.copyProperties(imMovementItem, newMovementItem);
						newMovementItem.setArrivalWarehouseCode(arrivalWarehouseCode);
						newMovementItem.setLotNo(defaultLotNo.trim());
						if (deliveryQuantity > availableQuantity) {
							newMovementItem.setDeliveryQuantity(availableQuantity);
							lotNoMap.put(defaultLotNo.trim(), 0D);
							deliveryQuantity -= availableQuantity;
						} else {
							newMovementItem.setDeliveryQuantity(deliveryQuantity);
							lotNoMap.put(defaultLotNo.trim(), availableQuantity - deliveryQuantity);
							deliveryQuantity = null;// 將調撥數量設為null藉以判斷是否已執行預設批號
						}
						actualSaveMovementItems.add(newMovementItem);
					}
				}
			}

			if (deliveryQuantity != null && deliveryQuantity >= 0) {
				for (Map.Entry entry : lotNoEntry) {
					String lotNo = (String) entry.getKey();
					Double availableQuantity = (Double) entry.getValue();
					if (availableQuantity > 0D && deliveryQuantity >= 0D) {
						ImMovementItem newMovementItem = new ImMovementItem();
						BeanUtils.copyProperties(imMovementItem, newMovementItem);
						newMovementItem.setArrivalWarehouseCode(arrivalWarehouseCode);
						newMovementItem.setLotNo(lotNo);
						if (deliveryQuantity > availableQuantity) {
							newMovementItem.setDeliveryQuantity(availableQuantity);
							entry.setValue(0D);
							deliveryQuantity -= availableQuantity;
							actualSaveMovementItems.add(newMovementItem);
						} else {
							newMovementItem.setDeliveryQuantity(deliveryQuantity);
							entry.setValue(availableQuantity - deliveryQuantity);
							actualSaveMovementItems.add(newMovementItem);
							break;
						}
					}
				}
			}
		}
	}

	private TreeMap getDeliveryQuantityToMap(List imMovementItems, String isOrigOrder) throws ValidationErrorException {

		StringBuffer key = new StringBuffer();
		TreeMap deliveryQuantityMap = new TreeMap();

		for (int i = 0; i < imMovementItems.size(); i++) {
			ImMovementItem imMovementItem = (ImMovementItem) imMovementItems.get(i);
			String itemCode = imMovementItem.getItemCode();
			String warehouseCode = imMovementItem.getDeliveryWarehouseCode();
			Double deliveryQuantity = null;
			if ("Y".equals(isOrigOrder)) {
				deliveryQuantity = imMovementItem.getOriginalDeliveryQuantity();
			} else {
				deliveryQuantity = imMovementItem.getDeliveryQuantity();
			}
			if (deliveryQuantity != null) {
				key.delete(0, key.length());
				key.append(itemCode + "#");
				key.append(warehouseCode);

				if (deliveryQuantityMap.get(key.toString()) == null) {
					deliveryQuantityMap.put(key.toString(), deliveryQuantity);
				} else {
					deliveryQuantityMap.put(key.toString(), deliveryQuantity + ((Double) deliveryQuantityMap.get(key.toString())));
				}
			} else {
				if ("Y".equals(isOrigOrder)) {
					throw new ValidationErrorException("原單據品號：" + itemCode + ",庫別：" + warehouseCode + "的原配貨數量為空值！");
				} else {
					throw new ValidationErrorException("品號：" + itemCode + ",庫別：" + warehouseCode + "的實際配貨數量為空值！");
				}
			}
		}

		return deliveryQuantityMap;
	}

	private void doValidate(TreeMap origDeliveryQuantityMap, TreeMap deliveryQuantityMap) throws ValidationErrorException {

		Set entrySet = origDeliveryQuantityMap.entrySet();
		Iterator it = entrySet.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			String[] keyArray = StringUtils.delimitedListToStringArray(key, "#");
			Double origDeliveryQuantity = (Double) entry.getValue();
			Double actualDeliveryQuantity = (Double) deliveryQuantityMap.get(key);
			if (actualDeliveryQuantity != null && actualDeliveryQuantity > origDeliveryQuantity) {
				throw new ValidationErrorException("品號：" + keyArray[0] + ",庫別：" + keyArray[1] + "的實際配貨數量：" + actualDeliveryQuantity
						+ "大於原配貨數量：" + origDeliveryQuantity + "！");
			}
		}
	}

	private void distributeDeliveryQuantity(List imMovementItems, TreeMap deliveryQuantityMap) {

		StringBuffer key = new StringBuffer();
		for (int i = 0; i < imMovementItems.size(); i++) {
			ImMovementItem imMovementItem = (ImMovementItem) imMovementItems.get(i);
			String itemCode = imMovementItem.getItemCode();
			String warehouseCode = imMovementItem.getDeliveryWarehouseCode();
			Double origDeliveryQuantity = imMovementItem.getOriginalDeliveryQuantity();
			key.delete(0, key.length());
			key.append(itemCode + "#");
			key.append(warehouseCode);
			Double deliveryQuantity = (Double) deliveryQuantityMap.get(key.toString());

			if (deliveryQuantity != null) {
				if (deliveryQuantity > origDeliveryQuantity) {
					imMovementItem.setDeliveryQuantity(origDeliveryQuantity);
					deliveryQuantityMap.put(key.toString(), deliveryQuantity - origDeliveryQuantity);
				} else {
					imMovementItem.setDeliveryQuantity(deliveryQuantity);
					deliveryQuantityMap.put(key.toString(), 0D);
				}
			}
		}
	}
}
