package tw.com.tm.erp.hbm.bean;

/**
 * SiUsersGroupId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiUsersGroupId implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1807311742199199959L;
	private String brandCode;
	private String employeeCode;
	private String groupCode;

	// Constructors

	/** default constructor */
	public SiUsersGroupId() {
	}

	/** full constructor */
	public SiUsersGroupId(String brandCode, String employeeCode,
			String groupCode) {
		this.brandCode = brandCode;
		this.employeeCode = employeeCode;
		this.groupCode = groupCode;
	}

	// Property accessors

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getEmployeeCode() {
		return this.employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getGroupCode() {
		return this.groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SiUsersGroupId))
			return false;
		SiUsersGroupId castOther = (SiUsersGroupId) other;

		return ((this.getBrandCode() == castOther.getBrandCode()) || (this
				.getBrandCode() != null
				&& castOther.getBrandCode() != null && this.getBrandCode()
				.equals(castOther.getBrandCode())))
				&& ((this.getEmployeeCode() == castOther.getEmployeeCode()) || (this
						.getEmployeeCode() != null
						&& castOther.getEmployeeCode() != null && this
						.getEmployeeCode().equals(castOther.getEmployeeCode())))
				&& ((this.getGroupCode() == castOther.getGroupCode()) || (this
						.getGroupCode() != null
						&& castOther.getGroupCode() != null && this
						.getGroupCode().equals(castOther.getGroupCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
		result = 37
				* result
				+ (getEmployeeCode() == null ? 0 : this.getEmployeeCode()
						.hashCode());
		result = 37 * result
				+ (getGroupCode() == null ? 0 : this.getGroupCode().hashCode());
		return result;
	}

}