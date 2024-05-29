package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.BuGoalCommissionException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.BuGoalCommissionService;
import tw.com.tm.erp.utils.AjaxUtils;
public class BuGoalCommissionExceptionAction {

	private static final Log log = LogFactory.getLog(BuGoalCommissionExceptionAction.class);
	private BuGoalCommissionService buGoalCommissionService;
	
	public void setBuGoalCommissionService(BuGoalCommissionService buGoalCommissionService){
		this.buGoalCommissionService = buGoalCommissionService;
	}
	
	/*
	private BuGoalCommissionService buGoalCommissionService;
	
	public void setBuGoalCommissionExceptionService(BuGoalCommissionService buGoalCommissionExceptionService){
		this.buGoalCommissionService = buGoalCommissionExceptionService;
	}
	*/
	
	/**
	 * 初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performExInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = buGoalCommissionService.executeExInitial(parameterMap);
		}catch (Exception ex) {
			log.error("執行抽成率初始化時發生錯誤，原因：" + ex.toString());
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
	public List<Properties> performBuGoalCommissionExTransaction(Map parameterMap){ 

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
//			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");     
			//驗證主檔
			buGoalCommissionService.validateHeadEx(parameterMap);
			
			// 驗對則存檔(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			buGoalCommissionService.executeFindActualBuGoalCommissionEx(parameterMap); 

			// 前端資料塞入bean
			buGoalCommissionService.updateBuGoalCommissionExBean(parameterMap);

			// 存檔							
			resultMap = buGoalCommissionService.updateAJAXBuGoalCommissionException(parameterMap);

			BuGoalCommissionException buGoalCommissionException = (BuGoalCommissionException) resultMap.get("entityBean");

			wrappedMessageBox(msgBox, buGoalCommissionException, true);

			msgBox.setMessage((String) resultMap.get("resultMsg"));


		} catch (ProcessFailedException px) {
			log.error("執行維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			log.error("執行維護單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}

		returnMap.put("vatMessage", msgBox);

		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	
	/**
	 * 綑綁MESSAGE 
	 * @param msgBox
	 * @param commissionBean
	 * @param isStartProcess
	 */
	private void wrappedMessageBox(MessageBox msgBox, BuGoalCommissionException goalcommissionBean,
			boolean isStartProcess) {
		Long lineId = goalcommissionBean.getLineId();
		Command cmd_ok = new Command();

		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}else{
			msgBox.setMessage(lineId + " :表單已送出！");
			cmd_ok.setCmd(Command.WIN_CLOSE);	       
		}

		msgBox.setOk(cmd_ok);
	}

}
