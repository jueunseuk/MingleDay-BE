package returns.mingleday.controller.schdule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.schedule.DailyScheduleResponse;
import returns.mingleday.model.schedule.DetailScheduleResponse;
import returns.mingleday.model.schedule.MonthlyScheduleResponse;
import returns.mingleday.service.schedule.ScheduleSearchService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingles/{mingleId}/schedules")
public class ScheduleSearchController {

    private final ScheduleSearchService scheduleSearchService;

    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyScheduleResponse>> getMonthlySchedules(
            @AuthenticationPrincipal AuthUserDetail user,
            @PathVariable Integer mingleId,
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        List<MonthlyScheduleResponse> response = scheduleSearchService.findMonthlySchedules(user.getUserId(), mingleId, year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/daily")
    public ResponseEntity<List<DailyScheduleResponse>> getDailySchedules(
            @AuthenticationPrincipal AuthUserDetail user,
            @PathVariable Integer mingleId,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam Integer day
    ) {
        List<DailyScheduleResponse> response = scheduleSearchService.findDailySchedules(user.getUserId(), mingleId, year, month, day);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/instances/{scheduleInstanceId}")
    public ResponseEntity<DetailScheduleResponse> getSchedule(
            @AuthenticationPrincipal AuthUserDetail user,
            @PathVariable Integer mingleId,
            @PathVariable Long scheduleInstanceId
    ) {
        DetailScheduleResponse response = scheduleSearchService.getDetailSchedules(user.getUserId(), mingleId, scheduleInstanceId);
        return ResponseEntity.ok(response);
    }
}
