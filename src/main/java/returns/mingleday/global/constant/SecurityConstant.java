package returns.mingleday.global.constant;

import java.util.List;

public class SecurityConstant {
    public static final List<String> ALLOWED_ORIGINS = List.of(
        "http://localhost:8080"
    );

    public static final List<String> ALLOWED_METHODS = List.of(
            "GET", "POST", "PUT", "DELETE", "PATCH"
    );

    public static final List<String> ALLOWED_HEADERS = List.of(
            "Authorization", "Content-Type", "Accept", "Origin"
    );

    public static final List<String> PERMIT_ENDPOINTS = List.of(
            ""
    );
}