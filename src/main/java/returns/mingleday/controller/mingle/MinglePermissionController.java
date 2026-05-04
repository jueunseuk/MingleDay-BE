package returns.mingleday.controller.mingle;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import returns.mingleday.flow.mingle.UpdatePermissionFlow;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.mingle.MinglePermissionRequest;
import returns.mingleday.response.success.SuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingles/{mingleId}/members/{mingleMemberId}")
public class MinglePermissionController {

    private final UpdatePermissionFlow updatePermissionFlow;

    @PatchMapping
    public ResponseEntity<SuccessResponse<String>> updatePermission(
            @AuthenticationPrincipal AuthUserDetail user,
            @PathVariable Integer mingleId,
            @PathVariable Long mingleMemberId,
            @RequestBody MinglePermissionRequest request
            ) {
        updatePermissionFlow.updatePermission(user.getUserId(), request, mingleId, mingleMemberId);
        return ResponseEntity.ok(SuccessResponse.success("success to update mingle member permission"));
    }
}
