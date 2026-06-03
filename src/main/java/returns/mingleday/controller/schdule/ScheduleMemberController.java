package returns.mingleday.controller.schdule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import returns.mingleday.flow.schedule.UpdateScheduleMemberFlow;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.schedule.ScheduleMemberRequest;
import returns.mingleday.model.schedule.UpdateScheduleMemberRequest;
import returns.mingleday.response.success.SuccessResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingles/{mingleId}/schedules/{scheduleId}/members")
public class ScheduleMemberController {

    private final UpdateScheduleMemberFlow updateScheduleMemberFlow;

    @PatchMapping
    public ResponseEntity<SuccessResponse<String>> updateScheduleMembers(
        @AuthenticationPrincipal AuthUserDetail user,
        @PathVariable Integer mingleId,
        @PathVariable Long scheduleId,
        @RequestBody List<ScheduleMemberRequest> request
    ) {
        updateScheduleMemberFlow.updateScheduleMembers(user.getUserId(), mingleId, scheduleId, request);
        return ResponseEntity.ok(SuccessResponse.success("Success to update schedule member"));
    }

    @PatchMapping("/{scheduleMemberId}")
    public ResponseEntity<SuccessResponse<String>> updateScheduleMember(
            @AuthenticationPrincipal AuthUserDetail user,
            @PathVariable Integer mingleId,
            @PathVariable Long scheduleId,
            @PathVariable Long scheduleMemberId,
            @RequestBody UpdateScheduleMemberRequest request
    ) {
        updateScheduleMemberFlow.updateScheduleMember(user.getUserId(), mingleId, scheduleId, scheduleMemberId, request);
        return ResponseEntity.ok(SuccessResponse.success("Success to update schedule member"));
    }
}
