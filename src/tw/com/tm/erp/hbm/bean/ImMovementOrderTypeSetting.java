package tw.com.tm.erp.hbm.bean;

import java.util.Date;

import com.inet.report.chart.axis.e;



public class ImMovementOrderTypeSetting implements java.io.Serializable{

	private static final long serialVersionUID = 5076713159677724301L;

	private String orderTypeCode;
	private String name;
	private String description;
	private String deliveryWarehouses;
	private String arrivalWarehouses;
	private String itemCategorymode;
	private String overOricount;
	private String typeOfIMV;
	private String checkItemcategory;
	private String bevoid;
	private String enable;
	private String showOricount;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Double indexNo;
	
	
	public ImMovementOrderTypeSetting(){
		
	}
	
	public ImMovementOrderTypeSetting(String orderTypeCode){
		this.orderTypeCode = orderTypeCode;
	}
	
	public ImMovementOrderTypeSetting(String orderTypeCode ,String name ,String description, 
			String deliveryWarehouses, String arrivalWarehouses, String itemCategorymode,
			String overOricount, String typeOfIMV ,String checkItemcategory ,String bevoid,
			String enable, String showOricount, String createdBy, Date creationDate, 
			String lastUpdatedBy, Date lastUpdatedate, Double indexNo){
		this.orderTypeCode = orderTypeCode;
		this.name = name;
		this.description = description;
		this.deliveryWarehouses = deliveryWarehouses;
		this.arrivalWarehouses = arrivalWarehouses;
		this.itemCategorymode = itemCategorymode;
		this.overOricount = overOricount;
		this.typeOfIMV = typeOfIMV;
		this.checkItemcategory = checkItemcategory;
		this.bevoid = bevoid;
		this.enable = enable;
		this.showOricount = showOricount;
		this.createdBy =createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdatedate;
		this.indexNo = indexNo;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeliveryWarehouses() {
		return deliveryWarehouses;
	}

	public void setDeliveryWarehouses(String deliveryWarehouses) {
		this.deliveryWarehouses = deliveryWarehouses;
	}

	public String getArrivalWarehouses() {
		return arrivalWarehouses;
	}

	public void setArrivalWarehouses(String arrivalWarehouses) {
		this.arrivalWarehouses = arrivalWarehouses;
	}

	public String getItemCategorymode() {
		return itemCategorymode;
	}

	public void setItemCategorymode(String itemCategorymode) {
		this.itemCategorymode = itemCategorymode;
	}

	public String getOverOricount() {
		return overOricount;
	}

	public void setOverOricount(String overOricount) {
		this.overOricount = overOricount;
	}

	public String getTypeOfIMV() {
		return typeOfIMV;
	}

	public void setTypeOfIMV(String typeOfIMV) {
		this.typeOfIMV = typeOfIMV;
	}

	public String getCheckItemcategory() {
		return checkItemcategory;
	}

	public void setCheckItemcategory(String checkItemcategory) {
		this.checkItemcategory = checkItemcategory;
	}

	public String getBevoid() {
		return bevoid;
	}

	public void setBevoid(String bevoid) {
		this.bevoid = bevoid;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getShowOricount() {
		return showOricount;
	}

	public void setShowOricount(String showOricount) {
		this.showOricount = showOricount;
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

	public Double getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Double indexNo) {
		this.indexNo = indexNo;
	}
	
	
	
	
	
}

