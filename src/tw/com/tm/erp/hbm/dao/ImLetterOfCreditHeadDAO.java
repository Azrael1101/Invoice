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
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.ImLetterOfCreditHead;

public class ImLetterOfCreditHeadDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(ImLetterOfCreditHeadDAO.class);
    
public List<ImLetterOfCreditHead> findLetterOfCreditList(HashMap conditionMap) {
	
	final String brandCode 		= (String) conditionMap.get("brandCode");
	final String lcNo_Start 	= (String) conditionMap.get("lcNo_Start");
	final String lcNo_End 		= (String) conditionMap.get("lcNo_End");
	final Date lcDate_Start 	= (Date) conditionMap.get("lcDate_Start");
	final Date lcDate_End 		= (Date) conditionMap.get("lcDate_End");	
	final String status_Start 	= (String) conditionMap.get("status_Start");
	final String status_End 	= (String) conditionMap.get("status_End");
	final String supplierCode 	= (String) conditionMap.get("supplierCode");	
	final String poNo_Start 	= (String) conditionMap.get("poNo_Start");
	final String poNo_End 		= (String) conditionMap.get("poNo_End");	
	final String openingBank 	= (String) conditionMap.get("openingBank");
	
	List<ImLetterOfCreditHead> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer(
				"from ImLetterOfCreditHead as model where model.brandCode = :brandCode");
			
			if (StringUtils.hasText(lcNo_Start))
			    hql.append(" and model.lcNo >= :lcNo_Start");
			
			if (StringUtils.hasText(lcNo_End))
			    hql.append(" and model.lcNo <= :lcNo_End");
			
			if (StringUtils.hasText(status_Start))
			    hql.append(" and ( model.status = :status_Start");
			
			if (StringUtils.hasText(status_End))
			    hql.append(" or model.status = :status_End )");
			
			if (StringUtils.hasText(supplierCode))
			    hql.append(" and model.supplierCode = :supplierCode");
			
			if (StringUtils.hasText(poNo_Start))
			    hql.append(" and model.poNo >= :poNo_Start");
			
			if (StringUtils.hasText(poNo_End))
			    hql.append(" and model.poNo <= :poNo_End");

			if (StringUtils.hasText(openingBank))
			    hql.append(" and model.openingBank = :openingBank");
					
			if (lcDate_Start != null)
			    hql.append(" and model.lcDate >= :lcDate_Start");
			
			if (lcDate_End != null)
			    hql.append(" and model.lcDate <= :lcDate_End");

			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			query.setString("brandCode", brandCode);
			
			if (StringUtils.hasText(lcNo_Start))
			    query.setString("lcNo_Start", lcNo_Start);
			
			if (StringUtils.hasText(lcNo_End))
			    query.setString("lcNo_End", lcNo_End);
			
			if (StringUtils.hasText(status_Start))
			    query.setString("status_Start", status_Start);
			
			if (StringUtils.hasText(status_End))
			    query.setString("status_End", status_End);
			
			if (StringUtils.hasText(supplierCode))
			    query.setString("supplierCode", supplierCode);
			
			if (StringUtils.hasText(poNo_Start))
			    query.setString("poNo_Start", poNo_Start);
			
			if (StringUtils.hasText(poNo_End))
			    query.setString("poNo_End", poNo_End);

			if (StringUtils.hasText(openingBank))
			    query.setString("openingBank", openingBank);
					
			if (lcDate_Start != null)
			    query.setDate("lcDate_Start", lcDate_Start);
			
			if (lcDate_End != null)
			    query.setDate("lcDate_End", lcDate_End);

			return query.list();
		    }
		});

	return result;	
    }

}