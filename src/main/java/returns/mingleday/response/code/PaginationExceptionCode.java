package returns.mingleday.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import returns.mingleday.response.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum PaginationExceptionCode implements ExceptionCode {
    CANNOT_USE_NEGATIVE_PARAMETER("PAGE_001", "음수 인자를 전달할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_USE_DIRECTION_VALUE("PAGE_002", "해당 방향으로 설정할 수 없습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
