package returns.mingleday.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import returns.mingleday.response.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum EmailExceptionCode implements ExceptionCode {
    INCORRECT_MAIL_CODE_PURPOSE("EMAIL_001", "메일 전송 목적이 부적합합니다.", HttpStatus.BAD_REQUEST),
    FAILED_TO_SEND_CODE("EMAIL_002", "메일을 보내는 데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    AUTHENTICATION_CODE_DOES_NOT_MATCH("EMAIL_003", "메일 인증 코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_VERIFY_REQUEST("EMAIL_004", "이미 인증을 완료한 메일 요청입니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
