package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuGoalEmployeeLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuGoalEmployee implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -820104695966586064L;
	private Long lineId;
	private BuGoal buGoal;
    private String brandCode; // 暫存欄位
    private String goalCode; // 暫存欄位
    private String year; // 暫存欄位
    private String month; // 暫存欄位
	private String workType;
	private String employeeCode;
	private String employeeName; 	// 非db欄位 工號名字
	private Double employeeGoal; 
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private Long indexNo;
	private String isDeleteRecord;
	private String isLockRecord;
	private String message;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	
    // Constructors
	public Long getLineId() {
		return lineId;
	}
	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}
	public BuGoal getBuGoal() {
		return buGoal;
	}
	public void setBuGoal(BuGoal buGoal) {
		this.buGoal = buGoal;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Double getEmployeeGoal() {
		return employeeGoal;
	}
	public void setEmployeeGoal(Double employeeGoal) {
		this.employeeGoal = employeeGoal;
	}
	public String getReserve1() {
		return reserve1;
	}
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	public String getReserve2() {
		return reserve2;
	}
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	public String getReserve3() {
		return reserve3;
	}
	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}
	public String getReserve4() {
		return reserve4;
	}
	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}
	public String getReserve5() {
		return reserve5;
	}
	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
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
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getGoalCode() {
		return goalCode;
	}
	public void setGoalCode(String goalCode) {
		this.goalCode = goalCode;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
}