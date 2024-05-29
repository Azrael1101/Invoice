package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuCompanyId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuBatchConfig implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = 1860447158807212169L;
    private String batchId;
    private String startHours;
    private String endHours;    
    private String reserve1;
    private String reserve2;
    
    private String createBy;
    private Date creationDate;
    private String lastUpdateBy;
    private Date lastUpdateDate;

    // Constructors

    /** default constructor */
    public BuBatchConfig() {
    }

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getStartHours() {
		return startHours;
	}

	public void setStartHours(String startHours) {
		this.startHours = startHours;
	}

	public String getEndHours() {
		return endHours;
	}

	public void setEndHours(String endHours) {
		this.endHours = endHours;
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

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
   
    // Property accessors

   
}