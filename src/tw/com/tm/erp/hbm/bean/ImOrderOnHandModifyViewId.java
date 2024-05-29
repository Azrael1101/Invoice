package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemOnHandView entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class ImOrderOnHandModifyViewId implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5722210655309197538L;
	private String orderNo;
	private String orderTypeCode;
	private String brandCode;

	public ImOrderOnHandModifyViewId() {

	}

	/**
	 * @param orderNo
	 * @param orderTypeCode
	 * @param brandCode
	 */
	public ImOrderOnHandModifyViewId(String orderNo, String orderTypeCode,
			String brandCode) {
		this.orderNo = orderNo;
		this.orderTypeCode = orderTypeCode;
		this.brandCode = brandCode;
	}

	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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
		result = prime * result + ((orderNo == null) ? 0 : orderNo.hashCode());
		result = prime * result
				+ ((orderTypeCode == null) ? 0 : orderTypeCode.hashCode());
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
		final ImOrderOnHandModifyViewId other = (ImOrderOnHandModifyViewId) obj;
		if (brandCode == null) {
			if (other.brandCode != null)
				return false;
		} else if (!brandCode.equals(other.brandCode))
			return false;
		if (orderNo == null) {
			if (other.orderNo != null)
				return false;
		} else if (!orderNo.equals(other.orderNo))
			return false;
		if (orderTypeCode == null) {
			if (other.orderTypeCode != null)
				return false;
		} else if (!orderTypeCode.equals(other.orderTypeCode))
			return false;
		return true;
	}

	






	
}