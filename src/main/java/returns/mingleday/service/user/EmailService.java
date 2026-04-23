package returns.mingleday.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.user.Email;
import returns.mingleday.domain.user.Purpose;
import returns.mingleday.domain.user.User;
import returns.mingleday.global.constant.MailMessageConstant;
import returns.mingleday.repository.EmailRepository;
import returns.mingleday.repository.UserRepository;
import returns.mingleday.response.code.EmailExceptionCode;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.code.UserExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.util.MailUtil;
import returns.mingleday.util.StringMasking;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final MailUtil mailUtil;
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;

    @Transactional
    public void sendVerifyCode(String inputEmail, Purpose purpose) {
        String authCode = generateVerificationCode();

        Optional<User> user = userRepository.findByEmail(inputEmail);
        if(user.isPresent() && purpose == Purpose.REGISTER) {
            log.info("Attempt to signup an existing user - userId: {}", user.get().getUserId());
            throw new BaseException(UserExceptionCode.ALREADY_REGISTERED_EMAIL);
        }

        Email email = Email.of(inputEmail, purpose, authCode);
        emailRepository.save(email);

        String purposeText = switch (purpose) {
            case REGISTER -> "회원가입을 위한 인증 코드입니다.";
            case REISSUE -> "비밀번호 변경을 위한 인증 코드입니다.";
            case WITHDRAWAL -> "회원탈퇴를 위한 인증 코드입니다.";
            default -> "서비스 이용을 위한 인증 코드입니다.";
        };

        mailUtil.sendMail(email.getEmail(), MailMessageConstant.VERIFY_CODE_TITLE(), MailMessageConstant.VERIFY_CODE_CONTENT(purposeText, authCode), purpose);
    }

    @Transactional
    public void verifyCode(String email, String code, Purpose purpose) {
        Email check = emailRepository.findFirstByEmailAndPurpose(email, purpose).orElse(null);

        if(check == null) {
            log.info("Non-existent mail verification - email: {}, purpose: {}", StringMasking.emailMasking(email), purpose);
            throw new BaseException(GlobalExceptionCode.RESOURCE_NOT_FOUND);
        }

        if(check.getIsVerified()) {
            throw new BaseException(EmailExceptionCode.ALREADY_VERIFY_REQUEST);
        }

        if(!check.getCode().equals(code)) {
            log.info("Verify code mismatch - email: {}, correctCode: {}, inputCode: {}, tryCount: {}", StringMasking.emailMasking(email), check.getCode(), code, check.getVerifyCnt());
            throw new BaseException(EmailExceptionCode.AUTHENTICATION_CODE_DOES_NOT_MATCH);
        }

        if(check.getVerifyCnt() > 5) {
            log.warn("Detect suspicious mail code requests - email: {}", email);
            check.expirationProcess();
            throw new BaseException(EmailExceptionCode.EXCEEDED_THE_NUMBER_OF_ATTEMPTS);
        }

        check.verify();
        log.info("Success to verify email - email: {}, purpose: {}, tryCount: {}", StringMasking.emailMasking(email), purpose, check.getVerifyCnt());
    }

    public Email getLatestEmailRequest(String email, Purpose purpose) {
        return emailRepository.findFirstByEmailAndPurpose(email, purpose).orElse(null);
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}
