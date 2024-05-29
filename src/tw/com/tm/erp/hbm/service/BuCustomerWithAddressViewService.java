package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.BuCustomerWithAddressView;
import tw.com.tm.erp.hbm.dao.BuCustomerWithAddressViewDAO;
import tw.com.tm.erp.utils.AjaxUtils;

public class BuCustomerWithAddressViewService {

    private static final Log log = LogFactory
	    .getLog(BuCustomerWithAddressViewDAO.class);

    private BuCustomerWithAddressViewDAO buCustomerWithAddressViewDAO;

    public void save(BuCustomerWithAddressView transientInstance) {

	buCustomerWithAddressViewDAO.save(transientInstance);
    }

    public void delete(BuCustomerWithAddressView persistentInstance) {

	buCustomerWithAddressViewDAO.delete(persistentInstance);
    }

    public BuCustomerWithAddressView findById(String id) {

	return buCustomerWithAddressViewDAO.findById(id);
    }

    public List findByExample(BuCustomerWithAddressView instance) {

	return buCustomerWithAddressViewDAO.findByExample(instance);
    }

    public List findByProperty(String propertyName, Object value) {

	return buCustomerWithAddressViewDAO.findByProperty(propertyName, value);
    }

    public List findAll() {
	return buCustomerWithAddressViewDAO.findAll();
    }

