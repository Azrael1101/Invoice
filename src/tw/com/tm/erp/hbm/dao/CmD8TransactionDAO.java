package tw.com.tm.erp.hbm.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exportdb.CmTransactionExportData;
import tw.com.tm.erp.hbm.bean.CmTransactionView;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;

public class CmD8TransactionDAO {
    private static final Log log = LogFactory.getLog(CmD8TransactionDAO.class);
    
    private String FW = "FW";
    private String FD = "FD";
    private String FA = "FA";
    private String HD = "HD";
    private String VD = "VD";
    
    private DataSource dataSource;
    private DataSource dataSourceFW;
    private DataSource dataSourceFD;
    private DataSource dataSourceFA;
    private DataSource dataSourceHD;
    private DataSource dataSourceVD;
    
    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    public void setDataSourceFW(DataSource dataSourceFW) {
	this.dataSourceFW = dataSourceFW;
    }

    public void setDataSourceFD(DataSource dataSourceFD) {
	this.dataSourceFD = dataSourceFD;
    }
    public void setDataSourceFA(DataSource dataSourceFA) {
	this.dataSourceFA = dataSourceFA;
    }
    
    public void setDataSourceHD(DataSource dataSourceHD) {
	this.dataSourceHD = dataSourceHD;
    }
    
