package tw.com.tm.erp.hbm.bean;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * SoSalesOrderHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoSalesOrderHead implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8971175682859679116L;
	// Fields
	private Long headId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private Date salesOrderDate;
	private String customerCode;
	private String customerName;
	private String customerType;
	private String customerPoNo;
	private String quotationCode;
	private String paymentTermCode;
	private String countryCode;
	private String currencyCode;
	private String shopCode;
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
	private Date scheduleCollectionDate;
	private String invoiceCity;
	private String invoiceArea;
	private String invoiceZipCode;
	private String invoiceAddress;
	private String shipCity;
	private String shipArea;
	private String shipZipCode;
	private String shipAddress;
	private Date scheduleShipDate;
	private String defaultWarehouseCode;
	private String promotionCode;
	private String promotionName;
	private Double totalOriginalSalesAmount;
	private Double totalActualSalesAmount;
	private Double totalDeductionAmount;
	private Double totalOtherExpense;
	private Double totalNoneTaxSalesAmount;
	private Double totalItemQuantity;
	private Double discountRate;
	private String posMachineCode;
	private String memberCardNo;
	private String nationalityCode;
	private String casherCode;
	private String casherName;
	private String transactionSeqNo;
	private Date departureDate;
	private String salesInvoicePage;
	private String flightNo;
	private String passportNo;
	private String ladingNo;
	private String sufficientQuantityDelivery;
	private String remark1;
	private String remark2;
	private String status;
	private String statusName;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String vipTypeCode;
	private String vipPromotionCode;
	private String exportDeclNo;
	private Date exportDeclDate;
	private String exportDeclType;
	private Double exportBoxAmount;
	private String exportTaxType;
	private Double exportCommissionRate;
	private Double exportExchangeRate;
	private String homeDelivery;
	private String paymentCategory;
	private String attachedInvoice;
	private Double exportBoxQty;
	private Double exportTotalWeight;
	private Double exportExpense;
	private String tradeTerm;
	private String manualOrderNo;
	private String salesType;
	private Double originalTotalFrnSalesAmt;
	private Double actualTotalFrnSalesAmt;
	private String latestExportDeclNo;
	private Date latestExportDeclDate;
	private String latestExportDeclType;
	private Double expenseForeignAmount;
	private Double expenseLocalAmount;
	private String transactionTime;
	private String verificationStatus;
	private String exportDeclNoLog;
	private String orderDiscountType;
	private String itemCategory;
	private String islandsCode;
	private String storeCode;
	private String transactionId;
	private String buyerId;
	private String warehouseStatus;
	private Long processId;//for ceap防重送
	private String flightNoSales;
	private String passportNoSales;
	private List<SoSalesOrderItem> soSalesOrderItems = new ArrayList(0);
	private List<SoSalesOrderPayment> soSalesOrderPayments = new ArrayList(0);
	private String transactionMinute;
	private String transactionSecond;
	
	private String tranRecordStatus;
	private String customsStatus;
	private String cStatus;
	private String tranAllowUpload;
	private String schedule;
	private String customsNo;
