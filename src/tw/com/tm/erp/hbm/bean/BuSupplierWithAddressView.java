package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuSupplierWithAddressView entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuSupplierWithAddressView implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3812199168028403447L;

	private String brandCode;
	private String supplierCode;
	private Long addressBookId;
	private String organizationCode;
	private String supplierTypeCode;
	private String supplierTypeCodeName; // 額外欄位 廠商類別中文
	private String identityCode;
	private String type;
	private String typeName; // 額外欄位 類別中文
	private String chineseName;
	private String englishName;
	private String shortName;
	private String companyName;
	private String department;
	private String countryCode;
	private String gender;
	private Long birthdayYear;
	private Long birthdayMonth;
	private Long birthdayDay;
	private String url;
	private Long capitalization;
	private Long income;
	private Long employees;
	private String industryCode;
	private String routeCode;
	private String contractPerson;
	private String categoryCode;
	private String categoryCodeName; // 額外欄位 廠商類型中文
	private String categoryType;
	private String invoiceTypeCode;
	private String taxType;
	private Double taxRate;
	private String currencyCode;
	private String invoiceDeliveryCode;
	private String paymentTermCode;
	private String priceTermCode;
	private String billStyleCode;
	private String grade;
	private String customsBroker;
	private String agent;
	private Double commissionRate;
	private String superintendent;
	private String category01;
	private String category02;
	private String category03;
	private String category04;
	private String category05;
	private String category06;
	private String category07;
	private String category08;
	private String category09;
	private String category10;
	private String category11;
	private String category12;
	private String category13;
	private String category14;
	private String category15;
	private String category16;
	private String category17;
	private String category18;
	private String category19;
	private String category20;
	private String aTel1;
	private String aTel2;
	private String aFax1;
	private String aFax2;
	private String aEMail;
	private String contactPerson1;
	private String tel1;
	private String fax1;
	private String EMail1;
	private String contactPerson2;
	private String tel2;
	private String fax2;
	private String EMail2;
	private String contactPerson3;
	private String tel3;
	private String fax3;
	private String EMail3;
	private String contactPerson4;
	private String tel4;
	private String fax4;
	private String EMail4;
	private String aCity;
	private String aArea;
	private String aZipCode;
	private String aAddress;
	private String city1;
	private String area1;
	private String zipCode1;
	private String address1;
	private String city2;
	private String area2;
	private String zipCode2;
	private String address2;
	private String city3;
	private String area3;
	private String zipCode3;
	private String address3;
	private String city4;
	private String area4;
	private String zipCode4;
	private String address4;
	private Double totalTradeAmount;
	private Double periodTradeAmount;
	private Date lastTradeDate;
	private String bankName;
	private String branchCode;
	private String accountCode;
	private String accepter;
	private String acceptTel;
	private String enable;
	private String aRemark1;
	private String aRemark2;
	private String aRemark3;
	private String aRemark4;
	private String aRemark5;
	private String aRemark6;
	private String remark1;
	private String remark2;
	private String remark3;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String default_warehouse_code;

	// Constructors

	/** default constructor */
	public BuSupplierWithAddressView() {
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Long getAddressBookId() {
		return addressBookId;
	}

	public void setAddressBookId(Long addressBookId) {
		this.addressBookId = addressBookId;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getSupplierTypeCode() {
		return supplierTypeCode;
	}

	public void setSupplierTypeCode(String supplierTypeCode) {
		this.supplierTypeCode = supplierTypeCode;
	}

	public String getSupplierTypeCodeName() {
		return supplierTypeCodeName;
	}

	public void setSupplierTypeCodeName(String supplierTypeCodeName) {
		this.supplierTypeCodeName = supplierTypeCodeName;
	}

	public String getIdentityCode() {
		return identityCode;
	}

	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public Long getBirthdayDay() {
		return birthdayDay;
	}

	public void setBirthdayDay(Long birthdayDay) {
		this.birthdayDay = birthdayDay;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getCapitalization() {
		return capitalization;
	}

	public void setCapitalization(Long capitalization) {
		this.capitalization = capitalization;
	}

	public Long getIncome() {
		return income;
	}

	public void setIncome(Long income) {
		this.income = income;
	}

	public Long getEmployees() {
		return employees;
	}

	public void setEmployees(Long employees) {
		this.employees = employees;
	}

	public String getIndustryCode() {
		return industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	public String getRouteCode() {
		return routeCode;
	}

	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}

	public String getContractPerson() {
		return contractPerson;
	}

	public void setContractPerson(String contractPerson) {
		this.contractPerson = contractPerson;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryCodeName() {
		return categoryCodeName;
	}

	public void setCategoryCodeName(String categoryCodeName) {
		this.categoryCodeName = categoryCodeName;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getInvoiceTypeCode() {
		return invoiceTypeCode;
	}

	public void setInvoiceTypeCode(String invoiceTypeCode) {
		this.invoiceTypeCode = invoiceTypeCode;
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

	public String getInvoiceDeliveryCode() {
		return invoiceDeliveryCode;
	}

	public void setInvoiceDeliveryCode(String invoiceDeliveryCode) {
		this.invoiceDeliveryCode = invoiceDeliveryCode;
	}

	public String getPaymentTermCode() {
		return paymentTermCode;
	}

	public void setPaymentTermCode(String paymentTermCode) {
		this.paymentTermCode = paymentTermCode;
	}

	public String getPriceTermCode() {
		return priceTermCode;
	}

	public void setPriceTermCode(String priceTermCode) {
		this.priceTermCode = priceTermCode;
	}

	public String getBillStyleCode() {
		return billStyleCode;
	}

	public void setBillStyleCode(String billStyleCode) {
		this.billStyleCode = billStyleCode;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getCustomsBroker() {
		return customsBroker;
	}

	public void setCustomsBroker(String customsBroker) {
		this.customsBroker = customsBroker;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public Double getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(Double commissionRate) {
		this.commissionRate = commissionRate;
	}

	public String getSuperintendent() {
		return superintendent;
	}

	public void setSuperintendent(String superintendent) {
		this.superintendent = superintendent;
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

	public String getCategory04() {
		return category04;
	}

	public void setCategory04(String category04) {
		this.category04 = category04;
	}

	public String getCategory05() {
		return category05;
	}

	public void setCategory05(String category05) {
		this.category05 = category05;
	}

	public String getCategory06() {
		return category06;
	}

	public void setCategory06(String category06) {
		this.category06 = category06;
	}

	public String getCategory07() {
		return category07;
	}

	public void setCategory07(String category07) {
		this.category07 = category07;
	}

	public String getCategory08() {
		return category08;
	}

	public void setCategory08(String category08) {
		this.category08 = category08;
	}

	public String getCategory09() {
		return category09;
	}

	public void setCategory09(String category09) {
		this.category09 = category09;
	}

	public String getCategory10() {
		return category10;
	}

	public void setCategory10(String category10) {
		this.category10 = category10;
	}

	public String getCategory11() {
		return category11;
	}

	public void setCategory11(String category11) {
		this.category11 = category11;
	}

	public String getCategory12() {
		return category12;
	}

	public void setCategory12(String category12) {
		this.category12 = category12;
	}

	public String getCategory13() {
		return category13;
	}

	public void setCategory13(String category13) {
		this.category13 = category13;
	}

	public String getCategory14() {
		return category14;
	}

	public void setCategory14(String category14) {
		this.category14 = category14;
	}

	public String getCategory15() {
		return category15;
	}

	public void setCategory15(String category15) {
		this.category15 = category15;
	}

	public String getCategory16() {
		return category16;
	}

	public void setCategory16(String category16) {
		this.category16 = category16;
	}

	public String getCategory17() {
		return category17;
	}

	public void setCategory17(String category17) {
		this.category17 = category17;
	}

	public String getCategory18() {
		return category18;
	}

	public void setCategory18(String category18) {
		this.category18 = category18;
	}

	public String getCategory19() {
		return category19;
	}

	public void setCategory19(String category19) {
		this.category19 = category19;
	}

	public String getCategory20() {
		return category20;
	}

	public void setCategory20(String category20) {
		this.category20 = category20;
	}

	public String getATel1() {
		return aTel1;
	}

	public void setATel1(String aTel1) {
		this.aTel1 = aTel1;
	}

	public String getATel2() {
		return aTel2;
	}

	public void setATel2(String aTel2) {
		this.aTel2 = aTel2;
	}

	public String getAFax1() {
		return aFax1;
	}

	public void setAFax1(String aFax1) {
		this.aFax1 = aFax1;
	}

	public String getAFax2() {
		return aFax2;
	}

	public void setAFax2(String aFax2) {
		this.aFax2 = aFax2;
	}

	public String getAEMail() {
		return aEMail;
	}

	public void setAEMail(String mail) {
		aEMail = mail;
	}

	public String getContactPerson1() {
		return contactPerson1;
	}

	public void setContactPerson1(String contactPerson1) {
		this.contactPerson1 = contactPerson1;
	}

	public String getTel1() {
		return tel1;
	}

	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}

	public String getFax1() {
		return fax1;
	}

	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}

	public String getEMail1() {
		return EMail1;
	}

	public void setEMail1(String mail1) {
		EMail1 = mail1;
	}

	public String getContactPerson2() {
		return contactPerson2;
	}

	public void setContactPerson2(String contactPerson2) {
		this.contactPerson2 = contactPerson2;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getFax2() {
		return fax2;
	}

	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}

	public String getEMail2() {
		return EMail2;
	}

	public void setEMail2(String mail2) {
		EMail2 = mail2;
	}

	public String getContactPerson3() {
		return contactPerson3;
	}

	public void setContactPerson3(String contactPerson3) {
		this.contactPerson3 = contactPerson3;
	}

	public String getTel3() {
		return tel3;
	}

	public void setTel3(String tel3) {
		this.tel3 = tel3;
	}

	public String getFax3() {
		return fax3;
	}

	public void setFax3(String fax3) {
		this.fax3 = fax3;
	}

	public String getEMail3() {
		return EMail3;
	}

	public void setEMail3(String mail3) {
		EMail3 = mail3;
	}

	public String getContactPerson4() {
		return contactPerson4;
	}

	public void setContactPerson4(String contactPerson4) {
		this.contactPerson4 = contactPerson4;
	}

	public String getTel4() {
		return tel4;
	}

	public void setTel4(String tel4) {
		this.tel4 = tel4;
	}

	public String getFax4() {
		return fax4;
	}

	public void setFax4(String fax4) {
		this.fax4 = fax4;
	}

	public String getEMail4() {
		return EMail4;
	}

	public void setEMail4(String mail4) {
		EMail4 = mail4;
	}

	public String getACity() {
		return aCity;
	}

	public void setACity(String city) {
		aCity = city;
	}

	public String getAArea() {
		return aArea;
	}

	public void setAArea(String area) {
		aArea = area;
	}

	public String getAZipCode() {
		return aZipCode;
	}

	public void setAZipCode(String zipCode) {
		aZipCode = zipCode;
	}

	public String getAAddress() {
		return aAddress;
	}

	public void setAAddress(String address) {
		aAddress = address;
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

	public String getZipCode1() {
		return zipCode1;
	}

	public void setZipCode1(String zipCode1) {
		this.zipCode1 = zipCode1;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
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

	public String getZipCode2() {
		return zipCode2;
	}

	public void setZipCode2(String zipCode2) {
		this.zipCode2 = zipCode2;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
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

	public String getZipCode3() {
		return zipCode3;
	}

	public void setZipCode3(String zipCode3) {
		this.zipCode3 = zipCode3;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getCity4() {
		return city4;
	}

	public void setCity4(String city4) {
		this.city4 = city4;
	}

	public String getArea4() {
		return area4;
	}

	public void setArea4(String area4) {
		this.area4 = area4;
	}

	public String getZipCode4() {
		return zipCode4;
	}

	public void setZipCode4(String zipCode4) {
		this.zipCode4 = zipCode4;
	}

	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	public Double getTotalTradeAmount() {
		return totalTradeAmount;
	}

	public void setTotalTradeAmount(Double totalTradeAmount) {
		this.totalTradeAmount = totalTradeAmount;
	}

	public Double getPeriodTradeAmount() {
		return periodTradeAmount;
	}

	public void setPeriodTradeAmount(Double periodTradeAmount) {
		this.periodTradeAmount = periodTradeAmount;
	}

	public Date getLastTradeDate() {
		return lastTradeDate;
	}

	public void setLastTradeDate(Date lastTradeDate) {
		this.lastTradeDate = lastTradeDate;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccepter() {
		return accepter;
	}

	public void setAccepter(String accepter) {
		this.accepter = accepter;
	}

	public String getAcceptTel() {
		return acceptTel;
	}

	public void setAcceptTel(String acceptTel) {
		this.acceptTel = acceptTel;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getARemark1() {
		return aRemark1;
	}

	public void setARemark1(String aRemark1) {
		this.aRemark1 = aRemark1;
	}

	public String getARemark2() {
		return aRemark2;
	}

	public void setARemark2(String aRemark2) {
		this.aRemark2 = aRemark2;
	}

	public String getARemark3() {
		return aRemark3;
	}

	public void setARemark3(String aRemark3) {
		this.aRemark3 = aRemark3;
	}

	public String getARemark4() {
		return aRemark4;
	}

	public void setARemark4(String aRemark4) {
		this.aRemark4 = aRemark4;
	}

	public String getARemark5() {
		return aRemark5;
	}

	public void setARemark5(String aRemark5) {
		this.aRemark5 = aRemark5;
	}

	public String getARemark6() {
		return aRemark6;
	}

	public void setARemark6(String aRemark6) {
		this.aRemark6 = aRemark6;
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

	public String getDefault_warehouse_code() {
		return default_warehouse_code;
	}

	public void setDefault_warehouse_code(String default_warehouse_code) {
		this.default_warehouse_code = default_warehouse_code;
	}

}