package tw.com.tm.erp.hbm.bean;

/**
 * BuOrderTypeId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuOrderTypeId implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5165033898737886772L;
    
    // Fields
    private String brandCode;
    private String orderTypeCode;

    // Constructors

    /** default constructor */
    public BuOrderTypeId() {
    }

    /** full constructor */
    public BuOrderTypeId(String brandCode, String orderTypeCode) {
	this.brandCode = brandCode;
	this.orderTypeCode = orderTypeCode;
    }

    // Property accessors

    public String getBrandCode() {
	return this.brandCode;
    }

    public void setBrandCode(String brandCode) {
	this.brandCode = brandCode;
    }

    public String getOrderTypeCode() {
	return this.orderTypeCode;
    }

    public void setOrderTypeCode(String orderTypeCode) {
	this.orderTypeCode = orderTypeCode;
    }

    public boolean equals(Object other) {
	if ((this == other))
	    return true;
	if ((other == null))
	    return false;
	if (!(other instanceof BuOrderTypeId))
	    return false;
	BuOrderTypeId castOther = (BuOrderTypeId) other;

	return ((this.getBrandCode() == castOther.getBrandCode()) || (this
		.getBrandCode() != null
		&& castOther.getBrandCode() != null && this.getBrandCode()
		.equals(castOther.getBrandCode())))
		&& ((this.getOrderTypeCode() == castOther.getOrderTypeCode()) || (this
			.getOrderTypeCode() != null
			&& castOther.getOrderTypeCode() != null && this
			.getOrderTypeCode()
			.equals(castOther.getOrderTypeCode())));
    }

    public int hashCode() {
	int result = 17;

	result = 37 * result
		+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
	result = 37
		* result
		+ (getOrderTypeCode() == null ? 0 : this.getOrderTypeCode()
			.hashCode());
	return result;
    }

}