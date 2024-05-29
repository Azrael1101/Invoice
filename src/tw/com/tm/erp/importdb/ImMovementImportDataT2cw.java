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
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemEanPriceView;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.dao.BuLocationDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.service.ImItemEanPriceViewService;
import tw.com.tm.erp.hbm.service.ImItemService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.User;

/**
 * 調撥單（菸酒）匯入
 *
 * fix 20081210 line id 不匯入
 *
 * @author T02049
 *
 */

public class ImMovementImportDataT2cw implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(ImMovementImportDataT2cw.class);
	public static final String split[] = { "{#}" };

	// private static final String ORGANIZATION_CODE = "TM";

	private static ApplicationContext context = SpringUtils.getApplicationContext();

	public ImportInfo initial(HashMap uiProperties) {
		log.info("<<<< ImMovementImportDataT2cw initial >>>>");
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
		imInfo.addFieldName("orderTypeCode");
		imInfo.addFieldName("orderNo");
		imInfo.addFieldName("deliveryDate");
		imInfo.addFieldName("packedBy");
		imInfo.addFieldName("deliveryWarehouseCode");
		imInfo.addFieldName("arrivalWarehouseCode");
		imInfo.addFieldName("originalOrderTypeCode");
		imInfo.addFieldName("originalOrderNo");
		imInfo.addFieldName("arrivalStoreCode");

		// set field type
		imInfo.setFieldType("h", "java.lang.String");
		imInfo.setFieldType("orderTypeCode", "java.lang.String");
		imInfo.setFieldType("orderNo", "java.lang.String");
		imInfo.setFieldType("deliveryDate", "java.util.Date");
		imInfo.setFieldType("packedBy", "java.lang.String");
		imInfo.setFieldType("deliveryWarehouseCode", "java.lang.String");
		imInfo.setFieldType("arrivalWarehouseCode", "java.lang.String");
		imInfo.setFieldType("originalOrderTypeCode", "java.lang.String");
		imInfo.setFieldType("originalOrderNo", "java.lang.String");
		imInfo.setFieldType("arrivalStoreCode", "java.lang.String");

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyyMMdd");

		// set default value
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
		HashMap defaultValue = new HashMap();
		defaultValue.put("brandCode", user.getBrandCode());
		defaultValue.put("lastUpdateDate", new Date());
		defaultValue.put("lastUpdatedBy", user.getEmployeeCode());
		defaultValue.put("reserve2", uiProperties.get(ImportDBService.BATCH_NO));
		defaultValue.put("reserve3", uiProperties.get(ImportDBService.UPLOAD_FILE_NAME));
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
		//log.info("getImMovementItems");
		ImportInfo imInfo = new ImportInfo();

		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("D");
		imInfo.setSaveKeyField(true);
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImMovementItem.class.getName());

		imInfo.addFieldName("d");
		imInfo.addFieldName("itemCode"); //品號
		imInfo.addFieldName("amt"); // 裝箱量（單據不顯示）
		imInfo.addFieldName("lotNo"); // 批號
		imInfo.addFieldName("qty"); // 箱數（單據不顯示）
		imInfo.addFieldName("boxNo"); // 箱號
		imInfo.addFieldName("deliveryQuantity"); // 出貨數量

		imInfo.setFieldType("d", "java.lang.String");
		imInfo.setFieldType("itemCode", "java.lang.String");
		imInfo.setFieldType("amt", "java.lang.Double");
		imInfo.setFieldType("lotNo", "java.lang.String");
		imInfo.setFieldType("qty", "java.lang.Double");
		imInfo.setFieldType("boxNo", "java.lang.String");
		imInfo.setFieldType("deliveryQuantity", "java.lang.Double");

		return imInfo;
	}

	public String updateDB(List entityBeans, ImportInfo info) throws Exception {
		StringBuffer reMsg = new StringBuffer();
		try {
			//調撥單原進貨單單號
			String originalOrderTypeCode = null;
			String originalOrderNo = null;

		    List<ImMovementHead> imMovementHeads = new ArrayList(0);
		    ImMovementService imMovementService = (ImMovementService) context.getBean("imMovementService");
		    ImItemService imItemService = (ImItemService) context.getBean("imItemService");
		    ImItemEancodeDAO imItemEancodeDAO = (ImItemEancodeDAO) context.getBean("imItemEancodeDAO");
		    for (int index = 0; index < entityBeans.size(); index++) {
		    	ImMovementHead entityBean = (ImMovementHead) entityBeans.get(index);
		    	originalOrderTypeCode = entityBean.getOriginalOrderTypeCode();
		    	originalOrderNo = entityBean.getOriginalOrderNo();
		    	List<ImMovementItem> imMovementItems = entityBean.getImMovementItems();
		    	//把ITEM的WAREHOUSECODE設定 LOTNO空值則設定預設值
		    	for (Iterator iterator = imMovementItems.iterator(); iterator.hasNext();) {
					ImMovementItem imMovementItem = (ImMovementItem) iterator.next();
					imMovementItem.setOriginalDeliveryQuantity(0D);
					imMovementItem.setArrivalWarehouseCode(entityBean.getArrivalWarehouseCode());
					imMovementItem.setDeliveryWarehouseCode(entityBean.getDeliveryWarehouseCode());
					if(null == imMovementItem.getLotNo())
						imMovementItem.setLotNo(SystemConfig.LOT_NO);
					String itemcode = imItemEancodeDAO.getOneItemCodeByProperty(entityBean.getBrandCode(), imMovementItem.getItemCode());
					//如果是國際碼，把商品品號匯入
					if(StringUtils.hasText(itemcode)){
						imMovementItem.setItemCode(itemcode);
					}
				}
		    	//第一筆資料業種
		    	if(null != imMovementItems && imMovementItems.size() > 1){
		    		ImMovementItem imMovementItem = imMovementItems.get(0);
		    		ImItem item = imItemService.findItem("T2", imMovementItem.getItemCode());
		    		if(null != item)
		    			entityBean.setItemCategory(item.getItemCategory());
		    	}
		    	imMovementHeads.add(entityBean);
		    }

		    List<ImMovementHead> newMovementHeads = imMovementService.executeBatchImportT2(imMovementHeads);

		    //執行Process
		    for (Iterator iterator = newMovementHeads.iterator(); iterator.hasNext();) {
				ImMovementHead imMovementHead = (ImMovementHead) iterator.next();
    	    	ImMovementService.startProcess(imMovementHead);
			}

			if(imMovementHeads.size() != newMovementHeads.size()){
				reMsg.append("調撥單覆核成功");
			}else{
				reMsg.append("調撥單匯入成功");
			}
			reMsg.append(";").append(originalOrderTypeCode).append(";").append(originalOrderNo);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
		log.info("reMsg.toString() = " + reMsg.toString());
		return reMsg.toString();
	}
}
