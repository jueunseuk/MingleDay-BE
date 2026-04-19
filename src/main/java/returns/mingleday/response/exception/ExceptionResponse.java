package returns.mingleday.response.exception;

import lombok.Getter;
import returns.mingleday.response.ApiResponse;

@Getter
public class ExceptionResponse extends ApiResponse {
    public ExceptionResponse(String code, String message) {
        super(false, code, message);
    }
}
