package tw.com.tm.erp.hbm.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.TmpImportPosItem;

public class TmpImportPosItemDAO extends BaseDAO{

    private static final Log log = LogFactory.getLog(TmpImportPosItemDAO.class);
    
    public List<TmpImportPosItem> getTmpImportPosItem(){
	StringBuffer hql = new StringBuffer("from TmpImportPosItem");
        List<TmpImportPosItem> result = getHibernateTemplate().find(hql.toString());
        return result;
    }
    
    public List<TmpImportPosItem> findbyIdentification(Date salesOrderDate, String posMachineCode, String transactionSeq){
	StringBuffer hql = new StringBuffer("from TmpImportPosItem as model where model.id.salesOrderDate = ? and " +
			"model.id.posMachineCode = ? and model.id.transactionSeqNo = ? order by model.id.fileId, model.id.seq");
        List<TmpImportPosItem> result = getHibernateTemplate().find(hql.toString(), new Object[]{salesOrderDate, posMachineCode, transactionSeq});
        return result;
    }
}