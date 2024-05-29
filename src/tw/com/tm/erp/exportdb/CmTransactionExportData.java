package tw.com.tm.erp.exportdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.CmTransactionView;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.CmD8TransactionDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.hbm.service.CmTransactionService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;

public class CmTransactionExportData {
	private static final Log log = LogFactory.getLog(CmTransactionService.class);
	public static String PROCESS_NAME = "TRANSACTION_TO_SQLSERVER";

	private BaseDAO baseDAO;
	private CmD8TransactionDAO cmD8TransactionDAO;
	private CmTransactionService cmTransactionService;
	private BuBrandService buBrandService;
	private NativeQueryDAO nativeQueryDAO;

	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}
	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
	public void setCmD8TransactionDAO(CmD8TransactionDAO cmD8TransactionDAO) {
		this.cmD8TransactionDAO = cmD8TransactionDAO;
	}
	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}
	public void setCmTransactionService(CmTransactionService cmTransactionService) {
		this.cmTransactionService = cmTransactionService;
	}

	public void executeCmTransaction(Map parameterMap){
		String uuid = UUID.randomUUID().toString();
		Date date = new Date();
		String opUser = (String)parameterMap.get("OP_USER");
		Long sum = 0L;
		Long sumSuccess = 0L;
		Long sumError = 0L;
		String orderTypeCode = null;
		boolean isTransCloseMonthStartDate = false;  //是否從關帳月的月初轉
		try {

			String customerWarehouseCode = (String)parameterMap.get("CUSTOMER_WAREHOUSE_CODE");
			String dateType = (String)parameterMap.get("DATE_TYPE");

			// 撈資料庫 BU_COMMON_PHRASE_HEAD CustomsAuditConfig
			BuCommonPhraseLine buCommonPhraseLine = (BuCommonPhraseLine)baseDAO.findFirstByProperty("BuCommonPhraseLine","", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[]{"CustomsAuditConfig", customerWarehouseCode, "Y"}, "order by indexNo" );

			if(null == buCommonPhraseLine){
				throw new Exception(" 查無 CustomsAuditConfig, " + customerWarehouseCode + " 配置");
			}

			int CustomsWatchBafferDate = NumberUtils.getInt(buCommonPhraseLine.getAttribute1()); // 海關給予緩衝要看的資料至少是幾天前的資料
			int transactionAppointDateBeforeNow = NumberUtils.getInt(buCommonPhraseLine.getAttribute2()); // 轉資料是預設幾天前的資料
			int pageSize = NumberUtils.getInt(buCommonPhraseLine.getAttribute3()); // 每天轉檔每次轉多少筆

			String endString = DateUtils.getAppointDateCompareDate(CustomsWatchBafferDate);
			String startString = DateUtils.getAppointDateCompareDate(DateUtils.parseDate(endString),transactionAppointDateBeforeNow);

			Date endDate = DateUtils.parseDate(endString);
			Date startDate = DateUtils.parseDate(startString);

			// 若日期放錯則交換
			Date temp = null;
			if(endDate.before(startDate)){
				temp = endDate;
				endDate = startDate;
				startDate = temp;

				endString = DateUtils.format(endDate, DateUtils.C_DATE_PATTON_DEFAULT);
				startString = DateUtils.format(startDate, DateUtils.C_DATE_PATTON_DEFAULT);
			}

			// 權重最小，若 startDate 小於 關帳日 以關帳日的下月初替代 
			BuBrand buBrand = buBrandService.findById("T2");
			String monthlyCloseMonth = buBrand.getMonthlyCloseMonth();
			Date monthlyCloseMonthDate = DateUtils.getLastDateOfMonth(DateUtils.parseDate("yyyyMMdd",monthlyCloseMonth+"01"));
			if(startDate.before(monthlyCloseMonthDate)){
				startString = DateUtils.format(DateUtils.getFirstDateOfMonth(monthlyCloseMonthDate, 1), DateUtils.C_DATE_PATTON_DEFAULT);

				if(endDate.before(monthlyCloseMonthDate)){
					endString = DateUtils.format(DateUtils.getFirstDateOfMonth(monthlyCloseMonthDate, 1), DateUtils.C_DATE_PATTON_DEFAULT);
				}
			}else if(endDate.before(monthlyCloseMonthDate)){
				startString = DateUtils.format(DateUtils.getFirstDateOfMonth(monthlyCloseMonthDate, 1), DateUtils.C_DATE_PATTON_DEFAULT);
				endString = DateUtils.format(DateUtils.getFirstDateOfMonth(monthlyCloseMonthDate, 1), DateUtils.C_DATE_PATTON_DEFAULT);
			}

			// 權重次之，撈BIA.RPT_CONFIG 的VALUE3，若VALUE3等於Y，則以月關帳的月初到現在替代
			// 最後則將 BIA.RPT_CONFIG 的VALUE3改成N  nativeQueryDAO.executeNativeSql
			List rptConfigs = nativeQueryDAO.executeNativeSql("SELECT * FROM BIA.RPT_CONFIG WHERE 1=1 AND CONFIG_CODE = 'IS_MONTH_CLOSE' and VALUE1 = 'T2'" );
			if(null != rptConfigs && rptConfigs.size() > 0){
				Object[] rptConfig = (Object[])(rptConfigs.get(0));
				String value3 = String.valueOf(rptConfig[5]);
				if(StringUtils.hasText(value3) && "Y".equalsIgnoreCase(value3)){
					startString = DateUtils.format(DateUtils.getFirstDateOfMonth(monthlyCloseMonthDate,  0), DateUtils.C_DATE_PATTON_DEFAULT);
					isTransCloseMonthStartDate = true;
				}
			}

			// 權重最大，指定日期則覆蓋掉不管
			if(parameterMap.containsKey("START_DATE") && !parameterMap.containsKey("END_DATE") ){
				startString = (String)parameterMap.get("START_DATE");
				endString = (String)parameterMap.get("START_DATE");
			}else if( !parameterMap.containsKey("START_DATE") && parameterMap.containsKey("END_DATE") ){
				startString = (String)parameterMap.get("END_DATE");
				endString = (String)parameterMap.get("END_DATE");
			}else if( parameterMap.containsKey("START_DATE") && parameterMap.containsKey("END_DATE") ){
				startString = (String)parameterMap.get("START_DATE");
				endString = (String)parameterMap.get("END_DATE");
			}

			// 是否指定轉單別
			if(parameterMap.containsKey("ORDER_TYPE_CODE")){
				orderTypeCode = (String)parameterMap.get("ORDER_TYPE_CODE");
			}

			SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_INFO,"開始從日期:" + startString + "至日期:" + endString + "轉資料至 SQL SERVER" , date, uuid, opUser);

			log.info("uuid = " + uuid);
			log.info("now = " + date);
			log.info("startDate = " + startString);
			log.info("endDate = " + endString);
			log.info("pageSize = " + pageSize);
			log.info("customerWarehouseCode = " + customerWarehouseCode);
			log.info("orderTypeCode = " + orderTypeCode);
			log.info("dateType = " + dateType);

			parameterMap.put("NOW", date);
			parameterMap.put("UUID", uuid);
			parameterMap.put("START_DATE", startString);
			parameterMap.put("END_DATE", endString);
			parameterMap.put("PAGE_SIZE", pageSize); 
			parameterMap.put("DATE_TYPE", dateType); // 決定是用更新日期轉還是交易日期轉

			String actStartDate = (String)parameterMap.get("START_DATE");
			String actEndDate = (String)parameterMap.get("END_DATE");
			final List<String> betweenDays = DateUtils.getDaysBetweenList(actStartDate, actEndDate);

			// 分天
			for (String day : betweenDays) {
				try {
					cmTransactionService.deleteCmTransaction(parameterMap, day);
				} catch (Exception e) {
					log.error(e.getMessage());
					SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_ERROR, e.getMessage(), date, uuid, opUser);
				}  

				try {
					Double count = cmD8TransactionDAO.findCountCmD8Transaction(parameterMap, day);
					log.info("關別:" +customerWarehouseCode + " 日期:" + day + "共有:" + count + "筆" + " 共分:" + Math.ceil(count/pageSize) + "頁");

					sum += count.longValue();

					// 分頁
					for (int page = 1; page <= Math.ceil(count/pageSize) ; page++) {
						List<CmTransactionView> part = cmD8TransactionDAO.findPartCmD8Transaction(parameterMap, day, page);
						// 成功幾筆
						Long success = 0L;
						Long error = 0L;
						// 分筆
						for (CmTransactionView cmTransactionView : part) {
							try {
								cmTransactionService.executeCmTransaction(parameterMap, cmTransactionView);
								success++;
							} catch (Exception e) {
								error--;
								log.error(e.getMessage());
								SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_ERROR, e.getMessage(), date, uuid, opUser);
							}
						}

						sumSuccess += success;
						sumError += error;

						String msg = "保稅稽核系統 => 關別:" + customerWarehouseCode +" 新增日期:" + day + " 總筆數:" + count+ "筆  第" +page+ "頁 成功筆數:" + success + " 失敗筆數:"+ error;
						log.info(msg);
						SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_INFO, msg, date, uuid, opUser);
					}

				} catch (Exception e) {
					log.error(e.getMessage());
					SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_ERROR, e.getMessage(), date, uuid, opUser);
				}  
			}

			// 若是從關帳月的月初轉，則將VALUE3改成N，表示不再從關帳月初轉
			if(isTransCloseMonthStartDate){
				DataSource dataSource = (DataSource) SpringUtils.getApplicationContext().getBean("dataSource");
				Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement("UPDATE BIA.RPT_CONFIG SET VALUE3 = 'N' WHERE 1=1 AND CONFIG_CODE = 'IS_MONTH_CLOSE' and VALUE1 = 'T2'");
				stmt.executeUpdate(); 
			}

			String msg = "保稅稽核系統 => 關別:" + customerWarehouseCode + " 所有日期總筆數:" + sum + "筆,共成功筆數:" + sumSuccess + " 失敗筆數:"+ sumError;
			log.info(msg);
			SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_INFO, msg, date, uuid, opUser);

			SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_INFO,"日期:" + startString + "至日期:" + endString + "轉資料至 SQL SERVER結束" , date, uuid, opUser);

		} catch (Exception e) {
			log.error(e.getMessage());
			SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_ERROR,"轉至SQL SERVER發生錯誤，原因 ：" + e.getMessage(), date, uuid, opUser);
		} finally{

			// 寄給維護人員
			MailUtils.systemErrorLogSendMail(PROCESS_NAME, MessageStatus.LOG_ERROR,uuid);
		}
	}
}
