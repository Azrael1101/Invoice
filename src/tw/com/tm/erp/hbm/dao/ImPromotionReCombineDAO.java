package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionReCombine;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImPromotionShop entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImPromotionShop
 * @author MyEclipse Persistence Tools
 */

public class ImPromotionReCombineDAO extends BaseDAO {
	private static final Log log = LogFactory
			.getLog(ImPromotionReCombineDAO.class);
	// property constants
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";
	public static final String INDEX_NO = "indexNo";

	protected void initDao() {
		// do nothing
	}

	public void save(ImPromotionReCombine transientInstance) {
		try {
			getHibernateTemplate().save(transientInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(ImPromotionReCombine transientInstance) {
		getHibernateTemplate().update(transientInstance);
	}

	public void delete(ImPromotionReCombine persistentInstance) {

		try {
			getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return List<ImPromotionShop>
	 */
	public List<ImPromotionReCombine> findPageLine(Long headId, int startPage,
			int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<ImPromotionReCombine> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer(
								"from ImPromotionReCombine as model where 1=1 ");
						if (hId != null)
							hql
									.append(" and model.imPromotion.headId = :headId order by indexNo");
						Query query = session.createQuery(hql.toString());
						query.setFirstResult(startRecordIndexStar);
						query.setMaxResults(pSize);
						if (hId != null)
							query.setLong("headId", hId);
						return query.list();
					}
				});

		return result;
	}

	/**
	 * find page line 最後一筆 index
	 * 
	 * @param headId
	 * @return Long
	 */
	public Long findPageLineMaxIndex(Long headId) {

		Long lineMaxIndex = new Long(0);
		final Long hId = headId;
		List result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer(
								"select count(model.imPromotion) as rowCount from ImPromotionReCombine as model where 1=1");
						if (hId != null)
							hql
									.append(" and model.imPromotion.headId = :headId");
						Query query = session.createQuery(hql.toString());
						if (hId != null)
							query.setLong("headId", hId);
						return query.list();
					}
				});
		if (result != null && result.size() > 0) {
			Long rowCount = (Long) result.get(0);
			if (rowCount != null)
				lineMaxIndex = rowCount.longValue();
		}
		return lineMaxIndex;
	}

	public List<ImPromotionReCombine> findReCombineByIdentification(Long headId) {

		StringBuffer hql = new StringBuffer(
				"from ImPromotionReCombine as model where model.headId = ?");
		List<ImPromotionReCombine> result = getHibernateTemplate().find(
				hql.toString(), new Object[] { headId });
		return result;
	}

	public HashMap findPageCombineLine(HashMap findObjs, int startPage, int pageSize,
			int searchType) {

		final HashMap fos = findObjs;
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final int type = searchType;
		List result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						String combineCode = (String) fos.get("combineCode");
						String itemBrand = (String) fos.get("itemBrand");
						String category02 = (String) fos.get("category02");
						String enable = (String) fos.get("enable");
						Date enableDate = (Date) fos.get("actualStartDate");
						String foreignCategory = (String) fos.get("foreignCategory");
						
						StringBuffer hql = new StringBuffer("");
						if (BaseDAO.QUERY_RECORD_COUNT == type) {
							hql.append("select count(model.headId) as rowCount from ImPromotionReCombine as model where 1=1 ");
						} else if (BaseDAO.QUERY_SELECT_ALL == type) {
							hql.append("select model.headId from ImPromotionReCombine as model where 1=1 ");
						} else {
							hql.append("from ImPromotionReCombine as model where 1=1 ");
						}
						
						if (StringUtils.hasText(combineCode))
							hql.append(" and model.combineCode = :combineCode");
						
						if (StringUtils.hasText(itemBrand))
							hql.append(" and model.itemBrand = :itemBrand");
						
						if (StringUtils.hasText(category02))
							hql.append(" and model.category02 = :category02");
						
						if (StringUtils.hasText(enable))
							hql.append(" and model.enable = :enable");
						
						if (enableDate != null)
							hql.append(" and model.enableDate = :enableDate");
						
						if (StringUtils.hasText(foreignCategory))
							hql.append(" and model.foreignCategory = :foreignCategory");
						
						hql.append(" order by model.enableDate desc , model.lastUpdateDate ");
						Query query = session.createQuery(hql.toString());
						
						if (BaseDAO.QUERY_SELECT_RANGE == type) {
							query.setFirstResult(startRecordIndexStar);
							query.setMaxResults(pSize);
						}

						if (StringUtils.hasText(combineCode))
							query.setString("combineCode", combineCode);
						
						if (StringUtils.hasText(itemBrand))
							query.setString("itemBrand", itemBrand);
						
						if (StringUtils.hasText(category02))
							query.setString("category02", category02);
						
						if (StringUtils.hasText(enable))
							query.setString("enable", enable);
						
						if (enableDate != null)
							query.setDate("enableDate", enableDate);
						
						if (StringUtils.hasText(foreignCategory))
							query.setString("foreignCategory", foreignCategory);
						
						return query.list();
					}
				});
		HashMap returnResult = new HashMap();
		returnResult.put(BaseDAO.TABLE_LIST, BaseDAO.QUERY_SELECT_ALL == type
				|| BaseDAO.QUERY_SELECT_RANGE == type ? result : null);
		if (result.size() == 0) {
			returnResult.put(BaseDAO.TABLE_RECORD_COUNT, 0L);
		} else {
			returnResult.put(BaseDAO.TABLE_RECORD_COUNT,
					BaseDAO.QUERY_SELECT_ALL == type
							|| BaseDAO.QUERY_SELECT_RANGE == type ? result
							.size() : Long.valueOf(result.get(0).toString()));
		}
		return returnResult;
	}
	
	public List<ImPromotionReCombine> findCombineEable(String brandCode, String actualDataDate, String actualDataDateEnd){
		log.info("停用日期 ="+actualDataDateEnd);
		StringBuffer hql = new StringBuffer(
		"from ImPromotionReCombine as model where model.endDate = TO_DATE(?,'YYYYMMDD') ");
		List<ImPromotionReCombine> result = getHibernateTemplate().find(
				hql.toString(), new Object[] { actualDataDateEnd });
		return result;
	}
	
	public List<ImPromotionReCombine> findCombineListByProperty(HashMap conditionMap){
		final String brandCode = (String) conditionMap.get("brandCode");
		final String dataDate = (String) conditionMap.get("dataDate");
		final String dataDateEnd = (String) conditionMap.get("dataDateEnd");
		
		StringBuffer hql = new StringBuffer(
		"from ImPromotionReCombine as model where model.enableDate = TO_DATE(?,'YYYYMMDD') or model.endDate = TO_DATE(?,'YYYYMMDD') ");
		hql.append(" order by model.lastUpdateDate ");
		List<ImPromotionReCombine> result = getHibernateTemplate().find(
				hql.toString(), new Object[] { dataDate, dataDateEnd });

		return result;
	}
}