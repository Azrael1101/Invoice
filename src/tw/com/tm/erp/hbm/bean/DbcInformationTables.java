package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DbcInformationTables implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2036801615378200717L;
	
	private Long sysSno;
	private String area;
	private String tableName;
	private String tableComments;
	private String enable;
	private String sysModifierAmail;
	private Date sysLastUpdateTime;
	private List dbcInformationColumnsList;

	/**
	 * 
	 */
	public DbcInformationTables() {
	}

	/**
	 * @param sysSno
	 * @param area
	 * @param tableName
	 * @param tableComments
	 * @param enable
	 * @param sysModifierAmail
	 * @param sysLastUpdateTime
	 * @param dbcInformationColumnsList
	 */
	public DbcInformationTables(Long sysSno, String area, String tableName, String tableComments, String enable,
			String sysModifierAmail, Date sysLastUpdateTime, List dbcInformationColumnsList) {
		this.sysSno = sysSno;
		this.area = area;
		this.tableName = tableName;
		this.tableComments = tableComments;
		this.enable = enable;
		this.sysModifierAmail = sysModifierAmail;
		this.sysLastUpdateTime = sysLastUpdateTime;
		this.dbcInformationColumnsList = dbcInformationColumnsList;
	}

	public Long getSysSno() {
		return sysSno;
	}

	public void setSysSno(Long sysSno) {
		this.sysSno = sysSno;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableComments() {
		return tableComments;
	}

	public void setTableComments(String tableComments) {
		this.tableComments = tableComments;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
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
	public List getDbcInformationColumnsList() {
		return dbcInformationColumnsList;
	}

	public void setDbcInformationColumnsList(List dbcInformationColumnsList) {
		this.dbcInformationColumnsList = dbcInformationColumnsList;
	}
}
