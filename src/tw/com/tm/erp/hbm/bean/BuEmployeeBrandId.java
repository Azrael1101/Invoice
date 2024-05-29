package tw.com.tm.erp.hbm.bean;

/**
 * BuEmployeeBrandId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuEmployeeBrandId implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2869665370262682738L;
    // Fields
    private String employeeCode;
    private String brandCode;

    // Constructors

    /** default constructor */
    public BuEmployeeBrandId() {
    }

    /** full constructor */
    public BuEmployeeBrandId(String employeeCode, String brandCode) {
	this.employeeCode = employeeCode;
	this.brandCode = brandCode;
    }

    // Property accessors

    public String getEmployeeCode() {
	return this.employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
	this.employeeCode = employeeCode;
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
	if (!(other instanceof BuEmployeeBrandId))
	    return false;
	BuEmployeeBrandId castOther = (BuEmployeeBrandId) other;

	return ((this.getEmployeeCode() == castOther.getEmployeeCode()) || (this
		.getEmployeeCode() != null
		&& castOther.getEmployeeCode() != null && this
		.getEmployeeCode().equals(castOther.getEmployeeCode())))
		&& ((this.getBrandCode() == castOther.getBrandCode()) || (this
			.getBrandCode() != null
			&& castOther.getBrandCode() != null && this
			.getBrandCode().equals(castOther.getBrandCode())));
    }

    public int hashCode() {
	int result = 17;

	result = 37
		* result
		+ (getEmployeeCode() == null ? 0 : this.getEmployeeCode()
			.hashCode());
	result = 37 * result
		+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
	return result;
    }

}