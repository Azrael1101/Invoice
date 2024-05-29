package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.InsufficientQuantityException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImOnHandId;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.utils.NumberUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImOnHand entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImOnHand
 * @author MyEclipse Persistence Tools
 */

public class ImOnHandDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImOnHandDAO.class);
	// property constants
	public static final String STOCK_ON_HAND_QTY = "stockOnHandQty";
	public static final String UNCOMMIT_QTY = "uncommitQty";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	private ImWarehouseDAO imWarehouseDAO;
	
	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO){
		this.imWarehouseDAO = imWarehouseDAO;
	}
	
	protected void initDao() {
		// do nothing
	}

	public void save(ImOnHand transientInstance) {
		log.debug("saving ImOnHand instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void update(ImOnHand imOnHand) {
		try {
			getHibernateTemplate().update(imOnHand);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 以自己建立的 Bean 來更新資料庫資料
	 * 
	 * @param imOnHand
	 */
	public void updateByExistBean(ImOnHand imOnHand) {
		try {
			ImOnHand i = this.findById(new ImOnHandId(imOnHand.getId().getOrganizationCode(), imOnHand.getId().getItemCode(), imOnHand
					.getId().getWarehouseCode(), imOnHand.getId().getLotNo()));
			i.setInUncommitQty(imOnHand.getInUncommitQty());
			i.setOutUncommitQty(imOnHand.getOutUncommitQty());
			i.setLastUpdateDate(new Date());
			i.setLastUpdatedBy(imOnHand.getLastUpdatedBy());
			getHibernateTemplate().update(i);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(ImOnHand persistentInstance) {
		log.debug("deleting ImOnHand instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImOnHand findById(tw.com.tm.erp.hbm.bean.ImOnHandId id) {
		log.debug("getting ImOnHand instance with id: " + id);
		try {
			ImOnHand instance = (ImOnHand) getHibernateTemplate().get("tw.com.tm.erp.hbm.bean.ImOnHand", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImOnHand instance) {
		log.debug("finding ImOnHand instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding ImOnHand instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from ImOnHand as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByStockOnHandQty(Object stockOnHandQty) {
		return findByProperty(STOCK_ON_HAND_QTY, stockOnHandQty);
	}

	public List findByUncommitQty(Object uncommitQty) {
		return findByProperty(UNCOMMIT_QTY, uncommitQty);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findAll() {
		log.debug("finding all ImOnHand instances");
		try {
			String queryString = "from ImOnHand";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public List findAllBrandShopOnHand(String organizationCode, String warehouseCodes) {
	    
	        String brandCode = null;
	        if(warehouseCodes.length() > 5){
	            brandCode = getBrandCode(warehouseCodes.substring(1));          
	        }
		StringBuffer hql = new StringBuffer(
				"select model.id.organizationCode, model.id.warehouseCode,model.id.itemCode, sum(model.stockOnHandQty - model.outUncommitQty + "
						+ "model.inUncommitQty + model.moveUncommitQty + model.otherUncommitQty) from ImOnHand as model");
		hql.append(" where model.id.organizationCode = ? and model.id.warehouseCode in (" + warehouseCodes + ")");
		hql.append(" and model.brandCode = ?");
		hql.append(" group by model.id.organizationCode, model.brandCode, model.id.warehouseCode , model.id.itemCode");
		Object[] conditionArray = new Object[]{organizationCode, brandCode};
			
		List result = getHibernateTemplate().find(hql.toString(), conditionArray);

		return result;
	}

	/**
	 * 依據organizationCode和itemCode查詢出目前可用庫存
	 * 
	 * @param organizationCode
	 * @param itemCode
	 * @return Double
	 */
	public Double getCurrentStockOnHandQty(String organizationCode, String itemCode) {
		StringBuffer hql = new StringBuffer("select sum(model.stockOnHandQty - model.outUncommitQty + "
				+ "model.inUncommitQty + model.moveUncommitQty + model.otherUncommitQty) from ImOnHand as model");
		hql.append(" where model.id.itemCode = ?");
		Object[] obj;
		if (organizationCode != null) {
			hql.append(" and model.id.organizationCode = ?");
			obj = new Object[] { itemCode, organizationCode };
		} else {
			obj = new Object[] { itemCode };
		}
		hql.append(" group by model.id.organizationCode, model.id.itemCode");

		List result = getHibernateTemplate().find(hql.toString(), obj);

		return (result.size() > 0 ? (Double) result.get(0) : null);
	}

	/**
	 * 依據organizationCode、itemCode、warehouseCode查詢出目前可用庫存
	 * 
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @return Double
	 */
	public Double getCurrentStockOnHandQty(String organizationCode, String itemCode, String warehouseCode) 
	        throws ValidationErrorException{
	    
	        String brandCode = getBrandCode(warehouseCode);
	        if(!StringUtils.hasText(brandCode)){
	            throw new ValidationErrorException("無法解析庫別(" + warehouseCode + ")的品牌！");
	        }
		StringBuffer hql = new StringBuffer("select sum(model.stockOnHandQty - model.outUncommitQty + "
				+ "model.inUncommitQty + model.moveUncommitQty + model.otherUncommitQty) from ImOnHand as model");
		hql.append(" where model.id.organizationCode = ?");
		hql.append(" and model.id.itemCode = ?");
		hql.append(" and model.id.warehouseCode = ?");
		hql.append(" and model.brandCode = ?");
		hql.append(" group by model.id.organizationCode, model.brandCode, model.id.itemCode, model.id.warehouseCode");
		Object[] conditionArray = new Object[]{organizationCode, itemCode, warehouseCode, brandCode};		
		List result = getHibernateTemplate().find(hql.toString(), conditionArray);		

		return (result != null && result.size() > 0 ? (Double) result.get(0) : null);
	}

	/**
	 * 依據organizationCode、warehouseCode 查詢出 warehouse 的商品數量 20081012 shan add
	 * 
	 * @param organizationCode
	 * @param warehouseCode
	 * @return Double
	 */
	public Double getWarehouseCurrentStockOnHandQty(String organizationCode, String warehouseCode) throws ValidationErrorException{
	    
	        String brandCode = getBrandCode(warehouseCode);
	        if(!StringUtils.hasText(brandCode)){
	            throw new ValidationErrorException("無法解析庫別(" + warehouseCode + ")的品牌！");
	        }
		StringBuffer hql = new StringBuffer("select sum(model.stockOnHandQty - model.outUncommitQty + "
				+ "model.inUncommitQty + model.moveUncommitQty + model.otherUncommitQty) from ImOnHand as model");
		hql.append(" where model.id.organizationCode = ?");
		hql.append(" and model.id.warehouseCode = ?");
		hql.append(" and model.brandCode = ?");
		hql.append(" group by model.id.organizationCode, model.brandCode, model.id.warehouseCode");
		Object[] conditionArray = new Object[]{organizationCode, warehouseCode, brandCode};		
		List result = getHibernateTemplate().find(hql.toString(), conditionArray);

		return (result.size() > 0 ? (Double) result.get(0) : null);
	}

	/**
	 * 依據organizationCode、itemCode、warehouseCode、lotNo查詢出目前可用庫存
	 * 
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @return Double
	 */
	public Double getCurrentStockOnHandQty(String organizationCode, String itemCode, String warehouseCode, String lotNo) 
	        throws ValidationErrorException{
	    
	        String brandCode =  getBrandCode(warehouseCode);
	        if(!StringUtils.hasText(brandCode)){
	            throw new ValidationErrorException("無法解析庫別(" + warehouseCode + ")的品牌！");
	        }
		StringBuffer hql = new StringBuffer("select sum(nvl(model.stockOnHandQty,0) - nvl(model.outUncommitQty,0) + "
				+ "nvl(model.inUncommitQty,0) + nvl(model.moveUncommitQty,0) + nvl(model.otherUncommitQty,0)) from ImOnHand as model");
		hql.append(" where model.id.organizationCode = ?");
		hql.append(" and model.id.itemCode = ?");
		hql.append(" and model.id.warehouseCode = ?");
		hql.append(" and model.id.lotNo = ?");
		hql.append(" and model.brandCode = ?");
		hql.append(" group by model.id.organizationCode, model.brandCode, model.id.itemCode, model.id.warehouseCode, model.id.lotNo");
		Object[] conditionArray =  new Object[]{organizationCode, itemCode, warehouseCode, lotNo, brandCode};			
		List result = getHibernateTemplate().find(hql.toString(), conditionArray);		

		return (result.size() > 0 ? (Double) result.get(0) : null);
	}
	
	/**
	 * 依據organizationCode、itemCode、warehouseCode、lotNo查詢出目前可用庫存
	 * 
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @return Double
	 */
	public Double getStockOnHandQty(String organizationCode, String itemCode, String warehouseCode
			, String lotNo, String brandCode) 
	        throws ValidationErrorException{
	    
	        //String brandCode =  getBrandCode(warehouseCode);
	        if(!StringUtils.hasText(brandCode)){
	            throw new ValidationErrorException("無法解析庫別(" + warehouseCode + ")的品牌！");
	        }
		StringBuffer hql = new StringBuffer("select sum(nvl(model.stockOnHandQty,0) - nvl(model.outUncommitQty,0) + "
				+ "nvl(model.inUncommitQty,0) + nvl(model.moveUncommitQty,0) + nvl(model.otherUncommitQty,0)) from ImOnHand as model");
		hql.append(" where model.id.organizationCode = ?");
		hql.append(" and model.id.itemCode = ?");
		hql.append(" and model.id.warehouseCode = ?");
		hql.append(" and model.id.lotNo = ?");
		hql.append(" and model.brandCode = ?");
		hql.append(" group by model.id.organizationCode, model.brandCode, model.id.itemCode, model.id.warehouseCode, model.id.lotNo");
		Object[] conditionArray =  new Object[]{organizationCode, itemCode, warehouseCode, lotNo, brandCode};			
		List result = getHibernateTemplate().find(hql.toString(), conditionArray);		

		return (result.size() > 0 ? (Double) result.get(0) : null);
	}

	/**
	 * 依據organizationCode、itemCode、warehouseCode、lotNo查詢，並進行鎖定
	 * 
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @return List
	 */
	public List<ImOnHand> getLockedOnHand(final String organizationCode, final String itemCode, final String warehouseCode,
			final String lotNo) throws ValidationErrorException{
	        final String brandCode = getBrandCode(warehouseCode);
	        if(!StringUtils.hasText(brandCode)){
	            throw new ValidationErrorException("無法解析庫別(" + warehouseCode + ")的品牌！");
	        }
		List<ImOnHand> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
                                			        			        
				StringBuffer hql = new StringBuffer("from ImOnHand as model ");
				hql.append(" where model.id.organizationCode = :organizationCode");
				hql.append(" and model.id.itemCode = :itemCode");
				hql.append(" and model.id.warehouseCode = :warehouseCode");
				hql.append(" and model.brandCode = :brandCode");
				if (lotNo != null) {
					hql.append(" and model.id.lotNo = :lotNo");
				}					
				
				Query query = session.createQuery(hql.toString());
				query.setLockMode("model", LockMode.UPGRADE_NOWAIT);
				query.setString("organizationCode", organizationCode);
				query.setString("itemCode", itemCode);
				query.setString("warehouseCode", warehouseCode);
				query.setString("brandCode", brandCode);
				if (lotNo != null) {
					query.setString("lotNo", lotNo);
				}

				return query.list();
			}
		});

		return result;
	}

	/**
	 * 依據organizationCode、itemCode、warehouseCode、lotNo、brandCode查詢，並進行鎖定
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @param brandCode
	 * @return List
	 */
	public List<ImOnHand> getLockedOnHand(final String organizationCode, final String itemCode, final String warehouseCode,
			final String lotNo, final String brandCode) {
		List<ImOnHand> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImOnHand as model ");
				hql.append(" where model.id.organizationCode = :organizationCode");
				hql.append(" and model.id.itemCode = :itemCode");
				hql.append(" and model.id.warehouseCode = :warehouseCode");
				hql.append(" and model.brandCode = :brandCode");
				if (lotNo != null) {
					hql.append(" and model.id.lotNo = :lotNo");
				}
				Query query = session.createQuery(hql.toString());
				query.setLockMode("model", LockMode.UPGRADE_NOWAIT);
				query.setString("organizationCode", organizationCode);
				query.setString("itemCode", itemCode);
				query.setString("warehouseCode", warehouseCode);
				query.setString("brandCode", brandCode);
				
				if (lotNo != null) {
					query.setString("lotNo", lotNo);
				}
				log.info("SQL:"+query.toString());
				return query.list();
			}
		});

		return result;
	}
	
	/**
	 * 依據organizationCode、itemCode、warehouseCode、lotNo、brandCode查詢，並進行鎖定
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @param brandCode
	 * @return List
	 */
	public List<ImOnHand> getNoLockedOnHand(final String organizationCode, final String itemCode, final String warehouseCode,
			final String lotNo, final String brandCode) {
		List<ImOnHand> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImOnHand as model ");
				hql.append(" where model.id.organizationCode = :organizationCode");
				hql.append(" and model.id.itemCode = :itemCode");
				hql.append(" and model.id.warehouseCode = :warehouseCode");
				hql.append(" and model.brandCode = :brandCode");
				if (lotNo != null) {
					hql.append(" and model.id.lotNo = :lotNo");
				}
				Query query = session.createQuery(hql.toString());
				//query.setLockMode("model", LockMode.UPGRADE_NOWAIT);
				query.setString("organizationCode", organizationCode);
				query.setString("itemCode", itemCode);
				query.setString("warehouseCode", warehouseCode);
				query.setString("brandCode", brandCode);
				
				if (lotNo != null) {
					query.setString("lotNo", lotNo);
				}
				return query.list();
			}
		});

		return result;
	}
	
	/**
	 * 預訂可用庫存(實作FIFO及LIFO)
	 * 
	 * @param lockedOnHands
	 * @param orderQuantity
	 * @param stockControl
	 * @param opUser
	 */
	public void bookAvailableQuantity(List<ImOnHand> lockedOnHands, Double orderQuantity, String stockControl, String opUser) {
		log.info("bookAvailableQuantity");
		if ("FIFO".equals(stockControl)) {
			log.info("FIFO");
			for (ImOnHand onHand : lockedOnHands) {
				Double outUncommitQty = onHand.getOutUncommitQty();
				Double availableQuantity = onHand.getStockOnHandQty() - outUncommitQty + onHand.getInUncommitQty()
						+ onHand.getMoveUncommitQty() + onHand.getOtherUncommitQty();
				if (availableQuantity > 0D && orderQuantity > 0D) {
					if (orderQuantity > availableQuantity) {
						onHand.setOutUncommitQty(outUncommitQty + availableQuantity);
						onHand.setLastUpdatedBy(opUser);
						onHand.setLastUpdateDate(new Date());
						update(onHand);
						orderQuantity -= availableQuantity;
					} else if (orderQuantity <= availableQuantity) {
						onHand.setOutUncommitQty(outUncommitQty + orderQuantity);
						onHand.setLastUpdatedBy(opUser);
						onHand.setLastUpdateDate(new Date());
						update(onHand);
						break;
					}
				}
			}
		} else {
			log.info("NOT FIFO");
			for (int i = lockedOnHands.size() - 1; i >= 0; i--) {
				ImOnHand onHand = lockedOnHands.get(i);
				Double outUncommitQty = onHand.getOutUncommitQty();
				Double availableQuantity = onHand.getStockOnHandQty() - outUncommitQty + onHand.getInUncommitQty()
						+ onHand.getMoveUncommitQty() + onHand.getOtherUncommitQty();
				if (availableQuantity > 0D && orderQuantity > 0D) {
					if (orderQuantity > availableQuantity) {
						onHand.setOutUncommitQty(outUncommitQty + availableQuantity);
						onHand.setLastUpdatedBy(opUser);
						onHand.setLastUpdateDate(new Date());
						update(onHand);
						orderQuantity -= availableQuantity;
					} else if (orderQuantity <= availableQuantity) {
						onHand.setOutUncommitQty(outUncommitQty + orderQuantity);
						onHand.setLastUpdatedBy(opUser);
						onHand.setLastUpdateDate(new Date());
						update(onHand);
						break;
					}
				}
			}
		}
	}

	/**
	 * 預訂可用庫存
	 * 
	 * @param lockedOnHand
	 * @param salesQuantity
	 * @param loginUser
	 */
	public void bookAvailableQuantity(ImOnHand lockedOnHand, Double salesQuantity, String loginUser) {

		lockedOnHand.setOutUncommitQty(lockedOnHand.getOutUncommitQty() + salesQuantity);
		// UserUtils.setUserAndDate(lockedOnHand, loginUser);
		lockedOnHand.setLastUpdatedBy(loginUser);
		lockedOnHand.setLastUpdateDate(new Date());
		update(lockedOnHand);
	}

	public void updateOutUncommitQuantity(String organizationCode, String itemCode, String warehouseCode, String lotNo, Double quantity,
			String loginUser) throws FormException {
	        String brandCode = null;
		try {
		        brandCode = getBrandCode(warehouseCode);
		        if(!StringUtils.hasText(brandCode)){
		            throw new ValidationErrorException("無法解析庫別(" + warehouseCode + ")的品牌！");
		        }
			List<ImOnHand> lockedOnHands = getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				ImOnHand lockedOnHand = (ImOnHand) lockedOnHands.get(0);
				lockedOnHand.setOutUncommitQty(lockedOnHand.getOutUncommitQty() - quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				ImOnHandId id = new ImOnHandId();
				ImOnHand newOnHand = new ImOnHand();
				id.setOrganizationCode(organizationCode);
				id.setItemCode(itemCode);
				id.setWarehouseCode(warehouseCode);
				id.setLotNo(lotNo);
				newOnHand.setId(id);
				newOnHand.setBrandCode(brandCode); //增加庫存的品牌
				newOnHand.setStockOnHandQty(0D);
				newOnHand.setOutUncommitQty(0D - quantity);
				newOnHand.setInUncommitQty(0D);
				newOnHand.setMoveUncommitQty(0D);
				newOnHand.setOtherUncommitQty(0D);
				newOnHand.setCreatedBy(loginUser);
				newOnHand.setCreationDate(new Date());
				newOnHand.setLastUpdatedBy(loginUser);
				newOnHand.setLastUpdateDate(new Date());
				save(newOnHand);
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}

	public void updateOutUncommitQuantity(String organizationCode, String itemCode, String warehouseCode, String lotNo, Double quantity,
			String loginUser, String brandCode) throws FormException {
		try {
			List<ImOnHand> lockedOnHands = getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				ImOnHand lockedOnHand = (ImOnHand) lockedOnHands.get(0);
				lockedOnHand.setOutUncommitQty(lockedOnHand.getOutUncommitQty() - quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				ImOnHandId id = new ImOnHandId();
				ImOnHand newOnHand = new ImOnHand();
				id.setOrganizationCode(organizationCode);
				id.setItemCode(itemCode);
				id.setWarehouseCode(warehouseCode);
				id.setLotNo(lotNo);
				newOnHand.setId(id);
				newOnHand.setBrandCode(brandCode); //增加庫存的品牌
				newOnHand.setStockOnHandQty(0D);
				newOnHand.setOutUncommitQty(0D - quantity);
				newOnHand.setInUncommitQty(0D);
				newOnHand.setMoveUncommitQty(0D);
				newOnHand.setOtherUncommitQty(0D);
				newOnHand.setCreatedBy(loginUser);
				newOnHand.setCreationDate(new Date());
				newOnHand.setLastUpdatedBy(loginUser);
				newOnHand.setLastUpdateDate(new Date());
				save(newOnHand);
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}
	
	/**
	 * 增加可用庫存
	 * 
	 * @param lockedOnHand
	 * @param receiveQuantity
	 * @param loginUser
	 * @throws FormException
	 */
	public void addAvailableQuantity(String organizationCode, String itemCode, String warehouseCode, String lotNo, Double receiveQuantity,
			String loginUser) throws FormException {
	        String brandCode = null;
		try {
		        brandCode = getBrandCode(warehouseCode);
		        if(!StringUtils.hasText(brandCode)){
		            throw new ValidationErrorException("無法解析庫別(" + warehouseCode + ")的品牌！");
		        }
			List<ImOnHand> lockedOnHands = this.getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			ImOnHand lockedOnHand = null;
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				lockedOnHand = lockedOnHands.get(0);
				lockedOnHand.setInUncommitQty(lockedOnHand.getInUncommitQty() + receiveQuantity);
				// UserUtils.setUserAndDate(lockedOnHand, loginUser);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				ImOnHandId id = new ImOnHandId();
				lockedOnHand = new ImOnHand();
				id.setOrganizationCode(organizationCode);
				id.setItemCode(itemCode);
				id.setWarehouseCode(warehouseCode);
				id.setLotNo(lotNo);
				lockedOnHand.setId(id);
				lockedOnHand.setBrandCode(brandCode); //增加庫存的品牌
				lockedOnHand.setStockOnHandQty(0D);
				lockedOnHand.setOutUncommitQty(0D);
				lockedOnHand.setInUncommitQty(receiveQuantity);
				lockedOnHand.setCreatedBy(loginUser);
				lockedOnHand.setCreationDate(new Date());
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				save(lockedOnHand);
			}

		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}

	/**
	 * 
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @param quantity
	 * @param loginUser
	 * @throws FormException
	 */
	public void updateMoveUncommitQuantity(String organizationCode, String itemCode, String warehouseCode, String lotNo, Double quantity,
			String loginUser) throws FormException {
	        String brandCode = null;
		try {
		        brandCode = getBrandCode(warehouseCode);
		        if(!StringUtils.hasText(brandCode)){
		            throw new ValidationErrorException("無法解析庫別(" + warehouseCode + ")的品牌！");
		        }
			List<ImOnHand> lockedOnHands = this.getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			ImOnHand lockedOnHand = null;
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				if (quantity < 0) {
					Double availableQuantity = this.getCurrentStockOnHandQty(organizationCode, itemCode, warehouseCode, lotNo);

					if (availableQuantity == null) {
						throw new NoSuchObjectException("更新庫存時，查無品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")的庫存量！");
					} else if (quantity > availableQuantity) {
						throw new InsufficientQuantityException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")可用庫存量不足！");
					}
				}

				lockedOnHand = lockedOnHands.get(0);
				Double movementUncommitQty = lockedOnHand.getMoveUncommitQty() == null ? 0D : lockedOnHand.getMoveUncommitQty();
				lockedOnHand.setMoveUncommitQty(movementUncommitQty + quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);

			} else {
				if (quantity > 0) {
					ImOnHandId id = new ImOnHandId();
					lockedOnHand = new ImOnHand();
					id.setOrganizationCode(organizationCode);
					id.setItemCode(itemCode);
					id.setWarehouseCode(warehouseCode);
					id.setLotNo(lotNo);
					lockedOnHand.setId(id);
					lockedOnHand.setBrandCode(brandCode); //增加庫存的品牌
					lockedOnHand.setStockOnHandQty(0D);
					lockedOnHand.setOutUncommitQty(0D);
					lockedOnHand.setInUncommitQty(0D);
					lockedOnHand.setMoveUncommitQty(quantity);
					lockedOnHand.setOtherUncommitQty(0D);
					lockedOnHand.setCreatedBy(loginUser);
					lockedOnHand.setCreationDate(new Date());
					lockedOnHand.setLastUpdatedBy(loginUser);
					lockedOnHand.setLastUpdateDate(new Date());
					save(lockedOnHand);
				} else {
					throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")目前並無庫存數量，請確認！");
				}
			}

		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}

	/**
	 * 
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @param quantity
	 * @param loginUser
	 * @throws FormException
	 */
	public void updateMoveUncommitQuantityAllowMinus(String organizationCode, String itemCode, String warehouseCode, String lotNo,
			Double quantity, String loginUser) throws FormException {
	        String brandCode = null;
		try {
		        brandCode = getBrandCode(warehouseCode);
		        if(!StringUtils.hasText(brandCode)){
		            throw new ValidationErrorException("無法解析庫別(" + warehouseCode + ")的品牌！");
		        }
			System.out.println("A:" + organizationCode + "/" + itemCode + "/" + warehouseCode + "/" + lotNo + "/" + quantity + "/"
					+ loginUser);

			List<ImOnHand> lockedOnHands = this.getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			System.out.println(quantity);
			ImOnHand lockedOnHand = null;
			System.out.println("C");
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				System.out.println("D");
				/*
				 * if (quantity < 0) { Double availableQuantity =
				 * this.getCurrentStockOnHandQty(organizationCode, itemCode,
				 * warehouseCode, lotNo);
				 * 
				 * if (availableQuantity == null) { throw new
				 * NoSuchObjectException("更新庫存時，查無品號：" + itemCode + ",庫別：" +
				 * warehouseCode + ",批號：" + lotNo + "的庫存量！"); } else if
				 * (quantity > availableQuantity) { throw new
				 * InsufficientQuantityException("品號：" + itemCode + ",庫別：" +
				 * warehouseCode + ",批號：" + lotNo + "可用庫存量不足！"); } }
				 */

				lockedOnHand = lockedOnHands.get(0);
				Double movementUncommitQty = lockedOnHand.getMoveUncommitQty() == null ? 0D : lockedOnHand.getMoveUncommitQty();
				lockedOnHand.setMoveUncommitQty(movementUncommitQty + quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				// if (quantity > 0) {
				ImOnHandId id = new ImOnHandId();
				lockedOnHand = new ImOnHand();
				id.setOrganizationCode(organizationCode);
				id.setItemCode(itemCode);
				id.setWarehouseCode(warehouseCode);
				id.setLotNo(lotNo);
				lockedOnHand.setId(id);
				lockedOnHand.setBrandCode(brandCode); //增加庫存的品牌
				lockedOnHand.setStockOnHandQty(0D);
				lockedOnHand.setOutUncommitQty(0D);
				lockedOnHand.setInUncommitQty(0D);
				lockedOnHand.setMoveUncommitQty(quantity);
				lockedOnHand.setOtherUncommitQty(0D);
				lockedOnHand.setCreatedBy(loginUser);
				lockedOnHand.setCreationDate(new Date());
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				save(lockedOnHand);
				// } else {
				// throw new FormException("品號：" + itemCode + ",庫別：" +
				// warehouseCode + ",批號：" + lotNo + "目前並無庫存數量，請確認！");
				// }
			}

		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}

	public void updateMoveOnHand(String organizationCode, String itemCode, String warehouseCode, String lotNo, Double quantity,
			String loginUser) throws FormException {
	        String brandCode = getBrandCode(warehouseCode);
	        if(!StringUtils.hasText(brandCode)){
	            throw new ValidationErrorException("無法解析庫別(" + warehouseCode + ")的品牌！");
	        }
		List<ImOnHand> lockedOnHands = this.getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
		ImOnHand lockedOnHand = null;
		if (lockedOnHands != null && lockedOnHands.size() > 0) {
			lockedOnHand = lockedOnHands.get(0);
			Double movementUncommitQty = lockedOnHand.getMoveUncommitQty() == null ? 0D : lockedOnHand.getMoveUncommitQty();
			lockedOnHand.setMoveUncommitQty(movementUncommitQty - quantity);
			lockedOnHand.setStockOnHandQty(lockedOnHand.getStockOnHandQty() + quantity);
			lockedOnHand.setLastUpdatedBy(loginUser);
			lockedOnHand.setLastUpdateDate(new Date());
			update(lockedOnHand);
		} else {
			throw new FormException("組織(" + organizationCode + ")、品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo
					+ ")目前並無庫存數量，請確認！");
		}

	}

	public ImOnHand merge(ImOnHand detachedInstance) {
		log.debug("merging ImOnHand instance");
		try {
			ImOnHand result = (ImOnHand) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImOnHand instance) {
		log.debug("attaching dirty ImOnHand instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImOnHand instance) {
		log.debug("attaching clean ImOnHand instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImOnHandDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ImOnHandDAO) ctx.getBean("imOnHandDAO");
	}

	/**
	 * 預訂可用庫存(實作FIFO及LIFO，不管庫存量是否足夠直接扣除)
	 * 
	 * @param lockedOnHands
	 * @param orderQuantity
	 * @param stockControl
	 * @param opUser
	 */
	public void bookQuantity(List<ImOnHand> lockedOnHands, Double orderQuantity, String stockControl, String opUser) {
		log.info("bookQuantity");
		if ("FIFO".equals(stockControl)) {
			log.info("FIFO");
			for (ImOnHand onHand : lockedOnHands) {
				onHand.setOutUncommitQty(onHand.getOutUncommitQty() + orderQuantity);
				onHand.setLastUpdatedBy(opUser);
				onHand.setLastUpdateDate(new Date());
				update(onHand);
				break;
			}
		} else {
			log.info("NOT FIFO");
			for (int i = lockedOnHands.size() - 1; i >= 0; i--) {
				ImOnHand onHand = lockedOnHands.get(i);
				onHand.setOutUncommitQty(onHand.getOutUncommitQty() + orderQuantity);
				onHand.setLastUpdatedBy(opUser);
				onHand.setLastUpdateDate(new Date());
				update(onHand);
				break;
			}
		}
	}

	public void updateOutOnHand(String organizationCode, String itemCode, String warehouseCode, String lotNo, Double quantity,
			String loginUser) throws FormException {
	        String brandCode = null;
		try {
		        brandCode = getBrandCode(warehouseCode);
		        if(!StringUtils.hasText(brandCode)){
		            throw new ValidationErrorException("無法解析庫別(" + warehouseCode + ")的品牌！");
		        }
			List<ImOnHand> lockedOnHands = getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				ImOnHand lockedOnHand = (ImOnHand) lockedOnHands.get(0);
				Double outUncommitQty = (lockedOnHand.getOutUncommitQty() == null) ? 0D : lockedOnHand.getOutUncommitQty();
				lockedOnHand.setOutUncommitQty(outUncommitQty - quantity);
				lockedOnHand.setStockOnHandQty(lockedOnHand.getStockOnHandQty() - quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				throw new FormException("查無品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")的庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}

	public void updateOtherOnHand(String organizationCode, String itemCode, String warehouseCode, String lotNo, Double quantity,
			String loginUser) throws FormException {
	        String brandCode = null;
		try {
		        brandCode = getBrandCode(warehouseCode);
		        if(!StringUtils.hasText(brandCode)){
		            throw new ValidationErrorException("無法解析庫別(" + warehouseCode + ")的品牌！");
		        }
			List<ImOnHand> lockedOnHands = getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				ImOnHand lockedOnHand = (ImOnHand) lockedOnHands.get(0);
				Double otherUncommitQty = (lockedOnHand.getOtherUncommitQty() == null) ? 0D : lockedOnHand.getOtherUncommitQty();
				lockedOnHand.setOtherUncommitQty(otherUncommitQty - quantity);
				lockedOnHand.setStockOnHandQty(lockedOnHand.getStockOnHandQty() + quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				throw new FormException("查無品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")的庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}

	public void updateInOnHand(String organizationCode, String itemCode, String warehouseCode, String lotNo, Double quantity,
			String loginUser) throws FormException {
	        String brandCode = null;
		try {
		        brandCode = getBrandCode(warehouseCode);
		        if(!StringUtils.hasText(brandCode)){
		            throw new ValidationErrorException("無法解析庫別(" + warehouseCode + ")的品牌！");
		        }
			List<ImOnHand> lockedOnHands = getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				ImOnHand lockedOnHand = (ImOnHand) lockedOnHands.get(0);
				Double inUncommitQty = (lockedOnHand.getInUncommitQty() == null) ? 0D : lockedOnHand.getInUncommitQty();
				// lockedOnHand.setOtherUncommitQty(inUncommitQty - quantity);
				// 20090306 shan fix
				lockedOnHand.setInUncommitQty(inUncommitQty - quantity);
				lockedOnHand.setStockOnHandQty(lockedOnHand.getStockOnHandQty() + quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				throw new FormException("查無品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")的庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}

	
	public void updateInOnHand(String organizationCode, String itemCode, String warehouseCode, String lotNo, Double quantity,
			String brandCode, String loginUser) throws FormException {
		try {
			List<ImOnHand> lockedOnHands = getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				ImOnHand lockedOnHand = (ImOnHand) lockedOnHands.get(0);
				Double inUncommitQty = (lockedOnHand.getInUncommitQty() == null) ? 0D : lockedOnHand.getInUncommitQty();
				// lockedOnHand.setOtherUncommitQty(inUncommitQty - quantity);
				// 20090306 shan fix
				lockedOnHand.setInUncommitQty(inUncommitQty - quantity);
				lockedOnHand.setStockOnHandQty(lockedOnHand.getStockOnHandQty() + quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				throw new FormException("查無品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")的庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}
	/*
	 * 進貨單相對應之庫存
	 */
	public List getOnHandByImReceiveHead(String headId, String organizationCode) {
		final String finalHeadId = headId;
		final String finalOrganizationCode = organizationCode;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select o,i from ImOnHand as o,ImReceiveItem as i,ImReceiveHead as h \n");
				hql.append("where h.headId = :headId \n")
				   .append("and h.headId = i.imReceiveHead.headId \n")
				   .append("and o.brandCode = h.brandCode \n")
				   .append("and o.id.lotNo = h.lotNo \n")
				   .append("and o.id.warehouseCode = h.defaultWarehouseCode \n")
				   .append("and o.id.itemCode = i.itemCode \n")
				   .append("and o.outUncommitQty = 0 \n")
				   .append("and o.moveUncommitQty = 0 \n")
				   .append("and o.otherUncommitQty = 0 \n")
				   .append("and o.id.organizationCode = :organizationCode order by o.id.itemCode");

				Query query = session.createQuery(hql.toString());
				query.setString("headId", finalHeadId);
				query.setString("organizationCode", finalOrganizationCode);
				return query.list();
			}
		});
		return result;
	}
	
	//=======================已下加brandCode============================
	

	
	/**
	 * 依據organizationCode、itemCode、warehouseCode、lotNo、brandCode 更新或存檔庫存
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @param brandCode
	 * @param quantity
	 * @param loginUser
	 * @throws FormException
	 */
	public void updateOtherUncommitQuantity(String organizationCode, String itemCode, String warehouseCode, String lotNo, String brandCode, Double quantity,
			String loginUser) throws FormException {
		ImOnHand lockedOnHand = null;
		try {
			List<ImOnHand> lockedOnHands = this.getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			log.info(" lockedOnHands.size() = " + lockedOnHands.size());
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				lockedOnHand = (ImOnHand) lockedOnHands.get(0);
				Double otherUncommitQty = (lockedOnHand.getOtherUncommitQty() == null) ? 0D : lockedOnHand.getOtherUncommitQty();
				lockedOnHand.setOtherUncommitQty(otherUncommitQty + quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				ImOnHandId id = new ImOnHandId();
				lockedOnHand = new ImOnHand();
				id.setOrganizationCode(organizationCode);
				id.setItemCode(itemCode);
				id.setWarehouseCode(warehouseCode);
				id.setLotNo(lotNo);
				lockedOnHand.setId(id);
				lockedOnHand.setBrandCode(brandCode);
				lockedOnHand.setStockOnHandQty(0D);
				lockedOnHand.setOutUncommitQty(0D);
				lockedOnHand.setInUncommitQty(0D);
				lockedOnHand.setOtherUncommitQty(quantity);
				lockedOnHand.setCreatedBy(loginUser);
				lockedOnHand.setCreationDate(new Date());
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				save(lockedOnHand);
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}
	
	//進貨單加庫存
	/**
	 * 依據organizationCode、itemCode、warehouseCode、lotNo、brandCode 更新或存檔庫存
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @param brandCode
	 * @param quantity
	 * @param loginUser
	 * @throws FormException
	 */
	public void updateInUncommitQuantity(String organizationCode, String itemCode, String warehouseCode, String lotNo, String brandCode, Double quantity,
			String loginUser) throws FormException {
		ImOnHand lockedOnHand = null;
		try {
			List<ImOnHand> lockedOnHands = this.getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			log.info(" lockedOnHands.size() = " + lockedOnHands.size());
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				lockedOnHand = (ImOnHand) lockedOnHands.get(0);
				Double inUncommitQty = NumberUtils.getDouble(lockedOnHand.getInUncommitQty());
				lockedOnHand.setInUncommitQty(inUncommitQty + quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				ImOnHandId id = new ImOnHandId();
				id.setOrganizationCode(organizationCode);
				id.setItemCode(itemCode);
				id.setLotNo(lotNo); // 設定批號
				id.setWarehouseCode(warehouseCode);
				lockedOnHand = new ImOnHand();
				lockedOnHand.setId(id);
		    	lockedOnHand.setBrandCode(brandCode);
		    	lockedOnHand.setStockOnHandQty(0D);
		    	lockedOnHand.setOutUncommitQty(0D);
		    	lockedOnHand.setMoveUncommitQty(0D);
		    	lockedOnHand.setOtherUncommitQty(0D);
		    	lockedOnHand.setInUncommitQty(quantity);
		    	lockedOnHand.setCreationDate(new Date());
		    	lockedOnHand.setCreatedBy(loginUser);
		    	lockedOnHand.setLastUpdateDate(new Date());
		    	lockedOnHand.setLastUpdatedBy(loginUser);
		    	lockedOnHand.setBrandCode(brandCode);
		    	save(lockedOnHand);
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}
	
	/**
	 * 依據organizationCode、brandCode、itemCode、warehouseCode、lotNo查詢出目前可用庫存
	 * @param organizationCode
	 * @param brandCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @return
	 */
	public Double getCurrentStockOnHandQuantity(String organizationCode, String brandCode, String itemCode, String warehouseCode, String lotNo) {
		StringBuffer hql = new StringBuffer("select sum(nvl(model.stockOnHandQty,0) - nvl(model.outUncommitQty,0) + "
				+ "nvl(model.inUncommitQty,0) + nvl(model.moveUncommitQty,0) + nvl(model.otherUncommitQty,0)) from ImOnHand as model");
		hql.append(" where model.id.organizationCode = ?");
		hql.append(" and model.brandCode = ?");
		hql.append(" and model.id.itemCode = ?");
		hql.append(" and model.id.warehouseCode = ?");
		hql.append(" and model.id.lotNo = ?");
		hql.append(" group by model.id.organizationCode, model.brandCode, model.id.itemCode, model.id.warehouseCode, model.id.lotNo");
		log.info( "HQL = " + hql.toString());
		List result = getHibernateTemplate().find(hql.toString(), new Object[] { organizationCode, brandCode, itemCode, warehouseCode, lotNo });
		log.info( "result.size() = " + result.size());
		log.info(organizationCode + "/" + brandCode + "/" + itemCode + "/" + warehouseCode + "/" + lotNo);

		return (result.size() > 0 ? (Double) result.get(0) : null);
	}
	
	/**
	 * 銷貨單扣庫存
	 * 
	 * @param organizationCode
	 * @param brandCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @param quantity
	 * @param loginUser
	 * @throws FormException
	 */
	public void updateOutUncommitQty(String organizationCode, String brandCode, String itemCode, String warehouseCode, String lotNo, 
		Double quantity, String allowMinusStock, String opUser) throws FormException {
	        try {
	                List<ImOnHand> imOnHands = getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
	                if(imOnHands != null && imOnHands.size() > 0){
		                ImOnHand imOnHand = imOnHands.get(0);
				Double availableQuantity = imOnHand.getStockOnHandQty() - imOnHand.getOutUncommitQty() + imOnHand.getInUncommitQty() + imOnHand.getMoveUncommitQty() + 
				        imOnHand.getOtherUncommitQty();
				if(quantity > availableQuantity && "N".equals(allowMinusStock)){
				        throw new ValidationErrorException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")的可用庫存量不足(" + quantity + ")！");			    
				}else{
				        imOnHand.setOutUncommitQty(imOnHand.getOutUncommitQty() + quantity);
				        imOnHand.setLastUpdatedBy(opUser);
				        imOnHand.setLastUpdateDate(new Date());
				        update(imOnHand);
				}			
			}else{
		                throw new NoSuchObjectException("查無品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")的庫別庫存資料！");
		        }		
	        } catch (CannotAcquireLockException cale) {
		        throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
	        }
    }
	
	/**
	 * 
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @param quantity
	 * @param loginUser
	 * @throws FormException
	 */
	public void updateMoveUncommitQuantity(String organizationCode, String itemCode, String warehouseCode, String lotNo, Double quantity,
			String loginUser, String brandCode) throws FormException {
		log.info("updateMoveUncommitQuantity....");
		try {
			List<ImOnHand> lockedOnHands = this.getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			ImOnHand lockedOnHand = null;
			if (lockedOnHands != null && lockedOnHands.size() > 0 ) {
				log.info("item had been locked");
				if (quantity < 0) {
					Double availableQuantity = this.getCurrentStockOnHandQty(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
					if (availableQuantity == null) {
						throw new NoSuchObjectException("更新庫存時，查無品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")的庫存量！");
					} else if (quantity + availableQuantity <0) {
						throw new InsufficientQuantityException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")可用庫存量不足！");
					}
				}
				lockedOnHand = lockedOnHands.get(0);
				Double movementUncommitQty = lockedOnHand.getMoveUncommitQty() == null ? 0D : lockedOnHand.getMoveUncommitQty();
				lockedOnHand.setMoveUncommitQty(movementUncommitQty + quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				log.info("item not found in imOnHand");
				if (quantity > 0) {
					ImOnHandId id = new ImOnHandId();
					lockedOnHand = new ImOnHand();
					id.setOrganizationCode(organizationCode);
					id.setItemCode(itemCode);
					id.setWarehouseCode(warehouseCode);
					id.setLotNo(lotNo);
					lockedOnHand.setId(id);
					lockedOnHand.setBrandCode(brandCode);
					lockedOnHand.setStockOnHandQty(0D);
					lockedOnHand.setOutUncommitQty(0D);
					lockedOnHand.setInUncommitQty(0D);
					lockedOnHand.setMoveUncommitQty(quantity);
					lockedOnHand.setOtherUncommitQty(0D);
					lockedOnHand.setCreatedBy(loginUser);
					lockedOnHand.setCreationDate(new Date());
					lockedOnHand.setLastUpdatedBy(loginUser);
					lockedOnHand.setLastUpdateDate(new Date());
					save(lockedOnHand);
				} else {
					throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")目前並無庫存數量，請確認！");
				}
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		} catch (Exception ex){
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")更新時，發生錯誤，請通知系統管理員，原因:" + ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @param quantity
	 * @param loginUser
	 * @throws FormException
	 */
	public void updateMoveUncommitQuantityAllowMinus(String organizationCode, String itemCode, String warehouseCode, String lotNo,
			Double quantity, String loginUser, String brandCode) throws FormException {
		log.info("updateMoveUncommitQuantityAllowMinus....");
		try {
			System.out.println("update moveOnHand:" + brandCode + "/" + itemCode + "/" + warehouseCode + "/" + lotNo + "/" + quantity + "/"
					+ loginUser + "/" +brandCode);

			List<ImOnHand> lockedOnHands = this.getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			System.out.println(quantity);
			ImOnHand lockedOnHand = null;
			Double movementUncommitQty = new Double(0);
			if (lockedOnHands != null && lockedOnHands.size() > 0) {

				lockedOnHand = lockedOnHands.get(0);
				movementUncommitQty = lockedOnHand.getMoveUncommitQty() == null ? 0D : lockedOnHand.getMoveUncommitQty()+ quantity;
				lockedOnHand.setMoveUncommitQty(movementUncommitQty );
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				movementUncommitQty = quantity == null ? 0D : quantity;
				ImOnHandId id = new ImOnHandId();
				lockedOnHand = new ImOnHand();
				id.setOrganizationCode(organizationCode);
				id.setItemCode(itemCode);
				id.setWarehouseCode(warehouseCode);
				id.setLotNo(lotNo);
				lockedOnHand.setId(id);
				lockedOnHand.setBrandCode(brandCode);
				lockedOnHand.setStockOnHandQty(0D);
				lockedOnHand.setOutUncommitQty(0D);
				lockedOnHand.setInUncommitQty(0D);
				lockedOnHand.setMoveUncommitQty(movementUncommitQty);
				lockedOnHand.setOtherUncommitQty(0D);
				lockedOnHand.setCreatedBy(loginUser);
				lockedOnHand.setCreationDate(new Date());
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				save(lockedOnHand);
			}

		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}

	
	public void updateMoveOnHand(String organizationCode, String itemCode, String warehouseCode, String lotNo, Double quantity,
			String loginUser, String brandCode) throws FormException {
		List<ImOnHand> lockedOnHands = this.getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
		ImOnHand lockedOnHand = null;
		if (lockedOnHands != null && lockedOnHands.size() > 0) {
			lockedOnHand = lockedOnHands.get(0);
			Double movementUncommitQty = lockedOnHand.getMoveUncommitQty() == null ? 0D : lockedOnHand.getMoveUncommitQty();
			lockedOnHand.setMoveUncommitQty(movementUncommitQty - quantity);
			lockedOnHand.setStockOnHandQty(lockedOnHand.getStockOnHandQty() + quantity);
			lockedOnHand.setLastUpdatedBy(loginUser);
			lockedOnHand.setLastUpdateDate(new Date());
			update(lockedOnHand);
		} else {
			throw new FormException("組織(" + organizationCode + ")、品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo
					+ ")目前並無庫存數量，請確認！");
		}

	}
	
	/**
	 * 依據organizationCode、itemCode、warehouseCode、lotNo查詢出目前可用庫存
	 * 
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @return Double
	 */
	public Double getCurrentStockOnHandQty(String organizationCode, String itemCode, String warehouseCode, String lotNo, String brandCode) {
		StringBuffer hql = new StringBuffer("select sum(nvl(model.stockOnHandQty,0) - nvl(model.outUncommitQty,0) + "
				+ "nvl(model.inUncommitQty,0) + nvl(model.moveUncommitQty,0) + nvl(model.otherUncommitQty,0)) from ImOnHand as model");
		hql.append(" where model.id.organizationCode = '" + organizationCode + "'");
		if(StringUtils.hasText(itemCode))
			hql.append(" and model.id.itemCode = '" +itemCode+ "'");
		if(StringUtils.hasText(warehouseCode))
			hql.append(" and model.id.warehouseCode = '" +warehouseCode+ "'");
		if(StringUtils.hasText(lotNo))
			hql.append(" and model.id.lotNo = '" +lotNo+ "'");
		if(StringUtils.hasText(brandCode))
			hql.append(" and model.brandCode = '" +brandCode+ "'");
		hql.append(" group by model.id.organizationCode, model.id.itemCode, model.id.warehouseCode, model.id.lotNo");
		List result = getHibernateTemplate().find(hql.toString());
		return (result.size() > 0 ? (Double) result.get(0) : null);
	}
	//20160418 盤點機匯入調撥確認庫存
	public Double getCurrentStockOnHandQty1(String organizationCode,String brandCode ,String itemCode ,String warehouseCode ) {
		StringBuffer hql = new StringBuffer("select sum(nvl(model.stockOnHandQty,0) - nvl(model.outUncommitQty,0) + "
				+ "nvl(model.inUncommitQty,0) + nvl(model.moveUncommitQty,0) + nvl(model.otherUncommitQty,0)) from ImOnHand as model");
		hql.append(" where model.id.organizationCode = '" + organizationCode + "'");
		if(StringUtils.hasText(itemCode))
			hql.append(" and model.id.itemCode = '" +itemCode+ "'");
		if(StringUtils.hasText(warehouseCode))
			hql.append(" and model.id.warehouseCode = '" +warehouseCode+ "'");
		if(StringUtils.hasText(brandCode))
			hql.append(" and model.brandCode = '" +brandCode+ "'");
		hql.append(" group by model.id.organizationCode, model.id.itemCode, model.id.warehouseCode, model.id.lotNo");
		List result = getHibernateTemplate().find(hql.toString());
		return (result.size() > 0 ? (Double) result.get(0) : null);
	}
	
  /**
	 * 依據organizationCode和brandCode,itemCode查詢出目前可用庫存
	 * @param organizationCode
	 * @param brandCode
	 * @param itemCode
	 * @return
	 */
	public Double getCurrentStockOnHandQuantity(String organizationCode, String brandCode, String itemCode) {
		StringBuffer hql = new StringBuffer("select sum(model.stockOnHandQty - model.outUncommitQty + "
				+ "model.inUncommitQty + model.moveUncommitQty + model.otherUncommitQty) from ImOnHand as model");
		hql.append(" where model.id.itemCode = ?");
		hql.append(" and model.brandCode = ?");
		hql.append(" and model.id.organizationCode = ?");
		hql.append(" group by model.id.organizationCode, model.brandCode, model.id.itemCode");
		Object[] obj = new Object[] { itemCode, brandCode, organizationCode };
		
		List result = getHibernateTemplate().find(hql.toString(), obj);

		return (result.size() > 0 ? (Double) result.get(0) : null);
	}
	
	public String getBrandCode(String warehouseCodes) {
		log.info("getBrandCode");
	    String brandCode = null;
	    ImWarehouse imWarehouse = imWarehouseDAO.findById(warehouseCodes);
	    
	    if(imWarehouse != null){
	    	brandCode = imWarehouse.getBrandCode();
	    }else{
	    	brandCode = "";
	    }
	    log.info("取得的brandCode:"+brandCode);
	    
	    return brandCode;
	}
	
	public ImOnHand findByIdentification(String organizationCode, String brandCode, String itemCode, 
			String warehouseCode, String lotNo) {
		StringBuffer hql = new StringBuffer("from ImOnHand as model");
		hql.append(" where model.id.organizationCode = ?");
		hql.append(" and model.id.itemCode = ?");
		hql.append(" and model.id.warehouseCode = ?");
		hql.append(" and model.id.lotNo = ?");
		hql.append(" and model.brandCode = ?");

		List result = getHibernateTemplate().find(hql.toString(), new Object[]{organizationCode, itemCode, warehouseCode, lotNo, brandCode});		

		return (result != null && result.size() > 0 ? (ImOnHand) result.get(0) : null);		
	}

	/**
	 * 日結時使用，更新可用庫存(onHandQuantity)及銷貨未結庫存欄位(outUncommitQty)
	 * @param declarationNo
	 * @param declarationSeq
	 * @param customsItemCode
	 * @param customsWarehouseCode
	 * @param brandCode
	 * @param quantity
	 * @param opUser
	 * @throws FormException
	 */
	public void updateOutOnHand(String organizationCode, String brandCode, String itemCode, String warehouseCode, String lotNo, 
			Double quantity, String loginUser) throws FormException {
		try {
			List<ImOnHand> lockedOnHands = getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				ImOnHand lockedOnHand = (ImOnHand) lockedOnHands.get(0);
				Double outUncommitQty = (lockedOnHand.getOutUncommitQty() == null) ? 0D : lockedOnHand.getOutUncommitQty();
				lockedOnHand.setOutUncommitQty(outUncommitQty - quantity);
				lockedOnHand.setStockOnHandQty(lockedOnHand.getStockOnHandQty() - quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				throw new FormException("查無品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")的庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}
	
	/**
	 * 反確認時使用，更新可用庫存(onHandQuantity)
	 * @param declarationNo
	 * @param declarationSeq
	 * @param customsItemCode
	 * @param customsWarehouseCode
	 * @param brandCode
	 * @param quantity
	 * @param opUser
	 * @throws FormException
	 */
	public void updateStockOnHand(String organizationCode, String brandCode, String itemCode, String warehouseCode, String lotNo, 
			Double quantity,String loginUser) throws FormException {
		try {
			List<ImOnHand> lockedOnHands = getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				ImOnHand lockedOnHand = (ImOnHand) lockedOnHands.get(0);
				lockedOnHand.setStockOnHandQty(NumberUtils.getDouble(lockedOnHand.getStockOnHandQty()) + quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				throw new FormException("查無品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")的庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}
	
	public void updateOtherOnHand(String organizationCode, String itemCode, String warehouseCode, String lotNo, Double quantity,
			String loginUser, String brandCode) throws FormException {
		try {

			List<ImOnHand> lockedOnHands = getLockedOnHand(organizationCode, itemCode, warehouseCode, lotNo, brandCode);
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				ImOnHand lockedOnHand = (ImOnHand) lockedOnHands.get(0);
				Double otherUncommitQty = (lockedOnHand.getOtherUncommitQty() == null) ? 0D : lockedOnHand.getOtherUncommitQty();
				lockedOnHand.setOtherUncommitQty(otherUncommitQty - quantity);
				lockedOnHand.setStockOnHandQty(lockedOnHand.getStockOnHandQty() + quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				update(lockedOnHand);
			} else {
				throw new FormException("查無品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")的庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號(" + itemCode + ")、庫別(" + warehouseCode + ")、批號(" + lotNo + ")已鎖定，請稍後再試！");
		}
	}
	
	
	/**
     * 依品牌、起迄日期，取得庫存清單
     * @param HashMap conditionMap
     * @return
     */
    public List findImOnHandByCondition(HashMap conditionMap) {
		final String brandCode = (String) conditionMap.get("brandCode");
		final String itemCode = (String) conditionMap.get("itemCode");
		final String dataDate = (String) conditionMap.get("dataDate");
		final String dataDateEnd = (String) conditionMap.get("dataDateEnd");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select model from ImOnHand as model where 1=1 ");
				hql.append(" and model.brandCode  = :brandCode ");
				if(StringUtils.hasText(itemCode))
					hql.append(" and model.itemCode >= :itemCode ");
				if(StringUtils.hasText(dataDate))
					hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') >= :dataDate ");
				if(StringUtils.hasText(dataDateEnd))
					hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') <= :dataDateEnd ");
				log.info("findImOnHandByCondition hql = " + hql.toString());
				Query query = session.createQuery(hql.toString());
				query.setString("brandCode", brandCode);
				if(StringUtils.hasText(itemCode))
					query.setString("itemCode", itemCode);
				if(StringUtils.hasText(dataDate))
					query.setString("dataDate", dataDate);
				if(StringUtils.hasText(dataDateEnd))
					query.setString("dataDateEnd", dataDateEnd);
				return query.list();
			}
		});
		return result;
	}
}