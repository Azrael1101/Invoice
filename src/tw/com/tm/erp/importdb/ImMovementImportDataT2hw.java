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
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.service.ImItemService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.User;
//Steve
/**
 * 調撥單匯入
 *
 * fix 20081210 line id 不匯入
 *
 * @author T02049
 *
 */

public class ImMovementImportDataT2hw implements ImportDataAbs {
    private static final Log log = LogFactory.getLog(ImMovementImportDataT2.class);
    public static final String split[] = { "{#}" };

    // private static final String ORGANIZATION_CODE = "TM";

    private static ApplicationContext context = SpringUtils.getApplicationContext();

    public ImportInfo initial(HashMap uiProperties) {
	//log.info("ImMovementImportDataT2 initial");
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
	imInfo.addFieldName("a");
	imInfo.addFieldName("b");
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
	imInfo.setFieldType("a", "java.lang.String");
	imInfo.setFieldType("b", "java.lang.String");
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
	imInfo.addFieldName("itemCode");
	imInfo.addFieldName("deliveryQuantity");
	imInfo.addFieldName("boxNo");
	imInfo.addFieldName("lotNo");

	imInfo.setFieldType("d", "java.lang.String");
	imInfo.setFieldType("itemCode", "java.lang.String");
	imInfo.setFieldType("deliveryQuantity", "java.lang.Double");
	imInfo.setFieldType("boxNo", "java.lang.String");
	imInfo.setFieldType("lotNo", "java.lang.String");

	return imInfo;
    }

    public String updateDB(List entityBeans, ImportInfo info) throws Exception {
	StringBuffer reMsg = new StringBuffer();
	StringBuffer ordNo = new StringBuffer();
	//20160426 by jason 建單工號
	HashMap uiProperties = info.getUiProperties();
	User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
	String employeeCode = user.getEmployeeCode();
	//
	
	try {

	    List<ImMovementHead> imMovementHeads = new ArrayList(0);
	    ImMovementService imMovementService = (ImMovementService) context.getBean("imMovementService");
	    ImItemService imItemService = (ImItemService) context.getBean("imItemService");
	    ImItemEancodeDAO imItemEancodeDAO = (ImItemEancodeDAO) context.getBean("imItemEancodeDAO");
	    ImWarehouseDAO imWarehouseDAO = (ImWarehouseDAO)context.getBean("imWarehouseDAO");//by jason
	    ImOnHandDAO imOnHandDAO = (ImOnHandDAO)context.getBean("imOnHandDAO");//by jason
	    ImItemDAO imItemDAO = (ImItemDAO)context.getBean("imItemDAO");//by jason
	    for (int index = 0; index < entityBeans.size(); index++) {

		ImMovementHead entityBean = (ImMovementHead) entityBeans.get(index);
		String orderNo = entityBean.getOrderNo();
		ordNo.append("-").append(orderNo);

		 //---庫別檢核
        String brandCode 		 = entityBean.getBrandCode();
        String deliveryWarehouse = entityBean.getDeliveryWarehouseCode();
        String arrivalWarehouse  = entityBean.getArrivalWarehouseCode();         
        if(imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode,deliveryWarehouse,"Y")== null||
        		imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode,arrivalWarehouse,"Y")== null){
        	 log.info("錯誤的庫別或庫別未啟用");
        	throw new Exception("錯誤的庫別或庫別未啟用");
        }
       if(deliveryWarehouse.equals(arrivalWarehouse)){
    	   log.info("轉出與轉入庫別相同");
       	throw new Exception("轉出與轉入庫別相同");
       }
       
        
    	//---
		
		List<ImMovementItem> imMovementItems = entityBean.getImMovementItems();

		//把ITEM的WAREHOUSECODE設定 LOTNO空值則設定預設值
		for (Iterator iterator = imMovementItems.iterator(); iterator.hasNext();) {
		    ImMovementItem imMovementItem = (ImMovementItem) iterator.next();

		    imMovementItem.setOriginalDeliveryQuantity(0D);
		    imMovementItem.setArrivalWarehouseCode(entityBean.getArrivalWarehouseCode());
		    imMovementItem.setDeliveryWarehouseCode(entityBean.getDeliveryWarehouseCode());
			imMovementItem.setCreatedBy(employeeCode);//20160426 by jason 建單工號
			imMovementItem.setLastUpdatedBy(employeeCode);//20160426 by jason 建單工號
		    imMovementItem.setCreationDate(new Date());
		    imMovementItem.setLastUpdateDate(new Date());
		    imMovementItem.setScanCode(imMovementItem.getItemCode());
		    if(null == imMovementItem.getLotNo())
			imMovementItem.setLotNo(SystemConfig.LOT_NO);
		    String itemcode = imItemEancodeDAO.getOneItemCodeByProperty(entityBean.getBrandCode(), imMovementItem.getItemCode());
		    //如果是國際碼，把商品品號匯入
		    if(StringUtils.hasText(itemcode)){
			imMovementItem.setItemCode(itemcode);
		    } 
		    
			//Double Qty = imOnHandDAO.getCurrentStockOnHandQty1("TM",brandCode,imMovementItem.getItemCode(),deliveryWarehouse);				
			
			ImItem imItem = imItemDAO.findImItem(brandCode,imMovementItem.getItemCode(),"Y");
			if(imItem == null)
				throw new Exception("品號: "+imMovementItem.getItemCode()+"不存在或未啟用");
			/*
			if(imItem.getAllowMinusStock().equals("N")){
			if(Qty == null || Qty <= 0.0D )
				throw new Exception("品號: "+imMovementItem.getItemCode()+"庫存為0");
		    
			}*/
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

	    List<ImMovementHead> newMovementHeads = imMovementService.executeBatchImportT2hw(imMovementHeads);

	    String orderTypeCode = "";
	    if(imMovementHeads.size() != newMovementHeads.size()){	
		orderTypeCode = imMovementHeads.get(0).getOrderTypeCode();
		reMsg.append("調撥單覆核成功-點貨覆核");  

	    }else{
		reMsg.append("調撥單匯入成功");
	    }
	    reMsg.append(";").append(orderTypeCode).append(";").append(ordNo);
	    System.out.println("回傳值:"+reMsg);
	} catch (Exception ex) {
	    log.info(ex.getMessage());
	    throw ex;
	}
	log.info("reMsg.toString() = " + reMsg.toString());
	return reMsg.toString();
    }
}