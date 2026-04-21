package returns.mingleday.controller.mingle;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.mingle.MingleLogResponse;
import returns.mingleday.model.mingle.MyMingleLogResponse;
import returns.mingleday.service.mingle.log.MingleLogService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingle/log")
public class MingleLogController {

    private final MingleLogService mingleLogService;

    @GetMapping("/all/me")
    public ResponseEntity<List<MyMingleLogResponse>> getAllMingleLog(@AuthenticationPrincipal AuthUserDetail user) {
        List<MyMingleLogResponse> response = mingleLogService.getAllMyMingleLog(user.getUserId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all/{mingleId}")
    public ResponseEntity<List<MingleLogResponse>> getAllMingleLog(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId) {
        List<MingleLogResponse> response = mingleLogService.getAllMingleLog(user.getUserId(), mingleId);
        return ResponseEntity.ok(response);
    }
}
