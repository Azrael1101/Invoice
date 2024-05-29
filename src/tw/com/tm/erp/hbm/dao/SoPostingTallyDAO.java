package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
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
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.SoPostingTally;
import tw.com.tm.erp.hbm.bean.SoPostingTallyId;
import tw.com.tm.erp.hbm.bean.SoShopDailyHead;
import tw.com.tm.erp.hbm.bean.SoShopDailyHeadId;
import tw.com.tm.erp.hbm.service.BuBasicDataService;
import tw.com.tm.erp.utils.DateUtils;

public class SoPostingTallyDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(SoPostingTallyDAO.class);
    BuBasicDataService buBasicDataService;
    private BuShopMachineDAO buShopMachineDAO;
    
    /**
     * 依據POS資料過帳螢幕輸入條件查詢
     * 
     * @param conditionMap
     * @return List
     */
    public List findPostingDataList(HashMap conditionMap){
	
	final String orderTypeCode = (String) conditionMap.get("orderTypeCode");
	final String brandCode = (String) conditionMap.get("brandCode");
	final Date transactionDate = (Date) conditionMap.get("transactionDate");
	final String shopCode = (String) conditionMap.get("shopCode");
	final String status = (String) conditionMap.get("status");
	final String restrictPageCount = (String) conditionMap.get("restrictPageCount");
	
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer(
					"select p.shop_code, p.transaction_date , s.actual_sales_amount, d.total_actual_sales_amount, p.is_posting from erp.so_posting_tally p, erp.so_shop_daily_head d, " +
					"(select s.shop_code, s.sales_order_date, sum(total_actual_sales_amount) as actual_sales_amount  from erp.so_sales_order_head s where s.brand_code = :brandCode and s.order_type_code = :orderTypeCode group by s.shop_code, s.sales_order_date) s " +
					"where p.shop_code = s.shop_code(+) and p.shop_code = d.shop_code(+) and p.transaction_date = s.sales_order_date(+) and p.transaction_date = d.sales_date(+)");

			hql.append(" and p.brand_code = :brandCode");
			hql.append(" and p.transaction_date <= :transactionDate");
			hql.append(" and p.is_posting = :status");	
			if (StringUtils.hasText(shopCode)) {
			    hql.append(" and p.shop_code = :shopCode");			    
			}
		
			hql.append(" order by p.transaction_date, p.shop_code");
			Query query = session.createSQLQuery(hql.toString());
			if (StringUtils.hasText(restrictPageCount)) {
			    query.setFirstResult(0);
		            query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);		    
			}
						
			query.setString("orderTypeCode", orderTypeCode);
			query.setString("brandCode", brandCode);
			query.setDate("transactionDate", transactionDate);
			query.setString("status", status);
			if (StringUtils.hasText(shopCode)) {
			    query.setString("shopCode", shopCode);		    
			}
			
			return query.list();
		}
	});

	return result;
    }
    
    
    /**
     * 依據專櫃代號、交易日期、過帳狀態進行查詢
     * 
     * @param brandCode
     * @param shopCode
     * @param transactionDate
     * @param lastTransactionDateString
     * @param isPosting
     * @param isDesignate
     * @return List
     */
    public List<SoPostingTally> findPostingTallyByProperty(final String brandCode, final String shopCode, final Date transactionDate, final Date lastTransactionDate , 
	    final String isPosting, final String isDesignate, final String employeeCode ,final String batch) {
	
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {

		    StringBuffer hql = new StringBuffer("from SoPostingTally as model where model.brandCode = :brandCode");
		    hql.append(" and model.isPosting = :isPosting");
		    if (StringUtils.hasText(shopCode)) {
	                hql.append(" and model.id.shopCode = :shopCode");		    
		    }
		    if("Y".equals(isDesignate)){
			hql.append(" and model.id.transactionDate = :lastTransactionDate");
		    }else{
			hql.append(" and model.id.transactionDate <= :lastTransactionDate");
			if(transactionDate != null){
		            hql.append(" and model.id.transactionDate >= :transactionDate");
			}
		    }
		    if (StringUtils.hasText(batch)) {
                hql.append(" and model.id.batch = :batch");		    
	    }	    
		    hql.append(" order by model.id.transactionDate, model.id.shopCode");
		    Query query = session.createQuery(hql.toString());
		    query.setString("brandCode", brandCode);
		    query.setDate("lastTransactionDate", lastTransactionDate);
		    query.setString("isPosting", isPosting);
		    query.setString("batch", batch);
		    if (StringUtils.hasText(shopCode)) {
			query.setString("shopCode", shopCode);		    
		    }else{
			if("T2".equals(brandCode)){
			    StringBuffer shopMachinesByEMP = new StringBuffer("");
			    List<BuShopMachine> allShopMachines = new ArrayList();
			    try {
				allShopMachines = buBasicDataService.getShopMachineForEmployee(brandCode, employeeCode, "Y");
			    } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			    if(allShopMachines != null && allShopMachines.size()>0){
				for(int i=0;i<allShopMachines.size();i++){
				    shopMachinesByEMP.append("'" + (allShopMachines.get(i)).getId().getPosMachineCode() + "', ");		    
				}
			    }	
			    final String allShopMachineByEMP = shopMachinesByEMP.replace(shopMachinesByEMP.lastIndexOf(", "), shopMachinesByEMP.length(), "").toString();
			    log.info("888==qry--SoPostingTally===allShopMachineByEMP = " + allShopMachineByEMP);
			    hql.append(" and model.id.shopCode in (" + allShopMachineByEMP + ")");			    
			}
		    }
		    
		    if(!"Y".equals(isDesignate) && transactionDate != null){
			query.setDate("transactionDate", transactionDate);
		    }
			
		    return query.list();
		}
	});

	return result;	
    }
    
    
    /**
     * 依據POS資料過帳螢幕輸入條件查詢(依過帳單位)
     * 
     * @param conditionMap
     * @param startPage
     * @param pageSize
     * @param searchType
     * @return HashMap
     */
    public HashMap findPostingDataListBySalesUnit(HashMap conditionMap, int startPage, int pageSize, int searchType){
	
	final String orderTypeCodeS = (String) conditionMap.get("orderTypeCodeS");
	final String orderTypeCodeD = (String) conditionMap.get("orderTypeCodeD");
	final String brandCode = (String) conditionMap.get("brandCode");
	final Date transactionBeginDate = (Date) conditionMap.get("transactionBeginDate");
	final Date transactionEndDate = (Date) conditionMap.get("transactionEndDate");
	final String status = (String) conditionMap.get("status");
	final String salesUnit = (String) conditionMap.get("salesUnit");
	final String batch = (String) conditionMap.get("batch");
	final String salesUnitType = (String)conditionMap.get("salesUnitType");
	final String soStatus = (String)conditionMap.get("soStatus");
	final String employeeCode = (String)conditionMap.get("employeeCode");
	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final int type = searchType;
	
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("");
			if(BaseDAO.QUERY_RECORD_COUNT == type) {
				hql.append("select count(*) as rowCount ");
			}else{
				hql.append("select p.shop_code, p.transaction_date,p.batch, s.actual_sales_amount, i.actual_ship_amount, d.total_actual_sales_amount, p.is_posting, s.head_amountS, i.head_amountD, d.visitor_count ");
			}
			hql.append("from erp.so_posting_tally p, erp.so_shop_daily_head d, ");
			
			if("S".equals(salesUnitType)){
				//銷售
				hql.append("(select h.shop_code, h.sales_order_date, sum(h.total_actual_sales_amount) as actual_sales_amount, count(h.head_id) as head_amountS " +
						"from erp.so_sales_order_head h where h.brand_code = :brandCode and h.order_type_code = :orderTypeCodeS and h.status in " + soStatus);
				if(transactionBeginDate != null){
					hql.append(" and h.sales_order_date >= :transactionBeginDate");
				}
				hql.append(" and h.sales_order_date <= :transactionEndDate group by h.shop_code, h.sales_order_date) s, ");
				//出貨
				hql.append("(select h.shop_code, h.ship_date, sum(h.total_actual_ship_amount) as actual_ship_amount, count(h.head_id) as head_amountD " +
						"from erp.IM_DELIVERY_HEAD h where h.brand_code = :brandCode and h.order_type_code = :orderTypeCodeD and h.status in " + soStatus);
				if(transactionBeginDate != null){
					hql.append(" and h.ship_date >= :transactionBeginDate");
				}
				hql.append(" and h.ship_date <= :transactionEndDate group by h.shop_code, h.ship_date) i ");
				
				hql.append("where p.shop_code = s.shop_code(+)");
				hql.append("and p.shop_code = i.shop_code(+)");
			}else{
				log.info("else UnitType");
				//銷售
				hql.append("(select h.pos_machine_code, h.sales_order_date, sum(h.total_actual_sales_amount) as actual_sales_amount, count(h.head_id) as head_amountS " +
						"from erp.so_sales_order_head h where h.brand_code = :brandCode and h.order_type_code = :orderTypeCodeS  and h.schedule = :batch and h.status in " + soStatus );
				if(transactionBeginDate != null){
					hql.append(" and h.sales_order_date >= :transactionBeginDate");
				}
				hql.append(" and h.sales_order_date <= :transactionEndDate group by h.pos_machine_code, h.sales_order_date) s, ");
				//出貨
				hql.append("(select h.pos_machine_code, h.ship_date, sum(h.total_actual_ship_amount) as actual_ship_amount, count(h.head_id) as head_amountD " +
						"from erp.IM_DELIVERY_HEAD h where h.brand_code = :brandCode and h.order_type_code = :orderTypeCodeD  and h.schedule = :batch and h.status in " + soStatus);
				if(transactionBeginDate != null){
					hql.append(" and h.ship_date >= :transactionBeginDate");
				}
				hql.append(" and h.ship_date <= :transactionEndDate group by h.pos_machine_code, h.ship_date) i ");
				
				hql.append("where p.shop_code = s.pos_machine_code(+)");
				hql.append("and p.shop_code = i.pos_machine_code(+)");
				//hql.append(" and p.batch = h.schedule(+)");
			}
			hql.append(" and p.brand_code = :brandCode");
			hql.append(" and p.shop_code = d.shop_code(+)");
			hql.append(" and p.transaction_date = s.sales_order_date(+)");
			hql.append(" and p.transaction_date = i.ship_date(+)");
			hql.append(" and p.transaction_date = d.sales_date(+)");
			//hql.append(" and p.batch = h.schedule(+)");
			
			if(transactionBeginDate != null)
				hql.append(" and p.transaction_date >= :transactionBeginDate");
			if(transactionEndDate != null)
				hql.append(" and p.transaction_date <= :transactionEndDate");
			if(StringUtils.hasText(status))
				hql.append(" and p.is_posting = :status");
			log.info("In salesUnit = "+salesUnit);
			if(StringUtils.hasText(batch)){
				hql.append(" and p.batch = :batch");
				hql.append(" and d.batch = :batch");
			}
			if(StringUtils.hasText(salesUnit)){
				hql.append(" and p.shop_code = :salesUnit");

			}else{
				
				
				if("T2".equals(brandCode)){
					
					StringBuffer shopMachinesByEMP = new StringBuffer("");
					List<BuShopMachine> allShopMachines = new ArrayList();
					try {
						allShopMachines = buBasicDataService.getShopMachineForEmployee(brandCode, employeeCode, "Y");
					} catch (Exception e) {

						e.printStackTrace();
					}
					if(allShopMachines != null && allShopMachines.size()>0){
						for(int i=0;i<allShopMachines.size();i++){
							shopMachinesByEMP.append("'" + (allShopMachines.get(i)).getId().getPosMachineCode() + "', ");		    
						}
					}	
					final String allShopMachineByEMP = shopMachinesByEMP.replace(shopMachinesByEMP.lastIndexOf(", "), shopMachinesByEMP.length(), "").toString();
					hql.append(" and p.shop_code in (" + allShopMachineByEMP + ")");			    
					log.info("allShopMachineByEMP = "+allShopMachineByEMP);
				}
			}
			
			hql.append(" order by p.transaction_date, p.shop_code");
			log.info("hql = " + hql.toString());
			log.info("batch = "+batch);
			Query query = session.createSQLQuery(hql.toString());
			if(BaseDAO.QUERY_SELECT_RANGE == type) {
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
			}
			log.info("666666666666"+orderTypeCodeS + orderTypeCodeD + brandCode);
			query.setString("orderTypeCodeS", orderTypeCodeS);
			query.setString("orderTypeCodeD", orderTypeCodeD);
			query.setString("brandCode", brandCode);
			if(transactionBeginDate != null){
				query.setDate("transactionBeginDate", transactionBeginDate);
			}				
			if(transactionEndDate != null){
				query.setDate("transactionEndDate", transactionEndDate);
			}				
			if(StringUtils.hasText(status)){
				query.setString("status", status);
			}				
			log.info("67777=");
			if(StringUtils.hasText(batch)){
				query.setString("batch", batch);
			}				
			if(StringUtils.hasText(salesUnit)){
				query.setString("salesUnit", salesUnit);
			}				
			return query.list();
		}
	});

	HashMap returnResult = new HashMap();		
	returnResult.put(BaseDAO.TABLE_LIST, (BaseDAO.QUERY_SELECT_RANGE == type || BaseDAO.QUERY_SELECT_ALL == type)? result : null);			
	if(result.size() == 0){
		returnResult.put(BaseDAO.TABLE_RECORD_COUNT, 0L);
	}else{
		returnResult.put(BaseDAO.TABLE_RECORD_COUNT, (BaseDAO.QUERY_SELECT_RANGE == type || BaseDAO.QUERY_SELECT_ALL == type)? result.size() : Long.valueOf(result.get(0).toString()));
	}
	return returnResult;
    }


    public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
        this.buBasicDataService = buBasicDataService;
    }
    
    /**
     * 依據POS資料過帳.(有機台/專櫃(起)、機台/專櫃(迄))
     * 
     * @param conditionMap
     */
    public List findPostingDataListBySalesUnit(HashMap conditionMap){
    	//log.info("enter===findPostingDataListBySalesUnit");
    	final String orderTypeCode = (String) conditionMap.get("orderTypeCode");
    	final String brandCode = (String) conditionMap.get("brandCode");
    	final Date transactionDate = (Date) conditionMap.get("transactionDate");
    	//final String status = (String) conditionMap.get("status");
    	final String postingSalesUnitBegin = (String) conditionMap.get("postingSalesUnitBegin");
    	final String postingSalesUnitEnd = (String) conditionMap.get("postingSalesUnitEnd");
    	final String salesUnitType = (String)conditionMap.get("salesUnitType");
    	//final String soStatus = (String)conditionMap.get("soStatus");
    	final String employeeCode = (String)conditionMap.get("employeeCode");
    	final String batch = (String) conditionMap.get("batch");
    	StringBuffer shopMachinesByEMP = new StringBuffer("");
    	List<BuShopMachine> allShopMachines = new ArrayList();
    	String subSql = "";
    	String subSql2 = "";
    	String subSql3 = "";

    	if(!"T2".equals(brandCode)){
    		try {
    			allShopMachines = buBasicDataService.getShopMachineForEmployee(brandCode, employeeCode, "Y");
    		} catch (Exception e) {
    			e.printStackTrace();
    		}			    
    	}else{//若為T2，找到範圍內人員所屬之機台
    		try {
    			allShopMachines = buShopMachineDAO.getShopMachineForEmployee(brandCode,
    					employeeCode, "Y", salesUnitType, postingSalesUnitBegin, postingSalesUnitEnd);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}	
    	}
    	if(allShopMachines != null && allShopMachines.size()>0){
    		log.info("allShopMachines");
    		for(int i=0;i<allShopMachines.size();i++){
    			if(!"T2".equals(brandCode)){
    				shopMachinesByEMP.append("'" + (allShopMachines.get(i)).getId().getShopCode() + "', ");	
    			}else{
    				shopMachinesByEMP.append("'" + (allShopMachines.get(i)).getId().getPosMachineCode() + "', ");	
    			}			    
    		}
    	}

    	if(shopMachinesByEMP.length()>0){ 
    		final String allShopMachineByEMP = shopMachinesByEMP.replace(shopMachinesByEMP.lastIndexOf(", "), shopMachinesByEMP.length(), "").toString();
    		subSql = " and p.shop_code in (" + allShopMachineByEMP + ")";
    		subSql2 = " and h.shop_code in (" + allShopMachineByEMP + ")";
    		subSql3 = " and h.POS_MACHINE_CODE in (" + allShopMachineByEMP + ")";
    	}

    	final String subSqlP = subSql;
    	final String subSqlHS = subSql2;
    	final String subSqlHM = subSql3;

    	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
    		public Object doInHibernate(Session session) throws HibernateException, SQLException {
    			StringBuffer hql = new StringBuffer("");
    			hql.append("select p.shop_code, p.transaction_date, s.actual_sales_amount, d.total_actual_sales_amount, p.is_posting, s.head_amount, d.visitor_count ");
    			hql.append("from erp.so_posting_tally p, erp.so_shop_daily_head d, ");
    			if("S".equals(salesUnitType)){		
    				hql.append("(select h.shop_code, h.sales_order_date, sum(h.total_actual_sales_amount) as actual_sales_amount, count(h.head_id) as head_amount " +
    				"from erp.so_sales_order_head h where h.brand_code = :brandCode and h.schedule = :batch and h.order_type_code = :orderTypeCode and h.status in ('SIGNING','FINISH','CLOSE') ");
    				hql.append(" and h.sales_order_date = :transactionDate " + subSqlHS +
    				" group by h.shop_code, h.sales_order_date) s ");               
    				hql.append("where p.shop_code = s.shop_code(+)");
    			}else{
    				hql.append("(select h.pos_machine_code, h.sales_order_date, sum(h.total_actual_sales_amount) as actual_sales_amount, count(h.head_id) as head_amount " +
    				"from erp.so_sales_order_head h where h.brand_code = :brandCode and h.schedule = :batch and h.order_type_code = :orderTypeCode and h.status in ('SIGNING','FINISH','CLOSE') ");
    				hql.append(" and h.sales_order_date = :transactionDate " + subSqlHM + " group by h.pos_machine_code, h.sales_order_date) s ");		
    				hql.append("where p.shop_code = s.pos_machine_code(+)");
    			}

    			hql.append(" and p.shop_code = d.shop_code(+) and p.transaction_date = s.sales_order_date(+) and p.transaction_date = d.sales_date(+)");
    			hql.append(" and p.brand_code = :brandCode");
    			hql.append(" and p.transaction_date = :transactionDate");
    			//if(StringUtils.hasText(status)){
    				//hql.append(" and p.is_posting = :status");
    			//}

    			hql.append(subSqlP);
    			hql.append(" order by p.transaction_date, p.shop_code");
    			log.info(" hql.toString() = " + hql.toString());
    			Query query = session.createSQLQuery(hql.toString());
    			query.setString("orderTypeCode", orderTypeCode);
    			query.setString("brandCode", brandCode);
    			query.setDate("transactionDate", transactionDate);
    			if(StringUtils.hasText(batch)){
    			query.setString("batch", batch);
    			}
    			//query.setString("soStatus", soStatus);
    			//if(StringUtils.hasText(status)){
    				//query.setString("status", status);
    			//}
    			log.info("batch = "+batch);
    			log.info("orderTypeCode = "+orderTypeCode);
    			log.info("brandCode = "+brandCode);
    			log.info("transactionDate = "+transactionDate);
    			//log.info("status = "+status);
    			return query.list();
    		}
    	});
    	//log.info("leave===findPostingDataListBySalesUnit");
    	return result;
    }


    public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
        this.buShopMachineDAO = buShopMachineDAO;
    }
    
    public SoPostingTally findById(SoShopDailyHeadId id){
    	
    	StringBuffer hql = new StringBuffer("from SoPostingTally as model where model.id.shopCode = ? and model.id.transactionDate = ?");
    	List<SoPostingTally> result = getHibernateTemplate().find(hql.toString(), new Object[]{id});
    	return (result != null && result.size() > 0 ? result.get(0) : null);
        }
    public SoPostingTally findByPosingId(SoPostingTallyId soPostingTallyId) {
		log.debug("getting BuSupplier instance with id: " + soPostingTallyId);
		try {
			SoPostingTally instance = (SoPostingTally) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.SoPostingTally", soPostingTallyId);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
    public SoPostingTally findHeadByBatch(String shopCode,Date transactionDate, String batch) {
    	log.info("shop:"+shopCode);
    	log.info("tran:"+transactionDate);
    	log.info("batch:"+batch);
	
	StringBuffer hql = new StringBuffer("from SoPostingTally as model ");
	hql.append("where model.id.shopCode = ? ");
	hql.append("and model.id.transactionDate = ? ");
	hql.append("and model.id.batch = ? ");
	List<SoPostingTally> result = getHibernateTemplate().find(hql.toString(),
			new Object[] { shopCode, transactionDate ,batch});
	log.info("findbatch2222"+result.size());
	return (result != null && result.size() > 0 ? result.get(0) : null);
}
    public List findPostingDataListBySalesUnit(String brandCode,String shopCode){
    	
    	final String searchBrandCode = brandCode;
    	final String searchShopCode = shopCode;
    	Date date = new Date();
    	final String dateString =  DateUtils.format(date, DateUtils.C_DATA_PATTON_YYYYMMDD);
    	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
    		public Object doInHibernate(Session session) throws HibernateException, SQLException {

    			StringBuffer hql = new StringBuffer("");

    				hql.append("select NVL(sum(h.total_actual_sales_amount),0) as actual_sales_amount, NVL(count(h.head_id),0) as head_amountS ");
    				hql.append(" from erp.so_sales_order_head h ");
    				hql.append(" where h.brand_code = '"+searchBrandCode+"' ");
    				hql.append(" and h.shop_code = '"+searchShopCode+"' ");
    				hql.append(" and h.order_type_code = 'SOP' ");
    				hql.append(" and h.schedule = '99' ");
    				hql.append(" and h.status in ('SIGNING', 'FINISH', 'CLOSE') ");
    				hql.append(" and h.sales_order_date >= TO_DATE(" + dateString + ",'yyyymmdd') ");
    				hql.append(" and h.sales_order_date <= TO_DATE(" + dateString + ",'yyyymmdd') ");
    			log.info("hql = " + hql.toString());

    			Query query = session.createSQLQuery(hql.toString());
				
    			return query.list();
    		}
    	});
    	return result;
    }
}