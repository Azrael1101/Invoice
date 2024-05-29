package tw.com.tm.erp.utils.processutils.value;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class approvalResult implements Serializable {

    /** serialVersionUID field */
    private static final long serialVersionUID = 3718625655528980595L; 

    /** identifier field */
    private Long id;

    /** nullable persistent field */
    private long processId;

    /** nullable persistent field */
    private String formName;

    /** nullable persistent field */
    private String nodeName;

    /** nullable persistent field */
    private long activityId;

    /** nullable persistent field */
    private String approver;

    /** nullable persistent field */
    private java.util.Date dateTime;

    /** nullable persistent field */
    private String result;

    /** nullable persistent field */
    private String memo;

    /** nullable persistent field */
    private String approvalType;

    /** full constructor */
    public approvalResult(long processId, java.lang.String formName, java.lang.String nodeName, long activityId, java.lang.String approver, java.util.Date dateTime, java.lang.String result, java.lang.String memo, java.lang.String approvalType) {
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

    /** default constructor */
    public approvalResult() {
    }

    public java.lang.Long getId() {
        return this.id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public long getProcessId() {
        return this.processId;
    }

    public void setProcessId(long processId) {
        this.processId = processId;
    }

    public java.lang.String getFormName() {
        return this.formName;
    }

    public void setFormName(java.lang.String formName) {
        this.formName = formName;
    }

    public java.lang.String getNodeName() {
        return this.nodeName;
    }

    public void setNodeName(java.lang.String nodeName) {
        this.nodeName = nodeName;
    }

    public long getActivityId() {
        return this.activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public java.lang.String getApprover() {
        return this.approver;
    }

    public void setApprover(java.lang.String approver) {
        this.approver = approver;
    }

    public java.util.Date getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(java.util.Date dateTime) {
        this.dateTime = dateTime;
    }

    public java.lang.String getResult() {
        return this.result;
    }

    public void setResult(java.lang.String result) {
        this.result = result;
    }

    public java.lang.String getMemo() {
        return this.memo;
    }

    public void setMemo(java.lang.String memo) {
        this.memo = memo;
    }

    public java.lang.String getApprovalType() {
        return this.approvalType;
    }

    public void setApprovalType(java.lang.String approvalType) {
        this.approvalType = approvalType;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id\n", getId())
            .append("processId\n", getProcessId())
            .append("formName\n", getFormName())
            .append("nodeName\n", getNodeName())
            .append("activityId\n", getActivityId())
            .append("approver\n", getApprover())
            .append("dateTime\n", getDateTime())
            .append("result\n", getResult())
            .append("memo\n", getMemo())
            .append("approvalType\n", getApprovalType())

            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof approvalResult) ) return false;
        approvalResult castOther = (approvalResult) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

}
