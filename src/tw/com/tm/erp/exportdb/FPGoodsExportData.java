package tw.com.tm.erp.exportdb;

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
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.CmTransactionView;
import tw.com.tm.erp.hbm.bean.FPGoods;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.CmD8TransactionDAO;
import tw.com.tm.erp.hbm.dao.FPGoodsDAO;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.CmTransactionService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;

public class FPGoodsExportData {
    private static final Log log = LogFactory.getLog(CmTransactionService.class);
    public static String PROCESS_NAME = "FPGoods_TO_SQLSERVER";
    
    private BaseDAO baseDAO;
    private FPGoodsDAO fPGoodsDAO;
    
    private CmTransactionService cmTransactionService;
    
    public void setFPGoodsDAO(FPGoodsDAO fPGoodsDAO) {
	this.fPGoodsDAO = fPGoodsDAO;
    }
    public void setBaseDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }
    public void setCmTransactionService(CmTransactionService cmTransactionService) {
        this.cmTransactionService = cmTransactionService;
    }
    
    public void executeFPGoods(Map parameterMap){
	String uuid = UUID.randomUUID().toString();
	Date date = new Date();
	String opUser = (String)parameterMap.get("OP_USER");
	Long sumDeleteSuccess = 0L;
	Long sumDeleteError = 0L;
	Long sumInsertSuccess = 0L;
	Long sumInsertError = 0L;
	Double count = 0D;
	String msg = null;
	try {
	    
	    String customerWarehouseCode = (String)parameterMap.get("CUSTOMER_WAREHOUSE_CODE");
	    String lastUpdateDateStart = (String)parameterMap.get("LAST_UPDATE_DATE_START");
	    String lastUpdateDateEnd = (String)parameterMap.get("LAST_UPDATE_DATE_END");
	    
	    // 撈資料庫 BU_COMMON_PHRASE_HEAD CustomsAuditConfig
	    BuCommonPhraseLine buCommonPhraseLine = (BuCommonPhraseLine)baseDAO.findFirstByProperty("BuCommonPhraseLine","", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[]{"FPGoodsConfig", customerWarehouseCode, "Y"}, "order by indexNo" );
	    
	    if(null == buCommonPhraseLine){
		throw new Exception(" 查無 FPGoodsConfig, " + customerWarehouseCode + " 配置");
	    }
	    
	    int pageSize = NumberUtils.getInt(buCommonPhraseLine.getAttribute1()); // 每頁轉多少筆
	    String isByLastUpdateDate = buCommonPhraseLine.getAttribute2(); // 是否依據更新日期
	    
	    if("YES".equalsIgnoreCase(isByLastUpdateDate) && !StringUtils.hasText(lastUpdateDateStart)){
		lastUpdateDateStart = DateUtils.getCurrentDateStr(DateUtils.C_DATA_PATTON_YYYYMMDD);
		lastUpdateDateEnd = DateUtils.format(DateUtils.addDays(new Date(),1), DateUtils.C_DATA_PATTON_YYYYMMDD);
		
		parameterMap.put("LAST_UPDATE_DATE_START", lastUpdateDateStart);
		parameterMap.put("LAST_UPDATE_DATE_END", DateUtils.format(DateUtils.addDays(new Date(),1), DateUtils.C_DATA_PATTON_YYYYMMDD));
	    }
	    
	    SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_INFO,"開始轉商品主檔資料至 SQL SERVER" , date, uuid, opUser);
	    
	    log.info("uuid = " + uuid);
	    log.info("now = " + date);
	    log.info("pageSize = " + pageSize);
	    log.info("customerWarehouseCode = " + customerWarehouseCode);
	    log.info("lastUpdateDateStart = " + lastUpdateDateStart);
	    log.info("lastUpdateDateEnd = " + lastUpdateDateEnd);
	    log.info("isByLastUpdateDate = " + isByLastUpdateDate);
	    
	    parameterMap.put("NOW", date);
	    parameterMap.put("UUID", uuid);
	    parameterMap.put("PAGE_SIZE", pageSize); 
	    
	    try {
		FPGoodsDAO fPGoodsDAO = (FPGoodsDAO) SpringUtils.getApplicationContext().getBean("fPGoodsDAO");
		log.info("fPGoodsDAO = " + fPGoodsDAO);
		count = fPGoodsDAO.findCountFPGoods(parameterMap);
		// 分頁
		log.info("關別:" +customerWarehouseCode + " 共有:" + count + "筆" + " 共分:" + Math.ceil(count/pageSize) + "頁");
		
		for(int page = 1; page <= Math.ceil(count/pageSize) ; page++){
		    List<FPGoods> part = fPGoodsDAO.findPartFPGoods(parameterMap, page);
		    
		    // (刪除)成功幾筆
		    Long deleteSuccess = 0L;
		    Long deleteError = 0L;
		    // (新增)成功幾筆
		    Long insertSuccess = 0L;
		    Long insertError = 0L;
		    for (FPGoods goods : part) {
			
			// 刪除
			try {
			    int delete = cmTransactionService.deleteFPGoods(parameterMap, goods); // 刪ms sql
			    if(delete == 1){
				deleteSuccess++;
			    }
			} catch (Exception e) {
			    e.printStackTrace();
			    deleteError--;
			    log.error(e.getMessage());
			    SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_ERROR, e.getMessage(), date, uuid, opUser);
			}
			
			// 新增
			try {
			    cmTransactionService.executeFPGoods(parameterMap, goods); // 刪ms sql
			    insertSuccess++;
			} catch (Exception e) {
			    e.printStackTrace();
			    insertError--;
			    log.error(e.getMessage());
			    SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_ERROR, e.getMessage(), date, uuid, opUser);
			}
		    }
		    
		    sumDeleteSuccess += deleteSuccess;
		    sumDeleteError += deleteError;
		    sumInsertSuccess += insertSuccess;
		    sumInsertError += insertError;
		    
		    msg = "保稅稽核系統 => 關別:" + customerWarehouseCode +" 刪除品號 總筆數:" + count+ "筆  第" +page+ "頁 成功筆數:" + deleteSuccess + " 失敗筆數:"+ deleteError;
		    log.info(msg);
		    SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_INFO, msg, date, uuid, opUser);

		    msg = "保稅稽核系統 => 關別:" + customerWarehouseCode +" 新增品號 總筆數:" + count+ "筆  第" +page+ "頁 成功筆數:" + insertSuccess + " 失敗筆數:"+ insertError;
		    log.info(msg);
		    SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_INFO, msg, date, uuid, opUser);
		    
		}
	    } catch (Exception e) {
		e.printStackTrace();
		log.error(e.getMessage());
		SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_ERROR, e.getMessage(), date, uuid, opUser);
	    }  
	    
	    msg = "保稅稽核系統 => 關別:" + customerWarehouseCode + " 品號總筆數:" + count + "筆, 刪除sqlServer品號共成功筆數:" + sumDeleteSuccess + " 失敗筆數:"+ sumDeleteError + " ,新增sqlServer品號共成功筆數:" + sumInsertSuccess + " 失敗筆數:"+ sumInsertError;
	    log.info(msg);
	    SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_INFO, msg, date, uuid, opUser);
	    
	    SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_INFO,"轉商品主檔資料至 SQL SERVER結束" , date, uuid, opUser);
	    
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error(e.getMessage());
	    SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_ERROR,"轉至SQL SERVER發生錯誤，原因 ：" + e.getMessage(), date, uuid, opUser);
	}
    }
}
