package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.ImItemEanPriceView;
import tw.com.tm.erp.hbm.dao.ImItemEanPriceViewDAO;
import tw.com.tm.erp.utils.AjaxUtils;

public class ImItemEanPriceViewService {

    private static final Log log = LogFactory.getLog(ImItemEanPriceViewService.class);
    
    private ImItemEanPriceViewDAO imItemEanPriceViewDAO;

    public void setImItemEanPriceViewDAO(ImItemEanPriceViewDAO imItemEanPriceViewDAO) {
	this.imItemEanPriceViewDAO = imItemEanPriceViewDAO;
    }

    /**
     * 依據品牌及國際碼查詢
     * 
     * @param brandCode
     * @param eanCode
     * @return ImItemEanPriceView
     * @throws Exception
     */
    public ImItemEanPriceView findById(String brandCode, String eanCode) throws Exception {
	
	try {
	    eanCode = eanCode.trim().toUpperCase();
	    return imItemEanPriceViewDAO.findById(brandCode, eanCode);
	} catch (Exception ex) {
	    log.error("依據品牌(" + brandCode + ")、國際碼(" + eanCode + ")查詢商品相關資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌(" + brandCode + ")、國際碼(" + eanCode + ")查詢商品相關資料時發生錯誤，原因："
		    + ex.getMessage());
	}
    }
    
    /**
     * 依據品牌及品號查詢
     * 
     * @param brandCode
     * @param itemCode
     * @return List<ImItemEanPriceView>
     * @throws Exception
     */
    public List<ImItemEanPriceView> findByBrandCodeAndItemCode(String brandCode, String itemCode) throws Exception {
	
	try {
	    itemCode = itemCode.trim().toUpperCase();
	    return imItemEanPriceViewDAO.findByBrandCodeAndItemCode(brandCode, itemCode);
	} catch (Exception ex) {
	    log.error("依據品牌(" + brandCode + ")、品號(" + itemCode + ")查詢商品相關資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌(" + brandCode + ")、品號(" + itemCode + ")查詢商品相關資料時發生錯誤，原因："
		    + ex.getMessage());
	}
    }
    
    /**
     * 依據品牌及(品號 or 國際碼)查詢
     * 
     * @param brandCode
     * @param tempCode
     * @return ImItemEanPriceView
     * @throws Exception
     */
    public ImItemEanPriceView getItemInfoByProperty(String brandCode, String itemCode) throws Exception {
	
	try {
	    itemCode = itemCode.trim().toUpperCase();
	    return imItemEanPriceViewDAO.getItemInfoByProperty(brandCode, itemCode);
	} catch (Exception ex) {
	    log.error("依據品牌(" + brandCode + ")、品號(" + itemCode + ")查詢商品相關資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌(" + brandCode + ")、品號(" + itemCode + ")查詢商品相關資料時發生錯誤，原因："
		    + ex.getMessage());
	}
    }
    
    /**
     * 依據品牌及(品號 or 國際碼)查詢取得商品資料(AJAX)
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getItemInfoForAJAX(Properties httpRequest) throws Exception{
	
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String brandCode = null;
	String itemCode = null;
	//回傳預設值
	String eanCode = null;
	String itemCName = "查無資料";
	String itemEName = null;
	String salesUnit = null;
	String currencyCode = null;
	Double unitPrice = null;
	String isServiceItem = null;
	String itemEnable = null;
	String priceEnable = null;
		
	try{
	    brandCode = httpRequest.getProperty("brandCode");
	    itemCode = httpRequest.getProperty("itemCode");
	    itemCode = itemCode.trim().toUpperCase();
	    ImItemEanPriceView itemEanPriceView = imItemEanPriceViewDAO.getItemInfoByProperty(brandCode, itemCode);
	    if(itemEanPriceView != null){
		eanCode = itemEanPriceView.getEanCode();
		itemCName = AjaxUtils.getPropertiesValue(itemEanPriceView.getItemCName(), "");
		itemEName = itemEanPriceView.getItemEName();
		salesUnit = itemEanPriceView.getSalesUnit();
		currencyCode = itemEanPriceView.getCurrencyCode();
		unitPrice = itemEanPriceView.getUnitPrice();		
		isServiceItem = itemEanPriceView.getIsServiceItem();
		itemEnable = itemEanPriceView.getItemEnable();
		priceEnable = itemEanPriceView.getPriceEnable();		
	    }	    
	    properties.setProperty("BrandCode", brandCode);
	    properties.setProperty("ItemCode", itemCode);
	    properties.setProperty("EanCode", AjaxUtils.getPropertiesValue(eanCode, ""));	    
	    properties.setProperty("ItemCName", itemCName);	
	    properties.setProperty("ItemEName", AjaxUtils.getPropertiesValue(itemEName, ""));	
	    properties.setProperty("SalesUnit", AjaxUtils.getPropertiesValue(salesUnit, ""));	
	    properties.setProperty("CurrencyCode", AjaxUtils.getPropertiesValue(currencyCode, ""));	
	    properties.setProperty("UnitPrice", AjaxUtils.getPropertiesValue(unitPrice, ""));	
	    properties.setProperty("IsServiceItem", AjaxUtils.getPropertiesValue(isServiceItem, ""));	
	    properties.setProperty("ItemEnable", AjaxUtils.getPropertiesValue(itemEnable, ""));	
	    properties.setProperty("PriceEnable", AjaxUtils.getPropertiesValue(priceEnable, ""));	
	    result.add(properties);
	    
	    return result;
	    
	}catch (Exception ex) {
	    log.error("依據品牌(" + brandCode + ")、品號(" + itemCode + ")查詢商品相關資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌(" + brandCode + ")、品號(" + itemCode + ")查詢商品相關資料失敗！");
	}	
    }
}
