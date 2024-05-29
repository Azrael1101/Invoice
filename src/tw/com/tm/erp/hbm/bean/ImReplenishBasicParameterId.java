package tw.com.tm.erp.hbm.bean;

/**
 * ImReplenishBasicParameterId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImReplenishBasicParameterId implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = -6336681799405930636L;
    private String brandCode;
    private String type;
    private Double parameter;
    

    // Constructors

    /** default constructor */
    public ImReplenishBasicParameterId() {
    }

    /** full constructor */
    public ImReplenishBasicParameterId(String brandCode, String type,
	    Double parameter) {
	this.brandCode = brandCode;
	this.type = type;
	this.parameter = parameter;
    }

    // Property accessors

    public String getBrandCode() {
	return this.brandCode;
    }

    public void setBrandCode(String brandCode) {
	this.brandCode = brandCode;
    }

    public String getType() {
	return this.type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public Double getParameter() {
	return this.parameter;
    }

    public void setParameter(Double parameter) {
	this.parameter = parameter;
    }

}