package tw.com.tm.erp.hbm.bean;

public class ImReplenishBasicParameter implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -4130702763589068717L;
    private ImReplenishBasicParameterId id;
    private Long value;
    
    public ImReplenishBasicParameterId getId() {
        return id;
    }

    public void setId(ImReplenishBasicParameterId id) {
        this.id = id;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
