package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.utils.AjaxUtils;

public class BuEmployeeWithAddressViewService{
	private static final Log log = LogFactory.getLog(BuEmployeeWithAddressViewService.class);

	private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO ;

	public void save(BuEmployeeWithAddressView transientInstance) {
		log.debug("saving BuEmployeeWithAddressView instance");
		try {
			buEmployeeWithAddressViewDAO.save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(BuEmployeeWithAddressView persistentInstance) {
		log.debug("deleting BuEmployeeWithAddressView instance");
		try {
			buEmployeeWithAddressViewDAO.delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuEmployeeWithAddressView findById(String id) {
		log.debug("getting BuEmployeeWithAddressView instance with id: " + id);
		try {
			System.out.println("getting BuEmployeeWithAddressView instance with id: " + id) ;
			id = id.trim().toUpperCase();
			return buEmployeeWithAddressViewDAO.findById(id);
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuEmployeeWithAddressView instance) {
		log.debug("finding BuEmployeeWithAddressView instance by example");
		try {
			return buEmployeeWithAddressViewDAO.findByExample(instance);
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding BuEmployeeWithAddressView instance with property: "
				+ propertyName + ", value: " + value);
		try {
			return buEmployeeWithAddressViewDAO.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all BuEmployeeWithAddressView instances");
		try {
			return buEmployeeWithAddressViewDAO.findAll();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public BuEmployeeWithAddressView merge(
			BuEmployeeWithAddressView detachedInstance) {
		log.debug("merging BuEmployeeWithAddressView instance");
		try {
			return buEmployeeWithAddressViewDAO.merge(detachedInstance);
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuEmployeeWithAddressView instance) {
		log.debug("attaching dirty BuEmployeeWithAddressView instance");
		try {
			buEmployeeWithAddressViewDAO.attachDirty(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuEmployeeWithAddressView instance) {
		log.debug("attaching clean BuEmployeeWithAddressView instance");
		try {
			buEmployeeWithAddressViewDAO.attachClean(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuEmployeeWithAddressViewService getFromApplicationContext(
			ApplicationContext ctx) {
		return (BuEmployeeWithAddressViewService) ctx
		.getBean("buEmployeeWithAddressViewDAO");
	}

	public void setBuEmployeeWithAddressViewDAO(
			BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
		this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
	}


	/**
	 * 依據品牌代號和工號進行查詢
	 * 
	 * @param brandCode
	 * @param employeeCode
	 * @return BuEmployeeWithAddressView
	 * @throws Exception
	 */
	public BuEmployeeWithAddressView findbyBrandCodeAndEmployeeCode(String brandCode, String employeeCode)
	throws Exception {

		try {
			employeeCode = employeeCode.trim().toUpperCase();
			return buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, employeeCode);
		} catch (Exception ex) {
			log.error("依據品牌代號：" + brandCode + "、工號：" + employeeCode + "查詢員工資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據品牌代號：" + brandCode + "、工號：" + employeeCode + "查詢員工資料時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 處理AJAX參數(依據品牌代號和工號進行查詢)
	 * 
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> findbyBrandCodeAndEmployeeCodeForAJAX(Properties httpRequest) throws Exception{

		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		String brandCode = null;
		String employeeCode = null;    
		try {
			brandCode = httpRequest.getProperty("brandCode");
			employeeCode = httpRequest.getProperty("employeeCode");
			employeeCode = employeeCode.trim().toUpperCase();
			BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, employeeCode);
			if(employeeWithAddressView != null){
				properties.setProperty("EmployeeCode", employeeWithAddressView.getEmployeeCode());
				properties.setProperty("EmployeeName", AjaxUtils.getPropertiesValue(employeeWithAddressView.getChineseName(), ""));            
			}else{
				properties.setProperty("EmployeeCode", employeeCode);
				properties.setProperty("EmployeeName", "查無此員工資料");   
			}
			result.add(properties);	

			return result;	        
		} catch (Exception ex) {
			log.error("依據品牌代號：" + brandCode + "、工號：" + employeeCode + "查詢員工資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢員工資料失敗！");
		}        
	}

	/** 依照 BuEmployeeWithAddressView 部門, 職位, 尋找 employee 
	 * @param findObjs
	 * @return
	 */
	public List<BuEmployeeWithAddressView> findByBuItemCategoryPrivilege( HashMap findObjs ){
		return buEmployeeWithAddressViewDAO.findByBuItemCategoryPrivilege(findObjs);
	}

}