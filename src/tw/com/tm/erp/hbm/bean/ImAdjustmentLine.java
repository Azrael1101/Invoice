package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImAdjustmentLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImAdjustmentLine implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2171943210835412169L;
	private Long lineId;
	private ImAdjustmentHead imAdjustmentHead;
	private String itemCode;
	private String itemCName;
	private String warehouseCode;
	private String warehouseName;
	private String lotNo;
	private Double quantity = new Double(0);
	private Double amount = new Double(0);
	private String reason;
	private String accountCode;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
    private Long indexNo; 
    private Double actualQuantity  = new Double(0);
    private Double difQuantity  = new Double(0);
    private String memo ;    
	private String checkStatus ;
	private Double localUnitCost = new Double(-1); // 標準單位成本
	private String sourceItemCode ; 
	
	private String customsItemCode;
	private String originalDeclarationNo;
	private Long originalDeclarationSeq;
	private Date originalDeclarationDate;
	private String boxNo;
	private Double weight;
	private String isDeleteRecord = "0"; // 是否被刪除 1 表示要被移除
    private String isLockRecord = "0"; // 是否被鎖定 1 表示鎖定
    private String message; // line 訊息的顯示
    private String status;
	private String moreOrLessType;	
	
	private Long itemNo;
	private String specDesc;
	private String unit;
	private Long importTax;
	private Long goodsTax;
	private Long cigarWineTax;
	private Long health;
	private Double advDutyRate;
	private Double cigarWineTaxRate;
	private Double goodsTaxRate;
	private String cigarWineMark;
    private String orgDeclarationNo; 
    private Long orgDeclItemNo;
    private String orgMoveWhNo;
    private Long orgMoveWhItemNo;
    private String beAft;
    private Double unitCost = new Double(-1);
    private Date originalDate;
    private Date orgImportDate;
    private Date expiryDate;
    private Date extensionDate;
    private String qty;
	// Constructors

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Double getActualQuantity() {
		return actualQuantity;
	}

	public void setActualQuantity(Double actualQuantity) {
		this.actualQuantity = actualQuantity;
	}

	public Double getDifQuantity() {
		return difQuantity;
	}

	public void setDifQuantity(Double difQuantity) {
		this.difQuantity = difQuantity;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	/** default constructor */
	public ImAdjustmentLine() {
	}

	/** minimal constructor */
	public ImAdjustmentLine(Long lineId) {
		this.lineId = lineId;
	}
	
	/** full constructor */

	public ImAdjustmentLine(Long lineId, ImAdjustmentHead imAdjustmentHead,
			String itemCode, String itemCName, String warehouseCode,
			String warehouseName, String lotNo, Double quantity, Double amount,
			String reason, String accountCode, String reserve1,
			String reserve2, String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, Long indexNo, Double actualQuantity,
			Double difQuantity, String memo, String checkStatus,
			Double localUnitCost, String sourceItemCode,
			String customsItemCode, String originalDeclarationNo,
			Long originalDeclarationSeq, Date originalDeclarationDate,
			String boxNo, Double weight, String isDeleteRecord,
			String isLockRecord, String message, String status,
			String moreOrLessType, Long itemNo, String specDesc, String unit,
			Long importTax, Long goodsTax, Long cigarWineTax, Long health,
			Double advDutyRate, Double cigarWineTaxRate, Double goodsTaxRate,
			String cigarWineMark, String orgDeclarationNo, Long orgDeclItemNo,
			String orgMoveWhNo, Long orgMoveWhItemNo, String beAft , Date orgImportDate, Date expiryDate, Date extensionDate, String qty ) {
		super();
		this.lineId = lineId;
		this.imAdjustmentHead = imAdjustmentHead;
		this.itemCode = itemCode;
		this.itemCName = itemCName;
		this.warehouseCode = warehouseCode;
		this.warehouseName = warehouseName;
		this.lotNo = lotNo;
		this.quantity = quantity;
		this.amount = amount;
		this.reason = reason;
		this.accountCode = accountCode;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.indexNo = indexNo;
		this.actualQuantity = actualQuantity;
		this.difQuantity = difQuantity;
		this.memo = memo;
		this.checkStatus = checkStatus;
		this.localUnitCost = localUnitCost;
		this.sourceItemCode = sourceItemCode;
		this.customsItemCode = customsItemCode;
		this.originalDeclarationNo = originalDeclarationNo;
		this.originalDeclarationSeq = originalDeclarationSeq;
		this.originalDeclarationDate = originalDeclarationDate;
		this.boxNo = boxNo;
		this.weight = weight;
		this.isDeleteRecord = isDeleteRecord;
		this.isLockRecord = isLockRecord;
		this.message = message;
		this.status = status;
		this.moreOrLessType = moreOrLessType;
		this.itemNo = itemNo;
		this.specDesc = specDesc;
		this.unit = unit;
		this.importTax = importTax;
		this.goodsTax = goodsTax;
		this.cigarWineTax = cigarWineTax;
		this.health = health;
		this.advDutyRate = advDutyRate;
		this.cigarWineTaxRate = cigarWineTaxRate;
		this.goodsTaxRate = goodsTaxRate;
		this.cigarWineMark = cigarWineMark;
		this.orgDeclarationNo = orgDeclarationNo;
		this.orgDeclItemNo = orgDeclItemNo;
		this.orgMoveWhNo = orgMoveWhNo;
		this.orgMoveWhItemNo = orgMoveWhItemNo;
		this.beAft = beAft;
		this.orgImportDate = orgImportDate;
		this.expiryDate = expiryDate;
		this.extensionDate = extensionDate;
		this.qty = qty;
	}


	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public ImAdjustmentHead getImAdjustmentHead() {
		return this.imAdjustmentHead;
	}

	public void setImAdjustmentHead(ImAdjustmentHead imAdjustmentHead) {
		this.imAdjustmentHead = imAdjustmentHead;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getWarehouseCode() {
		return this.warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAccountCode() {
		return this.accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
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

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getItemCName() {
		return itemCName;
	}

	public void setItemCName(String itemCName) {
		this.itemCName = itemCName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Double getLocalUnitCost() {
		return localUnitCost;
	}

	public void setLocalUnitCost(Double localUnitCost) {
		this.localUnitCost = localUnitCost;
	}

	public String getSourceItemCode() {
		return sourceItemCode;
	}

	public void setSourceItemCode(String sourceItemCode) {
		this.sourceItemCode = sourceItemCode;
	}

	public String getIsDeleteRecord() {
		return isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public String getIsLockRecord() {
		return isLockRecord;
	}

	public void setIsLockRecord(String isLockRecord) {
		this.isLockRecord = isLockRecord;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCustomsItemCode() {
		return customsItemCode;
	}

	public void setCustomsItemCode(String customsItemCode) {
		this.customsItemCode = customsItemCode;
	}

	public String getOriginalDeclarationNo() {
		return originalDeclarationNo;
	}

	public void setOriginalDeclarationNo(String originalDeclarationNo) {
		this.originalDeclarationNo = originalDeclarationNo;
	}

	public Long getOriginalDeclarationSeq() {
		return originalDeclarationSeq;
	}

	public void setOriginalDeclarationSeq(Long originalDeclarationSeq) {
		this.originalDeclarationSeq = originalDeclarationSeq;
	}

	public Date getOriginalDeclarationDate() {
		return originalDeclarationDate;
	}

	public void setOriginalDeclarationDate(Date originalDeclarationDate) {
		this.originalDeclarationDate = originalDeclarationDate;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getMoreOrLessType() {
		return moreOrLessType;
	}

	public void setMoreOrLessType(String moreOrLessType) {
		this.moreOrLessType = moreOrLessType;
	}

	public Long getItemNo() {
		return itemNo;
	}

	public void setItemNo(Long itemNo) {
		this.itemNo = itemNo;
	}

	public String getSpecDesc() {
		return specDesc;
	}

	public void setSpecDesc(String specDesc) {
		this.specDesc = specDesc;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getImportTax() {
		return importTax;
	}

	public void setImportTax(Long importTax) {
		this.importTax = importTax;
	}

	public Long getGoodsTax() {
		return goodsTax;
	}

	public void setGoodsTax(Long goodsTax) {
		this.goodsTax = goodsTax;
	}

	public Long getCigarWineTax() {
		return cigarWineTax;
	}

	public void setCigarWineTax(Long cigarWineTax) {
		this.cigarWineTax = cigarWineTax;
	}

	public Long getHealth() {
		return health;
	}

	public void setHealth(Long health) {
		this.health = health;
	}

	public Double getAdvDutyRate() {
		return advDutyRate;
	}

	public void setAdvDutyRate(Double advDutyRate) {
		this.advDutyRate = advDutyRate;
	}

	public Double getCigarWineTaxRate() {
		return cigarWineTaxRate;
	}

	public void setCigarWineTaxRate(Double cigarWineTaxRate) {
		this.cigarWineTaxRate = cigarWineTaxRate;
	}

	public Double getGoodsTaxRate() {
		return goodsTaxRate;
	}

	public void setGoodsTaxRate(Double goodsTaxRate) {
		this.goodsTaxRate = goodsTaxRate;
	}

	public String getCigarWineMark() {
		return cigarWineMark;
	}

	public void setCigarWineMark(String cigarWineMark) {
		this.cigarWineMark = cigarWineMark;
	}

	public String getOrgDeclarationNo() {
		return orgDeclarationNo;
	}

	public void setOrgDeclarationNo(String orgDeclarationNo) {
		this.orgDeclarationNo = orgDeclarationNo;
	}

	public Long getOrgDeclItemNo() {
		return orgDeclItemNo;
	}

	public void setOrgDeclItemNo(Long orgDeclItemNo) {
		this.orgDeclItemNo = orgDeclItemNo;
	}

	public String getOrgMoveWhNo() {
		return orgMoveWhNo;
	}

	public void setOrgMoveWhNo(String orgMoveWhNo) {
		this.orgMoveWhNo = orgMoveWhNo;
	}

	public Long getOrgMoveWhItemNo() {
		return orgMoveWhItemNo;
	}

	public void setOrgMoveWhItemNo(Long orgMoveWhItemNo) {
		this.orgMoveWhItemNo = orgMoveWhItemNo;
	}

	public String getBeAft() {
		return beAft;
	}

	public void setBeAft(String beAft) {
		this.beAft = beAft;
	}

	public Double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}
	
	public Date getOrgImportDate() {
		return orgImportDate;
	}

	public void setOrgImportDate(Date orgImportDate) {
		this.orgImportDate = orgImportDate;
	}
	
	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public Date getExtensionDate() {
		return extensionDate;
	}

	public void setExtensionDate(Date extensionDate) {
		this.extensionDate = extensionDate;
	}
	
	public String getQty() {
		return this.qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public Date getOriginalDate() {
		return originalDate;
	}

	public void setOriginalDate(Date originalDate) {
		this.originalDate = originalDate;
	}
	
}