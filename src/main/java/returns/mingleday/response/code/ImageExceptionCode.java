package returns.mingleday.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import returns.mingleday.response.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum ImageExceptionCode implements ExceptionCode {
    IMAGE_UPLOAD_FAILED("IMAGE_001", "이미지 업로드 중 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_SIZE_EXCEEDED("IMAGE_002", "이미지의 크기가 제한을 초과했습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
