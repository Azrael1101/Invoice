package tw.com.tm.erp.hbm.bean;

/**
 * ImWarehouseEmployeeId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImWarehouseEmployeeId implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 2448058728570173673L;
	private String warehouseCode;
	private String employeeCode;
	private String enable;
	// Constructors

	/** default constructor */
	public ImWarehouseEmployeeId() {
	}

	/** full constructor */
	public ImWarehouseEmployeeId(String warehouseCode, String employeeCode) {
		this.warehouseCode = warehouseCode;
		this.employeeCode = employeeCode;
	}

	// Property accessors

	public String getWarehouseCode() {
		return this.warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
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
		if (!(other instanceof ImWarehouseEmployeeId))
			return false;
		ImWarehouseEmployeeId castOther = (ImWarehouseEmployeeId) other;

		return ((this.getWarehouseCode() == castOther.getWarehouseCode()) || (this
				.getWarehouseCode() != null
				&& castOther.getWarehouseCode() != null && this
				.getWarehouseCode().equals(castOther.getWarehouseCode())))
				&& ((this.getEmployeeCode() == castOther.getEmployeeCode()) || (this
						.getEmployeeCode() != null
						&& castOther.getEmployeeCode() != null && this
						.getEmployeeCode().equals(castOther.getEmployeeCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getWarehouseCode() == null ? 0 : this.getWarehouseCode()
						.hashCode());
		result = 37
				* result
				+ (getEmployeeCode() == null ? 0 : this.getEmployeeCode()
						.hashCode());
		return result;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

}