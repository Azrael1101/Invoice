/**
 * 
 */
 
 //request bean struct
class reqBean {
	constructor(transaction,posTransaction,log){
		this.transaction = transaction
		this.posTransaction = posTransaction
		this.log = log
	};
}

class transaction {
	constructor(orderNo,orderTypeCode,brandCode,currencyCode,
	superintendentCode,scheduleShipDate,exportCommissionRate, exportExchangeRate, verificationStatus, createdBy,
	 creationDate, lastUpdatedBy , lastUpdateDate , schedule , discountRate , invoiceTypeCode
	,status,salesOrderDate,shopCode,totalActualSalesAmount,itemCode,quantity,discountType,discount,posPaymentType){
		this.status = status
		this.salesOrderDate = salesOrderDate
		this.shopCode = shopCode
		this.totalActualSalesAmount = totalActualSalesAmount
		this.itemCode = itemCode
		this.quantity = quantity
		this.discountType = discountType
		this.discount = discount
		this.posPaymentType = posPaymentType
		//==brian 20211202
		this.orderNo = orderNo
		this.orderTypeCode = orderTypeCode
		this.brandCode = brandCode
		this.currencyCode = currencyCode
		this.superintendentCode = superintendentCode
		this.scheduleShipDate = scheduleShipDate
		this.exportCommissionRate = exportCommissionRate
		this.exportExchangeRate = exportExchangeRate
		this.verificationStatus = verificationStatus
		this.createdBy = createdBy
	    this.creationDate = creationDate
		this.lastUpdatedBy = lastUpdatedBy
		this.lastUpdateDate = lastUpdateDate
		this.schedule = schedule
		this.discountRate = discountRate
		this.invoiceTypeCode = invoiceTypeCode

	};
}

class posTransaction {
	constructor(action,orderNo,orderTypeCode,brandCode,currencyCode,
	superintendentCode,scheduleShipDate,exportCommissionRate, exportExchangeRate, verificationStatus, createdBy,
	 creationDate, lastUpdatedBy , lastUpdateDate , schedule , discountRate , invoiceTypeCode
	,status,salesOrderDate,shopCode,totalActualSalesAmount,itemCode,quantity,discountType,discount,posPaymentType){
		this.action = action
		this.status = status
		this.salesOrderDate = salesOrderDate
		this.shopCode = shopCode
		this.totalActualSalesAmount = totalActualSalesAmount
		this.itemCode = itemCode
		this.quantity = quantity
		this.discountType = discountType
		this.discount = discount
		this.posPaymentType = posPaymentType
		//==brian 20211202
		this.orderNo = orderNo
		this.orderTypeCode = orderTypeCode
		this.brandCode = brandCode
		this.currencyCode = currencyCode
		this.superintendentCode = superintendentCode
		this.scheduleShipDate = scheduleShipDate
		this.exportCommissionRate = exportCommissionRate
		this.exportExchangeRate = exportExchangeRate
		this.verificationStatus = verificationStatus
		this.createdBy = createdBy
	    this.creationDate = creationDate
		this.lastUpdatedBy = lastUpdatedBy
		this.lastUpdateDate = lastUpdateDate
		this.schedule = schedule
		this.discountRate = discountRate
		this.invoiceTypeCode = invoiceTypeCode
	};
}

class log{
	constructor(action,user,logDateTime){
		this.action = action
		this.user = user
		this.logDateTime = logDateTime
	};
}
