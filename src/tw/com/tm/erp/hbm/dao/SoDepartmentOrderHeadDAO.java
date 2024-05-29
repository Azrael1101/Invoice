package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.Sequence;
import tw.com.tm.erp.hbm.bean.SoDepartmentOrderHead;
import tw.com.tm.erp.hbm.bean.SoDepartmentOrderItem;
import tw.com.tm.erp.hbm.bean.SoReceiptHead;
import tw.com.tm.erp.hbm.bean.SoReceiptLine;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderLine;
import tw.com.tm.erp.hbm.service.BuBasicDataService;

public class SoDepartmentOrderHeadDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(SoDepartmentOrderHeadDAO.class);

	BuBasicDataService buBasicDataService;

	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}

	public void save(Object saveObj) {
		// getHibernateTemplate().persist(saveObj);
		getHibernateTemplate().save(saveObj);
	}
	public void update(Object updateObj) {
		// getHibernateTemplate().merge(updateObj);
		getHibernateTemplate().update(updateObj);
	}

	public SoDepartmentOrderHead findById(Long id) {
		log.debug("getting SoDepartmentOrderHead instance with id: " + id);
		try {
			SoDepartmentOrderHead instance = (SoDepartmentOrderHead) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.SoDepartmentOrderHead", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
    public SoDepartmentOrderItem findByIndexNo(Long headId ,Long indexNo) {

        StringBuffer hql = new StringBuffer("from SoDepartmentOrderItem as model ");
        hql.append("where model.soDepartmentOrderHead.headId = ? ");
        hql.append(" and model.indexNo = ? ");
        List<SoDepartmentOrderItem> lists = getHibernateTemplate().find(hql.toString(),
                new Object[] { headId,indexNo });
        return (lists != null && lists.size() > 0 ? lists.get(0) : null);
    }
    
	public List<SoDepartmentOrderItem> findPageLine(Long headId, int startPage,int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<SoDepartmentOrderItem> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from SoDepartmentOrderItem as model where 1=1 ");
						if (hId != null)
							hql.append(" and model.soDepartmentOrderHead.headId = :headId order by indexNo");
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
	
	public List<SoSalesOrderItem> findT2PageLine(Long headId, int startPage,int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<SoSalesOrderItem> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from SoSalesOrderItem as model where 1=1 ");
						if (hId != null)
							hql.append(" and model.soSalesOrderHead.headId = :headId order by indexNo");
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
	
	public Long findPageLineMaxIndex(Long headId) {

		Long lineMaxIndex = new Long(0);
		final Long hId = headId;
		List<SoDepartmentOrderHead> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from SoDepartmentOrderHead as model where 1=1 ");
						if (hId != null)
							hql.append(" and model.headId = :headId");
						Query query = session.createQuery(hql.toString());
						if (hId != null)
							query.setLong("headId", hId);
						return query.list();
					}
				});
		if (result != null && result.size() > 0) {
			List<SoDepartmentOrderItem> soDepartmentOrderItems = result.get(0).getSoDepartmentOrderItem();		    
			if (soDepartmentOrderItems != null && soDepartmentOrderItems.size() > 0){
				lineMaxIndex = soDepartmentOrderItems.get(soDepartmentOrderItems.size() - 1).getIndexNo();	
			}
		}
		return lineMaxIndex;
	}
	
	
	public Long findT2PageLineMaxIndex(Long headId) {

		Long lineMaxIndex = new Long(0);
		final Long hId = headId;
		List<SoSalesOrderHead> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from SoSalesOrderHead as model where 1=1 ");
						if (hId != null)
							hql.append(" and model.headId = :headId");
						Query query = session.createQuery(hql.toString());
						if (hId != null)
							query.setLong("headId", hId);
						return query.list();
					}
				});
		if (result != null && result.size() > 0) {
			List<SoSalesOrderItem> soSalesOrderItems = result.get(0).getSoSalesOrderItems();
			if (soSalesOrderItems != null && soSalesOrderItems.size() > 0){
				lineMaxIndex = soSalesOrderItems.get(soSalesOrderItems.size() - 1).getIndexNo();	
			}
		}
		return lineMaxIndex;
	}
	
	public List<SoSalesOrderHead> findByCustomerPoNo(String brandCode,
			String customerPoNo) {
		StringBuffer hql = new StringBuffer(
				"from SoSalesOrderHead as model where model.brandCode = ?");
		hql.append(" and model.customerPoNo = ?");
		Object[] objArray = null;
		objArray = new Object[] { brandCode, customerPoNo };
		List<SoSalesOrderHead> result = getHibernateTemplate().find(
				hql.toString(), objArray);

		return result;
	}
	
	public Sequence findSeqByName(String name) {
		StringBuffer hql = new StringBuffer(
				"from Sequence as model where model.name = ?");
		Object[] objArray = null;
		objArray = new Object[] { name };
		List<Sequence> result = getHibernateTemplate().find(
				hql.toString(), objArray);

		return result.get(0);
	}
	
	public SoReceiptHead findReceiptHeadByYearMonth(String brandCode,String receiptYear ,String receiptMonth,String receiptType) {
		StringBuffer hql = new StringBuffer(
				" from SoReceiptHead as model where model.brandCode = ? ");
		hql.append(" and model.receiptYear = ? ");
		hql.append(" and model.receiptMonth = ? ");
		hql.append(" and model.receiptType = ? ");
		Object[] objArray = null;
		objArray = new Object[] { brandCode , receiptYear , receiptMonth , receiptType };
		List<SoReceiptHead> result = getHibernateTemplate().find(
				hql.toString(), objArray);

		return result.get(0);
	}
	
	public SoReceiptLine findReceiptLineByh(Long headId) {
		StringBuffer hql = new StringBuffer(
				" from SoReceiptLine as model where model.soReceiptHead.headId = ? ");
		Object[] objArray = null;
		objArray = new Object[] { headId };
		List<SoReceiptLine> result = getHibernateTemplate().find(
				hql.toString(), objArray);

		return result.get(0);
	}

	/**
	 * 依照 BuEmployeeWithAddressView 工號, 英文名子, 中文名字, 尋找 employee
	 * 
	 * @param findObj
	 * @return
	 */
	public List<SoSalesOrderHead> findByCustomerPoNo(HashMap findObjs) {
		final HashMap fos = findObjs;
		List<SoSalesOrderHead> re = getHibernateTemplate().executeFind(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer(
								"select model from SoSalesOrderHead as model, SoSalesOrderHead as model2 ");
						hql.append(" where model.headId = model2.headId");

						log.info("customerPoNo:"
								+ StringUtils.hasText((String) fos
										.get("customerPoNo")));
						if (StringUtils.hasText((String) fos
								.get("customerPoNo")))
							hql.append(" and model.customerPoNo ='").append(
									(String) fos.get("customerPoNo")).append(
									"'");
						log.info(hql.toString());
						Query query = session.createQuery(hql.toString());
						// query.setFirstResult(0);
						// query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
						return query.list();
					}
				});
		return re;
	}
	
	public SoDepartmentOrderItem findLineById(java.lang.Long id) {
		log.debug("getting SoDepartmentOrderItem instance with id: " + id);
		try {
			SoDepartmentOrderItem instance = (SoDepartmentOrderItem) getHibernateTemplate().get("tw.com.tm.erp.hbm.bean.SoDepartmentOrderItem", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public SoSalesOrderItem findT2LineById(java.lang.Long id) {
		log.debug("getting SoSalesOrderItem instance with id: " + id);
		try {
			SoSalesOrderItem instance = (SoSalesOrderItem) getHibernateTemplate().get("tw.com.tm.erp.hbm.bean.SoSalesOrderItem", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}