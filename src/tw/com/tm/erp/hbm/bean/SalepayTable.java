package tw.com.tm.erp.hbm.bean;

import java.util.Date;
import java.util.List;

public class SalepayTable implements java.io.Serializable  {

	private static final long serialVersionUID = -4312303996853625355L;
	private Long paymentSno;
	private Long headSno;
	private Long transactionSno;
	private String posMachineCode;
	private String shopCode;
	private Date salesOrderDate;
	private Long indexSno;
	public Long getPaymentSno() {
		return paymentSno;
	}
	public void setPaymentSno(Long paymentSno) {
		this.paymentSno = paymentSno;
	}
	public Long getHeadSno() {
		return headSno;
	}
	public void setHeadSno(Long headSno) {
		this.headSno = headSno;
	}
	public Long getTransactionSno() {
		return transactionSno;
	}
	public void setTransactionSno(Long transactionSno) {
		this.transactionSno = transactionSno;
	}
	public String getPosMachineCode() {
		return posMachineCode;
	}
	public void setPosMachineCode(String posMachineCode) {
		this.posMachineCode = posMachineCode;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public Date getSalesOrderDate() {
		return salesOrderDate;
	}
	public void setSalesOrderDate(Date salesOrderDate) {
		this.salesOrderDate = salesOrderDate;
	}
	public Long getIndexSno() {
		return indexSno;
	}
	public void setIndexSno(Long indexSno) {
		this.indexSno = indexSno;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
