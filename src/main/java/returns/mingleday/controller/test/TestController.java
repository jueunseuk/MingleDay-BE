package returns.mingleday.controller.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.user.TestUserResponse;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
@Slf4j
public class TestController {
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        log.info("server live test");
        return ResponseEntity.ok("MingleDay is live!");
    }

    @GetMapping("/me")
    public ResponseEntity<TestUserResponse> whoAmI(@AuthenticationPrincipal AuthUserDetail user) {
        log.info("server authentication test");
        if (user == null) {
            throw new BaseException(GlobalExceptionCode.UNAUTHORIZED);
        }
        return ResponseEntity.ok(new TestUserResponse(user.getUserId()));
    }
}
