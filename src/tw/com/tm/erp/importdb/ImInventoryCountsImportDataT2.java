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
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsHead;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.dao.BuLocationDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.service.BuEmployeeWithAddressViewService;
import tw.com.tm.erp.hbm.service.ImInventoryCountsService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.User;

/**
 * 盤點單匯入
 * 
 * fix 20081210 line id 不匯入
 * 
 * @author T02049
 * 
 */

public class ImInventoryCountsImportDataT2 implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(ImInventoryCountsImportDataT2.class);
	public static final String split[] = { "{#}" };

	// private static final String ORGANIZATION_CODE = "TM";

	private static ApplicationContext context = SpringUtils.getApplicationContext();

	public ImportInfo initial(HashMap uiProperties) {
		log.info("ImInventoryCountsImportDataT2 initial");
		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImInventoryCountsHead.class.getName());
		imInfo.setSplit(split);

		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("H");
		imInfo.setSaveKeyField(true);

		// set field name
		imInfo.addFieldName("h");
		imInfo.addFieldName("countsLotNo");
		imInfo.addFieldName("actualSuperintendentCode");
		imInfo.addFieldName("actualCountsDate");

		// set field type
		imInfo.setFieldType("h", "java.lang.String");
		imInfo.setFieldType("countsLotNo", "java.lang.String");
		imInfo.setFieldType("actualSuperintendentCode", "java.lang.String");
		imInfo.setFieldType("actualCountsDate", "java.util.Date");

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyyMMdd");

		// set default value
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
		HashMap defaultValue = new HashMap();
		defaultValue.put("countsId", uiProperties.get(ImportDBService.COUNTS_ID));
		defaultValue.put("brandCode", uiProperties.get("BRAND_CODE"));
		defaultValue.put("status", "COUNTING");
		defaultValue.put("lastUpdatedBy", user.getEmployeeCode());
		defaultValue.put("lastImportDate", uiProperties.get(ImportDBService.UPLOAD_DATE));
		defaultValue.put("lastUpdateDate", uiProperties.get(ImportDBService.UPLOAD_DATE));
		defaultValue.put("reserve2", uiProperties.get(ImportDBService.BATCH_NO));
		defaultValue.put("reserve3", uiProperties.get(ImportDBService.UPLOAD_FILE_NAME));
		defaultValue.put("fileName", uiProperties.get(ImportDBService.UPLOAD_FILE_NAME));
		imInfo.setDefaultValue(defaultValue);

		// add detail
		imInfo.addDetailImportInfos(getImInventoryCountsLines());
		return imInfo;
	}

	/**
	 * im item price detail config
	 * 
	 * @return
	 */
	private ImportInfo getImInventoryCountsLines() {
		log.info("getImInventoryCountsLines");
		ImportInfo imInfo = new ImportInfo();

		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("D");
		imInfo.setSaveKeyField(true);
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImInventoryCountsLine.class.getName());

		imInfo.addFieldName("d");
		imInfo.addFieldName("eanCode");
		imInfo.addFieldName("countsQty");
		
		imInfo.setFieldType("d", "java.lang.String");
		imInfo.setFieldType("eanCode", "java.lang.String");
		imInfo.setFieldType("countsQty", "java.lang.Double");

		return imInfo;
	}

	public String updateDB(List entityBeans, ImportInfo info) throws Exception {
		log.info("updateDB");
		StringBuffer reMsg = new StringBuffer();
		reMsg.append("盤點單匯入成功<br>");
		try {
			List<ImInventoryCountsHead> imInventoryCountsHeads = new ArrayList(0);
			List assembly = new ArrayList(0);
			ImInventoryCountsService imInventoryCountsService = (ImInventoryCountsService) context.getBean("imInventoryCountsService");
			BuEmployeeWithAddressViewService buEmployeeWithAddressViewService = (BuEmployeeWithAddressViewService) context.getBean("buEmployeeWithAddressViewService");
			
			for (int index = 0; index < entityBeans.size(); index++) {				
				ImInventoryCountsHead entityBean = (ImInventoryCountsHead) entityBeans.get(index);
				BuEmployeeWithAddressView buEmployeeWithAddressView = buEmployeeWithAddressViewService.findbyBrandCodeAndEmployeeCode("T2", entityBean.getActualSuperintendentCode());
				if(null == buEmployeeWithAddressView){
					throw new Exception("匯入盤點單失敗，原因： 盤點批號(" + entityBean.getCountsLotNo() + ")輸入的盤點人工號(" + entityBean.getActualSuperintendentCode() + ")錯誤！");
				}
			        imInventoryCountsHeads.add(entityBean);
			}
			List<ImInventoryCountsHead> heads = imInventoryCountsService.executeBatchImportT2(imInventoryCountsHeads, assembly);
			reMsg.append("<table border='1'>");
			reMsg.append("<tr><td>盤點批號</td><td>盤點日期</td><td>實際盤點日期</td><td>匯入日期</td><td>盤點次數</td></tr>");
			for (Iterator iterator = heads.iterator(); iterator.hasNext();) {
				ImInventoryCountsHead head = (ImInventoryCountsHead) iterator.next();
				reMsg.append("<tr><td>"+head.getCountsLotNo() + 
						"</td><td>"+DateUtils.format(head.getCountsDate(),DateUtils.C_DATE_PATTON_SLASH)+
						"</td><td>"+DateUtils.format(head.getActualCountsDate(),DateUtils.C_DATE_PATTON_SLASH)+
						"</td><td>"+DateUtils.format(head.getLastImportDate(),DateUtils.C_DATE_PATTON_SLASH)+
						"</td><td>"+head.getImportedTimes()+"</td></tr>");
			}
			reMsg.append("</table><br>");
		} catch (Exception ex) {
			reMsg = new StringBuffer(ex.getMessage());
			throw ex;
		}
		log.info("reMsg.toString() = " + reMsg.toString());
		return reMsg.toString();
	}
}
