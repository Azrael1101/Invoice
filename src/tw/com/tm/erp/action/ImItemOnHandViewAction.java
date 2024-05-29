package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.Imformation;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImItemOnHandViewService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class ImItemOnHandViewAction {

    private static final Log log = LogFactory.getLog(ImItemOnHandViewAction.class);

    private ImItemOnHandViewService imItemOnHandViewService;



    public void setImItemOnHandViewService(ImItemOnHandViewService imItemOnHandViewService) {
    	this.imItemOnHandViewService = imItemOnHandViewService;
    }



    /**
     * 商品主檔初始化
     * @param parameterMap
     * @return
     */
    public List<Properties> performInitial(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = imItemOnHandViewService.executeMainInitial(parameterMap);
		} catch (Exception ex) {
		    log.error("執行庫存查詢單初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
		    msgBox.setMessage(ex.getMessage());
		    returnMap.put("vatMessage", msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 綑綁MESSAGE 
     * @param msgBox
     * @param locationBean
     * @param isStartProcess
     */
    /*
    private void wrappedMessageBox(MessageBox msgBox, ImItem imItem, boolean isStartProcess){

	Command cmd_ok = new Command();
	if(isStartProcess){
	    msgBox.setType(MessageBox.CONFIRM);
	    cmd_ok.setCmd(Command.FUNCTION);
	    cmd_ok.setParameters(new String[]{"createRefreshForm('" + imItem.getItemId() + "')", ""});
	    Command cmd_cancel = new Command();
	    cmd_cancel.setCmd(Command.WIN_CLOSE);
	    msgBox.setCancel(cmd_cancel);
	}

	msgBox.setOk(cmd_ok);
    }*/

}
