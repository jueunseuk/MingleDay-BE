package returns.mingleday.response.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import returns.mingleday.response.code.GlobalExceptionCode;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleBaseException(BaseException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getCode(), e.getMessage()), e.getHttpStatus());
    }
}
