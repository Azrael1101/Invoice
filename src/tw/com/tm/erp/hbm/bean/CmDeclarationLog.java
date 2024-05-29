package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * CmDeclarationContainer entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CmDeclarationLog implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7416617429532282421L;
	private Long lineId;
	private Long identify;
	private String declType;
	private String declNo;
	private Long itemNo;
	private String modType;
	private String prdtNo;
	private String prdtNoMod;
	private String descrip;
	private String descripMod;
	private String brand;
	private String brandMod;
	private String model;
	private String modelMod;
	private String spec;
	private String specMod;
	private Double NWght;
	private String NWghtMod;
	private Double qty;
	private String qtyMod;
	private String unit;
	private String unitMod;
	private String ODeclNo;
	private String ODeclNoMod;
	private Long OItemNo;
	private String OItemNoMod;
	private String status;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
    private Long indexNo;
    private String isDeleteRecord = "0"; // 是否被刪除 1 表示要被移除
    private String isLockRecord = "0"; // 是否被鎖定 1 表示鎖定
    private String message; // line 訊息的顯示
    private String brandCode;
	private String orderTypeCode;
	private String orderNo ;
    
	private String sourceOrderNo;
	
	private String remark;
	private String statusType;
	
	private Double custValueAmt;
	// Constructors

	/** default constructor */
	public CmDeclarationLog() {
	}

	/** minimal constructor */
	public CmDeclarationLog(Long lineId) {
		this.lineId = lineId;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public Long getIdentify() {
		return identify;
	}

	public void setIdentify(Long identify) {
		this.identify = identify;
	}

	public String getDeclType() {
		return declType;
	}

	public void setDeclType(String declType) {
		this.declType = declType;
	}

	public String getDeclNo() {
		return declNo;
	}

	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}

	public Long getItemNo() {
		return itemNo;
	}

	public void setItemNo(Long itemNo) {
		this.itemNo = itemNo;
	}

	public String getModType() {
		return modType;
	}

	public void setModType(String modType) {
		this.modType = modType;
	}

	public String getPrdtNo() {
		return prdtNo;
	}

	public void setPrdtNo(String prdtNo) {
		this.prdtNo = prdtNo;
	}

	public String getPrdtNoMod() {
		return prdtNoMod;
	}

	public void setPrdtNoMod(String prdtNoMod) {
		this.prdtNoMod = prdtNoMod;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public String getDescripMod() {
		return descripMod;
	}

	public void setDescripMod(String descripMod) {
		this.descripMod = descripMod;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBrandMod() {
		return brandMod;
	}

	public void setBrandMod(String brandMod) {
		this.brandMod = brandMod;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelMod() {
		return modelMod;
	}

	public void setModelMod(String modelMod) {
		this.modelMod = modelMod;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getSpecMod() {
		return specMod;
	}

	public void setSpecMod(String specMod) {
		this.specMod = specMod;
	}

	public Double getNWght() {
		return NWght;
	}

	public void setNWght(Double wght) {
		NWght = wght;
	}

	public String getNWghtMod() {
		return NWghtMod;
	}

	public void setNWghtMod(String wghtMod) {
		NWghtMod = wghtMod;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public String getQtyMod() {
		return qtyMod;
	}

	public void setQtyMod(String qtyMod) {
		this.qtyMod = qtyMod;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnitMod() {
		return unitMod;
	}

	public void setUnitMod(String unitMod) {
		this.unitMod = unitMod;
	}

	public String getODeclNo() {
		return ODeclNo;
	}

	public void setODeclNo(String declNo) {
		ODeclNo = declNo;
	}

	public String getODeclNoMod() {
		return ODeclNoMod;
	}

	public void setODeclNoMod(String declNoMod) {
		ODeclNoMod = declNoMod;
	}

	public Long getOItemNo() {
		return this.OItemNo;
	}

	public void setOItemNo(Long OItemNo) {
		this.OItemNo = OItemNo;
	}

	public String getOItemNoMod() {
		return OItemNoMod;
	}

	public void setOItemNoMod(String itemNoMod) {
		OItemNoMod = itemNoMod;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getIsDeleteRecord() {
		return isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public String getIsLockRecord() {
		return isLockRecord;
	}

	public void setIsLockRecord(String isLockRecord) {
		this.isLockRecord = isLockRecord;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public String getSourceOrderNo() {
	    return sourceOrderNo;
	}

	public void setSourceOrderNo(String sourceOrderNo) {
	    this.sourceOrderNo = sourceOrderNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	public Double getCustValueAmt() {
		return custValueAmt;
	}

	public void setCustValueAmt(Double custValueAmt) {
		this.custValueAmt = custValueAmt;
	}
}