package cdpr.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author Jan Michalec
 */
@ControllerAdvice 
public class GameExceptionHandler {

    @ExceptionHandler(value = {GameNotFoundException.class})
    public ResponseEntity<Object> hangleGameNotFoundExpection(GameNotFoundException gameNotFoundException) {

        GameException gameException = new GameException(
                gameNotFoundException.getMessage(),
                gameNotFoundException.getCause(),
                HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(gameException, HttpStatus.NOT_FOUND);
    }
    public ResponseEntity<Object> handleIncorrectGameFormatException(IncorrectGameFormatException incorrectGameFormatException) {
        GameException gameException = new GameException(
                incorrectGameFormatException.getMessage(),
                incorrectGameFormatException.getCause(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        return new ResponseEntity<>(gameException, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

}
