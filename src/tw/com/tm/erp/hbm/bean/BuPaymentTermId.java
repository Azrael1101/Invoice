package tw.com.tm.erp.hbm.bean;

/**
 * BuPaymentTermId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuPaymentTermId implements java.io.Serializable {

	// Fields


	private static final long serialVersionUID = -9954719811517816L;
	private String organizationCode;
	private String paymentTermCode;

	// Constructors

	/** default constructor */
	public BuPaymentTermId() {
	}

	/** full constructor */
	public BuPaymentTermId(String organizationCode, String paymentTermCode) {
		this.organizationCode = organizationCode;
		this.paymentTermCode = paymentTermCode;
	}

	// Property accessors

	public String getOrganizationCode() {
		return this.organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getPaymentTermCode() {
		return this.paymentTermCode;
	}

	public void setPaymentTermCode(String paymentTermCode) {
		this.paymentTermCode = paymentTermCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BuPaymentTermId))
			return false;
		BuPaymentTermId castOther = (BuPaymentTermId) other;

		return ((this.getOrganizationCode() == castOther.getOrganizationCode()) || (this
				.getOrganizationCode() != null
				&& castOther.getOrganizationCode() != null && this
				.getOrganizationCode().equals(castOther.getOrganizationCode())))
				&& ((this.getPaymentTermCode() == castOther
						.getPaymentTermCode()) || (this.getPaymentTermCode() != null
						&& castOther.getPaymentTermCode() != null && this
						.getPaymentTermCode().equals(
								castOther.getPaymentTermCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getOrganizationCode() == null ? 0 : this
						.getOrganizationCode().hashCode());
		result = 37
				* result
				+ (getPaymentTermCode() == null ? 0 : this.getPaymentTermCode()
						.hashCode());
		return result;
	}

}