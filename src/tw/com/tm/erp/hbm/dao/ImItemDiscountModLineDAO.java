/*Local*/

package tw.com.tm.erp.hbm.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.FiBudgetModLine;
import tw.com.tm.erp.hbm.bean.ImItemDiscountModHead;
import tw.com.tm.erp.hbm.bean.ImItemDiscountModLine;

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

public class ImItemDiscountModLineDAO extends BaseDAO {
	/**
	 * find page line 最後一筆 index
	 * 
	 * @param headId
	 * @return Long
	 */
	public Long findPageLineMaxIndex(Long headId) {

		Long lineMaxIndex = new Long(0);
		final Long hId = headId;
		List<ImItemDiscountModHead> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer(
								"from ImItemDiscountModHead as model where 1=1 ");
						if (hId != null)
							hql.append(" and model.headId = :headId");
						Query query = session.createQuery(hql.toString());
						if (hId != null)
							query.setLong("headId", hId);
						return query.list();
					}
				});
		if (result != null && result.size() > 0) {
			List<ImItemDiscountModLine> ImItemDiscountModLines = result.get(0).getImItemDiscountModLines();
			if (ImItemDiscountModLines != null && ImItemDiscountModLines.size() > 0) {
				lineMaxIndex = ImItemDiscountModLines.get(ImItemDiscountModLines.size() - 1)
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
	public List<ImItemDiscountModLine> findPageLine(Long headId, int startPage,
			int pageSize) {
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<ImItemDiscountModLine> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer(
								"from ImItemDiscountModLine as model where 1=1 ");
						if (hId != null)
							hql.append(" and model.imItemDiscountModHead.headId = :headId order by indexNo");
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
	public ImItemDiscountModLine findById(Long lineId) {
		return (ImItemDiscountModLine) findByPrimaryKey(ImItemDiscountModLine.class, lineId);
	}
	
	public ImItemDiscountModLine findByHeadId(Long headId) {
		final Long itemCode1 = headId;
		List<ImItemDiscountModLine> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImItemDiscountModLine as model where 1=1 ");
				hql.append(" and upper(model.imItemDiscountModHead)  = :itemCode1 ");

				Query query = session.createQuery(hql.toString());
				query.setLong("itemCode1", itemCode1);
				return query.list();
			}
		});
		if (result.size() > 0)
			return result.get(0);
		else
			return null;
	}
}
