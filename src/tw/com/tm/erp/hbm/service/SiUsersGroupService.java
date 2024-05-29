package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrandId;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiFunction;
import tw.com.tm.erp.hbm.bean.SiGroup;
import tw.com.tm.erp.hbm.bean.SiGroupId;
import tw.com.tm.erp.hbm.bean.SiGroupMenu;
import tw.com.tm.erp.hbm.dao.BuEmployeeBrandDAO;
import tw.com.tm.erp.hbm.dao.SiGroupDAO;
import tw.com.tm.erp.hbm.dao.SiUsersGroupDAO;
import tw.com.tm.erp.hbm.bean.SiUsersGroup;
import tw.com.tm.erp.hbm.bean.SiUsersGroupId;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class SiUsersGroupService {

    private static final Log log = LogFactory.getLog(SiUsersGroupService.class);

    private BuEmployeeWithAddressViewService buEmployeeWithAddressViewService;
    private SiUsersGroupDAO siUsersGroupDAO;

  	public static final String[] GRID_FIELD_NAMES = { 
  		"indexNo","lineId","employeeCode","employeeName",
  		"reserve1", "reserve2", "reserve3", "reserve4", "reserve5",
  		"createdBy", "creationDate", "updatedBy", "udpateDate",
  		"isDeleteRecord","isLockRecord","message","showStatus"};
  	
    public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
    	"", "", "", "",
			"", "", "", "", "",  
			"", "", "", "", 
			"0", "0", "", ""};
    
    public static final int[] GRID_FIELD_TYPES = { 
    	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_LONG,		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,  
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, 	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, 
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};
  	
    // cache all common phrase data
  	
  	public void setSiUsersGroupDAO(SiUsersGroupDAO siUsersGroupDAO) {
  		this.siUsersGroupDAO = siUsersGroupDAO;
  	}

  	public void setBuEmployeeWithAddressViewService(
  			BuEmployeeWithAddressViewService buEmployeeWithAddressViewService) {
  		this.buEmployeeWithAddressViewService = buEmployeeWithAddressViewService;
  	}
  	
	/**
	 * AJAX Load Page Data
	 * 
	 * @param headObj
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<Properties> getAJAXPageData(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException ,Exception{

		String brandCode = httpRequest.getProperty("brandCode");
		String groupCode = httpRequest.getProperty("groupCode");
		List<Properties> re = new ArrayList();
		List<Properties> gridDatas = new ArrayList();
		int iSPage = AjaxUtils.getStartPage(httpRequest);
		int iPSize = AjaxUtils.getPageSize(httpRequest);

		log.info("SiUsersGroupService.getAJAXPageData groupCode=" + groupCode + ",iSPage=" + iSPage + ",iPSize=" + iPSize);

		List<SiUsersGroup> siUsersGroups =  siUsersGroupDAO.findPageLine(brandCode,groupCode,iSPage,iPSize);

		if (null != siUsersGroups && siUsersGroups.size() > 0) {
			log.info("SiUsersGroupService.getAJAXPageData AjaxUtils.siUsersGroups=" + siUsersGroups.size());
			try {
      	for (int i=0 ; i<siUsersGroups.size() ;i++) {
  				SiUsersGroup siUsersGroup = siUsersGroups.get(i);
  				String employeeCode = siUsersGroup.getEmployeeCode();
  				BuEmployeeWithAddressView buEmployeeWithAddressView = buEmployeeWithAddressViewService.findbyBrandCodeAndEmployeeCode(brandCode,employeeCode);
  				if(buEmployeeWithAddressView != null){
  					siUsersGroup.setEmployeeName(buEmployeeWithAddressView.getChineseName());
  				}else{
  					siUsersGroup.setEmployeeName("查無此員工資料");
  				}
  				if(AjaxUtils.IS_LOCK_RECORD_FALSE.equals(siUsersGroup.getIsLockRecord())){
  					siUsersGroup.setShowStatus("已驗證");
  				}else{
  					siUsersGroup.setShowStatus("未驗證");
  				}
  			}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 取得第一筆的INDEX
			Long firstIndex = (siUsersGroups.get(0)).getIndexNo();
			// 取得最後一筆 INDEX
			Long maxIndex = siUsersGroupDAO.findPageLineMaxIndex(brandCode,groupCode);
				re.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, siUsersGroups, gridDatas,
						firstIndex, maxIndex));
		}else{
				log.info("SiUsersGroupService.getAJAXPageData AjaxUtils.siGroupMenuLists=nothing");
				re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));
		}
		log.error("------is Here");
		return re;
	}

  
  /**
   * 更新PAGE的LINE
   * 
   * @param httpRequest
   * @return List<Properties>
   * @throws Exception
   */
  public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception{

  	try{
  	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
  	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
  	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
  	    String brandCode = httpRequest.getProperty("brandCode");
  	    String groupCode = httpRequest.getProperty("groupCode");
  	    String errorMsg = null;
  	    
  	    log.info("updateAJAXPageLinesData.gridData:"+gridData);
  	    log.info("updateAJAXPageLinesData.gridLineFirstIndex:"+gridLineFirstIndex);
  	    log.info("updateAJAXPageLinesData.gridRowCount:"+gridRowCount);
  	    log.info("updateAJAXPageLinesData.brandCode:"+brandCode);
  	    log.info("updateAJAXPageLinesData.groupCode:"+groupCode);
    		// 將STRING資料轉成List Properties record data
    		List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
    		// Get INDEX NO
    		long indexNo = siUsersGroupDAO.findPageLineMaxIndex(brandCode , groupCode);
    		log.info("updateAJAXPageLinesData.maxIndexNo:"+indexNo);
  			SiUsersGroup siUsersGroup;
  			//BuEmployeeBrand buEmployeeBrand;
  			//BuEmployeeBrandId buEmployeeBrandId;
    		if (upRecords != null) {
    		    for (Properties upRecord : upRecords) {
    		    	String employeeCode = upRecord.getProperty("employeeCode");
    		    	Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));

  	    			if (StringUtils.hasText(employeeCode)) {
  		    			log.info("updateAJAXPageLinesData.employeeCode:"+employeeCode);
  	    				
	  	    			siUsersGroup = siUsersGroupDAO.findById(lineId);
	  	    			
  	    			    if(siUsersGroup != null){
    		    				AjaxUtils.setPojoProperties(siUsersGroup, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
    		    				siUsersGroupDAO.update(siUsersGroup);
  	    			    }else{
    		    				indexNo++;
    	  	    			siUsersGroup = new SiUsersGroup();
    		    				AjaxUtils.setPojoProperties(siUsersGroup, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
    		    				siUsersGroup.setIndexNo(indexNo);
    		    				siUsersGroup.setBrandCode(brandCode);
    		    				siUsersGroup.setGroupCode(groupCode);
    		    				siUsersGroup.setIsLockRecord(AjaxUtils.IS_LOCK_RECORD_TRUE);
    		    				siUsersGroupDAO.save(siUsersGroup);
  	    			    }
  	    			}
    		    }
    		}
  	    return AjaxUtils.getResponseMsg(errorMsg);
      }catch(Exception ex){
  				log.info("更新使用者群組發生錯誤，原因：" + ex.toString());
          log.error("更新使用者群組發生錯誤，原因：" + ex.toString());
          throw new Exception("更新使用者群組失敗！"); 
      }	
  }

}
