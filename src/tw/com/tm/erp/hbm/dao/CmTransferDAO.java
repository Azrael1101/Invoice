package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.hbm.bean.CmTransfer;

/**
 * A data access object (DAO) providing persistence and search support for
 * CmTransfer entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 *
 * @see tw.com.tm.erp.hbm.bean.CmTransfer
 * @author MyEclipse Persistence Tools
 */

public class CmTransferDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(CmTransferDAO.class);
	// property constants
	public static final String TRANSFER = "transfer";
	public static final String START_STATION = "startStation";
	public static final String TO_STATION = "toStation";
	public static final String OWNER = "owner";
	public static final String LEAVE_BOX = "leaveBox";
	public static final String LEAVE_QUANTITY = "leaveQuantity";
	public static final String TRUCK_BOX = "truckBox";
	public static final String TRUCK_QUANTITY = "truckQuantity";
	public static final String ORDER_NO = "orderNo";
	public static final String SEAL_NO = "sealNo";
	public static final String VEHICLE_STATION = "vehicleStation";
	public static final String VEHICLE_NO = "vehicleNo";
	public static final String DRIVER_CODE = "driverCode";
	public static final String DRIVER_LICENCE = "driverLicence";
	public static final String CAR_NO = "carNo";
	public static final String TRACK = "track";

	protected void initDao() {
		// do nothing
	}

	public void save(CmTransfer transientInstance) {
		log.debug("saving CmTransfer instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CmTransfer persistentInstance) {
		log.debug("deleting CmTransfer instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CmTransfer findById(java.lang.String id) {
		log.debug("getting CmTransfer instance with id: " + id);
		try {
			CmTransfer instance = (CmTransfer) getHibernateTemplate().get("tw.com.tm.erp.hbm.bean.CmTransfer", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CmTransfer instance) {
		log.debug("finding CmTransfer instance by example");
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
		log.debug("finding CmTransfer instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from CmTransfer as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByTransfer(Object transfer) {
		return findByProperty(TRANSFER, transfer);
	}

	public List findByStartStation(Object startStation) {
		return findByProperty(START_STATION, startStation);
	}

	public List findByToStation(Object toStation) {
		return findByProperty(TO_STATION, toStation);
	}

	public List findByOwner(Object owner) {
		return findByProperty(OWNER, owner);
	}

	public List findByLeaveBox(Object leaveBox) {
		return findByProperty(LEAVE_BOX, leaveBox);
	}

	public List findByLeaveQuantity(Object leaveQuantity) {
		return findByProperty(LEAVE_QUANTITY, leaveQuantity);
	}

	public List findByTruckBox(Object truckBox) {
		return findByProperty(TRUCK_BOX, truckBox);
	}

	public List findByTruckQuantity(Object truckQuantity) {
		return findByProperty(TRUCK_QUANTITY, truckQuantity);
	}

	public List findByOrderNo(Object orderNo) {
		return findByProperty(ORDER_NO, orderNo);
	}

	public List findBySealNo(Object sealNo) {
		return findByProperty(SEAL_NO, sealNo);
	}

	public List findByVehicleStation(Object vehicleStation) {
		return findByProperty(VEHICLE_STATION, vehicleStation);
	}

	public List findByVehicleNo(Object vehicleNo) {
		return findByProperty(VEHICLE_NO, vehicleNo);
	}

	public List findByDriverCode(Object driverCode) {
		return findByProperty(DRIVER_CODE, driverCode);
	}

	public List findByDriverLicence(Object driverLicence) {
		return findByProperty(DRIVER_LICENCE, driverLicence);
	}

	public List findByCarNo(Object carNo) {
		return findByProperty(CAR_NO, carNo);
	}

	public List findByTrack(Object track) {
		return findByProperty(TRACK, track);
	}

	public List findAll() {
		log.debug("finding all CmTransfer instances");
		try {
			String queryString = "from CmTransfer";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CmTransfer merge(CmTransfer detachedInstance) {
		log.debug("merging CmTransfer instance");
		try {
			CmTransfer result = (CmTransfer) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CmTransfer instance) {
		log.debug("attaching dirty CmTransfer instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CmTransfer instance) {
		log.debug("attaching clean CmTransfer instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static CmTransferDAO getFromApplicationContext(ApplicationContext ctx) {
		return (CmTransferDAO) ctx.getBean("CmTransferDAO");
	}
}