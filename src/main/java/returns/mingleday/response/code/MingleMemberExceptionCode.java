package returns.mingleday.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import returns.mingleday.response.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum MingleMemberExceptionCode implements ExceptionCode {
    OWNER_OF_MINGLE_CANNOT_BE_EXPELLED("MING_MEM_001", "밍글 소유자는 추방할 수 없습니다", HttpStatus.FORBIDDEN),
    OWNER_CANNOT_LEAVE("MING_MEM_002", "밍글 소유자는 탈퇴할 수 없습니다.", HttpStatus.BAD_REQUEST),
    YOU_CANNOT_EXPEL_YOURSELF("MING_MEM_003", "자기 자신은 추방할 수 없습니다.", HttpStatus.BAD_REQUEST),;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
