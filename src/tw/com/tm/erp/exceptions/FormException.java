package tw.com.tm.erp.exceptions;

public class FormException extends Exception {
   
    private static final long serialVersionUID = -7367831346715413036L;

    public FormException() {

    }

    public FormException(String message) {
        
        super(message);
    }

    public FormException(String message, Throwable cause) {

        super(message, cause);
    }

    public FormException(Throwable cause) {

        super(cause);
    }
}
