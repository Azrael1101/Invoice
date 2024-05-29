package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuBrand entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuBrand implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = -7201585256994886103L;
    private String brandCode;
    private String branchCode;
    private BuBranch buBranch;
    private String brandName;
    private String description;
    private String lotControl;
    private String dailyCloseDate;
    private String monthlyCloseMonth;
    private String monthlyBalanceMonth;
    private String dailyBalancing;
    private String dailyClosing;
    private String monthlyBalancing;
    private String monthlyClosing;
    private String itemLevelControl;
    private String defaultWarehouseCode1;
    private String defaultWarehouseCode2;
    private String defaultWarehouseCode3;
    private String defaultWarehouseCode4;
    private String defaultWarehouseCode5;
    private String reportTitle;
    private String enable;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private String isSelected;
    private Long indexNo;
    private String defaultWarehouseCode6;
    private String defaultWarehouseCode11;
    private String defaultWarehouseCode21;
    private String defaultWarehouseCode31;
    private String defaultWarehouseCode41;
    private String defaultWarehouseCode51;
    private String defaultWarehouseCode61;
    private String defaultWarehouseCode7;
    private String defaultWarehouseCode71;
    private String budgetType;
    private String budgetCheckType;
    private String cmMonth;
    private String businessMode;
    private String cmDate;
    private String dailyBalanceDate;
    private String schedule;
    private String enforcedClose;
    
    
    // Constructors
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public BuBranch getBuBranch() {
		return buBranch;
	}
	public void setBuBranch(BuBranch buBranch) {
		this.buBranch = buBranch;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLotControl() {
		return lotControl;
	}
	public void setLotControl(String lotControl) {
		this.lotControl = lotControl;
	}
	public String getDailyCloseDate() {
		return dailyCloseDate;
	}
	public void setDailyCloseDate(String dailyCloseDate) {
		this.dailyCloseDate = dailyCloseDate;
	}
	public String getMonthlyCloseMonth() {
		return monthlyCloseMonth;
	}
	public void setMonthlyCloseMonth(String monthlyCloseMonth) {
		this.monthlyCloseMonth = monthlyCloseMonth;
	}
	public String getMonthlyBalanceMonth() {
		return monthlyBalanceMonth;
	}
	public void setMonthlyBalanceMonth(String monthlyBalanceMonth) {
		this.monthlyBalanceMonth = monthlyBalanceMonth;
	}
	public String getDailyBalancing() {
		return dailyBalancing;
	}
	public void setDailyBalancing(String dailyBalancing) {
		this.dailyBalancing = dailyBalancing;
	}
	public String getDailyClosing() {
		return dailyClosing;
	}
	public void setDailyClosing(String dailyClosing) {
		this.dailyClosing = dailyClosing;
	}
	public String getMonthlyBalancing() {
		return monthlyBalancing;
	}
	public void setMonthlyBalancing(String monthlyBalancing) {
		this.monthlyBalancing = monthlyBalancing;
	}
	public String getMonthlyClosing() {
		return monthlyClosing;
	}
	public void setMonthlyClosing(String monthlyClosing) {
		this.monthlyClosing = monthlyClosing;
	}
	public String getItemLevelControl() {
		return itemLevelControl;
	}
	public void setItemLevelControl(String itemLevelControl) {
		this.itemLevelControl = itemLevelControl;
	}
	public String getDefaultWarehouseCode1() {
		return defaultWarehouseCode1;
	}
	public void setDefaultWarehouseCode1(String defaultWarehouseCode1) {
		this.defaultWarehouseCode1 = defaultWarehouseCode1;
	}
	public String getDefaultWarehouseCode2() {
		return defaultWarehouseCode2;
	}
	public void setDefaultWarehouseCode2(String defaultWarehouseCode2) {
		this.defaultWarehouseCode2 = defaultWarehouseCode2;
	}
	public String getDefaultWarehouseCode3() {
		return defaultWarehouseCode3;
	}
	public void setDefaultWarehouseCode3(String defaultWarehouseCode3) {
		this.defaultWarehouseCode3 = defaultWarehouseCode3;
	}
	public String getDefaultWarehouseCode4() {
		return defaultWarehouseCode4;
	}
	public void setDefaultWarehouseCode4(String defaultWarehouseCode4) {
		this.defaultWarehouseCode4 = defaultWarehouseCode4;
	}
	public String getDefaultWarehouseCode5() {
		return defaultWarehouseCode5;
	}
	public void setDefaultWarehouseCode5(String defaultWarehouseCode5) {
		this.defaultWarehouseCode5 = defaultWarehouseCode5;
	}
	public String getReportTitle() {
		return reportTitle;
	}
	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getReserve1() {
		return reserve1;
	}
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	public String getReserve2() {
		return reserve2;
	}
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	public String getReserve3() {
		return reserve3;
	}
	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}
	public String getReserve4() {
		return reserve4;
	}
	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}
	public String getReserve5() {
		return reserve5;
	}
	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}
	public Long getIndexNo() {
		return indexNo;
	}
	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}
	public String getDefaultWarehouseCode6() {
		return defaultWarehouseCode6;
	}
	public void setDefaultWarehouseCode6(String defaultWarehouseCode6) {
		this.defaultWarehouseCode6 = defaultWarehouseCode6;
	}
	public String getDefaultWarehouseCode11() {
		return defaultWarehouseCode11;
	}
	public void setDefaultWarehouseCode11(String defaultWarehouseCode11) {
		this.defaultWarehouseCode11 = defaultWarehouseCode11;
	}
	public String getDefaultWarehouseCode21() {
		return defaultWarehouseCode21;
	}
	public void setDefaultWarehouseCode21(String defaultWarehouseCode21) {
		this.defaultWarehouseCode21 = defaultWarehouseCode21;
	}
	public String getDefaultWarehouseCode31() {
		return defaultWarehouseCode31;
	}
	public void setDefaultWarehouseCode31(String defaultWarehouseCode31) {
		this.defaultWarehouseCode31 = defaultWarehouseCode31;
	}
	public String getDefaultWarehouseCode41() {
		return defaultWarehouseCode41;
	}
	public void setDefaultWarehouseCode41(String defaultWarehouseCode41) {
		this.defaultWarehouseCode41 = defaultWarehouseCode41;
	}
	public String getDefaultWarehouseCode51() {
		return defaultWarehouseCode51;
	}
	public void setDefaultWarehouseCode51(String defaultWarehouseCode51) {
		this.defaultWarehouseCode51 = defaultWarehouseCode51;
	}
	public String getDefaultWarehouseCode61() {
		return defaultWarehouseCode61;
	}
	public void setDefaultWarehouseCode61(String defaultWarehouseCode61) {
		this.defaultWarehouseCode61 = defaultWarehouseCode61;
	}
	public String getDefaultWarehouseCode7() {
		return defaultWarehouseCode7;
	}
	public void setDefaultWarehouseCode7(String defaultWarehouseCode7) {
		this.defaultWarehouseCode7 = defaultWarehouseCode7;
	}
	public String getDefaultWarehouseCode71() {
		return defaultWarehouseCode71;
	}
	public void setDefaultWarehouseCode71(String defaultWarehouseCode71) {
		this.defaultWarehouseCode71 = defaultWarehouseCode71;
	}
	public String getBudgetType() {
		return budgetType;
	}
	public void setBudgetType(String budgetType) {
		this.budgetType = budgetType;
	}
	public String getBudgetCheckType() {
		return budgetCheckType;
	}
	public void setBudgetCheckType(String budgetCheckType) {
		this.budgetCheckType = budgetCheckType;
	}
	public String getCmMonth() {
		return cmMonth;
	}
	public void setCmMonth(String cmMonth) {
		this.cmMonth = cmMonth;
	}
	public String getBusinessMode() {
		return businessMode;
	}
	public void setBusinessMode(String businessMode) {
		this.businessMode = businessMode;
	}
	public String getCmDate() {
		return cmDate;
	}
	public void setCmDate(String cmDate) {
		this.cmDate = cmDate;
	}
	
	public String getDailyBalanceDate(){
		return dailyBalanceDate;
	}
	
	public void setDailyBalanceDate(String dailyBalanceDate){
		this.dailyBalanceDate = dailyBalanceDate;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	/**
	 * @return the enforcedClose
	 */
	public String getEnforcedClose() {
		return enforcedClose;
	}
	/**
	 * @param enforcedClose the enforcedClose to set
	 */
	public void setEnforcedClose(String enforcedClose) {
		this.enforcedClose = enforcedClose;
	}
	
	
}