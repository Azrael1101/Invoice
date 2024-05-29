package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.SiGroupService;
import tw.com.tm.erp.utils.AjaxUtils;

public class SiGroupAction {
    private static final Log log = LogFactory.getLog(SiGroupAction.class);
    private SiGroupService siGroupService;
    
    public void setSiGroupService(SiGroupService siGroupService) {
    	this.siGroupService = siGroupService;
    }
    
    public List<Properties> performInitial(Map parameterMap){
    	Map returnMap = new HashMap();	
		try{
		    Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			returnMap.put("brandCode", brandCode);
			returnMap.put("createdBy", loginEmployeeCode);
		}
		catch(Exception ex){
		    log.error("執行選單群組維護初始化時發生錯誤，原因：" + ex.toString());  //修正錯誤訊息
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
  			Object formBindBean = parameterMap.get("vatBeanFormBind");
  			//驗證輸入參數...
  			String brandCode = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
			if(brandCode == null || brandCode.trim().length() == 0){
			    msgBox.setMessage("請輸入品牌!");
			    returnMap.put("vatMessage" ,msgBox);
			    return AjaxUtils.parseReturnDataToJSON(returnMap);
			}
			String groupCode = (String)PropertyUtils.getProperty(formBindBean, "groupCode");
			if(groupCode == null || groupCode.trim().length() == 0){
			    msgBox.setMessage("請輸入群組代號!");
			    returnMap.put("vatMessage" ,msgBox);
			    return AjaxUtils.parseReturnDataToJSON(returnMap);
			}
			String groupName = (String)PropertyUtils.getProperty(formBindBean, "groupName");
			if(groupName == null || groupName.trim().length() == 0){
			    msgBox.setMessage("請輸入群組名稱!");
			    returnMap.put("vatMessage" ,msgBox);
			    return AjaxUtils.parseReturnDataToJSON(returnMap);
			}
			//寫入資料...
  			HashMap resultMap = siGroupService.updateAJAXMovement(parameterMap);
  			msgBox.setMessage((String)resultMap.get("resultMsg"));
  			msgBox.setType(MessageBox.ALERT);
  			Command cmd_ok = new Command();
  			cmd_ok.setCmd(Command.FUNCTION);
  			//按確定執行
  			cmd_ok.setParameters(new String[]{"kweInitialChild()"});
  			msgBox.setOk(cmd_ok);
  		} 
  		catch(ProcessFailedException px) {
  			log.error("執行選單群組時發生錯誤，原因：" + px.toString());
  			msgBox.setMessage(px.getMessage());
  		} 
  		catch(Exception ex) {
  			log.error("執行選單群組存檔失敗，原因：" + ex.toString());
  			msgBox.setMessage(ex.getMessage());
  		}
  		returnMap.put("vatMessage", msgBox);
  		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    public List<Properties> performSearchSelection(Map parameterMap){
		log.info("performSearchSelection");
		Map returnMap = null;	
		try{
			return siGroupService.getSearchSelection(parameterMap);	    
		}catch (Exception ex) {
			log.info("執行選單群組檢視時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
    
    //新增權限送出(wade)
    public List<Properties> performKwePrivilegeTransaction(Map parameterMap){
    	Map returnMap = new HashMap(0);
  		MessageBox msgBox = new MessageBox();
  		try {
	    	Object formBindBean = parameterMap.get("vatBeanFormBind");
	    	String applicant = (String)PropertyUtils.getProperty(formBindBean, "applicant");//申請人
	    	if(applicant == null || applicant.trim().length() == 0){
			    msgBox.setMessage("請選擇申請人!");
			    returnMap.put("vatMessage" ,msgBox);
			    return AjaxUtils.parseReturnDataToJSON(returnMap);
			}
	    	Object otherBean = parameterMap.get("vatBeanOther");
	    	String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");//登入工號
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");//品牌
			
			//新增群組名稱
			siGroupService.saveSiGroup(brandCode, applicant, employeeCode);
			//刪除SI_GROUP_MENU中enable為N的項目
			siGroupService.deleteUnableSiGroupMenu(brandCode, applicant);
			//加SI_USERS_GROUP
			siGroupService.saveOrUpdateSiUsersGroup(applicant, brandCode, applicant, employeeCode);
  		}
  		catch(Exception ex){
  			log.error("執行選單群組存檔失敗，原因：" + ex.toString());
  			msgBox.setMessage(ex.getMessage());
  		}
  		msgBox.setMessage("Kwe權限申請完成!");
  		returnMap.put("vatMessage", msgBox);
  		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
}
