package tw.com.tm.erp.hbm.bean;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * 
 * 
 * SoSalesOrderHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoDepartmentOrderHead implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5198509865440016082L;
	// Fields
	private Long headId;
	private String brandCode;
	private Long salesoOrderId;
	private Date salesOrderDate;
	private String customerCode;
	private String customerPoNo;
	private String paymentTermCode;
	private String shopCode;
	private String superintendentCode;
	private Double totalOriginalSalesAmount;
	private Double totalActualSalesAmount;
	private String transactionSeqNo;
	private String remark1;
	private String status;
	private String createdBy;
	private String lastUpdatedBy;
	private String salesType;
	private Date salesTime;
	private Double paymentCard;
	private Double paymentCash;
	private Double paymentGroupon;
	private List<SoDepartmentOrderItem> soDepartmentOrderItems = new ArrayList();
	private String guiCode;





	public void setGuiCode(String guiCode) {
		this.guiCode = guiCode;
	}





	public List<SoDepartmentOrderItem> getSoDepartmentOrderItems() {
		return soDepartmentOrderItems;
	}





	public void setSoDepartmentOrderItems(List<SoDepartmentOrderItem> soDepartmentOrderItems) {
		this.soDepartmentOrderItems = soDepartmentOrderItems;
	}




	private List<SoDepartmentOrderItem> soDepartmentOrderItem = new ArrayList(0);



	// Constructors
	/** default constructor */
	public SoDepartmentOrderHead() {
	}
	




	public Long getHeadId() {
		return headId;
	}




	public void setHeadId(Long headId) {
		this.headId = headId;
	}




	public String getBrandCode() {
		return brandCode;
	}




	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}




	public Long getSalesoOrderId() {
		return salesoOrderId;
	}




	public void setSalesoOrderId(Long salesoOrderId) {
		this.salesoOrderId = salesoOrderId;
	}




	public Date getSalesOrderDate() {
		return salesOrderDate;
	}




	public void setSalesOrderDate(Date salesOrderDate) {
		this.salesOrderDate = salesOrderDate;
	}




	public String getCustomerCode() {
		return customerCode;
	}




	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}




	public String getCustomerPoNo() {
		return customerPoNo;
	}




	public void setCustomerPoNo(String customerPoNo) {
		this.customerPoNo = customerPoNo;
	}




	public String getPaymentTermCode() {
		return paymentTermCode;
	}




	public void setPaymentTermCode(String paymentTermCode) {
		this.paymentTermCode = paymentTermCode;
	}




	public String getShopCode() {
		return shopCode;
	}




	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}




	public String getSuperintendentCode() {
		return superintendentCode;
	}




	public void setSuperintendentCode(String superintendentCode) {
		this.superintendentCode = superintendentCode;
	}




	public Double getTotalOriginalSalesAmount() {
		return totalOriginalSalesAmount;
	}




	public void setTotalOriginalSalesAmount(Double totalOriginalSalesAmount) {
		this.totalOriginalSalesAmount = totalOriginalSalesAmount;
	}




	public Double getTotalActualSalesAmount() {
		return totalActualSalesAmount;
	}




	public void setTotalActualSalesAmount(Double totalActualSalesAmount) {
		this.totalActualSalesAmount = totalActualSalesAmount;
	}




	public String getTransactionSeqNo() {
		return transactionSeqNo;
	}




	public void setTransactionSeqNo(String transactionSeqNo) {
		this.transactionSeqNo = transactionSeqNo;
	}




	public String getRemark1() {
		return remark1;
	}




	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}




	public String getStatus() {
		return status;
	}




	public void setStatus(String status) {
		this.status = status;
	}




	public String getCreatedBy() {
		return createdBy;
	}




	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}




	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}




	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}




	public String getSalesType() {
		return salesType;
	}




	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}





	public List<SoDepartmentOrderItem> getSoDepartmentOrderItem() {
		return soDepartmentOrderItem;
	}





	public void setSoDepartmentOrderItem(
			List<SoDepartmentOrderItem> soDepartmentOrderItem) {
		this.soDepartmentOrderItem = soDepartmentOrderItem;
	}





	public Date getSalesTime() {
		return salesTime;
	}





	public void setSalesTime(Date salesTime) {
		this.salesTime = salesTime;
	}





	public Double getPaymentCard() {
		return paymentCard;
	}





	public void setPaymentCard(Double paymentCard) {
		this.paymentCard = paymentCard;
	}





	public Double getPaymentCash() {
		return paymentCash;
	}





	public void setPaymentCash(Double paymentCash) {
		this.paymentCash = paymentCash;
	}





	public Double getPaymentGroupon() {
		return paymentGroupon;
	}





	public void setPaymentGroupon(Double paymentGroupon) {
		this.paymentGroupon = paymentGroupon;
	}





	public String getGuiCode() {
		// TODO Auto-generated method stub
		return null;
	}













}