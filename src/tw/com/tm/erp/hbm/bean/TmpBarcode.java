package tw.com.tm.erp.hbm.bean;

// 給補條碼暫存匯入用的bean
public class TmpBarcode {
    private String timescope;
    private String itemCode;
//COVER_BY_MACO
    private String beginDate;
    private Long paper;
    public String getBeginDate() {
    	return beginDate;
    }
    public void setBeginDate(String beginDate) {
    	this.beginDate = beginDate;
    }
    public String getTimescope() {
        return timescope;
    }
    public void setTimescope(String timescope) {
        this.timescope = timescope;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public Long getPaper() {
        return paper;
    }
    public void setPaper(Long paper) {
        this.paper = paper;
    }

}
