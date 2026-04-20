package returns.mingleday.controller.mingle;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import returns.mingleday.flow.mingle.MingleMembersResponse;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.service.mingle.MingleMemberService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingle/member")
public class MingleMemberController {

    private final MingleMemberService mingleMemberService;

    @GetMapping("/{mingleId}")
    public ResponseEntity<List<MingleMembersResponse>> getMingleMembers(@AuthenticationPrincipal AuthUserDetail user, @PathVariable("mingleId") Integer mingleId) {
        List<MingleMembersResponse> list = mingleMemberService.getMingleMembers(user.getUserId(), mingleId);
        return ResponseEntity.ok(list);
    }
}
