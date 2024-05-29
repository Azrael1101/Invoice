package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;
import java.util.Date;

public class DbcInformationColumns implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -181626844478427735L;
	
	private Long sysSno;
	private String tableName;
	private String columnName;
	private String columnComments;
	private Long columnIndex;
	private String enable;
	private String dataType;
	private Long dataSize;
	private String isRowId;
	private String notNull;
	private String sysModifierAmail;
	private Date sysLastUpdateTime;
	private DbcInformationTables dbcInformationTables;
	
	/**
	 * 
	 */
	public DbcInformationColumns() {
	}

	/**
	 * @param sysSno
	 * @param tableName
	 * @param columnName
	 * @param columnComments
	 * @param columnIndex
	 * @param enable
	 * @param dataType
	 * @param dataSize
	 * @param isRowId
	 * @param notNull
	 * @param sysModifierAmail
	 * @param sysLastUpdateTime
	 */
	public DbcInformationColumns(Long sysSno, String tableName, String columnName, String columnComments,
			Long columnIndex, String enable, String dataType, Long dataSize, String isRowId, String notNull,
			String sysModifierAmail, Date sysLastUpdateTime, DbcInformationTables dbcInformationTables) {
		this.sysSno = sysSno;
		this.tableName = tableName;
		this.columnName = columnName;
		this.columnComments = columnComments;
		this.columnIndex = columnIndex;
		this.enable = enable;
		this.dataType = dataType;
		this.dataSize = dataSize;
		this.isRowId = isRowId;
		this.notNull = notNull;
		this.sysModifierAmail = sysModifierAmail;
		this.sysLastUpdateTime = sysLastUpdateTime;
		this.dbcInformationTables = dbcInformationTables;
	}

	public Long getSysSno() {
		return sysSno;
	}

	public void setSysSno(Long sysSno) {
		this.sysSno = sysSno;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnComments() {
		return columnComments;
	}

	public void setColumnComments(String columnComments) {
		this.columnComments = columnComments;
	}

	public Long getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(Long columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Long getDataSize() {
		return dataSize;
	}

	public void setDataSize(Long dataSize) {
		this.dataSize = dataSize;
	}

	public String getIsRowId() {
		return isRowId;
	}

	public void setIsRowId(String isRowId) {
		this.isRowId = isRowId;
	}

	public String getNotNull() {
		return notNull;
	}

	public void setNotNull(String notNull) {
		this.notNull = notNull;
	}

	public String getSysModifierAmail() {
		return sysModifierAmail;
	}

	public void setSysModifierAmail(String sysModifierAmail) {
		this.sysModifierAmail = sysModifierAmail;
	}

	public Date getSysLastUpdateTime() {
		return sysLastUpdateTime;
	}

	public void setSysLastUpdateTime(Date sysLastUpdateTime) {
		this.sysLastUpdateTime = sysLastUpdateTime;
	}
	
	public DbcInformationTables getDbcInformationTables() {
		return dbcInformationTables;
	}
	
	public void setDbcInformationTables(DbcInformationTables dbcInformationTables) {
		this.dbcInformationTables = dbcInformationTables;
	}
}
