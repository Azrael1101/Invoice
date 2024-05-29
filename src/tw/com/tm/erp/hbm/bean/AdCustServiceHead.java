package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SoDeliveryHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AdCustServiceHead implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8459334208400484260L;
	/**
	 * 
	 */

	private Long headId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private String requestCode;
	private String categorySystem;
	private String categoryItem;
	private String categoryGroup;
	private String department;
	private String description;
	private String depManager;
	private String depManagerName;
	private Date requestDate;
	private String customerPoNo;
	private Date salesOrderDate;
	private String warehuseCode;
	private String posMachineCode;
	private String categoryType;
	private String itemBrand;
	private String status;
	private String createdBy;
	private String itemCode;
	private Double actualSalesAmount;
	private String requestSourceOther;
	private Integer customerRequest; // 暫時欄位 更新人員
	private String customerRequestOther;
	private Integer refound;
	private String refoundOther;
	private Integer closed;
	private String closedOther;
	private Integer exceptional;
	private String exceptionalOther;
	private Integer maintainReceive;
	private Integer maintainGiven;
	private Integer mainTainExpense;
	private Integer customerSex;
	private String nationalityOther;
	private Integer nationality;
	private String contactMobile;
	private String contactHome;
	private String contactWay;
	private String email;
	private String VIPType;
	private String customerCode;
	private Integer isGiht;
	private String paymentType;
	private Integer paymentKind;
	private String discount;
	private String discountOther;
	private Integer isDelivery;
	private Integer isCustomerPoNo;
	private String superintendentCode;
	private Date executeDate;
	private String memo;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private String createdByName;
	private String lastUpdatedByName; 
	private AdCustServiceHead adCustServiceHead;
	private String customerLastName;
	private String customerFristName;
	private Long processId;
	private Date isDeliveryDate;
	private String isClose;
	private String requestSource;
	private String itemName;
	private String itemCategory;
	private String itemCodeOther;
	private String itemNameOther;
	private String actualSalesAmountOther;
	private String autoMail;
	private String categoryTypeOther;
	private Date endDate;
	private String deliveryDate;
	private String saleOrderDate;
	private String title;
	
	private List<AdCustServiceLine> adCustServiceLines = new ArrayList(0);

	/** default constructor */
	public AdCustServiceHead() {

	}

	/** full constructor */
	public AdCustServiceHead(Long headId,String customerPoNo) {

		this.headId=headId;
		this.customerPoNo=customerPoNo;

	}

	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getCustomerPoNo() {
		return customerPoNo;
	}

	public void setCustomerPoNo(String customerPoNo) {
		this.customerPoNo = customerPoNo;
	}

	public Date getSalesOrderDate() {
		return salesOrderDate;
	}

	public void setSalesOrderDate(Date salesOrderDate) {
		this.salesOrderDate = salesOrderDate;
	}

	public String getWarehuseCode() {
		return warehuseCode;
	}

	public void setWarehuseCode(String warehuseCode) {
		this.warehuseCode = warehuseCode;
	}

	public String getPosMachineCode() {
		return posMachineCode;
	}

	public void setPosMachineCode(String posMachineCode) {
		this.posMachineCode = posMachineCode;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Double getActualSalesAmount() {
		return actualSalesAmount;
	}

	public void setActualSalesAmount(Double actualSalesAmount) {
		this.actualSalesAmount = actualSalesAmount;
	}

	public String getRequestSourceOther() {
		return requestSourceOther;
	}

	public void setRequestSourceOther(String requestSourceOther) {
		this.requestSourceOther = requestSourceOther;
	}

	public Integer getCustomerRequest() {
		return customerRequest;
	}

	public void setCustomerRequest(Integer customerRequest) {
		this.customerRequest = customerRequest;
	}

	public String getCustomerRequestOther() {
		return customerRequestOther;
	}

	public void setCustomerRequestOther(String customerRequestOther) {
		this.customerRequestOther = customerRequestOther;
	}

	public Integer getRefound() {
		return refound;
	}

	public void setRefound(Integer refound) {
		this.refound = refound;
	}

	public String getRefoundOther() {
		return refoundOther;
	}

	public void setRefoundOther(String refoundOther) {
		this.refoundOther = refoundOther;
	}

	public Integer getClosed() {
		return closed;
	}

	public void setClosed(Integer closed) {
		this.closed = closed;
	}

	public String getClosedOther() {
		return closedOther;
	}

	public void setClosedOther(String closedOther) {
		this.closedOther = closedOther;
	}

	public Integer getExceptional() {
		return exceptional;
	}

	public void setExceptional(Integer exceptional) {
		this.exceptional = exceptional;
	}

	public String getExceptionalOther() {
		return exceptionalOther;
	}

	public void setExceptionalOther(String exceptionalOther) {
		this.exceptionalOther = exceptionalOther;
	}

	public Integer getMaintainReceive() {
		return maintainReceive;
	}

	public void setMaintainReceive(Integer maintainReceive) {
		this.maintainReceive = maintainReceive;
	}

	public Integer getMaintainGiven() {
		return maintainGiven;
	}

	public void setMaintainGiven(Integer maintainGiven) {
		this.maintainGiven = maintainGiven;
	}

	public Integer getMainTainExpense() {
		return mainTainExpense;
	}

	public void setMainTainExpense(Integer mainTainExpense) {
		this.mainTainExpense = mainTainExpense;
	}

	public Integer getCustomerSex() {
		return customerSex;
	}

	public void setCustomerSex(Integer customerSex) {
		this.customerSex = customerSex;
	}

	public Integer getNationality() {
		return nationality;
	}

	public void setNationality(Integer nationality) {
		this.nationality = nationality;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getContactHome() {
		return contactHome;
	}

	public void setContactHome(String contactHome) {
		this.contactHome = contactHome;
	}

	public String getContactWay() {
		return contactWay;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVIPType() {
		return VIPType;
	}

	public void setVIPType(String type) {
		VIPType = type;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public Integer getIsGiht() {
		return isGiht;
	}

	public void setIsGiht(Integer isGiht) {
		this.isGiht = isGiht;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getPaymentKind() {
		return paymentKind;
	}

	public void setPaymentKind(Integer paymentKind) {
		this.paymentKind = paymentKind;
	}

	public String getDiscountOther() {
		return discountOther;
	}

	public void setDiscountOther(String discountOther) {
		this.discountOther = discountOther;
	}

	public Integer getIsDelivery() {
		return isDelivery;
	}

	public void setIsDelivery(Integer isDelivery) {
		this.isDelivery = isDelivery;
	}

	public Integer getIsCustomerPoNo() {
		return isCustomerPoNo;
	}

	public void setIsCustomerPoNo(Integer isCustomerPoNo) {
		this.isCustomerPoNo = isCustomerPoNo;
	}

	public String getSuperintendentCode() {
		return superintendentCode;
	}

	public void setSuperintendentCode(String superintendentCode) {
		this.superintendentCode = superintendentCode;
	}

	public Date getExecuteDate() {
		return executeDate;
	}

	public void setExecuteDate(Date executeDate) {
		this.executeDate = executeDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getNationalityOther() {
		return nationalityOther;
	}

	public void setNationalityOther(String nationalityOther) {
		this.nationalityOther = nationalityOther;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}

	public String getCategorySystem() {
		return categorySystem;
	}

	public void setCategorySystem(String categorySystem) {
		this.categorySystem = categorySystem;
	}

	public String getCategoryItem() {
		return categoryItem;
	}

	public void setCategoryItem(String categoryItem) {
		this.categoryItem = categoryItem;
	}

	public String getCategoryGroup() {
		return categoryGroup;
	}

	public void setCategoryGroup(String categoryGroup) {
		this.categoryGroup = categoryGroup;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AdCustServiceHead getAdCustServiceHead() {
		return adCustServiceHead;
	}

	public void setAdCustServiceHead(AdCustServiceHead adCustServiceHead) {
		this.adCustServiceHead = adCustServiceHead;
	}

	public List<AdCustServiceLine> getAdCustServiceLines() {
		return adCustServiceLines;
	}

	public void setAdCustServiceLines(List<AdCustServiceLine> adCustServiceLines) {
		this.adCustServiceLines = adCustServiceLines;
	}

	public String getDepManager() {
		return depManager;
	}

	public void setDepManager(String depManager) {
		this.depManager = depManager;
	}

	public String getDepManagerName() {
		return depManagerName;
	}

	public void setDepManagerName(String depManagerName) {
		this.depManagerName = depManagerName;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getLastUpdatedByName() {
		return lastUpdatedByName;
	}

	public void setLastUpdatedByName(String lastUpdatedByName) {
		this.lastUpdatedByName = lastUpdatedByName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getCustomerFristName() {
		return customerFristName;
	}

	public void setCustomerFristName(String customerFristName) {
		this.customerFristName = customerFristName;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public Date getIsDeliveryDate() {
		return isDeliveryDate;
	}

	public void setIsDeliveryDate(Date isDeliveryDate) {
		this.isDeliveryDate = isDeliveryDate;
	}

	public String getIsClose() {
		return isClose;
	}

	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}

	public String getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(String requestSource) {
		this.requestSource = requestSource;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemCodeOther() {
		return itemCodeOther;
	}

	public void setItemCodeOther(String itemCodeOther) {
		this.itemCodeOther = itemCodeOther;
	}

	public String getItemNameOther() {
		return itemNameOther;
	}

	public void setItemNameOther(String itemNameOther) {
		this.itemNameOther = itemNameOther;
	}

	public String getActualSalesAmountOther() {
		return actualSalesAmountOther;
	}

	public void setActualSalesAmountOther(String actualSalesAmountOther) {
		this.actualSalesAmountOther = actualSalesAmountOther;
	}

	public String getAutoMail() {
		return autoMail;
	}

	public void setAutoMail(String autoMail) {
		this.autoMail = autoMail;
	}

	public String getCategoryTypeOther() {
		return categoryTypeOther;
	}

	public void setCategoryTypeOther(String categoryTypeOther) {
		this.categoryTypeOther = categoryTypeOther;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getSaleOrderDate() {
		return saleOrderDate;
	}

	public void setSaleOrderDate(String saleOrderDate) {
		this.saleOrderDate = saleOrderDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

//Property accessors

