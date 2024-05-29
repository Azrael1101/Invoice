package tw.com.tm.erp.exceptions;

public class ValidationErrorException extends FormException {

    /**
     * 
     */
    private static final long serialVersionUID = 8854854059953584908L;

    public ValidationErrorException() {

    }

    public ValidationErrorException(String message) {
        
        super(message);
    }

    public ValidationErrorException(String message, Throwable cause) {

        super(message, cause);
    }

    public ValidationErrorException(Throwable cause) {

        super(cause);
    }
}
