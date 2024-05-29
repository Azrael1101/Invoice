package tw.com.tm.erp.utils;

import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.WfApprovalResult;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;

public class WfApprovalResultUtils {

    private static final Log log = LogFactory.getLog(WfApprovalResultUtils.class);

    public static void logApprovalResult(Map processInfo, WfApprovalResultService wfApprovalResultService){

	try{
	    Object bean = processInfo.get("entityBean");
	    String orderTypeCode = (String)PropertyUtils.getProperty(bean, "orderTypeCode");
	    String orderNo = (String)PropertyUtils.getProperty(bean, "orderNo");
            WfApprovalResult approvalResult = new WfApprovalResult() ;
	    approvalResult.setProcessId((Long)processInfo.get("processId"));
	    approvalResult.setBrandCode((String)PropertyUtils.getProperty(bean, "brandCode"));
	    approvalResult.setOrderTypeCode(orderTypeCode);
	    approvalResult.setOrderNo(orderNo);
	    approvalResult.setFormName(orderTypeCode + " - "+ orderNo);
	    approvalResult.setActivityId((Long)processInfo.get("activityId"));
	    approvalResult.setActivityName((String)processInfo.get("activityName"));
	    approvalResult.setApprover((String)PropertyUtils.getProperty(bean, "lastUpdatedBy"));
	    approvalResult.setDateTime(new Date());
	    approvalResult.setResult((String)processInfo.get("result"));
	    approvalResult.setApprovalComment((String)processInfo.get("approvalComment"));

	    wfApprovalResultService.save(approvalResult);
	}catch(Exception ex){
	    log.error("存入簽核歷史資料檔時發生錯誤，原因：" + ex.toString());
	}
    }
}
