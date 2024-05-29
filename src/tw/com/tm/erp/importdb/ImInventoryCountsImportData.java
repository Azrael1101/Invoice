package tw.com.tm.erp.importdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsHead;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsLine;
import tw.com.tm.erp.hbm.bean.TmpAppStockStatistics;
import tw.com.tm.erp.hbm.service.ImInventoryCountsService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.sp.TmpAppStockStatisticsService;

/**
 * Im Item Import Data Sample code
 * 
 * FIX : 只修改明細 FIX : 盤點單 ITEM 不需要流水號
 * 
 * @author T02049
 * 
 */

public class ImInventoryCountsImportData implements ImportDataAbs {
    private static final Log log = LogFactory
	    .getLog(ImInventoryCountsImportData.class);
    public static final String split[] = { "{#}" };

    public ImportInfo initial(HashMap uiProperties) {
	log.info("initial");
	ImportInfo imInfo = new ImportInfo();

	// set entity class name
	imInfo
		.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImInventoryCountsHead.class
			.getName());
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

	// set default value
	HashMap defaultValue = new HashMap();
	defaultValue.put("creationDate", new Date());
	defaultValue.put("lastUpdateDate", new Date());
	defaultValue.put("createdBy", "");
	defaultValue.put("lastUpdatedBy", "");
	defaultValue.put("fileName", uiProperties
		.get(ImportDBService.UPLOAD_FILE_NAME));

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
	ImportInfo imInfo2 = new ImportInfo();
	imInfo2.setKeyIndex(0);
	imInfo2.setKeyValue("D");
	imInfo2.setSaveKeyField(true);
	imInfo2
		.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImInventoryCountsLine.class
			.getName());

	imInfo2.addFieldName("d");
	// imInfo2.addFieldName("lineId");
	imInfo2.addFieldName("itemCode");
	imInfo2.addFieldName("onHandQty");
	imInfo2.addFieldName("countsQty");

	imInfo2.setFieldType("d", "java.lang.String");
	// imInfo2.setFieldType("lineId", "java.lang.Long" );
	imInfo2.setFieldType("itemCode", "java.lang.String");
	imInfo2.setFieldType("onHandQty", "java.lang.Double");
	imInfo2.setFieldType("countsQty", "java.lang.Double");

	return imInfo2;
    }

    public String updateDB(List entityBeans, ImportInfo info) throws Exception {
	
	log.info("updateDB");
	StringBuffer reMsg = new StringBuffer();
	try {
	    HashMap uiProperties = info.getUiProperties();
	    User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
	    String employeeCode = user.getEmployeeCode();
	    String brandCode = null;
	    String orderTypeCode = null;
	    String orderNo = null;
	    List assembly = new ArrayList(0);
	
	    ImInventoryCountsService imInventoryCountsService = (ImInventoryCountsService) SpringUtils.getApplicationContext().getBean("imInventoryCountsService");	
	    for (int index = 0; index < entityBeans.size(); index++) {
		try{
	            ImInventoryCountsHead entityBean = (ImInventoryCountsHead) entityBeans.get(index);           		
		    brandCode = entityBean.getBrandCode();
		    orderTypeCode = entityBean.getOrderTypeCode();
		    orderNo = entityBean.getOrderNo().trim();
		    ImInventoryCountsHead inventoryCountsHeadPO = imInventoryCountsService.findInventoryCountsByIdentification(brandCode, orderTypeCode, orderNo);
		    if(inventoryCountsHeadPO != null){
			if(OrderStatus.COUNTING.equals(inventoryCountsHeadPO.getStatus())){
			    inventoryCountsHeadPO = setDate(inventoryCountsHeadPO, entityBean, employeeCode, imInventoryCountsService);
		            imInventoryCountsService.updateImInventoryCounts(inventoryCountsHeadPO);
			}else{
			    assembly.add("品牌：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "的單據狀態為" + OrderStatus.getChineseWord(inventoryCountsHeadPO.getStatus()) + "，無法執行匯入！<br>"); 
			}
		    }else{
		        assembly.add("查無品牌：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "的盤點資料！<br>");
		    }
		}catch (Exception ex) {
		    log.error("品牌：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "的盤點資料匯入失敗！原因：" + ex.toString());
		    assembly.add("品牌：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "的盤點資料匯入失敗！原因：" + ex.getMessage() + "<br>");
		}
	    }
	    
	    if(assembly.size() == 0){
	        reMsg.append("盤點檔匯入成功！");
	    }else{
		reMsg.append("部分盤點資料匯入失敗！訊息如下：<br>");
		for(int k = 0; k < assembly.size(); k++){
		    reMsg.append(assembly.get(k));
		}
	    }
	}catch (Exception ex) {
	    log.error("盤點檔匯入失敗！原因：" + ex.toString());
	    reMsg.append("盤點檔匯入失敗！原因：" + ex.getMessage());
	}
	
	return reMsg.toString();
    }
    
    private ImInventoryCountsHead setDate(ImInventoryCountsHead inventoryCountsHeadPO, ImInventoryCountsHead inventoryCountsHead, String employeeCode, ImInventoryCountsService imInventoryCountsService) throws Exception{
	 
	 String status = inventoryCountsHeadPO.getStatus();
	 String countsType = inventoryCountsHeadPO.getCountsType();
	 List tmpAppStockStatisticsBeans = null;
	 List imInventoryCountsLinesPO = inventoryCountsHeadPO.getImInventoryCountsLines();
	 List imInventoryCountsLines = inventoryCountsHead.getImInventoryCountsLines();	  
	 //非帶量盤點call stored procedure 取庫存量
	 if(!"1".equals(countsType)){
	     HashMap map = new HashMap();
	     map.put("brandCode", inventoryCountsHeadPO.getBrandCode());
	     map.put("warehouseCode", inventoryCountsHeadPO.getWarehouseCode());
	     map.put("onHandDate", DateUtils.format(inventoryCountsHeadPO.getInventoryDate(), DateUtils.C_DATA_PATTON_YYYYMMDD));
	     TmpAppStockStatisticsService tmpAppStockStatisticsService = (TmpAppStockStatisticsService) SpringUtils.getApplicationContext().getBean("tmpAppStockStatisticsService");	
	     tmpAppStockStatisticsBeans = tmpAppStockStatisticsService.getStockStatistics(map);
	     //先刪除原盤點明細資料
	     Long headId = inventoryCountsHeadPO.getHeadId();
	     if(imInventoryCountsLinesPO != null && imInventoryCountsLinesPO.size() > 0){
	        imInventoryCountsService.deleteImInventoryCountsLines(imInventoryCountsLinesPO);
	     }	     
	     inventoryCountsHeadPO = imInventoryCountsService.findImInventoryCountsHeadById(headId);
	     if(inventoryCountsHeadPO == null){
		 throw new NoSuchObjectException("查無盤點單主鍵：" + headId + "的資料！");
	     }else{
		 imInventoryCountsLinesPO = inventoryCountsHeadPO.getImInventoryCountsLines();
	     }
	 }	 
	 //set line data
	 if(imInventoryCountsLines != null && imInventoryCountsLines.size() > 0){
             for(int i = 0; i < imInventoryCountsLines.size(); i++){
                 ImInventoryCountsLine imInventoryCountsLine = (ImInventoryCountsLine)imInventoryCountsLines.get(i);
                 String itemCode = imInventoryCountsLine.getItemCode();
                 Double countsQty = imInventoryCountsLine.getCountsQty();
                 String isExist = "N";
                 if("1".equals(countsType)){
                     for(int j = 0; j < imInventoryCountsLinesPO.size(); j++){
                         ImInventoryCountsLine imInventoryCountsLinePO = (ImInventoryCountsLine)imInventoryCountsLinesPO.get(j);
                         if(imInventoryCountsLinePO.getItemCode().equals(itemCode)){
                             imInventoryCountsLinePO.setCountsQty(countsQty);
                             imInventoryCountsLinePO.setLastUpdatedBy(employeeCode);
                             imInventoryCountsLinePO.setLastUpdateDate(new Date());
                             isExist = "Y";
                             break;
                         }
                     }
                 }
                 
                 if("N".equals(isExist)){
                     ImInventoryCountsLine newLine = new ImInventoryCountsLine();
            	     newLine.setItemCode(itemCode);
            	     if(tmpAppStockStatisticsBeans != null){
        		 for(int m = 0; m < tmpAppStockStatisticsBeans.size(); m++){
        		     TmpAppStockStatistics stockStatistic = (TmpAppStockStatistics)tmpAppStockStatisticsBeans.get(m);
        		     if(stockStatistic.getItemCode().equals(itemCode)){
        			newLine.setOnHandQty(stockStatistic.getEndOnHandQty());
        			break;
        		     }
        		 } 
                     }
            	     newLine.setCountsQty(countsQty);
            	     newLine.setStatus(status);
            	     newLine.setCreatedBy(employeeCode);
            	     newLine.setCreationDate(new Date());
            	     newLine.setLastUpdatedBy(employeeCode);
            	     newLine.setLastUpdateDate(new Date());        	     
            	     imInventoryCountsLinesPO.add(newLine);
                 }
	     }
	 }
	 //set head data
	 inventoryCountsHeadPO.setIsImportedFile("Y");
	 Long importedTimes = inventoryCountsHeadPO.getImportedTimes();
	 if(importedTimes == null){
	     inventoryCountsHeadPO.setImportedTimes(0L);
	 }else{
	     inventoryCountsHeadPO.setImportedTimes(importedTimes + 1L);
	 }
	 inventoryCountsHeadPO.setLastImportDate(new Date());
	 inventoryCountsHeadPO.setLastUpdatedBy(employeeCode);
	 inventoryCountsHeadPO.setLastUpdateDate(new Date());
	 
	 return inventoryCountsHeadPO;
    }
}