package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployee;

/**
 * CmDeclarationHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CmDeclarationHeadDAO extends BaseDAO {

	private static final Log log = LogFactory.getLog(CmDeclarationHeadDAO.class);

	public List<CmDeclarationHead> find(HashMap findObjs) {
		log.info("CmDeclarationHeadDAO.find");
		final HashMap fos = findObjs;

		List<CmDeclarationHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from CmDeclarationHead as model where 1=1 ");

				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");

				if (StringUtils.hasText((String) fos.get("startOrderNo")))
					hql.append(" and model.declNo >= :startOrderNo ");

				if (StringUtils.hasText((String) fos.get("endOrderNo")))
					hql.append(" and model.declNo <= :endOrderNo ");

				if (null != fos.get("orderDateS"))
					hql.append(" and model.orderDate >= :orderDateS ");

				if (null != fos.get("orderDateE"))
					hql.append(" and model.orderDate <= :orderDateE ");

				hql.append(" order by lastUpdateDate desc ");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));

				if (StringUtils.hasText((String) fos.get("startOrderNo")))
					query.setString("startOrderNo", (String) fos.get("startOrderNo"));

				if (StringUtils.hasText((String) fos.get("endOrderNo")))
					query.setString("endOrderNo", (String) fos.get("endOrderNo"));

				if (null != fos.get("orderDateS"))
					query.setDate("orderDateS", (Date) fos.get("orderDateS"));

				if (null != fos.get("orderDateE"))
					query.setDate("orderDateE", (Date) fos.get("orderDateE"));

				return query.list();
			}
		});

		return re;
	}

	/**
	 * 撈一筆報關單
	 * @param declNo
	 * @return
	 */
	public CmDeclarationHead findOneCmDeclaration(String declNo){
		return (CmDeclarationHead)findFirstByProperty("CmDeclarationHead", "and declNo = ?", new Object[]{ declNo });
	}
	
	public String getDeclarationTypeByNo(final String declNo){
	//	log.info("DAO.getDeclarationTypeByNo...");
		List temp = getHibernateTemplate().executeFind(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {
			String hql= "select decl_type from cm_declaration_head where decl_no = :declNo";
		//	System.out.println("compare receive hql:"+hql);
			SQLQuery query = session.createSQLQuery(hql);
			query.setString("declNo", declNo);
			return query.list();
		}
		});
		if(temp.size()==0){
			return null;
		}else{
			return temp.get(0).toString();
		}
	}
	
	public CmDeclarationHead findByDeclarationNo(String originalDeclarationNo){
	    
	    try {  
	    	StringBuffer hql = new StringBuffer(" from CmDeclarationHead as model");
	    	hql.append(" where model.decl_no = :originalDeclarationNo ");
	    	log.info("報單號hql:"+hql);
	  	
	    	List<CmDeclarationHead> result = getHibernateTemplate()
	  			.find(hql.toString(), new String[] {});
	    	return (result != null && result.size() > 0 ? result.get(0) : null);
	  	
	    } catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	 public List<CmDeclarationHead> findFileNo(String fileNo ) {
	    	
	    	StringBuffer hql = new StringBuffer("from CmDeclarationHead as model where model.fileNo = ?");
	    	List<CmDeclarationHead> result = getHibernateTemplate().find(hql.toString(),
			new Object[] { fileNo });
	    	
	    	return result;
	    }
}