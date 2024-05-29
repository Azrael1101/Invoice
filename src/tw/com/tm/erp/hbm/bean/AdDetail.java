package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SoDeliveryHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AdDetail implements java.io.Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = -1197032239251232988L;
	
	//private Long headId;
	private BuPurchaseHead buPurchaseHead;
	private Long lineId;
	private Long indexNo;
	private String brandCode;
	private String url;
	private String enable;
	private String brandName;
	private String categoryCode;
	private String categoryName;
	private String type;
	private String warehouseCode;
	private String warehouseName;
	private String menuId;
	private String menuName;
	private String shopCode;
	private String shopName;
	private String employeeCode;
	private String cost;
	private Date updateDate;
	private Date creationDate;
	private Double clickNumber;
	

	/** default constructor */
	public AdDetail() {
		
	}

	/** full constructor */
	public AdDetail(Long indexNo, Long lineId ,String brandCode, String url,
			String enable,String brandName, String categoryCode, String categoryName, String type,String warehouseCode,String warehouseName
			) {
		
       
		
		
		this.indexNo = indexNo;
		this.lineId = lineId;
		this.brandCode = brandCode;
		this.url = url;
		this.enable = enable;
		this.brandName = brandName;
		this.categoryCode = categoryCode;
	
		
		
		
		
		
	}

	
	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public BuPurchaseHead getBuPurchaseHead() {
		return buPurchaseHead;
	}

	public void setBuPurchaseHead(BuPurchaseHead buPurchaseHead) {
		this.buPurchaseHead = buPurchaseHead;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public Double getClickNumber() {
		return clickNumber;
	}

	public void setClickNumber(Double clickNumber) {
		this.clickNumber = clickNumber;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}

// Property accessors

