package tw.com.tm.erp.hbm.dao;

import javax.sql.DataSource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.PosMachineNumberController;
import tw.com.tm.erp.utils.DateUtils;


public class PosMachineNumberControllerDAO extends BaseDAO {

    private static final Log log = LogFactory.getLog(PosMachineNumberControllerDAO.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
    	this.dataSource = dataSource;
    }

    public List<PosMachineNumberController> getAvaliableInvoiceNumber(String posMachineCode,String taxYearMonth,String status) throws Exception {
    	List<PosMachineNumberController> result = new ArrayList<PosMachineNumberController>();
    	try {
    		//Date startDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startDeliveryDate"));
    		StringBuffer hql = new StringBuffer("from PosMachineNumberController as model where model.posMachineCode = ?");
	        hql.append(" and model.taxYearMonth = ?");
	        hql.append(" and model.status = ?");
	        hql.append(" order by model.invoiceSno asc ");
	        result = getHibernateTemplate().find(hql.toString(),new Object[] { posMachineCode, taxYearMonth,  status});
		
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	return (result != null && result.size() > 0 ? result : null);
    }
    
    public PosMachineNumberController getNextInvoiceNumber(String posMachineCode,String taxYearMonth,String status) throws Exception {
    	List<PosMachineNumberController> result = new ArrayList<PosMachineNumberController>();
    	try {
    		//Date startDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startDeliveryDate"));
    		StringBuffer hql = new StringBuffer("from PosMachineNumberController as model where model.posMachineCode = ?");
	        hql.append(" and model.taxYearMonth = ?");
	        hql.append(" and model.status = ?");
	        hql.append(" order by model.invoiceSno asc ");
	        result = getHibernateTemplate().find(hql.toString(),new Object[] { posMachineCode, taxYearMonth,  status});
		
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	return (result != null && result.size() > 0 ? result.get(0) : null);
    }
}