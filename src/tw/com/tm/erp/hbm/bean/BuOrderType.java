package tw.com.tm.erp.hbm.bean;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * BuOrderType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuOrderType implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7180702959620135526L;
    
    // Fields
    private BuOrderTypeId id;
    private String name;
    private String nextOrderTypeCode;
    private String sequenceType;
    private String typeCode;
    private String monthlyBalanceTypeCode;
    private String lastOrderNo;
    private String closeType;
    private String priceType;
    private String stockControl;
    private String taxCode;
    private String reportFunctionCode;
    private String reportFileName;
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
    private List buOrderTypeApprovals = new ArrayList(0);
    private String cmMoveWarehouseFrom;
    private String cmMoveWarehouseTo;
    private String accountingPostPeriod;
    private String moveFlowType;
    private String moveSignType;
    private String mailTo;
    private String orderCondition;

    // Constructors

    /** default constructor */
    public BuOrderType() {
    }

    /** minimal constructor */
    public BuOrderType(BuOrderTypeId id, String name) {
	this.id = id;
	this.name = name;
    }

	/** full constructor */

    public BuOrderType(BuOrderTypeId id, String name, String nextOrderTypeCode,
			String sequenceType, String typeCode,
			String monthlyBalanceTypeCode, String lastOrderNo,
			String closeType, String priceType, String stockControl,
			String taxCode, String reportFunctionCode, String reportFileName,
			String enable, String reserve1, String reserve2, String reserve3,
			String reserve4, String reserve5, String createdBy,
			Date creationDate, String lastUpdatedBy, Date lastUpdateDate,
			List buOrderTypeApprovals, String cmMoveWarehouseFrom,
			String cmMoveWarehouseTo) {
		this.id = id;
		this.name = name;
		this.nextOrderTypeCode = nextOrderTypeCode;
		this.sequenceType = sequenceType;
		this.typeCode = typeCode;
		this.monthlyBalanceTypeCode = monthlyBalanceTypeCode;
		this.lastOrderNo = lastOrderNo;
		this.closeType = closeType;
		this.priceType = priceType;
		this.stockControl = stockControl;
		this.taxCode = taxCode;
		this.reportFunctionCode = reportFunctionCode;
		this.reportFileName = reportFileName;
		this.enable = enable;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.buOrderTypeApprovals = buOrderTypeApprovals;
		this.cmMoveWarehouseFrom = cmMoveWarehouseFrom;
		this.cmMoveWarehouseTo = cmMoveWarehouseTo;
	}

    // Property accessors

    public BuOrderTypeId getId() {
	return this.id;
    }

    public void setId(BuOrderTypeId id) {
	this.id = id;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }
    
    public String getNextOrderTypeCode() {
        return nextOrderTypeCode;
    }

    public void setNextOrderTypeCode(String nextOrderTypeCode) {
        this.nextOrderTypeCode = nextOrderTypeCode;
    }

    public String getSequenceType() {
	return this.sequenceType;
    }

    public void setSequenceType(String sequenceType) {
	this.sequenceType = sequenceType;
    }

    public String getTypeCode() {
	return this.typeCode;
    }

    public void setTypeCode(String typeCode) {
	this.typeCode = typeCode;
    }

    public String getMonthlyBalanceTypeCode() {
        return monthlyBalanceTypeCode;
    }

    public void setMonthlyBalanceTypeCode(String monthlyBalanceTypeCode) {
        this.monthlyBalanceTypeCode = monthlyBalanceTypeCode;
    }
    
    public String getLastOrderNo() {
	return this.lastOrderNo;
    }

    public void setLastOrderNo(String lastOrderNo) {
	this.lastOrderNo = lastOrderNo;
    }

    public String getCloseType() {
        return closeType;
    }

    public void setCloseType(String closeType) {
        this.closeType = closeType;
    }
    
    public String getPriceType() {
	return this.priceType;
    }

    public void setPriceType(String priceType) {
	this.priceType = priceType;
    }

    public void setStockControl(String stockControl) {
		this.stockControl = stockControl;
	}

	public String getStockControl() {
		return stockControl;
	}

	public String getTaxCode() {
	return this.taxCode;
    }

    public void setTaxCode(String taxCode) {
	this.taxCode = taxCode;
    }
    
    public String getReportFunctionCode() {
        return reportFunctionCode;
    }

    public void setReportFunctionCode(String reportFunctionCode) {
        this.reportFunctionCode = reportFunctionCode;
    }

    public String getReportFileName() {
        return reportFileName;
    }

    public void setReportFileName(String reportFileName) {
        this.reportFileName = reportFileName;
    }

    public String getEnable() {
	return this.enable;
    }

    public void setEnable(String enable) {
	this.enable = enable;
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

    public List getBuOrderTypeApprovals() {
	return this.buOrderTypeApprovals;
    }

    public void setBuOrderTypeApprovals(List buOrderTypeApprovals) {
	this.buOrderTypeApprovals = buOrderTypeApprovals;
    }

	public String getCmMoveWarehouseFrom() {
		return cmMoveWarehouseFrom;
	}

	public void setCmMoveWarehouseFrom(String cmMoveWarehouseFrom) {
		this.cmMoveWarehouseFrom = cmMoveWarehouseFrom;
	}

	public String getCmMoveWarehouseTo() {
		return cmMoveWarehouseTo;
	}

	public void setCmMoveWarehouseTo(String cmMoveWarehouseTo) {
		this.cmMoveWarehouseTo = cmMoveWarehouseTo;
	}
	public String getAccountingPostPeriod() {
		return accountingPostPeriod;
	}

	public void setAccountingPostPeriod(String accountingPostPeriod) {
		this.accountingPostPeriod = accountingPostPeriod;
	}

	public String getMoveFlowType() {
		return moveFlowType;
	}

	public void setMoveFlowType(String moveFlowType) {
		this.moveFlowType = moveFlowType;
	}

	public String getMoveSignType() {
		return moveSignType;
	}

	public void setMoveSignType(String moveSignType) {
		this.moveSignType = moveSignType;
	}

	public String getMailTo() {
	    return mailTo;
	}

	public void setMailTo(String mailTo) {
	    this.mailTo = mailTo;
	}

	public String getOrderCondition() {
		return orderCondition;
	}

	public void setOrderCondition(String orderCondition) {
		this.orderCondition = orderCondition;
	}
}