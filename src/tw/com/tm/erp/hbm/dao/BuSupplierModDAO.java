package tw.com.tm.erp.hbm.dao;
 
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuSupplierMod;


public class BuSupplierModDAO extends BaseDAO{

	private static final Log log = LogFactory.getLog(BuSupplierModDAO.class);
	
	public BuSupplierMod findById(Long id) {
		log.debug("getting BuSupplierMod instance with id: " + id);
		try {
			BuSupplierMod instance = (BuSupplierMod) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuSupplierMod", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	
	
	public List<BuSupplierMod> findAll() {
		log.debug("finding all BuSupplierMod instances");
		try {
			String queryString = "from BuSupplierMod";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List<BuSupplierMod> find(HashMap conditionMap) {

		final String supplierCode = (String) conditionMap.get("supplierCode");
		final String brandCode = (String) conditionMap.get("brandCode");
		final String supplierTypeCode = (String) conditionMap.get("supplierTypeCode");
		final String categoryCode = (String) conditionMap.get("categoryCode");


		List<BuSupplierMod> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer("select model from BuSupplierMod as model");
						hql.append(" where model.headId != null");

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

	
	public BuSupplierMod findBysupplierCode(String supplierCode) {
		StringBuffer hql = new StringBuffer("from BuSupplierMod as model ");
		hql.append("where model.supplierCode = ? ");
		log.info("hqlllllll" + supplierCode);
		List<BuSupplierMod> lists = getHibernateTemplate().find(hql.toString(),
				new Object[] { supplierCode });
		return (lists != null && lists.size() > 0 ? lists.get(0) : null);
	}
	
	/**
	 * 更新PROCESS_ID，避免重複起流程
	 *
	 * @param headId
	 * @param ProcessId
	 * @throws Exception
	 */
	public int updateProcessId(Long headId, Long ProcessId) throws Exception {
		final String nativeSql = "UPDATE BU_SUPPLIER_MOD SET PROCESS_ID = " + ProcessId + " WHERE HEAD_ID = " + headId;
		System.out.println("更新PROCESS_ID SQL ::: " + nativeSql);
		int result = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(nativeSql);
				int iReturn = query.executeUpdate();
				return iReturn;
			}
		});
		return result;
	}
	
	
}
