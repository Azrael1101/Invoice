package tw.com.tm.erp.importdb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuEmployeeAward;
import tw.com.tm.erp.hbm.bean.BuEmployeeAwardCategory;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuGoal;
import tw.com.tm.erp.hbm.bean.BuGoalEmployeeLine;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeAwardDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.service.BuEmployeeAwardService;
import tw.com.tm.erp.hbm.service.BuGoalService;
import tw.com.tm.erp.hbm.service.BuOrderTypeService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.User;

public class BuGoalImportData implements ImportDataAbs{
	private static final Log log = LogFactory.getLog(BuGoalImportData.class);

	private static ApplicationContext context = SpringUtils.getApplicationContext();
	
	public ImportInfo initial(HashMap uiProperties) {
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);	
		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.BuGoal.class.getName());

		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue(ImportDBService.DEFAULT_KEY_HEAD + "0");
		imInfo.addFieldName("key");
		
		// set field type
		imInfo.setFieldType("key", "java.lang.String"); 

		imInfo.setXlsColumnLength(6);
		doInitial(imInfo);
		imInfo.setOneRecordDetailKeys(new int[] { 6 });

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyyMMdd");

		// 預設值
		HashMap defaultValue = new HashMap();
		defaultValue.put("creationDate", new Date());
		defaultValue.put("lastUpdateDate", new Date());
		defaultValue.put("createdBy", user.getEmployeeCode() );
		defaultValue.put("lastUpdatedBy", user.getEmployeeCode() );
		defaultValue.put("brandCode", user.getBrandCode() );
		imInfo.setDefaultValue(defaultValue);
		
		// 從第二筆資料開始
		imInfo.setImportDataStartRecord(1);
		return imInfo;
	}

	private ImportInfo doInitial(ImportInfo imInfo){

		//set field name
		imInfo.addFieldName("goalCode"); // 目標代號
		imInfo.addFieldName("department"); // 部門代號
		imInfo.addFieldName("descrption"); // 敘述
		imInfo.addFieldName("year"); // 年
		imInfo.addFieldName("month"); // 月
		imInfo.addFieldName("goal"); // 目標

		// set field type
		imInfo.setFieldType("goalCode", "java.lang.String");
		imInfo.setFieldType("department", "java.lang.String");
		imInfo.setFieldType("descrption", "java.lang.String");
		imInfo.setFieldType("year", "java.lang.String");
		imInfo.setFieldType("month", "java.lang.String");
		imInfo.setFieldType("goal", "java.lang.Double");
		return imInfo;
	}

	public String updateDB(List entityBeans, ImportInfo info) throws Exception {
		
		BuGoalService buGoalService = (BuGoalService) context.getBean("buGoalService");
		
		StringBuffer result = new StringBuffer("<br/>");
		
		int allCount = 0;
		int correctCount = 0 ;
		int failCount = 0 ;
		
		for (int index = 0; index < entityBeans.size(); index++) {
			BuGoal buGoal = (BuGoal) entityBeans.get(index); // 匯入的目標主黨
			String goalCode = buGoal.getGoalCode(); //
			// 新增
			if ((null != goalCode) && (StringUtils.hasText(goalCode)) ) { 
				try {
					BuGoal oldBuGoal = buGoalService.findBoGoalByKeyProperty(
							buGoal.getBrandCode(), buGoal.getGoalCode(), buGoal.getYear(), buGoal.getMonth());
					
					if(null == oldBuGoal){
						log.info("save");
						buGoalService.save(buGoal);
						result.append("目標代號: ").append(buGoal.getGoalCode()).append(" 新增成功<br/>");
					}else{
						log.info("update");
						oldBuGoal.setDepartment(buGoal.getDepartment());
						oldBuGoal.setDescrption(buGoal.getDescrption());
						oldBuGoal.setGoal(buGoal.getGoal());
						oldBuGoal.setLastUpdatedBy(buGoal.getLastUpdatedBy());
						oldBuGoal.setLastUpdateDate(buGoal.getLastUpdateDate());
						buGoalService.update(oldBuGoal);
						result.append("目標代號: ").append(oldBuGoal.getGoalCode()).append(" 修改成功<br/>");
					}
					correctCount++;
				} catch (Exception ex) {
					failCount++;
					log.error(ex.getMessage());
					result.append(ex.getMessage() + "<br/>");
				}
			}else{
				failCount++;
				log.error("第 " + index + " 筆資料未輸入目標代號");
				result.append("第 " + index + " 筆資料未輸入目標代號<br/>");
			}
			
			if(StringUtils.hasText(goalCode)){
				allCount++;
			}
		}
		result.append("<br/>匯入總筆數 : " + allCount  + "<br/>成功: " + correctCount + "<br/>失敗: " + failCount );
		return result.toString();
	}
}