package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SiMenu entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiMenuCtrl implements java.io.Serializable {
	// Fields
	private static final long serialVersionUID = -4312303994853625355L;
	private String menuId;
	private Double lineNo;
	private String systemType;
	private String functionType;
	private String type;
	private String name;
	private String functionCode;
	private String url;
	private String parentMenuId;
	private String parentMenuName;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String updatedBy;
	private Date updateDate;
	//private String owner;
	private int indexNo;			//not for hibernate 新增權限用
	private String enable;			//not for hibernate 新增權限用
    private String companyCode;
    private String reportCode;
    private String reportType;
    private String itemCategory;
    private String categoryType;
    private String warehouseControl;
    private String costControl;
    private String rptBrand;;
	// Constructors

	/** default constructor */
	public SiMenuCtrl() {
	}

	/** minimal constructor */
	public SiMenuCtrl(String menuId) {
		this.menuId = menuId;
	}

	/** full constructor */


	// Property accessors

	public String getMenuId() {
		return this.menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public Double getLineNo() {
		return this.lineNo;
	}

	public void setLineNo(Double lineNo) {
		this.lineNo = lineNo;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getFunctionType() {
		return functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFunctionCode() {
		return this.functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentMenuId() {
		return this.parentMenuId;
	}

	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
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

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
/*
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
*/	
	public String getParentMenuName() {
		return parentMenuName;
	}

	public void setParentMenuName(String parentMenuName) {
		this.parentMenuName = parentMenuName;
	}

	public int getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getWarehouseControl() {
		return warehouseControl;
	}

	public void setWarehouseControl(String warehouseControl) {
		this.warehouseControl = warehouseControl;
	}

	public String getCostControl() {
		return costControl;
	}

	public void setCostControl(String costControl) {
		this.costControl = costControl;
	}

	public String getRptBrand() {
		return rptBrand;
	}

	public void setRptBrand(String rptBrand) {
		this.rptBrand = rptBrand;
	}
}