package tw.com.tm.erp.importdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.ImStorageItem;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.service.ImStorageService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.User;

public class ImStorageImportDataT2 implements ImportDataAbs {
    private static final Log log = LogFactory.getLog(ImStorageImportDataT2.class);
    public static final String split[] = { "{#}" };

    private static ApplicationContext context = SpringUtils.getApplicationContext();

    public ImportInfo initial(HashMap uiProperties) {
	ImportInfo imInfo = new ImportInfo();

	// set entity class name
	imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImStorageHead.class.getName());
	imInfo.setSplit(split);

	// set key info
	imInfo.setKeyIndex(0);
	imInfo.setKeyValue("H");
	imInfo.setSaveKeyField(true);

	// set field name
	imInfo.addFieldName("h");
	imInfo.addFieldName("createdBy");
	imInfo.addFieldName("storageTransactionDate");
	imInfo.addFieldName("arrivalWarehouseCode");

	// set field type
	imInfo.setFieldType("h", "java.lang.String");
	imInfo.setFieldType("createdBy", "java.lang.String");
	imInfo.setFieldType("storageTransactionDate", "java.util.Date");
	imInfo.setFieldType("arrivalWarehouseCode", "java.lang.String");

	// set date format
	imInfo.setFieldTypeFormat("java.util.Date", "yyyyMMdd");

	// set default value
	User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
	HashMap defaultValue = new HashMap();
	defaultValue.put("brandCode", user.getBrandCode());
	defaultValue.put("lastUpdatedBy", user.getEmployeeCode());
	defaultValue.put("lastUpdateDate", new Date());
	defaultValue.put("reserve2", uiProperties.get(ImportDBService.BATCH_NO));
	defaultValue.put("reserve3", uiProperties.get(ImportDBService.UPLOAD_FILE_NAME));
	imInfo.setDefaultValue(defaultValue);

	// add detail
	imInfo.addDetailImportInfos(getImStorageItems());
	return imInfo;
    }

    private ImportInfo getImStorageItems() {
	log.info("getImStorageItems");
	ImportInfo imInfo = new ImportInfo();

	imInfo.setKeyIndex(0);
	imInfo.setKeyValue("D");
	imInfo.setSaveKeyField(true);
	imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImStorageItem.class.getName());

	imInfo.addFieldName("d");
	imInfo.addFieldName("remark");
	imInfo.addFieldName("itemCode");
	imInfo.addFieldName("storageInNo");
	imInfo.addFieldName("storageLotNo");
	imInfo.addFieldName("storageQuantity");
	imInfo.addFieldName("arrivalStorageCode");

	imInfo.setFieldType("d", "java.lang.String");
	imInfo.setFieldType("remark", "java.lang.String");
	imInfo.setFieldType("itemCode", "java.lang.String");
	imInfo.setFieldType("storageInNo", "java.lang.String");
	imInfo.setFieldType("storageLotNo", "java.lang.String");
	imInfo.setFieldType("storageQuantity", "java.lang.Double");
	imInfo.setFieldType("arrivalStorageCode", "java.lang.String");

	return imInfo;
    }

    public String updateDB(List entityBeans, ImportInfo info) throws Exception {
	StringBuffer reMsg = new StringBuffer();

	try {

	    List<ImStorageHead> imStorageHeads = new ArrayList(0);
	    ImStorageService imStorageService = (ImStorageService) context.getBean("imStorageService");

	    ImItemEancodeDAO imItemEancodeDAO = (ImItemEancodeDAO) context.getBean("imItemEancodeDAO");

	    for (int index = 0; index < entityBeans.size(); index++) {
		ImStorageHead entityBean = (ImStorageHead) entityBeans.get(index);
		List<ImStorageItem> imStorageItems = entityBean.getImStorageItems();

		//String deliveryWarehouseCode = entityBean.getDeliveryWarehouseCode();
		String arrivalWarehouseCode = entityBean.getArrivalWarehouseCode();

		//if(!StringUtils.hasText(deliveryWarehouseCode) || !StringUtils.hasText(arrivalWarehouseCode)){
		//throw new Exception("請輸入轉出/轉入庫別");
		//}

		//if(!(deliveryWarehouseCode.equals(arrivalWarehouseCode))){
		//throw new Exception("轉出/轉入庫別需相同");
		//}

		//把ITEM的WAREHOUSECODE設定 LOTNO空值則設定預設值
		for (Iterator iterator = imStorageItems.iterator(); iterator.hasNext();) {
		    ImStorageItem imStorageItem = (ImStorageItem) iterator.next();

		    imStorageItem.setIsDeleteRecord(AjaxUtils.IS_DELETE_RECORD_FALSE);

		    //imStorageItem.setDeliveryWarehouseCode(deliveryWarehouseCode);
		    imStorageItem.setArrivalWarehouseCode(arrivalWarehouseCode);

		    //設為預設儲位
		    //if(null ==  imStorageItem.getStorageInNo())
		    //imStorageItem.setStorageInNo(ImStorageService.STORAGE_IN_NO_DEFAULT);
		    //if(null ==  imStorageItem.getStorageLotNo())
		    //imStorageItem.setStorageLotNo(ImStorageService.STORAGE_LOT_NO_DEFAULT);
		    //if(null ==  imStorageItem.getDeliveryStorageCode())
		    //imStorageItem.setDeliveryStorageCode(ImStorageService.STORAGE_CODE_DEFAULT);
		    //if(null ==  imStorageItem.getArrivalStorageCode())
		    //imStorageItem.setArrivalStorageCode(ImStorageService.STORAGE_CODE_DEFAULT);

		    String itemcode = imItemEancodeDAO.getOneItemCodeByProperty(entityBean.getBrandCode(), imStorageItem.getItemCode());
		    //如果是國際碼，把商品品號匯入
		    if(StringUtils.hasText(itemcode)){
			imStorageItem.setItemCode(itemcode);
		    }

		    if("0".equals(imStorageItem.getRemark()))
			imStorageItem.setStorageQuantity(imStorageItem.getStorageQuantity() * -1 );
		}

		imStorageHeads.add(entityBean);
	    }

	    List<ImStorageHead> newImStorageHeads = imStorageService.executeBatchImportT2(imStorageHeads);

	    //執行Process
	    for (Iterator iterator = newImStorageHeads.iterator(); iterator.hasNext();) {
		ImStorageHead imStorageHead = (ImStorageHead) iterator.next();
		Object processObj[] = ImStorageService.startProcess(imStorageHead);
		ImStorageHead head = imStorageService.findById(imStorageHead.getStorageHeadId());
		head.setProcessId((Long)processObj[0]);
		imStorageService.update(head);
	    }

	    reMsg.append("匯入成功;");
	} catch (Exception ex) {
	    log.info(ex.getMessage());
	    throw ex;
	}
	log.info("reMsg.toString() = " + reMsg.toString());
	return reMsg.toString();
    }
}