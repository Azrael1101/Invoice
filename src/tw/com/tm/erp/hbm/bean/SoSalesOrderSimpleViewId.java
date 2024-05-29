package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoSalesOrderSimpleViewId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoSalesOrderSimpleViewId implements java.io.Serializable {

	// Fields

	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private Long indexNo;
	
	public SoSalesOrderSimpleViewId() {

	}
	/**
	 * @param brandCode
	 * @param orderTypeCode
	 * @param orderNo
	 * @param indexNo
	 */
	public SoSalesOrderSimpleViewId(String brandCode, String orderTypeCode,
			String orderNo, Long indexNo) {
		this.brandCode = brandCode;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.indexNo = indexNo;
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
	 * @return the indexNo
	 */
	public Long getIndexNo() {
		return indexNo;
	}
	/**
	 * @param indexNo the indexNo to set
	 */
	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
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
		result = prime * result + ((indexNo == null) ? 0 : indexNo.hashCode());
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
		final SoSalesOrderSimpleViewId other = (SoSalesOrderSimpleViewId) obj;
		if (brandCode == null) {
			if (other.brandCode != null)
				return false;
		} else if (!brandCode.equals(other.brandCode))
			return false;
		if (indexNo == null) {
			if (other.indexNo != null)
				return false;
		} else if (!indexNo.equals(other.indexNo))
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