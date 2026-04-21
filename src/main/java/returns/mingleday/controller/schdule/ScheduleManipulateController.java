package returns.mingleday.controller.schdule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import returns.mingleday.domain.schedule.ScheduleStatus;
import returns.mingleday.flow.schedule.CreateScheduleFlow;
import returns.mingleday.flow.schedule.DeleteScheduleFlow;
import returns.mingleday.flow.schedule.UpdateScheduleFlow;
import returns.mingleday.flow.schedule.UpdateScheduleInstanceFlow;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.schedule.*;
import returns.mingleday.response.success.SuccessResponse;
import returns.mingleday.service.schedule.ScheduleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingles/{mingleId}/schedules")
public class ScheduleManipulateController {

    private final ScheduleService scheduleService;
    private final CreateScheduleFlow createScheduleFlow;
    private final DeleteScheduleFlow deleteScheduleFlow;
    private final UpdateScheduleFlow updateScheduleFlow;
    private final UpdateScheduleInstanceFlow updateScheduleInstanceFlow;

    @PostMapping
    public ResponseEntity<DetailScheduleResponse> createSchedule(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId, @RequestBody CreateScheduleRequest request) {
        DetailScheduleResponse response = createScheduleFlow.createSchedule(user.getUserId(), mingleId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{scheduleId}")
    public ResponseEntity<DetailScheduleResponse> updateSchedule(
            @AuthenticationPrincipal AuthUserDetail user,
            @PathVariable Integer mingleId,
            @PathVariable Long scheduleId,
            @RequestBody UpdateScheduleRequest request
    ) {
        DetailScheduleResponse response = updateScheduleFlow.updateSchedule(
                user.getUserId(), mingleId, scheduleId, request
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{scheduleId}/instances/{scheduleInstanceId}")
    public ResponseEntity<DetailScheduleResponse> updateScheduleInstance(
            @AuthenticationPrincipal AuthUserDetail user,
            @PathVariable Integer mingleId,
            @PathVariable Long scheduleId,
            @PathVariable Long scheduleInstanceId,
            @RequestBody UpdateScheduleInstanceRequest request
    ) {
        DetailScheduleResponse response = updateScheduleInstanceFlow.updateScheduleInstance(
                user.getUserId(), mingleId, scheduleId, scheduleInstanceId, request
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{scheduleId}/instances/{scheduleInstanceId}/status")
    public ResponseEntity<SuccessResponse<String>> updateScheduleInstanceStatus(
            @AuthenticationPrincipal AuthUserDetail user,
            @PathVariable Integer mingleId,
            @PathVariable Long scheduleId,
            @PathVariable Long scheduleInstanceId,
            @RequestBody ScheduleStatus status
    ) {
        scheduleService.updateStatus(user.getUserId(), mingleId, scheduleId, scheduleInstanceId, status);
        return ResponseEntity.ok(SuccessResponse.success("Success to change schedule instance status"));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<SuccessResponse<String>> deleteSchedule(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId, @PathVariable Long scheduleId) {
        deleteScheduleFlow.deleteSchedule(user.getUserId(), mingleId, scheduleId);
        return ResponseEntity.ok(SuccessResponse.success("Success to delete schedule contains all instances"));
    }
}
