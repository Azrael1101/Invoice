package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImCategoryLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImCategoryLine implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 751080145623990317L;

	private Long categoryLineId;

	private ImCategoryHead imCategoryHead;

	private String code;

	private String name;

	private String description;

	private String enable;

	private Long createdBy;

	private Date creationDate;

	private Long lastUpdatedBy;

	private Date lastUpdateDate;

	// Constructors

	/** default constructor */
	public ImCategoryLine() {
	}

	/** minimal constructor */
	public ImCategoryLine(Long categoryLineId) {
		this.categoryLineId = categoryLineId;
	}

	/** full constructor */
	public ImCategoryLine(Long categoryLineId, ImCategoryHead imCategoryHead,
			String code, String name, String description, String enable,
			Long createdBy, Date creationDate, Long lastUpdatedBy,
			Date lastUpdateDate) {
		this.categoryLineId = categoryLineId;
		this.imCategoryHead = imCategoryHead;
		this.code = code;
		this.name = name;
		this.description = description;
		this.enable = enable;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	// Property accessors

	public Long getCategoryLineId() {
		return this.categoryLineId;
	}

	public void setCategoryLineId(Long categoryLineId) {
		this.categoryLineId = categoryLineId;
	}

	public ImCategoryHead getImCategoryHead() {
		return this.imCategoryHead;
	}

	public void setImCategoryHead(ImCategoryHead imCategoryHead) {
		this.imCategoryHead = imCategoryHead;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public Long getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Long getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

}