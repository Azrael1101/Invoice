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
import tw.com.tm.erp.hbm.bean.Sale;
import tw.com.tm.erp.utils.DateUtils;


public class SaleDAO extends BaseDAO {

    private static final Log log = LogFactory.getLog(SaleDAO.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
    	this.dataSource = dataSource;
    }

    public List<Sale> getSale() throws Exception {
    	List<Sale> result = new ArrayList<Sale>();
    	try {
    		
    		StringBuffer hql = new StringBuffer("from Sale as model order by model.seqNo asc ");
	        result = getHibernateTemplate().find(hql.toString());
		
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	return (result != null && result.size() > 0 ? result : null);
    }
}