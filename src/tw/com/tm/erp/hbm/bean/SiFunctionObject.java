package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SiFunctionObject entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiFunctionObject implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -56662297956347195L;
	private Long lineId;
	private SiFunction siFunction;
	private String objectCode;
	private String objectName;
	private String objectType;
	private String controlType;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String updatedBy;
	private Date updateDate;
	private Long indexNo;
	private String isDeleteRecord = "0"; // 是否被刪除 1 表示要被移除
  private String isLockRecord = "0"; // 是否被鎖定 1 表示鎖定
  private String message; // line 訊息的顯示
	// Constructors

	/** default constructor */
	public SiFunctionObject() {
	}

	/** full constructor */
	public SiFunctionObject(Long lineId, SiFunction siFunction, String objectCode, String objectName,
			String objectType, String controlType, String reserve1,
			String reserve2, String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String updatedBy,
			Date updateDate, Long indexNo, String isDeleteRecord, 
			String isLockRecord, String message) {
		this.lineId = lineId;
		this.siFunction = siFunction;
		this.objectCode = objectCode;
		this.objectName = objectName;
		this.objectType = objectType;
		this.controlType = controlType;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.updatedBy = updatedBy;
		this.updateDate = updateDate;
		this.indexNo = indexNo;
		this.isDeleteRecord = isDeleteRecord;
		this.isLockRecord = isLockRecord;
		this.message = message;
	}

	// Property accessors

	public String getObjectName() {
		return this.objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getControlType() {
		return this.controlType;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
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

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getIsDeleteRecord() {
		return isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public String getIsLockRecord() {
		return isLockRecord;
	}

	public void setIsLockRecord(String isLockRecord) {
		this.isLockRecord = isLockRecord;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public String getObjectCode() {
		return objectCode;
	}

	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}

	public SiFunction getSiFunction() {
		return siFunction;
	}

	public void setSiFunction(SiFunction siFunction) {
		this.siFunction = siFunction;
	}

}