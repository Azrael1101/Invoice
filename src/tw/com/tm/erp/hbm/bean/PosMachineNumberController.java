package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PosMachineNumberController implements java.io.Serializable {

	private static final long serialVersionUID = 3699940356722417342L;
	
	private Long invoiceSno;
	private String invoiceHeader;
	private String currentUse;
	private String invoiceStart;
	private String invoiceEnd;
	private String status;
	private String posMachineCode;
	private String taxYearMonth;
	private String sysTime;
	private String startDate;
	private String endDate;
	
	
	public String getCurrentUse() {
		return currentUse;
	}
	public void setCurrentUse(String currentUse) {
		this.currentUse = currentUse;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Long getInvoiceSno() {
		return invoiceSno;
	}
	public void setInvoiceSno(Long invoiceSno) {
		this.invoiceSno = invoiceSno;
	}
	public String getInvoiceHeader() {
		return invoiceHeader;
	}
	public void setInvoiceHeader(String invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}
	public String getInvoiceStart() {
		return invoiceStart;
	}
	public void setInvoiceStart(String invoiceStart) {
		this.invoiceStart = invoiceStart;
	}
	public String getInvoiceEnd() {
		return invoiceEnd;
	}
	public void setInvoiceEnd(String invoiceEnd) {
		this.invoiceEnd = invoiceEnd;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPosMachineCode() {
		return posMachineCode;
	}
	public void setPosMachineCode(String posMachineCode) {
		this.posMachineCode = posMachineCode;
	}
	public String getTaxYearMonth() {
		return taxYearMonth;
	}
	public void setTaxYearMonth(String taxYearMonth) {
		this.taxYearMonth = taxYearMonth;
	}
	public String getSysTime() {
		return sysTime;
	}
	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}
	
	
	
	
}
