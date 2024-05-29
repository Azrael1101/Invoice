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
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.ImPriceAdjustment;
import tw.com.tm.erp.hbm.bean.SiResend;
import tw.com.tm.erp.utils.DateUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImPriceAdjustment entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImPriceAdjustment
 * @author MyEclipse Persistence Tools
 */

public class ImPriceAdjustmentDAO extends BaseDAO {
	private static final Log log = LogFactory
			.getLog(ImPriceAdjustmentDAO.class);
	// property constants
	public static final String BRAND_CODE = "brandCode";
	public static final String ORDER_TYPE_CODE = "orderTypeCode";
	public static final String ORDER_NO = "orderNo";
	public static final String DESCRIPTION = "description";
	public static final String STATUS = "status";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(ImPriceAdjustment transientInstance) {
		log.debug("saving ImPriceAdjustment instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	public boolean update(ImPriceAdjustment transientInstance) {
		boolean result = false;
		try {
			getHibernateTemplate().update(transientInstance);
			result = true;
		} catch (RuntimeException re) {
			//throw re;
			result = false;
		}
		return result;
	}
	public void delete(ImPriceAdjustment persistentInstance) {
		log.debug("deleting ImPriceAdjustment instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImPriceAdjustment findById(java.lang.Long id) {
		log.debug("getting ImPriceAdjustment instance with id: " + id);
		try {
			ImPriceAdjustment instance = (ImPriceAdjustment) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.ImPriceAdjustment", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImPriceAdjustment instance) {
		log.debug("finding ImPriceAdjustment instance by example");
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
		log.debug("finding ImPriceAdjustment instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ImPriceAdjustment as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByBrandCode(Object brandCode) {
		return findByProperty(BRAND_CODE, brandCode);
	}

	public List findByOrderTypeCode(Object orderTypeCode) {
		return findByProperty(ORDER_TYPE_CODE, orderTypeCode);
	}

	public List findByOrderNo(Object orderNo) {
		return findByProperty(ORDER_NO, orderNo);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findAll() {
		log.debug("finding all ImPriceAdjustment instances");
		try {
			String queryString = "from ImPriceAdjustment";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImPriceAdjustment merge(ImPriceAdjustment detachedInstance) {
		log.debug("merging ImPriceAdjustment instance");
		try {
			ImPriceAdjustment result = (ImPriceAdjustment) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImPriceAdjustment instance) {
		log.debug("attaching dirty ImPriceAdjustment instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImPriceAdjustment instance) {
		log.debug("attaching clean ImPriceAdjustment instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImPriceAdjustmentDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ImPriceAdjustmentDAO) ctx.getBean("imPriceAdjustmentDAO");
	}
	/*
	public List<ImPriceAdjustment> findPriceAdjustmentByValue(String brandCode,
			String orderType, String startOrderNo, String endOrderNo,
			String startDate, String endDate, String status,
			String employeeCode, String categorySearchString) {
		try {

			StringBuffer hql = new StringBuffer(
					"from ImPriceAdjustment as model ");
			hql.append("where model.brandCode = '" + brandCode + "' ");
			hql.append("and model.orderTypeCode = '" + orderType + "' ");
			// 判斷針對OrderNo欄位是否要用模糊查詢(LIKE)的進行Query
			// 當startOrderNo或endOrderNo有一參數為空白或Null時，即使用模糊查詢
			if ((startOrderNo == null || "".equals(startOrderNo))
					|| (endOrderNo == null || "".equals(endOrderNo))) {
				hql
						.append((startOrderNo != null
								&& !"".equals(startOrderNo) ? "and model.orderNo LIKE '%"
								+ startOrderNo + "%' "
								: ""));
				hql
						.append((endOrderNo != null && !"".equals(endOrderNo) ? "and model.orderNo LIKE '%"
								+ endOrderNo + "%' "
								: ""));
			} else {
				hql
						.append((startOrderNo != null
								&& !"".equals(startOrderNo) ? "and model.orderNo >= '"
								+ startOrderNo + "' "
								: ""));
				hql
						.append((endOrderNo != null && !"".equals(endOrderNo) ? "and model.orderNo <= '"
								+ endOrderNo + "' "
								: ""));
			}

			// ----更新日期
			hql.append((startDate != null && !"".equals(startDate) ? "and model.lastUpdateDate between "
							+ "TO_DATE('"
							+ startDate
							+ "', 'YYYY/MM/DD') and "
							+ "TO_DATE('" + endDate + "', 'YYYY/MM/DD') "
							: ""));
			
			hql.append((startDate != null && !"".equals(startDate) ? "and model.lastUpdateDate >= "
					: ""));
			// 員工代號
			hql.append((employeeCode != null && !("".equals(employeeCode)) ? "and model.lastUpdatedBy = '"
							+ employeeCode + "' ": ""));
			// 狀態
			hql
					.append((status != null && !"".equals(status) ? "and model.status = '"
							+ status + "' "
							: ""));

			// ----類別組合字串
			hql
					.append((categorySearchString != null
							&& !"".equals(categorySearchString.trim()) ? categorySearchString
							: ""));

			System.out.println(hql.toString());

			return getHibernateTemplate().find(hql.toString());

		} catch (RuntimeException re) {
			throw re;
		}
	}
	*/
	public  List<ImPriceAdjustment>  findPriceAdjustmentByValue(HashMap findObjs) {
		final HashMap fos = findObjs ; 
		List<ImPriceAdjustment> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer(
					"from ImPriceAdjustment as model where 1=1 ");
				
				if( StringUtils.hasText((String)fos.get("brandCode")) )
					hql.append(" and model.brandCode = :brandCode ");
				
				if( StringUtils.hasText((String)fos.get("orderTypeCode")) )
					hql.append(" and model.orderTypeCode = :orderTypeCode ");
				
				if (StringUtils.hasText((String)fos.get("startOrderNo"))) 
					hql.append(" and model.orderNo >= :startOrderNo");									
				
				if (StringUtils.hasText((String)fos.get("endOrderNo"))) 
					hql.append(" and model.orderNo < :endOrderNo");	
				
				// 狀態
				if (StringUtils.hasText((String)fos.get("status"))) 
					hql.append(" and model.status = :status");	
				
				// 員工代號
				if (StringUtils.hasText((String)fos.get("employeeCode"))) 
					hql.append(" and model.lastUpdatedBy = :employeeCode");	
				 
				// ----更新日期

				if ((java.util.Date)fos.get("startDate")!= null) 
					hql.append(" and model.lastUpdateDate >= :startDate");	
				
				if ((java.util.Date)fos.get("endDate")!= null) 
					hql.append(" and model.lastUpdateDate <= :endDate");	
				
				hql.append(" ORDER BY model.orderTypeCode, model.orderNo DESC");	
				// ----類別組合字串
				if (StringUtils.hasText((String)fos.get("categorySearchString"))) 
					hql.append(":categorySearchString");	
				
				Query query = session.createQuery(hql.toString());
				if( StringUtils.hasText((String)fos.get("brandCode")) )
					query.setString("brandCode", (String)fos.get("brandCode") );
				
				if( StringUtils.hasText((String)fos.get("orderTypeCode")) )
					query.setString("orderTypeCode", (String)fos.get("orderTypeCode") );
				
				if (StringUtils.hasText((String)fos.get("startOrderNo"))) 
					query.setString("startOrderNo", (String)fos.get("startOrderNo") );								
				
				if (StringUtils.hasText((String)fos.get("endOrderNo"))) 
					query.setString("endOrderNo", (String)fos.get("endOrderNo") );
				
				// 狀態
				if (StringUtils.hasText((String)fos.get("status"))) 
					query.setString("status", (String)fos.get("status") );	
				
				// 員工代號
				if (StringUtils.hasText((String)fos.get("employeeCode"))) 
					query.setString("employeeCode", (String)fos.get("employeeCode") );	
				 
				// ----更新日期
				
				if ((java.util.Date)fos.get("startDate")!= null) {		
					query.setDate("startDate", DateUtils.parseDateTime( DateUtils.format((java.util.Date)fos.get("startDate"))+" 00:00:00") );	
				}	
				if ((java.util.Date)fos.get("endDate")!= null){
					query.setDate("endDate", DateUtils.addDate(DateUtils.format((java.util.Date)fos.get("endDate"))) );
				}
				// ----類別組合字串
				if (StringUtils.hasText((String)fos.get("categorySearchString"))) 
					query.setString("categorySearchString", (String)fos.get("categorySearchString") );

				return query.list();

			}
		});

		return re;		
	}
	 public HashMap findPageLine(HashMap findObjs, int startPage,  int pageSize) {
		final HashMap fos = findObjs;
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		System.out.println("start to find imPriceAdjustment....");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)throws HibernateException, SQLException {
						Date startDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startDate") );
						Date endDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("endDate") );
						StringBuffer hql = new StringBuffer("");
						
						System.out.println("has employeeCode = " + StringUtils.hasText((String) fos.get("employeeCode")));
						
						if( startRecordIndexStar >= 0 ) {
							hql.append("from ImPriceAdjustment as model where 1=1 ");
						}else{
							hql.append("select count(model.headId) as rowCount from ImPriceAdjustment as model where 1=1 ");
						}		
						if (StringUtils.hasText((String) fos.get("brandCode")))
							hql.append(" and model.brandCode = :brandCode ");

						if (StringUtils.hasText((String) fos.get("orderTypeCode")))
							hql.append(" and model.orderTypeCode = :orderTypeCode ");
						
					
							hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");
						
						if (StringUtils.hasText((String) fos.get("startOrderNo")))
							hql.append(" and model.orderNo >= :startOrderNo ");

						if (StringUtils.hasText((String) fos.get("endOrderNo")))
							hql.append(" and model.orderNo <= :endOrderNo ");


						if (null != startDate)
							hql.append(" and model.enableDate >= :startDate ");

						if (null !=  endDate)
							hql.append(" and model.enableDate <= :endDate ");

						if (StringUtils.hasText((String) fos.get("employeeCode")))
							hql.append(" and model.createdBy = :employeeCode ");
						
						if (StringUtils.hasText((String) fos.get("supplierCode")))
							hql.append(" and model.supplierCode = :supplierCode ");
						
						if (StringUtils.hasText((String) fos.get("status")))
							hql.append(" and model.status = :status ");

						if (StringUtils.hasText((String) fos.get("itemCode")))
							hql.append(" and model.headId IN(SELECT item.imPriceAdjustment FROM ImPriceList as item WHERE item.itemCode=:itemCode)");

						hql.append(" order by lastUpdateDate desc ");
						
						System.out.println(hql.toString());
						Query query = session.createQuery(hql.toString());
						if( startRecordIndexStar >= 0 ) {
							query.setFirstResult(startRecordIndexStar);
							query.setMaxResults(pSize);
							System.out.println("startRecordIndexStar:"+startRecordIndexStar);
							System.out.println("pSize:"+pSize);
						}
						
						if (StringUtils.hasText((String) fos.get("itemCode")))
							query.setString("itemCode", (String) fos.get("itemCode"));
						
							
						if (StringUtils.hasText("brandCode"))
							query.setParameter("brandCode", fos.get("brandCode"));
						
						
						if (StringUtils.hasText((String) fos.get("orderTypeCode")))
							query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));
					
						
						if (StringUtils.hasText((String) fos.get("startOrderNo")))
							query.setString("startOrderNo", (String) fos.get("startOrderNo"));
						
						if (StringUtils.hasText((String) fos.get("supplierCode")))
							query.setString("supplierCode", (String) fos.get("supplierCode"));
						
						if (StringUtils.hasText((String) fos.get("endOrderNo")))
							query.setString("endOrderNo", (String) fos.get("endOrderNo"));
						
						if (StringUtils.hasText((String) fos.get("itemCode")))
							query.setString("itemCode", (String) fos.get("itemCode"));
						
						if (null != startDate)
							query.setDate("startDate", startDate);

						if (null !=  endDate)
							query.setDate("endDate", endDate);

						if (StringUtils.hasText((String) fos.get("employeeCode")))
							query.setString("employeeCode", (String) fos.get("employeeCode"));
						
						if (StringUtils.hasText((String) fos.get("status")))
							query.setString("status", (String) fos.get("status"));
						
						return query.list();
					}
				});
			
			log.info("imPriceAdjustment.form:"+result.size());
			HashMap returnResult = new HashMap();		
			returnResult.put("form", startRecordIndexStar >= 0 ? result: null);			
			if(result.size() == 0){
				returnResult.put("recordCount", 0L);
			}else{
				log.info("imPriceAdjustment.size:"+result.get(0));
				returnResult.put("recordCount",startRecordIndexStar >= 0? result.size() : Long.valueOf(result.get(0).toString()));
			}
			return returnResult;
	}
	public List<ImPriceAdjustment> findPriceAdjustmentPAJ(){
		String orderTypeCode = "PAJ";
		String status = "FINISH";		
		Date date = new Date();
		String enableDate = DateUtils.format(date, DateUtils.C_DATA_PATTON_YYYYMMDD);				
		
		StringBuffer hql = new StringBuffer(
			"from ImPriceAdjustment as model where 1=1 ");
		// 單別
			hql.append(" and model.orderTypeCode = ? ");
		// 狀態
			hql.append(" and model.status = ? ");	
		// 啟用日期
			hql.append(" and model.enableDate = to_date( ?,'yyyymmdd') ");
			
		Object[] objArray = null;
		objArray = new Object[] { orderTypeCode, status, enableDate};
	    List<ImPriceAdjustment> result = getHibernateTemplate().find(hql.toString(), objArray);				
	
		return result;
	}
}