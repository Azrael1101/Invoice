package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.saxon.functions.Data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.CustomsConfiguration;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.PoVerificationSheet;
import tw.com.tm.erp.utils.DateUtils;

public class ImAdjustmentHeadDAO extends BaseDAO<ImAdjustmentHead> {
	private static final Log log = LogFactory.getLog(ImAdjustmentHeadDAO.class);
	public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
    public List<ImAdjustmentHead> find(HashMap findObjs) {

	final HashMap fos = findObjs;

	List<ImAdjustmentHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {

		StringBuffer hql = new StringBuffer("from ImAdjustmentHead as model where 1=1 ");
		if (StringUtils.hasText((String) fos.get("orderTypeCode")))
		    hql.append(" and model.orderTypeCode = :orderTypeCode ");

		if (StringUtils.hasText((String) fos.get("status")))
		    hql.append(" and model.status = :status ");

		if (StringUtils.hasText((String) fos.get("startOrderNo")))
		    hql.append(" and model.orderNo >= :startOrderNo ");

		if (StringUtils.hasText((String) fos.get("endOrderNo")))
		    hql.append(" and model.orderNo <= :endOrderNo ");

		if (StringUtils.hasText((String) fos.get("brandCode")))
		    hql.append(" and model.brandCode = :brandCode ");

		if (StringUtils.hasText((String) fos.get("sourceOrderTypeCode")))
		    hql.append(" and model.sourceOrderTypeCode = :sourceOrderTypeCode ");

		if (StringUtils.hasText((String) fos.get("sourceOrderNo")))
		    hql.append(" and model.sourceOrderNo = :sourceOrderNo ");

		hql.append(" order by orderNo desc ");
		Query query = session.createQuery(hql.toString());
		query.setFirstResult(0);
		query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

		if (StringUtils.hasText((String) fos.get("orderTypeCode")))
		    query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));

		if (StringUtils.hasText((String) fos.get("status")))
		    query.setString("status", (String) fos.get("status"));

		if (StringUtils.hasText((String) fos.get("startOrderNo")))
		    query.setString("startOrderNo", (String) fos.get("startOrderNo"));

		if (StringUtils.hasText((String) fos.get("endOrderNo")))
		    query.setString("endOrderNo", (String) fos.get("endOrderNo"));

		if (StringUtils.hasText((String) fos.get("brandCode")))
		    query.setParameter("brandCode", fos.get("brandCode"));

		if (StringUtils.hasText((String) fos.get("sourceOrderTypeCode")))
		    query.setParameter("sourceOrderTypeCode", fos.get("sourceOrderTypeCode"));

		if (StringUtils.hasText((String) fos.get("sourceOrderNo")))
		    query.setParameter("sourceOrderNo", fos.get("sourceOrderNo"));

