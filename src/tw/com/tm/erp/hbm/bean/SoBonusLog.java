package tw.com.tm.erp.hbm.bean;

import java.util.Date;

public class SoBonusLog implements java.io.Serializable {
    private static final long serialVersionUID = 7798344012061471148L;
    private long id;
    private String yearMonth;
    private Date startDate;
    private Date endDate;
    private String createdBy;
    private String status;
    
    //getter...
    public long getId() {
        return id;
    }
    
    public String getYearMonth() {
        return yearMonth;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public String getStatus() {
        return status;
    }

    //setter...
    public void setId(long id) {
        this.id = id;
    }
    
    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
