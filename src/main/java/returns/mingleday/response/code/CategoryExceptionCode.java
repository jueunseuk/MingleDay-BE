package returns.mingleday.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import returns.mingleday.response.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum CategoryExceptionCode implements ExceptionCode {
    COLOR_CODE_MUST_BE_SIX_DIGITS("CATE_001", "색상 코드는 6자리여야합니다.", HttpStatus.BAD_REQUEST),
    TOO_SIMILAR_WITH_COLORS("CATE_002", "텍스트와 배경의 색상이 너무 유사합니다.", HttpStatus.BAD_REQUEST),
    INVALID_COLOR_FORMAT("CATE_003", "색상값 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
