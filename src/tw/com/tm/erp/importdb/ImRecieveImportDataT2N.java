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
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.dao.BuLocationDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.ImReceiveHeadMainService;
import tw.com.tm.erp.hbm.service.ImReceiveHeadService;
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

public class ImRecieveImportDataT2N implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(ImRecieveImportDataT2N.class);
	public static final String split[] = { "{#}" };

	// private static final String ORGANIZATION_CODE = "TM";

	private static ApplicationContext context = SpringUtils.getApplicationContext();

	public ImportInfo initial(HashMap uiProperties) {
		log.info("ImRecieveImportDataT2 initial");
		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImReceiveHead.class.getName());
		imInfo.setSplit(split);

		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("H");
		imInfo.setSaveKeyField(true);

		// set field name
		imInfo.addFieldName("h");
		imInfo.addFieldName("orderTypeCode");
		imInfo.addFieldName("orderNo");
		imInfo.addFieldName("lastUpdateDate");
		imInfo.addFieldName("lastUpdatedBy");

		
		// set field type
		imInfo.setFieldType("h", "java.lang.String");
		imInfo.addFieldName("orderTypeCode");
		imInfo.addFieldName("orderNo");
		imInfo.setFieldType("lastUpdateDate", "java.util.Date");
		imInfo.setFieldType("lastUpdatedBy", "java.lang.String");

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyyMMdd");

		// set default value
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
		HashMap defaultValue = new HashMap();
		defaultValue.put("brandCode", "T2");
		defaultValue.put("reserve3", uiProperties.get(ImportDBService.UPLOAD_FILE_NAME));
		defaultValue.put("fileName", uiProperties.get(ImportDBService.UPLOAD_FILE_NAME));
		imInfo.setDefaultValue(defaultValue);

		// add detail
		imInfo.addDetailImportInfos(getImReceiveItems());
		return imInfo;
	}

	/**
	 * im item price detail config
	 * 
	 * @return
	 */
	private ImportInfo getImReceiveItems() {
		log.info("getImReceiveItems");
		ImportInfo imInfo = new ImportInfo();

		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("D");
		imInfo.setSaveKeyField(true);
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImReceiveItem.class.getName());

		imInfo.addFieldName("d");
		imInfo.addFieldName("itemCode");
		imInfo.addFieldName("quantity");
		imInfo.addFieldName("a");
		imInfo.addFieldName("lotNo");
		
		imInfo.setFieldType("d", "java.lang.String");
		imInfo.setFieldType("itemCode", "java.lang.String");
		imInfo.setFieldType("quantity", "java.lang.Double");
		imInfo.setFieldType("a", "java.lang.String");
		imInfo.setFieldType("lotNo", "java.lang.String");

		return imInfo;
	}

	public String updateDB(List entityBeans, ImportInfo info) throws Exception {
		log.info("updateDB");
		StringBuffer reMsg = new StringBuffer();
		try {
			List<ImReceiveHead> imReceiveHeads = new ArrayList(0);
			List assembly = new ArrayList(0);
			ImReceiveHeadMainService service = (ImReceiveHeadMainService) context.getBean("imReceiveHeadMainService");
			for (int index = 0; index < entityBeans.size(); index++) {				
			        ImReceiveHead entityBean = (ImReceiveHead) entityBeans.get(index);
			        imReceiveHeads.add(entityBean);
			}
			service.executeBatchImportT2(imReceiveHeads, assembly);
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
}
