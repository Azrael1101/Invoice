package tw.com.tm.erp.exceptions;

public class NoRequiredParameterException extends Exception {
   
    private static final long serialVersionUID = 3602755470015252771L;

    public NoRequiredParameterException() {

    }

    public NoRequiredParameterException(String message) {
        
        super(message);
    }

    public NoRequiredParameterException(String message, Throwable cause) {

        super(message, cause);
    }

    public NoRequiredParameterException(Throwable cause) {

        super(cause);
    }
}
