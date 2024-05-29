package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoShopDailyHeadId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoShopDailyHeadId implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8349433683290485145L;
    // Fields
    private String shopCode;
    private Date salesDate;
    private String batch;

    // Constructors

    /** default constructor */
    public SoShopDailyHeadId() {
    }

    /** full constructor */
    public SoShopDailyHeadId(String shopCode, Date salesDate, String batch) {
	this.shopCode = shopCode;
	this.salesDate = salesDate;
	this.batch = batch; 
    }

    // Property accessors

    public String getShopCode() {
	return this.shopCode;
    }

    public void setShopCode(String shopCode) {
	this.shopCode = shopCode;
    }

    public Date getSalesDate() {
	return this.salesDate;
    }

    public void setSalesDate(Date salesDate) {
	this.salesDate = salesDate;
    }

    public boolean equals(Object other) {
	if ((this == other))
	    return true;
	if ((other == null))
	    return false;
	if (!(other instanceof SoShopDailyHeadId))
	    return false;
	SoShopDailyHeadId castOther = (SoShopDailyHeadId) other;

	return ((this.getShopCode() == castOther.getShopCode()) || (this
		.getShopCode() != null
		&& castOther.getShopCode() != null && this.getShopCode()
		.equals(castOther.getShopCode())))
		&& ((this.getSalesDate() == castOther.getSalesDate()) || (this
			.getSalesDate() != null
			&& castOther.getSalesDate() != null && this
			.getSalesDate().equals(castOther.getSalesDate())));
    }

    public int hashCode() {
	int result = 17;

	result = 37 * result
		+ (getShopCode() == null ? 0 : this.getShopCode().hashCode());
	result = 37 * result
		+ (getSalesDate() == null ? 0 : this.getSalesDate().hashCode());
	return result;
    }

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

}