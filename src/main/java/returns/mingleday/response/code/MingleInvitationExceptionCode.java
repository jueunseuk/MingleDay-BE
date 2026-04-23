package returns.mingleday.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import returns.mingleday.response.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum MingleInvitationExceptionCode implements ExceptionCode {
    NOT_INVITED_USER_TO_THIS_MINGLE("MING_INV_001", "해당 밍글에 초대 받지 않은 사용자입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_JOIN_OR_REJECT_MINGLE("MING_INV_002", "이미 가입했거나 거절한 밍글입니다.", HttpStatus.BAD_REQUEST),;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
