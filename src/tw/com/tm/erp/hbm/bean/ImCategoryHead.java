package tw.com.tm.erp.hbm.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ImCategoryHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImCategoryHead implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -6403173107326159906L;

	private Long categoryHeadId;

	private Long brandId;

	private String code;

	private String name;

	private String description;

	private String enable;

	private Long createdBy;

	private Date creationDate;

	private Long lastUpdatedBy;

	private Date lastUpdateDate;

	private Set imCategoryLines = new HashSet(0);

	// Constructors

	/** default constructor */
	public ImCategoryHead() {
	}

	/** minimal constructor */
	public ImCategoryHead(Long categoryHeadId) {
		this.categoryHeadId = categoryHeadId;
	}

	/** full constructor */
	public ImCategoryHead(Long categoryHeadId, Long brandId, String code,
			String name, String description, String enable, Long createdBy,
			Date creationDate, Long lastUpdatedBy, Date lastUpdateDate,
			Set imCategoryLines) {
		this.categoryHeadId = categoryHeadId;
		this.brandId = brandId;
		this.code = code;
		this.name = name;
		this.description = description;
		this.enable = enable;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.imCategoryLines = imCategoryLines;
	}

	// Property accessors

	public Long getCategoryHeadId() {
		return this.categoryHeadId;
	}

	public void setCategoryHeadId(Long categoryHeadId) {
		this.categoryHeadId = categoryHeadId;
	}

	public Long getBrandId() {
		return this.brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
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

	public Set getImCategoryLines() {
		return this.imCategoryLines;
	}

	public void setImCategoryLines(Set imCategoryLines) {
		this.imCategoryLines = imCategoryLines;
	}

}