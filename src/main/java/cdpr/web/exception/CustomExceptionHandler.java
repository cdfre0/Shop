package cdpr.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Class to handle call of exception when Object is not found in repository.
 *
 * @author Jan Michalec
 */
@ControllerAdvice
public class CustomExceptionHandler {

    /**
     * MEthod called to handle Exception thrown.
     *
     * @param objectNotFoundException Exception threw
     * @return ResponseEntity with Exception and Not Found HttpStatus
     */
    @ExceptionHandler(value = {ObjectNotFoundException.class})
    public ResponseEntity<Object> hangleGameNotFoundExpection(
            ObjectNotFoundException objectNotFoundException) {

        CustomException customException = new CustomException(
                HttpStatus.NOT_FOUND,
                objectNotFoundException.getMessage());

        return new ResponseEntity<>(customException, HttpStatus.NOT_FOUND);
    }

}
