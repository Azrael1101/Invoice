package tw.com.tm.erp.hbm.bean;

/**
 * BuShopEmployeeId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuShopEmployeeId implements java.io.Serializable {

    // Fields

    /**
	 * 
	 */
	private static final long serialVersionUID = 1112507707334816306L;
	private String shopCode;
    private String employeeCode;

    // Constructors

    /** default constructor */
    public BuShopEmployeeId() {
    }

    /** full constructor */
    public BuShopEmployeeId(String shopCode, String employeeCode) {
	this.shopCode = shopCode;
	this.employeeCode = employeeCode;
    }

    // Property accessors

    public String getShopCode() {
	return this.shopCode;
    }

    public void setShopCode(String shopCode) {
	this.shopCode = shopCode;
    }

    public String getEmployeeCode() {
	return this.employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
	this.employeeCode = employeeCode;
    }

    public boolean equals(Object other) {
	if ((this == other))
	    return true;
	if ((other == null))
	    return false;
	if (!(other instanceof BuShopEmployeeId))
	    return false;
	BuShopEmployeeId castOther = (BuShopEmployeeId) other;

	return ((this.getShopCode() == castOther.getShopCode()) || (this
		.getShopCode() != null
		&& castOther.getShopCode() != null && this.getShopCode()
		.equals(castOther.getShopCode())))
		&& ((this.getEmployeeCode() == castOther.getEmployeeCode()) || (this
			.getEmployeeCode() != null
			&& castOther.getEmployeeCode() != null && this
			.getEmployeeCode().equals(castOther.getEmployeeCode())));
    }

    public int hashCode() {
	int result = 17;

	result = 37 * result
		+ (getShopCode() == null ? 0 : this.getShopCode().hashCode());
	result = 37
		* result
		+ (getEmployeeCode() == null ? 0 : this.getEmployeeCode()
			.hashCode());
	return result;
    }

}