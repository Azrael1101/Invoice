package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemCategory entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemCategory implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 2503651079022273172L;
	private ImItemCategoryId id;
	private String categoryName;
	private Long categoryLevel;
	private String billType;
	private String salesUnit;
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
	private String lastPromotionCode;
	
	private String enableName;	// 非db欄位
	private String categoryTypeName;	// 非db欄位
	
	
	// Constructors

	/** default constructor */
	public ImItemCategory() {
	}

	/** minimal constructor */
	public ImItemCategory(ImItemCategoryId id) {
		this.id = id;
	}

	/** full constructor */
	public ImItemCategory(ImItemCategoryId id, String categoryName,
			Long categoryLevel, String billType, String salesUnit,
			String parentCategoryCode, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5, String enable,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate) {
		this.id = id;
		this.categoryName = categoryName;
		this.categoryLevel = categoryLevel;
		this.billType = billType;
		this.salesUnit = salesUnit;
		this.parentCategoryCode = parentCategoryCode;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.enable = enable;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	// Property accessors

	public ImItemCategoryId getId() {
		return this.id;
	}

	public void setId(ImItemCategoryId id) {
		this.id = id;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Long getCategoryLevel() {
		return this.categoryLevel;
	}

	public void setCategoryLevel(Long categoryLevel) {
		this.categoryLevel = categoryLevel;
	}

	public String getBillType() {
		return this.billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getSalesUnit() {
		return this.salesUnit;
	}

	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
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
	    return enable;
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

	public String getEnableName() {
		return enableName;
	}

	public void setEnableName(String enableName) {
		this.enableName = enableName;
	}

	public String getCategoryTypeName() {
		return categoryTypeName;
	}

	public void setCategoryTypeName(String categoryTypeName) {
		this.categoryTypeName = categoryTypeName;
	}

	public String getLastPromotionCode() {
	    return lastPromotionCode;
	}

	public void setLastPromotionCode(String lastPromotionCode) {
	    this.lastPromotionCode = lastPromotionCode;
	}
}