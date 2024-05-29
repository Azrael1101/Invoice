package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * PosEmployee entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosEmployee implements java.io.Serializable {

    // Fields

    private String employeeCode;
    private String dataId;
    private Long headId;
    private String action;
    private String brandCode;
    private String chineseName;
    private Date arriveDate;
    private Date leaveDate;
    private String employeeDepartment;
    private String employeeDepartmentName;
    private String EMailCompany;
    private Date appointmentDate;
    private String password;

    // Constructors

    /** default constructor */
    public PosEmployee() {
    }

    // Property accessors

    public String getEmployeeCode() {
	return this.employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
	this.employeeCode = employeeCode;
    }

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
	return this.action;
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

    public String getChineseName() {
	return this.chineseName;
    }

    public void setChineseName(String chineseName) {
	this.chineseName = chineseName;
    }

    public Date getArriveDate() {
	return arriveDate;
    }

    public void setArriveDate(Date arriveDate) {
	this.arriveDate = arriveDate;
    }

    public Date getLeaveDate() {
	return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
	this.leaveDate = leaveDate;
    }

    public String getEmployeeDepartment() {
	return employeeDepartment;
    }

    public void setEmployeeDepartment(String employeeDepartment) {
	this.employeeDepartment = employeeDepartment;
    }

    public String getEmployeeDepartmentName() {
	return employeeDepartmentName;
    }

    public void setEmployeeDepartmentName(String employeeDepartmentName) {
	this.employeeDepartmentName = employeeDepartmentName;
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