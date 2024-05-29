package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.SoShopDailyCompetitor;

public class SoShopDailyCompetitorDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(SoShopDailyCompetitorDAO.class);

    /**
     * 依據鄰櫃每日資料查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     */
    public List<SoShopDailyCompetitor> findShopDailyCompetitorList(HashMap conditionMap) {

	final String shopCode = (String) conditionMap.get("shopCode");
	final Date salesDate = (Date) conditionMap.get("salesDate");
	
	List<SoShopDailyCompetitor> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer(
				"from SoShopDailyCompetitor as model ");
			hql.append("where model.id.shopCode = :shopCode");			
			hql.append(" and model.id.salesDate = :salesDate");

			Query query = session.createQuery(hql.toString());
			/*query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);*/

			query.setString("shopCode", shopCode);
                        query.setDate("salesDate", salesDate);
			return query.list();
		    }
		});

	return result;
    }
}