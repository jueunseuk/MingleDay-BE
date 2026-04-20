package returns.mingleday.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import returns.mingleday.flow.user.profile.UpdateProfileImageFlow;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.user.UpdateProfileInfoRequest;
import returns.mingleday.response.success.SuccessResponse;
import returns.mingleday.service.user.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final UpdateProfileImageFlow updateProfileImageFlow;

    @PostMapping("/profile/image")
    public ResponseEntity<String> updateProfileImage(@AuthenticationPrincipal AuthUserDetail user, @RequestBody MultipartFile profileImage) {
        String imageUrl = updateProfileImageFlow.updateProfileImage(user.getUserId(), profileImage);
        return ResponseEntity.ok(imageUrl);
    }

    @PostMapping("/profile/info")
    public ResponseEntity<SuccessResponse<String>> updateProfileInfo(@AuthenticationPrincipal AuthUserDetail user, @RequestBody UpdateProfileInfoRequest request) {
        userService.updateProfileInfo(user.getUserId(), request);
        return ResponseEntity.ok(SuccessResponse.success("Success to update profile info"));
    }
}
