package tw.com.tm.erp.hbm.bean;

/**
 * BuCustomerId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuCustomerId implements java.io.Serializable {

    // Fields

    private String customerCode;
    private String brandCode;

    // Constructors

    /** default constructor */
    public BuCustomerId() {
    }

    /** full constructor */
    public BuCustomerId(String customerCode, String brandCode) {
	this.customerCode = customerCode;
	this.brandCode = brandCode;
    }

    // Property accessors

    public String getCustomerCode() {
	return this.customerCode;
    }

    public void setCustomerCode(String customerCode) {
	this.customerCode = customerCode;
    }

    public String getBrandCode() {
	return this.brandCode;
    }

    public void setBrandCode(String brandCode) {
	this.brandCode = brandCode;
    }

    public boolean equals(Object other) {
	if ((this == other))
	    return true;
	if ((other == null))
	    return false;
	if (!(other instanceof BuCustomerId))
	    return false;
	BuCustomerId castOther = (BuCustomerId) other;

	return ((this.getCustomerCode() == castOther.getCustomerCode()) || (this
		.getCustomerCode() != null
		&& castOther.getCustomerCode() != null && this
		.getCustomerCode().equals(castOther.getCustomerCode())))
		&& ((this.getBrandCode() == castOther.getBrandCode()) || (this
			.getBrandCode() != null
			&& castOther.getBrandCode() != null && this
			.getBrandCode().equals(castOther.getBrandCode())));
    }

    public int hashCode() {
	int result = 17;

	result = 37
		* result
		+ (getCustomerCode() == null ? 0 : this.getCustomerCode()
			.hashCode());
	result = 37 * result
		+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
	return result;
    }

}