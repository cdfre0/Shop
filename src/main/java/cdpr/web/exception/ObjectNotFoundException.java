package cdpr.web.exception;

/**
 * Class ObjectNotFoundException extends RuntimeException and uses its
 * constructors.
 *
 * @author Jan Michalec
 */
public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
