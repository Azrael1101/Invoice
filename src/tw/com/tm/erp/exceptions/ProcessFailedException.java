package tw.com.tm.erp.exceptions;

public class ProcessFailedException extends Exception {
    
    private static final long serialVersionUID = 7959218062135047102L;

    public ProcessFailedException() {

    }

    public ProcessFailedException(String message) {
        
        super(message);
    }

    public ProcessFailedException(String message, Throwable cause) {

        super(message, cause);
    }

    public ProcessFailedException(Throwable cause) {

        super(cause);
    }

}
