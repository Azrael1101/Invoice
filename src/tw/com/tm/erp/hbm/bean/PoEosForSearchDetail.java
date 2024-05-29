package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SoDeliveryHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PoEosForSearchDetail implements java.io.Serializable {
	private static final long serialVersionUID = 1821506305223333867L;
	// Fields
	/**
	 * 
	 */
	private Long headId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private String request;
	private Date requestDate;
	private String classification;
	private String project;
	private String status;
	private String department;
	private String depName;
	private String description;
	private String depManager;
	private String createdBy;
	private Date creationDate;
	private Date lastUpdateDate;
	private String lastUpdatedBy;
	private String no;
	private String lastUpdatedByName;   // 暫時欄位 更新人員
	private String requestCode;
	private String contractTel;
	private String enFristName;
	private String enName;
	private String depManagerName;
	private String requestSource;
	private String categorySystem;
	private String categoryItem;
	private String categoryGroup;
	private String hisstoryInfo;
	private Date estimateStartDare;
	private Date estimateEndDare;
	private String otherGroup;
	private String processDescription;
	private String rqInChargeCode;
	private String createdByName;
	private Long processId;
	private String statusLog;

	private String costControl;;
	private String warehouseControl;
	private String applicant;;
	private String role;

	private Double priority;
	private Integer totalHours;
	
	private Long projectId;
	private String special;
	private Long assignmentId;

	
	
////////////////////////////////////////////////////////////////
	
	private Long lineId;
	private Long indexNo;
	private String itemNo;
	private String itemName;
	private String specInfo;
	private Integer quantity;
	private Integer purUnitPrice;
	private Integer purTotalAmount;
	private String supplier;
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


	/** default constructor */
	public PoEosForSearchDetail() {
	}


	public Long getHeadId() {
		return headId;
	}


	public void setHeadId(Long headId) {
		this.headId = headId;
	}


	public String getBrandCode() {
		return brandCode;
	}


	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}


	public String getOrderTypeCode() {
		return orderTypeCode;
	}


	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}


	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public String getRequest() {
		return request;
	}


	public void setRequest(String request) {
		this.request = request;
	}


	public Date getRequestDate() {
		return requestDate;
	}


	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}


	public String getClassification() {
		return classification;
	}


	public void setClassification(String classification) {
		this.classification = classification;
	}


	public String getProject() {
		return project;
	}


	public void setProject(String project) {
		this.project = project;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public String getDepName() {
		return depName;
	}


	public void setDepName(String depName) {
		this.depName = depName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getDepManager() {
		return depManager;
	}


	public void setDepManager(String depManager) {
		this.depManager = depManager;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}


	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}


	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}


	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}


	public String getNo() {
		return no;
	}


	public void setNo(String no) {
		this.no = no;
	}


	public String getLastUpdatedByName() {
		return lastUpdatedByName;
	}


	public void setLastUpdatedByName(String lastUpdatedByName) {
		this.lastUpdatedByName = lastUpdatedByName;
	}


	public String getRequestCode() {
		return requestCode;
	}


	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}


	public String getContractTel() {
		return contractTel;
	}


	public void setContractTel(String contractTel) {
		this.contractTel = contractTel;
	}


	public String getEnFristName() {
		return enFristName;
	}


	public void setEnFristName(String enFristName) {
		this.enFristName = enFristName;
	}


	public String getEnName() {
		return enName;
	}


	public void setEnName(String enName) {
		this.enName = enName;
	}


	public String getDepManagerName() {
		return depManagerName;
	}


	public void setDepManagerName(String depManagerName) {
		this.depManagerName = depManagerName;
	}


	public String getRequestSource() {
		return requestSource;
	}


	public void setRequestSource(String requestSource) {
		this.requestSource = requestSource;
	}


	public String getCategorySystem() {
		return categorySystem;
	}


	public void setCategorySystem(String categorySystem) {
		this.categorySystem = categorySystem;
	}


	public String getCategoryItem() {
		return categoryItem;
	}


	public void setCategoryItem(String categoryItem) {
		this.categoryItem = categoryItem;
	}


	public String getCategoryGroup() {
		return categoryGroup;
	}


	public void setCategoryGroup(String categoryGroup) {
		this.categoryGroup = categoryGroup;
	}


	public String getHisstoryInfo() {
		return hisstoryInfo;
	}


	public void setHisstoryInfo(String hisstoryInfo) {
		this.hisstoryInfo = hisstoryInfo;
	}


	public Date getEstimateStartDare() {
		return estimateStartDare;
	}


	public void setEstimateStartDare(Date estimateStartDare) {
		this.estimateStartDare = estimateStartDare;
	}


	public Date getEstimateEndDare() {
		return estimateEndDare;
	}


	public void setEstimateEndDare(Date estimateEndDare) {
		this.estimateEndDare = estimateEndDare;
	}


	public String getOtherGroup() {
		return otherGroup;
	}


	public void setOtherGroup(String otherGroup) {
		this.otherGroup = otherGroup;
	}


	public String getProcessDescription() {
		return processDescription;
	}


	public void setProcessDescription(String processDescription) {
		this.processDescription = processDescription;
	}


	public String getRqInChargeCode() {
		return rqInChargeCode;
	}


	public void setRqInChargeCode(String rqInChargeCode) {
		this.rqInChargeCode = rqInChargeCode;
	}


	public String getCreatedByName() {
		return createdByName;
	}


	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}


	public Long getProcessId() {
		return processId;
	}


	public void setProcessId(Long processId) {
		this.processId = processId;
	}


	public String getStatusLog() {
		return statusLog;
	}


	public void setStatusLog(String statusLog) {
		this.statusLog = statusLog;
	}


	public String getCostControl() {
		return costControl;
	}


	public void setCostControl(String costControl) {
		this.costControl = costControl;
	}


	public String getWarehouseControl() {
		return warehouseControl;
	}


	public void setWarehouseControl(String warehouseControl) {
		this.warehouseControl = warehouseControl;
	}


	public String getApplicant() {
		return applicant;
	}


	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public Double getPriority() {
		return priority;
	}


	public void setPriority(Double priority) {
		this.priority = priority;
	}


	public Integer getTotalHours() {
		return totalHours;
	}


	public void setTotalHours(Integer totalHours) {
		this.totalHours = totalHours;
	}


	public Long getProjectId() {
		return projectId;
	}


	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}


	public String getSpecial() {
		return special;
	}


	public void setSpecial(String special) {
		this.special = special;
	}


	public Long getAssignmentId() {
		return assignmentId;
	}


	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}


	public Long getLineId() {
		return lineId;
	}


	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}


	public Long getIndexNo() {
		return indexNo;
	}


	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}


	public String getItemNo() {
		return itemNo;
	}


	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}


	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public String getSpecInfo() {
		return specInfo;
	}


	public void setSpecInfo(String specInfo) {
		this.specInfo = specInfo;
	}


	public Integer getQuantity() {
		return quantity;
	}


	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}


	public Integer getPurUnitPrice() {
		return purUnitPrice;
	}


	public void setPurUnitPrice(Integer purUnitPrice) {
		this.purUnitPrice = purUnitPrice;
	}


	public Integer getPurTotalAmount() {
		return purTotalAmount;
	}


	public void setPurTotalAmount(Integer purTotalAmount) {
		this.purTotalAmount = purTotalAmount;
	}


	public String getSupplier() {
		return supplier;
	}


	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}


	public String getAssetsNo() {
		return assetsNo;
	}


	public void setAssetsNo(String assetsNo) {
		this.assetsNo = assetsNo;
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


	public String getCategoryCode() {
		return categoryCode;
	}


	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}


	public BuPurchaseHead getBuPurchaseHead() {
		return buPurchaseHead;
	}


	public void setBuPurchaseHead(BuPurchaseHead buPurchaseHead) {
		this.buPurchaseHead = buPurchaseHead;
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


	public String getTaskTypeNo() {
		return taskTypeNo;
	}


	public void setTaskTypeNo(String taskTypeNo) {
		this.taskTypeNo = taskTypeNo;
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


	public Double getLinePriority() {
		return linePriority;
	}


	public void setLinePriority(Double linePriority) {
		this.linePriority = linePriority;
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


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
