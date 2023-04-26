package cdpr.web.exception;

/**
 * Class ObjectNotFoundException extends RuntimeException and uses its
 * constructor.
 *
 * @author Jan Michalec
 */
public class ObjectNotFoundException extends RuntimeException {

    /**
     * Constructor with message passed to super.
     *
     * @param message String message to set
     */
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
