package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.BuGoalItemDiscount;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.BuGoalItemDiscountService;
import tw.com.tm.erp.utils.AjaxUtils;

public class BuGoalItemDiscountAction {

	private static final Log log = LogFactory.getLog(BuGoalItemDiscountAction.class);

	private BuGoalItemDiscountService buGoalItemDiscountService;

	public void setBuGoalItemDiscountService(BuGoalItemDiscountService buGoalItemDiscountService){
		this.buGoalItemDiscountService = buGoalItemDiscountService;
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
			returnMap = buGoalItemDiscountService.executeInitial(parameterMap);
		}catch (Exception ex) {
			log.error("執行初始化時發生錯誤，原因：" + ex.toString());
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
	public List<Properties> performBuGoalItemDiscountTransaction(Map parameterMap){ 

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
//			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
//			Object otherBean = parameterMap.get("vatBeanOther");

			// 驗證
			buGoalItemDiscountService.validateHead(parameterMap);

			// 驗對則存檔(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			buGoalItemDiscountService.executeFindActualBuGoalItemDiscount(parameterMap); 

			// 前端資料塞入bean
			buGoalItemDiscountService.updateBuGoalItemDiscountBean(parameterMap);

			// 存檔							
			resultMap = buGoalItemDiscountService.updateAJAXBuGoalItemDiscount(parameterMap);

			BuGoalItemDiscount buGoalItemDiscount = (BuGoalItemDiscount) resultMap.get("entityBean");

			wrappedMessageBox(msgBox, buGoalItemDiscount, true);

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
	private void wrappedMessageBox(MessageBox msgBox, BuGoalItemDiscount goalitemdiscountBean,
			boolean isStartProcess) {
		Long discountId = goalitemdiscountBean.getDiscountId();
		Command cmd_ok = new Command();

		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}else{
			msgBox.setMessage(discountId + "表單已送出！");
			cmd_ok.setCmd(Command.WIN_CLOSE);	       
		}

		msgBox.setOk(cmd_ok);
	}
	
	
	
}
