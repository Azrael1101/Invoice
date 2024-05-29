package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BuGoalDeployHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuGoalDeployHead implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = 4258194136850366186L;
    private Long headId;
    private String orderNo;			// 額外欄位 for process  = reserve1欄位
    private String orderTypeCode;	// 額外欄位 for process  = BGD靜態變數
    private String brandCode;
    private String shopCode;
    private String department;
    private Long year;
    private Long month;
    private Double originalGoal;
    private Double actualGoal;
    private Double lastYearGoal;
    private Double lastMonthGoal;
    private String status;
    private String statusName;		// 非db欄位, 查詢用
    private String flowStatus;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private List<BuGoalDeployLine> buGoalDeployLines = new ArrayList();

    // Constructors
    /** default constructor */
    public BuGoalDeployHead() {
    }

    /** minimal constructor */
    public BuGoalDeployHead(Long headId) {
	this.headId = headId;
    }

    /** full constructor */
    public BuGoalDeployHead(Long headId, String orderNo, String orderTypeCode,
	    String brandCode, String shopCode, String department, Long year,
	    Long month, Double originalGoal, Double actualGoal,
	    Double lastYearGoal, Double lastMonthGoal, String status,
	    String statusName, String flowStatus, String reserve1,
	    String reserve2, String reserve3, String reserve4, String reserve5,
	    String createdBy, Date creationDate, String lastUpdatedBy,
	    Date lastUpdateDate, List<BuGoalDeployLine> buGoalDeployLines) {
	super();
	this.headId = headId;
	this.orderNo = orderNo;
	this.orderTypeCode = orderTypeCode;
	this.brandCode = brandCode;
	this.shopCode = shopCode;
	this.department = department;
	this.year = year;
	this.month = month;
	this.originalGoal = originalGoal;
	this.actualGoal = actualGoal;
	this.lastYearGoal = lastYearGoal;
	this.lastMonthGoal = lastMonthGoal;
	this.status = status;
	this.statusName = statusName;
	this.flowStatus = flowStatus;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
	this.buGoalDeployLines = buGoalDeployLines;
    }

    // Property accessors

    public Long getHeadId() {
	return this.headId;
    }

    public void setHeadId(Long headId) {
	this.headId = headId;
    }

    public String getBrandCode() {
	return this.brandCode;
    }

    public void setBrandCode(String brandCode) {
	this.brandCode = brandCode;
    }

    public String getShopCode() {
	return this.shopCode;
    }

    public void setShopCode(String shopCode) {
	this.shopCode = shopCode;
    }

    public String getDepartment() {
	return this.department;
    }

    public void setDepartment(String department) {
	this.department = department;
    }

    public Long getYear() {
	return this.year;
    }

    public void setYear(Long year) {
	this.year = year;
    }

    public Long getMonth() {
	return this.month;
    }

    public void setMonth(Long month) {
	this.month = month;
    }

    public String getStatus() {
	return this.status;
    }

    public void setStatus(String status) {
	this.status = status;
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

    public List<BuGoalDeployLine> getBuGoalDeployLines() {
	return buGoalDeployLines;
    }

    public void setBuGoalDeployLines(List<BuGoalDeployLine> buGoalDeployLines) {
	this.buGoalDeployLines = buGoalDeployLines;
    }

    public Double getOriginalGoal() {
	return originalGoal;
    }

    public void setOriginalGoal(Double originalGoal) {
	this.originalGoal = originalGoal;
    }

    public Double getActualGoal() {
	return actualGoal;
    }

    public void setActualGoal(Double actualGoal) {
	this.actualGoal = actualGoal;
    }

    public Double getLastYearGoal() {
	return lastYearGoal;
    }

    public void setLastYearGoal(Double lastYearGoal) {
	this.lastYearGoal = lastYearGoal;
    }

    public Double getLastMonthGoal() {
	return lastMonthGoal;
    }

    public void setLastMonthGoal(Double lastMonthGoal) {
	this.lastMonthGoal = lastMonthGoal;
    }

    public String getFlowStatus() {
	return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
	this.flowStatus = flowStatus;
    }

    public String getStatusName() {
	return statusName;
    }

    public void setStatusName(String statusName) {
	this.statusName = statusName;
    }

    public String getOrderNo() {
	return orderNo;
    }

    public void setOrderNo(String orderNo) {
	this.orderNo = orderNo;
    }

    public String getOrderTypeCode() {
	return orderTypeCode;
    }

    public void setOrderTypeCode(String orderTypeCode) {
	this.orderTypeCode = orderTypeCode;
    }

}