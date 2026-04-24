package returns.mingleday.controller.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import returns.mingleday.global.authentication.AuthUserDetail;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
@Slf4j
public class TestController {
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        log.debug("server live test");
        return ResponseEntity.ok("MingleDay is live!");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal AuthUserDetail user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated user");
        }
        return ResponseEntity.ok(user);
    }
}
