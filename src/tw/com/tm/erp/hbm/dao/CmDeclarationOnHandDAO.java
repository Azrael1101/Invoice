package tw.com.tm.erp.hbm.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

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
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHand;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHandView;
import tw.com.tm.erp.hbm.bean.CmDeclarationView;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.CmDeclarationHeadService;
import tw.com.tm.erp.hbm.service.CmDeclarationItemService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
public class CmDeclarationOnHandDAO extends BaseDAO {
	
	
	private CmDeclarationHeadService cmDeclarationHeadService;
	private CmDeclarationViewDAO cmDeclarationViewDAO;
	
	public void setCmDeclarationHeadService(CmDeclarationHeadService cmDeclarationHeadService) {
		this.cmDeclarationHeadService = cmDeclarationHeadService;
	}
	
	public void setCmDeclarationViewDAO(CmDeclarationViewDAO cmDeclarationViewDAO) {
		this.cmDeclarationViewDAO = cmDeclarationViewDAO;
	}

    private static final Log log = LogFactory.getLog(CmDeclarationOnHandDAO.class);

    /**
     *依據brandCode、customsItemCode查詢出目前可用庫存數量
     *
     * @param brandCode
     * @param customsItemCode
     * @return Double
     */
    public Double getCurrentOnHandQtyByProperty(String brandCode, String customsItemCode) {

        StringBuffer hql = new StringBuffer("select sum(model.onHandQuantity - model.outUncommitQty + "
	        + "model.inUncommitQty + model.moveUncommitQty + model.otherUncommitQty) from CmDeclarationOnHand as model");
        hql.append(" where model.brandCode = ?");
	hql.append(" and model.customsItemCode = ?");
	hql.append(" group by model.brandCode, model.customsItemCode");
	List result = getHibernateTemplate().find(hql.toString(), new Object[]{brandCode, customsItemCode});
	return (result != null && result.size() > 0 ? (Double) result.get(0) : null);
    }

    /**
     *依據brandCode、customsItemCode, customsWarehouseCode查詢出目前可用庫存數量
     *
     * @param brandCode
     * @param customsItemCode
     * @param customsWarehouseCode
     * @return Double
     */
    public Double getCurrentOnHandQtyByProperty(String brandCode, String customsItemCode, String customsWarehouseCode) {

        StringBuffer hql = new StringBuffer("select sum(model.onHandQuantity - model.outUncommitQty + "
 	        + "model.inUncommitQty + model.moveUncommitQty + model.otherUncommitQty) from CmDeclarationOnHand as model");
        hql.append(" where model.brandCode = ?");
 	hql.append(" and model.customsItemCode = ?");
 	hql.append(" and model.customsWarehouseCode = ?");
 	hql.append(" group by model.brandCode, model.customsItemCode, model.customsWarehouseCode");
 	List result = getHibernateTemplate().find(hql.toString(), new Object[]{brandCode, customsItemCode, customsWarehouseCode});
        return (result != null && result.size() > 0 ? (Double) result.get(0) : null);
    }

    /**
     *依據brandCode、declarationNo、declarationSeq、customsItemCode、customsWarehouseCode查詢出目前可用庫存數量
     *
     * @param brandCode
     * @param declarationNo
     * @param declarationSeq
     * @param customsItemCode
     * @param customsWarehouseCode
     * @return Double
     */
    public Double getCurrentOnHandQtyByProperty(String brandCode, String declarationNo, Long declarationSeq, String customsItemCode,
	    String customsWarehouseCode) {

	Object[] obj = null;
        StringBuffer hql = new StringBuffer("select sum(nvl(model.onHandQuantity,0) - nvl(model.outUncommitQty,0) + "
 	        + "nvl(model.inUncommitQty,0) + nvl(model.moveUncommitQty,0) + nvl(model.otherUncommitQty,0)-nvl(model.blockOnHandQuantity,0)) from CmDeclarationOnHand as model");//20160429by jason新增nvl(model.blockOnHandQuantity,0)
        hql.append(" where model.brandCode = ?");
        hql.append(" and model.declarationNo = ?");
        hql.append(" and model.customsWarehouseCode = ?");
        if(declarationSeq != null){
            hql.append(" and model.declarationSeq = ?");
        }
        if(customsItemCode != null){
            hql.append(" and model.customsItemCode = ?");
        }

        //group by
        if(customsItemCode != null && declarationSeq != null){
            hql.append(" group by model.brandCode, model.declarationNo, model.customsWarehouseCode, model.declarationSeq, model.customsItemCode");
            obj = new Object[]{brandCode, declarationNo, customsWarehouseCode, declarationSeq, customsItemCode};
        }else if(declarationSeq != null){
            hql.append(" group by model.brandCode, model.declarationNo, model.customsWarehouseCode, model.declarationSeq");
            obj = new Object[]{brandCode, declarationNo, customsWarehouseCode, declarationSeq};
        }else if(customsItemCode != null){
            hql.append(" group by model.brandCode, model.declarationNo, model.customsWarehouseCode, model.customsItemCode");
            obj = new Object[]{brandCode, declarationNo, customsWarehouseCode, customsItemCode};
        }else{
            hql.append(" group by model.brandCode, model.declarationNo, model.customsWarehouseCode");
            obj = new Object[]{brandCode, declarationNo, customsWarehouseCode};
        }
        log.info("QQQQQQQ:"+hql.toString());
        List result = getHibernateTemplate().find(hql.toString(), obj);
        return (result != null && result.size() > 0 ? (Double) result.get(0) : null);
    }
    /**
     *依據brandCode、declarationNo、declarationSeq、customsItemCode、customsWarehouseCode查詢出目前可用庫存數量
    *
    * @param brandCode
    * @param declarationNo
    * @param declarationSeq
    * @param customsItemCode
    * @param customsWarehouseCode
    * @return Double
    */
   public Double getCurrentOnHandQtyWithBlockByProperty(String brandCode, String declarationNo, Long declarationSeq, String customsItemCode,
	    String customsWarehouseCode) {

	Object[] obj = null;
       StringBuffer hql = new StringBuffer("select sum(nvl(model.onHandQuantity,0) - nvl(model.outUncommitQty,0) + "
	        + "nvl(model.inUncommitQty,0) + nvl(model.moveUncommitQty,0) + nvl(model.otherUncommitQty,0)) from CmDeclarationOnHand as model");//20160429by jason新增nvl(model.blockOnHandQuantity,0)
       hql.append(" where model.brandCode = ?");
       hql.append(" and model.declarationNo = ?");
       hql.append(" and model.customsWarehouseCode = ?");
       if(declarationSeq != null){
           hql.append(" and model.declarationSeq = ?");
       }
       if(customsItemCode != null){
           hql.append(" and model.customsItemCode = ?");
       }

       //group by
       if(customsItemCode != null && declarationSeq != null){
           hql.append(" group by model.brandCode, model.declarationNo, model.customsWarehouseCode, model.declarationSeq, model.customsItemCode");
           obj = new Object[]{brandCode, declarationNo, customsWarehouseCode, declarationSeq, customsItemCode};
       }else if(declarationSeq != null){
           hql.append(" group by model.brandCode, model.declarationNo, model.customsWarehouseCode, model.declarationSeq");
           obj = new Object[]{brandCode, declarationNo, customsWarehouseCode, declarationSeq};
       }else if(customsItemCode != null){
           hql.append(" group by model.brandCode, model.declarationNo, model.customsWarehouseCode, model.customsItemCode");
           obj = new Object[]{brandCode, declarationNo, customsWarehouseCode, customsItemCode};
       }else{
           hql.append(" group by model.brandCode, model.declarationNo, model.customsWarehouseCode");
           obj = new Object[]{brandCode, declarationNo, customsWarehouseCode};
       }
       log.info("QQQQQQQ:"+hql.toString());
       List result = getHibernateTemplate().find(hql.toString(), obj);
       return (result != null && result.size() > 0 ? (Double) result.get(0) : null);
   }

