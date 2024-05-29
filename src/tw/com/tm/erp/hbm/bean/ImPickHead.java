package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImStorageHeadId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImPickHead implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4631432349273021627L;
	private Long headId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private Date pickDate;
	private String warehouseCode;
	private String status;
	private String description;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long processId;
	private String arrivalWarehouseCode1;
	private String arrivalWarehouseCode2;
	private String arrivalWarehouseCode3;
	private String arrivalWarehouseCode4;
	private String arrivalWarehouseCode5;
	private String arrivalWarehouseCode6;
	private String arrivalWarehouseCode7;
	private String arrivalWarehouseCode8;
	private String arrivalWarehouseCode9;
	private String arrivalWarehouseCode10;
	private List<ImPickItem> imPickItems = new ArrayList();
	private String createdByName;

	// Constructors

	/** default constructor */
	public ImPickHead() {
	}


	// Property accessors

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

	public Date getPickDate() {
		return pickDate;
	}

	public void setPickDate(Date pickDate) {
		this.pickDate = pickDate;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	
	public String getArrivalWarehouseCode1() {
		return arrivalWarehouseCode1;
	}

	public void setArrivalWarehouseCode1(String arrivalWarehouseCode1) {
		this.arrivalWarehouseCode1 = arrivalWarehouseCode1;
	}

	public String getArrivalWarehouseCode2() {
		return arrivalWarehouseCode2;
	}

	public void setArrivalWarehouseCode2(String arrivalWarehouseCode2) {
		this.arrivalWarehouseCode2 = arrivalWarehouseCode2;
	}

	public String getArrivalWarehouseCode3() {
		return arrivalWarehouseCode3;
	}

	public void setArrivalWarehouseCode3(String arrivalWarehouseCode3) {
		this.arrivalWarehouseCode3 = arrivalWarehouseCode3;
	}

	public String getArrivalWarehouseCode4() {
		return arrivalWarehouseCode4;
	}

	public void setArrivalWarehouseCode4(String arrivalWarehouseCode4) {
		this.arrivalWarehouseCode4 = arrivalWarehouseCode4;
	}

	public String getArrivalWarehouseCode5() {
		return arrivalWarehouseCode5;
	}

	public void setArrivalWarehouseCode5(String arrivalWarehouseCode5) {
		this.arrivalWarehouseCode5 = arrivalWarehouseCode5;
	}

	public String getArrivalWarehouseCode6() {
		return arrivalWarehouseCode6;
	}

	public void setArrivalWarehouseCode6(String arrivalWarehouseCode6) {
		this.arrivalWarehouseCode6 = arrivalWarehouseCode6;
	}

	public String getArrivalWarehouseCode7() {
		return arrivalWarehouseCode7;
	}

	public void setArrivalWarehouseCode7(String arrivalWarehouseCode7) {
		this.arrivalWarehouseCode7 = arrivalWarehouseCode7;
	}

	public String getArrivalWarehouseCode8() {
		return arrivalWarehouseCode8;
	}

	public void setArrivalWarehouseCode8(String arrivalWarehouseCode8) {
		this.arrivalWarehouseCode8 = arrivalWarehouseCode8;
	}

	public String getArrivalWarehouseCode9() {
		return arrivalWarehouseCode9;
	}

	public void setArrivalWarehouseCode9(String arrivalWarehouseCode9) {
		this.arrivalWarehouseCode9 = arrivalWarehouseCode9;
	}

	public String getArrivalWarehouseCode10() {
		return arrivalWarehouseCode10;
	}

	public void setArrivalWarehouseCode10(String arrivalWarehouseCode10) {
		this.arrivalWarehouseCode10 = arrivalWarehouseCode10;
	}

	public List<ImPickItem> getImPickItems() {
		return imPickItems;
	}

	public void setImPickItems(List<ImPickItem> imPickItems) {
		this.imPickItems = imPickItems;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

}