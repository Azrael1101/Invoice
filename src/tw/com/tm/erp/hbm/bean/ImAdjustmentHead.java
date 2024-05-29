package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImAdjustmentHead entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class ImAdjustmentHead implements java.io.Serializable {

	// Fields

	/**
	 *
	 */
	private static final long serialVersionUID = -8369606138865613795L;
	/**
	 *
	 */

	private Long headId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private Date adjustmentDate;
	private String reason;
	private String sourceOrderTypeCode;
	private String sourceOrderNo;
	private String remark1;
	private String remark2;
	private String reserve1;	// 放置invoice headId
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private List<ImAdjustmentLine> imAdjustmentLines = new ArrayList();
	private String status ; //狀態
	private String statusName;		// 額外欄位 顯示狀態的中文
	private String affectCost ; //是否影響成本
	private Date importDate;
	private Date checkDate ;
	private Double carTon ;
	private	Double box ;
	private String diffProcess ;
	private String checkStatus ;
	private String checkDepartment ;
	private String onHandStatus = "N" ;

	private String adjustmentType;
	private String taxType;
	private String declarationNo;
	private Long declarationSeq;
	private Date declarationDate;
	private String declarationType;
	private Long boxQty;
	private String isAdjustQuantity;
	private String isAdjustCost;
	private String defaultWarehouseCode;
	private String transport;
	private String district;
	private Long processId;

	private String unBlockOnHand; // 是否自動解鎖報單 Y/N add by Weichun 2011.09.28
	
	private String processCustCd;
	private String resStoreCode;
	private Long aftTotalItems;
	private Long befTotalItems;
	private String resNo;
	private String cStatus;
	private String customsStatus;
	
	private String tranPhrase;
	private String tranRecordStatus;
	private String tranAllowUpload;	
	private Date dischargeDate;
	private String schedule;
	private	String fileNo;
	
	//展延相關欄位 Daniel
	private Integer extentionTime;
	private String extentionTimeType;
	private String isSpecial;
	private String customsWarehouseCode;
	
//	//離島重量變更V2.3改版-DF11新增領用單號碼CON_NO Daniel
//	private String conNO;
//	
//	public String getConNO() {
//		return conNO;
//	}
//
//	public void setConNO(String conNO) {
//		this.conNO = conNO;
//	}

	public String getCustomsWarehouseCode(){
		return customsWarehouseCode;
	}
	
	public void setCustomsWarehouseCode(String customsWarehouseCode){
		this.customsWarehouseCode = customsWarehouseCode;
	}
	
	public Integer getExtentionTime() {
		return extentionTime;
	}

	public void setExtentionTime(Integer extentionTime) {
		this.extentionTime = extentionTime;
	}

	public String getExtentionTimeType() {
		return extentionTimeType;
	}

	public void setExtentionTimeType(String extentionTimeType) {
		this.extentionTimeType = extentionTimeType;
	}

	public String getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}

	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public String getCustomsStatus() {
		return customsStatus;
	}

	public void setCustomsStatus(String customsStatus) {
		this.customsStatus = customsStatus;
	}

	/**
	 * @return the unBlockOnHand
	 */
	public String getUnBlockOnHand() {
		return unBlockOnHand;
	}

	/**
	 * @param unBlockOnHand the unBlockOnHand to set
	 */
	public void setUnBlockOnHand(String unBlockOnHand) {
		this.unBlockOnHand = unBlockOnHand;
	}

	// Constructors
	public String getCheckDepartment() {
		return checkDepartment;
	}

	public void setCheckDepartment(String checkDepartment) {
		this.checkDepartment = checkDepartment;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Double getCarTon() {
		return carTon;
	}

	public void setCarTon(Double carTon) {
		this.carTon = carTon;
	}

	public Double getBox() {
		return box;
	}

	public void setBox(Double box) {
		this.box = box;
	}

	public String getDiffProcess() {
		return diffProcess;
	}

	public void setDiffProcess(String diffProcess) {
		this.diffProcess = diffProcess;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/** default constructor */
	public ImAdjustmentHead() {
	}

	/** minimal constructor */
	public ImAdjustmentHead(Long headId) {
		this.headId = headId;
	}

	/** full constructor */
	public ImAdjustmentHead(Long headId, String brandCode,
			String orderTypeCode, String orderNo, Date adjustmentDate,
			String reason, String sourceOrderTypeCode, String sourceOrderNo,
			String remark1, String remark2, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, List<ImAdjustmentLine> imAdjustmentLines,
			String status, String statusName, String affectCost,
			Date importDate, Date checkDate, Double carTon, Double box,
			String diffProcess, String checkStatus, String checkDepartment,
			String onHandStatus, String adjustmentType, String taxType,
			String declarationNo, Long declarationSeq, Date declarationDate,
			String declarationType, Long boxQty, String isAdjustQuantity,
			String isAdjustCost, String defaultWarehouseCode, String fileNo,
			Integer extentionTime, String extentionTimeType, String isSpecial,
			String customsWarehouseCode ) {
		super();
		this.headId = headId;
		this.brandCode = brandCode;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.adjustmentDate = adjustmentDate;
		this.reason = reason;
		this.sourceOrderTypeCode = sourceOrderTypeCode;
		this.sourceOrderNo = sourceOrderNo;
		this.remark1 = remark1;
		this.remark2 = remark2;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.imAdjustmentLines = imAdjustmentLines;
		this.status = status;
		this.statusName = statusName;
		this.affectCost = affectCost;
		this.importDate = importDate;
		this.checkDate = checkDate;
		this.carTon = carTon;
		this.box = box;
		this.diffProcess = diffProcess;
		this.checkStatus = checkStatus;
		this.checkDepartment = checkDepartment;
		this.onHandStatus = onHandStatus;
		this.adjustmentType = adjustmentType;
		this.taxType = taxType;
		this.declarationNo = declarationNo;
		this.declarationSeq = declarationSeq;
		this.declarationDate = declarationDate;
		this.declarationType = declarationType;
		this.boxQty = boxQty;
		this.isAdjustQuantity = isAdjustQuantity;
		this.isAdjustCost = isAdjustCost;
		this.defaultWarehouseCode = defaultWarehouseCode;
		this.fileNo = fileNo; 
		this.extentionTime = extentionTime;
		this.extentionTimeType = extentionTimeType;
		this.isSpecial = isSpecial;
		this.customsWarehouseCode = customsWarehouseCode;
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

	public Date getAdjustmentDate() {
		return this.adjustmentDate;
	}

	public void setAdjustmentDate(Date adjustmentDate) {
		this.adjustmentDate = adjustmentDate;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSourceOrderTypeCode() {
		return this.sourceOrderTypeCode;
	}

	public void setSourceOrderTypeCode(String sourceOrderTypeCode) {
		this.sourceOrderTypeCode = sourceOrderTypeCode;
	}

	public String getSourceOrderNo() {
		return this.sourceOrderNo;
	}

	public void setSourceOrderNo(String sourceOrderNo) {
		this.sourceOrderNo = sourceOrderNo;
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

	public List<ImAdjustmentLine> getImAdjustmentLines() {
		return imAdjustmentLines;
	}

	public void setImAdjustmentLines(List<ImAdjustmentLine> imAdjustmentLines) {
		this.imAdjustmentLines = imAdjustmentLines;
	}

	public String getAffectCost() {
		return affectCost;
	}

	public void setAffectCost(String affectCost) {
		this.affectCost = affectCost;
	}

	public String getOnHandStatus() {
		return onHandStatus;
	}

	public void setOnHandStatus(String onHandStatus) {
		this.onHandStatus = onHandStatus;
	}

	public String getAdjustmentType() {
		return adjustmentType;
	}

	public void setAdjustmentType(String adjustmentType) {
		this.adjustmentType = adjustmentType;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getDeclarationNo() {
		return declarationNo;
	}

	public void setDeclarationNo(String declarationNo) {
		this.declarationNo = declarationNo;
	}

	public Long getDeclarationSeq() {
		return declarationSeq;
	}

	public void setDeclarationSeq(Long declarationSeq) {
		this.declarationSeq = declarationSeq;
	}

	public Date getDeclarationDate() {
		return declarationDate;
	}

	public void setDeclarationDate(Date declarationDate) {
		this.declarationDate = declarationDate;
	}

	public Long getBoxQty() {
		return boxQty;
	}

	public void setBoxQty(Long boxQty) {
		this.boxQty = boxQty;
	}

	public String getIsAdjustQuantity() {
		return isAdjustQuantity;
	}

	public void setIsAdjustQuantity(String isAdjustQuantity) {
		this.isAdjustQuantity = isAdjustQuantity;
	}

	public String getIsAdjustCost() {
		return isAdjustCost;
	}

	public void setIsAdjustCost(String isAdjustCost) {
		this.isAdjustCost = isAdjustCost;
	}

	public String getDefaultWarehouseCode() {
		return defaultWarehouseCode;
	}
	
	public void setDefaultWarehouseCode(String defaultWarehouseCode) {
		this.defaultWarehouseCode = defaultWarehouseCode;
	}
	
	public String getFileNo() {
			return fileNo;
		}
	
	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getDeclarationType() {
		return declarationType;
	}

	public void setDeclarationType(String declarationType) {
		this.declarationType = declarationType;
	}

	public String getTransport() {
	    return transport;
	}

	public void setTransport(String transport) {
	    this.transport = transport;
	}

	public String getDistrict() {
	    return district;
	}

	public void setDistrict(String district) {
	    this.district = district;
	}

	public Long getProcessId() {
	    return processId;
	}

	public void setProcessId(Long processId) {
	    this.processId = processId;
	}

	public String getProcessCustCd() {
		return processCustCd;
	}

	public void setProcessCustCd(String processCustCd) {
		this.processCustCd = processCustCd;
	}

	public String getResStoreCode() {
		return resStoreCode;
	}

	public void setResStoreCode(String resStoreCode) {
		this.resStoreCode = resStoreCode;
	}

	public Long getAftTotalItems() {
		return aftTotalItems;
	}

	public void setAftTotalItems(Long aftTotalItems) {
		this.aftTotalItems = aftTotalItems;
	}

	public Long getBefTotalItems() {
		return befTotalItems;
	}

	public void setBefTotalItems(Long befTotalItems) {
		this.befTotalItems = befTotalItems;
	}

	public String getResNo() {
		return resNo;
	}

	public void setResNo(String resNo) {
		this.resNo = resNo;
	}

	public String getcStatus() {
		return cStatus;
	}

	public void setcStatus(String cStatus) {
		cStatus = cStatus;
	}

	public String getTranPhrase() {
		return tranPhrase;
	}

	public void setTranPhrase(String tranPhrase) {
		this.tranPhrase = tranPhrase;
	}

	public String getTranRecordStatus() {
		return tranRecordStatus;
	}

	public void setTranRecordStatus(String tranRecordStatus) {
		this.tranRecordStatus = tranRecordStatus;
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



}