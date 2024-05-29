package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SiUsersGroup entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiUsersGroup implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -2559313682005316902L;
  private Long lineId;
  //private SiGroup siGroup;
	private String brandCode;
	private String employeeCode;
	private String employeeName;
	private String groupCode;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String updatedBy;
	private Date udpateDate;
	private Long indexNo;
  private String isDeleteRecord = "0"; // 是否被刪除 1 表示要被移除
  private String isLockRecord = "0"; // 是否被鎖定 1 表示鎖定
  private String message; // line 訊息的顯示
  private String showStatus;
	// Constructors

	/** default constructor */
	public SiUsersGroup() {
	}
	
	/** minimal constructor */
	public SiUsersGroup(Long lineId) {
		this.lineId = lineId;
	}
	
	/** full constructor */
	public SiUsersGroup(Long lineId, String brandCode, String employeeCode, String employeeName,
			String groupCode ,String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String updatedBy,
			Date udpateDate, Long indexNo, String isDeleteRecord,
			String isLockRecord, String message , String showStatus) {
		this.lineId = lineId;
		this.brandCode = brandCode;
		this.employeeCode = employeeCode;
		this.employeeName = employeeName;
		this.groupCode = groupCode;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.updatedBy = updatedBy;
		this.udpateDate = udpateDate;
		this.indexNo = indexNo;
		this.isDeleteRecord = isDeleteRecord;
		this.isLockRecord = isLockRecord;
		this.message = message;
	}

	// Property accessors

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

	public Date getUdpateDate() {
		return this.udpateDate;
	}

	public void setUdpateDate(Date udpateDate) {
		this.udpateDate = udpateDate;
	}
	public Long getIndexNo() {
		return this.indexNo;
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

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(String showStatus) {
		this.showStatus = showStatus;
	}

}