package returns.mingleday.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import returns.mingleday.flow.authentication.password.ResetPasswordFlow;
import returns.mingleday.flow.authentication.signup.EmailLoginFlow;
import returns.mingleday.flow.authentication.signup.EmailSignupFlow;
import returns.mingleday.model.auth.LoginRequest;
import returns.mingleday.model.auth.PasswordResetRequest;
import returns.mingleday.model.auth.SignupRequest;
import returns.mingleday.model.auth.SignupResponse;
import returns.mingleday.model.email.EmailCodeRequest;
import returns.mingleday.model.email.EmailMatchRequest;
import returns.mingleday.response.success.SuccessResponse;
import returns.mingleday.service.user.MailService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final MailService mailService;
    private final EmailSignupFlow emailSignupFlow;
    private final EmailLoginFlow emailLoginFlow;
    private final ResetPasswordFlow resetPasswordFlow;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        SignupResponse signupResponse = emailSignupFlow.emailSignup(request);
        return ResponseEntity.ok(signupResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<SignupResponse> login(@RequestBody LoginRequest request) {
        SignupResponse signupResponse = emailLoginFlow.emailLogin(request);
        return ResponseEntity.ok(signupResponse);
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
