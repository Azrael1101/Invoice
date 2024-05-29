package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SiSystemLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiSystemLog implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4490342395235652692L;
	// Fields
	private Long id;
	private String processName;
	private String logLevel;
	private String message;
	private String executeDate;
	private String processLotNo;
	private String reserve1; //shop code
	private String reserve2; //pos machine code
	private String reserve3; //master file name
	private String reserve4; //detail file name
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String brandCode;
	private String action;
	private String executeTime;
	private String machineCode;
	

	// Constructors

	/** default constructor */
	public SiSystemLog() {
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProcessName() {
		return this.processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getLogLevel() {
		return this.logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExecuteDate() {
		return this.executeDate;
	}

	public void setExecuteDate(String executeDate) {
		this.executeDate = executeDate;
	}

	public String getProcessLotNo() {
		return processLotNo;
	}

	public void setProcessLotNo(String processLotNo) {
		this.processLotNo = processLotNo;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	
	
}