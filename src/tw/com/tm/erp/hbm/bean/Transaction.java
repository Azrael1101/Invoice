package tw.com.tm.erp.hbm.bean;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.service.SoDepartmentOrderService;

public class Transaction{
	
	private SoSalesOrderHead soSalesOrderHead = new SoSalesOrderHead();
	private SoSalesOrderItem soSalesOrderItem = new SoSalesOrderItem();
	private SoSalesOrderPayment soSalesOrderPayment = new SoSalesOrderPayment();
	
	private String status;
	private Date salesOrderDate;
	private String shopCode;
	private Double totalActualSalesAmount;
	private String itemCode;
	private Double quantity;
	private String discountType;
    private Double discount;
    private String posPaymentType;
    //==brian 20211202
    private String orderNo;
    private String orderTypeCode;
    private String brandCode;
    private String currencyCode;
    private String superintendentCode;
    private Date scheduleShipDate;
    private Double exportCommissionRate;
    private Double exportExchangeRate;
    private String verificationStatus;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private String schedule;
    private Double discountRate;
    private String invoiceTypeCode;
    
    
    //SO bean
    public SoSalesOrderHead getSoSalesOrderHead() {
		return soSalesOrderHead;
	}
	public void setSoSalesOrderHead(SoSalesOrderHead soSalesOrderHead) {
		this.soSalesOrderHead = soSalesOrderHead;
	}
	public SoSalesOrderItem getSoSalesOrderItem() {
		return soSalesOrderItem;
	}
	public void setSoSalesOrderItem(SoSalesOrderItem soSalesOrderItem) {
		this.soSalesOrderItem = soSalesOrderItem;
	}
	public SoSalesOrderPayment getSoSalesOrderPayment() {
		return soSalesOrderPayment;
	}
	public void setSoSalesOrderPayment(SoSalesOrderPayment soSalesOrderPayment) {
		this.soSalesOrderPayment = soSalesOrderPayment;
	}
	
