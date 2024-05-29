package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.ImItemCategoryLevel;
import tw.com.tm.erp.hbm.bean.ImItemCategoryNode;
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.hbm.bean.SiMenuCtrl;
import tw.com.tm.erp.hbm.dao.SiMenuDAO;
import tw.com.tm.erp.utils.AjaxUtils;

public class SiMenuMainService {
	private static final Log log = LogFactory.getLog(SiMenuMainService.class);
	
	private SiMenuDAO siMenuDAO;
	
	public void setSiMenuDAO(SiMenuDAO siMenuDAO) {
		this.siMenuDAO = siMenuDAO;
	}
	
	public static final String[] GRID_FIELD_NAMES = { 
	        "menuId", "name"};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = {
	        "", ""};

	public Map executeInitial(Map parameterMap) throws Exception {
		log.info("executeInitial...");
		
		HashMap resultMap = new HashMap();
		HashMap multiList = new HashMap();
		
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			
			List<SiMenuCtrl> allMenus = this.findByParentMenuId("000000000000");
			
			if(allMenus != null) {
				log.info(allMenus.size());
				multiList.put("allMenus", AjaxUtils.produceSelectorData(allMenus, "menuId",
						"name", true, true));
				resultMap.put("multiList", multiList);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Menu選單查詢作業初始化失敗，原因：" + ex.toString());
			
			throw new Exception("Menu選單查詢作業初始化失敗，原因：" + ex.toString());
		}
		return resultMap;
	}
	
	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{
		log.info("getAJAXPageData...");
		
		try {
			List<Properties> result = new ArrayList();
		    List<Properties> gridDatas = new ArrayList();
		    List<SiMenuCtrl> siMenuList = null;
		    
		    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
		    
		    String parentMenuId = httpRequest.getProperty("menuId");
		    
		    if(StringUtils.hasText(parentMenuId)) {
		    	siMenuList = this.findSearchPageLine(parentMenuId, iSPage, iPSize);
		    }
			
		    if(siMenuList != null && siMenuList.size() != 0) {
		    	// 取得第一筆的INDEX
				Long firstIndex = 1L;
				// 取得最後一筆 INDEX
				Long maxIndex = this.findSearchPageLineMaxIndex(parentMenuId);
		    	
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, siMenuList, gridDatas,
						firstIndex, maxIndex));	
		    }else{
		    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));   
		    }
		    
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
		    log.error("載入頁面顯示的通道明細明細發生錯誤，原因：" + ex.toString());
		    throw new Exception("載入頁面顯示的通道明細失敗！");
		}
	}
	
	public List<Properties> saveSearchResult(Properties httpRequest)throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	
	public List<SiMenuCtrl> findByParentMenuId(String parentMenuId){
		return siMenuDAO.findByParentMenuId(parentMenuId);
	}
	
	public List<SiMenuCtrl> findSearchPageLine(String parentMenuId, int iSPage, int iPSize){
		return siMenuDAO.findSearchPageLine(parentMenuId, iSPage, iPSize);
	}
	
	public Long findSearchPageLineMaxIndex(String parentMenuId) {
		return siMenuDAO.findSearchPageLineMaxIndex(parentMenuId);
	}

}
