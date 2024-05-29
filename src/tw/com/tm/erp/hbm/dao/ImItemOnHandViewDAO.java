package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
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
import tw.com.tm.erp.hbm.bean.ImItemOnHandView;
import tw.com.tm.erp.hbm.service.PoPurchaseOrderHeadService;
import tw.com.tm.erp.utils.DateUtils;

public class ImItemOnHandViewDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImItemOnHandViewDAO.class);
	public static final String QUARY_TYPE_SELECT_ALL = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
	public static final String QUARY_TYPE_SELECT_SHIFT = "shiftQuantity";

	public List<ImItemOnHandView> find(HashMap findObjs) {

		final HashMap fos = findObjs;

		List<ImItemOnHandView> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImItemOnHandView as model where 1=1 ");

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.id.brandCode = :brandCode ");

				if (StringUtils.hasText((String) fos.get("itemCName")))
					hql.append(" and model.itemCName like :itemCName ");

				if (StringUtils.hasText((String) fos.get("startItemCode"))) {
					if (StringUtils.hasText((String) fos.get("endItemCode"))) {
						hql.append(" and model.id.itemCode >= :startItemCode ");
						hql.append(" and model.id.itemCode <= :endItemCode ");
					} else {
						hql.append(" and model.id.itemCode like :startItemCode ");
					}
				} else if (StringUtils.hasText((String) fos.get("endItemCode"))) {
					hql.append(" and model.id.itemCode like :endItemCode ");
				}

				if (StringUtils.hasText((String) fos.get("startWarehouseCode"))) {
					if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
						hql.append(" and model.id.warehouseCode >= :startWarehouseCode ");
						hql.append(" and model.id.warehouseCode <= :endWarehouseCode ");
					} else {
						hql.append(" and model.id.warehouseCode like :startWarehouseCode ");
					}
				} else if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
					hql.append(" and model.id.warehouseCode like :endWarehouseCode ");
				}

				if (StringUtils.hasText((String) fos.get("startLotNo"))) {
					if (StringUtils.hasText((String) fos.get("endLotNo"))) {
						hql.append(" and model.id.lotNo >= :startLotNo ");
						hql.append(" and model.id.lotNo <= :endLotNo ");
					} else {
						hql.append(" and model.id.lotNo like :startLotNo ");
					}
				} else if (StringUtils.hasText((String) fos.get("endLotNo"))) {
					hql.append(" and model.id.lotNo like :endLotNo ");
				}

				if (!StringUtils.hasText((String) fos.get("showZero"))) {
					hql.append(" and model.currentOnHandQty != 0 ");
				}

				if (StringUtils.hasText((String) fos.get("categorySearchString")))
					hql.append(" and :categorySearchString");

				hql.append(" order by model.id.itemCode desc ");

				System.out.println((String) fos.get("categorySearchString"));
				System.out.println(hql.toString());
				Query query = session.createQuery(hql.toString());

				System.out.println(query.getQueryString());

				if (StringUtils.hasText((String) fos.get("brandCode")))
					query.setString("brandCode", (String) fos.get("brandCode"));

				if (StringUtils.hasText((String) fos.get("itemCName")))
					query.setString("itemCName", "%" + (String) fos.get("itemCName") + "%");

				// if( StringUtils.hasText((String)fos.get("warehouseManager"))
				// )
				// query.setString("warehouseManager",
				// (String)fos.get("warehouseManager") );

				if (StringUtils.hasText((String) fos.get("startItemCode"))) {
					if (StringUtils.hasText((String) fos.get("endItemCode"))) {
						query.setString("startItemCode", (String) fos.get("startItemCode"));
						query.setString("endItemCode", (String) fos.get("endItemCode"));
					} else {
						query.setString("startItemCode", "%" + (String) fos.get("startItemCode") + "%");
					}
				} else if (StringUtils.hasText((String) fos.get("endItemCode"))) {
					query.setString("endItemCode", "%" + (String) fos.get("endItemCode") + "%");
				}

				if (StringUtils.hasText((String) fos.get("startWarehouseCode"))) {
					if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
						query.setString("startWarehouseCode", (String) fos.get("startWarehouseCode"));
						query.setString("endWarehouseCode", (String) fos.get("endWarehouseCode"));
					} else {
						query.setString("startWarehouseCode", "%" + (String) fos.get("startWarehouseCode") + "%");
					}
				} else if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
					query.setString("endWarehouseCode", "%" + (String) fos.get("endWarehouseCode") + "%");
				}

				if (StringUtils.hasText((String) fos.get("startLotNo"))) {
					if (StringUtils.hasText((String) fos.get("endLotNo"))) {
						query.setString("startLotNo", (String) fos.get("startLotNo"));
						query.setString("endLotNo", (String) fos.get("endLotNo"));
					} else {
						query.setString("startLotNo", "%" + (String) fos.get("startLotNo") + "%");
					}
				} else if (StringUtils.hasText((String) fos.get("endLotNo"))) {
					query.setString("endLotNo", "%" + (String) fos.get("endLotNo") + "%");
				}

				if (StringUtils.hasText((String) fos.get("categorySearchString")))
					query.setString("categorySearchString", (String) fos.get("categorySearchString"));

				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
				return query.list();
			}
		});
		return re;

	}

	public List<ImItemOnHandView> findEosDetailItem(HashMap findObjs,int iSPage,int iPSize) {

		final String brandCode = (String)findObjs.get("brandCode");
		final String warehouseCode = (String)findObjs.get("warehouseCode");
		final String itemCategory = (String)findObjs.get("itemCategory");
		final String category01 = (String)findObjs.get("category01");
		final String category02 = (String)findObjs.get("category02");
		final String category03 = (String)findObjs.get("category03");
		final String isTax = (String)findObjs.get("isTax");
		final String itemBrand = (String)findObjs.get("itemBrand");
		final String srcWarehouse = (String)findObjs.get("srcWarehouse");
		
		final String filterA = (String)findObjs.get("filterA");
		final String filterB = (String)findObjs.get("filterB");
		final String filterC1 = (String)findObjs.get("filterC1");
		final String filterC2 = (String)findObjs.get("filterC2");
		final String filterD1 = (String)findObjs.get("filterD1");
		final String filterD2 = (String)findObjs.get("filterD2");
		final String sortCondition = (String)findObjs.get("sortCondition");
		final String sortType = (String)findObjs.get("sortType");
		
		final String itemCode = (String)findObjs.get("itemCode");
		
		final int startPage = iSPage;
		final int pageSize = iPSize;
		final int firstResult = pageSize * startPage;
		
		List<ImItemOnHandView> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				
				
				StringBuffer hql = new StringBuffer("from ImItemOnHandView as model where 1=1 ");
				hql.append(" and model.id.warehouseCode = '"+srcWarehouse+"' ");
				hql.append(" and model.id.brandCode = '"+brandCode+"' ");
				hql.append(" and model.id.lotNo = '000000000000' ");
				hql.append(" and model.itemCategory = '"+itemCategory+"' ");
				hql.append(" and model.category01 = '"+category01+"' ");
				if(!"".equals(category02)&&category02 != null&&!category02.equals("null"))
				{
					hql.append(" and model.category02 IN ("+category02+") ");
				}
				else
				{
					hql.append(" and model.category02 != 'A02' ");
				}
				if(!"".equals(category03)&&category03 != null&&!category03.equals("null"))
				{
					hql.append(" and model.category03 = '"+category03+"' ");
				}
				hql.append(" and model.isTax = '"+isTax+"' ");
				if(!"".equals(itemBrand)&&itemBrand != null&&!itemBrand.equals("null"))
				{
					hql.append(" and model.itemBrand IN ("+itemBrand+") ");
				}
				hql.append(" and model.currentOnHandQty > 0 " );
				hql.append(" and model.id.itemCode = SOME( ");
				hql.append(" select model2.id.itemCode ");
				hql.append(" from ImItemOnHandView as model2 ");
				hql.append(" where 1=1 ");
				hql.append(" and model2.id.brandCode = '"+brandCode+"' ");
				hql.append(" and model2.id.warehouseCode = '"+warehouseCode+"' ");
				hql.append(" and model2.itemCategory = '"+itemCategory+"' ");
				hql.append(" and model2.category01 = '"+category01+"' ");

				if(!"".equals(category02)&&category02 != null&&!category02.equals("null"))
				{
					hql.append(" and model2.category02 IN ("+category02+") ");
				}
				else
				{
					hql.append(" and model2.category02 = SOME( ");
					hql.append(" select  model3.id.categoryCode");
					hql.append(" from ImItemCategory as model3 ");
					hql.append(" where 1=1 ");
					hql.append(" and model3.id.brandCode = '"+brandCode+"' ");
					hql.append(" and model3.parentCategoryCode = '"+category01+"' ");
					hql.append(" and model3.id.categoryType = 'CATEGORY02' ");
					hql.append(" ) ");
					
					hql.append(" and model2.category02 != 'A02' ");
				}
				if(!"".equals(category03)&&category03 != null&&!category03.equals("null"))
				{
					hql.append(" and model2.category03 = '"+category03+"' ");
				}
				hql.append(" and model2.isTax = '"+isTax+"' ");
				if(!"".equals(itemBrand)&&itemBrand != null&&!itemBrand.equals("null"))
				{
					hql.append(" and model2.itemBrand IN ("+itemBrand+") ");
				}
				if(!"".equals(filterA)&&filterA != null&&!filterA.equals("null"))
				{
					hql.append(" and model2.category02 = '"+filterA+"' ");
				}
				if(!"".equals(filterC1)&&filterC1 != null&&!filterC1.equals("null"))
				{
					hql.append(" and model2.currentOnHandQty >= "+filterC1+" ");
				}
				if(!"".equals(filterC2)&&filterC2 != null&&!filterC2.equals("null"))
				{
					hql.append(" and model2.currentOnHandQty <= "+filterC2+" ");
				}
				hql.append(" ) ");
				if(!"".equals(filterB)&&filterB != null&&!filterB.equals("null"))
				{
					hql.append(" and model.itemBrand = '"+filterB+"' ");
				}
				if(!"".equals(filterD1)&&filterD1 != null&&!filterD1.equals("null"))
				{
					hql.append(" and model.unitPrice >= "+filterD1+" ");
				}
				if(!"".equals(filterD2)&&filterD2 != null&&!filterD2.equals("null"))
				{
					hql.append(" and model.unitPrice <= "+filterD2+" ");
				}
				if(!"".equals(itemCode)&&itemCode != null&&!itemCode.equals("null"))
				{
					hql.append(" and model.id.itemCode = '"+itemCode+"' ");
				}

				if(!"".equals(sortCondition)&&sortCondition != null&&!sortCondition.equals("null"))
				{
					hql.append(" order By ");
					hql.append(" "+sortCondition+" ");
					if(!"".equals(sortType))
					{
						hql.append(" "+sortType+" ");
					}
					else
					{
						hql.append(" asc");
					}

				}
				else
				{
					hql.append(" order By model.id.itemCode asc");
				}
				System.out.println(hql.toString());
				Query query = session.createQuery(hql.toString());
				query.setFirstResult( firstResult );
  				query.setMaxResults( pageSize );
				return query.list();
			}
		});

		return re;

	}
	public Long findEosDetailItemCount(HashMap findObjs,int iSPage,int iPSize) {

		final String brandCode = (String)findObjs.get("brandCode");
		final String warehouseCode = (String)findObjs.get("warehouseCode");
		final String itemCategory = (String)findObjs.get("itemCategory");
		final String category01 = (String)findObjs.get("category01");
		final String category02 = (String)findObjs.get("category02");
		final String category03 = (String)findObjs.get("category03");
		final String isTax = (String)findObjs.get("isTax");
		final String itemBrand = (String)findObjs.get("itemBrand");
		final String srcWarehouse = (String)findObjs.get("srcWarehouse");
		final int startPage = iSPage;
		final int pageSize = iPSize;
		
		
		final String filterA = (String)findObjs.get("filterA");
		final String filterB = (String)findObjs.get("filterB");
		final String filterC1 = (String)findObjs.get("filterC1");
		final String filterC2 = (String)findObjs.get("filterC2");
		final String filterD1 = (String)findObjs.get("filterD1");
		final String filterD2 = (String)findObjs.get("filterD2");
		final String sortCondition = (String)findObjs.get("sortCondition");
		final String sortType = (String)findObjs.get("sortType");
		
		final String itemCode = (String)findObjs.get("itemCode");
		
		List<ImItemOnHandView> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				
				
				StringBuffer hql = new StringBuffer("from ImItemOnHandView as model where 1=1 ");
				hql.append(" and model.id.warehouseCode = '"+srcWarehouse+"' ");
				hql.append(" and model.id.brandCode = '"+brandCode+"' ");
				hql.append(" and model.id.lotNo = '000000000000' ");
				hql.append(" and model.itemCategory = '"+itemCategory+"' ");
				hql.append(" and model.category01 = '"+category01+"' ");
				if(!"".equals(category02)&&category02 != null&&!category02.equals("null"))
				{
					hql.append(" and model.category02 IN ("+category02+") ");
				}
				else
				{
					hql.append(" and model.category02 != 'A02' ");
				}
				if(!"".equals(category03)&&category03 != null&&!category03.equals("null"))
				{
					hql.append(" and model.category03 = '"+category03+"' ");
				}
				hql.append(" and model.isTax = '"+isTax+"' ");
				if(!"".equals(itemBrand)&&itemBrand != null&&!itemBrand.equals("null"))
				{
					hql.append(" and model.itemBrand IN ("+itemBrand+") ");
				}
				hql.append(" and model.currentOnHandQty > 0 " );
				hql.append(" and model.id.itemCode = SOME( ");
				hql.append(" select model2.id.itemCode ");
				hql.append(" from ImItemOnHandView as model2 ");
				hql.append(" where 1=1 ");
				hql.append(" and model2.id.brandCode = '"+brandCode+"' ");
				hql.append(" and model2.id.warehouseCode = '"+warehouseCode+"' ");
				hql.append(" and model2.itemCategory = '"+itemCategory+"' ");
				hql.append(" and model2.category01 = '"+category01+"' ");
				if(!"".equals(itemCode)&&itemCode != null&&!itemCode.equals("null"))
				{
					hql.append(" and model2.id.itemCode = '"+itemCode+"' ");
				}
				if(!"".equals(category02)&&category02 != null&&!category02.equals("null"))
				{
					hql.append(" and model2.category02 IN ("+category02+") ");
				}
				else
				{
					hql.append(" and model2.category02 = SOME( ");
					hql.append(" select  model3.id.categoryCode");
					hql.append(" from ImItemCategory as model3 ");
					hql.append(" where 1=1 ");
					hql.append(" and model3.id.brandCode = '"+brandCode+"' ");
					hql.append(" and model3.parentCategoryCode = '"+category01+"' ");
					hql.append(" and model3.id.categoryType = 'CATEGORY02' ");
					hql.append(" ) ");
					hql.append(" and model2.category02 != 'A02' ");
				}
				if(!"".equals(category03)&&category03 != null&&!category03.equals("null"))
				{
					hql.append(" and model2.category03 = '"+category03+"' ");
				}
				hql.append(" and model2.isTax = '"+isTax+"' ");
				if(!"".equals(itemBrand)&&itemBrand != null&&!itemBrand.equals("null"))
				{
					hql.append(" and model2.itemBrand IN ("+itemBrand+") ");
				}
				if(!"".equals(filterA)&&filterA != null&&!filterA.equals("null"))
				{
					hql.append(" and model2.category02 = '"+filterA+"' ");
				}
				if(!"".equals(filterC1)&&filterC1 != null&&!filterC1.equals("null"))
				{
					hql.append(" and model2.currentOnHandQty >= "+filterC1+" ");
				}
				if(!"".equals(filterC2)&&filterC2 != null&&!filterC2.equals("null"))
				{
					hql.append(" and model2.currentOnHandQty <= "+filterC2+" ");
				}
				hql.append(" ) ");
				if(!"".equals(filterB)&&filterB != null&&!filterB.equals("null"))
				{
					hql.append(" and model.itemBrand = '"+filterB+"' ");
				}
				if(!"".equals(filterD1)&&filterD1 != null&&!filterD1.equals("null"))
				{
					hql.append(" and model.unitPrice >= "+filterD1+" ");
				}
				if(!"".equals(filterD2)&&filterD2 != null&&!filterD2.equals("null"))
				{
					hql.append(" and model.unitPrice <= "+filterD2+" ");
				}

				if(!"".equals(itemCode)&&itemCode != null&&!itemCode.equals("null"))
				{
					hql.append(" and model.id.itemCode = '"+itemCode+"' ");
				}

				System.out.println(hql.toString());
				Query query = session.createQuery(hql.toString());
				System.out.println("查詢完畢");
				return query.list();
			}
		});
		System.out.println("LIST PRODUCT");
		return re.size()+0L;

	}
	
	
	public List<ImItemOnHandView> findByWarehouseEmployee(HashMap findObjs) {

		final HashMap fos = findObjs;

		List<ImItemOnHandView> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImItemOnHandView as model where 1=1 ");

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.id.brandCode = :brandCode ");

				if (StringUtils.hasText((String) fos.get("itemCName")))
					hql.append(" and model.itemCName like :itemCName ");

				if (StringUtils.hasText((String) fos.get("warehouseManager")))
					hql.append(" and model.warehouseManager = :warehouseManager ");

				if (StringUtils.hasText((String) fos.get("startItemCode"))) {
					if (StringUtils.hasText((String) fos.get("endItemCode"))) {
						hql.append(" and model.id.itemCode >= :startItemCode ");
						hql.append(" and model.id.itemCode <= :endItemCode ");
					} else {
						hql.append(" and model.id.itemCode like :startItemCode ");
					}
				} else if (StringUtils.hasText((String) fos.get("endItemCode"))) {
					hql.append(" and model.id.itemCode like :endItemCode ");
				}

				hql.append(" and model.id.warehouseCode = some");
				hql.append("(select warehouse.id.warehouseCode from ImWarehouse as warehouse, ImWarehouseEmployee as employee where 1 = 1 ");
				hql.append("and warehouse.id.warehouseCode = employee.id.warehouseCode ");
				if (StringUtils.hasText((String) fos.get("startWarehouseCode"))) {
					if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
						hql.append(" and warehouse.id.warehouseCode >= :startWarehouseCode ");
						hql.append(" and warehouse.id.warehouseCode <= :endWarehouseCode ");
					} else {
						hql.append(" and warehouse.id.warehouseCode like :startWarehouseCode ");
					}
				} else if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
					hql.append(" and warehouse.id.warehouseCode like :endWarehouseCode ");
				}
				hql.append("and employee.id.employeeCode = :warehouseEmployee ) ");

				if (StringUtils.hasText((String) fos.get("startLotNo"))) {
					if (StringUtils.hasText((String) fos.get("endLotNo"))) {
						hql.append(" and model.id.lotNo >= :startLotNo ");
						hql.append(" and model.id.lotNo <= :endLotNo ");
					} else {
						hql.append(" and model.id.lotNo like :startLotNo ");
					}
				} else if (StringUtils.hasText((String) fos.get("endLotNo"))) {
					hql.append(" and model.id.lotNo like :endLotNo ");
				}
				if (StringUtils.hasText((String) fos.get("categorySearchString")))
					hql.append(":categorySearchString");

				// hql.append(" order by warehouseCode desc ");
				System.out.println(hql.toString());
				Query query = session.createQuery(hql.toString());

				if (StringUtils.hasText((String) fos.get("brandCode")))
					query.setString("brandCode", (String) fos.get("brandCode"));

				if (StringUtils.hasText((String) fos.get("itemCName")))
					query.setString("itemCName", "%" + (String) fos.get("itemCName") + "%");

				if (StringUtils.hasText((String) fos.get("warehouseManager")))
					query.setString("warehouseManager", (String) fos.get("warehouseManager"));

				if (StringUtils.hasText((String) fos.get("startItemCode"))) {
					if (StringUtils.hasText((String) fos.get("endItemCode"))) {
						query.setString("startItemCode", (String) fos.get("startItemCode"));
						query.setString("endItemCode", (String) fos.get("endItemCode"));
					} else {
						query.setString("startItemCode", "%" + (String) fos.get("startItemCode") + "%");
					}
				} else if (StringUtils.hasText((String) fos.get("endItemCode"))) {
					query.setString("endItemCode", "%" + (String) fos.get("endItemCode") + "%");
				}

				if (StringUtils.hasText((String) fos.get("startWarehouseCode"))) {
					if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
						query.setString("startWarehouseCode", (String) fos.get("startWarehouseCode"));
						query.setString("endWarehouseCode", (String) fos.get("endWarehouseCode"));
					} else {
						query.setString("startWarehouseCode", "%" + (String) fos.get("startWarehouseCode") + "%");
					}
				} else if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
					query.setString("endWarehouseCode", "%" + (String) fos.get("endWarehouseCode") + "%");
				}

				if (StringUtils.hasText((String) fos.get("startLotNo"))) {
					if (StringUtils.hasText((String) fos.get("endLotNo"))) {
						query.setString("startLotNo", (String) fos.get("startLotNo"));
						query.setString("endLotNo", (String) fos.get("endLotNo"));
					} else {
						query.setString("startLotNo", "%" + (String) fos.get("startLotNo") + "%");
					}
				} else if (StringUtils.hasText((String) fos.get("endLotNo"))) {
					query.setString("endLotNo", "%" + (String) fos.get("endLotNo") + "%");
				}

				if (StringUtils.hasText((String) fos.get("warehouseEmployee")))
					query.setString("warehouseEmployee", (String) fos.get("warehouseEmployee"));

				if (StringUtils.hasText((String) fos.get("categorySearchString")))
					query.setString("categorySearchString", (String) fos.get("categorySearchString"));

				return query.list();
			}
		});

		return re;

	}

	/**
	 * 依據品牌、更新日期、啟用狀態查詢出商品資訊
	 *
	 * @param brandCode
	 * @param dataDate
	 * @param dataDateEnd
	 * @param enable
	 * @return List
	 */
	public List getShopItemOnHandByCondition(final String brandCode, final String dataDate, final String dataDateEnd,
			final String enable, final String unSearchShopCode) {

		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select h.item_code, h.old_warehouse_code, h.current_qty from");
				hql.append(" (select s.brand_code, s.shop_code, s.sales_warehouse_code, sim.old_warehouse_code, on_hand.item_code, "
								+ "on_hand.current_qty from bu_shop s, sim_warehouse sim, im_warehouse w,");
				hql.append(" (select h.brand_code, h.item_code, h.warehouse_code, sum(h.stock_on_hand_qty - h.out_uncommit_qty + h.in_uncommit_qty "
								+ "+ h.move_uncommit_qty + h.other_uncommit_qty) as current_qty from im_on_hand h where h.brand_code = :brandCode");
				
				hql.append(" group by (h.organization_code, h.brand_code, h.item_code, h.warehouse_code)) on_hand");
				hql.append(" where s.brand_code = w.brand_code and w.brand_code = on_hand.brand_code and s.sales_warehouse_code = sim.warehouse_code and s.sales_warehouse_code = w.warehouse_code and w.warehouse_code = on_hand.warehouse_code");
				hql.append(" and s.brand_code = :brandCode and w.brand_code = :brandCode and s.enable = :enable and w.enable = :enable");
				if (StringUtils.hasText(unSearchShopCode)) {
					hql.append(" and s.sales_warehouse_code not in (" + unSearchShopCode + ")");
				}
				hql.append(") h");
				hql.append(" group by h.old_warehouse_code, h.item_code, h.current_qty");
				hql.append(" order by h.item_code, h.old_warehouse_code");

				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("enable", enable);

				return query.list();
			}
		});

		return result;
	}

	public List getOnHandQtyByItemCodes(String itemCodes) {
		try {
			StringBuffer hql = new StringBuffer();
			hql.append("select im.id.itemCode,sum( im.currentOnHandQty )");
			hql.append("FROM ImItemOnHandView im ");
			hql.append("where im.id.itemCode in (").append(itemCodes).append(") GROUP by im.id.itemCode");
			List result = getHibernateTemplate().find(hql.toString(), null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Double getOnHandQtyByItemCode(String organizationCode, String itemCode, String warehouseCode, String lotNo)
			throws Exception {
		try {
			StringBuffer hql = new StringBuffer();
			hql.append("select sum( im.currentOnHandQty )");
			hql.append("FROM ImItemOnHandView im where 1=1 ");
			
			StringBuffer groupBy = new StringBuffer();
			groupBy.append(" GROUP by ");
			if (StringUtils.hasText(organizationCode)) {
				hql.append("and im.organizationCode = '").append(organizationCode).append("' ");
				groupBy.append("im.organizationCode ,");
			}
			if (StringUtils.hasText(warehouseCode)) {
				hql.append("and im.id.warehouseCode = '").append(warehouseCode).append("' ");
				groupBy.append("im.id.warehouseCode ,");
			}
			if (StringUtils.hasText(itemCode)) {
				hql.append("and im.id.itemCode = '").append(itemCode).append("' ");
				groupBy.append("im.id.itemCode ,");
			}

			if (StringUtils.hasText(lotNo)) {
				hql.append("and im.id.lotNo = '").append(lotNo).append("' ");
				groupBy.append("im.id.lotNo ,");
			}
			if (groupBy.toString().indexOf(" ,") > 0) {
				groupBy.deleteCharAt(groupBy.lastIndexOf(","));
				hql.append(groupBy.toString());
			}
			
			List result = getHibernateTemplate().find(hql.toString(), null);
			Double qty = null;
			if (result != null && result.size() > 0) {
				qty = (Double) result.get(0);
			}
			return qty;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ImItemOnHandViewDAO getOnHandQtyByItemCode error:", e);
			throw new Exception(e.getMessage());
		}
	}
	
	public Double getOnHandQtyByItemCode(String brandCode, String itemCode, String warehouseCode)throws Exception {
		try {
		StringBuffer hql = new StringBuffer();
		hql.append("select sum( im.currentOnHandQty )");
		hql.append("FROM ImItemOnHandView im where 1=1 ");
		StringBuffer groupBy = new StringBuffer();
		groupBy.append(" GROUP by ");
		if (StringUtils.hasText(brandCode)) {
			hql.append("and im.id.brandCode = '").append(brandCode).append("' ");
			groupBy.append("im.id.brandCode ,");
		}
		if (StringUtils.hasText(warehouseCode)) {
			hql.append("and im.id.warehouseCode = '").append(warehouseCode).append("' ");
			groupBy.append("im.id.warehouseCode ,");
		}
		if (StringUtils.hasText(itemCode)) {
			hql.append("and im.id.itemCode = '").append(itemCode).append("' ");
			groupBy.append("im.id.itemCode ,");
		}
		if (groupBy.toString().indexOf(" ,") > 0) {
			groupBy.deleteCharAt(groupBy.lastIndexOf(","));
			hql.append(groupBy.toString());
		}
		
		List result = getHibernateTemplate().find(hql.toString(), null);
		Double qty = null;
		if (result != null && result.size() > 0) {
			qty = (Double) result.get(0);
		}
		return qty;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ImItemOnHandViewDAO getOnHandQtyByItemCode error:", e);
			throw new Exception(e.getMessage());
		}
	}

	public List findPageLine(String itemCode, int startPage, int pageSize) {
		final String itemCodeName = "itemCode";
		final String itemCodeValue = itemCode;
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImItemOnHandView as model where 1=1 ");
				if (itemCodeValue != null)
					hql.append(" and model.id.itemCode = :" + itemCodeName);
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if (itemCodeValue != null)
					query.setString(itemCodeName, itemCodeValue);
				return query.list();
			}
		});
		return result;
	}

	/**
	 *
	 * @param findObjs
	 * @param startPage
	 * @param pageSize
	 * @param searchType
	 *            1) get max record count 2) select data records according to
	 *            startPage and pageSize 3) select all records
	 * @return
	 */
	public HashMap findPageLine(HashMap findObjs, int startPage, int pageSize, String searchType) {
		final HashMap fos = findObjs;
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String type = searchType;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("");
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.id.brandCode) as rowCount from ImItemOnHandView as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
					hql.append("select model.id.brandCode, model.id.itemCode, model.id.warehouseCode, model.id.lotNo from ImItemOnHandView as model where 1=1 ");
				} else {
					hql.append("from ImItemOnHandView as model where 1=1 ");
				}

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.id.brandCode = :brandCode ");

				if (StringUtils.hasText((String) fos.get("itemCName")))
					hql.append(" and model.itemCName like :itemCName ");

				if (StringUtils.hasText((String) fos.get("itemCodeList"))) { // 如果輸入多品號，則不用範圍查詢
					hql.append(" and model.id.itemCode in (" + fos.get("itemCodeList") + ")");
				} else {
					if (StringUtils.hasText((String) fos.get("startItemCode"))) {
						if (StringUtils.hasText((String) fos.get("endItemCode"))) {
							hql.append(" and model.id.itemCode >= :startItemCode ");
							hql.append(" and model.id.itemCode <= :endItemCode ");
						} else {
							hql.append(" and model.id.itemCode like :startItemCode ");
						}
					} else if (StringUtils.hasText((String) fos.get("endItemCode"))) {
						hql.append(" and model.id.itemCode like :endItemCode ");
					}
				}

				if (StringUtils.hasText((String) fos.get("startWarehouseCode"))) {
					if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
						hql.append(" and model.id.warehouseCode >= :startWarehouseCode ");
						hql.append(" and model.id.warehouseCode <= :endWarehouseCode ");
					} else {
						hql.append(" and model.id.warehouseCode like :startWarehouseCode ");
					}
				} else if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
					hql.append(" and model.id.warehouseCode like :endWarehouseCode ");
				} else if ((List<String>) fos.get("warehouseCodeList") != null){
					hql.append(" and model.id.warehouseCode in ( :warehouseCodeList ) ");
				}

				if (StringUtils.hasText((String) fos.get("startLotNo"))) {
					if (StringUtils.hasText((String) fos.get("endLotNo"))) {
						hql.append(" and model.id.lotNo >= :startLotNo ");
						hql.append(" and model.id.lotNo <= :endLotNo ");
					} else {
						hql.append(" and model.id.lotNo like :startLotNo ");
					}
				} else if (StringUtils.hasText((String) fos.get("endLotNo"))) {
					hql.append(" and model.id.lotNo like :endLotNo ");
				}

				if (StringUtils.hasText((String) fos.get("itemCategory")))
					hql.append(" and model.itemCategory = :itemCategory ");
				if (StringUtils.hasText((String) fos.get("taxType")))
					hql.append(" and model.isTax = :taxType ");
				if (StringUtils.hasText((String) fos.get("itemBrand")))
					hql.append(" and model.itemBrand = :itemBrand ");
				if (StringUtils.hasText((String) fos.get("category01")))
					hql.append(" and model.category01 = :category01 ");
				if (StringUtils.hasText((String) fos.get("category02")))
					hql.append(" and model.category02 = :category02 ");
				if (StringUtils.hasText((String) fos.get("category03")))
					hql.append(" and model.category03 = :category03 ");
				if (StringUtils.hasText((String) fos.get("category04")))
					hql.append(" and model.category04 = :category04 ");
				if (StringUtils.hasText((String) fos.get("category07")))
					hql.append(" and model.category07 = :category07 ");
				if (StringUtils.hasText((String) fos.get("category09")))
					hql.append(" and model.category09 = :category09 ");
				if (StringUtils.hasText((String) fos.get("category13")))
					hql.append(" and model.category13 = :category13 ");
				if (StringUtils.hasText((String) fos.get("category17"))) // 加入廠商代號
					hql.append(" and model.category17 = :category17 ");
				
				// 加入負庫存查詢條件
				if ("N".equalsIgnoreCase((String) fos.get("showZero"))
						&& "N".equalsIgnoreCase((String) fos.get("showMinus"))){ // 只顯示大於零的庫存
					hql.append(" and model.currentOnHandQty <> 0 ");
				}else if("N".equalsIgnoreCase((String) fos.get("showZero"))
						&& !"N".equalsIgnoreCase((String) fos.get("showMinus"))){ // 只顯示負庫存
					hql.append(" and model.currentOnHandQty < 0 ");
				}else if(!"N".equalsIgnoreCase((String) fos.get("showZero"))
						&& "N".equalsIgnoreCase((String) fos.get("showMinus"))){ // 不顯示負庫存
					hql.append(" and model.currentOnHandQty >= 0 ");
				}
				hql.append(" order by model.id.itemCode desc ");
				
				System.out.println("hql = " + hql.toString());

				Query query = session.createQuery(hql.toString());

				if (QUARY_TYPE_SELECT_RANGE.equals(type)) {
					query.setFirstResult(startRecordIndexStar);
					query.setMaxResults(pSize);
				}

				if (StringUtils.hasText((String) fos.get("brandCode")))
					query.setString("brandCode", (String) fos.get("brandCode"));

				if (StringUtils.hasText((String) fos.get("itemCName")))
					query.setString("itemCName", "%" + (String) fos.get("itemCName") + "%");

				if (StringUtils.hasText((String) fos.get("itemCodeList"))) { // 如果輸入多品號，則不用範圍查詢
					//query.setString("itemCodeList", (String) fos.get("itemCodeList"));
				} else {
					if (StringUtils.hasText((String) fos.get("startItemCode"))) {
						if (StringUtils.hasText((String) fos.get("endItemCode"))) {
							query.setString("startItemCode", (String) fos.get("startItemCode"));
							query.setString("endItemCode", (String) fos.get("endItemCode"));
						} else {
							query.setString("startItemCode", "%" + (String) fos.get("startItemCode") + "%");
						}
					} else if (StringUtils.hasText((String) fos.get("endItemCode"))) {
						query.setString("endItemCode", "%" + (String) fos.get("endItemCode") + "%");
					}
				}

				if (StringUtils.hasText((String) fos.get("startWarehouseCode"))) {
					if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
						query.setString("startWarehouseCode", (String) fos.get("startWarehouseCode"));
						query.setString("endWarehouseCode", (String) fos.get("endWarehouseCode"));
					} else {
						query.setString("startWarehouseCode", "%" + (String) fos.get("startWarehouseCode") + "%");
					}
				} else if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
					query.setString("endWarehouseCode", "%" + (String) fos.get("endWarehouseCode") + "%");
				} else if ((List<String>) fos.get("warehouseCodeList") != null){
					query.setParameterList("warehouseCodeList", (List<String>) fos.get("warehouseCodeList"));
				}

				if (StringUtils.hasText((String) fos.get("startLotNo"))) {
					if (StringUtils.hasText((String) fos.get("endLotNo"))) {
						query.setString("startLotNo", (String) fos.get("startLotNo"));
						query.setString("endLotNo", (String) fos.get("endLotNo"));
					} else {
						query.setString("startLotNo", "%" + (String) fos.get("startLotNo") + "%");
					}
				} else if (StringUtils.hasText((String) fos.get("endLotNo"))) {
					query.setString("endLotNo", "%" + (String) fos.get("endLotNo") + "%");
				}

				if (StringUtils.hasText((String) fos.get("itemCategory")))
					query.setString("itemCategory", (String) fos.get("itemCategory"));
				if (StringUtils.hasText((String) fos.get("itemBrand")))
					query.setString("itemBrand", (String) fos.get("itemBrand"));
				if (StringUtils.hasText((String) fos.get("category01")))
					query.setString("category01", (String) fos.get("category01"));
				if (StringUtils.hasText((String) fos.get("category02")))
					query.setString("category02", (String) fos.get("category02"));
				if (StringUtils.hasText((String) fos.get("category03")))
					query.setString("category03", (String) fos.get("category03"));
				if (StringUtils.hasText((String) fos.get("category04")))
					query.setString("category04", (String) fos.get("category04"));
				if (StringUtils.hasText((String) fos.get("category07")))
					query.setString("category07", (String) fos.get("category07"));
				if (StringUtils.hasText((String) fos.get("category09")))
					query.setString("category09", (String) fos.get("category09"));
				if (StringUtils.hasText((String) fos.get("category13")))
					query.setString("category13", (String) fos.get("category13"));
				if (StringUtils.hasText((String) fos.get("taxType")))
					query.setString("taxType", (String) fos.get("taxType"));
				if (StringUtils.hasText((String) fos.get("category17"))) // 加入廠商代號的查詢條件
					query.setString("category17", (String) fos.get("category17"));

				return query.list();
			}
		});

		HashMap returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long.valueOf(result.get(0).toString()));
		}
		return returnResult;
	}
	
	public HashMap findPageLineNew(HashMap findObjs, int startPage, int pageSize, String searchType) {
		final HashMap fos = findObjs;
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String type = searchType;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("");
				
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.id.brandCode) as rowCount from ImItemOnHandTmp as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
					hql.append("select model.id.brandCode, model.id.itemCode, model.id.warehouseCode, model.id.lotNo from ImItemOnHandTmp as model where 1=1 ");
				} else {
					hql.append("from ImItemOnHandTmp as model where 1=1 ");
				}

				hql.append(" and model.id.onHandTimeScope = :onHandTimeScope ");
				hql.append(" order by model.id.itemCode desc ");
				
				log.info("hql = " + hql.toString());
				
				Query query = session.createQuery(hql.toString());
				if (QUARY_TYPE_SELECT_RANGE.equals(type)) {
					query.setFirstResult(startRecordIndexStar);
					query.setMaxResults(pSize);
				}

				query.setString("onHandTimeScope", (String) fos.get("onHandTimeScope"));
				
				return query.list();
			}
		});

		HashMap returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long.valueOf(result.get(0).toString()));
		}
		return returnResult;
	}
	
	public void insertImItemOnHandView(HashMap findObjs) {
		final HashMap fos = findObjs;
		log.info("---------------------insertImItemOnHandView---------------------");
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String onHandTimeScope = (String) fos.get("onHandTimeScope");
				StringBuffer hql = new StringBuffer(""); 
				hql.append("insert into ERP.TMP_IM_ON_HAND");
				hql.append("(");
				hql.append("ITEM_CODE, BRAND_CODE, WAREHOUSE_CODE, LOT_NO, ON_HAND_TIME_SCOPE, ");
				hql.append("ORGANIZATION_CODE, ITEM_C_NAME, WAREHOUSE_NAME, UNIT_PRICE, STOCK_ON_HAND_QTY, OUT_UNCOMMIT_QTY, ");
				hql.append("IN_UNCOMMIT_QTY, MOVE_UNCOMMIT_QTY, OTHER_UNCOMMIT_QTY, CURRENT_ON_HAND_QTY"); 
				hql.append(") "); 
				hql.append("select ");
				hql.append("ITEM_CODE, BRAND_CODE, WAREHOUSE_CODE, LOT_NO, '"+onHandTimeScope+"' AS ON_HAND_TIME_SCOPE, ");
				hql.append("ORGANIZATION_CODE, ITEM_C_NAME, WAREHOUSE_NAME, UNIT_PRICE, STOCK_ON_HAND_QTY, OUT_UNCOMMIT_QTY, ");
				hql.append("IN_UNCOMMIT_QTY, MOVE_UNCOMMIT_QTY, OTHER_UNCOMMIT_QTY, CURRENT_ON_HAND_QTY "); 
				hql.append("from ERP.IM_ITEM_ON_HAND_SIMPLIFY_VIEW model ");
				hql.append("where 1=1  ");
				
				//BRAND_CODE篩選條件處理
				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.BRAND_CODE = :brandCode ");
				
				//ITEM_C_NAME篩選條件處理
				if (StringUtils.hasText((String) fos.get("itemCName")))
					hql.append(" and model.ITEM_C_NAME like :itemCName ");
				
				//ITEM_CODE篩選條件處理
				if (StringUtils.hasText((String) fos.get("itemCodeList"))) { // 如果輸入多品號，則不用範圍查詢
					hql.append(" and model.ITEM_CODE in (" + fos.get("itemCodeList") + ")");
				} else {
					if (StringUtils.hasText((String) fos.get("startItemCode"))) {
						if (StringUtils.hasText((String) fos.get("endItemCode"))) {
							hql.append(" and model.ITEM_CODE >= :startItemCode ");
							hql.append(" and model.ITEM_CODE <= :endItemCode ");
						} else {
							hql.append(" and model.ITEM_CODE like :startItemCode ");
						}
					} else if (StringUtils.hasText((String) fos.get("endItemCode"))) {
						hql.append(" and model.ITEM_CODE like :endItemCode ");
					}
				}
				//ITEM_BRAND篩選條件處理
				if (StringUtils.hasText((String) fos.get("itemBrand")))
					hql.append(" and model.ITEM_BRAND = :itemBrand ");
				
				//ITEM_CATEGORY篩選條件處理
				if (StringUtils.hasText((String) fos.get("itemCategory")))
					hql.append(" and model.ITEM_CATEGORY = :itemCategory ");
				
				//CATEGORY01篩選條件處理
				if (StringUtils.hasText((String) fos.get("category01")))
					hql.append(" and model.CATEGORY01 = :category01 ");
				
				//CATEGORY02篩選條件處理
				if (StringUtils.hasText((String) fos.get("category02")))
					hql.append(" and model.CATEGORY02 = :category02 ");
				
				//CATEGORY03篩選條件處理
				if (StringUtils.hasText((String) fos.get("category03")))
					hql.append(" and model.CATEGORY03 = :category03 ");
				
				//WAREHOUSE_CODE篩選條件處理
				if (!StringUtils.hasText((String) fos.get("startWarehouseCode")) && 
						StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
					hql.append(" and model.WAREHOUSE_CODE like :endWarehouseCode ");
				} else{
					hql.append(" and model.WAREHOUSE_CODE IN (" + fos.get("warehouseRange")+")");
				}
				
				//LOT_NO篩選條件處理
				if (StringUtils.hasText((String) fos.get("startLotNo"))) {
					if (StringUtils.hasText((String) fos.get("endLotNo"))) {
						hql.append(" and model.LOT_NO >= :startLotNo ");
						hql.append(" and model.LOT_NO <= :endLotNo ");
					} else {
						hql.append(" and model.LOT_NO like :startLotNo ");
					}
				} else if (StringUtils.hasText((String) fos.get("endLotNo"))) {
					hql.append(" and model.LOT_NO like :endLotNo ");
				}
				
				// 加入負庫存查詢條件
				if ("N".equalsIgnoreCase((String) fos.get("showZero"))
						&& "N".equalsIgnoreCase((String) fos.get("showMinus"))){ // 只顯示大於零的庫存
					hql.append(" and model.CURRENT_ON_HAND_QTY > 0 ");
				}else if("N".equalsIgnoreCase((String) fos.get("showZero"))
						&& !"N".equalsIgnoreCase((String) fos.get("showMinus"))){ // 只顯示負庫存
					hql.append(" and model.CURRENT_ON_HAND_QTY < 0 ");
				}else if(!"N".equalsIgnoreCase((String) fos.get("showZero"))
						&& "N".equalsIgnoreCase((String) fos.get("showMinus"))){ // 不顯示負庫存
					hql.append(" and model.CURRENT_ON_HAND_QTY >= 0 ");
				}
				
				hql.append(" order by model.ITEM_CODE desc ");
				
				System.out.println("hql = " + hql.toString());

				Query query = session.createSQLQuery(hql.toString());
				
				if (StringUtils.hasText((String) fos.get("brandCode")))
					query.setString("brandCode", (String) fos.get("brandCode"));

				if (StringUtils.hasText((String) fos.get("itemCName")))
					query.setString("itemCName", "%" + (String) fos.get("itemCName") + "%");
				
				if (StringUtils.hasText((String) fos.get("itemBrand")))
					query.setString("itemBrand", (String) fos.get("itemBrand"));
				
				if (StringUtils.hasText((String) fos.get("itemCategory")))
					query.setString("itemCategory", (String) fos.get("itemCategory"));
				
				if (StringUtils.hasText((String) fos.get("category01")))
					query.setString("category01", (String) fos.get("category01"));
				
				if (StringUtils.hasText((String) fos.get("category02")))
					query.setString("category02", (String) fos.get("category02"));
				
				if (StringUtils.hasText((String) fos.get("category03")))
					query.setString("category03", (String) fos.get("category03"));

				if (StringUtils.hasText((String) fos.get("itemCodeList"))) { // 如果輸入多品號，則不用範圍查詢
					//query.setString("itemCodeList", (String) fos.get("itemCodeList"));
				} else {
					if (StringUtils.hasText((String) fos.get("startItemCode"))) {
						if (StringUtils.hasText((String) fos.get("endItemCode"))) {
							query.setString("startItemCode", (String) fos.get("startItemCode"));
							query.setString("endItemCode", (String) fos.get("endItemCode"));
						} else {
							query.setString("startItemCode", "%" + (String) fos.get("startItemCode") + "%");
						}
					} else if (StringUtils.hasText((String) fos.get("endItemCode"))) {
						query.setString("endItemCode", "%" + (String) fos.get("endItemCode") + "%");
					}
				}

				if (StringUtils.hasText((String) fos.get("startLotNo"))) {
					if (StringUtils.hasText((String) fos.get("endLotNo"))) {
						query.setString("startLotNo", (String) fos.get("startLotNo"));
						query.setString("endLotNo", (String) fos.get("endLotNo"));
					} else {
						query.setString("startLotNo", "%" + (String) fos.get("startLotNo") + "%");
					}
				} else if (StringUtils.hasText((String) fos.get("endLotNo"))) {
					query.setString("endLotNo", "%" + (String) fos.get("endLotNo") + "%");
				}
				
				return query.executeUpdate();
			}
		});
		log.info("---------------------塞入暫存資料表完成---------------------");
	}

	public List getOnHandQtyByWareHouseCodes(String brandCode, String warehouseCode) {
		try {
			StringBuffer hql = new StringBuffer();
			hql.append("select im.id.itemCode,sum( im.currentOnHandQty )");
			hql.append("FROM ImItemOnHandView im ");
			hql.append("where im.id.brandCode = '" + brandCode + "' and im.id.warehouseCode = '" + warehouseCode
					+ "' and im.currentOnHandQty > 0 ");
			hql.append("GROUP by im.id.itemCode, im.id.lotNo");
			List result = getHibernateTemplate().find(hql.toString(), null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public List getOnHandQtyByWareHouseCodes1(String brandCode, String warehouseCode, String itemCode) {
		try {
			StringBuffer hql = new StringBuffer();
			hql.append("select im.id.itemCode,im.id.warehouseCode,im.category01,im.category02,im.category03,im.category13,im.currentOnHandQty ,im.itemCName ,im.unitPrice,im.imageFileName ");
			hql.append("FROM ImItemOnHandView im ");
			hql.append("where ");
			hql.append("im.id.brandCode = '" + brandCode);
			hql.append("' and im.id.warehouseCode = '" + warehouseCode);
			hql.append("' and im.id.itemCode = '" + itemCode + "'");
			List result = getHibernateTemplate().find(hql.toString(), null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查詢可用庫存
	 *
	 * @param findObjs
	 * @param searchType
	 * @return
	 */
	public HashMap findPageLine(HashMap findObjs, String searchType) {
		final HashMap fos = findObjs;
		final String type = searchType;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("");
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.id.brandCode) as rowCount from ImItemOnHandView as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
					hql.append("select model.id.brandCode, model.id.itemCode, model.id.warehouseCode, model.id.lotNo from ImItemOnHandView as model where 1=1 ");
				} else {
					hql.append("from ImItemOnHandView as model where 1=1 ");
				}

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.id.brandCode = :brandCode ");

				if (StringUtils.hasText((String) fos.get("itemCName")))
					hql.append(" and model.itemCName like :itemCName ");

				if (StringUtils.hasText((String) fos.get("itemCodeList"))) { // 如果輸入多品號，則不用範圍查詢
					hql.append(" and model.id.itemCode in (" + fos.get("itemCodeList") + ")");
				} else {
					if (StringUtils.hasText((String) fos.get("startItemCode"))) {
						if (StringUtils.hasText((String) fos.get("endItemCode"))) {
							hql.append(" and model.id.itemCode >= :startItemCode ");
							hql.append(" and model.id.itemCode <= :endItemCode ");
						} else {
							hql.append(" and model.id.itemCode like :startItemCode ");
						}
					} else if (StringUtils.hasText((String) fos.get("endItemCode"))) {
						hql.append(" and model.id.itemCode like :endItemCode ");
					}
				}

				if (StringUtils.hasText((String) fos.get("startWarehouseCode"))) {
					if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
						hql.append(" and model.id.warehouseCode >= :startWarehouseCode ");
						hql.append(" and model.id.warehouseCode <= :endWarehouseCode ");
					} else {
						hql.append(" and model.id.warehouseCode like :startWarehouseCode ");
					}
				} else if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
					hql.append(" and model.id.warehouseCode like :endWarehouseCode ");
				}

				if (StringUtils.hasText((String) fos.get("startLotNo"))) {
					if (StringUtils.hasText((String) fos.get("endLotNo"))) {
						hql.append(" and model.id.lotNo >= :startLotNo ");
						hql.append(" and model.id.lotNo <= :endLotNo ");
					} else {
						hql.append(" and model.id.lotNo like :startLotNo ");
					}
				} else if (StringUtils.hasText((String) fos.get("endLotNo"))) {
					hql.append(" and model.id.lotNo like :endLotNo ");
				}

				if (StringUtils.hasText((String) fos.get("itemCategory")))
					hql.append(" and model.itemCategory = :itemCategory ");
				if (StringUtils.hasText((String) fos.get("taxType")))
					hql.append(" and model.isTax = :taxType ");
				if (StringUtils.hasText((String) fos.get("itemBrand")))
					hql.append(" and model.itemBrand = :itemBrand ");
				if (StringUtils.hasText((String) fos.get("category01")))
					hql.append(" and model.category01 = :category01 ");
				if (StringUtils.hasText((String) fos.get("category02")))
					hql.append(" and model.category02 = :category02 ");
				if (StringUtils.hasText((String) fos.get("category03")))
					hql.append(" and model.category03 = :category03 ");
				if (StringUtils.hasText((String) fos.get("category04")))
					hql.append(" and model.category04 = :category04 ");
				if (StringUtils.hasText((String) fos.get("category07")))
					hql.append(" and model.category07 = :category07 ");
				if (StringUtils.hasText((String) fos.get("category09")))
					hql.append(" and model.category09 = :category09 ");
				if (StringUtils.hasText((String) fos.get("category13")))
					hql.append(" and model.category13 = :category13 ");

				// 是否顯示負庫存或是零庫存
				//System.out.println("1. %=======> " +fos.get("showZero"));
				//System.out.println("2. %=======> " +fos.get("showMinus"));
				//System.out.println("1. %^_^=======> " +"N".equalsIgnoreCase((String) fos.get("showZero")));
				//System.out.println("2. %^_^=======> " +"N".equalsIgnoreCase((String) fos.get("showMinus")));
				// 是否顯示負庫存或是零庫存
				if ("N".equalsIgnoreCase((String) fos.get("showZero"))
						&& "N".equalsIgnoreCase((String) fos.get("showMinus"))) // 只顯示大於零的庫存
					hql.append(" and model.currentOnHandQty <> 0 ");
				else if ("N".equalsIgnoreCase((String) fos.get("showZero"))
						&& !"N".equalsIgnoreCase((String) fos.get("showMinus"))) // 只顯示負庫存
					hql.append(" and model.currentOnHandQty < 0 ");
				else if (!"N".equalsIgnoreCase((String) fos.get("showZero"))
						&& "N".equalsIgnoreCase((String) fos.get("showMinus"))) // 不顯示負庫存
					hql.append(" and model.currentOnHandQty >= 0 ");

				if (StringUtils.hasText((String) fos.get("category17"))) // 加入廠商代號
					hql.append(" and model.category17 = :category17 ");

				hql.append(" order by model.id.itemCode desc ");

				Query query = session.createQuery(hql.toString());

				if (StringUtils.hasText((String) fos.get("brandCode")))
					query.setString("brandCode", (String) fos.get("brandCode"));

				if (StringUtils.hasText((String) fos.get("itemCName")))
					query.setString("itemCName", "%" + (String) fos.get("itemCName") + "%");

				if (StringUtils.hasText((String) fos.get("itemCodeList"))) { // 如果輸入多品號，則不用範圍查詢
					//query.setString("itemCodeList", (String) fos.get("itemCodeList"));
				} else {
					if (StringUtils.hasText((String) fos.get("startItemCode"))) {
						if (StringUtils.hasText((String) fos.get("endItemCode"))) {
							query.setString("startItemCode", (String) fos.get("startItemCode"));
							query.setString("endItemCode", (String) fos.get("endItemCode"));
						} else {
							query.setString("startItemCode", "%" + (String) fos.get("startItemCode") + "%");
						}
					} else if (StringUtils.hasText((String) fos.get("endItemCode"))) {
						query.setString("endItemCode", "%" + (String) fos.get("endItemCode") + "%");
					}
				}

				if (StringUtils.hasText((String) fos.get("startWarehouseCode"))) {
					if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
						query.setString("startWarehouseCode", (String) fos.get("startWarehouseCode"));
						query.setString("endWarehouseCode", (String) fos.get("endWarehouseCode"));
					} else {
						query.setString("startWarehouseCode", "%" + (String) fos.get("startWarehouseCode") + "%");
					}
				} else if (StringUtils.hasText((String) fos.get("endWarehouseCode"))) {
					query.setString("endWarehouseCode", "%" + (String) fos.get("endWarehouseCode") + "%");
				}

				if (StringUtils.hasText((String) fos.get("startLotNo"))) {
					if (StringUtils.hasText((String) fos.get("endLotNo"))) {
						query.setString("startLotNo", (String) fos.get("startLotNo"));
						query.setString("endLotNo", (String) fos.get("endLotNo"));
					} else {
						query.setString("startLotNo", "%" + (String) fos.get("startLotNo") + "%");
					}
				} else if (StringUtils.hasText((String) fos.get("endLotNo"))) {
					query.setString("endLotNo", "%" + (String) fos.get("endLotNo") + "%");
				}

				if (StringUtils.hasText((String) fos.get("itemCategory")))
					query.setString("itemCategory", (String) fos.get("itemCategory"));
				if (StringUtils.hasText((String) fos.get("itemBrand")))
					query.setString("itemBrand", (String) fos.get("itemBrand"));
				if (StringUtils.hasText((String) fos.get("category01")))
					query.setString("category01", (String) fos.get("category01"));
				if (StringUtils.hasText((String) fos.get("category02")))
					query.setString("category02", (String) fos.get("category02"));
				if (StringUtils.hasText((String) fos.get("category03")))
					query.setString("category03", (String) fos.get("category03"));
				if (StringUtils.hasText((String) fos.get("category04")))
					query.setString("category04", (String) fos.get("category04"));
				if (StringUtils.hasText((String) fos.get("category07")))
					query.setString("category07", (String) fos.get("category07"));
				if (StringUtils.hasText((String) fos.get("category09")))
					query.setString("category09", (String) fos.get("category09"));
				if (StringUtils.hasText((String) fos.get("category13")))
					query.setString("category13", (String) fos.get("category13"));
				if (StringUtils.hasText((String) fos.get("category17"))) // 加入廠商代號查詢
					query.setString("category17", (String) fos.get("category17"));

				if (StringUtils.hasText((String) fos.get("taxType")))
					query.setString("taxType", (String) fos.get("taxType"));
				return query.list();
			}
		});

		HashMap returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long
							.valueOf(result.get(0).toString()));
		}
		return returnResult;
	}

	/**
	 * 查詢可用庫存轉條碼的資料
	 *
	 * @param loginBrandCode
	 * @param searchWarehouseCode
	 * @return
	 */
	public List findBarCode(String loginBrandCode, String searchWarehouseCode) {
		final String brandCode = loginBrandCode;
		final String warehouseCode = searchWarehouseCode;
		System.out.println("start to find BarCode....");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("");
				hql.append("from ImItemOnHandView as model where 1=1 ");

				if (StringUtils.hasText(brandCode))
					hql.append(" and model.id.brandCode = :brandCode ");

				if (StringUtils.hasText(warehouseCode)) {
					hql.append(" and model.id.warehouseCode = :warehouseCode ");
				}
				hql.append(" and model.currentOnHandQty > 0 "); // 數量 小於0的庫存不顯示
				hql.append(" order by model.id.itemCode desc ");

				Query query = session.createQuery(hql.toString());

				if (StringUtils.hasText(brandCode))
					query.setString("brandCode", brandCode);

				if (StringUtils.hasText(warehouseCode))
					query.setString("warehouseCode", warehouseCode);

				return query.list();
			}
		});

		log.info("ImItemOnHandView.BarCode size : " + result.size());
		return result;
	}
	
	public void deleteTmp(final String onHandTimeScope){
		log.info("---------------------deleteTmp---------------------");
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("delete from ImItemOnHandTmp as model where 1=1");
					hql.append("AND model.id.onHandTimeScope = :onHandTimeScope");
		    			
					Query query = session.createQuery(hql.toString());
		    			
					query.setString("onHandTimeScope", onHandTimeScope);
		    			
					return query.executeUpdate();
    		}
    	});
    }
	/*
	 * 撈取調撥單
	 */
	public List findMoveIn(String itemCode, String warehouseCode) {
		final String itemCodeString = itemCode;
		final String warehouseCodeString = warehouseCode;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("");
				hql.append("select model.id.headId, model.id.itemCode from ImMovementView as model where 1=1 ");
				hql.append(" and model.id.itemCode = :itemCode ");
				hql.append(" and model.arrivalWarehouseCode = :arrivalWarehouseCode ");
				hql.append(" and model.status = :status ");
				
				Query query = session.createQuery(hql.toString());

				query.setString("itemCode", itemCodeString);
				query.setString("arrivalWarehouseCode", warehouseCodeString);
				query.setString("status", "WAIT_IN");
				
				return query.list();
			}
		});

		return result;
	}
	
	public HashMap findOnHandModifyQuantity(HashMap findObjs, int startPage, int pageSize, String searchType)  {

		final HashMap fos = findObjs;
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String type = searchType;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.transationDate) as rowCount  from ImDailyOnHandModifyView as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_RANGE.equals(type)) {
					hql.append(" from ImDailyOnHandModifyView as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_SHIFT.equals(type)) {
					hql.append("select (model.dayTotal - model.sum) as shiftQuantity from ImDailyOnHandModifyView as model where 1=1 ");
				} 

				hql.append("AND model.warehouseCode = :warehouseCode ");
				hql.append("AND model.itemCode = :itemCode ");
				hql.append("AND model.id.brandCode = :brandCode ");
				hql.append("AND model.transationDate >= TO_DATE("+(String)fos.get("beginningYear") + (String)fos.get("beginningMonth") + "01"+",'YYYYMMDD') ");
				
				if (StringUtils.hasText((String)fos.get("endingYear")) && StringUtils.hasText((String)fos.get("endingMonth")) && StringUtils.hasText((String)fos.get("endingDay"))){
					hql.append("AND model.transationDate <= TO_DATE("+(String)fos.get("endingYear") + (String)fos.get("endingMonth") + (String)fos.get("endingDay")+",'YYYYMMDD') ");
				}
				
				hql.append("order by model.transationDate");
				
				
				log.info(hql);
				Query query = session.createQuery(hql.toString());
				query.setString("warehouseCode", (String)fos.get("warehouseCode"));
				query.setString("itemCode", (String)fos.get("itemCode"));
				query.setString("brandCode", (String)fos.get("brandCode"));
				
				if (QUARY_TYPE_SELECT_RANGE.equals(type)) {
					query.setFirstResult(startRecordIndexStar);
					query.setMaxResults(pSize);
				}
				
				return query.list();
			}
		});

		HashMap returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_SHIFT.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long.valueOf(result.get(0).toString()));
		}
		
		if (result.size() == 0) {
			returnResult.put("shiftQuantity", "0");
		}else{
			returnResult.put("shiftQuantity", QUARY_TYPE_SELECT_SHIFT.equals(type) ? result.get(0).toString() : "0");
		}
		return returnResult;

	}
	
	public List<ImItemOnHandView> getOnHandQtyListByItems(String brandCode, String itemCode) {
		List<ImItemOnHandView> rtnList = new ArrayList();
		final String fBrand = brandCode;
		final String fItemCode = itemCode;
		try{
			List<ImItemOnHandView> result = getHibernateTemplate().executeFind(new HibernateCallback() {
				public List<ImItemOnHandView> doInHibernate(Session session) throws HibernateException, SQLException {

					StringBuffer hql = new StringBuffer("");
					hql.append("from ImItemOnHandView as model where 1=1 ");

					if (StringUtils.hasText(fBrand))
						hql.append(" and model.id.brandCode = :brandCode ");

					if (StringUtils.hasText(fItemCode)) {
						hql.append(" and model.id.itemCode = :itemCode ");
					}
					hql.append(" and model.currentOnHandQty > 0 "); // 數量 小於0的庫存不顯示
					hql.append(" order by model.id.itemCode desc ");

					Query query = session.createQuery(hql.toString());

					if (StringUtils.hasText(fBrand))
						query.setString("brandCode", fBrand);

					if (StringUtils.hasText(fItemCode))
						query.setString("itemCode", fItemCode);

					return query.list();
				}
			});
			rtnList = result;
			log.info("ImItemOnHandView size : " + result.size());
			return result;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return rtnList;
	}
	
	public HashMap findOnHandOrderModifyQuantity(HashMap findObjs, int startPage, int pageSize, String searchType) {
		final HashMap fos = findObjs;
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String type = searchType;
		final Calendar now = Calendar.getInstance();
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("");
				
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.transationDate) as rowCount  from ImOrderOnHandModifyView as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_RANGE.equals(type)) {
					hql.append(" from ImOrderOnHandModifyView as model where 1=1 ");
				} 
				

				hql.append("AND model.warehouseCode = :warehouseCode ");
				hql.append("AND model.itemCode = :itemCode "); 
				hql.append("AND model.id.brandCode = :brandCode ");
				hql.append("AND model.id.orderTypeCode = :orderTypeCode ");
				hql.append("AND model.year = :year ");
				hql.append("AND model.month = :month ");
				hql.append("AND model.day = :day ");

				//hql.append("AND t.transationDate >= TO_DATE('20170701','YYYYMMDD') ");
				hql.append("order by model.transationDate");
				
				
				log.info(hql);
				Query query = session.createQuery(hql.toString());
				query.setString("warehouseCode", (String)fos.get("warehouseCode"));
				query.setString("itemCode", (String)fos.get("itemCode"));
				query.setString("brandCode", (String)fos.get("brandCode"));
				query.setString("orderTypeCode", (String)fos.get("orderTypeCode"));
				query.setString("year", (String)fos.get("year"));
				query.setString("month", (String)fos.get("month"));
				query.setString("day", (String)fos.get("day"));

				
				if (QUARY_TYPE_SELECT_RANGE.equals(type)) {
					query.setFirstResult(startRecordIndexStar);
					query.setMaxResults(pSize);
				}
				
				return query.list();
			}
		});

		HashMap returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long.valueOf(result.get(0).toString()));
		}
		return returnResult;
	}
}
