package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.ImPromotionView;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionViewDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;

public class ImPromotionViewService {

    private static final Log log = LogFactory
	    .getLog(ImPromotionViewService.class);

    private ImPromotionViewDAO imPromotionViewDAO;

    public void setImPromotionViewDAO(ImPromotionViewDAO imPromotionViewDAO) {
	this.imPromotionViewDAO = imPromotionViewDAO;
    }

    /**
     * 依據促銷活動種類查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     */
    public List<ImPromotionView> findPromotionList(HashMap conditionMap) throws Exception{
	try{
	    String promotionCode_Start = (String)conditionMap.get("promotionCode_Start");
            String promotionCode_End = (String)conditionMap.get("promotionCode_End");
            String promotionName = (String)conditionMap.get("promotionName");	
	    String shopCode = (String)conditionMap.get("shopCode");
            String itemCode = (String)conditionMap.get("itemCode");
		
            conditionMap.put("promotionCode_Start", promotionCode_Start.trim().toUpperCase());
            conditionMap.put("promotionCode_End", promotionCode_End.trim().toUpperCase());         
	    conditionMap.put("promotionName", promotionName.trim());
	    conditionMap.put("shopCode", shopCode.trim().toUpperCase());   
	    conditionMap.put("itemCode", itemCode.trim().toUpperCase());   
	        
	    return imPromotionViewDAO.findPromotionList(conditionMap);
	}catch (Exception ex) {
	    log.error("查詢促銷活動種類時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢促銷活種類時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 依據品牌代號、活動代號、價格類別、品號、櫃號、客戶類型、會員類型等條件查詢
     * 
     * @param conditionMap
     * @return ImPromotionView
     */
    public ImPromotionView findPromotionCodeByProperty(HashMap conditionMap) throws Exception{
	
        try{
            String promotionCode = (String) conditionMap.get("promotionCode");
            String itemCode = (String) conditionMap.get("itemCode");
	    conditionMap.put("promotionCode", promotionCode.trim().toUpperCase());
	    if(itemCode != null){
                conditionMap.put("itemCode", itemCode.trim().toUpperCase());
	    }
	    return imPromotionViewDAO.findPromotionCodeByProperty(conditionMap);
	}catch (Exception ex) {
	    log.error("依據品牌代號、活動代號、價格類別、品號、櫃號、客戶類型、會員類型查詢促銷活動種類時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌代號、活動代號、價格類別、品號、櫃號、客戶類型、會員類型查詢促銷活動種類時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    
    /**
     * 處理AJAX參數(依據品牌代號、活動代號、價格類別、品號、櫃號、客戶類型、會員類型等條件查詢)
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> findPromotionCodeByPropertyForAJAX(Properties httpRequest) throws Exception{
	
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
        try{
            String brandCode = httpRequest.getProperty("brandCode");
            String promotionCode = httpRequest.getProperty("promotionCode");
            promotionCode = promotionCode.trim().toUpperCase();
            String priceType = httpRequest.getProperty("priceType");          
            String shopCode = httpRequest.getProperty("shopCode");
            String customerType = httpRequest.getProperty("customerType");
            String vipType = httpRequest.getProperty("vipType");
            String salesDate = httpRequest.getProperty("salesDate");
	    salesDate = DateUtils.format(DateUtils.parseDate("yyyy/MM/dd", salesDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
	    
            HashMap conditionMap = new HashMap();
            conditionMap.put("brandCode", brandCode);
	    conditionMap.put("promotionCode", promotionCode);
            conditionMap.put("priceType", priceType);
	    conditionMap.put("shopCode", shopCode);
	    conditionMap.put("customerType", customerType);
	    conditionMap.put("vipType", vipType);
	    conditionMap.put("salesDate", salesDate);
	    Object[] promotionInfo = imPromotionViewDAO.findPromotionCodeByCondition(conditionMap);
	    properties.setProperty("PromotionCode", promotionCode);
	    properties.setProperty("PromotionName", "查無資料");
	    if(promotionInfo != null){
		if(promotionInfo[3] != null && promotionInfo[4] != null)             
		    properties.setProperty("PromotionName", AjaxUtils.getPropertiesValue(promotionInfo[2], ""));
	    }
	    result.add(properties);
	    
	    return result;
	}catch (Exception ex) {
	    log.error("依據銷售日期、品牌代號、活動代號、價格類別、品號、櫃號、客戶類型、會員類型查詢促銷活動種類時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢促銷活動資料失敗！");
		}
    }
  
}