    /**
     * 依據declarationNo、declarationSeq、customsItemCode、customsWarehouseCode、brandCode查詢，並進行鎖定
     *
     * @param declarationNo
     * @param declarationSeq
     * @param customsItemCode
     * @param customsWarehouseCode
     * @param brandCode
     * @return List
     */
    public List<CmDeclarationOnHand> getLockedOnHand(final String declarationNo, final Long declarationSeq,
	    final String customsItemCode, final String customsWarehouseCode, final String brandCode) {
	List<CmDeclarationOnHand> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("from CmDeclarationOnHand as model");
			hql.append(" where model.brandCode = :brandCode");
			hql.append(" and model.customsWarehouseCode = :customsWarehouseCode");
			if (declarationNo != null) {
			    hql.append(" and model.declarationNo = :declarationNo");
			    if (declarationSeq != null) {
			        hql.append(" and model.declarationSeq = :declarationSeq");
			    }
			}
			if(customsItemCode != null){
			    hql.append(" and model.customsItemCode = :customsItemCode");
			}

			Query query = session.createQuery(hql.toString());
			query.setLockMode("model", LockMode.UPGRADE_NOWAIT);
			query.setString("brandCode", brandCode);
			query.setString("customsWarehouseCode", customsWarehouseCode);
			if (declarationNo != null) {
			    query.setString("declarationNo", declarationNo);
			    if (declarationSeq != null) {
				query.setLong("declarationSeq", declarationSeq);
			    }
			}
			if(customsItemCode != null){
			    query.setString("customsItemCode", customsItemCode);
			}
			log.info("SQL:"+query.toString());
			return query.list();
		    }
		});

	return result;
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
	public void updateMoveUncommitQuantity(String declarationNo, Long declarationSeq, String customsItemCode, String customsWarehouseCode, String brandCode, Double quantity,
			String loginUser) throws FormException {

		try {
			List<CmDeclarationOnHand> lockedOnHands = this.getLockedOnHand(declarationNo, declarationSeq, customsItemCode, customsWarehouseCode, brandCode);
			CmDeclarationOnHand lockedOnHand = null;
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				if (quantity < 0) {

					Double availableQuantity = this.getCurrentOnHandQtyByProperty(brandCode, declarationNo, declarationSeq, customsItemCode, customsWarehouseCode);
					//log.info("availableQuantity:"+ availableQuantity);
					if (availableQuantity == null) {
						throw new NoSuchObjectException("更新庫存時，查無品號：" + customsItemCode + ",關別：" + customsWarehouseCode + "的庫存量！");
					} else if ((quantity *-1) > availableQuantity) {
						throw new InsufficientQuantityException("品號：" + customsItemCode + ",關別：" + customsWarehouseCode + "可用庫存量不足！");
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
					lockedOnHand = new CmDeclarationOnHand();
					lockedOnHand.setDeclarationNo(declarationNo);
					lockedOnHand.setDeclarationSeq(declarationSeq);
					lockedOnHand.setCustomsWarehouseCode(customsWarehouseCode);
					lockedOnHand.setBrandCode(brandCode);
					lockedOnHand.setCustomsItemCode(customsItemCode);
					lockedOnHand.setOnHandQuantity(0D);
					lockedOnHand.setOutUncommitQty(0D);
					lockedOnHand.setInUncommitQty(0D);
					lockedOnHand.setMoveUncommitQty(quantity);
					lockedOnHand.setOtherUncommitQty(0D);
					lockedOnHand.setCreatedBy(loginUser);
					lockedOnHand.setCreationDate(new Date());
					lockedOnHand.setLastUpdatedBy(loginUser);
					lockedOnHand.setLastUpdateDate(new Date());
					lockedOnHand.setBlockOnHandQuantity(0D); // 新增報單時，鎖定庫存的欄位值預設為0 add by Weichun 2012.03.28
					lockedOnHand.setUnblockOnHandQuantity(0D);
					
					log.info("test");
					
					lockedOnHand.setExpiryDate(getExpiryDateByImportDate(declarationNo, declarationSeq));
					
					save(lockedOnHand);
				} else {
					throw new FormException("品號：" + customsItemCode + ",關別：" + customsWarehouseCode + "目前並無庫存數量，請確認！");
				}
			}

		} catch (CannotAcquireLockException cale) {
			throw new FormException("品號：" + customsItemCode + ",關別：" + customsWarehouseCode + "已鎖定，請稍後再試！");
		}
	}

    /**
     *
     * @param declarationNo
     * @param declarationSeq
     * @param customsItemCode
     * @param customsWarehouseCode
     * @param brandCode
     * @param quantity
     * @param loginUser
     * @throws FormException
     */
	public void updateOutUncommitQuantity(String declarationNo, Long declarationSeq, String customsItemCode, String customsWarehouseCode, String brandCode, Double quantity,
			String loginUser) throws FormException {
            try {
	        List<CmDeclarationOnHand> lockedOnHands = getLockedOnHand(declarationNo, declarationSeq, customsItemCode, customsWarehouseCode, brandCode);
		if (lockedOnHands != null && lockedOnHands.size() > 0) {
		    CmDeclarationOnHand lockedOnHand = lockedOnHands.get(0);
		    lockedOnHand.setOutUncommitQty(lockedOnHand.getOutUncommitQty() - quantity);
		    lockedOnHand.setLastUpdatedBy(loginUser);
		    lockedOnHand.setLastUpdateDate(new Date());
		    update(lockedOnHand);
		}else{
		    CmDeclarationOnHand newDeclarationOnHand = new CmDeclarationOnHand();
		    newDeclarationOnHand.setBrandCode(brandCode);
		    newDeclarationOnHand.setDeclarationNo(declarationNo);
		    newDeclarationOnHand.setDeclarationSeq(declarationSeq);
		    newDeclarationOnHand.setCustomsItemCode(customsItemCode);
		    newDeclarationOnHand.setCustomsWarehouseCode(customsWarehouseCode);
		    newDeclarationOnHand.setOnHandQuantity(0D);
		    newDeclarationOnHand.setOutUncommitQty(0D - quantity);
		    newDeclarationOnHand.setInUncommitQty(0D);
		    newDeclarationOnHand.setMoveUncommitQty(0D);
		    newDeclarationOnHand.setOtherUncommitQty(0D);
		    newDeclarationOnHand.setCreatedBy(loginUser);
		    newDeclarationOnHand.setCreationDate(new Date());
		    newDeclarationOnHand.setLastUpdatedBy(loginUser);
		    newDeclarationOnHand.setLastUpdateDate(new Date());
		    newDeclarationOnHand.setBlockOnHandQuantity(0D); // 新增報單時，鎖定庫存的欄位值預設為0 add by Weichun 2012.03.28
		    newDeclarationOnHand.setUnblockOnHandQuantity(0D);
		    
		    newDeclarationOnHand.setExpiryDate(getExpiryDateByImportDate(declarationNo, declarationSeq));
		    
		    save(newDeclarationOnHand);
		}
	    } catch (CannotAcquireLockException cale) {
		throw new FormException("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")" +
				"、關別(" + customsWarehouseCode + ")的報單庫存已鎖定，請稍後再試！");
	    }
	}

	/**
	 * 更新未結調整數量
	 *
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @param quantity
	 * @param loginUser
	 * @throws FormException
	 */
	public void updateOtherUncommitQuantity(String declarationNo, Long declarationSeq, String customsItemCode,
			String customsWarehouseCode, String brandCode, Double quantity, String loginUser, String unBlockOnHand)
			throws FormException {
		try {

			List<CmDeclarationOnHand> lockedOnHands = this.getLockedOnHand(declarationNo, declarationSeq, customsItemCode,
					customsWarehouseCode, brandCode);
			CmDeclarationOnHand lockedOnHand = null;
			if (lockedOnHands != null && lockedOnHands.size() > 0) {
				if (quantity < 0) {
					Double availableQuantity = this.getCurrentOnHandQtyByProperty(brandCode, declarationNo, declarationSeq,
							customsItemCode, customsWarehouseCode);
					//Maco 2017.11.16 庫存不足判斷,2017.11.29因庫存鎖定問題退回
					//Double useQuantity = Math.abs(quantity);
					//log.info("quantity:"+quantity+" availableQuantity:"+availableQuantity+"  useQuantity > availableQuantity:"+(useQuantity > availableQuantity));
					log.info("quantity:"+quantity+" availableQuantity:"+availableQuantity+"  useQuantity > availableQuantity:"+(quantity > availableQuantity));
					if (availableQuantity == null) {
						throw new NoSuchObjectException("更新庫存時，查無品號：" + customsItemCode + ",關別：" + customsWarehouseCode
								+ "的庫存量！");
					} //else if (useQuantity > availableQuantity) {
						 else if (quantity > availableQuantity) {
						throw new InsufficientQuantityException("品號：" + customsItemCode + ",關別：" + customsWarehouseCode
								+ "可用庫存量不足！");
					}
				}

				lockedOnHand = lockedOnHands.get(0);
				Double otherUncommitQty = NumberUtils.getDouble(lockedOnHand.getOtherUncommitQty());
				lockedOnHand.setOtherUncommitQty(otherUncommitQty + quantity);
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				// 將鎖住的報單庫存解鎖，但是需要存記錄 by Weichun 2011.09.27
				if ("Y".equals(unBlockOnHand) && quantity < 0) {
					System.out.println("＝＝＝自動解鎖報單＝＝＝");
					Double blockQty = NumberUtils.getDouble(lockedOnHand.getBlockOnHandQuantity());
					if (quantity * -1 > blockQty) {
						lockedOnHand.setUnblockOnHandQuantity(blockQty);
						lockedOnHand.setBlockOnHandQuantity(0D);
					} else {
						lockedOnHand.setUnblockOnHandQuantity(quantity * -1);
						lockedOnHand.setBlockOnHandQuantity(blockQty + quantity);
					}
				} else if ("Y".equals(unBlockOnHand) && quantity > 0) { // 回寫已經解鎖的報單庫存  by Weichun 2011.09.27
					System.out.println("＝＝＝還原解鎖報單＝＝＝");
					Double unBlockQty = NumberUtils.getDouble(lockedOnHand.getUnblockOnHandQuantity());
					Double blockQty = NumberUtils.getDouble(lockedOnHand.getBlockOnHandQuantity());
					lockedOnHand.setBlockOnHandQuantity(blockQty + unBlockQty);
					lockedOnHand.setUnblockOnHandQuantity(0.0D);
				}
				update(lockedOnHand);
			} else {
				lockedOnHand = new CmDeclarationOnHand();
				lockedOnHand.setDeclarationNo(declarationNo);
				lockedOnHand.setDeclarationSeq(declarationSeq);
				lockedOnHand.setCustomsItemCode(customsItemCode);
				lockedOnHand.setCustomsWarehouseCode(customsWarehouseCode);
				lockedOnHand.setBrandCode(brandCode);
				lockedOnHand.setOnHandQuantity(0D);
				lockedOnHand.setMoveUncommitQty(0D);
				lockedOnHand.setInUncommitQty(0D);
				lockedOnHand.setOutUncommitQty(0D);
				lockedOnHand.setOtherUncommitQty(quantity);
				lockedOnHand.setCreatedBy(loginUser);
				lockedOnHand.setCreationDate(new Date());
				lockedOnHand.setLastUpdatedBy(loginUser);
				lockedOnHand.setLastUpdateDate(new Date());
				lockedOnHand.setBlockOnHandQuantity(0D); // 新增報單時，鎖定庫存的欄位值預設為0 add by Weichun 2012.03.28
				lockedOnHand.setUnblockOnHandQuantity(0D);
				
				lockedOnHand.setExpiryDate(getExpiryDateByImportDate(declarationNo, declarationSeq));
				
				save(lockedOnHand);

				// throw new FormException("查無品號：" + customsItemCode + ",庫別：" + customsWarehouseCode + "的庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品號：" + customsItemCode + ",關別：" + customsWarehouseCode + "已鎖定，請稍後再試！");
		}
	}

	/**
     * 依據declarationNo、declarationSeq、customsWarehouseCode、brandCode查詢
     *
     * @param declarationNo
     * @param declarationSeq
     * @param customsItemCode
     * @param customsWarehouseCode
     * @param brandCode
     * @return List
     */
    public CmDeclarationOnHand getOnHandById(final String declarationNo, final Long declarationSeq, final String customsWarehouseCode, final String brandCode) {
	List<CmDeclarationOnHand> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("from CmDeclarationOnHand as model");
			hql.append(" where model.brandCode = :brandCode");
			hql.append(" and model.customsWarehouseCode = :customsWarehouseCode");
			if (declarationNo != null) {
			    hql.append(" and model.declarationNo = :declarationNo");
			    if (declarationSeq != null) {
			        hql.append(" and model.declarationSeq = :declarationSeq");
			    }
			}
			Query query = session.createQuery(hql.toString());
			query.setString("brandCode", brandCode);
			query.setString("customsWarehouseCode", customsWarehouseCode);
			if (declarationNo != null) {
			    query.setString("declarationNo", declarationNo);
			    if (declarationSeq != null) {
				query.setLong("declarationSeq", declarationSeq);
			    }
			}
			return query.list();
		    }
		});

	if(result != null && result.size() > 0)
		return result.get(0);
	else
		return null;
    }

    /**
     * 銷貨單預定報單庫存
     *
     * @param declarationNo
     * @param declarationSeq
     * @param customsItemCode
     * @param customsWarehouseCode
     * @param brandCode
     * @param quantity
     * @param opUser
     * @throws FormException
     */
    public void updateOutUncommitQty(String declarationNo, Long declarationSeq, String customsItemCode, String customsWarehouseCode, String brandCode, Double quantity,
			String opUser, String allowWholeSale) throws FormException {
        log.info("updateOutUncommitQty " + "declarationNo = " + declarationNo + " declarationSeq = " + declarationSeq +
        		" customsItemCode = " + customsItemCode + " customsWarehouseCode = " + customsWarehouseCode + " brandCode = " +
        		" quantity = " + quantity + " opUser = " + opUser + " allowWholeSale = " + allowWholeSale);
        try {
        	//只用報關號碼，項次，關別，品牌做搜尋
            List<CmDeclarationOnHand> declarationOnHands = getLockedOnHand(declarationNo, declarationSeq, null, customsWarehouseCode, brandCode);
            if(declarationOnHands != null && declarationOnHands.size() > 0){
	        	CmDeclarationOnHand declarationOnHand = declarationOnHands.get(0);
	        	//判斷是否有可能同報關單號同項次品號不同
	        	if(!customsItemCode.equals(declarationOnHand.getCustomsItemCode()))
	        		throw new ValidationErrorException("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" +
	        	    		declarationSeq + ")、關別(" + customsWarehouseCode + ")中的海關料號(" + declarationOnHand.getCustomsItemCode() +
	        	    		")與商品品號(" + customsItemCode + ")不符！");
	        	Double availableQuantity = NumberUtils.getDouble(declarationOnHand.getOnHandQuantity())
	        			- NumberUtils.getDouble(declarationOnHand.getOutUncommitQty())
	        			+ NumberUtils.getDouble(declarationOnHand.getInUncommitQty())
	        			+ NumberUtils.getDouble(declarationOnHand.getMoveUncommitQty())
	        			+ NumberUtils.getDouble(declarationOnHand.getOtherUncommitQty());
	        	log.info("可用報單庫存量availableQuantity:"+availableQuantity);
	        	if(quantity > availableQuantity && !"Y".equals(allowWholeSale)){
	        	    throw new ValidationErrorException("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" +
	        	    		declarationSeq + ")、海關料號(" + customsItemCode + ")、關別(" +
	        	            customsWarehouseCode + ")的可用庫存量(" + availableQuantity + ")不足！");
	        	}else{
	        		log.info("進行更新現有報單庫存量");
	        	    declarationOnHand.setOutUncommitQty(NumberUtils.getDouble(declarationOnHand.getOutUncommitQty()) + quantity);
	        	    declarationOnHand.setLastUpdatedBy(opUser);
	        	    declarationOnHand.setLastUpdateDate(new Date());
	    	        update(declarationOnHand);
	    	        log.info("報單庫存量更新完成");
	        	}
            }else if("Y".equals(allowWholeSale)){
            	log.info("進行新增報單庫存量資料");
            	CmDeclarationOnHand declarationOnHand = new CmDeclarationOnHand();
            	declarationOnHand.setDeclarationNo(declarationNo);
            	declarationOnHand.setDeclarationSeq(declarationSeq);
            	declarationOnHand.setCustomsItemCode(customsItemCode);
            	declarationOnHand.setCustomsWarehouseCode(customsWarehouseCode);
            	declarationOnHand.setOnHandQuantity(0D);
            	declarationOnHand.setOutUncommitQty(quantity);
            	declarationOnHand.setInUncommitQty(0D);
            	declarationOnHand.setMoveUncommitQty(0D);
            	declarationOnHand.setOtherUncommitQty(0D);
            	declarationOnHand.setLastUpdatedBy("auto");
            	declarationOnHand.setLastUpdateDate(new Date());
            	declarationOnHand.setCreatedBy("auto");
            	declarationOnHand.setCreationDate(new Date());
            	declarationOnHand.setBrandCode(brandCode);
            	declarationOnHand.setBlockOnHandQuantity(0D); // 新增報單時，鎖定庫存的欄位值預設為0 add by Weichun 2012.03.28
			    declarationOnHand.setUnblockOnHandQuantity(0D);
			    
			    declarationOnHand.setExpiryDate(getExpiryDateByImportDate(declarationNo, declarationSeq));
			    
            	save(declarationOnHand);
            	log.info("報單庫存量資料新增完成");
            }else{
            	throw new NoSuchObjectException("查無品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")、關別(" +
            			customsWarehouseCode + ")的報單庫存資料！");
            }
	} catch (CannotAcquireLockException cale) {
			cale.printStackTrace();
            throw new FormException("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")" +
	            "、關別(" + customsWarehouseCode + ")的報單庫存已鎖定，請稍後再試！");
	}
    }

    /**
     * 進貨單新增庫存
     *
     * @param declarationNo
     * @param declarationSeq
     * @param customsItemCode
     * @param customsWarehouseCode
     * @param brandCode
     * @param quantity
     * @param opUser
     * @throws FormException
     */
    public void updateInUncommitQty(String declarationNo, Long declarationSeq, String customsItemCode, String customsWarehouseCode,
    			String brandCode, Double quantity, String opUser) throws FormException {
        log.info("updateInUncommitQty " + "declarationNo = " + declarationNo + " declarationSeq = " + declarationSeq +
        		" customsItemCode = " + customsItemCode + " customsWarehouseCode = " + customsWarehouseCode + " brandCode = " +
        		" quantity = " + quantity + " opUser = " + opUser);
        try {
        	//只用報關號碼，項次，關別，品牌做搜尋
            List<CmDeclarationOnHand> declarationOnHands = getLockedOnHand(declarationNo, declarationSeq, null, customsWarehouseCode, brandCode);
            if(declarationOnHands != null && declarationOnHands.size() > 0){
				Double availableQuantity = this.getCurrentOnHandQtyByProperty(brandCode, declarationNo, declarationSeq, customsItemCode, customsWarehouseCode);
				//log.info("availableQuantity:"+ availableQuantity);
				if ((quantity *-1) > availableQuantity) {
					throw new InsufficientQuantityException("品號：" + customsItemCode + ",關別：" + customsWarehouseCode + "可用庫存量不足！");
				}
	        	CmDeclarationOnHand declarationOnHand = declarationOnHands.get(0);
	        	//判斷是否有可能同報關單號同項次品號不同
	        	//if(!customsItemCode.equals(declarationOnHand.getCustomsItemCode()))
	        		//throw new ValidationErrorException("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" +
	        	    		//declarationSeq + ")、關別(" + customsWarehouseCode + ")中的海關料號(" + declarationOnHand.getCustomsItemCode() +
	        	    		//")與商品品號(" + customsItemCode + ")不符！");
	        	declarationOnHand.setCustomsItemCode(customsItemCode);
			    declarationOnHand.setInUncommitQty( quantity + NumberUtils.getDouble(declarationOnHand.getInUncommitQty()));
			    declarationOnHand.setLastUpdateDate(new Date());
			    declarationOnHand.setLastUpdatedBy(opUser);
    	        update(declarationOnHand);
            }else{
            	CmDeclarationOnHand declarationOnHand = new CmDeclarationOnHand();
			    declarationOnHand.setBrandCode(brandCode);
			    declarationOnHand.setDeclarationNo(declarationNo);
			    declarationOnHand.setDeclarationSeq( declarationSeq);
			    declarationOnHand.setCustomsItemCode(customsItemCode);
			    declarationOnHand.setCustomsWarehouseCode(customsWarehouseCode);
			    declarationOnHand.setItemCode(customsItemCode);
			    declarationOnHand.setOnHandQuantity(0D);
			    declarationOnHand.setOutUncommitQty(0D);
			    declarationOnHand.setMoveUncommitQty(0D);
			    declarationOnHand.setOtherUncommitQty(0D);
			    declarationOnHand.setInUncommitQty(quantity);
			    declarationOnHand.setCreationDate(new Date());
			    declarationOnHand.setCreatedBy(opUser);
			    declarationOnHand.setLastUpdateDate(new Date());
			    declarationOnHand.setLastUpdatedBy(opUser);
			    declarationOnHand.setBlockOnHandQuantity(0D); // 新增報單時，鎖定庫存的欄位值預設為0 add by Weichun 2012.03.28
			    declarationOnHand.setUnblockOnHandQuantity(0D);
			    
			    declarationOnHand.setExpiryDate(getExpiryDateByImportDate(declarationNo, declarationSeq));
			    
            	save(declarationOnHand);
            }
	} catch (CannotAcquireLockException cale) {
            throw new FormException("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")" +
	            "、關別(" + customsWarehouseCode + ")的報單庫存已鎖定，請稍後再試！");
	}
    }

	/**
	 * 日結時使用，更新可用庫存(onHandQuantity)及調撥未結庫存欄位(otherUncommitQty)
	 * @param declarationNo
	 * @param declarationSeq
	 * @param customsItemCode
	 * @param customsWarehouseCode
	 * @param brandCode
	 * @param quantity
	 * @param opUser
	 * @throws FormException
	 */
	public void updateMoveOnHand(String declarationNo, Long declarationSeq, String customsItemCode, String customsWarehouseCode, String brandCode, Double quantity,
			String opUser) throws FormException {
	    try{
			List<CmDeclarationOnHand> declarationOnHands = getLockedOnHand(declarationNo, declarationSeq, customsItemCode, customsWarehouseCode, brandCode);
			//ImOnHand lockedOnHand = null;
			if (declarationOnHands != null && declarationOnHands.size() > 0) {
	        	CmDeclarationOnHand declarationOnHand = declarationOnHands.get(0);

				Double movementUncommitQty = declarationOnHand.getMoveUncommitQty() == null ? 0D : declarationOnHand.getMoveUncommitQty();
				declarationOnHand.setMoveUncommitQty(movementUncommitQty - quantity);
				declarationOnHand.setOnHandQuantity(declarationOnHand.getOnHandQuantity() + quantity);
				declarationOnHand.setLastUpdatedBy(opUser);
				declarationOnHand.setLastUpdateDate(new Date());
				update(declarationOnHand);
			} else {
	        	throw new NoSuchObjectException("查無品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")、關別(" +
	            		customsWarehouseCode + ")的報單庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
	            throw new FormException("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")" +
		            "、關別(" + customsWarehouseCode + ")的報單庫存已鎖定，請稍後再試！");
		}
	}

	/**
	 * 日結時使用，更新可用庫存(onHandQuantity)及進貨未結庫存欄位(otherUncommitQty)
	 * @param declarationNo
	 * @param declarationSeq
	 * @param customsItemCode
	 * @param customsWarehouseCode
	 * @param brandCode
	 * @param quantity
	 * @param opUser
	 * @throws FormException
	 */
	public void updateInOnHand(String declarationNo, Long declarationSeq, String customsItemCode, String customsWarehouseCode, String brandCode, Double quantity,
			String opUser) throws FormException {
	    try{
			List<CmDeclarationOnHand> declarationOnHands = getLockedOnHand(declarationNo, declarationSeq, customsItemCode, customsWarehouseCode, brandCode);
			//ImOnHand lockedOnHand = null;
			if (declarationOnHands != null && declarationOnHands.size() > 0) {
	        	CmDeclarationOnHand declarationOnHand = declarationOnHands.get(0);

				Double inUncommitQty = declarationOnHand.getInUncommitQty() == null ? 0D : declarationOnHand.getInUncommitQty();
				declarationOnHand.setInUncommitQty(inUncommitQty - quantity);
				declarationOnHand.setOnHandQuantity(declarationOnHand.getOnHandQuantity() + quantity);
				declarationOnHand.setLastUpdatedBy(opUser);
				declarationOnHand.setLastUpdateDate(new Date());
				update(declarationOnHand);
			} else {
	        	throw new NoSuchObjectException("查無品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")、關別(" +
	            		customsWarehouseCode + ")的報單庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
	            throw new FormException("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")" +
		            "、關別(" + customsWarehouseCode + ")的報單庫存已鎖定，請稍後再試！");
		}
	}
	/**
	 * 日結時使用，更新可用庫存(onHandQuantity)及其他未結庫存欄位(otherUncommitQty)
	 * @param declarationNo
	 * @param declarationSeq
	 * @param customsItemCode
	 * @param customsWarehouseCode
	 * @param brandCode
	 * @param quantity
	 * @param opUser
	 * @throws FormException
	 */
	public void updateOtherOnHand(String declarationNo, Long declarationSeq, String customsItemCode, String customsWarehouseCode, String brandCode, Double quantity,
			String opUser) throws FormException {
	    try{
			List<CmDeclarationOnHand> declarationOnHands = getLockedOnHand(declarationNo, declarationSeq, customsItemCode, customsWarehouseCode, brandCode);
			//ImOnHand lockedOnHand = null;
			if (declarationOnHands != null && declarationOnHands.size() > 0) {
	        	CmDeclarationOnHand declarationOnHand = declarationOnHands.get(0);

				Double otherUncommitQty = declarationOnHand.getOtherUncommitQty() == null ? 0D : declarationOnHand.getOtherUncommitQty();
				declarationOnHand.setOtherUncommitQty(otherUncommitQty - quantity);
				declarationOnHand.setOnHandQuantity(declarationOnHand.getOnHandQuantity() + quantity);
				declarationOnHand.setLastUpdatedBy(opUser);
				declarationOnHand.setLastUpdateDate(new Date());
				update(declarationOnHand);
			} else {
	        	throw new NoSuchObjectException("查無品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")、關別(" +
	            		customsWarehouseCode + ")的報單庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
	            throw new FormException("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")" +
		            "、關別(" + customsWarehouseCode + ")的報單庫存已鎖定，請稍後再試！");
		}
	}

	/**
	 * 日結時使用，更新可用庫存(onHandQuantity)及出貨未結庫存欄位(otherUncommitQty)
	 * @param declarationNo
	 * @param declarationSeq
	 * @param customsItemCode
	 * @param customsWarehouseCode
	 * @param brandCode
	 * @param quantity
	 * @param opUser
	 * @throws FormException
	 */
	public void updateOutOnHand(String declarationNo, Long declarationSeq, String customsItemCode, String customsWarehouseCode, String brandCode, Double quantity,
			String opUser) throws FormException {
	    try{
			List<CmDeclarationOnHand> declarationOnHands = getLockedOnHand(declarationNo, declarationSeq, customsItemCode, customsWarehouseCode, brandCode);
			//ImOnHand lockedOnHand = null;
			if (declarationOnHands != null && declarationOnHands.size() > 0) {
	        	CmDeclarationOnHand declarationOnHand = declarationOnHands.get(0);

				Double outUncommitQty = declarationOnHand.getOutUncommitQty() == null ? 0D : declarationOnHand.getOutUncommitQty();
				declarationOnHand.setOutUncommitQty(outUncommitQty - quantity);
				declarationOnHand.setOnHandQuantity(declarationOnHand.getOnHandQuantity() - quantity);
				declarationOnHand.setLastUpdatedBy(opUser);
				declarationOnHand.setLastUpdateDate(new Date());
				update(declarationOnHand);
			} else {
	        	throw new NoSuchObjectException("查無品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")、關別(" +
	            		customsWarehouseCode + ")的報單庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
	            throw new FormException("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")" +
		            "、關別(" + customsWarehouseCode + ")的報單庫存已鎖定，請稍後再試！");
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
	public void updateStockOnHand(String declarationNo, Long declarationSeq, String customsItemCode, String customsWarehouseCode, String brandCode, Double quantity,
			String opUser) throws FormException {
	    try{
			List<CmDeclarationOnHand> declarationOnHands = getLockedOnHand(declarationNo, declarationSeq, customsItemCode, customsWarehouseCode, brandCode);
			//ImOnHand lockedOnHand = null;
			if (declarationOnHands != null && declarationOnHands.size() > 0) {
	        	CmDeclarationOnHand declarationOnHand = declarationOnHands.get(0);
				declarationOnHand.setOnHandQuantity(NumberUtils.getDouble(declarationOnHand.getOnHandQuantity()) + quantity);
				declarationOnHand.setLastUpdatedBy(opUser);
				declarationOnHand.setLastUpdateDate(new Date());
				update(declarationOnHand);
			} else {
	        	throw new NoSuchObjectException("查無品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")、關別(" +
	            		customsWarehouseCode + ")的報單庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
	            throw new FormException("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")" +
		            "、關別(" + customsWarehouseCode + ")的報單庫存已鎖定，請稍後再試！");
		}
	}

	/**
	 * 調整單反確認時使用，更新報單可用庫存，並且還原當初鎖住的報單庫存 by Weichun 2011.09.27
	 *
	 * @param declarationNo
	 * @param declarationSeq
	 * @param customsItemCode
	 * @param customsWarehouseCode
	 * @param brandCode
	 * @param quantity
	 * @param opUser
	 * @throws FormException
	 */
	public void updateStockOnHandByOther(String declarationNo, Long declarationSeq, String customsItemCode,
			String customsWarehouseCode, String brandCode, Double quantity, String opUser) throws FormException {
		try {
			List<CmDeclarationOnHand> declarationOnHands = getLockedOnHand(declarationNo, declarationSeq, customsItemCode,
					customsWarehouseCode, brandCode);
			// ImOnHand lockedOnHand = null;
			if (declarationOnHands != null && declarationOnHands.size() > 0) {
				CmDeclarationOnHand declarationOnHand = declarationOnHands.get(0);
				declarationOnHand.setOnHandQuantity(NumberUtils.getDouble(declarationOnHand.getOnHandQuantity()) + quantity);
				declarationOnHand.setLastUpdatedBy(opUser);
				declarationOnHand.setLastUpdateDate(new Date());
				Double unBlockQty = NumberUtils.getDouble(declarationOnHand.getUnblockOnHandQuantity());
				Double blockQty = NumberUtils.getDouble(declarationOnHand.getBlockOnHandQuantity());
				declarationOnHand.setBlockOnHandQuantity(blockQty + unBlockQty);
				declarationOnHand.setUnblockOnHandQuantity(0.0D);
				update(declarationOnHand);
			} else {
				throw new NoSuchObjectException("查無品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq
						+ ")、海關料號(" + customsItemCode + ")、關別(" + customsWarehouseCode + ")的報單庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號("
					+ customsItemCode + ")" + "、關別(" + customsWarehouseCode + ")的報單庫存已鎖定，請稍後再試！");
		}
	}

	/**
	 * 依據declarationNo、declarationSeq、customsItemCode、customsWarehouseCode、 brandCode查詢，並進行鎖定
	 *
	 * @param declarationNo
	 * @param declarationSeq
	 * @param customsItemCode
	 * @param customsWarehouseCode
	 * @param brandCode
	 * @param startDate
	 * @param endDate
	 * @return List
	 */
	public List<CmDeclarationOnHand> getLockedOnHandForT2(final String declarationNo, final Long declarationSeq,
			final String customsItemCode, final String customsWarehouseCode, final String brandCode, final Date startDate,
			final Date endDate) {
		log.info("declarationNo        = " + declarationNo);
		log.info("declarationSeq       = " + declarationSeq);
		log.info("customsItemCode      = " + customsItemCode);
		log.info("customsWarehouseCode = " + customsWarehouseCode);
		log.info("brandCode            = " + brandCode);
		log.info("startDate            = " + startDate);
		log.info("endDate               = " + endDate);
		List<CmDeclarationOnHand> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from CmDeclarationOnHand as model");
				hql.append(" where model.brandCode = :brandCode");
				hql.append(" and model.customsWarehouseCode = :customsWarehouseCode");
				if (declarationNo != null) {
					hql.append(" and model.declarationNo = :declarationNo");
					if (declarationSeq != null) {
						hql.append(" and model.declarationSeq = :declarationSeq");
					}
				}
				if (customsItemCode != null) {
					hql.append(" and model.customsItemCode = :customsItemCode");
				}
				if (startDate != null) {
					hql.append(" and trunc(model.creationDate) >= :startDate");
				}
				if (endDate != null) {
					hql.append(" and trunc(model.creationDate) <= :endDate");
				}
				Query query = session.createQuery(hql.toString());
				query.setLockMode("model", LockMode.UPGRADE_NOWAIT);
				query.setString("brandCode", brandCode);
				query.setString("customsWarehouseCode", customsWarehouseCode);
				if (declarationNo != null) {
					query.setString("declarationNo", declarationNo);
					if (declarationSeq != null) {
						query.setLong("declarationSeq", declarationSeq);
					}
				}
				if (customsItemCode != null) {
					query.setString("customsItemCode", customsItemCode);
				}
				if (startDate != null) {
					query.setDate("startDate", startDate);
				}
				if (endDate != null) {
					query.setDate("endDate", endDate);
				}
				log.info("query = " + query.list().toString());
				return query.list();
			}
		});
		return result;
	}

	/**
	 * 依據declarationNo、declarationSeq、customsItemCode、customsWarehouseCode、brandCode查詢，並不進行鎖定
	 *
	 * @param declarationNo
	 * @param declarationSeq
	 * @param customsItemCode
	 * @param customsWarehouseCode
	 * @param brandCode
	 * @param startDate
	 * @param endDate
	 * @return List
	 */
	public List<CmDeclarationOnHand> getNoLockedOnHand(final String declarationNo, final Long declarationSeq, final String customsItemCode,
			final String customsWarehouseCode, final String brandCode, final Date startDate, final Date endDate)throws Exception {
		log.info("declarationNo        = "+declarationNo);
		log.info("declarationSeq       = "+declarationSeq);
		log.info("customsItemCode      = "+customsItemCode);
		log.info("customsWarehouseCode = "+customsWarehouseCode);
		log.info("brandCode            = "+brandCode);
		log.info("startDate            = "+startDate);
		log.info("endDate               = "+endDate);
		List<CmDeclarationOnHand> CmDeclarationOnHands = new ArrayList<CmDeclarationOnHand>();		
		Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	DataSource dataSource = (DataSource) SpringUtils.getApplicationContext().getBean("dataSource");
		try{
			Date dateNew = new Date();
			Date finalDeclDate = DateUtils.addDays(dateNew, -5);
			String finalNewDate = DateUtils.format(finalDeclDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			conn = dataSource.getConnection();
			StringBuffer sql = new StringBuffer();
			stmt = conn.createStatement();
			sql.append("select * from ERP.CM_DECLARATION_ON_HAND CDO, ERP.CM_DECLARATION_HEAD CDH, ERP.CM_DECLARATION_ITEM CDI");
			sql.append(" where 1=1 AND CDO.CUSTOMS_WAREHOUSE_CODE = '"+customsWarehouseCode+"'");
			sql.append(" AND CDO.DECLARATION_NO = CDI.DECL_NO");
			sql.append(" AND CDO.DECLARATION_SEQ = CDI.ITEM_NO");
			sql.append(" AND CDI.O_DECL_NO = CDH.DECL_NO");
			sql.append(" AND CDH.DECL_DATE >= TO_DATE('20090505','YYYYMMDD')");
			sql.append(" AND CDO.LAST_UPDATE_DATE > TO_DATE('"+finalNewDate+"','YYYYMMDD')");
			sql.append(" ORDER BY CDH.DECL_DATE");
			
			rs = stmt.executeQuery(sql.toString());
			
			while(rs.next()){
				CmDeclarationOnHand cmDeclarationOnHand = new CmDeclarationOnHand();
				cmDeclarationOnHand.setDeclarationNo(rs.getString("DECLARATION_NO"));
				cmDeclarationOnHand.setDeclarationSeq(rs.getLong("DECLARATION_SEQ"));
				cmDeclarationOnHand.setCustomsItemCode(rs.getString("CUSTOMS_ITEM_CODE"));
				cmDeclarationOnHand.setCustomsWarehouseCode(rs.getString("CUSTOMS_WAREHOUSE_CODE"));
				cmDeclarationOnHand.setItemCode(rs.getString("ITEM_CODE"));
				cmDeclarationOnHand.setWarehouseCode(rs.getString("WAREHOUSE_CODE"));
				cmDeclarationOnHand.setOnHandQuantity(rs.getDouble("ON_HAND_QUANTITY"));
				cmDeclarationOnHand.setOutUncommitQty(rs.getDouble("OUT_UNCOMMIT_QTY"));
				cmDeclarationOnHand.setInUncommitQty(rs.getDouble("IN_UNCOMMIT_QTY"));
				cmDeclarationOnHand.setMoveUncommitQty(rs.getDouble("MOVE_UNCOMMIT_QTY"));
				cmDeclarationOnHand.setOtherUncommitQty(rs.getDouble("OTHER_UNCOMMIT_QTY"));
				cmDeclarationOnHand.setReserve1(rs.getString("RESERVE1"));
				cmDeclarationOnHand.setReserve2(rs.getString("RESERVE2"));
				cmDeclarationOnHand.setReserve3(rs.getString("RESERVE3"));
				cmDeclarationOnHand.setReserve4(rs.getString("RESERVE4"));
				cmDeclarationOnHand.setReserve5(rs.getString("RESERVE5"));
				cmDeclarationOnHand.setCreatedBy(rs.getString("CREATED_BY"));
				cmDeclarationOnHand.setCreationDate(rs.getDate("CREATION_DATE"));
				cmDeclarationOnHand.setLastUpdatedBy(rs.getString("LAST_UPDATED_BY"));
				cmDeclarationOnHand.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));
				cmDeclarationOnHand.setStatus(rs.getString("STATUS"));
				cmDeclarationOnHand.setBrandCode(rs.getString("BRAND_CODE"));
				cmDeclarationOnHand.setBlockOnHandQuantity(rs.getDouble("BLOCK_ON_HAND_QUANTITY"));
				cmDeclarationOnHand.setDescription(rs.getString("DESCRIPTION"));
				cmDeclarationOnHand.setUnblockOnHandQuantity(rs.getDouble("UNBLOCK_ON_HAND_QUANTITY"));
				CmDeclarationOnHands.add(cmDeclarationOnHand);
			}			
			return CmDeclarationOnHands;
		}catch(Exception e){
			
		}finally{
    		if (stmt != null) {
    			try {
    				stmt.close();
    			} catch (SQLException e) {
    				e.printStackTrace();
    				throw new Exception(e.getMessage());
    			}
    		}
    		if (conn != null) {
    			try {
    				conn.close();
    			} catch (SQLException e) {
    				e.printStackTrace();
    				throw new Exception(e.getMessage());
    			}
    		}
    	}
		return CmDeclarationOnHands;
//		
//		List<CmDeclarationOnHand> result = getHibernateTemplate().executeFind(
//			new HibernateCallback() {
//				public Object doInHibernate(Session session)
//				throws HibernateException, SQLException {
//					
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
//				    Date newDate = new Date();
//				    sdf.format(newDate);
//				    Date finalDeclDate = DateUtils.addDays(newDate, -730);
//				    
//					StringBuffer hql = new StringBuffer("from CmDeclarationOnHand CDO, CmDeclarationItem CDI, CmDeclarationHead CDH");
//					hql.append(" where CDO.brandCode = :brandCode");
//					hql.append(" and CDO.customsWarehouseCode = :customsWarehouseCode");
//					hql.append(" and CDO.declarationNo = CDI.declNo");
//					hql.append(" and CDO.declarationSeq = CDI.itemNo");
//					hql.append(" and CDH.declNo = CDI.ODeclNo");
//					hql.append(" and CDH.declDate >= :declDate");
//					
//					if (declarationNo != null) {
//						hql.append(" and CDO.declarationNo = :declarationNo");
//						if (declarationSeq != null) {
//							hql.append(" and CDO.declarationSeq = :declarationSeq");
//						}
//					}
//					if(customsItemCode != null){
//						hql.append(" and CDO.customsItemCode = :customsItemCode");
//					}
//					if(startDate != null){
//						hql.append(" and trunc(CDO.creationDate) >= :startDate");
//					}
//					if(endDate != null){
//						hql.append(" and trunc(CDO.creationDate) <= :endDate");
//					}
//
//					Query query = session.createQuery(hql.toString());
//					//query.setLockMode("model", LockMode.UPGRADE_NOWAIT);
//					query.setString("brandCode", brandCode);
//					query.setString("customsWarehouseCode", customsWarehouseCode);
//					query.setDate("declDate",finalDeclDate);
//					if (declarationNo != null) {
//						query.setString("declarationNo", declarationNo);
//						if (declarationSeq != null) {
//							query.setLong("declarationSeq", declarationSeq);
//						}
//					}
//					if(customsItemCode != null){
//						query.setString("customsItemCode", customsItemCode);
//					}
//					if(startDate != null){
//						query.setDate("startDate", startDate);
//					}
//					if(endDate != null){
//						query.setDate("endDate", endDate);
//					}
//					log.info("query = " + query.list().toString());
//					return query.list();
//				}
//			});
		
	}
	
	public static CmDeclarationOnHandDAO getFromApplicationContext(ApplicationContext ctx) {
		return (CmDeclarationOnHandDAO) ctx.getBean("cmDeclarationOnHandDAO");
	}
    
    /**
     * 銷貨單預定報單庫存
     *
     * @param declarationNo
     * @param declarationSeq
     * @param customsItemCode
     * @param customsWarehouseCode
     * @param brandCode
     * @param quantity
     * @param opUser
     * @throws FormException
     */
    public void updateOutUncommitQtyNew(String declarationNo, Long declarationSeq, String customsItemCode, String customsWarehouseCode, String brandCode, Double quantity,
			String opUser, String allowWholeSale,List errorMsgs) throws FormException, Exception {
        log.info("updateOutUncommitQty " + "declarationNo = " + declarationNo + " declarationSeq = " + declarationSeq +
        		" customsItemCode = " + customsItemCode + " customsWarehouseCode = " + customsWarehouseCode + " brandCode = " +
        		" quantity = " + quantity + " opUser = " + opUser + " allowWholeSale = " + allowWholeSale);
        
    	//只用報關號碼，項次，關別，品牌做搜尋
        List<CmDeclarationOnHand> declarationOnHands = getLockedOnHand(declarationNo, declarationSeq, null, customsWarehouseCode, brandCode);
        if(declarationOnHands != null && declarationOnHands.size() > 0){
        	CmDeclarationOnHand declarationOnHand = declarationOnHands.get(0);
        	//判斷是否有可能同報關單號同項次品號不同
        	if(customsItemCode.equals(declarationOnHand.getCustomsItemCode())){
        		Double availableQuantity = NumberUtils.getDouble(declarationOnHand.getOnHandQuantity())
    			- NumberUtils.getDouble(declarationOnHand.getOutUncommitQty())
    			+ NumberUtils.getDouble(declarationOnHand.getInUncommitQty())
    			+ NumberUtils.getDouble(declarationOnHand.getMoveUncommitQty())
    			+ NumberUtils.getDouble(declarationOnHand.getOtherUncommitQty());
		    	if(quantity > availableQuantity && !"Y".equals(allowWholeSale)){
		    		
		    		errorMsgs.add("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" +
		    	    		declarationSeq + ")、海關料號(" + customsItemCode + ")、關別(" +
		    	            customsWarehouseCode + ")的可用庫存量(" + availableQuantity + ")不足！");
		    		
	        	}else{
	        	    declarationOnHand.setOutUncommitQty(NumberUtils.getDouble(declarationOnHand.getOutUncommitQty()) + quantity);
	        	    declarationOnHand.setLastUpdatedBy(opUser);
	        	    declarationOnHand.setLastUpdateDate(new Date());
	    	        update(declarationOnHand);
	        	}
        	}else{
        		errorMsgs.add("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" +
        	    		declarationSeq + ")、關別(" + customsWarehouseCode + ")中的海關料號(" + declarationOnHand.getCustomsItemCode() +
        	    		")與商品品號(" + customsItemCode + ")不符！");
        	}   	        	
        }else if("Y".equals(allowWholeSale)){
        	CmDeclarationOnHand declarationOnHand = new CmDeclarationOnHand();
        	declarationOnHand.setDeclarationNo(declarationNo);
        	declarationOnHand.setDeclarationSeq(declarationSeq);
        	declarationOnHand.setCustomsItemCode(customsItemCode);
        	declarationOnHand.setCustomsWarehouseCode(customsWarehouseCode);
        	declarationOnHand.setOnHandQuantity(0D);
        	declarationOnHand.setOutUncommitQty(quantity);
        	declarationOnHand.setInUncommitQty(0D);
        	declarationOnHand.setMoveUncommitQty(0D);
        	declarationOnHand.setOtherUncommitQty(0D);
        	declarationOnHand.setLastUpdatedBy("auto");
        	declarationOnHand.setLastUpdateDate(new Date());
        	declarationOnHand.setCreatedBy("auto");
        	declarationOnHand.setCreationDate(new Date());
        	declarationOnHand.setBrandCode(brandCode);
        	declarationOnHand.setBlockOnHandQuantity(0D); // 新增報單時，鎖定庫存的欄位值預設為0 add by Weichun 2012.03.28
		    declarationOnHand.setUnblockOnHandQuantity(0D);
		    
		    declarationOnHand.setExpiryDate(getExpiryDateByImportDate(declarationNo, declarationSeq));
		    
        	save(declarationOnHand);
        }else{
        	errorMsgs.add("查無品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")、關別(" +
        			customsWarehouseCode + ")的報單庫存資料！");
        }
    }
    
    /**
     * 
     * @param declarationNo
     * @param declarationSeq
     * @param customsItemCode
     * @param customsWarehouseCode
     * @param brandCode
     * @param expiryDate
     * @throws FormException
     */
    public void updateInOnHand2(String declarationNo, Long declarationSeq, String customsItemCode, String customsWarehouseCode, String brandCode, Date expiryDate
			) throws FormException {
	    try{
			List<CmDeclarationOnHand> declarationOnHands = getLockedOnHand(declarationNo, declarationSeq, customsItemCode, customsWarehouseCode, brandCode);
			//ImOnHand lockedOnHand = null;
			if (declarationOnHands != null && declarationOnHands.size() > 0) {
	        	CmDeclarationOnHand declarationOnHand = declarationOnHands.get(0);

	        	declarationOnHand.setExpiryDate(expiryDate);
				declarationOnHand.setLastUpdateDate(new Date());
				update(declarationOnHand);
			} else {
	        	throw new NoSuchObjectException("查無品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")、關別(" +
	            		customsWarehouseCode + ")的報單庫存資料！");
			}
		} catch (CannotAcquireLockException cale) {
	            throw new FormException("品牌(" + brandCode + ")、報關單號(" + declarationNo + ")、報關項次(" + declarationSeq + ")、海關料號(" + customsItemCode + ")" +
		            "、關別(" + customsWarehouseCode + ")的報單庫存已鎖定，請稍後再試！");
		}
	}
 
    public Date getExpiryDateByImportDate(String declarationNo, Long declarationSeq) throws ValidationErrorException{
	
    	
    	try{
    		log.info("進入方法取得屆期日" + " declarationNo = " + declarationNo + " declarationSeq = " + declarationSeq);
        	
        	Date expiryDate = null; //屆期日期
        	Date importDate = null; //進口日期
        	
        	CmDeclarationView cmDeclarationViewFindByID = null;//存放查詢結果
        	CmDeclarationView cmDeclarationView = new CmDeclarationView();
        	
        	cmDeclarationView.setDeclNo(declarationNo);
        	cmDeclarationView.setItemNo(declarationSeq);
        	
        	cmDeclarationViewFindByID = (CmDeclarationView)cmDeclarationViewDAO.findById("CmDeclarationView", cmDeclarationView);
        
        	if(cmDeclarationViewFindByID == null){
        		throw new ValidationErrorException("查無報關單號(" + declarationNo + ")" + "項次(" + declarationSeq + ")");
        	}
        	
        	importDate = cmDeclarationViewFindByID.getOriginalDate();
        	
        	log.info("importDate = " + importDate);
        	
        	expiryDate = DateUtils.addMonths(importDate , 24); //存倉時間為2年(24個月)
        	
        	log.info("expiryDate = " + expiryDate);
        	
        	return expiryDate;
        	
    	}catch(ValidationErrorException ve){
    		ve.printStackTrace();
    		throw new ValidationErrorException();
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new ValidationErrorException();
    	}
    	
    	
    	
    }
    
    
    
    
}