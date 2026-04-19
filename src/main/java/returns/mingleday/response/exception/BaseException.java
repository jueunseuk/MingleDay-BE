package returns.mingleday.response.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final String code;
    private final HttpStatus httpStatus;

    public BaseException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.code = exceptionCode.getCode();
        this.httpStatus = exceptionCode.getStatus();
    }
}
