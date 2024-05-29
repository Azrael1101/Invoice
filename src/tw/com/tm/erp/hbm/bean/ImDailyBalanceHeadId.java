package tw.com.tm.erp.hbm.bean;

/**
 * ImMonthlyBalanceHeadId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImDailyBalanceHeadId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6913652399930578300L;
	private String brandCode;
	private String itemCode;
	private String dailyDate;

	// Constructors

	/** default constructor */
	public ImDailyBalanceHeadId() {
	}

	/** full constructor */
	public ImDailyBalanceHeadId(String brandCode, String itemCode,
			String dailyDate) {
		this.brandCode = brandCode;
		this.itemCode = itemCode;
		this.dailyDate = dailyDate;
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

	public String getDailyDate() {
		return this.dailyDate;
	}

	public void setDailyDate(String dailyDate) {
		this.dailyDate = dailyDate;
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
				&& ((this.getDailyDate() == castOther.getYear()) || (this.getDailyDate() != null
						&& castOther.getYear() != null && this.getDailyDate()
						.equals(castOther.getYear())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		result = 37 * result
				+ (getDailyDate() == null ? 0 : this.getDailyDate().hashCode());
		return result;
	}

}