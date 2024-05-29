package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BuCustomerCardId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuCustomerCard implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7371407511293659725L;
    // Fields
    private Long headId;
    private String cardNo;		
    private String brandCode;		// 對應buCustomer key
    private String customerCode;	// 對應buCustomer key
    private String cardType;
    private String maritalStatus;
    private String educationalBackground;
    private String nationality;
    private String businessCode;
    private String classCode;
    private Long yearIncome;
    private String seniority;
    private String isNeedDm;
    private String homeRegionCode;
    private String homeTel1;
    private String companyRegionCode;
    private String companyTel1;
    private String companyExtension;
    private String gender1;
    private String birthday1;
    private String gender2;
    private String birthday2;
    private String gender3;
    private String birthday3;
    private String issueDate;
    private String expirationDate;
    private String professionalTitle;
    private String name1;
    private String name2;
    private String name3;
    private String enable;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private Long indexNo;
    private String isTransmission;
    private String cardCategory;
    private String recommended; // 推薦人id
    private Date openDate;	// 開卡日
    private List<BuCustomerCardEvent> buCustomerCardEvents = new ArrayList(0);
    
    public String getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }

    // Constructors
    /** default constructor */
    public BuCustomerCard() {
    }

    /** full constructor */
    // Property accessors

    public String getCardNo() {
	return this.cardNo;
    }

    public void setCardNo(String cardNo) {
	this.cardNo = cardNo;
    }

    public String getCardType() {
	return this.cardType;
    }

    public void setCardType(String cardType) {
	this.cardType = cardType;
    }

    public String getMaritalStatus() {
	return this.maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
	this.maritalStatus = maritalStatus;
    }

    public String getEducationalBackground() {
	return this.educationalBackground;
    }

    public void setEducationalBackground(String educationalBackground) {
	this.educationalBackground = educationalBackground;
    }

    public String getNationality() {
	return this.nationality;
    }

    public void setNationality(String nationality) {
	this.nationality = nationality;
    }

    public String getBusinessCode() {
	return this.businessCode;
    }

    public void setBusinessCode(String businessCode) {
	this.businessCode = businessCode;
    }

    public String getClassCode() {
	return this.classCode;
    }

    public void setClassCode(String classCode) {
	this.classCode = classCode;
    }

    public Long getYearIncome() {
	return this.yearIncome;
    }

    public void setYearIncome(Long yearIncome) {
	this.yearIncome = yearIncome;
    }

    public String getSeniority() {
	return this.seniority;
    }

    public void setSeniority(String seniority) {
	this.seniority = seniority;
    }

    public String getIsNeedDm() {
	return this.isNeedDm;
    }

    public void setIsNeedDm(String isNeedDm) {
	this.isNeedDm = isNeedDm;
    }

    public String getHomeRegionCode() {
	return this.homeRegionCode;
    }

    public void setHomeRegionCode(String homeRegionCode) {
	this.homeRegionCode = homeRegionCode;
    }

    public String getHomeTel1() {
	return this.homeTel1;
    }

    public void setHomeTel1(String homeTel1) {
	this.homeTel1 = homeTel1;
    }

    public String getCompanyRegionCode() {
	return this.companyRegionCode;
    }

    public void setCompanyRegionCode(String companyRegionCode) {
	this.companyRegionCode = companyRegionCode;
    }

    public String getCompanyTel1() {
	return this.companyTel1;
    }

    public void setCompanyTel1(String companyTel1) {
	this.companyTel1 = companyTel1;
    }

    public String getCompanyExtension() {
	return this.companyExtension;
    }

    public void setCompanyExtension(String companyExtension) {
	this.companyExtension = companyExtension;
    }

    public String getGender1() {
	return this.gender1;
    }

    public void setGender1(String gender1) {
	this.gender1 = gender1;
    }

    public String getBirthday1() {
	return this.birthday1;
    }

    public void setBirthday1(String birthday1) {
	this.birthday1 = birthday1;
    }

    public String getGender2() {
	return this.gender2;
    }

    public void setGender2(String gender2) {
	this.gender2 = gender2;
    }

    public String getBirthday2() {
	return this.birthday2;
    }

    public void setBirthday2(String birthday2) {
	this.birthday2 = birthday2;
    }

    public String getGender3() {
	return this.gender3;
    }

    public void setGender3(String gender3) {
	this.gender3 = gender3;
    }

    public String getBirthday3() {
	return this.birthday3;
    }

    public void setBirthday3(String birthday3) {
	this.birthday3 = birthday3;
    }

    public String getIssueDate() {
	return this.issueDate;
    }

    public void setIssueDate(String issueDate) {
	this.issueDate = issueDate;
    }

    public String getExpirationDate() {
	return this.expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
	this.expirationDate = expirationDate;
    }

    public String getProfessionalTitle() {
	return this.professionalTitle;
    }

    public void setProfessionalTitle(String professionalTitle) {
	this.professionalTitle = professionalTitle;
    }

    public String getName1() {
	return this.name1;
    }

    public void setName1(String name1) {
	this.name1 = name1;
    }

    public String getName2() {
	return this.name2;
    }

    public void setName2(String name2) {
	this.name2 = name2;
    }

    public String getName3() {
	return this.name3;
    }

    public void setName3(String name3) {
	this.name3 = name3;
    }

    public String getEnable() {
	return this.enable;
    }

    public void setEnable(String enable) {
	this.enable = enable;
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

    public Long getIndexNo() {
	return this.indexNo;
    }

    public void setIndexNo(Long indexNo) {
	this.indexNo = indexNo;
    }

    public List<BuCustomerCardEvent> getBuCustomerCardEvents() {
        return buCustomerCardEvents;
    }

    public void setBuCustomerCardEvents(
    	List<BuCustomerCardEvent> buCustomerCardEvents) {
        this.buCustomerCardEvents = buCustomerCardEvents;
    }

    public String getIsTransmission() {
        return isTransmission;
    }

    public void setIsTransmission(String isTransmission) {
        this.isTransmission = isTransmission;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Long getHeadId() {
        return headId;
    }

    public void setHeadId(Long headId) {
        this.headId = headId;
    }

    public String getRecommended() {
        return recommended;
    }

    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }
}