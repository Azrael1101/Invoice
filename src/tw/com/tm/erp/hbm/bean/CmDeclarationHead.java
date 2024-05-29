package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CmDeclarationHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CmDeclarationHead implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5350770089716534595L;
	private Long headId;
	private String t1;
	private String msgFun;
	private String bondNo;
	private String strType;
	private String boxNo;
	private String declType;
	private String declNo;
	private Date importDate;
	private Date declDate;
	private String stgPlace;
	private Date rlsTime;
	private Long rlsPkg;
	private String extraCond;
	private String pkgUnit;
	private Double GWgt = new Double(0);;
	private String vesselSign;
	private String voyageNo;
	private String shipCode;
	private String exporter;
	private String clearType;
	private String refBillNo;
	private String inbondNo;
	private String outbondNo;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private List<CmDeclarationVehicle> cmDeclarationVehicles = new ArrayList();
	private List<CmDeclarationItem> cmDeclarationItems = new ArrayList();
	private List<CmDeclarationContainer> cmDeclarationContainers = new ArrayList();
	private String status ;
	private String fileName ;
	private String shipPort ;
	private String countryCode ;
	private Date exportDate ;
	private String mawbNo ;
	private String hawbNo ;
	private String originalDeclNo ;
	private String buyPayName ;
	private String salePayName ;
	private String buyBan ;
	private String buyBfNo ;
	private String saleBfNo ;
	private String relBfNo ;
	private String exWhBan ;
	private String imWhBan ;
	private Double fobValue ;
	private Double originalFobValue ;
	private Double cifValue ;
	private Double originalCifValue ;
	private String currencyCode ;
	private Double exchangeRate ;
	private Double freightFee ;
	private Double insuranceFee ;
	private Double additionFee ;
	private Double deductionFee ;
	private Double NWgt ;
	private String moaType1 ;
	private Double dutyAmt1 ;
	private String moaType2 ;
	private Double dutyAmt2 ;
	private String moaType3 ;
	private Double dutyAmt3 ;
	private String moaType4 ;
	private Double dutyAmt4 ;
	private String moaType5 ;
	private Double dutyAmt5 ;
	private String moaType6 ;
	private Double dutyAmt6 ;
	private String moaType7 ;
	private Double dutyAmt7 ;
	private Double dutyAmt ;
	private Double totalDuty ;
	private String dutyDase ;
	private Date sendDate ;
	private String sendTime ;
	private String transType ;
	private String exporterId ;
	private String exWhNo ;
	private String imWhNo ;
	private String isExtention;
	private String fileNo;
	private Date orgImportDate;
	
	// Constructors

	public Date getOrgImportDate() {
		return orgImportDate;
	}

	public void setOrgImportDate(Date orgImportDate) {
		this.orgImportDate = orgImportDate;
	}

	public String getIsExtention() {
		return isExtention;
	}

	public void setIsExtention(String isExtention) {
		this.isExtention = isExtention;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	/** default constructor */
	public CmDeclarationHead() {
	}

	/** minimal constructor */
	public CmDeclarationHead(Long headId) {
		this.headId = headId;
	}

	
	// Property accessors

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getT1() {
		return this.t1;
	}

	public void setT1(String t1) {
		this.t1 = t1;
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

	public List<CmDeclarationVehicle> getCmDeclarationVehicles() {
		return this.cmDeclarationVehicles;
	}

	public void setCmDeclarationVehicles(List<CmDeclarationVehicle> cmDeclarationVehicles) {
		this.cmDeclarationVehicles = cmDeclarationVehicles;
	}

	public List<CmDeclarationItem> getCmDeclarationItems() {
		return this.cmDeclarationItems;
	}

	public void setCmDeclarationItems(List<CmDeclarationItem> cmDeclarationItems) {
		this.cmDeclarationItems = cmDeclarationItems;
	}

	public List<CmDeclarationContainer> getCmDeclarationContainers() {
		return this.cmDeclarationContainers;
	}

	public void setCmDeclarationContainers(List<CmDeclarationContainer> cmDeclarationContainers) {
		this.cmDeclarationContainers = cmDeclarationContainers;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getShipPort() {
		return shipPort;
	}

	public void setShipPort(String shipPort) {
		this.shipPort = shipPort;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Date getExportDate() {
		return exportDate;
	}

	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}

	public String getMawbNo() {
		return mawbNo;
	}

	public void setMawbNo(String mawbNo) {
		this.mawbNo = mawbNo;
	}

	public String getHawbNo() {
		return hawbNo;
	}

	public void setHawbNo(String hawbNo) {
		this.hawbNo = hawbNo;
	}

	public String getOriginalDeclNo() {
		return originalDeclNo;
	}

	public void setOriginalDeclNo(String originalDeclNo) {
		this.originalDeclNo = originalDeclNo;
	}

	public String getBuyPayName() {
		return buyPayName;
	}

	public void setBuyPayName(String buyPayName) {
		this.buyPayName = buyPayName;
	}

	public String getSalePayName() {
		return salePayName;
	}

	public void setSalePayName(String salePayName) {
		this.salePayName = salePayName;
	}

	public String getBuyBan() {
		return buyBan;
	}

	public void setBuyBan(String buyBan) {
		this.buyBan = buyBan;
	}

	public String getBuyBfNo() {
		return buyBfNo;
	}

	public void setBuyBfNo(String buyBfNo) {
		this.buyBfNo = buyBfNo;
	}

	public String getSaleBfNo() {
		return saleBfNo;
	}

	public void setSaleBfNo(String saleBfNo) {
		this.saleBfNo = saleBfNo;
	}

	public String getRelBfNo() {
		return relBfNo;
	}

	public void setRelBfNo(String relBfNo) {
		this.relBfNo = relBfNo;
	}

	public String getExWhBan() {
		return exWhBan;
	}

	public void setExWhBan(String exWhBan) {
		this.exWhBan = exWhBan;
	}

	public String getImWhBan() {
		return imWhBan;
	}

	public void setImWhBan(String imWhBan) {
		this.imWhBan = imWhBan;
	}

	public Double getFobValue() {
		return fobValue;
	}

	public void setFobValue(Double fobValue) {
		this.fobValue = fobValue;
	}

	public Double getOriginalFobValue() {
		return originalFobValue;
	}

	public void setOriginalFobValue(Double originalFobValue) {
		this.originalFobValue = originalFobValue;
	}

	public Double getCifValue() {
		return cifValue;
	}

	public void setCifValue(Double cifValue) {
		this.cifValue = cifValue;
	}

	public Double getOriginalCifValue() {
		return originalCifValue;
	}

	public void setOriginalCifValue(Double originalCifValue) {
		this.originalCifValue = originalCifValue;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getFreightFee() {
		return freightFee;
	}

	public void setFreightFee(Double freightFee) {
		this.freightFee = freightFee;
	}

	public Double getInsuranceFee() {
		return insuranceFee;
	}

	public void setInsuranceFee(Double insuranceFee) {
		this.insuranceFee = insuranceFee;
	}

	public Double getAdditionFee() {
		return additionFee;
	}

	public void setAdditionFee(Double additionFee) {
		this.additionFee = additionFee;
	}

	public Double getDeductionFee() {
		return deductionFee;
	}

	public void setDeductionFee(Double deductionFee) {
		this.deductionFee = deductionFee;
	}

	public Double getNWgt() {
		return NWgt;
	}

	public void setNWgt(Double wgt) {
		NWgt = wgt;
	}

	public String getMoaType1() {
		return moaType1;
	}

	public void setMoaType1(String moaType1) {
		this.moaType1 = moaType1;
	}

	public Double getDutyAmt1() {
		return dutyAmt1;
	}

	public void setDutyAmt1(Double dutyAmt1) {
		this.dutyAmt1 = dutyAmt1;
	}

	public String getMoaType2() {
		return moaType2;
	}

	public void setMoaType2(String moaType2) {
		this.moaType2 = moaType2;
	}

	public Double getDutyAmt2() {
		return dutyAmt2;
	}

	public void setDutyAmt2(Double dutyAmt2) {
		this.dutyAmt2 = dutyAmt2;
	}

	public String getMoaType3() {
		return moaType3;
	}

	public void setMoaType3(String moaType3) {
		this.moaType3 = moaType3;
	}

	public Double getDutyAmt3() {
		return dutyAmt3;
	}

	public void setDutyAmt3(Double dutyAmt3) {
		this.dutyAmt3 = dutyAmt3;
	}

	public String getMoaType4() {
		return moaType4;
	}

	public void setMoaType4(String moaType4) {
		this.moaType4 = moaType4;
	}

	public Double getDutyAmt4() {
		return dutyAmt4;
	}

	public void setDutyAmt4(Double dutyAmt4) {
		this.dutyAmt4 = dutyAmt4;
	}

	public String getMoaType5() {
		return moaType5;
	}

	public void setMoaType5(String moaType5) {
		this.moaType5 = moaType5;
	}

	public Double getDutyAmt5() {
		return dutyAmt5;
	}

	public void setDutyAmt5(Double dutyAmt5) {
		this.dutyAmt5 = dutyAmt5;
	}

	public String getMoaType6() {
		return moaType6;
	}

	public void setMoaType6(String moaType6) {
		this.moaType6 = moaType6;
	}

	public Double getDutyAmt6() {
		return dutyAmt6;
	}

	public void setDutyAmt6(Double dutyAmt6) {
		this.dutyAmt6 = dutyAmt6;
	}

	public String getMoaType7() {
		return moaType7;
	}

	public void setMoaType7(String moaType7) {
		this.moaType7 = moaType7;
	}

	public Double getDutyAmt7() {
		return dutyAmt7;
	}

	public void setDutyAmt7(Double dutyAmt7) {
		this.dutyAmt7 = dutyAmt7;
	}

	public Double getDutyAmt() {
		return dutyAmt;
	}

	public void setDutyAmt(Double dutyAmt) {
		this.dutyAmt = dutyAmt;
	}

	public Double getTotalDuty() {
		return totalDuty;
	}

	public void setTotalDuty(Double totalDuty) {
		this.totalDuty = totalDuty;
	}

	public String getDutyDase() {
		return dutyDase;
	}

	public void setDutyDase(String dutyDase) {
		this.dutyDase = dutyDase;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getExporterId() {
		return exporterId;
	}

	public void setExporterId(String exporterId) {
		this.exporterId = exporterId;
	}

	public String getExWhNo() {
		return exWhNo;
	}

	public void setExWhNo(String exWhNo) {
		this.exWhNo = exWhNo;
	}

	public String getImWhNo() {
		return imWhNo;
	}

	public void setImWhNo(String imWhNo) {
		this.imWhNo = imWhNo;
	}	

}