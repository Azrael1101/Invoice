package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImDistributionHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImDistributionHead implements java.io.Serializable {

	private static final long serialVersionUID = 8132045049961736589L;
	public static final String TABLE_NAME = "ERP.IM_DISTRIBUTION_HEAD";
	// Fields

	private Long headId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
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
	private List<ImDistributionLine> imDistributionLines = new ArrayList();
	private String allDistributionItems;
	private String allDistributionShops;
	private String defaultWarehouseCode; // 配貨倉庫代號
	private Date deliveryDate;
	private Date arrivalDate;
	private String categoryType;
	private String itemCategory;
	private List<ImDistributionItem> imDistributionItems = new ArrayList();
	private List<ImDistributionShop> imDistributionShops = new ArrayList();
	private String actualWarehouseCode;
	private String receiveOrderTypeCode;
	private String receiveOrderNo;
	
	
	// Constructors

	public String getAllDistributionItems() {
		return allDistributionItems;
	}

	public void setAllDistributionItems(String allDistributionItems) {
		this.allDistributionItems = allDistributionItems;
	}

	public String getAllDistributionShops() {
		return allDistributionShops;
	}

	public void setAllDistributionShops(String allDistributionShops) {
		this.allDistributionShops = allDistributionShops;
	}

	/** default constructor */
	public ImDistributionHead() {
	}

	/** minimal constructor */
	public ImDistributionHead(Long headId) {
		this.headId = headId;
	}

	// Property accessors

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
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

	public String getReserve1() {
		return this.reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return this.reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return this.reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getReserve4() {
		return this.reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	public String getReserve5() {
		return this.reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ImDistributionLine> getImDistributionLines() {
		return imDistributionLines;
	}

	public void setImDistributionLines(List<ImDistributionLine> imDistributionLines) {
		this.imDistributionLines = imDistributionLines;
	}

	public String getDefaultWarehouseCode() {
		return defaultWarehouseCode;
	}

	public void setDefaultWarehouseCode(String defaultWarehouseCode) {
		this.defaultWarehouseCode = defaultWarehouseCode;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public List<ImDistributionItem> getImDistributionItems() {
		return imDistributionItems;
	}

	public void setImDistributionItems(List<ImDistributionItem> imDistributionItems) {
		this.imDistributionItems = imDistributionItems;
	}

	public List<ImDistributionShop> getImDistributionShops() {
		return imDistributionShops;
	}

	public void setImDistributionShops(List<ImDistributionShop> imDistributionShops) {
		this.imDistributionShops = imDistributionShops;
	}

	public String getActualWarehouseCode() {
		return actualWarehouseCode;
	}

	public void setActualWarehouseCode(String actualWarehouseCode) {
		this.actualWarehouseCode = actualWarehouseCode;
	}

	public String getReceiveOrderTypeCode() {
		return receiveOrderTypeCode;
	}

	public void setReceiveOrderTypeCode(String receiveOrderTypeCode) {
		this.receiveOrderTypeCode = receiveOrderTypeCode;
	}

	public String getReceiveOrderNo() {
		return receiveOrderNo;
	}

	public void setReceiveOrderNo(String receiveOrderNo) {
		this.receiveOrderNo = receiveOrderNo;
	}

}