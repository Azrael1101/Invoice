package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImPromotion entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosCommand implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -288726790301017844L;
	private Long batchId;
	private String type;
	private Long requestId;
	private Long responseId;
	private String brandCode;
	private String action;
	private String dataType;
	private String dataId;
	private String operation;
	private String machineCode;
	private Long numbers;
	private String status;
	private Date requestDate;
	private Date responseDate;
	private Date scheduleDate;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private String lastUpdatedBy;
	
	public Date getScheduleDate() {
	    return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
	    this.scheduleDate = scheduleDate;
	}

	public PosCommand() {
	    super();
	}
	
	public Long getBatchId() {
	    return batchId;
	}
	public void setBatchId(Long batchId) {
	    this.batchId = batchId;
	}
	public String getType() {
	    return type;
	}
	public void setType(String type) {
	    this.type = type;
	}
	public Long getRequestId() {
	    return requestId;
	}
	public void setRequestId(Long requestId) {
	    this.requestId = requestId;
	}
	public Long getResponseId() {
	    return responseId;
	}
	public void setResponseId(Long responseId) {
	    this.responseId = responseId;
	}
	public String getBrandCode() {
	    return brandCode;
	}
	public void setBrandCode(String brandCode) {
	    this.brandCode = brandCode;
	}
	public String getAction() {
	    return action;
	}
	public void setAction(String action) {
	    this.action = action;
	}
	public String getDataType() {
	    return dataType;
	}
	public void setDataType(String dataType) {
	    this.dataType = dataType;
	}
	public String getDataId() {
	    return dataId;
	}
	public void setDataId(String dataId) {
	    this.dataId = dataId;
	}
	public String getOperation() {
	    return operation;
	}
	public void setOperation(String operation) {
	    this.operation = operation;
	}
	public String getMachineCode() {
	    return machineCode;
	}
	public void setMachineCode(String machineCode) {
	    this.machineCode = machineCode;
	}
	public Long getNumbers() {
	    return numbers;
	}
	public void setNumbers(Long numbers) {
	    this.numbers = numbers;
	}
	public String getStatus() {
	    return status;
	}
	public void setStatus(String status) {
	    this.status = status;
	}
	public Date getRequestDate() {
	    return requestDate;
	}
	public void setRequestDate(Date requestDate) {
	    this.requestDate = requestDate;
	}
	public Date getResponseDate() {
	    return responseDate;
	}
	public void setResponseDate(Date responseDate) {
	    this.responseDate = responseDate;
	}
	public String getReserve1() {
	    return reserve1;
	}
	public void setReserve1(String reserve1) {
	    this.reserve1 = reserve1;
	}
	public String getReserve2() {
	    return reserve2;
	}
	public void setReserve2(String reserve2) {
	    this.reserve2 = reserve2;
	}
	public String getReserve3() {
	    return reserve3;
	}
	public void setReserve3(String reserve3) {
	    this.reserve3 = reserve3;
	}
	public String getReserve4() {
	    return reserve4;
	}
	public void setReserve4(String reserve4) {
	    this.reserve4 = reserve4;
	}
	public String getReserve5() {
	    return reserve5;
	}
	public void setReserve5(String reserve5) {
	    this.reserve5 = reserve5;
	}
	public String getCreatedBy() {
	    return createdBy;
	}
	public void setCreatedBy(String createdBy) {
	    this.createdBy = createdBy;
	}
	public String getLastUpdatedBy() {
	    return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
	    this.lastUpdatedBy = lastUpdatedBy;
	}
	
	
}