package returns.mingleday.flow.authentication.password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.user.Email;
import returns.mingleday.domain.user.Purpose;
import returns.mingleday.domain.user.Status;
import returns.mingleday.domain.user.User;
import returns.mingleday.repository.UserRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.code.UserExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.user.MailService;
import returns.mingleday.util.StringMasking;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResetPasswordFlow {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Transactional
    public void resetPassword(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(UserExceptionCode.NOT_EXIST_USER));

        if (user.getStatus() == Status.WITHDRAWN) {
            throw new BaseException(GlobalExceptionCode.INVALID_REQUEST);
        }

        Email emailRequest = mailService.getLatestEmailRequest(email, Purpose.REISSUE);
        if(emailRequest == null || !emailRequest.getIsVerified()) {
            log.warn("Request to change password for unauthenticated users - email: {}", email);
            throw new BaseException(UserExceptionCode.UNAUTHENTICATED_USER);
        }

        if(emailRequest.getExpiredAt().isBefore(LocalDateTime.now())) {
            log.warn("Request to change password for expired authentication - email: {}", email);
            throw new BaseException(UserExceptionCode.AUTHENTICATED_TIME_EXPIRES);
        }

        User.isValidPassword(password);
        user.resetPassword(passwordEncoder.encode(password));

        log.info("Request to reset password success - userId: {}, email: {}", user.getUserId(), StringMasking.emailMasking(email));
    }
}
