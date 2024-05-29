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
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;

public class BuCommonPhraseAction {
    
    private static final Log log = LogFactory.getLog(BuCommonPhraseAction.class);
    
    private BuCommonPhraseService buCommonPhraseService;
    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    private WfApprovalResultService wfApprovalResultService;
    
    public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
    	this.buCommonPhraseService = buCommonPhraseService;
    }
    
    public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
    	this.wfApprovalResultService = wfApprovalResultService;
    }
    
    public void setBuCommonPhraseLineDAO(
    	    BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
    	this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    }
        

    
	public List<Properties> performMainInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = buCommonPhraseService.executeInitial(parameterMap);	
			
		}catch (Exception ex) {
			log.error("執行地點維護存檔初始化時發生錯誤，原因：" + ex.toString());  //修正錯誤訊息
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
    public List<Properties> performTransaction(Map parameterMap){
     	Map returnMap = new HashMap(0);
  		MessageBox msgBox = new MessageBox();
  		try {
	    	Object otherBean = parameterMap.get("vatBeanOther");
	    	//System.out.println("淫蕩的formID="+(String)PropertyUtils.getProperty(otherBean, "formId"));	    	
  			HashMap resultMap = buCommonPhraseService.updateAJAXMovement(parameterMap);
   			BuCommonPhraseHead bean = (BuCommonPhraseHead)resultMap.get("entityBean");
   			returnMap.put("formId" , bean.getHeadCode());
   			
  			msgBox.setMessage((String) resultMap.get("resultMsg") + ",是否繼續新增Line？");
  			//這裡設置執行後的ALERT 訊息
  			//設置則為CONFIRM...
  	    msgBox.setType(MessageBox.CONFIRM);
  	    
        Command cmd_ok = new Command();
        Command cmd_cancel = new Command();
        cmd_ok.setCmd(Command.FUNCTION);
        //按確定執行
  	    cmd_ok.setParameters(new String[]{"kweInitialChild()"});
  	    msgBox.setOk(cmd_ok);
  	    //按取消執行
  	    cmd_cancel.setCmd(Command.WIN_CLOSE);
  	    msgBox.setCancel(cmd_cancel);
  	    
  		} catch (ProcessFailedException px) {
  			log.error("執行調發單流程時發生錯誤，原因：" + px.toString());
  			msgBox.setMessage(px.getMessage());
  		} catch (Exception ex) {
  			log.error("執行調發單存檔失敗，原因：" + ex.toString());
  			msgBox.setMessage(ex.getMessage());
  		}
  		returnMap.put("vatMessage", msgBox);
  		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    public List<Properties> performMainTransaction(Map parameterMap){

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
		
			Object otherBean = parameterMap.get("vatBeanOther");
	

			// 驗證 把畫面的值 存成BEAN 驗證
		//	buShopMainService.validateHead(parameterMap);

			// 驗對則存到資料庫(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			buCommonPhraseService.executeFindActualCommonPhraseHead(parameterMap); 

			// 前端資料塞入bean 
			buCommonPhraseService.updateBean(parameterMap);

									
			// 存檔  改狀態
			resultMap = buCommonPhraseService.updateAJAXMaster(parameterMap);

			wrappedMessageBox(msgBox, true);

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

	private void wrappedMessageBox(MessageBox msgBox, boolean isStartProcess){

		Command cmd_ok = new Command();
		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", "","","",""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}

		msgBox.setOk(cmd_ok);
	}
    /*
    private void executeProcess(Object otherBean, BuCommonPhraseHead bean, MessageBox msgBox) throws Exception {
    	HashMap resultMap = new HashMap(0);    
    	Object[] processResult = new Object[3];
        String assignId = (String)PropertyUtils.getProperty(otherBean, "assignmentId");
        String procId = (String)PropertyUtils.getProperty(otherBean, "processId");
        String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
        String result = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
        String originalStatus = (String)PropertyUtils.getProperty(otherBean, "status");
        Boolean approvalResult = true;
        String resultMessage = new String("");
        try{
	        if(!StringUtils.hasText(procId)&& 
	        		(OrderStatus.SIGNING.equals(bean.getStatus())||OrderStatus.SAVE.equals(bean.getStatus()))){
	        	processResult = ImMovementService.startProcess(bean);
	        	resultMessage = "送出";
	        	wrappedMessageBox(msgBox, bean, true);
	        }else{
			    if(StringUtils.hasText(procId) && StringUtils.hasText(assignId)&& StringUtils.hasText(result)){
		            Long assignmentId = NumberUtils.getLong(assignId);
		            //Boolean approvalResult = Boolean.valueOf(result);
		            processResult = ImMovementService.completeAssignment(assignmentId, true); ////TODO 
		            if (OrderStatus.REJECT.equals(originalStatus))
		            	resultMessage = "送出";
		            else	
		            	resultMessage = (approvalResult?"核准":"駁回");
		            wrappedMessageBox(msgBox, bean, false);
			    }else{
			    	throw new ProcessFailedException("Complete assignment時發生錯誤,ProcessId="
			    			+ procId +",AssignmentId="+ assignId +",result="+result);
			    }
	        }
		    resultMap.put("brandCode"    , bean.getBrandCode());
		    resultMap.put("orderTypeCode", bean.getOrderTypeCode());
		    resultMap.put("orderNo"      , bean.getOrderNo());
		    resultMap.put("processId"    , processResult[0]);
		    resultMap.put("activityId"   , processResult[1]);
		    resultMap.put("activityName" , processResult[2]);
		    resultMap.put("approver"     , loginEmployeeCode);
		    resultMap.put("result"       , resultMessage);
		    wfApprovalResultService.saveApprovalResult(resultMap);
		   
        }catch(Exception pe){
        	log.error("調撥單流程執行時發生錯誤，原因：" + pe.toString());
			throw new Exception(pe.getMessage());
        }	
    }
    
    private void wrappedMessageBox(MessageBox msgBox, BuCommonPhraseHead bean, boolean isStartProcess){
    	
    	String orderTypeCode = bean.getOrderTypeCode();
    	String orderNo = bean.getOrderNo();
    	String status = bean.getStatus();
    	String identification = orderTypeCode + "-" + orderNo;
        Command cmd_ok = new Command();
    	if(isStartProcess){
    	    msgBox.setType(MessageBox.CONFIRM);
    	    cmd_ok.setCmd(Command.FUNCTION);
    	    cmd_ok.setParameters(new String[]{"kweFormClear", ""});
    	    Command cmd_cancel = new Command();
    	    cmd_cancel.setCmd(Command.WIN_CLOSE);
    	    msgBox.setCancel(cmd_cancel);
    	}else{
    	    if(OrderStatus.SIGNING.equals(status)){
                    msgBox.setMessage(identification + "表單已送出！");
    	    }else{
    	        msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！");
    	    }
    	    cmd_ok.setCmd(Command.WIN_CLOSE);	       
    	}
    	
    	msgBox.setOk(cmd_ok);
    }
      */  
    /**
	 * 初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = buCommonPhraseService.executeInitialForMarquee(parameterMap);	
			
		}catch (Exception ex) {
			log.error("執行地點維護存檔初始化時發生錯誤，原因：" + ex.toString());  //修正錯誤訊息
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	public List<Properties> performMarqueeTransaction(Map parameterMap){
     	Map returnMap = new HashMap(0);
  		MessageBox msgBox = new MessageBox();
  		try {
	    	
	    	buCommonPhraseService.updateMarqueeBean(parameterMap);    	
	    	buCommonPhraseService.updateMarquee(parameterMap);
   			
   			
  			
  		} catch (ProcessFailedException px) {
  			log.error("執行調發單流程時發生錯誤，原因：" + px.toString());
  			msgBox.setMessage(px.getMessage());
  		} catch (Exception ex) {
  			log.error("執行調發單存檔失敗，原因：" + ex.toString());
  			msgBox.setMessage(ex.getMessage());
  		}
  		
  		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
}
