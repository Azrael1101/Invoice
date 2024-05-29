package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuExchangeRateId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuExchangeRateId implements java.io.Serializable {

    // Fields

    private String organizationCode;
    private String sourceCurrency;
    private String againstCurrency;
    private Date beginDate;

    // Constructors

    /** default constructor */
    public BuExchangeRateId() {
    }

    /** full constructor */
    public BuExchangeRateId(String organizationCode, String sourceCurrency,
	    String againstCurrency, Date beginDate) {
	this.organizationCode = organizationCode;
	this.sourceCurrency = sourceCurrency;
	this.againstCurrency = againstCurrency;
	this.beginDate = beginDate;
    }

    // Property accessors

    public String getOrganizationCode() {
	return this.organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
	this.organizationCode = organizationCode;
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

    public Date getBeginDate() {
	return this.beginDate;
    }

    public void setBeginDate(Date beginDate) {
	this.beginDate = beginDate;
    }

    public boolean equals(Object other) {
	if ((this == other))
	    return true;
	if ((other == null))
	    return false;
	if (!(other instanceof BuExchangeRateId))
	    return false;
	BuExchangeRateId castOther = (BuExchangeRateId) other;

	return ((this.getOrganizationCode() == castOther.getOrganizationCode()) || (this
		.getOrganizationCode() != null
		&& castOther.getOrganizationCode() != null && this
		.getOrganizationCode().equals(castOther.getOrganizationCode())))
		&& ((this.getSourceCurrency() == castOther.getSourceCurrency()) || (this
			.getSourceCurrency() != null
			&& castOther.getSourceCurrency() != null && this
			.getSourceCurrency().equals(
				castOther.getSourceCurrency())))
		&& ((this.getAgainstCurrency() == castOther
			.getAgainstCurrency()) || (this.getAgainstCurrency() != null
			&& castOther.getAgainstCurrency() != null && this
			.getAgainstCurrency().equals(
				castOther.getAgainstCurrency())))
		&& ((this.getBeginDate() == castOther.getBeginDate()) || (this
			.getBeginDate() != null
			&& castOther.getBeginDate() != null && this
			.getBeginDate().equals(castOther.getBeginDate())));
    }

    public int hashCode() {
	int result = 17;

	result = 37
		* result
		+ (getOrganizationCode() == null ? 0 : this
			.getOrganizationCode().hashCode());
	result = 37
		* result
		+ (getSourceCurrency() == null ? 0 : this.getSourceCurrency()
			.hashCode());
	result = 37
		* result
		+ (getAgainstCurrency() == null ? 0 : this.getAgainstCurrency()
			.hashCode());
	result = 37 * result
		+ (getBeginDate() == null ? 0 : this.getBeginDate().hashCode());
	return result;
    }

}