//	private Double unSettledCredit;
//	private Double settledCredit;
	private String eventCode;
	private Double totalBonusPointAmount;
	private String appCustomerCode;
	
	public SoSalesOrderHead(Long headId, String brandCode,
			String orderTypeCode, String orderNo, Date salesOrderDate,
			String customerCode, String customerName, String customerType,
			String customerPoNo, String quotationCode, String paymentTermCode,
			String countryCode, String currencyCode, String shopCode,
			String contactPerson, String contactTel, String receiver,
			String superintendentCode, String superintendentName,
			String invoiceTypeCode, String guiCode, String taxType,
			Double taxRate, Double taxAmount, Date scheduleCollectionDate,
			String invoiceCity, String invoiceArea, String invoiceZipCode,
			String invoiceAddress, String shipCity, String shipArea,
			String shipZipCode, String shipAddress, Date scheduleShipDate,
			String defaultWarehouseCode, String promotionCode,
			String promotionName, Double totalOriginalSalesAmount,
			Double totalActualSalesAmount, Double totalDeductionAmount,
			Double totalOtherExpense, Double totalNoneTaxSalesAmount,
			Double totalItemQuantity, Double discountRate,
			String posMachineCode, String memberCardNo, String nationalityCode,
			String casherCode, String casherName, String transactionSeqNo,
			Date departureDate, String salesInvoicePage, String flightNo,
			String passportNo, String ladingNo,
			String sufficientQuantityDelivery, String remark1, String remark2,
			String status, String statusName, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, String vipTypeCode, String vipPromotionCode,
			String exportDeclNo, Date exportDeclDate, String exportDeclType,
			Double exportBoxAmount, String exportTaxType,
			Double exportCommissionRate, Double exportExchangeRate,
			String homeDelivery, String paymentCategory,
			String attachedInvoice, Double exportBoxQty,
			Double exportTotalWeight, Double exportExpense, String tradeTerm,
			String manualOrderNo, String salesType,
			Double originalTotalFrnSalesAmt, Double actualTotalFrnSalesAmt,
			String latestExportDeclNo, Date latestExportDeclDate,
			String latestExportDeclType, Double expenseForeignAmount,
			Double expenseLocalAmount, String transactionTime,
			String verificationStatus, String exportDeclNoLog,
			String orderDiscountType, String itemCategory, String islandsCode,
			String storeCode, String transactionId, String buyerId,
			String warehouseStatus, Long processId, String flightNoSales,
			String passportNoSales, List<SoSalesOrderItem> soSalesOrderItems,
			List<SoSalesOrderPayment> soSalesOrderPayments,
			String transactionMinute, String transactionSecond,
			String tranRecordStatus, String customsStatus, String status2,
			String tranAllowUpload, String schedule, String customsNo,
			Double totalBonusPointAmount, String appCustomerCode) {
		super();
		this.headId = headId;
		this.brandCode = brandCode;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.salesOrderDate = salesOrderDate;
		this.customerCode = customerCode;
		this.customerName = customerName;
		this.customerType = customerType;
		this.customerPoNo = customerPoNo;
		this.quotationCode = quotationCode;
		this.paymentTermCode = paymentTermCode;
		this.countryCode = countryCode;
		this.currencyCode = currencyCode;
		this.shopCode = shopCode;
		this.contactPerson = contactPerson;
		this.contactTel = contactTel;
		this.receiver = receiver;
		this.superintendentCode = superintendentCode;
		this.superintendentName = superintendentName;
		this.invoiceTypeCode = invoiceTypeCode;
		this.guiCode = guiCode;
		this.taxType = taxType;
		this.taxRate = taxRate;
		this.taxAmount = taxAmount;
		this.scheduleCollectionDate = scheduleCollectionDate;
		this.invoiceCity = invoiceCity;
		this.invoiceArea = invoiceArea;
		this.invoiceZipCode = invoiceZipCode;
		this.invoiceAddress = invoiceAddress;
		this.shipCity = shipCity;
		this.shipArea = shipArea;
		this.shipZipCode = shipZipCode;
		this.shipAddress = shipAddress;
		this.scheduleShipDate = scheduleShipDate;
		this.defaultWarehouseCode = defaultWarehouseCode;
		this.promotionCode = promotionCode;
		this.promotionName = promotionName;
		this.totalOriginalSalesAmount = totalOriginalSalesAmount;
		this.totalActualSalesAmount = totalActualSalesAmount;
		this.totalDeductionAmount = totalDeductionAmount;
		this.totalOtherExpense = totalOtherExpense;
		this.totalNoneTaxSalesAmount = totalNoneTaxSalesAmount;
		this.totalItemQuantity = totalItemQuantity;
		this.discountRate = discountRate;
		this.posMachineCode = posMachineCode;
		this.memberCardNo = memberCardNo;
		this.nationalityCode = nationalityCode;
		this.casherCode = casherCode;
		this.casherName = casherName;
		this.transactionSeqNo = transactionSeqNo;
		this.departureDate = departureDate;
		this.salesInvoicePage = salesInvoicePage;
		this.flightNo = flightNo;
		this.passportNo = passportNo;
		this.ladingNo = ladingNo;
		this.sufficientQuantityDelivery = sufficientQuantityDelivery;
		this.remark1 = remark1;
		this.remark2 = remark2;
		this.status = status;
		this.statusName = statusName;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.vipTypeCode = vipTypeCode;
		this.vipPromotionCode = vipPromotionCode;
		this.exportDeclNo = exportDeclNo;
		this.exportDeclDate = exportDeclDate;
		this.exportDeclType = exportDeclType;
		this.exportBoxAmount = exportBoxAmount;
		this.exportTaxType = exportTaxType;
		this.exportCommissionRate = exportCommissionRate;
		this.exportExchangeRate = exportExchangeRate;
		this.homeDelivery = homeDelivery;
		this.paymentCategory = paymentCategory;
		this.attachedInvoice = attachedInvoice;
		this.exportBoxQty = exportBoxQty;
		this.exportTotalWeight = exportTotalWeight;
		this.exportExpense = exportExpense;
		this.tradeTerm = tradeTerm;
		this.manualOrderNo = manualOrderNo;
		this.salesType = salesType;
		this.originalTotalFrnSalesAmt = originalTotalFrnSalesAmt;
		this.actualTotalFrnSalesAmt = actualTotalFrnSalesAmt;
		this.latestExportDeclNo = latestExportDeclNo;
		this.latestExportDeclDate = latestExportDeclDate;
		this.latestExportDeclType = latestExportDeclType;
		this.expenseForeignAmount = expenseForeignAmount;
		this.expenseLocalAmount = expenseLocalAmount;
		this.transactionTime = transactionTime;
		this.verificationStatus = verificationStatus;
		this.exportDeclNoLog = exportDeclNoLog;
		this.orderDiscountType = orderDiscountType;
		this.itemCategory = itemCategory;
		this.islandsCode = islandsCode;
		this.storeCode = storeCode;
		this.transactionId = transactionId;
		this.buyerId = buyerId;
		this.warehouseStatus = warehouseStatus;
		this.processId = processId;
		this.flightNoSales = flightNoSales;
		this.passportNoSales = passportNoSales;
		this.soSalesOrderItems = soSalesOrderItems;
		this.soSalesOrderPayments = soSalesOrderPayments;
		this.transactionMinute = transactionMinute;
		this.transactionSecond = transactionSecond;
		this.tranRecordStatus = tranRecordStatus;
		this.customsStatus = customsStatus;
		cStatus = status2;
		this.tranAllowUpload = tranAllowUpload;
		this.schedule = schedule;
		this.customsNo = customsNo;
		this.totalBonusPointAmount = totalBonusPointAmount;
		this.appCustomerCode = appCustomerCode;
	}

	public Double getTotalBonusPointAmount() {
		return totalBonusPointAmount;
	}

	public void setTotalBonusPointAmount(Double totalBonusPointAmount) {
		this.totalBonusPointAmount = totalBonusPointAmount;
	}

	public String getAppCustomerCode() {
		return appCustomerCode;
	}

	public void setAppCustomerCode(String appCustomerCode) {
		this.appCustomerCode = appCustomerCode;
	}

	public String getTranAllowUpload() {
		return tranAllowUpload;
	}

	public void setTranAllowUpload(String tranAllowUpload) {
		this.tranAllowUpload = tranAllowUpload;
	}

	// Constructors
	/** default constructor */
	public SoSalesOrderHead() {
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

	public Date getSalesOrderDate() {
		return this.salesOrderDate;
	}

	public void setSalesOrderDate(Date salesOrderDate) {
		this.salesOrderDate = salesOrderDate;
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

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
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

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getShopCode() {
		return this.shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
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
		return this.taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
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

	public Date getScheduleShipDate() {
		return this.scheduleShipDate;
	}

	public void setScheduleShipDate(Date scheduleShipDate) {
		this.scheduleShipDate = scheduleShipDate;
	}

	public String getDefaultWarehouseCode() {
		return this.defaultWarehouseCode;
	}

	public void setDefaultWarehouseCode(String defaultWarehouseCode) {
		this.defaultWarehouseCode = defaultWarehouseCode;
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

	public Double getTotalNoneTaxSalesAmount() {
		return totalNoneTaxSalesAmount;
	}

	public void setTotalNoneTaxSalesAmount(Double totalNoneTaxSalesAmount) {
		this.totalNoneTaxSalesAmount = totalNoneTaxSalesAmount;
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

	public String getCasherName() {
		return casherName;
	}

	public void setCasherName(String casherName) {
		this.casherName = casherName;
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

	public String getRemark1() {
		return this.remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return this.remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getVipTypeCode() {
		return vipTypeCode;
	}

	public void setVipTypeCode(String vipTypeCode) {
		this.vipTypeCode = vipTypeCode;
	}

	public String getVipPromotionCode() {
		return vipPromotionCode;
	}

	public void setVipPromotionCode(String vipPromotionCode) {
		this.vipPromotionCode = vipPromotionCode;
	}

	public List<SoSalesOrderItem> getSoSalesOrderItems() {
		return this.soSalesOrderItems;
	}

	public void setSoSalesOrderItems(List<SoSalesOrderItem> soSalesOrderItems) {
		this.soSalesOrderItems = soSalesOrderItems;
	}

	public List<SoSalesOrderPayment> getSoSalesOrderPayments() {
		return soSalesOrderPayments;
	}

	public void setSoSalesOrderPayments(
			List<SoSalesOrderPayment> soSalesOrderPayments) {
		this.soSalesOrderPayments = soSalesOrderPayments;
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

	public Double getExportCommissionRate() {
		return exportCommissionRate;
	}

	public void setExportCommissionRate(Double exportCommissionRate) {
		this.exportCommissionRate = exportCommissionRate;
	}

	public Double getExportExchangeRate() {
		return exportExchangeRate;
	}

	public void setExportExchangeRate(Double exportExchangeRate) {
		this.exportExchangeRate = exportExchangeRate;
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

	public Double getExportBoxQty() {
		return exportBoxQty;
	}

	public void setExportBoxQty(Double exportBoxQty) {
		this.exportBoxQty = exportBoxQty;
	}

	public Double getExportTotalWeight() {
		return exportTotalWeight;
	}

	public void setExportTotalWeight(Double exportTotalWeight) {
		this.exportTotalWeight = exportTotalWeight;
	}

	public Double getExportExpense() {
		return exportExpense;
	}

	public void setExportExpense(Double exportExpense) {
		this.exportExpense = exportExpense;
	}

	public String getTradeTerm() {
		return tradeTerm;
	}

	public void setTradeTerm(String tradeTerm) {
		this.tradeTerm = tradeTerm;
	}

	public String getManualOrderNo() {
		return manualOrderNo;
	}

	public void setManualOrderNo(String manualOrderNo) {
		this.manualOrderNo = manualOrderNo;
	}

	public String getSalesType() {
		return salesType;
	}

	public void setSalesType(String salesType) {
		this.salesType = salesType;
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

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
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

	public Long getProcessId() {
	    return processId;
	}

	public void setProcessId(Long processId) {
	    this.processId = processId;
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

	public String getTranRecordStatus() {
		return tranRecordStatus;
	}

	public void setTranRecordStatus(String tranRecordStatus) {
		this.tranRecordStatus = tranRecordStatus;
	}

	public String getCustomsStatus() {
		return customsStatus;
	}

	public void setCustomsStatus(String customsStatus) {
		this.customsStatus = customsStatus;
	}

	public String getcStatus() {
		return cStatus;
	}

	public void setcStatus(String cStatus) {
		cStatus = cStatus;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
/*
	public Double getUnSettledCredit() {
		return unSettledCredit;
	}

	public void setUnSettledCredit(Double unSettledCredit) {
		this.unSettledCredit = unSettledCredit;
	}

	public Double getSettledCredit() {
		return settledCredit;
	}

	public void setSettledCredit(Double settledCredit) {
		this.settledCredit = settledCredit;
	}
*/	
	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	
	public String getCustomsNo() {
		return customsNo;
	}

	public void setCustomsNo(String customsNo) {
		this.customsNo = customsNo;
	}
}