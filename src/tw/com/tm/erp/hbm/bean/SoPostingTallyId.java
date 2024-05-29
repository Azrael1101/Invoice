package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoPostingTallyId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoPostingTallyId implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3325398012106651249L;
    // Fields
    private String shopCode;
    private Date transactionDate;
    private String batch;                //班次

    // Constructors

    /** default constructor */
    public SoPostingTallyId() {
    }

    /** full constructor */
    public SoPostingTallyId(String shopCode, Date transactionDate) {
	this.shopCode = shopCode;
	this.transactionDate = transactionDate;
    }

    // Property accessors

    public String getShopCode() {
	return this.shopCode;
    }

    public void setShopCode(String shopCode) {
	this.shopCode = shopCode;
    }

    public Date getTransactionDate() {
	return this.transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
	this.transactionDate = transactionDate;
    }

    public boolean equals(Object other) {
	if ((this == other))
	    return true;
	if ((other == null))
	    return false;
	if (!(other instanceof SoPostingTallyId))
	    return false;
	SoPostingTallyId castOther = (SoPostingTallyId) other;

	return ((this.getShopCode() == castOther.getShopCode()) || (this
		.getShopCode() != null
		&& castOther.getShopCode() != null && this.getShopCode()
		.equals(castOther.getShopCode())))
		&& ((this.getTransactionDate() == castOther
			.getTransactionDate()) || (this.getTransactionDate() != null
			&& castOther.getTransactionDate() != null && this
			.getTransactionDate().equals(
				castOther.getTransactionDate())));
    }

    public int hashCode() {
	int result = 17;

	result = 37 * result
		+ (getShopCode() == null ? 0 : this.getShopCode().hashCode());
	result = 37
		* result
		+ (getTransactionDate() == null ? 0 : this.getTransactionDate()
			.hashCode());
	return result;
    }

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

}