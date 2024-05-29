package tw.com.tm.erp.hbm.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.TmpImportPosPayment;

public class TmpImportPosPaymentDAO extends BaseDAO{

    private static final Log log = LogFactory.getLog(TmpImportPosPaymentDAO.class);
    
    public List<TmpImportPosPayment> findbyIdentification(Date salesOrderDate, String posMachineCode, String transactionSeq){
	StringBuffer hql = new StringBuffer("from TmpImportPosPayment as model where model.id.salesOrderDate = ? and " +
			"model.id.posMachineCode = ? and model.id.transactionSeqNo = ? order by model.id.paySeq");
        List<TmpImportPosPayment> result = getHibernateTemplate().find(hql.toString(), new Object[]{salesOrderDate, posMachineCode, transactionSeq});
        return result;
    }
    
}