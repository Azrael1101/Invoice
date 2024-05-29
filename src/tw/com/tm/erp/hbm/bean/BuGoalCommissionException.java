package tw.com.tm.erp.hbm.bean;

import java.util.Date;

public class BuGoalCommissionException implements java.io.Serializable {
	
	private static final long serialVersionUID = 4528142567910111604L;
	private Long lineId;
//	private BuGoalCommission headId;
	private String commissionType;
	private String shopCode;
	private String itemBrand;
	private String category01;
	private String category02;
	private String category03;
	private String category04;
	private String category05;
	private String category06;
	private String category07;
	private String category08;
	private String category09;
	private String category10;
	private String category11;
	private String category12;
	private String category13;
	private String category14;
	private String category15;
	private String category16;
	private String category17;
	private String category18;
	private String category19;
	private String category20;
	private Double commissionRate;
	private String lastUpdatedBy;
	private String lastUpdatedByName;
	private Date lastUpdateDate;
	
	public BuGoalCommissionException (){
		
	};
	
	public BuGoalCommissionException(Long lineId) {
		this.lineId = lineId;
	}
	
	public BuGoalCommissionException(Long lineId, String commissionType,String shopCode,String itemBrand,String category01 ,
			String category02,String category03 ,String category04,String category05,String category06 ,
			String category07 ,String category08 ,String category09 ,String category10 ,String category11 ,
			String category12,String category13  ,String category14 ,String category15 ,String category16 ,String category17 ,
			String category18,String category19 ,String category20 ,Double commissionRate, 
			String lastUpdatedBy, Date lastUpdateDate, String lastUpdatedByName){
		super();
		this.lineId = lineId;
		this.commissionType= commissionType;
		this.shopCode = shopCode;
		this.itemBrand =itemBrand;
		this.category01 = category01;
		this.category02 = category02;
		this.category03 = category03;
		this.category04 = category04;
		this.category05 = category05;
		this.category06 = category06;
		this.category07 = category07;
		this.category08 = category08;
		this.category09 = category09;
		this.category10 = category10;
		this.category11 = category11;
		this.category12 = category12;
		this.category13 = category13;
		this.category14 = category14;
		this.category15 = category15;
		this.category16 = category16;
		this.category17 = category17;
		this.category18 = category18;
		this.category19 = category19;
		this.category20 = category20;
		this.commissionRate = commissionRate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedByName = lastUpdatedByName;
	}
	
	
	
	
	
	// Property accessors
	
	public Long getLineId(){
		return this.lineId;
	}
	
	public void setLineId(Long lineId){
		this.lineId = lineId;
	}
	
	
/*	public BuGoalCommission getBuGoalCommission(){
		return this.headId;
	}
	
	public void setBuGoalCommission(BuGoalCommission headId){
		this.headId = headId;
	}
*/	
	public String getCommissionType(){
		return this.commissionType;
	}
	
	public void setCommissionType(String commissionType){
		this.commissionType=commissionType;
	}
	
	public String getShopCode(){
		return this.shopCode;
	}
	
	public void setShopCode(String shopCode){
		this.shopCode = shopCode;
	}
	
	public String getItemBrand(){
		return this.itemBrand;
	}
	
	public void setItemBrand(String itemBrand){
		this.itemBrand=itemBrand;
	}
	
	public String getCategory01(){
		return this.category01;
	}
	
	public void setCategory01(String category01){
		this.category01 = category01;
	}
	
	public String getCategory02(){
		return this.category02;
	}
	
	public void setCategory02(String category02){
		this.category02 = category02;
	}
	
	public String getCategory03(){
		return this.category03;
	}
	
	public void setCategory03(String category03){
		this.category03 = category03;
	}
	
	public String getCategory04(){
		return this.category04;
	}
	
	public void setCategory04(String category04){
		this.category04 = category04;
	}
	
	public String getCategory05(){
		return this.category05;
	}
	
	public void setCategory05(String category05){
		this.category05 = category05;
	}
	
	public String getCategory06(){
		return this.category06;
	}
	
	public void setCategory06(String category06){
		this.category06 = category06;
	}
	
	public String getCategory07(){
		return this.category07;
	}
	
	public void setCategory07(String category07){
		this.category07 = category07;
	}
	
	public String getCategory08(){
		return this.category08;
	}
	
	public void setCategory08(String category08){
		this.category08 = category08;
	}
	
	public String getCategory09(){
		return this.category09;
	}
	
	public void setCategory09(String category09){
		this.category09 = category09;
	}
	
	public String getCategory10(){
		return this.category10;
	}
	
	public void setCategory10(String category10){
		this.category10 = category10;
	}
	
	public String getCategory11(){
		return this.category11;
	}
	
	public void setCategory11(String category11){
		this.category11 = category11;
	}
	
	public String getCategory12(){
		return this.category12;
	}
	
	public void setCategory12(String category12){
		this.category12 = category12;
	}
	
	public String getCategory13(){
		return this.category13;
	}
	
	public void setCategory13(String category13){
		this.category13 = category13;
	}
	
	public String getCategory14(){
		return this.category14;
	}
	
	public void setCategory14(String category14){
		this.category14 = category14;
	}
	
	public String getCategory15(){
		return this.category15;
	}
	
	public void setCategory15(String category15){
		this.category15 = category15;
	}
	
	public String getCategory16(){
		return this.category16;
	}
	
	public void setCategory16(String category16){
		this.category16 = category16;
	}
	
	public String getCategory17(){
		return this.category17;
	}
	
	public void setCategory17(String category17){
		this.category17 = category17;
	}
	
	public String getCategory18(){
		return this.category18;
	}
	
	public void setCategory18(String category18){
		this.category18 = category18;
	}
	
	public String getCategory19(){
		return this.category19;
	}
	
	public void setCategory19(String category19){
		this.category19 = category19;
	}
	
	public String getCategory20(){
		return this.category20;
	}
	
	public void setCategory20(String category20){
		this.category20 = category20;
	}
	
	public Double getCommissionRate(){
		return this.commissionRate;
	}
	
	public void setCommissionRate(Double commissionRate){
		this.commissionRate = commissionRate;
	}
	
	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdatedByName() {
		return lastUpdatedByName;
	}

	public void setLastUpdatedByName(String lastUpdatedByName) {
		this.lastUpdatedByName = lastUpdatedByName;
	}
	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	
	
}
