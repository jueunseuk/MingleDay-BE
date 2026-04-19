package returns.mingleday.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import returns.mingleday.response.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum GlobalExceptionCode implements ExceptionCode {
    RESOURCE_NOT_FOUND("GLOBAL_001", "해당 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_VALUE_REQUEST("GLOBAL_002", "유효하지 않은 값 주입 시도입니다.", HttpStatus.BAD_REQUEST),
    FORBIDDEN("GLOBAL_003", "해당 요청울 실행할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INTERNAL_SERVER_ERROR("GLOBAL_004", "서버 내부 에러로 인해 요청을 실행할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
