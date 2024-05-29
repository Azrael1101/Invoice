package tw.com.tm.erp.hbm.bean;


public class BuVipPromote implements java.io.Serializable {

	private static final long serialVersionUID = 3699940356722417390L;
	private long sysSno;
	private String customerCode;
	private String promoteCategory;
	public long getSysSno() {
		return sysSno;
	}
	public void setSysSno(long sysSno) {
		this.sysSno = sysSno;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getPromoteCategory() {
		return promoteCategory;
	}
	public void setPromoteCategory(String promoteCategory) {
		this.promoteCategory = promoteCategory;
	}
}
