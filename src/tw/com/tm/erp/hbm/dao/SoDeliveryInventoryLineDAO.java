package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryInventoryHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryInventoryLine;
import tw.com.tm.erp.utils.NumberUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * SoDeliveryInventoryLine entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.dao.SoDeliveryInventoryLine
 * @author MyEclipse Persistence Tools
 */

public class SoDeliveryInventoryLineDAO extends BaseDAO {
	private static final Log log = LogFactory
			.getLog(SoDeliveryInventoryLineDAO.class);
	
	// property constants
	
	public  List<SoDeliveryInventoryLine>  findLineByheadId(Long headId) {
		log.info("findLineByheadId..."+headId);
		
		final Long hId = headId;
		
		List<SoDeliveryInventoryLine> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<SoDeliveryInventoryLine> doInHibernate(Session session) throws HibernateException, SQLException {
				
				StringBuffer hql = new StringBuffer("");
				hql.append("from SoDeliveryInventoryLine as model where 1=1 ");
				hql.append(" and model.headId = :headId ");
				
				Query query = session.createQuery(hql.toString());
				query.setLong("headId", hId);

				return query.list();
			}
		});
		
		log.info("SoDeliveryInventoryLine.form:" + result.size());
		return result;
	}
	
	public SoDeliveryInventoryLine findLineByheadIdAndOrderNo(Long headId, String orderNo){
		log.info("findLineByheadIdAndOrderNo...headId:"+headId);
		log.info("findLineByheadIdAndOrderNo...orderNo:"+orderNo);

		StringBuffer hql = new StringBuffer("");
		hql.append("from SoDeliveryInventoryLine as model where 1=1 ");
		hql.append(" and model.headId = ? and model.deliveryOrderNo = ?");
		
		Object[] objArray =  new Object[] {headId, orderNo};
		
		List<SoDeliveryInventoryLine> result = getHibernateTemplate().find(
			hql.toString(), objArray);
		
		log.info("SoDeliveryInventoryLine.size:" + result.size());
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	
	public List<SoDeliveryInventoryLine> findDelOrUpdLine(Long headId,String modifyType){
		
		StringBuffer hql = new StringBuffer("");
		hql.append("from SoDeliveryInventoryLine as model where 1=1 ");
		hql.append(" and model.headId = ? ");
		
		if ("UPD".equals(modifyType))
			hql.append(" and model.storageCode <> ?");
		else if ("DEL".equals(modifyType))
			hql.append(" and model.storageCodeSys = ?");
		
		Object[] objArray =  new Object[] {headId, ""};
		
		List<SoDeliveryInventoryLine> result = getHibernateTemplate().find(
				hql.toString(), objArray);
			
			log.info("SoDeliveryInventoryLine.size:" + result.size());
			return result;
	}
	
	public Long getMaxIndexNo(Long headId) {
		log.info("getMaxIndexNo..."+headId);
		final Long searchHeadId = headId;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("select MAX(model.indexNo) as indexNo from SoDeliveryInventoryLine as model where 1=1 ");
				hql.append(" and model.headId     = :searchHeadId");
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
			log.info("getMaxIndexNo.IndexNo:" + result.get(0));
			returnResult =(Long)result.get(0);
		}
		return returnResult;
	}
}