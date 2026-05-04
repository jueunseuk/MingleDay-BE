package returns.mingleday.flow.authentication.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import returns.mingleday.domain.user.Status;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.auth.LoginRequest;
import returns.mingleday.model.auth.TokenResponse;
import returns.mingleday.repository.UserRepository;
import returns.mingleday.response.code.UserExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.util.JwtTokenProvider;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailLoginFlow {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse emailLogin(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BaseException(UserExceptionCode.NOT_EXIST_USER));

        if (user.getStatus() != Status.ACTIVE) {
            throw new BaseException(UserExceptionCode.INACTIVE_USER);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BaseException(UserExceptionCode.PASSWORD_DOES_NOT_MATCH);
        }

        user.login();
        log.info("Success to update profile info - userId : {}", user.getUserId());

        return new TokenResponse(jwtTokenProvider.createToken(user));
    }
}