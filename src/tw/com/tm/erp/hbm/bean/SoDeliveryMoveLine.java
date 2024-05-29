package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoDeliveryMoveLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoDeliveryMoveLine implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2246397439608474246L;
	
	private Long lineId;
	private SoDeliveryMoveHead soDeliveryMoveHead;
	private String deliveryOrderType;
	private String deliveryOrderNo;
	private Long bagCounts1;
	private Long bagCounts2;
	private Long bagCounts3;
	private Long bagCounts4;
	private Long bagCounts5;
	private Long bagCounts6;
	private Long bagCounts7;
	private String isLockRecord;
	private String isDeleteRecord;
	private Long indexNo;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String returnMessage;
	private String deliveryStoreArea;
	private String arrivalStoreArea;
	private String deliveryStoreCode;
	private String arrivalStoreCode;
	// Constructors

	/** default constructor */
	public SoDeliveryMoveLine() {
	}

	/** full constructor */
	public SoDeliveryMoveLine(SoDeliveryMoveHead soDeliveryMoveHead,
			String deliveryOrderType, String deliveryOrderNo, Long bagCounts1,
			Long bagCounts2, Long bagCounts3, Long bagCounts4, Long bagCounts5,
			Long bagCounts6, Long bagCounts7, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, String deliveryStoreArea, String deliveryStoreCode, 
			String arrivalStoreArea, String arrivalStoreCode) {
		this.soDeliveryMoveHead = soDeliveryMoveHead;
		this.deliveryOrderType = deliveryOrderType;
		this.deliveryOrderNo = deliveryOrderNo;
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
		this.deliveryStoreArea = deliveryStoreArea;
		this.deliveryStoreCode = deliveryStoreCode;
		this.arrivalStoreArea = arrivalStoreArea;
		this.arrivalStoreCode = arrivalStoreCode;
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public SoDeliveryMoveHead getSoDeliveryMoveHead() {
		return this.soDeliveryMoveHead;
	}

	public void setSoDeliveryMoveHead(SoDeliveryMoveHead soDeliveryMoveHead) {
		this.soDeliveryMoveHead = soDeliveryMoveHead;
	}

	public String getDeliveryOrderType() {
		return this.deliveryOrderType;
	}

	public void setDeliveryOrderType(String deliveryOrderType) {
		this.deliveryOrderType = deliveryOrderType;
	}

	public String getDeliveryOrderNo() {
		return this.deliveryOrderNo;
	}

	public void setDeliveryOrderNo(String deliveryOrderNo) {
		this.deliveryOrderNo = deliveryOrderNo;
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

	public String getIsLockRecord() {
		return isLockRecord;
	}

	public void setIsLockRecord(String isLockRecord) {
		this.isLockRecord = isLockRecord;
	}

	public String getIsDeleteRecord() {
		return isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	
	public void setDeliveryStoreArea(String deliveryStoreArea){
		this.deliveryStoreArea = deliveryStoreArea;
	}
	
	public String getDeliveryStoreArea(){
		return deliveryStoreArea;
	}
	
	public void setArrivalStoreArea(String arrivalStoreArea){
		this.arrivalStoreArea = arrivalStoreArea;
	}
	
	public String getArrivalStoreArea(){
		return arrivalStoreArea;
	}
	
	public void setDeliveryStoreCode(String deliveryStoreCode){
		this.deliveryStoreCode = deliveryStoreCode;
	}
	
	public String getDeliveryStoreCode(){
		return deliveryStoreCode;
	}
	
	public void setArrivalStoreCode(String arrivalStoreCode){
		this.arrivalStoreCode = arrivalStoreCode;
	}
	
	public String getArrivalStoreCode(){
		return arrivalStoreCode;
	}
}