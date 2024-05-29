package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PoPurchaseOrderHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PoPurchaseOrderHead implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5982276340947102460L;
	public static final String PURCHASE_ORDER_FOREIGN = "POF" ; //國外採購單
	public static final String PURCHASE_ORDER_LOCAL = "POL" ; //國內採購單
	
	public static final String PURCHASE_RETURN_ORDER_FOREIGN = "PRF" ; //國外進貨退回
	public static final String PURCHASE_RETURN_ORDER_LOCAL = "PRL" ; //國內進貨退回

	public static final String PURCHASE_ORDER_NO_TAX = "1" ; //免稅
	public static final String PURCHASE_ORDER_TAX = "3" ; //應稅
	public static final String PURCHASE_ORDER_ZERO_TAX = "2" ; //零稅
	public static final String PURCHASE_ORDER_INVOICE_TYPE_CODE_3 = "3" ; //三聯
	public static final String PURCHASE_ORDER_INVOICE_TYPE_CODE_2 = "2" ; //二聯
	public static final String PURCHASE_ORDER_INVOICE_TYPE_CODE_1 = "1" ; //收據
	public static final String CLOSE_ORDER_Y = "Y" ;
	public static final String CLOSE_ORDER_N = "N" ;	
	
	// Fields
	private Long headId;  //pk
	private String brandCode;  //品牌代號
	private String orderTypeCode; //單別 PO 請參考 BU_ORDER_TYPE
	private String orderNo;  //單號 要呼叫 BuOrderTypeService 
	private Date purchaseOrderDate;  //採購日期
	private String purchaseType;  //I)國內 F)國外 
	private String supplierCode;  //廠商代號
	private String supplierName;  //廠商名稱
	private String quotationCode;  //廠商報價單號
	private String paymentTermCode;  //付款條件
	private String paymentTermCode1;  //付款條件
	
	private String countryCode;  //國別代碼
	private String currencyCode;  //幣別代碼
	private Double exchangeRate = new Double(1);  //匯率
	private String contactPerson;  //客戶聯絡窗口
	private String superintendentCode; //訂單負責人
	private String superintendentName; //訂單負責人
	private String invoiceTypeCode;  //發票類型( 3.三聯式統一發票,2.二聯式統一發票) 
	private String guiCode;  //統一編號
	private String taxType;	//稅別(應稅、零稅、免稅)
	private Double taxRate = new Double(0) ;  //稅率
	private String isPartialShipment;  //是否分批到貨 Y/N
	private Date schedulePaymentDate;  //預計付款日
	private Date scheduleReceiptDate;  //預計到貨日
	private String defaultWarehouseCode;  //入庫倉庫代號
	private Double totalLocalPurchaseAmount = new Double(0) ;  //台幣總金額
	private Double totalForeignPurchaseAmount = new Double(0) ;  //外幣總金額
	private String reserve1;  //保留欄位1
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private List<PoPurchaseOrderLine> poPurchaseOrderLines = new ArrayList();
	private String status ; //狀態
	private String budgetYear ;	//年度預算
	private String budgetMonth;
	//20080725 shan add
	private Double taxAmount = new Double(0) ; //稅合計
	private String closeOrder ; // close Order
	
	private Double totalBudget = new Double(0);
	private Double totalAppliedBudget = new Double(0);
	private Double totalRemainderBudget = new Double(0);
	private Double totalProductCounts = new Double(0);
	private Double totalUnitPriceAmount = new Double(0);
	private Double totalSigningBudget = new Double(0);
	private Double totalUsedPriceAmount = new Double(0);
	
	private String tradeTermCode;
	private String packaging;
	private String sourceOrderNo;
	private String categoryType;
	private String errorMessage;
	private Double asignedBudget; //指定預算 for 百貨(預算以此金額整批扣除, 不從 POLINE 合計)  20091022 Arthur
	
	private String purchaseAssist;
	private String purchaseMember;
	private String purchaseMaster;

	private Long processId;
	private String poOrderNo; //國外採購單號
	
	private Long budgetLineId; //
	private String category01;
	private String salesPeriod;
	// Constructors

	private Double totalReturnedBudget = new Double(0);
	
	
	public String getPurchaseAssist() {
	    return purchaseAssist;
	}

	public void setPurchaseAssist(String purchaseAssist) {
	    this.purchaseAssist = purchaseAssist;
	}

	public String getPurchaseMember() {
	    return purchaseMember;
	}

	public void setPurchaseMember(String purchaseMember) {
	    this.purchaseMember = purchaseMember;
	}

	public String getPurchaseMaster() {
	    return purchaseMaster;
	}

	public void setPurchaseMaster(String purchaseMaster) {
	    this.purchaseMaster = purchaseMaster;
	}

	public Double getTotalUsedPriceAmount() {
	    return totalUsedPriceAmount;
	}

	public void setTotalUsedPriceAmount(Double totalUsedPriceAmount) {
	    this.totalUsedPriceAmount = totalUsedPriceAmount;
	}

	public String getTradeTermCode() {
		return tradeTermCode;
	}

	public void setTradeTermCode(String tradeTermCode) {
		this.tradeTermCode = tradeTermCode;
	}

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public Double getTotalBudget() {
		return totalBudget;
	}

	public void setTotalBudget(Double totalBudget) {
		this.totalBudget = totalBudget;
	}

	public Double getTotalAppliedBudget() {
		return totalAppliedBudget;
	}

	public void setTotalAppliedBudget(Double totalAppliedBudget) {
		this.totalAppliedBudget = totalAppliedBudget;
	}

	public Double getTotalRemainderBudget() {
		return totalRemainderBudget;
	}

	public void setTotalRemainderBudget(Double totalRemainderBudget) {
		this.totalRemainderBudget = totalRemainderBudget;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/** default constructor */
	public PoPurchaseOrderHead() {
	}

	/** minimal constructor */
	public PoPurchaseOrderHead(Long headId) {
		this.headId = headId;
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

	public Date getPurchaseOrderDate() {
		return this.purchaseOrderDate;
	}

	public void setPurchaseOrderDate(Date purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
	}

	public String getPurchaseType() {
		return this.purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
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

	public Double getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getSuperintendentCode() {
		return this.superintendentCode;
	}

	public void setSuperintendentCode(String superintendentCode) {
		this.superintendentCode = superintendentCode;
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

	public String getIsPartialShipment() {
		return this.isPartialShipment;
	}

	public void setIsPartialShipment(String isPartialShipment) {
		this.isPartialShipment = isPartialShipment;
	}

	public Date getSchedulePaymentDate() {
		return this.schedulePaymentDate;
	}

	public void setSchedulePaymentDate(Date schedulePaymentDate) {
		this.schedulePaymentDate = schedulePaymentDate;
	}

	public Date getScheduleReceiptDate() {
		return this.scheduleReceiptDate;
	}

	public void setScheduleReceiptDate(Date scheduleReceiptDate) {
		this.scheduleReceiptDate = scheduleReceiptDate;
	}

	public String getDefaultWarehouseCode() {
		return this.defaultWarehouseCode;
	}

	public void setDefaultWarehouseCode(String defaultWarehouseCode) {
		this.defaultWarehouseCode = defaultWarehouseCode;
	}

	public Double getTotalLocalPurchaseAmount() {
		return this.totalLocalPurchaseAmount;
	}

	public void setTotalLocalPurchaseAmount(Double totalLocalPurchaseAmount) {
		this.totalLocalPurchaseAmount = totalLocalPurchaseAmount;
	}

	public Double getTotalForeignPurchaseAmount() {
		return this.totalForeignPurchaseAmount;
	}

	public void setTotalForeignPurchaseAmount(Double totalForeignPurchaseAmount) {
		this.totalForeignPurchaseAmount = totalForeignPurchaseAmount;
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

	public List<PoPurchaseOrderLine> getPoPurchaseOrderLines() {
		return this.poPurchaseOrderLines;
	}

	public void setPoPurchaseOrderLines(List<PoPurchaseOrderLine> poPurchaseOrderLines) {
		this.poPurchaseOrderLines = poPurchaseOrderLines;
	}

	public String getCloseOrder() {
		return closeOrder;
	}

	public void setCloseOrder(String closeOrder) {
		this.closeOrder = closeOrder;
	}

	public String getPaymentTermCode1() {
		return paymentTermCode1;
	}

	public void setPaymentTermCode1(String paymentTermCode1) {
		this.paymentTermCode1 = paymentTermCode1;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSuperintendentName() {
		return superintendentName;
	}

	public void setSuperintendentName(String superintendentName) {
		this.superintendentName = superintendentName;
	}

	public void setTotalProductCounts(Double totalProductCounts) {
		this.totalProductCounts = totalProductCounts;
	}

	public Double getTotalProductCounts() {
		return totalProductCounts;
	}

	public Double getTotalUnitPriceAmount() {
		return totalUnitPriceAmount;
	}

	public void setTotalUnitPriceAmount(Double totalUnitPriceAmount) {
		this.totalUnitPriceAmount = totalUnitPriceAmount;
	}

	public Double getTotalSigningBudget() {
		return totalSigningBudget;
	}

	public void setTotalSigningBudget(Double totalSigningBudget) {
		this.totalSigningBudget = totalSigningBudget;
	}

	public String getBudgetMonth() {
	    return budgetMonth;
	}

	public void setBudgetMonth(String budgetMonth) {
	    this.budgetMonth = budgetMonth;
	}

	public String getSourceOrderNo() {
		return sourceOrderNo;
	}

	public void setSourceOrderNo(String sourceOrderNo) {
		this.sourceOrderNo = sourceOrderNo;
	}
	
	public String getCategoryType() {
	    return categoryType;
	}

	public void setCategoryType(String categoryType) {
	    this.categoryType = categoryType;
	}

	public String getErrorMessage() {
	    return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
	    this.errorMessage = errorMessage;
	}

	public Double getAsignedBudget() {
	    return asignedBudget;
	}

	public void setAsignedBudget(Double asignedBudget) {
	    this.asignedBudget = asignedBudget;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getPoOrderNo() {
		return poOrderNo;
	}

	public void setPoOrderNo(String poOrderNo) {
		this.poOrderNo = poOrderNo;
	}

	public Double getTotalReturnedBudget() {
		return totalReturnedBudget;
	}

	public void setTotalReturnedBudget(Double totalReturnedBudget) {
		this.totalReturnedBudget = totalReturnedBudget;
	}

	public Long getBudgetLineId() {
		return budgetLineId;
	}

	public void setBudgetLineId(Long budgetLineId) {
		this.budgetLineId = budgetLineId;
	}

	public String getCategory01() {
		return category01;
	}

	public void setCategory01(String category01) {
		this.category01 = category01;
	}
	
	public String getSalesPeriod() {
		return salesPeriod;
	}

	public void setSalesPeriod(String salesPeriod) {
		this.salesPeriod = salesPeriod;
	}

}