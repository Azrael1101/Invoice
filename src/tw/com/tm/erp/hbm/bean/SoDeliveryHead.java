package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SoDeliveryHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoDeliveryHead implements java.io.Serializable {

	// Fields
	/**
	 * 
	 */
	private static final long serialVersionUID = -6608280311803767548L;
	private Long headId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private String customerCode;
	private String customerName;
	private String isPassportBlackList;
	private String isTelBlackList;
	private String passportNo;
	private String contactInfo;
	private Date orderDate;
	private Date scheduleDeliveryDate;
	private String shopCode;
	private String shopName;
	private String itemCategory;
	private String deliveryTerminal;
	private String deliveryArea;
	private String flightNo;
	private Date flightDate;
	private String flightArea;
	private Long bagCounts1;
	private Long bagCounts2;
	private Long bagCounts3;
	private Long bagCounts4;
	private Long bagCounts5;
	private Long bagCounts6;
	private Long bagCounts7;
	private Long totalBagCounts;
	private String breakable;
	private String valuable;
	private String bagBarCode;
	private Date deliveryDate;
	private String storeArea;
	private String storeAreaName;
	private String status;
	private String remark1;
	private String remark2;
	private String SFaultReason;
	private String DFaultReason;
	private String SFaultReasonStr;
	private String DFaultReasonStr;
	private String lockFlag;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private Date creationDate;
	private String createdBy;
	private Date lastUpdateDate;
	private String lastUpdatedBy;
	private String deliveredBy;
	private String madeBy;
	private Date makeDate;
	private String affidavit;
	private Date expiryDate;
	private String customerPoNoString;
	private String transactionSeqNoString;
	private String expiryReturnNo;
	private String expiryReturnMemo;
	private String storageCode;
	//STEVE
	private String soDelUpdateHeadCode;
	private String oriFlight;
	private Date oriDate;
	private String updateType;
	private String updateContent;
	private String contactInfo_cs;
	private String cs_Note;
	private String contactOverTime;

	private String cStatus;
	private String customsStatus;
	private String tranAllowUpload;
	private String tranRecordStatus;
	private String schedule;
	//STEVE
	private List<SoDeliveryLine> soDeliveryLines = new ArrayList(0);
	private List<SoDeliveryLog> soDeliveryLogs = new ArrayList(0);
	// Constructors

	/** default constructor */
	public SoDeliveryHead() {
	}

	/** full constructor */
	public SoDeliveryHead(String brandCode, String orderTypeCode,
			String orderNo, String customerCode, String customerName,
			String passportNo, String contactInfo, Date applicationDate,
			Date scheduleDeliveryDate, String shopCode,
			String deliveryTerminal, String deliveryArea, String flightNo,
			Long bagCounts1, Long bagCounts2, Long bagCounts3, Long bagCounts4,
			Long bagCounts5, Long bagCounts6, String bagBarCode,
			Date deliveryDate, String storeArea, String status, String remark1,
			String remark2, String SFaultReason, String DFaultReason,
			String lockFlag, String reserve1, String reserve2, String reserve3,
			String reserve4, String reserve5, Date creationDate,
			String createdBy, Date lastUpdateDate, String lastUpdatedBy,
			String deliveredBy, List<SoDeliveryLine> soDeliveryLines, List<SoDeliveryLog> soDeliveryLogs, String storageCode,
			String soDelUpdateHeadCode,String oriFlight,Date oriDate,String updateType,String updateContent,String contactInfo_cs,String cs_Note,
			String contactOverTime) {
		this.brandCode = brandCode;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.customerCode = customerCode;
		this.customerName = customerName;
		this.passportNo = passportNo;
		this.contactInfo = contactInfo;
		this.orderDate = applicationDate;
		this.scheduleDeliveryDate = scheduleDeliveryDate;
		this.shopCode = shopCode;
		this.deliveryTerminal = deliveryTerminal;
		this.deliveryArea = deliveryArea;
		this.flightNo = flightNo;
		this.bagCounts1 = bagCounts1;
		this.bagCounts2 = bagCounts2;
		this.bagCounts3 = bagCounts3;
		this.bagCounts4 = bagCounts4;
		this.bagCounts5 = bagCounts5;
		this.bagCounts6 = bagCounts6;
		this.bagBarCode = bagBarCode;
		this.deliveryDate = deliveryDate;
		this.storeArea = storeArea;
		this.status = status;
		this.remark1 = remark1;
		this.remark2 = remark2;
		this.SFaultReason = SFaultReason;
		this.DFaultReason = DFaultReason;
		this.lockFlag = lockFlag;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.deliveredBy = deliveredBy;
		this.soDeliveryLines = soDeliveryLines;
		this.soDeliveryLogs = soDeliveryLogs;
		this.storageCode = storageCode;
		//steve
		this.soDelUpdateHeadCode = soDelUpdateHeadCode;
		this.oriFlight = oriFlight;
		this.oriDate = oriDate;
		this.updateType = updateType;
		this.updateContent = updateContent;
		this.contactInfo_cs = contactInfo_cs;
		this.cs_Note = cs_Note;
		this.contactOverTime = contactOverTime;
		//steve
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

	public String getCustomerCode() {
		return this.customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPassportNo() {
		return this.passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getContactInfo() {
		return this.contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getScheduleDeliveryDate() {
		return this.scheduleDeliveryDate;
	}

	public void setScheduleDeliveryDate(Date scheduleDeliveryDate) {
		this.scheduleDeliveryDate = scheduleDeliveryDate;
	}

	public String getShopCode() {
		return this.shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getDeliveryTerminal() {
		return this.deliveryTerminal;
	}

	public void setDeliveryTerminal(String deliveryTerminal) {
		this.deliveryTerminal = deliveryTerminal;
	}

	public String getDeliveryArea() {
		return this.deliveryArea;
	}

	public void setDeliveryArea(String deliveryArea) {
		this.deliveryArea = deliveryArea;
	}

	public String getFlightNo() {
		return this.flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
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

	public String getBagBarCode() {
		return this.bagBarCode;
	}

	public void setBagBarCode(String bagBarCode) {
		this.bagBarCode = bagBarCode;
	}

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getStoreArea() {
		return this.storeArea;
	}

	public void setStoreArea(String storeArea) {
		this.storeArea = storeArea;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getSFaultReason() {
		return this.SFaultReason;
	}

	public void setSFaultReason(String SFaultReason) {
		this.SFaultReason = SFaultReason;
	}

	public String getDFaultReason() {
		return this.DFaultReason;
	}

	public void setDFaultReason(String DFaultReason) {
		this.DFaultReason = DFaultReason;
	}

	public String getLockFlag() {
		return this.lockFlag;
	}

	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
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

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getDeliveredBy() {
		return this.deliveredBy;
	}

	public void setDeliveredBy(String deliveredBy) {
		this.deliveredBy = deliveredBy;
	}

	public List<SoDeliveryLine> getSoDeliveryLines() {
		return this.soDeliveryLines;
	}

	public void setSoDeliveryLines(List<SoDeliveryLine> soDeliveryLines) {
		this.soDeliveryLines = soDeliveryLines;
	}

	public List<SoDeliveryLog> getSoDeliveryLogs() {
		return this.soDeliveryLogs;
	}

	public void setSoDeliveryLogs(List<SoDeliveryLog> soDeliveryLogs) {
		this.soDeliveryLogs = soDeliveryLogs;
	}

	public Date getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(Date flightDate) {
		this.flightDate = flightDate;
	}

	public Long getBagCounts7() {
		return bagCounts7;
	}

	public void setBagCounts7(Long bagCounts7) {
		this.bagCounts7 = bagCounts7;
	}

	public String getBreakable() {
		return breakable;
	}

	public void setBreakable(String breakable) {
		this.breakable = breakable;
	}

	public String getValuable() {
		return valuable;
	}

	public void setValuable(String valuable) {
		this.valuable = valuable;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getFlightArea() {
		return flightArea;
	}

	public void setFlightArea(String flightArea) {
		this.flightArea = flightArea;
	}

	public Long getTotalBagCounts() {
		return totalBagCounts;
	}

	public void setTotalBagCounts(Long totalBagCounts) {
		this.totalBagCounts = totalBagCounts;
	}

	public String getCustomerPoNoString() {
		return customerPoNoString;
	}

	public void setCustomerPoNoString(String customerPoNoString) {
		this.customerPoNoString = customerPoNoString;
	}

	public String getTransactionSeqNoString() {
		return transactionSeqNoString;
	}

	public void setTransactionSeqNoString(String transactionSeqNoString) {
		this.transactionSeqNoString = transactionSeqNoString;
	}

	public String getMadeBy() {
		return madeBy;
	}

	public void setMadeBy(String madeBy) {
		this.madeBy = madeBy;
	}

	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}

	public String getStoreAreaName() {
		return storeAreaName;
	}

	public void setStoreAreaName(String storeAreaName) {
		this.storeAreaName = storeAreaName;
	}

	public String getIsPassportBlackList() {
		return isPassportBlackList;
	}

	public void setIsPassportBlackList(String isPassportBlackList) {
		this.isPassportBlackList = isPassportBlackList;
	}

	public String getIsTelBlackList() {
		return isTelBlackList;
	}

	public void setIsTelBlackList(String isTelBlackList) {
		this.isTelBlackList = isTelBlackList;
	}

	public String getSFaultReasonStr() {
		return SFaultReasonStr;
	}

	public void setSFaultReasonStr(String faultReasonStr) {
		SFaultReasonStr = faultReasonStr;
	}

	public String getDFaultReasonStr() {
		return DFaultReasonStr;
	}

	public void setDFaultReasonStr(String faultReasonStr) {
		DFaultReasonStr = faultReasonStr;
	}

	public String getAffidavit() {
		return affidavit;
	}

	public void setAffidavit(String affidavit) {
		this.affidavit = affidavit;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getExpiryReturnNo() {
		return expiryReturnNo;
	}

	public void setExpiryReturnNo(String expiryReturnNo) {
		this.expiryReturnNo = expiryReturnNo;
	}

	public String getExpiryReturnMemo() {
		return expiryReturnMemo;
	}

	public void setExpiryReturnMemo(String expiryReturnMemo) {
		this.expiryReturnMemo = expiryReturnMemo;
	}
	
	public void setStorageCode(String storageCode){
		this.storageCode = storageCode;
	}

	public String getStorageCode(){
		return storageCode;
	}

	public String getsoDelUpdateHeadCode() {
		return soDelUpdateHeadCode;
	}

	public void setsoDelUpdateHeadCode(String soDelUpdateHeadCode) {
		this.soDelUpdateHeadCode = soDelUpdateHeadCode;
	}

	public String getOriFlight() {
		return oriFlight;
	}

	public void setOriFlight(String oriFlight) {
		this.oriFlight = oriFlight;
	}

	public Date getOriDate() {
		return oriDate;
	}

	public void setOriDate(Date oriDate) {
		this.oriDate = oriDate;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public String getUpdateContent() {
		return updateContent;
	}

	public void setUpdateContent(String updateContent) {
		this.updateContent = updateContent;
	}

	public String getContactInfo_cs() {
		return contactInfo_cs;
	}

	public void setContactInfo_cs(String contactInfo_cs) {
		this.contactInfo_cs = contactInfo_cs;
	}
	
	

	public String getCs_Note() {
		return cs_Note;
	}

	public void setCs_Note(String cs_Note) {
		this.cs_Note = cs_Note;
	}

	public String getContactOverTime() {
		return contactOverTime;
	}

	public void setContactOverTime(String contactOverTime) {
		this.contactOverTime = contactOverTime;
	}


	

	public String getSoDelUpdateHeadCode() {
		return soDelUpdateHeadCode;
	}

	public void setSoDelUpdateHeadCode(String soDelUpdateHeadCode) {
		this.soDelUpdateHeadCode = soDelUpdateHeadCode;
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


	public String getcStatus() {
		return cStatus;
	}

	public void setcStatus(String status) {
		cStatus = status;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

}