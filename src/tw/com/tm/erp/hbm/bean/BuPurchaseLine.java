package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SoDeliveryHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuPurchaseLine implements java.io.Serializable {

	 /**
	 * 
	 */
	
	private static final long serialVersionUID = 1890745642719405388L;

	private Long headId;
	private Long lineId;
	private Long indexNo;
	private String itemNo;
	private String itemName;
	private String specInfo;
	private Integer quantity;
	private Integer purUnitPrice;
	private Integer purTotalAmount;
	private String supplier;
	private String status;
	private String createdBy;
	private Date creationDate;
	private Date lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedByName; // 暫時欄位 更新人員
	private String assetsNo;
	private Integer reUnitPrice;
	private Integer reTotalAmount;
	private String taxEnable;
	private String isDeleteRecord;
	private String categoryCode;
	private BuPurchaseHead buPurchaseHead;
	private String enable;
	
	private Date saDate;
	private String saInChargeCode;
	private String saInChargeName;
	private Date devDate;
	private String devInChargeName;
	private String devInChargeCode;
	private String devConfirmorName;
	private String devConfirmorCode;
	private Date testDate;
	private String testInChargeName;
	private String testInChargeCode;
	private String version;
	private Date onlineDate;
	private String onlineInChargeName;
	private String onlineInChargeCode;
	
	
	private String shopCode;
	private String posMachineCode;
	private String suppilerCode;
	private String suppilerName;
	private Date assignMenuDateStart;
	private String assignMenuTimeStart;
	private String supportNo;
	private Date executeDateStart;
	private String executeTimeStart;
	private Date executeDateEnd;
	private String executeTimeEnd;
	private String executeMemo;
	private String executeInCharge;

	private String adGroupCode;
	
	private String adMemberCode;
	private String adMemberName;
	

	private String taskInchargeCode;
	private String taskInchargeName;
	private String taskType;
	private String taskTypeNo;
	private Date taskDate;
	private String taskHours;

	private Long executeHours;
	private Date finishDate;
	
	private Double execHours;
	private String yearBudget;
	private Double linePriority;
	private Long boxCapacity;
	private Long box;
	private Integer totalQty;
	private Double unitPrice;
	private String itemCategory;
	private String orderTime;
	/** default constructor */
	public BuPurchaseLine() {
		
	}

	/** full constructor */
	public BuPurchaseLine(Long headId,Long indexNo, Long lineId ,String itemNo, String itemName,
			String specInfo, Integer quantity, Integer purUnitPrice,
			Integer purTotalAmount,
			String supplier, String status, String createdBy,
			Date creationDate, String assetsNo,Integer reUnitPrice,Integer reTotalAmount,String taxEnable,String isDeleteRecord,String categoryCode,String enable, BuPurchaseHead buPurchaseHead
			,Date saDate,String saInChargeCode,String saInChargeName,Date devDate, String devInChargeName ,String devInChargeCode,Date testDate
			,String testInChargeName,String testInChargeCode,String devConfirmorName,String devConfirmorCode,String version,Date onlineDate,String onlineInChargeName,String onlineInChargeCode
			,String shopCode,String posMachineCode,String suppilerCode,String suppilerName,Date assignMenuDateStart,String assignMenuTimeStart
			,String supportNo,Date executeDateStart,String executeTimeStart,Date executeDateEnd,String executeTimeEnd,String executeMemo
			,String executeInCharge,String adGroupCode,String adMemberCode,String adMemberName) {
		
        this.shopCode=shopCode;
        this.posMachineCode=posMachineCode;
        this.suppilerCode=suppilerCode;
        this.suppilerName=suppilerName;
        this.assignMenuDateStart=assignMenuDateStart;
        this.assignMenuTimeStart=assignMenuTimeStart;
        this.supportNo=supportNo;
        this.executeDateStart=executeDateStart;
        this.executeTimeStart=executeTimeStart;
        this.executeDateEnd=executeDateEnd;
        this.executeTimeEnd=executeTimeEnd;
        this.executeMemo=executeMemo;
        this.executeInCharge=executeInCharge;
		
		this.headId = headId;
		this.indexNo = indexNo;
		this.lineId = lineId;
		this.itemNo = itemNo;
		this.itemName = itemName;
		this.specInfo = specInfo;
		this.quantity = quantity;
		this.purUnitPrice = purUnitPrice;
		this.purTotalAmount = purTotalAmount;
		this.supplier = supplier;
		this.status = status;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.assetsNo = assetsNo;
		this.buPurchaseHead = buPurchaseHead;
		this.reUnitPrice = reUnitPrice;
		this.reTotalAmount = reTotalAmount;
		this.taxEnable = taxEnable;
		this.isDeleteRecord=isDeleteRecord;
		this.categoryCode=categoryCode;
		this.enable =enable;
		
		this.adGroupCode=adGroupCode;
		
		this.adMemberCode=adMemberCode;
		this.adMemberName=adMemberName;
		
		this.saDate=saDate;
		this.saInChargeCode=saInChargeCode;
		this.saInChargeName=saInChargeName;
		this.devDate=devDate;
		this.devInChargeName=devInChargeName;
		this.devInChargeCode=devInChargeCode;
		this.devConfirmorName=devConfirmorName;
		this.devConfirmorCode=devConfirmorCode;
		this.testDate=testDate;
		this.testInChargeName=testInChargeName;
		this.testInChargeCode=testInChargeCode;
		this.version=version;
		this.onlineDate=onlineDate;
		this.onlineInChargeName=onlineInChargeName;
		this.onlineInChargeCode=onlineInChargeCode;
		
		
		
	}

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public String getItemNo() {
		return this.itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSpecInfo() {
		return this.specInfo;
	}

	public void setSpecInfo(String specInfo) {
		this.specInfo = specInfo;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getPurUnitPrice() {
		return this.purUnitPrice;
	}

	public void setPurUnitPrice(Integer purUnitPrice) {
		this.purUnitPrice = purUnitPrice;
	}

	public Integer getPurTotalAmount() {
		return this.purTotalAmount;
	}

	public void setPurTotalAmount(Integer purTotalAmount) {
		this.purTotalAmount = purTotalAmount;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdatedByName() {
		return lastUpdatedByName;
	}

	public void setLastUpdatedByName(String lastUpdatedByName) {
		this.lastUpdatedByName = lastUpdatedByName;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getCreatedBy() {
		return this.lastUpdatedBy;
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

	public String getAssetsNo() {
		return this.assetsNo;
	}

	public void setAssetsNo(String assetsNo) {
		this.assetsNo = assetsNo;
	}

	public BuPurchaseHead getBuPurchaseHead() {
		return this.buPurchaseHead;
	}

	public void setBuPurchaseHead(BuPurchaseHead buPurchaseHead) {
		this.buPurchaseHead = buPurchaseHead;
	}

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public Integer getReUnitPrice() {
		return reUnitPrice;
	}

	public void setReUnitPrice(Integer reUnitPrice) {
		this.reUnitPrice = reUnitPrice;
	}

	public Integer getReTotalAmount() {
		return reTotalAmount;
	}

	public void setReTotalAmount(Integer reTotalAmount) {
		this.reTotalAmount = reTotalAmount;
	}

	public String getTaxEnable() {
		return taxEnable;
	}

	public void setTaxEnable(String taxEnable) {
		this.taxEnable = taxEnable;
	}

	public String getIsDeleteRecord() {
		return isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}


	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public Date getSaDate() {
		return saDate;
	}

	public void setSaDate(Date saDate) {
		this.saDate = saDate;
	}

	public String getSaInChargeCode() {
		return saInChargeCode;
	}

	public void setSaInChargeCode(String saInChargeCode) {
		this.saInChargeCode = saInChargeCode;
	}

	public String getSaInChargeName() {
		return saInChargeName;
	}

	public void setSaInChargeName(String saInChargeName) {
		this.saInChargeName = saInChargeName;
	}

	public Date getDevDate() {
		return devDate;
	}

	public void setDevDate(Date devDate) {
		this.devDate = devDate;
	}

	public String getDevInChargeName() {
		return devInChargeName;
	}

	public void setDevInChargeName(String devInChargeName) {
		this.devInChargeName = devInChargeName;
	}

	public String getDevInChargeCode() {
		return devInChargeCode;
	}

	public void setDevInChargeCode(String devInChargeCode) {
		this.devInChargeCode = devInChargeCode;
	}

	public String getDevConfirmorName() {
		return devConfirmorName;
	}

	public void setDevConfirmorName(String devConfirmorName) {
		this.devConfirmorName = devConfirmorName;
	}

	public String getDevConfirmorCode() {
		return devConfirmorCode;
	}

	public void setDevConfirmorCode(String devConfirmorCode) {
		this.devConfirmorCode = devConfirmorCode;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public String getTestInChargeName() {
		return testInChargeName;
	}

	public void setTestInChargeName(String testInChargeName) {
		this.testInChargeName = testInChargeName;
	}

	public String getTestInChargeCode() {
		return testInChargeCode;
	}

	public void setTestInChargeCode(String testInChargeCode) {
		this.testInChargeCode = testInChargeCode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getOnlineDate() {
		return onlineDate;
	}

	public void setOnlineDate(Date onlineDate) {
		this.onlineDate = onlineDate;
	}

	public String getOnlineInChargeName() {
		return onlineInChargeName;
	}

	public void setOnlineInChargeName(String onlineInChargeName) {
		this.onlineInChargeName = onlineInChargeName;
	}

	public String getOnlineInChargeCode() {
		return onlineInChargeCode;
	}

	public void setOnlineInChargeCode(String onlineInChargeCode) {
		this.onlineInChargeCode = onlineInChargeCode;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getPosMachineCode() {
		return posMachineCode;
	}

	public void setPosMachineCode(String posMachineCode) {
		this.posMachineCode = posMachineCode;
	}

	public String getSuppilerCode() {
		return suppilerCode;
	}

	public void setSuppilerCode(String suppilerCode) {
		this.suppilerCode = suppilerCode;
	}

	public String getSuppilerName() {
		return suppilerName;
	}

	public void setSuppilerName(String suppilerName) {
		this.suppilerName = suppilerName;
	}

	public Date getAssignMenuDateStart() {
		return assignMenuDateStart;
	}

	public void setAssignMenuDateStart(Date assignMenuDateStart) {
		this.assignMenuDateStart = assignMenuDateStart;
	}

	public String getAssignMenuTimeStart() {
		return assignMenuTimeStart;
	}

	public void setAssignMenuTimeStart(String assignMenuTimeStart) {
		this.assignMenuTimeStart = assignMenuTimeStart;
	}

	public String getSupportNo() {
		return supportNo;
	}

	public void setSupportNo(String supportNo) {
		this.supportNo = supportNo;
	}

	public Date getExecuteDateStart() {
		return executeDateStart;
	}

	public void setExecuteDateStart(Date executeDateStart) {
		this.executeDateStart = executeDateStart;
	}

	public String getExecuteTimeStart() {
		return executeTimeStart;
	}

	public void setExecuteTimeStart(String executeTimeStart) {
		this.executeTimeStart = executeTimeStart;
	}

	public Date getExecuteDateEnd() {
		return executeDateEnd;
	}

	public void setExecuteDateEnd(Date executeDateEnd) {
		this.executeDateEnd = executeDateEnd;
	}

	public String getExecuteTimeEnd() {
		return executeTimeEnd;
	}

	public void setExecuteTimeEnd(String executeTimeEnd) {
		this.executeTimeEnd = executeTimeEnd;
	}

	public String getExecuteMemo() {
		return executeMemo;
	}

	
	public void setExecuteMemo(String executeMemo) {
		this.executeMemo = executeMemo;
	}

	public String getExecuteInCharge() {
		return executeInCharge;
	}

	public void setExecuteInCharge(String executeInCharge) {
		this.executeInCharge = executeInCharge;
	}

	public String getAdGroupCode() {
		return adGroupCode;
	}

	public void setAdGroupCode(String adGroupCode) {
		this.adGroupCode = adGroupCode;
	}

	public String getAdMemberCode() {
		return adMemberCode;
	}

	public void setAdMemberCode(String adMemberCode) {
		this.adMemberCode = adMemberCode;
	}

	public String getAdMemberName() {
		return adMemberName;
	}

	public void setAdMemberName(String adMemberName) {
		this.adMemberName = adMemberName;
	}

	public String getTaskInchargeCode() {
		return taskInchargeCode;
	}

	public void setTaskInchargeCode(String taskInchargeCode) {
		this.taskInchargeCode = taskInchargeCode;
	}

	public String getTaskInchargeName() {
		return taskInchargeName;
	}

	public void setTaskInchargeName(String taskInchargeName) {
		this.taskInchargeName = taskInchargeName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	public String getTaskHours() {
		return taskHours;
	}

	public void setTaskHours(String taskHours) {
		this.taskHours = taskHours;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	public Long getExecuteHours() {

		return executeHours;
	}


	public void setExecuteHours(Long executeHours) {

		this.executeHours = executeHours;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public Double getLinePriority(){
		return linePriority;
	}
	
	public void setLinePriority(Double linePriority){
		this.linePriority = linePriority;
	}

	public Double getExecHours() {
	    return execHours;
	}

	public void setExecHours(Double execHours) {
	    this.execHours = execHours;
	}

	public String getYearBudget() {
	    return yearBudget;
	}

	public void setYearBudget(String yearBudget) {
	    this.yearBudget = yearBudget;
	}

	public String getTaskTypeNo() {
	    return taskTypeNo;
	}

	public void setTaskTypeNo(String taskTypeNo) {
	    this.taskTypeNo = taskTypeNo;
	}

	public Long getBoxCapacity() {
		return boxCapacity;
	}

	public void setBoxCapacity(Long boxCapacity) {
		this.boxCapacity = boxCapacity;
	}

	public Long getBox() {
		return box;
	}

	public void setBox(Long box) {
		this.box = box;
	}

	public Integer getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}


}

// Property accessors

