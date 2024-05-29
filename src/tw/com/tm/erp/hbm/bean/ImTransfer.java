package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImTransferId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImTransfer implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4354802884083868604L;
	// Fields
	private Long lineId;
	private Long headId;
	private String warehouseCodeApply;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private String itemCode;
	private String reasonApply;
	private String remark1;
	private String applyBy;
	private Date applyDate;
	private String processBy;
	private Date processDate;	
	
	private Double quantityApply;
	private Double warehouseCodeProcess1;
	private Double quantityProcess1;
	private Double warehouseCodeProcess2;
	private Double quantityProcess2;
	private Double warehouseCodeProcess3;
	private Double quantityProcess3;	
	
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String status;
	private String createdBy;
	private Date creationDate;
	private String lastUpdateBy;
	private Date lastUpdateDate;
	private Long indexNo;
	private String isDeleteRecord;
	private String isLockRecord;
	private String message;
	private String itemName;
	
	private Double stockSalesqty;
	private Double stockOnHandqty;	
	private Double unCommitqty;
	private Double warehouseCodeProcess99;
	private Double warehouseCodeProcessfg;
		
	public ImTransfer() {
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getWarehouseCodeApply() {
		return warehouseCodeApply;
	}

	public void setWarehouseCodeApply(String warehouseCodeApply) {
		this.warehouseCodeApply = warehouseCodeApply;
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

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getReasonApply() {
		return reasonApply;
	}

	public void setReasonApply(String reasonApply) {
		this.reasonApply = reasonApply;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getApplyBy() {
		return applyBy;
	}

	public void setApplyBy(String applyBy) {
		this.applyBy = applyBy;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getProcessBy() {
		return processBy;
	}

	public void setProcessBy(String processBy) {
		this.processBy = processBy;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	

	public Double getQuantityApply() {
		return quantityApply;
	}

	public void setQuantityApply(Double quantityApply) {
		this.quantityApply = quantityApply;
	}

	public Double getWarehouseCodeProcess1() {
		return warehouseCodeProcess1;
	}

	public void setWarehouseCodeProcess1(Double warehouseCodeProcess1) {
		this.warehouseCodeProcess1 = warehouseCodeProcess1;
	}

	public Double getQuantityProcess1() {
		return quantityProcess1;
	}

	public void setQuantityProcess1(Double quantityProcess1) {
		this.quantityProcess1 = quantityProcess1;
	}

	public Double getWarehouseCodeProcess2() {
		return warehouseCodeProcess2;
	}

	public void setWarehouseCodeProcess2(Double warehouseCodeProcess2) {
		this.warehouseCodeProcess2 = warehouseCodeProcess2;
	}

	public Double getQuantityProcess2() {
		return quantityProcess2;
	}

	public void setQuantityProcess2(Double quantityProcess2) {
		this.quantityProcess2 = quantityProcess2;
	}

	public Double getWarehouseCodeProcess3() {
		return warehouseCodeProcess3;
	}

	public void setWarehouseCodeProcess3(Double warehouseCodeProcess3) {
		this.warehouseCodeProcess3 = warehouseCodeProcess3;
	}

	public Double getQuantityProcess3() {
		return quantityProcess3;
	}

	public void setQuantityProcess3(Double quantityProcess3) {
		this.quantityProcess3 = quantityProcess3;
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

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getStockSalesqty() {
		return stockSalesqty;
	}

	public void setStockSalesqty(Double stockSalesqty) {
		this.stockSalesqty = stockSalesqty;
	}

	public Double getStockOnHandqty() {
		return stockOnHandqty;
	}

	public void setStockOnHandqty(Double stockOnHandqty) {
		this.stockOnHandqty = stockOnHandqty;
	}

	public Double getUnCommitqty() {
		return unCommitqty;
	}

	public void setUnCommitqty(Double unCommitqty) {
		this.unCommitqty = unCommitqty;
	}

	public Double getWarehouseCodeProcess99() {
		return warehouseCodeProcess99;
	}

	public void setWarehouseCodeProcess99(Double warehouseCodeProcess99) {
		this.warehouseCodeProcess99 = warehouseCodeProcess99;
	}

	public Double getWarehouseCodeProcessfg() {
		return warehouseCodeProcessfg;
	}

	public void setWarehouseCodeProcessfg(Double warehouseCodeProcessfg) {
		this.warehouseCodeProcessfg = warehouseCodeProcessfg;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	


}