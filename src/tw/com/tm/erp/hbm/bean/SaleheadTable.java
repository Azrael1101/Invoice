package tw.com.tm.erp.hbm.bean;

import java.util.Date;
import java.util.List;

public class SaleheadTable implements java.io.Serializable  {

	private static final long serialVersionUID = -4312303996853625355L;
	private Long headSno;
	private Long transactionSno;
	private String posMachineCode;
	private String status;
	private Date salesOrderDate;
	private String shopCode;
	private Double totalActualSalesAmt;
	private String customerPoNo;
	private String printStatus;
	private String transactionTime;
	private String receiverMemo;
	private String brandCode;
	private String areaCode; 
	private String orderTypeCode;
	private String taxType;
	private Double taxRate;
	private String invoiceNo;
	private String invoiceAddress;
	private String receiverTele;
	private String transportContent;
	
	
	public String getReceiverMemo() {
		return receiverMemo;
	}

	public void setReceiverMemo(String receiverMemo) {
		this.receiverMemo = receiverMemo;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public String getReceiverTele() {
		return receiverTele;
	}

	public void setReceiverTele(String receiverTele) {
		this.receiverTele = receiverTele;
	}

	public String getTransportContent() {
		return transportContent;
	}

	public void setTransportContent(String transportContent) {
		this.transportContent = transportContent;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public SaleheadTable() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public SaleheadTable(Long headSno, Long transactionSno, String posMachineCode, String status, Date salesOrderDate,
			String shopCode, Double totalActualSalesAmt, String customerPoNo, String printStatus) {
		super();
		this.headSno = headSno;
		this.transactionSno = transactionSno;
		this.posMachineCode = posMachineCode;
		this.status = status;
		this.salesOrderDate = salesOrderDate;
		this.shopCode = shopCode;
		this.totalActualSalesAmt = totalActualSalesAmt;
		this.customerPoNo = customerPoNo;
		this.printStatus = printStatus;
	}

	/**
	 * @return the headSno
	 */
	public Long getHeadSno() {
		return headSno;
	}
	/**
	 * @param headSno the headSno to set
	 */
	public void setHeadSno(Long headSno) {
		this.headSno = headSno;
	}
	/**
	 * @return the transactionSno
	 */
	public Long getTransactionSno() {
		return transactionSno;
	}
	/**
	 * @param transactionSno the transactionSno to set
	 */
	public void setTransactionSno(Long transactionSno) {
		this.transactionSno = transactionSno;
	}
	/**
	 * @return the posMachineCode
	 */
	public String getPosMachineCode() {
		return posMachineCode;
	}
	/**
	 * @param posMachineCode the posMachineCode to set
	 */
	public void setPosMachineCode(String posMachineCode) {
		this.posMachineCode = posMachineCode;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the salesOrderDate
	 */
	public Date getSalesOrderDate() {
		return salesOrderDate;
	}
	/**
	 * @param salesOrderDate the salesOrderDate to set
	 */
	public void setSalesOrderDate(Date salesOrderDate) {
		this.salesOrderDate = salesOrderDate;
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
	/**
	 * @return the totalActualSalesAmt
	 */
	public Double getTotalActualSalesAmt() {
		return totalActualSalesAmt;
	}
	/**
	 * @param totalActualSalesAmt the totalActualSalesAmt to set
	 */
	public void setTotalActualSalesAmt(Double totalActualSalesAmt) {
		this.totalActualSalesAmt = totalActualSalesAmt;
	}
	/**
	 * @return the customerPoNo
	 */
	public String getCustomerPoNo() {
		return customerPoNo;
	}
	/**
	 * @param customerPoNo the customerPoNo to set
	 */
	public void setCustomerPoNo(String customerPoNo) {
		this.customerPoNo = customerPoNo;
	}
	/**
	 * @return the printStatus
	 */
	public String getPrintStatus() {
		return printStatus;
	}
	/**
	 * @param printStatus the printStatus to set
	 */
	public void setPrintStatus(String printStatus) {
		this.printStatus = printStatus;
	}
	
	
	
	
	
}
