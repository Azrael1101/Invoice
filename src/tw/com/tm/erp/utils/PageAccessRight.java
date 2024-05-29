package tw.com.tm.erp.utils;

import java.io.Serializable;

public class PageAccessRight implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3004659625145903948L;

    private String objectCode;
    
    private String objectName;
    
    private String objectType;
    
    private String controlType;

    
    public PageAccessRight(String objectCode, String objectName, String objectType, String controlType){
	this.objectCode = objectCode;
	this.objectName = objectName;
	this.objectType = objectType;
	this.controlType = controlType;
    }
    
    public String getObjectCode() {
        return objectCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getControlType() {
        return controlType;
    }

}
