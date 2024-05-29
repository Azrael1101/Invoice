package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * PoBudgetLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PoBudgetLine implements java.io.Serializable {

	// Fields

	private Long lineId;
	private PoBudgetHead poBudgetHead;
	private Long month;
	private String checkType;
	private String itemCode;
	private String categoryTypeCode1;
	private String categoryValue1;
	private String categoryTypeCode2;
	private String categoryValue2;
	private String categoryTypeCode3;
	private String categoryValue3;
	private String categoryTypeCode4;
	private String categoryValue4;
	private String categoryTypeCode5;
	private String categoryValue5;
	private Double budgetAmount;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long indexNo;

	// Constructors

	/** default constructor */
	public PoBudgetLine() {
	}

	/** minimal constructor */
	public PoBudgetLine(Long lineId) {
		this.lineId = lineId;
	}

	/** full constructor */
	public PoBudgetLine(Long lineId, PoBudgetHead poBudgetHead, Long month,
			String checkType, String itemCode, String categoryTypeCode1,
			String categoryValue1, String categoryTypeCode2,
			String categoryValue2, String categoryTypeCode3,
			String categoryValue3, String categoryTypeCode4,
			String categoryValue4, String categoryTypeCode5,
			String categoryValue5, Double budgetAmount, String reserve1,
			String reserve2, String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, Long indexNo) {
		this.lineId = lineId;
		this.poBudgetHead = poBudgetHead;
		this.month = month;
		this.checkType = checkType;
		this.itemCode = itemCode;
		this.categoryTypeCode1 = categoryTypeCode1;
		this.categoryValue1 = categoryValue1;
		this.categoryTypeCode2 = categoryTypeCode2;
		this.categoryValue2 = categoryValue2;
		this.categoryTypeCode3 = categoryTypeCode3;
		this.categoryValue3 = categoryValue3;
		this.categoryTypeCode4 = categoryTypeCode4;
		this.categoryValue4 = categoryValue4;
		this.categoryTypeCode5 = categoryTypeCode5;
		this.categoryValue5 = categoryValue5;
		this.budgetAmount = budgetAmount;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.indexNo = indexNo;
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public PoBudgetHead getPoBudgetHead() {
		return this.poBudgetHead;
	}

	public void setPoBudgetHead(PoBudgetHead poBudgetHead) {
		this.poBudgetHead = poBudgetHead;
	}

	public Long getMonth() {
		return this.month;
	}

	public void setMonth(Long month) {
		this.month = month;
	}

	public String getCheckType() {
		return this.checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getCategoryTypeCode1() {
		return this.categoryTypeCode1;
	}

	public void setCategoryTypeCode1(String categoryTypeCode1) {
		this.categoryTypeCode1 = categoryTypeCode1;
	}

	public String getCategoryValue1() {
		return this.categoryValue1;
	}

	public void setCategoryValue1(String categoryValue1) {
		this.categoryValue1 = categoryValue1;
	}

	public String getCategoryTypeCode2() {
		return this.categoryTypeCode2;
	}

	public void setCategoryTypeCode2(String categoryTypeCode2) {
		this.categoryTypeCode2 = categoryTypeCode2;
	}

	public String getCategoryValue2() {
		return this.categoryValue2;
	}

	public void setCategoryValue2(String categoryValue2) {
		this.categoryValue2 = categoryValue2;
	}

	public String getCategoryTypeCode3() {
		return this.categoryTypeCode3;
	}

	public void setCategoryTypeCode3(String categoryTypeCode3) {
		this.categoryTypeCode3 = categoryTypeCode3;
	}

	public String getCategoryValue3() {
		return this.categoryValue3;
	}

	public void setCategoryValue3(String categoryValue3) {
		this.categoryValue3 = categoryValue3;
	}

	public String getCategoryTypeCode4() {
		return this.categoryTypeCode4;
	}

	public void setCategoryTypeCode4(String categoryTypeCode4) {
		this.categoryTypeCode4 = categoryTypeCode4;
	}

	public String getCategoryValue4() {
		return this.categoryValue4;
	}

	public void setCategoryValue4(String categoryValue4) {
		this.categoryValue4 = categoryValue4;
	}

	public String getCategoryTypeCode5() {
		return this.categoryTypeCode5;
	}

	public void setCategoryTypeCode5(String categoryTypeCode5) {
		this.categoryTypeCode5 = categoryTypeCode5;
	}

	public String getCategoryValue5() {
		return this.categoryValue5;
	}

	public void setCategoryValue5(String categoryValue5) {
		this.categoryValue5 = categoryValue5;
	}

	public Double getBudgetAmount() {
		return this.budgetAmount;
	}

	public void setBudgetAmount(Double budgetAmount) {
		this.budgetAmount = budgetAmount;
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

	public Long getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

}