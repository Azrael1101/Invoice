package tw.com.tm.erp.hbm.bean;

import java.util.Date;

public class SoReceiptLine implements java.io.Serializable {

	private static final long serialVersionUID = 3699940356722417341L;
	
	private long lineId;
	private long headId;
	private SoReceiptHead soReceiptHead;
	private String receiptPrefix;
	
	private String receiptSerialNumberBegin;
	private String receiptSerialNumberEnd;
	private String receiptQuantity;
	private String posMachineCode;
	private String shopCode;
	private String receiptSerialNumberNextVal;
	private Long indexNo;
	
	
	public long getHeadId() {
		return headId;
	}
	public void setHeadId(long headId) {
		this.headId = headId;
	}
	public SoReceiptHead getSoReceiptHead() {
		return soReceiptHead;
	}
	public void setSoReceiptHead(SoReceiptHead soReceiptHead) {
		this.soReceiptHead = soReceiptHead;
	}
	public Long getIndexNo() {
		return indexNo;
	}
	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}
	public String getReceiptSerialNumberNextVal() {
		return receiptSerialNumberNextVal;
	}
	public void setReceiptSerialNumberNextVal(String receiptSerialNumberNextVal) {
		this.receiptSerialNumberNextVal = receiptSerialNumberNextVal;
	}
	public long getLineId() {
		return lineId;
	}
	public void setLineId(long lineId) {
		this.lineId = lineId;
	}
	
	public String getReceiptPrefix() {
		return receiptPrefix;
	}
	public void setReceiptPrefix(String receiptPrefix) {
		this.receiptPrefix = receiptPrefix;
	}
	public String getReceiptSerialNumberBegin() {
		return receiptSerialNumberBegin;
	}
	public void setReceiptSerialNumberBegin(String receiptSerialNumberBegin) {
		this.receiptSerialNumberBegin = receiptSerialNumberBegin;
	}
	public String getReceiptSerialNumberEnd() {
		return receiptSerialNumberEnd;
	}
	public void setReceiptSerialNumberEnd(String receiptSerialNumberEnd) {
		this.receiptSerialNumberEnd = receiptSerialNumberEnd;
	}
	public String getReceiptQuantity() {
		return receiptQuantity;
	}
	public void setReceiptQuantity(String receiptQuantity) {
		this.receiptQuantity = receiptQuantity;
	}
	public String getPosMachineCode() {
		return posMachineCode;
	}
	public void setPosMachineCode(String posMachineCode) {
		this.posMachineCode = posMachineCode;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	
}
