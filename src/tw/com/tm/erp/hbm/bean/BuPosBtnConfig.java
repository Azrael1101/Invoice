package tw.com.tm.erp.hbm.bean;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * SoSalesOrderHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuPosBtnConfig implements java.io.Serializable {

	// Fields
	private Long headId;
	private String brandCode;
	private String functionCode;
	private String functionName;
	private String functionInput;
	private String functionOutput;
	private String enable;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private String functionImg;
	private String functionComment;

	// Constructors
	/** default constructor */
	public BuPosBtnConfig() {
	}

	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionInput() {
		return functionInput;
	}

	public void setFunctionInput(String functionInput) {
		this.functionInput = functionInput;
	}

	public String getFunctionOutput() {
		return functionOutput;
	}

	public void setFunctionOutput(String functionOutput) {
		this.functionOutput = functionOutput;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
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

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getFunctionImg() {
		return functionImg;
	}

	public void setFunctionImg(String functionImg) {
		this.functionImg = functionImg;
	}

	public String getFunctionComment() {
		return functionComment;
	}

	public void setFunctionComment(String functionComment) {
		this.functionComment = functionComment;
	}
}