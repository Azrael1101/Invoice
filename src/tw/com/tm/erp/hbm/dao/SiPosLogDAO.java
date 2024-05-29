package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.SiPosLog;
import tw.com.tm.erp.hbm.bean.SiPosLogId;

public class SiPosLogDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(SiPosLogDAO.class);

	/**
	 * 依據專櫃代號、交易日期、過帳狀態進行查詢
	 * 
	 * @param shopCode
	 * @param transactionDate
	 * @param lastTransactionDateString
	 * @param isPosting
	 * @return List
	 */
	public List<SiPosLog> findPosLogByProperty(final String brandCode, final String shopCode, final Date transactionDate,
			final Date lastTransactionDate, final String isPosting) {

		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer(
						"from SiPosLog as model where model.brandCode = :brandCode and model.id.transactionDate <= :lastTransactionDate");
				hql.append(" and model.isPosting = :isPosting");
				if (StringUtils.hasText(shopCode)) {
					hql.append(" and model.id.shopCode = :shopCode");
				}

				if (transactionDate != null) {
					hql.append(" and model.id.transactionDate >= :transactionDate");
				}
				hql.append(" order by model.id.transactionDate, model.id.shopCode");
				Query query = session.createQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setDate("lastTransactionDate", lastTransactionDate);
				query.setString("isPosting", isPosting);
				if (StringUtils.hasText(shopCode)) {
					query.setString("shopCode", shopCode);
				}

				if (transactionDate != null) {
					query.setDate("transactionDate", transactionDate);
				}

				return query.list();
			}
		});

		return result;
	}

	public SiPosLog findById(SiPosLogId siPosLogId) {
		if (null != siPosLogId)
			return (SiPosLog) findByPrimaryKey(SiPosLog.class, siPosLogId);
		return null;
	}
}