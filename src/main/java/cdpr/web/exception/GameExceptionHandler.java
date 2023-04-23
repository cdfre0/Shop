package cdpr.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Class to handle call of exception when Game is not found in repository.
 * Returns Not Found Http Status with gameException.
 *
 * @author Jan Michalec
 */
@ControllerAdvice
public class GameExceptionHandler {

    @ExceptionHandler(value = {GameNotFoundException.class})
    public ResponseEntity<Object> hangleGameNotFoundExpection(
            GameNotFoundException gameNotFoundException) {

        GameException gameException = new GameException(
                HttpStatus.NOT_FOUND,
                gameNotFoundException.getMessage(),
                gameNotFoundException.getCause());

        return new ResponseEntity<>(gameException, HttpStatus.NOT_FOUND);
    }

}
