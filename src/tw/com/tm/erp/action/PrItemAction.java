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
import tw.com.tm.erp.hbm.bean.AdCustServiceHead;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PrItem;
import tw.com.tm.erp.hbm.service.AdCustomerServiceService;
import tw.com.tm.erp.hbm.service.PrItemService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;


public class PrItemAction {

	private static final Log log = LogFactory.getLog(PrItemAction.class);	

	private PrItemService prItemService;

	private WfApprovalResultService wfApprovalResultService;

	public void setPrItemService(PrItemService prItemService) {
		this.prItemService = prItemService;
	}
	/**
	 * 初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = prItemService.executeInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("執行請採商品主檔初始化時發生錯誤，原因：" + ex.toString());  //修正錯誤訊息
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	/**
	 * 
	 * @param parameterMap 
	 * @return
	 */
	public List<Properties> performTransaction(Map parameterMap){ 
		log.info("performTransaction");
		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");

			// 驗證
			prItemService.validateHead(parameterMap);
			// 驗對則存檔(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			prItemService.executeFindActualPrItem(parameterMap);  //在此塞入前端單頭值
			// 前端資料塞入bean
			prItemService.updatePrItemBean(parameterMap);   //沒有功能
			// 存檔							
			resultMap = prItemService.updateAJAXPrItem(parameterMap);
			PrItem prItem = (PrItem) resultMap.get("entityBean");
			wrappedMessageBox(msgBox, prItem, true,false);
			msgBox.setMessage((String) resultMap.get("resultMsg"));
			//executeProcess(otherBean, resultMap, adCustServiceHead, msgBox);

		} catch (ProcessFailedException px) {
			log.error("執行商品主檔時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			log.error("執行商品主檔存檔失敗，原因：" + ex.toString());
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
	 * @param isExecFunction 
	 */
	private void wrappedMessageBox(MessageBox msgBox, PrItem prItem, boolean isStartProcess, boolean isExecFunction){

		Long headId = prItem.getItemId();

		Command cmd_ok = new Command();
		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}else{
			msgBox.setMessage(headId + "表單已送出！");
			cmd_ok.setCmd(Command.WIN_CLOSE);	       
		}
		msgBox.setOk(cmd_ok);
	}
}




