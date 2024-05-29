package tw.com.tm.erp.hbm.bean;

import java.util.Date;

public class BuCustomerCouponRecord implements java.io.Serializable {
	private static final long serialVersionUID = -2734279202809927838L;
	
	private Long headId;
	private String customerCode;
	private Date useDate;
	private String type;
	private String couponNo;
	private String inCharge;
	private String shopCode;
	private Long salesOrderId;
	private String useYear;
	private String status;
	/**
	 * @return the headId
	 */
	public Long getHeadId() {
		return headId;
	}
	/**
	 * @param headId the headId to set
	 */
	public void setHeadId(Long headId) {
		this.headId = headId;
	}
	/**
	 * @return the customerCode
	 */
	public String getCustomerCode() {
		return customerCode;
	}
	/**
	 * @param customerCode the customerCode to set
	 */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}


	/**
	 * @return the useDate
	 */
	public Date getUseDate() {
		return useDate;
	}
	/**
	 * @param useDate the useDate to set
	 */
	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the couponNo
	 */
	public String getCouponNo() {
		return couponNo;
	}
	/**
	 * @param couponNo the couponNo to set
	 */
	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}
	/**
	 * @return the inCharge
	 */
	public String getInCharge() {
		return inCharge;
	}
	/**
	 * @param inCharge the inCharge to set
	 */
	public void setInCharge(String inCharge) {
		this.inCharge = inCharge;
	}
	/**
	 * @return the shopCode
	 */
	public String getShopCode() {
		return shopCode;
	}
	/**
	 * @param shopCode the shopCode to set
	 */
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public Long getSalesOrderId() {
		return salesOrderId;
	}
	public void setSalesOrderId(Long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}
	public String getUseYear() {
		return useYear;
	}
	public void setUseYear(String useYear) {
		this.useYear = useYear;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
}
