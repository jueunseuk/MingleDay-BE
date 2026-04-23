package returns.mingleday.controller.mingle;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import returns.mingleday.flow.mingle.KickMingleMemberFlow;
import returns.mingleday.flow.mingle.LeaveMingleMemberFlow;
import returns.mingleday.model.mingle.MingleMemberResponse;
import returns.mingleday.model.mingle.MingleMembersResponse;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.response.success.SuccessResponse;
import returns.mingleday.service.mingle.MingleMemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingles/{mingleId}/members")
public class MingleMemberController {

    private final MingleMemberService mingleMemberService;
    private final KickMingleMemberFlow kickMingleMemberFlow;
    private final LeaveMingleMemberFlow leaveMingleMemberFlow;

    @GetMapping("/{mingleMemberId}")
    public ResponseEntity<MingleMemberResponse> getMingleMember(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId, @PathVariable Long mingleMemberId) {
        MingleMemberResponse response = mingleMemberService.getMingleMember(user.getUserId(), mingleId, mingleMemberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MingleMembersResponse>> getMingleMembers(@AuthenticationPrincipal AuthUserDetail user, @PathVariable("mingleId") Integer mingleId) {
        List<MingleMembersResponse> list = mingleMemberService.getMingleMembers(user.getUserId(), mingleId);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{mingleMemberId}")
    public ResponseEntity<SuccessResponse<String>> kickMingleMember(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId, @PathVariable Long mingleMemberId) {
        kickMingleMemberFlow.kickMingleMember(user.getUserId(), mingleId, mingleMemberId);
        return ResponseEntity.ok(SuccessResponse.success("Success to kick mingle member"));
    }

    @DeleteMapping("/leave")
    public ResponseEntity<SuccessResponse<String>> leaveMingleMember(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId) {
        leaveMingleMemberFlow.leaveMingleMember(user.getUserId(), mingleId);
        return ResponseEntity.ok(SuccessResponse.success("Success to leave mingle member"));
    }
}
