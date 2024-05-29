package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuFlightSchedule;
import tw.com.tm.erp.utils.DateUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuFlightSchedule entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuFlightSchedule
 * @author MyEclipse Persistence Tools
 */

public class BuFlightScheduleDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BuFlightScheduleDAO.class);
	public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
	public List<BuFlightSchedule> findByMap(HashMap findObjs) {
		final HashMap fos = findObjs;
		List<BuFlightSchedule> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<BuFlightSchedule> doInHibernate(Session session) throws HibernateException, SQLException {
				 Date flightDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("flightDateString"));
				StringBuffer hql = new StringBuffer(
						"from BuFlightSchedule as model where 1=1 ");
				if (null != (java.util.Date) fos.get("flightDateString") )
					hql.append(" and model.flightDate = :flightDate ");				
				if (StringUtils.hasText((String) fos.get("flightNo")))
					hql.append(" and model.flightNo = :flightNo ");
				if (StringUtils.hasText((String) fos.get("flightCompany")))
					hql.append(" and model.flightCompany = :flightCompany ");
				if (StringUtils.hasText((String) fos.get("terminal")))
					hql.append(" and model.terminal = :terminal ");
				if (StringUtils.hasText((String) fos.get("flightType")))
					hql.append(" and model.flightType = :flightType ");		
				if (StringUtils.hasText((String) fos.get("departure")))
					hql.append(" and model.departure = :departure ");	
				if (StringUtils.hasText((String) fos.get("scheduleTime")))
					hql.append(" and model.scheduleTime = :scheduleTime ");	
				if (StringUtils.hasText((String) fos.get("actualTime")))
					hql.append(" and model.actualTime = :actualTime ");	
				if (StringUtils.hasText((String) fos.get("gate")))
					hql.append(" and model.gate = :gate ");	
				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");	
				hql.append(" order by flightDate, scheduleTime, flightNo asc ");
				
				System.out.println(hql.toString());
				Query query = session.createQuery(hql.toString());
				if (null != fos.get("flightDate"))
					query.setDate("flightDate", flightDate);		
				if (StringUtils.hasText((String) fos.get("flightNo")))
					query.setString("flightNo", (String) fos.get("flightNo"));
				if (StringUtils.hasText((String) fos.get("flightCompany")))
					query.setString("flightCompany", (String) fos.get("flightCompany"));
				if (StringUtils.hasText((String) fos.get("terminal")))
					query.setString("terminal", (String) fos.get("terminal"));
				if (StringUtils.hasText((String) fos.get("flightType")))
					query.setString("flightType", (String) fos.get("flightType"));
				if (StringUtils.hasText((String) fos.get("departure")))
					query.setString("departure", (String) fos.get("departure"));
				if (StringUtils.hasText((String) fos.get("scheduleTime")))
					query.setString("scheduleTime", (String) fos.get("scheduleTime"));
				if (StringUtils.hasText((String) fos.get("actualTime")))
					query.setString("actualTime", (String) fos.get("actualTime"));
				if (StringUtils.hasText((String) fos.get("gate")))
					query.setString("gate", (String) fos.get("gate"));
				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));
				return query.list();
			}
		});

		return result;
	}
	
	public HashMap findPageLine(HashMap findObjs, int startPage, int pageSize, String searchType) {
		log.info("findPageLine..."+searchType);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String type = searchType;
		final HashMap fos = findObjs;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Date flightDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("flightDateString"));
				
				StringBuffer hql = new StringBuffer("");
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.flightDate) as rowCount from BuFlightSchedule as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
					hql.append("select model.headId from BuFlightSchedule as model where 1=1 ");
				} else {
					hql.append("from BuFlightSchedule as model where 1=1 ");
				}
				if (null != flightDate )
					hql.append(" and model.flightDate = :flightDate ");				
				if (StringUtils.hasText((String) fos.get("flightNo")))
					hql.append(" and model.flightNo = :flightNo ");
				if (StringUtils.hasText((String) fos.get("flightCompany")))
					hql.append(" and model.flightCompany = :flightCompany ");
				if (StringUtils.hasText((String) fos.get("terminal")))
					hql.append(" and model.terminal = :terminal ");
				if (StringUtils.hasText((String) fos.get("flightType")))
					hql.append(" and model.flightType = :flightType ");		
				if (StringUtils.hasText((String) fos.get("departure")))
					hql.append(" and model.departure = :departure ");	
				if (StringUtils.hasText((String) fos.get("scheduleTime")))
					hql.append(" and model.scheduleTime = :scheduleTime ");	
				if (StringUtils.hasText((String) fos.get("actualTime")))
					hql.append(" and model.actualTime = :actualTime ");	
				if (StringUtils.hasText((String) fos.get("gate")))
					hql.append(" and model.gate = :gate ");	
				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");	
				if (!QUARY_TYPE_RECORD_COUNT.equals(type))
					hql.append(" order by flightDate, scheduleTime, flightNo asc ");
				log.info(hql.toString());
				
				//hql.append(" and model.soDeliveryHead.headId = :headId order by indexNo");
				
				Query query = session.createQuery(hql.toString());
				if(QUARY_TYPE_SELECT_RANGE.equals(type)){
					query.setFirstResult(startRecordIndexStar);
					query.setMaxResults(pSize);
				}
				if (null != flightDate)
					query.setDate("flightDate", flightDate);		
				if (StringUtils.hasText((String) fos.get("flightNo")))
					query.setString("flightNo", (String) fos.get("flightNo"));
				if (StringUtils.hasText((String) fos.get("flightCompany")))
					query.setString("flightCompany", (String) fos.get("flightCompany"));
				if (StringUtils.hasText((String) fos.get("terminal")))
					query.setString("terminal", (String) fos.get("terminal"));
				if (StringUtils.hasText((String) fos.get("flightType")))
					query.setString("flightType", (String) fos.get("flightType"));
				if (StringUtils.hasText((String) fos.get("departure")))
					query.setString("departure", (String) fos.get("departure"));
				if (StringUtils.hasText((String) fos.get("scheduleTime")))
					query.setString("scheduleTime", (String) fos.get("scheduleTime"));
				if (StringUtils.hasText((String) fos.get("actualTime")))
					query.setString("actualTime", (String) fos.get("actualTime"));
				if (StringUtils.hasText((String) fos.get("gate")))
					query.setString("gate", (String) fos.get("gate"));
				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));
				return query.list();
			}
		});
		log.info("buFlightSchedule.form:" + result.size());
		HashMap returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			log.info("buFlightSchedule.size:" + result.get(0));
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long
							.valueOf(result.get(0).toString()));
		}
		return returnResult;
	}
	
	public List<Object[]> findViewByMap(HashMap findObjs) {
		log.info("findViewByMap...");
		final HashMap fos = findObjs;
		List<Object[]> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<BuFlightSchedule> doInHibernate(Session session) throws HibernateException, SQLException {
				Date flightDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("flightDateString"));
				StringBuffer hql = new StringBuffer("");
				hql.append("SELECT FLIGHT_DATE, FLIGHT_NO, FLIGHT_COMPANY, TERMINAL, DEPARTURE, SCHEDULE_TIME, ACTUAL_TIME, GATE, STATUS, 'U' AS ACTION");
				hql.append(" from ERP.BU_FLIGHT_SCHEDULE where 1=1 ");
				if (null !=flightDate)
					hql.append(" and FLIGHT_DATE = :flightDate ");				
				if (StringUtils.hasText((String) fos.get("flightNo")))
					hql.append(" and FLIGHT_NO = :flightNo ");
				if (StringUtils.hasText((String) fos.get("flightCompany")))
					hql.append(" and FLIGHT_COMPANY = :flightCompany ");
				if (StringUtils.hasText((String) fos.get("terminal")))
					hql.append(" and TERMINAL = :terminal ");
				if (StringUtils.hasText((String) fos.get("flightType")))
					hql.append(" and FLIGHT_TYPE = :flightType ");		
				if (StringUtils.hasText((String) fos.get("departure")))
					hql.append(" and DEPARTURE = :departure ");	
				if (StringUtils.hasText((String) fos.get("scheduleTime")))
					hql.append(" and SCHEDULE_TIME = :scheduleTime ");	
				if (StringUtils.hasText((String) fos.get("actualTime")))
					hql.append(" and ACTUAL_TIME = :actualTime ");	
				if (StringUtils.hasText((String) fos.get("gate")))
					hql.append(" and GATE = :gate ");	
				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and STATUS = :status ");	
				hql.append(" order by FLIGHT_TYPE, SCHEDULE_TIME, FLIGHT_NO asc ");
				
				System.out.println(hql.toString());
				Query query = session.createSQLQuery(hql.toString());
				if (null !=flightDate)
					query.setDate("flightDate", flightDate);		
				if (StringUtils.hasText((String) fos.get("flightNo")))
					query.setString("flightNo", (String) fos.get("flightNo"));
				if (StringUtils.hasText((String) fos.get("flightCompany")))
					query.setString("flightCompany", (String) fos.get("flightCompany"));
				if (StringUtils.hasText((String) fos.get("terminal")))
					query.setString("terminal", (String) fos.get("terminal"));
				if (StringUtils.hasText((String) fos.get("flightType")))
					query.setString("flightType", (String) fos.get("flightType"));
				if (StringUtils.hasText((String) fos.get("departure")))
					query.setString("departure", (String) fos.get("departure"));
				if (StringUtils.hasText((String) fos.get("scheduleTime")))
					query.setString("scheduleTime", (String) fos.get("scheduleTime"));
				if (StringUtils.hasText((String) fos.get("actualTime")))
					query.setString("actualTime", (String) fos.get("actualTime"));
				if (StringUtils.hasText((String) fos.get("gate")))
					query.setString("gate", (String) fos.get("gate"));
				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));
				return query.list();
			}
		});

		return result;
	}
	
	
    public void deleteFlightScheduleByHeadId(final Long headId){
    	if (null!=headId){
    		getHibernateTemplate().execute(new HibernateCallback() {
    		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		    			StringBuffer hql = new StringBuffer("DELETE FROM ERP.BU_FLIGHT_SCHEDULE WHERE 1=1 ");
		    			hql.append(" AND HEAD_ID = :headId");
		    			
		    			Query query = session.createSQLQuery(hql.toString());
		    			query.setLong("headId", headId);
		    			return query.executeUpdate();
    		    	
    		    }
    		});
    	
    	}
		
    	
    }
    
    public void deleteFlightSchedule(final String flightType,final Date flightDate ,final String flightNo){
    	if (StringUtils.hasText(flightType)&& StringUtils.hasText(flightNo)&& null!=flightDate){
    		getHibernateTemplate().execute(new HibernateCallback() {
    		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		    			StringBuffer hql = new StringBuffer("DELETE FROM ERP.BU_FLIGHT_SCHEDULE WHERE 1=1 ");
		    			hql.append(" AND FLIGHT_TYPE = :flightType");
		    			hql.append(" AND FLIGHT_DATE = :flightDate");
		    			hql.append(" AND FLIGHT_NO = :flightNo");
		    			
		    			Query query = session.createSQLQuery(hql.toString());
		    			query.setString("flightType", flightType);
		    			query.setDate("flightDate", flightDate);
		    			query.setString("flightNo", flightNo);
		    			return query.executeUpdate();
    		    	
    		    }
    		});
    	
    	}
		
    	
    }
}