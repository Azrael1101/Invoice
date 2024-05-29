package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * FiExchangeRate entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FiExchangeRate implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 4608359966258744055L;

	private Long exchangeRateId;

	private Long branchId;

	private String sourceCurrency;

	private String againstCurrency;

	private Float exchangeRate;

	private Date beginDate;

	private Date endDate;

	private Long createdBy;

	private Date creationDate;

	private Long lastUpdatedBy;

	private Date lastUpdateDate;

	// Constructors

	/** default constructor */
	public FiExchangeRate() {
	}

	/** minimal constructor */
	public FiExchangeRate(Long exchangeRateId) {
		this.exchangeRateId = exchangeRateId;
	}

	/** full constructor */
	public FiExchangeRate(Long exchangeRateId, Long branchId,
			String sourceCurrency, String againstCurrency, Float exchangeRate,
			Date beginDate, Date endDate, Long createdBy, Date creationDate,
			Long lastUpdatedBy, Date lastUpdateDate) {
		this.exchangeRateId = exchangeRateId;
		this.branchId = branchId;
		this.sourceCurrency = sourceCurrency;
		this.againstCurrency = againstCurrency;
		this.exchangeRate = exchangeRate;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	// Property accessors

	public Long getExchangeRateId() {
		return this.exchangeRateId;
	}

	public void setExchangeRateId(Long exchangeRateId) {
		this.exchangeRateId = exchangeRateId;
	}

	public Long getBranchId() {
		return this.branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getSourceCurrency() {
		return this.sourceCurrency;
	}

	public void setSourceCurrency(String sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}

	public String getAgainstCurrency() {
		return this.againstCurrency;
	}

	public void setAgainstCurrency(String againstCurrency) {
		this.againstCurrency = againstCurrency;
	}

	public Float getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(Float exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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