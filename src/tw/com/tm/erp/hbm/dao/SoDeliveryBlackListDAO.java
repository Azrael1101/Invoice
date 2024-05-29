package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.SoDeliveryBlackList;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.utils.DateUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * SoDeliveryBlackList entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.SoDeliveryBlackList
 * @author MyEclipse Persistence Tools
 */

public class SoDeliveryBlackListDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(SoDeliveryBlackListDAO.class);
	public static final String QUARY_TYPE_SELECT_ALL     = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE   = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT   = "recordCount";

public List<SoDeliveryBlackList>  findBlackList(HashMap findObjs){
	final HashMap fos = findObjs;
	List<SoDeliveryBlackList> re = getHibernateTemplate().executeFind(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("from SoDeliveryBlackList as model where 1=1 ");

			if (StringUtils.hasText((String) fos.get("brandCode")))
				hql.append(" and model.brandCode = :brandCode ");
			if (StringUtils.hasText((String) fos.get("name")))
				hql.append(" and model.name = :name ");
			if (StringUtils.hasText((String) fos.get("passportNo")))
				hql.append(" and model.tel = :passportNo ");		
			if (StringUtils.hasText((String) fos.get("tel")))
				hql.append(" and model.tel = :tel ");		
			if (StringUtils.hasText((String) fos.get("customerCode")))
				hql.append(" and model.customerCode = :customerCode ");	
			if (StringUtils.hasText((String) fos.get("reason1")))
				hql.append(" and model.reason1 = :reason1 ");	
			if (StringUtils.hasText((String) fos.get("reason2")))
				hql.append(" and model.reason2 = :reason2 ");	
			Query query = session.createQuery(hql.toString());
			if (StringUtils.hasText((String) fos.get("brandCode")))
				query.setString("brandCode", (String) fos.get("brandCode"));
			if (StringUtils.hasText((String) fos.get("name")))
				query.setString("name", (String) fos.get("name"));
			if (StringUtils.hasText((String) fos.get("tel")))
				query.setString("tel", (String) fos.get("tel"));
			if (StringUtils.hasText((String) fos.get("passportNo")))
				query.setString("passportNo", (String) fos.get("passportNo"));
			if (StringUtils.hasText((String) fos.get("customerCode")))
				query.setString("customerCode", (String) fos.get("customerCode"));
			if (StringUtils.hasText((String) fos.get("reason1")))
				query.setString("reason1", (String) fos.get("reason1"));
			if (StringUtils.hasText((String) fos.get("reason2")))
				query.setString("reason2", (String) fos.get("reason2"));
			return query.list();
		}
	});
	return re;
}
public boolean isBlackListByName(String brandCode, String name){
	log.info("isBlackListByName...."+brandCode+"."+name);
	boolean result = false;
	HashMap findObjs = new HashMap(0);
	findObjs.put("brandCode", brandCode);
	findObjs.put("name", name);
	findObjs.put("passportNo", null);
	findObjs.put("tel", null);
	findObjs.put("customerCode", null);
	findObjs.put("reason1", null);
	findObjs.put("reason2", null);
	List<SoDeliveryBlackList>  lists = findBlackList(findObjs);
	log.info("lists.size...."+lists.size());
	if(lists.size()>0) result= true;
	
	return result;
}

public boolean isBlackListByPassportNo(String brandCode, String passportNo){
	log.info("isBlackListByPassportNo...."+brandCode+"."+passportNo);
	boolean result = false;
	HashMap findObjs = new HashMap(0);
	findObjs.put("brandCode", brandCode);
	findObjs.put("name", null);
	findObjs.put("passportNo", passportNo);
	findObjs.put("tel", null);
	findObjs.put("customerCode", null);
	findObjs.put("reason1", null);
	findObjs.put("reason2", null);
	List<SoDeliveryBlackList>  lists = findBlackList(findObjs);
	log.info("lists.size...."+lists.size());
	if(lists.size()>0) result= true;
	
	return result;
}

