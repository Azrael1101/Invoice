package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoDeliveryLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoDeliveryLog implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 9095096492452401386L;
	private Long lineId;
	private SoDeliveryHead soDeliveryHead;
	private String logType;
	private String logAction;
	private String logLevel;
	private String message;
	private Date logDate;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private Long indexNo;
	private String createdBy;
	private String createrName;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;

	// Constructors

	/** default constructor */
	public SoDeliveryLog() {
	}

	/** full constructor */
	public SoDeliveryLog(SoDeliveryHead soDeliveryHead, String logType,
			String logAction, String logLevel, String message, Date logDate,
			String reserve1, String reserve2, String reserve3, String reserve4,
			String reserve5, Long indexNo, String createdBy, Date creationDate,
			String lastUpdatedBy, Date lastUpdateDate) {
		this.soDeliveryHead = soDeliveryHead;
		this.logType = logType;
		this.logAction = logAction;
		this.logLevel = logLevel;
		this.message = message;
		this.logDate = logDate;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.indexNo = indexNo;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public SoDeliveryHead getSoDeliveryHead() {
		return this.soDeliveryHead;
	}

	public void setSoDeliveryHead(SoDeliveryHead soDeliveryHead) {
		this.soDeliveryHead = soDeliveryHead;
	}

	public String getLogType() {
		return this.logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getLogAction() {
		return this.logAction;
	}

	public void setLogAction(String logAction) {
		this.logAction = logAction;
	}

	public String getLogLevel() {
		return this.logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getLogDate() {
		return this.logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getReserve1() {
		return this.reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return this.reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return this.reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getReserve4() {
		return this.reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	public String getReserve5() {
		return this.reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}

	public Long getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

}