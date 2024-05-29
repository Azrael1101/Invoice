package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * CmDeclarationViewId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CmDeclarationView implements java.io.Serializable {

	private static final long serialVersionUID = -2761933050576301465L;
	// Fields
	private Long headId;
	private String msgFun;
	private String bondNo;
	private String strType;
	private String boxNo;
	private String declType;
	private String declNo;
	private Long itemNo;
	private Date importDate;
	private Date declDate;
	private Date originalDate;
	private String stgPlace;
	private Date rlsTime;
	private Long rlsPkg;
	private String extraCond;
	private String pkgUnit;
	private Double GWgt;
	private String vesselSign;
	private String voyageNo;
	private String shipCode;
	private String exporter;
	private String clearType;
	private String refBillNo;
	private String inbondNo;
	private String outbondNo;
	private String status;
	private String spec;
	private String prdtNo;
	private String descrip;
	private String brand;
	private String model;
	private Double NWght;
	private Double qty;
	private String unit;
	private String ODeclNo;
	private Long OItemNo;
	private String descripOther;
	private String produceCountry;
	private String code;
	private Long fobValue;
	private Long custValueAmt;
	private Double unitPrice;
	private String currencyCode;
	private String unitCuritem;
	private String permitNo;
	private Long permitItem;
	private Double pricduty;
	private String adj;
	private String assGoodNo;
	private Long dutiableValuePrice;
	private String isExtention;
	private Date ODeclDate;
	private Date orgImportDate;
	private String ODeclType;

	// Constructors

	/** default constructor */
	public CmDeclarationView() {
	}

	/** full constructor */
	public CmDeclarationView(Long headId, String msgFun, String bondNo,
			String strType, String boxNo, String declType, String declNo,
			Long itemNo, Date importDate, Date declDate, Date originalDate,
			String stgPlace, Date rlsTime, Long rlsPkg, String extraCond,
			String pkgUnit, Double GWgt, String vesselSign, String voyageNo,
			String shipCode, String exporter, String clearType,
			String refBillNo, String inbondNo, String outbondNo, String status,
			String spec, String prdtNo, String descrip, String brand,
			String model, Double NWght, Double qty, String unit,
			String ODeclNo, Long OItemNo, String descripOther,
			String produceCountry, String code, Long fobValue,
			Long custValueAmt, Double unitPrice, String currencyCode,
			String unitCuritem, String permitNo, Long permitItem,
			Double pricduty, String adj, String assGoodNo,
			Long dutiableValuePrice, String isExtention, Date ODeclDate,
			Date orgImportDate, String ODeclType) {
		this.headId = headId;
		this.msgFun = msgFun;
		this.bondNo = bondNo;
		this.strType = strType;
		this.boxNo = boxNo;
		this.declType = declType;
		this.declNo = declNo;
		this.itemNo = itemNo;
		this.importDate = importDate;
		this.declDate = declDate;
		this.originalDate = originalDate;
		this.stgPlace = stgPlace;
		this.rlsTime = rlsTime;
		this.rlsPkg = rlsPkg;
		this.extraCond = extraCond;
		this.pkgUnit = pkgUnit;
		this.GWgt = GWgt;
		this.vesselSign = vesselSign;
		this.voyageNo = voyageNo;
		this.shipCode = shipCode;
		this.exporter = exporter;
		this.clearType = clearType;
		this.refBillNo = refBillNo;
		this.inbondNo = inbondNo;
		this.outbondNo = outbondNo;
		this.status = status;
		this.spec = spec;
		this.prdtNo = prdtNo;
		this.descrip = descrip;
		this.brand = brand;
		this.model = model;
		this.NWght = NWght;
		this.qty = qty;
		this.unit = unit;
		this.ODeclNo = ODeclNo;
		this.OItemNo = OItemNo;
		this.descripOther = descripOther;
		this.produceCountry = produceCountry;
		this.code = code;
		this.fobValue = fobValue;
		this.custValueAmt = custValueAmt;
		this.unitPrice = unitPrice;
		this.currencyCode = currencyCode;
		this.unitCuritem = unitCuritem;
		this.permitNo = permitNo;
		this.permitItem = permitItem;
		this.pricduty = pricduty;
		this.adj = adj;
		this.assGoodNo = assGoodNo;
		this.dutiableValuePrice = dutiableValuePrice;
		this.isExtention = isExtention;
		this.ODeclDate = ODeclDate;
		this.orgImportDate = orgImportDate;
		this.ODeclType = ODeclType;
	}

	// Property accessors

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getMsgFun() {
		return this.msgFun;
	}

	public void setMsgFun(String msgFun) {
		this.msgFun = msgFun;
	}

	public String getBondNo() {
		return this.bondNo;
	}

	public void setBondNo(String bondNo) {
		this.bondNo = bondNo;
	}

	public String getStrType() {
		return this.strType;
	}

	public void setStrType(String strType) {
		this.strType = strType;
	}

	public String getBoxNo() {
		return this.boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getDeclType() {
		return this.declType;
	}

	public void setDeclType(String declType) {
		this.declType = declType;
	}

	public String getDeclNo() {
		return this.declNo;
	}

	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}

	public Long getItemNo() {
		return this.itemNo;
	}

	public void setItemNo(Long itemNo) {
		this.itemNo = itemNo;
	}

	public Date getImportDate() {
		return this.importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public Date getDeclDate() {
		return this.declDate;
	}

	public void setDeclDate(Date declDate) {
		this.declDate = declDate;
	}

	public Date getOriginalDate() {
		return this.originalDate;
	}

	public void setOriginalDate(Date originalDate) {
		this.originalDate = originalDate;
	}

	public String getStgPlace() {
		return this.stgPlace;
	}

	public void setStgPlace(String stgPlace) {
		this.stgPlace = stgPlace;
	}

	public Date getRlsTime() {
		return this.rlsTime;
	}

	public void setRlsTime(Date rlsTime) {
		this.rlsTime = rlsTime;
	}

	public Long getRlsPkg() {
		return this.rlsPkg;
	}

	public void setRlsPkg(Long rlsPkg) {
		this.rlsPkg = rlsPkg;
	}

	public String getExtraCond() {
		return this.extraCond;
	}

	public void setExtraCond(String extraCond) {
		this.extraCond = extraCond;
	}

	public String getPkgUnit() {
		return this.pkgUnit;
	}

	public void setPkgUnit(String pkgUnit) {
		this.pkgUnit = pkgUnit;
	}

	public Double getGWgt() {
		return this.GWgt;
	}

	public void setGWgt(Double GWgt) {
		this.GWgt = GWgt;
	}

	public String getVesselSign() {
		return this.vesselSign;
	}

	public void setVesselSign(String vesselSign) {
		this.vesselSign = vesselSign;
	}

	public String getVoyageNo() {
		return this.voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public String getShipCode() {
		return this.shipCode;
	}

	public void setShipCode(String shipCode) {
		this.shipCode = shipCode;
	}

	public String getExporter() {
		return this.exporter;
	}

	public void setExporter(String exporter) {
		this.exporter = exporter;
	}

	public String getClearType() {
		return this.clearType;
	}

	public void setClearType(String clearType) {
		this.clearType = clearType;
	}

	public String getRefBillNo() {
		return this.refBillNo;
	}

	public void setRefBillNo(String refBillNo) {
		this.refBillNo = refBillNo;
	}

	public String getInbondNo() {
		return this.inbondNo;
	}

	public void setInbondNo(String inbondNo) {
		this.inbondNo = inbondNo;
	}

	public String getOutbondNo() {
		return this.outbondNo;
	}

	public void setOutbondNo(String outbondNo) {
		this.outbondNo = outbondNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getPrdtNo() {
		return this.prdtNo;
	}

	public void setPrdtNo(String prdtNo) {
		this.prdtNo = prdtNo;
	}

	public String getDescrip() {
		return this.descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Double getNWght() {
		return this.NWght;
	}

	public void setNWght(Double NWght) {
		this.NWght = NWght;
	}

	public Double getQty() {
		return this.qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getODeclNo() {
		return this.ODeclNo;
	}

	public void setODeclNo(String ODeclNo) {
		this.ODeclNo = ODeclNo;
	}

	public Long getOItemNo() {
		return this.OItemNo;
	}

	public void setOItemNo(Long OItemNo) {
		this.OItemNo = OItemNo;
	}

	public String getDescripOther() {
		return this.descripOther;
	}

	public void setDescripOther(String descripOther) {
		this.descripOther = descripOther;
	}

	public String getProduceCountry() {
		return this.produceCountry;
	}

	public void setProduceCountry(String produceCountry) {
		this.produceCountry = produceCountry;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getFobValue() {
		return this.fobValue;
	}

	public void setFobValue(Long fobValue) {
		this.fobValue = fobValue;
	}

	public Long getCustValueAmt() {
		return this.custValueAmt;
	}

	public void setCustValueAmt(Long custValueAmt) {
		this.custValueAmt = custValueAmt;
	}

	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getUnitCuritem() {
		return this.unitCuritem;
	}

	public void setUnitCuritem(String unitCuritem) {
		this.unitCuritem = unitCuritem;
	}

	public String getPermitNo() {
		return this.permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public Long getPermitItem() {
		return this.permitItem;
	}

	public void setPermitItem(Long permitItem) {
		this.permitItem = permitItem;
	}

	public Double getPricduty() {
		return this.pricduty;
	}

	public void setPricduty(Double pricduty) {
		this.pricduty = pricduty;
	}

	public String getAdj() {
		return this.adj;
	}

	public void setAdj(String adj) {
		this.adj = adj;
	}

	public String getAssGoodNo() {
		return this.assGoodNo;
	}

	public void setAssGoodNo(String assGoodNo) {
		this.assGoodNo = assGoodNo;
	}

	public Long getDutiableValuePrice() {
		return this.dutiableValuePrice;
	}

	public void setDutiableValuePrice(Long dutiableValuePrice) {
		this.dutiableValuePrice = dutiableValuePrice;
	}

	public String getIsExtention() {
		return this.isExtention;
	}

	public void setIsExtention(String isExtention) {
		this.isExtention = isExtention;
	}

	public Date getODeclDate() {
		return this.ODeclDate;
	}

	public void setODeclDate(Date ODeclDate) {
		this.ODeclDate = ODeclDate;
	}

	public Date getOrgImportDate() {
		return this.orgImportDate;
	}

	public void setOrgImportDate(Date orgImportDate) {
		this.orgImportDate = orgImportDate;
	}

	public String getODeclType() {
		return this.ODeclType;
	}

	public void setODeclType(String ODeclType) {
		this.ODeclType = ODeclType;
	}
}