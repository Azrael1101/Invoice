package tw.com.tm.erp.importdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.service.SoSalesOrderService;
import tw.com.tm.erp.utils.User;

/**
 * 出貨盤點單匯入
 * 
 * fix 20081210 line id 不匯入
 * 
 * @author T02049
 * 
 */

public class SoSalesOrderImportData implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(SoSalesOrderImportData.class);

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
		imInfo.addFieldName("brandCode");
		imInfo.addFieldName("orderTypeCode");
		imInfo.addFieldName("orderNo");

		// set field type
		imInfo.setFieldType("h", "java.lang.String");
		imInfo.setFieldType("brandCode", "java.lang.String");
		imInfo.setFieldType("orderTypeCode", "java.lang.String");
		imInfo.setFieldType("orderNo", "java.lang.String");

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyyMMdd");

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
		// imInfo2.addFieldName("lineId");
		imInfo2.addFieldName("warehouseCode");
		imInfo2.addFieldName("itemCode");
		imInfo2.addFieldName("quantity");

		imInfo2.setFieldType("d", "java.lang.String");
		// imInfo2.setFieldType("lineId", "java.lang.Long");
		imInfo2.setFieldType("warehouseCode", "java.lang.String");
		imInfo2.setFieldType("itemCode", "java.lang.String");
		imInfo2.setFieldType("quantity", "java.lang.Double");

		return imInfo2;
	}

	public String updateDB(List entityBeans, ImportInfo info) throws Exception {

		log.info("SoSalesOrderImportData.updateDB");
		StringBuffer reMsg = new StringBuffer();
		List<SoSalesOrderHead> salesOrderHeads = new ArrayList();
		try {
			HashMap uiProperties = info.getUiProperties();
			User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
			for (int index = 0; index < entityBeans.size(); index++) {
				SoSalesOrderHead externalOrderHead = (SoSalesOrderHead) entityBeans.get(index);
				String brandCode = externalOrderHead.getBrandCode().trim();
				String orderTypeCode = externalOrderHead.getOrderTypeCode().trim();
				String orderNo = externalOrderHead.getOrderNo().trim();

				SoSalesOrderService salesOrderService = (SoSalesOrderService) context.getBean("soSalesOrderService");
				SoSalesOrderHead orderHeadPO = salesOrderService.findSalesOrderByIdentification(brandCode, orderTypeCode, orderNo);
				if (orderHeadPO == null) {
					throw new NoSuchDataException("查無品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "的銷售資料！");
				}
				if (!OrderStatus.SAVE.equals(orderHeadPO.getStatus())) {
					throw new ValidationErrorException("品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "的狀態不為暫存，無法執行匯入！");
				}
				setData(externalOrderHead, orderHeadPO, user.getEmployeeCode());
				salesOrderHeads.add(orderHeadPO);
				salesOrderService.updateSalesOrderForImportTask(salesOrderHeads);
				reMsg.append("銷售資料匯入成功！");
			}
		} catch (Exception ex) {
			log.error("銷售資料匯入失敗！原因：" + ex.toString());
			reMsg.append("銷售資料匯入失敗！原因：" + ex.getMessage());
		}
		return reMsg.toString();
	}

	private void setData(SoSalesOrderHead externalOrderHead, SoSalesOrderHead orderHeadPO, String opUser) {

		orderHeadPO.setLastUpdatedBy(opUser);
		orderHeadPO.setLastUpdateDate(new Date());

		List<SoSalesOrderItem> orderItems = orderHeadPO.getSoSalesOrderItems();
		List<SoSalesOrderItem> externalOrderItems = externalOrderHead.getSoSalesOrderItems();
		for (SoSalesOrderItem externalOrderItem : externalOrderItems) {
			externalOrderItem.setLineId(null);
			externalOrderItem.setSoSalesOrderHead(orderHeadPO);
			externalOrderItem.setTaxType(orderHeadPO.getTaxType());
			externalOrderItem.setTaxRate(orderHeadPO.getTaxRate());
			externalOrderItem.setStatus(orderHeadPO.getStatus());
			externalOrderItem.setCreatedBy(opUser);
			externalOrderItem.setCreationDate(new Date());
			externalOrderItem.setLastUpdatedBy(opUser);
			externalOrderItem.setLastUpdateDate(new Date());
			orderItems.add(externalOrderItem);
		}
	}
}
