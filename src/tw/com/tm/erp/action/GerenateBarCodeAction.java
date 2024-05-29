package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuGoalDeployHead;
import tw.com.tm.erp.hbm.bean.BuGoalHead;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.BuGoalService;
import tw.com.tm.erp.hbm.service.GenerateBarCodeService;
import tw.com.tm.erp.hbm.service.ImGeneralAdjustmentService;
import tw.com.tm.erp.hbm.service.ImReceiveAdjustmentService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class GerenateBarCodeAction {
	private static final Log log = LogFactory.getLog(BuGoalAction.class);
	
	private GenerateBarCodeService generateBarCodeService;
    
	
    public void setGenerateBarCodeService(
			GenerateBarCodeService generateBarCodeService) {
		this.generateBarCodeService = generateBarCodeService;
	}
	
	/**
	 * 條碼 初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    returnMap = generateBarCodeService.executeInitial(parameterMap);	    
		}catch (Exception ex) {
		    log.error("執行條碼初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
}
