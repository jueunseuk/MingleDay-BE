package returns.mingleday.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import returns.mingleday.response.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum ScheduleExceptionCode implements ExceptionCode {
    ALLOWED_ONLY_SAMEDAY("SCHE_002", "반복 일정은 날짜의 범위를 설정할 수 없습니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
