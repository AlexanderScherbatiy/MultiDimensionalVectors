package multidimensionalvectors.core;

public class MDException extends RuntimeException {

    public MDException(String message) {
        super(message);
    }

    public MDException(String message, Throwable cause) {
        super(message, cause);
    }
}
