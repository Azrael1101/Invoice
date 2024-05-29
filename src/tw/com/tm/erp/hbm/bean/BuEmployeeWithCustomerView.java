package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuEmployeeWithAddressView entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuEmployeeWithCustomerView implements java.io.Serializable {

		private static final long serialVersionUID = -8945869096360846457L;
    private Long addressBookId;
    private String identityCode;
    private String employeeCode;
    private Date arriveDate;
    private Date leaveDate;
    private Long counts;
    
    // Constructors

    /** default constructor */
    public BuEmployeeWithCustomerView() {
    }

    /** full constructor */
    public BuEmployeeWithCustomerView(Long addressBookId, String identityCode, 
    		String employeeCode,  Date arriveDate, Date leaveDate, Long counts) {
    	this.addressBookId = addressBookId;
      	this.identityCode = identityCode;
      	this.employeeCode = employeeCode;
    	this.arriveDate = arriveDate;
    	this.leaveDate = leaveDate;
    	this.counts = counts;
    }

    // Property accessors
	    public Long getAddressBookId() {
	    	return this.addressBookId;
	    }
	
	    public void setAddressBookId(Long addressBookId) {
	    	this.addressBookId = addressBookId;
	    }

		public void setIdentityCode(String identityCode) {
			this.identityCode = identityCode;
		}

		public String getIdentityCode() {
			return identityCode;
		}
		
		public String getEmployeeCode() {
			return employeeCode;
		}

		public void setEmployeeCode(String employeeCode) {
			this.employeeCode = employeeCode;
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

		public Long getCounts() {
			return counts;
		}

		public void setCounts(Long counts) {
			this.counts = counts;
		}
}