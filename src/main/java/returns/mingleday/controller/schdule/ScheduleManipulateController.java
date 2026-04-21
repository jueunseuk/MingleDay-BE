package returns.mingleday.controller.schdule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import returns.mingleday.flow.schedule.CreateScheduleFlow;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.schedule.CreateScheduleRequest;
import returns.mingleday.model.schedule.DetailScheduleResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingles/{mingleId}/schedules")
public class ScheduleManipulateController {

    private final CreateScheduleFlow createScheduleFlow;

    @PostMapping
    public ResponseEntity<DetailScheduleResponse> createSchedule(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId, @RequestBody CreateScheduleRequest request) {
        DetailScheduleResponse response = createScheduleFlow.createSchedule(user.getUserId(), mingleId, request);
        return ResponseEntity.ok(response);
    }
}
