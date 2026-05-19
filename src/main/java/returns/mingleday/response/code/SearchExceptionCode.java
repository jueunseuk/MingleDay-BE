package returns.mingleday.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import returns.mingleday.response.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum SearchExceptionCode implements ExceptionCode {
    TOO_SHORT_KEYWORD("SEARCH_001", "검색하려는 키워드가 너무 짧습니다.", HttpStatus.BAD_REQUEST),
    NO_MINGLE_EXISTS_TO_RETRIEVE_THE_SCHEDULE("SEARCH_002", "일정을 검색할 밍글이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