    public void setDataSourceVD(DataSource dataSourceVD) {
	this.dataSourceVD = dataSourceVD;
    }
    /**
     * 刪除區間日期
     * @param parameterMap
     * @throws Exception
     */
    public int deleteCmD8Transaction(Map parameterMap, String day) throws Exception{
	int deleteRow = 0;
	Date now = null;
	String uuid = null; 
	String opUser = (String)parameterMap.get("OP_USER");
	JdbcTemplate jdbcTemplateCustoms = null;
	JdbcTemplate jdbcTemplate = null;
	try {
	    now = (Date)parameterMap.get("NOW");
	    uuid = (String)parameterMap.get("UUID");
	    
	    String dateType = (String)parameterMap.get("DATE_TYPE");
	    String orderTypeCode = (String)parameterMap.get("ORDER_TYPE_CODE");
	    
	    final String customerWarehouseCode = (String)PropertyUtils.getProperty(parameterMap, "CUSTOMER_WAREHOUSE_CODE");
	    if(customerWarehouseCode.equalsIgnoreCase(FW)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceFW);
	    }else if(customerWarehouseCode.equalsIgnoreCase(FD)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceFD);
	    }else if(customerWarehouseCode.equalsIgnoreCase(FA)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceFA);
	    }else if(customerWarehouseCode.equalsIgnoreCase(HD)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceHD);
	    }else if(customerWarehouseCode.equalsIgnoreCase(VD)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceVD);
	    }

	    StringBuffer sql = new StringBuffer();
	    log.info("day = " + day);
	    final String tempDay  = day;
	    final String tmpOrderTypeCode = orderTypeCode;
	    
	    if("APF".equals(orderTypeCode)){
	    	StringBuffer sqlAPF = new StringBuffer();
	    	StringBuffer sqlCdAPF = new StringBuffer();
	    	StringBuffer sqlCdAAF = new StringBuffer();
	    	List reqList = null;
	    	try {
	    	    jdbcTemplate = new JdbcTemplate(dataSource);
	    	    
	    	    
	    	    sqlAPF.append("SELECT DISTINCT(CM.SOURCE_ORDER_TYPE_CODE || CM.SOURCE_ORDER_NO) as Sorder FROM CMAUDIT.CM_TRANSACTION CM where 1 = 1");
	    	    
	    	    sqlAPF.append(" and CM.CUSTOMS_WAREHOUSE_CODE = '").append(customerWarehouseCode).append("'");
	    	    sqlAPF.append(" and CM.ORDER_TYPE_CODE = '").append(orderTypeCode).append("'");
	    	    sqlAPF.append(" and CM.SOURCE_ORDER_TYPE_CODE = 'AAF'");
	    		
	    		reqList = jdbcTemplate.queryForList(sqlAPF.toString());
	    		
	    		Iterator it = reqList.iterator();
	    		String vOrder = "";
	    		while(it.hasNext()) {
	    			Map viewMap = (Map) it.next();
	    			vOrder = (String)viewMap.get("Sorder");
	    			sqlCdAAF.append("delete FROM CM_D8_TRANSACTION where cm_cusstk = '"+customerWarehouseCode +"' and cm_doc_id = '"+vOrder +"'");
	    			jdbcTemplateCustoms.execute(sqlCdAAF.toString());
	    		}    		
	    	    
	    		sqlCdAPF.append("delete FROM CM_D8_TRANSACTION where cm_cusstk = '"+customerWarehouseCode +"' and SUBSTRING( cm_doc_id,  1, 3 ) = '"+orderTypeCode +"' and cm_io_date = '"+tempDay+"'");
	    		jdbcTemplateCustoms.execute(sqlCdAPF.toString());
	    		
	    	} catch (Exception e) {
	    	    StringBuffer sbMsg = new StringBuffer("查詢關別:" + customerWarehouseCode +"單別:"+orderTypeCode+" 發生錯誤 ");
	    	    sbMsg.append( " 原因:" + e.getMessage());
	    	    log.error(sbMsg.toString());
	    	    throw new Exception(sbMsg.toString());
	    	}
	    	
	    	
	    }else{
	    	if("cm_cf_date".equals(dateType)){
    		sql.append("delete FROM CM_D8_TRANSACTION where cm_cusstk = ? and SUBSTRING( cm_cf_date,  1, 10 ) = ? "); // CONVERT( char(10),  cm_cf_date, 20 ) >= CAST( ? AS datetime ) and CONVERT( char(10),  cm_cf_date, 20 ) < CAST( ? AS datetime )
    	    }else{ // cm_io_date 預設
    		sql.append("delete FROM CM_D8_TRANSACTION where cm_cusstk = ? and cm_io_date = ? "); 
    	    }
    	    
    	    if(StringUtils.hasText(orderTypeCode)){
    		sql.append("and SUBSTRING(cm_doc_id, 1, 3) = ? ");
    	    }
    	    deleteRow = jdbcTemplateCustoms.update(sql.toString(), new PreparedStatementSetter() {
    		public void setValues(PreparedStatement ps) throws SQLException {
    		    ps.setString(1, customerWarehouseCode); 
    		    ps.setString(2, tempDay); 
    		    if(StringUtils.hasText(tmpOrderTypeCode)){
    			ps.setString(3, tmpOrderTypeCode); 
    		    }
//	    		    ps.setString(3, DateUtils.getAppointDateCompareDate(DateUtils.parseDate(tempDay), 1)); 
    		}
    	    });
	    }

	    String msg = "保稅稽核系統 => 關別:" + customerWarehouseCode +" 刪除日期:" + day + " 刪除筆數:"+deleteRow+"筆"+" 成功  "; 
	    log.info(msg);
	    SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_INFO, msg, now, uuid, opUser);
	    sql.delete(0, sql.length());
	} catch (Exception e) {
	    log.error("刪除 cmD8Transaction 失敗，原因：" + e.getMessage());
	    e.printStackTrace();
	    throw new Exception(e.toString());
	}
	return deleteRow;
    }
    

    /**
     * 轉到 SQL SERVER CmD8Transaction 
     * @param parameterMap
     * @throws Exception
     */
    public Double findCountCmD8Transaction(Map parameterMap, String day)throws Exception, SQLException{
	int insertRow = 0;
	JdbcTemplate jdbcTemplate = null;
	final Date now = (Date)parameterMap.get("NOW");
	final String uuid = (String)parameterMap.get("UUID");
	final String opUser = (String)parameterMap.get("OP_USER");
	final String customerWarehouseCode = (String)PropertyUtils.getProperty(parameterMap, "CUSTOMER_WAREHOUSE_CODE");
	StringBuffer sql = new StringBuffer();
	try {
	    jdbcTemplate = new JdbcTemplate(dataSource);
	    String dateType = (String)parameterMap.get("DATE_TYPE");
	    final String orderTypeCode = (String)parameterMap.get("ORDER_TYPE_CODE");
	    
	    if("cm_cf_date".equals(dateType)){
		sql.append("SELECT count(IO) FROM CMAUDIT.CM_TRANSACTION where CUSTOMS_WAREHOUSE_CODE = ? and LAST_UPDATE_DATE >= TO_DATE( ? , 'YYYY-MM-DD' ) and LAST_UPDATE_DATE < TO_DATE( ? , 'YYYY-MM-DD' ) ");
	    }else{
		sql.append("SELECT count(IO) FROM CMAUDIT.CM_TRANSACTION where CUSTOMS_WAREHOUSE_CODE = ? and TRANSACTION_DATE >= TO_DATE( ? , 'YYYY-MM-DD' ) and TRANSACTION_DATE < TO_DATE( ? , 'YYYY-MM-DD' ) ");
	    }
	    
	    if(StringUtils.hasText(orderTypeCode)){
		sql.append("and ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ");
	    }
	    
	    final String fsql = sql.toString();
	    log.info("保稽SQL:"+fsql);
	    return  (Double)jdbcTemplate.queryForObject(
		    fsql,
		    new Object[] {customerWarehouseCode, day, DateUtils.getAppointDateCompareDate(DateUtils.parseDate(day), 1), },
		    java.lang.Double.class
		    );
	    
	} catch (Exception e) {
	    StringBuffer sbMsg = new StringBuffer("關別:" + customerWarehouseCode +" 新增日期:" + day + " 撈總數發生錯誤 ");
	    sbMsg.append( " 原因:" + e.getMessage());
	    log.error(sbMsg.toString());
	    throw new Exception(sbMsg.toString());
	}
    }
    
    /**
     * 找部分資料
     * @param parameterMap
     * @param day
     * @return
     * @throws Exception
     * @throws SQLException
     */
    public List<CmTransactionView> findPartCmD8Transaction(Map parameterMap, String day, int page)throws Exception, SQLException{
	final Date now = (Date)parameterMap.get("NOW");
	final String uuid = (String)parameterMap.get("UUID");
	final String opUser = (String)parameterMap.get("OP_USER");
	final String customerWarehouseCode = (String)PropertyUtils.getProperty(parameterMap, "CUSTOMER_WAREHOUSE_CODE");
	final String orderTypeCode = (String)parameterMap.get("ORDER_TYPE_CODE");
	Integer pageSize = (Integer)parameterMap.get("PAGE_SIZE");
	JdbcTemplate jdbcTemplate = null;
	StringBuffer sql = new StringBuffer();
	try {
	    jdbcTemplate = new JdbcTemplate(dataSource);
	    String dateType = (String)parameterMap.get("DATE_TYPE");
	    
	    if("cm_cf_date".equals(dateType)){
		sql.append("select * from (SELECT ROWNUM rn, T.* FROM ( SELECT * FROM CMAUDIT.CM_TRANSACTION where CUSTOMS_WAREHOUSE_CODE = ? and LAST_UPDATE_DATE >= TO_DATE( ? , 'YYYY-MM-DD' ) and LAST_UPDATE_DATE < TO_DATE( ? , 'YYYY-MM-DD' ) ");
	    }else{
		sql.append("select * from (SELECT ROWNUM rn, T.* FROM ( SELECT * FROM CMAUDIT.CM_TRANSACTION where CUSTOMS_WAREHOUSE_CODE = ? and TRANSACTION_DATE >= TO_DATE( ? , 'YYYY-MM-DD' ) and TRANSACTION_DATE < TO_DATE( ? , 'YYYY-MM-DD' ) ");
	    }
	    
	    if(StringUtils.hasText(orderTypeCode)){
		sql.append("and ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ");
	    }
	    
	    sql.append(" )T WHERE ROWNUM <= " + pageSize * page+ " ) where rn >= " + (pageSize * (page-1) + 1) );
	    
	    final String fsql = sql.toString();
	    
	    return jdbcTemplate.query( 
		    fsql,
		    new Object[] {customerWarehouseCode, day, DateUtils.getAppointDateCompareDate(DateUtils.parseDate(day), 1)},
		    new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			    CmTransactionView cmTransactionView = new CmTransactionView();
			    cmTransactionView.setIo(rs.getString("IO"));
			    cmTransactionView.setGdType(rs.getString("GD_TYPE"));
			    cmTransactionView.setIoType(rs.getString("IO_TYPE"));
			    cmTransactionView.setD8Type(rs.getString("D8_TYPE"));
//			    cmTransactionView.setDeclType(rs.getString("DECL_TYPE"));
			    cmTransactionView.setDeclarationNo(rs.getString("DECLARATION_NO"));
			    cmTransactionView.setDeclarationSeq(rs.getLong("DECLARATION_SEQ"));
			    cmTransactionView.setDeclarationDate(rs.getDate("DECLARATION_DATE"));
			    cmTransactionView.setDeclQty(rs.getDouble("DECL_QTY"));
			    cmTransactionView.setTransactionDate(rs.getDate("TRANSACTION_DATE"));
			    cmTransactionView.setItemCode(rs.getString("ITEM_CODE"));
			    cmTransactionView.setCustomsWarehouseCode(rs.getString("CUSTOMS_WAREHOUSE_CODE"));
			    cmTransactionView.setQty(rs.getLong("QTY"));
			    cmTransactionView.setOrderTypeCode(rs.getString("ORDER_TYPE_CODE"));
			    cmTransactionView.setOrderNo(rs.getString("ORDER_NO"));
			    cmTransactionView.setIndexNo(rs.getLong("INDEX_NO"));
			    cmTransactionView.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));
			    cmTransactionView.setOriginalDeclarationNo(rs.getString("ORIGINAL_DECLARATION_NO"));
			    cmTransactionView.setOriginalDeclarationSeq(rs.getLong("ORIGINAL_DECLARATION_SEQ"));
			    cmTransactionView.setOriginalDeclarationDate(rs.getDate("ORIGINAL_DECLARATION_DATE"));
//			    cmTransactionView.setGdType(rs.getString("ADJUSTMENT_TYPE"));
			    
			    cmTransactionView.setCmMemo(rs.getString("MEMO")); // 原銷售單號
			    return cmTransactionView;
			}
		    });
	    
	} catch (Exception e) {
	    StringBuffer sbMsg = new StringBuffer("關別:" + customerWarehouseCode +" 新增日期:" + day + " 第"+page+"頁 撈部份資料失敗 ");
	    sbMsg.append( " 原因:" + e.getMessage());
	    log.error(sbMsg.toString());
	    throw new Exception(sbMsg.toString());
	}
	
    }
    
    /**
     * 轉到 SQL SERVER CmD8Transaction 
     * @param parameterMap
     * @throws Exception
     */
    public void executeCmD8Transaction(Map parameterMap, CmTransactionView cmTransactionView)throws Exception, SQLException{
	log.info("進入上傳保稽執行2");
	JdbcTemplate jdbcTemplateCustoms = null;
	final Date now = (Date)parameterMap.get("NOW");
	final String uuid = (String)parameterMap.get("UUID");
	final String opUser = (String)parameterMap.get("OP_USER");
	final String customerWarehouseCode = (String)PropertyUtils.getProperty(parameterMap, "CUSTOMER_WAREHOUSE_CODE");
	
	final String customsWarehouseCode = cmTransactionView.getCustomsWarehouseCode();
	final String io = cmTransactionView.getIo();
	String ioType = cmTransactionView.getIoType();
	String gdType = cmTransactionView.getGdType();
	String d8Type = cmTransactionView.getD8Type();
	String declarationNo = cmTransactionView.getDeclarationNo();
	final Long declarationSeq = cmTransactionView.getDeclarationSeq();
	String itemCode = cmTransactionView.getItemCode();
	final Long qty = cmTransactionView.getQty();
	final String orderTypeCode = cmTransactionView.getOrderTypeCode();
	final String orderNo = cmTransactionView.getOrderNo();
	final Long indexNo = cmTransactionView.getIndexNo();
	String originalDeclarationNo = cmTransactionView.getOriginalDeclarationNo();
	final Long originalDeclarationSeq = cmTransactionView.getOriginalDeclarationSeq();
	final Double declQty = cmTransactionView.getDeclQty();
	final Date transactionDate = cmTransactionView.getTransactionDate();
	final Date declarationDate = cmTransactionView.getDeclarationDate();
	final Date lastUpDateDate = cmTransactionView.getLastUpdateDate(); 
	final Date originalDeclarationDate = cmTransactionView.getOriginalDeclarationDate();
	
	final String cmMemo = cmTransactionView.getCmMemo();
	
	try {
	    if(customerWarehouseCode.equalsIgnoreCase(FW)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceFW);
	    }else if(customerWarehouseCode.equalsIgnoreCase(FD)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceFD);
	    }else if(customerWarehouseCode.equalsIgnoreCase(FA)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceFA);
	    }else if(customerWarehouseCode.equalsIgnoreCase(HD)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceHD);
	    }else if(customerWarehouseCode.equalsIgnoreCase(VD)){
		jdbcTemplateCustoms = new JdbcTemplate(dataSourceVD);
	    }
	    log.info("進入上傳保稽執行3");  
	    if( null == io ){
		String nullMsg = "查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " io 為 null ";
//		log.info(nullMsg);
//		io = "T";
		throw new SQLException(nullMsg);
	    }

	    if( null == ioType ){
		String nullMsg = "關別:" + customerWarehouseCode +" IO:" + io + " transactionDate: " + transactionDate + " orderTypeCode+orderNo:" + orderTypeCode + orderNo+ " indexNo:" + indexNo +" 查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " ioType 為 null ";
//		log.info(nullMsg);
//		SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_ERROR, nullMsg, now, uuid, opUser);
//		ioType = "";
		throw new SQLException(nullMsg);
	    }

	    if( null == gdType ){
		String nullMsg = "關別:" + customerWarehouseCode +" IO:" + io + " transactionDate: " + transactionDate + " orderTypeCode+orderNo:" + orderTypeCode + orderNo+ " indexNo:" + indexNo +" 查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " gdType 為 null ";
		log.info(nullMsg);
		SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_ERROR, nullMsg, now, uuid, opUser);
		gdType = "";
