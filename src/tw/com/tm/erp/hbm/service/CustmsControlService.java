package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuPaymentTerm;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.CustomsContorl;
import tw.com.tm.erp.hbm.bean.GnFile;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.CustomsContorlDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class CustmsControlService {
    private static final Log log = LogFactory.getLog(CustmsControlService.class);
    
    private BuBrandDAO buBrandDAO;
    private BuCommonPhraseService buCommonPhraseService = new BuCommonPhraseService();
    
    public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
        this.buCommonPhraseService = buCommonPhraseService;
    }

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
        this.buBrandDAO = buBrandDAO;
    }
    
    private CustomsContorlDAO customsContorlDAO;
    public void setCustomsContorlDAO(CustomsContorlDAO customsContorlDAO) {
        this.customsContorlDAO = customsContorlDAO;
    }
    
    private ImMovementHeadDAO imMovementHeadDAO;
    public void setImMovementHeadDAO(ImMovementHeadDAO imMovementHeadDAO) {
        this.imMovementHeadDAO = imMovementHeadDAO;
    }
    
    private ImAdjustmentHeadDAO imAdjustmentHeadDAO;
    public void setImAdjustmentHeadDAO(ImAdjustmentHeadDAO imAdjustmentHeadDAO) {
        this.imAdjustmentHeadDAO = imAdjustmentHeadDAO;
    }
    private BaseDAO baseDAO;
    public void setBaseDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }
    
    private BuBrandService buBrandService;
    public void setBuBrandService(BuBrandService buBrandService) {
        this.buBrandService = buBrandService;
    }
    
    public static final String[] GNFILE_GRID_FIELD_NAMES = { 
		"indexNo","orderTypeCode","name", "status", "allowDate", "lotNumber","waitToSend","sended"
		  };

    public static final String[] GNFILE_GRID_FIELD_DEFAULT_VALUES = { 
    	"", "", "", "", "", "", "" ,""
    	 };
    
    
    
    public Map doSubmit(Map parameterMap){
    	
    	
    	
    	Map resultMap = new HashMap();
    	String resultMsg = null;
    	Connection connDM = null;
    	PreparedStatement stmt = null;
    	Statement stmt1 = null;
    	connDM = this.getConnect();
    	
    	
    	try{
    		Object formLinkBean = parameterMap.get("vatBeanFormLink");
    		//
    		String lotNumberSelect = (String)PropertyUtils.getProperty(formLinkBean, "lotNumberSelect");
    		String CustomsAllowStatusSelect = (String)PropertyUtils.getProperty(formLinkBean, "CustomsAllowStatusSelect");
    		String allowDateSelect = (String)PropertyUtils.getProperty(formLinkBean, "allowDateSelect");
    		System.out.println("allowDateSelect:"+allowDateSelect);
    		String sql = "UPDATE CUSTOMS_CONTORL SET STATUS = '"+CustomsAllowStatusSelect+"' , LOT_NUMBER = '"+lotNumberSelect+"' ,ALLOW_DATE = to_date('"+allowDateSelect+"','YYYY/MM/DD')  ";
    		/*stmt = connDM.prepareStatement(sql);
    		stmt.setString(1,"21");//訊息狀態編碼*/
    		System.out.println(sql);
    		stmt1 = connDM.createStatement();
    		stmt1.executeUpdate(sql);
    		resultMsg = "執行成功！ 是否繼續？";
    	}catch(Exception e){
    		
    		
    		e.printStackTrace();
    		resultMsg = "執行失敗！原因:"+e.getMessage();
    	}finally{
    		try{
    			if(stmt!=null){
    				stmt.close();
    			}
    			if(connDM!=null){
    				connDM.close();
    			}
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    	
		resultMap.put("resultMsg", resultMsg);
		return resultMap;
    }

    
    
    public Connection getConnect(){
		try{
			//String conUrl = "jdbc:sqlserver://10.1.8.161:1433;databaseName=DFMS;user=sa;password=tmdfms;";  
			Class.forName("oracle.jdbc.driver.OracleDriver");
			return DriverManager.getConnection("jdbc:oracle:thin:@10.1.98.109:1521:KWEDB7","erp","erp52613");
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
    /**
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeDBFInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);

	try{
		System.out.println("executeDBFInitialexecuteDBFInitialexecuteDBFInitialexecuteDBFInitialexecuteDBFInitialexecuteDBFInitial");
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);

	    Map multiList = new HashMap(0);
	    resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
	    resultMap.put("employeeName", employeeName);
	    resultMap.put("employeeCode", loginEmployeeCode);
	    resultMap.put("brandCode", loginBrandCode);

	    List<BuCommonPhraseLine> CustomsAllowStatus = buCommonPhraseService.getCommonPhraseLinesById("CustomsAllowStatus", false);
	    List<BuCommonPhraseLine> lotNumber = buCommonPhraseService.getCommonPhraseLinesById("lotNumber", false);
	    List<BuCommonPhraseLine> allExportTypes = buCommonPhraseService.getCommonPhraseLinesById("ExportDBFType", false);
	    for(int i=0;i<CustomsAllowStatus.size();i++){
	    	System.out.println(CustomsAllowStatus.get(0));
	    	System.out.println(lotNumber.get(0));
	    }
	    
	    multiList.put("CustomsAllowStatus"	, AjaxUtils.produceSelectorData(CustomsAllowStatus, "lineCode", "name", false, true ));
	    //resultMap.put("CustomsAllowStatus",AjaxUtils.produceSelectorData(CustomsAllowStatus, "lineCode", "name", false, true ));
	    multiList.put("lotNumber"	, AjaxUtils.produceSelectorData(lotNumber, "lineCode", "name", false, true ));
	    multiList.put("allExportTypes"	, AjaxUtils.produceSelectorData(allExportTypes, "lineCode", "name", false, true ));
	    
	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("匯出初始化失敗，原因：" + ex.toString());
	    ex.printStackTrace();
	    throw new Exception("匯出初始化失敗，原因：" + ex.toString());

	}
    }
    
    public CustomsContorl findById(Long headId) throws Exception {
		try {log.info("find333");
		CustomsContorl customsContorl = (CustomsContorl) customsContorlDAO
					.findByPrimaryKey(BuSupplierMod.class, headId);

			log.info("find444" + customsContorl);
					return customsContorl;

		} catch (Exception ex) {
			log.error("依據主鍵：" + headId +" id時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢headId錯誤，原因："
					+ ex.getMessage());
		}
	}
    
    public List<Properties> updateAJAXPageData(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
	NoSuchMethodException,Exception {
	//log.info("PoPurchaseOrderHeadMainService.getAJAXPageData");
	try {
		List<Properties> result = new ArrayList();
		List<Properties> gridDatas = new ArrayList();
		int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
		// 要顯示的HEAD_ID
		String orderTypeCode = "RSF";
		log.info("headId"+orderTypeCode);
		List<CustomsContorl> customsContorlList = customsContorlDAO.findAll();
		log.info("customsContorl1"+customsContorlList);
		for(CustomsContorl customsContorl:customsContorlList){
			if(customsContorl.getOrderTypeCode().equals("RSF")){
				List wts = (List) imMovementHeadDAO.findCustomsWaitToSend();
				List s = (List) imMovementHeadDAO.findCustomsSended();
				 customsContorl.setWaitToSend(wts.get(0).toString());
				 customsContorl.setSended(s.get(0).toString());
				 customsContorlDAO.save(customsContorl);
			}
			if(customsContorl.getOrderTypeCode().equals("MEG")){
				List wts = (List) imAdjustmentHeadDAO.findCustomsWaitToSend();
				List s = (List) imAdjustmentHeadDAO.findCustomsSended();
				 customsContorl.setWaitToSend(wts.get(0).toString());
				 customsContorl.setSended(s.get(0).toString());
				 customsContorlDAO.save(customsContorl);
			}
		}
		
		//if(customsContorlList != null)
			//this.setCustomsContorlAllowDate(customsContorlList);
		HashMap findObjs = new HashMap();
		HashMap map = new HashMap();
		Map searchMap = baseDAO.search("CustomsContorl", findObjs,
				"order by indexNo", iSPage, iPSize,
				BaseDAO.QUERY_SELECT_RANGE);
		List<CustomsContorl> lists = (List<CustomsContorl>) searchMap.get(BaseDAO.TABLE_LIST);
		if (lists != null && lists.size() > 0) {
			// 設定額外欄位			
			Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
			Long maxIndex = (Long) customsContorlDAO.search("CustomsContorl as model","count(model.headId) as rowCount", findObjs,
					 iSPage, iPSize,BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); 
			result.add(AjaxUtils.getAJAXPageData(httpRequest,
					GNFILE_GRID_FIELD_NAMES,
					GNFILE_GRID_FIELD_DEFAULT_VALUES, lists,gridDatas, firstIndex, maxIndex));
		} else {
			log.info("add2222");
			result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
					GNFILE_GRID_FIELD_NAMES,
					GNFILE_GRID_FIELD_DEFAULT_VALUES, map, gridDatas));
		}
		
		
		return result;
	} catch (Exception ex) {
		ex.printStackTrace();
		log.error("附件查詢時發生錯誤，原因：" + ex.toString());
		throw new Exception("查詢附件資料失敗！");
	}
	
    }
    
    /**
     * 設定日期為今日
     * @param cmMovementHeads
     * @throws ParseException 
     */
    private void setCustomsContorlAllowDate(List<CustomsContorl> CustomsContorls) throws ParseException{
    	for(CustomsContorl customsContorl:CustomsContorls){
    		Date today = new Date();
    		DateFormat df = DateFormat.getDateInstance();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    		if(customsContorl.getAllowDate().compareTo(today)!=0){
    			System.out.println("df.parse(sdf.format(today)):"+df.parse(sdf.format(today)));
    			customsContorl.setAllowDate(df.parse(sdf.format(today)));
    		}
    		customsContorlDAO.save(customsContorl);
    	}
	/*for (CmMovementHead cmMovementHead : cmMovementHeads) {
	    cmMovementHead.setStatusName(OrderStatus.getChineseWord(cmMovementHead.getStatus()));
	}*/
    }
    
    public List<Properties> updateperformTransaction(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData....");
		try {
			String errorMsg = null;
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
			String status = httpRequest.getProperty("status");
			log.info("orderTypeCode~~~~"+orderTypeCode+" "+status);
				List<CustomsContorl> customsControls = customsContorlDAO.findByProperty(orderTypeCode);//.findById(headId);
				log.info("status~~~~@2"+customsControls.get(0).getAllowDate());
				customsControls.get(0).setStatus(status);
				customsControls.get(0).setLastUpdateDate(new Date());
				customsControls.get(0).setLastUpdatedBy(loginEmployeeCode);
				customsContorlDAO.update(customsControls.get(0));
				log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("更新入提明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新入提明細失敗！");
		}
	}
    
    public Map executeInitial(Map parameterMap) throws Exception{
    	 HashMap resultMap    = new HashMap();
    	Object otherBean     = parameterMap.get("vatBeanOther");
        String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");
        log.info("brand"+brandCode);
        String employeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
        log.info("empemp"+employeeCode);
        //String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
        String formIdString  = (String)PropertyUtils.getProperty(otherBean, "formId");
        Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
        try{
        	log.info("*----ffffff*"+formId);
            CustomsContorl customsContorl = null;
           // if(formId == null){
            //	customsContorl = customsContorlDAO.findByProperty(customsContorl, "orderTypeCode", "RSF");
				log.info("*+++++*++++++*"+customsContorl);
            //}
            BuBrand     buBrand     = buBrandService.findById( brandCode );
            List<CustomsContorl> customsContorlList = customsContorlDAO.findAll();
    		log.info("customsContorl22"+customsContorlList);
            this.setCustomsContorlAllowDate(customsContorlList);
          
           // resultMap.put("form", customsContorl);
            resultMap.put("superintendentCode",      employeeCode);
           // resultMap.put("status",            customsContorl.getStatus() );
            resultMap.put("branchCode",        buBrand.getBuBranch().getBranchCode());	// 2->T2
            resultMap.put("brandName",         buBrand.getBrandName());                                 
           // resultMap.put("statusName",        OrderStatus.getChineseWord(customsContorl.getStatus()));
            
	    	return resultMap;       	
        }catch (Exception ex) {
        	log.error("採購單初始化失敗，原因：" + ex.toString());
	    	throw new Exception("採購單初始化別失敗，原因：" + ex.toString());
        }   
    
    }
    
}
