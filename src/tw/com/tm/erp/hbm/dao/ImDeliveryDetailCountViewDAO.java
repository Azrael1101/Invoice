package tw.com.tm.erp.hbm.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
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
import tw.com.tm.erp.hbm.bean.ImItemOnHandView;
import tw.com.tm.erp.hbm.service.PoPurchaseOrderHeadService;
import tw.com.tm.erp.utils.DateUtils;

public class ImDeliveryDetailCountViewDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImDeliveryDetailCountViewDAO.class);
	public static final String QUARY_TYPE_SELECT_ALL = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";

	/**
	 * find by primary key
	 * 
	 * @param findClass
	 *            return class
	 * @param pk
	 *            primary key object
	 * @return
	 */
	@SuppressWarnings(value = "unchecked")
	public Object findByPrimaryKey(Class findClass, Serializable pk) {
		return getHibernateTemplate().get(findClass, pk, null);
	}
	
	public HashMap find(HashMap findObj) {
		final HashMap fos = findObj;
		HashMap returnResult = new HashMap();
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("select model.totalD, model.totalM, model.totalY from ImDeliveryDetailCountView as model where 1=1 ");
				hql.append("AND model.id.itemCode = :itemCode ");
				hql.append("AND model.id.warehouseCode = :warehouseCode ");
				hql.append("AND model.id.shipDay = :shipDay ");
				hql.append("AND model.id.shipMonth = :shipMonth ");
				hql.append("AND model.id.shipYear = :shipYear");
				
				Query query = session.createQuery(hql.toString());
				
				query.setString("itemCode", (String)fos.get("itemCode"));
				query.setString("warehouseCode", (String)fos.get("warehouseCode"));
				query.setString("shipDay", (String)fos.get("shipDay"));
				query.setString("shipMonth", (String)fos.get("shipMonth"));
				query.setString("shipYear", (String)fos.get("shipYear"));

				return query.list();
			}
		});
		log.info(result.size());
		returnResult.put("form", result);
		return returnResult;
	}
}
