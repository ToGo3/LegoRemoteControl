package wrapper;

public class SmartSpaceException extends Exception {
    public SmartSpaceException(String message) {
        super(message);
    }

    public SmartSpaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmartSpaceException(Throwable cause) {
        super(cause);
    }

    public SmartSpaceException() {

    }
}