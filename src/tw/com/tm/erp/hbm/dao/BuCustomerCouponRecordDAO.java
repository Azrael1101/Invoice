package tw.com.tm.erp.hbm.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuCustomerCouponRecord;


public class BuCustomerCouponRecordDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BuCustomerCouponRecordDAO.class);
	
	public List<BuCustomerCouponRecord> findByCustomerCodeAndYear(String customerCode, String beginYear, String endYear, String type){
		StringBuffer hql = new StringBuffer("");
		hql.append("from BuCustomerCouponRecord as model where 1=1 ");
		hql.append(" and model.customerCode = ? ");
//		hql.append(" and to_char(model.useDate,'YYYY') = ? ");
		hql.append(" and model.useYear >= ? ");
		hql.append(" and model.useYear <= ? ");
		hql.append(" and model.type = ? ");
		hql.append(" order by model.useYear desc");
		
		Object[] objArray =  new Object[] {customerCode, beginYear, endYear,  type};
		
		List<BuCustomerCouponRecord> result = getHibernateTemplate().find(
			hql.toString(), objArray);
		
		//log.info("BuCurrency.size:" + result.size());
		
//		return (result != null && result.size() > 0 ? result.get(0) : null);
		return result;
	}
	
	public BuCustomerCouponRecord findByCustomerCodeAndYear(String customerCode, String useYear, String type){
		StringBuffer hql = new StringBuffer("");
		hql.append("from BuCustomerCouponRecord as model where 1=1 ");
		hql.append(" and model.status <> 'VOID' ");
		hql.append(" and model.customerCode = ? ");
//		hql.append(" and to_char(model.useDate,'YYYY') = ? ");
		hql.append(" and model.useYear = ? ");
		hql.append(" and model.type = ? ");
		hql.append(" order by model.useYear desc");
		
		Object[] objArray =  new Object[] {customerCode, useYear, type};
		
		List<BuCustomerCouponRecord> result = getHibernateTemplate().find(
			hql.toString(), objArray);
		
		//log.info("BuCurrency.size:" + result.size());
		
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	
	public BuCustomerCouponRecord findBySalesOrderId(Long headId){
		StringBuffer hql = new StringBuffer("");
		hql.append("from BuCustomerCouponRecord as model where 1=1 ");
		hql.append(" and model.salesOrderId = ? ");
		
		Object[] objArray =  new Object[] {headId};
		
		List<BuCustomerCouponRecord> result = getHibernateTemplate().find(
			hql.toString(), objArray);
		
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
}
