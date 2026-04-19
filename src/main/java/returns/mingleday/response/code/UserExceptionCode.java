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
    ALREADY_REGISTERED_EMAIL("USER_004", "이미 가입된 이메일입니다.", HttpStatus.CONFLICT),
    PASSWORD_DOES_NOT_MATCH("USER_005", "패스워드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED_USER("USER_006", "이메일이 인증되지 않은 사용자입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
