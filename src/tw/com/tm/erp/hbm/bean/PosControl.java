package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;

public class PosControl implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3044928248619121956L;
    private String brandCode;
    private String dataType;
    private String machineCode;
    private String description;
    private String executeType;
    private Long frequence;
    private String transfer;
    private Long indexNo;
    private Long timeout;
    private String remark;
    
    public String getBrandCode() {
        return brandCode;
    }
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public String getMachineCode() {
        return machineCode;
    }
    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getExecuteType() {
        return executeType;
    }
    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }
    public Long getFrequence() {
        return frequence;
    }
    public void setFrequence(Long frequence) {
        this.frequence = frequence;
    }
    public String getTransfer() {
        return transfer;
    }
    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }
    public Long getIndexNo() {
        return indexNo;
    }
    public void setIndexNo(Long indexNo) {
        this.indexNo = indexNo;
    }
    public Long getTimeout() {
        return timeout;
    }
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
