package tw.com.tm.erp.hbm.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.SiResend;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryMoveHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.service.BuBasicDataService;
import tw.com.tm.erp.utils.DateUtils;

public class SoSalesOrderHeadDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(SoSalesOrderHeadDAO.class);
    
    BuBasicDataService buBasicDataService;
    private BuShopMachineDAO buShopMachineDAO;
    private SiResendDAO siResendDAO;

    /**
     * 依據銷貨單查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     */
    public List<SoSalesOrderHead> findSalesOrderList(HashMap conditionMap) {

	final String brandCode = (String) conditionMap.get("brandCode");
	final String orderTypeCode = (String) conditionMap.get("orderTypeCode");
	final String startOrderNo = (String) conditionMap.get("startOrderNo");
	final String endOrderNo = (String) conditionMap.get("endOrderNo");
	final String status = (String) conditionMap.get("status");
	final String customerCode = (String) conditionMap.get("customerCode");
	final String superintendentCode = (String) conditionMap
		.get("superintendentCode");
	final String defaultWarehouseCode = (String) conditionMap
		.get("defaultWarehouseCode");
	final Date scheduleShipDate = (Date) conditionMap
		.get("scheduleShipDate");
	final Date salesOrderStartDate = (Date) conditionMap
		.get("salesOrderStartDate");
	final Date salesOrderEndDate = (Date) conditionMap
		.get("salesOrderEndDate");
	final Date scheduleCollectionStartDate = (Date) conditionMap
		.get("scheduleCollectionStartDate");
	final Date scheduleCollectionEndDate = (Date) conditionMap
		.get("scheduleCollectionEndDate");

	List<SoSalesOrderHead> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer(
				"from SoSalesOrderHead as model ");
			hql.append("where model.brandCode = :brandCode and substring(model.orderNo,1,3) <> 'TMP'");
			if (StringUtils.hasText(orderTypeCode))
			    hql.append(" and model.orderTypeCode = :orderTypeCode");

			if (StringUtils.hasText(startOrderNo))
			    hql.append(" and model.orderNo >= :startOrderNo");

			if (StringUtils.hasText(endOrderNo))
			    hql.append(" and model.orderNo <= :endOrderNo");

			if (StringUtils.hasText(status))
			    hql.append(" and model.status = :status");

			if (StringUtils.hasText(customerCode))
			    hql.append(" and model.customerCode = :customerCode");

			if (StringUtils.hasText(superintendentCode))
			    hql.append(" and model.superintendentCode = :superintendentCode");

			if (StringUtils.hasText(defaultWarehouseCode))
			    hql.append(" and model.defaultWarehouseCode = :defaultWarehouseCode");

			if (scheduleShipDate != null)
			    hql.append(" and model.scheduleShipDate = :scheduleShipDate");

			if (salesOrderStartDate != null)
			    hql.append(" and model.salesOrderDate >= :salesOrderStartDate");

			if (salesOrderEndDate != null)
			    hql.append(" and model.salesOrderDate <= :salesOrderEndDate");

			if (scheduleCollectionStartDate != null)
			    hql.append(" and model.scheduleCollectionDate >= :scheduleCollectionStartDate");

			if (scheduleCollectionEndDate != null)
			    hql.append(" and model.scheduleCollectionDate <= :scheduleCollectionEndDate");
			
			hql.append(" order by model.orderNo desc");
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			query.setString("brandCode", brandCode);

			if (StringUtils.hasText(orderTypeCode))
			    query.setString("orderTypeCode", orderTypeCode);

			if (StringUtils.hasText(startOrderNo))
			    query.setString("startOrderNo", startOrderNo);

			if (StringUtils.hasText(endOrderNo))
			    query.setString("endOrderNo", endOrderNo);

			if (StringUtils.hasText(status))
			    query.setString("status", status);

			if (StringUtils.hasText(customerCode))
			    query.setString("customerCode", customerCode);

			if (StringUtils.hasText(superintendentCode))
			    query.setString("superintendentCode",
				    superintendentCode);

			if (StringUtils.hasText(defaultWarehouseCode))
			    query.setString("defaultWarehouseCode", defaultWarehouseCode);

			if (scheduleShipDate != null)
			    query.setDate("scheduleShipDate", scheduleShipDate);

			if (salesOrderStartDate != null)
			    query.setDate("salesOrderStartDate",
				    salesOrderStartDate);

			if (salesOrderEndDate != null)
			    query.setDate("salesOrderEndDate",
				    salesOrderEndDate);

			if (scheduleCollectionStartDate != null)
			    query.setDate("scheduleCollectionStartDate",
				    scheduleCollectionStartDate);

			if (scheduleCollectionEndDate != null)
			    query.setDate("scheduleCollectionEndDate",
				    scheduleCollectionEndDate);

			return query.list();
		    }
		});

	return result;
    }
    
    /**
     * 依據品牌代號、單別、專櫃代號、銷售日期、狀態進行查詢
     * 
     * @param conditionMap
     * @return SoSalesOrderHead
     */
    public List<SoSalesOrderHead> findSalesOrderByProperty(HashMap conditionMap) {
	
	String brandCode= (String)conditionMap.get("brandCode");
	String orderTypeCode = (String) conditionMap.get("orderTypeCode");
	String shopCode = (String)conditionMap.get("shopCode");
	Date transactionDate = (Date)conditionMap.get("transactionDate");
	String soStatus = (String)conditionMap.get("soStatus");
	    
	StringBuffer hql = new StringBuffer("from SoSalesOrderHead as model where model.brandCode = ?");	
	hql.append(" and model.orderTypeCode = ?");
	hql.append(" and model.salesOrderDate <= ?");
        hql.append(" and model.status = ?");
        if (StringUtils.hasText(shopCode)) {
	    hql.append(" and model.shopCode = ?");		    
	}
        Object[] objArray = null;
        if (StringUtils.hasText(shopCode)) {
            objArray = new Object[] { brandCode, orderTypeCode, transactionDate, soStatus, shopCode};
        }else{
            objArray = new Object[] { brandCode, orderTypeCode, transactionDate, soStatus};
        }
                    
        List<SoSalesOrderHead> result = getHibernateTemplate().find(hql.toString(), objArray);
        
	return result;
    }
    
    /**
     * 依據品牌代號、單別、專櫃代號、銷售日期、狀態查詢出貨單及銷貨單
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List findSalesOrderAndDeliveryByProperty(HashMap conditionMap){
	
	final String brandCode= (String)conditionMap.get("brandCode");
	final String orderTypeCode = (String) conditionMap.get("orderTypeCode");
	final String shopCode = (String)conditionMap.get("shopCode");
	final Date transactionDate = (Date)conditionMap.get("transactionDate");
	final Date lastTransactionDate = (Date)conditionMap.get("lastTransactionDate");
	final String soStatus = (String)conditionMap.get("soStatus");
	final String restrictPageCount = (String) conditionMap.get("restrictPageCount");
	
	List<SoSalesOrderHead> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("select s, d from SoSalesOrderHead as s, ImDeliveryHead as d ");
			hql.append("where s.brandCode = d.brandCode and s.shopCode = d.shopCode and s.headId = d.salesOrderId");
			hql.append(" and s.brandCode = :brandCode");
			hql.append(" and s.orderTypeCode = :orderTypeCode");
			hql.append(" and s.salesOrderDate >= :transactionDate");
			hql.append(" and s.salesOrderDate <= :lastTransactionDate");
					
			if (StringUtils.hasText(shopCode)){
			    hql.append(" and s.shopCode = :shopCode");
			}
			
			if(StringUtils.hasText(soStatus)){			    
		            hql.append(" and s.status in " + soStatus);
			}

			Query query = session.createQuery(hql.toString());
			if (StringUtils.hasText(restrictPageCount)) {
			    query.setFirstResult(0);
		            query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);		    
			}
			query.setString("brandCode", brandCode);
			query.setString("orderTypeCode", orderTypeCode);
			query.setDate("transactionDate", transactionDate);
			query.setDate("lastTransactionDate", lastTransactionDate);
			
			if (StringUtils.hasText(shopCode))
			    query.setString("shopCode", shopCode);

			return query.list();
		    }
		});

	return result;
    }

    /**
     * 依據品牌代號、單別、單號查詢銷貨單
     * 
     * @param brandCode
     * @param orderTypeCode
     * @param orderNo
     * @return SoSalesOrderHead
     */
    public SoSalesOrderHead findSalesOrderByIdentification(String brandCode, String orderTypeCode, String orderNo) {
	
	StringBuffer hql = new StringBuffer("from SoSalesOrderHead as model where model.brandCode = ?");
        hql.append(" and model.orderTypeCode = ?");
        hql.append(" and model.orderNo = ?");
        List<SoSalesOrderHead> result = getHibernateTemplate().find(hql.toString(),
		new Object[] { brandCode, orderTypeCode,  orderNo});
	return (result != null && result.size() > 0 ? result.get(0) : null);
    }
    
    public List<SoSalesOrderHead> findSalesOrderByCriteria(final String[] brandCodeArray, final Date salesOrderDate, final String[] statusArray) {
	
	final Date orderDate = DateUtils.getShortDate(salesOrderDate);
	List<SoSalesOrderHead> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			Criteria ctr = session.createCriteria(SoSalesOrderHead.class);
			ctr.add(Expression.le("salesOrderDate", orderDate));
			ctr.add(Expression.in("brandCode", brandCodeArray));
			ctr.add(Expression.in("status", statusArray));			
			ctr.addOrder(Order.asc("brandCode"));
			ctr.addOrder(Order.asc("orderTypeCode"));
			ctr.addOrder(Order.asc("orderNo"));

			return ctr.list();
		    }
		});

	return result;
    }
    
    /**
     * 依據品牌代號、單別、專櫃代號、銷售日期、識別碼、原始單號查詢SOP
     * 
     * @param conditionMap
     * @return List<SoSalesOrderHead>
     */
    public List<SoSalesOrderHead> findSOPByProperty(HashMap conditionMap) {
	
	String brandCode= (String)conditionMap.get("brandCode");
	String orderTypeCode = (String) conditionMap.get("orderTypeCode");
	String shopCode = (String)conditionMap.get("actualShopCode");
	String msNo = (String)conditionMap.get("msNo");
	String identification = (String)(String)conditionMap.get("identification");
	String fileKey = (String)conditionMap.get("fileKey");
	Date salesDate = (Date)conditionMap.get("actualSalesDate");	
	    
	StringBuffer hql = new StringBuffer("from SoSalesOrderHead as model where model.brandCode = ?");	
	hql.append(" and model.orderTypeCode = ?");
	hql.append(" and model.salesOrderDate = ?");
	hql.append(" and model.shopCode = ?");
	hql.append(" and model.reserve5 = ?");	
        if (StringUtils.hasText(msNo)) {
            hql.append(" and model.customerPoNo = ?");			    
	}
        if (StringUtils.hasText(fileKey)) {
            hql.append(" and model.reserve3 = ?");			    
	}
        Object[] objArray = null;
        if (StringUtils.hasText(msNo)) {
            objArray = new Object[] { brandCode, orderTypeCode, salesDate, shopCode, identification, msNo};
        }else if(StringUtils.hasText(fileKey)){
            objArray = new Object[] { brandCode, orderTypeCode, salesDate, shopCode, identification, fileKey};
        }else{
            objArray = new Object[] { brandCode, orderTypeCode, salesDate, shopCode, identification};
        }
                    
        List<SoSalesOrderHead> result = getHibernateTemplate().find(hql.toString(), objArray);
        
	return result;
    }
    
    /**
     * 依據品牌代號、單別、專櫃代號or機台、銷售日期、狀態進行查詢
     * 
     * @param conditionMap
     * @return SoSalesOrderHead
     */
    public List<SoSalesOrderHead> findSalesOrderByPropertyForPOS(HashMap conditionMap) {
	
	String brandCode= (String)conditionMap.get("brandCode");
	String orderTypeCode = (String) conditionMap.get("orderTypeCode");
	String salesUnit = (String)conditionMap.get("salesUnit");
	Date transactionDate = (Date)conditionMap.get("transactionEndDate");
	String soStatus = (String)conditionMap.get("soStatus");
	    
	StringBuffer hql = new StringBuffer("from SoSalesOrderHead as model where model.brandCode = ?");	
	hql.append(" and model.orderTypeCode = ?");
	hql.append(" and model.salesOrderDate <= ?");
        hql.append(" and model.status = ?");
        if (StringUtils.hasText(salesUnit)) {
            if(!"T2".equals(brandCode)){
	        hql.append(" and model.shopCode = ?");
            }else{
        	hql.append(" and model.posMachineCode = ?");
            }
	}
        Object[] objArray = null;
        if (StringUtils.hasText(salesUnit)) {
            objArray = new Object[] { brandCode, orderTypeCode, transactionDate, soStatus, salesUnit};
        }else{
            objArray = new Object[] { brandCode, orderTypeCode, transactionDate, soStatus};
        }
                    
        List<SoSalesOrderHead> result = getHibernateTemplate().find(hql.toString(), objArray);
        
	return result;
    }
    
    /**
     * 依據品牌代號、單別、專櫃代號、銷售日期、狀態查詢出貨單及銷貨單
     * 
     * @param conditionMap
     * @return List
     * @throws Exception 
     * @throws Exception
     */
    public List findSalesOrderAndDeliveryByPropertyForPOS(HashMap conditionMap) throws Exception{
    	log.info("--Start---");
    	final String brandCode= (String)conditionMap.get("brandCode");
    	final String orderTypeCode = (String) conditionMap.get("orderTypeCode");
    	final String salesUnitType = (String)conditionMap.get("salesUnitType");
    	final String postingSalesUnitBegin = (String) conditionMap.get("postingSalesUnitBegin");
    	final String postingSalesUnitEnd = (String) conditionMap.get("postingSalesUnitEnd");
    	final Date transactionDate = (Date)conditionMap.get("transactionDate");
    	final Date lastTransactionDate = (Date)conditionMap.get("lastTransactionDate");
    	final String soStatus = (String)conditionMap.get("soStatus");
    	final String isDesignate = (String)conditionMap.get("isDesignate");
    	final String employeeCode = (String) conditionMap.get("employeeCode");
    	final String batch = (String) conditionMap.get("batch");
    	//查詢人員所屬機台/專櫃
    	StringBuffer shopMachinesByEMP = new StringBuffer("");
    	List<BuShopMachine> allShopMachines = new ArrayList();//buBasicDataService.getShopMachineForEmployee(brandCode, employeeCode, "Y");

    	allShopMachines = buShopMachineDAO.getShopMachineForEmployee(brandCode,
    			employeeCode, "Y", salesUnitType, postingSalesUnitBegin, postingSalesUnitEnd);

    	if(allShopMachines != null && allShopMachines.size()>0){
    		for(int i=0;i<allShopMachines.size();i++){
    			if(!"T2".equals(brandCode)){
    				shopMachinesByEMP.append("'" + (allShopMachines.get(i)).getId().getShopCode() + "', ");
    			}else{
    				shopMachinesByEMP.append("'" + (allShopMachines.get(i)).getId().getPosMachineCode() + "', ");
    			}				    
    		}
    	}
    	
    	final String allShopMachineByEMP = shopMachinesByEMP.replace(shopMachinesByEMP.lastIndexOf(", "), shopMachinesByEMP.length(), "").toString();
    	
    	List<SoSalesOrderHead> result = getHibernateTemplate().executeFind(
    			new HibernateCallback() {
    				public Object doInHibernate(Session session)
    				throws HibernateException, SQLException {

    					StringBuffer hql = new StringBuffer("select s, d from SoSalesOrderHead as s, ImDeliveryHead as d ");
    					hql.append("where s.brandCode = d.brandCode and s.shopCode = d.shopCode and s.headId = d.salesOrderId");
    					hql.append(" and s.brandCode = :brandCode");
    					hql.append(" and s.orderTypeCode = :orderTypeCode");

    					if("Y".equals(isDesignate)){
    						hql.append(" and s.salesOrderDate = :transactionDate");
    					}else{			
    						hql.append(" and s.salesOrderDate >= :transactionDate");
    						hql.append(" and s.salesOrderDate <= :lastTransactionDate");
    					}

    					if(!"T2".equals(brandCode)){
    						if (!StringUtils.hasText(postingSalesUnitBegin) && !StringUtils.hasText(postingSalesUnitEnd)){
    							hql.append(" and s.shopCode IN (" + allShopMachineByEMP + ")");
    						}else{
    							if(StringUtils.hasText(postingSalesUnitBegin)){
    								hql.append(" and s.shopCode >= :postingSalesUnitBegin ");
    							}
    							if(StringUtils.hasText(postingSalesUnitEnd)){
    								hql.append(" and s.shopCode <= :postingSalesUnitEnd ");
    							}
    						}
    					}
    					else{
    						hql.append(" and s.posMachineCode IN (" + allShopMachineByEMP + ")");
    						if(StringUtils.hasText(batch))
    							hql.append(" and s.schedule = :batch ");
    					}

    					if(StringUtils.hasText(soStatus)){			    
    						hql.append(" and s.status in " + soStatus);
    					}

    					Query query = session.createQuery(hql.toString());
    					query.setString("brandCode", brandCode);
    					query.setString("orderTypeCode", orderTypeCode);
    					query.setDate("transactionDate", transactionDate);
    					if(!"Y".equals(isDesignate)){
    						query.setDate("lastTransactionDate", lastTransactionDate);
    					}
//MACO 百貨POS反過帳問題修正    					
//    					if(StringUtils.hasText(batch))
//    						query.setString("batch", batch);
    					if(!"T2".equals(brandCode)){
    						if(StringUtils.hasText(postingSalesUnitBegin)){
    							query.setString("postingSalesUnitBegin", postingSalesUnitBegin);
    						}
    						if(StringUtils.hasText(postingSalesUnitEnd)){
    							query.setString("postingSalesUnitEnd", postingSalesUnitEnd);
    						}
    					}
    					else{
    						//MACO 百貨POS反過帳問題修正    		
        					if(StringUtils.hasText(batch))
        						query.setString("batch", batch);
    					}
    					
    					log.info("SQL:"+query.toString());
    					return query.list();
    				}
    			});
    	return result;
    }
    public List findSalesOrderAndDeliveryByPropertyForOrderNo(HashMap conditionMap) throws Exception{
    	log.info("--Start---");
    	final String brandCode= (String)conditionMap.get("brandCode");
    	final String orderTypeCode = (String) conditionMap.get("orderTypeCode");
    	final String orderNo = (String) conditionMap.get("orderNo");

    	//final String soStatus = (String)conditionMap.get("soStatus");
    	log.info("==========");
    	log.info("brandCode:"+brandCode);
    	log.info("orderTypeCode"+orderTypeCode);
    	log.info("orderNo"+orderNo);
    	log.info("==========");


    	List<SoSalesOrderHead> result = getHibernateTemplate().executeFind(
    			new HibernateCallback() {
    				public Object doInHibernate(Session session)
    				throws HibernateException, SQLException {

    					StringBuffer hql = new StringBuffer(" select s, d ");
    					hql.append(" from SoSalesOrderHead as s, ImDeliveryHead as d ");
    					hql.append(" where 1=1 ");
    					hql.append(" and s.brandCode = d.brandCode ");
    					hql.append(" and s.shopCode = d.shopCode ");
    					hql.append(" and s.headId = d.salesOrderId ");
    					
    					hql.append(" and s.brandCode = :brandCode ");
    					hql.append(" and s.orderTypeCode = :orderTypeCode ");
    					hql.append(" and s.orderNo = :orderNo ");

/*    					if(StringUtils.hasText(soStatus))
    					{			    
    						hql.append(" and s.status in " + soStatus);
    					}*/

    					Query query = session.createQuery(hql.toString());
    					query.setString("brandCode", brandCode);
    					query.setString("orderNo", orderNo);
    					query.setString("orderTypeCode", orderTypeCode);

    					log.info("SQL:"+query.toString());
    					return query.list();
    				}
    			});
    	return result;
    }
    /**
     * 依據品牌代號、單別、POS機碼、銷售日期、識別碼、原始單號查詢SOP
     * 
     * @param conditionMap
     * @return List<SoSalesOrderHead>
     */
    public List<SoSalesOrderHead> findT2SOPByProperty(HashMap conditionMap) {
	
	String brandCode= (String)conditionMap.get("brandCode");
	String orderTypeCode = (String) conditionMap.get("orderTypeCode");
	String posMachineCode = (String)conditionMap.get("posMachineCode");
	String identification = (String)(String)conditionMap.get("identification");
	String fileKey = (String)conditionMap.get("fileKey");
	Date salesDate = (Date)conditionMap.get("actualSalesDate");	
	    
	StringBuffer hql = new StringBuffer("from SoSalesOrderHead as model where model.brandCode = ?");	
	hql.append(" and model.orderTypeCode = ?");
	hql.append(" and model.salesOrderDate = ?");
	hql.append(" and model.posMachineCode = ?");
	hql.append(" and model.reserve5 = ?");
        if (StringUtils.hasText(fileKey)) {
            hql.append(" and model.reserve3 = ?");			    
	}
        Object[] objArray = null;
        if(StringUtils.hasText(fileKey)){
            objArray = new Object[] { brandCode, orderTypeCode, salesDate, posMachineCode, identification, fileKey};
        }else{
            objArray = new Object[] { brandCode, orderTypeCode, salesDate, posMachineCode, identification};
        }
                    
        List<SoSalesOrderHead> result = getHibernateTemplate().find(hql.toString(), objArray);
        
	return result;
    }
    
    /**
     * 依據品牌代號、POS機碼、銷售日期、售貨單號起迄
     * 
     * @param conditionMap
     * @return List<SoSalesOrderHead>
     */
    public List<SoSalesOrderHead> findCustomerPoNo(Map conditionMap) {
	
	String brandCode= (String)conditionMap.get("brandCode");
	String posMachineCode = (String)conditionMap.get("posMachineCode");
	Date salesOrderDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String)conditionMap.get("salesOrderDate") );	
	String customerPoNoStart = (String)conditionMap.get("customerPoNoStart");
	String customerPoNoEnd = (String)conditionMap.get("customerPoNoEnd");
	return findByProperty("SoSalesOrderHead", "", "and brandCode = ? and posMachineCode = ? and salesOrderDate = ? and customerPoNo >= ? and customerPoNo <= ? ", 
		new Object[]{ brandCode, posMachineCode, salesOrderDate, customerPoNoStart, customerPoNoEnd},
		" order by customerPoNo ");
    }
    
    /**
     * 依據品牌代號、單別、專櫃代號、銷售日期、狀態進行查詢For test15
     * 
     * @param conditionMap
     * @return SoSalesOrderHead
     */
    public List<SoSalesOrderHead> getPOSByProperty(HashMap conditionMap) {
	
	final String brandCode= (String)conditionMap.get("brandCode");
	final String orderTypeCode = (String) conditionMap.get("orderTypeCode");
	final Date actualSalesDate = (Date)conditionMap.get("actualSalesDate");
	final String status = (String)conditionMap.get("status");
	final String posMachineCode = (String)conditionMap.get("posMachineCode");
	
	List<SoSalesOrderHead> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {
	                StringBuffer hql = new StringBuffer("from SoSalesOrderHead as model where model.brandCode = :brandCode");	
	                hql.append(" and model.orderTypeCode = :orderTypeCode");
	                hql.append(" and model.salesOrderDate = :salesOrderDate");
	                //hql.append(" and model.reserve5 = :reserve5");
                        hql.append(" and model.status = :status");
                        if(StringUtils.hasText(posMachineCode)){
                            hql.append(" and model.posMachineCode = :posMachineCode");
                        }
        
                        Query query = session.createQuery(hql.toString());
                        query.setFirstResult(0);
		        query.setMaxResults(1000);
		        query.setString("brandCode", brandCode);
			query.setString("orderTypeCode", orderTypeCode);
			query.setDate("salesOrderDate", actualSalesDate);
			//query.setString("reserve5", "POS");
			query.setString("status", status);
			if(StringUtils.hasText(posMachineCode)){
			    query.setString("posMachineCode", posMachineCode);
                        }
                        
			return query.list();
		    }
		});
	return result;
    }

    public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
        this.buBasicDataService = buBasicDataService;
    }

    public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
        this.buShopMachineDAO = buShopMachineDAO;
    }
    
    public void setSiResendDAO(SiResendDAO siResendDAO) {
        this.siResendDAO = siResendDAO;
    }
    
    /**
     * 依據品牌代號、POS機碼、銷售日期、交易序號 or DATA_ID (擇一)找尋銷售單
     * 
     * @param conditionMap
     * @return List<SoSalesOrderHead>
     */
    public List<SoSalesOrderHead> findOnlineSoBean(HashMap conditionMap) {
	
	String brandCode= (String)conditionMap.get("BRAND_CODE");
	Date salesOrderDate = (Date)conditionMap.get("salesOrderDate");
	String posMachineCode = (String)conditionMap.get("MACHINE_CODE");
	String dataId = (String)conditionMap.get("DATA_ID");
	String transactionSeqNo = (String)conditionMap.get("transactionSeqNo");
	StringBuffer hql = new StringBuffer("from SoSalesOrderHead as model where model.brandCode = ?");	
	hql.append(" and model.salesOrderDate = ?");
	hql.append(" and model.posMachineCode = ?");
	Object[] objArray = null;
	if(StringUtils.hasText(transactionSeqNo)){
		hql.append(" and model.transactionSeqNo = ?");
		objArray = new Object[] { brandCode, salesOrderDate, posMachineCode, transactionSeqNo };		
	}	else if (StringUtils.hasText(dataId)){
		hql.append(" and model.reserve4 = ?");
		objArray = new Object[] { brandCode, salesOrderDate, posMachineCode, dataId };
	}else{
		objArray = new Object[] { brandCode, salesOrderDate, posMachineCode};
	}
	//log.info("===================findOnlineSoBean hql.toString() : "+hql.toString()+"===================");
	//log.info("===================findOnlineSoBean brandCode : "+brandCode+"===================");
	//log.info("===================findOnlineSoBean salesOrderDate : "+salesOrderDate+"===================");
	//log.info("===================findOnlineSoBean posMachineCode : "+posMachineCode+"===================");
	//log.info("===================findOnlineSoBean transactionSeqNo : "+transactionSeqNo+"===================");
	//log.info("===================findOnlineSoBean dataId : "+dataId+"===================");
	List<SoSalesOrderHead> result = getHibernateTemplate().find(hql.toString(), objArray);
	return result;
    }
    
    public List<SoSalesOrderHead> findByCustomerPoNo(String brandCode, String customerPoNo) {
    	StringBuffer hql = new StringBuffer("from SoSalesOrderHead as model where model.brandCode = ?");	
    	hql.append(" and model.customerPoNo = ?");
    	Object[] objArray = null;
    	objArray = new Object[] { brandCode, customerPoNo };		
        List<SoSalesOrderHead> result = getHibernateTemplate().find(hql.toString(), objArray);
            
    	return result;
        }
    
	public SoSalesOrderHead findByOrderNo(java.lang.Long id) {
		log.debug("getting SoSalesOrderHead instance with id: " + id);
		try {
			SoSalesOrderHead instance = (SoSalesOrderHead) getSession()
					.get("tw.com.tm.erp.hbm.bean.SoSalesOrderHead", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	//搜尋未確單 是T2 SOP的
	 public List<SiResend> findByIsLock(String isLock) {
		 String status = "NOTOK";
		 StringBuffer hql = new StringBuffer("from SiResend as model where ROWNUM <501 and model.isLock = ? ");
		 hql.append(" and model.status = ?");
		 Object[] objArray = null;
    	 objArray = new Object[] { isLock , status};
         List<SiResend> result = getHibernateTemplate().find(hql.toString(), objArray);
		 //log.info("SiResend SQL--"+hql.toString());
         return result;
	 }
	 //由SiResend的HeadId取SoSalesOrderHead list
	 public List<SoSalesOrderHead> findSalesOrderByIsLock(List<SiResend> siresend){
		 List<SoSalesOrderHead> result = null;
		 List<SoSalesOrderHead> result1 = null;
		 
		 StringBuffer getHeadId = new StringBuffer("from SoSalesOrderHead as model where model.headId in( ''");
		 for (Iterator iterator = siresend.iterator(); iterator.hasNext();) {
			 SiResend siResend = (SiResend) iterator.next();			 
			 try{
				 StringBuffer hql = new StringBuffer("from SoSalesOrderHead as model where model.headId = ? ");
				 hql.append(" and model.status = ?");
				 //Object notOrderNo = this.findById("SoSalesOrderHead", siResend.getHeadId() );
				 String status = "UNCONFIRMED";
				 Object[] objArray = null;
		    	 objArray = new Object[] { siResend.getHeadId() , status};
		    	 result1 = getHibernateTemplate().find(hql.toString(), objArray);
		    	 //log.info("Mark----:"+ result1.size() );
				 if (result1.size()==0) {
					 //log.info("2:--銷售單沒資料就刪除Resend的多餘資料"+ siResend.getId().getOrderNo() );
					 siResend.setStatus("PENDING");
					 siResendDAO.update(siResend);
				 }else{
					 //log.info("2:--銷售單有資料就放進查詢"+ siResend.getId().getOrderNo() );
					 getHeadId.append(", '").append(siResend.getHeadId()).append("' ");
				 }
				 
			 }catch (Exception ex){
				 //log.info("1:--沒資料"+ siResend.getId().getOrderNo() );
				 siResendDAO.delete(siResend);
			 }			 			 
		 }
		 getHeadId.append(")");
		 getHeadId.append(" and model.status = 'UNCONFIRMED'");
		 //log.info("SQL---:"+getHeadId.toString());
		 result = getHibernateTemplate().find(getHeadId.toString());
		 return result;
	 }
		/** 依照 BuEmployeeWithAddressView 工號, 英文名子, 中文名字, 尋找 employee 
		 * @param findObj
		 * @return
		 */
		public List<SoSalesOrderHead> findByCustomerPoNo( HashMap findObjs ){
			final HashMap fos = findObjs;
			List<SoSalesOrderHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {

				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					StringBuffer hql = new StringBuffer("select model from SoSalesOrderHead as model, SoSalesOrderHead as model2 ");
					hql.append(" where model.headId = model2.headId");

					log.info("customerPoNo:"+StringUtils.hasText((String) fos.get("customerPoNo")));
					if (StringUtils.hasText((String) fos.get("customerPoNo")))
						hql.append(" and model.customerPoNo ='").append((String) fos.get("customerPoNo")).append("'");
					log.info(hql.toString());
					Query query = session.createQuery(hql.toString());
					//query.setFirstResult(0);
					//query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
					return query.list();
				}
			});
			return re;
		}
	 
	/**
	 * 依庫別代號查詢銷售資料	 
	 * @return List
	 */
	public List getSoListForIncharge(final Map findObjs, final String order, final int startPage, final int pageSize, final int searchType) {
		log.info("getSoListForIncharge:"+searchType);
		log.info("QUERY_RECORD_COUNT:"+QUERY_RECORD_COUNT);
		log.info("QUERY_SELECT_RANGE:"+QUERY_SELECT_RANGE);
		List<SoSalesOrderHead> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer();
						
						switch ( searchType ) {
						
						case QUERY_RECORD_COUNT:
							hql.append("select count(s.headId) as rowCount from SoSalesOrderHead as s, BuShop as b ");
							break;
						case QUERY_SELECT_RANGE:
							hql.append("select s from SoSalesOrderHead as s, BuShop as b ");
							break;
						default:
		  					throw new SQLException(" searchType 無此搜尋條件 ");
						}
						
						hql.append(" where s.shopCode = b.shopCode");  //join條件
						
						Iterator it = findObjs.keySet().iterator();
						
						while (it.hasNext()) {
							// objectKey = SQL敘述
							Object objectKey = (Object) it.next();
							String strKey = (String)objectKey;
							//log.info( "objectKey = " + objectKey );
							if( findObjs.containsKey(objectKey) ){
								Object objectValue = findObjs.get( objectKey );
								//log.info( "objectValue = " + objectValue );
								if(objectValue != null){
									if(objectValue instanceof Date){
		  								hql.append( objectKey );
			  						}else if( objectValue instanceof String ){ 
			  							String strValue = (String) objectValue;
			  							if( strKey.indexOf("like :") > -1 ){	// 代表使用like
			  								if ( !"%%".equals(strValue) ){
			  									log.info( strKey + "使用 like 判定" + (strKey.indexOf(" like ") > -1)  ); 
			  									hql.append( objectKey );
			  								}
			  							}else if(StringUtils.hasText( strValue ) ){
			  								hql.append( objectKey );
			  							}
			  						}else if( objectValue instanceof Long ){
			  							hql.append( objectKey );
			  						}else if( objectValue instanceof Double ){
			  						    hql.append( objectKey );
			  						}else if( objectValue instanceof String[] ){
			  							if( strKey.indexOf("in (:") > -1 ){	// 代表使用in
			  								log.info( strKey + "使用 in 判定" + (strKey.indexOf(" in ") > -1)  ); 
			  								hql.append( objectKey );
			  							}
			  						}
								}
							}
						}
						
//						hql.append(" where s.brandCode = :brandCode");
//						hql.append(" and b.incharge = :incharge");
//						hql.append(" and s.shopCode = b.shopCode");
						//hql.append(" order by s.customerPoNo, s.lastUpdateDate desc");
						hql.append( " "  + order );
						
						Query query = session.createQuery(hql.toString());
						if( QUERY_SELECT_RANGE == searchType ){
			  				query.setFirstResult( pageSize * startPage );
			  				query.setMaxResults( pageSize );
						}
						
						Iterator setIt = findObjs.keySet().iterator();
						while (setIt.hasNext()) {
							// strKey = SQL敘述
							String strKey = (String) setIt.next();
							// columnKey = :後的所有字元 (如果為in則要去掉最後一個尾碼)
							String columnKey 	 = strKey.substring( strKey.indexOf(":") + 1, strKey.indexOf("in (:") > 0 ? strKey.indexOf(")") : strKey.length() );
							log.info( "strKey = " + strKey );
							log.info( "columnKey = " + columnKey );
							if( findObjs.containsKey(strKey) ){
								Object objectValue = findObjs.get( strKey );
								log.info( "objectValue = " + objectValue );
								if(objectValue != null){
			  						if( objectValue instanceof Date ){
			  								query.setDate( columnKey, (Date)objectValue );
			  						}else if( objectValue instanceof String ){ 
			  							String strValue = (String) objectValue;
			  							if( strKey.indexOf("like :") > -1 ){	// 代表使用like
			  								if ( !"%%".equals(strValue) ){
			  									query.setString( columnKey, strValue );
			  								}
			  							}else if( StringUtils.hasText(strValue) ){
			  								query.setString( columnKey, strValue );
			  							}
			  						}else if( objectValue instanceof Long ){
			  							query.setLong( columnKey, (Long)objectValue );
			  						}else if( objectValue instanceof Double ){
			  						    query.setDouble( columnKey, (Double)objectValue );
			  						}else if( objectValue instanceof String[] ){
			  							if( strKey.indexOf("in (:") > -1 ){	// 代表使用in
			  								query.setParameterList( columnKey, (String[])objectValue);
			  							}
			  						}
								}
							}
						}
//						query.setString("brandCode", "T2");
//						query.setString("incharge", "T2F05");
						return query.list();
					}
				});
		return result;
	}
	
	/**
     * 依據品牌代號、POS機碼、銷售日期、交易序號 or DATA_ID (擇一)找尋銷售單
     * 
     * @param conditionMap
     * @return List<SoSalesOrderHead>
	 * @throws ParseException 
     */
    public List<SoSalesOrderHead> findOnlineSoHead(HashMap conditionMap) throws ParseException {
		String brandCode= (String)conditionMap.get("BRAND_CODE");
		String posMachineCode = (String)conditionMap.get("MACHINE_CODE");
		String transactionSeqNo = (String)conditionMap.get("transactionSeqNo");
		Date salesOrderDate = (Date)conditionMap.get("salesOrderDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dt = sdf.format(salesOrderDate);
		//dt = dt.substring(0,salesOrderDate.indexOf(" "));
		
		System.out.println("salesOrderDate:"+salesOrderDate);
		Date d = sdf.parse(dt);
		StringBuffer hql = new StringBuffer("from SoSalesOrderHead as model where model.brandCode = ?");
		hql.append(" and model.salesOrderDate = ?");
		hql.append(" and model.posMachineCode = ?");
		Object[] objArray = null;
		if(StringUtils.hasText(transactionSeqNo)){
			hql.append(" and model.transactionSeqNo = ?");
			objArray = new Object[] { brandCode, d, posMachineCode, transactionSeqNo };	
		} else{
			objArray = new Object[] { brandCode, d , posMachineCode };	
		}
		log.info("===================findOnlineSoBean hql.toString() : "+hql.toString()+"===================");
		List<SoSalesOrderHead> result = getHibernateTemplate().find(hql.toString(), objArray);
		return result;
    }
	
	public boolean updateSoHead(SoSalesOrderHead so) {
		log.debug("updating SoSalesOrderHeadDAO instance");
		boolean result = false;
		Connection conn = null;
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@10.1.95.101:1521:KWEDB1","KWE_ERP","1QAZ8IK,!@#$");
			Statement stat = conn.createStatement();
			stat.executeUpdate(" update ERP.SO_SALES_ORDER_HEAD H set RESERVE2 = '' where H.POS_MACHINE_CODE = '"+so.getPosMachineCode()+"' and H.BRAND_CODE='T2' and H.TRANSACTION_SEQ_NO='"+so.getTransactionSeqNo()+"'");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public List<SoSalesOrderHead> findRealTimeData(){
		
		final String place = "VD";
		Date date = new Date();
		
		Calendar cal = Calendar.getInstance();
		final int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);		
		Date finalDeclDate = DateUtils.addDays(date, -1); 
		
		final String actualDataDate = DateUtils.format(finalDeclDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
		
		List<SoSalesOrderHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				StringBuffer hql = new StringBuffer("select model from SoSalesOrderHead as model, BuShop as model2 ");
				hql.append(" where model.shopCode = model2.shopCode ");
				hql.append(" and model.orderTypeCode = 'SOP' ");
				hql.append(" and model.brandCode = 'T2' ");
				hql.append(" and model2.place = '"+place+"' ");
				hql.append(" and model.orderNo not like 'TMP_%' ");
				hql.append(" and model.salesOrderDate > TO_DATE( '"+actualDataDate+"','YYYYMMDD') ");
				hql.append(" and SUBSTR(model.transactionTime,0,2) < "+(hourOfDay-2));
				hql.append(" and model.customsStatus is null ");
				hql.append(" order by model.transactionTime");				
				
				log.info(hql.toString());
				
				Query query = session.createQuery(hql.toString());				
				return query.list();
			}
		});
		return re;
	}
	
	public List<SoSalesOrderHead> findRealTimeDataAll(int beforeTime){
		
		String place = "('FD','VD','HD')";
		//String place = "('HD')";
		Date date = new Date();
		String actualDataDate ="";
		String beforeDate = "";
		
		Calendar cal = Calendar.getInstance();
		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);		
		Date finalDeclDate = DateUtils.addDays(date, -1);
		
		if(hourOfDay-beforeTime>0){
			actualDataDate = DateUtils.format(date, DateUtils.C_DATA_PATTON_YYYYMMDD);
			beforeDate = DateUtils.format(DateUtils.addDays(date, -2), DateUtils.C_DATA_PATTON_YYYYMMDD);
			hourOfDay = hourOfDay-beforeTime;
		}else{
			actualDataDate = DateUtils.format(finalDeclDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			beforeDate = DateUtils.format(DateUtils.addDays(finalDeclDate, -2), DateUtils.C_DATA_PATTON_YYYYMMDD);
			hourOfDay = 24;
		}
		
		StringBuffer hql = new StringBuffer("select model from SoSalesOrderHead as model, BuShop as model2 ");
		hql.append(" where model.shopCode = model2.shopCode ");
		hql.append(" and model.orderTypeCode = 'SOP' ");
		hql.append(" and model.brandCode = 'T2' ");
		hql.append(" and model2.place IN "+place+"");
		hql.append(" and model.orderNo not like 'TMP_%' ");
		
		hql.append(" and( (model.salesOrderDate <= TO_DATE( '"+actualDataDate+"','YYYYMMDD') ");
		hql.append(" and model.salesOrderDate > TO_DATE( '"+beforeDate+"','YYYYMMDD') ");
		hql.append(" and SUBSTR(model.transactionTime,0,2) < "+hourOfDay+" )");
		
		hql.append(" or (model.salesOrderDate < TO_DATE( '"+actualDataDate+"','YYYYMMDD') ");
		hql.append(" and model.salesOrderDate > TO_DATE( '"+beforeDate+"','YYYYMMDD')) )");
		
		hql.append(" and model.customsStatus is null ");
		hql.append(" and model.customsNo is not null ");
		hql.append(" order by model.transactionTime");
		System.out.println(hql.toString());
		List<SoSalesOrderHead> re = getHibernateTemplate().find(hql.toString());		
						
		return re;
	}
	
}