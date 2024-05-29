package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.WfApprovalResult;
import tw.com.tm.erp.hbm.dao.WfApprovalResultDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class WfApprovalResultService {

	private static final Log log = LogFactory.getLog(WfApprovalResult.class);


	WfApprovalResultDAO wfApprovalResultDAO;
	public static final String[] GRID_FIELD_NAMES = {
		"activityName","approver","approverName","approverPosition","result","approvalComment","dateTime","id"};

	public static final int[] GRID_FIELD_TYPES = {
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATETIME, AjaxUtils.FIELD_TYPE_LONG};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = {
		"", "", "", "", "", "", "", "0"};

	public void setWfApprovalResultDAO(WfApprovalResultDAO wfApprovalResultDAO) {
		this.wfApprovalResultDAO = wfApprovalResultDAO;
	}

	public List<WfApprovalResult> findApprovalResultByOrderTypeNo(String brandCode, String orderType, String orderNo) {
		try {
			return wfApprovalResultDAO.findApprovalResultByOrderTypeNo(
					brandCode, orderType, orderNo);
		} catch (Exception e) {
			return null;
		}
	}

	public Long getProcessIdByOrderTypeNo(String brandCode, String orderType, String orderNo) {
		try {
			return wfApprovalResultDAO.getProcessIdByOrderTypeNo(brandCode, orderType, orderNo);
		} catch (Exception e) {
			return null;
		}

	}

	public void save(WfApprovalResult transientInstance) {
		wfApprovalResultDAO.save(transientInstance);
	}

	public void delete(WfApprovalResult persistentInstance) {
		wfApprovalResultDAO.delete(persistentInstance);
	}

	public WfApprovalResult findById(java.lang.Long id) {
		return wfApprovalResultDAO.findById(id);
	}

	public List findByExample(WfApprovalResult instance) {
		return wfApprovalResultDAO.findByExample(instance);
	}

	public List findByProperty(String propertyName, Object value) {
		return wfApprovalResultDAO.findByProperty(propertyName, value);
	}

	public List findAll() {
		return wfApprovalResultDAO.findAll();
	}

	public WfApprovalResult merge(WfApprovalResult detachedInstance) {
		return wfApprovalResultDAO.merge(detachedInstance);
	}

	public void attachDirty(WfApprovalResult instance) {
		wfApprovalResultDAO.attachDirty(instance);
	}

	public void attachClean(WfApprovalResult instance) {
		wfApprovalResultDAO.attachClean(instance);
	}

	public List getApproverMail(Long processId) throws Exception {
		try {
			return wfApprovalResultDAO.findApproverMail(processId);
		} catch (Exception ex) {
			log.error("取得簽核人員電子郵件時發生錯誤，原因：" + ex.toString());
			throw new Exception("取得簽核人員電子郵件時發生錯誤，原因：" + ex.getMessage());
		}
	}

	public void saveApprovalResult(HashMap resultMap){
		WfApprovalResult approvalResult = new WfApprovalResult();
		approvalResult.setBrandCode((String) resultMap.get("brandCode"));
		approvalResult.setOrderTypeCode((String) resultMap.get("orderTypeCode"));
		approvalResult.setOrderNo((String) resultMap.get("orderNo"));
		approvalResult.setFormName(approvalResult.getOrderTypeCode()+"-"+ approvalResult.getOrderNo());
		approvalResult.setProcessId((Long) resultMap.get("processId"));
		approvalResult.setActivityId((Long) resultMap.get("activityId"));
		approvalResult.setActivityName((String) resultMap.get("activityName"));
		approvalResult.setApprover((String) resultMap.get("approver"));
		approvalResult.setResult((String) resultMap.get("result"));
		approvalResult.setDateTime(new Date()); // 修改簽核頁簽的顯示時間 by Weichun 2011.02.15
		this.save(approvalResult);
	}

	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{
    	try{
    	    List<Properties> result = new ArrayList();
    	    List<Properties> gridDatas = new ArrayList();

    	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
    	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
    	    //======================帶入Head的值=========================
    	    String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
    	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
    	    String orderNo = httpRequest.getProperty("orderNo");

    	    log.info("wfApprovalResult.brandCode: "+brandCode +",orderTypeCode: "+orderTypeCode+",orderTypeCode: "+orderNo);
    	    Long processId = wfApprovalResultDAO.getProcessIdByOrderTypeNo(brandCode, orderTypeCode, orderNo);
    	    log.info("processId:"+processId);
    	    if(null != processId){
	    	    HashMap map = null;
	    	    List<WfApprovalResult> wfApprovalResults =
	    	    	(List<WfApprovalResult>) wfApprovalResultDAO.findPageLine(processId, iSPage, iPSize).get("form");
	    	    if (wfApprovalResults != null && wfApprovalResults.size() > 0) {
	    	    	log.info("wfApprovalResults.size() = " + wfApprovalResults.size());
	    	    	this.assignApproverInfo(wfApprovalResults);
	    	    	Long firstIndex = Long.valueOf(iSPage * iPSize) + 1;   // 取得第一筆的INDEX
	    	    	log.info("firstIndex = " + firstIndex);
	    	    	Long maxIndex = (Long) wfApprovalResultDAO.findPageLine(processId, 0, 0).get("recordCount"); 	// 取得最後一筆 INDEX
	    	    	log.info("maxIndex = " + maxIndex);
	    	    	log.info("WfApprovalResult.AjaxUtils.getAJAXPageData ");
	    	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,wfApprovalResults, gridDatas, firstIndex, maxIndex));
	    	    }else{
	    	    	log.info("WfApprovalResult.AjaxUtils.getAJAXPageDataDefault ");
	    	    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,gridDatas));
	    	    }

    	    }
    	    return result;
    	}catch(Exception ex){
    	    log.error("載入頁面顯示的簽核明細發生錯誤，原因：" + ex.toString());
    	    throw new Exception("載入頁面顯示的簽核明細失敗！");
    	}
    }

	public List<Properties> updateAJAXPageData(Properties httpRequest) throws Exception{
		try{
			String errorMsg = null;
			return AjaxUtils.getResponseMsg(errorMsg);
		}catch(Exception ex){
			log.error("更新簽核歷程明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新簽核歷程明細失敗！");
		}
	}

	public void assignApproverInfo(List<WfApprovalResult> wfApprovalResults){
		for(WfApprovalResult wfApprovalResult: wfApprovalResults){
			if(StringUtils.hasText(wfApprovalResult.getApprover())){
				BuEmployeeWithAddressView employee = UserUtils.getUserByEmployeeCode(wfApprovalResult.getApprover());
				if(null != employee){
					wfApprovalResult.setApproverName(employee.getChineseName());
					wfApprovalResult.setApproverPosition(employee.getEmployeePosition());
				}
			}
		}
	}

	/**
	 * 取得駁回原因
	 *
	 * @param activityId
	 * @return
	 */
	public WfApprovalResult getRejectReason(Long activityId) {
		WfApprovalResult wfApprovalResult = new WfApprovalResult();
		List<WfApprovalResult> wfApprovalResultList = wfApprovalResultDAO.findByActivityId(activityId);
		if (wfApprovalResultList.size() > 0)
			wfApprovalResult = (WfApprovalResult) wfApprovalResultList.get(wfApprovalResultList.size() - 1);
		return wfApprovalResult;
	}
}
