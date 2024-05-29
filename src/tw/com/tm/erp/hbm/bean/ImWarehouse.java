package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImWarehouse entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImWarehouse implements java.io.Serializable {


    // Fields
    private static final long serialVersionUID = 5576499100764788054L;
    public static final String WAIT_CHECK_WAREHOUSE_CODE = "00"; // 待驗倉
    private Long warehouseId;
    private String warehouseCode;
    private String brandCode;
    private String warehouseName;
    private Long locationId;
    private Long warehouseTypeId;
    private String storage;
    private String storageArea;
    private String storageBin;
    private String categoryCode;
    private String taxTypeCode;
    private Long warehouseCapacity;
    private String enable;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private String warehouseManager;
    private String warehouseTypeName; // 暫時欄位 類別名稱
    private String categoryName; // 暫時欄位 型態名稱
    private String locationName; // 暫時欄位 地點名稱
    private String taxTypeName; // 暫時欄位 稅別名稱
    private String tmpEnable;// 暫時欄位，設定enable

    // 以下欄位只有免稅事業體系需要填，百貨業無須填寫
    private String customsWarehouseCode; // 海關倉庫代號
    private String allowMinusStock;// 允許負庫存
    private String warehouseArea; // 倉庫區域
    private String ioArea; // 出入境
    private String contractCode; // 合約代號
    private String companyCode; // 公司代號
    private String taxAreaCode; // 報稅區域
    private String businessTypeCode; // 營業項目
    private String financeSalesType; 
    private String customsArea; 
    //private String warehouseCodeCustoms;
    
    private List<ImWarehouseEmployee> warehouseEmployees = new ArrayList(0);
    
    /*
    public String getWarehouseCodeCustoms() {
		return warehouseCodeCustoms;
	}

	public void setWarehouseCodeCustoms(String warehouseCodeCustoms) {
		this.warehouseCodeCustoms = warehouseCodeCustoms;
	}
	*/

	// Constructors
    /** default constructor */
    public ImWarehouse() {
    }

    /** minimal constructor */
    public ImWarehouse(String warehouseCode) {
	this.warehouseCode = warehouseCode;
    }

    // Property accessors
    public ImWarehouse(Long warehouseId, String warehouseCode, String brandCode,
	    String warehouseName, Long locationId, Long warehouseTypeId,
	    String storage, String storageArea, String storageBin,
	    String categoryCode, String taxTypeCode, Long warehouseCapacity,
	    String enable, String createdBy, Date creationDate,
	    String lastUpdatedBy, Date lastUpdateDate, String warehouseManager,
	    String customsWarehouseCode, String warehouseTypeName,
	    String categoryName, String locationName, String taxTypeName,
	    String tmpEnable, String allowMinusStock, String warehouseArea,
	    String ioArea, String contractCode, String companyCode,
	    String taxAreaCode, String businessTypeCode) {
	super();
	this.warehouseId = warehouseId;
	this.warehouseCode = warehouseCode;
	this.brandCode = brandCode;
	this.warehouseName = warehouseName;
	this.locationId = locationId;
	this.warehouseTypeId = warehouseTypeId;
	this.storage = storage;
	this.storageArea = storageArea;
	this.storageBin = storageBin;
	this.categoryCode = categoryCode;
	this.taxTypeCode = taxTypeCode;
	this.warehouseCapacity = warehouseCapacity;
	this.enable = enable;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
	this.warehouseManager = warehouseManager;
	this.customsWarehouseCode = customsWarehouseCode;
	this.warehouseTypeName = warehouseTypeName;
	this.categoryName = categoryName;
	this.locationName = locationName;
	this.taxTypeName = taxTypeName;
	this.tmpEnable = tmpEnable;
	this.allowMinusStock = allowMinusStock;
	this.warehouseArea = warehouseArea;
	this.ioArea = ioArea;
	this.contractCode = contractCode;
	this.companyCode = companyCode;
	this.taxAreaCode = taxAreaCode;
	this.businessTypeCode = businessTypeCode;
    }

    public String getWarehouseCode() {
	return this.warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
	this.warehouseCode = warehouseCode;
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

    public Long getLocationId() {
	return this.locationId;
    }

    public void setLocationId(Long locationId) {
	this.locationId = locationId;
    }

    public Long getWarehouseTypeId() {
	return this.warehouseTypeId;
    }

    public void setWarehouseTypeId(Long warehouseTypeId) {
	this.warehouseTypeId = warehouseTypeId;
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

    public String getCategoryCode() {
	return this.categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
	this.categoryCode = categoryCode;
    }

    public String getTaxTypeCode() {
	return this.taxTypeCode;
    }

    public void setTaxTypeCode(String taxTypeCode) {
	this.taxTypeCode = taxTypeCode;
    }

    public void setWarehouseCapacity(Long warehouseCapacity) {
	this.warehouseCapacity = warehouseCapacity;
    }

    public Long getWarehouseCapacity() {
	return warehouseCapacity;
    }

    public String getEnable() {
	return this.enable;
    }

    public void setEnable(String enable) {
	this.enable = enable;
    }

    public String getCreatedBy() {
	return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    public Date getCreationDate() {
	return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
    }

    public String getLastUpdatedBy() {
	return this.lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
	this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
	return this.lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
	this.lastUpdateDate = lastUpdateDate;
    }

    public String getWarehouseManager() {
	return this.warehouseManager;
    }

    public void setWarehouseManager(String warehouseManager) {
	this.warehouseManager = warehouseManager;
    }

    public String getCustomsWarehouseCode() {
	return customsWarehouseCode;
    }

    public void setCustomsWarehouseCode(String customsWarehouseCode) {
	this.customsWarehouseCode = customsWarehouseCode;
    }

    public String getWarehouseTypeName() {
	return warehouseTypeName;
    }

    public void setWarehouseTypeName(String warehouseTypeName) {
	this.warehouseTypeName = warehouseTypeName;
    }

    public String getCategoryName() {
	return categoryName;
    }

    public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
    }

    public String getLocationName() {
	return locationName;
    }

    public void setLocationName(String locationName) {
	this.locationName = locationName;
    }

    public String getTaxTypeName() {
	return taxTypeName;
    }

    public void setTaxTypeName(String taxTypeName) {
	this.taxTypeName = taxTypeName;
    }

    public String getAllowMinusStock() {
	return allowMinusStock;
    }

    public void setAllowMinusStock(String allowMinusStock) {
	this.allowMinusStock = allowMinusStock;
    }

    public String getTmpEnable() {
	return tmpEnable;
    }

    public void setTmpEnable(String tmpEnable) {
	this.tmpEnable = tmpEnable;
    }

    public String getWarehouseArea() {
	return warehouseArea;
    }

    public void setWarehouseArea(String warehouseArea) {
	this.warehouseArea = warehouseArea;
    }

    public String getIoArea() {
	return ioArea;
    }

    public void setIoArea(String ioArea) {
	this.ioArea = ioArea;
    }

    public String getContractCode() {
	return contractCode;
    }

    public void setContractCode(String contractCode) {
	this.contractCode = contractCode;
    }

    public String getCompanyCode() {
	return companyCode;
    }

    public void setCompanyCode(String companyCode) {
	this.companyCode = companyCode;
    }

    public String getTaxAreaCode() {
	return taxAreaCode;
    }

    public void setTaxAreaCode(String taxAreaCode) {
	this.taxAreaCode = taxAreaCode;
    }

    public String getBusinessTypeCode() {
	return businessTypeCode;
    }

    public void setBusinessTypeCode(String businessTypeCode) {
	this.businessTypeCode = businessTypeCode;
    }

	public String getFinanceSalesType() {
		return financeSalesType;
	}

	public void setFinanceSalesType(String financeSalesType) {
		this.financeSalesType = financeSalesType;
	}

	public String getCustomsArea() {
		return customsArea;
	}

	public void setCustomsArea(String customsArea) {
		this.customsArea = customsArea;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public List<ImWarehouseEmployee> getWarehouseEmployees() {
		return warehouseEmployees;
	}

	public void setWarehouseEmployees(
			List<ImWarehouseEmployee> warehouseEmployees) {
		this.warehouseEmployees = warehouseEmployees;
	}
}