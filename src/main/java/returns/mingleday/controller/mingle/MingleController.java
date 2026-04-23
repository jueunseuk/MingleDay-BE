package returns.mingleday.controller.mingle;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import returns.mingleday.flow.mingle.CreateMingleFlow;
import returns.mingleday.flow.mingle.UpdateMingleInfoFlow;
import returns.mingleday.flow.mingle.UpdateMingleProfileFlow;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.mingle.CreateMingleRequest;
import returns.mingleday.model.mingle.CreateMingleResponse;
import returns.mingleday.model.mingle.UpdateMingleRequest;
import returns.mingleday.response.success.SuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingles")
public class MingleController {

    private final CreateMingleFlow createMingleFlow;
    private final UpdateMingleInfoFlow updateMingleInfoFlow;
    private final UpdateMingleProfileFlow updateMingleProfileFlow;

    @PostMapping
    public ResponseEntity<CreateMingleResponse> createMingle(@AuthenticationPrincipal AuthUserDetail user, @RequestBody CreateMingleRequest request) {
        Integer mingleId = createMingleFlow.createMingle(user.getUserId(), request);
        return ResponseEntity.ok(new CreateMingleResponse(mingleId));
    }

    @PatchMapping("/{mingleId}")
    public ResponseEntity<SuccessResponse<String>> updateMingle(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId, @RequestBody UpdateMingleRequest request) {
        updateMingleInfoFlow.updateMingleInfo(user.getUserId(), mingleId, request);
        return ResponseEntity.ok(SuccessResponse.success("Success to update mingle"));
    }

    @PatchMapping("/{mingleId}/profile")
    public ResponseEntity<SuccessResponse<String>> updateMingleProfile(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId, @RequestBody MultipartFile request) {
        updateMingleProfileFlow.updateMingleProfile(user.getUserId(), mingleId, request);
        return ResponseEntity.ok(SuccessResponse.success("Success to update mingle representative profile image"));
    }
}
