package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * FiBank entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FiBank implements java.io.Serializable {

	private static final long serialVersionUID = 3815466245352684501L;

	// Fields

	private Long bankId;

	private Long brandId;

	private Long sourceId;

	private String bankCode;

	private String bankName;

	private String accountCode;

	private String accountName;

	private String defaultBank;

	private String enable;

	private Double createdBy;

	private Date creationDate;

	private Long lastUpdatedBy;

	private Date lastUpdateDate;

	// Constructors

	/** default constructor */
	public FiBank() {
	}

	/** minimal constructor */
	public FiBank(Long bankId) {
		this.bankId = bankId;
	}

	/** full constructor */
	public FiBank(Long bankId, Long brandId, Long sourceId, String bankCode,
			String bankName, String accountCode, String accountName,
			String defaultBank, String enable, Double createdBy,
			Date creationDate, Long lastUpdatedBy, Date lastUpdateDate) {
		this.bankId = bankId;
		this.brandId = brandId;
		this.sourceId = sourceId;
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.accountCode = accountCode;
		this.accountName = accountName;
		this.defaultBank = defaultBank;
		this.enable = enable;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	// Property accessors

	public Long getBankId() {
		return this.bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public Long getBrandId() {
		return this.brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Long getSourceId() {
		return this.sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountCode() {
		return this.accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDefaultBank() {
		return this.defaultBank;
	}

	public void setDefaultBank(String defaultBank) {
		this.defaultBank = defaultBank;
	}

	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public Double getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Double createdBy) {
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