package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuCustomer entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuCustomer implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4370632713685074245L;
    // Fields
    private BuCustomerId id;
    private Long addressBookId;
    private String customerTypeCode;
    private String invoiceTypeCode;
    private String taxType;
    private Double taxRate;
    private String currencyCode;
    private String invoiceDeliveryCode;
    private String paymentTermCode;
    private String billStyleCode;
    private String creditGrade;
    private String vipTypeCode;
    private String vipLevel;
    private Date vipStartDate;
    private Date vipEndDate;
    private Date applicationDate;
    private Double totalExpendAmount;
    private Double periodExpendAmount;
    private Date lastTradeDate;
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
    private String remark1;
    private String remark2;
    private String remark3;
    private String enable;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private String password;
    private String isFirstLogin;
    private Date firstLoginDate;
    private Date lastLoginDate;
    private Long totalBonusAmount;
    private Long minusBonusAmount;
    private String accountCode;
    private String isCurrentUse;
    private String remark4;
    private String remark5;
    private String remark6;
    
    
    private String identityCode;
    private String chineseName;
    private String englishName;
    private String shortName;
    private String birthdayYear;
    private String birthdayMonth;
    private String birthdayDay;
    
	//MACO 2016.11.29 鐘錶額度
/*    private Long stockCredits;
    private Long adjCredits;
    private Long totalUncommitCredits;*/
    
    
    // Constructors

	/** default constructor */
    public BuCustomer() {
    }

    /** minimal constructor */
    public BuCustomer(BuCustomerId id, Long addressBookId, String currencyCode) {
	this.id = id;
	this.addressBookId = addressBookId;
	this.currencyCode = currencyCode;
    }

    /** full constructor */

    public BuCustomer(BuCustomerId id, Long addressBookId,
			String customerTypeCode, String invoiceTypeCode, String taxType,
			Double taxRate, String currencyCode, String invoiceDeliveryCode,
			String paymentTermCode, String billStyleCode, String creditGrade,
			String vipTypeCode, String vipLevel, Date vipStartDate,
			Date vipEndDate, Date applicationDate, Double totalExpendAmount,
			Double periodExpendAmount, Date lastTradeDate, String category01,
			String category02, String category03, String category04,
			String category05, String category06, String category07,
			String category08, String category09, String category10,
			String category11, String category12, String category13,
			String category14, String category15, String category16,
			String category17, String category18, String category19,
			String category20, String contactPerson1, String tel1, String fax1,
			String mail1, String contactPerson2, String tel2, String fax2,
			String mail2, String contactPerson3, String tel3, String fax3,
			String mail3, String contactPerson4, String tel4, String fax4,
			String mail4, String city1, String area1, String zipCode1,
			String address1, String city2, String area2, String zipCode2,
			String address2, String city3, String area3, String zipCode3,
			String address3, String city4, String area4, String zipCode4,
			String address4, String remark1, String remark2, String remark3,
			String enable, String createdBy, Date creationDate,
			String lastUpdatedBy, Date lastUpdateDate, String password,
			String isFirstLogin, Date firstLoginDate, Date lastLoginDate,
			Long totalBonusAmount, Long minusBonusAmount, String accountCode,
			String isCurrentUse, String remark4, String remark5, String remark6) {
		this.id = id;
		this.addressBookId = addressBookId;
		this.customerTypeCode = customerTypeCode;
		this.invoiceTypeCode = invoiceTypeCode;
		this.taxType = taxType;
		this.taxRate = taxRate;
		this.currencyCode = currencyCode;
		this.invoiceDeliveryCode = invoiceDeliveryCode;
		this.paymentTermCode = paymentTermCode;
		this.billStyleCode = billStyleCode;
		this.creditGrade = creditGrade;
		this.vipTypeCode = vipTypeCode;
		this.vipLevel = vipLevel;
		this.vipStartDate = vipStartDate;
		this.vipEndDate = vipEndDate;
		this.applicationDate = applicationDate;
		this.totalExpendAmount = totalExpendAmount;
		this.periodExpendAmount = periodExpendAmount;
		this.lastTradeDate = lastTradeDate;
		this.category01 = category01;
		this.category02 = category02;
		this.category03 = category03;
		this.category04 = category04;
		this.category05 = category05;
		this.category06 = category06;
		this.category07 = category07;
		this.category08 = category08;
		this.category09 = category09;
		this.category10 = category10;
		this.category11 = category11;
		this.category12 = category12;
		this.category13 = category13;
		this.category14 = category14;
		this.category15 = category15;
		this.category16 = category16;
		this.category17 = category17;
		this.category18 = category18;
		this.category19 = category19;
		this.category20 = category20;
		this.contactPerson1 = contactPerson1;
		this.tel1 = tel1;
		this.fax1 = fax1;
		EMail1 = mail1;
		this.contactPerson2 = contactPerson2;
		this.tel2 = tel2;
		this.fax2 = fax2;
		EMail2 = mail2;
		this.contactPerson3 = contactPerson3;
		this.tel3 = tel3;
		this.fax3 = fax3;
		EMail3 = mail3;
		this.contactPerson4 = contactPerson4;
		this.tel4 = tel4;
		this.fax4 = fax4;
		EMail4 = mail4;
		this.city1 = city1;
		this.area1 = area1;
		this.zipCode1 = zipCode1;
		this.address1 = address1;
		this.city2 = city2;
		this.area2 = area2;
		this.zipCode2 = zipCode2;
		this.address2 = address2;
		this.city3 = city3;
		this.area3 = area3;
		this.zipCode3 = zipCode3;
		this.address3 = address3;
		this.city4 = city4;
		this.area4 = area4;
		this.zipCode4 = zipCode4;
		this.address4 = address4;
		this.remark1 = remark1;
		this.remark2 = remark2;
		this.remark3 = remark3;
		this.enable = enable;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.password = password;
		this.isFirstLogin = isFirstLogin;
		this.firstLoginDate = firstLoginDate;
		this.lastLoginDate = lastLoginDate;
		this.totalBonusAmount = totalBonusAmount;
		this.minusBonusAmount = minusBonusAmount;
		this.accountCode = accountCode;
		this.isCurrentUse = isCurrentUse;
		this.remark4 = remark4;
		this.remark5 = remark5;
		this.remark6 = remark6;
		
	}    
    

    // Property accessors

    public BuCustomerId getId() {
	return this.id;
    }

    public void setId(BuCustomerId id) {
	this.id = id;
    }

    public Long getAddressBookId() {
	return this.addressBookId;
    }

    public void setAddressBookId(Long addressBookId) {
	this.addressBookId = addressBookId;
    }

    public String getCustomerTypeCode() {
	return this.customerTypeCode;
    }

    public void setCustomerTypeCode(String customerTypeCode) {
	this.customerTypeCode = customerTypeCode;
    }

    public String getInvoiceTypeCode() {
	return this.invoiceTypeCode;
    }

    public void setInvoiceTypeCode(String invoiceTypeCode) {
	this.invoiceTypeCode = invoiceTypeCode;
    }

    public String getTaxType() {
	return this.taxType;
    }

    public void setTaxType(String taxType) {
	this.taxType = taxType;
    }

    public Double getTaxRate() {
	return this.taxRate;
    }

    public void setTaxRate(Double taxRate) {
	this.taxRate = taxRate;
    }

    public String getCurrencyCode() {
	return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
	this.currencyCode = currencyCode;
    }

    public String getInvoiceDeliveryCode() {
	return this.invoiceDeliveryCode;
    }

    public void setInvoiceDeliveryCode(String invoiceDeliveryCode) {
	this.invoiceDeliveryCode = invoiceDeliveryCode;
    }

    public String getPaymentTermCode() {
	return this.paymentTermCode;
    }

    public void setPaymentTermCode(String paymentTermCode) {
	this.paymentTermCode = paymentTermCode;
    }

    public String getBillStyleCode() {
	return this.billStyleCode;
    }

    public void setBillStyleCode(String billStyleCode) {
	this.billStyleCode = billStyleCode;
    }

    public String getCreditGrade() {
	return this.creditGrade;
    }

    public void setCreditGrade(String creditGrade) {
	this.creditGrade = creditGrade;
    }

    public String getVipTypeCode() {
	return this.vipTypeCode;
    }

    public void setVipTypeCode(String vipTypeCode) {
	this.vipTypeCode = vipTypeCode;
    }

    public String getVipLevel() {
	return this.vipLevel;
    }

    public void setVipLevel(String vipLevel) {
	this.vipLevel = vipLevel;
    }

    public Date getVipStartDate() {
	return this.vipStartDate;
    }

    public void setVipStartDate(Date vipStartDate) {
	this.vipStartDate = vipStartDate;
    }

    public Date getVipEndDate() {
	return this.vipEndDate;
    }

    public void setVipEndDate(Date vipEndDate) {
	this.vipEndDate = vipEndDate;
    }

    public Date getApplicationDate() {
	return this.applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
	this.applicationDate = applicationDate;
    }

    public Double getTotalExpendAmount() {
	return this.totalExpendAmount;
    }

    public void setTotalExpendAmount(Double totalExpendAmount) {
	this.totalExpendAmount = totalExpendAmount;
    }

    public Double getPeriodExpendAmount() {
	return this.periodExpendAmount;
    }

    public void setPeriodExpendAmount(Double periodExpendAmount) {
	this.periodExpendAmount = periodExpendAmount;
    }

    public Date getLastTradeDate() {
	return this.lastTradeDate;
    }

    public void setLastTradeDate(Date lastTradeDate) {
	this.lastTradeDate = lastTradeDate;
    }

    public String getCategory01() {
	return this.category01;
    }

    public void setCategory01(String category01) {
	this.category01 = category01;
    }

    public String getCategory02() {
	return this.category02;
    }

    public void setCategory02(String category02) {
	this.category02 = category02;
    }

    public String getCategory03() {
	return this.category03;
    }

    public void setCategory03(String category03) {
	this.category03 = category03;
    }

    public String getCategory04() {
	return this.category04;
    }

    public void setCategory04(String category04) {
	this.category04 = category04;
    }

    public String getCategory05() {
	return this.category05;
    }

    public void setCategory05(String category05) {
	this.category05 = category05;
    }

    public String getCategory06() {
	return this.category06;
    }

    public void setCategory06(String category06) {
	this.category06 = category06;
    }

    public String getCategory07() {
	return this.category07;
    }

    public void setCategory07(String category07) {
	this.category07 = category07;
    }

    public String getCategory08() {
	return this.category08;
    }

    public void setCategory08(String category08) {
	this.category08 = category08;
    }

    public String getCategory09() {
	return this.category09;
    }

    public void setCategory09(String category09) {
	this.category09 = category09;
    }

    public String getCategory10() {
	return this.category10;
    }

    public void setCategory10(String category10) {
	this.category10 = category10;
    }

    public String getCategory11() {
	return this.category11;
    }

    public void setCategory11(String category11) {
	this.category11 = category11;
    }

    public String getCategory12() {
	return this.category12;
    }

    public void setCategory12(String category12) {
	this.category12 = category12;
    }

    public String getCategory13() {
	return this.category13;
    }

    public void setCategory13(String category13) {
	this.category13 = category13;
    }

    public String getCategory14() {
	return this.category14;
    }

    public void setCategory14(String category14) {
	this.category14 = category14;
    }

    public String getCategory15() {
	return this.category15;
    }

    public void setCategory15(String category15) {
	this.category15 = category15;
    }

    public String getCategory16() {
	return this.category16;
    }

    public void setCategory16(String category16) {
	this.category16 = category16;
    }

    public String getCategory17() {
	return this.category17;
    }

    public void setCategory17(String category17) {
	this.category17 = category17;
    }

    public String getCategory18() {
	return this.category18;
    }

    public void setCategory18(String category18) {
	this.category18 = category18;
    }

    public String getCategory19() {
	return this.category19;
    }

    public void setCategory19(String category19) {
	this.category19 = category19;
    }

    public String getCategory20() {
	return this.category20;
    }

    public void setCategory20(String category20) {
	this.category20 = category20;
    }

    public String getContactPerson1() {
	return this.contactPerson1;
    }

    public void setContactPerson1(String contactPerson1) {
	this.contactPerson1 = contactPerson1;
    }

    public String getTel1() {
	return this.tel1;
    }

    public void setTel1(String tel1) {
	this.tel1 = tel1;
    }

    public String getFax1() {
	return this.fax1;
    }

    public void setFax1(String fax1) {
	this.fax1 = fax1;
    }

    public String getEMail1() {
	return this.EMail1;
    }

    public void setEMail1(String EMail1) {
	this.EMail1 = EMail1;
    }

    public String getContactPerson2() {
	return this.contactPerson2;
    }

    public void setContactPerson2(String contactPerson2) {
	this.contactPerson2 = contactPerson2;
    }

    public String getTel2() {
	return this.tel2;
    }

    public void setTel2(String tel2) {
	this.tel2 = tel2;
    }

    public String getFax2() {
	return this.fax2;
    }

    public void setFax2(String fax2) {
	this.fax2 = fax2;
    }

    public String getEMail2() {
	return this.EMail2;
    }

    public void setEMail2(String EMail2) {
	this.EMail2 = EMail2;
    }

    public String getContactPerson3() {
	return this.contactPerson3;
    }

    public void setContactPerson3(String contactPerson3) {
	this.contactPerson3 = contactPerson3;
    }

    public String getTel3() {
	return this.tel3;
    }

    public void setTel3(String tel3) {
	this.tel3 = tel3;
    }

    public String getFax3() {
	return this.fax3;
    }

    public void setFax3(String fax3) {
	this.fax3 = fax3;
    }

    public String getEMail3() {
	return this.EMail3;
    }

    public void setEMail3(String EMail3) {
	this.EMail3 = EMail3;
    }

    public String getContactPerson4() {
	return this.contactPerson4;
    }

    public void setContactPerson4(String contactPerson4) {
	this.contactPerson4 = contactPerson4;
    }

    public String getTel4() {
	return this.tel4;
    }

    public void setTel4(String tel4) {
	this.tel4 = tel4;
    }

    public String getFax4() {
	return this.fax4;
    }

    public void setFax4(String fax4) {
	this.fax4 = fax4;
    }

    public String getEMail4() {
	return this.EMail4;
    }

    public void setEMail4(String EMail4) {
	this.EMail4 = EMail4;
    }

    public String getCity1() {
	return this.city1;
    }

    public void setCity1(String city1) {
	this.city1 = city1;
    }

    public String getArea1() {
	return this.area1;
    }

    public void setArea1(String area1) {
	this.area1 = area1;
    }

    public String getZipCode1() {
	return this.zipCode1;
    }

    public void setZipCode1(String zipCode1) {
	this.zipCode1 = zipCode1;
    }

    public String getAddress1() {
	return this.address1;
    }

    public void setAddress1(String address1) {
	this.address1 = address1;
    }

    public String getCity2() {
	return this.city2;
    }

    public void setCity2(String city2) {
	this.city2 = city2;
    }

    public String getArea2() {
	return this.area2;
    }

    public void setArea2(String area2) {
	this.area2 = area2;
    }

    public String getZipCode2() {
	return this.zipCode2;
    }

    public void setZipCode2(String zipCode2) {
	this.zipCode2 = zipCode2;
    }

    public String getAddress2() {
	return this.address2;
    }

    public void setAddress2(String address2) {
	this.address2 = address2;
    }

    public String getCity3() {
	return this.city3;
    }

    public void setCity3(String city3) {
	this.city3 = city3;
    }

    public String getArea3() {
	return this.area3;
    }

    public void setArea3(String area3) {
	this.area3 = area3;
    }

    public String getZipCode3() {
	return this.zipCode3;
    }

    public void setZipCode3(String zipCode3) {
	this.zipCode3 = zipCode3;
    }

    public String getAddress3() {
	return this.address3;
    }

    public void setAddress3(String address3) {
	this.address3 = address3;
    }

    public String getCity4() {
	return this.city4;
    }

    public void setCity4(String city4) {
	this.city4 = city4;
    }

    public String getArea4() {
	return this.area4;
    }

    public void setArea4(String area4) {
	this.area4 = area4;
    }

    public String getZipCode4() {
	return this.zipCode4;
    }

    public void setZipCode4(String zipCode4) {
	this.zipCode4 = zipCode4;
    }

    public String getAddress4() {
	return this.address4;
    }

    public void setAddress4(String address4) {
	this.address4 = address4;
    }

    public String getRemark1() {
	return this.remark1;
    }

    public void setRemark1(String remark1) {
	this.remark1 = remark1;
    }

    public String getRemark2() {
	return this.remark2;
    }

    public void setRemark2(String remark2) {
	this.remark2 = remark2;
    }

    public String getRemark3() {
	return this.remark3;
    }

    public void setRemark3(String remark3) {
	this.remark3 = remark3;
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

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getIsFirstLogin() {
	return isFirstLogin;
    }

    public void setIsFirstLogin(String isFirstLogin) {
	this.isFirstLogin = isFirstLogin;
    }

    public Date getFirstLoginDate() {
	return firstLoginDate;
    }

    public void setFirstLoginDate(Date firstLoginDate) {
	this.firstLoginDate = firstLoginDate;
    }

    public Date getLastLoginDate() {
	return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
	this.lastLoginDate = lastLoginDate;
    }

    public Long getTotalBonusAmount() {
	return totalBonusAmount;
    }

    public void setTotalBonusAmount(Long totalBonusAmount) {
	this.totalBonusAmount = totalBonusAmount;
    }

    public Long getMinusBonusAmount() {
	return minusBonusAmount;
    }

    public void setMinusBonusAmount(Long minusBonusAmount) {
	this.minusBonusAmount = minusBonusAmount;
    }

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getIsCurrentUse() {
		return isCurrentUse;
	}

	public void setIsCurrentUse(String isCurrentUse) {
		this.isCurrentUse = isCurrentUse;
	}

	public String getRemark4() {
		return remark4;
	}

	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}

	public String getRemark5() {
		return remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

	public String getRemark6() {
		return remark6;
	}

	public void setRemark6(String remark6) {
		this.remark6 = remark6;
	}

	public String getIdentityCode() {
		return identityCode;
	}

	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
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

	public String getBirthdayYear() {
		return birthdayYear;
	}

	public void setBirthdayYear(String birthdayYear) {
		this.birthdayYear = birthdayYear;
	}

	public String getBirthdayMonth() {
		return birthdayMonth;
	}

	public void setBirthdayMonth(String birthdayMonth) {
		this.birthdayMonth = birthdayMonth;
	}

	public String getBirthdayDay() {
		return birthdayDay;
	}

	public void setBirthdayDay(String birthdayDay) {
		this.birthdayDay = birthdayDay;
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
	}*/

}