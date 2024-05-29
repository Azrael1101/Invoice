package tw.com.tm.erp.importdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.StringUtil;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCompose;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.service.BuAddressBookService;
import tw.com.tm.erp.hbm.service.BuBasicDataService;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.ImItemCategoryService;
import tw.com.tm.erp.hbm.service.ImItemPriceService;
import tw.com.tm.erp.hbm.service.ImItemService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.ValidateUtil;

public class CustomerUpdateImportData implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(CustomerUpdateImportData.class);
	
	private static String SPACE = "<S>";
	
	public ImportInfo initial(HashMap uiProperties) {
		log.info("CustomerUpdateImportData.initial");
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);			
		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.BuAddressBook.class.getName());

		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue(ImportDBService.DEFAULT_KEY_HEAD + "0");
		imInfo.addFieldName("key");
		
		// set field type
		imInfo.setFieldType("key", "java.lang.String");
		
		imInfo.setXlsColumnLength(5);
		doInitial(imInfo);
		imInfo.setOneRecordDetailKeys(new int[] { 5 });
		
		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyy/MM/dd");

		// set default value
		HashMap defaultValue = new HashMap();
		defaultValue.put("creationDate", new Date());
		defaultValue.put("lastUpdateDate", new Date());
		defaultValue.put("createdBy", user.getEmployeeCode() );
		defaultValue.put("lastUpdatedBy", user.getEmployeeCode() );
		imInfo.setDefaultValue(defaultValue);
		
		// 從第二筆資料開始
		imInfo.setImportDataStartRecord(1);
		return imInfo;
	}

	private ImportInfo doInitial(ImportInfo imInfo){
		
		//add field name
		imInfo.addFieldName("identityCode"); // 身份證明代號(X01....)
		imInfo.addFieldName("chineseName"); // 中文名稱
		imInfo.addFieldName("birthday"); // 生日
//		imInfo.addFieldName("birthdayYear"); // 年
//		imInfo.addFieldName("birthdayMonth"); // 月
//		imInfo.addFieldName("birthdayDay"); // 日
		imInfo.addFieldName("mobilePhone"); //手機
		imInfo.addFieldName("address"); // 地址

		// set field type
		imInfo.setFieldType("identityCode", "java.lang.String");
		imInfo.setFieldType("chineseName", "java.lang.String");
		imInfo.setFieldType("birthday", "java.lang.String");
//		imInfo.setFieldType("birthdayYear", "java.lang.Long");
//		imInfo.setFieldType("birthdayMonth", "java.lang.Long");
//		imInfo.setFieldType("birthdayDay", "java.lang.Long");
		imInfo.setFieldType("mobilePhone", "java.lang.String");
		imInfo.setFieldType("address", "java.lang.String");
		return imInfo;
	}
	
    public String updateDB(List entityBeans, ImportInfo info) throws Exception {
    	log.info("CustomerUpdateImportData.updateDB");
    	BuAddressBookService buAddressBookService = (BuAddressBookService)SpringUtils.getApplicationContext().getBean("buAddressBookService");
    	
    	StringBuffer result = new StringBuffer();
    	
    	int allCount = 0;
    	int correctCount = 0 ;
    	int failCount = 0 ;
    	
    	for (int index = 0; index < entityBeans.size(); index++) {
    	
    	    BuAddressBook buAddressBook = (BuAddressBook) entityBeans.get(index); // 匯入的通訊錄
    	    
    	    String importIdentityCode = buAddressBook.getIdentityCode(); // 身分證明代號
    	    String importChineseName = buAddressBook.getChineseName();   // 中文姓名
       	    String importBirthday = buAddressBook.getBirthday();   		 // 生日  
//    	    Long importBirthdayYear = buAddressBook.getBirthdayYear();   // 年
//    	    Long importBirthdayMonth = buAddressBook.getBirthdayMonth(); // 月
//    	    Long importBirthdayDay = buAddressBook.getBirthdayDay();     // 日
    	    String importMobilePhone = buAddressBook.getMobilePhone();	 // 手機
    	    String importAddress = buAddressBook.getAddress();       	 // 地址
    	    log.info("importIdentityCode = " + importIdentityCode);
    	    log.info("importChineseName = " + importChineseName);
    	    log.info("importBirthday = " + importBirthday);
//    	    log.info("importBirthdayYear = " + importBirthdayYear + "importBirthdayMonth = " + importBirthdayMonth + "importBirthdayDay = " + importBirthdayDay);
    	    log.info("importMobilePhone = " + importMobilePhone);
    	    log.info("importAddress = " + importAddress);
    	   
			Long importBirthdayYear = null;  	// 年
    	    Long importBirthdayMonth = null; 	// 月
    	    Long importBirthdayDay = null;       // 日
    	    if(null != importBirthday && importBirthday.length()<=10){
    	    	String[] splitStr = importBirthday.split("/");
    	    	importBirthdayYear = Long.valueOf(splitStr[0]);
	    	    importBirthdayMonth = Long.valueOf(splitStr[1]);
	    	    importBirthdayDay = Long.valueOf(splitStr[2]);
    	    }else{
    	    	result.append("匯入資料有問題: 生日格式錯誤 <br/>");
    	    }
    	    
    	    
    	    if ((null != buAddressBook) && (StringUtils.hasText(importIdentityCode) && StringUtils.hasText(importChineseName)) ) { 
    	    	BuAddressBook oldBuAddressBook = buAddressBookService.findIdentityCodeByProperty(importIdentityCode);
    		// read old data
    		if (null == oldBuAddressBook) { // 匯入的身分證號資料在資料庫沒有
    		    failCount++;
    		    log.error("匯入資料有問題 身分證號: "+importIdentityCode+"不存在" );
    		    result.append("匯入資料有問題 身分證號: " + importIdentityCode+ " 姓名: "+importChineseName + " 錯誤原因 : 身分證號不存在 <br/>");
    		} else {
    			// 資料庫已存在
    			try {
    				oldBuAddressBook.setChineseName(importChineseName);
    				oldBuAddressBook.setBirthdayYear(importBirthdayYear);
    				oldBuAddressBook.setBirthdayMonth(importBirthdayMonth);
    				oldBuAddressBook.setBirthdayDay(importBirthdayDay);
    				oldBuAddressBook.setMobilePhone(importMobilePhone);
    				oldBuAddressBook.setAddress(importAddress);
    				oldBuAddressBook.setLastUpdateDate(new Date());
    				oldBuAddressBook.setLastUpdatedBy(buAddressBook.getLastUpdatedBy());
    				buAddressBookService.update(oldBuAddressBook);
    			    correctCount++;
    			    
    			} catch (Exception ex) {
    			    failCount++;
    			    ex.printStackTrace();
    			    log.error("匯入資料有問題 身分證號: "
    				    + importIdentityCode + " 姓名: "
    				    + importChineseName
    				    + ex.getMessage());
    			    result.append("匯入資料有問題 身分證號: "
    				    + importIdentityCode + " 姓名: "
    				    + importChineseName + " 錯誤原因 : "
    				    + ex.getMessage() + "<br/>");
    			}
    		    
    		 }
    	    }else{
    		log.info("第" + index + " 筆未輸入身分證代號與姓名");	 
    			 
    	    }
    	    if(StringUtils.hasText(importIdentityCode)&& StringUtils.hasText(importChineseName)){
    		allCount++;
    	    }  

    	}
    	result.append("匯入總筆數 : " + allCount  + " 成功: " + correctCount + " 失敗: " + failCount );
    	return result.toString();
    }
}
