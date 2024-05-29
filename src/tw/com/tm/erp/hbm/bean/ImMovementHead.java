package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImMovementHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */  

public class ImMovementHead implements java.io.Serializable {

	private static final long serialVersionUID = 7110936185841803525L;
	// Fields

	private Long headId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private Date deliveryDate;
	private String deliveryWarehouseCode;
	private String deliveryWarehouseName;
	private String deliveryContactPerson;
	private String deliveryContactPersonName;
	private String deliveryCity;
	private String deliveryArea;
	private String deliveryZipCode;
	private String deliveryAddress;
	private Date arrivalDate;
	private String arrivalWarehouseCode;
	private String arrivalWarehouseName;
	private String arrivalContactPerson;
	private String arrivalContactPersonName;
	private String arrivalCity;
	private String arrivalArea;
	private String arrivalZipCode;
	private String arrivalAddress;
	private String originalOrderTypeCode;
	private String originalOrderNo;
	private String status;
	private String transport;
	private String remark1;
	private String remark2;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String taxType;
	private String packedBy;
	private String comfirmedBy;
	private String receiptedBy;
	private String cmMovementNo;
	private Double boxCount;
	private Double itemCount;
	private String itemCategory;
	private String customsDeliveryWarehouseCode;
	private String customsArrivalWarehouseCode;
	private String arrivalStoreCode; // 轉入店別
	private String customsArrivalStoreCode; // 轉入店別倉庫
	private List imMovementItems = new ArrayList(0);
	private List imMovementFiles = new ArrayList(0);
	private Long processId; // 流程控制用
	/*Steve 覆核*/
	private String whComfirmedBy; // 覆合人員-倉儲
	private Date whComfirmedDate; // 覆合日期-倉儲
	private Double whComfirmedTimes; // 覆合次數-倉儲
	private String whImportText; // 覆合匯入批號-倉儲
	/*Steve 覆核*/
    /*海關新增*/
	private String sendRpNo;
	private String bccCode;
	private String cStatus;
	/*海關新增*/
	private String isUsed;
	private String stockStatus;
	private String customsStatus;
	// Constructors
	private String tranAllowUpload;
	private String tranRecordStatus;
	private String tranPhrase;
	private String schedule;
	private String rpSupplier;

	
	public String getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	/** default constructor */
	public ImMovementHead() {
	}

	/** minimal constructor */
	public ImMovementHead(Long headId) {
		this.headId = headId;
	}

	public ImMovementHead(Long headId, String brandCode, String orderTypeCode, String orderNo, Date deliveryDate,
			String deliveryWarehouseCode, String deliveryWarehouseName, String deliveryContactPerson,
			String deliveryContactPersonName, String deliveryCity, String deliveryArea, String deliveryZipCode,
			String deliveryAddress, Date arrivalDate, String arrivalWarehouseCode, String arrivalWarehouseName,
			String arrivalContactPerson, String arrivalContactPersonName, String arrivalCity, String arrivalArea,
			String arrivalZipCode, String arrivalAddress, String originalOrderTypeCode, String originalOrderNo,
			String status, String transport, String remark1, String remark2, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5, String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, String taxType, String packedBy, String comfirmedBy, String cmMovementNo,
			List imMovementItems, List imMovementFiles) {
		super();
		this.headId = headId;
		this.brandCode = brandCode;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.deliveryDate = deliveryDate;
		this.deliveryWarehouseCode = deliveryWarehouseCode;
		this.deliveryWarehouseName = deliveryWarehouseName;
		this.deliveryContactPerson = deliveryContactPerson;
		this.deliveryContactPersonName = deliveryContactPersonName;
		this.deliveryCity = deliveryCity;
		this.deliveryArea = deliveryArea;
		this.deliveryZipCode = deliveryZipCode;
		this.deliveryAddress = deliveryAddress;
		this.arrivalDate = arrivalDate;
		this.arrivalWarehouseCode = arrivalWarehouseCode;
		this.arrivalWarehouseName = arrivalWarehouseName;
		this.arrivalContactPerson = arrivalContactPerson;
		this.arrivalContactPersonName = arrivalContactPersonName;
		this.arrivalCity = arrivalCity;
		this.arrivalArea = arrivalArea;
		this.arrivalZipCode = arrivalZipCode;
		this.arrivalAddress = arrivalAddress;
		this.originalOrderTypeCode = originalOrderTypeCode;
		this.originalOrderNo = originalOrderNo;
		this.status = status;
		this.transport = transport;
		this.remark1 = remark1;
		this.remark2 = remark2;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.taxType = taxType;
		this.packedBy = packedBy;
		this.comfirmedBy = comfirmedBy;
		this.cmMovementNo = cmMovementNo;
		this.imMovementItems = imMovementItems;
		this.imMovementFiles = imMovementFiles;
	}

