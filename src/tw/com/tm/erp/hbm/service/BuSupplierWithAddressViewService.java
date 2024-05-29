package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.hbm.bean.BuCustomerWithAddressView;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.dao.BuSupplierWithAddressViewDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class BuSupplierWithAddressViewService {
    private static final Log log = LogFactory
	    .getLog(BuSupplierWithAddressViewService.class);

    BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO;

    public void save(BuSupplierWithAddressView transientInstance) {
	log.debug("saving BuSupplierWithAddressView instance");
	try {
	    buSupplierWithAddressViewDAO.save(transientInstance);
	    log.debug("save successful");
	} catch (RuntimeException re) {
	    log.error("save failed", re);
	    throw re;
	}
    }

    public void delete(BuSupplierWithAddressView persistentInstance) {
	log.debug("deleting BuSupplierWithAddressView instance");
	try {
	    buSupplierWithAddressViewDAO.delete(persistentInstance);
	    log.debug("delete successful");
	} catch (RuntimeException re) {
	    log.error("delete failed", re);
	    throw re;
	}
    }

    public BuSupplierWithAddressView findById(String id) {
	log.debug("getting BuSupplierWithAddressView instance with id: " + id);
	try {
	    return (BuSupplierWithAddressView) buSupplierWithAddressViewDAO
		    .findById(id);
	} catch (RuntimeException re) {
	    log.error("get failed", re);
	    throw re;
	}
    }

    public BuSupplierWithAddressView findById(String supplierCode,
	    String brandCode) {
	try {
	    return (BuSupplierWithAddressView) buSupplierWithAddressViewDAO
		    .findById(supplierCode, brandCode);
	} catch (RuntimeException re) {
	    log.error("get failed", re);
	    throw re;
	}
    }

    public List findByExample(BuSupplierWithAddressView instance) {
	log.debug("finding BuSupplierWithAddressView instance by example");
	try {
	    return buSupplierWithAddressViewDAO.findByExample(instance);
	} catch (RuntimeException re) {
	    log.error("find by example failed", re);
	    throw re;
	}
    }

    public List findByProperty(String propertyName, Object value) {
	log.debug("finding BuSupplierWithAddressView instance with property: "
		+ propertyName + ", value: " + value);
	try {
	    return buSupplierWithAddressViewDAO.findByProperty(propertyName,
		    value);
	} catch (RuntimeException re) {
	    log.error("find by property name failed", re);
	    throw re;
	}
    }

    public List findAll() {
	log.debug("finding all BuSupplierWithAddressView instances");
	try {
	    return buSupplierWithAddressViewDAO.findAll();
	} catch (RuntimeException re) {
	    log.error("find all failed", re);
	    throw re;
	}
    }

    public BuSupplierWithAddressView merge(
	    BuSupplierWithAddressView detachedInstance) {
	log.debug("merging BuSupplierWithAddressView instance");
	try {
	    return buSupplierWithAddressViewDAO.merge(detachedInstance);
	} catch (RuntimeException re) {
	    log.error("merge failed", re);
	    throw re;
	}
    }

    public void attachDirty(BuSupplierWithAddressView instance) {
	log.debug("attaching dirty BuSupplierWithAddressView instance");
	try {
	    buSupplierWithAddressViewDAO.attachDirty(instance);
	} catch (RuntimeException re) {
	    log.error("attach failed", re);
	    throw re;
	}
    }

    public void attachClean(BuSupplierWithAddressView instance) {
	log.debug("attaching clean BuSupplierWithAddressView instance");
	try {
	    buSupplierWithAddressViewDAO.attachClean(instance);
	    log.debug("attach successful");
	} catch (RuntimeException re) {
	    log.error("attach failed", re);
	    throw re;
	}
    }

    public static BuSupplierWithAddressViewService getFromApplicationContext(
	    ApplicationContext ctx) {
	return (BuSupplierWithAddressViewService) ctx
		.getBean("buSupplierWithAddressViewDAO");
    }

    public void setBuSupplierWithAddressViewDAO(
	    BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO) {
	this.buSupplierWithAddressViewDAO = buSupplierWithAddressViewDAO;
    }

    /**
     * 依據品牌代號及addressBookId，查詢供應商通訊資料
     * 
     * @param addressBookId
     * @param brandCode
     * @return BuSupplierWithAddressView
     * @throws Exception
     */
    public BuSupplierWithAddressView findSupplierByAddressBookIdAndBrandCode(
	    Long addressBookId, String brandCode) throws Exception {
	try {
	    return buSupplierWithAddressViewDAO
		    .findSupplierByAddressBookIdAndBrandCode(addressBookId,
			    brandCode);
	} catch (Exception ex) {
	    log.error("依據addressBookId和brandCode查詢供應商通訊資料時發生錯誤，原因："
		    + ex.toString());
	    throw new Exception("依據addressBookId和brandCode查詢供應商通訊資料時發生錯誤，原因："
		    + ex.getMessage());
	}
    }

    /**
     * 依據品牌代號及供應商代號，查詢供應商資料
     * 
     * @param brandCode
     * @param supplierCode
     * @return BuSupplierWithAddressView
     * @throws Exception
     */
    public BuSupplierWithAddressView findByBrandCodeAndSupplierCode(
	    String brandCode, String supplierCode) throws Exception {
	try {
	    return buSupplierWithAddressViewDAO.findByBrandCodeAndSupplierCode(
		    brandCode, supplierCode);
	} catch (Exception ex) {
	    log.error("依據品牌代號：" + brandCode + "、供應商代號：" + supplierCode
		    + "查詢供應商資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌代號：" + brandCode + "、供應商代號："
		    + supplierCode + "查詢供應商資料時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
	 * ajax取得供應商名稱
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public  List<Properties> getAJAXSupplierName(Properties httpRequest) throws Exception{
		List<Properties> result = new ArrayList();
	    Properties properties = new Properties();
	    String brandCode = null;
	    String supplierCode = null;    
	    try {
			brandCode = httpRequest.getProperty("brandCode");
			supplierCode = httpRequest.getProperty("supplierCode");
			supplierCode = supplierCode.trim().toUpperCase();
			Long addressBookId = NumberUtils.getLong( httpRequest.getProperty("addressBookId") );
			
			if( supplierCode != ""){
				properties.setProperty("supplierCode", supplierCode);
				
				BuSupplierWithAddressView buSupplierWithAddressView = findByBrandCodeAndSupplierCode(brandCode, supplierCode);
		        if(buSupplierWithAddressView != null){
		            properties.setProperty("supplierName", AjaxUtils.getPropertiesValue( buSupplierWithAddressView.getChineseName(), ""));
		            
		            properties.setProperty("currencyCode", AjaxUtils.getPropertiesValue( buSupplierWithAddressView.getCurrencyCode(), "1"));
		        } else {
		        	properties.setProperty("supplierName", "查無此供應商資料");
		        }
			}else if( addressBookId != 0L ){
				BuSupplierWithAddressView buSupplierWithAddressView = findSupplierByAddressBookIdAndBrandCode(addressBookId, brandCode);
		        if(buSupplierWithAddressView != null){
		        	properties.setProperty("supplierCode", AjaxUtils.getPropertiesValue( buSupplierWithAddressView.getSupplierCode(), ""));
		            properties.setProperty("supplierName", AjaxUtils.getPropertiesValue( buSupplierWithAddressView.getChineseName(), ""));
		            properties.setProperty("currencyCode", AjaxUtils.getPropertiesValue( buSupplierWithAddressView.getCurrencyCode(), "1"));
		        } else {
		        	properties.setProperty("supplierName", "查無此供應商資料");
		        }
			}
			
	        result.add(properties);	
	        
	        return result;	        
	    } catch (Exception ex) {
			log.error("依據品牌代號：" + brandCode + "、供應商號：" + supplierCode + "查詢時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢供應商號資料失敗！");
	    }        
	}
}