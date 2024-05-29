package tw.com.tm.erp.hbm.dao;



import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.SnsBonusPointSetting;

public class SnsBonusPointSettingDAO extends BaseDAO {
	private static final Log log = LogFactory
			.getLog(SnsBonusPointSettingDAO.class);

	/**
	 * 新增紅利規則
	 * 
	 */
	public void save(SnsBonusPointSetting snsBonusPointSetting) {
		log.debug("-----開始新增-----");
		try {
			getHibernateTemplate().save(snsBonusPointSetting);
			log.debug("新增成功");
		} catch (RuntimeException re) {
			log.error("新增失敗", re);
			throw re;
		}
	}
	
	public void update(SnsBonusPointSetting snsBonusPointSetting) {
		try {
			getHibernateTemplate().update(snsBonusPointSetting);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void saveOrUpdate(SnsBonusPointSetting snsBonusPointSetting) {
		if(snsBonusPointSetting.getXID() == null || "".equals(snsBonusPointSetting.getXID())) {
			save(snsBonusPointSetting);
		} else {
			update(snsBonusPointSetting);
		}
	}

	public void delete(SnsBonusPointSetting snsBonusPointSetting) {
		log.debug("deleting BuCountry instance");
		try {
			getHibernateTemplate().delete(snsBonusPointSetting);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	/**
	 * 依據商品大類查詢出對應紅利率，Mapping進行累積點數
	 * 
	 * @param categoryCode
	 * @return bonusRate
	 */
	public List<SnsBonusPointSetting> getBonusRateByCategoryCode(String categoryCode) {
		log.debug("依據商品大類查詢" + categoryCode);
		try {
            StringBuffer hql = new StringBuffer("from SnsBonusPointSetting as model ");
            hql.append("where model.categoryCode = ? ");
            List<SnsBonusPointSetting> lists = getHibernateTemplate().find(hql.toString(),
            		new Object[] { categoryCode });
            return lists;
		} catch (RuntimeException re) {
			log.error("查尋失敗", re);
			throw re;
		}
	}



}