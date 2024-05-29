package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuLocation entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuLocation implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -8337223365585791327L;
	private Long locationId;
	private String locationName;
	private String city;
	private String area;
	private String zipCode;
	private String address;
	private String enable;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private String lastUpdatedByName;	// 暫時欄位 更新人員
	private Date lastUpdateDate;

	// Constructors

	/** default constructor */
	public BuLocation() {
	}

	/** minimal constructor */
	public BuLocation(Long locationId) {
		this.locationId = locationId;
	}

	/** full constructor */
	public BuLocation(Long locationId, String locationName, String city,
			String area, String zipCode, String address, String enable,
			String createdBy, Date creationDate, String lastUpdatedBy,
			String lastUpdatedByName, Date lastUpdateDate) {
		super();
		this.locationId = locationId;
		this.locationName = locationName;
		this.city = city;
		this.area = area;
		this.zipCode = zipCode;
		this.address = address;
		this.enable = enable;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedByName = lastUpdatedByName;
		this.lastUpdateDate = lastUpdateDate;
	}

	// Property accessors

	public Long getLocationId() {
		return this.locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return this.locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getLastUpdatedByName() {
		return lastUpdatedByName;
	}

	public void setLastUpdatedByName(String lastUpdatedByName) {
		this.lastUpdatedByName = lastUpdatedByName;
	}

}