//		throw new SQLException(nullMsg);
	    }

	    if( null == d8Type ){
		String nullMsg = "關別:" + customerWarehouseCode +" IO:" + io + " transactionDate: " + transactionDate + " orderTypeCode+orderNo:" + orderTypeCode + orderNo+ " indexNo:" + indexNo +" 查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " d8Type 為 null ";
		log.info(nullMsg);
		SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_ERROR, nullMsg, now, uuid, opUser);
		d8Type = "";
//		throw new SQLException(nullMsg);
	    }

	    if( null == declarationNo ){
		String nullMsg = "關別:" + customerWarehouseCode +" IO:" + io + " transactionDate: " + transactionDate + " orderTypeCode+orderNo:" + orderTypeCode + orderNo+ " indexNo:" + indexNo +" 查項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " declarationNo 為 null ";
		log.info(nullMsg);
		SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_ERROR, nullMsg, now, uuid, opUser);
		declarationNo = "";
//		throw new SQLException(nullMsg);
	    }

	    if( null == declarationSeq ){
		String nullMsg = "查報單:" + declarationNo + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " declarationSeq 為 null ";
//		log.info(nullMsg);
//		declarationSeq = -1L;
		throw new SQLException(nullMsg);
	    }

	    if( null == itemCode ){
		String nullMsg = "關別:" + customerWarehouseCode +" IO:" + io + " transactionDate: " + transactionDate + " orderTypeCode+orderNo:" + orderTypeCode + orderNo+ " indexNo:" + indexNo +" 查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " itemCode 為 null "; 
		log.info(nullMsg);
		SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_ERROR, nullMsg, now, uuid, opUser);
		itemCode = "";
