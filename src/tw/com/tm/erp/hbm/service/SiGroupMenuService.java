package tw.com.tm.erp.hbm.service;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.hbm.bean.SiGroupMenu;
import tw.com.tm.erp.hbm.bean.SiGroupMenuId;
import tw.com.tm.erp.hbm.dao.SiGroupMenuDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class SiGroupMenuService {
    private static final Log log = LogFactory.getLog(SiGroupMenuService.class);
    private SiGroupMenuDAO siGroupMenuDAO;
    
    // cache all common phrase data

    public void setSiGroupMenuDAO(SiGroupMenuDAO siGroupMenuDAO) {
    	this.siGroupMenuDAO = siGroupMenuDAO;
    }
    
  	/**
  	 * LINE 頁面顯示
  	 * 
  	 * @param headId
  	 * @param startPage
  	 * @param pageSize
  	 * @return
  	 */
  	public List<SiGroupMenu> findPageLine(String brandCode , String groupCode , int startPage, int pageSize) {
  		log.info("SiGroupMenuService.findPageLine brandCode=" + brandCode + "groupCode="+ groupCode + "startPage=" + startPage + "pageSize" + pageSize);
  		return siGroupMenuDAO.findPageLine(brandCode,groupCode,startPage,pageSize);
  	}
    
  	/**
  	 * 取得GRID最後一筆
  	 * 
  	 * @param headId
  	 * @return
  	 */
  	public Long findPageLineMaxIndex(String brandCode , String  groupCode) {
  		log.info("ImPriceAdjustmentDAO.findPageLineMaxIndex");
  		return siGroupMenuDAO.findPageLineMaxIndex(brandCode,groupCode);
  	}
  	
  	public static final String[] GRID_FIELD_NAMES = {"menuId", "indexNo", "name", "enable"};
  	
  	
  	public List<Properties> saveAJAXPageLinesData(Properties httpRequest) throws Exception {
  		try{
  			String errorMsg = null;
  			String applicant = httpRequest.getProperty("applicant");// 申請人(groupCode)
  			if(applicant == null || applicant.trim().length() == 0)
  				return AjaxUtils.getResponseMsg("請選擇申請人!");
  			
  			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
  			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
  			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
  			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
		    String employeeCode = httpRequest.getProperty("employeeCode");// 員工代號
		    
  			// 將STRING資料轉成List Properties record data
  			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
  			// Get INDEX NO
	  		if(upRecords != null){
	  			List<Long> ids = siGroupMenuDAO.getMenuIds(brandCode, applicant);
		  		for(Properties upRecord : upRecords){
			  		// 先載入HEAD_ID OR LINE DATA
			  		Long menuId = NumberUtils.getLong(upRecord.getProperty("menuId"));
			  		Long indexNo = NumberUtils.getLong(upRecord.getProperty("indexNo"));
		  			String enable = upRecord.getProperty("enable");
		  			Date today = new Date();
			  		//String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);
			  		if(!ids.contains(menuId)){
			  			SiGroupMenu siGroupMenu = new SiGroupMenu();
			  			SiGroupMenuId siGroupMenuId = new SiGroupMenuId(brandCode, applicant, menuId);
			  			siGroupMenu.setId(siGroupMenuId);
			  			siGroupMenu.setEnable(enable);
			  			siGroupMenu.setIndexNo(indexNo);
			  			siGroupMenu.setCreatedBy(employeeCode);
			  			siGroupMenu.setCreationDate(today);
			  			siGroupMenu.setUpdatedBy(employeeCode);
			  			siGroupMenu.setUpdateDate(today);
			  			siGroupMenuDAO.save(siGroupMenu);
			  		}
			  		else{
			  			siGroupMenuDAO.update("update SiGroupMenu set enable=?, updatedBy=?, updateDate=? where id.brandCode=? and id.groupCode=? and id.menuId=? ", 
			  					new Object[]{enable, employeeCode, today, brandCode, applicant, menuId});
			  		}
		  		}
	  		}
  		    return AjaxUtils.getResponseMsg(errorMsg);
  		} 
  		catch(Exception ex){
  		    log.error("更新促銷商品明細時發生錯誤，原因：" + ex.toString());
  		    throw new Exception("更新促銷商品明細失敗！");
  		}
  	}
  	
  	
}
