package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuAddressBook entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuAddressBook implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6059082286855357151L;
    // Fields
    private Long addressBookId;
    private String organizationCode;
    private String identityCode;
    private String type;
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
    private String EMail;
    private String url;
    private Long capitalization;
    private Long income;
    private Long employees;
    private String industryCode;
    private String city;
    private String area;
    private String zipCode;
    private String address;
    private String routeCode;
    private String contractPerson;
    private String tel1;
    private String tel2;
    private String fax1;
    private String fax2;
    private String mobilePhone;
    private String remark1;
    private String remark2;
    private String remark3;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private String chineseNameBak2;
    private String deliveryAddress; //宅配地址
    // Fields
    private String birthday;
    // Constructors

    /** default constructor */
    public BuAddressBook() {
    }

    /** minimal constructor */
    public BuAddressBook(String organizationCode, String identityCode) {
	this.organizationCode = organizationCode;
	this.identityCode = identityCode;
    }

    /** full constructor */
    public BuAddressBook(String organizationCode, String identityCode,
	    String type, String chineseName, String englishName,
	    String shortName, String companyName, String department,
	    String countryCode, String gender, Long birthdayYear,
	    Long birthdayMonth, Long birthdayDay, String EMail, String url,
	    Long capitalization, Long income, Long employees,
	    String industryCode, String city, String area, String zipCode,
	    String address, String routeCode, String contractPerson,
	    String tel1, String tel2, String fax1, String fax2,
	    String mobilePhone, String remark1, String remark2, String remark3,
	    String createdBy, Date creationDate, String lastUpdatedBy,
	    Date lastUpdateDate) {
	this.organizationCode = organizationCode;
	this.identityCode = identityCode;
	this.type = type;
	this.chineseName = chineseName;
	this.englishName = englishName;
	this.shortName = shortName;
	this.companyName = companyName;
	this.department = department;
	this.countryCode = countryCode;
	this.gender = gender;
	this.birthdayYear = birthdayYear;
	this.birthdayMonth = birthdayMonth;
	this.birthdayDay = birthdayDay;
	this.EMail = EMail;
	this.url = url;
	this.capitalization = capitalization;
	this.income = income;
	this.employees = employees;
	this.industryCode = industryCode;
	this.city = city;
	this.area = area;
	this.zipCode = zipCode;
	this.address = address;
	this.routeCode = routeCode;
	this.contractPerson = contractPerson;
	this.tel1 = tel1;
	this.tel2 = tel2;
	this.fax1 = fax1;
	this.fax2 = fax2;
	this.mobilePhone = mobilePhone;
	this.remark1 = remark1;
	this.remark2 = remark2;
	this.remark3 = remark3;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
    }

    // Property accessors

    public Long getAddressBookId() {
	return this.addressBookId;
    }

    public void setAddressBookId(Long addressBookId) {
	this.addressBookId = addressBookId;
    }

    public String getOrganizationCode() {
	return this.organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
	this.organizationCode = organizationCode;
    }

    public String getIdentityCode() {
	return this.identityCode;
    }

    public void setIdentityCode(String identityCode) {
	this.identityCode = identityCode;
    }

    public String getType() {
	return this.type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getChineseName() {
	return this.chineseName;
    }

    public void setChineseName(String chineseName) {
	this.chineseName = chineseName;
    }

    public String getEnglishName() {
	return this.englishName;
    }

    public void setEnglishName(String englishName) {
	this.englishName = englishName;
    }

    public String getShortName() {
	return this.shortName;
    }

    public void setShortName(String shortName) {
	this.shortName = shortName;
    }

    public String getCompanyName() {
	return this.companyName;
    }

    public void setCompanyName(String companyName) {
	this.companyName = companyName;
    }

    public String getDepartment() {
	return this.department;
    }

    public void setDepartment(String department) {
	this.department = department;
    }

    public String getCountryCode() {
	return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
	this.countryCode = countryCode;
    }

    public String getGender() {
	return this.gender;
    }

    public void setGender(String gender) {
	this.gender = gender;
    }

    public Long getBirthdayYear() {
	return this.birthdayYear;
    }

    public void setBirthdayYear(Long birthdayYear) {
	this.birthdayYear = birthdayYear;
    }

    public Long getBirthdayMonth() {
	return this.birthdayMonth;
    }

    public void setBirthdayMonth(Long birthdayMonth) {
	this.birthdayMonth = birthdayMonth;
    }

    public Long getBirthdayDay() {
	return this.birthdayDay;
    }

    public void setBirthdayDay(Long birthdayDay) {
	this.birthdayDay = birthdayDay;
    }

    public String getEMail() {
	return this.EMail;
    }

    public void setEMail(String EMail) {
	this.EMail = EMail;
    }

    public String getUrl() {
	return this.url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public Long getCapitalization() {
	return this.capitalization;
    }

    public void setCapitalization(Long capitalization) {
	this.capitalization = capitalization;
    }

    public Long getIncome() {
	return this.income;
    }

    public void setIncome(Long income) {
	this.income = income;
    }

    public Long getEmployees() {
	return this.employees;
    }

    public void setEmployees(Long employees) {
	this.employees = employees;
    }

    public String getIndustryCode() {
	return this.industryCode;
    }

    public void setIndustryCode(String industryCode) {
	this.industryCode = industryCode;
    }

    public String getCity() {
	return this.city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getArea() {
	return this.area;
    }

    public void setArea(String area) {
	this.area = area;
    }

    public String getZipCode() {
	return this.zipCode;
    }

    public void setZipCode(String zipCode) {
	this.zipCode = zipCode;
    }

    public String getAddress() {
	return this.address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getRouteCode() {
	return this.routeCode;
    }

    public void setRouteCode(String routeCode) {
	this.routeCode = routeCode;
    }

    public String getContractPerson() {
	return this.contractPerson;
    }

    public void setContractPerson(String contractPerson) {
	this.contractPerson = contractPerson;
    }

    public String getTel1() {
	return this.tel1;
    }

    public void setTel1(String tel1) {
	this.tel1 = tel1;
    }

    public String getTel2() {
	return this.tel2;
    }

    public void setTel2(String tel2) {
	this.tel2 = tel2;
    }

    public String getFax1() {
	return this.fax1;
    }

    public void setFax1(String fax1) {
	this.fax1 = fax1;
    }

    public String getFax2() {
	return this.fax2;
    }

    public void setFax2(String fax2) {
	this.fax2 = fax2;
    }

    public String getMobilePhone() {
	return this.mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
	this.mobilePhone = mobilePhone;
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

	public String getChineseNameBak2() {
		return chineseNameBak2;
	}

	public void setChineseNameBak2(String chineseNameBak2) {
		this.chineseNameBak2 = chineseNameBak2;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

}