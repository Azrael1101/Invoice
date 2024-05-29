package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;

public class ImMovementFileDAO extends BaseDAO {
	public List<ImMovementItem> getDeliveryWarehouseManager(HashMap findObjs) {
		
		final HashMap fos = findObjs ; 
		
		List<ImMovementItem> re = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						
						StringBuffer hql = 
							new StringBuffer("from ImMovementItem as item, ImWarehouse as warehouse where 1=1 ");
						
						if( StringUtils.hasText((String)fos.get("brandCode")) )
							hql.append(" and item.brandCode = :brandCode ");
									
						if( StringUtils.hasText((String)fos.get("headId")) )
							hql.append(" and item.headId = :headId ");
							hql.append(" and item.deliveryWarehouseCode = warehouse.warehouseCode ");
						
						hql.append(" group by warehouse.warehouseManager ");
						
						Query query = session.createQuery(hql.toString());
												
						if( StringUtils.hasText("brandCode") )
							query.setParameter("brandCode",fos.get("brandCode") );	
						
						if( StringUtils.hasText((String)fos.get("headId")) )
							query.setString("headId", (String)fos.get("headId") );
		
						
						
						return query.list();
					}
				});
	
		return re ;

	}

}