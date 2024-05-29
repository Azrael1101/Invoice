package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SiFunction entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiFunction implements java.io.Serializable {

    // Fields

    private String functionCode;
    private String functionName;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String updatedBy;
    private Date updateDate;
    private List siFunctionObject = new ArrayList(0);
    
    // Constructors

    /** default constructor */
    public SiFunction() {
    }

    /** minimal constructor */
    public SiFunction(String functionCode) {
	this.functionCode = functionCode;
    }

    /** full constructor */
    public SiFunction(String functionCode, String functionName,
	    String reserve1, String reserve2, String reserve3, String reserve4,
	    String reserve5, String createdBy, Date creationDate,
	    String updatedBy, Date updateDate, List siFunctionObject) {
	this.functionCode = functionCode;
	this.functionName = functionName;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.updatedBy = updatedBy;
	this.updateDate = updateDate;
	this.siFunctionObject = siFunctionObject;
    }

    // Property accessors

    public String getFunctionCode() {
	return this.functionCode;
    }

    public void setFunctionCode(String functionCode) {
	this.functionCode = functionCode;
    }

    public String getFunctionName() {
	return this.functionName;
    }

    public void setFunctionName(String functionName) {
	this.functionName = functionName;
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

    public String getUpdatedBy() {
	return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
	this.updatedBy = updatedBy;
    }

    public Date getUpdateDate() {
	return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

		public List getSiFunctionObject() {
			return siFunctionObject;
		}

		public void setSiFunctionObject(List siFunctionObject) {
			this.siFunctionObject = siFunctionObject;
		}

}