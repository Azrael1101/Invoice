package tw.com.tm.erp.importdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.service.BuOrderTypeService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.hbm.service.SoSalesOrderMainService;
import tw.com.tm.erp.utils.User;

/**
 * 出貨盤點單匯入
 * 
 * fix 20081210 line id 不匯入
 * 
 * @author T02049
 * 
 */

public class SoSalesOrderImportDataT2 implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(SoSalesOrderImportDataT2.class);

	private static ApplicationContext context = SpringUtils.getApplicationContext();

	public static final String split[] = { "{#}" };

	public ImportInfo initial(HashMap uiProperties) {
		log.info("initial");
		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.SoSalesOrderHead.class.getName());
		imInfo.setSplit(split);

		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("H");
		imInfo.setSaveKeyField(true);

		// set field name
		imInfo.addFieldName("h");
		imInfo.addFieldName("customerCode");
		imInfo.addFieldName("salesOrderDate");
		imInfo.addFieldName("shopCode");
		imInfo.addFieldName("scheduleShipDate");
		imInfo.addFieldName("invoiceTypeCode");
		imInfo.addFieldName("defaultWarehouseCode");
		imInfo.addFieldName("discountRate");
		imInfo.addFieldName("itemCategory");
		imInfo.addFieldName("exportCommissionRate");

		// set field type
		imInfo.setFieldType("h", "java.lang.String");
		imInfo.setFieldType("customerCode", "java.lang.String");
		imInfo.setFieldType("salesOrderDate", "java.util.Date");
		imInfo.setFieldType("shopCode", "java.lang.String");
		imInfo.setFieldType("scheduleShipDate", "java.util.Date");
		imInfo.setFieldType("invoiceTypeCode", "java.lang.String");
		imInfo.setFieldType("defaultWarehouseCode", "java.lang.String");
		imInfo.setFieldType("discountRate", "java.lang.Double");
		imInfo.setFieldType("itemCategory", "java.lang.String");
		imInfo.setFieldType("exportCommissionRate", "java.lang.Double");

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyyMMdd");
		
		// set default value
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
		HashMap defaultValue = new HashMap();
		defaultValue.put("brandCode", uiProperties.get("BRAND_CODE"));
		defaultValue.put("orderTypeCode", "SOE");
		defaultValue.put("status", "SAVE");
		defaultValue.put("taxType", "3");
		defaultValue.put("taxRate", "5");
		defaultValue.put("exportExchangeRate", "1");
		defaultValue.put("sufficientQuantityDelivery", "Y");
		defaultValue.put("verificationStatus", "N");
		defaultValue.put("superintendentCode", user.getEmployeeCode());
		defaultValue.put("createdBy", user.getEmployeeCode());
		defaultValue.put("creationDate", uiProperties.get(ImportDBService.UPLOAD_DATE));
		defaultValue.put("lastUpdatedBy", user.getEmployeeCode());
		defaultValue.put("lastImportDate", uiProperties.get(ImportDBService.UPLOAD_DATE));
		defaultValue.put("lastUpdateDate", uiProperties.get(ImportDBService.UPLOAD_DATE));
		defaultValue.put("reserve1", uiProperties.get(ImportDBService.BATCH_NO));
		defaultValue.put("reserve2", uiProperties.get(ImportDBService.UPLOAD_FILE_NAME));
		imInfo.setDefaultValue(defaultValue);

		// add detail
		imInfo.addDetailImportInfos(getSoSalesOrderItems());

		return imInfo;
	}

	/**
	 * im item price detail config
	 * 
	 * @return
	 */

	private ImportInfo getSoSalesOrderItems() {
		log.info("getSoSalesOrderItems");
		ImportInfo imInfo2 = new ImportInfo();
		imInfo2.setKeyIndex(0);
		imInfo2.setKeyValue("D");
		imInfo2.setSaveKeyField(true);
		imInfo2.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.SoSalesOrderItem.class.getName());

		imInfo2.addFieldName("d");
		imInfo2.addFieldName("itemCode");
		imInfo2.addFieldName("quantity");

		imInfo2.setFieldType("d", "java.lang.String");
		imInfo2.setFieldType("itemCode", "java.lang.String");
		imInfo2.setFieldType("quantity", "java.lang.Double");

		return imInfo2;
	}

	public String updateDB(List entityBeans, ImportInfo info) throws Exception {

		log.info("SoSalesOrderImportData.updateDB");
		StringBuffer reMsg = new StringBuffer();
		List<SoSalesOrderHead> salesOrderHeads = new ArrayList();
		List<SoSalesOrderHead> heads = null;
		
		try {
			BuOrderTypeService buOrderTypeService = (BuOrderTypeService) context.getBean("buOrderTypeService");
			SoSalesOrderMainService soSalesOrderMainService = (SoSalesOrderMainService) context.getBean("soSalesOrderMainService");

			//存單據
			for (int index = 0; index < entityBeans.size(); index++) {
				SoSalesOrderHead externalOrderHead = (SoSalesOrderHead) entityBeans.get(index);
				
				BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(externalOrderHead.getBrandCode(), externalOrderHead.getOrderTypeCode()));
				
				if (null == buOrderType)
					throw new Exception("取得 " + externalOrderHead.getBrandCode() + " " + externalOrderHead.getOrderTypeCode() + " 的單別失敗！");
								
				salesOrderHeads.add(externalOrderHead);
				String sNo = soSalesOrderMainService.saveBatchHead(externalOrderHead);
				
				System.out.println("sNo:"+sNo);
				heads = soSalesOrderMainService.updateBatchLine(externalOrderHead,sNo);
			}
			
			//啟流程
			for (Iterator iterator = heads.iterator(); iterator.hasNext();) {
				SoSalesOrderHead soSalesOrderHead = (SoSalesOrderHead) iterator.next();
				SoSalesOrderMainService.startProcess(soSalesOrderHead);
			}
			
			reMsg.append("銷售資料匯入成功！");
		} catch (Exception ex) {
			log.error("銷售資料匯入失敗！原因：" + ex.toString());
			reMsg.append("銷售資料匯入失敗！原因：" + ex.getMessage());
		}
		return reMsg.toString();
	}
}