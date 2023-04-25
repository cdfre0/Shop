package cdpr.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Class to handle call of exception when Object is not found in repository.
 * Returns Not Found Http Status with CustomException.
 *
 * @author Jan Michalec
 */
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = {ObjectNotFoundException.class})
    public ResponseEntity<Object> hangleGameNotFoundExpection(
            ObjectNotFoundException objectNotFoundException) {

        CustomException customException = new CustomException(
                HttpStatus.NOT_FOUND,
                objectNotFoundException.getMessage(),
                objectNotFoundException.getCause());

        return new ResponseEntity<>(customException, HttpStatus.NOT_FOUND);
    }

}
