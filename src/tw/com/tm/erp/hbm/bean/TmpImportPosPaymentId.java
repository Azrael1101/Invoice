package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * TmpImportPosPaymentId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TmpImportPosPaymentId implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5579325071691795258L;
    // Fields
    private Date salesOrderDate;
    private String posMachineCode;
    private String transactionSeqNo;
    private Long paySeq;

    // Constructors

    /** default constructor */
    public TmpImportPosPaymentId() {
    }

    /** full constructor */
    public TmpImportPosPaymentId(Date salesOrderDate, String posMachineCode,
	    String transactionSeqNo, Long paySeq) {
	this.salesOrderDate = salesOrderDate;
	this.posMachineCode = posMachineCode;
	this.transactionSeqNo = transactionSeqNo;
	this.paySeq = paySeq;
    }

    // Property accessors

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

    public Long getPaySeq() {
	return this.paySeq;
    }

    public void setPaySeq(Long paySeq) {
	this.paySeq = paySeq;
    }

    public boolean equals(Object other) {
	if ((this == other))
	    return true;
	if ((other == null))
	    return false;
	if (!(other instanceof TmpImportPosPaymentId))
	    return false;
	TmpImportPosPaymentId castOther = (TmpImportPosPaymentId) other;

	return ((this.getSalesOrderDate() == castOther.getSalesOrderDate()) || (this
		.getSalesOrderDate() != null
		&& castOther.getSalesOrderDate() != null && this
		.getSalesOrderDate().equals(castOther.getSalesOrderDate())))
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
		&& ((this.getPaySeq() == castOther.getPaySeq()) || (this
			.getPaySeq() != null
			&& castOther.getPaySeq() != null && this.getPaySeq()
			.equals(castOther.getPaySeq())));
    }

    public int hashCode() {
	int result = 17;

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
		+ (getPaySeq() == null ? 0 : this.getPaySeq().hashCode());
	return result;
    }

}