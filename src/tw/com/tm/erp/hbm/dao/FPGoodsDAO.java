package tw.com.tm.erp.hbm.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exportdb.CmTransactionExportData;
import tw.com.tm.erp.hbm.bean.CmTransactionView;
import tw.com.tm.erp.hbm.bean.FPGoods;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;

public class FPGoodsDAO {
    private static final Log log = LogFactory.getLog(FPGoodsDAO.class);
    
    private String FW = "FW";
    private String FD = "FD";
    private String HD = "HD";
    
    private DataSource dataSource;
    private DataSource dataSourceFW;
    private DataSource dataSourceFD;
    private DataSource dataSourceHD;
    
    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    public void setDataSourceFW(DataSource dataSourceFW) {
	this.dataSourceFW = dataSourceFW;
    }

    public void setDataSourceFD(DataSource dataSourceFD) {
	this.dataSourceFD = dataSourceFD;
    }
    
    public void setDataSourceHD(DataSource dataSourceHD) {
	this.dataSourceHD = dataSourceHD;
    }
    /**
     * 刪除多筆商品主檔
     * @param parameterMap
     * @throws Exception
     */
    public int deleteFPGoods(Map parameterMap, FPGoods fPGoods) throws Exception{
	int deleteRow = 0; 
	Date now = null;
	String uuid = null; 
	String opUser = (String)parameterMap.get("OP_USER");
	JdbcTemplate jdbcTemplateCustoms = null;
	final FPGoods fPGoodsTmp = fPGoods;
	try {
	    now = (Date)parameterMap.get("NOW");
	    uuid = (String)parameterMap.get("UUID");
	    
	    final String customerWarehouseCode = (String)PropertyUtils.getProperty(parameterMap, "CUSTOMER_WAREHOUSE_CODE");
	    if(customerWarehouseCode.equalsIgnoreCase(FW)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceFW);
	    }else if(customerWarehouseCode.equalsIgnoreCase(FD)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceFD);
	    }else if(customerWarehouseCode.equalsIgnoreCase(HD)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceHD);
	    }

	    StringBuffer sql = new StringBuffer();
	    
	    sql.append("delete FROM FP_GOODS where FP_GD_ID = ? and FP_FP = ? ");  //_TMP
	    
	    deleteRow = jdbcTemplateCustoms.update(sql.toString(), new PreparedStatementSetter() {
		public void setValues(PreparedStatement ps) throws SQLException {
		    ps.setString(1, fPGoodsTmp.getFpGdId()); 
		    ps.setString(2, fPGoodsTmp.getFpFp()); 
		}
	    });
	    
	    String msg = "保稅稽核系統 => 關別:" + customerWarehouseCode +" 刪除品號:" + fPGoodsTmp.getFpGdId() + "成功  "; 
