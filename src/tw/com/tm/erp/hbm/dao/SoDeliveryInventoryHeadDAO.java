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
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.ImInventoryCountsHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryInventoryHead;
import tw.com.tm.erp.utils.DateUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * SoDeliveryInventoryHead entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.dao.SoDeliveryInventoryHead
 * @author MyEclipse Persistence Tools
 */

public class SoDeliveryInventoryHeadDAO extends BaseDAO {
	private static final Log log = LogFactory
			.getLog(SoDeliveryInventoryHeadDAO.class);
	// property constants
	
	public SoDeliveryInventoryHead findById(java.lang.Long id) {
		log.debug("getting SoDeliveryInventoryHead instance with id: " + id);
		try {
			SoDeliveryInventoryHead instance = (SoDeliveryInventoryHead) getSession()
					.get("tw.com.tm.erp.hbm.bean.SoDeliveryInventoryHead", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public SoDeliveryInventoryHead findInventoryByCountsId(String countsId, String orderNo) {
		log.info("countsId: " + countsId);
		log.info("orderNo: " + orderNo);
		StringBuffer hql = new StringBuffer(
		"from SoDeliveryInventoryHead as model where model.countsId = ?");
		hql.append(" and model.orderTypeCode = 'DZC' ");
		hql.append(" and model.orderNo not like 'TMP%' ");
		if(StringUtils.hasText(orderNo)){
		    hql.append(" and model.orderNo != ?");
		}
		
		Object[] objArray = null;
        if (StringUtils.hasText(orderNo)) {
            objArray = new Object[] {countsId, orderNo};
        }else{
            objArray = new Object[] {countsId};
        }
		List<SoDeliveryInventoryHead> result = getHibernateTemplate().find(
			hql.toString(), objArray);
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
}