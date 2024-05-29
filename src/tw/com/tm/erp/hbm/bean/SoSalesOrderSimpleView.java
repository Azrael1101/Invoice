package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoSalesOrderSimpleView entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoSalesOrderSimpleView implements java.io.Serializable {

	
	

	private SoSalesOrderSimpleViewId id;
	private Date salesOrderDate;
	private String salesOrderYear;
	private String salesOrderMonth;
	private String salesOrderDay;
	private String customerCode;
	private String customerPoNo;
	private String quotationCode;
	private String paymentTermCode;
	private String countryCode;
	private String currencyCode;
	private String shopCode;
	private String contactPerson;
	private String superintendentCode;
	private String invoiceTypeCode;
	private String guiCode;
	private Double totalTaxAmount;
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
	private Double totalOriginalSalesAmount;
	private Double totalActualSalesAmount;
	private String posMachineCode;
	private String memberCardNo;
	private String nationalityCode;
	private String flightNo;
	private String passportNo;
	private String remark1;
	private String remark2;
	private String headStatus;
	private Long deliveryId;
	private String itemCode;
	private String warehouseCode;
	private Double originalUnitPrice;
	private Double originalSalesAmount;
	private String promotionCode;
	private String discountType;
	private Double discount;
	private Double actualUnitPrice;
	private Double actualSalesAmount;
	private Double discountRate;
	private Date shippedDate;
	private Double shippedQuantity;
	private String isTax;
	private String taxType;
	private Double taxRate;
	private Double taxAmount;
	private String depositCode;
	private String isUseDeposit;
	private String watchSerialNo;
	private String lineStatus;
	private String transactionTime;
	private String itemCName;
	private String customerName;
	private String superintendentName;
	
	public SoSalesOrderSimpleView() {

	}
	/**
	 * @param id
	 * @param salesOrderDate
	 * @param salesOrderYear
	 * @param salesOrderMonth
	 * @param salesOrderDay
	 * @param customerCode
	 * @param customerPoNo
	 * @param quotationCode
	 * @param paymentTermCode
	 * @param countryCode
	 * @param currencyCode
	 * @param shopCode
	 * @param contactPerson
	 * @param superintendentCode
	 * @param invoiceTypeCode
	 * @param guiCode
	 * @param totalTaxAmount
	 * @param scheduleCollectionDate
	 * @param invoiceCity
	 * @param invoiceArea
	 * @param invoiceZipCode
	 * @param invoiceAddress
	 * @param shipCity
	 * @param shipArea
	 * @param shipZipCode
	 * @param shipAddress
	 * @param scheduleShipDate
	 * @param totalOriginalSalesAmount
	 * @param totalActualSalesAmount
	 * @param posMachineCode
	 * @param memberCardNo
	 * @param nationalityCode
	 * @param flightNo
	 * @param passportNo
	 * @param remark1
	 * @param remark2
	 * @param headStatus
	 * @param deliveryId
	 * @param itemCode
	 * @param warehouseCode
	 * @param originalUnitPrice
	 * @param originalSalesAmount
	 * @param promotionCode
	 * @param discountType
	 * @param discount
	 * @param actualUnitPrice
	 * @param actualSalesAmount
	 * @param discountRate
	 * @param shippedDate
	 * @param shippedQuantity
	 * @param isTax
	 * @param taxType
	 * @param taxRate
	 * @param taxAmount
	 * @param depositCode
	 * @param isUseDeposit
	 * @param watchSerialNo
	 * @param lineStatus
	 * @param transactionTime
	 * @param itemName
	 * @param customerName
	 */
	public SoSalesOrderSimpleView(SoSalesOrderSimpleViewId id,
			Date salesOrderDate, String salesOrderYear, String salesOrderMonth,
			String salesOrderDay, String customerCode, String customerPoNo,
			String quotationCode, String paymentTermCode, String countryCode,
			String currencyCode, String shopCode, String contactPerson,
			String superintendentCode, String invoiceTypeCode, String guiCode,
			Double totalTaxAmount, Date scheduleCollectionDate,
			String invoiceCity, String invoiceArea, String invoiceZipCode,
			String invoiceAddress, String shipCity, String shipArea,
			String shipZipCode, String shipAddress, Date scheduleShipDate,
			Double totalOriginalSalesAmount, Double totalActualSalesAmount,
			String posMachineCode, String memberCardNo, String nationalityCode,
			String flightNo, String passportNo, String remark1, String remark2,
			String headStatus, Long deliveryId, String itemCode,
			String warehouseCode, Double originalUnitPrice,
			Double originalSalesAmount, String promotionCode,
			String discountType, Double discount, Double actualUnitPrice,
			Double actualSalesAmount, Double discountRate, Date shippedDate,
			Double shippedQuantity, String isTax, String taxType,
			Double taxRate, Double taxAmount, String depositCode,
			String isUseDeposit, String watchSerialNo, String lineStatus,
			String transactionTime, String itemCName, String customerName, String superintendentName) {
		this.id = id;
		this.salesOrderDate = salesOrderDate;
		this.salesOrderYear = salesOrderYear;
		this.salesOrderMonth = salesOrderMonth;
		this.salesOrderDay = salesOrderDay;
		this.customerCode = customerCode;
		this.customerPoNo = customerPoNo;
		this.quotationCode = quotationCode;
		this.paymentTermCode = paymentTermCode;
		this.countryCode = countryCode;
		this.currencyCode = currencyCode;
		this.shopCode = shopCode;
		this.contactPerson = contactPerson;
		this.superintendentCode = superintendentCode;
		this.invoiceTypeCode = invoiceTypeCode;
		this.guiCode = guiCode;
		this.totalTaxAmount = totalTaxAmount;
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
		this.totalOriginalSalesAmount = totalOriginalSalesAmount;
		this.totalActualSalesAmount = totalActualSalesAmount;
		this.posMachineCode = posMachineCode;
		this.memberCardNo = memberCardNo;
		this.nationalityCode = nationalityCode;
		this.flightNo = flightNo;
		this.passportNo = passportNo;
		this.remark1 = remark1;
		this.remark2 = remark2;
		this.headStatus = headStatus;
		this.deliveryId = deliveryId;
		this.itemCode = itemCode;
		this.warehouseCode = warehouseCode;
		this.originalUnitPrice = originalUnitPrice;
		this.originalSalesAmount = originalSalesAmount;
		this.promotionCode = promotionCode;
		this.discountType = discountType;
		this.discount = discount;
		this.actualUnitPrice = actualUnitPrice;
		this.actualSalesAmount = actualSalesAmount;
		this.discountRate = discountRate;
		this.shippedDate = shippedDate;
		this.shippedQuantity = shippedQuantity;
		this.isTax = isTax;
		this.taxType = taxType;
		this.taxRate = taxRate;
		this.taxAmount = taxAmount;
		this.depositCode = depositCode;
		this.isUseDeposit = isUseDeposit;
		this.watchSerialNo = watchSerialNo;
		this.lineStatus = lineStatus;
		this.transactionTime = transactionTime;
		this.itemCName = itemCName;
		this.customerName = customerName;
		this.superintendentName = superintendentName;
	}
	
	/**
	 * @return the id
	 */
	public SoSalesOrderSimpleViewId getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(SoSalesOrderSimpleViewId id) {
		this.id = id;
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
	 * @return the salesOrderYear
	 */
	public String getSalesOrderYear() {
		return salesOrderYear;
	}
	/**
	 * @param salesOrderYear the salesOrderYear to set
	 */
	public void setSalesOrderYear(String salesOrderYear) {
		this.salesOrderYear = salesOrderYear;
	}
	/**
	 * @return the salesOrderMonth
	 */
	public String getSalesOrderMonth() {
		return salesOrderMonth;
	}
	/**
	 * @param salesOrderMonth the salesOrderMonth to set
	 */
	public void setSalesOrderMonth(String salesOrderMonth) {
		this.salesOrderMonth = salesOrderMonth;
	}
	/**
	 * @return the salesOrderDay
	 */
	public String getSalesOrderDay() {
		return salesOrderDay;
	}
	/**
	 * @param salesOrderDay the salesOrderDay to set
	 */
	public void setSalesOrderDay(String salesOrderDay) {
		this.salesOrderDay = salesOrderDay;
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
	 * @return the quotationCode
	 */
	public String getQuotationCode() {
		return quotationCode;
	}
	/**
	 * @param quotationCode the quotationCode to set
	 */
	public void setQuotationCode(String quotationCode) {
		this.quotationCode = quotationCode;
	}
	/**
	 * @return the paymentTermCode
	 */
	public String getPaymentTermCode() {
		return paymentTermCode;
	}
	/**
	 * @param paymentTermCode the paymentTermCode to set
	 */
	public void setPaymentTermCode(String paymentTermCode) {
		this.paymentTermCode = paymentTermCode;
	}
	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}
	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
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
	 * @return the contactPerson
	 */
	public String getContactPerson() {
		return contactPerson;
	}
	/**
	 * @param contactPerson the contactPerson to set
	 */
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	/**
	 * @return the superintendentCode
	 */
	public String getSuperintendentCode() {
		return superintendentCode;
	}
	/**
	 * @param superintendentCode the superintendentCode to set
	 */
	public void setSuperintendentCode(String superintendentCode) {
		this.superintendentCode = superintendentCode;
	}
	/**
	 * @return the invoiceTypeCode
	 */
	public String getInvoiceTypeCode() {
		return invoiceTypeCode;
	}
	/**
	 * @param invoiceTypeCode the invoiceTypeCode to set
	 */
	public void setInvoiceTypeCode(String invoiceTypeCode) {
		this.invoiceTypeCode = invoiceTypeCode;
	}
	/**
	 * @return the guiCode
	 */
	public String getGuiCode() {
		return guiCode;
	}
	/**
	 * @param guiCode the guiCode to set
	 */
	public void setGuiCode(String guiCode) {
		this.guiCode = guiCode;
	}
	/**
	 * @return the totalTaxAmount
	 */
	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}
	/**
	 * @param totalTaxAmount the totalTaxAmount to set
	 */
	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}
	/**
	 * @return the scheduleCollectionDate
	 */
	public Date getScheduleCollectionDate() {
		return scheduleCollectionDate;
	}
	/**
	 * @param scheduleCollectionDate the scheduleCollectionDate to set
	 */
	public void setScheduleCollectionDate(Date scheduleCollectionDate) {
		this.scheduleCollectionDate = scheduleCollectionDate;
	}
	/**
	 * @return the invoiceCity
	 */
	public String getInvoiceCity() {
		return invoiceCity;
	}
	/**
	 * @param invoiceCity the invoiceCity to set
	 */
	public void setInvoiceCity(String invoiceCity) {
		this.invoiceCity = invoiceCity;
	}
	/**
	 * @return the invoiceArea
	 */
	public String getInvoiceArea() {
		return invoiceArea;
	}
	/**
	 * @param invoiceArea the invoiceArea to set
	 */
	public void setInvoiceArea(String invoiceArea) {
		this.invoiceArea = invoiceArea;
	}
	/**
	 * @return the invoiceZipCode
	 */
	public String getInvoiceZipCode() {
		return invoiceZipCode;
	}
	/**
	 * @param invoiceZipCode the invoiceZipCode to set
	 */
	public void setInvoiceZipCode(String invoiceZipCode) {
		this.invoiceZipCode = invoiceZipCode;
	}
	/**
	 * @return the invoiceAddress
	 */
	public String getInvoiceAddress() {
		return invoiceAddress;
	}
	/**
	 * @param invoiceAddress the invoiceAddress to set
	 */
	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}
	/**
	 * @return the shipCity
	 */
	public String getShipCity() {
		return shipCity;
	}
	/**
	 * @param shipCity the shipCity to set
	 */
	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}
	/**
	 * @return the shipArea
	 */
	public String getShipArea() {
		return shipArea;
	}
	/**
	 * @param shipArea the shipArea to set
	 */
	public void setShipArea(String shipArea) {
		this.shipArea = shipArea;
	}
	/**
	 * @return the shipZipCode
	 */
	public String getShipZipCode() {
		return shipZipCode;
	}
	/**
	 * @param shipZipCode the shipZipCode to set
	 */
	public void setShipZipCode(String shipZipCode) {
		this.shipZipCode = shipZipCode;
	}
	/**
	 * @return the shipAddress
	 */
	public String getShipAddress() {
		return shipAddress;
	}
	/**
	 * @param shipAddress the shipAddress to set
	 */
	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}
	/**
	 * @return the scheduleShipDate
	 */
	public Date getScheduleShipDate() {
		return scheduleShipDate;
	}
	/**
	 * @param scheduleShipDate the scheduleShipDate to set
	 */
	public void setScheduleShipDate(Date scheduleShipDate) {
		this.scheduleShipDate = scheduleShipDate;
	}
	/**
	 * @return the totalOriginalSalesAmount
	 */
	public Double getTotalOriginalSalesAmount() {
		return totalOriginalSalesAmount;
	}
	/**
	 * @param totalOriginalSalesAmount the totalOriginalSalesAmount to set
	 */
	public void setTotalOriginalSalesAmount(Double totalOriginalSalesAmount) {
		this.totalOriginalSalesAmount = totalOriginalSalesAmount;
	}
	/**
	 * @return the totalActualSalesAmount
	 */
	public Double getTotalActualSalesAmount() {
		return totalActualSalesAmount;
	}
	/**
	 * @param totalActualSalesAmount the totalActualSalesAmount to set
	 */
	public void setTotalActualSalesAmount(Double totalActualSalesAmount) {
		this.totalActualSalesAmount = totalActualSalesAmount;
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
	 * @return the memberCardNo
	 */
	public String getMemberCardNo() {
		return memberCardNo;
	}
	/**
	 * @param memberCardNo the memberCardNo to set
	 */
	public void setMemberCardNo(String memberCardNo) {
		this.memberCardNo = memberCardNo;
	}
	/**
	 * @return the nationalityCode
	 */
	public String getNationalityCode() {
		return nationalityCode;
	}
	/**
	 * @param nationalityCode the nationalityCode to set
	 */
	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = nationalityCode;
	}
	/**
	 * @return the flightNo
	 */
	public String getFlightNo() {
		return flightNo;
	}
	/**
	 * @param flightNo the flightNo to set
	 */
	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}
	/**
	 * @return the passportNo
	 */
	public String getPassportNo() {
		return passportNo;
	}
	/**
	 * @param passportNo the passportNo to set
	 */
	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}
	/**
	 * @return the remark1
	 */
	public String getRemark1() {
		return remark1;
	}
	/**
	 * @param remark1 the remark1 to set
	 */
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	/**
	 * @return the remark2
	 */
	public String getRemark2() {
		return remark2;
	}
	/**
	 * @param remark2 the remark2 to set
	 */
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	/**
	 * @return the headStatus
	 */
	public String getHeadStatus() {
		return headStatus;
	}
	/**
	 * @param headStatus the headStatus to set
	 */
	public void setHeadStatus(String headStatus) {
		this.headStatus = headStatus;
	}
	/**
	 * @return the deliveryId
	 */
	public Long getDeliveryId() {
		return deliveryId;
	}
	/**
	 * @param deliveryId the deliveryId to set
	 */
	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}
	/**
	 * @return the itemCode
	 */
	public String getItemCode() {
		return itemCode;
	}
	/**
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	/**
	 * @return the warehouseCode
	 */
	public String getWarehouseCode() {
		return warehouseCode;
	}
	/**
	 * @param warehouseCode the warehouseCode to set
	 */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	/**
	 * @return the originalUnitPrice
	 */
	public Double getOriginalUnitPrice() {
		return originalUnitPrice;
	}
	/**
	 * @param originalUnitPrice the originalUnitPrice to set
	 */
	public void setOriginalUnitPrice(Double originalUnitPrice) {
		this.originalUnitPrice = originalUnitPrice;
	}
	/**
	 * @return the originalSalesAmount
	 */
	public Double getOriginalSalesAmount() {
		return originalSalesAmount;
	}
	/**
	 * @param originalSalesAmount the originalSalesAmount to set
	 */
	public void setOriginalSalesAmount(Double originalSalesAmount) {
		this.originalSalesAmount = originalSalesAmount;
	}
	/**
	 * @return the promotionCode
	 */
	public String getPromotionCode() {
		return promotionCode;
	}
	/**
	 * @param promotionCode the promotionCode to set
	 */
	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}
	/**
	 * @return the discountType
	 */
	public String getDiscountType() {
		return discountType;
	}
	/**
	 * @param discountType the discountType to set
	 */
	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}
	/**
	 * @return the discount
	 */
	public Double getDiscount() {
		return discount;
	}
	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	/**
	 * @return the actualUnitPrice
	 */
	public Double getActualUnitPrice() {
		return actualUnitPrice;
	}
	/**
	 * @param actualUnitPrice the actualUnitPrice to set
	 */
	public void setActualUnitPrice(Double actualUnitPrice) {
		this.actualUnitPrice = actualUnitPrice;
	}
	/**
	 * @return the actualSalesAmount
	 */
	public Double getActualSalesAmount() {
		return actualSalesAmount;
	}
	/**
	 * @param actualSalesAmount the actualSalesAmount to set
	 */
	public void setActualSalesAmount(Double actualSalesAmount) {
		this.actualSalesAmount = actualSalesAmount;
	}
	/**
	 * @return the discountRate
	 */
	public Double getDiscountRate() {
		return discountRate;
	}
	/**
	 * @param discountRate the discountRate to set
	 */
	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}
	/**
	 * @return the shippedDate
	 */
	public Date getShippedDate() {
		return shippedDate;
	}
	/**
	 * @param shippedDate the shippedDate to set
	 */
	public void setShippedDate(Date shippedDate) {
		this.shippedDate = shippedDate;
	}
	/**
	 * @return the shippedQuantity
	 */
	public Double getShippedQuantity() {
		return shippedQuantity;
	}
	/**
	 * @param shippedQuantity the shippedQuantity to set
	 */
	public void setShippedQuantity(Double shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}
	/**
	 * @return the isTax
	 */
	public String getIsTax() {
		return isTax;
	}
	/**
	 * @param isTax the isTax to set
	 */
	public void setIsTax(String isTax) {
		this.isTax = isTax;
	}
	/**
	 * @return the taxType
	 */
	public String getTaxType() {
		return taxType;
	}
	/**
	 * @param taxType the taxType to set
	 */
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	/**
	 * @return the taxRate
	 */
	public Double getTaxRate() {
		return taxRate;
	}
	/**
	 * @param taxRate the taxRate to set
	 */
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	/**
	 * @return the taxAmount
	 */
	public Double getTaxAmount() {
		return taxAmount;
	}
	/**
	 * @param taxAmount the taxAmount to set
	 */
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	/**
	 * @return the depositCode
	 */
	public String getDepositCode() {
		return depositCode;
	}
	/**
	 * @param depositCode the depositCode to set
	 */
	public void setDepositCode(String depositCode) {
		this.depositCode = depositCode;
	}
	/**
	 * @return the isUseDeposit
	 */
	public String getIsUseDeposit() {
		return isUseDeposit;
	}
	/**
	 * @param isUseDeposit the isUseDeposit to set
	 */
	public void setIsUseDeposit(String isUseDeposit) {
		this.isUseDeposit = isUseDeposit;
	}
	/**
	 * @return the watchSerialNo
	 */
	public String getWatchSerialNo() {
		return watchSerialNo;
	}
	/**
	 * @param watchSerialNo the watchSerialNo to set
	 */
	public void setWatchSerialNo(String watchSerialNo) {
		this.watchSerialNo = watchSerialNo;
	}
	/**
	 * @return the lineStatus
	 */
	public String getLineStatus() {
		return lineStatus;
	}
	/**
	 * @param lineStatus the lineStatus to set
	 */
	public void setLineStatus(String lineStatus) {
		this.lineStatus = lineStatus;
	}
	/**
	 * @return the transactionTime
	 */
	public String getTransactionTime() {
		return transactionTime;
	}
	/**
	 * @param transactionTime the transactionTime to set
	 */
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	/**
	 * @return the itemName
	 */
	public String getItemCName() {
		return itemCName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemCName(String itemCName) {
		this.itemCName = itemCName;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the superintendentName
	 */
	public String getSuperintendentName() {
		return superintendentName;
	}
	/**
	 * @param superintendentName the superintendentName to set
	 */
	public void setSuperintendentName(String superintendentName) {
		this.superintendentName = superintendentName;
	}
	
	
}