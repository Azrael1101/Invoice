package tw.com.tm.erp.exceptions;

public class NoSuchDataException extends Exception {
   
    private static final long serialVersionUID = 15106436856468423L;

    public NoSuchDataException() {

    }

    public NoSuchDataException(String message) {
        
        super(message);
    }

    public NoSuchDataException(String message, Throwable cause) {

        super(message, cause);
    }

    public NoSuchDataException(Throwable cause) {

        super(cause);
    }
}
