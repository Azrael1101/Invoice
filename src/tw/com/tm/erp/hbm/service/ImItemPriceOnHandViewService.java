package tw.com.tm.erp.hbm.service;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.ImItemPriceOnHandView;
import tw.com.tm.erp.hbm.dao.ImItemPriceOnHandViewDAO;

public class ImItemPriceOnHandViewService {

    private static final Log log = LogFactory
	    .getLog(ImItemPriceOnHandViewService.class);

    private ImItemPriceOnHandViewDAO imItemPriceOnHandViewDAO;

    public void setImItemPriceOnHandViewDAO(
	    ImItemPriceOnHandViewDAO imItemPriceOnHandViewDAO) {
	this.imItemPriceOnHandViewDAO = imItemPriceOnHandViewDAO;
    }

    /**
     * 依據品牌代號、品號、價格類別等條件查詢
     * 
     * @param conditionMap
     * @return ImItemPriceOnHandView
     * @throws Exception
     */
    public ImItemPriceOnHandView getItemByCondition(HashMap conditionMap)
	    throws Exception {
	try {
	    String itemCode = (String) conditionMap.get("itemCode");
	    conditionMap.put("itemCode", itemCode.trim().toUpperCase());
	    return imItemPriceOnHandViewDAO.getItemByCondition(conditionMap);
	} catch (Exception ex) {
	    log.error("自商品價格庫存檔查詢商品資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("自商品價格庫存檔查詢商品資料時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 依據品牌代號、庫別代號、價格類別等條件查詢
     * 
     * @param conditionMap
     * @return ImItemPriceOnHandView
     * @throws Exception
     */
    public ImItemPriceOnHandView getWarehouseByCondition(HashMap conditionMap)
	    throws Exception {
	try {
	    String warehouseCode = (String) conditionMap.get("warehouseCode");
	    conditionMap.put("warehouseCode", warehouseCode.trim()
		    .toUpperCase());
	    return imItemPriceOnHandViewDAO
		    .getWarehouseByCondition(conditionMap);
	} catch (Exception ex) {
	    log.error("自商品價格庫存檔查詢庫別資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("自商品價格庫存檔查詢庫別資料時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 依據品牌代號、品號、庫別代號、價格類別等條件查詢，回傳品號價格、庫存量等
     * 
     * @param conditionMap
     * @return Object[]
     * @throws Exception
     */
    public Object[] getItemPriceOnHand(HashMap conditionMap) throws Exception {
	try {
	    String itemCode = (String) conditionMap.get("itemCode");
	    String warehouseCode = (String) conditionMap.get("warehouseCode");
	    conditionMap.put("itemCode", itemCode.trim().toUpperCase());
	    conditionMap.put("warehouseCode", warehouseCode.trim()
		    .toUpperCase());
	    return imItemPriceOnHandViewDAO.getItemPriceOnHand(conditionMap);
	} catch (Exception ex) {
	    log.error("自商品價格庫存檔查詢商品價格、庫存量時發生錯誤，原因：" + ex.toString());
	    throw new Exception("自商品價格庫存檔查詢商品價格、庫存量時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 依據商品價格及庫存查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List findItemPriceOnHandList(HashMap conditionMap) throws Exception {

	try {
	    String itemCode_Start = (String) conditionMap.get("itemCode_Start");
	    String itemCode_End = (String) conditionMap.get("itemCode_End");
	    String itemName = (String) conditionMap.get("itemName");
	    String warehouseCode_Start = (String) conditionMap.get("warehouseCode_Start");
	    String warehouseCode_End = (String) conditionMap.get("warehouseCode_End");
	    String lotNo = (String) conditionMap.get("lotNo");
	    conditionMap.put("itemCode_Start", itemCode_Start.trim().toUpperCase());
	    conditionMap.put("itemCode_End", itemCode_End.trim().toUpperCase());
	    conditionMap.put("itemName", itemName.trim());
	    conditionMap.put("warehouseCode_Start", warehouseCode_Start.trim().toUpperCase());
	    conditionMap.put("warehouseCode_End", warehouseCode_End.trim().toUpperCase());
	    conditionMap.put("lotNo", lotNo.trim());

	    return imItemPriceOnHandViewDAO.findItemPriceOnHandList(conditionMap);
	} catch (Exception ex) {
	    log.error("查詢商品價格庫存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢商品價格庫存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 依據品牌代號、價格類別、warehouseEmployee等條件查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List getWarehouseForWarehouseEmployee(HashMap conditionMap)
	    throws Exception {
	
	try {	  
	    return imItemPriceOnHandViewDAO.getWarehouseForWarehouseEmployee(conditionMap);
	} catch (Exception ex) {
	    log.error("自商品價格庫存檔查詢庫別資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("自商品價格庫存檔查詢庫別資料時發生錯誤，原因：" + ex.getMessage());
	}
    }
    /**
     * 依據商品價格及庫存查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List findItemPriceOnHandWithoutEmployee(HashMap conditionMap) throws Exception {

	try {
	    String itemCode_Start = (String) conditionMap.get("itemCode_Start");
	    String itemCode_End = (String) conditionMap.get("itemCode_End");
	    String itemName = (String) conditionMap.get("itemName");
	    String warehouseCode_Start = (String) conditionMap.get("warehouseCode_Start");
	    String warehouseCode_End = (String) conditionMap.get("warehouseCode_End");
	    String lotNo = (String) conditionMap.get("lotNo");
	    conditionMap.put("itemCode_Start", itemCode_Start.trim().toUpperCase());
	    conditionMap.put("itemCode_End", itemCode_End.trim().toUpperCase());
	    conditionMap.put("itemName", itemName.trim());
	    conditionMap.put("warehouseCode_Start", warehouseCode_Start.trim().toUpperCase());
	    conditionMap.put("warehouseCode_End", warehouseCode_End.trim().toUpperCase());
	    conditionMap.put("lotNo", lotNo.trim());

	    return imItemPriceOnHandViewDAO.findItemPriceOnHandWithoutEmployee(conditionMap);
	} catch (Exception ex) {
	    log.error("查詢商品價格庫存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢商品價格庫存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
}
