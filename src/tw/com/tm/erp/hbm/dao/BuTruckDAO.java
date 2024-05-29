package tw.com.tm.erp.hbm.dao;



import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.BuTruck;
import tw.com.tm.erp.hbm.bean.BuTruckMod;
import tw.com.tm.erp.hbm.bean.ImMovementHead;

public class BuTruckDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(BuTruckDAO.class);
    public BuTruckMod findModById(Long headId)
    {
    	return (BuTruckMod)findByPrimaryKey(BuTruckMod.class, headId);
    }
    public List<BuTruckMod> findModByIdTruckCode(Long headId,String truckCode)
    {	
    	String queryString = "from BuTruckMod  where truckCode = ? and headId = ?";
		List<BuTruckMod> result = getHibernateTemplate().find(queryString, new Object[] { truckCode,headId });
    	return result;
    }
  
	public List<BuTruck> findAllTruckCodeByEnable(String enable) {
		String queryString = "from BuTruck where 1=1 and enable = ? ";
		List<BuTruck> result = getHibernateTemplate().find(queryString, enable);
		return result;
	}
	public BuTruck findByTruckCode(String truckCode) {
		StringBuffer hql = new StringBuffer("from BuTruck where 1=1");
		hql.append(" and enable = ?");
		if(StringUtils.hasText(truckCode))
			hql.append(" and truckCode = ?");
		List<BuTruck> result = getHibernateTemplate().find(hql.toString(), new Object[] { "Y",truckCode });

		return result.size()!=0 && result!= null ?result.get(0):null;
	}
	
    public List<BuTruckMod> findTruckCode(String headId,String truckCode,String truckDriver,String freightName,String status)
    {	
    	StringBuffer hql = new StringBuffer("from BuTruckMod model where 1=1");
    	if(StringUtils.hasText(headId.toString()))
        {
    		hql.append(" and model.headId = '"+headId+"'");
        }
        if(StringUtils.hasText(truckCode))
        {
       	 	hql.append(" and model.truckCode = '"+truckCode+"'");       
        }
        if(StringUtils.hasText(truckDriver))
        {    	 
        	hql.append(" and model.truckDriver = '"+truckDriver+"'");
        }
        if(StringUtils.hasText(freightName))
        {
        	hql.append(" and model.freightName = '"+freightName+"'");
        }
        if(StringUtils.hasText(status))
        {
        	hql.append(" and model.status = '"+status+"'");
        }
        hql.append("order by headId desc");
		List<BuTruckMod> result = getHibernateTemplate().find(hql.toString());
    	return result;
    }
        
}