package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.TmpAjaxSearchData;
import tw.com.tm.erp.hbm.bean.TmpImportPosItem;
import tw.com.tm.erp.hbm.bean.TmpImportPosPayment;
import tw.com.tm.erp.hbm.dao.TmpAjaxSearchDataDAO;
import tw.com.tm.erp.hbm.dao.TmpImportPosItemDAO;
import tw.com.tm.erp.hbm.dao.TmpImportPosPaymentDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;

public class TmpImportPosPaymentService {

    private TmpImportPosPaymentDAO tmpImportPosPaymentDAO;
    private static final Log log = LogFactory
	    .getLog(TmpImportPosPaymentService.class);

    public void setTmpImportPosPaymentDAO(TmpImportPosPaymentDAO tmpImportPosPaymentDAO) {
	this.tmpImportPosPaymentDAO = tmpImportPosPaymentDAO;
    }

    public void savePosPayment(TmpImportPosPayment posPayment) {
	tmpImportPosPaymentDAO.save(posPayment);
    }

    /**
     * 刪除銷貨日期及POS機碼相符的資料
     * 
     * @param salesDate
     * @param posMachineCode
     * @throws Exception
     */
    public void deletePosPaymentByIdentification(Date salesDate,
	    String posMachineCode) throws Exception {

	try {
	    List tmpImportPosPayments = tmpImportPosPaymentDAO.findByProperty("TmpImportPosPayment", 
		    new String[] { "id.salesOrderDate","id.posMachineCode" }, new Object[] { salesDate, posMachineCode });
	    if (tmpImportPosPayments != null && tmpImportPosPayments.size() > 0) {
		for (int i = 0; i < tmpImportPosPayments.size(); i++) {
		    tmpImportPosPaymentDAO.delete(tmpImportPosPayments.get(i));
		}
	    }
	} catch (Exception ex) {
	    log.error("依據銷貨日期(" + DateUtils.format(salesDate,DateUtils.C_DATA_PATTON_YYYYMMDD) + ")、POS機碼("
		    + posMachineCode + ")刪除POS付款明細檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據銷貨日期(" + DateUtils.format(salesDate, DateUtils.C_DATA_PATTON_YYYYMMDD) + ")、POS機碼("
		    + posMachineCode + ")刪除POS付款明細檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
}