package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsHead;

public class ImInventoryCountsHeadDAO extends BaseDAO {
    private static final Log log = LogFactory
	    .getLog(ImInventoryCountsHeadDAO.class);
    public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
    public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
    public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";

    public List findInventoryCountsList(HashMap conditionMap) {

	final String brandCode = (String) conditionMap.get("brandCode");
	final String orderTypeCode = (String) conditionMap.get("orderTypeCode");
	final String orderNo_Start = (String) conditionMap.get("orderNo_Start");
	final String orderNo_End = (String) conditionMap.get("orderNo_End");
	final String status = (String) conditionMap.get("status");
	final Date countsDate_Start = (Date) conditionMap
		.get("countsDate_Start");
	final Date countsDate_End = (Date) conditionMap.get("countsDate_End");
	final String warehouseCode = (String) conditionMap.get("warehouseCode");
	final String countsType = (String) conditionMap.get("countsType");
	final String countsId = (String) conditionMap.get("countsId");
	final String countsLotNo_Start = (String) conditionMap
		.get("countsLotNo_Start");
	final String countsLotNo_End = (String) conditionMap
		.get("countsLotNo_End");

	List result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer(
				"from ImInventoryCountsHead as model where model.brandCode = :brandCode");

			hql.append(" and model.orderTypeCode = :orderTypeCode");

			if (StringUtils.hasText(orderNo_Start))
			    hql.append(" and model.orderNo >= :orderNo_Start");

			if (StringUtils.hasText(orderNo_End))
			    hql.append(" and model.orderNo <= :orderNo_End");

			if (StringUtils.hasText(status))
			    hql.append(" and model.status = :status");

			if (StringUtils.hasText(warehouseCode))
			    hql
				    .append(" and model.warehouseCode = :warehouseCode");

			if (StringUtils.hasText(countsType))
			    hql.append(" and model.countsType = :countsType");

			if (countsDate_Start != null)
			    hql
				    .append(" and model.countsDate >= :countsDate_Start");

			if (countsDate_End != null)
			    hql
				    .append(" and model.countsDate <= :countsDate_End");

			if (StringUtils.hasText(countsId))
			    hql.append(" and model.countsId = :countsId");

			if (StringUtils.hasText(countsLotNo_Start))
			    hql
				    .append(" and model.countsLotNo >= :countsLotNo_Start");

			if (StringUtils.hasText(countsLotNo_End))
			    hql
				    .append(" and model.countsLotNo <= :countsLotNo_End");

			hql.append(" order by model.orderNo desc");
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			query.setString("brandCode", brandCode);
			query.setString("orderTypeCode", orderTypeCode);

			if (StringUtils.hasText(orderNo_Start))
			    query.setString("orderNo_Start", orderNo_Start);

			if (StringUtils.hasText(orderNo_End))
			    query.setString("orderNo_End", orderNo_End);

			if (StringUtils.hasText(status))
			    query.setString("status", status);

			if (StringUtils.hasText(warehouseCode))
			    query.setString("warehouseCode", warehouseCode);

			if (StringUtils.hasText(countsType))
			    query.setString("countsType", countsType);

			if (countsDate_Start != null)
			    query.setDate("countsDate_Start", countsDate_Start);

			if (countsDate_End != null)
			    query.setDate("countsDate_End", countsDate_End);

			if (StringUtils.hasText(countsId))
			    query.setString("countsId", countsId);

			if (StringUtils.hasText(countsLotNo_Start))
			    query.setString("countsLotNo_Start",
				    countsLotNo_Start);

			if (StringUtils.hasText(countsLotNo_End))
			    query.setString("countsLotNo_End", countsLotNo_End);

			return query.list();
		    }
		});

	return result;
    }

    /**
     * 依據品牌代號、單別、單號查詢盤點單
     * 
     * @param brandCode
     * @param orderTypeCode
     * @param orderNo
     * @return ImInventoryCountsHead
     */
    public ImInventoryCountsHead findInventoryCountsByIdentification(
	    String brandCode, String orderTypeCode, String orderNo) {

	StringBuffer hql = new StringBuffer(
		"from ImInventoryCountsHead as model where model.brandCode = ?");
	hql.append(" and model.orderTypeCode = ?");
	hql.append(" and model.orderNo = ?");
	List<ImInventoryCountsHead> result = getHibernateTemplate().find(
		hql.toString(),
		new Object[] { brandCode, orderTypeCode, orderNo });
	return (result != null && result.size() > 0 ? result.get(0) : null);
    }
    
    /**
     * 依據盤點代號、盤點批號查詢盤點單
     * 
     * @param countsId
     * @param countsLotNo
     * @return ImInventoryCountsHead
     */
    public ImInventoryCountsHead findInventoryCountsByCountsIdAndLotNo(
	    String countsId, String countsLotNo, String orderNo) {

	StringBuffer hql = new StringBuffer(
		"from ImInventoryCountsHead as model where model.countsId = ?");
	hql.append(" and model.countsLotNo = ?");
	hql.append(" and model.orderNo not like 'TMP%' ");
	if(StringUtils.hasText(orderNo)){
	    hql.append(" and model.orderNo != ?");
	}
	Object[] objArray = null;
        if (StringUtils.hasText(orderNo)) {
            objArray = new Object[] {countsId, countsLotNo, orderNo};
        }else{
            objArray = new Object[] {countsId, countsLotNo};
        }
	List<ImInventoryCountsHead> result = getHibernateTemplate().find(
		hql.toString(), objArray);
	return (result != null && result.size() > 0 ? result.get(0) : null);
    }
    
    /**
     * 依據盤點代號、盤點批號、主鍵查詢盤點單
     * 
     * @param countsId
     * @param countsLotNo
     * @return ImInventoryCountsHead
     */
    public ImInventoryCountsHead findInventoryCountsByProperty(
	    String countsId, String countsLotNo, Long headId) {

	StringBuffer hql = new StringBuffer(
		"from ImInventoryCountsHead as model where model.countsId = ?");
	hql.append(" and model.countsLotNo = ?");
	if(headId != null){
	    hql.append(" and model.headId != ?");
	}
	Object[] objArray = null;
        if (headId != null) {
            objArray = new Object[] {countsId, countsLotNo, headId};
        }else{
            objArray = new Object[] {countsId, countsLotNo};
        }
	List<ImInventoryCountsHead> result = getHibernateTemplate().find(
		hql.toString(), objArray);
	return (result != null && result.size() > 0 ? result.get(0) : null);
    }
       
    /**
     * 
     * @param findObjs
     * @param startPage
     * @param pageSize
     * @param searchType  
     * 	      1) get max record count 
     *        2) select data records according to startPage and pageSize 
     *        3) select all records 
     * @return
     */
    public HashMap findPageLine(HashMap findObjs, int startPage,  int pageSize, String searchType) {
		
	final HashMap fos = findObjs;
	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final String type = searchType;
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
	        public Object doInHibernate(Session session)throws HibernateException, SQLException {
	            
	            String brandCode = (String)fos.get("brandCode");
	    	    String orderTypeCode = (String) fos.get("orderTypeCode");
	    	    String startOrderNo = (String) fos.get("startOrderNo");
	    	    String endOrderNo = (String) fos.get("endOrderNo");
	    	    String status = (String) fos.get("status");
	    	    String warehouseCode = (String) fos.get("warehouseCode");	    
	    	    Date startCountsDate = (Date) fos.get("startCountsDate");
	    	    Date endCountsDate = (Date) fos.get("endCountsDate");
	    	    String countsId = (String) fos.get("countsId");
	    	    String countsType = (String) fos.get("countsType");	    	    
	    	    String startCountsLotNo = (String) fos.get("startCountsLotNo");
	    	    String endCountsLotNo = (String) fos.get("endCountsLotNo");
	            
		    StringBuffer hql = new StringBuffer("");
		    if(QUARY_TYPE_RECORD_COUNT.equals(type)) {
		        hql.append("select count(model.headId) as rowCount from ImInventoryCountsHead as model where 1=1 ");
		    }else if(QUARY_TYPE_SELECT_ALL.equals(type)) {
			hql.append("select model.headId from ImInventoryCountsHead as model where 1=1 ");
		    }else{
			hql.append("from ImInventoryCountsHead as model where 1=1 ");
		    }
		    
		    if (StringUtils.hasText(brandCode))
		        hql.append(" and model.brandCode = :brandCode ");

		    if (StringUtils.hasText(orderTypeCode))
			hql.append(" and model.orderTypeCode = :orderTypeCode ");
		
	            hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");
		    		    
		    if (StringUtils.hasText(startOrderNo))
			hql.append(" and model.orderNo >= :startOrderNo");

	            if (StringUtils.hasText(endOrderNo))
			hql.append(" and model.orderNo <= :endOrderNo");

		    if (StringUtils.hasText(status))
			hql.append(" and model.status = :status");

		    if (StringUtils.hasText(warehouseCode))
			hql.append(" and model.warehouseCode = :warehouseCode");

		    if (StringUtils.hasText(countsId))
		        hql.append(" and model.countsId = :countsId");
		    
		    if (StringUtils.hasText(countsType))
			hql.append(" and model.countsType = :countsType");

		    if (startCountsDate != null)
			hql.append(" and model.countsDate >= :startCountsDate");

		    if (endCountsDate != null)
			hql.append(" and model.countsDate <= :endCountsDate");

		    if (StringUtils.hasText(startCountsLotNo))
			hql.append(" and model.countsLotNo >= :startCountsLotNo");

		    if (StringUtils.hasText(endCountsLotNo))
			hql.append(" and model.countsLotNo <= :endCountsLotNo");
		    
		    hql.append(" order by model.orderNo desc");
		    //System.out.println(hql.toString());
		    Query query = session.createQuery(hql.toString());
		    if( QUARY_TYPE_SELECT_RANGE.equals(type)) {
		        query.setFirstResult(startRecordIndexStar);
			query.setMaxResults(pSize);
		    }
						
		    if (StringUtils.hasText(brandCode))
		        query.setString("brandCode", brandCode);

		    if (StringUtils.hasText(orderTypeCode))
			query.setString("orderTypeCode", orderTypeCode);
		    		    
		    if (StringUtils.hasText(startOrderNo))
			query.setString("startOrderNo", startOrderNo);

	            if (StringUtils.hasText(endOrderNo))
	        	query.setString("endOrderNo", endOrderNo);

		    if (StringUtils.hasText(status))
			query.setString("status", status);

		    if (StringUtils.hasText(warehouseCode))
			query.setString("warehouseCode", warehouseCode);

		    if (StringUtils.hasText(countsId))
			query.setString("countsId", countsId);
		    
		    if (StringUtils.hasText(countsType))
			query.setString("countsType", countsType);

		    if (startCountsDate != null)
			query.setDate("startCountsDate", startCountsDate);

		    if (endCountsDate != null)
			query.setDate("endCountsDate", endCountsDate);

		    if (StringUtils.hasText(startCountsLotNo))
			query.setString("startCountsLotNo", startCountsLotNo);

		    if (StringUtils.hasText(endCountsLotNo))
			query.setString("endCountsLotNo", endCountsLotNo);		
						
		    return query.list();
		}
	});
	HashMap returnResult = new HashMap();		
	returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type)? result: null);			
	if(result.size() == 0){
	    returnResult.put("recordCount", 0L);
	}else{
	    returnResult.put("recordCount", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type)? result.size() : Long.valueOf(result.get(0).toString()));
	}
	return returnResult;
    }
}