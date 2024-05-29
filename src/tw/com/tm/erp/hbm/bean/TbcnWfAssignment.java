package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SoDeliveryHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TbcnWfAssignment implements java.io.Serializable {



	/**
	 * 
	 */
	private static long serialVersionUID = -5184265302062181237L;
	/**
	 * 
	 */
	private Long assignmentId;
	private Long activityId;
	private String assigneeId;
	private Date fromDate;
	private Date thruDate;
	private Date lastStatusUpadtedDate;
	private String assigneeType;
	private String currentStatus;
	private String applicationId;
	private String result;
	private String subject;
	private Date detedLine;
	private String packageId;
	private String processDefId;
	private String processDefVersion;
	private String activityDefId; // 暫時欄位 更新人員
	private String processName;
	private Long processId;
	private String positionId;
	private String rootAssigneeId;
	private String rootAssigneeType;
	private TbcnWfAssignment tbcnWfAssignment;
	private Long priority;
	private String activityName;

	/** default constructor */
	public TbcnWfAssignment() {
		
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static void setSerialVersionUID(long serialVersionUID) {
		TbcnWfAssignment.serialVersionUID = serialVersionUID;
	}

	public Long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(String assigneeId) {
		this.assigneeId = assigneeId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getThruDate() {
		return thruDate;
	}

	public void setThruDate(Date thruDate) {
		this.thruDate = thruDate;
	}

	public Date getLastStatusUpadtedDate() {
		return lastStatusUpadtedDate;
	}

	public void setLastStatusUpadtedDate(Date lastStatusUpadtedDate) {
		this.lastStatusUpadtedDate = lastStatusUpadtedDate;
	}

	public String getAssigneeType() {
		return assigneeType;
	}

	public void setAssigneeType(String assigneeType) {
		this.assigneeType = assigneeType;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getDetedLine() {
		return detedLine;
	}

	public void setDetedLine(Date detedLine) {
		this.detedLine = detedLine;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getProcessDefId() {
		return processDefId;
	}

	public void setProcessDefId(String processDefId) {
		this.processDefId = processDefId;
	}

	public String getProcessDefVersion() {
		return processDefVersion;
	}

	public void setProcessDefVersion(String processDefVersion) {
		this.processDefVersion = processDefVersion;
	}

	public String getActivityDefId() {
		return activityDefId;
	}

	public void setActivityDefId(String activityDefId) {
		this.activityDefId = activityDefId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getRootAssigneeId() {
		return rootAssigneeId;
	}

	public void setRootAssigneeId(String rootAssigneeId) {
		this.rootAssigneeId = rootAssigneeId;
	}

	public String getRootAssigneeType() {
		return rootAssigneeType;
	}

	public void setRootAssigneeType(String rootAssigneeType) {
		this.rootAssigneeType = rootAssigneeType;
	}

	public TbcnWfAssignment getTbcnWfAssignment() {
		return tbcnWfAssignment;
	}

	public void setTbcnWfAssignment(TbcnWfAssignment tbcnWfAssignment) {
		this.tbcnWfAssignment = tbcnWfAssignment;
	}

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	


}

// Property accessors

