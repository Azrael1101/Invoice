package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * TmpImportPosItemId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TmpImportPosItemId implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6766890257767634214L;
    // Fields
    private String fileId;
    private Date salesOrderDate;
    private String posMachineCode;
    private String transactionSeqNo;
    private Long seq;

    // Constructors

    /** default constructor */
    public TmpImportPosItemId() {
    }

    /** full constructor */
    public TmpImportPosItemId(String fileId, Date salesOrderDate,
	    String posMachineCode, String transactionSeqNo, Long seq) {
	this.fileId = fileId;
	this.salesOrderDate = salesOrderDate;
	this.posMachineCode = posMachineCode;
	this.transactionSeqNo = transactionSeqNo;
	this.seq = seq;
    }

    // Property accessors

    public String getFileId() {
	return this.fileId;
    }

    public void setFileId(String fileId) {
	this.fileId = fileId;
    }

    public Date getSalesOrderDate() {
	return this.salesOrderDate;
    }

    public void setSalesOrderDate(Date salesOrderDate) {
	this.salesOrderDate = salesOrderDate;
    }

    public String getPosMachineCode() {
	return this.posMachineCode;
    }

    public void setPosMachineCode(String posMachineCode) {
	this.posMachineCode = posMachineCode;
    }

    public String getTransactionSeqNo() {
	return this.transactionSeqNo;
    }

    public void setTransactionSeqNo(String transactionSeqNo) {
	this.transactionSeqNo = transactionSeqNo;
    }

    public Long getSeq() {
	return this.seq;
    }

    public void setSeq(Long seq) {
	this.seq = seq;
    }

    public boolean equals(Object other) {
	if ((this == other))
	    return true;
	if ((other == null))
	    return false;
	if (!(other instanceof TmpImportPosItemId))
	    return false;
	TmpImportPosItemId castOther = (TmpImportPosItemId) other;

	return ((this.getFileId() == castOther.getFileId()) || (this
		.getFileId() != null
		&& castOther.getFileId() != null && this.getFileId().equals(
		castOther.getFileId())))
		&& ((this.getSalesOrderDate() == castOther.getSalesOrderDate()) || (this
			.getSalesOrderDate() != null
			&& castOther.getSalesOrderDate() != null && this
			.getSalesOrderDate().equals(
				castOther.getSalesOrderDate())))
		&& ((this.getPosMachineCode() == castOther.getPosMachineCode()) || (this
			.getPosMachineCode() != null
			&& castOther.getPosMachineCode() != null && this
			.getPosMachineCode().equals(
				castOther.getPosMachineCode())))
		&& ((this.getTransactionSeqNo() == castOther
			.getTransactionSeqNo()) || (this.getTransactionSeqNo() != null
			&& castOther.getTransactionSeqNo() != null && this
			.getTransactionSeqNo().equals(
				castOther.getTransactionSeqNo())))
		&& ((this.getSeq() == castOther.getSeq()) || (this.getSeq() != null
			&& castOther.getSeq() != null && this.getSeq().equals(
			castOther.getSeq())));
    }

    public int hashCode() {
	int result = 17;

	result = 37 * result
		+ (getFileId() == null ? 0 : this.getFileId().hashCode());
	result = 37
		* result
		+ (getSalesOrderDate() == null ? 0 : this.getSalesOrderDate()
			.hashCode());
	result = 37
		* result
		+ (getPosMachineCode() == null ? 0 : this.getPosMachineCode()
			.hashCode());
	result = 37
		* result
		+ (getTransactionSeqNo() == null ? 0 : this
			.getTransactionSeqNo().hashCode());
	result = 37 * result
		+ (getSeq() == null ? 0 : this.getSeq().hashCode());
	return result;
    }

}