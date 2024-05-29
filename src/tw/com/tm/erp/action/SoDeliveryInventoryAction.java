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
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoDeliveryInventoryHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryInventoryLine;
import tw.com.tm.erp.hbm.service.TmpAjaxSearchDataService;
import tw.com.tm.erp.hbm.service.SoDeliveryInventoryService;

import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;


public class SoDeliveryInventoryAction {

    private static final Log log = LogFactory.getLog(SoDeliveryInventoryAction.class);

	private SiProgramLogAction siProgramLogAction;
	private SoDeliveryInventoryHead soDeliveryInventoryHead;
	private SoDeliveryInventoryLine soDeliveryInventoryLine;
	private TmpAjaxSearchDataService tmpAjaxSearchDataService;
	private SoDeliveryInventoryService soDeliveryInventoryService;
	
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}
	
	public void setSoDeliveryInventoryHead(SoDeliveryInventoryHead soDeliveryInventoryHead) {
		this.soDeliveryInventoryHead = soDeliveryInventoryHead;
	}
	
	public void setSoDeliveryInventoryLine(SoDeliveryInventoryLine soDeliveryInventoryLine) {
		this.soDeliveryInventoryLine = soDeliveryInventoryLine;
	}

	public void setTmpAjaxSearchDataService(TmpAjaxSearchDataService tmpAjaxSearchDataService) {
		this.tmpAjaxSearchDataService = tmpAjaxSearchDataService;
	}

	public void setSoDeliveryInventoryService(SoDeliveryInventoryService soDeliveryInventoryService){
		this.soDeliveryInventoryService = soDeliveryInventoryService;
	}

    /**
	 * 執行入提單交易流程
	 *
	 * @param parameterMap
	 * @return
     * @throws Exception 
	 */
	  /**
	 * 執行調撥單交易流程
	 *
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> performTransaction(Map parameterMap) {
		System.out.println("進入存檔作業~~~~~~~~");
    	Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		String logMessage = new String();
		Long lineHeadId = new Long(0);
		log.info("1.performTransaction");
		try {
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			Long headId = soDeliveryInventoryService.getHeadId(formLinkBean);
			
			String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "status");
			if (!StringUtils.hasText(beforeChangeStatus)) {
				throw new ValidationErrorException("原單據狀態參數為空值，無法執行存檔！");
			}

			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			System.out.println("beforeChangeStatus:" + beforeChangeStatus);
			soDeliveryInventoryService.saveActualOrderNo(headId, employeeCode);
			
			HashMap resultMap = soDeliveryInventoryService.updateAJAX(parameterMap);
			log.info("update Data compile");
			SoDeliveryInventoryHead bean = (SoDeliveryInventoryHead) resultMap.get("entityBean");
			
			log.info("get entityBean to SoDeliveryInventoryHead...");
			returnMap.put("orderNo",bean.getOrderNo());
			log.info("getOrderNo...");

			wrappedMessageBox(msgBox, bean,  false, false);
						
		} catch (FormException fex) {
			msgBox.setMessage("執行入提盤點單存檔時發生錯誤");
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "showMessage()", "" });
			msgBox.setOk(cmd_bf);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("執行入提盤點單存檔時發生錯誤，原因：" + ex.toString());
			log.error("執行入提盤點單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);		
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

	
    /**
	 * 綑綁回前畫面的視窗
	 *
	 * @param msgBox
	 * @param head
	 * @param isStartProcess
	 * @param isExecFunction
	 */
	private void wrappedMessageBox(MessageBox msgBox, SoDeliveryInventoryHead head, boolean isStartProcess, boolean isExecFunction) {
		log.info("wrappedMessageBox...............");
		String orderTypeCode = head.getOrderTypeCode();
		String orderNo = head.getOrderNo();
		String status = head.getStatus();
		String identification = orderTypeCode + "-" + orderNo;

		Command cmd_ok = new Command();
		
		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()",""});
			
			Command cmd_cancel1 = new Command();
			cmd_cancel1.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel1);
		}else{
			if(OrderStatus.COUNT_FINISH.equals(status))
			{
				msgBox.setType(MessageBox.CONFIRM);
				msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "，是否列印報表?");
				cmd_ok.setCmd(Command.FUNCTION);
				cmd_ok.setParameters(new String[] { "openReportWindow('AFTER_SUBMIT')", "" });
				Command cmd_cancel2 = new Command();
				cmd_cancel2.setCmd(Command.FUNCTION);
				cmd_cancel2.setParameters(new String[] { "tempRedirect()", "" });
				msgBox.setCancel(cmd_cancel2);
			}
			else
			{
				msgBox.setMessage(identification + "表單存檔成功！");
				cmd_ok.setCmd(Command.FUNCTION);
				cmd_ok.setParameters(new String[] { "tempRedirect()", "" });
			}
		}

		msgBox.setOk(cmd_ok);
		
		if(isExecFunction){
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[]{"execSubmitBgAction()", ""});
			msgBox.setBefore(cmd_bf);
		}
	}

}

