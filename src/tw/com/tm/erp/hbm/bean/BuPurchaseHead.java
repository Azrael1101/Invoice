package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SoDeliveryHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuPurchaseHead implements java.io.Serializable {
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
	private String categoryCode;


	//STEVE
	private List<BuPurchaseLine> buPurchaseLines = new ArrayList(0);
	private List<AdDetail> adDetails = new ArrayList(0);
	// Constructors

	/** default constructor */
	public BuPurchaseHead() {
	}

	/** full constructor */
	public BuPurchaseHead( String orderTypeCode,String orderNo, String brandCode, String request,
			Date requestDate, String classification,String project,String status,String department,String description,String depManager,String createdBy,Date creationDate,List<BuPurchaseLine> buPurchaseLines
			,String no,String contractTel
			,String enFristName,String enName,String requestCode,String depManagerName) 
	{
		this.brandCode=brandCode;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.request=request;
		this.requestDate=requestDate;
		this.classification=classification;
		this.project=project;
		this.status=status;
		this.department=department;
		this.description=description;
		this.depManager=depManager;
		this.createdBy=createdBy;
		this.creationDate=creationDate;
		this.no=no;
		this.buPurchaseLines=buPurchaseLines;
		this.requestCode=requestCode;
		this.contractTel=contractTel;
		this.enFristName=enFristName;
		this.enName=enFristName;
		this.depManagerName=depManagerName;
	}

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
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

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getRequest() {
		return this.request;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	
	public Date getRequestDate() {
		return this.requestDate;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getClassification() {
		return this.classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}
	
	public String getProject() {
		return this.project;
	}

	public void setProject(String project) {
		this.project = project;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getDepManager() {
		return this.depManager;
	}

	public void setDepManager(String depManager) {
		this.depManager = depManager;
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

	public List<BuPurchaseLine> getBuPurchaseLines() {
		return this.buPurchaseLines;
	}

	public void setBuPurchaseLines(List<BuPurchaseLine> buPurchaseLines) {
		this.buPurchaseLines = buPurchaseLines;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
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

	public String getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
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

	public void setDepName(String depName) {
		this.depName = depName;
	}
	
	public String getDepName() {
		return depName;
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



	public List<AdDetail> getAdDetails() {
		return adDetails;
	}

	public void setAdDetails(List<AdDetail> adDetails) {
		this.adDetails = adDetails;
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

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
}

