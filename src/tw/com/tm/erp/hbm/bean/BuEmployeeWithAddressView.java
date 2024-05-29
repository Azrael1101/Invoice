package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuEmployeeWithAddressView entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuEmployeeWithAddressView implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8945869096360846457L;
	private String employeeCode;
	private Long addressBookId;
	private String brandCode;
	private String organizationCode;
	private String identityCode;
	private String type;
	private String chineseName;
	private String englishName;
	private String shortName;
	private String employeePosition;
	private String isDepartmentManager;
	private String loginName;
	private String reportLoginName;
	private String reportPassword;
	private Date arriveDate;
	private Date leaveDate;
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
	private String fax1;
	private String EMail1;
	private String tel2;
	private String fax2;
	private String EMail2;
	private String tel3;
	private String fax3;
	private String EMail3;
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
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String employeeDepartment;
	private String costDepartment;
	private String employeeZone;
	private String employeeGroup;
	private String employeeType;
	private String workType;
	private String EMailCompany;
	private Date appointmentDate;
	private String password;
	
	// Constructors

	/** default constructor */
	public BuEmployeeWithAddressView() {
	}

	/** minimal constructor */
	public BuEmployeeWithAddressView(String employeeCode, Long addressBookId,
			String brandCode, String organizationCode) {
		this.employeeCode = employeeCode;
		this.addressBookId = addressBookId;
		this.brandCode = brandCode;
		this.organizationCode = organizationCode;
	}

	/** full constructor */

	// Property accessors

	public String getEmployeeCode() {
		return this.employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public Long getAddressBookId() {
		return this.addressBookId;
	}

	public void setAddressBookId(Long addressBookId) {
		this.addressBookId = addressBookId;
	}

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
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

	public String getEmployeePosition() {
		return this.employeePosition;
	}

	public void setEmployeePosition(String employeePosition) {
		this.employeePosition = employeePosition;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getReportLoginName() {
		return reportLoginName;
	}

	public void setReportLoginName(String reportLoginName) {
		this.reportLoginName = reportLoginName;
	}

	public String getReportPassword() {
		return reportPassword;
	}

	public void setReportPassword(String reportPassword) {
		this.reportPassword = reportPassword;
	}

	public Date getArriveDate() {
		return this.arriveDate;
	}

	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}

	public Date getLeaveDate() {
		return this.leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
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

	public String getIsDepartmentManager() {
		return isDepartmentManager;
	}

	public void setIsDepartmentManager(String isDepartmentManager) {
		this.isDepartmentManager = isDepartmentManager;
	}

	public String getEmployeeDepartment() {
		return employeeDepartment;
	}

	public void setEmployeeDepartment(String employeeDepartment) {
		this.employeeDepartment = employeeDepartment;
	}

	public String getCostDepartment() {
		return costDepartment;
	}

	public void setCostDepartment(String costDepartment) {
		this.costDepartment = costDepartment;
	}

	public String getEmployeeZone() {
		return employeeZone;
	}

	public void setEmployeeZone(String employeeZone) {
		this.employeeZone = employeeZone;
	}

	public String getEmployeeGroup() {
		return employeeGroup;
	}

	public void setEmployeeGroup(String employeeGroup) {
		this.employeeGroup = employeeGroup;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getEMailCompany() {
	    return EMailCompany;
	}

	public void setEMailCompany(String mailCompany) {
	    EMailCompany = mailCompany;
	}

	public Date getAppointmentDate() {
	    return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
	    this.appointmentDate = appointmentDate;
	}

	public String getPassword() {
	    return password;
	}

	public void setPassword(String password) {
	    this.password = password;
	}

}