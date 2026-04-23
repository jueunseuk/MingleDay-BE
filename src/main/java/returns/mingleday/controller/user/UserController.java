package returns.mingleday.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import returns.mingleday.flow.user.profile.UpdateProfileImageFlow;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.schedule.MonthlyScheduleResponse;
import returns.mingleday.model.user.UpdateProfileInfoRequest;
import returns.mingleday.response.success.SuccessResponse;
import returns.mingleday.service.schedule.ScheduleSearchService;
import returns.mingleday.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final ScheduleSearchService scheduleSearchService;
    private final UpdateProfileImageFlow updateProfileImageFlow;

    @PostMapping("/profile/image")
    public ResponseEntity<String> updateProfileImage(@AuthenticationPrincipal AuthUserDetail user, @RequestBody MultipartFile profileImage) {
        String imageUrl = updateProfileImageFlow.updateProfileImage(user.getUserId(), profileImage);
        return ResponseEntity.ok(imageUrl);
    }

    @PatchMapping("/profile/info")
    public ResponseEntity<SuccessResponse<String>> updateProfileInfo(@AuthenticationPrincipal AuthUserDetail user, @RequestBody UpdateProfileInfoRequest request) {
        userService.updateProfileInfo(user.getUserId(), request);
        return ResponseEntity.ok(SuccessResponse.success("Success to update profile info"));
    }

    @GetMapping("/schedules/me")
    public ResponseEntity<List<MonthlyScheduleResponse>> getSchedule(
            @AuthenticationPrincipal AuthUserDetail user,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam String keyword
    ) {
        List<MonthlyScheduleResponse> response = scheduleSearchService.findMySchedules(user.getUserId(), year, month, keyword);
        return ResponseEntity.ok(response);
    }
}
