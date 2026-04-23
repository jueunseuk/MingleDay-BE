package returns.mingleday.flow.authentication.signup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.user.Email;
import returns.mingleday.domain.user.Purpose;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.auth.SignupRequest;
import returns.mingleday.model.auth.TokenResponse;
import returns.mingleday.response.code.UserExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.user.EmailService;
import returns.mingleday.service.user.UserService;
import returns.mingleday.util.JwtTokenProvider;
import returns.mingleday.util.StringMasking;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSignupFlow {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final EmailService emailService;

    @Transactional
    public TokenResponse emailSignup(SignupRequest signupRequest) {
        log.info("Signup Request Occurred - email: {}", signupRequest.getEmail());

        String email = signupRequest.getEmail();
        Email emailRequest = emailService.getLatestEmailRequest(email, Purpose.REISSUE);
        if(emailRequest == null || !emailRequest.getIsVerified()) {
            log.warn("Request to change password for unauthenticated users - email: {}", email);
            throw new BaseException(UserExceptionCode.UNAUTHENTICATED_USER);
        }

        if(emailRequest.getExpiredAt().isBefore(LocalDateTime.now())) {
            log.warn("Request to change password for expired authentication - email: {}", email);
            throw new BaseException(UserExceptionCode.AUTHENTICATED_TIME_EXPIRES);
        }

        User user = userService.createUser(
                signupRequest.getName(),
                signupRequest.getEmail(),
                signupRequest.getPassword(),
                signupRequest.getNickname());

        log.info("Success to Register - userId: {}, email: {}", user.getUserId(), StringMasking.emailMasking(user.getEmail()));

        return new TokenResponse(jwtTokenProvider.createToken(user));
    }
}