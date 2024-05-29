package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import tw.com.tm.erp.hbm.bean.*;

public class PoVerificationSheetDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(PoVerificationSheetDAO.class);
	public int deleteByimReceiveLineId(String lineIds) {
		final String finalLineIds = lineIds;
		int t = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("delete from po_verification_sheet where im_receive_line_id in (").append(finalLineIds).append(")");
				Query query = session.createSQLQuery(hql.toString());
				return query.executeUpdate();
			}
		});
		return t;
	}
	

	public PoVerificationSheet findVerificationSheet(String itemCode ,String imReceiveOrderType ,String imReceiveOrderNo,String poOrderType ,String poOrderNo
	) {
		log.info("DAO1111"+imReceiveOrderType+imReceiveOrderNo+poOrderType+poOrderNo);
		StringBuffer hql = new StringBuffer("from PoVerificationSheet as model where model.itemCode = ?");
		hql.append(" and model.imReceiveOrderType = ?");
		hql.append(" and model.imReceiveOrderNo = ?");
		hql.append(" and model.poOrderType = ?");
		hql.append(" and model.poOrderNo = ?");
		
		List<PoVerificationSheet> result = getHibernateTemplate().find(hql.toString(),
				new Object[] { itemCode, imReceiveOrderType , imReceiveOrderNo ,poOrderType ,poOrderNo});
		log.info("DAO2222"+result.size());
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	public List<PoVerificationSheet> findPoVerificationSheet(String brandCode, String budgetYear  
	) {
    	log.info("listDAO1111"+brandCode+budgetYear);
    	StringBuffer hql = new StringBuffer("from PoVerificationSheet as model where model.brandCode = ?");
    	hql.append(" and model.budgetYear = ?");
    	hql.append(" and model.adjustmentOrderType = null");
    	List<PoVerificationSheet> result = getHibernateTemplate().find(hql.toString(),
		new Object[] { brandCode, budgetYear});
    	log.info("listDAO2222"+result.size());
    	return result;
	}
	public PoVerificationSheet findVerificationSheetAdj(String itemCode ,String imReceiveOrderType ,String imReceiveOrderNo
	) {
		log.info("FindReceive:"+imReceiveOrderType+imReceiveOrderNo);
		StringBuffer hql = new StringBuffer("from PoVerificationSheet as model where model.itemCode = ?");
		hql.append(" and model.imReceiveOrderType = ?");
		hql.append(" and model.imReceiveOrderNo = ?");
		
		
		List<PoVerificationSheet> result = getHibernateTemplate().find(hql.toString(),
				new Object[] { itemCode, imReceiveOrderType , imReceiveOrderNo  });
		log.info("Receive:"+result.size());
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	public List<PoVerificationSheet> findPoVerificationSheetAdjust(String brandCode, String budgetYear  
	) {
    	log.info("listDAO1111"+brandCode+budgetYear);
    	StringBuffer hql = new StringBuffer("from PoVerificationSheet as model where model.brandCode = ?");
    	hql.append(" and model.budgetYear = ?");
    	hql.append(" and model.adjustmentOrderType != null");
    	List<PoVerificationSheet> result = getHibernateTemplate().find(hql.toString(),
		new Object[] { brandCode, budgetYear});
    	log.info("listDAO2222"+result.size());
    	return result;
	}
	public List<PoVerificationSheet> findPoVerificationYearAll(String brandCode, String budgetYear  
	) {
    	log.info("listDAO1111"+brandCode+budgetYear);
    	StringBuffer hql = new StringBuffer("from PoVerificationSheet as model where model.brandCode = ?");
    	hql.append(" and model.budgetYear = ?");
    	//hql.append(" and model.adjustmentOrderType = null");
    	List<PoVerificationSheet> result = getHibernateTemplate().find(hql.toString(),
		new Object[] { brandCode, budgetYear});
    	log.info("listDAO2222"+result.size());
    	return result;
	}
}
