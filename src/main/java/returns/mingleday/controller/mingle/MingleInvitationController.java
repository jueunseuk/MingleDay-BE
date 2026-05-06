package returns.mingleday.controller.mingle;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import returns.mingleday.flow.mingle.InviteMingleFlow;
import returns.mingleday.flow.mingle.ResponseMingleInvitationFlow;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.mingle.InviteMingleRequest;
import returns.mingleday.model.mingle.MingleInvitationResponse;
import returns.mingleday.response.success.SuccessResponse;
import returns.mingleday.service.mingle.MingleInvitationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingles/invitation")
public class MingleInvitationController {

    private final InviteMingleFlow inviteMingleFlow;
    private final ResponseMingleInvitationFlow responseMingleInvitationFlow;
    private final MingleInvitationService mingleInvitationService;

    @PostMapping
    public ResponseEntity<SuccessResponse<String>> createMingleInvitation(@AuthenticationPrincipal AuthUserDetail user, @RequestBody InviteMingleRequest request) {
        inviteMingleFlow.inviteMingle(user.getUserId(), request);
        return ResponseEntity.ok(SuccessResponse.success("Success to create a mingle invitation"));
    }

    @GetMapping("/accept")
    public ResponseEntity<String> responseMingleInvitation(@RequestParam String token) {
        String response = responseMingleInvitationFlow.responseMingleInvitation(token);
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MingleInvitationResponse>> getAllMingleInvitation(@AuthenticationPrincipal AuthUserDetail user) {
        List<MingleInvitationResponse> response = mingleInvitationService.getAllMingleInvitations(user.getUserId());
        return ResponseEntity.ok(response);
    }
}
