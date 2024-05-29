package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * CmDeclarationItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CmDeclarationItem implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3467663133202052971L;
	private Long itemId; //PK
	private CmDeclarationHead cmDeclarationHead;
	private String t2; //細項資料
	private String declNo; //報單號碼
	private Long itemNo; //報單項次
	private String prdtNo;  //料號
	private String descrip;  //貨名
	private String brand;  //廠牌
	private String model;  //型號
	private String spec;  //規格
	private Double NWght = new Double(0);  //淨重
	private Double qty = new Double(0);  //數量
	private String unit;  //單位
	private String ODeclNo;  //原進倉報單號碼
	private Long OItemNo;  //原進倉報單項次
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long indexNo;
	private String descripOther ;
	private Date ODeclDate ;
	private String code ;
	private String pacs ;
	private String produceCountry ;
	private Double stuAnt ;
	private String stuUnit ;
	private Double fobValue ;
	private Double custValueAmt ;
	private Double unitPrice ;
	private String currencyCode ;
	private String unitCuritem ;
	private String buyCommNo ;
	private String saleCommNo ;
	private String statmode ;
	private String duty1 ;
	private Double dutyRate1 ;	
	private String duty2 ;
	private Double dutyRate2 ;
	private String duty3 ;
	private Double dutyRate3 ;
	private String duty4 ;
	private Double dutyRate4 ;
	private String permitNo ;
	private Double permitItem ;
	private Double pricduty ;
	private String adj ;
	private String assGoodNo ;
	private Long dutiableValuePrice;
	
	// Constructors

	public Long getDutiableValuePrice() {
		return dutiableValuePrice;
	}

	public void setDutiableValuePrice(Long dutiableValuePrice) {
		this.dutiableValuePrice = dutiableValuePrice;
	}

	/** default constructor */
	public CmDeclarationItem() {
	}

	/** minimal constructor */
	public CmDeclarationItem(Long itemId) {
		this.itemId = itemId;
	}

	

	// Property accessors

	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public CmDeclarationHead getCmDeclarationHead() {
		return this.cmDeclarationHead;
	}

	public void setCmDeclarationHead(CmDeclarationHead cmDeclarationHead) {
		this.cmDeclarationHead = cmDeclarationHead;
	}

	public String getT2() {
		return this.t2;
	}

	public void setT2(String t2) {
		this.t2 = t2;
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

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
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

	public Long getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getDescripOther() {
		return descripOther;
	}

	public void setDescripOther(String descripOther) {
		this.descripOther = descripOther;
	}

	public Date getODeclDate() {
		return ODeclDate;
	}

	public void setODeclDate(Date declDate) {
		ODeclDate = declDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPacs() {
		return pacs;
	}

	public void setPacs(String pacs) {
		this.pacs = pacs;
	}

	public String getProduceCountry() {
		return produceCountry;
	}

	public void setProduceCountry(String produceCountry) {
		this.produceCountry = produceCountry;
	}

	public Double getStuAnt() {
		return stuAnt;
	}

	public void setStuAnt(Double stuAnt) {
		this.stuAnt = stuAnt;
	}

	public String getStuUnit() {
		return stuUnit;
	}

	public void setStuUnit(String stuUnit) {
		this.stuUnit = stuUnit;
	}

	public Double getFobValue() {
		return fobValue;
	}

	public void setFobValue(Double fobValue) {
		this.fobValue = fobValue;
	}

	public Double getCustValueAmt() {
		return custValueAmt;
	}

	public void setCustValueAmt(Double custValueAmt) {
		this.custValueAmt = custValueAmt;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getUnitCuritem() {
		return unitCuritem;
	}

	public void setUnitCuritem(String unitCuritem) {
		this.unitCuritem = unitCuritem;
	}

	public String getBuyCommNo() {
		return buyCommNo;
	}

	public void setBuyCommNo(String buyCommNo) {
		this.buyCommNo = buyCommNo;
	}

	public String getSaleCommNo() {
		return saleCommNo;
	}

	public void setSaleCommNo(String saleCommNo) {
		this.saleCommNo = saleCommNo;
	}

	public String getStatmode() {
		return statmode;
	}

	public void setStatmode(String statmode) {
		this.statmode = statmode;
	}

	public String getDuty1() {
		return duty1;
	}

	public void setDuty1(String duty1) {
		this.duty1 = duty1;
	}

	public Double getDutyRate1() {
		return dutyRate1;
	}

	public void setDutyRate1(Double dutyRate1) {
		this.dutyRate1 = dutyRate1;
	}

	public String getDuty2() {
		return duty2;
	}

	public void setDuty2(String duty2) {
		this.duty2 = duty2;
	}

	public Double getDutyRate2() {
		return dutyRate2;
	}

	public void setDutyRate2(Double dutyRate2) {
		this.dutyRate2 = dutyRate2;
	}

	public String getDuty3() {
		return duty3;
	}

	public void setDuty3(String duty3) {
		this.duty3 = duty3;
	}

	public Double getDutyRate3() {
		return dutyRate3;
	}

	public void setDutyRate3(Double dutyRate3) {
		this.dutyRate3 = dutyRate3;
	}

	public String getDuty4() {
		return duty4;
	}

	public void setDuty4(String duty4) {
		this.duty4 = duty4;
	}

	public Double getDutyRate4() {
		return dutyRate4;
	}

	public void setDutyRate4(Double dutyRate4) {
		this.dutyRate4 = dutyRate4;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public Double getPermitItem() {
		return permitItem;
	}

	public void setPermitItem(Double permitItem) {
		this.permitItem = permitItem;
	}

	public Double getPricduty() {
		return pricduty;
	}

	public void setPricduty(Double pricduty) {
		this.pricduty = pricduty;
	}
	
	public String getAdj() {
		return adj;
	}

	public void setAdj(String adj) {
		this.adj = adj;
	}

	public String getAssGoodNo() {
		return assGoodNo;
	}

	public void setAssGoodNo(String assGoodNo) {
		this.assGoodNo = assGoodNo;
	}

}