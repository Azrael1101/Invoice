package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuPaymentTerm entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuPaymentTerm implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 2482367917131718118L;
	private BuPaymentTermId id;
	private String name;
	private String baseDateCode;
	private String billTypeCode;
	private Long paymentStartDate;
	private Long paymentDays;
	private String enable;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;

	// Constructors

	/** default constructor */
	public BuPaymentTerm() {
	}

	/** minimal constructor */
	public BuPaymentTerm(BuPaymentTermId id) {
		this.id = id;
	}

	/** full constructor */
	public BuPaymentTerm(BuPaymentTermId id, String name, String baseDateCode,
			String billTypeCode, Long paymentStartDate, Long paymentDays,
			String enable, String createdBy, Date creationDate,
			String lastUpdatedBy, Date lastUpdateDate) {
		this.id = id;
		this.name = name;
		this.baseDateCode = baseDateCode;
		this.billTypeCode = billTypeCode;
		this.paymentStartDate = paymentStartDate;
		this.paymentDays = paymentDays;
		this.enable = enable;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	// Property accessors

	public BuPaymentTermId getId() {
		return this.id;
	}

	public void setId(BuPaymentTermId id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBaseDateCode() {
		return this.baseDateCode;
	}

	public void setBaseDateCode(String baseDateCode) {
		this.baseDateCode = baseDateCode;
	}

	public String getBillTypeCode() {
		return this.billTypeCode;
	}

	public void setBillTypeCode(String billTypeCode) {
		this.billTypeCode = billTypeCode;
	}

	public Long getPaymentStartDate() {
		return this.paymentStartDate;
	}

	public void setPaymentStartDate(Long paymentStartDate) {
		this.paymentStartDate = paymentStartDate;
	}

	public Long getPaymentDays() {
		return this.paymentDays;
	}

	public void setPaymentDays(Long paymentDays) {
		this.paymentDays = paymentDays;
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