public boolean isBlackListByTel(String brandCode, String tel){
	log.info("isBlackListByTel...."+brandCode+"."+tel);
	boolean result = false;
	HashMap findObjs = new HashMap(0);
	findObjs.put("brandCode", brandCode);
	findObjs.put("name", null);
	findObjs.put("passportNo", null);
	findObjs.put("tel", tel);
	findObjs.put("customerCode", null);
	findObjs.put("reason1", null);
	findObjs.put("reason2", null);
	List<SoDeliveryBlackList>  lists = findBlackList(findObjs);
	log.info("lists.size...."+lists.size());
	if(lists.size()>0) result= true;
	
	return result;
}

public boolean isBlackListByNamePassport(String brandCode, String name, String passportNo){
	log.info("isBlackListByName...."+brandCode+"."+name);
	boolean result = false;
	HashMap findObjs = new HashMap(0);
	findObjs.put("brandCode", brandCode);
	findObjs.put("name", name);
	findObjs.put("passportNo", passportNo);
	findObjs.put("tel", null);
	findObjs.put("customerCode", null);
	findObjs.put("reason1", null);
	findObjs.put("reason2", null);
	List<SoDeliveryBlackList>  lists = findBlackList(findObjs);
	log.info("lists.size...."+lists.size());
	if(lists.size()>0) result= true;
	
	return result;
}
public HashMap findByMap(HashMap findObjs, int startPage, int pageSize, String searchType) {
	final HashMap fos = findObjs;
	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final String type = searchType;
	System.out.println("start to find soDeliveryBlackList....");
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("");
			if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
				hql.append("select count(model.headId) as rowCount from SoDeliveryBlackList as model where 1=1 ");
			} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
				hql.append("select model.headId from SoDeliveryBlackList as model where 1=1 ");
			} else {
				hql.append("from SoDeliveryBlackList as model where 1=1 ");
			}

			if (StringUtils.hasText((String) fos.get("brandCode")))
				hql.append(" and model.brandCode = :brandCode ");



			if (StringUtils.hasText((String) fos.get("customerName")))
				hql.append(" and model.name = :customerName ");


			if (StringUtils.hasText((String) fos.get("passportNo")))
				hql.append(" and model.passportNo = :passportNo ");
			
			if (StringUtils.hasText((String) fos.get("tel")))
				hql.append(" and model.tel = :tel ");

			if (StringUtils.hasText((String) fos.get("reason1")))
				hql.append(" and model.reason1 = :reason1 ");
			
			System.out.println(hql.toString());
			Query query = session.createQuery(hql.toString());

			if (QUARY_TYPE_SELECT_RANGE.equals(type) || QUARY_TYPE_SELECT_ALL.equals(type)) {
				System.out.println("type:" + type + " startFrom:" + startRecordIndexStar + " to " + pSize);
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
			}

			if (StringUtils.hasText("brandCode"))
				query.setParameter("brandCode", fos.get("brandCode"));

			if (StringUtils.hasText((String) fos.get("customerName")))
				query.setString("customerName", (String) fos.get("customerName"));
		
			if (StringUtils.hasText((String) fos.get("passportNo"))){
				query.setString("passportNo", (String) fos.get("passportNo"));
			}
			
			if (StringUtils.hasText((String) fos.get("tel"))){
				query.setString("tel", (String) fos.get("tel"));
			}
			if (StringUtils.hasText((String) fos.get("reason1")))
				query.setString("reason1", (String) fos.get("reason1"));
		
			return query.list();
		}
	});

	log.info("soDeliveryBlackList.form:" + result.size());
	HashMap returnResult = new HashMap();
	returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
	if (result.size() == 0) {
		returnResult.put("recordCount", 0L);
	} else {
		log.info("soDeliveryBlackList.size:" + result.get(0));
		returnResult.put("recordCount",
				QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long
						.valueOf(result.get(0).toString()));
	}
	return returnResult;
}
}