	/** full constructor */

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

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryWarehouseCode() {
		return this.deliveryWarehouseCode;
	}

	public String getDeliveryWarehouseName() {
		return deliveryWarehouseName;
	}

	public void setDeliveryWarehouseName(String deliveryWarehouseName) {
		this.deliveryWarehouseName = deliveryWarehouseName;
	}

	public void setDeliveryWarehouseCode(String deliveryWarehouseCode) {
		this.deliveryWarehouseCode = deliveryWarehouseCode;
	}

	public String getDeliveryContactPerson() {
		return this.deliveryContactPerson;
	}

	public void setDeliveryContactPerson(String deliveryContactPerson) {
		this.deliveryContactPerson = deliveryContactPerson;
	}

	public String getDeliveryContactPersonName() {
		return deliveryContactPersonName;
	}

	public void setDeliveryContactPersonName(String deliveryContactPersonName) {
		this.deliveryContactPersonName = deliveryContactPersonName;
	}

	public String getDeliveryCity() {
		return this.deliveryCity;
	}

	public void setDeliveryCity(String deliveryCity) {
		this.deliveryCity = deliveryCity;
	}

	public String getDeliveryArea() {
		return this.deliveryArea;
	}

	public void setDeliveryArea(String deliveryArea) {
		this.deliveryArea = deliveryArea;
	}

	public String getDeliveryZipCode() {
		return this.deliveryZipCode;
	}

	public void setDeliveryZipCode(String deliveryZipCode) {
		this.deliveryZipCode = deliveryZipCode;
	}

