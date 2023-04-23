package cdpr.web.exception;

/**
 *
 * @author Jan Michalec
 */
public class IncorrectGameFormatException extends RuntimeException {

    public IncorrectGameFormatException(String message) {
        super(message);
    }

    public IncorrectGameFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}