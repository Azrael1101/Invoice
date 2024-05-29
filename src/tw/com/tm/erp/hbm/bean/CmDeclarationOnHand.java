package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * CmDeclarationOnHand entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class CmDeclarationOnHand implements java.io.Serializable {

    private static final long serialVersionUID = 472942192560383156L;
    // Fields
    private String declarationNo;
    private Long declarationSeq;
    private String customsItemCode;
    private String customsWarehouseCode;
    private String itemCode;
    private String warehouseCode;
    private Double onHandQuantity;
    private Double outUncommitQty;
    private Double inUncommitQty;
    private Double moveUncommitQty;
    private Double otherUncommitQty;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private String status;
    private String brandCode;
    private Double blockOnHandQuantity; // 鎖住庫存
    private String description; // 備註
    private Double unblockOnHandQuantity; // 反確認需將庫存再度鎖住
    private Date expiryDate;

    // Constructors

    /** default constructor */
    public CmDeclarationOnHand() {
    }

	/**
	 * @param declarationNo
	 * @param declarationSeq
	 * @param customsItemCode
	 * @param customsWarehouseCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param onHandQuantity
	 * @param outUncommitQty
	 * @param inUncommitQty
	 * @param moveUncommitQty
	 * @param otherUncommitQty
	 * @param reserve1
	 * @param reserve2
	 * @param reserve3
	 * @param reserve4
	 * @param reserve5
	 * @param createdBy
	 * @param creationDate
	 * @param lastUpdatedBy
	 * @param lastUpdateDate
	 * @param status
	 * @param brandCode
	 * @param blockOnHandQuantity
	 * @param description
	 * @param unblockOnHandQuantity
	 * @param expiryDate
	 */
	public CmDeclarationOnHand(String declarationNo, Long declarationSeq, String customsItemCode,
			String customsWarehouseCode, String itemCode, String warehouseCode, Double onHandQuantity,
			Double outUncommitQty, Double inUncommitQty, Double moveUncommitQty, Double otherUncommitQty, String reserve1,
			String reserve2, String reserve3, String reserve4, String reserve5, String createdBy, Date creationDate,
			String lastUpdatedBy, Date lastUpdateDate, String status, String brandCode, Double blockOnHandQuantity,
			String description, Double unblockOnHandQuantity, Date expiryDate) {
		super();
		this.declarationNo = declarationNo;
		this.declarationSeq = declarationSeq;
		this.customsItemCode = customsItemCode;
		this.customsWarehouseCode = customsWarehouseCode;
		this.itemCode = itemCode;
		this.warehouseCode = warehouseCode;
		this.onHandQuantity = onHandQuantity;
		this.outUncommitQty = outUncommitQty;
		this.inUncommitQty = inUncommitQty;
		this.moveUncommitQty = moveUncommitQty;
		this.otherUncommitQty = otherUncommitQty;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.status = status;
		this.brandCode = brandCode;
		this.blockOnHandQuantity = blockOnHandQuantity;
		this.description = description;
		this.unblockOnHandQuantity = unblockOnHandQuantity;
		this.expiryDate = expiryDate;
	}

	/**
	 * @return the declarationNo
	 */
	public String getDeclarationNo() {
		return declarationNo;
	}

	/**
	 * @param declarationNo the declarationNo to set
	 */
	public void setDeclarationNo(String declarationNo) {
		this.declarationNo = declarationNo;
	}

	/**
	 * @return the declarationSeq
	 */
	public Long getDeclarationSeq() {
		return declarationSeq;
	}

	/**
	 * @param declarationSeq the declarationSeq to set
	 */
	public void setDeclarationSeq(Long declarationSeq) {
		this.declarationSeq = declarationSeq;
	}

	/**
	 * @return the customsItemCode
	 */
	public String getCustomsItemCode() {
		return customsItemCode;
	}

	/**
	 * @param customsItemCode the customsItemCode to set
	 */
	public void setCustomsItemCode(String customsItemCode) {
		this.customsItemCode = customsItemCode;
	}

	/**
	 * @return the customsWarehouseCode
	 */
	public String getCustomsWarehouseCode() {
		return customsWarehouseCode;
	}

	/**
	 * @param customsWarehouseCode the customsWarehouseCode to set
	 */
	public void setCustomsWarehouseCode(String customsWarehouseCode) {
		this.customsWarehouseCode = customsWarehouseCode;
	}

	/**
	 * @return the itemCode
	 */
	public String getItemCode() {
		return itemCode;
	}

	/**
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * @return the warehouseCode
	 */
	public String getWarehouseCode() {
		return warehouseCode;
	}

	/**
	 * @param warehouseCode the warehouseCode to set
	 */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	/**
	 * @return the onHandQuantity
	 */
	public Double getOnHandQuantity() {
		return onHandQuantity;
	}

	/**
	 * @param onHandQuantity the onHandQuantity to set
	 */
	public void setOnHandQuantity(Double onHandQuantity) {
		this.onHandQuantity = onHandQuantity;
	}

	/**
	 * @return the outUncommitQty
	 */
	public Double getOutUncommitQty() {
		return outUncommitQty;
	}

	/**
	 * @param outUncommitQty the outUncommitQty to set
	 */
	public void setOutUncommitQty(Double outUncommitQty) {
		this.outUncommitQty = outUncommitQty;
	}

	/**
	 * @return the inUncommitQty
	 */
	public Double getInUncommitQty() {
		return inUncommitQty;
	}

	/**
	 * @param inUncommitQty the inUncommitQty to set
	 */
	public void setInUncommitQty(Double inUncommitQty) {
		this.inUncommitQty = inUncommitQty;
	}

	/**
	 * @return the moveUncommitQty
	 */
	public Double getMoveUncommitQty() {
		return moveUncommitQty;
	}

	/**
	 * @param moveUncommitQty the moveUncommitQty to set
	 */
	public void setMoveUncommitQty(Double moveUncommitQty) {
		this.moveUncommitQty = moveUncommitQty;
	}

	/**
	 * @return the otherUncommitQty
	 */
	public Double getOtherUncommitQty() {
		return otherUncommitQty;
	}

	/**
	 * @param otherUncommitQty the otherUncommitQty to set
	 */
	public void setOtherUncommitQty(Double otherUncommitQty) {
		this.otherUncommitQty = otherUncommitQty;
	}

	/**
	 * @return the reserve1
	 */
	public String getReserve1() {
		return reserve1;
	}

	/**
	 * @param reserve1 the reserve1 to set
	 */
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	/**
	 * @return the reserve2
	 */
	public String getReserve2() {
		return reserve2;
	}

	/**
	 * @param reserve2 the reserve2 to set
	 */
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	/**
	 * @return the reserve3
	 */
	public String getReserve3() {
		return reserve3;
	}

	/**
	 * @param reserve3 the reserve3 to set
	 */
	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	/**
	 * @return the reserve4
	 */
	public String getReserve4() {
		return reserve4;
	}

	/**
	 * @param reserve4 the reserve4 to set
	 */
	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	/**
	 * @return the reserve5
	 */
	public String getReserve5() {
		return reserve5;
	}

	/**
	 * @param reserve5 the reserve5 to set
	 */
	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the lastUpdatedBy
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/**
	 * @return the lastUpdateDate
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	/**
	 * @param lastUpdateDate the lastUpdateDate to set
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the brandCode
	 */
	public String getBrandCode() {
		return brandCode;
	}

	/**
	 * @param brandCode the brandCode to set
	 */
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	/**
	 * @return the blockOnHandQuantity
	 */
	public Double getBlockOnHandQuantity() {
		return blockOnHandQuantity;
	}

	/**
	 * @param blockOnHandQuantity the blockOnHandQuantity to set
	 */
	public void setBlockOnHandQuantity(Double blockOnHandQuantity) {
		this.blockOnHandQuantity = blockOnHandQuantity;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the unblockOnHandQuantity
	 */
	public Double getUnblockOnHandQuantity() {
		return unblockOnHandQuantity;
	}

	/**
	 * @param unblockOnHandQuantity the unblockOnHandQuantity to set
	 */
	public void setUnblockOnHandQuantity(Double unblockOnHandQuantity) {
		this.unblockOnHandQuantity = unblockOnHandQuantity;
	}
	
	/**
	 * 
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}
	
	/**
	 * 
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate){
		this.expiryDate = expiryDate;
	}
	
	

}