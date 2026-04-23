package returns.mingleday.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import returns.mingleday.response.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum MinglePermissionExceptionCode implements ExceptionCode {
    DISABLED_THE_PERMISSION_FUNCTION("MING_PER_001", "해당 밍글은 권한 기능을 사용하지 않도록 설정했습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
