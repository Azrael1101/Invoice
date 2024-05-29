package tw.com.tm.erp.hbm.bean;

import java.sql.Date;

/**
 * 紅利累積點數設定檔
 * @author MyEclipse Persistence Tools
 */

public class SnsBonusPointSetting implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6134217804346084961L;
	private Long XID;
	private String brandCode;
	private String customerLevel;
	private String categoryCode;
	private Double bonusRate;
	private Double bonusProgramRate;
	private Double maxDiscountAmount;
	private Date deadline;

	public SnsBonusPointSetting(Long XID, String brandCode, String customerLevel,
			String categoryCode, Double bonusRate, Double bonusProgramRate,
			Double maxDiscountAmount, Date deadline) {
		super();
		this.XID = XID;
		this.brandCode = brandCode;
		this.customerLevel = customerLevel;
		this.categoryCode = categoryCode;
		this.bonusRate = bonusRate;
		this.bonusProgramRate = bonusProgramRate;
		this.maxDiscountAmount = maxDiscountAmount;
		this.deadline = deadline;
	}

	public SnsBonusPointSetting() {
		super();
	}


	public Long getXID() {
		return XID;
	}

	public void setXID(Long XID) {
		this.XID = XID;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getCustomerLevel() {
		return customerLevel;
	}

	public void setCustomerLevel(String level) {
		this.customerLevel = level;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Double getBonusRate() {
		return bonusRate;
	}

	public void setBonusRate(Double bonusRate) {
		this.bonusRate = bonusRate;
	}

	public Double getBonusProgramRate() {
		return bonusProgramRate;
	}

	public void setBonusProgramRate(Double bonusProgramRate) {
		this.bonusProgramRate = bonusProgramRate;
	}

	public Double getMaxDiscountAmount() {
		return maxDiscountAmount;
	}

	public void setMaxDiscountAmount(Double maxDiscountAmount) {
		this.maxDiscountAmount = maxDiscountAmount;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
