package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuCountry entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuCountry implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 4041271888661146620L;

	private String countryCode;

	private String countryCName;

	private String countryEName;

	private String description;

	private String enable;

	private String createdBy;

	private Date creationDate;

	private String lastUpdatedBy;
	
	private String lastUpdatedByName;	// 暫時欄位 更新人員
	
	private Date lastUpdateDate;
	
	private String oldCountryCode;

	// Constructors

	/** default constructor */
	public BuCountry() {
	}

	/** minimal constructor */
	public BuCountry(String countryCode) {
		this.countryCode = countryCode;
	}

	/** full constructor */
	public BuCountry(String countryCode, String countryCName,
			String countryEName, String description, String enable,
			String createdBy, Date creationDate, String lastUpdatedBy,
			String lastUpdatedByName, Date lastUpdateDate, String oldCountryCode) {
		super();
		this.countryCode = countryCode;
		this.countryCName = countryCName;
		this.countryEName = countryEName;
		this.description = description;
		this.enable = enable;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedByName = lastUpdatedByName;
		this.lastUpdateDate = lastUpdateDate;
		this.oldCountryCode = oldCountryCode;
	}

	// Property accessors

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryCName() {
		return this.countryCName;
	}

	public void setCountryCName(String countryCName) {
		this.countryCName = countryCName;
	}

	public String getCountryEName() {
		return this.countryEName;
	}

	public void setCountryEName(String countryEName) {
		this.countryEName = countryEName;
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

	public String getOldCountryCode() {
	    return oldCountryCode;
	}

	public void setOldCountryCode(String oldCountryCode) {
	    this.oldCountryCode = oldCountryCode;
	}

}