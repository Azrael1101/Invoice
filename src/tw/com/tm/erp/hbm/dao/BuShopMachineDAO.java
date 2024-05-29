package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopMachine;

public class BuShopMachineDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(BuShopMachineDAO.class);
    public List<BuShopMachine> findByShopCode(String shopCode){
	
	StringBuffer hql = new StringBuffer("from BuShopMachine as model ");
	hql.append("where model.id.shopCode = ? ");
	hql.append("order by model.id.posMachineCode ");

	List<BuShopMachine> result = getHibernateTemplate().find(hql.toString(), new Object[] {shopCode});
	return result;	
    }
	public List<BuShopMachine> findPageLine(String shopCodeFind, String enableFind, int startPage,int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String shopCode = shopCodeFind;		
		final String enable = enableFind;
		List<BuShopMachine> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from BuShopMachine as model where 1=1 ");
						if (shopCode != null)
							hql.append(" and model.id.shopCode = :shopCode");
							hql.append(" and model.enable = :enable");
							hql.append(" order by model.id.posMachineCode");
						Query query = session.createQuery(hql.toString());
						query.setFirstResult(startRecordIndexStar);
						query.setMaxResults(pSize);
						if (shopCode != null)
						{
							query.setString("shopCode", shopCode);
							query.setString("enable", enable);
						}	
						return query.list();
					}
				});
		return result;
	}
	public Long findPageLineNaxIndex(String shopCodeFind) {

		final String shopCode = shopCodeFind;		
		List<BuShopMachine> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from BuShopMachine as model where 1=1 ");
						if (shopCode != null)
							hql.append(" and model.id.shopCode = :shopCode");
							hql.append(" order by model.id.posMachineCode");
						Query query = session.createQuery(hql.toString());
						if (shopCode != null)
						{
							query.setString("shopCode", shopCode);
						}	
						return query.list();
					}
				});
		return result.size()+0l;
	}
	/*
	public List<BuShopMachine> findItemByIdentification(String buShop, String posMachineCode) {
		StringBuffer hql = new StringBuffer(
				"from BuShopMachine as model where model.id.shopCode = ? and model.id.posMachineCode = ?");
		List<BuShopMachine> result = getHibernateTemplate().find(hql.toString(), new Object[] { buShop, posMachineCode });
		return (result != null && result.size() > 0 ? result : null);
	}
	*/
	public BuShopMachine findItemByIdentification(String buShop, String posMachineCode) {
		StringBuffer hql = new StringBuffer(
				"from BuShopMachine as model where model.id.shopCode = ? and model.id.posMachineCode = ?");
		List<BuShopMachine> result = getHibernateTemplate().find(hql.toString(), new Object[] { buShop, posMachineCode });
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
    /**
     * 依據品牌、專櫃啟用狀態、機號啟用狀態查詢
     * 
     * @param brandCode
     * @param shopEnable
     * @param machineEnable
     * @return List
     */
    public List findByBrandAndEnable(final String brandCode, final String shopEnable, final String machineEnable){
	
	List result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("select model, model2 from BuShop as model, BuShopMachine as model2");
			hql.append(" where model.shopCode = model2.id.shopCode");
			hql.append(" and model.brandCode = :brandCode");
			if (StringUtils.hasText(shopEnable)){
			    hql.append(" and model.enable = :shopEnable");
			}
			if(StringUtils.hasText(machineEnable)){
			    hql.append(" and model2.enable = :machineEnable");
			}
			hql.append(" order by model2.id.posMachineCode ");
			
			Query query = session.createQuery(hql.toString());

			query.setString("brandCode", brandCode);
			if (StringUtils.hasText(shopEnable))
			    query.setString("shopEnable", shopEnable);

			if (StringUtils.hasText(machineEnable))
			    query.setString("machineEnable", machineEnable);

			return query.list();
		    }
		});

	return result;	
    }
    
    /**
     * 依據品牌、專櫃啟用狀態、機號啟用狀態查詢
     * 
     * @param brandCode
     * @param shopEnable
     * @param machineEnable
     * @return List
     */
    public List findByBrandAndScheduleDate(final String brandCode, final Date scheduleDate){
	
	List result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("select model, model2 from BuShop as model, BuShopMachine as model2");
			hql.append(" where model.shopCode = model2.id.shopCode");
			hql.append(" and model.brandCode = :brandCode");
			hql.append(" and model.enable = :shopEnable");
			hql.append(" and model.scheduleDate <= :scheduleDate");
			hql.append(" and model2.enable = :machineEnable");
			hql.append(" order by model2.id.posMachineCode ");
			
			Query query = session.createQuery(hql.toString());
			query.setString("brandCode", brandCode);
			query.setString("shopEnable", "Y");
			query.setDate("scheduleDate", scheduleDate);
			query.setString("machineEnable", "Y");
			return query.list();
		    }
		});
	return result;	
    }
    
    /**
     * 依據品牌代號、員工編號查詢出員工隸屬的機台代碼
     * 
     * @param brandCode
     * @param employeeCode
     * @param isEnable
     * @return List
     */
    public List<BuShopMachine> getShopMachineForEmployee(String brandCode,
	    String employeeCode, String isEnable) {

	final String brandCode_arg = brandCode;
	final String employeeCode_arg = employeeCode;
	final String isEnable_arg = isEnable;

	List<BuShopMachine> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer(
				"select model3 from BuShop as model, BuShopEmployee as model2, BuShopMachine as model3");
			hql.append(" where model.brandCode = :brandCode");
			hql.append(" and model2.id.employeeCode = :employeeCode");
			hql.append(" and model.shopCode = model2.id.shopCode");
			hql.append(" and model.shopCode = model3.id.shopCode");
			
			if(StringUtils.hasText(isEnable_arg)){
			    hql.append(" and model.enable = :enable");
			    hql.append(" and model2.enable = :enable");
			    hql.append(" and model3.enable = :enable");
			}
			hql.append(" order by model3.id.posMachineCode");

			Query query = session.createQuery(hql.toString());
			query.setString("brandCode", brandCode_arg);
			query.setString("employeeCode", employeeCode_arg);
			if(StringUtils.hasText(isEnable_arg)){
			    query.setString("enable", isEnable_arg);
			}

			return query.list();
		    }
		});
	return result;
    }
    
    public List<BuShopMachine> getShopMachineForEmployee(String brandCode,
    	    String employeeCode, String isEnable, String incharge) {

    	final String brandCode_arg = brandCode;
    	final String employeeCode_arg = employeeCode;
    	final String isEnable_arg = isEnable;
    	final String incharge_arg = incharge;

    	List<BuShopMachine> result = getHibernateTemplate().executeFind(
    		new HibernateCallback() {
    		    public Object doInHibernate(Session session)
    			    throws HibernateException, SQLException {

    			StringBuffer hql = new StringBuffer(
    				"select model3 from BuShop as model, BuShopEmployee as model2, BuShopMachine as model3");
    			hql.append(" where model.brandCode = :brandCode");
    			hql.append(" and model2.id.employeeCode = :employeeCode");
    			hql.append(" and model.shopCode = model2.id.shopCode");
    			hql.append(" and model.shopCode = model3.id.shopCode");
    			if(StringUtils.hasText(incharge_arg)){
    				hql.append(" and model.incharge = :incharge");
    			}
    			if(StringUtils.hasText(isEnable_arg)){
    			    hql.append(" and model.enable = :enable");
    			    hql.append(" and model2.enable = :enable");
    			    hql.append(" and model3.enable = :enable");
    			}
    			hql.append(" order by model3.id.posMachineCode");

    			Query query = session.createQuery(hql.toString());
    			query.setString("brandCode", brandCode_arg);
    			query.setString("employeeCode", employeeCode_arg);
    			if(StringUtils.hasText(incharge_arg)){
    				query.setString("incharge", incharge_arg);
    			}
    			if(StringUtils.hasText(isEnable_arg)){
    			    query.setString("enable", isEnable_arg);
    			}

    			return query.list();
    		    }
    		});
    	return result;
    }
    //拉出啟用全部機台
    public List<BuShopMachine> getShopMachineAll(String brandCode,
    	    String employeeCode, String isEnable) {

    	final String brandCode_arg = brandCode;
    	final String employeeCode_arg = employeeCode;
    	final String isEnable_arg = isEnable;

    	List<BuShopMachine> result = getHibernateTemplate().executeFind(
    		new HibernateCallback() {
    		    public Object doInHibernate(Session session)
    			    throws HibernateException, SQLException {

    			StringBuffer hql = new StringBuffer(
    				"select model3 from BuShop as model, BuShopMachine as model3");
    			hql.append(" where model.brandCode = :brandCode");
    			//hql.append(" and model2.id.employeeCode = :employeeCode");
    			//hql.append(" and model.shopCode = model2.id.shopCode");    			
    			hql.append(" and model.shopCode = model3.id.shopCode");
    			hql.append(" and model.shopCode not like '%T2%'");
    			
    			if(StringUtils.hasText(isEnable_arg)){
    			    hql.append(" and model.enable = :enable");
    			   // hql.append(" and model2.enable = :enable");
    			    hql.append(" and model3.enable = :enable");
    			}
    			hql.append(" order by model3.id.posMachineCode");

    			Query query = session.createQuery(hql.toString());
    			query.setString("brandCode", brandCode_arg);
    			//query.setString("employeeCode", employeeCode_arg);
    			if(StringUtils.hasText(isEnable_arg)){
    			    query.setString("enable", isEnable_arg);
    			}

    			return query.list();
    		    }
    		});
    	return result;
        }
    /**
     * 依據品牌、機台、專櫃啟用狀態、機號啟用狀態查詢出專櫃
     * 
     * @param brandCode
     * @param machineCode
     * @param shopEnable
     * @param machineEnable
     * @return BuShop
     */
    public BuShop getShopCodeByMachineCode(final String brandCode, final String machineCode, final String shopEnable, 
	    final String machineEnable){
	
	List<BuShop> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("select model from BuShop as model, BuShopMachine as model2");
			hql.append(" where model.shopCode = model2.id.shopCode");
			hql.append(" and model.brandCode = :brandCode");
			hql.append(" and model2.id.posMachineCode = :machineCode");
			if (StringUtils.hasText(shopEnable)){
			    hql.append(" and model.enable = :shopEnable");
			}
			if(StringUtils.hasText(machineEnable)){
			    hql.append(" and model2.enable = :machineEnable");
			}
			Query query = session.createQuery(hql.toString());
			query.setString("brandCode", brandCode);
			query.setString("machineCode", machineCode);
			if (StringUtils.hasText(shopEnable))
			    query.setString("shopEnable", shopEnable);

			if (StringUtils.hasText(machineEnable))
			    query.setString("machineEnable", machineEnable);
log.info("hql = "+hql.toString());
			return query.list();
		    }
		});

	return (result != null && result.size() > 0 ? result.get(0) : null);
    }
    
    /**
     * 依據 posMachineCode 查詢，並進行鎖定以取得單號.
     * @param posMachineCode
     * @author T96640
     */
    public List<BuShopMachine> getLockedOrderNo(final String posMachineCode) {
	List<BuShopMachine> result = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {

		StringBuffer hql = new StringBuffer("from BuShopMachine as model ");
		hql.append(" where model.id.shopCode not like 'T1%'");
		hql.append(" AND model.id.posMachineCode = :posMachineCode");

		Query query = session.createQuery(hql.toString());
		query.setLockMode("model", LockMode.UPGRADE_NOWAIT);
		query.setString("posMachineCode", posMachineCode);

		return query.list();
	    }
	});

	return result;
    }
    
	public BuShopMachine findById(tw.com.tm.erp.hbm.bean.BuShopMachineId id) {
		log.debug("getting BuShopMachine instance with id: " + id);
		try {
		    BuShopMachine instance = (BuShopMachine) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuShopMachine", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * 依據品牌代號、員工編號、機台起迄查詢出員工隸屬的機台代碼
	 * 
	 * @param brandCode
	 * @param employeeCode
	 * @param isEnable
	 * @return List
	 */
	public List<BuShopMachine> getShopMachineForEmployee(String brandCode,
			String employeeCode, String isEnable, String salesUnitType, String benginShop, String endShop) {
		log.info("getShopMachineForEmployeeFor");
		final String brandCode_arg = brandCode;
		final String employeeCode_arg = employeeCode;
		final String isEnable_arg = isEnable;
		final String salesUnitType_arg = salesUnitType;
		final String benginShopCode = benginShop;
		final String endShopCode = endShop;
		log.info("salesUnitType_arg = " + salesUnitType_arg);
		log.info("benginShopCode = " + benginShopCode);
		log.info("endShopCode = " + endShopCode);
		
		List<BuShopMachine> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer(
								"select model3 from BuShop as model, BuShopEmployee as model2, BuShopMachine as model3");
						hql.append(" where model.brandCode = :brandCode");
						hql.append(" and model2.id.employeeCode = :employeeCode");
						hql.append(" and model.shopCode = model2.id.shopCode");
						hql.append(" and model.shopCode = model3.id.shopCode");

						if(StringUtils.hasText(isEnable_arg)){
							hql.append(" and model.enable = :enable");
							hql.append(" and model2.enable = :enable");
							hql.append(" and model3.enable = :enable");
						}

						if("S".equals(salesUnitType_arg)){
							if(StringUtils.hasText(benginShopCode))
								hql.append(" and model3.id.shopCode >= :benginShopCode");
							if(StringUtils.hasText(endShopCode))
								hql.append(" and model3.id.shopCode <= :endShopCode");
							hql.append(" order by model3.id.shopCode");
						}else if("M".equals(salesUnitType_arg)){
							if(StringUtils.hasText(benginShopCode))
								hql.append(" and model3.id.posMachineCode >= :benginShopCode");
							if(StringUtils.hasText(endShopCode))
								hql.append(" and model3.id.posMachineCode <= :endShopCode");
							hql.append(" order by model3.id.posMachineCode");
						}
						
						Query query = session.createQuery(hql.toString());
						query.setString("brandCode", brandCode_arg);
						query.setString("employeeCode", employeeCode_arg);
						if(StringUtils.hasText(isEnable_arg))
							query.setString("enable", isEnable_arg);
						if(StringUtils.hasText(benginShopCode))
							query.setString("benginShopCode", benginShopCode);
						if(StringUtils.hasText(endShopCode))
							query.setString("endShopCode", endShopCode);
						return query.list();
					}
				});
		return result;
	}
	
	/**
     * 依據品牌、專櫃啟用狀態、機號啟用狀態查詢
     * 
     * @param brandCode
     * @param shopEnable
     * @param machineEnable
     * @return List
     */
    public BuShopMachine findByBrandCodeAndMachineCode(final String brandCode, final String machineCode){
	
    	List<BuShopMachine> result = getHibernateTemplate().executeFind(
    			new HibernateCallback() {
    				public Object doInHibernate(Session session)
    				throws HibernateException, SQLException {

    					StringBuffer hql = new StringBuffer("select model2 from BuShop as model, BuShopMachine as model2");
    					hql.append(" where model.shopCode = model2.id.shopCode");
    					hql.append(" and model.brandCode = :brandCode");
    					hql.append(" and model2.id.posMachineCode = :machineCode");
    					hql.append(" order by model2.id.posMachineCode ");

    					Query query = session.createQuery(hql.toString());
    					query.setString("brandCode", brandCode);
    					query.setString("machineCode", machineCode);
    					return query.list();
    				}
    			});
    	return (result != null && result.size() > 0 ? result.get(0) : null);	
    }
    
    public List findBatchTime() {
		try {
			
			StringBuffer hql = new StringBuffer("from BuBatchConfig as line ");
							
			hql.append("order by line.batchId");
			return getHibernateTemplate().find(hql.toString(), new Object[]{});

		} catch (RuntimeException re) {
			throw re;
		}
	}
}