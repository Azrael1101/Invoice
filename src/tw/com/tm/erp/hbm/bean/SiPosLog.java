package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoPostingTally entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiPosLog implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1392344103340696432L;
    // Fields
    private SiPosLogId id;
    private String brandCode;
    private String headFileName;
    private String lineFileName;
    private String status;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date createDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    


    // Constructors

    /** default constructor */
    public SiPosLog() {
    }


    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
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

    public Date getCreateDate() {
	return this.createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
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


	public SiPosLogId getId() {
		return id;
	}


	public void setId(SiPosLogId id) {
		this.id = id;
	}


	public String getHeadFileName() {
		return headFileName;
	}


	public void setHeadFileName(String headFileName) {
		this.headFileName = headFileName;
	}


	public String getLineFileName() {
		return lineFileName;
	}


	public void setLineFileName(String lineFileName) {
		this.lineFileName = lineFileName;
	}

	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

}