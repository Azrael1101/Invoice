package tw.com.tm.erp.exceptions;

public class StorageException extends Exception {
   
    private static final long serialVersionUID = -7367831346715413036L;

    public StorageException() {

    }

    public StorageException(String message) {
        
        super(message);
    }

    public StorageException(String message, Throwable cause) {

        super(message, cause);
    }

    public StorageException(Throwable cause) {

        super(cause);
    }
}