	public String getDeliveryAddress() {
		return this.deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public Date getArrivalDate() {
		return this.arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getArrivalWarehouseCode() {
		return this.arrivalWarehouseCode;
	}

	public void setArrivalWarehouseCode(String arrivalWarehouseCode) {
		this.arrivalWarehouseCode = arrivalWarehouseCode;
	}

	public String getArrivalWarehouseName() {
		return arrivalWarehouseName;
	}

	public void setArrivalWarehouseName(String arrivalWarehouseName) {
		this.arrivalWarehouseName = arrivalWarehouseName;
	}

	public String getArrivalContactPerson() {
		return this.arrivalContactPerson;
	}

	public void setArrivalContactPerson(String arrivalContactPerson) {
		this.arrivalContactPerson = arrivalContactPerson;
	}

	public String getArrivalContactPersonName() {
		return arrivalContactPersonName;
	}

	public void setArrivalContactPersonName(String arrivalContactPersonName) {
		this.arrivalContactPersonName = arrivalContactPersonName;
	}

	public String getArrivalCity() {
		return this.arrivalCity;
	}

	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
	}

	public String getArrivalArea() {
		return this.arrivalArea;
	}

	public void setArrivalArea(String arrivalArea) {
		this.arrivalArea = arrivalArea;
	}

	public String getArrivalZipCode() {
		return this.arrivalZipCode;
	}

	public void setArrivalZipCode(String arrivalZipCode) {
		this.arrivalZipCode = arrivalZipCode;
	}

	public String getArrivalAddress() {
		return this.arrivalAddress;
	}

	public void setArrivalAddress(String arrivalAddress) {
		this.arrivalAddress = arrivalAddress;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setOriginalOrderTypeCode(String originalOrderTypeCode) {
		this.originalOrderTypeCode = originalOrderTypeCode;
	}

	public String getOriginalOrderTypeCode() {
		return originalOrderTypeCode;
	}

	public void setOriginalOrderNo(String originalOrderNo) {
		this.originalOrderNo = originalOrderNo;
	}

	public String getOriginalOrderNo() {
		return originalOrderNo;
	}

	public String getTransport() {
		return this.transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getRemark1() {
		return this.remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return this.remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
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

	public List getImMovementItems() {
		return this.imMovementItems;
	}

	public void setImMovementItems(List imMovementItems) {
		this.imMovementItems = imMovementItems;
	}

	public List getImMovementFiles() {
		return this.imMovementFiles;
	}

	public void setImMovementFiles(List imMovementFiles) {
		this.imMovementFiles = imMovementFiles;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getPackedBy() {
		return packedBy;
	}

	public void setPackedBy(String packedBy) {
		this.packedBy = packedBy;
	}

	public String getComfirmedBy() {
		return comfirmedBy;
	}

	public void setComfirmedBy(String comfirmedBy) {
		this.comfirmedBy = comfirmedBy;
	}

	public String getCmMovementNo() {
		return cmMovementNo;
	}

	public void setCmMovementNo(String cmMovementNo) {
		this.cmMovementNo = cmMovementNo;
	}

	public Double getBoxCount() {
		return boxCount;
	}

	public void setBoxCount(Double boxCount) {
		this.boxCount = boxCount;
	}

	public Double getItemCount() {
		return itemCount;
	}

	public void setItemCount(Double itemCount) {
		this.itemCount = itemCount;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getCustomsDeliveryWarehouseCode() {
		return customsDeliveryWarehouseCode;
	}

	public void setCustomsDeliveryWarehouseCode(String customsDeliveryWarehouseCode) {
		this.customsDeliveryWarehouseCode = customsDeliveryWarehouseCode;
	}

	public String getCustomsArrivalWarehouseCode() {
		return customsArrivalWarehouseCode;
	}

	public void setCustomsArrivalWarehouseCode(String customsArrivalWarehouseCode) {
		this.customsArrivalWarehouseCode = customsArrivalWarehouseCode;
	}

	public String getReceiptedBy() {
		return receiptedBy;
	}

	public void setReceiptedBy(String receiptedBy) {
		this.receiptedBy = receiptedBy;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getCustomsArrivalStoreCode() {
		return customsArrivalStoreCode;
	}

	public void setCustomsArrivalStoreCode(String customsArrivalStoreCode) {
		this.customsArrivalStoreCode = customsArrivalStoreCode;
	}

	public String getArrivalStoreCode() {
		return arrivalStoreCode;
	}

	public void setArrivalStoreCode(String arrivalStoreCode) {
		this.arrivalStoreCode = arrivalStoreCode;
	}

	public String getWhComfirmedBy() {
		return whComfirmedBy;
	}

	public void setWhComfirmedBy(String whComfirmedBy) {
		this.whComfirmedBy = whComfirmedBy;
	}

	public Date getWhComfirmedDate() {
		return whComfirmedDate;
	}

	public void setWhComfirmedDate(Date whComfirmedDate) {
		this.whComfirmedDate = whComfirmedDate;
	}

	public Double getWhComfirmedTimes() {
		return whComfirmedTimes;
	}

	public void setWhComfirmedTimes(Double whComfirmedTimes) {
		this.whComfirmedTimes = whComfirmedTimes;
	}

	public String getWhImportText() {
		return whImportText;
	}

	public void setWhImportText(String whImportText) {
		this.whImportText = whImportText;
	}

	public String getSendRpNo() {
		return sendRpNo;
	}

	public void setSendRpNo(String sendRpNo) {
		this.sendRpNo = sendRpNo;
	}

	public String getBccCode() {
		return bccCode;
	}

	public void setBccCode(String bccCode) {
		this.bccCode = bccCode;
	}

	public String getcStatus() {
		return cStatus;
	}

	public void setcStatus(String status) {
		cStatus = status;
	}

	public String getCustomsStatus() {
		return customsStatus;
	}

	public void setCustomsStatus(String customsStatus) {
		this.customsStatus = customsStatus;
	}

	public String getTranAllowUpload() {
		return tranAllowUpload;
	}

	public void setTranAllowUpload(String tranAllowUpload) {
		this.tranAllowUpload = tranAllowUpload;
	}

	public String getTranRecordStatus() {
		return tranRecordStatus;
	}

	public void setTranRecordStatus(String tranRecordStatus) {
		this.tranRecordStatus = tranRecordStatus;
	}

	public String getTranPhrase() {
		return tranPhrase;
	}

	public void setTranPhrase(String tranPhrase) {
		this.tranPhrase = tranPhrase;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getCStatus() {
		return cStatus;
	}

	public void setCStatus(String status) {
		cStatus = status;
	}

	public String getRpSupplier() {
		return rpSupplier;
	}

	public void setRpSupplier(String rpSupplier) {
		this.rpSupplier = rpSupplier;
	}

}