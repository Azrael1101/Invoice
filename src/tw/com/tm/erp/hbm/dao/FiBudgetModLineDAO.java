package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.FiBudgetModHead;
import tw.com.tm.erp.hbm.bean.FiBudgetModLine;
import tw.com.tm.erp.utils.AjaxUtils;

public class FiBudgetModLineDAO extends BaseDAO {

	// public List<FiBudgetLine> find(HashMap findObjs) {
	public List<FiBudgetModLine> find(final Long headId, final String budgeType,
			final String itemBrandCode, final String category01,
			final String category02) {

		List<FiBudgetModLine> re = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						String currBudgetType = AjaxUtils.getPropertiesValue(
								budgeType, "A"); // 扣預算方式
						String currItemBrand = AjaxUtils.getPropertiesValue(
								itemBrandCode, "ALL"); // 商品品牌
						String currCategory01 = AjaxUtils.getPropertiesValue(
								category01, "ALL"); // 大類
						String currCategory02 = AjaxUtils.getPropertiesValue(
								category02, "ALL"); // 中類

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

						StringBuffer hql = new StringBuffer(
								"from FiBudgetModLine as model where 1=1 ");
						hql.append(" and model.fiBudgetModHead =").append(headId);
						hql.append(" and nvl(model.itemBrandCode,'ALL') ='")
								.append(currItemBrand).append("'");
						hql
								.append(
										" and nvl(model.categoryTypeCode1,'ALL') ='")
								.append(currCategory01).append("'");
						hql
								.append(
										" and nvl(model.categoryTypeCode2,'ALL') ='")
								.append(currCategory02).append("'");
						hql.append(" order by lineId ");

						Query query = session.createQuery(hql.toString());
						query.setFirstResult(0);
						query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

						System.out.println(hql.toString());
						return query.list();
					}
				});
		return re;
	}

	public Map getSummaryTotal(Long headId) throws HibernateException,
			SQLException {

		System.out.println("FiBudgetModLineDAO.getSummaryTotal");
		Map resultMap = new HashMap();
		;
		// 統計：總預算, 簽核中金額, 採購已使用金額, 進貨已使用金額, 調整己使用金額
		StringBuffer hql = new StringBuffer(
				"select sum( nvl(model.budgetAmount,0) ), ");
		hql.append(" sum( nvl(model.signingAmount,0) ),");
		hql
				.append(" sum( nvl(model.poActualAmount,0) ), sum( nvl(model.receiveActualAmount,0)), sum(nvl(model.adjustActualAmount,0)) ");
		hql.append(" from FiBudgetModLine as model where 1=1 ");
		hql.append(" and model.fiBudgetModHead =").append(headId);
		System.out.println(hql.toString());
		List result = getHibernateTemplate().find(hql.toString(), null);
		if (result != null) {
			Object[] item = (Object[]) result.get(0);
			resultMap.put("budgetAmount", (Double) item[0]);
			resultMap.put("signingAmount", (Double) item[1]);
			resultMap.put("poActualAmount", (Double) item[2]);
			resultMap.put("receiveActualAmount", (Double) item[3]);
			resultMap.put("adjustActualAmount", (Double) item[4]);
		}
		return resultMap;
	}

	public FiBudgetModLine findById(Long headId) {
		return (FiBudgetModLine) findByPrimaryKey(FiBudgetModLine.class, headId);
	}

	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return List<CmDeclarationItem>
	 */
	public List<FiBudgetModLine> findPageLine(Long headId, int startPage,
			int pageSize) {
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<FiBudgetModLine> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer(
								"from FiBudgetModLine as model where 1=1 ");
						if (hId != null)
							hql
									.append(" and model.fiBudgetModHead.headId = :headId order by indexNo");
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
		List<FiBudgetModHead> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer(
								"from FiBudgetModHead as model where 1=1 ");
						if (hId != null)
							hql.append(" and model.headId = :headId");
						Query query = session.createQuery(hql.toString());
						if (hId != null)
							query.setLong("headId", hId);
						return query.list();
					}
				});
		if (result != null && result.size() > 0) {
			List<FiBudgetModLine> fiBudgetLines = result.get(0).getFiBudgetModLines();
			if (fiBudgetLines != null && fiBudgetLines.size() > 0) {
				lineMaxIndex = fiBudgetLines.get(fiBudgetLines.size() - 1)
						.getIndexNo();
			}
		}
		return lineMaxIndex;
	}

}