//	    log.info(msg);
	    SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_INFO, msg, now, uuid, opUser);
	    sql.delete(0, sql.length());
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("刪除 FPGoods 失敗，原因：" + e.getMessage());
	    throw new Exception(e.toString());
	}
	return deleteRow;
    }
    
    /**
     * 轉到 SQL SERVER FPGoods 
     * @param parameterMap
     * @throws Exception
     */
    public void executeFPGoods(Map parameterMap, FPGoods fPGoods)throws Exception, SQLException{
	JdbcTemplate jdbcTemplateCustoms = null;
	final Date now = (Date)parameterMap.get("NOW");
	final String uuid = (String)parameterMap.get("UUID");
	final String opUser = (String)parameterMap.get("OP_USER");
	final String customerWarehouseCode = (String)PropertyUtils.getProperty(parameterMap, "CUSTOMER_WAREHOUSE_CODE");
	
	String fpName1tmp = null;
	if(StringUtils.hasText(fPGoods.getFpName1())){
	    fpName1tmp = CommonUtils.insertCharacterWithLimitedLength(AjaxUtils.getPropertiesValue(fPGoods.getFpName1(),""), 24, 24, CommonUtils.SPACE, "R");
	}
	final String fpGdId = fPGoods.getFpGdId();
	final String fpFp = fPGoods.getFpFp();
	final String fpName1 = fpName1tmp;
	final String fpUnitId = fPGoods.getFpUnitId();
	
	try {
	    if(customerWarehouseCode.equalsIgnoreCase(FW)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceFW);
	    }else if(customerWarehouseCode.equalsIgnoreCase(FD)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceFD);
	    }else if(customerWarehouseCode.equalsIgnoreCase(HD)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceHD);
	    }
	    
	    if( null == fpGdId ){
		String nullMsg = "查 稅別:" + fpFp + " 品名:" + fpName1 + " 交易單位: " + fpUnitId + " 品號fpGdId 為 null ";
//		log.info(nullMsg);
		throw new SQLException(nullMsg);
	    }

	    if( null == fpFp ){
	    }
	    
	    if( null == fpName1 ){
//		String nullMsg = "查 品號:"+fpGdId+"稅別:" + fpFp + " 交易單位: " + fpUnitId + " 品名 為 null ";
//		log.info(nullMsg);
//		SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_ERROR, nullMsg, now, uuid, opUser);
//		fpName1 = "";
//		throw new SQLException(nullMsg);
	    }

	    if( null == fpUnitId ){
//		String nullMsg = "查 品號:"+fpGdId+"稅別:" + fpFp + " 品名:" + fpName1 + " 交易單位 為 null ";
//		log.info(nullMsg);
//		SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_ERROR, nullMsg, now, uuid, opUser);
//		fpUnitId = "";
//		throw new SQLException(nullMsg);
	    }

	    StringBuffer inserSql = new StringBuffer();
	    inserSql.append("INSERT INTO FP_GOODS VALUES (?,?,?,?)"); // _TMP
	    int insertRow = jdbcTemplateCustoms.update(inserSql.toString(), new PreparedStatementSetter() {
		public void setValues(PreparedStatement ps) throws SQLException {

		    ps.setString(1,fpGdId); // fpGdId  -> fpGdId
		    ps.setString(2,fpFp); // fpFp -> fpFp
		    ps.setString(3, fpName1); // fpName1 -> fpName1
		    ps.setString(4,fpUnitId); // fpUnitId -> fpUnitId
		}
	    } );

	} catch (Exception e) {
	    StringBuffer sbMsg = new StringBuffer("關別:" + customerWarehouseCode + " 品號:" + fpGdId +" 新增失敗");
	    sbMsg.append( " 原因:" + e.getMessage());
	    log.error(sbMsg.toString());
	    throw new Exception(sbMsg.toString());
	}
    }
    
    /**
     * 查出分頁多筆商品主檔
     * @param parameterMap
     * @return
     */
    public List<FPGoods> findPartFPGoods(Map parameterMap, int page)throws Exception{
	
	Integer pageSize = (Integer)parameterMap.get("PAGE_SIZE");

	String lastUpdateDateStart = (String)parameterMap.get("LAST_UPDATE_DATE_START");
	String lastUpdateDateEnd = (String)parameterMap.get("LAST_UPDATE_DATE_END");
	    
	JdbcTemplate jdbcTemplate = null;
	StringBuffer sql = new StringBuffer();
	
	sql.append("select * from (SELECT ROWNUM rn, T.* FROM ( SELECT * FROM CMAUDIT.FP_GOODS where FP_GD_ID NOT LIKE '%RB410171951F' ");
	
	if(StringUtils.hasText(lastUpdateDateStart)){
	    sql.append("AND LAST_UPDATE_DATE >= TO_DATE('").append(lastUpdateDateStart).append("', 'YYYYMMDD') ");
	}
	if(StringUtils.hasText(lastUpdateDateEnd)){
	    sql.append("AND LAST_UPDATE_DATE <= TO_DATE('").append(lastUpdateDateEnd).append("', 'YYYYMMDD') ");
	}
	
	sql.append(")T WHERE ROWNUM <= ").append(pageSize * page).append(" ) where rn >= ").append((pageSize * (page-1) + 1)).append(" ORDER BY FP_GD_ID DESC ");
	try {
	    jdbcTemplate = new JdbcTemplate(dataSource);
	    final String fsql = sql.toString();
	    
	    return jdbcTemplate.query( 
		    fsql,
		    new RowMapper() {
			public Object mapRow(java.sql.ResultSet rs, int rowNum) throws SQLException {
			    FPGoods FPGoods = new FPGoods();
			    FPGoods.setFpGdId(rs.getString("FP_GD_ID"));
			    FPGoods.setFpFp(rs.getString("FP_FP"));
			    FPGoods.setFpName1(rs.getString("FP_NAME_1"));
			    FPGoods.setFpUnitId(rs.getString("FP_UNIT_ID"));
			    return FPGoods;
			}
		    });
	} catch(Exception e){
	    StringBuffer sbMsg = new StringBuffer(" 撈品號:第"+page+"頁 撈部份資料失敗 ");
	    sbMsg.append( " 原因:" + e.getMessage());
	    log.error(sbMsg.toString());
	    throw new Exception(sbMsg.toString());
	}
    }
    
    /**
     * 轉到 SQL SERVER FPGoods 
     * @param parameterMap
     * @throws Exception
     */
    public Double findCountFPGoods(Map parameterMap)throws Exception, SQLException{
	JdbcTemplate jdbcTemplate = null;
	final String customerWarehouseCode = (String)PropertyUtils.getProperty(parameterMap, "CUSTOMER_WAREHOUSE_CODE");
	StringBuffer sql = new StringBuffer();
	try {
	    jdbcTemplate = new JdbcTemplate(dataSource);
	    
	    String lastUpdateDateStart = (String)parameterMap.get("LAST_UPDATE_DATE_START");
	    String lastUpdateDateEnd = (String)parameterMap.get("LAST_UPDATE_DATE_END");
	    
	    sql.append("SELECT count(FP_GD_ID) FROM CMAUDIT.FP_GOODS where FP_GD_ID NOT LIKE '%RB410171951F' ");
	   
	    if(StringUtils.hasText(lastUpdateDateStart)){
		sql.append("AND LAST_UPDATE_DATE >= TO_DATE( '").append(lastUpdateDateStart).append("', 'YYYYMMDD') ");
	    }
	    
	    if(StringUtils.hasText(lastUpdateDateEnd)){
		sql.append("AND LAST_UPDATE_DATE <= TO_DATE( '").append(lastUpdateDateEnd).append("', 'YYYYMMDD') ");
	    }
	    
	    
	    final String fsql = sql.toString();
	    
	    return  (Double)jdbcTemplate.queryForObject(
		    fsql,
		    java.lang.Double.class
		    );
	    
	} catch (Exception e) {
	    StringBuffer sbMsg = new StringBuffer("關別:" + customerWarehouseCode +" :撈商品主檔總數發生錯誤 ");
	    sbMsg.append( " 原因:" + e.getMessage());
	    log.error(sbMsg.toString());
	    throw new Exception(sbMsg.toString());
	}
    }
}
