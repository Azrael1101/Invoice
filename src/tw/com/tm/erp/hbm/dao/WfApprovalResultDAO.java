package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.WfApprovalResult;

/**
 * A data access object (DAO) providing persistence and search support for
 * WfApprovalResult entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.WfApprovalResult
 * @author MyEclipse Persistence Tools
 */

public class WfApprovalResultDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(WfApprovalResultDAO.class);
	// property constants
	public static final String PROCESS_ID = "processId";
	public static final String ORDER_TYPE_CODE = "orderTypeCode";
	public static final String ORDER_NO = "orderNo";
	public static final String FORM_NAME = "formName";
	public static final String NODE_NAME = "nodeName";
	public static final String ACTIVITY_ID = "activityId";
	public static final String APPROVER = "approver";
	public static final String RESULT = "result";
	public static final String APPROVAL_TYPE = "approvalType";
	public static final String APPROVAL_COMMENT = "approvalComment";
	public static final String RESERVE1 = "reserve1";
	public static final String RESERVE2 = "reserve2";
	public static final String RESERVE3 = "reserve3";
	public static final String RESERVE4 = "reserve4";
	public static final String RESERVE5 = "reserve5";

	protected void initDao() {
		// do nothing
	}

	public void save(WfApprovalResult transientInstance) {
		log.debug("saving WfApprovalResult instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(WfApprovalResult persistentInstance) {
		log.debug("deleting WfApprovalResult instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public WfApprovalResult findById(java.lang.Long id) {
		log.debug("getting WfApprovalResult instance with id: " + id);
		try {
			WfApprovalResult instance = (WfApprovalResult) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.WfApprovalResult", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(WfApprovalResult instance) {
		log.debug("finding WfApprovalResult instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding WfApprovalResult instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from WfApprovalResult as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByProcessId(Object processId) {
		return findByProperty(PROCESS_ID, processId);
	}

	public List findByOrderType(Object orderTypeCode) {
		return findByProperty(ORDER_TYPE_CODE, orderTypeCode);
	}

	public List findByOrderNo(Object orderNo) {
		return findByProperty(ORDER_NO, orderNo);
	}

	public List findByFormName(Object formName) {
		return findByProperty(FORM_NAME, formName);
	}

	public List findByNodeName(Object nodeName) {
		return findByProperty(NODE_NAME, nodeName);
	}

	public List findByActivityId(Object activityId) {
		return findByProperty(ACTIVITY_ID, activityId);
	}

	public List findByApprover(Object approver) {
		return findByProperty(APPROVER, approver);
	}

	public List findByResult(Object result) {
		return findByProperty(RESULT, result);
	}

	public List findByApprovalType(Object approvalType) {
		return findByProperty(APPROVAL_TYPE, approvalType);
	}

	public List findByApprovalComment(Object approvalComment) {
		return findByProperty(APPROVAL_COMMENT, approvalComment);
	}

	public List findByReserve1(Object reserve1) {
		return findByProperty(RESERVE1, reserve1);
	}

	public List findByReserve2(Object reserve2) {
		return findByProperty(RESERVE2, reserve2);
	}

	public List findByReserve3(Object reserve3) {
		return findByProperty(RESERVE3, reserve3);
	}

	public List findByReserve4(Object reserve4) {
		return findByProperty(RESERVE4, reserve4);
	}

	public List findByReserve5(Object reserve5) {
		return findByProperty(RESERVE5, reserve5);
	}

	public List findAll() {
		log.debug("finding all WfApprovalResult instances");
		try {
			String queryString = "from WfApprovalResult";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public WfApprovalResult merge(WfApprovalResult detachedInstance) {
		log.debug("merging WfApprovalResult instance");
		try {
			WfApprovalResult result = (WfApprovalResult) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(WfApprovalResult instance) {
		log.debug("attaching dirty WfApprovalResult instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(WfApprovalResult instance) {
		log.debug("attaching clean WfApprovalResult instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static WfApprovalResultDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (WfApprovalResultDAO) ctx.getBean("wfApprovalResultDAO");
	}
	
	public List<WfApprovalResult> findApprovalResultByOrderTypeNo(
			String brandCode, String orderType, String orderNo) {

		try {
			StringBuffer hql = new StringBuffer(
					"from WfApprovalResult as model ");
			hql.append("where model.brandCode = ? ");
			hql.append("and model.orderTypeCode = ? ");
			hql.append("and model.orderNo = ? ");
			hql.append("order by id ");
			return getHibernateTemplate().find(
					hql.toString(),
					new String[] { brandCode, orderType, orderNo });
		} catch (Exception e) {
			return null;
		}

	}
	
	public Long getProcessIdByOrderTypeNo(String brandCode, String orderType,
			String orderNo) {

		try {
			StringBuffer hql = new StringBuffer(
					"from WfApprovalResult as model ");
			hql.append("where model.brandCode = ? ");
			hql.append("and model.orderTypeCode = ? ");
			hql.append("and model.orderNo = ? ");
			hql.append("order by id desc");
			List<WfApprovalResult> lists = getHibernateTemplate().find(hql.toString(),
							new String[] { brandCode, orderType, orderNo });
			if (lists == null) {
				return null;
			} else {
				return lists.get(0).getProcessId();
			}
		} catch (Exception e) {
			return null;
		}

	}

	public List findApproverMail(final Long processId) {

		List re = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						
						
						StringBuffer hql = new StringBuffer(
								"Select address.EMail as EMail ");						
						hql.append("from WfApprovalResult as result, " );
						hql.append("BuEmployeeWithAddressView as address where 1=1 ");		
						hql.append("and result.processId = :processId ");
						hql.append("and result.approver = address.employeeCode ");
						hql.append("and address.EMail != null ");
						//hql.append("and address.EMail != '' ");
						hql.append("group by address.EMail ");
					
						Query query = session.createQuery(hql.toString());
						query.setParameter("processId", processId);
						
						return query.list();
					}
				});

		return re;
	}
	
    public HashMap findPageLine(final String brandCode, final String orderTypeCode, final String orderNo, int startPage, int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		List<WfApprovalResult> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from WfApprovalResult as model ");
				
						hql.append("where model.brandCode = :brandCode ");
						hql.append("and model.orderTypeCode = :orderTypeCode ");
						hql.append("and model.orderNo = :orderNo ");
						hql.append("order by id ");
						Query query = session.createQuery(hql.toString());
						if( startRecordIndexStar >= 0 ) {
							query.setFirstResult(startRecordIndexStar);
							query.setMaxResults(pSize);
							System.out.println("startRecordIndexStar:"+startRecordIndexStar);
							System.out.println("pSize:"+pSize);
						}
					
						query.setString("brandCode", brandCode);
						query.setString("orderTypeCode", orderTypeCode);
						query.setString("orderNo", orderNo);
						return query.list();
					}
				});

		log.info("WfApprovalResult.size:" + result.size());
		HashMap returnResult = new HashMap();		
		returnResult.put("form", startRecordIndexStar >= 0 ? result: null);			
		returnResult.put("recordCount", Long.valueOf(result.size()));
		return returnResult;
	}
    
    public HashMap findPageLine(final Long processId, int startPage, int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		List<WfApprovalResult> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from WfApprovalResult as model ");
				
						hql.append("where model.processId = :processId ");
						hql.append("order by id ");
						Query query = session.createQuery(hql.toString());
						if( startRecordIndexStar > 0 ) {
							query.setFirstResult(startRecordIndexStar);
							query.setMaxResults(pSize);
							System.out.println("startRecordIndexStar:"+startRecordIndexStar);
							System.out.println("pSize:"+pSize);
						}
					
						query.setLong("processId", processId);
						return query.list();
					}
				});

		log.info("WfApprovalResult.size:" + result.size());
		HashMap returnResult = new HashMap();		
		returnResult.put("form", startRecordIndexStar >= 0 ? result: null);			
		returnResult.put("recordCount", Long.valueOf(result.size()));
		return returnResult;
	}
}