package tw.com.tm.erp.hbm.bean;

/**
 * ImItemCategoryId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemCategoryId implements java.io.Serializable {

	// Fields

	private String brandCode;
	private String categoryType;
	private String categoryCode;

	// Constructors

	/** default constructor */
	public ImItemCategoryId() {
	}

	/** full constructor */
	public ImItemCategoryId(String brandCode, String categoryType,
			String categoryCode) {
		this.brandCode = brandCode;
		this.categoryType = categoryType;
		this.categoryCode = categoryCode;
	}

	// Property accessors

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getCategoryType() {
		return this.categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getCategoryCode() {
		return this.categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ImItemCategoryId))
			return false;
		ImItemCategoryId castOther = (ImItemCategoryId) other;

		return ((this.getBrandCode() == castOther.getBrandCode()) || (this
				.getBrandCode() != null
				&& castOther.getBrandCode() != null && this.getBrandCode()
				.equals(castOther.getBrandCode())))
				&& ((this.getCategoryType() == castOther.getCategoryType()) || (this
						.getCategoryType() != null
						&& castOther.getCategoryType() != null && this
						.getCategoryType().equals(castOther.getCategoryType())))
				&& ((this.getCategoryCode() == castOther.getCategoryCode()) || (this
						.getCategoryCode() != null
						&& castOther.getCategoryCode() != null && this
						.getCategoryCode().equals(castOther.getCategoryCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
		result = 37
				* result
				+ (getCategoryType() == null ? 0 : this.getCategoryType()
						.hashCode());
		result = 37
				* result
				+ (getCategoryCode() == null ? 0 : this.getCategoryCode()
						.hashCode());
		return result;
	}

}