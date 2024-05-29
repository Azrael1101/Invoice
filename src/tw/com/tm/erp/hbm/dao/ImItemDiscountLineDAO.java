/*Local*/

package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.ImItemDiscountHead;
import tw.com.tm.erp.hbm.bean.ImItemDiscountLine;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImItem entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImItem
 * @author MyEclipse Persistence Tools
 */

public class ImItemDiscountLineDAO extends BaseDAO {
	/**
	 * find page line 最後一筆 index
	 * 
	 * @param headId
	 * @return Long
	 */
	public Long findPageLineMaxIndex(Long headId) {

		Long lineMaxIndex = new Long(0);
		final Long hId = headId;
		List<ImItemDiscountHead> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer(
								"from ImItemDiscountHead as model where 1=1 ");
						if (hId != null)
							hql.append(" and model.headId = :headId");
						Query query = session.createQuery(hql.toString());
						if (hId != null)
							query.setLong("headId", hId);
						return query.list();
					}
				});
		if (result != null && result.size() > 0) {
			List<ImItemDiscountLine> imItemDiscountLines = result.get(0).getImItemDiscountLines();
			if (imItemDiscountLines != null && imItemDiscountLines.size() > 0) {
				lineMaxIndex = imItemDiscountLines.get(imItemDiscountLines.size() - 1)
						.getIndexNo();
			}
		}
		return lineMaxIndex;
	}
	
	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return List<CmDeclarationItem>
	 */
	public List<ImItemDiscountLine> findPageLine(Long headId, int startPage,
			int pageSize) {
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<ImItemDiscountLine> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer(
								"from ImItemDiscountLine as model where 1=1 ");
						if (hId != null)
							hql
									.append(" and model.imItemDiscountHead.headId = :headId order by indexNo");
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
}
