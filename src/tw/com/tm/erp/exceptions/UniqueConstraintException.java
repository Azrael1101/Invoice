package tw.com.tm.erp.exceptions;

public class UniqueConstraintException extends FormException {
   
    private static final long serialVersionUID = 3186533310027262206L;

    public UniqueConstraintException() {

    }

    public UniqueConstraintException(String message) {
        
        super(message);
    }

    public UniqueConstraintException(String message, Throwable cause) {

        super(message, cause);
    }

    public UniqueConstraintException(Throwable cause) {

        super(cause);
    }
}
