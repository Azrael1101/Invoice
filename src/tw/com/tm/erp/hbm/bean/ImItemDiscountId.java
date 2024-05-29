package tw.com.tm.erp.hbm.bean;

/**
 * ImItemDiscountId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemDiscountId implements java.io.Serializable {

	// Fields

	private String brandCode;
	private String vipTypeCode;
	private String itemDiscountType;

	// Constructors

	/** default constructor */
	public ImItemDiscountId() {
	}

	/** full constructor */
	public ImItemDiscountId(String brandCode, String vipTypeCode, String itemDiscountType) {
		this.brandCode = brandCode;
		this.vipTypeCode = vipTypeCode;
		this.itemDiscountType = itemDiscountType;
	}

	// Property accessors

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getVipTypeCode() {
		return this.vipTypeCode;
	}

	public void setVipTypeCode(String vipTypeCode) {
		this.vipTypeCode = vipTypeCode;
	}

	public String getItemDiscountType() {
		return this.itemDiscountType;
	}

	public void setItemDiscountType(String itemDiscountType) {
		this.itemDiscountType = itemDiscountType;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ImItemDiscountId))
			return false;
		ImItemDiscountId castOther = (ImItemDiscountId) other;

		return ((this.getBrandCode() == castOther.getBrandCode()) || (this
				.getBrandCode() != null
				&& castOther.getBrandCode() != null && this.getBrandCode()
				.equals(castOther.getBrandCode())))
				&& ((this.getVipTypeCode() == castOther.getVipTypeCode()) || (this
						.getVipTypeCode() != null
						&& castOther.getVipTypeCode() != null && this
						.getVipTypeCode().equals(castOther.getVipTypeCode())))
				&& ((this.getItemDiscountType() == castOther
						.getItemDiscountType()) || (this.getItemDiscountType() != null
						&& castOther.getItemDiscountType() != null && this
						.getItemDiscountType().equals(
								castOther.getItemDiscountType())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
		result = 37
				* result
				+ (getVipTypeCode() == null ? 0 : this.getVipTypeCode()
						.hashCode());
		result = 37
				* result
				+ (getItemDiscountType() == null ? 0 : this
						.getItemDiscountType().hashCode());
		return result;
	}

}