package returns.mingleday.response;

import lombok.Getter;

@Getter
public abstract class ApiResponse {
    private final Boolean success;
    private final String code;
    private final String message;

    protected ApiResponse(Boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
