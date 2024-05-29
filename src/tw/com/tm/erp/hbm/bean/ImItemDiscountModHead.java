package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImItemDiscountModHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemDiscountModHead implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1418913895813644903L;
	private Long headId;
	private String brandCode;
	private String brandName;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String vipTypeCode;
	private String itemDiscountType;
	private String status;
	private String statusName;		// 額外欄位 顯示狀態的中文
	private String orderTypeCode;
	private String orderNo;
	
  private List<ImItemDiscountModLine> imItemDiscountModLines = new ArrayList(0);

	// Constructors

	/** default constructor */
	public ImItemDiscountModHead() {
	}

	/** minimal constructor */
	public ImItemDiscountModHead(Long headId) {
		this.headId = headId;
	}

	/** full constructor */
	public ImItemDiscountModHead(Long headId, String brandCode,
			String reserve1, String reserve2, String reserve3, String reserve4,
			String reserve5, String createdBy, Date creationDate,
			String lastUpdateBy, Date lastUpdateDate, String vipTypeCode,
			String itemDiscountType, String status, String orderTypeCode,
			String orderNo) {
		this.headId = headId;
		this.brandCode = brandCode;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdateBy;
		this.lastUpdateDate = lastUpdateDate;
		this.vipTypeCode = vipTypeCode;
		this.itemDiscountType = itemDiscountType;
		this.status = status;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		
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

	public String getVipTypeCode() {
		return this.vipTypeCode;
	}

	public void setVipTypeCode(String vipTypeCode) {
		this.vipTypeCode = vipTypeCode;
	}

	public String getItemDiscountType() {
		return this.itemDiscountType;
	}

	public void setItemDiscountType(String itemDiscountType) {
		this.itemDiscountType = itemDiscountType;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderTypeCode() {
		return this.orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public List<ImItemDiscountModLine> getImItemDiscountModLines() {
		return imItemDiscountModLines;
	}

	public void setImItemDiscountModLines(
			List<ImItemDiscountModLine> imItemDiscountModLines) {
		this.imItemDiscountModLines = imItemDiscountModLines;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}


	

}