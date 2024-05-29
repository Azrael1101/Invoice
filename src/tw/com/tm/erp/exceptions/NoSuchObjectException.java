package tw.com.tm.erp.exceptions;

public class NoSuchObjectException extends FormException {
   
    private static final long serialVersionUID = 6705377207585086114L;

    public NoSuchObjectException() {

    }

    public NoSuchObjectException(String message) {
        
        super(message);
    }

    public NoSuchObjectException(String message, Throwable cause) {

        super(message, cause);
    }

    public NoSuchObjectException(Throwable cause) {

        super(cause);
    }
}
