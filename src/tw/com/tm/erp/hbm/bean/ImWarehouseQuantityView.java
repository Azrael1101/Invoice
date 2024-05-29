package tw.com.tm.erp.hbm.bean;

/**
 * ImWarehouseQuantityViewId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImWarehouseQuantityView implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -290452393317216026L;
	private Long id;
	private Long locationId;
	private String locationName;
	private String locationType;
	private String locationLevel;
	private Long addressBookId;
	private String warehouseCode;
	private Long warehouseTypeId;
	private String warehouseTypeName;
	private String warehouseTypeCode;
	private Long warehouseTypeLevel;
	private String brandCode;
	private String warehouseName;
	private String categoryCode;
	private String storage;
	private String storageArea;
	private String storageBin;
	private String taxTypeCode;
	private String itemCode;
	private String lotNo;
	private Long stockOnHandQty;
	private Long uncommitQty;
	private String organizationCode;

	// Constructors

	/** default constructor */
	public ImWarehouseQuantityView() {
	}

	/** minimal constructor */
	public ImWarehouseQuantityView(Long locationId, String warehouseCode,
			String itemCode, String lotNo, String organizationCode) {
		this.locationId = locationId;
		this.warehouseCode = warehouseCode;
		this.itemCode = itemCode;
		this.lotNo = lotNo;
		this.organizationCode = organizationCode;
	}

	/** full constructor */
	public ImWarehouseQuantityView(Long id, Long locationId,
			String locationName, String locationType, String locationLevel,
			Long addressBookId, String warehouseCode, Long warehouseTypeId,
			String warehouseTypeName, String warehouseTypeCode,
			Long warehouseTypeLevel, String brandCode, String warehouseName,
			String categoryCode, String storage, String storageArea,
			String storageBin, String taxTypeCode, String itemCode,
			String lotNo, Long stockOnHandQty, Long uncommitQty,
			String organizationCode) {
		this.id = id;
		this.locationId = locationId;
		this.locationName = locationName;
		this.locationType = locationType;
		this.locationLevel = locationLevel;
		this.addressBookId = addressBookId;
		this.warehouseCode = warehouseCode;
		this.warehouseTypeId = warehouseTypeId;
		this.warehouseTypeName = warehouseTypeName;
		this.warehouseTypeCode = warehouseTypeCode;
		this.warehouseTypeLevel = warehouseTypeLevel;
		this.brandCode = brandCode;
		this.warehouseName = warehouseName;
		this.categoryCode = categoryCode;
		this.storage = storage;
		this.storageArea = storageArea;
		this.storageBin = storageBin;
		this.taxTypeCode = taxTypeCode;
		this.itemCode = itemCode;
		this.lotNo = lotNo;
		this.stockOnHandQty = stockOnHandQty;
		this.uncommitQty = uncommitQty;
		this.organizationCode = organizationCode;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLocationId() {
		return this.locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return this.locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationType() {
		return this.locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getLocationLevel() {
		return this.locationLevel;
	}

	public void setLocationLevel(String locationLevel) {
		this.locationLevel = locationLevel;
	}

	public Long getAddressBookId() {
		return this.addressBookId;
	}

	public void setAddressBookId(Long addressBookId) {
		this.addressBookId = addressBookId;
	}

	public String getWarehouseCode() {
		return this.warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public Long getWarehouseTypeId() {
		return this.warehouseTypeId;
	}

	public void setWarehouseTypeId(Long warehouseTypeId) {
		this.warehouseTypeId = warehouseTypeId;
	}

	public String getWarehouseTypeName() {
		return this.warehouseTypeName;
	}

	public void setWarehouseTypeName(String warehouseTypeName) {
		this.warehouseTypeName = warehouseTypeName;
	}

	public String getWarehouseTypeCode() {
		return this.warehouseTypeCode;
	}

	public void setWarehouseTypeCode(String warehouseTypeCode) {
		this.warehouseTypeCode = warehouseTypeCode;
	}

	public Long getWarehouseTypeLevel() {
		return this.warehouseTypeLevel;
	}

	public void setWarehouseTypeLevel(Long warehouseTypeLevel) {
		this.warehouseTypeLevel = warehouseTypeLevel;
	}

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getWarehouseName() {
		return this.warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getCategoryCode() {
		return this.categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getStorage() {
		return this.storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getStorageArea() {
		return this.storageArea;
	}

	public void setStorageArea(String storageArea) {
		this.storageArea = storageArea;
	}

	public String getStorageBin() {
		return this.storageBin;
	}

	public void setStorageBin(String storageBin) {
		this.storageBin = storageBin;
	}

	public String getTaxTypeCode() {
		return this.taxTypeCode;
	}

	public void setTaxTypeCode(String taxTypeCode) {
		this.taxTypeCode = taxTypeCode;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Long getStockOnHandQty() {
		return this.stockOnHandQty;
	}

	public void setStockOnHandQty(Long stockOnHandQty) {
		this.stockOnHandQty = stockOnHandQty;
	}

	public Long getUncommitQty() {
		return this.uncommitQty;
	}

	public void setUncommitQty(Long uncommitQty) {
		this.uncommitQty = uncommitQty;
	}

	public String getOrganizationCode() {
		return this.organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
}