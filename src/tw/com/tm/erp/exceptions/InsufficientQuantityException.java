package tw.com.tm.erp.exceptions;

public class InsufficientQuantityException extends FormException {
    
    private static final long serialVersionUID = -1531677687210732165L;

    public InsufficientQuantityException() {

    }

    public InsufficientQuantityException(String message) {
        
        super(message);
    }

    public InsufficientQuantityException(String message, Throwable cause) {

        super(message, cause);
    }

    public InsufficientQuantityException(Throwable cause) {

        super(cause);
    }

}
