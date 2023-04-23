package cdpr.web.exception;

import org.springframework.http.HttpStatus;

/**
 * Class tells what attributes should exception have and return when it is
 * called.
 *
 * @author Jan Michalec
 */
public class GameException {

    /**
     * String message of Exception.
     */
    private final String message;
    /**
     * HttpStatus status of returning exception.
     */
    private final HttpStatus httpStatus;

    public GameException(HttpStatus httpStatus, String message,
            Throwable throwable) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * Gets Http Status of exception.
     *
     * @return HttpStatus
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * Gets message of exception.
     *
     * @return String message
     */
    public String getMessage() {
        return message;
    }

}
