package tw.com.tm.erp.hbm.dao;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.PrSupplier;
import tw.com.tm.erp.hbm.bean.PrSupplierMod;
import tw.com.tm.erp.hbm.bean.BuGoalAchevement;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuCurrency entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuCurrency
 * @author MyEclipse Persistence Tools
 */

public class PrSupplierDAO extends BaseDAO {
	
	
	public static final String SUPPLIER_NO = "supplierNo";	
	public static final String SUPPLIER = "supplier";
	public static final String TEL = "tel";
	public static final String FAX = "fax";
	public static final String INVOICE_TYPE_CODE = "invoiceTypeCode";
	public static final String UNIFIED_UNMBERING = "unifiedUnmbering";
	public static final String NAME = "name";
	public static final String EXECUTE_IN_CHARGE = "executeInCharge";
	public static final String ENABLE = "enable";
	public static final String STATUS = "status";
	public static final String ORDER_NO = "orderNo";
	public static final String ORDER_TYPE_CODE = "orderTypeCode";
	public static final String CREATED_BY = "createdBy";
	//public static final String CREATED_DATE = "creadtedDate";
	public static final String LAST_UPDATE_BY = "lastUpdateBy";
	//public static final DATE LAST_UPDATE_DATE = "lastUpdateDate";
	private static final Log log = LogFactory.getLog(BuCompanyDAO.class);

	protected void initDao() {
	}
	
	public PrSupplier findById(String supplierNo) {
		log.info("supplierNo: " + supplierNo);
		try {
			PrSupplier instance = (PrSupplier) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.PrSupplier", supplierNo);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
