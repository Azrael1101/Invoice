package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.service.PosControlService;
import tw.com.tm.erp.hbm.service.PosDUService;
import tw.com.tm.erp.utils.AjaxUtils;

public class PosControlAction {
	private static final Log log = LogFactory.getLog(PosDUAction.class);

	    private String programId = "PosDU";
	    
		private String uuId;

		private String ip;
		
	private BuBrandDAO buBrandDAO;
	private PosDUService posDUService;
	private PosControlService posControlService;
	
	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
	    this.buBrandDAO = buBrandDAO;
	}
	
	public void setPosDUService(PosDUService posDUService) {
	    this.posDUService = posDUService;
	}
	
	public void setPosControlService(PosControlService posControlService) {
	    this.posControlService = posControlService;
	}
	
	public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = posDUService.executeInitial(parameterMap);
			List result = buBrandDAO.findAll();
			returnMap.put("bCodeId", AjaxUtils.produceSelectorData(result, "brandCode", "brandCode", true, true ));
		}catch (Exception ex) {
			System.out.println("執行POS_CONTROL作業初始化時發生錯誤，原因：" + ex.getMessage());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage("執行POS_CONTROL作業初始化時發生錯誤，原因：" + ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	public List<Properties> performPosCtrlTransaction(Map parameterMap) {
	  	Map returnMap = new HashMap(0); 
	  	MessageBox msgBox = new MessageBox();
	  	Map resultMap = new HashMap(0); 
	  	try{
	  	    resultMap = posControlService.updatePosCtrl(parameterMap);
	  	}catch(Exception ex){
	  	    System.out.println("=="+ex.toString()+"  "+ex.getStackTrace());
	  	}
	  	msgBox.setMessage((String) resultMap.get("resultMsg"));
	  	returnMap.put("vatMessage", msgBox);
	    return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
}
