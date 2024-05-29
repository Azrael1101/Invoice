package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * FiInvoiceHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FiInvoiceHead implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3784686350400084352L;
	public static String FI_INVOICE_ORDER_TYPE = "FII" ;

	private Long headId;
	private String brandCode;
	private String invoiceNo;
	private Date invoiceDate;
	private Date estimatedTimeDeparture;
	private Date estimatedTimeArrival;
	private String currencyCode;
	private String supplierCode ;
	private String supplierOrderNo;
	private String customsDeclarationType;
	private String customsDeclarationNo;
	private String reserve1;	// 2009.10.06 借用儲存 tmp 單號, arthur
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private List<FiInvoiceLine> fiInvoiceLines = new ArrayList();
	private String status ;
	private Double exchangeRate ;
	private Double totalLocalInvoiceAmount ;
	private Double totalForeignInvoiceAmount ;	
	private String lotNo ;
	private String orderTypeCode ;
	private String receiveOrderNo ;
	private String receiveOrderTypeCode ;
	private Long totalBoxNo ;
	private String weightUnit ;
	private String remark1 ;
	private String remark2 ;
	private String remark3 ;
	private String remark4 ;
	private String remark5 ;
	private String remark6 ;
	private Long   customsSeq;
	private String supplierName ;
	private String financeConfirm;			// 財務確認

	// Constructors

	public String getSupplierName() {
	    return supplierName;
	}

	public void setSupplierName(String supplierName) {
	    this.supplierName = supplierName;
	}

	/** default constructor */
	public FiInvoiceHead() {
	}

	/** minimal constructor */
	public FiInvoiceHead(Long headId) {
		this.headId = headId;
	}

	

	// Property accessors

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getEstimatedTimeDeparture() {
		return this.estimatedTimeDeparture;
	}

	public void setEstimatedTimeDeparture(Date estimatedTimeDeparture) {
		this.estimatedTimeDeparture = estimatedTimeDeparture;
	}

	public Date getEstimatedTimeArrival() {
		return this.estimatedTimeArrival;
	}

	public void setEstimatedTimeArrival(Date estimatedTimeArrival) {
		this.estimatedTimeArrival = estimatedTimeArrival;
	}

	public String getSupplierOrderNo() {
		return this.supplierOrderNo;
	}

	public void setSupplierOrderNo(String supplierOrderNo) {
		this.supplierOrderNo = supplierOrderNo;
	}

	public String getCustomsDeclarationType() {
		return this.customsDeclarationType;
	}

	public void setCustomsDeclarationType(String customsDeclarationType) {
		this.customsDeclarationType = customsDeclarationType;
	}

	public String getCustomsDeclarationNo() {
		return this.customsDeclarationNo;
	}

	public void setCustomsDeclarationNo(String customsDeclarationNo) {
		this.customsDeclarationNo = customsDeclarationNo;
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

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public List<FiInvoiceLine> getFiInvoiceLines() {
		return this.fiInvoiceLines;
	}

	public void setFiInvoiceLines(List<FiInvoiceLine> fiInvoiceLines) {
		this.fiInvoiceLines = fiInvoiceLines;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getTotalLocalInvoiceAmount() {
		return totalLocalInvoiceAmount;
	}

	public void setTotalLocalInvoiceAmount(Double totalLocalInvoiceAmount) {
		this.totalLocalInvoiceAmount = totalLocalInvoiceAmount;
	}

	public Double getTotalForeignInvoiceAmount() {
		return totalForeignInvoiceAmount;
	}

	public void setTotalForeignInvoiceAmount(Double totalForeignInvoiceAmount) {
		this.totalForeignInvoiceAmount = totalForeignInvoiceAmount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getReceiveOrderNo() {
		return receiveOrderNo;
	}

	public void setReceiveOrderNo(String receiveOrderNo) {
		this.receiveOrderNo = receiveOrderNo;
	}

	public String getReceiveOrderTypeCode() {
		return receiveOrderTypeCode;
	}

	public void setReceiveOrderTypeCode(String receiveOrderTypeCode) {
		this.receiveOrderTypeCode = receiveOrderTypeCode;
	}

	public Long getTotalBoxNo() {
		return totalBoxNo;
	}

	public void setTotalBoxNo(Long totalBoxNo) {
		this.totalBoxNo = totalBoxNo;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public String getRemark4() {
		return remark4;
	}

	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}

	public String getRemark5() {
		return remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

	public String getRemark6() {
		return remark6;
	}

	public void setRemark6(String remark6) {
		this.remark6 = remark6;
	}	
	
	public Long getCustomsSeq() {
	    return customsSeq;
	}

	public void setCustomsSeq(Long customsSeq) {
	    this.customsSeq = customsSeq;
	}

	public String getFinanceConfirm() {
	    return financeConfirm;
	}

	public void setFinanceConfirm(String financeConfirm) {
	    this.financeConfirm = financeConfirm;
	}	

}