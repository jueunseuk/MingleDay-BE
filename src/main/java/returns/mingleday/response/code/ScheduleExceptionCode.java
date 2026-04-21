package returns.mingleday.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import returns.mingleday.response.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum ScheduleExceptionCode implements ExceptionCode {
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
