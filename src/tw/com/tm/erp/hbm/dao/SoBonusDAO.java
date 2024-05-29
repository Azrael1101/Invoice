package tw.com.tm.erp.hbm.dao;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import tw.com.tm.erp.hbm.bean.SoBonus;
import tw.com.tm.erp.hbm.bean.SoBonusLog;
import tw.com.tm.erp.hbm.bean.TmpSoBonus;

public class SoBonusDAO extends BaseDAO {
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(SoBonusDAO.class);
	
	public int deleteTmpSoBonus(String startDate, String endDate){
		return getHibernateTemplate().bulkUpdate("delete TmpSoBonus where shipDate between to_date(?, 'yyyy-MM-dd') and to_date(?, 'yyyy-MM-dd')", new Object[]{startDate, endDate});
	}
	
	public void saveOrUpdateTmpSoBonus(List<TmpSoBonus> tmpSoBonus){
		getHibernateTemplate().setFlushMode(HibernateTemplate.FLUSH_EAGER);
		getHibernateTemplate().saveOrUpdateAll(tmpSoBonus);
	}
	
	public void saveOrUpdateTmpSoBonus(TmpSoBonus tmpSoBonus){
		getHibernateTemplate().setFlushMode(HibernateTemplate.FLUSH_EAGER);
		getHibernateTemplate().saveOrUpdate(tmpSoBonus);
	}
	
	public int update(String hql, Object[] params){
	    	return getHibernateTemplate().bulkUpdate(hql, params);
	}
	
	public List<TmpSoBonus> findTmpSoBonus(String date){
		return getHibernateTemplate().find("from TmpSoBonus where shipDate=to_date(?, 'yyyy-MM-dd')",  new Object[]{date});
	}
	
	public int deleteSoBonus(String year, String month){
		return getHibernateTemplate().bulkUpdate("delete SoBonus where year=? and month=?", new Object[]{year, month});
	}
	
	public List<SoBonus> findAllSoBonus(){
		return find("from SoBonus");
	}
	
	public long getSoBonusLogCount(String yearMonth){
	    Object obj = getHibernateTemplate().find("select count(*) from SoBonusLog where yearMonth=? and status=?", new Object[]{yearMonth, "SAVE"});
	    return obj == null ? 0 : (Long)(((List)obj).get(0));
	}
	
	public long saveSoBonusLog(String yearMonth, String user){
	    SoBonusLog log = new SoBonusLog();
	    log.setYearMonth(yearMonth);
	    log.setStartDate(new Date());
	    log.setCreatedBy(user);
	    log.setStatus("SAVE");
	    getHibernateTemplate().save(log);
	    return log.getId();
	}
	
	public void updateSoBonusLog(long id, String status){
	    String hql = "update SoBonusLog set endDate=? , status=? where id=?";
	    getHibernateTemplate().bulkUpdate(hql, new Object[]{new Date(), status, id});
	}
	
}
