package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.SiMenuMainService;
import tw.com.tm.erp.utils.AjaxUtils;

public class SiMenuMainAction {
	private static final Log log = LogFactory.getLog(SiMenuMainAction.class);
	
	private SiMenuMainService siMenuMainService;

	public void setSiMenuMainService(SiMenuMainService siMenuMainService) {
		this.siMenuMainService = siMenuMainService;
	}
	
	/**
	 * 
	 * 查詢初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performInitial(Map parameterMap) {
		log.info("performInitial...");
		Map returnMap = new HashMap(0);

		try {
			returnMap = siMenuMainService.executeInitial(parameterMap);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap.put("vatMessage", msgBox);
		}

		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	

}
