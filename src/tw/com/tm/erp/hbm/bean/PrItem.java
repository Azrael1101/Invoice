package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SoDeliveryHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PrItem implements java.io.Serializable {



	/**
	 * 
	 */
	private static long serialVersionUID = -3567650285447619510L;
	private Long itemId;
	private String itemNo;
	private String itemName;
	private String specInfo;
	//private String enable;
	private String department;
	private String supplierNo;
	private Integer purUnitPrice;
	private Integer reUnitPrice;
	private String groupNo;
	private String groupName;
	private String enable;
	private String orderTypeCode;
	private String supplier;
	private String itemBrand;
	private String isTax;
	private Date creationDate;
	private String createdByName;
	private String createdBy;
	/*private String category01;
	private String category02;
	private Double lowestPrice;
	private Double hightestPrice;
	private String acountCode; 
	private String isAsset;
	private String itemDefault;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private String isTax;
	private Date lastUpdateDate;
	private String createdByName;*/

	/** default constructor */
	public PrItem() {
		
	}

	/** full constructor */
	public PrItem(Long itemId,Long indexNo, Long lineId ,String itemNo, String itemName,
			String specInfo, Integer quantity, Integer purUnitPrice,
			Integer purTotalAmount,
			String supplier, String status, String createdBy,
			Date creationDate, String assetsNo, BuPurchaseHead buPurchaseHead,Integer reUnitPrice,Integer reTotalAmount) {
		
		this.itemId = itemId;
		this.itemName = itemName;
	
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static void setSerialVersionUID(long serialVersionUID) {
		PrItem.serialVersionUID = serialVersionUID;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSpecInfo() {
		return specInfo;
	}

	public void setSpecInfo(String specInfo) {
		this.specInfo = specInfo;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}

	public Integer getPurUnitPrice() {
		return purUnitPrice;
	}

	public void setPurUnitPrice(Integer purUnitPrice) {
		this.purUnitPrice = purUnitPrice;
	}

	public Integer getReUnitPrice() {
		return reUnitPrice;
	}

	public void setReUnitPrice(Integer reUnitPrice) {
		this.reUnitPrice = reUnitPrice;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getIsTax() {
		return isTax;
	}

	public void setIsTax(String isTax) {
		this.isTax = isTax;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	
}

// Property accessors

