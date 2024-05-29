package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuCustomer entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuCustomerMod implements java.io.Serializable {

	private  Long    headId;
	private  Long    addressBookId;
	private  String  customerCode;
	private  String  identityCode;
	private  String  brandCode;
	private  String  enable;
	private  String  vipTypeCode;
	private  String  customerTypeCode;
	private  String  chineseName;
	private  String  englishName;
	private  String  shortName;
	private  Date    vipStartDate;
	private  Date    vipEndDate;
	private  Date    applicationDate;
	private  String  type;
	private  String  taxType;
	private  Double  taxRate;
	private  String  currencyCode;
	private  String  category07;
	private  String  gender;
	private  String  countryCode;
	private  Long    birthdayYear;
	private  Long    birthdayMonth;
	private  Long    birthdayDay;
	private  String  EMail;
	private  String  city;
	private  String  area;
	private  String  zipCode;
	private  String  address;
	private  String  paymentTermCode;
	private  String  invoiceDeliveryCode;
	private  String  invoiceTypeCode;
	private  String  remark1;
	private  String  remark2;
	private  String  remark3;
	private  String  category01;
	private  String  category02;
	private  String  category03;
	private  String  mobilePhone;
	private  String  tel1;
	private  String  tel2;
    private  String  createdBy;
    private  Date    creationDate;
    private  String  lastUpdatedBy;
    private  Date    lastUpdateDate;
	private  String  status; 
	private  String  birthday;
	private  String  applicationDateString;
	private  String    birthdayMonthString;
	private  String    birthdayDayString;
	
	private  String	contractPerson;
	private  String	zipCode1;
	private  String	city1;
	private  String	area1;
	private  String	address1;
	private  String	zipCode2;
	private  String	city2;
	private  String	area2;
	private  String	address2;
	private  String	zipCode3;
	private  String	city3;
	private  String	area3;
	private  String	address3;
	private  String deliveryAddress; //宅配地址

	
	
	//MACO 2016.11.29 鐘錶額度
/*    private Long stockCredits;
    private Long adjCredits;
    private Long totalUncommitCredits;*/
    // Constructors

	/** default constructor */
    public BuCustomerMod() {
    }


	public Long getHeadId() {
		return headId;
	}


	public void setHeadId(Long headId) {
		this.headId = headId;
	}


	public Long getAddressBookId() {
		return addressBookId;
	}


	public void setAddressBookId(Long addressBookId) {
		this.addressBookId = addressBookId;
	}


	public String getCustomerCode() {
		return customerCode;
	}


	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}


	public String getIdentityCode() {
		return identityCode;
	}


	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}


	public String getBrandCode() {
		return brandCode;
	}


	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}


	public String getEnable() {
		return enable;
	}


	public void setEnable(String enable) {
		this.enable = enable;
	}


	public String getCustomerTypeCode() {
		return customerTypeCode;
	}


	public void setCustomerTypeCode(String customerTypeCode) {
		this.customerTypeCode = customerTypeCode;
	}


	public String getChineseName() {
		return chineseName;
	}


	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}


	public String getEnglishName() {
		return englishName;
	}


	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}


	public String getShortName() {
		return shortName;
	}


	public void setShortName(String shortName) {
		this.shortName = shortName;
	}


	public Date getVipStartDate() {
		return vipStartDate;
	}


	public void setVipStartDate(Date vipStartDate) {
		this.vipStartDate = vipStartDate;
	}


	public Date getVipEndDate() {
		return vipEndDate;
	}


	public void setVipEndDate(Date vipEndDate) {
		this.vipEndDate = vipEndDate;
	}


	public Date getApplicationDate() {
		return applicationDate;
	}


	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getTaxType() {
		return taxType;
	}


	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}


	public Double getTaxRate() {
		return taxRate;
	}


	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}


	public String getCurrencyCode() {
		return currencyCode;
	}


	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}


	public String getCategory07() {
		return category07;
	}


	public void setCategory07(String category07) {
		this.category07 = category07;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getCountryCode() {
		return countryCode;
	}


	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}


	public Long getBirthdayYear() {
		return birthdayYear;
	}


	public void setBirthdayYear(Long birthdayYear) {
		this.birthdayYear = birthdayYear;
	}


	public Long getBirthdayMonth() {
		return birthdayMonth;
	}


	public void setBirthdayMonth(Long birthdayMonth) {
		this.birthdayMonth = birthdayMonth;
	}

	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getZipCode() {
		return zipCode;
	}


	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getPaymentTermCode() {
		return paymentTermCode;
	}


	public void setPaymentTermCode(String paymentTermCode) {
		this.paymentTermCode = paymentTermCode;
	}


	public String getInvoiceDeliveryCode() {
		return invoiceDeliveryCode;
	}


	public void setInvoiceDeliveryCode(String invoiceDeliveryCode) {
		this.invoiceDeliveryCode = invoiceDeliveryCode;
	}


	public String getInvoiceTypeCode() {
		return invoiceTypeCode;
	}


	public void setInvoiceTypeCode(String invoiceTypeCode) {
		this.invoiceTypeCode = invoiceTypeCode;
	}


	public String getRemark1() {
		return remark1;
	}


	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}


	public String getRemark2() {
		return remark2;
	}


	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}


	public String getRemark3() {
		return remark3;
	}


	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}


	public String getCategory01() {
		return category01;
	}


	public void setCategory01(String category01) {
		this.category01 = category01;
	}


	public String getCategory02() {
		return category02;
	}


	public void setCategory02(String category02) {
		this.category02 = category02;
	}


	public String getCategory03() {
		return category03;
	}


	public void setCategory03(String category03) {
		this.category03 = category03;
	}


	public String getMobilePhone() {
		return mobilePhone;
	}


	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}


	public String getTel1() {
		return tel1;
	}


	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}


	public String getTel2() {
		return tel2;
	}


	public void setTel2(String tel2) {
		this.tel2 = tel2;
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

	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getEMail() {
		return EMail;
	}

	public void setEMail(String mail) {
		EMail = mail;
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


	public String getBirthday() {
		return birthday;
	}


	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}


	public String getApplicationDateString() {
		return applicationDateString;
	}


	public void setApplicationDateString(String applicationDateString) {
		this.applicationDateString = applicationDateString;
	}


	public String getVipTypeCode() {
		return vipTypeCode;
	}


	public void setVipTypeCode(String vipTypeCode) {
		this.vipTypeCode = vipTypeCode;
	}


	public Long getBirthdayDay() {
		return birthdayDay;
	}


	public void setBirthdayDay(Long birthdayDay) {
		this.birthdayDay = birthdayDay;
	}


	public String getBirthdayMonthString() {
		return birthdayMonthString;
	}


	public void setBirthdayMonthString(String birthdayMonthString) {
		this.birthdayMonthString = birthdayMonthString;
	}


	public String getBirthdayDayString() {
		return birthdayDayString;
	}


	public void setBirthdayDayString(String birthdayDayString) {
		this.birthdayDayString = birthdayDayString;
	}


	public String getContractPerson() {
		return contractPerson;
	}


	public void setContractPerson(String contractPerson) {
		this.contractPerson = contractPerson;
	}


	public String getZipCode1() {
		return zipCode1;
	}


	public void setZipCode1(String zipCode1) {
		this.zipCode1 = zipCode1;
	}


	public String getCity1() {
		return city1;
	}


	public void setCity1(String city1) {
		this.city1 = city1;
	}


	public String getArea1() {
		return area1;
	}


	public void setArea1(String area1) {
		this.area1 = area1;
	}


	public String getAddress1() {
		return address1;
	}


	public void setAddress1(String address1) {
		this.address1 = address1;
	}


	public String getZipCode2() {
		return zipCode2;
	}


	public void setZipCode2(String zipCode2) {
		this.zipCode2 = zipCode2;
	}


	public String getCity2() {
		return city2;
	}


	public void setCity2(String city2) {
		this.city2 = city2;
	}


	public String getArea2() {
		return area2;
	}


	public void setArea2(String area2) {
		this.area2 = area2;
	}


	public String getAddress2() {
		return address2;
	}


	public void setAddress2(String address2) {
		this.address2 = address2;
	}


	public String getZipCode3() {
		return zipCode3;
	}


	public void setZipCode3(String zipCode3) {
		this.zipCode3 = zipCode3;
	}


	public String getCity3() {
		return city3;
	}


	public void setCity3(String city3) {
		this.city3 = city3;
	}


	public String getArea3() {
		return area3;
	}


	public void setArea3(String area3) {
		this.area3 = area3;
	}


	public String getAddress3() {
		return address3;
	}


	public void setAddress3(String address3) {
		this.address3 = address3;
	}


	public String getDeliveryAddress() {
		return deliveryAddress;
	}


	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}


/*	public Long getStockCredits() {
		return stockCredits;
	}


	public void setStockCredits(Long stockCredits) {
		this.stockCredits = stockCredits;
	}


	public Long getAdjCredits() {
		return adjCredits;
	}


	public void setAdjCredits(Long adjCredits) {
		this.adjCredits = adjCredits;
	}


	public Long getTotalUncommitCredits() {
		return totalUncommitCredits;
	}


	public void setTotalUncommitCredits(Long totalUncommitCredits) {
		this.totalUncommitCredits = totalUncommitCredits;
	}
*/


}