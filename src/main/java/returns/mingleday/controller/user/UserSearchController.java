package returns.mingleday.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.schedule.SearchScheduleInstanceResponse;
import returns.mingleday.service.schedule.ScheduleSearchService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class UserSearchController {

    private final ScheduleSearchService scheduleSearchService;

    @GetMapping
    public ResponseEntity<List<SearchScheduleInstanceResponse>> searchUserSchedule(@AuthenticationPrincipal AuthUserDetail user, @RequestParam String keyword) {
        List<SearchScheduleInstanceResponse> response = scheduleSearchService.searchByKeyword(user.getUserId(), keyword);
        return ResponseEntity.ok(response);
    }
}
