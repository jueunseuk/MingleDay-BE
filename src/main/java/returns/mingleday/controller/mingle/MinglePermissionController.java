package returns.mingleday.controller.mingle;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import returns.mingleday.flow.mingle.UpdatePermissionFlow;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.mingle.MingleMemberPermissionResponse;
import returns.mingleday.model.mingle.MinglePermissionRequest;
import returns.mingleday.response.success.SuccessResponse;
import returns.mingleday.service.mingle.MinglePermissionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingles/{mingleId}/members")
public class MinglePermissionController {

    private final UpdatePermissionFlow updatePermissionFlow;
    private final MinglePermissionService minglePermissionService;

    @PatchMapping("/{mingleMemberId}")
    public ResponseEntity<SuccessResponse<String>> updatePermission(
            @AuthenticationPrincipal AuthUserDetail user,
            @PathVariable Integer mingleId,
            @PathVariable Long mingleMemberId,
            @RequestBody MinglePermissionRequest request
            ) {
        updatePermissionFlow.updatePermission(user.getUserId(), request, mingleId, mingleMemberId);
        return ResponseEntity.ok(SuccessResponse.success("success to update mingle member permission"));
    }

    @GetMapping
    public ResponseEntity<List<MingleMemberPermissionResponse>> getMingleMemberPermissions(
        @AuthenticationPrincipal AuthUserDetail user,
        @PathVariable Integer mingleId
    ) {
        List<MingleMemberPermissionResponse> response = minglePermissionService.getMemberPermissions(user.getUserId(), mingleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{mingleMemberId}")
    public ResponseEntity<MingleMemberPermissionResponse> getMingleMemberPermission(
        @AuthenticationPrincipal AuthUserDetail user,
        @PathVariable Integer mingleId,
        @PathVariable Long mingleMemberId
    ) {
        MingleMemberPermissionResponse response = minglePermissionService.getMemberPermission(user.getUserId(), mingleId, mingleMemberId);
        return ResponseEntity.ok(response);
    }
}
