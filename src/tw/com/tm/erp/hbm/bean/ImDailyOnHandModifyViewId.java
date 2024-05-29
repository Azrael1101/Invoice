package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemOnHandView entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class ImDailyOnHandModifyViewId implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5722210655309197538L;
	private String year;
	private String month;
	private String day;
	private String orderTypeCode;
	private String brandCode;

	public ImDailyOnHandModifyViewId() {

	}

	/**
	 * @param year
	 * @param month
	 * @param day
	 * @param orderTypeCode
	 * @param brandCode
	 */
	public ImDailyOnHandModifyViewId(String year, String month, String day,
			String orderTypeCode, String brandCode) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.orderTypeCode = orderTypeCode;
		this.brandCode = brandCode;
	}
	
	

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}



	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}



	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}



	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}



	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}



	/**
	 * @param day the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}



	/**
	 * @return the orderTypeCode
	 */
	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	/**
	 * @param orderTypeCode the orderTypeCode to set
	 */
	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	/**
	 * @return the brandCode
	 */
	public String getBrandCode() {
		return brandCode;
	}

	/**
	 * @param brandCode the brandCode to set
	 */
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((brandCode == null) ? 0 : brandCode.hashCode());
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result
				+ ((orderTypeCode == null) ? 0 : orderTypeCode.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ImDailyOnHandModifyViewId other = (ImDailyOnHandModifyViewId) obj;
		if (brandCode == null) {
			if (other.brandCode != null)
				return false;
		} else if (!brandCode.equals(other.brandCode))
			return false;
		if (day == null) {
			if (other.day != null)
				return false;
		} else if (!day.equals(other.day))
			return false;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		if (orderTypeCode == null) {
			if (other.orderTypeCode != null)
				return false;
		} else if (!orderTypeCode.equals(other.orderTypeCode))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}






	
}