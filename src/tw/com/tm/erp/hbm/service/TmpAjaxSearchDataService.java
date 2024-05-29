package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.TmpAjaxSearchData;
import tw.com.tm.erp.hbm.dao.TmpAjaxSearchDataDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class TmpAjaxSearchDataService {
    
    /**
     * 移倉單明細欄位
     */
    public static final String[] BARCODE_GRID_FIELD_NAMES = { 
	"checkbox", "indexNo", "imItemCode", 
	"imUnitPrice", "paper", "imItemCName", 
	"itemId"
    };
    
	private TmpAjaxSearchDataDAO tmpAjaxSearchDataDAO;
	private static final Log log = LogFactory.getLog(TmpAjaxSearchDataService.class);
	
	public void setTmpAjaxSearchDataDAO(TmpAjaxSearchDataDAO tmpAjaxSearchDataDAO) {
		this.tmpAjaxSearchDataDAO = tmpAjaxSearchDataDAO;
	}

	public TmpAjaxSearchData findByKey(String timeScope, String selectionData){
		return tmpAjaxSearchDataDAO.findByKey(timeScope, selectionData); 
	}
	
	public List<TmpAjaxSearchData> findByTimeScope(String timeScope){		
		return tmpAjaxSearchDataDAO.findByTimeScope(timeScope);
	}
		
	public List<TmpAjaxSearchData> find(String timeScope, String selectionData){
		return tmpAjaxSearchDataDAO.find(timeScope, selectionData);
	}
	
	public void deleteByTimeScope(String timeScope){
		log.info("deleteByTimeScope.by String");
		log.info("deleteByTimeScope.timeScope:"+ timeScope);
		tmpAjaxSearchDataDAO.deleteByTimeScope(timeScope); 
	}
	
	public List<Properties> deleteByTimeScope(Map parameterMap) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		log.info("deleteByTimeScope.by Map");
		Map resultMap = new HashMap(0);
		log.info(parameterMap.keySet().toString());
		String timeScope = (String) parameterMap.get(AjaxUtils.TIME_SCOPE);		
		log.info("deleteByTimeScope by Map.timeScope:"+ timeScope);
		if(StringUtils.hasText(timeScope)){
			tmpAjaxSearchDataDAO.deleteByTimeScope(timeScope);
		}
		log.info("deleteByTimeScope.success");
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	
	public List<Properties> deleteByOtherBeanTimeScope(Map parameterMap) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		log.info("deleteByTimeScope.by Map");
		Map resultMap = new HashMap(0);
		log.info(parameterMap.keySet().toString());
		String timeScope = (String) parameterMap.get(AjaxUtils.TIME_SCOPE);
		log.info("deleteByTimeScope by Map.timeScope:"+ timeScope);
		if(parameterMap.containsKey("vatBeanOther")){
		    Object otherBean = parameterMap.get("vatBeanOther");
		    String timeScope2 = (String)PropertyUtils.getProperty(otherBean, AjaxUtils.TIME_SCOPE);
		    if(StringUtils.hasText(timeScope2)){
			tmpAjaxSearchDataDAO.deleteByTimeScope(timeScope2);
		    }
		}
		log.info("deleteByTimeScope.success");
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	
	public void update(String timeScope, String[][] updateArray, String selectionType) throws Exception {
		//String timeScope = new String("");
		String selectionData = new String("");
		log.info("updateArray.length = " + updateArray.length);
		boolean isExist = true;
		try{
			if(AjaxUtils.SELECTION_TYPE_RADIO.equalsIgnoreCase(selectionType)){
				deleteByTimeScope(timeScope);
			}
			for (int i = 0; i < updateArray.length; i++) {
				TmpAjaxSearchData tmpAjaxSearchData = null;
				log.info(i+" before timeScope:"+ timeScope + " searchKey:"+ updateArray[i][0] + " status:"+ updateArray[i][1]);
				if (StringUtils.hasText(updateArray[i][0])
				 && StringUtils.hasText(updateArray[i][1])
				 && StringUtils.hasText(updateArray[i][2])) {
				
					selectionData = updateArray[i][0];
					isExist       = "Y".equalsIgnoreCase(updateArray[i][1]) ? true	: false;
					log.info(i+" after timeScope:"+ timeScope + " searchKey:"+ selectionData + " status:"+ isExist);
					if(!AjaxUtils.SELECTION_TYPE_RADIO.equalsIgnoreCase(selectionType)){
						tmpAjaxSearchData = tmpAjaxSearchDataDAO.findByKey(timeScope, selectionData);
					}
					if(null != tmpAjaxSearchData && !isExist){
						tmpAjaxSearchDataDAO.delete(tmpAjaxSearchData);
						log.info("update.DELETE");
					}else if(null == tmpAjaxSearchData && isExist){
						TmpAjaxSearchData insertObj = new TmpAjaxSearchData();
						insertObj.setTimeScope(timeScope);
						insertObj.setSelectionData(selectionData);
						insertObj.setChecked(updateArray[i][1]);
						insertObj.setRowId(Long.valueOf(updateArray[i][2]));
						tmpAjaxSearchDataDAO.save(insertObj);
						log.info("update.INSERT");
					}
				} else {
					log.info("查詢更新失敗，請通知系統管理員(timeScope=" + 
							timeScope+",selectionData=" + updateArray[i][0]+",isExist=" + updateArray[i][1]+")");
					//throw new ValidationErrorException("查詢更新失敗，請通知系統管理員(timeScope=" + 
					//		timeScope+",selectionData=" + updateArray[i][0]+",isExist=" + updateArray[i][1]+")");
				}
			}
			log.info("updateArray Over");
		}catch(Exception ex){
			log.info("查詢更新失敗，請通知系統管理員,原因:"+ex.toString());
			//throw new Exception("查詢更新失敗，請通知系統管理員,原因:"+ex.toString());
		}
	}
	
	/**
	 * for 可更新selectionData用
	 * @param timeScope
	 * @param updateArray
	 * @param selectionType
	 * @param pkIndex
	 * @throws Exception
	 */
	public void update(String timeScope, String[][] updateArray, String selectionType, Integer pkIndex) throws Exception {
	    //String timeScope = new String("");
	    String selectionData = new String("");
	    log.info("updateArray.length = " + updateArray.length);
	    boolean isExist = true;
	    try{
		if(AjaxUtils.SELECTION_TYPE_RADIO.equalsIgnoreCase(selectionType)){
		    deleteByTimeScope(timeScope);
		}
		for (int i = 0; i < updateArray.length; i++) {
		    TmpAjaxSearchData tmpAjaxSearchData = null;
		    log.info(i+" before timeScope:"+ timeScope + " searchKey:"+ updateArray[i][0] + " status:"+ updateArray[i][1]+ " rowId:"+ updateArray[i][2]);
		    if (StringUtils.hasText(updateArray[i][0])
			    && StringUtils.hasText(updateArray[i][1])
			    && StringUtils.hasText(updateArray[i][2])) {
			
			Long l = NumberUtils.getLong(String.valueOf((i + 1)));
			
			selectionData = updateArray[i][0];
			isExist       = "Y".equalsIgnoreCase(updateArray[i][1]) ? true	: false;
			log.info(i+" after timeScope:"+ timeScope + " searchKey:"+ selectionData + " status:"+ isExist+ " rowId:"+ updateArray[i][2]);
			if(!AjaxUtils.SELECTION_TYPE_RADIO.equalsIgnoreCase(selectionType)){
//			    if(null == pkIndex){
//				tmpAjaxSearchData = tmpAjaxSearchDataDAO.findByKey(timeScope, selectionData);
//			    }
//			    else{
				tmpAjaxSearchData = (TmpAjaxSearchData)tmpAjaxSearchDataDAO.findFirstByProperty("TmpAjaxSearchData", "and timeScope = ? and rowId = ?", new Object[]{timeScope,Long.valueOf(updateArray[i][2])});
//			    }
			}
			if(null != tmpAjaxSearchData && !isExist){
			    tmpAjaxSearchDataDAO.delete(tmpAjaxSearchData);
			    log.info("update.DELETE");
			}else if(null == tmpAjaxSearchData && isExist){
			    TmpAjaxSearchData insertObj = new TmpAjaxSearchData();
			    insertObj.setTimeScope(timeScope);
			    insertObj.setSelectionData(selectionData);
			    insertObj.setChecked(updateArray[i][1]);
			    insertObj.setRowId(Long.valueOf(updateArray[i][2]));
			    tmpAjaxSearchDataDAO.save(insertObj);
			    log.info("update.INSERT");
			}else if(null != tmpAjaxSearchData && isExist){
			    tmpAjaxSearchData.setTimeScope(timeScope);
			    tmpAjaxSearchData.setSelectionData(selectionData);
			    tmpAjaxSearchData.setChecked(updateArray[i][1]);
			    tmpAjaxSearchData.setRowId(Long.valueOf(updateArray[i][2]));
			    tmpAjaxSearchDataDAO.update(tmpAjaxSearchData);
			    log.info("update.update");
			}
		    } else {
			log.info("查詢更新失敗，請通知系統管理員(timeScope=" + 
				timeScope+",selectionData=" + updateArray[i][0]+",isExist=" + updateArray[i][1]+")");
			//throw new ValidationErrorException("查詢更新失敗，請通知系統管理員(timeScope=" + 
			//		timeScope+",selectionData=" + updateArray[i][0]+",isExist=" + updateArray[i][1]+")");
		    }
		}
		log.info("updateArray Over");
		
		// 重排indexNo 
		List<TmpAjaxSearchData> tmpAjaxSearchDatas = tmpAjaxSearchDataDAO.findByTimeScope(timeScope);
		Long l = 1L;
		for (TmpAjaxSearchData tmpAjaxSearchData : tmpAjaxSearchDatas) {
		    tmpAjaxSearchData.setRowId(l);
		    tmpAjaxSearchDataDAO.update(tmpAjaxSearchData);
		    l++;
		}
		
	    }catch(Exception ex){
		log.info("查詢更新失敗，請通知系統管理員,原因:"+ex.toString());
		//throw new Exception("查詢更新失敗，請通知系統管理員,原因:"+ex.toString());
	    }
	}
}