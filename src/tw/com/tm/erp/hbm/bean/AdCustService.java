package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SoDeliveryHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AdCustService implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3638935888635545142L;
	private Long headId;
	private Long lineId;
	private Long indexNo;
	private String categoryCode;
	private String customerPoNo;
	private Date salesOrderDate;
	private String warehuseCode;
	private String posMachineCode;
	private String categoryType;
	private String itemBrand;
	private String status;
	private String createdBy;
	private String itemCode;
	private Integer actualSalesAmount;
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
	private Integer paymentType;
	private Integer paymentKind;
	private Integer disscount;
	private String discountOther;
	private Integer isDelivery;
	private Integer isCustomerPoNo;
	private String superintendentCode;
	private Date executeDate;
	private String memo;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private BuPurchaseHead buPurchaseHead;
	

	/** default constructor */
	public AdCustService() {
		
	}

	/** full constructor */
	public AdCustService(Long headId,Long indexNo, Long lineId ,String categoryCode,String customerPoNo) {
		
    this.headId=headId;
    this.lineId=lineId;
    this.categoryCode=categoryCode;	
    this.customerPoNo=customerPoNo;
		
	}

	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
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

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
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

	public Integer getActualSalesAmount() {
		return actualSalesAmount;
	}

	public void setActualSalesAmount(Integer actualSalesAmount) {
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

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getPaymentKind() {
		return paymentKind;
	}

	public void setPaymentKind(Integer paymentKind) {
		this.paymentKind = paymentKind;
	}

	public Integer getDisscount() {
		return disscount;
	}

	public void setDisscount(Integer disscount) {
		this.disscount = disscount;
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

	public BuPurchaseHead getBuPurchaseHead() {
		return buPurchaseHead;
	}

	public void setBuPurchaseHead(BuPurchaseHead buPurchaseHead) {
		this.buPurchaseHead = buPurchaseHead;
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

	


}

// Property accessors

