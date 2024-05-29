package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImDeliveryHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImDeliveryHead implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7138074944619036100L;
	// Fields
	private Long headId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private String origDeliveryOrderTypeCode;
	private String origDeliveryOrderNo;
	private Long salesOrderId;
	private Date shipDate;
	private Date scheduleShipDate;
	private Date returnDate;
	private String customerCode;
	private String customerName;
	private String customerPoNo;
	private String quotationCode;
	private String paymentTermCode;
	private String paymentTermName;
	private String countryCode;
	private String countryName;
	private String currencyCode;
	private String currencyName;
	private String shopCode;
	private String shopName;
	private String contactPerson;
	private String contactTel;
	private String receiver;
	private String superintendentCode;
	private String superintendentName;
	private String invoiceTypeCode;
	private String guiCode;
	private String taxType;
	private Double taxRate;
	private Double taxAmount;
	private Double shipTaxAmount;
	private Double returnTaxAmount;
	private Date scheduleCollectionDate;
	private String invoiceCity;
	private String invoiceArea;
	private String invoiceZipCode;
	private String invoiceAddress;
	private String shipCity;
	private String shipArea;
	private String shipZipCode;
	private String shipAddress;
	private String defaultWarehouseCode;
	private String defaultWarehouseName;
	private String promotionCode;
	private String promotionName;
	private Double totalOriginalSalesAmount;
	private Double totalActualSalesAmount;
	private Double totalOriginalShipAmount;
	private Double totalActualShipAmount;   
	private Double totalOriginalReturnAmount;
	private Double totalActualReturnAmount;
	private Double totalDeductionAmount;
	private Double totalOtherExpense;
	private Double totalNoneTaxShipAmount;
	private Double totalItemQuantity;
	private Double discountRate;
	private String posMachineCode;
	private String memberCardNo;
	private String nationalityCode;
	private String casherCode;
	private String transactionSeqNo;
	private Date departureDate;
	private String salesInvoicePage;
	private String flightNo;
	private String passportNo;
	private String ladingNo;
	private String sufficientQuantityDelivery;
	private String invoiceNo;
	private Date invoiceDate;
	private String invoicePrintLotNo;
	private String remark1;
	private String remark2;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String status;
	private String statusName;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String homeDelivery;
	private String paymentCategory;
	private String attachedInvoice;
	private String exportDeclNo;
	private Date exportDeclDate;
	private String exportDeclType;
	private Double exportBoxAmount;
	private String exportTaxType;
	private Double exportExchangeRate;
	private Double originalTotalFrnSalesAmt;
	private Double actualTotalFrnSalesAmt;
	private Double originalTotalFrnShipAmt;
	private Double actualTotalFrnShipAmt;
	private String latestExportDeclNo;
	private Date latestExportDeclDate;
	private String latestExportDeclType;
	private Double exportCommissionRate;
	private Double expenseForeignAmount;
	private Double expenseLocalAmount;
	private Double exportExpense;
	private String transactionTime;
	private String exportDeclNoLog;
	private String orderDiscountType;
	private String itemCategory;
	private Date receiptDate;
	private String islandsCode;
	private String storeCode;
	private String transactionId;
	private String buyerId;
	private String warehouseStatus;
	private Double displayPer;
	private Double baselPer;
	private Double baselPer2;
	private Double salePer;
	private Double displayAmount;
	private Double baselAmount;
	private Double baselAmount2;
	private Double saleAmount;
	private String flightNoSales;
	private String passportNoSales;
	private String transactionMinute;
	private String transactionSecond;
	private String customsStatus;
	private String tranAllowUpload;
	private String tranRecordStatus;
	private String cStatus;
	private String schedule;
	
	private String customsNo;
	
	private List<ImDeliveryLine> imDeliveryLines = new ArrayList(0);

	// Constructors
	/** default constructor */
	public ImDeliveryHead() {
	}

	// Property accessors
	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getOrderTypeCode() {
		return this.orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrigDeliveryOrderTypeCode() {
		return origDeliveryOrderTypeCode;
	}

	public void setOrigDeliveryOrderTypeCode(String origDeliveryOrderTypeCode) {
		this.origDeliveryOrderTypeCode = origDeliveryOrderTypeCode;
	}

	public String getOrigDeliveryOrderNo() {
		return origDeliveryOrderNo;
	}

	public void setOrigDeliveryOrderNo(String origDeliveryOrderNo) {
		this.origDeliveryOrderNo = origDeliveryOrderNo;
	}

	public Long getSalesOrderId() {
		return this.salesOrderId;
	}

	public void setSalesOrderId(Long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public Date getShipDate() {
		return this.shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	public Date getScheduleShipDate() {
		return this.scheduleShipDate;
	}

	public void setScheduleShipDate(Date scheduleShipDate) {
		this.scheduleShipDate = scheduleShipDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getCustomerCode() {
		return this.customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPoNo() {
		return this.customerPoNo;
	}

	public void setCustomerPoNo(String customerPoNo) {
		this.customerPoNo = customerPoNo;
	}

	public String getQuotationCode() {
		return this.quotationCode;
	}

	public void setQuotationCode(String quotationCode) {
		this.quotationCode = quotationCode;
	}

	public String getPaymentTermCode() {
		return this.paymentTermCode;
	}

	public void setPaymentTermCode(String paymentTermCode) {
		this.paymentTermCode = paymentTermCode;
	}

	public String getPaymentTermName() {
		return paymentTermName;
	}

	public void setPaymentTermName(String paymentTermName) {
		this.paymentTermName = paymentTermName;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSuperintendentCode() {
		return this.superintendentCode;
	}

	public void setSuperintendentCode(String superintendentCode) {
		this.superintendentCode = superintendentCode;
	}

	public String getSuperintendentName() {
		return superintendentName;
	}

	public void setSuperintendentName(String superintendentName) {
		this.superintendentName = superintendentName;
	}

	public String getInvoiceTypeCode() {
		return this.invoiceTypeCode;
	}

	public void setInvoiceTypeCode(String invoiceTypeCode) {
		this.invoiceTypeCode = invoiceTypeCode;
	}

	public String getGuiCode() {
		return this.guiCode;
	}

	public void setGuiCode(String guiCode) {
		this.guiCode = guiCode;
	}

	public String getTaxType() {
		return this.taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public Double getTaxRate() {
		return this.taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getShipTaxAmount() {
		return shipTaxAmount;
	}

	public void setShipTaxAmount(Double shipTaxAmount) {
		this.shipTaxAmount = shipTaxAmount;
	}

	public Double getReturnTaxAmount() {
		return returnTaxAmount;
	}

	public void setReturnTaxAmount(Double returnTaxAmount) {
		this.returnTaxAmount = returnTaxAmount;
	}

	public Date getScheduleCollectionDate() {
		return this.scheduleCollectionDate;
	}

	public void setScheduleCollectionDate(Date scheduleCollectionDate) {
		this.scheduleCollectionDate = scheduleCollectionDate;
	}

	public String getInvoiceCity() {
		return this.invoiceCity;
	}

	public void setInvoiceCity(String invoiceCity) {
		this.invoiceCity = invoiceCity;
	}

	public String getInvoiceArea() {
		return this.invoiceArea;
	}

	public void setInvoiceArea(String invoiceArea) {
		this.invoiceArea = invoiceArea;
	}

	public String getInvoiceZipCode() {
		return this.invoiceZipCode;
	}

	public void setInvoiceZipCode(String invoiceZipCode) {
		this.invoiceZipCode = invoiceZipCode;
	}

	public String getInvoiceAddress() {
		return this.invoiceAddress;
	}

	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public String getShipCity() {
		return this.shipCity;
	}

	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}

	public String getShipArea() {
		return this.shipArea;
	}

	public void setShipArea(String shipArea) {
		this.shipArea = shipArea;
	}

	public String getShipZipCode() {
		return this.shipZipCode;
	}

	public void setShipZipCode(String shipZipCode) {
		this.shipZipCode = shipZipCode;
	}

	public String getShipAddress() {
		return this.shipAddress;
	}

	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}

	public String getDefaultWarehouseCode() {
		return this.defaultWarehouseCode;
	}

	public void setDefaultWarehouseCode(String defaultWarehouseCode) {
		this.defaultWarehouseCode = defaultWarehouseCode;
	}

	public String getDefaultWarehouseName() {
		return defaultWarehouseName;
	}

	public void setDefaultWarehouseName(String defaultWarehouseName) {
		this.defaultWarehouseName = defaultWarehouseName;
	}

	public String getPromotionCode() {
		return this.promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public Double getTotalOriginalSalesAmount() {
		return this.totalOriginalSalesAmount;
	}

	public void setTotalOriginalSalesAmount(Double totalOriginalSalesAmount) {
		this.totalOriginalSalesAmount = totalOriginalSalesAmount;
	}

	public Double getTotalActualSalesAmount() {
		return this.totalActualSalesAmount;
	}

	public void setTotalActualSalesAmount(Double totalActualSalesAmount) {
		this.totalActualSalesAmount = totalActualSalesAmount;
	}

	public Double getTotalOriginalShipAmount() {
		return totalOriginalShipAmount;
	}

	public void setTotalOriginalShipAmount(Double totalOriginalShipAmount) {
		this.totalOriginalShipAmount = totalOriginalShipAmount;
	}

	public Double getTotalActualShipAmount() {
		return totalActualShipAmount;
	}

	public void setTotalActualShipAmount(Double totalActualShipAmount) {
		this.totalActualShipAmount = totalActualShipAmount;
	}

	public Double getTotalOriginalReturnAmount() {
		return totalOriginalReturnAmount;
	}

	public void setTotalOriginalReturnAmount(Double totalOriginalReturnAmount) {
		this.totalOriginalReturnAmount = totalOriginalReturnAmount;
	}

	public Double getTotalActualReturnAmount() {
		return totalActualReturnAmount;
	}

	public void setTotalActualReturnAmount(Double totalActualReturnAmount) {
		this.totalActualReturnAmount = totalActualReturnAmount;
	}

	public Double getTotalDeductionAmount() {
		return totalDeductionAmount;
	}

	public void setTotalDeductionAmount(Double totalDeductionAmount) {
		this.totalDeductionAmount = totalDeductionAmount;
	}

	public Double getTotalOtherExpense() {
		return totalOtherExpense;
	}

	public void setTotalOtherExpense(Double totalOtherExpense) {
		this.totalOtherExpense = totalOtherExpense;
	}

	public Double getTotalNoneTaxShipAmount() {
		return totalNoneTaxShipAmount;
	}

	public void setTotalNoneTaxShipAmount(Double totalNoneTaxShipAmount) {
		this.totalNoneTaxShipAmount = totalNoneTaxShipAmount;
	}

	public Double getTotalItemQuantity() {
		return totalItemQuantity;
	}

	public void setTotalItemQuantity(Double totalItemQuantity) {
		this.totalItemQuantity = totalItemQuantity;
	}

	public Double getDiscountRate() {
		return this.discountRate;
	}

	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}

	public String getPosMachineCode() {
		return this.posMachineCode;
	}

	public void setPosMachineCode(String posMachineCode) {
		this.posMachineCode = posMachineCode;
	}

	public String getMemberCardNo() {
		return this.memberCardNo;
	}

	public void setMemberCardNo(String memberCardNo) {
		this.memberCardNo = memberCardNo;
	}

	public String getNationalityCode() {
		return this.nationalityCode;
	}

	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = nationalityCode;
	}

	public String getCasherCode() {
		return casherCode;
	}

	public void setCasherCode(String casherCode) {
		this.casherCode = casherCode;
	}

	public String getTransactionSeqNo() {
		return transactionSeqNo;
	}

	public void setTransactionSeqNo(String transactionSeqNo) {
		this.transactionSeqNo = transactionSeqNo;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public String getSalesInvoicePage() {
		return salesInvoicePage;
	}

	public void setSalesInvoicePage(String salesInvoicePage) {
		this.salesInvoicePage = salesInvoicePage;
	}

	public String getFlightNo() {
		return this.flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public String getPassportNo() {
		return this.passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getLadingNo() {
		return ladingNo;
	}

	public void setLadingNo(String ladingNo) {
		this.ladingNo = ladingNo;
	}

	public String getSufficientQuantityDelivery() {
		return sufficientQuantityDelivery;
	}

	public void setSufficientQuantityDelivery(String sufficientQuantityDelivery) {
		this.sufficientQuantityDelivery = sufficientQuantityDelivery;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoicePrintLotNo() {
		return invoicePrintLotNo;
	}

	public void setInvoicePrintLotNo(String invoicePrintLotNo) {
		this.invoicePrintLotNo = invoicePrintLotNo;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getReserve1() {
		return this.reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return this.reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return this.reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getReserve4() {
		return this.reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	public String getReserve5() {
		return this.reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getHomeDelivery() {
		return homeDelivery;
	}

	public void setHomeDelivery(String homeDelivery) {
		this.homeDelivery = homeDelivery;
	}

	public String getPaymentCategory() {
		return paymentCategory;
	}

	public void setPaymentCategory(String paymentCategory) {
		this.paymentCategory = paymentCategory;
	}

	public String getAttachedInvoice() {
		return attachedInvoice;
	}

	public void setAttachedInvoice(String attachedInvoice) {
		this.attachedInvoice = attachedInvoice;
	}

	public List<ImDeliveryLine> getImDeliveryLines() {
		return imDeliveryLines;
	}

	public void setImDeliveryLines(List<ImDeliveryLine> imDeliveryLines) {
		this.imDeliveryLines = imDeliveryLines;
	}

	public String getExportDeclNo() {
		return exportDeclNo;
	}

	public void setExportDeclNo(String exportDeclNo) {
		this.exportDeclNo = exportDeclNo;
	}

	public Date getExportDeclDate() {
		return exportDeclDate;
	}

	public void setExportDeclDate(Date exportDeclDate) {
		this.exportDeclDate = exportDeclDate;
	}

	public String getExportDeclType() {
		return exportDeclType;
	}

	public void setExportDeclType(String exportDeclType) {
		this.exportDeclType = exportDeclType;
	}

	public Double getExportBoxAmount() {
		return exportBoxAmount;
	}

	public void setExportBoxAmount(Double exportBoxAmount) {
		this.exportBoxAmount = exportBoxAmount;
	}

	public String getExportTaxType() {
		return exportTaxType;
	}

	public void setExportTaxType(String exportTaxType) {
		this.exportTaxType = exportTaxType;
	}

	public Double getExportExchangeRate() {
		return exportExchangeRate;
	}

	public void setExportExchangeRate(Double exportExchangeRate) {
		this.exportExchangeRate = exportExchangeRate;
	}

	public Double getOriginalTotalFrnSalesAmt() {
		return originalTotalFrnSalesAmt;
	}

	public void setOriginalTotalFrnSalesAmt(Double originalTotalFrnSalesAmt) {
		this.originalTotalFrnSalesAmt = originalTotalFrnSalesAmt;
	}

	public Double getActualTotalFrnSalesAmt() {
		return actualTotalFrnSalesAmt;
	}

	public void setActualTotalFrnSalesAmt(Double actualTotalFrnSalesAmt) {
		this.actualTotalFrnSalesAmt = actualTotalFrnSalesAmt;
	}

	public Double getOriginalTotalFrnShipAmt() {
		return originalTotalFrnShipAmt;
	}

	public void setOriginalTotalFrnShipAmt(Double originalTotalFrnShipAmt) {
		this.originalTotalFrnShipAmt = originalTotalFrnShipAmt;
	}

	public Double getActualTotalFrnShipAmt() {
		return actualTotalFrnShipAmt;
	}

	public void setActualTotalFrnShipAmt(Double actualTotalFrnShipAmt) {
		this.actualTotalFrnShipAmt = actualTotalFrnShipAmt;
	}

	public String getLatestExportDeclNo() {
		return latestExportDeclNo;
	}

	public void setLatestExportDeclNo(String latestExportDeclNo) {
		this.latestExportDeclNo = latestExportDeclNo;
	}

	public Date getLatestExportDeclDate() {
		return latestExportDeclDate;
	}

	public void setLatestExportDeclDate(Date latestExportDeclDate) {
		this.latestExportDeclDate = latestExportDeclDate;
	}

	public String getLatestExportDeclType() {
		return latestExportDeclType;
	}

	public void setLatestExportDeclType(String latestExportDeclType) {
		this.latestExportDeclType = latestExportDeclType;
	}

	public Double getExportCommissionRate() {
		return exportCommissionRate;
	}

	public void setExportCommissionRate(Double exportCommissionRate) {
		this.exportCommissionRate = exportCommissionRate;
	}

	public Double getExpenseForeignAmount() {
		return expenseForeignAmount;
	}

	public void setExpenseForeignAmount(Double expenseForeignAmount) {
		this.expenseForeignAmount = expenseForeignAmount;
	}

	public Double getExpenseLocalAmount() {
		return expenseLocalAmount;
	}

	public void setExpenseLocalAmount(Double expenseLocalAmount) {
		this.expenseLocalAmount = expenseLocalAmount;
	}

	public Double getExportExpense() {
		return exportExpense;
	}

	public void setExportExpense(Double exportExpense) {
		this.exportExpense = exportExpense;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getExportDeclNoLog() {
		return exportDeclNoLog;
	}

	public void setExportDeclNoLog(String exportDeclNoLog) {
		this.exportDeclNoLog = exportDeclNoLog;
	}

	public String getOrderDiscountType() {
		return orderDiscountType;
	}

	public void setOrderDiscountType(String orderDiscountType) {
		this.orderDiscountType = orderDiscountType;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getIslandsCode() {
		return islandsCode;
	}

	public void setIslandsCode(String islandsCode) {
		this.islandsCode = islandsCode;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getWarehouseStatus() {
		return warehouseStatus;
	}

	public void setWarehouseStatus(String warehouseStatus) {
		this.warehouseStatus = warehouseStatus;
	} 
	
	public Double getDisplayPer() {
		return displayPer;
	}

	public void setDisplayPer(Double displayPer) {
		this.displayPer = displayPer;
	}
	
	public Double getBaselPer() {
		return baselPer;
	}

	public void setBaselPer(Double baselPer) {
		this.baselPer = baselPer;
	}
	
	public Double getBaselPer2() {
		return baselPer2;
	}

	public void setBaselPer2(Double baselPer2) {
		this.baselPer2 = baselPer2;
	}
	
	public Double getSalePer() {
		return salePer;
	}

	public void setSalePer(Double salePer) {
		this.salePer = salePer;
	}
	
	public Double getDisplayAmount() {
		return displayAmount;
	}

	public void setDisplayAmount(Double displayAmount) {
		this.displayAmount = displayAmount;
	}
	
	public Double getBaselAmount() {
		return baselAmount;
	}

	public void setBaselAmount(Double baselAmount) {
		this.baselAmount = baselAmount;
	}
	
	public Double getBaselAmount2() {
		return baselAmount2;
	}

	public void setBaselAmount2(Double baselAmount2) {
		this.baselAmount2 = baselAmount2;
	}

	public Double getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(Double saleAmount) {
		this.saleAmount = saleAmount;
	}

	public String getFlightNoSales() {
		return flightNoSales;
	}

	public void setFlightNoSales(String flightNoSales) {
		this.flightNoSales = flightNoSales;
	}

	public String getPassportNoSales() {
		return passportNoSales;
	}

	public void setPassportNoSales(String passportNoSales) {
		this.passportNoSales = passportNoSales;
	}

	public String getTransactionMinute() {
		return transactionMinute;
	}

	public void setTransactionMinute(String transactionMinute) {
		this.transactionMinute = transactionMinute;
	}

	public String getTransactionSecond() {
		return transactionSecond;
	}

	public void setTransactionSecond(String transactionSecond) {
		this.transactionSecond = transactionSecond;
	}

	public String getCustomsStatus() {
		return customsStatus;
	}

	public void setCustomsStatus(String customsStatus) {
		this.customsStatus = customsStatus;
	}

	public String getTranAllowUpload() {
		return tranAllowUpload;
	}

	public void setTranAllowUpload(String tranAllowUpload) {
		this.tranAllowUpload = tranAllowUpload;
	}

	public String getTranRecordStatus() {
		return tranRecordStatus;
	}

	public void setTranRecordStatus(String tranRecordStatus) {
		this.tranRecordStatus = tranRecordStatus;
	}

	public String getcStatus() {
		return cStatus;
	}

	public void setcStatus(String status) {
		cStatus = status;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getCustomsNo() {
		return customsNo;
	}

	public void setCustomsNo(String customsNo) {
		this.customsNo = customsNo;
	}

}