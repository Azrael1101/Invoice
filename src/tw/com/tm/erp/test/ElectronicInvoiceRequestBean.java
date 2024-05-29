package tw.com.tm.erp.test;

import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;

public class ElectronicInvoiceRequestBean<T> {
 //執行動作
 private String taskCode;
 private String shopCode;
 private String posMachineCode;
 private String executeWay; // pos開機執行:INIT
 private Long invoiceId;
 private SoSalesOrderHead soSalesOrderHead;
 public Long getInvoiceId() {
	return invoiceId;
}

public void setInvoiceId(Long invoiceId) {
	this.invoiceId = invoiceId;
}

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
 
 public String getShopCode() {
  return shopCode;
 }

 public void setShopCode(String shopCode) {
  this.shopCode = shopCode;
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
 
 public String getExecuteWay() {
  return executeWay;
 }

 public void setExecuteWay(String executeWay) {
  this.executeWay = executeWay;
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