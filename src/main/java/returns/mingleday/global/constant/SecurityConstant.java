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
            "/api/v1/auth/signup",
            "/api/v1/auth/login",
            "/api/v1/auth/email/*",
            "/api/v1/test/ping"
    );
}