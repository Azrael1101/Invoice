package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImReceiveExpenseMod;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImReceiveExpenseMainService;
import tw.com.tm.erp.utils.AjaxUtils;

public class ImReceiveExpenseAction {
	private static final Log log = LogFactory.getLog(ImReceiveExpenseAction.class);
	
	private ImReceiveExpenseMainService imReceiveExpenseMainService;
	
	public void setImReceiveExpenseMainService(ImReceiveExpenseMainService imReceiveExpenseMainService){
		this.imReceiveExpenseMainService = imReceiveExpenseMainService;
	}
	
	/** 初始化, 取得各項下拉選單項目
	 * @param parameterMap
	 * @return List<Properties>
	 */
	 public List<Properties> performInitial(Map parameterMap){
		 log.info("performInitial");
		 Map returnMap = null;	
		 try{
			 returnMap = imReceiveExpenseMainService.executeInitial(parameterMap);
		 }catch (Exception ex) {
			 log.info("執行進貨費用單初始化時發生錯誤，原因：" + ex.toString());
			 MessageBox msgBox = new MessageBox();
			 msgBox.setMessage(ex.getMessage());
			 returnMap = new HashMap();
			 returnMap.put("vatMessage" ,msgBox);
		 }
		 return AjaxUtils.parseReturnDataToJSON(returnMap);	
	 }
	 
	 public List<Properties> performTransaction (Map parameterMap){
		 log.info("performTransaction");
		 Map returnMap = new HashMap(0); 
			MessageBox msgBox = new MessageBox();
			Map resultMap = new HashMap(0); 
			try {  
				
				log.info("aaa");
				// 驗證 把畫面的值 存成BEAN 驗證
				imReceiveExpenseMainService.validateHead(parameterMap);
				log.info("bbb");
				// 驗對則存到資料庫(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
				imReceiveExpenseMainService.executeFindActual(parameterMap); 
				log.info("ccc");
				// 前端資料塞入bean 
				imReceiveExpenseMainService.updateExpenseModBean(parameterMap);
				log.info("ddd");
										
				// 存檔  改狀態
				resultMap = imReceiveExpenseMainService.updateAJAXExpenseMod(parameterMap);

				ImReceiveExpenseMod imReceiveExpenseMod = (ImReceiveExpenseMod) resultMap.get("entityBean");
				
				wrappedMessageBox(msgBox, imReceiveExpenseMainService, true , false);
				log.info("eeeeeeeeeee");
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
	 private void wrappedMessageBox(MessageBox msgBox,ImReceiveExpenseMainService imReceiveExpenseMainService , boolean isStartProcess, boolean isExecFunction){

			log.info("wrappedMessageBox...............");
//			String supplierCode = buSupplierMod.getSupplierCode();
//			String supplierTypeCode = buSupplierMod.getSupplierTypeCode();
//			String status = buSupplierMod.getStatus();
//			String identification = orderTypeCode + "-" + orderNo;
			
			
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
