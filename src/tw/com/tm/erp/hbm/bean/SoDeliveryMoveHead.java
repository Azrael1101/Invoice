package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SoDeliveryMoveHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoDeliveryMoveHead implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8091437895458505369L;
	
	private Long headId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private Date orderDate;
	private String deliveryStoreArea;
	private String arrivalStoreArea;
	private String deliveryStoreName;
	private String arrivalStoreName;
	private String moveEmployee;
	private String moveEmployeeName;
	private Long bagCounts1;
	private Long bagCounts2;
	private Long bagCounts3;
	private Long bagCounts4;
	private Long bagCounts5;
	private Long bagCounts6;
	private Long bagCounts7;
	private Long totalBagCounts;
	private String status;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private List<SoDeliveryMoveLine> soDeliveryMoveLines = new ArrayList(0);
	private Long processId; // 流程控制用

	// Constructors

	/** default constructor */
	public SoDeliveryMoveHead() {
	}

	/** full constructor */
	public SoDeliveryMoveHead(String brandCode, String orderTypeCode,
			String orderNo, Date orderDate, String deliveryStoreArea,
			String arrivalStoreArea, Long bagCounts1, Long bagCounts2,
			Long bagCounts3, Long bagCounts4, Long bagCounts5, Long bagCounts6,
			Long bagCounts7, String reserve1, String reserve2, String reserve3,
			String reserve4, String reserve5, String createdBy,
			Date creationDate, String lastUpdatedBy, Date lastUpdateDate,
			List<SoDeliveryMoveLine> soDeliveryMoveLines) {
		this.brandCode = brandCode;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.orderDate = orderDate;
		this.deliveryStoreArea = deliveryStoreArea;
		this.arrivalStoreArea = arrivalStoreArea;
		this.bagCounts1 = bagCounts1;
		this.bagCounts2 = bagCounts2;
		this.bagCounts3 = bagCounts3;
		this.bagCounts4 = bagCounts4;
		this.bagCounts5 = bagCounts5;
		this.bagCounts6 = bagCounts6;
		this.bagCounts7 = bagCounts7;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.soDeliveryMoveLines = soDeliveryMoveLines;
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

	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getDeliveryStoreArea() {
		return this.deliveryStoreArea;
	}

	public void setDeliveryStoreArea(String deliveryStoreArea) {
		this.deliveryStoreArea = deliveryStoreArea;
	}

	public String getArrivalStoreArea() {
		return this.arrivalStoreArea;
	}

	public void setArrivalStoreArea(String arrivalStoreArea) {
		this.arrivalStoreArea = arrivalStoreArea;
	}

	public Long getBagCounts1() {
		return this.bagCounts1;
	}

	public void setBagCounts1(Long bagCounts1) {
		this.bagCounts1 = bagCounts1;
	}

	public Long getBagCounts2() {
		return this.bagCounts2;
	}

	public void setBagCounts2(Long bagCounts2) {
		this.bagCounts2 = bagCounts2;
	}

	public Long getBagCounts3() {
		return this.bagCounts3;
	}

	public void setBagCounts3(Long bagCounts3) {
		this.bagCounts3 = bagCounts3;
	}

	public Long getBagCounts4() {
		return this.bagCounts4;
	}

	public void setBagCounts4(Long bagCounts4) {
		this.bagCounts4 = bagCounts4;
	}

	public Long getBagCounts5() {
		return this.bagCounts5;
	}

	public void setBagCounts5(Long bagCounts5) {
		this.bagCounts5 = bagCounts5;
	}

	public Long getBagCounts6() {
		return this.bagCounts6;
	}

	public void setBagCounts6(Long bagCounts6) {
		this.bagCounts6 = bagCounts6;
	}

	public Long getBagCounts7() {
		return this.bagCounts7;
	}

	public void setBagCounts7(Long bagCounts7) {
		this.bagCounts7 = bagCounts7;
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

	public List<SoDeliveryMoveLine> getSoDeliveryMoveLines() {
		return this.soDeliveryMoveLines;
	}

	public void setSoDeliveryMoveLines(List<SoDeliveryMoveLine> soDeliveryMoveLines) {
		this.soDeliveryMoveLines = soDeliveryMoveLines;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTotalBagCounts() {
		return totalBagCounts;
	}

	public void setTotalBagCounts(Long totalBagCounts) {
		this.totalBagCounts = totalBagCounts;
	}

	public String getDeliveryStoreName() {
		return deliveryStoreName;
	}

	public void setDeliveryStoreName(String deliveryStoreName) {
		this.deliveryStoreName = deliveryStoreName;
	}

	public String getArrivalStoreName() {
		return arrivalStoreName;
	}

	public void setArrivalStoreName(String arrivalStoreName) {
		this.arrivalStoreName = arrivalStoreName;
	}

	public String getMoveEmployee() {
		return moveEmployee;
	}

	public void setMoveEmployee(String moveEmployee) {
		this.moveEmployee = moveEmployee;
	}

	public String getMoveEmployeeName() {
		return moveEmployeeName;
	}

	public void setMoveEmployeeName(String moveEmployeeName) {
		this.moveEmployeeName = moveEmployeeName;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

}