package returns.mingleday.controller.mingle;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import returns.mingleday.flow.mingle.CreateMingleFlow;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.mingle.CreateMingleRequest;
import returns.mingleday.model.mingle.CreateMingleResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingle")
public class MingleController {

    private final CreateMingleFlow createMingleFlow;

    @PostMapping
    public ResponseEntity<CreateMingleResponse> createMingle(@AuthenticationPrincipal AuthUserDetail user, @RequestBody CreateMingleRequest request) {
        Integer mingleId = createMingleFlow.createMingle(user.getUserId(), request);
        return ResponseEntity.ok(new CreateMingleResponse(mingleId));
    }
}
