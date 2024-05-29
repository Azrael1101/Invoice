package tw.com.tm.erp.hbm.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * BuOrganization entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuOrganization implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 7308297612877249187L;
	private String organizationCode;
	private String organizationCName;
	private String organizationEName;
	private String enable;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Set<BuBranch> buBranchs = new HashSet(0);
	
	// Constructors

	/** default constructor */
	public BuOrganization() {
	}

	/** minimal constructor */
	public BuOrganization(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	/** full constructor */
	public BuOrganization(String organizationCode,
			String organizationCName, String organizationEName, String enable,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, Set buBranchs) {
		this.organizationCode = organizationCode;
		this.organizationCName = organizationCName;
		this.organizationEName = organizationEName;
		this.enable = enable;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.buBranchs = buBranchs;
	}

	// Property accessors

	public String getOrganizationCode() {
		return this.organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getOrganizationCName() {
		return this.organizationCName;
	}

	public void setOrganizationCName(String organizationCName) {
		this.organizationCName = organizationCName;
	}

	public String getOrganizationEName() {
		return this.organizationEName;
	}

	public void setOrganizationEName(String organizationEName) {
		this.organizationEName = organizationEName;
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

	public Set<BuBranch> getBuBranchs() {
		return this.buBranchs;
	}

	public void setBuBranchs(Set<BuBranch> buBranchs) {
		this.buBranchs = buBranchs;
	}
}