		return query.list();
	    }
	});

	return re;
    }

    public ImAdjustmentHead findById(Long headId) {
	return (ImAdjustmentHead) findByPrimaryKey(ImAdjustmentHead.class, headId);
    }
    
    public List<CustomsConfiguration> getCustomsConfiguration(){
		return getHibernateTemplate().executeFind(
				new HibernateCallback(){
					public Object doInHibernate(Session session) 
						throws HibernateException,SQLException{
						StringBuffer hql = new StringBuffer("from CustomsConfiguration order by Reserve2");
						System.out.println(hql.toString());
						Query query = session.createQuery(hql.toString());
						return query.list();
					}
				}
		);
	}

    /**
     * 依據品牌及狀態查詢調整單
     * @param brandCode
     * @param status
     * @return
     */
    public List<ImAdjustmentHead> findAdjustmentByProperty(String brandCode, String status){
	StringBuffer hql = new StringBuffer(
	"from ImAdjustmentHead as model where model.brandCode = ?");
	hql.append(" and model.status = ?");
	List<ImAdjustmentHead> result = getHibernateTemplate().find(
		hql.toString(),	new Object[] { brandCode, status});
	return result;

    }

	public List<ImAdjustmentHead>findAdjustmentByProperty(final String brandCode, final String status, final Date startDate, final Date endDate) {
		log.info("findAdjustmentByProperty brandCode=" + brandCode+ "/"+DateUtils.format(startDate)+ "/"+DateUtils.format(endDate));
		List<ImAdjustmentHead> temp = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					StringBuffer hql =  new StringBuffer("from ImAdjustmentHead as model where 1=1");
					hql.append(" and model.brandCode = :brandCode ");
					hql.append(" and model.status = :status ");
					if (null != startDate)
						hql.append(" and model.adjustmentDate >= :startDate");
					
					if (null != endDate)
						hql.append(" and model.adjustmentDate <= :endDate");
					hql.append(" order by adjustmentDate asc");
					Query query = session.createQuery(hql.toString());
					query.setString("brandCode", brandCode);
					query.setString("status", status);
					if (null != startDate)
						query.setDate("startDate", startDate);
					
					if (null != endDate)
						query.setDate("endDate", endDate);
					return query.list();
				}
			});
			return temp;
		}
	
    /**
     * 查詢月結日之前還沒有完成關帳的單調整單
     * 
     * @param brandCodeArray
     * @param orderDate
     * @param statusArray
     * @return
     */
    public List<ImAdjustmentHead> findImAdjustmentByCriteria(final String[] brandCodeArray, final Date orderDate,
	    final String[] statusArray) {

	final Date oDate = DateUtils.getShortDate(orderDate);
	List<ImAdjustmentHead> result = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		Criteria ctr = session.createCriteria(ImAdjustmentHead.class);
		ctr.add(Expression.le("adjustmentDate", oDate));
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
    
    public Object findCustomsSended(){
		 return getHibernateTemplate().executeFind(
				new HibernateCallback(){
					public Object doInHibernate(Session session) 
						throws HibernateException,SQLException{
						StringBuffer hql = new StringBuffer();
						hql.append("select count(HEAD.HEAD_ID) from IM_ADJUSTMENT_HEAD head where 1=1 and HEAD.TRAN_ALLOW_UPLOAD is not null and HEAD.CREATION_DATE >= TO_DATE('12/01/2014 00:00:00', 'MM/DD/YYYY HH24:MI:SS') and HEAD.ORDER_TYPE_CODE='MEG' ");
						//hql.append("select MAX(head.send_Rp_No) from erp.im_movement_head head where head.send_Rp_No is not null");	
						System.out.println(hql.toString());
						Query query = session.createSQLQuery(hql.toString());
						System.out.println("query.list():"+query.list());
						return query.list();
					}
				}
		); 
	}
	
	public Object findCustomsWaitToSend(){
		 return getHibernateTemplate().executeFind(
				new HibernateCallback(){
					public Object doInHibernate(Session session) 
						throws HibernateException,SQLException{
						StringBuffer hql = new StringBuffer();
						hql.append("select count(HEAD.HEAD_ID) from IM_ADJUSTMENT_HEAD head where 1=1 and (HEAD.TRAN_ALLOW_UPLOAD is null OR HEAD.TRAN_ALLOW_UPLOAD = '' ) and HEAD.CREATION_DATE >= TO_DATE('12/01/2014 00:00:00', 'MM/DD/YYYY HH24:MI:SS') and HEAD.ORDER_TYPE_CODE='MEG' and HEAD.STATUS not in ( 'SAVE' , 'VOID' )");
						//hql.append("select MAX(head.send_Rp_No) from erp.im_movement_head head where head.send_Rp_No is not null");	
						System.out.println(hql.toString());
						Query query = session.createSQLQuery(hql.toString());
						System.out.println("query.list():"+query.list());
						return query.list();
					}
				}
		); 
	}
    
    public List getResNoSq(){
    	try{
            return getHibernateTemplate().executeFind(
                    new HibernateCallback(){
                        public Object doInHibernate(Session session) 
                            throws HibernateException,SQLException{
                            StringBuffer hql = new StringBuffer();
                            hql.append("select ERP.RES_NO_SEQ.nextval from dual");
                            //hql.append("select MAX(head.send_Rp_No) from erp.im_movement_head head where head.send_Rp_No is not null");    
                            System.out.println(hql.toString());
                            Query query = session.createSQLQuery(hql.toString());
                            return query.list();
                        }
                    }
            ); 
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public List<ImAdjustmentHead> findAdjust2Sheet(String brandCode, String sourceOrderTypeCode ,String sourceOrderNo,String status ) {
    	log.info("listDAO1111"+brandCode+sourceOrderTypeCode+sourceOrderNo+status);
    	StringBuffer hql = new StringBuffer("from ImAdjustmentHead as model where model.brandCode = ?");
    	hql.append(" and model.sourceOrderTypeCode = ?");
    	hql.append(" and model.sourceOrderNo = ?");
    	hql.append(" and model.status = ?");
    	List<ImAdjustmentHead> result = getHibernateTemplate().find(hql.toString(),
		new Object[] { brandCode,sourceOrderTypeCode,sourceOrderNo,status});
    	log.info("listDAO2222"+result.size());
    	return result;

   }
    
	public List findScheduleOrders(String schedule,Date closeDate,String startHours,String endHours) throws Exception{
		
		
        final String date = DateUtils.format(closeDate);
        final String argStartHours = startHours;
        final String argEndHours = endHours;
		System.out.println("adj轉型後:"+DateUtils.format(closeDate));
		
		List<CmMovementHead> re = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer(
								"from ImAdjustmentHead as model where 1=1 ");
						

						
							hql.append(" and model.status IN ( 'FINISH','CLOSE' ) ");

						
                       
							hql.append(" and model.lastUpdateDate between to_date('"+date+" "+argStartHours+"','yyyy-mm-dd HH24:MI:SS') and "+"to_date('"+date+" "+argEndHours+"','yyyy-mm-dd HH24:MI:SS')");

						
                        hql.append(" and model.orderTypeCode in ('AEG','BEF') order by lastUpdateDate desc ");

						System.out.println(hql.toString());
						Query query = session.createQuery(hql.toString());
						query.setFirstResult(0);
						//query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

						
						return query.list();
					}
				});

		return re;
		
		
	}

	public List findByBeanNId(String bean,final Long id) {
		try {
			System.out.println("id:"+id);
			String head = "";
			if(bean.indexOf("Head")==-1){
				head = bean.substring(0,bean.indexOf("Line")==-1?bean.indexOf("Item"):bean.indexOf("Line"));
				head = head+"Head";
				head = head.substring(0,1).toLowerCase() + head.substring(1);
			}else{
				head = "headId";
			}
			
			//System.out.println("!!!!!!!!!!!!!!!:"+head);
			final StringBuffer hql = new StringBuffer("from "+bean+" as model where model.headId = :head");
			//StringBuffer hql = new StringBuffer("from "+(bean.toString())+" as model where model.headId = :headId");
			System.out.println(hql.toString());
			
			return getHibernateTemplate().executeFind(
					new HibernateCallback(){
						public Object doInHibernate(Session session) 
							throws HibernateException,SQLException{
							Query query = session.createQuery(hql.toString());
							query.setLong("headId", id);
							return query.list();
						}
					}
			);
		} catch (RuntimeException re) {
			throw re;
		}
	}
    
    public List<ImAdjustmentHead> findAdjustForAIFOrderNo(String brandCode, String orderTypeCode ,String orderNo ) {
    	
    	StringBuffer hql = new StringBuffer("from ImAdjustmentHead as model where model.brandCode = ?");
    	hql.append(" and model.orderTypeCode = ?");
    	hql.append(" and model.orderNo = ?");    	
    	List<ImAdjustmentHead> result = getHibernateTemplate().find(hql.toString(),
		new Object[] { brandCode,orderTypeCode,orderNo});
    	
    	return result;
    }
    
 
    public List<Object[]> findExtensionPageLine(Long headId,String brandCode, String exportBeanName) {

		final Long hId = headId;
		final String bCode = brandCode;
		
		List<Object[]> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append("SELECT A.INDEX_NO, A.ORIGINAL_DECLARATION_NO, A.ORIGINAL_DECLARATION_SEQ, A.RESERVE2, A.RESERVE3, A.CUSTOMS_ITEM_CODE, A.ITEM_C_NAME, A.ORG_IMPORT_DATE, A.EXPIRY_DATE, A.EXTENSION_DATE , A.UNIT, A.QTY, A.RESERVE1 ");
				hql.append("FROM IM_ADJUSTMENT_LINE A ");
				hql.append("JOIN IM_ADJUSTMENT_HEAD B ON A.HEAD_ID = B.HEAD_ID ");
				hql.append(" and A.HEAD_ID = :headId ");
				hql.append(" and B.BRAND_CODE = :brandCode ");
				hql.append(" order by INDEX_NO");
				Query query = session.createSQLQuery(hql.toString());
				if (hId != null)
					query.setLong("headId", hId);
				if (bCode != null)
					query.setString("brandCode", bCode);
				return query.list();
			}
		});
		log.info("imMovementItems.headId:" + headId);
		log.info("imMovementItems.size:" + result.size());
		return result;
	}
    
    //使用關別及文號查詢展延紀錄
    public List<ImAdjustmentHead> findExtensionByFileNo(String fileNo, String customsWareHouseCode){
    	List<ImAdjustmentHead> imAdjustmentHeads = null;
    	
    	String orderTypeCode = "ACE";
    	String status = "FINISH";
    	
    	StringBuffer hql = new StringBuffer();
    	hql.append("from ImAdjustmentHead  as model ");
    	hql.append(" where model.fileNo = ? ");
    	hql.append(" and model.orderTypeCode = ? ");
    	hql.append(" and model.status = ? ");
    	hql.append(" and model.customsWarehouseCode = ? ");
    	imAdjustmentHeads = getHibernateTemplate().find(hql.toString(),
    			new Object[] { fileNo, orderTypeCode, status, customsWareHouseCode});
    	
    	return imAdjustmentHeads;	
    }
    
    //展延單查詢頁面
    public List<ImAdjustmentHead> findExtension(String brandCode, String orderTypeCode, String orderNo, String status,
    		String fileNo, String isSpecial, Date createDateStart, Date createDateEnd){
    	List<ImAdjustmentHead> imAdjustmentHeads = null;  	
    	List<Object> objects = new ArrayList();
    	
    	StringBuffer hql = new StringBuffer();
    	hql.append("from ImAdjustmentHead as model ");
    	hql.append(" where 1 = 1 ");
    	hql.append(" and model.orderNo NOT LIKE ? ");
    	objects.add("TMP%");
    	
    	if(!"".equals(brandCode)){
    		hql.append(" and model.brandCode = ? ");
    		objects.add(brandCode);
    	}
    	
    	if(!"".equals(orderTypeCode)){
    		hql.append(" and model.orderTypeCode = ? ");
    		objects.add(orderTypeCode);
    	}
    	
    	if(!"".equals(orderNo)){
    		hql.append(" and model.orderNo = ? ");
    		objects.add(orderNo);
    	}
    	
    	if(!"".equals(status)){
    		hql.append(" and model.status = ? ");
    		objects.add(status);
    	}
    	
    	if(!"".equals(fileNo)){
    		hql.append(" and model.fileNo = ? ");
    		objects.add(fileNo);
    	}
    	
    	if(!"".equals(isSpecial)){
    		if("Y".equals(isSpecial)){
    			hql.append(" and model.isSpecial = ? ");
    			objects.add(isSpecial);
    		}else{
    			hql.append(" and ( model.isSpecial <> ? or model.isSpecial is null ) ");
    			objects.add("Y");
    		}  
    	}
    	
    	if(createDateStart != null){
    		hql.append(" and model.creationDate >= ? ");
    		objects.add(createDateStart);
    	}
    	
    	if(createDateEnd != null){
    		hql.append(" and model.creationDate <= ? ");
    		objects.add(createDateEnd);
    	}
    	
    	hql.append(" order by orderNo Desc");
    	
    	log.info("hql = " + hql.toString());
    	
    	imAdjustmentHeads = getHibernateTemplate().find(hql.toString(),
    			objects.toArray() );
    	
    	return imAdjustmentHeads;
    }
    
    public HashMap findExtension(HashMap findObjs, int startPage, int pageSize, String searchType){
    	
    	final HashMap fos = findObjs;
    	final int startRecordIndexStar = startPage * pageSize;
    	final int pSize = pageSize;
    	final String type = searchType;
    	
    	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
    		public Object doInHibernate(Session session) throws HibernateException, SQLException {
    			
    			StringBuffer hql = new StringBuffer("");
    	    	
    	    	if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.headId) as rowCount from ImAdjustmentHead as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
					hql.append("select model.headId from ImAdjustmentHead as model where 1=1 ");
				} else {
					hql.append("from ImAdjustmentHead as model where 1=1 ");
				}
    	    	
    	    	hql.append(" and model.orderNo NOT LIKE 'TMP%' ");
    			
    	    	if(StringUtils.hasText((String) fos.get("brandCode")))
    	    		hql.append(" and model.brandCode = :brandCode ");
    	    	    	    	
    	    	if(StringUtils.hasText((String) fos.get("orderTypeCode")))
    	    		hql.append(" and model.orderTypeCode = :orderTypeCode ");
    	    	
    	    	if(StringUtils.hasText((String) fos.get("orderNo")))
    	    		hql.append(" and model.orderNo = :orderNo ");
    	    	
    	    	if(StringUtils.hasText((String) fos.get("status")))
    	    		hql.append(" and model.status = :status ");
    	    	
    	    	if(StringUtils.hasText((String) fos.get("fileNo")))
    	    		hql.append(" and model.fileNo = :fileNo ");
    	    	
    	    	//特殊展延單 = Y , 一般展延單 = N or Null
    	    	if(StringUtils.hasText((String) fos.get("isSpecial"))){
    	    		if("Y".equals((String) fos.get("isSpecial"))){
    	    			hql.append(" and model.isSpecial = :isSpecial ");   	    			
    	    		}else{
    	    			hql.append(" and ( model.isSpecial = :isSpecial or model.isSpecial is null ) ");
    	    		}  
    	    	}
    	    	
    	    	if (null != ((Date) fos.get("createDateStart")))
					hql.append(" and model.creationDate >= :createDateStart ");
    	    	
    	    	if (null != ((Date) fos.get("createDateEnd")))
					hql.append(" and model.creationDate <= :createDateEnd ");

    	    	hql.append(" order by orderNo Desc");
    	    	
    	    	log.info("hql = " + hql.toString());
    			
    			Query query = session.createQuery(hql.toString());
    			
    			if (QUARY_TYPE_SELECT_RANGE.equals(type) || QUARY_TYPE_SELECT_ALL.equals(type)) {
    				log.info("type: " + type + " ,startFrom: " + startRecordIndexStar + " to " + pSize);
    				query.setFirstResult(startRecordIndexStar);
    				query.setMaxResults(pSize);
    			}
    			
    			if (StringUtils.hasText((String) fos.get("brandCode")))
					query.setString("brandCode", (String) fos.get("brandCode"));
    			
    			if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));
    			
    			if (StringUtils.hasText((String) fos.get("orderNo")))
					query.setString("orderNo", (String) fos.get("orderNo"));
    			
    			if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));
    			
    			if (StringUtils.hasText((String) fos.get("fileNo")))
					query.setString("fileNo", (String) fos.get("fileNo"));
    			
    			if (StringUtils.hasText((String) fos.get("isSpecial")))
					query.setString("isSpecial", (String) fos.get("isSpecial"));
    			
    			if (null != ((Date) fos.get("createDateStart")))
    				query.setDate("createDateStart", DateUtils.parseDateTime(DateUtils.format((Date) fos.get("createDateStart"))
							+ " 00:00:00"));
    			
    			if (null != ((Date) fos.get("createDateEnd")))
    				query.setDate("createDateEnd", (Date) fos.get("createDateEnd"));
    			
    			return query.list();
    			
    		}
    	});
    	
    	log.info("imAdjustmentHeads.form:" + result.size());
    	
    	HashMap returnResult = new HashMap();
		returnResult.put("form",  result);
		
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			log.info("imAdjustmentHeads.size:" + result.get(0));
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long
							.valueOf(result.get(0).toString()));
		}
    	return returnResult;
    }
    
//    public List getConNoSeq(){
//		 return getHibernateTemplate().executeFind(
//				new HibernateCallback(){
//					public Object doInHibernate(Session session) 
//						throws HibernateException,SQLException{
//						StringBuffer hql = new StringBuffer();
//						hql.append("select ERP.IM_ADJUSTMENT_CON_NO.nextval from dual");
//						System.out.println(hql.toString());
//						Query query = session.createSQLQuery(hql.toString());
//						return query.list();
//					}
//				}
//		); 
//	}

}
