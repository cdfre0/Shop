package cdpr.web.exception;

import org.springframework.http.HttpStatus;

/**
 *
 * @author Jan Michalec
 */
public class GameException {

    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;

    public GameException(HttpStatus httpStatus,String message,
            Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    

}
