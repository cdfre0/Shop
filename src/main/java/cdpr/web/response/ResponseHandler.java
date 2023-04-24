package cdpr.web.response;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Class to handle output of response. Response contains httpStatus and message
 * with data returned from repository.
 *
 * @author Jan Michalec
 */
public class ResponseHandler {

    public static ResponseEntity<Object> responseBuilder(
            String message, HttpStatus httpStatus, Object result) {
        Map<String, Object> response = new HashMap<>();
        response.put("httpStatus", httpStatus);
        response.put("message", message);
        response.put("data", result);

        return new ResponseEntity<>(response, httpStatus);
    }
}
