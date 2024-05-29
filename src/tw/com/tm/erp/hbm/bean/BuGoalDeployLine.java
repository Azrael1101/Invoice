package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuGoalDeployLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuGoalDeployLine implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8042877950138442745L;
	private Long lineId;
	private BuGoalDeployHead buGoalDeployHead;
	private String itemCategory;
	private String itemSubcategory;
	private String itemBrand;
	private String employeeCode;
	private String workType;
	private Double goalAmount;
	private Double signingAmount;
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
	/** default constructor */
	public BuGoalDeployLine() {
	}

	/** minimal constructor */
	public BuGoalDeployLine(Long lineId) {
		this.lineId = lineId;
	}

	/** full constructor */
	public BuGoalDeployLine(Long lineId, BuGoalDeployHead buGoalDeployHead,
			String itemCategory, String itemSubcategory, String employeeCode,
			String workType, Double goalAmount, Double signingAmount,
			String reserve1, String reserve2, String reserve3, String reserve4,
			String reserve5, Long indexNo, String isDeleteRecord,
			String isLockRecord, String message, String createdBy,
			Date creationDate, String lastUpdatedBy, Date lastUpdateDate) {
		super();
		this.lineId = lineId;
		this.buGoalDeployHead = buGoalDeployHead;
		this.itemCategory = itemCategory;
		this.itemSubcategory = itemSubcategory;
		this.employeeCode = employeeCode;
		this.workType = workType;
		this.goalAmount = goalAmount;
		this.signingAmount = signingAmount;
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

	public String getItemCategory() {
		return this.itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemSubcategory() {
		return this.itemSubcategory;
	}

	public void setItemSubcategory(String itemSubcategory) {
		this.itemSubcategory = itemSubcategory;
	}

	public String getEmployeeCode() {
		return this.employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getWorkType() {
		return this.workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public Double getGoalAmount() {
		return this.goalAmount;
	}

	public void setGoalAmount(Double goalAmount) {
		this.goalAmount = goalAmount;
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

	public BuGoalDeployHead getBuGoalDeployHead() {
		return buGoalDeployHead;
	}

	public void setBuGoalDeployHead(BuGoalDeployHead buGoalDeployHead) {
		this.buGoalDeployHead = buGoalDeployHead;
	}

	public Double getSigningAmount() {
		return signingAmount;
	}

	public void setSigningAmount(Double signingAmount) {
		this.signingAmount = signingAmount;
	}

	public String getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}

}