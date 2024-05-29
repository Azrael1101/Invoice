package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrandId;
import tw.com.tm.erp.hbm.bean.BuShopEmployee;
import tw.com.tm.erp.hbm.bean.BuShopEmployeeId;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployee;

public class BuShopEmployeeDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(BuShopEmployeeDAO.class);

    public List<BuShopEmployee> findEmployeeByShopCode(String shopCode) {

	StringBuffer hql = new StringBuffer("from BuShopEmployee as model ");
	hql.append("where model.id.shopCode = ? ");
	
	List<BuShopEmployee> buShopEmployees = getHibernateTemplate().find(hql.toString(),
		new Object[] {shopCode});
	return buShopEmployees;
    }
    public BuShopEmployee findByempolyee(BuShopEmployeeId buEmpId) {
		log.debug("getting BuShopEmployee instance with id: " + buEmpId);
		try {
			BuShopEmployee instance = (BuShopEmployee) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuShopEmployee", buEmpId);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	public int deleteBuEmployeeShop(String brandCode, String employeeCode){
		log.info("刪除buEmployeeDAO");
		String hql = "delete BuShopEmployee where id.employeeCode=? and id.shopCode in (select shopCode from BuShop where brandCode = ?)";
		return getHibernateTemplate().bulkUpdate(hql, new Object[]{employeeCode, brandCode});
	}
	public List<BuShopEmployee> findBuEmployeeByShop(String shopCodeFind){

		final String shopCode = shopCodeFind;		
		List<BuShopEmployee> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from BuShopEmployee as model where 1=1 ");
						if (shopCode != null)
							hql.append(" and model.id.shopCode = :shopCode");
							hql.append(" order by model.id.employeeCode");
						Query query = session.createQuery(hql.toString());

						if (shopCode != null)
						{
							query.setString("shopCode", shopCode);
						}	
						return query.list();
					}
				});
		return result;
	}
	public List<BuShopEmployee> findPageLine(String shopCodeFind, int startPage,int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String shopCode = shopCodeFind;		
		List<BuShopEmployee> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from BuShopEmployee as model where 1=1 ");
						if (shopCode != null)
							hql.append(" and model.id.shopCode = :shopCode");
							hql.append(" order by model.id.employeeCode");
						Query query = session.createQuery(hql.toString());
						query.setFirstResult(startRecordIndexStar);
						query.setMaxResults(pSize);
						if (shopCode != null)
						{
							query.setString("shopCode", shopCode);
						}	
						return query.list();
					}
				});
		return result;
	}
	public Long findPageLineMaxIndex(String shopCodeFind, String enableFind) {


		final String shopCode = shopCodeFind;		
		final String enable = enableFind;
		List<BuShopEmployee> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from BuShopEmployee as model where 1=1 ");
						if (shopCode != null)
							hql.append(" and model.id.shopCode = :shopCode");
							hql.append(" and model.enable = :enable");
							hql.append(" order by model.id.employeeCode");
						Query query = session.createQuery(hql.toString());
						if (shopCode != null)
						{
							query.setString("shopCode", shopCode);
							query.setString("enable", enable);
						}	
						return query.list();
					}
				});
		return result.size()+0L		;
	}
	public BuShopEmployee findItemByIdentification(String buShop, String employeeCode) {
		StringBuffer hql = new StringBuffer(
		"from BuShopEmployee as model where model.id.shopCode = ? and model.id.employeeCode = ?");
		List<BuShopEmployee> result = getHibernateTemplate().find(hql.toString(), new Object[] { buShop, employeeCode });
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
}