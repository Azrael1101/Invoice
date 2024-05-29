package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import tw.com.tm.erp.hbm.bean.SiGroupMenu;

public class SiGroupMenuDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(SiGroupMenuDAO.class);
	
	
	protected void initDao() {
		// do nothing
	}

	/**
	 * find page line
	 * 
	 * @param groupCode
	 * @param brandCode
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public List<SiGroupMenu> findPageLine(final String groupCode ,final String brandCode , int startPage, int pageSize) {
		log.info("SiGroupMenuDAO.findPageLine groupCode=" + brandCode + "brandCode="+ groupCode + "startPage=" + startPage + "pageSize" + pageSize);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		List<SiGroupMenu> re = getHibernateTemplate().executeFind(
		new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from SiGroupMenu as model where ");
					hql.append("model.id.brandCode = '"+ brandCode +"' and model.id.groupCode = '"+groupCode+"' order by model.id.menuId");
					
					Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				log.info( "Query Over");
				return query.list();
			}
		});
		return re;
	}
	
	/**
	 * find page line 最後一筆 index
	 * 
	 * @param headId
	 * @return
	 */
	public Long findPageLineMaxIndex(final String brandCode ,final String groupCode) {
		log.info("SiGroupMenuDAO.findPageLineMaxIndex brandCode=" + brandCode + "groupCode="+ groupCode);
		List<SiGroupMenu> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from SiGroupMenu as model where ");
				hql.append("model.id.brandCode = "+ brandCode +" and model.id.groupCode = "+groupCode+" order by model.id.menuId");
				Query query = session.createQuery(hql.toString());
				log.info("SiGroupMenuDAO.findPageLineMaxIndex MaxIndex=" + query.list() );
				return query.list();
			}
		});
		return 0L;
	}
	
	public List<Long> getMenuIds(String brandCode, String groupCode){
		String hql = "select M.id.menuId from SiGroupMenu M where M.id.brandCode=? and M.id.groupCode=?";
		return getHibernateTemplate().find(hql, new Object[]{brandCode, groupCode});
	}
	
	public List<Long> getEnableMenuIds(String brandCode, String groupCode){
		String hql = "select M.id.menuId from SiGroupMenu M where M.id.brandCode=? and M.id.groupCode=? and M.enable=?";
		return getHibernateTemplate().find(hql, new Object[]{brandCode, groupCode, "Y"});
	}
	
	public List<Long> getEnableMenuIds_N(String brandCode, String groupCode, String enable){
		String hql = "select M.id.menuId,S.name,S.url from SiGroupMenu M,SiMenu S where M.id.brandCode=? and M.id.groupCode=? and M.enable=? and M.id.menuId = S.menuId";
		return getHibernateTemplate().find(hql, new Object[]{brandCode, groupCode, "Y"});
	}
	
	public List<Long> getUserMenuIds(String brandCode, String employeeCode){
		String hql = "select M.id.menuId from SiGroupMenu M where M.id.groupCode in (select G.groupCode from SiUsersGroup G where G.brandCode=? and G.employeeCode=? and G.groupCode<>?)";
		return getHibernateTemplate().find(hql, new Object[]{brandCode, employeeCode, employeeCode});
	}
	
	public int deleteUnableSiGroupMenu(String brandCode, String groupCode){
		String hql = "delete SiGroupMenu where id.brandCode=? and id.groupCode=? and enable=?";
		return getHibernateTemplate().bulkUpdate(hql, new Object[]{brandCode, groupCode, "N"});
	}
	
	public int deleteSiGroupMenu(String brandCode, String groupCode){
		log.info("刪除MenuDAO");
		String hql = "delete SiGroupMenuCtrl where id.brandCode=? and id.groupCode=?";
		return getHibernateTemplate().bulkUpdate(hql, new Object[]{brandCode, groupCode});
	}
}
