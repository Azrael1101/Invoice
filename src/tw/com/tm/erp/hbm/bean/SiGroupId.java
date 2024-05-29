package tw.com.tm.erp.hbm.bean;

/**
 * SiGroupId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiGroupId implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 3697564177565910622L;
	private String brandCode;
	private String groupCode;

	// Constructors

	/** default constructor */
	public SiGroupId() {
	}

	/** full constructor */
	public SiGroupId(String brandCode, String groupCode) {
		this.brandCode = brandCode;
		this.groupCode = groupCode;
	}

	// Property accessors

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
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
		if (!(other instanceof SiGroupId))
			return false;
		SiGroupId castOther = (SiGroupId) other;

		return ((this.getBrandCode() == castOther.getBrandCode()) || (this
				.getBrandCode() != null
				&& castOther.getBrandCode() != null && this.getBrandCode()
				.equals(castOther.getBrandCode())))
				&& ((this.getGroupCode() == castOther.getGroupCode()) || (this
						.getGroupCode() != null
						&& castOther.getGroupCode() != null && this
						.getGroupCode().equals(castOther.getGroupCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
		result = 37 * result
				+ (getGroupCode() == null ? 0 : this.getGroupCode().hashCode());
		return result;
	}

}