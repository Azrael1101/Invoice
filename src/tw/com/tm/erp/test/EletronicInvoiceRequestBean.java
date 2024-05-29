package tw.com.tm.erp.test;

import java.io.Serializable;

import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;

public class EletronicInvoiceRequestBean<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -792028440133382205L;
	//執行動作
	private String taskCode;
	private String posMachineCode;
	private SoSalesOrderHead soSalesOrderHead;
	/**
	 * @return the action
	 */
	public String getTaskCode() {
		return taskCode;
	}

	/**
	 * @param action the action to set
	 */
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	/**
	 * @return the posMachineCode
	 */
	public String getPosMachineCode() {
		return posMachineCode;
	}

	/**
	 * @param posMachineCode the posMachineCode to set
	 */
	public void setPosMachineCode(String posMachineCode) {
		this.posMachineCode = posMachineCode;
	}

	/**
	 * @return the soSalesOrderHead
	 */
	public SoSalesOrderHead getSoSalesOrderHead() {
		return soSalesOrderHead;
	}

	/**
	 * @param soSalesOrderHead the soSalesOrderHead to set
	 */
	public void setSoSalesOrderHead(SoSalesOrderHead soSalesOrderHead) {
		this.soSalesOrderHead = soSalesOrderHead;
	}
	
	
}
