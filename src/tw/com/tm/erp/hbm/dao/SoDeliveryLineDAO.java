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

import tw.com.tm.erp.hbm.bean.SoDeliveryLine;
import tw.com.tm.erp.utils.DateUtils;

public class SoDeliveryLineDAO extends BaseDAO {

    private static final Log log = LogFactory.getLog(SoDeliveryLineDAO.class);
	public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return List<ImMovementItem>
	 */
	public HashMap findPageLine(Long headId, int startPage, int pageSize, String searchType) {
		log.info("findPageLine..."+headId);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String type = searchType;
		final Long hId = headId;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.headId) as rowCount from SoDeliveryLine as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
					hql.append("select model.headId from SoDeliveryLine as model where 1=1 ");
				} else {
					hql.append("from SoDeliveryLine as model where 1=1 ");
				}
				if (hId != null)
					hql.append(" and model.soDeliveryHead.headId = :headId order by indexNo");
				
				
				//hql.append(" and model.soDeliveryHead.headId = :headId order by indexNo");
				
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if (hId != null)
					query.setLong("headId", hId);
				return query.list();
			}
		});
		log.info("SoDeliveryLine.form:" + result.size());
		HashMap returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			log.info("SoDeliveryLine.size:" + result.get(0));
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long
							.valueOf(result.get(0).toString()));
		}
		return returnResult;
	}
	
	public Long findHeadIdByCustomerPoNo(String customerPoNo) {
		log.info("findHeadIdByCustomerPoNo..."+customerPoNo);
		final String searchCustomerPoNo = customerPoNo;
		List<SoDeliveryLine> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("select model.headId from SoDeliveryLine as model where 1=1 ");
				hql.append(" and model.customerPoNo = :searchCustomerPoNo");
				hql.append(" order by model.salesOrderDate desc");
				Query query = session.createQuery(hql.toString());
				query.setString("searchCustomerPoNo", searchCustomerPoNo);
				return query.list();
			}
		});
		log.info("SoDeliveryLine.form:" + result.size());
		Long returnResult = new Long(0);
		if (result.size() == 0) {
			returnResult = 0L;
		} else {
			log.info("SoDeliveryLine.size:" + result.get(0));
			returnResult =result.get(0).getSoDeliveryHead().getHeadId();
		}
		return returnResult;
	}
	
	public Long getMaxIndexNo(Long headId) {
		log.info("getMaxIndexNo..."+headId);
		final Long searchHeadId = headId;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("select MAX(model.indexNo) as indexNo from SoDeliveryLine as model where 1=1 ");
				hql.append(" and model.soDeliveryHead.headId     = :searchHeadId");
				Query query = session.createQuery(hql.toString());
				query.setLong("searchHeadId"    , searchHeadId);
				return query.list();
			}
		});
		log.info("getMaxIndexNo.size:" + result.size());
		Long returnResult = new Long(0);
		if (result.size() == 0) {
			returnResult = 0L;
		} else {
			log.info("SoDeliveryLog.IndexNo:" + result.get(0));
			returnResult =(Long)result.get(0);
		}
		return returnResult;
	}
	
	
    public void updateCustomerPoNo2Void(Long headId, String operaror, String type) {
    	log.info("updateCustomerPoNo2Void..."+headId+"/"+operaror+"/"+type);
		final Long searchHeadId = headId;
		final String searchOperaror = operaror;
		final String opType = type;
    	if (searchHeadId != null && StringUtils.hasText(searchOperaror)){
    		getHibernateTemplate().execute(new HibernateCallback() {
    		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
    		    	StringBuffer hql = new StringBuffer("UPDATE ERP.SO_DELIVERY_LINE");
    				if("AFTER".equalsIgnoreCase(opType))
    					hql.append(" SET CUSTOMER_PO_NO = CUSTOMER_PO_NO||'-'||HEAD_ID");
    				else
    					hql.append(" SET CUSTOMER_PO_NO = SUBSTR(CUSTOMER_PO_NO,1,10)");
    				hql.append(", LAST_UPDATE_DATE = :searchDate");
    				hql.append(", LAST_UPDATED_BY = :searchOperaror");
    				hql.append(" WHERE HEAD_ID = :searchHeadId");
    				Query query = session.createSQLQuery(hql.toString());
    				query.setTimestamp("searchDate" ,  DateUtils.getCurrentDate());
    				query.setString("searchOperaror"  , searchOperaror);
    				query.setLong("searchHeadId"    , searchHeadId);
    				return query.executeUpdate();
    		    	
    		    }
    		});
    	
    	}
    }
	public String getCustomerPoNoString (Long headId) {
		log.info("getCustomerPoNoString..."+headId);
		final Long searchHeadId = headId;
		String customerPoNoString = new String("")	;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("select customerPoNo from SoDeliveryLine as model where 1=1 ");
				hql.append(" and model.soDeliveryHead.headId     = :searchHeadId");
				Query query = session.createQuery(hql.toString());
				query.setLong("searchHeadId"    , searchHeadId);
				return query.list();
			}
		});
		if(null != result){
			for(int i= 0; i < result.size(); i++){
				if(i>0)
					customerPoNoString = customerPoNoString +",";
				customerPoNoString = customerPoNoString + result.get(i).toString();
				
			}
			
		}else{
			customerPoNoString = "";
		}
		return customerPoNoString;
	}
}