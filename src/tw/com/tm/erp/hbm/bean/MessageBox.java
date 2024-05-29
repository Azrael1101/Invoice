package tw.com.tm.erp.hbm.bean;

public class MessageBox implements java.io.Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -5226102122269089965L;
    
    public static final String ALERT = "ALERT";
    
    public static final String CONFIRM = "CONFIRM";
    
    public static final String PROMPT = "PROMPT";
       
    private String type;
    
    private String message;
    
    private Command before;
    
    private Command ok;
    
    private Command cancel;
    
    public MessageBox(){
	type = ALERT;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Command getBefore() {
        return before;
    }

    public void setBefore(Command before) {
        this.before = before;
    }

    public Command getOk() {
        return ok;
    }

    public void setOk(Command ok) {
        this.ok = ok;
    }

    public Command getCancel() {
        return cancel;
    }

    public void setCancel(Command cancel) {
        this.cancel = cancel;
    }
}
