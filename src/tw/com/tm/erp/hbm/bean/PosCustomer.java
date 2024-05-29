package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * PosCustomerId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosCustomer implements java.io.Serializable {

	// Fields

	private String dataId;
	private Long headId;
	private String action;
	private String brandCode;
	private String customerCode;
	private String chineseName;
	private String identityCode;
	private Long birthday;
	private String gender;
	private String mobilePhone;
	private String tel2;
	private String EMail;
	private String category01;
	private String category03;
	private String companyName;
	private String unifiedSerialNumber;
	private String tel1;
	private String fax1;
	private String address;
	private String address3;
	private String vipTypeCode;
	private Date vipStartDate;
	private Date vipEndDate;
	private String category07;
	private String category08;
	private String category09;
	private String category10;
	private String countryCode;
	private Double totalExpendAmount;
	private Double periodExpendAmount;
	private String identification;
	
	
	// Constructors

	/** default constructor */
	public PosCustomer() {
	}


	// Property accessors

	public String getDataId() {
		return this.dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getCustomerCode() {
		return this.customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getChineseName() {
		return chineseName;
	}


	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	
	public String getIdentityCode() {
		return this.identityCode;
	}

	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}

	public Long getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Long birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getTel2() {
		return this.tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getEMail() {
		return this.EMail;
	}

	public void setEMail(String EMail) {
		this.EMail = EMail;
	}

	public String getCategory01() {
		return this.category01;
	}

	public void setCategory01(String category01) {
		this.category01 = category01;
	}

	public String getCategory03() {
		return this.category03;
	}

	public void setCategory03(String category03) {
		this.category03 = category03;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUnifiedSerialNumber() {
		return this.unifiedSerialNumber;
	}

	public void setUnifiedSerialNumber(String unifiedSerialNumber) {
		this.unifiedSerialNumber = unifiedSerialNumber;
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

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress3() {
		return this.address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getVipTypeCode() {
		return this.vipTypeCode;
	}

	public void setVipTypeCode(String vipTypeCode) {
		this.vipTypeCode = vipTypeCode;
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

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}


	public Double getTotalExpendAmount() {
		return totalExpendAmount;
	}


	public void setTotalExpendAmount(Double totalExpendAmount) {
		this.totalExpendAmount = totalExpendAmount;
	}


	public Double getPeriodExpendAmount() {
		return periodExpendAmount;
	}


	public void setPeriodExpendAmount(Double periodExpendAmount) {
		this.periodExpendAmount = periodExpendAmount;
	}


	public String getIdentification() {
		return identification;
	}


	public void setIdentification(String identification) {
		this.identification = identification;
	}
}