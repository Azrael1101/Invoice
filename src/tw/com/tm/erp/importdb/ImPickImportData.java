package tw.com.tm.erp.importdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImPickHead;
import tw.com.tm.erp.hbm.bean.ImPickItem;
import tw.com.tm.erp.hbm.bean.ImPickTemp;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.ImPickService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.ValidateUtil;

/**
 * 挑貨單
 * 
 * @author T02049
 * 
 */

public class ImPickImportData implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(ImPickImportData.class);

	private static ApplicationContext context = SpringUtils.getApplicationContext();
	
	private static String warehouseCode[] = null;

	public ImportInfo initial(HashMap uiProperties) {
		log.info("ImPickImportData.initial");

		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);	

		Date date = DateUtils.getShortDate(new Date());
		
		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImPickTemp.class.getName());

		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue(ImportDBService.DEFAULT_KEY_HEAD + "0");

		imInfo.addFieldName("key");
		// set field type
		imInfo.setFieldType("key", "java.lang.String");

		warehouseCode = new String [] {"22900","23090","30080","31250","32430"};

		imInfo.setXlsColumnLength(1 + warehouseCode.length);
		doInitial(imInfo);
		imInfo.setOneRecordDetailKeys(new int[] { 1 + warehouseCode.length });

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyy/MM/dd");

		// set default value
		HashMap defaultValue = new HashMap();
		defaultValue.put("brandCode", user.getBrandCode() );
		defaultValue.put("date", date);
		defaultValue.put("employeeCode", user.getEmployeeCode() );
		imInfo.setDefaultValue(defaultValue);

		imInfo.setImportDataStartRecord(1);
		return imInfo;
	}

	private ImportInfo doInitial(ImportInfo imInfo){

		imInfo.addFieldName("itemCode");
		for(int i =0;i<warehouseCode.length;i++)
			imInfo.addFieldName("temp" + i);

		// set field type
		imInfo.setFieldType("itemCode", "java.lang.String");
		for(int i =0;i<warehouseCode.length;i++)
			imInfo.setFieldType("temp" + i, "java.lang.Double");

		return imInfo;
		
	}

	public String updateDB(List entityBeans, ImportInfo info) throws Exception {
		log.info("ImItemImportData.updateDB");

		ImPickService imPickService = (ImPickService) context.getBean("imPickService");
		
		HashMap defaultValue = info.getDefaultValue();
		String brandCode = (String)defaultValue.get("brandCode");
		String employeeCode = (String)defaultValue.get("employeeCode");
		Date date = (Date)defaultValue.get("date");
		
		StringBuffer result = new StringBuffer("<br/>");

		List<ImPickHead> imPickHeads = new ArrayList<ImPickHead>(0);
		
		//依照庫別的數量 建立挑貨單
		for (int i = 0; i < warehouseCode.length; i++) {
			ImPickHead imPickHead = new ImPickHead();
			
			//imPickHead.setWarehouseCode(warehouseCode[i]);
			imPickHead.setBrandCode(brandCode);
			imPickHead.setOrderTypeCode("IPN");
			imPickHead.setPickDate(date);
			imPickHead.setCreatedBy(employeeCode);
			imPickHead.setCreationDate(date);
			imPickHead.setLastUpdatedBy(employeeCode);
			imPickHead.setLastUpdateDate(date);
			
			//建立挑貨單明細
			List<ImPickItem> imPickItems = new ArrayList<ImPickItem>(0);
			
			for (int index = 0; index < entityBeans.size(); index++) {
				ImPickTemp imPickTemp = (ImPickTemp) entityBeans.get(index);
				Double blockQuantity = NumberUtils.getDouble(BeanUtils.getNestedProperty(imPickTemp, "temp" + i));
				
				if(blockQuantity > 0){
					ImPickItem imPickItem = new ImPickItem();
					imPickItem.setItemCode(imPickTemp.getItemCode());
					imPickItem.setBlockQuantity(blockQuantity);
					imPickItems.add(imPickItem);
				}
			}
			imPickHead.setImPickItems(imPickItems);
			imPickHeads.add(imPickHead);
		}
		
		imPickService.executeBatchImport(imPickHeads);
		
		//執行Process
	    for (Iterator iterator = imPickHeads.iterator(); iterator.hasNext();) {
	    	ImPickHead imPickHead = (ImPickHead) iterator.next();
	    	ImPickService.startProcess(imPickHead);
		}
	    
		result.append("匯入成功");
		return result.toString();
	}    
}