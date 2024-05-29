package tw.com.tm.erp.hbm.bean;

public class Command implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1934663115189378239L;
    
    public static final String WIN_CLOSE = "WIN-CLOSE";
    
    public static final String HANDLER = "HANDLER";
    public static final String FUNCTION = "FUNCTION";
    
    private String cmd;
    
    private String[] parameters;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }
}
