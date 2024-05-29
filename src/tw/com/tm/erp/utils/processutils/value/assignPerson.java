package tw.com.tm.erp.utils.processutils.value;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class assignPerson implements Serializable {

    /** serialVersionUID field */
    private static final long serialVersionUID = 5243446439435139508L; 

    /** identifier field */
    private Long id;

    /** nullable persistent field */
    private String assignCode;

    /** nullable persistent field */
    private String partyType;

    /** nullable persistent field */
    private int seqOrder;

    /** nullable persistent field */
    private String assignType;

    /** nullable persistent field */
    private long processId;

    /** nullable persistent field */
    private String domain;

    /** full constructor */
    public assignPerson(java.lang.String assignCode, java.lang.String partyType, int seqOrder, java.lang.String assignType, long processId, java.lang.String domain) {
        this.assignCode = assignCode;
        this.partyType = partyType;
        this.seqOrder = seqOrder;
        this.assignType = assignType;
        this.processId = processId;
        this.domain = domain;
    }

    /** default constructor */
    public assignPerson() {
    }

    public java.lang.Long getId() {
        return this.id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getAssignCode() {
        return this.assignCode;
    }

    public void setAssignCode(java.lang.String assignCode) {
        this.assignCode = assignCode;
    }

    public java.lang.String getPartyType() {
        return this.partyType;
    }

    public void setPartyType(java.lang.String partyType) {
        this.partyType = partyType;
    }

    public int getSeqOrder() {
        return this.seqOrder;
    }

    public void setSeqOrder(int seqOrder) {
        this.seqOrder = seqOrder;
    }

    public java.lang.String getAssignType() {
        return this.assignType;
    }

    public void setAssignType(java.lang.String assignType) {
        this.assignType = assignType;
    }

    public long getProcessId() {
        return this.processId;
    }

    public void setProcessId(long processId) {
        this.processId = processId;
    }

    public java.lang.String getDomain() {
        return this.domain;
    }

    public void setDomain(java.lang.String domain) {
        this.domain = domain;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id\n", getId())
            .append("assignCode\n", getAssignCode())
            .append("partyType\n", getPartyType())
            .append("seqOrder\n", getSeqOrder())
            .append("assignType\n", getAssignType())
            .append("processId\n", getProcessId())
            .append("domain\n", getDomain())

            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof assignPerson) ) return false;
        assignPerson castOther = (assignPerson) other;
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
