package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoShopDailyCompetitorId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoShopDailyCompetitorId implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4698764581226900064L;
    // Fields
    private String shopCode;
    private Date salesDate;
    private String competitorName;

    // Constructors

    /** default constructor */
    public SoShopDailyCompetitorId() {
    }

    /** full constructor */
    public SoShopDailyCompetitorId(String shopCode, Date salesDate,
	    String competitorName) {
	this.shopCode = shopCode;
	this.salesDate = salesDate;
	this.competitorName = competitorName;
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

    public String getCompetitorName() {
	return this.competitorName;
    }

    public void setCompetitorName(String competitorName) {
	this.competitorName = competitorName;
    }

    public boolean equals(Object other) {
	if ((this == other))
	    return true;
	if ((other == null))
	    return false;
	if (!(other instanceof SoShopDailyCompetitorId))
	    return false;
	SoShopDailyCompetitorId castOther = (SoShopDailyCompetitorId) other;

	return ((this.getShopCode() == castOther.getShopCode()) || (this
		.getShopCode() != null
		&& castOther.getShopCode() != null && this.getShopCode()
		.equals(castOther.getShopCode())))
		&& ((this.getSalesDate() == castOther.getSalesDate()) || (this
			.getSalesDate() != null
			&& castOther.getSalesDate() != null && this
			.getSalesDate().equals(castOther.getSalesDate())))
		&& ((this.getCompetitorName() == castOther.getCompetitorName()) || (this
			.getCompetitorName() != null
			&& castOther.getCompetitorName() != null && this
			.getCompetitorName().equals(
				castOther.getCompetitorName())));
    }

    public int hashCode() {
	int result = 17;

	result = 37 * result
		+ (getShopCode() == null ? 0 : this.getShopCode().hashCode());
	result = 37 * result
		+ (getSalesDate() == null ? 0 : this.getSalesDate().hashCode());
	result = 37
		* result
		+ (getCompetitorName() == null ? 0 : this.getCompetitorName()
			.hashCode());
	return result;
    }

}