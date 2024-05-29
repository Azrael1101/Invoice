package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuShopTarget entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuShopTarget implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -294158371916826465L;
	private Long lineId;
	private BuShop buShop;
	private String year;
	private String month;
	private Long salesTarget;
	private Long accountTarget;
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
	private String computeFormula;
	private String areaGroup;
	private String targetAttribute;
	private String groupCode;
	private String employeeBenefit;
	private Long manpower;

	// Constructors

	/** default constructor */
	public BuShopTarget() {
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public BuShop getBuShop() {
		return this.buShop;
	}

	public void setBuShop(BuShop buShop) {
		this.buShop = buShop;
	}

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Long getSalesTarget() {
		return salesTarget;
	}

	public void setSalesTarget(Long salesTarget) {
		this.salesTarget = salesTarget;
	}

	public Long getAccountTarget() {
		return accountTarget;
	}

	public void setAccountTarget(Long accountTarget) {
		this.accountTarget = accountTarget;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getComputeFormula() {
		return computeFormula;
	}

	public void setComputeFormula(String computeFormula) {
		this.computeFormula = computeFormula;
	}

	public String getAreaGroup() {
		return areaGroup;
	}

	public void setAreaGroup(String areaGroup) {
		this.areaGroup = areaGroup;
	}

	public String getTargetAttribute() {
		return targetAttribute;
	}

	public void setTargetAttribute(String targetAttribute) {
		this.targetAttribute = targetAttribute;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getEmployeeBenefit() {
		return employeeBenefit;
	}

	public void setEmployeeBenefit(String employeeBenefit) {
		this.employeeBenefit = employeeBenefit;
	}

	public Long getManpower() {
		return manpower;
	}

	public void setManpower(Long manpower) {
		this.manpower = manpower;
	}

}