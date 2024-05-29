package tw.com.tm.erp.hbm.bean;

import java.util.Date;

public class ImStorageOnHand implements java.io.Serializable {

	private static final long serialVersionUID = 4822720092303810823L;
    private String organizationCode;
    private String brandCode;
    private String itemCode;
    private String warehouseCode;
    private String storageLotNo;
    private String storageInNo;
    private String storageCode;
    private Double stockOnHandQty;
    private Double outUncommitQty;
    private Double inUncommitQty;
    private Double moveUncommitQty;
    private Double otherUncommitQty;
    private Double blockQty;
    private Double tempQty;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private Double onHandQty;
    private String itemCName;

    public ImStorageOnHand (){
    	
    }
    
    public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getStorageLotNo() {
		return storageLotNo;
	}

	public void setStorageLotNo(String storageLotNo) {
		this.storageLotNo = storageLotNo;
	}
	
	public String getStorageInNo() {
		return storageInNo;
	}

	public void setStorageInNo(String storageInNo) {
		this.storageInNo = storageInNo;
	}

	public String getStorageCode() {
		return storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	
	public Double getStockOnHandQty() {
		return stockOnHandQty;
	}

	public void setStockOnHandQty(Double stockOnHandQty) {
		this.stockOnHandQty = stockOnHandQty;
	}

	public Double getOutUncommitQty() {
		return outUncommitQty;
	}

	public void setOutUncommitQty(Double outUncommitQty) {
		this.outUncommitQty = outUncommitQty;
	}

	public Double getInUncommitQty() {
		return inUncommitQty;
	}

	public void setInUncommitQty(Double inUncommitQty) {
		this.inUncommitQty = inUncommitQty;
	}

	public Double getMoveUncommitQty() {
		return moveUncommitQty;
	}

	public void setMoveUncommitQty(Double moveUncommitQty) {
		this.moveUncommitQty = moveUncommitQty;
	}

	public Double getOtherUncommitQty() {
		return otherUncommitQty;
	}

	public void setOtherUncommitQty(Double otherUncommitQty) {
		this.otherUncommitQty = otherUncommitQty;
	}

	public Double getBlockQty() {
		return blockQty;
	}

	public void setBlockQty(Double blockQty) {
		this.blockQty = blockQty;
	}

	public Double getTempQty() {
		return tempQty;
	}

	public void setTempQty(Double tempQty) {
		this.tempQty = tempQty;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Double getOnHandQty() {
		return onHandQty;
	}

	public void setOnHandQty(Double onHandQty) {
		this.onHandQty = onHandQty;
	}

	public String getItemCName() {
		return itemCName;
	}

	public void setItemCName(String itemCName) {
		this.itemCName = itemCName;
	}

}