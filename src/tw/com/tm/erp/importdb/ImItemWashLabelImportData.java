package tw.com.tm.erp.importdb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.service.ImItemService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.User;


/**
 * 洗標匯入
 * @author asd
 *
 */
public class ImItemWashLabelImportData implements ImportDataAbs{
    private static final Log log = LogFactory.getLog(ImItemWashLabelImportData.class);
    
    public ImportInfo initial(HashMap uiProperties) {
	log.info("ImItemWashLabelImportData.initial");
	User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);	
	log.info("user.getBrandCode() = " + user.getBrandCode());
	
	ImportInfo imInfo = new ImportInfo();

	// set entity class name
	imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImItem.class.getName());

	// set key info
	imInfo.setKeyIndex(0);
	imInfo.setKeyValue(ImportDBService.DEFAULT_KEY_HEAD + "0");

	imInfo.addFieldName("key");
	// set field type
	imInfo.setFieldType("key", "java.lang.String"); 
	
	if( user.getBrandCode().indexOf("T1") > -1 ){
		imInfo.setXlsColumnLength( 20 );
		doT1Initial(imInfo);
		imInfo.setOneRecordDetailKeys(new int[] { 20 });
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
	
	imInfo.setDefaultValue(defaultValue);

	// add detail
	
	imInfo.setImportDataStartRecord(1);
	return imInfo;
    }
    
    private ImportInfo doT1Initial(ImportInfo imInfo){

	imInfo.addFieldName("brandCode");  // 品牌
	imInfo.addFieldName("itemCode");   // 商品品號
	imInfo.addFieldName("show01"); // 表布1
	imInfo.addFieldName("show01Percent"); // 表布1百分比
	imInfo.addFieldName("show02"); // 表布2
	imInfo.addFieldName("show02Percent"); // 表布2百分比
	imInfo.addFieldName("show03"); // 表布3
	imInfo.addFieldName("show03Percent"); // 表布3百分比
	imInfo.addFieldName("lininging01"); // 裡布1
	imInfo.addFieldName("lininging01Percent"); // 裡布1百分比
	imInfo.addFieldName("lininging02"); // 裡布2
	imInfo.addFieldName("lininging02Percent"); // 裡布2百分比
	imInfo.addFieldName("lininging03"); // 裡布3
	imInfo.addFieldName("lininging03Percent"); // 裡布3百分比
	
	imInfo.addFieldName("washIconPath01");   // 圖片路徑1
	imInfo.addFieldName("washIconPath02");   // 圖片路徑2
	imInfo.addFieldName("washIconPath03");   // 圖片路徑3
	imInfo.addFieldName("washIconPath04");   // 圖片路徑4
	imInfo.addFieldName("washIconPath05");   // 圖片路徑5
	imInfo.addFieldName("washIconPath06");   // 圖片路徑6
	
	// set field type
	imInfo.setFieldType("brandCode", "java.lang.String");
	imInfo.setFieldType("itemCode", "java.lang.String");
	imInfo.setFieldType("show01", "java.lang.String");
	imInfo.setFieldType("show01Percent", "java.lang.Long");
	imInfo.setFieldType("show02", "java.lang.String");
	imInfo.setFieldType("show02Percent", "java.lang.Long");
	imInfo.setFieldType("show03", "java.lang.String");
	imInfo.setFieldType("show03Percent", "java.lang.Long");
	imInfo.setFieldType("lininging01", "java.lang.String");
	imInfo.setFieldType("lininging01Percent", "java.lang.Long");
	imInfo.setFieldType("lininging02", "java.lang.String");
	imInfo.setFieldType("lininging02Percent", "java.lang.Long");
	imInfo.setFieldType("lininging03", "java.lang.String");
	imInfo.setFieldType("lininging03Percent", "java.lang.Long");
	imInfo.setFieldType("washIconPath01", "java.lang.String");
	imInfo.setFieldType("washIconPath02", "java.lang.String");
	imInfo.setFieldType("washIconPath03", "java.lang.String");
	imInfo.setFieldType("washIconPath04", "java.lang.String");
	imInfo.setFieldType("washIconPath05", "java.lang.String");
	imInfo.setFieldType("washIconPath06", "java.lang.String");
	
	return imInfo;
    }
    
    public String updateDB(List entityBeans, ImportInfo info) throws Exception {
    	log.info("ImItemWashLabelImportData.updateDB");
    	ImItemService imItemService = (ImItemService) SpringUtils.getApplicationContext().getBean("imItemService");
    	
	StringBuffer result = new StringBuffer();
	
	int allCount = 0;
	int correctCount = 0 ;
	int failCount = 0 ;
	for (int index = 0; index < entityBeans.size(); index++) {
	    ImItem importImItem = (ImItem) entityBeans.get(index); // 匯入的商品國際碼
	    
	    String importBrandCode = importImItem.getBrandCode();
	    String importItemCode = importImItem.getItemCode();
	    log.info("importItemCode = " + importItemCode);
	    
	    try {
		if ((null != importImItem) && (StringUtils.hasText(importItemCode)) && (StringUtils.hasText(importBrandCode)) ) { // 
		    ImItem oldItem = imItemService.findItem(importBrandCode, importItemCode);
		    // read old data
		    if (null == oldItem) { // 匯入的商品品號資料在資料庫沒有
			failCount++;
			log.error(" 品牌: " + importBrandCode+ " 品號: "+importItemCode+"不存在" );
			result.append(" 品牌: " + importBrandCode+ " 品號: "+importItemCode+ " 錯誤原因 : 品號不存在 <br/>");
		    } else { // 資料庫已存在此品牌的商品

			// 檢核欄位
			doValidate(importImItem,index);

			// 複製
			copyImport(importImItem, oldItem);
			
			imItemService.update(oldItem);

			result.append("商品").append(importItemCode)
			.append(" 更新洗標 成功<br/>");

			correctCount++;
		    }
		}else{
		    
		    log.info("第" + index+1 + " 筆未輸入品號或品牌");	 
		    throw new Exception("第" + index+1 + " 筆未輸入品號或品牌");
		}
	    } catch (Exception ex) {
		failCount++;
		ex.printStackTrace();
		log.error(ex.getMessage());
		result.append(ex.getMessage() + "<br/>");
	    }
	    
	    if(StringUtils.hasText(importItemCode)){
		allCount++;
	    }
	}
	result.append("匯入總筆數 : " + allCount  + " 成功: " + correctCount + " 失敗: " + failCount );
	return result.toString();
    }
    
    /**
     * 檢核 ImItem
     * @param imItem
     * @return
     * @throws Exception
     */
    public ImItem doValidate(ImItem imItem, int index) throws Exception {

	BuBrandDAO buBrandDAO = (BuBrandDAO) SpringUtils.getApplicationContext().getBean("buBrandDAO");

	log.info("imItem.getReserve5() = " + imItem.getReserve5());
	// 依品牌匯入
	String brandCode = imItem.getBrandCode();
	String reserve5 = imItem.getReserve5();

	// 品牌
	if(!StringUtils.hasText(imItem.getBrandCode())){
	    throw new NoSuchDataException("品號:"+brandCode+"未輸入品牌");
	}else{
	    BuBrand buBrand = buBrandDAO.findById(brandCode);
	    if (null == buBrand) {
		throw new NoSuchDataException("品號：" + imItem.getItemCode()
			+ "依據品牌：" + imItem.getBrandCode() + "查無其品牌代號！");
	    }else{
		// 檢核登入人員與匯入所打的品牌是否一致
		if(!imItem.getBrandCode().equals(reserve5)){
		    throw new Exception("作業人員登入品牌: "+reserve5+"與新增品號品牌: " + brandCode +" 不符合，請檢查!");
		}else{
		    imItem.setReserve5(null);
		}
	    }
	}

	return imItem; 
    }
    
    /**
     * 複製
     */
    public void copyImport(ImItem importImItem, ImItem oldItem){
	oldItem.setShow01(importImItem.getShow01());
	oldItem.setShow02(importImItem.getShow02());
	oldItem.setShow03(importImItem.getShow03());
	oldItem.setLininging01(importImItem.getLininging01());
	oldItem.setLininging02(importImItem.getLininging02());
	oldItem.setLininging03(importImItem.getLininging03());
	
	oldItem.setShow01Percent(importImItem.getShow01Percent());
	oldItem.setShow02Percent(importImItem.getShow02Percent());
	oldItem.setShow03Percent(importImItem.getShow03Percent());
	oldItem.setLininging01Percent(importImItem.getLininging01Percent());
	oldItem.setLininging02Percent(importImItem.getLininging02Percent());
	oldItem.setLininging03Percent(importImItem.getLininging03Percent());
	
	oldItem.setWashIconPath01(importImItem.getWashIconPath01());
	oldItem.setWashIconPath02(importImItem.getWashIconPath02());
	oldItem.setWashIconPath03(importImItem.getWashIconPath03());
	oldItem.setWashIconPath04(importImItem.getWashIconPath04());
	oldItem.setWashIconPath05(importImItem.getWashIconPath05());
	oldItem.setWashIconPath06(importImItem.getWashIconPath06());
    }
}
