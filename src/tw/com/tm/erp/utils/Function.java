package tw.com.tm.erp.utils;

import java.io.Serializable;

public class Function implements Serializable{

    /**
     *  
     */
    private static final long serialVersionUID = 2631879407966127559L;

    private String functionName;

    private String functionURL;

    public Function(String functionName, String functionURL) {
	this.functionName = functionName;
	this.functionURL = functionURL;
    }

    public String getFunctionName() {
	return functionName;
    }

    public String getFunctionURL() {
	return functionURL;
    }

}
