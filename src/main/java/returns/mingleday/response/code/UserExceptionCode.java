package returns.mingleday.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import returns.mingleday.response.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum UserExceptionCode implements ExceptionCode {
    NOT_EXIST_USER("USER_001", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    INACTIVE_USER("USER_002", "현재 서비스를 이용할 수 없는 상태입니다.", HttpStatus.BAD_REQUEST),
    AUTHENTICATED_TIME_EXPIRES("USER_003", "요청 가능 시간이 만료됐습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
