package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.MachineSale;
import tw.com.tm.erp.utils.DateUtils;

public class MachineSaleDAO extends BaseDAO {

    public List<Object[]> findByCondition() {
	// TODO Auto-generated method stub
	return null;
    }

    public List<Object[]> findByCondition(final String lockDate, final String machineCode) {
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {
//			StringBuffer hql = new StringBuffer("from MachineSale as model where ");
//			hql.append(" to_char(model.lastUpdateDate, 'YYYYMMDD') = :lastUpdateDate ");
			StringBuffer sql = new StringBuffer("select class_id,item_brand,item_code,machine_code,status from machine_sale where");
			sql.append(" to_char(last_Update_Date, 'YYYYMMDD') = :lastUpdateDate ");
			if(StringUtils.hasText(machineCode)){
			    sql.append(" and model.machineCode = :machineCode");
			}
			System.out.println("======lockDate===="+lockDate);
			Query query = session.createSQLQuery(sql.toString());
			query.setString("lastUpdateDate", lockDate);
			if(!"".equals(machineCode)&&machineCode != null){
			    query.setString("machineCode", machineCode);
			}

			return query.list();
		}
	});
	return result;
    }

    public void saveLockItem(final MachineSale ms) {
	
	getHibernateTemplate().executeFind(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {
			StringBuffer sql = new StringBuffer("INSERT INTO MACHINE_SALE(CATEGORY01,CLASS_ID,ITEM_BRAND,ITEM_CODE,LAST_UPDATE_BY,LAST_UPDATE_DATE,MACHINE_CODE,STATUS)");
			sql.append(" VALUES('"+ms.getCategory01()+"',");
			sql.append("'"+ms.getClassId()+"',");
			sql.append("'"+ms.getItemBrand()+"',");
			sql.append("'"+ms.getItemCode()+"',");
			sql.append("'"+ms.getLastUpdateBy()+"',");
			sql.append("TO_DATE(sysdate),");
			sql.append("'"+ms.getMachineCode()+"',");
			sql.append("'"+ms.getStatus()+"')");
			Query query = session.createSQLQuery(sql.toString());
			query.executeUpdate();
			return null;
		}
	});
}

    public List checkExist(final MachineSale ms) {
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {
			StringBuffer sql = new StringBuffer("select category01,class_id,item_brand,item_code,machine_code,status from machine_sale where");
			sql.append(" category01 ='"+ms.getCategory01()+"'");
			if("".equals(ms.getClass())){
			    sql.append(" and class_id is null");
			}else{
			    sql.append(" and class_id = '"+ms.getClassId()+"'");
			}
			
			if("".equals(ms.getItemBrand())){
			    sql.append(" and item_brand is null");
			}else{
			    sql.append(" and item_brand = '"+ms.getItemBrand()+"'");    
			}
			if("".equals(ms.getItemCode())){
			    sql.append(" and item_code is null");
			}else{
			    sql.append(" and item_code = '"+ms.getItemCode()+"'");
			}
			sql.append(" and machine_code = '"+ms.getMachineCode()+"'");
			sql.append(" and status ='"+ms.getStatus()+"'");
			Query query = session.createSQLQuery(sql.toString());
			System.out.println("==sql=="+sql);
			System.out.println("==sql.list=="+query.list());
			return query.list();
		}
	});
	return result;
}

}
