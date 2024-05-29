package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * WfApprovalResult entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class WfApprovalResult implements java.io.Serializable {

	// Fields

	/**
	 *
	 */
	private static final long serialVersionUID = 8602469603075631121L;
	private Long id;
	private Long processId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private String formName;
	private String activityName;
	private Long activityId;
	private String approver;
	private String approverName;
	private String approverPosition;
	private Date dateTime;
	//private java.sql.Timestamp dateTime;
	private String result;
	private String approvalType;
	private String approvalComment;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private Long indexNo;
	
	// Constructors

	/** default constructor */
	public WfApprovalResult() {
	}

	/** full constructor */
	public WfApprovalResult(Long processId, String brandCode, String orderTypeCode, String orderNo,
			String formName, String activityName, Long activityId, String approver,
			Date dateTime, String result, String approvalType,
			String approvalComment, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5) {
		this.processId = processId;
		this.brandCode =brandCode;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.formName = formName;
		this.activityName = activityName;
		this.activityId = activityId;
		this.approver = approver;
		this.dateTime = dateTime;
		this.result = result;
		this.approvalType = approvalType;
		this.approvalComment = approvalComment;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
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

	public String getFormName() {
		return this.formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getActivityName() {
		return this.activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
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

	public String getApprovalType() {
		return this.approvalType;
	}

	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}

	public String getApprovalComment() {
		return this.approvalComment;
	}

	public void setApprovalComment(String approvalComment) {
		this.approvalComment = approvalComment;
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

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	
	public String getApproverPosition() {
		return approverPosition;
	}

	public void setApproverPosition(String approverPosition) {
		this.approverPosition = approverPosition;
	}

}