    /**
     * 依據品牌代號及客戶代號或身份ID，查詢啟用狀態之客戶通訊資料
     * 
     * @param brandCode
     * @param customerCode
     * @param type
     * @param isEnable
     * @return BuCustomerWithAddressView
     * @throws Exception
     */
    public BuCustomerWithAddressView findCustomerByType(String brandCode,
	    String customerCode, String type, String isEnable) throws Exception {
	try {
	    customerCode = customerCode.trim().toUpperCase();
	    return buCustomerWithAddressViewDAO.findCustomerByType(
		    brandCode, customerCode, type, isEnable);
	} catch (Exception ex) {
	    log.error("依據品牌代號：" + brandCode + "、客戶代碼：" + customerCode + "查詢客戶通訊資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌代號：" + brandCode + "、客戶代碼：" + customerCode + "查詢客戶通訊資料時發生錯誤，原因："
		    + ex.getMessage());
	}
    }
    
   
    /**
     * 處理AJAX參數(依據品牌代號及客戶代號或身份ID，查詢啟用狀態之客戶通訊資料)
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> findCustomerByTypeForAJAX(Properties httpRequest) throws Exception{
	
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
        String brandCode = null;
        String customerCode = null;
        String searchCustomerType = null;
        String isEnable = null;
	try {
	    brandCode = httpRequest.getProperty("brandCode");
	    customerCode = httpRequest.getProperty("customerCode");
	    customerCode = customerCode.trim().toUpperCase();
	    searchCustomerType = httpRequest.getProperty("searchCustomerType");
	    isEnable = httpRequest.getProperty("isEnable");
	    //if(!StringUtils.hasText(isEnable)){
		//isEnable = null;
	    //}
	    
	    BuCustomerWithAddressView customerWithAddressView = buCustomerWithAddressViewDAO.findCustomerByType
	        (brandCode, customerCode, searchCustomerType, isEnable);
	    if(customerWithAddressView != null){
		properties.setProperty("CustomerCode_var", customerCode);
		properties.setProperty("CustomerCode", AjaxUtils.getPropertiesValue(customerWithAddressView.getCustomerCode(), ""));
		properties.setProperty("ContactPerson", AjaxUtils.getPropertiesValue(customerWithAddressView.getContactPerson1(), ""));
		properties.setProperty("ContactTel", AjaxUtils.getPropertiesValue(customerWithAddressView.getTel1(), ""));
		properties.setProperty("Receiver", AjaxUtils.getPropertiesValue(customerWithAddressView.getContactPerson1(), ""));	
		properties.setProperty("CountryCode", AjaxUtils.getPropertiesValue(customerWithAddressView.getCountryCode(), ""));
		properties.setProperty("CurrencyCode", AjaxUtils.getPropertiesValue(customerWithAddressView.getCurrencyCode(), "NTD"));		
		properties.setProperty("PaymentTermCode", AjaxUtils.getPropertiesValue(customerWithAddressView.getPaymentTermCode(), ""));
		properties.setProperty("InvoiceCity", AjaxUtils.getPropertiesValue(customerWithAddressView.getCity2(), ""));
		properties.setProperty("InvoiceArea", AjaxUtils.getPropertiesValue(customerWithAddressView.getArea2(), ""));	
		properties.setProperty("InvoiceZipCode", AjaxUtils.getPropertiesValue(customerWithAddressView.getZipCode2(), ""));		
		properties.setProperty("InvoiceAddress", AjaxUtils.getPropertiesValue(customerWithAddressView.getAddress2(), ""));		
		properties.setProperty("ShipCity", AjaxUtils.getPropertiesValue(customerWithAddressView.getCity3(), ""));		
		properties.setProperty("ShipArea", AjaxUtils.getPropertiesValue(customerWithAddressView.getArea3(), ""));
		properties.setProperty("ShipZipCode", AjaxUtils.getPropertiesValue(customerWithAddressView.getZipCode3(), ""));		
		properties.setProperty("ShipAddress", AjaxUtils.getPropertiesValue(customerWithAddressView.getAddress3(), ""));		
		properties.setProperty("InvoiceTypeCode", AjaxUtils.getPropertiesValue(customerWithAddressView.getInvoiceTypeCode(), ""));
		properties.setProperty("TaxType", AjaxUtils.getPropertiesValue(customerWithAddressView.getTaxType(), "3"));
		properties.setProperty("TaxRate", AjaxUtils.getPropertiesValue(customerWithAddressView.getTaxRate(), "5.0"));  
		properties.setProperty("GuiCode", ("2".equals(customerWithAddressView.getType()))?AjaxUtils.getPropertiesValue(customerWithAddressView.getIdentityCode(), ""):"");	
		properties.setProperty("CustomerType", AjaxUtils.getPropertiesValue(customerWithAddressView.getCustomerTypeCode(), ""));
		properties.setProperty("CustomerName", AjaxUtils.getPropertiesValue(customerWithAddressView.getChineseName(), ""));
		properties.setProperty("VipType", AjaxUtils.getPropertiesValue(customerWithAddressView.getVipTypeCode(), ""));	
		properties.setProperty("VipPromotionCode", AjaxUtils.getPropertiesValue(customerWithAddressView.getPromotionCode(), ""));    
	    }else{
		properties.setProperty("CustomerCode_var", customerCode);
		properties.setProperty("CustomerCode", "");
		properties.setProperty("ContactPerson", "");
		properties.setProperty("ContactTel", "");
		properties.setProperty("Receiver", "");	
		properties.setProperty("CountryCode", "");
		properties.setProperty("CurrencyCode", "NTD");		
		properties.setProperty("PaymentTermCode", "");
		properties.setProperty("InvoiceCity", "");
		properties.setProperty("InvoiceArea", "");	
		properties.setProperty("InvoiceZipCode", "");		
		properties.setProperty("InvoiceAddress", "");		
		properties.setProperty("ShipCity", "");		
		properties.setProperty("ShipArea", "");
		properties.setProperty("ShipZipCode", "");		
		properties.setProperty("ShipAddress", "");		
		properties.setProperty("InvoiceTypeCode", "");
		properties.setProperty("TaxType", "3");
		properties.setProperty("TaxRate", "5.0");  
		properties.setProperty("GuiCode", "");	
		properties.setProperty("CustomerType", "");
		properties.setProperty("CustomerName", "查無此客戶資料");
		properties.setProperty("VipType", "");	
		properties.setProperty("VipPromotionCode", "");
	    }
	    result.add(properties);
	    
	    return result;
	} catch (Exception ex) {
	    log.error("依據品牌代號：" + brandCode + "、客戶代碼：" + customerCode + "查詢客戶通訊資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢客戶資料失敗！");
	}
    }

    /**
     * 依據品牌代號及addressBookId，查詢客戶通訊資料
     * 
     * @param addressBookId
     * @param brandCode
     * @return BuCustomerWithAddressView
     * @throws Exception
     */
    public BuCustomerWithAddressView findCustomerByAddressBookIdAndBrandCode(
	    Long addressBookId, String brandCode) throws Exception {
	try {
	    return buCustomerWithAddressViewDAO
		    .findCustomerByAddressBookIdAndBrandCode(addressBookId,
			    brandCode);
	} catch (Exception ex) {
	    log.error("依據addressBookId和brandCode查詢客戶通訊資料時發生錯誤，原因："
		    + ex.toString());
	    throw new Exception("依據addressBookId和brandCode查詢客戶通訊資料時發生錯誤，原因："
		    + ex.getMessage());
	}
    }

    public BuCustomerWithAddressView merge(
	    BuCustomerWithAddressView detachedInstance) {

	return buCustomerWithAddressViewDAO.merge(detachedInstance);
    }

    public void attachDirty(BuCustomerWithAddressView instance) {

	buCustomerWithAddressViewDAO.attachDirty(instance);
    }

    public void attachClean(BuCustomerWithAddressView instance) {

	buCustomerWithAddressViewDAO.attachClean(instance);
    }

    public static BuCustomerWithAddressViewDAO getFromApplicationContext(
	    ApplicationContext ctx) {
	return (BuCustomerWithAddressViewDAO) ctx
		.getBean("buCustomerWithAddressViewDAO");
    }

    public void setBuCustomerWithAddressViewDAO(
	    BuCustomerWithAddressViewDAO buCustomerWithAddressViewDAO) {
	this.buCustomerWithAddressViewDAO = buCustomerWithAddressViewDAO;
    }

}
