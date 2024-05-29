package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * TmpImportPosPayment entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TmpImportPosPayment implements java.io.Serializable {

    // Fields

    private TmpImportPosPaymentId id;
    private String shopCode;
    private String storeId;
    private String payId;
    private Double payAmt;
    private String payNo;
    private Double payQty;
    private Double exchangeRate;
    private Double payDue;
    private String orderFlag;
    private String orderId;
    private String casherCode;
    private String remark1;
    private String remark2;
    private String fileName;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;

    // Constructors

    /** default constructor */
    public TmpImportPosPayment() {
    }

    /** minimal constructor */
    public TmpImportPosPayment(TmpImportPosPaymentId id) {
	this.id = id;
    }

    /** full constructor */
    public TmpImportPosPayment(TmpImportPosPaymentId id, String shopCode,
	    String storeId, String payId, Double payAmt, String payNo,
	    Double payQty, Double exchangeRate, Double payDue,
	    String orderFlag, String orderId, String casherCode,
	    String remark1, String remark2, String fileName, String reserve1,
	    String reserve2, String reserve3, String reserve4, String reserve5,
	    String createdBy, Date creationDate) {
	this.id = id;
	this.shopCode = shopCode;
	this.storeId = storeId;
	this.payId = payId;
	this.payAmt = payAmt;
	this.payNo = payNo;
	this.payQty = payQty;
	this.exchangeRate = exchangeRate;
	this.payDue = payDue;
	this.orderFlag = orderFlag;
	this.orderId = orderId;
	this.casherCode = casherCode;
	this.remark1 = remark1;
	this.remark2 = remark2;
	this.fileName = fileName;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
    }

    // Property accessors

    public TmpImportPosPaymentId getId() {
	return this.id;
    }

    public void setId(TmpImportPosPaymentId id) {
	this.id = id;
    }

    public String getShopCode() {
	return this.shopCode;
    }

    public void setShopCode(String shopCode) {
	this.shopCode = shopCode;
    }

    public String getStoreId() {
	return this.storeId;
    }

    public void setStoreId(String storeId) {
	this.storeId = storeId;
    }

    public String getPayId() {
	return this.payId;
    }

    public void setPayId(String payId) {
	this.payId = payId;
    }

    public Double getPayAmt() {
	return this.payAmt;
    }

    public void setPayAmt(Double payAmt) {
	this.payAmt = payAmt;
    }

    public String getPayNo() {
	return this.payNo;
    }

    public void setPayNo(String payNo) {
	this.payNo = payNo;
    }

    public Double getPayQty() {
	return this.payQty;
    }

    public void setPayQty(Double payQty) {
	this.payQty = payQty;
    }

    public Double getExchangeRate() {
	return this.exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
	this.exchangeRate = exchangeRate;
    }

    public Double getPayDue() {
	return this.payDue;
    }

    public void setPayDue(Double payDue) {
	this.payDue = payDue;
    }

    public String getOrderFlag() {
	return this.orderFlag;
    }

    public void setOrderFlag(String orderFlag) {
	this.orderFlag = orderFlag;
    }

    public String getOrderId() {
	return this.orderId;
    }

    public void setOrderId(String orderId) {
	this.orderId = orderId;
    }

    public String getCasherCode() {
	return this.casherCode;
    }

    public void setCasherCode(String casherCode) {
	this.casherCode = casherCode;
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

    public String getFileName() {
	return this.fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
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

    public String getCreatedBy() {
	return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    public Date getCreationDate() {
	return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
    }

}