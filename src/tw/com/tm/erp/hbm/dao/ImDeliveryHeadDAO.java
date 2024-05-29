package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuGoalHead;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class ImDeliveryHeadDAO extends BaseDAO {
    
    private static final Log log = LogFactory.getLog(ImDeliveryHeadDAO.class);
    public static final int MAX_DAILY_BALANCE_RECORD=2000;

    public List findDeliveryList(HashMap conditionMap) {
	
	final String brandCode = (String) conditionMap.get("brandCode");
	final String customerCode = (String) conditionMap.get("customerCode");
	final Date scheduleShipDate_Start = (Date) conditionMap.get("scheduleShipDate_Start");
	final Date scheduleShipDate_End = (Date) conditionMap.get("scheduleShipDate_End");
	final String status = (String) conditionMap.get("status");
	final String shipTypeCode = (String) conditionMap.get("shipTypeCode");
	final String shipNo_Start = (String) conditionMap.get("shipNo_Start");
	final String shipNo_End = (String) conditionMap.get("shipNo_End");	
	final Date shipDate_Start = (Date) conditionMap.get("shipDate_Start");
	final Date shipDate_End = (Date) conditionMap.get("shipDate_End");
	final String salesTypeCode = (String) conditionMap.get("salesTypeCode");
	final String salesNo_Start = (String) conditionMap.get("salesNo_Start");
	final String salesNo_End = (String) conditionMap.get("salesNo_End");
	final Date salesDate_Start = (Date) conditionMap.get("salesDate_Start");
	final Date salesDate_End = (Date) conditionMap.get("salesDate_End");
	final String defaultWarehouseCode = (String) conditionMap.get("defaultWarehouseCode");
	final String transactionSeqNo = (String) conditionMap.get("transactionSeqNo");
	final String customerPoNo = (String) conditionMap.get("customerPoNo");
	final String incharge = (String) conditionMap.get("incharge");  //依URL區分T2,馬祖,高雄
	
	List result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("");
			
			if(shipTypeCode.equals("IOP")||salesTypeCode.equals("SOP")){
				
				if(incharge.equals("")){
					hql.append("select model, model2 from ImDeliveryHead as model, SoSalesOrderHead as model2 ");
					hql.append("where model.salesOrderId = model2.headId and model.brandCode = :brandCode");					
					log.info("執行IOP出貨單或SOP但是庫別不限定:shipTypeCode="+shipTypeCode+"或salesTypeCode:"+salesTypeCode); //可看所有出貨單
				}else{
					hql.append("select model, model2, model3 from ImDeliveryHead as model, SoSalesOrderHead as model2, BuShop as model3 ");
					hql.append("where model.salesOrderId = model2.headId and model.brandCode = :brandCode");
					hql.append(" and model.shopCode = model3.shopCode and model3.incharge = :incharge");
					log.info("執行IOP出貨單或SOP:shipTypeCode="+shipTypeCode+"或salesTypeCode:"+salesTypeCode); //只能看特定出貨單
				}				
			}else{
				hql.append("select model, model2 from ImDeliveryHead as model, SoSalesOrderHead as model2 ");
				hql.append("where model.salesOrderId = model2.headId and model.brandCode = :brandCode");				
				log.info("非執行IOP出貨單或SOP:shipTypeCode="+shipTypeCode+"或salesTypeCode:"+salesTypeCode); //可看所有出貨單
			}			
			
			if (StringUtils.hasText(customerCode))
			    hql.append(" and model.customerCode = :customerCode");
			
			if (StringUtils.hasText(status))
			    hql.append(" and model.status = :status");
			
			if (StringUtils.hasText(shipTypeCode))
			    hql.append(" and model.orderTypeCode = :shipTypeCode");

			if (StringUtils.hasText(shipNo_Start))
			    hql.append(" and model.orderNo >= :shipNo_Start");

			if (StringUtils.hasText(shipNo_End))
			    hql.append(" and model.orderNo <= :shipNo_End");
			
			if (StringUtils.hasText(salesTypeCode))
			    hql.append(" and model2.orderTypeCode = :salesTypeCode");

			if (StringUtils.hasText(salesNo_Start))
			    hql.append(" and model2.orderNo >= :salesNo_Start");

			if (StringUtils.hasText(salesNo_End))
			    hql.append(" and model2.orderNo <= :salesNo_End");

			if (scheduleShipDate_Start != null)
			    hql.append(" and model.scheduleShipDate >= :scheduleShipDate_Start");

			if (scheduleShipDate_End != null)
			    hql.append(" and model.scheduleShipDate <= :scheduleShipDate_End");

			if (shipDate_Start != null)
			    hql.append(" and model.shipDate >= :shipDate_Start");

			if (shipDate_End != null)
			    hql.append(" and model.shipDate <= :shipDate_End");

			if (salesDate_Start != null)
			    hql.append(" and model2.salesOrderDate >= :salesDate_Start");
			
			if (salesDate_End != null)
			    hql.append(" and model2.salesOrderDate <= :salesDate_End");
			
			if (StringUtils.hasText(defaultWarehouseCode))
			    hql.append(" and model.defaultWarehouseCode = :defaultWarehouseCode");

			if (StringUtils.hasText(transactionSeqNo))
			    hql.append(" and model.transactionSeqNo = :transactionSeqNo");

			if (StringUtils.hasText(customerPoNo))
			    hql.append(" and model.customerPoNo = :customerPoNo");
			
			hql.append(" order by model.orderNo desc");
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			query.setString("brandCode", brandCode);
			
			if(shipTypeCode.equals("IOP")||salesTypeCode.equals("SOP")){
				if(incharge.equals("")){
					log.info("不限定庫別"); //可看所有出貨單
				}else{
					query.setString("incharge", incharge);
				}				
			}

			if (StringUtils.hasText(customerCode))
			    query.setString("customerCode", customerCode);

			if (StringUtils.hasText(status))
			    query.setString("status", status);

			if (StringUtils.hasText(shipTypeCode))
			    query.setString("shipTypeCode", shipTypeCode);

			if (StringUtils.hasText(shipNo_Start))
			    query.setString("shipNo_Start", shipNo_Start);

			if (StringUtils.hasText(shipNo_End))
			    query.setString("shipNo_End", shipNo_End);
			
			if (StringUtils.hasText(salesTypeCode))
			    query.setString("salesTypeCode", salesTypeCode);
			
			if (StringUtils.hasText(salesNo_Start))
			    query.setString("salesNo_Start", salesNo_Start);
			
			if (StringUtils.hasText(salesNo_End))
			    query.setString("salesNo_End", salesNo_End);		
			
			if (scheduleShipDate_Start != null)
			    query.setDate("scheduleShipDate_Start", scheduleShipDate_Start);

			if (scheduleShipDate_End != null)
			    query.setDate("scheduleShipDate_End", scheduleShipDate_End);

			if (shipDate_Start != null)
			    query.setDate("shipDate_Start", shipDate_Start);

			if (shipDate_End != null)
			    query.setDate("shipDate_End", shipDate_End);

			if (salesDate_Start != null)
			    query.setDate("salesDate_Start", salesDate_Start);
			
			if (salesDate_End != null)
			    query.setDate("salesDate_End", salesDate_End);
			
			if (StringUtils.hasText(defaultWarehouseCode))
			    query.setString("defaultWarehouseCode", defaultWarehouseCode);
			
			if (StringUtils.hasText(transactionSeqNo))
			    query.setString("transactionSeqNo", transactionSeqNo);
			
			if (StringUtils.hasText(customerPoNo))
			    query.setString("customerPoNo", customerPoNo);
			
			return query.list();
		    }
		});

	return result;
	
    }

    public ImDeliveryHead findDeliveryByIdentification(String brandCode, String orderType, String orderNo) {
	
	StringBuffer hql = new StringBuffer("from ImDeliveryHead as model where model.brandCode = ?");
        hql.append(" and model.orderTypeCode = ?");
        hql.append(" and model.orderNo = ?");
        List<ImDeliveryHead> result = getHibernateTemplate().find(hql.toString(),
		new Object[] { brandCode, orderType,  orderNo});
	return (result != null && result.size() > 0 ? result.get(0) : null);
    }
    
    public List findReturnList(HashMap conditionMap) {
	
	final String brandCode = (String) conditionMap.get("brandCode");
	final String returnTypeCode = (String) conditionMap.get("returnTypeCode");	
	final String orderNo_Start = (String) conditionMap.get("orderNo_Start");
	final String orderNo_End = (String) conditionMap.get("orderNo_End");	
	final String status = (String) conditionMap.get("status");
	final Date returnDate_Start = (Date) conditionMap.get("returnDate_Start");
	final Date returnDate_End = (Date) conditionMap.get("returnDate_End");	

	List result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("select model from ImDeliveryHead as model where model.brandCode = :brandCode");
			hql.append(" and substring(model.orderNo,1,3) <> 'TMP'");
			
			if (StringUtils.hasText(returnTypeCode))
			    hql.append(" and model.orderTypeCode = :returnTypeCode");

			if (StringUtils.hasText(orderNo_Start))
			    hql.append(" and model.orderNo >= :orderNo_Start");

			if (StringUtils.hasText(orderNo_End))
			    hql.append(" and model.orderNo <= :orderNo_End");
			
			if (StringUtils.hasText(status))
			    hql.append(" and model.status = :status");
			
			if (returnDate_Start != null)
			    hql.append(" and model.shipDate >= :returnDate_Start");
			
			if (returnDate_End != null)
			    hql.append(" and model.shipDate <= :returnDate_End");

			hql.append(" order by model.orderNo desc");
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			query.setString("brandCode", brandCode);

			if (StringUtils.hasText(returnTypeCode))
			    query.setString("returnTypeCode", returnTypeCode);
			
			if (StringUtils.hasText(orderNo_Start))
			    query.setString("orderNo_Start", orderNo_Start);

			if (StringUtils.hasText(orderNo_End))
			    query.setString("orderNo_End", orderNo_End);

			if (StringUtils.hasText(status))
			    query.setString("status", status);
			
			if (returnDate_Start != null)
			    query.setDate("returnDate_Start", returnDate_Start);
			
			if (returnDate_End != null)
			    query.setDate("returnDate_End", returnDate_End);

			return query.list();
		    }
		});

	return result;
	
    }
    
    public List findInvoiceList(HashMap conditionMap) {
	
	final String brandCode = (String) conditionMap.get("brandCode");
	final String shipTypeCode = (String) conditionMap.get("shipTypeCode");	
	final String shipNo_Start = (String) conditionMap.get("shipNo_Start");
	final String shipNo_End = (String) conditionMap.get("shipNo_End");
	final Date shipDate_Start = (Date) conditionMap.get("shipDate_Start");	
	final Date shipDate_End = (Date) conditionMap.get("shipDate_End");
	final String isHavedInvoiceNo = (String) conditionMap.get("isHavedInvoiceNo");

	List result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("select model from ImDeliveryHead as model where model.brandCode = :brandCode");
			hql.append(" and model.orderTypeCode = :shipTypeCode and model.status in ('FINISH','CLOSE')");

			if (StringUtils.hasText(shipNo_Start))
			    hql.append(" and model.orderNo >= :shipNo_Start");

			if (StringUtils.hasText(shipNo_End))
			    hql.append(" and model.orderNo <= :shipNo_End");
			
			if (shipDate_Start != null)
			    hql.append(" and model.shipDate >= :shipDate_Start");

			if (shipDate_End != null)
			    hql.append(" and model.shipDate <= :shipDate_End");
			
			if(StringUtils.hasText(isHavedInvoiceNo)){
			    if("Y".equals(isHavedInvoiceNo))
			        hql.append(" and model.invoiceNo is not null");
			    else if("N".equals(isHavedInvoiceNo))
				hql.append(" and model.invoiceNo is null");
			}
			//hql.append(" or (model.brandCode = :brandCode and model.orderTypeCode = :shipTypeCode and model.status = 'FINISH' and model.invoiceNo is null)");

			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			query.setString("brandCode", brandCode);
			query.setString("shipTypeCode", shipTypeCode);
			
			if (StringUtils.hasText(shipNo_Start))
			    query.setString("shipNo_Start", shipNo_Start);

			if (StringUtils.hasText(shipNo_End))
			    query.setString("shipNo_End", shipNo_End);

			if (shipDate_Start != null)
			    query.setDate("shipDate_Start", shipDate_Start);

			if (shipDate_End != null)
			    query.setDate("shipDate_End", shipDate_End);

			return query.list();
		    }
		});

	return result;
	
    }
    
    public List<ImDeliveryHead> findDeliveryByProperty(final String brandCode, final String status, final String typeCode, final Date startDate, final Date endDate) {
    	
    	List<ImDeliveryHead> result = getHibernateTemplate().executeFind(
    		new HibernateCallback() {
    		    public Object doInHibernate(Session session)
    			    throws HibernateException, SQLException {
    		    	StringBuffer hql = new StringBuffer("select model from ImDeliveryHead as model , BuOrderType as model2 ");
    		    	hql.append("where model.brandCode = model2.id.brandCode and model.orderTypeCode = model2.id.orderTypeCode");	
    		    	hql.append(" and model.brandCode = :brandCode");
    		    	hql.append(" and model.status = :status");
    		    	if (null != startDate)
    		    		hql.append(" and model.shipDate >= :startDate");//20100525 ADD BY JOEYWU for 只日結一天
    		    	
    		    	if (null != endDate)
    		    		hql.append(" and model.shipDate <= :endDate");//20100525 ADD BY JOEYWU for 只日結一天
    		    	
    		    	hql.append(" and model2.typeCode = :typeCode");
    		    	hql.append(" order by model.orderTypeCode, model.orderNo");
    		    	Query query = session.createQuery(hql.toString());
    		    	query.setFirstResult(0);
    		    	query.setMaxResults(MAX_DAILY_BALANCE_RECORD);
    		    	query.setString("brandCode", brandCode);//T2
    		    	query.setString("status", status);//FINISH
    		    	if (null != startDate)
    		    		query.setDate("startDate", startDate);
    		    	if (null != endDate)
    		    		query.setDate("endDate", endDate);
    		    	query.setString("typeCode", typeCode);//IOU or IR
    		    	//System.out.println("111------findDeliveryByProperty = " + query.list().size());
            return query.list();
            }
    	});
    	return result;
    }
    

    
    public List<ImDeliveryHead> findDeliveryByProperty(String brandCode, String status, String typeCode) {
    	
    	StringBuffer hql = new StringBuffer("select model from ImDeliveryHead as model , BuOrderType as model2 ");
    	hql.append("where model.brandCode = model2.id.brandCode and model.orderTypeCode = model2.id.orderTypeCode");	
            hql.append(" and model.brandCode = ?");
            hql.append(" and model.status = ?");
            hql.append(" and model2.typeCode = ?");
            hql.append(" order by model.orderTypeCode, model.orderNo");
            List<ImDeliveryHead> result = getHibernateTemplate().find(hql.toString(),
    		new Object[] { brandCode, status,  typeCode});
    	return result;
    }
    
    public List<ImDeliveryHead> findDeliveryByProperty(String[] brandCodeArray, Date date, String[] statusArray, final String typeCode) {
	
	List<ImDeliveryHead> assembly = new ArrayList(0);
	final Date shipDate = DateUtils.getShortDate(date);
	StringBuffer brandCodeExpression = new StringBuffer();
	StringBuffer statusExpression = new StringBuffer();
	for(int i = 0; i < brandCodeArray.length; i++){
	    brandCodeExpression.append("'");
	    brandCodeExpression.append(brandCodeArray[i]);
	    brandCodeExpression.append("'");
	    if(i != brandCodeArray.length -1)
	        brandCodeExpression.append(",");
	}
	for(int j = 0; j < statusArray.length; j++){
	    statusExpression.append("'");
	    statusExpression.append(statusArray[j]);
	    statusExpression.append("'");
	    if(j != statusArray.length -1)
		statusExpression.append(",");
	}
	final String brandCodeWording = brandCodeExpression.toString();
	final String statusWording = statusExpression.toString();	
	//出貨日期不為NULL時
	StringBuffer hql = new StringBuffer("select model from ImDeliveryHead as model , BuOrderType as model2 ");
	hql.append("where model.brandCode = model2.id.brandCode and model.orderTypeCode = model2.id.orderTypeCode");
	hql.append(" and model.shipDate <= ?");
        hql.append(" and model.brandCode in (" + brandCodeWording + ")");
        hql.append(" and model.status in (" + statusWording + ")");
        hql.append(" and model2.typeCode = ?");
        hql.append(" order by model.brandCode, model.orderTypeCode, model.orderNo");
        List<ImDeliveryHead> result = getHibernateTemplate().find(hql.toString(),
		new Object[] { shipDate,  typeCode});
        if(result != null && result.size() > 0){
            assembly.addAll(result);
        }
        //出貨日期為NULL時
        List result2 = getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
        	StringBuffer sql = new StringBuffer("select d.brand_code, d.order_type_code, d.order_no, d.status from so_sales_order_head s,");
                sql.append(" (select model.brand_code, model.order_type_code, model.order_no, model.status, model.sales_order_id from im_delivery_head model, bu_order_type model2");
                sql.append(" where model.brand_code = model2.brand_code and model.order_type_code = model2.order_type_code");
                sql.append(" and model.ship_date is NULL");
                sql.append(" and model.brand_code in (" + brandCodeWording + ")");
                sql.append(" and model.status in (" + statusWording + ")");
                sql.append(" and model2.type_code = :typeCode");
                sql.append(" order by model.brand_code, model.order_type_code, model.order_no) d");
                sql.append(" where s.head_id = d.sales_order_id and s.sales_order_date <= :salesOrderDate");
                sql.append(" order by d.brand_code, d.order_type_code, d.order_no");
	        
		Query query = session.createSQLQuery(sql.toString());
		query.setString("typeCode", typeCode);
		query.setDate("salesOrderDate", shipDate);
				
		return query.list();
	    }
	});
        if(result2 != null && result2.size() > 0){
            for(int j = 0; j < result2.size(); j++){
                Object[] objArray = (Object[])result2.get(j);
                ImDeliveryHead deliveryHeadBean = new ImDeliveryHead();
                deliveryHeadBean.setBrandCode((String)objArray[0]);
                deliveryHeadBean.setOrderTypeCode((String)objArray[1]);
                deliveryHeadBean.setOrderNo((String)objArray[2]);
                deliveryHeadBean.setStatus((String)objArray[3]);
                assembly.add(deliveryHeadBean);
            }
        }
        
	return assembly;
    }
    
    /**
     * 依品牌年月取得上月銷售金額
     * @param brandCode
     * @param year
     * @param month
     * @return
     */
    public Double getLastMonthGoal(String brandCode,String shopCode, Long year, Long month){
    	double lastMonthGoal = 0.0D;
    	int yearInt = year.intValue();
    	int monthInt = month.intValue();
    	if( monthInt == 1 ){
    		monthInt = 12;
    		yearInt -= 1; 
    	}else{
    		monthInt -= 1;
    	}
    	
    	String monthString = String.valueOf( monthInt );
    	monthString = monthString.length() < 2 ?  "0" + monthString : monthString;
    	int dayInt = DateUtils.getLastDayOfMonth(yearInt, monthInt) - 1;
    	
    	StringBuffer sql = new StringBuffer("select sum(totalActualShipAmount) from ImDeliveryHead where brandCode = '").append(brandCode).append("' ");
    	sql.append( "and shopCode ='").append(shopCode).append("' ");
    	sql.append( "and shipDate >= TO_DATE('" ).append( yearInt + monthString + "01','YYYYMMDD') ");
    	sql.append( "and shipDate < TO_DATE('" ).append( yearInt + monthString + dayInt ).append("','YYYYMMDD') + 1 ");
    	sql.append( "and orderTypeCode IN( 'IOP', 'IRP' ) and status not in( 'VOID', 'SAVE', 'REJECT' ) ");
    	List result = find(sql.toString());
    	if( result != null && result.size() > 0 ) {
    		Object obj = (Object)result.get(0);
    		log.info( "上月銷售金額  = " + obj);
    		lastMonthGoal = obj != null ? (Double)obj : 0.0d;
    	}
    	return lastMonthGoal;
    }
    
    /**
     * 依品牌年月取得去年同月銷售金額
     * @param brandCode
     * @param year
     * @param month
     * @return
     */
    public Double getLastYearGoal(String brandCode,String shopCode, Long year, Long month){
    	double lastYearGoal = 0.0D;
    	int yearInt = year.intValue() - 1;
    	int monthInt = month.intValue();
    	
    	String monthString = String.valueOf( monthInt );
    	monthString = monthString.length() < 2 ?  "0" + monthString : monthString;
    	int dayInt = DateUtils.getLastDayOfMonth(yearInt, monthInt) - 1;
    	
    	StringBuffer sql = new StringBuffer("select sum(totalActualShipAmount) from ImDeliveryHead where brandCode = '").append(brandCode).append("' ");
    	sql.append( "and shopCode ='").append(shopCode).append("' ");
    	sql.append( "and shipDate >= TO_DATE('" ).append( yearInt + monthString + "01','YYYYMMDD') ");
    	sql.append( "and shipDate < TO_DATE('" ).append( yearInt + monthString + dayInt ).append("','YYYYMMDD') + 1 ");
    	sql.append( "and orderTypeCode IN( 'IOP', 'IRP' ) and status not in( 'VOID', 'SAVE', 'REJECT' ) ");
    	List result = find(sql.toString());
    	
    	if( result != null && result.size() > 0 ) {
    		Object obj = (Object)result.get(0);
    		log.info( "去年同月銷售金額  = " + obj);
    		lastYearGoal = obj != null ? (Double)obj : 0.0d;
    	}
    	return lastYearGoal;
    }

    
    public List findScheduleOrders(String schedule,Date closeDate,String startHours,String endHours) throws Exception{
		
		
        final String date = DateUtils.format(closeDate);
        final String argStartHours = startHours;
        final String argEndHours = endHours;
		System.out.println("ID轉型後:"+DateUtils.format(closeDate));
		
		List<ImDeliveryHead> re = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer(
								"from ImDeliveryHead as model where 1=1 ");
						

						
							hql.append(" and model.status IN( 'FINISH','CLOSE' ) ");

						
                       
							hql.append(" and model.lastUpdateDate between to_date('"+date+" "+argStartHours+"','yyyy-mm-dd HH24:MI:SS') and "+"to_date('"+date+" "+argEndHours+"','yyyy-mm-dd HH24:MI:SS')");

						
                        hql.append(" and model.orderTypeCode in ('IBT','IRP') order by lastUpdateDate desc ");

						System.out.println(hql.toString());
						Query query = session.createQuery(hql.toString());
						query.setFirstResult(0);
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
	public List getDeliveryListForIncharge(final Map findObjs, final String order, final int startPage, final int pageSize, final int searchType) {
		log.info("getDeliveryListForIncharge:"+searchType);
		
		List<ImDeliveryHead> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer();
						
						switch ( searchType ) {
						
						case QUERY_RECORD_COUNT:
							hql.append("select count(d.headId) as rowCount from ImDeliveryHead as d, BuShop as b ");
							break;
						case QUERY_SELECT_RANGE:
							hql.append("select d from ImDeliveryHead as d, BuShop as b ");
							break;
						default:
		  					throw new SQLException(" searchType 無此搜尋條件 ");
						}
						
						hql.append(" where d.shopCode = b.shopCode");  //join條件
						
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

}