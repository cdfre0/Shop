package cdpr.web.exception;

/**
 * Class GameNotFoundException extends RuntimeException and uses its
 * constructors.
 *
 * @author Jan Michalec
 */
public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(String message) {
        super(message);
    }

    public GameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