//		throw new SQLException(nullMsg);
	    }

	    if( null == qty ){
		String nullMsg = "查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " qty 為 null ";
//		log.info(nullMsg);
//		qty = -1L;
		throw new SQLException(nullMsg);
	    }

	    if( null == orderTypeCode ){
		String nullMsg = "查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " orderTypeCode 為 null ";
//		log.info(nullMsg);
//		orderTypeCode = "T";
		throw new SQLException(nullMsg);
	    }

	    if( null == orderNo ){
		String nullMsg = "查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " orderNo 為 null ";
//		log.info(nullMsg);
//		orderNo = "T";
		throw new SQLException(nullMsg);
	    }

	    if( null == indexNo ){
		String nullMsg = "查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " indexNo 為 null ";
//		log.info(nullMsg);
//		indexNo = -1L;
		throw new SQLException(nullMsg);
	    }

	    if( null == originalDeclarationNo ){
		String nullMsg = "關別:" + customerWarehouseCode +" IO:" + io + " transactionDate: " + transactionDate + " orderTypeCode+orderNo:" + orderTypeCode + orderNo+ " indexNo:" + indexNo +" 查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " originalDeclarationNo 為 null ";
		log.info(nullMsg);
		SiSystemLogUtils.createSystemLog(CmTransactionExportData.PROCESS_NAME, MessageStatus.LOG_ERROR, nullMsg, now, uuid, opUser);
		originalDeclarationNo = "";
//		throw new SQLException(nullMsg);
	    }

	    if( null == originalDeclarationSeq ){
		String nullMsg = "查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " originalDeclarationSeq 為 null ";
//		log.info(nullMsg);
//		originalDeclarationSeq = -1L;
		throw new SQLException(nullMsg);
	    }

	    if( null == declQty ){
		String nullMsg = "查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " declQty 為 null ";
//		log.info(nullMsg);
//		declQty = -1D;
		throw new SQLException(nullMsg);
	    }


	    if( null == customsWarehouseCode ){
		String nullMsg = "查報單:" + declarationNo + " 項次:" + declarationSeq + " 品號: " + itemCode + " customsWarehouseCode 為 null ";
//		log.info(nullMsg);
		throw new SQLException(nullMsg);
	    }

	    if( null == transactionDate ){
		String nullMsg = "查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " transactionDate 日期為 null ";
//		log.info(nullMsg);
		throw new SQLException(nullMsg);
	    }

	    if( null == declarationDate ){
		String nullMsg = "查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " declarationDate 日期為 null ";
//		log.info(nullMsg);
		throw new SQLException(nullMsg);
	    }

	    if( null == lastUpDateDate ){
		String nullMsg = "查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " lastUpDateDate 日期為 null ";
//		log.info(nullMsg);
		throw new SQLException(nullMsg);
	    }

	    if( null == originalDeclarationDate ){
		String nullMsg = "查報單:" + declarationNo + " 項次:" + declarationSeq + " 關別:" + customsWarehouseCode + " 品號: " + itemCode + " originalDeclarationDate 日期為 null ";
//		log.info(nullMsg);
		throw new SQLException(nullMsg);
	    }
	    
	    final String fioType = ioType;
	    final String fgdType = gdType;
	    final String fd8Type = d8Type;
	    final String fdeclarationNo = declarationNo;
	    final String fitemCode = itemCode;
	    final String foriginalDeclarationNo = originalDeclarationNo;
	    log.info("進入上傳保稽執行4");
	    StringBuffer inserSql = new StringBuffer();
	    inserSql.append("INSERT INTO CM_D8_TRANSACTION VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); // TEST_BATCH_SEQ.nextval
	    int insertRow = jdbcTemplateCustoms.update(inserSql.toString(), new PreparedStatementSetter() {
		public void setValues(PreparedStatement ps) throws SQLException {
//		    String TEST = "查IO:" + io + " 交易日:" + transactionDate + " 單別:" + orderTypeCode + " 單號: " + orderNo + " 索引:" + indexNo;
//		    log.info(TEST);

		    ps.setString(1,customsWarehouseCode); // cm_cusstk  -> CUSTOMS_WAREHOUSE_CODE
		    ps.setString(2,io); // cm_io -> IO
		    ps.setDate(3, new java.sql.Date(transactionDate.getTime()) ); // cm_io_date -> TRANSACTION_DATE
		    ps.setString(4,fioType); // cm_io_type -> IO_TYPE
		    ps.setString(5,fgdType); // cm_gd_type -> GD_TYPE
		    ps.setString(6, fd8Type); // cm_d8type -> D8_TYPE
		    ps.setString(7, fdeclarationNo); // cm_d8no -> DECLARATION_NO
		    ps.setDouble(8, declarationSeq); // cm_d8seq -> DECLARATION_SEQ
		    ps.setDate(9,  new java.sql.Date(declarationDate.getTime()) ); // cm_d8date -> DECLARATION_DATE
		    ps.setString(10, fitemCode); // cm_cusplu -> ITEM_CODE
		    ps.setDouble(11, qty); // cm_qty -> QTY
		    ps.setString(12, orderTypeCode+orderNo); // cm_doc_id -> ORDER_TYPE_CODE + ORDER_NO
		    ps.setDouble(13, indexNo); // cm_doc_seq -> INDEX_NO
		    ps.setString(14, DateUtils.formatTime(lastUpDateDate, DateUtils.C_TIME_PATTON_DEFAULT) ); // cm_cf_date -> LAST_UPDATE_DATE
		    ps.setString(15, foriginalDeclarationNo); // cm_i_d8no -> ORIGINAL_DECLARATION_NO
		    ps.setDouble(16, originalDeclarationSeq); // cm_i_d8seq -> ORIGINAL_DECLARATION_SEQ
		    ps.setDouble(17, declQty); // cm_i_qty -> DECL_QTY
		    ps.setDate(18, new java.sql.Date(originalDeclarationDate.getTime()) ); // cm_i_d8date -> ORIGINAL_DECLARATION_DATE
		    ps.setDate(19, new java.sql.Date(new Date().getTime())); //TODO : 有問題欄位  cm_kdate -> 系統最大日期
		    ps.setDate(20, new java.sql.Date(new Date().getTime())); // sys_date -> 系統日
		    ps.setString(21, DateUtils.getCurrentDateStr("yyyy")); // sys_year -> 系統年
		    ps.setString(22, DateUtils.getCurrentDateStr("MM")); // sys_month -> 系統月
		    ps.setString(23, cmMemo); // cm_memo -> 原銷售單號
		}
	    } );
	    log.info("進入上傳保稽執行5");
	} catch (Exception e) {
	    StringBuffer sbMsg = new StringBuffer("關別:" + customerWarehouseCode +" IO:" + io + " transactionDate: " + transactionDate + " orderTypeCode+orderNo:" + orderTypeCode + orderNo+ " indexNo:" + indexNo +" 新增失敗");
	    sbMsg.append( " 原因:" + e.getMessage());
	    log.error(sbMsg.toString());
	    throw new Exception(sbMsg.toString());
	}
    }
    
}
