package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;

public class PosMachineSale implements Serializable {
    
    private Long headId;
    private String dataId;
    private String action;
    private String category01;
    private String classId;
    private String itemBrand;
    private String itemCode;
    private String machineCode;
    private String status;

    public Long getHeadId() {
        return headId;
    }
    public void setHeadId(Long headId) {
        this.headId = headId;
    }
    public String getDataId() {
        return dataId;
    }
    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getClassId() {
        return classId;
    }
    public void setClassId(String classId) {
        this.classId = classId;
    }
    public String getItemBrand() {
        return itemBrand;
    }
    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getMachineCode() {
        return machineCode;
    }
    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCategory01() {
        return category01;
    }
    public void setCategory01(String category01) {
        this.category01 = category01;
    }
}
