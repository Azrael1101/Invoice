package tw.com.tm.erp.hbm.bean;

/**
 * SiGroupObjectId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiGroupObjectId implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -7280633159215739195L;
	private String brandCode;
	private String groupCode;
	private String functionCode;
	private String objectCode;

	// Constructors

	/** default constructor */
	public SiGroupObjectId() {
	}

	/** full constructor */
	public SiGroupObjectId(String brandCode, String groupCode,
			String functionCode, String objectCode) {
		this.brandCode = brandCode;
		this.groupCode = groupCode;
		this.functionCode = functionCode;
		this.objectCode = objectCode;
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

	public String getFunctionCode() {
		return this.functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getObjectCode() {
		return this.objectCode;
	}

	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SiGroupObjectId))
			return false;
		SiGroupObjectId castOther = (SiGroupObjectId) other;

		return ((this.getBrandCode() == castOther.getBrandCode()) || (this
				.getBrandCode() != null
				&& castOther.getBrandCode() != null && this.getBrandCode()
				.equals(castOther.getBrandCode())))
				&& ((this.getGroupCode() == castOther.getGroupCode()) || (this
						.getGroupCode() != null
						&& castOther.getGroupCode() != null && this
						.getGroupCode().equals(castOther.getGroupCode())))
				&& ((this.getFunctionCode() == castOther.getFunctionCode()) || (this
						.getFunctionCode() != null
						&& castOther.getFunctionCode() != null && this
						.getFunctionCode().equals(castOther.getFunctionCode())))
				&& ((this.getObjectCode() == castOther.getObjectCode()) || (this
						.getObjectCode() != null
						&& castOther.getObjectCode() != null && this
						.getObjectCode().equals(castOther.getObjectCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
		result = 37 * result
				+ (getGroupCode() == null ? 0 : this.getGroupCode().hashCode());
		result = 37
				* result
				+ (getFunctionCode() == null ? 0 : this.getFunctionCode()
						.hashCode());
		result = 37
				* result
				+ (getObjectCode() == null ? 0 : this.getObjectCode()
						.hashCode());
		return result;
	}

}