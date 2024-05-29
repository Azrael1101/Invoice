package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuCurrency entity.
 * 
 * @author MyEclipse Persistence Tools
 */
 
public class BuDeleteLog implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5755882819719223238L;

	private Long logId;

	private String cleanTable;

	private String orderTypeCode;

	private Date cleanDate;
	
	private Double cleanDay;

	private String enable;

	private String createdBy;

	private Date creationDate;

	private String lastUpdatedBy;

	private Date lastUpdateDate;
	
	private Long deleteItems;
	
	private String executionTime;
	
	// Constructors

	/** default constructor */
	public BuDeleteLog() {
	}

	/** minimal constructor */
	public BuDeleteLog(Long logId) {
		this.logId = logId;
	}

	/** full constructor */
	public BuDeleteLog(Long logId, String cleanTable, String orderTypeCode, Date cleanDate, 
			Double cleanDay, String enable, String createdBy, Date creationDate, 
			String lastUpdatedBy, Date lastUpdateDate) {
		this.logId = logId;
		this.cleanTable = cleanTable;
		this.orderTypeCode = orderTypeCode;
		this.cleanDate = cleanDate;
		this.cleanDay = cleanDay;
		this.enable = enable;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;		
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getCleanTable() {
		return cleanTable;
	}

	public void setCleanTable(String cleanTable) {
		this.cleanTable = cleanTable;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public Date getCleanDate() {
		return cleanDate;
	}

	public void setCleanDate(Date cleanDate) {
		this.cleanDate = cleanDate;
	}

	public Double getCleanDay() {
		return cleanDay;
	}

	public void setCleanDay(Double cleanDay) {
		this.cleanDay = cleanDay;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Long getDeleteItems() {
		return deleteItems;
	}

	public void setDeleteItems(Long deleteItems) {
		this.deleteItems = deleteItems;
	}

	public String getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}
	
}