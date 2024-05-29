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
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;

public class BuSupplierDAO extends BaseDAO {
   
	private static final Log log = LogFactory.getLog(BuSupplierDAO.class);

    public BuSupplier findSupplierByAddressBookIdAndBrandCode(
	    Long addressBookId, String brandCode) {

	StringBuffer hql = new StringBuffer("from BuSupplier as model ");
	hql.append("where model.id.brandCode = ? ");
	hql.append("and model.addressBookId = ? ");

	List<BuSupplier> result = getHibernateTemplate().find(hql.toString(),
		new Object[] { brandCode, addressBookId });
	return (result != null && result.size() > 0 ? result.get(0) : null);

    }
    public List<BuSupplier> findAll() {
		log.debug("finding all BuSupplier instances");
		try {
			String queryString = "from BuSupplier";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
    public BuSupplier findById(BuSupplierId buSupplierId) {
		log.debug("getting BuSupplier instance with id: " + buSupplierId);
		try {
			BuSupplier instance = (BuSupplier) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuSupplier", buSupplierId);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
   
    
    
    public List<BuSupplier> find(HashMap conditionMap) {

		final String supplierCode = (String) conditionMap.get("supplierCode");
		final String brandCode = (String) conditionMap.get("brandCode");
		final String supplierTypeCode = (String) conditionMap.get("supplierTypeCode");
		final String categoryCode = (String) conditionMap.get("categoryCode");


		List<BuSupplier> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer("select model from BuSupplier as model");
						hql.append(" where model.id != null");

						if (StringUtils.hasText(supplierCode))
							hql.append(" and model.supplierCode like :supplierCode");

						if (StringUtils.hasText(brandCode))
							hql.append(" and model.brandCode like :brandCode");

						if (StringUtils.hasText(supplierTypeCode))
							hql.append(" and model.supplierTypeCode like :supplierTypeCode");
						
						if (StringUtils.hasText(categoryCode))
							hql.append(" and model.categoryCode like :categoryCode");


						Query query = session.createQuery(hql.toString());
						query.setFirstResult(0);
						query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

						if (StringUtils.hasText(supplierCode))
							query.setString("supplierCode", "%" + supplierCode + "%");

						if (StringUtils.hasText(brandCode))
							query.setString("brandCode", "%" + brandCode + "%");

						if (StringUtils.hasText(supplierTypeCode))
							query.setString("supplierTypeCode", "%" + supplierTypeCode + "%");
						
						if (StringUtils.hasText(categoryCode))
							query.setString("categoryCode", "%" + categoryCode + "%");


					return query.list();
					}
				});

		return result;
	}
    
}