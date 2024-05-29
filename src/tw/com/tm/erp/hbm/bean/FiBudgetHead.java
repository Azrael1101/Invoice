package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * FiBudgetHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FiBudgetHead implements java.io.Serializable {

	// Fields
	public static final String BUDGET_CHECK_TYPE_YEAR = "1" ; 
	public static final String BUDGET_CHECK_TYPE_MONTH = "2" ;
	public static final String BUDGET_ORDER_TYPE_CODE_PO =	"PO";
	public static final String BUDGET_ORDER_TYPE_CODE_SO =	"SO";
	/**
	 * 
	 */
	private static final long serialVersionUID = 2952493218977418304L;
	private Long headId;
	private String brandCode;
	private String budgetYear;
	private String budgetMonth;
	private String description;
	private String budgetCheckType;
	private Double totalBudget = new Double(0);
	private Double totalAppliedBudget = new Double(0);
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String orderTypeCode;
	private List<FiBudgetLine> fiBudgetLines = new ArrayList(0);
	private String status ;
	private Double totalSigningBudget = new Double(0);
	private String itemType ;
	private Double totalForecastAmount = new Double(0);
	private Double totalReturnedBudget = new Double(0);
	
	// Constructors
	/** default constructor */
	public FiBudgetHead() {
	}

	/** minimal constructor */
	public FiBudgetHead(Long headId) {
		this.headId = headId;
	}
	
	// Utils
	public String getItemTypeName() {
		return itemType == null ? "" : itemType.equals("0") ? "非正貨類" : 
			itemType.equals("1") ? "菸酒巧克力" : itemType.equals("2") ? "精品" : 
			itemType.equals("3") ? "化妝品" : itemType.equals("4") ? "台產品" : 
			itemType.equals("5") ? "3C影音圖書" : itemType.equals("6") ? "公益" : "";
	}
	
	// Property accessors
	public String getItemType() {
	    return itemType;
	}

	public void setItemType(String itemType) {
	    this.itemType = itemType;
	}

	public Double getTotalSigningBudget() {
		return totalSigningBudget;
	}

	public void setTotalSigningBudget(Double totalSigningBudget) {
		this.totalSigningBudget = totalSigningBudget;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
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

	public String getBudgetYear() {
		return this.budgetYear;
	}

	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBudgetCheckType() {
		return this.budgetCheckType;
	}

	public void setBudgetCheckType(String budgetCheckType) {
		this.budgetCheckType = budgetCheckType;
	}

	public Double getTotalBudget() {
		return this.totalBudget;
	}

	public void setTotalBudget(Double totalBudget) {
		this.totalBudget = totalBudget;
	}

	public Double getTotalAppliedBudget() {
		return this.totalAppliedBudget;
	}

	public void setTotalAppliedBudget(Double totalAppliedBudget) {
		this.totalAppliedBudget = totalAppliedBudget;
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

	public String getOrderTypeCode() {
		return this.orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public List<FiBudgetLine> getFiBudgetLines() {
		return fiBudgetLines;
	}

	public void setFiBudgetLines(List<FiBudgetLine> fiBudgetLines) {
		this.fiBudgetLines = fiBudgetLines;
	}

	public String getBudgetMonth() {
	    return budgetMonth;
	}

	public void setBudgetMonth(String budgetMonth) {
	    this.budgetMonth = budgetMonth;
	}

	public Double getTotalForecastAmount() {
		return totalForecastAmount;
	}

	public void setTotalForecastAmount(Double totalForecastAmount) {
		this.totalForecastAmount = totalForecastAmount;
	}

	public Double getTotalReturnedBudget() {
		return totalReturnedBudget;
	}

	public void setTotalReturnedBudget(Double totalReturnedBudget) {
		this.totalReturnedBudget = totalReturnedBudget;
	}

}