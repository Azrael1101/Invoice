package tw.com.tm.erp.importdb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuEmployeeAward;
import tw.com.tm.erp.hbm.dao.BuEmployeeAwardDAO;
import tw.com.tm.erp.hbm.service.BuEmployeeAwardService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.User;

public class BuEmployeeAwardDeleteImportData implements ImportDataAbs{
	private static final Log log = LogFactory.getLog(BuEmployeeAwardDeleteImportData.class);

	public ImportInfo initial(HashMap uiProperties) {
		log.info("BuEmployeeAwardDeleteImportData.initial");
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);	
		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.BuEmployeeAward.class.getName());

		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue(ImportDBService.DEFAULT_KEY_HEAD + "0");

		imInfo.addFieldName("key");
		// set field type
		imInfo.setFieldType("key", "java.lang.String"); 

		if("T2".equals(user.getBrandCode())){
			imInfo.setXlsColumnLength(2);
			doT2Initial(imInfo);
			imInfo.setOneRecordDetailKeys(new int[] { 2 });
		}

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyyMMdd");

		// set default value
		HashMap defaultValue = new HashMap();
		defaultValue.put("lastUpdateDate", new Date());
		defaultValue.put("lastUpdatedBy", user.getEmployeeCode() );

		imInfo.setDefaultValue(defaultValue);

		// add detail
		imInfo.setImportDataStartRecord(1);
		return imInfo;
	}

	private ImportInfo doT2Initial(ImportInfo imInfo){

		// set field NAME
		imInfo.addFieldName("employeeCode"); // 工號
		imInfo.addFieldName("indexNo"); // 項次

		// set field type
		imInfo.setFieldType("employeeCode", "java.lang.String");
		imInfo.setFieldType("indexNo", "java.lang.Long");

		return imInfo;
	}

	public String updateDB(List entityBeans, ImportInfo info) throws Exception {
		BuEmployeeAwardDAO buEmployeeAwardDAO = (BuEmployeeAwardDAO) SpringUtils.getApplicationContext().getBean("buEmployeeAwardDAO");
		BuEmployeeAwardService buEmployeeAwardService = (BuEmployeeAwardService) SpringUtils.getApplicationContext().getBean("buEmployeeAwardService");

		StringBuffer result = new StringBuffer();

		int allCount = 0;
		int correctCount = 0 ;
		int failCount = 0 ;
		for (int index = 0; index < entityBeans.size(); index++) {
			BuEmployeeAward buEmployeeAward = (BuEmployeeAward) entityBeans.get(index); 
			String importEmployeeCode = buEmployeeAward.getEmployeeCode(); // .getId()
			Long importIndexNo = buEmployeeAward.getIndexNo(); // .getId()

			if ((null != importEmployeeCode) && (StringUtils.hasText(importEmployeeCode)) ) { 
				if( 0 != importIndexNo || null == importIndexNo ){
					BuEmployeeAward oldBuEmployeeAward = (BuEmployeeAward)buEmployeeAwardDAO.findFirstByProperty("BuEmployeeAward", "and employeeCode = ? and indexNo = ? ", new Object[]{importEmployeeCode,importIndexNo});
					// read old data
					if (null == oldBuEmployeeAward) { // 匯入的資料在資料庫沒有
						failCount++;
						log.error("項次: " + importIndexNo + "工號: "+importEmployeeCode+" 工號或項次不存在 " ); // 品牌: " + imItemEancode.getBrandCode()+ "
						result.append("項次: " + importIndexNo + " 工號: "+importEmployeeCode + " 工號或項次不存在 <br/>"); //品號: " + importItemCode+ "
					} else { // 資料庫已存在此項
						try {
							oldBuEmployeeAward.setEnable("N");
							oldBuEmployeeAward.setLastUpdatedBy(buEmployeeAward.getLastUpdatedBy());
							oldBuEmployeeAward.setLastUpdateDate(buEmployeeAward.getLastUpdateDate());
							buEmployeeAwardService.update(oldBuEmployeeAward);
							result.append("工號: ").append(importEmployeeCode)
							.append(" 項次: ").append(importIndexNo)
							.append(" 作廢成功<br/>");
							correctCount++;
						} catch (Exception ex) {
							failCount++;
							log.error(ex.getMessage());
							result.append(ex.getMessage() + "<br/>");
						}
					}
				}else{
					failCount++;
					log.error("第 " + index + " 筆資料未輸入項次");
					result.append("第 " + index + " 筆資料未輸入項次<br/>");
				}
			}else{
				failCount++;
				log.error("第 " + index + " 筆資料未輸入工號");
				result.append("第 " + index + " 筆資料未輸入工號<br/>");
			}
			if(StringUtils.hasText(importEmployeeCode)){
				allCount++;
			}
		}
		result.append("匯入總筆數 : " + allCount  + " 成功: " + correctCount + " 失敗: " + failCount );
		return result.toString();
	}
}
