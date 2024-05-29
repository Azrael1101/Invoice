package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImItemCategoryLevelService;
import tw.com.tm.erp.utils.AjaxUtils;

public class ImItemCategoryLevelAction {
	private static final Log log = LogFactory.getLog(ImItemCategoryLevelAction.class);
	
	private ImItemCategoryLevelService imItemCategoryLevelService;
	
	public void setImItemCategoryLevelService(ImItemCategoryLevelService imItemCategoryLevelService) {
		this.imItemCategoryLevelService = imItemCategoryLevelService;
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
			returnMap = imItemCategoryLevelService.executeInitial(parameterMap);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("執行商品分類結構查詢作業初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap.put("vatMessage", msgBox);
		}

		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

}
