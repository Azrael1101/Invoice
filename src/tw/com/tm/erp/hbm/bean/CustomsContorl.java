package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImMovementHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CustomsContorl implements java.io.Serializable {

	private static final long serialVersionUID = 7110936185841803525L;
	// Fields

	private Long headId;
	private Date allowDate;
	private String lotNumber;
	private String lotTime;
	private String orderTypeCode;
	private String status;
	private String type;
	private Long indexNo;
	private String name;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String waitToSend;
	private String sended;
	private String category01;
	private String customsWarehouseCode;
	private String uploadType;
	
	// Constructors

	public String getWaitToSend() {
		return waitToSend;
	}

	public void setWaitToSend(String waitToSend) {
		this.waitToSend = waitToSend;
	}

	public String getSended() {
		return sended;
	}

	public void setSended(String sended) {
		this.sended = sended;
	}

	/** default constructor */
	public CustomsContorl() {
	}

	/** minimal constructor */
	public CustomsContorl(Long headId) {
		this.headId = headId;
	}

	public CustomsContorl(Long headId, Date allowDate,String lotNumber, String lotTime,String orderTypeCode,String status,String type,Long indexNo
			,String name,String lastUpdatedBy,Date lastUpdateDate) {
		super();
		this.headId = headId;
		this.allowDate = allowDate;
		this.lotNumber = lotNumber;
		this.lotTime = lotTime;
		this.orderTypeCode = orderTypeCode;
		this.status = status;
		this.type = type;
		this.indexNo = indexNo;
		this.name = name;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	/** full constructor */

	// Property accessors
	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public Date getAllowDate() {
		return allowDate;
	}

	public void setAllowDate(Date allowDate) {
		this.allowDate = allowDate;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getLotTime() {
		return lotTime;
	}

	public void setLotTime(String lotTime) {
		this.lotTime = lotTime;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getCategory01() {
		return category01;
	}

	public void setCategory01(String category01) {
		this.category01 = category01;
	}

	public String getCustomsWarehouseCode() {
		return customsWarehouseCode;
	}

	public void setCustomsWarehouseCode(String customsWarehouseCode) {
		this.customsWarehouseCode = customsWarehouseCode;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}
}