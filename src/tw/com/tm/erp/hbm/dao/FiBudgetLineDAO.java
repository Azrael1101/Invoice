package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.FiBudgetHead;
import tw.com.tm.erp.hbm.bean.FiBudgetLine;
import tw.com.tm.erp.hbm.bean.PoVerificationSheet;
import tw.com.tm.erp.utils.AjaxUtils;

public class FiBudgetLineDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(FiBudgetLineDAO.class);
	public List<FiBudgetLine> find(final Long headId, final String budgeType,
			final String itemBrandCode, final String category01,
			final String category02) {
		log.info("List<FiBudgetLine> find == "+ headId + budgeType + itemBrandCode + category01 + category02);
		List<FiBudgetLine> re = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						String currBudgetType = budgeType; // 扣預算方式
						String currItemBrand = itemBrandCode; // 商品品牌
						String currCategory01 = category01; // 大類
						String currCategory02 = category02; // 中類

						if ("A".equals(currBudgetType)) { // "A"類:ALL
							currItemBrand = "ALL";
							currCategory01 = "ALL";
							currCategory02 = "ALL";
						} else if ("B".equals(currBudgetType)) { // "B"類:品牌
							currCategory01 = "ALL";
							currCategory02 = "ALL";
						} else if ("C".equals(currBudgetType)) { // "C"類:大類
							currItemBrand = "ALL";
							currCategory02 = "ALL";
						} else if ("D".equals(currBudgetType)) { // "D"類:中類
							currItemBrand = "ALL";
							currCategory01 = "ALL";
						} else if ("E".equals(currBudgetType)) { // "E"類:品牌+大類
							currCategory02 = "ALL";
						} else if ("F".equals(currBudgetType)) { // "F"類:品牌+中類
							currCategory01 = "ALL";
						} else if ("G".equals(currBudgetType)) { // "G"類:大類+中類
							currItemBrand = "ALL";
						} else if ("H".equals(currBudgetType)) { // "H"類:品牌+大類+中類
							
						}

						StringBuffer hql = new StringBuffer("from FiBudgetLine as model where 1=1 ");
						hql.append(" and model.fiBudgetHead =").append(headId);
						hql.append(" and nvl(model.itemBrandCode,'ALL') ='");
						hql.append(currItemBrand).append("'");
						hql.append(" and nvl(model.categoryTypeCode1,'ALL') ='");
						hql.append(currCategory01).append("'");
						hql.append(" and nvl(model.categoryTypeCode2,'ALL') ='");
						hql.append(currCategory02).append("'");
						hql.append(" order by lineId ");
						System.out.println(hql.toString());
						Query query = session.createQuery(hql.toString());
						query.setFirstResult(0);
						query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
						return query.list();
					}
				});
		return re;
	}

	public Map getSummaryTotalT2(Long headId,long itemBrandCode) throws HibernateException,
			SQLException {

		System.out.println("FiBudgetLineDAO.getSummaryTotal");
		Map resultMap = new HashMap();
		;
		// 統計：總預算, 簽核中金額, 採購已使用金額, 進貨已使用金額, 調整己使用金額
		StringBuffer hql = new StringBuffer("select sum( nvl(model.budgetAmount,0) ), ");
		hql.append(" sum( nvl(model.signingAmount,0) ),");
		hql.append(" sum( nvl(model.poActualAmount,0) ),");
		hql.append(" sum( nvl(model.receiveActualAmount,0) ),");
		hql.append(" sum( nvl(model.adjustActualAmount,0) ) ,");
		hql.append(" sum( nvl(model.poReturnAmount,0) ) ");
		hql.append(" from FiBudgetLine as model where 1=1 ");
		hql.append(" and model.fiBudgetHead =").append(headId);
		hql.append(" and model.lineId =").append(itemBrandCode);		
		System.out.println(hql.toString());
		List result = getHibernateTemplate().find(hql.toString(), null);
		if (result != null) {
			Object[] item = (Object[]) result.get(0);
			resultMap.put("budgetAmount", (Double) item[0]);
			resultMap.put("signingAmount", (Double) item[1]);
			resultMap.put("poActualAmount", (Double) item[2]);
			resultMap.put("receiveActualAmount", (Double) item[3]);
			resultMap.put("adjustActualAmount", (Double) item[4]);
			resultMap.put("poReturnAmount", (Double) item[5]);
			System.out.println("------"+(Double) item[5]);
		}
		return resultMap;
	}
	
	public Map getSummaryTotal(Long headId) throws HibernateException,
	SQLException {

		System.out.println("FiBudgetLineDAO.getSummaryTotal");
		Map resultMap = new HashMap();
		;
//		統計：總預算, 簽核中金額, 採購已使用金額, 進貨已使用金額, 調整己使用金額
		StringBuffer hql = new StringBuffer("select sum( nvl(model.budgetAmount,0) ), ");
		hql.append(" sum( nvl(model.signingAmount,0) ),");
		hql.append(" sum( nvl(model.poActualAmount,0) ),");
		hql.append(" sum( nvl(model.receiveActualAmount,0) ),");
		hql.append(" sum( nvl(model.adjustActualAmount,0) ) ,");
		hql.append(" sum( nvl(model.poReturnAmount,0) ) ");
		hql.append(" from FiBudgetLine as model where 1=1 ");
		hql.append(" and model.fiBudgetHead =").append(headId);
		System.out.println(hql.toString());
		List result = getHibernateTemplate().find(hql.toString(), null);
		if (result != null) {
			Object[] item = (Object[]) result.get(0);
			resultMap.put("budgetAmount", (Double) item[0]);
			resultMap.put("signingAmount", (Double) item[1]);
			resultMap.put("poActualAmount", (Double) item[2]);
			resultMap.put("receiveActualAmount", (Double) item[3]);
			resultMap.put("adjustActualAmount", (Double) item[4]);
			resultMap.put("poReturnAmount", (Double) item[5]);
			System.out.println("------"+(Double) item[5]);
		}
		return resultMap;
	}
	public Map getSummaryTotalT2addCategory(Long headId,long itemBrandCode,String category01) throws HibernateException,
	SQLException {
		Map resultMap = new HashMap();
		try{
			System.out.println("FiBudgetLineDAO.getSummaryTotalT2addCategory"+category01);
			
//			統計：總預算, 簽核中金額, 採購已使用金額, 進貨已使用金額, 調整己使用金額
			StringBuffer hql = new StringBuffer("select sum( nvl(model.budgetAmount,0) ), ");
			hql.append(" sum( nvl(model.signingAmount,0) ),");
			hql.append(" sum( nvl(model.poActualAmount,0) ),");
			hql.append(" sum( nvl(model.receiveActualAmount,0) ),");
			hql.append(" sum( nvl(model.adjustActualAmount,0) ) ,");
			hql.append(" sum( nvl(model.poReturnAmount,0) ) ");
			hql.append(" from FiBudgetLine as model where 1=1 ");
			hql.append(" and model.fiBudgetHead =").append(headId);
			hql.append(" and model.lineId =").append(itemBrandCode);
			hql.append(" and model.categoryTypeCode1 ='").append(category01).append("'");							   
			System.out.println(hql.toString());
			List result = getHibernateTemplate().find(hql.toString(), null);
			if (result != null) {
				Object[] item = (Object[]) result.get(0);
				resultMap.put("budgetAmount", (Double) item[0]);
				resultMap.put("signingAmount", (Double) item[1]);
				resultMap.put("poActualAmount", (Double) item[2]);
				resultMap.put("receiveActualAmount", (Double) item[3]);
				resultMap.put("adjustActualAmount", (Double) item[4]);
				resultMap.put("poReturnAmount", (Double) item[5]);
				System.out.println("------"+(Double) item[5]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}
	public FiBudgetLine findById(Long headId) {
		return (FiBudgetLine) findByPrimaryKey(FiBudgetLine.class, headId);
	}

	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return List<CmDeclarationItem>
	 */
	public List<FiBudgetLine> findPageLine(Long headId, int startPage,
			int pageSize) {
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<FiBudgetLine> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from FiBudgetLine as model where 1=1 ");
						if (hId != null)
							hql.append(" and model.fiBudgetHead.headId = :headId order by indexNo");
						Query query = session.createQuery(hql.toString());
						query.setFirstResult(startRecordIndexStar);
						query.setMaxResults(pSize);
						if (hId != null)
							query.setLong("headId", hId);
						return query.list();
					}
				});
		return result;
	}

	/**
	 * find page line 最後一筆 index
	 * 
	 * @param headId
	 * @return Long
	 */
	public Long findPageLineMaxIndex(Long headId) {

		Long lineMaxIndex = new Long(0);
		final Long hId = headId;
		List<FiBudgetHead> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer(
								"from FiBudgetHead as model where 1=1 ");
						if (hId != null)
							hql.append(" and model.headId = :headId");
						Query query = session.createQuery(hql.toString());
						if (hId != null)
							query.setLong("headId", hId);
						return query.list();
					}
				});
		if (result != null && result.size() > 0) {
			List<FiBudgetLine> fiBudgetLines = result.get(0).getFiBudgetLines();
			if (fiBudgetLines != null && fiBudgetLines.size() > 0) {
				lineMaxIndex = fiBudgetLines.get(fiBudgetLines.size() - 1)
						.getIndexNo();
			}
		}
		return lineMaxIndex;
	}
	
	public FiBudgetLine findByPoItem(Long headId , String itemBrand
	) {
		log.info("FindReceive:"+headId+itemBrand);
		StringBuffer hql = new StringBuffer("from FiBudgetLine as model where 1=1 ");
		hql.append(" and model.fiBudgetHead ="+headId);
		hql.append(" and model.itemBrandCode = ?");
		//StringBuffer hql = new StringBuffer("from FiBudgetLine as model where model.itemBrandCode = ?");
		//hql.append(" and model.itemBrandCode = ?");
		
		List<FiBudgetLine> result = getHibernateTemplate().find(hql.toString(),
				new Object[] {  itemBrand  });
		log.info("Receive:"+result.size());
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	public List<FiBudgetLine> findByItembrand(Long lineId
	) {
    	log.info("find::itemLine"+lineId);
    	StringBuffer hql = new StringBuffer("from FiBudgetLine as model where model.lineId = ? ");		
    	
    	List<FiBudgetLine> result = getHibernateTemplate().find(hql.toString(),
		new Object[] {lineId });
    	log.info("find::itemLine"+result.size());
    	return result;
	}
	public FiBudgetLine findByLine(Long lineId 
	) {
		log.info("FiBudgetLine:"+lineId);
		StringBuffer hql = new StringBuffer("from FiBudgetLine as model where model.lineId = ? ");			
		//StringBuffer hql = new StringBuffer("from FiBudgetLine as model where model.itemBrandCode = ?");
		//hql.append(" and model.itemBrandCode = ?");
		
		List<FiBudgetLine> result = getHibernateTemplate().find(hql.toString(),
				new Object[] {  lineId  });
		log.info("FiBudgetLine::"+result.size());
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	public FiBudgetLine findByRRItem(Long headId , String category01
	) {
		log.info("FindReceive:"+headId+category01);
		StringBuffer hql = new StringBuffer("from FiBudgetLine as model where 1=1 ");
		hql.append(" and model.fiBudgetHead ="+headId);
		hql.append(" and model.categoryTypeCode1 = ?");
		//StringBuffer hql = new StringBuffer("from FiBudgetLine as model where model.itemBrandCode = ?");
		//hql.append(" and model.itemBrandCode = ?");
		
		List<FiBudgetLine> result = getHibernateTemplate().find(hql.toString(),
				new Object[] {  category01  });
		log.info("Receive:"+result.size());
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	public FiBudgetLine findByRRItemFi(Long headId , String itemBrand, String category01
	) {
		log.info("FindReceive:"+headId+category01);
		StringBuffer hql = new StringBuffer("from FiBudgetLine as model where 1=1 ");
		hql.append(" and model.fiBudgetHead ="+headId);
		hql.append(" and model.itemBrandCode = ?");
		hql.append(" and model.categoryTypeCode1 = ?");
		//StringBuffer hql = new StringBuffer("from FiBudgetLine as model where model.itemBrandCode = ?");
		//hql.append(" and model.itemBrandCode = ?");
		
		List<FiBudgetLine> result = getHibernateTemplate().find(hql.toString(),
				new Object[] {  itemBrand,category01  });
		log.info("Receive:"+result.size());
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	public FiBudgetLine findByHeadId(Long headId 
	) {
		log.info("FindReceive:"+headId);
		StringBuffer hql = new StringBuffer("from FiBudgetLine as model where 1=1 ");
		hql.append(" and model.fiBudgetHead ="+headId);
		
		//StringBuffer hql = new StringBuffer("from FiBudgetLine as model where model.itemBrandCode = ?");
		//hql.append(" and model.itemBrandCode = ?");
		
		List<FiBudgetLine> result = getHibernateTemplate().find(hql.toString(),
				new Object[] {    });
		log.info("ReceiveheadId:"+result.size());
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	public List<FiBudgetLine> findByListline(Long headId
	) {
    	log.info("findByListline="+headId);
    	StringBuffer hql = new StringBuffer("from FiBudgetLine as model where model.headId = ? ");		
    	
    	List<FiBudgetLine> result = getHibernateTemplate().find(hql.toString(),
		new Object[] {headId });
    	log.info("find:findByListline"+result.size());
    	return result;
	}
}
