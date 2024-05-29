package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SiMenu entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiMenuView implements java.io.Serializable {
	// Fields
	private static final long serialVersionUID = -4312303994853625355L;
	private SiMenuViewId id;
	private String parentMenuId;
	private String lineNo;
	private String systemType;
	private String functionType;
	private String type;
	private String name;
	private String functionCode;
	private String url;
	private String rptBrand;
	private String costControl;
	private String warehouseControl;
	private String categoryType;
	private String itemCategory;
	private String reportType;
	private String reportCode;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String updatedBy;
	private Date updateDate;
	
	// Constructors

	/** default constructor */
	public SiMenuView() {
	}

	/** minimal constructor */
	public SiMenuView(SiMenuViewId id) {
		this.id = id;
	}
	
	public SiMenuViewId getId() {
		return this.id;
	}

	public void setId(SiMenuViewId id) {
		this.id = id;
	}

	public String getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
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
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRptBrand() {
		return rptBrand;
	}

	public void setRptBrand(String rptBrand) {
		this.rptBrand = rptBrand;
	}

	public String getCostControl() {
		return costControl;
	}

	public void setCostControl(String costControl) {
		this.costControl = costControl;
	}

	public String getWarehouseControl() {
		return warehouseControl;
	}

	public void setWarehouseControl(String warehouseControl) {
		this.warehouseControl = warehouseControl;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}