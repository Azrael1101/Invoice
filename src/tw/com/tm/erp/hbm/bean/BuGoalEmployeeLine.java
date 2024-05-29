package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuGoalEmployeeLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuGoalEmployeeLine implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -820104695966586064L;
	private Long lineId;
	private BuGoalHead buGoalHead;
	private String employeeItemCategory;
	private String employeeItemSubcategory;
	private String employeeItemBrand;
	private String employeeItemBrandName;	// 非db欄位 品牌名稱
	private String employeeCode;
	private String employeeName; 	// 非db欄位 工號名字
	
	private String employeeWorkType;
	private Double totalPencent; 
	private String reserve1;		// 以利後續打原目標金額動態改變簽核目標金額
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
	/** default constructor */
	public BuGoalEmployeeLine() {
	}

	/** minimal constructor */
	public BuGoalEmployeeLine(Long lineId) {
		this.lineId = lineId;
	}

	/** full constructor */
	public BuGoalEmployeeLine(Long lineId, BuGoalHead buGoalHead,
			String employeeItemCategory, String employeeItemSubcategory,
			String employeeCode, String employeeName, String employeeWorkType,
			Double totalPencent, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5, Long indexNo,
			String isDeleteRecord, String isLockRecord, String message,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate) {
		super();
		this.lineId = lineId;
		this.buGoalHead = buGoalHead;
		this.employeeItemCategory = employeeItemCategory;
		this.employeeItemSubcategory = employeeItemSubcategory;
		this.employeeCode = employeeCode;
		this.employeeName = employeeName;
		this.employeeWorkType = employeeWorkType;
		this.totalPencent = totalPencent;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.indexNo = indexNo;
		this.isDeleteRecord = isDeleteRecord;
		this.isLockRecord = isLockRecord;
		this.message = message;
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

	public String getEmployeeCode() {
		return this.employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
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

	public String getIsDeleteRecord() {
		return this.isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public String getIsLockRecord() {
		return this.isLockRecord;
	}

	public void setIsLockRecord(String isLockRecord) {
		this.isLockRecord = isLockRecord;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public BuGoalHead getBuGoalHead() {
		return buGoalHead;
	}

	public void setBuGoalHead(BuGoalHead buGoalHead) {
		this.buGoalHead = buGoalHead;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeItemCategory() {
		return employeeItemCategory;
	}

	public void setEmployeeItemCategory(String employeeItemCategory) {
		this.employeeItemCategory = employeeItemCategory;
	}

	public String getEmployeeItemSubcategory() {
		return employeeItemSubcategory;
	}

	public void setEmployeeItemSubcategory(String employeeItemSubcategory) {
		this.employeeItemSubcategory = employeeItemSubcategory;
	}

	public String getEmployeeWorkType() {
		return employeeWorkType;
	}

	public void setEmployeeWorkType(String employeeWorkType) {
		this.employeeWorkType = employeeWorkType;
	}

	public void setTotalPencent(Double totalPencent) {
		this.totalPencent = totalPencent;
	}

	public Double getTotalPencent() {
		return totalPencent;
	}

	public String getEmployeeItemBrand() {
		return employeeItemBrand;
	}

	public void setEmployeeItemBrand(String employeeItemBrand) {
		this.employeeItemBrand = employeeItemBrand;
	}

	public String getEmployeeItemBrandName() {
		return employeeItemBrandName;
	}

	public void setEmployeeItemBrandName(String employeeItemBrandName) {
		this.employeeItemBrandName = employeeItemBrandName;
	}

}