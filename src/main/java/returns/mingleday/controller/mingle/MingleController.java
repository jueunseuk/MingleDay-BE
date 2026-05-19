package returns.mingleday.controller.mingle;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import returns.mingleday.flow.mingle.*;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.mingle.*;
import returns.mingleday.response.success.SuccessResponse;
import returns.mingleday.service.mingle.MingleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingles")
public class MingleController {

    private final MingleService mingleService;
    private final CreateMingleFlow createMingleFlow;
    private final UpdateMingleInfoFlow updateMingleInfoFlow;
    private final UpdateMingleProfileFlow updateMingleProfileFlow;
    private final UpdateMingleSettingFlow updateMingleSettingFlow;
    private final SearchMingleInformationFlow searchMingleInformationFlow;

    @PostMapping
    public ResponseEntity<CreateMingleResponse> createMingle(@AuthenticationPrincipal AuthUserDetail user, @RequestBody CreateMingleRequest request) {
        Integer mingleId = createMingleFlow.createMingle(user.getUserId(), request);
        return ResponseEntity.ok(new CreateMingleResponse(mingleId));
    }

    @PatchMapping("/{mingleId}/info")
    public ResponseEntity<SuccessResponse<String>> updateMingle(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId, @RequestBody UpdateMingleRequest request) {
        updateMingleInfoFlow.updateMingleInfo(user.getUserId(), mingleId, request);
        return ResponseEntity.ok(SuccessResponse.success("Success to update mingle"));
    }

    @PatchMapping("/{mingleId}/profile")
    public ResponseEntity<SuccessResponse<String>> updateMingleProfile(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId, @RequestBody MultipartFile request) {
        updateMingleProfileFlow.updateMingleProfile(user.getUserId(), mingleId, request);
        return ResponseEntity.ok(SuccessResponse.success("Success to update mingle representative profile image"));
    }

    @PatchMapping("/{mingleId}/setting")
    public ResponseEntity<SuccessResponse<String>> updateMingleSetting(
            @AuthenticationPrincipal AuthUserDetail user,
            @PathVariable Integer mingleId,
            @RequestParam String option,
            @RequestParam Boolean value
    ) {
        updateMingleSettingFlow.updateMingleSetting(user.getUserId(), mingleId, option, value);
        return ResponseEntity.ok(SuccessResponse.success("Success to update mingle setting"));
    }

    @GetMapping
    public ResponseEntity<List<MinglesResponse>> getAllMyMingles(@AuthenticationPrincipal AuthUserDetail user) {
        List<MinglesResponse> responses = mingleService.getMinglesResponseByUser(user.getUserId());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{mingleId}")
    public ResponseEntity<MingleResponse> getMingle(@AuthenticationPrincipal AuthUserDetail user, @PathVariable Integer mingleId) {
        MingleResponse response = searchMingleInformationFlow.searchMingleInformation(user.getUserId(), mingleId);
        return ResponseEntity.ok(response);
    }
}
