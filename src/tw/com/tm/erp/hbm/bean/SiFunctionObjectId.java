package tw.com.tm.erp.hbm.bean;

/**
 * SiFunctionObjectId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiFunctionObjectId implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1991910782477542519L;
	private String functionCode;
	private String objectCode;

	// Constructors

	/** default constructor */
	public SiFunctionObjectId() {
	}

	/** full constructor */
	public SiFunctionObjectId(String functionCode, String objectCode) {
		this.functionCode = functionCode;
		this.objectCode = objectCode;
	}

	// Property accessors

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
		if (!(other instanceof SiFunctionObjectId))
			return false;
		SiFunctionObjectId castOther = (SiFunctionObjectId) other;

		return ((this.getFunctionCode() == castOther.getFunctionCode()) || (this
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