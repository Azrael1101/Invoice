package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ApprovalResult entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ApprovalResult implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -5593329701927739035L;

	private Long id;

	private Long processId;

	private String formName;

	private String nodeName;

	private Long activityId;

	private String approver;

	private Date dateTime;

	private String result;

	private String memo;

	private String approvalType;

	// Constructors

	/** default constructor */
	public ApprovalResult() {
	}

	/** minimal constructor */
	public ApprovalResult(Long id) {
		this.id = id;
	}

	/** full constructor */
	public ApprovalResult(Long id, Long processId, String formName,
			String nodeName, Long activityId, String approver, Date dateTime,
			String result, String memo, String approvalType) {
		this.id = id;
		this.processId = processId;
		this.formName = formName;
		this.nodeName = nodeName;
		this.activityId = activityId;
		this.approver = approver;
		this.dateTime = dateTime;
		this.result = result;
		this.memo = memo;
		this.approvalType = approvalType;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProcessId() {
		return this.processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getFormName() {
		return this.formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Long getActivityId() {
		return this.activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getApprover() {
		return this.approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public Date getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getApprovalType() {
		return this.approvalType;
	}

	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}

}