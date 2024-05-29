package tw.com.tm.erp.exceptions;

public class ObtainSerialNoFailedException extends Exception {
    
    private static final long serialVersionUID = 7959218062135047102L;

    public ObtainSerialNoFailedException() {

    }

    public ObtainSerialNoFailedException(String message) {
        
        super(message);
    }

    public ObtainSerialNoFailedException(String message, Throwable cause) {

        super(message, cause);
    }

    public ObtainSerialNoFailedException(Throwable cause) {

        super(cause);
    }

}
