package tw.com.tm.erp.utils;

import java.io.Serializable;

public class User implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5265717195988710096L;
   
    private String organizationCode;  
    private String brandCode;
    private String brandName;  
    private String employeeCode;    
    private String identityCode;    
    private String loginName;
    private String reportLoginName;
    private String reportPassword;
    private String chineseName;
    private String englishName;
    private String keyInMode;
    private String department;
    private String employeePosition;
    private String eMail;   
    private UserProgramRight userProgramRight;
    
    public String getOrganizationCode() {
        return organizationCode;
    }
    
    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }
    
    public String getBrandCode() {
        return brandCode;
    }
    
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }
    
    public String getBrandName() {
        return brandName;
    }
    
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    
    public String getEmployeeCode() {
        return employeeCode;
    }
    
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }
    
    public String getIdentityCode() {
        return identityCode;
    }
    
    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }
    
    public String getLoginName() {
        return loginName;
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
    
    public String getKeyInMode() {
		return keyInMode;
	}

	public void setKeyInMode(String keyInMode) {
		this.keyInMode = keyInMode;
	}

	public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getEmployeePosition() {
        return employeePosition;
    }
    
    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }
    
    public String getEMail() {
        return eMail;
    }
    
    public void setEMail(String mail) {
        eMail = mail;
    }
    
    public UserProgramRight getUserProgramRight() {
        return userProgramRight;
    }
    
    public void setUserProgramRight(UserProgramRight userProgramRight) {
        this.userProgramRight = userProgramRight;
    }
}
