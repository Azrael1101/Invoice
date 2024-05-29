package tw.com.tm.erp.hbm.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * BuBranch entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuBranch implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -1400507274577184809L;
	private String branchCode;
	private String organizationCode;
	private BuOrganization buOrganization;
	private String branchName;
	private String description;
	private String enable;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Set buBrands = new HashSet(0);

	// Constructors

	/** default constructor */
	public BuBranch() {
	}

	/** minimal constructor */
	public BuBranch(String branchCode, String organizationCode) {
		this.branchCode = branchCode;
		this.organizationCode = organizationCode;
	}

	/** full constructor */
	public BuBranch(String branchCode, String organizationCode,
			String branchName, String description, String enable,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, Set buBrands) {
		this.branchCode = branchCode;
		this.organizationCode = organizationCode;
		this.branchName = branchName;
		this.description = description;
		this.enable = enable;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.buBrands = buBrands;
	}

	// Property accessors

	public String getBranchCode() {
		return this.branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	
	public BuOrganization getBuOrganization() {
		return this.buOrganization;
	}

	public void setBuOrganization(BuOrganization buOrganization) {
		this.buOrganization = buOrganization;
	}

	public String getOrganizationCode() {
		return this.organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
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
	
	public Set getBuBrands() {
		return this.buBrands;
	}

	public void setBuBrands(Set buBrands) {
		this.buBrands = buBrands;
	}
}