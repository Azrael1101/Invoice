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
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.dao.BuEmployeeAwardDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.service.BuEmployeeAwardService;
import tw.com.tm.erp.hbm.service.BuOrderTypeService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.User;

public class BuEmployeeAwardImportData implements ImportDataAbs{
	private static final Log log = LogFactory.getLog(BuEmployeeAwardImportData.class);

	private static ApplicationContext context = SpringUtils.getApplicationContext();
	
	public ImportInfo initial(HashMap uiProperties) {
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);	
		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.BuEmployeeAward.class.getName());

		// set key info
		imInfo.setKeyIndex(1);
		imInfo.setKeyValue(ImportDBService.DEFAULT_KEY_HEAD + "0");
		imInfo.addFieldName("key");
		// set field type
		imInfo.setFieldType("key", "java.lang.String"); 

		if("T2".equals(user.getBrandCode())){
			imInfo.setXlsColumnLength(6);
			doT2Initial(imInfo);
			imInfo.setOneRecordDetailKeys(new int[] { 6 });
		}

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyyMMdd");

		// set default value
		HashMap defaultValue = new HashMap();
		defaultValue.put("creationDate", new Date());
		defaultValue.put("lastUpdateDate", new Date());
		defaultValue.put("createdBy", user.getEmployeeCode() );
		defaultValue.put("lastUpdatedBy", user.getEmployeeCode() );
		defaultValue.put("brandCode", user.getBrandCode() );
		defaultValue.put("orderTypeCode", "LPA" );
		defaultValue.put("enable", "Y" );
		imInfo.setDefaultValue(defaultValue);
		// add detail
		imInfo.setImportDataStartRecord(1);
		return imInfo;
	}

	private ImportInfo doT2Initial(ImportInfo imInfo){

		//set field name
		imInfo.addFieldName("employeeCode"); // 工號 id.
		imInfo.addFieldName("occurrenceDate"); // 日期
		imInfo.addFieldName("point"); // 點數
		imInfo.addFieldName("category01"); // 大類
		imInfo.addFieldName("category02"); // 小類
		imInfo.addFieldName("memo"); // 備註

		// set field type
		imInfo.setFieldType("employeeCode", "java.lang.String"); // 
		imInfo.setFieldType("occurrenceDate", "java.util.Date");
		imInfo.setFieldType("point", "java.lang.Double");
		imInfo.setFieldType("category01", "java.lang.String");
		imInfo.setFieldType("category02", "java.lang.String");
		imInfo.setFieldType("memo", "java.lang.String");
		return imInfo;
	}

	public String updateDB(List entityBeans, ImportInfo info) throws Exception {
		BuEmployeeAwardDAO buEmployeeAwardDAO = (BuEmployeeAwardDAO) context.getBean("buEmployeeAwardDAO");
		BuEmployeeAwardService buEmployeeAwardService = (BuEmployeeAwardService) context.getBean("buEmployeeAwardService");
		StringBuffer result = new StringBuffer("<br/>");
		
		int allCount = 0;
		int correctCount = 0 ;
		int failCount = 0 ;
		
		HashMap defaultValue = info.getDefaultValue();
		String brandCode = (String)defaultValue.get("brandCode");
		String orderTypeCode =  (String)defaultValue.get("orderTypeCode");
		BuOrderTypeService buOrderTypeService = (BuOrderTypeService) context.getBean("buOrderTypeService");
		String orderNo = buOrderTypeService.getOrderSerialNo(brandCode, orderTypeCode);
		BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(brandCode, orderTypeCode));
		buOrderType.setLastOrderNo(orderNo);
		buOrderTypeService.update(buOrderType);
		result.append("單別: " + orderTypeCode + "<br/>單號: " + orderNo + "<br/><br/>");
		
		for (int index = 0; index < entityBeans.size(); index++) {
			BuEmployeeAward importBuEmployeeAward = (BuEmployeeAward) entityBeans.get(index); // 匯入的商品國際碼
			String importEmployeeCode = importBuEmployeeAward.getEmployeeCode(); // .getId()
			// 新增
			if ((null != importEmployeeCode) && (StringUtils.hasText(importEmployeeCode)) ) { 
				try {
					// 檢核資料
					doValidate(importBuEmployeeAward);
					// 取得新indexNo
					newImport(importBuEmployeeAward, buEmployeeAwardDAO);
					// 存檔
					importBuEmployeeAward.setOrderTypeCode(orderTypeCode);
					importBuEmployeeAward.setOrderNo(orderNo);
					buEmployeeAwardService.save(importBuEmployeeAward);
					result.append("工號: ").append(importEmployeeCode).append(" 新增成功<br/>");
					correctCount++;
				} catch (Exception ex) {
					failCount++;
					log.error(ex.getMessage());
					result.append(ex.getMessage() + "<br/>");
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
		result.append("<br/>匯入總筆數 : " + allCount  + "<br/>成功: " + correctCount + "<br/>失敗: " + failCount );
		return result.toString();
	}

	/**
	 * 複製匯入的資料
	 * @param importBuEmployeeAward
	 * @param oldBuEmployeeAward
	 */
	public void copyImport(BuEmployeeAward importBuEmployeeAward, BuEmployeeAward oldBuEmployeeAward){
		oldBuEmployeeAward.setOccurrenceDate(importBuEmployeeAward.getOccurrenceDate());
		oldBuEmployeeAward.setPoint(importBuEmployeeAward.getPoint());
		oldBuEmployeeAward.setCategory01(importBuEmployeeAward.getCategory01());
		oldBuEmployeeAward.setCategory02(importBuEmployeeAward.getCategory02());
		oldBuEmployeeAward.setMemo(importBuEmployeeAward.getMemo());
	}

	/**
	 * 新增匯入的資料
	 * @param importBuEmployeeAward
	 * @param oldBuEmployeeAward
	 */
	public void newImport(BuEmployeeAward importBuEmployeeAward, BuEmployeeAwardDAO buEmployeeAwardDAO){
		Long maxIndexNo = -1L;
		String importEmployeeCode = importBuEmployeeAward.getEmployeeCode();
		List<BuEmployeeAward> oldBuEmployeeAwards = (List<BuEmployeeAward>)buEmployeeAwardDAO.findByProperty("BuEmployeeAward","", "and id.employeeCode = ? ", new Object[]{importEmployeeCode}," order by id.indexNo desc");
		if(oldBuEmployeeAwards.size() > 0 ){
			for (BuEmployeeAward buEmployeeAward2 : oldBuEmployeeAwards) {
				maxIndexNo = buEmployeeAward2.getIndexNo() + 1L; // .getId()
				break;
			}
		}else{
			maxIndexNo = 1L;
		}
		importBuEmployeeAward.setIndexNo(maxIndexNo);
	}

	/***
	 * 檢核資料
	 * @param importBuEmployeeAward
	 * @throws Exception
	 */
	private void doValidate(BuEmployeeAward importBuEmployeeAward) throws Exception{
		String employeeCode = importBuEmployeeAward.getEmployeeCode();
		String category01 = importBuEmployeeAward.getCategory01();
		String category02 = importBuEmployeeAward.getCategory02();
		String brandCode = importBuEmployeeAward.getBrandCode();
		Date occurrenceDate = importBuEmployeeAward.getOccurrenceDate();

		BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO = (BuEmployeeWithAddressViewDAO) context.getBean("buEmployeeWithAddressViewDAO");
		BuEmployeeAwardDAO buEmployeeAwardDAO = (BuEmployeeAwardDAO) context.getBean("buEmployeeAwardDAO");

		// 檢查工號
		BuEmployeeWithAddressView employeeWithAddressView = 
			buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, employeeCode);
		if(null == employeeWithAddressView){
			throw new NoSuchDataException("工號: " + employeeCode +" 不存在");
		}

		// 檢查大類
		BuEmployeeAwardCategory buEmployeeAwardCategory1 = buEmployeeAwardDAO.findOneCategoryCode(brandCode, "CATEGORY01", category01, "Y");
		if(null == buEmployeeAwardCategory1){
			throw new NoSuchDataException("工號: " + employeeCode
					+ " 大類: "+category01+" 不存在");
		}

		// 檢查中類
		BuEmployeeAwardCategory buEmployeeAwardCategory2 = buEmployeeAwardDAO.findOneCategoryCode(brandCode, "CATEGORY02", category02, "Y");
		if(null == buEmployeeAwardCategory2){
			throw new NoSuchDataException("工號: " + employeeCode
					+ " 中類: "+category02+" 不存在");
		}else{
			// 檢查中類的父類是否對應正常
			BuEmployeeAwardCategory buEmployeeAwardCategory = buEmployeeAwardDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY02, category02, category01, "Y");
			if( null == buEmployeeAwardCategory ){
				throw new NoSuchDataException("工號:" + employeeCode
						+ " 大類 "+category01+" 不包含中類: "+category02);
			}
		}
		
		//檢查輸入點數
		if(null == importBuEmployeeAward.getPoint()){
			throw new NoSuchDataException("工號:" + employeeCode+ " 點數不可為空值");
		}
		
		//檢查可用點數
		Double avaliablePoint = buEmployeeAwardDAO.getAvailablePoint(importBuEmployeeAward);
		if((avaliablePoint + importBuEmployeeAward.getPoint()) < 0)
			throw new NoSuchDataException("工號:" + employeeCode+ " 剩餘點數不足 ("+avaliablePoint+")");

		//檢查匯入日期
		if(occurrenceDate.after(new Date())){
			throw new NoSuchDataException("工號:" + employeeCode+ " 日期:" + 
					DateUtils.format(occurrenceDate, DateUtils.C_DATA_PATTON_YYYYMMDD) + " 不可超過今天");
		}
		
		Date when = DateUtils.addMonths(new Date(), -3);
		//檢查匯入日期
		/*
		if(occurrenceDate.before(when)){
			throw new NoSuchDataException("工號:" + employeeCode+ " 日期:" + 
					DateUtils.format(occurrenceDate, DateUtils.C_DATA_PATTON_YYYYMMDD) + " 不可為小於三個月前");
		}
		*/
	}
}