	//SoSalesOrderHead
	public String getStatus() {
		return soSalesOrderHead.getStatus();
	}
	public void setStatus(String status) {
		 soSalesOrderHead.setStatus(status);
		 this.status = status;
	}
	public Date getSalesOrderDate() {
		return soSalesOrderHead.getSalesOrderDate();
	}
	public void setSalesOrderDate(Date salesOrderDate) {
		soSalesOrderHead.setSalesOrderDate(salesOrderDate);
		this.salesOrderDate = salesOrderDate;
	}
	public String getShopCode() {
		return soSalesOrderHead.getShopCode();
	}
	public void setShopCode(String shopCode) {
		soSalesOrderHead.setShopCode(shopCode);
		this.shopCode = shopCode;
	}
	public Double getTotalActualSalesAmount() {
		return soSalesOrderHead.getTotalActualSalesAmount();
	}
	public void setTotalActualSalesAmount(Double totalActualSalesAmount) {
		soSalesOrderHead.setTotalActualSalesAmount(totalActualSalesAmount);
		this.totalActualSalesAmount = totalActualSalesAmount;
	}
	public String getOrderNo() {
		return soSalesOrderHead.getOrderNo();
	}
	public void setOrderNo(String orderNo) {
		soSalesOrderHead.setOrderNo(orderNo);
		this.orderNo = orderNo;
	}
	public String getOrderTypeCode() {
		return soSalesOrderHead.getOrderTypeCode();
	}
	public void setOrderTypeCode(String orderNo) {
		soSalesOrderHead.setOrderTypeCode(orderNo);
		this.orderNo = orderNo;
	}
	public String getBrandCode() {
		return soSalesOrderHead.getBrandCode();
	}
	public void setBrandCode(String brandCode) {
		soSalesOrderHead.setBrandCode(brandCode);
		this.brandCode = brandCode;
	}
	public String getCurrencyCode() {
		soSalesOrderHead.getCurrencyCode();
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		soSalesOrderHead.setCurrencyCode(currencyCode);
		this.currencyCode = currencyCode;
	}
	public String getSuperintendentCode() {
		return soSalesOrderHead.getSuperintendentCode();
	}
	public void setSuperintendentCode(String superintendentCode) {
		soSalesOrderHead.setSuperintendentCode(superintendentCode);
		this.superintendentCode = superintendentCode;
	}
	public Date getScheduleShipDate() {
		soSalesOrderHead.getScheduleShipDate();
		return scheduleShipDate;
	}
	public void setScheduleShipDate(Date scheduleShipDate) {
		soSalesOrderHead.setScheduleShipDate(scheduleShipDate);
		this.scheduleShipDate = scheduleShipDate;
	}
	public Double getExportCommissionRate() {
		return soSalesOrderHead.getExportCommissionRate();
	}
	public void setExportCommissionRate(Double exportCommissionRate) {
		soSalesOrderHead.setExportCommissionRate(exportCommissionRate);
		this.exportCommissionRate = exportCommissionRate;
	}
	public Double getExportExchangeRate() {
		return soSalesOrderHead.getExportExchangeRate();
	}
	public void setExportExchangeRate(Double exportExchangeRate) {
		soSalesOrderHead.setExportExchangeRate(exportExchangeRate);
		this.exportExchangeRate = exportExchangeRate;
	}
	public String getVerificationStatus() {
		return soSalesOrderHead.getVerificationStatus();
	}
	public void setVerificationStatus(String verificationStatus) {
		soSalesOrderHead.setVerificationStatus(verificationStatus);
		this.verificationStatus = verificationStatus;
	}
	public String getCreatedBy() {
		return soSalesOrderHead.getCreatedBy();
	}
	public void setCreatedBy(String createdBy) {
		soSalesOrderHead.setCreatedBy(createdBy);
		this.createdBy = createdBy;
	}
	public Date getCreationDate() {
		return soSalesOrderHead.getCreationDate();
	}
	public void setCreationDate(Date creationDate) {
		soSalesOrderHead.setCreationDate(creationDate);
		this.creationDate = creationDate;
	}
	public String getLastUpdatedBy() {
		return soSalesOrderHead.getLastUpdatedBy();
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		soSalesOrderHead.setLastUpdatedBy(lastUpdatedBy);
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastUpdateDate() {
		return soSalesOrderHead.getLastUpdateDate();
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		soSalesOrderHead.setLastUpdateDate(lastUpdateDate);
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getSchedule() {
		return soSalesOrderHead.getSchedule();
	}
	public void setSchedule(String schedule) {
		soSalesOrderHead.setSchedule(schedule);
		this.schedule = schedule;
	}
	public Double getDiscountRate() {
		return soSalesOrderHead.getDiscountRate();
	}
	public void setDiscountRate(Double discountRate) {
		soSalesOrderHead.setDiscountRate(discountRate);
		this.discountRate = discountRate;
	}
	public String getInvoiceTypeCode() {
		return soSalesOrderHead.getInvoiceTypeCode();
	}
	public void setInvoiceTypeCode(String invoiceTypeCode) {
		soSalesOrderHead.setInvoiceTypeCode(invoiceTypeCode);
		this.invoiceTypeCode = invoiceTypeCode;
	}
	
	//SoSalesOrderItem
	public String getItemCode() {
		return soSalesOrderItem.getItemCode();
	}
	public void setItemCode(String itemCode) {
		soSalesOrderItem.setItemCode(itemCode);
		this.itemCode = itemCode;
	}
	public Double getQuantity() {
		return soSalesOrderItem.getQuantity();
	}
	public void setQuantity(Double quantity) {
		soSalesOrderItem.setQuantity(quantity);
		this.quantity = quantity;
	}
	public String getDiscountType() {
		return soSalesOrderItem.getDiscountType();
	}
	public void setDiscountType(String discountType) {
		soSalesOrderItem.setDiscountType(discountType);
		this.discountType = discountType;
	}
	public Double getDiscount() {
		return soSalesOrderItem.getDiscount();
	}
	public void setDiscount(Double discount) {
		soSalesOrderItem.setDiscount(discount);
		this.discount = discount;
	}
	
	//SoSalesOrderPayment
	public String getPosPaymentType() {
		return soSalesOrderPayment.getPosPaymentType();
	}
	public void setPosPaymentType(String posPaymentType) {
		soSalesOrderPayment.setPosPaymentType(posPaymentType);
		this.posPaymentType = posPaymentType;
	}
	
	
	
	
}
