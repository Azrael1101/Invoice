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

import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.ImItemDiscount;
import tw.com.tm.erp.hbm.bean.ImItemDiscountId;
import tw.com.tm.erp.hbm.bean.SiGroup;
import tw.com.tm.erp.hbm.bean.SiGroupId;

public class ImItemDiscountDAO extends BaseDAO {

	private static final Log log = LogFactory.getLog(ImItemDiscountDAO.class);

	/**
	 * 依據品牌、最後更新日期查詢
	 * 
	 * @param conditionMap
	 * @return List<ImItemDiscount>
	 */
	public List<ImItemDiscount> findItemDiscountListByProperty(HashMap conditionMap) {
		final String brandCode = (String) conditionMap.get("brandCode");
		final String dataDate = (String) conditionMap.get("dataDate");
		List<ImItemDiscount> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select model from ImItemDiscount as model ");
				hql.append("where model.id.brandCode = :brandCode ");
				if(StringUtils.hasText(dataDate)){
					hql.append("and to_char(model.lastUpdateDate, 'YYYYMMDD') = :dataDate ");
				}
				Query query = session.createQuery(hql.toString());
				query.setString("id.brandCode", brandCode);
				if(StringUtils.hasText(dataDate)){
					query.setString("dataDate", dataDate);
				}
				return query.list();
			}
		});

		return result;
	}
	public ImItemDiscount findById(java.lang.String brandCode, java.lang.String vipTypeCode, java.lang.String itemDiscountType) {

		final String itemCode1 = brandCode;
		final String itemCode2 = vipTypeCode;
		final String itemCode3 = itemDiscountType;
		List<ImItemDiscount> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImItemDiscount as model where 1=1 ");
				hql.append(" and upper(model.id.brandCode)  = :itemCode1 ");
				hql.append(" and upper(model.id.vipTypeCode)  = :itemCode2 ");
				hql.append(" and upper(model.id.itemDiscountType)  = :itemCode3 ");

				Query query = session.createQuery(hql.toString());
				query.setString("itemCode1", itemCode1);
				query.setString("itemCode2", itemCode2);
				query.setString("itemCode3", itemCode3);

				return query.list();
			}
		});
		if (result.size() > 0)
			return result.get(0);
		else
			return null;
	}

	public List findItemDiscountByCondition(HashMap conditionMap) {
		final String brandCode = (String) conditionMap.get("brandCode");
		final String dataDate = (String) conditionMap.get("dataDate");
		final String dataDateEnd = (String) conditionMap.get("dataDateEnd");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select model from ImItemDiscount as model where 1=1 ");
				hql.append(" and model.id.brandCode  = :brandCode ");
				if(StringUtils.hasText(dataDate))
					hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') >= :dataDate ");
				if(StringUtils.hasText(dataDateEnd))
					hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') <= :dataDateEnd ");
				log.info("findItemDiscountByCondition hql = " + hql.toString());
				Query query = session.createQuery(hql.toString());
				query.setString("brandCode", brandCode);
				if(StringUtils.hasText(dataDate))
					query.setString("dataDate", dataDate);
				if(StringUtils.hasText(dataDateEnd))
					query.setString("dataDateEnd", dataDateEnd);
				return query.list();
			}
		});

		return result;
	}
	
	public ImItemDiscount findById(ImItemDiscountId imItemDiscountId) {
		log.info("getting id: " + imItemDiscountId);
		try {
			ImItemDiscount instance = (ImItemDiscount) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.ImItemDiscount", imItemDiscountId);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
