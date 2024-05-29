package tw.com.tm.erp.hbm.bean;

/**
 * ImMonthlyBalanceHeadId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImMonthlyBalanceHeadId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6913652399930578300L;
	private String brandCode;
	private String itemCode;
	private String year;
	private String month;

	// Constructors

	/** default constructor */
	public ImMonthlyBalanceHeadId() {
	}

	/** full constructor */
	public ImMonthlyBalanceHeadId(String brandCode, String itemCode,
			String year, String month) {
		this.brandCode = brandCode;
		this.itemCode = itemCode;
		this.year = year;
		this.month = month;
	}

	// Property accessors

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ImMonthlyBalanceHeadId))
			return false;
		ImMonthlyBalanceHeadId castOther = (ImMonthlyBalanceHeadId) other;

		return ((this.getBrandCode() == castOther.getBrandCode()) || (this
				.getBrandCode() != null
				&& castOther.getBrandCode() != null && this.getBrandCode()
				.equals(castOther.getBrandCode())))
				&& ((this.getItemCode() == castOther.getItemCode()) || (this
						.getItemCode() != null
						&& castOther.getItemCode() != null && this
						.getItemCode().equals(castOther.getItemCode())))
				&& ((this.getYear() == castOther.getYear()) || (this.getYear() != null
						&& castOther.getYear() != null && this.getYear()
						.equals(castOther.getYear())))
				&& ((this.getMonth() == castOther.getMonth()) || (this
						.getMonth() != null
						&& castOther.getMonth() != null && this.getMonth()
						.equals(castOther.getMonth())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		result = 37 * result
				+ (getYear() == null ? 0 : this.getYear().hashCode());
		result = 37 * result
				+ (getMonth() == null ? 0 : this.getMonth().hashCode());
		return result;
	}

}