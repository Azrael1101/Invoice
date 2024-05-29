package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImReceiveHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImReceiveHead implements java.io.Serializable {

	/**
	 * 
	 */
	public static final String IM_RECEIVE_LOCAL = "IRL";
	public static final String IM_RECEIVE_FOREIGN = "IRF";
	public static final String RECEIVE_RETURN_ORDER_FOREIGN = "RRF"; // 國外進貨退回
	public static final String RECEIVE_RETURN_ORDER_LOCAL = "RRL"; // 國內進貨退回
	private static final long serialVersionUID = 4826116070354615611L;
	private Long headId; // PK
	private String brandCode; // 品牌代號
	private String orderTypeCode; // 單別
	private String orderNo; // 單號
	private String declarationType; // 報關單類別
	private String declarationNo; // 報關單代號
	private Date declarationDate; // 報關單日期
	private String supplierCode; // 廠商代號
	private String supplierName; // 廠商名稱
	private Date importDate; // 進口日期
	private String bondNo; // 海關監管編號
	private String referenceBillNo; // 參考單號
	private Date releaseTime; // 放行日期
	private Long releasePackage; // 放行件數
	private String releaseCondition; // 放行附帶條件
	private String storagePlace; // 存放處所
	private String packageUnit; // 件數單位
	private Double weight = new Double(0); // 總重量
	private String vesselSign; // 船舶呼號
	private String voyageNo; // 航次
	private String shipCode; // 船公司代碼
	private String exporter; // 貨物輸出人
	private String clearanceType; // 通關方式
	private String declarationBoxNo; // 報關行箱數
	private String inbondNo; // 進倉保稅業者代碼?
	private String outbondNo; // 出倉保稅業者代碼?
	private String tradeTeam; // 交易條件
	private String paymentTermCode; // 付款條件
	private String countryCode; // 國別代碼
	private String currencyCode; // 幣別代碼
	private Double exchangeRate = new Double(1); // 匯率
	private String contactPerson; // 客戶聯絡窗口
	private String taxType; // 稅別(應稅:3、零稅:2、免稅:1)
	private Double taxRate = new Double(0); // 稅率
	private Double taxAmount = new Double(0); // 稅金
	private String defaultWarehouseCode; // 庫別
	private Double totalLocalPurchaseAmount = new Double(0); // 原總銷售金額
	private Double totalForeignPurchaseAmount = new Double(0); // 總實際銷售金額
	private String remark1;
	private String remark2;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private List<ImReceiveInvoice> imReceiveInvoices = new ArrayList();
	private List<ImReceiveExpense> imReceiveExpenses = new ArrayList();
	private List<ImReceiveItem> imReceiveItems = new ArrayList();
	private String status;
	private Date orderDate; // 訂單日期
	private Double invoiceLocalAmount = new Double(0);
	private Double invoiceForeignAmount = new Double(0);
	private Double expenseLocalAmount = new Double(0);
	private Double expenseForeignAmount = new Double(0);
	private String budgetYear;
	private String warehouseStatus;
	private Date warehouseInDate;
	private String lotNo; // 國內用批號
	private String verificationStatus = "N";
	private String onHandStatus = "N";
	private String guiNo; // 國內用發票
	private String defPoOrderType = PoPurchaseOrderHead.PURCHASE_ORDER_LOCAL; // 預設是國內採購
	private String defPoOrderNo; // 國內用預設採購單號 / default invoiceNo
	private String lcNo; // LC NO
	private Double lcUseAmount = new Double(0); // LC 調整金額
	private Double lcUseAmount1 = new Double(0); // LC 調整金額	
	private String expenseStatus; // 費用輸入狀態
	private String lcNo1; // LC NO
	private Date actualPayDate ;

	private String itemCategory;
	private String receiptedBy;
	private Date   receiptDate;
	private Double totalLocalAccountsPayable;	// 本幣應付帳款
	private Double totalForeignAccountsPayable;	// 原幣應付帳款
	private String financeConfirm;			// 財務確認
	private String budgetMonth;			// 預算月份
	
	private String sourceOrderNo;       // 來源單別(T2)
	private String sampleMovOrderTypeCode;
	private String sampleMovOrderNo;
	private String defectMovOrderTypeCode;
	private String defectMovOrderNo;
	private String shortMovOrderTypeCode;
	private String shortMovOrderNo;
	private String warehouseEmployee;
	private String latestExportDeclNo;
	private Date   latestExportDeclDate;
	private String latestExportDeclType;
	private Double exportCommissionRate;
	private Double exportExpense;
	private Long processId;
	
	private String tranRecordStatus;
	private String customsStatus;
	private String cStatus;
	private String tranAllowUpload;
	private String schedule;
	private Date orgImportDate;
	
	
	// Constructors

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

	public String getTranAllowUpload() {
		return tranAllowUpload;
	}

	public void setTranAllowUpload(String tranAllowUpload) {
		this.tranAllowUpload = tranAllowUpload;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	
	public Date getOrgImportDate() {
		return orgImportDate;
	}

	public void setOrgImportDate(Date orgImportDate) {
		this.orgImportDate = orgImportDate;
	}

	public String getWarehouseEmployee() {
	    return warehouseEmployee;
	}

	public void setWarehouseEmployee(String warehouseEmployee) {
	    this.warehouseEmployee = warehouseEmployee;
	}

	public String getFinanceConfirm() {
	    return financeConfirm;
	}

	public void setFinanceConfirm(String financeConfirm) {
	    this.financeConfirm = financeConfirm;
	}

	public String getExpenseStatus() {
		return expenseStatus;
	}

	public void setExpenseStatus(String expenseStatus) {
		this.expenseStatus = expenseStatus;
	}

	public String getLcNo() {
		return lcNo;
	}

	public void setLcNo(String lcNo) {
		this.lcNo = lcNo;
	}

	public String getGuiNo() {
		return guiNo;
	}

	public void setGuiNo(String guiNo) {
		this.guiNo = guiNo;
	}

	public String getDefPoOrderType() {
		return defPoOrderType;
	}

	public void setDefPoOrderType(String defPoOrderType) {
		this.defPoOrderType = defPoOrderType;
	}

	public String getDefPoOrderNo() {
		return defPoOrderNo;
	}

	public void setDefPoOrderNo(String defPoOrderNo) {
		this.defPoOrderNo = defPoOrderNo;
	}

	public Date getWarehouseInDate() {
		return warehouseInDate;
	}

	public void setWarehouseInDate(Date warehouseInDate) {
		this.warehouseInDate = warehouseInDate;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	/** default constructor */
	public ImReceiveHead() {
	}

	/** minimal constructor */
	public ImReceiveHead(Long headId) {
		this.headId = headId;
	}
	
	// Utils
	public boolean isCorrectTaxRate(){
		if(taxType != null){
			if(taxType.equals("1") || taxType.equals("2")) return taxRate == 0;
			else if(taxType.equals("3")) return taxRate == 5;
			else return false;
		}
		else 
			return false;
	}
	
	public String getTaxTypeName(){
		if(taxType != null){
			if(taxType.equals("1")) return "免稅";
			else if(taxType.equals("2")) return "零稅";
			else if(taxType.equals("3")) return "應稅";
			else return "未知";
		}
		else
			return "未知";
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

	public String getDeclarationType() {
		return this.declarationType;
	}

	public void setDeclarationType(String declarationType) {
		this.declarationType = declarationType;
	}

	public String getDeclarationNo() {
		return this.declarationNo;
	}

	public void setDeclarationNo(String declarationNo) {
		this.declarationNo = declarationNo;
	}

	public Date getDeclarationDate() {
		return this.declarationDate;
	}

	public void setDeclarationDate(Date declarationDate) {
		this.declarationDate = declarationDate;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Date getImportDate() {
		return this.importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public String getBondNo() {
		return this.bondNo;
	}

	public void setBondNo(String bondNo) {
		this.bondNo = bondNo;
	}

	public String getReferenceBillNo() {
		return this.referenceBillNo;
	}

	public void setReferenceBillNo(String referenceBillNo) {
		this.referenceBillNo = referenceBillNo;
	}

	public Date getReleaseTime() {
		return this.releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public Long getReleasePackage() {
		return this.releasePackage;
	}

	public void setReleasePackage(Long releasePackage) {
		this.releasePackage = releasePackage;
	}

	public String getReleaseCondition() {
		return this.releaseCondition;
	}

	public void setReleaseCondition(String releaseCondition) {
		this.releaseCondition = releaseCondition;
	}

	public String getStoragePlace() {
		return this.storagePlace;
	}

	public void setStoragePlace(String storagePlace) {
		this.storagePlace = storagePlace;
	}

	public String getPackageUnit() {
		return this.packageUnit;
	}

	public void setPackageUnit(String packageUnit) {
		this.packageUnit = packageUnit;
	}

	public Double getWeight() {
		return this.weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getVesselSign() {
		return this.vesselSign;
	}

	public void setVesselSign(String vesselSign) {
		this.vesselSign = vesselSign;
	}

	public String getVoyageNo() {
		return this.voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public String getShipCode() {
		return this.shipCode;
	}

	public void setShipCode(String shipCode) {
		this.shipCode = shipCode;
	}

	public String getExporter() {
		return this.exporter;
	}

	public void setExporter(String exporter) {
		this.exporter = exporter;
	}

	public String getClearanceType() {
		return this.clearanceType;
	}

	public void setClearanceType(String clearanceType) {
		this.clearanceType = clearanceType;
	}

	public String getDeclarationBoxNo() {
		return this.declarationBoxNo;
	}

	public void setDeclarationBoxNo(String declarationBoxNo) {
		this.declarationBoxNo = declarationBoxNo;
	}

	public String getInbondNo() {
		return this.inbondNo;
	}

	public void setInbondNo(String inbondNo) {
		this.inbondNo = inbondNo;
	}

	public String getOutbondNo() {
		return this.outbondNo;
	}

	public void setOutbondNo(String outbondNo) {
		this.outbondNo = outbondNo;
	}

	public String getTradeTeam() {
		return this.tradeTeam;
	}

	public void setTradeTeam(String tradeTeam) {
		this.tradeTeam = tradeTeam;
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

	public List<ImReceiveInvoice> getImReceiveInvoices() {
		return this.imReceiveInvoices;
	}

	public void setImReceiveInvoices(List<ImReceiveInvoice> imReceiveInvoices) {
		this.imReceiveInvoices = imReceiveInvoices;
	}

	public List<ImReceiveExpense> getImReceiveExpenses() {
		return this.imReceiveExpenses;
	}

	public void setImReceiveExpenses(List<ImReceiveExpense> imReceiveExpenses) {
		this.imReceiveExpenses = imReceiveExpenses;
	}

	public List<ImReceiveItem> getImReceiveItems() {
		return this.imReceiveItems;
	}

	public void setImReceiveItems(List<ImReceiveItem> imReceiveItems) {
		this.imReceiveItems = imReceiveItems;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Double getInvoiceLocalAmount() {
		return invoiceLocalAmount;
	}

	public void setInvoiceLocalAmount(Double invoiceLocalAmount) {
		this.invoiceLocalAmount = invoiceLocalAmount;
	}

	public Double getInvoiceForeignAmount() {
		return invoiceForeignAmount;
	}

	public void setInvoiceForeignAmount(Double invoiceForeignAmount) {
		this.invoiceForeignAmount = invoiceForeignAmount;
	}

	public Double getExpenseLocalAmount() {
		return expenseLocalAmount;
	}

	public void setExpenseLocalAmount(Double expenseLocalAmount) {
		this.expenseLocalAmount = expenseLocalAmount;
	}

	public Double getExpenseForeignAmount() {
		return expenseForeignAmount;
	}

	public void setExpenseForeignAmount(Double expenseForeignAmount) {
		this.expenseForeignAmount = expenseForeignAmount;
	}

	public String getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}

	public String getWarehouseStatus() {
		return warehouseStatus;
	}

	public void setWarehouseStatus(String warehouseStatus) {
		this.warehouseStatus = warehouseStatus;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public String getOnHandStatus() {
		return onHandStatus;
	}

	public void setOnHandStatus(String onHandStatus) {
		this.onHandStatus = onHandStatus;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getLcNo1() {
		return lcNo1;
	}

	public void setLcNo1(String lcNo1) {
		this.lcNo1 = lcNo1;
	}

	public Date getActualPayDate() {
		return actualPayDate;
	}

	public void setActualPayDate(Date actualPayDate) {
		this.actualPayDate = actualPayDate;
	}

	public Double getLcUseAmount() {
		return lcUseAmount;
	}

	public void setLcUseAmount(Double lcUseAmount) {
		this.lcUseAmount = lcUseAmount;
	}

	public Double getLcUseAmount1() {
		return lcUseAmount1;
	}

	public void setLcUseAmount1(Double lcUseAmount1) {
		this.lcUseAmount1 = lcUseAmount1;
	}

	public String getItemCategory() {
	    return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
	    this.itemCategory = itemCategory;
	}

	public String getReceiptedBy() {
	    return receiptedBy;
	}

	public void setReceiptedBy(String receiptedBy) {
	    this.receiptedBy = receiptedBy;
	}

	public Date getReceiptDate() {
	    return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
	    this.receiptDate = receiptDate;
	}

	public Double getTotalLocalAccountsPayable() {
	    return totalLocalPurchaseAmount + invoiceLocalAmount + expenseLocalAmount ;
	}

	public void setTotalLocalAccountsPayable(Double totalLocalAccountsPayable) {
	    this.totalLocalAccountsPayable = totalLocalAccountsPayable;
	}

	public Double getTotalForeignAccountsPayable() {
	    return totalForeignPurchaseAmount + invoiceForeignAmount + expenseForeignAmount ;
	}

	public void setTotalForeignAccountsPayable(Double totalForeignAccountsPayable) {
	    this.totalForeignAccountsPayable = totalForeignAccountsPayable;
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

	public String getSampleMovOrderTypeCode() {
	    return sampleMovOrderTypeCode;
	}

	public void setSampleMovOrderTypeCode(String sampleMovOrderTypeCode) {
	    this.sampleMovOrderTypeCode = sampleMovOrderTypeCode;
	}

	public String getSampleMovOrderNo() {
	    return sampleMovOrderNo;
	}

	public void setSampleMovOrderNo(String sampleMovOrderNo) {
	    this.sampleMovOrderNo = sampleMovOrderNo;
	}

	public String getDefectMovOrderTypeCode() {
	    return defectMovOrderTypeCode;
	}

	public void setDefectMovOrderTypeCode(String defectMovOrderTypeCode) {
	    this.defectMovOrderTypeCode = defectMovOrderTypeCode;
	}

	public String getDefectMovOrderNo() {
	    return defectMovOrderNo;
	}

	public void setDefectMovOrderNo(String defectMovOrderNo) {
	    this.defectMovOrderNo = defectMovOrderNo;
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

	public Double getExportExpense() {
	    return exportExpense;
	}

	public void setExportExpense(Double exportExpense) {
	    this.exportExpense = exportExpense;
	}

	public String getShortMovOrderTypeCode() {
		return shortMovOrderTypeCode;
	}

	public void setShortMovOrderTypeCode(String shortMovOrderTypeCode) {
		this.shortMovOrderTypeCode = shortMovOrderTypeCode;
	}

	public String getShortMovOrderNo() {
		return shortMovOrderNo;
	}

	public void setShortMovOrderNo(String shortMovOrderNo) {
		this.shortMovOrderNo = shortMovOrderNo;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

}