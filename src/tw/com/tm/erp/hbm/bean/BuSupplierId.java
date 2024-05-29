package tw.com.tm.erp.hbm.bean;

/**
 * BuSupplierId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuSupplierId implements java.io.Serializable {

    // Fields

    private String supplierCode;
    private String brandCode;

    // Constructors

    /** default constructor */
    public BuSupplierId() {
    }

    /** full constructor */
    public BuSupplierId(String supplierCode, String brandCode) {
	this.supplierCode = supplierCode;
	this.brandCode = brandCode;
    }

    // Property accessors

    public String getSupplierCode() {
	return this.supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
	this.supplierCode = supplierCode;
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
	if (!(other instanceof BuSupplierId))
	    return false;
	BuSupplierId castOther = (BuSupplierId) other;

	return ((this.getSupplierCode() == castOther.getSupplierCode()) || (this
		.getSupplierCode() != null
		&& castOther.getSupplierCode() != null && this
		.getSupplierCode().equals(castOther.getSupplierCode())))
		&& ((this.getBrandCode() == castOther.getBrandCode()) || (this
			.getBrandCode() != null
			&& castOther.getBrandCode() != null && this
			.getBrandCode().equals(castOther.getBrandCode())));
    }

    public int hashCode() {
	int result = 17;

	result = 37
		* result
		+ (getSupplierCode() == null ? 0 : this.getSupplierCode()
			.hashCode());
	result = 37 * result
		+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
	return result;
    }

}