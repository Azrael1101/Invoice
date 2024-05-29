package tw.com.tm.erp.hbm.bean;

import java.util.Date;

public class SiResendId implements java.io.Serializable {
	
	// Constructors
	/** default constructor */
	public SiResendId() {
	}
	
	private String orderTypeCode;
	private String orderNo;
	
	public SiResendId(String orderTypeCode,String orderNo){
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
	}
	
	public String getOrderTypeCode() {
		return orderTypeCode;
	}
	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}	
}
