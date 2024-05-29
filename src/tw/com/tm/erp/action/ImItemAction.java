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
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImItemService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class ImItemAction {

    private static final Log log = LogFactory.getLog(ImItemAction.class);

    public static final String PROGRAM_ID = "IM_ITEM";

    private ImItemService imItemService;

    private SiProgramLogAction siProgramLogAction;

    private WfApprovalResultService wfApprovalResultService;

    public void setImItemService(ImItemService imItemService) {
	this.imItemService = imItemService;
    }

    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }

    public void setWfApprovalResultService(
	    WfApprovalResultService wfApprovalResultService) {
	this.wfApprovalResultService = wfApprovalResultService;
    }

    /**
     * 商品主檔初始化
     * @param parameterMap
     * @return
     */
    public List<Properties> performInitial(Map parameterMap) {
	Map returnMap = null;
	try {
	    returnMap = imItemService.executeInitial(parameterMap);
	} catch (Exception ex) {
		ex.printStackTrace();
	    log.error("執行商品維護單初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap.put("vatMessage", msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 商品序號初始化
     * @param parameterMap
     * @return
     */
    public List<Properties> performSerialInitial(Map parameterMap) {
	Map returnMap = new HashMap(0);
	try {
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
//	    Object otherBean = parameterMap.get("vatBeanOther");

	    returnMap = imItemService.executeSerialInitial(parameterMap);

	} catch (Exception ex) {
	    log.error("執行商品序號維護存檔失敗，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 商品序號查詢初始化
     * @param parameterMap
     * @return
     */
    public List<Properties> performSerialSearchInitial(Map parameterMap) {
	Map returnMap = new HashMap(0);
	try {
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
//	    Object otherBean = parameterMap.get("vatBeanOther");

	    returnMap = imItemService.executeSerialSearchInitial(parameterMap);

	} catch (Exception ex) {
	    log.error("執行商品序號查詢初始化失敗，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
	
    /**
     * 商品國際碼查詢初始化
     * @param parameterMap
     * @return
     */
    public List<Properties> performEanCodeSearchInitial(Map parameterMap) {
	Map returnMap = new HashMap(0);
	try {
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
//	    Object otherBean = parameterMap.get("vatBeanOther");

	    returnMap = imItemService.executeEanCodeSearchInitial(parameterMap);

	} catch (Exception ex) {
	    log.error(Imformation.valueOf("EANCODE").getActionSearchInitial() + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    public List<Properties> performTransaction(Map parameterMap) {
	Map returnMap = new HashMap(0);
	MessageBox msgBox = new MessageBox();
	Map resultMap = new HashMap(0);
	Long itemId = -1l;
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");

	    resultMap = imItemService.updateAJAXImItem(parameterMap);

//	    String id = (String) PropertyUtils.getProperty(otherBean, "formId");
//	    if (StringUtils.hasText(id)) {
//	    itemId = NumberUtils.getLong(id);
//	    }
//	    imItemService.deleteImItemEanCode(imItemService.findByItemId(itemId));

	    msgBox.setType(MessageBox.ALERT);
	    msgBox.setMessage((String) resultMap.get("resultMsg"));
	    ImItem imItem = (ImItem) resultMap.get("entityBean");
	    Command cmd_ok = new Command();
	    cmd_ok.setCmd(Command.FUNCTION);
	    if (imItem != null) {
		cmd_ok.setParameters(new String[] {
			"showLine('" + imItem.getItemId() + "')", "" });
	    }
	    msgBox.setBefore(cmd_ok);
	} catch (Exception ex) {
	    log.error("執行商品維護存檔失敗，原因：" + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage", msgBox);
	//returnMap.put("vatMessage", resultMap.get("resultMsg"));

	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    public List<Properties> performSerialTransaction(Map parameterMap) {
	Map returnMap = new HashMap(0);
	MessageBox msgBox = new MessageBox();
	try {
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
//	    Object otherBean = parameterMap.get("vatBeanOther");

	    returnMap = imItemService.updateAJAXImItemSerial(parameterMap);

	    msgBox.setMessage((String) returnMap.get("resultMsg") );

	    ImItem imItem = (ImItem)returnMap.get("entityBean");

	    wrappedMessageBox(msgBox, imItem, true);
	} catch (Exception ex) {
	    log.error("執行商品序號維護存檔失敗，原因：" + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	}

	returnMap.put("vatMessage", msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 綑綁MESSAGE 
     * @param msgBox
     * @param locationBean
     * @param isStartProcess
     */
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
    }

}
