package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List; //import tw.com.tm.erp.constants.MessageStatus;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
//import tw.com.tm.erp.hbm.SpringUtils;
//import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiFunction;
import tw.com.tm.erp.hbm.bean.SiFunctionObject;
import tw.com.tm.erp.hbm.bean.SiFunctionObjectId;
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.SiFunctionDAO;
import tw.com.tm.erp.hbm.dao.SiFunctionObjectDAO;
import tw.com.tm.erp.hbm.dao.SiMenuDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class SiFunctionObjectService {
	
	private static final Log log = LogFactory.getLog(SiFunctionObjectService.class);
	
	private SiFunctionDAO siFunctionDAO;
	private SiFunctionObjectDAO siFunctionObjectDAO;

	private BuCommonPhraseHeadDAO buCommonPhraseHeadDAO;

	private BuCommonPhraseService buCommonPhraseService;
	
	public static final String[] GRID_FIELD_NAMES = { 
	 "indexNo", "objectCode", "objectName", "objectType", 
	 "lineId", "isLockRecord", "isDeleteRecord", "message"};
	
	public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
  	"", "", "", "",
  	"", AjaxUtils.IS_LOCK_RECORD_CHECK, AjaxUtils.IS_DELETE_RECORD_FALSE, ""};
	
	public static final int[] GRID_FIELD_TYPES = { 
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, 
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
  	};
	
	public void setSiFunctionDAO(SiFunctionDAO siFunctionDAO) {
		this.siFunctionDAO = siFunctionDAO;
	}
	
	public void setSiFunctionObjectDAO(SiFunctionObjectDAO siFunctionObjectDAO) {
		this.siFunctionObjectDAO = siFunctionObjectDAO;
	}
	
	public void setBuCommonPhraseHeadDAO(BuCommonPhraseHeadDAO buCommonPhraseHeadDAO) {
		this.buCommonPhraseHeadDAO = buCommonPhraseHeadDAO;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}
	
	
	public List<Properties> executeInitialLineSelect(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
	
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			
			Map multiList = new HashMap(0);
			
			List<BuCommonPhraseLine> allObjectTypes = buCommonPhraseService.findCommonPhraseLineByProperty( "id.buCommonPhraseHead.headCode", "SiFunctionObjectObjectType" );
			log.info( "allObjectTypes size:" + allObjectTypes.size() );			
			
			multiList.put( "allObjectTypes", AjaxUtils.produceSelectorData(allObjectTypes ,"name" ,"name"         ,  false,  false) );
			resultMap.put("multiList",multiList);
			log.info( "結束" );				
		}catch(Exception ex){
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type"   , "ALERT");
			messageMap.put("message", "表單初始化失敗，原因："+ex.toString());
			messageMap.put("event1" , null);
			messageMap.put("event2" , null);
			resultMap.put("vatMessage",messageMap);
			
		}
		
		return AjaxUtils.parseReturnDataToJSON(resultMap);
		
	}
	
	
	public List<Properties> getAJAXPageData(Properties httpRequest)throws Exception{
		
		try{
			
  		List<Properties> result = new ArrayList();
  		List<Properties> gridDatas = new ArrayList();
  		
  		String functionCode = httpRequest.getProperty("functionCode");
  		log.info("functionCode=" + functionCode );
  		
  		int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
      int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
  		
  		List<SiFunctionObject> siFunctionObject = siFunctionObjectDAO.findPageLine(functionCode, iSPage, iPSize); 
  		
  	  HashMap map = new HashMap();
      map.put("functionCode", functionCode);
		
			if (siFunctionObject != null && siFunctionObject.size() > 0) {
	    	
	    	// 取得第一筆的INDEX
	    	Long firstIndex = siFunctionObject.get(0).getIndexNo();
	    	// 取得最後一筆 INDEX
	    	Long maxIndex = siFunctionObjectDAO.findPageLineMaxIndex(functionCode); 
	    	log.info("siFunction.AjaxUtils.getAJAXPageData:maxIndex= " + maxIndex );
	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,siFunctionObject, gridDatas, firstIndex, maxIndex));
	    } else {
	    	log.info("siFunction.AjaxUtils.getAJAXPageDataDefault ");
	    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,gridDatas));
	   
	    }
	
	    return result;
		}catch(Exception ex){
	    log.error("載入頁面顯示的元件明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的元件明細失敗！");
		}	
	}
	

	/**
   * 更新PAGE的LINE
   * 
   * @param httpRequest
   * @return List<Properties>
   * @throws Exception
   */
  public List<Properties> updateOrSaveAJAXPageLinesData(Properties httpRequest) throws Exception{

      try{
      	log.info("updateOrSaveAJAXPageLinesData.載入");
  	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
  	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
  	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
  	    String functionCode = httpRequest.getProperty("functionCode");
  	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
  	    
  	    if(functionCode == null){
  	    	throw new ValidationErrorException("傳入的系統功能單主鍵為空值！");
  	    }
  	    String errorMsg = null;
  	    
  	    log.info("updateOrSaveAJAXPageLinesData.gridData:"+gridData);
  	    log.info("updateOrSaveAJAXPageLinesData.gridLineFirstIndex:"+gridLineFirstIndex);
  	    log.info("updateOrSaveAJAXPageLinesData.functionCode:"+functionCode);
  	    log.info("updateOrSaveAJAXPageLinesData.gridRowCount:"+gridRowCount);
  	    
    		// 將STRING資料轉成List Properties record data
    		List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
    		// Get INDEX NO
    		int indexNo = siFunctionObjectDAO.findPageLineMaxIndex(functionCode).intValue();
    		log.info("updateOrSaveAJAXPageLinesData.maxIndexNo:"+indexNo);
    		if (upRecords != null) {
    		    for (Properties upRecord : upRecords) {	
    		        // 先載入HEAD_ID OR LINE DATA
  	    			String objectCode = upRecord.getProperty("objectCode");
  	    			log.info("updateOrSaveAJAXPageLinesData.objectCode:"+objectCode);
  	    			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
  	    			log.info("updateOrSaveAJAXPageLinesData.lineId:"+lineId);
  	    			
  	    			String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);
  	    			
  	    			if (StringUtils.hasText(itemCode)) {
  	    				
  	    				SiFunctionObject siFunctionObjectUp = siFunctionObjectDAO.findById(functionCode, lineId);
  	    				Date date = new Date();
  	    			    if(siFunctionObjectUp != null){
  	    			    	log.info("updateOrSaveAJAXPageLinesData.siFunctionObjectUp != null");
    		    				AjaxUtils.setPojoProperties(siFunctionObjectUp, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
    		    				siFunctionObjectUp.setUpdatedBy(loginEmployeeCode);
    		    				siFunctionObjectUp.setUpdateDate(date);
    		    				siFunctionObjectDAO.update(siFunctionObjectUp);
    		    				
  	    			    }else{
  	    			    	log.info("updateOrSaveAJAXPageLinesData.siFunctionObjectUp == null");
    		    				indexNo++;
    		    				SiFunctionObject siFunctionObject = new SiFunctionObject( ); // new SiFunctionObjectId( functionCode, objectCode )
    		    				
    		    				AjaxUtils.setPojoProperties(siFunctionObject, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
    		    				log.info( "line_id = " +siFunctionObject.getLineId() ) ;
    		    				siFunctionObject.setSiFunction( new SiFunction( functionCode ) );
    		    				siFunctionObject.setControlType("D"); // 預設
    		    				siFunctionObject.setCreatedBy(loginEmployeeCode);
    		    				siFunctionObject.setCreationDate(date);
    		    				siFunctionObject.setUpdatedBy(loginEmployeeCode);
    		    				siFunctionObject.setUpdateDate(date);
    		    				siFunctionObject.setIndexNo(Long.valueOf(indexNo));
    		    				siFunctionObjectDAO.save(siFunctionObject);
    		    				
  	    			    }
  	    			}
    		    }
    		}
  	    
  	    
  	    return AjaxUtils.getResponseMsg(errorMsg);
      }catch(Exception ex){
          log.error("更新系統功能元件明細時發生錯誤，原因：" + ex.toString());
          throw new Exception("更新系統功能元件明細失敗！"); 
      }	
  }
	
  /**
   * 
   * 
   * @param httpRequest
   * @return
   * @throws ValidationErrorException
   */
  public String updateObjectData(Object beanBind) throws ValidationErrorException , Exception{
  	log.info( "=======updateObjectData() start=========" );
  	try{
  		
  			String functionCode = String.valueOf( PropertyUtils.getProperty( beanBind, "functionCode") );    
  	    //======================帶入Head的值=========================
  			String functionName = String.valueOf( PropertyUtils.getProperty( beanBind, "functionName") );
  	
  	    log.info( "functionCode = " + functionCode );
  	    log.info( "functionName = " + functionName );
  	    
  	    HashMap map = new HashMap();
  	    map.put("functionCode", functionCode);
  	    map.put("functionName", functionName);
  	    
  	    SiFunction siFunctionPO  = siFunctionDAO.getById( functionCode ); 
    		if( siFunctionPO == null ){ 
    	  	throw new ValidationErrorException("查無系統單主鍵：" + functionCode + "的資料！");
    		}
    		log.info( "=======updateObjectData() end=========" );
  	    return removeAJAXLine( siFunctionPO ); //AjaxUtils.getResponseMsg(null);
  	    
  	}catch (Exception ex) {
  	    log.error("更新系統功能元件明細相關欄位發生錯誤，原因：" + ex.toString());
  	    throw new ValidationErrorException("更新系統功能元件相關欄位失敗！" + ex.toString() );
  	} 
  }
  
  /**
   * remove delete mark record
   * 
   * @param salesOrderHead
   */
  private String removeAJAXLine(SiFunction siFunction) throws Exception{
	
  	List<SiFunctionObject> siFunctionObjects  = siFunction.getSiFunctionObject();
    
  	// 驗證
  	String resultMsg = checkLine( siFunctionObjects ); 
  	if( AjaxUtils.CHECK_OK.equals( resultMsg ) ){ 
  		int size = siFunctionObjects.size();
  		// 刪除 line IS_DELETE_RECORD_TRUE
    	if(siFunctionObjects != null && size > 0 ){
    		for(int i = size - 1; i >= 0; i--){
        	SiFunctionObject siFunctionObject = siFunctionObjects.get(i);         
        	if( AjaxUtils.IS_DELETE_RECORD_TRUE.equals(siFunctionObject.getIsDeleteRecord()) ){
        		log.info( "元件代號 = " + siFunctionObject.getObjectCode() );
        		siFunctionObjectDAO.delete( siFunctionObject );
        		siFunctionObjects.remove( siFunctionObject );
        	}
      	}
      }
    	// 更新 indexNo , IS_LOCK_
    	
    	int sizeUpdate = siFunctionObjects.size();
    	log.info( "sizeUpdate = " + sizeUpdate );
    	for (int i = 0; i < sizeUpdate; i++) {
    		SiFunctionObject siFunctionObjectUpdate = siFunctionObjects.get(i);
    		
    		siFunctionObjectUpdate.setIndexNo( i + 1L );
    		siFunctionObjectUpdate.setIsLockRecord( AjaxUtils.IS_LOCK_RECORD_FALSE );
    		siFunctionObjectDAO.update( siFunctionObjectUpdate );
  		}
  	} else {
  		return resultMsg;
  	}
  	return AjaxUtils.CHECK_OK;
  }
  
  /**
   * 驗證 line
   * @param beanBind
   * @return
   */
  public String checkLine( List<SiFunctionObject> siFunctionObjects ){
  	log.info( "=======checkLine() start=========" );
  	StringBuffer checkMsg = new StringBuffer();
  	Map map = new HashMap();
  	
  	int size = siFunctionObjects.size(); 
  	if(siFunctionObjects != null && size > 0 ){
  		for(int i = 0; i < size; i++){
        	SiFunctionObject siFunctionObject = siFunctionObjects.get(i); 
        	if( !AjaxUtils.IS_DELETE_RECORD_TRUE.equals(siFunctionObject.getIsDeleteRecord()) ){
        		String objectCode = siFunctionObject.getObjectCode(); 
        		Long indexNo = siFunctionObject.getIndexNo();
        		
        		if( !map.containsKey( objectCode ) ){  
        			map.put( objectCode, indexNo );
        		} else {
        			checkMsg.append( "發現元件代號 " )
        							.append( objectCode )
        							.append( "在第" )
        							.append( map.get( objectCode ) )
        							.append( "與" )
        							.append( indexNo )
        							.append( "筆重複,請再次確認" ); 
               return checkMsg.toString();
        		}
        		
        	}
        }
    }
  	log.info( "=======checkLine() end=========" );
  	return AjaxUtils.CHECK_OK;
  }
  
}