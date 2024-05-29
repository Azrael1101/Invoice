package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiGroup;
import tw.com.tm.erp.hbm.service.BuEmployeeService;
import tw.com.tm.erp.hbm.service.FiBudgetHeadService;
import tw.com.tm.erp.hbm.service.SiGroupService;
import tw.com.tm.erp.hbm.service.BuAddressBookService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;


public class FiBudgetHeadAction {
    
    private static final Log log = LogFactory.getLog(BuEmployeeAction.class);
    FiBudgetHeadService fiBudgetHeadService;
   
	/**
	 * @param fiBudgetHeadService the fiBudgetHeadService to set
	 */
	public void setFiBudgetHeadService(FiBudgetHeadService fiBudgetHeadService) {
		this.fiBudgetHeadService = fiBudgetHeadService;
	}

	public List<Properties> performSearchInitial(Map parameterMap){
		log.info("performInitial");
		Map returnMap = null;	
		try{
		    returnMap = fiBudgetHeadService.executeSearchInitial(parameterMap);			    
		}catch (Exception ex) {
		    log.error("執行員工初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
    
    /**
	 * 綑綁MESSAGE 
	 * @param msgBox
	 * @param 
	 * @param isStartProcess
	 */
	
	private void wrappedMessageBox(MessageBox msgBox, boolean isStartProcess, boolean isExecFunction){

		log.info("wrappedMessageBox................");
		
		Command cmd_ok = new Command();
		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}else{
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}

		msgBox.setOk(cmd_ok);
	}

	
}
