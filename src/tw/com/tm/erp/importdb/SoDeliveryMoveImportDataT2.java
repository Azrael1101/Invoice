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
import tw.com.tm.erp.hbm.bean.SoDeliveryMoveHead;
import tw.com.tm.erp.hbm.service.BuEmployeeWithAddressViewService;
import tw.com.tm.erp.hbm.service.SoDeliveryMoveService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.User;

/**
 * 入提儲位匯入
 * 
 * fix 20081210 line id 不匯入
 * 
 * @author T02049
 * 
 */

public class SoDeliveryMoveImportDataT2 implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(SoDeliveryMoveImportDataT2.class);
	public static final String split[] = { "{#}" };
	
	// private static final String ORGANIZATION_CODE = "TM";
	
	private static ApplicationContext context = SpringUtils.getApplicationContext();
	
	public ImportInfo initial(HashMap uiProperties) {
		log.info("SoDeliveryMoveImportDataT2 initial");
		ImportInfo imInfo = new ImportInfo();
		
		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.SoDeliveryMoveHead.class.getName());
		imInfo.setSplit(split);
		
		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("H");
		imInfo.setSaveKeyField(true);
		
		// set field name
		imInfo.addFieldName("h");
		imInfo.addFieldName("reserve1");
		imInfo.addFieldName("moveEmployee");
		imInfo.addFieldName("orderDate");
		
		// set field type
		imInfo.setFieldType("h", "java.lang.String");
		imInfo.setFieldType("reserve1", "java.lang.String");
		imInfo.setFieldType("moveEmployee", "java.lang.String");
		imInfo.setFieldType("orderDate", "java.util.Date");
		
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
		imInfo.setDefaultValue(defaultValue);
		
		// add detail
		imInfo.addDetailImportInfos(getSoDeliveryInventoryLines());
		return imInfo;
	}
	
	/**
	 * im item price detail config
	 * 
	 * @return
	 */
	private ImportInfo getSoDeliveryInventoryLines() {
		log.info("getSoDeliveryInventoryLines");
		ImportInfo imInfo = new ImportInfo();
		
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("D");
		imInfo.setSaveKeyField(true);
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.SoDeliveryMoveLine.class.getName());

		imInfo.addFieldName("d");
		imInfo.addFieldName("arrivalStoreCode");
		imInfo.addFieldName("deliveryOrderNo");
		imInfo.addFieldName("reserve1");
		
		imInfo.setFieldType("d", "java.lang.String");
		imInfo.setFieldType("arrivalStoreCode", "java.lang.String");
		imInfo.setFieldType("deliveryOrderNo", "java.lang.String");
		imInfo.setFieldType("reserve1", "java.lang.Long");
		
		return imInfo;
	}
	
	public String updateDB(List entityBeans, ImportInfo info) throws Exception {
		log.info("updateDB");
		StringBuffer reMsg = new StringBuffer();
		try {
			List<SoDeliveryMoveHead> soDeliveryInventoryHeads = new ArrayList(0);

			SoDeliveryMoveService soDeliveryMoveService = (SoDeliveryMoveService) context.getBean("soDeliveryMoveService");
			BuEmployeeWithAddressViewService buEmployeeWithAddressViewService = (BuEmployeeWithAddressViewService) context.getBean("buEmployeeWithAddressViewService");
			
			for (int index = 0; index < entityBeans.size(); index++) {
				
				SoDeliveryMoveHead entityBean = (SoDeliveryMoveHead) entityBeans.get(index);
				
				BuEmployeeWithAddressView buEmployeeWithAddressView = buEmployeeWithAddressViewService.findbyBrandCodeAndEmployeeCode("T2", entityBean.getMoveEmployee());
				if(null == buEmployeeWithAddressView){
					throw new Exception("匯入移轉單失敗，原因： 代號(" + entityBean.getReserve1() + ")輸入的盤點人工號(" + entityBean.getMoveEmployee() + ")錯誤！");
				}
				soDeliveryInventoryHeads.add(entityBean);
			}
			List<SoDeliveryMoveHead> heads = soDeliveryMoveService.executeBatchImportT2(soDeliveryInventoryHeads);
			
			//加流程
		    for (Iterator iterator = heads.iterator(); iterator.hasNext();) {
		    	SoDeliveryMoveHead soDeliveryMoveHead = (SoDeliveryMoveHead) iterator.next();
		    	soDeliveryMoveService.startProcess(soDeliveryMoveHead);
			}
			
			System.out.println("=============中斷點！=============");
			
			reMsg.append("匯入成功！");
			
		} catch (Exception ex) {
			reMsg = new StringBuffer(ex.getMessage());
			throw ex;
		}
		log.info("reMsg.toString() = " + reMsg.toString());
		return reMsg.toString();
	}
}
