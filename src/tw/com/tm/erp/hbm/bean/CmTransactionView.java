package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * CmTransactionId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CmTransactionView implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7835701152040071284L;
    // Fields
    private String io;
    private String gdType;
    private String ioType;
    private String d8Type;
    private String declType;
    private String declarationNo;
    private Long declarationSeq;
    private Date declarationDate;
    private Double declQty;
    private Date transactionDate;
    private String itemCode;
    private String customsWarehouseCode;
    private Long qty;
    private String orderTypeCode;
    private String orderNo;
    private Long indexNo;
    private Date lastUpdateDate;
    private String originalDeclarationNo;
    private Long originalDeclarationSeq;
    private Date originalDeclarationDate;
    private String adjustmentType;

    private String cmMemo;
    
    // Constructors

    /** default constructor */
    public CmTransactionView() {
    }

    /** full constructor */
    public CmTransactionView(String declType, String declarationNo,
	    Long declarationSeq, Date declarationDate, Double declQty,
	    Date transactionDate, String itemCode, String customsWarehouseCode,
	    Long qty, String orderTypeCode, String orderNo, Long indexNo,
	    Date lastUpdateDate, String originalDeclarationNo,
	    Long originalDeclarationSeq, Date originalDeclarationDate,
	    String adjustmentType) {
	this.declType = declType;
	this.declarationNo = declarationNo;
	this.declarationSeq = declarationSeq;
	this.declarationDate = declarationDate;
	this.declQty = declQty;
	this.transactionDate = transactionDate;
	this.itemCode = itemCode;
	this.customsWarehouseCode = customsWarehouseCode;
	this.qty = qty;
	this.orderTypeCode = orderTypeCode;
	this.orderNo = orderNo;
	this.indexNo = indexNo;
	this.lastUpdateDate = lastUpdateDate;
	this.originalDeclarationNo = originalDeclarationNo;
	this.originalDeclarationSeq = originalDeclarationSeq;
	this.originalDeclarationDate = originalDeclarationDate;
	this.adjustmentType = adjustmentType;
    }

    // Property accessors

    public String getDeclType() {
	return this.declType;
    }

    public void setDeclType(String declType) {
	this.declType = declType;
    }

    public String getDeclarationNo() {
	return this.declarationNo;
    }

    public void setDeclarationNo(String declarationNo) {
	this.declarationNo = declarationNo;
    }

    public Long getDeclarationSeq() {
	return this.declarationSeq;
    }

    public void setDeclarationSeq(Long declarationSeq) {
	this.declarationSeq = declarationSeq;
    }

    public Date getDeclarationDate() {
	return this.declarationDate;
    }

    public void setDeclarationDate(Date declarationDate) {
	this.declarationDate = declarationDate;
    }

    public Double getDeclQty() {
	return this.declQty;
    }

    public void setDeclQty(Double declQty) {
	this.declQty = declQty;
    }

    public Date getTransactionDate() {
	return this.transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
	this.transactionDate = transactionDate;
    }

    public String getItemCode() {
	return this.itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
    }

    public String getCustomsWarehouseCode() {
	return this.customsWarehouseCode;
    }

    public void setCustomsWarehouseCode(String customsWarehouseCode) {
	this.customsWarehouseCode = customsWarehouseCode;
    }

    public Long getQty() {
	return this.qty;
    }

    public void setQty(Long qty) {
	this.qty = qty;
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

    public Long getIndexNo() {
	return this.indexNo;
    }

    public void setIndexNo(Long indexNo) {
	this.indexNo = indexNo;
    }

    public Date getLastUpdateDate() {
	return this.lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
	this.lastUpdateDate = lastUpdateDate;
    }

    public String getOriginalDeclarationNo() {
	return this.originalDeclarationNo;
    }

    public void setOriginalDeclarationNo(String originalDeclarationNo) {
	this.originalDeclarationNo = originalDeclarationNo;
    }

    public Long getOriginalDeclarationSeq() {
	return this.originalDeclarationSeq;
    }

    public void setOriginalDeclarationSeq(Long originalDeclarationSeq) {
	this.originalDeclarationSeq = originalDeclarationSeq;
    }

    public Date getOriginalDeclarationDate() {
	return this.originalDeclarationDate;
    }

    public void setOriginalDeclarationDate(Date originalDeclarationDate) {
	this.originalDeclarationDate = originalDeclarationDate;
    }

    public String getAdjustmentType() {
	return this.adjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
	this.adjustmentType = adjustmentType;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((adjustmentType == null) ? 0 : adjustmentType.hashCode());
	result = prime
		* result
		+ ((customsWarehouseCode == null) ? 0 : customsWarehouseCode
			.hashCode());
	result = prime * result + ((declQty == null) ? 0 : declQty.hashCode());
	result = prime * result
		+ ((declType == null) ? 0 : declType.hashCode());
	result = prime * result
		+ ((declarationDate == null) ? 0 : declarationDate.hashCode());
	result = prime * result
		+ ((declarationNo == null) ? 0 : declarationNo.hashCode());
	result = prime * result
		+ ((declarationSeq == null) ? 0 : declarationSeq.hashCode());
	result = prime * result + ((indexNo == null) ? 0 : indexNo.hashCode());
	result = prime * result
		+ ((itemCode == null) ? 0 : itemCode.hashCode());
	result = prime * result
		+ ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
	result = prime * result + ((orderNo == null) ? 0 : orderNo.hashCode());
	result = prime * result
		+ ((orderTypeCode == null) ? 0 : orderTypeCode.hashCode());
	result = prime
		* result
		+ ((originalDeclarationDate == null) ? 0
			: originalDeclarationDate.hashCode());
	result = prime
		* result
		+ ((originalDeclarationNo == null) ? 0 : originalDeclarationNo
			.hashCode());
	result = prime
		* result
		+ ((originalDeclarationSeq == null) ? 0
			: originalDeclarationSeq.hashCode());
	result = prime * result + ((qty == null) ? 0 : qty.hashCode());
	result = prime * result
		+ ((transactionDate == null) ? 0 : transactionDate.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	final CmTransactionView other = (CmTransactionView) obj;
	if (adjustmentType == null) {
	    if (other.adjustmentType != null)
		return false;
	} else if (!adjustmentType.equals(other.adjustmentType))
	    return false;
	if (customsWarehouseCode == null) {
	    if (other.customsWarehouseCode != null)
		return false;
	} else if (!customsWarehouseCode.equals(other.customsWarehouseCode))
	    return false;
	if (declQty == null) {
	    if (other.declQty != null)
		return false;
	} else if (!declQty.equals(other.declQty))
	    return false;
	if (declType == null) {
	    if (other.declType != null)
		return false;
	} else if (!declType.equals(other.declType))
	    return false;
	if (declarationDate == null) {
	    if (other.declarationDate != null)
		return false;
	} else if (!declarationDate.equals(other.declarationDate))
	    return false;
	if (declarationNo == null) {
	    if (other.declarationNo != null)
		return false;
	} else if (!declarationNo.equals(other.declarationNo))
	    return false;
	if (declarationSeq == null) {
	    if (other.declarationSeq != null)
		return false;
	} else if (!declarationSeq.equals(other.declarationSeq))
	    return false;
	if (indexNo == null) {
	    if (other.indexNo != null)
		return false;
	} else if (!indexNo.equals(other.indexNo))
	    return false;
	if (itemCode == null) {
	    if (other.itemCode != null)
		return false;
	} else if (!itemCode.equals(other.itemCode))
	    return false;
	if (lastUpdateDate == null) {
	    if (other.lastUpdateDate != null)
		return false;
	} else if (!lastUpdateDate.equals(other.lastUpdateDate))
	    return false;
	if (orderNo == null) {
	    if (other.orderNo != null)
		return false;
	} else if (!orderNo.equals(other.orderNo))
	    return false;
	if (orderTypeCode == null) {
	    if (other.orderTypeCode != null)
		return false;
	} else if (!orderTypeCode.equals(other.orderTypeCode))
	    return false;
	if (originalDeclarationDate == null) {
	    if (other.originalDeclarationDate != null)
		return false;
	} else if (!originalDeclarationDate
		.equals(other.originalDeclarationDate))
	    return false;
	if (originalDeclarationNo == null) {
	    if (other.originalDeclarationNo != null)
		return false;
	} else if (!originalDeclarationNo.equals(other.originalDeclarationNo))
	    return false;
	if (originalDeclarationSeq == null) {
	    if (other.originalDeclarationSeq != null)
		return false;
	} else if (!originalDeclarationSeq.equals(other.originalDeclarationSeq))
	    return false;
	if (qty == null) {
	    if (other.qty != null)
		return false;
	} else if (!qty.equals(other.qty))
	    return false;
	if (transactionDate == null) {
	    if (other.transactionDate != null)
		return false;
	} else if (!transactionDate.equals(other.transactionDate))
	    return false;
	return true;
    }

    public String getIo() {
        return io;
    }

    public void setIo(String io) {
        this.io = io;
    }

    public String getGdType() {
        return gdType;
    }

    public void setGdType(String gdType) {
        this.gdType = gdType;
    }

    public String getIoType() {
        return ioType;
    }

    public void setIoType(String ioType) {
        this.ioType = ioType;
    }

    public String getD8Type() {
        return d8Type;
    }

    public void setD8Type(String type) {
        d8Type = type;
    }

    public String getCmMemo() {
        return cmMemo;
    }

    public void setCmMemo(String cmMemo) {
        this.cmMemo = cmMemo;
    }

}