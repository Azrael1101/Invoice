package tw.com.tm.erp.hbm.dao;


import java.util.List;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.UploadControl;
import tw.com.tm.erp.hbm.bean.UploadControlList;
import tw.com.tm.erp.hbm.SpringUtils;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
/**
 * A data access object (DAO) providing persistence and search support for
 * BuOrganization entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuOrganization
 * @author MyEclipse Persistence Tools
 */

public class UploadControlDAO extends BaseDAO {
	
	private NativeQueryDAO nativeQueryDAO;
	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}
	private static final Log log = LogFactory.getLog(UploadControlDAO.class);
	    public List<UploadControl> findByProperty(String[] fieldNames,Object[] fieldValue)
	    {
	    	List<UploadControl> uc = this.findByProperty("UploadControl", fieldNames, fieldValue);
	    	return uc;
	    }
	    public List<UploadControl> findLastSchedual(String[] fieldNames,Object[] fieldValue)
	    {
			boolean second = false ;
			StringBuffer queryString = new StringBuffer("from ");
			queryString.append(" UploadControl as model where ");
			for( String fieldName : fieldNames)
			{
				if(second)
				{
					queryString.append(" and ");
				}
				queryString.append(" model.");
				queryString.append(fieldName);
				queryString.append("= ? ");
				second = true ;
			}
			queryString.append(" order by schedualDate,schedual desc");
			return getHibernateTemplate().find(queryString.toString(), fieldValue);
	    }
	    public List<UploadControl> findThisSchedual(String orderTypeCode,String schedualDate,String schedual,String department)
	    {
			boolean second = false ;
			StringBuffer queryString = new StringBuffer("from ");
			queryString.append(" UploadControl as model where 1=1 ");
			queryString.append(" and model.orderTypeCode = '"+orderTypeCode+"' ");
			//queryString.append(" and model.schedualDate like '%"+schedualDate+"%' ");
			queryString.append(" and model.schedualDate <= TO_DATE( '"+schedualDate+"', 'YYYY/MM/DD')");
			if(department.equals("AC"))
			{
				queryString.append(" and model.statusByAC = 'OFF' ");
			}
			else if(department.equals("ED"))
			{
				queryString.append(" and model.statusByED = 'OFF' ");
			}
			else if(department.equals("BC"))
			{
				queryString.append(" and model.statusByBC = 'OFF' ");
			}
			else if(department.equals("SU"))
			{
				queryString.append(" and model.statusByBC = 'OFF' ");
			}
			queryString.append(" order by model.schedualDate,model.schedual ");
			//queryString.append(" and schedual = '"+schedual+"' ");
			return getHibernateTemplate().find(queryString.toString());
	    }	    
	    public List<UploadControl> findThisSchedual(HashMap conditionMap,int iSPage,int iPSize,String searchType)
	    {
	    	final String type = searchType;
	    	final int startIndex = iSPage * iPSize;
	    	final int rowCount = iPSize;
	    	final String deliveryControl = (String)conditionMap.get("deliveryControl");
	    	final String orderTypeCode = (String)conditionMap.get("orderTypeCode");
	    	final String schedualDate = (String)conditionMap.get("schedualDate");
	    	final String schedual = (String)conditionMap.get("schedual");
	    	final String department = (String)conditionMap.get("department");
	    	final String customsWarehouseCode = (String)conditionMap.get("customsWarehouseCode");
	    	log.info("==傳入參數==");
	    	log.info(deliveryControl);
	    	log.info(orderTypeCode);
	    	log.info(schedualDate);
	    	log.info(schedual);
	    	log.info(department);
	    	log.info(customsWarehouseCode);
	    	log.info("===========");
	    	List<UploadControl> result = getHibernateTemplate().executeFind(
	                new HibernateCallback() {
	                    public Object doInHibernate(Session session)
	                            throws HibernateException, SQLException {

	                        StringBuffer hql = new StringBuffer(" select model ");
	                        hql.append(" from UploadControl as model, CustomsContorl as model1  ");
	                        hql.append(" where 1=1 ");

	                        hql.append(" and model1.type = 'NF' ");
	                        if (!"Y".equals(deliveryControl))
	                        {
	                        	hql.append(" and model.orderTypeCode NOT IN ('DKP','DZN') ");
	                        }
	                        if (StringUtils.hasText(schedualDate))
	                        {
	                        	hql.append(" and model.schedualDate <= to_date( :schedualDate " );
	                        	hql.append(",'yyyy/mm/dd') " );
	                        }
	                        if (StringUtils.hasText(customsWarehouseCode))
	                        {
	                        	
	                        	if(customsWarehouseCode.equals("ALL"))
	                        	{
	                        		
	                        	}
	                        	else
	                        	{
	                        		hql.append(" and model1.customsWarehouseCode = '" + customsWarehouseCode + "' " );
	                        	}
	                        }
	                        if (StringUtils.hasText(department))
	                        {
	                        	if("BC".equals(department)||"SU".equals(department))
	                        	{
	                        		hql.append("and model.statusByBC = 'OFF' ");
	                        	}
	                        	else{
	                        		if("AC".equals(department))
	                        		{
	                        			hql.append("and model.statusByAC = 'OFF' ");
	                        		}
	                        		else if("ED".equals(department))
	                        		{
	                        			hql.append("and model.statusByED = 'OFF' ");
	                        		}
	                        		hql.append("and model1.status = 'ON' ");
	                        	}
	                        }
	                        else{
	                        	
	                        }
	                        hql.append(" and model.orderTypeCode = model1.orderTypeCode ");
	                        hql.append(" order by model.schedualDate,model.schedual,model.orderTypeCode " );
	                        Query query = session.createQuery(hql.toString());
	                        //query.setLockMode("model", LockMode.UPGRADE_NOWAIT);
	                        if("find".equals(type)){
	                        	query.setFirstResult(startIndex);
	                        	query.setMaxResults(rowCount);

	                        }
                        	log.info("總筆數");
	                        if (StringUtils.hasText(schedualDate))
	                            query.setString("schedualDate", schedualDate);


	                        log.info(hql.toString());
	                        log.info(query.toString());

	                        return query.list();
	                    }
	                });


       		 	log.info("搜尋結果:"+result.size());


	        return result;
	    }
		/*
		public List findWaitToSend(String model,String orderTypeCode,String schedule,String scheduleDate,String gateStatus) {
			StringBuffer queryString = new StringBuffer(" select model");
			queryString.append(" from "+model+" as model ");
			queryString.append(" , ImWarehouse as model1 ");
			queryString.append(" where 1=1 ");
			queryString.append(" and model.brandCode = 'T2' ");
			if(orderTypeCode.equals("DZN")||orderTypeCode.equals("DKP"))
			{
				queryString.append(" and model.orderTypeCode = 'DZN' ");
				if(orderTypeCode.equals("DZN")) 
					queryString.append(" and model.orderNo like '%DZN%' ");
				else if(orderTypeCode.equals("DKP"))
					queryString.append(" and model.orderNo like '%DKP%' ");
			}
			else
			{
				queryString.append(" and model.orderTypeCode = '"+orderTypeCode+"' ");
			}
			queryString.append(" and model.schedule = "+schedule);
			if(orderTypeCode.equals("SOP")) 
			{
				
				queryString.append(" and model.defaultWarehouseCode = model1.id.warehouseCode ");
				queryString.append(" and model1.storage != 'KD' ");
				queryString.append(" and model.salesOrderDate LIKE "+"to_DATE('"+scheduleDate+"','yyyy/mm/dd') ");
			}
			else
			{
				queryString.append(" and model.lastUpdateDate LIKE "+"to_DATE('"+scheduleDate+"','yyyy/mm/dd') ");
			}
			queryString.append(" and model.status = 'FINISH' ");
			queryString.append(" and model.customsStatus is null ");

			
			
			
			return getHibernateTemplate().find(queryString.toString());
		}
		*/

		public List findWaitToSend(HashMap conditionMap) {
			String model = (String)conditionMap.get("bean");
			String orgOrderTypeCode = (String)conditionMap.get("orgOrderTypeCode");
			String orderTypeCode = (String)conditionMap.get("orderType");
			String schedule = (String)conditionMap.get("findSchedual");
			String scheduleDate = (String)conditionMap.get("findSchedualDateString");
			String gateStatus = (String)conditionMap.get("statusByBC");
			String department = (String)conditionMap.get("department");
			String brandCode = (String)conditionMap.get("brandCode");
			String customsWarehouseCode = (String)conditionMap.get("customsWarehouseCode");
			
			log.info("bean:"+model);
			log.info("model:"+model);
			log.info("orgOrderTypeCode:"+orgOrderTypeCode);
			log.info("orderType:"+orderTypeCode);
			log.info("findSchedual:"+schedule);
			log.info("findSchedualDateString:"+scheduleDate);
			log.info("statusByBC:"+gateStatus);
			log.info("department:"+department);
			log.info("brandCode:"+brandCode);
			log.info("customsWarehouseCode:"+customsWarehouseCode);
			
			
			
			
			
			boolean hasCustomsWarehousCode = !(null==customsWarehouseCode || "".equals(customsWarehouseCode));

			StringBuffer queryString = new StringBuffer(" select HEAD.* ");
			queryString.append(" from "+model+" HEAD ");
			if(hasCustomsWarehousCode) 
			{
				queryString.append(" , IM_WAREHOUSE model1 ");
			}
			queryString.append(" where 1=1 ");
			queryString.append(" and HEAD.BRAND_CODE = '"+brandCode+"' ");
			queryString.append(" and HEAD.ORDER_NO not like '%TMP%' ");//非暫存單
			queryString.append(" and HEAD.ORDER_TYPE_CODE = '"+orgOrderTypeCode+"' ");//單別
			queryString.append(" and HEAD.SCHEDULE = "+schedule);//班別
			if(orderTypeCode.indexOf("@")>=0)
			{
				queryString.append(" and HEAD.STATUS = 'VOID' ");//作廢單據
			}
			else{
				queryString.append(" and HEAD.STATUS IN ('CLOSE','FINISH') ");//完成或結案單據
			}
			queryString.append(" and HEAD.CUSTOMS_STATUS is null ");//海關回應狀態為空
			//若有單據分流則判斷關別
			if(hasCustomsWarehousCode) 
			{
				queryString.append(" and HEAD.DEFAULT_WAREHOUSE_CODE = model1.WAREHOUSE_CODE ");
				queryString.append(" and model1.CUSTOMS_WAREHOUSE_CODE = '"+customsWarehouseCode+"' ");
			}
			//提貨單
			if(orgOrderTypeCode.equals("DZN"))
			{
				queryString.append(" and HEAD.ORDER_TYPE_CODE = '"+orderTypeCode+"' ");
				queryString.append(" and HEAD.ORDER_NO like '%"+orderTypeCode+"%' ");
			}
			//銷售單
			if(orgOrderTypeCode.equals("SOP")) 
			{	
			//入提單位僅確認有入提單號者
				if(department.equals("ED"))
				{
					queryString.append(" and HEAD.LADING_NO is not null ");
				}
			//銷售單拉銷售時間
				queryString.append(" and HEAD.SALES_ORDER_DATE >= "+"to_DATE('"+scheduleDate+"','yyyy/mm/dd') ");
				queryString.append(" and HEAD.SALES_ORDER_DATE <= "+"to_DATE('"+scheduleDate+"','yyyy/mm/dd') ");

			//排除無海關上傳單號
				queryString.append(" and HEAD.CUSTOMS_NO IS NOT NULL ");

			}
			else
			{
			//銷售單以外拉最後更新時間
				queryString.append(" and HEAD.LAST_UPDATE_DATE LIKE "+"TO_DATE('"+scheduleDate+"','yyyy/mm/dd') ");
			}



			log.info("SQL"+queryString.toString());
			List result = nativeQueryDAO.executeNativeSql(queryString.toString());
			
			
			
			
			
			return result;
		}
		
		
		
		public List findWaitToSend(String model,String orderTypeCode,String schedule,String scheduleDate,String gateStatus,String department) {
			StringBuffer queryString = new StringBuffer(" select model");
			queryString.append(" from "+model+" as model ");
			if(orderTypeCode.equals("SOP")||orderTypeCode.equals("SOPHD")||
					orderTypeCode.equals("AEG")||orderTypeCode.equals("AEGHD")||
						orderTypeCode.equals("IBT")||orderTypeCode.equals("IBTHD")||
							orderTypeCode.equals("IRP")||orderTypeCode.equals("IRPHD")) 
			{
				queryString.append(" , ImWarehouse as model1 ");
			}
			queryString.append(" where 1=1 ");
			queryString.append(" and model.brandCode = 'T2' ");
			queryString.append(" and model.orderNo not like '%TMP%' ");
			if(orderTypeCode.equals("DZN")||orderTypeCode.equals("DKP"))
			{
				queryString.append(" and model.orderTypeCode = 'DZN' ");
				if(orderTypeCode.equals("DZN")) 
					queryString.append(" and model.orderNo like '%DZN%' ");
				else if(orderTypeCode.equals("DKP"))
					queryString.append(" and model.orderNo like '%DKP%' ");

			}
			if(orderTypeCode.equals("SOP")||orderTypeCode.equals("SOPHD")||
					orderTypeCode.equals("AEG")||orderTypeCode.equals("AEGHD")||
						orderTypeCode.equals("IBT")||orderTypeCode.equals("IBTHD")||
							orderTypeCode.equals("IRP")||orderTypeCode.equals("IRPHD")) 
			{
				if(orderTypeCode.equals("SOP")||orderTypeCode.equals("AEG")||orderTypeCode.equals("IBT")||orderTypeCode.equals("IRP"))
				{
					queryString.append(" and model.defaultWarehouseCode = model1.warehouseCode ");
					queryString.append(" and model1.storage = 'FD' ");
				}
				else if(orderTypeCode.equals("SOPHD")||orderTypeCode.equals("AEGHD")||orderTypeCode.equals("IBTHD")||orderTypeCode.equals("IRPHD"))
				{
					queryString.append(" and model.defaultWarehouseCode = model1.warehouseCode ");
					queryString.append(" and model1.storage = 'HD' ");
				}

			}
			else
			{

				queryString.append(" and model.orderTypeCode = '"+orderTypeCode+"' ");
			}
			queryString.append(" and model.schedule = "+schedule);
			if(orderTypeCode.equals("SOP")||orderTypeCode.equals("SOPHD")) 
			{
				queryString.append(" and model.orderTypeCode = 'SOP' ");
				if(department.equals("ED"))
				{
					queryString.append(" and model.ladingNo is not null ");
				}
				queryString.append(" and model.salesOrderDate >= "+"to_DATE('"+scheduleDate+"','yyyy/mm/dd') ");
				queryString.append(" and model.salesOrderDate <= "+"to_DATE('"+scheduleDate+"','yyyy/mm/dd') ");

			}
			else
			{
				if(orderTypeCode.equals("IBT")||orderTypeCode.equals("IBTHD")) 
				{
					queryString.append(" and model.orderTypeCode = 'IBT' ");
				}
				else if(orderTypeCode.equals("IRP")||orderTypeCode.equals("IRPHD")) 
				{
					queryString.append(" and model.orderTypeCode = 'IRP' ");
				}
				else if(orderTypeCode.equals("AEG")||orderTypeCode.equals("AEGHD")) 
				{
					queryString.append(" and model.orderTypeCode = 'AEG' ");
				}
				queryString.append(" and model.lastUpdateDate LIKE "+"to_DATE('"+scheduleDate+"','yyyy/mm/dd') ");


			}
			queryString.append(" and model.status IN ('CLOSE','FINISH') ");
			queryString.append(" and model.customsStatus is null ");
			log.info("SQL"+queryString.toString());
			
			
			
			return getHibernateTemplate().find(queryString.toString());
		}
		
		
		
		
		
		
		
		
		/*
		public List<SoSalesOrderHead> getSoSalesOrderHeads() {
			ApplicationContext context = SpringUtils.getApplicationContext();
			SessionFactory ss = (SessionFactory) context.getBean("SessionFactory98101");
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			//cal.add(Calendar.HOUR_OF_DAY, -5);
			cal.add(Calendar.MINUTE, -30);
			Date date = cal.getTime();
			System.out.println("現在時間減30分="+date);
			
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");		
			String  nowDate = df.format(new Date());
			String  befDate = df.format(date);			
			
			StringBuffer sql = new StringBuffer();
			sql.append("from SoSalesOrderHead where lastUpdateDate < TO_DATE('").append(nowDate).append("','YYYY/MM/DD HH24:MI:SS') ");
			sql.append("and lastUpdateDate > TO_DATE('").append(befDate).append("','YYYY/MM/DD HH24:MI:SS') ");
			sql.append("and orderTypeCode = 'SOP' ");
			sql.append("and orderNo not like '%TMP%' ");
			sql.append("and status not in ('FINISH','CLOSE') ");
			sql.append("and brandCode = 'T2' ");
			sql.append("order by headId");
			System.out.println("!!!!!!!!!!!!!!!!!================================sql:"+sql.toString());
			this.setSessionFactory(ss);
			List<SoSalesOrderHead> result = getHibernateTemplate().find(sql.toString());
			return result;
		}

		
	    
	    public List<SoSalesOrderHead> getSoSalesOrderHeadsSetMax(int maxResults) {
	    	ApplicationContext context = SpringUtils.getApplicationContext();
	    	SessionFactory SessionFactory98101 = (SessionFactory) context.getBean("SessionFactory98101");
	    	this.setSessionFactory(SessionFactory98101);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MINUTE, -30);
			Date date = cal.getTime();
			
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");		
			String  nowDate = df.format(new Date());
			String  befDate = df.format(date);			
			
			StringBuffer sql = new StringBuffer();
			sql.append("from SoSalesOrderHead where 1=1 ");
			//sql.append("and lastUpdateDate < TO_DATE('").append(nowDate).append("','YYYY/MM/DD HH24:MI:SS') ");
			//sql.append("and lastUpdateDate > TO_DATE('").append(befDate).append("','YYYY/MM/DD HH24:MI:SS') ");
			sql.append("and reserve2 = 'W' ");
			sql.append("and orderTypeCode = 'SOP' ");
			sql.append("and orderNo not like '%TMP%' ");
			sql.append("and brandCode = 'T2' ");
			sql.append("order by headId ");
			
			HibernateTemplate ht = getHibernateTemplate();
			ht.setMaxResults(maxResults);
			List<SoSalesOrderHead> result = ht.find(sql.toString());
			return result;
		}
	    		*/
}