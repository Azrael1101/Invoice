package tw.com.tm.erp.importdb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.service.ImItemPriceService;
import tw.com.tm.erp.hbm.service.ImItemService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.User;

public class ImItemEanCodeImportData implements ImportDataAbs{
    private static final Log log = LogFactory.getLog(ImItemEanCodeImportData.class);
    
    public ImportInfo initial(HashMap uiProperties) {
	log.info("ImItemEanCodeImportData.initial");
	User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);	
	log.info("user.getBrandCode() = " + user.getBrandCode());
	
	ImportInfo imInfo = new ImportInfo();

	// set entity class name
	imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImItemEancode.class.getName());

	// set key info
	imInfo.setKeyIndex(0);
	imInfo.setKeyValue(ImportDBService.DEFAULT_KEY_HEAD + "0");

	imInfo.addFieldName("key");
	// set field type
	imInfo.setFieldType("key", "java.lang.String"); 
	
	if( user.getBrandCode().indexOf("T2") > -1 ){
		imInfo.setXlsColumnLength(3);
		doT2Initial(imInfo);
		imInfo.setOneRecordDetailKeys(new int[] { 3 });
	}
	

	// set date format
	imInfo.setFieldTypeFormat("java.util.Date", "yyyy/MM/dd");

	// set default value
	HashMap defaultValue = new HashMap();
	defaultValue.put("creationDate", new Date());
	defaultValue.put("lastUpdateDate", new Date());
	defaultValue.put("createdBy", user.getEmployeeCode() );
	defaultValue.put("lastUpdatedBy", user.getEmployeeCode() );
	defaultValue.put("reserve5", user.getBrandCode() );	// 20100314借用存放 user.brandCode 存檔前 clear
	defaultValue.put("isDeleteRecord", AjaxUtils.IS_DELETE_RECORD_FALSE );	
	defaultValue.put("isLockRecord", AjaxUtils.IS_LOCK_RECORD_FALSE );	
	
	imInfo.setDefaultValue(defaultValue);

	// add detail
	
	imInfo.setImportDataStartRecord(1);
	return imInfo;
    }
    
    private ImportInfo doT2Initial(ImportInfo imInfo){
	
	imInfo.addFieldName("itemCode"); // 商品品號
	imInfo.addFieldName("eanCode"); // 國際碼
	imInfo.addFieldName("enable"); // 啟用
	
	// set field type
	imInfo.setFieldType("itemCode", "java.lang.String");
	imInfo.setFieldType("eanCode", "java.lang.String");
	imInfo.setFieldType("enable", "java.lang.String");
	
	return imInfo;
    }
    
    public String updateDB(List entityBeans, ImportInfo info) throws Exception {
    	log.info("ImItemEanCodeImportData.updateDB");
    	ImItemEancodeDAO imItemEancodeDAO = (ImItemEancodeDAO) SpringUtils.getApplicationContext().getBean("imItemEancodeDAO");
    	ImItemService imItemService = (ImItemService) SpringUtils.getApplicationContext().getBean("imItemService");
    	
	StringBuffer result = new StringBuffer();
	Map eanCodeMap = new HashMap();
	String otherItemCode = null;
	boolean isWriteMessage = false; 
	
	int allCount = 0;
	int correctCount = 0 ;
	int failCount = 0 ;
	for (int index = 0; index < entityBeans.size(); index++) {
	    ImItemEancode imItemEancode = (ImItemEancode) entityBeans.get(index); // 匯入的商品國際碼
	    
	    String importEnable = imItemEancode.getEnable();
	    String importItemCode = imItemEancode.getItemCode();
	    String importEanCode = imItemEancode.getEanCode();
	    log.info("importEnable = " + importEnable);
	    log.info("importItemCode = " + importItemCode);
	    log.info("importEanCode = " + importEanCode);
	    
	    // 設定預設值
	    setDefaultValues(imItemEancode);
	    
	    if ((null != imItemEancode) && (StringUtils.hasText(importItemCode)) ) { // && (StringUtils.hasText(imItem.getBrandCode()))
		ImItem oldItem = imItemService.findItem(imItemEancode.getBrandCode(), importItemCode);
		// read old data
		if (null == oldItem) { // 匯入的商品品號資料在資料庫沒有
		    failCount++;
		    log.error("匯入國際碼商品資料有問題 品牌: " + imItemEancode.getBrandCode()+ " 品號: "+importItemCode+"不存在" );
		    result.append("匯入國際碼商品資料有問題 品號: " + importItemCode+ " 國際碼: "+importEanCode + " 錯誤原因 : 品號不存在 <br/>");
		} else { // 資料庫已存在此品牌的商品
		    
		    // 塞入主檔
		    imItemEancode.setImItem(oldItem);
		    
		    ImItemEancode oldImItemEancode = imItemEancodeDAO.findEanCodeByProperty(imItemEancode.getBrandCode(), importItemCode, importEanCode);
		    if(null == oldImItemEancode){ // 新增
			log.info("ImItemEanCodeImportData.updateDB.create "+ importItemCode);
			try {
			    // 檢核國際碼
			    String returnString = doValidateEanCode(imItemEancode, index);

			    // 補欄位
			    replenishColumnImItemEanCode(imItemEancode);
			    
			    imItemService.save(imItemEancode);
			    log.info("imItemEancodeDAO.save after ");
			    if(returnString.length() > 0 ){
				result.append("商品").append(importItemCode)
				      .append("新增國際碼").append(importEanCode)
				      .append("匯入 成功但").append(returnString).append("<br/>");
			    }else{
				result.append("商品").append(importItemCode)
				      .append("新增國際碼").append(importEanCode)
				      .append("匯入成功<br/>");
			    }
			    
			    correctCount++;
			} catch (Exception ex) {
			    failCount++;
			    ex.printStackTrace();
			    log.error("匯入國際碼商品資料有問題 品號: "
				    + importItemCode + " 國際碼: "
				    + importEanCode
				    + ex.getMessage());
			    result.append("匯入國際碼商品資料有問題 品號: "
				    + importItemCode + " 國際碼: "
				    + importEanCode + " 錯誤原因 : "
				    + ex.getMessage() + "<br/>");
			}
		    }else{ // 修改 只能修改啟用欄位
			try {
			    Date date = new Date();
			    // 檢核國際碼
			    String returnString = doValidateEanCode(imItemEancode,index);
			    
			    oldImItemEancode.setEnable(imItemEancode.getEnable());
			    log.info("oldImItemEancode.enable = " + imItemEancode.getEnable());
			    oldImItemEancode.setLastUpdateDate(date);
			    oldImItemEancode.setLastUpdatedBy(imItemEancode.getLastUpdatedBy());
			    imItemService.update(oldImItemEancode);
			    
			    if(returnString.length() > 0){
				result.append("商品").append(importItemCode)
				      .append("更新國際碼").append(importEanCode)
				      .append("匯入 成功但").append(returnString).append("<br/>");
			    }else{
				result.append("商品").append(importItemCode)
				      .append("更新國際碼").append(importEanCode)
				      .append("匯入 成功<br/>");
			    }
			    
			    correctCount++;
			} catch (Exception ex) {
			    failCount++;
			    ex.printStackTrace();
			    log.error("匯入國際碼商品資料有問題 品號: "
				    + importItemCode + " 國際碼: "
				    + importEanCode
				    + ex.getMessage());
			    result.append("匯入國際碼商品資料有問題 品號: "
				    + importItemCode + " 國際碼: "
				    + importEanCode + " 錯誤原因 : "
				    + ex.getMessage() + "<br/>");
			}
		    }
		}
	    }else{
		log.info("品牌:" + imItemEancode.getBrandCode() + "第" + index
			+ " 筆未輸入品號");	 
			 
	    }
	    if(StringUtils.hasText(importItemCode)){
		allCount++;
	    }
	}
	result.append("匯入總筆數 : " + allCount  + " 成功: " + correctCount + " 失敗: " + failCount );
	return result.toString();
    }
    
    /**
     * 設定預設值
     * @param imItem
     * @return
     * @throws Exception
     */
    public ImItemEancode setDefaultValues(ImItemEancode imItemEancode) throws Exception {
	log.info("====<setDefaultValues>=======");
	
	if(StringUtils.hasText(imItemEancode.getItemCode())){
	    imItemEancode.setItemCode(imItemEancode.getItemCode().trim().toUpperCase());
	}else{
	    imItemEancode.setItemCode("");
	}
	
	if(StringUtils.hasText(imItemEancode.getEanCode())){
	    imItemEancode.setEanCode(imItemEancode.getEanCode().trim().toUpperCase());
        }else{
	    imItemEancode.setEanCode("");
	}
	
	imItemEancode.setBrandCode(imItemEancode.getReserve5());
	imItemEancode.setReserve5(null);
	
	log.info("imItemEancode.getItemCode() = " + imItemEancode.getItemCode());
	log.info("imItemEancode.getBrandCode() = " + imItemEancode.getBrandCode());
	log.info("imItemEancode.getReserve5() = " + imItemEancode.getReserve5());
    	log.info("====<setDefaultValues/>=======");
	return imItemEancode;
    }
    
    /**
     * 檢查每一筆國際碼
     * @param imItem
     */
    public String doValidateEanCode(ImItemEancode imItemEancodeImport, int index) throws Exception{
	log.info("====<doValidateEanCode>=======");
	ImItemEancodeDAO imItemEancodeDAO = (ImItemEancodeDAO) SpringUtils.getApplicationContext().getBean("imItemEancodeDAO");
	ImItemDAO imItemDAO = (ImItemDAO) SpringUtils.getApplicationContext().getBean("imItemDAO");
	
	String otherItemCode = null;
	
	StringBuffer returnString = new StringBuffer();
	
	String brandCode = imItemEancodeImport.getBrandCode();
	if( brandCode.indexOf("T2") > -1){
        	    
	    String enable = imItemEancodeImport.getEnable();
	    String eanCode = imItemEancodeImport.getEanCode();
	    String itemCode = imItemEancodeImport.getItemCode();
            log.info("eanCode = " + eanCode);
            
            // enable
            if(!StringUtils.hasText(enable)){
        	imItemEancodeImport.setEnable("Y");
            }else{
        	if( !"Y".equals(enable) && !"N".equals(enable) ){
        	    throw new Exception("是否為啟用欄位錯誤，非Y/N");
        	}	
            }
            
            // eanCode
            if(!StringUtils.hasText(eanCode)){
        	throw new Exception("第"+index+"筆品號:"+itemCode+"未輸入國際碼");
            }else{
        	// 打國際碼,查出是否有相同B品號
                ImItem line = (ImItem)imItemDAO.findImItem(brandCode, eanCode,"Y");
                if(null != line && "Y".equalsIgnoreCase(enable)){ // 若匯入的國際碼為啟用且找出已有商品主檔相同品號
                    // 匯入的國際碼與已存在的品號相同
		    imItemEancodeImport.setEnable("N");
		    returnString.append("國際碼:"+eanCode+"與已啟用的品號相同故變更為停用");
//		    throw new Exception("國際碼:"+eanCode+"與已啟用的品號相同故變更為停用");
                    
                }else{
                    // 檢核相同國際碼是否有啟用
                    List<ImItemEancode> lines = imItemEancodeDAO.findEanCodeByProperty(brandCode, eanCode) ;
                    if( null != lines && lines.size() > 0 ){
                	// 查出是否有相同國際碼且啟用的
                	for (ImItemEancode imItemEancodeSelect : lines) {
                	    log.info("imItemEancodeSelect.getEanCode() = " + imItemEancodeSelect.getEanCode());
                	    log.info("imItemEancodeImport.getItemCode() = " + itemCode);
                	    log.info("imItemEancodeSelect.getItemCode() = " + imItemEancodeSelect.getItemCode());
                	    if(imItemEancodeSelect.getEanCode().equals(eanCode) && !itemCode.equals(imItemEancodeSelect.getItemCode())){
                		otherItemCode = imItemEancodeSelect.getItemCode();
                		if("Y".equals(enable)){
                		    // 有相同國際碼且啟用
                		    imItemEancodeImport.setEnable("N");
                		    returnString.append("已被品號:"+otherItemCode+"國際碼啟用故變更為停用");
                		}
                		break;
                	    }
                	}
                    }	
                }
            }
	}	
	log.info("====<doValidateEanCode/>=======");
	return returnString.toString();
    }
    
    /**
     * 補indexNo
     * @param imItemEancode
     * @return
     */
    public ImItemEancode replenishColumnImItemEanCode(ImItemEancode imItemEancode){
	ImItemEancodeDAO imItemEancodeDAO = (ImItemEancodeDAO) SpringUtils.getApplicationContext().getBean("imItemEancodeDAO");
	
	// indexNo
	Long indexNo = 1L;
	List<ImItemEancode> imItemEancodes = imItemEancodeDAO.findByProperty("ImItemEancode", "", "and brandCode = ? and itemCode = ?", new Object[]{imItemEancode.getBrandCode(), imItemEancode.getItemCode()}, "order by indexNo");
	if(null != imItemEancodes && imItemEancodes.size() > 0){
	    indexNo = imItemEancodes.get(imItemEancodes.size() - 1 ).getIndexNo() + 1; 
	}
	imItemEancode.setIndexNo(indexNo);
	log.info("indexNo = " + indexNo);
	return imItemEancode;
    }
}
