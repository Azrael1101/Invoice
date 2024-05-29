package tw.com.tm.erp.hbm.dao;


import java.sql.SQLException;
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
import tw.com.tm.erp.hbm.bean.BuCompany;
import tw.com.tm.erp.hbm.bean.BuGoalAchevement;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuCurrency entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuCurrency
 * @author MyEclipse Persistence Tools
 */

public class BuCompanyDAO extends BaseDAO {
	
	
	public static final String COMPANY_CODE = "companyCode";	
	public static final String COMPANY_NAME = "companyName";
	public static final String GUI_CODE = "guiCode";
	public static final String BUSSINESS_MASTER_NAME = "businessMasterName";
	public static final String TAX_REGISTER_NO = "taxRegisterNo";
	public static final String REGISTER_MASTER_NAME = "registerMasterName";
	public static final String MASTER_NAME = "masterName";
	public static final String ACCREDITEE = "accreditee";
	public static final String ADDRESS = "address";
	public static final String TEL = "tel";
	public static final String REPORT_TITLE = "reportTitle";
	public static final String RESERVE1 = "reserve1";
	public static final String RESERVE2 = "reserve2";
	public static final String RESERVE3 = "reserve3";
	public static final String RESERVE4 = "reserve4";
	public static final String RESERVE5 = "reserve5";
	private static final Log log = LogFactory.getLog(BuCompanyDAO.class);

	protected void initDao() {
	}
	
	public BuCompany findById(java.lang.String id) {
		try {
			BuCompany instance = (BuCompany) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuCompany", id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	
	    public List<BuCompany> findAll() {
		log.debug("finding all BuCompany instances");
		try {
		    String queryString = "from BuCompany";
		    return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
		    log.error("find all failed", re);
		    throw re;
		}
	    }
/*****
	    public List<BuCompany> find(HashMap conditionMap) {

	    final String companyCode = (String) conditionMap.get("companyCode");	
		final String companyName = (String) conditionMap.get("companyName");
		final String guiCode = (String) conditionMap.get("guiCode");
		final String businessMasterName = (String) conditionMap.get("businessMasterName");
		final String taxRegisterNo = (String) conditionMap.get("taxRegisterNo");
		final String registerMasterName = (String) conditionMap.get("registerMasterName");
		final String masterName = (String) conditionMap.get("masterName");
		final String accreditee = (String) conditionMap.get("accreditee");
		final String address = (String) conditionMap.get("address");
		final String tel = (String) conditionMap.get("tel");
		final String reportTitle = (String) conditionMap.get("reportTitle");
		final String reserve1 = (String) conditionMap.get("reserve1");
		final String reserve2 = (String) conditionMap.get("reserve2");
		final String reserve3 = (String) conditionMap.get("reserve3");
		final String reserve4 = (String) conditionMap.get("reserve4");
		final String reserve5 = (String) conditionMap.get("reserve5");
	
		

		List<BuCompany> result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select model from BuCompany as model");
				hql.append(" where model.companyCode != null");
				
				if (StringUtils.hasText(companyCode))
				    hql.append(" and model.companyCode like :companyCode");

				if (StringUtils.hasText(companyName))
				    hql.append(" and model.companyName like :companyName");

				if (StringUtils.hasText(guiCode))
				    hql.append(" and model.discount like :guiCode");

				if (StringUtils.hasText(businessMasterName))
				    hql.append(" and model.bonus like :businessMasterName");
				
				if (StringUtils.hasText(taxRegisterNo))
				    hql.append(" and model.achevement like :taxRegisterNo");

				if (StringUtils.hasText(registerMasterName))
				    hql.append(" and model.discount like :registerMasterName");

				if (StringUtils.hasText(masterName))
				    hql.append(" and model.bonus like :masterName");
				
				if (StringUtils.hasText(accreditee))
				    hql.append(" and model.achevement like :accreditee");

				if (StringUtils.hasText(address))
				    hql.append(" and model.discount like :address");

				if (StringUtils.hasText(tel))
				    hql.append(" and model.bonus like :tel");
				
				if (StringUtils.hasText(reportTitle))
				    hql.append(" and model.bonus like :reportTitle");
				
				if (StringUtils.hasText(reserve1))
				    hql.append(" and model.bonus like :reserve1");
				
				if (StringUtils.hasText(reserve2))
				    hql.append(" and model.bonus like :reserve2");
				
				if (StringUtils.hasText(reserve3))
				    hql.append(" and model.bonus like :reserve3");
				
				if (StringUtils.hasText(reserve4))
				    hql.append(" and model.bonus like :reserve4");
				
				if (StringUtils.hasText(reserve5))
				    hql.append(" and model.bonus like :reserve5");
				
			

				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
				
				if (StringUtils.hasText(companyCode))
				    query.setString("companyCode", "%" + companyCode + "%");

				if (StringUtils.hasText(companyName))
				    query.setString("companyName", "%" + companyName + "%");
				
				if (StringUtils.hasText(guiCode))
				    query.setString("guiCode", "%" + guiCode + "%");
				
				if (StringUtils.hasText(businessMasterName))
				    query.setString("businessMasterName", "%" + businessMasterName + "%");
				
				if (StringUtils.hasText(taxRegisterNo))
				    query.setString("taxRegisterNo", "%" + taxRegisterNo + "%");
				
				if (StringUtils.hasText(registerMasterName))
				    query.setString("registerMasterName", "%" + registerMasterName + "%");
				
				if (StringUtils.hasText(masterName))
				    query.setString("masterName", "%" + masterName + "%");
				
				if (StringUtils.hasText(accreditee))
				    query.setString("accreditee", "%" + accreditee + "%");
				
				if (StringUtils.hasText(address))
				    query.setString("address", "%" + address + "%");
				
				if (StringUtils.hasText(tel))
				    query.setString("tel", "%" + tel + "%");
				
				if (StringUtils.hasText(reportTitle))
				    query.setString("reportTitle", "%" + reportTitle + "%");
				
				if (StringUtils.hasText(reserve1))
				    query.setString("reserve1", "%" + reserve1 + "%");
				
				if (StringUtils.hasText(reserve2))
				    query.setString("reserve2", "%" + reserve2 + "%");
				
				if (StringUtils.hasText(reserve3))
				    query.setString("reserve3", "%" + reserve3 + "%");
				
				if (StringUtils.hasText(reserve4))
				    query.setString("reserve4", "%" + reserve4 + "%");
				
				if (StringUtils.hasText(reserve5))
				    query.setString("reserve5", "%" + reserve5 + "%");
			

				return query.list();
			    }
			});

		return result;
	    }
****************************************/
}
