package returns.mingleday.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import returns.mingleday.flow.authentication.password.ResetPasswordFlow;
import returns.mingleday.flow.authentication.login.EmailLoginFlow;
import returns.mingleday.flow.authentication.signup.EmailSignupFlow;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.model.auth.LoginRequest;
import returns.mingleday.model.auth.PasswordResetRequest;
import returns.mingleday.model.auth.SignupRequest;
import returns.mingleday.model.auth.TokenResponse;
import returns.mingleday.model.email.EmailCodeRequest;
import returns.mingleday.model.email.EmailMatchRequest;
import returns.mingleday.response.success.SuccessResponse;
import returns.mingleday.service.user.AuthService;
import returns.mingleday.service.user.MailService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final MailService mailService;
    private final EmailSignupFlow emailSignupFlow;
    private final EmailLoginFlow emailLoginFlow;
    private final ResetPasswordFlow resetPasswordFlow;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<TokenResponse> signup(@RequestBody SignupRequest request) {
        TokenResponse tokenResponse = emailSignupFlow.emailSignup(request);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = emailLoginFlow.emailLogin(request);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<String>> logout(@AuthenticationPrincipal AuthUserDetail user) {
        authService.logout(user.getUserId());
        return ResponseEntity.ok(SuccessResponse.success("Verify for logout request"));
    }

    @PostMapping("/email/send")
    public ResponseEntity<SuccessResponse<String>> mailSend(@RequestBody EmailCodeRequest request){
        mailService.sendMail(request.getEmail(), request.getPurpose());
        return ResponseEntity.ok(SuccessResponse.success("Success to request authentication code"));
    }

    @PostMapping("/email/verify")
    public ResponseEntity<SuccessResponse<String>> signupCodeCheck(@RequestBody EmailMatchRequest request) {
        mailService.verifyCode(request.getEmail(), request.getCode(), request.getPurpose());
        return ResponseEntity.ok(SuccessResponse.success("Matches with authentication code"));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<SuccessResponse<String>> passwordReset(@RequestBody PasswordResetRequest request) {
        resetPasswordFlow.resetPassword(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(SuccessResponse.success("Success to reset password"));
    }
}
