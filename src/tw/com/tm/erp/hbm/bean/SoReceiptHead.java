package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SoReceiptHead implements java.io.Serializable {

	private static final long serialVersionUID = 3699940356722417342L;
	
	private long headId;
	private String brandCode;
	private String receiptYear;
	private String receiptMonth;
	private String receiptType;
	private List<SoReceiptLine> soReceiptLines = new ArrayList();
	public List<SoReceiptLine> getSoReceiptLines() {
		return soReceiptLines;
	}
	public void setSoReceiptLines(List<SoReceiptLine> soReceiptLines) {
		this.soReceiptLines = soReceiptLines;
	}
	public long getHeadId() {
		return headId;
	}
	public void setHeadId(long headId) {
		this.headId = headId;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getReceiptYear() {
		return receiptYear;
	}
	public void setReceiptYear(String receiptYear) {
		this.receiptYear = receiptYear;
	}
	public String getReceiptMonth() {
		return receiptMonth;
	}
	public void setReceiptMonth(String receiptMonth) {
		this.receiptMonth = receiptMonth;
	}
	public String getReceiptType() {
		return receiptType;
	}
	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}
	
}
