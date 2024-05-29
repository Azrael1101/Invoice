package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * TmpExtendItemInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TmpExtendItemInfo implements java.io.Serializable {
 
    private static final long serialVersionUID = 8318445179012444951L;
    // Fields
    private String brandCode;
    private String itemCode;
    private String warehouseCode;
    private Long originalLineId;
    private String originalLotNo;
    private String originalDeclarationNo;
    private Long originalDeclarationSeq;
    private Double originalQuantity;
    private Long lineId;
    private String lotNo;
    private String declarationNo;
    private Long declarationSeq;
    private Date declarationDate;
    private Double quantity;
    private String message;
    private String status;
    private String declarationType;
    private Double perUnitAmount;

    // Constructors

    /** default constructor */
    public TmpExtendItemInfo() {
    }

    /** full constructor */
    public TmpExtendItemInfo(String brandCode, String itemCode,
	    String warehouseCode, Long originalLineId, String originalLotNo,
	    String originalDeclarationNo, Long originalDeclarationSeq,
	    Double originalQuantity, Long lineId, String lotNo,
	    String declarationNo, Long declarationSeq, Date declarationDate,
	    Double quantity, String message, String status, String declarationType, Double perUnitAmount) {
	this.brandCode = brandCode;
	this.itemCode = itemCode;
	this.warehouseCode = warehouseCode;
	this.originalLineId = originalLineId;
	this.originalLotNo = originalLotNo;
	this.originalDeclarationNo = originalDeclarationNo;
	this.originalDeclarationSeq = originalDeclarationSeq;
	this.originalQuantity = originalQuantity;
	this.lineId = lineId;
	this.lotNo = lotNo;
	this.declarationNo = declarationNo;
	this.declarationSeq = declarationSeq;
	this.declarationDate = declarationDate;
	this.quantity = quantity;
	this.message = message;
	this.status = status;
	this.declarationType = declarationType;
	this.perUnitAmount = perUnitAmount;
    }

    // Property accessors

    public String getBrandCode() {
	return this.brandCode;
    }

    public void setBrandCode(String brandCode) {
	this.brandCode = brandCode;
    }

    public String getItemCode() {
	return this.itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
    }

    public String getWarehouseCode() {
	return this.warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
	this.warehouseCode = warehouseCode;
    }

    public Long getOriginalLineId() {
	return this.originalLineId;
    }

    public void setOriginalLineId(Long originalLineId) {
	this.originalLineId = originalLineId;
    }

    public String getOriginalLotNo() {
	return this.originalLotNo;
    }

    public void setOriginalLotNo(String originalLotNo) {
	this.originalLotNo = originalLotNo;
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

    public Double getOriginalQuantity() {
	return this.originalQuantity;
    }

    public void setOriginalQuantity(Double originalQuantity) {
	this.originalQuantity = originalQuantity;
    }

    public Long getLineId() {
	return this.lineId;
    }

    public void setLineId(Long lineId) {
	this.lineId = lineId;
    }

    public String getLotNo() {
	return this.lotNo;
    }

    public void setLotNo(String lotNo) {
	this.lotNo = lotNo;
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

    public Double getQuantity() {
	return this.quantity;
    }

    public void setQuantity(Double quantity) {
	this.quantity = quantity;
    }

    public String getMessage() {
	return this.message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getStatus() {
	return this.status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getDeclarationType() {
        return declarationType;
    }

    public void setDeclarationType(String declarationType) {
        this.declarationType = declarationType;
    }

	public Double getPerUnitAmount() {
		return perUnitAmount;
	}

	public void setPerUnitAmount(Double perUnitAmount) {
		this.perUnitAmount = perUnitAmount;
	}
}