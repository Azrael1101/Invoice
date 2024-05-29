package tw.com.tm.erp.hbm.bean;

/**
 * ImItemOnHandViewId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImStorageOnHandViewId implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = -8431913695744638259L;
    private String brandCode;
    private String warehouseCode;
    private String itemCode;
    private String storageLotNo;
    private String storageInNo;
    private String storageCode;

    // Constructors

    /** default constructor */
    public ImStorageOnHandViewId() {
    }

    /** full constructor */
    public ImStorageOnHandViewId(String brandCode, String itemCode,
	    String warehouseCode, String storageLotNo, String storageInNo, String storageCode) {
	this.brandCode = brandCode;
	this.warehouseCode = warehouseCode;
	this.itemCode = itemCode;
	this.storageLotNo = storageLotNo;
	this.storageInNo = storageInNo;
	this.storageCode = storageCode;
    }

    
    // Property accessors
    
    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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

}