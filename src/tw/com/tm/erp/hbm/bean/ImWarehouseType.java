package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImWarehouseType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImWarehouseType implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7759409217394176736L;
	private Long warehouseTypeId;
	private String typeCode;
	private Long typeLevel;
	private String name;
	private String enable;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;

	// Constructors

	/** default constructor */
	public ImWarehouseType() {
	}

	/** minimal constructor */
	public ImWarehouseType(Long warehouseTypeId) {
		this.warehouseTypeId = warehouseTypeId;
	}

	/** full constructor */
	public ImWarehouseType(Long warehouseTypeId, String typeCode,
			Long typeLevel, String name, String enable, String createdBy,
			Date creationDate, String lastUpdatedBy, Date lastUpdateDate) {
		this.warehouseTypeId = warehouseTypeId;
		this.typeCode = typeCode;
		this.typeLevel = typeLevel;
		this.name = name;
		this.enable = enable;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	// Property accessors

	public Long getWarehouseTypeId() {
		return this.warehouseTypeId;
	}

	public void setWarehouseTypeId(Long warehouseTypeId) {
		this.warehouseTypeId = warehouseTypeId;
	}

	public String getTypeCode() {
		return this.typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Long getTypeLevel() {
		return this.typeLevel;
	}

	public void setTypeLevel(Long typeLevel) {
		this.typeLevel = typeLevel;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

}