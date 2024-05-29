package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException; 
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFHead;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFLine;
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.hbm.bean.SiMenuCtrl;

public class SiMenuDAO extends BaseDAO {
	
	private static final Log log = LogFactory.getLog(SiMenuDAO.class);
	
	protected void initDao() {
		// do nothing
	}

	public List myfindByProperty(String entityBean ) {
		log.info( "===========<myfindByProperty>===========");
		StringBuffer queryString = new StringBuffer(" from ");
			queryString.append( entityBean );
			queryString.append(" as model");
			queryString.append(" order by parentMenuId "); // where menuId < 1000   
			
			log.info( queryString.toString() );
			log.info( "===========</myfindByProperty>===========");
		return getHibernateTemplate().find(queryString.toString());
	}

	
	public List getBrandUserMenuManager(final String brandCode,
			final String loginName, final String systemType, final String functionType) {
		List re = null;
		re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				StringBuffer hql = new StringBuffer("select distinct sim ");
				hql.append("from SiMenu as sim,SiGroupMenu as sigm,SiUsersGroup as siug ");
				hql.append("where sim.menuId=sigm.id.menuId and sigm.id.brandCode=siug.id.brandCode and sigm.id.groupCode=siug.id.groupCode ");
				hql.append("and siug.id.brandCode=:brandCode ");
				hql.append("and siug.id.employeeCode=:loginName ");
				if (systemType != null) 
					hql.append("and sim.systemType=:systemType ");
				if (functionType != null) 
					hql.append("and sim.functionType=:functionType ");
				hql.append(" order by sim.parentMenuId,sim.lineNo");
				Query query = session.createQuery(hql.toString());
				query.setParameter("brandCode", brandCode);
				query.setParameter("loginName", loginName);
				if (systemType != null)
					query.setParameter("systemType", systemType);
				if (functionType != null)
					query.setParameter("functionType", functionType);
				return query.list();
			}
		});
		return re;
	}

	public List getBrandUserReportManager(final String brandCode,	final String loginName, 
			final String systemType,	final String functionType, final String functionCode) {
		List re = null;
		re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,	SQLException {
				StringBuffer hql = new StringBuffer("select distinct sim ");
				hql.append("from SiMenu as sim,SiGroupMenu as sigm,SiUsersGroup as siug ");
				hql.append("where sim.menuId=sigm.id.menuId and sigm.id.brandCode=siug.id.brandCode and sigm.id.groupCode=siug.id.groupCode ");
 		 // hql.append("and siug.id.brandCode='"+brandCode+"' ");
	 	 // hql.append("and siug.id.employeeCode='"+loginName+"' ");
				hql.append("and siug.id.brandCode=:brandCode ");
				hql.append("and siug.id.employeeCode=:loginName ");
				if (systemType != null) {
					hql.append("and sim.systemType=:systemType ");
				}
				hql.append("and sim.functionType=:functionType ");
				hql.append("and sim.functionCode=:functionCode ");
				hql.append(" order by sim.parentMenuId,sim.lineNo");
				Query query = session.createQuery(hql.toString());
		//	query.setParameter("brandCode", brandCode);
				query.setParameter("loginName", loginName);
				if (systemType != null) {
					query.setParameter("systemType", systemType);
				}
				query.setParameter("functionType", functionType);
				query.setParameter("functionCode", functionCode);
				return query.list();
			}
		});
		return re;
	}

	public List getFunctionURL(final String systemType,	final String functionType, final String functionCode) {
		List re = null;
		re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				StringBuffer hql = new StringBuffer("select distinct sim ");
				hql.append("from SiMenu as sim ");
				hql.append("where sim.functionCode=:functionCode ");
				if (StringUtils.hasText(systemType))
					hql.append("and sim.systemType=:systemType ");
				if (StringUtils.hasText(functionType))
					hql.append("and sim.functionType=:functionType ");
				Query query = session.createQuery(hql.toString());
				if (StringUtils.hasText(systemType)) 
					query.setParameter("systemType", systemType);
				if (StringUtils.hasText(functionType))
					query.setParameter("functionType", functionType);
				query.setParameter("functionCode", functionCode);
				return query.list();
			}
		});
		return re;
	}
	
	public boolean isRepeat( String propertyName,  String value ) {
		log.debug("finding SiMenu instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from SiMenu as model where model."
					+ propertyName + "= ?";
			if ( getHibernateTemplate().find(queryString, value).size() > 0 ) {
				return true;
			}
			
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
		
		return false;
	}
	
	//新增權限(wade)
	public List<SiMenu> findPageLine(String employeeCode, String brandCode, int startPage, int pageSize) {
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String employee_Code = employeeCode;
		final String brand_Code = brandCode;
		List<SiMenu> result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session) throws HibernateException, SQLException {
			    	String hql = "from SiMenu M where M.menuId in (select G.id.menuId from SiGroupMenu G where G.id.groupCode in (select U.groupCode from SiUsersGroup U where U.employeeCode=:employeeCode and U.brandCode=:brandCode)) order by M.menuId";
					Query query = session.createQuery(hql);
					if(startRecordIndexStar > 0) query.setFirstResult(startRecordIndexStar);
					if(pSize > 0) query.setMaxResults(pSize);
					query.setString("employeeCode", employee_Code);
					query.setString("brandCode", brand_Code);
					return query.list();
			    }
			}); 
		return result;
	}
	
	public Long findPageLineCount(String employeeCode, String brandCode){
		String hql = "select count(*) from SiMenu M where M.menuId in (select G.id.menuId from SiGroupMenu G where G.id.groupCode in (select U.groupCode from SiUsersGroup U where U.employeeCode=? and U.brandCode=?)) order by M.menuId";
		Object obj = getHibernateTemplate().find(hql, new Object[]{employeeCode, brandCode});
		return obj == null ? 0L : (Long)(((List)obj).get(0));
	}
	
	public SiMenuCtrl findById(String menuId) {
		log.info("找選單:"+menuId);
		try {
			String queryString = "from SiMenuCtrl as model where model.menuId = ?";
			List<SiMenuCtrl> siMenus = 	getHibernateTemplate().find(queryString,menuId);	
			return (siMenus != null && siMenus.size() > 0 ? siMenus.get(0) : null);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	public List<SiMenuCtrl> findByParentMenuId(String parentMenuId){
		List<SiMenuCtrl> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from SiMenuCtrl as model where 1=1 ");
				if (parentMenuId != null || "".equals(parentMenuId))
					hql.append(" and model.parentMenuId = :parentMenuId");
				hql.append(" order by model.menuId");
				Query query = session.createQuery(hql.toString());
				if (parentMenuId != null || "".equals(parentMenuId))
					query.setString("parentMenuId", parentMenuId);
				return query.list();
			}
		});
		return result;
	}
	
	public List<SiMenuCtrl> findSearchPageLine(String parentMenuId, int startPage, int pageSize){
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		List<SiMenuCtrl> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from SiMenuCtrl as model where 1=1 ");
				if ( !"".equals(parentMenuId) )
					hql.append(" and model.parentMenuId = :parentMenuId");
				hql.append(" order by model.menuId");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if ( !"".equals(parentMenuId) )
					query.setString("parentMenuId", parentMenuId);
				return query.list();
			}
		});
		return result;
	}
	
	public Long findSearchPageLineMaxIndex(String parentMenuId) {
		Long lineMaxIndex = new Long(0);
		List<SiMenuCtrl> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from SiMenuCtrl as model where 1=1 ");
				if (!"".equals(parentMenuId))
					hql.append(" and model.parentMenuId = :parentMenuId");
				Query query = session.createQuery(hql.toString());
				if (!"".equals(parentMenuId))
					query.setString("parentMenuId", parentMenuId);
				return query.list();
			}
		});
		if (result != null && result.size() > 0) {
			lineMaxIndex = (long) result.size();
		}
		return lineMaxIndex;
	}
	
}
