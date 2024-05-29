package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

//import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BuCompanyDAO;
import tw.com.tm.erp.hbm.service.BuCompanyService;
import tw.com.tm.erp.hbm.service.ImItemCategoryService;
import tw.com.tm.erp.utils.AjaxUtils;


public class BuCompanyAction {	
	private static final Log log = LogFactory.getLog(BuCompanyAction.class);	
	private BuCompanyService buCompanyService;
	private ImItemCategoryService imItemCategoryService;
	public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}
	public void setBuCompanyService(BuCompanyService buCompanyService) {
		this.buCompanyService = buCompanyService;
	}
	/**
	 * 商品類別查詢 初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performItemCategorySearchInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    returnMap = imItemCategoryService.executeSearchInitial(parameterMap);	    
		}catch (Exception ex) {
		    log.error("執行商品類別查詢初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	/**
	 * 公司初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 * 實作List介面的集合物件
	 * 將頁面LIST類別物件資訊放到Map parameterMap中,先在Map returnMap畫出一個位置,
	 * returnMaP的位置 buCompanyService.executeBuCompanyInitial,放著parameterMap的物件
	 * returnMaP畫出空間叫HashMap
	 */ 
	public List<Properties> performBuCompanyInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    /* parameterMap傳入參數到buCompanyService.executeBuCompanyInitial,returnMap物件中  */
		    returnMap = buCompanyService.executeBuCompanyInitial(parameterMap);			    
		}catch (Exception ex) {
		    log.error("執行公司維護存檔初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	/**
	 * 公司送出
	 * @param parameterMap 
	 * 	 * @return
	 * 
	 */
	public List<Properties> performBuCompanyTransaction(Map parameterMap){ 
	    Map returnMap = new HashMap(0); 
	    MessageBox msgBox = new MessageBox();
	    Map resultMap = new HashMap(0); 
	    try {
	    /*********
		buCompanyService.updateBuCompanyBean(parameterMap);
		resultMap = buCompanyService.updateCheckBuCompany(parameterMap);
		resultMap = buCompanyService.updateAJAXBuCompany(parameterMap);
		**********/
		//驗證
		buCompanyService.validateBuCompanyHead(parameterMap);
		// 取得實際主檔
		buCompanyService.executeFindActualBuCompany(parameterMap);
		// 前端資料塞入bean
		buCompanyService.updateBuCompanyBean(parameterMap);
		//存檔
		resultMap = buCompanyService.updateAJAXBuCompany(parameterMap);
		
		wrappedMessageBox(msgBox, true);
		msgBox.setMessage((String) resultMap.get("resultMsg"));
	    } catch (ProcessFailedException px) {
		log.error("執行公司維護單流程時發生錯誤，原因：" + px.toString());
		msgBox.setMessage(px.getMessage());
	    } catch (Exception ex) {
		ex.printStackTrace();
		log.error("執行公司維護單存檔失敗，原因：" + ex.toString());
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
	private void wrappedMessageBox(MessageBox msgBox, boolean isStartProcess){
	    Command cmd_ok = new Command();
	    if(isStartProcess){
		msgBox.setType(MessageBox.CONFIRM);
		cmd_ok.setCmd(Command.FUNCTION);
		cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
		Command cmd_cancel = new Command();
		cmd_cancel.setCmd(Command.WIN_CLOSE);
		msgBox.setCancel(cmd_cancel);
	    }
	    msgBox.setOk(cmd_ok);
	}

}
