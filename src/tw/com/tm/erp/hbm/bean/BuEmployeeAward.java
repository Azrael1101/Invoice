package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuEmployeeAward entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuEmployeeAward implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5113629627520061357L;
	private String employeeCode;
	private Long indexNo;
//	private BuEmployeeAwardId id;
	private String type;
	private Date occurrenceDate;
	private Double point;
	private Double hour;
	private String memo;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String category01;
	private String category02;
	private String enable;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;

	
	// Constructors

	public Long getIndexNo() {
	    return indexNo;
	}

	public void setIndexNo(Long indexNo) {
	    this.indexNo = indexNo;
	}

	/** default constructor */
	public BuEmployeeAward() {
	}

	/** minimal constructor */
//	public BuEmployeeAward(BuEmployeeAwardId id) {
//		this.id = id;
//	}

	/** full constructor */
//	public BuEmployeeAward(BuEmployeeAwardId id, String type,
//			Date occurrenceDate, Double point, Double hour, String memo,
//			String reserve1, String reserve2, String reserve3, String reserve4,
//			String reserve5, String createdBy, Date creationDate,
//			String lastUpdatedBy, Date lastUpdateDate, String category01,
//			String category02) {
//		this.id = id;
//		this.type = type;
//		this.occurrenceDate = occurrenceDate;
//		this.point = point;
//		this.hour = hour;
//		this.memo = memo;
//		this.reserve1 = reserve1;
//		this.reserve2 = reserve2;
//		this.reserve3 = reserve3;
//		this.reserve4 = reserve4;
//		this.reserve5 = reserve5;
//		this.createdBy = createdBy;
//		this.creationDate = creationDate;
//		this.lastUpdatedBy = lastUpdatedBy;
//		this.lastUpdateDate = lastUpdateDate;
//		this.category01 = category01;
//		this.category02 = category02;
//	}

	// Property accessors

//	public BuEmployeeAwardId getId() {
//		return this.id;
//	}
//
//	public void setId(BuEmployeeAwardId id) {
//		this.id = id;
//	}
	public String getEmployeeCode() {
	    return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
	    this.employeeCode = employeeCode;
	}
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getOccurrenceDate() {
		return this.occurrenceDate;
	}

	public void setOccurrenceDate(Date occurrenceDate) {
		this.occurrenceDate = occurrenceDate;
	}

	public Double getPoint() {
		return this.point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public Double getHour() {
		return this.hour;
	}

	public void setHour(Double hour) {
		this.hour = hour;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public String getCategory01() {
		return this.category01;
	}

	public void setCategory01(String category01) {
		this.category01 = category01;
	}

	public String getCategory02() {
		return this.category02;
	}

	public void setCategory02(String category02) {
		this.category02 = category02;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
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

}