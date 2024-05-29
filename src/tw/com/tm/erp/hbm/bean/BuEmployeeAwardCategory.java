package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuEmployeeAwardCategory entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuEmployeeAwardCategory implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4603652218605914961L;
//	private BuEmployeeAwardCategoryId id;
	private String brandCode;
	private String categoryType;
	private String categoryCode;
	private String categoryName;
	private String categoryLevel;
	private String parentCategoryCode;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String enable;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long point;

	// Constructors

	/** default constructor */
	public BuEmployeeAwardCategory() {
	}

	/** minimal constructor */

	/** full constructor */

	// Property accessors

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryLevel() {
		return this.categoryLevel;
	}

	public void setCategoryLevel(String categoryLevel) {
		this.categoryLevel = categoryLevel;
	}

	public String getParentCategoryCode() {
		return this.parentCategoryCode;
	}

	public void setParentCategoryCode(String parentCategoryCode) {
		this.parentCategoryCode = parentCategoryCode;
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

	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
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

	public Long getPoint() {
		return this.point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public String getBrandCode() {
	    return brandCode;
	}

	public void setBrandCode(String brandCode) {
	    this.brandCode = brandCode;
	}

	public String getCategoryType() {
	    return categoryType;
	}

	public void setCategoryType(String categoryType) {
	    this.categoryType = categoryType;
	}

	public String getCategoryCode() {
	    return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
	    this.categoryCode = categoryCode;
	}

}