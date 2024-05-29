package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BuGoalHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuGoalHead implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 132854446881310510L;
	private Long headId;
	private String brandCode;
	private String shopCode;
	private String department;
	private String reserve1;		// 用作判定是否為可用定義檔 FINISH = 表示可用, WAITING = 表示還在定義中 
	private String reserve2;		// 用作判定是否為流程內 Y 表示流程內 N 表示未在流程內
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String status;
	
	private List<BuGoalPercentLine> buGoalPercentLines = new ArrayList();
	private List<BuGoalEmployeeLine> buGoalEmployeeLines = new ArrayList();
	
	private String orderTypeCode;	//  非DB欄位  簽核用
	private String orderNo;			//  非DB欄位  簽核用
	private String statusName;		//  非DB欄位  查詢用 狀態名稱
	
	// Constructors

	/** default constructor */
	public BuGoalHead() {
	}

	/** minimal constructor */
	public BuGoalHead(Long headId) {
		this.headId = headId;
	}

	/** full constructor */
	public BuGoalHead(Long headId, String brandCode, String shopCode,
			String department, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, List<BuGoalPercentLine> buGoalPercentLines,
			List<BuGoalEmployeeLine> buGoalEmployeeLines) {
		super();
		this.headId = headId;
		this.brandCode = brandCode;
		this.shopCode = shopCode;
		this.department = department;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.buGoalPercentLines = buGoalPercentLines;
		this.buGoalEmployeeLines = buGoalEmployeeLines;
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

	public List<BuGoalPercentLine> getBuGoalPercentLines() {
		return buGoalPercentLines;
	}

	public void setBuGoalPercentLines(List<BuGoalPercentLine> buGoalPercentLines) {
		this.buGoalPercentLines = buGoalPercentLines;
	}

	public List<BuGoalEmployeeLine> getBuGoalEmployeeLines() {
		return buGoalEmployeeLines;
	}

	public void setBuGoalEmployeeLines(List<BuGoalEmployeeLine> buGoalEmployeeLines) {
		this.buGoalEmployeeLines = buGoalEmployeeLines;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}