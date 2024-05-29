package tw.com.tm.erp.utils.sp;

import javax.sql.DataSource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import oracle.jdbc.OracleTypes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;

public class AppGetSaleItemInfoService {

    private static final Log log = LogFactory
	    .getLog(AppGetSaleItemInfoService.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    public SoSalesOrderItem getSaleItemInfo(HashMap conditionMap, SoSalesOrderItem origiSalesOrderItem)
	    throws ValidationErrorException {

	Connection conn = null;
	CallableStatement calStmt = null;
	ResultSet rs = null;
	try {
            //================取得傳遞進來的參數====================
	    String brandCode = (String) conditionMap.get("brandCode");
	    String itemCode = (String) conditionMap.get("itemCode");
	    if(StringUtils.hasText(itemCode)){
	        itemCode = itemCode.trim().toUpperCase();
	    }
	    String salesDate = (String) conditionMap.get("salesDate");
	    String shopCode = (String) conditionMap.get("shopCode");
	    String warehouseCode = (String) conditionMap.get("warehouseCode");
	    if(StringUtils.hasText(warehouseCode)){
                warehouseCode = warehouseCode.trim().toUpperCase();
	    }
	    String vipPromotionCode = (String) conditionMap.get("vipPromotionCode");
	    String promotionCode = (String) conditionMap.get("promotionCode");
	    if(StringUtils.hasText(promotionCode)){
	        promotionCode = promotionCode.trim().toUpperCase();
	        conditionMap.put("promotionCode", promotionCode);
	    }
	    String customerTypeCode = (String) conditionMap.get("customerType");
	    String vipTypeCode = (String) conditionMap.get("vipType");
	    String priceType = (String) conditionMap.get("priceType");
	    Double quantity = (Double) conditionMap.get("quantity");
	    if(quantity == null){
	        quantity = 0D;
	        conditionMap.put("quantity", quantity);
	    }
	    Double originalUnitPrice = (Double) conditionMap.get("originalUnitPrice");
	    if(originalUnitPrice == null){
	        originalUnitPrice = 0D;
	        conditionMap.put("originalUnitPrice", originalUnitPrice);
	    }
	    String taxType = (String) conditionMap.get("taxType");
	    Double taxRate = (Double) conditionMap.get("taxRate");
	    if(taxRate == null){
	        if(taxType != null && ("1".equals(taxType) || "2".equals(taxType))){
		    taxRate = 0D;
		    conditionMap.put("taxRate", taxRate);
	        }else{
		    taxType = "3";
		    taxRate = 5D;
		    conditionMap.put("taxType", taxType);
		    conditionMap.put("taxRate", taxRate);
	        }
	    }
	    Double discountRate = (Double) conditionMap.get("discountRate");
	    if(discountRate == null){
	        discountRate = 100D;
	        conditionMap.put("discountRate", discountRate);
	    }
	    Double deductionAmount = (Double) conditionMap.get("deductionAmount");
	    if(deductionAmount == null){
	        deductionAmount = 0D;
	        conditionMap.put("deductionAmount", deductionAmount);
	    }
	    String warehouseManager = (String) conditionMap.get("warehouseManager");
	    String warehouseEmployee = (String) conditionMap.get("warehouseEmployee");
	
	    conn = dataSource.getConnection();
	    calStmt = conn.prepareCall("{call ERP.APP_GET_SALE_ITEM_INFO.GETDATA(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
	    calStmt.registerOutParameter(1, OracleTypes.CURSOR);
	    calStmt.setString(2, brandCode);
	    calStmt.setString(3, itemCode);
	    calStmt.setString(4, salesDate);
	    calStmt.setString(5, shopCode);
	    calStmt.setString(6, warehouseCode);
	    calStmt.setString(7, vipPromotionCode);
	    calStmt.setString(8, promotionCode);
	    calStmt.setString(9, customerTypeCode);
	    calStmt.setString(10, vipTypeCode);
	    calStmt.setString(11, priceType);
	    calStmt.setDouble(12, quantity);
	    calStmt.setDouble(13, originalUnitPrice);
	    calStmt.setString(14, taxType);
	    calStmt.setDouble(15, taxRate);
	    calStmt.setDouble(16, discountRate);
	    calStmt.setDouble(17, deductionAmount);
	    calStmt.setString(18, warehouseManager);
	    calStmt.setString(19, warehouseEmployee);	   
	    calStmt.executeQuery();
	    rs = (ResultSet) calStmt.getObject(1);
	    SoSalesOrderItem salesOrderItem = produceSoSalesOrderItemBean(rs, conditionMap);    
	  
	    return getReturnItemInfo(origiSalesOrderItem, salesOrderItem);
	} catch (Exception ex) {
	    log.error("查詢銷售明細資料發生錯誤，原因：" + ex.toString());
	    throw new ValidationErrorException(ex.toString());
	} finally {
	    if (rs != null) {
		try {
		    rs.close();
		} catch (SQLException e) {
		    log.error("關閉ResultSet時發生錯誤！");
		}
	    }
	    if (calStmt != null) {
		try {
		    calStmt.close();
		} catch (SQLException e) {
		    log.error("關閉CallableStatement時發生錯誤！");
		}
	    }
	    if (conn != null) {
		try {
		    conn.close();
		} catch (SQLException e) {
		    log.error("關閉Connection時發生錯誤！");
		}
	    }
	}
    }

    /**
     * 將查詢結果放進bean
     * 
     * @param rs
     * @return List
     * @throws Exception
     */
    private SoSalesOrderItem produceSoSalesOrderItemBean(ResultSet rs, HashMap conditionMap)
	    throws Exception {

	String taxType = (String) conditionMap.get("taxType");
	Double taxRate = (Double) conditionMap.get("taxRate");
	String vipPromotionCode = (String) conditionMap.get("vipPromotionCode");
	String promotionCode = (String) conditionMap.get("promotionCode");
	Double discountRate = (Double) conditionMap.get("discountRate");
	Double deductionAmount = (Double) conditionMap.get("deductionAmount");
	SoSalesOrderItem salesOrderItem  = null;
	if (rs != null) {
	    while (rs.next()) {
		salesOrderItem = new SoSalesOrderItem();
		String brandCode = rs.getString(1);
		String itemCode = rs.getString(2);
		String itemName = rs.getString(3);
		String warehouseCode = rs.getString(4);
		String warehouseName = rs.getString(5);
		Double currentOnHandQty = rs.getDouble(6);
		Double originalUnitPrice = rs.getDouble(7);
		Double actualUnitPrice = rs.getDouble(8);
		Double quantity = rs.getDouble(9);
		Double originalSalesAmount = rs.getDouble(10);
		Double actualSalesAmount = rs.getDouble(11);
		Double taxAmount = rs.getDouble(12);
		String vipPromotionName = rs.getString(13);
		Double vipDiscount = rs.getDouble(14);
		String vipDiscountType = rs.getString(15);
		String foundVipPromotion = rs.getString(16);
		String promotionName = rs.getString(17);		
		Double discount = rs.getDouble(18);
		String discountType = rs.getString(19);
		String foundPromotion = rs.getString(20);	
		String isServiceItem = rs.getString(21);
		String isComposeItem = rs.getString(22);
		String isTax = rs.getString(23);
		Double importCost = rs.getDouble(24);
		String importCurrencyCode = rs.getString(25);
		
		salesOrderItem.setItemCode(itemCode);
		salesOrderItem.setItemCName(itemName);
		salesOrderItem.setWarehouseCode(warehouseCode);
		salesOrderItem.setWarehouseName(warehouseName);
		salesOrderItem.setCurrentOnHandQty(currentOnHandQty);
		salesOrderItem.setOriginalUnitPrice(originalUnitPrice);
		salesOrderItem.setActualUnitPrice(actualUnitPrice);
		salesOrderItem.setQuantity(quantity);
		salesOrderItem.setOriginalSalesAmount(originalSalesAmount);
		salesOrderItem.setActualSalesAmount(actualSalesAmount);
		salesOrderItem.setTaxAmount(taxAmount);
		salesOrderItem.setVipPromotionCode(vipPromotionCode);
		salesOrderItem.setVipPromotionName(vipPromotionName);
		salesOrderItem.setVipDiscount(vipDiscount);
		salesOrderItem.setVipDiscountType(vipDiscountType);
		salesOrderItem.setFoundVipPromotion(foundVipPromotion);
		salesOrderItem.setPromotionCode(promotionCode);
		salesOrderItem.setPromotionName(promotionName);
		salesOrderItem.setDiscount(discount);
		salesOrderItem.setDiscountType(discountType);
		salesOrderItem.setFoundPromotion(foundPromotion);
		salesOrderItem.setIsServiceItem(isServiceItem);
		salesOrderItem.setIsComposeItem(isComposeItem);
		salesOrderItem.setIsTax(isTax);
		salesOrderItem.setTaxType(taxType);
		salesOrderItem.setTaxRate(taxRate);
		salesOrderItem.setDiscountRate(discountRate);
		salesOrderItem.setDeductionAmount(deductionAmount);
		salesOrderItem.setImportCost(importCost);
		salesOrderItem.setImportCurrencyCode(importCurrencyCode);
		/*System.out.println("brandCode = " + brandCode);
		System.out.println("itemCode = " + itemCode);
		System.out.println("itemName = " + itemName);
		System.out.println("warehouseCode = " + warehouseCode);
		System.out.println("warehouseName = " + warehouseName);
		System.out.println("currentOnHandQty = " + currentOnHandQty);
		System.out.println("originalUnitPrice = " + originalUnitPrice);
		System.out.println("actualUnitPrice = " + actualUnitPrice);
		System.out.println("quantity = " + quantity);
		System.out.println("originalSalesAmt = " + originalSalesAmount);
		System.out.println("actualSalesAmt = " + actualSalesAmount);
		System.out.println("taxAmt = " + taxAmount);
		System.out.println("vipPromotionName = " + vipPromotionName);
		System.out.println("vipDiscount = " + vipDiscount);
		System.out.println("vipDiscountType = " + vipDiscountType);
		System.out.println("foundVipPromotion = " + foundVipPromotion);
		System.out.println("promotionName = " + promotionName);
		System.out.println("discount = " + discount);
		System.out.println("discountType = " + discountType);
		System.out.println("foundPromotion = " + foundPromotion);
		System.out.println("isServiceItem = " + isServiceItem);
		System.out.println("isComposeItem = " + isComposeItem);
		System.out.println("isTax = " + isTax);
		System.out.println("taxType = " + taxType);
		System.out.println("taxRate = " + taxRate);
		System.out.println("discountRate = " + discountRate);
		System.out.println("deductionAmount = " + deductionAmount);*/
		break;
	    }
	}
	return salesOrderItem;
    }
    
    private SoSalesOrderItem getReturnItemInfo(SoSalesOrderItem origiSalesOrderItem, SoSalesOrderItem salesOrderItem){
	
	SoSalesOrderItem actualReturnItem = null;
	if(salesOrderItem != null){
	    if(!StringUtils.hasText(salesOrderItem.getWarehouseCode())){
		salesOrderItem.setWarehouseName("");
	    }else if(salesOrderItem.getWarehouseName() == null && salesOrderItem.getCurrentOnHandQty() == 0D){
		salesOrderItem.setCurrentOnHandQty(null);
	    }
	    if("Y".equals(salesOrderItem.getIsServiceItem())){
		salesOrderItem.setDiscountRate(100D);
		salesOrderItem.setDeductionAmount(null);
		salesOrderItem.setPromotionCode(null);
		salesOrderItem.setDiscountType(null);
	        salesOrderItem.setDiscount(null);
	    }
	    if("N".equals(salesOrderItem.getFoundVipPromotion())){
                if(StringUtils.hasText(salesOrderItem.getVipPromotionCode())){
                    salesOrderItem.setVipPromotionName(null);
                }else{
                    salesOrderItem.setVipPromotionName("");
                }
                salesOrderItem.setVipDiscountType(null);
                salesOrderItem.setVipDiscount(null);
	    }
	    if("N".equals(salesOrderItem.getFoundPromotion())){
                if(StringUtils.hasText(salesOrderItem.getPromotionCode())){
                    salesOrderItem.setPromotionName(null);
                }else{
                    salesOrderItem.setPromotionName("");
                }
                salesOrderItem.setDiscountType(null);
                salesOrderItem.setDiscount(null);
	    }
	    actualReturnItem = salesOrderItem;
	}else{
	    if(!StringUtils.hasText(origiSalesOrderItem.getItemCode())){
		origiSalesOrderItem.setItemCName("");
	    }else {
		origiSalesOrderItem.setItemCName(null);
	    }
	    
	    if(!StringUtils.hasText(origiSalesOrderItem.getWarehouseCode())){
		origiSalesOrderItem.setWarehouseName("");
	    }else {
		origiSalesOrderItem.setWarehouseName(null);
	    }
	    
	    if(!StringUtils.hasText(origiSalesOrderItem.getVipPromotionCode())){
		origiSalesOrderItem.setVipPromotionName("");
            }else{
        	origiSalesOrderItem.setVipPromotionName(null);
            }
	    
	    if(!StringUtils.hasText(origiSalesOrderItem.getPromotionCode())){
		origiSalesOrderItem.setPromotionName("");
            }else{
        	origiSalesOrderItem.setPromotionName(null);
            }
	    origiSalesOrderItem.setOriginalUnitPrice(null);
	    origiSalesOrderItem.setOriginalSalesAmount(null);
	    origiSalesOrderItem.setActualUnitPrice(null);
	    origiSalesOrderItem.setActualSalesAmount(null);
	    origiSalesOrderItem.setCurrentOnHandQty(null);
	    origiSalesOrderItem.setIsTax(null);
	    origiSalesOrderItem.setIsServiceItem("N");
	    origiSalesOrderItem.setIsComposeItem("N");
	    actualReturnItem = origiSalesOrderItem;
	}
	return actualReturnItem;
    }
}