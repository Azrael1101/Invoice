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
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.InsufficientQuantityException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.hbm.bean.ImStorageOnHand;
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

public class ImStorageOnHandDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(ImStorageOnHandDAO.class);

    /**
     * 查詢並鎖定儲位庫存
     */
    public List<ImStorageOnHand> getLockedOnHand(final String organizationCode, final String brandCode, final String itemCode, 
	    final String warehouseCode, final String storageCode , final String storageInNo, final String storageLotNo) throws Exception {
	List<ImStorageOnHand> result = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		StringBuffer hql = new StringBuffer("from ImStorageOnHand as model");
		hql.append(" where model.organizationCode = :organizationCode");
		hql.append(" and model.brandCode = :brandCode");
		//if(StringUtils.hasText(itemCode))
		hql.append(" and model.itemCode = :itemCode");
		//if(StringUtils.hasText(warehouseCode))
		hql.append(" and model.warehouseCode = :warehouseCode");
		//if(StringUtils.hasText(storageCode))
		hql.append(" and model.storageCode = :storageCode");
		//if(StringUtils.hasText(storageInNo))
		hql.append(" and model.storageInNo = :storageInNo");
		//if(StringUtils.hasText(storageLotNo))
		hql.append(" and model.storageLotNo = :storageLotNo");

		Query query = session.createQuery(hql.toString());
		query.setLockMode("model", LockMode.UPGRADE_NOWAIT);
		query.setString("organizationCode", organizationCode);
		query.setString("brandCode", brandCode);
		//if(StringUtils.hasText(itemCode))
		query.setString("itemCode", itemCode);
		//if(StringUtils.hasText(warehouseCode))
		query.setString("warehouseCode", warehouseCode);
		//if(StringUtils.hasText(storageCode))
		query.setString("storageCode", storageCode);
		//if(StringUtils.hasText(storageInNo))
		query.setString("storageInNo", storageInNo);
		//if(StringUtils.hasText(storageLotNo))
		query.setString("storageLotNo", storageLotNo);
		return query.list();
	    }
	});
	return result;
    }

    /**
     * 依據 organizationCode, brandCode, itemCode, warehouseCode, storageCode, lotNo查詢儲位庫存
     */
    public Double getCurrentOnHandQty(String organizationCode, String brandCode, String itemCode, 
	    String warehouseCode, String storageCode, String storageInNo, String storageLotNo) throws Exception {
	log.info("getCurrentOnHandQty " + organizationCode + " " + brandCode + " " + itemCode + " " + warehouseCode + " " + storageCode + " " + storageInNo + " " + storageLotNo);
	StringBuffer hql = new StringBuffer(" select ");
	hql.append(" sum( 	nvl(model.stockOnHandQty,0) - nvl(model.outUncommitQty,0) + ");
	hql.append(" 		nvl(model.inUncommitQty,0) + nvl(model.moveUncommitQty,0) + ");
	hql.append(" 		nvl(model.otherUncommitQty,0) - nvl(model.blockQty,0) ");
	hql.append("    ) from ImStorageOnHand as model ");
	hql.append(" where model.organizationCode = '" + organizationCode + "'");
	hql.append(" and model.brandCode = '" + brandCode + "'");
	//if(StringUtils.hasText(itemCode))
	hql.append(" and model.itemCode = '"+itemCode+"'");
	//if(StringUtils.hasText(warehouseCode))
	hql.append(" and model.warehouseCode = '"+warehouseCode+"'");
	//if(StringUtils.hasText(storageCode))
	hql.append(" and model.storageCode = '"+storageCode+"'");
	//if(StringUtils.hasText(storageInNo))
	hql.append(" and model.storageInNo = '"+storageInNo+"'");
	//if(StringUtils.hasText(storageLotNo))
	hql.append(" and model.storageLotNo = '"+storageLotNo+"'");

	hql.append(" group by model.organizationCode, model.brandCode");

	//if(StringUtils.hasText(itemCode))
	hql.append(", model.itemCode");
	//if(StringUtils.hasText(warehouseCode))
	hql.append(", model.warehouseCode");
	//if(StringUtils.hasText(storageCode))
	hql.append(", model.storageCode");
	//if(StringUtils.hasText(storageInNo))
	hql.append(", model.storageInNo");
	//if(StringUtils.hasText(storageLotNo))
	hql.append(", model.storageLotNo");
	log.info("hql.toString() = " + hql.toString());
	List result = getHibernateTemplate().find(hql.toString());
	return (result != null && result.size() > 0 ? (Double) result.get(0) : null);
    }

    /**
     * 依據 ImStorageOnHand取得儲位庫存
     */
    public Double getCurrentOnHandQty(ImStorageOnHand imStorageOnHand) throws Exception {
	if(null != imStorageOnHand)
	    return NumberUtils.getDouble(imStorageOnHand.getStockOnHandQty())
	    + NumberUtils.getDouble(imStorageOnHand.getInUncommitQty())
	    + NumberUtils.getDouble(imStorageOnHand.getMoveUncommitQty())
	    - NumberUtils.getDouble(imStorageOnHand.getOutUncommitQty())
	    + NumberUtils.getDouble(imStorageOnHand.getOtherUncommitQty())
	    - NumberUtils.getDouble(imStorageOnHand.getBlockQty());
	else
	    return null;

    }

    /**
     * 更新OUT未結
     */
    public void updateOutUncommitQuantity(String organizationCode, String brandCode, String itemCode, String warehouseCode, 
	    String storageCode, String storageInNo, String storageLotNo, Double quantity, String loginUser, boolean allowMinus) throws Exception {
	try {
	    log.info("updateOutUncommitQuantity");
	    List<ImStorageOnHand> lockedOnHands = getLockedOnHand(organizationCode, brandCode, itemCode, warehouseCode, 
		    storageCode, storageInNo, storageLotNo);
	    ImStorageOnHand lockedOnHand = null;
	    if (null != lockedOnHands && lockedOnHands.size() > 0) {

		log.info("lockedOnHands.size = " + lockedOnHands.size());

		lockedOnHand = lockedOnHands.get(0);

		//如果不允許負庫存
		if(!allowMinus){
		    //if (quantity > 0) {
		    Double availableQuantity = getCurrentOnHandQty(lockedOnHand);
		    //log.info("availableQuantity:"+ availableQuantity);
		    if (null == availableQuantity) {
			throw new NoSuchObjectException("更新庫存時，查無品號：" + itemCode + "，庫別："+warehouseCode+"，" +
				"儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"的可用儲位量！");
		    } else if ((quantity * -1) + availableQuantity < 0) {
			throw new InsufficientQuantityException("品號：" + itemCode + "，庫別："+warehouseCode+"，" +
				"儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"可用儲位量不足！");
		    }
		    //}
		}

		Double outUncommitQty = NumberUtils.getDouble(lockedOnHand.getOutUncommitQty());
		lockedOnHand.setOutUncommitQty(outUncommitQty + quantity);
		lockedOnHand.setLastUpdatedBy(loginUser);
		lockedOnHand.setLastUpdateDate(new Date());
		if(isDeleteOnHand(lockedOnHand))
		    delete(lockedOnHand);
		else
		    update(lockedOnHand);
	    } else {
		//log.info("insert");
		if (quantity < 0) {
		    lockedOnHand = new ImStorageOnHand();
		    lockedOnHand.setOrganizationCode(organizationCode);
		    lockedOnHand.setBrandCode(brandCode);
		    lockedOnHand.setItemCode(itemCode);
		    lockedOnHand.setWarehouseCode(warehouseCode);
		    lockedOnHand.setStorageCode(storageCode);
		    lockedOnHand.setStorageInNo(storageInNo);
		    lockedOnHand.setStorageLotNo(storageLotNo);
		    lockedOnHand.setStockOnHandQty(0D);
		    lockedOnHand.setOutUncommitQty(quantity);
		    lockedOnHand.setInUncommitQty(0D);
		    lockedOnHand.setMoveUncommitQty(0D);
		    lockedOnHand.setOtherUncommitQty(0D);
		    lockedOnHand.setBlockQty(0D);
		    lockedOnHand.setTempQty(0D);
		    lockedOnHand.setCreatedBy(loginUser);
		    lockedOnHand.setCreationDate(new Date());
		    lockedOnHand.setLastUpdatedBy(loginUser);
		    lockedOnHand.setLastUpdateDate(new Date());
		    save(lockedOnHand);
		} else if(!allowMinus){
		    throw new Exception("品號：" + itemCode + "，庫別："+warehouseCode+"，" +
			    "儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"目前並無儲位量，請確認！");
		}
	    }
	} catch (CannotAcquireLockException cale) {
	    throw new Exception("品號：" + itemCode + "已鎖定，請稍後再試！");
	}
    }


    /**
     * 更新IN未結
     */
    public void updateInUncommitQuantity(String organizationCode, String brandCode, String itemCode, String warehouseCode, 
	    String storageCode, String storageInNo, String storageLotNo, Double quantity, String loginUser, boolean allowMinus) throws Exception {
	try {
	    log.info("updateInUncommitQuantity");
	    List<ImStorageOnHand> lockedOnHands = getLockedOnHand(organizationCode, brandCode, 
		    itemCode, warehouseCode, storageCode, storageInNo, storageLotNo);
	    ImStorageOnHand lockedOnHand = null;
	    if (null != lockedOnHands && lockedOnHands.size() > 0) {

		log.info("lockedOnHands.size = " + lockedOnHands.size());

		lockedOnHand = lockedOnHands.get(0);

		//如果不允許負庫存
		if(!allowMinus){
		    //if (quantity < 0) {
		    Double availableQuantity = getCurrentOnHandQty(lockedOnHand);
		    //log.info("availableQuantity:"+ availableQuantity);
		    if (null == availableQuantity) {
			throw new NoSuchObjectException("更新庫存時，查無品號：" + itemCode + "，庫別："+warehouseCode+"，" +
				"儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"的可用儲位量！");
		    } else if (quantity + availableQuantity < 0) {
			throw new InsufficientQuantityException("品號：" + itemCode + "，庫別："+warehouseCode+"，" +
				"儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"可用儲位量不足！");
		    }
		    //}
		}

		Double inUncommitQty = NumberUtils.getDouble(lockedOnHand.getInUncommitQty());
		lockedOnHand.setInUncommitQty(inUncommitQty + quantity);
		lockedOnHand.setLastUpdatedBy(loginUser);
		lockedOnHand.setLastUpdateDate(new Date());
		if(isDeleteOnHand(lockedOnHand))
		    delete(lockedOnHand);
		else
		    update(lockedOnHand);
	    } else {
		//log.info("insert");
		if (quantity > 0) {
		    lockedOnHand = new ImStorageOnHand();
		    lockedOnHand.setOrganizationCode(organizationCode);
		    lockedOnHand.setBrandCode(brandCode);
		    lockedOnHand.setItemCode(itemCode);
		    lockedOnHand.setWarehouseCode(warehouseCode);
		    lockedOnHand.setStorageCode(storageCode);
		    lockedOnHand.setStorageInNo(storageInNo);
		    lockedOnHand.setStorageLotNo(storageLotNo);
		    lockedOnHand.setStockOnHandQty(0D);
		    lockedOnHand.setOutUncommitQty(0D);
		    lockedOnHand.setInUncommitQty(quantity);
		    lockedOnHand.setMoveUncommitQty(0D);
		    lockedOnHand.setOtherUncommitQty(0D);
		    lockedOnHand.setBlockQty(0D);
		    lockedOnHand.setTempQty(0D);
		    lockedOnHand.setCreatedBy(loginUser);
		    lockedOnHand.setCreationDate(new Date());
		    lockedOnHand.setLastUpdatedBy(loginUser);
		    lockedOnHand.setLastUpdateDate(new Date());
		    save(lockedOnHand);
		} else if(!allowMinus){
		    throw new Exception("品號：" + itemCode + "，庫別："+warehouseCode+"，" +
			    "儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"目前並無儲位量，請確認！");
		}
	    }
	} catch (CannotAcquireLockException cale) {
	    throw new Exception("品號：" + itemCode + "已鎖定，請稍後再試！");
	}
    }

    /**
     * 更新MOVE未結
     */
    public void updateMoveUncommitQuantity(String organizationCode, String brandCode, String itemCode, String warehouseCode, 
	    String storageCode, String storageInNo, String storageLotNo, Double quantity, String loginUser, boolean allowMinus) throws Exception {
	try {
	    log.info("updateMoveUncommitQuantity");
	    List<ImStorageOnHand> lockedOnHands = getLockedOnHand(organizationCode, brandCode, itemCode, warehouseCode, 
		    storageCode, storageInNo, storageLotNo);
	    ImStorageOnHand lockedOnHand = null;
	    if (null != lockedOnHands && lockedOnHands.size() > 0) {

		log.info("lockedOnHands.size = " + lockedOnHands.size());

		lockedOnHand = lockedOnHands.get(0);

		//如果不允許負庫存
		if(!allowMinus){
		    //if (quantity < 0) {
		    Double availableQuantity = getCurrentOnHandQty(lockedOnHand);
		    //log.info("availableQuantity:"+ availableQuantity);
		    if (null == availableQuantity) {
			throw new NoSuchObjectException("更新庫存時，查無品號：" + itemCode + "，庫別："+warehouseCode+"，" +
				"儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"的可用儲位量！");
		    } else if (quantity + availableQuantity < 0) {
			throw new InsufficientQuantityException("品號：" + itemCode + "，庫別："+warehouseCode+"，" +
				"儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"可用儲位量不足！");
		    }
		    //}
		}

		Double moveUncommitQty = NumberUtils.getDouble(lockedOnHand.getMoveUncommitQty());
		lockedOnHand.setMoveUncommitQty(moveUncommitQty + quantity);
		lockedOnHand.setLastUpdatedBy(loginUser);
		lockedOnHand.setLastUpdateDate(new Date());
		if(isDeleteOnHand(lockedOnHand))
		    delete(lockedOnHand);
		else
		    update(lockedOnHand);
	    } else {
		//log.info("insert");
		if (quantity > 0) {
		    lockedOnHand = new ImStorageOnHand();
		    lockedOnHand.setOrganizationCode(organizationCode);
		    lockedOnHand.setBrandCode(brandCode);
		    lockedOnHand.setItemCode(itemCode);
		    lockedOnHand.setWarehouseCode(warehouseCode);
		    lockedOnHand.setStorageCode(storageCode);
		    lockedOnHand.setStorageInNo(storageInNo);
		    lockedOnHand.setStorageLotNo(storageLotNo);
		    lockedOnHand.setStockOnHandQty(0D);
		    lockedOnHand.setOutUncommitQty(0D);
		    lockedOnHand.setInUncommitQty(0D);
		    lockedOnHand.setMoveUncommitQty(quantity);
		    lockedOnHand.setOtherUncommitQty(0D);
		    lockedOnHand.setBlockQty(0D);
		    lockedOnHand.setTempQty(0D);
		    lockedOnHand.setCreatedBy(loginUser);
		    lockedOnHand.setCreationDate(new Date());
		    lockedOnHand.setLastUpdatedBy(loginUser);
		    lockedOnHand.setLastUpdateDate(new Date());
		    save(lockedOnHand);
		} else if(!allowMinus){
		    throw new Exception("品號：" + itemCode + "，庫別："+warehouseCode+"，" +
			    "儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"目前並無儲位量，請確認！");
		}
	    }
	} catch (CannotAcquireLockException cale) {
	    throw new Exception("品號：" + itemCode + "已鎖定，請稍後再試！");
	}
    }

    /**
     * 更新OTHER未結
     */
    public void updateOtherUncommitQuantity(String organizationCode, String brandCode, String itemCode, String warehouseCode, 
	    String storageCode, String storageInNo, String storageLotNo, Double quantity, String loginUser, boolean allowMinus) throws Exception {
	try {
	    log.info("updateOtherUncommitQuantity");
	    List<ImStorageOnHand> lockedOnHands = getLockedOnHand(organizationCode, brandCode, itemCode, warehouseCode, 
		    storageCode, storageInNo, storageLotNo);
	    ImStorageOnHand lockedOnHand = null;
	    if (null != lockedOnHands && lockedOnHands.size() > 0) {

		log.info("lockedOnHands.size = " + lockedOnHands.size());

		lockedOnHand = lockedOnHands.get(0);

		//如果不允許負庫存
		if(!allowMinus){
		    //if (quantity < 0) {
		    Double availableQuantity = getCurrentOnHandQty(lockedOnHand);
		    //log.info("availableQuantity:"+ availableQuantity);
		    if (null == availableQuantity) {
			throw new NoSuchObjectException("更新庫存時，查無品號：" + itemCode + "，庫別："+warehouseCode+"，" +
				"儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"的可用儲位量！");
		    } else if (quantity + availableQuantity < 0) {
			throw new InsufficientQuantityException("品號：" + itemCode + "，庫別："+warehouseCode+"，" +
				"儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"可用儲位量不足！");
		    }
		    //}
		}

		Double otherUncommitQty = NumberUtils.getDouble(lockedOnHand.getOtherUncommitQty());
		lockedOnHand.setOtherUncommitQty(otherUncommitQty + quantity);
		lockedOnHand.setLastUpdatedBy(loginUser);
		lockedOnHand.setLastUpdateDate(new Date());
		if(isDeleteOnHand(lockedOnHand))
		    delete(lockedOnHand);
		else
		    update(lockedOnHand);
	    } else {
		//log.info("insert");
		if (quantity > 0) {
		    lockedOnHand = new ImStorageOnHand();
		    lockedOnHand.setOrganizationCode(organizationCode);
		    lockedOnHand.setBrandCode(brandCode);
		    lockedOnHand.setItemCode(itemCode);
		    lockedOnHand.setWarehouseCode(warehouseCode);
		    lockedOnHand.setStorageCode(storageCode);
		    lockedOnHand.setStorageInNo(storageInNo);
		    lockedOnHand.setStorageLotNo(storageLotNo);
		    lockedOnHand.setStockOnHandQty(0D);
		    lockedOnHand.setOutUncommitQty(0D);
		    lockedOnHand.setInUncommitQty(0D);
		    lockedOnHand.setMoveUncommitQty(0D);
		    lockedOnHand.setOtherUncommitQty(quantity);
		    lockedOnHand.setBlockQty(0D);
		    lockedOnHand.setTempQty(0D);
		    lockedOnHand.setCreatedBy(loginUser);
		    lockedOnHand.setCreationDate(new Date());
		    lockedOnHand.setLastUpdatedBy(loginUser);
		    lockedOnHand.setLastUpdateDate(new Date());
		    save(lockedOnHand);
		} else if(!allowMinus){
		    throw new Exception("品號：" + itemCode + "，庫別："+warehouseCode+"，" +
			    "儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"目前並無儲位量，請確認！");
		}
	    }
	} catch (CannotAcquireLockException cale) {
	    throw new Exception("品號：" + itemCode + "已鎖定，請稍後再試！");
	}
    }

    /**
     * 更新Block
     */
    public void updateBlockUncommitQuantity(String organizationCode, String brandCode, String itemCode, String warehouseCode, 
	    String storageCode, String storageInNo, String storageLotNo, Double quantity, String loginUser, boolean allowMinus) throws FormException, Exception {
	try {
	    log.info("updateBlockUncommitQuantity");
	    List<ImStorageOnHand> lockedOnHands = getLockedOnHand(organizationCode, brandCode, itemCode, warehouseCode, 
		    storageCode, storageInNo, storageLotNo);
	    ImStorageOnHand lockedOnHand = null;
	    if (null != lockedOnHands && lockedOnHands.size() > 0) {

		log.info("lockedOnHands.size = " + lockedOnHands.size());

		lockedOnHand = lockedOnHands.get(0);

		//如果不允許負庫存
		if(!allowMinus){
		    //if (quantity > 0) {
			Double availableQuantity = getCurrentOnHandQty(lockedOnHand);
			//log.info("availableQuantity:"+ availableQuantity);
			if (null == availableQuantity) {
			    throw new NoSuchObjectException("更新庫存時，查無品號：" + itemCode + "，庫別："+warehouseCode+"，" +
				    "儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"的可用儲位量！");
			} else if ((quantity * -1) + availableQuantity < 0) {
			    throw new InsufficientQuantityException("品號：" + itemCode + "，庫別："+warehouseCode+"，" +
				    "儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"可用儲位量不足！");
			}
		    //}
		}

		Double blockOnHandQty = NumberUtils.getDouble(lockedOnHand.getBlockQty());
		lockedOnHand.setBlockQty(blockOnHandQty + quantity);
		lockedOnHand.setLastUpdatedBy(loginUser);
		lockedOnHand.setLastUpdateDate(new Date());
		if(isDeleteOnHand(lockedOnHand))
		    delete(lockedOnHand);
		else
		    update(lockedOnHand);
	    } else {
		//log.info("insert");
		if (quantity < 0) {
		    lockedOnHand = new ImStorageOnHand();
		    lockedOnHand.setOrganizationCode(organizationCode);
		    lockedOnHand.setBrandCode(brandCode);
		    lockedOnHand.setItemCode(itemCode);
		    lockedOnHand.setWarehouseCode(warehouseCode);
		    lockedOnHand.setStorageCode(storageCode);
		    lockedOnHand.setStorageInNo(storageInNo);
		    lockedOnHand.setStorageLotNo(storageLotNo);
		    lockedOnHand.setStockOnHandQty(0D);
		    lockedOnHand.setOutUncommitQty(0D);
		    lockedOnHand.setInUncommitQty(0D);
		    lockedOnHand.setMoveUncommitQty(0D);
		    lockedOnHand.setOtherUncommitQty(0D);
		    lockedOnHand.setBlockQty(quantity);
		    lockedOnHand.setTempQty(0D);
		    lockedOnHand.setCreatedBy(loginUser);
		    lockedOnHand.setCreationDate(new Date());
		    lockedOnHand.setLastUpdatedBy(loginUser);
		    lockedOnHand.setLastUpdateDate(new Date());
		    save(lockedOnHand);
		} else if(!allowMinus){
		    throw new Exception("品號：" + itemCode + "，庫別："+warehouseCode+"，" +
			    "儲位："+storageCode+"，進貨日："+storageInNo+"，效期："+storageLotNo+"目前並無儲位量，請確認！");
		}
	    }
	} catch (CannotAcquireLockException cale) {
	    throw new Exception("品號：" + itemCode + "已鎖定，請稍後再試！");
	}
    }

    /**
     * 檢查ImStorageOnHand
     */
    public boolean isDeleteOnHand(ImStorageOnHand imStorageOnHand){
	boolean isDeleteOnHand = false;
	//Double quanaity = imStorageOnHand.getOutUncommitQty() + imStorageOnHand.getInUncommitQty() +  
	//imStorageOnHand.getMoveUncommitQty() + imStorageOnHand.getOtherUncommitQty();
	//if( 0 == quanaity && 0 == imStorageOnHand.getStockOnHandQty() && 0 == imStorageOnHand.getBlockQty() )
	//isDeleteOnHand = true;
	return isDeleteOnHand;
    }

}