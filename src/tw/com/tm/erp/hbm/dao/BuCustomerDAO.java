package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.BuCustomerCard;
import tw.com.tm.erp.hbm.bean.BuCustomerId;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;

public class BuCustomerDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(BuCustomerDAO.class);

    public BuCustomer findCustomerByAddressBookIdAndBrandCode(
	    Long addressBookId, String brandCode) {

	StringBuffer hql = new StringBuffer("from BuCustomer as model ");
	hql.append("where model.id.brandCode = ? ");
	hql.append("and model.addressBookId = ? ");

	List<BuCustomer> result = getHibernateTemplate().find(hql.toString(),
		new Object[] { brandCode, addressBookId });
	return (result != null && result.size() > 0 ? result.get(0) : null);

    }
    
    public BuCustomer findByCardNo(String cardNo){
	StringBuffer hql = new StringBuffer("from BuCustomerCard as model ");
	hql.append("where model.cardNo = ? ");
	
	List<BuCustomerCard> result = getHibernateTemplate().find(hql.toString(),
		new Object[] { cardNo});
	String customerCode = "";
	if(result != null && result.size()>0){
	   customerCode =  result.get(0).getCustomerCode();
	}
	StringBuffer newHql = new StringBuffer("from BuCustomer as model ");
	newHql.append("where model.id.customerCode = ?");
	List<BuCustomer> resultCust = getHibernateTemplate().find(newHql.toString(),
		new Object[] { customerCode});
	return (resultCust != null && resultCust.size() > 0 ? resultCust.get(0) : null);
	
    }
    public BuCustomer findById(BuCustomerId buCustomerId) {
		log.debug("getting BuCustomer instance with id: " + buCustomerId);
		try {
			BuCustomer instance = (BuCustomer) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuCustomer", buCustomerId);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
    public List<BuCustomer> findBySearchKey(HashMap conditionMap,int iSPageIn,int iPSizeIn) {
    	 final int iSPage = iSPageIn;
		 final int iPSize = iPSizeIn;
		 final String brandCode = (String)conditionMap.get("brandCode");
		 final String customerCode = (String)conditionMap.get("customerCode");
		 final String vipTypeCode = (String)conditionMap.get("vipTypeCode");
		
		 /*
		  final String enable = (String)conditionMap.get("enable");
		 final String identityCode = (String)conditionMap.get("identityCode");
		 final String gender = (String)conditionMap.get("gender");
		 final String vipStartDate = (String)conditionMap.get("vipStartDate");
		 final String vipEndDate = (String)conditionMap.get("vipEndDate");
		 final String applyDate = (String)conditionMap.get("applyDate");
		 final String chineseName = (String)conditionMap.get("chineseName");
		 final String customerEName = (String)conditionMap.get("customerEName");
		 final String birthdayYear = (String)conditionMap.get("birthdayYear");
		 final String birthdayMonth = (String)conditionMap.get("birthdayMonth");
		 final String birthdayDate = (String)conditionMap.get("birthdayDate");
		 final String countryCode = (String)conditionMap.get("countryCode");*/
		 
		 log.info("iSPage:"+iSPageIn);
		 log.info("iPSize:"+iPSizeIn);
		 log.info("brandCode:"+brandCode);
		 log.info("customerCode:"+customerCode);
		 log.info("vipTypeCode:"+vipTypeCode);
/*		 log.info("enable:"+enable);
		 log.info("identityCode:"+identityCode);
 		 log.info("gender:"+gender);
		 log.info("vipStartDate:"+vipStartDate);
		 log.info("vipEndDate:"+vipEndDate);
		 log.info("applyDate:"+applyDate);
		 log.info("chineseName:"+chineseName);
		 log.info("customerEName:"+customerEName);
		 log.info("birthdayYear:"+birthdayYear);
		 log.info("birthdayMonth:"+birthdayMonth);
		 log.info("birthdayDate:"+birthdayDate);
		 log.info("countryCode:"+countryCode);*/
		 
 	
		 List<BuCustomer> result = getHibernateTemplate().executeFind(
	                new HibernateCallback() {
	                    public Object doInHibernate(Session session)
	                            throws HibernateException, SQLException {

	                        StringBuffer hql = new StringBuffer(" select model ");
	                        //hql.append(" from BuCustomer as model,BuAddressBook as model1 ");
	                        hql.append(" from BuCustomer as model ");
	                        hql.append(" where 1=1 ");
	                       // hql.append(" and model.addressBookId = model1.addressBookId ");
	                        
	                        if (StringUtils.hasText(brandCode))
	                        	hql.append(" and model.id.brandCode = :brandCode ");
	                        if (StringUtils.hasText(customerCode))
	                        	hql.append(" and model.id.customerCode = :customerCode " );
	                        if (StringUtils.hasText(vipTypeCode))
	                        	hql.append(" and model.vipTypeCode = :vipTypeCode ");
/*	                        if (StringUtils.hasText(enable))
	                        	hql.append(" and model.enable = :enable ");
	                        if (StringUtils.hasText(identityCode))
	                        	hql.append(" and model1.identityCode = :identityCode ");

	                        if (StringUtils.hasText(gender))
	                        	hql.append(" and model1.gender = :gender ");
	                        if (StringUtils.hasText(vipStartDate))
	                        	hql.append(" and model.vipStartDate = :vipStartDate ");
	                        if (StringUtils.hasText(vipEndDate))
	                        	hql.append(" and model.vipEndDate = :vipEndDate ");
	                        if (StringUtils.hasText(applyDate))
	                        	hql.append(" and model.applyDate = :applyDate ");
	                        if (StringUtils.hasText(chineseName))
	                        	hql.append(" and model1.chineseName = :chineseName ");
	                        if (StringUtils.hasText(customerEName))
	                        	hql.append(" and model1.englishName = :customerEName ");
	                        if (StringUtils.hasText(birthdayYear))
	                        	hql.append(" and model1.birthdayYear = :birthdayYear ");
	                        if (StringUtils.hasText(birthdayMonth))
	                        	hql.append(" and model1.birthdayMonth = :birthdayMonth ");
	                        if (StringUtils.hasText(birthdayDate))
	                        	hql.append(" and model1.birthdayDay = :birthdayDate ");
	                        if (StringUtils.hasText(countryCode))
	                        	hql.append(" and model.countryCode = :countryCode ");*/
	                        hql.append(" order by model.id.customerCode " );
	                        Query query = session.createQuery(hql.toString());
	                        //query.setLockMode("model", LockMode.UPGRADE_NOWAIT);
	                        query.setFirstResult(iSPage*iPSize+1);
	                        query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

	                        if (StringUtils.hasText(brandCode))
	                            query.setString("brandCode", brandCode);
	                        if (StringUtils.hasText(customerCode))
	                            query.setString("customerCode", customerCode);

	                        if (StringUtils.hasText(vipTypeCode))
	                        	query.setString("vipTypeCode", vipTypeCode);
/*	                        if (StringUtils.hasText(enable))
	                            query.setString("enable", enable);
	                        if (StringUtils.hasText(identityCode))
	                        	query.setString("identityCode", identityCode);
	                        if (StringUtils.hasText(gender))
	                        	query.setString("gender", gender);
	                        if (StringUtils.hasText(vipStartDate))
	                        	query.setString("vipStartDate", vipStartDate);
	                        if (StringUtils.hasText(vipEndDate))
	                        	query.setString("vipEndDate", vipEndDate);
	                        if (StringUtils.hasText(applyDate))
	                        	query.setString("applyDate", applyDate);
	                        if (StringUtils.hasText(chineseName))
	                        	query.setString("chineseName", chineseName);
	                        if (StringUtils.hasText(customerEName))
	                        	query.setString("customerEName", customerEName);
	                        if (StringUtils.hasText(birthdayYear))
	                        	query.setString("birthdayYear", birthdayYear);
	                        if (StringUtils.hasText(birthdayMonth))
	                        	query.setString("birthdayMonth", birthdayMonth);
	                        if (StringUtils.hasText(birthdayDate))
	                        	query.setString("birthdayDate", birthdayDate);
	                        if (StringUtils.hasText(countryCode))
	                        	query.setString("countryCode", countryCode);*/
	                        


	                        log.info(hql.toString());
	                        log.info(query.toString());

	                        return query.list();
	                    }
	                });
		 log.info("搜尋結果:"+result.size());
	        return result;
 }
    public Long findBySearchKeyMaxCount(HashMap conditionMap) {
    	
		 final String brandCode = (String)conditionMap.get("brandCode");
		 final String customerCode = (String)conditionMap.get("customerCode");

		 final String vipTypeCode = (String)conditionMap.get("vipTypeCode");
/*
 		 final String enable = (String)conditionMap.get("enable");
		 final String identityCode = (String)conditionMap.get("identityCode");
 		 final String gender = (String)conditionMap.get("gender");
		 final String vipStartDate = (String)conditionMap.get("vipStartDate");
		 final String vipEndDate = (String)conditionMap.get("vipEndDate");
		 final String applyDate = (String)conditionMap.get("applyDate");
		 final String chineseName = (String)conditionMap.get("chineseName");
		 final String customerEName = (String)conditionMap.get("customerEName");
		 final String birthdayYear = (String)conditionMap.get("birthdayYear");
		 final String birthdayMonth = (String)conditionMap.get("birthdayMonth");
		 final String birthdayDate = (String)conditionMap.get("birthdayDate");
		 final String countryCode = (String)conditionMap.get("countryCode");*/
 	
		 List<BuCustomer> result = getHibernateTemplate().executeFind(
	                new HibernateCallback() {
	                    public Object doInHibernate(Session session)
	                            throws HibernateException, SQLException {

	                    	 StringBuffer hql = new StringBuffer(" select model ");
		                        //hql.append(" from BuCustomer as model,BuAddressBook as model1 ");
		                        hql.append(" from BuCustomer as model ");
		                        hql.append(" where 1=1 ");
		                       // hql.append(" and model.addressBookId = model1.addressBookId ");
		                        
		                        if (StringUtils.hasText(brandCode))
		                        	hql.append(" and model.id.brandCode = :brandCode ");
		                        if (StringUtils.hasText(customerCode))
		                        	hql.append(" and model.id.customerCode = :customerCode " );
		                        if (StringUtils.hasText(vipTypeCode))
		                        	hql.append(" and model.vipTypeCode = :vipTypeCode ");
	/*	                        if (StringUtils.hasText(enable))
		                        	hql.append(" and model.enable = :enable ");
		                        if (StringUtils.hasText(identityCode))
		                        	hql.append(" and model1.identityCode = :identityCode ");

		                        if (StringUtils.hasText(gender))
		                        	hql.append(" and model1.gender = :gender ");
		                        if (StringUtils.hasText(vipStartDate))
		                        	hql.append(" and model.vipStartDate = :vipStartDate ");
		                        if (StringUtils.hasText(vipEndDate))
		                        	hql.append(" and model.vipEndDate = :vipEndDate ");
		                        if (StringUtils.hasText(applyDate))
		                        	hql.append(" and model.applyDate = :applyDate ");
		                        if (StringUtils.hasText(chineseName))
		                        	hql.append(" and model1.chineseName = :chineseName ");
		                        if (StringUtils.hasText(customerEName))
		                        	hql.append(" and model1.englishName = :customerEName ");
		                        if (StringUtils.hasText(birthdayYear))
		                        	hql.append(" and model1.birthdayYear = :birthdayYear ");
		                        if (StringUtils.hasText(birthdayMonth))
		                        	hql.append(" and model1.birthdayMonth = :birthdayMonth ");
		                        if (StringUtils.hasText(birthdayDate))
		                        	hql.append(" and model1.birthdayDay = :birthdayDate ");
		                        if (StringUtils.hasText(countryCode))
		                        	hql.append(" and model.countryCode = :countryCode ");*/
		                        hql.append(" order by model.id.customerCode " );
	                        Query query = session.createQuery(hql.toString());
	                        //query.setLockMode("model", LockMode.UPGRADE_NOWAIT);

	                        if (StringUtils.hasText(brandCode))
	                            query.setString("brandCode", brandCode);
	                        if (StringUtils.hasText(customerCode))
	                            query.setString("customerCode", customerCode);
	                        if (StringUtils.hasText(vipTypeCode))
	                        	query.setString("vipTypeCode", vipTypeCode);
/*	                        if (StringUtils.hasText(enable))
	                            query.setString("enable", enable);
	                        if (StringUtils.hasText(identityCode))
	                        	query.setString("identityCode", identityCode);

	                        if (StringUtils.hasText(gender))
	                        	query.setString("gender", gender);
	                        if (StringUtils.hasText(vipStartDate))
	                        	query.setString("vipStartDate", vipStartDate);
	                        if (StringUtils.hasText(vipEndDate))
	                        	query.setString("vipEndDate", vipEndDate);
	                        if (StringUtils.hasText(applyDate))
	                        	query.setString("applyDate", applyDate);
	                        if (StringUtils.hasText(chineseName))
	                        	query.setString("chineseName", chineseName);
	                        if (StringUtils.hasText(customerEName))
	                        	query.setString("customerEName", customerEName);
	                        if (StringUtils.hasText(birthdayYear))
	                        	query.setString("birthdayYear", birthdayYear);
	                        if (StringUtils.hasText(birthdayMonth))
	                        	query.setString("birthdayMonth", birthdayMonth);
	                        if (StringUtils.hasText(birthdayDate))
	                        	query.setString("birthdayDate", birthdayDate);
	                        if (StringUtils.hasText(countryCode))
	                        	query.setString("countryCode", countryCode);*/


	                        log.info(hql.toString());
	                        log.info(query.toString());

	                        return query.list();
	                    }
	                });

	        return result.size()+0L;
 }
}