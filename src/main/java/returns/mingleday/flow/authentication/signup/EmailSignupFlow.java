package returns.mingleday.flow.authentication.signup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.users.User;
import returns.mingleday.model.auth.SignupRequest;
import returns.mingleday.model.auth.SignupResponse;
import returns.mingleday.response.code.UserExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.users.UserService;
import returns.mingleday.util.JwtTokenProvider;
import returns.mingleday.util.Masking;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSignupFlow {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Transactional
    public SignupResponse emailSignup(SignupRequest signupRequest) {
        log.info("Signup Request Occurred - email: {}", signupRequest.getEmail());

        if(!signupRequest.getAuthenticated()) {
            throw new BaseException(UserExceptionCode.UNAUTHENTICATED_USER);
        }

        User user = userService.createUser(
                signupRequest.getName(),
                signupRequest.getEmail(),
                signupRequest.getPassword(),
                signupRequest.getNickname());

        log.info("Success to Register - userId: {}, email: {}", user.getUserId(), Masking.emailMasking(user.getEmail()));

        return new SignupResponse(jwtTokenProvider.createToken(user));
    }
}